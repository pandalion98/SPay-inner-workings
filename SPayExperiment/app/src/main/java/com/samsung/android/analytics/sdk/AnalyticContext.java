/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.samsung.android.analytics.sdk;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticContext
implements Parcelable {
    public static final Parcelable.Creator<AnalyticContext> CREATOR = new Parcelable.Creator<AnalyticContext>(){

        public AnalyticContext a(Parcel parcel) {
            return new AnalyticContext(parcel);
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return this.a(parcel);
        }

        public AnalyticContext[] e(int n2) {
            return new AnalyticContext[n2];
        }

        public /* synthetic */ Object[] newArray(int n2) {
            return this.e(n2);
        }
    };
    private String bE = null;
    private String bH = null;
    private String bI = null;
    private String bJ = null;
    private String bK = null;
    private String bL = null;
    private String bM = null;
    private String bN = null;
    private String bO = null;
    private String bP = null;
    private String bQ = null;
    private String bR = null;
    private String bS = null;
    private String mAppPackageName = null;

    public AnalyticContext() {
        this.bI = Build.MODEL;
        this.bJ = Build.VERSION.RELEASE;
        this.bK = "ANDROID";
        this.bO = "1.0.7";
        this.mAppPackageName = "com.samsung.android.spay";
    }

    public AnalyticContext(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.bH = parcel.readString();
        this.bS = parcel.readString();
        this.bI = parcel.readString();
        this.bJ = parcel.readString();
        this.bK = parcel.readString();
        this.bL = parcel.readString();
        this.bM = parcel.readString();
        this.bN = parcel.readString();
        this.bO = parcel.readString();
        this.mAppPackageName = parcel.readString();
        this.bP = parcel.readString();
        this.bE = parcel.readString();
        this.bQ = parcel.readString();
        this.bR = parcel.readString();
        Log.d((String)"AnalyticContext", (String)("parcelled Event:" + this.toString()));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public JSONObject C() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("id", (Object)this.bS);
            jSONObject2.put("model", (Object)this.bI);
            jSONObject2.put("specVersion", (Object)this.bO);
            jSONObject2.put("appPkgName", (Object)this.mAppPackageName);
            String string = this.bP == null ? "" : this.bP;
            jSONObject2.put("appVersion", (Object)string);
            jSONObject2.put("pfVersion", (Object)this.bE);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("build", (Object)this.bL);
            jSONObject3.put("name", (Object)this.bK);
            jSONObject3.put("version", (Object)this.bJ);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("mcc", (Object)this.bQ);
            jSONObject4.put("mnc", (Object)this.bR);
            JSONObject jSONObject5 = new JSONObject();
            jSONObject5.put("id", (Object)this.bN);
            JSONObject jSONObject6 = new JSONObject();
            jSONObject6.put("id", (Object)this.bM);
            jSONObject2.put("os", (Object)jSONObject3);
            jSONObject2.put("network", (Object)jSONObject4);
            if (jSONObject5.length() > 0) {
                jSONObject.put("user", (Object)jSONObject5);
            }
            if (jSONObject6.length() > 0) {
                jSONObject.put("wallet", (Object)jSONObject6);
            }
            if (jSONObject2.length() <= 0) return jSONObject;
            {
                jSONObject.put("device", (Object)jSONObject2);
                return jSONObject;
            }
        }
        catch (JSONException jSONException) {
            Log.e((String)"AnalyticContext", (String)"Could not create JSONObject.");
            jSONException.printStackTrace();
        }
        return jSONObject;
    }

    public String D() {
        return this.bH;
    }

    public String E() {
        return this.bI;
    }

    public String F() {
        return this.bJ;
    }

    public String G() {
        return this.bK;
    }

    public String H() {
        return this.bL;
    }

    public String I() {
        return this.bM;
    }

    public String J() {
        return this.bN;
    }

    public String K() {
        return this.bS;
    }

    public int describeContents() {
        return 0;
    }

    public void l(String string) {
        this.bE = string;
    }

    public void m(String string) {
        this.bS = string;
    }

    public void n(String string) {
        this.bL = string;
    }

    public String toString() {
        return "AnalyticContext{mSession='" + this.bH + '\'' + ", mDeviceId='" + this.bS + '\'' + ", mDeviceModel='" + this.bI + '\'' + ", mDeviceOsVersion='" + this.bJ + '\'' + ", mDeviceOsName='" + this.bK + '\'' + ", mDeviceOsBuild='" + this.bL + '\'' + ", mWalletId='" + this.bM + '\'' + ", mUserId='" + this.bN + '\'' + ", mMcc='" + this.bQ + '\'' + ", mMnc='" + this.bR + '\'' + '}';
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.bH);
        parcel.writeString(this.bS);
        parcel.writeString(this.bI);
        parcel.writeString(this.bJ);
        parcel.writeString(this.bK);
        parcel.writeString(this.bL);
        parcel.writeString(this.bM);
        parcel.writeString(this.bN);
        parcel.writeString(this.bO);
        parcel.writeString(this.mAppPackageName);
        parcel.writeString(this.bP);
        parcel.writeString(this.bE);
        parcel.writeString(this.bQ);
        parcel.writeString(this.bR);
    }

}

