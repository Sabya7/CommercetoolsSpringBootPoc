package com.sabyacommercetools.officialtraining;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.AddressBuilder;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupResourceIdentifier;
import com.commercetools.api.models.customer_group.CustomerGroupResourceIdentifierBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class TASK02a_CREATE {


    @Autowired
    ApiRoot clientTraining;

    CustomerToken token;

    @GetMapping("/creCustTrng")
    public ApiHttpResponse<CustomerSignInResult> createCustomer() throws ExecutionException, InterruptedException {

         Address address = AddressBuilder.of().country("IN").build();

        CustomerDraft customerDraft
                = CustomerDraftBuilder.of()
                .email("sabya@gmail.com")
                .firstName("sabya1")
                .lastName("sahoo1")
                .password("12345")
                .addresses(address)
                .defaultBillingAddress(0L)
                .build();

        CompletableFuture<ApiHttpResponse<CustomerSignInResult>> customer =
                clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .customers()
                .post(customerDraft)
                .execute();


        return customer.get();
    }

    @GetMapping("/creEmailTokTrng")
    public CustomerToken creEmailToken() throws ExecutionException, InterruptedException {

       CustomerCreateEmailToken emailToken =
        CustomerCreateEmailTokenBuilder.of()
                .id("02ffec27-ea2d-40ce-8820-774ea84fc0e2")
                .ttlMinutes(1000L)
                .build();

        CompletableFuture<ApiHttpResponse<CustomerToken>> customerEmailToken =
                clientTraining.withProjectKey("training-0816-mixed-java-dev")
                        .customers()
                        .emailToken()
                        .post(emailToken)
                        .execute();

        token = customerEmailToken.get().getBody();
        return token;
    }

    @GetMapping("/verEmailTokTrng")
    public ApiHttpResponse<JsonNode> verifyEmailToken() throws ExecutionException, InterruptedException {
        System.out.println(token.getValue());
       CustomerEmailVerify customerEmailVerify = CustomerEmailVerifyBuilder.of()
               .tokenValue(token.getValue())
               .build();

        CompletableFuture<ApiHttpResponse<JsonNode>> customerEmailToken =
                clientTraining.withProjectKey("training-0816-mixed-java-dev")
                        .customers()
                        .emailConfirm()
                        .post(customerEmailVerify)
                        .execute();

        return customerEmailToken.get();
    }

    @GetMapping("/updCustGrpTrng")
    public ApiHttpResponse<Customer> getCust() throws ExecutionException, InterruptedException {

        CompletableFuture<ApiHttpResponse<CustomerGroup>> customerGroup = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .customerGroups()
                .withKey("indoor-outdoor-group")
                .get()
                .execute();

        CustomerGroupResourceIdentifier customerGroupResourceIdentifier =
        CustomerGroupResourceIdentifierBuilder.of()
                .id(customerGroup.get().getBody().getId())
                .build();

        CustomerSetCustomerGroupAction action = CustomerSetCustomerGroupActionBuilder.of()
                .customerGroup(customerGroupResourceIdentifier)
                .build();

        CustomerUpdate updates = CustomerUpdateBuilder.of().actions(action)
                .version(2L)
                .build();


       CompletableFuture<ApiHttpResponse<Customer>> customer =  clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .customers()
                .withId("02ffec27-ea2d-40ce-8820-774ea84fc0e2")
                .post(updates)
                .execute();

        return customer.get();

    }
}
