package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class EnrollCardLoyaltyInfo extends EnrollCardInfo implements Parcelable {
    public static final Creator<EnrollCardLoyaltyInfo> CREATOR;
    private static final String TAG = "EnrollCardLoyaltyInfo";
    private List<EncryptedImage> encryptedImages;
    private String loyaltyCardInfo;

    /* renamed from: com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo.1 */
    static class C03451 implements Creator<EnrollCardLoyaltyInfo> {
        C03451() {
        }

        public EnrollCardLoyaltyInfo createFromParcel(Parcel parcel) {
            return new EnrollCardLoyaltyInfo(parcel);
        }

        public EnrollCardLoyaltyInfo[] newArray(int i) {
            return new EnrollCardLoyaltyInfo[i];
        }
    }

    static {
        CREATOR = new C03451();
    }

    public EnrollCardLoyaltyInfo(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public EnrollCardLoyaltyInfo() {
        super(3);
        this.loyaltyCardInfo = null;
        this.encryptedImages = null;
        this.encryptedImages = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.d(TAG, "EnrollCardLoyaltyInfo: writeToParcel");
        super.writeToParcel(parcel, i);
        parcel.writeString(this.loyaltyCardInfo);
        parcel.writeList(this.encryptedImages);
    }

    public void readFromParcel(Parcel parcel) {
        Log.d(TAG, "EnrollCardLoyaltyInfo: readFromParcel");
        super.readFromParcel(parcel);
        this.loyaltyCardInfo = parcel.readString();
        parcel.readList(this.encryptedImages, getClass().getClassLoader());
    }

    public String toString() {
        StringBuffer append = new StringBuffer("LoyaltyCardInfo: loyaltyCardInfo=").append(this.loyaltyCardInfo);
        if (this.encryptedImages != null) {
            for (int i = 0; i < this.encryptedImages.size(); i++) {
                EncryptedImage encryptedImage = (EncryptedImage) this.encryptedImages.get(i);
                if (encryptedImage != null) {
                    append.append(encryptedImage.toString());
                }
            }
        } else {
            append.append(" encryptedImages: null");
        }
        return append.toString() + super.toString();
    }

    public String getLoyaltyInfo() {
        return this.loyaltyCardInfo;
    }

    public void setLoyaltyInfo(String str) {
        this.loyaltyCardInfo = str;
    }

    public List<EncryptedImage> getEncryptedImages() {
        return this.encryptedImages;
    }

    public void setEncryptedImages(List<EncryptedImage> list) {
        this.encryptedImages = list;
    }
}
