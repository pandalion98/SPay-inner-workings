package android.sec.clipboard;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.sec.clipboard.IClipboardDataPasteEvent.Stub;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.list.ClipboardDataBitmap;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.sec.clipboard.data.list.ClipboardDataText;
import android.sec.clipboard.data.list.ClipboardDataUri;
import android.sec.clipboard.util.FileHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.android.internal.R;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.File;
import java.util.ArrayList;

public class ClipboardExManager {
    public static final int DATE = 1;
    public static final int DEFAULT = 0;
    public static final int EMAIL = 4;
    public static final int FORMAT_ALL_ID = 1;
    public static final int FORMAT_BITMAP_ID = 3;
    public static final int FORMAT_HTML_FLAGMENT_ID = 4;
    public static final int FORMAT_INTENT_ID = 6;
    public static final int FORMAT_MULTIPLE_TYPE_ID = 8;
    public static final int FORMAT_MULTIPLE_URI_ID = 7;
    public static final int FORMAT_TEXT_ID = 2;
    public static final int FORMAT_URI_ID = 5;
    public static final int PHONE_NUMBER = 3;
    private static final String TAG = "ClipboardExManager";
    public static final int TIME = 2;
    public static final int URL = 5;
    private static IClipboardService sService = null;
    private final int FAIL_SET_DATA = 1;
    private final int PROTECTED_DATA_MAX = 3;
    private final int SUCCESS_AND_SAVE_BITMAP = 2;
    private final int SUCCESS_SET_DATA = 0;
    private final int _UNFORMAT = -1;
    private ClipboardConverter mClipboardConverter = null;
    private IClipboardDataPasteEventImpl mClipboardEventImpl = new IClipboardDataPasteEventImpl();
    private ClipboardEventListener mClipboardEventListener = null;
    private Context mContext = null;
    private int mFormatid = -1;
    private boolean mIsFiltered = false;
    private boolean mIsMaximumSize = false;
    private IClipboardDataPasteEvent mPasteEvent = null;
    private PersonaManager mPersonaManager = null;
    private IClipboardWorkingFormUiInterface mRegInterface = null;
    private Handler mSetDataHandler = null;

    public interface ClipboardEventListener {
        void onPaste(ClipboardData clipboardData);
    }

    public static class Format {
        public static final int ALL = 1;
        public static final int BITMAP = 3;
        public static final int HTML = 4;
        public static final int INTENT = 6;
        public static final int MULTIPLE_TYPE = 8;
        public static final int TEXT = 2;
        public static final int URI = 5;
        public static final int URI_LIST = 7;

        private Format() {
        }
    }

    private class IClipboardDataPasteEventImpl implements IClipboardDataPasteEvent {
        private final Stub mBinder;

        private IClipboardDataPasteEventImpl() {
            this.mBinder = new Stub() {
                public void onClipboardDataPaste(ClipboardData data) {
                    IClipboardDataPasteEventImpl.this.onClipboardDataPaste(data);
                }
            };
        }

        public IBinder asBinder() {
            return this.mBinder;
        }

        public void onClipboardDataPaste(ClipboardData data) {
            if (ClipboardExManager.this.mClipboardEventListener != null) {
                ClipboardExManager.this.mClipboardEventListener.onPaste(data);
            } else {
                Log.d(ClipboardExManager.TAG, "mClipboardEventListener is null");
            }
        }
    }

    public ClipboardExManager(Context context, Handler handler) {
        this.mContext = context;
        this.mPersonaManager = (PersonaManager) this.mContext.getSystemService("persona");
        if (!ClipboardConstants.HAS_KNOX_FEATURE) {
            Log.d(TAG, "no knox");
        }
    }

    public int getPersonaId() {
        if (!ClipboardConstants.HAS_KNOX_FEATURE) {
            return getUserId();
        }
        if (this.mContext == null) {
            return 0;
        }
        if (this.mPersonaManager == null) {
            this.mPersonaManager = (PersonaManager) this.mContext.getSystemService("persona");
        }
        if (this.mPersonaManager != null) {
            return this.mPersonaManager.getFocusedUser();
        }
        return ActivityManager.getCurrentUser();
    }

    private int getUserId() {
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        if (!PersonaManager.isBBCContainer(userId)) {
            return userId;
        }
        Log.d(TAG, "getUserId is BBC");
        return 0;
    }

