package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CRLException;
import java.security.cert.X509CRLEntry;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.TBSCertList.CRLEntry;
import org.bouncycastle.asn1.x509.X509Extension;

public class X509CRLEntryObject extends X509CRLEntry {
    private CRLEntry f300c;
    private X500Name certificateIssuer;
    private int hashValue;
    private boolean isHashValueSet;

    public X509CRLEntryObject(CRLEntry cRLEntry) {
        this.f300c = cRLEntry;
        this.certificateIssuer = null;
    }

    public X509CRLEntryObject(CRLEntry cRLEntry, boolean z, X500Name x500Name) {
        this.f300c = cRLEntry;
        this.certificateIssuer = loadCertificateIssuer(z, x500Name);
    }

    private Extension getExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Extensions extensions = this.f300c.getExtensions();
        return extensions != null ? extensions.getExtension(aSN1ObjectIdentifier) : null;
    }

    private Set getExtensionOIDs(boolean z) {
        Extensions extensions = this.f300c.getExtensions();
        if (extensions == null) {
            return null;
        }
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

    private X500Name loadCertificateIssuer(boolean z, X500Name x500Name) {
        if (!z) {
            return null;
        }
        Extension extension = getExtension(Extension.certificateIssuer);
        if (extension == null) {
            return x500Name;
        }
        try {
            GeneralName[] names = GeneralNames.getInstance(extension.getParsedValue()).getNames();
            for (int i = 0; i < names.length; i++) {
                if (names[i].getTagNo() == 4) {
                    return X500Name.getInstance(names[i].getName());
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof X509CRLEntryObject)) {
            return super.equals(this);
        }
        return this.f300c.equals(((X509CRLEntryObject) obj).f300c);
    }

    public X500Principal getCertificateIssuer() {
        if (this.certificateIssuer == null) {
            return null;
        }
        try {
            return new X500Principal(this.certificateIssuer.getEncoded());
        } catch (IOException e) {
            return null;
        }
    }

    public Set getCriticalExtensionOIDs() {
        return getExtensionOIDs(true);
    }

    public byte[] getEncoded() {
        try {
            return this.f300c.getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            throw new CRLException(e.toString());
        }
    }

    public byte[] getExtensionValue(String str) {
        Extension extension = getExtension(new ASN1ObjectIdentifier(str));
        if (extension == null) {
            return null;
        }
        try {
            return extension.getExtnValue().getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("error encoding " + e.toString());
        }
    }

    public Set getNonCriticalExtensionOIDs() {
        return getExtensionOIDs(false);
    }

    public Date getRevocationDate() {
        return this.f300c.getRevocationDate().getDate();
    }

    public BigInteger getSerialNumber() {
        return this.f300c.getUserCertificate().getValue();
    }

    public boolean hasExtensions() {
        return this.f300c.getExtensions() != null;
    }

    public boolean hasUnsupportedCriticalExtension() {
        Set criticalExtensionOIDs = getCriticalExtensionOIDs();
        return (criticalExtensionOIDs == null || criticalExtensionOIDs.isEmpty()) ? false : true;
    }

    public int hashCode() {
        if (!this.isHashValueSet) {
            this.hashValue = super.hashCode();
            this.isHashValueSet = true;
        }
        return this.hashValue;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("line.separator");
        stringBuffer.append("      userCertificate: ").append(getSerialNumber()).append(property);
        stringBuffer.append("       revocationDate: ").append(getRevocationDate()).append(property);
        stringBuffer.append("       certificateIssuer: ").append(getCertificateIssuer()).append(property);
        Extensions extensions = this.f300c.getExtensions();
        if (extensions != null) {
            Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                stringBuffer.append("   crlEntryExtensions:").append(property);
                while (oids.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                    Extension extension = extensions.getExtension(aSN1ObjectIdentifier);
                    if (extension.getExtnValue() != null) {
                        ASN1InputStream aSN1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                        stringBuffer.append("                       critical(").append(extension.isCritical()).append(") ");
                        try {
                            if (aSN1ObjectIdentifier.equals(X509Extension.reasonCode)) {
                                stringBuffer.append(CRLReason.getInstance(ASN1Enumerated.getInstance(aSN1InputStream.readObject()))).append(property);
                            } else if (aSN1ObjectIdentifier.equals(X509Extension.certificateIssuer)) {
                                stringBuffer.append("Certificate issuer: ").append(GeneralNames.getInstance(aSN1InputStream.readObject())).append(property);
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
        }
        return stringBuffer.toString();
    }
}
