/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
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
 *  java.util.Hashtable
 *  org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util
 *  org.bouncycastle.jcajce.provider.config.ProviderConfiguration
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec
 *  org.bouncycastle.jce.spec.ECNamedCurveSpec
 *  org.bouncycastle.jce.spec.ECParameterSpec
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.jcajce.provider.asymmetric.ec;

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
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Integers;

public abstract class KeyPairGeneratorSpi
extends KeyPairGenerator {
    public KeyPairGeneratorSpi(String string) {
        super(string);
    }

    public static class EC
    extends KeyPairGeneratorSpi {
        private static Hashtable ecParameters = new Hashtable();
        String algorithm;
        int certainty = 50;
        ProviderConfiguration configuration;
        Object ecParams = null;
        ECKeyPairGenerator engine = new ECKeyPairGenerator();
        boolean initialised = false;
        ECKeyGenerationParameters param;
        SecureRandom random = new SecureRandom();
        int strength = 239;

        static {
            ecParameters.put((Object)Integers.valueOf((int)192), (Object)new ECGenParameterSpec("prime192v1"));
            ecParameters.put((Object)Integers.valueOf((int)239), (Object)new ECGenParameterSpec("prime239v1"));
            ecParameters.put((Object)Integers.valueOf((int)256), (Object)new ECGenParameterSpec("prime256v1"));
            ecParameters.put((Object)Integers.valueOf((int)224), (Object)new ECGenParameterSpec("P-224"));
            ecParameters.put((Object)Integers.valueOf((int)384), (Object)new ECGenParameterSpec("P-384"));
            ecParameters.put((Object)Integers.valueOf((int)521), (Object)new ECGenParameterSpec("P-521"));
        }

        public EC() {
            super("EC");
            this.algorithm = "EC";
            this.configuration = BouncyCastleProvider.CONFIGURATION;
        }

        public EC(String string, ProviderConfiguration providerConfiguration) {
            super(string);
            this.algorithm = string;
            this.configuration = providerConfiguration;
        }

        protected ECKeyGenerationParameters createKeyGenParamsBC(org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec, SecureRandom secureRandom) {
            return new ECKeyGenerationParameters(new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN()), secureRandom);
        }

        protected ECKeyGenerationParameters createKeyGenParamsJCE(ECParameterSpec eCParameterSpec, SecureRandom secureRandom) {
            ECCurve eCCurve = EC5Util.convertCurve((EllipticCurve)eCParameterSpec.getCurve());
            return new ECKeyGenerationParameters(new ECDomainParameters(eCCurve, EC5Util.convertPoint((ECCurve)eCCurve, (ECPoint)eCParameterSpec.getGenerator(), (boolean)false), eCParameterSpec.getOrder(), BigInteger.valueOf((long)eCParameterSpec.getCofactor())), secureRandom);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        protected ECNamedCurveSpec createNamedCurveSpec(String string) {
            X9ECParameters x9ECParameters;
            X9ECParameters x9ECParameters2;
            X9ECParameters x9ECParameters3 = ECNamedCurveTable.getByName(string);
            if (x9ECParameters3 != null) {
                x9ECParameters2 = x9ECParameters3;
                return new ECNamedCurveSpec(string, x9ECParameters2.getCurve(), x9ECParameters2.getG(), x9ECParameters2.getN(), x9ECParameters2.getH(), null);
            }
            try {
                x9ECParameters = ECNamedCurveTable.getByOID(new ASN1ObjectIdentifier(string));
                if (x9ECParameters == null) {
                    throw new InvalidAlgorithmParameterException("unknown curve OID: " + string);
                }
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new InvalidAlgorithmParameterException("unknown curve name: " + string);
            }
            x9ECParameters2 = x9ECParameters;
            return new ECNamedCurveSpec(string, x9ECParameters2.getCurve(), x9ECParameters2.getG(), x9ECParameters2.getN(), x9ECParameters2.getH(), null);
        }

        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                this.initialize(this.strength, new SecureRandom());
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
            ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
            ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
            if (this.ecParams instanceof org.bouncycastle.jce.spec.ECParameterSpec) {
                org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = (org.bouncycastle.jce.spec.ECParameterSpec)this.ecParams;
                BCECPublicKey bCECPublicKey = new BCECPublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec, this.configuration);
                return new KeyPair((PublicKey)bCECPublicKey, (PrivateKey)new BCECPrivateKey(this.algorithm, eCPrivateKeyParameters, bCECPublicKey, eCParameterSpec, this.configuration));
            }
            if (this.ecParams == null) {
                return new KeyPair((PublicKey)new BCECPublicKey(this.algorithm, eCPublicKeyParameters, this.configuration), (PrivateKey)new BCECPrivateKey(this.algorithm, eCPrivateKeyParameters, this.configuration));
            }
            ECParameterSpec eCParameterSpec = (ECParameterSpec)this.ecParams;
            BCECPublicKey bCECPublicKey = new BCECPublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec, this.configuration);
            return new KeyPair((PublicKey)bCECPublicKey, (PrivateKey)new BCECPrivateKey(this.algorithm, eCPrivateKeyParameters, bCECPublicKey, eCParameterSpec, this.configuration));
        }

        public void initialize(int n2, SecureRandom secureRandom) {
            this.strength = n2;
            this.random = secureRandom;
            ECGenParameterSpec eCGenParameterSpec = (ECGenParameterSpec)ecParameters.get((Object)Integers.valueOf((int)n2));
            if (eCGenParameterSpec == null) {
                throw new InvalidParameterException("unknown key size.");
            }
            try {
                this.initialize((AlgorithmParameterSpec)eCGenParameterSpec, secureRandom);
                return;
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                throw new InvalidParameterException("key size not configurable.");
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
            if (algorithmParameterSpec == null) {
                org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = this.configuration.getEcImplicitlyCa();
                if (eCParameterSpec == null) {
                    throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
                }
                this.ecParams = null;
                this.param = this.createKeyGenParamsBC(eCParameterSpec, secureRandom);
            } else if (algorithmParameterSpec instanceof org.bouncycastle.jce.spec.ECParameterSpec) {
                this.ecParams = algorithmParameterSpec;
                this.param = this.createKeyGenParamsBC((org.bouncycastle.jce.spec.ECParameterSpec)algorithmParameterSpec, secureRandom);
            } else if (algorithmParameterSpec instanceof ECParameterSpec) {
                this.ecParams = algorithmParameterSpec;
                this.param = this.createKeyGenParamsJCE((ECParameterSpec)algorithmParameterSpec, secureRandom);
            } else if (algorithmParameterSpec instanceof ECGenParameterSpec) {
                this.initializeNamedCurve(((ECGenParameterSpec)algorithmParameterSpec).getName(), secureRandom);
            } else {
                if (!(algorithmParameterSpec instanceof ECNamedCurveGenParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
                }
                this.initializeNamedCurve(((ECNamedCurveGenParameterSpec)algorithmParameterSpec).getName(), secureRandom);
            }
            this.engine.init(this.param);
            this.initialised = true;
        }

        protected void initializeNamedCurve(String string, SecureRandom secureRandom) {
            ECNamedCurveSpec eCNamedCurveSpec = this.createNamedCurveSpec(string);
            this.ecParams = eCNamedCurveSpec;
            this.param = this.createKeyGenParamsJCE((ECParameterSpec)eCNamedCurveSpec, secureRandom);
        }
    }

    public static class ECDH
    extends EC {
        public ECDH() {
            super("ECDH", BouncyCastleProvider.CONFIGURATION);
        }
    }

    public static class ECDHC
    extends EC {
        public ECDHC() {
            super("ECDHC", BouncyCastleProvider.CONFIGURATION);
        }
    }

    public static class ECDSA
    extends EC {
        public ECDSA() {
            super("ECDSA", BouncyCastleProvider.CONFIGURATION);
        }
    }

    public static class ECMQV
    extends EC {
        public ECMQV() {
            super("ECMQV", BouncyCastleProvider.CONFIGURATION);
        }
    }

}

