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

public class RIPEMD256Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 32;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int[] X = new int[16];
    private int xOff;

    public RIPEMD256Digest() {
        this.reset();
    }

    public RIPEMD256Digest(RIPEMD256Digest rIPEMD256Digest) {
        super(rIPEMD256Digest);
        this.copyIn(rIPEMD256Digest);
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

    private void copyIn(RIPEMD256Digest rIPEMD256Digest) {
        super.copyIn(rIPEMD256Digest);
        this.H0 = rIPEMD256Digest.H0;
        this.H1 = rIPEMD256Digest.H1;
        this.H2 = rIPEMD256Digest.H2;
        this.H3 = rIPEMD256Digest.H3;
        this.H4 = rIPEMD256Digest.H4;
        this.H5 = rIPEMD256Digest.H5;
        this.H6 = rIPEMD256Digest.H6;
        this.H7 = rIPEMD256Digest.H7;
        System.arraycopy((Object)rIPEMD256Digest.X, (int)0, (Object)this.X, (int)0, (int)rIPEMD256Digest.X.length);
        this.xOff = rIPEMD256Digest.xOff;
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
        return new RIPEMD256Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        this.unpackWord(this.H0, arrby, n2);
        this.unpackWord(this.H1, arrby, n2 + 4);
        this.unpackWord(this.H2, arrby, n2 + 8);
        this.unpackWord(this.H3, arrby, n2 + 12);
        this.unpackWord(this.H4, arrby, n2 + 16);
        this.unpackWord(this.H5, arrby, n2 + 20);
        this.unpackWord(this.H6, arrby, n2 + 24);
        this.unpackWord(this.H7, arrby, n2 + 28);
        this.reset();
        return 32;
    }

    @Override
    public String getAlgorithmName() {
        return "RIPEMD256";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    protected void processBlock() {
        int n2 = this.H0;
        int n3 = this.H1;
        int n4 = this.H2;
        int n5 = this.H3;
        int n6 = this.H4;
        int n7 = this.H5;
        int n8 = this.H6;
        int n9 = this.H7;
        int n10 = this.F1(n2, n3, n4, n5, this.X[0], 11);
        int n11 = this.F1(n5, n10, n3, n4, this.X[1], 14);
        int n12 = this.F1(n4, n11, n10, n3, this.X[2], 15);
        int n13 = this.F1(n3, n12, n11, n10, this.X[3], 12);
        int n14 = this.F1(n10, n13, n12, n11, this.X[4], 5);
        int n15 = this.F1(n11, n14, n13, n12, this.X[5], 8);
        int n16 = this.F1(n12, n15, n14, n13, this.X[6], 7);
        int n17 = this.F1(n13, n16, n15, n14, this.X[7], 9);
        int n18 = this.F1(n14, n17, n16, n15, this.X[8], 11);
        int n19 = this.F1(n15, n18, n17, n16, this.X[9], 13);
        int n20 = this.F1(n16, n19, n18, n17, this.X[10], 14);
        int n21 = this.F1(n17, n20, n19, n18, this.X[11], 15);
        int n22 = this.F1(n18, n21, n20, n19, this.X[12], 6);
        int n23 = this.F1(n19, n22, n21, n20, this.X[13], 7);
        int n24 = this.F1(n20, n23, n22, n21, this.X[14], 9);
        int n25 = this.F1(n21, n24, n23, n22, this.X[15], 8);
        int n26 = this.FF4(n6, n7, n8, n9, this.X[5], 8);
        int n27 = this.FF4(n9, n26, n7, n8, this.X[14], 9);
        int n28 = this.FF4(n8, n27, n26, n7, this.X[7], 9);
        int n29 = this.FF4(n7, n28, n27, n26, this.X[0], 11);
        int n30 = this.FF4(n26, n29, n28, n27, this.X[9], 13);
        int n31 = this.FF4(n27, n30, n29, n28, this.X[2], 15);
        int n32 = this.FF4(n28, n31, n30, n29, this.X[11], 15);
        int n33 = this.FF4(n29, n32, n31, n30, this.X[4], 5);
        int n34 = this.FF4(n30, n33, n32, n31, this.X[13], 7);
        int n35 = this.FF4(n31, n34, n33, n32, this.X[6], 7);
        int n36 = this.FF4(n32, n35, n34, n33, this.X[15], 8);
        int n37 = this.FF4(n33, n36, n35, n34, this.X[8], 11);
        int n38 = this.FF4(n34, n37, n36, n35, this.X[1], 14);
        int n39 = this.FF4(n35, n38, n37, n36, this.X[10], 14);
        int n40 = this.FF4(n36, n39, n38, n37, this.X[3], 12);
        int n41 = this.FF4(n37, n40, n39, n38, this.X[12], 6);
        int n42 = this.F2(n38, n25, n24, n23, this.X[7], 7);
        int n43 = this.F2(n23, n42, n25, n24, this.X[4], 6);
        int n44 = this.F2(n24, n43, n42, n25, this.X[13], 8);
        int n45 = this.F2(n25, n44, n43, n42, this.X[1], 13);
        int n46 = this.F2(n42, n45, n44, n43, this.X[10], 11);
        int n47 = this.F2(n43, n46, n45, n44, this.X[6], 9);
        int n48 = this.F2(n44, n47, n46, n45, this.X[15], 7);
        int n49 = this.F2(n45, n48, n47, n46, this.X[3], 15);
        int n50 = this.F2(n46, n49, n48, n47, this.X[12], 7);
        int n51 = this.F2(n47, n50, n49, n48, this.X[0], 12);
        int n52 = this.F2(n48, n51, n50, n49, this.X[9], 15);
        int n53 = this.F2(n49, n52, n51, n50, this.X[5], 9);
        int n54 = this.F2(n50, n53, n52, n51, this.X[2], 11);
        int n55 = this.F2(n51, n54, n53, n52, this.X[14], 7);
        int n56 = this.F2(n52, n55, n54, n53, this.X[11], 13);
        int n57 = this.F2(n53, n56, n55, n54, this.X[8], 12);
        int n58 = this.FF3(n22, n41, n40, n39, this.X[6], 9);
        int n59 = this.FF3(n39, n58, n41, n40, this.X[11], 13);
        int n60 = this.FF3(n40, n59, n58, n41, this.X[3], 15);
        int n61 = this.FF3(n41, n60, n59, n58, this.X[7], 7);
        int n62 = this.FF3(n58, n61, n60, n59, this.X[0], 12);
        int n63 = this.FF3(n59, n62, n61, n60, this.X[13], 8);
        int n64 = this.FF3(n60, n63, n62, n61, this.X[5], 9);
        int n65 = this.FF3(n61, n64, n63, n62, this.X[10], 11);
        int n66 = this.FF3(n62, n65, n64, n63, this.X[14], 7);
        int n67 = this.FF3(n63, n66, n65, n64, this.X[15], 7);
        int n68 = this.FF3(n64, n67, n66, n65, this.X[8], 12);
        int n69 = this.FF3(n65, n68, n67, n66, this.X[12], 7);
        int n70 = this.FF3(n66, n69, n68, n67, this.X[4], 6);
        int n71 = this.FF3(n67, n70, n69, n68, this.X[9], 15);
        int n72 = this.FF3(n68, n71, n70, n69, this.X[1], 13);
        int n73 = this.FF3(n69, n72, n71, n70, this.X[2], 11);
        int n74 = this.F3(n54, n73, n56, n55, this.X[3], 11);
        int n75 = this.F3(n55, n74, n73, n56, this.X[10], 13);
        int n76 = this.F3(n56, n75, n74, n73, this.X[14], 6);
        int n77 = this.F3(n73, n76, n75, n74, this.X[4], 7);
        int n78 = this.F3(n74, n77, n76, n75, this.X[9], 14);
        int n79 = this.F3(n75, n78, n77, n76, this.X[15], 9);
        int n80 = this.F3(n76, n79, n78, n77, this.X[8], 13);
        int n81 = this.F3(n77, n80, n79, n78, this.X[1], 15);
        int n82 = this.F3(n78, n81, n80, n79, this.X[2], 14);
        int n83 = this.F3(n79, n82, n81, n80, this.X[7], 8);
        int n84 = this.F3(n80, n83, n82, n81, this.X[0], 13);
        int n85 = this.F3(n81, n84, n83, n82, this.X[6], 6);
        int n86 = this.F3(n82, n85, n84, n83, this.X[13], 5);
        int n87 = this.F3(n83, n86, n85, n84, this.X[11], 12);
        int n88 = this.F3(n84, n87, n86, n85, this.X[5], 7);
        int n89 = this.F3(n85, n88, n87, n86, this.X[12], 5);
        int n90 = this.FF2(n70, n57, n72, n71, this.X[15], 9);
        int n91 = this.FF2(n71, n90, n57, n72, this.X[5], 7);
        int n92 = this.FF2(n72, n91, n90, n57, this.X[1], 15);
        int n93 = this.FF2(n57, n92, n91, n90, this.X[3], 11);
        int n94 = this.FF2(n90, n93, n92, n91, this.X[7], 8);
        int n95 = this.FF2(n91, n94, n93, n92, this.X[14], 6);
        int n96 = this.FF2(n92, n95, n94, n93, this.X[6], 6);
        int n97 = this.FF2(n93, n96, n95, n94, this.X[9], 14);
        int n98 = this.FF2(n94, n97, n96, n95, this.X[11], 12);
        int n99 = this.FF2(n95, n98, n97, n96, this.X[8], 13);
        int n100 = this.FF2(n96, n99, n98, n97, this.X[12], 5);
        int n101 = this.FF2(n97, n100, n99, n98, this.X[2], 14);
        int n102 = this.FF2(n98, n101, n100, n99, this.X[10], 13);
        int n103 = this.FF2(n99, n102, n101, n100, this.X[0], 13);
        int n104 = this.FF2(n100, n103, n102, n101, this.X[4], 7);
        int n105 = this.FF2(n101, n104, n103, n102, this.X[13], 5);
        int n106 = this.F4(n86, n89, n104, n87, this.X[1], 11);
        int n107 = this.F4(n87, n106, n89, n104, this.X[9], 12);
        int n108 = this.F4(n104, n107, n106, n89, this.X[11], 14);
        int n109 = this.F4(n89, n108, n107, n106, this.X[10], 15);
        int n110 = this.F4(n106, n109, n108, n107, this.X[0], 14);
        int n111 = this.F4(n107, n110, n109, n108, this.X[8], 15);
        int n112 = this.F4(n108, n111, n110, n109, this.X[12], 9);
        int n113 = this.F4(n109, n112, n111, n110, this.X[4], 8);
        int n114 = this.F4(n110, n113, n112, n111, this.X[13], 9);
        int n115 = this.F4(n111, n114, n113, n112, this.X[3], 14);
        int n116 = this.F4(n112, n115, n114, n113, this.X[7], 5);
        int n117 = this.F4(n113, n116, n115, n114, this.X[15], 6);
        int n118 = this.F4(n114, n117, n116, n115, this.X[14], 8);
        int n119 = this.F4(n115, n118, n117, n116, this.X[5], 6);
        int n120 = this.F4(n116, n119, n118, n117, this.X[6], 5);
        int n121 = this.F4(n117, n120, n119, n118, this.X[2], 12);
        int n122 = this.FF1(n102, n105, n88, n103, this.X[8], 15);
        int n123 = this.FF1(n103, n122, n105, n88, this.X[6], 5);
        int n124 = this.FF1(n88, n123, n122, n105, this.X[4], 8);
        int n125 = this.FF1(n105, n124, n123, n122, this.X[1], 11);
        int n126 = this.FF1(n122, n125, n124, n123, this.X[3], 14);
        int n127 = this.FF1(n123, n126, n125, n124, this.X[11], 14);
        int n128 = this.FF1(n124, n127, n126, n125, this.X[15], 6);
        int n129 = this.FF1(n125, n128, n127, n126, this.X[0], 14);
        int n130 = this.FF1(n126, n129, n128, n127, this.X[5], 6);
        int n131 = this.FF1(n127, n130, n129, n128, this.X[12], 9);
        int n132 = this.FF1(n128, n131, n130, n129, this.X[2], 12);
        int n133 = this.FF1(n129, n132, n131, n130, this.X[13], 9);
        int n134 = this.FF1(n130, n133, n132, n131, this.X[9], 12);
        int n135 = this.FF1(n131, n134, n133, n132, this.X[7], 5);
        int n136 = this.FF1(n132, n135, n134, n133, this.X[10], 15);
        int n137 = this.FF1(n133, n136, n135, n134, this.X[14], 8);
        this.H0 = n118 + this.H0;
        this.H1 = n121 + this.H1;
        this.H2 = n120 + this.H2;
        this.H3 = n135 + this.H3;
        this.H4 = n134 + this.H4;
        this.H5 = n137 + this.H5;
        this.H6 = n136 + this.H6;
        this.H7 = n119 + this.H7;
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
        this.H4 = 1985229328;
        this.H5 = -19088744;
        this.H6 = -1985229329;
        this.H7 = 19088743;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((RIPEMD256Digest)memoable);
    }
}

