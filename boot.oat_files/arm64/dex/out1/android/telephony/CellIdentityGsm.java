package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellIdentityGsm implements Parcelable {
    public static final Creator<CellIdentityGsm> CREATOR = new Creator<CellIdentityGsm>() {
        public CellIdentityGsm createFromParcel(Parcel in) {
            return new CellIdentityGsm(in);
        }

        public CellIdentityGsm[] newArray(int size) {
            return new CellIdentityGsm[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellIdentityGsm";
    private final int mCid;
    private final int mLac;
    private final int mMcc;
    private final int mMnc;

    public CellIdentityGsm() {
        this.mMcc = Integer.MAX_VALUE;
        this.mMnc = Integer.MAX_VALUE;
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
    }

    public CellIdentityGsm(int mcc, int mnc, int lac, int cid) {
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mLac = lac;
        this.mCid = cid;
    }

    private CellIdentityGsm(CellIdentityGsm cid) {
        this.mMcc = cid.mMcc;
        this.mMnc = cid.mMnc;
        this.mLac = cid.mLac;
        this.mCid = cid.mCid;
    }

    CellIdentityGsm copy() {
        return new CellIdentityGsm(this);
    }

    public int getMcc() {
        return this.mMcc;
    }

    public int getMnc() {
        return this.mMnc;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    @Deprecated
    public int getPsc() {
        return Integer.MAX_VALUE;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mMcc), Integer.valueOf(this.mMnc), Integer.valueOf(this.mLac), Integer.valueOf(this.mCid)});
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellIdentityGsm)) {
            return false;
        }
        CellIdentityGsm o = (CellIdentityGsm) other;
        if (this.mMcc == o.mMcc && this.mMnc == o.mMnc && this.mLac == o.mLac && this.mCid == o.mCid) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CellIdentityGsm:{");
        sb.append(" mMcc=").append(this.mMcc);
        sb.append(" mMnc=").append(this.mMnc);
        sb.append(" mLac=").append(this.mLac);
        sb.append(" mCid=").append(this.mCid);
        sb.append("}");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMcc);
        dest.writeInt(this.mMnc);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
    }

    private CellIdentityGsm(Parcel in) {
        this.mMcc = in.readInt();
        this.mMnc = in.readInt();
        this.mLac = in.readInt();
        this.mCid = in.readInt();
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
