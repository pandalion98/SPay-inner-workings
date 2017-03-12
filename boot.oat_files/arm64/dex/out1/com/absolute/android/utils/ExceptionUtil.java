package com.absolute.android.utils;

import java.util.ArrayList;
import java.util.List;

public class ExceptionUtil {
    public static String getExceptionMessage(Throwable th) {
        List arrayList = new ArrayList();
        arrayList.add(th.toString());
        StackTraceElement[] stackTrace = th.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            arrayList.add(stackTraceElement.toString());
        }
        return arrayList.toString();
    }
}
