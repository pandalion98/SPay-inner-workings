package com.samsung.android.spaytzsvc.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.spay.IPaymentManager;
import android.spay.IPaymentManager.Stub;
import android.spay.ITAController;
import android.spay.PaymentTZServiceCommnInfo;
import android.spay.PaymentTZServiceConfig;
import com.samsung.android.spayfw.p002b.Log;
import java.util.ArrayList;
import java.util.List;

public class PaymentTZServiceIF {
    private static final String TAG = "PaymentTZServiceIF";
    private static final int error_version = -1;
    private static PaymentTZServiceIF mInstance;
    boolean bInitialized;
    private IPaymentServiceIFCallback mCallback;
    private PaymentTZServiceConfig mConfig;
    ServiceConnection mConnection;
    private Context mContext;
    boolean mIsBound;
    private IPaymentManager mPaymentTZService;
    private PaymentTZServiceCommnInfo mTZServiceCommnInfo;
    List<IPaymentSvcDeathReceiver> paymentSvcDeathReceivers;

    /* renamed from: com.samsung.android.spaytzsvc.api.PaymentTZServiceIF.1 */
    class C05991 implements ServiceConnection {
        C05991() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PaymentTZServiceIF.this.mIsBound = true;
            PaymentTZServiceIF.this.mPaymentTZService = Stub.asInterface(iBinder);
            try {
                if (PaymentTZServiceIF.this.mPaymentTZService == null) {
                    Log.m286e(PaymentTZServiceIF.TAG, "onServiceConnected: Unable to find payment TZ Service service!");
                    return;
                }
                PaymentTZServiceIF.this.mTZServiceCommnInfo = PaymentTZServiceIF.this.mPaymentTZService.registerSPayFW(PaymentTZServiceIF.this.mConfig);
                PaymentTZServiceIF.this.mCallback.onInitialized();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            PaymentTZServiceIF.this.mPaymentTZService = null;
            PaymentTZServiceIF.this.mTZServiceCommnInfo = null;
        }
    }

    private PaymentTZServiceIF() {
        this.mContext = null;
        this.mPaymentTZService = null;
        this.mTZServiceCommnInfo = null;
        this.paymentSvcDeathReceivers = new ArrayList();
        this.bInitialized = false;
        this.mIsBound = false;
        this.mConnection = new C05991();
        mInstance = this;
    }

    public static synchronized PaymentTZServiceIF getInstance() {
        PaymentTZServiceIF paymentTZServiceIF;
        synchronized (PaymentTZServiceIF.class) {
            if (mInstance == null) {
                mInstance = new PaymentTZServiceIF();
            }
            paymentTZServiceIF = mInstance;
        }
        return paymentTZServiceIF;
    }

    public boolean init(List<TAController> list) {
        if (this.bInitialized) {
            Log.m285d(TAG, "PaymentTZServiceIF already Initialized");
        } else {
            Log.m285d(TAG, "init: called");
            this.mConfig = new PaymentTZServiceConfig();
            for (TAController tAInfo : list) {
                TAInfo tAInfo2 = tAInfo.getTAInfo();
                if (tAInfo2 == null) {
                    Log.m286e(TAG, "Error: Not able to create TA - taInfo null");
                } else {
                    this.mConfig.addTAConfig(tAInfo2.getTAType(), tAInfo2.getTAConfig());
                }
            }
            startSPaySvc();
            for (TAController tAInfo3 : list) {
                if (!tAInfo3.init()) {
                    Log.m286e(TAG, "TA Initialization failed for TA " + tAInfo3.getTAInfo().toString());
                }
            }
            this.bInitialized = true;
        }
        return true;
    }

    public int getVersion() {
        if (this.mTZServiceCommnInfo == null) {
            return error_version;
        }
        return this.mTZServiceCommnInfo.mServiceVersion;
    }

    public ITAController getTAController(int i) {
        if (this.mTZServiceCommnInfo != null) {
            return ITAController.Stub.asInterface((IBinder) this.mTZServiceCommnInfo.mTAs.get(Integer.valueOf(i)));
        }
        Log.m285d(TAG, "getTAController: mTZServiceCommnInfo=null .. Something went wrong in registerSPayFW API");
        startSPaySvc();
        return null;
    }

    public void registerForDisconnection(IPaymentSvcDeathReceiver iPaymentSvcDeathReceiver) {
        this.paymentSvcDeathReceivers.add(iPaymentSvcDeathReceiver);
    }

    public void unRegisterForDisconnection(IPaymentSvcDeathReceiver iPaymentSvcDeathReceiver) {
        this.paymentSvcDeathReceivers.remove(iPaymentSvcDeathReceiver);
    }

    private void startSPaySvc() {
        try {
            Class cls = Class.forName("android.os.ServiceManager");
            this.mPaymentTZService = Stub.asInterface((IBinder) cls.getMethod("getService", new Class[]{String.class}).invoke(cls, new Object[]{"mobile_payment"}));
            if (this.mPaymentTZService == null) {
                Log.m286e(TAG, "startSPaySvc: Unable to find payment TZ Service service!");
                return;
            }
            this.mTZServiceCommnInfo = this.mPaymentTZService.registerSPayFW(this.mConfig);
            if (this.mTZServiceCommnInfo != null) {
                Log.m285d(TAG, "startSPaySvc: binder list = " + this.mTZServiceCommnInfo.mTAs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PaymentTZServiceIF(Context context) {
        this.mContext = null;
        this.mPaymentTZService = null;
        this.mTZServiceCommnInfo = null;
        this.paymentSvcDeathReceivers = new ArrayList();
        this.bInitialized = false;
        this.mIsBound = false;
        this.mConnection = new C05991();
        this.mContext = context;
        mInstance = this;
    }

    public static synchronized PaymentTZServiceIF getInstanceForTesting(Context context) {
        PaymentTZServiceIF paymentTZServiceIF;
        synchronized (PaymentTZServiceIF.class) {
            if (mInstance == null) {
                mInstance = new PaymentTZServiceIF(context);
            }
            paymentTZServiceIF = mInstance;
        }
        return paymentTZServiceIF;
    }

    public void initForTesting(List<TAController> list, IPaymentServiceIFCallback iPaymentServiceIFCallback) {
        this.mCallback = iPaymentServiceIFCallback;
        this.mConfig = new PaymentTZServiceConfig();
        for (TAController tAInfo : list) {
            TAInfo tAInfo2 = tAInfo.getTAInfo();
            this.mConfig.addTAConfig(tAInfo2.getTAType(), tAInfo2.getTAConfig());
        }
        startSPayTestSvc();
    }

    private void startSPayTestSvc() {
        Intent intent = new Intent();
        intent.setClassName("com.android.server.spay", "com.android.server.spay.PaymentTZTestService");
        if (!this.mContext.bindService(intent, this.mConnection, 1)) {
            Log.m286e(TAG, "Error Binding to com.android.server.spay");
        }
    }

    private void stopSPayTestSvc() {
        this.mContext.unbindService(this.mConnection);
    }

    public byte[] getMeasurementFile() {
        try {
            return this.mPaymentTZService.getMeasurementFile();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
}
