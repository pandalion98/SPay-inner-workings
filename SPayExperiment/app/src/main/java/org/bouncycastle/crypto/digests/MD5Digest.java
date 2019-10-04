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

public class MD5Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private static final int S11 = 7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 = 5;
    private static final int S22 = 9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 = 4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 = 6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] X = new int[16];
    private int xOff;

    public MD5Digest() {
        this.reset();
    }

    public MD5Digest(MD5Digest mD5Digest) {
        super(mD5Digest);
        this.copyIn(mD5Digest);
    }

    private int F(int n2, int n3, int n4) {
        return n2 & n3 | n4 & ~n2;
    }

    private int G(int n2, int n3, int n4) {
        return n2 & n4 | n3 & ~n4;
    }

    private int H(int n2, int n3, int n4) {
        return n4 ^ (n2 ^ n3);
    }

    private int K(int n2, int n3, int n4) {
        return n3 ^ (n2 | ~n4);
    }

    private void copyIn(MD5Digest mD5Digest) {
        super.copyIn(mD5Digest);
        this.H1 = mD5Digest.H1;
        this.H2 = mD5Digest.H2;
        this.H3 = mD5Digest.H3;
        this.H4 = mD5Digest.H4;
        System.arraycopy((Object)mD5Digest.X, (int)0, (Object)this.X, (int)0, (int)mD5Digest.X.length);
        this.xOff = mD5Digest.xOff;
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
        return new MD5Digest(this);
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
        return "MD5";
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
        int n6 = n3 + this.rotateLeft(-680876936 + (n2 + this.F(n3, n4, n5) + this.X[0]), 7);
        int n7 = n6 + this.rotateLeft(-389564586 + (n5 + this.F(n6, n3, n4) + this.X[1]), 12);
        int n8 = n7 + this.rotateLeft(606105819 + (n4 + this.F(n7, n6, n3) + this.X[2]), 17);
        int n9 = n8 + this.rotateLeft(-1044525330 + (n3 + this.F(n8, n7, n6) + this.X[3]), 22);
        int n10 = n9 + this.rotateLeft(-176418897 + (n6 + this.F(n9, n8, n7) + this.X[4]), 7);
        int n11 = n10 + this.rotateLeft(1200080426 + (n7 + this.F(n10, n9, n8) + this.X[5]), 12);
        int n12 = n11 + this.rotateLeft(-1473231341 + (n8 + this.F(n11, n10, n9) + this.X[6]), 17);
        int n13 = n12 + this.rotateLeft(-45705983 + (n9 + this.F(n12, n11, n10) + this.X[7]), 22);
        int n14 = n13 + this.rotateLeft(1770035416 + (n10 + this.F(n13, n12, n11) + this.X[8]), 7);
        int n15 = n14 + this.rotateLeft(-1958414417 + (n11 + this.F(n14, n13, n12) + this.X[9]), 12);
        int n16 = n15 + this.rotateLeft(-42063 + (n12 + this.F(n15, n14, n13) + this.X[10]), 17);
        int n17 = n16 + this.rotateLeft(-1990404162 + (n13 + this.F(n16, n15, n14) + this.X[11]), 22);
        int n18 = n17 + this.rotateLeft(1804603682 + (n14 + this.F(n17, n16, n15) + this.X[12]), 7);
        int n19 = n18 + this.rotateLeft(-40341101 + (n15 + this.F(n18, n17, n16) + this.X[13]), 12);
        int n20 = n19 + this.rotateLeft(-1502002290 + (n16 + this.F(n19, n18, n17) + this.X[14]), 17);
        int n21 = n20 + this.rotateLeft(1236535329 + (n17 + this.F(n20, n19, n18) + this.X[15]), 22);
        int n22 = n21 + this.rotateLeft(-165796510 + (n18 + this.G(n21, n20, n19) + this.X[1]), 5);
        int n23 = n22 + this.rotateLeft(-1069501632 + (n19 + this.G(n22, n21, n20) + this.X[6]), 9);
        int n24 = n23 + this.rotateLeft(643717713 + (n20 + this.G(n23, n22, n21) + this.X[11]), 14);
        int n25 = n24 + this.rotateLeft(-373897302 + (n21 + this.G(n24, n23, n22) + this.X[0]), 20);
        int n26 = n25 + this.rotateLeft(-701558691 + (n22 + this.G(n25, n24, n23) + this.X[5]), 5);
        int n27 = n26 + this.rotateLeft(38016083 + (n23 + this.G(n26, n25, n24) + this.X[10]), 9);
        int n28 = n27 + this.rotateLeft(-660478335 + (n24 + this.G(n27, n26, n25) + this.X[15]), 14);
        int n29 = n28 + this.rotateLeft(-405537848 + (n25 + this.G(n28, n27, n26) + this.X[4]), 20);
        int n30 = n29 + this.rotateLeft(568446438 + (n26 + this.G(n29, n28, n27) + this.X[9]), 5);
        int n31 = n30 + this.rotateLeft(-1019803690 + (n27 + this.G(n30, n29, n28) + this.X[14]), 9);
        int n32 = n31 + this.rotateLeft(-187363961 + (n28 + this.G(n31, n30, n29) + this.X[3]), 14);
        int n33 = n32 + this.rotateLeft(1163531501 + (n29 + this.G(n32, n31, n30) + this.X[8]), 20);
        int n34 = n33 + this.rotateLeft(-1444681467 + (n30 + this.G(n33, n32, n31) + this.X[13]), 5);
        int n35 = n34 + this.rotateLeft(-51403784 + (n31 + this.G(n34, n33, n32) + this.X[2]), 9);
        int n36 = n35 + this.rotateLeft(1735328473 + (n32 + this.G(n35, n34, n33) + this.X[7]), 14);
        int n37 = n36 + this.rotateLeft(-1926607734 + (n33 + this.G(n36, n35, n34) + this.X[12]), 20);
        int n38 = n37 + this.rotateLeft(-378558 + (n34 + this.H(n37, n36, n35) + this.X[5]), 4);
        int n39 = n38 + this.rotateLeft(-2022574463 + (n35 + this.H(n38, n37, n36) + this.X[8]), 11);
        int n40 = n39 + this.rotateLeft(1839030562 + (n36 + this.H(n39, n38, n37) + this.X[11]), 16);
        int n41 = n40 + this.rotateLeft(-35309556 + (n37 + this.H(n40, n39, n38) + this.X[14]), 23);
        int n42 = n41 + this.rotateLeft(-1530992060 + (n38 + this.H(n41, n40, n39) + this.X[1]), 4);
        int n43 = n42 + this.rotateLeft(1272893353 + (n39 + this.H(n42, n41, n40) + this.X[4]), 11);
        int n44 = n43 + this.rotateLeft(-155497632 + (n40 + this.H(n43, n42, n41) + this.X[7]), 16);
        int n45 = n44 + this.rotateLeft(-1094730640 + (n41 + this.H(n44, n43, n42) + this.X[10]), 23);
        int n46 = n45 + this.rotateLeft(681279174 + (n42 + this.H(n45, n44, n43) + this.X[13]), 4);
        int n47 = n46 + this.rotateLeft(-358537222 + (n43 + this.H(n46, n45, n44) + this.X[0]), 11);
        int n48 = n47 + this.rotateLeft(-722521979 + (n44 + this.H(n47, n46, n45) + this.X[3]), 16);
        int n49 = n48 + this.rotateLeft(76029189 + (n45 + this.H(n48, n47, n46) + this.X[6]), 23);
        int n50 = n49 + this.rotateLeft(-640364487 + (n46 + this.H(n49, n48, n47) + this.X[9]), 4);
        int n51 = n50 + this.rotateLeft(-421815835 + (n47 + this.H(n50, n49, n48) + this.X[12]), 11);
        int n52 = n51 + this.rotateLeft(530742520 + (n48 + this.H(n51, n50, n49) + this.X[15]), 16);
        int n53 = n52 + this.rotateLeft(-995338651 + (n49 + this.H(n52, n51, n50) + this.X[2]), 23);
        int n54 = n53 + this.rotateLeft(-198630844 + (n50 + this.K(n53, n52, n51) + this.X[0]), 6);
        int n55 = n54 + this.rotateLeft(1126891415 + (n51 + this.K(n54, n53, n52) + this.X[7]), 10);
        int n56 = n55 + this.rotateLeft(-1416354905 + (n52 + this.K(n55, n54, n53) + this.X[14]), 15);
        int n57 = n56 + this.rotateLeft(-57434055 + (n53 + this.K(n56, n55, n54) + this.X[5]), 21);
        int n58 = n57 + this.rotateLeft(1700485571 + (n54 + this.K(n57, n56, n55) + this.X[12]), 6);
        int n59 = n58 + this.rotateLeft(-1894986606 + (n55 + this.K(n58, n57, n56) + this.X[3]), 10);
        int n60 = n59 + this.rotateLeft(-1051523 + (n56 + this.K(n59, n58, n57) + this.X[10]), 15);
        int n61 = n60 + this.rotateLeft(-2054922799 + (n57 + this.K(n60, n59, n58) + this.X[1]), 21);
        int n62 = n61 + this.rotateLeft(1873313359 + (n58 + this.K(n61, n60, n59) + this.X[8]), 6);
        int n63 = n62 + this.rotateLeft(-30611744 + (n59 + this.K(n62, n61, n60) + this.X[15]), 10);
        int n64 = n63 + this.rotateLeft(-1560198380 + (n60 + this.K(n63, n62, n61) + this.X[6]), 15);
        int n65 = n64 + this.rotateLeft(1309151649 + (n61 + this.K(n64, n63, n62) + this.X[13]), 21);
        int n66 = n65 + this.rotateLeft(-145523070 + (n62 + this.K(n65, n64, n63) + this.X[4]), 6);
        int n67 = n66 + this.rotateLeft(-1120210379 + (n63 + this.K(n66, n65, n64) + this.X[11]), 10);
        int n68 = n67 + this.rotateLeft(718787259 + (n64 + this.K(n67, n66, n65) + this.X[2]), 15);
        int n69 = n68 + this.rotateLeft(-343485551 + (n65 + this.K(n68, n67, n66) + this.X[9]), 21);
        this.H1 = n66 + this.H1;
        this.H2 = n69 + this.H2;
        this.H3 = n68 + this.H3;
        this.H4 = n67 + this.H4;
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
        this.copyIn((MD5Digest)memoable);
    }
}

