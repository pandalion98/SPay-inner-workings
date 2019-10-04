/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERIA5String;

public class SPuri {
    private DERIA5String uri;

    public SPuri(DERIA5String dERIA5String) {
        this.uri = dERIA5String;
    }

    public static SPuri getInstance(Object object) {
        if (object instanceof SPuri) {
            return (SPuri)object;
        }
        if (object instanceof DERIA5String) {
            return new SPuri(DERIA5String.getInstance(object));
        }
        return null;
    }

    public DERIA5String getUri() {
        return this.uri;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.uri.toASN1Primitive();
    }
}

