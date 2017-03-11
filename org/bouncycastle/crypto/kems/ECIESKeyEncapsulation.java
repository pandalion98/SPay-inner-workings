package org.bouncycastle.crypto.kems;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.KeyEncapsulation;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class ECIESKeyEncapsulation implements KeyEncapsulation {
    private static final BigInteger ONE;
    private boolean CofactorMode;
    private boolean OldCofactorMode;
    private boolean SingleHashMode;
    private DerivationFunction kdf;
    private ECKeyParameters key;
    private SecureRandom rnd;

    static {
        ONE = BigInteger.valueOf(1);
    }

    public ECIESKeyEncapsulation(DerivationFunction derivationFunction, SecureRandom secureRandom) {
        this.kdf = derivationFunction;
        this.rnd = secureRandom;
        this.CofactorMode = false;
        this.OldCofactorMode = false;
        this.SingleHashMode = false;
    }

    public ECIESKeyEncapsulation(DerivationFunction derivationFunction, SecureRandom secureRandom, boolean z, boolean z2, boolean z3) {
        this.kdf = derivationFunction;
        this.rnd = secureRandom;
        this.CofactorMode = z;
        this.OldCofactorMode = z2;
        this.SingleHashMode = z3;
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    public CipherParameters decrypt(byte[] bArr, int i) {
        return decrypt(bArr, 0, bArr.length, i);
    }

    public CipherParameters decrypt(byte[] bArr, int i, int i2, int i3) {
        if (this.key instanceof ECPrivateKeyParameters) {
            ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters) this.key;
            ECDomainParameters parameters = eCPrivateKeyParameters.getParameters();
            ECCurve curve = parameters.getCurve();
            BigInteger n = parameters.getN();
            BigInteger h = parameters.getH();
            Object obj = new byte[i2];
            System.arraycopy(bArr, i, obj, 0, i2);
            ECPoint decodePoint = curve.decodePoint(obj);
            if (this.CofactorMode || this.OldCofactorMode) {
                decodePoint = decodePoint.multiply(h);
            }
            BigInteger d = eCPrivateKeyParameters.getD();
            if (this.CofactorMode) {
                d = d.multiply(h.modInverse(n)).mod(n);
            }
            return deriveKey(i3, obj, decodePoint.multiply(d).normalize().getAffineXCoord().getEncoded());
        }
        throw new IllegalArgumentException("Private key required for encryption");
    }

    protected KeyParameter deriveKey(int i, byte[] bArr, byte[] bArr2) {
        if (this.SingleHashMode) {
            byte[] concatenate = Arrays.concatenate(bArr, bArr2);
            Arrays.fill(bArr2, (byte) 0);
            bArr2 = concatenate;
        }
        try {
            this.kdf.init(new KDFParameters(bArr2, null));
            concatenate = new byte[i];
            this.kdf.generateBytes(concatenate, 0, concatenate.length);
            KeyParameter keyParameter = new KeyParameter(concatenate);
            return keyParameter;
        } finally {
            Arrays.fill(bArr2, (byte) 0);
        }
    }

    public CipherParameters encrypt(byte[] bArr, int i) {
        return encrypt(bArr, 0, i);
    }

    public CipherParameters encrypt(byte[] bArr, int i, int i2) {
        if (this.key instanceof ECPublicKeyParameters) {
            ECDomainParameters parameters = ((ECPublicKeyParameters) this.key).getParameters();
            ECCurve curve = parameters.getCurve();
            BigInteger n = parameters.getN();
            BigInteger h = parameters.getH();
            BigInteger createRandomInRange = BigIntegers.createRandomInRange(ONE, n, this.rnd);
            n = this.CofactorMode ? createRandomInRange.multiply(h).mod(n) : createRandomInRange;
            ECPoint[] eCPointArr = new ECPoint[]{createBasePointMultiplier().multiply(parameters.getG(), createRandomInRange), r0.getQ().multiply(n)};
            curve.normalizeAll(eCPointArr);
            ECPoint eCPoint = eCPointArr[0];
            ECPoint eCPoint2 = eCPointArr[1];
            Object encoded = eCPoint.getEncoded(false);
            System.arraycopy(encoded, 0, bArr, i, encoded.length);
            return deriveKey(i2, encoded, eCPoint2.getAffineXCoord().getEncoded());
        }
        throw new IllegalArgumentException("Public key required for encryption");
    }

    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ECKeyParameters) {
            this.key = (ECKeyParameters) cipherParameters;
            return;
        }
        throw new IllegalArgumentException("EC key required");
    }
}
