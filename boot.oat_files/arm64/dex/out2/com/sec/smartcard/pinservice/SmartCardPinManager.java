package com.sec.smartcard.pinservice;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.sec.smartcard.pinservice.ISmartCardPinService.Stub;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SmartCardPinManager {
    private static final String BIND_PIN_SERVICE = "com.sec.smartcard.pinservice.action.BIND_SMART_CARD_PIN_SERVICE";
    private static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String LOCKSCREEN_TYPE_OTHER = "Other";
    public static final String LOCKSCREEN_TYPE_SMARTCARD = "Smartcard";
    private static final String PROVIDER_NAME = "com.sec.smartcard.manager/smartcards";
    private static final String SAMSUNG_SC_PKG_PREFIX = "com.sec.enterprise.mdm.sc.";
    private static final String[] SMARTCARD_PROJECTION = new String[]{"CardCUID"};
    private static final String TAG = "SmartCardPinManager";
    private static final String URL = "content://com.sec.smartcard.manager/smartcards";
    public static final int VERIFY_PIN_CARDASSOCIATEERROR = 8;
    public static final int VERIFY_PIN_CARDDISCONNECT = 6;
    public static final int VERIFY_PIN_CARDERROR = 5;
    public static final int VERIFY_PIN_CARDEXPIRED = 3;
    public static final int VERIFY_PIN_CARDLOCKED = 2;
    public static final int VERIFY_PIN_CONNECTIONERROR = 4;
    public static final int VERIFY_PIN_FAIL = 1;
    public static final int VERIFY_PIN_SUCCESS = 0;
    public static final int VERIFY_PIN_USERCANCEL = 7;
    private static BlockingQueue queue = null;
    private Context mContext = null;
    private boolean mIsCallbackCalled;
    private char[] mPin;
    private boolean mServiceConnectionProgress = false;
    private ISmartCardPinService mSmartCardPin;
    private ServiceConnection pinServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName arg0) {
            SmartCardPinManager.this.mSmartCardPin = null;
            Log.i(SmartCardPinManager.TAG, "onServiceDisconnected");
            SmartCardPinManager.this.mServiceConnectionProgress = false;
        }

        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            SmartCardPinManager.this.mSmartCardPin = Stub.asInterface(arg1);
            Log.i(SmartCardPinManager.TAG, "onServiceConnected");
            SmartCardPinManager.this.mServiceConnectionProgress = false;
            if (SmartCardPinManager.queue != null) {
                try {
                    Log.i(SmartCardPinManager.TAG, "calling queue.put");
                    SmartCardPinManager.queue.put("1");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    public static abstract class PinCallback {
        public ISmartCardGetPinCallback mICallback = new ISmartCardGetPinCallback.Stub() {
            public void onUserEnteredPin(char[] pin) throws RemoteException {
                PinCallback.this.onUserEnteredPin(pin);
            }

            public void onUserCancelled() throws RemoteException {
                PinCallback.this.onUserCancelled();
            }

            public void onUserPinError(int error) throws RemoteException {
                PinCallback.this.onUserPinError(error);
            }
        };

        public abstract void onUserCancelled();

        public abstract void onUserEnteredPin(char[] cArr);

        public abstract void onUserPinError(int i);
    }

    public SmartCardPinManager(IBinder smartCardPin) {
        this.mSmartCardPin = Stub.asInterface(smartCardPin);
    }

    public SmartCardPinManager(Context context, UserHandle currentUser) {
        Log.i(TAG, TAG);
        this.mContext = context;
        this.mSmartCardPin = null;
        queue = null;
        bindSmartCardPinService(currentUser);
    }

    public SmartCardPinManager(UserHandle currentUser, Context context, int flag) {
        Log.i(TAG, "SmartCardPinManager Sync");
        this.mContext = context;
        this.mSmartCardPin = null;
        if (flag == 0) {
            queue = null;
            bindSmartCardPinService(currentUser);
            return;
        }
        queue = new ArrayBlockingQueue(1);
    }

    public void getPin(PinCallback callback) {
        try {
            this.mSmartCardPin.getPin(callback.mICallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized char[] getPinSync() {
        final ConditionVariable cv = new ConditionVariable();
        this.mPin = null;
        this.mIsCallbackCalled = false;
        getPin(new PinCallback() {
            public void onUserEnteredPin(char[] pin) {
                SmartCardPinManager.this.mPin = pin;
                SmartCardPinManager.this.mIsCallbackCalled = true;
                cv.open();
            }

            public void onUserCancelled() {
                SmartCardPinManager.this.mPin = null;
                SmartCardPinManager.this.mIsCallbackCalled = true;
                cv.open();
            }

            public void onUserPinError(int error) {
                SmartCardPinManager.this.mPin = null;
                SmartCardPinManager.this.mIsCallbackCalled = true;
                cv.open();
            }
        });
        if (!this.mIsCallbackCalled) {
            cv.block();
        }
        return this.mPin;
    }

    public void registerCard(char[] pin, ISmartCardRegisterCallback cb) throws Exception {
        Log.i(TAG, "registerCard");
        if (this.mSmartCardPin != null) {
            try {
                this.mSmartCardPin.registerCard(pin, cb);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        Log.i(TAG, "unable to connect to smartcard pin service");
        throw new Exception("unable to connect to smartcard pin service");
    }

    public void verifyCard(char[] pin, ISmartCardVerifyCallback cb) throws Exception {
        Log.i(TAG, "verifyCard");
        if (this.mSmartCardPin != null) {
            try {
                this.mSmartCardPin.verifyCard(pin, cb);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        Log.i(TAG, "unable to connect to smartcard pin service");
        throw new Exception("unable to connect to smartcard pin service");
    }

    public void unRegisterCard(char[] pin, ISmartCardRegisterCallback cb) throws Exception {
        if (this.mSmartCardPin != null) {
            try {
                this.mSmartCardPin.unRegisterCard(pin, cb);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        Log.i(TAG, "unable to connect to smartcard pin service");
        throw new Exception("unable to connect to smartcard pin service");
    }

    public static boolean isCardRegistered(Context ctx) {
        ContentResolver cr = ctx.getContentResolver();
        Log.i(TAG, "context : " + ctx);
        Log.i(TAG, "content resolver : " + cr);
        Cursor cursor = cr.query(CONTENT_URI, SMARTCARD_PROJECTION, null, null, null);
        Log.i(TAG, "cursor : " + cursor);
        Log.i(TAG, "cursor.count : " + cursor.getCount());
        if (cursor == null || cursor.getCount() <= 0) {
            return false;
        }
        return true;
    }

    public boolean isCardRegistered() {
        boolean ret = false;
        if (this.mSmartCardPin != null) {
            try {
                ret = this.mSmartCardPin.isCardRegistered();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "unable to connect to smartcard pin service");
        }
        return ret;
    }

    public void showCardNotRegisteredDialog() {
        if (this.mSmartCardPin != null) {
            try {
                this.mSmartCardPin.showCardNotRegisteredDialog();
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        Log.i(TAG, "unable to connect to smartcard pin service");
    }

    public boolean isDeviceConnectedWithCard() {
        return true;
    }

    public boolean isSmartCardAuthenticationAvailable() {
        boolean ret = false;
        if (this.mSmartCardPin != null) {
            try {
                ret = this.mSmartCardPin.isSmartCardAuthenticationAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "unable to connect to smartcard pin service");
        }
        return ret;
    }

    public static boolean isSmartCardAuthenticationInstalled() {
        return false;
    }

    public static boolean isSmartCardAuthenticationInstalled(Context context) {
        boolean ret = false;
        if (context == null) {
            Log.d(TAG, "context is null returning");
            return 0;
        }
        for (PackageInfo pi : context.getPackageManager().getInstalledPackages(0)) {
            if (context.getPackageManager().checkPermission("com.sec.smartcard.permission.SMARTCARD_ADAPTER", pi.packageName) == 0) {
                Log.i(TAG, "isSmartCardAuthenticationInstalled: True");
                ret = true;
                break;
            }
        }
        return ret;
    }

    public void getCardLoginAttemptRemain(ISmartCardInfoCallback cb) throws Exception {
        if (this.mSmartCardPin != null) {
            try {
                this.mSmartCardPin.getCardLoginAttemptRemain(cb);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        Log.e(TAG, "unable to connect to smartcard pin service");
        throw new Exception("unable to connect to smartcard pin service");
    }

    private void bindSmartCardPinService(UserHandle currentUser) {
        Log.i(TAG, "bindSmartCardPinService()");
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "mSmartCardPin is null");
            if (this.mServiceConnectionProgress) {
                Log.i(TAG, "binding to service is progress. new request to bind is ignored");
                return;
            }
            Intent intent = new Intent(BIND_PIN_SERVICE);
            intent.setComponent(new ComponentName("com.sec.smartcard.manager", "com.sec.smartcard.pinservice.SmartCardService"));
            Log.i(TAG, "binding to smartcard pin service");
            if (currentUser != null) {
                Log.i(TAG, "binding to smartcard pin service for a user handle: " + currentUser.getIdentifier());
                this.mContext.bindServiceAsUser(intent, this.pinServiceConnection, 1, currentUser);
            } else {
                this.mContext.bindService(intent, this.pinServiceConnection, 1);
            }
            this.mServiceConnectionProgress = true;
        }
    }

    public void bindSmartCardPinService_Sync(UserHandle currentUser) {
        Log.i(TAG, "bindSmartCardPinService_Sync()");
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "mSmartCardPin is null");
            if (this.mServiceConnectionProgress) {
                Log.i(TAG, "binding to service is progress. new request to bind is ignored");
                return;
            }
            Log.i(TAG, "binding to smartcard pin service");
            Intent intent = new Intent(BIND_PIN_SERVICE);
            intent.setComponent(new ComponentName("com.sec.smartcard.manager", "com.sec.smartcard.pinservice.SmartCardService"));
            if (currentUser != null) {
                Log.i(TAG, "binding to smartcard pin service for a user handle: " + currentUser.getIdentifier());
                this.mContext.bindServiceAsUser(intent, this.pinServiceConnection, 1, currentUser);
            } else {
                this.mContext.bindService(intent, this.pinServiceConnection, 1);
            }
            this.mServiceConnectionProgress = true;
            try {
                Log.i(TAG, "calling queue.take");
                queue.take();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void unbindSmartCardPinService() {
        Log.i(TAG, "unbindSmartCardPinService()");
        Log.i(TAG, "unbinding to smartcard pin service ");
        if (this.mSmartCardPin != null) {
            Log.i(TAG, "mSmartCardPin is not null");
            this.mContext.unbindService(this.pinServiceConnection);
        }
    }
}
