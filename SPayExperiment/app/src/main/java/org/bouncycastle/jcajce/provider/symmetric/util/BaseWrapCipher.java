/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.KeyFactory
 *  java.security.NoSuchAlgorithmException
 *  java.security.NoSuchProviderException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.PKCS8EncodedKeySpec
 *  java.security.spec.X509EncodedKeySpec
 *  javax.crypto.BadPaddingException
 *  javax.crypto.CipherSpi
 *  javax.crypto.IllegalBlockSizeException
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.spec.IvParameterSpec
 *  javax.crypto.spec.PBEParameterSpec
 *  javax.crypto.spec.RC2ParameterSpec
 *  javax.crypto.spec.RC5ParameterSpec
 *  javax.crypto.spec.SecretKeySpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.io.PrintStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class BaseWrapCipher
extends CipherSpi
implements PBE {
    private Class[] availableSpecs = new Class[]{IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class};
    protected AlgorithmParameters engineParams = null;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private byte[] iv;
    private int ivSize;
    protected int pbeHash = 1;
    protected int pbeIvSize;
    protected int pbeKeySize;
    protected int pbeType = 2;
    protected Wrapper wrapEngine = null;

    protected BaseWrapCipher() {
    }

    protected BaseWrapCipher(Wrapper wrapper) {
        this(wrapper, 0);
    }

    protected BaseWrapCipher(Wrapper wrapper, int n) {
        this.wrapEngine = wrapper;
        this.ivSize = n;
    }

    protected final AlgorithmParameters createParametersInstance(String string) {
        return this.helper.createAlgorithmParameters(string);
    }

    protected int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        return 0;
    }

    protected byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        return null;
    }

    protected int engineGetBlockSize() {
        return 0;
    }

    protected byte[] engineGetIV() {
        return (byte[])this.iv.clone();
    }

    protected int engineGetKeySize(Key key) {
        return key.getEncoded().length;
    }

    protected int engineGetOutputSize(int n) {
        return -1;
    }

    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void engineInit(int n, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        AlgorithmParameterSpec algorithmParameterSpec;
        if (algorithmParameters == null) {
            algorithmParameterSpec = null;
        } else {
            block6 : {
                for (int i = 0; i != this.availableSpecs.length; ++i) {
                    try {
                        AlgorithmParameterSpec algorithmParameterSpec2;
                        algorithmParameterSpec = algorithmParameterSpec2 = algorithmParameters.getParameterSpec(this.availableSpecs[i]);
                        break block6;
                    }
                    catch (Exception exception) {
                        continue;
                    }
                }
                algorithmParameterSpec = null;
            }
            if (algorithmParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
            }
        }
        this.engineParams = algorithmParameters;
        this.engineInit(n, key, algorithmParameterSpec, secureRandom);
    }

    protected void engineInit(int n, Key key, SecureRandom secureRandom) {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new IllegalArgumentException(invalidAlgorithmParameterException.getMessage());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        CipherParameters cipherParameters;
        CipherParameters cipherParameters2;
        if (key instanceof BCPBEKey) {
            BCPBEKey bCPBEKey = (BCPBEKey)key;
            if (algorithmParameterSpec instanceof PBEParameterSpec) {
                cipherParameters2 = PBE.Util.makePBEParameters(bCPBEKey, algorithmParameterSpec, this.wrapEngine.getAlgorithmName());
            } else {
                if (bCPBEKey.getParam() == null) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                cipherParameters2 = bCPBEKey.getParam();
            }
        } else {
            cipherParameters2 = new KeyParameter(key.getEncoded());
        }
        if (algorithmParameterSpec instanceof IvParameterSpec) {
            cipherParameters2 = new ParametersWithIV(cipherParameters2, ((IvParameterSpec)algorithmParameterSpec).getIV());
        }
        if (cipherParameters2 instanceof KeyParameter && this.ivSize != 0) {
            this.iv = new byte[this.ivSize];
            secureRandom.nextBytes(this.iv);
            cipherParameters = new ParametersWithIV(cipherParameters2, this.iv);
        } else {
            cipherParameters = cipherParameters2;
        }
        CipherParameters cipherParameters3 = secureRandom != null ? new ParametersWithRandom(cipherParameters, secureRandom) : cipherParameters;
        switch (n) {
            default: {
                System.out.println("eeek!");
                return;
            }
            case 3: {
                this.wrapEngine.init(true, cipherParameters3);
                return;
            }
            case 4: {
                this.wrapEngine.init(false, cipherParameters3);
                return;
            }
            case 1: 
            case 2: 
        }
        throw new IllegalArgumentException("engine only valid for wrapping");
    }

    protected void engineSetMode(String string) {
        throw new NoSuchAlgorithmException("can't support mode " + string);
    }

    protected void engineSetPadding(String string) {
        throw new NoSuchPaddingException("Padding " + string + " unknown.");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected Key engineUnwrap(byte[] arrby, String string, int n) {
        KeyFactory keyFactory;
        byte[] arrby2;
        block16 : {
            block17 : {
                block15 : {
                    byte[] arrby3;
                    if (this.wrapEngine != null) break block15;
                    arrby2 = arrby3 = this.engineDoFinal(arrby, 0, arrby.length);
                    while (n == 3) {
                        return new SecretKeySpec(arrby2, string);
                    }
                    break block17;
                }
                try {
                    byte[] arrby4 = this.wrapEngine.unwrap(arrby, 0, arrby.length);
                    arrby2 = arrby4;
                }
                catch (InvalidCipherTextException invalidCipherTextException) {
                    throw new InvalidKeyException(invalidCipherTextException.getMessage());
                }
                catch (BadPaddingException badPaddingException) {
                    throw new InvalidKeyException(badPaddingException.getMessage());
                }
                catch (IllegalBlockSizeException illegalBlockSizeException) {
                    throw new InvalidKeyException(illegalBlockSizeException.getMessage());
                }
            }
            if (string.equals((Object)"") && n == 2) {
                PrivateKeyInfo privateKeyInfo;
                try {
                    privateKeyInfo = PrivateKeyInfo.getInstance(arrby2);
                    SecretKeySpec secretKeySpec = BouncyCastleProvider.getPrivateKey(privateKeyInfo);
                    if (secretKeySpec != null) return secretKeySpec;
                }
                catch (Exception exception) {
                    throw new InvalidKeyException("Invalid key encoding.");
                }
                throw new InvalidKeyException("algorithm " + privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm() + " not supported");
            }
            keyFactory = this.helper.createKeyFactory(string);
            if (n != 1) break block16;
            return keyFactory.generatePublic((KeySpec)new X509EncodedKeySpec(arrby2));
        }
        if (n != 2) throw new InvalidKeyException("Unknown key type " + n);
        try {
            return keyFactory.generatePrivate((KeySpec)new PKCS8EncodedKeySpec(arrby2));
        }
        catch (NoSuchProviderException noSuchProviderException) {
            throw new InvalidKeyException("Unknown key type " + noSuchProviderException.getMessage());
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new InvalidKeyException("Unknown key type " + invalidKeySpecException.getMessage());
        }
    }

    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        throw new RuntimeException("not supported for wrapping");
    }

    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        throw new RuntimeException("not supported for wrapping");
    }

    protected byte[] engineWrap(Key key) {
        byte[] arrby = key.getEncoded();
        if (arrby == null) {
            throw new InvalidKeyException("Cannot wrap key, null encoding.");
        }
        try {
            if (this.wrapEngine == null) {
                return this.engineDoFinal(arrby, 0, arrby.length);
            }
            byte[] arrby2 = this.wrapEngine.wrap(arrby, 0, arrby.length);
            return arrby2;
        }
        catch (BadPaddingException badPaddingException) {
            throw new IllegalBlockSizeException(badPaddingException.getMessage());
        }
    }
}

