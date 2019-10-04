/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class RSASSAPSSparams
extends ASN1Object {
    public static final AlgorithmIdentifier DEFAULT_HASH_ALGORITHM = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
    public static final AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, DEFAULT_HASH_ALGORITHM);
    public static final ASN1Integer DEFAULT_SALT_LENGTH = new ASN1Integer(20L);
    public static final ASN1Integer DEFAULT_TRAILER_FIELD = new ASN1Integer(1L);
    private AlgorithmIdentifier hashAlgorithm;
    private AlgorithmIdentifier maskGenAlgorithm;
    private ASN1Integer saltLength;
    private ASN1Integer trailerField;

    public RSASSAPSSparams() {
        this.hashAlgorithm = DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = DEFAULT_MASK_GEN_FUNCTION;
        this.saltLength = DEFAULT_SALT_LENGTH;
        this.trailerField = DEFAULT_TRAILER_FIELD;
    }

    /*
     * Enabled aggressive block sorting
     */
    private RSASSAPSSparams(ASN1Sequence aSN1Sequence) {
        this.hashAlgorithm = DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = DEFAULT_MASK_GEN_FUNCTION;
        this.saltLength = DEFAULT_SALT_LENGTH;
        this.trailerField = DEFAULT_TRAILER_FIELD;
        int n2 = 0;
        while (n2 != aSN1Sequence.size()) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Sequence.getObjectAt(n2);
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("unknown tag");
                }
                case 0: {
                    this.hashAlgorithm = AlgorithmIdentifier.getInstance(aSN1TaggedObject, true);
                    break;
                }
                case 1: {
                    this.maskGenAlgorithm = AlgorithmIdentifier.getInstance(aSN1TaggedObject, true);
                    break;
                }
                case 2: {
                    this.saltLength = ASN1Integer.getInstance(aSN1TaggedObject, true);
                    break;
                }
                case 3: {
                    this.trailerField = ASN1Integer.getInstance(aSN1TaggedObject, true);
                }
            }
            ++n2;
        }
        return;
    }

    public RSASSAPSSparams(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2, ASN1Integer aSN1Integer, ASN1Integer aSN1Integer2) {
        this.hashAlgorithm = algorithmIdentifier;
        this.maskGenAlgorithm = algorithmIdentifier2;
        this.saltLength = aSN1Integer;
        this.trailerField = aSN1Integer2;
    }

    public static RSASSAPSSparams getInstance(Object object) {
        if (object instanceof RSASSAPSSparams) {
            return (RSASSAPSSparams)object;
        }
        if (object != null) {
            return new RSASSAPSSparams(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public AlgorithmIdentifier getMaskGenAlgorithm() {
        return this.maskGenAlgorithm;
    }

    public BigInteger getSaltLength() {
        return this.saltLength.getValue();
    }

    public BigInteger getTrailerField() {
        return this.trailerField.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(DEFAULT_HASH_ALGORITHM)) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.hashAlgorithm));
        }
        if (!this.maskGenAlgorithm.equals(DEFAULT_MASK_GEN_FUNCTION)) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.maskGenAlgorithm));
        }
        if (!this.saltLength.equals(DEFAULT_SALT_LENGTH)) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.saltLength));
        }
        if (!this.trailerField.equals(DEFAULT_TRAILER_FIELD)) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 3, this.trailerField));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

