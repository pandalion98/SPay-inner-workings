/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.IllegalAccessException
 *  java.lang.IllegalArgumentException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnrollCardInfo
implements Parcelable {
    private static final String[] CARD_ENTRY_MODES;
    public static final String CARD_ENTRY_MODE_APP = "APP";
    public static final String CARD_ENTRY_MODE_FILE = "FILE";
    public static final String CARD_ENTRY_MODE_MANUAL = "MAN";
    public static final String CARD_ENTRY_MODE_OCR = "OCR";
    private static final String[] CARD_PRESENTATION_MODES;
    public static final String CARD_PRESENTATION_MODE_ALL = "ALL";
    public static final String CARD_PRESENTATION_MODE_APP = "APP";
    public static final String CARD_PRESENTATION_MODE_ECM = "ECM";
    public static final String CARD_PRESENTATION_MODE_MST = "MST";
    public static final String CARD_PRESENTATION_MODE_NFC = "NFC";
    public static final Parcelable.Creator<EnrollCardInfo> CREATOR;
    protected static final int ENROLL_TYPE_LOYALTY = 3;
    protected static final int ENROLL_TYPE_PAN = 1;
    protected static final int ENROLL_TYPE_PAN_REFERENCE = 2;
    private static final String TAG = "EnrollCardInfo";
    private String appId;
    private String cardEntryMode;
    private String cardPresentationMode;
    private String deviceParentId;
    private int enrollType = 0;
    private Bundle extraEnrollData;
    private String gcmId;
    protected String name;
    private int refCount = 1;
    private String sppId;
    private String userEmail;
    private String userId;
    private String walletId;

    static {
        CREATOR = new Parcelable.Creator<EnrollCardInfo>(){

            public EnrollCardInfo createFromParcel(Parcel parcel) {
                int n2 = parcel.readInt();
                if (1 == n2) {
                    return new EnrollCardPanInfo(parcel);
                }
                if (2 == n2) {
                    return new EnrollCardReferenceInfo(parcel);
                }
                if (3 == n2) {
                    return new EnrollCardLoyaltyInfo(parcel);
                }
                return new EnrollCardInfo(parcel);
            }

            public EnrollCardInfo[] newArray(int n2) {
                return new EnrollCardInfo[n2];
            }
        };
        CARD_ENTRY_MODES = new String[]{CARD_ENTRY_MODE_MANUAL, CARD_ENTRY_MODE_OCR, "APP", CARD_ENTRY_MODE_FILE};
        CARD_PRESENTATION_MODES = new String[]{CARD_PRESENTATION_MODE_NFC, CARD_PRESENTATION_MODE_MST, CARD_PRESENTATION_MODE_ECM, CARD_PRESENTATION_MODE_ALL};
    }

    public EnrollCardInfo() {
    }

    public EnrollCardInfo(int n2) {
        this.enrollType = n2;
    }

    private EnrollCardInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private static boolean isValidMode(String[] arrstring, String string) {
        int n2 = arrstring.length;
        int n3 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    bl = false;
                    if (n3 >= n2) break block3;
                    if (!arrstring[n3].equals((Object)string)) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n3;
        } while (true);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected final void clearMemory(String string) {
        if (string == null) return;
        if (string.isEmpty()) {
            return;
        }
        try {
            String.class.getMethod("clear", new Class[0]).invoke((Object)string, new Object[0]);
            Log.d((String)TAG, (String)"clearMemory: sensitive data cleared: ");
            return;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            block5 : {
                noSuchMethodException.printStackTrace();
                break block5;
                catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                    break block5;
                }
                catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
            Log.d((String)TAG, (String)"clearMemory: sensitive data clear failed ");
            return;
        }
    }

    protected void clearSensitiveData() {
    }

    public void decrementRefCount() {
        EnrollCardInfo enrollCardInfo = this;
        synchronized (enrollCardInfo) {
            this.refCount = -1 + this.refCount;
            Log.d((String)TAG, (String)("decrementRefCount: refCount  " + this.refCount));
            if (this.refCount == 0) {
                this.clearSensitiveData();
            }
            return;
        }
    }

    public int describeContents() {
        return 0;
    }

    public String getApplicationId() {
        return this.appId;
    }

    public String getCardEntryMode() {
        return this.cardEntryMode;
    }

    public String getCardPresentationMode() {
        return this.cardPresentationMode;
    }

    public String getDeviceParentId() {
        return this.deviceParentId;
    }

    public int getEnrollType() {
        return this.enrollType;
    }

    public Bundle getExtraEnrollData() {
        return this.extraEnrollData;
    }

    public String getGcmId() {
        return this.gcmId;
    }

    public String getName() {
        return this.name;
    }

    public int getRefCount() {
        Log.d((String)TAG, (String)("getRefCount(): refCount  " + this.refCount));
        return this.refCount;
    }

    public String getSppId() {
        return this.sppId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getWalletId() {
        return this.walletId;
    }

    public void incrementRefCount() {
        EnrollCardInfo enrollCardInfo = this;
        synchronized (enrollCardInfo) {
            this.refCount = 1 + this.refCount;
            Log.d((String)TAG, (String)("incrementRefCount: refCount  " + this.refCount));
            return;
        }
    }

    public void readFromParcel(Parcel parcel) {
        Log.d((String)TAG, (String)"EnrollCardInfo: readFromParcel");
        this.userId = parcel.readString();
        this.userEmail = parcel.readString();
        this.cardEntryMode = parcel.readString();
        this.cardPresentationMode = parcel.readString();
        this.gcmId = parcel.readString();
        this.sppId = parcel.readString();
        this.appId = parcel.readString();
        this.walletId = parcel.readString();
        this.deviceParentId = parcel.readString();
        this.name = parcel.readString();
        this.extraEnrollData = parcel.readBundle();
    }

    public void setApplicationId(String string) {
        this.appId = string;
    }

    public void setCardEntryMode(String string) {
        if (!EnrollCardInfo.isValidMode(CARD_ENTRY_MODES, string)) {
            throw new IllegalArgumentException(string + " is not a valid card entry mode");
        }
        this.cardEntryMode = string;
    }

    public void setCardPresentationMode(String string) {
        if (!EnrollCardInfo.isValidMode(CARD_PRESENTATION_MODES, string)) {
            throw new IllegalArgumentException(string + " is not a valid card presentation mode");
        }
        this.cardPresentationMode = string;
    }

    public void setDeviceParentId(String string) {
        this.deviceParentId = string;
    }

    public void setEnrollType(int n2) {
        this.enrollType = n2;
    }

    public void setExtraEnrollData(Bundle bundle) {
        this.extraEnrollData = bundle;
    }

    public void setGcmId(String string) {
        this.gcmId = string;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setSppId(String string) {
        this.sppId = string;
    }

    public void setUserEmail(String string) {
        this.userEmail = string;
    }

    public void setUserId(String string) {
        this.userId = string;
    }

    public void setWalletId(String string) {
        this.walletId = string;
    }

    public String toString() {
        String string = "EnrollCardInfo: userId: " + this.userId + " userEmail: " + this.userEmail + " cardEntryMode: " + this.cardEntryMode + " cardPresentationMode: " + this.cardPresentationMode + " gcmId: " + this.gcmId + "sppId: " + this.sppId + " appId: " + this.appId + " deviceParentId: " + this.deviceParentId + " walletId: " + this.walletId;
        return string + " extraEnrollData: " + (Object)this.extraEnrollData;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.enrollType);
        parcel.writeString(this.userId);
        parcel.writeString(this.userEmail);
        parcel.writeString(this.cardEntryMode);
        parcel.writeString(this.cardPresentationMode);
        parcel.writeString(this.gcmId);
        parcel.writeString(this.sppId);
        parcel.writeString(this.appId);
        parcel.writeString(this.walletId);
        parcel.writeString(this.deviceParentId);
        parcel.writeString(this.name);
        parcel.writeBundle(this.extraEnrollData);
    }

}

