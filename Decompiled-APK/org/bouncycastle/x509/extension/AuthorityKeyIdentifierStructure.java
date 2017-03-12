package org.bouncycastle.x509.extension;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PrincipalUtil;

public class AuthorityKeyIdentifierStructure extends AuthorityKeyIdentifier {
    public AuthorityKeyIdentifierStructure(PublicKey publicKey) {
        super(fromKey(publicKey));
    }

    public AuthorityKeyIdentifierStructure(X509Certificate x509Certificate) {
        super(fromCertificate(x509Certificate));
    }

    public AuthorityKeyIdentifierStructure(Extension extension) {
        super((ASN1Sequence) extension.getParsedValue());
    }

    public AuthorityKeyIdentifierStructure(X509Extension x509Extension) {
        super((ASN1Sequence) x509Extension.getParsedValue());
    }

    public AuthorityKeyIdentifierStructure(byte[] bArr) {
        super((ASN1Sequence) X509ExtensionUtil.fromExtensionValue(bArr));
    }

    private static ASN1Sequence fromCertificate(X509Certificate x509Certificate) {
        try {
            if (x509Certificate.getVersion() != 3) {
                return (ASN1Sequence) new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()), new GeneralNames(new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate))), x509Certificate.getSerialNumber()).toASN1Object();
            }
            GeneralName generalName = new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate));
            byte[] extensionValue = x509Certificate.getExtensionValue(X509Extensions.SubjectKeyIdentifier.getId());
            return extensionValue != null ? (ASN1Sequence) new AuthorityKeyIdentifier(((ASN1OctetString) X509ExtensionUtil.fromExtensionValue(extensionValue)).getOctets(), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Object() : (ASN1Sequence) new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Object();
        } catch (Exception e) {
            throw new CertificateParsingException("Exception extracting certificate details: " + e.toString());
        }
    }

    private static ASN1Sequence fromKey(PublicKey publicKey) {
        try {
            return (ASN1Sequence) new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(publicKey.getEncoded()).readObject())).toASN1Object();
        } catch (Exception e) {
            throw new InvalidKeyException("can't process key: " + e);
        }
    }
}
