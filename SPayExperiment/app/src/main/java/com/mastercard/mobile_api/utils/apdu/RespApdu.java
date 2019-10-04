/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils.apdu;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;

public class RespApdu {
    private ByteArray val;

    public RespApdu() {
    }

    public RespApdu(int n2, int n3) {
        this.val = ByteArrayFactory.getInstance().getFromWord(n2);
    }

    public RespApdu(ByteArray byteArray) {
        this.val = byteArray;
    }

    public RespApdu(ByteArray byteArray, ByteArray byteArray2) {
        this.setValue(byteArray, byteArray2);
    }

    public RespApdu(byte[] arrby, int n2) {
        this.val = ByteArrayFactory.getInstance().getByteArray(arrby, n2);
    }

    public ByteArray getByteArray() {
        return this.val;
    }

    public byte[] getBytes() {
        return this.val.getBytes();
    }

    public void setValue(ByteArray byteArray, ByteArray byteArray2) {
        this.val = byteArray;
        this.val.append(byteArray2);
    }

    public void setValueAndSuccess(ByteArray byteArray) {
        this.val = byteArray;
        ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray(2);
        byteArray2.setByte(0, (byte)-112);
        byteArray2.setByte(1, (byte)0);
        this.val.append(byteArray2);
    }
}

