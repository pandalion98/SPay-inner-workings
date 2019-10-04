/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;

public class TokenAPDUResponse
extends TokenOperationStatus {
    private byte[] baApduResponse = null;
    private short sSW = (short)-28672;

    public byte[] getOutBuffer() {
        int n2 = 2;
        if (this.baApduResponse != null) {
            n2 += this.baApduResponse.length;
        }
        byte[] arrby = new byte[n2];
        if (this.baApduResponse != null) {
            System.arraycopy((Object)this.baApduResponse, (int)0, (Object)arrby, (int)0, (int)this.baApduResponse.length);
            HexUtils.setShort(arrby, (short)this.baApduResponse.length, this.sSW);
            return arrby;
        }
        HexUtils.setShort(arrby, (short)0, this.sSW);
        return arrby;
    }

    public short getsSW() {
        return this.sSW;
    }

    public void setBaApduResponse(byte[] arrby) {
        this.baApduResponse = arrby;
    }

    public void setsSW(short s2) {
        this.sSW = s2;
    }
}

