package com.sabyacommercetools.marut.controllers.merchant;


import com.commercetools.importapi.client.ApiRoot;
import com.commercetools.importapi.models.importrequests.ProductImportRequestBuilder;
import com.commercetools.importapi.models.importsinks.ImportSink;
import com.commercetools.importapi.models.products.ProductImport;
import com.commercetools.importapi.models.products.ProductImportBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.sabyacommercetools.marut.model.ProductImportModel;
import com.sabyacommercetools.marut.util.GroupingCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
    public void importProducts() throws FileNotFoundException {

        //Step-1 : parse the CSV file.
        File csv = ResourceUtils.getFile("classpath:testingCSVtoJAVA.csv");
//        File csv = new File("C:\\Commercetools\\poc\\CommercetoolsSpringBootPoc\\src\\main\\resources\\testingCSVtoJAVA.csv");
        HeaderColumnNameMappingStrategy<ProductImportModel> strategy
                = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ProductImportModel.class);


        CsvToBean<ProductImportModel> csvToBean =
                new CsvToBeanBuilder<ProductImportModel>(new FileReader(csv))
                        .withMappingStrategy(strategy)
                        .build();

//        long count = csvToBean.stream().count();
//        System.out.println("count   "+count);

        List<List<ProductImport>> productImportLists =
        csvToBean.stream().parallel().map(productImportModel ->
                ProductImportBuilder.of()
                        .key(productImportModel.getKey())
                        .name(productImportModel.getName())
                        .productType(productImportModel.getProductType())
                        .slug(productImportModel.getSlug())
                        .build()
        ).collect(new GroupingCollector<>(20));

        for(List<ProductImport> productImportList : productImportLists)
        {
            System.out.println("hellllllloooooooooo");
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


}
