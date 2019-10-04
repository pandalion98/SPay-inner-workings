/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPTransactionListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public interface MTBPTransactionContext {
    public void checkContext();

    public void clearCredentials();

    public ByteArray getAIP();

    public MCCryptoOutput getCryptoOutput();

    public ByteArray getDeviceRREntropy();

    public ByteArray getPDOL();

    public ByteArray getPOSCII();

    public int getRRPCounter();

    public ByteArray getTerminalRREntropy();

    public MCTransactionCredentials getTransactionCredentials();

    public MCTransactionResult getTransactionError();

    public MCTransactionInformation getTransactionInformation();

    public MTBPTransactionListener getTransactionListener();

    public void incrementRRPCounter();

    public boolean isAlternateAID();

    public boolean isOnlineAllowed();

    public void setAIP(ByteArray var1);

    public void setAlternateAID(boolean var1);

    public void setCryptoOutput(MCCryptoOutput var1);

    public void setDeviceRREntropy(ByteArray var1);

    public void setNfcError(Bundle var1);

    public void setPDOL(ByteArray var1);

    public void setPOSCII(ByteArray var1);

    public void setTerminalRREntropy(ByteArray var1);

    public void setTransactionError(MCTransactionResult var1);

    public void setTransactionListener(MTBPTransactionListener var1);

    public void setTransactionResult(MCTransactionResult var1);

    public Bundle stopNfc();
}

