package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.net.Uri;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.util.Log;
import java.util.ArrayList;

public class ClipboardDataUriList extends ClipboardData {
    private static final String TAG = "ClipboardDataUriList";
    protected static final long serialVersionUID = 1;
    protected ArrayList<String> mUriArray;

    public ClipboardDataUriList() {
        super(7);
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mUriArray.size() < 1) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 7:
                Result = ((ClipboardDataUriList) altData).setUriListInternal(this.mUriArray);
                break;
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public void clearData() {
        this.mUriArray = null;
    }

    public boolean setUriList(ArrayList<Uri> uriList) {
        if (uriList == null) {
            return false;
        }
        int N = uriList.size();
        this.mUriArray = new ArrayList();
        for (int i = 0; i < N; i++) {
            this.mUriArray.add(((Uri) uriList.get(i)).toString());
        }
        return true;
    }

    public boolean setUriListInternal(ArrayList<String> uries) {
        if (uries == null) {
            return false;
        }
        int N = uries.size();
        this.mUriArray = new ArrayList();
        for (int i = 0; i < N; i++) {
            this.mUriArray.add(uries.get(i));
        }
        return true;
    }

    public ArrayList<Uri> getUriList() {
        if (this.mUriArray == null) {
            return null;
        }
        ArrayList<Uri> multiUri = new ArrayList();
        int N = this.mUriArray.size();
        for (int i = 0; i < N; i++) {
            multiUri.add(Uri.parse((String) this.mUriArray.get(i)));
        }
        return multiUri;
    }

    public boolean isValidData() {
        if (this.mUriArray == null) {
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "multiple uri equals");
        }
        if (!super.equals(o) || !(o instanceof ClipboardDataUriList)) {
            return false;
        }
        ClipboardDataUriList trgData = (ClipboardDataUriList) o;
        boolean Result = false;
        if (trgData != null) {
            if (trgData.getUriList() != null) {
                Result = this.mUriArray.toString().compareTo(trgData.getUriList().toString()) == 0;
            } else if (getUriList() == null) {
                return true;
            }
        }
        return Result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "Multiple Uri write to parcel");
        }
        super.writeToParcel(dest, flags);
        if (this.mClipdata == null && this.mUriArray.size() > 0) {
            CharSequence charSequence = "clipboarddragNdrop";
            this.mClipdata = new ClipData(charSequence, new String[]{"text/uri-list"}, new Item(Uri.parse((String) this.mUriArray.get(0))));
            for (int i = 1; i < this.mUriArray.size(); i++) {
                this.mClipdata.addItem(new Item(Uri.parse((String) this.mUriArray.get(i))));
            }
        }
        dest.writeStringList(this.mUriArray);
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
    }

    protected void readFromSource(Parcel source) {
        this.mUriArray = new ArrayList();
        source.readStringList(this.mUriArray);
        this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
        this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
    }

    public String toString() {
        return "this UriList class. Value is " + (this.mUriArray.toString().length() > 20 ? this.mUriArray.toString().subSequence(0, 20) : this.mUriArray.toString());
    }

    protected void readFormSource(Parcel source) {
    }
}
