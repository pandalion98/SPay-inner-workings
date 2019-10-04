/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.TweakableBlockCipherParameters;

public class ThreefishEngine
implements BlockCipher {
    public static final int BLOCKSIZE_1024 = 1024;
    public static final int BLOCKSIZE_256 = 256;
    public static final int BLOCKSIZE_512 = 512;
    private static final long C_240 = 2004413935125273122L;
    private static final int MAX_ROUNDS = 80;
    private static int[] MOD17;
    private static int[] MOD3;
    private static int[] MOD5;
    private static int[] MOD9;
    private static final int ROUNDS_1024 = 80;
    private static final int ROUNDS_256 = 72;
    private static final int ROUNDS_512 = 72;
    private static final int TWEAK_SIZE_BYTES = 16;
    private static final int TWEAK_SIZE_WORDS = 2;
    private int blocksizeBytes;
    private int blocksizeWords;
    private ThreefishCipher cipher;
    private long[] currentBlock;
    private boolean forEncryption;
    private long[] kw;
    private long[] t = new long[5];

    static {
        MOD9 = new int[80];
        MOD17 = new int[MOD9.length];
        MOD5 = new int[MOD9.length];
        MOD3 = new int[MOD9.length];
        for (int i2 = 0; i2 < MOD9.length; ++i2) {
            ThreefishEngine.MOD17[i2] = i2 % 17;
            ThreefishEngine.MOD9[i2] = i2 % 9;
            ThreefishEngine.MOD5[i2] = i2 % 5;
            ThreefishEngine.MOD3[i2] = i2 % 3;
        }
    }

    public ThreefishEngine(int n2) {
        this.blocksizeBytes = n2 / 8;
        this.blocksizeWords = this.blocksizeBytes / 8;
        this.currentBlock = new long[this.blocksizeWords];
        this.kw = new long[1 + 2 * this.blocksizeWords];
        switch (n2) {
            default: {
                throw new IllegalArgumentException("Invalid blocksize - Threefish is defined with block size of 256, 512, or 1024 bits");
            }
            case 256: {
                this.cipher = new Threefish256Cipher(this.kw, this.t);
                return;
            }
            case 512: {
                this.cipher = new Threefish512Cipher(this.kw, this.t);
                return;
            }
            case 1024: 
        }
        this.cipher = new Threefish1024Cipher(this.kw, this.t);
    }

    public static long bytesToWord(byte[] arrby, int n2) {
        if (n2 + 8 > arrby.length) {
            throw new IllegalArgumentException();
        }
        int n3 = n2 + 1;
        long l2 = 255L & (long)arrby[n2];
        int n4 = n3 + 1;
        long l3 = l2 | (255L & (long)arrby[n3]) << 8;
        int n5 = n4 + 1;
        long l4 = l3 | (255L & (long)arrby[n4]) << 16;
        int n6 = n5 + 1;
        long l5 = l4 | (255L & (long)arrby[n5]) << 24;
        int n7 = n6 + 1;
        long l6 = l5 | (255L & (long)arrby[n6]) << 32;
        int n8 = n7 + 1;
        long l7 = l6 | (255L & (long)arrby[n7]) << 40;
        int n9 = n8 + 1;
        long l8 = l7 | (255L & (long)arrby[n8]) << 48;
        n9 + 1;
        return l8 | (255L & (long)arrby[n9]) << 56;
    }

    static long rotlXor(long l2, int n2, long l3) {
        return l3 ^ (l2 << n2 | l2 >>> -n2);
    }

    private void setKey(long[] arrl) {
        if (arrl.length != this.blocksizeWords) {
            throw new IllegalArgumentException("Threefish key must be same size as block (" + this.blocksizeWords + " words)");
        }
        long l2 = 2004413935125273122L;
        for (int i2 = 0; i2 < this.blocksizeWords; ++i2) {
            this.kw[i2] = arrl[i2];
            l2 ^= this.kw[i2];
        }
        this.kw[this.blocksizeWords] = l2;
        System.arraycopy((Object)this.kw, (int)0, (Object)this.kw, (int)(1 + this.blocksizeWords), (int)this.blocksizeWords);
    }

    private void setTweak(long[] arrl) {
        if (arrl.length != 2) {
            throw new IllegalArgumentException("Tweak must be 2 words.");
        }
        this.t[0] = arrl[0];
        this.t[1] = arrl[1];
        this.t[2] = this.t[0] ^ this.t[1];
        this.t[3] = this.t[0];
        this.t[4] = this.t[1];
    }

    public static void wordToBytes(long l2, byte[] arrby, int n2) {
        if (n2 + 8 > arrby.length) {
            throw new IllegalArgumentException();
        }
        int n3 = n2 + 1;
        arrby[n2] = (byte)l2;
        int n4 = n3 + 1;
        arrby[n3] = (byte)(l2 >> 8);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(l2 >> 16);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(l2 >> 24);
        int n7 = n6 + 1;
        arrby[n6] = (byte)(l2 >> 32);
        int n8 = n7 + 1;
        arrby[n7] = (byte)(l2 >> 40);
        int n9 = n8 + 1;
        arrby[n8] = (byte)(l2 >> 48);
        n9 + 1;
        arrby[n9] = (byte)(l2 >> 56);
    }

    static long xorRotr(long l2, int n2, long l3) {
        long l4 = l2 ^ l3;
        return l4 >>> n2 | l4 << -n2;
    }

    @Override
    public String getAlgorithmName() {
        return "Threefish-" + 8 * this.blocksizeBytes;
    }

    @Override
    public int getBlockSize() {
        return this.blocksizeBytes;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        long[] arrl;
        long[] arrl2;
        byte[] arrby;
        byte[] arrby2;
        if (cipherParameters instanceof TweakableBlockCipherParameters) {
            TweakableBlockCipherParameters tweakableBlockCipherParameters = (TweakableBlockCipherParameters)cipherParameters;
            byte[] arrby3 = tweakableBlockCipherParameters.getKey().getKey();
            arrby2 = tweakableBlockCipherParameters.getTweak();
            arrby = arrby3;
        } else {
            if (!(cipherParameters instanceof KeyParameter)) {
                throw new IllegalArgumentException("Invalid parameter passed to Threefish init - " + cipherParameters.getClass().getName());
            }
            arrby = ((KeyParameter)cipherParameters).getKey();
            arrby2 = null;
        }
        if (arrby != null) {
            if (arrby.length != this.blocksizeBytes) {
                throw new IllegalArgumentException("Threefish key must be same size as block (" + this.blocksizeBytes + " bytes)");
            }
            arrl2 = new long[this.blocksizeWords];
            for (int i2 = 0; i2 < arrl2.length; ++i2) {
                arrl2[i2] = ThreefishEngine.bytesToWord(arrby, i2 * 8);
            }
        } else {
            arrl2 = null;
        }
        if (arrby2 != null) {
            if (arrby2.length != 16) {
                throw new IllegalArgumentException("Threefish tweak must be 16 bytes");
            }
            arrl = new long[]{ThreefishEngine.bytesToWord(arrby2, 0), ThreefishEngine.bytesToWord(arrby2, 8)};
        } else {
            arrl = null;
        }
        this.init(bl, arrl2, arrl);
    }

    public void init(boolean bl, long[] arrl, long[] arrl2) {
        this.forEncryption = bl;
        if (arrl != null) {
            this.setKey(arrl);
        }
        if (arrl2 != null) {
            this.setTweak(arrl2);
        }
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = 0;
        if (n3 + this.blocksizeBytes > arrby2.length) {
            throw new DataLengthException("Output buffer too short");
        }
        if (n2 + this.blocksizeBytes > arrby.length) {
            throw new DataLengthException("Input buffer too short");
        }
        for (int i2 = 0; i2 < this.blocksizeBytes; i2 += 8) {
            this.currentBlock[i2 >> 3] = ThreefishEngine.bytesToWord(arrby, n2 + i2);
        }
        this.processBlock(this.currentBlock, this.currentBlock);
        while (n4 < this.blocksizeBytes) {
            ThreefishEngine.wordToBytes(this.currentBlock[n4 >> 3], arrby2, n3 + n4);
            n4 += 8;
        }
        return this.blocksizeBytes;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int processBlock(long[] arrl, long[] arrl2) {
        if (this.kw[this.blocksizeWords] == 0L) {
            throw new IllegalStateException("Threefish engine not initialised");
        }
        if (arrl.length != this.blocksizeWords) {
            throw new DataLengthException("Input buffer too short");
        }
        if (arrl2.length != this.blocksizeWords) {
            throw new DataLengthException("Output buffer too short");
        }
        if (this.forEncryption) {
            this.cipher.encryptBlock(arrl, arrl2);
            do {
                return this.blocksizeWords;
                break;
            } while (true);
        }
        this.cipher.decryptBlock(arrl, arrl2);
        return this.blocksizeWords;
    }

    @Override
    public void reset() {
    }

    private static final class Threefish1024Cipher
    extends ThreefishCipher {
        private static final int ROTATION_0_0 = 24;
        private static final int ROTATION_0_1 = 13;
        private static final int ROTATION_0_2 = 8;
        private static final int ROTATION_0_3 = 47;
        private static final int ROTATION_0_4 = 8;
        private static final int ROTATION_0_5 = 17;
        private static final int ROTATION_0_6 = 22;
        private static final int ROTATION_0_7 = 37;
        private static final int ROTATION_1_0 = 38;
        private static final int ROTATION_1_1 = 19;
        private static final int ROTATION_1_2 = 10;
        private static final int ROTATION_1_3 = 55;
        private static final int ROTATION_1_4 = 49;
        private static final int ROTATION_1_5 = 18;
        private static final int ROTATION_1_6 = 23;
        private static final int ROTATION_1_7 = 52;
        private static final int ROTATION_2_0 = 33;
        private static final int ROTATION_2_1 = 4;
        private static final int ROTATION_2_2 = 51;
        private static final int ROTATION_2_3 = 13;
        private static final int ROTATION_2_4 = 34;
        private static final int ROTATION_2_5 = 41;
        private static final int ROTATION_2_6 = 59;
        private static final int ROTATION_2_7 = 17;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 20;
        private static final int ROTATION_3_2 = 48;
        private static final int ROTATION_3_3 = 41;
        private static final int ROTATION_3_4 = 47;
        private static final int ROTATION_3_5 = 28;
        private static final int ROTATION_3_6 = 16;
        private static final int ROTATION_3_7 = 25;
        private static final int ROTATION_4_0 = 41;
        private static final int ROTATION_4_1 = 9;
        private static final int ROTATION_4_2 = 37;
        private static final int ROTATION_4_3 = 31;
        private static final int ROTATION_4_4 = 12;
        private static final int ROTATION_4_5 = 47;
        private static final int ROTATION_4_6 = 44;
        private static final int ROTATION_4_7 = 30;
        private static final int ROTATION_5_0 = 16;
        private static final int ROTATION_5_1 = 34;
        private static final int ROTATION_5_2 = 56;
        private static final int ROTATION_5_3 = 51;
        private static final int ROTATION_5_4 = 4;
        private static final int ROTATION_5_5 = 53;
        private static final int ROTATION_5_6 = 42;
        private static final int ROTATION_5_7 = 41;
        private static final int ROTATION_6_0 = 31;
        private static final int ROTATION_6_1 = 44;
        private static final int ROTATION_6_2 = 47;
        private static final int ROTATION_6_3 = 46;
        private static final int ROTATION_6_4 = 19;
        private static final int ROTATION_6_5 = 42;
        private static final int ROTATION_6_6 = 44;
        private static final int ROTATION_6_7 = 25;
        private static final int ROTATION_7_0 = 9;
        private static final int ROTATION_7_1 = 48;
        private static final int ROTATION_7_2 = 35;
        private static final int ROTATION_7_3 = 52;
        private static final int ROTATION_7_4 = 23;
        private static final int ROTATION_7_5 = 31;
        private static final int ROTATION_7_6 = 37;
        private static final int ROTATION_7_7 = 20;

        public Threefish1024Cipher(long[] arrl, long[] arrl2) {
            super(arrl, arrl2);
        }

        @Override
        void decryptBlock(long[] arrl, long[] arrl2) {
            long[] arrl3 = this.kw;
            long[] arrl4 = this.t;
            int[] arrn = MOD17;
            int[] arrn2 = MOD3;
            if (arrl3.length != 33) {
                throw new IllegalArgumentException();
            }
            if (arrl4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = arrl[0];
            long l3 = arrl[1];
            long l4 = arrl[2];
            long l5 = arrl[3];
            long l6 = arrl[4];
            long l7 = arrl[5];
            long l8 = arrl[6];
            long l9 = arrl[7];
            long l10 = arrl[8];
            long l11 = arrl[9];
            long l12 = arrl[10];
            long l13 = arrl[11];
            long l14 = arrl[12];
            long l15 = arrl[13];
            long l16 = arrl[14];
            long l17 = arrl[15];
            for (int i2 = 19; i2 >= 1; i2 -= 2) {
                int n2 = arrn[i2];
                int n3 = arrn2[i2];
                long l18 = l2 - arrl3[n2 + 1];
                long l19 = l3 - arrl3[n2 + 2];
                long l20 = l4 - arrl3[n2 + 3];
                long l21 = l5 - arrl3[n2 + 4];
                long l22 = l6 - arrl3[n2 + 5];
                long l23 = l7 - arrl3[n2 + 6];
                long l24 = l8 - arrl3[n2 + 7];
                long l25 = l9 - arrl3[n2 + 8];
                long l26 = l10 - arrl3[n2 + 9];
                long l27 = l11 - arrl3[n2 + 10];
                long l28 = l12 - arrl3[n2 + 11];
                long l29 = l13 - arrl3[n2 + 12];
                long l30 = l14 - arrl3[n2 + 13];
                long l31 = l15 - (arrl3[n2 + 14] + arrl4[n3 + 1]);
                long l32 = l16 - (arrl3[n2 + 15] + arrl4[n3 + 2]);
                long l33 = ThreefishEngine.xorRotr(l17 - (1L + (arrl3[n2 + 16] + (long)i2)), 9, l18);
                long l34 = l18 - l33;
                long l35 = ThreefishEngine.xorRotr(l29, 48, l20);
                long l36 = l20 - l35;
                long l37 = ThreefishEngine.xorRotr(l31, 35, l24);
                long l38 = l24 - l37;
                long l39 = ThreefishEngine.xorRotr(l27, 52, l22);
                long l40 = l22 - l39;
                long l41 = ThreefishEngine.xorRotr(l19, 23, l32);
                long l42 = l32 - l41;
                long l43 = ThreefishEngine.xorRotr(l23, 31, l26);
                long l44 = l26 - l43;
                long l45 = ThreefishEngine.xorRotr(l21, 37, l28);
                long l46 = l28 - l45;
                long l47 = ThreefishEngine.xorRotr(l25, 20, l30);
                long l48 = l30 - l47;
                long l49 = ThreefishEngine.xorRotr(l47, 31, l34);
                long l50 = l34 - l49;
                long l51 = ThreefishEngine.xorRotr(l43, 44, l36);
                long l52 = l36 - l51;
                long l53 = ThreefishEngine.xorRotr(l45, 47, l40);
                long l54 = l40 - l53;
                long l55 = ThreefishEngine.xorRotr(l41, 46, l38);
                long l56 = l38 - l55;
                long l57 = ThreefishEngine.xorRotr(l33, 19, l48);
                long l58 = l48 - l57;
                long l59 = ThreefishEngine.xorRotr(l37, 42, l42);
                long l60 = l42 - l59;
                long l61 = ThreefishEngine.xorRotr(l35, 44, l44);
                long l62 = l44 - l61;
                long l63 = ThreefishEngine.xorRotr(l39, 25, l46);
                long l64 = l46 - l63;
                long l65 = ThreefishEngine.xorRotr(l63, 16, l50);
                long l66 = l50 - l65;
                long l67 = ThreefishEngine.xorRotr(l59, 34, l52);
                long l68 = l52 - l67;
                long l69 = ThreefishEngine.xorRotr(l61, 56, l56);
                long l70 = l56 - l69;
                long l71 = ThreefishEngine.xorRotr(l57, 51, l54);
                long l72 = l54 - l71;
                long l73 = ThreefishEngine.xorRotr(l49, 4, l64);
                long l74 = l64 - l73;
                long l75 = ThreefishEngine.xorRotr(l53, 53, l58);
                long l76 = l58 - l75;
                long l77 = ThreefishEngine.xorRotr(l51, 42, l60);
                long l78 = l60 - l77;
                long l79 = ThreefishEngine.xorRotr(l55, 41, l62);
                long l80 = l62 - l79;
                long l81 = ThreefishEngine.xorRotr(l79, 41, l66);
                long l82 = l66 - l81;
                long l83 = ThreefishEngine.xorRotr(l75, 9, l68);
                long l84 = l68 - l83;
                long l85 = ThreefishEngine.xorRotr(l77, 37, l72);
                long l86 = l72 - l85;
                long l87 = ThreefishEngine.xorRotr(l73, 31, l70);
                long l88 = l70 - l87;
                long l89 = ThreefishEngine.xorRotr(l65, 12, l80);
                long l90 = l80 - l89;
                long l91 = ThreefishEngine.xorRotr(l69, 47, l74);
                long l92 = l74 - l91;
                long l93 = ThreefishEngine.xorRotr(l67, 44, l76);
                long l94 = l76 - l93;
                long l95 = ThreefishEngine.xorRotr(l71, 30, l78);
                long l96 = l78 - l95;
                long l97 = l82 - arrl3[n2];
                long l98 = l81 - arrl3[n2 + 1];
                long l99 = l84 - arrl3[n2 + 2];
                long l100 = l83 - arrl3[n2 + 3];
                long l101 = l86 - arrl3[n2 + 4];
                long l102 = l85 - arrl3[n2 + 5];
                long l103 = l88 - arrl3[n2 + 6];
                long l104 = l87 - arrl3[n2 + 7];
                long l105 = l90 - arrl3[n2 + 8];
                long l106 = l89 - arrl3[n2 + 9];
                long l107 = l92 - arrl3[n2 + 10];
                long l108 = l91 - arrl3[n2 + 11];
                long l109 = l94 - arrl3[n2 + 12];
                long l110 = l93 - (arrl3[n2 + 13] + arrl4[n3]);
                long l111 = l96 - (arrl3[n2 + 14] + arrl4[n3 + 1]);
                long l112 = ThreefishEngine.xorRotr(l95 - (arrl3[n2 + 15] + (long)i2), 5, l97);
                long l113 = l97 - l112;
                long l114 = ThreefishEngine.xorRotr(l108, 20, l99);
                long l115 = l99 - l114;
                long l116 = ThreefishEngine.xorRotr(l110, 48, l103);
                long l117 = l103 - l116;
                long l118 = ThreefishEngine.xorRotr(l106, 41, l101);
                long l119 = l101 - l118;
                long l120 = ThreefishEngine.xorRotr(l98, 47, l111);
                long l121 = l111 - l120;
                long l122 = ThreefishEngine.xorRotr(l102, 28, l105);
                long l123 = l105 - l122;
                long l124 = ThreefishEngine.xorRotr(l100, 16, l107);
                long l125 = l107 - l124;
                long l126 = ThreefishEngine.xorRotr(l104, 25, l109);
                long l127 = l109 - l126;
                long l128 = ThreefishEngine.xorRotr(l126, 33, l113);
                long l129 = l113 - l128;
                long l130 = ThreefishEngine.xorRotr(l122, 4, l115);
                long l131 = l115 - l130;
                long l132 = ThreefishEngine.xorRotr(l124, 51, l119);
                long l133 = l119 - l132;
                long l134 = ThreefishEngine.xorRotr(l120, 13, l117);
                long l135 = l117 - l134;
                long l136 = ThreefishEngine.xorRotr(l112, 34, l127);
                long l137 = l127 - l136;
                long l138 = ThreefishEngine.xorRotr(l116, 41, l121);
                long l139 = l121 - l138;
                long l140 = ThreefishEngine.xorRotr(l114, 59, l123);
                long l141 = l123 - l140;
                long l142 = ThreefishEngine.xorRotr(l118, 17, l125);
                long l143 = l125 - l142;
                long l144 = ThreefishEngine.xorRotr(l142, 38, l129);
                long l145 = l129 - l144;
                long l146 = ThreefishEngine.xorRotr(l138, 19, l131);
                long l147 = l131 - l146;
                long l148 = ThreefishEngine.xorRotr(l140, 10, l135);
                long l149 = l135 - l148;
                long l150 = ThreefishEngine.xorRotr(l136, 55, l133);
                long l151 = l133 - l150;
                long l152 = ThreefishEngine.xorRotr(l128, 49, l143);
                long l153 = l143 - l152;
                long l154 = ThreefishEngine.xorRotr(l132, 18, l137);
                long l155 = l137 - l154;
                long l156 = ThreefishEngine.xorRotr(l130, 23, l139);
                long l157 = l139 - l156;
                long l158 = ThreefishEngine.xorRotr(l134, 52, l141);
                long l159 = l141 - l158;
                l3 = ThreefishEngine.xorRotr(l158, 24, l145);
                l2 = l145 - l3;
                l5 = ThreefishEngine.xorRotr(l154, 13, l147);
                l4 = l147 - l5;
                l7 = ThreefishEngine.xorRotr(l156, 8, l151);
                l6 = l151 - l7;
                l9 = ThreefishEngine.xorRotr(l152, 47, l149);
                l8 = l149 - l9;
                l11 = ThreefishEngine.xorRotr(l144, 8, l159);
                l10 = l159 - l11;
                l13 = ThreefishEngine.xorRotr(l148, 17, l153);
                l12 = l153 - l13;
                l15 = ThreefishEngine.xorRotr(l146, 22, l155);
                l14 = l155 - l15;
                l17 = ThreefishEngine.xorRotr(l150, 37, l157);
                l16 = l157 - l17;
            }
            long l160 = l2 - arrl3[0];
            long l161 = l3 - arrl3[1];
            long l162 = l4 - arrl3[2];
            long l163 = l5 - arrl3[3];
            long l164 = l6 - arrl3[4];
            long l165 = l7 - arrl3[5];
            long l166 = l8 - arrl3[6];
            long l167 = l9 - arrl3[7];
            long l168 = l10 - arrl3[8];
            long l169 = l11 - arrl3[9];
            long l170 = l12 - arrl3[10];
            long l171 = l13 - arrl3[11];
            long l172 = l14 - arrl3[12];
            long l173 = l15 - (arrl3[13] + arrl4[0]);
            long l174 = l16 - (arrl3[14] + arrl4[1]);
            long l175 = l17 - arrl3[15];
            arrl2[0] = l160;
            arrl2[1] = l161;
            arrl2[2] = l162;
            arrl2[3] = l163;
            arrl2[4] = l164;
            arrl2[5] = l165;
            arrl2[6] = l166;
            arrl2[7] = l167;
            arrl2[8] = l168;
            arrl2[9] = l169;
            arrl2[10] = l170;
            arrl2[11] = l171;
            arrl2[12] = l172;
            arrl2[13] = l173;
            arrl2[14] = l174;
            arrl2[15] = l175;
        }

        @Override
        void encryptBlock(long[] arrl, long[] arrl2) {
            long[] arrl3 = this.kw;
            long[] arrl4 = this.t;
            int[] arrn = MOD17;
            int[] arrn2 = MOD3;
            if (arrl3.length != 33) {
                throw new IllegalArgumentException();
            }
            if (arrl4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = arrl[0];
            long l3 = arrl[1];
            long l4 = arrl[2];
            long l5 = arrl[3];
            long l6 = arrl[4];
            long l7 = arrl[5];
            long l8 = arrl[6];
            long l9 = arrl[7];
            long l10 = arrl[8];
            long l11 = arrl[9];
            long l12 = arrl[10];
            long l13 = arrl[11];
            long l14 = arrl[12];
            long l15 = arrl[13];
            long l16 = arrl[14];
            long l17 = arrl[15];
            long l18 = l2 + arrl3[0];
            long l19 = l3 + arrl3[1];
            long l20 = l4 + arrl3[2];
            long l21 = l5 + arrl3[3];
            long l22 = l6 + arrl3[4];
            long l23 = l7 + arrl3[5];
            long l24 = l8 + arrl3[6];
            long l25 = l9 + arrl3[7];
            long l26 = l10 + arrl3[8];
            long l27 = l11 + arrl3[9];
            long l28 = l12 + arrl3[10];
            long l29 = l13 + arrl3[11];
            long l30 = l14 + arrl3[12];
            long l31 = l15 + (arrl3[13] + arrl4[0]);
            long l32 = l16 + (arrl3[14] + arrl4[1]);
            long l33 = l17 + arrl3[15];
            for (int i2 = 1; i2 < 20; i2 += 2) {
                int n2 = arrn[i2];
                int n3 = arrn2[i2];
                long l34 = l18 + l19;
                long l35 = ThreefishEngine.rotlXor(l19, 24, l34);
                long l36 = l20 + l21;
                long l37 = ThreefishEngine.rotlXor(l21, 13, l36);
                long l38 = l22 + l23;
                long l39 = ThreefishEngine.rotlXor(l23, 8, l38);
                long l40 = l24 + l25;
                long l41 = ThreefishEngine.rotlXor(l25, 47, l40);
                long l42 = l26 + l27;
                long l43 = ThreefishEngine.rotlXor(l27, 8, l42);
                long l44 = l28 + l29;
                long l45 = ThreefishEngine.rotlXor(l29, 17, l44);
                long l46 = l30 + l31;
                long l47 = ThreefishEngine.rotlXor(l31, 22, l46);
                long l48 = l32 + l33;
                long l49 = ThreefishEngine.rotlXor(l33, 37, l48);
                long l50 = l34 + l43;
                long l51 = ThreefishEngine.rotlXor(l43, 38, l50);
                long l52 = l36 + l47;
                long l53 = ThreefishEngine.rotlXor(l47, 19, l52);
                long l54 = l40 + l45;
                long l55 = ThreefishEngine.rotlXor(l45, 10, l54);
                long l56 = l38 + l49;
                long l57 = ThreefishEngine.rotlXor(l49, 55, l56);
                long l58 = l44 + l41;
                long l59 = ThreefishEngine.rotlXor(l41, 49, l58);
                long l60 = l46 + l37;
                long l61 = ThreefishEngine.rotlXor(l37, 18, l60);
                long l62 = l48 + l39;
                long l63 = ThreefishEngine.rotlXor(l39, 23, l62);
                long l64 = l42 + l35;
                long l65 = ThreefishEngine.rotlXor(l35, 52, l64);
                long l66 = l50 + l59;
                long l67 = ThreefishEngine.rotlXor(l59, 33, l66);
                long l68 = l52 + l63;
                long l69 = ThreefishEngine.rotlXor(l63, 4, l68);
                long l70 = l56 + l61;
                long l71 = ThreefishEngine.rotlXor(l61, 51, l70);
                long l72 = l54 + l65;
                long l73 = ThreefishEngine.rotlXor(l65, 13, l72);
                long l74 = l60 + l57;
                long l75 = ThreefishEngine.rotlXor(l57, 34, l74);
                long l76 = l62 + l53;
                long l77 = ThreefishEngine.rotlXor(l53, 41, l76);
                long l78 = l64 + l55;
                long l79 = ThreefishEngine.rotlXor(l55, 59, l78);
                long l80 = l58 + l51;
                long l81 = ThreefishEngine.rotlXor(l51, 17, l80);
                long l82 = l66 + l75;
                long l83 = ThreefishEngine.rotlXor(l75, 5, l82);
                long l84 = l68 + l79;
                long l85 = ThreefishEngine.rotlXor(l79, 20, l84);
                long l86 = l72 + l77;
                long l87 = ThreefishEngine.rotlXor(l77, 48, l86);
                long l88 = l70 + l81;
                long l89 = ThreefishEngine.rotlXor(l81, 41, l88);
                long l90 = l76 + l73;
                long l91 = ThreefishEngine.rotlXor(l73, 47, l90);
                long l92 = l78 + l69;
                long l93 = ThreefishEngine.rotlXor(l69, 28, l92);
                long l94 = l80 + l71;
                long l95 = ThreefishEngine.rotlXor(l71, 16, l94);
                long l96 = l74 + l67;
                long l97 = ThreefishEngine.rotlXor(l67, 25, l96);
                long l98 = l82 + arrl3[n2];
                long l99 = l91 + arrl3[n2 + 1];
                long l100 = l84 + arrl3[n2 + 2];
                long l101 = l95 + arrl3[n2 + 3];
                long l102 = l88 + arrl3[n2 + 4];
                long l103 = l93 + arrl3[n2 + 5];
                long l104 = l86 + arrl3[n2 + 6];
                long l105 = l97 + arrl3[n2 + 7];
                long l106 = l92 + arrl3[n2 + 8];
                long l107 = l89 + arrl3[n2 + 9];
                long l108 = l94 + arrl3[n2 + 10];
                long l109 = l85 + arrl3[n2 + 11];
                long l110 = l96 + arrl3[n2 + 12];
                long l111 = l87 + (arrl3[n2 + 13] + arrl4[n3]);
                long l112 = l90 + (arrl3[n2 + 14] + arrl4[n3 + 1]);
                long l113 = l83 + (arrl3[n2 + 15] + (long)i2);
                long l114 = l98 + l99;
                long l115 = ThreefishEngine.rotlXor(l99, 41, l114);
                long l116 = l100 + l101;
                long l117 = ThreefishEngine.rotlXor(l101, 9, l116);
                long l118 = l102 + l103;
                long l119 = ThreefishEngine.rotlXor(l103, 37, l118);
                long l120 = l104 + l105;
                long l121 = ThreefishEngine.rotlXor(l105, 31, l120);
                long l122 = l106 + l107;
                long l123 = ThreefishEngine.rotlXor(l107, 12, l122);
                long l124 = l108 + l109;
                long l125 = ThreefishEngine.rotlXor(l109, 47, l124);
                long l126 = l110 + l111;
                long l127 = ThreefishEngine.rotlXor(l111, 44, l126);
                long l128 = l112 + l113;
                long l129 = ThreefishEngine.rotlXor(l113, 30, l128);
                long l130 = l114 + l123;
                long l131 = ThreefishEngine.rotlXor(l123, 16, l130);
                long l132 = l116 + l127;
                long l133 = ThreefishEngine.rotlXor(l127, 34, l132);
                long l134 = l120 + l125;
                long l135 = ThreefishEngine.rotlXor(l125, 56, l134);
                long l136 = l118 + l129;
                long l137 = ThreefishEngine.rotlXor(l129, 51, l136);
                long l138 = l124 + l121;
                long l139 = ThreefishEngine.rotlXor(l121, 4, l138);
                long l140 = l126 + l117;
                long l141 = ThreefishEngine.rotlXor(l117, 53, l140);
                long l142 = l128 + l119;
                long l143 = ThreefishEngine.rotlXor(l119, 42, l142);
                long l144 = l122 + l115;
                long l145 = ThreefishEngine.rotlXor(l115, 41, l144);
                long l146 = l130 + l139;
                long l147 = ThreefishEngine.rotlXor(l139, 31, l146);
                long l148 = l132 + l143;
                long l149 = ThreefishEngine.rotlXor(l143, 44, l148);
                long l150 = l136 + l141;
                long l151 = ThreefishEngine.rotlXor(l141, 47, l150);
                long l152 = l134 + l145;
                long l153 = ThreefishEngine.rotlXor(l145, 46, l152);
                long l154 = l140 + l137;
                long l155 = ThreefishEngine.rotlXor(l137, 19, l154);
                long l156 = l142 + l133;
                long l157 = ThreefishEngine.rotlXor(l133, 42, l156);
                long l158 = l144 + l135;
                long l159 = ThreefishEngine.rotlXor(l135, 44, l158);
                long l160 = l138 + l131;
                long l161 = ThreefishEngine.rotlXor(l131, 25, l160);
                long l162 = l146 + l155;
                long l163 = ThreefishEngine.rotlXor(l155, 9, l162);
                long l164 = l148 + l159;
                long l165 = ThreefishEngine.rotlXor(l159, 48, l164);
                long l166 = l152 + l157;
                long l167 = ThreefishEngine.rotlXor(l157, 35, l166);
                long l168 = l150 + l161;
                long l169 = ThreefishEngine.rotlXor(l161, 52, l168);
                long l170 = l156 + l153;
                long l171 = ThreefishEngine.rotlXor(l153, 23, l170);
                long l172 = l158 + l149;
                long l173 = ThreefishEngine.rotlXor(l149, 31, l172);
                long l174 = l160 + l151;
                long l175 = ThreefishEngine.rotlXor(l151, 37, l174);
                long l176 = l154 + l147;
                long l177 = ThreefishEngine.rotlXor(l147, 20, l176);
                l18 = l162 + arrl3[n2 + 1];
                l19 = l171 + arrl3[n2 + 2];
                l20 = l164 + arrl3[n2 + 3];
                l21 = l175 + arrl3[n2 + 4];
                l22 = l168 + arrl3[n2 + 5];
                l23 = l173 + arrl3[n2 + 6];
                l24 = l166 + arrl3[n2 + 7];
                l25 = l177 + arrl3[n2 + 8];
                l26 = l172 + arrl3[n2 + 9];
                l27 = l169 + arrl3[n2 + 10];
                l28 = l174 + arrl3[n2 + 11];
                l29 = l165 + arrl3[n2 + 12];
                l30 = l176 + arrl3[n2 + 13];
                l31 = l167 + (arrl3[n2 + 14] + arrl4[n3 + 1]);
                l32 = l170 + (arrl3[n2 + 15] + arrl4[n3 + 2]);
                l33 = l163 + (1L + (arrl3[n2 + 16] + (long)i2));
            }
            arrl2[0] = l18;
            arrl2[1] = l19;
            arrl2[2] = l20;
            arrl2[3] = l21;
            arrl2[4] = l22;
            arrl2[5] = l23;
            arrl2[6] = l24;
            arrl2[7] = l25;
            arrl2[8] = l26;
            arrl2[9] = l27;
            arrl2[10] = l28;
            arrl2[11] = l29;
            arrl2[12] = l30;
            arrl2[13] = l31;
            arrl2[14] = l32;
            arrl2[15] = l33;
        }
    }

    private static final class Threefish256Cipher
    extends ThreefishCipher {
        private static final int ROTATION_0_0 = 14;
        private static final int ROTATION_0_1 = 16;
        private static final int ROTATION_1_0 = 52;
        private static final int ROTATION_1_1 = 57;
        private static final int ROTATION_2_0 = 23;
        private static final int ROTATION_2_1 = 40;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 37;
        private static final int ROTATION_4_0 = 25;
        private static final int ROTATION_4_1 = 33;
        private static final int ROTATION_5_0 = 46;
        private static final int ROTATION_5_1 = 12;
        private static final int ROTATION_6_0 = 58;
        private static final int ROTATION_6_1 = 22;
        private static final int ROTATION_7_0 = 32;
        private static final int ROTATION_7_1 = 32;

        public Threefish256Cipher(long[] arrl, long[] arrl2) {
            super(arrl, arrl2);
        }

        @Override
        void decryptBlock(long[] arrl, long[] arrl2) {
            long[] arrl3 = this.kw;
            long[] arrl4 = this.t;
            int[] arrn = MOD5;
            int[] arrn2 = MOD3;
            if (arrl3.length != 9) {
                throw new IllegalArgumentException();
            }
            if (arrl4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = arrl[0];
            long l3 = arrl[1];
            long l4 = arrl[2];
            long l5 = arrl[3];
            for (int i2 = 17; i2 >= 1; i2 -= 2) {
                int n2 = arrn[i2];
                int n3 = arrn2[i2];
                long l6 = l2 - arrl3[n2 + 1];
                long l7 = l3 - (arrl3[n2 + 2] + arrl4[n3 + 1]);
                long l8 = l4 - (arrl3[n2 + 3] + arrl4[n3 + 2]);
                long l9 = ThreefishEngine.xorRotr(l5 - (1L + (arrl3[n2 + 4] + (long)i2)), 32, l6);
                long l10 = l6 - l9;
                long l11 = ThreefishEngine.xorRotr(l7, 32, l8);
                long l12 = l8 - l11;
                long l13 = ThreefishEngine.xorRotr(l11, 58, l10);
                long l14 = l10 - l13;
                long l15 = ThreefishEngine.xorRotr(l9, 22, l12);
                long l16 = l12 - l15;
                long l17 = ThreefishEngine.xorRotr(l15, 46, l14);
                long l18 = l14 - l17;
                long l19 = ThreefishEngine.xorRotr(l13, 12, l16);
                long l20 = l16 - l19;
                long l21 = ThreefishEngine.xorRotr(l19, 25, l18);
                long l22 = l18 - l21;
                long l23 = ThreefishEngine.xorRotr(l17, 33, l20);
                long l24 = l20 - l23;
                long l25 = l22 - arrl3[n2];
                long l26 = l21 - (arrl3[n2 + 1] + arrl4[n3]);
                long l27 = l24 - (arrl3[n2 + 2] + arrl4[n3 + 1]);
                long l28 = ThreefishEngine.xorRotr(l23 - (arrl3[n2 + 3] + (long)i2), 5, l25);
                long l29 = l25 - l28;
                long l30 = ThreefishEngine.xorRotr(l26, 37, l27);
                long l31 = l27 - l30;
                long l32 = ThreefishEngine.xorRotr(l30, 23, l29);
                long l33 = l29 - l32;
                long l34 = ThreefishEngine.xorRotr(l28, 40, l31);
                long l35 = l31 - l34;
                long l36 = ThreefishEngine.xorRotr(l34, 52, l33);
                long l37 = l33 - l36;
                long l38 = ThreefishEngine.xorRotr(l32, 57, l35);
                long l39 = l35 - l38;
                l3 = ThreefishEngine.xorRotr(l38, 14, l37);
                l2 = l37 - l3;
                l5 = ThreefishEngine.xorRotr(l36, 16, l39);
                l4 = l39 - l5;
            }
            long l40 = l2 - arrl3[0];
            long l41 = l3 - (arrl3[1] + arrl4[0]);
            long l42 = l4 - (arrl3[2] + arrl4[1]);
            long l43 = l5 - arrl3[3];
            arrl2[0] = l40;
            arrl2[1] = l41;
            arrl2[2] = l42;
            arrl2[3] = l43;
        }

        @Override
        void encryptBlock(long[] arrl, long[] arrl2) {
            long[] arrl3 = this.kw;
            long[] arrl4 = this.t;
            int[] arrn = MOD5;
            int[] arrn2 = MOD3;
            if (arrl3.length != 9) {
                throw new IllegalArgumentException();
            }
            if (arrl4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = arrl[0];
            long l3 = arrl[1];
            long l4 = arrl[2];
            long l5 = arrl[3];
            long l6 = l2 + arrl3[0];
            long l7 = l3 + (arrl3[1] + arrl4[0]);
            long l8 = l4 + (arrl3[2] + arrl4[1]);
            long l9 = l5 + arrl3[3];
            for (int i2 = 1; i2 < 18; i2 += 2) {
                int n2 = arrn[i2];
                int n3 = arrn2[i2];
                long l10 = l6 + l7;
                long l11 = ThreefishEngine.rotlXor(l7, 14, l10);
                long l12 = l8 + l9;
                long l13 = ThreefishEngine.rotlXor(l9, 16, l12);
                long l14 = l10 + l13;
                long l15 = ThreefishEngine.rotlXor(l13, 52, l14);
                long l16 = l12 + l11;
                long l17 = ThreefishEngine.rotlXor(l11, 57, l16);
                long l18 = l14 + l17;
                long l19 = ThreefishEngine.rotlXor(l17, 23, l18);
                long l20 = l16 + l15;
                long l21 = ThreefishEngine.rotlXor(l15, 40, l20);
                long l22 = l18 + l21;
                long l23 = ThreefishEngine.rotlXor(l21, 5, l22);
                long l24 = l20 + l19;
                long l25 = ThreefishEngine.rotlXor(l19, 37, l24);
                long l26 = l22 + arrl3[n2];
                long l27 = l25 + (arrl3[n2 + 1] + arrl4[n3]);
                long l28 = l24 + (arrl3[n2 + 2] + arrl4[n3 + 1]);
                long l29 = l23 + (arrl3[n2 + 3] + (long)i2);
                long l30 = l26 + l27;
                long l31 = ThreefishEngine.rotlXor(l27, 25, l30);
                long l32 = l28 + l29;
                long l33 = ThreefishEngine.rotlXor(l29, 33, l32);
                long l34 = l30 + l33;
                long l35 = ThreefishEngine.rotlXor(l33, 46, l34);
                long l36 = l32 + l31;
                long l37 = ThreefishEngine.rotlXor(l31, 12, l36);
                long l38 = l34 + l37;
                long l39 = ThreefishEngine.rotlXor(l37, 58, l38);
                long l40 = l36 + l35;
                long l41 = ThreefishEngine.rotlXor(l35, 22, l40);
                long l42 = l38 + l41;
                long l43 = ThreefishEngine.rotlXor(l41, 32, l42);
                long l44 = l40 + l39;
                long l45 = ThreefishEngine.rotlXor(l39, 32, l44);
                l6 = l42 + arrl3[n2 + 1];
                l7 = l45 + (arrl3[n2 + 2] + arrl4[n3 + 1]);
                l8 = l44 + (arrl3[n2 + 3] + arrl4[n3 + 2]);
                l9 = l43 + (1L + (arrl3[n2 + 4] + (long)i2));
            }
            arrl2[0] = l6;
            arrl2[1] = l7;
            arrl2[2] = l8;
            arrl2[3] = l9;
        }
    }

    private static final class Threefish512Cipher
    extends ThreefishCipher {
        private static final int ROTATION_0_0 = 46;
        private static final int ROTATION_0_1 = 36;
        private static final int ROTATION_0_2 = 19;
        private static final int ROTATION_0_3 = 37;
        private static final int ROTATION_1_0 = 33;
        private static final int ROTATION_1_1 = 27;
        private static final int ROTATION_1_2 = 14;
        private static final int ROTATION_1_3 = 42;
        private static final int ROTATION_2_0 = 17;
        private static final int ROTATION_2_1 = 49;
        private static final int ROTATION_2_2 = 36;
        private static final int ROTATION_2_3 = 39;
        private static final int ROTATION_3_0 = 44;
        private static final int ROTATION_3_1 = 9;
        private static final int ROTATION_3_2 = 54;
        private static final int ROTATION_3_3 = 56;
        private static final int ROTATION_4_0 = 39;
        private static final int ROTATION_4_1 = 30;
        private static final int ROTATION_4_2 = 34;
        private static final int ROTATION_4_3 = 24;
        private static final int ROTATION_5_0 = 13;
        private static final int ROTATION_5_1 = 50;
        private static final int ROTATION_5_2 = 10;
        private static final int ROTATION_5_3 = 17;
        private static final int ROTATION_6_0 = 25;
        private static final int ROTATION_6_1 = 29;
        private static final int ROTATION_6_2 = 39;
        private static final int ROTATION_6_3 = 43;
        private static final int ROTATION_7_0 = 8;
        private static final int ROTATION_7_1 = 35;
        private static final int ROTATION_7_2 = 56;
        private static final int ROTATION_7_3 = 22;

        protected Threefish512Cipher(long[] arrl, long[] arrl2) {
            super(arrl, arrl2);
        }

        @Override
        public void decryptBlock(long[] arrl, long[] arrl2) {
            long[] arrl3 = this.kw;
            long[] arrl4 = this.t;
            int[] arrn = MOD9;
            int[] arrn2 = MOD3;
            if (arrl3.length != 17) {
                throw new IllegalArgumentException();
            }
            if (arrl4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = arrl[0];
            long l3 = arrl[1];
            long l4 = arrl[2];
            long l5 = arrl[3];
            long l6 = arrl[4];
            long l7 = arrl[5];
            long l8 = arrl[6];
            long l9 = arrl[7];
            for (int i2 = 17; i2 >= 1; i2 -= 2) {
                int n2 = arrn[i2];
                int n3 = arrn2[i2];
                long l10 = l2 - arrl3[n2 + 1];
                long l11 = l3 - arrl3[n2 + 2];
                long l12 = l4 - arrl3[n2 + 3];
                long l13 = l5 - arrl3[n2 + 4];
                long l14 = l6 - arrl3[n2 + 5];
                long l15 = l7 - (arrl3[n2 + 6] + arrl4[n3 + 1]);
                long l16 = l8 - (arrl3[n2 + 7] + arrl4[n3 + 2]);
                long l17 = l9 - (1L + (arrl3[n2 + 8] + (long)i2));
                long l18 = ThreefishEngine.xorRotr(l11, 8, l16);
                long l19 = l16 - l18;
                long l20 = ThreefishEngine.xorRotr(l17, 35, l10);
                long l21 = l10 - l20;
                long l22 = ThreefishEngine.xorRotr(l15, 56, l12);
                long l23 = l12 - l22;
                long l24 = ThreefishEngine.xorRotr(l13, 22, l14);
                long l25 = l14 - l24;
                long l26 = ThreefishEngine.xorRotr(l18, 25, l25);
                long l27 = l25 - l26;
                long l28 = ThreefishEngine.xorRotr(l24, 29, l19);
                long l29 = l19 - l28;
                long l30 = ThreefishEngine.xorRotr(l22, 39, l21);
                long l31 = l21 - l30;
                long l32 = ThreefishEngine.xorRotr(l20, 43, l23);
                long l33 = l23 - l32;
                long l34 = ThreefishEngine.xorRotr(l26, 13, l33);
                long l35 = l33 - l34;
                long l36 = ThreefishEngine.xorRotr(l32, 50, l27);
                long l37 = l27 - l36;
                long l38 = ThreefishEngine.xorRotr(l30, 10, l29);
                long l39 = l29 - l38;
                long l40 = ThreefishEngine.xorRotr(l28, 17, l31);
                long l41 = l31 - l40;
                long l42 = ThreefishEngine.xorRotr(l34, 39, l41);
                long l43 = l41 - l42;
                long l44 = ThreefishEngine.xorRotr(l40, 30, l35);
                long l45 = l35 - l44;
                long l46 = ThreefishEngine.xorRotr(l38, 34, l37);
                long l47 = l37 - l46;
                long l48 = ThreefishEngine.xorRotr(l36, 24, l39);
                long l49 = l39 - l48;
                long l50 = l43 - arrl3[n2];
                long l51 = l42 - arrl3[n2 + 1];
                long l52 = l45 - arrl3[n2 + 2];
                long l53 = l44 - arrl3[n2 + 3];
                long l54 = l47 - arrl3[n2 + 4];
                long l55 = l46 - (arrl3[n2 + 5] + arrl4[n3]);
                long l56 = l49 - (arrl3[n2 + 6] + arrl4[n3 + 1]);
                long l57 = l48 - (arrl3[n2 + 7] + (long)i2);
                long l58 = ThreefishEngine.xorRotr(l51, 44, l56);
                long l59 = l56 - l58;
                long l60 = ThreefishEngine.xorRotr(l57, 9, l50);
                long l61 = l50 - l60;
                long l62 = ThreefishEngine.xorRotr(l55, 54, l52);
                long l63 = l52 - l62;
                long l64 = ThreefishEngine.xorRotr(l53, 56, l54);
                long l65 = l54 - l64;
                long l66 = ThreefishEngine.xorRotr(l58, 17, l65);
                long l67 = l65 - l66;
                long l68 = ThreefishEngine.xorRotr(l64, 49, l59);
                long l69 = l59 - l68;
                long l70 = ThreefishEngine.xorRotr(l62, 36, l61);
                long l71 = l61 - l70;
                long l72 = ThreefishEngine.xorRotr(l60, 39, l63);
                long l73 = l63 - l72;
                long l74 = ThreefishEngine.xorRotr(l66, 33, l73);
                long l75 = l73 - l74;
                long l76 = ThreefishEngine.xorRotr(l72, 27, l67);
                long l77 = l67 - l76;
                long l78 = ThreefishEngine.xorRotr(l70, 14, l69);
                long l79 = l69 - l78;
                long l80 = ThreefishEngine.xorRotr(l68, 42, l71);
                long l81 = l71 - l80;
                l3 = ThreefishEngine.xorRotr(l74, 46, l81);
                l2 = l81 - l3;
                l5 = ThreefishEngine.xorRotr(l80, 36, l75);
                l4 = l75 - l5;
                l7 = ThreefishEngine.xorRotr(l78, 19, l77);
                l6 = l77 - l7;
                l9 = ThreefishEngine.xorRotr(l76, 37, l79);
                l8 = l79 - l9;
            }
            long l82 = l2 - arrl3[0];
            long l83 = l3 - arrl3[1];
            long l84 = l4 - arrl3[2];
            long l85 = l5 - arrl3[3];
            long l86 = l6 - arrl3[4];
            long l87 = l7 - (arrl3[5] + arrl4[0]);
            long l88 = l8 - (arrl3[6] + arrl4[1]);
            long l89 = l9 - arrl3[7];
            arrl2[0] = l82;
            arrl2[1] = l83;
            arrl2[2] = l84;
            arrl2[3] = l85;
            arrl2[4] = l86;
            arrl2[5] = l87;
            arrl2[6] = l88;
            arrl2[7] = l89;
        }

        @Override
        public void encryptBlock(long[] arrl, long[] arrl2) {
            long[] arrl3 = this.kw;
            long[] arrl4 = this.t;
            int[] arrn = MOD9;
            int[] arrn2 = MOD3;
            if (arrl3.length != 17) {
                throw new IllegalArgumentException();
            }
            if (arrl4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = arrl[0];
            long l3 = arrl[1];
            long l4 = arrl[2];
            long l5 = arrl[3];
            long l6 = arrl[4];
            long l7 = arrl[5];
            long l8 = arrl[6];
            long l9 = arrl[7];
            long l10 = l2 + arrl3[0];
            long l11 = l3 + arrl3[1];
            long l12 = l4 + arrl3[2];
            long l13 = l5 + arrl3[3];
            long l14 = l6 + arrl3[4];
            long l15 = l7 + (arrl3[5] + arrl4[0]);
            long l16 = l8 + (arrl3[6] + arrl4[1]);
            long l17 = l9 + arrl3[7];
            for (int i2 = 1; i2 < 18; i2 += 2) {
                int n2 = arrn[i2];
                int n3 = arrn2[i2];
                long l18 = l10 + l11;
                long l19 = ThreefishEngine.rotlXor(l11, 46, l18);
                long l20 = l12 + l13;
                long l21 = ThreefishEngine.rotlXor(l13, 36, l20);
                long l22 = l14 + l15;
                long l23 = ThreefishEngine.rotlXor(l15, 19, l22);
                long l24 = l16 + l17;
                long l25 = ThreefishEngine.rotlXor(l17, 37, l24);
                long l26 = l20 + l19;
                long l27 = ThreefishEngine.rotlXor(l19, 33, l26);
                long l28 = l22 + l25;
                long l29 = ThreefishEngine.rotlXor(l25, 27, l28);
                long l30 = l24 + l23;
                long l31 = ThreefishEngine.rotlXor(l23, 14, l30);
                long l32 = l18 + l21;
                long l33 = ThreefishEngine.rotlXor(l21, 42, l32);
                long l34 = l28 + l27;
                long l35 = ThreefishEngine.rotlXor(l27, 17, l34);
                long l36 = l30 + l33;
                long l37 = ThreefishEngine.rotlXor(l33, 49, l36);
                long l38 = l32 + l31;
                long l39 = ThreefishEngine.rotlXor(l31, 36, l38);
                long l40 = l26 + l29;
                long l41 = ThreefishEngine.rotlXor(l29, 39, l40);
                long l42 = l36 + l35;
                long l43 = ThreefishEngine.rotlXor(l35, 44, l42);
                long l44 = l38 + l41;
                long l45 = ThreefishEngine.rotlXor(l41, 9, l44);
                long l46 = l40 + l39;
                long l47 = ThreefishEngine.rotlXor(l39, 54, l46);
                long l48 = l34 + l37;
                long l49 = ThreefishEngine.rotlXor(l37, 56, l48);
                long l50 = l44 + arrl3[n2];
                long l51 = l43 + arrl3[n2 + 1];
                long l52 = l46 + arrl3[n2 + 2];
                long l53 = l49 + arrl3[n2 + 3];
                long l54 = l48 + arrl3[n2 + 4];
                long l55 = l47 + (arrl3[n2 + 5] + arrl4[n3]);
                long l56 = l42 + (arrl3[n2 + 6] + arrl4[n3 + 1]);
                long l57 = l45 + (arrl3[n2 + 7] + (long)i2);
                long l58 = l50 + l51;
                long l59 = ThreefishEngine.rotlXor(l51, 39, l58);
                long l60 = l52 + l53;
                long l61 = ThreefishEngine.rotlXor(l53, 30, l60);
                long l62 = l54 + l55;
                long l63 = ThreefishEngine.rotlXor(l55, 34, l62);
                long l64 = l56 + l57;
                long l65 = ThreefishEngine.rotlXor(l57, 24, l64);
                long l66 = l60 + l59;
                long l67 = ThreefishEngine.rotlXor(l59, 13, l66);
                long l68 = l62 + l65;
                long l69 = ThreefishEngine.rotlXor(l65, 50, l68);
                long l70 = l64 + l63;
                long l71 = ThreefishEngine.rotlXor(l63, 10, l70);
                long l72 = l58 + l61;
                long l73 = ThreefishEngine.rotlXor(l61, 17, l72);
                long l74 = l68 + l67;
                long l75 = ThreefishEngine.rotlXor(l67, 25, l74);
                long l76 = l70 + l73;
                long l77 = ThreefishEngine.rotlXor(l73, 29, l76);
                long l78 = l72 + l71;
                long l79 = ThreefishEngine.rotlXor(l71, 39, l78);
                long l80 = l66 + l69;
                long l81 = ThreefishEngine.rotlXor(l69, 43, l80);
                long l82 = l76 + l75;
                long l83 = ThreefishEngine.rotlXor(l75, 8, l82);
                long l84 = l78 + l81;
                long l85 = ThreefishEngine.rotlXor(l81, 35, l84);
                long l86 = l80 + l79;
                long l87 = ThreefishEngine.rotlXor(l79, 56, l86);
                long l88 = l74 + l77;
                long l89 = ThreefishEngine.rotlXor(l77, 22, l88);
                l10 = l84 + arrl3[n2 + 1];
                l11 = l83 + arrl3[n2 + 2];
                l12 = l86 + arrl3[n2 + 3];
                l13 = l89 + arrl3[n2 + 4];
                l14 = l88 + arrl3[n2 + 5];
                l15 = l87 + (arrl3[n2 + 6] + arrl4[n3 + 1]);
                l16 = l82 + (arrl3[n2 + 7] + arrl4[n3 + 2]);
                l17 = l85 + (1L + (arrl3[n2 + 8] + (long)i2));
            }
            arrl2[0] = l10;
            arrl2[1] = l11;
            arrl2[2] = l12;
            arrl2[3] = l13;
            arrl2[4] = l14;
            arrl2[5] = l15;
            arrl2[6] = l16;
            arrl2[7] = l17;
        }
    }

    private static abstract class ThreefishCipher {
        protected final long[] kw;
        protected final long[] t;

        protected ThreefishCipher(long[] arrl, long[] arrl2) {
            this.kw = arrl;
            this.t = arrl2;
        }

        abstract void decryptBlock(long[] var1, long[] var2);

        abstract void encryptBlock(long[] var1, long[] var2);
    }

}

