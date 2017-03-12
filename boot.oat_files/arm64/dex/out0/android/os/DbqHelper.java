package android.os;

import android.util.Log;
import android.util.Slog;
import java.util.HashMap;
import java.util.Map.Entry;

public class DbqHelper {
    private static final String TAG_DBQ = "DbqHelper";
    private static final boolean isEngBinary = "eng".equals(Build.TYPE);
    private static DbqHelper mInstance = null;
    private HashMap<String, DbqInfo> mWhiteList = new HashMap();

    class DbqInfo {
        public int bufferCount;
        public Boolean isActEnabled;
        public Boolean isSvEnabled;

        public DbqInfo(int bufferCount, boolean actEnable, boolean svEnable) {
            this.bufferCount = bufferCount;
            this.isActEnabled = Boolean.valueOf(actEnable);
            this.isSvEnabled = Boolean.valueOf(svEnable);
        }
    }

    private DbqHelper() {
    }

    public static synchronized DbqHelper getInstance() {
        DbqHelper dbqHelper;
        synchronized (DbqHelper.class) {
            if (mInstance == null) {
                mInstance = new DbqHelper();
            }
            dbqHelper = mInstance;
        }
        return dbqHelper;
    }

    public synchronized void addPackage(String packageName, Integer count, Boolean actEnable, Boolean svEnable) {
        this.mWhiteList.put(packageName, new DbqInfo(count.intValue(), actEnable.booleanValue(), svEnable.booleanValue()));
    }

    public synchronized boolean isPackageExist(String packageName) {
        return this.mWhiteList.containsKey(packageName);
    }

    public synchronized Integer getDbqCount(String packageName) {
        return Integer.valueOf(((DbqInfo) this.mWhiteList.get(packageName)).bufferCount);
    }

    public synchronized boolean checkDbqEnabledforAct(String packageName) {
        return ((DbqInfo) this.mWhiteList.get(packageName)).isActEnabled.booleanValue();
    }

    public synchronized boolean checkDbqEnabledforSV(String packageName) {
        return ((DbqInfo) this.mWhiteList.get(packageName)).isSvEnabled.booleanValue();
    }

    public static void logOnEng(String tag, String msg) {
        if (isEngBinary) {
            Log.i(tag, msg);
        }
    }

    public synchronized void showAllDBQInfo() {
        for (Entry<String, DbqInfo> elem : this.mWhiteList.entrySet()) {
            Slog.i(TAG_DBQ, "DBQ PackageName : " + ((String) elem.getKey()) + ",  DBQValues : " + ((DbqInfo) elem.getValue()).bufferCount + ", " + ((DbqInfo) elem.getValue()).isActEnabled + ", " + ((DbqInfo) elem.getValue()).isSvEnabled);
        }
    }
}
