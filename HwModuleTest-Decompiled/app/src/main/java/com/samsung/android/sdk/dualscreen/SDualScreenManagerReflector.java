package com.samsung.android.sdk.dualscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity.DualScreen;
import com.samsung.android.sdk.dualscreen.SDualScreenListener.ScreenChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

class SDualScreenManagerReflector extends SDualScreenReflector {
    private static final boolean DEBUG = true;
    static String[] DUALSCREEN_ENUM_FIELD_NAMES = {"MAIN", "SUB", "EXPANDED", "UNKNOWN"};
    static int[] ORDINALS = new int[DUALSCREEN_ENUM_FIELD_NAMES.length];
    private static final String TAG = SDualScreenManagerReflector.class.getSimpleName();
    private static Class<?> sKlassCoupledTaskInfo;
    private static Class<?> sKlassDualScreen;
    private static Class<?> sKlassDualScreenManager;
    static Method sMethodEnumOrdinal;
    private Context mContext;
    private boolean mInitialized = false;
    private Object mInstanceActivity;
    private Object mInstanceDualScreenManager;

    static {
        loadKlass();
    }

    public SDualScreenManagerReflector(Context context) {
        if (context != null) {
            try {
                loadKlass();
                this.mInstanceDualScreenManager = sKlassDualScreenManager.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
                this.mContext = context;
                if (context instanceof Activity) {
                    Class<?> activityClass = ((Activity) context).getClass();
                    this.mInstanceActivity = context;
                    putMethod(activityClass, "setScreenChangedListner", new Class[]{ScreenChangeListener.class});
                }
                this.mInitialized = DEBUG;
                Log.d(TAG, "completely initialized");
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "NoSuchMethodException !");
                e.printStackTrace();
            } catch (InstantiationException e2) {
                Log.e(TAG, "InstantiationException !");
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                Log.e(TAG, "IllegalAccessException !");
                e3.printStackTrace();
            } catch (IllegalArgumentException e4) {
                Log.e(TAG, "IllegalArgumentException !");
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("InvocationTargetException ! cause=");
                sb.append(e5.getCause());
                Log.e(str, sb.toString());
                e5.printStackTrace();
            }
        } else {
            throw new NullPointerException("context is null");
        }
    }

    static void loadKlass() {
        Method[] arr$;
        Method[] arr$2;
        if (sKlassDualScreenManager == null) {
            try {
                sKlassDualScreenManager = Class.forName("com.samsung.android.dualscreen.DualScreenManager");
                for (Method m : sKlassDualScreenManager.getMethods()) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("DualScreenManager : available method => ");
                    sb.append(m);
                    Log.d(str, sb.toString());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "loadKlass() : DualScreen SDK version (0.3)");
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("loadKlass() : Device supports DualScreen version (");
            sb2.append(getVersionName());
            sb2.append(")");
            Log.d(str2, sb2.toString());
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("loadKlass() : Device supports Minimum SDK version (");
            sb3.append(getMinimumSdkVersionName());
            sb3.append(")");
            Log.d(str3, sb3.toString());
            Class[] clsArr = null;
            putMethod(sKlassDualScreenManager, "getScreen", clsArr);
            putMethod(sKlassDualScreenManager, "getScreen", new Class[]{Integer.TYPE});
            putMethod(sKlassDualScreenManager, "moveToScreen", new Class[]{sKlassDualScreen});
            putMethod(sKlassDualScreenManager, "switchScreen", clsArr);
            putMethod(sKlassDualScreenManager, "swapTopTask", clsArr);
            putMethod(sKlassDualScreenManager, "getCoupledTaskInfo", clsArr);
            checkVersion();
        }
        loadDualScreenEnumKlass();
        if (sKlassCoupledTaskInfo == null) {
            try {
                sKlassCoupledTaskInfo = Class.forName("com.samsung.android.dualscreen.CoupledTaskInfo");
                for (Method m2 : sKlassCoupledTaskInfo.getMethods()) {
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("CoupledTaskInfo : available method => ");
                    sb4.append(m2);
                    Log.d(str4, sb4.toString());
                }
                putMethod(sKlassCoupledTaskInfo, "getTaskId", null);
                putMethod(sKlassCoupledTaskInfo, "getCoupledTaskMode", null);
                putMethod(sKlassCoupledTaskInfo, "getParentCoupledTaskId", null);
                putMethod(sKlassCoupledTaskInfo, "getChildCoupledTaskId", null);
                putMethod(sKlassCoupledTaskInfo, "isCoupled", null);
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
        }
    }

    static void loadDualScreenEnumKlass() {
        Method[] arr$;
        Field[] arr$2;
        if (sKlassDualScreen == null) {
            try {
                sKlassDualScreen = Class.forName("com.samsung.android.dualscreen.DualScreen");
                if (sKlassDualScreen.isEnum()) {
                    Log.d(TAG, String.format(Locale.US, "Enum name:  %s%nEnum constants:  %s%n", new Object[]{sKlassDualScreen.getName(), Arrays.asList(sKlassDualScreen.getEnumConstants())}));
                }
                for (Method m : sKlassDualScreen.getMethods()) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("DualScreen : available method => ");
                    sb.append(m);
                    Log.d(str, sb.toString());
                }
                for (Field f : sKlassDualScreen.getDeclaredFields()) {
                    if (f.isEnumConstant()) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("DualScreen : available fields => ");
                        sb2.append(f.getName());
                        sb2.append(" (enum constant)");
                        Log.d(str2, sb2.toString());
                    } else {
                        String str3 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("DualScreen : available fields => ");
                        sb3.append(f.getName());
                        Log.d(str3, sb3.toString());
                    }
                }
                int N = DUALSCREEN_ENUM_FIELD_NAMES.length;
                for (int i = 0; i < N; i++) {
                    Field src = sKlassDualScreen.getDeclaredField(DUALSCREEN_ENUM_FIELD_NAMES[i]);
                    Log.d(TAG, "loadDualScreenEnums() : ===================================");
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("loadDualScreenEnums() : DUALSCREEN_ENUM_FIELD_NAMES[");
                    sb4.append(i);
                    sb4.append("]=");
                    sb4.append(src);
                    Log.d(str4, sb4.toString());
                    int constansLenth = sKlassDualScreen.getEnumConstants().length;
                    for (int j = 0; j < constansLenth; j++) {
                        Object o = sKlassDualScreen.getEnumConstants()[j];
                        boolean same = o.toString().equals(DUALSCREEN_ENUM_FIELD_NAMES[i]);
                        String str5 = TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("loadDualScreenEnums() : o=");
                        sb5.append(o);
                        sb5.append(", same=");
                        sb5.append(same);
                        sb5.append(" index=");
                        sb5.append(j);
                        Log.d(str5, sb5.toString());
                        if (same) {
                            ORDINALS[i] = j;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "loadDualScreenEnums : ClassNotFoundException !");
                e.printStackTrace();
            } catch (NoSuchFieldException e2) {
                Log.e(TAG, "loadDualScreenEnums : NoSuchFieldException !");
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                Log.e(TAG, "loadDualScreenEnums : IllegalArgumentException !");
                e3.printStackTrace();
            }
        }
        if (sMethodEnumOrdinal == null) {
            try {
                sMethodEnumOrdinal = sKlassDualScreen.getMethod("ordinal", null);
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
            } catch (IllegalArgumentException e5) {
                e5.printStackTrace();
            }
        }
    }

    static Object convertToNativeDualScreenEnum(DualScreen screen) {
        loadKlass();
        if (screen != null) {
            return sKlassDualScreen.getEnumConstants()[screen.targetOrdinal];
        }
        return null;
    }

    static DualScreen convertToSdkDualScreenEnum(Object screen) {
        DualScreen[] arr$;
        loadKlass();
        if (screen != null) {
            int ordinal = -1;
            try {
                ordinal = ((Integer) sMethodEnumOrdinal.invoke(screen, new Object[0])).intValue();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
            for (DualScreen o : DualScreen.values()) {
                if (o.targetOrdinal == ordinal) {
                    return o;
                }
            }
        }
        return DualScreen.UNKNOWN;
    }

    static SDualScreenCoupledTaskInfo convertToSdkCoupledTaskInfo(Object coupledTaskInfo) {
        loadKlass();
        SDualScreenCoupledTaskInfo sdualScreenCoupledTaskInfo = null;
        if (checkMethod("getTaskId()")) {
            int taskId = ((Integer) invoke("getTaskId()", coupledTaskInfo, new Object[0])).intValue();
            if (taskId > 0) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("convertToSdkCoupledTaskInfo() : taskId=");
                sb.append(taskId);
                Log.d(str, sb.toString());
                sdualScreenCoupledTaskInfo = new SDualScreenCoupledTaskInfo(taskId);
                int coupledTaskMode = 0;
                int parentTaskId = 0;
                int childTaskId = 0;
                boolean isCoupled = false;
                if (checkMethod("getCoupledTaskMode()")) {
                    coupledTaskMode = ((Integer) invoke("getCoupledTaskMode()", coupledTaskInfo, new Object[0])).intValue();
                } else {
                    Log.w(TAG, "getCoupledTaskMode() is not loaded");
                }
                if (checkMethod("getParentCoupledTaskId()")) {
                    parentTaskId = ((Integer) invoke("getParentCoupledTaskId()", coupledTaskInfo, new Object[0])).intValue();
                } else {
                    Log.w(TAG, "getParentCoupledTaskId() is not loaded");
                }
                if (checkMethod("getChildCoupledTaskId()")) {
                    childTaskId = ((Integer) invoke("getChildCoupledTaskId()", coupledTaskInfo, new Object[0])).intValue();
                } else {
                    Log.w(TAG, "getParentCoupledTaskId() is not loaded");
                }
                if (checkMethod("isCoupled()")) {
                    isCoupled = ((Boolean) invoke("isCoupled()", coupledTaskInfo, new Object[0])).booleanValue();
                } else {
                    Log.w(TAG, "isCoupled() is not loaded");
                }
                sdualScreenCoupledTaskInfo.setCoupledTaskMode(coupledTaskMode);
                sdualScreenCoupledTaskInfo.setParentCoupledTaskId(parentTaskId);
                sdualScreenCoupledTaskInfo.setChildCoupledTaskId(childTaskId);
                sdualScreenCoupledTaskInfo.setCoupledState(isCoupled);
            }
        } else {
            Log.w(TAG, "getTaskId() is not loaded");
        }
        return sdualScreenCoupledTaskInfo;
    }

    public boolean initialized() {
        return this.mInitialized;
    }

    /* access modifiers changed from: 0000 */
    public DualScreen getScreen() {
        Log.d(TAG, "getScreen()");
        Object ret = null;
        String methodName = "getScreen()";
        if (checkMethod(methodName)) {
            ret = invoke(methodName, this.mInstanceDualScreenManager, new Object[0]);
        }
        if (ret == null) {
            return DualScreen.UNKNOWN;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getScreen() ret=");
        sb.append(ret);
        Log.d(str, sb.toString());
        return convertToSdkDualScreenEnum(ret);
    }

    /* access modifiers changed from: 0000 */
    public DualScreen getScreen(int taskId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getScreen() : taskId=");
        sb.append(taskId);
        Log.d(str, sb.toString());
        String methodName = "getScreen(int)";
        if (!checkMethod(methodName)) {
            return DualScreen.UNKNOWN;
        }
        return (DualScreen) invoke(methodName, this.mInstanceDualScreenManager, Integer.valueOf(taskId));
    }

    /* access modifiers changed from: 0000 */
    public boolean moveToScreen(DualScreen toScreen) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("moveToScreen() : toScreen=");
        sb.append(toScreen);
        Log.d(str, sb.toString());
        String methodName = "moveToScreen(DualScreen)";
        Object nativeEnum = convertToNativeDualScreenEnum(toScreen);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("moveToScreen() : nativeEnum=");
        sb2.append(nativeEnum);
        Log.d(str2, sb2.toString());
        if (!checkMethod(methodName)) {
            return false;
        }
        try {
            return ((Boolean) invoke(methodName, this.mInstanceDualScreenManager, nativeEnum)).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public void switchScreen() {
        Log.d(TAG, "switchScreen()");
        String methodName = "switchScreen()";
        if (checkMethod(methodName)) {
            invoke(methodName, this.mInstanceDualScreenManager, new Object[0]);
        }
    }

    /* access modifiers changed from: 0000 */
    public void swapTopTask() {
        Log.d(TAG, "swapTopTask()");
        String methodName = "swapTopTask()";
        if (checkMethod(methodName)) {
            invoke(methodName, this.mInstanceDualScreenManager, new Object[0]);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setScreenChangedListner(ScreenChangeListener listener) {
        Log.d(TAG, "setScreenChangedListner()");
        String methodName = "setScreenChangedListner(ScreenChangeListener)";
        if (checkMethod(methodName)) {
            invoke(methodName, this.mInstanceActivity, listener);
        }
    }

    /* access modifiers changed from: 0000 */
    public SDualScreenCoupledTaskInfo getCoupledTaskInfo() {
        Log.d(TAG, "getCoupledTaskInfo()");
        String methodName = "getCoupledTaskInfo()";
        if (checkMethod(methodName)) {
            Object ret = invoke(methodName, this.mInstanceDualScreenManager, new Object[0]);
            if (ret != null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getCoupledTaskInfo() ret=");
                sb.append(ret);
                Log.d(str, sb.toString());
                return convertToSdkCoupledTaskInfo(ret);
            }
            if (this.mContext instanceof Activity) {
                new SDualScreenCoupledTaskInfo(((Activity) this.mContext).getTaskId());
            }
            return null;
        }
        Log.w(TAG, "getCoupledTaskInfo() is not loaded");
        return null;
    }

    static Intent makeIntent(Context context, Intent intent, DualScreen targetScreen, int flags) {
        Log.d(TAG, "makeIntent()");
        String methodName = "makeIntent(Context,Intent,DualScreen,int)";
        if (!checkMethod(methodName)) {
            loadKlass();
            putMethod(sKlassDualScreenManager, "makeIntent", new Class[]{Context.class, Intent.class, sKlassDualScreen, Integer.TYPE});
        }
        if (checkMethod(methodName)) {
            invoke(methodName, null, context, intent, convertToNativeDualScreenEnum(targetScreen), Integer.valueOf(flags));
        } else {
            Log.w(TAG, "makeIntent(Context,Intent,DualScreen,int) is not loaded");
        }
        return intent;
    }

    static DualScreen getFocusedScreen() {
        Log.d(TAG, "getFocusedScreen()");
        String methodName = "getFocusedScreen()";
        Object ret = null;
        if (!checkMethod(methodName)) {
            loadKlass();
            putMethod(sKlassDualScreenManager, "getFocusedScreen", null);
        }
        if (checkMethod(methodName)) {
            ret = invoke(methodName, null, new Object[0]);
        } else {
            Log.w(TAG, "getFocusedScreen() is not loaded");
        }
        if (ret == null) {
            return DualScreen.UNKNOWN;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getFocusedScreen() ret=");
        sb.append(ret);
        Log.d(str, sb.toString());
        return convertToSdkDualScreenEnum(ret);
    }

    static int getVersionCode() {
        String methodName = "getVersionCode()";
        Object ret = null;
        if (!checkMethod(methodName)) {
            loadKlass();
            putMethod(sKlassDualScreenManager, "getVersionCode", null);
        }
        if (checkMethod(methodName)) {
            ret = invoke(methodName, null, new Object[0]);
        } else {
            Log.w(TAG, "getVersionCode() is not loaded");
        }
        if (ret == null) {
            return -1;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getVersionCode() ret=");
        sb.append(ret);
        Log.d(str, sb.toString());
        return ((Integer) ret).intValue();
    }

    static String getVersionName() {
        String methodName = "getVersionName()";
        Object ret = null;
        if (!checkMethod(methodName)) {
            loadKlass();
            putMethod(sKlassDualScreenManager, "getVersionName", null);
        }
        if (checkMethod(methodName)) {
            ret = invoke(methodName, null, new Object[0]);
        }
        if (ret == null) {
            return "UNKNOWN";
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getVersionName() ret=");
        sb.append(ret);
        Log.d(str, sb.toString());
        return (String) ret;
    }

    static int getMinimumSdkVersionCode() {
        String methodName = "getMinimumSdkVersionCode()";
        Object ret = null;
        if (!checkMethod(methodName)) {
            loadKlass();
            putMethod(sKlassDualScreenManager, "getMinimumSdkVersionCode", null);
        }
        if (checkMethod(methodName)) {
            ret = invoke(methodName, null, new Object[0]);
        }
        if (ret == null) {
            return -1;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getMinimumSdkVersionCode() : ");
        sb.append(ret);
        Log.d(str, sb.toString());
        return ((Integer) ret).intValue();
    }

    static String getMinimumSdkVersionName() {
        String methodName = "getMinimumSdkVersionName()";
        Object ret = null;
        if (!checkMethod(methodName)) {
            loadKlass();
            putMethod(sKlassDualScreenManager, "getMinimumSdkVersionName", null);
        }
        if (checkMethod(methodName)) {
            ret = invoke(methodName, null, new Object[0]);
        }
        if (ret == null) {
            return "UNKNOWN";
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getMinimumSdkVersionName() : ");
        sb.append(ret);
        Log.d(str, sb.toString());
        return (String) ret;
    }

    static void checkVersion() {
        if (3 < getMinimumSdkVersionCode()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Application using SDK version is old (version 0.3). The device requires (version ");
            sb.append(getMinimumSdkVersionName());
            sb.append(") at least");
            throw new RuntimeException(sb.toString());
        }
    }
}
