package org.bouncycastle.crypto.macs;

import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class VMPCMac implements Mac {
    private byte[] f190P;
    private byte[] f191T;
    private byte f192g;
    private byte f193n;
    private byte f194s;
    private byte[] workingIV;
    private byte[] workingKey;
    private byte x1;
    private byte x2;
    private byte x3;
    private byte x4;

    public VMPCMac() {
        this.f193n = (byte) 0;
        this.f190P = null;
        this.f194s = (byte) 0;
    }

    private void initKey(byte[] bArr, byte[] bArr2) {
        int i;
        this.f194s = (byte) 0;
        this.f190P = new byte[SkeinMac.SKEIN_256];
        for (i = 0; i < SkeinMac.SKEIN_256; i++) {
            this.f190P[i] = (byte) i;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.f194s = this.f190P[((this.f194s + this.f190P[i & GF2Field.MASK]) + bArr[i % bArr.length]) & GF2Field.MASK];
            byte b = this.f190P[i & GF2Field.MASK];
            this.f190P[i & GF2Field.MASK] = this.f190P[this.f194s & GF2Field.MASK];
            this.f190P[this.f194s & GF2Field.MASK] = b;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.f194s = this.f190P[((this.f194s + this.f190P[i & GF2Field.MASK]) + bArr2[i % bArr2.length]) & GF2Field.MASK];
            b = this.f190P[i & GF2Field.MASK];
            this.f190P[i & GF2Field.MASK] = this.f190P[this.f194s & GF2Field.MASK];
            this.f190P[this.f194s & GF2Field.MASK] = b;
        }
        this.f193n = (byte) 0;
    }

    public int doFinal(byte[] bArr, int i) {
        int i2;
        for (i2 = 1; i2 < 25; i2++) {
            this.f194s = this.f190P[(this.f194s + this.f190P[this.f193n & GF2Field.MASK]) & GF2Field.MASK];
            this.x4 = this.f190P[((this.x4 + this.x3) + i2) & GF2Field.MASK];
            this.x3 = this.f190P[((this.x3 + this.x2) + i2) & GF2Field.MASK];
            this.x2 = this.f190P[((this.x2 + this.x1) + i2) & GF2Field.MASK];
            this.x1 = this.f190P[((this.x1 + this.f194s) + i2) & GF2Field.MASK];
            this.f191T[this.f192g & 31] = (byte) (this.f191T[this.f192g & 31] ^ this.x1);
            this.f191T[(this.f192g + 1) & 31] = (byte) (this.f191T[(this.f192g + 1) & 31] ^ this.x2);
            this.f191T[(this.f192g + 2) & 31] = (byte) (this.f191T[(this.f192g + 2) & 31] ^ this.x3);
            this.f191T[(this.f192g + 3) & 31] = (byte) (this.f191T[(this.f192g + 3) & 31] ^ this.x4);
            this.f192g = (byte) ((this.f192g + 4) & 31);
            byte b = this.f190P[this.f193n & GF2Field.MASK];
            this.f190P[this.f193n & GF2Field.MASK] = this.f190P[this.f194s & GF2Field.MASK];
            this.f190P[this.f194s & GF2Field.MASK] = b;
            this.f193n = (byte) ((this.f193n + 1) & GF2Field.MASK);
        }
        for (i2 = 0; i2 < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i2++) {
            this.f194s = this.f190P[((this.f194s + this.f190P[i2 & GF2Field.MASK]) + this.f191T[i2 & 31]) & GF2Field.MASK];
            b = this.f190P[i2 & GF2Field.MASK];
            this.f190P[i2 & GF2Field.MASK] = this.f190P[this.f194s & GF2Field.MASK];
            this.f190P[this.f194s & GF2Field.MASK] = b;
        }
        Object obj = new byte[20];
        for (i2 = 0; i2 < 20; i2++) {
            this.f194s = this.f190P[(this.f194s + this.f190P[i2 & GF2Field.MASK]) & GF2Field.MASK];
            obj[i2] = this.f190P[(this.f190P[this.f190P[this.f194s & GF2Field.MASK] & GF2Field.MASK] + 1) & GF2Field.MASK];
            byte b2 = this.f190P[i2 & GF2Field.MASK];
            this.f190P[i2 & GF2Field.MASK] = this.f190P[this.f194s & GF2Field.MASK];
            this.f190P[this.f194s & GF2Field.MASK] = b2;
        }
        System.arraycopy(obj, 0, bArr, i, obj.length);
        reset();
        return obj.length;
    }

    public String getAlgorithmName() {
        return "VMPC-MAC";
    }

    public int getMacSize() {
        return 20;
    }

    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            KeyParameter keyParameter = (KeyParameter) parametersWithIV.getParameters();
            if (parametersWithIV.getParameters() instanceof KeyParameter) {
                this.workingIV = parametersWithIV.getIV();
                if (this.workingIV == null || this.workingIV.length < 1 || this.workingIV.length > McTACommands.MOP_MC_TA_CMD_CASD_BASE) {
                    throw new IllegalArgumentException("VMPC-MAC requires 1 to 768 bytes of IV");
                }
                this.workingKey = keyParameter.getKey();
                reset();
                return;
            }
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include a key");
        }
        throw new IllegalArgumentException("VMPC-MAC Init parameters must include an IV");
    }

    public void reset() {
        initKey(this.workingKey, this.workingIV);
        this.f193n = (byte) 0;
        this.x4 = (byte) 0;
        this.x3 = (byte) 0;
        this.x2 = (byte) 0;
        this.x1 = (byte) 0;
        this.f192g = (byte) 0;
        this.f191T = new byte[32];
        for (int i = 0; i < 32; i++) {
            this.f191T[i] = (byte) 0;
        }
    }

    public void update(byte b) {
        this.f194s = this.f190P[(this.f194s + this.f190P[this.f193n & GF2Field.MASK]) & GF2Field.MASK];
        byte b2 = (byte) (this.f190P[(this.f190P[this.f190P[this.f194s & GF2Field.MASK] & GF2Field.MASK] + 1) & GF2Field.MASK] ^ b);
        this.x4 = this.f190P[(this.x4 + this.x3) & GF2Field.MASK];
        this.x3 = this.f190P[(this.x3 + this.x2) & GF2Field.MASK];
        this.x2 = this.f190P[(this.x2 + this.x1) & GF2Field.MASK];
        this.x1 = this.f190P[(b2 + (this.x1 + this.f194s)) & GF2Field.MASK];
        this.f191T[this.f192g & 31] = (byte) (this.f191T[this.f192g & 31] ^ this.x1);
        this.f191T[(this.f192g + 1) & 31] = (byte) (this.f191T[(this.f192g + 1) & 31] ^ this.x2);
        this.f191T[(this.f192g + 2) & 31] = (byte) (this.f191T[(this.f192g + 2) & 31] ^ this.x3);
        this.f191T[(this.f192g + 3) & 31] = (byte) (this.f191T[(this.f192g + 3) & 31] ^ this.x4);
        this.f192g = (byte) ((this.f192g + 4) & 31);
        b2 = this.f190P[this.f193n & GF2Field.MASK];
        this.f190P[this.f193n & GF2Field.MASK] = this.f190P[this.f194s & GF2Field.MASK];
        this.f190P[this.f194s & GF2Field.MASK] = b2;
        this.f193n = (byte) ((this.f193n + 1) & GF2Field.MASK);
    }

    public void update(byte[] bArr, int i, int i2) {
        if (i + i2 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        for (int i3 = 0; i3 < i2; i3++) {
            update(bArr[i3]);
        }
    }
}
