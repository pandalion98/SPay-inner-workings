package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCProfilesTable {
    private final byte[] mTAProfilesRefs;

    public enum TAProfile {
        PROFILE_CL_TA_GPO,
        PROFILE_CL_TA_TRACK1_NO_CVM,
        PROFILE_CL_TA_TRACK2_NO_CVM,
        PROFILE_CL_TA_TRACK1_CVM,
        PROFILE_CL_TA_TRACK2_CVM,
        PROFILE_CL_TA_GAC_DECLINE_NO_CVM,
        PROFILE_CL_TA_GAC_DECLINE_CVM,
        PROFILE_CL_TA_GAC_ONLINE_NO_CVM,
        PROFILE_CL_TA_GAC_ONLINE_CVM,
        PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM,
        PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM,
        PROFILE_MST_TA_GPO,
        PROFILE_MST_TA_TRACK1,
        PROFILE_MST_TA_TRACK2,
        PROFILE_DSRP_TA_GPO,
        PROFILE_DSRP_TA_GAC_ONLINE_CVM
    }

    public MCProfilesTable(byte[] bArr) {
        this.mTAProfilesRefs = bArr;
    }

    public boolean checkTAProfilesTable() {
        return TAProfile.values().length == this.mTAProfilesRefs.length;
    }

    public byte getTAProfileReference(TAProfile tAProfile) {
        return this.mTAProfilesRefs[tAProfile.ordinal()];
    }

    public boolean isMSTProfileExist() {
        return (getTAProfileReference(TAProfile.PROFILE_MST_TA_GPO) == null || getTAProfileReference(TAProfile.PROFILE_MST_TA_TRACK1) == null || getTAProfileReference(TAProfile.PROFILE_MST_TA_TRACK2) == null) ? false : true;
    }

    public boolean isDSRPProfileExist() {
        return (getTAProfileReference(TAProfile.PROFILE_DSRP_TA_GPO) == null || getTAProfileReference(TAProfile.PROFILE_DSRP_TA_GAC_ONLINE_CVM) == null) ? false : true;
    }
}
