package android.filterfw.core;

import android.util.Log;
import java.util.HashSet;
import java.util.Iterator;

public class FilterFactory {
    private static final String TAG = "FilterFactory";
    private static Object mClassLoaderGuard = new Object();
    private static ClassLoader mCurrentClassLoader = Thread.currentThread().getContextClassLoader();
    private static HashSet<String> mLibraries = new HashSet();
    private static boolean mLogVerbose = Log.isLoggable(TAG, 2);
    private static FilterFactory mSharedFactory;
    private HashSet<String> mPackages = new HashSet();

    public static FilterFactory sharedFactory() {
        if (mSharedFactory == null) {
            mSharedFactory = new FilterFactory();
        }
        return mSharedFactory;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void addFilterLibrary(java.lang.String r3) {
        /*
        r0 = mLogVerbose;
        if (r0 == 0) goto L_0x001c;
    L_0x0004:
        r0 = "FilterFactory";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Adding filter library ";
        r1 = r1.append(r2);
        r1 = r1.append(r3);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x001c:
        r1 = mClassLoaderGuard;
        monitor-enter(r1);
        r0 = mLibraries;	 Catch:{ all -> 0x0044 }
        r0 = r0.contains(r3);	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x0034;
    L_0x0027:
        r0 = mLogVerbose;	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x0032;
    L_0x002b:
        r0 = "FilterFactory";
        r2 = "Library already added";
        android.util.Log.v(r0, r2);	 Catch:{ all -> 0x0044 }
    L_0x0032:
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
    L_0x0033:
        return;
    L_0x0034:
        r0 = mLibraries;	 Catch:{ all -> 0x0044 }
        r0.add(r3);	 Catch:{ all -> 0x0044 }
        r0 = new dalvik.system.PathClassLoader;	 Catch:{ all -> 0x0044 }
        r2 = mCurrentClassLoader;	 Catch:{ all -> 0x0044 }
        r0.<init>(r3, r2);	 Catch:{ all -> 0x0044 }
        mCurrentClassLoader = r0;	 Catch:{ all -> 0x0044 }
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
        goto L_0x0033;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.filterfw.core.FilterFactory.addFilterLibrary(java.lang.String):void");
    }

    public void addPackage(String packageName) {
        if (mLogVerbose) {
            Log.v(TAG, "Adding package " + packageName);
        }
        this.mPackages.add(packageName);
    }

    public Filter createFilterByClassName(String className, String filterName) {
        if (mLogVerbose) {
            Log.v(TAG, "Looking up class " + className);
        }
        Class filterClass = null;
        Iterator i$ = this.mPackages.iterator();
        while (i$.hasNext()) {
            String packageName = (String) i$.next();
            try {
                if (mLogVerbose) {
                    Log.v(TAG, "Trying " + packageName + "." + className);
                }
                synchronized (mClassLoaderGuard) {
                    filterClass = mCurrentClassLoader.loadClass(packageName + "." + className);
                }
                if (filterClass != null) {
                    break;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        if (filterClass != null) {
            return createFilterByClass(filterClass, filterName);
        }
        throw new IllegalArgumentException("Unknown filter class '" + className + "'!");
    }

    public Filter createFilterByClass(Class filterClass, String filterName) {
        try {
            filterClass.asSubclass(Filter.class);
            try {
                Filter filter = null;
                try {
                    filter = (Filter) filterClass.getConstructor(new Class[]{String.class}).newInstance(new Object[]{filterName});
                } catch (Throwable th) {
                }
                if (filter != null) {
                    return filter;
                }
                throw new IllegalArgumentException("Could not construct the filter '" + filterName + "'!");
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("The filter class '" + filterClass + "' does not have a constructor of the form <init>(String name)!");
            }
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException("Attempting to allocate class '" + filterClass + "' which is not a subclass of Filter!");
        }
    }
}
