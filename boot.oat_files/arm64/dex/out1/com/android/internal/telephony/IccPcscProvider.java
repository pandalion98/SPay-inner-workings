package com.android.internal.telephony;

import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.telephony.ITelephony.Stub;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IccPcscProvider {
    public static final int CONNECT = 4;
    public static final int DISCONNECT = 5;
    public static final int INIT = 1;
    private static final int OEM_AUTH_ATR = 13;
    private static final int OEM_AUTH_OPEN_CHANNEL = 9;
    private static final int OEM_AUTH_SEND_APDU = 8;
    private static final int OEM_DOMESTIC_PCSC_POWERDOWN = 40;
    private static final int OEM_DOMESTIC_PCSC_POWERUP = 38;
    private static final int OEM_DOMESTIC_PCSC_TRANSMIT = 39;
    private static final int OEM_FUNCTION_ID_AUTH = 21;
    private static final int OEM_FUNCTION_ID_DOMESTIC = 22;
    public static final int POWERDOWN = 3;
    public static final int POWERUP = 2;
    public static final int RESPONSE_MAX_SIZE = 262;
    public static final int SIM_STATE_ABSENT = 1;
    public static final int SIM_STATE_NETWORK_LOCKED = 4;
    public static final int SIM_STATE_PIN_REQUIRED = 2;
    public static final int SIM_STATE_PUK_REQUIRED = 3;
    public static final int SIM_STATE_READY = 5;
    public static final int SIM_STATE_UNKNOWN = 0;
    public static final int SMARTCARD_IO_ERROR_ATR_BUFFER = -6;
    public static final int SMARTCARD_IO_ERROR_CARD_NOT_EXIST = -2;
    public static final int SMARTCARD_IO_ERROR_OPEN_CHANNEL = -1;
    public static final int SMARTCARD_IO_ERROR_RESPONSE_BUFFER = -5;
    public static final int SMARTCARD_IO_ERROR_TRANSMIT_BUFFER = -4;
    public static final int SMARTCARD_IO_INVALID_CHANNEL = -3;
    public static final int SMARTCARD_IO_SUCCESS = 0;
    public static final int TRANSMIT = 6;
    public static final int USIMAUTH = 7;
    private static final int lastChannel = 3;
    static final String mLogTag = "RIL_IccPcscProvider";
    private static IccPcscProvider scInstance;
    private int NUM_OF_CHANNEL = 4;
    private byte[] _atr;
    private int cardStatus;
    private int[] channel;
    private boolean isInitiated = false;
    private final Object scLock = new Object();

    public IccPcscProvider() {
        pscsPowerup();
    }

    private void pscsPowerup() {
        Log.d(mLogTag, "pscsPowerup");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(22);
            dos.writeByte(38);
            dos.writeShort(4);
            try {
                byte[] response = new byte[262];
                getTelephonyService().sendRequestToRIL(bos.toByteArray(), response, 2);
                int atrLength = response[0];
                Log.d(mLogTag, "pscsPowerup ATR:" + bytesToHexString(response));
                Log.d(mLogTag, "pscsPowerup atrLength:" + atrLength);
                this._atr = new byte[atrLength];
                System.arraycopy(response, 2, this._atr, 0, atrLength);
                this.isInitiated = true;
            } catch (Exception e) {
                e.printStackTrace();
                this.isInitiated = false;
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e2) {
                    Log.w("pscsPowerup", "close fail!!!");
                    return;
                }
            }
            if (bos != null) {
                bos.close();
            }
        } catch (IOException e3) {
            Log.d(mLogTag, "IOException - connect");
        }
    }

    public void finalize() {
        pcscPowerdown();
    }

    private void pcscPowerdown() {
        Log.d(mLogTag, "pcscPowerdown");
    }

    public int connect() {
        if (!this.isInitiated) {
            pscsPowerup();
            this.isInitiated = true;
        }
        Log.d(mLogTag, "connect");
        if (!"LGT".equals("EUR")) {
            return connectToRIL();
        }
        int logiCh = connectToRIL();
        if (logiCh == -1) {
            disconnectFromRIL(3);
            logiCh = connectToRIL();
        }
        return logiCh;
    }

    private int connectToRIL() {
        int i = -1;
        Log.d(mLogTag, "connectToRIL");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(22);
            dos.writeByte(39);
            dos.writeShort(9);
            dos.writeByte(0);
            dos.writeByte(112);
            dos.writeByte(0);
            if ("LGT".equals("EUR")) {
                dos.writeByte(3);
                dos.writeByte(0);
            } else {
                dos.writeByte(0);
                dos.writeByte(1);
            }
            try {
                int val = getTelephonyService().sendRequestToRIL(bos.toByteArray(), new byte[1], 4);
                dos.close();
                bos.close();
                dos = null;
                bos = null;
                if ("LGT".equals("EUR") && val == 0) {
                    val = 3;
                }
                return val;
            } catch (Exception e) {
                Log.d(mLogTag, "Exception - connect");
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e2) {
                        return i;
                    }
                }
                if (bos == null) {
                    return i;
                }
                bos.close();
                return i;
            }
        } catch (IOException e3) {
            Log.d(mLogTag, "IOException - connect");
            return i;
        }
    }

    public int transmit(int channel, byte[] command, byte[] response) {
        if (command == null) {
            return -4;
        }
        if (response == null) {
            return -5;
        }
        return transmitToRIL(channel, command, response);
    }

    private int transmitToRIL(int channel, byte[] command, byte[] response) {
        Log.d(mLogTag, "transmitToRIL");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            int fileSize = command.length + 4;
            dos.writeByte(22);
            dos.writeByte(39);
            dos.writeShort(fileSize);
            for (byte writeByte : command) {
                dos.writeByte(writeByte);
            }
            try {
                int val = getTelephonyService().sendRequestToRIL(bos.toByteArray(), response, 6);
                dos.close();
                bos.close();
                return val;
            } catch (Exception e) {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e2) {
                        e.printStackTrace();
                        return -1;
                    }
                }
                if (bos != null) {
                    bos.close();
                }
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e3) {
            Log.d(mLogTag, "IOException - transmit");
            return -1;
        }
    }

    public int disconnect(int channel) {
        Log.d(mLogTag, "disconnect");
        return disconnectFromRIL(channel);
    }

    private int disconnectFromRIL(int channel) {
        Log.d(mLogTag, "disconnectFromRIL");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(22);
            dos.writeByte(39);
            dos.writeShort(8);
            dos.writeByte(0);
            dos.writeByte(112);
            dos.writeByte(128);
            dos.writeByte(channel);
            try {
                int val = getTelephonyService().sendRequestToRIL(bos.toByteArray(), new byte[1], 5);
                dos.close();
                bos.close();
                return val;
            } catch (Exception e) {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e2) {
                        e.printStackTrace();
                        return -1;
                    }
                }
                if (bos != null) {
                    bos.close();
                }
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e3) {
            Log.d(mLogTag, "IO Exception - Disconnect");
            return -1;
        }
    }

    public int getATR(byte[] atr) {
        int size = this._atr.length;
        if (atr == null || atr.length < size) {
            Log.d(mLogTag, "getATR SMARTCARD_IO_ERROR_ATR_BUFFER");
            return -6;
        }
        System.arraycopy(this._atr, 0, atr, 0, size);
        return size;
    }

    private ITelephony getTelephonyService() {
        ITelephony telephonyService = Stub.asInterface(ServiceManager.checkService(PhoneConstants.PHONE_KEY));
        if (telephonyService == null) {
            Log.w(mLogTag, "Unable to find ITelephony interface.");
        }
        return telephonyService;
    }

    private static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append("0123456789abcdef".charAt((bytes[i] >> 4) & 15));
            ret.append("0123456789abcdef".charAt(bytes[i] & 15));
        }
        return ret.toString();
    }
}
