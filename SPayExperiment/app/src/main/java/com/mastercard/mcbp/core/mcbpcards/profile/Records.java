/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class Records {
    private ByteArray SFI;
    private ByteArray recordNumber;
    private ByteArray recordValue;

    public byte getRecordNumber() {
        return this.recordNumber.getByte(0);
    }

    public ByteArray getRecordValue() {
        return this.recordValue;
    }

    public byte getSFI() {
        return this.SFI.getByte(0);
    }

    public void setRecordNumber(ByteArray byteArray) {
        this.recordNumber = byteArray;
    }

    public void setRecordValue(ByteArray byteArray) {
        this.recordValue = byteArray;
    }

    public void setSFI(ByteArray byteArray) {
        this.SFI = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.recordNumber);
        Utils.clearByteArray(this.recordValue);
        Utils.clearByteArray(this.SFI);
    }
}

