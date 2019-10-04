/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;

public class MCTransactionCredentials {
    protected final ByteArrayFactory baf = ByteArrayFactory.getInstance();
    protected ByteArray mATC;
    private MCCVMResult mCVMResult;
    protected ByteArray mIDN;
    private byte[] mSecureObject;
    private int mTAProfileType;
    private MCProfilesTable mTAProfilesTable;
    private byte[] mWrappedAtcObject;

    public ByteArray getATC() {
        return this.mATC;
    }

    public MCCVMResult getCVMResult() {
        return this.mCVMResult;
    }

    public ByteArray getIDN() {
        return this.mIDN;
    }

    public int getProfileType() {
        return this.mTAProfileType;
    }

    public byte[] getSecureObject() {
        return this.mSecureObject;
    }

    public MCProfilesTable getTAProfilesTable() {
        return this.mTAProfilesTable;
    }

    public byte[] getmWrappedAtcObject() {
        return this.mWrappedAtcObject;
    }

    public void setATC(byte[] arrby) {
        this.mATC = this.baf.getByteArray(arrby, arrby.length);
    }

    public void setCVMResult(MCCVMResult mCCVMResult) {
        this.mCVMResult = mCCVMResult;
    }

    public void setIDN(byte[] arrby) {
        this.mIDN = this.baf.getByteArray(arrby, arrby.length);
    }

    public void setProfileType(int n2) {
        this.mTAProfileType = n2;
    }

    public void setSecureObject(byte[] arrby) {
        this.mSecureObject = arrby;
    }

    public void setTAProfilesTable(MCProfilesTable mCProfilesTable) {
        this.mTAProfilesTable = mCProfilesTable;
    }

    public void setmWrappedAtcObject(byte[] arrby) {
        this.mWrappedAtcObject = arrby;
    }

    public void wipe() {
        Utils.clearByteArray(this.mSecureObject);
        Utils.clearByteArray(this.mATC);
        Utils.clearByteArray(this.mIDN);
        Utils.clearByteArray(this.mWrappedAtcObject);
        this.mCVMResult = null;
        this.mTAProfileType = 0;
    }
}

