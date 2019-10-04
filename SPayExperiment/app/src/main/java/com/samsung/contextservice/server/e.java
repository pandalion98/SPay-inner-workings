/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Message
 *  android.os.Parcelable
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 */
package com.samsung.contextservice.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.server.ServerListener;
import com.samsung.contextservice.server.d;
import com.samsung.contextservice.server.f;
import com.samsung.contextservice.server.g;
import com.samsung.contextservice.server.i;
import com.samsung.contextservice.server.k;
import com.samsung.contextservice.server.models.CacheResponseData;
import com.samsung.contextservice.server.models.DeviceInfo;
import com.samsung.contextservice.server.models.PolicyResponseData;
import com.samsung.contextservice.server.models.TriggerRequestData;
import com.samsung.contextservice.server.models.TriggerResponseData;
import com.samsung.contextservice.server.models.UserInfo;
import com.samsung.contextservice.server.models.WalletInfo;
import java.util.ArrayList;

public class e
extends com.samsung.contextservice.a {
    private static e GH = null;
    private d GI;
    private com.samsung.contextservice.server.b GJ = null;
    private com.samsung.contextservice.a.d GK;
    private com.samsung.contextservice.a.c GL;
    private f GM;
    private com.samsung.contextservice.server.a GN;

    private e(Context context) {
        super(context, "ContextServerManager");
        this.mContext = context;
        this.GI = d.aw(this.mContext);
        this.GJ = new com.samsung.contextservice.server.b(this.mContext);
        this.GL = new com.samsung.contextservice.a.c(this.mContext);
        this.GK = new com.samsung.contextservice.a.d(this.mContext);
        this.GM = new f(this.mContext, "PolicyPolicy", 86400000L, 1);
        this.GN = new com.samsung.contextservice.server.a(this.mContext, "CachePolicy", 3600000L, 2);
    }

    static /* synthetic */ com.samsung.contextservice.a.d a(e e2) {
        return e2.GK;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean a(Location location, ServerListener serverListener) {
        e e2 = this;
        synchronized (e2) {
            if (location == null) {
                com.samsung.contextservice.b.b.d("CtxSrvManager", "Update cache, location is null");
                return false;
            }
            com.samsung.contextservice.b.b.d("CtxSrvManager", "using " + location.toString() + " to query POIs from server");
            if (!com.samsung.contextservice.b.e.aL(this.mContext)) return true;
            com.samsung.contextservice.b.b.i("CtxSrvManager", "Update cache");
            com.samsung.contextservice.server.c c2 = this.GI.a(location);
            a a2 = new a(serverListener);
            if (c2 != null && a2 != null) {
                this.GN.b(new i<com.samsung.contextservice.server.c>(c2, a2));
                return true;
            } else {
                com.samsung.contextservice.b.b.e("CtxSrvManager", "cannot update cache");
            }
            return true;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean a(String string, ServerListener serverListener) {
        e e2 = this;
        synchronized (e2) {
            if (!com.samsung.contextservice.b.e.aL(this.mContext)) return false;
            com.samsung.contextservice.b.b.i("CtxSrvManager", "query policy");
            g g2 = this.GI.bL(null);
            b b2 = new b(serverListener);
            if (g2 == null) return true;
            if (b2 == null) return true;
            this.GM.b(new i<g>(g2, b2));
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean a(ArrayList<Poi> arrayList, ServerListener serverListener) {
        e e2 = this;
        synchronized (e2) {
            boolean bl = false;
            if (arrayList == null) return bl;
            boolean bl2 = com.samsung.contextservice.b.e.aL(this.mContext);
            bl = false;
            if (!bl2) return bl;
            com.samsung.contextservice.b.b.i("CtxSrvManager", "send trigger");
            TriggerRequestData triggerRequestData = new TriggerRequestData();
            triggerRequestData.setPois(arrayList);
            triggerRequestData.setUser(com.samsung.contextservice.b.e.aH(this.mContext));
            triggerRequestData.setWallet(new WalletInfo(com.samsung.contextservice.b.e.aI(this.mContext)));
            triggerRequestData.setDevice(DeviceInfo.getDefaultDeviceInfo(this.mContext));
            com.samsung.contextservice.b.b.d("CtxSrvManager", "Sending trigger request data " + triggerRequestData.toString());
            k k2 = this.GI.a(triggerRequestData);
            bl = false;
            if (k2 == null) return bl;
            if (serverListener == null) {
                k2.a(new c());
                return true;
            } else {
                k2.a(new c(serverListener));
            }
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static e ax(Context context) {
        Class<e> class_ = e.class;
        synchronized (e.class) {
            if (GH != null) return GH;
            if (context == null) {
                throw new Exception("context is null");
            }
            GH = new e(context);
            return GH;
        }
    }

    static /* synthetic */ com.samsung.contextservice.server.b b(e e2) {
        return e2.GJ;
    }

    static /* synthetic */ Context c(e e2) {
        return e2.mContext;
    }

    static /* synthetic */ com.samsung.contextservice.server.a d(e e2) {
        return e2.GN;
    }

    public void ay(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
        intent.putExtra("notiType", "updateJwtToken");
        com.samsung.contextservice.b.a.a(intent, context);
    }

    public void b(Location location, ServerListener serverListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", (Parcelable)location);
        bundle.putParcelable("listener", (Parcelable)serverListener);
        message.what = 2;
        message.setData(bundle);
        this.getHandler().sendMessage(message);
    }

    public void b(String string, ServerListener serverListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("id", string);
        bundle.putParcelable("listener", (Parcelable)serverListener);
        message.what = 1;
        message.setData(bundle);
        this.getHandler().sendMessage(message);
    }

    public void b(ArrayList<Poi> arrayList, ServerListener serverListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("pois", arrayList);
        bundle.putParcelable("listener", (Parcelable)serverListener);
        message.what = 3;
        message.setData(bundle);
        this.getHandler().sendMessage(message);
    }

    @Override
    public void c(Message message) {
        if (message == null) {
            return;
        }
        Bundle bundle = message.getData();
        ServerListener serverListener = (ServerListener)bundle.getParcelable("listener");
        switch (message.what) {
            default: {
                return;
            }
            case 1: {
                this.a(bundle.getString("id"), serverListener);
                return;
            }
            case 2: {
                this.a((Location)bundle.getParcelable("location"), serverListener);
                return;
            }
            case 3: 
        }
        this.a((ArrayList<Poi>)bundle.getParcelableArrayList("pois"), serverListener);
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<CacheResponseData>, com.samsung.contextservice.server.c> {
        private ServerListener GE;

        public a(ServerListener serverListener) {
            this.GE = serverListener;
        }

        /*
         * Exception decompiling
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<CacheResponseData> var2_2) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [4[CASE]], but top level block is 0[TRYBLOCK]
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

        public ServerListener gv() {
            return this.GE;
        }
    }

    private class b
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<PolicyResponseData>, g> {
        private ServerListener GE;

        public b(ServerListener serverListener) {
            this.GE = serverListener;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void a(int n2, com.samsung.android.spayfw.remoteservice.c<PolicyResponseData> c2) {
            com.samsung.contextservice.b.b.d("CtxSrvManager", "PolicyRequestCallback:onRequestComplete():  " + n2);
            switch (n2) {
                case 200: {
                    if (c2 != null && c2.getResult() != null) {
                        PolicyResponseData policyResponseData = c2.getResult();
                        com.samsung.contextservice.b.b.d("CtxSrvManager", policyResponseData.toString());
                        long l2 = e.this.GL.a(policyResponseData);
                        if (l2 >= 0L) {
                            com.samsung.contextservice.b.b.d("CtxSrvManager", "add policy " + policyResponseData.getId() + " at row " + l2);
                        } else {
                            com.samsung.contextservice.b.b.d("CtxSrvManager", "cannot add policy to db");
                        }
                        if (this.GE != null) {
                            this.GE.onSuccess();
                        }
                    }
                }
                default: {
                    break;
                }
                case -2: {
                    e.this.ay(e.this.mContext);
                    if (this.GE == null) break;
                    this.GE.W(-2);
                }
            }
            if (n2 != 0) {
                e.this.GM.C(System.currentTimeMillis());
            }
        }
    }

    private class c
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<TriggerResponseData>, k> {
        private ServerListener GE;

        private c() {
        }

        public c(ServerListener serverListener) {
            this.GE = serverListener;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void a(int n2, com.samsung.android.spayfw.remoteservice.c<TriggerResponseData> c2) {
            com.samsung.contextservice.b.b.d("CtxSrvManager", "TriggerRequestCallback:onRequestComplete():  " + n2);
            switch (n2) {
                default: {
                    if (this.GE == null) return;
                    this.GE.gD();
                    return;
                }
                case 200: 
                case 202: 
                case 204: {
                    if (this.GE == null) return;
                    this.GE.onSuccess();
                    return;
                }
                case 400: 
                case 401: 
                case 404: 
                case 503: {
                    if (this.GE == null) return;
                    this.GE.gD();
                    return;
                }
                case -2: {
                    e.this.ay(e.this.mContext);
                    if (this.GE == null) return;
                    this.GE.W(-2);
                    return;
                }
                case 0: {
                    if (this.GE == null) return;
                    this.GE.W(0);
                    return;
                }
            }
        }
    }

}

