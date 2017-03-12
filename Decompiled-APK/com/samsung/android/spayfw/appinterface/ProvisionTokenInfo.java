package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProvisionTokenInfo implements Parcelable {
    public static final Creator<ProvisionTokenInfo> CREATOR;
    private Map<String, String> activationParams;
    private Bundle activationParamsBundle;

    /* renamed from: com.samsung.android.spayfw.appinterface.ProvisionTokenInfo.1 */
    static class C03771 implements Creator<ProvisionTokenInfo> {
        C03771() {
        }

        public ProvisionTokenInfo createFromParcel(Parcel parcel) {
            return new ProvisionTokenInfo(null);
        }

        public ProvisionTokenInfo[] newArray(int i) {
            return new ProvisionTokenInfo[i];
        }
    }

    static {
        CREATOR = new C03771();
    }

    private ProvisionTokenInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public Map<String, String> getActivationParams() {
        return this.activationParams;
    }

    public void readFromParcel(Parcel parcel) {
        this.activationParamsBundle = parcel.readBundle();
        if (this.activationParamsBundle != null && !this.activationParamsBundle.isEmpty()) {
            this.activationParams = new HashMap();
            for (String str : this.activationParamsBundle.keySet()) {
                this.activationParams.put(str, this.activationParamsBundle.getString(str));
            }
        }
    }

    public void setActivationParams(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            this.activationParams = map;
        }
    }

    public Bundle getActivationParamsBundle() {
        return this.activationParamsBundle;
    }

    public void setActivationParamsBundle(Bundle bundle) {
        this.activationParamsBundle = bundle;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("ProvisionTokenInfo: activationParams: ");
        if (this.activationParams != null) {
            StringBuffer stringBuffer2 = new StringBuffer(" Values: ");
            for (Entry entry : this.activationParams.entrySet()) {
                stringBuffer2.append(((String) entry.getKey()) + " : " + ((String) entry.getValue()));
            }
            stringBuffer.append(stringBuffer2.toString());
        } else {
            stringBuffer.append("null");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (!(this.activationParams == null || this.activationParams.isEmpty())) {
            if (this.activationParamsBundle == null) {
                this.activationParamsBundle = new Bundle();
            }
            for (Entry entry : this.activationParams.entrySet()) {
                this.activationParamsBundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
        }
        parcel.writeBundle(this.activationParamsBundle);
    }
}
