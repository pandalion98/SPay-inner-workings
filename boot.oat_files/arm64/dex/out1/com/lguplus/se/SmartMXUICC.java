package com.lguplus.se;

import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.IccPcscProvider;
import java.io.IOException;
import java.util.Random;

public class SmartMXUICC {
    static final boolean DBG = true;
    public static final String EXTRA_ID = "android.nfc.extra.ID";
    public static final int RESPONSE_MAX_SIZE = 262;
    public static final int SMARTCARD_IO_ERROR_ATR_BUFFER = -6;
    public static final int SMARTCARD_IO_ERROR_CARD_NOT_EXIST = -2;
    public static final int SMARTCARD_IO_ERROR_OPEN_CHANNEL = -1;
    public static final int SMARTCARD_IO_ERROR_RESPONSE_BUFFER = -5;
    public static final int SMARTCARD_IO_ERROR_TRANSMIT_BUFFER = -4;
    public static final int SMARTCARD_IO_INVALID_CHANNEL = -3;
    public static final int SMARTCARD_IO_SUCCESS = 0;
    public static final String SMART_MX_ID = "android.nfc.smart_mx.ID";
    public static final String UICC_ID = "android.nfc.uicc.ID";
    private static int[] handleId = new int[]{0, 0, 0, 0, 0};
    static final String mLogTag = "SmartMXUICC";
    private static IccPcscProvider pcscInstance = null;
    static Random random = new Random();
    private static SmartMXUICC sInstance = null;

    public static SmartMXUICC createSmartMXUICC() {
        synchronized (SmartMXUICC.class) {
            Log.d(mLogTag, "Making an Instance...");
            if (sInstance == null) {
                sInstance = new SmartMXUICC();
            }
        }
        return sInstance;
    }

    private SmartMXUICC() {
        Log.d(mLogTag, mLogTag);
        pcscInstance = new IccPcscProvider();
        if (pcscInstance == null) {
            Log.d(mLogTag, "SmartMXUICC pcscInstance return null");
        } else {
            Log.d(mLogTag, "SmartMXUICC pcscInstance retrun : " + pcscInstance);
        }
    }

    private int openLogicalChannel() {
        int channel = pcscInstance.connect();
        if (channel <= 1 || channel >= 4) {
            Log.d(mLogTag, "openLogicalChannel Failed : " + channel);
        } else {
            Log.d(mLogTag, "openLogicalChannel channel[" + channel);
        }
        return channel;
    }

    public int openSecureElementConnection(String seType) throws IOException {
        if (seType == null) {
            throw new NullPointerException("seType must not be null");
        }
        int retVal;
        if (TelephonyManager.getDefault().getSimState() == 1) {
            retVal = -2;
        } else if (seType.equals(UICC_ID)) {
            retVal = openLogicalChannel();
        } else {
            retVal = -1;
        }
        Log.d(mLogTag, "openSecureElementConnection, retVal:" + retVal);
        if (retVal != -1) {
            return retVal;
        }
        throw new IOException("Fail to open channel");
    }

    public byte[] exchangeAPDU(int ch, byte[] data) throws IOException {
        int channel = ch;
        byte[] apdu_res = new byte[262];
        byte[] response = null;
        Log.d(mLogTag, "exchangeAPDU channel : " + channel);
        data[0] = (byte) (data[0] | channel);
        int resSize = pcscInstance.transmit(channel, data, apdu_res);
        if (resSize > 0) {
            response = new byte[resSize];
            System.arraycopy(apdu_res, 0, response, 0, resSize);
        }
        if (response != null) {
            return response;
        }
        Log.d(mLogTag, "exchangeAPDU return null");
        throw new IOException("Response is NULL");
    }

    public void closeSecureElementConnection(int ch) throws IOException {
        int channel = ch;
        Log.d(mLogTag, "closeSecureElementConnection channel : " + channel);
        if (channel < 1) {
            Log.d(mLogTag, "closeSecureElementConnection channel is wrong");
            return;
        }
        pcscInstance.disconnect(channel);
        Log.d(mLogTag, "closeSecureElementConnection done!");
    }

    public int[] getSecureElementTechList(int ch) throws IOException {
        return null;
    }

    public byte[] getSecureElementUid(int ch) throws IOException {
        return null;
    }

    public byte[] getATR() {
        byte[] apdu_res = new byte[262];
        byte[] response = null;
        try {
            int resSize = pcscInstance.getATR(apdu_res);
            if (resSize > 0) {
                response = new byte[resSize];
                System.arraycopy(apdu_res, 0, response, 0, resSize);
            }
            return response;
        } catch (Exception e) {
            Log.d(mLogTag, "getATR Errors");
            return null;
        }
    }

    public boolean isSmartMX() {
        return false;
    }
}
