package android.app;

import android.app.im.InjectionManager;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import java.util.ArrayList;

public class Application extends ContextWrapper implements ComponentCallbacks2 {
    private ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList();
    private ArrayList<OnProvideAssistDataListener> mAssistCallbacks = null;
    private ArrayList<ComponentCallbacks> mComponentCallbacks = new ArrayList();
    private int mFlipfont = 0;
    public LoadedApk mLoadedApk;

    public interface ActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity, Bundle bundle);

        void onActivityDestroyed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle bundle);

        void onActivityStarted(Activity activity);

        void onActivityStopped(Activity activity);
    }

    public interface OnProvideAssistDataListener {
        void onProvideAssistData(Activity activity, Bundle bundle);
    }

    public Application() {
        super(null);
    }

    public void onCreate() {
        Context context = null;
        try {
            context = getApplicationContext();
        } catch (Exception e) {
        }
        Typeface.SetAppTypeFace(context, getPackageName());
    }

    public void onTerminate() {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (!(newConfig == null || newConfig.FlipFont <= 0 || this.mFlipfont == newConfig.FlipFont)) {
            Typeface.SetAppTypeFace(getApplicationContext(), getPackageName());
            this.mFlipfont = newConfig.FlipFont;
        }
        Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ComponentCallbacks) obj).onConfigurationChanged(newConfig);
            }
        }
    }

    public void onLowMemory() {
        Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ComponentCallbacks) obj).onLowMemory();
            }
        }
    }

    public void onTrimMemory(int level) {
        Object[] callbacks = collectComponentCallbacks();
        if (callbacks != null) {
            for (Object c : callbacks) {
                if (c instanceof ComponentCallbacks2) {
                    ((ComponentCallbacks2) c).onTrimMemory(level);
                }
            }
        }
    }

    public void registerComponentCallbacks(ComponentCallbacks callback) {
        synchronized (this.mComponentCallbacks) {
            this.mComponentCallbacks.add(callback);
        }
    }

    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        synchronized (this.mComponentCallbacks) {
            this.mComponentCallbacks.remove(callback);
        }
    }

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.remove(callback);
        }
    }

    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        synchronized (this) {
            if (this.mAssistCallbacks == null) {
                this.mAssistCallbacks = new ArrayList();
            }
            this.mAssistCallbacks.add(callback);
        }
    }

    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        synchronized (this) {
            if (this.mAssistCallbacks != null) {
                this.mAssistCallbacks.remove(callback);
            }
        }
    }

    final void attach(Context context) {
        attachBaseContext(context);
        this.mLoadedApk = ContextImpl.getImpl(context).mPackageInfo;
    }

    void dispatchActivityCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityCreated(activity, savedInstanceState);
            }
        }
    }

    void dispatchActivityStarted(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityStarted(activity);
            }
        }
    }

    void dispatchActivityResumed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityResumed(activity);
            }
        }
    }

    void dispatchActivityPaused(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPaused(activity);
            }
        }
    }

    void dispatchActivityStopped(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityStopped(activity);
            }
        }
    }

    void dispatchActivitySaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivitySaveInstanceState(activity, outState);
            }
        }
    }

    void dispatchActivityDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityDestroyed(activity);
            }
        }
    }

    private Object[] collectComponentCallbacks() {
        Object[] callbacks = null;
        synchronized (this.mComponentCallbacks) {
            if (this.mComponentCallbacks.size() > 0) {
                callbacks = this.mComponentCallbacks.toArray();
            }
        }
        return callbacks;
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (this.mActivityLifecycleCallbacks) {
            if (this.mActivityLifecycleCallbacks.size() > 0) {
                callbacks = this.mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void dispatchOnProvideAssistData(android.app.Activity r4, android.os.Bundle r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r2 = r3.mAssistCallbacks;	 Catch:{ all -> 0x001e }
        if (r2 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
    L_0x0006:
        return;
    L_0x0007:
        r2 = r3.mAssistCallbacks;	 Catch:{ all -> 0x001e }
        r0 = r2.toArray();	 Catch:{ all -> 0x001e }
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        if (r0 == 0) goto L_0x0006;
    L_0x0010:
        r1 = 0;
    L_0x0011:
        r2 = r0.length;
        if (r1 >= r2) goto L_0x0006;
    L_0x0014:
        r2 = r0[r1];
        r2 = (android.app.Application.OnProvideAssistDataListener) r2;
        r2.onProvideAssistData(r4, r5);
        r1 = r1 + 1;
        goto L_0x0011;
    L_0x001e:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Application.dispatchOnProvideAssistData(android.app.Activity, android.os.Bundle):void");
    }

    final void dispatchInjectionManagerGetInstance() {
        InjectionManager.getInstance(this);
    }
}
