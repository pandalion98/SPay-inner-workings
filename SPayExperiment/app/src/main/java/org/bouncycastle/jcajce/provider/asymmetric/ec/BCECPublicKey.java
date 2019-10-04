/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.ObjectInputStream
 *  java.io.ObjectOutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.interfaces.ECPublicKey
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 *  java.security.spec.ECPublicKeySpec
 *  java.security.spec.EllipticCurve
 *  org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util
 *  org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
 *  org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil
 *  org.bouncycastle.jcajce.provider.config.ProviderConfiguration
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.jce.spec.ECNamedCurveSpec
 *  org.bouncycastle.jce.spec.ECParameterSpec
 *  org.bouncycastle.jce.spec.ECPublicKeySpec
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.jcajce.provider.asymmetric.ec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class BCECPublicKey
implements ECPublicKey,
ECPointEncoder,
org.bouncycastle.jce.interfaces.ECPublicKey {
    static final long serialVersionUID = 2422789860422731812L;
    private String algorithm = "EC";
    private transient ProviderConfiguration configuration;
    private transient ECParameterSpec ecSpec;
    private transient org.bouncycastle.math.ec.ECPoint q;
    private boolean withCompression;

    public BCECPublicKey(String string, java.security.spec.ECPublicKeySpec eCPublicKeySpec, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.ecSpec = eCPublicKeySpec.getParams();
        this.q = EC5Util.convertPoint((ECParameterSpec)this.ecSpec, (ECPoint)eCPublicKeySpec.getW(), (boolean)false);
        this.configuration = providerConfiguration;
    }

    BCECPublicKey(String string, SubjectPublicKeyInfo subjectPublicKeyInfo, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.configuration = providerConfiguration;
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCECPublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, ECParameterSpec eCParameterSpec, ProviderConfiguration providerConfiguration) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        this.ecSpec = eCParameterSpec == null ? this.createSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), eCDomainParameters) : eCParameterSpec;
        this.configuration = providerConfiguration;
    }

    public BCECPublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        this.ecSpec = null;
        this.configuration = providerConfiguration;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCECPublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec, ProviderConfiguration providerConfiguration) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.ecSpec = eCParameterSpec == null ? this.createSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), eCDomainParameters) : EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCParameterSpec.getCurve(), (byte[])eCParameterSpec.getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCParameterSpec);
        this.q = EC5Util.convertCurve((EllipticCurve)this.ecSpec.getCurve()).createPoint(eCPublicKeyParameters.getQ().getAffineXCoord().toBigInteger(), eCPublicKeyParameters.getQ().getAffineYCoord().toBigInteger());
        this.configuration = providerConfiguration;
    }

    public BCECPublicKey(String string, BCECPublicKey bCECPublicKey) {
        this.algorithm = string;
        this.q = bCECPublicKey.q;
        this.ecSpec = bCECPublicKey.ecSpec;
        this.withCompression = bCECPublicKey.withCompression;
        this.configuration = bCECPublicKey.configuration;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BCECPublicKey(String string, ECPublicKeySpec eCPublicKeySpec, ProviderConfiguration providerConfiguration) {
        this.algorithm = string;
        this.q = eCPublicKeySpec.getQ();
        if (eCPublicKeySpec.getParams() != null) {
            EllipticCurve ellipticCurve = EC5Util.convertCurve((ECCurve)eCPublicKeySpec.getParams().getCurve(), (byte[])eCPublicKeySpec.getParams().getSeed());
            this.q = EC5Util.convertCurve((EllipticCurve)ellipticCurve).createPoint(eCPublicKeySpec.getQ().getAffineXCoord().toBigInteger(), eCPublicKeySpec.getQ().getAffineYCoord().toBigInteger());
            this.ecSpec = EC5Util.convertSpec((EllipticCurve)ellipticCurve, (org.bouncycastle.jce.spec.ECParameterSpec)eCPublicKeySpec.getParams());
        } else {
            if (this.q.getCurve() == null) {
                this.q = providerConfiguration.getEcImplicitlyCa().getCurve().createPoint(this.q.getXCoord().toBigInteger(), this.q.getYCoord().toBigInteger(), false);
            }
            this.ecSpec = null;
        }
        this.configuration = providerConfiguration;
    }

    public BCECPublicKey(ECPublicKey eCPublicKey, ProviderConfiguration providerConfiguration) {
        this.algorithm = eCPublicKey.getAlgorithm();
        this.ecSpec = eCPublicKey.getParams();
        this.q = EC5Util.convertPoint((ECParameterSpec)this.ecSpec, (ECPoint)eCPublicKey.getW(), (boolean)false);
    }

    private ECParameterSpec createSpec(EllipticCurve ellipticCurve, ECDomainParameters eCDomainParameters) {
        return new ECParameterSpec(ellipticCurve, new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void extractBytes(byte[] arrby, int n2, BigInteger bigInteger) {
        byte[] arrby2;
        byte[] arrby3 = bigInteger.toByteArray();
        if (arrby3.length < 32) {
            arrby2 = new byte[32];
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(arrby2.length - arrby3.length), (int)arrby3.length);
        } else {
            arrby2 = arrby3;
        }
        int n3 = 0;
        while (n3 != 32) {
            arrby[n2 + n3] = arrby2[-1 + arrby2.length - n3];
            ++n3;
        }
        return;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void populateFromPubKeyInfo(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        void var7_11;
        ECCurve eCCurve;
        X962Parameters x962Parameters = new X962Parameters((ASN1Primitive)subjectPublicKeyInfo.getAlgorithm().getParameters());
        if (x962Parameters.isNamedCurve()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)x962Parameters.getParameters();
            X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid((ASN1ObjectIdentifier)aSN1ObjectIdentifier);
            ECCurve eCCurve2 = x9ECParameters.getCurve();
            EllipticCurve ellipticCurve = EC5Util.convertCurve((ECCurve)eCCurve2, (byte[])x9ECParameters.getSeed());
            this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName((ASN1ObjectIdentifier)aSN1ObjectIdentifier), ellipticCurve, new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH());
            eCCurve = eCCurve2;
        } else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
            eCCurve = this.configuration.getEcImplicitlyCa().getCurve();
        } else {
            X9ECParameters x9ECParameters = X9ECParameters.getInstance(x962Parameters.getParameters());
            ECCurve eCCurve3 = x9ECParameters.getCurve();
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve((ECCurve)eCCurve3, (byte[])x9ECParameters.getSeed()), new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
            eCCurve = eCCurve3;
        }
        byte[] arrby = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        DEROctetString dEROctetString = new DEROctetString(arrby);
        if (arrby[0] == 4 && arrby[1] == -2 + arrby.length && (arrby[2] == 2 || arrby[2] == 3) && new X9IntegerConverter().getByteLength(eCCurve) >= -3 + arrby.length) {
            ASN1OctetString aSN1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(arrby);
        }
        this.q = new X9ECPoint(eCCurve, (ASN1OctetString)var7_11).getPoint();
        return;
        catch (IOException iOException) {
            throw new IllegalArgumentException("error recovering public key");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.configuration = BouncyCastleProvider.CONFIGURATION;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject((Object)this.getEncoded());
    }

    public org.bouncycastle.math.ec.ECPoint engineGetQ() {
        return this.q;
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
                if (!(object instanceof BCECPublicKey)) break block2;
                BCECPublicKey bCECPublicKey = (BCECPublicKey)object;
                if (this.engineGetQ().equals(bCECPublicKey.engineGetQ()) && this.engineGetSpec().equals((Object)bCECPublicKey.engineGetSpec())) break block3;
            }
            return false;
        }
        return true;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] getEncoded() {
        X962Parameters x962Parameters;
        ASN1OctetString aSN1OctetString;
        if (this.ecSpec instanceof ECNamedCurveSpec) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = ECUtil.getNamedCurveOid((String)((ECNamedCurveSpec)this.ecSpec).getName());
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
            }
            x962Parameters = new X962Parameters(aSN1ObjectIdentifier);
        } else if (this.ecSpec == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
        } else {
            ECCurve eCCurve = EC5Util.convertCurve((EllipticCurve)this.ecSpec.getCurve());
            x962Parameters = new X962Parameters(new X9ECParameters(eCCurve, EC5Util.convertPoint((ECCurve)eCCurve, (ECPoint)this.ecSpec.getGenerator(), (boolean)this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf((long)this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        ECCurve eCCurve = this.engineGetQ().getCurve();
        if (this.ecSpec == null) {
            aSN1OctetString = (ASN1OctetString)new X9ECPoint(eCCurve.createPoint(this.getQ().getXCoord().toBigInteger(), this.getQ().getYCoord().toBigInteger(), this.withCompression)).toASN1Primitive();
            return KeyUtil.getEncodedSubjectPublicKeyInfo((SubjectPublicKeyInfo)new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), aSN1OctetString.getOctets()));
        }
        aSN1OctetString = (ASN1OctetString)new X9ECPoint(eCCurve.createPoint(this.getQ().getAffineXCoord().toBigInteger(), this.getQ().getAffineYCoord().toBigInteger(), this.withCompression)).toASN1Primitive();
        return KeyUtil.getEncodedSubjectPublicKeyInfo((SubjectPublicKeyInfo)new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), aSN1OctetString.getOctets()));
    }

    public String getFormat() {
        return "X.509";
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

    @Override
    public org.bouncycastle.math.ec.ECPoint getQ() {
        if (this.ecSpec == null) {
            return this.q.getDetachedPoint();
        }
        return this.q;
    }

    public ECPoint getW() {
        return new ECPoint(this.q.getAffineXCoord().toBigInteger(), this.q.getAffineYCoord().toBigInteger());
    }

    public int hashCode() {
        return this.engineGetQ().hashCode() ^ this.engineGetSpec().hashCode();
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
        stringBuffer.append("EC Public Key").append(string);
        stringBuffer.append("            X: ").append(this.q.getAffineXCoord().toBigInteger().toString(16)).append(string);
        stringBuffer.append("            Y: ").append(this.q.getAffineYCoord().toBigInteger().toString(16)).append(string);
        return stringBuffer.toString();
    }
}

