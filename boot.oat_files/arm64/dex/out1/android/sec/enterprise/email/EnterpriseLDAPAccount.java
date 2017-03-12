package android.sec.enterprise.email;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class EnterpriseLDAPAccount implements Parcelable {
    public static final Creator<EnterpriseLDAPAccount> CREATOR = new Creator<EnterpriseLDAPAccount>() {
        public EnterpriseLDAPAccount createFromParcel(Parcel in) {
            return new EnterpriseLDAPAccount(in);
        }

        public EnterpriseLDAPAccount[] newArray(int size) {
            return new EnterpriseLDAPAccount[size];
        }
    };
    public String mBaseDN;
    public String mHost;
    public long mId;
    public boolean mIsAnonymous;
    public String mPassword;
    public int mPort;
    public int mTrustAll;
    public boolean mUseSSL;
    public String mUserName;

    public int describeContents() {
        return 0;
    }

    public EnterpriseLDAPAccount(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 0;
        out.writeLong(this.mId);
        out.writeString(this.mUserName);
        out.writeString(this.mPassword);
        out.writeInt(this.mPort);
        out.writeString(this.mHost);
        if (this.mUseSSL) {
            i = 0;
        } else {
            i = 1;
        }
        out.writeInt(i);
        if (!this.mIsAnonymous) {
            i2 = 1;
        }
        out.writeInt(i2);
        out.writeString(this.mBaseDN);
        out.writeInt(this.mTrustAll);
    }

    private void readFromParcel(Parcel in) {
        boolean z = true;
        this.mId = in.readLong();
        this.mUserName = in.readString();
        this.mPassword = in.readString();
        this.mPort = in.readInt();
        this.mHost = in.readString();
        this.mUseSSL = in.readInt() == 0;
        if (in.readInt() != 0) {
            z = false;
        }
        this.mIsAnonymous = z;
        this.mBaseDN = in.readString();
        this.mTrustAll = in.readInt();
    }
}
