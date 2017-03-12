package android.app;

import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.BrowserContract;
import android.provider.Downloads.Impl;
import android.provider.Downloads.Impl.RequestHeaders;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DownloadManager {
    public static final String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";
    public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
    public static final String ACTION_VIEW_DOWNLOADS = "android.intent.action.VIEW_DOWNLOADS";
    public static final String ACTION_VIEW_SEC_DOWNLOADS = "android.intent.action.VIEW_SEC_DOWNLOADS";
    public static final String COLUMN_ALLOW_WRITE = "allow_write";
    public static final String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";
    public static final String COLUMN_DD_CONTENT_SIZE = "dd_contentSize";
    public static final String COLUMN_DD_FILE_DESCRIPTION = "dd_description";
    public static final String COLUMN_DD_FILE_NAME = "dd_fileName";
    public static final String COLUMN_DD_PRIMARY_MIMETYPE = "dd_primaryMimeType";
    public static final String COLUMN_DD_VENDOR_NAME = "dd_vendor";
    public static final String COLUMN_DD_VERSION_NUMBER = "dd_majorVersion";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DOWNLOAD_METHOD = "downloadmethod";
    public static final String COLUMN_DOWNLOAD_STATE = "state";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";
    public static final String COLUMN_LOCAL_FILENAME = "local_filename";
    public static final String COLUMN_LOCAL_URI = "local_uri";
    public static final String COLUMN_MEDIAPROVIDER_URI = "mediaprovider_uri";
    public static final String COLUMN_MEDIA_TYPE = "media_type";
    public static final String COLUMN_RANGE_END = "range_end";
    public static final String COLUMN_RANGE_FIRSTCHUNK_END = "range_first_end";
    public static final String COLUMN_RANGE_START = "range_start";
    public static final String COLUMN_REASON = "reason";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_STORAGE_TYPE = "storage_type";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TOTAL_SIZE_BYTES = "total_size";
    public static final String COLUMN_URI = "uri";
    public static final int ERROR_BLOCKED = 1010;
    public static final int ERROR_CANNOT_RESUME = 1008;
    public static final int ERROR_DEVICE_NOT_FOUND = 1007;
    public static final int ERROR_FILE_ALREADY_EXISTS = 1009;
    public static final int ERROR_FILE_ERROR = 1001;
    public static final int ERROR_HTTP_DATA_ERROR = 1004;
    public static final int ERROR_INSUFFICIENT_SPACE = 1006;
    public static final int ERROR_TOO_MANY_REDIRECTS = 1005;
    public static final int ERROR_UNHANDLED_HTTP_CODE = 1002;
    public static final int ERROR_UNKNOWN = 1000;
    public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";
    public static final String EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS = "extra_click_download_ids";
    public static final String INTENT_EXTRAS_SORT_BY_SIZE = "android.app.DownloadManager.extra_sortBySize";
    private static final Set<String> LONG_COLUMNS = new HashSet(Arrays.asList(new String[]{"_id", COLUMN_TOTAL_SIZE_BYTES, "status", "reason", COLUMN_BYTES_DOWNLOADED_SO_FAR, COLUMN_LAST_MODIFIED_TIMESTAMP, "dd_contentSize", "state", "downloadmethod", COLUMN_STORAGE_TYPE}));
    private static final String NON_DOWNLOADMANAGER_DOWNLOAD = "non-dwnldmngr-download-dont-retry2download";
    public static final int PAUSED_BY_APP = 5;
    public static final int PAUSED_QUEUED_FOR_WIFI = 3;
    public static final int PAUSED_UNKNOWN = 4;
    public static final int PAUSED_WAITING_FOR_NETWORK = 2;
    public static final int PAUSED_WAITING_TO_RETRY = 1;
    private static final String[] SEC_COLUMNS = new String[]{"_id", "mediaprovider_uri", "title", "description", "uri", "media_type", COLUMN_TOTAL_SIZE_BYTES, COLUMN_LOCAL_URI, "status", "reason", COLUMN_BYTES_DOWNLOADED_SO_FAR, COLUMN_LAST_MODIFIED_TIMESTAMP, "dd_fileName", "dd_vendor", "dd_description", "dd_majorVersion", "dd_primaryMimeType", "dd_contentSize", "state", "downloadmethod", COLUMN_LOCAL_FILENAME, COLUMN_STORAGE_TYPE};
    private static final String[] SEC_UNDERLYING_COLUMNS = new String[]{"_id", "title", "status", "state", Impl.COLUMN_TOTAL_BYTES, Impl.COLUMN_CURRENT_BYTES, "_data", "description", "mimetype", Impl.COLUMN_LAST_MODIFICATION, Impl.COLUMN_VISIBILITY, "downloadmethod", "uri", Impl.COLUMN_DESTINATION, "dd_primaryMimeType", Impl.COLUMN_DD_SECONDARY_MIMETYPE1, Impl.COLUMN_DD_SECONDARY_MIMETYPE2, "dd_fileName", "dd_vendor", "dd_description", "dd_contentSize", Impl.COLUMN_DD_OBJ_URL, Impl.COLUMN_DD_NOTIFY_URL, "dd_majorVersion", Impl.COLUMN_STORAGE_TYPE};
    public static final int STATUS_FAILED = 16;
    public static final int STATUS_OMA_PENDING = 65536;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_SUCCESSFUL = 8;
    public static final String[] UNDERLYING_COLUMNS = new String[]{"_id", "_data AS local_filename", "mediaprovider_uri", Impl.COLUMN_DESTINATION, "title", "description", "uri", "status", Impl.COLUMN_FILE_NAME_HINT, "mimetype AS media_type", "total_bytes AS total_size", "lastmod AS last_modified_timestamp", "current_bytes AS bytes_so_far", "allow_write", "dd_primaryMimeType", "dd_fileName", "dd_vendor", "dd_description", "dd_contentSize", Impl.COLUMN_DD_OBJ_URL, "dd_majorVersion", "range_start", "range_end", "range_first_end", "'placeholder' AS local_uri", "'placeholder' AS reason"};
    private Uri mBaseUri = Impl.CONTENT_URI;
    private String mPackageName;
    private ContentResolver mResolver;
    private Uri mSecBaseUri = Impl.CONTENT_CDURI;

    private static class CursorTranslator extends CursorWrapper {
        static final /* synthetic */ boolean $assertionsDisabled = (!DownloadManager.class.desiredAssertionStatus());
        private Uri mBaseUri;

        public CursorTranslator(Cursor cursor, Uri baseUri) {
            super(cursor);
            this.mBaseUri = baseUri;
        }

        public int getInt(int columnIndex) {
            return (int) getLong(columnIndex);
        }

        public long getLong(int columnIndex) {
            if (getColumnName(columnIndex).equals("reason")) {
                return getReason(super.getInt(getColumnIndex("status")));
            }
            if (getColumnName(columnIndex).equals("status")) {
                return (long) translateStatus(super.getInt(getColumnIndex("status")));
            }
            return super.getLong(columnIndex);
        }

        public String getString(int columnIndex) {
            return getColumnName(columnIndex).equals(DownloadManager.COLUMN_LOCAL_URI) ? getLocalUri() : super.getString(columnIndex);
        }

        private String getLocalUri() {
            long destinationType = getLong(getColumnIndex(Impl.COLUMN_DESTINATION));
            if (destinationType == 4 || destinationType == 0 || destinationType == 6) {
                String localPath = getString(getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                if (localPath == null) {
                    return null;
                }
                return Uri.fromFile(new File(localPath)).toString();
            }
            return ContentUris.withAppendedId(this.mBaseUri, getLong(getColumnIndex("_id"))).toString();
        }

        private long getReason(int status) {
            switch (translateStatus(status)) {
                case 4:
                    return getPausedReason(status);
                case 16:
                    return getErrorCode(status);
                default:
                    return 0;
            }
        }

        private long getPausedReason(int status) {
            switch (status) {
                case 193:
                    return 5;
                case 194:
                    return 1;
                case 195:
                    return 2;
                case 196:
                    return 3;
                default:
                    return 4;
            }
        }

        private long getErrorCode(int status) {
            if ((400 <= status && status < 488) || (500 <= status && status < 700)) {
                return (long) status;
            }
            switch (status) {
                case 198:
                    return 1006;
                case 199:
                    return 1007;
                case 488:
                    return 1009;
                case Impl.STATUS_CANNOT_RESUME /*489*/:
                    return 1008;
                case Impl.STATUS_FILE_ERROR /*492*/:
                    return 1001;
                case Impl.STATUS_UNHANDLED_REDIRECT /*493*/:
                case Impl.STATUS_UNHANDLED_HTTP_CODE /*494*/:
                    return 1002;
                case Impl.STATUS_HTTP_DATA_ERROR /*495*/:
                    return 1004;
                case Impl.STATUS_TOO_MANY_REDIRECTS /*497*/:
                    return 1005;
                default:
                    return 1000;
            }
        }

        private int translateStatus(int status) {
            switch (status) {
                case 181:
                case 183:
                case 184:
                case 185:
                case 186:
                case 188:
                case 192:
                case 201:
                    return 2;
                case 182:
                    return 65536;
                case 190:
                    return 1;
                case 193:
                case 194:
                case 195:
                case 196:
                    return 4;
                case 200:
                    return 8;
                default:
                    if ($assertionsDisabled || Impl.isStatusError(status)) {
                        return 16;
                    }
                    throw new AssertionError();
            }
        }
    }

    public static class Query {
        public static final int ORDER_ASCENDING = 1;
        public static final int ORDER_DESCENDING = 2;
        private long[] mIds = null;
        private boolean mOnlyIncludeVisibleInDownloadsUi = false;
        private String mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
        private int mOrderDirection = 2;
        private Integer mStatusFlags = null;

        public Query setFilterById(long... ids) {
            this.mIds = ids;
            return this;
        }

        public Query setFilterByStatus(int flags) {
            this.mStatusFlags = Integer.valueOf(flags);
            return this;
        }

        public Query setOnlyIncludeVisibleInDownloadsUi(boolean value) {
            this.mOnlyIncludeVisibleInDownloadsUi = value;
            return this;
        }

        public Query orderBy(String column, int direction) {
            if (direction == 1 || direction == 2) {
                if (column.equals(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)) {
                    this.mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
                } else if (column.equals(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) {
                    this.mOrderByColumn = Impl.COLUMN_TOTAL_BYTES;
                } else if (column.equals("title")) {
                    this.mOrderByColumn = "title COLLATE NOCASE";
                } else {
                    throw new IllegalArgumentException("Cannot order by " + column);
                }
                this.mOrderDirection = direction;
                return this;
            }
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        public Query orderByLocalized(String column, int direction) {
            if (direction == 1 || direction == 2) {
                if (column.equals(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)) {
                    this.mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
                } else if (column.equals(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) {
                    this.mOrderByColumn = Impl.COLUMN_TOTAL_BYTES;
                } else if (column.equals("title")) {
                    this.mOrderByColumn = "title COLLATE LOCALIZED";
                } else {
                    throw new IllegalArgumentException("Cannot order Localized " + column);
                }
                this.mOrderDirection = direction;
                return this;
            }
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        Cursor runQuery(ContentResolver resolver, String[] projection, Uri baseUri) {
            Uri uri = baseUri;
            List<String> selectionParts = new ArrayList();
            String[] selectionArgs = null;
            if (this.mIds != null) {
                selectionParts.add(DownloadManager.getWhereClauseForIds(this.mIds));
                selectionArgs = DownloadManager.getWhereArgsForIds(this.mIds);
            }
            if (this.mStatusFlags != null) {
                List<String> parts = new ArrayList();
                if ((this.mStatusFlags.intValue() & 1) != 0) {
                    parts.add(statusClause("=", 190));
                }
                if ((this.mStatusFlags.intValue() & 2) != 0) {
                    parts.add(statusClause("=", 192));
                }
                if ((this.mStatusFlags.intValue() & 4) != 0) {
                    parts.add(statusClause("=", 193));
                    parts.add(statusClause("=", 194));
                    parts.add(statusClause("=", 195));
                    parts.add(statusClause("=", 196));
                }
                if ((this.mStatusFlags.intValue() & 8) != 0) {
                    parts.add(statusClause("=", 200));
                }
                if ((this.mStatusFlags.intValue() & 16) != 0) {
                    parts.add("(" + statusClause(">=", 400) + " AND " + statusClause("<", CalendarColumns.CAL_ACCESS_EDITOR) + ")");
                }
                selectionParts.add(joinStrings(" OR ", parts));
            }
            if (this.mOnlyIncludeVisibleInDownloadsUi) {
                selectionParts.add("is_visible_in_downloads_ui != '0'");
            }
            selectionParts.add("deleted != '1'");
            return resolver.query(uri, projection, joinStrings(" AND ", selectionParts), selectionArgs, this.mOrderByColumn + " " + (this.mOrderDirection == 1 ? "ASC" : "DESC"));
        }

        private String joinStrings(String joiner, Iterable<String> parts) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (String part : parts) {
                if (!first) {
                    builder.append(joiner);
                }
                builder.append(part);
                first = false;
            }
            return builder.toString();
        }

        private String statusClause(String operator, int value) {
            return "status" + operator + "'" + value + "'";
        }
    }

    public static class Request {
        static final /* synthetic */ boolean $assertionsDisabled = (!DownloadManager.class.desiredAssertionStatus());
        public static final int NETWORK_BLUETOOTH = 4;
        public static final int NETWORK_ETHERNET = 512;
        public static final int NETWORK_MOBILE = 1;
        public static final int NETWORK_WIFI = 2;
        private static final int SCANNABLE_VALUE_NO = 2;
        private static final int SCANNABLE_VALUE_YES = 0;
        public static final int VISIBILITY_HIDDEN = 2;
        public static final int VISIBILITY_VISIBLE = 0;
        public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
        public static final int VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3;
        private int mAllowedNetworkTypes = -1;
        private CharSequence mDescription;
        private Uri mDestinationUri;
        private boolean mIsVisibleInDownloadsUi = true;
        private boolean mMeteredAllowed = true;
        private String mMimeType;
        private int mNotificationVisibility = 0;
        private List<Pair<String, String>> mRequestHeaders = new ArrayList();
        private boolean mRoamingAllowed = true;
        private boolean mScannable = false;
        private int mStorageType = 0;
        private CharSequence mTitle;
        private Uri mUri;
        private boolean mUseSystemCache = false;

        public Request(Uri uri) {
            if (uri == null) {
                throw new NullPointerException();
            }
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equals(IntentFilter.SCHEME_HTTP) || scheme.equals(IntentFilter.SCHEME_HTTPS))) {
                throw new IllegalArgumentException("Can only download HTTP/HTTPS URIs: " + uri);
            }
            this.mUri = uri;
        }

        Request(String uriString) {
            this.mUri = Uri.parse(uriString);
        }

        public Request setDestinationUri(Uri uri) {
            this.mDestinationUri = uri;
            return this;
        }

        public Request setDestinationToSystemCache() {
            this.mUseSystemCache = true;
            return this;
        }

        public Request setDestinationInExternalFilesDir(Context context, String dirType, String subPath) {
            File file = context.getExternalFilesDir(dirType);
            if (file == null) {
                throw new IllegalStateException("Failed to get external storage files directory");
            }
            if (file.exists()) {
                if (!file.isDirectory()) {
                    throw new IllegalStateException(file.getAbsolutePath() + " already exists and is not a directory");
                }
            } else if (!file.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: " + file.getAbsolutePath());
            }
            setDestinationFromBase(file, subPath);
            return this;
        }

        public Request setDestinationInExternalPublicDir(String dirType, String subPath) {
            File file = Environment.getExternalStoragePublicDirectory(dirType);
            if (file == null) {
                throw new IllegalStateException("Failed to get external storage public directory");
            }
            if (file.exists()) {
                if (!file.isDirectory()) {
                    throw new IllegalStateException(file.getAbsolutePath() + " already exists and is not a directory");
                }
            } else if (!file.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: " + file.getAbsolutePath());
            }
            setDestinationFromBase(file, subPath);
            return this;
        }

        private void setDestinationFromBase(File base, String subPath) {
            if (subPath == null) {
                throw new NullPointerException("subPath cannot be null");
            }
            this.mDestinationUri = Uri.withAppendedPath(Uri.fromFile(base), subPath);
        }

        public void allowScanningByMediaScanner() {
            this.mScannable = true;
        }

        public Request addRequestHeader(String header, String value) {
            if (header == null) {
                throw new NullPointerException("header cannot be null");
            } else if (header.contains(":")) {
                throw new IllegalArgumentException("header may not contain ':'");
            } else {
                if (value == null) {
                    value = ProxyInfo.LOCAL_EXCL_LIST;
                }
                this.mRequestHeaders.add(Pair.create(header, value));
                return this;
            }
        }

        public Request setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Request setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Request setMimeType(String mimeType) {
            this.mMimeType = mimeType;
            return this;
        }

        public Request setStorageType(int storageType) {
            this.mStorageType = storageType;
            return this;
        }

        @Deprecated
        public Request setShowRunningNotification(boolean show) {
            return show ? setNotificationVisibility(0) : setNotificationVisibility(2);
        }

        public Request setNotificationVisibility(int visibility) {
            this.mNotificationVisibility = visibility;
            return this;
        }

        public Request setAllowedNetworkTypes(int flags) {
            this.mAllowedNetworkTypes = flags;
            return this;
        }

        public Request setAllowedOverRoaming(boolean allowed) {
            this.mRoamingAllowed = allowed;
            return this;
        }

        public Request setAllowedOverMetered(boolean allow) {
            this.mMeteredAllowed = allow;
            return this;
        }

        public Request setVisibleInDownloadsUi(boolean isVisible) {
            this.mIsVisibleInDownloadsUi = isVisible;
            return this;
        }

        ContentValues toContentValues(String packageName) {
            int i = 2;
            ContentValues values = new ContentValues();
            if ($assertionsDisabled || this.mUri != null) {
                values.put("uri", this.mUri.toString());
                values.put(Impl.COLUMN_IS_PUBLIC_API, Boolean.valueOf(true));
                values.put(Impl.COLUMN_NOTIFICATION_PACKAGE, packageName);
                if (this.mDestinationUri != null) {
                    values.put(Impl.COLUMN_DESTINATION, Integer.valueOf(4));
                    values.put(Impl.COLUMN_FILE_NAME_HINT, this.mDestinationUri.toString());
                } else {
                    values.put(Impl.COLUMN_DESTINATION, Integer.valueOf(this.mUseSystemCache ? 5 : 2));
                }
                String str = Impl.COLUMN_MEDIA_SCANNED;
                if (this.mScannable) {
                    i = 0;
                }
                values.put(str, Integer.valueOf(i));
                if (!this.mRequestHeaders.isEmpty()) {
                    encodeHttpHeaders(values);
                }
                putIfNonNull(values, "title", this.mTitle);
                putIfNonNull(values, "description", this.mDescription);
                putIfNonNull(values, "mimetype", this.mMimeType);
                values.put(Impl.COLUMN_VISIBILITY, Integer.valueOf(this.mNotificationVisibility));
                values.put(Impl.COLUMN_ALLOWED_NETWORK_TYPES, Integer.valueOf(this.mAllowedNetworkTypes));
                values.put(Impl.COLUMN_ALLOW_ROAMING, Boolean.valueOf(this.mRoamingAllowed));
                values.put("allow_metered", Boolean.valueOf(this.mMeteredAllowed));
                values.put(Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI, Boolean.valueOf(this.mIsVisibleInDownloadsUi));
                return values;
            }
            throw new AssertionError();
        }

        ContentValues sectoContentValues(String packageName) {
            ContentValues values = new ContentValues();
            if ($assertionsDisabled || this.mUri != null) {
                values.put("uri", this.mUri.toString());
                values.put(Impl.COLUMN_IS_PUBLIC_API, Boolean.valueOf(true));
                values.put(Impl.COLUMN_NOTIFICATION_PACKAGE, packageName);
                if (this.mDestinationUri != null) {
                    values.put(Impl.COLUMN_FILE_NAME_HINT, this.mDestinationUri.toString());
                }
                values.put(Impl.COLUMN_MEDIA_SCANNED, Integer.valueOf(this.mScannable ? 0 : 2));
                if (!this.mRequestHeaders.isEmpty()) {
                    encodeHttpHeaders(values);
                }
                putIfNonNull(values, "title", this.mTitle);
                putIfNonNull(values, "description", this.mDescription);
                putIfNonNull(values, "mimetype", this.mMimeType);
                values.put(Impl.COLUMN_VISIBILITY, Integer.valueOf(this.mNotificationVisibility));
                values.put(Impl.COLUMN_ALLOWED_NETWORK_TYPES, Integer.valueOf(this.mAllowedNetworkTypes));
                values.put(Impl.COLUMN_ALLOW_ROAMING, Boolean.valueOf(this.mRoamingAllowed));
                values.put("allow_metered", Boolean.valueOf(this.mMeteredAllowed));
                values.put(Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI, Boolean.valueOf(this.mIsVisibleInDownloadsUi));
                values.put(Impl.COLUMN_STORAGE_TYPE, Integer.valueOf(this.mStorageType));
                return values;
            }
            throw new AssertionError();
        }

        private void encodeHttpHeaders(ContentValues values) {
            int index = 0;
            for (Pair<String, String> header : this.mRequestHeaders) {
                values.put(RequestHeaders.INSERT_KEY_PREFIX + index, ((String) header.first) + ": " + ((String) header.second));
                index++;
            }
        }

        private void putIfNonNull(ContentValues contentValues, String key, Object value) {
            if (value != null) {
                contentValues.put(key, value.toString());
            }
        }
    }

    private static class SecCursorTranslator extends CursorWrapper {
        static final /* synthetic */ boolean $assertionsDisabled = (!DownloadManager.class.desiredAssertionStatus());
        private Uri mBaseUri;

        public SecCursorTranslator(Cursor cursor, Uri baseUri) {
            super(cursor);
            this.mBaseUri = baseUri;
        }

        public int getColumnIndex(String columnName) {
            return Arrays.asList(DownloadManager.SEC_COLUMNS).indexOf(columnName);
        }

        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            int index = getColumnIndex(columnName);
            if (index != -1) {
                return index;
            }
            throw new IllegalArgumentException("No such column: " + columnName);
        }

        public String getColumnName(int columnIndex) {
            int numColumns = DownloadManager.SEC_COLUMNS.length;
            if (columnIndex >= 0 && columnIndex < numColumns) {
                return DownloadManager.SEC_COLUMNS[columnIndex];
            }
            throw new IllegalArgumentException("Invalid column index " + columnIndex + ", " + numColumns + " columns exist");
        }

        public String[] getColumnNames() {
            String[] returnColumns = new String[DownloadManager.SEC_COLUMNS.length];
            System.arraycopy(DownloadManager.SEC_COLUMNS, 0, returnColumns, 0, DownloadManager.SEC_COLUMNS.length);
            return returnColumns;
        }

        public int getColumnCount() {
            return DownloadManager.SEC_COLUMNS.length;
        }

        public byte[] getBlob(int columnIndex) {
            throw new UnsupportedOperationException();
        }

        public double getDouble(int columnIndex) {
            return (double) getLong(columnIndex);
        }

        private boolean isLongColumn(String column) {
            return DownloadManager.LONG_COLUMNS.contains(column);
        }

        public float getFloat(int columnIndex) {
            return (float) getDouble(columnIndex);
        }

        public int getInt(int columnIndex) {
            return (int) getLong(columnIndex);
        }

        public long getLong(int columnIndex) {
            return translateLong(getColumnName(columnIndex));
        }

        public short getShort(int columnIndex) {
            return (short) ((int) getLong(columnIndex));
        }

        public String getString(int columnIndex) {
            return translateString(getColumnName(columnIndex));
        }

        private String translateString(String column) {
            if (isLongColumn(column)) {
                return Long.toString(translateLong(column));
            }
            if (column.equals("title")) {
                return getUnderlyingString("title");
            }
            if (column.equals("description")) {
                return getUnderlyingString("description");
            }
            if (column.equals("uri")) {
                return getUnderlyingString("uri");
            }
            if (column.equals("media_type")) {
                return getUnderlyingString("mimetype");
            }
            if (column.equals(DownloadManager.COLUMN_LOCAL_FILENAME)) {
                return getUnderlyingString("_data");
            }
            if (column.equals("dd_fileName")) {
                return getUnderlyingString("dd_fileName");
            }
            if (column.equals("dd_vendor")) {
                return getUnderlyingString("dd_vendor");
            }
            if (column.equals("dd_majorVersion")) {
                return getUnderlyingString("dd_majorVersion");
            }
            if (column.equals("dd_primaryMimeType")) {
                return getUnderlyingString("dd_primaryMimeType");
            }
            if (column.equals("dd_description")) {
                return getUnderlyingString("dd_description");
            }
            if ($assertionsDisabled || column.equals(DownloadManager.COLUMN_LOCAL_URI)) {
                return getLocalUri();
            }
            throw new AssertionError();
        }

        private String getLocalUri() {
            long destinationType = getUnderlyingLong(Impl.COLUMN_DESTINATION);
            if (destinationType == 4) {
                return getUnderlyingString(Impl.COLUMN_FILE_NAME_HINT);
            }
            String localPath;
            if (destinationType == 0) {
                localPath = getUnderlyingString("_data");
                if (localPath != null) {
                    return Uri.fromFile(new File(localPath)).toString();
                }
                return null;
            } else if (destinationType == 6) {
                localPath = getString(getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                if (localPath != null) {
                    return Uri.fromFile(new File(localPath)).toString();
                }
                return null;
            } else {
                return ContentUris.withAppendedId(this.mBaseUri, getUnderlyingLong("_id")).toString();
            }
        }

        private long translateLong(String column) {
            if (!isLongColumn(column)) {
                return Long.valueOf(translateString(column)).longValue();
            }
            if (column.equals("_id")) {
                return getUnderlyingLong("_id");
            }
            if (column.equals(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) {
                return getUnderlyingLong(Impl.COLUMN_TOTAL_BYTES);
            }
            if (column.equals("status")) {
                return (long) translateStatus((int) getUnderlyingLong("status"));
            }
            if (column.equals("reason")) {
                return getReason((int) getUnderlyingLong("status"));
            }
            if (column.equals(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)) {
                return getUnderlyingLong(Impl.COLUMN_CURRENT_BYTES);
            }
            if (column.equals("dd_contentSize")) {
                return getUnderlyingLong("dd_contentSize");
            }
            if (column.equals("downloadmethod")) {
                return getUnderlyingLong("downloadmethod");
            }
            if (column.equals("state")) {
                return getUnderlyingLong("state");
            }
            if (column.equals(DownloadManager.COLUMN_STORAGE_TYPE)) {
                return getUnderlyingLong(Impl.COLUMN_STORAGE_TYPE);
            }
            if (column.equals("range_start")) {
                return getUnderlyingLong("range_start");
            }
            if (column.equals("range_end")) {
                return getUnderlyingLong("range_end");
            }
            if (column.equals("range_first_end")) {
                return getUnderlyingLong("range_first_end");
            }
            if ($assertionsDisabled || column.equals(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)) {
                return getUnderlyingLong(Impl.COLUMN_LAST_MODIFICATION);
            }
            throw new AssertionError();
        }

        public long getReason(int status) {
            switch (translateStatus(status)) {
                case 4:
                    return getPausedReason(status);
                case 16:
                    return getErrorCode(status);
                default:
                    return 0;
            }
        }

        private long getPausedReason(int status) {
            switch (status) {
                case 194:
                    return 1;
                case 195:
                    return 2;
                case 196:
                    return 3;
                default:
                    return 4;
            }
        }

        private long getErrorCode(int status) {
            if ((400 <= status && status < 488) || (500 <= status && status < 700)) {
                return (long) status;
            }
            switch (status) {
                case 198:
                    return 1006;
                case 199:
                    return 1007;
                case 488:
                    return 1009;
                case Impl.STATUS_CANNOT_RESUME /*489*/:
                    return 1008;
                default:
                    return (long) status;
            }
        }

        private long getUnderlyingLong(String column) {
            return super.getLong(super.getColumnIndex(column));
        }

        private String getUnderlyingString(String column) {
            return super.getString(super.getColumnIndex(column));
        }

        public int translateStatus(int status) {
            switch (status) {
                case 181:
                case 183:
                case 184:
                case 185:
                case 186:
                case 188:
                case 192:
                case 201:
                    return 2;
                case 182:
                    return 65536;
                case 190:
                    return 1;
                case 193:
                case 194:
                case 195:
                case 196:
                    return 4;
                case 200:
                case 202:
                case 203:
                    return 8;
                default:
                    return 16;
            }
        }
    }

    public static class SecQuery {
        public static final int ORDER_ASCENDING = 1;
        public static final int ORDER_DESCENDING = 2;
        private long[] mIds = null;
        private String mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
        private int mOrderDirection = 2;
        private Integer mStatusFlags = null;

        public SecQuery setFilterById(long... ids) {
            this.mIds = ids;
            return this;
        }

        public SecQuery setFilterByStatus(int flags) {
            this.mStatusFlags = Integer.valueOf(flags);
            return this;
        }

        public SecQuery orderBy(String column, int direction) {
            if (direction == 1 || direction == 2) {
                if (column.equals(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)) {
                    this.mOrderByColumn = Impl.COLUMN_LAST_MODIFICATION;
                } else if (column.equals(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) {
                    this.mOrderByColumn = Impl.COLUMN_TOTAL_BYTES;
                } else if (column.equals("title")) {
                    this.mOrderByColumn = "title COLLATE NOCASE";
                } else {
                    throw new IllegalArgumentException("Cannot order by " + column);
                }
                this.mOrderDirection = direction;
                return this;
            }
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        Cursor runQuery(ContentResolver resolver, String[] projection, Uri baseUri) {
            Uri uri = baseUri;
            List<String> selectionParts = new ArrayList();
            String[] selectionArgs = null;
            if (this.mIds != null) {
                selectionParts.add(DownloadManager.getWhereClauseForIds(this.mIds));
                selectionArgs = DownloadManager.getWhereArgsForIds(this.mIds);
            }
            return resolver.query(uri, projection, joinStrings(" AND ", selectionParts), selectionArgs, this.mOrderByColumn + " " + (this.mOrderDirection == 1 ? "ASC" : "DESC"));
        }

        private String joinStrings(String joiner, Iterable<String> parts) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (String part : parts) {
                if (!first) {
                    builder.append(joiner);
                }
                builder.append(part);
                first = false;
            }
            return builder.toString();
        }

        private String statusClause(String operator, int value) {
            return "status" + operator + "'" + value + "'";
        }
    }

    public DownloadManager(ContentResolver resolver, String packageName) {
        this.mResolver = resolver;
        this.mPackageName = packageName;
    }

    public void setAccessAllDownloads(boolean accessAllDownloads) {
        if (accessAllDownloads) {
            this.mBaseUri = Impl.ALL_DOWNLOADS_CONTENT_URI;
        } else {
            this.mBaseUri = Impl.CONTENT_URI;
        }
    }

    public void setSecDownloads(boolean accessAllDownloads) {
        if (accessAllDownloads) {
            this.mBaseUri = Impl.CONTENT_CDURI;
        }
    }

    public long enqueue(Request request) {
        Uri downloadUri = this.mResolver.insert(Impl.CONTENT_URI, request.toContentValues(this.mPackageName));
        if (downloadUri != null) {
            return Long.parseLong(downloadUri.getLastPathSegment());
        }
        return 0;
    }

    public long secenqueue(Request request) {
        ContentValues values = request.sectoContentValues(this.mPackageName);
        values.put(Impl.COLUMN_NOTIFICATION_PACKAGE, BrowserContract.AUTHORITY);
        values.put(Impl.COLUMN_NOTIFICATION_CLASS, "com.android.browser.OpenDownloadReceiver");
        Uri downloadUri = this.mResolver.insert(Impl.CONTENT_CDURI, values);
        if (downloadUri != null) {
            return Long.parseLong(downloadUri.getLastPathSegment());
        }
        return 0;
    }

    public int markRowDeleted(long... ids) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("input param 'ids' can't be null");
        }
        ContentValues values = new ContentValues();
        values.put("deleted", Integer.valueOf(1));
        if (ids.length == 1) {
            return this.mResolver.update(ContentUris.withAppendedId(this.mBaseUri, ids[0]), values, null, null);
        }
        return this.mResolver.update(this.mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
    }

    public int remove(long... ids) {
        return markRowDeleted(ids);
    }

    public int secmarkRowDeleted(long... ids) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("input param 'ids' can't be null");
        }
        ContentValues values = new ContentValues();
        values.put("deleted", Integer.valueOf(1));
        return this.mResolver.update(ContentUris.withAppendedId(Impl.CONTENT_CDURI, ids[0]), values, null, null);
    }

    private String joinStrings(String joiner, Iterable<String> parts) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String part : parts) {
            if (!first) {
                builder.append(joiner);
            }
            builder.append(part);
            first = false;
        }
        return builder.toString();
    }

    public int secremove(long... ids) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("input param 'ids' can't be null");
        }
        List<String> selectionParts = new ArrayList();
        selectionParts.add(getWhereClauseForIds(ids));
        String[] selectionArgs = getWhereArgsForIds(ids);
        return this.mResolver.delete(this.mSecBaseUri, joinStrings(" AND ", selectionParts), selectionArgs);
    }

    public Cursor query(Query query) {
        Cursor underlyingCursor = query.runQuery(this.mResolver, UNDERLYING_COLUMNS, this.mBaseUri);
        if (underlyingCursor == null) {
            return null;
        }
        return new CursorTranslator(underlyingCursor, this.mBaseUri);
    }

    public Cursor secquery(SecQuery query) {
        Cursor underlyingCursor = query.runQuery(this.mResolver, SEC_UNDERLYING_COLUMNS, this.mSecBaseUri);
        if (underlyingCursor == null) {
            return null;
        }
        return new SecCursorTranslator(underlyingCursor, this.mSecBaseUri);
    }

    public ParcelFileDescriptor openDownloadedFile(long id) throws FileNotFoundException {
        return this.mResolver.openFileDescriptor(getDownloadUri(id), FullBackup.ROOT_TREE_TOKEN);
    }

    public Uri getUriForDownloadedFile(long id) {
        Uri uri = null;
        Cursor cursor = null;
        try {
            cursor = query(new Query().setFilterById(id));
            if (cursor != null) {
                if (cursor.moveToFirst() && 8 == cursor.getInt(cursor.getColumnIndexOrThrow("status"))) {
                    uri = ContentUris.withAppendedId(Impl.CONTENT_URI, id);
                    if (cursor != null) {
                        cursor.close();
                    }
                } else if (cursor != null) {
                    cursor.close();
                }
            }
            return uri;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getMimeTypeForDownloadedFile(long id) {
        String str = null;
        Cursor cursor = null;
        try {
            cursor = query(new Query().setFilterById(id));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    str = cursor.getString(cursor.getColumnIndexOrThrow("media_type"));
                    if (cursor != null) {
                        cursor.close();
                    }
                } else if (cursor != null) {
                    cursor.close();
                }
            }
            return str;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean restartDownload(long... ids) {
        Cursor cursor = query(new Query().setFilterById(ids));
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                if (status != 8 && status != 16) {
                    return false;
                }
                cursor.moveToNext();
            }
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(Impl.COLUMN_CURRENT_BYTES, Integer.valueOf(0));
            values.put(Impl.COLUMN_TOTAL_BYTES, Integer.valueOf(-1));
            values.putNull("_data");
            values.put("status", Integer.valueOf(190));
            values.put(Impl.COLUMN_FAILED_CONNECTIONS, Integer.valueOf(0));
            values.put("state", Integer.valueOf(0));
            values.put("range_start", Long.valueOf(0));
            values.put("range_end", Long.valueOf(0));
            values.put("range_first_end", Long.valueOf(0));
            this.mResolver.update(this.mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
            return true;
        } finally {
            cursor.close();
        }
    }

    public void pauseDownload(long id) {
        ContentValues values = new ContentValues();
        values.put(Impl.COLUMN_CONTROL, Integer.valueOf(1));
        this.mResolver.update(ContentUris.withAppendedId(this.mBaseUri, id), values, null, null);
    }

    public void resumeDownload(long id) {
        ContentValues values = new ContentValues();
        values.put(Impl.COLUMN_CONTROL, Integer.valueOf(0));
        this.mResolver.update(ContentUris.withAppendedId(this.mBaseUri, id), values, null, null);
    }

    public boolean secrestartDownload(long... ids) {
        Cursor cursor = secquery(new SecQuery().setFilterById(ids));
        if (cursor == null) {
            return false;
        }
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                if (status != 8 && status != 16) {
                    return false;
                }
                cursor.moveToNext();
            }
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(Impl.COLUMN_CURRENT_BYTES, Integer.valueOf(0));
            values.put(Impl.COLUMN_TOTAL_BYTES, Integer.valueOf(-1));
            values.putNull("_data");
            values.put("state", Integer.valueOf(0));
            values.put(Impl.COLUMN_VISIBILITY, Integer.valueOf(1));
            values.put("status", Integer.valueOf(190));
            this.mResolver.update(this.mSecBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
            return true;
        } finally {
            cursor.close();
        }
    }

    public static Long getMaxBytesOverMobile(Context context) {
        try {
            return Long.valueOf(Global.getLong(context.getContentResolver(), Global.DOWNLOAD_MAX_BYTES_OVER_MOBILE));
        } catch (SettingNotFoundException e) {
            return null;
        }
    }

    public static Long getRecommendedMaxBytesOverMobile(Context context) {
        try {
            return Long.valueOf(Global.getLong(context.getContentResolver(), Global.DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE));
        } catch (SettingNotFoundException e) {
            return null;
        }
    }

    public static boolean isActiveNetworkExpensive(Context context) {
        return false;
    }

    public static long getActiveNetworkWarningBytes(Context context) {
        return -1;
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification) {
        return addCompletedDownload(title, description, isMediaScannerScannable, mimeType, path, length, showNotification, false);
    }

    public long addCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification, boolean allowWrite) {
        validateArgumentIsNonEmpty("title", title);
        validateArgumentIsNonEmpty("description", description);
        validateArgumentIsNonEmpty("path", path);
        validateArgumentIsNonEmpty("mimeType", mimeType);
        if (length < 0) {
            throw new IllegalArgumentException(" invalid value for param: totalBytes");
        }
        ContentValues values = new Request(NON_DOWNLOADMANAGER_DOWNLOAD).setTitle(title).setDescription(description).setMimeType(mimeType).toContentValues(null);
        values.put(Impl.COLUMN_DESTINATION, Integer.valueOf(6));
        values.put("_data", path);
        values.put("status", Integer.valueOf(200));
        values.put("state", Integer.valueOf(10));
        values.put(Impl.COLUMN_TOTAL_BYTES, Long.valueOf(length));
        values.put(Impl.COLUMN_MEDIA_SCANNED, Integer.valueOf(isMediaScannerScannable ? 0 : 2));
        values.put(Impl.COLUMN_VISIBILITY, Integer.valueOf(showNotification ? 1 : 2));
        values.put("allow_write", Integer.valueOf(allowWrite ? 1 : 0));
        Uri downloadUri = this.mResolver.insert(Impl.CONTENT_URI, values);
        if (downloadUri == null) {
            return -1;
        }
        return Long.parseLong(downloadUri.getLastPathSegment());
    }

    public long secAddCompletedDownload(String title, String description, boolean isMediaScannerScannable, String mimeType, String path, long length, boolean showNotification) {
        validateArgumentIsNonEmpty("title", title);
        validateArgumentIsNonEmpty("description", description);
        validateArgumentIsNonEmpty("path", path);
        validateArgumentIsNonEmpty("mimeType", mimeType);
        if (length < 0) {
            throw new IllegalArgumentException(" invalid value for param: totalBytes");
        }
        ContentValues values = new Request(NON_DOWNLOADMANAGER_DOWNLOAD).setTitle(title).setDescription(description).setMimeType(mimeType).sectoContentValues(null);
        values.put(Impl.COLUMN_DESTINATION, Integer.valueOf(0));
        values.put("_data", path);
        values.put("status", Integer.valueOf(200));
        values.put("state", Integer.valueOf(10));
        values.put(Impl.COLUMN_STORAGE_TYPE, Integer.valueOf(1));
        values.put(Impl.COLUMN_TOTAL_BYTES, Long.valueOf(length));
        values.put(Impl.COLUMN_MEDIA_SCANNED, Integer.valueOf(isMediaScannerScannable ? 0 : 2));
        values.put(Impl.COLUMN_VISIBILITY, Integer.valueOf(showNotification ? 1 : 2));
        Uri downloadUri = this.mResolver.insert(Impl.CONTENT_CDURI, values);
        if (downloadUri == null) {
            return -1;
        }
        return Long.parseLong(downloadUri.getLastPathSegment());
    }

    private static void validateArgumentIsNonEmpty(String paramName, String val) {
        if (TextUtils.isEmpty(val)) {
            throw new IllegalArgumentException(paramName + " can't be null");
        }
    }

    public Uri getDownloadUri(long id) {
        return ContentUris.withAppendedId(this.mBaseUri, id);
    }

    static String getWhereClauseForIds(long[] ids) {
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("(");
        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                whereClause.append("OR ");
            }
            whereClause.append("_id");
            whereClause.append(" = ? ");
        }
        whereClause.append(")");
        return whereClause.toString();
    }

    static String[] getWhereArgsForIds(long[] ids) {
        String[] whereArgs = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            whereArgs[i] = Long.toString(ids[i]);
        }
        return whereArgs;
    }
}
