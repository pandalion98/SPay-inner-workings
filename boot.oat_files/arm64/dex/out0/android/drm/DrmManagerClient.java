package android.drm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.drm.DrmStore.Action;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class DrmManagerClient {
    private static final int ACTION_PROCESS_DRM_INFO = 1002;
    private static final int ACTION_REMOVE_ALL_RIGHTS = 1001;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_UNKNOWN = -2000;
    public static final int INVALID_SESSION = -1;
    private static final String TAG = "DrmManagerClient";
    private static final boolean isLogEnabled = false;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private Context mContext;
    private EventHandler mEventHandler;
    HandlerThread mEventThread;
    private InfoHandler mInfoHandler;
    HandlerThread mInfoThread;
    private long mNativeContext;
    private OnErrorListener mOnErrorListener;
    private OnEventListener mOnEventListener;
    private OnInfoListener mOnInfoListener;
    private volatile boolean mReleased;
    private int mUniqueId;

    private class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            IOException e;
            DrmEvent event = null;
            DrmErrorEvent error = null;
            HashMap<String, Object> attributes = new HashMap();
            switch (msg.what) {
                case 1001:
                    if (DrmManagerClient.this._removeAllRights(DrmManagerClient.this.mUniqueId) != 0) {
                        error = new DrmErrorEvent(DrmManagerClient.this.mUniqueId, DrmErrorEvent.TYPE_REMOVE_ALL_RIGHTS_FAILED, null);
                        break;
                    } else {
                        event = new DrmEvent(DrmManagerClient.this.mUniqueId, 1001, null);
                        break;
                    }
                case 1002:
                    DrmInfoStatus status;
                    int infoType;
                    DrmInfo drmInfo = msg.obj;
                    String filepath = (String) drmInfo.get(DrmInfoRequest.DRM_PATH);
                    FileInputStream fis = null;
                    if (!(filepath == null || filepath.isEmpty())) {
                        try {
                            FileInputStream fis2 = new FileInputStream(filepath);
                            try {
                                drmInfo.put("FileDescriptorKey", fis2.getFD().toString());
                                fis = fis2;
                            } catch (IOException e2) {
                                e = e2;
                                fis = fis2;
                                Log.e(DrmManagerClient.TAG, "Exception the file " + e.toString());
                                status = DrmManagerClient.this._processDrmInfo(DrmManagerClient.this.mUniqueId, drmInfo);
                                attributes.put(DrmEvent.DRM_INFO_STATUS_OBJECT, status);
                                attributes.put(DrmEvent.DRM_INFO_OBJECT, drmInfo);
                                if (fis != null) {
                                    try {
                                        fis.close();
                                    } catch (IOException e3) {
                                    }
                                }
                                if (status == null) {
                                    break;
                                }
                                infoType = status != null ? status.infoType : drmInfo.getInfoType();
                                if (status != null) {
                                    break;
                                }
                                DrmManagerClient.this.mOnEventListener.onEvent(DrmManagerClient.this, event);
                                if (DrmManagerClient.this.mOnErrorListener != null) {
                                }
                                return;
                            }
                        } catch (IOException e4) {
                            e = e4;
                            Log.e(DrmManagerClient.TAG, "Exception the file " + e.toString());
                            status = DrmManagerClient.this._processDrmInfo(DrmManagerClient.this.mUniqueId, drmInfo);
                            attributes.put(DrmEvent.DRM_INFO_STATUS_OBJECT, status);
                            attributes.put(DrmEvent.DRM_INFO_OBJECT, drmInfo);
                            if (fis != null) {
                                fis.close();
                            }
                            if (status == null) {
                            }
                            if (status != null) {
                            }
                            if (status != null) {
                            }
                            DrmManagerClient.this.mOnEventListener.onEvent(DrmManagerClient.this, event);
                            if (DrmManagerClient.this.mOnErrorListener != null) {
                                return;
                            }
                        }
                    }
                    status = DrmManagerClient.this._processDrmInfo(DrmManagerClient.this.mUniqueId, drmInfo);
                    attributes.put(DrmEvent.DRM_INFO_STATUS_OBJECT, status);
                    attributes.put(DrmEvent.DRM_INFO_OBJECT, drmInfo);
                    if (fis != null) {
                        fis.close();
                    }
                    if (status == null && 1 == status.statusCode) {
                        event = new DrmEvent(DrmManagerClient.this.mUniqueId, DrmManagerClient.this.getEventType(status.infoType), null, attributes);
                    } else {
                        if (status != null) {
                        }
                        error = (status != null || status.data.getData() == null) ? new DrmErrorEvent(DrmManagerClient.this.mUniqueId, DrmManagerClient.this.getErrorType(infoType, status), null, attributes) : new DrmErrorEvent(DrmManagerClient.this.mUniqueId, DrmManagerClient.this.getErrorType(infoType, status), new String(status.data.getData(), StandardCharsets.UTF_8), attributes);
                    }
                    break;
                default:
                    Log.e(DrmManagerClient.TAG, "Unknown message type " + msg.what);
                    return;
            }
            if (!(DrmManagerClient.this.mOnEventListener == null || event == null)) {
                DrmManagerClient.this.mOnEventListener.onEvent(DrmManagerClient.this, event);
            }
            if (DrmManagerClient.this.mOnErrorListener != null && error != null) {
                DrmManagerClient.this.mOnErrorListener.onError(DrmManagerClient.this, error);
            }
        }
    }

    private class InfoHandler extends Handler {
        public static final int INFO_EVENT_TYPE = 1;

        public InfoHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            DrmInfoEvent info = null;
            DrmErrorEvent error = null;
            switch (msg.what) {
                case 1:
                    int uniqueId = msg.arg1;
                    int infoType = msg.arg2;
                    String message = msg.obj.toString();
                    switch (infoType) {
                        case 1:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            info = new DrmInfoEvent(uniqueId, infoType, message);
                            break;
                        case 2:
                            try {
                                DrmUtils.removeFile(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            info = new DrmInfoEvent(uniqueId, infoType, message);
                            break;
                        default:
                            error = new DrmErrorEvent(uniqueId, infoType, message);
                            break;
                    }
                    if (!(DrmManagerClient.this.mOnInfoListener == null || info == null)) {
                        DrmManagerClient.this.mOnInfoListener.onInfo(DrmManagerClient.this, info);
                    }
                    if (DrmManagerClient.this.mOnErrorListener != null && error != null) {
                        DrmManagerClient.this.mOnErrorListener.onError(DrmManagerClient.this, error);
                        return;
                    }
                    return;
                default:
                    Log.e(DrmManagerClient.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    public interface OnErrorListener {
        void onError(DrmManagerClient drmManagerClient, DrmErrorEvent drmErrorEvent);
    }

    public interface OnEventListener {
        void onEvent(DrmManagerClient drmManagerClient, DrmEvent drmEvent);
    }

    public interface OnInfoListener {
        void onInfo(DrmManagerClient drmManagerClient, DrmInfoEvent drmInfoEvent);
    }

    private native DrmInfo _acquireDrmInfo(int i, DrmInfoRequest drmInfoRequest);

    private native boolean _canHandle(int i, String str, String str2);

    private native int _checkRightsStatus(int i, String str, int i2);

    private native DrmConvertedStatus _closeConvertSession(int i, int i2);

    private native DrmConvertedStatus _convertData(int i, int i2, byte[] bArr);

    private native DrmSupportInfo[] _getAllSupportInfo(int i);

    private native ContentValues _getConstraints(int i, String str, int i2);

    private native int _getDrmObjectType(int i, String str, String str2);

    private native ContentValues _getMetadata(int i, String str);

    private native String _getOriginalMimeType(int i, String str, FileDescriptor fileDescriptor);

    private native int _initialize();

    private native void _installDrmEngine(int i, String str);

    private native int _openConvertSession(int i, String str);

    private native DrmInfoStatus _processDrmInfo(int i, DrmInfo drmInfo);

    private native void _release(int i);

    private native int _removeAllRights(int i);

    private native int _removeRights(int i, String str);

    private native int _saveRights(int i, DrmRights drmRights, String str, String str2);

    private native void _setListeners(int i, Object obj);

    static {
        System.loadLibrary("drmframework_jni");
    }

    public static void notify(Object thisReference, int uniqueId, int infoType, String message) {
        DrmManagerClient instance = (DrmManagerClient) ((WeakReference) thisReference).get();
        if (instance != null && instance.mInfoHandler != null) {
            instance.mInfoHandler.sendMessage(instance.mInfoHandler.obtainMessage(1, uniqueId, infoType, message));
        }
    }

    private int _checkFDSupporting(String path) {
        String[] OmaExtensions = new String[]{".dcf"};
        String[] PlayReadyExtensions = new String[]{".pyv", ".pya", ".wmv", ".wma", ".asf", ".eny", ".pye", ".ismv", ".isma", ".mp4", ".fdsa"};
        String[] DivxExtensions = new String[]{".avi", "divx"};
        if (path == null) {
            return 0;
        }
        for (String endsWith : OmaExtensions) {
            if (path.toLowerCase().endsWith(endsWith)) {
                return 1;
            }
        }
        for (String endsWith2 : PlayReadyExtensions) {
            if (path.toLowerCase().endsWith(endsWith2)) {
                return 2;
            }
        }
        for (String endsWith3 : DivxExtensions) {
            if (path.toLowerCase().endsWith(endsWith3)) {
                return 3;
            }
        }
        return 0;
    }

    public DrmManagerClient(Context context) {
        this.mContext = context;
        createEventThreads();
        this.mUniqueId = _initialize();
        this.mCloseGuard.open("release");
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            release();
        } finally {
            super.finalize();
        }
    }

    public void release() {
        if (!this.mReleased) {
            this.mReleased = true;
            if (this.mEventHandler != null) {
                this.mEventThread.quit();
                this.mEventThread = null;
            }
            if (this.mInfoHandler != null) {
                this.mInfoThread.quit();
                this.mInfoThread = null;
            }
            this.mEventHandler = null;
            this.mInfoHandler = null;
            this.mOnEventListener = null;
            this.mOnInfoListener = null;
            this.mOnErrorListener = null;
            _release(this.mUniqueId);
            this.mCloseGuard.close();
        }
    }

    public synchronized void setOnInfoListener(OnInfoListener infoListener) {
        this.mOnInfoListener = infoListener;
        if (infoListener != null) {
            createListeners();
        }
    }

    public synchronized void setOnEventListener(OnEventListener eventListener) {
        this.mOnEventListener = eventListener;
        if (eventListener != null) {
            createListeners();
        }
    }

    public synchronized void setOnErrorListener(OnErrorListener errorListener) {
        this.mOnErrorListener = errorListener;
        if (errorListener != null) {
            createListeners();
        }
    }

    public String[] getAvailableDrmEngines() {
        DrmSupportInfo[] supportInfos = _getAllSupportInfo(this.mUniqueId);
        ArrayList<String> descriptions = new ArrayList();
        for (DrmSupportInfo descriprition : supportInfos) {
            descriptions.add(descriprition.getDescriprition());
        }
        return (String[]) descriptions.toArray(new String[descriptions.size()]);
    }

    public ContentValues getConstraints(String path, int action) {
        IOException e;
        Throwable th;
        if (path == null || path.equals(ProxyInfo.LOCAL_EXCL_LIST) || !Action.isValid(action)) {
            throw new IllegalArgumentException("Given usage or path is invalid/null");
        } else if (_checkFDSupporting(path) == 0) {
            return _getConstraints(this.mUniqueId, path, action);
        } else {
            ContentValues contentValues = null;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(path);
                try {
                    contentValues = _getConstraints(this.mUniqueId, fis2.getFD().toString(), action);
                    if (fis2 != null) {
                        try {
                            fis2.close();
                            fis = fis2;
                            return contentValues;
                        } catch (IOException e2) {
                            fis = fis2;
                            return contentValues;
                        }
                    }
                    return contentValues;
                } catch (IOException e3) {
                    e = e3;
                    fis = fis2;
                    try {
                        Log.e(TAG, "Exception the file " + e.toString());
                        if (fis != null) {
                            return contentValues;
                        }
                        try {
                            fis.close();
                            return contentValues;
                        } catch (IOException e4) {
                            return contentValues;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                Log.e(TAG, "Exception the file " + e.toString());
                if (fis != null) {
                    return contentValues;
                }
                fis.close();
                return contentValues;
            }
        }
    }

    public ContentValues getMetadata(String path) {
        if (path != null && !path.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            return _getMetadata(this.mUniqueId, path);
        }
        throw new IllegalArgumentException("Given path is invalid/null");
    }

    public ContentValues getConstraints(Uri uri, int action) {
        if (uri != null && Uri.EMPTY != uri) {
            return getConstraints(convertUriToPath(uri), action);
        }
        throw new IllegalArgumentException("Uri should be non null");
    }

    public ContentValues getMetadata(Uri uri) {
        if (uri != null && Uri.EMPTY != uri) {
            return getMetadata(convertUriToPath(uri));
        }
        throw new IllegalArgumentException("Uri should be non null");
    }

    public int saveRights(DrmRights drmRights, String rightsPath, String contentPath) throws IOException {
        if (drmRights == null || !drmRights.isValid()) {
            throw new IllegalArgumentException("Given drmRights or contentPath is not valid");
        }
        if (!(rightsPath == null || rightsPath.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
            DrmUtils.writeToFile(rightsPath, drmRights.getData());
        }
        return _saveRights(this.mUniqueId, drmRights, rightsPath, contentPath);
    }

    public void installDrmEngine(String engineFilePath) {
        if (engineFilePath == null || engineFilePath.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            throw new IllegalArgumentException("Given engineFilePath: " + engineFilePath + "is not valid");
        }
        _installDrmEngine(this.mUniqueId, engineFilePath);
    }

    public boolean canHandle(String path, String mimeType) {
        IOException e;
        Throwable th;
        if ((path == null || path.equals(ProxyInfo.LOCAL_EXCL_LIST)) && (mimeType == null || mimeType.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
            throw new IllegalArgumentException("Path or the mimetype should be non null");
        } else if (path == null || _checkFDSupporting(path) == 0) {
            return _canHandle(this.mUniqueId, path, mimeType);
        } else {
            boolean result = false;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(path);
                try {
                    result = _canHandle(this.mUniqueId, fis2.getFD().toString(), mimeType);
                    if (fis2 != null) {
                        try {
                            fis2.close();
                            fis = fis2;
                            return result;
                        } catch (IOException e2) {
                            fis = fis2;
                            return result;
                        }
                    }
                    return result;
                } catch (IOException e3) {
                    e = e3;
                    fis = fis2;
                    try {
                        Log.e(TAG, "Exception the file " + e.toString());
                        if (fis != null) {
                            return result;
                        }
                        try {
                            fis.close();
                            return result;
                        } catch (IOException e4) {
                            return result;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                Log.e(TAG, "Exception the file " + e.toString());
                if (fis != null) {
                    return result;
                }
                fis.close();
                return result;
            }
        }
    }

    public boolean canHandle(Uri uri, String mimeType) {
        if ((uri != null && Uri.EMPTY != uri) || (mimeType != null && !mimeType.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
            return canHandle(convertUriToPath(uri), mimeType);
        }
        throw new IllegalArgumentException("Uri or the mimetype should be non null");
    }

    public int processDrmInfo(DrmInfo drmInfo) {
        if (drmInfo == null || !drmInfo.isValid()) {
            throw new IllegalArgumentException("Given drmInfo is invalid/null");
        } else if (this.mEventHandler == null) {
            return ERROR_UNKNOWN;
        } else {
            if (this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(1002, drmInfo))) {
                return 0;
            }
            return ERROR_UNKNOWN;
        }
    }

    public DrmInfo acquireDrmInfo(DrmInfoRequest drmInfoRequest) {
        FileInputStream fis;
        FileOutputStream fos;
        IOException e;
        DrmInfo lDrmInfo;
        if (drmInfoRequest == null || !drmInfoRequest.isValid()) {
            throw new IllegalArgumentException("Given drmInfoRequest is invalid/null");
        }
        File uf;
        String Filename;
        FileInputStream fis2 = null;
        FileOutputStream fos2 = null;
        String filepath = (String) drmInfoRequest.get(DrmInfoRequest.DRM_PATH);
        if (drmInfoRequest.getInfoType() == 7) {
            if (!(filepath == null || filepath.isEmpty())) {
                if (filepath.toLowerCase().endsWith(".dm")) {
                    try {
                        fis = new FileInputStream(filepath);
                        try {
                            drmInfoRequest.put("FileDescriptorKey", fis.getFD().toString());
                            fos = new FileOutputStream(filepath.replace(".dm", ".dcf"));
                        } catch (IOException e2) {
                            e = e2;
                            fis2 = fis;
                            Log.e(TAG, "Exception the file " + e.toString());
                            lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                            if (drmInfoRequest.getInfoType() != 7) {
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                                if (fis2 != null) {
                                    try {
                                        fis2.close();
                                    } catch (IOException e3) {
                                    }
                                }
                                if (fos2 != null) {
                                    try {
                                        fos2.close();
                                    } catch (IOException e4) {
                                    }
                                }
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                    Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                    uf = new File(filepath);
                                    Log.e(TAG, "DM file delete fail");
                                }
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                    if (!filepath.toLowerCase().endsWith(".dm")) {
                                        Filename = filepath.replace(".dm", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    } else if (filepath.toLowerCase().endsWith(".fl")) {
                                        Filename = filepath.replace(".fl", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    }
                                }
                            } else if (fis2 != null) {
                                try {
                                    fis2.close();
                                } catch (IOException e5) {
                                }
                            }
                            return lDrmInfo;
                        }
                        try {
                            drmInfoRequest.put("OutFileDescriptorKey", fos.getFD().toString());
                            fos2 = fos;
                            fis2 = fis;
                        } catch (IOException e6) {
                            e = e6;
                            fos2 = fos;
                            fis2 = fis;
                            Log.e(TAG, "Exception the file " + e.toString());
                            lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                            if (drmInfoRequest.getInfoType() != 7) {
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                                if (fis2 != null) {
                                    fis2.close();
                                }
                                if (fos2 != null) {
                                    fos2.close();
                                }
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                    Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                    uf = new File(filepath);
                                    Log.e(TAG, "DM file delete fail");
                                }
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                    if (!filepath.toLowerCase().endsWith(".dm")) {
                                        Filename = filepath.replace(".dm", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    } else if (filepath.toLowerCase().endsWith(".fl")) {
                                        Filename = filepath.replace(".fl", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    }
                                }
                            } else if (fis2 != null) {
                                fis2.close();
                            }
                            return lDrmInfo;
                        }
                    } catch (IOException e7) {
                        e = e7;
                        Log.e(TAG, "Exception the file " + e.toString());
                        lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                        if (drmInfoRequest.getInfoType() != 7) {
                            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                            if (fis2 != null) {
                                fis2.close();
                            }
                            if (fos2 != null) {
                                fos2.close();
                            }
                            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                            if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                uf = new File(filepath);
                                Log.e(TAG, "DM file delete fail");
                            }
                            if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                if (!filepath.toLowerCase().endsWith(".dm")) {
                                    Filename = filepath.replace(".dm", ".dcf");
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                    uf = new File(Filename);
                                    Log.e(TAG, "DM file delete fail");
                                } else if (filepath.toLowerCase().endsWith(".fl")) {
                                    Filename = filepath.replace(".fl", ".dcf");
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                    uf = new File(Filename);
                                    Log.e(TAG, "DM file delete fail");
                                }
                            }
                        } else if (fis2 != null) {
                            fis2.close();
                        }
                        return lDrmInfo;
                    }
                } else if (filepath.toLowerCase().endsWith(".fl")) {
                    try {
                        fis = new FileInputStream(filepath);
                        try {
                            drmInfoRequest.put("FileDescriptorKey", fis.getFD().toString());
                            fos = new FileOutputStream(filepath.replace(".fl", ".dcf"));
                        } catch (IOException e8) {
                            e = e8;
                            fis2 = fis;
                            Log.e(TAG, "Exception the file " + e.toString());
                            lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                            if (drmInfoRequest.getInfoType() != 7) {
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                                if (fis2 != null) {
                                    fis2.close();
                                }
                                if (fos2 != null) {
                                    fos2.close();
                                }
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                    Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                    uf = new File(filepath);
                                    Log.e(TAG, "DM file delete fail");
                                }
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                    if (!filepath.toLowerCase().endsWith(".dm")) {
                                        Filename = filepath.replace(".dm", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    } else if (filepath.toLowerCase().endsWith(".fl")) {
                                        Filename = filepath.replace(".fl", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    }
                                }
                            } else if (fis2 != null) {
                                fis2.close();
                            }
                            return lDrmInfo;
                        }
                        try {
                            drmInfoRequest.put("OutFileDescriptorKey", fos.getFD().toString());
                            fos2 = fos;
                            fis2 = fis;
                        } catch (IOException e9) {
                            e = e9;
                            fos2 = fos;
                            fis2 = fis;
                            Log.e(TAG, "Exception the file " + e.toString());
                            lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                            if (drmInfoRequest.getInfoType() != 7) {
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                                if (fis2 != null) {
                                    fis2.close();
                                }
                                if (fos2 != null) {
                                    fos2.close();
                                }
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                    Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                    uf = new File(filepath);
                                    Log.e(TAG, "DM file delete fail");
                                }
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                    if (!filepath.toLowerCase().endsWith(".dm")) {
                                        Filename = filepath.replace(".dm", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    } else if (filepath.toLowerCase().endsWith(".fl")) {
                                        Filename = filepath.replace(".fl", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    }
                                }
                            } else if (fis2 != null) {
                                fis2.close();
                            }
                            return lDrmInfo;
                        }
                    } catch (IOException e10) {
                        e = e10;
                        Log.e(TAG, "Exception the file " + e.toString());
                        lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                        if (drmInfoRequest.getInfoType() != 7) {
                            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                            if (fis2 != null) {
                                fis2.close();
                            }
                            if (fos2 != null) {
                                fos2.close();
                            }
                            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                            if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                uf = new File(filepath);
                                Log.e(TAG, "DM file delete fail");
                            }
                            if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                if (!filepath.toLowerCase().endsWith(".dm")) {
                                    Filename = filepath.replace(".dm", ".dcf");
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                    uf = new File(Filename);
                                    Log.e(TAG, "DM file delete fail");
                                } else if (filepath.toLowerCase().endsWith(".fl")) {
                                    Filename = filepath.replace(".fl", ".dcf");
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                    uf = new File(Filename);
                                    Log.e(TAG, "DM file delete fail");
                                }
                            }
                        } else if (fis2 != null) {
                            fis2.close();
                        }
                        return lDrmInfo;
                    }
                } else {
                    Log.i(TAG, "file extention is not dm or fl");
                    try {
                        fis = new FileInputStream(filepath);
                        try {
                            drmInfoRequest.put("FileDescriptorKey", fis.getFD().toString());
                            fis2 = fis;
                        } catch (IOException e11) {
                            e = e11;
                            fis2 = fis;
                            Log.e(TAG, "Exception the file " + e.toString());
                            lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                            if (drmInfoRequest.getInfoType() != 7) {
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                                if (fis2 != null) {
                                    fis2.close();
                                }
                                if (fos2 != null) {
                                    fos2.close();
                                }
                                Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                    Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                    uf = new File(filepath);
                                    Log.e(TAG, "DM file delete fail");
                                }
                                if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                    if (!filepath.toLowerCase().endsWith(".dm")) {
                                        Filename = filepath.replace(".dm", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    } else if (filepath.toLowerCase().endsWith(".fl")) {
                                        Filename = filepath.replace(".fl", ".dcf");
                                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                        uf = new File(Filename);
                                        Log.e(TAG, "DM file delete fail");
                                    }
                                }
                            } else if (fis2 != null) {
                                fis2.close();
                            }
                            return lDrmInfo;
                        }
                    } catch (IOException e12) {
                        e = e12;
                        Log.e(TAG, "Exception the file " + e.toString());
                        lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                        if (drmInfoRequest.getInfoType() != 7) {
                            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                            if (fis2 != null) {
                                fis2.close();
                            }
                            if (fos2 != null) {
                                fos2.close();
                            }
                            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                            if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                                Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                                uf = new File(filepath);
                                Log.e(TAG, "DM file delete fail");
                            }
                            if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                                Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                                if (!filepath.toLowerCase().endsWith(".dm")) {
                                    Filename = filepath.replace(".dm", ".dcf");
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                    uf = new File(Filename);
                                    Log.e(TAG, "DM file delete fail");
                                } else if (filepath.toLowerCase().endsWith(".fl")) {
                                    Filename = filepath.replace(".fl", ".dcf");
                                    Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                    uf = new File(Filename);
                                    Log.e(TAG, "DM file delete fail");
                                }
                            }
                        } else if (fis2 != null) {
                            fis2.close();
                        }
                        return lDrmInfo;
                    }
                }
            }
        } else if (!(filepath == null || filepath.isEmpty())) {
            try {
                fis = new FileInputStream(filepath);
                try {
                    drmInfoRequest.put("FileDescriptorKey", fis.getFD().toString());
                    fis2 = fis;
                } catch (IOException e13) {
                    e = e13;
                    fis2 = fis;
                    Log.e(TAG, "Exception the file " + e.toString());
                    lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                    if (drmInfoRequest.getInfoType() != 7) {
                        Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                        if (fis2 != null) {
                            fis2.close();
                        }
                        if (fos2 != null) {
                            fos2.close();
                        }
                        Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                        if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                            Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                            uf = new File(filepath);
                            Log.e(TAG, "DM file delete fail");
                        }
                        if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                            Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                            if (!filepath.toLowerCase().endsWith(".dm")) {
                                Filename = filepath.replace(".dm", ".dcf");
                                Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                uf = new File(Filename);
                                Log.e(TAG, "DM file delete fail");
                            } else if (filepath.toLowerCase().endsWith(".fl")) {
                                Filename = filepath.replace(".fl", ".dcf");
                                Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                                uf = new File(Filename);
                                Log.e(TAG, "DM file delete fail");
                            }
                        }
                    } else if (fis2 != null) {
                        fis2.close();
                    }
                    return lDrmInfo;
                }
            } catch (IOException e14) {
                e = e14;
                Log.e(TAG, "Exception the file " + e.toString());
                lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
                if (drmInfoRequest.getInfoType() != 7) {
                    Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
                    if (fis2 != null) {
                        fis2.close();
                    }
                    if (fos2 != null) {
                        fos2.close();
                    }
                    Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
                    if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                        Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                        uf = new File(filepath);
                        Log.e(TAG, "DM file delete fail");
                    }
                    if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                        if (!filepath.toLowerCase().endsWith(".dm")) {
                            Filename = filepath.replace(".dm", ".dcf");
                            Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                            uf = new File(Filename);
                            Log.e(TAG, "DM file delete fail");
                        } else if (filepath.toLowerCase().endsWith(".fl")) {
                            Filename = filepath.replace(".fl", ".dcf");
                            Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                            uf = new File(Filename);
                            Log.e(TAG, "DM file delete fail");
                        }
                    }
                } else if (fis2 != null) {
                    fis2.close();
                }
                return lDrmInfo;
            }
        }
        lDrmInfo = _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
        if (drmInfoRequest.getInfoType() != 7) {
            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE");
            if (fis2 != null) {
                fis2.close();
            }
            if (fos2 != null) {
                fos2.close();
            }
            Log.e(TAG, "_acquireDrmInfo TYPE_CONVERT_DRM_FILE " + lDrmInfo.get("status"));
            if (lDrmInfo.get("status").equals(DrmInfoRequest.SUCCESS)) {
                Log.e(TAG, "DM file delete DrmInfoRequest.SUCCESS");
                if (!(filepath == null || filepath.isEmpty())) {
                    uf = new File(filepath);
                    if (uf.exists() && !uf.delete()) {
                        Log.e(TAG, "DM file delete fail");
                    }
                }
            }
            if (lDrmInfo.get("status").equals(DrmInfoRequest.FAIL)) {
                Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL");
                if (!(filepath == null || filepath.isEmpty())) {
                    if (!filepath.toLowerCase().endsWith(".dm")) {
                        Filename = filepath.replace(".dm", ".dcf");
                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                        uf = new File(Filename);
                        if (uf.exists() && !uf.delete()) {
                            Log.e(TAG, "DM file delete fail");
                        }
                    } else if (filepath.toLowerCase().endsWith(".fl")) {
                        Filename = filepath.replace(".fl", ".dcf");
                        Log.e(TAG, "dcf file delete DrmInfoRequest.FAIL" + Filename);
                        uf = new File(Filename);
                        if (uf.exists() && !uf.delete()) {
                            Log.e(TAG, "DM file delete fail");
                        }
                    }
                }
            }
        } else if (fis2 != null) {
            fis2.close();
        }
        return lDrmInfo;
    }

    public int acquireRights(DrmInfoRequest drmInfoRequest) {
        DrmInfo drmInfo = acquireDrmInfo(drmInfoRequest);
        if (drmInfo == null) {
            return ERROR_UNKNOWN;
        }
        return processDrmInfo(drmInfo);
    }

    public int getDrmObjectType(String path, String mimeType) {
        IOException e;
        Throwable th;
        if ((path == null || path.equals(ProxyInfo.LOCAL_EXCL_LIST)) && (mimeType == null || mimeType.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
            throw new IllegalArgumentException("Path or the mimetype should be non null");
        } else if (path == null || _checkFDSupporting(path) == 0) {
            return _getDrmObjectType(this.mUniqueId, path, mimeType);
        } else {
            int result = 0;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(path);
                try {
                    result = _getDrmObjectType(this.mUniqueId, fis2.getFD().toString(), mimeType);
                    if (fis2 != null) {
                        try {
                            fis2.close();
                            fis = fis2;
                            return result;
                        } catch (IOException e2) {
                            fis = fis2;
                            return result;
                        }
                    }
                    return result;
                } catch (IOException e3) {
                    e = e3;
                    fis = fis2;
                    try {
                        Log.e(TAG, "Exception the file " + e.toString());
                        if (fis != null) {
                            return result;
                        }
                        try {
                            fis.close();
                            return result;
                        } catch (IOException e4) {
                            return result;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                Log.e(TAG, "Exception the file " + e.toString());
                if (fis != null) {
                    return result;
                }
                fis.close();
                return result;
            }
        }
    }

    public int getDrmObjectType(Uri uri, String mimeType) {
        if ((uri == null || Uri.EMPTY == uri) && (mimeType == null || mimeType.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
            throw new IllegalArgumentException("Uri or the mimetype should be non null");
        }
        String path = ProxyInfo.LOCAL_EXCL_LIST;
        try {
            path = convertUriToPath(uri);
        } catch (Exception e) {
            Log.w(TAG, "Given Uri could not be found in media store");
        }
        return getDrmObjectType(path, mimeType);
    }

    public String getOriginalMimeType(String path) {
        Throwable th;
        if (path == null || path.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            throw new IllegalArgumentException("Given path should be non null");
        }
        String mime = null;
        FileInputStream is = null;
        FileDescriptor fd = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream is2 = new FileInputStream(file);
                try {
                    fd = is2.getFD();
                    is = is2;
                } catch (IOException e) {
                    is = is2;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                        }
                    }
                    return mime;
                } catch (Throwable th2) {
                    th = th2;
                    is = is2;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            }
            mime = _getOriginalMimeType(this.mUniqueId, path, fd);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        } catch (IOException e5) {
            if (is != null) {
                is.close();
            }
            return mime;
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                is.close();
            }
            throw th;
        }
        return mime;
    }

    public String getOriginalMimeType(Uri uri) {
        if (uri != null && Uri.EMPTY != uri) {
            return getOriginalMimeType(convertUriToPath(uri));
        }
        throw new IllegalArgumentException("Given uri is not valid");
    }

    public int checkRightsStatus(String path) {
        return checkRightsStatus(path, 0);
    }

    public int checkRightsStatus(Uri uri) {
        if (uri != null && Uri.EMPTY != uri) {
            return checkRightsStatus(convertUriToPath(uri));
        }
        throw new IllegalArgumentException("Given uri is not valid");
    }

    public int checkRightsStatus(String path, int action) {
        IOException e;
        Throwable th;
        if (path == null || path.equals(ProxyInfo.LOCAL_EXCL_LIST) || !Action.isValid(action)) {
            throw new IllegalArgumentException("Given path or action is not valid");
        } else if (_checkFDSupporting(path) == 0) {
            return _checkRightsStatus(this.mUniqueId, path, action);
        } else {
            int result = 0;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(path);
                try {
                    result = _checkRightsStatus(this.mUniqueId, fis2.getFD().toString(), action);
                    if (fis2 != null) {
                        try {
                            fis2.close();
                            fis = fis2;
                            return result;
                        } catch (IOException e2) {
                            fis = fis2;
                            return result;
                        }
                    }
                    return result;
                } catch (IOException e3) {
                    e = e3;
                    fis = fis2;
                    try {
                        Log.e(TAG, "Exception the file " + e.toString());
                        if (fis != null) {
                            return result;
                        }
                        try {
                            fis.close();
                            return result;
                        } catch (IOException e4) {
                            return result;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                Log.e(TAG, "Exception the file " + e.toString());
                if (fis != null) {
                    return result;
                }
                fis.close();
                return result;
            }
        }
    }

    public int checkRightsStatus(Uri uri, int action) {
        if (uri != null && Uri.EMPTY != uri) {
            return checkRightsStatus(convertUriToPath(uri), action);
        }
        throw new IllegalArgumentException("Given uri is not valid");
    }

    public int removeRights(String path) {
        IOException e;
        Throwable th;
        if (path == null || path.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            throw new IllegalArgumentException("Given path should be non null");
        } else if (_checkFDSupporting(path) == 0) {
            return _removeRights(this.mUniqueId, path);
        } else {
            int result = 0;
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(path);
                try {
                    result = _removeRights(this.mUniqueId, fis2.getFD().toString());
                    if (fis2 != null) {
                        try {
                            fis2.close();
                            fis = fis2;
                            return result;
                        } catch (IOException e2) {
                            fis = fis2;
                            return result;
                        }
                    }
                    return result;
                } catch (IOException e3) {
                    e = e3;
                    fis = fis2;
                    try {
                        Log.e(TAG, "Exception the file " + e.toString());
                        if (fis != null) {
                            return result;
                        }
                        try {
                            fis.close();
                            return result;
                        } catch (IOException e4) {
                            return result;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                Log.e(TAG, "Exception the file " + e.toString());
                if (fis != null) {
                    return result;
                }
                fis.close();
                return result;
            }
        }
    }

    public int removeRights(Uri uri) {
        if (uri != null && Uri.EMPTY != uri) {
            return removeRights(convertUriToPath(uri));
        }
        throw new IllegalArgumentException("Given uri is not valid");
    }

    public int removeAllRights() {
        if (this.mEventHandler == null) {
            return ERROR_UNKNOWN;
        }
        if (this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(1001))) {
            return 0;
        }
        return ERROR_UNKNOWN;
    }

    public int openConvertSession(String mimeType) {
        if (mimeType != null && !mimeType.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            return _openConvertSession(this.mUniqueId, mimeType);
        }
        throw new IllegalArgumentException("Path or the mimeType should be non null");
    }

    public DrmConvertedStatus convertData(int convertId, byte[] inputData) {
        if (inputData != null && inputData.length > 0) {
            return _convertData(this.mUniqueId, convertId, inputData);
        }
        throw new IllegalArgumentException("Given inputData should be non null");
    }

    public DrmConvertedStatus closeConvertSession(int convertId) {
        return _closeConvertSession(this.mUniqueId, convertId);
    }

    private int getEventType(int infoType) {
        switch (infoType) {
            case 1:
            case 2:
            case 3:
            case 22:
            case 31:
            case 32:
            case 33:
            case 34:
                return 1002;
            default:
                return -1;
        }
    }

    private int getErrorType(int infoType, DrmInfoStatus infoStatus) {
        switch (infoType) {
            case 1:
            case 2:
            case 3:
            case 22:
            case 31:
            case 32:
            case 33:
            case 34:
                if (infoStatus == null || infoStatus.mimeType.contains("video/wvm") || infoStatus.statusCode == 2) {
                    Log.i(TAG, "getErrorType return TYPE_PROCESS_DRM_INFO_FAILED becauseof widevine or STATUS_ERROR");
                    return DrmErrorEvent.TYPE_PROCESS_DRM_INFO_FAILED;
                }
                Log.i(TAG, "getErrorType infoStatus.statusCode: " + infoStatus.statusCode);
                return infoStatus.statusCode;
            default:
                return -1;
        }
    }

    private String convertUriToPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals(ProxyInfo.LOCAL_EXCL_LIST) || scheme.equals(ContentResolver.SCHEME_FILE)) {
            return uri.getPath();
        }
        if (scheme.equals(IntentFilter.SCHEME_HTTP)) {
            return uri.toString();
        }
        if (scheme.equals("content")) {
            Cursor cursor = null;
            try {
                cursor = this.mContext.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst()) {
                    throw new IllegalArgumentException("Given Uri could not be found in media store");
                }
                String path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                if (cursor == null) {
                    return path;
                }
                cursor.close();
                return path;
            } catch (SQLiteException e) {
                throw new IllegalArgumentException("Given Uri is not formatted in a way so that it can be found in media store.");
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            throw new IllegalArgumentException("Given Uri scheme is not supported");
        }
    }

    private void createEventThreads() {
        if (this.mEventHandler == null && this.mInfoHandler == null) {
            this.mInfoThread = new HandlerThread("DrmManagerClient.InfoHandler");
            this.mInfoThread.start();
            this.mInfoHandler = new InfoHandler(this.mInfoThread.getLooper());
            this.mEventThread = new HandlerThread("DrmManagerClient.EventHandler");
            this.mEventThread.start();
            this.mEventHandler = new EventHandler(this.mEventThread.getLooper());
        }
    }

    private void createListeners() {
        _setListeners(this.mUniqueId, new WeakReference(this));
    }
}
