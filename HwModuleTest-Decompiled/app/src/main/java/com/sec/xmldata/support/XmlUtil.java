package com.sec.xmldata.support;

import android.os.Process;
import android.util.Log;
import com.sec.xmldata.support.Support.Kernel;

public class XmlUtil {
    private static final String CLASS_NAME = "XmlUtil";
    private static final int LOG_LEVEL_D = 1;
    private static final int LOG_LEVEL_E = 4;
    private static final int LOG_LEVEL_F = 5;
    private static final int LOG_LEVEL_I = 2;
    private static final int LOG_LEVEL_V = 0;
    private static final int LOG_LEVEL_W = 3;
    public static String TAG = "XmlData";

    public static void log_d(String className, String methodName) {
        Log.d(TAG, getLogMessage(className, methodName, null, 1));
    }

    public static void log_d(String className, String methodName, String message) {
        Log.d(TAG, getLogMessage(className, methodName, message, 1));
    }

    public static void log_e(String className, String methodName) {
        Log.e(TAG, getLogMessage(className, methodName, null, 4));
    }

    public static void log_e(String className, String methodName, String message) {
        Log.e(TAG, getLogMessage(className, methodName, message, 4));
    }

    public static void log_i(String className, String methodName) {
        Log.i(TAG, getLogMessage(className, methodName, null, 2));
    }

    public static void log_i(String className, String methodName, String message) {
        Log.i(TAG, getLogMessage(className, methodName, message, 2));
    }

    public static void log_v(String className, String methodName) {
        Log.v(TAG, getLogMessage(className, methodName, null, 0));
    }

    public static void log_v(String className, String methodName, String message) {
        Log.v(TAG, getLogMessage(className, methodName, message, 0));
    }

    public static void log_w(String className, String methodName) {
        Log.w(TAG, getLogMessage(className, methodName, null, 3));
    }

    public static void log_w(String className, String methodName, String message) {
        Log.w(TAG, getLogMessage(className, methodName, message, 3));
    }

    public static void log_wtf(String className, String methodName) {
        Log.wtf(TAG, getLogMessage(className, methodName, null, 5));
    }

    public static void log_wtf(String className, String methodName, String message) {
        Log.wtf(TAG, getLogMessage(className, methodName, message, 5));
    }

    private static String getLogMessage(String className, String methodName, String message, int logLevel) {
        String str = "";
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
        sb.append("](");
        sb.append(Process.myTid());
        sb.append(") ");
        sb.append(message);
        return sb.toString();
    }

    public static void log_e(Exception e) {
        String TAG2 = "FactoryTestApp";
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("WARNNING: ");
        sb.append(e.toString());
        Log.w(TAG2, sb.toString());
        for (StackTraceElement append : stackTraceElements) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("WARNNING:     ");
            sb2.append(append);
            Log.w(TAG2, sb2.toString());
        }
    }

    public static boolean isFolderOpen() {
        String strVal = Kernel.read(Kernel.PATH_HALLIC_STATE);
        log_v(CLASS_NAME, "isFolderOpen", strVal);
        if (strVal == null) {
            return true;
        }
        if (strVal.equals("CLOSE") || strVal.equals("0")) {
            return false;
        }
        return true;
    }
}
