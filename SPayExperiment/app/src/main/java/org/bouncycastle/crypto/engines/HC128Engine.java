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

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class HC128Engine
implements StreamCipher {
    private byte[] buf = new byte[4];
    private int cnt = 0;
    private int idx = 0;
    private boolean initialised;
    private byte[] iv;
    private byte[] key;
    private int[] p = new int[512];
    private int[] q = new int[512];

    private static int dim(int n2, int n3) {
        return HC128Engine.mod512(n2 - n3);
    }

    private static int f1(int n2) {
        return HC128Engine.rotateRight(n2, 7) ^ HC128Engine.rotateRight(n2, 18) ^ n2 >>> 3;
    }

    private static int f2(int n2) {
        return HC128Engine.rotateRight(n2, 17) ^ HC128Engine.rotateRight(n2, 19) ^ n2 >>> 10;
    }

    private int g1(int n2, int n3, int n4) {
        return (HC128Engine.rotateRight(n2, 10) ^ HC128Engine.rotateRight(n4, 23)) + HC128Engine.rotateRight(n3, 8);
    }

    private int g2(int n2, int n3, int n4) {
        return (HC128Engine.rotateLeft(n2, 10) ^ HC128Engine.rotateLeft(n4, 23)) + HC128Engine.rotateLeft(n3, 8);
    }

    private byte getByte() {
        if (this.idx == 0) {
            int n2 = this.step();
            this.buf[0] = (byte)(n2 & 255);
            int n3 = n2 >> 8;
            this.buf[1] = (byte)(n3 & 255);
            int n4 = n3 >> 8;
            this.buf[2] = (byte)(n4 & 255);
            int n5 = n4 >> 8;
            this.buf[3] = (byte)(n5 & 255);
        }
        byte by = this.buf[this.idx];
        this.idx = 3 & 1 + this.idx;
        return by;
    }

    private int h1(int n2) {
        return this.q[n2 & 255] + this.q[256 + (255 & n2 >> 16)];
    }

    private int h2(int n2) {
        return this.p[n2 & 255] + this.p[256 + (255 & n2 >> 16)];
    }

    private void init() {
        if (this.key.length != 16) {
            throw new IllegalArgumentException("The key must be 128 bits long");
        }
        this.idx = 0;
        this.cnt = 0;
        int[] arrn = new int[1280];
        for (int i2 = 0; i2 < 16; ++i2) {
            int n2 = i2 >> 2;
            arrn[n2] = arrn[n2] | (255 & this.key[i2]) << 8 * (i2 & 3);
        }
        System.arraycopy((Object)arrn, (int)0, (Object)arrn, (int)4, (int)4);
        for (int i3 = 0; i3 < this.iv.length && i3 < 16; ++i3) {
            int n3 = 8 + (i3 >> 2);
            arrn[n3] = arrn[n3] | (255 & this.iv[i3]) << 8 * (i3 & 3);
        }
        System.arraycopy((Object)arrn, (int)8, (Object)arrn, (int)12, (int)4);
        for (int i4 = 16; i4 < 1280; ++i4) {
            arrn[i4] = i4 + (HC128Engine.f2(arrn[i4 - 2]) + arrn[i4 - 7] + HC128Engine.f1(arrn[i4 - 15]) + arrn[i4 - 16]);
        }
        System.arraycopy((Object)arrn, (int)256, (Object)this.p, (int)0, (int)512);
        System.arraycopy((Object)arrn, (int)768, (Object)this.q, (int)0, (int)512);
        for (int i5 = 0; i5 < 512; ++i5) {
            this.p[i5] = this.step();
        }
        for (int i6 = 0; i6 < 512; ++i6) {
            this.q[i6] = this.step();
        }
        this.cnt = 0;
    }

    private static int mod1024(int n2) {
        return n2 & 1023;
    }

    private static int mod512(int n2) {
        return n2 & 511;
    }

    private static int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    private static int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int step() {
        int n2;
        int n3 = HC128Engine.mod512(this.cnt);
        if (this.cnt < 512) {
            int[] arrn = this.p;
            arrn[n3] = arrn[n3] + this.g1(this.p[HC128Engine.dim(n3, 3)], this.p[HC128Engine.dim(n3, 10)], this.p[HC128Engine.dim(n3, 511)]);
            n2 = this.h1(this.p[HC128Engine.dim(n3, 12)]) ^ this.p[n3];
        } else {
            int[] arrn = this.q;
            arrn[n3] = arrn[n3] + this.g2(this.q[HC128Engine.dim(n3, 3)], this.q[HC128Engine.dim(n3, 10)], this.q[HC128Engine.dim(n3, 511)]);
            n2 = this.h2(this.q[HC128Engine.dim(n3, 12)]) ^ this.q[n3];
        }
        this.cnt = HC128Engine.mod1024(1 + this.cnt);
        return n2;
    }

    @Override
    public String getAlgorithmName() {
        return "HC-128";
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        if (cipherParameters instanceof ParametersWithIV) {
            this.iv = ((ParametersWithIV)cipherParameters).getIV();
            cipherParameters2 = ((ParametersWithIV)cipherParameters).getParameters();
        } else {
            this.iv = new byte[0];
            cipherParameters2 = cipherParameters;
        }
        if (cipherParameters2 instanceof KeyParameter) {
            this.key = ((KeyParameter)cipherParameters2).getKey();
            this.init();
            this.initialised = true;
            return;
        }
        throw new IllegalArgumentException("Invalid parameter passed to HC128 init - " + cipherParameters.getClass().getName());
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
            arrby2[n4 + i2] = (byte)(arrby[n2 + i2] ^ this.getByte());
        }
        return n3;
    }

    @Override
    public void reset() {
        this.init();
    }

    @Override
    public byte returnByte(byte by) {
        return (byte)(by ^ this.getByte());
    }
}

