/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.security.spec.KeySpec
 *  java.util.Collection
 *  java.util.Vector
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.KeySpec;
import java.util.Collection;
import java.util.Vector;
import org.bouncycastle.pqc.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.crypto.gmss.Treehash;
import org.bouncycastle.util.Arrays;

public class GMSSPrivateKeySpec
implements KeySpec {
    private byte[][][] currentAuthPath;
    private Vector[][] currentRetain;
    private byte[][] currentRootSig;
    private byte[][] currentSeed;
    private Vector[] currentStack;
    private Treehash[][] currentTreehash;
    private GMSSParameters gmssPS;
    private int[] index;
    private byte[][][] keep;
    private int[] minTreehash;
    private byte[][][] nextAuthPath;
    private GMSSLeaf[] nextNextLeaf;
    private GMSSRootCalc[] nextNextRoot;
    private byte[][] nextNextSeed;
    private Vector[][] nextRetain;
    private byte[][] nextRoot;
    private GMSSRootSig[] nextRootSig;
    private Vector[] nextStack;
    private Treehash[][] nextTreehash;
    private GMSSLeaf[] upperLeaf;
    private GMSSLeaf[] upperTreehashLeaf;

    public GMSSPrivateKeySpec(int[] arrn, byte[][] arrby, byte[][] arrby2, byte[][][] arrby3, byte[][][] arrby4, Treehash[][] arrtreehash, Treehash[][] arrtreehash2, Vector[] arrvector, Vector[] arrvector2, Vector[][] arrvector3, Vector[][] arrvector4, byte[][][] arrby5, GMSSLeaf[] arrgMSSLeaf, GMSSLeaf[] arrgMSSLeaf2, GMSSLeaf[] arrgMSSLeaf3, int[] arrn2, byte[][] arrby6, GMSSRootCalc[] arrgMSSRootCalc, byte[][] arrby7, GMSSRootSig[] arrgMSSRootSig, GMSSParameters gMSSParameters) {
        this.index = arrn;
        this.currentSeed = arrby;
        this.nextNextSeed = arrby2;
        this.currentAuthPath = arrby3;
        this.nextAuthPath = arrby4;
        this.currentTreehash = arrtreehash;
        this.nextTreehash = arrtreehash2;
        this.currentStack = arrvector;
        this.nextStack = arrvector2;
        this.currentRetain = arrvector3;
        this.nextRetain = arrvector4;
        this.keep = arrby5;
        this.nextNextLeaf = arrgMSSLeaf;
        this.upperLeaf = arrgMSSLeaf2;
        this.upperTreehashLeaf = arrgMSSLeaf3;
        this.minTreehash = arrn2;
        this.nextRoot = arrby6;
        this.nextNextRoot = arrgMSSRootCalc;
        this.currentRootSig = arrby7;
        this.nextRootSig = arrgMSSRootSig;
        this.gmssPS = gMSSParameters;
    }

    private static Vector[] clone(Vector[] arrvector) {
        if (arrvector == null) {
            return null;
        }
        Vector[] arrvector2 = new Vector[arrvector.length];
        for (int i = 0; i != arrvector.length; ++i) {
            arrvector2[i] = new Vector((Collection)arrvector[i]);
        }
        return arrvector2;
    }

    private static GMSSLeaf[] clone(GMSSLeaf[] arrgMSSLeaf) {
        if (arrgMSSLeaf == null) {
            return null;
        }
        GMSSLeaf[] arrgMSSLeaf2 = new GMSSLeaf[arrgMSSLeaf.length];
        System.arraycopy((Object)arrgMSSLeaf, (int)0, (Object)arrgMSSLeaf2, (int)0, (int)arrgMSSLeaf.length);
        return arrgMSSLeaf2;
    }

    private static GMSSRootCalc[] clone(GMSSRootCalc[] arrgMSSRootCalc) {
        if (arrgMSSRootCalc == null) {
            return null;
        }
        GMSSRootCalc[] arrgMSSRootCalc2 = new GMSSRootCalc[arrgMSSRootCalc.length];
        System.arraycopy((Object)arrgMSSRootCalc, (int)0, (Object)arrgMSSRootCalc2, (int)0, (int)arrgMSSRootCalc.length);
        return arrgMSSRootCalc2;
    }

    private static GMSSRootSig[] clone(GMSSRootSig[] arrgMSSRootSig) {
        if (arrgMSSRootSig == null) {
            return null;
        }
        GMSSRootSig[] arrgMSSRootSig2 = new GMSSRootSig[arrgMSSRootSig.length];
        System.arraycopy((Object)arrgMSSRootSig, (int)0, (Object)arrgMSSRootSig2, (int)0, (int)arrgMSSRootSig.length);
        return arrgMSSRootSig2;
    }

    private static Treehash[] clone(Treehash[] arrtreehash) {
        if (arrtreehash == null) {
            return null;
        }
        Treehash[] arrtreehash2 = new Treehash[arrtreehash.length];
        System.arraycopy((Object)arrtreehash, (int)0, (Object)arrtreehash2, (int)0, (int)arrtreehash.length);
        return arrtreehash2;
    }

    private static byte[][] clone(byte[][] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[][] arrarrby = new byte[arrby.length][];
        for (int i = 0; i != arrby.length; ++i) {
            arrarrby[i] = Arrays.clone(arrby[i]);
        }
        return arrarrby;
    }

    private static Vector[][] clone(Vector[][] arrvector) {
        if (arrvector == null) {
            return null;
        }
        Vector[][] arrarrvector = new Vector[arrvector.length][];
        for (int i = 0; i != arrvector.length; ++i) {
            arrarrvector[i] = GMSSPrivateKeySpec.clone(arrvector[i]);
        }
        return arrarrvector;
    }

    private static Treehash[][] clone(Treehash[][] arrtreehash) {
        if (arrtreehash == null) {
            return null;
        }
        Treehash[][] arrtreehash2 = new Treehash[arrtreehash.length][];
        for (int i = 0; i != arrtreehash.length; ++i) {
            arrtreehash2[i] = GMSSPrivateKeySpec.clone(arrtreehash[i]);
        }
        return arrtreehash2;
    }

    private static byte[][][] clone(byte[][][] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[][][] arrarrby = new byte[arrby.length][][];
        for (int i = 0; i != arrby.length; ++i) {
            arrarrby[i] = GMSSPrivateKeySpec.clone(arrby[i]);
        }
        return arrarrby;
    }

    public byte[][][] getCurrentAuthPath() {
        return GMSSPrivateKeySpec.clone(this.currentAuthPath);
    }

    public Vector[][] getCurrentRetain() {
        return GMSSPrivateKeySpec.clone(this.currentRetain);
    }

    public byte[][] getCurrentRootSig() {
        return GMSSPrivateKeySpec.clone(this.currentRootSig);
    }

    public byte[][] getCurrentSeed() {
        return GMSSPrivateKeySpec.clone(this.currentSeed);
    }

    public Vector[] getCurrentStack() {
        return GMSSPrivateKeySpec.clone(this.currentStack);
    }

    public Treehash[][] getCurrentTreehash() {
        return GMSSPrivateKeySpec.clone(this.currentTreehash);
    }

    public GMSSParameters getGmssPS() {
        return this.gmssPS;
    }

    public int[] getIndex() {
        return Arrays.clone(this.index);
    }

    public byte[][][] getKeep() {
        return GMSSPrivateKeySpec.clone(this.keep);
    }

    public int[] getMinTreehash() {
        return Arrays.clone(this.minTreehash);
    }

    public byte[][][] getNextAuthPath() {
        return GMSSPrivateKeySpec.clone(this.nextAuthPath);
    }

    public GMSSLeaf[] getNextNextLeaf() {
        return GMSSPrivateKeySpec.clone(this.nextNextLeaf);
    }

    public GMSSRootCalc[] getNextNextRoot() {
        return GMSSPrivateKeySpec.clone(this.nextNextRoot);
    }

    public byte[][] getNextNextSeed() {
        return GMSSPrivateKeySpec.clone(this.nextNextSeed);
    }

    public Vector[][] getNextRetain() {
        return GMSSPrivateKeySpec.clone(this.nextRetain);
    }

    public byte[][] getNextRoot() {
        return GMSSPrivateKeySpec.clone(this.nextRoot);
    }

    public GMSSRootSig[] getNextRootSig() {
        return GMSSPrivateKeySpec.clone(this.nextRootSig);
    }

    public Vector[] getNextStack() {
        return GMSSPrivateKeySpec.clone(this.nextStack);
    }

    public Treehash[][] getNextTreehash() {
        return GMSSPrivateKeySpec.clone(this.nextTreehash);
    }

    public GMSSLeaf[] getUpperLeaf() {
        return GMSSPrivateKeySpec.clone(this.upperLeaf);
    }

    public GMSSLeaf[] getUpperTreehashLeaf() {
        return GMSSPrivateKeySpec.clone(this.upperTreehashLeaf);
    }
}

