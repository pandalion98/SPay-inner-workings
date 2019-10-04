/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.lang.reflect.Constructor
 *  java.lang.reflect.Method
 *  java.nio.ByteBuffer
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.NoSuchAlgorithmException
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.BadPaddingException
 *  javax.crypto.IllegalBlockSizeException
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.SecretKey
 *  javax.crypto.ShortBufferException
 *  javax.crypto.spec.IvParameterSpec
 *  javax.crypto.spec.PBEParameterSpec
 *  javax.crypto.spec.RC2ParameterSpec
 *  javax.crypto.spec.RC5ParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.GCMParameters;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.CTSBlockCipher;
import org.bouncycastle.crypto.modes.EAXBlockCipher;
import org.bouncycastle.crypto.modes.GCFBBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.modes.GOFBBlockCipher;
import org.bouncycastle.crypto.modes.OCBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.OpenPGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.TBCPadding;
import org.bouncycastle.crypto.paddings.X923Padding;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.crypto.params.RC2Parameters;
import org.bouncycastle.crypto.params.RC5Parameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseWrapCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.BlockCipherProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.bouncycastle.jcajce.spec.GOST28147ParameterSpec;
import org.bouncycastle.jcajce.spec.RepeatedSecretKeySpec;
import org.bouncycastle.util.Strings;

