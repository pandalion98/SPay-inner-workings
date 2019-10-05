/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.RemoteException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.l;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.k;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvSelectionResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;

public class j
extends o {
    protected ISelectIdvCallback lo;
    protected IdvMethod lp;
    protected String mEnrollmentId;

    public j(Context context, String string, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback) {
        super(context);
        this.mEnrollmentId = string;
        this.lp = idvMethod;
        this.lo = iSelectIdvCallback;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void process() {
        Log.d("IdvSelector", "selectIdv()");
        if (this.mEnrollmentId == null || this.lo == null || this.iJ == null || this.lp == null || this.lp.getId() == null) {
            int n2 = -5;
            if (this.iJ == null) {
                Log.e("IdvSelector", "selectIdv  - Failed to initialize account");
                n2 = -1;
            } else {
                Log.e("IdvSelector", "selectIdv Failed - Invalid inputs");
            }
            if (this.lo == null) return;
            try {
                this.lo.onFail(this.mEnrollmentId, n2);
                return;
            }
            catch (RemoteException remoteException) {
                Log.c("IdvSelector", remoteException.getMessage(), remoteException);
                return;
            }
        }
        c c2 = this.iJ.q(this.mEnrollmentId);
        if (c2 == null) {
            Log.e("IdvSelector", "selectIdv Failed - unable to find the card in memory. ");
            try {
                this.lo.onFail(this.mEnrollmentId, -6);
                return;
            }
            catch (RemoteException remoteException) {
                Log.c("IdvSelector", remoteException.getMessage(), remoteException);
                return;
            }
        }
        if (c2.ac() != null && c2.ac().getTokenStatus() != null && "PENDING_PROVISION".equals((Object)c2.ac().getTokenStatus())) {
            TokenRequestData tokenRequestData = l.a(System.currentTimeMillis(), this.mEnrollmentId, null, null);
            this.lQ.a(c.y(c2.getCardBrand()), tokenRequestData, this.lp).a(new a(this.mEnrollmentId, this.lo));
            return;
        }
        Log.e("IdvSelector", "selectIdv Failed - token is null or token staus is not correct. ");
        if (c2.ac() != null && c2.ac().getTokenStatus() != null) {
            Log.e("IdvSelector", "selectIdv Failed - token status:  " + c2.ac().getTokenStatus());
        }
        try {
            this.lo.onFail(this.mEnrollmentId, -4);
            return;
        }
        catch (RemoteException remoteException) {
            Log.c("IdvSelector", remoteException.getMessage(), remoteException);
            return;
        }
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<IdvSelectionResponseData>, k> {
        ISelectIdvCallback lo;
        String mEnrollmentId;

        public a(String string, ISelectIdvCallback iSelectIdvCallback) {
            this.mEnrollmentId = string;
            this.lo = iSelectIdvCallback;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<IdvSelectionResponseData> var2_2) {
            block29 : {
                var3_3 = -1;
                var4_4 = -17;
                Log.d("IdvSelector", "SelectIdv : onRequestComplete:  " + var1_1);
                if (j.this.iJ.q(this.mEnrollmentId) == null) {
                    Log.e("IdvSelector", "selectIdv Failed - unable to find the card in memory. ");
                    try {
                        this.lo.onFail(this.mEnrollmentId, -6);
                        return;
                    }
                    catch (RemoteException var13_5) {
                        Log.c("IdvSelector", var13_5.getMessage(), var13_5);
                        return;
                    }
                }
                switch (var1_1) {
                    default: {
                        var6_6 = null;
                        break block29;
                    }
                    case 202: {
                        if (var2_2 != null) {
                            var6_6 = m.a(var2_2.getResult());
                            var3_3 = 0;
                        } else {
                            Log.e("IdvSelector", "IdvSelectionResponseData is null");
                            var3_3 = -204;
                            var6_6 = null;
                        }
                        break block29;
                    }
                    case 403: 
                    case 407: {
                        var11_8 = var2_2.fa();
                        if (var11_8 == null || var11_8.getCode() == null) break;
                        var12_9 = var11_8.getCode();
                        if (!"407.3".equals((Object)var12_9) && !"407.2".equals((Object)var12_9)) {
                            if ("407.1".equals((Object)var12_9)) {
                                var4_4 = -16;
                            } else if ("403.2".equals((Object)var12_9)) {
                                var4_4 = -18;
                            } else if ("403.3".equals((Object)var12_9)) {
                                var4_4 = -19;
                            } else if ("403.6".equals((Object)var12_9)) {
                                var4_4 = -20;
                            } else if ("403.7".equals((Object)var12_9)) {
                                var4_4 = -21;
                            }
                        }
                        var3_3 = var4_4;
                        var6_6 = null;
                        break block29;
                    }
                    case 205: {
                        j.this.iJ.s(this.mEnrollmentId);
                        j.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                        var3_3 = -8;
                        var6_6 = null;
                        break block29;
                    }
                    case 404: 
                    case 500: {
                        var3_3 = -205;
                        var6_6 = null;
                        break block29;
                    }
                    case 0: 
                    case 503: {
                        var3_3 = -201;
                        var6_6 = null;
                        break block29;
                    }
                    case -2: {
                        var3_3 = -206;
                        var6_6 = null;
                        break block29;
                    }
                    case 400: {
                        var5_10 = var2_2.fa();
                        if (var5_10 != null && var5_10.getCode() != null) {
                            var8_11 = "403.1".equals((Object)var5_10.getCode()) != false ? -202 : var3_3;
                            var3_3 = var8_11;
                            var6_6 = null;
                        } else {
                            var6_6 = null;
                        }
                        break block29;
                    }
                }
                var3_3 = var4_4;
                var6_6 = null;
            }
            if (var3_3 != 0) ** GOTO lbl81
            try {
                this.lo.onSuccess(this.mEnrollmentId, var6_6);
                return;
lbl81: // 1 sources:
                this.lo.onFail(this.mEnrollmentId, var3_3);
                return;
            }
            catch (RemoteException var7_7) {
                Log.c("IdvSelector", var7_7.getMessage(), var7_7);
                return;
            }
        }
    }

}

