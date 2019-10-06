package com.samsung.android.sdk.dualscreen;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

class SDualScreenReflector {
    private static final boolean DEBUG = true;
    private static final String TAG = SDualScreenReflector.class.getSimpleName();
    private static HashMap<String, Method> mMethodMap = new HashMap<>();

    protected SDualScreenReflector() {
    }

    public static void putMethod(Class<?> cls, String methodName, Class<?>[] params) {
        try {
            Method m = cls.getMethod(methodName, params);
            StringBuilder b = new StringBuilder(256);
            b.append(methodName);
            b.append("(");
            if (params != null) {
                int paramSize = params.length;
                for (int i = 0; i < paramSize; i++) {
                    b.append(params[i].getSimpleName());
                    if (i < paramSize - 1) {
                        b.append(",");
                    }
                }
            }
            b.append(")");
            String methodNameWithParams = b.toString();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(cls.getSimpleName());
            sb.append(" : loaded method => ");
            sb.append(methodNameWithParams);
            Log.d(str, sb.toString());
            mMethodMap.put(methodNameWithParams, m);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "putMethod() : NoSuchFieldException !");
            e.printStackTrace();
        }
    }

    public static boolean checkMethod(String name) {
        if (((Method) mMethodMap.get(name)) != null) {
            return DEBUG;
        }
        return false;
    }

    public static Object invoke(String name, Object instance, Object... args) {
        Object[] arr$;
        try {
            Method method = (Method) mMethodMap.get(name);
            if (method != null) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("invoke : Method=");
                sb.append(method);
                sb.append(" instance=");
                sb.append(instance);
                Log.d(str, sb.toString());
                for (Object o : args) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("invoke : arg=>");
                    sb2.append(o);
                    Log.d(str2, sb2.toString());
                }
                return method.invoke(instance, args);
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "invoke() : IllegalAccessException !");
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, "invoke() : IllegalArgumentException !");
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "invoke() : InvocationTargetException !");
            e3.printStackTrace();
        }
        return null;
    }
}
