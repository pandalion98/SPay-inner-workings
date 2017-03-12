package com.samsung.android.cocktailbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.widget.RemoteViews;
import com.samsung.android.cocktailbar.ICocktailHost.Stub;
import java.lang.ref.WeakReference;

public class CocktailHost {
    static final int HANDLE_COCKTAIL_CLOSE_CONTEXTUAL = 5;
    static final int HANDLE_COCKTAIL_PARTIALLY_UPDATE = 2;
    static final int HANDLE_COCKTAIL_REMOVE = 3;
    static final int HANDLE_COCKTAIL_SEND_EXTRA_DATA = 12;
    static final int HANDLE_COCKTAIL_SHOW = 4;
    static final int HANDLE_COCKTAIL_SWITCH_DEFAULT = 10;
    static final int HANDLE_COCKTAIL_TICKER_DISABLE = 9;
    static final int HANDLE_COCKTAIL_UPDATE = 1;
    static final int HANDLE_COCKTAIL_UPDATE_EXTRA = 8;
    static final int HANDLE_COCKTAIL_UPDATE_TOOL_LAUNCHER = 7;
    static final int HANDLE_COCKTAIL_VIEW_DATA_CHANGED = 6;
    static final int HANDLE_NOTIFY_CHANGE_VISIBLE_EDGE_SERVICE = 102;
    static final int HANDLE_NOTIFY_KEYGUARD_STATE = 100;
    static final int HANDLE_NOTIFY_WAKEUP_STATE = 101;
    static ICocktailBarService sService;
    static final Object sServiceLock = new Object();
    ICallbackListener mCallbackListener;
    private final Callbacks mCallbacks;
    private String mContextOpPackageName;
    private final Handler mHandler;

    static class Callbacks extends Stub {
        private final WeakReference<Handler> mWeakHandler;

        public Callbacks(Handler handler) {
            this.mWeakHandler = new WeakReference(handler);
        }

        public void updateCocktail(int cocktailId, Cocktail cocktail, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(1, cocktailId, userId, cocktail).sendToTarget();
            }
        }

