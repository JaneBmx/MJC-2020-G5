package com.epam.esm.util;

import java.util.Optional;

public enum Operator {
    GREATER("gt", ">"),
    GREATER_EQUAL("gte", ">="),
    LESS("lt", "<"),
    LESS_EQUAL("lte", "<="),
    EQUAL("eq", "="),
    NOT_EQUAL("neq", "<>");

    private final String shortForm;
    private final String sign;

    Operator(String shortForm, String sign) {
        this.shortForm = shortForm;
        this.sign = sign;
    }

    public String getShortForm() {
        return shortForm;
    }

    public String getSign() {
        return sign;
    }

    public static Optional<Operator> getBySortForm(String shortForm){
        for (Operator op : Operator.values()){
            if (op.shortForm.equalsIgnoreCase(shortForm)){
                return Optional.of(op);
            }
        }
        return Optional.empty();
    }
}
