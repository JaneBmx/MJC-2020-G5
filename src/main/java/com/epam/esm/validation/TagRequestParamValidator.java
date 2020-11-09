package com.epam.esm.validation;

import com.epam.esm.entity.Tag;

public class TagRequestParamValidator {
    private static final int MIN_NAME_LENGTH = 2;

    public boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() >= MIN_NAME_LENGTH;
    }

    public boolean isValidTag(Tag tag) {
        return tag != null && isValidName(tag.getName());
    }
}
