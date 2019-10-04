/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.util.Memoable;

public class MD4Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private static final int S11 = 3;
    private static final int S12 = 7;
    private static final int S13 = 11;
    private static final int S14 = 19;
    private static final int S21 = 3;
    private static final int S22 = 5;
    private static final int S23 = 9;
    private static final int S24 = 13;
    private static final int S31 = 3;
    private static final int S32 = 9;
    private static final int S33 = 11;
    private static final int S34 = 15;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] X = new int[16];
    private int xOff;

    public MD4Digest() {
        this.reset();
    }

    public MD4Digest(MD4Digest mD4Digest) {
        super(mD4Digest);
        this.copyIn(mD4Digest);
    }

    private int F(int n2, int n3, int n4) {
        return n2 & n3 | n4 & ~n2;
    }

    private int G(int n2, int n3, int n4) {
        return n2 & n3 | n2 & n4 | n3 & n4;
    }

    private int H(int n2, int n3, int n4) {
        return n4 ^ (n2 ^ n3);
    }

    private void copyIn(MD4Digest mD4Digest) {
        super.copyIn(mD4Digest);
        this.H1 = mD4Digest.H1;
        this.H2 = mD4Digest.H2;
        this.H3 = mD4Digest.H3;
        this.H4 = mD4Digest.H4;
        System.arraycopy((Object)mD4Digest.X, (int)0, (Object)this.X, (int)0, (int)mD4Digest.X.length);
        this.xOff = mD4Digest.xOff;
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private void unpackWord(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
        arrby[n3 + 1] = (byte)(n2 >>> 8);
        arrby[n3 + 2] = (byte)(n2 >>> 16);
        arrby[n3 + 3] = (byte)(n2 >>> 24);
    }

    @Override
    public Memoable copy() {
        return new MD4Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        this.unpackWord(this.H1, arrby, n2);
        this.unpackWord(this.H2, arrby, n2 + 4);
        this.unpackWord(this.H3, arrby, n2 + 8);
        this.unpackWord(this.H4, arrby, n2 + 12);
        this.reset();
        return 16;
    }

    @Override
    public String getAlgorithmName() {
        return "MD4";
    }

    @Override
    public int getDigestSize() {
        return 16;
    }

    @Override
    protected void processBlock() {
        int n2 = this.H1;
        int n3 = this.H2;
        int n4 = this.H3;
        int n5 = this.H4;
        int n6 = this.rotateLeft(n2 + this.F(n3, n4, n5) + this.X[0], 3);
        int n7 = this.rotateLeft(n5 + this.F(n6, n3, n4) + this.X[1], 7);
        int n8 = this.rotateLeft(n4 + this.F(n7, n6, n3) + this.X[2], 11);
        int n9 = this.rotateLeft(n3 + this.F(n8, n7, n6) + this.X[3], 19);
        int n10 = this.rotateLeft(n6 + this.F(n9, n8, n7) + this.X[4], 3);
        int n11 = this.rotateLeft(n7 + this.F(n10, n9, n8) + this.X[5], 7);
        int n12 = this.rotateLeft(n8 + this.F(n11, n10, n9) + this.X[6], 11);
        int n13 = this.rotateLeft(n9 + this.F(n12, n11, n10) + this.X[7], 19);
        int n14 = this.rotateLeft(n10 + this.F(n13, n12, n11) + this.X[8], 3);
        int n15 = this.rotateLeft(n11 + this.F(n14, n13, n12) + this.X[9], 7);
        int n16 = this.rotateLeft(n12 + this.F(n15, n14, n13) + this.X[10], 11);
        int n17 = this.rotateLeft(n13 + this.F(n16, n15, n14) + this.X[11], 19);
        int n18 = this.rotateLeft(n14 + this.F(n17, n16, n15) + this.X[12], 3);
        int n19 = this.rotateLeft(n15 + this.F(n18, n17, n16) + this.X[13], 7);
        int n20 = this.rotateLeft(n16 + this.F(n19, n18, n17) + this.X[14], 11);
        int n21 = this.rotateLeft(n17 + this.F(n20, n19, n18) + this.X[15], 19);
        int n22 = this.rotateLeft(1518500249 + (n18 + this.G(n21, n20, n19) + this.X[0]), 3);
        int n23 = this.rotateLeft(1518500249 + (n19 + this.G(n22, n21, n20) + this.X[4]), 5);
        int n24 = this.rotateLeft(1518500249 + (n20 + this.G(n23, n22, n21) + this.X[8]), 9);
        int n25 = this.rotateLeft(1518500249 + (n21 + this.G(n24, n23, n22) + this.X[12]), 13);
        int n26 = this.rotateLeft(1518500249 + (n22 + this.G(n25, n24, n23) + this.X[1]), 3);
        int n27 = this.rotateLeft(1518500249 + (n23 + this.G(n26, n25, n24) + this.X[5]), 5);
        int n28 = this.rotateLeft(1518500249 + (n24 + this.G(n27, n26, n25) + this.X[9]), 9);
        int n29 = this.rotateLeft(1518500249 + (n25 + this.G(n28, n27, n26) + this.X[13]), 13);
        int n30 = this.rotateLeft(1518500249 + (n26 + this.G(n29, n28, n27) + this.X[2]), 3);
        int n31 = this.rotateLeft(1518500249 + (n27 + this.G(n30, n29, n28) + this.X[6]), 5);
        int n32 = this.rotateLeft(1518500249 + (n28 + this.G(n31, n30, n29) + this.X[10]), 9);
        int n33 = this.rotateLeft(1518500249 + (n29 + this.G(n32, n31, n30) + this.X[14]), 13);
        int n34 = this.rotateLeft(1518500249 + (n30 + this.G(n33, n32, n31) + this.X[3]), 3);
        int n35 = this.rotateLeft(1518500249 + (n31 + this.G(n34, n33, n32) + this.X[7]), 5);
        int n36 = this.rotateLeft(1518500249 + (n32 + this.G(n35, n34, n33) + this.X[11]), 9);
        int n37 = this.rotateLeft(1518500249 + (n33 + this.G(n36, n35, n34) + this.X[15]), 13);
        int n38 = this.rotateLeft(1859775393 + (n34 + this.H(n37, n36, n35) + this.X[0]), 3);
        int n39 = this.rotateLeft(1859775393 + (n35 + this.H(n38, n37, n36) + this.X[8]), 9);
        int n40 = this.rotateLeft(1859775393 + (n36 + this.H(n39, n38, n37) + this.X[4]), 11);
        int n41 = this.rotateLeft(1859775393 + (n37 + this.H(n40, n39, n38) + this.X[12]), 15);
        int n42 = this.rotateLeft(1859775393 + (n38 + this.H(n41, n40, n39) + this.X[2]), 3);
        int n43 = this.rotateLeft(1859775393 + (n39 + this.H(n42, n41, n40) + this.X[10]), 9);
        int n44 = this.rotateLeft(1859775393 + (n40 + this.H(n43, n42, n41) + this.X[6]), 11);
        int n45 = this.rotateLeft(1859775393 + (n41 + this.H(n44, n43, n42) + this.X[14]), 15);
        int n46 = this.rotateLeft(1859775393 + (n42 + this.H(n45, n44, n43) + this.X[1]), 3);
        int n47 = this.rotateLeft(1859775393 + (n43 + this.H(n46, n45, n44) + this.X[9]), 9);
        int n48 = this.rotateLeft(1859775393 + (n44 + this.H(n47, n46, n45) + this.X[5]), 11);
        int n49 = this.rotateLeft(1859775393 + (n45 + this.H(n48, n47, n46) + this.X[13]), 15);
        int n50 = this.rotateLeft(1859775393 + (n46 + this.H(n49, n48, n47) + this.X[3]), 3);
        int n51 = this.rotateLeft(1859775393 + (n47 + this.H(n50, n49, n48) + this.X[11]), 9);
        int n52 = this.rotateLeft(1859775393 + (n48 + this.H(n51, n50, n49) + this.X[7]), 11);
        int n53 = this.rotateLeft(1859775393 + (n49 + this.H(n52, n51, n50) + this.X[15]), 15);
        this.H1 = n50 + this.H1;
        this.H2 = n53 + this.H2;
        this.H3 = n52 + this.H3;
        this.H4 = n51 + this.H4;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    protected void processLength(long l2) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        this.X[14] = (int)(-1L & l2);
        this.X[15] = (int)(l2 >>> 32);
    }

    @Override
    protected void processWord(byte[] arrby, int n2) {
        int[] arrn = this.X;
        int n3 = this.xOff;
        this.xOff = n3 + 1;
        arrn[n3] = 255 & arrby[n2] | (255 & arrby[n2 + 1]) << 8 | (255 & arrby[n2 + 2]) << 16 | (255 & arrby[n2 + 3]) << 24;
        if (this.xOff == 16) {
            this.processBlock();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((MD4Digest)memoable);
    }
}

