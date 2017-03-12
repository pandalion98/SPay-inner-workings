package android.app.epm;

import android.app.epm.IPluginManagerCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.thememanager.IThemeManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Deprecated
public final class PluginManager {
    private static final boolean DEBUG_ELASTIC = true;
    private static String TAG = "PluginManager";
    private static PluginManager mInstance;
    private static IThemeManager sService;
    private IPluginManagerCallback mCallback = new Stub() {
        public void onInstallProgress(String pkgName, int progress) throws RemoteException {
            Iterator i$ = PluginManager.this.mClientCallback.iterator();
            while (i$.hasNext()) {
                IStatusListener isl = (IStatusListener) i$.next();
                Log.d(PluginManager.TAG, "PluginManager onInstallProgress --- mClientCallback = " + isl + ", progress = " + progress);
                if (isl != null) {
                    isl.onInstallProgress(pkgName, progress);
                }
            }
        }

        public void onInstallCompleted(String pkgName, int errorCode) throws RemoteException {
            PluginManager.this.getComponentPackageMap();
            Iterator i$ = PluginManager.this.mClientCallback.iterator();
            while (i$.hasNext()) {
                IStatusListener isl = (IStatusListener) i$.next();
                Log.d(PluginManager.TAG, "PluginManager onInstallCompleted --- mClientCallback = " + isl);
                if (isl != null) {
                    isl.onInstallCompleted(pkgName, errorCode);
                }
            }
        }

        public void onUninstallProgress(String pkgName, int progress) throws RemoteException {
            Iterator i$ = PluginManager.this.mClientCallback.iterator();
            while (i$.hasNext()) {
                IStatusListener isl = (IStatusListener) i$.next();
                Log.d(PluginManager.TAG, "PluginManager onUninstallProgress --- mClientCallback = " + isl);
                if (isl != null) {
                    isl.onUninstallProgress(pkgName, progress);
                }
            }
        }

        public void onUninstallCompleted(String pkgName, int result) throws RemoteException {
            PluginManager.this.getComponentPackageMap();
            Iterator i$ = PluginManager.this.mClientCallback.iterator();
            while (i$.hasNext()) {
                IStatusListener isl = (IStatusListener) i$.next();
                Log.d(PluginManager.TAG, "PluginManager onUninstallCompleted --- mClientCallback = " + isl);
                if (isl != null) {
                    isl.onUninstallCompleted(pkgName, result);
                }
            }
        }

        public void onStateChangeProgress(String pkgName, int progress) throws RemoteException {
            Iterator i$ = PluginManager.this.mClientCallback.iterator();
            while (i$.hasNext()) {
                IStatusListener isl = (IStatusListener) i$.next();
                Log.d(PluginManager.TAG, "PluginManager onStateChangeProgress --- mClientCallback = " + isl);
                if (isl != null) {
                    isl.onStateChangeProgress(pkgName, progress);
                }
            }
        }

        public void onStateChangeCompleted(String pkgName, String type, int result) throws RemoteException {
            Iterator i$ = PluginManager.this.mClientCallback.iterator();
            while (i$.hasNext()) {
                IStatusListener isl = (IStatusListener) i$.next();
                Log.d(PluginManager.TAG, "PluginManager onStateChangeCompleted --- mClientCallback = " + isl);
                if (isl != null) {
                    isl.onStateChangeCompleted(pkgName, type, result);
                }
            }
        }
    };
    private ArrayList<IStatusListener> mClientCallback;
    private HashMap<String, List<String>> mComponentPackageMap;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            PluginManager.sService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            PluginManager.sService = IThemeManager.Stub.asInterface(service);
        }
    };
    private Context mContext = null;
    private HashMap<String, String> pluginDetails = null;

    public interface IStatusListener {
        void onInstallCompleted(String str, int i);

        void onInstallProgress(String str, int i);

        void onStateChangeCompleted(String str, String str2, int i);

        void onStateChangeProgress(String str, int i);

        void onUninstallCompleted(String str, int i);

        void onUninstallProgress(String str, int i);
    }

    public static IThemeManager getService() {
        Log.i(TAG, "getService -- sService=" + sService);
        if (sService != null) {
            return sService;
        }
        return sService;
    }

    private PluginManager(Context context) {
        this.mContext = context;
        Log.d(TAG, "PluginManager Constructor");
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.samsung.android.themecenter", "com.samsung.android.thememanager.ThemeManagerService"));
        this.mContext.bindService(i, this.mConnection, 1);
    }

    @Deprecated
    public static PluginManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PluginManager(context);
        }
        return mInstance;
    }

    public List<String> getPluginPackageList() {
        getPluginDetailsList(null);
        return new ArrayList(this.pluginDetails.keySet());
    }

    public String getTitle(String packageName) {
        String titleDesc = (String) this.pluginDetails.get(packageName);
        return titleDesc != null ? titleDesc.split("#")[0] : titleDesc;
    }

    public String getDesc(String packageName) {
        String titleDesc = (String) this.pluginDetails.get(packageName);
        return titleDesc != null ? titleDesc.split("#")[1] : titleDesc;
    }

    public boolean getStatus(String packageName) {
        String status = (String) this.pluginDetails.get(packageName);
        return status != null ? Boolean.parseBoolean(status.split("#")[2]) : false;
    }

    public boolean getCategory(String packageName) {
        String status = (String) this.pluginDetails.get(packageName);
        return status != null ? Boolean.parseBoolean(status.split("#")[3]) : false;
    }

    public Drawable getIcon(String packageName) {
        Drawable icon = null;
        try {
            icon = this.mContext.getPackageManager().getApplicationIcon(packageName);
        } catch (Exception e) {
        }
        return icon;
    }

    public void setStatusListener(IStatusListener callback) {
        this.mClientCallback.add(callback);
        Log.d(TAG, "PluginManager setStatusListener -- callback = " + callback);
    }

    private void getPluginDetailsList(String packageName) {
    }

    public void getComponentPackageMap() {
        Log.d(TAG, "PluginManager getThemeList -- SERVICE = " + getService());
        if (getService() != null) {
            try {
                this.mComponentPackageMap = (HashMap) getService().getComponentPackageMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getChineseFestivalList() {
        List<String> tmpChineseList = new ArrayList();
        Log.d(TAG, "PluginManager getThemeList -- SERVICE = " + getService());
        if (getService() != null) {
            try {
                tmpChineseList = getService().getChineseFestivalList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tmpChineseList;
    }

    public ArrayList<String> getThemesForComponent(String compName, int order) {
        ArrayList<String> packageListForComponent = new ArrayList();
        if (this.mComponentPackageMap == null || this.mComponentPackageMap.get(compName) == null) {
            return packageListForComponent;
        }
        return (ArrayList) this.mComponentPackageMap.get(compName);
    }

    private String getTitleFromPackageName(String packageName) {
        if (!(this.mComponentPackageMap == null || packageName == null)) {
            Log.i(TAG, "components = " + this.mComponentPackageMap);
            ArrayList<String> components = (ArrayList) this.mComponentPackageMap.get(PluginManagerConstants.COMPONENT_THEME);
            if (components != null && components.size() > 0) {
                Iterator i$ = components.iterator();
                while (i$.hasNext()) {
                    String s = (String) i$.next();
                    if (packageName.equals(s.split("#")[0])) {
                        return s.split("#")[1];
                    }
                }
            }
        }
        return "Default";
    }

    public void setTimeForMyEvent(String myEventType, String packageName, String startTime, String repeatSchedule) {
        if (getService() != null) {
            try {
                getService().setTimeForMyEvent(myEventType, packageName, startTime, repeatSchedule);
            } catch (Exception e) {
            }
        }
    }

    public void setDeleteMyEvents(ArrayList<String> myEventType, String action) {
        if (getService() != null) {
            try {
                getService().setDeleteMyEvents(myEventType, action);
            } catch (Exception e) {
            }
        }
    }

    public String getVersionForMaster(String packageName) {
        if (this.mComponentPackageMap.get(PluginManagerConstants.COMPONENT_THEME) == null) {
            return null;
        }
        for (String pkg : (List) this.mComponentPackageMap.get(PluginManagerConstants.COMPONENT_THEME)) {
            if (pkg.equals(packageName)) {
                return pkg.split("#")[2];
            }
        }
        return null;
    }

    public String getVersionForThemeFramework() {
        return "1.0.0";
    }

    public void installThemePackage(Uri path, boolean isTrial) {
        if (getService() != null) {
            try {
                getService().installThemePackage(path, isTrial);
            } catch (Exception e) {
            }
        }
    }

    public boolean isOnTrialMode(String packageName) {
        if (getService() != null) {
            try {
                return getService().isOnTrialMode(packageName);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public void removeThemePackage(String packageName) {
        showThemeProgress(packageName, false, 102);
    }

    public void deleteThemePackage(String packageName) {
        if (getService() != null) {
            try {
                getService().removeThemePackage(packageName);
            } catch (Exception e) {
            }
        }
    }

    public int getStateThemePackage(String packageName) {
        int i = -1;
        if (getService() != null) {
            try {
                i = getService().getStateThemePackage(packageName);
            } catch (Exception e) {
            }
        }
        return i;
    }

    public int setStateThemePackage(String packageName, int state) {
        int i = -1;
        if (getService() != null) {
            try {
                i = getService().setStateThemePackage(packageName, state);
            } catch (Exception e) {
            }
        }
        return i;
    }

    public String[] getActiveComponents() {
        Log.d(TAG, "PluginManager getThemeList -- SERVICE = " + getService());
        if (getService() != null) {
            try {
                return getService().getActiveComponents();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public List<String> getActiveMyEvents() {
        Log.d(TAG, "getActiveMyEvents PluginManager getThemeList -- SERVICE = " + getService());
        if (getService() != null) {
            try {
                return getService().getActiveMyEvents();
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Deprecated
    public String getCurrentThemePackage() {
        String pkg = null;
        if (getService() != null) {
            try {
                pkg = getService().getCurrentThemePackage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pkg;
    }

    private String getActiveAppliedPackage() {
        if (getService() != null) {
            try {
                String[] PackageArr = new String[9];
                String currentPackage = getService().getActiveComponents()[0];
                if (currentPackage != null) {
                    return currentPackage.split("#")[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private HashMap<String, String> getLanguagePackList(String packageName) {
        return null;
    }

    public boolean changeThemeState(String packageName, int order, boolean isTrial) {
        Log.d(TAG, "PluginManager changeThemeState -- " + packageName);
        if (getService() != null) {
            try {
                return getService().changeThemeState(packageName, order, isTrial);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean stopTrialThemePackage() {
        Log.d(TAG, "PluginManager stopTrialThemePackage --");
        return applyThemePackage(getActiveAppliedPackage(), false);
    }

    public boolean applyThemePackage(String packageName, boolean isTrial) {
        Log.d(TAG, "PluginManager applyThemePackage -- packageName = " + packageName);
        showThemeProgress(packageName, isTrial, 101);
        return true;
    }

    private void showThemeProgress(String pkg, boolean isTrial, int why) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.samsung.android.themecenter", "com.samsung.android.thememanager.ShowDialogService"));
        intent.putExtra("PACKAGE", pkg);
        intent.putExtra("isTrial", isTrial);
        intent.putExtra("why", why);
        intent.putExtra("TITLE", getTitleFromPackageName(pkg));
        this.mContext.startService(intent);
    }

    public List<Integer> getCategoryList() {
        if (getService() != null) {
            try {
                return getService().getCategoryList();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public List<String> getListByCategory(int category) {
        if (getService() != null) {
            try {
                return getService().getListByCategory(category);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
