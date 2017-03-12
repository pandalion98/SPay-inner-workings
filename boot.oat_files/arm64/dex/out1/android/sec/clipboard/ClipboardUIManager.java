package android.sec.clipboard;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.sec.clipboard.IClipboardUIManager.Stub;
import android.sec.clipboard.data.ClipboardDefine;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;

public class ClipboardUIManager {
    private static final String TAG = "ClipboardUIManager";
    private static IClipboardUIManager mCBUIManager;
    private static ClipboardUIManager sInstance = null;
    BaseAdapter mAdapter = null;
    private ClipboardExManager mClipboardEx = null;
    private Context mContext;
    private Handler mHandler = new Handler();
    private Runnable mRecursiveCall = new Runnable() {
        public void run() {
            if (ClipboardUIManager.this.mClipboardEx == null) {
                ClipboardUIManager.this.mClipboardEx = (ClipboardExManager) ClipboardUIManager.this.mContext.getSystemService("clipboardEx");
            }
            ClipboardUIManager.this.mClipboardEx.showUIDataDialog();
        }
    };
    private int mViewID = 0;

    public ClipboardUIManager(View parentView) {
        initVariable();
    }

    public ClipboardUIManager(Context context) {
        this.mContext = context;
    }

    private void initVariable() {
        mCBUIManager = null;
    }

    private boolean setupInRuntime() {
        mCBUIManager = Stub.asInterface(ServiceManager.getService("clipboarduiservice"));
        if (mCBUIManager != null) {
            return true;
        }
        Log.e(TAG, "Failed to get ClipboardService");
        return false;
    }

    public void finish() {
        Log.d(TAG, "Finish ClipboardService");
        finish(true);
    }

    public void finish(boolean immediate) {
        Log.d(TAG, "Finish : " + immediate);
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
        closeClopboardUI(immediate);
        mCBUIManager = null;
    }

    private boolean showClipBoard(int type, IClipboardDataPasteEvent clPasteEvent) {
        Log.d(TAG, "ClipboardUIManager showClipBoard()");
        if (setupInRuntime()) {
            boolean result;
            setPasteTargetViewInfo(type, clPasteEvent);
            try {
                mCBUIManager.show(this.mViewID, null);
                result = true;
            } catch (RemoteException e1) {
                e1.printStackTrace();
                result = false;
            }
            return result;
        }
        if (!this.mHandler.hasCallbacks(this.mRecursiveCall)) {
            this.mHandler.postDelayed(this.mRecursiveCall, 100);
        }
        return false;
    }

    private boolean closeClopboardUI(boolean immediate) {
        if (setupInRuntime()) {
            try {
                if (mCBUIManager != null) {
                    mCBUIManager.dismiss(this.mViewID, immediate);
                    mCBUIManager = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void show(int type, IClipboardDataPasteEvent clPasteEvent) {
        Log.d(TAG, "show()");
        showClipBoard(type, clPasteEvent);
    }

    public void dismiss() {
        Log.d(TAG, "dismiss()");
        closeClopboardUI(false);
    }

    public boolean isShowing() {
        Log.d(TAG, "ClipboardUIManager isShowingClipBoard()");
        if (!setupInRuntime()) {
            return false;
        }
        boolean result;
        try {
            result = mCBUIManager.isShowing();
        } catch (RemoteException e1) {
            e1.printStackTrace();
            result = false;
        }
        return result;
    }

    public void setPasteTargetViewInfo(int type, IClipboardDataPasteEvent clPasteEvent) {
        if (ClipboardDefine.DEBUG) {
            Log.i(TAG, "ClipboardUIManager > setPasteTargetViewInfo - " + clPasteEvent + ", " + type);
        }
        try {
            if (mCBUIManager != null) {
                mCBUIManager.setPasteTargetViewType(type, clPasteEvent);
            } else {
                Log.i(TAG, "mCBUIManager is null!!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ClipboardUIManager getInstance(Context mContext) {
        if (sInstance == null) {
            sInstance = new ClipboardUIManager(mContext);
        }
        return sInstance;
    }

    public void hideFloatingIconForScrap() {
    }

    public void sendCropRectForAnimation(Rect cropRect, boolean showAni) {
    }
}
