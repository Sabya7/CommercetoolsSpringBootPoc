package com.sabyacommercetools.marut.model;

import com.commercetools.importapi.models.common.LocalizedString;
import com.commercetools.importapi.models.common.ProductTypeKeyReference;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductImportModel {

    @CsvBindByName(column = "key")
    private String key;

    @CsvCustomBindByName(column = "name",converter = CsvLocalizedStringConverter.class)
    private com.commercetools.importapi.models.common.LocalizedString name;

    @CsvCustomBindByName(column = "productType",converter = CsvProductTypeKeyReferenceConverter.class)
    private com.commercetools.importapi.models.common.ProductTypeKeyReference productType;

    @CsvCustomBindByName(column = "slug",converter = CsvLocalizedStringConverter.class)
    private com.commercetools.importapi.models.common.LocalizedString slug;

//    private com.commercetools.importapi.models.common.LocalizedString description;

    @CsvCustomBindByName(column = "categories",converter = CsvCategoryKeyReferenceConverter.class)
    private java.util.List<com.commercetools.importapi.models.common.CategoryKeyReference> categories;
//
//    private Boolean publish;


    public ProductImportModel(String key, LocalizedString name, ProductTypeKeyReference productType, LocalizedString slug) {
        this.key = key;
        this.name = name;
        this.productType = productType;
        this.slug = slug;
    }
}
