package org.bouncycastle.asn1.eac;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class RSAPublicKey extends PublicKeyDataObject {
    private static int exponentValid;
    private static int modulusValid;
    private BigInteger exponent;
    private BigInteger modulus;
    private ASN1ObjectIdentifier usage;
    private int valid;

    static {
        modulusValid = 1;
        exponentValid = 2;
    }

    public RSAPublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, BigInteger bigInteger, BigInteger bigInteger2) {
        this.valid = 0;
        this.usage = aSN1ObjectIdentifier;
        this.modulus = bigInteger;
        this.exponent = bigInteger2;
    }

    RSAPublicKey(ASN1Sequence aSN1Sequence) {
        this.valid = 0;
        Enumeration objects = aSN1Sequence.getObjects();
        this.usage = ASN1ObjectIdentifier.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            UnsignedInteger instance = UnsignedInteger.getInstance(objects.nextElement());
            switch (instance.getTagNo()) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    setModulus(instance);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    setExponent(instance);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown DERTaggedObject :" + instance.getTagNo() + "-> not an Iso7816RSAPublicKeyStructure");
            }
        }
        if (this.valid != 3) {
            throw new IllegalArgumentException("missing argument -> not an Iso7816RSAPublicKeyStructure");
        }
    }

    private void setExponent(UnsignedInteger unsignedInteger) {
        if ((this.valid & exponentValid) == 0) {
            this.valid |= exponentValid;
            this.exponent = unsignedInteger.getValue();
            return;
        }
        throw new IllegalArgumentException("Exponent already set");
    }

    private void setModulus(UnsignedInteger unsignedInteger) {
        if ((this.valid & modulusValid) == 0) {
            this.valid |= modulusValid;
            this.modulus = unsignedInteger.getValue();
            return;
        }
        throw new IllegalArgumentException("Modulus already set");
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.exponent;
    }

    public ASN1ObjectIdentifier getUsage() {
        return this.usage;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.usage);
        aSN1EncodableVector.add(new UnsignedInteger(1, getModulus()));
        aSN1EncodableVector.add(new UnsignedInteger(2, getPublicExponent()));
        return new DERSequence(aSN1EncodableVector);
    }
}
