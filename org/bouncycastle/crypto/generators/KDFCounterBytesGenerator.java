package org.bouncycastle.crypto.generators;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import java.math.BigInteger;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.MacDerivationFunction;
import org.bouncycastle.crypto.params.KDFCounterParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class KDFCounterBytesGenerator implements MacDerivationFunction {
    private static final BigInteger INTEGER_MAX;
    private static final BigInteger TWO;
    private byte[] fixedInputDataCtrPrefix;
    private byte[] fixedInputData_afterCtr;
    private int generatedBytes;
    private final int f176h;
    private byte[] ios;
    private byte[] f177k;
    private int maxSizeExcl;
    private final Mac prf;

    static {
        INTEGER_MAX = BigInteger.valueOf(2147483647L);
        TWO = BigInteger.valueOf(2);
    }

    public KDFCounterBytesGenerator(Mac mac) {
        this.prf = mac;
        this.f176h = mac.getMacSize();
        this.f177k = new byte[this.f176h];
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void generateNext() {
        /*
        r5 = this;
        r4 = 0;
        r0 = r5.generatedBytes;
        r1 = r5.f176h;
        r0 = r0 / r1;
        r0 = r0 + 1;
        r1 = r5.ios;
        r1 = r1.length;
        switch(r1) {
            case 1: goto L_0x0035;
            case 2: goto L_0x0029;
            case 3: goto L_0x001d;
            case 4: goto L_0x0016;
            default: goto L_0x000e;
        };
    L_0x000e:
        r0 = new java.lang.IllegalStateException;
        r1 = "Unsupported size of counter i";
        r0.<init>(r1);
        throw r0;
    L_0x0016:
        r1 = r5.ios;
        r2 = r0 >>> 24;
        r2 = (byte) r2;
        r1[r4] = r2;
    L_0x001d:
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r2 = r2 + -3;
        r3 = r0 >>> 16;
        r3 = (byte) r3;
        r1[r2] = r3;
    L_0x0029:
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r2 = r2 + -2;
        r3 = r0 >>> 8;
        r3 = (byte) r3;
        r1[r2] = r3;
    L_0x0035:
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r2 = r2 + -1;
        r0 = (byte) r0;
        r1[r2] = r0;
        r0 = r5.prf;
        r1 = r5.fixedInputDataCtrPrefix;
        r2 = r5.fixedInputDataCtrPrefix;
        r2 = r2.length;
        r0.update(r1, r4, r2);
        r0 = r5.prf;
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r0.update(r1, r4, r2);
        r0 = r5.prf;
        r1 = r5.fixedInputData_afterCtr;
        r2 = r5.fixedInputData_afterCtr;
        r2 = r2.length;
        r0.update(r1, r4, r2);
        r0 = r5.prf;
        r1 = r5.f177k;
        r0.doFinal(r1, r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.generators.KDFCounterBytesGenerator.generateNext():void");
    }

    public int generateBytes(byte[] bArr, int i, int i2) {
        int i3 = this.generatedBytes + i2;
        if (i3 < 0 || i3 >= this.maxSizeExcl) {
            throw new DataLengthException("Current KDFCTR may only be used for " + this.maxSizeExcl + " bytes");
        }
        if (this.generatedBytes % this.f176h == 0) {
            generateNext();
        }
        i3 = this.generatedBytes % this.f176h;
        int min = Math.min(this.f176h - (this.generatedBytes % this.f176h), i2);
        System.arraycopy(this.f177k, i3, bArr, i, min);
        this.generatedBytes += min;
        i3 = i2 - min;
        min += i;
        while (i3 > 0) {
            generateNext();
            int min2 = Math.min(this.f176h, i3);
            System.arraycopy(this.f177k, 0, bArr, min, min2);
            this.generatedBytes += min2;
            i3 -= min2;
            min += min2;
        }
        return i2;
    }

    public Mac getMac() {
        return this.prf;
    }

    public void init(DerivationParameters derivationParameters) {
        if (derivationParameters instanceof KDFCounterParameters) {
            KDFCounterParameters kDFCounterParameters = (KDFCounterParameters) derivationParameters;
            this.prf.init(new KeyParameter(kDFCounterParameters.getKI()));
            this.fixedInputDataCtrPrefix = kDFCounterParameters.getFixedInputDataCounterPrefix();
            this.fixedInputData_afterCtr = kDFCounterParameters.getFixedInputDataCounterSuffix();
            int r = kDFCounterParameters.getR();
            this.ios = new byte[(r / 8)];
            BigInteger multiply = TWO.pow(r).multiply(BigInteger.valueOf((long) this.f176h));
            this.maxSizeExcl = multiply.compareTo(INTEGER_MAX) == 1 ? CNCCCommands.CMD_CNCC_CMD_UNKNOWN : multiply.intValue();
            this.generatedBytes = 0;
            return;
        }
        throw new IllegalArgumentException("Wrong type of arguments given");
    }
}
