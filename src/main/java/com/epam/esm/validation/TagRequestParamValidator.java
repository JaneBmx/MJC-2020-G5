package com.epam.esm.validation;

import com.epam.esm.entity.Tag;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class TagRequestParamValidator implements Validator {
    private static final int MIN_NAME_LENGTH = 2;

    public boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() >= MIN_NAME_LENGTH;
    }

    public boolean isValidTag(Tag tag) {
        return tag != null && isValidName(tag.getName());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
    }
}
