/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.spay.IPaymentManager
 *  android.spay.IPaymentManager$Stub
 *  android.spay.ITAController
 *  android.spay.ITAController$Stub
 *  android.spay.PaymentTZServiceCommnInfo
 *  android.spay.PaymentTZServiceConfig
 *  android.spay.PaymentTZServiceConfig$TAConfig
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spaytzsvc.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.spay.IPaymentManager;
import android.spay.ITAController;
import android.spay.PaymentTZServiceCommnInfo;
import android.spay.PaymentTZServiceConfig;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spaytzsvc.api.IPaymentServiceIFCallback;
import com.samsung.android.spaytzsvc.api.IPaymentSvcDeathReceiver;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PaymentTZServiceIF {
    private static final String TAG = "PaymentTZServiceIF";
    private static final int error_version = -1;
    private static PaymentTZServiceIF mInstance;
    boolean bInitialized = false;
    private IPaymentServiceIFCallback mCallback;
    private PaymentTZServiceConfig mConfig;
    ServiceConnection mConnection = new ServiceConnection(){

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PaymentTZServiceIF.this.mIsBound = true;
            PaymentTZServiceIF.this.mPaymentTZService = IPaymentManager.Stub.asInterface((IBinder)iBinder);
            try {
                if (PaymentTZServiceIF.this.mPaymentTZService == null) {
                    c.e(PaymentTZServiceIF.TAG, "onServiceConnected: Unable to find payment TZ Service service!");
                    return;
                }
                PaymentTZServiceIF.this.mTZServiceCommnInfo = PaymentTZServiceIF.this.mPaymentTZService.registerSPayFW(PaymentTZServiceIF.this.mConfig);
                PaymentTZServiceIF.this.mCallback.onInitialized();
                return;
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                return;
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            PaymentTZServiceIF.this.mPaymentTZService = null;
            PaymentTZServiceIF.this.mTZServiceCommnInfo = null;
        }
    };
    private Context mContext = null;
    boolean mIsBound = false;
    private IPaymentManager mPaymentTZService = null;
    private PaymentTZServiceCommnInfo mTZServiceCommnInfo = null;
    List<IPaymentSvcDeathReceiver> paymentSvcDeathReceivers = new ArrayList();

    private PaymentTZServiceIF() {
        mInstance = this;
    }

    private PaymentTZServiceIF(Context context) {
        this.mContext = context;
        mInstance = this;
    }

    public static PaymentTZServiceIF getInstance() {
        Class<PaymentTZServiceIF> class_ = PaymentTZServiceIF.class;
        synchronized (PaymentTZServiceIF.class) {
            if (mInstance == null) {
                mInstance = new PaymentTZServiceIF();
            }
            PaymentTZServiceIF paymentTZServiceIF = mInstance;
            // ** MonitorExit[var2] (shouldn't be in output)
            return paymentTZServiceIF;
        }
    }

    public static PaymentTZServiceIF getInstanceForTesting(Context context) {
        Class<PaymentTZServiceIF> class_ = PaymentTZServiceIF.class;
        synchronized (PaymentTZServiceIF.class) {
            if (mInstance == null) {
                mInstance = new PaymentTZServiceIF(context);
            }
            PaymentTZServiceIF paymentTZServiceIF = mInstance;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return paymentTZServiceIF;
        }
    }

    private void startSPaySvc() {
        try {
            Class class_ = Class.forName((String)"android.os.ServiceManager");
            this.mPaymentTZService = IPaymentManager.Stub.asInterface((IBinder)((IBinder)class_.getMethod("getService", new Class[]{String.class}).invoke((Object)class_, new Object[]{"mobile_payment"})));
            if (this.mPaymentTZService == null) {
                c.e(TAG, "startSPaySvc: Unable to find payment TZ Service service!");
                return;
            }
            this.mTZServiceCommnInfo = this.mPaymentTZService.registerSPayFW(this.mConfig);
            if (this.mTZServiceCommnInfo != null) {
                c.d(TAG, "startSPaySvc: binder list = " + (Object)this.mTZServiceCommnInfo.mTAs);
                return;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void startSPayTestSvc() {
        Intent intent = new Intent();
        intent.setClassName("com.android.server.spay", "com.android.server.spay.PaymentTZTestService");
        if (!this.mContext.bindService(intent, this.mConnection, 1)) {
            c.e(TAG, "Error Binding to com.android.server.spay");
        }
    }

    private void stopSPayTestSvc() {
        this.mContext.unbindService(this.mConnection);
    }

    public byte[] getMeasurementFile() {
        try {
            byte[] arrby = this.mPaymentTZService.getMeasurementFile();
            return arrby;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return null;
        }
    }

    public ITAController getTAController(int n2) {
        if (this.mTZServiceCommnInfo == null) {
            c.d(TAG, "getTAController: mTZServiceCommnInfo=null .. Something went wrong in registerSPayFW API");
            this.startSPaySvc();
            return null;
        }
        return ITAController.Stub.asInterface((IBinder)((IBinder)this.mTZServiceCommnInfo.mTAs.get((Object)n2)));
    }

    public int getVersion() {
        if (this.mTZServiceCommnInfo == null) {
            return -1;
        }
        return this.mTZServiceCommnInfo.mServiceVersion;
    }

    public boolean init(List<TAController> list) {
        if (this.bInitialized) {
            c.d(TAG, "PaymentTZServiceIF already Initialized");
            return true;
        }
        c.d(TAG, "init: called");
        this.mConfig = new PaymentTZServiceConfig();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            TAInfo tAInfo = ((TAController)iterator.next()).getTAInfo();
            if (tAInfo == null) {
                c.e(TAG, "Error: Not able to create TA - taInfo null");
                continue;
            }
            this.mConfig.addTAConfig(tAInfo.getTAType(), tAInfo.getTAConfig());
        }
        this.startSPaySvc();
        for (TAController tAController : list) {
            if (tAController.init()) continue;
            c.e(TAG, "TA Initialization failed for TA " + tAController.getTAInfo().toString());
        }
        this.bInitialized = true;
        return true;
    }

    public void initForTesting(List<TAController> list, IPaymentServiceIFCallback iPaymentServiceIFCallback) {
        this.mCallback = iPaymentServiceIFCallback;
        this.mConfig = new PaymentTZServiceConfig();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            TAInfo tAInfo = ((TAController)iterator.next()).getTAInfo();
            this.mConfig.addTAConfig(tAInfo.getTAType(), tAInfo.getTAConfig());
        }
        this.startSPayTestSvc();
    }

    public void registerForDisconnection(IPaymentSvcDeathReceiver iPaymentSvcDeathReceiver) {
        this.paymentSvcDeathReceivers.add((Object)iPaymentSvcDeathReceiver);
    }

    public void unRegisterForDisconnection(IPaymentSvcDeathReceiver iPaymentSvcDeathReceiver) {
        this.paymentSvcDeathReceivers.remove((Object)iPaymentSvcDeathReceiver);
    }

}

