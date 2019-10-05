/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.RemoteException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.core.p;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.h;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;

public class i
extends o {
    protected IRefreshIdvCallback lm;
    protected String mEnrollmentId;

    public i(Context context, String string, IRefreshIdvCallback iRefreshIdvCallback) {
        super(context);
        this.mEnrollmentId = string;
        this.lm = iRefreshIdvCallback;
    }

    protected int ba() {
        if (this.mEnrollmentId == null || this.mEnrollmentId.isEmpty() || this.lm == null) {
            Log.e("IdvRefresher", "refreshIdv Failed - Invalid inputs");
            return -5;
        }
        int n2 = super.a(true, this.mEnrollmentId, null, true);
        if (n2 != 0) {
            Log.e("IdvRefresher", "refreshIdv Failed - validate failed: " + n2);
            return n2;
        }
        if (this.iJ == null) {
            Log.e("IdvRefresher", "refreshIdv Failed - mAccount is null: ");
            return -1;
        }
        if (!super.a(this.iJ.q(this.mEnrollmentId), "PENDING_PROVISION")) {
            Log.w("IdvRefresher", "refreshIdv Failed - Not valid token status");
            return -4;
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void process() {
        Log.d("IdvRefresher", "refreshIdv()");
        int n2 = this.ba();
        if (n2 != 0) {
            Log.e("IdvRefresher", "refreshIdv validation failed");
            if (this.lm == null) return;
            {
                try {
                    this.lm.onFail(this.mEnrollmentId, n2);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.c("IdvRefresher", remoteException.getMessage(), remoteException);
                    return;
                }
            }
        }
        c c2 = this.iJ.q(this.mEnrollmentId);
        if (c2 != null && c2.getCardBrand() != null && c2.ac() != null && c2.ac().getTokenId() != null) {
            this.lQ.y(c.y(c2.getCardBrand()), c2.ac().getTokenId()).a(new a());
            return;
        }
        Log.e("IdvRefresher", "invalid cardInfo found in the PF");
        if (this.lm == null) return;
        try {
            this.lm.onFail(this.mEnrollmentId, -1);
            return;
        }
        catch (RemoteException remoteException) {
            Log.c("IdvRefresher", remoteException.getMessage(), remoteException);
            return;
        }
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<IdvOptionsData>, h> {
        private a() {
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<IdvOptionsData> var2_2) {
            block12 : {
                block11 : {
                    var3_3 = null;
                    Log.d("IdvRefresher", "RefreshIdv : onRequestComplete:  " + var1_1);
                    var4_4 = i.this.ba();
                    if (var4_4 != 0 && i.this.lm != null) {
                        try {
                            i.this.lm.onFail(i.this.mEnrollmentId, var4_4);
                        }
                        catch (RemoteException var11_8) {
                            Log.c("IdvRefresher", var11_8.getMessage(), var11_8);
                        }
                    }
                    var5_5 = i.this.iJ.q(i.this.mEnrollmentId);
                    switch (var1_1) {
                        default: {
                            var10_6 = var2_2 != null ? var2_2.fa() : null;
                        }
                        case 200: {
                            if (var2_2 == null) ** GOTO lbl21
                            var3_3 = m.a(var5_5, var2_2.getResult());
                            if (var3_3 != null) break block11;
                            Log.e("IdvRefresher", "IdvMethod is null");
                            var6_7 = -3;
                            break block12;
lbl21: // 1 sources:
                            Log.e("IdvRefresher", "IdvMethod is null");
                            var6_7 = -3;
                            var3_3 = null;
                            break block12;
                        }
                        case 205: {
                            var6_7 = -8;
                            i.this.iJ.s(i.this.mEnrollmentId);
                            i.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, i.this.mEnrollmentId);
                            var3_3 = null;
                            break block12;
                        }
                    }
                    var6_7 = p.a(var1_1, var10_6);
                    break block12;
                }
                var6_7 = var4_4;
            }
            try {
                if (i.this.lm == null) return;
                if (var6_7 == 0) {
                    i.this.lm.onSuccess(i.this.mEnrollmentId, var3_3);
                    return;
                }
                i.this.lm.onFail(i.this.mEnrollmentId, var6_7);
                return;
            }
            catch (RemoteException var9_9) {
                Log.c("IdvRefresher", var9_9.getMessage(), var9_9);
                return;
            }
        }
    }

}

