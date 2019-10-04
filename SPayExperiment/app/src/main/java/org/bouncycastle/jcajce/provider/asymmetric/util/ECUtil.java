/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.interfaces.ECPrivateKey
 *  java.security.interfaces.ECPublicKey
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECCurve;

public class ECUtil {
    static int[] convertMidTerms(int[] arrn) {
        int[] arrn2 = new int[3];
        if (arrn.length == 1) {
            arrn2[0] = arrn[0];
            return arrn2;
        }
        if (arrn.length != 3) {
            throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
        }
        if (arrn[0] < arrn[1] && arrn[0] < arrn[2]) {
            arrn2[0] = arrn[0];
            if (arrn[1] < arrn[2]) {
                arrn2[1] = arrn[1];
                arrn2[2] = arrn[2];
                return arrn2;
            }
            arrn2[1] = arrn[2];
            arrn2[2] = arrn[1];
            return arrn2;
        }
        if (arrn[1] < arrn[2]) {
            arrn2[0] = arrn[1];
            if (arrn[0] < arrn[2]) {
                arrn2[1] = arrn[0];
                arrn2[2] = arrn[2];
                return arrn2;
            }
            arrn2[1] = arrn[2];
            arrn2[2] = arrn[0];
            return arrn2;
        }
        arrn2[0] = arrn[2];
        if (arrn[0] < arrn[1]) {
            arrn2[1] = arrn[0];
            arrn2[2] = arrn[1];
            return arrn2;
        }
        arrn2[1] = arrn[1];
        arrn2[2] = arrn[0];
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec;
        ECPrivateKey eCPrivateKey;
        org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec2;
        if (privateKey instanceof ECPrivateKey) {
            eCPrivateKey = (ECPrivateKey)privateKey;
            eCParameterSpec = eCPrivateKey.getParameters();
            if (eCParameterSpec == null) {
                eCParameterSpec2 = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
                return new ECPrivateKeyParameters(eCPrivateKey.getD(), new ECDomainParameters(eCParameterSpec2.getCurve(), eCParameterSpec2.getG(), eCParameterSpec2.getN(), eCParameterSpec2.getH(), eCParameterSpec2.getSeed()));
            }
        } else {
            if (privateKey instanceof java.security.interfaces.ECPrivateKey) {
                java.security.interfaces.ECPrivateKey eCPrivateKey2 = (java.security.interfaces.ECPrivateKey)privateKey;
                org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec3 = EC5Util.convertSpec(eCPrivateKey2.getParams(), false);
                return new ECPrivateKeyParameters(eCPrivateKey2.getS(), new ECDomainParameters(eCParameterSpec3.getCurve(), eCParameterSpec3.getG(), eCParameterSpec3.getN(), eCParameterSpec3.getH(), eCParameterSpec3.getSeed()));
            }
            try {
                byte[] arrby = privateKey.getEncoded();
                if (arrby == null) {
                    throw new InvalidKeyException("no encoding for EC private key");
                }
                PrivateKey privateKey2 = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(arrby));
                if (!(privateKey2 instanceof java.security.interfaces.ECPrivateKey)) throw new InvalidKeyException("can't identify EC private key.");
                return ECUtil.generatePrivateKeyParameter(privateKey2);
            }
            catch (Exception exception) {
                throw new InvalidKeyException("cannot identify EC private key: " + exception.toString());
            }
        }
        eCParameterSpec2 = eCParameterSpec;
        return new ECPrivateKeyParameters(eCPrivateKey.getD(), new ECDomainParameters(eCParameterSpec2.getCurve(), eCParameterSpec2.getG(), eCParameterSpec2.getN(), eCParameterSpec2.getH(), eCParameterSpec2.getSeed()));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof org.bouncycastle.jce.interfaces.ECPublicKey) {
            org.bouncycastle.jce.interfaces.ECPublicKey eCPublicKey = (org.bouncycastle.jce.interfaces.ECPublicKey)publicKey;
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = eCPublicKey.getParameters();
            if (eCParameterSpec != null) return new ECPublicKeyParameters(eCPublicKey.getQ(), new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec2 = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new ECPublicKeyParameters(((BCECPublicKey)eCPublicKey).engineGetQ(), new ECDomainParameters(eCParameterSpec2.getCurve(), eCParameterSpec2.getG(), eCParameterSpec2.getN(), eCParameterSpec2.getH(), eCParameterSpec2.getSeed()));
        }
        if (publicKey instanceof ECPublicKey) {
            ECPublicKey eCPublicKey = (ECPublicKey)publicKey;
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = EC5Util.convertSpec(eCPublicKey.getParams(), false);
            return new ECPublicKeyParameters(EC5Util.convertPoint(eCPublicKey.getParams(), eCPublicKey.getW(), false), new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
        }
        try {
            byte[] arrby = publicKey.getEncoded();
            if (arrby == null) {
                throw new InvalidKeyException("no encoding for EC public key");
            }
            PublicKey publicKey2 = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(arrby));
            if (!(publicKey2 instanceof ECPublicKey)) throw new InvalidKeyException("cannot identify EC public key.");
            return ECUtil.generatePublicKeyParameter(publicKey2);
        }
        catch (Exception exception) {
            throw new InvalidKeyException("cannot identify EC public key: " + exception.toString());
        }
    }

    public static String getCurveName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        String string = X962NamedCurves.getName(aSN1ObjectIdentifier);
        if (string == null) {
            string = SECNamedCurves.getName(aSN1ObjectIdentifier);
            if (string == null) {
                string = NISTNamedCurves.getName(aSN1ObjectIdentifier);
            }
            if (string == null) {
                string = TeleTrusTNamedCurves.getName(aSN1ObjectIdentifier);
            }
            if (string == null) {
                string = ECGOST3410NamedCurves.getName(aSN1ObjectIdentifier);
            }
        }
        return string;
    }

    public static X9ECParameters getNamedCurveByOid(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        X9ECParameters x9ECParameters = CustomNamedCurves.getByOID(aSN1ObjectIdentifier);
        if (x9ECParameters == null) {
            x9ECParameters = X962NamedCurves.getByOID(aSN1ObjectIdentifier);
            if (x9ECParameters == null) {
                x9ECParameters = SECNamedCurves.getByOID(aSN1ObjectIdentifier);
            }
            if (x9ECParameters == null) {
                x9ECParameters = NISTNamedCurves.getByOID(aSN1ObjectIdentifier);
            }
            if (x9ECParameters == null) {
                x9ECParameters = TeleTrusTNamedCurves.getByOID(aSN1ObjectIdentifier);
            }
        }
        return x9ECParameters;
    }

    public static ASN1ObjectIdentifier getNamedCurveOid(String string) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = X962NamedCurves.getOID(string);
        if (aSN1ObjectIdentifier == null) {
            aSN1ObjectIdentifier = SECNamedCurves.getOID(string);
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = NISTNamedCurves.getOID(string);
            }
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = TeleTrusTNamedCurves.getOID(string);
            }
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = ECGOST3410NamedCurves.getOID(string);
            }
        }
        return aSN1ObjectIdentifier;
    }

    public static int getOrderBitLength(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger == null) {
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            if (eCParameterSpec == null) {
                return bigInteger2.bitLength();
            }
            return eCParameterSpec.getN().bitLength();
        }
        return bigInteger.bitLength();
    }
}

