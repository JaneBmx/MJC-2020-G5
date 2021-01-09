package com.epam.esm.entity.association;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GiftCertificate2tagId implements Serializable {
    @Column(name = "gift_certificate_id")
    private Integer giftCertificateId;

    @Column(name = "tag_id")
    private Integer tagId;

    public GiftCertificate2tagId() {}

    public GiftCertificate2tagId(Integer giftCertificateId,Integer tagId) {
        this.giftCertificateId = giftCertificateId;
        this.tagId = tagId;
    }

    public Integer getGiftCertificateId() {
        return giftCertificateId;
    }

    public void setGiftCertificateId(Integer giftCertificateId) {
        this.giftCertificateId = giftCertificateId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate2tagId)) return false;
        GiftCertificate2tagId that = (GiftCertificate2tagId) o;
        return Objects.equals(getGiftCertificateId(), that.getGiftCertificateId()) &&
                Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGiftCertificateId(), getTagId());
    }
}
