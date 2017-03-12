package android.sec.clipboard.data.list;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.ClipboardDefine;
import android.sec.clipboard.util.ClipboardProcText;
import android.util.Log;

public class ClipboardDataHTMLFragment extends ClipboardDataHtml {
    private static final String TAG = "ClipboardDataHTMLFragment";
    private static final long serialVersionUID = 1;
    public Bitmap mFirstImg = null;
    public String mFirstImgPath = "";
    public String mValue = "";
    public String mValuePlainText = "";
    public String mValueUrl = "";

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        return super.SetAlternateFormat(format, altData);
    }

    public void clearData() {
        super.clearData();
    }

    public boolean SetHTMLFragment(CharSequence text) {
        return super.setHtml(text);
    }

    public boolean SetHTMLFragmentInternal(CharSequence text, CharSequence html) {
        return super.setHtmlInternal(text, html);
    }

    public boolean SetHTMLFragmentWithImagePathInternal(CharSequence text, CharSequence html, CharSequence filePath) {
        return super.setHtmlWithImagePathInternal(text, html, filePath);
    }

    public boolean SetHTMLFragmentWithImagePath(CharSequence text, CharSequence filePath) {
        return super.setHtmlWithImagePath(text, filePath);
    }

    public boolean SetHTMLFragment(CharSequence text, CharSequence HtmlUrl) {
        if (!SetHTMLFragmentInternal(this.mValuePlainText, text)) {
            return false;
        }
        if (HtmlUrl != null && HtmlUrl.length() > 0) {
            this.mValueUrl = HtmlUrl.toString();
        }
        return true;
    }

    public CharSequence GetHTMLFragment() {
        return super.getHtml();
    }

    public CharSequence GetHTMLUrl() {
        return this.mValueUrl;
    }

    public Bitmap getFirstImage(int reqWidth, int reqHeight) {
        return super.getFirstImage(reqWidth, reqHeight);
    }

    public String getClipHtmlImageFilePath() {
        String sFileName = "";
        try {
            if (this.mValue.length() < 1) {
                if (ClipboardDefine.DEBUG) {
                    Log.w(TAG, "getClipHtmlImageFilePath : Data is empty.");
                }
                return sFileName;
            }
            sFileName = ClipboardProcText.getImgFileNameFormHtml(this.mValue.toString());
            if (sFileName != null) {
                sFileName = Uri.decode(sFileName);
            }
            return sFileName;
        } catch (Exception e) {
            if (ClipboardDefine.DEBUG) {
                Log.e(TAG, "getHtmlImageFilePath : " + e.getMessage());
            }
        }
    }

    public String getDimensionsFromHTML(String aInput) {
        String lDimen = "";
        try {
            if (this.mValue.length() < 1) {
                if (ClipboardDefine.DEBUG) {
                    Log.e(TAG, "getDimensionsFromHTML : Data is empty.");
                }
                return lDimen;
            }
            String IMG_BEGIN = "<img";
            String lImgTag = this.mValue.substring(this.mValue.toLowerCase().indexOf("<img"));
            String lDimenSub = lImgTag.substring((aInput.length() + lImgTag.indexOf(aInput.toLowerCase())) + "=\"".length());
            lDimen = lDimenSub.substring(0, lDimenSub.indexOf("\""));
            return lDimen;
        } catch (Exception e) {
            if (ClipboardDefine.DEBUG) {
                Log.e(TAG, "getDimensionsFromHTML Exception: " + e.getMessage());
            }
        }
    }

    public String getText() {
        return super.getPlainText();
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

    public boolean SetFirstImgPath(String FilePath) {
        return super.setFirstImgPath(FilePath);
    }

    public String GetFirstImgPath() {
        return super.getFirstImgPath();
    }

    protected void readFormSource(Parcel source) {
    }
}
