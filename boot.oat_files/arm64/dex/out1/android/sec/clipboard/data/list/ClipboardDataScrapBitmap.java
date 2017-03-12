package android.sec.clipboard.data.list;

import android.os.Parcel;
import android.sec.clipboard.data.ClipboardData;

public class ClipboardDataScrapBitmap extends ClipboardData {
    public ClipboardDataScrapBitmap() {
        super(10);
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        return false;
    }

    public void clearData() {
    }

    public boolean SetBitmapPath(String FilePath) {
        return false;
    }

    public String GetBitmapPath() {
        return null;
    }

    public boolean isValidData() {
        return false;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    protected void readFormSource(Parcel source) {
    }

    protected void readFromSource(Parcel source) {
    }
}
