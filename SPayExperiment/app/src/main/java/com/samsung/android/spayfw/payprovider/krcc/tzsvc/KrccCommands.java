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
package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class KrccCommands
extends TACommands {
    public static final int CMD_KRCC_PAY_CLEAR_MST_DATA = 5;
    public static final int CMD_KRCC_PAY_LOAD_CERT = 2;
    public static final int CMD_KRCC_PAY_PREPARE_MST_DATA = 3;
    public static final int CMD_KRCC_PAY_TRANSMIT_MST_DATA = 4;
    private static final int KRCC_PAY_MAX_CERT_DATA_LEN = 4096;
    public static final int KRCC_PAY_MAX_ERROR_STR_LEN = 256;
    public static final int KRCC_PAY_MAX_MST_TRACK_LEN = 128;
    public static final int KRCC_PAY_MAX_TRANSMIT_MST_CONFIG_LEN = 16;
    public static final int KRCC_PAY_SIGNATURE_LEN = 256;
    public static final byte[] TL_MAGIC_NUM = new byte[]{126, -102, -21, -65};
    public static final int TL_VERSION = 1;

    public KrccCommands() {
        this.addCommandInfo(2, new TACommands.CommandInfo(LoadCert.getMaxRequestSize(), LoadCert.getMaxResponseSize()));
        this.addCommandInfo(4, new TACommands.CommandInfo(TransmitMstData.getMaxRequestSize(), TransmitMstData.getMaxResponseSize()));
        this.addCommandInfo(3, new TACommands.CommandInfo(PrepareMstData.getMaxRequestSize(), PrepareMstData.getMaxResponseSize()));
        this.addCommandInfo(5, new TACommands.CommandInfo(ClearMstData.getMaxRequestSize(), ClearMstData.getMaxResponseSize()));
    }

    public static byte[] getMagicNumber() {
        return TL_MAGIC_NUM;
    }

    public static int getVersion() {
        return 1;
    }

    public static class ClearMstData {
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
                this.init(1, KrccCommands.TL_MAGIC_NUM, 5, arrby);
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

    public static class LoadCert {
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
                this.init(1, KrccCommands.TL_MAGIC_NUM, 2, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob server_cert_sign = this.inner(new Blob(4096));
                Blob server_cert_sub = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2) {
                    this.server_cert_sign.setData(arrby);
                    this.server_cert_sub.setData(arrby2);
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

    public static class PrepareMstData {
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
                this.init(1, KrccCommands.TL_MAGIC_NUM, 3, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob signedTrackHash = this.inner(new Blob(256));
                Blob track = this.inner(new Blob(128));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2) {
                    this.track.setData(arrby);
                    this.signedTrackHash.setData(arrby2);
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

    public static class TransmitMstData {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby) {
                byte[] arrby2 = new Data(n2, arrby).serialize();
                this.init(1, KrccCommands.TL_MAGIC_NUM, 4, arrby2);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 baudRate = new Struct.Unsigned32();
                Blob config = this.inner(new Blob(16));

                public Data() {
                }

                public Data(int n2, byte[] arrby) {
                    this.baudRate.set(n2);
                    this.config.setData(arrby);
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

}

