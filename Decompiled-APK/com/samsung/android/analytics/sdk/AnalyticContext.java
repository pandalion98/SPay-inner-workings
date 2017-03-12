package com.samsung.android.analytics.sdk;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticContext implements Parcelable {
    public static final Creator<AnalyticContext> CREATOR;
    private String bE;
    private String bH;
    private String bI;
    private String bJ;
    private String bK;
    private String bL;
    private String bM;
    private String bN;
    private String bO;
    private String bP;
    private String bQ;
    private String bR;
    private String bS;
    private String mAppPackageName;

    /* renamed from: com.samsung.android.analytics.sdk.AnalyticContext.1 */
    static class C03231 implements Creator<AnalyticContext> {
        C03231() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return m149a(parcel);
        }

        public /* synthetic */ Object[] newArray(int i) {
            return m150e(i);
        }

        public AnalyticContext m149a(Parcel parcel) {
            return new AnalyticContext(parcel);
        }

        public AnalyticContext[] m150e(int i) {
            return new AnalyticContext[i];
        }
    }

    public AnalyticContext() {
        this.bH = null;
        this.bI = null;
        this.bJ = null;
        this.bK = null;
        this.bL = null;
        this.bM = null;
        this.bN = null;
        this.bO = null;
        this.mAppPackageName = null;
        this.bP = null;
        this.bQ = null;
        this.bR = null;
        this.bS = null;
        this.bE = null;
        this.bI = Build.MODEL;
        this.bJ = VERSION.RELEASE;
        this.bK = "ANDROID";
        this.bO = "1.0.7";
        this.mAppPackageName = "com.samsung.android.spay";
    }

    public void m160l(String str) {
        this.bE = str;
    }

    public void m161m(String str) {
        this.bS = str;
    }

    public void m162n(String str) {
        this.bL = str;
    }

    public AnalyticContext(Parcel parcel) {
        this.bH = null;
        this.bI = null;
        this.bJ = null;
        this.bK = null;
        this.bL = null;
        this.bM = null;
        this.bN = null;
        this.bO = null;
        this.mAppPackageName = null;
        this.bP = null;
        this.bQ = null;
        this.bR = null;
        this.bS = null;
        this.bE = null;
        readFromParcel(parcel);
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
        Log.d("AnalyticContext", "parcelled Event:" + toString());
    }

    public void writeToParcel(Parcel parcel, int i) {
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

    static {
        CREATOR = new C03231();
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "AnalyticContext{mSession='" + this.bH + '\'' + ", mDeviceId='" + this.bS + '\'' + ", mDeviceModel='" + this.bI + '\'' + ", mDeviceOsVersion='" + this.bJ + '\'' + ", mDeviceOsName='" + this.bK + '\'' + ", mDeviceOsBuild='" + this.bL + '\'' + ", mWalletId='" + this.bM + '\'' + ", mUserId='" + this.bN + '\'' + ", mMcc='" + this.bQ + '\'' + ", mMnc='" + this.bR + '\'' + '}';
    }

    public JSONObject m151C() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(PushMessage.JSON_KEY_ID, this.bS);
            jSONObject2.put("model", this.bI);
            jSONObject2.put("specVersion", this.bO);
            jSONObject2.put("appPkgName", this.mAppPackageName);
            jSONObject2.put("appVersion", this.bP == null ? BuildConfig.FLAVOR : this.bP);
            jSONObject2.put("pfVersion", this.bE);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("build", this.bL);
            jSONObject3.put("name", this.bK);
            jSONObject3.put("version", this.bJ);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("mcc", this.bQ);
            jSONObject4.put("mnc", this.bR);
            JSONObject jSONObject5 = new JSONObject();
            jSONObject5.put(PushMessage.JSON_KEY_ID, this.bN);
            JSONObject jSONObject6 = new JSONObject();
            jSONObject6.put(PushMessage.JSON_KEY_ID, this.bM);
            jSONObject2.put("os", jSONObject3);
            jSONObject2.put("network", jSONObject4);
            if (jSONObject5.length() > 0) {
                jSONObject.put("user", jSONObject5);
            }
            if (jSONObject6.length() > 0) {
                jSONObject.put(PushMessage.JSON_KEY_WALLET, jSONObject6);
            }
            if (jSONObject2.length() > 0) {
                jSONObject.put("device", jSONObject2);
            }
        } catch (JSONException e) {
            Log.e("AnalyticContext", "Could not create JSONObject.");
            e.printStackTrace();
        }
        return jSONObject;
    }

    public String m152D() {
        return this.bH;
    }

    public String m153E() {
        return this.bI;
    }

    public String m154F() {
        return this.bJ;
    }

    public String m155G() {
        return this.bK;
    }

    public String m156H() {
        return this.bL;
    }

    public String m157I() {
        return this.bM;
    }

    public String m158J() {
        return this.bN;
    }

    public String m159K() {
        return this.bS;
    }
}
