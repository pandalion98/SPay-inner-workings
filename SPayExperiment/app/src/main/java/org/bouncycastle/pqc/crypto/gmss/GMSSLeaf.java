/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.pqc.crypto.gmss;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class GMSSLeaf {
    private byte[] concHashs;
    private GMSSRandom gmssRandom;
    private int i;
    private int j;
    private int keysize;
    private byte[] leaf;
    private int mdsize;
    private Digest messDigestOTS;
    byte[] privateKeyOTS;
    private byte[] seed;
    private int steps;
    private int two_power_w;
    private int w;

    GMSSLeaf(Digest digest, int n, int n2) {
        this.w = n;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int n3 = (int)Math.ceil((double)((double)(this.mdsize << 3) / (double)n));
        this.keysize = n3 + (int)Math.ceil((double)((double)this.getLog(1 + (n3 << n)) / (double)n));
        this.two_power_w = 1 << n;
        this.steps = (int)Math.ceil((double)((double)(1 + (-1 + (1 << n)) * this.keysize + this.keysize) / (double)n2));
        this.seed = new byte[this.mdsize];
        this.leaf = new byte[this.mdsize];
        this.privateKeyOTS = new byte[this.mdsize];
        this.concHashs = new byte[this.mdsize * this.keysize];
    }

    public GMSSLeaf(Digest digest, int n, int n2, byte[] arrby) {
        this.w = n;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int n3 = (int)Math.ceil((double)((double)(this.mdsize << 3) / (double)n));
        this.keysize = n3 + (int)Math.ceil((double)((double)this.getLog(1 + (n3 << n)) / (double)n));
        this.two_power_w = 1 << n;
        this.steps = (int)Math.ceil((double)((double)(1 + (-1 + (1 << n)) * this.keysize + this.keysize) / (double)n2));
        this.seed = new byte[this.mdsize];
        this.leaf = new byte[this.mdsize];
        this.privateKeyOTS = new byte[this.mdsize];
        this.concHashs = new byte[this.mdsize * this.keysize];
        this.initLeafCalc(arrby);
    }

    public GMSSLeaf(Digest digest, byte[][] arrby, int[] arrn) {
        this.i = arrn[0];
        this.j = arrn[1];
        this.steps = arrn[2];
        this.w = arrn[3];
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int n = (int)Math.ceil((double)((double)(this.mdsize << 3) / (double)this.w));
        this.keysize = n + (int)Math.ceil((double)((double)this.getLog(1 + (n << this.w)) / (double)this.w));
        this.two_power_w = 1 << this.w;
        this.privateKeyOTS = arrby[0];
        this.seed = arrby[1];
        this.concHashs = arrby[2];
        this.leaf = arrby[3];
    }

    private GMSSLeaf(GMSSLeaf gMSSLeaf) {
        this.messDigestOTS = gMSSLeaf.messDigestOTS;
        this.mdsize = gMSSLeaf.mdsize;
        this.keysize = gMSSLeaf.keysize;
        this.gmssRandom = gMSSLeaf.gmssRandom;
        this.leaf = Arrays.clone(gMSSLeaf.leaf);
        this.concHashs = Arrays.clone(gMSSLeaf.concHashs);
        this.i = gMSSLeaf.i;
        this.j = gMSSLeaf.j;
        this.two_power_w = gMSSLeaf.two_power_w;
        this.w = gMSSLeaf.w;
        this.steps = gMSSLeaf.steps;
        this.seed = Arrays.clone(gMSSLeaf.seed);
        this.privateKeyOTS = Arrays.clone(gMSSLeaf.privateKeyOTS);
    }

    private int getLog(int n) {
        int n2 = 1;
        int n3 = 2;
        while (n3 < n) {
            n3 <<= 1;
            ++n2;
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLeafCalc() {
        byte[] arrby = new byte[this.messDigestOTS.getDigestSize()];
        int n = 0;
        do {
            if (n >= 10000 + this.steps) {
                throw new IllegalStateException("unable to updateLeaf in steps: " + this.steps + " " + this.i + " " + this.j);
            }
            if (this.i == this.keysize && this.j == -1 + this.two_power_w) {
                this.messDigestOTS.update(this.concHashs, 0, this.concHashs.length);
                this.leaf = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.leaf, 0);
                return;
            }
            if (this.i == 0 || this.j == -1 + this.two_power_w) {
                this.i = 1 + this.i;
                this.j = 0;
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = arrby;
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.j = 1 + this.j;
                if (this.j == -1 + this.two_power_w) {
                    System.arraycopy((Object)this.privateKeyOTS, (int)0, (Object)this.concHashs, (int)(this.mdsize * (-1 + this.i)), (int)this.mdsize);
                }
            }
            ++n;
        } while (true);
    }

    public byte[] getLeaf() {
        return Arrays.clone(this.leaf);
    }

    public byte[][] getStatByte() {
        byte[][] arrarrby = new byte[][]{new byte[this.mdsize], new byte[this.mdsize], new byte[this.mdsize * this.keysize], new byte[this.mdsize]};
        arrarrby[0] = this.privateKeyOTS;
        arrarrby[1] = this.seed;
        arrarrby[2] = this.concHashs;
        arrarrby[3] = this.leaf;
        return arrarrby;
    }

    public int[] getStatInt() {
        int[] arrn = new int[]{this.i, this.j, this.steps, this.w};
        return arrn;
    }

    void initLeafCalc(byte[] arrby) {
        this.i = 0;
        this.j = 0;
        byte[] arrby2 = new byte[this.mdsize];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)this.seed.length);
        this.seed = this.gmssRandom.nextSeed(arrby2);
    }

    GMSSLeaf nextLeaf() {
        GMSSLeaf gMSSLeaf = new GMSSLeaf(this);
        gMSSLeaf.updateLeafCalc();
        return gMSSLeaf;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        String string = "";
        for (int i = 0; i < 4; ++i) {
            string = string + this.getStatInt()[i] + " ";
        }
        String string2 = string + " " + this.mdsize + " " + this.keysize + " " + this.two_power_w + " ";
        byte[][] arrby = this.getStatByte();
        String string3 = string2;
        int n = 0;
        while (n < 4) {
            string3 = arrby[n] != null ? string3 + new String(Hex.encode(arrby[n])) + " " : string3 + "null ";
            ++n;
        }
        return string3;
    }
}

