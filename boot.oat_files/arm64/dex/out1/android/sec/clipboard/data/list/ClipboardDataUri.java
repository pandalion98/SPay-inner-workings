package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.net.Uri;
import android.os.Parcel;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.util.Log;
import java.io.File;
import java.io.FileFilter;

public class ClipboardDataUri extends ClipboardData {
    private static final String TAG = "ClipboardDataUri";
    private static final long serialVersionUID = 1;
    private String mPreviewImgPath = "";
    private String mValue = "";

    private static class ImageFileFilter implements FileFilter {
        private final String[] extensions;

        private ImageFileFilter() {
            this.extensions = new String[]{"jpg", "png", "gif", "jpeg"};
        }

        public boolean accept(File file) {
            if (file == null) {
                return false;
            }
            for (String extension : this.extensions) {
                if (file.getName().toLowerCase().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }
    }

    public ClipboardDataUri() {
        super(5);
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mValue.length() < 1) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 5:
                if (!(altData instanceof ClipboardDataUri)) {
                    Result = false;
                    break;
                }
                ClipboardDataUri data = (ClipboardDataUri) altData;
                Result = data.setUri(Uri.parse(this.mValue));
                if (this.mPreviewImgPath.length() > 1) {
                    Result &= data.setPreviewImgPath(this.mPreviewImgPath);
                    break;
                }
                break;
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public void clearData() {
        this.mValue = "";
    }

    public boolean SetUri(Uri uri) {
        return setUri(uri);
    }

    public boolean setUri(Uri uri) {
        if (uri == null || uri.toString().length() == 0) {
            return false;
        }
        this.mValue = uri.toString();
        return true;
    }

    public Uri GetUri() {
        return getUri();
    }

    public Uri getUri() {
        return Uri.parse(this.mValue);
    }

    public boolean isValidData() {
        if (this.mValue == null) {
            return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "uri equals");
        }
        if (!super.equals(o) || !(o instanceof ClipboardDataUri)) {
            return false;
        }
        return this.mValue.toString().compareTo(((ClipboardDataUri) o).getUri().toString()) == 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "Uri write to parcel");
        }
        super.writeToParcel(dest, flags);
        if (this.mClipdata == null) {
            this.mClipdata = new ClipData(ClipboardConstants.MULTIWINDOW_DRAGNDROP, new String[]{"text/uri-list"}, new Item(Uri.parse(this.mValue)));
        }
        dest.writeValue(this.mValue);
        dest.writeValue(this.mPreviewImgPath);
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
    }

    protected void readFromSource(Parcel source) {
        this.mValue = (String) source.readValue(String.class.getClassLoader());
        this.mPreviewImgPath = (String) source.readValue(String.class.getClassLoader());
        this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
        this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
    }

    public String toString() {
        return "this Uri class. Value is " + (this.mValue.length() > 20 ? this.mValue.subSequence(0, 20) : this.mValue);
    }

    public boolean setPreviewImgPath(String FilePath) {
        if (ClipboardConstants.INFO_DEBUG) {
            Log.i(TAG, "setPreviewImgPath :" + FilePath);
        }
        boolean Result = false;
        if (FilePath == null || FilePath.length() < 1) {
            return 0;
        }
        if (new File(FilePath).isFile() && isImagefile()) {
            this.mPreviewImgPath = FilePath;
            Result = true;
        } else {
            this.mPreviewImgPath = "";
            if (ClipboardConstants.DEBUG) {
                Log.e(TAG, "ClipboardDataUri : value is no file path or not image file");
            }
        }
        return Result;
    }

    public String getPreviewImgPath() {
        return this.mPreviewImgPath;
    }

    public boolean isImagefile() {
        Uri uri = getUri();
        if (uri == null || !"file".equals(uri.getScheme())) {
            return false;
        }
        return new ImageFileFilter().accept(new File(uri.getPath()));
    }

    protected void readFormSource(Parcel source) {
    }
}
