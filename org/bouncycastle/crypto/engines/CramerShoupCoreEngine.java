package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.CramerShoupKeyParameters;
import org.bouncycastle.crypto.params.CramerShoupPrivateKeyParameters;
import org.bouncycastle.crypto.params.CramerShoupPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.BigIntegers;

public class CramerShoupCoreEngine {
    private static final BigInteger ONE;
    private boolean forEncryption;
    private CramerShoupKeyParameters key;
    private String label;
    private SecureRandom random;

    public static class CramerShoupCiphertextException extends Exception {
        private static final long serialVersionUID = -6360977166495345076L;

        public CramerShoupCiphertextException(String str) {
            super(str);
        }
    }

    static {
        ONE = BigInteger.valueOf(1);
    }

    public CramerShoupCoreEngine() {
        this.label = null;
    }

    private BigInteger generateRandomElement(BigInteger bigInteger, SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange(ONE, bigInteger.subtract(ONE), secureRandom);
    }

    private boolean isValidMessage(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger.compareTo(bigInteger2) < 0;
    }

    public BigInteger convertInput(byte[] bArr, int i, int i2) {
        if (i2 > getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        } else if (i2 == getInputBlockSize() + 1 && this.forEncryption) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        } else {
            if (!(i == 0 && i2 == bArr.length)) {
                Object obj = new byte[i2];
                System.arraycopy(bArr, i, obj, 0, i2);
                bArr = obj;
            }
            BigInteger bigInteger = new BigInteger(1, bArr);
            if (bigInteger.compareTo(this.key.getParameters().getP()) < 0) {
                return bigInteger;
            }
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        }
    }

    public byte[] convertOutput(BigInteger bigInteger) {
        Object toByteArray = bigInteger.toByteArray();
        Object obj;
        if (this.forEncryption) {
            if (toByteArray[0] == null) {
                obj = new byte[(toByteArray.length - 1)];
                System.arraycopy(toByteArray, 1, obj, 0, obj.length);
                return obj;
            }
        } else if (toByteArray[0] == null && toByteArray.length > getOutputBlockSize()) {
            obj = new byte[(toByteArray.length - 1)];
            System.arraycopy(toByteArray, 1, obj, 0, obj.length);
            return obj;
        } else if (toByteArray.length < getOutputBlockSize()) {
            obj = new byte[getOutputBlockSize()];
            System.arraycopy(toByteArray, 0, obj, obj.length - toByteArray.length, toByteArray.length);
            return obj;
        }
        return toByteArray;
    }

    public BigInteger decryptBlock(CramerShoupCiphertext cramerShoupCiphertext) {
        if (!this.key.isPrivate() || this.forEncryption || !(this.key instanceof CramerShoupPrivateKeyParameters)) {
            return null;
        }
        CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters = (CramerShoupPrivateKeyParameters) this.key;
        BigInteger p = cramerShoupPrivateKeyParameters.getParameters().getP();
        Digest h = cramerShoupPrivateKeyParameters.getParameters().getH();
        byte[] toByteArray = cramerShoupCiphertext.getU1().toByteArray();
        h.update(toByteArray, 0, toByteArray.length);
        toByteArray = cramerShoupCiphertext.getU2().toByteArray();
        h.update(toByteArray, 0, toByteArray.length);
        toByteArray = cramerShoupCiphertext.getE().toByteArray();
        h.update(toByteArray, 0, toByteArray.length);
        if (this.label != null) {
            toByteArray = this.label.getBytes();
            h.update(toByteArray, 0, toByteArray.length);
        }
        toByteArray = new byte[h.getDigestSize()];
        h.doFinal(toByteArray, 0);
        BigInteger bigInteger = new BigInteger(1, toByteArray);
        if (cramerShoupCiphertext.f152v.equals(cramerShoupCiphertext.u1.modPow(cramerShoupPrivateKeyParameters.getX1().add(cramerShoupPrivateKeyParameters.getY1().multiply(bigInteger)), p).multiply(cramerShoupCiphertext.u2.modPow(cramerShoupPrivateKeyParameters.getX2().add(cramerShoupPrivateKeyParameters.getY2().multiply(bigInteger)), p)).mod(p))) {
            return cramerShoupCiphertext.f151e.multiply(cramerShoupCiphertext.u1.modPow(cramerShoupPrivateKeyParameters.getZ(), p).modInverse(p)).mod(p);
        }
        throw new CramerShoupCiphertextException("Sorry, that ciphertext is not correct");
    }

    public CramerShoupCiphertext encryptBlock(BigInteger bigInteger) {
        CramerShoupCiphertext cramerShoupCiphertext;
        if (!this.key.isPrivate() && this.forEncryption && (this.key instanceof CramerShoupPublicKeyParameters)) {
            CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = (CramerShoupPublicKeyParameters) this.key;
            BigInteger p = cramerShoupPublicKeyParameters.getParameters().getP();
            BigInteger g1 = cramerShoupPublicKeyParameters.getParameters().getG1();
            BigInteger g2 = cramerShoupPublicKeyParameters.getParameters().getG2();
            BigInteger h = cramerShoupPublicKeyParameters.getH();
            if (!isValidMessage(bigInteger, p)) {
                return null;
            }
            BigInteger generateRandomElement = generateRandomElement(p, this.random);
            g1 = g1.modPow(generateRandomElement, p);
            g2 = g2.modPow(generateRandomElement, p);
            h = h.modPow(generateRandomElement, p).multiply(bigInteger).mod(p);
            Digest h2 = cramerShoupPublicKeyParameters.getParameters().getH();
            byte[] toByteArray = g1.toByteArray();
            h2.update(toByteArray, 0, toByteArray.length);
            toByteArray = g2.toByteArray();
            h2.update(toByteArray, 0, toByteArray.length);
            toByteArray = h.toByteArray();
            h2.update(toByteArray, 0, toByteArray.length);
            if (this.label != null) {
                toByteArray = this.label.getBytes();
                h2.update(toByteArray, 0, toByteArray.length);
            }
            toByteArray = new byte[h2.getDigestSize()];
            h2.doFinal(toByteArray, 0);
            cramerShoupCiphertext = new CramerShoupCiphertext(g1, g2, h, cramerShoupPublicKeyParameters.getC().modPow(generateRandomElement, p).multiply(cramerShoupPublicKeyParameters.getD().modPow(generateRandomElement.multiply(new BigInteger(1, toByteArray)), p)).mod(p));
        } else {
            cramerShoupCiphertext = null;
        }
        return cramerShoupCiphertext;
    }

    public int getInputBlockSize() {
        int bitLength = this.key.getParameters().getP().bitLength();
        return this.forEncryption ? ((bitLength + 7) / 8) - 1 : (bitLength + 7) / 8;
    }

    public int getOutputBlockSize() {
        int bitLength = this.key.getParameters().getP().bitLength();
        return this.forEncryption ? (bitLength + 7) / 8 : ((bitLength + 7) / 8) - 1;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        SecureRandom secureRandom = null;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.key = (CramerShoupKeyParameters) parametersWithRandom.getParameters();
            secureRandom = parametersWithRandom.getRandom();
        } else {
            this.key = (CramerShoupKeyParameters) cipherParameters;
        }
        this.random = initSecureRandom(z, secureRandom);
        this.forEncryption = z;
    }

    public void init(boolean z, CipherParameters cipherParameters, String str) {
        init(z, cipherParameters);
        this.label = str;
    }

    protected SecureRandom initSecureRandom(boolean z, SecureRandom secureRandom) {
        return !z ? null : secureRandom == null ? new SecureRandom() : secureRandom;
    }
}
