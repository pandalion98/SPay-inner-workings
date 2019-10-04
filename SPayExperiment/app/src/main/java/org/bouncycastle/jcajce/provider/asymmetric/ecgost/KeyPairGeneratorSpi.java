/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.ECGenParameterSpec
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 *  java.security.spec.EllipticCurve
 */
package org.bouncycastle.jcajce.provider.asymmetric.ecgost;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ecgost.BCECGOST3410PrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ecgost.BCECGOST3410PublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;

public class KeyPairGeneratorSpi
extends KeyPairGenerator {
    String algorithm = "ECGOST3410";
    Object ecParams = null;
    ECKeyPairGenerator engine = new ECKeyPairGenerator();
    boolean initialised = false;
    ECKeyGenerationParameters param;
    SecureRandom random = null;
    int strength = 239;

    public KeyPairGeneratorSpi() {
        super("ECGOST3410");
    }

    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            throw new IllegalStateException("EC Key Pair Generator not initialised");
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        if (this.ecParams instanceof org.bouncycastle.jce.spec.ECParameterSpec) {
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = (org.bouncycastle.jce.spec.ECParameterSpec)this.ecParams;
            BCECGOST3410PublicKey bCECGOST3410PublicKey = new BCECGOST3410PublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec);
            return new KeyPair((PublicKey)bCECGOST3410PublicKey, (PrivateKey)new BCECGOST3410PrivateKey(this.algorithm, eCPrivateKeyParameters, bCECGOST3410PublicKey, eCParameterSpec));
        }
        if (this.ecParams == null) {
            return new KeyPair((PublicKey)new BCECGOST3410PublicKey(this.algorithm, eCPublicKeyParameters), (PrivateKey)new BCECGOST3410PrivateKey(this.algorithm, eCPrivateKeyParameters));
        }
        ECParameterSpec eCParameterSpec = (ECParameterSpec)this.ecParams;
        BCECGOST3410PublicKey bCECGOST3410PublicKey = new BCECGOST3410PublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec);
        return new KeyPair((PublicKey)bCECGOST3410PublicKey, (PrivateKey)new BCECGOST3410PrivateKey(this.algorithm, eCPrivateKeyParameters, bCECGOST3410PublicKey, eCParameterSpec));
    }

    public void initialize(int n, SecureRandom secureRandom) {
        this.strength = n;
        this.random = secureRandom;
        if (this.ecParams != null) {
            try {
                this.initialize((AlgorithmParameterSpec)((ECGenParameterSpec)this.ecParams), secureRandom);
                return;
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                throw new InvalidParameterException("key size not configurable.");
            }
        }
        throw new InvalidParameterException("unknown key size.");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (algorithmParameterSpec instanceof org.bouncycastle.jce.spec.ECParameterSpec) {
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = (org.bouncycastle.jce.spec.ECParameterSpec)algorithmParameterSpec;
            this.ecParams = algorithmParameterSpec;
            this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN()), secureRandom);
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        if (algorithmParameterSpec instanceof ECParameterSpec) {
            ECParameterSpec eCParameterSpec = (ECParameterSpec)algorithmParameterSpec;
            this.ecParams = algorithmParameterSpec;
            ECCurve eCCurve = EC5Util.convertCurve(eCParameterSpec.getCurve());
            this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCCurve, EC5Util.convertPoint(eCCurve, eCParameterSpec.getGenerator(), false), eCParameterSpec.getOrder(), BigInteger.valueOf((long)eCParameterSpec.getCofactor())), secureRandom);
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        if (algorithmParameterSpec instanceof ECGenParameterSpec || algorithmParameterSpec instanceof ECNamedCurveGenParameterSpec) {
            String string = algorithmParameterSpec instanceof ECGenParameterSpec ? ((ECGenParameterSpec)algorithmParameterSpec).getName() : ((ECNamedCurveGenParameterSpec)algorithmParameterSpec).getName();
            ECDomainParameters eCDomainParameters = ECGOST3410NamedCurves.getByName(string);
            if (eCDomainParameters == null) {
                throw new InvalidAlgorithmParameterException("unknown curve name: " + string);
            }
            this.ecParams = new ECNamedCurveSpec(string, eCDomainParameters.getCurve(), eCDomainParameters.getG(), eCDomainParameters.getN(), eCDomainParameters.getH(), eCDomainParameters.getSeed());
            ECParameterSpec eCParameterSpec = (ECParameterSpec)this.ecParams;
            ECCurve eCCurve = EC5Util.convertCurve(eCParameterSpec.getCurve());
            this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCCurve, EC5Util.convertPoint(eCCurve, eCParameterSpec.getGenerator(), false), eCParameterSpec.getOrder(), BigInteger.valueOf((long)eCParameterSpec.getCofactor())), secureRandom);
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        if (algorithmParameterSpec == null && BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa() != null) {
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            this.ecParams = algorithmParameterSpec;
            this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN()), secureRandom);
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        if (algorithmParameterSpec == null && BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa() == null) {
            throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
        }
        throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec: " + algorithmParameterSpec.getClass().getName());
    }
}

