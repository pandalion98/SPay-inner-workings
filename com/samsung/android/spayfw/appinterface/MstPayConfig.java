package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class MstPayConfig implements Parcelable {
    public static final Creator<MstPayConfig> CREATOR;
    private List<MstPayConfigEntry> mstPayConfigEntryList;

    /* renamed from: com.samsung.android.spayfw.appinterface.MstPayConfig.1 */
    static class C03691 implements Creator<MstPayConfig> {
        C03691() {
        }

        public MstPayConfig createFromParcel(Parcel parcel) {
            return new MstPayConfig(null);
        }

        public MstPayConfig[] newArray(int i) {
            return new MstPayConfig[i];
        }
    }

    static {
        CREATOR = new C03691();
    }

    private MstPayConfig(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public MstPayConfig() {
        this.mstPayConfigEntryList = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public List<MstPayConfigEntry> getMstPayConfigEntry() {
        return this.mstPayConfigEntryList;
    }

    public void readFromParcel(Parcel parcel) {
        parcel.readList(this.mstPayConfigEntryList, getClass().getClassLoader());
    }

    public void setMstPayConfigEntry(List<MstPayConfigEntry> list) {
        this.mstPayConfigEntryList = list;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("MstPayConfig: : ");
        if (this.mstPayConfigEntryList != null) {
            for (int i = 0; i < this.mstPayConfigEntryList.size(); i++) {
                stringBuffer.append(((MstPayConfigEntry) this.mstPayConfigEntryList.get(i)).toString());
            }
        } else {
            stringBuffer.append(" mstPayConfigEntry: null ");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.mstPayConfigEntryList);
    }
}
