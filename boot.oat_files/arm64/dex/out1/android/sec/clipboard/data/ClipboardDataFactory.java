package android.sec.clipboard.data;

import android.sec.clipboard.data.list.ClipboardDataBitmap;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.sec.clipboard.data.list.ClipboardDataIntent;
import android.sec.clipboard.data.list.ClipboardDataMultipleType;
import android.sec.clipboard.data.list.ClipboardDataText;
import android.sec.clipboard.data.list.ClipboardDataUri;
import android.sec.clipboard.data.list.ClipboardDataUriList;

public class ClipboardDataFactory {
    public static ClipboardData CreateClipBoardData(int format) {
        switch (format) {
            case 2:
                return new ClipboardDataText();
            case 3:
                return new ClipboardDataBitmap();
            case 4:
                return new ClipboardDataHtml();
            case 5:
                return new ClipboardDataUri();
            case 6:
                return new ClipboardDataIntent();
            case 7:
                return new ClipboardDataUriList();
            case 8:
                return new ClipboardDataMultipleType();
            default:
                return null;
        }
    }
}
