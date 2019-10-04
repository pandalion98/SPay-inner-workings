/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;

public class ContentIdentifier
extends ASN1Object {
    ASN1OctetString value;

    private ContentIdentifier(ASN1OctetString aSN1OctetString) {
        this.value = aSN1OctetString;
    }

    public ContentIdentifier(byte[] arrby) {
        this(new DEROctetString(arrby));
    }

    public static ContentIdentifier getInstance(Object object) {
        if (object instanceof ContentIdentifier) {
            return (ContentIdentifier)object;
        }
        if (object != null) {
            return new ContentIdentifier(ASN1OctetString.getInstance(object));
        }
        return null;
    }

    public ASN1OctetString getValue() {
        return this.value;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
}

