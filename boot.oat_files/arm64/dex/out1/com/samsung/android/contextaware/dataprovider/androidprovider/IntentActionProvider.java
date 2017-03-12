package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class IntentActionProvider extends AndroidProvider {
    private IntentFilter mIntentFilter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public final void onReceive(Context context, Intent intent) {
            if (intent == null) {
                CaLogger.info("intent is null");
            } else if (intent.getAction().equals(IntentActionProvider.this.getIntentFilterName())) {
                IntentActionProvider.this.updateContext(intent);
            }
        }
    };

    protected abstract String getIntentFilterName();

    protected abstract void updateContext(Intent intent);

    protected IntentActionProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
        } else if (getIntentFilterName() == null || getIntentFilterName().isEmpty()) {
            CaLogger.error("mIntentAction is null");
        } else {
            this.mIntentFilter = new IntentFilter(getIntentFilterName());
        }
    }

    protected final void terminateManager() {
        this.mIntentFilter = null;
    }

    public String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    protected int getIntentAction() {
        return 0;
    }

    public void enable() {
        if (super.getContext() == null || this.mReceiver == null || this.mIntentFilter == null) {
            CaLogger.error("cannot enable");
        } else {
            super.getContext().registerReceiver(this.mReceiver, this.mIntentFilter);
        }
    }

    public void disable() {
        if (super.getContext() == null || this.mReceiver == null) {
            CaLogger.error("cannot disable");
        } else {
            super.getContext().unregisterReceiver(this.mReceiver);
        }
    }
}
