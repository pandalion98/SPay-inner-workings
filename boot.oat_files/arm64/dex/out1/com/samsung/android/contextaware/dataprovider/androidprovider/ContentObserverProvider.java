package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class ContentObserverProvider extends AndroidProvider {
    private ContentResolver mContentResolver;

    protected abstract ContentObserver getContentObserver();

    protected abstract Uri getUri();

    protected ContentObserverProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
        } else {
            this.mContentResolver = super.getContext().getContentResolver();
        }
    }

    protected void terminateManager() {
        this.mContentResolver = null;
    }

    protected boolean isNotifyForDescendents() {
        return true;
    }

    public String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    public void enable() {
        registerContentObserver();
    }

    public void disable() {
        unregisterContentObserver();
    }

    protected final void registerContentObserver() {
        if (super.getContext() != null && getUri() != null && getContentObserver() != null) {
            this.mContentResolver.registerContentObserver(getUri(), isNotifyForDescendents(), getContentObserver());
        }
    }

    protected final void unregisterContentObserver() {
        if (this.mContentResolver != null && getContentObserver() != null) {
            this.mContentResolver.unregisterContentObserver(getContentObserver());
        }
    }

    protected void updateContext(int action) {
        getContextBean().putContext(getContextValueNames()[0], action);
        notifyObserver();
    }

    protected final ContentResolver getContentResolver() {
        return this.mContentResolver;
    }
}
