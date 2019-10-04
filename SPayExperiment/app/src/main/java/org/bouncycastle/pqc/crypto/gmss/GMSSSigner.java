/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Array
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.gmss.GMSSDigestProvider;
import org.bouncycastle.pqc.crypto.gmss.GMSSKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSUtil;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSVerify;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature;
import org.bouncycastle.util.Arrays;

public class GMSSSigner
implements MessageSigner {
    private byte[][][] currentAuthPaths;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSRandom gmssRandom;
    private GMSSUtil gmssUtil = new GMSSUtil();
    private int[] index;
    GMSSKeyParameters key;
    private int mdLength;
    private Digest messDigestOTS;
    private Digest messDigestTrees;
    private int numLayer;
    private WinternitzOTSignature ots;
    private byte[] pubKeyBytes;
    private SecureRandom random;
    private byte[][] subtreeRootSig;

    public GMSSSigner(GMSSDigestProvider gMSSDigestProvider) {
        this.digestProvider = gMSSDigestProvider;
        this.messDigestOTS = this.messDigestTrees = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTrees.getDigestSize();
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
    }

    private void initSign() {
        this.messDigestTrees.reset();
        GMSSPrivateKeyParameters gMSSPrivateKeyParameters = (GMSSPrivateKeyParameters)this.key;
        if (gMSSPrivateKeyParameters.isUsed()) {
            throw new IllegalStateException("Private key already used");
        }
        if (gMSSPrivateKeyParameters.getIndex(0) >= gMSSPrivateKeyParameters.getNumLeafs(0)) {
            throw new IllegalStateException("No more signatures can be generated");
        }
        this.gmssPS = gMSSPrivateKeyParameters.getParameters();
        this.numLayer = this.gmssPS.getNumOfLayers();
        byte[] arrby = gMSSPrivateKeyParameters.getCurrentSeeds()[-1 + this.numLayer];
        new byte[this.mdLength];
        byte[] arrby2 = new byte[this.mdLength];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)this.mdLength);
        this.ots = new WinternitzOTSignature(this.gmssRandom.nextSeed(arrby2), this.digestProvider.get(), this.gmssPS.getWinternitzParameter()[-1 + this.numLayer]);
        byte[][][] arrby3 = gMSSPrivateKeyParameters.getCurrentAuthPaths();
        this.currentAuthPaths = new byte[this.numLayer][][];
        for (int i = 0; i < this.numLayer; ++i) {
            byte[][][] arrby4 = this.currentAuthPaths;
            int[] arrn = new int[]{arrby3[i].length, this.mdLength};
            arrby4[i] = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
            for (int j = 0; j < arrby3[i].length; ++j) {
                System.arraycopy((Object)arrby3[i][j], (int)0, (Object)this.currentAuthPaths[i][j], (int)0, (int)this.mdLength);
            }
        }
        this.index = new int[this.numLayer];
        System.arraycopy((Object)gMSSPrivateKeyParameters.getIndex(), (int)0, (Object)this.index, (int)0, (int)this.numLayer);
        this.subtreeRootSig = new byte[-1 + this.numLayer][];
        for (int i = 0; i < -1 + this.numLayer; ++i) {
            byte[] arrby5 = gMSSPrivateKeyParameters.getSubtreeRootSig(i);
            this.subtreeRootSig[i] = new byte[arrby5.length];
            System.arraycopy((Object)arrby5, (int)0, (Object)this.subtreeRootSig[i], (int)0, (int)arrby5.length);
        }
        gMSSPrivateKeyParameters.markUsed();
    }

    private void initVerify() {
        this.messDigestTrees.reset();
        GMSSPublicKeyParameters gMSSPublicKeyParameters = (GMSSPublicKeyParameters)this.key;
        this.pubKeyBytes = gMSSPublicKeyParameters.getPublicKey();
        this.gmssPS = gMSSPublicKeyParameters.getParameters();
        this.numLayer = this.gmssPS.getNumOfLayers();
    }

    @Override
    public byte[] generateSignature(byte[] arrby) {
        new byte[this.mdLength];
        byte[] arrby2 = this.ots.getSignature(arrby);
        byte[] arrby3 = this.gmssUtil.concatenateArray(this.currentAuthPaths[-1 + this.numLayer]);
        byte[] arrby4 = this.gmssUtil.intToBytesLittleEndian(this.index[-1 + this.numLayer]);
        byte[] arrby5 = new byte[arrby4.length + arrby2.length + arrby3.length];
        System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)0, (int)arrby4.length);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby5, (int)arrby4.length, (int)arrby2.length);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby5, (int)(arrby4.length + arrby2.length), (int)arrby3.length);
        byte[] arrby6 = new byte[]{};
        for (int i = -1 + (-1 + this.numLayer); i >= 0; --i) {
            byte[] arrby7 = this.gmssUtil.concatenateArray(this.currentAuthPaths[i]);
            byte[] arrby8 = this.gmssUtil.intToBytesLittleEndian(this.index[i]);
            byte[] arrby9 = new byte[arrby6.length];
            System.arraycopy((Object)arrby6, (int)0, (Object)arrby9, (int)0, (int)arrby6.length);
            arrby6 = new byte[arrby9.length + arrby8.length + this.subtreeRootSig[i].length + arrby7.length];
            System.arraycopy((Object)arrby9, (int)0, (Object)arrby6, (int)0, (int)arrby9.length);
            System.arraycopy((Object)arrby8, (int)0, (Object)arrby6, (int)arrby9.length, (int)arrby8.length);
            System.arraycopy((Object)this.subtreeRootSig[i], (int)0, (Object)arrby6, (int)(arrby9.length + arrby8.length), (int)this.subtreeRootSig[i].length);
            System.arraycopy((Object)arrby7, (int)0, (Object)arrby6, (int)(arrby9.length + arrby8.length + this.subtreeRootSig[i].length), (int)arrby7.length);
        }
        byte[] arrby10 = new byte[arrby5.length + arrby6.length];
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby10, (int)0, (int)arrby5.length);
        System.arraycopy((Object)arrby6, (int)0, (Object)arrby10, (int)arrby5.length, (int)arrby6.length);
        return arrby10;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (GMSSPrivateKeyParameters)parametersWithRandom.getParameters();
                this.initSign();
                return;
            }
            this.random = new SecureRandom();
            this.key = (GMSSPrivateKeyParameters)cipherParameters;
            this.initSign();
            return;
        }
        this.key = (GMSSPublicKeyParameters)cipherParameters;
        this.initVerify();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean verifySignature(byte[] arrby, byte[] arrby2) {
        this.messDigestOTS.reset();
        int n = -1 + this.numLayer;
        int n2 = 0;
        do {
            byte[] arrby3;
            int n3;
            byte[][] arrby4;
            if (n >= 0) {
                WinternitzOTSVerify winternitzOTSVerify = new WinternitzOTSVerify(this.digestProvider.get(), this.gmssPS.getWinternitzParameter()[n]);
                int n4 = winternitzOTSVerify.getSignatureLength();
                int n5 = this.gmssUtil.bytesToIntLittleEndian(arrby2, n2);
                int n6 = n2 + 4;
                byte[] arrby5 = new byte[n4];
                System.arraycopy((Object)arrby2, (int)n6, (Object)arrby5, (int)0, (int)n4);
                int n7 = n4 + n6;
                byte[] arrby6 = winternitzOTSVerify.Verify(arrby, arrby5);
                if (arrby6 == null) {
                    System.err.println("OTS Public Key is null in GMSSSignature.verify");
                    return false;
                }
                int[] arrn = new int[]{this.gmssPS.getHeightOfTrees()[n], this.mdLength};
                arrby4 = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
                n2 = n7;
                for (int i = 0; i < arrby4.length; n2 += this.mdLength, ++i) {
                    System.arraycopy((Object)arrby2, (int)n2, (Object)arrby4[i], (int)0, (int)this.mdLength);
                }
                new byte[this.mdLength];
                int n8 = n5 + (1 << arrby4.length);
                arrby3 = arrby6;
                n3 = n8;
            } else {
                if (!Arrays.areEqual(this.pubKeyBytes, arrby)) return false;
                return true;
            }
            for (int i = 0; i < arrby4.length; ++i) {
                byte[] arrby7 = new byte[this.mdLength << 1];
                if (n3 % 2 == 0) {
                    System.arraycopy((Object)arrby3, (int)0, (Object)arrby7, (int)0, (int)this.mdLength);
                    System.arraycopy((Object)arrby4[i], (int)0, (Object)arrby7, (int)this.mdLength, (int)this.mdLength);
                    n3 /= 2;
                } else {
                    System.arraycopy((Object)arrby4[i], (int)0, (Object)arrby7, (int)0, (int)this.mdLength);
                    System.arraycopy((Object)arrby3, (int)0, (Object)arrby7, (int)this.mdLength, (int)arrby3.length);
                    n3 = (n3 - 1) / 2;
                }
                this.messDigestTrees.update(arrby7, 0, arrby7.length);
                arrby3 = new byte[this.messDigestTrees.getDigestSize()];
                this.messDigestTrees.doFinal(arrby3, 0);
            }
            --n;
            arrby = arrby3;
        } while (true);
    }
}

