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
package org.bouncycastle.jcajce.provider.asymmetric.dstu;

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
import org.bouncycastle.asn1.ua.DSTU4145NamedCurves;
import org.bouncycastle.asn1.ua.UAObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.dstu.BCDSTU4145PublicKey;
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

public class BCDSTU4145PrivateKey
implements java.security.interfaces.ECPrivateKey,
ECPointEncoder,
ECPrivateKey,
PKCS12BagAttributeCarrier {
    static final long serialVersionUID = 7245981689601667138L;
    private String algorithm = "DSTU4145";
    private transient PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
    private transient BigInteger d;
    private transient ECParameterSpec ecSpec;
    private transient DERBitString publicKey;
    private boolean withCompression;

    protected BCDSTU4145PrivateKey() {
    }

    public BCDSTU4145PrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters) {
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCDSTU4145PrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, BCDSTU4145PublicKey bCDSTU4145PublicKey, ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = eCParameterSpec == null ? new ECParameterSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue()) : eCParameterSpec;
        this.publicKey = this.getPublicKeyDetails(bCDSTU4145PublicKey);
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCDSTU4145PrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, BCDSTU4145PublicKey bCDSTU4145PublicKey, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = eCParameterSpec == null ? new ECParameterSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue()) : new ECParameterSpec(EC5Util.convertCurve((ECCurve)eCParameterSpec.getCurve(), (byte[])eCParameterSpec.getSeed()), new ECPoint(eCParameterSpec.getG().getAffineXCoord().toBigInteger(), eCParameterSpec.getG().getAffineYCoord().toBigInteger()), eCParameterSpec.getN(), eCParameterSpec.getH().intValue());
        this.publicKey = this.getPublicKeyDetails(bCDSTU4145PublicKey);
    }

    public BCDSTU4145PrivateKey(java.security.interfaces.ECPrivateKey eCPrivateKey) {
        this.d = eCPrivateKey.getS();
        this.algorithm = eCPrivateKey.getAlgorithm();
        this.ecSpec = eCPrivateKey.getParams();
    }

    public BCDSTU4145PrivateKey(ECPrivateKeySpec eCPrivateKeySpec) {
        this.d = eCPrivateKeySpec.getS();
        this.ecSpec = eCPrivateKeySpec.getParams();
    }

    BCDSTU4145PrivateKey(PrivateKeyInfo privateKeyInfo) {
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }

    public BCDSTU4145PrivateKey(BCDSTU4145PrivateKey bCDSTU4145PrivateKey) {
        this.d = bCDSTU4145PrivateKey.d;
        this.ecSpec = bCDSTU4145PrivateKey.ecSpec;
        this.withCompression = bCDSTU4145PrivateKey.withCompression;
        this.attrCarrier = bCDSTU4145PrivateKey.attrCarrier;
        this.publicKey = bCDSTU4145PrivateKey.publicKey;
    }

    public BCDSTU4145PrivateKey(org.bouncycastle.jce.spec.ECPrivateKeySpec eCPrivateKeySpec) {
        this.d = eCPrivateKeySpec.getD();
        if (eCPrivateKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCPrivateKeySpec.getParams().getCurve(), (byte[])eCPrivateKeySpec.getParams().getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCPrivateKeySpec.getParams());
            return;
        }
        this.ecSpec = null;
    }

    private DERBitString getPublicKeyDetails(BCDSTU4145PublicKey bCDSTU4145PublicKey) {
        try {
            DERBitString dERBitString = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(bCDSTU4145PublicKey.getEncoded())).getPublicKeyData();
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
        X962Parameters x962Parameters = new X962Parameters((ASN1Primitive)privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        if (x962Parameters.isNamedCurve()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters());
            X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid((ASN1ObjectIdentifier)aSN1ObjectIdentifier);
            if (x9ECParameters == null) {
                ECDomainParameters eCDomainParameters = DSTU4145NamedCurves.getByOID(aSN1ObjectIdentifier);
                EllipticCurve ellipticCurve = EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed());
                this.ecSpec = new ECNamedCurveSpec(aSN1ObjectIdentifier.getId(), ellipticCurve, new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH());
            } else {
                EllipticCurve ellipticCurve = EC5Util.convertCurve((ECCurve)x9ECParameters.getCurve(), (byte[])x9ECParameters.getSeed());
                this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName((ASN1ObjectIdentifier)aSN1ObjectIdentifier), ellipticCurve, new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH());
            }
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
        return BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof BCDSTU4145PrivateKey)) break block2;
                BCDSTU4145PrivateKey bCDSTU4145PrivateKey = (BCDSTU4145PrivateKey)object;
                if (this.getD().equals((Object)bCDSTU4145PrivateKey.getD()) && this.engineGetSpec().equals((Object)bCDSTU4145PrivateKey.engineGetSpec())) break block3;
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
            PrivateKeyInfo privateKeyInfo = this.algorithm.equals((Object)"DSTU4145") ? new PrivateKeyInfo(new AlgorithmIdentifier(UAObjectIdentifiers.dstu4145be, x962Parameters.toASN1Primitive()), eCPrivateKey.toASN1Primitive()) : new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters.toASN1Primitive()), eCPrivateKey.toASN1Primitive());
            return privateKeyInfo.getEncoded("DER");
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

