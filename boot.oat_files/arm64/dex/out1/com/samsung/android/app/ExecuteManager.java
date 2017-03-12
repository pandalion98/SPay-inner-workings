package com.samsung.android.app;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.app.IExecuteManager.Stub;
import java.util.List;

public class ExecuteManager {
    public static final String EXTRA_EXECUTE_ICON = "com.samsung.android.execute.extra.ICON";
    public static final String EXTRA_EXECUTE_INTENT = "com.samsung.android.execute.extra.INTENT";
    public static final String EXTRA_EXECUTE_NAME = "com.samsung.android.execute.extra.NAME";
    public static final String EXTRA_EXECUTE_SMALL_ICON = "com.samsung.android.execute.extra.SMALLICON";
    private static final String TAG = "ExecuteManager";
    private static IExecuteManager mService;
    private final Context mContext;

    public ExecuteManager(Context context) {
        this.mContext = context;
        mService = Stub.asInterface(ServiceManager.getService("execute"));
    }

    public ExecutableInfo getExecutableInfo(String id) {
        try {
            return mService.getExecutableInfo(id);
        } catch (Exception ex) {
            Log.e(TAG, "getExecutableInfo() failed: " + ex);
            return null;
        }
    }

    public List<ExecutableInfo> getExecutableInfos() {
        try {
            return mService.getExecutableInfos();
        } catch (Exception ex) {
            Log.e(TAG, "getExecutableInfo() failed: " + ex);
            return null;
        }
    }
}
