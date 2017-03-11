package org.bouncycastle.jce.provider;

import com.samsung.android.spayfw.cncc.CNCCCommands;
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
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.misc.NetscapeRevocationURL;
import org.bouncycastle.asn1.misc.VerisignCzagExtension;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class X509CertificateObject extends X509Certificate implements PKCS12BagAttributeCarrier {
    private PKCS12BagAttributeCarrier attrCarrier;
    private BasicConstraints basicConstraints;
    private Certificate f302c;
    private int hashValue;
    private boolean hashValueSet;
    private boolean[] keyUsage;

    public X509CertificateObject(Certificate certificate) {
        int i = 9;
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.f302c = certificate;
        try {
            byte[] extensionBytes = getExtensionBytes("2.5.29.19");
            if (extensionBytes != null) {
                this.basicConstraints = BasicConstraints.getInstance(ASN1Primitive.fromByteArray(extensionBytes));
            }
            try {
                extensionBytes = getExtensionBytes("2.5.29.15");
                if (extensionBytes != null) {
                    DERBitString instance = DERBitString.getInstance(ASN1Primitive.fromByteArray(extensionBytes));
                    byte[] bytes = instance.getBytes();
                    int length = (bytes.length * 8) - instance.getPadBits();
                    if (length >= 9) {
                        i = length;
                    }
                    this.keyUsage = new boolean[i];
                    for (int i2 = 0; i2 != length; i2++) {
                        this.keyUsage[i2] = (bytes[i2 / 8] & (X509KeyUsage.digitalSignature >>> (i2 % 8))) != 0;
                    }
                    return;
                }
                this.keyUsage = null;
            } catch (Exception e) {
                throw new CertificateParsingException("cannot construct KeyUsage: " + e);
            }
        } catch (Exception e2) {
            throw new CertificateParsingException("cannot construct BasicConstraints: " + e2);
        }
    }

    private int calculateHashCode() {
        try {
            byte[] encoded = getEncoded();
            int i = 1;
            int i2 = 0;
            while (i < encoded.length) {
                int i3 = (encoded[i] * i) + i2;
                i++;
                i2 = i3;
            }
            return i2;
        } catch (CertificateEncodingException e) {
            return 0;
        }
    }

    private void checkSignature(PublicKey publicKey, Signature signature) {
        if (isAlgIdEqual(this.f302c.getSignatureAlgorithm(), this.f302c.getTBSCertificate().getSignature())) {
            X509SignatureUtil.setSignatureParameters(signature, this.f302c.getSignatureAlgorithm().getParameters());
            signature.initVerify(publicKey);
            signature.update(getTBSCertificate());
            if (!signature.verify(getSignature())) {
                throw new SignatureException("certificate does not verify with supplied key");
            }
            return;
        }
        throw new CertificateException("signature algorithm in TBS cert not same as outer cert");
    }

    private static Collection getAlternativeNames(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Collection arrayList = new ArrayList();
        Enumeration objects = ASN1Sequence.getInstance(bArr).getObjects();
        while (objects.hasMoreElements()) {
            GeneralName instance = GeneralName.getInstance(objects.nextElement());
            List arrayList2 = new ArrayList();
            arrayList2.add(Integers.valueOf(instance.getTagNo()));
            switch (instance.getTagNo()) {
                case ECCurve.COORD_AFFINE /*0*/:
                case F2m.PPB /*3*/:
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    arrayList2.add(instance.getEncoded());
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    arrayList2.add(((ASN1String) instance.getName()).getString());
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    arrayList2.add(X500Name.getInstance(RFC4519Style.INSTANCE, instance.getName()).toString());
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    try {
                        try {
                            arrayList2.add(InetAddress.getByAddress(ASN1OctetString.getInstance(instance.getName()).getOctets()).getHostAddress());
                            break;
                        } catch (Exception e) {
                            throw new CertificateParsingException(e.getMessage());
                        }
                    } catch (UnknownHostException e2) {
                        break;
                    }
                case X509KeyUsage.keyAgreement /*8*/:
                    arrayList2.add(ASN1ObjectIdentifier.getInstance(instance.getName()).getId());
                    break;
                default:
                    throw new IOException("Bad tag number: " + instance.getTagNo());
            }
            arrayList.add(Collections.unmodifiableList(arrayList2));
        }
        return arrayList.size() == 0 ? null : Collections.unmodifiableCollection(arrayList);
    }

    private byte[] getExtensionBytes(String str) {
        Extensions extensions = this.f302c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            Extension extension = extensions.getExtension(new ASN1ObjectIdentifier(str));
            if (extension != null) {
                return extension.getExtnValue().getOctets();
            }
        }
        return null;
    }

    private boolean isAlgIdEqual(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2) {
        return !algorithmIdentifier.getAlgorithm().equals(algorithmIdentifier2.getAlgorithm()) ? false : algorithmIdentifier.getParameters() == null ? algorithmIdentifier2.getParameters() == null || algorithmIdentifier2.getParameters().equals(DERNull.INSTANCE) : algorithmIdentifier2.getParameters() == null ? algorithmIdentifier.getParameters() == null || algorithmIdentifier.getParameters().equals(DERNull.INSTANCE) : algorithmIdentifier.getParameters().equals(algorithmIdentifier2.getParameters());
    }

    public void checkValidity() {
        checkValidity(new Date());
    }

    public void checkValidity(Date date) {
        if (date.getTime() > getNotAfter().getTime()) {
            throw new CertificateExpiredException("certificate expired on " + this.f302c.getEndDate().getTime());
        } else if (date.getTime() < getNotBefore().getTime()) {
            throw new CertificateNotYetValidException("certificate not valid till " + this.f302c.getStartDate().getTime());
        }
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof java.security.cert.Certificate)) {
            return z;
        }
        try {
            return Arrays.areEqual(getEncoded(), ((java.security.cert.Certificate) obj).getEncoded());
        } catch (CertificateEncodingException e) {
            return z;
        }
    }

    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(aSN1ObjectIdentifier);
    }

    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }

    public int getBasicConstraints() {
        return (this.basicConstraints == null || !this.basicConstraints.isCA()) ? -1 : this.basicConstraints.getPathLenConstraint() == null ? CNCCCommands.CMD_CNCC_CMD_UNKNOWN : this.basicConstraints.getPathLenConstraint().intValue();
    }

    public Set getCriticalExtensionOIDs() {
        if (getVersion() == 3) {
            Set hashSet = new HashSet();
            Extensions extensions = this.f302c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                    if (extensions.getExtension(aSN1ObjectIdentifier).isCritical()) {
                        hashSet.add(aSN1ObjectIdentifier.getId());
                    }
                }
                return hashSet;
            }
        }
        return null;
    }

    public byte[] getEncoded() {
        try {
            return this.f302c.getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        }
    }

    public List getExtendedKeyUsage() {
        byte[] extensionBytes = getExtensionBytes("2.5.29.37");
        if (extensionBytes == null) {
            return null;
        }
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence) new ASN1InputStream(extensionBytes).readObject();
            List arrayList = new ArrayList();
            for (int i = 0; i != aSN1Sequence.size(); i++) {
                arrayList.add(((ASN1ObjectIdentifier) aSN1Sequence.getObjectAt(i)).getId());
            }
            return Collections.unmodifiableList(arrayList);
        } catch (Exception e) {
            throw new CertificateParsingException("error processing extended key usage extension");
        }
    }

    public byte[] getExtensionValue(String str) {
        Extensions extensions = this.f302c.getTBSCertificate().getExtensions();
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

    public Collection getIssuerAlternativeNames() {
        return getAlternativeNames(getExtensionBytes(Extension.issuerAlternativeName.getId()));
    }

    public Principal getIssuerDN() {
        try {
            return new X509Principal(X500Name.getInstance(this.f302c.getIssuer().getEncoded()));
        } catch (IOException e) {
            return null;
        }
    }

    public boolean[] getIssuerUniqueID() {
        DERBitString issuerUniqueId = this.f302c.getTBSCertificate().getIssuerUniqueId();
        if (issuerUniqueId == null) {
            return null;
        }
        byte[] bytes = issuerUniqueId.getBytes();
        boolean[] zArr = new boolean[((bytes.length * 8) - issuerUniqueId.getPadBits())];
        for (int i = 0; i != zArr.length; i++) {
            zArr[i] = (bytes[i / 8] & (X509KeyUsage.digitalSignature >>> (i % 8))) != 0;
        }
        return zArr;
    }

    public X500Principal getIssuerX500Principal() {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.f302c.getIssuer());
            return new X500Principal(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public boolean[] getKeyUsage() {
        return this.keyUsage;
    }

    public Set getNonCriticalExtensionOIDs() {
        if (getVersion() == 3) {
            Set hashSet = new HashSet();
            Extensions extensions = this.f302c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                    if (!extensions.getExtension(aSN1ObjectIdentifier).isCritical()) {
                        hashSet.add(aSN1ObjectIdentifier.getId());
                    }
                }
                return hashSet;
            }
        }
        return null;
    }

    public Date getNotAfter() {
        return this.f302c.getEndDate().getDate();
    }

    public Date getNotBefore() {
        return this.f302c.getStartDate().getDate();
    }

    public PublicKey getPublicKey() {
        try {
            return BouncyCastleProvider.getPublicKey(this.f302c.getSubjectPublicKeyInfo());
        } catch (IOException e) {
            return null;
        }
    }

    public BigInteger getSerialNumber() {
        return this.f302c.getSerialNumber().getValue();
    }

    public String getSigAlgName() {
        Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider != null) {
            String property = provider.getProperty("Alg.Alias.Signature." + getSigAlgOID());
            if (property != null) {
                return property;
            }
        }
        Provider[] providers = Security.getProviders();
        for (int i = 0; i != providers.length; i++) {
            String property2 = providers[i].getProperty("Alg.Alias.Signature." + getSigAlgOID());
            if (property2 != null) {
                return property2;
            }
        }
        return getSigAlgOID();
    }

    public String getSigAlgOID() {
        return this.f302c.getSignatureAlgorithm().getAlgorithm().getId();
    }

    public byte[] getSigAlgParams() {
        byte[] bArr = null;
        if (this.f302c.getSignatureAlgorithm().getParameters() != null) {
            try {
                bArr = this.f302c.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded(ASN1Encoding.DER);
            } catch (IOException e) {
            }
        }
        return bArr;
    }

    public byte[] getSignature() {
        return this.f302c.getSignature().getBytes();
    }

    public Collection getSubjectAlternativeNames() {
        return getAlternativeNames(getExtensionBytes(Extension.subjectAlternativeName.getId()));
    }

    public Principal getSubjectDN() {
        return new X509Principal(X500Name.getInstance(this.f302c.getSubject().toASN1Primitive()));
    }

    public boolean[] getSubjectUniqueID() {
        DERBitString subjectUniqueId = this.f302c.getTBSCertificate().getSubjectUniqueId();
        if (subjectUniqueId == null) {
            return null;
        }
        byte[] bytes = subjectUniqueId.getBytes();
        boolean[] zArr = new boolean[((bytes.length * 8) - subjectUniqueId.getPadBits())];
        for (int i = 0; i != zArr.length; i++) {
            zArr[i] = (bytes[i / 8] & (X509KeyUsage.digitalSignature >>> (i % 8))) != 0;
        }
        return zArr;
    }

    public X500Principal getSubjectX500Principal() {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.f302c.getSubject());
            return new X500Principal(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }

    public byte[] getTBSCertificate() {
        try {
            return this.f302c.getTBSCertificate().getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        }
    }

    public int getVersion() {
        return this.f302c.getVersionNumber();
    }

    public boolean hasUnsupportedCriticalExtension() {
        if (getVersion() == 3) {
            Extensions extensions = this.f302c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                    String id = aSN1ObjectIdentifier.getId();
                    if (!id.equals(RFC3280CertPathUtilities.KEY_USAGE) && !id.equals(RFC3280CertPathUtilities.CERTIFICATE_POLICIES) && !id.equals(RFC3280CertPathUtilities.POLICY_MAPPINGS) && !id.equals(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY) && !id.equals(RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS) && !id.equals(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT) && !id.equals(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR) && !id.equals(RFC3280CertPathUtilities.POLICY_CONSTRAINTS) && !id.equals(RFC3280CertPathUtilities.BASIC_CONSTRAINTS) && !id.equals(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME) && !id.equals(RFC3280CertPathUtilities.NAME_CONSTRAINTS) && extensions.getExtension(aSN1ObjectIdentifier).isCritical()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public synchronized int hashCode() {
        if (!this.hashValueSet) {
            this.hashValue = calculateHashCode();
            this.hashValueSet = true;
        }
        return this.hashValue;
    }

    public void setBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.attrCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("line.separator");
        stringBuffer.append("  [0]         Version: ").append(getVersion()).append(property);
        stringBuffer.append("         SerialNumber: ").append(getSerialNumber()).append(property);
        stringBuffer.append("             IssuerDN: ").append(getIssuerDN()).append(property);
        stringBuffer.append("           Start Date: ").append(getNotBefore()).append(property);
        stringBuffer.append("           Final Date: ").append(getNotAfter()).append(property);
        stringBuffer.append("            SubjectDN: ").append(getSubjectDN()).append(property);
        stringBuffer.append("           Public Key: ").append(getPublicKey()).append(property);
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
        Extensions extensions = this.f302c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                stringBuffer.append("       Extensions: \n");
            }
            while (oids.hasMoreElements()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) oids.nextElement();
                Extension extension = extensions.getExtension(aSN1ObjectIdentifier);
                if (extension.getExtnValue() != null) {
                    ASN1InputStream aSN1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                    stringBuffer.append("                       critical(").append(extension.isCritical()).append(") ");
                    try {
                        if (aSN1ObjectIdentifier.equals(Extension.basicConstraints)) {
                            stringBuffer.append(BasicConstraints.getInstance(aSN1InputStream.readObject())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(Extension.keyUsage)) {
                            stringBuffer.append(KeyUsage.getInstance(aSN1InputStream.readObject())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(MiscObjectIdentifiers.netscapeCertType)) {
                            stringBuffer.append(new NetscapeCertType((DERBitString) aSN1InputStream.readObject())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(MiscObjectIdentifiers.netscapeRevocationURL)) {
                            stringBuffer.append(new NetscapeRevocationURL((DERIA5String) aSN1InputStream.readObject())).append(property);
                        } else if (aSN1ObjectIdentifier.equals(MiscObjectIdentifiers.verisignCzagExtension)) {
                            stringBuffer.append(new VerisignCzagExtension((DERIA5String) aSN1InputStream.readObject())).append(property);
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
        return stringBuffer.toString();
    }

    public final void verify(PublicKey publicKey) {
        Signature instance;
        String signatureName = X509SignatureUtil.getSignatureName(this.f302c.getSignatureAlgorithm());
        try {
            instance = Signature.getInstance(signatureName, BouncyCastleProvider.PROVIDER_NAME);
        } catch (Exception e) {
            instance = Signature.getInstance(signatureName);
        }
        checkSignature(publicKey, instance);
    }

    public final void verify(PublicKey publicKey, String str) {
        checkSignature(publicKey, Signature.getInstance(X509SignatureUtil.getSignatureName(this.f302c.getSignatureAlgorithm()), str));
    }
}
