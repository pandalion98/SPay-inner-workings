/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.DerivationParameters;

public class DHKDFParameters
implements DerivationParameters {
    private ASN1ObjectIdentifier algorithm;
    private byte[] extraInfo;
    private int keySize;
    private byte[] z;

    public DHKDFParameters(ASN1ObjectIdentifier aSN1ObjectIdentifier, int n2, byte[] arrby) {
        this(aSN1ObjectIdentifier, n2, arrby, null);
    }

    public DHKDFParameters(ASN1ObjectIdentifier aSN1ObjectIdentifier, int n2, byte[] arrby, byte[] arrby2) {
        this.algorithm = aSN1ObjectIdentifier;
        this.keySize = n2;
        this.z = arrby;
        this.extraInfo = arrby2;
    }

    public ASN1ObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }

    public byte[] getExtraInfo() {
        return this.extraInfo;
    }

    public int getKeySize() {
        return this.keySize;
    }

    public byte[] getZ() {
        return this.z;
    }
}

