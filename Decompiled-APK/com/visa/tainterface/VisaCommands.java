package com.visa.tainterface;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TACommands.CommandInfo;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct.Unsigned32;
import javolution.io.Struct.Unsigned8;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class VisaCommands extends TACommands {
    public static final byte[] TL_MAGIC_NUM;

    public static class AuthenticateTransaction {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Request.a */
            public static class C0667a extends TAStruct {
                Blob auth_result_sec_obj;

                public C0667a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Request.a.<init>():void");
                }

                public C0667a(byte[] r3) {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Request.a.<init>(byte[]):void");
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
                r0 = new com.visa.tainterface.VisaCommands$AuthenticateTransaction$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 21;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0668a LS;

            /* renamed from: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Response.a */
            public static class C0668a extends TAStruct {
                Unsigned32 auth_result;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0668a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$AuthenticateTransaction$Response$a;
                r0.<init>();
                r3.LS = r0;
                r0 = r3.LS;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$AuthenticateTransaction$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$AuthenticateTransaction$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.AuthenticateTransaction.getMaxResponseSize():int");
        }
    }

    public static class ClearMstData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.ClearMstData.Request.a */
            public static class C0669a extends TAStruct {
                Unsigned32 unused_uint;

                public C0669a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.Request.a.<init>():void");
                }

                public C0669a(int r5) {
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
                    r4 = this;
                    r4.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r4.unused_uint = r0;
                    r0 = r4.unused_uint;
                    r2 = (long) r5;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.Request.a.<init>(int):void");
                }
            }

            Request(int r5) {
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
                r0 = new com.visa.tainterface.VisaCommands$ClearMstData$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 22;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.Request.<init>(int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0670a LT;

            /* renamed from: com.visa.tainterface.VisaCommands.ClearMstData.Response.a */
            public static class C0670a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0670a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$ClearMstData$Response$a;
                r0.<init>();
                r3.LT = r0;
                r0 = r3.LT;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$ClearMstData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$ClearMstData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.ClearMstData.getMaxResponseSize():int");
        }
    }

    public static class GenInAppJwePaymentInfo {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.GenInAppJwePaymentInfo.Request.a */
            public static class C0671a extends TAStruct {
                Blob LU;
                Blob LV;
                Blob LW;
                Blob LX;

                public C0671a() {
                    this.LU = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LV = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LW = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LX = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                }

                public C0671a(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
                    this.LU = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LV = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LW = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LX = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.LU.setData(bArr3);
                    this.LV.setData(bArr4);
                    this.LW.setData(bArr);
                    this.LX.setData(bArr2);
                }
            }

            Request(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
                init(1, VisaCommands.TL_MAGIC_NUM, 24, new C0671a(bArr, bArr2, bArr3, bArr4).serialize());
            }
        }

        public static class Response extends TACommandResponse {
        }
    }

    public static class GenInAppPaymentInfo {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Request.a */
            public static class C0672a extends TAStruct {
                Blob LW;
                Blob LX;
                Blob nonce;

                public C0672a() {
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
                    r2.LW = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.LX = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 32;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.nonce = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Request.a.<init>():void");
                }

                public C0672a(byte[] r3, byte[] r4, byte[] r5) {
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
                    r2.LW = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.LX = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 32;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.nonce = r0;
                    r0 = r2.LW;
                    r0.setData(r3);
                    r0 = r2.LX;
                    r0.setData(r4);
                    r0 = r2.nonce;
                    r0.setData(r5);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Request.a.<init>(byte[], byte[], byte[]):void");
                }
            }

            Request(byte[] r5, byte[] r6, byte[] r7) {
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
                r0 = new com.visa.tainterface.VisaCommands$GenInAppPaymentInfo$Request$a;
                r0.<init>(r5, r6, r7);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 23;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Request.<init>(byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0673a LY;

            /* renamed from: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Response.a */
            public static class C0673a extends TAStruct {
                Unsigned8[] error_msg;
                public Blob resp;
                Unsigned32 return_code;

                public C0673a() {
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
                    r1 = 5120; // 0x1400 float:7.175E-42 double:2.5296E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.resp = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$GenInAppPaymentInfo$Response$a;
                r0.<init>();
                r3.LY = r0;
                r0 = r3.LY;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$GenInAppPaymentInfo$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$GenInAppPaymentInfo$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GenInAppPaymentInfo.getMaxResponseSize():int");
        }
    }

    public static class Generate {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.Generate.Request.a */
            public static class C0674a extends TAStruct {
                Unsigned32 LZ;
                Blob Ma;
                Blob Mb;

                public C0674a() {
                    /* JADX: method processing error */
/*
                    Error: java.util.ConcurrentModificationException
	at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:901)
	at java.util.ArrayList$Itr.next(ArrayList.java:851)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominanceFrontier(BlockProcessor.java:143)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:63)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
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
                    r2.LZ = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Ma = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mb = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.Request.a.<init>():void");
                }

                public C0674a(byte[] r5, com.visa.tainterface.VisaTAController.VISA_CRYPTO_ALG r6, byte[] r7) {
                    /* JADX: method processing error */
/*
                    Error: java.lang.UnsupportedOperationException
	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
	at jadx.core.dex.nodes.BlockNode.addDominatesOn(BlockNode.java:170)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:135)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r4 = this;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r4.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r4.LZ = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.Ma = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.Mb = r0;
                    r0 = r4.Ma;
                    r0.setData(r5);
                    r0 = r4.LZ;
                    r1 = r6.ordinal();
                    r2 = (long) r1;
                    r0.set(r2);
                    r0 = r4.Mb;
                    r0.setData(r7);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.Request.a.<init>(byte[], com.visa.tainterface.VisaTAController$VISA_CRYPTO_ALG, byte[]):void");
                }
            }

            Request(byte[] r5, com.visa.tainterface.VisaTAController.VISA_CRYPTO_ALG r6, byte[] r7) {
                /* JADX: method processing error */
/*
                Error: java.lang.UnsupportedOperationException
	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
	at jadx.core.dex.nodes.BlockNode.addDominatesOn(BlockNode.java:170)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:135)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.visa.tainterface.VisaCommands$Generate$Request$a;
                r0.<init>(r5, r6, r7);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 17;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.Request.<init>(byte[], com.visa.tainterface.VisaTAController$VISA_CRYPTO_ALG, byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0675a Mc;

            /* renamed from: com.visa.tainterface.VisaCommands.Generate.Response.a */
            public static class C0675a extends TAStruct {
                Blob Md;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0675a() {
                    /* JADX: method processing error */
/*
                    Error: java.lang.UnsupportedOperationException
	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
	at jadx.core.dex.nodes.BlockNode.addDominatesOn(BlockNode.java:170)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:135)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r2 = this;
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r2.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.return_code = r0;
                    r0 = new javolution.io.Struct.Unsigned8[r1];
                    r0 = r2.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r2.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Md = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.Response.a.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
                /* JADX: method processing error */
/*
                Error: java.lang.UnsupportedOperationException
	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
	at jadx.core.dex.nodes.BlockNode.addDominatesOn(BlockNode.java:170)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:135)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
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
                r0 = new com.visa.tainterface.VisaCommands$Generate$Response$a;
                r0.<init>();
                r3.Mc = r0;
                r0 = r3.Mc;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
            /* JADX: method processing error */
/*
            Error: java.lang.UnsupportedOperationException
	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
	at jadx.core.dex.nodes.BlockNode.addDominatesOn(BlockNode.java:170)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:135)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.visa.tainterface.VisaCommands$Generate$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
            /* JADX: method processing error */
/*
            Error: java.lang.UnsupportedOperationException
	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
	at jadx.core.dex.nodes.BlockNode.addDominatesOn(BlockNode.java:170)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:135)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:38)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.visa.tainterface.VisaCommands$Generate$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.Generate.getMaxResponseSize():int");
        }
    }

    public static class GetNonce {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.GetNonce.Request.a */
            public static class C0676a extends TAStruct {
                Unsigned32 _nonceSize;

                public C0676a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.Request.a.<init>():void");
                }

                public C0676a(int r5) {
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
                    r4 = this;
                    r4.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r4._nonceSize = r0;
                    r0 = r4._nonceSize;
                    r2 = (long) r5;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.Request.a.<init>(int):void");
                }
            }

            Request(int r5) {
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
                r0 = new com.visa.tainterface.VisaCommands$GetNonce$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 20;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.Request.<init>(int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0677a Me;

            /* renamed from: com.visa.tainterface.VisaCommands.GetNonce.Response.a */
            public static class C0677a extends TAStruct {
                Unsigned8[] error_msg;
                public Blob out_data;
                Unsigned32 return_code;

                public C0677a() {
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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.out_data = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$GetNonce$Response$a;
                r0.<init>();
                r3.Me = r0;
                r0 = r3.Me;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$GetNonce$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$GetNonce$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.GetNonce.getMaxResponseSize():int");
        }
    }

    public static class LoadCerts {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.LoadCerts.Request.a */
            public static class C0678a extends TAStruct {
                Blob device_cert_enc;
                Blob device_cert_sign;
                Blob server_cert_enc;
                Blob server_cert_sign;

                public C0678a() {
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
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.Request.a.<init>():void");
                }

                public C0678a(byte[] r3, byte[] r4, byte[] r5, byte[] r6) {
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
                    r0 = r2.device_cert_sign;
                    r0.setData(r3);
                    r0 = r2.device_cert_enc;
                    r0.setData(r4);
                    r0 = r2.server_cert_sign;
                    r0.setData(r5);
                    r0 = r2.server_cert_enc;
                    r0.setData(r6);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.Request.a.<init>(byte[], byte[], byte[], byte[]):void");
                }
            }

            Request(byte[] r5, byte[] r6, byte[] r7, byte[] r8) {
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
                r0 = new com.visa.tainterface.VisaCommands$LoadCerts$Request$a;
                r0.<init>(r5, r6, r7, r8);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 3;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.Request.<init>(byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0679a Mf;

            /* renamed from: com.visa.tainterface.VisaCommands.LoadCerts.Response.a */
            public static class C0679a extends TAStruct {
                Unsigned32 Mg;
                Unsigned32 Mh;
                Blob Mi;
                Blob cert_drk;
                Blob cert_encrypt;
                Blob cert_sign;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0679a() {
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
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.Mg = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.Mh = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 24;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mi = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$LoadCerts$Response$a;
                r0.<init>();
                r3.Mf = r0;
                r0 = r3.Mf;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$LoadCerts$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$LoadCerts$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.LoadCerts.getMaxResponseSize():int");
        }
    }

    public static class PrepareDataForVTS {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Request.a */
            public static class C0680a extends TAStruct {
                Unsigned8 Mj;
                Blob payment_instrument;

                public C0680a() {
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
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r2.Mj = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Request.a.<init>():void");
                }

                public C0680a(byte[] r3, boolean r4) {
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
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r2.Mj = r0;
                    r0 = r2.payment_instrument;
                    r0.setData(r3);
                    r1 = r2.Mj;
                    if (r4 == 0) goto L_0x0028;
                L_0x0022:
                    r0 = 1;
                L_0x0023:
                    r0 = (short) r0;
                    r1.set(r0);
                    return;
                L_0x0028:
                    r0 = 0;
                    goto L_0x0023;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Request.a.<init>(byte[], boolean):void");
                }
            }

            Request(byte[] r5, boolean r6) {
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
                r0 = new com.visa.tainterface.VisaCommands$PrepareDataForVTS$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 6;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Request.<init>(byte[], boolean):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0681a Mk;

            /* renamed from: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Response.a */
            public static class C0681a extends TAStruct {
                Unsigned8[] error_msg;
                public Blob resp;
                Unsigned32 return_code;

                public C0681a() {
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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.resp = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$PrepareDataForVTS$Response$a;
                r0.<init>();
                r3.Mk = r0;
                r0 = r3.Mk;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$PrepareDataForVTS$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$PrepareDataForVTS$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareDataForVTS.getMaxResponseSize():int");
        }
    }

    public static class PrepareMstData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.PrepareMstData.Request.a */
            public static class C0682a extends TAStruct {
                Blob Ml;
                Blob Mm;
                Blob Mn;
                Blob Mo;
                Blob Mp;
                Blob Mq;
                Blob Mr;
                Blob Ms;
                Blob timestamp;

                public C0682a() {
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
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Ml = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mm = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mn = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.timestamp = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mo = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mp = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mq = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mr = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Ms = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.Request.a.<init>():void");
                }

                public C0682a(byte[] r3, byte[] r4, byte[] r5, byte[] r6, byte[] r7, byte[] r8, byte[] r9, byte[] r10, byte[] r11) {
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
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Ml = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mm = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mn = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.timestamp = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mo = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mp = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mq = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mr = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Ms = r0;
                    r0 = r2.Ml;
                    r0.setData(r3);
                    r0 = r2.Mm;
                    r0.setData(r4);
                    r0 = r2.Mn;
                    r0.setData(r5);
                    r0 = r2.timestamp;
                    r0.setData(r6);
                    r0 = r2.Mo;
                    r0.setData(r7);
                    r0 = r2.Mp;
                    r0.setData(r8);
                    r0 = r2.Mq;
                    r0.setData(r9);
                    r0 = r2.Mr;
                    r0.setData(r10);
                    r0 = r2.Ms;
                    r0.setData(r11);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.Request.a.<init>(byte[], byte[], byte[], byte[], byte[], byte[], byte[], byte[], byte[]):void");
                }
            }

            Request(byte[] r11, byte[] r12, byte[] r13, byte[] r14, byte[] r15, byte[] r16, byte[] r17, byte[] r18, byte[] r19) {
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
                r10 = this;
                r10.<init>();
                r0 = new com.visa.tainterface.VisaCommands$PrepareMstData$Request$a;
                r1 = r11;
                r2 = r12;
                r3 = r13;
                r4 = r14;
                r5 = r15;
                r6 = r16;
                r7 = r17;
                r8 = r18;
                r9 = r19;
                r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 5;
                r10.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.Request.<init>(byte[], byte[], byte[], byte[], byte[], byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0683a Mt;

            /* renamed from: com.visa.tainterface.VisaCommands.PrepareMstData.Response.a */
            public static class C0683a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0683a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$PrepareMstData$Response$a;
                r0.<init>();
                r3.Mt = r0;
                r0 = r3.Mt;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$PrepareMstData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$PrepareMstData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareMstData.getMaxResponseSize():int");
        }
    }

    public static class PrepareSecureDataForVTS {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.PrepareSecureDataForVTS.Request.a */
            public static class C0684a extends TAStruct {
                Blob payment_instrument;

                public C0684a() {
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
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareSecureDataForVTS.Request.a.<init>():void");
                }
            }
        }

        public static class Response extends TACommandResponse {

            /* renamed from: com.visa.tainterface.VisaCommands.PrepareSecureDataForVTS.Response.a */
            public static class C0685a extends TAStruct {
                Unsigned8[] error_msg;
                public Blob resp;
                Unsigned32 return_code;

                public C0685a() {
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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.resp = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareSecureDataForVTS.Response.a.<init>():void");
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
            r0 = new com.visa.tainterface.VisaCommands$PrepareSecureDataForVTS$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareSecureDataForVTS.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$PrepareSecureDataForVTS$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.PrepareSecureDataForVTS.getMaxResponseSize():int");
        }
    }

    public static class RetrieveFromStorage {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Request.a */
            public static class C0686a extends TAStruct {
                Blob Mu;

                public C0686a() {
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
                    r2.Mu = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Request.a.<init>():void");
                }

                public C0686a(byte[] r3) {
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
                    r2.Mu = r0;
                    r0 = r2.Mu;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Request.a.<init>(byte[]):void");
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
                r0 = new com.visa.tainterface.VisaCommands$RetrieveFromStorage$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 16;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0687a Mv;

            /* renamed from: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Response.a */
            public static class C0687a extends TAStruct {
                Blob Mw;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0687a() {
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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mw = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$RetrieveFromStorage$Response$a;
                r0.<init>();
                r3.Mv = r0;
                r0 = r3.Mv;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$RetrieveFromStorage$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$RetrieveFromStorage$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.RetrieveFromStorage.getMaxResponseSize():int");
        }
    }

    public static class StoreData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.StoreData.Request.a */
            public static class C0688a extends TAStruct {
                Blob Mx;

                public C0688a() {
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
                    r2.Mx = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.Request.a.<init>():void");
                }

                public C0688a(byte[] r3) {
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
                    r2.Mx = r0;
                    r0 = r2.Mx;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.Request.a.<init>(byte[]):void");
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
                r0 = new com.visa.tainterface.VisaCommands$StoreData$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 9;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0689a My;

            /* renamed from: com.visa.tainterface.VisaCommands.StoreData.Response.a */
            public static class C0689a extends TAStruct {
                Blob Mz;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0689a() {
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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.Mz = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$StoreData$Response$a;
                r0.<init>();
                r3.My = r0;
                r0 = r3.My;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$StoreData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$StoreData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreData.getMaxResponseSize():int");
        }
    }

    public static class StoreVTSData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.StoreVTSData.Request.a */
            public static class C0690a extends TAStruct {
                Blob MA;

                public C0690a() {
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
                    r2.MA = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.Request.a.<init>():void");
                }

                public C0690a(byte[] r3) {
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
                    r2.MA = r0;
                    r0 = r2.MA;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.Request.a.<init>(byte[]):void");
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
                r0 = new com.visa.tainterface.VisaCommands$StoreVTSData$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 8;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0691a MB;

            /* renamed from: com.visa.tainterface.VisaCommands.StoreVTSData.Response.a */
            public static class C0691a extends TAStruct {
                Blob MC;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0691a() {
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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.MC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$StoreVTSData$Response$a;
                r0.<init>();
                r3.MB = r0;
                r0 = r3.MB;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$StoreVTSData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$StoreVTSData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.StoreVTSData.getMaxResponseSize():int");
        }
    }

    public static class TransmitMstData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.visa.tainterface.VisaCommands.TransmitMstData.Request.a */
            public static class C0692a extends TAStruct {
                Unsigned32 baudRate;
                Blob config;

                public C0692a() {
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
                    r1 = 28;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.config = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.baudRate = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.Request.a.<init>():void");
                }

                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public C0692a(int r5, byte[] r6) {
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
                    r4 = this;
                    r4.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 28;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.config = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r4.baudRate = r0;
                    r0 = r4.config;
                    r0.setData(r6);
                    r0 = r4.baudRate;
                    r2 = (long) r5;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.Request.a.<init>(int, byte[]):void");
                }
            }

            Request(int r5, byte[] r6) {
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
                r0 = new com.visa.tainterface.VisaCommands$TransmitMstData$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.visa.tainterface.VisaCommands.TL_MAGIC_NUM;
                r3 = 2;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.Request.<init>(int, byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0693a MD;

            /* renamed from: com.visa.tainterface.VisaCommands.TransmitMstData.Response.a */
            public static class C0693a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0693a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.Response.a.<init>():void");
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
                r0 = new com.visa.tainterface.VisaCommands$TransmitMstData$Response$a;
                r0.<init>();
                r3.MD = r0;
                r0 = r3.MD;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.visa.tainterface.VisaCommands$TransmitMstData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.getMaxRequestSize():int");
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
            r0 = new com.visa.tainterface.VisaCommands$TransmitMstData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.visa.tainterface.VisaCommands.TransmitMstData.getMaxResponseSize():int");
        }
    }

    static {
        TL_MAGIC_NUM = new byte[]{(byte) 126, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -21, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG};
    }

    public VisaCommands() {
        addCommandInfo(Integer.valueOf(3), new CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(9), new CommandInfo(StoreData.getMaxRequestSize(), StoreData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(8), new CommandInfo(StoreVTSData.getMaxRequestSize(), StoreVTSData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(16), new CommandInfo(RetrieveFromStorage.getMaxRequestSize(), RetrieveFromStorage.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(5), new CommandInfo(PrepareMstData.getMaxRequestSize(), PrepareMstData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(17), new CommandInfo(Generate.getMaxRequestSize(), Generate.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(6), new CommandInfo(PrepareDataForVTS.getMaxRequestSize(), PrepareDataForVTS.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(7), new CommandInfo(PrepareSecureDataForVTS.getMaxRequestSize(), PrepareSecureDataForVTS.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(20), new CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(21), new CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(2), new CommandInfo(TransmitMstData.getMaxRequestSize(), TransmitMstData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(22), new CommandInfo(ClearMstData.getMaxRequestSize(), ClearMstData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(23), new CommandInfo(GenInAppPaymentInfo.getMaxRequestSize(), GenInAppPaymentInfo.getMaxResponseSize()));
    }
}
