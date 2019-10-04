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
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
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
import org.bouncycastle.jce.ECGOST3410NamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class JCEECPublicKey
implements ECPublicKey,
ECPointEncoder,
org.bouncycastle.jce.interfaces.ECPublicKey {
    private String algorithm = "EC";
    private ECParameterSpec ecSpec;
    private GOST3410PublicKeyAlgParameters gostParams;
    private org.bouncycastle.math.ec.ECPoint q;
    private boolean withCompression;

    public JCEECPublicKey(String string, ECPublicKeySpec eCPublicKeySpec) {
        this.algorithm = string;
        this.ecSpec = eCPublicKeySpec.getParams();
        this.q = EC5Util.convertPoint(this.ecSpec, eCPublicKeySpec.getW(), false);
    }

    public JCEECPublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters) {
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        this.ecSpec = null;
    }

    public JCEECPublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        if (eCParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed()), eCDomainParameters);
            return;
        }
        this.ecSpec = eCParameterSpec;
    }

    public JCEECPublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        if (eCParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed()), eCDomainParameters);
            return;
        }
        this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(eCParameterSpec.getCurve(), eCParameterSpec.getSeed()), eCParameterSpec);
    }

    public JCEECPublicKey(String string, JCEECPublicKey jCEECPublicKey) {
        this.algorithm = string;
        this.q = jCEECPublicKey.q;
        this.ecSpec = jCEECPublicKey.ecSpec;
        this.withCompression = jCEECPublicKey.withCompression;
        this.gostParams = jCEECPublicKey.gostParams;
    }

    public JCEECPublicKey(String string, org.bouncycastle.jce.spec.ECPublicKeySpec eCPublicKeySpec) {
        this.algorithm = string;
        this.q = eCPublicKeySpec.getQ();
        if (eCPublicKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(eCPublicKeySpec.getParams().getCurve(), eCPublicKeySpec.getParams().getSeed()), eCPublicKeySpec.getParams());
            return;
        }
        if (this.q.getCurve() == null) {
            this.q = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve().createPoint(this.q.getAffineXCoord().toBigInteger(), this.q.getAffineYCoord().toBigInteger(), false);
        }
        this.ecSpec = null;
    }

    public JCEECPublicKey(ECPublicKey eCPublicKey) {
        this.algorithm = eCPublicKey.getAlgorithm();
        this.ecSpec = eCPublicKey.getParams();
        this.q = EC5Util.convertPoint(this.ecSpec, eCPublicKey.getW(), false);
    }

    JCEECPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }

    private ECParameterSpec createSpec(EllipticCurve ellipticCurve, ECDomainParameters eCDomainParameters) {
        return new ECParameterSpec(ellipticCurve, new ECPoint(eCDomainParameters.getG().getAffineXCoord().toBigInteger(), eCDomainParameters.getG().getAffineYCoord().toBigInteger()), eCDomainParameters.getN(), eCDomainParameters.getH().intValue());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void extractBytes(byte[] arrby, int n, BigInteger bigInteger) {
        byte[] arrby2;
        byte[] arrby3 = bigInteger.toByteArray();
        if (arrby3.length < 32) {
            arrby2 = new byte[32];
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(arrby2.length - arrby3.length), (int)arrby3.length);
        } else {
            arrby2 = arrby3;
        }
        int n2 = 0;
        while (n2 != 32) {
            arrby[n + n2] = arrby2[-1 + arrby2.length - n2];
            ++n2;
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
        void var7_22;
        ECCurve eCCurve;
        block12 : {
            byte[] arrby;
            byte[] arrby3;
            byte[] arrby2;
            ASN1OctetString aSN1OctetString;
            if (!subjectPublicKeyInfo.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001)) break block12;
            DERBitString dERBitString = subjectPublicKeyInfo.getPublicKeyData();
            this.algorithm = "ECGOST3410";
            try {
                aSN1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(dERBitString.getBytes());
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("error recovering public key");
            }
            arrby2 = aSN1OctetString.getOctets();
            arrby = new byte[32];
            arrby3 = new byte[32];
            for (int i = 0; i != arrby.length; ++i) {
                arrby[i] = arrby2[31 - i];
            }
            int n = 0;
            do {
                if (n == arrby3.length) {
                    this.gostParams = new GOST3410PublicKeyAlgParameters((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
                    ECNamedCurveParameterSpec eCNamedCurveParameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()));
                    ECCurve eCCurve2 = eCNamedCurveParameterSpec.getCurve();
                    EllipticCurve ellipticCurve = EC5Util.convertCurve(eCCurve2, eCNamedCurveParameterSpec.getSeed());
                    this.q = eCCurve2.createPoint(new BigInteger(1, arrby), new BigInteger(1, arrby3), false);
                    this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), ellipticCurve, new ECPoint(eCNamedCurveParameterSpec.getG().getAffineXCoord().toBigInteger(), eCNamedCurveParameterSpec.getG().getAffineYCoord().toBigInteger()), eCNamedCurveParameterSpec.getN(), eCNamedCurveParameterSpec.getH());
                    return;
                }
                arrby3[n] = arrby2[63 - n];
                ++n;
            } while (true);
        }
        X962Parameters x962Parameters = new X962Parameters((ASN1Primitive)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        if (x962Parameters.isNamedCurve()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)x962Parameters.getParameters();
            X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid(aSN1ObjectIdentifier);
            ECCurve eCCurve3 = x9ECParameters.getCurve();
            EllipticCurve ellipticCurve = EC5Util.convertCurve(eCCurve3, x9ECParameters.getSeed());
            this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(aSN1ObjectIdentifier), ellipticCurve, new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH());
            eCCurve = eCCurve3;
        } else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
            eCCurve = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve();
        } else {
            X9ECParameters x9ECParameters = X9ECParameters.getInstance(x962Parameters.getParameters());
            ECCurve eCCurve4 = x9ECParameters.getCurve();
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(eCCurve4, x9ECParameters.getSeed()), new ECPoint(x9ECParameters.getG().getAffineXCoord().toBigInteger(), x9ECParameters.getG().getAffineYCoord().toBigInteger()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
            eCCurve = eCCurve4;
        }
        byte[] arrby = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        DEROctetString dEROctetString = new DEROctetString(arrby);
        if (arrby[0] == 4 && arrby[1] == -2 + arrby.length && (arrby[2] == 2 || arrby[2] == 3) && new X9IntegerConverter().getByteLength(eCCurve) >= -3 + arrby.length) {
            ASN1OctetString aSN1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(arrby);
        }
        this.q = new X9ECPoint(eCCurve, (ASN1OctetString)var7_22).getPoint();
        return;
        catch (IOException iOException) {
            throw new IllegalArgumentException("error recovering public key");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.algorithm = (String)objectInputStream.readObject();
        this.withCompression = objectInputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeObject((Object)this.getEncoded());
        objectOutputStream.writeObject((Object)this.algorithm);
        objectOutputStream.writeBoolean(this.withCompression);
    }

    public org.bouncycastle.math.ec.ECPoint engineGetQ() {
        return this.q;
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
                if (!(object instanceof JCEECPublicKey)) break block2;
                JCEECPublicKey jCEECPublicKey = (JCEECPublicKey)object;
                if (this.engineGetQ().equals(jCEECPublicKey.engineGetQ()) && this.engineGetSpec().equals(jCEECPublicKey.engineGetSpec())) break block3;
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
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getEncoded() {
        X962Parameters x962Parameters;
        SubjectPublicKeyInfo subjectPublicKeyInfo;
        if (this.algorithm.equals((Object)"ECGOST3410")) {
            if (this.gostParams != null) {
                GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = this.gostParams;
            } else if (this.ecSpec instanceof ECNamedCurveSpec) {
                GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet);
            } else {
                ECCurve eCCurve = EC5Util.convertCurve(this.ecSpec.getCurve());
                X962Parameters x962Parameters2 = new X962Parameters(new X9ECParameters(eCCurve, EC5Util.convertPoint(eCCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf((long)this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
            BigInteger bigInteger = this.q.getAffineXCoord().toBigInteger();
            BigInteger bigInteger2 = this.q.getAffineYCoord().toBigInteger();
            byte[] arrby = new byte[64];
            this.extractBytes(arrby, 0, bigInteger);
            this.extractBytes(arrby, 32, bigInteger2);
            try {
                void var7_2;
                SubjectPublicKeyInfo subjectPublicKeyInfo2;
                subjectPublicKeyInfo = subjectPublicKeyInfo2 = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, (ASN1Encodable)var7_2), new DEROctetString(arrby));
            }
            catch (IOException iOException) {
                return null;
            }
            return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
        }
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
        ASN1OctetString aSN1OctetString = (ASN1OctetString)new X9ECPoint(this.engineGetQ().getCurve().createPoint(this.getQ().getAffineXCoord().toBigInteger(), this.getQ().getAffineYCoord().toBigInteger(), this.withCompression)).toASN1Primitive();
        subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), aSN1OctetString.getOctets());
        return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
    }

    public String getFormat() {
        return "X.509";
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

