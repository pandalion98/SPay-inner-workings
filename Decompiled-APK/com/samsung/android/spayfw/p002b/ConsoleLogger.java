package com.samsung.android.spayfw.p002b;

import android.util.Log;

/* renamed from: com.samsung.android.spayfw.b.b */
public class ConsoleLogger extends Logger {
    public ConsoleLogger(String str) {
        super(str);
    }

    public int m278H(int i) {
        if (i == 1) {
            return 3;
        }
        return i;
    }

    public void m279a(int i, String str, String str2) {
        if (str2 != null && str != null && isLoggable(i)) {
            int length = str2.length() / 2000;
            for (int i2 = 0; i2 < length; i2++) {
                Log.println(m278H(i), "SpayFw_" + str, str2.substring(i2 * 2000, (i2 + 1) * 2000));
            }
            Log.println(m278H(i), "SpayFw_" + str, str2.substring(length * 2000, str2.length()));
        }
    }
}
