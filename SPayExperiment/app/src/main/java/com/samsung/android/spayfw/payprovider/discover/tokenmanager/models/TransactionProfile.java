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
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearProfileData;

public class TransactionProfile {
    private String AFL;
    private String AIP;
    private String AUC;
    @SerializedName(value="CL-Accumulator")
    private String CL_Accumulator;
    @SerializedName(value="CL-Cons_limit")
    private String CL_Cons_limit;
    @SerializedName(value="CL-Counter")
    private String CL_Counter;
    @SerializedName(value="CL-Cum_limit")
    private String CL_Cum_limit;
    @SerializedName(value="CL-STA_limit")
    private String CL_STA_limit;
    private String COA;
    private String CPR;
    @SerializedName(value="CRM-CAC_Default")
    private String CRM_CAC_Default;
    @SerializedName(value="CRM-CAC_Denial")
    private String CRM_CAC_Denial;
    @SerializedName(value="CRM-CAC_Online")
    private String CRM_CAC_Online;
    @SerializedName(value="CRM-CAC_Switch_Interface")
    private String CRM_CAC_Switch_Interface;
    @SerializedName(value="CVM-Accumulator")
    private String CVM_Accumulator;
    @SerializedName(value="CVM-CAC_Online-PIN")
    private String CVM_CAC_Online_PIN;
    @SerializedName(value="CVM-CAC_Signature")
    private String CVM_CAC_Signature;
    @SerializedName(value="CVM-Cons_limit_1")
    private String CVM_Cons_limit_1;
    @SerializedName(value="CVM-Cons_limit_2")
    private String CVM_Cons_limit_2;
    @SerializedName(value="CVM-Counter")
    private String CVM_Counter;
    @SerializedName(value="CVM-Cum_limit_1")
    private String CVM_Cum_limit_1;
    @SerializedName(value="CVM-Cum_limit_2")
    private String CVM_Cum_limit_2;
    @SerializedName(value="CVM-Sta_limit_1")
    private String CVM_Sta_limit_1;
    @SerializedName(value="CVM-Sta_limit_2")
    private String CVM_Sta_limit_2;
    private String LCOA;
    private String LCOL;
    private String NCOT;
    private String PRU;
    private String STA;
    private String UCOA;
    private String UCOL;

    public String getAFL() {
        return ClearProfileData.logger("AFL is", this.AFL);
    }

    public String getAIP() {
        return this.AIP;
    }

    public String getAUC() {
        return ClearProfileData.logger("AUC", this.AUC);
    }

    public String getCL_Accumulator() {
        return ClearProfileData.logger("CL_Accumulator", this.CL_Accumulator);
    }

    public String getCL_Cons_limit() {
        return ClearProfileData.logger("CL_Cons_limit", this.CL_Cons_limit);
    }

    public String getCL_Counter() {
        return ClearProfileData.logger("CL_Counter", this.CL_Counter);
    }

    public String getCL_Cum_limit() {
        return ClearProfileData.logger("CL_Cum_limit", this.CL_Cum_limit);
    }

    public String getCL_STA_limit() {
        return ClearProfileData.logger("CL_STA_limit", this.CL_STA_limit);
    }

    public String getCOA() {
        return ClearProfileData.logger("COA", this.COA);
    }

    public String getCPR() {
        return ClearProfileData.logger("CPR is", this.CPR);
    }

    public String getCRM_CAC_Default() {
        return ClearProfileData.logger("CRM_CAC_Default", this.CRM_CAC_Default);
    }

    public String getCRM_CAC_Denial() {
        return ClearProfileData.logger("CRM_CAC_Denial", this.CRM_CAC_Denial);
    }

    public String getCRM_CAC_Online() {
        return ClearProfileData.logger("CRM_CAC_Online", this.CRM_CAC_Online);
    }

    public String getCRM_CAC_Switch_Interface() {
        return ClearProfileData.logger("CRM_CAC_Switch_Interface", this.CRM_CAC_Switch_Interface);
    }

    public String getCVM_Accumulator() {
        return ClearProfileData.logger("CVM_Accumulator", this.CVM_Accumulator);
    }

    public String getCVM_CAC_Online_PIN() {
        return ClearProfileData.logger("CVM_CAC_Online_PIN", this.CVM_CAC_Online_PIN);
    }

    public String getCVM_CAC_Signature() {
        return ClearProfileData.logger("CVM_CAC_Signature", this.CVM_CAC_Signature);
    }

    public String getCVM_Cons_limit_1() {
        return ClearProfileData.logger("CVM_Cons_limit_1", this.CVM_Cons_limit_1);
    }

    public String getCVM_Cons_limit_2() {
        return ClearProfileData.logger("CVM_Cons_limit_2", this.CVM_Cons_limit_2);
    }

    public String getCVM_Counter() {
        return ClearProfileData.logger("CVM_Counter", this.CVM_Counter);
    }

    public String getCVM_Cum_limit_1() {
        return ClearProfileData.logger("CVM_Cum_limit_1", this.CVM_Cum_limit_1);
    }

    public String getCVM_Cum_limit_2() {
        return ClearProfileData.logger("CVM_Cum_limit_2", this.CVM_Cum_limit_2);
    }

    public String getCVM_Sta_limit_1() {
        return ClearProfileData.logger("CVM_Sta_limit_1", this.CVM_Sta_limit_1);
    }

    public String getCVM_Sta_limit_2() {
        return ClearProfileData.logger("CVM_Sta_limit_2", this.CVM_Sta_limit_2);
    }

    public String getLCOA() {
        return ClearProfileData.logger("LCOA", this.LCOA);
    }

    public String getLCOL() {
        return ClearProfileData.logger("LCOL", this.LCOL);
    }

    public String getNCOT() {
        return ClearProfileData.logger("NCOT", this.NCOT);
    }

    public String getPRU() {
        return ClearProfileData.logger("PRU", this.PRU);
    }

    public String getSTA() {
        return ClearProfileData.logger("STA", this.STA);
    }

    public String getUCOA() {
        return ClearProfileData.logger("UCOA", this.UCOA);
    }

    public String getUCOL() {
        return ClearProfileData.logger("UCOL", this.UCOL);
    }
}

