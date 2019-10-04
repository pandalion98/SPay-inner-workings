/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.NoSuchAlgorithmException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.util.Hashtable
 *  javax.crypto.KeyAgreementSpi
 *  javax.crypto.SecretKey
 *  javax.crypto.ShortBufferException
 *  javax.crypto.spec.SecretKeySpec
 *  org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
 *  org.bouncycastle.jce.interfaces.MQVPrivateKey
 *  org.bouncycastle.jce.interfaces.MQVPublicKey
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.util.Integers
 *  org.bouncycastle.util.Strings
 */
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
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle.crypto.agreement.ECMQVBasicAgreement;
import org.bouncycastle.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle.crypto.agreement.kdf.ECDHKEKGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.MQVPrivateParameters;
import org.bouncycastle.crypto.params.MQVPublicParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.MQVPrivateKey;
import org.bouncycastle.jce.interfaces.MQVPublicKey;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;

public class KeyAgreementSpi
extends javax.crypto.KeyAgreementSpi {
    private static final Hashtable algorithms;
    private static final X9IntegerConverter converter;
    private static final Hashtable des;
    private static final Hashtable oids;
    private BasicAgreement agreement;
    private String kaAlgorithm;
    private DerivationFunction kdf;
    private ECDomainParameters parameters;
    private BigInteger result;

    static {
        converter = new X9IntegerConverter();
        algorithms = new Hashtable();
        oids = new Hashtable();
        des = new Hashtable();
        Integer n2 = Integers.valueOf((int)64);
        Integer n3 = Integers.valueOf((int)128);
        Integer n4 = Integers.valueOf((int)192);
        Integer n5 = Integers.valueOf((int)256);
        algorithms.put((Object)NISTObjectIdentifiers.id_aes128_CBC.getId(), (Object)n3);
        algorithms.put((Object)NISTObjectIdentifiers.id_aes192_CBC.getId(), (Object)n4);
        algorithms.put((Object)NISTObjectIdentifiers.id_aes256_CBC.getId(), (Object)n5);
        algorithms.put((Object)NISTObjectIdentifiers.id_aes128_wrap.getId(), (Object)n3);
        algorithms.put((Object)NISTObjectIdentifiers.id_aes192_wrap.getId(), (Object)n4);
        algorithms.put((Object)NISTObjectIdentifiers.id_aes256_wrap.getId(), (Object)n5);
        algorithms.put((Object)PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), (Object)n4);
        algorithms.put((Object)PKCSObjectIdentifiers.des_EDE3_CBC.getId(), (Object)n4);
        algorithms.put((Object)OIWObjectIdentifiers.desCBC.getId(), (Object)n2);
        oids.put((Object)"DESEDE", (Object)PKCSObjectIdentifiers.des_EDE3_CBC);
        oids.put((Object)"AES", (Object)NISTObjectIdentifiers.id_aes256_CBC);
        oids.put((Object)"DES", (Object)OIWObjectIdentifiers.desCBC);
        des.put((Object)"DES", (Object)"DES");
        des.put((Object)"DESEDE", (Object)"DES");
        des.put((Object)OIWObjectIdentifiers.desCBC.getId(), (Object)"DES");
        des.put((Object)PKCSObjectIdentifiers.des_EDE3_CBC.getId(), (Object)"DES");
        des.put((Object)PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), (Object)"DES");
    }

    protected KeyAgreementSpi(String string, BasicAgreement basicAgreement, DerivationFunction derivationFunction) {
        this.kaAlgorithm = string;
        this.agreement = basicAgreement;
        this.kdf = derivationFunction;
    }

    private byte[] bigIntToBytes(BigInteger bigInteger) {
        return converter.integerToBytes(bigInteger, converter.getByteLength(this.parameters.getCurve()));
    }

    private static String getSimpleName(Class class_) {
        String string = class_.getName();
        return string.substring(1 + string.lastIndexOf(46));
    }

    private void initFromKey(Key key) {
        if (this.agreement instanceof ECMQVBasicAgreement) {
            if (!(key instanceof MQVPrivateKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + KeyAgreementSpi.getSimpleName(MQVPrivateKey.class) + " for initialisation");
            }
            MQVPrivateKey mQVPrivateKey = (MQVPrivateKey)key;
            ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)mQVPrivateKey.getStaticPrivateKey());
            ECPrivateKeyParameters eCPrivateKeyParameters2 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)mQVPrivateKey.getEphemeralPrivateKey());
            PublicKey publicKey = mQVPrivateKey.getEphemeralPublicKey();
            ECPublicKeyParameters eCPublicKeyParameters = null;
            if (publicKey != null) {
                eCPublicKeyParameters = (ECPublicKeyParameters)ECUtil.generatePublicKeyParameter((PublicKey)mQVPrivateKey.getEphemeralPublicKey());
            }
            MQVPrivateParameters mQVPrivateParameters = new MQVPrivateParameters(eCPrivateKeyParameters, eCPrivateKeyParameters2, eCPublicKeyParameters);
            this.parameters = eCPrivateKeyParameters.getParameters();
            this.agreement.init(mQVPrivateParameters);
            return;
        }
        if (!(key instanceof PrivateKey)) {
            throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + KeyAgreementSpi.getSimpleName(ECPrivateKey.class) + " for initialisation");
        }
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)((PrivateKey)key));
        this.parameters = eCPrivateKeyParameters.getParameters();
        this.agreement.init(eCPrivateKeyParameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected Key engineDoPhase(Key key, boolean bl) {
        void var3_5;
        if (this.parameters == null) {
            throw new IllegalStateException(this.kaAlgorithm + " not initialised.");
        }
        if (!bl) {
            throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties.");
        }
        if (this.agreement instanceof ECMQVBasicAgreement) {
            if (!(key instanceof MQVPublicKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + KeyAgreementSpi.getSimpleName(MQVPublicKey.class) + " for doPhase");
            }
            MQVPublicKey mQVPublicKey = (MQVPublicKey)key;
            MQVPublicParameters mQVPublicParameters = new MQVPublicParameters((ECPublicKeyParameters)ECUtil.generatePublicKeyParameter((PublicKey)mQVPublicKey.getStaticKey()), (ECPublicKeyParameters)ECUtil.generatePublicKeyParameter((PublicKey)mQVPublicKey.getEphemeralKey()));
        } else {
            if (!(key instanceof PublicKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + KeyAgreementSpi.getSimpleName(ECPublicKey.class) + " for doPhase");
            }
            AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePublicKeyParameter((PublicKey)((PublicKey)key));
        }
        this.result = this.agreement.calculateAgreement((CipherParameters)var3_5);
        return null;
    }

    protected int engineGenerateSecret(byte[] arrby, int n2) {
        byte[] arrby2 = this.engineGenerateSecret();
        if (arrby.length - n2 < arrby2.length) {
            throw new ShortBufferException(this.kaAlgorithm + " key agreement: need " + arrby2.length + " bytes");
        }
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)arrby2.length);
        return arrby2.length;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected SecretKey engineGenerateSecret(String string) {
        byte[] arrby;
        byte[] arrby2 = this.bigIntToBytes(this.result);
        String string2 = Strings.toUpperCase((String)string);
        String string3 = oids.containsKey((Object)string2) ? ((ASN1ObjectIdentifier)oids.get((Object)string2)).getId() : string;
        if (this.kdf != null) {
            if (!algorithms.containsKey((Object)string3)) {
                throw new NoSuchAlgorithmException("unknown algorithm encountered: " + string);
            }
            int n2 = (Integer)algorithms.get((Object)string3);
            DHKDFParameters dHKDFParameters = new DHKDFParameters(new ASN1ObjectIdentifier(string3), n2, arrby2);
            arrby = new byte[n2 / 8];
            this.kdf.init(dHKDFParameters);
            this.kdf.generateBytes(arrby, 0, arrby.length);
        } else if (algorithms.containsKey((Object)string3)) {
            arrby = new byte[(Integer)algorithms.get((Object)string3) / 8];
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)0, (int)arrby.length);
        } else {
            arrby = arrby2;
        }
        if (des.containsKey((Object)string3)) {
            DESParameters.setOddParity(arrby);
        }
        return new SecretKeySpec(arrby, string);
    }

    protected byte[] engineGenerateSecret() {
        if (this.kdf != null) {
            throw new UnsupportedOperationException("KDF can only be used when algorithm is known");
        }
        return this.bigIntToBytes(this.result);
    }

    protected void engineInit(Key key, SecureRandom secureRandom) {
        this.initFromKey(key);
    }

    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No algorithm parameters supported");
        }
        this.initFromKey(key);
    }

    public static class DH
    extends KeyAgreementSpi {
        public DH() {
            super("ECDH", new ECDHBasicAgreement(), null);
        }
    }

    public static class DHC
    extends KeyAgreementSpi {
        public DHC() {
            super("ECDHC", new ECDHCBasicAgreement(), null);
        }
    }

    public static class DHwithSHA1KDF
    extends KeyAgreementSpi {
        public DHwithSHA1KDF() {
            super("ECDHwithSHA1KDF", new ECDHBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }

    public static class MQV
    extends KeyAgreementSpi {
        public MQV() {
            super("ECMQV", new ECMQVBasicAgreement(), null);
        }
    }

    public static class MQVwithSHA1KDF
    extends KeyAgreementSpi {
        public MQVwithSHA1KDF() {
            super("ECMQVwithSHA1KDF", new ECMQVBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }

}

