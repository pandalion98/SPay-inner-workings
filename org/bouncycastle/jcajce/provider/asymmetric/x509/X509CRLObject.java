package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.TBSCertList.CRLEntry;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

class X509CRLObject extends X509CRL {
    private CertificateList f288c;
    private int hashCodeValue;
    private boolean isHashCodeSet;
    private boolean isIndirect;
    private String sigAlgName;
    private byte[] sigAlgParams;

    protected X509CRLObject(CertificateList certificateList) {
        this.isHashCodeSet = false;
        this.f288c = certificateList;
        try {
            this.sigAlgName = X509SignatureUtil.getSignatureName(certificateList.getSignatureAlgorithm());
            if (certificateList.getSignatureAlgorithm().getParameters() != null) {
                this.sigAlgParams = certificateList.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded(ASN1Encoding.DER);
            } else {
                this.sigAlgParams = null;
            }
            this.isIndirect = isIndirectCRL(this);
        } catch (Exception e) {
            throw new CRLException("CRL contents invalid: " + e);
        }
    }

    private Set getExtensionOIDs(boolean z) {
        if (getVersion() == 2) {
            Extensions extensions = this.f288c.getTBSCertList().getExtensions();
            if (extensions != null) {
                Set hashSet = new HashSet();
                Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                    if (z == extensions.getExtension(aSN1ObjectIdentifier).isCritical()) {
                        hashSet.add(aSN1ObjectIdentifier.getId());
                    }
                }
                return hashSet;
            }
        }
        return null;
    }

    static boolean isIndirectCRL(X509CRL x509crl) {
        try {
            Object extensionValue = x509crl.getExtensionValue(Extension.issuingDistributionPoint.getId());
            return extensionValue != null && IssuingDistributionPoint.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets()).isIndirectCRL();
        } catch (Throwable e) {
            throw new ExtCRLException("Exception reading IssuingDistributionPoint", e);
        }
    }

    private Set loadCRLEntries() {
        Set hashSet = new HashSet();
        Enumeration revokedCertificateEnumeration = this.f288c.getRevokedCertificateEnumeration();
        X500Name x500Name = null;
        while (revokedCertificateEnumeration.hasMoreElements()) {
            X500Name instance;
            CRLEntry cRLEntry = (CRLEntry) revokedCertificateEnumeration.nextElement();
            hashSet.add(new X509CRLEntryObject(cRLEntry, this.isIndirect, x500Name));
            if (this.isIndirect && cRLEntry.hasExtensions()) {
                Extension extension = cRLEntry.getExtensions().getExtension(Extension.certificateIssuer);
                if (extension != null) {
                    instance = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
                    x500Name = instance;
                }
            }
            instance = x500Name;
            x500Name = instance;
        }
        return hashSet;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X509CRL)) {
            return false;
        }
        if (!(obj instanceof X509CRLObject)) {
            return super.equals(obj);
        }
        X509CRLObject x509CRLObject = (X509CRLObject) obj;
        return (this.isHashCodeSet && x509CRLObject.isHashCodeSet && x509CRLObject.hashCodeValue != this.hashCodeValue) ? false : this.f288c.equals(x509CRLObject.f288c);
    }

    public Set getCriticalExtensionOIDs() {
        return getExtensionOIDs(true);
    }

    public byte[] getEncoded() {
        try {
            return this.f288c.getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            throw new CRLException(e.toString());
        }
    }

    public byte[] getExtensionValue(String str) {
        Extensions extensions = this.f288c.getTBSCertList().getExtensions();
        if (extensions != null) {
            Extension extension = extensions.getExtension(new ASN1ObjectIdentifier(str));
            if (extension != null) {
                try {
                    return extension.getExtnValue().getEncoded();
                } catch (Exception e) {
                    throw new IllegalStateException("error parsing " + e.toString());
                }
            }
        }
        return null;
    }

    public Principal getIssuerDN() {
        return new X509Principal(X500Name.getInstance(this.f288c.getIssuer().toASN1Primitive()));
    }

    public X500Principal getIssuerX500Principal() {
        try {
            return new X500Principal(this.f288c.getIssuer().getEncoded());
        } catch (IOException e) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public Date getNextUpdate() {
        return this.f288c.getNextUpdate() != null ? this.f288c.getNextUpdate().getDate() : null;
    }

    public Set getNonCriticalExtensionOIDs() {
        return getExtensionOIDs(false);
    }

    public X509CRLEntry getRevokedCertificate(BigInteger bigInteger) {
        Enumeration revokedCertificateEnumeration = this.f288c.getRevokedCertificateEnumeration();
        X500Name x500Name = null;
        while (revokedCertificateEnumeration.hasMoreElements()) {
            CRLEntry cRLEntry = (CRLEntry) revokedCertificateEnumeration.nextElement();
            if (bigInteger.equals(cRLEntry.getUserCertificate().getValue())) {
                return new X509CRLEntryObject(cRLEntry, this.isIndirect, x500Name);
            }
            X500Name instance;
            if (this.isIndirect && cRLEntry.hasExtensions()) {
                Extension extension = cRLEntry.getExtensions().getExtension(Extension.certificateIssuer);
                if (extension != null) {
                    instance = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
                    x500Name = instance;
                }
            }
            instance = x500Name;
            x500Name = instance;
        }
        return null;
    }

    public Set getRevokedCertificates() {
        Set loadCRLEntries = loadCRLEntries();
        return !loadCRLEntries.isEmpty() ? Collections.unmodifiableSet(loadCRLEntries) : null;
    }

    public String getSigAlgName() {
        return this.sigAlgName;
    }

    public String getSigAlgOID() {
        return this.f288c.getSignatureAlgorithm().getAlgorithm().getId();
    }

    public byte[] getSigAlgParams() {
        if (this.sigAlgParams == null) {
            return null;
        }
        Object obj = new byte[this.sigAlgParams.length];
        System.arraycopy(this.sigAlgParams, 0, obj, 0, obj.length);
        return obj;
    }

    public byte[] getSignature() {
        return this.f288c.getSignature().getBytes();
    }

    public byte[] getTBSCertList() {
        try {
            return this.f288c.getTBSCertList().getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            throw new CRLException(e.toString());
        }
    }

    public Date getThisUpdate() {
        return this.f288c.getThisUpdate().getDate();
    }

    public int getVersion() {
        return this.f288c.getVersionNumber();
    }

    public boolean hasUnsupportedCriticalExtension() {
        Set criticalExtensionOIDs = getCriticalExtensionOIDs();
        if (criticalExtensionOIDs == null) {
            return false;
        }
        criticalExtensionOIDs.remove(Extension.issuingDistributionPoint.getId());
        criticalExtensionOIDs.remove(Extension.deltaCRLIndicator.getId());
        return !criticalExtensionOIDs.isEmpty();
    }

    public int hashCode() {
        if (!this.isHashCodeSet) {
            this.isHashCodeSet = true;
            this.hashCodeValue = super.hashCode();
        }
        return this.hashCodeValue;
    }

    public boolean isRevoked(Certificate certificate) {
        if (certificate.getType().equals("X.509")) {
            Enumeration revokedCertificateEnumeration = this.f288c.getRevokedCertificateEnumeration();
            X500Name issuer = this.f288c.getIssuer();
            if (revokedCertificateEnumeration.hasMoreElements()) {
                BigInteger serialNumber = ((X509Certificate) certificate).getSerialNumber();
                X500Name x500Name = issuer;
                while (revokedCertificateEnumeration.hasMoreElements()) {
                    CRLEntry instance = CRLEntry.getInstance(revokedCertificateEnumeration.nextElement());
                    if (this.isIndirect && instance.hasExtensions()) {
                        Extension extension = instance.getExtensions().getExtension(Extension.certificateIssuer);
                        if (extension != null) {
                            x500Name = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
                        }
                    }
                    if (instance.getUserCertificate().getValue().equals(serialNumber)) {
                        Object instance2;
                        if (certificate instanceof X509Certificate) {
                            instance2 = X500Name.getInstance(((X509Certificate) certificate).getIssuerX500Principal().getEncoded());
                        } else {
                            try {
                                instance2 = org.bouncycastle.asn1.x509.Certificate.getInstance(certificate.getEncoded()).getIssuer();
                            } catch (CertificateEncodingException e) {
                                throw new RuntimeException("Cannot process certificate");
                            }
                        }
                        return x500Name.equals(instance2);
                    }
                }
            }
            return false;
        }
        throw new RuntimeException("X.509 CRL used with non X.509 Cert");
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("line.separator");
        stringBuffer.append("              Version: ").append(getVersion()).append(property);
        stringBuffer.append("             IssuerDN: ").append(getIssuerDN()).append(property);
        stringBuffer.append("          This update: ").append(getThisUpdate()).append(property);
        stringBuffer.append("          Next update: ").append(getNextUpdate()).append(property);
        stringBuffer.append("  Signature Algorithm: ").append(getSigAlgName()).append(property);
        byte[] signature = getSignature();
        stringBuffer.append("            Signature: ").append(new String(Hex.encode(signature, 0, 20))).append(property);
        for (int i = 20; i < signature.length; i += 20) {
            if (i < signature.length - 20) {
                stringBuffer.append("                       ").append(new String(Hex.encode(signature, i, 20))).append(property);
            } else {
                stringBuffer.append("                       ").append(new String(Hex.encode(signature, i, signature.length - i))).append(property);
            }
        }
        Extensions extensions = this.f288c.getTBSCertList().getExtensions();
        if (extensions != null) {
            Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                stringBuffer.append("           Extensions: ").append(property);
            }
            while (oids.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                Extension extension = extensions.getExtension(aSN1ObjectIdentifier);
                if (extension.getExtnValue() != null) {
                    ASN1InputStream aSN1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                    stringBuffer.append("                       critical(").append(extension.isCritical()).append(") ");
                    try {
                        if (aSN1ObjectIdentifier.equals(Extension.cRLNumber)) {
                            stringBuffer.append(new CRLNumber(ASN1Integer.getInstance(aSN1InputStream.readObject()).getPositiveValue())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(Extension.deltaCRLIndicator)) {
                            stringBuffer.append("Base CRL: " + new CRLNumber(ASN1Integer.getInstance(aSN1InputStream.readObject()).getPositiveValue())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(Extension.issuingDistributionPoint)) {
                            stringBuffer.append(IssuingDistributionPoint.getInstance(aSN1InputStream.readObject())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(Extension.cRLDistributionPoints)) {
                            stringBuffer.append(CRLDistPoint.getInstance(aSN1InputStream.readObject())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(Extension.freshestCRL)) {
                            stringBuffer.append(CRLDistPoint.getInstance(aSN1InputStream.readObject())).append(property);
                        } else {
                            stringBuffer.append(aSN1ObjectIdentifier.getId());
                            stringBuffer.append(" value = ").append(ASN1Dump.dumpAsString(aSN1InputStream.readObject())).append(property);
                        }
                    } catch (Exception e) {
                        stringBuffer.append(aSN1ObjectIdentifier.getId());
                        stringBuffer.append(" value = ").append("*****").append(property);
                    }
                } else {
                    stringBuffer.append(property);
                }
            }
        }
        Set<Object> revokedCertificates = getRevokedCertificates();
        if (revokedCertificates != null) {
            for (Object append : revokedCertificates) {
                stringBuffer.append(append);
                stringBuffer.append(property);
            }
        }
        return stringBuffer.toString();
    }

    public void verify(PublicKey publicKey) {
        verify(publicKey, BouncyCastleProvider.PROVIDER_NAME);
    }

    public void verify(PublicKey publicKey, String str) {
        if (this.f288c.getSignatureAlgorithm().equals(this.f288c.getTBSCertList().getSignature())) {
            Signature instance = str != null ? Signature.getInstance(getSigAlgName(), str) : Signature.getInstance(getSigAlgName());
            instance.initVerify(publicKey);
            instance.update(getTBSCertList());
            if (!instance.verify(getSignature())) {
                throw new SignatureException("CRL does not verify with supplied public key.");
            }
            return;
        }
        throw new CRLException("Signature algorithm on CertificateList does not match TBSCertList.");
    }
}
