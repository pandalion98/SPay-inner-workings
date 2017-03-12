package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

class SContextEventContext implements Parcelable {
    public static final Creator<SContextEventContext> CREATOR = new Creator<SContextEventContext>() {
        public SContextEventContext createFromParcel(Parcel in) {
            return new SContextEventContext(in);
        }

        public SContextEventContext[] newArray(int size) {
            return new SContextEventContext[size];
        }
    };

    public SContextEventContext(Parcel src) {
    }

    void setValues(Bundle context) {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
