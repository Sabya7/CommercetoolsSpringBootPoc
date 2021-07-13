package com.sabyacommercetools.poc;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.customer.CustomerDraftBuilder;
import com.commercetools.api.models.customer.CustomerPagedQueryResponse;
import com.commercetools.api.models.customer.CustomerSignInResult;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class CustomerExploration {

    @Autowired
    private ApiRoot client;

    @GetMapping("/creCust")
    public ApiHttpResponse<CustomerSignInResult> createCustomer() throws ExecutionException, InterruptedException {
        CustomerDraft customerDraft
                = CustomerDraftBuilder.of()
                .email("pinkidash2@gmail.com")
                .firstName("Pinaki2")
                .lastName("Dash2")
                .password("****2")
                .build();

        CompletableFuture<ApiHttpResponse<CustomerSignInResult>> customer =  client.withProjectKey("demo-project")
                .customers()
                .post(customerDraft)
                .execute();

        return customer.get();
    }

    @GetMapping("/getCust")
    public ApiHttpResponse<CustomerPagedQueryResponse> getCust() throws ExecutionException, InterruptedException {
        CompletableFuture<ApiHttpResponse<CustomerPagedQueryResponse>> customer =  client.withProjectKey("demo-project")
                .customers()
                .get()
                .execute();

        return customer.get();

    }

}
