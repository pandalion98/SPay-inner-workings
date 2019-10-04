/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;

public class EnrollCardReferenceInfo
extends EnrollCardInfo
implements Parcelable {
    public static final String CARD_INFO_CVV = "cardCvv";
    public static final String CARD_INFO_EXP_MM = "cardExpMM";
    public static final String CARD_INFO_EXP_YY = "cardExpYY";
    public static final String CARD_INFO_ZIP = "cardZip";
    public static final String CARD_REFERENCE_ID = "cardReferenceId";
    public static final String CARD_REF_TYPE_APP2APP = "app2app";
    public static final String CARD_REF_TYPE_COF = "cof";
    public static final String CARD_REF_TYPE_ID = "referenceId";
    public static final Parcelable.Creator<EnrollCardReferenceInfo> CREATOR = new Parcelable.Creator<EnrollCardReferenceInfo>(){

        public EnrollCardReferenceInfo createFromParcel(Parcel parcel) {
            return new EnrollCardReferenceInfo(parcel);
        }

        public EnrollCardReferenceInfo[] newArray(int n2) {
            return new EnrollCardReferenceInfo[n2];
        }
    };
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

    public EnrollCardReferenceInfo() {
        super(2);
    }

    public EnrollCardReferenceInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCardBrand() {
        return this.cardBrand;
    }

    public String getCardType() {
        return this.cardType;
    }

    public String getReferenceType() {
        return this.referenceType;
    }

    @Override
    public void readFromParcel(Parcel parcel) {
        Log.d((String)TAG, (String)"EnrollCardReferenceInfo: readFromParcel");
        super.readFromParcel(parcel);
        this.referenceType = parcel.readString();
        this.cardBrand = parcel.readString();
        this.cardType = parcel.readString();
        this.name = parcel.readString();
    }

    public void setCardBrand(String string) {
        this.cardBrand = string;
    }

    public void setCardReferenceType(String string) {
        this.referenceType = string;
    }

    public void setCardType(String string) {
        this.cardType = string;
    }

    @Override
    public String toString() {
        return "EnrollCardReferenceInfo{cardType='" + this.cardType + '\'' + ", cardBrand='" + this.cardBrand + '\'' + "} " + super.toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int n2) {
        Log.d((String)TAG, (String)"EnrollCardPanInfo: readFromParcel");
        super.writeToParcel(parcel, n2);
        parcel.writeString(this.referenceType);
        parcel.writeString(this.cardBrand);
        parcel.writeString(this.cardType);
        parcel.writeString(this.name);
    }

}

