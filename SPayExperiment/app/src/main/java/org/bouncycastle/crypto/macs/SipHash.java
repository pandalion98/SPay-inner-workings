/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class SipHash
implements Mac {
    protected final int c;
    protected final int d;
    protected long k0;
    protected long k1;
    protected long m = 0L;
    protected long v0;
    protected long v1;
    protected long v2;
    protected long v3;
    protected int wordCount = 0;
    protected int wordPos = 0;

    public SipHash() {
        this.c = 2;
        this.d = 4;
    }

    public SipHash(int n2, int n3) {
        this.c = n2;
        this.d = n3;
    }

    protected static long rotateLeft(long l2, int n2) {
        return l2 << n2 | l2 >>> -n2;
    }

    protected void applySipRounds(int n2) {
        long l2 = this.v0;
        long l3 = this.v1;
        long l4 = this.v2;
        long l5 = this.v3;
        for (int i2 = 0; i2 < n2; ++i2) {
            long l6 = l2 + l3;
            long l7 = l4 + l5;
            long l8 = SipHash.rotateLeft(l3, 13);
            long l9 = SipHash.rotateLeft(l5, 16);
            long l10 = l8 ^ l6;
            long l11 = l9 ^ l7;
            long l12 = SipHash.rotateLeft(l6, 32);
            long l13 = l7 + l10;
            l2 = l12 + l11;
            long l14 = SipHash.rotateLeft(l10, 17);
            long l15 = SipHash.rotateLeft(l11, 21);
            l3 = l14 ^ l13;
            l5 = l15 ^ l2;
            l4 = SipHash.rotateLeft(l13, 32);
        }
        this.v0 = l2;
        this.v1 = l3;
        this.v2 = l4;
        this.v3 = l5;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        Pack.longToLittleEndian((long)this.doFinal(), (byte[])arrby, (int)n2);
        return 8;
    }

    public long doFinal() {
        this.m >>>= 7 - this.wordPos << 3;
        this.m >>>= 8;
        this.m |= (255L & (long)((this.wordCount << 3) + this.wordPos)) << 56;
        this.processMessageWord();
        this.v2 = 255L ^ this.v2;
        this.applySipRounds(this.d);
        long l2 = this.v0 ^ this.v1 ^ this.v2 ^ this.v3;
        this.reset();
        return l2;
    }

    @Override
    public String getAlgorithmName() {
        return "SipHash-" + this.c + "-" + this.d;
    }

    @Override
    public int getMacSize() {
        return 8;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("'params' must be an instance of KeyParameter");
        }
        byte[] arrby = ((KeyParameter)cipherParameters).getKey();
        if (arrby.length != 16) {
            throw new IllegalArgumentException("'params' must be a 128-bit key");
        }
        this.k0 = Pack.littleEndianToLong((byte[])arrby, (int)0);
        this.k1 = Pack.littleEndianToLong((byte[])arrby, (int)8);
        this.reset();
    }

    protected void processMessageWord() {
        this.wordCount = 1 + this.wordCount;
        this.v3 ^= this.m;
        this.applySipRounds(this.c);
        this.v0 ^= this.m;
    }

    @Override
    public void reset() {
        this.v0 = 8317987319222330741L ^ this.k0;
        this.v1 = 7237128888997146477L ^ this.k1;
        this.v2 = 7816392313619706465L ^ this.k0;
        this.v3 = 8387220255154660723L ^ this.k1;
        this.m = 0L;
        this.wordPos = 0;
        this.wordCount = 0;
    }

    @Override
    public void update(byte by) {
        int n2;
        this.m >>>= 8;
        this.m |= (255L & (long)by) << 56;
        this.wordPos = n2 = 1 + this.wordPos;
        if (n2 == 8) {
            this.processMessageWord();
            this.wordPos = 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(byte[] arrby, int n2, int n3) {
        int n4;
        int n5 = n3 & -8;
        int n6 = this.wordPos;
        if (n6 == 0) {
            for (n4 = 0; n4 < n5; n4 += 8) {
                this.m = Pack.littleEndianToLong((byte[])arrby, (int)(n2 + n4));
                this.processMessageWord();
            }
            while (n4 < n3) {
                this.m >>>= 8;
                this.m |= (255L & (long)arrby[n2 + n4]) << 56;
                ++n4;
            }
            this.wordPos = n3 - n5;
            return;
        } else {
            int n7 = this.wordPos << 3;
            while (n4 < n5) {
                long l2 = Pack.littleEndianToLong((byte[])arrby, (int)(n2 + n4));
                this.m = l2 << n7 | this.m >>> -n7;
                this.processMessageWord();
                this.m = l2;
                n4 += 8;
            }
            while (n4 < n3) {
                int n8;
                this.m >>>= 8;
                this.m |= (255L & (long)arrby[n2 + n4]) << 56;
                this.wordPos = n8 = 1 + this.wordPos;
                if (n8 == 8) {
                    this.processMessageWord();
                    this.wordPos = 0;
                }
                ++n4;
            }
        }
    }
}

