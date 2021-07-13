package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.custom_object.CustomObjectDraft;
import com.commercetools.api.models.custom_object.CustomObjectDraftBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class CustObj {

    @Autowired
    ApiRoot apiRoot;

    @GetMapping("/cust")
    public ApiHttpResponse<CustomObject> createCust() throws ExecutionException, InterruptedException {
        CustomObjectDraft c = CustomObjectDraftBuilder.of()
                .container("SabyaCustObj")
                .key("cust1")
                .value("12345")
                .version(0L)
                .build();

        CompletableFuture<ApiHttpResponse<CustomObject>> customObject =
                apiRoot.withProjectKey("demo-project")
                .customObjects()
                .post(c)
                .execute();

        return customObject.get();

    }
}
