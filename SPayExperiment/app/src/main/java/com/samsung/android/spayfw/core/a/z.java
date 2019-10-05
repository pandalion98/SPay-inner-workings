/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.RemoteException
 *  com.google.gson.JsonObject
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.g;
import com.samsung.android.spayfw.remoteservice.tokenrequester.i;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.utils.h;
import java.util.HashMap;
import java.util.Map;

public class z
extends o
implements Runnable {
    private static final Map<String, z> mD = new HashMap();
    private static Handler mHandler;
    protected IPushMessageCallback lS;
    protected String lU;
    protected String mTokenId;

    static {
        HandlerThread handlerThread = new HandlerThread("PaymentFramework");
        Log.d("TokenReplenisher", "TokenReplenisher Thread is Started");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    private z(Context context, String string) {
        super(context);
        this.mTokenId = string;
    }

    private z(Context context, String string, IPushMessageCallback iPushMessageCallback, String string2) {
        super(context);
        this.mTokenId = string;
        this.lS = iPushMessageCallback;
        this.lU = string2;
    }

    static /* synthetic */ void a(z z2, String string, c c2) {
        z2.g(string, c2);
    }

    static /* synthetic */ boolean a(z z2, c c2) {
        return z2.b(c2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static z b(Context context, String string) {
        Class<z> class_ = z.class;
        synchronized (z.class) {
            v v2 = v.S(string);
            if (v2 != null && (long)v2.mr < 10L) {
                Log.w("TokenReplenisher", "Token Change Checker Pending. Do not start replenish request.");
                return null;
            }
            if (v2 != null) {
                Log.w("TokenReplenisher", "Removing Token Change Checker");
                v.remove(string);
            } else {
                Log.w("TokenReplenisher", "No Token Change Checker");
            }
            if ((z)mD.get((Object)string) != null) {
                Log.i("TokenReplenisher", "Token Replenisher Pending. Do not start replenish request.");
                return null;
            }
            Log.i("TokenReplenisher", "New Instance of Token Replenisher");
            z z2 = new z(context, string);
            mD.put((Object)string, (Object)z2);
            // ** MonitorExit[var6_2] (shouldn't be in output)
            return z2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static z b(Context context, String string, IPushMessageCallback iPushMessageCallback, String string2) {
        Class<z> class_ = z.class;
        synchronized (z.class) {
            v v2 = v.S(string);
            if (v2 != null && (long)v2.mr < 10L) {
                Log.w("TokenReplenisher", "Token Change Checker Pending. Do not start replenish request.");
                return null;
            }
            Log.w("TokenReplenisher", "Removing Token Change Checker");
            v.remove(string);
            z z2 = (z)mD.get((Object)string);
            if (z2 == null) {
                Log.i("TokenReplenisher", "New Instance of Token Replenisher");
                z2 = new z(context, string, iPushMessageCallback, string2);
                mD.put((Object)string, (Object)z2);
            } else {
                Log.i("TokenReplenisher", "Update Instance of Token Replenisher");
                z2.lU = string2;
                z2.lS = iPushMessageCallback;
            }
            // ** MonitorExit[var8_4] (shouldn't be in output)
            return z2;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean b(c c2) {
        Log.i("TokenReplenisher", "startReplenishRequest");
        f f2 = c2.ac().aQ();
        com.samsung.android.spayfw.payprovider.c c3 = c2.ad().getReplenishmentRequestDataTA();
        if (c3 != null && c3.getErrorCode() == 0) {
            ReplenishTokenRequestData replenishTokenRequestData = new ReplenishTokenRequestData(this.mTokenId);
            if (c3.ch() != null) {
                replenishTokenRequestData.setData(c3.ch());
                Log.d("TokenReplenisher", "replenishRequestData:" + c3.ch().toString());
            } else {
                Log.w("TokenReplenisher", "replenishRequestData:replenishRequstData is null ");
            }
            i i2 = this.lQ.a(c.y(c2.getCardBrand()), c2.ac().getTokenId(), replenishTokenRequestData);
            i2.setCardBrand(c.y(c2.getCardBrand()));
            i2.bf(this.P(c2.getCardBrand()));
            i2.b(new a(this.mTokenId, c2, f2));
            return true;
        }
        Log.e("TokenReplenisher", " unable to get replenish data from pay provider");
        return false;
    }

    private void g(String string, c c2) {
        Log.d("TokenReplenisher", "deleting token id = " + string);
        if (this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string) < 1) {
            Log.e("TokenReplenisher", "Not able to delete Token from DB");
        }
        this.iJ.s(c2.getEnrollmentId());
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static void remove(String string) {
        Class<z> class_ = z.class;
        synchronized (z.class) {
            Log.i("TokenReplenisher", "Remove Instance of Token Replenisher");
            z z2 = (z)mD.remove((Object)string);
            z.getHandler().removeCallbacks((Runnable)z2);
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return;
        }
    }

    public String getTokenId() {
        return this.mTokenId;
    }

    public void process() {
        mHandler.post((Runnable)this);
    }

    public void run() {
        Log.i("TokenReplenisher", "Entered replenish Request");
        Log.d("TokenReplenisher", "Entered replenish Request: tokenId " + this.mTokenId);
        final c c2 = this.iJ.r(this.mTokenId);
        if (c2 == null) {
            Log.e("TokenReplenisher", " unable to get card based on tokenId. ignore replenish request");
            return;
        }
        g g2 = this.lQ.x(c.y(c2.getCardBrand()), this.mTokenId);
        g2.h(false);
        g2.bk(this.iJ.u(c2.getCardBrand()));
        g2.b(new Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, g>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> var2_2) {
                block41 : {
                    block40 : {
                        Log.i("TokenReplenisher", "onRequestComplete: Token change: code " + var1_1);
                        if (c2 == null) {
                            Log.e("TokenReplenisher", "Abort Card is Null");
                            return;
                        }
                        var3_3 = null;
                        var4_4 = z.this.jJ.bq(z.this.mTokenId);
                        if (var4_4 == null) {
                            Log.e("TokenReplenisher", "Abort Record is Null");
                            return;
                        }
                        var5_5 = c2.ac().getTokenStatus();
                        var6_6 = false;
                        switch (var1_1) {
                            default: {
                                if (var1_1 < 500) break;
                                com.samsung.android.spayfw.core.retry.c.b(z.this);
                                break block40;
                            }
                            case 200: {
                                var18_12 = var2_2.getResult();
                                if (var18_12 != null && var18_12.getStatus() != null) ** GOTO lbl28
                                Log.e("TokenReplenisher", "TokenResponseData or status is null");
                                var6_6 = true;
                                var8_9 = var5_5;
                                var9_10 = false;
                                var3_3 = null;
                                var10_7 = null;
                                var7_8 = 0;
                                break block41;
lbl28: // 1 sources:
                                var19_13 = new TokenStatus(m.a(var18_12), var18_12.getStatus().getReason());
                                if (var19_13.getCode().equals((Object)var5_5)) ** GOTO lbl-1000
                                Log.i("TokenReplenisher", "Token Status Changed Before Replenishment");
                                var3_3 = c2.ad().updateTokenStatusTA(var18_12.getData(), var18_12.getStatus());
                                if (var3_3 == null || var3_3.getErrorCode() != 0) {
                                    Log.e("TokenReplenisher", "updateTokenStatus failed on provider side");
                                    var21_14 = false;
                                } else {
                                    var21_14 = true;
                                }
                                if (var21_14) {
                                    var4_4.setTokenStatus(var19_13.getCode());
                                    var4_4.H(var19_13.getReason());
                                    z.this.jJ.d(var4_4);
                                    c2.ac().setTokenStatus(var4_4.getTokenStatus());
                                    c2.ac().H(var4_4.fy());
                                    var5_5 = m.F(var4_4.getTokenStatus());
                                    var23_15 = com.samsung.android.spayfw.fraud.a.x(z.this.mContext);
                                    if (var23_15 != null) {
                                        var23_15.k(var4_4.getTrTokenId(), var4_4.getTokenStatus());
                                    } else {
                                        Log.d("TokenReplenisher", "FraudCollector: updateFTokenRecordStatus cannot get data");
                                    }
                                }
                                if (var5_5.equals((Object)"SUSPENDED")) {
                                    Log.e("TokenReplenisher", "TOKEN SUSPENDED");
                                    var1_1 = -4;
                                    z.remove(z.this.mTokenId);
                                    var10_7 = var19_13;
                                    var7_8 = -4;
                                    var8_9 = var5_5;
                                    var6_6 = false;
                                    var9_10 = false;
                                } else lbl-1000: // 2 sources:
                                {
                                    Log.i("TokenReplenisher", "TOKEN ACTIVE ... Proceed with replenishment.");
                                    if (var18_12.getData() == null || !c2.ad().isReplenishDataAvailable(var18_12.getData())) {
                                        if (z.a(z.this, c2) != false) return;
                                        z.remove(z.this.mTokenId);
                                        return;
                                    }
                                    var3_3 = c2.ad().replenishTokenTA(var18_12.getData(), var18_12.getStatus());
                                    if (var3_3 != null && var3_3.getErrorCode() == 0 && var19_13 != null) {
                                        var4_4.setTokenStatus(var19_13.getCode());
                                        var4_4.H(var19_13.getReason());
                                        c2.ac().setTokenStatus(var19_13.getCode());
                                        c2.ac().H(var19_13.getReason());
                                        z.this.jJ.d(var4_4);
                                        var8_9 = var4_4.getTokenStatus();
                                        var10_7 = var19_13;
                                        var9_10 = true;
                                        var6_6 = true;
                                        var7_8 = 0;
                                    } else if (h.ak(z.this.mContext)) {
                                        com.samsung.android.spayfw.core.retry.c.b(z.this);
                                        var6_6 = true;
                                        var10_7 = var19_13;
                                        var8_9 = var5_5;
                                        var9_10 = false;
                                        var7_8 = 0;
                                    } else {
                                        com.samsung.android.spayfw.core.retry.c.a(z.this);
                                        var6_6 = true;
                                        var10_7 = var19_13;
                                        var8_9 = var5_5;
                                        var9_10 = false;
                                        var7_8 = 0;
                                    }
                                }
                                break block41;
                            }
                            case 404: 
                            case 410: {
                                Log.w("TokenReplenisher", "unable to find the token on server. something wrong. deleting the token");
                                var17_16 = new TokenStatus("DISPOSED", null);
                                var3_3 = c2.ad().updateTokenStatusTA(null, var17_16);
                                z.a(z.this, z.this.mTokenId, c2);
                                z.remove(z.this.mTokenId);
                                var10_7 = var17_16;
                                var7_8 = -6;
                                var8_9 = "DISPOSED";
                                var6_6 = false;
                                var9_10 = false;
                                break block41;
                            }
                            case 500: {
                                com.samsung.android.spayfw.core.retry.c.b(z.this);
                                var7_8 = -205;
                                var8_9 = var5_5;
                                var6_6 = false;
                                var9_10 = false;
                                var3_3 = null;
                                var10_7 = null;
                                break block41;
                            }
                            case 503: {
                                com.samsung.android.spayfw.core.retry.c.b(z.this);
                                var7_8 = -201;
                                var8_9 = var5_5;
                                var6_6 = false;
                                var9_10 = false;
                                var3_3 = null;
                                var10_7 = null;
                                break block41;
                            }
                            case 0: {
                                if (h.ak(z.this.mContext)) {
                                    com.samsung.android.spayfw.core.retry.c.b(z.this);
                                } else {
                                    com.samsung.android.spayfw.core.retry.c.a(z.this);
                                }
                                var7_8 = -201;
                                var8_9 = var5_5;
                                var6_6 = false;
                                var9_10 = false;
                                var3_3 = null;
                                var10_7 = null;
                                break block41;
                            }
                            case -2: {
                                var7_8 = -206;
                                var8_9 = var5_5;
                                var6_6 = false;
                                var9_10 = false;
                                var3_3 = null;
                                var10_7 = null;
                                break block41;
                            }
                        }
                        z.remove(z.this.mTokenId);
                    }
                    var10_7 = null;
                    var7_8 = -1;
                    var8_9 = var5_5;
                    var9_10 = false;
                }
                if (var7_8 != 0) {
                    Log.e("TokenReplenisher", "Replenish Token Failed - Error Code = " + var7_8);
                    var16_11 = new d(23, -1, c2.ac().aQ());
                    c2.ad().updateRequestStatus(var16_11);
                } else {
                    var11_17 = new d(23, 0, c2.ac().aQ());
                    c2.ad().updateRequestStatus(var11_17);
                }
                if (var6_6) {
                    if (var9_10) {
                        z.this.a(null, z.this.mTokenId, var8_9, "TOKEN_CHANGE", c.y(c2.getCardBrand()), var3_3, true);
                    } else {
                        Log.e("TokenReplenisher", "processTokenChange:Send error report to TR server");
                        z.this.b(null, z.this.mTokenId, var8_9, "TOKEN_CHANGE", c.y(c2.getCardBrand()), var3_3, true);
                    }
                }
                if (z.this.lS != null) {
                    if (var7_8 == -6 || var7_8 == -4) {
                        z.this.lS.onTokenStatusUpdate(z.this.lU, z.this.mTokenId, var10_7);
                    }
                    if (var7_8 != 0) {
                        z.this.lS.onFail(z.this.lU, var7_8);
                    }
                    try {
                        z.this.lS.onTokenReplenishRequested(z.this.lU, z.this.mTokenId);
                    }
                    catch (RemoteException var15_18) {
                        Log.c("TokenReplenisher", var15_18.getMessage(), var15_18);
                    }
                    z.this.lS = null;
                    return;
                }
                if (var1_1 != -6 && var1_1 != -2) {
                    if (var1_1 != -4) return;
                }
                var12_19 = new Intent("com.samsung.android.spayfw.action.notification");
                if (var1_1 == -6 || var1_1 == -4) {
                    var12_19.putExtra("notiType", "syncAllCards");
                } else if (var1_1 == -2) {
                    var12_19.putExtra("notiType", "updateJwtToken");
                }
                PaymentFrameworkApp.a(var12_19);
            }
        });
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<ReplenishTokenRequestData>, i> {
        String mF = null;
        f mProviderTokenKey = null;
        c mw = null;

        public a(String string, c c2, f f2) {
            this.mF = string;
            this.mw = c2;
            this.mProviderTokenKey = f2;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<ReplenishTokenRequestData> var2_2) {
            block36 : {
                block35 : {
                    block33 : {
                        block38 : {
                            block37 : {
                                block34 : {
                                    Log.i("TokenReplenisher", "onRequestComplete:Replenish Request: " + var1_1);
                                    var3_3 = z.this.iJ.r(z.this.mTokenId);
                                    var4_4 = false;
                                    var5_5 = this.mw.getCardBrand();
                                    var6_6 = "";
                                    switch (var1_1) {
                                        default: {
                                            if (var1_1 < 500) break;
                                            com.samsung.android.spayfw.core.retry.c.b(z.this);
                                            break block34;
                                        }
                                        case 200: 
                                        case 202: {
                                            if (var2_2 != null) ** GOTO lbl21
                                            Log.e("TokenReplenisher", "ReplenishTokenRequestData is null");
                                            var10_9 = true;
                                            var4_4 = false;
                                            var11_11 = null;
                                            var12_10 = null;
                                            var13_7 = null;
                                            var9_8 = 0;
                                            break block33;
lbl21: // 1 sources:
                                            var21_13 = var2_2.getResult();
                                            if (var21_13 != null && var21_13.getStatus() != null) ** GOTO lbl40
                                            if (var21_13 == null) {
                                                Log.e("TokenReplenisher", "TokenResponseData is null");
                                                var10_9 = true;
                                                var4_4 = false;
                                                var11_11 = null;
                                                var12_10 = null;
                                                var13_7 = null;
                                                var9_8 = 0;
                                            } else {
                                                Log.e("TokenReplenisher", "TokenResponseData token status is null");
                                                var10_9 = true;
                                                var4_4 = false;
                                                var11_11 = null;
                                                var12_10 = null;
                                                var13_7 = null;
                                                var9_8 = 0;
                                            }
                                            break block33;
lbl40: // 1 sources:
                                            var22_14 = z.this.jJ.bq(z.this.mTokenId);
                                            if (var3_3 != null && var22_14 != null) ** GOTO lbl56
                                            Log.e("TokenReplenisher", "unable to get card object ");
                                            if (var22_14 != null) {
                                                Log.i("TokenReplenisher", "delete record from db ");
                                                z.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, var22_14.getEnrollmentId());
                                            }
                                            if (var3_3 != null) {
                                                Log.e("TokenReplenisher", "delete card object");
                                                z.this.iJ.t(z.this.mTokenId);
                                            }
                                            var9_8 = -6;
                                            var10_9 = true;
                                            var4_4 = false;
                                            var11_11 = null;
                                            var12_10 = null;
                                            var13_7 = null;
                                            break block33;
lbl56: // 1 sources:
                                            var25_15 = new TokenStatus(m.a(var21_13), var21_13.getStatus().getReason());
                                            var26_16 = var25_15.getCode();
                                            if (var21_13.getData() == null || !var3_3.ad().isReplenishDataAvailable(var21_13.getData())) ** GOTO lbl69
                                            var28_17 = var3_3.ad().replenishTokenTA(var21_13.getData(), var21_13.getStatus());
                                            if (var28_17 == null || var28_17.getErrorCode() != 0) break block35;
                                            var22_14.setTokenStatus(var25_15.getCode());
                                            var22_14.H(var25_15.getReason());
                                            var3_3.ac().setTokenStatus(var25_15.getCode());
                                            var3_3.ac().H(var25_15.getReason());
                                            z.this.jJ.d(var22_14);
                                            var29_18 = var22_14.getTokenStatus();
                                            var30_19 = true;
                                            break block36;
lbl69: // 1 sources:
                                            v.a(z.this.mContext, this.mF, 120000L);
                                            var10_9 = false;
                                            var4_4 = false;
                                            var27_20 = null;
lbl73: // 2 sources:
                                            do {
                                                z.remove(z.this.mTokenId);
                                                var13_7 = var25_15;
                                                var12_10 = var27_20;
                                                var11_11 = var26_16;
                                                var9_8 = 0;
                                                break block33;
                                                break;
                                            } while (true);
                                        }
                                        case 404: 
                                        case 503: {
                                            com.samsung.android.spayfw.core.retry.c.b(z.this);
                                            var9_8 = -1;
                                            var10_9 = false;
                                            var4_4 = false;
                                            var11_11 = null;
                                            var12_10 = null;
                                            var13_7 = null;
                                            break block33;
                                        }
                                        case 0: {
                                            if (h.ak(z.this.mContext)) {
                                                com.samsung.android.spayfw.core.retry.c.b(z.this);
                                            } else {
                                                com.samsung.android.spayfw.core.retry.c.a(z.this);
                                            }
                                            var9_8 = -1;
                                            var10_9 = false;
                                            var4_4 = false;
                                            var11_11 = null;
                                            var12_10 = null;
                                            var13_7 = null;
                                            break block33;
                                        }
                                        case -2: {
                                            var9_8 = -206;
                                            var10_9 = false;
                                            var4_4 = false;
                                            var11_11 = null;
                                            var12_10 = null;
                                            var13_7 = null;
                                            break block33;
                                        }
                                        case 403: {
                                            var7_21 = var2_2.fa();
                                            if (var7_21 == null || var7_21.getCode() == null || !"403.5".equals((Object)(var8_22 = var7_21.getCode()))) break block37;
                                            break block38;
                                        }
                                    }
                                    z.remove(z.this.mTokenId);
                                }
                                Log.i("TokenReplenisher", "DEFAULT CASE");
                                var13_7 = null;
                                var9_8 = -1;
                                var10_9 = false;
                                var12_10 = null;
                                var11_11 = null;
                                break block33;
                            }
                            var8_22 = var6_6;
                        }
                        z.remove(z.this.mTokenId);
                        var6_6 = var8_22;
                        var9_8 = -202;
                        var10_9 = false;
                        var4_4 = false;
                        var11_11 = null;
                        var12_10 = null;
                        var13_7 = null;
                    }
                    if (var9_8 != 0) {
                        Log.e("TokenReplenisher", "Replenish Token Failed - Error Code = " + var9_8);
                        if (var3_3 != null) {
                            var20_12 = new d(11, -1, this.mProviderTokenKey);
                            var20_12.ar(var6_6);
                            var3_3.ad().updateRequestStatus(var20_12);
                        }
                    } else if (var3_3 != null) {
                        var14_23 = new d(11, 0, this.mProviderTokenKey);
                        var3_3.ad().updateRequestStatus(var14_23);
                    }
                    if (var10_9) {
                        if (var4_4) {
                            z.this.a(null, z.this.mTokenId, var11_11, "TOKEN_CHANGE", c.y(var5_5), var12_10, true);
                        } else {
                            Log.e("TokenReplenisher", "processTokenChange:Send error report to TR server");
                            z.this.b(null, z.this.mTokenId, var11_11, "TOKEN_CHANGE", c.y(var5_5), var12_10, true);
                        }
                    }
                    if (z.this.lS != null) {
                        if (var9_8 == -6) {
                            z.this.lS.onTokenStatusUpdate(z.this.lU, z.this.mTokenId, var13_7);
                        }
                        if (var9_8 != 0) {
                            z.this.lS.onFail(z.this.lU, var9_8);
                        }
                        try {
                            z.this.lS.onTokenReplenishRequested(z.this.lU, z.this.mTokenId);
                        }
                        catch (RemoteException var19_24) {
                            Log.c("TokenReplenisher", var19_24.getMessage(), var19_24);
                        }
                        z.this.lS = null;
                        return;
                    }
                    var15_25 = new Intent("com.samsung.android.spayfw.action.notification");
                    if (var1_1 == -6 || var1_1 == -2) {
                        if (var1_1 == -6) {
                            var15_25.putExtra("notiType", "syncAllCards");
                        } else if (var1_1 == -2) {
                            var15_25.putExtra("notiType", "updateJwtToken");
                        }
                    } else {
                        var15_25.putExtra("notiType", "syncAllCards");
                    }
                    PaymentFrameworkApp.a(var15_25);
                    return;
                }
                var29_18 = var26_16;
                var30_19 = false;
            }
            v.remove(z.this.mTokenId);
            var26_16 = var29_18;
            var4_4 = var30_19;
            var10_9 = true;
            var27_20 = var28_17;
            ** while (true)
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int n2, ServerCertificates serverCertificates, i i2) {
            boolean bl = true;
            Log.w("TokenReplenisher", "onCertsReceived: called for Replenish");
            c c2 = z.this.iJ.r(this.mF);
            if (c2 == null) {
                Log.e("TokenReplenisher", "TokenReplenisher : unable to get Card object :" + this.mF);
                try {
                    if (z.this.lS != null) {
                        z.this.lS.onFail(z.this.lU, -6);
                    }
                }
                catch (RemoteException remoteException) {
                    Log.c("TokenReplenisher", remoteException.getMessage(), remoteException);
                }
                z.this.lS = null;
                return;
            }
            if (z.this.a(c2.getEnrollmentId(), c2, serverCertificates)) {
                com.samsung.android.spayfw.payprovider.c c3 = c2.ad().getReplenishmentRequestDataTA();
                if (c3 != null && c3.getErrorCode() == 0) {
                    ((ReplenishTokenRequestData)i2.eT()).setData(c3.ch());
                    i2.bf(z.this.P(this.mw.getCardBrand()));
                    i2.a(this);
                    Log.i("TokenReplenisher", "replenish request successfully sent after server cert update");
                    bl = false;
                } else {
                    Log.e("TokenReplenisher", " unable to get replenish data from pay provider");
                }
            } else {
                Log.e("TokenReplenisher", "Server certificate update failed.Replenishment aborted");
            }
            if (!bl) return;
            if (z.this.lS == null) return;
            try {
                z.this.lS.onFail(z.this.lU, -1);
            }
            catch (RemoteException remoteException) {
                Log.c("TokenReplenisher", remoteException.getMessage(), remoteException);
            }
            z.this.lS = null;
        }
    }

}

