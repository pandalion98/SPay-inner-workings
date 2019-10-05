/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.CountDownTimer
 *  android.os.Message
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.Arrays
 *  java.util.Objects
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.core.f;
import com.samsung.android.spayfw.core.h;
import com.samsung.android.spayfw.core.i;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.core.o;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spaytzsvc.api.TAException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class n
extends com.samsung.android.spayfw.core.a.o
implements com.samsung.android.spayfw.core.a.h {
    private static boolean lA = false;
    private static n lB;
    private static CountDownTimer lC;
    static IPayCallback lz;
    private Timer lD = null;
    private Timer lE = null;
    private String lF;
    private Bundle lG = null;
    private Timer lH;
    private IPayCallback lI;
    private boolean lJ = false;
    private PayConfig lK = null;
    PaymentDetailsRecord lf;
    long startTime;

    private n(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int a(com.samsung.android.spayfw.core.c c2, PayConfig payConfig) {
        int n2;
        if (payConfig == null || payConfig.getPayType() == 2 && payConfig.getMstTransmitTime() == 0) {
            int n3 = c2.ad().getPayConfigTransmitTime(false);
            if (!com.samsung.android.spayfw.utils.h.ao(this.mContext) || !c2.k(2)) {
                n3 = 30;
            }
            n2 = n3 * 1000;
        } else {
            n2 = payConfig.getPayType() == 1 && payConfig.getMstTransmitTime() == 0 ? 30000 : payConfig.getMstTransmitTime();
        }
        Log.d("PaymentProcessor", "mstTransmitTime : " + n2);
        return n2;
    }

    /*
     * Exception decompiling
     */
    private ApduReasonCode a(int var1_1, com.samsung.android.spayfw.core.c var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        // java.util.ArrayList.get(ArrayList.java:437)
        // org.benf.cfr.reader.b.a.a.b.ak.a(PointlessJumps.java:107)
        // org.benf.cfr.reader.b.a.a.b.ai.m(Op03Rewriters.java:113)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:457)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private InAppTransactionInfo a(InAppTransactionInfo inAppTransactionInfo) {
        InAppTransactionInfo inAppTransactionInfo2 = new InAppTransactionInfo();
        inAppTransactionInfo2.setContextId(inAppTransactionInfo.getContextId());
        inAppTransactionInfo2.setAmount(inAppTransactionInfo.getAmount());
        inAppTransactionInfo2.setCurrencyCode(inAppTransactionInfo.getCurrencyCode());
        inAppTransactionInfo2.setPid(inAppTransactionInfo.getPid());
        inAppTransactionInfo2.setMerchantRefId(inAppTransactionInfo.getMerchantRefId());
        inAppTransactionInfo2.setRecurring(inAppTransactionInfo.isRecurring());
        inAppTransactionInfo2.setFPANLast4Digits(inAppTransactionInfo.getFPANLast4Digits());
        return inAppTransactionInfo2;
    }

    public static final short a(byte[] arrby, short s2) {
        return (short)(((short)arrby[s2] << 8) + (255 & (short)arrby[(short)(s2 + 1)]));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void a(int n2, long l2) {
        if (this.lE == null) {
            Log.d("PaymentProcessor", "mNFCTimer is null. Scheduling NFCTimerTask. Timestamp =  " + System.currentTimeMillis() + " reason: " + n2);
        } else {
            Log.d("PaymentProcessor", "mNFCTimer is not null. Cancel and reschedule. Timestamp = " + System.currentTimeMillis() + " reason: " + n2);
            this.lE.cancel();
        }
        a a2 = new a(n2);
        if (l2 > 0L) {
            this.lE = new Timer();
            this.lE.schedule((TimerTask)a2, l2);
            return;
        }
        a2.run();
    }

    static /* synthetic */ Timer b(n n2, Timer timer) {
        n2.lH = timer;
        return timer;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void b(com.samsung.android.spayfw.core.c c2, final int n2) {
        if (c2 == null || c2.ad() == null) {
            Log.e("PaymentProcessor", "start Nfc timer, card is null ");
            return;
        }
        if (this.lD != null) {
            Log.e("PaymentProcessor", "NFC session timer already started ");
            return;
        }
        int n3 = this.a(c2, this.lK);
        Log.d("PaymentProcessor", "startNFCSessionTimer mstTransmitTime " + n3);
        Log.d("PaymentProcessor", "startTime " + this.startTime);
        long l2 = (long)n3 - (System.currentTimeMillis() - this.startTime);
        Log.d("PaymentProcessor", "period " + l2);
        if (l2 <= 0L) return;
        TimerTask timerTask = new TimerTask(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void run() {
                Log.d("PaymentProcessor", "nfc usage timer expired");
                try {
                    n.this.u(n2);
                }
                catch (RemoteException remoteException) {
                    Log.c("PaymentProcessor", remoteException.getMessage(), remoteException);
                }
                n.this.lD = null;
            }
        };
        if (this.lD == null) {
            Log.d("PaymentProcessor", "NFC session timer starts");
        } else {
            Log.d("PaymentProcessor", "NFC session timer restarts");
            this.lD.cancel();
        }
        this.lD = new Timer();
        this.lD.schedule(timerTask, l2);
    }

    private void bb() {
        if (this.iJ == null) {
            this.iJ = com.samsung.android.spayfw.core.a.a(this.mContext, null);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private Bundle d(Bundle bundle) {
        Bundle bundle2;
        block8 : {
            block7 : {
                bundle2 = null;
                if (bundle == null) break block7;
                boolean bl = bundle.containsKey("tapNGotransactionErrorCode");
                bundle2 = null;
                if (bl) {
                    int n2 = bundle.getInt("tapNGotransactionErrorCode");
                    Log.d("PaymentProcessor", "bundle transactionErrorCode = " + n2);
                    bundle2 = null;
                    if (!false) {
                        bundle2 = new Bundle();
                    }
                    bundle2.putInt("status", n2);
                    bundle2.putString("transactionType", "tapNGoState");
                }
                if (bundle.containsKey("pdolValues")) {
                    Bundle bundle3 = bundle.getBundle("pdolValues");
                    Log.d("PaymentProcessor", "bundle pdolValues = " + bundle3.toString());
                    if (bundle2 == null) {
                        bundle2 = new Bundle();
                    }
                    bundle2.putBundle("pdolValues", bundle3);
                }
                if (bundle2 != null) break block8;
            }
            return bundle2;
        }
        Log.d("PaymentProcessor", "extraData = " + bundle2.toString());
        return bundle2;
    }

    static /* synthetic */ Timer d(n n2) {
        return n2.lH;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void d(PaymentDetailsRecord paymentDetailsRecord) {
        if (paymentDetailsRecord == null) {
            Log.w("PaymentProcessor", "mPaymentDetails is null, returning!");
            return;
        }
        i i2 = PaymentFrameworkApp.az();
        if (paymentDetailsRecord.getInAppTransactionInfo() == null) {
            paymentDetailsRecord.setElapsedTime(System.currentTimeMillis() - this.startTime);
        }
        if (i2 != null) {
            Log.d("PaymentProcessor", "Post PAYFW_OPT_ANALYTICS_REPORT request");
            i2.sendMessage(j.a(21, paymentDetailsRecord, null));
        } else {
            Log.e("PaymentProcessor", "HANDLER IS NOT INITIAILIZED");
        }
        com.samsung.android.spayfw.core.retry.e.w(this.mContext).add(paymentDetailsRecord.getTrTokenId());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final com.samsung.android.spayfw.core.a.h q(Context context) {
        Class<n> class_ = n.class;
        synchronized (n.class) {
            void var2_4;
            block7 : {
                com.samsung.android.spayfw.core.a.h h2;
                if (lB == null) {
                    lB = new n(context);
                }
                if (!e.i(context)) break block7;
                com.samsung.android.spayfw.core.a.h h3 = h2 = t.a(context, lB);
                do {
                    return var2_4;
                    break;
                } while (true);
            }
            n n2 = lB;
            return var2_4;
        }
    }

    @Override
    public com.samsung.android.spayfw.core.c X() {
        this.bb();
        if (this.iJ != null) {
            return this.iJ.X();
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void a(ICommonCallback iCommonCallback) {
        n n2 = this;
        synchronized (n2) {
            Log.d("PaymentProcessor", "stopPay()");
            this.bb();
            if (iCommonCallback == null) {
                Log.e("PaymentProcessor", "stopPay- invalid inputs: callback is null");
            } else {
                if (this.lH != null) {
                    Log.d("PaymentProcessor", "Cancel Retry Timer");
                    this.lH.cancel();
                    this.lH = null;
                }
                if (this.lD != null) {
                    Log.d("PaymentProcessor", "Cancel mNFCSessionTimer Timer");
                    this.lD.cancel();
                    this.lD = null;
                }
                if (this.lE != null) {
                    this.lE.cancel();
                    this.lE = null;
                }
                if (lC != null) {
                    Log.d("PaymentProcessor", "Cancel CountDown Timer");
                    lC.cancel();
                    lC = null;
                }
                if (o.p(1)) {
                    Log.e("PaymentProcessor", "Nothing to Stop");
                    if (this.lI != null) {
                        this.lI.onFail(this.lF, -7);
                        this.lI = null;
                    }
                    iCommonCallback.onSuccess(this.lF);
                } else if (!o.r(24320) || !o.r(4096)) {
                    Log.e("PaymentProcessor", "state cannot be changed");
                    iCommonCallback.onFail(this.lF, -4);
                } else if (this.lF == null) {
                    iCommonCallback.onFail(this.lF, -4);
                    Log.e("PaymentProcessor", "stopPay- mSelectCardTokenId is null");
                } else if (this.iJ == null) {
                    iCommonCallback.onFail(this.lF, -4);
                    Log.e("PaymentProcessor", "stopPay- mAccount is null");
                } else {
                    com.samsung.android.spayfw.core.c c2 = this.iJ.r(this.lF);
                    if (c2 == null) {
                        iCommonCallback.onFail(this.lF, -6);
                        Log.e("PaymentProcessor", "stopPay- unable to get card Object");
                        this.lF = null;
                    } else {
                        String string = this.lF;
                        if (o.p(128)) {
                            c2.ad().stopInAppPay(this.lF, iCommonCallback);
                        } else {
                            if (!this.lJ) {
                                if (this.lf != null && o.p(72)) {
                                    this.lf.setMstCancelled("CANCELLED");
                                }
                            } else if (this.lf != null && o.p(72)) {
                                this.lf.setMstRetryCancelled("CANCELLED");
                            }
                            c2.ad().stopPay();
                            if (o.p(512)) {
                                Log.d("PaymentProcessor", "NFC_USER_CANCELLED");
                                this.u(-98);
                            }
                            iCommonCallback.onSuccess(string);
                        }
                    }
                }
            }
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void a(SecuredObject var1_1, PayConfig var2_2, IPayCallback var3_3, boolean var4_4) {
        block36 : {
            var17_5 = this;
            // MONITORENTER : var17_5
            Log.d("PaymentProcessor", "startPay()");
            Log.d("PaymentProcessor", "startPay(): authAlive = " + var4_4);
            this.bb();
            this.startTime = System.currentTimeMillis();
            Log.i("PaymentProcessor", "Start time = " + this.startTime);
            if (this.lF != null && (var4_4 || var1_1 != null) && var3_3 != null && (var6_6 = this.iJ) != null) break block36;
            if (var3_3 == null) ** GOTO lbl13
            try {
                block37 : {
                    var3_3.onFail(this.lF, -5);
                    break block37;
lbl13: // 1 sources:
                    Log.e("PaymentProcessor", "pay callback is null");
                }
                if (this.lF == null) {
                    Log.e("PaymentProcessor", "startPay- invalid inputs: mSelectCardTokenId is null");
                    return;
                }
                if (var1_1 == null) {
                    Log.e("PaymentProcessor", "startPay- invalid inputs: secObj is null");
                    return;
                }
                if (this.iJ == null) {
                    Log.e("PaymentProcessor", "startPay- invalid inputs: Account is null");
                    return;
                }
                Log.e("PaymentProcessor", "startPay- invalid inputs");
                return;
            }
            finally {
                this.clearCard();
            }
        }
        var7_8 = this.iJ.r(this.lF);
        if (var7_8 == null) {
            try {
                var3_3.onFail(this.lF, -6);
                Log.e("PaymentProcessor", "startPay- unable to get card Object");
                return;
            }
            finally {
                this.clearCard();
                return;
            }
        }
        if (var7_8.ac() != null) {
            Log.d("PaymentProcessor", "Token Status = " + var7_8.ac().getTokenStatus());
            var13_10 = Objects.equals((Object)var7_8.ac().getTokenStatus(), (Object)"ACTIVE");
            if (!var13_10) {
                try {
                    var3_3.onFail(this.lF, -4);
                    Log.e("PaymentProcessor", "startPay- Token Status is not Active");
                    return;
                }
                finally {
                    this.clearCard();
                    return;
                }
            }
        }
        PaymentFrameworkApp.az().c(true);
        if (!Objects.equals((Object)this.lF, (Object)"GIFT")) {
            Log.d("PaymentProcessor", "Not Gift Card");
            try {
                f.j(this.mContext).B(this.lF);
            }
            catch (Exception var11_13) {
                Log.c("PaymentProcessor", var11_13.getMessage(), var11_13);
            }
        }
        this.lK = var2_2;
        if (var2_2 == null) {
            Log.d("PaymentProcessor", "startPay: using default payConfig from PF");
            var8_12 = h.b("default", var7_8.getCardBrand());
        } else if (var2_2.getPayType() == 2 && var2_2.getMstPayConfig() == null) {
            Log.d("PaymentProcessor", "startPay: type MST but mstPayConfig is NULL, using default payConfig from PF");
            var8_12 = h.b("default", var7_8.getCardBrand());
        } else {
            Log.d("PaymentProcessor", "startPay: using payConfig from APP");
            var8_12 = var2_2;
        }
        this.lf = new PaymentDetailsRecord();
        if (var4_4 && this.lf != null) {
            this.lf.setBarcodeAttempted("ATTEMPTED");
        }
        this.lI = var3_3;
        Log.d("PaymentProcessor", "Card presentation mode " + var7_8.ab());
        if (!com.samsung.android.spayfw.utils.h.ao(this.mContext) || !var7_8.k(2)) {
            var8_12 = new PayConfig();
            var8_12.setPayType(1);
            Log.v("PaymentProcessor", "MST is not supported");
        }
        var7_8.ad().startPay(var8_12, var1_1, new b(var3_3), var4_4);
        com.samsung.android.spayfw.core.b.Z();
        if (var8_12.getPayType() == 1) {
            Log.v("PaymentProcessor", "NFC only Payment");
            return;
        }
        if (n.lC != null) {
            Log.d("PaymentProcessor", "Cancel CountDown Timer");
            n.lC.cancel();
            n.lC = null;
        }
        if (n.lC != null) {
            // MONITOREXIT : var17_5
            return;
        }
        var9_14 = this.a(var7_8, this.lK);
        Log.d("PaymentProcessor", "StartPayCountDownTimer : " + var9_14);
        n.lC = new d(var9_14, 5000L);
        n.lC.start();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void a(String string, ISelectCardCallback iSelectCardCallback, boolean bl) {
        int n2 = -1;
        n n3 = this;
        synchronized (n3) {
            Log.d("PaymentProcessor", "selectCard() - tokenId = " + string);
            if (!o.r(2)) {
                Log.e("PaymentProcessor", "selectCard  - cannot change state");
                if (iSelectCardCallback != null) {
                    iSelectCardCallback.onFail(string, -4);
                } else {
                    Log.e("PaymentProcessor", "selectCard callback is null");
                }
            } else {
                this.bb();
                if (string == null || iSelectCardCallback == null || this.iJ == null) {
                    if (this.iJ == null) {
                        Log.e("PaymentProcessor", "selectCard  - Failed to initialize account");
                    } else if (string == null) {
                        Log.e("PaymentProcessor", "selectCard- invalid inputs: tokenId is null");
                        n2 = -5;
                    } else {
                        Log.e("PaymentProcessor", "selectCard- invalid inputs: callback object is null");
                        n2 = -5;
                    }
                    if (iSelectCardCallback != null) {
                        iSelectCardCallback.onFail(string, n2);
                    } else {
                        Log.e("PaymentProcessor", "selectCard callback is null");
                    }
                    this.clearCard();
                } else {
                    com.samsung.android.spayfw.core.c c2;
                    com.samsung.android.spayfw.core.c c3 = this.iJ.r(string);
                    if ((c3 == null || c3.ac() == null || c3.ac().aQ() == null) && Objects.equals((Object)string, (Object)"GIFT")) {
                        Log.e("PaymentProcessor", "Gift Card record in DB was null. Recreate");
                        c2 = com.samsung.android.spayfw.core.a.f.n(this.mContext).M(null);
                    } else {
                        c2 = c3;
                    }
                    if (c2 == null || c2.ac() == null || c2.ac().aQ() == null) {
                        int n4 = -6;
                        if (c2 == null) {
                            Log.e("PaymentProcessor", "selectCard- unable to get card Object : " + string);
                        } else if (c2.ac() == null) {
                            Log.e("PaymentProcessor", "selectCard- unable to get token: " + string);
                        } else if (c2.ac().aQ() == null) {
                            Log.e("PaymentProcessor", "selectCard- unable to get token providerKey: " + string);
                            n4 = -4;
                        } else if (!"ACTIVE".equals((Object)c2.ac().getTokenStatus())) {
                            Log.e("PaymentProcessor", "selectCard- token is not active : " + c2.ac().getTokenStatus());
                            n4 = -4;
                        }
                        iSelectCardCallback.onFail(string, n4);
                        this.clearCard();
                    } else {
                        SelectCardResult selectCardResult;
                        int n5;
                        Log.d("PaymentProcessor", "selectCard- selectCard() - call Base PayProvider :" + string);
                        try {
                            SelectCardResult selectCardResult2;
                            selectCardResult = selectCardResult2 = c2.ad().getSecureObjectInputForPayment(bl);
                        }
                        catch (TAException tAException) {
                            Log.c("PaymentProcessor", tAException.getMessage(), (Throwable)((Object)tAException));
                            if (tAException.getErrorCode() == 2) {
                                iSelectCardCallback.onFail(string, -42);
                                selectCardResult = null;
                            }
                            iSelectCardCallback.onFail(string, -41);
                            selectCardResult = null;
                        }
                        this.lF = string;
                        if (selectCardResult == null || selectCardResult.getStatus() != 0 && (n5 = selectCardResult.getStatus()) != 1) {
                            try {
                                iSelectCardCallback.onFail(string, -1);
                                Log.e("PaymentProcessor", "getSecureObjectInputForPayment-unable to get proper response");
                            }
                            finally {
                                this.clearCard();
                            }
                        } else {
                            int n6 = c2.ad().getPayConfigTransmitTime(false);
                            Log.d("PaymentProcessor", "mstTransmitTime : " + n6);
                            selectCardResult.setMstTransmitTime(n6);
                            if (!com.samsung.android.spayfw.utils.h.ao(this.mContext) || !c2.k(2)) {
                                selectCardResult.setMstTransmitTime(30);
                            }
                            o.q(2);
                            iSelectCardCallback.onSuccess(string, selectCardResult);
                        }
                    }
                }
            }
            return;
        }
    }

    @Override
    public void a(String string, String string2, ICommonCallback iCommonCallback) {
        Log.d("PaymentProcessor", "updateBinAttribute version [" + string + "], url [" + string2 + "]");
        if (TextUtils.isEmpty((CharSequence)string) || TextUtils.isEmpty((CharSequence)string2) || iCommonCallback == null) {
            if (iCommonCallback != null) {
                iCommonCallback.onFail("", -5);
            }
            return;
        }
        BinAttribute.setServerBinVersion(string);
        iCommonCallback.onSuccess("");
    }

    @Override
    public void clearCard() {
        this.bb();
        Log.d("PaymentProcessor", "clearCard(): mSelectCardTokenId = " + this.lF);
        o.q(1);
        if (this.lF != null && this.iJ != null) {
            com.samsung.android.spayfw.core.c c2 = this.iJ.r(this.lF);
            if (c2 == null) {
                Log.e("PaymentProcessor", "clearCard: card object does not exist");
                this.lF = null;
                return;
            }
            c2.ad().clearSecureObjectInputForPayment();
        }
        this.lF = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void clearPay() {
        n n2 = this;
        // MONITORENTER : n2
        this.bb();
        if (lA) {
            com.samsung.android.spayfw.e.c.ar(this.mContext);
            com.samsung.android.spayfw.e.c.fi();
            lA = false;
        }
        if (this.lD != null) {
            this.lD.cancel();
            this.lD = null;
        }
        if (this.lE != null) {
            this.lE.cancel();
            this.lE = null;
        }
        com.samsung.android.spayfw.core.b.Z();
        PaymentFrameworkApp.az().c(false);
        o.q(1);
        this.lJ = false;
        if (this.lF != null && this.iJ != null) {
            com.samsung.android.spayfw.core.c c2 = this.iJ.r(this.lF);
            if (c2 == null) {
                Log.e("PaymentProcessor", "clearPay: unable to get card Object");
                // MONITOREXIT : n2
                return;
            }
            Log.d("PaymentProcessor", "clearPay(): mSelectCardTokenId = " + this.lF);
            c2.ad().clearPay();
            if (this.lf != null) {
                this.lf.setPaymentType(com.samsung.android.spayfw.core.c.y(c2.getCardBrand()));
            }
        }
        if (this.lf != null) {
            this.lf.setTrTokenId(this.lF);
            this.lf.setRscAttemptRequestId(f.j(this.mContext).getRscAttemptRequestId());
            this.d(this.lf);
        }
        this.lF = null;
        this.lK = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void clearRetryPay() {
        n n2 = this;
        // MONITORENTER : n2
        this.lH = null;
        this.bb();
        if (lA) {
            com.samsung.android.spayfw.e.c.ar(this.mContext);
            com.samsung.android.spayfw.e.c.fi();
            lA = false;
        }
        if (this.lD != null) {
            this.lD.cancel();
            this.lD = null;
        }
        if (this.lE != null) {
            this.lE.cancel();
            this.lE = null;
        }
        if (lC != null) {
            lC.cancel();
            lC = null;
        }
        PaymentFrameworkApp.az().c(false);
        o.q(1);
        this.lJ = false;
        if (this.lF != null && this.iJ != null) {
            com.samsung.android.spayfw.core.c c2 = this.iJ.r(this.lF);
            if (c2 == null) {
                Log.e("PaymentProcessor", "clearRetryPay: unable to get card Object");
                // MONITOREXIT : n2
                return;
            }
            Log.d("PaymentProcessor", "clearRetryPay(): mSelectCardTokenId = " + this.lF);
            c2.ad().clearRetryPay();
            if (this.lf != null) {
                this.lf.setPaymentType(com.samsung.android.spayfw.core.c.y(c2.getCardBrand()));
            }
        }
        if (this.lf != null) {
            this.lf.setTrTokenId(this.lF);
            this.lf.setRscAttemptRequestId(f.j(this.mContext).getRscAttemptRequestId());
            this.d(this.lf);
        }
        this.lF = null;
        this.lK = null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void getInAppToken(String var1_1, MerchantServerRequester.MerchantInfo var2_2, String var3_3, IInAppPayCallback var4_4) {
        block12 : {
            block11 : {
                Log.d("PaymentProcessor", "getInAppToken()");
                if (this.lF != null && var2_2 != null && var4_4 != null && var3_3 != null) break block12;
                if (var4_4 == null) ** GOTO lbl6
                var4_4.onFail(var1_1, -5);
lbl6: // 2 sources:
                if (this.lF == null) {
                    Log.e("PaymentProcessor", "getInAppToken- invalid inputs: mSelectCardTokenId is null");
                    return;
                }
                if (var2_2 != null) break block11;
                Log.e("PaymentProcessor", "getInAppToken- invalid inputs: merchantInfo is null");
                return;
            }
            if (var3_3 == null) {
                Log.e("PaymentProcessor", "getInAppToken- invalid inputs: paymentPayloadJson is null");
                return;
            }
            Log.e("PaymentProcessor", "getInAppToken- invalid inputs: callback is null");
            return;
        }
        var6_6 = this.iJ.r(this.lF);
        if (var6_6 != null) {
            if (var6_6.ad() == null) return;
            var6_6.ad().getInAppToken(var1_1, var2_2, var3_3, var4_4);
            return;
        }
        try {
            var4_4.onFail(var1_1, -6);
            Log.e("PaymentProcessor", "getInAppToken- unable to get card Object");
            return;
        }
        finally {
            this.clearCard();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public byte[] processApdu(byte[] var1_1, Bundle var2_2) {
        var12_3 = this;
        // MONITORENTER : var12_3
        this.bb();
        if ("GIFT".equals((Object)this.lF)) {
            Log.w("PaymentProcessor", "Current selected card is a Gift card, hence ignore processApdu!");
            var4_4 = com.samsung.android.spayfw.core.b.iR;
            return var4_4;
        }
        if (!this.lJ) {
            if (this.lf != null) {
                Log.d("PaymentProcessor", "processApdu - set nfc attempted");
                this.lf.setNfcAttempted("ATTEMPTED");
            }
        } else if (this.lf != null) {
            Log.d("PaymentProcessor", "processApdu - set nfc retry attempted");
            this.lf.setNfcRetryAttempted("ATTEMPTED");
        }
        if (var1_1 == null) {
            Log.e("PaymentProcessor", "command apdu is null");
            return com.samsung.android.spayfw.core.b.iR;
        }
        if (this.lE != null) {
            Log.d("PaymentProcessor", "processApdu called, cancel mNfcTimer");
            this.lE.cancel();
            this.lE = null;
        }
        if (!o.r(32)) {
            if (o.p(24320) || com.samsung.android.spayfw.core.b.g(var1_1)) {
                Log.d("PaymentProcessor", "should stop nfc payment");
                return com.samsung.android.spayfw.core.b.iS;
            }
            Log.d("PaymentProcessor", "cannot make nfc payment, status not satisfied");
            Log.w("PaymentProcessor", "Abort NFC payment, not authenticated yet, notify app");
            var8_5 = new Intent("com.samsung.android.spayfw.action.notification");
            var8_5.putExtra("notiType", "tapNGoState");
            if (this.lF != null) {
                var8_5.putExtra("tokenId", this.lF);
            }
            var8_5.putExtra("status", -35);
            PaymentFrameworkApp.a(var8_5);
            return com.samsung.android.spayfw.core.b.iS;
        }
        var5_6 = n.a(var1_1, (short)0);
        if (var5_6 != 164) ** GOTO lbl47
        if (Arrays.equals((byte[])com.samsung.android.spayfw.core.b.iO, (byte[])var1_1)) {
            if (com.samsung.android.spayfw.core.b.aa() >= 5) {
                Log.e("PaymentProcessor", "processApdu - reach max PPSE count");
                this.a(-99, 1000L);
                return com.samsung.android.spayfw.core.b.iS;
            }
            var6_7 = 165;
        } else {
            if (com.samsung.android.spayfw.core.b.g(var1_1)) {
                return com.samsung.android.spayfw.core.b.iR;
            }
lbl47: // 3 sources:
            var6_7 = var5_6;
        }
        com.samsung.android.spayfw.core.b.b(var6_7);
        if (this.lF == null) {
            Log.e("PaymentProcessor", "processApdu- mSelectCardTokenId is null");
            if (n.lz != null) {
                n.lz.onFail(this.lF, -5);
                return com.samsung.android.spayfw.core.b.iR;
            } else {
                Log.d("PaymentProcessor", "pay callback is null");
            }
            return com.samsung.android.spayfw.core.b.iR;
        }
        var7_8 = this.iJ.r(this.lF);
        if (var7_8 == null) {
            Log.e("PaymentProcessor", "processApdu- unable to get card object");
            if (n.lz == null) return com.samsung.android.spayfw.core.b.iR;
            n.lz.onFail(this.lF, -1);
            return com.samsung.android.spayfw.core.b.iR;
        }
        if (!var7_8.k(1)) {
            Log.e("PaymentProcessor", "this card cannot be used for nfc payment");
            return com.samsung.android.spayfw.core.b.iR;
        }
        var4_4 = var7_8.ad().processApdu(var1_1, var2_2);
        if (var4_4 == null) {
            return com.samsung.android.spayfw.core.b.iR;
        }
        this.b(var7_8, -98);
        Log.d("APDU_LOG", com.samsung.android.spayfw.core.b.a(var6_7, var4_4));
        if (com.samsung.android.spayfw.core.b.hasError()) {
            Log.d("PaymentProcessor", "apdu processing failed run timer");
            this.a(-99, 1000L);
            return var4_4;
        }
        if (com.samsung.android.spayfw.core.b.a(var7_8, com.samsung.android.spayfw.core.b.Y().getCode())) {
            Log.d("PaymentProcessor", "Transaction sequence completed, run timer also");
            this.a(-97, 100L);
            return var4_4;
        }
        // MONITOREXIT : var12_3
        return var4_4;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int retryPay(PayConfig payConfig) {
        n n2 = this;
        synchronized (n2) {
            int n3;
            Log.d("PaymentProcessor", "retryPay()");
            this.lJ = true;
            if (this.lH != null) {
                Log.d("PaymentProcessor", "Cancel Retry Timer");
                this.lH.cancel();
                this.lH = null;
            }
            if (lC != null) {
                Log.d("PaymentProcessor", "Cancel CountDown Timer");
                lC.cancel();
                lC = null;
            }
            this.bb();
            if (this.iJ == null) {
                Log.e("PaymentProcessor", "retryPay- Account is null");
                return -4;
            }
            this.startTime = System.currentTimeMillis();
            Log.i("PaymentProcessor", "Start time = " + this.startTime);
            if (this.lF == null) {
                Log.e("PaymentProcessor", "retryPay- invalid inputs");
                return -44;
            }
            com.samsung.android.spayfw.core.c c2 = this.iJ.r(this.lF);
            if (c2 == null) {
                try {
                    Log.e("PaymentProcessor", "retryPay- unable to get card Object");
                    n3 = -6;
                }
                finally {
                    this.clearCard();
                }
            } else {
                PayConfig payConfig2;
                PaymentFrameworkApp.az().c(true);
                this.lK = payConfig;
                if (payConfig == null) {
                    Log.d("PaymentProcessor", "retryPay: using default retry seq from PF");
                    payConfig2 = h.b("retry1", c2.getCardBrand());
                } else if (payConfig.getPayType() == 2 && payConfig.getMstPayConfig() == null) {
                    PayConfig payConfig3;
                    Log.d("PaymentProcessor", "retryPay: type MST but mstPayConfig is NULL, using default payConfig from PF");
                    payConfig2 = payConfig3 = h.b("retry1", c2.getCardBrand());
                } else {
                    payConfig2 = payConfig;
                }
                Log.d("PaymentProcessor", "Card presentation mode " + c2.ab());
                if (!c2.k(2)) {
                    payConfig2 = new PayConfig();
                    payConfig2.setPayType(1);
                    Log.v("PaymentProcessor", "MST is not supported");
                }
                if (h.aw() == 2 && o.aN()) {
                    c2.ad().forceQuitMst();
                    if (!o.q(1)) {
                        Log.e("PaymentProcessor", "Cannot goto Npay ready");
                    }
                    if (!o.q(2)) {
                        Log.e("PaymentProcessor", "Cannot goto Selected State");
                    }
                }
                n3 = c2.ad().retryPay(payConfig2);
                com.samsung.android.spayfw.core.b.Z();
                PaymentFrameworkApp.az().postAtFrontOfQueue((Runnable)new c(c2, this.lK));
            }
            return n3;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void startInAppPay(SecuredObject var1_1, InAppTransactionInfo var2_2, IInAppPayCallback var3_3) {
        block18 : {
            block17 : {
                Log.d("PaymentProcessor", "startInAppPay()");
                if (this.lF != null && var1_1 != null && var3_3 != null && var2_2 != null) break block18;
                if (var3_3 == null || var2_2 == null) ** GOTO lbl6
                var3_3.onFail(var2_2.getContextId(), -5);
lbl6: // 2 sources:
                if (this.lF == null) {
                    Log.e("PaymentProcessor", "startInAppPay- invalid inputs: mSelectCardTokenId is null");
                    return;
                }
                if (var1_1 != null) break block17;
                Log.e("PaymentProcessor", "startInAppPay- invalid inputs: secObj is null");
                return;
            }
            if (var2_2 == null) {
                Log.e("PaymentProcessor", "startInAppPay- invalid inputs: txnInfo is null");
                return;
            }
            Log.e("PaymentProcessor", "startInAppPay- invalid inputs: callback is null");
            return;
        }
        var5_5 = this.iJ.r(this.lF);
        if (var5_5 == null) {
            try {
                var3_3.onFail(var2_2.getContextId(), -6);
                Log.e("PaymentProcessor", "startInAppPay- unable to get card Object");
                return;
            }
            finally {
                this.clearCard();
            }
        }
        this.lf = new PaymentDetailsRecord();
        if (var5_5.ac() != null) {
            if (this.lf != null) {
                this.lf.setTrTokenId(this.lF);
                var7_7 = this.a(var2_2);
                this.lf.setInAppTransactionInfo(var7_7);
                this.d(this.lf);
            }
            Log.d("PaymentProcessor", "Token Status = " + var5_5.ac().getTokenStatus());
            if (!Objects.equals((Object)var5_5.ac().getTokenStatus(), (Object)"ACTIVE")) {
                try {
                    var3_3.onFail(var2_2.getContextId(), -4);
                    Log.e("PaymentProcessor", "startInAppPay- Token Status is not Active");
                    return;
                }
                finally {
                    this.clearCard();
                }
            }
        }
        var5_5.ad().startInAppPay(var1_1, var2_2, var3_3);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public ApduReasonCode u(int n2) {
        ApduReasonCode apduReasonCode = null;
        n n3 = this;
        synchronized (n3) {
            if (this.lE != null) {
                Log.d("PaymentProcessor", "onDeactivated called. mNFCTimer is not null. Cancel mNFCTimer. Timestamp =  " + System.currentTimeMillis());
                this.lE.cancel();
                this.lE = null;
            }
            this.bb();
            Log.d("PaymentProcessor", "onDeactivated(): " + n2);
            if (o.aO()) {
                if (this.lF == null) {
                    Log.e("PaymentProcessor", "onDeactivated- mSelectCardTokenId is null");
                    return null;
                }
                if (this.iJ == null) {
                    Log.e("PaymentProcessor", "onDeactivated- Account is null");
                    return null;
                }
                com.samsung.android.spayfw.core.c c2 = this.iJ.r(this.lF);
                if (c2 == null) {
                    Log.e("PaymentProcessor", "onDeactivated- unable to get card object");
                    return null;
                }
                if (!c2.k(1)) {
                    Log.e("PaymentProcessor", "NFC is not supported");
                    return null;
                }
                if (com.samsung.android.spayfw.core.b.a(c2, n2) || n2 == -99 || com.samsung.android.spayfw.core.b.hasError() || n2 == -98 || n2 == -96) {
                    if (this.lD == null) return this.a(n2, c2);
                    Log.d("PaymentProcessor", "Cancel timer");
                    this.lD.cancel();
                    this.lD = null;
                    return this.a(n2, c2);
                }
                Log.d("PaymentProcessor", "no need to cancel, continue NFC session ");
                return null;
            }
            Log.d("PaymentProcessor", "onDeactivated -no need to deactivate");
            return apduReasonCode;
        }
    }

    private class a
    extends TimerTask {
        private int reason = -99;

        public a(int n3) {
            this.reason = n3;
        }

        public void run() {
            Log.i("PaymentProcessor", "NFC apdu TimerExpired::invoke onDeactivated. Timestamp = " + System.currentTimeMillis() + ", reason: " + this.reason);
            try {
                n.this.u(this.reason);
                return;
            }
            catch (RemoteException remoteException) {
                Log.c("PaymentProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
    }

    private class b
    implements com.samsung.android.spayfw.payprovider.b {
        public b(IPayCallback iPayCallback) {
            n.lz = iPayCallback;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void a(Object object, int n2, int n3, String string) {
            com.samsung.android.spayfw.core.c c2;
            if (n.lz == null) {
                Log.d("PaymentProcessor", "invoking app callback onPaySwitch, callback is null");
                return;
            }
            try {
                Log.d("PaymentProcessor", "invoking app callback onPaySwitch");
                n.lz.onPaySwitch(n.this.lF, n2, n3);
                if (n.this.lf != null) {
                    Log.d("PaymentProcessor", "onPaySwitch");
                    n.this.lf.setAuthenticationMode(string);
                }
                if (n2 != 1 || n3 != 2) return;
                {
                    if (lC != null) {
                        Log.d("PaymentProcessor", "Cancel CountDown Timer");
                        lC.cancel();
                    }
                    if ((c2 = n.this.iJ.r(n.this.lF)) == null) {
                        Log.e("PaymentProcessor", "onPaySwitch card is null");
                        return;
                    }
                }
                if (n.this.lJ) {
                    PaymentFrameworkApp.az().postAtFrontOfQueue((Runnable)new c(c2, n.this.lK));
                    return;
                }
            }
            catch (RemoteException remoteException) {
                Log.c("PaymentProcessor", remoteException.getMessage(), remoteException);
                return;
            }
            PaymentFrameworkApp.az().postAtFrontOfQueue(new Runnable(){

                public void run() {
                    int n2 = n.this.a(c2, n.this.lK);
                    lC = new d(n2, 5000L);
                    lC.start();
                }
            });
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(Object var1_1, int var2_2, int var3_3, String var4_4, String var5_5) {
            block15 : {
                block16 : {
                    if (n.this.lf == null) break block16;
                    if (var2_2 == -46) ** GOTO lbl16
                    n.this.lf.setMstSequenceId(var5_5);
                    if (!n.a(n.this)) {
                        n.this.lf.setMstAttempted("ATTEMPTED");
                        if (n.this.lf.getAuthenticationMode() == null) {
                            n.this.lf.setAuthenticationMode(var4_4);
                        }
                        n.this.lf.setMstLoopcount(var2_2);
                    } else {
                        n.this.lf.setMstRetryAttempted("ATTEMPTED");
                        n.this.lf.setMstRetryLoopcount(var2_2);
lbl16: // 2 sources:
                        Log.d("PaymentProcessor", "NFC Timeout Expired");
                        n.this.lf.setNfcRetryAttempted("ATTEMPTED");
                    }
                }
                if (var2_2 == var3_3) {
                    n.this.clearRetryPay();
                }
                if (n.lz != null) break block15;
                Log.d("PaymentProcessor", "invoking app callback onPay, callback is null");
                Log.d("PaymentProcessor", "onPay: CurrentLoop = " + var2_2);
                return;
            }
            if (var2_2 != var3_3) {
                Log.i("PaymentProcessor", "invoking app callback onPay");
                n.lz.onPay(n.b(n.this), var2_2, var3_3);
                return;
            }
            if (var2_2 != -46) return;
            try {
                Log.i("PaymentProcessor", "invoking app callback onFinish after NFC timeout");
                n.lz.onFinish(n.b(n.this), 1, null);
                return;
            }
            catch (RemoteException var7_6) {
                Log.c("PaymentProcessor", var7_6.getMessage(), var7_6);
                return;
            }
            catch (Throwable var6_7) {
                throw var6_7;
            }
            finally {
                Log.d("PaymentProcessor", "onPay: CurrentLoop = " + var2_2);
            }
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(Object var1_1, int var2_2, String var3_3) {
            if (n.this.lf != null) {
                Log.e("PaymentProcessor", "onFail");
                if (!n.a(n.this)) {
                    if (!(n.this.lf.getMstAttempted() != null && n.this.lf.getMstAttempted().equals((Object)"ATTEMPTED") || var2_2 == -11)) {
                        n.this.lf.setMstAttempted("FAILED");
                    }
                    n.this.lf.setAuthenticationMode(var3_3);
                } else if (!(n.this.lf.getMstRetryAttempted() != null && n.this.lf.getMstRetryAttempted().equals((Object)"ATTEMPTED") || var2_2 == -11)) {
                    n.this.lf.setMstRetryAttempted("FAILED");
                }
            }
            n.this.clearRetryPay();
            if (n.lz != null) ** GOTO lbl19
            Log.e("PaymentProcessor", "invoking app callback onFail, callback is null");
            return;
            {
                catch (RemoteException var5_4) {}
                Log.c("PaymentProcessor", var5_4.getMessage(), var5_4);
                return;
lbl19: // 1 sources:
                Log.d("PaymentProcessor", "invoking app callback onFail: errorCode: " + var2_2);
                var6_6 = var2_2 == -11 ? -2 : var2_2;
            }
            switch (var6_6) {
                default: {
                    break;
                }
                case -1: {
                    var6_6 = -35;
                    break;
                }
                case -2: {
                    var6_6 = -36;
                    break;
                }
                case -3: {
                    var6_6 = -37;
                }
            }
            n.lz.onFail(n.b(n.this), var6_6);
            n.lz = null;
        }

        @Override
        public void g(Object object) {
            Log.d("PaymentProcessor", "onRetry()");
            if (n.lz == null) {
                Log.d("PaymentProcessor", "Callback is null");
                n.this.clearRetryPay();
            }
        }

    }

    private class c
    implements Runnable {
        private PayConfig js;
        private com.samsung.android.spayfw.core.c lN;

        c(com.samsung.android.spayfw.core.c c2, PayConfig payConfig) {
            this.lN = c2;
            this.js = payConfig;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void run() {
            int n2 = this.js == null || this.js.getMstTransmitTime() == 0 ? 1000 * this.lN.ad().getPayConfigTransmitTime(true) : this.js.getMstTransmitTime();
            Log.d("PaymentProcessor", "Retry mstTransmitTime in ms: " + n2);
            lC = new CountDownTimer(n2, 5000L){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                public void onFinish() {
                    n n2;
                    Log.d("PaymentProcessor", "CDT Retry : onFinish - sMstRetryType : " + h.aw());
                    n n3 = n2 = n.this;
                    synchronized (n3) {
                        IPayCallback iPayCallback = n.lz;
                        if (iPayCallback != null) {
                            try {
                                Log.i("PaymentProcessor", "invoking app callback onFinish");
                                n.lz.onFinish(n.this.lF, 2, null);
                                n.lz = null;
                            }
                            catch (RemoteException remoteException) {
                                remoteException.printStackTrace();
                            }
                        } else {
                            Log.i("PaymentProcessor", "Callback is null");
                        }
                    }
                    n.this.clearRetryPay();
                }

                public void onTick(long l2) {
                    int n2 = (int)l2 / 1000;
                    Log.d("PaymentProcessor", "CDT Retry : remaining time : " + n2);
                }
            };
            lC.start();
        }

    }

    private class d
    extends CountDownTimer {
        d(long l2, long l3) {
            super(l2, l3);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public void onFinish() {
            block10 : {
                Log.d("PaymentProcessor", "CDT : onFinish - sMstRetryType : " + h.aw());
                if (h.aw() != 1) {
                    if (h.aw() != 2) return;
                    n.this.clearRetryPay();
                    return;
                }
                if (n.lz == null) {
                    Log.d("PaymentProcessor", "Callback is null");
                    n.this.clearRetryPay();
                    return;
                }
                var1_1 = h.av();
                try {
                    Log.d("PaymentProcessor", "CDT : invoking app callback onRetry");
                    if (!o.q(1)) {
                        Log.e("PaymentProcessor", "CDT : Cannot goto Npay ready");
                    }
                    if (!o.q(2)) {
                        Log.e("PaymentProcessor", "CDT : Cannot goto Selected State");
                    }
                    if ((var8_2 = n.this.iJ.r(n.b(n.this))) == null || var8_2.ad() == null) {
                        n.lz.onFail(n.b(n.this), -10);
                        n.this.clearRetryPay();
                        return;
                    }
                    var9_7 = var8_2.ad().getPayConfigTransmitTime(true);
                    if (n.c(n.this) != null && (var10_8 = n.c(n.this).getMstTransmitTime()) != 0) {
                        var9_7 = var10_8 / 1000;
                    }
                    if (com.samsung.android.spayfw.utils.h.ao(n.this.mContext) && var8_2.k(2)) ** GOTO lbl30
                    ** GOTO lbl29
                }
                catch (RemoteException var2_3) {
                    block11 : {
                        Log.c("PaymentProcessor", var2_3.getMessage(), var2_3);
                        break block11;
lbl29: // 1 sources:
                        var9_7 = 30;
lbl30: // 2 sources:
                        Log.d("PaymentProcessor", "CDT : Mst Transmit Time = " + var9_7);
                        Log.d("PaymentProcessor", "CDT : Retry Timeout = " + var1_1);
                        if (n.this.lf != null && !n.a(n.this)) {
                            n.this.lf.setMstLoopcount(1 + n.this.lf.getMstLoopcount());
                        }
                        Log.i("PaymentProcessor", "invoking app callback onRetry");
                        var8_2.ad().forceQuitMst();
                        n.lz.onRetry(n.b(n.this), var1_1, var9_7);
                    }
                    var3_4 = new TimerTask(){

                        public void run() {
                            Log.d("PaymentProcessor", "CDT : Retry Timeout Expired");
                            n.this.clearRetryPay();
                        }
                    };
                    var4_5 = new Timer();
                    var5_6 = var1_1 + 5;
                    if (n.d(n.this) != null) break block10;
                    n.b(n.this, var4_5);
                    n.d(n.this).schedule(var3_4, (long)(var5_6 * 1000));
                    return;
                }
            }
            Log.e("PaymentProcessor", "CDT : Retry Timer Running. Restart.");
            n.d(n.this).cancel();
            n.b(n.this, var4_5);
            n.d(n.this).schedule(var3_4, (long)(var5_6 * 1000));
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void onTick(long l2) {
            int n2;
            int n3;
            n n4;
            block14 : {
                block13 : {
                    n3 = (int)l2 / 1000;
                    Log.d("PaymentProcessor", "CDT : remaining time : " + n3);
                    if (n3 > 10 || h.aw() != 2) break block13;
                    if (n.this.iJ != null && n.this.lF != null) break block14;
                    Log.e("PaymentProcessor", "No account or selected token id");
                }
                return;
            }
            com.samsung.android.spayfw.core.c c2 = n.this.iJ.r(n.this.lF);
            if (c2 == null) {
                Log.e("PaymentProcessor", "card is null during timer tick");
                return;
            }
            int n5 = c2.ad().getPayConfigTransmitTime(true);
            if (n.this.lK != null && (n2 = n.this.lK.getMstTransmitTime()) != 0) {
                n5 = n2 / 1000;
            }
            Log.d("PaymentProcessor", "Mst Transmit Time = " + n5);
            Log.d("PaymentProcessor", "Retry Timeout = " + n3);
            if (n.this.lf != null && !n.this.lJ) {
                n.this.lf.setMstLoopcount(1 + n.this.lf.getMstLoopcount());
            }
            n n6 = n4 = n.this;
            synchronized (n6) {
                IPayCallback iPayCallback = n.lz;
                if (iPayCallback != null) {
                    try {
                        Log.i("PaymentProcessor", "invoking app callback onRetry");
                        n.lz.onRetry(n.this.lF, n3, n5);
                    }
                    catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                } else {
                    Log.i("PaymentProcessor", "Callback is null");
                }
                return;
            }
        }

    }

}