    private static IClipboardService getService() {
        if (sService != null) {
            return sService;
        }
        sService = IClipboardService.Stub.asInterface(ServiceManager.getService("clipboardEx"));
        if (sService == null && ClipboardConstants.DEBUG) {
            Log.e(TAG, "Had failed to obtaining clipboardEx service.");
        }
        return sService;
    }

    public void unRegistClipboardWorkingFormUiInterface() {
        if (isEnabled("unRegistClipboardWorkingFormUiInterface")) {
            try {
                if (getService() == null) {
                    if (ClipboardConstants.DEBUG) {
                        Log.w(TAG, "unRegistClipboardWorkingFormUiInterface - Fail~ Service is null.");
                    }
                } else if (this.mRegInterface != null) {
                    getService().unRegistClipboardWorkingFormUiInterfaces(this.mRegInterface);
                } else {
                    Log.e(TAG, "reg interface is null!");
                }
            } catch (Exception e) {
                if (ClipboardConstants.DEBUG) {
                    Log.e(TAG, "unRegistClipboardWorkingFormUiInterface(RemoteException): ");
                }
                e.printStackTrace();
            }
        }
    }

    public void setPasteFrozen(int formatid, IClipboardDataPasteEvent clPasteEvent) {
        if (isEnabled("setPasteFrozen")) {
            if (this.mFormatid != formatid) {
                updateData(formatid, clPasteEvent);
            }
            this.mFormatid = formatid;
            this.mPasteEvent = clPasteEvent;
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "setPasteFrozen - Format:" + formatid + this.mPasteEvent);
            }
        }
    }

    public void setThawPaste() {
        if (isEnabled("setThawPaste")) {
            this.mFormatid = -1;
            this.mPasteEvent = null;
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "setThawPaste");
            }
        }
    }

    public boolean getFrozenState() {
        if (isEnabled("getFrozenState") && this.mPasteEvent != null) {
            return true;
        }
        return false;
    }

    public void callPasteMenuFromApp(int enabled) {
        if (isEnabled("callPasteMenuFromApp")) {
            try {
                if (getService() != null) {
                    getService().callPasteMenuFromApp(enabled);
                } else if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "callPasteMenuFromApp - Fail~ Service is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean iscalledPasteMenuFromApp() {
        boolean z = false;
        if (isEnabled("iscalledPasteMenuFromApp")) {
            try {
                if (getService() != null) {
                    z = getService().iscalledPasteMenuFromApp();
                } else if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "iscalledPasteMenuFromApp - Fail~ Service is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public void callPasteApplication(ClipboardData clipdata) {
        if (!isEnabled("callPasteApplication")) {
            return;
        }
        if (this.mPasteEvent != null) {
            try {
                if (getService() != null) {
                    getService().callPasteMenuFromApp(0);
                    this.mPasteEvent.onClipboardDataPaste(clipdata);
                } else if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "callPasteApplication - Fail~ Service is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ClipboardConstants.DEBUG) {
            Log.e(TAG, "Clipboard Service callPasteApplication mPasteEvent == null");
        }
    }

    public boolean isShowing() {
        boolean z = false;
        if (isEnabled("isShowing")) {
            try {
                if (getService() != null) {
                    z = getService().IsShowUIClipboardData();
                } else if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "isShowing - Fail~ Service is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public void updateData(int formatid, IClipboardDataPasteEvent clPasteEvent) {
        if (!isEnabled("updateData")) {
            return;
        }
        if (this.mPasteEvent == null) {
            try {
                Log.d(TAG, "updateData - " + formatid + ", " + clPasteEvent);
                if (getService() == null) {
                    if (ClipboardConstants.DEBUG) {
                        Log.w(TAG, "updateData - Fail~ Service is null.");
                    }
                } else if (isShowing()) {
                    getService().UpdateUIClipboardData(formatid, clPasteEvent);
                    if (ClipboardConstants.DEBUG) {
                        Log.d(TAG, "updateData - clPasteEvent:" + clPasteEvent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ClipboardConstants.DEBUG) {
            Log.d(TAG, "updateData return : " + clPasteEvent);
        }
    }

    public boolean setData(Context context, ClipboardData data) {
        return addData(context, data, false, true, false);
    }

    public boolean setDataWithoutNoti(Context context, ClipboardData data) {
        return addData(context, data, true, true, false);
    }

    public boolean setData(Context context, ClipData data, boolean setPrimary) {
        this.mClipboardConverter = ClipboardConverter.getInstance(context);
        ClipboardData clipboardData = this.mClipboardConverter.ClipDataToClipboardData(data);
        if (clipboardData == null) {
            return false;
        }
        return addData(context, clipboardData, !setPrimary, setPrimary, false);
    }

    public boolean setDataWithoutSendingOrginalClipboard(Context context, ClipboardData data) {
        return addData(context, data, false, false, true);
    }

    public ClipboardData getData(Context context, int formatid) {
        return getData(formatid);
    }

    public ClipboardData getData(int formatid) {
        if (!isEnabled("getData")) {
            return null;
        }
        try {
            if (getService() != null) {
                int myFormat;
                if (this.mFormatid != -1) {
                    myFormat = this.mFormatid;
                } else {
                    myFormat = formatid;
                }
                Log.w(TAG, "getData : " + formatid + ", " + this.mFormatid + ", " + this.mClipboardEventListener);
                ClipboardData data = getService().GetClipboardData(myFormat);
                if (data != null || this.mContext == null) {
                    return data;
                }
                Toast.makeText(this.mContext, (int) R.string.tw_clipboard_no_items_to_paste, 0).show();
                return data;
            } else if (!ClipboardConstants.DEBUG) {
                return null;
            } else {
                Log.w(TAG, "getData - Fail~ Service is null.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasData(int formatid) {
        if (!isEnabled("hasData")) {
            return false;
        }
        try {
            if (getService() != null) {
                int myFormat;
                if (this.mFormatid != -1) {
                    myFormat = this.mFormatid;
                } else {
                    myFormat = formatid;
                }
                if (getService().GetClipboardData(myFormat) == null) {
                    return false;
                }
                Log.w(TAG, "hasData : " + formatid + ", " + this.mFormatid + ", " + this.mClipboardEventListener);
                return true;
            } else if (!ClipboardConstants.DEBUG) {
                return false;
            } else {
                Log.w(TAG, "hasData - Fail~ Service is null.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasData(Context context, int formatid) {
        return hasData(formatid);
    }

    public boolean hasDataOf(int formatid) {
        return hasData(formatid);
    }

    public int getDataListSize() {
        if (!isEnabled("getDataListSize")) {
            return 0;
        }
        int size = -1;
        try {
            if (getService() != null) {
                return getService().getDataSize();
            }
            if (!ClipboardConstants.DEBUG) {
                return size;
            }
            Log.w(TAG, "getDataListSize - Fail~ Service is null.");
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            return size;
        }
    }

    public int getScrapDataSize() {
        Log.e(TAG, "getScrapDataSize!");
        return 0;
    }

    public boolean getData(Context context, int formatid, IClipboardDataPasteEvent clPasteEvent) {
        if (!isEnabled("getData")) {
            return false;
        }
        startClipboardUIServiceService();
        Log.i(TAG, "call getData.." + formatid);
        try {
            if (getService() == null) {
                if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "getData - Fail~ Service is null.");
                }
                return false;
            } else if (this.mPasteEvent == null) {
                getService().ShowUIClipboardData(formatid, clPasteEvent);
                Log.i(TAG, "getdata - " + clPasteEvent + ", " + formatid);
                return true;
            } else {
                getService().ShowUIClipboardData(this.mFormatid, this.mPasteEvent);
                Log.i(TAG, "getdata - " + this.mPasteEvent + ", " + this.mFormatid);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean AddClipboardFormatListener(IClipboardFormatListener listener) {
        if (!isEnabled("AddClipboardFormatListener")) {
            return false;
        }
        try {
            if (getService() != null) {
                return getService().AddClipboardFormatListener(listener);
            }
            if (!ClipboardConstants.DEBUG) {
                return false;
            }
            Log.w(TAG, "AddClipboardFormatListener - Fail~ Service is null.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean RemoveClipboardFormatListener(IClipboardFormatListener listener) {
        if (!isEnabled("RemoveClipboardFormatListener")) {
            return false;
        }
        try {
            if (getService() != null) {
                return getService().RemoveClipboardFormatListener(listener);
            }
            if (!ClipboardConstants.DEBUG) {
                return false;
            }
            Log.w(TAG, "RemoveClipboardFormatListener - Fail~ Service is null.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean RegistClipboardWorkingFormUiInterface(IClipboardWorkingFormUiInterface iRegInterface) {
        if (!isEnabled("RegistClipboardWorkingFormUiInterface")) {
            return false;
        }
        if (this.mRegInterface == null || this.mRegInterface != iRegInterface) {
            this.mRegInterface = iRegInterface;
        }
        if (getService() != null) {
            try {
                getService().RegistClipboardWorkingFormUiInterfaces(this.mRegInterface);
                if (ClipboardConstants.DEBUG) {
                    Log.i(TAG, "Regist ClipboardWorkingFormUiInterface - Success.");
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (!ClipboardConstants.DEBUG) {
            return false;
        } else {
            Log.w(TAG, "RegistClipboardWorkingFormUiInterface - Fail~ Service is null.");
            return false;
        }
    }

    public ArrayList<String> getClipedStrings(int metaType, int maxCount) {
        if (!isEnabled("getClipedStrings")) {
            return null;
        }
        ArrayList<String> clipboardData = new ArrayList();
        try {
            if (getService() != null) {
                return (ArrayList) getService().getClipedStrings(metaType, maxCount);
            }
            if (ClipboardConstants.DEBUG) {
                Log.w(TAG, "getClipedStrings - Fail~ Service is null.");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return clipboardData;
        }
    }

    public float getPreviewIconXpos() {
        Log.d(TAG, "getPreviewIconXpos");
        return 0.0f;
    }

    public float getPreviewIconYpos() {
        Log.d(TAG, "getPreviewIconYpos");
        return 0.0f;
    }

    public void showUIFloatingIcon() {
        Log.w(TAG, "showUIFloatingIcon");
    }

    public void hideUIFloatingIcon() {
        Log.w(TAG, "hideUIFloatingIcon");
    }

    public void sendCropRect(Rect cropRect, boolean showAni) {
        Log.e(TAG, "sendCropRect!");
    }

    private void startClipboardUIServiceService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.samsung.android.clipboarduiservice", "com.samsung.android.clipboarduiservice.ClipboardUIServiceStarter"));
            this.mContext.startServiceAsUser(intent, UserHandle.OWNER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startKeepUIServiceService() {
        Log.w(TAG, "startKeepUIServiceService");
    }

    private int isClipboardAllowed(boolean showToast, int userId) {
        if (this.mContext == null) {
            return -1;
        }
        Cursor cr = this.mContext.getContentResolver().query(Uri.parse(SecContentProviderURI.RESTRICTION1_URI), null, SecContentProviderURI.RESTRICTIONPOLICY_CLIPBOARDALLOWEDASUSER_METHOD, new String[]{Boolean.toString(showToast), Integer.toString(userId)}, null);
        if (cr == null) {
            return -1;
        }
        try {
            cr.moveToFirst();
            if (!cr.getString(cr.getColumnIndex(SecContentProviderURI.RESTRICTIONPOLICY_CLIPBOARDALLOWEDASUSER_METHOD)).equals(SmartFaceManager.FALSE)) {
                return 1;
            }
            cr.close();
            return 0;
        } finally {
            cr.close();
        }
    }

    public void setClipboardFormat(int formatid, ClipboardEventListener listener) {
        if (isEnabled("setClipboardFormat")) {
            if (this.mFormatid != formatid) {
                updateClipboard(formatid, listener);
            }
            this.mIsFiltered = true;
            this.mFormatid = formatid;
            this.mClipboardEventListener = listener;
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "setClipboardFormat - Format:" + formatid + ", " + listener);
            }
        }
    }

    public void setFilter(int filter, ClipboardEventListener listener) {
        if (isEnabled("setFilter")) {
            this.mIsFiltered = false;
            if (filter == 4) {
                filter = 1;
                Log.d(TAG, "setFilter - Format changed");
            }
            updateFilter(filter, listener);
            Log.d(TAG, "setFilter - Format:" + filter + ", " + listener);
            this.mIsFiltered = true;
            this.mFormatid = filter;
            this.mClipboardEventListener = listener;
        }
    }

    public void updateFilter(int formatid, ClipboardEventListener listener) {
        if (!isEnabled("updateFilter")) {
            return;
        }
        if (!isFiltered()) {
            try {
                if (getService() != null) {
                    this.mClipboardEventListener = listener;
                    getService().UpdateUIClipboardData(formatid, this.mClipboardEventImpl);
                    Log.d(TAG, "updateFilter - " + formatid + ", " + this.mClipboardEventListener);
                } else if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "updateFilter - Fail~ Service is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ClipboardConstants.DEBUG) {
            Log.d(TAG, "updateFilter return : " + this.mClipboardEventListener);
        }
    }

    public void clearClipboardFormat() {
        if (isEnabled("setFilter")) {
            this.mIsFiltered = false;
            this.mFormatid = -1;
            this.mClipboardEventListener = null;
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "clearClipboardFormat");
            }
        }
    }

    public void clearFilter() {
        if (isEnabled("setFilter")) {
            this.mIsFiltered = false;
            this.mFormatid = -1;
            this.mClipboardEventListener = null;
            Log.d(TAG, "clearClipboardFormat");
        }
    }

    public boolean hasAppClipboardListener() {
        return isFiltered();
    }

    public boolean isFiltered() {
        if (isEnabled("isFiltered")) {
            return this.mIsFiltered;
        }
        return false;
    }

    public void requestPaste(ClipboardData clipdata) {
        if (isEnabled("requestPaste")) {
            Log.d(TAG, "requestPaste : " + (clipdata != null ? clipdata.getFormat() : -1));
            if (isFiltered()) {
                try {
                    if (getService() == null) {
                        if (ClipboardConstants.DEBUG) {
                            Log.w(TAG, "requestPaste - Fail~ Service is null.");
                            return;
                        }
                        return;
                    } else if (clipdata.IsAlternateformatAvailable(this.mFormatid)) {
                        this.mClipboardEventListener.onPaste(clipdata);
                        return;
                    } else {
                        Log.e(TAG, "Can't convert format type : " + this.mFormatid + ", " + clipdata.getFormat());
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Log.e(TAG, "no app clipboard listener!");
        }
    }

    public void updateClipboard(int formatid, ClipboardEventListener listener) {
        if (isEnabled("updateClipboard")) {
            if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "updateClipboard " + this.mClipboardEventListener + ", " + formatid);
            }
            if (!hasAppClipboardListener()) {
                try {
                    if (getService() == null) {
                        if (ClipboardConstants.DEBUG) {
                            Log.w(TAG, "updateData - Fail~ Service is null.");
                        }
                    } else if (isShowing()) {
                        this.mClipboardEventListener = listener;
                        getService().UpdateUIClipboardData(formatid, this.mClipboardEventImpl);
                        if (ClipboardConstants.DEBUG) {
                            Log.d(TAG, "updateClipboard - listener:" + this.mClipboardEventListener);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ClipboardConstants.DEBUG) {
                Log.d(TAG, "updateData return : " + this.mClipboardEventListener);
            }
        }
    }

    public boolean showClipboard(int formatid, ClipboardEventListener listener) {
        return showDialog(formatid, listener);
    }

    public boolean showDialog(int formatid, ClipboardEventListener listener) {
        if (!isEnabled("showDialog")) {
            return false;
        }
        startClipboardUIServiceService();
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "showDialog : " + formatid);
        }
        try {
            if (getService() == null) {
                if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "showDialog - Fail~ Service is null.");
                }
                return false;
            } else if (isFiltered()) {
                getService().ShowUIClipboardData(this.mFormatid, this.mClipboardEventImpl);
                Log.d(TAG, "showDialog - " + formatid + ", " + this.mFormatid + " : " + listener);
                return true;
            } else {
                this.mClipboardEventListener = listener;
                getService().ShowUIClipboardData(formatid, this.mClipboardEventImpl);
                Log.d(TAG, "showDialog - " + formatid + " : " + listener);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showUIDataDialog() {
        showDialog();
    }

    public void showDialog() {
        if (isEnabled("showDialog")) {
            startClipboardUIServiceService();
            try {
                if (getService() != null) {
                    getService().showUIDataDialog();
                } else if (ClipboardConstants.DEBUG) {
                    Log.w(TAG, "showDialog - Fail~ Service is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissUIDataDialog() {
        dismissDialog();
    }

    public void dismissDialog() {
        try {
            if (getService() != null) {
                getService().dismissUIDataDialog();
                Log.d(TAG, "dismissDialog");
            } else if (ClipboardConstants.DEBUG) {
                Log.w(TAG, "dismissDialog - Fail~ Service is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addData(Context context, ClipboardData data, boolean withoutNoti, boolean setPrimary, boolean withoutSendingOrigin) {
        if (!isEnabled("addData")) {
            return false;
        }
        this.mContext = context;
        if (getService() != null && data != null) {
            if (data.getFormat() == 4) {
                ClipboardDataHtml htmlData = (ClipboardDataHtml) data.GetAlternateFormat(4);
                if (htmlData == null || htmlData.getPlainText() == null || htmlData.getPlainText().length() < 131072) {
                    this.mIsMaximumSize = false;
                } else {
                    this.mIsMaximumSize = true;
                }
            } else if (data.getFormat() == 2) {
                ClipboardDataText textData = (ClipboardDataText) data.GetAlternateFormat(2);
                if (textData == null || textData.getText() == null || textData.getText().length() < 131072) {
                    this.mIsMaximumSize = false;
                } else {
                    this.mIsMaximumSize = true;
                }
            } else {
                this.mIsMaximumSize = false;
            }
            if (this.mSetDataHandler == null) {
                this.mSetDataHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        AccessibilityManager am = (AccessibilityManager) ClipboardExManager.this.mContext.getSystemService("accessibility");
                        switch (msg.what) {
                            case 0:
                                if (ClipboardExManager.this.mContext != null) {
                                    if (!am.isTwoFingerGestureRecognitionEnabled()) {
                                        if (ClipboardExManager.this.mIsMaximumSize) {
                                            Toast.makeText(ClipboardExManager.this.mContext, (int) R.string.clipboard_copied_to_clipboard_maximum_exceeded, 0).show();
                                            ClipboardExManager.this.mIsMaximumSize = false;
                                        } else {
                                            Toast.makeText(ClipboardExManager.this.mContext, (int) R.string.clipboard_copied_to_clipboard, 0).show();
                                        }
                                    }
                                    if (ClipboardConstants.DEBUG) {
                                        Log.e(ClipboardExManager.TAG, "success set data ");
                                        return;
                                    }
                                    return;
                                }
                                return;
                            case 1:
                                if (ClipboardExManager.this.mContext != null && ClipboardExManager.this.isClipboardAllowed(false, ClipboardExManager.this.getPersonaId()) != 0) {
                                    if (!am.isTwoFingerGestureRecognitionEnabled()) {
                                        Toast.makeText(ClipboardExManager.this.mContext, (int) R.string.tw_clipboard_already_exists, 0).show();
                                    }
                                    if (ClipboardConstants.DEBUG) {
                                        Log.e(ClipboardExManager.TAG, "Fail set data ");
                                        return;
                                    }
                                    return;
                                }
                                return;
                            case 3:
                                Toast.makeText(ClipboardExManager.this.mContext, String.format(ClipboardExManager.this.mContext.getString(R.string.clipboard_exceed_msg), new Object[]{Integer.valueOf(10)}), 0).show();
                                return;
                            default:
                                return;
                        }
                    }
                };
            }
            return add(data, withoutNoti, setPrimary, withoutSendingOrigin);
        } else if (!ClipboardConstants.DEBUG) {
            return false;
        } else {
            Log.w(TAG, "addData - Fail~ Service is null, " + data);
            return false;
        }
    }

    private boolean makeFileDescriptor(ClipboardData data) {
        FileHelper fh = FileHelper.getInstance();
        String imgPath;
        File f;
        switch (data.getFormat()) {
            case 3:
                ClipboardDataBitmap target = (ClipboardDataBitmap) data;
                imgPath = target.getBitmapPath();
                if (imgPath == null || imgPath.length() <= 0) {
                    Log.d(TAG, "no bitmap file");
                } else {
                    f = new File(imgPath);
                    if (fh.checkFile(f)) {
                        try {
                            target.setParcelFileDescriptor(ParcelFileDescriptor.open(f, 939524096));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    Log.d(TAG, "it's not file. : " + f.getAbsolutePath());
                    return false;
                }
                if (!target.HasExtraData()) {
                    Log.d(TAG, "no extra bitmap file");
                    break;
                }
                String extraPath = target.getExtraDataPath();
                if (extraPath != null && extraPath.length() > 0) {
                    f = new File(extraPath);
                    if (fh.checkFile(f)) {
                        try {
                            target.setExtraParcelFileDescriptor(ParcelFileDescriptor.open(f, 939524096));
                            break;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return false;
                        }
                    }
                    Log.d(TAG, "it's not file. : " + f.getAbsolutePath());
                    return false;
                }
                break;
            case 4:
                ClipboardDataHtml target2 = (ClipboardDataHtml) data;
                imgPath = target2.getFirstImgPath();
                if (imgPath == null || imgPath.length() <= 0) {
                    Log.d(TAG, "no first image file");
                    break;
                }
                f = new File(imgPath);
                if (fh.checkFile(f)) {
                    try {
                        target2.setParcelFileDescriptor(ParcelFileDescriptor.open(f, 939524096));
                        break;
                    } catch (Exception e22) {
                        e22.printStackTrace();
                        return false;
                    }
                }
                Log.d(TAG, "it's not file. : " + f.getAbsolutePath());
                return false;
                break;
            case 5:
                ClipboardDataUri target3 = (ClipboardDataUri) data;
                imgPath = target3.getPreviewImgPath();
                if (imgPath == null || imgPath.length() <= 0) {
                    Log.d(TAG, "no preview image file");
                    break;
                }
                f = new File(imgPath);
                if (fh.checkFile(f)) {
                    try {
                        target3.setParcelFileDescriptor(ParcelFileDescriptor.open(f, 939524096));
                        break;
                    } catch (Exception e222) {
                        e222.printStackTrace();
                        return false;
                    }
                }
                Log.d(TAG, "it's not file. : " + f.getAbsolutePath());
                return false;
                break;
        }
        return true;
    }

    private boolean add(ClipboardData data, boolean withoutNoti, boolean setPrimary, boolean withoutSendingOrigin) {
        boolean result = false;
        try {
            if (ClipboardConstants.DEBUG) {
                Log.v(TAG, "result : " + withoutNoti + ", " + setPrimary + ", " + withoutSendingOrigin);
            }
            if (makeFileDescriptor(data)) {
                if (data != null) {
                    int formatId = data.getFormat();
                    if (withoutSendingOrigin) {
                        if (this.mContext == null || !getService().SetClipboardDataWithoutSendingOrginalClipboard(formatId, data, this.mContext.getOpPackageName())) {
                            result = false;
                        } else {
                            result = true;
                        }
                    } else if (setPrimary) {
                        result = this.mContext != null && getService().SetClipboardData(formatId, data, this.mContext.getOpPackageName());
                    } else {
                        result = getService().SetClipboardDataOriginalToEx(formatId, data);
                    }
                    sendResult(withoutNoti, result);
                    if (data.getParcelFileDescriptor() != null) {
                        data.getParcelFileDescriptor().close();
                        data.setParcelFileDescriptor(null);
                    }
                } else {
                    Log.e(TAG, "addData - clipdata is null!");
                }
                return result;
            }
            Log.e(TAG, "failed making file descriptor!");
            sendResult(withoutNoti, false);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResult(boolean withoutNoti, boolean result) {
        if (!withoutNoti) {
            Message msg = this.mSetDataHandler.obtainMessage();
            if (result) {
                msg.what = 0;
            } else {
                msg.what = 1;
            }
            this.mSetDataHandler.sendMessage(msg);
        }
    }

    private boolean isEnabled(String func) {
        if (isEnabled()) {
            return true;
        }
        Log.d(TAG, "not enabled! from " + func);
        return false;
    }

    public boolean isEnabled() {
        try {
            if (getService() != null) {
                return sService.isEnabled();
            }
            Log.d(TAG, "sService is null!");
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ClipboardExManager(Context context, int containerID) {
    }

    public int getContainerID() {
        return 0;
    }

    public boolean RegistScrapWorkingFormUiInterface(IClipboardWorkingFormUiInterface iRegInterface) {
        return false;
    }
}
