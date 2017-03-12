package org.bouncycastle.pqc.asn1;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McEliecePublicKey extends ASN1Object {
    private byte[] matrixG;
    private int f389n;
    private ASN1ObjectIdentifier oid;
    private int f390t;

    public McEliecePublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, int i, int i2, GF2Matrix gF2Matrix) {
        this.oid = aSN1ObjectIdentifier;
        this.f389n = i;
        this.f390t = i2;
        this.matrixG = gF2Matrix.getEncoded();
    }

    private McEliecePublicKey(ASN1Sequence aSN1Sequence) {
        this.oid = (ASN1ObjectIdentifier) aSN1Sequence.getObjectAt(0);
        this.f389n = ((ASN1Integer) aSN1Sequence.getObjectAt(1)).getValue().intValue();
        this.f390t = ((ASN1Integer) aSN1Sequence.getObjectAt(2)).getValue().intValue();
        this.matrixG = ((ASN1OctetString) aSN1Sequence.getObjectAt(3)).getOctets();
    }

    public static McEliecePublicKey getInstance(Object obj) {
        return obj instanceof McEliecePublicKey ? (McEliecePublicKey) obj : obj != null ? new McEliecePublicKey(ASN1Sequence.getInstance(obj)) : null;
    }

    public GF2Matrix getG() {
        return new GF2Matrix(this.matrixG);
    }

    public int getN() {
        return this.f389n;
    }

    public ASN1ObjectIdentifier getOID() {
        return this.oid;
    }

    public int getT() {
        return this.f390t;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.oid);
        aSN1EncodableVector.add(new ASN1Integer((long) this.f389n));
        aSN1EncodableVector.add(new ASN1Integer((long) this.f390t));
        aSN1EncodableVector.add(new DEROctetString(this.matrixG));
        return new DERSequence(aSN1EncodableVector);
    }
}
