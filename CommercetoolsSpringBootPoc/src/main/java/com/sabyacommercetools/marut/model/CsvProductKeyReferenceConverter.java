package com.sabyacommercetools.marut.model;

import com.commercetools.importapi.models.common.ProductKeyReference;
import com.commercetools.importapi.models.common.ProductKeyReferenceBuilder;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class CsvProductKeyReferenceConverter extends AbstractBeanField<ProductKeyReference,String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return ProductKeyReferenceBuilder.of().key(value).build();
    }
}
