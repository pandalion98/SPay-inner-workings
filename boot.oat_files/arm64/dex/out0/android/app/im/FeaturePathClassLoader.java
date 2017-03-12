package android.app.im;

import android.net.ProxyInfo;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Field;

public class FeaturePathClassLoader extends PathClassLoader {
    private static final boolean DEBUG_ELASTIC = true;
    private static String TAG = "FeaturePathClassLoader";

    public FeaturePathClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);
    }

    public FeaturePathClassLoader(String dexPath, String libraryPath, ClassLoader parent) {
        super(dexPath, libraryPath, parent);
        Log.d(TAG, "FeaturePathClassLoader init load class dexPath =" + dexPath + ", libraryPath =" + libraryPath + ", parent classloader =" + parent);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> classCreated = super.loadClass(className);
        if (className.contains(".R$")) {
            String packageName = className.split("\\.R\\$")[0];
            Log.i(TAG, "packageName = " + packageName);
            String classResPath = InjectionManager.getClassLibPath(0);
            int offset = 0;
            if (!(classResPath == null || classResPath.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                String zip = classResPath.split("#")[0];
                if (zip != null) {
                    String[] resPaths = zip.split(":");
                    for (int featureCnt = 0; featureCnt < resPaths.length; featureCnt++) {
                        Log.i(TAG, "resPaths[featureCnt] :" + resPaths[featureCnt]);
                        if (resPaths[featureCnt].contains(packageName)) {
                            offset = featureCnt;
                            break;
                        }
                    }
                }
                Log.i(TAG, "Found R class");
                Field[] f = classCreated.getDeclaredFields();
                Log.i(TAG, "field count = " + f.length);
                int val = 0;
                for (Field field : f) {
                    Log.i(TAG, "Field " + field);
                    field.setAccessible(true);
                    try {
                        val = field.getInt(classCreated);
                        Log.i(TAG, "val " + val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                    }
                    Log.i(TAG, "offset = " + offset + " val - (offset << 24) =" + (val - (offset << 24)));
                    try {
                        field.setInt(classCreated, val - (offset << 24));
                    } catch (IllegalAccessException e3) {
                        e3.printStackTrace();
                    } catch (IllegalArgumentException e22) {
                        e22.printStackTrace();
                    }
                }
            }
        }
        return classCreated;
    }
}
