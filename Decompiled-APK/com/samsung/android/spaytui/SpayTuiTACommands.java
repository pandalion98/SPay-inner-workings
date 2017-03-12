package com.samsung.android.spaytui;

import android.os.Build;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TACommands.CommandInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct.Unsigned32;
import javolution.io.Struct.Unsigned8;

public class SpayTuiTACommands extends TACommands {
    private static final int APP_NAME_BUFFER_SIZE = 128;
    private static final int AUTH_SO_BUFFER_SIZE = 1024;
    private static final int FP_ID_LEN = 1024;
    private static final int IRIS_ID_LEN = 1024;
    private static final int NONCE_BUFFER_SIZE = 64;
    private static final int PIN_SIZE = 16;
    private static final int PIN_SO_SIZE;
    private static final int PIN_SO_SIZE_NO_IRIS = 1266;
    private static final int PIN_SO_SIZE_WITH_IRIS = 2297;
    public static final int RESOURCE_BUFFER_SIZE = 256000;
    public static final int SPAY_AUTH_CMD_CHECK_WARRANTY_BIT = 524288;
    public static final int SPAY_AUTH_CMD_GET_AUTH_RESULT = 524290;
    public static final int SPAY_AUTH_CMD_GET_NONCE = 524289;
    public static final int SPAY_AUTH_CMD_INAPP_CONFIRM = 589824;
    public static final int SPAY_AUTH_CMD_MERCHANT_SECURE_DISPLAY = 589825;
    public static final int SPAY_AUTH_CMD_MERCHANT_SECURE_INIT = 589827;
    public static final int SPAY_AUTH_CMD_MERCHANT_SECURE_TOUCH = 589826;
    public static final int SPAY_AUTH_CMD_MERCHANT_SEND_SECURE_IMG = 589828;
    public static final int SPAY_TUI_CMD_CHECK_RET_CODE = 327681;
    public static final int SPAY_TUI_CMD_CHECK_TUI_SESSION = 327680;
    public static final int SPAY_TUI_CMD_CLEAR_STATE = 131089;
    public static final int SPAY_TUI_CMD_CLOSE = 131088;
    public static final int SPAY_TUI_CMD_GENERATE_RANDOM = 262147;
    public static final int SPAY_TUI_CMD_GET_PIN = 262144;
    public static final int SPAY_TUI_CMD_GET_RANDOM = 262146;
    public static final int SPAY_TUI_CMD_GET_SECURE_RESULT = 262145;
    public static final int SPAY_TUI_CMD_LOAD_PIN = 65536;
    public static final int SPAY_TUI_CMD_PRELOAD_FP_SECURE_RESULT = 262148;
    public static final int SPAY_TUI_CMD_QSEE_START_SECURE_UI = 590224;
    public static final int SPAY_TUI_CMD_RESUME = 131074;
    public static final int SPAY_TUI_CMD_SETUP = 131072;
    public static final int SPAY_TUI_CMD_SETUP_BIO = 131076;
    public static final int SPAY_TUI_CMD_SET_ACTION_BAR_TEXT = 65538;
    public static final int SPAY_TUI_CMD_SET_CANCEL_IMAGE = 196611;
    public static final int SPAY_TUI_CMD_SET_IMAGE = 196610;
    public static final int SPAY_TUI_CMD_SET_MODE_TEXT = 65537;
    public static final int SPAY_TUI_CMD_SET_PIN_BOX = 196608;
    public static final int SPAY_TUI_CMD_SET_PROMPT = 196609;
    public static final int SPAY_TUI_CMD_SET_RTL = 196624;
    public static final int SPAY_TUI_CMD_UPDATE_FP = 131075;
    public static final int SPAY_TUI_CMD_VERIFY = 131073;
    private static final int SPAY_TUI_RANDOM_NUMBER_SIZE = 24;
    private static final int SPAY_VERIFY_PIN_SO_SIZE = 436;
    public static final byte[] TL_MAGIC_NUM;
    public static final int TL_VERSION = 1;

