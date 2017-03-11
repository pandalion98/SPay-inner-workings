package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.os.Bundle;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;

public interface MCBaseDigitalizedCard {
    void clearContext();

    MCBaseCardProfile<?> getCardProfile();

    DSRPOutputData getPayInfoData(DSRPInputData dSRPInputData);

    MTBPCardActivationResult initTransaction(MCCVMResult mCCVMResult, MTBPCardListener mTBPCardListener);

    boolean isMSTSupported();

    boolean isRPSupported();

    boolean isReadyForMSTTransaction();

    boolean isReadyForNFCTransaction();

    boolean isReadyForRPTransaction();

    void loadCardProfile(long j);

    long prepareMSTData();

    byte[] proccessApdu(byte[] bArr);

    Bundle processDeactivated();

    void setCardProfile(MCBaseCardProfile<?> mCBaseCardProfile);
}
