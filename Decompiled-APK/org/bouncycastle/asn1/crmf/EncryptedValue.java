package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class EncryptedValue extends ASN1Object {
    private DERBitString encSymmKey;
    private DERBitString encValue;
    private AlgorithmIdentifier intendedAlg;
    private AlgorithmIdentifier keyAlg;
    private AlgorithmIdentifier symmAlg;
    private ASN1OctetString valueHint;

    private EncryptedValue(ASN1Sequence aSN1Sequence) {
        int i = 0;
        while (aSN1Sequence.getObjectAt(i) instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Sequence.getObjectAt(i);
            switch (aSN1TaggedObject.getTagNo()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    this.intendedAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.symmAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.encSymmKey = DERBitString.getInstance(aSN1TaggedObject, false);
                    break;
                case F2m.PPB /*3*/:
                    this.keyAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    this.valueHint = ASN1OctetString.getInstance(aSN1TaggedObject, false);
                    break;
                default:
                    break;
            }
            i++;
        }
        this.encValue = DERBitString.getInstance(aSN1Sequence.getObjectAt(i));
    }

    public EncryptedValue(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2, DERBitString dERBitString, AlgorithmIdentifier algorithmIdentifier3, ASN1OctetString aSN1OctetString, DERBitString dERBitString2) {
        if (dERBitString2 == null) {
            throw new IllegalArgumentException("'encValue' cannot be null");
        }
        this.intendedAlg = algorithmIdentifier;
        this.symmAlg = algorithmIdentifier2;
        this.encSymmKey = dERBitString;
        this.keyAlg = algorithmIdentifier3;
        this.valueHint = aSN1OctetString;
        this.encValue = dERBitString2;
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int i, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, i, aSN1Encodable));
        }
    }

    public static EncryptedValue getInstance(Object obj) {
        return obj instanceof EncryptedValue ? (EncryptedValue) obj : obj != null ? new EncryptedValue(ASN1Sequence.getInstance(obj)) : null;
    }

    public DERBitString getEncSymmKey() {
        return this.encSymmKey;
    }

    public DERBitString getEncValue() {
        return this.encValue;
    }

    public AlgorithmIdentifier getIntendedAlg() {
        return this.intendedAlg;
    }

    public AlgorithmIdentifier getKeyAlg() {
        return this.keyAlg;
    }

    public AlgorithmIdentifier getSymmAlg() {
        return this.symmAlg;
    }

    public ASN1OctetString getValueHint() {
        return this.valueHint;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        addOptional(aSN1EncodableVector, 0, this.intendedAlg);
        addOptional(aSN1EncodableVector, 1, this.symmAlg);
        addOptional(aSN1EncodableVector, 2, this.encSymmKey);
        addOptional(aSN1EncodableVector, 3, this.keyAlg);
        addOptional(aSN1EncodableVector, 4, this.valueHint);
        aSN1EncodableVector.add(this.encValue);
        return new DERSequence(aSN1EncodableVector);
    }
}
