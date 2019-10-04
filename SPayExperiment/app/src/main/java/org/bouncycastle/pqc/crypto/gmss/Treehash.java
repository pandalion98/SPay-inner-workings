/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Integer
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
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.encoders.Hex;

public class Treehash {
    private byte[] firstNode;
    private int firstNodeHeight;
    private Vector heightOfNodes;
    private boolean isFinished;
    private boolean isInitialized;
    private int maxHeight;
    private Digest messDigestTree;
    private byte[] seedActive;
    private boolean seedInitialized;
    private byte[] seedNext;
    private int tailLength;
    private Vector tailStack;

    public Treehash(Vector vector, int n, Digest digest) {
        this.tailStack = vector;
        this.maxHeight = n;
        this.firstNode = null;
        this.isInitialized = false;
        this.isFinished = false;
        this.seedInitialized = false;
        this.messDigestTree = digest;
        this.seedNext = new byte[this.messDigestTree.getDigestSize()];
        this.seedActive = new byte[this.messDigestTree.getDigestSize()];
    }

    /*
     * Enabled aggressive block sorting
     */
    public Treehash(Digest digest, byte[][] arrby, int[] arrn) {
        int n = 0;
        this.messDigestTree = digest;
        this.maxHeight = arrn[0];
        this.tailLength = arrn[1];
        this.firstNodeHeight = arrn[2];
        this.isFinished = arrn[3] == 1;
        this.isInitialized = arrn[4] == 1;
        this.seedInitialized = arrn[5] == 1;
        this.heightOfNodes = new Vector();
        for (int i = 0; i < this.tailLength; ++i) {
            this.heightOfNodes.addElement((Object)Integers.valueOf(arrn[i + 6]));
        }
        this.firstNode = arrby[0];
        this.seedActive = arrby[1];
        this.seedNext = arrby[2];
        this.tailStack = new Vector();
        while (n < this.tailLength) {
            this.tailStack.addElement((Object)arrby[n + 3]);
            ++n;
        }
        return;
    }

    public void destroy() {
        this.isInitialized = false;
        this.isFinished = false;
        this.firstNode = null;
        this.tailLength = 0;
        this.firstNodeHeight = -1;
    }

    public byte[] getFirstNode() {
        return this.firstNode;
    }

    public int getFirstNodeHeight() {
        if (this.firstNode == null) {
            return this.maxHeight;
        }
        return this.firstNodeHeight;
    }

    public int getLowestNodeHeight() {
        if (this.firstNode == null) {
            return this.maxHeight;
        }
        if (this.tailLength == 0) {
            return this.firstNodeHeight;
        }
        return Math.min((int)this.firstNodeHeight, (int)((Integer)this.heightOfNodes.lastElement()));
    }

    public byte[] getSeedActive() {
        return this.seedActive;
    }

