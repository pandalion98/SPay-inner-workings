/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class McElieceKeysToParams {
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        if (privateKey instanceof BCMcEliecePrivateKey) {
            BCMcEliecePrivateKey bCMcEliecePrivateKey = (BCMcEliecePrivateKey)privateKey;
            return new McEliecePrivateKeyParameters(bCMcEliecePrivateKey.getOIDString(), bCMcEliecePrivateKey.getN(), bCMcEliecePrivateKey.getK(), bCMcEliecePrivateKey.getField(), bCMcEliecePrivateKey.getGoppaPoly(), bCMcEliecePrivateKey.getSInv(), bCMcEliecePrivateKey.getP1(), bCMcEliecePrivateKey.getP2(), bCMcEliecePrivateKey.getH(), bCMcEliecePrivateKey.getQInv(), bCMcEliecePrivateKey.getMcElieceParameters());
        }
        throw new InvalidKeyException("can't identify McEliece private key.");
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof BCMcEliecePublicKey) {
            BCMcEliecePublicKey bCMcEliecePublicKey = (BCMcEliecePublicKey)publicKey;
            return new McEliecePublicKeyParameters(bCMcEliecePublicKey.getOIDString(), bCMcEliecePublicKey.getN(), bCMcEliecePublicKey.getT(), bCMcEliecePublicKey.getG(), bCMcEliecePublicKey.getMcElieceParameters());
        }
        throw new InvalidKeyException("can't identify McEliece public key: " + publicKey.getClass().getName());
    }
}

