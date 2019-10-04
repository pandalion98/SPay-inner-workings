/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class PlccCommands
extends TACommands {
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
    private static final boolean DEBUG = false;
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

    static {
        DEBUG = TAController.DEBUG;
        TL_MAGIC_NUM = new byte[]{126, -102, -21, -65};
    }

    public PlccCommands() {
        this.addCommandInfo(3, new TACommands.CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        this.addCommandInfo(4, new TACommands.CommandInfo(AddCard.getMaxRequestSize(), AddCard.getMaxResponseSize()));
        this.addCommandInfo(1879048193, new TACommands.CommandInfo(MstTransmitSequence.getMaxRequestSize(), MstTransmitSequence.getMaxResponseSize()));
        this.addCommandInfo(5, new TACommands.CommandInfo(MstTransmit.getMaxRequestSize(), MstTransmit.getMaxResponseSize()));
        this.addCommandInfo(6, new TACommands.CommandInfo(Utility_enc4Server_Transport.getMaxRequestSize(), Utility_enc4Server_Transport.getMaxResponseSize()));
        this.addCommandInfo(7, new TACommands.CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        this.addCommandInfo(8, new TACommands.CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
        this.addCommandInfo(9, new TACommands.CommandInfo(ClearData.getMaxRequestSize(), ClearData.getMaxResponseSize()));
        this.addCommandInfo(16, new TACommands.CommandInfo(ExtractGiftCardDetail.getMaxRequestSize(), ExtractGiftCardDetail.getMaxResponseSize()));
        this.addCommandInfo(17, new TACommands.CommandInfo(ExtractLoyaltyCardDetail.getMaxRequestSize(), ExtractLoyaltyCardDetail.getMaxResponseSize()));
    }

    public static byte[] getMagicNumber() {
        return TL_MAGIC_NUM;
    }

    public static int getVersion() {
        return 1;
    }

    public static class AddCard {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 4, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob buf_plcc_data = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.buf_plcc_data.setData(arrby);
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
                Blob encPlccData = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class AuthenticateTransaction {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 8, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob auth_result_sec_obj = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.auth_result_sec_obj.setData(arrby);
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
                Struct.Unsigned32 auth_result = new Struct.Unsigned32();
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ClearData {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2) {
                byte[] arrby = new Data(n2).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 9, arrby);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 unused_uint = new Struct.Unsigned32();

                public Data() {
                }

                public Data(int n2) {
                    this.unused_uint.set(n2);
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
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ExtractGiftCardDetail {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new Data(arrby, arrby2).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 16, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob buf_card_encdata = this.inner(new Blob(4096));
                Blob buf_card_id = this.inner(new Blob(1024));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2) {
                    this.buf_card_id.setData(arrby);
                    this.buf_card_encdata.setData(arrby2);
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
                Blob barcodecontent = this.inner(new Blob(4096));
                Blob cardnumber = this.inner(new Blob(1024));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob extra = this.inner(new Blob(1024));
                Blob pin = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ExtractLoyaltyCardDetail {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new Data(arrby, arrby2).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 17, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob buf_card_encdata = this.inner(new Blob(4096));
                Blob buf_card_id = this.inner(new Blob(1024));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2) {
                    this.buf_card_id.setData(arrby);
                    this.buf_card_encdata.setData(arrby2);
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
                Blob barcodecontent = this.inner(new Blob(4096));
                Blob cardnumber = this.inner(new Blob(1024));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob extra = this.inner(new Blob(2048));
                Blob imgSessionKey = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
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
            Request(int n2) {
                byte[] arrby = new Data(n2).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 7, arrby);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 _nonceSize = new Struct.Unsigned32();

                public Data() {
                }

                public Data(int n2) {
                    this._nonceSize.set(n2);
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
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob out_data = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class LoadCerts {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5) {
                byte[] arrby6 = new Data(arrby, arrby2, arrby3, arrby4, arrby5).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 3, arrby6);
            }

            public static class Data
            extends TAStruct {
                Blob device_cert_enc = this.inner(new Blob(4096));
                Blob device_cert_sign = this.inner(new Blob(4096));
                Blob server_cert_enc = this.inner(new Blob(4096));
                Blob server_cert_sign = this.inner(new Blob(4096));
                Blob server_cert_sub = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5) {
                    this.device_cert_sign.setData(arrby);
                    this.device_cert_enc.setData(arrby2);
                    this.server_cert_sign.setData(arrby3);
                    this.server_cert_enc.setData(arrby4);
                    this.server_cert_sub.setData(arrby5);
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
                Blob cert_drk = this.inner(new Blob(4096));
                Blob cert_encrypt = this.inner(new Blob(4096));
                Blob cert_sign = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class MstTransmit {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new Data(n2, arrby, arrby2).serialize();
                if (DEBUG) {
                    c.d(PlccCommands.TAG, "Request.length = " + arrby3.length);
                }
                this.init(1, PlccCommands.TL_MAGIC_NUM, 5, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob ac = this.inner(new Blob(4096));
                Struct.Unsigned32 baudrate = new Struct.Unsigned32();
                Blob config = this.inner(new Blob(1024));

                public Data() {
                }

                public Data(int n2, byte[] arrby, byte[] arrby2) {
                    this.baudrate.set(n2);
                    this.ac.setData(arrby);
                    this.config.setData(arrby2);
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
                Blob error = this.inner(new Blob(256));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class MstTransmitSequence {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
                byte[] arrby4 = new Data(n2, arrby, arrby2, arrby3).serialize();
                if (DEBUG) {
                    c.d(PlccCommands.TAG, "Request.length = " + arrby4.length);
                }
                this.init(1, PlccCommands.TL_MAGIC_NUM, 1879048193, arrby4);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 baudrate = new Struct.Unsigned32();
                Blob config = this.inner(new Blob(1024));
                Blob track1 = this.inner(new Blob(80));
                Blob track2 = this.inner(new Blob(40));

                public Data() {
                }

                public Data(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this.baudrate.set(n2);
                    this.track1.setData(arrby);
                    this.track2.setData(arrby2);
                    this.config.setData(arrby3);
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
                Blob error = this.inner(new Blob(256));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class RetrieveFromStorage {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 11, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob buf_plcc_data = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.buf_plcc_data.setData(arrby);
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
                Blob buf_plcc_data = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class StoreData {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 10, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob buf_plcc_data = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.buf_plcc_data.setData(arrby);
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
                Blob buf_plcc_data = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Utility_enc4Server_Transport {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new Data(arrby, arrby2).serialize();
                this.init(1, PlccCommands.TL_MAGIC_NUM, 6, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob payment_instrument = this.inner(new Blob(4096));
                Blob timestamp = this.inner(new Blob(7));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2) {
                    this.payment_instrument.setData(arrby);
                    this.timestamp.setData(arrby2);
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
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob resp = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

}

