package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

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

public class KrccCommands extends TACommands {
    public static final int CMD_KRCC_PAY_CLEAR_MST_DATA = 5;
    public static final int CMD_KRCC_PAY_LOAD_CERT = 2;
    public static final int CMD_KRCC_PAY_PREPARE_MST_DATA = 3;
    public static final int CMD_KRCC_PAY_TRANSMIT_MST_DATA = 4;
    private static final int KRCC_PAY_MAX_CERT_DATA_LEN = 4096;
    public static final int KRCC_PAY_MAX_ERROR_STR_LEN = 256;
    public static final int KRCC_PAY_MAX_MST_TRACK_LEN = 128;
    public static final int KRCC_PAY_MAX_TRANSMIT_MST_CONFIG_LEN = 16;
    public static final int KRCC_PAY_SIGNATURE_LEN = 256;
    public static final byte[] TL_MAGIC_NUM;
    public static final int TL_VERSION = 1;

    public static class ClearMstData {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Unsigned32 unused_uint;

                public Data() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.Request.Data.<init>():void");
                }

                public Data(int r5) {
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
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r4.unused_uint = r0;
                    r0 = r4.unused_uint;
                    r2 = (long) r5;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.Request.Data.<init>(int):void");
                }
            }

            Request(int r5) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$ClearMstData$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TL_MAGIC_NUM;
                r3 = 5;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.Request.<init>(int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public Data() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.Response.Data.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$ClearMstData$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public ClearMstData() {
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
            r0 = this;
            r0.<init>();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.<init>():void");
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$ClearMstData$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$ClearMstData$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.ClearMstData.getMaxResponseSize():int");
        }
    }

    public static class LoadCert {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob server_cert_sign;
                Blob server_cert_sub;

                public Data() {
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
                    r2.server_cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_sub = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Request.Data.<init>():void");
                }

                public Data(byte[] r3, byte[] r4) {
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
                    r2.server_cert_sign = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.server_cert_sub = r0;
                    r0 = r2.server_cert_sign;
                    r0.setData(r3);
                    r0 = r2.server_cert_sub;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Request.Data.<init>(byte[], byte[]):void");
                }
            }

            Request(byte[] r5, byte[] r6) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$LoadCert$Request$Data;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TL_MAGIC_NUM;
                r3 = 2;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public Data() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Response.Data.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r4) {
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
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$LoadCert$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public LoadCert() {
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
            r0 = this;
            r0.<init>();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.<init>():void");
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$LoadCert$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.getMaxRequestSize():int");
        }

        public static int getMaxResponseSize() {
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$LoadCert$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.LoadCert.getMaxResponseSize():int");
        }
    }

    public static class PrepareMstData {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob signedTrackHash;
                Blob track;

                public Data() {
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
                    r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.track = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.signedTrackHash = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.Request.Data.<init>():void");
                }

                public Data(byte[] r3, byte[] r4) {
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
                    r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.track = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.signedTrackHash = r0;
                    r0 = r2.track;
                    r0.setData(r3);
                    r0 = r2.signedTrackHash;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.Request.Data.<init>(byte[], byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$PrepareMstData$Request$Data;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TL_MAGIC_NUM;
                r3 = 3;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public Data() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$PrepareMstData$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$PrepareMstData$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$PrepareMstData$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.PrepareMstData.getMaxResponseSize():int");
        }
    }

    public static class TransmitMstData {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Unsigned32 baudRate;
                Blob config;

                public Data() {
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
                    r2.baudRate = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 16;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.config = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.Request.Data.<init>():void");
                }

                public Data(int r5, byte[] r6) {
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
                    r4.baudRate = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 16;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.config = r0;
                    r0 = r4.baudRate;
                    r2 = (long) r5;
                    r0.set(r2);
                    r0 = r4.config;
                    r0.setData(r6);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.Request.Data.<init>(int, byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$TransmitMstData$Request$Data;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TL_MAGIC_NUM;
                r3 = 4;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.Request.<init>(int, byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned8[] error_msg;
                Unsigned32 return_code;

                public Data() {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$TransmitMstData$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$TransmitMstData$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands$TransmitMstData$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands.TransmitMstData.getMaxResponseSize():int");
        }
    }

    static {
        TL_MAGIC_NUM = new byte[]{(byte) 126, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -21, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG};
    }

    public static int getVersion() {
        return TL_VERSION;
    }

    public static byte[] getMagicNumber() {
        return TL_MAGIC_NUM;
    }

    public KrccCommands() {
        addCommandInfo(Integer.valueOf(CMD_KRCC_PAY_LOAD_CERT), new CommandInfo(LoadCert.getMaxRequestSize(), LoadCert.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_KRCC_PAY_TRANSMIT_MST_DATA), new CommandInfo(TransmitMstData.getMaxRequestSize(), TransmitMstData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_KRCC_PAY_PREPARE_MST_DATA), new CommandInfo(PrepareMstData.getMaxRequestSize(), PrepareMstData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_KRCC_PAY_CLEAR_MST_DATA), new CommandInfo(ClearMstData.getMaxRequestSize(), ClearMstData.getMaxResponseSize()));
    }
}
