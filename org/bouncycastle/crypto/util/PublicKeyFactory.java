package org.bouncycastle.crypto.util;

import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.x9.DHDomainParameters;
import org.bouncycastle.asn1.x9.DHPublicKey;
import org.bouncycastle.asn1.x9.DHValidationParms;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.DHValidationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECNamedDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class PublicKeyFactory {
    public static AsymmetricKeyParameter createKey(InputStream inputStream) {
        return createKey(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }

    public static AsymmetricKeyParameter createKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        int i = 0;
        DSAParameters dSAParameters = null;
        AlgorithmIdentifier algorithm = subjectPublicKeyInfo.getAlgorithm();
        if (algorithm.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption) || algorithm.getAlgorithm().equals(X509ObjectIdentifiers.id_ea_rsa)) {
            RSAPublicKey instance = RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            return new RSAKeyParameters(false, instance.getModulus(), instance.getPublicExponent());
        } else if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.dhpublicnumber)) {
            DHValidationParameters dHValidationParameters;
            BigInteger value = DHPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey()).getY().getValue();
            DHDomainParameters instance2 = DHDomainParameters.getInstance(algorithm.getParameters());
            BigInteger value2 = instance2.getP().getValue();
            BigInteger value3 = instance2.getG().getValue();
            r3 = instance2.getQ().getValue();
            BigInteger value4 = instance2.getJ() != null ? instance2.getJ().getValue() : null;
            DHValidationParms validationParms = instance2.getValidationParms();
            if (validationParms != null) {
                dHValidationParameters = new DHValidationParameters(validationParms.getSeed().getBytes(), validationParms.getPgenCounter().getValue().intValue());
            }
            return new DHPublicKeyParameters(value, new DHParameters(value2, value3, r3, value4, dHValidationParameters));
        } else if (algorithm.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            DHParameter instance3 = DHParameter.getInstance(algorithm.getParameters());
            r0 = (ASN1Integer) subjectPublicKeyInfo.parsePublicKey();
            r3 = instance3.getL();
            if (r3 != null) {
                i = r3.intValue();
            }
            return new DHPublicKeyParameters(r0.getValue(), new DHParameters(instance3.getP(), instance3.getG(), null, i));
        } else if (algorithm.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            ElGamalParameter instance4 = ElGamalParameter.getInstance(algorithm.getParameters());
            return new ElGamalPublicKeyParameters(((ASN1Integer) subjectPublicKeyInfo.parsePublicKey()).getValue(), new ElGamalParameters(instance4.getP(), instance4.getG()));
        } else if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa) || algorithm.getAlgorithm().equals(OIWObjectIdentifiers.dsaWithSHA1)) {
            r0 = (ASN1Integer) subjectPublicKeyInfo.parsePublicKey();
            ASN1Encodable parameters = algorithm.getParameters();
            if (parameters != null) {
                DSAParameter instance5 = DSAParameter.getInstance(parameters.toASN1Primitive());
                dSAParameters = new DSAParameters(instance5.getP(), instance5.getQ(), instance5.getG());
            }
            return new DSAPublicKeyParameters(r0.getValue(), dSAParameters);
        } else if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            X9ECParameters byOID;
            ECDomainParameters eCNamedDomainParameters;
            X962Parameters instance6 = X962Parameters.getInstance(algorithm.getParameters());
            if (instance6.isNamedCurve()) {
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) instance6.getParameters();
                X9ECParameters byOID2 = CustomNamedCurves.getByOID(aSN1ObjectIdentifier);
                byOID = byOID2 == null ? ECNamedCurveTable.getByOID(aSN1ObjectIdentifier) : byOID2;
                eCNamedDomainParameters = new ECNamedDomainParameters(aSN1ObjectIdentifier, byOID.getCurve(), byOID.getG(), byOID.getN(), byOID.getH(), byOID.getSeed());
            } else {
                byOID = X9ECParameters.getInstance(instance6.getParameters());
                eCNamedDomainParameters = new ECDomainParameters(byOID.getCurve(), byOID.getG(), byOID.getN(), byOID.getH(), byOID.getSeed());
            }
            return new ECPublicKeyParameters(new X9ECPoint(byOID.getCurve(), new DEROctetString(subjectPublicKeyInfo.getPublicKeyData().getBytes())).getPoint(), eCNamedDomainParameters);
        } else {
            throw new RuntimeException("algorithm identifier in key not recognised");
        }
    }

    public static AsymmetricKeyParameter createKey(byte[] bArr) {
        return createKey(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(bArr)));
    }
}
