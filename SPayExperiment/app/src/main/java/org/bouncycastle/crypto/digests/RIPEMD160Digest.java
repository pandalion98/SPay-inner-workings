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

public class RIPEMD160Digest
extends GeneralDigest {
    private static final int DIGEST_LENGTH = 20;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] X = new int[16];
    private int xOff;

    public RIPEMD160Digest() {
        this.reset();
    }

    public RIPEMD160Digest(RIPEMD160Digest rIPEMD160Digest) {
        super(rIPEMD160Digest);
        this.copyIn(rIPEMD160Digest);
    }

    private int RL(int n2, int n3) {
        return n2 << n3 | n2 >>> 32 - n3;
    }

    private void copyIn(RIPEMD160Digest rIPEMD160Digest) {
        super.copyIn(rIPEMD160Digest);
        this.H0 = rIPEMD160Digest.H0;
        this.H1 = rIPEMD160Digest.H1;
        this.H2 = rIPEMD160Digest.H2;
        this.H3 = rIPEMD160Digest.H3;
        this.H4 = rIPEMD160Digest.H4;
        System.arraycopy((Object)rIPEMD160Digest.X, (int)0, (Object)this.X, (int)0, (int)rIPEMD160Digest.X.length);
        this.xOff = rIPEMD160Digest.xOff;
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
        return new RIPEMD160Digest(this);
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.finish();
        this.unpackWord(this.H0, arrby, n2);
        this.unpackWord(this.H1, arrby, n2 + 4);
        this.unpackWord(this.H2, arrby, n2 + 8);
        this.unpackWord(this.H3, arrby, n2 + 12);
        this.unpackWord(this.H4, arrby, n2 + 16);
        this.reset();
        return 20;
    }

    @Override
    public String getAlgorithmName() {
        return "RIPEMD160";
    }

    @Override
    public int getDigestSize() {
        return 20;
    }

    @Override
    protected void processBlock() {
        int n2 = this.H0;
        int n3 = this.H1;
        int n4 = this.H2;
        int n5 = this.H3;
        int n6 = this.H4;
        int n7 = n6 + this.RL(n2 + this.f1(n3, n4, n5) + this.X[0], 11);
        int n8 = this.RL(n4, 10);
        int n9 = n5 + this.RL(n6 + this.f1(n7, n3, n8) + this.X[1], 14);
        int n10 = this.RL(n3, 10);
        int n11 = n8 + this.RL(n5 + this.f1(n9, n7, n10) + this.X[2], 15);
        int n12 = this.RL(n7, 10);
        int n13 = n10 + this.RL(n8 + this.f1(n11, n9, n12) + this.X[3], 12);
        int n14 = this.RL(n9, 10);
        int n15 = n12 + this.RL(n10 + this.f1(n13, n11, n14) + this.X[4], 5);
        int n16 = this.RL(n11, 10);
        int n17 = n14 + this.RL(n12 + this.f1(n15, n13, n16) + this.X[5], 8);
        int n18 = this.RL(n13, 10);
        int n19 = n16 + this.RL(n14 + this.f1(n17, n15, n18) + this.X[6], 7);
        int n20 = this.RL(n15, 10);
        int n21 = n18 + this.RL(n16 + this.f1(n19, n17, n20) + this.X[7], 9);
        int n22 = this.RL(n17, 10);
        int n23 = n20 + this.RL(n18 + this.f1(n21, n19, n22) + this.X[8], 11);
        int n24 = this.RL(n19, 10);
        int n25 = n22 + this.RL(n20 + this.f1(n23, n21, n24) + this.X[9], 13);
        int n26 = this.RL(n21, 10);
        int n27 = n24 + this.RL(n22 + this.f1(n25, n23, n26) + this.X[10], 14);
        int n28 = this.RL(n23, 10);
        int n29 = n26 + this.RL(n24 + this.f1(n27, n25, n28) + this.X[11], 15);
        int n30 = this.RL(n25, 10);
        int n31 = n28 + this.RL(n26 + this.f1(n29, n27, n30) + this.X[12], 6);
        int n32 = this.RL(n27, 10);
        int n33 = n30 + this.RL(n28 + this.f1(n31, n29, n32) + this.X[13], 7);
        int n34 = this.RL(n29, 10);
        int n35 = n32 + this.RL(n30 + this.f1(n33, n31, n34) + this.X[14], 9);
        int n36 = this.RL(n31, 10);
        int n37 = n34 + this.RL(n32 + this.f1(n35, n33, n36) + this.X[15], 8);
        int n38 = this.RL(n33, 10);
        int n39 = n6 + this.RL(1352829926 + (n2 + this.f5(n3, n4, n5) + this.X[5]), 8);
        int n40 = this.RL(n4, 10);
        int n41 = n5 + this.RL(1352829926 + (n6 + this.f5(n39, n3, n40) + this.X[14]), 9);
        int n42 = this.RL(n3, 10);
        int n43 = n40 + this.RL(1352829926 + (n5 + this.f5(n41, n39, n42) + this.X[7]), 9);
        int n44 = this.RL(n39, 10);
        int n45 = n42 + this.RL(1352829926 + (n40 + this.f5(n43, n41, n44) + this.X[0]), 11);
        int n46 = this.RL(n41, 10);
        int n47 = n44 + this.RL(1352829926 + (n42 + this.f5(n45, n43, n46) + this.X[9]), 13);
        int n48 = this.RL(n43, 10);
        int n49 = n46 + this.RL(1352829926 + (n44 + this.f5(n47, n45, n48) + this.X[2]), 15);
        int n50 = this.RL(n45, 10);
        int n51 = n48 + this.RL(1352829926 + (n46 + this.f5(n49, n47, n50) + this.X[11]), 15);
        int n52 = this.RL(n47, 10);
        int n53 = n50 + this.RL(1352829926 + (n48 + this.f5(n51, n49, n52) + this.X[4]), 5);
        int n54 = this.RL(n49, 10);
        int n55 = n52 + this.RL(1352829926 + (n50 + this.f5(n53, n51, n54) + this.X[13]), 7);
        int n56 = this.RL(n51, 10);
        int n57 = n54 + this.RL(1352829926 + (n52 + this.f5(n55, n53, n56) + this.X[6]), 7);
        int n58 = this.RL(n53, 10);
        int n59 = n56 + this.RL(1352829926 + (n54 + this.f5(n57, n55, n58) + this.X[15]), 8);
        int n60 = this.RL(n55, 10);
        int n61 = n58 + this.RL(1352829926 + (n56 + this.f5(n59, n57, n60) + this.X[8]), 11);
        int n62 = this.RL(n57, 10);
        int n63 = n60 + this.RL(1352829926 + (n58 + this.f5(n61, n59, n62) + this.X[1]), 14);
        int n64 = this.RL(n59, 10);
        int n65 = n62 + this.RL(1352829926 + (n60 + this.f5(n63, n61, n64) + this.X[10]), 14);
        int n66 = this.RL(n61, 10);
        int n67 = n64 + this.RL(1352829926 + (n62 + this.f5(n65, n63, n66) + this.X[3]), 12);
        int n68 = this.RL(n63, 10);
        int n69 = n66 + this.RL(1352829926 + (n64 + this.f5(n67, n65, n68) + this.X[12]), 6);
        int n70 = this.RL(n65, 10);
        int n71 = n36 + this.RL(1518500249 + (n34 + this.f2(n37, n35, n38) + this.X[7]), 7);
        int n72 = this.RL(n35, 10);
        int n73 = n38 + this.RL(1518500249 + (n36 + this.f2(n71, n37, n72) + this.X[4]), 6);
        int n74 = this.RL(n37, 10);
        int n75 = n72 + this.RL(1518500249 + (n38 + this.f2(n73, n71, n74) + this.X[13]), 8);
        int n76 = this.RL(n71, 10);
        int n77 = n74 + this.RL(1518500249 + (n72 + this.f2(n75, n73, n76) + this.X[1]), 13);
        int n78 = this.RL(n73, 10);
        int n79 = n76 + this.RL(1518500249 + (n74 + this.f2(n77, n75, n78) + this.X[10]), 11);
        int n80 = this.RL(n75, 10);
        int n81 = n78 + this.RL(1518500249 + (n76 + this.f2(n79, n77, n80) + this.X[6]), 9);
        int n82 = this.RL(n77, 10);
        int n83 = n80 + this.RL(1518500249 + (n78 + this.f2(n81, n79, n82) + this.X[15]), 7);
        int n84 = this.RL(n79, 10);
        int n85 = n82 + this.RL(1518500249 + (n80 + this.f2(n83, n81, n84) + this.X[3]), 15);
        int n86 = this.RL(n81, 10);
        int n87 = n84 + this.RL(1518500249 + (n82 + this.f2(n85, n83, n86) + this.X[12]), 7);
        int n88 = this.RL(n83, 10);
        int n89 = n86 + this.RL(1518500249 + (n84 + this.f2(n87, n85, n88) + this.X[0]), 12);
        int n90 = this.RL(n85, 10);
        int n91 = n88 + this.RL(1518500249 + (n86 + this.f2(n89, n87, n90) + this.X[9]), 15);
        int n92 = this.RL(n87, 10);
        int n93 = n90 + this.RL(1518500249 + (n88 + this.f2(n91, n89, n92) + this.X[5]), 9);
        int n94 = this.RL(n89, 10);
        int n95 = n92 + this.RL(1518500249 + (n90 + this.f2(n93, n91, n94) + this.X[2]), 11);
        int n96 = this.RL(n91, 10);
        int n97 = n94 + this.RL(1518500249 + (n92 + this.f2(n95, n93, n96) + this.X[14]), 7);
        int n98 = this.RL(n93, 10);
        int n99 = n96 + this.RL(1518500249 + (n94 + this.f2(n97, n95, n98) + this.X[11]), 13);
        int n100 = this.RL(n95, 10);
        int n101 = n98 + this.RL(1518500249 + (n96 + this.f2(n99, n97, n100) + this.X[8]), 12);
        int n102 = this.RL(n97, 10);
        int n103 = n68 + this.RL(1548603684 + (n66 + this.f4(n69, n67, n70) + this.X[6]), 9);
        int n104 = this.RL(n67, 10);
        int n105 = n70 + this.RL(1548603684 + (n68 + this.f4(n103, n69, n104) + this.X[11]), 13);
        int n106 = this.RL(n69, 10);
        int n107 = n104 + this.RL(1548603684 + (n70 + this.f4(n105, n103, n106) + this.X[3]), 15);
        int n108 = this.RL(n103, 10);
        int n109 = n106 + this.RL(1548603684 + (n104 + this.f4(n107, n105, n108) + this.X[7]), 7);
        int n110 = this.RL(n105, 10);
        int n111 = n108 + this.RL(1548603684 + (n106 + this.f4(n109, n107, n110) + this.X[0]), 12);
        int n112 = this.RL(n107, 10);
        int n113 = n110 + this.RL(1548603684 + (n108 + this.f4(n111, n109, n112) + this.X[13]), 8);
        int n114 = this.RL(n109, 10);
        int n115 = n112 + this.RL(1548603684 + (n110 + this.f4(n113, n111, n114) + this.X[5]), 9);
        int n116 = this.RL(n111, 10);
        int n117 = n114 + this.RL(1548603684 + (n112 + this.f4(n115, n113, n116) + this.X[10]), 11);
        int n118 = this.RL(n113, 10);
        int n119 = n116 + this.RL(1548603684 + (n114 + this.f4(n117, n115, n118) + this.X[14]), 7);
        int n120 = this.RL(n115, 10);
        int n121 = n118 + this.RL(1548603684 + (n116 + this.f4(n119, n117, n120) + this.X[15]), 7);
        int n122 = this.RL(n117, 10);
        int n123 = n120 + this.RL(1548603684 + (n118 + this.f4(n121, n119, n122) + this.X[8]), 12);
        int n124 = this.RL(n119, 10);
        int n125 = n122 + this.RL(1548603684 + (n120 + this.f4(n123, n121, n124) + this.X[12]), 7);
        int n126 = this.RL(n121, 10);
        int n127 = n124 + this.RL(1548603684 + (n122 + this.f4(n125, n123, n126) + this.X[4]), 6);
        int n128 = this.RL(n123, 10);
        int n129 = n126 + this.RL(1548603684 + (n124 + this.f4(n127, n125, n128) + this.X[9]), 15);
        int n130 = this.RL(n125, 10);
        int n131 = n128 + this.RL(1548603684 + (n126 + this.f4(n129, n127, n130) + this.X[1]), 13);
        int n132 = this.RL(n127, 10);
        int n133 = n130 + this.RL(1548603684 + (n128 + this.f4(n131, n129, n132) + this.X[2]), 11);
        int n134 = this.RL(n129, 10);
        int n135 = n100 + this.RL(1859775393 + (n98 + this.f3(n101, n99, n102) + this.X[3]), 11);
        int n136 = this.RL(n99, 10);
        int n137 = n102 + this.RL(1859775393 + (n100 + this.f3(n135, n101, n136) + this.X[10]), 13);
        int n138 = this.RL(n101, 10);
        int n139 = n136 + this.RL(1859775393 + (n102 + this.f3(n137, n135, n138) + this.X[14]), 6);
        int n140 = this.RL(n135, 10);
        int n141 = n138 + this.RL(1859775393 + (n136 + this.f3(n139, n137, n140) + this.X[4]), 7);
        int n142 = this.RL(n137, 10);
        int n143 = n140 + this.RL(1859775393 + (n138 + this.f3(n141, n139, n142) + this.X[9]), 14);
        int n144 = this.RL(n139, 10);
        int n145 = n142 + this.RL(1859775393 + (n140 + this.f3(n143, n141, n144) + this.X[15]), 9);
        int n146 = this.RL(n141, 10);
        int n147 = n144 + this.RL(1859775393 + (n142 + this.f3(n145, n143, n146) + this.X[8]), 13);
        int n148 = this.RL(n143, 10);
        int n149 = n146 + this.RL(1859775393 + (n144 + this.f3(n147, n145, n148) + this.X[1]), 15);
        int n150 = this.RL(n145, 10);
        int n151 = n148 + this.RL(1859775393 + (n146 + this.f3(n149, n147, n150) + this.X[2]), 14);
        int n152 = this.RL(n147, 10);
        int n153 = n150 + this.RL(1859775393 + (n148 + this.f3(n151, n149, n152) + this.X[7]), 8);
        int n154 = this.RL(n149, 10);
        int n155 = n152 + this.RL(1859775393 + (n150 + this.f3(n153, n151, n154) + this.X[0]), 13);
        int n156 = this.RL(n151, 10);
        int n157 = n154 + this.RL(1859775393 + (n152 + this.f3(n155, n153, n156) + this.X[6]), 6);
        int n158 = this.RL(n153, 10);
        int n159 = n156 + this.RL(1859775393 + (n154 + this.f3(n157, n155, n158) + this.X[13]), 5);
        int n160 = this.RL(n155, 10);
        int n161 = n158 + this.RL(1859775393 + (n156 + this.f3(n159, n157, n160) + this.X[11]), 12);
        int n162 = this.RL(n157, 10);
        int n163 = n160 + this.RL(1859775393 + (n158 + this.f3(n161, n159, n162) + this.X[5]), 7);
        int n164 = this.RL(n159, 10);
        int n165 = n162 + this.RL(1859775393 + (n160 + this.f3(n163, n161, n164) + this.X[12]), 5);
        int n166 = this.RL(n161, 10);
        int n167 = n132 + this.RL(1836072691 + (n130 + this.f3(n133, n131, n134) + this.X[15]), 9);
        int n168 = this.RL(n131, 10);
        int n169 = n134 + this.RL(1836072691 + (n132 + this.f3(n167, n133, n168) + this.X[5]), 7);
        int n170 = this.RL(n133, 10);
        int n171 = n168 + this.RL(1836072691 + (n134 + this.f3(n169, n167, n170) + this.X[1]), 15);
        int n172 = this.RL(n167, 10);
        int n173 = n170 + this.RL(1836072691 + (n168 + this.f3(n171, n169, n172) + this.X[3]), 11);
        int n174 = this.RL(n169, 10);
        int n175 = n172 + this.RL(1836072691 + (n170 + this.f3(n173, n171, n174) + this.X[7]), 8);
        int n176 = this.RL(n171, 10);
        int n177 = n174 + this.RL(1836072691 + (n172 + this.f3(n175, n173, n176) + this.X[14]), 6);
        int n178 = this.RL(n173, 10);
        int n179 = n176 + this.RL(1836072691 + (n174 + this.f3(n177, n175, n178) + this.X[6]), 6);
        int n180 = this.RL(n175, 10);
        int n181 = n178 + this.RL(1836072691 + (n176 + this.f3(n179, n177, n180) + this.X[9]), 14);
        int n182 = this.RL(n177, 10);
        int n183 = n180 + this.RL(1836072691 + (n178 + this.f3(n181, n179, n182) + this.X[11]), 12);
        int n184 = this.RL(n179, 10);
        int n185 = n182 + this.RL(1836072691 + (n180 + this.f3(n183, n181, n184) + this.X[8]), 13);
        int n186 = this.RL(n181, 10);
        int n187 = n184 + this.RL(1836072691 + (n182 + this.f3(n185, n183, n186) + this.X[12]), 5);
        int n188 = this.RL(n183, 10);
        int n189 = n186 + this.RL(1836072691 + (n184 + this.f3(n187, n185, n188) + this.X[2]), 14);
        int n190 = this.RL(n185, 10);
        int n191 = n188 + this.RL(1836072691 + (n186 + this.f3(n189, n187, n190) + this.X[10]), 13);
        int n192 = this.RL(n187, 10);
        int n193 = n190 + this.RL(1836072691 + (n188 + this.f3(n191, n189, n192) + this.X[0]), 13);
        int n194 = this.RL(n189, 10);
        int n195 = n192 + this.RL(1836072691 + (n190 + this.f3(n193, n191, n194) + this.X[4]), 7);
        int n196 = this.RL(n191, 10);
        int n197 = n194 + this.RL(1836072691 + (n192 + this.f3(n195, n193, n196) + this.X[13]), 5);
        int n198 = this.RL(n193, 10);
        int n199 = n164 + this.RL(-1894007588 + (n162 + this.f4(n165, n163, n166) + this.X[1]), 11);
        int n200 = this.RL(n163, 10);
        int n201 = n166 + this.RL(-1894007588 + (n164 + this.f4(n199, n165, n200) + this.X[9]), 12);
        int n202 = this.RL(n165, 10);
        int n203 = n200 + this.RL(-1894007588 + (n166 + this.f4(n201, n199, n202) + this.X[11]), 14);
        int n204 = this.RL(n199, 10);
        int n205 = n202 + this.RL(-1894007588 + (n200 + this.f4(n203, n201, n204) + this.X[10]), 15);
        int n206 = this.RL(n201, 10);
        int n207 = n204 + this.RL(-1894007588 + (n202 + this.f4(n205, n203, n206) + this.X[0]), 14);
        int n208 = this.RL(n203, 10);
        int n209 = n206 + this.RL(-1894007588 + (n204 + this.f4(n207, n205, n208) + this.X[8]), 15);
        int n210 = this.RL(n205, 10);
        int n211 = n208 + this.RL(-1894007588 + (n206 + this.f4(n209, n207, n210) + this.X[12]), 9);
        int n212 = this.RL(n207, 10);
        int n213 = n210 + this.RL(-1894007588 + (n208 + this.f4(n211, n209, n212) + this.X[4]), 8);
        int n214 = this.RL(n209, 10);
        int n215 = n212 + this.RL(-1894007588 + (n210 + this.f4(n213, n211, n214) + this.X[13]), 9);
        int n216 = this.RL(n211, 10);
        int n217 = n214 + this.RL(-1894007588 + (n212 + this.f4(n215, n213, n216) + this.X[3]), 14);
        int n218 = this.RL(n213, 10);
        int n219 = n216 + this.RL(-1894007588 + (n214 + this.f4(n217, n215, n218) + this.X[7]), 5);
        int n220 = this.RL(n215, 10);
        int n221 = n218 + this.RL(-1894007588 + (n216 + this.f4(n219, n217, n220) + this.X[15]), 6);
        int n222 = this.RL(n217, 10);
        int n223 = n220 + this.RL(-1894007588 + (n218 + this.f4(n221, n219, n222) + this.X[14]), 8);
        int n224 = this.RL(n219, 10);
        int n225 = n222 + this.RL(-1894007588 + (n220 + this.f4(n223, n221, n224) + this.X[5]), 6);
        int n226 = this.RL(n221, 10);
        int n227 = n224 + this.RL(-1894007588 + (n222 + this.f4(n225, n223, n226) + this.X[6]), 5);
        int n228 = this.RL(n223, 10);
        int n229 = n226 + this.RL(-1894007588 + (n224 + this.f4(n227, n225, n228) + this.X[2]), 12);
        int n230 = this.RL(n225, 10);
        int n231 = n196 + this.RL(2053994217 + (n194 + this.f2(n197, n195, n198) + this.X[8]), 15);
        int n232 = this.RL(n195, 10);
        int n233 = n198 + this.RL(2053994217 + (n196 + this.f2(n231, n197, n232) + this.X[6]), 5);
        int n234 = this.RL(n197, 10);
        int n235 = n232 + this.RL(2053994217 + (n198 + this.f2(n233, n231, n234) + this.X[4]), 8);
        int n236 = this.RL(n231, 10);
        int n237 = n234 + this.RL(2053994217 + (n232 + this.f2(n235, n233, n236) + this.X[1]), 11);
        int n238 = this.RL(n233, 10);
        int n239 = n236 + this.RL(2053994217 + (n234 + this.f2(n237, n235, n238) + this.X[3]), 14);
        int n240 = this.RL(n235, 10);
        int n241 = n238 + this.RL(2053994217 + (n236 + this.f2(n239, n237, n240) + this.X[11]), 14);
        int n242 = this.RL(n237, 10);
        int n243 = n240 + this.RL(2053994217 + (n238 + this.f2(n241, n239, n242) + this.X[15]), 6);
        int n244 = this.RL(n239, 10);
        int n245 = n242 + this.RL(2053994217 + (n240 + this.f2(n243, n241, n244) + this.X[0]), 14);
        int n246 = this.RL(n241, 10);
        int n247 = n244 + this.RL(2053994217 + (n242 + this.f2(n245, n243, n246) + this.X[5]), 6);
        int n248 = this.RL(n243, 10);
        int n249 = n246 + this.RL(2053994217 + (n244 + this.f2(n247, n245, n248) + this.X[12]), 9);
        int n250 = this.RL(n245, 10);
        int n251 = n248 + this.RL(2053994217 + (n246 + this.f2(n249, n247, n250) + this.X[2]), 12);
        int n252 = this.RL(n247, 10);
        int n253 = n250 + this.RL(2053994217 + (n248 + this.f2(n251, n249, n252) + this.X[13]), 9);
        int n254 = this.RL(n249, 10);
        int n255 = n252 + this.RL(2053994217 + (n250 + this.f2(n253, n251, n254) + this.X[9]), 12);
        int n256 = this.RL(n251, 10);
        int n257 = n254 + this.RL(2053994217 + (n252 + this.f2(n255, n253, n256) + this.X[7]), 5);
        int n258 = this.RL(n253, 10);
        int n259 = n256 + this.RL(2053994217 + (n254 + this.f2(n257, n255, n258) + this.X[10]), 15);
        int n260 = this.RL(n255, 10);
        int n261 = n258 + this.RL(2053994217 + (n256 + this.f2(n259, n257, n260) + this.X[14]), 8);
        int n262 = this.RL(n257, 10);
        int n263 = n228 + this.RL(-1454113458 + (n226 + this.f5(n229, n227, n230) + this.X[4]), 9);
        int n264 = this.RL(n227, 10);
        int n265 = n230 + this.RL(-1454113458 + (n228 + this.f5(n263, n229, n264) + this.X[0]), 15);
        int n266 = this.RL(n229, 10);
        int n267 = n264 + this.RL(-1454113458 + (n230 + this.f5(n265, n263, n266) + this.X[5]), 5);
        int n268 = this.RL(n263, 10);
        int n269 = n266 + this.RL(-1454113458 + (n264 + this.f5(n267, n265, n268) + this.X[9]), 11);
        int n270 = this.RL(n265, 10);
        int n271 = n268 + this.RL(-1454113458 + (n266 + this.f5(n269, n267, n270) + this.X[7]), 6);
        int n272 = this.RL(n267, 10);
        int n273 = n270 + this.RL(-1454113458 + (n268 + this.f5(n271, n269, n272) + this.X[12]), 8);
        int n274 = this.RL(n269, 10);
        int n275 = n272 + this.RL(-1454113458 + (n270 + this.f5(n273, n271, n274) + this.X[2]), 13);
        int n276 = this.RL(n271, 10);
        int n277 = n274 + this.RL(-1454113458 + (n272 + this.f5(n275, n273, n276) + this.X[10]), 12);
        int n278 = this.RL(n273, 10);
        int n279 = n276 + this.RL(-1454113458 + (n274 + this.f5(n277, n275, n278) + this.X[14]), 5);
        int n280 = this.RL(n275, 10);
        int n281 = n278 + this.RL(-1454113458 + (n276 + this.f5(n279, n277, n280) + this.X[1]), 12);
        int n282 = this.RL(n277, 10);
        int n283 = n280 + this.RL(-1454113458 + (n278 + this.f5(n281, n279, n282) + this.X[3]), 13);
        int n284 = this.RL(n279, 10);
        int n285 = n282 + this.RL(-1454113458 + (n280 + this.f5(n283, n281, n284) + this.X[8]), 14);
        int n286 = this.RL(n281, 10);
        int n287 = n284 + this.RL(-1454113458 + (n282 + this.f5(n285, n283, n286) + this.X[11]), 11);
        int n288 = this.RL(n283, 10);
        int n289 = n286 + this.RL(-1454113458 + (n284 + this.f5(n287, n285, n288) + this.X[6]), 8);
        int n290 = this.RL(n285, 10);
        int n291 = n288 + this.RL(-1454113458 + (n286 + this.f5(n289, n287, n290) + this.X[15]), 5);
        int n292 = this.RL(n287, 10);
        int n293 = n290 + this.RL(-1454113458 + (n288 + this.f5(n291, n289, n292) + this.X[13]), 6);
        int n294 = this.RL(n289, 10);
        int n295 = n260 + this.RL(n258 + this.f1(n261, n259, n262) + this.X[12], 8);
        int n296 = this.RL(n259, 10);
        int n297 = n262 + this.RL(n260 + this.f1(n295, n261, n296) + this.X[15], 5);
        int n298 = this.RL(n261, 10);
        int n299 = n296 + this.RL(n262 + this.f1(n297, n295, n298) + this.X[10], 12);
        int n300 = this.RL(n295, 10);
        int n301 = n298 + this.RL(n296 + this.f1(n299, n297, n300) + this.X[4], 9);
        int n302 = this.RL(n297, 10);
        int n303 = n300 + this.RL(n298 + this.f1(n301, n299, n302) + this.X[1], 12);
        int n304 = this.RL(n299, 10);
        int n305 = n302 + this.RL(n300 + this.f1(n303, n301, n304) + this.X[5], 5);
        int n306 = this.RL(n301, 10);
        int n307 = n304 + this.RL(n302 + this.f1(n305, n303, n306) + this.X[8], 14);
        int n308 = this.RL(n303, 10);
        int n309 = n306 + this.RL(n304 + this.f1(n307, n305, n308) + this.X[7], 6);
        int n310 = this.RL(n305, 10);
        int n311 = n308 + this.RL(n306 + this.f1(n309, n307, n310) + this.X[6], 8);
        int n312 = this.RL(n307, 10);
        int n313 = n310 + this.RL(n308 + this.f1(n311, n309, n312) + this.X[2], 13);
        int n314 = this.RL(n309, 10);
        int n315 = n312 + this.RL(n310 + this.f1(n313, n311, n314) + this.X[13], 6);
        int n316 = this.RL(n311, 10);
        int n317 = n314 + this.RL(n312 + this.f1(n315, n313, n316) + this.X[14], 5);
        int n318 = this.RL(n313, 10);
        int n319 = n316 + this.RL(n314 + this.f1(n317, n315, n318) + this.X[0], 15);
        int n320 = this.RL(n315, 10);
        int n321 = n318 + this.RL(n316 + this.f1(n319, n317, n320) + this.X[3], 13);
        int n322 = this.RL(n317, 10);
        int n323 = n320 + this.RL(n318 + this.f1(n321, n319, n322) + this.X[9], 11);
        int n324 = this.RL(n319, 10);
        int n325 = n322 + this.RL(n320 + this.f1(n323, n321, n324) + this.X[11], 11);
        int n326 = this.RL(n321, 10) + (n291 + this.H1);
        this.H1 = n324 + (n294 + this.H2);
        this.H2 = n322 + (n292 + this.H3);
        this.H3 = n325 + (n290 + this.H4);
        this.H4 = n323 + (n293 + this.H0);
        this.H0 = n326;
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
        this.xOff = 0;
        for (int i2 = 0; i2 != this.X.length; ++i2) {
            this.X[i2] = 0;
        }
    }

    @Override
    public void reset(Memoable memoable) {
        this.copyIn((RIPEMD160Digest)memoable);
    }
}

