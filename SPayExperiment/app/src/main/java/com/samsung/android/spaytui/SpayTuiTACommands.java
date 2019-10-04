/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spaytui;

import android.os.Build;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class SpayTuiTACommands
extends TACommands {
    private static final int APP_NAME_BUFFER_SIZE = 128;
    private static final int AUTH_SO_BUFFER_SIZE = 1024;
    private static final int FP_ID_LEN = 1024;
    private static final int IRIS_ID_LEN = 1024;
    private static final int NONCE_BUFFER_SIZE = 64;
    private static final int PIN_SIZE = 16;
    private static final int PIN_SO_SIZE = 0;
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
    public static final byte[] TL_MAGIC_NUM = new byte[]{126, -102, -21, -65};
    public static final int TL_VERSION = 1;

    /*
     * Enabled aggressive block sorting
     */
    static {
        int n2 = Build.MODEL.matches("(?i)(?:([a-z\\d-]*SM-N(?:930[a-z\\d]*))|(SM-W2017)|([a-z\\d-]*SM-G(?:95[05][a-z\\d]*)))") ? 2297 : 1266;
        PIN_SO_SIZE = n2;
    }

    public SpayTuiTACommands() {
        this.addCommandInfo(65536, new TACommands.CommandInfo(LoadPin.getMaxRequestSize(), LoadPin.getMaxResponseSize()));
        this.addCommandInfo(262145, new TACommands.CommandInfo(GetSecureResult.getMaxRequestSize(), GetSecureResult.getMaxResponseSize()));
        this.addCommandInfo(196608, new TACommands.CommandInfo(SetPinBox.getMaxRequestSize(), SetPinBox.getMaxResponseSize()));
        this.addCommandInfo(131076, new TACommands.CommandInfo(SetupBio.getMaxRequestSize(), SetupBio.getMaxResponseSize()));
        this.addCommandInfo(131072, new TACommands.CommandInfo(SetupPin.getMaxRequestSize(), SetupPin.getMaxResponseSize()));
        this.addCommandInfo(131074, new TACommands.CommandInfo(Resume.getMaxRequestSize(), Resume.getMaxResponseSize()));
        this.addCommandInfo(262144, new TACommands.CommandInfo(GetPinSO.getMaxRequestSize(), GetPinSO.getMaxResponseSize()));
        this.addCommandInfo(131073, new TACommands.CommandInfo(Verify.getMaxRequestSize(), Verify.getMaxResponseSize()));
        this.addCommandInfo(196610, new TACommands.CommandInfo(SetBackgroundImg.getMaxRequestSize(), SetBackgroundImg.getMaxResponseSize()));
        this.addCommandInfo(196611, new TACommands.CommandInfo(SetCancelBtn.getMaxRequestSize(), SetCancelBtn.getMaxResponseSize()));
        this.addCommandInfo(196609, new TACommands.CommandInfo(SetPrompt.getMaxRequestSize(), SetPrompt.getMaxResponseSize()));
        this.addCommandInfo(65537, new TACommands.CommandInfo(SetSecureModeText.getMaxRequestSize(), SetSecureModeText.getMaxResponseSize()));
        this.addCommandInfo(524289, new TACommands.CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        this.addCommandInfo(524290, new TACommands.CommandInfo(GetAuthResult.getMaxRequestSize(), GetAuthResult.getMaxResponseSize()));
        this.addCommandInfo(590224, new TACommands.CommandInfo(StartSecureUI.getMaxRequestSize(), StartSecureUI.getMaxResponseSize()));
        this.addCommandInfo(327681, new TACommands.CommandInfo(CheckRetCode.getMaxRequestSize(), CheckRetCode.getMaxResponseSize()));
        if (TAController.DEBUG) {
            c.d("SpayTuiTACommands", "GetAuthResult.getMaxRequestSize=" + GetAuthResult.getMaxRequestSize() + "  GetAuthResult.getMaxResponseSize=" + GetAuthResult.getMaxResponseSize());
        }
        this.addCommandInfo(131088, new TACommands.CommandInfo(CloseTui.getMaxRequestSize(), CloseTui.getMaxResponseSize()));
        this.addCommandInfo(262146, new TACommands.CommandInfo(GetPinRandom.getMaxRequestSize(), GetPinRandom.getMaxResponseSize()));
        this.addCommandInfo(327680, new TACommands.CommandInfo(CheckTUISession.getMaxRequestSize(), CheckTUISession.getMaxResponseSize()));
        this.addCommandInfo(65538, new TACommands.CommandInfo(SetActionBarText.getMaxRequestSize(), SetActionBarText.getMaxResponseSize()));
        this.addCommandInfo(262147, new TACommands.CommandInfo(GenerateRandom.getMaxRequestSize(), GenerateRandom.getMaxResponseSize()));
        this.addCommandInfo(131075, new TACommands.CommandInfo(UpdateFP.getMaxRequestSize(), UpdateFP.getMaxResponseSize()));
        this.addCommandInfo(131089, new TACommands.CommandInfo(ClearState.getMaxRequestSize(), ClearState.getMaxResponseSize()));
        this.addCommandInfo(196624, new TACommands.CommandInfo(SetRtl.getMaxRequestSize(), SetRtl.getMaxResponseSize()));
        this.addCommandInfo(589824, new TACommands.CommandInfo(InAppConfirm.getMaxRequestSize(), InAppConfirm.getMaxResponseSize()));
        this.addCommandInfo(589825, new TACommands.CommandInfo(MerchantSecureDisplay.getMaxRequestSize(), MerchantSecureDisplay.getMaxResponseSize()));
        this.addCommandInfo(589826, new TACommands.CommandInfo(MerchantSecureTouch.getMaxRequestSize(), MerchantSecureTouch.getMaxResponseSize()));
        this.addCommandInfo(589827, new TACommands.CommandInfo(MerchantSecureInit.getMaxRequestSize(), MerchantSecureInit.getMaxResponseSize()));
        this.addCommandInfo(589828, new TACommands.CommandInfo(MerchantSendSecureImg.getMaxRequestSize(), MerchantSendSecureImg.getMaxResponseSize()));
    }

    public static class CheckRetCode {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 327681, arrby);
            }

            public static class Data
            extends TAStruct {
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class CheckTUISession {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 327680, arrby);
            }

            public static class Data
            extends TAStruct {
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class ClearState {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131089, arrby);
            }

            public static class Data
            extends TAStruct {
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class CloseTui {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131088, arrby);
            }

            public static class Data
            extends TAStruct {
                Blob placeholder = this.inner(new Blob(1));
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class GenerateRandom {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 262147, arrby);
            }

            public static class Data
            extends TAStruct {
            }

        }

        public static class Response
        extends TACommandResponse {
            private static final String TAG = "SpayTuiTACommands";
            public Data mRetVal = new Data();

            /*
             * Enabled aggressive block sorting
             */
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
                if (this.mRetVal.retCode.get() == 0L) {
                    if (!TAController.DEBUG) return;
                    {
                        c.d(TAG, "Generate PIN random Successful");
                        return;
                    }
                } else {
                    if (!TAController.DEBUG) return;
                    {
                        c.d(TAG, "failed to generate PIN random, ret = " + this.mRetVal.retCode.get());
                        return;
                    }
                }
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 existPinLen = new Struct.Unsigned32();
                Blob pinSo = this.inner(new Blob(SpayTuiTACommands.access$000()));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class GetAuthResult {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, String string, byte[] arrby2) {
                this.mData.setData(arrby, string.getBytes(), arrby2);
                byte[] arrby3 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 524290, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob app_name = this.inner(new Blob(128));
                Blob nonce = this.inner(new Blob(64));
                Blob so = this.inner(new Blob(1024));

                public void setData(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this.nonce.setData(arrby);
                    this.app_name.setData(arrby2);
                    this.so.setData(arrby3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 authType = new Struct.Unsigned32();
                Struct.Unsigned32 existPinLen = new Struct.Unsigned32();
                Blob pin_so = this.inner(new Blob(SpayTuiTACommands.access$000()));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
                Blob so = this.inner(new Blob(436));
                Struct.Unsigned8 update_pin_so = new Struct.Unsigned8();
            }

        }

    }

    public static class GetNonce {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(int n2) {
                this.mData.setData(n2);
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 524289, arrby);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 nonce_len = new Struct.Unsigned32();
                Blob placeholder = this.inner(new Blob(1));

                public void setData(int n2) {
                    this.nonce_len.set(n2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Blob nonce = this.inner(new Blob(64));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class GetPinRandom {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(String string) {
                this.mData.setData(string.getBytes());
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 262146, arrby);
            }

            public static class Data
            extends TAStruct {
                Blob app_name = this.inner(new Blob(128));

                public void setData(byte[] arrby) {
                    this.app_name.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 authType = new Struct.Unsigned32();
                Blob result_so = this.inner(new Blob(436));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class GetPinSO {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 262144, arrby);
            }

            public static class Data
            extends TAStruct {
                Blob placeholder = this.inner(new Blob(1));
            }

        }

        public static class Response
        extends TACommandResponse {
            private static final String TAG = "SpayTuiTACommands";
            public Data mRetVal = new Data();

            /*
             * Enabled aggressive block sorting
             */
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
                c.d(TAG, "PIN_SO_SIZE " + PIN_SO_SIZE);
                if (this.mRetVal.retCode.get() == 0L) {
                    if (!TAController.DEBUG) return;
                    {
                        c.d(TAG, "Get Pin So Successful");
                        return;
                    }
                } else {
                    if (!TAController.DEBUG) return;
                    {
                        c.d(TAG, "failed to get PIN SO, ret = " + this.mRetVal.retCode.get());
                        return;
                    }
                }
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 existPinLen = new Struct.Unsigned32();
                Blob pinSo = this.inner(new Blob(SpayTuiTACommands.access$000()));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class GetSecureResult {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, String string) {
                this.mData.setData(arrby, string.getBytes());
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 262145, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob app_name = this.inner(new Blob(128));
                Blob nonce = this.inner(new Blob(64));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this.nonce.setData(arrby);
                    this.app_name.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 authType = new Struct.Unsigned32();
                Blob result_so = this.inner(new Blob(436));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class InAppConfirm {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6, byte[] arrby7) {
                this.mData.setData(arrby, arrby2, arrby3, arrby4, arrby5, arrby6, arrby7);
                byte[] arrby8 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 589824, arrby8);
            }

            public static class Data
            extends TAStruct {
                Blob img = this.inner(new Blob(256000));

                public void setData(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6, byte[] arrby7) {
                    int n2 = 0;
                    if (16 + (arrby.length + arrby2.length + arrby3.length + arrby4.length + arrby5.length + arrby6.length + arrby7.length) > 256000) {
                        // empty if block
                    }
                    byte[] arrby8 = new byte[]{(byte)(255 & arrby.length >> 8), (byte)(255 & arrby.length), (byte)(255 & arrby2.length >> 8), (byte)(255 & arrby2.length), (byte)(255 & arrby3.length >> 8), (byte)(255 & arrby3.length), (byte)(255 & arrby4.length >> 8), (byte)(255 & arrby4.length), (byte)(255 & arrby5.length >> 8), (byte)(255 & arrby5.length), (byte)(255 & arrby6.length >> 16), (byte)(255 & arrby6.length >> 8), (byte)(255 & arrby6.length), (byte)(255 & arrby7.length >> 16), (byte)(255 & arrby7.length >> 8), (byte)(255 & arrby7.length)};
                    while (n2 < 16) {
                        c.d("SpayTuiTACommands", "length[" + n2 + "]=" + arrby8[n2]);
                        ++n2;
                    }
                    this.img.setData(arrby8);
                    this.img.addData(arrby);
                    this.img.addData(arrby2);
                    this.img.addData(arrby3);
                    this.img.addData(arrby4);
                    this.img.addData(arrby5);
                    this.img.addData(arrby6);
                    this.img.addData(arrby7);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class LoadPin {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 65536, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob pin_so = this.inner(new Blob(SpayTuiTACommands.access$000()));

                public void setData(byte[] arrby) {
                    this.pin_so.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 existPinLen = new Struct.Unsigned32();
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
                Blob updated_pin_so = this.inner(new Blob(SpayTuiTACommands.access$000()));
            }

        }

    }

    public static class MerchantSecureDisplay {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 589825, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob img = this.inner(new Blob(256000));

                public void setData(byte[] arrby) {
                    if (3 + arrby.length > 256000) {
                        // empty if block
                    }
                    byte[] arrby2 = new byte[]{(byte)(255 & arrby.length >> 16), (byte)(255 & arrby.length >> 8), (byte)(255 & arrby.length)};
                    this.img.setData(arrby2);
                    this.img.addData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class MerchantSecureInit {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 589827, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob img = this.inner(new Blob(256000));

                public void setData(byte[] arrby) {
                    if (3 + arrby.length > 256000) {
                        // empty if block
                    }
                    byte[] arrby2 = new byte[]{(byte)(255 & arrby.length >> 16), (byte)(255 & arrby.length >> 8), (byte)(255 & arrby.length)};
                    this.img.setData(arrby2);
                    this.img.addData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class MerchantSecureTouch {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 589826, arrby);
            }

            public static class Data
            extends TAStruct {
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class MerchantSendSecureImg {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 589828, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob img = this.inner(new Blob(256000));

                public void setData(byte[] arrby) {
                    if (3 + arrby.length > 256000) {
                        // empty if block
                    }
                    byte[] arrby2 = new byte[]{(byte)(255 & arrby.length >> 16), (byte)(255 & arrby.length >> 8), (byte)(255 & arrby.length)};
                    this.img.setData(arrby2);
                    this.img.addData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class PreloadFpSecureResult {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 262148, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob secure_object = this.inner(new Blob(1024));

                public void setData(byte[] arrby) {
                    this.secure_object.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class Resume {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(boolean bl) {
                this.mData.setData(bl);
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131074, arrby);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned8 update_display_only = new Struct.Unsigned8();

                public void setData(boolean bl) {
                    if (bl) {
                        this.update_display_only.set((short)1);
                        return;
                    }
                    this.update_display_only.set((short)0);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetActionBarText {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 65538, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob image = this.inner(new Blob(256000));

                public void setData(byte[] arrby) {
                    this.image.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetBackgroundImg {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, int n2, int n3) {
                this.mData.setData(arrby, n2, n3);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 196610, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob image = this.inner(new Blob(256000));
                Struct.Unsigned32 x = new Struct.Unsigned32();
                Struct.Unsigned32 y = new Struct.Unsigned32();

                public void setData(byte[] arrby, int n2, int n3) {
                    this.image.setData(arrby);
                    this.x.set(n2);
                    this.y.set(n3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetCancelBtn {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, byte[] arrby2, int n2, int n3) {
                this.mData.setData(arrby, arrby2, n2, n3);
                byte[] arrby3 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 196611, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob images = this.inner(new Blob(256000));
                Struct.Unsigned32 pressed_len = new Struct.Unsigned32();
                Struct.Unsigned32 unpressed_len = new Struct.Unsigned32();
                Struct.Unsigned32 x = new Struct.Unsigned32();
                Struct.Unsigned32 y = new Struct.Unsigned32();

                /*
                 * Enabled aggressive block sorting
                 */
                public void setData(byte[] arrby, byte[] arrby2, int n2, int n3) {
                    long l2 = 0L;
                    this.images.setData(arrby);
                    this.images.addData(arrby2);
                    Struct.Unsigned32 unsigned32 = this.unpressed_len;
                    long l3 = arrby == null ? l2 : (long)arrby.length;
                    unsigned32.set(l3);
                    Struct.Unsigned32 unsigned322 = this.pressed_len;
                    if (arrby2 != null) {
                        l2 = arrby2.length;
                    }
                    unsigned322.set(l2);
                    this.x.set(n2);
                    this.y.set(n3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetPinBox {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5, int n6) {
                this.mData.setData(arrby, arrby2, n2, n3, n4, n5, n6);
                byte[] arrby3 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 196608, arrby3);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 error_pin_len = new Struct.Unsigned32();
                Blob images = this.inner(new Blob(256000));
                Struct.Unsigned32 normal_pin_len = new Struct.Unsigned32();
                Struct.Unsigned8 number = new Struct.Unsigned8();
                Struct.Unsigned32 pinbox_height = new Struct.Unsigned32();
                Struct.Unsigned32 pinbox_width = new Struct.Unsigned32();
                Struct.Unsigned32 space = new Struct.Unsigned32();
                Struct.Unsigned32 y = new Struct.Unsigned32();

                /*
                 * Enabled aggressive block sorting
                 */
                public void setData(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5, int n6) {
                    long l2 = 0L;
                    this.images.setData(arrby);
                    this.images.addData(arrby2);
                    Struct.Unsigned32 unsigned32 = this.normal_pin_len;
                    long l3 = arrby == null ? l2 : (long)arrby.length;
                    unsigned32.set(l3);
                    Struct.Unsigned32 unsigned322 = this.error_pin_len;
                    if (arrby2 != null) {
                        l2 = arrby2.length;
                    }
                    unsigned322.set(l2);
                    this.number.set((short)n2);
                    this.y.set(n3);
                    this.space.set(n4);
                    this.pinbox_width.set(n5);
                    this.pinbox_height.set(n6);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetPrompt {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, int n2, int n3) {
                this.mData.setData(arrby, n2, n3);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 196609, arrby2);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 above = new Struct.Unsigned32();
                Struct.Unsigned32 dis_to_pin = new Struct.Unsigned32();
                Blob image = this.inner(new Blob(256000));

                public void setData(byte[] arrby, int n2, int n3) {
                    this.image.setData(arrby);
                    this.above.set(n2);
                    this.dis_to_pin.set(n3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetRtl {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(boolean bl) {
                this.mData.setData(bl);
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 196624, arrby);
            }

            public static class Data
            extends TAStruct {
                Blob msg = this.inner(new Blob(2));

                /*
                 * Enabled aggressive block sorting
                 */
                public void setData(boolean bl) {
                    int n2 = 1;
                    byte[] arrby = new byte[n2];
                    if (!bl) {
                        n2 = 0;
                    }
                    arrby[0] = n2;
                    this.msg.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetSecureModeText {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 65537, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob image = this.inner(new Blob(256000));

                public void setData(byte[] arrby) {
                    this.image.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetupBio {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby, byte[] arrby2) {
                this.mData.setData(arrby, arrby2);
                byte[] arrby3 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131076, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob fp_so = this.inner(new Blob(1024));
                Blob iris_so = this.inner(new Blob(1024));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this.fp_so.setData(arrby);
                    this.iris_so.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class SetupPin {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131072, arrby);
            }

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131072, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob so = this.inner(new Blob(1024));

                public Data() {
                    this.so.setData(null);
                }

                public void setData(byte[] arrby) {
                    this.so.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class StartSecureUI {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request() {
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 590224, arrby);
            }

            public static class Data
            extends TAStruct {
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

    public static class UpdateFP {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(byte[] arrby) {
                this.mData.setData(arrby);
                byte[] arrby2 = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131075, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob so = this.inner(new Blob(1024));

                public void setData(byte[] arrby) {
                    this.so.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 existPinLen = new Struct.Unsigned32();
                Blob pin_so = this.inner(new Blob(SpayTuiTACommands.access$000()));
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
                Struct.Unsigned8 update_pin_so = new Struct.Unsigned8();
            }

        }

    }

    public static class Verify {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Data mData = new Data();

            Request(boolean bl, boolean bl2) {
                this.mData.setData(bl, bl2);
                byte[] arrby = this.mData.serialize();
                this.init(1, SpayTuiTACommands.TL_MAGIC_NUM, 131073, arrby);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned8 close_tui_session = new Struct.Unsigned8();
                Struct.Unsigned8 update_display_only = new Struct.Unsigned8();

                /*
                 * Enabled aggressive block sorting
                 */
                public void setData(boolean bl, boolean bl2) {
                    if (bl) {
                        this.close_tui_session.set((short)1);
                    } else {
                        this.close_tui_session.set((short)0);
                    }
                    if (bl2) {
                        this.update_display_only.set((short)1);
                        return;
                    }
                    this.update_display_only.set((short)0);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 retCode = new Struct.Unsigned32();
            }

        }

    }

}

