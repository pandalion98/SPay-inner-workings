package com.samsung.android.service.gesture;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.security.KeyChain;
import android.util.Log;
import android.view.InputEvent;
import com.samsung.android.service.gesture.IGestureService.Stub;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GestureManager {
    private static final String AIR_GESTURE_ACTIVE_STATUS = "com.samsung.android.app.gestureservice.GESTURE_ACTIVE_STATUS";
    public static final String AIR_MOTION_AIRBROWSE = "air_motion_turn";
    public static final String AIR_MOTION_AIRJUMP = "air_motion_scroll";
    public static final String AIR_MOTION_AIRPIN = "air_motion_clip";
    public static final String AIR_MOTION_CALL_ACCEPT = "air_motion_call_accept";
    public static final String AIR_MOTION_MOVE = "air_motion_item_move";
    public static final String CAMERA_PROVIDER = "camera_provider";
    public static final String IR_PROVIDER = "ir_provider";
    private static final String TAG = "GestureManager";
    public static final String TSP_PROVIDER = "tsp_provider";
    private BroadcastReceiver airGesutreActiveStatusReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.v(GestureManager.TAG, "onReceive GesutureActive :" + intent.getBooleanExtra("isActive", false));
            if (intent.getAction() != GestureManager.AIR_GESTURE_ACTIVE_STATUS) {
                return;
            }
            if (intent.getBooleanExtra("isActive", false)) {
                GestureManager.this.mGestureActive = true;
            } else {
                GestureManager.this.mGestureActive = false;
            }
        }
    };
    private IntentFilter mAirGestureActiveStatusFilter = null;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(GestureManager.TAG, "onServiceConnected");
            GestureManager.this.mService = Stub.asInterface(service);
            GestureManager.this.mBound = true;
            GestureManager.this.mConnectionListener.onServiceConnected();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.v(GestureManager.TAG, "onServiceDisconnected");
            GestureManager.this.mBound = false;
            GestureManager.this.mConnectionListener.onServiceDisconnected();
        }
    };
    private ServiceConnectionListener mConnectionListener;
    private Context mContext;
    private boolean mGestureActive = false;
    private final CopyOnWriteArrayList<GestureListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList();
    private IGestureService mService;

    private class GestureListenerDelegate extends IGestureCallback.Stub {
        private Handler mHandler;
        private final GestureListener mListener;

        GestureListenerDelegate(GestureListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = new Handler(handler == null ? GestureManager.this.mContext.getMainLooper() : handler.getLooper(), GestureManager.this) {
                public void handleMessage(Message msg) {
                    if (GestureListenerDelegate.this.mListener != null) {
                        GestureEvent gestureEvent = msg.obj;
                        if (gestureEvent != null) {
                            GestureListenerDelegate.this.mListener.onGestureEvent(gestureEvent);
                        } else {
                            Log.e(GestureManager.TAG, "gestureEvent : null");
                        }
                    }
                }
            };
        }

        public GestureListener getListener() {
            return this.mListener;
        }

        public void gestureCallback(GestureEvent event) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 0;
            msg.obj = event;
            this.mHandler.sendMessage(msg);
        }

        public String getListenerInfo() throws RemoteException {
            return this.mListener.toString();
        }
    }

    public interface ServiceConnectionListener {
        void onServiceConnected();

        void onServiceDisconnected();
    }

    public GestureManager(Context context, ServiceConnectionListener connectionListener) {
        this.mConnectionListener = connectionListener;
        this.mContext = context;
        bindtoService();
    }

    private void bindtoService() {
        Intent intent = new Intent();
        intent.setClassName("com.samsung.android.app.gestureservice", "com.samsung.android.app.gestureservice.GestureService");
        this.mContext.bindService(intent, this.mConnection, 1);
    }

    public void unbindFromService() {
        if (this.mAirGestureActiveStatusFilter != null) {
            this.mContext.unregisterReceiver(this.airGesutreActiveStatusReceiver);
        }
        if (this.mBound) {
            this.mContext.unbindService(this.mConnection);
            this.mBound = false;
        }
    }

    public void sendInputEvent(InputEvent event) {
        if (this.mAirGestureActiveStatusFilter == null) {
            this.mAirGestureActiveStatusFilter = new IntentFilter();
            this.mAirGestureActiveStatusFilter.addAction(AIR_GESTURE_ACTIVE_STATUS);
            this.mContext.registerReceiver(this.airGesutreActiveStatusReceiver, this.mAirGestureActiveStatusFilter);
        } else if (this.mBound && this.mGestureActive) {
            try {
                this.mService.sendInputEvent(event);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public void registerListener(GestureListener listener, String provider) {
        registerListener(listener, provider, "air_motion_scroll");
    }

    public void registerListener(GestureListener listener, String provider, String eventType) {
        registerListener(listener, provider, eventType, true);
    }

    public void registerListener(GestureListener listener, String provider, String eventType, boolean supportLandscape) {
        if (this.mBound) {
            Log.d(TAG, "registerListener");
            if (listener != null) {
                GestureListenerDelegate gestureListener = null;
                Iterator<GestureListenerDelegate> i = this.mListenerDelegates.iterator();
                while (i.hasNext()) {
                    GestureListenerDelegate delegate = (GestureListenerDelegate) i.next();
                    if (delegate.getListener().equals(listener)) {
                        gestureListener = delegate;
                        break;
                    }
                }
                if (gestureListener == null) {
                    gestureListener = new GestureListenerDelegate(listener, null);
                    this.mListenerDelegates.add(gestureListener);
                }
                try {
                    this.mService.registerCallback(gestureListener, provider, eventType, supportLandscape);
                    return;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in registerListener: ", e);
                    return;
                }
            }
            return;
        }
        bindtoService();
    }

    public void unregisterListener(GestureListener listener) {
        unregisterListener(listener, CAMERA_PROVIDER);
        unregisterListener(listener, IR_PROVIDER);
        unregisterListener(listener, TSP_PROVIDER);
    }

    public void unregisterListener(GestureListener listener, String provider) {
        Log.v(TAG, "unregisterListener");
        if (listener != null) {
            GestureListenerDelegate gestureListener = null;
            Iterator<GestureListenerDelegate> i = this.mListenerDelegates.iterator();
            while (i.hasNext()) {
                GestureListenerDelegate delegate = (GestureListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    gestureListener = delegate;
                    break;
                }
            }
            if (gestureListener != null) {
                try {
                    if (this.mService.unregisterCallback(gestureListener, provider)) {
                        this.mListenerDelegates.remove(gestureListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public void resetGestureService(String service) {
        try {
            this.mService.resetGestureService(service);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void enable() {
        try {
            this.mService.enable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        try {
            this.mService.disable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<String> getProviders() {
        try {
            return this.mService.getProviders();
        } catch (RemoteException ex) {
            Log.e(TAG, "getProviders: RemoteException", ex);
            return null;
        }
    }

    public GestureProviderInfo getProvider(String name) {
        GestureProviderInfo gestureProviderInfo = null;
        if (name == null) {
            throw new IllegalArgumentException("name==null");
        }
        try {
            Bundle info = this.mService.getProviderInfo(name);
            if (info != null) {
                gestureProviderInfo = createProvider(name, info);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "getProvider: RemoteException", ex);
        }
        return gestureProviderInfo;
    }

    private GestureProviderInfo createProvider(String name, Bundle info) {
        GestureProviderInfo provider = new GestureProviderInfo();
        provider.setName(info.getString(KeyChain.EXTRA_NAME));
        provider.setSupportedGestures(info.getIntegerArrayList("supported_gesture"));
        return provider;
    }
}
