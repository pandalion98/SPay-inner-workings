/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class McTACommands
extends TACommands {
    private static final int ATC_LEN = 2;
    private static final int CRYPTO_OUT_LEN = 1024;
    private static final int INPUT_DATA_LEN = 256;
    private static final int MAX_CASD_BUF_LEN = 2048;
    private static final int MAX_DSRP_PAYMENT_INFO = 4096;
    private static final int MAX_DSRP_PAYMENT_JWE_INFO = 8192;
    private static final int MAX_INAPP_NONCE_LEN = 32;
    private static final int MAX_MERCHANT_CERT = 4096;
    private static final int MAX_SECONDARY_CERT = 4096;
    private static final int MAX_SHA256_LEN = 32;
    public static final int MOP_MC_CMD_PING_TA = 1;
    public static final int MOP_MC_TA_CMD_BASE = 256;
    public static final int MOP_MC_TA_CMD_CASD_BASE = 768;
    public static final int MOP_MC_TA_CMD_CASD_GET_UID = 769;
    public static final int MOP_MC_TA_CMD_CASD_VERIFY_KEY = 771;
    public static final int MOP_MC_TA_CMD_CASD_WRITE_KEY = 770;
    public static final int MOP_MC_TA_CMD_CLEAR_MST_DATA = 264;
    public static final int MOP_MC_TA_CMD_CLEAR_SECURE_DATA = 267;
    public static final int MOP_MC_TA_CMD_CRYPTO_BASE = 512;
    public static final int MOP_MC_TA_CMD_CRYPTO_COMPUTE_CC = 515;
    public static final int MOP_MC_TA_CMD_CRYPTO_COPY_AC_KEY = 517;
    public static final int MOP_MC_TA_CMD_CRYPTO_DSRP_GEN_CNCC = 516;
    public static final int MOP_MC_TA_CMD_CRYPTO_DSRP_GEN_JWE = 518;
    public static final int MOP_MC_TA_CMD_CRYPTO_GENERATE_MAC = 514;
    public static final int MOP_MC_TA_CMD_CRYPTO_SET_CONTEXT = 513;
    public static final int MOP_MC_TA_CMD_ENCRYPT_CARD_INFO = 257;
    public static final int MOP_MC_TA_CMD_GENERATE_UN = 265;
    public static final int MOP_MC_TA_CMD_GET_NONCE = 262;
    public static final int MOP_MC_TA_CMD_GET_SPSD_INFO = 258;
    public static final int MOP_MC_TA_CMD_PING_TA = 1;
    public static final int MOP_MC_TA_CMD_PREPARE_MST_TRACKS = 261;
    public static final int MOP_MC_TA_CMD_PROCESS_MST = 260;
    public static final int MOP_MC_TA_CMD_PROCESS_SIGNATURE = 266;
    public static final int MOP_MC_TA_CMD_PROVISION_TOKEN = 259;
    public static final int MOP_MC_TA_CMD_TRANSACTION_AUTH = 263;
    private static final String TAG = "McTACommands";
    private static final int TA_AID_LEN = 16;
    private static final int TA_MSG_LEN = 128;
    private static final int TA_MST_CONFIG_LEN = 28;
    private static final int TA_NONCE_MAX_LEN = 32;
    private static final int TA_PROFILE_LEN = 4096;
    private static final int TA_SECUREOBJECT_LEN = 4096;
    private static final int TA_WRAPPED_ATC_OBJ_LEN = 256;
    public static final byte[] TL_MAGIC_NUM = new byte[]{-84, -34, -91, -37};
    public static final int TL_VERSION = 1;
    private static final int UN_LEN = 4;

    public McTACommands() {
        this.addCommandInfo(257, new TACommands.CommandInfo(CardInfoEncryption.getMaxRequestSize(), CardInfoEncryption.getMaxResponseSize()));
        this.addCommandInfo(258, new TACommands.CommandInfo(GetSpsd.getMaxRequestSize(), GetSpsd.getMaxResponseSize()));
        this.addCommandInfo(259, new TACommands.CommandInfo(ProvisionToken.getMaxRequestSize(), ProvisionToken.getMaxResponseSize()));
        this.addCommandInfo(1, new TACommands.CommandInfo(PingTA.getMaxRequestSize(), PingTA.getMaxResponseSize()));
        this.addCommandInfo(514, new TACommands.CommandInfo(TAGenerateMAC.getMaxRequestSize(), TAGenerateMAC.getMaxResponseSize()));
        this.addCommandInfo(513, new TACommands.CommandInfo(TASetContext.getMaxRequestSize(), TASetContext.getMaxResponseSize()));
        this.addCommandInfo(260, new TACommands.CommandInfo(TAProcessMST.getMaxRequestSize(), TAProcessMST.getMaxResponseSize()));
        this.addCommandInfo(263, new TACommands.CommandInfo(TATransactionAuth.getMaxRequestSize(), TATransactionAuth.getMaxResponseSize()));
        this.addCommandInfo(262, new TACommands.CommandInfo(TAGetNonce.getMaxRequestSize(), TAGetNonce.getMaxResponseSize()));
        this.addCommandInfo(265, new TACommands.CommandInfo(TAGenerateUN.getMaxRequestSize(), TAGenerateUN.getMaxResponseSize()));
        this.addCommandInfo(516, new TACommands.CommandInfo(TAEncryptDsrpCnccPaymentInfo.getMaxRequestSize(), TAEncryptDsrpCnccPaymentInfo.getMaxResponseSize()));
        this.addCommandInfo(267, new TACommands.CommandInfo(TAClearSecureData.getMaxRequestSize(), TAClearSecureData.getMaxResponseSize()));
        this.addCommandInfo(517, new TACommands.CommandInfo(TACopyACKey.getMaxRequestSize(), TACopyACKey.getMaxResponseSize()));
        this.addCommandInfo(518, new TACommands.CommandInfo(TAEncryptDsrpPaymentInfo.getMaxRequestSize(), TAEncryptDsrpPaymentInfo.getMaxResponseSize()));
    }

    public static byte[] getMagicNumber() {
        return TL_MAGIC_NUM;
    }

    public static int getVersion() {
        return 1;
    }

    public static class CardInfoEncryption {
        public static int getMaxRequestSize() {
            return new Request.CardInfoData().size();
        }

        public static int getMaxResponseSize() {
            return new Response.CardData().size();
        }

        public static class Request
        extends TACommandRequest {
            public Request(byte[] arrby, byte[] arrby2) {
                CardInfoData cardInfoData = new CardInfoData();
                cardInfoData.setData(arrby, arrby2);
                this.mRequest = cardInfoData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 257, this.mRequest);
            }

            public static class CardInfoData
            extends TAStruct {
                Blob mCardInfo = this.inner(new Blob(4096));
                Struct.Unsigned32 mIsEncrypted = new Struct.Unsigned32();
                Blob mMdesPublicCert = this.inner(new Blob(4096));

                private void setData(byte[] arrby, byte[] arrby2) {
                    this.mMdesPublicCert.setData(arrby2);
                    this.mCardInfo.setData(arrby);
                    this.mIsEncrypted.set(0L);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            private static final int MAX_ENCRYPTED_CARD_INFO_SIZE = 4096;
            private static final int MAX_ENCRYPTED_KEY_INFO_SIZE = 512;
            private static final int MAX_OAEP_HASHING_ALGORITHM_SIZE = 6;
            public CardData mRetVal = new CardData();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class CardData
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Blob encryptedData = this.inner(new Blob(4096));
                Blob encryptedKey = this.inner(new Blob(512));
                Struct.Unsigned32 mReturnCode = new Struct.Unsigned32();
                Blob oaeHashingAlgorithm = this.inner(new Blob(6));

                public byte[] getEncryptedData() {
                    return this.encryptedData.getData();
                }

                public byte[] getEncryptedKey() {
                    return this.encryptedKey.getData();
                }

                public byte[] getOaeHashingAlgorithm() {
                    return this.oaeHashingAlgorithm.getData();
                }

                public long getReturnCode() {
                    return this.mReturnCode.get();
                }
            }

        }

    }

    public static class CasdUpdateGetUid {
        public static int getMaxRequestSize() {
            return new Request.Data(null).size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 769, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob blob;

                public Data(byte[] arrby) {
                    Blob blob = this.blob = this.inner(new Blob(2048));
                    if (arrby == null) {
                        arrby = null;
                    }
                    blob.setData(arrby);
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
                Struct.Unsigned8[] hpk = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[32]);
                Struct.Unsigned8[] huid = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[32]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class CasdUpdateVerifyKey {
        public static int getMaxRequestSize() {
            return new Request.Data(null).size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 771, arrby2);
            }

            public static class Data
            extends TAStruct {
                Blob blob;

                public Data(byte[] arrby) {
                    Blob blob = this.blob = this.inner(new Blob(2048));
                    if (arrby == null) {
                        arrby = null;
                    }
                    blob.setData(arrby);
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
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class CasdUpdateWriteKey {
        public static int getMaxRequestSize() {
            return new Request.Data(null, null).size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new Data(arrby, arrby2).serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 770, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob ecasd = this.inner(new Blob(2048));
                Blob old_casd_blob = this.inner(new Blob(2048));

                /*
                 * Enabled aggressive block sorting
                 */
                public Data(byte[] arrby, byte[] arrby2) {
                    Blob blob = this.ecasd;
                    if (arrby == null) {
                        arrby = null;
                    }
                    blob.setData(arrby);
                    Blob blob2 = this.old_casd_blob;
                    byte[] arrby3 = null;
                    if (arrby2 != null) {
                        arrby3 = arrby2;
                    }
                    blob2.setData(arrby3);
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
                Blob blob = this.inner(new Blob(2048));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class GetSpsd {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.SpsdResponse().size();
        }

        public static class Request
        extends TACommandRequest {
            private static final int MAX_CASD_CER_LEN_MAX = 4096;
            private static final int MAX_MDES_RGK_PUBLIC_CER_LEN_MAX = 4096;
            Data mData = new Data();

            public Request(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
                this.mData.setData(n2, arrby, arrby2, arrby3);
                this.mCommandId = 258;
                this.mRequest = this.mData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 258, this.mRequest);
            }

            public static class Data
            extends TAStruct {
                Blob mAid = this.inner(new Blob(16));
                Blob mCasdData = this.inner(new Blob(4096));
                Struct.Unsigned32 mCasdType = new Struct.Unsigned32();
                Blob mMdesPublicCert = this.inner(new Blob(4096));

                public void setData(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this.mCasdType.set(n2);
                    this.mCasdData.setData(arrby);
                    this.mMdesPublicCert.setData(arrby2);
                    this.mAid.setData(arrby3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            private static final int MAX_CASDPK_CERTIFICATE_LEN = 4096;
            private static final int MAX_RGK_DRIVEN_KEY_LEN = 1024;
            private static final int MAX_SPSD_RGK_LEN = 1024;
            private static final int MAX_SPSD_SEQUENCE_COUNTER_LEN = 32;
            SpsdResponse mRetVal = new SpsdResponse();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class SpsdResponse
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 casdCertType = new Struct.Unsigned32();
                Blob cert_encrypt = this.inner(new Blob(4096));
                Struct.Unsigned32 mReturnCode = new Struct.Unsigned32();
                Blob rgk = this.inner(new Blob(1024));
                Blob sku_keys = this.inner(new Blob(1024));
                Blob spsdSequenceCounter = this.inner(new Blob(32));

                public int getCasdCertType() {
                    return (int)this.casdCertType.get();
                }

                public byte[] getCertEncrypt() {
                    return this.cert_encrypt.getData();
                }

                public Struct.Unsigned32 getReturnCode() {
                    return this.mReturnCode;
                }

                public byte[] getRgk() {
                    return this.rgk.getData();
                }

                public byte[] getSkuKeys() {
                    return this.sku_keys.getData();
                }

                public byte[] getSpsdSequenceCounter() {
                    return this.spsdSequenceCounter.getData();
                }
            }

        }

    }

    public static final class MC_TA_ERRORS
    extends Enum<MC_TA_ERRORS> {
        private static final /* synthetic */ MC_TA_ERRORS[] $VALUES;
        public static final /* enum */ MC_TA_ERRORS ERROR;
        public static final /* enum */ MC_TA_ERRORS NO_ERROR;
        public static final /* enum */ MC_TA_ERRORS TA_TIMEOUT;

        static {
            NO_ERROR = new MC_TA_ERRORS();
            TA_TIMEOUT = new MC_TA_ERRORS();
            ERROR = new MC_TA_ERRORS();
            MC_TA_ERRORS[] arrmC_TA_ERRORS = new MC_TA_ERRORS[]{NO_ERROR, TA_TIMEOUT, ERROR};
            $VALUES = arrmC_TA_ERRORS;
        }

        public static MC_TA_ERRORS valueOf(String string) {
            return (MC_TA_ERRORS)Enum.valueOf(MC_TA_ERRORS.class, (String)string);
        }

        public static MC_TA_ERRORS[] values() {
            return (MC_TA_ERRORS[])$VALUES.clone();
        }
    }

    public static class PingTA {
        private static final int MC_TA_PING_DATA_LEN = 16;

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
                this.mRequest = this.mData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 1, this.mRequest);
            }

            public static class Data
            extends TAStruct {
                Blob mPingData = this.inner(new Blob(16));

                public void setData(byte[] arrby) {
                    this.mPingData.setData(arrby);
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
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Blob mPingRespData = this.inner(new Blob(16));
                Struct.Unsigned32 mReturnCode = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessSignature {
        public static final int MC_TA_SIGNATURE_DATA_LEN = 8192;

        public static int getMaxRequestSize() {
            return new ProcessSignatureRequest.Data().size();
        }

        public static int getMaxResponseSize() {
            return new ProcessSignatureResponse.Data().size();
        }

        public static class ProcessSignatureRequest
        extends TACommandRequest {
            Data mData = new Data();

            ProcessSignatureRequest(byte[] arrby, int n2) {
                this.mData.setData(arrby, n2);
                this.mRequest = this.mData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 266, this.mRequest);
            }

            public static class Data
            extends TAStruct {
                Blob inputData = this.inner(new Blob(8192));
                Struct.Unsigned32 mCasdType = new Struct.Unsigned32();

                public void setData(byte[] arrby, int n2) {
                    this.inputData.setData(arrby);
                    this.mCasdType.set(n2);
                }
            }

        }

        public static class ProcessSignatureResponse
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public ProcessSignatureResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 mReturnCode = new Struct.Unsigned32();
                Blob mSignatureResponse = this.inner(new Blob(8192));
            }

        }

    }

    public static class ProvisionToken {
        public static final int MC_TA_APDU_COMMANDS_DATA_LEN = 8192;
        private static final int MC_TA_EXT_KEY_CONTAINER_DATA_LEN = 512;

        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            private Data mApduData = new Data();

            public Request(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                this.mApduData.setData(arrby, arrby2, arrby3);
                this.mRequest = this.mApduData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 259, this.mRequest);
            }

            public static class Data
            extends TAStruct {
                Blob aid = this.inner(new Blob(16));
                Blob apduCommandsPkt = this.inner(new Blob(8192));
                Blob skuPkt = this.inner(new Blob(512));

                private void setData(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this.apduCommandsPkt.setData(arrby);
                    this.skuPkt.setData(arrby2);
                    this.aid.setData(arrby3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            private static final int MC_TA_CARD_SECUREDATA_LEN = 4096;
            private static final int MC_TA_RAPDU_COMMANDS_DATA_LEN = 8192;
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                public Blob cardSecureData = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                public Blob rApdus = this.inner(new Blob(8192));
                public Struct.Unsigned32 result = new Struct.Unsigned32();
                public Blob wrappedAtcObj = this.inner(new Blob(256));
            }

        }

    }

    public static class TAClearMstTracks {
        public static int getMaxRequestSize() {
            return new ClearMstTracksRequest.ClearMstTracksData().size();
        }

        public static int getMaxResponseSize() {
            return new ClearMstTracksResponse.ClearMstTracksOut().size();
        }

        public static class ClearMstTracksRequest
        extends TACommandRequest {
            ClearMstTracksRequest(int n2) {
                byte[] arrby = new ClearMstTracksData(n2).serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 264, arrby);
            }

            public static class ClearMstTracksData
            extends TAStruct {
                Struct.Unsigned32 unused_uint = new Struct.Unsigned32();

                public ClearMstTracksData() {
                }

                public ClearMstTracksData(int n2) {
                    this.unused_uint.set(n2);
                }
            }

        }

        public static class ClearMstTracksResponse
        extends TACommandResponse {
            public ClearMstTracksOut mRetVal = new ClearMstTracksOut();

            public ClearMstTracksResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class ClearMstTracksOut
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class TAClearSecureData {
        public static int getMaxRequestSize() {
            return new ClearSecureDataRequest.ClearSecureData().size();
        }

        public static int getMaxResponseSize() {
            return new ClearSecureDataResponse.ClearSecureDataOut().size();
        }

        public static class ClearSecureDataRequest
        extends TACommandRequest {
            ClearSecureDataRequest(int n2) {
                byte[] arrby = new ClearSecureData(n2).serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 267, arrby);
            }

            public static class ClearSecureData
            extends TAStruct {
                Struct.Unsigned32 unused_uint = new Struct.Unsigned32();

                public ClearSecureData() {
                }

                public ClearSecureData(int n2) {
                    this.unused_uint.set(n2);
                }
            }

        }

        public static class ClearSecureDataResponse
        extends TACommandResponse {
            public ClearSecureDataOut mRetVal = new ClearSecureDataOut();

            public ClearSecureDataResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class ClearSecureDataOut
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class TAComputeCC {
        public static int getMaxRequestSize() {
            return new TAComputeCCRequest.TAComputeCCIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAComputeCCResponse.TAComputeCCOut().size();
        }

        public static class TAComputeCCRequest
        extends TACommandRequest {
            public TAComputeCCRequest(int n2, int n3, byte[] arrby, byte[] arrby2) {
                TAComputeCCIn tAComputeCCIn = new TAComputeCCIn();
                tAComputeCCIn.setData(n2, n3, arrby, arrby2);
                this.mRequest = tAComputeCCIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 515, this.mRequest);
            }

            public static class TAComputeCCIn
            extends TAStruct {
                Blob _inputData1 = this.inner(new Blob(256));
                Struct.Unsigned32 _profileIdTrack1 = new Struct.Unsigned32();
                Struct.Unsigned32 _profileIdTrack2 = new Struct.Unsigned32();
                Blob _un = this.inner(new Blob(4));

                public void setData(int n2, int n3, byte[] arrby, byte[] arrby2) {
                    this._profileIdTrack1.set(n2);
                    this._profileIdTrack2.set(n3);
                    this._inputData1.setData(arrby);
                    this._un.setData(arrby2);
                }
            }

        }

        public static class TAComputeCCResponse
        extends TACommandResponse {
            public TAComputeCCOut mRetVal = new TAComputeCCOut();

            public TAComputeCCResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class TAComputeCCOut
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                public Blob _taMACTrack1 = this.inner(new Blob(1024));
                public Blob _taMACTrack2 = this.inner(new Blob(1024));
                Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }

    public static class TACopyACKey {
        public static int getMaxRequestSize() {
            return new TACopyACKeyRequest.TACopyACKeyRequestData().size();
        }

        public static int getMaxResponseSize() {
            return new TACopyACKeyResponse.TACopyACKeyResponseData().size();
        }

        public static class TACopyACKeyRequest
        extends TACommandRequest {
            public TACopyACKeyRequest(byte[] arrby) {
                TACopyACKeyRequestData tACopyACKeyRequestData = new TACopyACKeyRequestData();
                tACopyACKeyRequestData.setData(arrby);
                this.mRequest = tACopyACKeyRequestData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 517, this.mRequest);
            }

            public static class TACopyACKeyRequestData
            extends TAStruct {
                Blob _taPaymentProfile = this.inner(new Blob(4096));

                public void setData(byte[] arrby) {
                    this._taPaymentProfile.setData(arrby);
                }
            }

        }

        public static class TACopyACKeyResponse
        extends TACommandResponse {
            public TACopyACKeyResponseData mRetVal = new TACopyACKeyResponseData();

            public TACopyACKeyResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public long getErrorCode() {
                if (this.mRetVal._responseCode == null) {
                    return -1L;
                }
                return this.mRetVal._responseCode.get();
            }

            public byte[] getSecureProfile() {
                Blob blob = this.mRetVal._taPaymentProfile;
                if (blob != null) {
                    return blob.getData();
                }
                return null;
            }

            public static class TACopyACKeyResponseData
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 _responseCode = new Struct.Unsigned32();
                Blob _taPaymentProfile = this.inner(new Blob(4096));
            }

        }

    }

    public static class TAEncryptDsrpCnccPaymentInfo {
        public static int getMaxRequestSize() {
            return new TAEncryptDsrpCnccPaymentInfoRequest.EncryptDsrpPaymentInfoIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAEncryptDsrpCnccPaymentInfoResponse.EncryptDsrpPaymentInfoOut().size();
        }

        public static class TAEncryptDsrpCnccPaymentInfoRequest
        extends TACommandRequest {
            public TAEncryptDsrpCnccPaymentInfoRequest(byte[] arrby, byte[] arrby2) {
                if (TAController.DEBUG) {
                    Log.d(McTACommands.TAG, "dsrpPlainData.length:" + arrby.length);
                }
                EncryptDsrpPaymentInfoIn encryptDsrpPaymentInfoIn = new EncryptDsrpPaymentInfoIn();
                encryptDsrpPaymentInfoIn.setData(arrby, arrby2);
                this.mRequest = encryptDsrpPaymentInfoIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 516, this.mRequest);
            }

            public static class EncryptDsrpPaymentInfoIn
            extends TAStruct {
                Blob _taDsrpPayInfo = this.inner(new Blob(4096));
                Blob _taInAppNonde = this.inner(new Blob(32));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this._taDsrpPayInfo.setData(arrby);
                    this._taInAppNonde.setData(arrby2);
                }
            }

        }

        public static class TAEncryptDsrpCnccPaymentInfoResponse
        extends TACommandResponse {
            public EncryptDsrpPaymentInfoOut mRetVal = new EncryptDsrpPaymentInfoOut();

            public TAEncryptDsrpCnccPaymentInfoResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class EncryptDsrpPaymentInfoOut
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                public Blob _taJweData = this.inner(new Blob(8192));
                Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }

    public static class TAEncryptDsrpPaymentInfo {
        public static int getMaxRequestSize() {
            return new TAEncryptDsrpPaymentInfoRequest.EncryptDsrpPaymentInfoIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAEncryptDsrpPaymentInfoResponse.EncryptDsrpPaymentInfoOut().size();
        }

        public static class TAEncryptDsrpPaymentInfoRequest
        extends TACommandRequest {
            public TAEncryptDsrpPaymentInfoRequest(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                if (TAController.DEBUG) {
                    Log.d("com.samsung", "dsrpPlainData.length:" + arrby.length);
                }
                EncryptDsrpPaymentInfoIn encryptDsrpPaymentInfoIn = new EncryptDsrpPaymentInfoIn();
                encryptDsrpPaymentInfoIn.setData(arrby, arrby2, arrby3);
                this.mRequest = encryptDsrpPaymentInfoIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 518, this.mRequest);
            }

            public static class EncryptDsrpPaymentInfoIn
            extends TAStruct {
                Blob _taDsrpPayInfo = this.inner(new Blob(4096));
                Blob _taMerchantCert = this.inner(new Blob(4096));
                Blob _taSecondaryCert = this.inner(new Blob(4096));

                public void setData(byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this._taDsrpPayInfo.setData(arrby);
                    this._taMerchantCert.setData(arrby2);
                    this._taSecondaryCert.setData(arrby3);
                }
            }

        }

        public static class TAEncryptDsrpPaymentInfoResponse
        extends TACommandResponse {
            public EncryptDsrpPaymentInfoOut mRetVal = new EncryptDsrpPaymentInfoOut();

            public TAEncryptDsrpPaymentInfoResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class EncryptDsrpPaymentInfoOut
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                public Blob _taJweData = this.inner(new Blob(8192));
                Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }

    public static class TAGenerateMAC {
        public static int getMaxRequestSize() {
            return new TAGenerateMACRequest.TAGenerateMACIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAGenerateMACResponse.TAGenerateMACOut().size();
        }

        public static class TAGenerateMACRequest
        extends TACommandRequest {
            public TAGenerateMACRequest(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
                TAGenerateMACIn tAGenerateMACIn = new TAGenerateMACIn();
                tAGenerateMACIn.setData(n2, arrby, arrby2, arrby3);
                this.mRequest = tAGenerateMACIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 514, this.mRequest);
            }

            public static class TAGenerateMACIn
            extends TAStruct {
                Blob _inputData1 = this.inner(new Blob(256));
                Blob _inputData2 = this.inner(new Blob(256));
                Struct.Unsigned32 _profileId = new Struct.Unsigned32();
                Blob _un = this.inner(new Blob(4));

                public void setData(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
                    this._profileId.set(n2);
                    this._inputData1.setData(arrby);
                    this._inputData2.setData(arrby2);
                    this._un.setData(arrby3);
                }
            }

        }

        public static class TAGenerateMACResponse
        extends TACommandResponse {
            public TAGenerateMACOut mRetVal = new TAGenerateMACOut();

            public TAGenerateMACResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class TAGenerateMACOut
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Blob _taMAC = this.inner(new Blob(1024));
                Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }

    public static class TAGenerateUN {
        public static int getMaxRequestSize() {
            return new TAGenerateUNRequest.GenerateUNIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAGenerateUNResponse.GenerateUNOut().size();
        }

        public static class TAGenerateUNRequest
        extends TACommandRequest {
            public TAGenerateUNRequest() {
                this.mRequest = new GenerateUNIn().serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 265, this.mRequest);
            }

            public static class GenerateUNIn
            extends TAStruct {
            }

        }

        public static class TAGenerateUNResponse
        extends TACommandResponse {
            private static final byte UN_LEN = 4;
            public GenerateUNOut mRetVal = new GenerateUNOut();

            public TAGenerateUNResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class GenerateUNOut
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 _returnCode = new Struct.Unsigned32();
                public Blob _un = this.inner(new Blob(4));
            }

        }

    }

    public static class TAGetNonce {
        public static int getMaxRequestSize() {
            return new TAGetNonceRequest.GetNonceRequestData().size();
        }

        public static int getMaxResponseSize() {
            return new TAGetNonceResponse.GetNonceResponseData().size();
        }

        public static class TAGetNonceRequest
        extends TACommandRequest {
            public TAGetNonceRequest(byte[] arrby, byte[] arrby2) {
                GetNonceRequestData getNonceRequestData = new GetNonceRequestData();
                getNonceRequestData.setData(arrby, arrby2);
                this.mRequest = getNonceRequestData.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 262, this.mRequest);
            }

            public static class GetNonceRequestData
            extends TAStruct {
                Struct.Unsigned32 _nonceSize = new Struct.Unsigned32();
                Blob _taPaymentProfile = this.inner(new Blob(4096));
                Blob wrappedAtcObj = this.inner(new Blob(256));

                public void setData(byte[] arrby, byte[] arrby2) {
                    this._nonceSize.set(32L);
                    this._taPaymentProfile.setData(arrby);
                    this.wrappedAtcObj.setData(arrby2);
                }
            }

        }

        public static class TAGetNonceResponse
        extends TACommandResponse {
            public GetNonceResponseData mRetVal = new GetNonceResponseData();

            public TAGetNonceResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public String getNonce() {
                if (this.mRetVal._nonce != null) {
                    return new String(this.mRetVal._nonce.getData());
                }
                return null;
            }

            public static class GetNonceResponseData
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                public Blob _nonce = this.inner(new Blob(4096));
                Struct.Unsigned32 _responseCode = new Struct.Unsigned32();
            }

        }

    }

    public static class TAPrepareMSTtracks {
        public static int getMaxRequestSize() {
            return new TAPrepareMSTtracksRequest.PrepareMSTtracksIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAPrepareMSTtracksResponse.ProcessMSTtracksOut().size();
        }

        public static class TAPrepareMSTtracksRequest
        extends TACommandRequest {
            public TAPrepareMSTtracksRequest(long l2) {
                PrepareMSTtracksIn prepareMSTtracksIn = new PrepareMSTtracksIn();
                prepareMSTtracksIn.setData(l2);
                this.mRequest = prepareMSTtracksIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 261, this.mRequest);
            }

            public static class PrepareMSTtracksIn
            extends TAStruct {
                Struct.Unsigned32 _taMSTtimestamp = new Struct.Unsigned32();

                public void setData(long l2) {
                    this._taMSTtimestamp.set(l2);
                }
            }

        }

        public static class TAPrepareMSTtracksResponse
        extends TACommandResponse {
            public ProcessMSTtracksOut mRetVal = new ProcessMSTtracksOut();

            public TAPrepareMSTtracksResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class ProcessMSTtracksOut
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 _result = new Struct.Unsigned32();
            }

        }

    }

    public static class TAProcessMST {
        public static int getMaxRequestSize() {
            return new TAProcessMSTRequest.ProcessMSTDataIn().size();
        }

        public static int getMaxResponseSize() {
            return new TAProcessMSTResponse.ProcessMSTResult().size();
        }

        public static class TAProcessMSTRequest
        extends TACommandRequest {
            public TAProcessMSTRequest(int n2, byte[] arrby) {
                ProcessMSTDataIn processMSTDataIn = new ProcessMSTDataIn();
                processMSTDataIn.setData(n2, arrby);
                this.mRequest = processMSTDataIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 260, this.mRequest);
            }

            public static class ProcessMSTDataIn
            extends TAStruct {
                Struct.Unsigned32 _taBaudRateInUs = new Struct.Unsigned32();
                Blob _taMSTConfig = this.inner(new Blob(28));

                public void setData(int n2, byte[] arrby) {
                    this._taBaudRateInUs.set(n2);
                    this._taMSTConfig.setData(arrby);
                }
            }

        }

        public static class TAProcessMSTResponse
        extends TACommandResponse {
            public ProcessMSTResult mRetVal = new ProcessMSTResult();

            public TAProcessMSTResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class ProcessMSTResult
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 _result = new Struct.Unsigned32();
            }

        }

    }

    public static class TASetContext {
        public static int getMaxRequestSize() {
            return new TASetContextRequest.SetContextIn().size();
        }

        public static int getMaxResponseSize() {
            return new TASetContextResponse.SetContextOut().size();
        }

        public static class TASetContextRequest
        extends TACommandRequest {
            public TASetContextRequest(int n2) {
                SetContextIn setContextIn = new SetContextIn();
                setContextIn.setData(n2);
                this.mRequest = setContextIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 513, this.mRequest);
            }

            public static class SetContextIn
            extends TAStruct {
                Struct.Unsigned32 _profileId = new Struct.Unsigned32();

                public void setData(int n2) {
                    this._profileId.set(n2);
                }
            }

        }

        public static class TASetContextResponse
        extends TACommandResponse {
            private static final byte ICCDN_LEN = 16;
            public SetContextOut mRetVal = new SetContextOut();

            public TASetContextResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class SetContextOut
            extends TAStruct {
                public Blob _atc = this.inner(new Blob(2));
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                public Blob _iccdn = this.inner(new Blob(16));
                Struct.Unsigned32 _returnCode = new Struct.Unsigned32();
                public Blob _wrapped_atc_obj = this.inner(new Blob(256));
            }

        }

    }

    public static class TATransactionAuth {
        public static int getMaxRequestSize() {
            return new TATransactionAuthRequest.TransactionAuthDataIn().size();
        }

        public static int getMaxResponseSize() {
            return new TATransactionAuthResponse.TransactionAuthResult().size();
        }

        public static class TATransactionAuthRequest
        extends TACommandRequest {
            public TATransactionAuthRequest(byte[] arrby) {
                TransactionAuthDataIn transactionAuthDataIn = new TransactionAuthDataIn();
                transactionAuthDataIn.setData(arrby);
                this.mRequest = transactionAuthDataIn.serialize();
                this.init(1, McTACommands.TL_MAGIC_NUM, 263, this.mRequest);
            }

            public static class TransactionAuthDataIn
            extends TAStruct {
                Blob _taSecureObject = this.inner(new Blob(4096));

                public void setData(byte[] arrby) {
                    this._taSecureObject.setData(arrby);
                }
            }

        }

        public static class TATransactionAuthResponse
        extends TACommandResponse {
            public TransactionAuthResult mRetVal = new TransactionAuthResult();

            public TATransactionAuthResponse(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class TransactionAuthResult
            extends TAStruct {
                Struct.Unsigned8[] _errorMsg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[128]);
                Struct.Unsigned32 _result = new Struct.Unsigned32();
            }

        }

    }

}

