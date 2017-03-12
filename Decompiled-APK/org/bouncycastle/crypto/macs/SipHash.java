package org.bouncycastle.crypto.macs;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class SipHash implements Mac {
    protected final int f187c;
    protected final int f188d;
    protected long k0;
    protected long k1;
    protected long f189m;
    protected long v0;
    protected long v1;
    protected long v2;
    protected long v3;
    protected int wordCount;
    protected int wordPos;

    public SipHash() {
        this.f189m = 0;
        this.wordPos = 0;
        this.wordCount = 0;
        this.f187c = 2;
        this.f188d = 4;
    }

    public SipHash(int i, int i2) {
        this.f189m = 0;
        this.wordPos = 0;
        this.wordCount = 0;
        this.f187c = i;
        this.f188d = i2;
    }

    protected static long rotateLeft(long j, int i) {
        return (j << i) | (j >>> (-i));
    }

    protected void applySipRounds(int i) {
        long j = this.v0;
        long j2 = this.v1;
        long j3 = this.v2;
        long j4 = this.v3;
        for (int i2 = 0; i2 < i; i2++) {
            j += j2;
            j3 += j4;
            j2 = rotateLeft(j2, 13) ^ j;
            j4 = rotateLeft(j4, 16) ^ j3;
            j3 += j2;
            j = rotateLeft(j, 32) + j4;
            j2 = rotateLeft(j2, 17) ^ j3;
            j4 = rotateLeft(j4, 21) ^ j;
            j3 = rotateLeft(j3, 32);
        }
        this.v0 = j;
        this.v1 = j2;
        this.v2 = j3;
        this.v3 = j4;
    }

    public int doFinal(byte[] bArr, int i) {
        Pack.longToLittleEndian(doFinal(), bArr, i);
        return 8;
    }

    public long doFinal() {
        this.f189m >>>= (7 - this.wordPos) << 3;
        this.f189m >>>= 8;
        this.f189m |= (((long) ((this.wordCount << 3) + this.wordPos)) & 255) << 56;
        processMessageWord();
        this.v2 ^= 255;
        applySipRounds(this.f188d);
        long j = ((this.v0 ^ this.v1) ^ this.v2) ^ this.v3;
        reset();
        return j;
    }

    public String getAlgorithmName() {
        return "SipHash-" + this.f187c + HCEClientConstants.TAG_KEY_SEPARATOR + this.f188d;
    }

    public int getMacSize() {
        return 8;
    }

    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            byte[] key = ((KeyParameter) cipherParameters).getKey();
            if (key.length != 16) {
                throw new IllegalArgumentException("'params' must be a 128-bit key");
            }
            this.k0 = Pack.littleEndianToLong(key, 0);
            this.k1 = Pack.littleEndianToLong(key, 8);
            reset();
            return;
        }
        throw new IllegalArgumentException("'params' must be an instance of KeyParameter");
    }

    protected void processMessageWord() {
        this.wordCount++;
        this.v3 ^= this.f189m;
        applySipRounds(this.f187c);
        this.v0 ^= this.f189m;
    }

    public void reset() {
        this.v0 = this.k0 ^ 8317987319222330741L;
        this.v1 = this.k1 ^ 7237128888997146477L;
        this.v2 = this.k0 ^ 7816392313619706465L;
        this.v3 = this.k1 ^ 8387220255154660723L;
        this.f189m = 0;
        this.wordPos = 0;
        this.wordCount = 0;
    }

    public void update(byte b) {
        this.f189m >>>= 8;
        this.f189m |= (((long) b) & 255) << 56;
        int i = this.wordPos + 1;
        this.wordPos = i;
        if (i == 8) {
            processMessageWord();
            this.wordPos = 0;
        }
    }

    public void update(byte[] bArr, int i, int i2) {
        int i3 = 0;
        int i4 = i2 & -8;
        if (this.wordPos == 0) {
            while (i3 < i4) {
                this.f189m = Pack.littleEndianToLong(bArr, i + i3);
                processMessageWord();
                i3 += 8;
            }
            while (i3 < i2) {
                this.f189m >>>= 8;
                this.f189m |= (((long) bArr[i + i3]) & 255) << 56;
                i3++;
            }
            this.wordPos = i2 - i4;
            return;
        }
        int i5 = this.wordPos << 3;
        while (i3 < i4) {
            long littleEndianToLong = Pack.littleEndianToLong(bArr, i + i3);
            this.f189m = (littleEndianToLong << i5) | (this.f189m >>> (-i5));
            processMessageWord();
            this.f189m = littleEndianToLong;
            i3 += 8;
        }
        while (i3 < i2) {
            this.f189m >>>= 8;
            this.f189m |= (((long) bArr[i + i3]) & 255) << 56;
            i4 = this.wordPos + 1;
            this.wordPos = i4;
            if (i4 == 8) {
                processMessageWord();
                this.wordPos = 0;
            }
            i3++;
        }
    }
}
