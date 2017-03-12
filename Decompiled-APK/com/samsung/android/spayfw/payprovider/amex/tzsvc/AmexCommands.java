package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.KeyValPairs;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TACommands.CommandInfo;
import com.samsung.android.spaytzsvc.api.TAStruct;
import java.util.List;
import java.util.Map;
import javolution.io.Struct.Unsigned32;
import javolution.io.Struct.Unsigned8;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;

public class AmexCommands extends SPayTAAuthCommands {

    public static class ActivateToken {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Request.a */
            public static class C0418a extends TAStruct {
                Blob pC;

                public C0418a() {
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
                    r2.pC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Request.a.<init>():void");
                }

                public C0418a(byte[] r3) {
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
                    r2.pC = r0;
                    r0 = r2.pC;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Request.a.<init>(byte[]):void");
                }
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ActivateToken$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = null;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 7;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0419a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Response.a */
            public static class C0419a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pC;
                Unsigned32 return_code;

                public C0419a() {
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
                    r2.pC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ActivateToken$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken.Response.<init>(android.spay.TACommandResponse):void");
            }
        }
    }

    public static class ClearLUPC {

        public static class Request extends TACommandRequest {
            Request() {
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
                r0 = 0;
                r1 = com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TL_MAGIC_NUM;
                r2 = 13;
                r3 = 0;
                r4.init(r0, r1, r2, r3);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ClearLUPC.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0420a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ClearLUPC.Response.a */
            public static class C0420a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0420a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ClearLUPC.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ClearLUPC$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ClearLUPC.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
            return 0;
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ClearLUPC$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ClearLUPC.getMaxResponseSize():int");
        }
    }

    public static class DecryptTokenData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Request.a */
            public static class C0421a extends TAStruct {
                Blob pE;

                public C0421a() {
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
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pE = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Request.a.<init>():void");
                }

                public C0421a(byte[] r3) {
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
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pE = r0;
                    r0 = r2.pE;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Request.a.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$DecryptTokenData$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 6;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0422a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Response.a */
            public static class C0422a extends TAStruct {
                Blob blob;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0422a() {
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
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.blob = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$DecryptTokenData$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$DecryptTokenData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$DecryptTokenData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData.getMaxResponseSize():int");
        }
    }

    public static class GenerateECDHKeys {

        public static class Request extends TACommandRequest {
            Request() {
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
                r0 = 0;
                r1 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r2 = 2;
                r3 = 0;
                r4.init(r0, r1, r2, r3);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateECDHKeys.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0423a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateECDHKeys.Response.a */
            public static class C0423a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pF;
                Blob pG;
                Unsigned32 return_code;

                public C0423a() {
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
                    r2 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r3.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r3.return_code = r0;
                    r0 = new javolution.io.Struct.Unsigned8[r2];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pF = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pG = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateECDHKeys.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$GenerateECDHKeys$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateECDHKeys.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
            return 0;
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$GenerateECDHKeys$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateECDHKeys.getMaxResponseSize():int");
        }
    }

    public static class GenerateInAppJwePaymentPayload {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateInAppJwePaymentPayload.Request.a */
            public static class C0424a extends TAStruct {
                Blob pH;
                C0449b pI;
                KeyValPairs pJ;

                public C0424a() {
                    this.pH = (Blob) inner(new Blob(8));
                    this.pI = (C0449b) inner(new C0449b(3));
                    this.pJ = (KeyValPairs) inner(new KeyValPairs(10, 64, SkeinMac.SKEIN_256));
                }

                public C0424a(byte[] bArr, Map<byte[], byte[]> map, List<byte[]> list) {
                    this.pH = (Blob) inner(new Blob(8));
                    this.pI = (C0449b) inner(new C0449b(3));
                    this.pJ = (KeyValPairs) inner(new KeyValPairs(10, 64, SkeinMac.SKEIN_256));
                    this.pH.setData(bArr);
                    this.pJ.setData(map);
                    this.pI.setData(list);
                }
            }

            Request(byte[] bArr, Map<byte[], byte[]> map, List<byte[]> list) {
                init(0, TACommands.TL_MAGIC_NUM, 15, new C0424a(bArr, map, list).serialize());
            }
        }

        public static class Response extends TACommandResponse {
            public C0425a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateInAppJwePaymentPayload.Response.a */
            public static class C0425a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pK;
                Blob pL;
                Unsigned32 return_code;

                public C0425a() {
                    this.return_code = new Unsigned32();
                    this.error_msg = (Unsigned8[]) array(new Unsigned8[SkeinMac.SKEIN_256]);
                    this.pK = (Blob) inner(new Blob(5120));
                    this.pL = (Blob) inner(new Blob(64));
                }
            }

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal = new C0425a();
                this.mRetVal.deserialize(this.mResponse);
            }
        }
    }

