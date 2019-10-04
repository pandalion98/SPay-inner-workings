/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.interfaces.RSAPrivateCrtKey
 *  java.security.interfaces.RSAPrivateKey
 *  java.security.interfaces.RSAPublicKey
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class RSAUtil {
    public static final ASN1ObjectIdentifier[] rsaOids;

    static {
        ASN1ObjectIdentifier[] arraSN1ObjectIdentifier = new ASN1ObjectIdentifier[]{PKCSObjectIdentifiers.rsaEncryption, X509ObjectIdentifiers.id_ea_rsa, PKCSObjectIdentifiers.id_RSAES_OAEP, PKCSObjectIdentifiers.id_RSASSA_PSS};
        rsaOids = arraSN1ObjectIdentifier;
    }

    static RSAKeyParameters generatePrivateKeyParameter(RSAPrivateKey rSAPrivateKey) {
        if (rSAPrivateKey instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey)rSAPrivateKey;
            return new RSAPrivateCrtKeyParameters(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient());
        }
        return new RSAKeyParameters(true, rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent());
    }

    static RSAKeyParameters generatePublicKeyParameter(RSAPublicKey rSAPublicKey) {
        return new RSAKeyParameters(false, rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
    }

    public static boolean isRsaOid(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        int n = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n2 = rsaOids.length;
                    bl = false;
                    if (n == n2) break block3;
                    if (!aSN1ObjectIdentifier.equals(rsaOids[n])) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n;
        } while (true);
    }
}

