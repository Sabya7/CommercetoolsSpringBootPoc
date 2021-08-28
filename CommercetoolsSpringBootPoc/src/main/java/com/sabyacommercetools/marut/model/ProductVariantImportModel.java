package com.sabyacommercetools.marut.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantImportModel {

    @CsvBindByName
    private String key;

    @CsvBindByName
    private String sku;

    @CsvBindByName
    private Boolean isMasterVariant;

    @CsvCustomBindByName(column = "attributes",converter = CsvAttributesConverter.class)
    private java.util.List<com.commercetools.importapi.models.productvariants.Attribute> attributes;

    @CsvCustomBindByName(column = "product",converter = CsvProductKeyReferenceConverter.class)
    private com.commercetools.importapi.models.common.ProductKeyReference product;
}
