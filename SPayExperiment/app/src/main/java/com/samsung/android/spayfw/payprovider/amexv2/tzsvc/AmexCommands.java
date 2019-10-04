/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  android.util.Log
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.SPayTAAuthCommands;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.KeyValPairs;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import java.util.List;
import javolution.io.Struct;

public class AmexCommands
extends SPayTAAuthCommands {
    public AmexCommands() {
        this.addCommandInfo(1, new TACommands.CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        this.addCommandInfo(2, new TACommands.CommandInfo(ProcessRequestData.getMaxRequestSize(), ProcessRequestData.getMaxResponseSize()));
        this.addCommandInfo(4, new TACommands.CommandInfo(ProcessResponseData.getMaxRequestSize(), ProcessResponseData.getMaxResponseSize()));
        this.addCommandInfo(5, new TACommands.CommandInfo(ProcessData.getMaxRequestSize(), ProcessData.getMaxResponseSize()));
        this.addCommandInfo(17, new TACommands.CommandInfo(Open.getMaxRequestSize(), Open.getMaxResponseSize()));
        this.addCommandInfo(32, new TACommands.CommandInfo(Close.getMaxRequestSize(), Close.getMaxResponseSize()));
        this.addCommandInfo(21, new TACommands.CommandInfo(Init.getMaxRequestSize(), Init.getMaxResponseSize()));
        this.addCommandInfo(22, new TACommands.CommandInfo(PersoToken.getMaxRequestSize(), PersoToken.getMaxResponseSize()));
        this.addCommandInfo(23, new TACommands.CommandInfo(Update.getMaxRequestSize(), Update.getMaxResponseSize()));
        this.addCommandInfo(33, new TACommands.CommandInfo(LCM.getMaxRequestSize(), LCM.getMaxResponseSize()));
        this.addCommandInfo(18, new TACommands.CommandInfo(InitializeSecureChannel.getMaxRequestSize(), InitializeSecureChannel.getMaxResponseSize()));
        this.addCommandInfo(19, new TACommands.CommandInfo(UpdateSecureChannel.getMaxRequestSize(), UpdateSecureChannel.getMaxResponseSize()));
        this.addCommandInfo(34, new TACommands.CommandInfo(ComputeAC.getMaxRequestSize(), ComputeAC.getMaxResponseSize()));
        this.addCommandInfo(35, new TACommands.CommandInfo(Sign.getMaxRequestSize(), Sign.getMaxResponseSize()));
        this.addCommandInfo(38, new TACommands.CommandInfo(Verify.getMaxRequestSize(), Verify.getMaxResponseSize()));
        this.addCommandInfo(36, new TACommands.CommandInfo(ReqMST.getMaxRequestSize(), ReqMST.getMaxResponseSize()));
        this.addCommandInfo(37, new TACommands.CommandInfo(GenerateInAppPaymentPayload.getMaxRequestSize(), GenerateInAppPaymentPayload.getMaxResponseSize()));
    }

    public static class Close {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 32, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qN = this.inner(new Blob(16384));

                public a() {
                }

                public a(byte[] arrby) {
                    this.qN.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a qO = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.qO.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob qP = this.inner(new Blob(16384));
                Blob qQ = this.inner(new Blob(16384));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ComputeAC {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 34, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qR = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby) {
                    this.qR.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a qS = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.qS.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob pP = this.inner(new Blob(8));
                Struct.Unsigned16 qT = new Struct.Unsigned16();
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GenerateInAppPaymentPayload {
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
                Blob pH = this.inner(new Blob(8));
                KeyValPairs pJ = this.inner(new KeyValPairs(10, 64, 256));
                b qU = this.inner(new b(3));
            }

        }

        public static class Response
        extends TACommandResponse {

            public static class a
            extends TAStruct {
                Blob pK = this.inner(new Blob(2048));
                Blob pL = this.inner(new Blob(64));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GetSignatureData {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 24, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qV = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby) {
                    this.qV.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a qW = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.qW.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob qX = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Init {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 21, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qY = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby) {
                    this.qY.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a qZ = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.qZ.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class InitializeSecureChannel {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 18, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob ra = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby) {
                    this.ra.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rb = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rb.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob rc = this.inner(new Blob(384));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class LCM {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 33, arrby);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8 rd = new Struct.Unsigned8();

                public a() {
                }

                public a(int n2) {
                    this.rd.set((short)n2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a re = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.re.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
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
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new a(arrby, arrby2).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 1, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob deviceSignRSA2048PrivCert = this.inner(new Blob(4096));
                Blob pQ = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.deviceSignRSA2048PrivCert.setData(arrby);
                    this.pQ.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rf = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rf.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob deviceRootRSA2048PubCert = this.inner(new Blob(4096));
                Blob deviceSignRSA2048PubCert = this.inner(new Blob(4096));
                Blob pR = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Open {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 17, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob qP = this.inner(new Blob(16384));
                Blob qQ = this.inner(new Blob(16384));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.qP.setData(arrby);
                    this.qQ.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rg = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rg.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob qN = this.inner(new Blob(16384));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class PersoToken {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new a(n2, arrby, arrby2).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 22, arrby3);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 rh = new Struct.Unsigned32();
                Blob ri = this.inner(new Blob(4096));
                Blob rj = this.inner(new Blob(8));

                public a() {
                }

                public a(int n2, byte[] arrby, byte[] arrby2) {
                    this.rh.set(n2);
                    this.ri.setData(arrby);
                    this.rj.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rk = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rk.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessData {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 5, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob data = this.inner(new Blob(8192));
                Struct.Unsigned8 pS = new Struct.Unsigned8();

                public a() {
                }

                /*
                 * Enabled aggressive block sorting
                 */
                public a(byte[] arrby, boolean bl) {
                    this.data.setData(arrby);
                    Struct.Unsigned8 unsigned8 = this.pS;
                    boolean bl2 = bl;
                    unsigned8.set((short)(bl2 ? 1 : 0));
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rl = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rl.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob data = this.inner(new Blob(8192));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessDataJwsJwe {

        public static final class JsonOperation
        extends Enum<JsonOperation> {
            public static final /* enum */ JsonOperation rm = new JsonOperation();
            public static final /* enum */ JsonOperation rn = new JsonOperation();
            private static final /* synthetic */ JsonOperation[] ro;

            static {
                JsonOperation[] arrjsonOperation = new JsonOperation[]{rm, rn};
                ro = arrjsonOperation;
            }

            public static JsonOperation valueOf(String string) {
                return (JsonOperation)Enum.valueOf(JsonOperation.class, (String)string);
            }

            public static JsonOperation[] values() {
                return (JsonOperation[])ro.clone();
            }
        }

        public static class Request
        extends TACommandRequest {
            Request(JsonOperation jsonOperation, byte[] arrby, byte[] arrby2, List<byte[]> list) {
                byte[] arrby3 = new a(jsonOperation.ordinal(), arrby, arrby2, list).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 25, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob data = this.inner(new Blob(7168));
                Struct.Unsigned32 rp = new Struct.Unsigned32();
                Blob rq = this.inner(new Blob(128));
                b rr = this.inner(new b(3));

                public a() {
                }

                public a(int n2, byte[] arrby, byte[] arrby2, List<byte[]> list) {
                    if (list.size() > 3) {
                        Log.e((String)"SPAY:AmexCommands", (String)"TSP Encryption Certificate Chain is longer than maximum allowed (3)");
                        throw new AmexTAException(-1);
                    }
                    this.rp.set(n2);
                    this.rr.setData(list);
                    this.data.setData(arrby2);
                    this.rq.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
        }

    }

    public static class ProcessRequestData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(List<byte[]> list, byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new a(list, arrby, arrby2).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 2, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob pU = this.inner(new Blob(8192));
                Blob pV = this.inner(new Blob(8192));
                b rr = this.inner(new b(3));

                public a() {
                }

                public a(List<byte[]> list, byte[] arrby, byte[] arrby2) {
                    if (list.size() > 3) {
                        Log.e((String)"SPAY:AmexCommands", (String)"TSP Encryption Certificate Chain is longer than maximum allowed (3)");
                        throw new AmexTAException(-1);
                    }
                    this.rr.setData(list);
                    this.pU.setData(arrby);
                    this.pV.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rs = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rs.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob pW = this.inner(new Blob(8192));
                Blob pX = this.inner(new Blob(512));
                Blob pY = this.inner(new Blob(256));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessResponseData {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 4, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob pZ = this.inner(new Blob(4096));
                Blob qa = this.inner(new Blob(512));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.pZ.setData(arrby);
                    this.qa.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rt = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rt.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob qb = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ReqMST {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 36, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob track1 = this.inner(new Blob(1024));
                Blob track2 = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.track1.setData(arrby);
                    this.track2.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a ru = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.ru.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned16 qT = new Struct.Unsigned16();
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
                Blob rv = this.inner(new Blob(64));
            }

        }

    }

    public static class Sign {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 35, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob rw = this.inner(new Blob(256));
                Blob rx = this.inner(new Blob(8));

                public a() {
                }

                public a(byte[] arrby, byte[] arrby2) {
                    this.rw.setData(arrby);
                    this.rx.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a ry = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.ry.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8 rA = new Struct.Unsigned8();
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
                Blob rz = this.inner(new Blob(128));
            }

        }

    }

    public static class TransmitMstData {

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby) {
                byte[] arrby2 = new a(n2, arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 39, arrby2);
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
            public a rB = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rB.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class UnWrap {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, boolean bl) {
                byte[] arrby2 = new a(arrby, bl).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 20, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qQ = this.inner(new Blob(16384));
                Struct.Unsigned8 rC = new Struct.Unsigned8();

                public a() {
                }

                /*
                 * Enabled aggressive block sorting
                 */
                public a(byte[] arrby, boolean bl) {
                    this.qQ.setData(arrby);
                    Struct.Unsigned8 unsigned8 = this.rC;
                    boolean bl2 = bl;
                    unsigned8.set((short)(bl2 ? 1 : 0));
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rD = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rD.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob qN = this.inner(new Blob(16384));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Update {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 23, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob rE = this.inner(new Blob(8192));

                public a() {
                }

                public a(byte[] arrby) {
                    this.rE.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rF = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rF.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class UpdateSecureChannel {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 19, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob rG = this.inner(new Blob(12352));

                public a() {
                }

                public a(byte[] arrby) {
                    this.rG.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rH = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rH.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob rI = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Verify {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 38, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qR = this.inner(new Blob(4096));

                public a() {
                }

                public a(byte[] arrby) {
                    this.qR.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rJ = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rJ.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob rK = this.inner(new Blob(1));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class Wrap {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 31, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob qN = this.inner(new Blob(16384));

                public a() {
                }

                public a(byte[] arrby) {
                    this.qN.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a rL = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.rL.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob qQ = this.inner(new Blob(16384));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class a
    extends Blob {
        public a() {
            super(4096);
        }
    }

    public static class b
    extends TAStruct {
        Struct.Unsigned32 numCerts = new Struct.Unsigned32();
        a[] qM;

        public b(int n2) {
            this.qM = new a[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                this.qM[i2] = this.inner(new a());
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setData(List<byte[]> list) {
            if (list != null && list.size() > this.qM.length) {
                Log.e((String)"SPAY:AmexCommands", (String)"Error: Can set Cert Chain more than the size of the destination Cert Chain");
                return;
            } else {
                if (list != null && list.size() > 0) {
                    for (int i2 = 0; i2 < list.size(); ++i2) {
                        this.qM[i2].setData((byte[])list.get(i2));
                    }
                }
                if (list == null) return;
                {
                    this.numCerts.set(list.size());
                    return;
                }
            }
        }
    }

}

