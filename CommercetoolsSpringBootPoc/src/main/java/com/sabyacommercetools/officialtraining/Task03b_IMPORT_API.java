package com.sabyacommercetools.officialtraining;

import com.commercetools.importapi.client.ApiRoot;
import com.commercetools.importapi.models.common.*;
import com.commercetools.importapi.models.importrequests.ImportResponse;
import com.commercetools.importapi.models.importrequests.PriceImportRequestBuilder;
import com.commercetools.importapi.models.importsinks.ImportSink;
import com.commercetools.importapi.models.importsinks.ImportSinkDraft;
import com.commercetools.importapi.models.importsinks.ImportSinkDraftBuilder;
import com.commercetools.importapi.models.prices.PriceImport;
import com.commercetools.importapi.models.prices.PriceImportBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class Task03b_IMPORT_API {

    @Autowired
    ApiRoot clientImpTraining;

    ImportSink importSink;

    @RequestMapping("/creImpSinkTrng")
   public ImportSink createImportSink() throws ExecutionException, InterruptedException {
       ImportSinkDraft importSinkDraft = ImportSinkDraftBuilder.of()
               .key("sabya_imp_sink")
               .build();

       CompletableFuture<ApiHttpResponse<ImportSink>> importSink =
       clientImpTraining.withProjectKeyValue("training-0816-mixed-java-dev")
               .importSinks()
               .post(importSinkDraft)
               .execute();

       this.importSink = importSink.get().getBody();

       return this.importSink;

   }

   @RequestMapping("/impPriTrng")
   public ApiHttpResponse<ImportResponse> importPrice() throws ExecutionException, InterruptedException {
       PriceImport priceImport = PriceImportBuilder.of()
               .key("my-sabya-price-import")
               .value(
                       MoneyBuilder.of()
                               .currencyCode("USD")
                               .centAmount(1337L)
                               .build()
               )
               .product(
                       ProductKeyReferenceBuilder.of()
                       .key("celery-seed-product")
                       .build()
               )
               .productVariant(
                       ProductVariantKeyReferenceBuilder.of()
                       .key("CELERYSEEDS01")
                       .build()
               )
               .build();

       CompletableFuture<ApiHttpResponse<ImportResponse>> imp =
       clientImpTraining.withProjectKeyValue("training-0816-mixed-java-dev")
               .prices()
               .importSinkKeyWithImportSinkKeyValue("sabya_imp_sink")
               .post(PriceImportRequestBuilder
                               .of()
                       .resources(priceImport)
                       .build()
               ).execute();

       return imp.get();
   }
}
