package com.samsung.android.hermes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.samsung.android.hermes.object.HermesObject;
import java.util.ArrayList;
import java.util.List;

public class KerykeionRequest implements Parcelable {
    public static final Creator<KerykeionRequest> CREATOR = new Creator<KerykeionRequest>() {
        public KerykeionRequest createFromParcel(Parcel in) {
            KerykeionRequest data = new KerykeionRequest();
            data.readFromParcel(in);
            return data;
        }

        public KerykeionRequest[] newArray(int size) {
            return new KerykeionRequest[size];
        }
    };
    private HermesObject mHermesObject;
    private List<Object> mPrimitive;
    private int nPatternType;
    private int nType;

    public KerykeionRequest() {
        this.mPrimitive = null;
        this.nPatternType = 0;
        this.mHermesObject = null;
        this.mPrimitive = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public void setRequestData(int type, List<Object> origin, int patternType) {
        this.nType = type;
        this.nPatternType = patternType;
        for (Object obj : origin) {
            if (((obj instanceof String) | (obj instanceof Uri)) != 0) {
                this.mPrimitive.add(obj);
            }
        }
    }

    public void setRequestData(int type, List<Object> origin, int patternType, HermesObject hObj) {
        this.nType = type;
        this.nPatternType = patternType;
        for (Object obj : origin) {
            if (((obj instanceof String) | (obj instanceof Uri)) != 0) {
                this.mPrimitive.add(obj);
            }
        }
        if (hObj != null) {
            this.mHermesObject = hObj;
        }
    }

    public int getType() {
        return this.nType;
    }

    public int getPatternType() {
        return this.nPatternType;
    }

    public List<Object> getSourceData() {
        return this.mPrimitive;
    }

    public HermesObject getHermesObject() {
        return this.mHermesObject;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.nType);
        out.writeList(this.mPrimitive);
        out.writeInt(this.nPatternType);
        out.writeParcelable(this.mHermesObject, flags);
    }

    public void readFromParcel(Parcel in) {
        this.nType = in.readInt();
        this.mPrimitive = in.readArrayList(Object.class.getClassLoader());
        this.nPatternType = in.readInt();
        this.mHermesObject = (HermesObject) in.readParcelable(HermesObject.class.getClassLoader());
    }
}
