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
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.prng.drbg;

import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;
import org.bouncycastle.crypto.prng.drbg.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class HashSP800DRBG
implements SP80090DRBG {
    private static final int MAX_BITS_REQUEST = 262144;
    private static final byte[] ONE = new byte[]{1};
    private static final long RESEED_MAX = 0x800000000000L;
    private static final Hashtable seedlens = new Hashtable();
    private byte[] _C;
    private byte[] _V;
    private Digest _digest;
    private EntropySource _entropySource;
    private long _reseedCounter;
    private int _securityStrength;
    private int _seedLength;

    static {
        seedlens.put((Object)"SHA-1", (Object)Integers.valueOf((int)440));
        seedlens.put((Object)"SHA-224", (Object)Integers.valueOf((int)440));
        seedlens.put((Object)"SHA-256", (Object)Integers.valueOf((int)440));
        seedlens.put((Object)"SHA-512/256", (Object)Integers.valueOf((int)440));
        seedlens.put((Object)"SHA-512/224", (Object)Integers.valueOf((int)440));
        seedlens.put((Object)"SHA-384", (Object)Integers.valueOf((int)888));
        seedlens.put((Object)"SHA-512", (Object)Integers.valueOf((int)888));
    }

    public HashSP800DRBG(Digest digest, int n2, EntropySource entropySource, byte[] arrby, byte[] arrby2) {
        if (n2 > Utils.getMaxSecurityStrength(digest)) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        }
        if (entropySource.entropySize() < n2) {
            throw new IllegalArgumentException("Not enough entropy for security strength required");
        }
        this._digest = digest;
        this._entropySource = entropySource;
        this._securityStrength = n2;
        this._seedLength = (Integer)seedlens.get((Object)digest.getAlgorithmName());
        byte[] arrby3 = Arrays.concatenate((byte[])entropySource.getEntropy(), (byte[])arrby2, (byte[])arrby);
        this._V = Utils.hash_df(this._digest, arrby3, this._seedLength);
        byte[] arrby4 = new byte[1 + this._V.length];
        System.arraycopy((Object)this._V, (int)0, (Object)arrby4, (int)1, (int)this._V.length);
        this._C = Utils.hash_df(this._digest, arrby4, this._seedLength);
        this._reseedCounter = 1L;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addTo(byte[] arrby, byte[] arrby2) {
        int n2 = 0;
        for (int i2 = 1; i2 <= arrby2.length; ++i2) {
            int n3 = n2 + ((255 & arrby[arrby.length - i2]) + (255 & arrby2[arrby2.length - i2]));
            n2 = n3 > 255 ? 1 : 0;
            arrby[arrby.length - i2] = (byte)n3;
        }
        int n4 = 1 + arrby2.length;
        while (n4 <= arrby.length) {
            int n5 = n2 + (255 & arrby[arrby.length - n4]);
            n2 = n5 > 255 ? 1 : 0;
            arrby[arrby.length - n4] = (byte)n5;
            ++n4;
        }
        return;
    }

    private void doHash(byte[] arrby, byte[] arrby2) {
        this._digest.update(arrby, 0, arrby.length);
        this._digest.doFinal(arrby2, 0);
    }

    private byte[] hash(byte[] arrby) {
        byte[] arrby2 = new byte[this._digest.getDigestSize()];
        this.doHash(arrby, arrby2);
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] hashgen(byte[] arrby, int n2) {
        int n3 = this._digest.getDigestSize();
        int n4 = n2 / 8 / n3;
        byte[] arrby2 = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        byte[] arrby3 = new byte[n2 / 8];
        byte[] arrby4 = new byte[this._digest.getDigestSize()];
        int n5 = 0;
        while (n5 <= n4) {
            this.doHash(arrby2, arrby4);
            int n6 = arrby3.length - n5 * arrby4.length > arrby4.length ? arrby4.length : arrby3.length - n5 * arrby4.length;
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)(n5 * arrby4.length), (int)n6);
            this.addTo(arrby2, ONE);
            ++n5;
        }
        return arrby3;
    }

    @Override
    public int generate(byte[] arrby, byte[] arrby2, boolean bl) {
        int n2 = 8 * arrby.length;
        if (n2 > 262144) {
            throw new IllegalArgumentException("Number of bits per request limited to 262144");
        }
        if (this._reseedCounter > 0x800000000000L) {
            return -1;
        }
        if (bl) {
            this.reseed(arrby2);
            arrby2 = null;
        }
        if (arrby2 != null) {
            byte[] arrby3 = new byte[1 + this._V.length + arrby2.length];
            arrby3[0] = 2;
            System.arraycopy((Object)this._V, (int)0, (Object)arrby3, (int)1, (int)this._V.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)(1 + this._V.length), (int)arrby2.length);
            byte[] arrby4 = this.hash(arrby3);
            this.addTo(this._V, arrby4);
        }
        byte[] arrby5 = this.hashgen(this._V, n2);
        byte[] arrby6 = new byte[1 + this._V.length];
        System.arraycopy((Object)this._V, (int)0, (Object)arrby6, (int)1, (int)this._V.length);
        arrby6[0] = 3;
        byte[] arrby7 = this.hash(arrby6);
        this.addTo(this._V, arrby7);
        this.addTo(this._V, this._C);
        byte[] arrby8 = new byte[]{(byte)(this._reseedCounter >> 24), (byte)(this._reseedCounter >> 16), (byte)(this._reseedCounter >> 8), (byte)this._reseedCounter};
        this.addTo(this._V, arrby8);
        this._reseedCounter = 1L + this._reseedCounter;
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby, (int)0, (int)arrby.length);
        return n2;
    }

    @Override
    public int getBlockSize() {
        return 8 * this._digest.getDigestSize();
    }

    @Override
    public void reseed(byte[] arrby) {
        byte[] arrby2 = this._entropySource.getEntropy();
        byte[] arrby3 = Arrays.concatenate((byte[])ONE, (byte[])this._V, (byte[])arrby2, (byte[])arrby);
        this._V = Utils.hash_df(this._digest, arrby3, this._seedLength);
        byte[] arrby4 = new byte[1 + this._V.length];
        arrby4[0] = 0;
        System.arraycopy((Object)this._V, (int)0, (Object)arrby4, (int)1, (int)this._V.length);
        this._C = Utils.hash_df(this._digest, arrby4, this._seedLength);
        this._reseedCounter = 1L;
    }
}

