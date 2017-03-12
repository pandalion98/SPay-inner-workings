package org.bouncycastle.crypto.engines;

import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class VMPCKSA3Engine extends VMPCEngine {
    public String getAlgorithmName() {
        return "VMPC-KSA3";
    }

    protected void initKey(byte[] bArr, byte[] bArr2) {
        int i;
        this.s = (byte) 0;
        this.P = new byte[SkeinMac.SKEIN_256];
        for (i = 0; i < SkeinMac.SKEIN_256; i++) {
            this.P[i] = (byte) i;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.s = this.P[((this.s + this.P[i & GF2Field.MASK]) + bArr[i % bArr.length]) & GF2Field.MASK];
            byte b = this.P[i & GF2Field.MASK];
            this.P[i & GF2Field.MASK] = this.P[this.s & GF2Field.MASK];
            this.P[this.s & GF2Field.MASK] = b;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.s = this.P[((this.s + this.P[i & GF2Field.MASK]) + bArr2[i % bArr2.length]) & GF2Field.MASK];
            b = this.P[i & GF2Field.MASK];
            this.P[i & GF2Field.MASK] = this.P[this.s & GF2Field.MASK];
            this.P[this.s & GF2Field.MASK] = b;
        }
        for (i = 0; i < McTACommands.MOP_MC_TA_CMD_CASD_BASE; i++) {
            this.s = this.P[((this.s + this.P[i & GF2Field.MASK]) + bArr[i % bArr.length]) & GF2Field.MASK];
            b = this.P[i & GF2Field.MASK];
            this.P[i & GF2Field.MASK] = this.P[this.s & GF2Field.MASK];
            this.P[this.s & GF2Field.MASK] = b;
        }
        this.n = (byte) 0;
    }
}
