package android.bluetooth;

import android.os.Debug;

public final class Log {
    private static final boolean DBG;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public static void v(String tag, String msg) {
        if (DBG) {
            android.util.Log.v(tag, msg);
        }
        BluetoothDump.BtLog(tag + " -- " + msg);
    }

    public static void d(String tag, String msg) {
        if (DBG) {
            android.util.Log.d(tag, msg);
        }
        BluetoothDump.BtLog(tag + " -- " + msg);
    }

    public static void e(String tag, String msg) {
        if (DBG) {
            android.util.Log.d(tag, msg);
        }
        BluetoothDump.BtLog(tag + " -- " + msg);
    }

    public static void i(String tag, String msg) {
        if (DBG) {
            android.util.Log.d(tag, msg);
        }
        BluetoothDump.BtLog(tag + " -- " + msg);
    }

    public static void w(String tag, String msg) {
        if (DBG) {
            android.util.Log.d(tag, msg);
        }
        BluetoothDump.BtLog(tag + " -- " + msg);
    }
}
