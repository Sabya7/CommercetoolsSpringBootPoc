package com.sabyacommercetools.marut.controllers.mobile;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.cart.LineItem;
import com.commercetools.api.models.cart.LineItemDraft;
import com.commercetools.api.models.cart.LineItemDraftBuilder;
import com.commercetools.api.models.customer.CustomerResourceIdentifierBuilder;
import com.commercetools.api.models.shopping_list.*;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ShoppingListApi {

    //http client to connect to commercetools API
    @Autowired
    private ApiRoot clientTraining;

    //value for project name
    @Value("${ct.project}")
    private String project;

    @PostMapping("/createShoppingList")
    public ApiHttpResponse<ShoppingList> createShoppingList(ShoppingListDraft shoppingListDraft) throws ExecutionException, InterruptedException {
        CompletableFuture<ApiHttpResponse<ShoppingList>> shoppingList =
        clientTraining.withProjectKey(project)
                .shoppingLists()
                .post(shoppingListDraft)
                .execute();

        return shoppingList.get();
    }

    @PostMapping("/setCustomer")
    public ApiHttpResponse<ShoppingList> setCustomer(String id, String customerId) throws ExecutionException, InterruptedException {

        ShoppingListSetCustomerAction shoppingListSetCustomerAction = ShoppingListSetCustomerActionBuilder
                .of()
                .customer(
                        CustomerResourceIdentifierBuilder.of()
                        .id(customerId)
                        .build()
                )
                .build();

        ShoppingListUpdateAction shoppingListUpdateAction =
                ShoppingListSetCustomerAction.of()
                        .withShoppingListSetCustomerAction(action -> shoppingListSetCustomerAction);

        return updateShoppingList(shoppingListUpdateAction,id).get();

    }

    @PostMapping("/addLineItem")
    public ApiHttpResponse<ShoppingList> addLineItem(String id, String SKU) throws ExecutionException, InterruptedException {

        ShoppingListAddLineItemAction listAddLineItemAction = ShoppingListAddLineItemActionBuilder.of()
                .sku(SKU)
                .build();

        ShoppingListUpdateAction shoppingListUpdateAction =
                ShoppingListAddLineItemAction.of()
                        .withShoppingListAddLineItemAction(action -> listAddLineItemAction);

        return updateShoppingList(shoppingListUpdateAction,id).get();

    }

    @PostMapping("/changeLineItemQuantity")
    public ApiHttpResponse<ShoppingList> changeLineItemQuantity
            (String id,String lineItemId, Long quantity) throws ExecutionException, InterruptedException {

        ShoppingListChangeLineItemQuantityAction changeLineItemQuantityAction =
                ShoppingListChangeLineItemQuantityActionBuilder.of()
                        .quantity(quantity)
                        .lineItemId(lineItemId)
                        .build();
        ShoppingListUpdateAction shoppingListUpdateAction = ShoppingListChangeLineItemQuantityAction
                .of().withShoppingListChangeLineItemQuantityAction(action -> changeLineItemQuantityAction);

        return updateShoppingList(shoppingListUpdateAction,id).get();

    }

    @PostMapping("/removeLineItem")
    public ApiHttpResponse<ShoppingList> removeLineItem
            (String id,String lineItemId, Long quantity) throws ExecutionException, InterruptedException {

        ShoppingListRemoveLineItemAction shoppingListRemoveLineItemAction =
                ShoppingListRemoveLineItemActionBuilder.of()
                        .quantity(quantity)
                        .lineItemId(lineItemId)
                        .build();
        ShoppingListUpdateAction shoppingListUpdateAction = ShoppingListRemoveLineItemAction
                .of().withShoppingListRemoveLineItemAction(action -> shoppingListRemoveLineItemAction);

        return updateShoppingList(shoppingListUpdateAction,id).get();
    }

    private CompletableFuture<ApiHttpResponse<ShoppingList>> updateShoppingList
            (ShoppingListUpdateAction shoppingListUpdateAction, String id)
    {
        return retrieveShoppingList(id)
                .thenCompose(
                        shoppingListApiHttpResponse ->
                                clientTraining.withProjectKey(project).shoppingLists().withId(id)
                                        .post(
                                                ShoppingListUpdateBuilder.of()
                                                        .actions(shoppingListUpdateAction)
                                                        .version(shoppingListApiHttpResponse
                                                                .getBody()
                                                                .getVersion()
                                                        )
                                                        .build()
                                        )
                                        .execute()
                );
    }

    private CompletableFuture<ApiHttpResponse<ShoppingList>> retrieveShoppingList(String id)
    {
        return
                clientTraining.withProjectKey(project)
                        .shoppingLists()
                        .withId(id)
                        .get()
                        .execute();
    }
}
