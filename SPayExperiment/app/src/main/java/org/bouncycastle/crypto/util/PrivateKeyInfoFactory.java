/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.util;

import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECNamedDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class PrivateKeyInfoFactory {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static PrivateKeyInfo createPrivateKeyInfo(AsymmetricKeyParameter asymmetricKeyParameter) {
        int n2;
        X962Parameters x962Parameters;
        if (asymmetricKeyParameter instanceof RSAKeyParameters) {
            RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)asymmetricKeyParameter;
            return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new RSAPrivateKey(rSAPrivateCrtKeyParameters.getModulus(), rSAPrivateCrtKeyParameters.getPublicExponent(), rSAPrivateCrtKeyParameters.getExponent(), rSAPrivateCrtKeyParameters.getP(), rSAPrivateCrtKeyParameters.getQ(), rSAPrivateCrtKeyParameters.getDP(), rSAPrivateCrtKeyParameters.getDQ(), rSAPrivateCrtKeyParameters.getQInv()));
        }
        if (asymmetricKeyParameter instanceof DSAPrivateKeyParameters) {
            DSAPrivateKeyParameters dSAPrivateKeyParameters = (DSAPrivateKeyParameters)asymmetricKeyParameter;
            DSAParameters dSAParameters = dSAPrivateKeyParameters.getParameters();
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(dSAParameters.getP(), dSAParameters.getQ(), dSAParameters.getG())), new ASN1Integer(dSAPrivateKeyParameters.getX()));
        }
        if (!(asymmetricKeyParameter instanceof ECPrivateKeyParameters)) throw new IOException("key parameters not recognised.");
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)asymmetricKeyParameter;
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        if (eCDomainParameters == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
            n2 = eCPrivateKeyParameters.getD().bitLength();
            do {
                return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), new ECPrivateKey(n2, eCPrivateKeyParameters.getD(), (ASN1Encodable)x962Parameters));
                break;
            } while (true);
        }
        if (eCDomainParameters instanceof ECNamedDomainParameters) {
            x962Parameters = new X962Parameters(((ECNamedDomainParameters)eCDomainParameters).getName());
            n2 = eCDomainParameters.getCurve().getOrder().bitLength();
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), new ECPrivateKey(n2, eCPrivateKeyParameters.getD(), (ASN1Encodable)x962Parameters));
        }
        x962Parameters = new X962Parameters(new X9ECParameters(eCDomainParameters.getCurve(), eCDomainParameters.getG(), eCDomainParameters.getN(), eCDomainParameters.getH(), eCDomainParameters.getSeed()));
        n2 = eCDomainParameters.getCurve().getOrder().bitLength();
        return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), new ECPrivateKey(n2, eCPrivateKeyParameters.getD(), (ASN1Encodable)x962Parameters));
    }
}

