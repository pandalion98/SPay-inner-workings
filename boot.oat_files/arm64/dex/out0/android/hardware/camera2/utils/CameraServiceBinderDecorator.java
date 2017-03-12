package android.hardware.camera2.utils;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.util.Log;
import java.lang.reflect.Method;

public class CameraServiceBinderDecorator extends CameraBinderDecorator {
    private static final String TAG = "CameraServiceBinderDecorator";

    static class CameraServiceBinderDecoratorListener extends CameraBinderDecoratorListener {
        CameraServiceBinderDecoratorListener() {
        }

        public boolean onCatchException(Method m, Object[] args, Throwable t) {
            if (!(t instanceof DeadObjectException) && (t instanceof RemoteException)) {
                Log.e(CameraServiceBinderDecorator.TAG, "Unexpected RemoteException from camera service call.", t);
            }
            return false;
        }
    }

    public static <T> T newInstance(T obj) {
        return Decorator.newInstance(obj, new CameraServiceBinderDecoratorListener());
    }
}
