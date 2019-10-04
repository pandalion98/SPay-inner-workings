/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.TransactionProfilesContainer;

public class ClearProfileData {
    private static final String TAG = "DCSDK_ClearProfileData";
    private String ALT_AID1_FCI;
    private String ALT_AID2_FCI;
    private String ATC_Limit;
    private String AVN;
    private String Application_State;
    private String CACO;
    private String CAVV_Key_Indicator;
    private String CL_ACO;
    private String CRM_Country_Code;
    private String CRM_Currency_Code;
    private String Cardholder_Name;
    private String Common_debit_FCI;
    private String DPAS_AID_FCI;
    private String Effective_Date;
    private String Exp_Date;
    private String Failed_MAC_limit;
    private String IADOL;
    private String IDDT0;
    private String IDDT1;
    private String IDDT2;
    private String Issuer_Application_Data;
    private String Issuer_Country_Code;
    private String Issuer_Life_Cycle_Data;
    private String Lifetime_MAC_limit;
    private String PAN_Seq_NBR;
    private String PASSCODE_Bypass;
    private String PASSCODE_Bypass_Limit;
    private String PASSCODE_Retry_Counter;
    private String PASSCODE_Retry_Limit;
    private String PDOL_Profile_check;
    private String PIN_Cryptographic_limit;
    private String PPSE_FCI;
    private String Passcode_reference_data;
    private String Secondary_Currency_1;
    private String Secondary_Currency_2;
    private String Service_Code;
    private String Session_MAC_limit;
    private String Token_PAN;
    private String Track_1_Data_for_MST;
    @SerializedName(value="Track_1_Data_for_ZIP/MS_Mode")
    private String Track_1_Data_for_ZIPMode;
    private String Track_2_Data_for_MST;
    @SerializedName(value="Track_2_Data_for_ZIP/MS_Mode")
    private String Track_2_Data_for_ZIPMode;
    private String Track_2_Equivalent_Data;
    private String Zip_AID_FCI;
    private TokenContraints constraints;
    private TransactionProfilesContainer profiles;
    private String tokenId;
    private String track_data_hash;
    private String transaction_log_format;

    public static String logger(String string, String string2) {
        if (string2 == null) {
            // empty if block
        }
        return string2;
    }

    private byte[] toBytes(String string) {
        if (string != null) {
            c.d(TAG, "toBytes: " + string);
            return string.getBytes();
        }
        return null;
    }

    public String getALT_AID1_FCI() {
        return ClearProfileData.logger("ALT_AID1_FCI", this.ALT_AID1_FCI);
    }

    public String getALT_AID2_FCI() {
        return ClearProfileData.logger("ALT_AID2_FCI", this.ALT_AID2_FCI);
    }

    public String getATC_Limit() {
        return ClearProfileData.logger("ATC_Limit", this.ATC_Limit);
    }

    public String getAVN() {
        return ClearProfileData.logger("AVN", this.AVN);
    }

    public String getApplication_State() {
        return ClearProfileData.logger("Application_State", this.Application_State);
    }

    public String getCACO() {
        return ClearProfileData.logger("CACO", this.CACO);
    }

    public String getCAVV_Key_Indicator() {
        return ClearProfileData.logger("CAVV_Key_Indicator", this.CAVV_Key_Indicator);
    }

    public String getCL_ACO() {
        return ClearProfileData.logger("CL_ACO", this.CL_ACO);
    }

    public String getCRM_Country_Code() {
        return ClearProfileData.logger("CRM_Country_Code", this.CRM_Country_Code);
    }

    public String getCRM_Currency_Code() {
        return ClearProfileData.logger("CRM_Currency_Code", this.CRM_Currency_Code);
    }

    public String getCardholder_Name() {
        return ClearProfileData.logger("Cardholder_Name", this.Cardholder_Name);
    }

    public String getCommon_debit_FCI() {
        return ClearProfileData.logger("Common_debit_FCI", this.Common_debit_FCI);
    }

    public TokenContraints getConstraints() {
        return this.constraints;
    }

    public String getDPAS_AID_FCI() {
        return ClearProfileData.logger("DPAS_AID_FCI", this.DPAS_AID_FCI);
    }

    public String getEffective_Date() {
        return ClearProfileData.logger("Effective_Date", this.Effective_Date);
    }

    public String getExp_Date() {
        return ClearProfileData.logger("Exp_Date", this.Exp_Date);
    }

    public String getFailed_MAC_limit() {
        return ClearProfileData.logger("Failed_MAC_limit", this.Failed_MAC_limit);
    }

    public String getIADOL() {
        return ClearProfileData.logger("IADOL", this.IADOL);
    }

    public String getIDDT0() {
        return ClearProfileData.logger("IDDT0", this.IDDT0);
    }

