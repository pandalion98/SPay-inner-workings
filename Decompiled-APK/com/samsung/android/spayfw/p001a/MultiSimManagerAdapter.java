package com.samsung.android.spayfw.p001a;

import android.os.UserHandle;
import java.lang.reflect.InvocationTargetException;

/* renamed from: com.samsung.android.spayfw.a.a */
public class MultiSimManagerAdapter {
    public static final String getImei(int i) {
        Object a = MultiSimManagerAdapter.m261a("getImei", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i)});
        if (a == null) {
            return null;
        }
        return (String) a;
    }

    private static Object m261a(String str, Class[] clsArr, Object[] objArr) {
        try {
            return Class.forName("com.samsung.android.telephony.MultiSimManager").getMethod(str, clsArr).invoke(UserHandle.class, objArr);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return null;
        } catch (ClassNotFoundException e4) {
            e4.printStackTrace();
            return null;
        }
    }
}
