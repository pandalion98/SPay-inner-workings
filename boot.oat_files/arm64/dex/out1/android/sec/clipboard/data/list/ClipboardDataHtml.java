package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.util.ClipboardDataBitmapUrl;
import android.sec.clipboard.util.ClipboardProcText;
import android.text.Html;
import android.util.Log;
import java.io.File;

public class ClipboardDataHtml extends ClipboardData {
    private static final String TAG = "ClipboardDataHtml";
    private static final String regex = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
    private static final String regex2 = "(?i)<[^/bpd][^>]*>|<p[a-z][^>]*>|<br[a-z][^>]*>|<d[^i][^v][^>]*>|<div[a-z][^>]*>|</[^bpd]+?>|</p[a-z]+>|</br[a-z]+>|</d[^i][^v]+>|</div[a-z]+>";
    private static final long serialVersionUID = 1;
    private Bitmap mFirstImg;
    private String mFirstImgPath;
    private String mHtml;
    private String mPlainText;

    public ClipboardDataHtml() {
        super(4);
        this.mHtml = "";
        this.mPlainText = "";
        this.mFirstImg = null;
        this.mFirstImgPath = "";
    }

    public ClipboardDataHtml(ClipboardDataHTMLFragment htmlFragment) {
        this();
        this.mHtml = htmlFragment.mValue;
        this.mPlainText = htmlFragment.mValuePlainText;
        this.mFirstImg = htmlFragment.mFirstImg;
        this.mFirstImgPath = htmlFragment.mFirstImgPath;
        setTimestamp(htmlFragment.getTimestamp());
    }

    protected void setClipboardDataHtml(String html, String plain, Bitmap firstImg, String firstImgPath) {
        this.mHtml = html;
        this.mPlainText = plain;
        this.mFirstImg = firstImg;
        this.mFirstImgPath = firstImgPath;
    }

    public boolean isValidData() {
        if (this.mHtml == null) {
            return false;
        }
        return true;
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mHtml.length() < 1) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 2:
                Result = ((ClipboardDataText) altData).setText(this.mPlainText);
                break;
            case 4:
                ((ClipboardDataHtml) altData).setHtmlWithImagePathInternal(this.mPlainText, this.mHtml, this.mFirstImgPath.toString());
                if (this.mHtml.length() > 0) {
                    Result = true;
                } else {
                    Result = false;
                }
                break;
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public void clearData() {
        this.mHtml = "";
        this.mPlainText = "";
        this.mFirstImg = null;
        this.mFirstImgPath = "";
    }

    public boolean setHtml(CharSequence text) {
        return setHtmlInternal(this.mPlainText, text);
    }

    public boolean setHtmlInternal(CharSequence text, CharSequence html) {
        if (html == null || html.toString().length() == 0) {
            return false;
        }
        if (html.length() > 131072) {
            html = html.subSequence(0, 131072);
        }
        this.mHtml = html.toString();
        if (ClipboardConstants.INFO_DEBUG) {
            Log.d(TAG, this.mHtml);
        }
        if (text == null || text.toString().length() <= 0) {
            this.mPlainText = this.mHtml.replaceAll(regex2, "");
            this.mPlainText = Html.fromHtml(this.mPlainText).toString();
        } else {
            this.mPlainText = text.toString();
        }
        if (ClipboardConstants.INFO_DEBUG) {
            Log.d(TAG, this.mPlainText.toString());
        }
        if (this.mFirstImg != null) {
            this.mFirstImg = null;
        }
        return true;
    }

    public boolean setHtmlWithImagePathInternal(CharSequence text, CharSequence html, CharSequence filePath) {
        if (text != null && text.toString().length() > 0) {
            this.mPlainText = text.toString();
        }
        return setHtmlWithImagePath(html, filePath);
    }

