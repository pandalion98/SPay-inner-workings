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
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.TnC;
import java.util.ArrayList;
import java.util.List;

public class EnrollCardResult
implements Parcelable {
    public static final Parcelable.Creator<EnrollCardResult> CREATOR = new Parcelable.Creator<EnrollCardResult>(){

        public EnrollCardResult createFromParcel(Parcel parcel) {
            return new EnrollCardResult(parcel);
        }

        public EnrollCardResult[] newArray(int n2) {
            return new EnrollCardResult[n2];
        }
    };
    private String enrollmentId;
    private List<TnC> tnc = new ArrayList();

    public EnrollCardResult() {
    }

    private EnrollCardResult(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
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
        parcel.readList(this.tnc, this.getClass().getClassLoader());
    }

    public void setEnrollmentId(String string) {
        this.enrollmentId = string;
    }

    public void setTnC(List<TnC> list) {
        this.tnc = list;
    }

    public String toString() {
        String string = "EnrollmentResponse: enrollmentId: " + this.enrollmentId;
        if (this.tnc != null) {
            return string + this.tnc.toString();
        }
        return string + " tnc: null";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.enrollmentId);
        parcel.writeList(this.tnc);
    }

}

