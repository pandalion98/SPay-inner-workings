/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.EncryptedImage;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import java.util.ArrayList;
import java.util.List;

public class EnrollCardLoyaltyInfo
extends EnrollCardInfo
implements Parcelable {
    public static final Parcelable.Creator<EnrollCardLoyaltyInfo> CREATOR = new Parcelable.Creator<EnrollCardLoyaltyInfo>(){

        public EnrollCardLoyaltyInfo createFromParcel(Parcel parcel) {
            return new EnrollCardLoyaltyInfo(parcel);
        }

        public EnrollCardLoyaltyInfo[] newArray(int n2) {
            return new EnrollCardLoyaltyInfo[n2];
        }
    };
    private static final String TAG = "EnrollCardLoyaltyInfo";
    private List<EncryptedImage> encryptedImages = new ArrayList();
    private String loyaltyCardInfo = null;

    public EnrollCardLoyaltyInfo() {
        super(3);
    }

    public EnrollCardLoyaltyInfo(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<EncryptedImage> getEncryptedImages() {
        return this.encryptedImages;
    }

    public String getLoyaltyInfo() {
        return this.loyaltyCardInfo;
    }

    @Override
    public void readFromParcel(Parcel parcel) {
        Log.d((String)TAG, (String)"EnrollCardLoyaltyInfo: readFromParcel");
        super.readFromParcel(parcel);
        this.loyaltyCardInfo = parcel.readString();
        parcel.readList(this.encryptedImages, this.getClass().getClassLoader());
    }

    public void setEncryptedImages(List<EncryptedImage> list) {
        this.encryptedImages = list;
    }

    public void setLoyaltyInfo(String string) {
        this.loyaltyCardInfo = string;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("LoyaltyCardInfo: loyaltyCardInfo=").append(this.loyaltyCardInfo);
        if (this.encryptedImages != null) {
            for (int i2 = 0; i2 < this.encryptedImages.size(); ++i2) {
                EncryptedImage encryptedImage = (EncryptedImage)this.encryptedImages.get(i2);
                if (encryptedImage == null) continue;
                stringBuffer.append(encryptedImage.toString());
            }
        } else {
            stringBuffer.append(" encryptedImages: null");
        }
        return stringBuffer.toString() + super.toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int n2) {
        Log.d((String)TAG, (String)"EnrollCardLoyaltyInfo: writeToParcel");
        super.writeToParcel(parcel, n2);
        parcel.writeString(this.loyaltyCardInfo);
        parcel.writeList(this.encryptedImages);
    }

}

