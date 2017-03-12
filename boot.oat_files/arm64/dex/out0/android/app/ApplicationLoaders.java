package android.app;

import android.app.im.FeaturePathClassLoader;
import android.os.Trace;
import android.util.ArrayMap;
import dalvik.system.PathClassLoader;

class ApplicationLoaders {
    private static final ApplicationLoaders gApplicationLoaders = new ApplicationLoaders();
    private final boolean isElasticEnabled = true;
    private final ArrayMap<String, ClassLoader> mLoaders = new ArrayMap();

    ApplicationLoaders() {
    }

    public static ApplicationLoaders getDefault() {
        return gApplicationLoaders;
    }

    public ClassLoader getClassLoader(String zip, String libPath, ClassLoader parent) {
        return getClassLoader(zip, libPath, parent, false);
    }

    public ClassLoader getClassLoader(String zip, String libPath, ClassLoader parent, boolean isElasticApp) {
        ClassLoader baseParent = ClassLoader.getSystemClassLoader().getParent();
        synchronized (this.mLoaders) {
            if (parent == null) {
                parent = baseParent;
            }
            ClassLoader pathClassloader;
            if (parent == baseParent) {
                ClassLoader loader = (ClassLoader) this.mLoaders.get(zip);
                if (loader != null) {
                    return loader;
                }
                Trace.traceBegin(64, zip);
                if (isElasticApp) {
                    pathClassloader = new FeaturePathClassLoader(zip, libPath, parent);
                } else {
                    pathClassloader = new PathClassLoader(zip, libPath, parent);
                }
                Trace.traceEnd(64);
                this.mLoaders.put(zip, pathClassloader);
                return pathClassloader;
            }
            Trace.traceBegin(64, zip);
            if (isElasticApp) {
                pathClassloader = new FeaturePathClassLoader(zip, parent);
            } else {
                pathClassloader = new PathClassLoader(zip, parent);
            }
            Trace.traceEnd(64);
            return pathClassloader;
        }
    }
}
