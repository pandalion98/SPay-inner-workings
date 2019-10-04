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
package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class GlobalMembershipCommands
extends TACommands {
    private static final boolean DEBUG = TAController.DEBUG;
    public static final byte[] TL_MAGIC_NUM = new byte[]{126, -102, -21, -65};

    public GlobalMembershipCommands() {
        this.addCommandInfo(6, new TACommands.CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        this.addCommandInfo(8, new TACommands.CommandInfo(AddCard.getMaxRequestSize(), AddCard.getMaxResponseSize()));
        this.addCommandInfo(21, new TACommands.CommandInfo(MstTransmit.getMaxRequestSize(), MstTransmit.getMaxResponseSize()));
        this.addCommandInfo(7, new TACommands.CommandInfo(Utility_enc4Server_Transport.getMaxRequestSize(), Utility_enc4Server_Transport.getMaxResponseSize()));
        this.addCommandInfo(23, new TACommands.CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        this.addCommandInfo(24, new TACommands.CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
        this.addCommandInfo(25, new TACommands.CommandInfo(ClearData.getMaxRequestSize(), ClearData.getMaxResponseSize()));
        this.addCommandInfo(9, new TACommands.CommandInfo(ExtractGlobalMembershipCardDetail.getMaxRequestSize(), ExtractGlobalMembershipCardDetail.getMaxResponseSize()));
    }

    public static class AddCard {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new a(arrby, arrby2).serialize();
                this.init(1, GlobalMembershipCommands.TL_MAGIC_NUM, 8, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob zk = this.inner(new Blob(1024));
                Blob zl = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.zk.setData(arrby);
                    this.zl.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a zm = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.zm.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
                Blob zn = this.inner(new Blob(4096));
            }

        }

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
                this.init(1, GlobalMembershipCommands.TL_MAGIC_NUM, 24, arrby2);
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
            public a zo = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.zo.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 auth_result = new Struct.Unsigned32();
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ClearData {
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
                Struct.Unsigned32 unused_uint = new Struct.Unsigned32();
            }

        }

        public static class Response
        extends TACommandResponse {

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ExtractGlobalMembershipCardDetail {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new a(arrby, arrby2).serialize();
                this.init(1, GlobalMembershipCommands.TL_MAGIC_NUM, 9, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob buf_card_encdata = this.inner(new Blob(4096));
                Blob zp = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.zp.setData(arrby);
                    this.buf_card_encdata.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a zq = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.zq.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob barcodecontent = this.inner(new Blob(4096));
                Blob cardnumber = this.inner(new Blob(1024));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pin = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
                Blob zr = this.inner(new Blob(1024));
                Blob zt = this.inner(new Blob(1024));
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

            public static class a
            extends TAStruct {
                Struct.Unsigned32 _nonceSize = new Struct.Unsigned32();
            }

        }

        public static class Response
        extends TACommandResponse {

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob out_data = this.inner(new Blob(1024));
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
            Request(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5) {
                byte[] arrby6 = new a(arrby, arrby2, arrby3, arrby4, arrby5).serialize();
                this.init(1, GlobalMembershipCommands.TL_MAGIC_NUM, 6, arrby6);
            }

            public static class a
            extends TAStruct {
                Blob device_cert_enc = this.inner(new Blob(4096));
                Blob device_cert_sign = this.inner(new Blob(4096));
                Blob server_cert_enc = this.inner(new Blob(4096));
                Blob server_cert_sign = this.inner(new Blob(4096));
                Blob server_cert_sub = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5) {
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
            public a zu = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.zu.deserialize(this.mResponse);
            }

            public static class a
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
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {

            public static class a
            extends TAStruct {
                Blob ac = this.inner(new Blob(4096));
                Struct.Unsigned32 baudrate = new Struct.Unsigned32();
                Blob config = this.inner(new Blob(1024));
            }

        }

        public static class Response
        extends TACommandResponse {

            public static class a
            extends TAStruct {
                Blob error = this.inner(new Blob(256));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Utility_enc4Server_Transport {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new a(arrby, arrby2).serialize();
                this.init(1, GlobalMembershipCommands.TL_MAGIC_NUM, 7, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob payment_instrument = this.inner(new Blob(4096));
                Blob timestamp = this.inner(new Blob(7));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.payment_instrument.setData(arrby);
                    this.timestamp.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a zv = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.zv.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob resp = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

}

