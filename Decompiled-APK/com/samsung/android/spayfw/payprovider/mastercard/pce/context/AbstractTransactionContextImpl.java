package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import android.text.TextUtils;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPTransactionListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCFilterCriteria;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import java.util.List;

public abstract class AbstractTransactionContextImpl implements MTBPTransactionContext {
    protected static final String TAG = "mcpce_AbstractTransactionContextImpl";
    private ByteArray mAIP;
    private MCCryptoOutput mCryptoOutput;
    private ByteArray mDeviceRREntropy;
    protected List<MCFilterCriteria> mFilters;
    private boolean mIsAlternateAID;
    private boolean mOnlineAllowed;
    private ByteArray mPDOL;
    private ByteArray mPOSCII;
    private int mRRPCounter;
    private ByteArray mTerminalRREntropy;
    protected MCTransactionCredentials mTransactionCredentials;
    protected MCTransactionResult mTransactionError;
    protected MCTransactionInformation mTransactionInformation;
    private MTBPTransactionListener mTransactionListener;
    protected MCTransactionResult mTransactionResult;

    protected abstract MCTransactionCompleteResult getTransactionResult();

    AbstractTransactionContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        this.mOnlineAllowed = true;
        this.mRRPCounter = 0;
        this.mTransactionCredentials = mCTransactionCredentials;
        this.mTransactionInformation = new MCTransactionInformation();
    }

    public void clearCredentials() {
        this.mTransactionCredentials.wipe();
        this.mTransactionCredentials = null;
    }

    public void setCryptoOutput(MCCryptoOutput mCCryptoOutput) {
        this.mCryptoOutput = mCCryptoOutput;
    }

    public MCCryptoOutput getCryptoOutput() {
        return this.mCryptoOutput;
    }

    public MCTransactionCredentials getTransactionCredentials() {
        return this.mTransactionCredentials;
    }

    public MCTransactionInformation getTransactionInformation() {
        return this.mTransactionInformation;
    }

    public ByteArray getPDOL() {
        return this.mPDOL;
    }

    public ByteArray getPOSCII() {
        return this.mPOSCII;
    }

    public void setPOSCII(ByteArray byteArray) {
        this.mPOSCII = byteArray;
    }

    public boolean isAlternateAID() {
        return this.mIsAlternateAID;
    }

    public void setAlternateAID(boolean z) {
        this.mIsAlternateAID = z;
    }

    public void setPDOL(ByteArray byteArray) {
        this.mPDOL = byteArray.clone();
    }

    public ByteArray getAIP() {
        return this.mAIP;
    }

    public void setAIP(ByteArray byteArray) {
        this.mAIP = byteArray.clone();
    }

    public boolean isOnlineAllowed() {
        return this.mOnlineAllowed;
    }

    public void setOnlineAllowed(boolean z) {
        this.mOnlineAllowed = z;
    }

    public void setTransactionResult(MCTransactionResult mCTransactionResult) {
        this.mTransactionResult = mCTransactionResult;
    }

    public void setTransactionListener(MTBPTransactionListener mTBPTransactionListener) {
        this.mTransactionListener = mTBPTransactionListener;
    }

    public MTBPTransactionListener getTransactionListener() {
        return this.mTransactionListener;
    }

    public int getRRPCounter() {
        return this.mRRPCounter;
    }

    public void incrementRRPCounter() {
        this.mRRPCounter++;
    }

    public ByteArray getDeviceRREntropy() {
        return this.mDeviceRREntropy;
    }

    public void setDeviceRREntropy(ByteArray byteArray) {
        this.mDeviceRREntropy = byteArray;
    }

    public ByteArray getTerminalRREntropy() {
        return this.mTerminalRREntropy;
    }

    public void setTerminalRREntropy(ByteArray byteArray) {
        this.mTerminalRREntropy = byteArray;
    }

    public MCTransactionResult getTransactionError() {
        return this.mTransactionError;
    }

    public void setTransactionError(MCTransactionResult mCTransactionResult) {
        this.mTransactionError = mCTransactionResult;
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

    protected void contextConflictError(MCTransactionResult mCTransactionResult) {
        setTransactionResult(mCTransactionResult);
        setTransactionError(mCTransactionResult);
        ByteArray byteArray = ByteArrayFactory.getInstance().getByteArray(3);
        byteArray.setByte(1, (byte) 8);
        setPOSCII(byteArray);
        throw new MCTransactionException(mCTransactionResult);
    }

    public void setNfcError(Bundle bundle) {
        MCTransactionCompleteResult transactionResult = getTransactionResult();
        if (transactionResult == null) {
            Log.m286e(TAG, "stopNfcPay: txnResult is null");
            bundle.putShort("nfcApduErrorCode", (short) 1);
            return;
        }
        if (transactionResult.getTransactionResult() != null) {
            Log.m285d(TAG, "stopNfc: result: " + transactionResult.getTransactionResult().name());
        }
        if (transactionResult.getTransactionError() != null) {
            Log.m285d(TAG, "stopNfc: error: " + transactionResult.getTransactionError().name());
        }
        short s = (transactionResult.isTransactionComplete() && transactionResult.getTransactionError() == null) ? (short) 2 : (short) 3;
        bundle.putShort("nfcApduErrorCode", s);
    }

    protected MCTransactionResult filterCheck(List<MCFilterCriteria> list) {
        MCTransactionResult mCTransactionResult = MCTransactionResult.CONTEXT_CONFLICT;
        if (list == null || list.isEmpty()) {
            return mCTransactionResult;
        }
        MCTransactionResult mCTransactionResult2 = mCTransactionResult;
        for (MCFilterCriteria mCFilterCriteria : list) {
            if (mCFilterCriteria != null) {
                mCTransactionResult = mCFilterCriteria.filterCheck(this.mTransactionInformation);
                if (mCTransactionResult == null) {
                    mCTransactionResult2 = mCTransactionResult;
                } else if (mCTransactionResult.equals(MCTransactionResult.CONTEXT_CONFLICT_PASS)) {
                    Log.m285d(TAG, "Filter Check passed");
                    return mCTransactionResult;
                } else {
                    mCTransactionResult2 = mCTransactionResult;
                }
            }
        }
        return mCTransactionResult2;
    }

    protected void setMerchantNameLocation(Bundle bundle) {
        if (this.mTransactionInformation == null || this.mTransactionInformation.getMerchantNameAndLoc() == null || TextUtils.isEmpty(this.mTransactionInformation.getMerchantNameAndLoc().getString())) {
            Log.m285d(TAG, "No MerchantNameLoc provided");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : this.mTransactionInformation.getMerchantNameAndLoc().getBytes()) {
            if (b == null) {
                break;
            }
            stringBuilder.append((char) b);
        }
        if (TextUtils.isEmpty(stringBuilder.toString())) {
            Log.m285d(TAG, "No MerchantNameLoc provided");
            return;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putString(TransactionStatus.EXTRA_MERCHANT_NAME_TAG, stringBuilder.toString());
        bundle.putBundle(TransactionStatus.EXTRA_PDOL_VALUES, bundle2);
    }
}
