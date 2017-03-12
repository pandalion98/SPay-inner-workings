package android.media;

import android.content.Context;
import android.util.Log;

public class UIBCInputHandler {
    private static final String TAG = "UIBCInputHandler_JNI.java";
    private Context mContext = null;

    public static native void handleDown(int i, int[] iArr, int[] iArr2, int[] iArr3);

    public static native void handleMove(int i, int[] iArr, int[] iArr2, int[] iArr3);

    public static native void handleRotate(int i, int i2);

    public static native void handleScroll(float f, float f2);

    public static native void handleUp(int i, int[] iArr, int[] iArr2, int[] iArr3);

    public static native boolean isActiveUIBC();

    public static native void keyDown(int i, int i2);

    public static native void keyDownASCII(int i, int i2);

    public static native void keyUp(int i, int i2);

    public static native void keyUpASCII(int i, int i2);

    static {
        Log.d(TAG, "try to load libuibc.so");
        System.loadLibrary("uibc");
    }

    public UIBCInputHandler(Context context) {
        this.mContext = context;
        Log.v(TAG, "UIBCInputHandler construct");
    }

    public boolean isActive() {
        boolean isActive = false;
        try {
            isActive = isActiveUIBC();
            Log.v(TAG, "UIBC Active = " + isActive);
            return isActive;
        } catch (NoSuchMethodError e) {
            Log.e(TAG, "NoSuchMethod - mWfdSinkManager.isActiveUIBC()");
            return isActive;
        }
    }
}
