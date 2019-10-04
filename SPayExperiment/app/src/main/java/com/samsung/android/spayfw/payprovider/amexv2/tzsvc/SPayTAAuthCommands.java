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
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class SPayTAAuthCommands
extends TACommands {
    public SPayTAAuthCommands() {
        this.addCommandInfo(257, new TACommands.CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
        this.addCommandInfo(258, new TACommands.CommandInfo(AuthenticateTransaction.getMaxRequestSize(), AuthenticateTransaction.getMaxResponseSize()));
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

            public static class a
            extends TAStruct {
                Blob auth_result_sec_obj = this.inner(new Blob(4096));
            }

        }

        public static class Response
        extends TACommandResponse {

            public static class a
            extends TAStruct {
                Struct.Unsigned32 auth_result = new Struct.Unsigned32();
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
                this.init(0, AmexCommands.TL_MAGIC_NUM, 257, arrby);
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
            public a rV = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rV.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                public Blob out_data = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

}

