/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Array
 *  java.util.Vector
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.GMSSDigestProvider;
import org.bouncycastle.pqc.crypto.gmss.GMSSKeyParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.crypto.gmss.Treehash;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature;
import org.bouncycastle.util.Arrays;

public class GMSSPrivateKeyParameters
extends GMSSKeyParameters {
    private int[] K;
    private byte[][][] currentAuthPaths;
    private Vector[][] currentRetain;
    private byte[][] currentRootSig;
    private byte[][] currentSeeds;
    private Vector[] currentStack;
    private Treehash[][] currentTreehash;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSRandom gmssRandom;
    private int[] heightOfTrees;
    private int[] index;
    private byte[][][] keep;
    private int mdLength;
    private Digest messDigestTrees;
    private int[] minTreehash;
    private byte[][][] nextAuthPaths;
    private GMSSLeaf[] nextNextLeaf;
    private GMSSRootCalc[] nextNextRoot;
    private byte[][] nextNextSeeds;
    private Vector[][] nextRetain;
    private byte[][] nextRoot;
    private GMSSRootSig[] nextRootSig;
    private Vector[] nextStack;
    private Treehash[][] nextTreehash;
    private int numLayer;
    private int[] numLeafs;
    private int[] otsIndex;
    private GMSSLeaf[] upperLeaf;
    private GMSSLeaf[] upperTreehashLeaf;
    private boolean used = false;

    private GMSSPrivateKeyParameters(GMSSPrivateKeyParameters gMSSPrivateKeyParameters) {
        super(true, gMSSPrivateKeyParameters.getParameters());
        this.index = Arrays.clone(gMSSPrivateKeyParameters.index);
        this.currentSeeds = Arrays.clone(gMSSPrivateKeyParameters.currentSeeds);
        this.nextNextSeeds = Arrays.clone(gMSSPrivateKeyParameters.nextNextSeeds);
        this.currentAuthPaths = Arrays.clone(gMSSPrivateKeyParameters.currentAuthPaths);
        this.nextAuthPaths = Arrays.clone(gMSSPrivateKeyParameters.nextAuthPaths);
        this.currentTreehash = gMSSPrivateKeyParameters.currentTreehash;
        this.nextTreehash = gMSSPrivateKeyParameters.nextTreehash;
        this.currentStack = gMSSPrivateKeyParameters.currentStack;
        this.nextStack = gMSSPrivateKeyParameters.nextStack;
        this.currentRetain = gMSSPrivateKeyParameters.currentRetain;
        this.nextRetain = gMSSPrivateKeyParameters.nextRetain;
        this.keep = Arrays.clone(gMSSPrivateKeyParameters.keep);
        this.nextNextLeaf = gMSSPrivateKeyParameters.nextNextLeaf;
        this.upperLeaf = gMSSPrivateKeyParameters.upperLeaf;
        this.upperTreehashLeaf = gMSSPrivateKeyParameters.upperTreehashLeaf;
        this.minTreehash = gMSSPrivateKeyParameters.minTreehash;
        this.gmssPS = gMSSPrivateKeyParameters.gmssPS;
        this.nextRoot = Arrays.clone(gMSSPrivateKeyParameters.nextRoot);
        this.nextNextRoot = gMSSPrivateKeyParameters.nextNextRoot;
        this.currentRootSig = gMSSPrivateKeyParameters.currentRootSig;
        this.nextRootSig = gMSSPrivateKeyParameters.nextRootSig;
        this.digestProvider = gMSSPrivateKeyParameters.digestProvider;
        this.heightOfTrees = gMSSPrivateKeyParameters.heightOfTrees;
        this.otsIndex = gMSSPrivateKeyParameters.otsIndex;
        this.K = gMSSPrivateKeyParameters.K;
        this.numLayer = gMSSPrivateKeyParameters.numLayer;
        this.messDigestTrees = gMSSPrivateKeyParameters.messDigestTrees;
        this.mdLength = gMSSPrivateKeyParameters.mdLength;
        this.gmssRandom = gMSSPrivateKeyParameters.gmssRandom;
        this.numLeafs = gMSSPrivateKeyParameters.numLeafs;
    }

    /*
     * Enabled aggressive block sorting
     */
    public GMSSPrivateKeyParameters(int[] arrn, byte[][] arrby, byte[][] arrby2, byte[][][] arrby3, byte[][][] arrby4, byte[][][] arrby5, Treehash[][] arrtreehash, Treehash[][] arrtreehash2, Vector[] arrvector, Vector[] arrvector2, Vector[][] arrvector3, Vector[][] arrvector4, GMSSLeaf[] arrgMSSLeaf, GMSSLeaf[] arrgMSSLeaf2, GMSSLeaf[] arrgMSSLeaf3, int[] arrn2, byte[][] arrby6, GMSSRootCalc[] arrgMSSRootCalc, byte[][] arrby7, GMSSRootSig[] arrgMSSRootSig, GMSSParameters gMSSParameters, GMSSDigestProvider gMSSDigestProvider) {
        super(true, gMSSParameters);
        this.messDigestTrees = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTrees.getDigestSize();
        this.gmssPS = gMSSParameters;
        this.otsIndex = gMSSParameters.getWinternitzParameter();
        this.K = gMSSParameters.getK();
        this.heightOfTrees = gMSSParameters.getHeightOfTrees();
        this.numLayer = this.gmssPS.getNumOfLayers();
        if (arrn == null) {
            this.index = new int[this.numLayer];
            for (int i = 0; i < this.numLayer; ++i) {
                this.index[i] = 0;
            }
        } else {
            this.index = arrn;
        }
        this.currentSeeds = arrby;
        this.nextNextSeeds = arrby2;
        this.currentAuthPaths = arrby3;
        this.nextAuthPaths = arrby4;
        if (arrby5 == null) {
            this.keep = new byte[this.numLayer][][];
            for (int i = 0; i < this.numLayer; ++i) {
                byte[][][] arrby8 = this.keep;
                int[] arrn3 = new int[]{(int)Math.floor((double)(this.heightOfTrees[i] / 2)), this.mdLength};
                arrby8[i] = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn3);
            }
        } else {
            this.keep = arrby5;
        }
        if (arrvector == null) {
            this.currentStack = new Vector[this.numLayer];
            for (int i = 0; i < this.numLayer; ++i) {
                this.currentStack[i] = new Vector();
            }
        } else {
            this.currentStack = arrvector;
        }
        if (arrvector2 == null) {
            this.nextStack = new Vector[-1 + this.numLayer];
            for (int i = 0; i < -1 + this.numLayer; ++i) {
                this.nextStack[i] = new Vector();
            }
        } else {
            this.nextStack = arrvector2;
        }
        this.currentTreehash = arrtreehash;
        this.nextTreehash = arrtreehash2;
        this.currentRetain = arrvector3;
        this.nextRetain = arrvector4;
        this.nextRoot = arrby6;
        this.digestProvider = gMSSDigestProvider;
        if (arrgMSSRootCalc == null) {
            this.nextNextRoot = new GMSSRootCalc[-1 + this.numLayer];
            for (int i = 0; i < -1 + this.numLayer; ++i) {
                this.nextNextRoot[i] = new GMSSRootCalc(this.heightOfTrees[i + 1], this.K[i + 1], this.digestProvider);
            }
        } else {
            this.nextNextRoot = arrgMSSRootCalc;
        }
        this.currentRootSig = arrby7;
        this.numLeafs = new int[this.numLayer];
        for (int i = 0; i < this.numLayer; ++i) {
            this.numLeafs[i] = 1 << this.heightOfTrees[i];
        }
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
        if (this.numLayer > 1) {
            if (arrgMSSLeaf == null) {
                this.nextNextLeaf = new GMSSLeaf[-2 + this.numLayer];
                for (int i = 0; i < -2 + this.numLayer; ++i) {
                    this.nextNextLeaf[i] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[i + 1], this.numLeafs[i + 2], this.nextNextSeeds[i]);
                }
            } else {
                this.nextNextLeaf = arrgMSSLeaf;
            }
        } else {
            this.nextNextLeaf = new GMSSLeaf[0];
        }
        if (arrgMSSLeaf2 == null) {
            this.upperLeaf = new GMSSLeaf[-1 + this.numLayer];
            for (int i = 0; i < -1 + this.numLayer; ++i) {
                this.upperLeaf[i] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[i], this.numLeafs[i + 1], this.currentSeeds[i]);
            }
        } else {
            this.upperLeaf = arrgMSSLeaf2;
        }
        if (arrgMSSLeaf3 == null) {
            this.upperTreehashLeaf = new GMSSLeaf[-1 + this.numLayer];
            for (int i = 0; i < -1 + this.numLayer; ++i) {
                this.upperTreehashLeaf[i] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[i], this.numLeafs[i + 1]);
            }
        } else {
            this.upperTreehashLeaf = arrgMSSLeaf3;
        }
        if (arrn2 == null) {
            this.minTreehash = new int[-1 + this.numLayer];
            for (int i = 0; i < -1 + this.numLayer; ++i) {
                this.minTreehash[i] = -1;
            }
        } else {
            this.minTreehash = arrn2;
        }
        byte[] arrby9 = new byte[this.mdLength];
        new byte[this.mdLength];
        if (arrgMSSRootSig == null) {
            this.nextRootSig = new GMSSRootSig[-1 + this.numLayer];
            for (int i = 0; i < -1 + this.numLayer; ++i) {
                System.arraycopy((Object)arrby[i], (int)0, (Object)arrby9, (int)0, (int)this.mdLength);
                this.gmssRandom.nextSeed(arrby9);
                byte[] arrby10 = this.gmssRandom.nextSeed(arrby9);
                this.nextRootSig[i] = new GMSSRootSig(gMSSDigestProvider.get(), this.otsIndex[i], this.heightOfTrees[i + 1]);
                this.nextRootSig[i].initSign(arrby10, arrby6[i]);
            }
            return;
        } else {
            this.nextRootSig = arrgMSSRootSig;
        }
    }

    public GMSSPrivateKeyParameters(byte[][] arrby, byte[][] arrby2, byte[][][] arrby3, byte[][][] arrby4, Treehash[][] arrtreehash, Treehash[][] arrtreehash2, Vector[] arrvector, Vector[] arrvector2, Vector[][] arrvector3, Vector[][] arrvector4, byte[][] arrby5, byte[][] arrby6, GMSSParameters gMSSParameters, GMSSDigestProvider gMSSDigestProvider) {
        this(null, arrby, arrby2, arrby3, arrby4, null, arrtreehash, arrtreehash2, arrvector, arrvector2, arrvector3, arrvector4, null, null, null, null, arrby5, null, arrby6, null, gMSSParameters, gMSSDigestProvider);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void computeAuthPaths(int var1_1) {
        block17 : {
            var2_2 = this.index[var1_1];
            var3_3 = this.heightOfTrees[var1_1];
            var4_4 = this.K[var1_1];
            for (var5_5 = 0; var5_5 < var3_3 - var4_4; ++var5_5) {
                this.currentTreehash[var1_1][var5_5].updateNextSeed(this.gmssRandom);
            }
            var6_6 = this.heightOfPhi(var2_2);
            new byte[this.mdLength];
            var8_7 = this.gmssRandom.nextSeed(this.currentSeeds[var1_1]);
            var9_8 = 1 & var2_2 >>> var6_6 + 1;
            var10_9 = new byte[this.mdLength];
            if (var6_6 < var3_3 - 1 && var9_8 == 0) {
                System.arraycopy((Object)this.currentAuthPaths[var1_1][var6_6], (int)0, (Object)var10_9, (int)0, (int)this.mdLength);
            }
            new byte[this.mdLength];
            if (var6_6 != 0) break block17;
            if (var1_1 == -1 + this.numLayer) {
                var22_10 = new WinternitzOTSignature(var8_7, this.digestProvider.get(), this.otsIndex[var1_1]).getPublicKey();
            } else {
                var20_11 = new byte[this.mdLength];
                System.arraycopy((Object)this.currentSeeds[var1_1], (int)0, (Object)var20_11, (int)0, (int)this.mdLength);
                this.gmssRandom.nextSeed(var20_11);
                var22_10 = this.upperLeaf[var1_1].getLeaf();
                this.upperLeaf[var1_1].initLeafCalc(var20_11);
            }
            System.arraycopy((Object)var22_10, (int)0, (Object)this.currentAuthPaths[var1_1][0], (int)0, (int)this.mdLength);
            ** GOTO lbl36
        }
        var12_12 = new byte[this.mdLength << 1];
        System.arraycopy((Object)this.currentAuthPaths[var1_1][var6_6 - 1], (int)0, (Object)var12_12, (int)0, (int)this.mdLength);
        System.arraycopy((Object)this.keep[var1_1][(int)Math.floor((double)((var6_6 - 1) / 2))], (int)0, (Object)var12_12, (int)this.mdLength, (int)this.mdLength);
        this.messDigestTrees.update(var12_12, 0, var12_12.length);
        this.currentAuthPaths[var1_1][var6_6] = new byte[this.messDigestTrees.getDigestSize()];
        this.messDigestTrees.doFinal(this.currentAuthPaths[var1_1][var6_6], 0);
        var14_13 = 0;
        do {
            block18 : {
                if (var14_13 < var6_6) break block18;
lbl36: // 2 sources:
                if (var6_6 < var3_3 - 1 && var9_8 == 0) {
                    System.arraycopy((Object)var10_9, (int)0, (Object)this.keep[var1_1][(int)Math.floor((double)(var6_6 / 2))], (int)0, (int)this.mdLength);
                }
                if (var1_1 != -1 + this.numLayer) {
                    this.minTreehash[var1_1] = this.getMinTreehashIndex(var1_1);
                    return;
                }
                var15_14 = 1;
                while (var15_14 <= (var3_3 - var4_4) / 2) {
                    var16_15 = this.getMinTreehashIndex(var1_1);
                    if (var16_15 >= 0) {
                        try {
                            var18_17 = new byte[this.mdLength];
                            System.arraycopy((Object)this.currentTreehash[var1_1][var16_15].getSeedActive(), (int)0, (Object)var18_17, (int)0, (int)this.mdLength);
                            var19_18 = new WinternitzOTSignature(this.gmssRandom.nextSeed(var18_17), this.digestProvider.get(), this.otsIndex[var1_1]).getPublicKey();
                            this.currentTreehash[var1_1][var16_15].update(this.gmssRandom, var19_18);
                        }
                        catch (Exception var17_16) {
                            System.out.println((Object)var17_16);
                        }
                    }
                    ++var15_14;
                }
                return;
            }
            if (var14_13 < var3_3 - var4_4) {
                if (this.currentTreehash[var1_1][var14_13].wasFinished()) {
                    System.arraycopy((Object)this.currentTreehash[var1_1][var14_13].getFirstNode(), (int)0, (Object)this.currentAuthPaths[var1_1][var14_13], (int)0, (int)this.mdLength);
                    this.currentTreehash[var1_1][var14_13].destroy();
                } else {
                    System.err.println("Treehash (" + var1_1 + "," + var14_13 + ") not finished when needed in AuthPathComputation");
                }
            }
            if (var14_13 < var3_3 - 1 && var14_13 >= var3_3 - var4_4 && this.currentRetain[var1_1][var14_13 - (var3_3 - var4_4)].size() > 0) {
                System.arraycopy((Object)this.currentRetain[var1_1][var14_13 - (var3_3 - var4_4)].lastElement(), (int)0, (Object)this.currentAuthPaths[var1_1][var14_13], (int)0, (int)this.mdLength);
                this.currentRetain[var1_1][var14_13 - (var3_3 - var4_4)].removeElementAt(-1 + this.currentRetain[var1_1][var14_13 - (var3_3 - var4_4)].size());
            }
            if (var14_13 < var3_3 - var4_4 && var2_2 + 3 * (1 << var14_13) < this.numLeafs[var1_1]) {
                this.currentTreehash[var1_1][var14_13].initialize();
            }
            ++var14_13;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getMinTreehashIndex(int n) {
        int n2 = 0;
        int n3 = -1;
        while (n2 < this.heightOfTrees[n] - this.K[n]) {
            if (this.currentTreehash[n][n2].wasInitialized() && !this.currentTreehash[n][n2].wasFinished()) {
                if (n3 == -1) {
                    n3 = n2;
                } else if (this.currentTreehash[n][n2].getLowestNodeHeight() < this.currentTreehash[n][n3].getLowestNodeHeight()) {
                    n3 = n2;
                }
            }
            ++n2;
        }
        return n3;
    }

    private int heightOfPhi(int n) {
        if (n == 0) {
            return -1;
        }
        int n2 = 0;
        int n3 = 1;
        while (n % n3 == 0) {
            n3 *= 2;
            ++n2;
        }
        return n2 - 1;
    }

    private void nextKey(int n) {
        if (n == -1 + this.numLayer) {
            int[] arrn = this.index;
            arrn[n] = 1 + arrn[n];
        }
        if (this.index[n] == this.numLeafs[n]) {
            if (this.numLayer != 1) {
                this.nextTree(n);
                this.index[n] = 0;
            }
            return;
        }
        this.updateKey(n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void nextTree(int n) {
        if (n > 0) {
            int[] arrn = this.index;
            int n2 = n - 1;
            arrn[n2] = 1 + arrn[n2];
            int n3 = n;
            boolean bl = true;
            do {
                if (this.index[--n3] >= this.numLeafs[n3]) continue;
                bl = false;
            } while (bl && n3 > 0);
            if (!bl) {
                this.gmssRandom.nextSeed(this.currentSeeds[n]);
                this.nextRootSig[n - 1].updateSign();
                if (n > 1) {
                    this.nextNextLeaf[-1 + (n - 1)] = this.nextNextLeaf[-1 + (n - 1)].nextLeaf();
                }
                this.upperLeaf[n - 1] = this.upperLeaf[n - 1].nextLeaf();
                if (this.minTreehash[n - 1] >= 0) {
                    this.upperTreehashLeaf[n - 1] = this.upperTreehashLeaf[n - 1].nextLeaf();
                    byte[] arrby = this.upperTreehashLeaf[n - 1].getLeaf();
                    try {
                        this.currentTreehash[n - 1][this.minTreehash[n - 1]].update(this.gmssRandom, arrby);
                        boolean bl2 = this.currentTreehash[n - 1][this.minTreehash[n - 1]].wasFinished();
                        if (bl2) {
                            // empty if block
                        }
                    }
                    catch (Exception exception) {
                        System.out.println((Object)exception);
                    }
                }
                this.updateNextNextAuthRoot(n);
                this.currentRootSig[n - 1] = this.nextRootSig[n - 1].getSig();
                for (int i = 0; i < this.heightOfTrees[n] - this.K[n]; ++i) {
                    this.currentTreehash[n][i] = this.nextTreehash[n - 1][i];
                    this.nextTreehash[n - 1][i] = this.nextNextRoot[n - 1].getTreehash()[i];
                }
                for (int i = 0; i < this.heightOfTrees[n]; ++i) {
                    System.arraycopy((Object)this.nextAuthPaths[n - 1][i], (int)0, (Object)this.currentAuthPaths[n][i], (int)0, (int)this.mdLength);
                    System.arraycopy((Object)this.nextNextRoot[n - 1].getAuthPath()[i], (int)0, (Object)this.nextAuthPaths[n - 1][i], (int)0, (int)this.mdLength);
                }
                for (int i = 0; i < -1 + this.K[n]; ++i) {
                    this.currentRetain[n][i] = this.nextRetain[n - 1][i];
                    this.nextRetain[n - 1][i] = this.nextNextRoot[n - 1].getRetain()[i];
                }
                this.currentStack[n] = this.nextStack[n - 1];
                this.nextStack[n - 1] = this.nextNextRoot[n - 1].getStack();
                this.nextRoot[n - 1] = this.nextNextRoot[n - 1].getRoot();
                new byte[this.mdLength];
                byte[] arrby = new byte[this.mdLength];
                System.arraycopy((Object)this.currentSeeds[n - 1], (int)0, (Object)arrby, (int)0, (int)this.mdLength);
                this.gmssRandom.nextSeed(arrby);
                this.gmssRandom.nextSeed(arrby);
                byte[] arrby2 = this.gmssRandom.nextSeed(arrby);
                this.nextRootSig[n - 1].initSign(arrby2, this.nextRoot[n - 1]);
                this.nextKey(n - 1);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateKey(int n) {
        this.computeAuthPaths(n);
        if (n > 0) {
            if (n > 1) {
                this.nextNextLeaf[-1 + (n - 1)] = this.nextNextLeaf[-1 + (n - 1)].nextLeaf();
            }
            this.upperLeaf[n - 1] = this.upperLeaf[n - 1].nextLeaf();
            int n2 = (int)Math.floor((double)((double)(2 * this.getNumLeafs(n)) / (double)(this.heightOfTrees[n - 1] - this.K[n - 1])));
            if (this.index[n] % n2 == 1) {
                if (this.index[n] > 1 && this.minTreehash[n - 1] >= 0) {
                    byte[] arrby = this.upperTreehashLeaf[n - 1].getLeaf();
                    try {
                        this.currentTreehash[n - 1][this.minTreehash[n - 1]].update(this.gmssRandom, arrby);
                        boolean bl = this.currentTreehash[n - 1][this.minTreehash[n - 1]].wasFinished();
                        if (bl) {
                            // empty if block
                        }
                    }
                    catch (Exception exception) {
                        System.out.println((Object)exception);
                    }
                }
                this.minTreehash[n - 1] = this.getMinTreehashIndex(n - 1);
                if (this.minTreehash[n - 1] >= 0) {
                    byte[] arrby = this.currentTreehash[n - 1][this.minTreehash[n - 1]].getSeedActive();
                    this.upperTreehashLeaf[n - 1] = new GMSSLeaf(this.digestProvider.get(), this.otsIndex[n - 1], n2, arrby);
                    this.upperTreehashLeaf[n - 1] = this.upperTreehashLeaf[n - 1].nextLeaf();
                }
            } else if (this.minTreehash[n - 1] >= 0) {
                this.upperTreehashLeaf[n - 1] = this.upperTreehashLeaf[n - 1].nextLeaf();
            }
            this.nextRootSig[n - 1].updateSign();
            if (this.index[n] == 1) {
                this.nextNextRoot[n - 1].initialize(new Vector());
            }
            this.updateNextNextAuthRoot(n);
        }
    }

    private void updateNextNextAuthRoot(int n) {
        new byte[this.mdLength];
        byte[] arrby = this.gmssRandom.nextSeed(this.nextNextSeeds[n - 1]);
        if (n == -1 + this.numLayer) {
            WinternitzOTSignature winternitzOTSignature = new WinternitzOTSignature(arrby, this.digestProvider.get(), this.otsIndex[n]);
            this.nextNextRoot[n - 1].update(this.nextNextSeeds[n - 1], winternitzOTSignature.getPublicKey());
            return;
        }
        this.nextNextRoot[n - 1].update(this.nextNextSeeds[n - 1], this.nextNextLeaf[n - 1].getLeaf());
        this.nextNextLeaf[n - 1].initLeafCalc(this.nextNextSeeds[n - 1]);
    }

    public byte[][][] getCurrentAuthPaths() {
        return Arrays.clone(this.currentAuthPaths);
    }

    public byte[][] getCurrentSeeds() {
        return Arrays.clone(this.currentSeeds);
    }

    public int getIndex(int n) {
        return this.index[n];
    }

    public int[] getIndex() {
        return this.index;
    }

    public GMSSDigestProvider getName() {
        return this.digestProvider;
    }

    public int getNumLeafs(int n) {
        return this.numLeafs[n];
    }

    public byte[] getSubtreeRootSig(int n) {
        return this.currentRootSig[n];
    }

    public boolean isUsed() {
        return this.used;
    }

    public void markUsed() {
        this.used = true;
    }

    public GMSSPrivateKeyParameters nextKey() {
        GMSSPrivateKeyParameters gMSSPrivateKeyParameters = new GMSSPrivateKeyParameters(this);
        gMSSPrivateKeyParameters.nextKey(-1 + this.gmssPS.getNumOfLayers());
        return gMSSPrivateKeyParameters;
    }
}