    public byte[][] getStatByte() {
        int[] arrn = new int[]{3 + this.tailLength, this.messDigestTree.getDigestSize()};
        byte[][] arrby = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        arrby[0] = this.firstNode;
        arrby[1] = this.seedActive;
        arrby[2] = this.seedNext;
        for (int i = 0; i < this.tailLength; ++i) {
            arrby[i + 3] = (byte[])this.tailStack.elementAt(i);
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int[] getStatInt() {
        int[] arrn = new int[6 + this.tailLength];
        arrn[0] = this.maxHeight;
        arrn[1] = this.tailLength;
        arrn[2] = this.firstNodeHeight;
        arrn[3] = this.isFinished ? 1 : 0;
        arrn[4] = this.isInitialized ? 1 : 0;
        arrn[5] = this.seedInitialized ? 1 : 0;
        int n = 0;
        while (n < this.tailLength) {
            arrn[n + 6] = (Integer)this.heightOfNodes.elementAt(n);
            ++n;
        }
        return arrn;
    }

    public Vector getTailStack() {
        return this.tailStack;
    }

    public void initialize() {
        if (!this.seedInitialized) {
            System.err.println("Seed " + this.maxHeight + " not initialized");
            return;
        }
        this.heightOfNodes = new Vector();
        this.tailLength = 0;
        this.firstNode = null;
        this.firstNodeHeight = -1;
        this.isInitialized = true;
        System.arraycopy((Object)this.seedNext, (int)0, (Object)this.seedActive, (int)0, (int)this.messDigestTree.getDigestSize());
    }

    public void initializeSeed(byte[] arrby) {
        System.arraycopy((Object)arrby, (int)0, (Object)this.seedNext, (int)0, (int)this.messDigestTree.getDigestSize());
        this.seedInitialized = true;
    }

    public void setFirstNode(byte[] arrby) {
        if (!this.isInitialized) {
            this.initialize();
        }
        this.firstNode = arrby;
        this.firstNodeHeight = this.maxHeight;
        this.isFinished = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        int n;
        String string = "Treehash    : ";
        int n2 = 0;
        do {
            int n3 = 6 + this.tailLength;
            n = 0;
            if (n2 >= n3) break;
            string = string + this.getStatInt()[n2] + " ";
            ++n2;
        } while (true);
        while (n < 3 + this.tailLength) {
            string = this.getStatByte()[n] != null ? string + new String(Hex.encode(this.getStatByte()[n])) + " " : string + "null ";
            ++n;
        }
        return string + "  " + this.messDigestTree.getDigestSize();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void update(GMSSRandom gMSSRandom, byte[] arrby) {
        if (this.isFinished) {
            System.err.println("No more update possible for treehash instance!");
            return;
        } else {
            if (!this.isInitialized) {
                System.err.println("Treehash instance not initialized before update");
                return;
            }
            new byte[this.messDigestTree.getDigestSize()];
            gMSSRandom.nextSeed(this.seedActive);
            if (this.firstNode == null) {
                this.firstNode = arrby;
                this.firstNodeHeight = 0;
            } else {
                int n = 0;
                while (this.tailLength > 0 && n == (Integer)this.heightOfNodes.lastElement()) {
                    byte[] arrby2 = new byte[this.messDigestTree.getDigestSize() << 1];
                    System.arraycopy((Object)this.tailStack.lastElement(), (int)0, (Object)arrby2, (int)0, (int)this.messDigestTree.getDigestSize());
                    this.tailStack.removeElementAt(-1 + this.tailStack.size());
                    this.heightOfNodes.removeElementAt(-1 + this.heightOfNodes.size());
                    System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)this.messDigestTree.getDigestSize(), (int)this.messDigestTree.getDigestSize());
                    this.messDigestTree.update(arrby2, 0, arrby2.length);
                    arrby = new byte[this.messDigestTree.getDigestSize()];
                    this.messDigestTree.doFinal(arrby, 0);
                    int n2 = n + 1;
                    this.tailLength = -1 + this.tailLength;
                    n = n2;
                }
                this.tailStack.addElement((Object)arrby);
                this.heightOfNodes.addElement((Object)Integers.valueOf(n));
                this.tailLength = 1 + this.tailLength;
                if ((Integer)this.heightOfNodes.lastElement() == this.firstNodeHeight) {
                    byte[] arrby3 = new byte[this.messDigestTree.getDigestSize() << 1];
                    System.arraycopy((Object)this.firstNode, (int)0, (Object)arrby3, (int)0, (int)this.messDigestTree.getDigestSize());
                    System.arraycopy((Object)this.tailStack.lastElement(), (int)0, (Object)arrby3, (int)this.messDigestTree.getDigestSize(), (int)this.messDigestTree.getDigestSize());
                    this.tailStack.removeElementAt(-1 + this.tailStack.size());
                    this.heightOfNodes.removeElementAt(-1 + this.heightOfNodes.size());
                    this.messDigestTree.update(arrby3, 0, arrby3.length);
                    this.firstNode = new byte[this.messDigestTree.getDigestSize()];
                    this.messDigestTree.doFinal(this.firstNode, 0);
                    this.firstNodeHeight = 1 + this.firstNodeHeight;
                    this.tailLength = 0;
                }
            }
            if (this.firstNodeHeight != this.maxHeight) return;
            {
                this.isFinished = true;
                return;
            }
        }
    }

    public void updateNextSeed(GMSSRandom gMSSRandom) {
        gMSSRandom.nextSeed(this.seedNext);
    }

    public boolean wasFinished() {
        return this.isFinished;
    }

    public boolean wasInitialized() {
        return this.isInitialized;
    }
}

