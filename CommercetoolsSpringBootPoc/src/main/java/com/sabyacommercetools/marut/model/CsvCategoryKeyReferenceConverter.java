package com.sabyacommercetools.marut.model;

import com.commercetools.importapi.models.common.CategoryKeyReference;
import com.commercetools.importapi.models.common.CategoryKeyReferenceBuilder;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CsvCategoryKeyReferenceConverter extends AbstractBeanField<List<CategoryKeyReference>,String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

        String[] categoryKeys = value.split("\\s+");
        List<CategoryKeyReference> categoryKeyReferenceList = new ArrayList<>();
        for(String categoryKey : categoryKeys)
        {
              categoryKeyReferenceList.add(
                      CategoryKeyReferenceBuilder.of()
                      .key(categoryKey)
                      .build()
              );
        }
        return categoryKeyReferenceList;
    }
}
