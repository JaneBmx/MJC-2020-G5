package com.epam.esm.util;

public class Criteria {
    private final String key;
    private final String value;
    private final Operator operator;

    public Criteria(String key, String value, Operator operator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }
}
