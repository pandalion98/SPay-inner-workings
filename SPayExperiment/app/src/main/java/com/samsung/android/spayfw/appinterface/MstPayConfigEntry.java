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
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import java.util.ArrayList;
import java.util.List;

public class MstPayConfigEntry
implements Parcelable {
    public static final Parcelable.Creator<MstPayConfigEntry> CREATOR = new Parcelable.Creator<MstPayConfigEntry>(){

        public MstPayConfigEntry createFromParcel(Parcel parcel) {
            return new MstPayConfigEntry(parcel);
        }

        public MstPayConfigEntry[] newArray(int n2) {
            return new MstPayConfigEntry[n2];
        }
    };
    private int baudRate;
    private int delayBetweenTransmission;
    private List<MstPayConfigEntryItem> mstPayConfigEntryItemList = new ArrayList();

    public MstPayConfigEntry() {
    }

    private MstPayConfigEntry(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
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
        parcel.readList(this.mstPayConfigEntryItemList, this.getClass().getClassLoader());
    }

    public void setBaudRate(int n2) {
        this.baudRate = n2;
    }

    public void setDelayBetweenRepeat(int n2) {
        this.delayBetweenTransmission = n2;
    }

    public void setMstPayConfigEntry(List<MstPayConfigEntryItem> list) {
        this.mstPayConfigEntryItemList = list;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("MstPayConfigEntry:  baudRate: " + this.baudRate + " delayBetweenTransmission: " + this.delayBetweenTransmission);
        if (this.mstPayConfigEntryItemList != null) {
            for (int i2 = 0; i2 < this.mstPayConfigEntryItemList.size(); ++i2) {
                stringBuffer.append(((MstPayConfigEntryItem)this.mstPayConfigEntryItemList.get(i2)).toString());
            }
        } else {
            stringBuffer.append(" mstPayConfigEntryItemList: null ");
        }
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.baudRate);
        parcel.writeInt(this.delayBetweenTransmission);
        parcel.writeList(this.mstPayConfigEntryItemList);
    }

}

