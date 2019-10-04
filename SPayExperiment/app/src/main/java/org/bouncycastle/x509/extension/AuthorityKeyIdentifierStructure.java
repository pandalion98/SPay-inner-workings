/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PublicKey
 *  java.security.cert.CertificateParsingException
 *  java.security.cert.X509Certificate
 */
package org.bouncycastle.x509.extension;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class AuthorityKeyIdentifierStructure
extends AuthorityKeyIdentifier {
    public AuthorityKeyIdentifierStructure(PublicKey publicKey) {
        super(AuthorityKeyIdentifierStructure.fromKey(publicKey));
    }

    public AuthorityKeyIdentifierStructure(X509Certificate x509Certificate) {
        super(AuthorityKeyIdentifierStructure.fromCertificate(x509Certificate));
    }

    public AuthorityKeyIdentifierStructure(Extension extension) {
        super((ASN1Sequence)extension.getParsedValue());
    }

    public AuthorityKeyIdentifierStructure(X509Extension x509Extension) {
        super((ASN1Sequence)x509Extension.getParsedValue());
    }

    public AuthorityKeyIdentifierStructure(byte[] arrby) {
        super((ASN1Sequence)X509ExtensionUtil.fromExtensionValue(arrby));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static ASN1Sequence fromCertificate(X509Certificate x509Certificate) {
        byte[] arrby;
        GeneralName generalName;
        try {
            if (x509Certificate.getVersion() != 3) {
                GeneralName generalName2 = new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate));
                return (ASN1Sequence)new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()), new GeneralNames(generalName2), x509Certificate.getSerialNumber()).toASN1Object();
            }
            generalName = new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate));
            arrby = x509Certificate.getExtensionValue(X509Extensions.SubjectKeyIdentifier.getId());
            if (arrby == null) return (ASN1Sequence)new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Object();
        }
        catch (Exception exception) {
            throw new CertificateParsingException("Exception extracting certificate details: " + exception.toString());
        }
        return (ASN1Sequence)new AuthorityKeyIdentifier(((ASN1OctetString)X509ExtensionUtil.fromExtensionValue(arrby)).getOctets(), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Object();
    }

    private static ASN1Sequence fromKey(PublicKey publicKey) {
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(publicKey.getEncoded()).readObject())).toASN1Object();
            return aSN1Sequence;
        }
        catch (Exception exception) {
            throw new InvalidKeyException("can't process key: " + (Object)((Object)exception));
        }
    }
}

