package android.sec.clipboard;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.list.ClipboardDataBitmap;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.sec.clipboard.data.list.ClipboardDataIntent;
import android.sec.clipboard.data.list.ClipboardDataMultipleType;
import android.sec.clipboard.data.list.ClipboardDataText;
import android.sec.clipboard.data.list.ClipboardDataUri;
import android.sec.clipboard.data.list.ClipboardDataUriList;
import android.sec.clipboard.util.FileHelper;
import android.util.Log;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class ClipboardConverter {
    private static String TAG = "ClipboardConverter";
    private static ClipboardConverter sInstance;

    private static class ImageFileFilter implements FileFilter {
        private final String[] extensions;
        FileHelper mFileHelper;

        private ImageFileFilter() {
            this.mFileHelper = FileHelper.getInstance();
            this.extensions = new String[]{"jpg", "png", "gif", "jpeg"};
        }

        public boolean accept(File file) {
            if (file == null) {
                return false;
            }
            for (String extension : this.extensions) {
                if (file.getName().toLowerCase().endsWith(extension) && !this.mFileHelper.checkFile(file)) {
                    return true;
                }
            }
            return false;
        }
    }

    private ClipboardConverter() {
    }

    public ClipData ClipboardDataToClipData(ClipboardData data) {
        return null;
    }

    public ClipboardData ClipDataToClipboardData(ClipData data) {
        if (data == null || data.getItemAt(0) == null) {
            return null;
        }
        int formatID = getFormatID(data);
        switch (formatID) {
            case 2:
                ClipboardData textdata = new ClipboardDataText();
                if (data.getItemAt(0).getText() != null) {
                    textdata.setText(data.getItemAt(0).getText());
                }
                textdata.setClipdata(data);
                return textdata;
            case 3:
                ClipboardData bitmapData = new ClipboardDataBitmap();
                if (data.getItemAt(0).getUri().getPath() != null) {
                    bitmapData.setBitmapPath(data.getItemAt(0).getUri().getPath());
                }
                bitmapData.setClipdata(data);
                return bitmapData;
            case 4:
                ClipboardData Htmldata = new ClipboardDataHtml();
                if (data.getItemAt(0).getHtmlText() != null) {
                    Htmldata.setHtml(data.getItemAt(0).getHtmlText());
                }
                Htmldata.setClipdata(data);
                return Htmldata;
            case 5:
                ClipboardData uriData = new ClipboardDataUri();
                if (data.getItemAt(0).getUri() != null) {
                    uriData.setUri(data.getItemAt(0).getUri());
                }
                uriData.setClipdata(data);
                return uriData;
            case 6:
                ClipboardData intentData = new ClipboardDataIntent();
                if (data.getItemAt(0).getIntent() != null) {
                    intentData.setIntent(data.getItemAt(0).getIntent());
                }
                intentData.setClipdata(data);
                return intentData;
            case 7:
                ClipboardData uriList = new ClipboardDataUriList();
                ArrayList<Uri> list = getUriList(data);
                if (list != null) {
                    uriList.setUriList(list);
                }
                uriList.setClipdata(data);
                return uriList;
            case 8:
                ClipboardData multiType = new ClipboardDataMultipleType();
                if (data != null) {
                    multiType.setClipdata(data);
                }
                return multiType;
            default:
                Log.e(TAG, "default : " + formatID);
                return null;
        }
    }

    private ArrayList<Uri> getUriList(ClipData data) {
        ArrayList<Uri> uries = new ArrayList();
        int count = data.getItemCount();
        int uri_count = 0;
        for (int index = 0; index < count; index++) {
            if (data.getItemAt(index).getUri() != null) {
                uries.set(uri_count, data.getItemAt(index).getUri());
                uri_count++;
            }
        }
        return uries;
    }

    private int getFormatID(ClipData data) {
        int count = data.getItemCount();
        int text_count = 0;
        int html_count = 0;
        int uri_count = 0;
        int uri_image_count = 0;
        int intent_count = 0;
        for (int index = 0; index < count; index++) {
            if (data.getItemAt(index).getText() != null) {
                text_count++;
            }
            if (data.getItemAt(index).getHtmlText() != null) {
                html_count++;
            }
            if (data.getItemAt(index).getIntent() != null) {
                intent_count++;
            }
            if (data.getItemAt(index).getUri() != null) {
                uri_count++;
                if (isImagefile(data.getItemAt(index).getUri())) {
                    uri_image_count++;
                }
            }
        }
        if (count == 1) {
            if (html_count == 1) {
                return 4;
            }
            if (text_count == 1) {
                return 2;
            }
            if (uri_image_count == 1) {
                return 3;
            }
            if (intent_count == 1) {
                return 6;
            }
            if (uri_count == 1) {
                return 5;
            }
            return 0;
        } else if (uri_count <= 1 || count <= uri_count) {
            return 7;
        } else {
            return 8;
        }
    }

    private boolean isImagefile(Uri uri) {
        if (uri == null || !"file".equals(uri.getScheme())) {
            return false;
        }
        return new ImageFileFilter().accept(new File(uri.getPath()));
    }

    public static ClipboardConverter getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ClipboardConverter();
        }
        return sInstance;
    }
}
