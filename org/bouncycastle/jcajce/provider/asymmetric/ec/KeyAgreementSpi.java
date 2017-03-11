package org.bouncycastle.jcajce.provider.asymmetric.ec;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle.crypto.agreement.ECMQVBasicAgreement;
import org.bouncycastle.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle.crypto.agreement.kdf.ECDHKEKGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.MQVPrivateParameters;
import org.bouncycastle.crypto.params.MQVPublicParameters;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.MQVPrivateKey;
import org.bouncycastle.jce.interfaces.MQVPublicKey;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;

public class KeyAgreementSpi extends javax.crypto.KeyAgreementSpi {
    private static final Hashtable algorithms;
    private static final X9IntegerConverter converter;
    private static final Hashtable des;
    private static final Hashtable oids;
    private BasicAgreement agreement;
    private String kaAlgorithm;
    private DerivationFunction kdf;
    private ECDomainParameters parameters;
    private BigInteger result;

    public static class DH extends KeyAgreementSpi {
        public DH() {
            super("ECDH", new ECDHBasicAgreement(), null);
        }
    }

    public static class DHC extends KeyAgreementSpi {
        public DHC() {
            super("ECDHC", new ECDHCBasicAgreement(), null);
        }
    }

    public static class DHwithSHA1KDF extends KeyAgreementSpi {
        public DHwithSHA1KDF() {
            super("ECDHwithSHA1KDF", new ECDHBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }

    public static class MQV extends KeyAgreementSpi {
        public MQV() {
            super("ECMQV", new ECMQVBasicAgreement(), null);
        }
    }

    public static class MQVwithSHA1KDF extends KeyAgreementSpi {
        public MQVwithSHA1KDF() {
            super("ECMQVwithSHA1KDF", new ECMQVBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }

    static {
        converter = new X9IntegerConverter();
        algorithms = new Hashtable();
        oids = new Hashtable();
        des = new Hashtable();
        Integer valueOf = Integers.valueOf(64);
        Integer valueOf2 = Integers.valueOf(X509KeyUsage.digitalSignature);
        Integer valueOf3 = Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256);
        Integer valueOf4 = Integers.valueOf(SkeinMac.SKEIN_256);
        algorithms.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), valueOf2);
        algorithms.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), valueOf3);
        algorithms.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), valueOf4);
        algorithms.put(NISTObjectIdentifiers.id_aes128_wrap.getId(), valueOf2);
        algorithms.put(NISTObjectIdentifiers.id_aes192_wrap.getId(), valueOf3);
        algorithms.put(NISTObjectIdentifiers.id_aes256_wrap.getId(), valueOf4);
        algorithms.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), valueOf3);
        algorithms.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), valueOf3);
        algorithms.put(OIWObjectIdentifiers.desCBC.getId(), valueOf);
        oids.put("DESEDE", PKCSObjectIdentifiers.des_EDE3_CBC);
        oids.put("AES", NISTObjectIdentifiers.id_aes256_CBC);
        oids.put("DES", OIWObjectIdentifiers.desCBC);
        des.put("DES", "DES");
        des.put("DESEDE", "DES");
        des.put(OIWObjectIdentifiers.desCBC.getId(), "DES");
        des.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), "DES");
        des.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), "DES");
    }

    protected KeyAgreementSpi(String str, BasicAgreement basicAgreement, DerivationFunction derivationFunction) {
        this.kaAlgorithm = str;
        this.agreement = basicAgreement;
        this.kdf = derivationFunction;
    }

    private byte[] bigIntToBytes(BigInteger bigInteger) {
        return converter.integerToBytes(bigInteger, converter.getByteLength(this.parameters.getCurve()));
    }

    private static String getSimpleName(Class cls) {
        String name = cls.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }

    private void initFromKey(Key key) {
        ECPrivateKeyParameters eCPrivateKeyParameters;
        if (this.agreement instanceof ECMQVBasicAgreement) {
            if (key instanceof MQVPrivateKey) {
                MQVPrivateKey mQVPrivateKey = (MQVPrivateKey) key;
                eCPrivateKeyParameters = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(mQVPrivateKey.getStaticPrivateKey());
                ECPrivateKeyParameters eCPrivateKeyParameters2 = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(mQVPrivateKey.getEphemeralPrivateKey());
                ECPublicKeyParameters eCPublicKeyParameters = null;
                if (mQVPrivateKey.getEphemeralPublicKey() != null) {
                    eCPublicKeyParameters = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(mQVPrivateKey.getEphemeralPublicKey());
                }
                CipherParameters mQVPrivateParameters = new MQVPrivateParameters(eCPrivateKeyParameters, eCPrivateKeyParameters2, eCPublicKeyParameters);
                this.parameters = eCPrivateKeyParameters.getParameters();
                this.agreement.init(mQVPrivateParameters);
                return;
            }
            throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPrivateKey.class) + " for initialisation");
        } else if (key instanceof PrivateKey) {
            eCPrivateKeyParameters = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter((PrivateKey) key);
            this.parameters = eCPrivateKeyParameters.getParameters();
            this.agreement.init(eCPrivateKeyParameters);
        } else {
            throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPrivateKey.class) + " for initialisation");
        }
    }

    protected Key engineDoPhase(Key key, boolean z) {
        if (this.parameters == null) {
            throw new IllegalStateException(this.kaAlgorithm + " not initialised.");
        } else if (z) {
            CipherParameters mQVPublicParameters;
            if (this.agreement instanceof ECMQVBasicAgreement) {
                if (key instanceof MQVPublicKey) {
                    MQVPublicKey mQVPublicKey = (MQVPublicKey) key;
                    mQVPublicParameters = new MQVPublicParameters((ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(mQVPublicKey.getStaticKey()), (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(mQVPublicKey.getEphemeralKey()));
                } else {
                    throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPublicKey.class) + " for doPhase");
                }
            } else if (key instanceof PublicKey) {
                mQVPublicParameters = ECUtil.generatePublicKeyParameter((PublicKey) key);
            } else {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPublicKey.class) + " for doPhase");
            }
            this.result = this.agreement.calculateAgreement(mQVPublicParameters);
            return null;
        } else {
            throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties.");
        }
    }

    protected int engineGenerateSecret(byte[] bArr, int i) {
        Object engineGenerateSecret = engineGenerateSecret();
        if (bArr.length - i < engineGenerateSecret.length) {
            throw new ShortBufferException(this.kaAlgorithm + " key agreement: need " + engineGenerateSecret.length + " bytes");
        }
        System.arraycopy(engineGenerateSecret, 0, bArr, i, engineGenerateSecret.length);
        return engineGenerateSecret.length;
    }

    protected SecretKey engineGenerateSecret(String str) {
        byte[] bArr;
        Object bigIntToBytes = bigIntToBytes(this.result);
        String toUpperCase = Strings.toUpperCase(str);
        String id = oids.containsKey(toUpperCase) ? ((ASN1ObjectIdentifier) oids.get(toUpperCase)).getId() : str;
        if (this.kdf != null) {
            if (algorithms.containsKey(id)) {
                int intValue = ((Integer) algorithms.get(id)).intValue();
                DerivationParameters dHKDFParameters = new DHKDFParameters(new ASN1ObjectIdentifier(id), intValue, bigIntToBytes);
                bArr = new byte[(intValue / 8)];
                this.kdf.init(dHKDFParameters);
                this.kdf.generateBytes(bArr, 0, bArr.length);
            } else {
                throw new NoSuchAlgorithmException("unknown algorithm encountered: " + str);
            }
        } else if (algorithms.containsKey(id)) {
            bArr = new byte[(((Integer) algorithms.get(id)).intValue() / 8)];
            System.arraycopy(bigIntToBytes, 0, bArr, 0, bArr.length);
        } else {
            Object obj = bigIntToBytes;
        }
        if (des.containsKey(id)) {
            DESParameters.setOddParity(bArr);
        }
        return new SecretKeySpec(bArr, str);
    }

    protected byte[] engineGenerateSecret() {
        if (this.kdf == null) {
            return bigIntToBytes(this.result);
        }
        throw new UnsupportedOperationException("KDF can only be used when algorithm is known");
    }

    protected void engineInit(Key key, SecureRandom secureRandom) {
        initFromKey(key);
    }

    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No algorithm parameters supported");
        }
        initFromKey(key);
    }
}
