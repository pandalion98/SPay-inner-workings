package android.os;

import android.net.ProxyInfo;
import android.util.Log;
import java.util.HashMap;
import java.util.Map.Entry;

public class DTSHelper {
    private static final double SCALE_100 = 1.0d;
    private static final double SCALE_25 = 0.25d;
    private static final double SCALE_50 = 0.5d;
    private static final double SCALE_75 = 0.75d;
    private static final String TAG = "DTSHelper";
    private static DTSHelper mInstance = null;
    HashMap<String, Double> dtsWhiteList = new HashMap();

    private DTSHelper() {
    }

    public static synchronized DTSHelper getInstance() {
        DTSHelper dTSHelper;
        synchronized (DTSHelper.class) {
            if (mInstance == null) {
                mInstance = new DTSHelper();
            }
            dTSHelper = mInstance;
        }
        return dTSHelper;
    }

    public synchronized void addPackage(String packageName, double scalingFactor) {
        if (scalingFactor == SCALE_100) {
            if (this.dtsWhiteList.containsKey(packageName)) {
                this.dtsWhiteList.remove(packageName);
            }
        } else if (scalingFactor == SCALE_25 || scalingFactor == SCALE_50 || scalingFactor == SCALE_75) {
            this.dtsWhiteList.put(packageName, Double.valueOf(scalingFactor));
        } else {
            Log.w(TAG, "DTS_WARNING : Adding Package : " + packageName + " with invalid ScalingFactor : " + scalingFactor);
        }
    }

    public synchronized boolean isPackageExist(String packageName) {
        return this.dtsWhiteList.containsKey(packageName);
    }

    public synchronized void removePackage(String packageName) {
        if (isPackageExist(packageName)) {
            this.dtsWhiteList.remove(packageName);
        }
    }

    public synchronized double getScalingFactor(String packageName) {
        double doubleValue;
        if (this.dtsWhiteList.containsKey(packageName)) {
            doubleValue = ((Double) this.dtsWhiteList.get(packageName)).doubleValue();
        } else {
            doubleValue = SCALE_100;
        }
        return doubleValue;
    }

    public StringBuilder getWhiteList() {
        String packageNameWithValue = ProxyInfo.LOCAL_EXCL_LIST;
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Double> elem : this.dtsWhiteList.entrySet()) {
            if (((Double) elem.getValue()).doubleValue() >= 0.0d) {
                sb.append(((String) elem.getKey()).concat(":" + String.valueOf(elem.getValue())));
                sb.append("\n");
            }
        }
        return sb;
    }

    public synchronized void showAllDTSInfo() {
        for (Entry<String, Double> elem : this.dtsWhiteList.entrySet()) {
            Log.i(TAG, "DTS PackageName : " + ((String) elem.getKey()) + ",  DTSValue : " + elem.getValue() + ProxyInfo.LOCAL_EXCL_LIST);
        }
    }
}
