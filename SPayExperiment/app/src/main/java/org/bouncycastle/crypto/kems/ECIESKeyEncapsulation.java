/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.kems;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.KeyEncapsulation;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class ECIESKeyEncapsulation
implements KeyEncapsulation {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private boolean CofactorMode;
    private boolean OldCofactorMode;
    private boolean SingleHashMode;
    private DerivationFunction kdf;
    private ECKeyParameters key;
    private SecureRandom rnd;

    public ECIESKeyEncapsulation(DerivationFunction derivationFunction, SecureRandom secureRandom) {
        this.kdf = derivationFunction;
        this.rnd = secureRandom;
        this.CofactorMode = false;
        this.OldCofactorMode = false;
        this.SingleHashMode = false;
    }

    public ECIESKeyEncapsulation(DerivationFunction derivationFunction, SecureRandom secureRandom, boolean bl, boolean bl2, boolean bl3) {
        this.kdf = derivationFunction;
        this.rnd = secureRandom;
        this.CofactorMode = bl;
        this.OldCofactorMode = bl2;
        this.SingleHashMode = bl3;
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    public CipherParameters decrypt(byte[] arrby, int n2) {
        return this.decrypt(arrby, 0, arrby.length, n2);
    }

    @Override
    public CipherParameters decrypt(byte[] arrby, int n2, int n3, int n4) {
        if (!(this.key instanceof ECPrivateKeyParameters)) {
            throw new IllegalArgumentException("Private key required for encryption");
        }
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)this.key;
        ECDomainParameters eCDomainParameters = eCPrivateKeyParameters.getParameters();
        ECCurve eCCurve = eCDomainParameters.getCurve();
        BigInteger bigInteger = eCDomainParameters.getN();
        BigInteger bigInteger2 = eCDomainParameters.getH();
        byte[] arrby2 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        ECPoint eCPoint = eCCurve.decodePoint(arrby2);
        if (this.CofactorMode || this.OldCofactorMode) {
            eCPoint = eCPoint.multiply(bigInteger2);
        }
        BigInteger bigInteger3 = eCPrivateKeyParameters.getD();
        if (this.CofactorMode) {
            bigInteger3 = bigInteger3.multiply(bigInteger2.modInverse(bigInteger)).mod(bigInteger);
        }
        return this.deriveKey(n4, arrby2, eCPoint.multiply(bigInteger3).normalize().getAffineXCoord().getEncoded());
    }

    protected KeyParameter deriveKey(int n2, byte[] arrby, byte[] arrby2) {
        if (this.SingleHashMode) {
            byte[] arrby3 = Arrays.concatenate((byte[])arrby, (byte[])arrby2);
            Arrays.fill((byte[])arrby2, (byte)0);
            arrby2 = arrby3;
        }
        try {
            this.kdf.init(new KDFParameters(arrby2, null));
            byte[] arrby4 = new byte[n2];
            this.kdf.generateBytes(arrby4, 0, arrby4.length);
            KeyParameter keyParameter = new KeyParameter(arrby4);
            return keyParameter;
        }
        finally {
            Arrays.fill((byte[])arrby2, (byte)0);
        }
    }

    public CipherParameters encrypt(byte[] arrby, int n2) {
        return this.encrypt(arrby, 0, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public CipherParameters encrypt(byte[] arrby, int n2, int n3) {
        if (!(this.key instanceof ECPublicKeyParameters)) {
            throw new IllegalArgumentException("Public key required for encryption");
        }
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters)this.key;
        ECDomainParameters eCDomainParameters = eCPublicKeyParameters.getParameters();
        ECCurve eCCurve = eCDomainParameters.getCurve();
        BigInteger bigInteger = eCDomainParameters.getN();
        BigInteger bigInteger2 = eCDomainParameters.getH();
        BigInteger bigInteger3 = BigIntegers.createRandomInRange((BigInteger)ONE, (BigInteger)bigInteger, (SecureRandom)this.rnd);
        BigInteger bigInteger4 = this.CofactorMode ? bigInteger3.multiply(bigInteger2).mod(bigInteger) : bigInteger3;
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        ECPoint[] arreCPoint = new ECPoint[]{eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger3), eCPublicKeyParameters.getQ().multiply(bigInteger4)};
        eCCurve.normalizeAll(arreCPoint);
        ECPoint eCPoint = arreCPoint[0];
        ECPoint eCPoint2 = arreCPoint[1];
        byte[] arrby2 = eCPoint.getEncoded(false);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)arrby2.length);
        return this.deriveKey(n3, arrby2, eCPoint2.getAffineXCoord().getEncoded());
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ECKeyParameters)) {
            throw new IllegalArgumentException("EC key required");
        }
        this.key = (ECKeyParameters)cipherParameters;
    }
}

