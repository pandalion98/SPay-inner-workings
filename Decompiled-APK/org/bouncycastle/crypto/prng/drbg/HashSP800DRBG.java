package org.bouncycastle.crypto.prng.drbg;

import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class HashSP800DRBG implements SP80090DRBG {
    private static final int MAX_BITS_REQUEST = 262144;
    private static final byte[] ONE;
    private static final long RESEED_MAX = 140737488355328L;
    private static final Hashtable seedlens;
    private byte[] _C;
    private byte[] _V;
    private Digest _digest;
    private EntropySource _entropySource;
    private long _reseedCounter;
    private int _securityStrength;
    private int _seedLength;

    static {
        ONE = new byte[]{(byte) 1};
        seedlens = new Hashtable();
        seedlens.put("SHA-1", Integers.valueOf(440));
        seedlens.put("SHA-224", Integers.valueOf(440));
        seedlens.put("SHA-256", Integers.valueOf(440));
        seedlens.put("SHA-512/256", Integers.valueOf(440));
        seedlens.put("SHA-512/224", Integers.valueOf(440));
        seedlens.put("SHA-384", Integers.valueOf(888));
        seedlens.put("SHA-512", Integers.valueOf(888));
    }

    public HashSP800DRBG(Digest digest, int i, EntropySource entropySource, byte[] bArr, byte[] bArr2) {
        if (i > Utils.getMaxSecurityStrength(digest)) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        } else if (entropySource.entropySize() < i) {
            throw new IllegalArgumentException("Not enough entropy for security strength required");
        } else {
            this._digest = digest;
            this._entropySource = entropySource;
            this._securityStrength = i;
            this._seedLength = ((Integer) seedlens.get(digest.getAlgorithmName())).intValue();
            this._V = Utils.hash_df(this._digest, Arrays.concatenate(entropySource.getEntropy(), bArr2, bArr), this._seedLength);
            Object obj = new byte[(this._V.length + 1)];
            System.arraycopy(this._V, 0, obj, 1, this._V.length);
            this._C = Utils.hash_df(this._digest, obj, this._seedLength);
            this._reseedCounter = 1;
        }
    }

    private void addTo(byte[] bArr, byte[] bArr2) {
        int i;
        int i2 = 0;
        for (i = 1; i <= bArr2.length; i++) {
            int i3 = ((bArr[bArr.length - i] & GF2Field.MASK) + (bArr2[bArr2.length - i] & GF2Field.MASK)) + i2;
            i2 = i3 > GF2Field.MASK ? 1 : 0;
            bArr[bArr.length - i] = (byte) i3;
        }
        for (i = bArr2.length + 1; i <= bArr.length; i++) {
            i3 = (bArr[bArr.length - i] & GF2Field.MASK) + i2;
            i2 = i3 > GF2Field.MASK ? 1 : 0;
            bArr[bArr.length - i] = (byte) i3;
        }
    }

    private void doHash(byte[] bArr, byte[] bArr2) {
        this._digest.update(bArr, 0, bArr.length);
        this._digest.doFinal(bArr2, 0);
    }

    private byte[] hash(byte[] bArr) {
        byte[] bArr2 = new byte[this._digest.getDigestSize()];
        doHash(bArr, bArr2);
        return bArr2;
    }

    private byte[] hashgen(byte[] bArr, int i) {
        int digestSize = (i / 8) / this._digest.getDigestSize();
        Object obj = new byte[bArr.length];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        Object obj2 = new byte[(i / 8)];
        Object obj3 = new byte[this._digest.getDigestSize()];
        for (int i2 = 0; i2 <= digestSize; i2++) {
            doHash(obj, obj3);
            System.arraycopy(obj3, 0, obj2, obj3.length * i2, obj2.length - (obj3.length * i2) > obj3.length ? obj3.length : obj2.length - (obj3.length * i2));
            addTo(obj, ONE);
        }
        return obj2;
    }

    public int generate(byte[] bArr, byte[] bArr2, boolean z) {
        int length = bArr.length * 8;
        if (length > MAX_BITS_REQUEST) {
            throw new IllegalArgumentException("Number of bits per request limited to 262144");
        } else if (this._reseedCounter > RESEED_MAX) {
            return -1;
        } else {
            Object obj;
            Object obj2;
            if (z) {
                reseed(bArr2);
                obj = null;
            }
            if (obj != null) {
                obj2 = new byte[((this._V.length + 1) + obj.length)];
                obj2[0] = 2;
                System.arraycopy(this._V, 0, obj2, 1, this._V.length);
                System.arraycopy(obj, 0, obj2, this._V.length + 1, obj.length);
                addTo(this._V, hash(obj2));
            }
            obj2 = hashgen(this._V, length);
            Object obj3 = new byte[(this._V.length + 1)];
            System.arraycopy(this._V, 0, obj3, 1, this._V.length);
            obj3[0] = 3;
            addTo(this._V, hash(obj3));
            addTo(this._V, this._C);
            addTo(this._V, new byte[]{(byte) ((int) (this._reseedCounter >> 24)), (byte) ((int) (this._reseedCounter >> 16)), (byte) ((int) (this._reseedCounter >> 8)), (byte) ((int) this._reseedCounter)});
            this._reseedCounter++;
            System.arraycopy(obj2, 0, bArr, 0, bArr.length);
            return length;
        }
    }

    public int getBlockSize() {
        return this._digest.getDigestSize() * 8;
    }

    public void reseed(byte[] bArr) {
        this._V = Utils.hash_df(this._digest, Arrays.concatenate(ONE, this._V, this._entropySource.getEntropy(), bArr), this._seedLength);
        Object obj = new byte[(this._V.length + 1)];
        obj[0] = null;
        System.arraycopy(this._V, 0, obj, 1, this._V.length);
        this._C = Utils.hash_df(this._digest, obj, this._seedLength);
        this._reseedCounter = 1;
    }
}
