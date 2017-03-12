package com.samsung.android.multidisplay.dualscreen;

import android.content.res.Resources;
import android.util.Log;
import android.util.Singleton;
import com.android.internal.R;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class DualScreenConfigs extends Singleton<DualScreenConfigs> {
    public static final String TAG = "DualScreenConfigs";
    private static ArrayList<String> mOppositeLaunchAppList = new ArrayList();
    private static ArrayList<String> mSamsungHomeLaunchAppList = new ArrayList();
    private static Resources mSystemResources;
    private static DualScreenConfigs sInstance = new DualScreenConfigs();

    public static DualScreenConfigs getInstance() {
        return (DualScreenConfigs) sInstance.get();
    }

    protected DualScreenConfigs create() {
        Log.d(TAG, "DualScreenConfigs :: create()");
        mSystemResources = Resources.getSystem();
        if (mSystemResources == null) {
            throw new RuntimeException("System Resources is not ready.");
        }
        initConfig();
        return sInstance;
    }

    public static boolean isOppositeLaunchSupportApp(String pkgName) {
        return mOppositeLaunchAppList.contains(pkgName);
    }

    public boolean isSamsungHomeLaunchSupportApp(String pname) {
        return mSamsungHomeLaunchAppList.contains(pname);
    }

    public ArrayList<String> getSamsungHomeLaunchSupportAppList() {
        return mSamsungHomeLaunchAppList;
    }

    public void addSamsungHomeLaunchSupportAppList(String str) {
        mSamsungHomeLaunchAppList.add(str);
    }

    public void removeSamsungHomeLaunchSupportAppList(String str) {
        for (int i = 0; i < mSamsungHomeLaunchAppList.size(); i++) {
            if (((String) mSamsungHomeLaunchAppList.get(i)).equals(str)) {
                mSamsungHomeLaunchAppList.remove(i);
            }
        }
    }

    public static void dump(String prefix) {
        PrintWriter pw = new PrintWriter(new StringWriter());
        pw.print(prefix);
        pw.println("DUALSCREEN MANAGER configurations (dumpsys dualscreen config)");
        String localPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("Opposite launch app list :");
        int oppositeLaunchListCount = mOppositeLaunchAppList.size();
        Iterator i$ = mOppositeLaunchAppList.iterator();
        while (i$.hasNext()) {
            String pkgName = (String) i$.next();
            pw.print(localPrefix);
            pw.println("[" + 0 + "] " + pkgName);
        }
        int samsungHomeLaunchListCount = mSamsungHomeLaunchAppList.size();
        i$ = mOppositeLaunchAppList.iterator();
        while (i$.hasNext()) {
            pkgName = (String) i$.next();
            pw.print(localPrefix);
            pw.println("[" + 0 + "] " + pkgName);
        }
    }

    private DualScreenConfigs() {
    }

    private void initConfig() {
        loadResourcesConfig();
        loadSecProductFeature();
        loadCscFeatures();
    }

    private void loadResourcesConfig() {
        loadStringArray(R.array.config_dualscreen_opposite_launch_app_list, mOppositeLaunchAppList);
        loadStringArray(R.array.config_dualscreen_samsung_home_app_list, mSamsungHomeLaunchAppList);
    }

    private void loadStringArray(int resId, ArrayList<String> outArray) {
        outArray.clear();
        String[] strings = mSystemResources.getStringArray(resId);
        if (strings != null) {
            for (String string : strings) {
                outArray.add(string);
            }
        }
    }

    private void loadSecProductFeature() {
    }

    private void loadCscFeatures() {
    }
}
