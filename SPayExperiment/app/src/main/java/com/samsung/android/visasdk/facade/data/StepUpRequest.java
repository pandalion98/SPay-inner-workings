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
package com.samsung.android.visasdk.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

public class StepUpRequest {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public StepUpRequest createFromParcel(Parcel parcel) {
            return new StepUpRequest(parcel);
        }

        public StepUpRequest[] newArray(int n2) {
            return new StepUpRequest[n2];
        }
    };
    private String identifier;
    private String method;
    private String requestPayload;
    private String source;
    private String value;

    public StepUpRequest(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getMethod() {
        return this.method;
    }

    public String getRequestPayload() {
        return this.requestPayload;
    }

    public String getSource() {
        return this.source;
    }

    public String getValue() {
        return this.value;
    }

    public void readFromParcel(Parcel parcel) {
        this.method = parcel.readString();
        this.value = parcel.readString();
        this.identifier = parcel.readString();
        this.source = parcel.readString();
    }

    public void setIdentifier(String string) {
        this.identifier = string;
    }

    public void setMethod(String string) {
        this.method = string;
    }

    public void setRequestPayload(String string) {
        this.requestPayload = string;
    }

    public void setSource(String string) {
        this.source = string;
    }

    public void setValue(String string) {
        this.value = string;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.method);
        parcel.writeString(this.value);
        parcel.writeString(this.identifier);
        parcel.writeString(this.source);
    }

}

