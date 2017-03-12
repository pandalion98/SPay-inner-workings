package com.samsung.android.spayfw.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.spayfw.core.p005a.PaymentProcessor;
import com.samsung.android.spayfw.p002b.Log;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.crypto.tls.NamedCurve;

/* renamed from: com.samsung.android.spayfw.core.i */
public class PaymentFrameworkHandler extends Handler {
    private boolean jN;
    private List<Message> jO;
    private Context mContext;

    private static boolean m617m(int i) {
        if (8 == i || 45 == i) {
            return true;
        }
        return false;
    }

    PaymentFrameworkHandler(Context context, Looper looper) {
        super(looper);
        this.jN = false;
        this.jO = null;
        this.mContext = context;
    }

    public boolean aK() {
        Log.m285d("PaymentFrameworkHandler", " getPaymentMode:  " + this.jN);
        return this.jN;
    }

    public void m619c(boolean z) {
        Log.m285d("PaymentFrameworkHandler", " setPaymentMode:  " + z);
        this.jN = z;
        if (!z) {
            aM();
        }
    }

    public synchronized void m618a(Message message) {
        Log.m285d("PaymentFrameworkHandler", " sendMessage: PaymentMode : " + this.jN);
        if (this.jN) {
            if (this.jO == null) {
                this.jO = new ArrayList();
            }
            if (PaymentFrameworkHandler.m617m(message.what)) {
                Log.m285d("PaymentFrameworkHandler", " PaymentMode allowed operation: " + message.what);
            } else {
                Log.m285d("PaymentFrameworkHandler", " PaymentMode not allowed and put in pending queue: operation: " + message.what);
                this.jO.add(message);
            }
        }
        sendMessage(message);
    }

