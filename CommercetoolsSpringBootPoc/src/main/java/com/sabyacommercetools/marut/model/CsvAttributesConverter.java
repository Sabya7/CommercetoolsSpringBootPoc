package com.sabyacommercetools.marut.model;

import com.commercetools.importapi.models.productvariants.*;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

public class CsvAttributesConverter extends AbstractBeanField<List<Attribute>, String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        String[] attributeKeyValuePairs = value.split("\\s+");
        List<Attribute> attributeList = new ArrayList<>();
        AttributesBuilder attributesBuilder = AttributesBuilder.of();
        for (String attributeKeyValuePair : attributeKeyValuePairs) {
            String[] arr = attributeKeyValuePair.split(";");
            Attribute attribute;
            switch (arr[1]) {

                case "num":
                    attribute= NumberAttributeBuilder.of().name(arr[0]).value(Double.valueOf(arr[2])).build();
                    break;
                case "bool":
                    attribute= BooleanAttributeBuilder.of().name(arr[0]).value(Boolean.valueOf(arr[2])).build();
                    break;
                case "text":
                default:
                    attribute = TextAttributeBuilder.of().name(arr[0]).value(arr[2]).build();
                    break;
            }

            attributesBuilder.addValue(arr[0], attribute);
        }
        return new ArrayList<>(attributesBuilder.build().values().values());
    }
}
