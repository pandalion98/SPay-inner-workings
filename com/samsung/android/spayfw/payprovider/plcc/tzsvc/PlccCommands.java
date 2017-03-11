package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

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

public class PlccCommands extends TACommands {
    public static final int CMD_PLCC_MST_TRANSMIT_RAW = 1879048193;
    public static final int CMD_PLCC_PAY_ADD_CARD = 4;
    public static final int CMD_PLCC_PAY_AUTHENTICATE_TRANSACTION = 8;
    public static final int CMD_PLCC_PAY_CLEAR_DATA = 9;
    public static final int CMD_PLCC_PAY_GET_NONCE = 7;
    public static final int CMD_PLCC_PAY_LOAD_CERT = 3;
    public static final int CMD_PLCC_PAY_MST_TRANSMIT = 5;
    public static final int CMD_PLCC_PAY_UTILITY_ENC4SERVER_TRANSPORT = 6;
    public static final int CMD_PLCC_RETRIEVE_DATA_FROM_STORAGE = 11;
    public static final int CMD_PLCC_STORE_DATA = 10;
    public static final int CMD_VAS_PAY_GET_GIFT_CARD_DETAIL = 16;
    public static final int CMD_VAS_PAY_GET_LOYALTY_CARD_DETAIL = 17;
    public static final int CONFIG_MAX_LEN = 1024;
    private static final boolean DEBUG;
    public static final int PLCC_PAY_AC_TOKEN_MAX_LEN = 1024;
    public static final int PLCC_PAY_IN_DATA_MAX_LEN = 4096;
    public static final int PLCC_PAY_MAX_BUF_SIZE = 4096;
    private static final int PLCC_PAY_MAX_CERT_DATA_LEN = 4096;
    public static final int PLCC_PAY_MAX_ERROR_STR_LEN = 256;
    private static final int PLCC_PAY_OUT_DATA_MAX_LEN = 1024;
    static final int PLCC_PAY_TIMESTAMP_LEN = 7;
    private static final String TAG = "PlccCommands";
    public static final byte[] TL_MAGIC_NUM;
    public static final int TL_VERSION = 1;
    public static final int TRACK1_MAX_LEN = 80;
    public static final int TRACK2_MAX_LEN = 40;
    public static final int VAS_PAY_SMALL_BLOB_LEN = 1024;
    public static final int ZAP_TRACK_LUMP_MAX_LEN = 1024;

    public static class AddCard {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob buf_plcc_data;

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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_plcc_data = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.Request.Data.<init>():void");
                }

