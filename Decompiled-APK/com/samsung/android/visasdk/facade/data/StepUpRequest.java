package com.samsung.android.visasdk.facade.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class StepUpRequest {
    public static final Creator CREATOR;
    private String identifier;
    private String method;
    private String requestPayload;
    private String source;
    private String value;

    /* renamed from: com.samsung.android.visasdk.facade.data.StepUpRequest.1 */
    static class C06001 implements Creator {
        C06001() {
        }

        public StepUpRequest createFromParcel(Parcel parcel) {
            return new StepUpRequest(parcel);
        }

        public StepUpRequest[] newArray(int i) {
            return new StepUpRequest[i];
        }
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getRequestPayload() {
        return this.requestPayload;
    }

    public void setRequestPayload(String str) {
        this.requestPayload = str;
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06001();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.method);
        parcel.writeString(this.value);
        parcel.writeString(this.identifier);
        parcel.writeString(this.source);
    }

    public StepUpRequest(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.method = parcel.readString();
        this.value = parcel.readString();
        this.identifier = parcel.readString();
        this.source = parcel.readString();
    }
}
