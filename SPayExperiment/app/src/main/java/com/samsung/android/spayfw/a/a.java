/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.UserHandle
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.IllegalAccessException
 *  java.lang.Integer
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 */
package com.samsung.android.spayfw.a;

import android.os.UserHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class a {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Object a(String string, Class[] arrclass, Object[] arrobject) {
        try {
            return Class.forName((String)"com.samsung.android.telephony.MultiSimManager").getMethod(string, arrclass).invoke(UserHandle.class, arrobject);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
            do {
                return null;
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
        catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            return null;
        }
    }

    public static final String getImei(int n2) {
        Class[] arrclass = new Class[]{Integer.TYPE};
        Object[] arrobject = new Object[]{n2};
        Object object = a.a("getImei", arrclass, arrobject);
        if (object == null) {
            return null;
        }
        return (String)object;
    }
}

