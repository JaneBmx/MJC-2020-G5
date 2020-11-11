package com.epam.esm.validation;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GiftCertificateRequestParamValidator implements Validator {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MIN_DURATION_PER_DAYS = 1;

    public boolean isValidGiftCertificate(GiftCertificate giftCertificate) {
        return giftCertificate != null
                && isValidName(giftCertificate.getName())
                && isValidDescription(giftCertificate.getDescription())
                && isValidPrice(giftCertificate.getPrice())
                && isValidDuration(giftCertificate.getDuration());
    }

    public boolean isValidName(String name) {
        return name != null && !name.isEmpty() && name.length() >= MIN_NAME_LENGTH;
    }

    public boolean isValidDescription(String description) {
        return description != null && !description.isEmpty();
    }

    public boolean isValidPrice(double price) {
        return price > 0;
    }

    public boolean isValidDuration(int duration) {
        return duration >= MIN_DURATION_PER_DAYS;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificate.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(errors, "description", "description.empty");

        GiftCertificate giftCertificate = (GiftCertificate) target;
        if (giftCertificate.getPrice()<=0){
            errors.rejectValue("price", "negativevalue");
        }
        if(giftCertificate.getDuration()<1){
            errors.rejectValue("duration","negativevalue");
        }
    }
}
