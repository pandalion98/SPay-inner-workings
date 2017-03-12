package android.sec.clipboard.data;

import android.os.SystemProperties;

public class ClipboardDefine {
    public static final String CLIPBOARD_DRAGNDROP = "clipboarddragNdrop";
    public static final String CLIPBOARD_KNOX_ROOT_PATH = "/knox";
    public static final String CLIPBOARD_ROOT_PATH = "/data/clipboard";
    public static final String CLIPBOARD_SCRAP_ROOT_PATH = "/scrap";
    public static final String CLIPBOARD_TAG = "ClipboardServiceEx";
    public static final int CLIPBOARD_TYPE_NORMAL = 0;
    public static final int CLIPBOARD_TYPE_SCRAPBOOK = 1;
    public static boolean DEBUG = ClipboardConstants.DEBUG;
    public static String DEFAULT_PATH = "";
    public static final int FORMAT_ALL_ID = 1;
    public static final String FORMAT_BITMAP = "Bitmap";
    public static final int FORMAT_BITMAP_ID = 3;
    public static final String FORMAT_HTML_FLAGMENT = "HTMLFlagment";
    public static final int FORMAT_HTML_FLAGMENT_ID = 4;
    public static final String FORMAT_INTENT = "Intent";
    public static final int FORMAT_INTENT_ID = 6;
    public static final String FORMAT_MULTIPLE_TYPE = "MultipleType";
    public static final int FORMAT_MULTIPLE_TYPE_ID = 8;
    public static final String FORMAT_MULTIPLE_URI = "MultipleUri";
    public static final int FORMAT_MULTIPLE_URI_ID = 7;
    public static final int FORMAT_SMARTCLIP_BITMAP_ID = 10;
    public static final String FORMAT_SMARTCLIP_BITMAP_TYPE = "SmartClipBitmapType";
    public static final int FORMAT_SMARTCLIP_ID = 9;
    public static final String FORMAT_SMARTCLIP_TYPE = "SmartClipType";
    public static final String FORMAT_TEXT = "Text";
    public static final int FORMAT_TEXT_ID = 2;
    public static final String FORMAT_URI = "Uri";
    public static final int FORMAT_URI_ID = 5;
    public static final String HTML_PREVIEW_IMAGE_NAME = "previewhtemlclipboarditem";
    public static boolean INFO_DEBUG = ClipboardConstants.INFO_DEBUG;
    public static final String KNOX_PACKAGE_PREFIX = "sec_container_1.";
    public static final int LIMIT_STRING_LENGTH = 262144;
    public static final int MAX_CLIPDATA_COUNT = 10;
    public static final int MAX_DATA_COUNT = 20;
    public static final int SAFETY_STRING_LENGTH = 131072;
    public static final boolean SUPPORT_KNOX = ClipboardConstants.SUPPORT_KNOX;
    public static final boolean SUPPORT_SAVE_MODE = "americano".equals(SystemProperties.get("ro.build.scafe"));
    public static String THUMBNAIL_SUFFIX = ClipboardConstants.THUMBNAIL_SUFFIX;
    public static final String USER_ADDED = "ADDED";
    public static final String USER_REMOVED = "REMOVED";
    public static final String USER_SWITCHED = "SWITCHED";
}
