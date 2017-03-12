package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellIdentityWcdma implements Parcelable {
    public static final Creator<CellIdentityWcdma> CREATOR = new Creator<CellIdentityWcdma>() {
        public CellIdentityWcdma createFromParcel(Parcel in) {
            return new CellIdentityWcdma(in);
        }

        public CellIdentityWcdma[] newArray(int size) {
            return new CellIdentityWcdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellIdentityWcdma";
    private final int mCid;
    private final int mLac;
    private final int mMcc;
    private final int mMnc;
    private final int mPsc;

    public CellIdentityWcdma() {
        this.mMcc = Integer.MAX_VALUE;
        this.mMnc = Integer.MAX_VALUE;
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
        this.mPsc = Integer.MAX_VALUE;
    }

    public CellIdentityWcdma(int mcc, int mnc, int lac, int cid, int psc) {
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mLac = lac;
        this.mCid = cid;
        this.mPsc = psc;
    }

    private CellIdentityWcdma(CellIdentityWcdma cid) {
        this.mMcc = cid.mMcc;
        this.mMnc = cid.mMnc;
        this.mLac = cid.mLac;
        this.mCid = cid.mCid;
        this.mPsc = cid.mPsc;
    }

    CellIdentityWcdma copy() {
        return new CellIdentityWcdma(this);
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

    public int getPsc() {
        return this.mPsc;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mMcc), Integer.valueOf(this.mMnc), Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Integer.valueOf(this.mPsc)});
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellIdentityWcdma)) {
            return false;
        }
        CellIdentityWcdma o = (CellIdentityWcdma) other;
        if (this.mMcc == o.mMcc && this.mMnc == o.mMnc && this.mLac == o.mLac && this.mCid == o.mCid && this.mPsc == o.mPsc) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CellIdentityWcdma:{");
        sb.append(" mMcc=").append(this.mMcc);
        sb.append(" mMnc=").append(this.mMnc);
        sb.append(" mLac=").append(this.mLac);
        sb.append(" mCid=").append(this.mCid);
        sb.append(" mPsc=").append(this.mPsc);
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
        dest.writeInt(this.mPsc);
    }

    private CellIdentityWcdma(Parcel in) {
        this.mMcc = in.readInt();
        this.mMnc = in.readInt();
        this.mLac = in.readInt();
        this.mCid = in.readInt();
        this.mPsc = in.readInt();
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
