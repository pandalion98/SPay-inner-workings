package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import com.samsung.android.analytics.sdk.AnalyticEvent.Data;
import com.samsung.android.analytics.sdk.AnalyticEvent.Field;
import com.samsung.android.analytics.sdk.AnalyticEvent.Type;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.SystemPropertiesWrapper;
import com.samsung.android.spayfw.remoteservice.p018a.AnalyticsRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.AnalyticsRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.AnalyticsReportCache;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.core.a.a */
public class AnalyticsFrameworkProcessor extends Processor {
    private static Handler handler;
    private static HandlerThread km;
    private static AnalyticsFrameworkProcessor kq;
    private boolean kn;
    private AnalyticsRequesterClient ko;
    private AnalyticsReportCache kp;

    /* renamed from: com.samsung.android.spayfw.core.a.a.1 */
    class AnalyticsFrameworkProcessor extends Handler {
        final /* synthetic */ AnalyticsFrameworkProcessor kr;

        AnalyticsFrameworkProcessor(AnalyticsFrameworkProcessor analyticsFrameworkProcessor, Looper looper) {
            this.kr = analyticsFrameworkProcessor;
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        Log.m285d("AnalyticsFrameworkProcessor", "ACTION_UPLOAD_EVENT");
                        AnalyticsFrameworkProcessor analyticsFrameworkProcessor = null;
                        if (message != null) {
                            analyticsFrameworkProcessor = (AnalyticsFrameworkProcessor) message.obj;
                        }
                        Log.m285d("AnalyticsFrameworkProcessor", "Entered AnalyticsReporter: lEvent " + analyticsFrameworkProcessor);
                        this.kr.m342a(analyticsFrameworkProcessor);
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        Log.m285d("AnalyticsFrameworkProcessor", "ACTION_FLUSH_EVENTS_FROM_STORAGE");
                        this.kr.aU();
                    default:
                }
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.a.2 */
    static /* synthetic */ class AnalyticsFrameworkProcessor {
        static final /* synthetic */ int[] kt;

        static {
            kt = new int[Type.values().length];
            try {
                kt[Type.LOYALTY_SELECTED_BRAND.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                kt[Type.LOYALTY_ADD_METHOD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                kt[Type.LOYALTY_VIEW_CARD_DETAILS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                kt[Type.LOYALTY_VIEW_ALL_OFFERS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                kt[Type.LOYALTY_VIEW_CLIPPED_OFFERS.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                kt[Type.LOYALTY_CLIP_OFFER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                kt[Type.LOYALTY_LAUNCH_SIMPLE_PAY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                kt[Type.LAUNCH_ACTIVITY.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                kt[Type.ATTEMPT_ADD_CARD.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                kt[Type.ADD_CARD_SUCCESS.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                kt[Type.DELETE_CARD_SUCCESS.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                kt[Type.DELETED_ALL_CARDS.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                kt[Type.RESTORE_STARTED.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                kt[Type.RESTORE_COMPLETED.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.a.a */
    public static class AnalyticsFrameworkProcessor {
        public AnalyticsFrameworkProcessor ku;

        /* renamed from: com.samsung.android.spayfw.core.a.a.a.a */
        public static class AnalyticsFrameworkProcessor {
            public AnalyticsFrameworkProcessor kv;

            /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a */
            private static class AnalyticsFrameworkProcessor {
                public AnalyticsFrameworkProcessor kx;
                public ArrayList<AnalyticsFrameworkProcessor> ky;

                /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.a */
                public static class AnalyticsFrameworkProcessor {
                    public AnalyticsFrameworkProcessor kA;
                    public AnalyticsFrameworkProcessor kB;
                    public AnalyticsFrameworkProcessor kC;
                    public AnalyticsFrameworkProcessor kz;

                    /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.a.a */
                    public static class AnalyticsFrameworkProcessor {
                        public String id;
                        public String kD;
                        public String kE;
                        public String kF;
                        public AnalyticsFrameworkProcessor kG;
                        public String model;
                        public String pfVersion;

                        /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.a.a.a */
                        public static class AnalyticsFrameworkProcessor {
                            public String build;
                            public String name;
                            public String version;

                            public java.lang.String toString() {
                                /* JADX: method processing error */
/*
                                Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                                /*
                                r3 = this;
                                r2 = 34;
                                r0 = new java.lang.StringBuilder;
                                r0.<init>();
                                r1 = "{\"build\":\"";
                                r0 = r0.append(r1);
                                r1 = r3.build;
                                r0 = r0.append(r1);
                                r0 = r0.append(r2);
                                r1 = ", \"name\":\"";
                                r0 = r0.append(r1);
                                r1 = r3.name;
                                r0 = r0.append(r1);
                                r0 = r0.append(r2);
                                r1 = ", \"version\":\"";
                                r0 = r0.append(r1);
                                r1 = r3.version;
                                r0 = r0.append(r1);
                                r0 = r0.append(r2);
                                r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                                r0 = r0.append(r1);
                                r0 = r0.toString();
                                return r0;
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.a.a.a.toString():java.lang.String");
                            }
                        }

                        public java.lang.String toString() {
                            /* JADX: method processing error */
/*
                            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                            /*
                            r3 = this;
                            r2 = 34;
                            r0 = new java.lang.StringBuilder;
                            r0.<init>();
                            r1 = "{\"id\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.id;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"model\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.model;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"appVersion\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.kD;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"appPkgName\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.kE;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"specVersion\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.kF;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"pfVersion\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.pfVersion;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"os\":";
                            r0 = r0.append(r1);
                            r1 = r3.kG;
                            r0 = r0.append(r1);
                            r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                            r0 = r0.append(r1);
                            r0 = r0.toString();
                            return r0;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.a.a.toString():java.lang.String");
                        }
                    }

                    /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.a.b */
                    public static class AnalyticsFrameworkProcessor {
                        public String id;

                        public java.lang.String toString() {
                            /* JADX: method processing error */
/*
                            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                            /*
                            r2 = this;
                            r0 = new java.lang.StringBuilder;
                            r0.<init>();
                            r1 = "{\"id\":\"";
                            r0 = r0.append(r1);
                            r1 = r2.id;
                            r0 = r0.append(r1);
                            r1 = 34;
                            r0 = r0.append(r1);
                            r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                            r0 = r0.append(r1);
                            r0 = r0.toString();
                            return r0;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.a.b.toString():java.lang.String");
                        }
                    }

                    /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.a.c */
                    public static class AnalyticsFrameworkProcessor {
                        public String id;

                        public java.lang.String toString() {
                            /* JADX: method processing error */
/*
                            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                            /*
                            r2 = this;
                            r0 = new java.lang.StringBuilder;
                            r0.<init>();
                            r1 = "{\"id\":\"";
                            r0 = r0.append(r1);
                            r1 = r2.id;
                            r0 = r0.append(r1);
                            r1 = 34;
                            r0 = r0.append(r1);
                            r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                            r0 = r0.append(r1);
                            r0 = r0.toString();
                            return r0;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.a.c.toString():java.lang.String");
                        }
                    }

                    /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.a.d */
                    public static class AnalyticsFrameworkProcessor {
                        public String id;

                        public java.lang.String toString() {
                            /* JADX: method processing error */
/*
                            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                            /*
                            r2 = this;
                            r0 = new java.lang.StringBuilder;
                            r0.<init>();
                            r1 = "{\"id\":\"";
                            r0 = r0.append(r1);
                            r1 = r2.id;
                            r0 = r0.append(r1);
                            r1 = 34;
                            r0 = r0.append(r1);
                            r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                            r0 = r0.append(r1);
                            r0 = r0.toString();
                            return r0;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.a.d.toString():java.lang.String");
                        }
                    }

                    public java.lang.String toString() {
                        /* JADX: method processing error */
/*
                        Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                        /*
                        r2 = this;
                        r0 = new java.lang.StringBuilder;
                        r0.<init>();
                        r1 = "{\"device\":";
                        r0 = r0.append(r1);
                        r1 = r2.kz;
                        r1 = r1.toString();
                        r0 = r0.append(r1);
                        r1 = ", \"user\":";
                        r0 = r0.append(r1);
                        r1 = r2.kA;
                        r1 = r1.toString();
                        r0 = r0.append(r1);
                        r1 = ", \"wallet\":";
                        r0 = r0.append(r1);
                        r1 = r2.kB;
                        r1 = r1.toString();
                        r0 = r0.append(r1);
                        r1 = ", \"session\":";
                        r0 = r0.append(r1);
                        r1 = r2.kC;
                        r1 = r1.toString();
                        r0 = r0.append(r1);
                        r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                        r0 = r0.append(r1);
                        r0 = r0.toString();
                        return r0;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.a.toString():java.lang.String");
                    }
                }

                /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.b */
                public static class AnalyticsFrameworkProcessor {
                    public String category;
                    public String id;
                    public ArrayList<AnalyticsFrameworkProcessor> kH;
                    public String timestamp;

                    /* renamed from: com.samsung.android.spayfw.core.a.a.a.a.a.b.a */
                    public static class AnalyticsFrameworkProcessor {
                        public String key;
                        public String value;

                        public AnalyticsFrameworkProcessor() {
                            /* JADX: method processing error */
/*
                            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                            /*
                            r1 = this;
                            r0 = 0;
                            r1.<init>();
                            r1.key = r0;
                            r1.value = r0;
                            return;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.b.a.<init>():void");
                        }

                        public java.lang.String toString() {
                            /* JADX: method processing error */
/*
                            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                            /*
                            r3 = this;
                            r2 = 34;
                            r0 = new java.lang.StringBuilder;
                            r0.<init>();
                            r1 = "{\"key\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.key;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = ", \"value\":\"";
                            r0 = r0.append(r1);
                            r1 = r3.value;
                            r0 = r0.append(r1);
                            r0 = r0.append(r2);
                            r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                            r0 = r0.append(r1);
                            r0 = r0.toString();
                            return r0;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.b.a.toString():java.lang.String");
                        }
                    }

                    public AnalyticsFrameworkProcessor() {
                        /* JADX: method processing error */
/*
                        Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                        /*
                        r1 = this;
                        r0 = 0;
                        r1.<init>();
                        r1.category = r0;
                        r1.id = r0;
                        r1.timestamp = r0;
                        r1.kH = r0;
                        r0 = new java.util.ArrayList;
                        r0.<init>();
                        r1.kH = r0;
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.b.<init>():void");
                    }

                    public java.lang.String toString() {
                        /* JADX: method processing error */
/*
                        Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                        /*
                        r3 = this;
                        r2 = 34;
                        r0 = new java.lang.StringBuilder;
                        r0.<init>();
                        r1 = "{\"category\":\"";
                        r0 = r0.append(r1);
                        r1 = r3.category;
                        r0 = r0.append(r1);
                        r0 = r0.append(r2);
                        r1 = ", \"id\":\"";
                        r0 = r0.append(r1);
                        r1 = r3.id;
                        r0 = r0.append(r1);
                        r0 = r0.append(r2);
                        r1 = ", \"timestamp\":\"";
                        r0 = r0.append(r1);
                        r1 = r3.timestamp;
                        r0 = r0.append(r1);
                        r0 = r0.append(r2);
                        r1 = ", \"fields\":";
                        r0 = r0.append(r1);
                        r1 = r3.kH;
                        r0 = r0.append(r1);
                        r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                        r0 = r0.append(r1);
                        r0 = r0.toString();
                        return r0;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.b.toString():java.lang.String");
                    }
                }

                public java.lang.String toString() {
                    /* JADX: method processing error */
/*
                    Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r2 = this;
                    r0 = new java.lang.StringBuilder;
                    r0.<init>();
                    r1 = "{\"context\":";
                    r0 = r0.append(r1);
                    r1 = r2.kx;
                    r1 = r1.toString();
                    r0 = r0.append(r1);
                    r1 = ", \"events\":";
                    r0 = r0.append(r1);
                    r1 = r2.ky;
                    r1 = r1.toString();
                    r0 = r0.append(r1);
                    r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                    r0 = r0.append(r1);
                    r0 = r0.toString();
                    return r0;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.a.toString():java.lang.String");
                }
            }

            public java.lang.String toString() {
                /* JADX: method processing error */
/*
                Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r2 = this;
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "{\"app\":";
                r0 = r0.append(r1);
                r1 = r2.kv;
                r0 = r0.append(r1);
                r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
                r0 = r0.append(r1);
                r0 = r0.toString();
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.a.toString():java.lang.String");
            }
        }

        public java.lang.String toString() {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r2 = this;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "{\"data\":";
            r0 = r0.append(r1);
            r1 = r2.ku;
            r0 = r0.append(r1);
            r1 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
            r0 = r0.append(r1);
            r0 = r0.toString();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.toString():java.lang.String");
        }

        public AnalyticsFrameworkProcessor(java.util.List<com.samsung.android.analytics.sdk.AnalyticEvent> r7, com.samsung.android.analytics.sdk.AnalyticContext r8) {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:119)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r6 = this;
            r6.<init>();
            r0 = 0;
            r6.ku = r0;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kz;
            r1 = r8.m159K();
            r0.id = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kz;
            r1 = r8.m153E();
            r0.model = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kz;
            r0 = r0.kG;
            r1 = r8.m156H();
            r0.build = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kz;
            r0 = r0.kG;
            r1 = r8.m155G();
            r0.name = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kz;
            r0 = r0.kG;
            r1 = r8.m154F();
            r0.version = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kA;
            r1 = r8.m158J();
            r0.id = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kB;
            r1 = r8.m157I();
            r0.id = r1;
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.kx;
            r0 = r0.kC;
            r1 = r8.m152D();
            r0.id = r1;
            r2 = new com.samsung.android.spayfw.core.a.a$a$a$a$b;
            r2.<init>();
            if (r7 == 0) goto L_0x00e4;
        L_0x0083:
            r3 = r7.iterator();
        L_0x0087:
            r0 = r3.hasNext();
            if (r0 == 0) goto L_0x00eb;
        L_0x008d:
            r0 = r3.next();
            r0 = (com.samsung.android.analytics.sdk.AnalyticEvent) r0;
            r1 = r0.m166L();
            r1 = r1.getString();
            r2.category = r1;
            r0 = r0.m167M();
            if (r0 == 0) goto L_0x00dc;
        L_0x00a3:
            r0 = r0.valueSet();
            if (r0 == 0) goto L_0x00d4;
        L_0x00a9:
            r4 = r0.iterator();
        L_0x00ad:
            r0 = r4.hasNext();
            if (r0 == 0) goto L_0x0087;
        L_0x00b3:
            r0 = r4.next();
            r0 = (java.util.Map.Entry) r0;
            r5 = new com.samsung.android.spayfw.core.a.a$a$a$a$b$a;
            r5.<init>();
            r1 = r0.getKey();
            r1 = (java.lang.String) r1;
            r5.key = r1;
            r0 = r0.getValue();
            r0 = (java.lang.String) r0;
            r5.value = r0;
            r0 = r2.kH;
            r0.add(r5);
            goto L_0x00ad;
        L_0x00d4:
            r0 = "AnalyticsFrameworkProcessor";
            r1 = "content values are empty (lSet is null)";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
            goto L_0x0087;
        L_0x00dc:
            r0 = "AnalyticsFrameworkProcessor";
            r1 = "content values are empty";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
            goto L_0x0087;
        L_0x00e4:
            r0 = "AnalyticsFrameworkProcessor";
            r1 = "given list of events are null";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        L_0x00eb:
            r0 = r6.ku;
            r0 = r0.kv;
            r0 = r0.ky;
            r0.add(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.a.a.<init>(java.util.List, com.samsung.android.analytics.sdk.AnalyticContext):void");
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.a.b */
    public static class AnalyticsFrameworkProcessor extends AnalyticsFrameworkProcessor {
    }

    /* renamed from: com.samsung.android.spayfw.core.a.a.c */
    public static class AnalyticsFrameworkProcessor {
        private String accuracy;
        private String altitude;
        private String latitude;
        private String longitude;
        private String provider;
        private String time;
        private String timezone;

        public AnalyticsFrameworkProcessor(String str, String str2, String str3, String str4, String str5) {
            this.latitude = str;
            this.longitude = str2;
            this.timezone = str3;
            this.provider = str4;
            this.altitude = str5;
        }

        public void setAccuracy(String str) {
            this.accuracy = str;
        }

        public void setTime(String str) {
            this.time = str;
        }

        public String toJsonString() {
            return "{\"latitude\":\"" + this.latitude + "\"" + ",\"longitude\":\"" + this.longitude + "\"" + ",\"altitude\":\"" + this.altitude + "\"" + ",\"timezone\":\"" + this.timezone + "\"" + ",\"provider\":\"" + this.provider + "\"" + ",\"accuracy\":\"" + this.accuracy + "\"" + ",\"time\":\"" + this.time + "\"" + '}';
        }
    }

    private AnalyticsFrameworkProcessor(Context context) {
        super(context);
        this.kn = true;
        this.ko = null;
        this.kp = null;
        this.ko = AnalyticsRequesterClient.m1159H(context);
        try {
            this.kp = AnalyticsReportCache.aa(context);
        } catch (RuntimeRemoteException e) {
            e.printStackTrace();
            Log.m286e("AnalyticsFrameworkProcessor", "AnalyticsReportCache threw exception and failed to store or upload events");
        }
        km = new HandlerThread("AnalyticsReportThread");
        km.start();
        handler = new AnalyticsFrameworkProcessor(this, km.getLooper());
    }

    public static final synchronized AnalyticsFrameworkProcessor m340l(Context context) {
        AnalyticsFrameworkProcessor analyticsFrameworkProcessor;
        synchronized (AnalyticsFrameworkProcessor.class) {
            if (kq == null) {
                kq = new AnalyticsFrameworkProcessor(context);
            }
            analyticsFrameworkProcessor = kq;
        }
        return analyticsFrameworkProcessor;
    }

    private void m337b(AnalyticEvent analyticEvent) {
        Log.m285d("AnalyticsFrameworkProcessor", "updateLocationInfo");
        Location googleLocation = DeviceInfo.getGoogleLocation();
        if (googleLocation != null) {
            AnalyticsFrameworkProcessor analyticsFrameworkProcessor = new AnalyticsFrameworkProcessor(String.valueOf(googleLocation.getLatitude()), String.valueOf(googleLocation.getLongitude()), null, googleLocation.getProvider(), String.valueOf(googleLocation.getAltitude()));
            analyticsFrameworkProcessor.setAccuracy(String.valueOf(googleLocation.getAccuracy()));
            analyticsFrameworkProcessor.setTime(String.valueOf(googleLocation.getTime()));
            analyticEvent.m168a(Field.LOCATION, analyticsFrameworkProcessor.toJsonString());
            Log.m285d("AnalyticsFrameworkProcessor", "location = " + analyticsFrameworkProcessor.toJsonString());
        }
    }

    public void m341a(AnalyticEvent analyticEvent, AnalyticContext analyticContext) {
        Log.m285d("AnalyticsFrameworkProcessor", "process");
        String str = SystemPropertiesWrapper.get("ro.build.PDA");
        Log.m285d("AnalyticsFrameworkProcessor", "BuildNumber :" + str);
        analyticContext.m162n(str);
        str = null;
        if (this.mContext != null) {
            str = DeviceInfo.getDeviceId(this.mContext);
        }
        Log.m285d("AnalyticsFrameworkProcessor", "fetched DID:" + str);
        analyticContext.m161m(str);
        if (m338c(analyticEvent) || m339d(analyticEvent)) {
            m337b(analyticEvent);
            Log.m285d("AnalyticsFrameworkProcessor", "locationUpdatedEvent" + analyticEvent);
        }
        this.kp.m1244b(analyticEvent, analyticContext);
    }

    public void m343a(List<AnalyticEvent> list, AnalyticContext analyticContext) {
        Log.m285d("AnalyticsFrameworkProcessor", "process-event list");
        String str = SystemPropertiesWrapper.get("ro.build.PDA");
        Log.m285d("AnalyticsFrameworkProcessor", "BuildNumber :" + str);
        analyticContext.m162n(str);
        str = null;
        if (this.mContext != null) {
            str = DeviceInfo.getDeviceId(this.mContext);
        }
        Log.m285d("AnalyticsFrameworkProcessor", "fetched DID:" + str);
        analyticContext.m161m(str);
        Message message = new Message();
        message.obj = new AnalyticsFrameworkProcessor(list, analyticContext);
        message.what = 1;
        handler.sendMessage(message);
    }

    protected synchronized void m342a(AnalyticsFrameworkProcessor analyticsFrameworkProcessor) {
        Log.m285d("AnalyticsFrameworkProcessor", "Sending analytic event Report");
        if (analyticsFrameworkProcessor == null) {
            Log.m285d("AnalyticsFrameworkProcessor", "Json Report Data Empty");
        } else {
            if (analyticsFrameworkProcessor instanceof AnalyticsFrameworkProcessor) {
                Log.m285d("AnalyticsFrameworkProcessor", "Report GSIMData := " + analyticsFrameworkProcessor);
            } else {
                Log.m285d("AnalyticsFrameworkProcessor", "Report Data := " + analyticsFrameworkProcessor);
            }
            if (AnalyticsRequest.fc()) {
                Log.m286e("AnalyticsFrameworkProcessor", " server is UnAvailable Now, reports should remain in the cache");
            }
        }
    }

    private void aU() {
        Log.m285d("AnalyticsFrameworkProcessor", "flushEventsFromStorage");
    }

    private boolean m338c(AnalyticEvent analyticEvent) {
        return analyticEvent.m166L().equals(Type.ATTEMPT_TRANSACTION) || analyticEvent.m166L().equals(Type.TRANSACTION_SUCCESS);
    }

    private boolean m339d(AnalyticEvent analyticEvent) {
        String value;
        switch (AnalyticsFrameworkProcessor.kt[analyticEvent.m166L().ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case F2m.PPB /*3*/:
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
            case ECCurve.COORD_SKEWED /*7*/:
                return true;
            case X509KeyUsage.keyAgreement /*8*/:
                value = analyticEvent.getValue(Field.ACTIVITY.getString());
                if (value != null) {
                    if (value.equals(Data.SCREEN_US_LOYALTY_RETAILERS.getString())) {
                        return true;
                    }
                    if (value.equals(Data.SCREEN_US_LOYALTY_CARD_REG_COMPLETE.getString())) {
                        return true;
                    }
                }
                break;
            case NamedCurve.sect283k1 /*9*/:
            case NamedCurve.sect283r1 /*10*/:
            case CertStatus.UNREVOKED /*11*/:
            case CertStatus.UNDETERMINED /*12*/:
                value = analyticEvent.getValue(Field.CARD_CATEGORY.getString());
                if (value != null && value.equals(Data.CARD_CATEGORY_MEMBERSHIP_CARD.toString())) {
                    return true;
                }
            case NamedCurve.sect571k1 /*13*/:
            case NamedCurve.sect571r1 /*14*/:
                value = analyticEvent.getValue(Field.RESTORE_TYPE.getString());
                if (value != null && value.equals(Data.RESTORE_TYPE_LOYALTY.toString())) {
                    return true;
                }
        }
        return false;
    }
}
