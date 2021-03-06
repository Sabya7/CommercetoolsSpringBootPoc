package com.sabyacommercetools.marut.controllers.merchant;


import com.commercetools.importapi.client.ApiRoot;
import com.commercetools.importapi.models.importoperations.ImportOperationPagedResponse;
import com.commercetools.importapi.models.importrequests.ProductImportRequestBuilder;
import com.commercetools.importapi.models.importsinks.ImportSink;
import com.commercetools.importapi.models.products.ProductImport;
import com.commercetools.importapi.models.products.ProductImportBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.sabyacommercetools.marut.model.ProductImportModel;
import com.sabyacommercetools.marut.util.GroupingCollector;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
public class ProductImporter {

    @Autowired
    ApiRoot clientImpTraining;

    @Value("${ct.project}")
    private String project;

    @Autowired
    ImportSink importSink;

    //@RequestParam("csv") MultipartFile csv
    @RequestMapping("/importProducts")
    public void importProducts() throws FileNotFoundException, ExecutionException, InterruptedException {

        //Step-1 : parse the CSV file.
        File csv = ResourceUtils.getFile("classpath:testingCSVtoJAVA.csv");

        HeaderColumnNameMappingStrategy<ProductImportModel> strategy
                = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ProductImportModel.class);


        CsvToBean<ProductImportModel> csvToBean =
                new CsvToBeanBuilder<ProductImportModel>(new FileReader(csv))
                        .withMappingStrategy(strategy)
                        .build();

        List<List<ProductImport>> productImportLists =
                csvToBean.stream().parallel().map(productImportModel ->
                        ProductImportBuilder.of()
                                .key(productImportModel.getKey())
                                .name(productImportModel.getName())
                                .productType(productImportModel.getProductType())
                                .slug(productImportModel.getSlug())
                                .categories(productImportModel.getCategories())
                                .build()
                ).collect(new GroupingCollector<>(20));


        for (List<ProductImport> productImportList : productImportLists) {


            clientImpTraining.withProjectKeyValue(project)
                    .products()
                    .importSinkKeyWithImportSinkKeyValue(importSink.getKey())
                    .post(
                            ProductImportRequestBuilder.of()
                                    .resources(productImportList)
                                    .build()
                    ).execute();


        }

    }

    @RequestMapping("/queryProductImportOperations")
    public ApiHttpResponse<ImportOperationPagedResponse> queryImportOperations() throws ExecutionException, InterruptedException {

        CompletableFuture<ApiHttpResponse<ImportOperationPagedResponse>> imoprtOperationResponse = clientImpTraining.withProjectKeyValue(project)
                .products()
                .importSinkKeyWithImportSinkKeyValue(importSink.getKey())
                .importOperations()
                .get().withLimit(10000.0).execute();

        return imoprtOperationResponse.get();
    }


}
