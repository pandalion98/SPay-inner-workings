package com.android.internal.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MemDumpInfo implements Parcelable {
    public static final Creator<MemDumpInfo> CREATOR = new Creator<MemDumpInfo>() {
        public MemDumpInfo createFromParcel(Parcel in) {
            return new MemDumpInfo(in);
        }

        public MemDumpInfo[] newArray(int size) {
            return new MemDumpInfo[size];
        }
    };
    private long free_size;
    public boolean hasExtra;
    public String label;
    private long lost_size;
    public String procName;
    public long pss;
    public long swap_out;
    private long total_size;
    private long used_size;

    public MemDumpInfo() {
        this.hasExtra = false;
        this.label = "";
        this.procName = "";
        this.pss = 0;
        this.swap_out = 0;
    }

    public MemDumpInfo(Parcel parcel) {
        this.hasExtra = parcel.readInt() != 0;
        this.label = parcel.readString();
        this.procName = parcel.readString();
        this.pss = parcel.readLong();
        this.swap_out = parcel.readLong();
    }

    public void SetTotalSize(long total) {
        this.total_size = total;
    }

    public void SetFreeSize(long free) {
        this.free_size = free;
    }

    public void SetUsedSize(long Used) {
        this.used_size = Used;
    }

    public void SetLostSize(long Lost) {
        this.lost_size = Lost;
    }

    public long GetTotalSize() {
        return this.total_size;
    }

    public long GetFreeSize() {
        return this.free_size;
    }

    public long GetUsedSize() {
        return this.used_size;
    }

    public long GetLostSize() {
        return this.lost_size;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hasExtra ? 1 : 0);
        dest.writeString(this.label);
        dest.writeString(this.procName);
        dest.writeLong(this.pss);
        dest.writeLong(this.swap_out);
    }
}
