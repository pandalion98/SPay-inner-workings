package org.bouncycastle.pqc.crypto.gmss;

import java.lang.reflect.Array;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature;
import org.bouncycastle.util.Arrays;

public class GMSSPrivateKeyParameters extends GMSSKeyParameters {
    private int[] f400K;
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
    private boolean used;

    private GMSSPrivateKeyParameters(GMSSPrivateKeyParameters gMSSPrivateKeyParameters) {
        super(true, gMSSPrivateKeyParameters.getParameters());
        this.used = false;
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
        this.f400K = gMSSPrivateKeyParameters.f400K;
        this.numLayer = gMSSPrivateKeyParameters.numLayer;
        this.messDigestTrees = gMSSPrivateKeyParameters.messDigestTrees;
        this.mdLength = gMSSPrivateKeyParameters.mdLength;
        this.gmssRandom = gMSSPrivateKeyParameters.gmssRandom;
        this.numLeafs = gMSSPrivateKeyParameters.numLeafs;
    }

    public GMSSPrivateKeyParameters(int[] iArr, byte[][] bArr, byte[][] bArr2, byte[][][] bArr3, byte[][][] bArr4, byte[][][] bArr5, Treehash[][] treehashArr, Treehash[][] treehashArr2, Vector[] vectorArr, Vector[] vectorArr2, Vector[][] vectorArr3, Vector[][] vectorArr4, GMSSLeaf[] gMSSLeafArr, GMSSLeaf[] gMSSLeafArr2, GMSSLeaf[] gMSSLeafArr3, int[] iArr2, byte[][] bArr6, GMSSRootCalc[] gMSSRootCalcArr, byte[][] bArr7, GMSSRootSig[] gMSSRootSigArr, GMSSParameters gMSSParameters, GMSSDigestProvider gMSSDigestProvider) {
        int i;
        super(true, gMSSParameters);
        this.used = false;
        this.messDigestTrees = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTrees.getDigestSize();
        this.gmssPS = gMSSParameters;
        this.otsIndex = gMSSParameters.getWinternitzParameter();
        this.f400K = gMSSParameters.getK();
        this.heightOfTrees = gMSSParameters.getHeightOfTrees();
        this.numLayer = this.gmssPS.getNumOfLayers();
        if (iArr == null) {
            this.index = new int[this.numLayer];
            for (i = 0; i < this.numLayer; i++) {
                this.index[i] = 0;
            }
        } else {
            this.index = iArr;
        }
        this.currentSeeds = bArr;
        this.nextNextSeeds = bArr2;
        this.currentAuthPaths = bArr3;
        this.nextAuthPaths = bArr4;
        if (bArr5 == null) {
            this.keep = new byte[this.numLayer][][];
            for (int i2 = 0; i2 < this.numLayer; i2++) {
                this.keep[i2] = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{(int) Math.floor((double) (this.heightOfTrees[i2] / 2)), this.mdLength});
            }
        } else {
            this.keep = bArr5;
        }
        if (vectorArr == null) {
            this.currentStack = new Vector[this.numLayer];
            for (i = 0; i < this.numLayer; i++) {
                this.currentStack[i] = new Vector();
            }
        } else {
            this.currentStack = vectorArr;
        }
        if (vectorArr2 == null) {
            this.nextStack = new Vector[(this.numLayer - 1)];
            for (i = 0; i < this.numLayer - 1; i++) {
                this.nextStack[i] = new Vector();
            }
        } else {
            this.nextStack = vectorArr2;
        }
        this.currentTreehash = treehashArr;
        this.nextTreehash = treehashArr2;
        this.currentRetain = vectorArr3;
        this.nextRetain = vectorArr4;
        this.nextRoot = bArr6;
        this.digestProvider = gMSSDigestProvider;
        if (gMSSRootCalcArr == null) {
            this.nextNextRoot = new GMSSRootCalc[(this.numLayer - 1)];
            for (i = 0; i < this.numLayer - 1; i++) {
                this.nextNextRoot[i] = new GMSSRootCalc(this.heightOfTrees[i + 1], this.f400K[i + 1], this.digestProvider);
            }
        } else {
            this.nextNextRoot = gMSSRootCalcArr;
        }
        this.currentRootSig = bArr7;
        this.numLeafs = new int[this.numLayer];
        for (i = 0; i < this.numLayer; i++) {
            this.numLeafs[i] = 1 << this.heightOfTrees[i];
        }
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
        if (this.numLayer <= 1) {
            this.nextNextLeaf = new GMSSLeaf[0];
        } else if (gMSSLeafArr == null) {
            this.nextNextLeaf = new GMSSLeaf[(this.numLayer - 2)];
            for (i = 0; i < this.numLayer - 2; i++) {
                this.nextNextLeaf[i] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[i + 1], this.numLeafs[i + 2], this.nextNextSeeds[i]);
            }
        } else {
            this.nextNextLeaf = gMSSLeafArr;
        }
        if (gMSSLeafArr2 == null) {
            this.upperLeaf = new GMSSLeaf[(this.numLayer - 1)];
            for (i = 0; i < this.numLayer - 1; i++) {
                this.upperLeaf[i] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[i], this.numLeafs[i + 1], this.currentSeeds[i]);
            }
        } else {
            this.upperLeaf = gMSSLeafArr2;
        }
        if (gMSSLeafArr3 == null) {
            this.upperTreehashLeaf = new GMSSLeaf[(this.numLayer - 1)];
            for (i = 0; i < this.numLayer - 1; i++) {
                this.upperTreehashLeaf[i] = new GMSSLeaf(gMSSDigestProvider.get(), this.otsIndex[i], this.numLeafs[i + 1]);
            }
        } else {
            this.upperTreehashLeaf = gMSSLeafArr3;
        }
        if (iArr2 == null) {
            this.minTreehash = new int[(this.numLayer - 1)];
            for (i = 0; i < this.numLayer - 1; i++) {
                this.minTreehash[i] = -1;
            }
        } else {
            this.minTreehash = iArr2;
        }
        Object obj = new byte[this.mdLength];
        byte[] bArr8 = new byte[this.mdLength];
        if (gMSSRootSigArr == null) {
            this.nextRootSig = new GMSSRootSig[(this.numLayer - 1)];
            for (i = 0; i < this.numLayer - 1; i++) {
                System.arraycopy(bArr[i], 0, obj, 0, this.mdLength);
                this.gmssRandom.nextSeed(obj);
                byte[] nextSeed = this.gmssRandom.nextSeed(obj);
                this.nextRootSig[i] = new GMSSRootSig(gMSSDigestProvider.get(), this.otsIndex[i], this.heightOfTrees[i + 1]);
                this.nextRootSig[i].initSign(nextSeed, bArr6[i]);
            }
            return;
        }
        this.nextRootSig = gMSSRootSigArr;
    }

    public GMSSPrivateKeyParameters(byte[][] bArr, byte[][] bArr2, byte[][][] bArr3, byte[][][] bArr4, Treehash[][] treehashArr, Treehash[][] treehashArr2, Vector[] vectorArr, Vector[] vectorArr2, Vector[][] vectorArr3, Vector[][] vectorArr4, byte[][] bArr5, byte[][] bArr6, GMSSParameters gMSSParameters, GMSSDigestProvider gMSSDigestProvider) {
        this(null, bArr, bArr2, bArr3, bArr4, (byte[][][]) null, treehashArr, treehashArr2, vectorArr, vectorArr2, vectorArr3, vectorArr4, null, null, null, null, bArr5, null, bArr6, null, gMSSParameters, gMSSDigestProvider);
    }

    private void computeAuthPaths(int i) {
        int i2;
        int i3 = this.index[i];
        int i4 = this.heightOfTrees[i];
        int i5 = this.f400K[i];
        for (i2 = 0; i2 < i4 - i5; i2++) {
            this.currentTreehash[i][i2].updateNextSeed(this.gmssRandom);
        }
        int heightOfPhi = heightOfPhi(i3);
        byte[] bArr = new byte[this.mdLength];
        bArr = this.gmssRandom.nextSeed(this.currentSeeds[i]);
        int i6 = (i3 >>> (heightOfPhi + 1)) & 1;
        Object obj = new byte[this.mdLength];
        if (heightOfPhi < i4 - 1 && i6 == 0) {
            System.arraycopy(this.currentAuthPaths[i][heightOfPhi], 0, obj, 0, this.mdLength);
        }
        byte[] bArr2 = new byte[this.mdLength];
        Object publicKey;
        if (heightOfPhi == 0) {
            if (i == this.numLayer - 1) {
                publicKey = new WinternitzOTSignature(bArr, this.digestProvider.get(), this.otsIndex[i]).getPublicKey();
            } else {
                Object obj2 = new byte[this.mdLength];
                System.arraycopy(this.currentSeeds[i], 0, obj2, 0, this.mdLength);
                this.gmssRandom.nextSeed(obj2);
                publicKey = this.upperLeaf[i].getLeaf();
                this.upperLeaf[i].initLeafCalc(obj2);
            }
            System.arraycopy(publicKey, 0, this.currentAuthPaths[i][0], 0, this.mdLength);
        } else {
            publicKey = new byte[(this.mdLength << 1)];
            System.arraycopy(this.currentAuthPaths[i][heightOfPhi - 1], 0, publicKey, 0, this.mdLength);
            System.arraycopy(this.keep[i][(int) Math.floor((double) ((heightOfPhi - 1) / 2))], 0, publicKey, this.mdLength, this.mdLength);
            this.messDigestTrees.update(publicKey, 0, publicKey.length);
            this.currentAuthPaths[i][heightOfPhi] = new byte[this.messDigestTrees.getDigestSize()];
            this.messDigestTrees.doFinal(this.currentAuthPaths[i][heightOfPhi], 0);
            i2 = 0;
            while (i2 < heightOfPhi) {
                if (i2 < i4 - i5) {
                    if (this.currentTreehash[i][i2].wasFinished()) {
                        System.arraycopy(this.currentTreehash[i][i2].getFirstNode(), 0, this.currentAuthPaths[i][i2], 0, this.mdLength);
                        this.currentTreehash[i][i2].destroy();
                    } else {
                        System.err.println("Treehash (" + i + "," + i2 + ") not finished when needed in AuthPathComputation");
                    }
                }
                if (i2 < i4 - 1 && i2 >= i4 - i5 && this.currentRetain[i][i2 - (i4 - i5)].size() > 0) {
                    System.arraycopy(this.currentRetain[i][i2 - (i4 - i5)].lastElement(), 0, this.currentAuthPaths[i][i2], 0, this.mdLength);
                    this.currentRetain[i][i2 - (i4 - i5)].removeElementAt(this.currentRetain[i][i2 - (i4 - i5)].size() - 1);
                }
                if (i2 < i4 - i5 && ((1 << i2) * 3) + i3 < this.numLeafs[i]) {
                    this.currentTreehash[i][i2].initialize();
                }
                i2++;
            }
        }
        if (heightOfPhi < i4 - 1 && i6 == 0) {
            System.arraycopy(obj, 0, this.keep[i][(int) Math.floor((double) (heightOfPhi / 2))], 0, this.mdLength);
        }
        if (i == this.numLayer - 1) {
            for (i2 = 1; i2 <= (i4 - i5) / 2; i2++) {
                int minTreehashIndex = getMinTreehashIndex(i);
                if (minTreehashIndex >= 0) {
                    try {
                        Object obj3 = new byte[this.mdLength];
                        System.arraycopy(this.currentTreehash[i][minTreehashIndex].getSeedActive(), 0, obj3, 0, this.mdLength);
                        this.currentTreehash[i][minTreehashIndex].update(this.gmssRandom, new WinternitzOTSignature(this.gmssRandom.nextSeed(obj3), this.digestProvider.get(), this.otsIndex[i]).getPublicKey());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
            return;
        }
        this.minTreehash[i] = getMinTreehashIndex(i);
    }

    private int getMinTreehashIndex(int i) {
        int i2 = 0;
        int i3 = -1;
        while (i2 < this.heightOfTrees[i] - this.f400K[i]) {
            if (this.currentTreehash[i][i2].wasInitialized() && !this.currentTreehash[i][i2].wasFinished()) {
                if (i3 == -1) {
                    i3 = i2;
                } else if (this.currentTreehash[i][i2].getLowestNodeHeight() < this.currentTreehash[i][i3].getLowestNodeHeight()) {
                    i3 = i2;
                }
            }
            i2++;
        }
        return i3;
    }

    private int heightOfPhi(int i) {
        if (i == 0) {
            return -1;
        }
        int i2 = 0;
        int i3 = 1;
        while (i % i3 == 0) {
            i3 *= 2;
            i2++;
        }
        return i2 - 1;
    }

    private void nextKey(int i) {
        if (i == this.numLayer - 1) {
            int[] iArr = this.index;
            iArr[i] = iArr[i] + 1;
        }
        if (this.index[i] != this.numLeafs[i]) {
            updateKey(i);
        } else if (this.numLayer != 1) {
            nextTree(i);
            this.index[i] = 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void nextTree(int r7) {
        /*
        r6 = this;
        r2 = 1;
        r3 = 0;
        if (r7 <= 0) goto L_0x01b4;
    L_0x0004:
        r0 = r6.index;
        r1 = r7 + -1;
        r4 = r0[r1];
        r4 = r4 + 1;
        r0[r1] = r4;
        r0 = r7;
        r1 = r2;
    L_0x0010:
        r0 = r0 + -1;
        r4 = r6.index;
        r4 = r4[r0];
        r5 = r6.numLeafs;
        r5 = r5[r0];
        if (r4 >= r5) goto L_0x001d;
    L_0x001c:
        r1 = r3;
    L_0x001d:
        if (r1 == 0) goto L_0x0021;
    L_0x001f:
        if (r0 > 0) goto L_0x0010;
    L_0x0021:
        if (r1 != 0) goto L_0x01b4;
    L_0x0023:
        r0 = r6.gmssRandom;
        r1 = r6.currentSeeds;
        r1 = r1[r7];
        r0.nextSeed(r1);
        r0 = r6.nextRootSig;
        r1 = r7 + -1;
        r0 = r0[r1];
        r0.updateSign();
        if (r7 <= r2) goto L_0x004b;
    L_0x0037:
        r0 = r6.nextNextLeaf;
        r1 = r7 + -1;
        r1 = r1 + -1;
        r2 = r6.nextNextLeaf;
        r4 = r7 + -1;
        r4 = r4 + -1;
        r2 = r2[r4];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
    L_0x004b:
        r0 = r6.upperLeaf;
        r1 = r7 + -1;
        r2 = r6.upperLeaf;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
        r0 = r6.minTreehash;
        r1 = r7 + -1;
        r0 = r0[r1];
        if (r0 < 0) goto L_0x00a4;
    L_0x0063:
        r0 = r6.upperTreehashLeaf;
        r1 = r7 + -1;
        r2 = r6.upperTreehashLeaf;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
        r0 = r6.upperTreehashLeaf;
        r1 = r7 + -1;
        r0 = r0[r1];
        r0 = r0.getLeaf();
        r1 = r6.currentTreehash;	 Catch:{ Exception -> 0x00e8 }
        r2 = r7 + -1;
        r1 = r1[r2];	 Catch:{ Exception -> 0x00e8 }
        r2 = r6.minTreehash;	 Catch:{ Exception -> 0x00e8 }
        r4 = r7 + -1;
        r2 = r2[r4];	 Catch:{ Exception -> 0x00e8 }
        r1 = r1[r2];	 Catch:{ Exception -> 0x00e8 }
        r2 = r6.gmssRandom;	 Catch:{ Exception -> 0x00e8 }
        r1.update(r2, r0);	 Catch:{ Exception -> 0x00e8 }
        r0 = r6.currentTreehash;	 Catch:{ Exception -> 0x00e8 }
        r1 = r7 + -1;
        r0 = r0[r1];	 Catch:{ Exception -> 0x00e8 }
        r1 = r6.minTreehash;	 Catch:{ Exception -> 0x00e8 }
        r2 = r7 + -1;
        r1 = r1[r2];	 Catch:{ Exception -> 0x00e8 }
        r0 = r0[r1];	 Catch:{ Exception -> 0x00e8 }
        r0 = r0.wasFinished();	 Catch:{ Exception -> 0x00e8 }
        if (r0 == 0) goto L_0x00a4;
    L_0x00a4:
        r6.updateNextNextAuthRoot(r7);
        r0 = r6.currentRootSig;
        r1 = r7 + -1;
        r2 = r6.nextRootSig;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.getSig();
        r0[r1] = r2;
        r0 = r3;
    L_0x00b8:
        r1 = r6.heightOfTrees;
        r1 = r1[r7];
        r2 = r6.f400K;
        r2 = r2[r7];
        r1 = r1 - r2;
        if (r0 >= r1) goto L_0x00ef;
    L_0x00c3:
        r1 = r6.currentTreehash;
        r1 = r1[r7];
        r2 = r6.nextTreehash;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2[r0];
        r1[r0] = r2;
        r1 = r6.nextTreehash;
        r2 = r7 + -1;
        r1 = r1[r2];
        r2 = r6.nextNextRoot;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.getTreehash();
        r2 = r2[r0];
        r1[r0] = r2;
        r0 = r0 + 1;
        goto L_0x00b8;
    L_0x00e8:
        r0 = move-exception;
        r1 = java.lang.System.out;
        r1.println(r0);
        goto L_0x00a4;
    L_0x00ef:
        r0 = r3;
    L_0x00f0:
        r1 = r6.heightOfTrees;
        r1 = r1[r7];
        if (r0 >= r1) goto L_0x0125;
    L_0x00f6:
        r1 = r6.nextAuthPaths;
        r2 = r7 + -1;
        r1 = r1[r2];
        r1 = r1[r0];
        r2 = r6.currentAuthPaths;
        r2 = r2[r7];
        r2 = r2[r0];
        r4 = r6.mdLength;
        java.lang.System.arraycopy(r1, r3, r2, r3, r4);
        r1 = r6.nextNextRoot;
        r2 = r7 + -1;
        r1 = r1[r2];
        r1 = r1.getAuthPath();
        r1 = r1[r0];
        r2 = r6.nextAuthPaths;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2[r0];
        r4 = r6.mdLength;
        java.lang.System.arraycopy(r1, r3, r2, r3, r4);
        r0 = r0 + 1;
        goto L_0x00f0;
    L_0x0125:
        r0 = r3;
    L_0x0126:
        r1 = r6.f400K;
        r1 = r1[r7];
        r1 = r1 + -1;
        if (r0 >= r1) goto L_0x0153;
    L_0x012e:
        r1 = r6.currentRetain;
        r1 = r1[r7];
        r2 = r6.nextRetain;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2[r0];
        r1[r0] = r2;
        r1 = r6.nextRetain;
        r2 = r7 + -1;
        r1 = r1[r2];
        r2 = r6.nextNextRoot;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.getRetain();
        r2 = r2[r0];
        r1[r0] = r2;
        r0 = r0 + 1;
        goto L_0x0126;
    L_0x0153:
        r0 = r6.currentStack;
        r1 = r6.nextStack;
        r2 = r7 + -1;
        r1 = r1[r2];
        r0[r7] = r1;
        r0 = r6.nextStack;
        r1 = r7 + -1;
        r2 = r6.nextNextRoot;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.getStack();
        r0[r1] = r2;
        r0 = r6.nextRoot;
        r1 = r7 + -1;
        r2 = r6.nextNextRoot;
        r4 = r7 + -1;
        r2 = r2[r4];
        r2 = r2.getRoot();
        r0[r1] = r2;
        r0 = r6.mdLength;
        r0 = new byte[r0];
        r0 = r6.mdLength;
        r0 = new byte[r0];
        r1 = r6.currentSeeds;
        r2 = r7 + -1;
        r1 = r1[r2];
        r2 = r6.mdLength;
        java.lang.System.arraycopy(r1, r3, r0, r3, r2);
        r1 = r6.gmssRandom;
        r1.nextSeed(r0);
        r1 = r6.gmssRandom;
        r1.nextSeed(r0);
        r1 = r6.gmssRandom;
        r0 = r1.nextSeed(r0);
        r1 = r6.nextRootSig;
        r2 = r7 + -1;
        r1 = r1[r2];
        r2 = r6.nextRoot;
        r3 = r7 + -1;
        r2 = r2[r3];
        r1.initSign(r0, r2);
        r0 = r7 + -1;
        r6.nextKey(r0);
    L_0x01b4:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.pqc.crypto.gmss.GMSSPrivateKeyParameters.nextTree(int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateKey(int r10) {
        /*
        r9 = this;
        r8 = 1;
        r9.computeAuthPaths(r10);
        if (r10 <= 0) goto L_0x00fa;
    L_0x0006:
        if (r10 <= r8) goto L_0x001c;
    L_0x0008:
        r0 = r9.nextNextLeaf;
        r1 = r10 + -1;
        r1 = r1 + -1;
        r2 = r9.nextNextLeaf;
        r3 = r10 + -1;
        r3 = r3 + -1;
        r2 = r2[r3];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
    L_0x001c:
        r0 = r9.upperLeaf;
        r1 = r10 + -1;
        r2 = r9.upperLeaf;
        r3 = r10 + -1;
        r2 = r2[r3];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
        r0 = r9.getNumLeafs(r10);
        r0 = r0 * 2;
        r0 = (double) r0;
        r2 = r9.heightOfTrees;
        r3 = r10 + -1;
        r2 = r2[r3];
        r3 = r9.f400K;
        r4 = r10 + -1;
        r3 = r3[r4];
        r2 = r2 - r3;
        r2 = (double) r2;
        r0 = r0 / r2;
        r0 = java.lang.Math.floor(r0);
        r1 = (int) r0;
        r0 = r9.index;
        r0 = r0[r10];
        r0 = r0 % r1;
        if (r0 != r8) goto L_0x0102;
    L_0x004e:
        r0 = r9.index;
        r0 = r0[r10];
        if (r0 <= r8) goto L_0x008d;
    L_0x0054:
        r0 = r9.minTreehash;
        r2 = r10 + -1;
        r0 = r0[r2];
        if (r0 < 0) goto L_0x008d;
    L_0x005c:
        r0 = r9.upperTreehashLeaf;
        r2 = r10 + -1;
        r0 = r0[r2];
        r0 = r0.getLeaf();
        r2 = r9.currentTreehash;	 Catch:{ Exception -> 0x00fb }
        r3 = r10 + -1;
        r2 = r2[r3];	 Catch:{ Exception -> 0x00fb }
        r3 = r9.minTreehash;	 Catch:{ Exception -> 0x00fb }
        r4 = r10 + -1;
        r3 = r3[r4];	 Catch:{ Exception -> 0x00fb }
        r2 = r2[r3];	 Catch:{ Exception -> 0x00fb }
        r3 = r9.gmssRandom;	 Catch:{ Exception -> 0x00fb }
        r2.update(r3, r0);	 Catch:{ Exception -> 0x00fb }
        r0 = r9.currentTreehash;	 Catch:{ Exception -> 0x00fb }
        r2 = r10 + -1;
        r0 = r0[r2];	 Catch:{ Exception -> 0x00fb }
        r2 = r9.minTreehash;	 Catch:{ Exception -> 0x00fb }
        r3 = r10 + -1;
        r2 = r2[r3];	 Catch:{ Exception -> 0x00fb }
        r0 = r0[r2];	 Catch:{ Exception -> 0x00fb }
        r0 = r0.wasFinished();	 Catch:{ Exception -> 0x00fb }
        if (r0 == 0) goto L_0x008d;
    L_0x008d:
        r0 = r9.minTreehash;
        r2 = r10 + -1;
        r3 = r10 + -1;
        r3 = r9.getMinTreehashIndex(r3);
        r0[r2] = r3;
        r0 = r9.minTreehash;
        r2 = r10 + -1;
        r0 = r0[r2];
        if (r0 < 0) goto L_0x00da;
    L_0x00a1:
        r0 = r9.currentTreehash;
        r2 = r10 + -1;
        r0 = r0[r2];
        r2 = r9.minTreehash;
        r3 = r10 + -1;
        r2 = r2[r3];
        r0 = r0[r2];
        r0 = r0.getSeedActive();
        r2 = r9.upperTreehashLeaf;
        r3 = r10 + -1;
        r4 = new org.bouncycastle.pqc.crypto.gmss.GMSSLeaf;
        r5 = r9.digestProvider;
        r5 = r5.get();
        r6 = r9.otsIndex;
        r7 = r10 + -1;
        r6 = r6[r7];
        r4.<init>(r5, r6, r1, r0);
        r2[r3] = r4;
        r0 = r9.upperTreehashLeaf;
        r1 = r10 + -1;
        r2 = r9.upperTreehashLeaf;
        r3 = r10 + -1;
        r2 = r2[r3];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
    L_0x00da:
        r0 = r9.nextRootSig;
        r1 = r10 + -1;
        r0 = r0[r1];
        r0.updateSign();
        r0 = r9.index;
        r0 = r0[r10];
        if (r0 != r8) goto L_0x00f7;
    L_0x00e9:
        r0 = r9.nextNextRoot;
        r1 = r10 + -1;
        r0 = r0[r1];
        r1 = new java.util.Vector;
        r1.<init>();
        r0.initialize(r1);
    L_0x00f7:
        r9.updateNextNextAuthRoot(r10);
    L_0x00fa:
        return;
    L_0x00fb:
        r0 = move-exception;
        r2 = java.lang.System.out;
        r2.println(r0);
        goto L_0x008d;
    L_0x0102:
        r0 = r9.minTreehash;
        r1 = r10 + -1;
        r0 = r0[r1];
        if (r0 < 0) goto L_0x00da;
    L_0x010a:
        r0 = r9.upperTreehashLeaf;
        r1 = r10 + -1;
        r2 = r9.upperTreehashLeaf;
        r3 = r10 + -1;
        r2 = r2[r3];
        r2 = r2.nextLeaf();
        r0[r1] = r2;
        goto L_0x00da;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.pqc.crypto.gmss.GMSSPrivateKeyParameters.updateKey(int):void");
    }

    private void updateNextNextAuthRoot(int i) {
        byte[] bArr = new byte[this.mdLength];
        bArr = this.gmssRandom.nextSeed(this.nextNextSeeds[i - 1]);
        if (i == this.numLayer - 1) {
            this.nextNextRoot[i - 1].update(this.nextNextSeeds[i - 1], new WinternitzOTSignature(bArr, this.digestProvider.get(), this.otsIndex[i]).getPublicKey());
            return;
        }
        this.nextNextRoot[i - 1].update(this.nextNextSeeds[i - 1], this.nextNextLeaf[i - 1].getLeaf());
        this.nextNextLeaf[i - 1].initLeafCalc(this.nextNextSeeds[i - 1]);
    }

    public byte[][][] getCurrentAuthPaths() {
        return Arrays.clone(this.currentAuthPaths);
    }

    public byte[][] getCurrentSeeds() {
        return Arrays.clone(this.currentSeeds);
    }

    public int getIndex(int i) {
        return this.index[i];
    }

    public int[] getIndex() {
        return this.index;
    }

    public GMSSDigestProvider getName() {
        return this.digestProvider;
    }

    public int getNumLeafs(int i) {
        return this.numLeafs[i];
    }

    public byte[] getSubtreeRootSig(int i) {
        return this.currentRootSig[i];
    }

    public boolean isUsed() {
        return this.used;
    }

    public void markUsed() {
        this.used = true;
    }

    public GMSSPrivateKeyParameters nextKey() {
        GMSSPrivateKeyParameters gMSSPrivateKeyParameters = new GMSSPrivateKeyParameters(this);
        gMSSPrivateKeyParameters.nextKey(this.gmssPS.getNumOfLayers() - 1);
        return gMSSPrivateKeyParameters;
    }
}
