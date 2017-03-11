package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PBKDF2Params extends ASN1Object {
    private static final AlgorithmIdentifier algid_hmacWithSHA1;
    private ASN1Integer iterationCount;
    private ASN1Integer keyLength;
    private ASN1OctetString octStr;
    private AlgorithmIdentifier prf;

    static {
        algid_hmacWithSHA1 = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA1, DERNull.INSTANCE);
    }

    private PBKDF2Params(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.octStr = (ASN1OctetString) objects.nextElement();
        this.iterationCount = (ASN1Integer) objects.nextElement();
        if (objects.hasMoreElements()) {
            Object nextElement = objects.nextElement();
            if (nextElement instanceof ASN1Integer) {
                this.keyLength = ASN1Integer.getInstance(nextElement);
                nextElement = objects.hasMoreElements() ? objects.nextElement() : null;
            } else {
                this.keyLength = null;
            }
            if (nextElement != null) {
                this.prf = AlgorithmIdentifier.getInstance(nextElement);
            }
        }
    }

    public PBKDF2Params(byte[] bArr, int i) {
        this.octStr = new DEROctetString(bArr);
        this.iterationCount = new ASN1Integer((long) i);
    }

    public PBKDF2Params(byte[] bArr, int i, int i2) {
        this(bArr, i);
        this.keyLength = new ASN1Integer((long) i2);
    }

    public PBKDF2Params(byte[] bArr, int i, int i2, AlgorithmIdentifier algorithmIdentifier) {
        this(bArr, i);
        this.keyLength = new ASN1Integer((long) i2);
        this.prf = algorithmIdentifier;
    }

    public PBKDF2Params(byte[] bArr, int i, AlgorithmIdentifier algorithmIdentifier) {
        this(bArr, i);
        this.prf = algorithmIdentifier;
    }

    public static PBKDF2Params getInstance(Object obj) {
        return obj instanceof PBKDF2Params ? (PBKDF2Params) obj : obj != null ? new PBKDF2Params(ASN1Sequence.getInstance(obj)) : null;
    }

    public BigInteger getIterationCount() {
        return this.iterationCount.getValue();
    }

    public BigInteger getKeyLength() {
        return this.keyLength != null ? this.keyLength.getValue() : null;
    }

    public AlgorithmIdentifier getPrf() {
        return this.prf != null ? this.prf : algid_hmacWithSHA1;
    }

    public byte[] getSalt() {
        return this.octStr.getOctets();
    }

    public boolean isDefaultPrf() {
        return this.prf == null || this.prf.equals(algid_hmacWithSHA1);
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.octStr);
        aSN1EncodableVector.add(this.iterationCount);
        if (this.keyLength != null) {
            aSN1EncodableVector.add(this.keyLength);
        }
        if (!(this.prf == null || this.prf.equals(algid_hmacWithSHA1))) {
            aSN1EncodableVector.add(this.prf);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
