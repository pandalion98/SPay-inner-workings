package com.android.internal.telephony;

import android.util.Log;

public class SmartcardInternal {
    public static final int SMARTCARD_IO_ERROR_ATR_BUFFER = -6;
    public static final int SMARTCARD_IO_ERROR_CARD_NOT_EXIST = -2;
    public static final int SMARTCARD_IO_ERROR_OPEN_CHANNEL = -1;
    public static final int SMARTCARD_IO_ERROR_RESPONSE_BUFFER = -5;
    public static final int SMARTCARD_IO_ERROR_TRANSMIT_BUFFER = -4;
    public static final int SMARTCARD_IO_INVALID_CHANNEL = -3;
    public static final int SMARTCARD_IO_SUCCESS = 0;
    static final String mLogTag = "SmartcardInternal";
    private static IccPcscProvider pcscInstance = null;
    private static SmartcardInternal scInstance = null;

    public static SmartcardInternal getInstance() {
        if (scInstance == null) {
            synchronized (SmartcardInternal.class) {
                Log.d(mLogTag, "Making an Instance...");
                if (scInstance == null) {
                    scInstance = new SmartcardInternal();
                }
            }
        }
        return scInstance;
    }

    private SmartcardInternal() {
        pcscInstance = new IccPcscProvider();
    }

    public void finalize() {
        pcscInstance.finalize();
    }

    public int connect() {
        return pcscInstance.connect();
    }

    public int transmit(int channel, byte[] command, byte[] response) {
        return pcscInstance.transmit(channel, command, response);
    }

    public int disconnect(int channel) {
        return pcscInstance.disconnect(channel);
    }

    public int getATR(byte[] atr) {
        return pcscInstance.getATR(atr);
    }
}
