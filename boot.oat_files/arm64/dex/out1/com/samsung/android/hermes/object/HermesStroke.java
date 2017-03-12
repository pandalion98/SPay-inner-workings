package com.samsung.android.hermes.object;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class HermesStroke implements Parcelable {
    public static final Creator<HermesStroke> CREATOR = new Creator<HermesStroke>() {
        public HermesStroke createFromParcel(Parcel in) {
            HermesStroke data = new HermesStroke();
            data.readFromParcel(in);
            return data;
        }

        public HermesStroke[] newArray(int size) {
            return new HermesStroke[size];
        }
    };
    private List<PointF> mStroke;

    public HermesStroke() {
        this.mStroke = null;
        this.mStroke = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public void setPoints(PointF[] points) {
        for (PointF p : points) {
            this.mStroke.add(p);
        }
    }

    public List<PointF> getPoints() {
        return this.mStroke;
    }

    public int size() {
        return this.mStroke.size();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this.mStroke);
    }

    public void readFromParcel(Parcel in) {
        if (this.mStroke == null) {
            this.mStroke = new ArrayList();
        }
        in.readTypedList(this.mStroke, PointF.CREATOR);
    }
}
