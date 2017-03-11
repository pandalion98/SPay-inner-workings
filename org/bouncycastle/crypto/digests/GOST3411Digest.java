package org.bouncycastle.crypto.digests;

import android.support.v4.view.MotionEventCompat;
import java.lang.reflect.Array;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public class GOST3411Digest implements ExtendedDigest, Memoable {
    private static final byte[] C2;
    private static final int DIGEST_LENGTH = 32;
    private byte[][] f107C;
    private byte[] f108H;
    private byte[] f109K;
    private byte[] f110L;
    private byte[] f111M;
    byte[] f112S;
    private byte[] Sum;
    byte[] f113U;
    byte[] f114V;
    byte[] f115W;
    byte[] f116a;
    private long byteCount;
    private BlockCipher cipher;
    private byte[] sBox;
    short[] wS;
    short[] w_S;
    private byte[] xBuf;
    private int xBufOff;

    static {
        C2 = new byte[]{(byte) 0, (byte) -1, (byte) 0, (byte) -1, (byte) 0, (byte) -1, (byte) 0, (byte) -1, (byte) -1, (byte) 0, (byte) -1, (byte) 0, (byte) -1, (byte) 0, (byte) -1, (byte) 0, (byte) 0, (byte) -1, (byte) -1, (byte) 0, (byte) -1, (byte) 0, (byte) 0, (byte) -1, (byte) -1, (byte) 0, (byte) 0, (byte) 0, (byte) -1, (byte) -1, (byte) 0, (byte) -1};
    }

    public GOST3411Digest() {
        this.f108H = new byte[DIGEST_LENGTH];
        this.f110L = new byte[DIGEST_LENGTH];
        this.f111M = new byte[DIGEST_LENGTH];
        this.Sum = new byte[DIGEST_LENGTH];
        this.f107C = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, DIGEST_LENGTH});
        this.xBuf = new byte[DIGEST_LENGTH];
        this.cipher = new GOST28147Engine();
        this.f109K = new byte[DIGEST_LENGTH];
        this.f116a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.f112S = new byte[DIGEST_LENGTH];
        this.f113U = new byte[DIGEST_LENGTH];
        this.f114V = new byte[DIGEST_LENGTH];
        this.f115W = new byte[DIGEST_LENGTH];
        this.sBox = GOST28147Engine.getSBox("D-A");
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        reset();
    }

    public GOST3411Digest(GOST3411Digest gOST3411Digest) {
        this.f108H = new byte[DIGEST_LENGTH];
        this.f110L = new byte[DIGEST_LENGTH];
        this.f111M = new byte[DIGEST_LENGTH];
        this.Sum = new byte[DIGEST_LENGTH];
        this.f107C = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, DIGEST_LENGTH});
        this.xBuf = new byte[DIGEST_LENGTH];
        this.cipher = new GOST28147Engine();
        this.f109K = new byte[DIGEST_LENGTH];
        this.f116a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.f112S = new byte[DIGEST_LENGTH];
        this.f113U = new byte[DIGEST_LENGTH];
        this.f114V = new byte[DIGEST_LENGTH];
        this.f115W = new byte[DIGEST_LENGTH];
        reset(gOST3411Digest);
    }

    public GOST3411Digest(byte[] bArr) {
        this.f108H = new byte[DIGEST_LENGTH];
        this.f110L = new byte[DIGEST_LENGTH];
        this.f111M = new byte[DIGEST_LENGTH];
        this.Sum = new byte[DIGEST_LENGTH];
        this.f107C = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, DIGEST_LENGTH});
        this.xBuf = new byte[DIGEST_LENGTH];
        this.cipher = new GOST28147Engine();
        this.f109K = new byte[DIGEST_LENGTH];
        this.f116a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.f112S = new byte[DIGEST_LENGTH];
        this.f113U = new byte[DIGEST_LENGTH];
        this.f114V = new byte[DIGEST_LENGTH];
        this.f115W = new byte[DIGEST_LENGTH];
        this.sBox = Arrays.clone(bArr);
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        reset();
    }

    private byte[] m1719A(byte[] bArr) {
        for (int i = 0; i < 8; i++) {
            this.f116a[i] = (byte) (bArr[i] ^ bArr[i + 8]);
        }
        System.arraycopy(bArr, 8, bArr, 0, 24);
        System.arraycopy(this.f116a, 0, bArr, 24, 8);
        return bArr;
    }

    private void m1720E(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2) {
        this.cipher.init(true, new KeyParameter(bArr));
        this.cipher.processBlock(bArr3, i2, bArr2, i);
    }

    private byte[] m1721P(byte[] bArr) {
        for (int i = 0; i < 8; i++) {
            this.f109K[i * 4] = bArr[i];
            this.f109K[(i * 4) + 1] = bArr[i + 8];
            this.f109K[(i * 4) + 2] = bArr[i + 16];
            this.f109K[(i * 4) + 3] = bArr[i + 24];
        }
        return this.f109K;
    }

    private void cpyBytesToShort(byte[] bArr, short[] sArr) {
        for (int i = 0; i < bArr.length / 2; i++) {
            sArr[i] = (short) (((bArr[(i * 2) + 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (bArr[i * 2] & GF2Field.MASK));
        }
    }

    private void cpyShortToBytes(short[] sArr, byte[] bArr) {
        for (int i = 0; i < bArr.length / 2; i++) {
            bArr[(i * 2) + 1] = (byte) (sArr[i] >> 8);
            bArr[i * 2] = (byte) sArr[i];
        }
    }

    private void finish() {
        Pack.longToLittleEndian(this.byteCount * 8, this.f110L, 0);
        while (this.xBufOff != 0) {
            update((byte) 0);
        }
        processBlock(this.f110L, 0);
        processBlock(this.Sum, 0);
    }

    private void fw(byte[] bArr) {
        cpyBytesToShort(bArr, this.wS);
        this.w_S[15] = (short) (((((this.wS[0] ^ this.wS[1]) ^ this.wS[2]) ^ this.wS[3]) ^ this.wS[12]) ^ this.wS[15]);
        System.arraycopy(this.wS, 1, this.w_S, 0, 15);
        cpyShortToBytes(this.w_S, bArr);
    }

    private void sumByteArray(byte[] bArr) {
        int i = 0;
        int i2 = 0;
        while (i != this.Sum.length) {
            i2 += (this.Sum[i] & GF2Field.MASK) + (bArr[i] & GF2Field.MASK);
            this.Sum[i] = (byte) i2;
            i2 >>>= 8;
            i++;
        }
    }

    public Memoable copy() {
        return new GOST3411Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        finish();
        System.arraycopy(this.f108H, 0, bArr, i, this.f108H.length);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "GOST3411";
    }

    public int getByteLength() {
        return DIGEST_LENGTH;
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    protected void processBlock(byte[] bArr, int i) {
        int i2;
        System.arraycopy(bArr, i, this.f111M, 0, DIGEST_LENGTH);
        System.arraycopy(this.f108H, 0, this.f113U, 0, DIGEST_LENGTH);
        System.arraycopy(this.f111M, 0, this.f114V, 0, DIGEST_LENGTH);
        for (i2 = 0; i2 < DIGEST_LENGTH; i2++) {
            this.f115W[i2] = (byte) (this.f113U[i2] ^ this.f114V[i2]);
        }
        m1720E(m1721P(this.f115W), this.f112S, 0, this.f108H, 0);
        for (int i3 = 1; i3 < 4; i3++) {
            byte[] A = m1719A(this.f113U);
            for (i2 = 0; i2 < DIGEST_LENGTH; i2++) {
                this.f113U[i2] = (byte) (A[i2] ^ this.f107C[i3][i2]);
            }
            this.f114V = m1719A(m1719A(this.f114V));
            for (i2 = 0; i2 < DIGEST_LENGTH; i2++) {
                this.f115W[i2] = (byte) (this.f113U[i2] ^ this.f114V[i2]);
            }
            m1720E(m1721P(this.f115W), this.f112S, i3 * 8, this.f108H, i3 * 8);
        }
        for (i2 = 0; i2 < 12; i2++) {
            fw(this.f112S);
        }
        for (i2 = 0; i2 < DIGEST_LENGTH; i2++) {
            this.f112S[i2] = (byte) (this.f112S[i2] ^ this.f111M[i2]);
        }
        fw(this.f112S);
        for (i2 = 0; i2 < DIGEST_LENGTH; i2++) {
            this.f112S[i2] = (byte) (this.f108H[i2] ^ this.f112S[i2]);
        }
        for (i2 = 0; i2 < 61; i2++) {
            fw(this.f112S);
        }
        System.arraycopy(this.f112S, 0, this.f108H, 0, this.f108H.length);
    }

    public void reset() {
        int i;
        this.byteCount = 0;
        this.xBufOff = 0;
        for (i = 0; i < this.f108H.length; i++) {
            this.f108H[i] = (byte) 0;
        }
        for (i = 0; i < this.f110L.length; i++) {
            this.f110L[i] = (byte) 0;
        }
        for (i = 0; i < this.f111M.length; i++) {
            this.f111M[i] = (byte) 0;
        }
        for (i = 0; i < this.f107C[1].length; i++) {
            this.f107C[1][i] = (byte) 0;
        }
        for (i = 0; i < this.f107C[3].length; i++) {
            this.f107C[3][i] = (byte) 0;
        }
        for (i = 0; i < this.Sum.length; i++) {
            this.Sum[i] = (byte) 0;
        }
        for (i = 0; i < this.xBuf.length; i++) {
            this.xBuf[i] = (byte) 0;
        }
        System.arraycopy(C2, 0, this.f107C[2], 0, C2.length);
    }

    public void reset(Memoable memoable) {
        GOST3411Digest gOST3411Digest = (GOST3411Digest) memoable;
        this.sBox = gOST3411Digest.sBox;
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        reset();
        System.arraycopy(gOST3411Digest.f108H, 0, this.f108H, 0, gOST3411Digest.f108H.length);
        System.arraycopy(gOST3411Digest.f110L, 0, this.f110L, 0, gOST3411Digest.f110L.length);
        System.arraycopy(gOST3411Digest.f111M, 0, this.f111M, 0, gOST3411Digest.f111M.length);
        System.arraycopy(gOST3411Digest.Sum, 0, this.Sum, 0, gOST3411Digest.Sum.length);
        System.arraycopy(gOST3411Digest.f107C[1], 0, this.f107C[1], 0, gOST3411Digest.f107C[1].length);
        System.arraycopy(gOST3411Digest.f107C[2], 0, this.f107C[2], 0, gOST3411Digest.f107C[2].length);
        System.arraycopy(gOST3411Digest.f107C[3], 0, this.f107C[3], 0, gOST3411Digest.f107C[3].length);
        System.arraycopy(gOST3411Digest.xBuf, 0, this.xBuf, 0, gOST3411Digest.xBuf.length);
        this.xBufOff = gOST3411Digest.xBufOff;
        this.byteCount = gOST3411Digest.byteCount;
    }

    public void update(byte b) {
        byte[] bArr = this.xBuf;
        int i = this.xBufOff;
        this.xBufOff = i + 1;
        bArr[i] = b;
        if (this.xBufOff == this.xBuf.length) {
            sumByteArray(this.xBuf);
            processBlock(this.xBuf, 0);
            this.xBufOff = 0;
        }
        this.byteCount++;
    }

    public void update(byte[] bArr, int i, int i2) {
        while (this.xBufOff != 0 && i2 > 0) {
            update(bArr[i]);
            i++;
            i2--;
        }
        while (i2 > this.xBuf.length) {
            System.arraycopy(bArr, i, this.xBuf, 0, this.xBuf.length);
            sumByteArray(this.xBuf);
            processBlock(this.xBuf, 0);
            i += this.xBuf.length;
            i2 -= this.xBuf.length;
            this.byteCount += (long) this.xBuf.length;
        }
        while (i2 > 0) {
            update(bArr[i]);
            i++;
            i2--;
        }
    }
}
