package com.samsung.android.contextaware.utilbundle;

import android.os.Bundle;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaAkmManager {
    private static final String SETTING_FILE_NAME = "/data/misc/akmd_set.txt";
    private static volatile CaAkmManager instance;

    public static CaAkmManager getInstance() {
        if (instance == null) {
            synchronized (CaAkmManager.class) {
                if (instance == null) {
                    instance = new CaAkmManager();
                }
            }
        }
        return instance;
    }

    public final String[] getOrientationValueNames() {
        return new String[]{"HSUC_HDST_FORM0", "HSUC_HO_FORM0.x", "HSUC_HO_FORM0.y", "HSUC_HO_FORM0.z", "HFLUCV_HREF_FORM0.x", "HFLUCV_HREF_FORM0.y", "HFLUCV_HREF_FORM0.z"};
    }

    public final Bundle loadOrientationInfo() {
        Bundle loadAkmData = new Bundle();
        for (String i : getOrientationValueNames()) {
            int value = loadIntValue(i);
            loadAkmData.putInt(i, value);
            CaLogger.info(i + " : " + Integer.toString(value));
        }
        return loadAkmData;
    }

    private int loadIntValue(String lpKeyName) {
        return 0;
    }

    public final void saveOrientationInfo(Bundle oriInfo) {
        if (oriInfo == null || oriInfo.isEmpty()) {
            CaLogger.error("can't save the orientation information");
            return;
        }
        for (String i : getOrientationValueNames()) {
            int value = oriInfo.getInt(i);
            saveIntValue(i, value);
            CaLogger.info(i + " : " + Integer.toString(value));
        }
    }

    private void saveIntValue(String lpKeyName, int value) {
    }
}
