package com.visa.tainterface;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import org.bouncycastle.crypto.macs.SkeinMac;

/* renamed from: com.visa.tainterface.a */
public class TrackData {
    private byte[] LK;
    private String LL;
    private String LM;
    private String LN;
    private String LO;
    private String LP;
    private String LQ;
    private String LR;

    public void m1718N(byte[] bArr) {
        if (bArr.length > SkeinMac.SKEIN_256) {
            throw new InvalidParameterException(String.format("token data must be %d bytes long", new Object[]{Integer.valueOf(SkeinMac.SKEIN_256)}));
        } else {
            this.LK = bArr;
        }
    }

    public void cz(String str) {
        if (str.length() != 4) {
            throw new InvalidParameterException(String.format("token expiration data must be %d bytes long and of the format YYMM", new Object[]{Integer.valueOf(4)}));
        } else {
            this.LL = new String(str);
        }
    }

    public void setServiceCode(String str) {
        if (str.length() != 3) {
            throw new InvalidParameterException(String.format("Service code must be %d bytes long", new Object[]{Integer.valueOf(3)}));
        } else {
            this.LM = new String(str);
        }
    }

    public void setTimestamp(String str) {
        if (str.length() != 4) {
            throw new InvalidParameterException(String.format("Timestamp must be %d bytes long and of the format HHHH", new Object[]{Integer.valueOf(4)}));
        } else {
            this.LN = new String(str);
        }
    }

    public void cA(String str) {
        if (str.length() != 2) {
            throw new InvalidParameterException(String.format("Sequence counter must be %d bytes long and of the format CC", new Object[]{Integer.valueOf(2)}));
        } else {
            this.LO = new String(str);
        }
    }

    public void cB(String str) {
        if (str.length() != 4) {
            throw new InvalidParameterException(String.format("ATC must be %d bytes long and of the format AAAA", new Object[]{Integer.valueOf(4)}));
        } else {
            this.LP = new String(str);
        }
    }

    public void setCVV(String str) {
        if (str.length() != 3) {
            throw new InvalidParameterException(String.format("CVV must be %d bytes long", new Object[]{Integer.valueOf(3)}));
        } else {
            this.LQ = new String(str);
        }
    }

    public void cC(String str) {
        if (str.length() != 8) {
            throw new InvalidParameterException(String.format("only %d bytes reserved", new Object[]{Integer.valueOf(8)}));
        } else {
            this.LR = new String(str);
        }
    }

    public byte[] ii() {
        return this.LK;
    }

    public byte[] ij() {
        return this.LL.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] ik() {
        return this.LM.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] il() {
        return this.LN.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] im() {
        return this.LO.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] in() {
        return this.LP.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] io() {
        return this.LQ.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] ip() {
        return this.LR.getBytes(StandardCharsets.US_ASCII);
    }
}