                public Data(byte[] r3) {
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
                    r2.buf_plcc_data = r0;
                    r0 = r2.buf_plcc_data;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.Request.Data.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AddCard$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 4;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob encPlccData;
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.encPlccData = r0;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AddCard$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AddCard$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AddCard$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard.getMaxResponseSize():int");
        }
    }

    public static class AuthenticateTransaction {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob auth_result_sec_obj;

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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.auth_result_sec_obj = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.Request.Data.<init>():void");
                }

                public Data(byte[] r3) {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.Request.Data.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AuthenticateTransaction$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 8;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 auth_result;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AuthenticateTransaction$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AuthenticateTransaction$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$AuthenticateTransaction$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction.getMaxResponseSize():int");
        }
    }

    public static class ClearData {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Unsigned32 unused_uint;

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
                    r1.unused_uint = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.Request.Data.<init>():void");
                }

                public Data(int r5) {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.Request.Data.<init>(int):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ClearData$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 9;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.Request.<init>(int):void");
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ClearData$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ClearData$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ClearData$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData.getMaxResponseSize():int");
        }
    }

    public static class ExtractGiftCardDetail {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob buf_card_encdata;
                Blob buf_card_id;

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
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_id = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_encdata = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.Request.Data.<init>():void");
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
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_id = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_encdata = r0;
                    r0 = r2.buf_card_id;
                    r0.setData(r3);
                    r0 = r2.buf_card_encdata;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.Request.Data.<init>(byte[], byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractGiftCardDetail$Request$Data;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 16;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob barcodecontent;
                Blob cardnumber;
                Unsigned8[] error_msg;
                Blob extra;
                Blob pin;
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
                    r3.extra = r0;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractGiftCardDetail$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public ExtractGiftCardDetail() {
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
            r0 = this;
            r0.<init>();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.<init>():void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractGiftCardDetail$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractGiftCardDetail$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail.getMaxResponseSize():int");
        }
    }

    public static class ExtractLoyaltyCardDetail {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob buf_card_encdata;
                Blob buf_card_id;

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
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_id = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_encdata = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.Request.Data.<init>():void");
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
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_id = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_card_encdata = r0;
                    r0 = r2.buf_card_id;
                    r0.setData(r3);
                    r0 = r2.buf_card_encdata;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.Request.Data.<init>(byte[], byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractLoyaltyCardDetail$Request$Data;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 17;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob barcodecontent;
                Blob cardnumber;
                Unsigned8[] error_msg;
                Blob extra;
                Blob imgSessionKey;
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
                    r2 = this;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.cardnumber = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.imgSessionKey = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.barcodecontent = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.extra = r0;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractLoyaltyCardDetail$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractLoyaltyCardDetail$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$ExtractLoyaltyCardDetail$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail.getMaxResponseSize():int");
        }
    }

    public static class GetNonce {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Unsigned32 _nonceSize;

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
                    r1._nonceSize = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.Request.Data.<init>():void");
                }

                public Data(int r5) {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.Request.Data.<init>(int):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$GetNonce$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 7;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.Request.<init>(int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned8[] error_msg;
                public Blob out_data;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$GetNonce$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public GetNonce() {
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
            r0 = this;
            r0.<init>();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.<init>():void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$GetNonce$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$GetNonce$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce.getMaxResponseSize():int");
        }
    }

    public static class LoadCerts {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob device_cert_enc;
                Blob device_cert_sign;
                Blob server_cert_enc;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Request.Data.<init>():void");
                }

                public Data(byte[] r3, byte[] r4, byte[] r5, byte[] r6, byte[] r7) {
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Request.Data.<init>(byte[], byte[], byte[], byte[], byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$LoadCerts$Request$Data;
                r1 = r7;
                r2 = r8;
                r3 = r9;
                r4 = r10;
                r5 = r11;
                r0.<init>(r1, r2, r3, r4, r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 3;
                r6.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Request.<init>(byte[], byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob cert_drk;
                Blob cert_encrypt;
                Blob cert_sign;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$LoadCerts$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$LoadCerts$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$LoadCerts$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.getMaxResponseSize():int");
        }
    }

    public static class MstTransmit {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob ac;
                Unsigned32 baudrate;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.Request.Data.<init>():void");
                }

                public Data(int r5, byte[] r6, byte[] r7) {
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
                    r4.baudrate = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.ac = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.config = r0;
                    r0 = r4.baudrate;
                    r2 = (long) r5;
                    r0.set(r2);
                    r0 = r4.ac;
                    r0.setData(r6);
                    r0 = r4.config;
                    r0.setData(r7);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.Request.Data.<init>(int, byte[], byte[]):void");
                }
            }

            Request(int r5, byte[] r6, byte[] r7) {
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmit$Request$Data;
                r0.<init>(r5, r6, r7);
                r0 = r0.serialize();
                r1 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.DEBUG;
                if (r1 == 0) goto L_0x002b;
            L_0x0012:
                r1 = "PlccCommands";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r3 = "Request.length = ";
                r2 = r2.append(r3);
                r3 = r0.length;
                r2 = r2.append(r3);
                r2 = r2.toString();
                com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
            L_0x002b:
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 5;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.Request.<init>(int, byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob error;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmit$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmit$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmit$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit.getMaxResponseSize():int");
        }
    }

    public static class MstTransmitSequence {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Unsigned32 baudrate;
                Blob config;
                Blob track1;
                Blob track2;

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
                    r2.baudrate = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 80;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.track1 = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 40;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.track2 = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.config = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.Request.Data.<init>():void");
                }

                public Data(int r5, byte[] r6, byte[] r7, byte[] r8) {
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
                    r4.baudrate = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 80;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.track1 = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 40;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.track2 = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r4.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r4.config = r0;
                    r0 = r4.baudrate;
                    r2 = (long) r5;
                    r0.set(r2);
                    r0 = r4.track1;
                    r0.setData(r6);
                    r0 = r4.track2;
                    r0.setData(r7);
                    r0 = r4.config;
                    r0.setData(r8);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.Request.Data.<init>(int, byte[], byte[], byte[]):void");
                }
            }

            Request(int r5, byte[] r6, byte[] r7, byte[] r8) {
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmitSequence$Request$Data;
                r0.<init>(r5, r6, r7, r8);
                r0 = r0.serialize();
                r1 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.DEBUG;
                if (r1 == 0) goto L_0x002b;
            L_0x0012:
                r1 = "PlccCommands";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r3 = "Request.length = ";
                r2 = r2.append(r3);
                r3 = r0.length;
                r2 = r2.append(r3);
                r2 = r2.toString();
                com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
            L_0x002b:
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 1879048193; // 0x70000001 float:1.5845634E29 double:9.28373159E-315;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.Request.<init>(int, byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob error;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmitSequence$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmitSequence$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$MstTransmitSequence$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmitSequence.getMaxResponseSize():int");
        }
    }

    public static class RetrieveFromStorage {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob buf_plcc_data;

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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_plcc_data = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.Request.Data.<init>():void");
                }

                public Data(byte[] r3) {
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
                    r2.buf_plcc_data = r0;
                    r0 = r2.buf_plcc_data;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.Request.Data.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$RetrieveFromStorage$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 11;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob buf_plcc_data;
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_plcc_data = r0;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$RetrieveFromStorage$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$RetrieveFromStorage$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$RetrieveFromStorage$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage.getMaxResponseSize():int");
        }
    }

    public static class StoreData {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob buf_plcc_data;

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
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_plcc_data = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.Request.Data.<init>():void");
                }

                public Data(byte[] r3) {
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
                    r2.buf_plcc_data = r0;
                    r0 = r2.buf_plcc_data;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.Request.Data.<init>(byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$StoreData$Request$Data;
                r0.<init>(r5);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 10;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob buf_plcc_data;
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
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.buf_plcc_data = r0;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$StoreData$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$StoreData$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$StoreData$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData.getMaxResponseSize():int");
        }
    }

    public static class Utility_enc4Server_Transport {

        public static class Request extends TACommandRequest {

            public static class Data extends TAStruct {
                Blob payment_instrument;
                Blob timestamp;

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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.Request.Data.<init>():void");
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.Request.Data.<init>(byte[], byte[]):void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$Utility_enc4Server_Transport$Request$Data;
                r0.<init>(r5, r6);
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.TL_MAGIC_NUM;
                r3 = 6;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned8[] error_msg;
                public Blob resp;
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
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$Utility_enc4Server_Transport$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public Utility_enc4Server_Transport() {
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
            r0 = this;
            r0.<init>();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.<init>():void");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$Utility_enc4Server_Transport$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands$Utility_enc4Server_Transport$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport.getMaxResponseSize():int");
        }
    }

    static {
        DEBUG = TAController.DEBUG;
        TL_MAGIC_NUM = new byte[]{(byte) 126, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -21, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG};
    }

    public static int getVersion() {
        return TL_VERSION;
    }

    public static byte[] getMagicNumber() {
        return TL_MAGIC_NUM;
    }

    public PlccCommands() {
        addCommandInfo(Integer.valueOf(CMD_PLCC_PAY_LOAD_CERT), new CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_PLCC_PAY_ADD_CARD), new CommandInfo(AddCard.getMaxRequestSize(), AddCard.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_PLCC_MST_TRANSMIT_RAW), new CommandInfo(MstTransmitSequence.getMaxRequestSize(), MstTransmitSequence.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_PLCC_PAY_MST_TRANSMIT), new CommandInfo(MstTransmit.getMaxRequestSize(), MstTransmit.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_PLCC_PAY_UTILITY_ENC4SERVER_TRANSPORT), new CommandInfo(Utility_enc4Server_Transport.getMaxRequestSize(), Utility_enc4Server_Transport.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(PLCC_PAY_TIMESTAMP_LEN), new CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_PLCC_PAY_AUTHENTICATE_TRANSACTION), new CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_PLCC_PAY_CLEAR_DATA), new CommandInfo(ClearData.getMaxRequestSize(), ClearData.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_VAS_PAY_GET_GIFT_CARD_DETAIL), new CommandInfo(ExtractGiftCardDetail.getMaxRequestSize(), ExtractGiftCardDetail.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(CMD_VAS_PAY_GET_LOYALTY_CARD_DETAIL), new CommandInfo(ExtractLoyaltyCardDetail.getMaxRequestSize(), ExtractLoyaltyCardDetail.getMaxResponseSize()));
    }
}
