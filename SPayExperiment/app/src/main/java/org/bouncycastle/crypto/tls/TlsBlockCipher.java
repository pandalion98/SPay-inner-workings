/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsMac;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsBlockCipher
implements TlsCipher {
    protected TlsContext context;
    protected BlockCipher decryptCipher;
    protected BlockCipher encryptCipher;
    protected boolean encryptThenMAC;
    protected byte[] randomData;
    protected TlsMac readMac;
    protected boolean useExplicitIV;
    protected TlsMac writeMac;

    /*
     * Enabled aggressive block sorting
     */
    public TlsBlockCipher(TlsContext tlsContext, BlockCipher blockCipher, BlockCipher blockCipher2, Digest digest, Digest digest2, int n2) {
        ParametersWithIV parametersWithIV;
        byte[] arrby;
        int n3;
        ParametersWithIV parametersWithIV2;
        byte[] arrby2;
        this.context = tlsContext;
        this.randomData = new byte[256];
        tlsContext.getNonceRandomGenerator().nextBytes(this.randomData);
        this.useExplicitIV = TlsUtils.isTLSv11(tlsContext);
        this.encryptThenMAC = tlsContext.getSecurityParameters().encryptThenMAC;
        int n4 = n2 * 2 + digest.getDigestSize() + digest2.getDigestSize();
        int n5 = !this.useExplicitIV ? n4 + (blockCipher.getBlockSize() + blockCipher2.getBlockSize()) : n4;
        byte[] arrby3 = TlsUtils.calculateKeyBlock(tlsContext, n5);
        TlsMac tlsMac = new TlsMac(tlsContext, digest, arrby3, 0, digest.getDigestSize());
        int n6 = 0 + digest.getDigestSize();
        TlsMac tlsMac2 = new TlsMac(tlsContext, digest2, arrby3, n6, digest2.getDigestSize());
        int n7 = n6 + digest2.getDigestSize();
        KeyParameter keyParameter = new KeyParameter(arrby3, n7, n2);
        int n8 = n7 + n2;
        KeyParameter keyParameter2 = new KeyParameter(arrby3, n8, n2);
        int n9 = n8 + n2;
        if (this.useExplicitIV) {
            byte[] arrby4 = new byte[blockCipher.getBlockSize()];
            byte[] arrby5 = new byte[blockCipher2.getBlockSize()];
            arrby = arrby4;
            arrby2 = arrby5;
            n3 = n9;
        } else {
            byte[] arrby6 = Arrays.copyOfRange((byte[])arrby3, (int)n9, (int)(n9 + blockCipher.getBlockSize()));
            int n10 = n9 + blockCipher.getBlockSize();
            byte[] arrby7 = Arrays.copyOfRange((byte[])arrby3, (int)n10, (int)(n10 + blockCipher2.getBlockSize()));
            n3 = n10 + blockCipher2.getBlockSize();
            arrby = arrby6;
            arrby2 = arrby7;
        }
        if (n3 != n5) {
            throw new TlsFatalAlert(80);
        }
        if (tlsContext.isServer()) {
            this.writeMac = tlsMac2;
            this.readMac = tlsMac;
            this.encryptCipher = blockCipher2;
            this.decryptCipher = blockCipher;
            parametersWithIV = new ParametersWithIV(keyParameter2, arrby2);
            parametersWithIV2 = new ParametersWithIV(keyParameter, arrby);
        } else {
            this.writeMac = tlsMac;
            this.readMac = tlsMac2;
            this.encryptCipher = blockCipher;
            this.decryptCipher = blockCipher2;
            parametersWithIV = new ParametersWithIV(keyParameter, arrby);
            parametersWithIV2 = new ParametersWithIV(keyParameter2, arrby2);
        }
        this.encryptCipher.init(true, parametersWithIV);
        this.decryptCipher.init(false, parametersWithIV2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected int checkPaddingConstantTime(byte[] var1_1, int var2_2, int var3_3, int var4_4, int var5_5) {
        var6_6 = var2_2 + var3_3;
        var7_7 = var1_1[var6_6 - 1];
        var8_8 = 1 + (var7_7 & 255);
        if (TlsUtils.isSSL(this.context) && var8_8 > var4_4 || var5_5 + var8_8 > var3_3) {
            var12_9 = 0;
            var13_10 = 0;
            var8_8 = 0;
lbl8: // 3 sources:
            do {
                var14_14 = this.randomData;
                do {
                    if (var13_10 >= 256) {
                        var14_14[0] = (byte)(var12_9 ^ var14_14[0]);
                        return var8_8;
                    }
                    var15_15 = var13_10 + 1;
                    var12_9 = (byte)(var12_9 | var7_7 ^ var14_14[var13_10]);
                    var13_10 = var15_15;
                } while (true);
                break;
            } while (true);
        }
        var9_11 = var6_6 - var8_8;
        var10_12 = 0;
        do {
            block5 : {
                block6 : {
                    var11_13 = var9_11 + 1;
                    var12_9 = (byte)(var10_12 | var7_7 ^ var1_1[var9_11]);
                    if (var11_13 < var6_6) break block5;
                    if (var12_9 == 0) break block6;
                    var13_10 = var8_8;
                    var8_8 = 0;
                    ** GOTO lbl8
                }
                var13_10 = var8_8;
                ** continue;
            }
            var10_12 = var12_9;
            var9_11 = var11_13;
        } while (true);
    }

    protected int chooseExtraPadBlocks(SecureRandom secureRandom, int n2) {
        return Math.min((int)this.lowestBitSet(secureRandom.nextInt()), (int)n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] decodeCiphertext(long l2, short s2, byte[] arrby, int n2, int n3) {
        int n4;
        int n5;
        int n6 = this.decryptCipher.getBlockSize();
        int n7 = this.readMac.getSize();
        int n8 = this.encryptThenMAC ? n6 + n7 : Math.max((int)n6, (int)(n7 + 1));
        if (this.useExplicitIV) {
            n8 += n6;
        }
        if (n3 < n8) {
            throw new TlsFatalAlert(50);
        }
        int n9 = this.encryptThenMAC ? n3 - n7 : n3;
        if (n9 % n6 != 0) {
            throw new TlsFatalAlert(21);
        }
        if (this.encryptThenMAC) {
            int n10 = n2 + n3;
            byte[] arrby2 = Arrays.copyOfRange((byte[])arrby, (int)(n10 - n7), (int)n10);
            boolean bl = !Arrays.constantTimeAreEqual((byte[])this.readMac.calculateMac(l2, s2, arrby, n2, n3 - n7), (byte[])arrby2);
            if (bl) {
                throw new TlsFatalAlert(20);
            }
        }
        if (this.useExplicitIV) {
            this.decryptCipher.init(false, new ParametersWithIV(null, arrby, n2, n6));
            n4 = n2 + n6;
            n5 = n9 - n6;
        } else {
            n5 = n9;
            n4 = n2;
        }
        for (int i2 = 0; i2 < n5; i2 += n6) {
            this.decryptCipher.processBlock(arrby, n4 + i2, arrby, n4 + i2);
        }
        int n11 = this.encryptThenMAC ? 0 : n7;
        int n12 = this.checkPaddingConstantTime(arrby, n4, n5, n6, n11);
        boolean bl = n12 == 0;
        int n13 = n5 - n12;
        if (!this.encryptThenMAC) {
            int n14 = n4 + (n13 -= n7);
            byte[] arrby3 = Arrays.copyOfRange((byte[])arrby, (int)n14, (int)(n14 + n7));
            TlsMac tlsMac = this.readMac;
            int n15 = n5 - n7;
            byte[] arrby4 = this.randomData;
            boolean bl2 = !Arrays.constantTimeAreEqual((byte[])tlsMac.calculateMacConstantTime(l2, s2, arrby, n4, n13, n15, arrby4), (byte[])arrby3);
            bl |= bl2;
        }
        if (bl) {
            throw new TlsFatalAlert(20);
        }
        return Arrays.copyOfRange((byte[])arrby, (int)n4, (int)(n4 + n13));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] encodePlaintext(long l2, short s2, byte[] arrby, int n2, int n3) {
        int n4;
        int n5;
        int n6 = this.encryptCipher.getBlockSize();
        int n7 = this.writeMac.getSize();
        ProtocolVersion protocolVersion = this.context.getServerVersion();
        int n8 = !this.encryptThenMAC ? n3 + n7 : n3;
        int n9 = n6 - 1 - n8 % n6;
        if (!protocolVersion.isDTLS() && !protocolVersion.isSSL()) {
            int n10 = (255 - n9) / n6;
            n9 += n6 * this.chooseExtraPadBlocks(this.context.getSecureRandom(), n10);
        }
        int n11 = 1 + (n9 + (n7 + n3));
        if (this.useExplicitIV) {
            n11 += n6;
        }
        byte[] arrby2 = new byte[n11];
        if (this.useExplicitIV) {
            byte[] arrby3 = new byte[n6];
            this.context.getNonceRandomGenerator().nextBytes(arrby3);
            this.encryptCipher.init(true, new ParametersWithIV(null, arrby3));
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)n6);
            n5 = 0 + n6;
        } else {
            n5 = 0;
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)n5, (int)n3);
        int n12 = n5 + n3;
        if (!this.encryptThenMAC) {
            byte[] arrby4 = this.writeMac.calculateMac(l2, s2, arrby, n2, n3);
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)n12, (int)arrby4.length);
            n4 = n12 + arrby4.length;
        } else {
            n4 = n12;
        }
        int n13 = n4;
        for (int i2 = 0; i2 <= n9; ++i2) {
            int n14 = n13 + 1;
            arrby2[n13] = (byte)n9;
            n13 = n14;
        }
        while (n5 < n13) {
            this.encryptCipher.processBlock(arrby2, n5, arrby2, n5);
            n5 += n6;
        }
        if (this.encryptThenMAC) {
            byte[] arrby5 = this.writeMac.calculateMac(l2, s2, arrby2, 0, n13);
            System.arraycopy((Object)arrby5, (int)0, (Object)arrby2, (int)n13, (int)arrby5.length);
            n13 + arrby5.length;
        }
        return arrby2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int getPlaintextLimit(int n2) {
        int n3;
        int n4 = this.encryptCipher.getBlockSize();
        int n5 = this.writeMac.getSize();
        if (this.useExplicitIV) {
            n2 -= n4;
        }
        if (this.encryptThenMAC) {
            int n6 = n2 - n5;
            n3 = n6 - n6 % n4;
            do {
                return n3 - 1;
                break;
            } while (true);
        }
        n3 = n2 - n2 % n4 - n5;
        return n3 - 1;
    }

    public TlsMac getReadMac() {
        return this.readMac;
    }

    public TlsMac getWriteMac() {
        return this.writeMac;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected int lowestBitSet(int n2) {
        if (n2 == 0) {
            return 32;
        }
        int n3 = 0;
        while ((n2 & 1) == 0) {
            ++n3;
            n2 >>= 1;
        }
        return n3;
    }
}