    public static class GenerateInAppPaymentPayload {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateInAppPaymentPayload.Request.a */
            public static class C0426a extends TAStruct {
                Blob nonce;
                Blob pH;
                KeyValPairs pJ;

                public C0426a() {
                    this.pH = (Blob) inner(new Blob(8));
                    this.pJ = (KeyValPairs) inner(new KeyValPairs(10, 64, SkeinMac.SKEIN_256));
                    this.nonce = (Blob) inner(new Blob(32));
                }

                public C0426a(byte[] bArr, Map<byte[], byte[]> map, byte[] bArr2) {
                    this.pH = (Blob) inner(new Blob(8));
                    this.pJ = (KeyValPairs) inner(new KeyValPairs(10, 64, SkeinMac.SKEIN_256));
                    this.nonce = (Blob) inner(new Blob(32));
                    this.pH.setData(bArr);
                    this.pJ.setData(map);
                    this.nonce.setData(bArr2);
                }
            }

            Request(byte[] bArr, Map<byte[], byte[]> map, byte[] bArr2) {
                init(0, TACommands.TL_MAGIC_NUM, 14, new C0426a(bArr, map, bArr2).serialize());
            }
        }

        public static class Response extends TACommandResponse {
            public C0427a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateInAppPaymentPayload.Response.a */
            public static class C0427a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pK;
                Blob pL;
                Unsigned32 return_code;

