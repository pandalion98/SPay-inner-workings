package com.samsung.android.spayfw.p001a;

import android.os.UserHandle;
import java.lang.reflect.InvocationTargetException;

/* renamed from: com.samsung.android.spayfw.a.b */
public class UserHandleAdapter {
    public static int USER_OWNER;

    static {
        try {
            USER_OWNER = Integer.valueOf(UserHandle.class.getField("USER_OWNER").getInt(UserHandle.class)).intValue();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static final int myUserId() {
        Object a = UserHandleAdapter.m262a("myUserId", null, null);
        if (a == null) {
            return -1;
        }
        return ((Integer) a).intValue();
    }

    public static final int getCallingUserId() {
        Object a = UserHandleAdapter.m262a("getCallingUserId", null, null);
        if (a == null) {
            return -1;
        }
        return ((Integer) a).intValue();
    }

    private static Object m262a(String str, Class[] clsArr, Object[] objArr) {
        try {
            return UserHandle.class.getMethod(str, clsArr).invoke(UserHandle.class, objArr);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return null;
        }
    }
}
