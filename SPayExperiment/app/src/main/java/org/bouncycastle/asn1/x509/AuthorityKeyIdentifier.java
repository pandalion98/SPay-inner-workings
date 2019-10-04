/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509;

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
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.digests.SHA1Digest;

public class AuthorityKeyIdentifier
extends ASN1Object {
    GeneralNames certissuer = null;
    ASN1Integer certserno = null;
    ASN1OctetString keyidentifier = null;

    protected AuthorityKeyIdentifier(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        block5 : while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = DERTaggedObject.getInstance(enumeration.nextElement());
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("illegal tag");
                }
                case 0: {
                    this.keyidentifier = ASN1OctetString.getInstance(aSN1TaggedObject, false);
                    continue block5;
                }
                case 1: {
                    this.certissuer = GeneralNames.getInstance(aSN1TaggedObject, false);
                    continue block5;
                }
                case 2: 
            }
            this.certserno = ASN1Integer.getInstance(aSN1TaggedObject, false);
        }
    }

    public AuthorityKeyIdentifier(GeneralNames generalNames, BigInteger bigInteger) {
        this((byte[])null, generalNames, bigInteger);
    }

    public AuthorityKeyIdentifier(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        SHA1Digest sHA1Digest = new SHA1Digest();
        byte[] arrby = new byte[sHA1Digest.getDigestSize()];
        byte[] arrby2 = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sHA1Digest.update(arrby2, 0, arrby2.length);
        sHA1Digest.doFinal(arrby, 0);
        this.keyidentifier = new DEROctetString(arrby);
    }

    public AuthorityKeyIdentifier(SubjectPublicKeyInfo subjectPublicKeyInfo, GeneralNames generalNames, BigInteger bigInteger) {
        SHA1Digest sHA1Digest = new SHA1Digest();
        byte[] arrby = new byte[sHA1Digest.getDigestSize()];
        byte[] arrby2 = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sHA1Digest.update(arrby2, 0, arrby2.length);
        sHA1Digest.doFinal(arrby, 0);
        this.keyidentifier = new DEROctetString(arrby);
        this.certissuer = GeneralNames.getInstance(generalNames.toASN1Primitive());
        this.certserno = new ASN1Integer(bigInteger);
    }

    public AuthorityKeyIdentifier(byte[] arrby) {
        this(arrby, null, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public AuthorityKeyIdentifier(byte[] arrby, GeneralNames generalNames, BigInteger bigInteger) {
        DEROctetString dEROctetString = arrby != null ? new DEROctetString(arrby) : null;
        this.keyidentifier = dEROctetString;
        this.certissuer = generalNames;
        ASN1Integer aSN1Integer = null;
        if (bigInteger != null) {
            aSN1Integer = new ASN1Integer(bigInteger);
        }
        this.certserno = aSN1Integer;
    }

    public static AuthorityKeyIdentifier fromExtensions(Extensions extensions) {
        return AuthorityKeyIdentifier.getInstance(extensions.getExtensionParsedValue(Extension.authorityKeyIdentifier));
    }

    public static AuthorityKeyIdentifier getInstance(Object object) {
        if (object instanceof AuthorityKeyIdentifier) {
            return (AuthorityKeyIdentifier)object;
        }
        if (object != null) {
            return new AuthorityKeyIdentifier(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static AuthorityKeyIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return AuthorityKeyIdentifier.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public GeneralNames getAuthorityCertIssuer() {
        return this.certissuer;
    }

    public BigInteger getAuthorityCertSerialNumber() {
        if (this.certserno != null) {
            return this.certserno.getValue();
        }
        return null;
    }

    public byte[] getKeyIdentifier() {
        if (this.keyidentifier != null) {
            return this.keyidentifier.getOctets();
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.keyidentifier != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.keyidentifier));
        }
        if (this.certissuer != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.certissuer));
        }
        if (this.certserno != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.certserno));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return "AuthorityKeyIdentifier: KeyID(" + this.keyidentifier.getOctets() + ")";
    }
}

