/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.AlgorithmParameters
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.KeyFactory
 *  java.security.NoSuchAlgorithmException
 *  java.security.NoSuchProviderException
 *  java.security.PrivateKey
 *  java.security.PublicKey
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
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class BaseCipherSpi
extends CipherSpi {
    private Class[] availableSpecs = new Class[]{IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class};
    protected AlgorithmParameters engineParams = null;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private byte[] iv;
    private int ivSize;
    protected Wrapper wrapEngine = null;

    protected BaseCipherSpi() {
    }

    protected final AlgorithmParameters createParametersInstance(String string) {
        return this.helper.createAlgorithmParameters(string);
    }

    protected int engineGetBlockSize() {
        return 0;
    }

    protected byte[] engineGetIV() {
        return null;
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
        block17 : {
            block18 : {
                block16 : {
                    byte[] arrby3;
                    if (this.wrapEngine != null) break block16;
                    arrby2 = arrby3 = this.engineDoFinal(arrby, 0, arrby.length);
                    while (n == 3) {
                        return new SecretKeySpec(arrby2, string);
                    }
                    break block18;
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
            if (n != 1) break block17;
            return keyFactory.generatePublic((KeySpec)new X509EncodedKeySpec(arrby2));
        }
        if (n != 2) throw new InvalidKeyException("Unknown key type " + n);
        try {
            return keyFactory.generatePrivate((KeySpec)new PKCS8EncodedKeySpec(arrby2));
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new InvalidKeyException("Unknown key type " + noSuchAlgorithmException.getMessage());
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new InvalidKeyException("Unknown key type " + invalidKeySpecException.getMessage());
        }
        catch (NoSuchProviderException noSuchProviderException) {
            throw new InvalidKeyException("Unknown key type " + noSuchProviderException.getMessage());
        }
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