                public C0427a() {
                    this.return_code = new Unsigned32();
                    this.error_msg = (Unsigned8[]) array(new Unsigned8[SkeinMac.SKEIN_256]);
                    this.pK = (Blob) inner(new Blob(5120));
                    this.pL = (Blob) inner(new Blob(64));
                }
            }

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal = new C0427a();
                this.mRetVal.deserialize(this.mResponse);
            }
        }
    }

    public static class GetNFCCryptogram {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Request.a */
            public static class C0428a extends TAStruct {
                Unsigned8 pM;
                Unsigned8 pN;
                KeyValPairs pO;

                public C0428a() {
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
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r4.pM = r0;
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r4.pN = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.KeyValPairs;
                    r1 = 10;
                    r2 = 64;
                    r3 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1, r2, r3);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.KeyValPairs) r0;
                    r4.pO = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Request.a.<init>():void");
                }

                public C0428a(int r5, int r6, java.util.Map<byte[], byte[]> r7) {
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
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r4.pM = r0;
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r4.pN = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.KeyValPairs;
                    r1 = 10;
                    r2 = 64;
                    r3 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1, r2, r3);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.KeyValPairs) r0;
                    r4.pO = r0;
                    r0 = r4.pM;
                    r1 = (short) r5;
                    r0.set(r1);
                    r0 = r4.pN;
                    r1 = (short) r6;
                    r0.set(r1);
                    r0 = r4.pO;
                    r0.setData(r7);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Request.a.<init>(int, int, java.util.Map):void");
                }
            }

            Request(int r5, int r6, java.util.Map<byte[], byte[]> r7) {
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$GetNFCCryptogram$Request$a;
                r0.<init>(r5, r6, r7);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 11;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Request.<init>(int, int, java.util.Map):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0429a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Response.a */
            public static class C0429a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pP;
                Unsigned32 return_code;

                public C0429a() {
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
                    r1 = 8;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pP = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$GetNFCCryptogram$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public static int getMaxRequestSize() {
            /* JADX: method processing error */
/*
            Error: java.lang.NullPointerException
	at jadx.core.dex.instructions.mods.ConstructorInsn.<init>(ConstructorInsn.java:45)
	at jadx.core.dex.visitors.ModVisitor.processInvoke(ModVisitor.java:181)
	at jadx.core.dex.visitors.ModVisitor.replaceStep(ModVisitor.java:71)
	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:56)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$GetNFCCryptogram$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$GetNFCCryptogram$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram.getMaxResponseSize():int");
        }
    }

    public static class LoadCerts {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Request.a */
            public static class C0430a extends TAStruct {
                Blob deviceSignRSA2048PrivCert;
                Blob pQ;

                public C0430a(byte[] r3, byte[] r4) {
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
                    r2.deviceSignRSA2048PrivCert = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pQ = r0;
                    r0 = r2.deviceSignRSA2048PrivCert;
                    r0.setData(r3);
                    r0 = r2.pQ;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Request.a.<init>(byte[], byte[]):void");
                }

                public C0430a() {
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
                    r2.deviceSignRSA2048PrivCert = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pQ = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Request.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$LoadCerts$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 1;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0431a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Response.a */
            public static class C0431a extends TAStruct {
                Blob deviceRootRSA2048PubCert;
                Blob deviceSignRSA2048PubCert;
                Unsigned8[] error_msg;
                Blob pR;
                Unsigned32 return_code;

                public C0431a() {
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
                    r2.deviceRootRSA2048PubCert = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.deviceSignRSA2048PubCert = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pR = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$LoadCerts$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$LoadCerts$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.getMaxRequestSize():int");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$LoadCerts$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.getMaxResponseSize():int");
        }
    }

    public static class ProcessData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessData.Request.a */
            public static class C0432a extends TAStruct {
                Blob data;
                Unsigned8 pS;

                public C0432a() {
                    this.pS = new Unsigned8();
                    this.data = (Blob) inner(new Blob(PKIFailureInfo.certRevoked));
                }

                public C0432a(byte[] bArr, boolean z) {
                    this.pS = new Unsigned8();
                    this.data = (Blob) inner(new Blob(PKIFailureInfo.certRevoked));
                    this.data.setData(bArr);
                    this.pS.set((short) (z ? 1 : 0));
                }
            }

            Request(byte[] bArr, boolean z) {
                init(0, TACommands.TL_MAGIC_NUM, 61441, new C0432a(bArr, z).serialize());
            }
        }

        public static class Response extends TACommandResponse {
            public C0433a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessData.Response.a */
            public static class C0433a extends TAStruct {
                Blob data;
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0433a() {
                    this.return_code = new Unsigned32();
                    this.error_msg = (Unsigned8[]) array(new Unsigned8[SkeinMac.SKEIN_256]);
                    this.data = (Blob) inner(new Blob(PKIFailureInfo.certRevoked));
                }
            }

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal = new C0433a();
                this.mRetVal.deserialize(this.mResponse);
            }
        }
    }

    public static class ProcessRequestData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Request.a */
            public static class C0434a extends TAStruct {
                C0449b pT;
                Blob pU;
                Blob pV;

                public C0434a() {
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
                    r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
                    r3.<init>();
                    r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$b;
                    r1 = 3;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.C0449b) r0;
                    r3.pT = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pU = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pV = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Request.a.<init>():void");
                }

                public C0434a(java.util.List<byte[]> r4, byte[] r5, byte[] r6) {
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
                    r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
                    r1 = 3;
                    r3.<init>();
                    r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$b;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.C0449b) r0;
                    r3.pT = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pU = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pV = r0;
                    r0 = r4.size();
                    if (r0 <= r1) goto L_0x0042;
                L_0x0033:
                    r0 = "AmexCommands";
                    r1 = "TSP Encryption Certificate Chain is longer than maximum allowed (3)";
                    com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
                    r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
                    r1 = 983040; // 0xf0000 float:1.377532E-39 double:4.856863E-318;
                    r0.<init>(r1);
                    throw r0;
                L_0x0042:
                    r0 = r3.pT;
                    r0.setData(r4);
                    r0 = r3.pU;
                    r0.setData(r5);
                    r0 = r3.pV;
                    r0.setData(r6);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Request.a.<init>(java.util.List, byte[], byte[]):void");
                }
            }

            Request(java.util.List<byte[]> r5, byte[] r6, byte[] r7) {
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessRequestData$Request$a;
                r0.<init>(r5, r6, r7);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 3;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Request.<init>(java.util.List, byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0435a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Response.a */
            public static class C0435a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pW;
                Blob pX;
                Blob pY;
                Unsigned32 return_code;

                public C0435a() {
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
                    r2 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r3.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r3.return_code = r0;
                    r0 = new javolution.io.Struct.Unsigned8[r2];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pW = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pX = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pY = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessRequestData$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessRequestData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessRequestData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData.getMaxResponseSize():int");
        }
    }

    public static class ProcessResponseData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessResponseData.Request.a */
            public static class C0436a extends TAStruct {
                Blob pZ;
                Blob qa;

                public C0436a() {
                    this.pZ = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.qa = (Blob) inner(new Blob(SkeinMac.SKEIN_512));
                }

                public C0436a(byte[] bArr, byte[] bArr2) {
                    this.pZ = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                    this.qa = (Blob) inner(new Blob(SkeinMac.SKEIN_512));
                    this.pZ.setData(bArr);
                    this.qa.setData(bArr2);
                }
            }

            Request(byte[] bArr, byte[] bArr2) {
                init(0, TACommands.TL_MAGIC_NUM, 4, new C0436a(bArr, bArr2).serialize());
            }
        }

        public static class Response extends TACommandResponse {
            public C0437a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessResponseData.Response.a */
            public static class C0437a extends TAStruct {
                Unsigned8[] error_msg;
                Blob qb;
                Unsigned32 return_code;

                public C0437a() {
                    this.return_code = new Unsigned32();
                    this.error_msg = (Unsigned8[]) array(new Unsigned8[SkeinMac.SKEIN_256]);
                    this.qb = (Blob) inner(new Blob(PKIFailureInfo.certConfirmed));
                }
            }

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal = new C0437a();
                this.mRetVal.deserialize(this.mResponse);
            }
        }
    }

    public static class ProcessTokenData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Request.a */
            public static class C0438a extends TAStruct {
                Blob pG;
                C0449b qc;
                Unsigned8[] qd;
                Blob qe;
                Blob qf;
                Blob qg;
                Blob qh;

                public C0438a() {
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
                    r2 = 2;
                    r3.<init>();
                    r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$b;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.C0449b) r0;
                    r3.qc = r0;
                    r0 = 16;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.qd = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qe = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qf = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 64;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qh = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pG = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Request.a.<init>():void");
                }

                public C0438a(java.util.List<byte[]> r4, byte[] r5, byte[] r6, byte[] r7, byte[] r8, byte[] r9, byte[] r10) {
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
                    r2 = 2;
                    r3.<init>();
                    r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$b;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.C0449b) r0;
                    r3.qc = r0;
                    r0 = 16;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.qd = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qe = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qf = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 64;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qh = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pG = r0;
                    r0 = r4.size();
                    if (r0 <= r2) goto L_0x007b;
                L_0x006c:
                    r0 = "AmexCommands";
                    r1 = "TSP ECC Certificate Chain is longer than maximum allowed (2)";
                    com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
                    r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
                    r1 = 983040; // 0xf0000 float:1.377532E-39 double:4.856863E-318;
                    r0.<init>(r1);
                    throw r0;
                L_0x007b:
                    r0 = r3.qc;
                    r0.setData(r4);
                    r0 = r3.qd;
                    com.samsung.android.spayfw.payprovider.amex.tzsvc.Utils.setUnsigned8ArrayData(r0, r5);
                    r0 = r3.qe;
                    r0.setData(r6);
                    r0 = r3.qf;
                    r0.setData(r7);
                    r0 = r3.qg;
                    r0.setData(r8);
                    r0 = r3.qh;
                    r0.setData(r9);
                    r0 = r3.pG;
                    r0.setData(r10);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Request.a.<init>(java.util.List, byte[], byte[], byte[], byte[], byte[], byte[]):void");
                }
            }

            Request(java.util.List<byte[]> r9, byte[] r10, byte[] r11, byte[] r12, byte[] r13, byte[] r14, byte[] r15) {
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
                r8 = this;
                r8.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTokenData$Request$a;
                r1 = r9;
                r2 = r10;
                r3 = r11;
                r4 = r12;
                r5 = r13;
                r6 = r14;
                r7 = r15;
                r0.<init>(r1, r2, r3, r4, r5, r6, r7);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 5;
                r8.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Request.<init>(java.util.List, byte[], byte[], byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0439a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Response.a */
            public static class C0439a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pE;
                Blob qi;
                Blob qj;
                Blob qk;
                Blob ql;
                Blob qm;
                Unsigned32 return_code;

                public C0439a() {
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
                    r2 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r3.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r3.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pE = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qi = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qj = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qk = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.ql = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qm = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTokenData$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTokenData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTokenData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData.getMaxResponseSize():int");
        }
    }

    public static class ProcessTransaction {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Request.a */
            public static class C0440a extends TAStruct {
                Blob pC;
                Blob pE;
                Unsigned8 pM;
                Blob qi;
                Blob qj;
                Blob qn;

                public C0440a() {
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
                    r2 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r3.<init>();
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r3.pM = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pE = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qi = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qj = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pC = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qn = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Request.a.<init>():void");
                }

                public C0440a(int r4, byte[] r5, byte[] r6, byte[] r7, byte[] r8, byte[] r9) {
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
                    r2 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r3.<init>();
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r3.pM = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pE = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qi = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qj = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.pC = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qn = r0;
                    r0 = r3.pM;
                    r1 = (short) r4;
                    r0.set(r1);
                    r0 = r3.pE;
                    r0.setData(r5);
                    r0 = r3.qi;
                    r0.setData(r6);
                    r0 = r3.qj;
                    r0.setData(r7);
                    r0 = r3.pC;
                    r0.setData(r8);
                    r0 = r3.qn;
                    r0.setData(r9);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Request.a.<init>(int, byte[], byte[], byte[], byte[], byte[]):void");
                }
            }

            Request(int r8, byte[] r9, byte[] r10, byte[] r11, byte[] r12, byte[] r13) {
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
                r7 = this;
                r7.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTransaction$Request$a;
                r1 = r8;
                r2 = r9;
                r3 = r10;
                r4 = r11;
                r5 = r12;
                r6 = r13;
                r0.<init>(r1, r2, r3, r4, r5, r6);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 10;
                r7.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Request.<init>(int, byte[], byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0441a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Response.a */
            public static class C0441a extends TAStruct {
                Unsigned8[] error_msg;
                Blob qi;
                Blob qj;
                Blob qn;
                Blob qo;
                Blob qp;
                Unsigned32 return_code;

                public C0441a() {
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
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r3.return_code = r0;
                    r0 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0 = new javolution.io.Struct.Unsigned8[r0];
                    r0 = r3.array(r0);
                    r0 = (javolution.io.Struct.Unsigned8[]) r0;
                    r3.error_msg = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qi = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qj = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r2);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qn = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 5;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qo = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 64;
                    r0.<init>(r1);
                    r0 = r3.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r3.qp = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTransaction$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTransaction$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ProcessTransaction$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction.getMaxResponseSize():int");
        }
    }

    public static class ResumeToken {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Request.a */
            public static class C0442a extends TAStruct {
                Blob pC;

                public C0442a() {
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
                    r2.pC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Request.a.<init>():void");
                }

                public C0442a(byte[] r3) {
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
                    r2.pC = r0;
                    r0 = r2.pC;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Request.a.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ResumeToken$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 9;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0443a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Response.a */
            public static class C0443a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pC;
                Unsigned32 return_code;

                public C0443a() {
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
                    r2.pC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ResumeToken$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ResumeToken$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$ResumeToken$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken.getMaxResponseSize():int");
        }
    }

    public static class SuspendToken {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Request.a */
            public static class C0444a extends TAStruct {
                Blob pC;

                public C0444a() {
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
                    r2.pC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Request.a.<init>():void");
                }

                public C0444a(byte[] r3) {
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
                    r2.pC = r0;
                    r0 = r2.pC;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Request.a.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$SuspendToken$Request$a;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 8;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0445a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Response.a */
            public static class C0445a extends TAStruct {
                Unsigned8[] error_msg;
                Blob pC;
                Unsigned32 return_code;

                public C0445a() {
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
                    r2.pC = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$SuspendToken$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$SuspendToken$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$SuspendToken$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken.getMaxResponseSize():int");
        }
    }

    public static class TransmitMstData {

        public static class Request extends TACommandRequest {

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Request.a */
            public static class C0446a extends TAStruct {
                Unsigned32 baudRate;
                Blob config;

                public C0446a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Request.a.<init>():void");
                }

                public C0446a(int r5, byte[] r6) {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Request.a.<init>(int, byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$TransmitMstData$Request$a;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 0;
                r2 = com.samsung.android.spaytzsvc.api.TACommands.TL_MAGIC_NUM;
                r3 = 12;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Request.<init>(int, byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public C0447a mRetVal;

            /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Response.a */
            public static class C0447a extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public C0447a() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Response.a.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$TransmitMstData$Response$a;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$TransmitMstData$Request$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands$TransmitMstData$Response$a;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData.getMaxResponseSize():int");
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.a */
    public static class C0448a extends Blob {
        public C0448a() {
            super(PKIFailureInfo.certConfirmed);
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.b */
    public static class C0449b extends TAStruct {
        Unsigned32 numCerts;
        C0448a[] pD;

        public C0449b(int i) {
            this.numCerts = new Unsigned32();
            this.pD = new C0448a[i];
            for (int i2 = 0; i2 < i; i2++) {
                this.pD[i2] = (C0448a) inner(new C0448a());
            }
        }

        public void setData(List<byte[]> list) {
            if (list == null || list.size() <= this.pD.length) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        this.pD[i].setData((byte[]) list.get(i));
                    }
                }
                if (list != null) {
                    this.numCerts.set((long) list.size());
                    return;
                }
                return;
            }
            Log.m286e("AmexCommands", "Error: Can set Cert Chain more than the size of the destination Cert Chain");
        }
    }

    public AmexCommands() {
        addCommandInfo(Integer.valueOf(1), new CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(2), new CommandInfo(GenerateECDHKeys.getMaxRequestSize(), GenerateECDHKeys.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(3), new CommandInfo(ProcessRequestData.getMaxRequestSize(), ProcessRequestData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(5), new CommandInfo(ProcessTokenData.getMaxRequestSize(), ProcessTokenData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(6), new CommandInfo(DecryptTokenData.getMaxRequestSize(), DecryptTokenData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(8), new CommandInfo(SuspendToken.getMaxRequestSize(), SuspendToken.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(9), new CommandInfo(ResumeToken.getMaxRequestSize(), ResumeToken.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(10), new CommandInfo(ProcessTransaction.getMaxRequestSize(), ProcessTransaction.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(11), new CommandInfo(GetNFCCryptogram.getMaxRequestSize(), GetNFCCryptogram.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(12), new CommandInfo(TransmitMstData.getMaxRequestSize(), TransmitMstData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(13), new CommandInfo(ClearLUPC.getMaxRequestSize(), ClearLUPC.getMaxResponseSize()));
    }
}
