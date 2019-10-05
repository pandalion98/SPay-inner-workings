/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.Process
 *  android.os.ServiceManager
 *  android.service.tima.ITimaService
 *  android.service.tima.ITimaService$Stub
 *  android.util.Base64
 *  java.lang.Byte
 *  java.lang.Character
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Method
 */
package com.samsung.android.spayfw.utils;

import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import android.service.tima.ITimaService;
import android.util.Base64;

import com.samsung.android.spayfw.b.Log;

import java.lang.reflect.Method;

public class b {
    private static ITimaService CT;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String bx(String string) {
        CT = ITimaService.Stub.asInterface((IBinder)ServiceManager.getService((String)"tima"));
        if (CT == null) {
            Log.e("AttestationHelper", "TIMA service is not available!!!");
            return null;
        }
        byte[] arrby = b.getByteArray(string);
        try {
            byte[] arrby2;
            try {
                Method method = CT.getClass().getMethod("attestation", new Class[]{byte[].class});
                Log.d("AttestationHelper", "Using old Attestaion API");
                arrby2 = (byte[])method.invoke((Object)CT, new Object[]{arrby});
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.d("AttestationHelper", "Using new Attestaion API");
                Class class_ = CT.getClass();
                Class[] arrclass = new Class[]{byte[].class, Integer.TYPE};
                Method method = class_.getMethod("attestation", arrclass);
                ITimaService iTimaService = CT;
                Object[] arrobject = new Object[]{arrby, Process.myUid()};
                arrby2 = (byte[])method.invoke((Object)iTimaService, arrobject);
            }
            if (arrby2 == null) {
                Log.e("AttestationHelper", "Blob from TIMA is invalid");
                return null;
            }
            if (!b.n(arrby2)) {
                Log.e("AttestationHelper", "Error in exit code byte array");
                return null;
            }
            byte[] arrby3 = new byte[-1 + arrby2.length];
            System.arraycopy((Object)arrby2, (int)1, (Object)arrby3, (int)0, (int)(-1 + arrby2.length));
            int n2 = Byte.valueOf((byte)arrby3[0]).intValue();
            if (n2 == 0) return Base64.encodeToString((byte[])arrby3, (int)2);
            {
                int n3 = Byte.valueOf((byte)arrby3[1]).intValue();
                byte[] arrby4 = new byte[n3];
                System.arraycopy((Object)arrby3, (int)2, (Object)arrby4, (int)0, (int)n3);
                Log.e("AttestationHelper", "Blob is not valid. ErrorCode = " + n2 + " Message = " + new String(arrby4));
                return null;
            }
        }
        catch (Exception exception) {
            Log.e("AttestationHelper", "Failed talking with attestation policy/Invalid Nonce length" + Log.getStackTraceString(exception));
            return null;
        }
    }

    private static byte[] getByteArray(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
    }

    private static boolean n(byte[] arrby) {
        switch (Byte.valueOf((byte)arrby[0]).intValue()) {
            default: {
                return false;
            }
            case 0: 
        }
        return true;
    }
}

