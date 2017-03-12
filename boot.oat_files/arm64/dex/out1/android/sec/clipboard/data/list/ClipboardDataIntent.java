package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.util.Log;
import java.net.URISyntaxException;

public class ClipboardDataIntent extends ClipboardData {
    private static final String TAG = "ClipboardDataIntent";
    private static final long serialVersionUID = 1;
    private String mValue = "";

    public ClipboardDataIntent() {
        super(6);
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mValue.length() < 1) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 6:
                try {
                    Result = ((ClipboardDataIntent) altData).setIntent(Intent.parseUri(this.mValue, 1));
                    break;
                } catch (URISyntaxException e) {
                    Result = false;
                    e.printStackTrace();
                    break;
                }
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public void clearData() {
        this.mValue = "";
    }

    public boolean SetIntent(Intent intent) {
        return setIntent(intent);
    }

    public boolean setIntent(Intent intent) {
        if (intent == null || intent.toUri(1).length() == 0) {
            return false;
        }
        this.mValue = intent.toUri(1);
        return true;
    }

    public Intent GetIntent() {
        return getIntent();
    }

    public Intent getIntent() {
        Intent intent = null;
        try {
            intent = Intent.parseUri(this.mValue, 1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return intent;
    }

    public boolean isValidData() {
        if (this.mValue == null) {
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "intent equals");
        }
        boolean Result = false;
        if (!super.equals(o)) {
            return 0;
        }
        if (!(o instanceof ClipboardDataIntent)) {
            return 0;
        }
        ClipboardDataIntent trgData = (ClipboardDataIntent) o;
        if (!(trgData == null || trgData.getIntent() == null)) {
            Result = this.mValue.compareTo(trgData.getIntent().toUri(1)) == 0;
        }
        return Result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "Intent write to parcel");
        }
        super.writeToParcel(dest, flags);
        Intent intent = null;
        try {
            intent = Intent.parseUri(this.mValue, 1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (this.mClipdata == null) {
            CharSequence charSequence = "clipboarddragNdrop";
            this.mClipdata = new ClipData(charSequence, new String[]{"text/vnd.android.intent"}, new Item(intent));
        }
        dest.writeValue(this.mValue);
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
    }

    protected void readFromSource(Parcel source) {
        this.mValue = (String) source.readValue(String.class.getClassLoader());
        try {
            this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
            this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
            if (ClipboardConstants.INFO_DEBUG) {
                Log.i(TAG, "ClipboardDataIntent : readFromSource : " + this.mValue);
            }
        } catch (Exception e) {
            Log.i(TAG, "readFromSource~Exception :" + e.getMessage());
        }
    }

    public String toString() {
        return "this Intent class. Value is " + (this.mValue.length() > 20 ? this.mValue.subSequence(0, 20) : this.mValue);
    }

    protected void readFormSource(Parcel source) {
    }
}
