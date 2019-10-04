/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.ObjectInputStream
 *  java.io.ObjectOutputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.interfaces.ECPrivateKey
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 *  java.security.spec.ECPrivateKeySpec
 *  java.security.spec.EllipticCurve
 *  java.util.Enumeration
 *  org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util
 *  org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
 *  org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl
 *  org.bouncycastle.jcajce.provider.config.ProviderConfiguration
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.jce.spec.ECNamedCurveSpec
 *  org.bouncycastle.jce.spec.ECParameterSpec
 *  org.bouncycastle.jce.spec.ECPrivateKeySpec
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.jcajce.provider.asymmetric.ec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class BCECPrivateKey
implements java.security.interfaces.ECPrivateKey,
ECPointEncoder,
ECPrivateKey,
PKCS12BagAttributeCarrier {
    static final long serialVersionUID = 994553197664784084L;
    private String algorithm = "EC";
    private transient PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
    private transient ProviderConfiguration configuration;
    private transient BigInteger d;
    private transient ECParameterSpec ecSpec;
    private transient DERBitString publicKey;
    private boolean withCompression;

    protected BCECPrivateKey() {
    }

    public BCECPrivateKey(String string, ECPrivateKeySpec eCPrivateKeySpec, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.d = eCPrivateKeySpec.getS();
        this.ecSpec = eCPrivateKeySpec.getParams();
        this.configuration = providerConfiguration;
    }

    BCECPrivateKey(String string, PrivateKeyInfo privateKeyInfo, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.configuration = providerConfiguration;
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCECPrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, BCECPublicKey bCECPublicKey, ECParameterSpec eCParameterSpec, ProviderConfiguration providerConfiguration) {
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.configuration = providerConfiguration;
        this.ecSpec = eCParameterSpec == null ? new ECParameterSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue()) : eCParameterSpec;
        this.publicKey = this.getPublicKeyDetails(bCECPublicKey);
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCECPrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, BCECPublicKey bCECPublicKey, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec, ProviderConfiguration providerConfiguration) {
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.configuration = providerConfiguration;
        this.ecSpec = eCParameterSpec == null ? new ECParameterSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue()) : EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCParameterSpec.getCurve(), (byte[])eCParameterSpec.getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCParameterSpec);
        this.publicKey = this.getPublicKeyDetails(bCECPublicKey);
    }

    public BCECPrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = null;
        this.configuration = providerConfiguration;
    }

    public BCECPrivateKey(String string, BCECPrivateKey bCECPrivateKey) {
        this.algorithm = string;
        this.d = bCECPrivateKey.d;
        this.ecSpec = bCECPrivateKey.ecSpec;
        this.withCompression = bCECPrivateKey.withCompression;
        this.attrCarrier = bCECPrivateKey.attrCarrier;
        this.publicKey = bCECPrivateKey.publicKey;
        this.configuration = bCECPrivateKey.configuration;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCECPrivateKey(String string, org.bouncycastle.jce.spec.ECPrivateKeySpec eCPrivateKeySpec, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.d = eCPrivateKeySpec.getD();
        this.ecSpec = eCPrivateKeySpec.getParams() != null ? EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCPrivateKeySpec.getParams().getCurve(), (byte[])eCPrivateKeySpec.getParams().getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCPrivateKeySpec.getParams()) : null;
        this.configuration = providerConfiguration;
    }

    public BCECPrivateKey(java.security.interfaces.ECPrivateKey eCPrivateKey, ProviderConfiguration providerConfiguration) {
        this.d = eCPrivateKey.getS();
        this.algorithm = eCPrivateKey.getAlgorithm();
        this.ecSpec = eCPrivateKey.getParams();
        this.configuration = providerConfiguration;
    }

    private DERBitString getPublicKeyDetails(BCECPublicKey bCECPublicKey) {
        try {
            DERBitString dERBitString = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(bCECPublicKey.getEncoded())).getPublicKeyData();
            return dERBitString;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void populateFromPrivKeyInfo(PrivateKeyInfo privateKeyInfo) {
        ASN1Encodable aSN1Encodable;
        X962Parameters x962Parameters = X962Parameters.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        if (x962Parameters.isNamedCurve()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters());
            X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid((ASN1ObjectIdentifier)aSN1ObjectIdentifier);
            EllipticCurve ellipticCurve = EC5Util.convertCurve((ECCurve)x9ECParameters.getCurve(), (byte[])x9ECParameters.getSeed());
            this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName((ASN1ObjectIdentifier)aSN1ObjectIdentifier), ellipticCurve, new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH());
        } else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
        } else {
            X9ECParameters x9ECParameters = X9ECParameters.getInstance(x962Parameters.getParameters());
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve((ECCurve)x9ECParameters.getCurve(), (byte[])x9ECParameters.getSeed()), new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
        }
        if ((aSN1Encodable = privateKeyInfo.parsePrivateKey()) instanceof ASN1Integer) {
            this.d = ASN1Integer.getInstance(aSN1Encodable).getValue();
            return;
        }
        org.bouncycastle.asn1.sec.ECPrivateKey eCPrivateKey = org.bouncycastle.asn1.sec.ECPrivateKey.getInstance(aSN1Encodable);
        this.d = eCPrivateKey.getKey();
        this.publicKey = eCPrivateKey.getPublicKey();
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.configuration = BouncyCastleProvider.CONFIGURATION;
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject((Object)this.getEncoded());
    }

    org.bouncycastle.jce.spec.ECParameterSpec engineGetSpec() {
        if (this.ecSpec != null) {
            return EC5Util.convertSpec((ECParameterSpec)this.ecSpec, (boolean)this.withCompression);
        }
        return this.configuration.getEcImplicitlyCa();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof BCECPrivateKey)) break block2;
                BCECPrivateKey bCECPrivateKey = (BCECPrivateKey)object;
                if (this.getD().equals((Object)bCECPrivateKey.getD()) && this.engineGetSpec().equals((Object)bCECPrivateKey.engineGetSpec())) break block3;
            }
            return false;
        }
        return true;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    @Override
    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(aSN1ObjectIdentifier);
    }

    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }

    @Override
    public BigInteger getD() {
        return this.d;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getEncoded() {
        X962Parameters x962Parameters;
        int n2;
        if (this.ecSpec instanceof ECNamedCurveSpec) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = ECUtil.getNamedCurveOid((String)((ECNamedCurveSpec)this.ecSpec).getName());
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
            }
            x962Parameters = new X962Parameters(aSN1ObjectIdentifier);
            n2 = ECUtil.getOrderBitLength((BigInteger)this.ecSpec.getOrder(), (BigInteger)this.getS());
        } else if (this.ecSpec == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
            n2 = ECUtil.getOrderBitLength(null, (BigInteger)this.getS());
        } else {
            ECCurve eCCurve = EC5Util.convertCurve((EllipticCurve)this.ecSpec.getCurve());
            x962Parameters = new X962Parameters(new X9ECParameters(eCCurve, EC5Util.convertPoint((ECCurve)eCCurve, (ECPoint)this.ecSpec.getGenerator(), (boolean)this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf((long)this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            n2 = ECUtil.getOrderBitLength((BigInteger)this.ecSpec.getOrder(), (BigInteger)this.getS());
        }
        org.bouncycastle.asn1.sec.ECPrivateKey eCPrivateKey = this.publicKey != null ? new org.bouncycastle.asn1.sec.ECPrivateKey(n2, this.getS(), this.publicKey, x962Parameters) : new org.bouncycastle.asn1.sec.ECPrivateKey(n2, this.getS(), (ASN1Encodable)x962Parameters);
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), eCPrivateKey).getEncoded("DER");
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public String getFormat() {
        return "PKCS#8";
    }

    @Override
    public org.bouncycastle.jce.spec.ECParameterSpec getParameters() {
        if (this.ecSpec == null) {
            return null;
        }
        return EC5Util.convertSpec((ECParameterSpec)this.ecSpec, (boolean)this.withCompression);
    }

    public ECParameterSpec getParams() {
        return this.ecSpec;
    }

    public BigInteger getS() {
        return this.d;
    }

    public int hashCode() {
        return this.getD().hashCode() ^ this.engineGetSpec().hashCode();
    }

    @Override
    public void setBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.attrCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setPointFormat(String string) {
        boolean bl = !"UNCOMPRESSED".equalsIgnoreCase(string);
        this.withCompression = bl;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("EC Private Key").append(string);
        stringBuffer.append("             S: ").append(this.d.toString(16)).append(string);
        return stringBuffer.toString();
    }
}

