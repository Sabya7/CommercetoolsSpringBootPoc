package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.error.*;
import com.commercetools.api.models.extension.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.vrap.rmf.base.client.ApiHttpResponse;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ExtensionsExploration {

    @Autowired
    private ApiRoot client;

    @GetMapping("/creExt")
    public ApiHttpResponse<Extension> createExtension() throws ExecutionException, InterruptedException {

        ExtensionDestination destination = ExtensionHttpDestinationBuilder.of()

                .url("https://439dd870fd7b38.localhost.run/extension")
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
        JSONArray errors = new JSONArray();


        JSONObject changeFirstNameAction = new JSONObject();
        changeFirstNameAction.put("action","setFirstName");
        changeFirstNameAction.put("firstName","HeroPinaki");

        if(customerFName.contains("137")) {
            System.out.println("modifying actions");
            actions.appendElement(changeFirstNameAction);
        }

        JSONObject error = new JSONObject();
        error.put("code" , "RequiredField");
        error.put("message"," Hello World");

        errors.appendElement(error);


        if(customerFName.contains("error"))
        { obj.put("errors",errors);
            return new ResponseEntity(obj, HttpStatus.BAD_REQUEST);
        }
        else
        obj.put("actions",actions);



        return new ResponseEntity(obj, HttpStatus.OK);
    }

    @RequestMapping("/extension13")
    public ResponseEntity extSab13(@RequestBody JsonNode req, @RequestHeader HttpHeaders headers) throws JsonProcessingException {
        System.out.println("headers : " + headers);
        System.out.println("reqBody : " + req);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        Customer customer = objectMapper
                .treeToValue(req.get("resource").get("obj"), Customer.class);
        String customerName = customer.getFirstName();

        CustomerSetFirstNameAction custAction =
                CustomerSetFirstNameActionBuilder.of().firstName("Sabya").build();

        CustomerUpdateAction customerUpdateAction = CustomerSetFirstNameAction.of()
                .withCustomerSetFirstNameAction(a -> custAction);


        CustomerUpdate customerUpdate = CustomerUpdateBuilder.of()
                .actions(customerUpdateAction)
                .build();
        CustomerUpdate emptyCustomerUpdate = CustomerUpdateBuilder
                .of()
                .actions()
                .build();

        ErrorObject object = RequiredFieldErrorBuilder
                .of()
                .message("Sabya Custom Ex13 msg")
                .build();

        ErrorResponse errorResponse = ErrorResponseBuilder
                .of()
                .errors(object)
                .build();

        if(customerName.contains("abcd"))
            return new ResponseEntity(errorResponse,HttpStatus.BAD_REQUEST);
        else if(customerName.contains("137"))
        return new ResponseEntity(customerUpdate, HttpStatus.CREATED);
        else
            return new ResponseEntity(emptyCustomerUpdate,HttpStatus.OK);
    }
    @GetMapping("/upExt")
    public ApiHttpResponse<Extension> updateExtension() throws ExecutionException, InterruptedException {
//        JSONObject object = new JSONObject();
//        JSONArray array = new JSONArray();
//
//        JSONObject update = new JSONObject();
//
//        update.put("action", "changeDestination");
//        update.put("destination","sabya.com");
//
//        array.appendElement(update);
//        object.put("version",1);
//        object.put("actions",object);
        ExtensionDestination destination = ExtensionHttpDestinationBuilder.of()

                .url("https://sabya.com")
                .build();


        ExtensionUpdateAction extensionUpdateAction = ExtensionChangeDestinationActionBuilder.of()
                .destination(destination)
                .build();

        ExtensionUpdate extensionUpdate = ExtensionUpdateBuilder.of()
                .actions(extensionUpdateAction)
                .version(1L)
                .build();

        CompletableFuture<ApiHttpResponse<Extension>> response = client.withProjectKey("demo-project")
                .extensions()
                .withId("0f3cd171-61f3-466c-98ab-5e1f0f363436")
                .post(extensionUpdate)
                .execute();

       return response.get();
    }

    @GetMapping("delExt")
    public void delExt()
    {
        client.withProjectKey("demo-project")
                .extensions()
                .withId("")
                .delete()
                .execute();

    }
}