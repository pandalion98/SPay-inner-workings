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

public class HC256Engine
implements StreamCipher {
    private byte[] buf = new byte[4];
    private int cnt = 0;
    private int idx = 0;
    private boolean initialised;
    private byte[] iv;
    private byte[] key;
    private int[] p = new int[1024];
    private int[] q = new int[1024];

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

    private void init() {
        int n2 = 16;
        if (this.key.length != 32 && this.key.length != n2) {
            throw new IllegalArgumentException("The key must be 128/256 bits long");
        }
        if (this.iv.length < n2) {
            throw new IllegalArgumentException("The IV must be at least 128 bits long");
        }
        if (this.key.length != 32) {
            byte[] arrby = new byte[32];
            System.arraycopy((Object)this.key, (int)0, (Object)arrby, (int)0, (int)this.key.length);
            System.arraycopy((Object)this.key, (int)0, (Object)arrby, (int)n2, (int)this.key.length);
            this.key = arrby;
        }
        if (this.iv.length < 32) {
            byte[] arrby = new byte[32];
            System.arraycopy((Object)this.iv, (int)0, (Object)arrby, (int)0, (int)this.iv.length);
            System.arraycopy((Object)this.iv, (int)0, (Object)arrby, (int)this.iv.length, (int)(arrby.length - this.iv.length));
            this.iv = arrby;
        }
        this.idx = 0;
        this.cnt = 0;
        int[] arrn = new int[2560];
        for (int i2 = 0; i2 < 32; ++i2) {
            int n3 = i2 >> 2;
            arrn[n3] = arrn[n3] | (255 & this.key[i2]) << 8 * (i2 & 3);
        }
        for (int i3 = 0; i3 < 32; ++i3) {
            int n4 = 8 + (i3 >> 2);
            arrn[n4] = arrn[n4] | (255 & this.iv[i3]) << 8 * (i3 & 3);
        }
        while (n2 < 2560) {
            int n5 = arrn[n2 - 2];
            int n6 = arrn[n2 - 15];
            arrn[n2] = n2 + ((HC256Engine.rotateRight(n5, 17) ^ HC256Engine.rotateRight(n5, 19) ^ n5 >>> 10) + arrn[n2 - 7] + (HC256Engine.rotateRight(n6, 7) ^ HC256Engine.rotateRight(n6, 18) ^ n6 >>> 3) + arrn[n2 - 16]);
            ++n2;
        }
        System.arraycopy((Object)arrn, (int)512, (Object)this.p, (int)0, (int)1024);
        System.arraycopy((Object)arrn, (int)1536, (Object)this.q, (int)0, (int)1024);
        for (int i4 = 0; i4 < 4096; ++i4) {
            this.step();
        }
        this.cnt = 0;
    }

    private static int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int step() {
        int n2;
        int n3 = 1023 & this.cnt;
        if (this.cnt < 1024) {
            int n4 = this.p[1023 & n3 - 3];
            int n5 = this.p[1023 & n3 - 1023];
            int[] arrn = this.p;
            arrn[n3] = arrn[n3] + (this.p[1023 & n3 - 10] + (HC256Engine.rotateRight(n4, 10) ^ HC256Engine.rotateRight(n5, 23)) + this.q[1023 & (n4 ^ n5)]);
            int n6 = this.p[1023 & n3 - 12];
            n2 = this.q[n6 & 255] + this.q[256 + (255 & n6 >> 8)] + this.q[512 + (255 & n6 >> 16)] + this.q[768 + (255 & n6 >> 24)] ^ this.p[n3];
        } else {
            int n7 = this.q[1023 & n3 - 3];
            int n8 = this.q[1023 & n3 - 1023];
            int[] arrn = this.q;
            arrn[n3] = arrn[n3] + (this.q[1023 & n3 - 10] + (HC256Engine.rotateRight(n7, 10) ^ HC256Engine.rotateRight(n8, 23)) + this.p[1023 & (n7 ^ n8)]);
            int n9 = this.q[1023 & n3 - 12];
            n2 = this.p[n9 & 255] + this.p[256 + (255 & n9 >> 8)] + this.p[512 + (255 & n9 >> 16)] + this.p[768 + (255 & n9 >> 24)] ^ this.q[n3];
        }
        this.cnt = 2047 & 1 + this.cnt;
        return n2;
    }

    @Override
    public String getAlgorithmName() {
        return "HC-256";
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
        throw new IllegalArgumentException("Invalid parameter passed to HC256 init - " + cipherParameters.getClass().getName());
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