public class BaseBlockCipher
extends BaseWrapCipher
implements PBE {
    private static final Class gcmSpecClass = BaseBlockCipher.lookup("javax.crypto.spec.GCMParameterSpec");
    private AEADParameters aeadParams;
    private Class[] availableSpecs;
    private BlockCipher baseEngine;
    private GenericBlockCipher cipher;
    private BlockCipherProvider engineProvider;
    private int ivLength;
    private ParametersWithIV ivParam;
    private String modeName;
    private boolean padded;
    private String pbeAlgorithm;
    private PBEParameterSpec pbeSpec;

    protected BaseBlockCipher(BlockCipher blockCipher) {
        Class[] arrclass = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class, gcmSpecClass};
        this.availableSpecs = arrclass;
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = blockCipher;
        this.cipher = new BufferedGenericBlockCipher(blockCipher);
    }

    protected BaseBlockCipher(BlockCipher blockCipher, int n) {
        Class[] arrclass = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class, gcmSpecClass};
        this.availableSpecs = arrclass;
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = blockCipher;
        this.cipher = new BufferedGenericBlockCipher(blockCipher);
        this.ivLength = n / 8;
    }

    protected BaseBlockCipher(BufferedBlockCipher bufferedBlockCipher, int n) {
        Class[] arrclass = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class, gcmSpecClass};
        this.availableSpecs = arrclass;
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = bufferedBlockCipher.getUnderlyingCipher();
        this.cipher = new BufferedGenericBlockCipher(bufferedBlockCipher);
        this.ivLength = n / 8;
    }

    protected BaseBlockCipher(AEADBlockCipher aEADBlockCipher) {
        Class[] arrclass = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class, gcmSpecClass};
        this.availableSpecs = arrclass;
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = aEADBlockCipher.getUnderlyingCipher();
        this.ivLength = this.baseEngine.getBlockSize();
        this.cipher = new AEADGenericBlockCipher(aEADBlockCipher);
    }

    protected BaseBlockCipher(BlockCipherProvider blockCipherProvider) {
        Class[] arrclass = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class, gcmSpecClass};
        this.availableSpecs = arrclass;
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = blockCipherProvider.get();
        this.engineProvider = blockCipherProvider;
        this.cipher = new BufferedGenericBlockCipher(blockCipherProvider.get());
    }

    private boolean isAEADModeName(String string) {
        return "CCM".equals((Object)string) || "EAX".equals((Object)string) || "GCM".equals((Object)string) || "OCB".equals((Object)string);
    }

    private static Class lookup(String string) {
        try {
            Class class_ = BaseBlockCipher.class.getClassLoader().loadClass(string);
            return class_;
        }
        catch (Exception exception) {
            return null;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected int engineDoFinal(byte[] var1_1, int var2_2, int var3_3, byte[] var4_4, int var5_5) {
        var6_6 = 0;
        if (var3_3 == 0) ** GOTO lbl5
        try {
            var6_6 = this.cipher.processBytes(var1_1, var2_2, var3_3, var4_4, var5_5);
lbl5: // 2 sources:
            var9_7 = this.cipher.doFinal(var4_4, var5_5 + var6_6);
        }
        catch (OutputLengthException var8_8) {
            throw new ShortBufferException(var8_8.getMessage());
        }
        catch (DataLengthException var7_9) {
            throw new IllegalBlockSizeException(var7_9.getMessage());
        }
        return var6_6 + var9_7;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
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
        int n5 = n3 + n4;
        if (n5 == arrby2.length) {
            return arrby2;
        }
        byte[] arrby3 = new byte[n5];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)0, (int)n5);
        return arrby3;
    }

    @Override
    protected int engineGetBlockSize() {
        return this.baseEngine.getBlockSize();
    }

    @Override
    protected byte[] engineGetIV() {
        if (this.aeadParams != null) {
            return this.aeadParams.getNonce();
        }
        if (this.ivParam != null) {
            return this.ivParam.getIV();
        }
        return null;
    }

    @Override
    protected int engineGetKeySize(Key key) {
        return 8 * key.getEncoded().length;
    }

    @Override
    protected int engineGetOutputSize(int n) {
        return this.cipher.getOutputSize(n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams != null) return this.engineParams;
        if (this.pbeSpec != null) {
            try {
                this.engineParams = this.createParametersInstance(this.pbeAlgorithm);
                this.engineParams.init((AlgorithmParameterSpec)this.pbeSpec);
                return this.engineParams;
            }
            catch (Exception exception) {
                return null;
            }
        }
        if (this.ivParam != null) {
            String string = this.cipher.getUnderlyingCipher().getAlgorithmName();
            if (string.indexOf(47) >= 0) {
                string = string.substring(0, string.indexOf(47));
            }
            try {
                this.engineParams = this.createParametersInstance(string);
                this.engineParams.init(this.ivParam.getIV());
                return this.engineParams;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception.toString());
            }
        }
        if (this.aeadParams == null) return this.engineParams;
        try {
            this.engineParams = this.createParametersInstance("GCM");
            this.engineParams.init(new GCMParameters(this.aeadParams.getNonce(), this.aeadParams.getMacSize()).getEncoded());
            return this.engineParams;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void engineInit(int n, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        AlgorithmParameterSpec algorithmParameterSpec;
        if (algorithmParameters == null) {
            algorithmParameterSpec = null;
        } else {
            block6 : {
                for (int i = 0; i != this.availableSpecs.length; ++i) {
                    if (this.availableSpecs[i] == null) continue;
                    try {
                        AlgorithmParameterSpec algorithmParameterSpec2;
                        algorithmParameterSpec = algorithmParameterSpec2 = algorithmParameters.getParameterSpec(this.availableSpecs[i]);
                        break block6;
                    }
                    catch (Exception exception) {}
                }
                algorithmParameterSpec = null;
            }
            if (algorithmParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
            }
        }
        this.engineInit(n, key, algorithmParameterSpec, secureRandom);
        this.engineParams = algorithmParameters;
    }

    @Override
    protected void engineInit(int n, Key key, SecureRandom secureRandom) {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException(invalidAlgorithmParameterException.getMessage());
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected void engineInit(int var1_1, Key var2_2, AlgorithmParameterSpec var3_3, SecureRandom var4_4) {
        block32 : {
            block29 : {
                block30 : {
                    block36 : {
                        block35 : {
                            block39 : {
                                block38 : {
                                    block42 : {
                                        block41 : {
                                            block40 : {
                                                block37 : {
                                                    block34 : {
                                                        block33 : {
                                                            block31 : {
                                                                block26 : {
                                                                    block27 : {
                                                                        block28 : {
                                                                            this.pbeSpec = null;
                                                                            this.pbeAlgorithm = null;
                                                                            this.engineParams = null;
                                                                            this.aeadParams = null;
                                                                            if (!(var2_2 instanceof SecretKey)) {
                                                                                throw new InvalidKeyException("Key for algorithm " + var2_2.getAlgorithm() + " not suitable for symmetric enryption.");
                                                                            }
                                                                            if (var3_3 == null && this.baseEngine.getAlgorithmName().startsWith("RC5-64")) {
                                                                                throw new InvalidAlgorithmParameterException("RC5 requires an RC5ParametersSpec to be passed in.");
                                                                            }
                                                                            if (!(var2_2 instanceof BCPBEKey)) break block26;
                                                                            var27_5 = (BCPBEKey)var2_2;
                                                                            this.pbeAlgorithm = var27_5.getOID() != null ? var27_5.getOID().getId() : var27_5.getAlgorithm();
                                                                            if (var27_5.getParam() == null) break block27;
                                                                            var29_6 = var27_5.getParam();
                                                                            if (!(var3_3 instanceof IvParameterSpec)) break block28;
                                                                            var28_7 = new ParametersWithIV(var29_6, ((IvParameterSpec)var3_3).getIV());
                                                                            break block29;
                                                                        }
                                                                        if (var3_3 instanceof GOST28147ParameterSpec) {
                                                                            var30_14 = (GOST28147ParameterSpec)var3_3;
                                                                            var31_15 = new ParametersWithSBox(var29_6, var30_14.getSbox());
                                                                            var32_16 /* !! */  = var30_14.getIV() != null && this.ivLength != 0 ? new ParametersWithIV(var31_15, var30_14.getIV()) : var31_15;
                                                                        }
                                                                        break block30;
                                                                    }
                                                                    if (var3_3 instanceof PBEParameterSpec == false) throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                                                                    this.pbeSpec = (PBEParameterSpec)var3_3;
                                                                    var28_7 = PBE.Util.makePBEParameters(var27_5, var3_3, this.cipher.getUnderlyingCipher().getAlgorithmName());
                                                                    break block29;
                                                                }
                                                                if (var3_3 != null) break block31;
                                                                var9_8 /* !! */  = new KeyParameter(var2_2.getEncoded());
                                                                break block32;
                                                            }
                                                            if (!(var3_3 instanceof IvParameterSpec)) break block33;
                                                            if (this.ivLength != 0) {
                                                                var25_17 = (IvParameterSpec)var3_3;
                                                                if (var25_17.getIV().length != this.ivLength && !this.isAEADModeName(this.modeName)) {
                                                                    throw new InvalidAlgorithmParameterException("IV must be " + this.ivLength + " bytes long.");
                                                                }
                                                                this.ivParam = var2_2 instanceof RepeatedSecretKeySpec != false ? (var26_18 = new ParametersWithIV(null, var25_17.getIV())) : (var26_18 = new ParametersWithIV(new KeyParameter(var2_2.getEncoded()), var25_17.getIV()));
                                                                var9_8 /* !! */  = var26_18;
                                                            } else {
                                                                if (this.modeName != null && this.modeName.equals((Object)"ECB")) {
                                                                    throw new InvalidAlgorithmParameterException("ECB mode does not use an IV");
                                                                }
                                                                var9_8 /* !! */  = new KeyParameter(var2_2.getEncoded());
                                                            }
                                                            break block32;
                                                        }
                                                        if (!(var3_3 instanceof GOST28147ParameterSpec)) break block34;
                                                        var22_19 = (GOST28147ParameterSpec)var3_3;
                                                        var23_20 = new ParametersWithSBox(new KeyParameter(var2_2.getEncoded()), ((GOST28147ParameterSpec)var3_3).getSbox());
                                                        if (var22_19.getIV() == null || this.ivLength == 0) break block35;
                                                        var24_21 /* !! */  = new ParametersWithIV(var23_20, var22_19.getIV());
                                                        this.ivParam = (ParametersWithIV)var24_21 /* !! */ ;
                                                        break block36;
                                                    }
                                                    if (!(var3_3 instanceof RC2ParameterSpec)) break block37;
                                                    var19_22 = (RC2ParameterSpec)var3_3;
                                                    var20_23 = new RC2Parameters(var2_2.getEncoded(), ((RC2ParameterSpec)var3_3).getEffectiveKeyBits());
                                                    if (var19_22.getIV() == null || this.ivLength == 0) break block38;
                                                    var21_24 /* !! */  = new ParametersWithIV(var20_23, var19_22.getIV());
                                                    this.ivParam = (ParametersWithIV)var21_24 /* !! */ ;
                                                    break block39;
                                                }
                                                if (!(var3_3 instanceof RC5ParameterSpec)) break block40;
                                                var16_25 = (RC5ParameterSpec)var3_3;
                                                var17_26 = new RC5Parameters(var2_2.getEncoded(), ((RC5ParameterSpec)var3_3).getRounds());
                                                if (this.baseEngine.getAlgorithmName().startsWith("RC5") == false) throw new InvalidAlgorithmParameterException("RC5 parameters passed to a cipher that is not RC5.");
                                                if (this.baseEngine.getAlgorithmName().equals((Object)"RC5-32")) {
                                                    if (var16_25.getWordSize() != 32) {
                                                        throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 32 not " + var16_25.getWordSize() + ".");
                                                    }
                                                } else if (this.baseEngine.getAlgorithmName().equals((Object)"RC5-64") && var16_25.getWordSize() != 64) {
                                                    throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 64 not " + var16_25.getWordSize() + ".");
                                                }
                                                if (var16_25.getIV() == null || this.ivLength == 0) break block41;
                                                var18_27 /* !! */  = new ParametersWithIV(var17_26, var16_25.getIV());
                                                this.ivParam = (ParametersWithIV)var18_27 /* !! */ ;
                                                break block42;
                                            }
                                            if (BaseBlockCipher.gcmSpecClass == null) throw new InvalidAlgorithmParameterException("unknown parameter type.");
                                            if (BaseBlockCipher.gcmSpecClass.isInstance((Object)var3_3) == false) throw new InvalidAlgorithmParameterException("unknown parameter type.");
                                            if (!this.isAEADModeName(this.modeName) && !(this.cipher instanceof AEADGenericBlockCipher)) {
                                                throw new InvalidAlgorithmParameterException("GCMParameterSpec can only be used with AEAD modes.");
                                            }
                                            try {
                                                var6_28 = BaseBlockCipher.gcmSpecClass.getDeclaredMethod("getTLen", new Class[0]);
                                                var7_29 = BaseBlockCipher.gcmSpecClass.getDeclaredMethod("getIV", new Class[0]);
                                                if (var2_2 instanceof RepeatedSecretKeySpec) {
                                                    this.aeadParams = var8_30 = new AEADParameters(null, (Integer)var6_28.invoke((Object)var3_3, new Object[0]), (byte[])var7_29.invoke((Object)var3_3, new Object[0]));
                                                    var9_8 /* !! */  = var8_30;
                                                }
                                                this.aeadParams = var15_31 = new AEADParameters(new KeyParameter(var2_2.getEncoded()), (Integer)var6_28.invoke((Object)var3_3, new Object[0]), (byte[])var7_29.invoke((Object)var3_3, new Object[0]));
                                                var9_8 /* !! */  = var15_31;
                                            }
                                            catch (Exception var5_32) {
                                                throw new InvalidAlgorithmParameterException("Cannot process GCMParameterSpec.");
                                            }
                                        }
                                        var18_27 /* !! */  = var17_26;
                                    }
                                    var9_8 /* !! */  = var18_27 /* !! */ ;
                                    break block32;
                                }
                                var21_24 /* !! */  = var20_23;
                            }
                            var9_8 /* !! */  = var21_24 /* !! */ ;
                            break block32;
                        }
                        var24_21 /* !! */  = var23_20;
                    }
                    var9_8 /* !! */  = var24_21 /* !! */ ;
                    break block32;
                    var28_7 = var32_16 /* !! */ ;
                    break block29;
                }
                var28_7 = var29_6;
            }
            if (var28_7 instanceof ParametersWithIV) {
                this.ivParam = (ParametersWithIV)var28_7;
            }
            var9_8 /* !! */  = var28_7;
        }
        if (this.ivLength == 0 || var9_8 /* !! */  instanceof ParametersWithIV || var9_8 /* !! */  instanceof AEADParameters) ** GOTO lbl127
        var13_9 = var4_4 == null ? new SecureRandom() : var4_4;
        if (var1_1 == 1 || var1_1 == 3) {
            var14_10 = new byte[this.ivLength];
            var13_9.nextBytes(var14_10);
            var10_11 /* !! */  = new ParametersWithIV(var9_8 /* !! */ , var14_10);
            this.ivParam = (ParametersWithIV)var10_11 /* !! */ ;
        } else {
            if (this.cipher.getUnderlyingCipher().getAlgorithmName().indexOf("PGPCFB") < 0) {
                throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
lbl127: // 3 sources:
            var10_11 /* !! */  = var9_8 /* !! */ ;
        }
        var11_12 /* !! */  = var4_4 != null && this.padded != false ? new ParametersWithRandom(var10_11 /* !! */ , var4_4) : var10_11 /* !! */ ;
        switch (var1_1) {
            default: {
                throw new InvalidParameterException("unknown opmode " + var1_1 + " passed");
            }
            case 1: 
            case 3: {
                try {
                    this.cipher.init(true, var11_12 /* !! */ );
                    return;
                }
                catch (Exception var12_13) {
                    throw new InvalidKeyException(var12_13.getMessage());
                }
            }
            case 2: 
            case 4: 
        }
        this.cipher.init(false, var11_12 /* !! */ );
    }

    @Override
    protected void engineSetMode(String string) {
        this.modeName = Strings.toUpperCase(string);
        if (this.modeName.equals((Object)"ECB")) {
            this.ivLength = 0;
            this.cipher = new BufferedGenericBlockCipher(this.baseEngine);
            return;
        }
        if (this.modeName.equals((Object)"CBC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new CBCBlockCipher(this.baseEngine));
            return;
        }
        if (this.modeName.startsWith("OFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                int n = Integer.parseInt((String)this.modeName.substring(3));
                this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, n));
                return;
            }
            this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
            return;
        }
        if (this.modeName.startsWith("CFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                int n = Integer.parseInt((String)this.modeName.substring(3));
                this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, n));
                return;
            }
            this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
            return;
        }
        if (this.modeName.startsWith("PGP")) {
            boolean bl = this.modeName.equalsIgnoreCase("PGPCFBwithIV");
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new PGPCFBBlockCipher(this.baseEngine, bl));
            return;
        }
        if (this.modeName.equalsIgnoreCase("OpenPGPCFB")) {
            this.ivLength = 0;
            this.cipher = new BufferedGenericBlockCipher(new OpenPGPCFBBlockCipher(this.baseEngine));
            return;
        }
        if (this.modeName.startsWith("SIC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.ivLength < 16) {
                throw new IllegalArgumentException("Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)");
            }
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
            return;
        }
        if (this.modeName.startsWith("CTR")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
            return;
        }
        if (this.modeName.startsWith("GOFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new GOFBBlockCipher(this.baseEngine)));
            return;
        }
        if (this.modeName.startsWith("GCFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new GCFBBlockCipher(this.baseEngine)));
            return;
        }
        if (this.modeName.startsWith("CTS")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(new CBCBlockCipher(this.baseEngine)));
            return;
        }
        if (this.modeName.startsWith("CCM")) {
            this.ivLength = 13;
            this.cipher = new AEADGenericBlockCipher(new CCMBlockCipher(this.baseEngine));
            return;
        }
        if (this.modeName.startsWith("OCB")) {
            if (this.engineProvider != null) {
                this.ivLength = 15;
                this.cipher = new AEADGenericBlockCipher(new OCBBlockCipher(this.baseEngine, this.engineProvider.get()));
                return;
            }
            throw new NoSuchAlgorithmException("can't support mode " + string);
        }
        if (this.modeName.startsWith("EAX")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new EAXBlockCipher(this.baseEngine));
            return;
        }
        if (this.modeName.startsWith("GCM")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new GCMBlockCipher(this.baseEngine));
            return;
        }
        throw new NoSuchAlgorithmException("can't support mode " + string);
    }

    @Override
    protected void engineSetPadding(String string) {
        String string2 = Strings.toUpperCase(string);
        if (string2.equals((Object)"NOPADDING")) {
            if (this.cipher.wrapOnNoPadding()) {
                this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(this.cipher.getUnderlyingCipher()));
            }
            return;
        }
        if (string2.equals((Object)"WITHCTS")) {
            this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(this.cipher.getUnderlyingCipher()));
            return;
        }
        this.padded = true;
        if (this.isAEADModeName(this.modeName)) {
            throw new NoSuchPaddingException("Only NoPadding can be used with AEAD modes.");
        }
        if (string2.equals((Object)"PKCS5PADDING") || string2.equals((Object)"PKCS7PADDING")) {
            this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher());
            return;
        }
        if (string2.equals((Object)"ZEROBYTEPADDING")) {
            this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ZeroBytePadding());
            return;
        }
        if (string2.equals((Object)"ISO10126PADDING") || string2.equals((Object)"ISO10126-2PADDING")) {
            this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO10126d2Padding());
            return;
        }
        if (string2.equals((Object)"X9.23PADDING") || string2.equals((Object)"X923PADDING")) {
            this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new X923Padding());
            return;
        }
        if (string2.equals((Object)"ISO7816-4PADDING") || string2.equals((Object)"ISO9797-1PADDING")) {
            this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO7816d4Padding());
            return;
        }
        if (string2.equals((Object)"TBCPADDING")) {
            this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new TBCPadding());
            return;
        }
        throw new NoSuchPaddingException("Padding " + string + " unknown.");
    }

    @Override
    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        try {
            int n4 = this.cipher.processBytes(arrby, n, n2, arrby2, n3);
            return n4;
        }
        catch (DataLengthException dataLengthException) {
            throw new ShortBufferException(dataLengthException.getMessage());
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        int n3 = this.cipher.getUpdateOutputSize(n2);
        if (n3 > 0) {
            byte[] arrby2 = new byte[n3];
            int n4 = this.cipher.processBytes(arrby, n, n2, arrby2, 0);
            if (n4 == 0) {
                return null;
            }
            if (n4 == arrby2.length) return arrby2;
            byte[] arrby3 = new byte[n4];
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)0, (int)n4);
            return arrby3;
        }
        this.cipher.processBytes(arrby, n, n2, null, 0);
        return null;
    }

    protected void engineUpdateAAD(ByteBuffer byteBuffer) {
        int n = byteBuffer.arrayOffset() + byteBuffer.position();
        int n2 = byteBuffer.limit() - byteBuffer.position();
        this.engineUpdateAAD(byteBuffer.array(), n, n2);
    }

    protected void engineUpdateAAD(byte[] arrby, int n, int n2) {
        this.cipher.updateAAD(arrby, n, n2);
    }

    private static class AEADGenericBlockCipher
    implements GenericBlockCipher {
        private static final Constructor aeadBadTagConstructor;
        private AEADBlockCipher cipher;

        static {
            Class class_ = BaseBlockCipher.lookup("javax.crypto.AEADBadTagException");
            aeadBadTagConstructor = class_ != null ? AEADGenericBlockCipher.findExceptionConstructor(class_) : null;
        }

        AEADGenericBlockCipher(AEADBlockCipher aEADBlockCipher) {
            this.cipher = aEADBlockCipher;
        }

        private static Constructor findExceptionConstructor(Class class_) {
            try {
                Constructor constructor = class_.getConstructor(new Class[]{String.class});
                return constructor;
            }
            catch (Exception exception) {
                return null;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public int doFinal(byte[] arrby, int n) {
            try {
                return this.cipher.doFinal(arrby, n);
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                BadPaddingException badPaddingException;
                if (aeadBadTagConstructor == null) throw new BadPaddingException(invalidCipherTextException.getMessage());
                try {
                    Constructor constructor = aeadBadTagConstructor;
                    Object[] arrobject = new Object[]{invalidCipherTextException.getMessage()};
                    badPaddingException = (BadPaddingException)((Object)constructor.newInstance(arrobject));
                }
                catch (Exception exception) {
                    badPaddingException = null;
                }
                if (badPaddingException == null) throw new BadPaddingException(invalidCipherTextException.getMessage());
                throw badPaddingException;
            }
        }

        @Override
        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }

        @Override
        public int getOutputSize(int n) {
            return this.cipher.getOutputSize(n);
        }

        @Override
        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }

        @Override
        public int getUpdateOutputSize(int n) {
            return this.cipher.getUpdateOutputSize(n);
        }

        @Override
        public void init(boolean bl, CipherParameters cipherParameters) {
            this.cipher.init(bl, cipherParameters);
        }

        @Override
        public int processByte(byte by, byte[] arrby, int n) {
            return this.cipher.processByte(by, arrby, n);
        }

        @Override
        public int processBytes(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
            return this.cipher.processBytes(arrby, n, n2, arrby2, n3);
        }

        @Override
        public void updateAAD(byte[] arrby, int n, int n2) {
            this.cipher.processAADBytes(arrby, n, n2);
        }

        @Override
        public boolean wrapOnNoPadding() {
            return false;
        }
    }

    private static class BufferedGenericBlockCipher
    implements GenericBlockCipher {
        private BufferedBlockCipher cipher;

        BufferedGenericBlockCipher(BlockCipher blockCipher) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        }

        BufferedGenericBlockCipher(BlockCipher blockCipher, BlockCipherPadding blockCipherPadding) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher, blockCipherPadding);
        }

        BufferedGenericBlockCipher(BufferedBlockCipher bufferedBlockCipher) {
            this.cipher = bufferedBlockCipher;
        }

        @Override
        public int doFinal(byte[] arrby, int n) {
            try {
                int n2 = this.cipher.doFinal(arrby, n);
                return n2;
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                throw new BadPaddingException(invalidCipherTextException.getMessage());
            }
        }

        @Override
        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }

        @Override
        public int getOutputSize(int n) {
            return this.cipher.getOutputSize(n);
        }

        @Override
        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }

        @Override
        public int getUpdateOutputSize(int n) {
            return this.cipher.getUpdateOutputSize(n);
        }

        @Override
        public void init(boolean bl, CipherParameters cipherParameters) {
            this.cipher.init(bl, cipherParameters);
        }

        @Override
        public int processByte(byte by, byte[] arrby, int n) {
            return this.cipher.processByte(by, arrby, n);
        }

        @Override
        public int processBytes(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
            return this.cipher.processBytes(arrby, n, n2, arrby2, n3);
        }

        @Override
        public void updateAAD(byte[] arrby, int n, int n2) {
            throw new UnsupportedOperationException("AAD is not supported in the current mode.");
        }

        @Override
        public boolean wrapOnNoPadding() {
            return !(this.cipher instanceof CTSBlockCipher);
        }
    }

    private static interface GenericBlockCipher {
        public int doFinal(byte[] var1, int var2);

        public String getAlgorithmName();

        public int getOutputSize(int var1);

        public BlockCipher getUnderlyingCipher();

        public int getUpdateOutputSize(int var1);

        public void init(boolean var1, CipherParameters var2);

        public int processByte(byte var1, byte[] var2, int var3);

        public int processBytes(byte[] var1, int var2, int var3, byte[] var4, int var5);

        public void updateAAD(byte[] var1, int var2, int var3);

        public boolean wrapOnNoPadding();
    }

}

