/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.UserHandle
 *  java.lang.Class
 *  java.lang.IllegalAccessException
 *  java.lang.Integer
 *  java.lang.NoSuchFieldException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Field
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 */
package com.samsung.android.spayfw.a;

import android.os.UserHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class b {
    public static int USER_OWNER;

    static {
        try {
            USER_OWNER = UserHandle.class.getField("USER_OWNER").getInt(UserHandle.class);
            return;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Object a(String string, Class[] arrclass, Object[] arrobject) {
        try {
            return UserHandle.class.getMethod(string, arrclass).invoke(UserHandle.class, arrobject);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
            do {
                return null;
                break;
            } while (true);
        }
        catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return null;
        }
    }

    public static final int getCallingUserId() {
        Object object = b.a("getCallingUserId", null, null);
        if (object == null) {
            return -1;
        }
        return (Integer)object;
    }

    public static final int myUserId() {
        Object object = b.a("myUserId", null, null);
        if (object == null) {
            return -1;
        }
        return (Integer)object;
    }
}

