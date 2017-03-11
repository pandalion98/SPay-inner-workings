package org.bouncycastle.math.field;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public abstract class FiniteFields {
    static final FiniteField GF_2;
    static final FiniteField GF_3;

    static {
        GF_2 = new PrimeField(BigInteger.valueOf(2));
        GF_3 = new PrimeField(BigInteger.valueOf(3));
    }

    public static PolynomialExtensionField getBinaryExtensionField(int[] iArr) {
        if (iArr[0] != 0) {
            throw new IllegalArgumentException("Irreducible polynomials in GF(2) must have constant term");
        }
        for (int i = 1; i < iArr.length; i++) {
            if (iArr[i] <= iArr[i - 1]) {
                throw new IllegalArgumentException("Polynomial exponents must be montonically increasing");
            }
        }
        return new GenericPolynomialExtensionField(GF_2, new GF2Polynomial(iArr));
    }

    public static FiniteField getPrimeField(BigInteger bigInteger) {
        int bitLength = bigInteger.bitLength();
        if (bigInteger.signum() <= 0 || bitLength < 2) {
            throw new IllegalArgumentException("'characteristic' must be >= 2");
        }
        if (bitLength < 3) {
            switch (bigInteger.intValue()) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    return GF_2;
                case F2m.PPB /*3*/:
                    return GF_3;
            }
        }
        return new PrimeField(bigInteger);
    }
}