package com.balicki.hibernate.model;

import javax.persistence.AttributeConverter;

public class YesNoBooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return value ? "Yes" : "No";
    }

    @Override
    public Boolean convertToEntityAttribute(String dataBase) {
        return dataBase.equals("Yes");
    }
}
