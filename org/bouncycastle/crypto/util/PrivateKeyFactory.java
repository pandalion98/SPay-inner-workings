package org.bouncycastle.crypto.util;

import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECNamedDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class PrivateKeyFactory {
    public static AsymmetricKeyParameter createKey(InputStream inputStream) {
        return createKey(PrivateKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }

    public static AsymmetricKeyParameter createKey(PrivateKeyInfo privateKeyInfo) {
        AlgorithmIdentifier privateKeyAlgorithm = privateKeyInfo.getPrivateKeyAlgorithm();
        if (privateKeyAlgorithm.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            RSAPrivateKey instance = RSAPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
            return new RSAPrivateCrtKeyParameters(instance.getModulus(), instance.getPublicExponent(), instance.getPrivateExponent(), instance.getPrime1(), instance.getPrime2(), instance.getExponent1(), instance.getExponent2(), instance.getCoefficient());
        } else if (privateKeyAlgorithm.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            DHParameter instance2 = DHParameter.getInstance(privateKeyAlgorithm.getParameters());
            r0 = (ASN1Integer) privateKeyInfo.parsePrivateKey();
            BigInteger l = instance2.getL();
            return new DHPrivateKeyParameters(r0.getValue(), new DHParameters(instance2.getP(), instance2.getG(), null, l == null ? 0 : l.intValue()));
        } else if (privateKeyAlgorithm.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            ElGamalParameter instance3 = ElGamalParameter.getInstance(privateKeyAlgorithm.getParameters());
            return new ElGamalPrivateKeyParameters(((ASN1Integer) privateKeyInfo.parsePrivateKey()).getValue(), new ElGamalParameters(instance3.getP(), instance3.getG()));
        } else if (privateKeyAlgorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa)) {
            DSAParameters dSAParameters;
            r0 = (ASN1Integer) privateKeyInfo.parsePrivateKey();
            ASN1Encodable parameters = privateKeyAlgorithm.getParameters();
            if (parameters != null) {
                DSAParameter instance4 = DSAParameter.getInstance(parameters.toASN1Primitive());
                dSAParameters = new DSAParameters(instance4.getP(), instance4.getQ(), instance4.getG());
            } else {
                dSAParameters = null;
            }
            return new DSAPrivateKeyParameters(r0.getValue(), dSAParameters);
        } else if (privateKeyAlgorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            ECDomainParameters eCNamedDomainParameters;
            X962Parameters x962Parameters = new X962Parameters((ASN1Primitive) privateKeyAlgorithm.getParameters());
            if (x962Parameters.isNamedCurve()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) x962Parameters.getParameters();
                X9ECParameters byOID = CustomNamedCurves.getByOID(aSN1ObjectIdentifier);
                X9ECParameters byOID2 = byOID == null ? ECNamedCurveTable.getByOID(aSN1ObjectIdentifier) : byOID;
                eCNamedDomainParameters = new ECNamedDomainParameters(aSN1ObjectIdentifier, byOID2.getCurve(), byOID2.getG(), byOID2.getN(), byOID2.getH(), byOID2.getSeed());
            } else {
                X9ECParameters instance5 = X9ECParameters.getInstance(x962Parameters.getParameters());
                eCNamedDomainParameters = new ECDomainParameters(instance5.getCurve(), instance5.getG(), instance5.getN(), instance5.getH(), instance5.getSeed());
            }
            return new ECPrivateKeyParameters(ECPrivateKey.getInstance(privateKeyInfo.parsePrivateKey()).getKey(), eCNamedDomainParameters);
        } else {
            throw new RuntimeException("algorithm identifier in key not recognised");
        }
    }

    public static AsymmetricKeyParameter createKey(byte[] bArr) {
        return createKey(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(bArr)));
    }
}