        public void partiallyUpdateCocktail(int cocktailId, RemoteViews contentView, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(2, cocktailId, userId, contentView).sendToTarget();
            }
        }

        public void removeCocktail(int cocktailId, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(3, cocktailId, userId).sendToTarget();
            }
        }

        public void showCocktail(int cocktailId, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(4, cocktailId, userId).sendToTarget();
            }
        }

        public void closeContextualCocktail(int cocktailId, int category, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(5, cocktailId, category, Integer.valueOf(userId)).sendToTarget();
            }
        }

        public void viewDataChanged(int cocktailId, int viewId, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(6, cocktailId, viewId, Integer.valueOf(userId)).sendToTarget();
            }
        }

        public void updateToolLauncher(int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(7, userId, 0).sendToTarget();
            }
        }

        public void notifyKeyguardState(boolean enable, int userId) {
            int i = 1;
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                if (!enable) {
                    i = 0;
                }
                handler.obtainMessage(100, i, userId).sendToTarget();
            }
        }

        public void notifyWakeUpState(boolean bEnable, int keyCode, int reason) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(101, bEnable ? 1 : 0, keyCode, Integer.valueOf(reason)).sendToTarget();
            }
        }

        public void switchDefaultCocktail(int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(10, userId, 0).sendToTarget();
            }
        }

        public void sendExtraData(int userId, Bundle extraData) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(12, userId, 0, extraData).sendToTarget();
            }
        }

        public void setDisableTickerView(int state, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(9, state, userId).sendToTarget();
            }
        }

        public void changeVisibleEdgeService(boolean visible, int userId) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(102, visible ? 1 : 0, userId).sendToTarget();
            }
        }
    }

    public interface ICallbackListener {
        void onChangeVisibleEdgeService(boolean z, int i);

        void onCloseContextualCocktail(int i, int i2, int i3);

        void onNotifyKeyguardState(boolean z, int i);

        void onNotifyWakeUpModeState(boolean z, int i, int i2);

        void onPartiallyUpdateCocktail(int i, RemoteViews remoteViews, int i2);

        void onRemoveCocktail(int i, int i2);

        void onSendExtraDataToCocktailBar(Bundle bundle, int i);

        void onSetDisableTickerView(int i, int i2);

        void onShowCocktail(int i, int i2);

        void onSwitchDefaultCocktail(int i);

        void onUpdateCocktail(int i, Cocktail cocktail, int i2);

        void onUpdateToolLauncher(int i);

        void onViewDataChanged(int i, int i2, int i3);
    }

    class UpdateHandler extends Handler {
        public UpdateHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CocktailHost.this.updateCocktail(msg.arg1, (Cocktail) msg.obj, msg.arg2);
                    return;
                case 2:
                    CocktailHost.this.partiallyUpdateCocktail(msg.arg1, (RemoteViews) msg.obj, msg.arg2);
                    return;
                case 3:
                    CocktailHost.this.removeCocktail(msg.arg1, msg.arg2);
                    return;
                case 4:
                    CocktailHost.this.showCocktail(msg.arg1, msg.arg2);
                    return;
                case 5:
                    CocktailHost.this.closeContextualCocktail(msg.arg1, msg.arg2, ((Integer) msg.obj).intValue());
                    return;
                case 6:
                    CocktailHost.this.viewDataChanged(msg.arg1, msg.arg2, ((Integer) msg.obj).intValue());
                    return;
                case 7:
                    CocktailHost.this.updateToolLauncher(msg.arg1);
                    return;
                case 9:
                    CocktailHost.this.setDisableTickerView(msg.arg1, msg.arg2);
                    return;
                case 10:
                    CocktailHost.this.switchDefaultCocktail(msg.arg1);
                    return;
                case 12:
                    CocktailHost.this.sendExtraDataToCocktailBar(msg.arg1, (Bundle) msg.obj);
                    return;
                case 100:
                    CocktailHost.this.notifyKeyguardState(msg.arg1, msg.arg2);
                    return;
                case 101:
                    CocktailHost.this.notifyWakeUpState(msg.arg1, msg.arg2, ((Integer) msg.obj).intValue());
                    return;
                case 102:
                    CocktailHost.this.changeVisibleEdgeService(msg.arg1, msg.arg2);
                    return;
                default:
                    return;
            }
        }
    }

    public CocktailHost(Context context, ICallbackListener callbackListener) {
        this(context, callbackListener, context.getMainLooper());
    }

    public CocktailHost(Context context, int category, ICallbackListener callbackListener) {
        this(context, category, callbackListener, context.getMainLooper());
    }

    public CocktailHost(Context context, ICallbackListener callbackListener, Looper looper) {
        this.mContextOpPackageName = context.getOpPackageName();
        this.mCallbackListener = callbackListener;
        this.mHandler = new UpdateHandler(looper);
        this.mCallbacks = new Callbacks(this.mHandler);
        bindService(0);
    }

    public CocktailHost(Context context, int category, ICallbackListener callbackListener, Looper looper) {
        this.mContextOpPackageName = context.getOpPackageName();
        this.mCallbackListener = callbackListener;
        this.mHandler = new UpdateHandler(looper);
        this.mCallbacks = new Callbacks(this.mHandler);
        bindService(category);
    }

    private void bindService(int category) {
        synchronized (sServiceLock) {
            if (sService == null) {
                sService = ICocktailBarService.Stub.asInterface(ServiceManager.getService("CocktailBarService"));
            }
            try {
                sService.setCocktailHostCallbacks(this.mCallbacks, this.mContextOpPackageName, category);
            } catch (RemoteException e) {
            }
        }
    }

    public void startListening() {
        try {
            sService.startListening(this.mCallbacks, this.mContextOpPackageName, 0);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public void startListening(int category) {
        try {
            sService.startListening(this.mCallbacks, this.mContextOpPackageName, category);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public void stopListening() {
        try {
            sService.stopListening(this.mContextOpPackageName);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    private void updateCocktail(int cocktailId, Cocktail cocktail, int userId) {
        this.mCallbackListener.onUpdateCocktail(cocktailId, cocktail, userId);
    }

    private void partiallyUpdateCocktail(int cocktailId, RemoteViews contentView, int userId) {
        this.mCallbackListener.onPartiallyUpdateCocktail(cocktailId, contentView, userId);
    }

    private void removeCocktail(int cocktailId, int userId) {
        this.mCallbackListener.onRemoveCocktail(cocktailId, userId);
    }

    private void showCocktail(int cocktailId, int userId) {
        this.mCallbackListener.onShowCocktail(cocktailId, userId);
    }

    private void closeContextualCocktail(int cocktailId, int category, int userId) {
        this.mCallbackListener.onCloseContextualCocktail(cocktailId, category, userId);
    }

    private void viewDataChanged(int cocktailId, int viewId, int userId) {
        this.mCallbackListener.onViewDataChanged(cocktailId, viewId, userId);
    }

    private void updateToolLauncher(int userId) {
        this.mCallbackListener.onUpdateToolLauncher(userId);
    }

    private void notifyKeyguardState(int enable, int userId) {
        boolean z = true;
        ICallbackListener iCallbackListener = this.mCallbackListener;
        if (enable != 1) {
            z = false;
        }
        iCallbackListener.onNotifyKeyguardState(z, userId);
    }

    private void notifyWakeUpState(int bEnable, int keyCode, int reason) {
        boolean z = true;
        ICallbackListener iCallbackListener = this.mCallbackListener;
        if (bEnable != 1) {
            z = false;
        }
        iCallbackListener.onNotifyWakeUpModeState(z, keyCode, reason);
    }

    private void switchDefaultCocktail(int userId) {
        this.mCallbackListener.onSwitchDefaultCocktail(userId);
    }

    private void sendExtraDataToCocktailBar(int userId, Bundle extraData) {
        this.mCallbackListener.onSendExtraDataToCocktailBar(extraData, userId);
    }

    private void setDisableTickerView(int state, int userId) {
        this.mCallbackListener.onSetDisableTickerView(state, userId);
    }

    private void changeVisibleEdgeService(int visible, int userId) {
        boolean z = true;
        ICallbackListener iCallbackListener = this.mCallbackListener;
        if (visible != 1) {
            z = false;
        }
        iCallbackListener.onChangeVisibleEdgeService(z, userId);
    }
}
