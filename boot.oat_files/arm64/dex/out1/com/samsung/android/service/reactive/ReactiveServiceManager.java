package com.samsung.android.service.reactive;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.reactive.IReactiveService.Stub;
import com.samsung.android.smartface.SmartFaceManager;

public final class ReactiveServiceManager {
    public static final int FLAG_ACTIVATED = 1;
    public static final int FLAG_DEACTIVATED = 0;
    public static final int FLAG_DEACTIVATED_WITH_ACCOUNT = 3;
    public static final int FLAG_TRIGGERED = 2;
    public static final int FRP_FLAG = 2;
    public static final int FRP_SERIVCE_OPERATION_FAILED = -7;
    public static final int GOOGLE_FACTORY_RESET_PROTECTION_IS_SUPPORTED = 2;
    private static final int RC_VT_VALID_SIZE = 32;
    public static final int REACTIVATION_FLAG = 0;
    public static final int REACTIVE_SERVICE_EXCEPTION_ERROR = -10;
    public static final int REACTIVE_SERVICE_INVALID_ARGUMENTS = -8;
    public static final int REACTIVE_SERVICE_IS_NOT_EXIST = -9;
    public static final int REACTIVE_SERVICE_IS_NOT_SUPPORTED = 0;
    public static final int REACTIVE_SERVICE_OPERATION_FAILED = -6;
    public static final int REACTIVE_SERVICE_RETURN_FLAG_IS_NOT_EXIST = -3;
    public static final int REACTIVE_SERVICE_RETURN_NATIVE_ERROR = -1;
    public static final int REACTIVE_SERVICE_RETURN_NO_ERROR = 0;
    public static final int REACTIVE_SERVICE_RETURN_PERMISSION_DENIED = -5;
    public static final int REACTIVE_SERVICE_RETURN_STRING_IS_NOT_EXIST = -4;
    public static final int REACTIVE_SERVICE_RETURN_UNSUPPORTED_OPERATION = -2;
    private static final int RS_GOOGLE_NWD_SUPPORTED = 4;
    private static final int RS_IS_NOT_SUPPORTED = 0;
    private static final int RS_SAMSUNG_NWD_SUPPORTED = 2;
    private static final int RS_SAMSUNG_SWD_SUPPORTED = 1;
    public static final int SAMSUNG_GOOGLE_REACTIVE_SERVICES_ARE_SUPPORTED = 3;
    public static final int SAMSUNG_REACTIVE_SERVICE_IS_SUPPORTED = 1;
    public static final int SERVICE_FLAG = 1;
    private static final String TAG = "ReactiveServiceManager";
    private static final int USE_SAMSUNG_ACCOUNT = 0;
    private static final int USE_VERIFICATION_TOKEN = 1;
    private final Context mContext;
    private IReactiveService mService = Stub.asInterface(ServiceManager.getService("ReactiveService"));

    public ReactiveServiceManager(Context context) {
        this.mContext = context;
    }

    public boolean isConnected() {
        if (this.mService != null) {
            return true;
        }
        return false;
    }

