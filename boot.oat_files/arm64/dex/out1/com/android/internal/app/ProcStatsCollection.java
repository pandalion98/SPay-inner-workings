package com.android.internal.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class ProcStatsCollection implements Parcelable {
    public static final Creator<ProcStatsCollection> CREATOR = new Creator<ProcStatsCollection>() {
        public ProcStatsCollection createFromParcel(Parcel in) {
            return new ProcStatsCollection(in);
        }

        public ProcStatsCollection[] newArray(int size) {
            return new ProcStatsCollection[size];
        }
    };
    public static final int LABEL_BG_TOTAL = 1;
    public static final int LABEL_TOTAL = 0;
    public String[] Label;
    public long[] avgPss;
    public long[] avgUss;
    public boolean hasExtra;
    private int installed_app;
    public long[] maxPss;
    public long[] maxUss;
    public long[] minPss;
    public long[] minUss;
    public double[] percentage;
    public String procName;

    public ProcStatsCollection() {
        this.hasExtra = false;
        this.procName = null;
        this.Label = new String[2];
        this.percentage = new double[2];
        this.minPss = new long[2];
        this.avgPss = new long[2];
        this.maxPss = new long[2];
        this.minUss = new long[2];
        this.avgUss = new long[2];
        this.maxUss = new long[2];
        this.installed_app = 0;
    }

    public ProcStatsCollection(Parcel parcel) {
        this.hasExtra = parcel.readInt() != 0;
        this.procName = parcel.readString();
        this.Label = parcel.createStringArray();
        this.percentage = parcel.createDoubleArray();
        this.minPss = parcel.createLongArray();
        this.avgPss = parcel.createLongArray();
        this.maxPss = parcel.createLongArray();
        this.minUss = parcel.createLongArray();
        this.avgUss = parcel.createLongArray();
        this.maxUss = parcel.createLongArray();
    }

    public void SetInstalledAppCount(int apps) {
        this.installed_app = apps;
    }

    public int GetInstalledAppCount() {
        return this.installed_app;
    }

    public String getProcName() {
        return this.procName;
    }

    public void setProcName(String _procName) {
        this.procName = _procName;
    }

    public String[] getLabel() {
        return this.Label;
    }

    public void setLabel(String[] _Label) {
        this.Label = (String[]) Arrays.copyOf(_Label, _Label.length);
    }

    public double[] getPercentage() {
        return this.percentage;
    }

    public void setPercentage(double[] _percentage) {
        this.percentage = Arrays.copyOf(_percentage, _percentage.length);
    }

    public long[] getMinPss() {
        return this.minPss;
    }

    public void setMinPss(long[] _minPss) {
        this.minPss = Arrays.copyOf(_minPss, _minPss.length);
    }

    public long[] getAvgPss() {
        return this.avgPss;
    }

    public void setAvgPss(long[] _avgPss) {
        this.avgPss = Arrays.copyOf(_avgPss, _avgPss.length);
    }

    public long[] getMaxPss() {
        return this.maxPss;
    }

    public void setMaxPss(long[] _maxPss) {
        this.maxPss = Arrays.copyOf(_maxPss, _maxPss.length);
    }

    public long[] getMinUss() {
        return this.minUss;
    }

    public void setMinUss(long[] _minUss) {
        this.minUss = Arrays.copyOf(_minUss, _minUss.length);
    }

    public long[] getAvgUss() {
        return this.avgUss;
    }

    public void setAvgUss(long[] _avgUss) {
        this.avgUss = Arrays.copyOf(_avgUss, _avgUss.length);
    }

    public long[] getMaxUss() {
        return this.maxUss;
    }

    public void setMaxUss(long[] _maxUss) {
        this.maxUss = Arrays.copyOf(_maxUss, _maxUss.length);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hasExtra ? 1 : 0);
        dest.writeString(this.procName);
        dest.writeStringArray(this.Label);
        dest.writeDoubleArray(this.percentage);
        dest.writeLongArray(this.minPss);
        dest.writeLongArray(this.avgPss);
        dest.writeLongArray(this.maxPss);
        dest.writeLongArray(this.minUss);
        dest.writeLongArray(this.avgUss);
        dest.writeLongArray(this.maxUss);
    }
}
