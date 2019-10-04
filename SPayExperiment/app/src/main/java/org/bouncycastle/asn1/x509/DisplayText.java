/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
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

public class DisplayText
extends ASN1Object
implements ASN1Choice {
    public static final int CONTENT_TYPE_BMPSTRING = 1;
    public static final int CONTENT_TYPE_IA5STRING = 0;
    public static final int CONTENT_TYPE_UTF8STRING = 2;
    public static final int CONTENT_TYPE_VISIBLESTRING = 3;
    public static final int DISPLAY_TEXT_MAXIMUM_SIZE = 200;
    int contentType;
    ASN1String contents;

    public DisplayText(int n2, String string) {
        if (string.length() > 200) {
            string = string.substring(0, 200);
        }
        this.contentType = n2;
        switch (n2) {
            default: {
                this.contents = new DERUTF8String(string);
                return;
            }
            case 0: {
                this.contents = new DERIA5String(string);
                return;
            }
            case 2: {
                this.contents = new DERUTF8String(string);
                return;
            }
            case 3: {
                this.contents = new DERVisibleString(string);
                return;
            }
            case 1: 
        }
        this.contents = new DERBMPString(string);
    }

    public DisplayText(String string) {
        if (string.length() > 200) {
            string = string.substring(0, 200);
        }
        this.contentType = 2;
        this.contents = new DERUTF8String(string);
    }

    private DisplayText(ASN1String aSN1String) {
        this.contents = aSN1String;
    }

    public static DisplayText getInstance(Object object) {
        if (object instanceof ASN1String) {
            return new DisplayText((ASN1String)object);
        }
        if (object == null || object instanceof DisplayText) {
            return (DisplayText)object;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DisplayText getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return DisplayText.getInstance(aSN1TaggedObject.getObject());
    }

    public String getString() {
        return this.contents.getString();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return (ASN1Primitive)((Object)this.contents);
    }
}

