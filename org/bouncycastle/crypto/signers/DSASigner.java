package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class DSASigner implements DSA {
    private final DSAKCalculator kCalculator;
    private DSAKeyParameters key;
    private SecureRandom random;

    public DSASigner() {
        this.kCalculator = new RandomDSAKCalculator();
    }

    public DSASigner(DSAKCalculator dSAKCalculator) {
        this.kCalculator = dSAKCalculator;
    }

    private BigInteger calculateE(BigInteger bigInteger, byte[] bArr) {
        if (bigInteger.bitLength() >= bArr.length * 8) {
            return new BigInteger(1, bArr);
        }
        Object obj = new byte[(bigInteger.bitLength() / 8)];
        System.arraycopy(bArr, 0, obj, 0, obj.length);
        return new BigInteger(1, obj);
    }

    public BigInteger[] generateSignature(byte[] bArr) {
        DSAParameters parameters = this.key.getParameters();
        BigInteger q = parameters.getQ();
        BigInteger calculateE = calculateE(q, bArr);
        BigInteger x = ((DSAPrivateKeyParameters) this.key).getX();
        if (this.kCalculator.isDeterministic()) {
            this.kCalculator.init(q, x, bArr);
        } else {
            this.kCalculator.init(q, this.random);
        }
        BigInteger nextK = this.kCalculator.nextK();
        x = nextK.modInverse(q).multiply(calculateE.add(x.multiply(parameters.getG().modPow(nextK, parameters.getP()).mod(q)))).mod(q);
        return new BigInteger[]{r1, x};
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        SecureRandom secureRandom;
        if (!z) {
            this.key = (DSAPublicKeyParameters) cipherParameters;
            secureRandom = null;
        } else if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.key = (DSAPrivateKeyParameters) parametersWithRandom.getParameters();
            secureRandom = parametersWithRandom.getRandom();
        } else {
            this.key = (DSAPrivateKeyParameters) cipherParameters;
            secureRandom = null;
        }
        boolean z2 = z && !this.kCalculator.isDeterministic();
        this.random = initSecureRandom(z2, secureRandom);
    }

    protected SecureRandom initSecureRandom(boolean z, SecureRandom secureRandom) {
        return !z ? null : secureRandom == null ? new SecureRandom() : secureRandom;
    }

    public boolean verifySignature(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2) {
        DSAParameters parameters = this.key.getParameters();
        BigInteger q = parameters.getQ();
        BigInteger calculateE = calculateE(q, bArr);
        BigInteger valueOf = BigInteger.valueOf(0);
        if (valueOf.compareTo(bigInteger) >= 0 || q.compareTo(bigInteger) <= 0 || valueOf.compareTo(bigInteger2) >= 0 || q.compareTo(bigInteger2) <= 0) {
            return false;
        }
        BigInteger modInverse = bigInteger2.modInverse(q);
        calculateE = calculateE.multiply(modInverse).mod(q);
        valueOf = bigInteger.multiply(modInverse).mod(q);
        BigInteger p = parameters.getP();
        return parameters.getG().modPow(calculateE, p).multiply(((DSAPublicKeyParameters) this.key).getY().modPow(valueOf, p)).mod(p).mod(q).equals(bigInteger);
    }
}
