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

public class RIPEMD128Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 16;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int[] X = new int[16];
    private int xOff;

    public RIPEMD128Digest() {
        this.reset();
    }

    public RIPEMD128Digest(RIPEMD128Digest rIPEMD128Digest) {
        super(rIPEMD128Digest);
        this.copyIn(rIPEMD128Digest);
    }

    private int F1(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n6 + (n2 + this.f1(n3, n4, n5)), n7);
    }

    private int F2(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(1518500249 + (n6 + (n2 + this.f2(n3, n4, n5))), n7);
    }

    private int F3(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(1859775393 + (n6 + (n2 + this.f3(n3, n4, n5))), n7);
    }

    private int F4(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(-1894007588 + (n6 + (n2 + this.f4(n3, n4, n5))), n7);
    }

    private int FF1(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(n6 + (n2 + this.f1(n3, n4, n5)), n7);
    }

    private int FF2(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(1836072691 + (n6 + (n2 + this.f2(n3, n4, n5))), n7);
    }

    private int FF3(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(1548603684 + (n6 + (n2 + this.f3(n3, n4, n5))), n7);
    }

    private int FF4(int n2, int n3, int n4, int n5, int n6, int n7) {
        return this.RL(1352829926 + (n6 + (n2 + this.f4(n3, n4, n5))), n7);
    }

    private int RL(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private void copyIn(RIPEMD128Digest rIPEMD128Digest) {
        super.copyIn(rIPEMD128Digest);
        this.H0 = rIPEMD128Digest.H0;
        this.H1 = rIPEMD128Digest.H1;
        this.H2 = rIPEMD128Digest.H2;
        this.H3 = rIPEMD128Digest.H3;
        System.arraycopy((Object)rIPEMD128Digest.X, (int)0, (Object)this.X, (int)0, (int)rIPEMD128Digest.X.length);
        this.xOff = rIPEMD128Digest.xOff;
    }

    private int f1(int n2, int n3, int n4) {
        return n4 ^ (n2 ^ n3);
    }

    private int f2(int n2, int n3, int n4) {
        return n2 & n3 | n4 & ~n2;
    }

    private int f3(int n2, int n3, int n4) {
        return n4 ^ (n2 | ~n3);
    }

    private int f4(int n2, int n3, int n4) {
        return n2 & n4 | n3 & ~n4;
    }

    private void unpackWord(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
        arrby[n3 + 1] = (byte)(n2 >>> 8);
        arrby[n3 + 2] = (byte)(n2 >>> 16);
        arrby[n3 + 3] = (byte)(n2 >>> 24);
    }

    @Override
    public Memoable copy() {
        return new RIPEMD128Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        this.unpackWord(this.H0, arrby, n2);
        this.unpackWord(this.H1, arrby, n2 + 4);
        this.unpackWord(this.H2, arrby, n2 + 8);
        this.unpackWord(this.H3, arrby, n2 + 12);
        this.reset();
        return 16;
    }

    @Override
    public String getAlgorithmName() {
        return "RIPEMD128";
    }

    @Override
    public int getDigestSize() {
        return 16;
    }

    @Override
    protected void processBlock() {
        int n2 = this.H0;
        int n3 = this.H1;
        int n4 = this.H2;
        int n5 = this.H3;
        int n6 = this.F1(n2, n3, n4, n5, this.X[0], 11);
        int n7 = this.F1(n5, n6, n3, n4, this.X[1], 14);
        int n8 = this.F1(n4, n7, n6, n3, this.X[2], 15);
        int n9 = this.F1(n3, n8, n7, n6, this.X[3], 12);
        int n10 = this.F1(n6, n9, n8, n7, this.X[4], 5);
        int n11 = this.F1(n7, n10, n9, n8, this.X[5], 8);
        int n12 = this.F1(n8, n11, n10, n9, this.X[6], 7);
        int n13 = this.F1(n9, n12, n11, n10, this.X[7], 9);
        int n14 = this.F1(n10, n13, n12, n11, this.X[8], 11);
        int n15 = this.F1(n11, n14, n13, n12, this.X[9], 13);
        int n16 = this.F1(n12, n15, n14, n13, this.X[10], 14);
        int n17 = this.F1(n13, n16, n15, n14, this.X[11], 15);
        int n18 = this.F1(n14, n17, n16, n15, this.X[12], 6);
        int n19 = this.F1(n15, n18, n17, n16, this.X[13], 7);
        int n20 = this.F1(n16, n19, n18, n17, this.X[14], 9);
        int n21 = this.F1(n17, n20, n19, n18, this.X[15], 8);
        int n22 = this.F2(n18, n21, n20, n19, this.X[7], 7);
        int n23 = this.F2(n19, n22, n21, n20, this.X[4], 6);
        int n24 = this.F2(n20, n23, n22, n21, this.X[13], 8);
        int n25 = this.F2(n21, n24, n23, n22, this.X[1], 13);
        int n26 = this.F2(n22, n25, n24, n23, this.X[10], 11);
        int n27 = this.F2(n23, n26, n25, n24, this.X[6], 9);
        int n28 = this.F2(n24, n27, n26, n25, this.X[15], 7);
        int n29 = this.F2(n25, n28, n27, n26, this.X[3], 15);
        int n30 = this.F2(n26, n29, n28, n27, this.X[12], 7);
        int n31 = this.F2(n27, n30, n29, n28, this.X[0], 12);
        int n32 = this.F2(n28, n31, n30, n29, this.X[9], 15);
        int n33 = this.F2(n29, n32, n31, n30, this.X[5], 9);
        int n34 = this.F2(n30, n33, n32, n31, this.X[2], 11);
        int n35 = this.F2(n31, n34, n33, n32, this.X[14], 7);
        int n36 = this.F2(n32, n35, n34, n33, this.X[11], 13);
        int n37 = this.F2(n33, n36, n35, n34, this.X[8], 12);
        int n38 = this.F3(n34, n37, n36, n35, this.X[3], 11);
        int n39 = this.F3(n35, n38, n37, n36, this.X[10], 13);
        int n40 = this.F3(n36, n39, n38, n37, this.X[14], 6);
        int n41 = this.F3(n37, n40, n39, n38, this.X[4], 7);
        int n42 = this.F3(n38, n41, n40, n39, this.X[9], 14);
        int n43 = this.F3(n39, n42, n41, n40, this.X[15], 9);
        int n44 = this.F3(n40, n43, n42, n41, this.X[8], 13);
        int n45 = this.F3(n41, n44, n43, n42, this.X[1], 15);
        int n46 = this.F3(n42, n45, n44, n43, this.X[2], 14);
        int n47 = this.F3(n43, n46, n45, n44, this.X[7], 8);
        int n48 = this.F3(n44, n47, n46, n45, this.X[0], 13);
        int n49 = this.F3(n45, n48, n47, n46, this.X[6], 6);
        int n50 = this.F3(n46, n49, n48, n47, this.X[13], 5);
        int n51 = this.F3(n47, n50, n49, n48, this.X[11], 12);
        int n52 = this.F3(n48, n51, n50, n49, this.X[5], 7);
        int n53 = this.F3(n49, n52, n51, n50, this.X[12], 5);
        int n54 = this.F4(n50, n53, n52, n51, this.X[1], 11);
        int n55 = this.F4(n51, n54, n53, n52, this.X[9], 12);
        int n56 = this.F4(n52, n55, n54, n53, this.X[11], 14);
        int n57 = this.F4(n53, n56, n55, n54, this.X[10], 15);
        int n58 = this.F4(n54, n57, n56, n55, this.X[0], 14);
        int n59 = this.F4(n55, n58, n57, n56, this.X[8], 15);
        int n60 = this.F4(n56, n59, n58, n57, this.X[12], 9);
        int n61 = this.F4(n57, n60, n59, n58, this.X[4], 8);
        int n62 = this.F4(n58, n61, n60, n59, this.X[13], 9);
        int n63 = this.F4(n59, n62, n61, n60, this.X[3], 14);
        int n64 = this.F4(n60, n63, n62, n61, this.X[7], 5);
        int n65 = this.F4(n61, n64, n63, n62, this.X[15], 6);
        int n66 = this.F4(n62, n65, n64, n63, this.X[14], 8);
        int n67 = this.F4(n63, n66, n65, n64, this.X[5], 6);
        int n68 = this.F4(n64, n67, n66, n65, this.X[6], 5);
        int n69 = this.F4(n65, n68, n67, n66, this.X[2], 12);
        int n70 = this.FF4(n2, n3, n4, n5, this.X[5], 8);
        int n71 = this.FF4(n5, n70, n3, n4, this.X[14], 9);
        int n72 = this.FF4(n4, n71, n70, n3, this.X[7], 9);
        int n73 = this.FF4(n3, n72, n71, n70, this.X[0], 11);
        int n74 = this.FF4(n70, n73, n72, n71, this.X[9], 13);
        int n75 = this.FF4(n71, n74, n73, n72, this.X[2], 15);
        int n76 = this.FF4(n72, n75, n74, n73, this.X[11], 15);
        int n77 = this.FF4(n73, n76, n75, n74, this.X[4], 5);
        int n78 = this.FF4(n74, n77, n76, n75, this.X[13], 7);
        int n79 = this.FF4(n75, n78, n77, n76, this.X[6], 7);
        int n80 = this.FF4(n76, n79, n78, n77, this.X[15], 8);
        int n81 = this.FF4(n77, n80, n79, n78, this.X[8], 11);
        int n82 = this.FF4(n78, n81, n80, n79, this.X[1], 14);
        int n83 = this.FF4(n79, n82, n81, n80, this.X[10], 14);
        int n84 = this.FF4(n80, n83, n82, n81, this.X[3], 12);
        int n85 = this.FF4(n81, n84, n83, n82, this.X[12], 6);
        int n86 = this.FF3(n82, n85, n84, n83, this.X[6], 9);
        int n87 = this.FF3(n83, n86, n85, n84, this.X[11], 13);
        int n88 = this.FF3(n84, n87, n86, n85, this.X[3], 15);
        int n89 = this.FF3(n85, n88, n87, n86, this.X[7], 7);
        int n90 = this.FF3(n86, n89, n88, n87, this.X[0], 12);
        int n91 = this.FF3(n87, n90, n89, n88, this.X[13], 8);
        int n92 = this.FF3(n88, n91, n90, n89, this.X[5], 9);
        int n93 = this.FF3(n89, n92, n91, n90, this.X[10], 11);
        int n94 = this.FF3(n90, n93, n92, n91, this.X[14], 7);
        int n95 = this.FF3(n91, n94, n93, n92, this.X[15], 7);
        int n96 = this.FF3(n92, n95, n94, n93, this.X[8], 12);
        int n97 = this.FF3(n93, n96, n95, n94, this.X[12], 7);
        int n98 = this.FF3(n94, n97, n96, n95, this.X[4], 6);
        int n99 = this.FF3(n95, n98, n97, n96, this.X[9], 15);
        int n100 = this.FF3(n96, n99, n98, n97, this.X[1], 13);
        int n101 = this.FF3(n97, n100, n99, n98, this.X[2], 11);
        int n102 = this.FF2(n98, n101, n100, n99, this.X[15], 9);
        int n103 = this.FF2(n99, n102, n101, n100, this.X[5], 7);
        int n104 = this.FF2(n100, n103, n102, n101, this.X[1], 15);
        int n105 = this.FF2(n101, n104, n103, n102, this.X[3], 11);
        int n106 = this.FF2(n102, n105, n104, n103, this.X[7], 8);
        int n107 = this.FF2(n103, n106, n105, n104, this.X[14], 6);
        int n108 = this.FF2(n104, n107, n106, n105, this.X[6], 6);
        int n109 = this.FF2(n105, n108, n107, n106, this.X[9], 14);
        int n110 = this.FF2(n106, n109, n108, n107, this.X[11], 12);
        int n111 = this.FF2(n107, n110, n109, n108, this.X[8], 13);
        int n112 = this.FF2(n108, n111, n110, n109, this.X[12], 5);
        int n113 = this.FF2(n109, n112, n111, n110, this.X[2], 14);
        int n114 = this.FF2(n110, n113, n112, n111, this.X[10], 13);
        int n115 = this.FF2(n111, n114, n113, n112, this.X[0], 13);
        int n116 = this.FF2(n112, n115, n114, n113, this.X[4], 7);
        int n117 = this.FF2(n113, n116, n115, n114, this.X[13], 5);
        int n118 = this.FF1(n114, n117, n116, n115, this.X[8], 15);
        int n119 = this.FF1(n115, n118, n117, n116, this.X[6], 5);
        int n120 = this.FF1(n116, n119, n118, n117, this.X[4], 8);
        int n121 = this.FF1(n117, n120, n119, n118, this.X[1], 11);
        int n122 = this.FF1(n118, n121, n120, n119, this.X[3], 14);
        int n123 = this.FF1(n119, n122, n121, n120, this.X[11], 14);
        int n124 = this.FF1(n120, n123, n122, n121, this.X[15], 6);
        int n125 = this.FF1(n121, n124, n123, n122, this.X[0], 14);
        int n126 = this.FF1(n122, n125, n124, n123, this.X[5], 6);
        int n127 = this.FF1(n123, n126, n125, n124, this.X[12], 9);
        int n128 = this.FF1(n124, n127, n126, n125, this.X[2], 12);
        int n129 = this.FF1(n125, n128, n127, n126, this.X[13], 9);
        int n130 = this.FF1(n126, n129, n128, n127, this.X[9], 12);
        int n131 = this.FF1(n127, n130, n129, n128, this.X[7], 5);
        int n132 = this.FF1(n128, n131, n130, n129, this.X[10], 15);
        int n133 = this.FF1(n129, n132, n131, n130, this.X[14], 8);
        int n134 = n131 + (n68 + this.H1);
        this.H1 = n130 + (n67 + this.H2);
        this.H2 = n133 + (n66 + this.H3);
        this.H3 = n132 + (n69 + this.H0);
        this.H0 = n134;
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
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((RIPEMD128Digest)memoable);
    }
}

