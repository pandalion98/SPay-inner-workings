package android.sec.clipboard.data;

import android.content.ClipData;
import android.os.Binder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.Serializable;

public abstract class ClipboardData implements Parcelable, Serializable {
    public static final Creator<ClipboardData> CREATOR = new Creator<ClipboardData>() {
        public ClipboardData createFromParcel(Parcel source) {
            boolean hasFD = true;
            int format = source.readInt();
            long timestamp = source.readLong();
            long callerUid = source.readLong();
            if (source.readInt() != 1) {
                hasFD = false;
            }
            ParcelFileDescriptor pfd = hasFD ? new ParcelFileDescriptor(source.readRawFileDescriptor()) : null;
            ClipboardData Result = ClipboardDataFactory.CreateClipBoardData(format);
            if (Result != null) {
                Result.setTimestamp(timestamp);
                Result.setCallerUid(callerUid);
                Result.setParcelFileDescriptor(pfd);
                Result.readFromSource(source);
            }
            return Result;
        }

        public ClipboardData[] newArray(int size) {
            return new ClipboardData[size];
        }
    };
    private static final String TAG = "ClipboardData";
    private static final long serialVersionUID = 1;
    protected final int LOG_LEN;
    protected long mCallerUid;
    protected ClipData mClipdata;
    protected int mFormatID;
    protected boolean mIsProtected;
    private transient ParcelFileDescriptor mParcelFd;
    private transient boolean mStateToSave;
    private long mTimestamp;

    public abstract void clearData();

    public abstract boolean isValidData();

    protected abstract void readFormSource(Parcel parcel);

    protected abstract void readFromSource(Parcel parcel);

    public ClipboardData(int format) {
        this.LOG_LEN = 20;
        this.mCallerUid = -1;
        this.mIsProtected = false;
        this.mTimestamp = 0;
        this.mStateToSave = false;
        this.mParcelFd = null;
        this.mStateToSave = false;
        this.mFormatID = format;
        this.mCallerUid = (long) Binder.getCallingUid();
        this.mTimestamp = System.currentTimeMillis();
        this.mParcelFd = null;
    }

    public boolean getStateToSave() {
        return this.mStateToSave;
    }

    public void setStateToSave(boolean state) {
        this.mStateToSave = state;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    private void readObject(ObjectInputStream in) throws OptionalDataException, ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.mTimestamp <= 0) {
            this.mStateToSave = true;
            this.mTimestamp = System.currentTimeMillis() - 2592000000L;
        }
    }

    public void setParcelFileDescriptor(ParcelFileDescriptor fd) {
        this.mParcelFd = fd;
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        return this.mParcelFd;
    }

    public int GetFomat() {
        return this.mFormatID;
    }

    public int getFormat() {
        return this.mFormatID;
    }

    public long getCallerUid() {
        return this.mCallerUid;
    }

    public void setCallerUid(long callerUid) {
        this.mCallerUid = callerUid;
    }

    public ClipboardData GetAlternateFormat(int format) {
        ClipboardData Result = ClipboardDataFactory.CreateClipBoardData(format);
        if (Result != null) {
            if (SetAlternateFormat(format, Result)) {
                return Result;
            }
            return null;
        } else if (!ClipboardConstants.DEBUG) {
            return Result;
        } else {
            Log.i(TAG, "ClipBoardDataFactory.CreateClipBoardData(format) -> result == null, format == " + format);
            return Result;
        }
    }

    public boolean IsAlternateformatAvailable(int format) {
        if (format == 1 || this.mFormatID == format) {
            return true;
        }
        return SetAlternateFormat(format, ClipboardDataFactory.CreateClipBoardData(format));
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (altData == null) {
            return false;
        }
        altData.setParcelFileDescriptor(this.mParcelFd);
        altData.setTimestamp(this.mTimestamp);
        altData.setCallerUid(this.mCallerUid);
        altData.setClipdata(this.mClipdata);
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mFormatID);
        dest.writeLong(this.mTimestamp);
        dest.writeLong(this.mCallerUid);
        if (this.mParcelFd != null) {
            dest.writeInt(1);
            dest.writeFileDescriptor(this.mParcelFd.getFileDescriptor());
            return;
        }
        dest.writeInt(0);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof ClipboardData) {
            return ((ClipboardData) o).getFormat() == getFormat();
        } else {
            return super.equals(o);
        }
    }

    public void SetProtectState(boolean isProtect) {
        this.mIsProtected = isProtect;
    }

    public boolean GetProtectState() {
        return this.mIsProtected;
    }

    public ClipData getClipData() {
        return this.mClipdata;
    }

    public void setClipdata(ClipData data) {
        this.mClipdata = data;
    }
}
