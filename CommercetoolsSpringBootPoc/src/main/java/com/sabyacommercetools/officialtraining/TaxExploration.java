package com.sabyacommercetools.officialtraining;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.tax_category.*;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class TaxExploration {

    @Autowired
    ApiRoot clientTraining;

    @RequestMapping("/creTaxCatTrng")
    public ApiHttpResponse<TaxCategoryPagedQueryResponse> createTaxCategory() throws ExecutionException, InterruptedException {
        TaxCategoryDraft taxCategory = TaxCategoryDraftBuilder.of()
                .name("FancyTax1")
                .description("Tax for fancy products")
                .rates(Collections.emptyList())
                .build();

        TaxCategoryDraft taxCategory2 = TaxCategoryDraftBuilder.of()
                .name("FancyTax2")
                .description("Tax for fancy products")
                .rates(Arrays.asList(TaxRateDraftBuilder.of()
                        .name("FancyTaxDE")
                        .includedInPrice(true)
                        .country("DE")
                        .amount(0.19)
                        .build()))
                .build();

        TaxCategoryDraft taxCategory3 = TaxCategoryDraftBuilder.of()
                .name("FancyTax3")
                .description("Tax for fancy products")
                .rates(Arrays.asList())
                .build();

        TaxCategoryDraft taxCategory4 =  TaxCategoryDraftBuilder.of()
                .name("FancyTax4")
                .description("Tax for fancy products")
                .rates(Arrays.asList(TaxRateDraftBuilder.of()
                        .name("FancyTaxDE2")
                        .country("DE")
                        .amount(0.19)
                        .build()))
                .build();

        CompletableFuture<ApiHttpResponse<TaxCategory>> category1 = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .taxCategories()
                .post(taxCategory)
                .execute();
        CompletableFuture<ApiHttpResponse<TaxCategory>> category2 = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .taxCategories()
                .post(taxCategory2)
                .execute();
        CompletableFuture<ApiHttpResponse<TaxCategory>> category3 = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .taxCategories()
                .post(taxCategory3)
                .execute();
//        CompletableFuture<ApiHttpResponse<TaxCategory>> category4 = clientTraining.withProjectKey("training-0816-mixed-java-dev")
//                .taxCategories()
//                .post(taxCategory4)
//                .execute();

        CompletableFuture<ApiHttpResponse<TaxCategoryPagedQueryResponse>> categories = clientTraining.withProjectKey("training-0816-mixed-java-dev")
                .taxCategories()
                .get()
                .execute();

        return categories.get();
    }

}
