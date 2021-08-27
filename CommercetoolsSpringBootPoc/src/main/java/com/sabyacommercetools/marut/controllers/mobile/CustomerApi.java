package com.sabyacommercetools.marut.controllers.mobile;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.customer.*;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
* The Mobile App's customer profile should consume this controller.
* Usages:
*       Customer can create his identity.
*       Customer can update certain fields.
*       Customer can delete itself.(optional functionality)
*
* This controller is like a bridge between app and CT.
* */

@RestController
public class CustomerApi implements InitializingBean {

    //http client to connect to commercetools API
    @Autowired
    private ApiRoot clientTraining;

    //value for project name
    @Value("${ct.project}")
    private String project;

    //signing up the customer
    @PostMapping("/createACustomer")
    public ApiHttpResponse<CustomerSignInResult> createACustomer(@RequestBody CustomerDraft customerDraft)
            throws ExecutionException, InterruptedException {


        /*When the customer signs up we are using customer endpoint to create a new one,
        * after that we immediately log in that customer to get the token.
        * The login is done using /me/login/ endpoint.
        */
        CompletableFuture<ApiHttpResponse<CustomerSignInResult>> customerSignIn =
                clientTraining.withProjectKey(project)
                        .customers()
                        .post(customerDraft)
                        .execute();

        return customerSignIn.get();
    }

    @PostMapping("/updateFirstName")
    public ApiHttpResponse<Customer> updateFirstName(String id, String firstName) throws ExecutionException, InterruptedException {
        CustomerSetFirstNameAction custAction =
                CustomerSetFirstNameActionBuilder.of().firstName(firstName).build();

        CustomerUpdateAction customerUpdateAction = CustomerSetFirstNameAction.of()
                .withCustomerSetFirstNameAction(action -> custAction);

        return updateCustomer(customerUpdateAction,id).get();
    }

    @PostMapping("/updateLastName")
    public ApiHttpResponse<Customer> updateLastName(String id, String lastName) throws ExecutionException, InterruptedException {
        CustomerSetLastNameAction custAction =
                CustomerSetLastNameActionBuilder.of().lastName(lastName).build();

        CustomerUpdateAction customerUpdateAction = CustomerSetLastNameAction.of()
                .withCustomerSetLastNameAction(action -> custAction);

        return updateCustomer(customerUpdateAction,id).get();

    }

    @PostMapping("/addAddress")
    public ApiHttpResponse<Customer> addAddress(String id, Address address) throws ExecutionException, InterruptedException {

        CustomerAddAddressAction customerAddAddressAction =
                CustomerAddAddressActionBuilder.of().address(address).build();

        CustomerUpdateAction customerUpdateAction = CustomerAddAddressAction.of()
                .withCustomerAddAddressAction(action -> customerAddAddressAction);

        return updateCustomer(customerUpdateAction,id).get();

    }

    @PostMapping("/changeAddress")
    public ApiHttpResponse<Customer> changeAddress(String id, String addressId, Address address) throws ExecutionException, InterruptedException {

        CustomerChangeAddressAction customerChangeAddressAction =
                CustomerChangeAddressActionBuilder.of().
                        addressId(addressId).address(address).build();

        CustomerUpdateAction customerUpdateAction = CustomerChangeAddressAction.of()
                .withCustomerChangeAddressAction(action -> customerChangeAddressAction);

        return updateCustomer(customerUpdateAction,id).get();

    }

    @PostMapping("/removeAddress")
    public ApiHttpResponse<Customer> removeAddress(String id, String addressId) throws ExecutionException, InterruptedException {

        CustomerRemoveAddressAction customerRemoveAddressAction =
                CustomerRemoveAddressActionBuilder.of().
                        addressId(addressId).build();

        CustomerUpdateAction customerUpdateAction = CustomerRemoveAddressAction.of()
                .withCustomerRemoveAddressAction(action -> customerRemoveAddressAction);

        return updateCustomer(customerUpdateAction,id).get();

    }

    @PostMapping("/setDefaultShippingAddress")
    public ApiHttpResponse<Customer> setShippingAddress(String id, String addressId) throws ExecutionException, InterruptedException {

        CustomerSetDefaultBillingAddressAction setDefaultBillingAddressAction =
                CustomerSetDefaultBillingAddressActionBuilder.of()
                        .addressId(addressId)
                        .build();

        CustomerUpdateAction customerUpdateAction = CustomerSetDefaultBillingAddressAction.of()
                .withCustomerSetDefaultBillingAddressAction(action -> setDefaultBillingAddressAction);

        return updateCustomer(customerUpdateAction,id).get();

    }



    private CompletableFuture<ApiHttpResponse<Customer>> updateCustomer
            (CustomerUpdateAction customerUpdateAction,String id)
    {
              return retrieveCustomer(id)
                       .thenCompose(
                               customerApiHttpResponse ->
                               clientTraining.withProjectKey(project).customers().withId(id)
                               .post(
                                       CustomerUpdateBuilder.of()
                                               .actions(customerUpdateAction)
                                               .version(customerApiHttpResponse
                                                       .getBody()
                                                       .getVersion()
                                               )
                                               .build()
                               )
                               .execute()
                       );
    }


    public CompletableFuture<ApiHttpResponse<Customer>> retrieveCustomer(String id)
    {
        return
                clientTraining.withProjectKey(project).
                customers()
                .withId(id)
                .get()
                .execute();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Hey Sabya");
    }
}
