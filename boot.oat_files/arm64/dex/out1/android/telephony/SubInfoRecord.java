package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SubInfoRecord implements Parcelable {
    public static final Creator<SubInfoRecord> CREATOR = new Creator<SubInfoRecord>() {
        public SubInfoRecord createFromParcel(Parcel source) {
            return null;
        }

        public SubInfoRecord[] newArray(int size) {
            return null;
        }
    };
    public int color;
    public int dataRoaming;
    public String displayName;
    public int displayNumberFormat;
    public String iccId;
    public int mNwMode;
    public int mStatus;
    public int mcc;
    public int mnc;
    public int nameSource;
    public String number;
    public int[] simIconRes;
    public int slotId;
    public long subId;

    public SubInfoRecord() {
        this.subId = -1000;
        this.iccId = "";
        this.slotId = -1;
        this.displayName = "";
        this.nameSource = 0;
        this.color = 0;
        this.number = "";
        this.displayNumberFormat = 0;
        this.dataRoaming = 0;
        this.simIconRes = new int[2];
        this.mcc = 0;
        this.mnc = 0;
        this.mStatus = 0;
        this.mNwMode = -1;
    }

    public SubInfoRecord(long subId, String iccId, int slotId, String displayName, int nameSource, int color, String number, int displayFormat, int roaming, int[] iconRes, int mcc, int mnc, int status) {
    }

    public SubInfoRecord(long subId, String iccId, int slotId, String displayName, int nameSource, int color, String number, int displayFormat, int roaming, int[] iconRes, int mcc, int mnc, int status, int nwMode) {
        this.subId = subId;
        this.iccId = iccId;
        this.slotId = slotId;
        this.displayName = displayName;
        this.nameSource = nameSource;
        this.color = color;
        this.number = number;
        this.displayNumberFormat = displayFormat;
        this.dataRoaming = roaming;
        this.simIconRes = iconRes;
        this.mcc = mcc;
        this.mnc = mnc;
        this.mStatus = status;
        this.mNwMode = nwMode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.subId);
        dest.writeString(this.iccId);
        dest.writeInt(this.slotId);
        dest.writeString(this.displayName);
        dest.writeInt(this.nameSource);
        dest.writeInt(this.color);
        dest.writeString(this.number);
        dest.writeInt(this.displayNumberFormat);
        dest.writeInt(this.dataRoaming);
        dest.writeIntArray(this.simIconRes);
        dest.writeInt(this.mcc);
        dest.writeInt(this.mnc);
        dest.writeInt(this.mStatus);
        dest.writeInt(this.mNwMode);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "None";
    }
}
