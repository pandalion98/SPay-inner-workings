/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCProfilesTable {
    private final byte[] mTAProfilesRefs;

    public MCProfilesTable(byte[] arrby) {
        this.mTAProfilesRefs = arrby;
    }

    public boolean checkTAProfilesTable() {
        return TAProfile.values().length == this.mTAProfilesRefs.length;
    }

    public byte getTAProfileReference(TAProfile tAProfile) {
        return this.mTAProfilesRefs[tAProfile.ordinal()];
    }

    public boolean isDSRPProfileExist() {
        return this.getTAProfileReference(TAProfile.PROFILE_DSRP_TA_GPO) != 0 && this.getTAProfileReference(TAProfile.PROFILE_DSRP_TA_GAC_ONLINE_CVM) != 0;
    }

    public boolean isMSTProfileExist() {
        return this.getTAProfileReference(TAProfile.PROFILE_MST_TA_GPO) != 0 && this.getTAProfileReference(TAProfile.PROFILE_MST_TA_TRACK1) != 0 && this.getTAProfileReference(TAProfile.PROFILE_MST_TA_TRACK2) != 0;
    }

    public static final class TAProfile
    extends Enum<TAProfile> {
        private static final /* synthetic */ TAProfile[] $VALUES;
        public static final /* enum */ TAProfile PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_GAC_DECLINE_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_GAC_DECLINE_NO_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_GAC_ONLINE_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_GAC_ONLINE_NO_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_GPO;
        public static final /* enum */ TAProfile PROFILE_CL_TA_TRACK1_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_TRACK1_NO_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_TRACK2_CVM;
        public static final /* enum */ TAProfile PROFILE_CL_TA_TRACK2_NO_CVM;
        public static final /* enum */ TAProfile PROFILE_DSRP_TA_GAC_ONLINE_CVM;
        public static final /* enum */ TAProfile PROFILE_DSRP_TA_GPO;
        public static final /* enum */ TAProfile PROFILE_MST_TA_GPO;
        public static final /* enum */ TAProfile PROFILE_MST_TA_TRACK1;
        public static final /* enum */ TAProfile PROFILE_MST_TA_TRACK2;

        static {
            PROFILE_CL_TA_GPO = new TAProfile();
            PROFILE_CL_TA_TRACK1_NO_CVM = new TAProfile();
            PROFILE_CL_TA_TRACK2_NO_CVM = new TAProfile();
            PROFILE_CL_TA_TRACK1_CVM = new TAProfile();
            PROFILE_CL_TA_TRACK2_CVM = new TAProfile();
            PROFILE_CL_TA_GAC_DECLINE_NO_CVM = new TAProfile();
            PROFILE_CL_TA_GAC_DECLINE_CVM = new TAProfile();
            PROFILE_CL_TA_GAC_ONLINE_NO_CVM = new TAProfile();
            PROFILE_CL_TA_GAC_ONLINE_CVM = new TAProfile();
            PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM = new TAProfile();
            PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM = new TAProfile();
            PROFILE_MST_TA_GPO = new TAProfile();
            PROFILE_MST_TA_TRACK1 = new TAProfile();
            PROFILE_MST_TA_TRACK2 = new TAProfile();
            PROFILE_DSRP_TA_GPO = new TAProfile();
            PROFILE_DSRP_TA_GAC_ONLINE_CVM = new TAProfile();
            TAProfile[] arrtAProfile = new TAProfile[]{PROFILE_CL_TA_GPO, PROFILE_CL_TA_TRACK1_NO_CVM, PROFILE_CL_TA_TRACK2_NO_CVM, PROFILE_CL_TA_TRACK1_CVM, PROFILE_CL_TA_TRACK2_CVM, PROFILE_CL_TA_GAC_DECLINE_NO_CVM, PROFILE_CL_TA_GAC_DECLINE_CVM, PROFILE_CL_TA_GAC_ONLINE_NO_CVM, PROFILE_CL_TA_GAC_ONLINE_CVM, PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM, PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM, PROFILE_MST_TA_GPO, PROFILE_MST_TA_TRACK1, PROFILE_MST_TA_TRACK2, PROFILE_DSRP_TA_GPO, PROFILE_DSRP_TA_GAC_ONLINE_CVM};
            $VALUES = arrtAProfile;
        }

        public static TAProfile valueOf(String string) {
            return (TAProfile)Enum.valueOf(TAProfile.class, (String)string);
        }

        public static TAProfile[] values() {
            return (TAProfile[])$VALUES.clone();
        }
    }

}

