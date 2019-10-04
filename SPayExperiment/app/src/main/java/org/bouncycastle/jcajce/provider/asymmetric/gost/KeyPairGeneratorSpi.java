/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.gost;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.GOST3410KeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.GOST3410KeyGenerationParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.gost.BCGOST3410PrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.gost.BCGOST3410PublicKey;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public class KeyPairGeneratorSpi
extends KeyPairGenerator {
    GOST3410KeyPairGenerator engine = new GOST3410KeyPairGenerator();
    GOST3410ParameterSpec gost3410Params;
    boolean initialised = false;
    GOST3410KeyGenerationParameters param;
    SecureRandom random = null;
    int strength = 1024;

    public KeyPairGeneratorSpi() {
        super("GOST3410");
    }

    private void init(GOST3410ParameterSpec gOST3410ParameterSpec, SecureRandom secureRandom) {
        GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = gOST3410ParameterSpec.getPublicKeyParameters();
        this.param = new GOST3410KeyGenerationParameters(secureRandom, new GOST3410Parameters(gOST3410PublicKeyParameterSetSpec.getP(), gOST3410PublicKeyParameterSetSpec.getQ(), gOST3410PublicKeyParameterSetSpec.getA()));
        this.engine.init(this.param);
        this.initialised = true;
        this.gost3410Params = gOST3410ParameterSpec;
    }

    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            this.init(new GOST3410ParameterSpec(CryptoProObjectIdentifiers.gostR3410_94_CryptoPro_A.getId()), new SecureRandom());
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        GOST3410PublicKeyParameters gOST3410PublicKeyParameters = (GOST3410PublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        GOST3410PrivateKeyParameters gOST3410PrivateKeyParameters = (GOST3410PrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair((PublicKey)new BCGOST3410PublicKey(gOST3410PublicKeyParameters, this.gost3410Params), (PrivateKey)new BCGOST3410PrivateKey(gOST3410PrivateKeyParameters, this.gost3410Params));
    }

    public void initialize(int n, SecureRandom secureRandom) {
        this.strength = n;
        this.random = secureRandom;
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof GOST3410ParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a GOST3410ParameterSpec");
        }
        this.init((GOST3410ParameterSpec)algorithmParameterSpec, secureRandom);
    }
}

