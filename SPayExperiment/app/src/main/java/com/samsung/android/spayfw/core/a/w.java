/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  com.google.gson.JsonObject
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.b;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.storage.TokenRecordStorage;

public class w
extends o {
    protected String mEnrollmentId;
    protected IDeleteCardCallback mt;
    protected Bundle mv;

    public w(Context context, String string, Bundle bundle, IDeleteCardCallback iDeleteCardCallback) {
        super(context);
        this.mEnrollmentId = string;
        this.mt = iDeleteCardCallback;
        this.mv = bundle;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void process() {
        int n2;
        block14 : {
            block13 : {
                block12 : {
                    n2 = -1;
                    if (this.mEnrollmentId != null && this.mt != null && this.iJ != null) break block12;
                    if (this.mt == null) return;
                    if (this.iJ != null) break block13;
                    break block14;
                }
                c c2 = this.iJ.q(this.mEnrollmentId);
                if (c2 == null) {
                    com.samsung.android.spayfw.b.c.e("TokenDeleter", "Token delete Failed - Invalid Enrollment Id");
                    try {
                        this.mt.onFail(this.mEnrollmentId, -6, null);
                        return;
                    }
                    catch (RemoteException remoteException) {
                        com.samsung.android.spayfw.b.c.c("TokenDeleter", remoteException.getMessage(), remoteException);
                        return;
                    }
                }
                com.samsung.android.spayfw.b.c.i("TokenDeleter", "TokenDeleter: Tokenstatus" + c2.ac().getTokenStatus());
                if ("ENROLLED".equals((Object)c2.ac().getTokenStatus()) || c2.ac().getTokenId() == null || TextUtils.equals((CharSequence)c2.getCardBrand(), (CharSequence)"GM")) {
                    this.iJ.s(this.mEnrollmentId);
                    if (this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId) < 1) {
                        com.samsung.android.spayfw.b.c.e("TokenDeleter", "Not able to delete enrollementId from DB");
                    }
                    try {
                        TokenStatus tokenStatus = new TokenStatus("DISPOSED", null);
                        this.mt.onSuccess(this.mEnrollmentId, tokenStatus);
                        return;
                    }
                    catch (RemoteException remoteException) {
                        com.samsung.android.spayfw.b.c.c("TokenDeleter", remoteException.getMessage(), remoteException);
                        return;
                    }
                }
                com.samsung.android.spayfw.payprovider.c c3 = c2.ad().getDeleteRequestDataTA(this.mv);
                if (c3 != null && c3.getErrorCode() == 0) {
                    Data data = new Data();
                    data.setData(c3.ch());
                    b b2 = this.lQ.a(c.y(c2.getCardBrand()), c2.ac().getTokenId(), data);
                    b2.bf(this.P(c2.getCardBrand()));
                    b2.a(new a(this.mEnrollmentId, c2, this.mt));
                    com.samsung.android.spayfw.b.c.d("TokenDeleter", "TokenDeleter: deleteToken request made for: " + c2.ac().getTokenId());
                    com.samsung.android.spayfw.b.c.i("TokenDeleter", "TokenDeleter: deleteToken request made");
                    return;
                }
                com.samsung.android.spayfw.b.c.e("TokenDeleter", "Not able to delete: pay provider error");
                try {
                    this.mt.onFail(this.mEnrollmentId, -1, null);
                    return;
                }
                catch (RemoteException remoteException) {
                    com.samsung.android.spayfw.b.c.c("TokenDeleter", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            n2 = -5;
        }
        try {
            com.samsung.android.spayfw.b.c.e("TokenDeleter", "Token delete Failed - Invalid inputs");
            this.mt.onFail(this.mEnrollmentId, n2, null);
            return;
        }
        catch (RemoteException remoteException) {
            com.samsung.android.spayfw.b.c.c("TokenDeleter", remoteException.getMessage(), remoteException);
            return;
        }
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<String>, b> {
        String mEnrollmentId;
        IDeleteCardCallback mt;
        c mw = null;

        public a(String string, c c2, IDeleteCardCallback iDeleteCardCallback) {
            this.mEnrollmentId = string;
            this.mt = iDeleteCardCallback;
            this.mw = c2;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<String> var2_2) {
            block20 : {
                var3_3 = -1;
                com.samsung.android.spayfw.b.c.i("TokenDeleter", "TokenDeleter: onRequestComplete:  " + var1_1);
                var4_4 = w.this.iJ.q(this.mEnrollmentId);
                var5_5 = new TokenStatus("DISPOSED", null);
                switch (var1_1) {
                    case 200: 
                    case 202: {
                        var3_3 = 0;
                        ** break;
                    }
                    case 204: 
                    case 404: 
                    case 410: {
                        var3_3 = 0;
                        ** break;
                    }
                    case 500: {
                        var3_3 = -205;
                        ** break;
                    }
                    case 0: 
                    case 503: {
                        var3_3 = -201;
                    }
lbl17: // 5 sources:
                    default: {
                        break block20;
                    }
                    case -2: 
                }
                var3_3 = -206;
            }
            if (var4_4 == null) ** GOTO lbl32
            try {
                if (var4_4.ac() != null) {
                    if (var3_3 != 0) {
                        com.samsung.android.spayfw.b.c.e("TokenDeleter", "Delete Token Failed - Error Code = " + var3_3);
                        var16_6 = new d(22, -1, var4_4.ac().aQ());
                        var4_4.ad().updateRequestStatus(var16_6);
                    } else {
                        var17_13 = new d(22, 0, var4_4.ac().aQ());
                        var4_4.ad().updateRequestStatus(var17_13);
                    }
                }
lbl32: // 5 sources:
                if (var3_3 == 0) {
                    var6_7 = new TokenStatus("DISPOSED", null);
                    if (var4_4 != null) {
                        var8_8 = var4_4.ad().updateTokenStatusTA(null, var6_7);
                        var9_9 = w.this.jJ.bp(this.mEnrollmentId);
                        var10_10 = c.y(var4_4.getCardBrand());
                        var11_11 = var4_4.ac().getTokenId();
                        if (var9_9 != null) {
                            w.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                            com.samsung.android.spayfw.b.c.d("TokenDeleter", "TokenDeleter: db recored deleted for : " + this.mEnrollmentId);
                            var13_12 = com.samsung.android.spayfw.fraud.a.x(w.this.mContext);
                            if (var13_12 != null) {
                                var13_12.k(var9_9.getTrTokenId(), "DISPOSED");
                            }
                        }
                        w.this.iJ.s(this.mEnrollmentId);
                        if (var3_3 != 204) {
                            if (var8_8 != null && var8_8.getErrorCode() == 0) {
                                w.this.a(null, var11_11, "DISPOSED", "STATUS_CHANGE", var10_10, var8_8, false);
                            } else {
                                w.this.b(null, var11_11, "DISPOSED", "STATUS_CHANGE", var10_10, var8_8, false);
                            }
                        }
                    }
                    this.mt.onSuccess(this.mEnrollmentId, var5_5);
                    return;
                }
            }
            catch (RemoteException var7_14) {
                com.samsung.android.spayfw.b.c.c("TokenDeleter", var7_14.getMessage(), var7_14);
                return;
            }
            var15_15 = var4_4 != null && var4_4.ac() != null ? new TokenStatus(var4_4.ac().getTokenStatus(), var4_4.ac().aP()) : var5_5;
            this.mt.onFail(this.mEnrollmentId, var3_3, var15_15);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void a(int n2, ServerCertificates serverCertificates, b b2) {
            com.samsung.android.spayfw.b.c.d("TokenDeleter", "onCertsReceived: called for Delete");
            boolean bl = false;
            c c2 = w.this.iJ.q(this.mEnrollmentId);
            if (c2 == null) {
                com.samsung.android.spayfw.b.c.e("TokenDeleter", "TokenDeleter : unable to get Card object :" + this.mEnrollmentId);
                try {
                    TokenStatus tokenStatus = new TokenStatus("DISPOSED", null);
                    this.mt.onSuccess(this.mEnrollmentId, tokenStatus);
                    return;
                }
                catch (RemoteException remoteException) {
                    com.samsung.android.spayfw.b.c.c("TokenDeleter", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            if (w.this.a(c2.getEnrollmentId(), c2, serverCertificates)) {
                c2.ac().aQ();
                com.samsung.android.spayfw.payprovider.c c3 = c2.ad().getDeleteRequestDataTA(null);
                if (c3 != null && c3.getErrorCode() == 0) {
                    Data data = new Data();
                    data.setData(c3.ch());
                    b2.k(data);
                    b2.bf(w.this.P(this.mw.getCardBrand()));
                    b2.a(this);
                    com.samsung.android.spayfw.b.c.i("TokenDeleter", "TokenDeleter: deleteToken request made for: " + this.mw.ac().getTokenId());
                } else {
                    com.samsung.android.spayfw.b.c.e("TokenDeleter", " unable to get delete data from pay provider");
                    bl = true;
                }
            } else {
                com.samsung.android.spayfw.b.c.e("TokenDeleter", "Server certificate update failed.Delete aborted");
                bl = true;
            }
            if (!bl || this.mt == null) return;
            TokenStatus tokenStatus = new TokenStatus(c2.ac().getTokenStatus(), c2.ac().aP());
            try {
                this.mt.onFail(this.mEnrollmentId, -1, tokenStatus);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("TokenDeleter", remoteException.getMessage(), remoteException);
                return;
            }
        }
    }

}

