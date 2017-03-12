package com.samsung.android.contextaware.utilbundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaBootStatus implements IUtilManager {
    private static final int BOOT_COMPLETED = 4099;
    private static final int LOG_CONTEXT_NULL = 4097;
    private static final int LOG_INTENT_NULL = 4098;
    private static volatile CaBootStatus instance;
    private boolean mBootComplete = false;
    private Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == CaBootStatus.LOG_CONTEXT_NULL) {
                CaLogger.info("context is null");
            } else if (msg.what == 4098) {
                CaLogger.info("intent is null");
            } else if (msg.what == CaBootStatus.BOOT_COMPLETED) {
                CaLogger.info("Boot Complete");
                if (CaBootStatus.this.mContext != null) {
                    CaBootStatus.this.mContext.unregisterReceiver(CaBootStatus.this.mReceiver);
                }
                CaBootStatus.this.mBootComplete = true;
                CaBootStatus.this.notifyObservers();
            }
        }
    };
    private final CopyOnWriteArrayList<IBootStatusObserver> mListeners = new CopyOnWriteArrayList();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaBootStatus.this.mHandler.sendEmptyMessage(CaBootStatus.LOG_CONTEXT_NULL);
            } else if (intent == null) {
                CaBootStatus.this.mHandler.sendEmptyMessage(4098);
            } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                CaBootStatus.this.mHandler.sendEmptyMessage(CaBootStatus.BOOT_COMPLETED);
            }
        }
    };

    public static CaBootStatus getInstance() {
        if (instance == null) {
            synchronized (CaBootStatus.class) {
                if (instance == null) {
                    instance = new CaBootStatus();
                }
            }
        }
        return instance;
    }

    public void initializeManager(Context context) {
        this.mContext = context;
        checkBootComplete();
    }

    public boolean isBootComplete() {
        return this.mBootComplete;
    }

    public final void registerObserver(IBootStatusObserver observer) {
        if (!this.mListeners.contains(observer)) {
            this.mListeners.add(observer);
        }
    }

    public final void unregisterObserver(IBootStatusObserver observer) {
        if (this.mListeners.contains(observer)) {
            this.mListeners.remove(observer);
        }
    }

    private void notifyObservers() {
        Iterator<IBootStatusObserver> i = this.mListeners.iterator();
        while (i.hasNext()) {
            IBootStatusObserver observer = (IBootStatusObserver) i.next();
            if (observer != null) {
                observer.bootCompleted();
            }
        }
    }

    private void checkBootComplete() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        if (this.mContext != null) {
            this.mContext.registerReceiver(this.mReceiver, filter);
        }
    }

    public void terminateManager() {
    }
}
