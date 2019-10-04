/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public class DERObjectIdentifier
extends ASN1ObjectIdentifier {
    public DERObjectIdentifier(String string) {
        super(string);
    }

    DERObjectIdentifier(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        super(aSN1ObjectIdentifier, string);
    }

    DERObjectIdentifier(byte[] arrby) {
        super(arrby);
    }
}