    public int getServiceSupport() {
        try {
            int supported = this.mService.getServiceSupport();
            Log.i(TAG, "Supported : " + supported);
            switch (supported) {
                case 1:
                case 2:
                    return 1;
                case 4:
                    return 2;
                case 5:
                case 6:
                    return 3;
                default:
                    return 0;
            }
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getRawServiceValueForAtCommand() {
        try {
            return this.mService.getServiceSupport();
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    private String toHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (byte b : bytes) {
            String hexNumber = SmartFaceManager.PAGE_MIDDLE + Integer.toHexString(b & 255);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    public int getFlag(int flag) {
        try {
            int ret = this.mService.getFlag(flag);
            if (ret <= 2 && ret >= 0) {
                return ret;
            }
            Log.e(TAG, "getFlag() : error code[" + ret + "]");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int setFlag(int flag, int value, String info) {
        Log.e(TAG, "setFlag() - No longer used API");
        return -2;
    }

    public String getString() {
        String str = null;
        try {
            str = this.mService.getString();
        } catch (NullPointerException e) {
            Log.e(TAG, "getString() : Service is not exist.");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return str;
    }

    public int setString(String string) {
        if (string == null) {
            return -8;
        }
        try {
            int ret = this.mService.setString(string);
            if (ret < 0) {
                Log.e(TAG, "setString() : error code[" + ret + "]");
                return ret;
            }
            Log.i(TAG, "setString() : " + ret + " characters are saved.");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int removeString() {
        try {
            int ret = this.mService.removeString();
            if (ret < 0) {
                Log.e(TAG, "removeString() : error code[" + ret + "]");
                return ret;
            }
            Log.i(TAG, "removeString Success ");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public byte[] sessionAccept(byte[] input) {
        byte[] bArr = null;
        if (input == null) {
            Log.e(TAG, "SessionAccept() : Invalid argument");
        } else {
            try {
                bArr = this.mService.sessionAccept(input);
            } catch (NullPointerException e) {
                Log.e(TAG, "SessionAccpet() : Service is not exist.");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bArr;
    }

    public int sessionComplete(byte[] input) {
        if (input == null) {
            return -8;
        }
        try {
            int ret = this.mService.sessionComplete(input);
            if (ret == 0) {
                return ret;
            }
            Log.e(TAG, "sessionComplete() : error code[" + ret + "]");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getErrorCode() {
        try {
            return this.mService.getErrorCode();
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getStatus() {
        if (isConnected()) {
            int ret;
            int which = getServiceSupport();
            if (which == 1) {
                ret = getFlag(0);
            } else if (which != 2) {
                return -3;
            } else {
                ret = getFlag(2);
            }
            if (ret == 2) {
                return 1;
            }
            return ret;
        }
        Log.e(TAG, "ReactiveService is not exist.");
        return -9;
    }

    public int enable(byte[] rc) {
        if (rc == null || rc.length != 32) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int ret = this.mService.setFlag(0, 1, toHex(rc));
            if (ret == 0) {
                return ret;
            }
            Log.e(TAG, "enable() : error code[" + ret + "]");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int disable(byte[] vt) {
        if (vt == null || vt.length != 32) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int ret = this.mService.setFlag(0, 0, toHex(vt));
            if (ret == 0) {
                return ret;
            }
            Log.e(TAG, "disable() : error code[" + ret + "]");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int disableWithAccountId(String id) {
        if (id == null) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int ret = this.mService.setFlag(0, 3, id);
            if (ret == 0) {
                return ret;
            }
            Log.e(TAG, "disableWithAccountId() : error code[" + ret + "]");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int verify(byte[] vt) {
        if (vt == null || vt.length != 32) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int ret = this.mService.verify(toHex(vt), 1);
            if (ret < 0) {
                Log.e(TAG, "verify() : error code[" + ret + "]");
                return ret;
            }
            Log.i(TAG, "Verification success");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int verifyWithAccountId(String id) {
        if (id == null) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int ret = this.mService.verify(id, 0);
            if (ret < 0) {
                Log.e(TAG, "verifyWithAccountId() : error code[" + ret + "]");
                return ret;
            }
            Log.i(TAG, "Verification with id, success");
            return ret;
        } catch (NullPointerException e) {
            return -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public byte[] getRandom() {
        byte[] buffer = null;
        if (getServiceSupport() != 1) {
            Log.e(TAG, "Invalid operation.");
            return null;
        }
        int ret;
        try {
            buffer = this.mService.getRandom();
            if (buffer == null) {
                ret = -6;
            } else {
                ret = 0;
            }
        } catch (NullPointerException e) {
            ret = -9;
        } catch (Exception e2) {
            e2.printStackTrace();
            ret = -10;
        }
        if (ret < 0) {
            Log.e(TAG, "getRandom() : error code[" + ret + "]");
        } else {
            Log.i(TAG, "Success of generate random numbers.");
        }
        return buffer;
    }
}
