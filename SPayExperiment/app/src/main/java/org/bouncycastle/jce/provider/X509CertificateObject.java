/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.net.InetAddress
 *  java.net.UnknownHostException
 *  java.security.Principal
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.security.Security
 *  java.security.Signature
 *  java.security.SignatureException
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.CertificateException
 *  java.security.cert.CertificateExpiredException
 *  java.security.cert.CertificateNotYetValidException
 *  java.security.cert.CertificateParsingException
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jce.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.misc.NetscapeRevocationURL;
import org.bouncycastle.asn1.misc.VerisignCzagExtension;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.RFC3280CertPathUtilities;
import org.bouncycastle.jce.provider.X509SignatureUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.encoders.Hex;

public class X509CertificateObject
extends X509Certificate
implements PKCS12BagAttributeCarrier {
    private PKCS12BagAttributeCarrier attrCarrier;
    private BasicConstraints basicConstraints;
    private Certificate c;
    private int hashValue;
    private boolean hashValueSet;
    private boolean[] keyUsage;

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public X509CertificateObject(Certificate certificate) {
        int n2;
        byte[] arrby;
        int i;
        int n;
        block6 : {
            n = 9;
            this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
            this.c = certificate;
            byte[] arrby2 = this.getExtensionBytes("2.5.29.19");
            if (arrby2 == null) break block6;
            this.basicConstraints = BasicConstraints.getInstance(ASN1Primitive.fromByteArray(arrby2));
            {
                catch (Exception exception) {
                    throw new CertificateParsingException("cannot construct BasicConstraints: " + (Object)((Object)exception));
                }
            }
        }
        try {
            block9 : {
                block7 : {
                    block8 : {
                        byte[] arrby2 = this.getExtensionBytes("2.5.29.15");
                        if (arrby2 == null) break block7;
                        DERBitString dERBitString = DERBitString.getInstance(ASN1Primitive.fromByteArray(arrby2));
                        arrby = dERBitString.getBytes();
                        n2 = 8 * arrby.length - dERBitString.getPadBits();
                        if (n2 >= n) break block8;
                        break block9;
                    }
                    n = n2;
                    break block9;
                }
                this.keyUsage = null;
                return;
            }
            this.keyUsage = new boolean[n];
            i = 0;
        }
        catch (Exception exception) {
            throw new CertificateParsingException("cannot construct KeyUsage: " + (Object)((Object)exception));
        }
        while (i != n2) {
            boolean[] arrbl = this.keyUsage;
            boolean bl = (arrby[i / 8] & 128 >>> i % 8) != 0;
            arrbl[i] = bl;
            ++i;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int calculateHashCode() {
        int n;
        byte[] arrby;
        int n2;
        try {
            arrby = this.getEncoded();
            n2 = 1;
            n = 0;
        }
        catch (CertificateEncodingException certificateEncodingException) {
            return 0;
        }
        while (n2 < arrby.length) {
            byte by = arrby[n2];
            int n3 = n + by * n2;
            ++n2;
            n = n3;
        }
        return n;
    }

    private void checkSignature(PublicKey publicKey, Signature signature) {
        if (!this.isAlgIdEqual(this.c.getSignatureAlgorithm(), this.c.getTBSCertificate().getSignature())) {
            throw new CertificateException("signature algorithm in TBS cert not same as outer cert");
        }
        X509SignatureUtil.setSignatureParameters(signature, this.c.getSignatureAlgorithm().getParameters());
        signature.initVerify(publicKey);
        signature.update(this.getTBSCertificate());
        if (!signature.verify(this.getSignature())) {
            throw new SignatureException("certificate does not verify with supplied key");
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Collection getAlternativeNames(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        try {
            ArrayList arrayList = new ArrayList();
            Enumeration enumeration = ASN1Sequence.getInstance(arrby).getObjects();
            do {
                if (enumeration.hasMoreElements()) {
                    GeneralName generalName = GeneralName.getInstance(enumeration.nextElement());
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add((Object)Integers.valueOf(generalName.getTagNo()));
                    switch (generalName.getTagNo()) {
                        default: {
                            throw new IOException("Bad tag number: " + generalName.getTagNo());
                        }
                        case 0: 
                        case 3: 
                        case 5: {
                            arrayList2.add((Object)generalName.getEncoded());
                            break;
                        }
                        case 4: {
                            arrayList2.add((Object)X500Name.getInstance(RFC4519Style.INSTANCE, generalName.getName()).toString());
                            break;
                        }
                        case 1: 
                        case 2: 
                        case 6: {
                            arrayList2.add((Object)((ASN1String)((Object)generalName.getName())).getString());
                            break;
                        }
                        case 8: {
                            arrayList2.add((Object)ASN1ObjectIdentifier.getInstance(generalName.getName()).getId());
                            break;
                        }
                        case 7: {
                            byte[] arrby2 = DEROctetString.getInstance(generalName.getName()).getOctets();
                            String string = InetAddress.getByAddress((byte[])arrby2).getHostAddress();
                            arrayList2.add((Object)string);
                        }
                    }
                    arrayList.add((Object)Collections.unmodifiableList((List)arrayList2));
                    continue;
                }
                if (arrayList.size() != 0) return Collections.unmodifiableCollection((Collection)arrayList);
                return null;
                catch (UnknownHostException unknownHostException) {
                    continue;
                }
                break;
            } while (true);
        }
        catch (Exception exception) {
            throw new CertificateParsingException(exception.getMessage());
        }
    }

    private byte[] getExtensionBytes(String string) {
        Extension extension;
        Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null && (extension = extensions.getExtension(new ASN1ObjectIdentifier(string))) != null) {
            return extension.getExtnValue().getOctets();
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isAlgIdEqual(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2) {
        if (!algorithmIdentifier.getAlgorithm().equals(algorithmIdentifier2.getAlgorithm())) return false;
        if (algorithmIdentifier.getParameters() == null) {
            if (algorithmIdentifier2.getParameters() != null && !algorithmIdentifier2.getParameters().equals((Object)DERNull.INSTANCE)) return false;
            return true;
        }
        if (algorithmIdentifier2.getParameters() != null) {
            return algorithmIdentifier.getParameters().equals((Object)algorithmIdentifier2.getParameters());
        }
        if (algorithmIdentifier.getParameters() == null || algorithmIdentifier.getParameters().equals((Object)DERNull.INSTANCE)) return true;
        return false;
    }

    public void checkValidity() {
        this.checkValidity(new Date());
    }

    public void checkValidity(Date date) {
        if (date.getTime() > this.getNotAfter().getTime()) {
            throw new CertificateExpiredException("certificate expired on " + this.c.getEndDate().getTime());
        }
        if (date.getTime() < this.getNotBefore().getTime()) {
            throw new CertificateNotYetValidException("certificate not valid till " + this.c.getStartDate().getTime());
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
        boolean bl = object instanceof java.security.cert.Certificate;
        boolean bl2 = false;
        if (!bl) return bl2;
        java.security.cert.Certificate certificate = (java.security.cert.Certificate)object;
        try {
            return Arrays.areEqual(this.getEncoded(), certificate.getEncoded());
        }
        catch (CertificateEncodingException certificateEncodingException) {
            return false;
        }
    }

    @Override
    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(aSN1ObjectIdentifier);
    }

    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }

    public int getBasicConstraints() {
        block3 : {
            int n;
            block2 : {
                n = -1;
                if (this.basicConstraints == null || !this.basicConstraints.isCA()) break block2;
                if (this.basicConstraints.getPathLenConstraint() != null) break block3;
                n = Integer.MAX_VALUE;
            }
            return n;
        }
        return this.basicConstraints.getPathLenConstraint().intValue();
    }

    public Set getCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            HashSet hashSet = new HashSet();
            Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                Enumeration enumeration = extensions.oids();
                while (enumeration.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                    if (!extensions.getExtension(aSN1ObjectIdentifier).isCritical()) continue;
                    hashSet.add((Object)aSN1ObjectIdentifier.getId());
                }
                return hashSet;
            }
        }
        return null;
    }

    public byte[] getEncoded() {
        try {
            byte[] arrby = this.c.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new CertificateEncodingException(iOException.toString());
        }
    }

    public List getExtendedKeyUsage() {
        byte[] arrby = this.getExtensionBytes("2.5.29.37");
        if (arrby != null) {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)new ASN1InputStream(arrby).readObject();
            ArrayList arrayList = new ArrayList();
            int n = 0;
            do {
                if (n == aSN1Sequence.size()) break;
                arrayList.add((Object)((ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(n)).getId());
                ++n;
            } while (true);
            try {
                List list = Collections.unmodifiableList((List)arrayList);
                return list;
            }
            catch (Exception exception) {
                throw new CertificateParsingException("error processing extended key usage extension");
            }
        }
        return null;
    }

    public byte[] getExtensionValue(String string) {
        Extension extension;
        Extensions extensions = this.c.getTBSCertificate().getExtensions();
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

    public Collection getIssuerAlternativeNames() {
        return X509CertificateObject.getAlternativeNames(this.getExtensionBytes(Extension.issuerAlternativeName.getId()));
    }

    public Principal getIssuerDN() {
        try {
            X509Principal x509Principal = new X509Principal(X500Name.getInstance(this.c.getIssuer().getEncoded()));
            return x509Principal;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean[] getIssuerUniqueID() {
        DERBitString dERBitString = this.c.getTBSCertificate().getIssuerUniqueId();
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

    public X500Principal getIssuerX500Principal() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream((OutputStream)byteArrayOutputStream).writeObject(this.c.getIssuer());
            X500Principal x500Principal = new X500Principal(byteArrayOutputStream.toByteArray());
            return x500Principal;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public boolean[] getKeyUsage() {
        return this.keyUsage;
    }

    public Set getNonCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            HashSet hashSet = new HashSet();
            Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                Enumeration enumeration = extensions.oids();
                while (enumeration.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                    if (extensions.getExtension(aSN1ObjectIdentifier).isCritical()) continue;
                    hashSet.add((Object)aSN1ObjectIdentifier.getId());
                }
                return hashSet;
            }
        }
        return null;
    }

    public Date getNotAfter() {
        return this.c.getEndDate().getDate();
    }

    public Date getNotBefore() {
        return this.c.getStartDate().getDate();
    }

    public PublicKey getPublicKey() {
        try {
            PublicKey publicKey = BouncyCastleProvider.getPublicKey(this.c.getSubjectPublicKeyInfo());
            return publicKey;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public BigInteger getSerialNumber() {
        return this.c.getSerialNumber().getValue();
    }

    public String getSigAlgName() {
        String string;
        Provider provider = Security.getProvider((String)"BC");
        if (provider != null && (string = provider.getProperty("Alg.Alias.Signature." + this.getSigAlgOID())) != null) {
            return string;
        }
        Provider[] arrprovider = Security.getProviders();
        for (int i = 0; i != arrprovider.length; ++i) {
            String string2 = arrprovider[i].getProperty("Alg.Alias.Signature." + this.getSigAlgOID());
            if (string2 == null) continue;
            return string2;
        }
        return this.getSigAlgOID();
    }

    public String getSigAlgOID() {
        return this.c.getSignatureAlgorithm().getAlgorithm().getId();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] getSigAlgParams() {
        ASN1Encodable aSN1Encodable = this.c.getSignatureAlgorithm().getParameters();
        byte[] arrby = null;
        if (aSN1Encodable == null) return arrby;
        try {
            byte[] arrby2 = this.c.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded("DER");
            return arrby2;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public byte[] getSignature() {
        return this.c.getSignature().getBytes();
    }

    public Collection getSubjectAlternativeNames() {
        return X509CertificateObject.getAlternativeNames(this.getExtensionBytes(Extension.subjectAlternativeName.getId()));
    }

    public Principal getSubjectDN() {
        return new X509Principal(X500Name.getInstance(this.c.getSubject().toASN1Primitive()));
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean[] getSubjectUniqueID() {
        DERBitString dERBitString = this.c.getTBSCertificate().getSubjectUniqueId();
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

    public X500Principal getSubjectX500Principal() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream((OutputStream)byteArrayOutputStream).writeObject(this.c.getSubject());
            X500Principal x500Principal = new X500Principal(byteArrayOutputStream.toByteArray());
            return x500Principal;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public byte[] getTBSCertificate() {
        try {
            byte[] arrby = this.c.getTBSCertificate().getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new CertificateEncodingException(iOException.toString());
        }
    }

    public int getVersion() {
        return this.c.getVersionNumber();
    }

    public boolean hasUnsupportedCriticalExtension() {
        Extensions extensions;
        if (this.getVersion() == 3 && (extensions = this.c.getTBSCertificate().getExtensions()) != null) {
            Enumeration enumeration = extensions.oids();
            while (enumeration.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                String string = aSN1ObjectIdentifier.getId();
                if (string.equals((Object)RFC3280CertPathUtilities.KEY_USAGE) || string.equals((Object)RFC3280CertPathUtilities.CERTIFICATE_POLICIES) || string.equals((Object)RFC3280CertPathUtilities.POLICY_MAPPINGS) || string.equals((Object)RFC3280CertPathUtilities.INHIBIT_ANY_POLICY) || string.equals((Object)RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS) || string.equals((Object)RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT) || string.equals((Object)RFC3280CertPathUtilities.DELTA_CRL_INDICATOR) || string.equals((Object)RFC3280CertPathUtilities.POLICY_CONSTRAINTS) || string.equals((Object)RFC3280CertPathUtilities.BASIC_CONSTRAINTS) || string.equals((Object)RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME) || string.equals((Object)RFC3280CertPathUtilities.NAME_CONSTRAINTS) || !extensions.getExtension(aSN1ObjectIdentifier).isCritical()) continue;
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        X509CertificateObject x509CertificateObject = this;
        synchronized (x509CertificateObject) {
            if (!this.hashValueSet) {
                this.hashValue = this.calculateHashCode();
                this.hashValueSet = true;
            }
            int n = this.hashValue;
            return n;
        }
    }

    @Override
    public void setBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.attrCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("  [0]         Version: ").append(this.getVersion()).append(string);
        stringBuffer.append("         SerialNumber: ").append((Object)this.getSerialNumber()).append(string);
        stringBuffer.append("             IssuerDN: ").append((Object)this.getIssuerDN()).append(string);
        stringBuffer.append("           Start Date: ").append((Object)this.getNotBefore()).append(string);
        stringBuffer.append("           Final Date: ").append((Object)this.getNotAfter()).append(string);
        stringBuffer.append("            SubjectDN: ").append((Object)this.getSubjectDN()).append(string);
        stringBuffer.append("           Public Key: ").append((Object)this.getPublicKey()).append(string);
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
        Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            Enumeration enumeration = extensions.oids();
            if (enumeration.hasMoreElements()) {
                stringBuffer.append("       Extensions: \n");
            }
            while (enumeration.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
                Extension extension = extensions.getExtension(aSN1ObjectIdentifier);
                if (extension.getExtnValue() != null) {
                    ASN1InputStream aSN1InputStream;
                    block14 : {
                        aSN1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                        stringBuffer.append("                       critical(").append(extension.isCritical()).append(") ");
                        try {
                            if (aSN1ObjectIdentifier.equals(Extension.basicConstraints)) {
                                stringBuffer.append((Object)BasicConstraints.getInstance(aSN1InputStream.readObject())).append(string);
                            }
                            if (aSN1ObjectIdentifier.equals(Extension.keyUsage)) {
                                stringBuffer.append((Object)KeyUsage.getInstance(aSN1InputStream.readObject())).append(string);
                                continue;
                            }
                            break block14;
                        }
                        catch (Exception exception) {
                            stringBuffer.append(aSN1ObjectIdentifier.getId());
                            stringBuffer.append(" value = ").append("*****").append(string);
                        }
                        continue;
                    }
                    if (aSN1ObjectIdentifier.equals(MiscObjectIdentifiers.netscapeCertType)) {
                        stringBuffer.append((Object)new NetscapeCertType((DERBitString)aSN1InputStream.readObject())).append(string);
                        continue;
                    }
                    if (aSN1ObjectIdentifier.equals(MiscObjectIdentifiers.netscapeRevocationURL)) {
                        stringBuffer.append((Object)new NetscapeRevocationURL((DERIA5String)aSN1InputStream.readObject())).append(string);
                        continue;
                    }
                    if (aSN1ObjectIdentifier.equals(MiscObjectIdentifiers.verisignCzagExtension)) {
                        stringBuffer.append((Object)new VerisignCzagExtension((DERIA5String)aSN1InputStream.readObject())).append(string);
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void verify(PublicKey publicKey) {
        Signature signature;
        String string = X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm());
        try {
            Signature signature2;
            signature = signature2 = Signature.getInstance((String)string, (String)"BC");
        }
        catch (Exception exception) {
            signature = Signature.getInstance((String)string);
        }
        this.checkSignature(publicKey, signature);
    }

    public final void verify(PublicKey publicKey, String string) {
        this.checkSignature(publicKey, Signature.getInstance((String)X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm()), (String)string));
    }
}

