package com.epam.esm.entity.association;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import javax.persistence.*;

@Entity(name = "GiftCertificate2tag")
@Table(name = "gift_certificate2tag")
public class GiftCertificate2tag {
    @EmbeddedId
    private GiftCertificate2tagId id;

    @ManyToOne
    @MapsId("giftCertificateId")
    private GiftCertificate giftCertificate;

    @ManyToOne
    @MapsId("tagId")
    private Tag tag;

    private GiftCertificate2tag() {}

    public GiftCertificate2tag(GiftCertificate giftCertificate, Tag tag) {
        this.giftCertificate = giftCertificate;
        this.tag = tag;
        this.id = new GiftCertificate2tagId(
                giftCertificate.getId(),
                tag.getId()
        );
    }

    public GiftCertificate2tagId getId() {
        return id;
    }

    public void setId(GiftCertificate2tagId id) {
        this.id = id;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
