package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.util.Log;

public class ClipboardDataMultipleType extends ClipboardData {
    private static final String TAG = "ClipboardDataMultipleType";
    private static final long serialVersionUID = 1;

    public ClipboardDataMultipleType() {
        super(8);
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mClipdata == null || this.mClipdata.getItemCount() < 2) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 8:
                Result = ((ClipboardDataMultipleType) altData).setMultipleType(this.mClipdata);
                break;
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public boolean setMultipleType(ClipData clipdata) {
        this.mClipdata = clipdata;
        return true;
    }

    public ClipData getMultipleType() {
        return this.mClipdata;
    }

    public void clearData() {
        this.mClipdata = null;
    }

    public boolean isValidData() {
        if (this.mClipdata == null) {
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "multiple type equals");
        }
        if (!super.equals(o) || !(o instanceof ClipboardDataMultipleType)) {
            return false;
        }
        return this.mClipdata.equals(((ClipboardDataMultipleType) o).getClipData());
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "Multiple Type write to parcel");
        }
        super.writeToParcel(dest, flags);
        if (this.mClipdata == null) {
            this.mClipdata = ClipData.newPlainText("", "");
        }
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
    }

    protected void readFromSource(Parcel source) {
        try {
            this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
            this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
            if (ClipboardConstants.INFO_DEBUG) {
                Log.i(TAG, "ClipboardDataMultipleType : readFromSource : ");
            }
        } catch (Exception e) {
            Log.i(TAG, "readFromSource~Exception :" + e.getMessage());
        }
    }

    protected void readFormSource(Parcel source) {
    }
}
