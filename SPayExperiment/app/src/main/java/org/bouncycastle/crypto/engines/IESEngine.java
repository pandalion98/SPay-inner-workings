/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.engines;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.EphemeralKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.generators.EphemeralKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Pack;

public class IESEngine {
    private byte[] IV;
    byte[] V;
    BasicAgreement agree;
    BufferedBlockCipher cipher;
    boolean forEncryption;
    DerivationFunction kdf;
    private EphemeralKeyPairGenerator keyPairGenerator;
    private KeyParser keyParser;
    Mac mac;
    byte[] macBuf;
    IESParameters param;
    CipherParameters privParam;
    CipherParameters pubParam;

    public IESEngine(BasicAgreement basicAgreement, DerivationFunction derivationFunction, Mac mac) {
        this.agree = basicAgreement;
        this.kdf = derivationFunction;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = null;
    }

    public IESEngine(BasicAgreement basicAgreement, DerivationFunction derivationFunction, Mac mac, BufferedBlockCipher bufferedBlockCipher) {
        this.agree = basicAgreement;
        this.kdf = derivationFunction;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = bufferedBlockCipher;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] decryptBlock(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        byte[] arrby3;
        int n4;
        if (n3 <= this.param.getMacKeySize() / 8) {
            throw new InvalidCipherTextException("Length of input must be greater than the MAC");
        }
        if (this.cipher == null) {
            byte[] arrby4 = new byte[n3 - this.V.length - this.mac.getMacSize()];
            arrby2 = new byte[this.param.getMacKeySize() / 8];
            byte[] arrby5 = new byte[arrby4.length + arrby2.length];
            this.kdf.generateBytes(arrby5, 0, arrby5.length);
            if (this.V.length != 0) {
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
                System.arraycopy((Object)arrby5, (int)arrby2.length, (Object)arrby4, (int)0, (int)arrby4.length);
            } else {
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby4, (int)0, (int)arrby4.length);
                System.arraycopy((Object)arrby5, (int)arrby4.length, (Object)arrby2, (int)0, (int)arrby2.length);
            }
            arrby3 = new byte[arrby4.length];
            for (int i2 = 0; i2 != arrby4.length; ++i2) {
                arrby3[i2] = (byte)(arrby[i2 + (n2 + this.V.length)] ^ arrby4[i2]);
            }
            n4 = arrby4.length;
        } else {
            byte[] arrby6 = new byte[((IESWithCipherParameters)this.param).getCipherKeySize() / 8];
            byte[] arrby7 = new byte[this.param.getMacKeySize() / 8];
            byte[] arrby8 = new byte[arrby6.length + arrby7.length];
            this.kdf.generateBytes(arrby8, 0, arrby8.length);
            System.arraycopy((Object)arrby8, (int)0, (Object)arrby6, (int)0, (int)arrby6.length);
            System.arraycopy((Object)arrby8, (int)arrby6.length, (Object)arrby7, (int)0, (int)arrby7.length);
            if (this.IV != null) {
                this.cipher.init(false, new ParametersWithIV(new KeyParameter(arrby6), this.IV));
            } else {
                this.cipher.init(false, new KeyParameter(arrby6));
            }
            arrby3 = new byte[this.cipher.getOutputSize(n3 - this.V.length - this.mac.getMacSize())];
            int n5 = this.cipher.processBytes(arrby, n2 + this.V.length, n3 - this.V.length - this.mac.getMacSize(), arrby3, 0);
            n4 = n5 + this.cipher.doFinal(arrby3, n5);
            arrby2 = arrby7;
        }
        byte[] arrby9 = this.param.getEncodingV();
        byte[] arrby10 = new byte[4];
        if (this.V.length != 0 && arrby9 != null) {
            Pack.intToBigEndian((int)(8 * arrby9.length), (byte[])arrby10, (int)0);
        }
        int n6 = n2 + n3;
        byte[] arrby11 = Arrays.copyOfRange((byte[])arrby, (int)(n6 - this.mac.getMacSize()), (int)n6);
        byte[] arrby12 = new byte[arrby11.length];
        this.mac.init(new KeyParameter(arrby2));
        this.mac.update(arrby, n2 + this.V.length, n3 - this.V.length - arrby12.length);
        if (arrby9 != null) {
            this.mac.update(arrby9, 0, arrby9.length);
        }
        if (this.V.length != 0) {
            this.mac.update(arrby10, 0, arrby10.length);
        }
        this.mac.doFinal(arrby12, 0);
        if (!Arrays.constantTimeAreEqual((byte[])arrby11, (byte[])arrby12)) {
            throw new InvalidCipherTextException("Invalid MAC.");
        }
        return Arrays.copyOfRange((byte[])arrby3, (int)0, (int)n4);
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] encryptBlock(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        byte[] arrby3;
        if (this.cipher == null) {
            byte[] arrby4 = new byte[n3];
            arrby3 = new byte[this.param.getMacKeySize() / 8];
            byte[] arrby5 = new byte[arrby4.length + arrby3.length];
            this.kdf.generateBytes(arrby5, 0, arrby5.length);
            if (this.V.length != 0) {
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby3, (int)0, (int)arrby3.length);
                System.arraycopy((Object)arrby5, (int)arrby3.length, (Object)arrby4, (int)0, (int)arrby4.length);
            } else {
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby4, (int)0, (int)arrby4.length);
                System.arraycopy((Object)arrby5, (int)n3, (Object)arrby3, (int)0, (int)arrby3.length);
            }
            arrby2 = new byte[n3];
            for (int i2 = 0; i2 != n3; ++i2) {
                arrby2[i2] = (byte)(arrby[n2 + i2] ^ arrby4[i2]);
            }
        } else {
            byte[] arrby6 = new byte[((IESWithCipherParameters)this.param).getCipherKeySize() / 8];
            byte[] arrby7 = new byte[this.param.getMacKeySize() / 8];
            byte[] arrby8 = new byte[arrby6.length + arrby7.length];
            this.kdf.generateBytes(arrby8, 0, arrby8.length);
            System.arraycopy((Object)arrby8, (int)0, (Object)arrby6, (int)0, (int)arrby6.length);
            System.arraycopy((Object)arrby8, (int)arrby6.length, (Object)arrby7, (int)0, (int)arrby7.length);
            if (this.IV != null) {
                this.cipher.init(true, new ParametersWithIV(new KeyParameter(arrby6), this.IV));
            } else {
                this.cipher.init(true, new KeyParameter(arrby6));
            }
            arrby2 = new byte[this.cipher.getOutputSize(n3)];
            int n4 = this.cipher.processBytes(arrby, n2, n3, arrby2, 0);
            n3 = n4 + this.cipher.doFinal(arrby2, n4);
            arrby3 = arrby7;
        }
        byte[] arrby9 = this.param.getEncodingV();
        byte[] arrby10 = new byte[4];
        if (this.V.length != 0 && arrby9 != null) {
            Pack.intToBigEndian((int)(8 * arrby9.length), (byte[])arrby10, (int)0);
        }
        byte[] arrby11 = new byte[this.mac.getMacSize()];
        this.mac.init(new KeyParameter(arrby3));
        this.mac.update(arrby2, 0, arrby2.length);
        if (arrby9 != null) {
            this.mac.update(arrby9, 0, arrby9.length);
        }
        if (this.V.length != 0) {
            this.mac.update(arrby10, 0, arrby10.length);
        }
        this.mac.doFinal(arrby11, 0);
        byte[] arrby12 = new byte[n3 + this.V.length + arrby11.length];
        System.arraycopy((Object)this.V, (int)0, (Object)arrby12, (int)0, (int)this.V.length);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby12, (int)this.V.length, (int)n3);
        System.arraycopy((Object)arrby11, (int)0, (Object)arrby12, (int)(n3 + this.V.length), (int)arrby11.length);
        return arrby12;
    }

    private void extractParams(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            this.IV = ((ParametersWithIV)cipherParameters).getIV();
            this.param = (IESParameters)((ParametersWithIV)cipherParameters).getParameters();
            return;
        }
        this.IV = null;
        this.param = (IESParameters)cipherParameters;
    }

    public BufferedBlockCipher getCipher() {
        return this.cipher;
    }

    public Mac getMac() {
        return this.mac;
    }

    public void init(AsymmetricKeyParameter asymmetricKeyParameter, CipherParameters cipherParameters, KeyParser keyParser) {
        this.forEncryption = false;
        this.privParam = asymmetricKeyParameter;
        this.keyParser = keyParser;
        this.extractParams(cipherParameters);
    }

    public void init(AsymmetricKeyParameter asymmetricKeyParameter, CipherParameters cipherParameters, EphemeralKeyPairGenerator ephemeralKeyPairGenerator) {
        this.forEncryption = true;
        this.pubParam = asymmetricKeyParameter;
        this.keyPairGenerator = ephemeralKeyPairGenerator;
        this.extractParams(cipherParameters);
    }

    public void init(boolean bl, CipherParameters cipherParameters, CipherParameters cipherParameters2, CipherParameters cipherParameters3) {
        this.forEncryption = bl;
        this.privParam = cipherParameters;
        this.pubParam = cipherParameters2;
        this.V = new byte[0];
        this.extractParams(cipherParameters3);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] processBlock(byte[] var1_1, int var2_2, int var3_3) {
        if (this.forEncryption) {
            if (this.keyPairGenerator != null) {
                var14_4 = this.keyPairGenerator.generate();
                this.privParam = var14_4.getKeyPair().getPrivate();
                this.V = var14_4.getEncodedPublicKey();
            }
        } else if (this.keyParser != null) {
            var4_11 = new ByteArrayInputStream(var1_1, var2_2, var3_3);
            this.pubParam = this.keyParser.readKey((InputStream)var4_11);
            this.V = Arrays.copyOfRange((byte[])var1_1, (int)var2_2, (int)(var2_2 + (var3_3 - var4_11.available())));
        }
        this.agree.init(this.privParam);
        var6_5 = this.agree.calculateAgreement(this.pubParam);
        var7_6 = BigIntegers.asUnsignedByteArray((int)this.agree.getFieldSize(), (BigInteger)var6_5);
        if (this.V.length != 0) {
            var13_7 = Arrays.concatenate((byte[])this.V, (byte[])var7_6);
            Arrays.fill((byte[])var7_6, (byte)0);
            var7_6 = var13_7;
        }
        var8_8 = new KDFParameters(var7_6, this.param.getDerivationV());
        this.kdf.init(var8_8);
        if (this.forEncryption) {
            var11_10 = var12_9 = this.encryptBlock(var1_1, var2_2, var3_3);
            return var11_10;
        }
        ** break block12
        catch (IOException var5_12) {
            throw new InvalidCipherTextException("unable to recover ephemeral public key: " + var5_12.getMessage(), var5_12);
        }
lbl-1000: // 1 sources:
        {
            
            var10_13 = this.decryptBlock(var1_1, var2_2, var3_3);
            var11_10 = var10_13;
            return var11_10;
        }
        finally {
            Arrays.fill((byte[])var7_6, (byte)0);
        }
    }
}

