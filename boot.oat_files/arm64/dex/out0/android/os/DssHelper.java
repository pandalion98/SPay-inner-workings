package android.os;

import android.util.Slog;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class DssHelper {
    static final String TAG = "DssHelper";
    private static DssHelper mInstance = null;
    private final float DEFAULT_FACTOR = 0.5f;
    HashMap<String, DssAppDate> mWhiteAdssList = new HashMap();
    HashMap<String, DssAppDate> mWhiteList = new HashMap();

    private class DssAppDate {
        public boolean mEnableSIOP = true;
        public float mScale = 1.0f;

        DssAppDate() {
        }

        void addPackage(float scalingFactor) {
            this.mScale = scalingFactor;
            this.mEnableSIOP = true;
        }

        void addPackage(float scalingFactor, boolean enableSIOP) {
            this.mScale = scalingFactor;
            this.mEnableSIOP = enableSIOP;
        }
    }

    private DssHelper() {
    }

    public static synchronized DssHelper getInstance() {
        DssHelper dssHelper;
        synchronized (DssHelper.class) {
            if (mInstance == null) {
                mInstance = new DssHelper();
            }
            dssHelper = mInstance;
        }
        return dssHelper;
    }

    public synchronized void addPackage(String packageName, float scalingFactor) {
        if (scalingFactor == 1.0f) {
            removePackage(packageName);
        } else {
            addPackageData(packageName, scalingFactor);
        }
    }

    public synchronized void addPackageByTransact(String packageName, float scalingFactor) {
        if (scalingFactor == 1.0f) {
            removePackage(packageName);
        } else {
            addPackageData(packageName, scalingFactor);
        }
    }

    public synchronized void addAdssPackageByTransact(String packageName, float scalingFactor) {
        DssAppDate dssAppData = new DssAppDate();
        dssAppData.addPackage(scalingFactor);
        this.mWhiteAdssList.put(packageName, dssAppData);
    }

    public synchronized boolean isPackageExist(String packageName) {
        boolean result;
        result = false;
        if (this.mWhiteAdssList.containsKey(packageName)) {
            result = true;
        } else if (this.mWhiteList.containsKey(packageName)) {
            result = true;
        }
        return result;
    }

    public synchronized boolean isAdss(String packageName) {
        boolean z;
        if (this.mWhiteAdssList.containsKey(packageName)) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized boolean isSIOP(String packageName) {
        boolean z;
        if (this.mWhiteList.containsKey(packageName)) {
            z = ((DssAppDate) this.mWhiteList.get(packageName)).mEnableSIOP;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized void addPackageData(String packageName, float scalingFactor) {
        DssAppDate dssAppData = new DssAppDate();
        dssAppData.addPackage(scalingFactor);
        this.mWhiteList.put(packageName, dssAppData);
    }

    public synchronized void addPackageData(String packageName, float scalingFactor, boolean enableSIOP) {
        DssAppDate dssAppData = new DssAppDate();
        dssAppData.addPackage(scalingFactor, enableSIOP);
        this.mWhiteList.put(packageName, dssAppData);
    }

    public synchronized void removePackage(String packageName) {
        if (this.mWhiteList.containsKey(packageName)) {
            this.mWhiteList.remove(packageName);
        }
    }

    public synchronized void removeAdssPackage(String packageName) {
        if (this.mWhiteAdssList.containsKey(packageName)) {
            this.mWhiteAdssList.remove(packageName);
        }
    }

    public synchronized float getScalingFactor() {
        return 0.5f;
    }

    public synchronized float getScalingFactor(String packageName) {
        float f;
        if (this.mWhiteAdssList.containsKey(packageName)) {
            f = ((DssAppDate) this.mWhiteAdssList.get(packageName)).mScale;
        } else if (this.mWhiteList.containsKey(packageName)) {
            f = ((DssAppDate) this.mWhiteList.get(packageName)).mScale;
        } else {
            f = 0.5f;
        }
        return f;
    }

    public synchronized void applySetFixedSize(String packageName, SurfaceView surface) {
        if (surface != null) {
            float resolutionFactorf = getScalingFactor(packageName);
            float width = ((float) surface.getWidth()) * resolutionFactorf;
            float height = ((float) surface.getHeight()) * resolutionFactorf;
            if (!(width == 0.0f || height == 0.0f)) {
                surface.getHolder().setFixedSize((int) width, (int) height);
            }
        }
    }

    public synchronized float getScalingFactorAdss(String packageName) {
        float f;
        if (this.mWhiteAdssList.containsKey(packageName)) {
            if (this.mWhiteList.containsKey(packageName)) {
                f = ((DssAppDate) this.mWhiteList.get(packageName)).mScale;
            } else {
                f = ((DssAppDate) this.mWhiteAdssList.get(packageName)).mScale;
            }
        } else if (this.mWhiteList.containsKey(packageName)) {
            f = ((DssAppDate) this.mWhiteList.get(packageName)).mScale;
        } else {
            f = 0.5f;
        }
        return f;
    }

    public synchronized void showAllDSSInfo() {
        for (Entry<String, DssAppDate> elem : this.mWhiteList.entrySet()) {
            Slog.i(TAG, "DSS PackageName : " + ((String) elem.getKey()) + ",  Scale : " + ((DssAppDate) elem.getValue()).mScale + ",  SIOP : " + ((DssAppDate) elem.getValue()).mEnableSIOP);
        }
    }

    public StringBuilder getWhiteList() {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> dss0_5List = new ArrayList();
        ArrayList<String> dss0_75List = new ArrayList();
        for (Entry<String, DssAppDate> elem : this.mWhiteList.entrySet()) {
            if (((DssAppDate) elem.getValue()).mScale == 0.5f) {
                dss0_5List.add(elem.getKey());
            } else if (((DssAppDate) elem.getValue()).mScale == 0.75f) {
                dss0_75List.add(elem.getKey());
            }
        }
        sb.append("DSS 0.5 Group\n");
        sb.append("=============\n");
        Iterator i$ = dss0_5List.iterator();
        while (i$.hasNext()) {
            sb.append((String) i$.next());
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("DSS 0.75 Group\n");
        sb.append("=============\n");
        i$ = dss0_75List.iterator();
        while (i$.hasNext()) {
            sb.append((String) i$.next());
            sb.append("\n");
        }
        sb.append("\n");
        return sb;
    }
}
