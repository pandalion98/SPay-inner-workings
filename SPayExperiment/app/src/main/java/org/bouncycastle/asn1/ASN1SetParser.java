/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.InMemoryRepresentable;

public interface ASN1SetParser
extends ASN1Encodable,
InMemoryRepresentable {
    public ASN1Encodable readObject();
}

