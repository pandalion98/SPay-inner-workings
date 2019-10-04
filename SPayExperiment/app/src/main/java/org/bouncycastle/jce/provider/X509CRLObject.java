/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.Principal
 *  java.security.PublicKey
 *  java.security.Signature
 *  java.security.SignatureException
 *  java.security.cert.CRLException
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.X509CRL
 *  java.security.cert.X509CRLEntry
 *  java.security.cert.X509Certificate
 *  java.util.Collections
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.ExtCRLException;
import org.bouncycastle.jce.provider.RFC3280CertPathUtilities;
import org.bouncycastle.jce.provider.X509CRLEntryObject;
import org.bouncycastle.jce.provider.X509SignatureUtil;
import org.bouncycastle.util.encoders.Hex;

public class X509CRLObject
extends X509CRL {
    private CertificateList c;
    private int hashCodeValue;
    private boolean isHashCodeSet = false;
    private boolean isIndirect;
    private String sigAlgName;
    private byte[] sigAlgParams;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public X509CRLObject(CertificateList certificateList) {
        this.c = certificateList;
        try {
            this.sigAlgName = X509SignatureUtil.getSignatureName(certificateList.getSignatureAlgorithm());
            this.sigAlgParams = certificateList.getSignatureAlgorithm().getParameters() != null ? certificateList.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded("DER") : null;
            this.isIndirect = X509CRLObject.isIndirectCRL(this);
            return;
        }
        catch (Exception exception) {
            throw new CRLException("CRL contents invalid: " + (Object)((Object)exception));
        }
    }

    private Set getExtensionOIDs(boolean bl) {
        Extensions extensions;
        if (this.getVersion() == 2 && (extensions = this.c.getTBSCertList().getExtensions()) != null) {
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

    public static boolean isIndirectCRL(X509CRL x509CRL) {
        block3 : {
            byte[] arrby;
            try {
                arrby = x509CRL.getExtensionValue(Extension.issuingDistributionPoint.getId());
                if (arrby == null) break block3;
            }
            catch (Exception exception) {
                throw new ExtCRLException("Exception reading IssuingDistributionPoint", exception);
            }
            boolean bl = IssuingDistributionPoint.getInstance(ASN1OctetString.getInstance(arrby).getOctets()).isIndirectCRL();
            if (!bl) break block3;
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Set loadCRLEntries() {
        HashSet hashSet = new HashSet();
        Enumeration enumeration = this.c.getRevokedCertificateEnumeration();
        X500Name x500Name = null;
        while (enumeration.hasMoreElements()) {
            Extension extension;
            TBSCertList.CRLEntry cRLEntry = (TBSCertList.CRLEntry)enumeration.nextElement();
            hashSet.add((Object)new X509CRLEntryObject(cRLEntry, this.isIndirect, x500Name));
            X500Name x500Name2 = this.isIndirect && cRLEntry.hasExtensions() && (extension = cRLEntry.getExtensions().getExtension(Extension.certificateIssuer)) != null ? X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName()) : x500Name;
            x500Name = x500Name2;
        }
        return hashSet;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        boolean bl = object instanceof X509CRL;
        boolean bl2 = false;
        if (!bl) return bl2;
        if (!(object instanceof X509CRLObject)) return super.equals(object);
        X509CRLObject x509CRLObject = (X509CRLObject)((Object)object);
        if (!this.isHashCodeSet) return this.c.equals(x509CRLObject.c);
        if (!x509CRLObject.isHashCodeSet) return this.c.equals(x509CRLObject.c);
        int n = x509CRLObject.hashCodeValue;
        int n2 = this.hashCodeValue;
        bl2 = false;
        if (n != n2) return bl2;
        return this.c.equals(x509CRLObject.c);
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
        Extension extension;
        Extensions extensions = this.c.getTBSCertList().getExtensions();
        if (extensions != null && (extension = extensions.getExtension(new ASN1ObjectIdentifier(string))) != null) {
            try {
                byte[] arrby = extension.getExtnValue().getEncoded();
                return arrby;
            }
            catch (Exception exception) {
                throw new IllegalStateException("error parsing " + exception.toString());
            }
        }
        return null;
    }

    public Principal getIssuerDN() {
        return new X509Principal(X500Name.getInstance(this.c.getIssuer().toASN1Primitive()));
    }

    public X500Principal getIssuerX500Principal() {
        try {
            X500Principal x500Principal = new X500Principal(this.c.getIssuer().getEncoded());
            return x500Principal;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public Date getNextUpdate() {
        if (this.c.getNextUpdate() != null) {
            return this.c.getNextUpdate().getDate();
        }
        return null;
    }

    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public X509CRLEntry getRevokedCertificate(BigInteger bigInteger) {
        Enumeration enumeration = this.c.getRevokedCertificateEnumeration();
        X500Name x500Name = null;
        do {
            Extension extension;
            boolean bl = enumeration.hasMoreElements();
            X509CRLEntryObject x509CRLEntryObject = null;
            if (!bl) return x509CRLEntryObject;
            TBSCertList.CRLEntry cRLEntry = (TBSCertList.CRLEntry)enumeration.nextElement();
            if (bigInteger.equals((Object)cRLEntry.getUserCertificate().getValue())) {
                return new X509CRLEntryObject(cRLEntry, this.isIndirect, x500Name);
            }
            X500Name x500Name2 = this.isIndirect && cRLEntry.hasExtensions() && (extension = cRLEntry.getExtensions().getExtension(Extension.certificateIssuer)) != null ? X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName()) : x500Name;
            x500Name = x500Name2;
        } while (true);
    }

    public Set getRevokedCertificates() {
        Set set = this.loadCRLEntries();
        if (!set.isEmpty()) {
            return Collections.unmodifiableSet((Set)set);
        }
        return null;
    }

    public String getSigAlgName() {
        return this.sigAlgName;
    }

    public String getSigAlgOID() {
        return this.c.getSignatureAlgorithm().getAlgorithm().getId();
    }

    public byte[] getSigAlgParams() {
        if (this.sigAlgParams != null) {
            byte[] arrby = new byte[this.sigAlgParams.length];
            System.arraycopy((Object)this.sigAlgParams, (int)0, (Object)arrby, (int)0, (int)arrby.length);
            return arrby;
        }
        return null;
    }

    public byte[] getSignature() {
        return this.c.getSignature().getBytes();
    }

    public byte[] getTBSCertList() {
        try {
            byte[] arrby = this.c.getTBSCertList().getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new CRLException(iOException.toString());
        }
    }

    public Date getThisUpdate() {
        return this.c.getThisUpdate().getDate();
    }

    public int getVersion() {
        return this.c.getVersionNumber();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean hasUnsupportedCriticalExtension() {
        block3 : {
            block2 : {
                Set set = this.getCriticalExtensionOIDs();
                if (set == null) break block2;
                set.remove((Object)RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                set.remove((Object)RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                if (!set.isEmpty()) break block3;
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (!this.isHashCodeSet) {
            this.isHashCodeSet = true;
            this.hashCodeValue = super.hashCode();
        }
        return this.hashCodeValue;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean isRevoked(java.security.cert.Certificate certificate) {
        TBSCertList.CRLEntry cRLEntry;
        X500Name x500Name3;
        if (!certificate.getType().equals((Object)"X.509")) {
            throw new RuntimeException("X.509 CRL used with non X.509 Cert");
        }
        Enumeration enumeration = this.c.getRevokedCertificateEnumeration();
        X500Name x500Name = this.c.getIssuer();
        if (enumeration == null) return false;
        BigInteger bigInteger = ((X509Certificate)certificate).getSerialNumber();
        X500Name x500Name2 = x500Name;
        do {
            Extension extension;
            if (!enumeration.hasMoreElements()) return false;
            cRLEntry = TBSCertList.CRLEntry.getInstance(enumeration.nextElement());
            if (!this.isIndirect || !cRLEntry.hasExtensions() || (extension = cRLEntry.getExtensions().getExtension(Extension.certificateIssuer)) == null) continue;
            x500Name2 = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
        } while (!cRLEntry.getUserCertificate().getValue().equals((Object)bigInteger));
        if (certificate instanceof X509Certificate) {
            x500Name3 = X500Name.getInstance(((X509Certificate)certificate).getIssuerX500Principal().getEncoded());
        } else {
            X500Name x500Name4;
            x500Name3 = x500Name4 = Certificate.getInstance(certificate.getEncoded()).getIssuer();
        }
        if (x500Name2.equals(x500Name3)) return true;
        return false;
        catch (CertificateEncodingException certificateEncodingException) {
            throw new RuntimeException("Cannot process certificate");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String toString() {
        Set set;
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("              Version: ").append(this.getVersion()).append(string);
        stringBuffer.append("             IssuerDN: ").append((Object)this.getIssuerDN()).append(string);
        stringBuffer.append("          This update: ").append((Object)this.getThisUpdate()).append(string);
        stringBuffer.append("          Next update: ").append((Object)this.getNextUpdate()).append(string);
        stringBuffer.append("  Signature Algorithm: ").append(this.getSigAlgName()).append(string);
        byte[] arrby = this.getSignature();
        stringBuffer.append("            Signature: ").append(new String(Hex.encode(arrby, 0, 20))).append(string);
        for (int i = 20; i < arrby.length; i += 20) {
            if (i < -20 + arrby.length) {
                stringBuffer.append("                       ").append(new String(Hex.encode(arrby, i, 20))).append(string);
                continue;
            }
            stringBuffer.append("                       ").append(new String(Hex.encode(arrby, i, arrby.length - i))).append(string);
        }
        Extensions extensions = this.c.getTBSCertList().getExtensions();
        if (extensions != null) {
            Enumeration enumeration = extensions.oids();
            if (enumeration.hasMoreElements()) {
                stringBuffer.append("           Extensions: ").append(string);
            }
            while (enumeration.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                Extension extension = extensions.getExtension(aSN1ObjectIdentifier);
                if (extension.getExtnValue() != null) {
                    ASN1InputStream aSN1InputStream;
                    block16 : {
                        aSN1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                        stringBuffer.append("                       critical(").append(extension.isCritical()).append(") ");
                        try {
                            if (aSN1ObjectIdentifier.equals(Extension.cRLNumber)) {
                                stringBuffer.append((Object)new CRLNumber(ASN1Integer.getInstance(aSN1InputStream.readObject()).getPositiveValue())).append(string);
                            }
                            if (aSN1ObjectIdentifier.equals(Extension.deltaCRLIndicator)) {
                                stringBuffer.append("Base CRL: " + new CRLNumber(ASN1Integer.getInstance(aSN1InputStream.readObject()).getPositiveValue())).append(string);
                                continue;
                            }
                            break block16;
                        }
                        catch (Exception exception) {
                            stringBuffer.append(aSN1ObjectIdentifier.getId());
                            stringBuffer.append(" value = ").append("*****").append(string);
                        }
                        continue;
                    }
                    if (aSN1ObjectIdentifier.equals(Extension.issuingDistributionPoint)) {
                        stringBuffer.append((Object)IssuingDistributionPoint.getInstance(aSN1InputStream.readObject())).append(string);
                        continue;
                    }
                    if (aSN1ObjectIdentifier.equals(Extension.cRLDistributionPoints)) {
                        stringBuffer.append((Object)CRLDistPoint.getInstance(aSN1InputStream.readObject())).append(string);
                        continue;
                    }
                    if (aSN1ObjectIdentifier.equals(Extension.freshestCRL)) {
                        stringBuffer.append((Object)CRLDistPoint.getInstance(aSN1InputStream.readObject())).append(string);
                        continue;
                    }
                    stringBuffer.append(aSN1ObjectIdentifier.getId());
                    stringBuffer.append(" value = ").append(ASN1Dump.dumpAsString(aSN1InputStream.readObject())).append(string);
                    continue;
                }
                stringBuffer.append(string);
            }
        }
        if ((set = this.getRevokedCertificates()) != null) {
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                stringBuffer.append(iterator.next());
                stringBuffer.append(string);
            }
        }
        return stringBuffer.toString();
    }

    public void verify(PublicKey publicKey) {
        this.verify(publicKey, "BC");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void verify(PublicKey publicKey, String string) {
        if (!this.c.getSignatureAlgorithm().equals(this.c.getTBSCertList().getSignature())) {
            throw new CRLException("Signature algorithm on CertificateList does not match TBSCertList.");
        }
        Signature signature = string != null ? Signature.getInstance((String)this.getSigAlgName(), (String)string) : Signature.getInstance((String)this.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(this.getTBSCertList());
        if (!signature.verify(this.getSignature())) {
            throw new SignatureException("CRL does not verify with supplied public key.");
        }
    }
}

