package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;

public class EnrollCardInfo implements Parcelable {
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
    public static final Creator<EnrollCardInfo> CREATOR;
    protected static final int ENROLL_TYPE_LOYALTY = 3;
    protected static final int ENROLL_TYPE_PAN = 1;
    protected static final int ENROLL_TYPE_PAN_REFERENCE = 2;
    private static final String TAG = "EnrollCardInfo";
    private String appId;
    private String cardEntryMode;
    private String cardPresentationMode;
    private String deviceParentId;
    private int enrollType;
    private Bundle extraEnrollData;
    private String gcmId;
    protected String name;
    private int refCount;
    private String sppId;
    private String userEmail;
    private String userId;
    private String walletId;

    /* renamed from: com.samsung.android.spayfw.appinterface.EnrollCardInfo.1 */
    static class C03441 implements Creator<EnrollCardInfo> {
        C03441() {
        }

        public EnrollCardInfo createFromParcel(Parcel parcel) {
            int readInt = parcel.readInt();
            if (EnrollCardInfo.ENROLL_TYPE_PAN == readInt) {
                return new EnrollCardPanInfo(parcel);
            }
            if (EnrollCardInfo.ENROLL_TYPE_PAN_REFERENCE == readInt) {
                return new EnrollCardReferenceInfo(parcel);
            }
            if (EnrollCardInfo.ENROLL_TYPE_LOYALTY == readInt) {
                return new EnrollCardLoyaltyInfo(parcel);
            }
            return new EnrollCardInfo(null);
        }

        public EnrollCardInfo[] newArray(int i) {
            return new EnrollCardInfo[i];
        }
    }

    static {
        CREATOR = new C03441();
        CARD_ENTRY_MODES = new String[]{CARD_ENTRY_MODE_MANUAL, CARD_ENTRY_MODE_OCR, CARD_PRESENTATION_MODE_APP, CARD_ENTRY_MODE_FILE};
        CARD_PRESENTATION_MODES = new String[]{CARD_PRESENTATION_MODE_NFC, CARD_PRESENTATION_MODE_MST, CARD_PRESENTATION_MODE_ECM, CARD_PRESENTATION_MODE_ALL};
    }

    private static boolean isValidMode(String[] strArr, String str) {
        int length = strArr.length;
        for (int i = 0; i < length; i += ENROLL_TYPE_PAN) {
            if (strArr[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private EnrollCardInfo(Parcel parcel) {
        this.enrollType = 0;
        this.refCount = ENROLL_TYPE_PAN;
        readFromParcel(parcel);
    }

    public EnrollCardInfo() {
        this.enrollType = 0;
        this.refCount = ENROLL_TYPE_PAN;
    }

    public EnrollCardInfo(int i) {
        this.enrollType = 0;
        this.refCount = ENROLL_TYPE_PAN;
        this.enrollType = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getEnrollType() {
        return this.enrollType;
    }

    public void setEnrollType(int i) {
        this.enrollType = i;
    }

    public Bundle getExtraEnrollData() {
        return this.extraEnrollData;
    }

    public void setExtraEnrollData(Bundle bundle) {
        this.extraEnrollData = bundle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
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

    public String getApplicationId() {
        return this.appId;
    }

    public void setApplicationId(String str) {
        this.appId = str;
    }

    public String getCardEntryMode() {
        return this.cardEntryMode;
    }

    public void setCardEntryMode(String str) {
        if (isValidMode(CARD_ENTRY_MODES, str)) {
            this.cardEntryMode = str;
            return;
        }
        throw new IllegalArgumentException(str + " is not a valid card entry mode");
    }

    public String getCardPresentationMode() {
        return this.cardPresentationMode;
    }

    public void setCardPresentationMode(String str) {
        if (isValidMode(CARD_PRESENTATION_MODES, str)) {
            this.cardPresentationMode = str;
            return;
        }
        throw new IllegalArgumentException(str + " is not a valid card presentation mode");
    }

    public String getDeviceParentId() {
        return this.deviceParentId;
    }

    public void setDeviceParentId(String str) {
        this.deviceParentId = str;
    }

    public String getGcmId() {
        return this.gcmId;
    }

    public void setGcmId(String str) {
        this.gcmId = str;
    }

    public String getSppId() {
        return this.sppId;
    }

    public void setSppId(String str) {
        this.sppId = str;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String str) {
        this.userEmail = str;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public String getWalletId() {
        return this.walletId;
    }

    public void setWalletId(String str) {
        this.walletId = str;
    }

    public void readFromParcel(Parcel parcel) {
        Log.d(TAG, "EnrollCardInfo: readFromParcel");
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

    public synchronized void incrementRefCount() {
        this.refCount += ENROLL_TYPE_PAN;
        Log.d(TAG, "incrementRefCount: refCount  " + this.refCount);
    }

    public synchronized void decrementRefCount() {
        this.refCount--;
        Log.d(TAG, "decrementRefCount: refCount  " + this.refCount);
        if (this.refCount == 0) {
            clearSensitiveData();
        }
    }

    public String toString() {
        return ("EnrollCardInfo: userId: " + this.userId + " userEmail: " + this.userEmail + " cardEntryMode: " + this.cardEntryMode + " cardPresentationMode: " + this.cardPresentationMode + " gcmId: " + this.gcmId + "sppId: " + this.sppId + " appId: " + this.appId + " deviceParentId: " + this.deviceParentId + " walletId: " + this.walletId) + " extraEnrollData: " + this.extraEnrollData;
    }

    public int getRefCount() {
        Log.d(TAG, "getRefCount(): refCount  " + this.refCount);
        return this.refCount;
    }

    protected final void clearMemory(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                String.class.getMethod("clear", new Class[0]).invoke(str, new Object[0]);
                Log.d(TAG, "clearMemory: sensitive data cleared: ");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Log.d(TAG, "clearMemory: sensitive data clear failed ");
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                Log.d(TAG, "clearMemory: sensitive data clear failed ");
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
                Log.d(TAG, "clearMemory: sensitive data clear failed ");
            }
        }
    }

    protected void clearSensitiveData() {
    }
}