    public boolean setHtmlWithImagePath(CharSequence text, CharSequence filePath) {
        if (!setHtmlInternal(this.mPlainText, text)) {
            return false;
        }
        if (filePath != null && filePath.length() >= 1) {
            this.mFirstImgPath = filePath.toString();
            if (new File(filePath.toString()).isFile()) {
                if (ClipboardConstants.DEBUG) {
                    Log.i(TAG, "setHtmlWithImagePath : value is GOOD file path.");
                }
                return true;
            } else if (!ClipboardConstants.DEBUG) {
                return false;
            } else {
                Log.e(TAG, "setHtmlWithImagePath : value is no file path ..check plz");
                return false;
            }
        } else if (!ClipboardConstants.DEBUG) {
            return false;
        } else {
            Log.i(TAG, "filePath is null");
            return false;
        }
    }

    public String getHtml() {
        return this.mHtml;
    }

    public Bitmap getFirstImage(int reqWidth, int reqHeight) {
        if (this.mFirstImg != null) {
            return this.mFirstImg;
        }
        Bitmap Result = null;
        if (this.mHtml.length() >= 1) {
            String sFileName = "";
            try {
                sFileName = Html.fromHtml(Uri.decode(ClipboardProcText.getImgFileNameFormHtml(this.mHtml.toString()))).toString();
            } catch (Exception e) {
                if (ClipboardConstants.DEBUG) {
                    Log.e(TAG, "getFirstImage : " + e.getMessage());
                }
            }
            if (sFileName == null || sFileName.length() >= 1) {
                if (sFileName == null || sFileName.length() <= 7 || sFileName.substring(0, 7).compareTo("http://") != 0) {
                    if (sFileName == null || sFileName.length() <= 7 || sFileName.substring(0, 7).compareTo("file://") != 0) {
                        Result = ClipboardDataBitmapUrl.getFilePathBitmap(sFileName, reqHeight, reqHeight);
                    } else {
                        Result = ClipboardDataBitmapUrl.getFilePathBitmap(sFileName.substring(7, sFileName.length()), reqWidth, reqHeight);
                    }
                }
                this.mFirstImg = Result;
                return Result;
            } else if (!ClipboardConstants.DEBUG) {
                return null;
            } else {
                Log.w(TAG, "getFirstImage : FileName is empty.");
                return null;
            }
        } else if (!ClipboardConstants.DEBUG) {
            return null;
        } else {
            Log.w(TAG, "getFirstImage : Data is empty.");
            return null;
        }
    }

    public String getPlainText() {
        return this.mPlainText;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "html equals");
        }
        if (!super.equals(o) || !(o instanceof ClipboardDataHtml)) {
            return false;
        }
        if (this.mHtml.compareTo(((ClipboardDataHtml) o).getHtml()) == 0) {
            return true;
        }
        return false;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "html write to parcel");
        }
        super.writeToParcel(dest, flags);
        if (this.mClipdata == null) {
            this.mClipdata = new ClipData(null, new String[]{"text/html"}, new Item(this.mPlainText, this.mHtml, null, Uri.fromFile(new File(this.mFirstImgPath))));
        }
        dest.writeValue(this.mHtml);
        dest.writeValue(this.mPlainText);
        dest.writeValue(this.mFirstImgPath);
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
    }

    protected void readFromSource(Parcel source) {
        try {
            this.mHtml = (String) source.readValue(String.class.getClassLoader());
            this.mPlainText = (String) source.readValue(String.class.getClassLoader());
            this.mFirstImgPath = (String) source.readValue(String.class.getClassLoader());
            this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
            this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
            if (ClipboardConstants.INFO_DEBUG) {
                Log.i(TAG, "ClipboardDataHtml : readFromSource : " + this.mHtml);
            }
        } catch (Exception e) {
            Log.i(TAG, "readFromSource~Exception :" + e.getMessage());
        }
    }

    public String toString() {
        return "this Html class. Value is " + (this.mHtml.length() > 20 ? this.mHtml.subSequence(0, 20) : this.mHtml);
    }

    public boolean setFirstImgPath(String FilePath) {
        if (FilePath == null || FilePath.length() < 1) {
            return false;
        }
        this.mFirstImgPath = FilePath;
        if (new File(FilePath).isFile()) {
            return true;
        }
        if (ClipboardConstants.DEBUG) {
            Log.e(TAG, "ClipboardDataHtml : value is no file path ..check plz");
        }
        return false;
    }

    public String getFirstImgPath() {
        return this.mFirstImgPath;
    }

    protected void readFormSource(Parcel source) {
    }
}
