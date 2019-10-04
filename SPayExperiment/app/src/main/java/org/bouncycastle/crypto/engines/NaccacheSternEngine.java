/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.engines;

import java.io.PrintStream;
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

public class NaccacheSternEngine
implements AsymmetricBlockCipher {
    private static BigInteger ONE;
    private static BigInteger ZERO;
    private boolean debug = false;
    private boolean forEncryption;
    private NaccacheSternKeyParameters key;
    private Vector[] lookup = null;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
    }

    private static BigInteger chineseRemainder(Vector vector, Vector vector2) {
        int n2 = 0;
        BigInteger bigInteger = ZERO;
        BigInteger bigInteger2 = ONE;
        BigInteger bigInteger3 = bigInteger2;
        for (int i2 = 0; i2 < vector2.size(); ++i2) {
            bigInteger3 = bigInteger3.multiply((BigInteger)vector2.elementAt(i2));
        }
        BigInteger bigInteger4 = bigInteger;
        while (n2 < vector2.size()) {
            BigInteger bigInteger5 = (BigInteger)vector2.elementAt(n2);
            BigInteger bigInteger6 = bigInteger3.divide(bigInteger5);
            BigInteger bigInteger7 = bigInteger4.add(bigInteger6.multiply(bigInteger6.modInverse(bigInteger5)).multiply((BigInteger)vector.elementAt(n2)));
            ++n2;
            bigInteger4 = bigInteger7;
        }
        return bigInteger4.mod(bigInteger3);
    }

    public byte[] addCryptedBlocks(byte[] arrby, byte[] arrby2) {
        if (this.forEncryption ? arrby.length > this.getOutputBlockSize() || arrby2.length > this.getOutputBlockSize() : arrby.length > this.getInputBlockSize() || arrby2.length > this.getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
        }
        BigInteger bigInteger = new BigInteger(1, arrby);
        BigInteger bigInteger2 = new BigInteger(1, arrby2);
        BigInteger bigInteger3 = bigInteger.multiply(bigInteger2).mod(this.key.getModulus());
        if (this.debug) {
            System.out.println("c(m1) as BigInteger:....... " + (Object)bigInteger);
            System.out.println("c(m2) as BigInteger:....... " + (Object)bigInteger2);
            System.out.println("c(m1)*c(m2)%n = c(m1+m2)%n: " + (Object)bigInteger3);
        }
        byte[] arrby3 = this.key.getModulus().toByteArray();
        Arrays.fill((byte[])arrby3, (byte)0);
        System.arraycopy((Object)bigInteger3.toByteArray(), (int)0, (Object)arrby3, (int)(arrby3.length - bigInteger3.toByteArray().length), (int)bigInteger3.toByteArray().length);
        return arrby3;
    }

    public byte[] encrypt(BigInteger bigInteger) {
        byte[] arrby = this.key.getModulus().toByteArray();
        Arrays.fill((byte[])arrby, (byte)0);
        byte[] arrby2 = this.key.getG().modPow(bigInteger, this.key.getModulus()).toByteArray();
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)(arrby.length - arrby2.length), (int)arrby2.length);
        if (this.debug) {
            System.out.println("Encrypted value is:  " + (Object)new BigInteger(arrby));
        }
        return arrby;
    }

    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return -1 + (7 + this.key.getLowerSigmaBound()) / 8;
        }
        return this.key.getModulus().toByteArray().length;
    }

    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return this.key.getModulus().toByteArray().length;
        }
        return -1 + (7 + this.key.getLowerSigmaBound()) / 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncryption = bl;
        CipherParameters cipherParameters2 = cipherParameters instanceof ParametersWithRandom ? ((ParametersWithRandom)cipherParameters).getParameters() : cipherParameters;
        this.key = (NaccacheSternKeyParameters)cipherParameters2;
        if (!this.forEncryption) {
            if (this.debug) {
                System.out.println("Constructing lookup Array");
            }
            NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
            Vector vector = naccacheSternPrivateKeyParameters.getSmallPrimes();
            this.lookup = new Vector[vector.size()];
            for (int i2 = 0; i2 < vector.size(); ++i2) {
                BigInteger bigInteger = (BigInteger)vector.elementAt(i2);
                int n2 = bigInteger.intValue();
                this.lookup[i2] = new Vector();
                this.lookup[i2].addElement((Object)ONE);
                if (this.debug) {
                    System.out.println("Constructing lookup ArrayList for " + n2);
                }
                BigInteger bigInteger2 = ZERO;
                for (int n3 = 1; n3 < n2; bigInteger2 = bigInteger2.add((BigInteger)naccacheSternPrivateKeyParameters.getPhi_n()), ++n3) {
                    BigInteger bigInteger3 = bigInteger2.divide(bigInteger);
                    this.lookup[i2].addElement((Object)naccacheSternPrivateKeyParameters.getG().modPow(bigInteger3, naccacheSternPrivateKeyParameters.getModulus()));
                }
            }
        }
    }

    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        int n4 = 0;
        if (this.key == null) {
            throw new IllegalStateException("NaccacheStern engine not initialised");
        }
        if (n3 > 1 + this.getInputBlockSize()) {
            throw new DataLengthException("input too large for Naccache-Stern cipher.\n");
        }
        if (!this.forEncryption && n3 < this.getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength does not match modulus for Naccache-Stern cipher.\n");
        }
        if (n2 != 0 || n3 != arrby.length) {
            byte[] arrby2 = new byte[n3];
            System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
            arrby = arrby2;
        }
        BigInteger bigInteger = new BigInteger(1, arrby);
        if (this.debug) {
            System.out.println("input as BigInteger: " + (Object)bigInteger);
        }
        if (this.forEncryption) {
            return this.encrypt(bigInteger);
        }
        Vector vector = new Vector();
        NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
        Vector vector2 = naccacheSternPrivateKeyParameters.getSmallPrimes();
        for (int i2 = 0; i2 < vector2.size(); ++i2) {
            BigInteger bigInteger2 = bigInteger.modPow(naccacheSternPrivateKeyParameters.getPhi_n().divide((BigInteger)vector2.elementAt(i2)), naccacheSternPrivateKeyParameters.getModulus());
            Vector vector3 = this.lookup[i2];
            if (this.lookup[i2].size() != ((BigInteger)vector2.elementAt(i2)).intValue()) {
                if (this.debug) {
                    System.out.println("Prime is " + vector2.elementAt(i2) + ", lookup table has size " + vector3.size());
                }
                throw new InvalidCipherTextException("Error in lookup Array for " + ((BigInteger)vector2.elementAt(i2)).intValue() + ": Size mismatch. Expected ArrayList with length " + ((BigInteger)vector2.elementAt(i2)).intValue() + " but found ArrayList of length " + this.lookup[i2].size());
            }
            int n5 = vector3.indexOf((Object)bigInteger2);
            if (n5 == -1) {
                if (this.debug) {
                    System.out.println("Actual prime is " + vector2.elementAt(i2));
                    System.out.println("Decrypted value is " + (Object)bigInteger2);
                    System.out.println("LookupList for " + vector2.elementAt(i2) + " with size " + this.lookup[i2].size() + " is: ");
                    while (n4 < this.lookup[i2].size()) {
                        System.out.println(this.lookup[i2].elementAt(n4));
                        ++n4;
                    }
                }
                throw new InvalidCipherTextException("Lookup failed");
            }
            vector.addElement((Object)BigInteger.valueOf((long)n5));
        }
        return NaccacheSternEngine.chineseRemainder(vector, vector2).toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] processData(byte[] arrby) {
        int n2;
        byte[] arrby2;
        int n3;
        int n4;
        if (this.debug) {
            System.out.println();
        }
        if (arrby.length > this.getInputBlockSize()) {
            n2 = this.getInputBlockSize();
            int n5 = this.getOutputBlockSize();
            if (this.debug) {
                System.out.println("Input blocksize is:  " + n2 + " bytes");
                System.out.println("Output blocksize is: " + n5 + " bytes");
                System.out.println("Data has length:.... " + arrby.length + " bytes");
            }
            arrby2 = new byte[n5 * (1 + arrby.length / n2)];
            n3 = 0;
            n4 = 0;
        } else {
            if (this.debug) {
                System.out.println("data size is less then input block size, processing directly");
            }
            return this.processBlock(arrby, 0, arrby.length);
        }
        while (n4 < arrby.length) {
            byte[] arrby3;
            if (n4 + n2 < arrby.length) {
                arrby3 = this.processBlock(arrby, n4, n2);
                n4 += n2;
            } else {
                arrby3 = this.processBlock(arrby, n4, arrby.length - n4);
                n4 += arrby.length - n4;
            }
            if (this.debug) {
                System.out.println("new datapos is " + n4);
            }
            if (arrby3 != null) {
                System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n3, (int)arrby3.length);
                n3 += arrby3.length;
                continue;
            }
            if (this.debug) {
                System.out.println("cipher returned null");
            }
            throw new InvalidCipherTextException("cipher returned null");
        }
        byte[] arrby4 = new byte[n3];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)0, (int)n3);
        if (this.debug) {
            System.out.println("returning " + arrby4.length + " bytes");
        }
        return arrby4;
    }

    public void setDebug(boolean bl) {
        this.debug = bl;
    }
}

