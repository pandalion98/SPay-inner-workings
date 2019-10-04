/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.prng.drbg;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;
import org.bouncycastle.crypto.prng.drbg.Utils;
import org.bouncycastle.util.Arrays;

public class HMacSP800DRBG
implements SP80090DRBG {
    private static final int MAX_BITS_REQUEST = 262144;
    private static final long RESEED_MAX = 0x800000000000L;
    private byte[] _K;
    private byte[] _V;
    private EntropySource _entropySource;
    private Mac _hMac;
    private long _reseedCounter;

    public HMacSP800DRBG(Mac mac, int n2, EntropySource entropySource, byte[] arrby, byte[] arrby2) {
        if (n2 > Utils.getMaxSecurityStrength(mac)) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        }
        if (entropySource.entropySize() < n2) {
            throw new IllegalArgumentException("Not enough entropy for security strength required");
        }
        this._entropySource = entropySource;
        this._hMac = mac;
        byte[] arrby3 = Arrays.concatenate((byte[])entropySource.getEntropy(), (byte[])arrby2, (byte[])arrby);
        this._K = new byte[mac.getMacSize()];
        this._V = new byte[this._K.length];
        Arrays.fill((byte[])this._V, (byte)1);
        this.hmac_DRBG_Update(arrby3);
        this._reseedCounter = 1L;
    }

    private void hmac_DRBG_Update(byte[] arrby) {
        this.hmac_DRBG_Update_Func(arrby, (byte)0);
        if (arrby != null) {
            this.hmac_DRBG_Update_Func(arrby, (byte)1);
        }
    }

    private void hmac_DRBG_Update_Func(byte[] arrby, byte by) {
        this._hMac.init(new KeyParameter(this._K));
        this._hMac.update(this._V, 0, this._V.length);
        this._hMac.update(by);
        if (arrby != null) {
            this._hMac.update(arrby, 0, arrby.length);
        }
        this._hMac.doFinal(this._K, 0);
        this._hMac.init(new KeyParameter(this._K));
        this._hMac.update(this._V, 0, this._V.length);
        this._hMac.doFinal(this._V, 0);
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
            this.hmac_DRBG_Update(arrby2);
        }
        byte[] arrby3 = new byte[arrby.length];
        int n3 = arrby.length / this._V.length;
        this._hMac.init(new KeyParameter(this._K));
        for (int i2 = 0; i2 < n3; ++i2) {
            this._hMac.update(this._V, 0, this._V.length);
            this._hMac.doFinal(this._V, 0);
            System.arraycopy((Object)this._V, (int)0, (Object)arrby3, (int)(i2 * this._V.length), (int)this._V.length);
        }
        if (n3 * this._V.length < arrby3.length) {
            this._hMac.update(this._V, 0, this._V.length);
            this._hMac.doFinal(this._V, 0);
            System.arraycopy((Object)this._V, (int)0, (Object)arrby3, (int)(n3 * this._V.length), (int)(arrby3.length - n3 * this._V.length));
        }
        this.hmac_DRBG_Update(arrby2);
        this._reseedCounter = 1L + this._reseedCounter;
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby, (int)0, (int)arrby.length);
        return n2;
    }

    @Override
    public int getBlockSize() {
        return 8 * this._V.length;
    }

    @Override
    public void reseed(byte[] arrby) {
        this.hmac_DRBG_Update(Arrays.concatenate((byte[])this._entropySource.getEntropy(), (byte[])arrby));
        this._reseedCounter = 1L;
    }
}

