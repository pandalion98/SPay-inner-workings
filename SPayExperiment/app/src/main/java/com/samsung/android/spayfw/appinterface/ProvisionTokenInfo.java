/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProvisionTokenInfo
implements Parcelable {
    public static final Parcelable.Creator<ProvisionTokenInfo> CREATOR = new Parcelable.Creator<ProvisionTokenInfo>(){

        public ProvisionTokenInfo createFromParcel(Parcel parcel) {
            return new ProvisionTokenInfo(parcel);
        }

        public ProvisionTokenInfo[] newArray(int n2) {
            return new ProvisionTokenInfo[n2];
        }
    };
    private Map<String, String> activationParams;
    private Bundle activationParamsBundle;

    public ProvisionTokenInfo() {
    }

    private ProvisionTokenInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public Map<String, String> getActivationParams() {
        return this.activationParams;
    }

    public Bundle getActivationParamsBundle() {
        return this.activationParamsBundle;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void readFromParcel(Parcel parcel) {
        this.activationParamsBundle = parcel.readBundle();
        if (this.activationParamsBundle != null && !this.activationParamsBundle.isEmpty()) {
            this.activationParams = new HashMap();
            for (String string : this.activationParamsBundle.keySet()) {
                this.activationParams.put((Object)string, (Object)this.activationParamsBundle.getString(string));
            }
        }
    }

    public void setActivationParams(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        this.activationParams = map;
    }

    public void setActivationParamsBundle(Bundle bundle) {
        this.activationParamsBundle = bundle;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("ProvisionTokenInfo: activationParams: ");
        if (this.activationParams != null) {
            StringBuffer stringBuffer2 = new StringBuffer(" Values: ");
            for (Map.Entry entry : this.activationParams.entrySet()) {
                stringBuffer2.append((String)entry.getKey() + " : " + (String)entry.getValue());
            }
            stringBuffer.append(stringBuffer2.toString());
            do {
                return stringBuffer.toString();
                break;
            } while (true);
        }
        stringBuffer.append("null");
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int n2) {
        if (this.activationParams != null && !this.activationParams.isEmpty()) {
            if (this.activationParamsBundle == null) {
                this.activationParamsBundle = new Bundle();
            }
            for (Map.Entry entry : this.activationParams.entrySet()) {
                this.activationParamsBundle.putString((String)entry.getKey(), (String)entry.getValue());
            }
        }
        parcel.writeBundle(this.activationParamsBundle);
    }

}

