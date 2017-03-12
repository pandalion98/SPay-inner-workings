package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PoiTrigger extends JsonWraper implements Parcelable {
    public static final Creator<PoiTrigger> CREATOR;
    private String endTimeOfDay;
    private long gapBetweenTriggers;
    private long maxLifetimeTriggers;
    private int maxTriggersPerDay;
    private String purpose;
    private double radius;
    private String startTimeOfDay;
    private String status;

    /* renamed from: com.samsung.contextclient.data.PoiTrigger.1 */
    static class C06101 implements Creator<PoiTrigger> {
        C06101() {
        }

        public PoiTrigger createFromParcel(Parcel parcel) {
            return new PoiTrigger(parcel);
        }

        public PoiTrigger[] newArray(int i) {
            return new PoiTrigger[i];
        }
    }

    public PoiTrigger(double d, long j, int i, long j2, String str, String str2, String str3, String str4) {
        this.radius = d;
        this.gapBetweenTriggers = j;
        this.maxTriggersPerDay = i;
        this.maxLifetimeTriggers = j2;
        this.startTimeOfDay = str;
        this.endTimeOfDay = str2;
        this.status = str3;
        this.purpose = str4;
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

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.radius);
        parcel.writeLong(this.gapBetweenTriggers);
        parcel.writeInt(this.maxTriggersPerDay);
        parcel.writeLong(this.maxLifetimeTriggers);
        parcel.writeString(this.startTimeOfDay);
        parcel.writeString(this.endTimeOfDay);
        parcel.writeString(this.status);
        parcel.writeString(this.purpose);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06101();
    }

    public void setRadius(double d) {
        this.radius = d;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setGapBetweenTriggers(long j) {
        this.gapBetweenTriggers = j;
    }

    public long getGapBetweenTriggers() {
        return this.gapBetweenTriggers;
    }

    public void setMaxTriggersPerDay(int i) {
        this.maxTriggersPerDay = i;
    }

    public int getMaxTriggersPerDay() {
        return this.maxTriggersPerDay;
    }

    public void setMaxLifetimeTriggers(int i) {
        this.maxLifetimeTriggers = (long) i;
    }

    public long getMaxLifetimeTriggers() {
        return this.maxLifetimeTriggers;
    }

    public void setStartTimeOfDay(String str) {
        this.startTimeOfDay = str;
    }

    public String getStartTimeOfDay() {
        return this.startTimeOfDay;
    }

    public void setEndTimeOfDay(String str) {
        this.endTimeOfDay = str;
    }

    public String getEndTimeOfDay() {
        return this.endTimeOfDay;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getStatus() {
        return this.status;
    }

    public void setPurpose(String str) {
        this.purpose = str;
    }

    public String getPurpose() {
        return this.purpose;
    }
}
