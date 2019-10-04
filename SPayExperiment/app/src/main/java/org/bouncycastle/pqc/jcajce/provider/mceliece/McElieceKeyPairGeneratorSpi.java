/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyPairGenerator;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyPairGenerator;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PrivateKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PublicKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2ParameterSpec;

public abstract class McElieceKeyPairGeneratorSpi
extends KeyPairGenerator {
    public McElieceKeyPairGeneratorSpi(String string) {
        super(string);
    }

    public static class McEliece
    extends McElieceKeyPairGeneratorSpi {
        McElieceKeyPairGenerator kpg;

        public McEliece() {
            super("McEliece");
        }

        public KeyPair generateKeyPair() {
            AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.kpg.generateKeyPair();
            McEliecePrivateKeyParameters mcEliecePrivateKeyParameters = (McEliecePrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
            return new KeyPair((PublicKey)new BCMcEliecePublicKey((McEliecePublicKeyParameters)asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BCMcEliecePrivateKey(mcEliecePrivateKeyParameters));
        }

        public void initialize(int n, SecureRandom secureRandom) {
            ECCKeyGenParameterSpec eCCKeyGenParameterSpec = new ECCKeyGenParameterSpec();
            try {
                this.initialize(eCCKeyGenParameterSpec);
                return;
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                return;
            }
        }

        public void initialize(AlgorithmParameterSpec algorithmParameterSpec) {
            this.kpg = new McElieceKeyPairGenerator();
            super.initialize(algorithmParameterSpec);
            ECCKeyGenParameterSpec eCCKeyGenParameterSpec = (ECCKeyGenParameterSpec)algorithmParameterSpec;
            McElieceKeyGenerationParameters mcElieceKeyGenerationParameters = new McElieceKeyGenerationParameters(new SecureRandom(), new McElieceParameters(eCCKeyGenParameterSpec.getM(), eCCKeyGenParameterSpec.getT()));
            this.kpg.init(mcElieceKeyGenerationParameters);
        }
    }

    public static class McElieceCCA2
    extends McElieceKeyPairGeneratorSpi {
        McElieceCCA2KeyPairGenerator kpg;

        public McElieceCCA2() {
            super("McElieceCCA-2");
        }

        public McElieceCCA2(String string) {
            super(string);
        }

        public KeyPair generateKeyPair() {
            AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.kpg.generateKeyPair();
            McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters = (McElieceCCA2PrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
            return new KeyPair((PublicKey)new BCMcElieceCCA2PublicKey((McElieceCCA2PublicKeyParameters)asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BCMcElieceCCA2PrivateKey(mcElieceCCA2PrivateKeyParameters));
        }

        public void initialize(int n, SecureRandom secureRandom) {
            McElieceCCA2ParameterSpec mcElieceCCA2ParameterSpec = new McElieceCCA2ParameterSpec();
            try {
                this.initialize(mcElieceCCA2ParameterSpec);
                return;
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                return;
            }
        }

        public void initialize(AlgorithmParameterSpec algorithmParameterSpec) {
            this.kpg = new McElieceCCA2KeyPairGenerator();
            super.initialize(algorithmParameterSpec);
            ECCKeyGenParameterSpec eCCKeyGenParameterSpec = (ECCKeyGenParameterSpec)algorithmParameterSpec;
            McElieceCCA2KeyGenerationParameters mcElieceCCA2KeyGenerationParameters = new McElieceCCA2KeyGenerationParameters(new SecureRandom(), new McElieceCCA2Parameters(eCCKeyGenParameterSpec.getM(), eCCKeyGenParameterSpec.getT()));
            this.kpg.init(mcElieceCCA2KeyGenerationParameters);
        }
    }

}

