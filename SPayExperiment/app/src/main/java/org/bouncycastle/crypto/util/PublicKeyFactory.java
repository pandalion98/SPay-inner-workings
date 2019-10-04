/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.util;

import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
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
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class PublicKeyFactory {
    public static AsymmetricKeyParameter createKey(InputStream inputStream) {
        return PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static AsymmetricKeyParameter createKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        X9ECParameters x9ECParameters;
        ECDomainParameters eCDomainParameters;
        AlgorithmIdentifier algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
        if (algorithmIdentifier.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption) || algorithmIdentifier.getAlgorithm().equals(X509ObjectIdentifiers.id_ea_rsa)) {
            RSAPublicKey rSAPublicKey = RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            return new RSAKeyParameters(false, rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
        }
        if (algorithmIdentifier.getAlgorithm().equals(X9ObjectIdentifiers.dhpublicnumber)) {
            BigInteger bigInteger = DHPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey()).getY().getValue();
            DHDomainParameters dHDomainParameters = DHDomainParameters.getInstance(algorithmIdentifier.getParameters());
            BigInteger bigInteger2 = dHDomainParameters.getP().getValue();
            BigInteger bigInteger3 = dHDomainParameters.getG().getValue();
            BigInteger bigInteger4 = dHDomainParameters.getQ().getValue();
            BigInteger bigInteger5 = dHDomainParameters.getJ() != null ? dHDomainParameters.getJ().getValue() : null;
            DHValidationParms dHValidationParms = dHDomainParameters.getValidationParms();
            DHValidationParameters dHValidationParameters = null;
            if (dHValidationParms != null) {
                dHValidationParameters = new DHValidationParameters(dHValidationParms.getSeed().getBytes(), dHValidationParms.getPgenCounter().getValue().intValue());
            }
            return new DHPublicKeyParameters(bigInteger, new DHParameters(bigInteger2, bigInteger3, bigInteger4, bigInteger5, dHValidationParameters));
        }
        if (algorithmIdentifier.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            DHParameter dHParameter = DHParameter.getInstance(algorithmIdentifier.getParameters());
            ASN1Integer aSN1Integer = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
            BigInteger bigInteger = dHParameter.getL();
            int n2 = 0;
            if (bigInteger != null) {
                n2 = bigInteger.intValue();
            }
            DHParameters dHParameters = new DHParameters(dHParameter.getP(), dHParameter.getG(), null, n2);
            return new DHPublicKeyParameters(aSN1Integer.getValue(), dHParameters);
        }
        if (algorithmIdentifier.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            ElGamalParameter elGamalParameter = ElGamalParameter.getInstance(algorithmIdentifier.getParameters());
            return new ElGamalPublicKeyParameters(((ASN1Integer)subjectPublicKeyInfo.parsePublicKey()).getValue(), new ElGamalParameters(elGamalParameter.getP(), elGamalParameter.getG()));
        }
        if (algorithmIdentifier.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa) || algorithmIdentifier.getAlgorithm().equals(OIWObjectIdentifiers.dsaWithSHA1)) {
            ASN1Integer aSN1Integer = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
            ASN1Encodable aSN1Encodable = algorithmIdentifier.getParameters();
            DSAParameters dSAParameters = null;
            if (aSN1Encodable != null) {
                DSAParameter dSAParameter = DSAParameter.getInstance(aSN1Encodable.toASN1Primitive());
                dSAParameters = new DSAParameters(dSAParameter.getP(), dSAParameter.getQ(), dSAParameter.getG());
            }
            return new DSAPublicKeyParameters(aSN1Integer.getValue(), dSAParameters);
        }
        if (!algorithmIdentifier.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            throw new RuntimeException("algorithm identifier in key not recognised");
        }
        X962Parameters x962Parameters = X962Parameters.getInstance(algorithmIdentifier.getParameters());
        if (x962Parameters.isNamedCurve()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)x962Parameters.getParameters();
            X9ECParameters x9ECParameters2 = CustomNamedCurves.getByOID(aSN1ObjectIdentifier);
            x9ECParameters = x9ECParameters2 == null ? ECNamedCurveTable.getByOID(aSN1ObjectIdentifier) : x9ECParameters2;
            eCDomainParameters = new ECNamedDomainParameters(aSN1ObjectIdentifier, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
        } else {
            x9ECParameters = X9ECParameters.getInstance(x962Parameters.getParameters());
            eCDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
        }
        DEROctetString dEROctetString = new DEROctetString(subjectPublicKeyInfo.getPublicKeyData().getBytes());
        return new ECPublicKeyParameters(new X9ECPoint(x9ECParameters.getCurve(), dEROctetString).getPoint(), eCDomainParameters);
    }

    public static AsymmetricKeyParameter createKey(byte[] arrby) {
        return PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrby)));
    }
}

