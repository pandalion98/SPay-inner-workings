package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.cover.CoverManager;
import com.samsung.android.cover.CoverManager.StateListener;
import com.samsung.android.cover.CoverState;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaCoverManager implements IUtilManager, IBootStatusObserver {
    private static final int COVER_TYPE_FLIP = 1;
    private static final int COVER_TYPE_NONE = 0;
    private static final int COVER_TYPE_VIEW = 2;
    private static volatile CaCoverManager instance;
    private Context mContext;
    private CoverManager mCoverManager = null;
    private final StateListener mCoverStateListener = new StateListener() {
        public void onCoverStateChanged(CoverState state) {
            CaLogger.info("state:" + state);
            if (state != null) {
                CaCoverManager.this.mCurrentCoverState = state.getSwitchState();
                CaCoverManager.this.mCurrentCoverType = state.getType();
                CaCoverManager.this.notifyObservers(state);
                return;
            }
            CaLogger.error("state is null");
        }
    };
    private boolean mCurrentCoverState = true;
    private int mCurrentCoverType = 0;
    private final CopyOnWriteArrayList<ICoverStatusChangeObserver> mListeners = new CopyOnWriteArrayList();
    private final Looper mLooper;

    public CaCoverManager(Looper looper) {
        this.mLooper = looper;
    }

    public static CaCoverManager getInstance(Looper looper) {
        if (instance == null) {
            synchronized (CaCoverManager.class) {
                if (instance == null) {
                    instance = new CaCoverManager(looper);
                }
            }
        }
        return instance;
    }

    public void initializeManager(Context context) {
        CaBootStatus.getInstance().registerObserver(this);
        this.mContext = context;
    }

    public final void registerObserver(ICoverStatusChangeObserver observer) {
        if (!this.mListeners.contains(observer)) {
            this.mListeners.add(observer);
        }
    }

    public final void unregisterObserver(ICoverStatusChangeObserver observer) {
        if (this.mListeners.contains(observer)) {
            this.mListeners.remove(observer);
        }
    }

    private void notifyObservers(CoverState state) {
        Iterator<ICoverStatusChangeObserver> i = this.mListeners.iterator();
        while (i.hasNext()) {
            ICoverStatusChangeObserver observer = (ICoverStatusChangeObserver) i.next();
            if (observer != null) {
                observer.onCoverStatusChanged(state);
            }
        }
    }

    public void terminateManager() {
        new Handler(this.mLooper).postDelayed(new Runnable() {
            public void run() {
                if (CaCoverManager.this.mCoverManager != null && CaCoverManager.this.mCoverStateListener != null) {
                    CaCoverManager.this.mCoverManager.unregisterListener(CaCoverManager.this.mCoverStateListener);
                }
            }
        }, 0);
    }

    public boolean getCoverState() {
        CaLogger.info("State:" + this.mCurrentCoverState);
        return this.mCurrentCoverState;
    }

    public int getCoverType() {
        CaLogger.info("Type:" + this.mCurrentCoverType);
        switch (this.mCurrentCoverType) {
            case 0:
            case 4:
            case 5:
            case 7:
            case 100:
                return 1;
            case 1:
            case 3:
            case 6:
                return 2;
            default:
                return 0;
        }
    }

    public void bootCompleted() {
        this.mCoverManager = new CoverManager(this.mContext);
        if (this.mLooper != null) {
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    if (CaCoverManager.this.mCoverManager == null || CaCoverManager.this.mCoverStateListener == null) {
                        CaLogger.error("cover null");
                    } else {
                        CaCoverManager.this.mCoverManager.registerListener(CaCoverManager.this.mCoverStateListener);
                    }
                }
            }, 0);
        } else {
            CaLogger.error("looper null");
        }
    }
}
