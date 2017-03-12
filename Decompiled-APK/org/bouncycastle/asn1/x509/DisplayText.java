package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERVisibleString;

public class DisplayText extends ASN1Object implements ASN1Choice {
    public static final int CONTENT_TYPE_BMPSTRING = 1;
    public static final int CONTENT_TYPE_IA5STRING = 0;
    public static final int CONTENT_TYPE_UTF8STRING = 2;
    public static final int CONTENT_TYPE_VISIBLESTRING = 3;
    public static final int DISPLAY_TEXT_MAXIMUM_SIZE = 200;
    int contentType;
    ASN1String contents;

    public DisplayText(int i, String str) {
        if (str.length() > DISPLAY_TEXT_MAXIMUM_SIZE) {
            str = str.substring(CONTENT_TYPE_IA5STRING, DISPLAY_TEXT_MAXIMUM_SIZE);
        }
        this.contentType = i;
        switch (i) {
            case CONTENT_TYPE_IA5STRING /*0*/:
                this.contents = new DERIA5String(str);
            case CONTENT_TYPE_BMPSTRING /*1*/:
                this.contents = new DERBMPString(str);
            case CONTENT_TYPE_UTF8STRING /*2*/:
                this.contents = new DERUTF8String(str);
            case CONTENT_TYPE_VISIBLESTRING /*3*/:
                this.contents = new DERVisibleString(str);
            default:
                this.contents = new DERUTF8String(str);
        }
    }

    public DisplayText(String str) {
        if (str.length() > DISPLAY_TEXT_MAXIMUM_SIZE) {
            str = str.substring(CONTENT_TYPE_IA5STRING, DISPLAY_TEXT_MAXIMUM_SIZE);
        }
        this.contentType = CONTENT_TYPE_UTF8STRING;
        this.contents = new DERUTF8String(str);
    }

    private DisplayText(ASN1String aSN1String) {
        this.contents = aSN1String;
    }

    public static DisplayText getInstance(Object obj) {
        if (obj instanceof ASN1String) {
            return new DisplayText((ASN1String) obj);
        }
        if (obj == null || (obj instanceof DisplayText)) {
            return (DisplayText) obj;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DisplayText getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(aSN1TaggedObject.getObject());
    }

    public String getString() {
        return this.contents.getString();
    }

    public ASN1Primitive toASN1Primitive() {
        return (ASN1Primitive) this.contents;
    }
}
