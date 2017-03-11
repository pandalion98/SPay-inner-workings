package com.samsung.android.spayfw.utils;

import android.os.Process;
import android.os.ServiceManager;
import android.service.tima.ITimaService;
import android.service.tima.ITimaService.Stub;
import android.util.Base64;
import com.samsung.android.spayfw.p002b.Log;
import java.lang.reflect.Method;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.utils.b */
public class AttestationHelper {
    private static ITimaService CT;

    private static boolean m1265n(byte[] bArr) {
        switch (Byte.valueOf(bArr[0]).intValue()) {
            case ECCurve.COORD_AFFINE /*0*/:
                return true;
            default:
                return false;
        }
    }

    private static byte[] getByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static String bx(String str) {
        CT = Stub.asInterface(ServiceManager.getService("tima"));
        if (CT == null) {
            Log.m286e("AttestationHelper", "TIMA service is not available!!!");
            return null;
        }
        byte[] byteArray = AttestationHelper.getByteArray(str);
        try {
            Method method = CT.getClass().getMethod("attestation", new Class[]{byte[].class});
            Log.m285d("AttestationHelper", "Using old Attestaion API");
            Object obj = (byte[]) method.invoke(CT, new Object[]{byteArray});
        } catch (NoSuchMethodException e) {
            Log.m285d("AttestationHelper", "Using new Attestaion API");
            byte[] bArr = (byte[]) CT.getClass().getMethod("attestation", new Class[]{byte[].class, Integer.TYPE}).invoke(CT, new Object[]{byteArray, Integer.valueOf(Process.myUid())});
        }
        if (obj == null) {
            try {
                Log.m286e("AttestationHelper", "Blob from TIMA is invalid");
                return null;
            } catch (Throwable e2) {
                Log.m286e("AttestationHelper", "Failed talking with attestation policy/Invalid Nonce length" + Log.getStackTraceString(e2));
                return null;
            }
        } else if (AttestationHelper.m1265n(obj)) {
            Object obj2 = new byte[(obj.length - 1)];
            System.arraycopy(obj, 1, obj2, 0, obj.length - 1);
            int intValue = Byte.valueOf(obj2[0]).intValue();
            if (intValue == 0) {
                return Base64.encodeToString(obj2, 2);
            }
            int intValue2 = Byte.valueOf(obj2[1]).intValue();
            Object obj3 = new byte[intValue2];
            System.arraycopy(obj2, 2, obj3, 0, intValue2);
            Log.m286e("AttestationHelper", "Blob is not valid. ErrorCode = " + intValue + " Message = " + new String(obj3));
            return null;
        } else {
            Log.m286e("AttestationHelper", "Error in exit code byte array");
            return null;
        }
    }
}
