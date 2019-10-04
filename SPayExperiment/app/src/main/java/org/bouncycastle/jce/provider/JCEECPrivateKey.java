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
 */
package org.bouncycastle.jce.provider;

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
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class JCEECPrivateKey
implements java.security.interfaces.ECPrivateKey,
ECPointEncoder,
ECPrivateKey,
PKCS12BagAttributeCarrier {
    private String algorithm = "EC";
    private PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
    private BigInteger d;
    private ECParameterSpec ecSpec;
    private DERBitString publicKey;
    private boolean withCompression;

    protected JCEECPrivateKey() {
    }

    public JCEECPrivateKey(String string, ECPrivateKeySpec eCPrivateKeySpec) {
        this.algorithm = string;
        this.d = eCPrivateKeySpec.getS();
        this.ecSpec = eCPrivateKeySpec.getParams();
    }

    public JCEECPrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters) {
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public JCEECPrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, JCEECPublicKey jCEECPublicKey, ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = eCParameterSpec == null ? new ECParameterSpec(EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed()), new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue()) : eCParameterSpec;
        this.publicKey = this.getPublicKeyDetails(jCEECPublicKey);
    }

    /*
     * Enabled aggressive block sorting
     */
    public JCEECPrivateKey(String string, ECPrivateKeyParameters eCPrivateKeyParameters, JCEECPublicKey jCEECPublicKey, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        this.algorithm = string;
        this.d = eCPrivateKeyParameters.getD();
        this.ecSpec = eCParameterSpec == null ? new ECParameterSpec(EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed()), new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue()) : new ECParameterSpec(EC5Util.convertCurve(eCParameterSpec.getCurve(), eCParameterSpec.getSeed()), new ECPoint(eCParameterSpec.getG().getAffineXCoord().toBigInteger(), eCParameterSpec.getG().getAffineYCoord().toBigInteger()), eCParameterSpec.getN(), eCParameterSpec.getH().intValue());
        this.publicKey = this.getPublicKeyDetails(jCEECPublicKey);
    }

    public JCEECPrivateKey(String string, JCEECPrivateKey jCEECPrivateKey) {
        this.algorithm = string;
        this.d = jCEECPrivateKey.d;
        this.ecSpec = jCEECPrivateKey.ecSpec;
        this.withCompression = jCEECPrivateKey.withCompression;
        this.attrCarrier = jCEECPrivateKey.attrCarrier;
        this.publicKey = jCEECPrivateKey.publicKey;
    }

    public JCEECPrivateKey(String string, org.bouncycastle.jce.spec.ECPrivateKeySpec eCPrivateKeySpec) {
        this.algorithm = string;
        this.d = eCPrivateKeySpec.getD();
        if (eCPrivateKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(eCPrivateKeySpec.getParams().getCurve(), eCPrivateKeySpec.getParams().getSeed()), eCPrivateKeySpec.getParams());
            return;
        }
        this.ecSpec = null;
    }

    public JCEECPrivateKey(java.security.interfaces.ECPrivateKey eCPrivateKey) {
        this.d = eCPrivateKey.getS();
        this.algorithm = eCPrivateKey.getAlgorithm();
        this.ecSpec = eCPrivateKey.getParams();
    }

    JCEECPrivateKey(PrivateKeyInfo privateKeyInfo) {
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }

    private DERBitString getPublicKeyDetails(JCEECPublicKey jCEECPublicKey) {
        try {
            DERBitString dERBitString = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(jCEECPublicKey.getEncoded())).getPublicKeyData();
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
            X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid(aSN1ObjectIdentifier);
            if (x9ECParameters == null) {
                ECDomainParameters eCDomainParameters = ECGOST3410NamedCurves.getByOID(aSN1ObjectIdentifier);
                EllipticCurve ellipticCurve = EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed());
                this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(aSN1ObjectIdentifier), ellipticCurve, new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH());
            } else {
                EllipticCurve ellipticCurve = EC5Util.convertCurve(x9ECParameters.getCurve(), x9ECParameters.getSeed());
                this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(aSN1ObjectIdentifier), ellipticCurve, new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH());
            }
        } else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
        } else {
            X9ECParameters x9ECParameters = X9ECParameters.getInstance(x962Parameters.getParameters());
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(x9ECParameters.getCurve(), x9ECParameters.getSeed()), new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
        }
        if ((aSN1Encodable = privateKeyInfo.parsePrivateKey()) instanceof ASN1Integer) {
            this.d = ASN1Integer.getInstance(aSN1Encodable).getValue();
            return;
        }
        ECPrivateKeyStructure eCPrivateKeyStructure = new ECPrivateKeyStructure((ASN1Sequence)aSN1Encodable);
        this.d = eCPrivateKeyStructure.getKey();
        this.publicKey = eCPrivateKeyStructure.getPublicKey();
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.algorithm = (String)objectInputStream.readObject();
        this.withCompression = objectInputStream.readBoolean();
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.attrCarrier.readObject(objectInputStream);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeObject((Object)this.getEncoded());
        objectOutputStream.writeObject((Object)this.algorithm);
        objectOutputStream.writeBoolean(this.withCompression);
        this.attrCarrier.writeObject(objectOutputStream);
    }

    org.bouncycastle.jce.spec.ECParameterSpec engineGetSpec() {
        if (this.ecSpec != null) {
            return EC5Util.convertSpec(this.ecSpec, this.withCompression);
        }
        return BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof JCEECPrivateKey)) break block2;
                JCEECPrivateKey jCEECPrivateKey = (JCEECPrivateKey)object;
                if (this.getD().equals((Object)jCEECPrivateKey.getD()) && this.engineGetSpec().equals(jCEECPrivateKey.engineGetSpec())) break block3;
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
        if (this.ecSpec instanceof ECNamedCurveSpec) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = ECUtil.getNamedCurveOid(((ECNamedCurveSpec)this.ecSpec).getName());
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
            }
            x962Parameters = new X962Parameters(aSN1ObjectIdentifier);
        } else if (this.ecSpec == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
        } else {
            ECCurve eCCurve = EC5Util.convertCurve(this.ecSpec.getCurve());
            x962Parameters = new X962Parameters(new X9ECParameters(eCCurve, EC5Util.convertPoint(eCCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf((long)this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        ECPrivateKeyStructure eCPrivateKeyStructure = this.publicKey != null ? new ECPrivateKeyStructure(this.getS(), this.publicKey, x962Parameters) : new ECPrivateKeyStructure(this.getS(), x962Parameters);
        try {
            PrivateKeyInfo privateKeyInfo;
            PrivateKeyInfo privateKeyInfo2 = this.algorithm.equals((Object)"ECGOST3410") ? new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, x962Parameters.toASN1Primitive()), eCPrivateKeyStructure.toASN1Primitive()) : (privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters.toASN1Primitive()), eCPrivateKeyStructure.toASN1Primitive()));
            return privateKeyInfo2.getEncoded("DER");
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
        return EC5Util.convertSpec(this.ecSpec, this.withCompression);
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

