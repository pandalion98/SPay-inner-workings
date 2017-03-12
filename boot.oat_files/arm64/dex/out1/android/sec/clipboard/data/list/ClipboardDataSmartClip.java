package android.sec.clipboard.data.list;

import android.os.Parcel;
import android.sec.clipboard.data.ClipboardData;
import com.samsung.android.smartclip.SmartClipDataRepositoryImpl;

public class ClipboardDataSmartClip extends ClipboardData {
    public ClipboardDataSmartClip() {
        super(9);
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        return false;
    }

    public boolean SetSmartClip(SmartClipDataRepositoryImpl smartClip) {
        return false;
    }

    public void setBmpImagePath(String Path) {
    }

    public String getBmpImagePath() {
        return null;
    }

    public void clearData() {
    }

    public boolean isValidData() {
        return false;
    }

    public SmartClipDataRepositoryImpl GetSmartClip() {
        return null;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    protected void readFormSource(Parcel source) {
    }

    protected void readFromSource(Parcel source) {
    }

    public String getImagePath() {
        return null;
    }
}
