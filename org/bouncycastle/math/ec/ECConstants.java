package org.bouncycastle.math.ec;

import java.math.BigInteger;

public interface ECConstants {
    public static final BigInteger FOUR;
    public static final BigInteger ONE;
    public static final BigInteger THREE;
    public static final BigInteger TWO;
    public static final BigInteger ZERO;

    static {
        ZERO = BigInteger.valueOf(0);
        ONE = BigInteger.valueOf(1);
        TWO = BigInteger.valueOf(2);
        THREE = BigInteger.valueOf(3);
        FOUR = BigInteger.valueOf(4);
    }
}
