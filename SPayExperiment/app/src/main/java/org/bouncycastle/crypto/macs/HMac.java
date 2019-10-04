/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Hashtable
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.macs;

import java.util.Hashtable;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Memoable;

public class HMac
implements Mac {
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private static Hashtable blockLengths = new Hashtable();
    private int blockLength;
    private Digest digest;
    private int digestSize;
    private byte[] inputPad;
    private Memoable ipadState;
    private Memoable opadState;
    private byte[] outputBuf;

    static {
        blockLengths.put((Object)"GOST3411", (Object)Integers.valueOf((int)32));
        blockLengths.put((Object)"MD2", (Object)Integers.valueOf((int)16));
        blockLengths.put((Object)"MD4", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"MD5", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"RIPEMD128", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"RIPEMD160", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"SHA-1", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"SHA-224", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"SHA-256", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"SHA-384", (Object)Integers.valueOf((int)128));
        blockLengths.put((Object)"SHA-512", (Object)Integers.valueOf((int)128));
        blockLengths.put((Object)"Tiger", (Object)Integers.valueOf((int)64));
        blockLengths.put((Object)"Whirlpool", (Object)Integers.valueOf((int)64));
    }

    public HMac(Digest digest) {
        this(digest, HMac.getByteLength(digest));
    }

    private HMac(Digest digest, int n2) {
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
        this.blockLength = n2;
        this.inputPad = new byte[this.blockLength];
        this.outputBuf = new byte[this.blockLength + this.digestSize];
    }

    private static int getByteLength(Digest digest) {
        if (digest instanceof ExtendedDigest) {
            return ((ExtendedDigest)digest).getByteLength();
        }
        Integer n2 = (Integer)blockLengths.get((Object)digest.getAlgorithmName());
        if (n2 == null) {
            throw new IllegalArgumentException("unknown digest passed: " + digest.getAlgorithmName());
        }
        return n2;
    }

    private static void xorPad(byte[] arrby, int n2, byte by) {
        for (int i2 = 0; i2 < n2; ++i2) {
            arrby[i2] = (byte)(by ^ arrby[i2]);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int doFinal(byte[] arrby, int n2) {
        this.digest.doFinal(this.outputBuf, this.blockLength);
        if (this.opadState != null) {
            ((Memoable)((Object)this.digest)).reset(this.opadState);
            this.digest.update(this.outputBuf, this.blockLength, this.digest.getDigestSize());
        } else {
            this.digest.update(this.outputBuf, 0, this.outputBuf.length);
        }
        int n3 = this.digest.doFinal(arrby, n2);
        for (int i2 = this.blockLength; i2 < this.outputBuf.length; ++i2) {
            this.outputBuf[i2] = 0;
        }
        if (this.ipadState != null) {
            ((Memoable)((Object)this.digest)).reset(this.ipadState);
            return n3;
        }
        this.digest.update(this.inputPad, 0, this.inputPad.length);
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "/HMAC";
    }

    @Override
    public int getMacSize() {
        return this.digestSize;
    }

    public Digest getUnderlyingDigest() {
        return this.digest;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(CipherParameters cipherParameters) {
        this.digest.reset();
        byte[] arrby = ((KeyParameter)cipherParameters).getKey();
        int n2 = arrby.length;
        if (n2 > this.blockLength) {
            this.digest.update(arrby, 0, n2);
            this.digest.doFinal(this.inputPad, 0);
            n2 = this.digestSize;
        } else {
            System.arraycopy((Object)arrby, (int)0, (Object)this.inputPad, (int)0, (int)n2);
        }
        while (n2 < this.inputPad.length) {
            this.inputPad[n2] = 0;
            ++n2;
        }
        System.arraycopy((Object)this.inputPad, (int)0, (Object)this.outputBuf, (int)0, (int)this.blockLength);
        HMac.xorPad(this.inputPad, this.blockLength, (byte)54);
        HMac.xorPad(this.outputBuf, this.blockLength, (byte)92);
        if (this.digest instanceof Memoable) {
            this.opadState = ((Memoable)((Object)this.digest)).copy();
            ((Digest)((Object)this.opadState)).update(this.outputBuf, 0, this.blockLength);
        }
        this.digest.update(this.inputPad, 0, this.inputPad.length);
        if (this.digest instanceof Memoable) {
            this.ipadState = ((Memoable)((Object)this.digest)).copy();
        }
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }
}

