package com.samsung.android.security;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.SpannableStringBuilder;
import android.util.Slog;
import android.widget.TextView;

public class MemoryWipeUtils {
    public static final int DUMMY_SEND_COUNT = 64;

    public static char[] getChars(TextView tv) {
        SpannableStringBuilder ssb = (SpannableStringBuilder) tv.getEditableText();
        char[] data = new char[ssb.length()];
        for (int i = 0; i < ssb.length(); i++) {
            data[i] = ssb.charAt(i);
        }
        ssb.clear();
        return data;
    }

    public static void clear(char[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = '\u0000';
        }
    }

    public static void clear() {
        clear(null, new String(new byte[16]), 0, 16);
    }

    public static void clear(IBinder mRemote, String desc, int cmd, int size) {
        if (CCManager.isMdfEnforced() || "encrypted".equals(System.getProperty("ro.crypto.state"))) {
            long start_time = System.currentTimeMillis();
            String data = "                                ";
            if (size < 0) {
                size = 0;
            }
            size = (size / 32) + 1;
            if (mRemote != null) {
                for (int i = 0; i < 64; i++) {
                    Parcel _data = Parcel.obtain();
                    Parcel _reply = Parcel.obtain();
                    try {
                        _data.writeInterfaceToken(desc);
                        for (int j = 0; j < size; j++) {
                            _data.writeString(data);
                        }
                        mRemote.transact(cmd, _data, _reply, 0);
                        _reply.readException();
                        int _result = _reply.readInt();
                    } catch (RemoteException e) {
                    } finally {
                        _reply.recycle();
                        _data.recycle();
                    }
                }
            }
            Slog.d("MDPP", new Exception().getStackTrace()[1].getClassName() + "::count = " + 64 + ", delay = " + (System.currentTimeMillis() - start_time) + "ms");
        }
    }
}
