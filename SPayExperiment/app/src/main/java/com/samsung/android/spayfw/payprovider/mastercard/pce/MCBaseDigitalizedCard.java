/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.os.Bundle;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCardActivationResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCardListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;

public interface MCBaseDigitalizedCard {
    public void clearContext();

    public MCBaseCardProfile<?> getCardProfile();

    public DSRPOutputData getPayInfoData(DSRPInputData var1);

    public MTBPCardActivationResult initTransaction(MCCVMResult var1, MTBPCardListener var2);

    public boolean isMSTSupported();

    public boolean isRPSupported();

    public boolean isReadyForMSTTransaction();

    public boolean isReadyForNFCTransaction();

    public boolean isReadyForRPTransaction();

    public void loadCardProfile(long var1);

    public long prepareMSTData();

    public byte[] proccessApdu(byte[] var1);

    public Bundle processDeactivated();

    public void setCardProfile(MCBaseCardProfile<?> var1);
}

