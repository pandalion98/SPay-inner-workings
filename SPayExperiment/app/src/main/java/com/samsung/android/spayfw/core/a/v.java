/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Handler
 *  android.os.RemoteException
 *  com.google.gson.JsonObject
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.a.z;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.g;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.utils.h;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class v
extends o
implements Runnable {
    private static final Map<String, v> mo = new HashMap();
    private static final PaymentFrameworkApp.a mp = new PaymentFrameworkApp.a();
    protected IPushMessageCallback lS;
    protected String lU;
    protected String mTokenId;
    protected long mq;
    protected int mr;

    private v(Context context, String string) {
        super(context);
        this.mTokenId = string;
    }

    private void Q(String string) {
        com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "deleting token id = " + string);
        if (this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string) < 1) {
            com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "Not able to delete Token from DB");
        }
        this.iJ.t(string);
    }

    public static v S(String string) {
        Class<v> class_ = v.class;
        synchronized (v.class) {
            v v2 = (v)mo.get((Object)string);
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return v2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(Context context, String string, long l2) {
        Class<v> class_ = v.class;
        synchronized (v.class) {
            Handler handler = z.getHandler();
            v v2 = (v)mo.get((Object)string);
            if (v2 == null) {
                com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "New Instance of Token Change Checker");
                v2 = new v(context, string);
                v2.mq = l2;
            } else {
                com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "Update Instance of Token Change Checker");
                handler.removeCallbacks((Runnable)v2);
                v2.mq = l2;
            }
            if (v2.mq != -1L) {
                handler.postDelayed((Runnable)v2, v2.mq);
            }
            if ((long)v2.mr >= 10L) {
                com.samsung.android.spayfw.b.c.w("TokenChangeChecker", "Token Change Checker Retry Limit Reached. Try Token Replenisher.");
                v.remove(string);
                a a2 = a.a(context, null);
                if (a2 != null) {
                    c c2 = a2.r(string);
                    if (c2 != null) {
                        mp.a(c2.ac().aQ());
                    } else {
                        com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "Card is NULL");
                    }
                } else {
                    com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "Account is NULL");
                }
            } else {
                mo.put((Object)string, (Object)v2);
            }
            // ** MonitorExit[var11_3] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(Context context, String string, IPushMessageCallback iPushMessageCallback, String string2) {
        Class<v> class_ = v.class;
        synchronized (v.class) {
            Handler handler = z.getHandler();
            v v2 = (v)mo.get((Object)string);
            if (v2 == null) {
                com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "New Instance of Token Change Checker");
                v2 = new v(context, string);
            } else {
                com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "Update Instance of Token Change Checker");
                handler.removeCallbacks((Runnable)v2);
            }
            v2.mq = 0L;
            v2.lS = iPushMessageCallback;
            v2.lU = string2;
            handler.postDelayed((Runnable)v2, v2.mq);
            mo.put((Object)string, (Object)v2);
            // ** MonitorExit[var9_4] (shouldn't be in output)
            return;
        }
    }

    public static void remove(String string) {
        Class<v> class_ = v.class;
        synchronized (v.class) {
            com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "Remove Instance of Token Change Checker");
            v v2 = (v)mo.remove((Object)string);
            z.getHandler().removeCallbacks((Runnable)v2);
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return;
        }
    }

    public static void restart() {
        Class<v> class_ = v.class;
        synchronized (v.class) {
            com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "restart");
            Handler handler = z.getHandler();
            for (Map.Entry entry : mo.entrySet()) {
                if (((v)entry.getValue()).mq != -1L) continue;
                v v2 = (v)entry.getValue();
                v2.mq = 0L;
                handler.postDelayed((Runnable)v2, v2.mq);
            }
            return;
        }
    }

    public void process() {
        z.getHandler().post((Runnable)this);
    }

    public void run() {
        com.samsung.android.spayfw.b.c.d("TokenChangeChecker", "Entered token change checker : tokenId " + this.mTokenId);
        com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "Entered token change checker : retryCount " + this.mr);
        this.mr = 1 + this.mr;
        final c c2 = this.iJ.r(this.mTokenId);
        if (c2 == null) {
            com.samsung.android.spayfw.b.c.e("TokenChangeChecker", " unable to get card based on tokenId. ignore replenish request");
            return;
        }
        final String string = c2.getCardBrand();
        g g2 = this.lQ.x(c.y(c2.getCardBrand()), this.mTokenId);
        g2.h(false);
        g2.b(new Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, g>(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void a(int n2, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> c22) {
                c c3;
                boolean bl;
                TokenStatus tokenStatus;
                String string4;
                boolean bl2;
                int n3;
                e e2;
                block39 : {
                    String string2;
                    block38 : {
                        int n4 = 0;
                        e2 = null;
                        string2 = c2.ac().getTokenStatus();
                        com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "onRequestComplete: Token change: code " + n2);
                        c3 = v.this.iJ.r(v.this.mTokenId);
                        switch (n2) {
                            default: {
                                if (n2 < 500) break;
                                v.a(v.this.mContext, v.this.mTokenId, 120000L);
                                break block38;
                            }
                            case 200: {
                                TokenResponseData tokenResponseData = c22.getResult();
                                if (tokenResponseData == null) {
                                    com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "TokenResponseData is null");
                                    bl2 = true;
                                    string4 = string2;
                                    bl = false;
                                    e2 = null;
                                    tokenStatus = null;
                                    n3 = 0;
                                } else {
                                    com.samsung.android.spayfw.storage.models.a a2 = v.this.jJ.bq(v.this.mTokenId);
                                    if (c3 == null || a2 == null) {
                                        com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "unable to get card object ");
                                        if (a2 != null) {
                                            com.samsung.android.spayfw.b.c.i("TokenChangeChecker", "delete record from db ");
                                            v.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, a2.getEnrollmentId());
                                        }
                                        if (c3 != null) {
                                            com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "delete card object");
                                            v.this.iJ.s(c3.getEnrollmentId());
                                        }
                                        TokenStatus tokenStatus2 = new TokenStatus("DISPOSED", null);
                                        v.remove(v.this.mTokenId);
                                        tokenStatus = tokenStatus2;
                                        n3 = -6;
                                        bl2 = true;
                                        string4 = string2;
                                        bl = false;
                                        e2 = null;
                                    } else {
                                        boolean bl3;
                                        TokenStatus tokenStatus3 = new TokenStatus(m.a(tokenResponseData), tokenResponseData.getStatus().getReason());
                                        if (tokenResponseData.getData() == null || !c3.ad().isReplenishDataAvailable(tokenResponseData.getData())) {
                                            v.a(v.this.mContext, v.this.mTokenId, 120000L);
                                            return;
                                        }
                                        e2 = c3.ad().replenishTokenTA(tokenResponseData.getData(), tokenResponseData.getStatus());
                                        if (e2 != null && e2.getErrorCode() == 0 && tokenStatus3 != null) {
                                            a2.setTokenStatus(tokenStatus3.getCode());
                                            a2.H(tokenStatus3.getReason());
                                            c3.ac().setTokenStatus(tokenStatus3.getCode());
                                            c3.ac().H(tokenStatus3.getReason());
                                            v.this.jJ.d(a2);
                                            string2 = a2.getTokenStatus();
                                            bl3 = true;
                                        } else {
                                            n4 = -36;
                                            bl3 = false;
                                        }
                                        v.remove(v.this.mTokenId);
                                        tokenStatus = tokenStatus3;
                                        n3 = n4;
                                        String string3 = string2;
                                        bl = bl3;
                                        bl2 = true;
                                        string4 = string3;
                                    }
                                }
                                break block39;
                            }
                            case 404: 
                            case 410: {
                                com.samsung.android.spayfw.b.c.w("TokenChangeChecker", "unable to find the token on server. something wrong. deleting the token");
                                TokenStatus tokenStatus4 = new TokenStatus("DISPOSED", null);
                                e e3 = null;
                                if (c3 != null) {
                                    e3 = c3.ad().updateTokenStatusTA(null, tokenStatus4);
                                    v.this.Q(v.this.mTokenId);
                                }
                                v.remove(v.this.mTokenId);
                                e2 = e3;
                                tokenStatus = tokenStatus4;
                                n3 = -6;
                                string4 = string2;
                                bl2 = false;
                                bl = false;
                                break block39;
                            }
                            case 503: {
                                v.a(v.this.mContext, v.this.mTokenId, 120000L);
                                n3 = -1;
                                string4 = string2;
                                bl2 = false;
                                bl = false;
                                e2 = null;
                                tokenStatus = null;
                                break block39;
                            }
                            case 0: {
                                if (h.ak(v.this.mContext)) {
                                    v.a(v.this.mContext, v.this.mTokenId, 120000L);
                                } else {
                                    v.a(v.this.mContext, v.this.mTokenId, -1L);
                                }
                                n3 = -1;
                                string4 = string2;
                                bl2 = false;
                                bl = false;
                                e2 = null;
                                tokenStatus = null;
                                break block39;
                            }
                            case -2: {
                                v.remove(v.this.mTokenId);
                                n3 = -206;
                                string4 = string2;
                                bl2 = false;
                                bl = false;
                                e2 = null;
                                tokenStatus = null;
                                break block39;
                            }
                        }
                        v.remove(v.this.mTokenId);
                    }
                    bl2 = false;
                    tokenStatus = null;
                    n3 = -1;
                    string4 = string2;
                    bl = false;
                }
                if (n3 != 0) {
                    com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "Replenish Token Failed - Error Code = " + n3);
                    if (c3 != null) {
                        d d2 = new d(23, -1, c3.ac().aQ());
                        c3.ad().updateRequestStatus(d2);
                    }
                } else if (c3 != null) {
                    d d3 = new d(23, 0, c3.ac().aQ());
                    c3.ad().updateRequestStatus(d3);
                }
                if (bl2) {
                    if (bl) {
                        v.this.a(null, v.this.mTokenId, string4, "TOKEN_CHANGE", c.y(string), e2, true);
                    } else {
                        com.samsung.android.spayfw.b.c.e("TokenChangeChecker", "processTokenChange:Send error report to TR server");
                        v.this.b(null, v.this.mTokenId, string4, "TOKEN_CHANGE", c.y(string), e2, true);
                    }
                }
                if (v.this.lS != null) {
                    if (n3 == -6) {
                        v.this.lS.onTokenStatusUpdate(v.this.lU, v.this.mTokenId, tokenStatus);
                    }
                    if (n3 != 0) {
                        v.this.lS.onFail(v.this.lU, n3);
                    }
                    try {
                        v.this.lS.onTokenReplenished(v.this.lU, v.this.mTokenId);
                    }
                    catch (RemoteException remoteException) {
                        com.samsung.android.spayfw.b.c.c("TokenChangeChecker", remoteException.getMessage(), remoteException);
                    }
                    v.this.lS = null;
                    return;
                }
                Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                if (n2 == -6 || n2 == -2) {
                    if (n2 == -6) {
                        intent.putExtra("notiType", "syncAllCards");
                    } else if (n2 == -2) {
                        intent.putExtra("notiType", "updateJwtToken");
                    }
                } else {
                    intent.putExtra("notiType", "syncAllCards");
                }
                PaymentFrameworkApp.a(intent);
            }
        });
    }

}

