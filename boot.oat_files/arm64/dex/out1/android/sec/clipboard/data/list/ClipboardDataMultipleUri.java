package android.sec.clipboard.data.list;

import android.net.Uri;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardData;
import java.util.ArrayList;

public class ClipboardDataMultipleUri extends ClipboardDataUriList {
    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        return super.SetAlternateFormat(format, altData);
    }

    public void clearData() {
        super.clearData();
    }

    public boolean SetMultipleUri(ArrayList<Uri> uries) {
        return super.setUriList(uries);
    }

    public boolean SetMultipleUriInternal(ArrayList<String> uries) {
        return super.setUriListInternal(uries);
    }

    public ArrayList<Uri> GetMultipleUri() {
        return super.getUriList();
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected void readFromSource(Parcel source) {
        super.readFromSource(source);
    }

    public String toString() {
        return super.toString();
    }

    protected void readFormSource(Parcel source) {
    }
}
