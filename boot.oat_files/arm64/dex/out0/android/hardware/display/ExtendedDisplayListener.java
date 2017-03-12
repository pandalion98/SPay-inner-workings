package android.hardware.display;

import android.util.Log;

public class ExtendedDisplayListener {
    private static final String TAG = "ExtendedDisplayListener";

    public void onConnectionStatusChanged(int status) {
        Log.d(TAG, "onConnectionStatusChanged is called as default! do nothing!");
    }

    public void onQoSLevelChanged(int level) {
        Log.d(TAG, "onQoSLevelChanged is called as default! do nothing!");
    }

    public void onBridgeStatusChanged(int status) {
        Log.d(TAG, "onBridgeStatusChanged is called as default! do nothing!");
    }

    public void onDLNAConnectionStatusChanged(boolean connected) {
        Log.d(TAG, "onDLNAConnectionStatusChanged is called as default! do nothing!");
    }

    public void onScreenSharingStatusChanged(int status) {
        Log.d(TAG, "onScreenSharingStatusChanged is called as default! do nothing!");
    }
}
