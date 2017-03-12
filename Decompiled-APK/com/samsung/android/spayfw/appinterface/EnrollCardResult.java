package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class EnrollCardResult implements Parcelable {
    public static final Creator<EnrollCardResult> CREATOR;
    private String enrollmentId;
    private List<TnC> tnc;

    /* renamed from: com.samsung.android.spayfw.appinterface.EnrollCardResult.1 */
    static class C03481 implements Creator<EnrollCardResult> {
        C03481() {
        }

        public EnrollCardResult createFromParcel(Parcel parcel) {
            return new EnrollCardResult(null);
        }

        public EnrollCardResult[] newArray(int i) {
            return new EnrollCardResult[i];
        }
    }

    static {
        CREATOR = new C03481();
    }

    private EnrollCardResult(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public EnrollCardResult() {
        this.tnc = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public List<TnC> getTnC() {
        return this.tnc;
    }

    public void readFromParcel(Parcel parcel) {
        this.enrollmentId = parcel.readString();
        parcel.readList(this.tnc, getClass().getClassLoader());
    }

    public void setEnrollmentId(String str) {
        this.enrollmentId = str;
    }

    public void setTnC(List<TnC> list) {
        this.tnc = list;
    }

    public String toString() {
        String str = "EnrollmentResponse: enrollmentId: " + this.enrollmentId;
        if (this.tnc != null) {
            return str + this.tnc.toString();
        }
        return str + " tnc: null";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.enrollmentId);
        parcel.writeList(this.tnc);
    }
}
