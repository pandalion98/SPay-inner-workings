package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;

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
    @SerializedName("Track_1_Data_for_ZIP/MS_Mode")
    private String Track_1_Data_for_ZIPMode;
    private String Track_2_Data_for_MST;
    @SerializedName("Track_2_Data_for_ZIP/MS_Mode")
    private String Track_2_Data_for_ZIPMode;
    private String Track_2_Equivalent_Data;
    private String Zip_AID_FCI;
    private TokenContraints constraints;
    private TransactionProfilesContainer profiles;
    private String tokenId;
    private String track_data_hash;
    private String transaction_log_format;

    public static class TokenContraints {
        private String hardExpirationTimestamp;
        private int lowCredentialsThreshold;
        private String softExpirationTimestamp;

        public String getSoftExpirationTimestamp() {
            return this.softExpirationTimestamp;
        }

        public String getHardExpirationTimestamp() {
            return this.hardExpirationTimestamp;
        }

        public int getLowCredentialsThreshold() {
            return this.lowCredentialsThreshold;
        }
    }

    public String getPPSE_FCI() {
        return logger("PPSE_FCI", this.PPSE_FCI);
    }

    public String getDPAS_AID_FCI() {
        return logger("DPAS_AID_FCI", this.DPAS_AID_FCI);
    }

    public String getZip_AID_FCI() {
        return logger("Zip_AID_FCI", this.Zip_AID_FCI);
    }

    public String getCommon_debit_FCI() {
        return logger("Common_debit_FCI", this.Common_debit_FCI);
    }

    public String getPAN_Seq_NBR() {
        return logger("PAN_Seq_NBR", this.PAN_Seq_NBR);
    }

    public String getToken_PAN() {
        return logger("Token_PAN", this.Token_PAN);
    }

    public String getEffective_Date() {
        return logger("Effective_Date", this.Effective_Date);
    }

    public String getExp_Date() {
        return logger("Exp_Date", this.Exp_Date);
    }

    public String getApplication_State() {
        return logger("Application_State", this.Application_State);
    }

    public String getAVN() {
        return logger("AVN", this.AVN);
    }

    public String getCACO() {
        return logger("CACO", this.CACO);
    }

    public String getCardholder_Name() {
        return logger("Cardholder_Name", this.Cardholder_Name);
    }

    public String getCL_ACO() {
        return logger("CL_ACO", this.CL_ACO);
    }

    public String getCRM_Country_Code() {
        return logger("CRM_Country_Code", this.CRM_Country_Code);
    }

    public String getCRM_Currency_Code() {
        return logger("CRM_Currency_Code", this.CRM_Currency_Code);
    }

    public String getIssuer_Application_Data() {
        return logger("Issuer_Application_Data", this.Issuer_Application_Data);
    }

    public String getIssuer_Country_Code() {
        return logger("Issuer_Country_Code", this.Issuer_Country_Code);
    }

    public String getIssuer_Life_Cycle_Data() {
        return logger("Issuer_Life_Cycle_Data", this.Issuer_Life_Cycle_Data);
    }

    public String getPASSCODE_Retry_Counter() {
        return logger("PASSCODE_Retry_Counter", this.PASSCODE_Retry_Counter);
    }

    public String getSecondary_Currency_1() {
        return logger("Secondary_Currency_1", this.Secondary_Currency_1);
    }

    public String getSecondary_Currency_2() {
        return logger("Secondary_Currency_2", this.Secondary_Currency_2);
    }

    public String getService_Code() {
        return logger("Service_Code", this.Service_Code);
    }

    public String getTrack_1_Data_for_ZIPMode() {
        return logger("Track_1_Data_for_ZIPMode", this.Track_1_Data_for_ZIPMode);
    }

    public String getTrack_2_Data_for_ZIPMode() {
        return logger("Track_2_Data_for_ZIPMode", this.Track_2_Data_for_ZIPMode);
    }

    public String getTrack_2_Equivalent_Data() {
        return logger("Track_2_Equivalent_Data", this.Track_2_Equivalent_Data);
    }

    public String getATC_Limit() {
        return logger("ATC_Limit", this.ATC_Limit);
    }

    public String getPIN_Cryptographic_limit() {
        return logger("PIN_Cryptographic_limit", this.PIN_Cryptographic_limit);
    }

    public String getFailed_MAC_limit() {
        return logger("Failed_MAC_limit", this.Failed_MAC_limit);
    }

    public String getLifetime_MAC_limit() {
        return logger("Lifetime_MAC_limit", this.Lifetime_MAC_limit);
    }

    public String getSession_MAC_limit() {
        return logger("Session_MAC_limit", this.Session_MAC_limit);
    }

    public String getPASSCODE_Bypass() {
        return logger("PASSCODE_Bypass", this.PASSCODE_Bypass);
    }

    public String getPASSCODE_Bypass_Limit() {
        return logger("PASSCODE_Bypass_Limit", this.PASSCODE_Bypass_Limit);
    }

    public String getPasscode_reference_data() {
        return logger("Passcode_reference_data", this.Passcode_reference_data);
    }

    public String getPASSCODE_Retry_Limit() {
        return logger("PASSCODE_Retry_Limit", this.PASSCODE_Retry_Limit);
    }

    public String getCAVV_Key_Indicator() {
        return logger("CAVV_Key_Indicator", this.CAVV_Key_Indicator);
    }

    public String getALT_AID1_FCI() {
        return logger("ALT_AID1_FCI", this.ALT_AID1_FCI);
    }

    public String getALT_AID2_FCI() {
        return logger("ALT_AID2_FCI", this.ALT_AID2_FCI);
    }

    public String getTrack_1_Data_for_MST() {
        return logger("Track_1_Data_for_MST", this.Track_1_Data_for_MST);
    }

    public String getTrack_2_Data_for_MST() {
        return logger("Track_2_Data_for_MST", this.Track_2_Data_for_MST);
    }

    public String getTrack_data_hash() {
        return logger("track_data_hash", this.track_data_hash);
    }

    public String getIDDT0() {
        return logger("IDDT0", this.IDDT0);
    }

    public String getIDDT1() {
        return logger("IDDT1", this.IDDT1);
    }

    public String getIDDT2() {
        return logger("IDDT2", this.IDDT2);
    }

    public String getIADOL() {
        return logger("IADOL", this.IADOL);
    }

    public String getTransaction_log_format() {
        return logger("transaction_log_format", this.transaction_log_format);
    }

    public String getPDOL_Profile_check() {
        return logger("PDOL_Profile_check", this.PDOL_Profile_check);
    }

    public String getTokenId() {
        return logger(PaymentFramework.EXTRA_TOKEN_ID, this.tokenId);
    }

    public TransactionProfilesContainer getProfiles() {
        return this.profiles;
    }

    public TokenContraints getConstraints() {
        return this.constraints;
    }

    private byte[] toBytes(String str) {
        if (str == null) {
            return null;
        }
        Log.m285d(TAG, "toBytes: " + str);
        return str.getBytes();
    }

    public static String logger(String str, String str2) {
        if (str2 == null) {
            String str3 = "null";
        }
        return str2;
    }
}
