/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.interfaces.DSAParams
 *  java.security.interfaces.DSAPrivateKey
 *  java.security.interfaces.DSAPublicKey
 */
package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;

public class DSAUtil {
    public static final ASN1ObjectIdentifier[] dsaOids;

    static {
        ASN1ObjectIdentifier[] arraSN1ObjectIdentifier = new ASN1ObjectIdentifier[]{X9ObjectIdentifiers.id_dsa, OIWObjectIdentifiers.dsaWithSHA1};
        dsaOids = arraSN1ObjectIdentifier;
    }

    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        if (privateKey instanceof DSAPrivateKey) {
            DSAPrivateKey dSAPrivateKey = (DSAPrivateKey)privateKey;
            return new DSAPrivateKeyParameters(dSAPrivateKey.getX(), new DSAParameters(dSAPrivateKey.getParams().getP(), dSAPrivateKey.getParams().getQ(), dSAPrivateKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify DSA private key.");
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof DSAPublicKey) {
            DSAPublicKey dSAPublicKey = (DSAPublicKey)publicKey;
            return new DSAPublicKeyParameters(dSAPublicKey.getY(), new DSAParameters(dSAPublicKey.getParams().getP(), dSAPublicKey.getParams().getQ(), dSAPublicKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify DSA public key: " + publicKey.getClass().getName());
    }

    public static boolean isDsaOid(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = dsaOids.length;
                    bl = false;
                    if (n2 == n3) break block3;
                    if (!aSN1ObjectIdentifier.equals(dsaOids[n2])) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }
}

