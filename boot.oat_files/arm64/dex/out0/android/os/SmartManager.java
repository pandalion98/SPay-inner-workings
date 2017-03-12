package android.os;

import android.content.Context;
import android.os.ICustomFrequencyManager.Stub;

public class SmartManager {
    private static ICustomFrequencyManager mService;

    private SmartManager() {
    }

    private static synchronized ICustomFrequencyManager getService(Context context) {
        ICustomFrequencyManager iCustomFrequencyManager;
        synchronized (SmartManager.class) {
            if (mService == null) {
                IBinder b = ServiceManager.getService(Context.CFMS_SERVICE);
                if (b != null) {
                    mService = Stub.asInterface(b);
                }
            }
            iCustomFrequencyManager = mService;
        }
        return iCustomFrequencyManager;
    }

    public static int getBatteryRemainingUsageTime(Context context, int mode) {
        ICustomFrequencyManager svc = getService(context);
        int remainTime = 0;
        if (svc != null) {
            try {
                remainTime = svc.getBatteryRemainingUsageTime(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return remainTime;
    }

    public static String[] getFrequentlyUsedAppListByLocation(Context context, double latitude, double longitude, int numOfItems) {
        ICustomFrequencyManager svc = getService(context);
        String[] result = null;
        if (svc != null) {
            try {
                result = svc.getFrequentlyUsedAppListByLocation(latitude, longitude, numOfItems);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String[] getLeastRecentlyUsedAppList(Context context, int numOfItems) {
        ICustomFrequencyManager svc = getService(context);
        String[] result = null;
        if (svc != null) {
            try {
                result = svc.getLeastRecentlyUsedAppList(numOfItems);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
