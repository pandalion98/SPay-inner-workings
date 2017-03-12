package org.bouncycastle.crypto.prng.drbg;

import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.util.Arrays;

public class HMacSP800DRBG implements SP80090DRBG {
    private static final int MAX_BITS_REQUEST = 262144;
    private static final long RESEED_MAX = 140737488355328L;
    private byte[] _K;
    private byte[] _V;
    private EntropySource _entropySource;
    private Mac _hMac;
    private long _reseedCounter;

    public HMacSP800DRBG(Mac mac, int i, EntropySource entropySource, byte[] bArr, byte[] bArr2) {
        if (i > Utils.getMaxSecurityStrength(mac)) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        } else if (entropySource.entropySize() < i) {
            throw new IllegalArgumentException("Not enough entropy for security strength required");
        } else {
            this._entropySource = entropySource;
            this._hMac = mac;
            byte[] concatenate = Arrays.concatenate(entropySource.getEntropy(), bArr2, bArr);
            this._K = new byte[mac.getMacSize()];
            this._V = new byte[this._K.length];
            Arrays.fill(this._V, (byte) 1);
            hmac_DRBG_Update(concatenate);
            this._reseedCounter = 1;
        }
    }

    private void hmac_DRBG_Update(byte[] bArr) {
        hmac_DRBG_Update_Func(bArr, (byte) 0);
        if (bArr != null) {
            hmac_DRBG_Update_Func(bArr, (byte) 1);
        }
    }

    private void hmac_DRBG_Update_Func(byte[] bArr, byte b) {
        this._hMac.init(new KeyParameter(this._K));
        this._hMac.update(this._V, 0, this._V.length);
        this._hMac.update(b);
        if (bArr != null) {
            this._hMac.update(bArr, 0, bArr.length);
        }
        this._hMac.doFinal(this._K, 0);
        this._hMac.init(new KeyParameter(this._K));
        this._hMac.update(this._V, 0, this._V.length);
        this._hMac.doFinal(this._V, 0);
    }

    public int generate(byte[] bArr, byte[] bArr2, boolean z) {
        int length = bArr.length * 8;
        if (length > MAX_BITS_REQUEST) {
            throw new IllegalArgumentException("Number of bits per request limited to 262144");
        } else if (this._reseedCounter > RESEED_MAX) {
            return -1;
        } else {
            if (z) {
                reseed(bArr2);
                bArr2 = null;
            }
            if (bArr2 != null) {
                hmac_DRBG_Update(bArr2);
            }
            Object obj = new byte[bArr.length];
            int length2 = bArr.length / this._V.length;
            this._hMac.init(new KeyParameter(this._K));
            for (int i = 0; i < length2; i++) {
                this._hMac.update(this._V, 0, this._V.length);
                this._hMac.doFinal(this._V, 0);
                System.arraycopy(this._V, 0, obj, this._V.length * i, this._V.length);
            }
            if (this._V.length * length2 < obj.length) {
                this._hMac.update(this._V, 0, this._V.length);
                this._hMac.doFinal(this._V, 0);
                System.arraycopy(this._V, 0, obj, this._V.length * length2, obj.length - (length2 * this._V.length));
            }
            hmac_DRBG_Update(bArr2);
            this._reseedCounter++;
            System.arraycopy(obj, 0, bArr, 0, bArr.length);
            return length;
        }
    }

    public int getBlockSize() {
        return this._V.length * 8;
    }

    public void reseed(byte[] bArr) {
        hmac_DRBG_Update(Arrays.concatenate(this._entropySource.getEntropy(), bArr));
        this._reseedCounter = 1;
    }
}
