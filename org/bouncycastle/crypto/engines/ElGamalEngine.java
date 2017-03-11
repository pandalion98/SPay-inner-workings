package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ElGamalKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.BigIntegers;

public class ElGamalEngine implements AsymmetricBlockCipher {
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private int bitSize;
    private boolean forEncryption;
    private ElGamalKeyParameters key;
    private SecureRandom random;

    static {
        ZERO = BigInteger.valueOf(0);
        ONE = BigInteger.valueOf(1);
        TWO = BigInteger.valueOf(2);
    }

    public int getInputBlockSize() {
        return this.forEncryption ? (this.bitSize - 1) / 8 : ((this.bitSize + 7) / 8) * 2;
    }

    public int getOutputBlockSize() {
        return this.forEncryption ? ((this.bitSize + 7) / 8) * 2 : (this.bitSize - 1) / 8;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.key = (ElGamalKeyParameters) parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        } else {
            this.key = (ElGamalKeyParameters) cipherParameters;
            this.random = new SecureRandom();
        }
        this.forEncryption = z;
        this.bitSize = this.key.getParameters().getP().bitLength();
        if (z) {
            if (!(this.key instanceof ElGamalPublicKeyParameters)) {
                throw new IllegalArgumentException("ElGamalPublicKeyParameters are required for encryption.");
            }
        } else if (!(this.key instanceof ElGamalPrivateKeyParameters)) {
            throw new IllegalArgumentException("ElGamalPrivateKeyParameters are required for decryption.");
        }
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        if (this.key == null) {
            throw new IllegalStateException("ElGamal engine not initialised");
        }
        if (i2 > (this.forEncryption ? ((this.bitSize - 1) + 7) / 8 : getInputBlockSize())) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        BigInteger p = this.key.getParameters().getP();
        if (this.key instanceof ElGamalPrivateKeyParameters) {
            Object obj = new byte[(i2 / 2)];
            Object obj2 = new byte[(i2 / 2)];
            System.arraycopy(bArr, i, obj, 0, obj.length);
            System.arraycopy(bArr, obj.length + i, obj2, 0, obj2.length);
            return BigIntegers.asUnsignedByteArray(new BigInteger(1, obj).modPow(p.subtract(ONE).subtract(((ElGamalPrivateKeyParameters) this.key).getX()), p).multiply(new BigInteger(1, obj2)).mod(p));
        }
        if (!(i == 0 && i2 == bArr.length)) {
            obj = new byte[i2];
            System.arraycopy(bArr, i, obj, 0, i2);
            bArr = obj;
        }
        BigInteger bigInteger = new BigInteger(1, bArr);
        if (bigInteger.compareTo(p) >= 0) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        ElGamalPublicKeyParameters elGamalPublicKeyParameters = (ElGamalPublicKeyParameters) this.key;
        int bitLength = p.bitLength();
        BigInteger bigInteger2 = new BigInteger(bitLength, this.random);
        while (true) {
            if (!bigInteger2.equals(ZERO) && bigInteger2.compareTo(p.subtract(TWO)) <= 0) {
                break;
            }
            bigInteger2 = new BigInteger(bitLength, this.random);
        }
        BigInteger modPow = this.key.getParameters().getG().modPow(bigInteger2, p);
        BigInteger mod = bigInteger.multiply(elGamalPublicKeyParameters.getY().modPow(bigInteger2, p)).mod(p);
        obj2 = modPow.toByteArray();
        Object toByteArray = mod.toByteArray();
        obj = new byte[getOutputBlockSize()];
        if (obj2.length > obj.length / 2) {
            System.arraycopy(obj2, 1, obj, (obj.length / 2) - (obj2.length - 1), obj2.length - 1);
        } else {
            System.arraycopy(obj2, 0, obj, (obj.length / 2) - obj2.length, obj2.length);
        }
        if (toByteArray.length > obj.length / 2) {
            System.arraycopy(toByteArray, 1, obj, obj.length - (toByteArray.length - 1), toByteArray.length - 1);
            return obj;
        }
        System.arraycopy(toByteArray, 0, obj, obj.length - toByteArray.length, toByteArray.length);
        return obj;
    }
}
