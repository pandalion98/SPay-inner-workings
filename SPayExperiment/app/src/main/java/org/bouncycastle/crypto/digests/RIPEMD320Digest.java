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

public class RIPEMD320Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 40;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int H8;
    private int H9;
    private int[] X = new int[16];
    private int xOff;

    public RIPEMD320Digest() {
        this.reset();
    }

    public RIPEMD320Digest(RIPEMD320Digest rIPEMD320Digest) {
        super(rIPEMD320Digest);
        this.doCopy(rIPEMD320Digest);
    }

    private int RL(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private void doCopy(RIPEMD320Digest rIPEMD320Digest) {
        super.copyIn(rIPEMD320Digest);
        this.H0 = rIPEMD320Digest.H0;
        this.H1 = rIPEMD320Digest.H1;
        this.H2 = rIPEMD320Digest.H2;
        this.H3 = rIPEMD320Digest.H3;
        this.H4 = rIPEMD320Digest.H4;
        this.H5 = rIPEMD320Digest.H5;
        this.H6 = rIPEMD320Digest.H6;
        this.H7 = rIPEMD320Digest.H7;
        this.H8 = rIPEMD320Digest.H8;
        this.H9 = rIPEMD320Digest.H9;
        System.arraycopy((Object)rIPEMD320Digest.X, (int)0, (Object)this.X, (int)0, (int)rIPEMD320Digest.X.length);
        this.xOff = rIPEMD320Digest.xOff;
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

    private int f5(int n2, int n3, int n4) {
        return n2 ^ (n3 | ~n4);
    }

    private void unpackWord(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
        arrby[n3 + 1] = (byte)(n2 >>> 8);
        arrby[n3 + 2] = (byte)(n2 >>> 16);
        arrby[n3 + 3] = (byte)(n2 >>> 24);
    }

    @Override
    public Memoable copy() {
        return new RIPEMD320Digest(this);
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
        this.unpackWord(this.H8, arrby, n2 + 32);
        this.unpackWord(this.H9, arrby, n2 + 36);
        this.reset();
        return 40;
    }

    @Override
    public String getAlgorithmName() {
        return "RIPEMD320";
    }

    @Override
    public int getDigestSize() {
        return 40;
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
        int n10 = this.H8;
        int n11 = this.H9;
        int n12 = n6 + this.RL(n2 + this.f1(n3, n4, n5) + this.X[0], 11);
        int n13 = this.RL(n4, 10);
        int n14 = n5 + this.RL(n6 + this.f1(n12, n3, n13) + this.X[1], 14);
        int n15 = this.RL(n3, 10);
        int n16 = n13 + this.RL(n5 + this.f1(n14, n12, n15) + this.X[2], 15);
        int n17 = this.RL(n12, 10);
        int n18 = n15 + this.RL(n13 + this.f1(n16, n14, n17) + this.X[3], 12);
        int n19 = this.RL(n14, 10);
        int n20 = n17 + this.RL(n15 + this.f1(n18, n16, n19) + this.X[4], 5);
        int n21 = this.RL(n16, 10);
        int n22 = n19 + this.RL(n17 + this.f1(n20, n18, n21) + this.X[5], 8);
        int n23 = this.RL(n18, 10);
        int n24 = n21 + this.RL(n19 + this.f1(n22, n20, n23) + this.X[6], 7);
        int n25 = this.RL(n20, 10);
        int n26 = n23 + this.RL(n21 + this.f1(n24, n22, n25) + this.X[7], 9);
        int n27 = this.RL(n22, 10);
        int n28 = n25 + this.RL(n23 + this.f1(n26, n24, n27) + this.X[8], 11);
        int n29 = this.RL(n24, 10);
        int n30 = n27 + this.RL(n25 + this.f1(n28, n26, n29) + this.X[9], 13);
        int n31 = this.RL(n26, 10);
        int n32 = n29 + this.RL(n27 + this.f1(n30, n28, n31) + this.X[10], 14);
        int n33 = this.RL(n28, 10);
        int n34 = n31 + this.RL(n29 + this.f1(n32, n30, n33) + this.X[11], 15);
        int n35 = this.RL(n30, 10);
        int n36 = n33 + this.RL(n31 + this.f1(n34, n32, n35) + this.X[12], 6);
        int n37 = this.RL(n32, 10);
        int n38 = n35 + this.RL(n33 + this.f1(n36, n34, n37) + this.X[13], 7);
        int n39 = this.RL(n34, 10);
        int n40 = n37 + this.RL(n35 + this.f1(n38, n36, n39) + this.X[14], 9);
        int n41 = this.RL(n36, 10);
        int n42 = n39 + this.RL(n37 + this.f1(n40, n38, n41) + this.X[15], 8);
        int n43 = this.RL(n38, 10);
        int n44 = n11 + this.RL(1352829926 + (n7 + this.f5(n8, n9, n10) + this.X[5]), 8);
        int n45 = this.RL(n9, 10);
        int n46 = n10 + this.RL(1352829926 + (n11 + this.f5(n44, n8, n45) + this.X[14]), 9);
        int n47 = this.RL(n8, 10);
        int n48 = n45 + this.RL(1352829926 + (n10 + this.f5(n46, n44, n47) + this.X[7]), 9);
        int n49 = this.RL(n44, 10);
        int n50 = n47 + this.RL(1352829926 + (n45 + this.f5(n48, n46, n49) + this.X[0]), 11);
        int n51 = this.RL(n46, 10);
        int n52 = n49 + this.RL(1352829926 + (n47 + this.f5(n50, n48, n51) + this.X[9]), 13);
        int n53 = this.RL(n48, 10);
        int n54 = n51 + this.RL(1352829926 + (n49 + this.f5(n52, n50, n53) + this.X[2]), 15);
        int n55 = this.RL(n50, 10);
        int n56 = n53 + this.RL(1352829926 + (n51 + this.f5(n54, n52, n55) + this.X[11]), 15);
        int n57 = this.RL(n52, 10);
        int n58 = n55 + this.RL(1352829926 + (n53 + this.f5(n56, n54, n57) + this.X[4]), 5);
        int n59 = this.RL(n54, 10);
        int n60 = n57 + this.RL(1352829926 + (n55 + this.f5(n58, n56, n59) + this.X[13]), 7);
        int n61 = this.RL(n56, 10);
        int n62 = n59 + this.RL(1352829926 + (n57 + this.f5(n60, n58, n61) + this.X[6]), 7);
        int n63 = this.RL(n58, 10);
        int n64 = n61 + this.RL(1352829926 + (n59 + this.f5(n62, n60, n63) + this.X[15]), 8);
        int n65 = this.RL(n60, 10);
        int n66 = n63 + this.RL(1352829926 + (n61 + this.f5(n64, n62, n65) + this.X[8]), 11);
        int n67 = this.RL(n62, 10);
        int n68 = n65 + this.RL(1352829926 + (n63 + this.f5(n66, n64, n67) + this.X[1]), 14);
        int n69 = this.RL(n64, 10);
        int n70 = n67 + this.RL(1352829926 + (n65 + this.f5(n68, n66, n69) + this.X[10]), 14);
        int n71 = this.RL(n66, 10);
        int n72 = n69 + this.RL(1352829926 + (n67 + this.f5(n70, n68, n71) + this.X[3]), 12);
        int n73 = this.RL(n68, 10);
        int n74 = n71 + this.RL(1352829926 + (n69 + this.f5(n72, n70, n73) + this.X[12]), 6);
        int n75 = this.RL(n70, 10);
        int n76 = n41 + this.RL(1518500249 + (n39 + this.f2(n74, n40, n43) + this.X[7]), 7);
        int n77 = this.RL(n40, 10);
        int n78 = n43 + this.RL(1518500249 + (n41 + this.f2(n76, n74, n77) + this.X[4]), 6);
        int n79 = this.RL(n74, 10);
        int n80 = n77 + this.RL(1518500249 + (n43 + this.f2(n78, n76, n79) + this.X[13]), 8);
        int n81 = this.RL(n76, 10);
        int n82 = n79 + this.RL(1518500249 + (n77 + this.f2(n80, n78, n81) + this.X[1]), 13);
        int n83 = this.RL(n78, 10);
        int n84 = n81 + this.RL(1518500249 + (n79 + this.f2(n82, n80, n83) + this.X[10]), 11);
        int n85 = this.RL(n80, 10);
        int n86 = n83 + this.RL(1518500249 + (n81 + this.f2(n84, n82, n85) + this.X[6]), 9);
        int n87 = this.RL(n82, 10);
        int n88 = n85 + this.RL(1518500249 + (n83 + this.f2(n86, n84, n87) + this.X[15]), 7);
        int n89 = this.RL(n84, 10);
        int n90 = n87 + this.RL(1518500249 + (n85 + this.f2(n88, n86, n89) + this.X[3]), 15);
        int n91 = this.RL(n86, 10);
        int n92 = n89 + this.RL(1518500249 + (n87 + this.f2(n90, n88, n91) + this.X[12]), 7);
        int n93 = this.RL(n88, 10);
        int n94 = n91 + this.RL(1518500249 + (n89 + this.f2(n92, n90, n93) + this.X[0]), 12);
        int n95 = this.RL(n90, 10);
        int n96 = n93 + this.RL(1518500249 + (n91 + this.f2(n94, n92, n95) + this.X[9]), 15);
        int n97 = this.RL(n92, 10);
        int n98 = n95 + this.RL(1518500249 + (n93 + this.f2(n96, n94, n97) + this.X[5]), 9);
        int n99 = this.RL(n94, 10);
        int n100 = n97 + this.RL(1518500249 + (n95 + this.f2(n98, n96, n99) + this.X[2]), 11);
        int n101 = this.RL(n96, 10);
        int n102 = n99 + this.RL(1518500249 + (n97 + this.f2(n100, n98, n101) + this.X[14]), 7);
        int n103 = this.RL(n98, 10);
        int n104 = n101 + this.RL(1518500249 + (n99 + this.f2(n102, n100, n103) + this.X[11]), 13);
        int n105 = this.RL(n100, 10);
        int n106 = n103 + this.RL(1518500249 + (n101 + this.f2(n104, n102, n105) + this.X[8]), 12);
        int n107 = this.RL(n102, 10);
        int n108 = n73 + this.RL(1548603684 + (n71 + this.f4(n42, n72, n75) + this.X[6]), 9);
        int n109 = this.RL(n72, 10);
        int n110 = n75 + this.RL(1548603684 + (n73 + this.f4(n108, n42, n109) + this.X[11]), 13);
        int n111 = this.RL(n42, 10);
        int n112 = n109 + this.RL(1548603684 + (n75 + this.f4(n110, n108, n111) + this.X[3]), 15);
        int n113 = this.RL(n108, 10);
        int n114 = n111 + this.RL(1548603684 + (n109 + this.f4(n112, n110, n113) + this.X[7]), 7);
        int n115 = this.RL(n110, 10);
        int n116 = n113 + this.RL(1548603684 + (n111 + this.f4(n114, n112, n115) + this.X[0]), 12);
        int n117 = this.RL(n112, 10);
        int n118 = n115 + this.RL(1548603684 + (n113 + this.f4(n116, n114, n117) + this.X[13]), 8);
        int n119 = this.RL(n114, 10);
        int n120 = n117 + this.RL(1548603684 + (n115 + this.f4(n118, n116, n119) + this.X[5]), 9);
        int n121 = this.RL(n116, 10);
        int n122 = n119 + this.RL(1548603684 + (n117 + this.f4(n120, n118, n121) + this.X[10]), 11);
        int n123 = this.RL(n118, 10);
        int n124 = n121 + this.RL(1548603684 + (n119 + this.f4(n122, n120, n123) + this.X[14]), 7);
        int n125 = this.RL(n120, 10);
        int n126 = n123 + this.RL(1548603684 + (n121 + this.f4(n124, n122, n125) + this.X[15]), 7);
        int n127 = this.RL(n122, 10);
        int n128 = n125 + this.RL(1548603684 + (n123 + this.f4(n126, n124, n127) + this.X[8]), 12);
        int n129 = this.RL(n124, 10);
        int n130 = n127 + this.RL(1548603684 + (n125 + this.f4(n128, n126, n129) + this.X[12]), 7);
        int n131 = this.RL(n126, 10);
        int n132 = n129 + this.RL(1548603684 + (n127 + this.f4(n130, n128, n131) + this.X[4]), 6);
        int n133 = this.RL(n128, 10);
        int n134 = n131 + this.RL(1548603684 + (n129 + this.f4(n132, n130, n133) + this.X[9]), 15);
        int n135 = this.RL(n130, 10);
        int n136 = n133 + this.RL(1548603684 + (n131 + this.f4(n134, n132, n135) + this.X[1]), 13);
        int n137 = this.RL(n132, 10);
        int n138 = n135 + this.RL(1548603684 + (n133 + this.f4(n136, n134, n137) + this.X[2]), 11);
        int n139 = this.RL(n134, 10);
        int n140 = n105 + this.RL(1859775393 + (n103 + this.f3(n106, n104, n139) + this.X[3]), 11);
        int n141 = this.RL(n104, 10);
        int n142 = n139 + this.RL(1859775393 + (n105 + this.f3(n140, n106, n141) + this.X[10]), 13);
        int n143 = this.RL(n106, 10);
        int n144 = n141 + this.RL(1859775393 + (n139 + this.f3(n142, n140, n143) + this.X[14]), 6);
        int n145 = this.RL(n140, 10);
        int n146 = n143 + this.RL(1859775393 + (n141 + this.f3(n144, n142, n145) + this.X[4]), 7);
        int n147 = this.RL(n142, 10);
        int n148 = n145 + this.RL(1859775393 + (n143 + this.f3(n146, n144, n147) + this.X[9]), 14);
        int n149 = this.RL(n144, 10);
        int n150 = n147 + this.RL(1859775393 + (n145 + this.f3(n148, n146, n149) + this.X[15]), 9);
        int n151 = this.RL(n146, 10);
        int n152 = n149 + this.RL(1859775393 + (n147 + this.f3(n150, n148, n151) + this.X[8]), 13);
        int n153 = this.RL(n148, 10);
        int n154 = n151 + this.RL(1859775393 + (n149 + this.f3(n152, n150, n153) + this.X[1]), 15);
        int n155 = this.RL(n150, 10);
        int n156 = n153 + this.RL(1859775393 + (n151 + this.f3(n154, n152, n155) + this.X[2]), 14);
        int n157 = this.RL(n152, 10);
        int n158 = n155 + this.RL(1859775393 + (n153 + this.f3(n156, n154, n157) + this.X[7]), 8);
        int n159 = this.RL(n154, 10);
        int n160 = n157 + this.RL(1859775393 + (n155 + this.f3(n158, n156, n159) + this.X[0]), 13);
        int n161 = this.RL(n156, 10);
        int n162 = n159 + this.RL(1859775393 + (n157 + this.f3(n160, n158, n161) + this.X[6]), 6);
        int n163 = this.RL(n158, 10);
        int n164 = n161 + this.RL(1859775393 + (n159 + this.f3(n162, n160, n163) + this.X[13]), 5);
        int n165 = this.RL(n160, 10);
        int n166 = n163 + this.RL(1859775393 + (n161 + this.f3(n164, n162, n165) + this.X[11]), 12);
        int n167 = this.RL(n162, 10);
        int n168 = n165 + this.RL(1859775393 + (n163 + this.f3(n166, n164, n167) + this.X[5]), 7);
        int n169 = this.RL(n164, 10);
        int n170 = n167 + this.RL(1859775393 + (n165 + this.f3(n168, n166, n169) + this.X[12]), 5);
        int n171 = this.RL(n166, 10);
        int n172 = n137 + this.RL(1836072691 + (n135 + this.f3(n138, n136, n107) + this.X[15]), 9);
        int n173 = this.RL(n136, 10);
        int n174 = n107 + this.RL(1836072691 + (n137 + this.f3(n172, n138, n173) + this.X[5]), 7);
        int n175 = this.RL(n138, 10);
        int n176 = n173 + this.RL(1836072691 + (n107 + this.f3(n174, n172, n175) + this.X[1]), 15);
        int n177 = this.RL(n172, 10);
        int n178 = n175 + this.RL(1836072691 + (n173 + this.f3(n176, n174, n177) + this.X[3]), 11);
        int n179 = this.RL(n174, 10);
        int n180 = n177 + this.RL(1836072691 + (n175 + this.f3(n178, n176, n179) + this.X[7]), 8);
        int n181 = this.RL(n176, 10);
        int n182 = n179 + this.RL(1836072691 + (n177 + this.f3(n180, n178, n181) + this.X[14]), 6);
        int n183 = this.RL(n178, 10);
        int n184 = n181 + this.RL(1836072691 + (n179 + this.f3(n182, n180, n183) + this.X[6]), 6);
        int n185 = this.RL(n180, 10);
        int n186 = n183 + this.RL(1836072691 + (n181 + this.f3(n184, n182, n185) + this.X[9]), 14);
        int n187 = this.RL(n182, 10);
        int n188 = n185 + this.RL(1836072691 + (n183 + this.f3(n186, n184, n187) + this.X[11]), 12);
        int n189 = this.RL(n184, 10);
        int n190 = n187 + this.RL(1836072691 + (n185 + this.f3(n188, n186, n189) + this.X[8]), 13);
        int n191 = this.RL(n186, 10);
        int n192 = n189 + this.RL(1836072691 + (n187 + this.f3(n190, n188, n191) + this.X[12]), 5);
        int n193 = this.RL(n188, 10);
        int n194 = n191 + this.RL(1836072691 + (n189 + this.f3(n192, n190, n193) + this.X[2]), 14);
        int n195 = this.RL(n190, 10);
        int n196 = n193 + this.RL(1836072691 + (n191 + this.f3(n194, n192, n195) + this.X[10]), 13);
        int n197 = this.RL(n192, 10);
        int n198 = n195 + this.RL(1836072691 + (n193 + this.f3(n196, n194, n197) + this.X[0]), 13);
        int n199 = this.RL(n194, 10);
        int n200 = n197 + this.RL(1836072691 + (n195 + this.f3(n198, n196, n199) + this.X[4]), 7);
        int n201 = this.RL(n196, 10);
        int n202 = n199 + this.RL(1836072691 + (n197 + this.f3(n200, n198, n201) + this.X[13]), 5);
        int n203 = this.RL(n198, 10);
        int n204 = n169 + this.RL(-1894007588 + (n199 + this.f4(n170, n168, n171) + this.X[1]), 11);
        int n205 = this.RL(n168, 10);
        int n206 = n171 + this.RL(-1894007588 + (n169 + this.f4(n204, n170, n205) + this.X[9]), 12);
        int n207 = this.RL(n170, 10);
        int n208 = n205 + this.RL(-1894007588 + (n171 + this.f4(n206, n204, n207) + this.X[11]), 14);
        int n209 = this.RL(n204, 10);
        int n210 = n207 + this.RL(-1894007588 + (n205 + this.f4(n208, n206, n209) + this.X[10]), 15);
        int n211 = this.RL(n206, 10);
        int n212 = n209 + this.RL(-1894007588 + (n207 + this.f4(n210, n208, n211) + this.X[0]), 14);
        int n213 = this.RL(n208, 10);
        int n214 = n211 + this.RL(-1894007588 + (n209 + this.f4(n212, n210, n213) + this.X[8]), 15);
        int n215 = this.RL(n210, 10);
        int n216 = n213 + this.RL(-1894007588 + (n211 + this.f4(n214, n212, n215) + this.X[12]), 9);
        int n217 = this.RL(n212, 10);
        int n218 = n215 + this.RL(-1894007588 + (n213 + this.f4(n216, n214, n217) + this.X[4]), 8);
        int n219 = this.RL(n214, 10);
        int n220 = n217 + this.RL(-1894007588 + (n215 + this.f4(n218, n216, n219) + this.X[13]), 9);
        int n221 = this.RL(n216, 10);
        int n222 = n219 + this.RL(-1894007588 + (n217 + this.f4(n220, n218, n221) + this.X[3]), 14);
        int n223 = this.RL(n218, 10);
        int n224 = n221 + this.RL(-1894007588 + (n219 + this.f4(n222, n220, n223) + this.X[7]), 5);
        int n225 = this.RL(n220, 10);
        int n226 = n223 + this.RL(-1894007588 + (n221 + this.f4(n224, n222, n225) + this.X[15]), 6);
        int n227 = this.RL(n222, 10);
        int n228 = n225 + this.RL(-1894007588 + (n223 + this.f4(n226, n224, n227) + this.X[14]), 8);
        int n229 = this.RL(n224, 10);
        int n230 = n227 + this.RL(-1894007588 + (n225 + this.f4(n228, n226, n229) + this.X[5]), 6);
        int n231 = this.RL(n226, 10);
        int n232 = n229 + this.RL(-1894007588 + (n227 + this.f4(n230, n228, n231) + this.X[6]), 5);
        int n233 = this.RL(n228, 10);
        int n234 = n231 + this.RL(-1894007588 + (n229 + this.f4(n232, n230, n233) + this.X[2]), 12);
        int n235 = this.RL(n230, 10);
        int n236 = n201 + this.RL(2053994217 + (n167 + this.f2(n202, n200, n203) + this.X[8]), 15);
        int n237 = this.RL(n200, 10);
        int n238 = n203 + this.RL(2053994217 + (n201 + this.f2(n236, n202, n237) + this.X[6]), 5);
        int n239 = this.RL(n202, 10);
        int n240 = n237 + this.RL(2053994217 + (n203 + this.f2(n238, n236, n239) + this.X[4]), 8);
        int n241 = this.RL(n236, 10);
        int n242 = n239 + this.RL(2053994217 + (n237 + this.f2(n240, n238, n241) + this.X[1]), 11);
        int n243 = this.RL(n238, 10);
        int n244 = n241 + this.RL(2053994217 + (n239 + this.f2(n242, n240, n243) + this.X[3]), 14);
        int n245 = this.RL(n240, 10);
        int n246 = n243 + this.RL(2053994217 + (n241 + this.f2(n244, n242, n245) + this.X[11]), 14);
        int n247 = this.RL(n242, 10);
        int n248 = n245 + this.RL(2053994217 + (n243 + this.f2(n246, n244, n247) + this.X[15]), 6);
        int n249 = this.RL(n244, 10);
        int n250 = n247 + this.RL(2053994217 + (n245 + this.f2(n248, n246, n249) + this.X[0]), 14);
        int n251 = this.RL(n246, 10);
        int n252 = n249 + this.RL(2053994217 + (n247 + this.f2(n250, n248, n251) + this.X[5]), 6);
        int n253 = this.RL(n248, 10);
        int n254 = n251 + this.RL(2053994217 + (n249 + this.f2(n252, n250, n253) + this.X[12]), 9);
        int n255 = this.RL(n250, 10);
        int n256 = n253 + this.RL(2053994217 + (n251 + this.f2(n254, n252, n255) + this.X[2]), 12);
        int n257 = this.RL(n252, 10);
        int n258 = n255 + this.RL(2053994217 + (n253 + this.f2(n256, n254, n257) + this.X[13]), 9);
        int n259 = this.RL(n254, 10);
        int n260 = n257 + this.RL(2053994217 + (n255 + this.f2(n258, n256, n259) + this.X[9]), 12);
        int n261 = this.RL(n256, 10);
        int n262 = n259 + this.RL(2053994217 + (n257 + this.f2(n260, n258, n261) + this.X[7]), 5);
        int n263 = this.RL(n258, 10);
        int n264 = n261 + this.RL(2053994217 + (n259 + this.f2(n262, n260, n263) + this.X[10]), 15);
        int n265 = this.RL(n260, 10);
        int n266 = n263 + this.RL(2053994217 + (n261 + this.f2(n264, n262, n265) + this.X[14]), 8);
        int n267 = this.RL(n262, 10);
        int n268 = n233 + this.RL(-1454113458 + (n231 + this.f5(n234, n264, n235) + this.X[4]), 9);
        int n269 = this.RL(n264, 10);
        int n270 = n235 + this.RL(-1454113458 + (n233 + this.f5(n268, n234, n269) + this.X[0]), 15);
        int n271 = this.RL(n234, 10);
        int n272 = n269 + this.RL(-1454113458 + (n235 + this.f5(n270, n268, n271) + this.X[5]), 5);
        int n273 = this.RL(n268, 10);
        int n274 = n271 + this.RL(-1454113458 + (n269 + this.f5(n272, n270, n273) + this.X[9]), 11);
        int n275 = this.RL(n270, 10);
        int n276 = n273 + this.RL(-1454113458 + (n271 + this.f5(n274, n272, n275) + this.X[7]), 6);
        int n277 = this.RL(n272, 10);
        int n278 = n275 + this.RL(-1454113458 + (n273 + this.f5(n276, n274, n277) + this.X[12]), 8);
        int n279 = this.RL(n274, 10);
        int n280 = n277 + this.RL(-1454113458 + (n275 + this.f5(n278, n276, n279) + this.X[2]), 13);
        int n281 = this.RL(n276, 10);
        int n282 = n279 + this.RL(-1454113458 + (n277 + this.f5(n280, n278, n281) + this.X[10]), 12);
        int n283 = this.RL(n278, 10);
        int n284 = n281 + this.RL(-1454113458 + (n279 + this.f5(n282, n280, n283) + this.X[14]), 5);
        int n285 = this.RL(n280, 10);
        int n286 = n283 + this.RL(-1454113458 + (n281 + this.f5(n284, n282, n285) + this.X[1]), 12);
        int n287 = this.RL(n282, 10);
        int n288 = n285 + this.RL(-1454113458 + (n283 + this.f5(n286, n284, n287) + this.X[3]), 13);
        int n289 = this.RL(n284, 10);
        int n290 = n287 + this.RL(-1454113458 + (n285 + this.f5(n288, n286, n289) + this.X[8]), 14);
        int n291 = this.RL(n286, 10);
        int n292 = n289 + this.RL(-1454113458 + (n287 + this.f5(n290, n288, n291) + this.X[11]), 11);
        int n293 = this.RL(n288, 10);
        int n294 = n291 + this.RL(-1454113458 + (n289 + this.f5(n292, n290, n293) + this.X[6]), 8);
        int n295 = this.RL(n290, 10);
        int n296 = n293 + this.RL(-1454113458 + (n291 + this.f5(n294, n292, n295) + this.X[15]), 5);
        int n297 = this.RL(n292, 10);
        int n298 = n295 + this.RL(-1454113458 + (n293 + this.f5(n296, n294, n297) + this.X[13]), 6);
        int n299 = this.RL(n294, 10);
        int n300 = n265 + this.RL(n263 + this.f1(n266, n232, n267) + this.X[12], 8);
        int n301 = this.RL(n232, 10);
        int n302 = n267 + this.RL(n265 + this.f1(n300, n266, n301) + this.X[15], 5);
        int n303 = this.RL(n266, 10);
        int n304 = n301 + this.RL(n267 + this.f1(n302, n300, n303) + this.X[10], 12);
        int n305 = this.RL(n300, 10);
        int n306 = n303 + this.RL(n301 + this.f1(n304, n302, n305) + this.X[4], 9);
        int n307 = this.RL(n302, 10);
        int n308 = n305 + this.RL(n303 + this.f1(n306, n304, n307) + this.X[1], 12);
        int n309 = this.RL(n304, 10);
        int n310 = n307 + this.RL(n305 + this.f1(n308, n306, n309) + this.X[5], 5);
        int n311 = this.RL(n306, 10);
        int n312 = n309 + this.RL(n307 + this.f1(n310, n308, n311) + this.X[8], 14);
        int n313 = this.RL(n308, 10);
        int n314 = n311 + this.RL(n309 + this.f1(n312, n310, n313) + this.X[7], 6);
        int n315 = this.RL(n310, 10);
        int n316 = n313 + this.RL(n311 + this.f1(n314, n312, n315) + this.X[6], 8);
        int n317 = this.RL(n312, 10);
        int n318 = n315 + this.RL(n313 + this.f1(n316, n314, n317) + this.X[2], 13);
        int n319 = this.RL(n314, 10);
        int n320 = n317 + this.RL(n315 + this.f1(n318, n316, n319) + this.X[13], 6);
        int n321 = this.RL(n316, 10);
        int n322 = n319 + this.RL(n317 + this.f1(n320, n318, n321) + this.X[14], 5);
        int n323 = this.RL(n318, 10);
        int n324 = n321 + this.RL(n319 + this.f1(n322, n320, n323) + this.X[0], 15);
        int n325 = this.RL(n320, 10);
        int n326 = n323 + this.RL(n321 + this.f1(n324, n322, n325) + this.X[3], 13);
        int n327 = this.RL(n322, 10);
        int n328 = n325 + this.RL(n323 + this.f1(n326, n324, n327) + this.X[9], 11);
        int n329 = this.RL(n324, 10);
        int n330 = n327 + this.RL(n325 + this.f1(n328, n326, n329) + this.X[11], 11);
        int n331 = this.RL(n326, 10);
        this.H0 = n295 + this.H0;
        this.H1 = n298 + this.H1;
        this.H2 = n296 + this.H2;
        this.H3 = n299 + this.H3;
        this.H4 = n329 + this.H4;
        this.H5 = n327 + this.H5;
        this.H6 = n330 + this.H6;
        this.H7 = n328 + this.H7;
        this.H8 = n331 + this.H8;
        this.H9 = n297 + this.H9;
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
        this.H4 = -1009589776;
        this.H5 = 1985229328;
        this.H6 = -19088744;
        this.H7 = -1985229329;
        this.H8 = 19088743;
        this.H9 = 1009589775;
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.doCopy((RIPEMD320Digest)memoable);
    }
}

