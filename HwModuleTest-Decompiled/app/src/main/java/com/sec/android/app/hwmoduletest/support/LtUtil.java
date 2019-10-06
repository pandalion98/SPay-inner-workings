package com.sec.android.app.hwmoduletest.support;

import android.content.ComponentName;
import android.content.Context;
import android.os.FactoryTest;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.p000os.ServiceManager;
import android.util.Log;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.samsung.android.service.EngineeringMode.EngineeringModeManager;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class LtUtil {
    private static final String CLASS_NAME = "HwModuleTest";
    private static final boolean LOG_DISABLED = false;
    private static final boolean LOG_ENABLED = true;
    private static final int LOG_TYPE_D = 0;
    private static final int LOG_TYPE_E = 1;
    private static final int LOG_TYPE_I = 2;
    private static final int LOG_TYPE_V = 3;
    private static final int LOG_TYPE_W = 4;
    private static final int LOG_TYPE_WTF = 5;
    private static final int MODE_TEST_AT_COMMAND = 28;
    private static int counter = 0;
    private static boolean mEnableLogs = LOG_ENABLED;

    public static class Sleep implements Runnable {
        final Object _lock = new Object();
        private long _timeout = 1000;

        private Sleep(long timeout) {
            this._timeout = timeout;
        }

        public void run() {
            synchronized (this._lock) {
                try {
                    this._lock.wait(this._timeout);
                } catch (InterruptedException e) {
                    LtUtil.log_e(e);
                }
            }
        }

        public static void sleep(long timeout) {
            StringBuilder sb = new StringBuilder();
            sb.append("timeout: ");
            sb.append(timeout);
            LtUtil.log_d(LtUtil.CLASS_NAME, "sleep start", sb.toString());
            Thread thread = new Thread(new Sleep(timeout));
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                LtUtil.log_e(e);
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("timeout: ");
            sb2.append(timeout);
            LtUtil.log_d(LtUtil.CLASS_NAME, "sleep end", sb2.toString());
        }

        public static void sleep(long timeout, String message) {
            StringBuilder sb = new StringBuilder();
            sb.append("message: ");
            sb.append(message);
            LtUtil.log_d(LtUtil.CLASS_NAME, "sleep", sb.toString());
            sleep(timeout);
        }
    }

    public static void setSystemKeyBlock(ComponentName componentName, int keyCode) {
        IWindowManager wm = Stub.asInterface(ServiceManager.getService("window"));
        if (wm != null) {
            try {
                wm.requestSystemKeyEvent(keyCode, componentName, LOG_ENABLED);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sleep(Context context) {
        ((PowerManager) context.getSystemService("power")).goToSleep(SystemClock.uptimeMillis());
    }

    public static int convertDpFromPixel(Context context, int pixel) {
        return (int) ((((float) pixel) / 2.0f) * context.getResources().getDisplayMetrics().density);
    }

    public static int convertPixelFromDp(Context context, int dp) {
        return (int) ((((float) dp) / context.getResources().getDisplayMetrics().density) * 2.0f);
    }

    public static void EnableLogs() {
        mEnableLogs = LOG_ENABLED;
    }

    public static void DisableLogs() {
        mEnableLogs = false;
    }

    public static boolean getStateLogs() {
        return mEnableLogs;
    }

    private static void log(int type, String className, String methodName, String message) {
        if (getStateLogs() || type == 1) {
            String TAG = CLASS_NAME;
            if (className == null || className.equals("")) {
                className = " ";
            }
            if (methodName == null || methodName.equals("")) {
                methodName = " ";
            }
            if (message == null || message.equals("")) {
                message = " ";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(className);
            sb.append("$");
            sb.append(methodName);
            sb.append("] ");
            sb.append(message);
            String str = sb.toString();
            switch (type) {
                case 0:
                    Log.d(TAG, str);
                    break;
                case 1:
                    Log.e(TAG, str);
                    break;
                case 2:
                    Log.i(TAG, str);
                    break;
                case 3:
                    Log.v(TAG, str);
                    break;
                case 4:
                    Log.w(TAG, str);
                    break;
                case 5:
                    Log.wtf(TAG, str);
                    break;
            }
            return;
        }
        counter++;
        if (counter > 10) {
            counter = 0;
            EnableLogs();
        }
    }

    public static void log_d(String className, String methodName, String message) {
        log(0, className, methodName, message);
    }

    public static void log_e(String className, String methodName, String message) {
        log(1, className, methodName, message);
    }

    public static void log_i(String className, String methodName, String message) {
        log(2, className, methodName, message);
    }

    public static void log_v(String className, String methodName, String message) {
        log(3, className, methodName, message);
    }

    public static void log_wtf(String className, String methodName, String message) {
        log(5, className, methodName, message);
    }

    public static void log_e(Exception e) {
        String TAG = CLASS_NAME;
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("WARNNING: ");
        sb.append(e.toString());
        Log.w(TAG, sb.toString());
        for (StackTraceElement append : stackTraceElements) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("WARNNING:     ");
            sb2.append(append);
            Log.w(TAG, sb2.toString());
        }
    }

    public static void setDisplayCutOutMode(Window window, int param) {
        LayoutParams lp = window.getAttributes();
        if (param < 0 || param > 2) {
            log_i(CLASS_NAME, "setDisplayCutOutMode", "Wrong Value(range : 0,1,2)");
            return;
        }
        try {
            LayoutParams.class.getDeclaredField("layoutInDisplayCutoutMode").set(lp, Integer.valueOf(param));
            window.setAttributes(lp);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static void disableDisplayCutOutMode(Window window, boolean remove) {
        LayoutParams lp = window.getAttributes();
        try {
            Field field = LayoutParams.class.getDeclaredField("layoutInDisplayCutoutMode");
            if (remove) {
                field.set(lp, Integer.valueOf(1));
                lp.systemUiVisibility |= 1024;
            } else {
                field.set(lp, Integer.valueOf(0));
                if ((lp.systemUiVisibility & 1024) != 0) {
                    lp.systemUiVisibility ^= 1024;
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        window.setAttributes(lp);
        window.getDecorView().setSystemUiVisibility(lp.systemUiVisibility);
    }

    public static void setRemoveSystemUI(Window window, boolean remove) {
        LayoutParams lp = window.getAttributes();
        StringBuilder sb = new StringBuilder();
        sb.append("systemUiVisibility: ");
        sb.append(2050);
        log_i(CLASS_NAME, "setRemoveSystemUI", sb.toString());
        if (remove) {
            lp.systemUiVisibility |= 2050;
        } else if ((lp.systemUiVisibility & 2050) != 0) {
            lp.systemUiVisibility ^= 2050;
        }
        window.setAttributes(lp);
    }

    public static void setRemoveStatusBar(Window window) {
        LayoutParams lp = window.getAttributes();
        lp.flags |= 1024;
        window.setAttributes(lp);
    }

    public static void setPressureTouchStatus(String status) {
        if (Feature.getBoolean(Feature.SUPPORT_PRESSURE_TOUCH) && !FactoryTest.isFactoryBinary()) {
            Kernel.write(Kernel.PRESSURE_TOUCH_STATUS, status);
        }
    }

    public static boolean isWriteable(String... paths) {
        int length = paths.length;
        int i = 0;
        while (i < length) {
            String path = paths[i];
            File file = new File(path);
            boolean bExist = file.exists();
            if (bExist) {
                boolean bCanWrite = file.canWrite();
                if (!bCanWrite) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(path);
                    sb.append(" canWrite: ");
                    sb.append(bCanWrite);
                    log_d(CLASS_NAME, "isWriteable", sb.toString());
                    return false;
                }
                i++;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(path);
                sb2.append(" exists: ");
                sb2.append(bExist);
                log_d(CLASS_NAME, "isWriteable", sb2.toString());
                return false;
            }
        }
        return LOG_ENABLED;
    }

    public static boolean writeToFile(String path, String value) {
        if (!isWriteable(path)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed isWriteable: ");
            sb.append(path);
            log_d(CLASS_NAME, "writeToFile", sb.toString());
            return false;
        }
        FileOutputStream out = null;
        try {
            long nStart = System.currentTimeMillis();
            out = new FileOutputStream(path);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("start, path: ");
            sb2.append(path);
            sb2.append(", value: ");
            sb2.append(value);
            log_d(CLASS_NAME, "writeToFile", sb2.toString());
            out.write(value.getBytes());
            out.flush();
            long nEnd = System.currentTimeMillis();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("finish, time: ");
            sb3.append(nEnd - nStart);
            sb3.append(", path: ");
            sb3.append(path);
            sb3.append(", value: ");
            sb3.append(value);
            log_d(CLASS_NAME, "writeToFile", sb3.toString());
            try {
                out.close();
                return LOG_ENABLED;
            } catch (IOException e) {
            }
        } catch (IOException e2) {
            log_e(CLASS_NAME, "writeToFile", e2.toString());
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e3) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (out != null) {
                out.close();
            }
            throw th;
        }
        return false;
    }

    public static boolean isReadable(String... paths) {
        int length = paths.length;
        int i = 0;
        while (i < length) {
            String path = paths[i];
            File file = new File(path);
            boolean bExist = file.exists();
            if (bExist) {
                boolean bCanRead = file.canRead();
                if (!bCanRead) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(path);
                    sb.append(" canRead: ");
                    sb.append(bCanRead);
                    log_d(CLASS_NAME, "isReadable", sb.toString());
                    return false;
                }
                i++;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(path);
                sb2.append(" exists: ");
                sb2.append(bExist);
                log_d(CLASS_NAME, "isReadable", sb2.toString());
                return false;
            }
        }
        return LOG_ENABLED;
    }

    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.BufferedReader, java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v1, names: [reader], types: [java.io.BufferedReader, java.lang.String]
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
      uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.BufferedReader, java.lang.String]
      mth insns count: 79
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readFromFile(java.lang.String r12) {
        /*
            r0 = 1
            java.lang.String[] r0 = new java.lang.String[r0]
            r1 = 0
            r0[r1] = r12
            boolean r0 = isReadable(r0)
            r1 = 0
            if (r0 != 0) goto L_0x0026
            java.lang.String r0 = "HwModuleTest"
            java.lang.String r2 = "readFromFile"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Failed isReadable: "
            r3.append(r4)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            log_d(r0, r2, r3)
            return r1
        L_0x0026:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r2 = 0
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00b3 }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00b3 }
            r3 = r5
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ IOException -> 0x00b3 }
            java.io.FileReader r6 = new java.io.FileReader     // Catch:{ IOException -> 0x00b3 }
            r6.<init>(r12)     // Catch:{ IOException -> 0x00b3 }
            r5.<init>(r6)     // Catch:{ IOException -> 0x00b3 }
            r1 = r5
            java.lang.String r5 = "HwModuleTest"
            java.lang.String r6 = "readFromFile"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00b3 }
            r7.<init>()     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r8 = "start, path: "
            r7.append(r8)     // Catch:{ IOException -> 0x00b3 }
            r7.append(r12)     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x00b3 }
            log_d(r5, r6, r7)     // Catch:{ IOException -> 0x00b3 }
        L_0x0059:
            java.lang.String r5 = r1.readLine()     // Catch:{ IOException -> 0x00b3 }
            r2 = r5
            if (r5 == 0) goto L_0x0069
            r0.append(r2)     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r5 = "\n"
            r0.append(r5)     // Catch:{ IOException -> 0x00b3 }
            goto L_0x0059
        L_0x0069:
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r7 = "HwModuleTest"
            java.lang.String r8 = "readFromFile"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00b3 }
            r9.<init>()     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r10 = "finish, time: "
            r9.append(r10)     // Catch:{ IOException -> 0x00b3 }
            long r10 = r5 - r3
            r9.append(r10)     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r10 = ", path: "
            r9.append(r10)     // Catch:{ IOException -> 0x00b3 }
            r9.append(r12)     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r10 = ", value: "
            r9.append(r10)     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r10 = r0.toString()     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r10 = r10.trim()     // Catch:{ IOException -> 0x00b3 }
            r9.append(r10)     // Catch:{ IOException -> 0x00b3 }
            java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x00b3 }
            log_d(r7, r8, r9)     // Catch:{ IOException -> 0x00b3 }
            r1.close()     // Catch:{ IOException -> 0x00a4 }
        L_0x00a3:
            goto L_0x00c5
        L_0x00a4:
            r3 = move-exception
            java.lang.String r4 = "HwModuleTest"
            java.lang.String r5 = "readFromFile"
            java.lang.String r6 = r3.toString()
            log_e(r4, r5, r6)
            goto L_0x00a3
        L_0x00b1:
            r3 = move-exception
            goto L_0x00ce
        L_0x00b3:
            r3 = move-exception
            java.lang.String r4 = "HwModuleTest"
            java.lang.String r5 = "readFromFile"
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x00b1 }
            log_e(r4, r5, r6)     // Catch:{ all -> 0x00b1 }
            if (r1 == 0) goto L_0x00c5
            r1.close()     // Catch:{ IOException -> 0x00a4 }
            goto L_0x00a3
        L_0x00c5:
            java.lang.String r3 = r0.toString()
            java.lang.String r3 = r3.trim()
            return r3
        L_0x00ce:
            if (r1 == 0) goto L_0x00e0
            r1.close()     // Catch:{ IOException -> 0x00d4 }
            goto L_0x00e0
        L_0x00d4:
            r4 = move-exception
            java.lang.String r5 = r4.toString()
            java.lang.String r6 = "HwModuleTest"
            java.lang.String r7 = "readFromFile"
            log_e(r6, r7, r5)
        L_0x00e0:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.support.LtUtil.readFromFile(java.lang.String):java.lang.String");
    }

    public static boolean isEMATCmd(Context context) {
        if (SystemProperties.getInt("ro.product.first_api_level", 0) < 28) {
            log_d(CLASS_NAME, "isEMATCmd", "Available from P-OS");
            return false;
        }
        EngineeringModeManager emm = new EngineeringModeManager(context);
        if (!emm.isConnected()) {
            log_d(CLASS_NAME, "isEMATCmd", "Cannot use EM manager ");
            return false;
        } else if (emm.getStatus(28) == 1) {
            log_d(CLASS_NAME, "isEMATCmd", "EM AT command allowed");
            return LOG_ENABLED;
        } else {
            log_d(CLASS_NAME, "isEMATCmd", "Unknown error");
            return false;
        }
    }
}
