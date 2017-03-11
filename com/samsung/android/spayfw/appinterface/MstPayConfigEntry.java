package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class MstPayConfigEntry implements Parcelable {
    public static final Creator<MstPayConfigEntry> CREATOR;
    private int baudRate;
    private int delayBetweenTransmission;
    private List<MstPayConfigEntryItem> mstPayConfigEntryItemList;

    /* renamed from: com.samsung.android.spayfw.appinterface.MstPayConfigEntry.1 */
    static class C03701 implements Creator<MstPayConfigEntry> {
        C03701() {
        }

        public MstPayConfigEntry createFromParcel(Parcel parcel) {
            return new MstPayConfigEntry(null);
        }

        public MstPayConfigEntry[] newArray(int i) {
            return new MstPayConfigEntry[i];
        }
    }

    static {
        CREATOR = new C03701();
    }

    private MstPayConfigEntry(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public MstPayConfigEntry() {
        this.mstPayConfigEntryItemList = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public int getBaudRate() {
        return this.baudRate;
    }

    public int getDelayBetweenRepeat() {
        return this.delayBetweenTransmission;
    }

    public List<MstPayConfigEntryItem> getMstPayConfigEntry() {
        return this.mstPayConfigEntryItemList;
    }

    public void readFromParcel(Parcel parcel) {
        this.baudRate = parcel.readInt();
        this.delayBetweenTransmission = parcel.readInt();
        parcel.readList(this.mstPayConfigEntryItemList, getClass().getClassLoader());
    }

    public void setBaudRate(int i) {
        this.baudRate = i;
    }

    public void setDelayBetweenRepeat(int i) {
        this.delayBetweenTransmission = i;
    }

    public void setMstPayConfigEntry(List<MstPayConfigEntryItem> list) {
        this.mstPayConfigEntryItemList = list;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("MstPayConfigEntry:  baudRate: " + this.baudRate + " delayBetweenTransmission: " + this.delayBetweenTransmission);
        if (this.mstPayConfigEntryItemList != null) {
            for (int i = 0; i < this.mstPayConfigEntryItemList.size(); i++) {
                stringBuffer.append(((MstPayConfigEntryItem) this.mstPayConfigEntryItemList.get(i)).toString());
            }
        } else {
            stringBuffer.append(" mstPayConfigEntryItemList: null ");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.baudRate);
        parcel.writeInt(this.delayBetweenTransmission);
        parcel.writeList(this.mstPayConfigEntryItemList);
    }
}
