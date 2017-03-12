package com.samsung.android.hermes.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class HermesStrokes extends HermesObject implements Parcelable {
    public static final Creator<HermesStrokes> CREATOR = new Creator<HermesStrokes>() {
        public HermesStrokes createFromParcel(Parcel in) {
            HermesStrokes data = new HermesStrokes();
            data.readFromParcel(in);
            return data;
        }

        public HermesStrokes[] newArray(int size) {
            return new HermesStrokes[size];
        }
    };
    private List<HermesStroke> mStrokes;

    public HermesStrokes() {
        this.mStrokes = null;
        this.mStrokes = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public void setStrokes(HermesStroke[] stroke) {
        for (HermesStroke s : stroke) {
            this.mStrokes.add(s);
        }
    }

    public void addStroke(HermesStroke stroke) {
        if (stroke != null) {
            this.mStrokes.add(stroke);
        }
    }

    public List<HermesStroke> getStrokes() {
        return this.mStrokes;
    }

    public int size() {
        return this.mStrokes.size();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this.mStrokes);
    }

    public void readFromParcel(Parcel in) {
        if (this.mStrokes == null) {
            this.mStrokes = new ArrayList();
        }
        in.readTypedList(this.mStrokes, HermesStroke.CREATOR);
    }
}
