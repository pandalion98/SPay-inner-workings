/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.DigestInfo;

public class MacData
extends ASN1Object {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    DigestInfo digInfo;
    BigInteger iterationCount;
    byte[] salt;

    private MacData(ASN1Sequence aSN1Sequence) {
        this.digInfo = DigestInfo.getInstance(aSN1Sequence.getObjectAt(0));
        this.salt = ((ASN1OctetString)aSN1Sequence.getObjectAt(1)).getOctets();
        if (aSN1Sequence.size() == 3) {
            this.iterationCount = ((ASN1Integer)aSN1Sequence.getObjectAt(2)).getValue();
            return;
        }
        this.iterationCount = ONE;
    }

    public MacData(DigestInfo digestInfo, byte[] arrby, int n2) {
        this.digInfo = digestInfo;
        this.salt = arrby;
        this.iterationCount = BigInteger.valueOf((long)n2);
    }

    public static MacData getInstance(Object object) {
        if (object instanceof MacData) {
            return (MacData)object;
        }
        if (object != null) {
            return new MacData(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public BigInteger getIterationCount() {
        return this.iterationCount;
    }

    public DigestInfo getMac() {
        return this.digInfo;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.digInfo);
        aSN1EncodableVector.add(new DEROctetString(this.salt));
        if (!this.iterationCount.equals((Object)ONE)) {
            aSN1EncodableVector.add(new ASN1Integer(this.iterationCount));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

