package com.samsung.android.quickconnect;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.quickconnect.IQuickConnectCallback.Stub;
import java.util.ArrayList;
import java.util.Iterator;

public final class QuickConnectManager {
    public static final int DO_QUICK_CONNECT = 1;
    private static final String TAG = "QuickConnectManager";
    private Context mContext = null;
    private final ArrayList<QuickConnectListenerDelegate> mListenerDelegates = new ArrayList();
    IQuickConnectManager mQuickConnectService;

    public interface QuickConnectListener {
        void onItemSelected();
    }

    private static class QuickConnectListenerDelegate extends Stub {
        public Handler mHandler;
        public QuickConnectListener mListener;

        static class ListenerHandler extends Handler {
            public QuickConnectListener mListener = null;

            public ListenerHandler(Looper looper, QuickConnectListener listener) {
                super(looper);
                this.mListener = listener;
            }

            public void handleMessage(Message msg) {
                if (this.mListener != null) {
                    this.mListener.onItemSelected();
                }
            }
        }

        QuickConnectListenerDelegate(QuickConnectListener listener, Looper looper) {
            this.mListener = listener;
            this.mHandler = new ListenerHandler(looper, this.mListener);
        }

        public QuickConnectListener getListener() {
            return this.mListener;
        }

        public void onQuickConnectCallback() throws RemoteException {
            this.mHandler.sendEmptyMessage(1);
        }

        public String getListenerInfo() throws RemoteException {
            return this.mListener.toString();
        }
    }

    public QuickConnectManager(Context context, IQuickConnectManager quickconnectservice) {
        this.mContext = context;
        this.mQuickConnectService = quickconnectservice;
    }

    @Deprecated
    public void terminate() {
    }

    @Deprecated
    public void registerListener(QuickConnectListener listener) {
        QuickConnectListenerDelegate quickconnectListener;
        if (listener == null) {
            Log.w(TAG, "registerListener : listener is null");
            return;
        }
        Log.d(TAG, "registerListener");
        synchronized (this.mListenerDelegates) {
            try {
                QuickConnectListenerDelegate quickconnectListener2;
                Iterator<QuickConnectListenerDelegate> i = this.mListenerDelegates.iterator();
                while (i.hasNext()) {
                    QuickConnectListenerDelegate delegate = (QuickConnectListenerDelegate) i.next();
                    if (delegate.getListener() != listener) {
                        if (delegate.getListener().equals(listener)) {
                        }
                    }
                    quickconnectListener = delegate;
                }
                quickconnectListener = null;
                if (quickconnectListener == null) {
                    try {
                        quickconnectListener2 = new QuickConnectListenerDelegate(listener, this.mContext.getMainLooper());
                        this.mListenerDelegates.add(quickconnectListener2);
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        quickconnectListener2 = quickconnectListener;
                        throw th2;
                    }
                }
                quickconnectListener2 = quickconnectListener;
                if (this.mQuickConnectService != null) {
                    this.mQuickConnectService.registerCallback(quickconnectListener2, new ComponentName(this.mContext.getPackageName(), this.mContext.getClass().getCanonicalName()));
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: " + e);
            } catch (Throwable th3) {
                th2 = th3;
                throw th2;
            }
        }
    }

    public void registerListener(QuickConnectListener listener, Context context) {
        if (listener == null) {
            Log.w(TAG, "registerListener : listener is null");
            return;
        }
        this.mContext = context;
        try {
            Log.d(TAG, "registerListener with context  " + this.mContext.getClass().getCanonicalName());
        } catch (NullPointerException e) {
            Log.e(TAG, "registerListener with context, context is null ");
        }
        synchronized (this.mListenerDelegates) {
            try {
                QuickConnectListenerDelegate quickconnectListener;
                QuickConnectListenerDelegate quickconnectListener2;
                Iterator<QuickConnectListenerDelegate> i = this.mListenerDelegates.iterator();
                while (i.hasNext()) {
                    QuickConnectListenerDelegate delegate = (QuickConnectListenerDelegate) i.next();
                    if (delegate.getListener() != listener) {
                        if (delegate.getListener().equals(listener)) {
                        }
                    }
                    quickconnectListener = delegate;
                }
                quickconnectListener = null;
                if (quickconnectListener == null) {
                    try {
                        quickconnectListener2 = new QuickConnectListenerDelegate(listener, this.mContext.getMainLooper());
                        this.mListenerDelegates.add(quickconnectListener2);
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        quickconnectListener2 = quickconnectListener;
                        throw th2;
                    }
                }
                quickconnectListener2 = quickconnectListener;
                if (this.mQuickConnectService != null) {
                    this.mQuickConnectService.registerCallback(quickconnectListener2, new ComponentName(this.mContext.getPackageName(), this.mContext.getClass().getCanonicalName()));
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "RemoteException in registerListener: " + e2);
            } catch (Throwable th3) {
                th2 = th3;
                throw th2;
            }
        }
    }

    public void unregisterListener(QuickConnectListener listener) {
        if (listener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
            return;
        }
        synchronized (this.mListenerDelegates) {
            QuickConnectListenerDelegate quickconnectListener = null;
            Iterator<QuickConnectListenerDelegate> i = this.mListenerDelegates.iterator();
            while (i.hasNext()) {
                QuickConnectListenerDelegate delegate = (QuickConnectListenerDelegate) i.next();
                if (delegate.getListener() != listener) {
                    if (delegate.getListener().equals(listener)) {
                    }
                }
                quickconnectListener = delegate;
                Log.d(TAG, "unregisterListener- found");
            }
            if (quickconnectListener == null) {
                Log.i(TAG, "quickconnectListener is null");
                return;
            }
            try {
                if (this.mQuickConnectService != null) {
                    Log.i(TAG, "mQuickConnectService != null");
                    if (this.mQuickConnectService.unregisterCallback(quickconnectListener)) {
                        this.mListenerDelegates.remove(quickconnectListener);
                        quickconnectListener.mHandler = null;
                        quickconnectListener.mListener = null;
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterListener: " + e);
            }
        }
    }
}
