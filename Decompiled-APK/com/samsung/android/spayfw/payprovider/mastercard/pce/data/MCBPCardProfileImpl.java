package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_BL;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;

public class MCBPCardProfileImpl extends MCBaseCardProfile<DC_CP> {
    private boolean mIsMSTTransactionSupported;
    private boolean mIsNFCTransactionSupported;
    private boolean mIsRPTransactionSupported;
    private DC_CP_BL mMCBLOptionsProfile;
    private DC_CP_MPP mMCPaymentProfile;
    private MCUnusedDGIElements mMCUnusedDGIElements;

    public void initializeMCProfile(long j, DC_CP dc_cp, byte[] bArr, MCProfilesTable mCProfilesTable, byte[] bArr2, MCUnusedDGIElements mCUnusedDGIElements) {
        setDigitalizedCardContainer(dc_cp);
        setTADataContainer(bArr);
        setTAProfilesTable(mCProfilesTable);
        setUniqueTokenReferenceId(j);
        setTaAtcContainer(bArr2);
        setUnusedDGIElements(mCUnusedDGIElements);
        this.mMCPaymentProfile = dc_cp.getDC_CP_MPP();
        this.mIsNFCTransactionSupported = dc_cp.getCL_Supported();
        this.mIsRPTransactionSupported = dc_cp.getRP_Supported();
        this.mMCBLOptionsProfile = dc_cp.getDC_CP_BL();
        this.mIsMSTTransactionSupported = true;
    }

    public DC_CP_MPP getMCPaymentProfile() {
        return this.mMCPaymentProfile;
    }

    public boolean isNFCTransactionSupported() {
        return this.mIsNFCTransactionSupported;
    }

    public boolean isMSTTransactionSupported() {
        return this.mIsMSTTransactionSupported;
    }

    public boolean isRPTransactionSupported() {
        return this.mIsRPTransactionSupported;
    }

    public DC_CP_BL getBLOptionsProfile() {
        return this.mMCBLOptionsProfile;
    }
}
