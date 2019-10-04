/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECCurve$Fp
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.prng.drbg;

import java.math.BigInteger;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.drbg.DualECPoints;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;
import org.bouncycastle.crypto.prng.drbg.Utils;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class DualECSP800DRBG
implements SP80090DRBG {
    private static final int MAX_ADDITIONAL_INPUT = 4096;
    private static final int MAX_ENTROPY_LENGTH = 4096;
    private static final int MAX_PERSONALIZATION_STRING = 4096;
    private static final long RESEED_MAX = 0x80000000L;
    private static final DualECPoints[] nistPoints;
    private static final BigInteger p256_Px;
    private static final BigInteger p256_Py;
    private static final BigInteger p256_Qx;
    private static final BigInteger p256_Qy;
    private static final BigInteger p384_Px;
    private static final BigInteger p384_Py;
    private static final BigInteger p384_Qx;
    private static final BigInteger p384_Qy;
    private static final BigInteger p521_Px;
    private static final BigInteger p521_Py;
    private static final BigInteger p521_Qx;
    private static final BigInteger p521_Qy;
    private ECPoint _P;
    private ECPoint _Q;
    private ECCurve.Fp _curve;
    private Digest _digest;
    private EntropySource _entropySource;
    private ECMultiplier _fixedPointMultiplier = new FixedPointCombMultiplier();
    private int _outlen;
    private long _reseedCounter;
    private byte[] _s;
    private int _sLength;
    private int _securityStrength;
    private int _seedlen;

    static {
        p256_Px = new BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16);
        p256_Py = new BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16);
        p256_Qx = new BigInteger("c97445f45cdef9f0d3e05e1e585fc297235b82b5be8ff3efca67c59852018192", 16);
        p256_Qy = new BigInteger("b28ef557ba31dfcbdd21ac46e2a91e3c304f44cb87058ada2cb815151e610046", 16);
        p384_Px = new BigInteger("aa87ca22be8b05378eb1c71ef320ad746e1d3b628ba79b9859f741e082542a385502f25dbf55296c3a545e3872760ab7", 16);
        p384_Py = new BigInteger("3617de4a96262c6f5d9e98bf9292dc29f8f41dbd289a147ce9da3113b5f0b8c00a60b1ce1d7e819d7a431d7c90ea0e5f", 16);
        p384_Qx = new BigInteger("8e722de3125bddb05580164bfe20b8b432216a62926c57502ceede31c47816edd1e89769124179d0b695106428815065", 16);
        p384_Qy = new BigInteger("023b1660dd701d0839fd45eec36f9ee7b32e13b315dc02610aa1b636e346df671f790f84c5e09b05674dbb7e45c803dd", 16);
        p521_Px = new BigInteger("c6858e06b70404e9cd9e3ecb662395b4429c648139053fb521f828af606b4d3dbaa14b5e77efe75928fe1dc127a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66", 16);
        p521_Py = new BigInteger("11839296a789a3bc0045c8a5fb42c7d1bd998f54449579b446817afbd17273e662c97ee72995ef42640c550b9013fad0761353c7086a272c24088be94769fd16650", 16);
        p521_Qx = new BigInteger("1b9fa3e518d683c6b65763694ac8efbaec6fab44f2276171a42726507dd08add4c3b3f4c1ebc5b1222ddba077f722943b24c3edfa0f85fe24d0c8c01591f0be6f63", 16);
        p521_Qy = new BigInteger("1f3bdba585295d9a1110d1df1f9430ef8442c5018976ff3437ef91b81dc0b8132c8d5c39c32d0e004a3092b7d327c0e7a4d26d2c7b69b58f9066652911e457779de", 16);
        nistPoints = new DualECPoints[3];
        ECCurve.Fp fp = (ECCurve.Fp)NISTNamedCurves.getByName("P-256").getCurve();
        DualECSP800DRBG.nistPoints[0] = new DualECPoints(128, fp.createPoint(p256_Px, p256_Py), fp.createPoint(p256_Qx, p256_Qy), 1);
        ECCurve.Fp fp2 = (ECCurve.Fp)NISTNamedCurves.getByName("P-384").getCurve();
        DualECSP800DRBG.nistPoints[1] = new DualECPoints(192, fp2.createPoint(p384_Px, p384_Py), fp2.createPoint(p384_Qx, p384_Qy), 1);
        ECCurve.Fp fp3 = (ECCurve.Fp)NISTNamedCurves.getByName("P-521").getCurve();
        DualECSP800DRBG.nistPoints[2] = new DualECPoints(256, fp3.createPoint(p521_Px, p521_Py), fp3.createPoint(p521_Qx, p521_Qy), 1);
    }

    public DualECSP800DRBG(Digest digest, int n2, EntropySource entropySource, byte[] arrby, byte[] arrby2) {
        this(nistPoints, digest, n2, entropySource, arrby, arrby2);
    }

    public DualECSP800DRBG(DualECPoints[] arrdualECPoints, Digest digest, int n2, EntropySource entropySource, byte[] arrby, byte[] arrby2) {
        this._digest = digest;
        this._entropySource = entropySource;
        this._securityStrength = n2;
        if (Utils.isTooLarge(arrby, 512)) {
            throw new IllegalArgumentException("Personalization string too large");
        }
        if (entropySource.entropySize() < n2 || entropySource.entropySize() > 4096) {
            throw new IllegalArgumentException("EntropySource must provide between " + n2 + " and " + 4096 + " bits");
        }
        byte[] arrby3 = Arrays.concatenate((byte[])entropySource.getEntropy(), (byte[])arrby2, (byte[])arrby);
        int n3 = 0;
        do {
            block8 : {
                block7 : {
                    if (n3 == arrdualECPoints.length) break block7;
                    if (n2 > arrdualECPoints[n3].getSecurityStrength()) break block8;
                    if (Utils.getMaxSecurityStrength(digest) < arrdualECPoints[n3].getSecurityStrength()) {
                        throw new IllegalArgumentException("Requested security strength is not supported by digest");
                    }
                    this._seedlen = arrdualECPoints[n3].getSeedLen();
                    this._outlen = arrdualECPoints[n3].getMaxOutlen() / 8;
                    this._P = arrdualECPoints[n3].getP();
                    this._Q = arrdualECPoints[n3].getQ();
                }
                if (this._P != null) break;
                throw new IllegalArgumentException("security strength cannot be greater than 256 bits");
            }
            ++n3;
        } while (true);
        this._s = Utils.hash_df(this._digest, arrby3, this._seedlen);
        this._sLength = this._s.length;
        this._reseedCounter = 0L;
    }

    private BigInteger getScalarMultipleXCoord(ECPoint eCPoint, BigInteger bigInteger) {
        return this._fixedPointMultiplier.multiply(eCPoint, bigInteger).normalize().getAffineXCoord().toBigInteger();
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] pad8(byte[] arrby, int n2) {
        if (n2 % 8 != 0) {
            int n3 = 8 - n2 % 8;
            int n4 = 0;
            for (int i2 = -1 + arrby.length; i2 >= 0; --i2) {
                int n5 = 255 & arrby[i2];
                arrby[i2] = (byte)(n5 << n3 | n4 >> 8 - n3);
                n4 = n5;
            }
        }
        return arrby;
    }

    private byte[] xor(byte[] arrby, byte[] arrby2) {
        if (arrby2 == null) {
            return arrby;
        }
        byte[] arrby3 = new byte[arrby.length];
        for (int i2 = 0; i2 != arrby3.length; ++i2) {
            arrby3[i2] = (byte)(arrby[i2] ^ arrby2[i2]);
        }
        return arrby3;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int generate(byte[] arrby, byte[] arrby2, boolean bl) {
        BigInteger bigInteger;
        int n2 = 8 * arrby.length;
        int n3 = arrby.length / this._outlen;
        if (Utils.isTooLarge(arrby2, 512)) {
            throw new IllegalArgumentException("Additional input too large");
        }
        if (this._reseedCounter + (long)n3 > 0x80000000L) {
            return -1;
        }
        if (bl) {
            this.reseed(arrby2);
            arrby2 = null;
        }
        if (arrby2 != null) {
            byte[] arrby3 = Utils.hash_df(this._digest, arrby2, this._seedlen);
            bigInteger = new BigInteger(1, this.xor(this._s, arrby3));
        } else {
            bigInteger = new BigInteger(1, this._s);
        }
        Arrays.fill((byte[])arrby, (byte)0);
        int n4 = 0;
        for (int i2 = 0; i2 < n3; n4 += this._outlen, this._reseedCounter = 1L + this._reseedCounter, ++i2) {
            BigInteger bigInteger2 = this.getScalarMultipleXCoord(this._P, bigInteger);
            byte[] arrby4 = this.getScalarMultipleXCoord(this._Q, bigInteger2).toByteArray();
            if (arrby4.length > this._outlen) {
                System.arraycopy((Object)arrby4, (int)(arrby4.length - this._outlen), (Object)arrby, (int)n4, (int)this._outlen);
            } else {
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby, (int)(n4 + (this._outlen - arrby4.length)), (int)arrby4.length);
            }
            bigInteger = bigInteger2;
        }
        if (n4 < arrby.length) {
            bigInteger = this.getScalarMultipleXCoord(this._P, bigInteger);
            byte[] arrby5 = this.getScalarMultipleXCoord(this._Q, bigInteger).toByteArray();
            int n5 = arrby.length - n4;
            if (arrby5.length > this._outlen) {
                System.arraycopy((Object)arrby5, (int)(arrby5.length - this._outlen), (Object)arrby, (int)n4, (int)n5);
            } else {
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby, (int)(n4 + (this._outlen - arrby5.length)), (int)n5);
            }
            this._reseedCounter = 1L + this._reseedCounter;
        }
        this._s = BigIntegers.asUnsignedByteArray((int)this._sLength, (BigInteger)this.getScalarMultipleXCoord(this._P, bigInteger));
        return n2;
    }

    @Override
    public int getBlockSize() {
        return 8 * this._outlen;
    }

    @Override
    public void reseed(byte[] arrby) {
        if (Utils.isTooLarge(arrby, 512)) {
            throw new IllegalArgumentException("Additional input string too large");
        }
        byte[] arrby2 = this._entropySource.getEntropy();
        byte[] arrby3 = Arrays.concatenate((byte[])this.pad8(this._s, this._seedlen), (byte[])arrby2, (byte[])arrby);
        this._s = Utils.hash_df(this._digest, arrby3, this._seedlen);
        this._reseedCounter = 0L;
    }
}

