package com.epam.esm.validation;

import com.epam.esm.entity.GiftCertificate;

public class GiftCertificateRequestParamValidator {
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
}
