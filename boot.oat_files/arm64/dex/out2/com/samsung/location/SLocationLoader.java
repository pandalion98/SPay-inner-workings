package com.samsung.location;

import android.content.Context;
import android.os.IBinder;
import com.samsung.location.ISLocationManager.Stub;

public class SLocationLoader {
    private static final String CLASS_SLocationService = "com.samsung.location.SLocationService";
    private static final String METHOD_systemReady = "systemReady";
    private static final String TAG = "SLocation";

    private static Class getClassFromLib(Context context, String name) throws Throwable {
        return context.createPackageContext("com.samsung.location", 3).getClassLoader().loadClass(name);
    }

    public static IBinder getSLocationService(Context context) throws Throwable {
        Class sLocationServiceClass = getClassFromLib(context, CLASS_SLocationService);
        if (sLocationServiceClass == null) {
            return null;
        }
        return (IBinder) sLocationServiceClass.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
    }

    public static Object getSLocationManager(IBinder b) throws Throwable {
        return new SLocationManager(Stub.asInterface(b));
    }

    public static void systemReady(Context context, IBinder b) throws Throwable {
        Class sLocationService = getClassFromLib(context, CLASS_SLocationService);
        ISLocationManager service = Stub.asInterface(b);
        if (sLocationService != null) {
            sLocationService.getDeclaredMethod(METHOD_systemReady, new Class[0]).invoke(service, new Object[0]);
        }
    }
}
