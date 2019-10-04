/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.NoSuchAlgorithmException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.MGF1ParameterSpec
 *  javax.crypto.BadPaddingException
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.interfaces.DHKey
 *  javax.crypto.spec.DHParameterSpec
 *  javax.crypto.spec.OAEPParameterSpec
 *  javax.crypto.spec.PSource
 *  javax.crypto.spec.PSource$PSpecified
 */
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.BufferedAsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.ISO9796d1Encoding;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.ElGamalUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseCipherSpi;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.bouncycastle.jce.interfaces.ElGamalKey;
import org.bouncycastle.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.bouncycastle.util.Strings;

public class CipherSpi
extends BaseCipherSpi {
    private BufferedAsymmetricBlockCipher cipher;
    private AlgorithmParameters engineParams;
    private AlgorithmParameterSpec paramSpec;

    public CipherSpi(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.cipher = new BufferedAsymmetricBlockCipher(asymmetricBlockCipher);
    }

    private void initFromSpec(OAEPParameterSpec oAEPParameterSpec) {
        MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec)oAEPParameterSpec.getMGFParameters();
        Digest digest = DigestFactory.getDigest(mGF1ParameterSpec.getDigestAlgorithm());
        if (digest == null) {
            throw new NoSuchPaddingException("no match on OAEP constructor for digest algorithm: " + mGF1ParameterSpec.getDigestAlgorithm());
        }
        this.cipher = new BufferedAsymmetricBlockCipher(new OAEPEncoding(new ElGamalEngine(), digest, ((PSource.PSpecified)oAEPParameterSpec.getPSource()).getValue()));
        this.paramSpec = oAEPParameterSpec;
    }

    protected int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        byte[] arrby3;
        this.cipher.processBytes(arrby, n, n2);
        try {
            arrby3 = this.cipher.doFinal();
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
        for (int i = 0; i != arrby3.length; ++i) {
            arrby2[n3 + i] = arrby3[i];
        }
        return arrby3.length;
    }

    protected byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        this.cipher.processBytes(arrby, n, n2);
        try {
            byte[] arrby2 = this.cipher.doFinal();
            return arrby2;
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
    }

    @Override
    protected int engineGetBlockSize() {
        return this.cipher.getInputBlockSize();
    }

    @Override
    protected int engineGetKeySize(Key key) {
        if (key instanceof ElGamalKey) {
            return ((ElGamalKey)key).getParameters().getP().bitLength();
        }
        if (key instanceof DHKey) {
            return ((DHKey)key).getParams().getP().bitLength();
        }
        throw new IllegalArgumentException("not an ElGamal key!");
    }

    @Override
    protected int engineGetOutputSize(int n) {
        return this.cipher.getOutputBlockSize();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams != null || this.paramSpec == null) return this.engineParams;
        try {
            this.engineParams = this.createParametersInstance("OAEP");
            this.engineParams.init(this.paramSpec);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        return this.engineParams;
    }

    protected void engineInit(int n, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        throw new InvalidAlgorithmParameterException("can't handle parameters in ElGamal");
    }

    protected void engineInit(int n, Key key, SecureRandom secureRandom) {
        this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (algorithmParameterSpec != null) throw new IllegalArgumentException("unknown parameter type.");
        if (key instanceof ElGamalPublicKey) {
            asymmetricKeyParameter = ElGamalUtil.generatePublicKeyParameter((PublicKey)key);
        } else {
            if (!(key instanceof ElGamalPrivateKey)) throw new InvalidKeyException("unknown key type passed to ElGamal");
            asymmetricKeyParameter = ElGamalUtil.generatePrivateKeyParameter((PrivateKey)key);
        }
        CipherParameters cipherParameters = secureRandom != null ? new ParametersWithRandom(asymmetricKeyParameter, secureRandom) : asymmetricKeyParameter;
        switch (n) {
            default: {
                throw new InvalidParameterException("unknown opmode " + n + " passed to ElGamal");
            }
            case 1: 
            case 3: {
                this.cipher.init(true, cipherParameters);
                return;
            }
            case 2: 
            case 4: 
        }
        this.cipher.init(false, cipherParameters);
    }

    @Override
    protected void engineSetMode(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals((Object)"NONE") || string2.equals((Object)"ECB")) {
            return;
        }
        throw new NoSuchAlgorithmException("can't support mode " + string);
    }

    @Override
    protected void engineSetPadding(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals((Object)"NOPADDING")) {
            this.cipher = new BufferedAsymmetricBlockCipher(new ElGamalEngine());
            return;
        }
        if (string2.equals((Object)"PKCS1PADDING")) {
            this.cipher = new BufferedAsymmetricBlockCipher(new PKCS1Encoding(new ElGamalEngine()));
            return;
        }
        if (string2.equals((Object)"ISO9796-1PADDING")) {
            this.cipher = new BufferedAsymmetricBlockCipher(new ISO9796d1Encoding(new ElGamalEngine()));
            return;
        }
        if (string2.equals((Object)"OAEPPADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (string2.equals((Object)"OAEPWITHMD5ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("MD5", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("MD5"), (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA1ANDMGF1PADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA224ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("SHA-224"), (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA256ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", (AlgorithmParameterSpec)MGF1ParameterSpec.SHA256, (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA384ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", (AlgorithmParameterSpec)MGF1ParameterSpec.SHA384, (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA512ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", (AlgorithmParameterSpec)MGF1ParameterSpec.SHA512, (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        throw new NoSuchPaddingException(string + " unavailable with ElGamal.");
    }

    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        this.cipher.processBytes(arrby, n, n2);
        return 0;
    }

    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        this.cipher.processBytes(arrby, n, n2);
        return null;
    }

    public static class NoPadding
    extends CipherSpi {
        public NoPadding() {
            super(new ElGamalEngine());
        }
    }

    public static class PKCS1v1_5Padding
    extends CipherSpi {
        public PKCS1v1_5Padding() {
            super(new PKCS1Encoding(new ElGamalEngine()));
        }
    }

}

