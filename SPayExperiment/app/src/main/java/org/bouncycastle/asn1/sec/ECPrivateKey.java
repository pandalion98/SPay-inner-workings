/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.util.Enumeration
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.asn1.sec;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.util.BigIntegers;

public class ECPrivateKey
extends ASN1Object {
    private ASN1Sequence seq;

    public ECPrivateKey(int n2, BigInteger bigInteger) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((int)((n2 + 7) / 8), (BigInteger)bigInteger);
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer(1L));
        aSN1EncodableVector.add(new DEROctetString(arrby));
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    public ECPrivateKey(int n2, BigInteger bigInteger, ASN1Encodable aSN1Encodable) {
        this(n2, bigInteger, null, aSN1Encodable);
    }

    public ECPrivateKey(int n2, BigInteger bigInteger, DERBitString dERBitString, ASN1Encodable aSN1Encodable) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((int)((n2 + 7) / 8), (BigInteger)bigInteger);
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer(1L));
        aSN1EncodableVector.add(new DEROctetString(arrby));
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, aSN1Encodable));
        }
        if (dERBitString != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, dERBitString));
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    public ECPrivateKey(BigInteger bigInteger) {
        this(bigInteger.bitLength(), bigInteger);
    }

    public ECPrivateKey(BigInteger bigInteger, ASN1Encodable aSN1Encodable) {
        this(bigInteger, null, aSN1Encodable);
    }

    public ECPrivateKey(BigInteger bigInteger, DERBitString dERBitString, ASN1Encodable aSN1Encodable) {
        this(bigInteger.bitLength(), bigInteger, dERBitString, aSN1Encodable);
    }

    private ECPrivateKey(ASN1Sequence aSN1Sequence) {
        this.seq = aSN1Sequence;
    }

    public static ECPrivateKey getInstance(Object object) {
        if (object instanceof ECPrivateKey) {
            return (ECPrivateKey)object;
        }
        if (object != null) {
            return new ECPrivateKey(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    private ASN1Primitive getObjectInTag(int n2) {
        Enumeration enumeration = this.seq.getObjects();
        while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject;
            ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
            if (!(aSN1Encodable instanceof ASN1TaggedObject) || (aSN1TaggedObject = (ASN1TaggedObject)aSN1Encodable).getTagNo() != n2) continue;
            return aSN1TaggedObject.getObject().toASN1Primitive();
        }
        return null;
    }

    public BigInteger getKey() {
        return new BigInteger(1, ((ASN1OctetString)this.seq.getObjectAt(1)).getOctets());
    }

    public ASN1Primitive getParameters() {
        return this.getObjectInTag(0);
    }

    public DERBitString getPublicKey() {
        return (DERBitString)this.getObjectInTag(1);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}

