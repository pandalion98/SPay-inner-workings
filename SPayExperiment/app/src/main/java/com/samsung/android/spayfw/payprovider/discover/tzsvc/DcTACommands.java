/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandResponse
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.spay.TACommandResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTaCommandRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTaCommandResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.d;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import java.util.List;
import javolution.io.Struct;

public class DcTACommands
extends TACommands {
    public static final byte[] TL_MAGIC_NUM = new byte[]{68, 67, 84, 65};

    protected DcTACommands() {
        this.addCommandInfo(257, new TACommands.CommandInfo(CardCtxEncryption.getMaxRequestSize(), CardCtxEncryption.getMaxResponseSize()));
        this.addCommandInfo(265, new TACommands.CommandInfo(DevicePublicKeyCtxEncryption.getMaxRequestSize(), DevicePublicKeyCtxEncryption.getMaxResponseSize()));
        this.addCommandInfo(258, new TACommands.CommandInfo(ProcessCardProfile.getMaxRequestSize(), ProcessCardProfile.getMaxResponseSize()));
        this.addCommandInfo(266, new TACommands.CommandInfo(ReplenishContextEncryption.getMaxRequestSize(), ReplenishContextEncryption.getMaxResponseSize()));
        this.addCommandInfo(267, new TACommands.CommandInfo(ProcessReplenishmentData.getMaxRequestSize(), ProcessReplenishmentData.getMaxResponseSize()));
        this.addCommandInfo(268, new TACommands.CommandInfo(ProcessDataGeneric.getMaxRequestSize(), ProcessDataGeneric.getMaxResponseSize()));
        this.addCommandInfo(512, new TACommands.CommandInfo(DiscoverTAGetNonceCommand.getMaxRequestSize(), DiscoverTAGetNonceCommand.getMaxResponseSize()));
        this.addCommandInfo(513, new TACommands.CommandInfo(DiscoverTAAuthenticateTransaction.getMaxRequestSize(), DiscoverTAAuthenticateTransaction.getMaxResponseSize()));
        this.addCommandInfo(514, new TACommands.CommandInfo(DiscoverTAComputeDcvv.getMaxRequestSize(), DiscoverTAComputeDcvv.getMaxResponseSize()));
        this.addCommandInfo(515, new TACommands.CommandInfo(DiscoverTAComputeAppCryptogram.getMaxRequestSize(), DiscoverTAComputeAppCryptogram.getMaxResponseSize()));
        this.addCommandInfo(516, new TACommands.CommandInfo(DiscoverTAInitiateTransaction.getMaxRequestSize(), DiscoverTAInitiateTransaction.getMaxResponseSize()));
        this.addCommandInfo(517, new TACommands.CommandInfo(DiscoverTATransmitMst.getMaxRequestSize(), DiscoverTATransmitMst.getMaxResponseSize()));
        this.addCommandInfo(518, new TACommands.CommandInfo(DiscoverTAComposeMST.getMaxRequestSize(), DiscoverTAComposeMST.getMaxResponseSize()));
        this.addCommandInfo(519, new TACommands.CommandInfo(DiscoverTAClearMST.getMaxRequestSize(), DiscoverTAClearMST.getMaxResponseSize()));
        this.addCommandInfo(269, new TACommands.CommandInfo(ProcessAcctTxns.getMaxRequestSize(), ProcessAcctTxns.getMaxResponseSize()));
        this.addCommandInfo(520, new TACommands.CommandInfo(DiscoverTAGetInAppData.getMaxRequestSize(), DiscoverTAGetInAppData.getMaxResponseSize()));
        this.addCommandInfo(521, new TACommands.CommandInfo(DiscoverTAGetInAppCnccData.getMaxRequestSize(), DiscoverTAGetInAppCnccData.getMaxResponseSize()));
    }

    public static class CardCtxEncryption {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a wz = new a();

            public Request(byte[] arrby, List<byte[]> list) {
                this.wz.a(arrby, (List<byte[]>)list);
                this.mRequest = this.wz.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 257, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wz.wB.getData().length != 0 && this.wz.wA.getCount() != 0;
            }

            public static class a
            extends TAStruct {
                b wA = this.inner(new b(3));
                Blob wB = this.inner(new Blob(4096));

                private void a(byte[] arrby, List<byte[]> list) {
                    this.wA.setData(list);
                    this.wB.setData(arrby);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                Blob encryptedData = this.inner(new Blob(4096));

                public byte[] getEncryptedData() {
                    return this.encryptedData.getData();
                }
            }

        }

    }

    public static class DevicePublicKeyCtxEncryption {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a wD = new a();

            public Request(byte[] arrby) {
                this.wD.setData(arrby);
                this.mRequest = this.wD.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 265, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wD.wE.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Blob wE = this.inner(new Blob(4096));

                private void setData(byte[] arrby) {
                    this.wE.setData(arrby);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                Blob encryptedData = this.inner(new Blob(4096));

                public byte[] getEncryptedData() {
                    return this.encryptedData.getData();
                }
            }

        }

    }

    public static class DiscoverTAAuthenticateTransaction {
        public static int getMaxRequestSize() {
            return new DiscoverTAAuthenticateTransactionRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new d().size();
        }

        public static class DiscoverTAAuthenticateTransactionRequest
        extends DcTaCommandRequest {
            private a wF = new a();

            public DiscoverTAAuthenticateTransactionRequest(byte[] arrby) {
                this.wF.setData(arrby);
                this.mRequest = this.wF.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 513, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wF._taSecureObject.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Blob _taSecureObject = this.inner(new Blob(4096));

                public void setData(byte[] arrby) {
                    this._taSecureObject.setData(arrby);
                }
            }

        }

        public static class DiscoverTAAuthenticateTransactionResponse
        extends DcTaCommandResponse {
            public DiscoverTAAuthenticateTransactionResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new d();
                this.yX.deserialize(this.mResponse);
            }

            public long ek() {
                return this.yX.return_code.get();
            }
        }

    }

    public static class DiscoverTAClearMST {
        public static int getMaxRequestSize() {
            return new DiscoverTAClearMSTRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new d().size();
        }

        public static class DiscoverTAClearMSTRequest
        extends DcTaCommandRequest {
            private a wG;

            DiscoverTAClearMSTRequest(int n2) {
                this.wG = new a(n2);
                this.mRequest = this.wG.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 519, this.mRequest);
            }

            @Override
            boolean validate() {
                return true;
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

        public static class DiscoverTAClearMSTResponse
        extends DcTaCommandResponse {
            public DiscoverTAClearMSTResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new d();
                this.yX.deserialize(this.mResponse);
            }

            public long el() {
                return this.yX.return_code.get();
            }
        }

    }

    public static class DiscoverTAComposeMST {
        public static int getMaxRequestSize() {
            return new DiscoverTAComposeMSTRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new d().size();
        }

        public static class DiscoverTAComposeMSTRequest
        extends DcTaCommandRequest {
            private a wH;

            public DiscoverTAComposeMSTRequest(int n2) {
                this.wH = new a(n2);
                this.mRequest = this.wH.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 518, this.mRequest);
            }

            @Override
            boolean validate() {
                return true;
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

        public static class DiscoverTAComposeMSTResponse
        extends DcTaCommandResponse {
            public DiscoverTAComposeMSTResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new d();
                this.yX.deserialize(this.mResponse);
            }

            public long em() {
                return this.yX.return_code.get();
            }
        }

    }

    public static class DiscoverTAComputeAppCryptogram {
        public static int getMaxRequestSize() {
            return new DiscoverTAComputeAppCryptogramRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new DiscoverTAComputeAppCryptogramResponse.a().size();
        }

        public static class DiscoverTAComputeAppCryptogramRequest
        extends DcTaCommandRequest {
            private a wI = new a();

            public DiscoverTAComputeAppCryptogramRequest(byte[] arrby, byte[] arrby2) {
                this.wI.setData(arrby, arrby2);
                this.mRequest = this.wI.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 515, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wI.wJ.getData().length != 0 && this.wI.wK.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Blob wJ = this.inner(new Blob(256));
                Blob wK = this.inner(new Blob(256));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this.wJ.setData(arrby);
                    this.wK.setData(arrby2);
                }
            }

        }

        public static class DiscoverTAComputeAppCryptogramResponse
        extends DcTaCommandResponse {
            public DiscoverTAComputeAppCryptogramResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public byte[] getCryptogram() {
                return ((a)this.yX).wL.getData();
            }

            public static class a
            extends d {
                public Blob wL = this.inner(new Blob(8));
            }

        }

    }

    public static class DiscoverTAComputeDcvv {
        public static int getMaxRequestSize() {
            return new DiscoverTAComputeDcvvRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new DiscoverTAComputeDcvvResponse.a().size();
        }

        public static class DiscoverTAComputeDcvvRequest
        extends DcTaCommandRequest {
            private a wM = new a();

            public DiscoverTAComputeDcvvRequest(byte[] arrby) {
                this.wM.setData(arrby);
                this.mRequest = this.wM.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 514, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wM.wN.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Blob wN = this.inner(new Blob(4));

                public void setData(byte[] arrby) {
                    this.wN.setData(arrby);
                }
            }

        }

        public static class DiscoverTAComputeDcvvResponse
        extends DcTaCommandResponse {
            public DiscoverTAComputeDcvvResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                public Struct.Unsigned32 wO = new Struct.Unsigned32();
            }

        }

    }

    public static class DiscoverTAGetInAppCnccData {
        public static int getMaxRequestSize() {
            return new DiscoverTAGetInAppCnccDataRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new DiscoverTAGetInAppCnccDataResponse.a().size();
        }

        public static class DiscoverTAGetInAppCnccDataRequest
        extends DcTaCommandRequest {
            private a wP = new a();

            public DiscoverTAGetInAppCnccDataRequest(byte[] arrby, byte[] arrby2) {
                this.wP.setData(arrby, arrby2);
                this.mRequest = this.wP.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 521, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wP.wQ.getData().length != 0 && this.wP.wR.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Blob wQ = this.inner(new Blob(4096));
                Blob wR = this.inner(new Blob(32));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this.wQ.setData(arrby);
                    this.wR.setData(arrby2);
                }
            }

        }

        public static class DiscoverTAGetInAppCnccDataResponse
        extends DcTaCommandResponse {
            public DiscoverTAGetInAppCnccDataResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public byte[] getPayload() {
                return ((a)this.yX).wT.getData();
            }

            public static class a
            extends d {
                public Blob wT = this.inner(new Blob(8192));
            }

        }

    }

    public static class DiscoverTAGetInAppData {
        public static int getMaxRequestSize() {
            return new DiscoverTAGetInAppDataRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new DiscoverTAGetInAppDataResponse.a().size();
        }

        public static class DiscoverTAGetInAppDataRequest
        extends DcTaCommandRequest {
            private a wU = new a();

            public DiscoverTAGetInAppDataRequest(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                this.wU.setData(arrby, arrby2, arrby3);
                this.mRequest = this.wU.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 520, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wU.wV.getData().length != 0 && this.wU._taSecureObject.getData().length != 0 && this.wU.wW.getData().length == 6;
            }

            public static class a
            extends TAStruct {
                Blob _taSecureObject = this.inner(new Blob(4096));
                Blob wV = this.inner(new Blob(4096));
                Blob wW = this.inner(new Blob(6));

                public void setData(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this._taSecureObject.setData(arrby2);
                    this.wV.setData(arrby);
                    this.wW.setData(arrby3);
                }
            }

        }

        public static class DiscoverTAGetInAppDataResponse
        extends DcTaCommandResponse {
            public DiscoverTAGetInAppDataResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public byte[] en() {
                return ((a)this.yX).wV.getData();
            }

            public byte[] getPayload() {
                return ((a)this.yX).wX.getData();
            }

            public long getRemainingOtpkCount() {
                return ((a)this.yX).wY.get();
            }

            public static class a
            extends d {
                public Blob wV = this.inner(new Blob(4096));
                public Blob wX = this.inner(new Blob(20));
                public Struct.Unsigned32 wY = new Struct.Unsigned32();
            }

        }

    }

    public static class DiscoverTAGetNonceCommand {
        public static int getMaxRequestSize() {
            return new DiscoverTAGetNonceRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new DiscoverTAGetNonceResponse.a().size();
        }

        public static class DiscoverTAGetNonceRequest
        extends DcTaCommandRequest {
            private a wZ = new a();

            public DiscoverTAGetNonceRequest(byte[] arrby) {
                this.wZ.setData(arrby);
                this.mRequest = this.wZ.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 512, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.wZ._taPaymentProfile.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 _nonceSize = new Struct.Unsigned32();
                Blob _taPaymentProfile = this.inner(new Blob(4096));

                public void setData(byte[] arrby) {
                    this._nonceSize.set(32L);
                    this._taPaymentProfile.setData(arrby);
                }
            }

        }

        public static class DiscoverTAGetNonceResponse
        extends DcTaCommandResponse {
            public DiscoverTAGetNonceResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public byte[] getNonce() {
                return ((a)this.yX)._nonce.getData();
            }

            public static class a
            extends d {
                public Blob _nonce = this.inner(new Blob(4096));
            }

        }

    }

    public static class DiscoverTAInitiateTransaction {
        public static int getMaxRequestSize() {
            return new DiscoverTAInitiateTransactionRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new DiscoverTAInitiateTransactionResponse.a().size();
        }

        public static class DiscoverTAInitiateTransactionRequest
        extends DcTaCommandRequest {
            private a xa = new a();

            public DiscoverTAInitiateTransactionRequest(byte[] arrby, byte[] arrby2) {
                this.xa.setData(arrby, arrby2);
                this.mRequest = this.xa.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 516, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xa._taSecureObject.getData().length != 0 && this.xa.wV.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                public Blob _taSecureObject = this.inner(new Blob(4096));
                public Blob wV = this.inner(new Blob(4096));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this._taSecureObject.setData(arrby);
                    this.wV.setData(arrby2);
                }
            }

        }

        public static class DiscoverTAInitiateTransactionResponse
        extends DcTaCommandResponse {
            public DiscoverTAInitiateTransactionResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                public Struct.Unsigned32 wY = new Struct.Unsigned32();
                public Blob xb = this.inner(new Blob(4096));
                public Blob xc = this.inner(new Blob(2));
            }

        }

    }

    public static class DiscoverTATransmitMst {
        public static int getMaxRequestSize() {
            return new DiscoverTATransmitMstRequest.a().size();
        }

        public static int getMaxResponseSize() {
            return new d().size();
        }

        public static class DiscoverTATransmitMstRequest
        extends DcTaCommandRequest {
            private a xd = new a();

            public DiscoverTATransmitMstRequest(int n2, byte[] arrby) {
                this.xd.setData(n2, arrby);
                this.mRequest = this.xd.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 517, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xd._taMSTConfig.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 _taBaudRateInUs = new Struct.Unsigned32();
                Blob _taMSTConfig = this.inner(new Blob(28));

                public void setData(int n2, byte[] arrby) {
                    this._taBaudRateInUs.set(n2);
                    this._taMSTConfig.setData(arrby);
                }
            }

        }

        public static class DiscoverTATransmitMstResponse
        extends DcTaCommandResponse {
            public DiscoverTATransmitMstResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new d();
                this.yX.deserialize(this.mResponse);
            }

            public long eo() {
                return this.yX.return_code.get();
            }
        }

    }

    public static class ProcessAcctTxns {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a xe = new a();

            public Request(byte[] arrby, byte[] arrby2, List<byte[]> list) {
                this.xe.a(arrby, arrby2, (List<byte[]>)list);
                this.mRequest = this.xe.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 269, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xe.xf.getData().length != 0 && this.xe.xh.getData().length != 0 && this.xe.xg.getCount() != 0;
            }

            public static class a
            extends TAStruct {
                Blob xf = this.inner(new Blob(4096));
                b xg = this.inner(new b(3));
                Blob xh = this.inner(new Blob(40960));

                private void a(byte[] arrby, byte[] arrby2, List<byte[]> list) {
                    this.xf.setData(arrby2);
                    this.xg.setData(list);
                    this.xh.setData(arrby);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public String ep() {
                return new String(((a)this.yX).xh.getData());
            }

            public static class a
            extends d {
                Blob xh = this.inner(new Blob(40960));
            }

        }

    }

    public static class ProcessCardProfile {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a xi = new a();

            public Request(byte[] arrby, byte[] arrby2, List<byte[]> list, boolean bl) {
                this.xi.a(arrby, arrby2, (List<byte[]>)list, bl);
                this.mRequest = this.xi.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 258, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xi.xj.getData().length != 0 && this.xi.xf.getData().length != 0 && this.xi.xg.getCount() != 0;
            }

            public static class a
            extends TAStruct {
                Blob xf = this.inner(new Blob(4096));
                b xg = this.inner(new b(3));
                Blob xj = this.inner(new Blob(40960));
                Struct.Unsigned32 xk = new Struct.Unsigned32();

                private void a(byte[] arrby, byte[] arrby2, List<byte[]> list, boolean bl) {
                    this.xj.setData(arrby);
                    this.xf.setData(arrby2);
                    this.xg.setData(list);
                    if (bl) {
                        this.xk.set(1L);
                        return;
                    }
                    this.xk.set(0L);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                Struct.UTF8String xl = new Struct.UTF8String(40960);
                Blob xm = this.inner(new Blob(4096));
                Blob xn = this.inner(new Blob(4096));
                Struct.Unsigned32 xo = new Struct.Unsigned32();

                public byte[] eq() {
                    return this.xn.getData();
                }

                public String er() {
                    return this.xl.get();
                }

                public int es() {
                    return (int)this.xo.get();
                }

                public byte[] getEncryptedData() {
                    return this.xm.getData();
                }
            }

        }

    }

    public static final class ProcessDataDataType
    extends Enum<ProcessDataDataType> {
        public static final /* enum */ ProcessDataDataType xp = new ProcessDataDataType(51);
        public static final /* enum */ ProcessDataDataType xq = new ProcessDataDataType(52);
        private static final /* synthetic */ ProcessDataDataType[] xr;
        private short mValue;

        static {
            ProcessDataDataType[] arrprocessDataDataType = new ProcessDataDataType[]{xp, xq};
            xr = arrprocessDataDataType;
        }

        private ProcessDataDataType(short s2) {
            this.mValue = s2;
        }

        public static ProcessDataDataType valueOf(String string) {
            return (ProcessDataDataType)Enum.valueOf(ProcessDataDataType.class, (String)string);
        }

        public static ProcessDataDataType[] values() {
            return (ProcessDataDataType[])xr.clone();
        }

        public short et() {
            return this.mValue;
        }
    }

    public static class ProcessDataGeneric {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a xs = new a();

            public Request(ProcessDataOperationType processDataOperationType, ProcessDataDataType processDataDataType, byte[] arrby, byte[] arrby2, String string) {
                this.xs.a(processDataOperationType, processDataDataType, arrby, arrby2, string);
                this.mRequest = this.xs.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 268, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xs.xv.getData().length != 0 && this.xs.xf.getData().length != 0;
            }

            public static class a
            extends TAStruct {
                Blob xf = this.inner(new Blob(4096));
                Struct.Unsigned8 xt = new Struct.Unsigned8();
                Struct.Unsigned8 xu = new Struct.Unsigned8();
                Blob xv = this.inner(new Blob(40960));

                public void a(ProcessDataOperationType processDataOperationType, ProcessDataDataType processDataDataType, byte[] arrby, byte[] arrby2, String string) {
                    this.xt.set(processDataOperationType.et());
                    this.xu.set(processDataDataType.et());
                    this.xv.setData(arrby);
                    this.xf.setData(arrby2);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public byte[] getSignature() {
                return ((a)this.yX).xw.getData();
            }

            public static class a
            extends d {
                Blob xw = this.inner(new Blob(40960));
            }

        }

    }

    public static final class ProcessDataOperationType
    extends Enum<ProcessDataOperationType> {
        public static final /* enum */ ProcessDataOperationType xx = new ProcessDataOperationType(10);
        public static final /* enum */ ProcessDataOperationType xy = new ProcessDataOperationType(11);
        private static final /* synthetic */ ProcessDataOperationType[] xz;
        private final short mValue;

        static {
            ProcessDataOperationType[] arrprocessDataOperationType = new ProcessDataOperationType[]{xx, xy};
            xz = arrprocessDataOperationType;
        }

        private ProcessDataOperationType(short s2) {
            this.mValue = s2;
        }

        public static ProcessDataOperationType valueOf(String string) {
            return (ProcessDataOperationType)Enum.valueOf(ProcessDataOperationType.class, (String)string);
        }

        public static ProcessDataOperationType[] values() {
            return (ProcessDataOperationType[])xz.clone();
        }

        public short et() {
            return this.mValue;
        }
    }

    public static class ProcessReplenishmentData {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a xA = new a();

            public Request(byte[] arrby, byte[] arrby2, byte[] arrby3, List<byte[]> list, boolean bl) {
                this.xA.a(arrby, arrby2, arrby3, (List<byte[]>)list, bl);
                this.mRequest = this.xA.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 267, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xA.xB.getData().length != 0 && this.xA.xf.getData().length != 0 && this.xA.xn.getData().length != 0 && this.xA.xg.getCount() != 0;
            }

            public static class a
            extends TAStruct {
                Struct.Unsigned32 mIsEncrypted = new Struct.Unsigned32();
                Blob xB = this.inner(new Blob(40960));
                Blob xf = this.inner(new Blob(4096));
                b xg = this.inner(new b(3));
                Blob xn = this.inner(new Blob(4096));

                private void a(byte[] arrby, byte[] arrby2, byte[] arrby3, List<byte[]> list, boolean bl) {
                    this.xB.setData(arrby);
                    this.xf.setData(arrby3);
                    this.xn.setData(arrby2);
                    this.xg.setData(list);
                    if (bl) {
                        this.mIsEncrypted.set(1L);
                        return;
                    }
                    this.mIsEncrypted.set(0L);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                Struct.UTF8String xl = new Struct.UTF8String(40960);
                Blob xm = this.inner(new Blob(4096));
                Blob xn = this.inner(new Blob(4096));
                Struct.Unsigned32 xo = new Struct.Unsigned32();

                public byte[] eq() {
                    return this.xn.getData();
                }

                public int es() {
                    return (int)this.xo.get();
                }
            }

        }

    }

    public static class ReplenishContextEncryption {
        public static int getMaxRequestSize() {
            return new Request.a().size();
        }

        public static int getMaxResponseSize() {
            return new Response.a().size();
        }

        public static class Request
        extends DcTaCommandRequest {
            private a xC = new a();

            public Request(byte[] arrby, byte[] arrby2, List<byte[]> list, String string) {
                this.xC.a(arrby, arrby2, (List<byte[]>)list, string);
                this.mRequest = this.xC.serialize();
                this.init(1, DcTACommands.TL_MAGIC_NUM, 266, this.mRequest);
            }

            @Override
            public boolean validate() {
                return this.xC.xn.getData().length != 0 && this.xC.xD.getData().length != 0 && this.xC.wA.getCount() != 0;
            }

            public static class a
            extends TAStruct {
                b wA = this.inner(new b(3));
                Blob xD = this.inner(new Blob(4096));
                Blob xn = this.inner(new Blob(4096));

                private void a(byte[] arrby, byte[] arrby2, List<byte[]> list, String string) {
                    this.xn.setData(arrby);
                    this.xD.setData(arrby2);
                    this.wA.setData(list);
                }
            }

        }

        public static class Response
        extends DcTaCommandResponse {
            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse);
                this.yX = new a();
                this.yX.deserialize(this.mResponse);
            }

            public static class a
            extends d {
                Blob encryptedData = this.inner(new Blob(4096));

                public byte[] getEncryptedData() {
                    return this.encryptedData.getData();
                }
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
        a[] wC;

        public b(int n2) {
            this.wC = new a[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                this.wC[i2] = this.inner(new a());
            }
        }

        public int getCount() {
            return (int)this.numCerts.get();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setData(List<byte[]> list) {
            block6 : {
                block5 : {
                    if (list != null && list.size() > this.wC.length) break block5;
                    if (list != null && list.size() > 0) {
                        for (int i2 = 0; i2 < list.size(); ++i2) {
                            this.wC[i2].setData((byte[])list.get(i2));
                        }
                    }
                    if (list != null) break block6;
                }
                return;
            }
            this.numCerts.set(list.size());
        }
    }

}

