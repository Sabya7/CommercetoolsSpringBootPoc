package com.sabyacommercetools.officialtraining;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.channel.Channel;
import com.commercetools.api.models.channel.ChannelResourceIdentifierBuilder;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.commercetools.api.models.order.OrderFromCartDraftBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class TASK04b_CHECKOUT {

    @Autowired
    ApiRoot clientTraining;

    Cart cart;

    @RequestMapping("/chkOutTrng")
    public ApiHttpResponse<Cart> checkingOut() throws ExecutionException, InterruptedException {

        Channel channel = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .channels()
                .withId("84d30c34-4da5-42ff-a34e-cf5cd525d1e2")
                .get()
                .execute()
                .get()
                .getBody();

        Customer customer = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .customers()
                .withId("02ffec27-ea2d-40ce-8820-774ea84fc0e2")
                .get()
                .execute()
                .get()
                .getBody();

        CartDraft cartDraft = CartDraftBuilder.of()
                .customerEmail(customer.getEmail())
                .currency("USD")
                .customerId(customer.getId())
                .deleteDaysAfterLastModification(90L)
                .shippingAddress(customer.getAddresses().get(0))
                .country(customer.getAddresses().get(0).getCountry())
                .build();

        Cart cart = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .carts()
                .post(cartDraft)
                .execute()
                .get()
                .getBody();

        CartUpdateAction updateAction = CartAddLineItemActionBuilder.of()
                .sku("CELERYSEEDS01")
                .quantity(5L)
                .supplyChannel(
                        ChannelResourceIdentifierBuilder.of()
                        .id(channel.getId())
                        .build()
                ).build();

        CartUpdate update = CartUpdateBuilder.of()
                .actions(updateAction)
                .version(1L)
                .build();

        CompletableFuture<ApiHttpResponse<Cart>> rCart =clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .carts()
                .withId(cart.getId())
                .post(update)
                .execute();

        this.cart=cart;

        return rCart.get();

    }

    @RequestMapping("creOrdTrng")
    public ApiHttpResponse<Order> createOrder() throws ExecutionException, InterruptedException {

        OrderFromCartDraft fromCartDraft = OrderFromCartDraftBuilder.of()
                .cart(
                        CartResourceIdentifierBuilder.of()
                        .id("16fe6fe0-e021-42b1-beb7-66f8b8a9de7d")
                        .build()
                )
                .version(8L)
                .orderNumber("207")
                .build();

        CompletableFuture<ApiHttpResponse<Order>> order = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .orders()
                .post(fromCartDraft)
                .execute();

        return order.get();
    }

    @RequestMapping("/recTrng")
    public ApiHttpResponse<Cart> recalculate() throws ExecutionException, InterruptedException {
        CartUpdateAction updateAction = CartRecalculateActionBuilder.of()
                .updateProductData(true)
                .build();

        CartUpdate update = CartUpdateBuilder.of()
                .actions(updateAction)
                .version(6L)
                .build();

        CompletableFuture<ApiHttpResponse<Cart>> rCart =clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .carts()
                .withId("16fe6fe0-e021-42b1-beb7-66f8b8a9de7d")
                .post(update)
                .execute();

        return rCart.get();
    }

}
