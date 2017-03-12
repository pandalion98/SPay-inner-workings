package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class EnrollCardReferenceInfo extends EnrollCardInfo implements Parcelable {
    public static final String CARD_INFO_CVV = "cardCvv";
    public static final String CARD_INFO_EXP_MM = "cardExpMM";
    public static final String CARD_INFO_EXP_YY = "cardExpYY";
    public static final String CARD_INFO_ZIP = "cardZip";
    public static final String CARD_REFERENCE_ID = "cardReferenceId";
    public static final String CARD_REF_TYPE_APP2APP = "app2app";
    public static final String CARD_REF_TYPE_COF = "cof";
    public static final String CARD_REF_TYPE_ID = "referenceId";
    public static final Creator<EnrollCardReferenceInfo> CREATOR;
    public static final String ENROLL_FEATURE_APP2APP_VALUE = "APP2APP";
    public static final String ENROLL_FEATURE_CARD_CAPTURE_VALUE = "CARD_CAPTURE";
    public static final String ENROLL_FEATURE_DEFAULT_VALUE = "DEFAULT";
    public static final String ENROLL_FEATURE_KEY = "enrollCardFeature";
    public static final String ENROLL_FEATURE_SAMSUNG_REWARDS_VALUE = "SAMSUNG_REWARDS_CARD";
    public static final String ENROLL_INITIATOR_ID_KEY = "enrollCardInitiatorId";
    public static final String ENROLL_PAYLOAD = "enrollPayload";
    private static final String TAG = "EnrollCardReferenceInfo";
    private String cardBrand;
    private String cardType;
    private String referenceType;

    /* renamed from: com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo.1 */
    static class C03471 implements Creator<EnrollCardReferenceInfo> {
        C03471() {
        }

        public EnrollCardReferenceInfo createFromParcel(Parcel parcel) {
            return new EnrollCardReferenceInfo(parcel);
        }

        public EnrollCardReferenceInfo[] newArray(int i) {
            return new EnrollCardReferenceInfo[i];
        }
    }

    static {
        CREATOR = new C03471();
    }

    public EnrollCardReferenceInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public EnrollCardReferenceInfo() {
        super(2);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.d(TAG, "EnrollCardPanInfo: readFromParcel");
        super.writeToParcel(parcel, i);
        parcel.writeString(this.referenceType);
        parcel.writeString(this.cardBrand);
        parcel.writeString(this.cardType);
        parcel.writeString(this.name);
    }

    public void readFromParcel(Parcel parcel) {
        Log.d(TAG, "EnrollCardReferenceInfo: readFromParcel");
        super.readFromParcel(parcel);
        this.referenceType = parcel.readString();
        this.cardBrand = parcel.readString();
        this.cardType = parcel.readString();
        this.name = parcel.readString();
    }

    public String toString() {
        return "EnrollCardReferenceInfo{cardType='" + this.cardType + '\'' + ", cardBrand='" + this.cardBrand + '\'' + "} " + super.toString();
    }

    public String getReferenceType() {
        return this.referenceType;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setCardType(String str) {
        this.cardType = str;
    }

    public String getCardBrand() {
        return this.cardBrand;
    }

    public void setCardBrand(String str) {
        this.cardBrand = str;
    }

    public void setCardReferenceType(String str) {
        this.referenceType = str;
    }
}
