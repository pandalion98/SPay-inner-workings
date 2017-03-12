package android.os;

import android.net.ProxyInfo;
import android.util.Log;
import android.util.Slog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class DcsHelper {
    private static final String TAG_DCS = "DCSHelper";
    private static final boolean isEngBinary = "eng".equals(Build.TYPE);
    private static DcsHelper mInstance = null;
    private HashMap<String, Integer> mWhiteList = new HashMap();

    private DcsHelper() {
    }

    public static synchronized DcsHelper getInstance() {
        DcsHelper dcsHelper;
        synchronized (DcsHelper.class) {
            if (mInstance == null) {
                mInstance = new DcsHelper();
            }
            dcsHelper = mInstance;
        }
        return dcsHelper;
    }

    public synchronized void addPackage(String packageName, Integer format) {
        this.mWhiteList.put(packageName, format);
    }

    public synchronized boolean isPackageExist(String packageName) {
        return this.mWhiteList.containsKey(packageName);
    }

    public synchronized Integer getPixelFormat(String packageName) {
        return (Integer) this.mWhiteList.get(packageName);
    }

    public synchronized void clear() {
        this.mWhiteList.clear();
    }

    public StringBuilder getWhiteList() {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> TempList = new ArrayList();
        for (Entry<String, Integer> elem : this.mWhiteList.entrySet()) {
            if (((Integer) elem.getValue()).intValue() == 4) {
                TempList.add(elem.getKey());
            }
        }
        sb.append("RGB_565 Group\n");
        sb.append("=============\n");
        Iterator i$ = TempList.iterator();
        while (i$.hasNext()) {
            sb.append((String) i$.next());
            sb.append("\n");
        }
        sb.append("\n");
        TempList.clear();
        for (Entry<String, Integer> elem2 : this.mWhiteList.entrySet()) {
            if (((Integer) elem2.getValue()).intValue() == 6) {
                TempList.add(elem2.getKey());
            }
        }
        sb.append("RGBA_5551List Group\n");
        sb.append("=============\n");
        i$ = TempList.iterator();
        while (i$.hasNext()) {
            sb.append((String) i$.next());
            sb.append("\n");
        }
        sb.append("\n");
        TempList.clear();
        for (Entry<String, Integer> elem22 : this.mWhiteList.entrySet()) {
            if (((Integer) elem22.getValue()).intValue() == 7) {
                TempList.add(elem22.getKey());
            }
        }
        sb.append("RGBA_4444List Group\n");
        sb.append("=============\n");
        i$ = TempList.iterator();
        while (i$.hasNext()) {
            sb.append((String) i$.next());
            sb.append("\n");
        }
        sb.append("\n");
        return sb;
    }

    public static synchronized void logOnEng(String tag, String msg) {
        synchronized (DcsHelper.class) {
            if (isEngBinary) {
                Log.i(tag, msg);
            }
        }
    }

    public synchronized void showAllDCSInfo() {
        for (Entry<String, Integer> elem : this.mWhiteList.entrySet()) {
            Slog.i(TAG_DCS, "DCS PackageName : " + ((String) elem.getKey()) + ",  ColorFormat : " + elem.getValue() + ProxyInfo.LOCAL_EXCL_LIST);
        }
    }
}
