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
package com.visa.tainterface;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import com.visa.tainterface.VisaTAController;
import javolution.io.Struct;

public class VisaCommands
extends TACommands {
    public static final byte[] TL_MAGIC_NUM = new byte[]{126, -102, -21, -65};

    public VisaCommands() {
        this.addCommandInfo(3, new TACommands.CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        this.addCommandInfo(9, new TACommands.CommandInfo(StoreData.getMaxRequestSize(), StoreData.getMaxResponseSize()));
        this.addCommandInfo(8, new TACommands.CommandInfo(StoreVTSData.getMaxRequestSize(), StoreVTSData.getMaxResponseSize()));
        this.addCommandInfo(16, new TACommands.CommandInfo(RetrieveFromStorage.getMaxRequestSize(), RetrieveFromStorage.getMaxResponseSize()));
        this.addCommandInfo(5, new TACommands.CommandInfo(PrepareMstData.getMaxRequestSize(), PrepareMstData.getMaxResponseSize()));
        this.addCommandInfo(17, new TACommands.CommandInfo(Generate.getMaxRequestSize(), Generate.getMaxResponseSize()));
        this.addCommandInfo(6, new TACommands.CommandInfo(PrepareDataForVTS.getMaxRequestSize(), PrepareDataForVTS.getMaxResponseSize()));
        this.addCommandInfo(7, new TACommands.CommandInfo(PrepareSecureDataForVTS.getMaxRequestSize(), PrepareSecureDataForVTS.getMaxResponseSize()));
        this.addCommandInfo(20, new TACommands.CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        this.addCommandInfo(21, new TACommands.CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
        this.addCommandInfo(2, new TACommands.CommandInfo(TransmitMstData.getMaxRequestSize(), TransmitMstData.getMaxResponseSize()));
        this.addCommandInfo(22, new TACommands.CommandInfo(ClearMstData.getMaxRequestSize(), ClearMstData.getMaxResponseSize()));
        this.addCommandInfo(23, new TACommands.CommandInfo(GenInAppPaymentInfo.getMaxRequestSize(), GenInAppPaymentInfo.getMaxResponseSize()));
    }

    public static class AuthenticateTransaction {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 21, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob auth_result_sec_obj = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby) {
                    this.auth_result_sec_obj.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a LS = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.LS.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 auth_result = new Struct.Unsigned32();
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ClearMstData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2) {
                byte[] arrby = new a(n2).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 22, arrby);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 unused_uint = new Struct.Unsigned32();

                public a() {
                }

                public a(int n2) {
                    this.unused_uint.set(n2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a LT = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.LT.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GenInAppJwePaymentInfo {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
                byte[] arrby5 = new a(arrby, arrby2, arrby3, arrby4).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 24, arrby5);
            }

            public static class a
            extends TAStruct {
                Blob LU = this.inner(new Blob(4096));
                Blob LV = this.inner(new Blob(4096));
                Blob LW = this.inner(new Blob(4096));
                Blob LX = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
                    this.LU.setData(arrby3);
                    this.LV.setData(arrby4);
                    this.LW.setData(arrby);
                    this.LX.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
        }

    }

    public static class GenInAppPaymentInfo {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                byte[] arrby4 = new a(arrby, arrby2, arrby3).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 23, arrby4);
            }

            public static class a
            extends TAStruct {
                Blob LW = this.inner(new Blob(4096));
                Blob LX = this.inner(new Blob(4096));
                Blob nonce = this.inner(new Blob(32));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this.LW.setData(arrby);
                    this.LX.setData(arrby2);
                    this.nonce.setData(arrby3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a LY = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.LY.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob resp = this.inner(new Blob(5120));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Generate {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, VisaTAController.VISA_CRYPTO_ALG vISA_CRYPTO_ALG, byte[] arrby2) {
                byte[] arrby3 = new a(arrby, vISA_CRYPTO_ALG, arrby2).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 17, arrby3);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 LZ = new Struct.Unsigned32();
                Blob Ma = this.inner(new Blob(4096));
                Blob Mb = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby, VisaTAController.VISA_CRYPTO_ALG vISA_CRYPTO_ALG, byte[] arrby2) {
                    this.Ma.setData(arrby);
                    this.LZ.set(vISA_CRYPTO_ALG.ordinal());
                    this.Mb.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a Mc = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.Mc.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob Md = this.inner(new Blob(256));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GetNonce {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2) {
                byte[] arrby = new a(n2).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 20, arrby);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 _nonceSize = new Struct.Unsigned32();

                public a() {
                }

                public a(int n2) {
                    this._nonceSize.set(n2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a Me = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.Me.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob out_data = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class LoadCerts {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
                byte[] arrby5 = new a(arrby, arrby2, arrby3, arrby4).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 3, arrby5);
            }

            public static class a
            extends TAStruct {
                Blob device_cert_enc = this.inner(new Blob(4096));
                Blob device_cert_sign = this.inner(new Blob(4096));
                Blob server_cert_enc = this.inner(new Blob(4096));
                Blob server_cert_sign = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
                    this.device_cert_sign.setData(arrby);
                    this.device_cert_enc.setData(arrby2);
                    this.server_cert_sign.setData(arrby3);
                    this.server_cert_enc.setData(arrby4);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a Mf = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.Mf.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 Mg = new Struct.Unsigned32();
                Struct.Unsigned32 Mh = new Struct.Unsigned32();
                Blob Mi = this.inner(new Blob(24));
                Blob cert_drk = this.inner(new Blob(4096));
                Blob cert_encrypt = this.inner(new Blob(4096));
                Blob cert_sign = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class PrepareDataForVTS {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, boolean bl) {
                byte[] arrby2 = new a(arrby, bl).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 6, arrby2);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8 Mj = new Struct.Unsigned8();
                Blob payment_instrument = this.inner(new Blob(4096));

                public a() {
                }

                /*
                 * Enabled aggressive block sorting
                 */
                public a(byte[] arrby, boolean bl) {
                    this.payment_instrument.setData(arrby);
                    Struct.Unsigned8 unsigned8 = this.Mj;
                    boolean bl2 = bl;
                    unsigned8.set((short)(bl2 ? 1 : 0));
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a Mk = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.Mk.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob resp = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class PrepareMstData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6, byte[] arrby7, byte[] arrby8, byte[] arrby9) {
                byte[] arrby10 = new a(arrby, arrby2, arrby3, arrby4, arrby5, arrby6, arrby7, arrby8, arrby9).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 5, arrby10);
            }

            public static class a
            extends TAStruct {
                Blob Ml = this.inner(new Blob(256));
                Blob Mm = this.inner(new Blob(256));
                Blob Mn = this.inner(new Blob(256));
                Blob Mo = this.inner(new Blob(256));
                Blob Mp = this.inner(new Blob(256));
                Blob Mq = this.inner(new Blob(256));
                Blob Mr = this.inner(new Blob(256));
                Blob Ms = this.inner(new Blob(4096));
                Blob timestamp = this.inner(new Blob(256));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6, byte[] arrby7, byte[] arrby8, byte[] arrby9) {
                    this.Ml.setData(arrby);
                    this.Mm.setData(arrby2);
                    this.Mn.setData(arrby3);
                    this.timestamp.setData(arrby4);
                    this.Mo.setData(arrby5);
                    this.Mp.setData(arrby6);
                    this.Mq.setData(arrby7);
                    this.Mr.setData(arrby8);
                    this.Ms.setData(arrby9);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a Mt = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.Mt.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class PrepareSecureDataForVTS {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {

            public static class a
            extends TAStruct {
                Blob payment_instrument = this.inner(new Blob(4096));
            }

        }

        public static class Response
        extends TACommandResponse {

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob resp = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class RetrieveFromStorage {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 16, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob Mu = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby) {
                    this.Mu.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a Mv = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.Mv.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob Mw = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class StoreData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 9, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob Mx = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby) {
                    this.Mx.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a My = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.My.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob Mz = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class StoreVTSData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 8, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob MA = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby) {
                    this.MA.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a MB = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.MB.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob MC = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class TransmitMstData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby) {
                byte[] arrby2 = new a(n2, arrby).serialize();
                this.init(1, VisaCommands.TL_MAGIC_NUM, 2, arrby2);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 baudRate = new Struct.Unsigned32();
                Blob config = this.inner(new Blob(28));

                public a() {
                }

                public a(int n2, byte[] arrby) {
                    this.config.setData(arrby);
                    this.baudRate.set(n2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a MD = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.MD.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

}

