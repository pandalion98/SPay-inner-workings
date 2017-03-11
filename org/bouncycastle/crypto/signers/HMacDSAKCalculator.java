package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class HMacDSAKCalculator implements DSAKCalculator {
    private static final BigInteger ZERO;
    private final byte[] f260K;
    private final byte[] f261V;
    private final HMac hMac;
    private BigInteger f262n;

    static {
        ZERO = BigInteger.valueOf(0);
    }

    public HMacDSAKCalculator(Digest digest) {
        this.hMac = new HMac(digest);
        this.f261V = new byte[this.hMac.getMacSize()];
        this.f260K = new byte[this.hMac.getMacSize()];
    }

    private BigInteger bitsToInt(byte[] bArr) {
        BigInteger bigInteger = new BigInteger(1, bArr);
        return bArr.length * 8 > this.f262n.bitLength() ? bigInteger.shiftRight((bArr.length * 8) - this.f262n.bitLength()) : bigInteger;
    }

    public void init(BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr) {
        this.f262n = bigInteger;
        Arrays.fill(this.f261V, (byte) 1);
        Arrays.fill(this.f260K, (byte) 0);
        Object obj = new byte[((bigInteger.bitLength() + 7) / 8)];
        Object asUnsignedByteArray = BigIntegers.asUnsignedByteArray(bigInteger2);
        System.arraycopy(asUnsignedByteArray, 0, obj, obj.length - asUnsignedByteArray.length, asUnsignedByteArray.length);
        Object obj2 = new byte[((bigInteger.bitLength() + 7) / 8)];
        BigInteger bitsToInt = bitsToInt(bArr);
        if (bitsToInt.compareTo(bigInteger) >= 0) {
            bitsToInt = bitsToInt.subtract(bigInteger);
        }
        asUnsignedByteArray = BigIntegers.asUnsignedByteArray(bitsToInt);
        System.arraycopy(asUnsignedByteArray, 0, obj2, obj2.length - asUnsignedByteArray.length, asUnsignedByteArray.length);
        this.hMac.init(new KeyParameter(this.f260K));
        this.hMac.update(this.f261V, 0, this.f261V.length);
        this.hMac.update((byte) 0);
        this.hMac.update(obj, 0, obj.length);
        this.hMac.update(obj2, 0, obj2.length);
        this.hMac.doFinal(this.f260K, 0);
        this.hMac.init(new KeyParameter(this.f260K));
        this.hMac.update(this.f261V, 0, this.f261V.length);
        this.hMac.doFinal(this.f261V, 0);
        this.hMac.update(this.f261V, 0, this.f261V.length);
        this.hMac.update((byte) 1);
        this.hMac.update(obj, 0, obj.length);
        this.hMac.update(obj2, 0, obj2.length);
        this.hMac.doFinal(this.f260K, 0);
        this.hMac.init(new KeyParameter(this.f260K));
        this.hMac.update(this.f261V, 0, this.f261V.length);
        this.hMac.doFinal(this.f261V, 0);
    }

    public void init(BigInteger bigInteger, SecureRandom secureRandom) {
        throw new IllegalStateException("Operation not supported");
    }

    public boolean isDeterministic() {
        return true;
    }

    public BigInteger nextK() {
        Object obj = new byte[((this.f262n.bitLength() + 7) / 8)];
        while (true) {
            int i = 0;
            while (i < obj.length) {
                this.hMac.update(this.f261V, 0, this.f261V.length);
                this.hMac.doFinal(this.f261V, 0);
                int min = Math.min(obj.length - i, this.f261V.length);
                System.arraycopy(this.f261V, 0, obj, i, min);
                i += min;
            }
            BigInteger bitsToInt = bitsToInt(obj);
            if (bitsToInt.compareTo(ZERO) > 0 && bitsToInt.compareTo(this.f262n) < 0) {
                return bitsToInt;
            }
            this.hMac.update(this.f261V, 0, this.f261V.length);
            this.hMac.update((byte) 0);
            this.hMac.doFinal(this.f260K, 0);
            this.hMac.init(new KeyParameter(this.f260K));
            this.hMac.update(this.f261V, 0, this.f261V.length);
            this.hMac.doFinal(this.f261V, 0);
        }
    }
}
