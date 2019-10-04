/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.JsonWraper;

public class PoiTrigger
extends JsonWraper
implements Parcelable {
    public static final Parcelable.Creator<PoiTrigger> CREATOR = new Parcelable.Creator<PoiTrigger>(){

        public PoiTrigger createFromParcel(Parcel parcel) {
            return new PoiTrigger(parcel);
        }

        public PoiTrigger[] newArray(int n2) {
            return new PoiTrigger[n2];
        }
    };
    private String endTimeOfDay;
    private long gapBetweenTriggers;
    private long maxLifetimeTriggers;
    private int maxTriggersPerDay;
    private String purpose;
    private double radius;
    private String startTimeOfDay;
    private String status;

    public PoiTrigger(double d2, long l2, int n2, long l3, String string, String string2, String string3, String string4) {
        this.radius = d2;
        this.gapBetweenTriggers = l2;
        this.maxTriggersPerDay = n2;
        this.maxLifetimeTriggers = l3;
        this.startTimeOfDay = string;
        this.endTimeOfDay = string2;
        this.status = string3;
        this.purpose = string4;
    }

    protected PoiTrigger(Parcel parcel) {
        this.radius = parcel.readDouble();
        this.gapBetweenTriggers = parcel.readLong();
        this.maxTriggersPerDay = parcel.readInt();
        this.maxLifetimeTriggers = parcel.readLong();
        this.startTimeOfDay = parcel.readString();
        this.endTimeOfDay = parcel.readString();
        this.status = parcel.readString();
        this.purpose = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getEndTimeOfDay() {
        return this.endTimeOfDay;
    }

    public long getGapBetweenTriggers() {
        return this.gapBetweenTriggers;
    }

    public long getMaxLifetimeTriggers() {
        return this.maxLifetimeTriggers;
    }

    public int getMaxTriggersPerDay() {
        return this.maxTriggersPerDay;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public double getRadius() {
        return this.radius;
    }

    public String getStartTimeOfDay() {
        return this.startTimeOfDay;
    }

    public String getStatus() {
        return this.status;
    }

    public void setEndTimeOfDay(String string) {
        this.endTimeOfDay = string;
    }

    public void setGapBetweenTriggers(long l2) {
        this.gapBetweenTriggers = l2;
    }

    public void setMaxLifetimeTriggers(int n2) {
        this.maxLifetimeTriggers = n2;
    }

    public void setMaxTriggersPerDay(int n2) {
        this.maxTriggersPerDay = n2;
    }

    public void setPurpose(String string) {
        this.purpose = string;
    }

    public void setRadius(double d2) {
        this.radius = d2;
    }

    public void setStartTimeOfDay(String string) {
        this.startTimeOfDay = string;
    }

    public void setStatus(String string) {
        this.status = string;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeDouble(this.radius);
        parcel.writeLong(this.gapBetweenTriggers);
        parcel.writeInt(this.maxTriggersPerDay);
        parcel.writeLong(this.maxLifetimeTriggers);
        parcel.writeString(this.startTimeOfDay);
        parcel.writeString(this.endTimeOfDay);
        parcel.writeString(this.status);
        parcel.writeString(this.purpose);
    }

}

