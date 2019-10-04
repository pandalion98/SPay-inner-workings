/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import java.util.ArrayList;
import java.util.List;

public class MstPayConfig
implements Parcelable {
    public static final Parcelable.Creator<MstPayConfig> CREATOR = new Parcelable.Creator<MstPayConfig>(){

        public MstPayConfig createFromParcel(Parcel parcel) {
            return new MstPayConfig(parcel);
        }

        public MstPayConfig[] newArray(int n2) {
            return new MstPayConfig[n2];
        }
    };
    private List<MstPayConfigEntry> mstPayConfigEntryList = new ArrayList();

    public MstPayConfig() {
    }

    private MstPayConfig(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public List<MstPayConfigEntry> getMstPayConfigEntry() {
        return this.mstPayConfigEntryList;
    }

    public void readFromParcel(Parcel parcel) {
        parcel.readList(this.mstPayConfigEntryList, this.getClass().getClassLoader());
    }

    public void setMstPayConfigEntry(List<MstPayConfigEntry> list) {
        this.mstPayConfigEntryList = list;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("MstPayConfig: : ");
        if (this.mstPayConfigEntryList != null) {
            for (int i2 = 0; i2 < this.mstPayConfigEntryList.size(); ++i2) {
                stringBuffer.append(((MstPayConfigEntry)this.mstPayConfigEntryList.get(i2)).toString());
            }
        } else {
            stringBuffer.append(" mstPayConfigEntry: null ");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeList(this.mstPayConfigEntryList);
    }

}

