package com.samsung.android.contextaware.creator;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ListenerListManager;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ContextProviderCreator {
    private static ISensorHubResetObservable sAPPowerObservable;
    private static Context sContext;
    private static final ConcurrentHashMap<String, ContextComponent> sContextProviderMap = new ConcurrentHashMap();
    private static Looper sLooper;
    private static int sVersion;

    public abstract IListObjectCreator getValueOfList(String str);

    protected ContextProviderCreator(Context context, Looper looper, ISensorHubResetObservable observable, int version) {
        setContext(context);
        setLooper(looper);
        setAPPowerObservable(observable);
        setVersion(version);
    }

    public ContextComponent create(String name) {
        return create(name, false);
    }

    public ContextComponent create(String name, Object... property) {
        return create(name, false, property);
    }

    private ContextComponent create(String name, boolean isSubCollection) {
        if (!existContextProvider(name)) {
            return null;
        }
        if (isSubCollection) {
            return getValueOfList(name).getObjectForSubCollection();
        }
        return getValueOfList(name).getObject();
    }

    public final ContextComponent create(String name, boolean isSubCollection, Object... property) {
        if (!existContextProvider(name)) {
            return null;
        }
        if (property == null || property.length <= 0) {
            return create(name, isSubCollection);
        }
        if (isSubCollection) {
            return getValueOfList(name).getObjectForSubCollection(property);
        }
        return getValueOfList(name).getObject(property);
    }

    public final boolean existContextProvider(String name) {
        try {
            getValueOfList(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    protected static final synchronized ConcurrentHashMap<String, ContextComponent> getContextProviderMap() {
        ConcurrentHashMap<String, ContextComponent> concurrentHashMap;
        synchronized (ContextProviderCreator.class) {
            concurrentHashMap = sContextProviderMap;
        }
        return concurrentHashMap;
    }

    public final void removeContextObj(String service) {
        if (existContextProvider(service)) {
            getValueOfList(service).removeObject(service);
        }
    }

    private static void setContext(Context sContext) {
        sContext = sContext;
    }

    protected static final Context getContext() {
        return sContext;
    }

    private static void setLooper(Looper sLooper) {
        sLooper = sLooper;
    }

    protected static Looper getLooper() {
        return sLooper;
    }

    private static void setAPPowerObservable(ISensorHubResetObservable sAPPowerObservable) {
        sAPPowerObservable = sAPPowerObservable;
    }

    protected static final ISensorHubResetObservable getApPowerObservable() {
        return sAPPowerObservable;
    }

    public static void setVersion(int version) {
        sVersion = version;
    }

    protected static int getVersion() {
        return sVersion;
    }

    protected static final boolean removeObj(String service) {
        if (ListenerListManager.getInstance().getUsedTotalCount(service) >= 1) {
            return false;
        }
        if (!getContextProviderMap().containsKey(service)) {
            return true;
        }
        getContextProviderMap().remove(service);
        return true;
    }
}
