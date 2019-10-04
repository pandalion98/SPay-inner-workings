/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
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
 *  javax.crypto.IllegalBlockSizeException
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.spec.IvParameterSpec
 *  javax.crypto.spec.PBEParameterSpec
 *  javax.crypto.spec.RC2ParameterSpec
 *  javax.crypto.spec.RC5ParameterSpec
 *  javax.crypto.spec.SecretKeySpec
 */
package org.bouncycastle.jce.provider;

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
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.CTSBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.RC2Parameters;
import org.bouncycastle.crypto.params.RC5Parameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jce.provider.BrokenPBE;
import org.bouncycastle.util.Strings;

public class BrokenJCEBlockCipher
implements BrokenPBE {
    private Class[] availableSpecs = new Class[]{IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class};
    private BufferedBlockCipher cipher;
    private AlgorithmParameters engineParams = null;
    private int ivLength = 0;
    private ParametersWithIV ivParam;
    private int pbeHash = 1;
    private int pbeIvSize;
    private int pbeKeySize;
    private int pbeType = 2;

    protected BrokenJCEBlockCipher(BlockCipher blockCipher) {
        this.cipher = new PaddedBufferedBlockCipher(blockCipher);
    }

    protected BrokenJCEBlockCipher(BlockCipher blockCipher, int n, int n2, int n3, int n4) {
        this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        this.pbeType = n;
        this.pbeHash = n2;
        this.pbeKeySize = n3;
        this.pbeIvSize = n4;
    }

    protected int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        int n4;
        int n5 = 0;
        if (n2 != 0) {
            n5 = this.cipher.processBytes(arrby, n, n2, arrby2, n3);
        }
        try {
            n4 = this.cipher.doFinal(arrby2, n3 + n5);
        }
        catch (DataLengthException dataLengthException) {
            throw new IllegalBlockSizeException(dataLengthException.getMessage());
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
        return n5 + n4;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        int n3;
        byte[] arrby2 = new byte[this.engineGetOutputSize(n2)];
        int n4 = n2 != 0 ? this.cipher.processBytes(arrby, n, n2, arrby2, 0) : 0;
        try {
            n3 = this.cipher.doFinal(arrby2, n4);
        }
        catch (DataLengthException dataLengthException) {
            throw new IllegalBlockSizeException(dataLengthException.getMessage());
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
        int n5 = n4 + n3;
        byte[] arrby3 = new byte[n5];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)0, (int)n5);
        return arrby3;
    }

    protected int engineGetBlockSize() {
        return this.cipher.getBlockSize();
    }

    protected byte[] engineGetIV() {
        if (this.ivParam != null) {
            return this.ivParam.getIV();
        }
        return null;
    }

    protected int engineGetKeySize(Key key) {
        return key.getEncoded().length;
    }

    protected int engineGetOutputSize(int n) {
        return this.cipher.getOutputSize(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams != null || this.ivParam == null) return this.engineParams;
        String string = this.cipher.getUnderlyingCipher().getAlgorithmName();
        if (string.indexOf(47) >= 0) {
            string = string.substring(0, string.indexOf(47));
        }
        try {
            this.engineParams = AlgorithmParameters.getInstance((String)string, (String)"BC");
            this.engineParams.init(this.ivParam.getIV());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        return this.engineParams;
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
     * Lifted jumps to return sites
     */
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        void var8_9;
        void var8_7;
        block15 : {
            void var13_22;
            block20 : {
                RC2Parameters rC2Parameters;
                block19 : {
                    void var7_27;
                    block18 : {
                        ParametersWithIV parametersWithIV;
                        block17 : {
                            block16 : {
                                block14 : {
                                    if (!(key instanceof BCPBEKey)) break block14;
                                    CipherParameters cipherParameters = BrokenPBE.Util.makePBEParameters((BCPBEKey)key, algorithmParameterSpec, this.pbeType, this.pbeHash, this.cipher.getUnderlyingCipher().getAlgorithmName(), this.pbeKeySize, this.pbeIvSize);
                                    if (this.pbeIvSize != 0) {
                                        this.ivParam = (ParametersWithIV)cipherParameters;
                                        CipherParameters cipherParameters2 = cipherParameters;
                                    } else {
                                        CipherParameters cipherParameters3 = cipherParameters;
                                    }
                                    break block15;
                                }
                                if (algorithmParameterSpec != null) break block16;
                                KeyParameter keyParameter = new KeyParameter(key.getEncoded());
                                break block15;
                            }
                            if (!(algorithmParameterSpec instanceof IvParameterSpec)) break block17;
                            if (this.ivLength != 0) {
                                ParametersWithIV parametersWithIV2;
                                this.ivParam = parametersWithIV2 = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
                                ParametersWithIV parametersWithIV3 = parametersWithIV2;
                            } else {
                                KeyParameter keyParameter = new KeyParameter(key.getEncoded());
                            }
                            break block15;
                        }
                        if (!(algorithmParameterSpec instanceof RC2ParameterSpec)) break block18;
                        RC2ParameterSpec rC2ParameterSpec = (RC2ParameterSpec)algorithmParameterSpec;
                        rC2Parameters = new RC2Parameters(key.getEncoded(), ((RC2ParameterSpec)algorithmParameterSpec).getEffectiveKeyBits());
                        if (rC2ParameterSpec.getIV() == null || this.ivLength == 0) break block19;
                        this.ivParam = parametersWithIV = new ParametersWithIV(rC2Parameters, rC2ParameterSpec.getIV());
                        break block20;
                    }
                    if (!(algorithmParameterSpec instanceof RC5ParameterSpec)) throw new InvalidAlgorithmParameterException("unknown parameter type.");
                    RC5ParameterSpec rC5ParameterSpec = (RC5ParameterSpec)algorithmParameterSpec;
                    RC5Parameters rC5Parameters = new RC5Parameters(key.getEncoded(), ((RC5ParameterSpec)algorithmParameterSpec).getRounds());
                    if (rC5ParameterSpec.getWordSize() != 32) {
                        throw new IllegalArgumentException("can only accept RC5 word size 32 (at the moment...)");
                    }
                    if (rC5ParameterSpec.getIV() != null && this.ivLength != 0) {
                        ParametersWithIV parametersWithIV;
                        this.ivParam = parametersWithIV = new ParametersWithIV(rC5Parameters, rC5ParameterSpec.getIV());
                    } else {
                        RC5Parameters rC5Parameters2 = rC5Parameters;
                    }
                    void var8_14 = var7_27;
                    break block15;
                }
                RC2Parameters rC2Parameters2 = rC2Parameters;
            }
            void var8_13 = var13_22;
        }
        if (this.ivLength != 0 && !(var8_7 instanceof ParametersWithIV)) {
            ParametersWithIV parametersWithIV;
            if (secureRandom == null) {
                secureRandom = new SecureRandom();
            }
            if (n != 1) {
                if (n != 3) throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
            byte[] arrby = new byte[this.ivLength];
            secureRandom.nextBytes(arrby);
            this.ivParam = parametersWithIV = new ParametersWithIV((CipherParameters)var8_7, arrby);
            ParametersWithIV parametersWithIV4 = parametersWithIV;
        }
        switch (n) {
            default: {
                System.out.println("eeek!");
                return;
            }
            case 1: 
            case 3: {
                this.cipher.init(true, (CipherParameters)var8_9);
                return;
            }
            case 2: 
            case 4: 
        }
        this.cipher.init(false, (CipherParameters)var8_9);
    }

    protected void engineSetMode(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals((Object)"ECB")) {
            this.ivLength = 0;
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
            return;
        }
        if (string2.equals((Object)"CBC")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(this.cipher.getUnderlyingCipher()));
            return;
        }
        if (string2.startsWith("OFB")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            if (string2.length() != 3) {
                int n = Integer.parseInt((String)string2.substring(3));
                this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.cipher.getUnderlyingCipher(), n));
                return;
            }
            this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.cipher.getUnderlyingCipher(), 8 * this.cipher.getBlockSize()));
            return;
        }
        if (string2.startsWith("CFB")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            if (string2.length() != 3) {
                int n = Integer.parseInt((String)string2.substring(3));
                this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.cipher.getUnderlyingCipher(), n));
                return;
            }
            this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.cipher.getUnderlyingCipher(), 8 * this.cipher.getBlockSize()));
            return;
        }
        throw new IllegalArgumentException("can't support mode " + string);
    }

    protected void engineSetPadding(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals((Object)"NOPADDING")) {
            this.cipher = new BufferedBlockCipher(this.cipher.getUnderlyingCipher());
            return;
        }
        if (string2.equals((Object)"PKCS5PADDING") || string2.equals((Object)"PKCS7PADDING") || string2.equals((Object)"ISO10126PADDING")) {
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
            return;
        }
        if (string2.equals((Object)"WITHCTS")) {
            this.cipher = new CTSBlockCipher(this.cipher.getUnderlyingCipher());
            return;
        }
        throw new NoSuchPaddingException("Padding " + string + " unknown.");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected Key engineUnwrap(byte[] var1_1, String var2_2, int var3_3) {
        try {
            var6_4 = this.engineDoFinal(var1_1, 0, var1_1.length);
            ** if (var3_3 != 3) goto lbl-1000
        }
        catch (BadPaddingException var5_5) {
            throw new InvalidKeyException(var5_5.getMessage());
        }
        catch (IllegalBlockSizeException var4_6) {
            throw new InvalidKeyException(var4_6.getMessage());
        }
lbl4: // 1 sources:
        return new SecretKeySpec(var6_4, var2_2);
lbl-1000: // 1 sources:
        {
        }
        try {
            var10_7 = KeyFactory.getInstance((String)var2_2, (String)"BC");
            if (var3_3 == 1) {
                return var10_7.generatePublic((KeySpec)new X509EncodedKeySpec(var6_4));
            }
            if (var3_3 != 2) throw new InvalidKeyException("Unknown key type " + var3_3);
            return var10_7.generatePrivate((KeySpec)new PKCS8EncodedKeySpec(var6_4));
        }
        catch (NoSuchProviderException var9_9) {
            throw new InvalidKeyException("Unknown key type " + var9_9.getMessage());
        }
        catch (NoSuchAlgorithmException var8_10) {
            throw new InvalidKeyException("Unknown key type " + var8_10.getMessage());
        }
        catch (InvalidKeySpecException var7_11) {
            throw new InvalidKeyException("Unknown key type " + var7_11.getMessage());
        }
    }

    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        return this.cipher.processBytes(arrby, n, n2, arrby2, n3);
    }

    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        int n3 = this.cipher.getUpdateOutputSize(n2);
        if (n3 > 0) {
            byte[] arrby2 = new byte[n3];
            this.cipher.processBytes(arrby, n, n2, arrby2, 0);
            return arrby2;
        }
        this.cipher.processBytes(arrby, n, n2, null, 0);
        return null;
    }

    protected byte[] engineWrap(Key key) {
        byte[] arrby = key.getEncoded();
        if (arrby == null) {
            throw new InvalidKeyException("Cannot wrap key, null encoding.");
        }
        try {
            byte[] arrby2 = this.engineDoFinal(arrby, 0, arrby.length);
            return arrby2;
        }
        catch (BadPaddingException badPaddingException) {
            throw new IllegalBlockSizeException(badPaddingException.getMessage());
        }
    }

    public static class BrokePBEWithMD5AndDES
    extends BrokenJCEBlockCipher {
        public BrokePBEWithMD5AndDES() {
            super(new CBCBlockCipher(new DESEngine()), 0, 0, 64, 64);
        }
    }

    public static class BrokePBEWithSHA1AndDES
    extends BrokenJCEBlockCipher {
        public BrokePBEWithSHA1AndDES() {
            super(new CBCBlockCipher(new DESEngine()), 0, 1, 64, 64);
        }
    }

    public static class BrokePBEWithSHAAndDES2Key
    extends BrokenJCEBlockCipher {
        public BrokePBEWithSHAAndDES2Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 2, 1, 128, 64);
        }
    }

    public static class BrokePBEWithSHAAndDES3Key
    extends BrokenJCEBlockCipher {
        public BrokePBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 2, 1, 192, 64);
        }
    }

    public static class OldPBEWithSHAAndDES3Key
    extends BrokenJCEBlockCipher {
        public OldPBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 3, 1, 192, 64);
        }
    }

    public static class OldPBEWithSHAAndTwofish
    extends BrokenJCEBlockCipher {
        public OldPBEWithSHAAndTwofish() {
            super(new CBCBlockCipher(new TwofishEngine()), 3, 1, 256, 128);
        }
    }

}

