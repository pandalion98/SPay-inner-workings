/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.cert.CRLException
 *  java.security.cert.X509CRLEntry
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashSet
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CRLException;
import java.security.cert.X509CRLEntry;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.asn1.x509.Time;

class X509CRLEntryObject
extends X509CRLEntry {
    private TBSCertList.CRLEntry c;
    private X500Name certificateIssuer;
    private int hashValue;
    private boolean isHashValueSet;

    protected X509CRLEntryObject(TBSCertList.CRLEntry cRLEntry) {
        this.c = cRLEntry;
        this.certificateIssuer = null;
    }

    protected X509CRLEntryObject(TBSCertList.CRLEntry cRLEntry, boolean bl, X500Name x500Name) {
        this.c = cRLEntry;
        this.certificateIssuer = this.loadCertificateIssuer(bl, x500Name);
    }

    private Extension getExtension(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Extensions extensions = this.c.getExtensions();
        if (extensions != null) {
            return extensions.getExtension(aSN1ObjectIdentifier);
        }
        return null;
    }

    private Set getExtensionOIDs(boolean bl) {
        Extensions extensions = this.c.getExtensions();
        if (extensions != null) {
            HashSet hashSet = new HashSet();
            Enumeration enumeration = extensions.oids();
            while (enumeration.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                if (bl != extensions.getExtension(aSN1ObjectIdentifier).isCritical()) continue;
                hashSet.add((Object)aSN1ObjectIdentifier.getId());
            }
            return hashSet;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private X500Name loadCertificateIssuer(boolean bl, X500Name x500Name) {
        GeneralName[] arrgeneralName;
        int n;
        if (!bl) {
            return null;
        }
        Extension extension = this.getExtension(Extension.certificateIssuer);
        if (extension == null) return x500Name;
        try {
            arrgeneralName = GeneralNames.getInstance(extension.getParsedValue()).getNames();
            n = 0;
        }
        catch (Exception exception) {
            return null;
        }
        while (n < arrgeneralName.length) {
            if (arrgeneralName[n].getTagNo() == 4) {
                return X500Name.getInstance(arrgeneralName[n].getName());
            }
            ++n;
        }
        return null;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof X509CRLEntryObject) {
            X509CRLEntryObject x509CRLEntryObject = (X509CRLEntryObject)((Object)object);
            return this.c.equals(x509CRLEntryObject.c);
        }
        return super.equals((Object)this);
    }

    public X500Principal getCertificateIssuer() {
        if (this.certificateIssuer == null) {
            return null;
        }
        try {
            X500Principal x500Principal = new X500Principal(this.certificateIssuer.getEncoded());
            return x500Principal;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }

    public byte[] getEncoded() {
        try {
            byte[] arrby = this.c.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new CRLException(iOException.toString());
        }
    }

    public byte[] getExtensionValue(String string) {
        Extension extension = this.getExtension(new ASN1ObjectIdentifier(string));
        if (extension != null) {
            try {
                byte[] arrby = extension.getExtnValue().getEncoded();
                return arrby;
            }
            catch (Exception exception) {
                throw new RuntimeException("error encoding " + exception.toString());
            }
        }
        return null;
    }

    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }

    public Date getRevocationDate() {
        return this.c.getRevocationDate().getDate();
    }

    public BigInteger getSerialNumber() {
        return this.c.getUserCertificate().getValue();
    }

    public boolean hasExtensions() {
        return this.c.getExtensions() != null;
    }

    public boolean hasUnsupportedCriticalExtension() {
        Set set = this.getCriticalExtensionOIDs();
        return set != null && !set.isEmpty();
    }

    public int hashCode() {
        if (!this.isHashValueSet) {
            this.hashValue = super.hashCode();
            this.isHashValueSet = true;
        }
        return this.hashValue;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String toString() {
        Enumeration enumeration;
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("      userCertificate: ").append((Object)this.getSerialNumber()).append(string);
        stringBuffer.append("       revocationDate: ").append((Object)this.getRevocationDate()).append(string);
        stringBuffer.append("       certificateIssuer: ").append((Object)this.getCertificateIssuer()).append(string);
        Extensions extensions = this.c.getExtensions();
        if (extensions != null && (enumeration = extensions.oids()).hasMoreElements()) {
            stringBuffer.append("   crlEntryExtensions:").append(string);
            while (enumeration.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                Extension extension = extensions.getExtension(aSN1ObjectIdentifier);
                if (extension.getExtnValue() != null) {
                    ASN1InputStream aSN1InputStream;
                    block8 : {
                        aSN1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                        stringBuffer.append("                       critical(").append(extension.isCritical()).append(") ");
                        try {
                            if (aSN1ObjectIdentifier.equals(Extension.reasonCode)) {
                                stringBuffer.append((Object)CRLReason.getInstance(ASN1Enumerated.getInstance(aSN1InputStream.readObject()))).append(string);
                            }
                            if (aSN1ObjectIdentifier.equals(Extension.certificateIssuer)) {
                                stringBuffer.append("Certificate issuer: ").append((Object)GeneralNames.getInstance(aSN1InputStream.readObject())).append(string);
                                continue;
                            }
                            break block8;
                        }
                        catch (Exception exception) {
                            stringBuffer.append(aSN1ObjectIdentifier.getId());
                            stringBuffer.append(" value = ").append("*****").append(string);
                        }
                        continue;
                    }
                    stringBuffer.append(aSN1ObjectIdentifier.getId());
                    stringBuffer.append(" value = ").append(ASN1Dump.dumpAsString(aSN1InputStream.readObject())).append(string);
                    continue;
                }
                stringBuffer.append(string);
            }
        }
        return stringBuffer.toString();
    }
}

