package com.samsung.android.contextaware.utilbundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaTimeChangeManager implements IUtilManager {
    private static final int LOG_CONTEXT_NULL = 4113;
    private static final int LOG_INTENT_NULL = 4114;
    private static final int TIME_CHANGED = 4115;
    private static volatile CaTimeChangeManager instance;
    private boolean mAutoCheck;
    private Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == CaTimeChangeManager.LOG_CONTEXT_NULL) {
                CaLogger.info("context is null");
            } else if (msg.what == CaTimeChangeManager.LOG_INTENT_NULL) {
                CaLogger.info("intent is null");
            } else if (msg.what == CaTimeChangeManager.TIME_CHANGED) {
                boolean autoCheck = false;
                try {
                    autoCheck = Global.getInt(CaTimeChangeManager.this.mContext.getContentResolver(), Settings$System.AUTO_TIME) > 0;
                } catch (SettingNotFoundException e) {
                    CaLogger.error("settings not found");
                    e.printStackTrace();
                }
                CaLogger.info("Time Change, auto old:" + CaTimeChangeManager.this.mAutoCheck + " new:" + autoCheck);
                if ((!CaTimeChangeManager.this.mAutoCheck && autoCheck) || !autoCheck) {
                    CaTimeChangeManager.this.notifyObservers();
                }
                CaTimeChangeManager.this.mAutoCheck = autoCheck;
            }
        }
    };
    private final CopyOnWriteArrayList<ITimeChangeObserver> mListeners = new CopyOnWriteArrayList();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaTimeChangeManager.this.mHandler.sendEmptyMessage(CaTimeChangeManager.LOG_CONTEXT_NULL);
            } else if (intent == null) {
                CaTimeChangeManager.this.mHandler.sendEmptyMessage(CaTimeChangeManager.LOG_INTENT_NULL);
            } else if (intent.getAction().equals("android.intent.action.TIME_SET")) {
                CaTimeChangeManager.this.mHandler.sendEmptyMessage(CaTimeChangeManager.TIME_CHANGED);
            }
        }
    };

    public static CaTimeChangeManager getInstance() {
        if (instance == null) {
            synchronized (CaTimeChangeManager.class) {
                if (instance == null) {
                    instance = new CaTimeChangeManager();
                }
            }
        }
        return instance;
    }

    public void initializeManager(Context context) {
        this.mContext = context;
        checkTimeChange();
        try {
            this.mAutoCheck = Global.getInt(context.getContentResolver(), Settings$System.AUTO_TIME) > 0;
        } catch (SettingNotFoundException e) {
            CaLogger.error("settings not found");
            e.printStackTrace();
        }
    }

    public final void registerObserver(ITimeChangeObserver observer) {
        if (!this.mListeners.contains(observer)) {
            this.mListeners.add(observer);
        }
    }

    public final void unregisterObserver(ITimeChangeObserver observer) {
        if (this.mListeners.contains(observer)) {
            this.mListeners.remove(observer);
        }
    }

    private void notifyObservers() {
        Iterator<ITimeChangeObserver> i = this.mListeners.iterator();
        while (i.hasNext()) {
            ITimeChangeObserver observer = (ITimeChangeObserver) i.next();
            if (observer != null) {
                observer.onTimeChanged();
            }
        }
    }

    private void checkTimeChange() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_SET");
        if (this.mContext != null) {
            this.mContext.registerReceiver(this.mReceiver, filter);
        }
    }

    public void terminateManager() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }
}
