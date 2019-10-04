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
package org.bouncycastle.jcajce.provider.asymmetric.ecgost;

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
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.ECGOST3410NamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class BCECGOST3410PublicKey
implements ECPublicKey,
ECPointEncoder,
org.bouncycastle.jce.interfaces.ECPublicKey {
    static final long serialVersionUID = 7026240464295649314L;
    private String algorithm = "ECGOST3410";
    private transient ECParameterSpec ecSpec;
    private transient GOST3410PublicKeyAlgParameters gostParams;
    private transient org.bouncycastle.math.ec.ECPoint q;
    private boolean withCompression;

    public BCECGOST3410PublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters) {
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        this.ecSpec = null;
    }

    public BCECGOST3410PublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        if (eCParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed()), eCDomainParameters);
            return;
        }
        this.ecSpec = eCParameterSpec;
    }

    public BCECGOST3410PublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        if (eCParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(eCDomainParameters.getCurve(), eCDomainParameters.getSeed()), eCDomainParameters);
            return;
        }
        this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(eCParameterSpec.getCurve(), eCParameterSpec.getSeed()), eCParameterSpec);
    }

    public BCECGOST3410PublicKey(ECPublicKey eCPublicKey) {
        this.algorithm = eCPublicKey.getAlgorithm();
        this.ecSpec = eCPublicKey.getParams();
        this.q = EC5Util.convertPoint(this.ecSpec, eCPublicKey.getW(), false);
    }

    public BCECGOST3410PublicKey(java.security.spec.ECPublicKeySpec eCPublicKeySpec) {
        this.ecSpec = eCPublicKeySpec.getParams();
        this.q = EC5Util.convertPoint(this.ecSpec, eCPublicKeySpec.getW(), false);
    }

    BCECGOST3410PublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }

    public BCECGOST3410PublicKey(BCECGOST3410PublicKey bCECGOST3410PublicKey) {
        this.q = bCECGOST3410PublicKey.q;
        this.ecSpec = bCECGOST3410PublicKey.ecSpec;
        this.withCompression = bCECGOST3410PublicKey.withCompression;
        this.gostParams = bCECGOST3410PublicKey.gostParams;
    }

    public BCECGOST3410PublicKey(ECPublicKeySpec eCPublicKeySpec) {
        this.q = eCPublicKeySpec.getQ();
        if (eCPublicKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(eCPublicKeySpec.getParams().getCurve(), eCPublicKeySpec.getParams().getSeed()), eCPublicKeySpec.getParams());
            return;
        }
        if (this.q.getCurve() == null) {
            this.q = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve().createPoint(this.q.getAffineXCoord().toBigInteger(), this.q.getAffineYCoord().toBigInteger());
        }
        this.ecSpec = null;
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

    private void populateFromPubKeyInfo(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        byte[] arrby;
        byte[] arrby2;
        byte[] arrby3;
        ASN1OctetString aSN1OctetString;
        DERBitString dERBitString = subjectPublicKeyInfo.getPublicKeyData();
        this.algorithm = "ECGOST3410";
        try {
            aSN1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(dERBitString.getBytes());
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("error recovering public key");
        }
        arrby3 = aSN1OctetString.getOctets();
        arrby = new byte[32];
        arrby2 = new byte[32];
        int n = 0;
        do {
            int n2 = arrby.length;
            if (n != n2) {
                arrby[n] = arrby3[31 - n];
                ++n;
                continue;
            }
            break;
        } while (true);
        for (int i = 0; i != arrby2.length; ++i) {
            arrby2[i] = arrby3[63 - i];
        }
        this.gostParams = GOST3410PublicKeyAlgParameters.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        ECNamedCurveParameterSpec eCNamedCurveParameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()));
        ECCurve eCCurve = eCNamedCurveParameterSpec.getCurve();
        EllipticCurve ellipticCurve = EC5Util.convertCurve(eCCurve, eCNamedCurveParameterSpec.getSeed());
        this.q = eCCurve.createPoint(new BigInteger(1, arrby), new BigInteger(1, arrby2));
        this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), ellipticCurve, new ECPoint(eCNamedCurveParameterSpec.getG().getAffineXCoord().toBigInteger(), eCNamedCurveParameterSpec.getG().getAffineYCoord().toBigInteger()), eCNamedCurveParameterSpec.getN(), eCNamedCurveParameterSpec.getH());
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
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
                if (!(object instanceof BCECGOST3410PublicKey)) break block2;
                BCECGOST3410PublicKey bCECGOST3410PublicKey = (BCECGOST3410PublicKey)object;
                if (this.engineGetQ().equals(bCECGOST3410PublicKey.engineGetQ()) && this.engineGetSpec().equals(bCECGOST3410PublicKey.engineGetSpec())) break block3;
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
        SubjectPublicKeyInfo subjectPublicKeyInfo;
        if (this.gostParams != null) {
            GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = this.gostParams;
        } else if (this.ecSpec instanceof ECNamedCurveSpec) {
            GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet);
        } else {
            ECCurve eCCurve = EC5Util.convertCurve(this.ecSpec.getCurve());
            X962Parameters x962Parameters = new X962Parameters(new X9ECParameters(eCCurve, EC5Util.convertPoint(eCCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf((long)this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        BigInteger bigInteger = this.q.getAffineXCoord().toBigInteger();
        BigInteger bigInteger2 = this.q.getAffineYCoord().toBigInteger();
        byte[] arrby = new byte[64];
        this.extractBytes(arrby, 0, bigInteger);
        this.extractBytes(arrby, 32, bigInteger2);
        try {
            void var2_2;
            subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, (ASN1Encodable)var2_2), new DEROctetString(arrby));
        }
        catch (IOException iOException) {
            return null;
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
    }

    public String getFormat() {
        return "X.509";
    }

    public GOST3410PublicKeyAlgParameters getGostParams() {
        return this.gostParams;
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

