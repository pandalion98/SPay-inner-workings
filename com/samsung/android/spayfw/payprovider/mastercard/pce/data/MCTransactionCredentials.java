package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;

public class MCTransactionCredentials {
    protected final ByteArrayFactory baf;
    protected ByteArray mATC;
    private MCCVMResult mCVMResult;
    protected ByteArray mIDN;
    private byte[] mSecureObject;
    private int mTAProfileType;
    private MCProfilesTable mTAProfilesTable;
    private byte[] mWrappedAtcObject;

    public MCTransactionCredentials() {
        this.baf = ByteArrayFactory.getInstance();
    }

    public byte[] getmWrappedAtcObject() {
        return this.mWrappedAtcObject;
    }

    public void setmWrappedAtcObject(byte[] bArr) {
        this.mWrappedAtcObject = bArr;
    }

    public void setSecureObject(byte[] bArr) {
        this.mSecureObject = bArr;
    }

    public byte[] getSecureObject() {
        return this.mSecureObject;
    }

    public void setATC(byte[] bArr) {
        this.mATC = this.baf.getByteArray(bArr, bArr.length);
    }

    public void setIDN(byte[] bArr) {
        this.mIDN = this.baf.getByteArray(bArr, bArr.length);
    }

    public MCCVMResult getCVMResult() {
        return this.mCVMResult;
    }

    public void setCVMResult(MCCVMResult mCCVMResult) {
        this.mCVMResult = mCCVMResult;
    }

    public ByteArray getIDN() {
        return this.mIDN;
    }

    public ByteArray getATC() {
        return this.mATC;
    }

    public void setTAProfilesTable(MCProfilesTable mCProfilesTable) {
        this.mTAProfilesTable = mCProfilesTable;
    }

    public MCProfilesTable getTAProfilesTable() {
        return this.mTAProfilesTable;
    }

    public void setProfileType(int i) {
        this.mTAProfileType = i;
    }

    public int getProfileType() {
        return this.mTAProfileType;
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
