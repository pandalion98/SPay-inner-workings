/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.ParcelFileDescriptor
 *  android.os.RemoteException
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.ServerRequest;
import com.samsung.android.spayfw.appinterface.ServerResponseData;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.commerce.b;
import com.samsung.android.spayfw.remoteservice.d;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import java.io.File;
import java.io.IOException;

public class r
extends o {
    private static r me;

    private r(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(ServerResponseData serverResponseData, File file) {
        c.d("ServerRequestProcessor", "cleanServerResponse");
        if (serverResponseData != null && serverResponseData.getFd() != null) {
            try {
                serverResponseData.getFd().close();
                c.d("ServerRequestProcessor", "closing serverResponse fd");
            }
            catch (IOException iOException) {
                c.c("ServerRequestProcessor", iOException.getMessage(), iOException);
            }
            if (file != null) {
                file.delete();
                c.d("ServerRequestProcessor", "Delete file");
            }
        }
    }

    static /* synthetic */ void a(r r2, ServerResponseData serverResponseData, File file) {
        r2.a(serverResponseData, file);
    }

    public static final r u(Context context) {
        Class<r> class_ = r.class;
        synchronized (r.class) {
            if (me == null) {
                me = new r(context);
            }
            r r2 = me;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return r2;
        }
    }

    private com.samsung.android.spayfw.remoteservice.a v(int n2) {
        switch (n2) {
            default: {
                c.e("ServerRequestProcessor", "Client not found " + n2);
                return null;
            }
            case 0: {
                return l.Q(this.mContext);
            }
            case 1: {
                return com.samsung.android.spayfw.remoteservice.c.a.K(this.mContext);
            }
            case 3: {
                return com.samsung.android.spayfw.remoteservice.cashcard.a.I(this.mContext);
            }
            case 2: {
                return com.samsung.android.spayfw.remoteservice.a.a.H(this.mContext);
            }
            case 4: {
                return b.J(this.mContext);
            }
            case 5: 
        }
        return com.samsung.android.spayfw.remoteservice.d.a.L(this.mContext);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void a(ServerRequest var1_1, IServerResponseCallback var2_2) {
        c.d("ServerRequestProcessor", "process() called!");
        if (var1_1 == null || var2_2 == null) ** GOTO lbl5
        try {
            block8 : {
                if (var1_1.getRelativeUrl() != null && var1_1.getServiceType() != -1 && var1_1.getRequestMethod() != -1) break block8;
lbl5: // 2 sources:
                if (var2_2 != null) {
                    var2_2.onFail(-5);
                }
                c.e("ServerRequestProcessor", "process: inputs are invalid! request = " + var1_1 + "; cb = " + var2_2);
                if (var1_1 == null) return;
                c.e("ServerRequestProcessor", "process: request.toString = " + var1_1.toString());
                return;
            }
            var4_3 = this.v(var1_1.getServiceType());
            if (var4_3 == null) {
                c.e("ServerRequestProcessor", "Error not able to get client");
                var2_2.onFail(-5);
                return;
            }
            var4_3.a(var1_1.getRequestMethod(), var1_1.getRelativeUrl(), var1_1.getHeaders(), var1_1.getBody()).a(new a(var2_2));
            c.i("ServerRequestProcessor", "ServerRequestProcessor: generic request made: " + var1_1.getRequestMethod() + " " + var1_1.getRelativeUrl());
            return;
        }
        catch (RemoteException var3_4) {
            c.c("ServerRequestProcessor", var3_4.getMessage(), var3_4);
            var2_2.onFail(-5);
            return;
        }
    }

    private class a
    extends Request.a<d, com.samsung.android.spayfw.remoteservice.b> {
        IServerResponseCallback mf;

        public a(IServerResponseCallback iServerResponseCallback) {
            this.mf = iServerResponseCallback;
        }

        /*
         * Exception decompiling
         */
        @Override
        public void a(int var1_1, d var2_2) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[SWITCH], 12[CASE]], but top level block is 14[CATCHBLOCK]
            // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
            // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
            // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
            // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
            // org.benf.cfr.reader.entities.g.p(Method.java:396)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
            // org.benf.cfr.reader.entities.d.c(ClassFile.java:773)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:870)
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
    }

}