    public static class CheckRetCode {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckRetCode$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 327681; // 0x50001 float:4.59179E-40 double:1.61896E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckRetCode.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckRetCode.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckRetCode$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckRetCode.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckRetCode$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckRetCode.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckRetCode$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckRetCode.getMaxResponseSize():int");
        }
    }

    public static class CheckTUISession {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckTUISession$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 327680; // 0x50000 float:4.59177E-40 double:1.618954E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckTUISession.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckTUISession.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckTUISession$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckTUISession.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckTUISession$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckTUISession.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CheckTUISession$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CheckTUISession.getMaxResponseSize():int");
        }
    }

    public static class ClearState {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$ClearState$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131089; // 0x20011 float:1.83695E-40 double:6.47666E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.ClearState.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.ClearState.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$ClearState$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.ClearState.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$ClearState$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.ClearState.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$ClearState$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.ClearState.getMaxResponseSize():int");
        }
    }

    public static class CloseTui {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob placeholder;

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
                    r1 = 1;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.placeholder = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CloseTui.Request.Data.<init>():void");
                }
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CloseTui$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131088; // 0x20010 float:1.83693E-40 double:6.4766E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CloseTui.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CloseTui.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CloseTui$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CloseTui.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CloseTui$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CloseTui.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$CloseTui$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.CloseTui.getMaxResponseSize():int");
        }
    }

    public static class GenerateRandom {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GenerateRandom$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 262147; // 0x40003 float:3.67346E-40 double:1.29518E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GenerateRandom.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            private static final String TAG = "SpayTuiTACommands";
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 existPinLen;
                Blob pinSo;
                Unsigned32 retCode;

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
                    r2.retCode = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pinSo = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.existPinLen = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GenerateRandom.Response.Data.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r5) {
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
                r0 = r5.mResponseCode;
                r1 = r5.mErrorMsg;
                r2 = r5.mResponse;
                r4.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GenerateRandom$Response$Data;
                r0.<init>();
                r4.mRetVal = r0;
                r0 = r4.mRetVal;
                r1 = r4.mResponse;
                r0.deserialize(r1);
                r0 = r4.mRetVal;
                r0 = r0.retCode;
                r0 = r0.get();
                r2 = 0;
                r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
                if (r0 != 0) goto L_0x0031;
            L_0x0025:
                r0 = com.samsung.android.spaytzsvc.api.TAController.DEBUG;
                if (r0 == 0) goto L_0x0030;
            L_0x0029:
                r0 = "SpayTuiTACommands";
                r1 = "Generate PIN random Successful";
                com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
            L_0x0030:
                return;
            L_0x0031:
                r0 = com.samsung.android.spaytzsvc.api.TAController.DEBUG;
                if (r0 == 0) goto L_0x0030;
            L_0x0035:
                r0 = "SpayTuiTACommands";
                r1 = new java.lang.StringBuilder;
                r1.<init>();
                r2 = "failed to generate PIN random, ret = ";
                r1 = r1.append(r2);
                r2 = r4.mRetVal;
                r2 = r2.retCode;
                r2 = r2.get();
                r1 = r1.append(r2);
                r1 = r1.toString();
                com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
                goto L_0x0030;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GenerateRandom.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GenerateRandom$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GenerateRandom.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GenerateRandom$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GenerateRandom.getMaxResponseSize():int");
        }
    }

    public static class GetAuthResult {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob app_name;
                Blob nonce;
                Blob so;

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
                    r1 = 64;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.nonce = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.app_name = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.Request.Data.<init>():void");
                }

                public void setData(byte[] r2, byte[] r3, byte[] r4) {
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
                    r0 = r1.nonce;
                    r0.setData(r2);
                    r0 = r1.app_name;
                    r0.setData(r3);
                    r0 = r1.so;
                    r0.setData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.Request.Data.setData(byte[], byte[], byte[]):void");
                }
            }

            Request(byte[] r5, java.lang.String r6, byte[] r7) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetAuthResult$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r1 = r6.getBytes();
                r0.setData(r5, r1, r7);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 524290; // 0x80002 float:7.34687E-40 double:2.590337E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.Request.<init>(byte[], java.lang.String, byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 authType;
                Unsigned32 existPinLen;
                Blob pin_so;
                Unsigned32 retCode;
                Blob so;
                Unsigned8 update_pin_so;

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
                    r2.retCode = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.authType = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 436; // 0x1b4 float:6.11E-43 double:2.154E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.so = r0;
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r2.update_pin_so = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pin_so = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.existPinLen = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetAuthResult$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetAuthResult$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetAuthResult$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetAuthResult.getMaxResponseSize():int");
        }
    }

    public static class GetNonce {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Unsigned32 nonce_len;
                Blob placeholder;

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
                    r2.nonce_len = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 1;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.placeholder = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.Request.Data.<init>():void");
                }

                public void setData(int r5) {
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
                    r0 = r4.nonce_len;
                    r2 = (long) r5;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.Request.Data.setData(int):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetNonce$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 524289; // 0x80001 float:7.34685E-40 double:2.59033E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.Request.<init>(int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Blob nonce;
                Unsigned32 retCode;

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
                    r2.retCode = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 64;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.nonce = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetNonce$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetNonce$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetNonce$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetNonce.getMaxResponseSize():int");
        }
    }

    public static class GetPinRandom {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob app_name;

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
                    r2.app_name = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
                    r0 = r1.app_name;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.Request.Data.setData(byte[]):void");
                }
            }

            Request(java.lang.String r5) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinRandom$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r1 = r5.getBytes();
                r0.setData(r1);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 262146; // 0x40002 float:3.67345E-40 double:1.295173E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.Request.<init>(java.lang.String):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 authType;
                Blob result_so;
                Unsigned32 retCode;

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
                    r2.retCode = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.authType = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 436; // 0x1b4 float:6.11E-43 double:2.154E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.result_so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinRandom$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinRandom$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinRandom$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinRandom.getMaxResponseSize():int");
        }
    }

    public static class GetPinSO {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob placeholder;

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
                    r1 = 1;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.placeholder = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO.Request.Data.<init>():void");
                }
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinSO$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            private static final String TAG = "SpayTuiTACommands";
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 existPinLen;
                Blob pinSo;
                Unsigned32 retCode;

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
                    r2.retCode = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pinSo = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.existPinLen = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO.Response.Data.<init>():void");
                }
            }

            public Response(android.spay.TACommandResponse r5) {
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
                r0 = r5.mResponseCode;
                r1 = r5.mErrorMsg;
                r2 = r5.mResponse;
                r4.<init>(r0, r1, r2);
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinSO$Response$Data;
                r0.<init>();
                r4.mRetVal = r0;
                r0 = r4.mRetVal;
                r1 = r4.mResponse;
                r0.deserialize(r1);
                r0 = "SpayTuiTACommands";
                r1 = new java.lang.StringBuilder;
                r1.<init>();
                r2 = "PIN_SO_SIZE ";
                r1 = r1.append(r2);
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                r1 = r1.append(r2);
                r1 = r1.toString();
                com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
                r0 = r4.mRetVal;
                r0 = r0.retCode;
                r0 = r0.get();
                r2 = 0;
                r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
                if (r0 != 0) goto L_0x004d;
            L_0x0041:
                r0 = com.samsung.android.spaytzsvc.api.TAController.DEBUG;
                if (r0 == 0) goto L_0x004c;
            L_0x0045:
                r0 = "SpayTuiTACommands";
                r1 = "Get Pin So Successful";
                com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
            L_0x004c:
                return;
            L_0x004d:
                r0 = com.samsung.android.spaytzsvc.api.TAController.DEBUG;
                if (r0 == 0) goto L_0x004c;
            L_0x0051:
                r0 = "SpayTuiTACommands";
                r1 = new java.lang.StringBuilder;
                r1.<init>();
                r2 = "failed to get PIN SO, ret = ";
                r1 = r1.append(r2);
                r2 = r4.mRetVal;
                r2 = r2.retCode;
                r2 = r2.get();
                r1 = r1.append(r2);
                r1 = r1.toString();
                com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
                goto L_0x004c;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinSO$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetPinSO$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetPinSO.getMaxResponseSize():int");
        }
    }

    public static class GetSecureResult {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob app_name;
                Blob nonce;

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
                    r1 = 64;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.nonce = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.app_name = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.Request.Data.<init>():void");
                }

                public void setData(byte[] r2, byte[] r3) {
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
                    r0 = r1.nonce;
                    r0.setData(r2);
                    r0 = r1.app_name;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.Request.Data.setData(byte[], byte[]):void");
                }
            }

            Request(byte[] r5, java.lang.String r6) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetSecureResult$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r1 = r6.getBytes();
                r0.setData(r5, r1);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 262145; // 0x40001 float:3.67343E-40 double:1.29517E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.Request.<init>(byte[], java.lang.String):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 authType;
                Blob result_so;
                Unsigned32 retCode;

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
                    r2.retCode = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.authType = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 436; // 0x1b4 float:6.11E-43 double:2.154E-321;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.result_so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetSecureResult$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetSecureResult$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$GetSecureResult$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.GetSecureResult.getMaxResponseSize():int");
        }
    }

    public static class InAppConfirm {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob img;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.img = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.Request.Data.<init>():void");
                }

                public void setData(byte[] r7, byte[] r8, byte[] r9, byte[] r10, byte[] r11, byte[] r12, byte[] r13) {
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
                    r6 = this;
                    r5 = 16;
                    r0 = 0;
                    r1 = r7.length;
                    r2 = r8.length;
                    r1 = r1 + r2;
                    r2 = r9.length;
                    r1 = r1 + r2;
                    r2 = r10.length;
                    r1 = r1 + r2;
                    r2 = r11.length;
                    r1 = r1 + r2;
                    r2 = r12.length;
                    r1 = r1 + r2;
                    r2 = r13.length;
                    r1 = r1 + r2;
                    r1 = r1 + 16;
                    r2 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    if (r1 <= r2) goto L_0x0017;
                L_0x0017:
                    r1 = new byte[r5];
                    r2 = r7.length;
                    r2 = r2 >> 8;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r1[r0] = r2;
                    r2 = 1;
                    r3 = r7.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 2;
                    r3 = r8.length;
                    r3 = r3 >> 8;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 3;
                    r3 = r8.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 4;
                    r3 = r9.length;
                    r3 = r3 >> 8;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 5;
                    r3 = r9.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 6;
                    r3 = r10.length;
                    r3 = r3 >> 8;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 7;
                    r3 = r10.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 8;
                    r3 = r11.length;
                    r3 = r3 >> 8;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 9;
                    r3 = r11.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 10;
                    r3 = r12.length;
                    r3 = r3 >> 16;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 11;
                    r3 = r12.length;
                    r3 = r3 >> 8;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 12;
                    r3 = r12.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 13;
                    r3 = r13.length;
                    r3 = r3 >> 16;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 14;
                    r3 = r13.length;
                    r3 = r3 >> 8;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                    r2 = 15;
                    r3 = r13.length;
                    r3 = r3 & 255;
                    r3 = (byte) r3;
                    r1[r2] = r3;
                L_0x00a2:
                    if (r0 >= r5) goto L_0x00cb;
                L_0x00a4:
                    r2 = "SpayTuiTACommands";
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r4 = "length[";
                    r3 = r3.append(r4);
                    r3 = r3.append(r0);
                    r4 = "]=";
                    r3 = r3.append(r4);
                    r4 = r1[r0];
                    r3 = r3.append(r4);
                    r3 = r3.toString();
                    com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);
                    r0 = r0 + 1;
                    goto L_0x00a2;
                L_0x00cb:
                    r0 = r6.img;
                    r0.setData(r1);
                    r0 = r6.img;
                    r0.addData(r7);
                    r0 = r6.img;
                    r0.addData(r8);
                    r0 = r6.img;
                    r0.addData(r9);
                    r0 = r6.img;
                    r0.addData(r10);
                    r0 = r6.img;
                    r0.addData(r11);
                    r0 = r6.img;
                    r0.addData(r12);
                    r0 = r6.img;
                    r0.addData(r13);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.Request.Data.setData(byte[], byte[], byte[], byte[], byte[], byte[], byte[]):void");
                }
            }

            Request(byte[] r9, byte[] r10, byte[] r11, byte[] r12, byte[] r13, byte[] r14, byte[] r15) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$InAppConfirm$Request$Data;
                r0.<init>();
                r8.mData = r0;
                r0 = r8.mData;
                r1 = r9;
                r2 = r10;
                r3 = r11;
                r4 = r12;
                r5 = r13;
                r6 = r14;
                r7 = r15;
                r0.setData(r1, r2, r3, r4, r5, r6, r7);
                r0 = r8.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 589824; // 0x90000 float:8.2652E-40 double:2.91412E-318;
                r8.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.Request.<init>(byte[], byte[], byte[], byte[], byte[], byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$InAppConfirm$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$InAppConfirm$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$InAppConfirm$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.InAppConfirm.getMaxResponseSize():int");
        }
    }

    public static class LoadPin {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob pin_so;

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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pin_so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r1 = this;
                    r0 = r1.pin_so;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Request.Data.setData(byte[]):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$LoadPin$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 existPinLen;
                Unsigned32 retCode;
                Blob updated_pin_so;

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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r2 = this;
                    r2.<init>();
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.retCode = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.existPinLen = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.updated_pin_so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$LoadPin$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.Response.<init>(android.spay.TACommandResponse):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$LoadPin$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.getMaxRequestSize():int");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$LoadPin$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.LoadPin.getMaxResponseSize():int");
        }
    }

    public static class MerchantSecureDisplay {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob img;

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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r2 = this;
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.img = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.Request.Data.<init>():void");
                }

                public void setData(byte[] r4) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r3 = this;
                    r0 = r4.length;
                    r0 = r0 + 3;
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    if (r0 <= r1) goto L_0x0008;
                L_0x0008:
                    r0 = 3;
                    r0 = new byte[r0];
                    r1 = 0;
                    r2 = r4.length;
                    r2 = r2 >> 16;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = 1;
                    r2 = r4.length;
                    r2 = r2 >> 8;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = 2;
                    r2 = r4.length;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = r3.img;
                    r1.setData(r0);
                    r0 = r3.img;
                    r0.addData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.Request.Data.setData(byte[]):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureDisplay$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 589825; // 0x90001 float:8.26521E-40 double:2.914123E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureDisplay$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.Response.<init>(android.spay.TACommandResponse):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureDisplay$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.getMaxRequestSize():int");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureDisplay$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureDisplay.getMaxResponseSize():int");
        }
    }

    public static class MerchantSecureInit {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob img;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.img = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.Request.Data.<init>():void");
                }

                public void setData(byte[] r4) {
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
                    r0 = r4.length;
                    r0 = r0 + 3;
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    if (r0 <= r1) goto L_0x0008;
                L_0x0008:
                    r0 = 3;
                    r0 = new byte[r0];
                    r1 = 0;
                    r2 = r4.length;
                    r2 = r2 >> 16;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = 1;
                    r2 = r4.length;
                    r2 = r2 >> 8;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = 2;
                    r2 = r4.length;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = r3.img;
                    r1.setData(r0);
                    r0 = r3.img;
                    r0.addData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.Request.Data.setData(byte[]):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureInit$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 589827; // 0x90003 float:8.26524E-40 double:2.914133E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureInit$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureInit$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureInit$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureInit.getMaxResponseSize():int");
        }
    }

    public static class MerchantSecureTouch {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureTouch$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 589826; // 0x90002 float:8.26522E-40 double:2.91413E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureTouch.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureTouch.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureTouch$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureTouch.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureTouch$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureTouch.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSecureTouch$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSecureTouch.getMaxResponseSize():int");
        }
    }

    public static class MerchantSendSecureImg {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob img;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.img = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.Request.Data.<init>():void");
                }

                public void setData(byte[] r4) {
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
                    r0 = r4.length;
                    r0 = r0 + 3;
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    if (r0 <= r1) goto L_0x0008;
                L_0x0008:
                    r0 = 3;
                    r0 = new byte[r0];
                    r1 = 0;
                    r2 = r4.length;
                    r2 = r2 >> 16;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = 1;
                    r2 = r4.length;
                    r2 = r2 >> 8;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = 2;
                    r2 = r4.length;
                    r2 = r2 & 255;
                    r2 = (byte) r2;
                    r0[r1] = r2;
                    r1 = r3.img;
                    r1.setData(r0);
                    r0 = r3.img;
                    r0.addData(r4);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.Request.Data.setData(byte[]):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSendSecureImg$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 589828; // 0x90004 float:8.26525E-40 double:2.914138E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSendSecureImg$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public MerchantSendSecureImg() {
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
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.<init>():void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSendSecureImg$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$MerchantSendSecureImg$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.MerchantSendSecureImg.getMaxResponseSize():int");
        }
    }

    public static class PreloadFpSecureResult {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob secure_object;

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
                    r2.secure_object = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
                    r0 = r1.secure_object;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.Request.Data.setData(byte[]):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$PreloadFpSecureResult$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 262148; // 0x40004 float:3.67348E-40 double:1.295183E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$PreloadFpSecureResult$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$PreloadFpSecureResult$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$PreloadFpSecureResult$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.PreloadFpSecureResult.getMaxResponseSize():int");
        }
    }

    public static class Resume {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Unsigned8 update_display_only;

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
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r1.update_display_only = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.Request.Data.<init>():void");
                }

                public void setData(boolean r3) {
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
                    if (r3 == 0) goto L_0x0009;
                L_0x0002:
                    r0 = r2.update_display_only;
                    r1 = 1;
                    r0.set(r1);
                L_0x0008:
                    return;
                L_0x0009:
                    r0 = r2.update_display_only;
                    r1 = 0;
                    r0.set(r1);
                    goto L_0x0008;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.Request.Data.setData(boolean):void");
                }
            }

            Request(boolean r5) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Resume$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131074; // 0x20002 float:1.83674E-40 double:6.4759E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.Request.<init>(boolean):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Resume$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Resume$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Resume$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Resume.getMaxResponseSize():int");
        }
    }

    public static class SetActionBarText {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob image;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.image = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
                    r0 = r1.image;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.Request.Data.setData(byte[]):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetActionBarText$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 65538; // 0x10002 float:9.1838E-41 double:3.238E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetActionBarText$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetActionBarText$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetActionBarText$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetActionBarText.getMaxResponseSize():int");
        }
    }

    public static class SetBackgroundImg {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob image;
                Unsigned32 f16x;
                Unsigned32 f17y;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.image = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.f16x = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.f17y = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.Request.Data.<init>():void");
                }

                public void setData(byte[] r5, int r6, int r7) {
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
                    r0 = r4.image;
                    r0.setData(r5);
                    r0 = r4.f16x;
                    r2 = (long) r6;
                    r0.set(r2);
                    r0 = r4.f17y;
                    r2 = (long) r7;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.Request.Data.setData(byte[], int, int):void");
                }
            }

            Request(byte[] r5, int r6, int r7) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetBackgroundImg$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5, r6, r7);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 196610; // 0x30002 float:2.75509E-40 double:9.7138E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.Request.<init>(byte[], int, int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetBackgroundImg$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetBackgroundImg$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetBackgroundImg$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetBackgroundImg.getMaxResponseSize():int");
        }
    }

    public static class SetCancelBtn {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob images;
                Unsigned32 pressed_len;
                Unsigned32 unpressed_len;
                Unsigned32 f18x;
                Unsigned32 f19y;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.images = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.unpressed_len = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.pressed_len = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.f18x = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.f19y = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.Request.Data.<init>():void");
                }

                public void setData(byte[] r6, byte[] r7, int r8, int r9) {
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
                    r5 = this;
                    r2 = 0;
                    r0 = r5.images;
                    r0.setData(r6);
                    r0 = r5.images;
                    r0.addData(r7);
                    r4 = r5.unpressed_len;
                    if (r6 != 0) goto L_0x0028;
                L_0x0010:
                    r0 = r2;
                L_0x0011:
                    r4.set(r0);
                    r0 = r5.pressed_len;
                    if (r7 != 0) goto L_0x002b;
                L_0x0018:
                    r0.set(r2);
                    r0 = r5.f18x;
                    r2 = (long) r8;
                    r0.set(r2);
                    r0 = r5.f19y;
                    r2 = (long) r9;
                    r0.set(r2);
                    return;
                L_0x0028:
                    r0 = r6.length;
                    r0 = (long) r0;
                    goto L_0x0011;
                L_0x002b:
                    r1 = r7.length;
                    r2 = (long) r1;
                    goto L_0x0018;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.Request.Data.setData(byte[], byte[], int, int):void");
                }
            }

            Request(byte[] r5, byte[] r6, int r7, int r8) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetCancelBtn$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5, r6, r7, r8);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 196611; // 0x30003 float:2.7551E-40 double:9.71387E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.Request.<init>(byte[], byte[], int, int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetCancelBtn$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetCancelBtn$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetCancelBtn$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetCancelBtn.getMaxResponseSize():int");
        }
    }

    public static class SetPinBox {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Unsigned32 error_pin_len;
                Blob images;
                Unsigned32 normal_pin_len;
                Unsigned8 number;
                Unsigned32 pinbox_height;
                Unsigned32 pinbox_width;
                Unsigned32 space;
                Unsigned32 f20y;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.images = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.normal_pin_len = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.error_pin_len = r0;
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r2.number = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.f20y = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.space = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.pinbox_width = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.pinbox_height = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.Request.Data.<init>():void");
                }

                public void setData(byte[] r6, byte[] r7, int r8, int r9, int r10, int r11, int r12) {
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
                    r5 = this;
                    r2 = 0;
                    r0 = r5.images;
                    r0.setData(r6);
                    r0 = r5.images;
                    r0.addData(r7);
                    r4 = r5.normal_pin_len;
                    if (r6 != 0) goto L_0x003a;
                L_0x0010:
                    r0 = r2;
                L_0x0011:
                    r4.set(r0);
                    r0 = r5.error_pin_len;
                    if (r7 != 0) goto L_0x003d;
                L_0x0018:
                    r0.set(r2);
                    r0 = r5.number;
                    r1 = (short) r8;
                    r0.set(r1);
                    r0 = r5.f20y;
                    r2 = (long) r9;
                    r0.set(r2);
                    r0 = r5.space;
                    r2 = (long) r10;
                    r0.set(r2);
                    r0 = r5.pinbox_width;
                    r2 = (long) r11;
                    r0.set(r2);
                    r0 = r5.pinbox_height;
                    r2 = (long) r12;
                    r0.set(r2);
                    return;
                L_0x003a:
                    r0 = r6.length;
                    r0 = (long) r0;
                    goto L_0x0011;
                L_0x003d:
                    r1 = r7.length;
                    r2 = (long) r1;
                    goto L_0x0018;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.Request.Data.setData(byte[], byte[], int, int, int, int, int):void");
                }
            }

            Request(byte[] r9, byte[] r10, int r11, int r12, int r13, int r14, int r15) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPinBox$Request$Data;
                r0.<init>();
                r8.mData = r0;
                r0 = r8.mData;
                r1 = r9;
                r2 = r10;
                r3 = r11;
                r4 = r12;
                r5 = r13;
                r6 = r14;
                r7 = r15;
                r0.setData(r1, r2, r3, r4, r5, r6, r7);
                r0 = r8.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 196608; // 0x30000 float:2.75506E-40 double:9.71373E-319;
                r8.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.Request.<init>(byte[], byte[], int, int, int, int, int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPinBox$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPinBox$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPinBox$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPinBox.getMaxResponseSize():int");
        }
    }

    public static class SetPrompt {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Unsigned32 above;
                Unsigned32 dis_to_pin;
                Blob image;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.image = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.above = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.dis_to_pin = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.Request.Data.<init>():void");
                }

                public void setData(byte[] r5, int r6, int r7) {
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
                    r0 = r4.image;
                    r0.setData(r5);
                    r0 = r4.above;
                    r2 = (long) r6;
                    r0.set(r2);
                    r0 = r4.dis_to_pin;
                    r2 = (long) r7;
                    r0.set(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.Request.Data.setData(byte[], int, int):void");
                }
            }

            Request(byte[] r5, int r6, int r7) {
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPrompt$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5, r6, r7);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 196609; // 0x30001 float:2.75508E-40 double:9.71378E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.Request.<init>(byte[], int, int):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPrompt$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPrompt$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetPrompt$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetPrompt.getMaxResponseSize():int");
        }
    }

    public static class SetRtl {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob msg;

                public Data() {
                    /* JADX: method processing error */
/*
                    Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
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
                    r2.<init>();
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = 2;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.msg = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.Request.Data.<init>():void");
                }

                public void setData(boolean r4) {
                    /* JADX: method processing error */
/*
                    Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
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
                    r3 = this;
                    r0 = 1;
                    r1 = 0;
                    r2 = new byte[r0];
                    if (r4 == 0) goto L_0x000e;
                L_0x0006:
                    r2[r1] = r0;
                    r0 = r3.msg;
                    r0.setData(r2);
                    return;
                L_0x000e:
                    r0 = r1;
                    goto L_0x0006;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.Request.Data.setData(boolean):void");
                }
            }

            Request(boolean r5) {
                /* JADX: method processing error */
/*
                Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetRtl$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 196624; // 0x30010 float:2.75529E-40 double:9.7145E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.Request.<init>(boolean):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetRtl$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public SetRtl() {
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
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.<init>():void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetRtl$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetRtl$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetRtl.getMaxResponseSize():int");
        }
    }

    public static class SetSecureModeText {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob image;

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
                    r1 = 256000; // 0x3e800 float:3.58732E-40 double:1.26481E-318;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.image = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
                    r0 = r1.image;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.Request.Data.setData(byte[]):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetSecureModeText$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 65537; // 0x10001 float:9.1837E-41 double:3.23796E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetSecureModeText$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.Response.<init>(android.spay.TACommandResponse):void");
            }
        }

        public SetSecureModeText() {
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
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.<init>():void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetSecureModeText$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetSecureModeText$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetSecureModeText.getMaxResponseSize():int");
        }
    }

    public static class SetupBio {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob fp_so;
                Blob iris_so;

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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
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
                    r2.fp_so = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.iris_so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.Request.Data.<init>():void");
                }

                public void setData(byte[] r2, byte[] r3) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r1 = this;
                    r0 = r1.fp_so;
                    r0.setData(r2);
                    r0 = r1.iris_so;
                    r0.setData(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.Request.Data.setData(byte[], byte[]):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupBio$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5, r6);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131076; // 0x20004 float:1.83677E-40 double:6.476E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.Request.<init>(byte[], byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupBio$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.Response.<init>(android.spay.TACommandResponse):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupBio$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.getMaxRequestSize():int");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupBio$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupBio.getMaxResponseSize():int");
        }
    }

    public static class SetupPin {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob so;

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
                    r2.so = r0;
                    r0 = r2.so;
                    r1 = 0;
                    r0.setData(r1);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
                    r0 = r1.so;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.Request.Data.setData(byte[]):void");
                }
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupPin$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.Request.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupPin$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupPin$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupPin$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$SetupPin$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.SetupPin.getMaxResponseSize():int");
        }
    }

    public static class StartSecureUI {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
            }

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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$StartSecureUI$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 590224; // 0x90190 float:8.2708E-40 double:2.916094E-318;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.StartSecureUI.Request.<init>():void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.StartSecureUI.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$StartSecureUI$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.StartSecureUI.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$StartSecureUI$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.StartSecureUI.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$StartSecureUI$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.StartSecureUI.getMaxResponseSize():int");
        }
    }

    public static class UpdateFP {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Blob so;

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
                    r2.so = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.Request.Data.<init>():void");
                }

                public void setData(byte[] r2) {
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
                    r0 = r1.so;
                    r0.setData(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.Request.Data.setData(byte[]):void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$UpdateFP$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131075; // 0x20003 float:1.83675E-40 double:6.47597E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.Request.<init>(byte[]):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 existPinLen;
                Blob pin_so;
                Unsigned32 retCode;
                Unsigned8 update_pin_so;

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
                    r2.retCode = r0;
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r2.update_pin_so = r0;
                    r0 = new com.samsung.android.spaytzsvc.api.Blob;
                    r1 = com.samsung.android.spaytui.SpayTuiTACommands.PIN_SO_SIZE;
                    r0.<init>(r1);
                    r0 = r2.inner(r0);
                    r0 = (com.samsung.android.spaytzsvc.api.Blob) r0;
                    r2.pin_so = r0;
                    r0 = new javolution.io.Struct$Unsigned32;
                    r0.<init>();
                    r2.existPinLen = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$UpdateFP$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.Response.<init>(android.spay.TACommandResponse):void");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$UpdateFP$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.getMaxRequestSize():int");
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
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$UpdateFP$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.UpdateFP.getMaxResponseSize():int");
        }
    }

    public static class Verify {

        public static class Request extends TACommandRequest {
            Data mData;

            public static class Data extends TAStruct {
                Unsigned8 close_tui_session;
                Unsigned8 update_display_only;

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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r1 = this;
                    r1.<init>();
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r1.close_tui_session = r0;
                    r0 = new javolution.io.Struct$Unsigned8;
                    r0.<init>();
                    r1.update_display_only = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.Request.Data.<init>():void");
                }

                public void setData(boolean r4, boolean r5) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r3 = this;
                    r2 = 1;
                    r1 = 0;
                    if (r4 == 0) goto L_0x0011;
                L_0x0004:
                    r0 = r3.close_tui_session;
                    r0.set(r2);
                L_0x0009:
                    if (r5 == 0) goto L_0x0017;
                L_0x000b:
                    r0 = r3.update_display_only;
                    r0.set(r2);
                L_0x0010:
                    return;
                L_0x0011:
                    r0 = r3.close_tui_session;
                    r0.set(r1);
                    goto L_0x0009;
                L_0x0017:
                    r0 = r3.update_display_only;
                    r0.set(r1);
                    goto L_0x0010;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.Request.Data.setData(boolean, boolean):void");
                }
            }

            Request(boolean r5, boolean r6) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                /*
                r4 = this;
                r4.<init>();
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Verify$Request$Data;
                r0.<init>();
                r4.mData = r0;
                r0 = r4.mData;
                r0.setData(r5, r6);
                r0 = r4.mData;
                r0 = r0.serialize();
                r1 = 1;
                r2 = com.samsung.android.spaytui.SpayTuiTACommands.TL_MAGIC_NUM;
                r3 = 131073; // 0x20001 float:1.83672E-40 double:6.47587E-319;
                r4.init(r1, r2, r3, r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.Request.<init>(boolean, boolean):void");
            }
        }

        public static class Response extends TACommandResponse {
            public Data mRetVal;

            public static class Data extends TAStruct {
                Unsigned32 retCode;

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
                    r1.retCode = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.Response.Data.<init>():void");
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
                r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Verify$Response$Data;
                r0.<init>();
                r3.mRetVal = r0;
                r0 = r3.mRetVal;
                r1 = r3.mResponse;
                r0.deserialize(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.Response.<init>(android.spay.TACommandResponse):void");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Verify$Request$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.getMaxRequestSize():int");
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r0 = new com.samsung.android.spaytui.SpayTuiTACommands$Verify$Response$Data;
            r0.<init>();
            r0 = r0.size();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytui.SpayTuiTACommands.Verify.getMaxResponseSize():int");
        }
    }

    static {
        TL_MAGIC_NUM = new byte[]{(byte) 126, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -21, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG};
        PIN_SO_SIZE = Build.MODEL.matches("(?i)(?:([a-z\\d-]*SM-N(?:930[a-z\\d]*))|(SM-W2017)|([a-z\\d-]*SM-G(?:95[05][a-z\\d]*)))") ? PIN_SO_SIZE_WITH_IRIS : PIN_SO_SIZE_NO_IRIS;
    }

    public SpayTuiTACommands() {
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_LOAD_PIN), new CommandInfo(LoadPin.getMaxRequestSize(), LoadPin.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_GET_SECURE_RESULT), new CommandInfo(GetSecureResult.getMaxRequestSize(), GetSecureResult.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_PIN_BOX), new CommandInfo(SetPinBox.getMaxRequestSize(), SetPinBox.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SETUP_BIO), new CommandInfo(SetupBio.getMaxRequestSize(), SetupBio.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SETUP), new CommandInfo(SetupPin.getMaxRequestSize(), SetupPin.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_RESUME), new CommandInfo(Resume.getMaxRequestSize(), Resume.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_GET_PIN), new CommandInfo(GetPinSO.getMaxRequestSize(), GetPinSO.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_VERIFY), new CommandInfo(Verify.getMaxRequestSize(), Verify.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_IMAGE), new CommandInfo(SetBackgroundImg.getMaxRequestSize(), SetBackgroundImg.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_CANCEL_IMAGE), new CommandInfo(SetCancelBtn.getMaxRequestSize(), SetCancelBtn.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_PROMPT), new CommandInfo(SetPrompt.getMaxRequestSize(), SetPrompt.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_MODE_TEXT), new CommandInfo(SetSecureModeText.getMaxRequestSize(), SetSecureModeText.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_GET_NONCE), new CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_GET_AUTH_RESULT), new CommandInfo(GetAuthResult.getMaxRequestSize(), GetAuthResult.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_QSEE_START_SECURE_UI), new CommandInfo(StartSecureUI.getMaxRequestSize(), StartSecureUI.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_CHECK_RET_CODE), new CommandInfo(CheckRetCode.getMaxRequestSize(), CheckRetCode.getMaxResponseSize()));
        if (TAController.DEBUG) {
            Log.m285d("SpayTuiTACommands", "GetAuthResult.getMaxRequestSize=" + GetAuthResult.getMaxRequestSize() + "  GetAuthResult.getMaxResponseSize=" + GetAuthResult.getMaxResponseSize());
        }
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_CLOSE), new CommandInfo(CloseTui.getMaxRequestSize(), CloseTui.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_GET_RANDOM), new CommandInfo(GetPinRandom.getMaxRequestSize(), GetPinRandom.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_CHECK_TUI_SESSION), new CommandInfo(CheckTUISession.getMaxRequestSize(), CheckTUISession.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_ACTION_BAR_TEXT), new CommandInfo(SetActionBarText.getMaxRequestSize(), SetActionBarText.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_GENERATE_RANDOM), new CommandInfo(GenerateRandom.getMaxRequestSize(), GenerateRandom.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_UPDATE_FP), new CommandInfo(UpdateFP.getMaxRequestSize(), UpdateFP.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_CLEAR_STATE), new CommandInfo(ClearState.getMaxRequestSize(), ClearState.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_TUI_CMD_SET_RTL), new CommandInfo(SetRtl.getMaxRequestSize(), SetRtl.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_INAPP_CONFIRM), new CommandInfo(InAppConfirm.getMaxRequestSize(), InAppConfirm.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_MERCHANT_SECURE_DISPLAY), new CommandInfo(MerchantSecureDisplay.getMaxRequestSize(), MerchantSecureDisplay.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_MERCHANT_SECURE_TOUCH), new CommandInfo(MerchantSecureTouch.getMaxRequestSize(), MerchantSecureTouch.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_MERCHANT_SECURE_INIT), new CommandInfo(MerchantSecureInit.getMaxRequestSize(), MerchantSecureInit.getMaxResponseSize()));
        addCommandInfo(Integer.valueOf(SPAY_AUTH_CMD_MERCHANT_SEND_SECURE_IMG), new CommandInfo(MerchantSendSecureImg.getMaxRequestSize(), MerchantSendSecureImg.getMaxResponseSize()));
    }
}
