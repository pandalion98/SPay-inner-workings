package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Hex;

public class DSAParametersGenerator {
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private int f174L;
    private int f175N;
    private int certainty;
    private Digest digest;
    private SecureRandom random;
    private int usageIndex;
    private boolean use186_3;

    static {
        ZERO = BigInteger.valueOf(0);
        ONE = BigInteger.valueOf(1);
        TWO = BigInteger.valueOf(2);
    }

    public DSAParametersGenerator() {
        this(new SHA1Digest());
    }

    public DSAParametersGenerator(Digest digest) {
        this.digest = digest;
    }

    private static BigInteger calculateGenerator_FIPS186_2(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger modPow;
        BigInteger divide = bigInteger.subtract(ONE).divide(bigInteger2);
        BigInteger subtract = bigInteger.subtract(TWO);
        do {
            modPow = BigIntegers.createRandomInRange(TWO, subtract, secureRandom).modPow(divide, bigInteger);
        } while (modPow.bitLength() <= 1);
        return modPow;
    }

    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        return calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }

    private static BigInteger calculateGenerator_FIPS186_3_Verifiable(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr, int i) {
        BigInteger divide = bigInteger.subtract(ONE).divide(bigInteger2);
        Object decode = Hex.decode("6767656E");
        Object obj = new byte[(((bArr.length + decode.length) + 1) + 2)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(decode, 0, obj, bArr.length, decode.length);
        obj[obj.length - 3] = (byte) i;
        byte[] bArr2 = new byte[digest.getDigestSize()];
        for (int i2 = 1; i2 < PKIFailureInfo.notAuthorized; i2++) {
            inc(obj);
            hash(digest, obj, bArr2);
            BigInteger modPow = new BigInteger(1, bArr2).modPow(divide, bigInteger);
            if (modPow.compareTo(TWO) >= 0) {
                return modPow;
            }
        }
        return null;
    }

    private DSAParameters generateParameters_FIPS186_2() {
        byte[] bArr = new byte[20];
        Object obj = new byte[20];
        Object obj2 = new byte[20];
        byte[] bArr2 = new byte[20];
        int i = (this.f174L - 1) / CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256;
        Object obj3 = new byte[(this.f174L / 8)];
        if (this.digest instanceof SHA1Digest) {
            while (true) {
                int i2;
                this.random.nextBytes(bArr);
                hash(this.digest, bArr, obj);
                System.arraycopy(bArr, 0, obj2, 0, bArr.length);
                inc(obj2);
                hash(this.digest, obj2, obj2);
                for (i2 = 0; i2 != bArr2.length; i2++) {
                    bArr2[i2] = (byte) (obj[i2] ^ obj2[i2]);
                }
                bArr2[0] = (byte) (bArr2[0] | -128);
                bArr2[19] = (byte) (bArr2[19] | 1);
                BigInteger bigInteger = new BigInteger(1, bArr2);
                if (bigInteger.isProbablePrime(this.certainty)) {
                    byte[] clone = Arrays.clone(bArr);
                    inc(clone);
                    for (int i3 = 0; i3 < PKIFailureInfo.certConfirmed; i3++) {
                        for (i2 = 0; i2 < i; i2++) {
                            inc(clone);
                            hash(this.digest, clone, obj);
                            System.arraycopy(obj, 0, obj3, obj3.length - ((i2 + 1) * obj.length), obj.length);
                        }
                        inc(clone);
                        hash(this.digest, clone, obj);
                        System.arraycopy(obj, obj.length - (obj3.length - (obj.length * i)), obj3, 0, obj3.length - (obj.length * i));
                        obj3[0] = (byte) (obj3[0] | -128);
                        BigInteger bigInteger2 = new BigInteger(1, obj3);
                        bigInteger2 = bigInteger2.subtract(bigInteger2.mod(bigInteger.shiftLeft(1)).subtract(ONE));
                        if (bigInteger2.bitLength() == this.f174L && bigInteger2.isProbablePrime(this.certainty)) {
                            return new DSAParameters(bigInteger2, bigInteger, calculateGenerator_FIPS186_2(bigInteger2, bigInteger, this.random), new DSAValidationParameters(bArr, i3));
                        }
                    }
                    continue;
                }
            }
        } else {
            throw new IllegalStateException("can only use SHA-1 for generating FIPS 186-2 parameters");
        }
    }

    private DSAParameters generateParameters_FIPS186_3() {
        BigInteger subtract;
        int i;
        BigInteger subtract2;
        Digest digest = this.digest;
        int digestSize = digest.getDigestSize() * 8;
        byte[] bArr = new byte[(this.f175N / 8)];
        int i2 = (this.f174L - 1) / digestSize;
        int i3 = (this.f174L - 1) % digestSize;
        byte[] bArr2 = new byte[digest.getDigestSize()];
        loop0:
        while (true) {
            this.random.nextBytes(bArr);
            hash(digest, bArr, bArr2);
            BigInteger mod = new BigInteger(1, bArr2).mod(ONE.shiftLeft(this.f175N - 1));
            subtract = ONE.shiftLeft(this.f175N - 1).add(mod).add(ONE).subtract(mod.mod(TWO));
            if (subtract.isProbablePrime(this.certainty)) {
                byte[] clone = Arrays.clone(bArr);
                int i4 = this.f174L * 4;
                i = 0;
                while (i < i4) {
                    BigInteger bigInteger = ZERO;
                    int i5 = 0;
                    int i6 = 0;
                    while (i5 <= i2) {
                        inc(clone);
                        hash(digest, clone, bArr2);
                        mod = new BigInteger(1, bArr2);
                        if (i5 == i2) {
                            mod = mod.mod(ONE.shiftLeft(i3));
                        }
                        bigInteger = bigInteger.add(mod.shiftLeft(i6));
                        i5++;
                        i6 += digestSize;
                    }
                    mod = bigInteger.add(ONE.shiftLeft(this.f174L - 1));
                    subtract2 = mod.subtract(mod.mod(subtract.shiftLeft(1)).subtract(ONE));
                    if (subtract2.bitLength() == this.f174L && subtract2.isProbablePrime(this.certainty)) {
                        break loop0;
                    }
                    i++;
                }
                continue;
            }
        }
        if (this.usageIndex >= 0) {
            BigInteger calculateGenerator_FIPS186_3_Verifiable = calculateGenerator_FIPS186_3_Verifiable(digest, subtract2, subtract, bArr, this.usageIndex);
            if (calculateGenerator_FIPS186_3_Verifiable != null) {
                return new DSAParameters(subtract2, subtract, calculateGenerator_FIPS186_3_Verifiable, new DSAValidationParameters(bArr, i, this.usageIndex));
            }
        }
        return new DSAParameters(subtract2, subtract, calculateGenerator_FIPS186_3_Unverifiable(subtract2, subtract, this.random), new DSAValidationParameters(bArr, i));
    }

    private static int getDefaultN(int i) {
        return i > SkeinMac.SKEIN_1024 ? SkeinMac.SKEIN_256 : CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256;
    }

    private static void hash(Digest digest, byte[] bArr, byte[] bArr2) {
        digest.update(bArr, 0, bArr.length);
        digest.doFinal(bArr2, 0);
    }

    private static void inc(byte[] bArr) {
        int length = bArr.length - 1;
        while (length >= 0) {
            byte b = (byte) ((bArr[length] + 1) & GF2Field.MASK);
            bArr[length] = b;
            if (b == null) {
                length--;
            } else {
                return;
            }
        }
    }

    public DSAParameters generateParameters() {
        return this.use186_3 ? generateParameters_FIPS186_3() : generateParameters_FIPS186_2();
    }

    public void init(int i, int i2, SecureRandom secureRandom) {
        this.use186_3 = false;
        this.f174L = i;
        this.f175N = getDefaultN(i);
        this.certainty = i2;
        this.random = secureRandom;
    }

    public void init(DSAParameterGenerationParameters dSAParameterGenerationParameters) {
        this.use186_3 = true;
        this.f174L = dSAParameterGenerationParameters.getL();
        this.f175N = dSAParameterGenerationParameters.getN();
        this.certainty = dSAParameterGenerationParameters.getCertainty();
        this.random = dSAParameterGenerationParameters.getRandom();
        this.usageIndex = dSAParameterGenerationParameters.getUsageIndex();
        if (this.f174L < SkeinMac.SKEIN_1024 || this.f174L > 3072 || this.f174L % SkeinMac.SKEIN_1024 != 0) {
            throw new IllegalArgumentException("L values must be between 1024 and 3072 and a multiple of 1024");
        } else if (this.f174L == SkeinMac.SKEIN_1024 && this.f175N != CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256) {
            throw new IllegalArgumentException("N must be 160 for L = 1024");
        } else if (this.f174L == PKIFailureInfo.wrongIntegrity && this.f175N != 224 && this.f175N != SkeinMac.SKEIN_256) {
            throw new IllegalArgumentException("N must be 224 or 256 for L = 2048");
        } else if (this.f174L == 3072 && this.f175N != SkeinMac.SKEIN_256) {
            throw new IllegalArgumentException("N must be 256 for L = 3072");
        } else if (this.digest.getDigestSize() * 8 < this.f175N) {
            throw new IllegalStateException("Digest output size too small for value of N");
        }
    }
}
