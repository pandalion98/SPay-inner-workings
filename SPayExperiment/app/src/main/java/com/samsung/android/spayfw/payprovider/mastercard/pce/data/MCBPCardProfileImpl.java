/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_BL;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;

public class MCBPCardProfileImpl
extends MCBaseCardProfile<DC_CP> {
    private boolean mIsMSTTransactionSupported;
    private boolean mIsNFCTransactionSupported;
    private boolean mIsRPTransactionSupported;
    private DC_CP_BL mMCBLOptionsProfile;
    private DC_CP_MPP mMCPaymentProfile;
    private MCUnusedDGIElements mMCUnusedDGIElements;

    public DC_CP_BL getBLOptionsProfile() {
        return this.mMCBLOptionsProfile;
    }

    public DC_CP_MPP getMCPaymentProfile() {
        return this.mMCPaymentProfile;
    }

    public void initializeMCProfile(long l2, DC_CP dC_CP, byte[] arrby, MCProfilesTable mCProfilesTable, byte[] arrby2, MCUnusedDGIElements mCUnusedDGIElements) {
        this.setDigitalizedCardContainer(dC_CP);
        this.setTADataContainer(arrby);
        this.setTAProfilesTable(mCProfilesTable);
        this.setUniqueTokenReferenceId(l2);
        this.setTaAtcContainer(arrby2);
        this.setUnusedDGIElements(mCUnusedDGIElements);
        this.mMCPaymentProfile = dC_CP.getDC_CP_MPP();
        this.mIsNFCTransactionSupported = dC_CP.getCL_Supported();
        this.mIsRPTransactionSupported = dC_CP.getRP_Supported();
        this.mMCBLOptionsProfile = dC_CP.getDC_CP_BL();
        this.mIsMSTTransactionSupported = true;
    }

    public boolean isMSTTransactionSupported() {
        return this.mIsMSTTransactionSupported;
    }

    public boolean isNFCTransactionSupported() {
        return this.mIsNFCTransactionSupported;
    }

    public boolean isRPTransactionSupported() {
        return this.mIsRPTransactionSupported;
    }
}

