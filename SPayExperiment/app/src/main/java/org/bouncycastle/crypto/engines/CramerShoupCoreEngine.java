/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.engines.CramerShoupCiphertext;
import org.bouncycastle.crypto.params.CramerShoupKeyParameters;
import org.bouncycastle.crypto.params.CramerShoupParameters;
import org.bouncycastle.crypto.params.CramerShoupPrivateKeyParameters;
import org.bouncycastle.crypto.params.CramerShoupPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.BigIntegers;

public class CramerShoupCoreEngine {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private boolean forEncryption;
    private CramerShoupKeyParameters key;
    private String label = null;
    private SecureRandom random;

    private BigInteger generateRandomElement(BigInteger bigInteger, SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange((BigInteger)ONE, (BigInteger)bigInteger.subtract(ONE), (SecureRandom)secureRandom);
    }

    private boolean isValidMessage(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger.compareTo(bigInteger2) < 0;
    }

    public BigInteger convertInput(byte[] arrby, int n2, int n3) {
        BigInteger bigInteger;
        if (n3 > 1 + this.getInputBlockSize()) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        }
        if (n3 == 1 + this.getInputBlockSize() && this.forEncryption) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        }
        if (n2 != 0 || n3 != arrby.length) {
            byte[] arrby2 = new byte[n3];
            System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
            arrby = arrby2;
        }
        if ((bigInteger = new BigInteger(1, arrby)).compareTo(this.key.getParameters().getP()) >= 0) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        }
        return bigInteger;
    }

    public byte[] convertOutput(BigInteger bigInteger) {
        byte[] arrby = bigInteger.toByteArray();
        if (!this.forEncryption) {
            if (arrby[0] == 0 && arrby.length > this.getOutputBlockSize()) {
                byte[] arrby2 = new byte[-1 + arrby.length];
                System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
                return arrby2;
            }
            if (arrby.length < this.getOutputBlockSize()) {
                byte[] arrby3 = new byte[this.getOutputBlockSize()];
                System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)(arrby3.length - arrby.length), (int)arrby.length);
                return arrby3;
            }
        } else if (arrby[0] == 0) {
            byte[] arrby4 = new byte[-1 + arrby.length];
            System.arraycopy((Object)arrby, (int)1, (Object)arrby4, (int)0, (int)arrby4.length);
            return arrby4;
        }
        return arrby;
    }

    public BigInteger decryptBlock(CramerShoupCiphertext cramerShoupCiphertext) {
        block5 : {
            BigInteger bigInteger;
            block4 : {
                boolean bl = this.key.isPrivate();
                bigInteger = null;
                if (!bl) break block4;
                boolean bl2 = this.forEncryption;
                bigInteger = null;
                if (bl2) break block4;
                boolean bl3 = this.key instanceof CramerShoupPrivateKeyParameters;
                bigInteger = null;
                if (!bl3) break block4;
                CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters = (CramerShoupPrivateKeyParameters)this.key;
                BigInteger bigInteger2 = cramerShoupPrivateKeyParameters.getParameters().getP();
                Digest digest = cramerShoupPrivateKeyParameters.getParameters().getH();
                byte[] arrby = cramerShoupCiphertext.getU1().toByteArray();
                digest.update(arrby, 0, arrby.length);
                byte[] arrby2 = cramerShoupCiphertext.getU2().toByteArray();
                digest.update(arrby2, 0, arrby2.length);
                byte[] arrby3 = cramerShoupCiphertext.getE().toByteArray();
                digest.update(arrby3, 0, arrby3.length);
                if (this.label != null) {
                    byte[] arrby4 = this.label.getBytes();
                    digest.update(arrby4, 0, arrby4.length);
                }
                byte[] arrby5 = new byte[digest.getDigestSize()];
                digest.doFinal(arrby5, 0);
                BigInteger bigInteger3 = new BigInteger(1, arrby5);
                BigInteger bigInteger4 = cramerShoupCiphertext.u1.modPow(cramerShoupPrivateKeyParameters.getX1().add(cramerShoupPrivateKeyParameters.getY1().multiply(bigInteger3)), bigInteger2).multiply(cramerShoupCiphertext.u2.modPow(cramerShoupPrivateKeyParameters.getX2().add(cramerShoupPrivateKeyParameters.getY2().multiply(bigInteger3)), bigInteger2)).mod(bigInteger2);
                if (!cramerShoupCiphertext.v.equals((Object)bigInteger4)) break block5;
                bigInteger = cramerShoupCiphertext.e.multiply(cramerShoupCiphertext.u1.modPow(cramerShoupPrivateKeyParameters.getZ(), bigInteger2).modInverse(bigInteger2)).mod(bigInteger2);
            }
            return bigInteger;
        }
        throw new CramerShoupCiphertextException("Sorry, that ciphertext is not correct");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public CramerShoupCiphertext encryptBlock(BigInteger bigInteger) {
        if (this.key.isPrivate()) return null;
        if (!this.forEncryption) return null;
        if (!(this.key instanceof CramerShoupPublicKeyParameters)) return null;
        CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = (CramerShoupPublicKeyParameters)this.key;
        BigInteger bigInteger2 = cramerShoupPublicKeyParameters.getParameters().getP();
        BigInteger bigInteger3 = cramerShoupPublicKeyParameters.getParameters().getG1();
        BigInteger bigInteger4 = cramerShoupPublicKeyParameters.getParameters().getG2();
        BigInteger bigInteger5 = cramerShoupPublicKeyParameters.getH();
        if (!this.isValidMessage(bigInteger, bigInteger2)) {
            return null;
        }
        BigInteger bigInteger6 = this.generateRandomElement(bigInteger2, this.random);
        BigInteger bigInteger7 = bigInteger3.modPow(bigInteger6, bigInteger2);
        BigInteger bigInteger8 = bigInteger4.modPow(bigInteger6, bigInteger2);
        BigInteger bigInteger9 = bigInteger5.modPow(bigInteger6, bigInteger2).multiply(bigInteger).mod(bigInteger2);
        Digest digest = cramerShoupPublicKeyParameters.getParameters().getH();
        byte[] arrby = bigInteger7.toByteArray();
        digest.update(arrby, 0, arrby.length);
        byte[] arrby2 = bigInteger8.toByteArray();
        digest.update(arrby2, 0, arrby2.length);
        byte[] arrby3 = bigInteger9.toByteArray();
        digest.update(arrby3, 0, arrby3.length);
        if (this.label != null) {
            byte[] arrby4 = this.label.getBytes();
            digest.update(arrby4, 0, arrby4.length);
        }
        byte[] arrby5 = new byte[digest.getDigestSize()];
        digest.doFinal(arrby5, 0);
        BigInteger bigInteger10 = new BigInteger(1, arrby5);
        return new CramerShoupCiphertext(bigInteger7, bigInteger8, bigInteger9, cramerShoupPublicKeyParameters.getC().modPow(bigInteger6, bigInteger2).multiply(cramerShoupPublicKeyParameters.getD().modPow(bigInteger6.multiply(bigInteger10), bigInteger2)).mod(bigInteger2));
    }

    public int getInputBlockSize() {
        int n2 = this.key.getParameters().getP().bitLength();
        if (this.forEncryption) {
            return -1 + (n2 + 7) / 8;
        }
        return (n2 + 7) / 8;
    }

    public int getOutputBlockSize() {
        int n2 = this.key.getParameters().getP().bitLength();
        if (this.forEncryption) {
            return (n2 + 7) / 8;
        }
        return -1 + (n2 + 7) / 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(boolean bl, CipherParameters cipherParameters) {
        SecureRandom secureRandom;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (CramerShoupKeyParameters)parametersWithRandom.getParameters();
            secureRandom = parametersWithRandom.getRandom();
        } else {
            this.key = (CramerShoupKeyParameters)cipherParameters;
            secureRandom = null;
        }
        this.random = this.initSecureRandom(bl, secureRandom);
        this.forEncryption = bl;
    }

    public void init(boolean bl, CipherParameters cipherParameters, String string) {
        this.init(bl, cipherParameters);
        this.label = string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected SecureRandom initSecureRandom(boolean bl, SecureRandom secureRandom) {
        if (!bl) {
            return null;
        }
        if (secureRandom != null) return secureRandom;
        return new SecureRandom();
    }

    public static class CramerShoupCiphertextException
    extends Exception {
        private static final long serialVersionUID = -6360977166495345076L;

        public CramerShoupCiphertextException(String string) {
            super(string);
        }
    }

}

