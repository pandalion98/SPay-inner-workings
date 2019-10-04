/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.NoSuchAlgorithmException
 *  java.security.SecureRandom
 *  java.security.interfaces.RSAPrivateKey
 *  java.security.interfaces.RSAPublicKey
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  java.security.spec.MGF1ParameterSpec
 *  javax.crypto.BadPaddingException
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.spec.OAEPParameterSpec
 *  javax.crypto.spec.PSource
 *  javax.crypto.spec.PSource$PSpecified
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.ISO9796d1Encoding;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseCipherSpi;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.util.Strings;

public class CipherSpi
extends BaseCipherSpi {
    private ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    private AsymmetricBlockCipher cipher;
    private AlgorithmParameters engineParams;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private AlgorithmParameterSpec paramSpec;
    private boolean privateKeyOnly = false;
    private boolean publicKeyOnly = false;

    public CipherSpi(OAEPParameterSpec oAEPParameterSpec) {
        try {
            this.initFromSpec(oAEPParameterSpec);
            return;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            throw new IllegalArgumentException(noSuchPaddingException.getMessage());
        }
    }

    public CipherSpi(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.cipher = asymmetricBlockCipher;
    }

    public CipherSpi(boolean bl, boolean bl2, AsymmetricBlockCipher asymmetricBlockCipher) {
        this.publicKeyOnly = bl;
        this.privateKeyOnly = bl2;
        this.cipher = asymmetricBlockCipher;
    }

    private void initFromSpec(OAEPParameterSpec oAEPParameterSpec) {
        MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec)oAEPParameterSpec.getMGFParameters();
        Digest digest = DigestFactory.getDigest(mGF1ParameterSpec.getDigestAlgorithm());
        if (digest == null) {
            throw new NoSuchPaddingException("no match on OAEP constructor for digest algorithm: " + mGF1ParameterSpec.getDigestAlgorithm());
        }
        this.cipher = new OAEPEncoding(new RSABlindedEngine(), digest, ((PSource.PSpecified)oAEPParameterSpec.getPSource()).getValue());
        this.paramSpec = oAEPParameterSpec;
    }

    protected int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        byte[] arrby3;
        int n4 = 0;
        if (arrby != null) {
            this.bOut.write(arrby, n, n2);
        }
        if (this.cipher instanceof RSABlindedEngine ? this.bOut.size() > 1 + this.cipher.getInputBlockSize() : this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        try {
            byte[] arrby4 = this.bOut.toByteArray();
            arrby3 = this.cipher.processBlock(arrby4, 0, arrby4.length);
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
        finally {
            this.bOut.reset();
        }
        while (n4 != arrby3.length) {
            arrby2[n3 + n4] = arrby3[n4];
            ++n4;
        }
        return arrby3.length;
    }

    protected byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        if (arrby != null) {
            this.bOut.write(arrby, n, n2);
        }
        if (this.cipher instanceof RSABlindedEngine ? this.bOut.size() > 1 + this.cipher.getInputBlockSize() : this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        try {
            byte[] arrby2 = this.bOut.toByteArray();
            this.bOut.reset();
            byte[] arrby3 = this.cipher.processBlock(arrby2, 0, arrby2.length);
            return arrby3;
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
    }

    @Override
    protected int engineGetBlockSize() {
        try {
            int n = this.cipher.getInputBlockSize();
            return n;
        }
        catch (NullPointerException nullPointerException) {
            throw new IllegalStateException("RSA Cipher not initialised");
        }
    }

    @Override
    protected int engineGetKeySize(Key key) {
        if (key instanceof RSAPrivateKey) {
            return ((RSAPrivateKey)key).getModulus().bitLength();
        }
        if (key instanceof RSAPublicKey) {
            return ((RSAPublicKey)key).getModulus().bitLength();
        }
        throw new IllegalArgumentException("not an RSA key!");
    }

    @Override
    protected int engineGetOutputSize(int n) {
        try {
            int n2 = this.cipher.getOutputBlockSize();
            return n2;
        }
        catch (NullPointerException nullPointerException) {
            throw new IllegalStateException("RSA Cipher not initialised");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams != null || this.paramSpec == null) return this.engineParams;
        try {
            this.engineParams = this.helper.createAlgorithmParameters("OAEP");
            this.engineParams.init(this.paramSpec);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        return this.engineParams;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected void engineInit(int n, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        AlgorithmParameterSpec algorithmParameterSpec = null;
        if (algorithmParameters != null) {
            AlgorithmParameterSpec algorithmParameterSpec2;
            algorithmParameterSpec = algorithmParameterSpec2 = algorithmParameters.getParameterSpec(OAEPParameterSpec.class);
        }
        this.engineParams = algorithmParameters;
        this.engineInit(n, key, algorithmParameterSpec, secureRandom);
        return;
        catch (InvalidParameterSpecException invalidParameterSpecException) {
            throw new InvalidAlgorithmParameterException("cannot recognise parameters: " + invalidParameterSpecException.toString(), (Throwable)invalidParameterSpecException);
        }
    }

    protected void engineInit(int n, Key key, SecureRandom secureRandom) {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException("Eeeek! " + invalidAlgorithmParameterException.toString(), (Throwable)invalidAlgorithmParameterException);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        CipherParameters cipherParameters;
        block17 : {
            RSAKeyParameters rSAKeyParameters;
            block15 : {
                block16 : {
                    block14 : {
                        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof OAEPParameterSpec)) break block14;
                        if (key instanceof RSAPublicKey) {
                            if (this.privateKeyOnly && n == 1) {
                                throw new InvalidKeyException("mode 1 requires RSAPrivateKey");
                            }
                            rSAKeyParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)key);
                        } else {
                            if (!(key instanceof RSAPrivateKey)) {
                                throw new InvalidKeyException("unknown key type passed to RSA");
                            }
                            if (this.publicKeyOnly && n == 1) {
                                throw new InvalidKeyException("mode 2 requires RSAPublicKey");
                            }
                            rSAKeyParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)key);
                        }
                        if (algorithmParameterSpec != null) {
                            OAEPParameterSpec oAEPParameterSpec = (OAEPParameterSpec)algorithmParameterSpec;
                            this.paramSpec = algorithmParameterSpec;
                            if (!oAEPParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1") && !oAEPParameterSpec.getMGFAlgorithm().equals((Object)PKCSObjectIdentifiers.id_mgf1.getId())) {
                                throw new InvalidAlgorithmParameterException("unknown mask generation function specified");
                            }
                            if (!(oAEPParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
                                throw new InvalidAlgorithmParameterException("unkown MGF parameters");
                            }
                            Digest digest = DigestFactory.getDigest(oAEPParameterSpec.getDigestAlgorithm());
                            if (digest == null) {
                                throw new InvalidAlgorithmParameterException("no match on digest algorithm: " + oAEPParameterSpec.getDigestAlgorithm());
                            }
                            MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec)oAEPParameterSpec.getMGFParameters();
                            Digest digest2 = DigestFactory.getDigest(mGF1ParameterSpec.getDigestAlgorithm());
                            if (digest2 == null) {
                                throw new InvalidAlgorithmParameterException("no match on MGF digest algorithm: " + mGF1ParameterSpec.getDigestAlgorithm());
                            }
                            this.cipher = new OAEPEncoding(new RSABlindedEngine(), digest, digest2, ((PSource.PSpecified)oAEPParameterSpec.getPSource()).getValue());
                        }
                        if (this.cipher instanceof RSABlindedEngine) break block15;
                        if (secureRandom == null) break block16;
                        cipherParameters = new ParametersWithRandom(rSAKeyParameters, secureRandom);
                        break block17;
                    }
                    throw new InvalidAlgorithmParameterException("unknown parameter type: " + algorithmParameterSpec.getClass().getName());
                }
                cipherParameters = new ParametersWithRandom(rSAKeyParameters, new SecureRandom());
                break block17;
            }
            cipherParameters = rSAKeyParameters;
        }
        this.bOut.reset();
        switch (n) {
            default: {
                throw new InvalidParameterException("unknown opmode " + n + " passed to RSA");
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
        if (string2.equals((Object)"1")) {
            this.privateKeyOnly = true;
            this.publicKeyOnly = false;
            return;
        }
        if (string2.equals((Object)"2")) {
            this.privateKeyOnly = false;
            this.publicKeyOnly = true;
            return;
        }
        throw new NoSuchAlgorithmException("can't support mode " + string);
    }

    @Override
    protected void engineSetPadding(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals((Object)"NOPADDING")) {
            this.cipher = new RSABlindedEngine();
            return;
        }
        if (string2.equals((Object)"PKCS1PADDING")) {
            this.cipher = new PKCS1Encoding(new RSABlindedEngine());
            return;
        }
        if (string2.equals((Object)"ISO9796-1PADDING")) {
            this.cipher = new ISO9796d1Encoding(new RSABlindedEngine());
            return;
        }
        if (string2.equals((Object)"OAEPWITHMD5ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("MD5", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("MD5"), (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPPADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA1ANDMGF1PADDING") || string2.equals((Object)"OAEPWITHSHA-1ANDMGF1PADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA224ANDMGF1PADDING") || string2.equals((Object)"OAEPWITHSHA-224ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("SHA-224"), (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA256ANDMGF1PADDING") || string2.equals((Object)"OAEPWITHSHA-256ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", (AlgorithmParameterSpec)MGF1ParameterSpec.SHA256, (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA384ANDMGF1PADDING") || string2.equals((Object)"OAEPWITHSHA-384ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", (AlgorithmParameterSpec)MGF1ParameterSpec.SHA384, (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        if (string2.equals((Object)"OAEPWITHSHA512ANDMGF1PADDING") || string2.equals((Object)"OAEPWITHSHA-512ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", (AlgorithmParameterSpec)MGF1ParameterSpec.SHA512, (PSource)PSource.PSpecified.DEFAULT));
            return;
        }
        throw new NoSuchPaddingException(string + " unavailable with RSA.");
    }

    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        this.bOut.write(arrby, n, n2);
        if (this.cipher instanceof RSABlindedEngine ? this.bOut.size() > 1 + this.cipher.getInputBlockSize() : this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return 0;
    }

    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        this.bOut.write(arrby, n, n2);
        if (this.cipher instanceof RSABlindedEngine ? this.bOut.size() > 1 + this.cipher.getInputBlockSize() : this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return null;
    }

    public static class ISO9796d1Padding
    extends CipherSpi {
        public ISO9796d1Padding() {
            super(new ISO9796d1Encoding(new RSABlindedEngine()));
        }
    }

    public static class NoPadding
    extends CipherSpi {
        public NoPadding() {
            super(new RSABlindedEngine());
        }
    }

    public static class OAEPPadding
    extends CipherSpi {
        public OAEPPadding() {
            super(OAEPParameterSpec.DEFAULT);
        }
    }

    public static class PKCS1v1_5Padding
    extends CipherSpi {
        public PKCS1v1_5Padding() {
            super(new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class PKCS1v1_5Padding_PrivateOnly
    extends CipherSpi {
        public PKCS1v1_5Padding_PrivateOnly() {
            super(false, true, new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

    public static class PKCS1v1_5Padding_PublicOnly
    extends CipherSpi {
        public PKCS1v1_5Padding_PublicOnly() {
            super(true, false, new PKCS1Encoding(new RSABlindedEngine()));
        }
    }

}

