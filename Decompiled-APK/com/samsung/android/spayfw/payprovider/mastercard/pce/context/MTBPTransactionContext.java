package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPTransactionListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public interface MTBPTransactionContext {
    void checkContext();

    void clearCredentials();

    ByteArray getAIP();

    MCCryptoOutput getCryptoOutput();

    ByteArray getDeviceRREntropy();

    ByteArray getPDOL();

    ByteArray getPOSCII();

    int getRRPCounter();

    ByteArray getTerminalRREntropy();

    MCTransactionCredentials getTransactionCredentials();

    MCTransactionResult getTransactionError();

    MCTransactionInformation getTransactionInformation();

    MTBPTransactionListener getTransactionListener();

    void incrementRRPCounter();

    boolean isAlternateAID();

    boolean isOnlineAllowed();

    void setAIP(ByteArray byteArray);

    void setAlternateAID(boolean z);

    void setCryptoOutput(MCCryptoOutput mCCryptoOutput);

    void setDeviceRREntropy(ByteArray byteArray);

    void setNfcError(Bundle bundle);

    void setPDOL(ByteArray byteArray);

    void setPOSCII(ByteArray byteArray);

    void setTerminalRREntropy(ByteArray byteArray);

    void setTransactionError(MCTransactionResult mCTransactionResult);

    void setTransactionListener(MTBPTransactionListener mTBPTransactionListener);

    void setTransactionResult(MCTransactionResult mCTransactionResult);

    Bundle stopNfc();
}
