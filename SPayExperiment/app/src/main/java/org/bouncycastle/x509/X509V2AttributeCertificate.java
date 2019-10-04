/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PublicKey
 *  java.security.Signature
 *  java.security.SignatureException
 *  java.security.cert.CertificateException
 *  java.security.cert.CertificateExpiredException
 *  java.security.cert.CertificateNotYetValidException
 *  java.text.ParseException
 *  java.util.ArrayList
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashSet
 *  java.util.Set
 */
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
import java.util.Set;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AttCertIssuer;
import org.bouncycastle.asn1.x509.AttCertValidityPeriod;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.AttributeCertificateInfo;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.Holder;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.x509.AttributeCertificateHolder;
import org.bouncycastle.x509.AttributeCertificateIssuer;
import org.bouncycastle.x509.X509Attribute;
import org.bouncycastle.x509.X509AttributeCertificate;

public class X509V2AttributeCertificate
implements X509AttributeCertificate {
    private AttributeCertificate cert;
    private Date notAfter;
    private Date notBefore;

    public X509V2AttributeCertificate(InputStream inputStream) {
        this(X509V2AttributeCertificate.getObject(inputStream));
    }

    X509V2AttributeCertificate(AttributeCertificate attributeCertificate) {
        this.cert = attributeCertificate;
        try {
            this.notAfter = attributeCertificate.getAcinfo().getAttrCertValidityPeriod().getNotAfterTime().getDate();
            this.notBefore = attributeCertificate.getAcinfo().getAttrCertValidityPeriod().getNotBeforeTime().getDate();
            return;
        }
        catch (ParseException parseException) {
            throw new IOException("invalid data structure in certificate!");
        }
    }

    public X509V2AttributeCertificate(byte[] arrby) {
        this((InputStream)new ByteArrayInputStream(arrby));
    }

    private Set getExtensionOIDs(boolean bl) {
        Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null) {
            HashSet hashSet = new HashSet();
            Enumeration enumeration = extensions.oids();
            while (enumeration.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                if (extensions.getExtension(aSN1ObjectIdentifier).isCritical() != bl) continue;
                hashSet.add((Object)aSN1ObjectIdentifier.getId());
            }
            return hashSet;
        }
        return null;
    }

    private static AttributeCertificate getObject(InputStream inputStream) {
        try {
            AttributeCertificate attributeCertificate = AttributeCertificate.getInstance(new ASN1InputStream(inputStream).readObject());
            return attributeCertificate;
        }
        catch (IOException iOException) {
            throw iOException;
        }
        catch (Exception exception) {
            throw new IOException("exception decoding certificate structure: " + exception.toString());
        }
    }

    @Override
    public void checkValidity() {
        this.checkValidity(new Date());
    }

    @Override
    public void checkValidity(Date date) {
        if (date.after(this.getNotAfter())) {
            throw new CertificateExpiredException("certificate expired on " + (Object)this.getNotAfter());
        }
        if (date.before(this.getNotBefore())) {
            throw new CertificateNotYetValidException("certificate not valid till " + (Object)this.getNotBefore());
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        boolean bl = object instanceof X509AttributeCertificate;
        boolean bl2 = false;
        if (!bl) return bl2;
        X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)object;
        try {
            return Arrays.areEqual(this.getEncoded(), x509AttributeCertificate.getEncoded());
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public X509Attribute[] getAttributes() {
        ASN1Sequence aSN1Sequence = this.cert.getAcinfo().getAttributes();
        X509Attribute[] arrx509Attribute = new X509Attribute[aSN1Sequence.size()];
        for (int i = 0; i != aSN1Sequence.size(); ++i) {
            arrx509Attribute[i] = new X509Attribute(aSN1Sequence.getObjectAt(i));
        }
        return arrx509Attribute;
    }

    @Override
    public X509Attribute[] getAttributes(String string) {
        ASN1Sequence aSN1Sequence = this.cert.getAcinfo().getAttributes();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i != aSN1Sequence.size(); ++i) {
            X509Attribute x509Attribute = new X509Attribute(aSN1Sequence.getObjectAt(i));
            if (!x509Attribute.getOID().equals((Object)string)) continue;
            arrayList.add((Object)x509Attribute);
        }
        if (arrayList.size() == 0) {
            return null;
        }
        return (X509Attribute[])arrayList.toArray((Object[])new X509Attribute[arrayList.size()]);
    }

    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }

    @Override
    public byte[] getEncoded() {
        return this.cert.getEncoded();
    }

    public byte[] getExtensionValue(String string) {
        Extension extension;
        Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null && (extension = extensions.getExtension(new ASN1ObjectIdentifier(string))) != null) {
            try {
                byte[] arrby = extension.getExtnValue().getEncoded("DER");
                return arrby;
            }
            catch (Exception exception) {
                throw new RuntimeException("error encoding " + exception.toString());
            }
        }
        return null;
    }

    @Override
    public AttributeCertificateHolder getHolder() {
        return new AttributeCertificateHolder((ASN1Sequence)this.cert.getAcinfo().getHolder().toASN1Object());
    }

    @Override
    public AttributeCertificateIssuer getIssuer() {
        return new AttributeCertificateIssuer(this.cert.getAcinfo().getIssuer());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean[] getIssuerUniqueID() {
        DERBitString dERBitString = this.cert.getAcinfo().getIssuerUniqueID();
        if (dERBitString == null) {
            return null;
        }
        byte[] arrby = dERBitString.getBytes();
        boolean[] arrbl = new boolean[8 * arrby.length - dERBitString.getPadBits()];
        int n = 0;
        while (n != arrbl.length) {
            boolean bl = (arrby[n / 8] & 128 >>> n % 8) != 0;
            arrbl[n] = bl;
            ++n;
        }
        return arrbl;
    }

    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }

    @Override
    public Date getNotAfter() {
        return this.notAfter;
    }

    @Override
    public Date getNotBefore() {
        return this.notBefore;
    }

    @Override
    public BigInteger getSerialNumber() {
        return this.cert.getAcinfo().getSerialNumber().getValue();
    }

    @Override
    public byte[] getSignature() {
        return this.cert.getSignatureValue().getBytes();
    }

    @Override
    public int getVersion() {
        return 1 + this.cert.getAcinfo().getVersion().getValue().intValue();
    }

    public boolean hasUnsupportedCriticalExtension() {
        Set set = this.getCriticalExtensionOIDs();
        return set != null && !set.isEmpty();
    }

    public int hashCode() {
        try {
            int n = Arrays.hashCode(this.getEncoded());
            return n;
        }
        catch (IOException iOException) {
            return 0;
        }
    }

    @Override
    public final void verify(PublicKey publicKey, String string) {
        if (!this.cert.getSignatureAlgorithm().equals(this.cert.getAcinfo().getSignature())) {
            throw new CertificateException("Signature algorithm in certificate info not same as outer certificate");
        }
        Signature signature = Signature.getInstance((String)this.cert.getSignatureAlgorithm().getObjectId().getId(), (String)string);
        signature.initVerify(publicKey);
        try {
            signature.update(this.cert.getAcinfo().getEncoded());
        }
        catch (IOException iOException) {
            throw new SignatureException("Exception encoding certificate info object");
        }
        if (!signature.verify(this.getSignature())) {
            throw new InvalidKeyException("Public key presented not for certificate signature");
        }
    }
}

