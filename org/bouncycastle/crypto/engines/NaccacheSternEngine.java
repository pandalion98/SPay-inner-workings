package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle.crypto.params.NaccacheSternPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class NaccacheSternEngine implements AsymmetricBlockCipher {
    private static BigInteger ONE;
    private static BigInteger ZERO;
    private boolean debug;
    private boolean forEncryption;
    private NaccacheSternKeyParameters key;
    private Vector[] lookup;

    static {
        ZERO = BigInteger.valueOf(0);
        ONE = BigInteger.valueOf(1);
    }

    public NaccacheSternEngine() {
        this.lookup = null;
        this.debug = false;
    }

    private static BigInteger chineseRemainder(Vector vector, Vector vector2) {
        int i = 0;
        BigInteger bigInteger = ZERO;
        BigInteger bigInteger2 = ONE;
        for (int i2 = 0; i2 < vector2.size(); i2++) {
            bigInteger2 = bigInteger2.multiply((BigInteger) vector2.elementAt(i2));
        }
        BigInteger bigInteger3 = bigInteger;
        while (i < vector2.size()) {
            BigInteger bigInteger4 = (BigInteger) vector2.elementAt(i);
            bigInteger = bigInteger2.divide(bigInteger4);
            i++;
            bigInteger3 = bigInteger3.add(bigInteger.multiply(bigInteger.modInverse(bigInteger4)).multiply((BigInteger) vector.elementAt(i)));
        }
        return bigInteger3.mod(bigInteger2);
    }

    public byte[] addCryptedBlocks(byte[] bArr, byte[] bArr2) {
        if (this.forEncryption) {
            if (bArr.length > getOutputBlockSize() || bArr2.length > getOutputBlockSize()) {
                throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
            }
        } else if (bArr.length > getInputBlockSize() || bArr2.length > getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
        }
        BigInteger bigInteger = new BigInteger(1, bArr);
        BigInteger bigInteger2 = new BigInteger(1, bArr2);
        BigInteger mod = bigInteger.multiply(bigInteger2).mod(this.key.getModulus());
        if (this.debug) {
            System.out.println("c(m1) as BigInteger:....... " + bigInteger);
            System.out.println("c(m2) as BigInteger:....... " + bigInteger2);
            System.out.println("c(m1)*c(m2)%n = c(m1+m2)%n: " + mod);
        }
        byte[] toByteArray = this.key.getModulus().toByteArray();
        Arrays.fill(toByteArray, (byte) 0);
        System.arraycopy(mod.toByteArray(), 0, toByteArray, toByteArray.length - mod.toByteArray().length, mod.toByteArray().length);
        return toByteArray;
    }

    public byte[] encrypt(BigInteger bigInteger) {
        byte[] toByteArray = this.key.getModulus().toByteArray();
        Arrays.fill(toByteArray, (byte) 0);
        Object toByteArray2 = this.key.getG().modPow(bigInteger, this.key.getModulus()).toByteArray();
        System.arraycopy(toByteArray2, 0, toByteArray, toByteArray.length - toByteArray2.length, toByteArray2.length);
        if (this.debug) {
            System.out.println("Encrypted value is:  " + new BigInteger(toByteArray));
        }
        return toByteArray;
    }

    public int getInputBlockSize() {
        return this.forEncryption ? ((this.key.getLowerSigmaBound() + 7) / 8) - 1 : this.key.getModulus().toByteArray().length;
    }

    public int getOutputBlockSize() {
        return this.forEncryption ? this.key.getModulus().toByteArray().length : ((this.key.getLowerSigmaBound() + 7) / 8) - 1;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forEncryption = z;
        this.key = (NaccacheSternKeyParameters) (cipherParameters instanceof ParametersWithRandom ? ((ParametersWithRandom) cipherParameters).getParameters() : cipherParameters);
        if (!this.forEncryption) {
            if (this.debug) {
                System.out.println("Constructing lookup Array");
            }
            NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters) this.key;
            Vector smallPrimes = naccacheSternPrivateKeyParameters.getSmallPrimes();
            this.lookup = new Vector[smallPrimes.size()];
            for (int i = 0; i < smallPrimes.size(); i++) {
                BigInteger bigInteger = (BigInteger) smallPrimes.elementAt(i);
                int intValue = bigInteger.intValue();
                this.lookup[i] = new Vector();
                this.lookup[i].addElement(ONE);
                if (this.debug) {
                    System.out.println("Constructing lookup ArrayList for " + intValue);
                }
                BigInteger bigInteger2 = ZERO;
                for (int i2 = 1; i2 < intValue; i2++) {
                    bigInteger2 = bigInteger2.add(naccacheSternPrivateKeyParameters.getPhi_n());
                    this.lookup[i].addElement(naccacheSternPrivateKeyParameters.getG().modPow(bigInteger2.divide(bigInteger), naccacheSternPrivateKeyParameters.getModulus()));
                }
            }
        }
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (this.key == null) {
            throw new IllegalStateException("NaccacheStern engine not initialised");
        } else if (i2 > getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for Naccache-Stern cipher.\n");
        } else if (this.forEncryption || i2 >= getInputBlockSize()) {
            if (!(i == 0 && i2 == bArr.length)) {
                Object obj = new byte[i2];
                System.arraycopy(bArr, i, obj, 0, i2);
                bArr = obj;
            }
            BigInteger bigInteger = new BigInteger(1, bArr);
            if (this.debug) {
                System.out.println("input as BigInteger: " + bigInteger);
            }
            if (this.forEncryption) {
                return encrypt(bigInteger);
            }
            Vector vector = new Vector();
            NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters) this.key;
            Vector smallPrimes = naccacheSternPrivateKeyParameters.getSmallPrimes();
            for (int i4 = 0; i4 < smallPrimes.size(); i4++) {
                BigInteger modPow = bigInteger.modPow(naccacheSternPrivateKeyParameters.getPhi_n().divide((BigInteger) smallPrimes.elementAt(i4)), naccacheSternPrivateKeyParameters.getModulus());
                Vector vector2 = this.lookup[i4];
                if (this.lookup[i4].size() != ((BigInteger) smallPrimes.elementAt(i4)).intValue()) {
                    if (this.debug) {
                        System.out.println("Prime is " + smallPrimes.elementAt(i4) + ", lookup table has size " + vector2.size());
                    }
                    throw new InvalidCipherTextException("Error in lookup Array for " + ((BigInteger) smallPrimes.elementAt(i4)).intValue() + ": Size mismatch. Expected ArrayList with length " + ((BigInteger) smallPrimes.elementAt(i4)).intValue() + " but found ArrayList of length " + this.lookup[i4].size());
                }
                int indexOf = vector2.indexOf(modPow);
                if (indexOf == -1) {
                    if (this.debug) {
                        System.out.println("Actual prime is " + smallPrimes.elementAt(i4));
                        System.out.println("Decrypted value is " + modPow);
                        System.out.println("LookupList for " + smallPrimes.elementAt(i4) + " with size " + this.lookup[i4].size() + " is: ");
                        while (i3 < this.lookup[i4].size()) {
                            System.out.println(this.lookup[i4].elementAt(i3));
                            i3++;
                        }
                    }
                    throw new InvalidCipherTextException("Lookup failed");
                }
                vector.addElement(BigInteger.valueOf((long) indexOf));
            }
            return chineseRemainder(vector, smallPrimes).toByteArray();
        } else {
            throw new InvalidCipherTextException("BlockLength does not match modulus for Naccache-Stern cipher.\n");
        }
    }

    public byte[] processData(byte[] bArr) {
        if (this.debug) {
            System.out.println();
        }
        if (bArr.length > getInputBlockSize()) {
            int inputBlockSize = getInputBlockSize();
            int outputBlockSize = getOutputBlockSize();
            if (this.debug) {
                System.out.println("Input blocksize is:  " + inputBlockSize + " bytes");
                System.out.println("Output blocksize is: " + outputBlockSize + " bytes");
                System.out.println("Data has length:.... " + bArr.length + " bytes");
            }
            Object obj = new byte[(outputBlockSize * ((bArr.length / inputBlockSize) + 1))];
            outputBlockSize = 0;
            int i = 0;
            while (i < bArr.length) {
                Object processBlock;
                if (i + inputBlockSize < bArr.length) {
                    processBlock = processBlock(bArr, i, inputBlockSize);
                    i += inputBlockSize;
                } else {
                    processBlock = processBlock(bArr, i, bArr.length - i);
                    i += bArr.length - i;
                }
                if (this.debug) {
                    System.out.println("new datapos is " + i);
                }
                if (processBlock != null) {
                    System.arraycopy(processBlock, 0, obj, outputBlockSize, processBlock.length);
                    outputBlockSize += processBlock.length;
                } else {
                    if (this.debug) {
                        System.out.println("cipher returned null");
                    }
                    throw new InvalidCipherTextException("cipher returned null");
                }
            }
            Object obj2 = new byte[outputBlockSize];
            System.arraycopy(obj, 0, obj2, 0, outputBlockSize);
            if (this.debug) {
                System.out.println("returning " + obj2.length + " bytes");
            }
            return obj2;
        }
        if (this.debug) {
            System.out.println("data size is less then input block size, processing directly");
        }
        return processBlock(bArr, 0, bArr.length);
    }

    public void setDebug(boolean z) {
        this.debug = z;
    }
}
