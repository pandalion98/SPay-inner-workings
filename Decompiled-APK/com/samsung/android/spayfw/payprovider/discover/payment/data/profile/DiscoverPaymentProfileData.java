package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverPaymentProfileData {
    private static final int PRU_LENGTH = 3;
    private static final String TAG = "DCSDK_DiscoverPaymentProfileData";
    private DiscoverPaymentProfile mDefaultProfile;
    private DiscoverPaymentProfile mSelectedProfile;
    private ByteBuffer mSelectedProfilePRU;
    private DiscoverPaymentProfile mTransactionProfile;

    public DiscoverPaymentProfileData(DiscoverPaymentProfile discoverPaymentProfile, DiscoverPaymentProfile discoverPaymentProfile2) {
        this.mSelectedProfile = null;
        this.mSelectedProfilePRU = null;
        this.mDefaultProfile = discoverPaymentProfile;
        this.mSelectedProfile = discoverPaymentProfile2;
        if (this.mSelectedProfile == null) {
            this.mTransactionProfile = this.mDefaultProfile;
            return;
        }
        this.mSelectedProfilePRU = this.mSelectedProfile.getPru();
        composeTransactionProfile();
    }

    public DiscoverPaymentProfileData(DiscoverPaymentProfile discoverPaymentProfile) {
        this(discoverPaymentProfile, null);
    }

    public void setSelectedPaymentProfile(DiscoverPaymentProfile discoverPaymentProfile) {
        this.mSelectedProfile = discoverPaymentProfile;
        if (this.mSelectedProfile != null) {
            this.mSelectedProfilePRU = this.mSelectedProfile.getPru();
            composeTransactionProfile();
        }
    }

    public DiscoverPaymentProfile getPaymentProfile() {
        return this.mTransactionProfile;
    }

    private void composeTransactionProfile() {
        if (this.mSelectedProfile == null) {
            Log.m286e(TAG, "composeTransactionProfile, selected profile is null.");
            return;
        }
        Log.m285d(TAG, "composeTransactionProfile, set selected profile.");
        this.mTransactionProfile = this.mSelectedProfile;
        if (this.mSelectedProfilePRU == null) {
            Log.m286e(TAG, "composeTransactionProfile, selected profile PRU is null.");
        } else if (this.mSelectedProfilePRU.getSize() != PRU_LENGTH) {
            Log.m286e(TAG, "composeTransactionProfile, selected profile PRU length is not equal 3, PRU length is " + this.mSelectedProfilePRU.getSize());
        } else {
            if (!this.mSelectedProfilePRU.checkBit(1, 8)) {
                this.mTransactionProfile.setAip(this.mDefaultProfile.getAip());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, 7)) {
                this.mTransactionProfile.setAfl(this.mDefaultProfile.getAfl());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, 6)) {
                this.mTransactionProfile.getCRM().setCRM_CAC_Default(this.mDefaultProfile.getCRM().getCRM_CAC_Default());
                this.mTransactionProfile.getCRM().setCRM_CAC_Denial(this.mDefaultProfile.getCRM().getCRM_CAC_Denial());
                this.mTransactionProfile.getCRM().setCRM_CAC_Online(this.mDefaultProfile.getCRM().getCRM_CAC_Online());
                this.mTransactionProfile.getCRM().setCRM_CAC_Switch_Interface(this.mDefaultProfile.getCRM().getCRM_CAC_Switch_Interface());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, 5)) {
                this.mTransactionProfile.getCRM().setSTA(this.mDefaultProfile.getCRM().getSTA());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, 4)) {
                this.mTransactionProfile.getCRM().setLCOA(this.mDefaultProfile.getCRM().getLCOA());
                this.mTransactionProfile.getCRM().setUCOA(this.mDefaultProfile.getCRM().getUCOA());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, PRU_LENGTH)) {
                this.mTransactionProfile.getCRM().setLCOL(this.mDefaultProfile.getCRM().getLCOL());
                this.mTransactionProfile.getCRM().setUCOL(this.mDefaultProfile.getCRM().getUCOL());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, 2)) {
                this.mTransactionProfile.getCRM().setCrmCounter(this.mDefaultProfile.getCRM().getCrmCounter());
            }
            if (!this.mSelectedProfilePRU.checkBit(1, 1)) {
                this.mTransactionProfile.getCRM().setCrmAccumulator(this.mDefaultProfile.getCRM().getCrmAccumulator());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 8)) {
                this.mTransactionProfile.setApplicationUsageControl(this.mDefaultProfile.getApplicationUsageControl());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 7)) {
                this.mTransactionProfile.setCpr(this.mDefaultProfile.getCpr());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 6)) {
                this.mTransactionProfile.getCVM().setCVM_CAC_Online_PIN(this.mDefaultProfile.getCVM().getCVM_CAC_Online_PIN());
                this.mTransactionProfile.getCVM().setCVM_CAC_Signature(this.mDefaultProfile.getCVM().getCVM_CAC_Signature());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 5)) {
                this.mTransactionProfile.getCVM().setCVM_Cons_Limit_1(this.mDefaultProfile.getCVM().getCVM_Cons_Limit_1());
                this.mTransactionProfile.getCVM().setCVM_Cons_Limit_2(this.mDefaultProfile.getCVM().getCVM_Cons_Limit_2());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 4)) {
                this.mTransactionProfile.getCVM().setCVM_Cum_Limit_1(this.mDefaultProfile.getCVM().getCVM_Cum_Limit_1());
                this.mTransactionProfile.getCVM().setCVM_Cum_Limit_2(this.mDefaultProfile.getCVM().getCVM_Cum_Limit_2());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, PRU_LENGTH)) {
                this.mTransactionProfile.getCVM().setCVM_Sta_Limit_1(this.mDefaultProfile.getCVM().getCVM_Sta_Limit_1());
                this.mTransactionProfile.getCVM().setCVM_Sta_Limit_2(this.mDefaultProfile.getCVM().getCVM_Sta_Limit_2());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 2)) {
                this.mTransactionProfile.getCVM().setCvmCounter(this.mDefaultProfile.getCVM().getCvmCounter());
            }
            if (!this.mSelectedProfilePRU.checkBit(2, 1)) {
                this.mTransactionProfile.getCVM().setCvmAccumulator(this.mDefaultProfile.getCVM().getCvmAccumulator());
            }
            if (!this.mSelectedProfilePRU.checkBit(PRU_LENGTH, 8)) {
                this.mTransactionProfile.setCtq(this.mDefaultProfile.getCtq());
            }
            if (!this.mSelectedProfilePRU.checkBit(PRU_LENGTH, 7)) {
                this.mTransactionProfile.getCl().setCL_Cons_Limit(this.mDefaultProfile.getCl().getCL_Cons_Limit());
            }
            if (!this.mSelectedProfilePRU.checkBit(PRU_LENGTH, 6)) {
                this.mTransactionProfile.getCl().setCL_Cum_Limit(this.mDefaultProfile.getCl().getCL_Cum_Limit());
            }
            if (!this.mSelectedProfilePRU.checkBit(PRU_LENGTH, 5)) {
                this.mTransactionProfile.getCl().setCL_STA_Limit(this.mDefaultProfile.getCl().getCL_STA_Limit());
            }
            if (!this.mSelectedProfilePRU.checkBit(PRU_LENGTH, 4)) {
                this.mTransactionProfile.getCl().setClCounter(this.mDefaultProfile.getCl().getClCounter());
            }
            if (!this.mSelectedProfilePRU.checkBit(PRU_LENGTH, PRU_LENGTH)) {
                this.mTransactionProfile.getCl().setClAccumulator(this.mDefaultProfile.getCl().getClAccumulator());
            }
        }
    }

    public DiscoverPaymentProfile getSelectedProfile() {
        return this.mSelectedProfile;
    }

    public DiscoverPaymentProfile getDefaultProfile() {
        return this.mDefaultProfile;
    }
}
