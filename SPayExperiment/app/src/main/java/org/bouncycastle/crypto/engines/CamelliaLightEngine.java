/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class CamelliaLightEngine
implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final int MASK8 = 255;
    private static final byte[] SBOX1;
    private static final int[] SIGMA;
    private boolean _keyis128;
    private boolean initialized;
    private int[] ke = new int[12];
    private int[] kw = new int[8];
    private int[] state = new int[4];
    private int[] subkey = new int[96];

    static {
        SIGMA = new int[]{-1600231809, 1003262091, -1233459112, 1286239154, -957401297, -380665154, 1426019237, -237801700, 283453434, -563598051, -1336506174, -1276722691};
        SBOX1 = new byte[]{112, -126, 44, -20, -77, 39, -64, -27, -28, -123, 87, 53, -22, 12, -82, 65, 35, -17, 107, -109, 69, 25, -91, 33, -19, 14, 79, 78, 29, 101, -110, -67, -122, -72, -81, -113, 124, -21, 31, -50, 62, 48, -36, 95, 94, -59, 11, 26, -90, -31, 57, -54, -43, 71, 93, 61, -39, 1, 90, -42, 81, 86, 108, 77, -117, 13, -102, 102, -5, -52, -80, 45, 116, 18, 43, 32, -16, -79, -124, -103, -33, 76, -53, -62, 52, 126, 118, 5, 109, -73, -87, 49, -47, 23, 4, -41, 20, 88, 58, 97, -34, 27, 17, 28, 50, 15, -100, 22, 83, 24, -14, 34, -2, 68, -49, -78, -61, -75, 122, -111, 36, 8, -24, -88, 96, -4, 105, 80, -86, -48, -96, 125, -95, -119, 98, -105, 84, 91, 30, -107, -32, -1, 100, -46, 16, -60, 0, 72, -93, -9, 117, -37, -118, 3, -26, -38, 9, 63, -35, -108, -121, 92, -125, 2, -51, 74, -112, 51, 115, 103, -10, -13, -99, 127, -65, -30, 82, -101, -40, 38, -56, 55, -58, 59, -127, -106, 111, 75, 19, -66, 99, 46, -23, 121, -89, -116, -97, 110, -68, -114, 41, -11, -7, -74, 47, -3, -76, 89, 120, -104, 6, 106, -25, 70, 113, -70, -44, 37, -85, 66, -120, -94, -115, -6, 114, 7, -71, 85, -8, -18, -84, 10, 54, 73, 42, 104, 60, 56, -15, -92, 64, 40, -45, 123, -69, -55, 67, -63, 21, -29, -83, -12, 119, -57, -128, -98};
    }

    private int bytes2int(byte[] arrby, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < 4; ++i2) {
            n3 = (n3 << 8) + (255 & arrby[i2 + n2]);
        }
        return n3;
    }

    private void camelliaF2(int[] arrn, int[] arrn2, int n2) {
        int n3 = arrn[0] ^ arrn2[n2 + 0];
        int n4 = this.sbox4(n3 & 255) | this.sbox3(255 & n3 >>> 8) << 8 | this.sbox2(255 & n3 >>> 16) << 16 | (255 & SBOX1[255 & n3 >>> 24]) << 24;
        int n5 = arrn[1] ^ arrn2[n2 + 1];
        int n6 = CamelliaLightEngine.leftRotate(255 & SBOX1[n5 & 255] | this.sbox4(255 & n5 >>> 8) << 8 | this.sbox3(255 & n5 >>> 16) << 16 | this.sbox2(255 & n5 >>> 24) << 24, 8);
        int n7 = n4 ^ n6;
        int n8 = n7 ^ CamelliaLightEngine.leftRotate(n6, 8);
        int n9 = n8 ^ CamelliaLightEngine.rightRotate(n7, 8);
        arrn[2] = arrn[2] ^ (n9 ^ CamelliaLightEngine.leftRotate(n8, 16));
        arrn[3] = arrn[3] ^ CamelliaLightEngine.leftRotate(n9, 8);
        int n10 = arrn[2] ^ arrn2[n2 + 2];
        int n11 = this.sbox4(n10 & 255) | this.sbox3(255 & n10 >>> 8) << 8 | this.sbox2(255 & n10 >>> 16) << 16 | (255 & SBOX1[255 & n10 >>> 24]) << 24;
        int n12 = arrn[3] ^ arrn2[n2 + 3];
        int n13 = CamelliaLightEngine.leftRotate(255 & SBOX1[n12 & 255] | this.sbox4(255 & n12 >>> 8) << 8 | this.sbox3(255 & n12 >>> 16) << 16 | this.sbox2(255 & n12 >>> 24) << 24, 8);
        int n14 = n11 ^ n13;
        int n15 = n14 ^ CamelliaLightEngine.leftRotate(n13, 8);
        int n16 = n15 ^ CamelliaLightEngine.rightRotate(n14, 8);
        arrn[0] = arrn[0] ^ (n16 ^ CamelliaLightEngine.leftRotate(n15, 16));
        arrn[1] = arrn[1] ^ CamelliaLightEngine.leftRotate(n16, 8);
    }

    private void camelliaFLs(int[] arrn, int[] arrn2, int n2) {
        arrn[1] = arrn[1] ^ CamelliaLightEngine.leftRotate(arrn[0] & arrn2[n2 + 0], 1);
        arrn[0] = arrn[0] ^ (arrn2[n2 + 1] | arrn[1]);
        arrn[2] = arrn[2] ^ (arrn2[n2 + 3] | arrn[3]);
        arrn[3] = arrn[3] ^ CamelliaLightEngine.leftRotate(arrn2[n2 + 2] & arrn[2], 1);
    }

    private static void decroldq(int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        arrn2[n4 + 2] = arrn[n3 + 0] << n2 | arrn[n3 + 1] >>> 32 - n2;
        arrn2[n4 + 3] = arrn[n3 + 1] << n2 | arrn[n3 + 2] >>> 32 - n2;
        arrn2[n4 + 0] = arrn[n3 + 2] << n2 | arrn[n3 + 3] >>> 32 - n2;
        arrn2[n4 + 1] = arrn[n3 + 3] << n2 | arrn[n3 + 0] >>> 32 - n2;
        arrn[n3 + 0] = arrn2[n4 + 2];
        arrn[n3 + 1] = arrn2[n4 + 3];
        arrn[n3 + 2] = arrn2[n4 + 0];
        arrn[n3 + 3] = arrn2[n4 + 1];
    }

    private static void decroldqo32(int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        arrn2[n4 + 2] = arrn[n3 + 1] << n2 - 32 | arrn[n3 + 2] >>> 64 - n2;
        arrn2[n4 + 3] = arrn[n3 + 2] << n2 - 32 | arrn[n3 + 3] >>> 64 - n2;
        arrn2[n4 + 0] = arrn[n3 + 3] << n2 - 32 | arrn[n3 + 0] >>> 64 - n2;
        arrn2[n4 + 1] = arrn[n3 + 0] << n2 - 32 | arrn[n3 + 1] >>> 64 - n2;
        arrn[n3 + 0] = arrn2[n4 + 2];
        arrn[n3 + 1] = arrn2[n4 + 3];
        arrn[n3 + 2] = arrn2[n4 + 0];
        arrn[n3 + 3] = arrn2[n4 + 1];
    }

    private void int2bytes(int n2, byte[] arrby, int n3) {
        for (int i2 = 0; i2 < 4; ++i2) {
            arrby[n3 + (3 - i2)] = (byte)n2;
            n2 >>>= 8;
        }
    }

    private byte lRot8(byte by, int n2) {
        return (byte)(by << n2 | (by & 255) >>> 8 - n2);
    }

    private static int leftRotate(int n2, int n3) {
        return (n2 << n3) + (n2 >>> 32 - n3);
    }

    private int processBlock128(byte[] arrby, int n2, byte[] arrby2, int n3) {
        for (int i2 = 0; i2 < 4; ++i2) {
            this.state[i2] = this.bytes2int(arrby, n2 + i2 * 4);
            int[] arrn = this.state;
            arrn[i2] = arrn[i2] ^ this.kw[i2];
        }
        this.camelliaF2(this.state, this.subkey, 0);
        this.camelliaF2(this.state, this.subkey, 4);
        this.camelliaF2(this.state, this.subkey, 8);
        this.camelliaFLs(this.state, this.ke, 0);
        this.camelliaF2(this.state, this.subkey, 12);
        this.camelliaF2(this.state, this.subkey, 16);
        this.camelliaF2(this.state, this.subkey, 20);
        this.camelliaFLs(this.state, this.ke, 4);
        this.camelliaF2(this.state, this.subkey, 24);
        this.camelliaF2(this.state, this.subkey, 28);
        this.camelliaF2(this.state, this.subkey, 32);
        int[] arrn = this.state;
        arrn[2] = arrn[2] ^ this.kw[4];
        int[] arrn2 = this.state;
        arrn2[3] = arrn2[3] ^ this.kw[5];
        int[] arrn3 = this.state;
        arrn3[0] = arrn3[0] ^ this.kw[6];
        int[] arrn4 = this.state;
        arrn4[1] = arrn4[1] ^ this.kw[7];
        this.int2bytes(this.state[2], arrby2, n3);
        this.int2bytes(this.state[3], arrby2, n3 + 4);
        this.int2bytes(this.state[0], arrby2, n3 + 8);
        this.int2bytes(this.state[1], arrby2, n3 + 12);
        return 16;
    }

    private int processBlock192or256(byte[] arrby, int n2, byte[] arrby2, int n3) {
        for (int i2 = 0; i2 < 4; ++i2) {
            this.state[i2] = this.bytes2int(arrby, n2 + i2 * 4);
            int[] arrn = this.state;
            arrn[i2] = arrn[i2] ^ this.kw[i2];
        }
        this.camelliaF2(this.state, this.subkey, 0);
        this.camelliaF2(this.state, this.subkey, 4);
        this.camelliaF2(this.state, this.subkey, 8);
        this.camelliaFLs(this.state, this.ke, 0);
        this.camelliaF2(this.state, this.subkey, 12);
        this.camelliaF2(this.state, this.subkey, 16);
        this.camelliaF2(this.state, this.subkey, 20);
        this.camelliaFLs(this.state, this.ke, 4);
        this.camelliaF2(this.state, this.subkey, 24);
        this.camelliaF2(this.state, this.subkey, 28);
        this.camelliaF2(this.state, this.subkey, 32);
        this.camelliaFLs(this.state, this.ke, 8);
        this.camelliaF2(this.state, this.subkey, 36);
        this.camelliaF2(this.state, this.subkey, 40);
        this.camelliaF2(this.state, this.subkey, 44);
        int[] arrn = this.state;
        arrn[2] = arrn[2] ^ this.kw[4];
        int[] arrn2 = this.state;
        arrn2[3] = arrn2[3] ^ this.kw[5];
        int[] arrn3 = this.state;
        arrn3[0] = arrn3[0] ^ this.kw[6];
        int[] arrn4 = this.state;
        arrn4[1] = arrn4[1] ^ this.kw[7];
        this.int2bytes(this.state[2], arrby2, n3);
        this.int2bytes(this.state[3], arrby2, n3 + 4);
        this.int2bytes(this.state[0], arrby2, n3 + 8);
        this.int2bytes(this.state[1], arrby2, n3 + 12);
        return 16;
    }

    private static int rightRotate(int n2, int n3) {
        return (n2 >>> n3) + (n2 << 32 - n3);
    }

    private static void roldq(int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        arrn2[n4 + 0] = arrn[n3 + 0] << n2 | arrn[n3 + 1] >>> 32 - n2;
        arrn2[n4 + 1] = arrn[n3 + 1] << n2 | arrn[n3 + 2] >>> 32 - n2;
        arrn2[n4 + 2] = arrn[n3 + 2] << n2 | arrn[n3 + 3] >>> 32 - n2;
        arrn2[n4 + 3] = arrn[n3 + 3] << n2 | arrn[n3 + 0] >>> 32 - n2;
        arrn[n3 + 0] = arrn2[n4 + 0];
        arrn[n3 + 1] = arrn2[n4 + 1];
        arrn[n3 + 2] = arrn2[n4 + 2];
        arrn[n3 + 3] = arrn2[n4 + 3];
    }

    private static void roldqo32(int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        arrn2[n4 + 0] = arrn[n3 + 1] << n2 - 32 | arrn[n3 + 2] >>> 64 - n2;
        arrn2[n4 + 1] = arrn[n3 + 2] << n2 - 32 | arrn[n3 + 3] >>> 64 - n2;
        arrn2[n4 + 2] = arrn[n3 + 3] << n2 - 32 | arrn[n3 + 0] >>> 64 - n2;
        arrn2[n4 + 3] = arrn[n3 + 0] << n2 - 32 | arrn[n3 + 1] >>> 64 - n2;
        arrn[n3 + 0] = arrn2[n4 + 0];
        arrn[n3 + 1] = arrn2[n4 + 1];
        arrn[n3 + 2] = arrn2[n4 + 2];
        arrn[n3 + 3] = arrn2[n4 + 3];
    }

    private int sbox2(int n2) {
        return 255 & this.lRot8(SBOX1[n2], 1);
    }

    private int sbox3(int n2) {
        return 255 & this.lRot8(SBOX1[n2], 7);
    }

    private int sbox4(int n2) {
        return 255 & SBOX1[255 & this.lRot8((byte)n2, 1)];
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setKey(boolean bl, byte[] arrby) {
        int[] arrn = new int[8];
        int[] arrn2 = new int[4];
        int[] arrn3 = new int[4];
        int[] arrn4 = new int[4];
        switch (arrby.length) {
            default: {
                throw new IllegalArgumentException("key sizes are only 16/24/32 bytes.");
            }
            case 16: {
                this._keyis128 = true;
                arrn[0] = this.bytes2int(arrby, 0);
                arrn[1] = this.bytes2int(arrby, 4);
                arrn[2] = this.bytes2int(arrby, 8);
                arrn[3] = this.bytes2int(arrby, 12);
                arrn[7] = 0;
                arrn[6] = 0;
                arrn[5] = 0;
                arrn[4] = 0;
                break;
            }
            case 24: {
                arrn[0] = this.bytes2int(arrby, 0);
                arrn[1] = this.bytes2int(arrby, 4);
                arrn[2] = this.bytes2int(arrby, 8);
                arrn[3] = this.bytes2int(arrby, 12);
                arrn[4] = this.bytes2int(arrby, 16);
                arrn[5] = this.bytes2int(arrby, 20);
                arrn[6] = -1 ^ arrn[4];
                arrn[7] = -1 ^ arrn[5];
                this._keyis128 = false;
                break;
            }
            case 32: {
                arrn[0] = this.bytes2int(arrby, 0);
                arrn[1] = this.bytes2int(arrby, 4);
                arrn[2] = this.bytes2int(arrby, 8);
                arrn[3] = this.bytes2int(arrby, 12);
                arrn[4] = this.bytes2int(arrby, 16);
                arrn[5] = this.bytes2int(arrby, 20);
                arrn[6] = this.bytes2int(arrby, 24);
                arrn[7] = this.bytes2int(arrby, 28);
                this._keyis128 = false;
            }
        }
        for (int i2 = 0; i2 < 4; ++i2) {
            arrn2[i2] = arrn[i2] ^ arrn[i2 + 4];
        }
        this.camelliaF2(arrn2, SIGMA, 0);
        for (int i3 = 0; i3 < 4; ++i3) {
            arrn2[i3] = arrn2[i3] ^ arrn[i3];
        }
        this.camelliaF2(arrn2, SIGMA, 4);
        if (this._keyis128) {
            if (bl) {
                this.kw[0] = arrn[0];
                this.kw[1] = arrn[1];
                this.kw[2] = arrn[2];
                this.kw[3] = arrn[3];
                CamelliaLightEngine.roldq(15, arrn, 0, this.subkey, 4);
                CamelliaLightEngine.roldq(30, arrn, 0, this.subkey, 12);
                CamelliaLightEngine.roldq(15, arrn, 0, arrn4, 0);
                this.subkey[18] = arrn4[2];
                this.subkey[19] = arrn4[3];
                CamelliaLightEngine.roldq(17, arrn, 0, this.ke, 4);
                CamelliaLightEngine.roldq(17, arrn, 0, this.subkey, 24);
                CamelliaLightEngine.roldq(17, arrn, 0, this.subkey, 32);
                this.subkey[0] = arrn2[0];
                this.subkey[1] = arrn2[1];
                this.subkey[2] = arrn2[2];
                this.subkey[3] = arrn2[3];
                CamelliaLightEngine.roldq(15, arrn2, 0, this.subkey, 8);
                CamelliaLightEngine.roldq(15, arrn2, 0, this.ke, 0);
                CamelliaLightEngine.roldq(15, arrn2, 0, arrn4, 0);
                this.subkey[16] = arrn4[0];
                this.subkey[17] = arrn4[1];
                CamelliaLightEngine.roldq(15, arrn2, 0, this.subkey, 20);
                CamelliaLightEngine.roldqo32(34, arrn2, 0, this.subkey, 28);
                CamelliaLightEngine.roldq(17, arrn2, 0, this.kw, 4);
                return;
            }
            this.kw[4] = arrn[0];
            this.kw[5] = arrn[1];
            this.kw[6] = arrn[2];
            this.kw[7] = arrn[3];
            CamelliaLightEngine.decroldq(15, arrn, 0, this.subkey, 28);
            CamelliaLightEngine.decroldq(30, arrn, 0, this.subkey, 20);
            CamelliaLightEngine.decroldq(15, arrn, 0, arrn4, 0);
            this.subkey[16] = arrn4[0];
            this.subkey[17] = arrn4[1];
            CamelliaLightEngine.decroldq(17, arrn, 0, this.ke, 0);
            CamelliaLightEngine.decroldq(17, arrn, 0, this.subkey, 8);
            CamelliaLightEngine.decroldq(17, arrn, 0, this.subkey, 0);
            this.subkey[34] = arrn2[0];
            this.subkey[35] = arrn2[1];
            this.subkey[32] = arrn2[2];
            this.subkey[33] = arrn2[3];
            CamelliaLightEngine.decroldq(15, arrn2, 0, this.subkey, 24);
            CamelliaLightEngine.decroldq(15, arrn2, 0, this.ke, 4);
            CamelliaLightEngine.decroldq(15, arrn2, 0, arrn4, 0);
            this.subkey[18] = arrn4[2];
            this.subkey[19] = arrn4[3];
            CamelliaLightEngine.decroldq(15, arrn2, 0, this.subkey, 12);
            CamelliaLightEngine.decroldqo32(34, arrn2, 0, this.subkey, 4);
            CamelliaLightEngine.roldq(17, arrn2, 0, this.kw, 0);
            return;
        }
        for (int i4 = 0; i4 < 4; ++i4) {
            arrn3[i4] = arrn2[i4] ^ arrn[i4 + 4];
        }
        this.camelliaF2(arrn3, SIGMA, 8);
        if (bl) {
            this.kw[0] = arrn[0];
            this.kw[1] = arrn[1];
            this.kw[2] = arrn[2];
            this.kw[3] = arrn[3];
            CamelliaLightEngine.roldqo32(45, arrn, 0, this.subkey, 16);
            CamelliaLightEngine.roldq(15, arrn, 0, this.ke, 4);
            CamelliaLightEngine.roldq(17, arrn, 0, this.subkey, 32);
            CamelliaLightEngine.roldqo32(34, arrn, 0, this.subkey, 44);
            CamelliaLightEngine.roldq(15, arrn, 4, this.subkey, 4);
            CamelliaLightEngine.roldq(15, arrn, 4, this.ke, 0);
            CamelliaLightEngine.roldq(30, arrn, 4, this.subkey, 24);
            CamelliaLightEngine.roldqo32(34, arrn, 4, this.subkey, 36);
            CamelliaLightEngine.roldq(15, arrn2, 0, this.subkey, 8);
            CamelliaLightEngine.roldq(30, arrn2, 0, this.subkey, 20);
            this.ke[8] = arrn2[1];
            this.ke[9] = arrn2[2];
            this.ke[10] = arrn2[3];
            this.ke[11] = arrn2[0];
            CamelliaLightEngine.roldqo32(49, arrn2, 0, this.subkey, 40);
            this.subkey[0] = arrn3[0];
            this.subkey[1] = arrn3[1];
            this.subkey[2] = arrn3[2];
            this.subkey[3] = arrn3[3];
            CamelliaLightEngine.roldq(30, arrn3, 0, this.subkey, 12);
            CamelliaLightEngine.roldq(30, arrn3, 0, this.subkey, 28);
            CamelliaLightEngine.roldqo32(51, arrn3, 0, this.kw, 4);
            return;
        }
        this.kw[4] = arrn[0];
        this.kw[5] = arrn[1];
        this.kw[6] = arrn[2];
        this.kw[7] = arrn[3];
        CamelliaLightEngine.decroldqo32(45, arrn, 0, this.subkey, 28);
        CamelliaLightEngine.decroldq(15, arrn, 0, this.ke, 4);
        CamelliaLightEngine.decroldq(17, arrn, 0, this.subkey, 12);
        CamelliaLightEngine.decroldqo32(34, arrn, 0, this.subkey, 0);
        CamelliaLightEngine.decroldq(15, arrn, 4, this.subkey, 40);
        CamelliaLightEngine.decroldq(15, arrn, 4, this.ke, 8);
        CamelliaLightEngine.decroldq(30, arrn, 4, this.subkey, 20);
        CamelliaLightEngine.decroldqo32(34, arrn, 4, this.subkey, 8);
        CamelliaLightEngine.decroldq(15, arrn2, 0, this.subkey, 36);
        CamelliaLightEngine.decroldq(30, arrn2, 0, this.subkey, 24);
        this.ke[2] = arrn2[1];
        this.ke[3] = arrn2[2];
        this.ke[0] = arrn2[3];
        this.ke[1] = arrn2[0];
        CamelliaLightEngine.decroldqo32(49, arrn2, 0, this.subkey, 4);
        this.subkey[46] = arrn3[0];
        this.subkey[47] = arrn3[1];
        this.subkey[44] = arrn3[2];
        this.subkey[45] = arrn3[3];
        CamelliaLightEngine.decroldq(30, arrn3, 0, this.subkey, 32);
        CamelliaLightEngine.decroldq(30, arrn3, 0, this.subkey, 16);
        CamelliaLightEngine.roldqo32(51, arrn3, 0, this.kw, 0);
    }

    @Override
    public String getAlgorithmName() {
        return "Camellia";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("only simple KeyParameter expected.");
        }
        this.setKey(bl, ((KeyParameter)cipherParameters).getKey());
        this.initialized = true;
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (!this.initialized) {
            throw new IllegalStateException("Camellia is not initialized");
        }
        if (n2 + 16 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 16 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this._keyis128) {
            return this.processBlock128(arrby, n2, arrby2, n3);
        }
        return this.processBlock192or256(arrby, n2, arrby2, n3);
    }

    @Override
    public void reset() {
    }
}

