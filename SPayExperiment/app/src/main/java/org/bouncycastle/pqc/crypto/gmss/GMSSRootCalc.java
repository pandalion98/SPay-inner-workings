/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Array
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.GMSSDigestProvider;
import org.bouncycastle.pqc.crypto.gmss.GMSSUtils;
import org.bouncycastle.pqc.crypto.gmss.Treehash;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.encoders.Hex;

public class GMSSRootCalc {
    private byte[][] AuthPath;
    private int K;
    private GMSSDigestProvider digestProvider;
    private int heightOfNextSeed;
    private Vector heightOfNodes;
    private int heightOfTree;
    private int[] index;
    private int indexForNextSeed;
    private boolean isFinished;
    private boolean isInitialized;
    private int mdLength;
    private Digest messDigestTree;
    private Vector[] retain;
    private byte[] root;
    private Vector tailStack;
    private Treehash[] treehash;

    public GMSSRootCalc(int n, int n2, GMSSDigestProvider gMSSDigestProvider) {
        this.heightOfTree = n;
        this.digestProvider = gMSSDigestProvider;
        this.messDigestTree = gMSSDigestProvider.get();
        this.mdLength = this.messDigestTree.getDigestSize();
        this.K = n2;
        this.index = new int[n];
        int[] arrn = new int[]{n, this.mdLength};
        this.AuthPath = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        this.root = new byte[this.mdLength];
        this.retain = new Vector[-1 + this.K];
        for (int i = 0; i < n2 - 1; ++i) {
            this.retain[i] = new Vector();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public GMSSRootCalc(Digest digest, byte[][] arrby, int[] arrn, Treehash[] arrtreehash, Vector[] arrvector) {
        int n = 0;
        this.messDigestTree = this.digestProvider.get();
        this.digestProvider = this.digestProvider;
        this.heightOfTree = arrn[0];
        this.mdLength = arrn[1];
        this.K = arrn[2];
        this.indexForNextSeed = arrn[3];
        this.heightOfNextSeed = arrn[4];
        this.isFinished = arrn[5] == 1;
        this.isInitialized = arrn[6] == 1;
        int n2 = arrn[7];
        this.index = new int[this.heightOfTree];
        for (int i = 0; i < this.heightOfTree; ++i) {
            this.index[i] = arrn[i + 8];
        }
        this.heightOfNodes = new Vector();
        for (int i = 0; i < n2; ++i) {
            this.heightOfNodes.addElement((Object)Integers.valueOf(arrn[i + (8 + this.heightOfTree)]));
        }
        this.root = arrby[0];
        int[] arrn2 = new int[]{this.heightOfTree, this.mdLength};
        this.AuthPath = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn2);
        for (int i = 0; i < this.heightOfTree; ++i) {
            this.AuthPath[i] = arrby[i + 1];
        }
        this.tailStack = new Vector();
        do {
            if (n >= n2) {
                this.treehash = GMSSUtils.clone(arrtreehash);
                this.retain = GMSSUtils.clone(arrvector);
                return;
            }
            this.tailStack.addElement((Object)arrby[n + (1 + this.heightOfTree)]);
            ++n;
        } while (true);
    }

    public byte[][] getAuthPath() {
        return GMSSUtils.clone(this.AuthPath);
    }

    public Vector[] getRetain() {
        return GMSSUtils.clone(this.retain);
    }

    public byte[] getRoot() {
        return Arrays.clone(this.root);
    }

    public Vector getStack() {
        Vector vector = new Vector();
        Enumeration enumeration = this.tailStack.elements();
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
        return vector;
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[][] getStatByte() {
        int n = this.tailStack == null ? 0 : this.tailStack.size();
        int[] arrn = new int[]{n + (1 + this.heightOfTree), 64};
        byte[][] arrby = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        arrby[0] = this.root;
        for (int i = 0; i < this.heightOfTree; ++i) {
            arrby[i + 1] = this.AuthPath[i];
        }
        int n2 = 0;
        while (n2 < n) {
            arrby[n2 + (1 + this.heightOfTree)] = (byte[])this.tailStack.elementAt(n2);
            ++n2;
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int[] getStatInt() {
        int n = this.tailStack == null ? 0 : this.tailStack.size();
        int[] arrn = new int[n + (8 + this.heightOfTree)];
        arrn[0] = this.heightOfTree;
        arrn[1] = this.mdLength;
        arrn[2] = this.K;
        arrn[3] = this.indexForNextSeed;
        arrn[4] = this.heightOfNextSeed;
        arrn[5] = this.isFinished ? 1 : 0;
        arrn[6] = this.isInitialized ? 1 : 0;
        arrn[7] = n;
        for (int i = 0; i < this.heightOfTree; ++i) {
            arrn[i + 8] = this.index[i];
        }
        int n2 = 0;
        while (n2 < n) {
            arrn[n2 + (8 + this.heightOfTree)] = (Integer)this.heightOfNodes.elementAt(n2);
            ++n2;
        }
        return arrn;
    }

    public Treehash[] getTreehash() {
        return GMSSUtils.clone(this.treehash);
    }

    public void initialize(Vector vector) {
        this.treehash = new Treehash[this.heightOfTree - this.K];
        for (int i = 0; i < this.heightOfTree - this.K; ++i) {
            this.treehash[i] = new Treehash(vector, i, this.digestProvider.get());
        }
        this.index = new int[this.heightOfTree];
        int[] arrn = new int[]{this.heightOfTree, this.mdLength};
        this.AuthPath = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        this.root = new byte[this.mdLength];
        this.tailStack = new Vector();
        this.heightOfNodes = new Vector();
        this.isInitialized = true;
        this.isFinished = false;
        for (int i = 0; i < this.heightOfTree; ++i) {
            this.index[i] = -1;
        }
        this.retain = new Vector[-1 + this.K];
        for (int i = 0; i < -1 + this.K; ++i) {
            this.retain[i] = new Vector();
        }
        this.indexForNextSeed = 3;
        this.heightOfNextSeed = 0;
    }

    public void initializeTreehashSeed(byte[] arrby, int n) {
        this.treehash[n].initializeSeed(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        int n;
        int n2 = this.tailStack == null ? 0 : this.tailStack.size();
        String string = "";
        int n3 = 0;
        do {
            int n4 = n2 + (8 + this.heightOfTree);
            n = 0;
            if (n3 >= n4) break;
            string = string + this.getStatInt()[n3] + " ";
            ++n3;
        } while (true);
        while (n < n2 + (1 + this.heightOfTree)) {
            string = string + new String(Hex.encode(this.getStatByte()[n])) + " ";
            ++n;
        }
        return string + "  " + this.digestProvider.get().getDigestSize();
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void update(byte[] arrby) {
        if (this.isFinished) {
            System.out.print("Too much updates for Tree!!");
            return;
        }
        if (!this.isInitialized) {
            System.err.println("GMSSRootCalc not initialized!");
            return;
        }
        int[] arrn = this.index;
        arrn[0] = 1 + arrn[0];
        if (this.index[0] == 1) {
            System.arraycopy((Object)arrby, (int)0, (Object)this.AuthPath[0], (int)0, (int)this.mdLength);
        } else if (this.index[0] == 3 && this.heightOfTree > this.K) {
            this.treehash[0].setFirstNode(arrby);
        }
        if ((-3 + this.index[0]) % 2 == 0 && this.index[0] >= 3 && this.heightOfTree == this.K) {
            this.retain[0].insertElementAt((Object)arrby, 0);
        }
        if (this.index[0] == 0) {
            this.tailStack.addElement((Object)arrby);
            this.heightOfNodes.addElement((Object)Integers.valueOf(0));
            return;
        }
        byte[] arrby2 = new byte[this.mdLength];
        byte[] arrby3 = new byte[this.mdLength << 1];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)this.mdLength);
        int n = 0;
        byte[] arrby4 = arrby2;
        do {
            int n2;
            if (this.tailStack.size() > 0 && n == (Integer)this.heightOfNodes.lastElement()) {
                System.arraycopy((Object)this.tailStack.lastElement(), (int)0, (Object)arrby3, (int)0, (int)this.mdLength);
                this.tailStack.removeElementAt(-1 + this.tailStack.size());
                this.heightOfNodes.removeElementAt(-1 + this.heightOfNodes.size());
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)this.mdLength, (int)this.mdLength);
                this.messDigestTree.update(arrby3, 0, arrby3.length);
                arrby4 = new byte[this.messDigestTree.getDigestSize()];
                this.messDigestTree.doFinal(arrby4, 0);
                n2 = n + 1;
                if (n2 < this.heightOfTree) {
                    int[] arrn2 = this.index;
                    arrn2[n2] = 1 + arrn2[n2];
                    if (this.index[n2] == 1) {
                        System.arraycopy((Object)arrby4, (int)0, (Object)this.AuthPath[n2], (int)0, (int)this.mdLength);
                    }
                    if (n2 >= this.heightOfTree - this.K) {
                        if (n2 == 0) {
                            System.out.println("M\ufffd\ufffd\ufffdP");
                        }
                        if ((-3 + this.index[n2]) % 2 == 0 && this.index[n2] >= 3) {
                            this.retain[n2 - (this.heightOfTree - this.K)].insertElementAt((Object)arrby4, 0);
                            n = n2;
                            continue;
                        }
                    } else if (this.index[n2] == 3) {
                        this.treehash[n2].setFirstNode(arrby4);
                        n = n2;
                        continue;
                    }
                }
            } else {
                this.tailStack.addElement((Object)arrby4);
                this.heightOfNodes.addElement((Object)Integers.valueOf(n));
                if (n != this.heightOfTree) return;
                this.isFinished = true;
                this.isInitialized = false;
                this.root = (byte[])this.tailStack.lastElement();
                return;
            }
            n = n2;
        } while (true);
    }

    public void update(byte[] arrby, byte[] arrby2) {
        if (this.heightOfNextSeed < this.heightOfTree - this.K && -2 + this.indexForNextSeed == this.index[0]) {
            this.initializeTreehashSeed(arrby, this.heightOfNextSeed);
            this.heightOfNextSeed = 1 + this.heightOfNextSeed;
            this.indexForNextSeed = 2 * this.indexForNextSeed;
        }
        this.update(arrby2);
    }

    public boolean wasFinished() {
        return this.isFinished;
    }

    public boolean wasInitialized() {
        return this.isInitialized;
    }
}

