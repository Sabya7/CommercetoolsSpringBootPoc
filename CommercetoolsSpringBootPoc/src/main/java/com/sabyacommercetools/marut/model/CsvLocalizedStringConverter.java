package com.sabyacommercetools.marut.model;

import com.commercetools.importapi.models.common.LocalizedString;
import com.commercetools.importapi.models.common.LocalizedStringBuilder;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class CsvLocalizedStringConverter extends AbstractBeanField<LocalizedString,String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        return LocalizedStringBuilder.of().addValue("en",value).build();
    }
}
