package com.samsung.android.sdk.look;

import android.app.ActivityThread;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import com.samsung.android.cocktailbar.CocktailBarManager;
import java.util.List;

public class SlookImpl {
    private static final int AIRBUTTON = 1;
    private static final int COCKTAIL_BAR = 6;
    private static final int COCKTAIL_PANEL = 7;
    public static final boolean DEBUG = true;
    private static final int SDK_INT = SystemProperties.getInt("ro.slook.ver", 0);
    private static final int SMARTCLIP = 2;
    private static final int SPEN_HOVER_ICON = 4;
    private static final int WRITINGBUDDY = 3;
    private static int sCocktailLevel = -1;
    private static int sHasMetaEdgeSingle = -1;
    private static int sUspLevel = -1;

    public static class VERSION_CODES {
        public static final int L1 = 1;
        public static final int L2 = 2;
    }

    public static int getVersionCode() {
        return SDK_INT;
    }

    public static boolean isFeatureEnabled(int type) {
        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
                if (sUspLevel == -1) {
                    IPackageManager pm = ActivityThread.getPackageManager();
                    if (pm != null) {
                        try {
                            sUspLevel = pm.getSystemFeatureLevel("com.sec.feature.spen_usp");
                        } catch (RemoteException e) {
                            throw new RuntimeException("Package manager has died", e);
                        }
                    }
                }
                if (type == 1) {
                    if (sUspLevel < 2 || sUspLevel > 3) {
                        return false;
                    }
                    return true;
                } else if (sUspLevel < 2) {
                    return false;
                } else {
                    return true;
                }
            case 6:
                checkCocktailLevel();
                if (sCocktailLevel > 0 && sCocktailLevel <= type) {
                    return true;
                }
                if (sCocktailLevel <= 0) {
                    return false;
                }
                checkValidCocktailMetaData();
                if (sHasMetaEdgeSingle != 1) {
                    return false;
                }
                return true;
            case 7:
                checkCocktailLevel();
                return sCocktailLevel > 0 && sCocktailLevel <= type;
            default:
                return false;
        }
    }

    private static void checkCocktailLevel() {
        int i = 0;
        if (sCocktailLevel == -1) {
            IPackageManager pm = ActivityThread.getPackageManager();
            if (pm != null) {
                try {
                    sCocktailLevel = pm.hasSystemFeature("com.sec.feature.cocktailbar") ? 6 : 0;
                    if (sCocktailLevel == 0) {
                        if (pm.hasSystemFeature("com.sec.feature.cocktailpanel")) {
                            i = 7;
                        }
                        sCocktailLevel = i;
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException("Package manager has died", e);
                }
            }
        }
    }

    private static void checkValidCocktailMetaData() {
        int N = 0;
        if (sHasMetaEdgeSingle == -1) {
            sHasMetaEdgeSingle = 0;
            IPackageManager pm = ActivityThread.getPackageManager();
            String packageName = ActivityThread.currentOpPackageName();
            if (pm != null && packageName != null) {
                try {
                    ApplicationInfo ai = pm.getApplicationInfo(packageName, 128, UserHandle.myUserId());
                    if (ai != null) {
                        String value;
                        Bundle metaData = ai.metaData;
                        if (metaData != null) {
                            value = metaData.getString("com.samsung.android.cocktail.mode", "");
                            if (value != null && value.equals("edge_single")) {
                                sHasMetaEdgeSingle = 1;
                            }
                        }
                        if (sHasMetaEdgeSingle == 0) {
                            Intent intent = new Intent(CocktailBarManager.ACTION_COCKTAIL_UPDATE);
                            intent.setPackage(packageName);
                            List<ResolveInfo> broadcastReceivers = pm.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(ActivityThread.currentApplication().getContentResolver()), 128, UserHandle.myUserId());
                            if (broadcastReceivers != null) {
                                N = broadcastReceivers.size();
                            }
                            for (int i = 0; i < N; i++) {
                                ActivityInfo activityInfo = ((ResolveInfo) broadcastReceivers.get(i)).activityInfo;
                                if ((activityInfo.applicationInfo.flags & 262144) == 0 && packageName.equals(activityInfo.packageName)) {
                                    metaData = activityInfo.metaData;
                                    if (metaData != null) {
                                        value = metaData.getString("com.samsung.android.cocktail.mode", "");
                                        if (value != null && value.equals("edge_single")) {
                                            sHasMetaEdgeSingle = 1;
                                            return;
                                        }
                                    }
                                    continue;
                                }
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
