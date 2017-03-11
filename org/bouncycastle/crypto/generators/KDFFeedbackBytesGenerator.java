package org.bouncycastle.crypto.generators;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import java.math.BigInteger;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.MacDerivationFunction;
import org.bouncycastle.crypto.params.KDFFeedbackParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class KDFFeedbackBytesGenerator implements MacDerivationFunction {
    private static final BigInteger INTEGER_MAX;
    private static final BigInteger TWO;
    private byte[] fixedInputData;
    private int generatedBytes;
    private final int f181h;
    private byte[] ios;
    private byte[] iv;
    private byte[] f182k;
    private int maxSizeExcl;
    private final Mac prf;
    private boolean useCounter;

    static {
        INTEGER_MAX = BigInteger.valueOf(2147483647L);
        TWO = BigInteger.valueOf(2);
    }

    public KDFFeedbackBytesGenerator(Mac mac) {
        this.prf = mac;
        this.f181h = mac.getMacSize();
        this.f182k = new byte[this.f181h];
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void generateNext() {
        /*
        r5 = this;
        r4 = 0;
        r0 = r5.generatedBytes;
        if (r0 != 0) goto L_0x0028;
    L_0x0005:
        r0 = r5.prf;
        r1 = r5.iv;
        r2 = r5.iv;
        r2 = r2.length;
        r0.update(r1, r4, r2);
    L_0x000f:
        r0 = r5.useCounter;
        if (r0 == 0) goto L_0x0066;
    L_0x0013:
        r0 = r5.generatedBytes;
        r1 = r5.f181h;
        r0 = r0 / r1;
        r0 = r0 + 1;
        r1 = r5.ios;
        r1 = r1.length;
        switch(r1) {
            case 1: goto L_0x0052;
            case 2: goto L_0x0046;
            case 3: goto L_0x003a;
            case 4: goto L_0x0033;
            default: goto L_0x0020;
        };
    L_0x0020:
        r0 = new java.lang.IllegalStateException;
        r1 = "Unsupported size of counter i";
        r0.<init>(r1);
        throw r0;
    L_0x0028:
        r0 = r5.prf;
        r1 = r5.f182k;
        r2 = r5.f182k;
        r2 = r2.length;
        r0.update(r1, r4, r2);
        goto L_0x000f;
    L_0x0033:
        r1 = r5.ios;
        r2 = r0 >>> 24;
        r2 = (byte) r2;
        r1[r4] = r2;
    L_0x003a:
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r2 = r2 + -3;
        r3 = r0 >>> 16;
        r3 = (byte) r3;
        r1[r2] = r3;
    L_0x0046:
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r2 = r2 + -2;
        r3 = r0 >>> 8;
        r3 = (byte) r3;
        r1[r2] = r3;
    L_0x0052:
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r2 = r2 + -1;
        r0 = (byte) r0;
        r1[r2] = r0;
        r0 = r5.prf;
        r1 = r5.ios;
        r2 = r5.ios;
        r2 = r2.length;
        r0.update(r1, r4, r2);
    L_0x0066:
        r0 = r5.prf;
        r1 = r5.fixedInputData;
        r2 = r5.fixedInputData;
        r2 = r2.length;
        r0.update(r1, r4, r2);
        r0 = r5.prf;
        r1 = r5.f182k;
        r0.doFinal(r1, r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.generators.KDFFeedbackBytesGenerator.generateNext():void");
    }

    public int generateBytes(byte[] bArr, int i, int i2) {
        int i3 = this.generatedBytes + i2;
        if (i3 < 0 || i3 >= this.maxSizeExcl) {
            throw new DataLengthException("Current KDFCTR may only be used for " + this.maxSizeExcl + " bytes");
        }
        if (this.generatedBytes % this.f181h == 0) {
            generateNext();
        }
        i3 = this.generatedBytes % this.f181h;
        int min = Math.min(this.f181h - (this.generatedBytes % this.f181h), i2);
        System.arraycopy(this.f182k, i3, bArr, i, min);
        this.generatedBytes += min;
        i3 = i2 - min;
        min += i;
        while (i3 > 0) {
            generateNext();
            int min2 = Math.min(this.f181h, i3);
            System.arraycopy(this.f182k, 0, bArr, min, min2);
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
        int i = CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        if (derivationParameters instanceof KDFFeedbackParameters) {
            KDFFeedbackParameters kDFFeedbackParameters = (KDFFeedbackParameters) derivationParameters;
            this.prf.init(new KeyParameter(kDFFeedbackParameters.getKI()));
            this.fixedInputData = kDFFeedbackParameters.getFixedInputData();
            int r = kDFFeedbackParameters.getR();
            this.ios = new byte[(r / 8)];
            if (kDFFeedbackParameters.useCounter()) {
                BigInteger multiply = TWO.pow(r).multiply(BigInteger.valueOf((long) this.f181h));
                if (multiply.compareTo(INTEGER_MAX) != 1) {
                    i = multiply.intValue();
                }
                this.maxSizeExcl = i;
            } else {
                this.maxSizeExcl = CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
            }
            this.iv = kDFFeedbackParameters.getIV();
            this.useCounter = kDFFeedbackParameters.useCounter();
            this.generatedBytes = 0;
            return;
        }
        throw new IllegalArgumentException("Wrong type of arguments given");
    }
}
