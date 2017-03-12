package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.samsung.android.spayfw.appinterface.ISO7816;

public class TokenAPDUResponse extends TokenOperationStatus {
    private byte[] baApduResponse;
    private short sSW;

    public TokenAPDUResponse() {
        this.sSW = ISO7816.SW_NO_ERROR;
        this.baApduResponse = null;
    }

    public short getsSW() {
        return this.sSW;
    }

    public void setsSW(short s) {
        this.sSW = s;
    }

    public void setBaApduResponse(byte[] bArr) {
        this.baApduResponse = bArr;
    }

    public byte[] getOutBuffer() {
        int i = 2;
        if (this.baApduResponse != null) {
            i = 2 + this.baApduResponse.length;
        }
        Object obj = new byte[i];
        if (this.baApduResponse != null) {
            System.arraycopy(this.baApduResponse, 0, obj, 0, this.baApduResponse.length);
            HexUtils.setShort(obj, (short) this.baApduResponse.length, this.sSW);
        } else {
            HexUtils.setShort(obj, (short) 0, this.sSW);
        }
        return obj;
    }
}
