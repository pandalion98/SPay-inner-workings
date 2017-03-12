package org.bouncycastle.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.Arrays;

public class X509V2AttributeCertificate implements X509AttributeCertificate {
    private AttributeCertificate cert;
    private Date notAfter;
    private Date notBefore;

    public X509V2AttributeCertificate(InputStream inputStream) {
        this(getObject(inputStream));
    }

    X509V2AttributeCertificate(AttributeCertificate attributeCertificate) {
        this.cert = attributeCertificate;
        try {
            this.notAfter = attributeCertificate.getAcinfo().getAttrCertValidityPeriod().getNotAfterTime().getDate();
            this.notBefore = attributeCertificate.getAcinfo().getAttrCertValidityPeriod().getNotBeforeTime().getDate();
        } catch (ParseException e) {
            throw new IOException("invalid data structure in certificate!");
        }
    }

    public X509V2AttributeCertificate(byte[] bArr) {
        this(new ByteArrayInputStream(bArr));
    }

    private Set getExtensionOIDs(boolean z) {
        Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions == null) {
            return null;
        }
        Set hashSet = new HashSet();
        Enumeration oids = extensions.oids();
        while (oids.hasMoreElements()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
            if (extensions.getExtension(aSN1ObjectIdentifier).isCritical() == z) {
                hashSet.add(aSN1ObjectIdentifier.getId());
            }
        }
        return hashSet;
    }

    private static AttributeCertificate getObject(InputStream inputStream) {
        try {
            return AttributeCertificate.getInstance(new ASN1InputStream(inputStream).readObject());
        } catch (IOException e) {
            throw e;
        } catch (Exception e2) {
            throw new IOException("exception decoding certificate structure: " + e2.toString());
        }
    }

    public void checkValidity() {
        checkValidity(new Date());
    }

    public void checkValidity(Date date) {
        if (date.after(getNotAfter())) {
            throw new CertificateExpiredException("certificate expired on " + getNotAfter());
        } else if (date.before(getNotBefore())) {
            throw new CertificateNotYetValidException("certificate not valid till " + getNotBefore());
        }
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof X509AttributeCertificate)) {
            return z;
        }
        try {
            return Arrays.areEqual(getEncoded(), ((X509AttributeCertificate) obj).getEncoded());
        } catch (IOException e) {
            return z;
        }
    }

    public X509Attribute[] getAttributes() {
        ASN1Sequence attributes = this.cert.getAcinfo().getAttributes();
        X509Attribute[] x509AttributeArr = new X509Attribute[attributes.size()];
        for (int i = 0; i != attributes.size(); i++) {
            x509AttributeArr[i] = new X509Attribute(attributes.getObjectAt(i));
        }
        return x509AttributeArr;
    }

    public X509Attribute[] getAttributes(String str) {
        ASN1Sequence attributes = this.cert.getAcinfo().getAttributes();
        List arrayList = new ArrayList();
        for (int i = 0; i != attributes.size(); i++) {
            X509Attribute x509Attribute = new X509Attribute(attributes.getObjectAt(i));
            if (x509Attribute.getOID().equals(str)) {
                arrayList.add(x509Attribute);
            }
        }
        return arrayList.size() == 0 ? null : (X509Attribute[]) arrayList.toArray(new X509Attribute[arrayList.size()]);
    }

    public Set getCriticalExtensionOIDs() {
        return getExtensionOIDs(true);
    }

    public byte[] getEncoded() {
        return this.cert.getEncoded();
    }

    public byte[] getExtensionValue(String str) {
        Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null) {
            Extension extension = extensions.getExtension(new ASN1ObjectIdentifier(str));
            if (extension != null) {
                try {
                    return extension.getExtnValue().getEncoded(ASN1Encoding.DER);
                } catch (Exception e) {
                    throw new RuntimeException("error encoding " + e.toString());
                }
            }
        }
        return null;
    }

    public AttributeCertificateHolder getHolder() {
        return new AttributeCertificateHolder((ASN1Sequence) this.cert.getAcinfo().getHolder().toASN1Object());
    }

    public AttributeCertificateIssuer getIssuer() {
        return new AttributeCertificateIssuer(this.cert.getAcinfo().getIssuer());
    }

    public boolean[] getIssuerUniqueID() {
        DERBitString issuerUniqueID = this.cert.getAcinfo().getIssuerUniqueID();
        if (issuerUniqueID == null) {
            return null;
        }
        byte[] bytes = issuerUniqueID.getBytes();
        boolean[] zArr = new boolean[((bytes.length * 8) - issuerUniqueID.getPadBits())];
        for (int i = 0; i != zArr.length; i++) {
            zArr[i] = (bytes[i / 8] & (X509KeyUsage.digitalSignature >>> (i % 8))) != 0;
        }
        return zArr;
    }

    public Set getNonCriticalExtensionOIDs() {
        return getExtensionOIDs(false);
    }

    public Date getNotAfter() {
        return this.notAfter;
    }

    public Date getNotBefore() {
        return this.notBefore;
    }

    public BigInteger getSerialNumber() {
        return this.cert.getAcinfo().getSerialNumber().getValue();
    }

    public byte[] getSignature() {
        return this.cert.getSignatureValue().getBytes();
    }

    public int getVersion() {
        return this.cert.getAcinfo().getVersion().getValue().intValue() + 1;
    }

    public boolean hasUnsupportedCriticalExtension() {
        Set criticalExtensionOIDs = getCriticalExtensionOIDs();
        return (criticalExtensionOIDs == null || criticalExtensionOIDs.isEmpty()) ? false : true;
    }

    public int hashCode() {
        try {
            return Arrays.hashCode(getEncoded());
        } catch (IOException e) {
            return 0;
        }
    }

    public final void verify(PublicKey publicKey, String str) {
        if (this.cert.getSignatureAlgorithm().equals(this.cert.getAcinfo().getSignature())) {
            Signature instance = Signature.getInstance(this.cert.getSignatureAlgorithm().getObjectId().getId(), str);
            instance.initVerify(publicKey);
            try {
                instance.update(this.cert.getAcinfo().getEncoded());
                if (!instance.verify(getSignature())) {
                    throw new InvalidKeyException("Public key presented not for certificate signature");
                }
                return;
            } catch (IOException e) {
                throw new SignatureException("Exception encoding certificate info object");
            }
        }
        throw new CertificateException("Signature algorithm in certificate info not same as outer certificate");
    }
}
