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
 *  org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil
 *  org.bouncycastle.jcajce.provider.config.ProviderConfiguration
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
 *  org.bouncycastle.jce.spec.ECNamedCurveSpec
 *  org.bouncycastle.jce.spec.ECParameterSpec
 *  org.bouncycastle.jce.spec.ECPublicKeySpec
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECCurve$F2m
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.jcajce.provider.asymmetric.dstu;

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
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ua.DSTU4145BinaryField;
import org.bouncycastle.asn1.ua.DSTU4145ECBinary;
import org.bouncycastle.asn1.ua.DSTU4145NamedCurves;
import org.bouncycastle.asn1.ua.DSTU4145Params;
import org.bouncycastle.asn1.ua.DSTU4145PointEncoder;
import org.bouncycastle.asn1.ua.UAObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.interfaces.ECPointEncoder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class BCDSTU4145PublicKey
implements ECPublicKey,
ECPointEncoder,
org.bouncycastle.jce.interfaces.ECPublicKey {
    static final long serialVersionUID = 7026240464295649314L;
    private String algorithm = "DSTU4145";
    private transient DSTU4145Params dstuParams;
    private transient ECParameterSpec ecSpec;
    private transient org.bouncycastle.math.ec.ECPoint q;
    private boolean withCompression;

    public BCDSTU4145PublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters) {
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        this.ecSpec = null;
    }

    public BCDSTU4145PublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        if (eCParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), eCDomainParameters);
            return;
        }
        this.ecSpec = eCParameterSpec;
    }

    public BCDSTU4145PublicKey(String string, ECPublicKeyParameters eCPublicKeyParameters, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec) {
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        this.algorithm = string;
        this.q = eCPublicKeyParameters.getQ();
        if (eCParameterSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve((ECCurve)eCDomainParameters.getCurve(), (byte[])eCDomainParameters.getSeed()), eCDomainParameters);
            return;
        }
        this.ecSpec = EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCParameterSpec.getCurve(), (byte[])eCParameterSpec.getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCParameterSpec);
    }

    public BCDSTU4145PublicKey(ECPublicKey eCPublicKey) {
        this.algorithm = eCPublicKey.getAlgorithm();
        this.ecSpec = eCPublicKey.getParams();
        this.q = EC5Util.convertPoint((ECParameterSpec)this.ecSpec, (ECPoint)eCPublicKey.getW(), (boolean)false);
    }

    public BCDSTU4145PublicKey(java.security.spec.ECPublicKeySpec eCPublicKeySpec) {
        this.ecSpec = eCPublicKeySpec.getParams();
        this.q = EC5Util.convertPoint((ECParameterSpec)this.ecSpec, (ECPoint)eCPublicKeySpec.getW(), (boolean)false);
    }

    BCDSTU4145PublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }

    public BCDSTU4145PublicKey(BCDSTU4145PublicKey bCDSTU4145PublicKey) {
        this.q = bCDSTU4145PublicKey.q;
        this.ecSpec = bCDSTU4145PublicKey.ecSpec;
        this.withCompression = bCDSTU4145PublicKey.withCompression;
        this.dstuParams = bCDSTU4145PublicKey.dstuParams;
    }

    public BCDSTU4145PublicKey(ECPublicKeySpec eCPublicKeySpec) {
        this.q = eCPublicKeySpec.getQ();
        if (eCPublicKeySpec.getParams() != null) {
            this.ecSpec = EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCPublicKeySpec.getParams().getCurve(), (byte[])eCPublicKeySpec.getParams().getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCPublicKeySpec.getParams());
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
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void populateFromPubKeyInfo(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ECNamedCurveParameterSpec eCNamedCurveParameterSpec;
        ASN1OctetString aSN1OctetString;
        DERBitString dERBitString = subjectPublicKeyInfo.getPublicKeyData();
        this.algorithm = "DSTU4145";
        try {
            aSN1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(dERBitString.getBytes());
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("error recovering public key");
        }
        byte[] arrby = aSN1OctetString.getOctets();
        if (subjectPublicKeyInfo.getAlgorithm().getAlgorithm().equals(UAObjectIdentifiers.dstu4145le)) {
            this.reverseBytes(arrby);
        }
        this.dstuParams = DSTU4145Params.getInstance((ASN1Sequence)subjectPublicKeyInfo.getAlgorithm().getParameters());
        if (this.dstuParams.isNamedCurve()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = this.dstuParams.getNamedCurve();
            ECDomainParameters eCDomainParameters = DSTU4145NamedCurves.getByOID(aSN1ObjectIdentifier);
            eCNamedCurveParameterSpec = new ECNamedCurveParameterSpec(aSN1ObjectIdentifier.getId(), eCDomainParameters.getCurve(), eCDomainParameters.getG(), eCDomainParameters.getN(), eCDomainParameters.getH(), eCDomainParameters.getSeed());
        } else {
            DSTU4145ECBinary dSTU4145ECBinary = this.dstuParams.getECBinary();
            byte[] arrby2 = dSTU4145ECBinary.getB();
            if (subjectPublicKeyInfo.getAlgorithm().getAlgorithm().equals(UAObjectIdentifiers.dstu4145le)) {
                this.reverseBytes(arrby2);
            }
            DSTU4145BinaryField dSTU4145BinaryField = dSTU4145ECBinary.getField();
            ECCurve.F2m f2m = new ECCurve.F2m(dSTU4145BinaryField.getM(), dSTU4145BinaryField.getK1(), dSTU4145BinaryField.getK2(), dSTU4145BinaryField.getK3(), dSTU4145ECBinary.getA(), new BigInteger(1, arrby2));
            byte[] arrby3 = dSTU4145ECBinary.getG();
            if (subjectPublicKeyInfo.getAlgorithm().getAlgorithm().equals(UAObjectIdentifiers.dstu4145le)) {
                this.reverseBytes(arrby3);
            }
            eCNamedCurveParameterSpec = new org.bouncycastle.jce.spec.ECParameterSpec((ECCurve)f2m, DSTU4145PointEncoder.decodePoint((ECCurve)f2m, arrby3), dSTU4145ECBinary.getN());
        }
        ECCurve eCCurve = eCNamedCurveParameterSpec.getCurve();
        EllipticCurve ellipticCurve = EC5Util.convertCurve((ECCurve)eCCurve, (byte[])eCNamedCurveParameterSpec.getSeed());
        this.q = DSTU4145PointEncoder.decodePoint(eCCurve, arrby);
        if (this.dstuParams.isNamedCurve()) {
            this.ecSpec = new ECNamedCurveSpec(this.dstuParams.getNamedCurve().getId(), ellipticCurve, new ECPoint(eCNamedCurveParameterSpec.getG().getAffineXCoord().toBigInteger(), eCNamedCurveParameterSpec.getG().getAffineYCoord().toBigInteger()), eCNamedCurveParameterSpec.getN(), eCNamedCurveParameterSpec.getH());
            return;
        }
        this.ecSpec = new ECParameterSpec(ellipticCurve, new ECPoint(eCNamedCurveParameterSpec.getG().getAffineXCoord().toBigInteger(), eCNamedCurveParameterSpec.getG().getAffineYCoord().toBigInteger()), eCNamedCurveParameterSpec.getN(), eCNamedCurveParameterSpec.getH().intValue());
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
    }

    private void reverseBytes(byte[] arrby) {
        for (int i2 = 0; i2 < arrby.length / 2; ++i2) {
            byte by = arrby[i2];
            arrby[i2] = arrby[-1 + arrby.length - i2];
            arrby[-1 + arrby.length - i2] = by;
        }
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
        return BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof BCDSTU4145PublicKey)) break block2;
                BCDSTU4145PublicKey bCDSTU4145PublicKey = (BCDSTU4145PublicKey)object;
                if (this.engineGetQ().equals(bCDSTU4145PublicKey.engineGetQ()) && this.engineGetSpec().equals((Object)bCDSTU4145PublicKey.engineGetSpec())) break block3;
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
        if (this.dstuParams != null) {
            DSTU4145Params dSTU4145Params = this.dstuParams;
        } else if (this.ecSpec instanceof ECNamedCurveSpec) {
            DSTU4145Params dSTU4145Params = new DSTU4145Params(new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName()));
        } else {
            ECCurve eCCurve = EC5Util.convertCurve((EllipticCurve)this.ecSpec.getCurve());
            X962Parameters x962Parameters = new X962Parameters(new X9ECParameters(eCCurve, EC5Util.convertPoint((ECCurve)eCCurve, (ECPoint)this.ecSpec.getGenerator(), (boolean)this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf((long)this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        byte[] arrby = DSTU4145PointEncoder.encodePoint(this.q);
        try {
            void var2_2;
            subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(UAObjectIdentifiers.dstu4145be, (ASN1Encodable)var2_2), new DEROctetString(arrby));
        }
        catch (IOException iOException) {
            return null;
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo((SubjectPublicKeyInfo)subjectPublicKeyInfo);
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

    public byte[] getSbox() {
        if (this.dstuParams != null) {
            return this.dstuParams.getDKE();
        }
        return DSTU4145Params.getDefaultDKE();
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

