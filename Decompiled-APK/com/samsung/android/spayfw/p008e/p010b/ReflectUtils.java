package com.samsung.android.spayfw.p008e.p010b;

import android.util.Log;
import java.lang.reflect.Field;

/* renamed from: com.samsung.android.spayfw.e.b.b */
public class ReflectUtils {
    public static Field m691a(Class cls, String str) {
        Exception e;
        if (cls == null) {
            return null;
        }
        try {
            return cls.getField(str);
        } catch (NoSuchFieldException e2) {
            e = e2;
            Log.d("ReflectUtils", "Cannot load field: " + e.getMessage());
            return null;
        } catch (SecurityException e3) {
            e = e3;
            Log.d("ReflectUtils", "Cannot load field: " + e.getMessage());
            return null;
        }
    }
}