    public void aL() {
        Log.m285d("PaymentFrameworkHandler", " clearMessageOnAppDead : ");
        synchronized (PaymentFrameworkHandler.class) {
            removeMessages(1);
            removeMessages(2);
            removeMessages(3);
            removeMessages(4);
            removeMessages(5);
            removeMessages(6);
            removeMessages(7);
            removeMessages(8);
            removeMessages(9);
            removeMessages(10);
            removeMessages(14);
            removeMessages(20);
            removeMessages(22);
            removeMessages(24);
            removeMessages(17);
            removeMessages(19);
            removeMessages(25);
            removeMessages(26);
            removeMessages(27);
            removeMessages(28);
            removeMessages(29);
            removeMessages(30);
            removeMessages(31);
            removeMessages(32);
            removeMessages(33);
            removeMessages(34);
            removeMessages(35);
            removeMessages(36);
            removeMessages(37);
            removeMessages(38);
            removeMessages(39);
            removeMessages(40);
            removeMessages(41);
            removeMessages(42);
            removeMessages(43);
            if (!(this.jO == null || this.jO.isEmpty())) {
                for (int i = 0; i < this.jO.size(); i++) {
                    Message message = (Message) this.jO.get(i);
                    switch (message.what) {
                        case CertStatus.UNREVOKED /*11*/:
                        case CertStatus.UNDETERMINED /*12*/:
                        case NamedCurve.sect571k1 /*13*/:
                        case NamedCurve.secp160k1 /*15*/:
                        case NamedCurve.secp224r1 /*21*/:
                        case NamedCurve.secp256r1 /*23*/:
                            Log.m287i("PaymentFrameworkHandler", " clearMessageOnAppDead : not removed msg" + message.what);
                            break;
                        default:
                            Log.m287i("PaymentFrameworkHandler", " clearMessageOnAppDead : remove msg" + message.what);
                            this.jO.remove(i);
                            break;
                    }
                }
                this.jO.clear();
            }
        }
        PaymentProcessor.m470q(this.mContext).clearPay();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleMessage(android.os.Message r11) {
        /*
        r10 = this;
        r2 = 0;
        r0 = r11.obj;
        r0 = (com.samsung.android.spayfw.core.PaymentFrameworkMessage) r0;
        r1 = "PaymentFrameworkHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "handleMessage: operation ";
        r3 = r3.append(r4);
        r4 = r11.what;
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r3);
        r1 = r11.what;	 Catch:{ RemoteException -> 0x003c }
        switch(r1) {
            case 1: goto L_0x0025;
            case 2: goto L_0x004b;
            case 3: goto L_0x0066;
            case 4: goto L_0x007d;
            case 5: goto L_0x0094;
            case 6: goto L_0x00ac;
            case 7: goto L_0x00c0;
            case 8: goto L_0x00d8;
            case 9: goto L_0x00e7;
            case 10: goto L_0x00ff;
            case 11: goto L_0x0139;
            case 12: goto L_0x0024;
            case 13: goto L_0x010a;
            case 14: goto L_0x011d;
            case 15: goto L_0x014a;
            case 16: goto L_0x0024;
            case 17: goto L_0x0024;
            case 18: goto L_0x0024;
            case 19: goto L_0x0024;
            case 20: goto L_0x015e;
            case 21: goto L_0x017b;
            case 22: goto L_0x018a;
            case 23: goto L_0x0172;
            case 24: goto L_0x01a2;
            case 25: goto L_0x01b9;
            case 26: goto L_0x01ca;
            case 27: goto L_0x0215;
            case 28: goto L_0x0228;
            case 29: goto L_0x023b;
            case 30: goto L_0x025e;
            case 31: goto L_0x0275;
            case 32: goto L_0x0288;
            case 33: goto L_0x02a1;
            case 34: goto L_0x02b4;
            case 35: goto L_0x02c7;
            case 36: goto L_0x02da;
            case 37: goto L_0x02ed;
            case 38: goto L_0x0319;
            case 39: goto L_0x032d;
            case 40: goto L_0x0347;
            case 41: goto L_0x035a;
            case 42: goto L_0x036d;
            case 43: goto L_0x038e;
            case 44: goto L_0x03a1;
            case 45: goto L_0x03b8;
            default: goto L_0x0024;
        };	 Catch:{ RemoteException -> 0x003c }
    L_0x0024:
        return;
    L_0x0025:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.EnrollCardInfo) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.BillingInfo) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IEnrollCardCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.d;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x003c:
        r0 = move-exception;
        r1 = "PaymentFrameworkHandler";
        r2 = r0.getMessage();
        r0 = r0.getCause();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x0024;
    L_0x004b:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (java.lang.Boolean) r2;	 Catch:{ RemoteException -> 0x003c }
        r2 = r2.booleanValue();	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.u;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0066:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.ProvisionTokenInfo) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IProvisionTokenCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.y;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x007d:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.IdvMethod) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ISelectIdvCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.j;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0094:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.VerifyIdvInfo) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IVerifyIdvCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.k;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x00ac:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ISelectCardCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r2);	 Catch:{ RemoteException -> 0x003c }
        r3 = 0;
        r2.m420a(r1, r0, r3);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x00c0:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.SecuredObject) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.PayConfig) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IPayCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r3);	 Catch:{ RemoteException -> 0x003c }
        r4 = 0;
        r3.m419a(r1, r2, r0, r4);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x00d8:
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r1 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r1 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r1);	 Catch:{ RemoteException -> 0x003c }
        r1.m418a(r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x00e7:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.PushMessage) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (java.lang.Integer) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IPushMessageCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.p;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x00ff:
        r0 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r0 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r0);	 Catch:{ RemoteException -> 0x003c }
        r0.clearCard();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x010a:
        r1 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.ResetNotifier.m499r(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m502a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x011d:
        r1 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.ICardAttributeCallback) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r2 = (java.lang.String) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r0 = (java.lang.Boolean) r0;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.booleanValue();	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.c;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r2, r0, r1);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0139:
        r0 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x003c }
        r1 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r0 = com.samsung.android.spayfw.core.p005a.TokenReplenisher.m547b(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        if (r0 == 0) goto L_0x0024;
    L_0x0145:
        r0.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x014a:
        if (r0 == 0) goto L_0x0159;
    L_0x014c:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        if (r1 == 0) goto L_0x0159;
    L_0x0150:
        r0 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.core.retry.RetryRequestData) r0;	 Catch:{ RemoteException -> 0x003c }
        com.samsung.android.spayfw.core.retry.RetryRequester.m667a(r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0159:
        com.samsung.android.spayfw.core.retry.RetryRequester.bo();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x015e:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICardDataCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = new com.samsung.android.spayfw.core.a.x;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2.<init>(r3);	 Catch:{ RemoteException -> 0x003c }
        r2.m536a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0172:
        r0 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.core.p005a.TokenChangeChecker) r0;	 Catch:{ RemoteException -> 0x003c }
        r0.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x017b:
        r0 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.storage.models.PaymentDetailsRecord) r0;	 Catch:{ RemoteException -> 0x003c }
        r1 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r1 = com.samsung.android.spayfw.core.p005a.AnalyticsReporter.m358m(r1);	 Catch:{ RemoteException -> 0x003c }
        r1.m359a(r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x018a:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (android.os.Bundle) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IDeleteCardCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.w;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x01a2:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.SecuredObject) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.InAppTransactionInfo) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IInAppPayCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r3);	 Catch:{ RemoteException -> 0x003c }
        r3.startInAppPay(r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x01b9:
        r1 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r2 = (android.os.ParcelFileDescriptor) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x003c }
        com.samsung.android.spayfw.core.PaymentFrameworkApp.m316a(r2, r0, r1);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x01ca:
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r1 = 0;
        r4 = "PROD";
        r3 = -1;
        r5 = r4.hashCode();	 Catch:{ RemoteException -> 0x003c }
        switch(r5) {
            case 67573: goto L_0x01ff;
            case 79491: goto L_0x01eb;
            case 82438: goto L_0x01f5;
            case 2464599: goto L_0x01e2;
            default: goto L_0x01d9;
        };	 Catch:{ RemoteException -> 0x003c }
    L_0x01d9:
        r2 = r3;
    L_0x01da:
        switch(r2) {
            case 0: goto L_0x0209;
            case 1: goto L_0x020c;
            case 2: goto L_0x020f;
            case 3: goto L_0x0212;
            default: goto L_0x01dd;
        };	 Catch:{ RemoteException -> 0x003c }
    L_0x01dd:
        r0.onSuccess(r1);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x01e2:
        r5 = "PROD";
        r4 = r4.equals(r5);	 Catch:{ RemoteException -> 0x003c }
        if (r4 == 0) goto L_0x01d9;
    L_0x01ea:
        goto L_0x01da;
    L_0x01eb:
        r2 = "PRE";
        r2 = r4.equals(r2);	 Catch:{ RemoteException -> 0x003c }
        if (r2 == 0) goto L_0x01d9;
    L_0x01f3:
        r2 = 1;
        goto L_0x01da;
    L_0x01f5:
        r2 = "STG";
        r2 = r4.equals(r2);	 Catch:{ RemoteException -> 0x003c }
        if (r2 == 0) goto L_0x01d9;
    L_0x01fd:
        r2 = 2;
        goto L_0x01da;
    L_0x01ff:
        r2 = "DEV";
        r2 = r4.equals(r2);	 Catch:{ RemoteException -> 0x003c }
        if (r2 == 0) goto L_0x01d9;
    L_0x0207:
        r2 = 3;
        goto L_0x01da;
    L_0x0209:
        r1 = "PRODUCTION";
        goto L_0x01dd;
    L_0x020c:
        r1 = "PRE-PRODUCTION";
        goto L_0x01dd;
    L_0x020f:
        r1 = "STAGING";
        goto L_0x01dd;
    L_0x0212:
        r1 = "DEVELOPMENT";
        goto L_0x01dd;
    L_0x0215:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.GiftCardProcessor.m402n(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m405a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0228:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.GiftCardProcessor.m402n(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m407b(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x023b:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (byte[]) r1;	 Catch:{ RemoteException -> 0x003c }
        r1 = (byte[]) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (byte[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r2 = (byte[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r3 = r0.jQ;	 Catch:{ RemoteException -> 0x003c }
        r3 = (com.samsung.android.spayfw.appinterface.SecuredObject) r3;	 Catch:{ RemoteException -> 0x003c }
        r4 = r0.jR;	 Catch:{ RemoteException -> 0x003c }
        r4 = (com.samsung.android.spayfw.appinterface.PayConfig) r4;	 Catch:{ RemoteException -> 0x003c }
        r5 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r5 = (com.samsung.android.spayfw.appinterface.IPayCallback) r5;	 Catch:{ RemoteException -> 0x003c }
        r0 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r0 = com.samsung.android.spayfw.core.p005a.GiftCardProcessor.m402n(r0);	 Catch:{ RemoteException -> 0x003c }
        r0.m406a(r1, r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x025e:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.appinterface.SecuredObject) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3 = com.samsung.android.spayfw.core.p005a.GiftCardProcessor.m402n(r3);	 Catch:{ RemoteException -> 0x003c }
        r3.m404a(r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0275:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IUserSignatureCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.SignatureProcessor.m510v(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m511a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0288:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (byte[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r2 = (byte[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3 = com.samsung.android.spayfw.core.p005a.SignatureProcessor.m510v(r3);	 Catch:{ RemoteException -> 0x003c }
        r3.m512a(r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x02a1:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.ServerRequest) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IServerResponseCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.ServerRequestProcessor.m507u(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m509a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x02b4:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.LoyaltyCardProcessor.m436p(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m439a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x02c7:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.LoyaltyCardProcessor.m436p(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m437a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x02da:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IPayCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.LoyaltyCardProcessor.m436p(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m438a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x02ed:
        r3 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r3 = (java.lang.String) r3;	 Catch:{ RemoteException -> 0x003c }
        r1 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.Long) r1;	 Catch:{ RemoteException -> 0x003c }
        r4 = r1.longValue();	 Catch:{ RemoteException -> 0x003c }
        r1 = r0.jQ;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.Long) r1;	 Catch:{ RemoteException -> 0x003c }
        r6 = r1.longValue();	 Catch:{ RemoteException -> 0x003c }
        r1 = r0.jR;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.Integer) r1;	 Catch:{ RemoteException -> 0x003c }
        r8 = r1.intValue();	 Catch:{ RemoteException -> 0x003c }
        r9 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r9 = (com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback) r9;	 Catch:{ RemoteException -> 0x003c }
        r1 = new com.samsung.android.spayfw.core.a.aa;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r1.<init>(r2, r3, r4, r6, r8, r9);	 Catch:{ RemoteException -> 0x003c }
        r1.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0319:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IRefreshIdvCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = new com.samsung.android.spayfw.core.a.i;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2.<init>(r3, r1, r0);	 Catch:{ RemoteException -> 0x003c }
        r2.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x032d:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (char[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r2 = (char[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = new com.samsung.android.spayfw.core.a.e;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3.<init>(r4, r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        r3.process();	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x0347:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.GlobalMembershipCardProcessor.m411o(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m412a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x035a:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.GlobalMembershipCardProcessor.m411o(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m416b(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x036d:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (byte[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r2 = (byte[]) r2;	 Catch:{ RemoteException -> 0x003c }
        r3 = r0.jQ;	 Catch:{ RemoteException -> 0x003c }
        r3 = (com.samsung.android.spayfw.appinterface.SecuredObject) r3;	 Catch:{ RemoteException -> 0x003c }
        r4 = r0.jR;	 Catch:{ RemoteException -> 0x003c }
        r4 = (com.samsung.android.spayfw.appinterface.PayConfig) r4;	 Catch:{ RemoteException -> 0x003c }
        r5 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r5 = (com.samsung.android.spayfw.appinterface.IPayCallback) r5;	 Catch:{ RemoteException -> 0x003c }
        r0 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r0 = com.samsung.android.spayfw.core.p005a.GlobalMembershipCardProcessor.m411o(r0);	 Catch:{ RemoteException -> 0x003c }
        r0.m413a(r1, r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x038e:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.util.List) r1;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r2 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r2 = com.samsung.android.spayfw.core.p005a.GlobalMembershipCardProcessor.m411o(r2);	 Catch:{ RemoteException -> 0x003c }
        r2.m414a(r1, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x03a1:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (java.lang.String) r2;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r3 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r3 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r3);	 Catch:{ RemoteException -> 0x003c }
        r3.m421a(r1, r2, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
    L_0x03b8:
        r1 = r0.obj;	 Catch:{ RemoteException -> 0x003c }
        r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x003c }
        r2 = r0.jP;	 Catch:{ RemoteException -> 0x003c }
        r2 = (com.samsung.android.spayfw.payprovider.MerchantServerRequester.MerchantInfo) r2;	 Catch:{ RemoteException -> 0x003c }
        r3 = r0.jQ;	 Catch:{ RemoteException -> 0x003c }
        r3 = (java.lang.String) r3;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.jS;	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.samsung.android.spayfw.appinterface.IInAppPayCallback) r0;	 Catch:{ RemoteException -> 0x003c }
        r4 = r10.mContext;	 Catch:{ RemoteException -> 0x003c }
        r4 = com.samsung.android.spayfw.core.p005a.PaymentProcessor.m470q(r4);	 Catch:{ RemoteException -> 0x003c }
        r4.getInAppToken(r1, r2, r3, r0);	 Catch:{ RemoteException -> 0x003c }
        goto L_0x0024;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.i.handleMessage(android.os.Message):void");
    }

    private synchronized void aM() {
        if (!(this.jO == null || this.jO.isEmpty())) {
            Log.m285d("PaymentFrameworkHandler", " clearMessageOnNonPaymentMode : ");
            for (int i = 0; i < this.jO.size(); i++) {
                Log.m285d("PaymentFrameworkHandler", " clearMessageOnNonPaymentMode : post msg" + ((Message) this.jO.get(i)).what);
                sendMessage((Message) this.jO.get(i));
            }
            this.jO.clear();
        }
    }
}
