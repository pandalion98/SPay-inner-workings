/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PBKDF2Params
extends ASN1Object {
    private static final AlgorithmIdentifier algid_hmacWithSHA1 = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA1, DERNull.INSTANCE);
    private ASN1Integer iterationCount;
    private ASN1Integer keyLength;
    private ASN1OctetString octStr;
    private AlgorithmIdentifier prf;

    /*
     * Enabled aggressive block sorting
     */
    private PBKDF2Params(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.octStr = (ASN1OctetString)enumeration.nextElement();
        this.iterationCount = (ASN1Integer)enumeration.nextElement();
        if (!enumeration.hasMoreElements()) return;
        Object object = enumeration.nextElement();
        if (object instanceof ASN1Integer) {
            this.keyLength = ASN1Integer.getInstance(object);
            if (!enumeration.hasMoreElements()) return;
            object = enumeration.nextElement();
        } else {
            this.keyLength = null;
        }
        if (object == null) return;
        this.prf = AlgorithmIdentifier.getInstance(object);
    }

    public PBKDF2Params(byte[] arrby, int n2) {
        this.octStr = new DEROctetString(arrby);
        this.iterationCount = new ASN1Integer(n2);
    }

    public PBKDF2Params(byte[] arrby, int n2, int n3) {
        this(arrby, n2);
        this.keyLength = new ASN1Integer(n3);
    }

    public PBKDF2Params(byte[] arrby, int n2, int n3, AlgorithmIdentifier algorithmIdentifier) {
        this(arrby, n2);
        this.keyLength = new ASN1Integer(n3);
        this.prf = algorithmIdentifier;
    }

    public PBKDF2Params(byte[] arrby, int n2, AlgorithmIdentifier algorithmIdentifier) {
        this(arrby, n2);
        this.prf = algorithmIdentifier;
    }

    public static PBKDF2Params getInstance(Object object) {
        if (object instanceof PBKDF2Params) {
            return (PBKDF2Params)object;
        }
        if (object != null) {
            return new PBKDF2Params(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public BigInteger getIterationCount() {
        return this.iterationCount.getValue();
    }

    public BigInteger getKeyLength() {
        if (this.keyLength != null) {
            return this.keyLength.getValue();
        }
        return null;
    }

    public AlgorithmIdentifier getPrf() {
        if (this.prf != null) {
            return this.prf;
        }
        return algid_hmacWithSHA1;
    }

    public byte[] getSalt() {
        return this.octStr.getOctets();
    }

    public boolean isDefaultPrf() {
        return this.prf == null || this.prf.equals(algid_hmacWithSHA1);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.octStr);
        aSN1EncodableVector.add(this.iterationCount);
        if (this.keyLength != null) {
            aSN1EncodableVector.add(this.keyLength);
        }
        if (this.prf != null && !this.prf.equals(algid_hmacWithSHA1)) {
            aSN1EncodableVector.add(this.prf);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

