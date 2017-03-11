package org.bouncycastle.crypto.engines;

import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class VMPCEngine implements StreamCipher {
    protected byte[] f169P;
    protected byte f170n;
    protected byte f171s;
    protected byte[] workingIV;
    protected byte[] workingKey;

    public VMPCEngine() {
        this.f170n = (byte) 0;
        this.f169P = null;
        this.f171s = (byte) 0;
    }

    public String getAlgorithmName() {
        return "VMPC";
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            if (parametersWithIV.getParameters() instanceof KeyParameter) {
                KeyParameter keyParameter = (KeyParameter) parametersWithIV.getParameters();
                this.workingIV = parametersWithIV.getIV();
                if (this.workingIV == null || this.workingIV.length < 1 || this.workingIV.length > McTACommands.MOP_MC_TA_CMD_CASD_BASE) {
                    throw new IllegalArgumentException("VMPC requires 1 to 768 bytes of IV");
                }
                this.workingKey = keyParameter.getKey();
                initKey(this.workingKey, this.workingIV);
                return;
            }
            throw new IllegalArgumentException("VMPC init parameters must include a key");
        }
        throw new IllegalArgumentException("VMPC init parameters must include an IV");
    }

    protected void initKey(byte[] bArr, byte[] bArr2) {
        int i;
        this.f171s = (byte) 0;
        this.f169P = new byte[SkeinMac.SKEIN_256];
        for (i = 0; i < SkeinMac.SKEIN_256; i++) {
            this.f169P[i] = (byte) i;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.f171s = this.f169P[((this.f171s + this.f169P[i & GF2Field.MASK]) + bArr[i % bArr.length]) & GF2Field.MASK];
            byte b = this.f169P[i & GF2Field.MASK];
            this.f169P[i & GF2Field.MASK] = this.f169P[this.f171s & GF2Field.MASK];
            this.f169P[this.f171s & GF2Field.MASK] = b;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.f171s = this.f169P[((this.f171s + this.f169P[i & GF2Field.MASK]) + bArr2[i % bArr2.length]) & GF2Field.MASK];
            b = this.f169P[i & GF2Field.MASK];
            this.f169P[i & GF2Field.MASK] = this.f169P[this.f171s & GF2Field.MASK];
            this.f169P[this.f171s & GF2Field.MASK] = b;
        }
        this.f170n = (byte) 0;
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (i + i2 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i3 + i2 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            for (int i4 = 0; i4 < i2; i4++) {
                this.f171s = this.f169P[(this.f171s + this.f169P[this.f170n & GF2Field.MASK]) & GF2Field.MASK];
                byte b = this.f169P[(this.f169P[this.f169P[this.f171s & GF2Field.MASK] & GF2Field.MASK] + 1) & GF2Field.MASK];
                byte b2 = this.f169P[this.f170n & GF2Field.MASK];
                this.f169P[this.f170n & GF2Field.MASK] = this.f169P[this.f171s & GF2Field.MASK];
                this.f169P[this.f171s & GF2Field.MASK] = b2;
                this.f170n = (byte) ((this.f170n + 1) & GF2Field.MASK);
                bArr2[i4 + i3] = (byte) (b ^ bArr[i4 + i]);
            }
            return i2;
        }
    }

    public void reset() {
        initKey(this.workingKey, this.workingIV);
    }

    public byte returnByte(byte b) {
        this.f171s = this.f169P[(this.f171s + this.f169P[this.f170n & GF2Field.MASK]) & GF2Field.MASK];
        byte b2 = this.f169P[(this.f169P[this.f169P[this.f171s & GF2Field.MASK] & GF2Field.MASK] + 1) & GF2Field.MASK];
        byte b3 = this.f169P[this.f170n & GF2Field.MASK];
        this.f169P[this.f170n & GF2Field.MASK] = this.f169P[this.f171s & GF2Field.MASK];
        this.f169P[this.f171s & GF2Field.MASK] = b3;
        this.f170n = (byte) ((this.f170n + 1) & GF2Field.MASK);
        return (byte) (b2 ^ b);
    }
}
