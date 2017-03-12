package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TACommands.CommandInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct.Unsigned32;
import javolution.io.Struct.Unsigned8;

public class GlobalMembershipCommands extends TACommands {
    private static final boolean DEBUG;
    public static final byte[] TL_MAGIC_NUM;

    public static class AddCard {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Request.a */
            public static class C0542a extends TAStruct {
                Blob zk;
                Blob zl;

                public C0542a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zk = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zl = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Request.a.<init>():void");
                }

                public C0542a(byte[] r3, byte[] r4) {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zk = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zl = r0;
                    r0 = r2.zk;
                    r0.setData(r3);
                    r0 = r2.zl;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Request.a.<init>(byte[], byte[]):void");
                }
            }

            Request(byte[] r5, byte[] r6) {
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
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AddCard$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.TL_MAGIC_NUM;
                r3 = 8;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0543a zm;

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Response.a */
            public static class C0543a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;
                Blob zn;

                public C0543a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zn = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r2.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r2.error_msg = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Response.a.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = r4.mResponseCode;
                r1 = r4.mErrorMsg;
                r2 = r4.mResponse;
                r3.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AddCard$Response$a;
                r0.<init>();
                r3.zm = r0;
                r0 = r3.zm;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AddCard$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AddCard$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard.getMaxResponseSize():int");
        }
    }

    public static class AuthenticateTransaction {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Request.a */
            public static class C0544a extends TAStruct {
                Blob auth_result_sec_obj;

                public C0544a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.auth_result_sec_obj = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Request.a.<init>():void");
                }

                public C0544a(byte[] r3) {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.auth_result_sec_obj = r0;
                    r0 = r2.auth_result_sec_obj;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Request.a.<init>(byte[]):void");
                }
            }

            Request(byte[] r5) {
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
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AuthenticateTransaction$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.TL_MAGIC_NUM;
                r3 = 24;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0545a zo;

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Response.a */
            public static class C0545a extends TAStruct {
                Unsigned32 auth_result;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0545a() {
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
                    r1.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r1.auth_result = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r1.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r1.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r1.error_msg = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Response.a.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = r4.mResponseCode;
                r1 = r4.mErrorMsg;
                r2 = r4.mResponse;
                r3.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AuthenticateTransaction$Response$a;
                r0.<init>();
                r3.zo = r0;
                r0 = r3.zo;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AuthenticateTransaction$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$AuthenticateTransaction$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction.getMaxResponseSize():int");
        }
    }

    public static class ClearData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ClearData.Request.a */
            public static class C0546a extends TAStruct {
                Unsigned32 unused_uint;

                public C0546a() {
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
                    r1.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r1.unused_uint = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ClearData.Request.a.<init>():void");
                }
            }
        }

        public static class Response extends TACommandResponse {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ClearData.Response.a */
            public static class C0547a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0547a() {
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
                    r1.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r1.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r1.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r1.error_msg = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ClearData.Response.a.<init>():void");
                }
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$ClearData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ClearData.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$ClearData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ClearData.getMaxResponseSize():int");
        }
    }

    public static class ExtractGlobalMembershipCardDetail {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Request.a */
            public static class C0548a extends TAStruct {
                Blob buf_card_encdata;
                Blob zp;

                public C0548a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zp = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_encdata = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Request.a.<init>():void");
                }

                public C0548a(byte[] r3, byte[] r4) {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.zp = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_encdata = r0;
                    r0 = r2.zp;
                    r0.setData(r3);
                    r0 = r2.buf_card_encdata;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Request.a.<init>(byte[], byte[]):void");
                }
            }

            Request(byte[] r5, byte[] r6) {
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
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$ExtractGlobalMembershipCardDetail$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.TL_MAGIC_NUM;
                r3 = 9;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0549a zq;

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Response.a */
            public static class C0549a extends TAStruct {
                Blob barcodecontent;
                Blob cardnumber;
                Unsigned8[] error_msg;
                Blob pin;
                Unsigned32 return_code;
                Blob zr;
                Blob zt;

                public C0549a() {
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
                    r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r3.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.cardnumber = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pin = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.barcodecontent = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.zr = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.zt = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r3.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.error_msg = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Response.a.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = r4.mResponseCode;
                r1 = r4.mErrorMsg;
                r2 = r4.mResponse;
                r3.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$ExtractGlobalMembershipCardDetail$Response$a;
                r0.<init>();
                r3.zq = r0;
                r0 = r3.zq;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$ExtractGlobalMembershipCardDetail$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$ExtractGlobalMembershipCardDetail$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail.getMaxResponseSize():int");
        }
    }

    public static class GetNonce {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.GetNonce.Request.a */
            public static class C0550a extends TAStruct {
                Unsigned32 _nonceSize;

                public C0550a() {
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
                    r1.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r1._nonceSize = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.GetNonce.Request.a.<init>():void");
                }
            }
        }

        public static class Response extends TACommandResponse {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.GetNonce.Response.a */
            public static class C0551a extends TAStruct {
                Unsigned8[] error_msg;
                public Blob out_data;
                Unsigned32 return_code;

                public C0551a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r2.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r2.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.out_data = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.GetNonce.Response.a.<init>():void");
                }
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$GetNonce$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.GetNonce.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$GetNonce$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.GetNonce.getMaxResponseSize():int");
        }
    }

    public static class LoadCerts {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Request.a */
            public static class C0552a extends TAStruct {
                Blob device_cert_enc;
                Blob device_cert_sign;
                Blob server_cert_enc;
                Blob server_cert_sign;
                Blob server_cert_sub;

                public C0552a() {
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
                    r2 = this;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.device_cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.device_cert_enc = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_enc = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_sub = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Request.a.<init>():void");
                }

                public C0552a(byte[] r3, byte[] r4, byte[] r5, byte[] r6, byte[] r7) {
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
                    r2 = this;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.device_cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.device_cert_enc = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_enc = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_sub = r0;
                    r0 = r2.device_cert_sign;
                    r0.setData(r3);
                    r0 = r2.device_cert_enc;
                    r0.setData(r4);
                    r0 = r2.server_cert_sign;
                    r0.setData(r5);
                    r0 = r2.server_cert_enc;
                    r0.setData(r6);
                    r0 = r2.server_cert_sub;
                    r0.setData(r7);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Request.a.<init>(byte[], byte[], byte[], byte[], byte[]):void");
                }
            }

            Request(byte[] r7, byte[] r8, byte[] r9, byte[] r10, byte[] r11) {
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
                r6 = this;
                r6.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$LoadCerts$Request$a;
                r1 = r7;
                r2 = r8;
                r3 = r9;
                r4 = r10;
                r5 = r11;
                r0.<init>(r1, r2, r3, r4, r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.TL_MAGIC_NUM;
                r3 = 6;
                r6.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Request.<init>(byte[], byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0553a zu;

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Response.a */
            public static class C0553a extends TAStruct {
                Blob cert_drk;
                Blob cert_encrypt;
                Blob cert_sign;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0553a() {
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
                    r2 = this;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r2.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r2.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r2.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.cert_drk = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.cert_encrypt = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Response.a.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = r4.mResponseCode;
                r1 = r4.mErrorMsg;
                r2 = r4.mResponse;
                r3.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$LoadCerts$Response$a;
                r0.<init>();
                r3.zu = r0;
                r0 = r3.zu;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$LoadCerts$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$LoadCerts$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.getMaxResponseSize():int");
        }
    }

    public static class MstTransmit {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.MstTransmit.Request.a */
            public static class C0554a extends TAStruct {
                Blob ac;
                Unsigned32 baudrate;
                Blob config;

                public C0554a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.baudrate = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.ac = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.config = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.MstTransmit.Request.a.<init>():void");
                }
            }
        }

        public static class Response extends TACommandResponse {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.MstTransmit.Response.a */
            public static class C0555a extends TAStruct {
                Blob error;
                Unsigned32 return_code;

                public C0555a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.return_code = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.error = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.MstTransmit.Response.a.<init>():void");
                }
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$MstTransmit$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.MstTransmit.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$MstTransmit$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.MstTransmit.getMaxResponseSize():int");
        }
    }

    public static class Utility_enc4Server_Transport {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Request.a */
            public static class C0556a extends TAStruct {
                Blob payment_instrument;
                Blob timestamp;

                public C0556a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.payment_instrument = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 7;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.timestamp = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Request.a.<init>():void");
                }

                public C0556a(byte[] r3, byte[] r4) {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.payment_instrument = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 7;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.timestamp = r0;
                    r0 = r2.payment_instrument;
                    r0.setData(r3);
                    r0 = r2.timestamp;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Request.a.<init>(byte[], byte[]):void");
                }
            }

            Request(byte[] r5, byte[] r6) {
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
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$Utility_enc4Server_Transport$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.TL_MAGIC_NUM;
                r3 = 7;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0557a zv;

            /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Response.a */
            public static class C0557a extends TAStruct {
                Unsigned8[] error_msg;
                public Blob resp;
                Unsigned32 return_code;

                public C0557a() {
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.resp = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r2.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r2.error_msg = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Response.a.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = r4.mResponseCode;
                r1 = r4.mErrorMsg;
                r2 = r4.mResponse;
                r3.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$Utility_enc4Server_Transport$Response$a;
                r0.<init>();
                r3.zv = r0;
                r0 = r3.zv;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$Utility_enc4Server_Transport$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands$Utility_enc4Server_Transport$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport.getMaxResponseSize():int");
        }
    }

    static {
        DEBUG = TAController.DEBUG;
        TL_MAGIC_NUM = new byte[]{(byte) 126, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -21, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG};
    }

    public GlobalMembershipCommands() {
        addCommandInfo(Integer.valueOf(6), new CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(8), new CommandInfo(AddCard.getMaxRequestSize(), AddCard.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(21), new CommandInfo(MstTransmit.getMaxRequestSize(), MstTransmit.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(7), new CommandInfo(Utility_enc4Server_Transport.getMaxRequestSize(), Utility_enc4Server_Transport.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(23), new CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(24), new CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(25), new CommandInfo(ClearData.getMaxRequestSize(), ClearData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(9), new CommandInfo(ExtractGlobalMembershipCardDetail.getMaxRequestSize(), ExtractGlobalMembershipCardDetail.getMaxResponseSize()));
    }
}
