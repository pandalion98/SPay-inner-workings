/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.SPayTAAuthCommands;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.c;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.KeyValPairs;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import java.util.List;
import java.util.Map;
import javolution.io.Struct;

public class AmexCommands
extends SPayTAAuthCommands {
    public AmexCommands() {
        this.addCommandInfo(1, new TACommands.CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        this.addCommandInfo(2, new TACommands.CommandInfo(GenerateECDHKeys.getMaxRequestSize(), GenerateECDHKeys.getMaxResponseSize()));
        this.addCommandInfo(3, new TACommands.CommandInfo(ProcessRequestData.getMaxRequestSize(), ProcessRequestData.getMaxResponseSize()));
        this.addCommandInfo(5, new TACommands.CommandInfo(ProcessTokenData.getMaxRequestSize(), ProcessTokenData.getMaxResponseSize()));
        this.addCommandInfo(6, new TACommands.CommandInfo(DecryptTokenData.getMaxRequestSize(), DecryptTokenData.getMaxResponseSize()));
        this.addCommandInfo(8, new TACommands.CommandInfo(SuspendToken.getMaxRequestSize(), SuspendToken.getMaxResponseSize()));
        this.addCommandInfo(9, new TACommands.CommandInfo(ResumeToken.getMaxRequestSize(), ResumeToken.getMaxResponseSize()));
        this.addCommandInfo(10, new TACommands.CommandInfo(ProcessTransaction.getMaxRequestSize(), ProcessTransaction.getMaxResponseSize()));
        this.addCommandInfo(11, new TACommands.CommandInfo(GetNFCCryptogram.getMaxRequestSize(), GetNFCCryptogram.getMaxResponseSize()));
        this.addCommandInfo(12, new TACommands.CommandInfo(TransmitMstData.getMaxRequestSize(), TransmitMstData.getMaxResponseSize()));
        this.addCommandInfo(13, new TACommands.CommandInfo(ClearLUPC.getMaxRequestSize(), ClearLUPC.getMaxResponseSize()));
    }

    public static class ActivateToken {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new a(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 7, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob pC = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby) {
                    this.pC.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pC = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ClearLUPC {
        public static int getMaxRequestSize() {
            return 0;
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request() {
                this.init(0, AmexCommands.TL_MAGIC_NUM, 13, null);
            }
        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class DecryptTokenData {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 6, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob pE = this.inner(new Blob(2048));

                public a() {
                }

                public a(byte[] arrby) {
                    this.pE.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob blob = this.inner(new Blob(2048));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GenerateECDHKeys {
        public static int getMaxRequestSize() {
            return 0;
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request() {
                this.init(0, TACommands.TL_MAGIC_NUM, 2, null);
            }
        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pF = this.inner(new Blob(128));
                Blob pG = this.inner(new Blob(256));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GenerateInAppJwePaymentPayload {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, Map<byte[], byte[]> map, List<byte[]> list) {
                byte[] arrby2 = new a(arrby, map, list).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 15, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob pH = this.inner(new Blob(8));
                b pI = this.inner(new b(3));
                KeyValPairs pJ = this.inner(new KeyValPairs(10, 64, 256));

                public a() {
                }

                public a(byte[] arrby, Map<byte[], byte[]> map, List<byte[]> list) {
                    this.pH.setData(arrby);
                    this.pJ.setData(map);
                    this.pI.setData(list);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pK = this.inner(new Blob(5120));
                Blob pL = this.inner(new Blob(64));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GenerateInAppPaymentPayload {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, Map<byte[], byte[]> map, byte[] arrby2) {
                byte[] arrby3 = new a(arrby, map, arrby2).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 14, arrby3);
            }

            public static class a
            extends TAStruct {
                Blob nonce = this.inner(new Blob(32));
                Blob pH = this.inner(new Blob(8));
                KeyValPairs pJ = this.inner(new KeyValPairs(10, 64, 256));

                public a() {
                }

                public a(byte[] arrby, Map<byte[], byte[]> map, byte[] arrby2) {
                    this.pH.setData(arrby);
                    this.pJ.setData(map);
                    this.nonce.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pK = this.inner(new Blob(5120));
                Blob pL = this.inner(new Blob(64));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GetNFCCryptogram {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, int n3, Map<byte[], byte[]> map) {
                byte[] arrby = new a(n2, n3, map).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 11, arrby);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8 pM = new Struct.Unsigned8();
                Struct.Unsigned8 pN = new Struct.Unsigned8();
                KeyValPairs pO = this.inner(new KeyValPairs(10, 64, 256));

                public a() {
                }

                public a(int n2, int n3, Map<byte[], byte[]> map) {
                    this.pM.set((short)n2);
                    this.pN.set((short)n3);
                    this.pO.setData(map);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pP = this.inner(new Blob(8));
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
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob deviceRootRSA2048PubCert = this.inner(new Blob(4096));
                Blob deviceSignRSA2048PubCert = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pR = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessData {

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, boolean bl) {
                byte[] arrby2 = new a(arrby, bl).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 61441, arrby2);
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
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Blob data = this.inner(new Blob(8192));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

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
                this.init(0, TACommands.TL_MAGIC_NUM, 3, arrby3);
            }

            public static class a
            extends TAStruct {
                b pT = this.inner(new b(3));
                Blob pU = this.inner(new Blob(8192));
                Blob pV = this.inner(new Blob(8192));

                public a() {
                }

                public a(List<byte[]> list, byte[] arrby, byte[] arrby2) {
                    if (list.size() > 3) {
                        com.samsung.android.spayfw.b.c.e("AmexCommands", "TSP Encryption Certificate Chain is longer than maximum allowed (3)");
                        throw new AmexTAException(983040);
                    }
                    this.pT.setData(list);
                    this.pU.setData(arrby);
                    this.pV.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pW = this.inner(new Blob(8192));
                Blob pX = this.inner(new Blob(512));
                Blob pY = this.inner(new Blob(256));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessResponseData {

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
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob qb = this.inner(new Blob(4096));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessTokenData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(List<byte[]> list, byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6) {
                byte[] arrby7 = new a(list, arrby, arrby2, arrby3, arrby4, arrby5, arrby6).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 5, arrby7);
            }

            public static class a
            extends TAStruct {
                Blob pG = this.inner(new Blob(256));
                b qc = this.inner(new b(2));
                Struct.Unsigned8[] qd = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[16]);
                Blob qe = this.inner(new Blob(4096));
                Blob qf = this.inner(new Blob(2048));
                Blob qg = this.inner(new Blob(2));
                Blob qh = this.inner(new Blob(64));

                public a() {
                }

                public a(List<byte[]> list, byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6) {
                    if (list.size() > 2) {
                        com.samsung.android.spayfw.b.c.e("AmexCommands", "TSP ECC Certificate Chain is longer than maximum allowed (2)");
                        throw new AmexTAException(983040);
                    }
                    this.qc.setData(list);
                    c.setUnsigned8ArrayData(this.qd, arrby);
                    this.qe.setData(arrby2);
                    this.qf.setData(arrby3);
                    this.qg.setData(arrby4);
                    this.qh.setData(arrby5);
                    this.pG.setData(arrby6);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pE = this.inner(new Blob(2048));
                Blob qi = this.inner(new Blob(1024));
                Blob qj = this.inner(new Blob(2048));
                Blob qk = this.inner(new Blob(1024));
                Blob ql = this.inner(new Blob(1024));
                Blob qm = this.inner(new Blob(512));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessTransaction {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5) {
                byte[] arrby6 = new a(n2, arrby, arrby2, arrby3, arrby4, arrby5).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 10, arrby6);
            }

            public static class a
            extends TAStruct {
                Blob pC = this.inner(new Blob(1024));
                Blob pE = this.inner(new Blob(2048));
                Struct.Unsigned8 pM = new Struct.Unsigned8();
                Blob qi = this.inner(new Blob(1024));
                Blob qj = this.inner(new Blob(2048));
                Blob qn = this.inner(new Blob(1024));

                public a() {
                }

                public a(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5) {
                    this.pM.set((short)n2);
                    this.pE.setData(arrby);
                    this.qi.setData(arrby2);
                    this.qj.setData(arrby3);
                    this.pC.setData(arrby4);
                    this.qn.setData(arrby5);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob qi = this.inner(new Blob(1024));
                Blob qj = this.inner(new Blob(2048));
                Blob qn = this.inner(new Blob(1024));
                Blob qo = this.inner(new Blob(5));
                Blob qp = this.inner(new Blob(64));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ResumeToken {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 9, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob pC = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby) {
                    this.pC.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pC = this.inner(new Blob(1024));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class SuspendToken {
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
                this.init(0, TACommands.TL_MAGIC_NUM, 8, arrby2);
            }

            public static class a
            extends TAStruct {
                Blob pC = this.inner(new Blob(1024));

                public a() {
                }

                public a(byte[] arrby) {
                    this.pC.setData(arrby);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Blob pC = this.inner(new Blob(1024));
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
                this.init(0, TACommands.TL_MAGIC_NUM, 12, arrby2);
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
            public a mRetVal = new a();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
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
        a[] pD;

        public b(int n2) {
            this.pD = new a[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                this.pD[i2] = this.inner(new a());
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setData(List<byte[]> list) {
            if (list != null && list.size() > this.pD.length) {
                com.samsung.android.spayfw.b.c.e("AmexCommands", "Error: Can set Cert Chain more than the size of the destination Cert Chain");
                return;
            } else {
                if (list != null && list.size() > 0) {
                    for (int i2 = 0; i2 < list.size(); ++i2) {
                        this.pD[i2].setData((byte[])list.get(i2));
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

