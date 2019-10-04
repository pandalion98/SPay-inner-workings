/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.pqc.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;

public class RainbowPublicKey
extends ASN1Object {
    private byte[][] coeffQuadratic;
    private byte[] coeffScalar;
    private byte[][] coeffSingular;
    private ASN1Integer docLength;
    private ASN1ObjectIdentifier oid;
    private ASN1Integer version;

    public RainbowPublicKey(int n, short[][] arrs, short[][] arrs2, short[] arrs3) {
        this.version = new ASN1Integer(0L);
        this.docLength = new ASN1Integer(n);
        this.coeffQuadratic = RainbowUtil.convertArray(arrs);
        this.coeffSingular = RainbowUtil.convertArray(arrs2);
        this.coeffScalar = RainbowUtil.convertArray(arrs3);
    }

    /*
     * Enabled aggressive block sorting
     */
    private RainbowPublicKey(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
        } else {
            this.oid = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        }
        this.docLength = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(1));
        ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(2));
        this.coeffQuadratic = new byte[aSN1Sequence2.size()][];
        for (int i = 0; i < aSN1Sequence2.size(); ++i) {
            this.coeffQuadratic[i] = ASN1OctetString.getInstance(aSN1Sequence2.getObjectAt(i)).getOctets();
        }
        ASN1Sequence aSN1Sequence3 = (ASN1Sequence)aSN1Sequence.getObjectAt(3);
        this.coeffSingular = new byte[aSN1Sequence3.size()][];
        int n = 0;
        do {
            if (n >= aSN1Sequence3.size()) {
                this.coeffScalar = ASN1OctetString.getInstance(((ASN1Sequence)aSN1Sequence.getObjectAt(4)).getObjectAt(0)).getOctets();
                return;
            }
            this.coeffSingular[n] = ASN1OctetString.getInstance(aSN1Sequence3.getObjectAt(n)).getOctets();
            ++n;
        } while (true);
    }

    public static RainbowPublicKey getInstance(Object object) {
        if (object instanceof RainbowPublicKey) {
            return (RainbowPublicKey)object;
        }
        if (object != null) {
            return new RainbowPublicKey(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public short[][] getCoeffQuadratic() {
        return RainbowUtil.convertArray(this.coeffQuadratic);
    }

    public short[] getCoeffScalar() {
        return RainbowUtil.convertArray(this.coeffScalar);
    }

    public short[][] getCoeffSingular() {
        return RainbowUtil.convertArray(this.coeffSingular);
    }

    public int getDocLength() {
        return this.docLength.getValue().intValue();
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        int n = 0;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != null) {
            aSN1EncodableVector.add(this.version);
        } else {
            aSN1EncodableVector.add(this.oid);
        }
        aSN1EncodableVector.add(this.docLength);
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i = 0; i < this.coeffQuadratic.length; ++i) {
            aSN1EncodableVector2.add(new DEROctetString(this.coeffQuadratic[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        do {
            if (n >= this.coeffSingular.length) {
                aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector3));
                ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
                aSN1EncodableVector4.add(new DEROctetString(this.coeffScalar));
                aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector4));
                return new DERSequence(aSN1EncodableVector);
            }
            aSN1EncodableVector3.add(new DEROctetString(this.coeffSingular[n]));
            ++n;
        } while (true);
    }
}

