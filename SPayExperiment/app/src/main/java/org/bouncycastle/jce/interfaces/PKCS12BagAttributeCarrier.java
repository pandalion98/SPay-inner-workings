/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.jce.interfaces;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface PKCS12BagAttributeCarrier {
    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier var1);

    public Enumeration getBagAttributeKeys();

    public void setBagAttribute(ASN1ObjectIdentifier var1, ASN1Encodable var2);
}

