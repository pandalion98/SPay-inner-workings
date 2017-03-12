package android.sec.enterprise.email;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class EnterpriseExchangeAccount implements Parcelable {
    public static final Creator<EnterpriseExchangeAccount> CREATOR = new Creator<EnterpriseExchangeAccount>() {
        public EnterpriseExchangeAccount createFromParcel(Parcel in) {
            return new EnterpriseExchangeAccount(in);
        }

        public EnterpriseExchangeAccount[] newArray(int size) {
            return new EnterpriseExchangeAccount[size];
        }
    };
    public boolean mAcceptAllCertificates;
    public boolean mAllowHTMLEmail;
    public String mDisplayName;
    public String mEasUser;
    public String mEmailAddress;
    public int mEmailBodyTruncationSize;
    public boolean mEmailNotificationVibrateAlways;
    public boolean mEmailNotificationVibrateWhenSilent;
    public int mEmailRoamingBodyTruncationSize;
    public long mId;
    public boolean mIsDefault;
    public int mMaxCalendarAgeFilter;
    public int mMaxDevicePasswordFailedAttempts;
    public int mMaxEmailAgeFilter;
    public int mMaxEmailBodyTruncationSize;
    public int mMaxEmailHtmlBodyTruncationSize;
    public int mMinDevicePasswordLength;
    public int mMinPasswordComplexCharacters;
    public int mOffPeakSyncSchedule;
    public String mPassword;
    public int mPeakDays;
    public int mPeakEndMinute;
    public int mPeakStartMinute;
    public int mPeakSyncSchedule;
    public String mProtocol;
    public String mProtocolVersion;
    public int mRoamingSyncSchedule;
    public String mSenderName;
    public String mServerAddress;
    public int mServerPort;
    public String mSignature;
    public boolean mSyncCalendar;
    public int mSyncCalendarAge;
    public boolean mSyncContacts;
    public int mSyncInterval;
    public int mSyncLookback;
    public boolean mSyncNotes;
    public boolean mSyncTasks;
    public boolean mUseSSL;
    public boolean mUseTLS;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 0;
        out.writeString(this.mEasUser);
        out.writeString(this.mServerAddress);
        out.writeInt(this.mServerPort);
        out.writeString(this.mProtocol);
        out.writeString(this.mPassword);
        out.writeInt(this.mUseSSL ? 0 : 1);
        if (this.mUseTLS) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mAcceptAllCertificates) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        out.writeLong(this.mId);
        out.writeString(this.mDisplayName);
        out.writeString(this.mEmailAddress);
        out.writeString(this.mSenderName);
        out.writeString(this.mProtocolVersion);
        out.writeString(this.mSignature);
        out.writeInt(this.mSyncLookback);
        out.writeInt(this.mSyncInterval);
        if (this.mEmailNotificationVibrateAlways) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mEmailNotificationVibrateWhenSilent) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mIsDefault) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        out.writeInt(this.mPeakDays);
        out.writeInt(this.mPeakStartMinute);
        out.writeInt(this.mPeakEndMinute);
        out.writeInt(this.mPeakSyncSchedule);
        out.writeInt(this.mOffPeakSyncSchedule);
        out.writeInt(this.mRoamingSyncSchedule);
        out.writeInt(this.mSyncCalendarAge);
        out.writeInt(this.mEmailBodyTruncationSize);
        out.writeInt(this.mEmailRoamingBodyTruncationSize);
        if (this.mSyncCalendar) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mSyncContacts) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mSyncTasks) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mSyncNotes) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (!this.mAllowHTMLEmail) {
            i2 = 1;
        }
        out.writeInt(i2);
        out.writeInt(this.mMinDevicePasswordLength);
        out.writeInt(this.mMinPasswordComplexCharacters);
        out.writeInt(this.mMaxEmailHtmlBodyTruncationSize);
        out.writeInt(this.mMaxEmailBodyTruncationSize);
        out.writeInt(this.mMaxCalendarAgeFilter);
        out.writeInt(this.mMaxEmailAgeFilter);
        out.writeInt(this.mMaxDevicePasswordFailedAttempts);
    }

    private EnterpriseExchangeAccount(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.mEasUser = in.readString();
        this.mServerAddress = in.readString();
        this.mServerPort = in.readInt();
        this.mProtocol = in.readString();
        this.mPassword = in.readString();
        this.mUseSSL = in.readInt() == 0;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mUseTLS = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mAcceptAllCertificates = z;
        this.mId = in.readLong();
        this.mDisplayName = in.readString();
        this.mEmailAddress = in.readString();
        this.mSenderName = in.readString();
        this.mProtocolVersion = in.readString();
        this.mSignature = in.readString();
        this.mSyncLookback = in.readInt();
        this.mSyncInterval = in.readInt();
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mEmailNotificationVibrateAlways = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mEmailNotificationVibrateWhenSilent = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mIsDefault = z;
        this.mPeakDays = in.readInt();
        this.mPeakStartMinute = in.readInt();
        this.mPeakEndMinute = in.readInt();
        this.mPeakSyncSchedule = in.readInt();
        this.mOffPeakSyncSchedule = in.readInt();
        this.mRoamingSyncSchedule = in.readInt();
        this.mSyncCalendarAge = in.readInt();
        this.mEmailBodyTruncationSize = in.readInt();
        this.mEmailRoamingBodyTruncationSize = in.readInt();
        this.mSyncCalendar = in.readInt() == 0;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mSyncContacts = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mSyncTasks = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mSyncNotes = z;
        if (in.readInt() != 0) {
            z2 = false;
        }
        this.mAllowHTMLEmail = z2;
        this.mMinDevicePasswordLength = in.readInt();
        this.mMinPasswordComplexCharacters = in.readInt();
        this.mMaxEmailHtmlBodyTruncationSize = in.readInt();
        this.mMaxEmailBodyTruncationSize = in.readInt();
        this.mMaxCalendarAgeFilter = in.readInt();
        this.mMaxEmailAgeFilter = in.readInt();
        this.mMaxDevicePasswordFailedAttempts = in.readInt();
    }

    public String toString() {
        return "mId = " + this.mId + ", mDisplayName=" + this.mDisplayName + ",mEmailAddress =" + this.mEmailAddress + ", mSenderName=" + this.mSenderName + ", mProtocol=" + this.mProtocol + ", mProtocolVersion = " + this.mProtocolVersion + ", mUseSSL=" + this.mUseSSL + ", mUseTLS=" + this.mUseTLS + ", mAcceptAllCertificates = " + this.mAcceptAllCertificates + ", mSyncLookback=" + this.mSyncLookback + ", mSyncInterval=" + this.mSyncInterval + ", mEmailNotificationVibrateAlways=" + this.mEmailNotificationVibrateAlways + ", mIsDefault =" + this.mIsDefault + ", mPeakDays =" + this.mPeakDays + ", mPeakStartMinute=" + this.mPeakStartMinute + ", mPeakEndMinute=" + this.mPeakEndMinute + ", mPeakSyncSchedule=" + this.mPeakSyncSchedule + ", mOffPeakSyncSchedule=" + this.mOffPeakSyncSchedule + ", mRoamingSyncSchedule=" + this.mRoamingSyncSchedule + ", mSyncCalendarAge=" + this.mSyncCalendarAge + ", mEmailBodyTruncationSize=" + this.mEmailBodyTruncationSize + ", mEmailRoamingBodyTruncationSize=" + this.mEmailRoamingBodyTruncationSize + ", mSyncCalendar=" + this.mSyncCalendar + ", mSyncContacts=" + this.mSyncContacts + ", mAllowHTMLEmail=" + this.mAllowHTMLEmail;
    }
}
