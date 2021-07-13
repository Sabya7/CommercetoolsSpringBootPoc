package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.extension.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vrap.rmf.base.client.ApiHttpResponse;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ExtensionsExploration {

    @Autowired
    private ApiRoot client;

    @GetMapping("/creExt")
    public ApiHttpResponse<Extension> createExtension() throws ExecutionException, InterruptedException {

        ExtensionDestination destination = ExtensionHttpDestinationBuilder.of()

                .url("https://804e02bb0387ba.localhost.run/extension")
                .build();

        ExtensionTrigger trigger = ExtensionTriggerBuilder.of()
                .resourceTypeId(ExtensionResourceTypeId.CUSTOMER)
                .actions(ExtensionAction.CREATE,ExtensionAction.UPDATE)
                .build();

        ExtensionDraft extensionDraft = ExtensionDraftBuilder.of()
                .destination(destination)
                .triggers(trigger)
                .build();

        CompletableFuture<ApiHttpResponse<Extension>> response = client.withProjectKey("demo-project")
                .extensions()
                .post(extensionDraft)
                .execute();

        return response.get();
    }

    @GetMapping("/getExt")
    public ApiHttpResponse<ExtensionPagedQueryResponse> getExt() throws ExecutionException, InterruptedException {


       CompletableFuture<ApiHttpResponse<ExtensionPagedQueryResponse>> exts = client.withProjectKey("demo-project")
                .extensions()
                .get()
                .execute();

       return exts.get();
    }

    @RequestMapping("/extension")
    public ResponseEntity extSab(@RequestBody JsonNode req, @RequestHeader HttpHeaders headers)
    {
        System.out.println("headers : " + headers);
        System.out.println("reqBody : " + req);

        JsonNode customerName = req.get("resource").get("obj");

        System.out.println("customerName :" +customerName.get("firstName"));
       String customerFName = customerName.get("firstName").toString();
//        System.out.println("Customer : " + customer);
        JSONObject obj = new JSONObject();
        JSONArray actions = new JSONArray();


        JSONObject changeFirstNameAction = new JSONObject();
        changeFirstNameAction.put("action","setFirstName");
        changeFirstNameAction.put("firstName","HeroPinaki");

        if(customerFName.contains("137")) {
            System.out.println("modifying actions");
            actions.appendElement(changeFirstNameAction);
        }

        obj.put("actions",actions);


        return new ResponseEntity(obj, HttpStatus.OK);
    }

}
@JsonSerialize
class EmptyResp{}