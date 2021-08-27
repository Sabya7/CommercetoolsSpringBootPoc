package com.sabyacommercetools.marut.controllers.mobile;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.AddressImpl;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.commercetools.api.models.order.OrderFromCartDraftBuilder;
import com.commercetools.api.models.shopping_list.ShoppingListLineItem;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class CheckingOutApi {

    //http client to connect to commercetools API
    @Autowired
    private ApiRoot clientTraining;

    //value for project name
    @Value("${ct.project}")
    private String project;

    //autowired this to remove lineItems from shopping list once they are added to the cart.
    @Autowired
    ShoppingListApi shoppingListApi;

    @Autowired
    CustomerApi customerApi;

    @PostMapping("/cartFromDiscountedItems")
    public ApiHttpResponse<Cart> cartFromDiscountedItems(String shoppingListId, String customerId,
                                                         ArrayList<String> lineItemIds) throws ExecutionException, InterruptedException {

         //Step - 1: Create Array of LineItems from array of LineItem IDs

        //fetch all the lineItems in shopping list and filter the discounted lineItems.
      List<ShoppingListLineItem> listLineItems =
              clientTraining.withProjectKey(project)
                           .shoppingLists()
                           .withId(shoppingListId)
                           .get()
                           .execute()
                           .get().getBody().getLineItems()
                           .stream()
                           .filter(listLineItem -> lineItemIds.contains(listLineItem.getId()))
                           .collect(Collectors.toList());

       ArrayList<LineItemDraft> lineItemDrafts = new ArrayList<>();

        for(ShoppingListLineItem listLineItem :listLineItems )
        {
              lineItemDrafts.add(
                      LineItemDraftBuilder.of()
                              .variantId(listLineItem.getVariantId())
                              .quantity((long)listLineItem.getQuantity())
                              .build()
              );
              //Remove this listLineItem from shoppingList as well.
            shoppingListApi.removeLineItem(shoppingListId,
                    listLineItem.getId(), Long.valueOf(listLineItem.getQuantity()));
        }

         //Step-2 : Create a cart.
        /*
        * Here, currency is hard-coded, but logic needs to be added
        * so that it can be determined from customer's country.
        * */

        Customer customer =
                 customerApi.retrieveCustomer(customerId)
                .get()
                .getBody();


        Optional<Address> shippingAddress = customer.getAddresses().stream()
                .filter( address -> address.getId().equals(customer.getDefaultShippingAddressId()))
                .findFirst();

        CartDraft cartDraft = CartDraftBuilder.of()
                .currency("USD")
                .customerId(customerId)
                .lineItems(lineItemDrafts)
                .shippingAddress(shippingAddress.orElseGet(AddressImpl::new)) // will refactor this
                .build();

        CompletableFuture<ApiHttpResponse<Cart>> cartResponse = clientTraining
                .withProjectKey(project)
                .carts()
                .post(cartDraft)
                .execute();



        return cartResponse.get();
    }

    @PostMapping("/checkOut")
    public ApiHttpResponse<Order> checkOut(String cartId) throws ExecutionException, InterruptedException {

        OrderFromCartDraft orderFromCartDraft = OrderFromCartDraftBuilder.of()
                .cart(
                        CartResourceIdentifierBuilder.of()
                        .id(cartId)
                        .build()
                ).version(
                        clientTraining.withProjectKey(project).carts()
                        .withId(cartId)
                        .get().execute()
                        .get().getBody().getVersion()
                ).build();

       return clientTraining.withProjectKey(project).orders()
                .post(orderFromCartDraft)
                .execute().get();
    }

}
