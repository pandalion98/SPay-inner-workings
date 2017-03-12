package com.samsung.android.service.gesture;

import android.content.Context;
import android.view.InputEvent;
import com.samsung.android.service.gesture.GestureManager.ServiceConnectionListener;

public class TspInputEventObserver {
    public static final String NAME = "tsp_inputevent_observer";
    private boolean mConnected = false;
    private GestureManager mGestureManager;

    public TspInputEventObserver(Context context) {
        this.mGestureManager = new GestureManager(context, new ServiceConnectionListener() {
            public void onServiceDisconnected() {
                TspInputEventObserver.this.mConnected = false;
            }

            public void onServiceConnected() {
                TspInputEventObserver.this.mConnected = true;
            }
        });
    }

    public final void onInputEvent(InputEvent event) {
        if (this.mConnected) {
            this.mGestureManager.sendInputEvent(event);
        }
    }
}
