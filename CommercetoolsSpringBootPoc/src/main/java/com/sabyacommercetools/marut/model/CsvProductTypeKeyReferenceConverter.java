package com.sabyacommercetools.marut.model;

import com.commercetools.importapi.models.common.ProductTypeKeyReference;
import com.commercetools.importapi.models.common.ProductTypeKeyReferenceBuilder;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class CsvProductTypeKeyReferenceConverter extends
        AbstractBeanField<ProductTypeKeyReference,String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return ProductTypeKeyReferenceBuilder.of().key(value).build();
    }
}
