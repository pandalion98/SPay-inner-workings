package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.util.HtmlUtils;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

public class ClipboardDataText extends ClipboardData {
    private static final String TAG = "ClipboardDataText";
    private static final long serialVersionUID = 1;
    private int mNumberOfTrailingWhiteLines;
    private transient CharSequence mText;
    private String mValue;

    public ClipboardDataText() {
        super(2);
        this.mValue = "";
        this.mText = "";
        this.mNumberOfTrailingWhiteLines = 0;
        this.mNumberOfTrailingWhiteLines = 0;
    }

    public void toLoad() {
        if (this.mValue != null) {
            if (HtmlUtils.isHtml(this.mValue)) {
                this.mText = Html.fromHtml(this.mValue);
            } else {
                this.mText = this.mValue;
            }
            int numNewLine = 0;
            int i = 1;
            while (i <= this.mText.length() - 1 && this.mText.charAt(this.mText.length() - i) == '\n') {
                numNewLine++;
                i++;
            }
            if (numNewLine > this.mNumberOfTrailingWhiteLines) {
                this.mText = this.mText.subSequence(0, this.mText.length() - (numNewLine - this.mNumberOfTrailingWhiteLines));
            }
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "mValue = " + this.mValue.toString());
                Log.d(TAG, "mText = " + this.mText.toString());
            }
        }
    }

    public void toSave() {
        if (this.mText == null) {
            return;
        }
        if (this.mText instanceof Spanned) {
            this.mNumberOfTrailingWhiteLines = 0;
            int i = 1;
            while (i <= this.mText.length() - 1 && this.mText.charAt(this.mText.length() - i) == '\n') {
                this.mNumberOfTrailingWhiteLines++;
                i++;
            }
            this.mValue = Html.toHtml((Spanned) this.mText);
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "mText is an instance of Spanned: mValue = " + this.mValue.toString());
                return;
            }
            return;
        }
        this.mValue = this.mText.toString();
        if (ClipboardConstants.DEBUG) {
            Log.d(TAG, "mText is not an instance of Spanned: mValue = " + this.mValue.toString());
        }
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mText.length() < 1) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 2:
                Result = ((ClipboardDataText) altData).setText(this.mText);
                break;
            case 4:
                Result = ((ClipboardDataHtml) altData).setHtml(this.mText);
                break;
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public void clearData() {
        this.mText = "";
    }

    public boolean SetText(CharSequence text) {
        return setText(text);
    }

    public boolean setText(CharSequence text) {
        if (text == null || text.toString().length() == 0) {
            return false;
        }
        if (text.length() > 131072) {
            text = text.subSequence(0, 131072);
        }
        this.mText = text;
        return true;
    }

    public CharSequence GetText() {
        return getText();
    }

    public CharSequence getText() {
        return this.mText;
    }

    public boolean isValidData() {
        if (this.mText == null) {
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "text equals");
        }
        if (!super.equals(o) || !(o instanceof ClipboardDataText)) {
            return false;
        }
        return this.mText.toString().compareTo(((ClipboardDataText) o).getText().toString()) == 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "text write to parcel");
        }
        super.writeToParcel(dest, flags);
        if (this.mClipdata == null) {
            CharSequence charSequence = "clipboarddragNdrop";
            this.mClipdata = new ClipData(charSequence, new String[]{"text/plain"}, new Item(this.mText));
        }
        dest.writeValue(this.mText);
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
    }

    protected void readFromSource(Parcel source) {
        this.mText = (CharSequence) source.readValue(CharSequence.class.getClassLoader());
        this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
        this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("this Text class. Value is ");
        Object subSequence = (this.mText == null || this.mText.length() <= 20) ? this.mText : this.mText.subSequence(0, 20);
        return append.append(subSequence).toString();
    }

    protected void readFormSource(Parcel source) {
    }
}
