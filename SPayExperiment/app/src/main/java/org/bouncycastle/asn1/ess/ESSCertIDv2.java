/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.IssuerSerial;

public class ESSCertIDv2
extends ASN1Object {
    private static final AlgorithmIdentifier DEFAULT_ALG_ID = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
    private byte[] certHash;
    private AlgorithmIdentifier hashAlgorithm;
    private IssuerSerial issuerSerial;

    /*
     * Enabled aggressive block sorting
     */
    private ESSCertIDv2(ASN1Sequence aSN1Sequence) {
        int n2 = 0;
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1OctetString) {
            this.hashAlgorithm = DEFAULT_ALG_ID;
        } else {
            this.hashAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(0).toASN1Primitive());
            n2 = 1;
        }
        int n3 = n2 + 1;
        this.certHash = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(n2).toASN1Primitive()).getOctets();
        if (aSN1Sequence.size() > n3) {
            this.issuerSerial = IssuerSerial.getInstance(aSN1Sequence.getObjectAt(n3));
        }
    }

    public ESSCertIDv2(AlgorithmIdentifier algorithmIdentifier, byte[] arrby) {
        this(algorithmIdentifier, arrby, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ESSCertIDv2(AlgorithmIdentifier algorithmIdentifier, byte[] arrby, IssuerSerial issuerSerial) {
        this.hashAlgorithm = algorithmIdentifier == null ? DEFAULT_ALG_ID : algorithmIdentifier;
        this.certHash = arrby;
        this.issuerSerial = issuerSerial;
    }

    public ESSCertIDv2(byte[] arrby) {
        this(null, arrby, null);
    }

    public ESSCertIDv2(byte[] arrby, IssuerSerial issuerSerial) {
        this(null, arrby, issuerSerial);
    }

    public static ESSCertIDv2 getInstance(Object object) {
        if (object instanceof ESSCertIDv2) {
            return (ESSCertIDv2)object;
        }
        if (object != null) {
            return new ESSCertIDv2(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public byte[] getCertHash() {
        return this.certHash;
    }

    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(DEFAULT_ALG_ID)) {
            aSN1EncodableVector.add(this.hashAlgorithm);
        }
        aSN1EncodableVector.add(new DEROctetString(this.certHash).toASN1Primitive());
        if (this.issuerSerial != null) {
            aSN1EncodableVector.add(this.issuerSerial);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

