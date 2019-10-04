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
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.l;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.e;
import com.samsung.android.spayfw.remoteservice.tokenrequester.f;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;

public class u
extends o {
    protected String mEnrollmentId;
    protected ICommonCallback ml;
    protected boolean mm;

    public u(Context context, String string, boolean bl, ICommonCallback iCommonCallback) {
        super(context);
        this.mEnrollmentId = string;
        this.mm = bl;
        this.ml = iCommonCallback;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void process() {
        if (this.mEnrollmentId == null || this.ml == null || this.iJ == null) {
            if (this.ml == null) return;
            try {
                com.samsung.android.spayfw.b.c.e("TncProcessor", "acceptTnc Failed - Invalid inputs");
                this.ml.onFail(this.mEnrollmentId, -5);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("TncProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        c c2 = this.iJ.q(this.mEnrollmentId);
        com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bp(this.mEnrollmentId);
        if (c2 == null || a2 == null) {
            if (c2 == null) {
                com.samsung.android.spayfw.b.c.e("TncProcessor", "acceptTnc Failed - Invalid Enrollment Id");
            } else {
                com.samsung.android.spayfw.b.c.e("TncProcessor", "acceptTnc Failed - unable to find Enrollment Id from db");
            }
            try {
                this.ml.onFail(this.mEnrollmentId, -6);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("TncProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        if (!this.mm) {
            TokenRequestData tokenRequestData = l.a(-1L, this.mEnrollmentId, null, null);
            this.lQ.a(c.y(c2.getCardBrand()), tokenRequestData).a(new a(this.mEnrollmentId, this.ml));
            return;
        }
        a2.b(System.currentTimeMillis());
        this.jJ.d(a2);
        try {
            this.ml.onSuccess(this.mEnrollmentId);
            return;
        }
        catch (RemoteException remoteException) {
            com.samsung.android.spayfw.b.c.c("TncProcessor", remoteException.getMessage(), remoteException);
            return;
        }
    }

    private class a
    extends Request.a<f, e> {
        String mEnrollmentId;
        ICommonCallback ml;

        public a(String string, ICommonCallback iCommonCallback) {
            this.mEnrollmentId = string;
            this.ml = iCommonCallback;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, f var2_2) {
            com.samsung.android.spayfw.b.c.i("TncProcessor", "Tnc reject to TR server: onRequestComplete:  " + var1_1);
            var3_3 = 0;
            switch (var1_1) {
                default: {
                    var3_3 = -1;
                    break;
                }
                case 500: {
                    var3_3 = -205;
                    break;
                }
                case 0: 
                case 503: {
                    var3_3 = -201;
                }
                case 202: 
                case 205: {
                    break;
                }
                case -2: {
                    var3_3 = -206;
                }
            }
            if (var3_3 != 0) ** GOTO lbl24
            try {
                if (u.this.jJ.bp(this.mEnrollmentId) != null) {
                    u.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                    com.samsung.android.spayfw.b.c.d("TncProcessor", "TncCallback: db recored deleted for : " + this.mEnrollmentId);
                }
                u.this.iJ.s(this.mEnrollmentId);
                this.ml.onSuccess(this.mEnrollmentId);
                return;
lbl24: // 1 sources:
                this.ml.onFail(this.mEnrollmentId, var3_3);
                return;
            }
            catch (RemoteException var4_4) {
                com.samsung.android.spayfw.b.c.c("TncProcessor", var4_4.getMessage(), var4_4);
                return;
            }
        }
    }

}

