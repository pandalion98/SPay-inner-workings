package android.sec.enterprise.email;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class EnterpriseEmailAccount implements Parcelable {
    public static final Creator<EnterpriseEmailAccount> CREATOR = new Creator<EnterpriseEmailAccount>() {
        public EnterpriseEmailAccount createFromParcel(Parcel in) {
            return new EnterpriseEmailAccount(in);
        }

        public EnterpriseEmailAccount[] newArray(int size) {
            return new EnterpriseEmailAccount[size];
        }
    };
    public String mDisplayName;
    public String mEmailAddress;
    public boolean mEmailNotificationVibrateAlways;
    public boolean mEmailNotificationVibrateWhenSilent;
    public long mId;
    public boolean mInComingAcceptAllCertificates;
    public String mInComingPassword;
    public String mInComingProtocol;
    public String mInComingServerAddress;
    public int mInComingServerPort;
    public boolean mInComingUseSSL;
    public boolean mInComingUseTLS;
    public String mInComingUserName;
    public boolean mIsDefault;
    public int mOffPeakSyncSchedule;
    public boolean mOutgoingAcceptAllCertificates;
    public String mOutgoingPassword;
    public String mOutgoingProtocol;
    public String mOutgoingServerAddress;
    public int mOutgoingServerPort;
    public boolean mOutgoingUseSSL;
    public boolean mOutgoingUseTLS;
    public String mOutgoingUserName;
    public int mPeakDays;
    public int mPeakEndMinute;
    public int mPeakStartMinute;
    public int mPeakSyncSchedule;
    public int mRoamingSyncSchedule;
    public String mSenderName;
    public String mSignature;
    public int mSyncInterval;
    public int mSyncLookback;

    public int describeContents() {
        return 0;
    }

    private EnterpriseEmailAccount(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flag) {
        int i;
        int i2 = 0;
        out.writeString(this.mOutgoingUserName);
        out.writeString(this.mOutgoingServerAddress);
        out.writeInt(this.mOutgoingServerPort);
        out.writeString(this.mOutgoingProtocol);
        out.writeString(this.mOutgoingPassword);
        out.writeInt(this.mOutgoingUseSSL ? 0 : 1);
        if (this.mOutgoingUseTLS) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mOutgoingAcceptAllCertificates) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        out.writeString(this.mInComingUserName);
        out.writeString(this.mInComingServerAddress);
        out.writeInt(this.mInComingServerPort);
        out.writeString(this.mInComingProtocol);
        out.writeString(this.mInComingPassword);
        if (this.mInComingUseSSL) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mInComingUseTLS) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (this.mInComingAcceptAllCertificates) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        out.writeLong(this.mId);
        out.writeString(this.mDisplayName);
        out.writeString(this.mEmailAddress);
        out.writeString(this.mSenderName);
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
        if (!this.mIsDefault) {
            i2 = 1;
        }
        out.writeInt(i2);
        out.writeInt(this.mPeakDays);
        out.writeInt(this.mPeakStartMinute);
        out.writeInt(this.mPeakEndMinute);
        out.writeInt(this.mPeakSyncSchedule);
        out.writeInt(this.mOffPeakSyncSchedule);
        out.writeInt(this.mRoamingSyncSchedule);
    }

    public void readFromParcel(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.mOutgoingUserName = in.readString();
        this.mOutgoingServerAddress = in.readString();
        this.mOutgoingServerPort = in.readInt();
        this.mOutgoingProtocol = in.readString();
        this.mOutgoingPassword = in.readString();
        this.mOutgoingUseSSL = in.readInt() == 0;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mOutgoingUseTLS = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mOutgoingAcceptAllCertificates = z;
        this.mInComingUserName = in.readString();
        this.mInComingServerAddress = in.readString();
        this.mInComingServerPort = in.readInt();
        this.mInComingProtocol = in.readString();
        this.mInComingPassword = in.readString();
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mInComingUseSSL = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mInComingUseTLS = z;
        if (in.readInt() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mInComingAcceptAllCertificates = z;
        this.mId = in.readLong();
        this.mDisplayName = in.readString();
        this.mEmailAddress = in.readString();
        this.mSenderName = in.readString();
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
        if (in.readInt() != 0) {
            z2 = false;
        }
        this.mIsDefault = z2;
        this.mPeakDays = in.readInt();
        this.mPeakStartMinute = in.readInt();
        this.mPeakEndMinute = in.readInt();
        this.mPeakSyncSchedule = in.readInt();
        this.mOffPeakSyncSchedule = in.readInt();
        this.mRoamingSyncSchedule = in.readInt();
    }

    public String toString() {
        return "mId = " + this.mId + ", mDisplayName=" + this.mDisplayName + ", mEmailAddress=" + this.mEmailAddress + ", mSenderName" + this.mSenderName + ", mSyncLookback =" + this.mSyncLookback + ", mSyncInterval=" + this.mSyncInterval + ", mEmailNotificationVibrateAlways =" + this.mEmailNotificationVibrateAlways + ", mIsDefault=" + this.mIsDefault + ", mPeakDays=" + this.mPeakDays + ", mPeakStartMinute=" + this.mPeakStartMinute + ", mPeakEndMinute=" + this.mPeakEndMinute + ", mPeakSyncSchedule= " + this.mPeakSyncSchedule + ", mOffPeakSyncSchedule=" + this.mOffPeakSyncSchedule + ", mRoamingSyncSchedule=" + this.mRoamingSyncSchedule;
    }
}
