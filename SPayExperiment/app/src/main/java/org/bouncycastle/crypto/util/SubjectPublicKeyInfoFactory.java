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
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECNamedDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class SubjectPublicKeyInfoFactory {
    /*
     * Enabled aggressive block sorting
     */
    public static SubjectPublicKeyInfo createSubjectPublicKeyInfo(AsymmetricKeyParameter asymmetricKeyParameter) {
        if (asymmetricKeyParameter instanceof RSAKeyParameters) {
            RSAKeyParameters rSAKeyParameters = (RSAKeyParameters)asymmetricKeyParameter;
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new RSAPublicKey(rSAKeyParameters.getModulus(), rSAKeyParameters.getExponent()));
        }
        if (asymmetricKeyParameter instanceof DSAPublicKeyParameters) {
            DSAPublicKeyParameters dSAPublicKeyParameters = (DSAPublicKeyParameters)asymmetricKeyParameter;
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa), new ASN1Integer(dSAPublicKeyParameters.getY()));
        }
        if (!(asymmetricKeyParameter instanceof ECPublicKeyParameters)) {
            throw new IOException("key parameters not recognised.");
        }
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters)asymmetricKeyParameter;
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        X962Parameters x962Parameters = eCDomainParameters == null ? new X962Parameters(DERNull.INSTANCE) : (eCDomainParameters instanceof ECNamedDomainParameters ? new X962Parameters(((ECNamedDomainParameters)eCDomainParameters).getName()) : new X962Parameters(new X9ECParameters(eCDomainParameters.getCurve(), eCDomainParameters.getG(), eCDomainParameters.getN(), eCDomainParameters.getH(), eCDomainParameters.getSeed())));
        ASN1OctetString aSN1OctetString = (ASN1OctetString)new X9ECPoint(eCPublicKeyParameters.getQ()).toASN1Primitive();
        return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), aSN1OctetString.getOctets());
    }
}

