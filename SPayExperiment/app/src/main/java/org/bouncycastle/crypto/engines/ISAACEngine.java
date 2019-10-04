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
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class ISAACEngine
implements StreamCipher {
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int[] engineState = null;
    private int index = 0;
    private boolean initialised = false;
    private byte[] keyStream = new byte[1024];
    private int[] results = null;
    private final int sizeL = 8;
    private final int stateArraySize = 256;
    private byte[] workingKey = null;

    /*
     * Enabled aggressive block sorting
     */
    private void isaac() {
        int n2;
        int n3 = this.b;
        this.c = n2 = 1 + this.c;
        this.b = n3 + n2;
        int n4 = 0;
        while (n4 < 256) {
            int n5;
            int n6;
            int n7 = this.engineState[n4];
            switch (n4 & 3) {
                case 0: {
                    this.a ^= this.a << 13;
                    break;
                }
                case 1: {
                    this.a ^= this.a >>> 6;
                    break;
                }
                case 2: {
                    this.a ^= this.a << 2;
                    break;
                }
                case 3: {
                    this.a ^= this.a >>> 16;
                    break;
                }
            }
            this.a += this.engineState[255 & n4 + 128];
            int[] arrn = this.engineState;
            arrn[n4] = n6 = this.engineState[255 & n7 >>> 2] + this.a + this.b;
            int[] arrn2 = this.results;
            this.b = n5 = n7 + this.engineState[255 & n6 >>> 10];
            arrn2[n4] = n5;
            ++n4;
        }
        return;
    }

    private void mix(int[] arrn) {
        arrn[0] = arrn[0] ^ arrn[1] << 11;
        arrn[3] = arrn[3] + arrn[0];
        arrn[1] = arrn[1] + arrn[2];
        arrn[1] = arrn[1] ^ arrn[2] >>> 2;
        arrn[4] = arrn[4] + arrn[1];
        arrn[2] = arrn[2] + arrn[3];
        arrn[2] = arrn[2] ^ arrn[3] << 8;
        arrn[5] = arrn[5] + arrn[2];
        arrn[3] = arrn[3] + arrn[4];
        arrn[3] = arrn[3] ^ arrn[4] >>> 16;
        arrn[6] = arrn[6] + arrn[3];
        arrn[4] = arrn[4] + arrn[5];
        arrn[4] = arrn[4] ^ arrn[5] << 10;
        arrn[7] = arrn[7] + arrn[4];
        arrn[5] = arrn[5] + arrn[6];
        arrn[5] = arrn[5] ^ arrn[6] >>> 4;
        arrn[0] = arrn[0] + arrn[5];
        arrn[6] = arrn[6] + arrn[7];
        arrn[6] = arrn[6] ^ arrn[7] << 8;
        arrn[1] = arrn[1] + arrn[6];
        arrn[7] = arrn[7] + arrn[0];
        arrn[7] = arrn[7] ^ arrn[0] >>> 9;
        arrn[2] = arrn[2] + arrn[7];
        arrn[0] = arrn[0] + arrn[1];
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setKey(byte[] arrby) {
        this.workingKey = arrby;
        if (this.engineState == null) {
            this.engineState = new int[256];
        }
        if (this.results == null) {
            this.results = new int[256];
        }
        for (int i2 = 0; i2 < 256; ++i2) {
            int[] arrn = this.engineState;
            this.results[i2] = 0;
            arrn[i2] = 0;
        }
        this.c = 0;
        this.b = 0;
        this.a = 0;
        this.index = 0;
        byte[] arrby2 = new byte[arrby.length + (3 & arrby.length)];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        for (int i3 = 0; i3 < arrby2.length; i3 += 4) {
            this.results[i3 >>> 2] = Pack.littleEndianToInt((byte[])arrby2, (int)i3);
        }
        int[] arrn = new int[8];
        for (int i4 = 0; i4 < 8; ++i4) {
            arrn[i4] = -1640531527;
        }
        for (int i5 = 0; i5 < 4; ++i5) {
            this.mix(arrn);
        }
        int n2 = 0;
        block4 : do {
            if (n2 >= 2) {
                this.isaac();
                this.initialised = true;
                return;
            }
            int n3 = 0;
            do {
                if (n3 < 256) {
                } else {
                    ++n2;
                    continue block4;
                }
                for (int i6 = 0; i6 < 8; ++i6) {
                    int n4 = arrn[i6];
                    int n5 = n2 < 1 ? this.results[n3 + i6] : this.engineState[n3 + i6];
                    arrn[i6] = n5 + n4;
                }
                this.mix(arrn);
                for (int i7 = 0; i7 < 8; ++i7) {
                    this.engineState[n3 + i7] = arrn[i7];
                }
                n3 += 8;
            } while (true);
            break;
        } while (true);
    }

    @Override
    public String getAlgorithmName() {
        return "ISAAC";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to ISAAC init - " + cipherParameters.getClass().getName());
        }
        this.setKey(((KeyParameter)cipherParameters).getKey());
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            if (this.index == 0) {
                this.isaac();
                this.keyStream = Pack.intToBigEndian((int[])this.results);
            }
            arrby2[i2 + n4] = (byte)(this.keyStream[this.index] ^ arrby[i2 + n2]);
            this.index = 1023 & 1 + this.index;
        }
        return n3;
    }

    @Override
    public void reset() {
        this.setKey(this.workingKey);
    }

    @Override
    public byte returnByte(byte by) {
        if (this.index == 0) {
            this.isaac();
            this.keyStream = Pack.intToBigEndian((int[])this.results);
        }
        byte by2 = (byte)(by ^ this.keyStream[this.index]);
        this.index = 1023 & 1 + this.index;
        return by2;
    }
}