    public String getIDDT1() {
        return ClearProfileData.logger("IDDT1", this.IDDT1);
    }

    public String getIDDT2() {
        return ClearProfileData.logger("IDDT2", this.IDDT2);
    }

    public String getIssuer_Application_Data() {
        return ClearProfileData.logger("Issuer_Application_Data", this.Issuer_Application_Data);
    }

    public String getIssuer_Country_Code() {
        return ClearProfileData.logger("Issuer_Country_Code", this.Issuer_Country_Code);
    }

    public String getIssuer_Life_Cycle_Data() {
        return ClearProfileData.logger("Issuer_Life_Cycle_Data", this.Issuer_Life_Cycle_Data);
    }

    public String getLifetime_MAC_limit() {
        return ClearProfileData.logger("Lifetime_MAC_limit", this.Lifetime_MAC_limit);
    }

    public String getPAN_Seq_NBR() {
        return ClearProfileData.logger("PAN_Seq_NBR", this.PAN_Seq_NBR);
    }

    public String getPASSCODE_Bypass() {
        return ClearProfileData.logger("PASSCODE_Bypass", this.PASSCODE_Bypass);
    }

    public String getPASSCODE_Bypass_Limit() {
        return ClearProfileData.logger("PASSCODE_Bypass_Limit", this.PASSCODE_Bypass_Limit);
    }

    public String getPASSCODE_Retry_Counter() {
        return ClearProfileData.logger("PASSCODE_Retry_Counter", this.PASSCODE_Retry_Counter);
    }

    public String getPASSCODE_Retry_Limit() {
        return ClearProfileData.logger("PASSCODE_Retry_Limit", this.PASSCODE_Retry_Limit);
    }

    public String getPDOL_Profile_check() {
        return ClearProfileData.logger("PDOL_Profile_check", this.PDOL_Profile_check);
    }

    public String getPIN_Cryptographic_limit() {
        return ClearProfileData.logger("PIN_Cryptographic_limit", this.PIN_Cryptographic_limit);
    }

    public String getPPSE_FCI() {
        return ClearProfileData.logger("PPSE_FCI", this.PPSE_FCI);
    }

    public String getPasscode_reference_data() {
        return ClearProfileData.logger("Passcode_reference_data", this.Passcode_reference_data);
    }

    public TransactionProfilesContainer getProfiles() {
        return this.profiles;
    }

    public String getSecondary_Currency_1() {
        return ClearProfileData.logger("Secondary_Currency_1", this.Secondary_Currency_1);
    }

    public String getSecondary_Currency_2() {
        return ClearProfileData.logger("Secondary_Currency_2", this.Secondary_Currency_2);
    }

    public String getService_Code() {
        return ClearProfileData.logger("Service_Code", this.Service_Code);
    }

    public String getSession_MAC_limit() {
        return ClearProfileData.logger("Session_MAC_limit", this.Session_MAC_limit);
    }

    public String getTokenId() {
        return ClearProfileData.logger("tokenId", this.tokenId);
    }

    public String getToken_PAN() {
        return ClearProfileData.logger("Token_PAN", this.Token_PAN);
    }

    public String getTrack_1_Data_for_MST() {
        return ClearProfileData.logger("Track_1_Data_for_MST", this.Track_1_Data_for_MST);
    }

    public String getTrack_1_Data_for_ZIPMode() {
        return ClearProfileData.logger("Track_1_Data_for_ZIPMode", this.Track_1_Data_for_ZIPMode);
    }

    public String getTrack_2_Data_for_MST() {
        return ClearProfileData.logger("Track_2_Data_for_MST", this.Track_2_Data_for_MST);
    }

    public String getTrack_2_Data_for_ZIPMode() {
        return ClearProfileData.logger("Track_2_Data_for_ZIPMode", this.Track_2_Data_for_ZIPMode);
    }

    public String getTrack_2_Equivalent_Data() {
        return ClearProfileData.logger("Track_2_Equivalent_Data", this.Track_2_Equivalent_Data);
    }

    public String getTrack_data_hash() {
        return ClearProfileData.logger("track_data_hash", this.track_data_hash);
    }

    public String getTransaction_log_format() {
        return ClearProfileData.logger("transaction_log_format", this.transaction_log_format);
    }

    public String getZip_AID_FCI() {
        return ClearProfileData.logger("Zip_AID_FCI", this.Zip_AID_FCI);
    }

    public static class TokenContraints {
        private String hardExpirationTimestamp;
        private int lowCredentialsThreshold;
        private String softExpirationTimestamp;

        public String getHardExpirationTimestamp() {
            return this.hardExpirationTimestamp;
        }

        public int getLowCredentialsThreshold() {
            return this.lowCredentialsThreshold;
        }

        public String getSoftExpirationTimestamp() {
            return this.softExpirationTimestamp;
        }
    }

}

