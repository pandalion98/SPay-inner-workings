/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import android.text.TextUtils;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPTransactionListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCFilterCriteria;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTransactionContextImpl
implements MTBPTransactionContext {
    protected static final String TAG = "mcpce_AbstractTransactionContextImpl";
    private ByteArray mAIP;
    private MCCryptoOutput mCryptoOutput;
    private ByteArray mDeviceRREntropy;
    protected List<MCFilterCriteria> mFilters;
    private boolean mIsAlternateAID;
    private boolean mOnlineAllowed = true;
    private ByteArray mPDOL;
    private ByteArray mPOSCII;
    private int mRRPCounter = 0;
    private ByteArray mTerminalRREntropy;
    protected MCTransactionCredentials mTransactionCredentials;
    protected MCTransactionResult mTransactionError;
    protected MCTransactionInformation mTransactionInformation;
    private MTBPTransactionListener mTransactionListener;
    protected MCTransactionResult mTransactionResult;

    AbstractTransactionContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        this.mTransactionCredentials = mCTransactionCredentials;
        this.mTransactionInformation = new MCTransactionInformation();
    }

    @Override
    public void clearCredentials() {
        this.mTransactionCredentials.wipe();
        this.mTransactionCredentials = null;
    }

    protected void contextConflictError(MCTransactionResult mCTransactionResult) {
        this.setTransactionResult(mCTransactionResult);
        this.setTransactionError(mCTransactionResult);
        ByteArray byteArray = ByteArrayFactory.getInstance().getByteArray(3);
        byteArray.setByte(1, (byte)8);
        this.setPOSCII(byteArray);
        throw new MCTransactionException((Object)mCTransactionResult);
    }

    protected MCTransactionResult filterCheck(List<MCFilterCriteria> list) {
        MCTransactionResult mCTransactionResult = MCTransactionResult.CONTEXT_CONFLICT;
        if (list == null || list.isEmpty()) {
            return mCTransactionResult;
        }
        Iterator iterator = list.iterator();
        MCTransactionResult mCTransactionResult2 = mCTransactionResult;
        do {
            MCTransactionResult mCTransactionResult3;
            if (iterator.hasNext()) {
                MCFilterCriteria mCFilterCriteria = (MCFilterCriteria)iterator.next();
                if (mCFilterCriteria == null) continue;
                mCTransactionResult3 = mCFilterCriteria.filterCheck(this.mTransactionInformation);
                if (mCTransactionResult3 != null) {
                    if (!mCTransactionResult3.equals((Object)MCTransactionResult.CONTEXT_CONFLICT_PASS)) {
                        mCTransactionResult2 = mCTransactionResult3;
                        continue;
                    }
                    Log.d(TAG, "Filter Check passed");
                    return mCTransactionResult3;
                }
            } else {
                return mCTransactionResult2;
            }
            mCTransactionResult2 = mCTransactionResult3;
        } while (true);
    }

    @Override
    public ByteArray getAIP() {
        return this.mAIP;
    }

    @Override
    public MCCryptoOutput getCryptoOutput() {
        return this.mCryptoOutput;
    }

    @Override
    public ByteArray getDeviceRREntropy() {
        return this.mDeviceRREntropy;
    }

    @Override
    public ByteArray getPDOL() {
        return this.mPDOL;
    }

    @Override
    public ByteArray getPOSCII() {
        return this.mPOSCII;
    }

    @Override
    public int getRRPCounter() {
        return this.mRRPCounter;
    }

    @Override
    public ByteArray getTerminalRREntropy() {
        return this.mTerminalRREntropy;
    }

    @Override
    public MCTransactionCredentials getTransactionCredentials() {
        return this.mTransactionCredentials;
    }

    @Override
    public MCTransactionResult getTransactionError() {
        return this.mTransactionError;
    }

    @Override
    public MCTransactionInformation getTransactionInformation() {
        return this.mTransactionInformation;
    }

    @Override
    public MTBPTransactionListener getTransactionListener() {
        return this.mTransactionListener;
    }

    protected abstract MCTransactionCompleteResult getTransactionResult();

    @Override
    public void incrementRRPCounter() {
        this.mRRPCounter = 1 + this.mRRPCounter;
    }

    @Override
    public boolean isAlternateAID() {
        return this.mIsAlternateAID;
    }

    @Override
    public boolean isOnlineAllowed() {
        return this.mOnlineAllowed;
    }

    @Override
    public void setAIP(ByteArray byteArray) {
        this.mAIP = byteArray.clone();
    }

    @Override
    public void setAlternateAID(boolean bl) {
        this.mIsAlternateAID = bl;
    }

    @Override
    public void setCryptoOutput(MCCryptoOutput mCCryptoOutput) {
        this.mCryptoOutput = mCCryptoOutput;
    }

    @Override
    public void setDeviceRREntropy(ByteArray byteArray) {
        this.mDeviceRREntropy = byteArray;
    }

    protected void setMerchantNameLocation(Bundle bundle) {
        if (this.mTransactionInformation == null || this.mTransactionInformation.getMerchantNameAndLoc() == null || TextUtils.isEmpty((CharSequence)this.mTransactionInformation.getMerchantNameAndLoc().getString())) {
            Log.d("mcpce_AbstractTransactionContextImpl", "No MerchantNameLoc provided");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        byte[] arrby = this.mTransactionInformation.getMerchantNameAndLoc().getBytes();
        int n2 = arrby.length;
        int n3 = 0;
        do {
            byte by;
            if (n3 >= n2 || (by = arrby[n3]) == 0) {
                if (!TextUtils.isEmpty((CharSequence)stringBuilder.toString())) break;
                Log.d("mcpce_AbstractTransactionContextImpl", "No MerchantNameLoc provided");
                return;
            }
            stringBuilder.append((char)by);
            ++n3;
        } while (true);
        Bundle bundle2 = new Bundle();
        bundle2.putString("9F4E", stringBuilder.toString());
        bundle.putBundle("pdolValues", bundle2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setNfcError(Bundle bundle) {
        MCTransactionCompleteResult mCTransactionCompleteResult = this.getTransactionResult();
        if (mCTransactionCompleteResult == null) {
            Log.e("mcpce_AbstractTransactionContextImpl", "stopNfcPay: txnResult is null");
            bundle.putShort("nfcApduErrorCode", (short)1);
            return;
        }
        if (mCTransactionCompleteResult.getTransactionResult() != null) {
            Log.d("mcpce_AbstractTransactionContextImpl", "stopNfc: result: " + mCTransactionCompleteResult.getTransactionResult().name());
        }
        if (mCTransactionCompleteResult.getTransactionError() != null) {
            Log.d("mcpce_AbstractTransactionContextImpl", "stopNfc: error: " + mCTransactionCompleteResult.getTransactionError().name());
        }
        short s2 = mCTransactionCompleteResult.isTransactionComplete() && mCTransactionCompleteResult.getTransactionError() == null ? (short)2 : 3;
        bundle.putShort("nfcApduErrorCode", s2);
    }

    public void setOnlineAllowed(boolean bl) {
        this.mOnlineAllowed = bl;
    }

    @Override
    public void setPDOL(ByteArray byteArray) {
        this.mPDOL = byteArray.clone();
    }

    @Override
    public void setPOSCII(ByteArray byteArray) {
        this.mPOSCII = byteArray;
    }

    @Override
    public void setTerminalRREntropy(ByteArray byteArray) {
        this.mTerminalRREntropy = byteArray;
    }

    @Override
    public void setTransactionError(MCTransactionResult mCTransactionResult) {
        this.mTransactionError = mCTransactionResult;
    }

    @Override
    public void setTransactionListener(MTBPTransactionListener mTBPTransactionListener) {
        this.mTransactionListener = mTBPTransactionListener;
    }

    @Override
    public void setTransactionResult(MCTransactionResult mCTransactionResult) {
        this.mTransactionResult = mCTransactionResult;
    }

    public void wipe() {
        Utils.clearByteArray(this.mAIP);
        Utils.clearByteArray(this.mPDOL);
        Utils.clearByteArray(this.mPOSCII);
        Utils.clearByteArray(this.mDeviceRREntropy);
        Utils.clearByteArray(this.mTerminalRREntropy);
        this.mAIP = null;
        this.mPDOL = null;
        this.mPOSCII = null;
        this.mRRPCounter = 0;
        this.mFilters = null;
    }
}

