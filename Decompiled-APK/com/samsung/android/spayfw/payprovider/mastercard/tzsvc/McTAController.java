package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import android.content.Context;
import android.os.Build;
import android.spay.CertInfo;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.CardInfoEncryption.Request;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.CardInfoEncryption.Response;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.CardInfoEncryption.Response.CardData;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.CasdUpdateGetUid;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.CasdUpdateVerifyKey;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.CasdUpdateWriteKey;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.GetSpsd;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.GetSpsd.Response.SpsdResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.PingTA;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.ProcessSignature.ProcessSignatureRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.ProcessSignature.ProcessSignatureResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.ProvisionToken;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAClearMstTracks.ClearMstTracksRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAClearMstTracks.ClearMstTracksResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAClearSecureData.ClearSecureDataRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAClearSecureData.ClearSecureDataResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAComputeCC.TAComputeCCRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAComputeCC.TAComputeCCResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAComputeCC.TAComputeCCResponse.TAComputeCCOut;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TACopyACKey.TACopyACKeyRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TACopyACKey.TACopyACKeyResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAEncryptDsrpCnccPaymentInfo.TAEncryptDsrpCnccPaymentInfoRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAEncryptDsrpCnccPaymentInfo.TAEncryptDsrpCnccPaymentInfoResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAEncryptDsrpPaymentInfo.TAEncryptDsrpPaymentInfoRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAEncryptDsrpPaymentInfo.TAEncryptDsrpPaymentInfoResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAGenerateMAC.TAGenerateMACRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAGenerateMAC.TAGenerateMACResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAGenerateUN.TAGenerateUNRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAGenerateUN.TAGenerateUNResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAGetNonce.TAGetNonceRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAGetNonce.TAGetNonceResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAPrepareMSTtracks.TAPrepareMSTtracksRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAPrepareMSTtracks.TAPrepareMSTtracksResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAProcessMST.TAProcessMSTRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAProcessMST.TAProcessMSTResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TASetContext.TASetContextRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TASetContext.TASetContextResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TASetContext.TASetContextResponse.SetContextOut;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TATransactionAuth.TATransactionAuthRequest;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TATransactionAuth.TATransactionAuthResponse;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.Arrays;
import javolution.io.Struct.Unsigned8;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class McTAController extends TAController {
    public static final int CASD_CERT_TYPE_GP = 2;
    public static final int CASD_CERT_TYPE_X509 = 1;
    private static final int MC_PAY_CASDUPDATE_ERR = 24576;
    private static final int MC_PAY_CASDUPDATE_EXISTS_ERR = 24578;
    private static final int MC_PAY_CASDUPDATE_KEYVERIFY_ERR = 24577;
    public static final String MC_PAY_CASD_CERTIFICATE_PATH = "/efs/mc/mc.dat";
    public static final String MC_PAY_CERT_SIGN_FILENAME = "mc_pay_sign.dat";
    public static final int SGNATURE_DEC = 2;
    public static final int SGNATURE_ENC = 1;
    private static final String TAG = "MCTAController";
    public static TAInfo TA_INFO;
    private static final boolean bQC;
    private static McTAController mInstance;
    private static McTAController mMcTAController;
    private final String MC_PAY_CASD_NEW_CERTIFICATE_PATH;
    private final String MC_PAY_CERT_PATH_X509;
    private final int MC_TA_INTERNAL_ERROR;
    private final int MC_TA_SUCCESS;
    private byte[] PING_DATA;
    private McDeviceCert mMcDeviceCert;

    public static class CasdParams {
        public byte[] hpk;
        public byte[] huid;
    }

    public static class McCertInfo {
        public byte[] blob;
        public int type;
    }

    static {
        TA_INFO = new McTAInfo();
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
    }

    public static synchronized McTAController createOnlyInstance(Context context) {
        McTAController mcTAController;
        synchronized (McTAController.class) {
            if (mInstance == null) {
                mInstance = new McTAController(context);
            }
            mcTAController = mInstance;
        }
        return mcTAController;
    }

    public static synchronized McTAController getInstance() {
        McTAController mcTAController;
        synchronized (McTAController.class) {
            mcTAController = mInstance;
        }
        return mcTAController;
    }

    private McTAController(Context context) {
        super(context, TA_INFO);
        this.MC_PAY_CASD_NEW_CERTIFICATE_PATH = "/efs/mc/rst.dat";
        this.MC_PAY_CERT_PATH_X509 = "/efs/prov_data/mc_pay/mc_pay_sign.dat";
        this.MC_TA_INTERNAL_ERROR = -1;
        this.MC_TA_SUCCESS = 0;
        this.mMcDeviceCert = null;
        this.PING_DATA = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 0, (byte) 1, (byte) 2, (byte) 3};
    }

    protected boolean init() {
        if (super.init()) {
            this.mMcDeviceCert = new McDeviceCert(this);
            return true;
        }
        Log.m286e(TAG, "Error: init failed");
        return false;
    }

    public CardData encryptCardInfo(byte[] bArr, byte[] bArr2) {
        if (!isTALoaded()) {
            Log.m286e(TAG, "encryptCardInfo: TA is not loaded.");
            return null;
        } else if (bArr == null || bArr2 == null) {
            return null;
        } else {
            Response response = new Response(executeNoLoad(new Request(bArr, bArr2)));
            if (response != null) {
                return response.mRetVal;
            }
            Log.m286e(TAG, "encryptCardInfo: response is null");
            return null;
        }
    }

    public SpsdResponse getSpsdInfo(byte[] bArr, byte[] bArr2) {
        if (isTALoaded()) {
            McCertInfo cASDCertEx = this.mMcDeviceCert.getCASDCertEx();
            if (cASDCertEx == null || cASDCertEx.blob == null) {
                return null;
            }
            GetSpsd.Response response = new GetSpsd.Response(executeNoLoad(new GetSpsd.Request(cASDCertEx.type, cASDCertEx.blob, bArr, bArr2)));
            if (response != null) {
                return response.mRetVal;
            }
            Log.m286e(TAG, "getSpsdInfo : response is null");
            return null;
        }
        Log.m286e(TAG, "getSpsdInfo: TA is not loaded.");
        return null;
    }

    public ProvisionToken.Response provisionToken(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (!isTALoaded()) {
            Log.m286e(TAG, "provisionToken: TA is not loaded.");
            return null;
        } else if (bArr.length > PKIFailureInfo.certRevoked) {
            Log.m286e(TAG, "apdu size is too big to handle : " + bArr.length);
            return null;
        } else {
            ProvisionToken.Response response = new ProvisionToken.Response(executeNoLoad(new ProvisionToken.Request(bArr, bArr2, bArr3)));
            if (response != null) {
                return response;
            }
            Log.m286e(TAG, "provisionToken : response is null");
            return null;
        }
    }

    public byte[] processSignatureData(byte[] bArr, int i) {
        Object obj = SGNATURE_ENC;
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController:processSignatureData");
        }
        if (!isTALoaded()) {
            Log.m286e(TAG, "MCTAController:processSignatureData Error: TA is not loaded");
            return null;
        } else if (bArr == null) {
            Log.m286e(TAG, "MCTAController:processSignatureData Error: input data is null");
            return null;
        } else if (bArr.length >= PKIFailureInfo.certRevoked) {
            Log.m286e(TAG, "Input Signature data size is too big : " + bArr.length);
            return null;
        } else {
            if (!(i == SGNATURE_ENC || i == SGNATURE_DEC)) {
                obj = null;
            }
            if (obj == null) {
                Log.m286e(TAG, "processSignatureData : wrong input data");
                return null;
            }
            ProcessSignatureResponse processSignatureResponse = new ProcessSignatureResponse(executeNoLoad(new ProcessSignatureRequest(bArr, i)));
            if (processSignatureResponse == null || processSignatureResponse.mRetVal == null || processSignatureResponse.mRetVal.mSignatureResponse == null) {
                Log.m286e(TAG, "processSignatureData : response is null");
                return null;
            } else if (((long) processSignatureResponse.mResponseCode) == McTAError.MC_PAY_OK.getValue()) {
                return processSignatureResponse.mRetVal.mSignatureResponse.getData();
            } else {
                Log.m286e(TAG, "processSignatureData : error => " + processSignatureResponse.mResponseCode);
                return null;
            }
        }
    }

    public boolean pingTA() {
        if (DEBUG) {
            Log.m285d(TAG, "pingTA...");
        }
        if (isTALoaded()) {
            TACommandResponse executeNoLoad = executeNoLoad(new PingTA.Request(this.PING_DATA));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: executeNoLoad failed");
                return false;
            }
            byte[] data = new PingTA.Response(executeNoLoad).mRetVal.mPingRespData.getData();
            if (DEBUG) {
                Log.m285d(TAG, "Ping Response" + Arrays.toString(data));
            }
            return true;
        }
        Log.m286e(TAG, "MCTAController:pingTA Error: TA is not loaded.");
        return false;
    }

    public byte[] generateMAC(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: generateMAC");
        }
        if (isTALoaded()) {
            TAGenerateMACResponse tAGenerateMACResponse = new TAGenerateMACResponse(executeNoLoad(new TAGenerateMACRequest(i, bArr, bArr2, bArr3)));
            if (tAGenerateMACResponse.mRetVal == null || tAGenerateMACResponse.mRetVal.result == null || tAGenerateMACResponse.mRetVal.result.get() != 0 || tAGenerateMACResponse.mRetVal._taMAC == null) {
                Log.m286e(TAG, "MC TA generateMAC command failed.");
                if (tAGenerateMACResponse.mRetVal == null || tAGenerateMACResponse.mRetVal.result == null) {
                    return null;
                }
                Log.m286e(TAG, "MC TA generateMAC: Error: TA command returned error = " + tAGenerateMACResponse.mRetVal.result.get());
                return null;
            }
            if (DEBUG) {
                Log.m285d(TAG, "MCTAController: generateMAC - ending");
            }
            return tAGenerateMACResponse.mRetVal._taMAC.getData();
        }
        Log.m286e(TAG, "MCTAController: in generateMAC: TA is not loaded.");
        return null;
    }

    public SetContextOut setContext(int i) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: setContext");
        }
        if (isTALoaded()) {
            TASetContextResponse tASetContextResponse = new TASetContextResponse(executeNoLoad(new TASetContextRequest(i)));
            if (tASetContextResponse.mRetVal == null || tASetContextResponse.mRetVal._returnCode == null || tASetContextResponse.mRetVal._returnCode.get() != 0) {
                Log.m286e(TAG, "MC TA setContext command failed.");
                return null;
            }
            if (DEBUG) {
                Log.m285d(TAG, "MCTAController: setContext - ending");
            }
            return tASetContextResponse.mRetVal;
        }
        Log.m286e(TAG, "MCTAController: in setContext: TA is not loaded.");
        return null;
    }

    public long processMST(int i, byte[] bArr) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: processMST");
        }
        if (isTALoaded()) {
            TACommandResponse executeNoLoad = executeNoLoad(new TAProcessMSTRequest(i, bArr));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: processMST executeNoLoad failed");
                return -1;
            } else if (executeNoLoad.mResponseCode != 0) {
                Log.m286e(TAG, "processMST: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                return -1;
            } else {
                TAProcessMSTResponse tAProcessMSTResponse = new TAProcessMSTResponse(executeNoLoad);
                if (tAProcessMSTResponse != null && tAProcessMSTResponse.mRetVal != null && tAProcessMSTResponse.mRetVal._result != null) {
                    return tAProcessMSTResponse.mRetVal._result.get();
                }
                Log.m286e(TAG, "MC TA processMST command failed.");
                return -1;
            }
        }
        Log.m286e(TAG, "MCTAController: in processMST: TA is not loaded.");
        return -1;
    }

    public TAComputeCCOut computeCC(int i, int i2, byte[] bArr, byte[] bArr2) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: computeCC");
        }
        if (isTALoaded()) {
            TACommandResponse executeNoLoad = executeNoLoad(new TAComputeCCRequest(i, i2, bArr, bArr2));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: computeCC executeNoLoad failed");
                return null;
            } else if (executeNoLoad.mResponseCode != 0) {
                Log.m286e(TAG, "computeCC: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                return null;
            } else {
                TAComputeCCResponse tAComputeCCResponse = new TAComputeCCResponse(executeNoLoad);
                if (tAComputeCCResponse.mRetVal != null && tAComputeCCResponse.mRetVal.result != null && tAComputeCCResponse.mRetVal.result.get() == 0) {
                    return tAComputeCCResponse.mRetVal;
                }
                Log.m286e(TAG, "computeCC: Error: command returned parsing error");
                return null;
            }
        }
        Log.m286e(TAG, "computeCC: in generateMAC: TA is not loaded.");
        return null;
    }

    public byte[] getNonce(byte[] bArr, byte[] bArr2) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: getNonce");
        }
        if (isTALoaded()) {
            TAGetNonceResponse tAGetNonceResponse = new TAGetNonceResponse(executeNoLoad(new TAGetNonceRequest(bArr, bArr2)));
            if (tAGetNonceResponse.mRetVal != null && tAGetNonceResponse.mRetVal._nonce != null) {
                return tAGetNonceResponse.mRetVal._nonce.getData();
            }
            Log.m286e(TAG, "MC TA getNonce command failed.");
            return null;
        }
        Log.m286e(TAG, "MCTAController: in getNonce: TA is not loaded.");
        return null;
    }

    public long authenticateTransaction(byte[] bArr) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: authenticateTransaction");
        }
        if (isTALoaded()) {
            TATransactionAuthResponse tATransactionAuthResponse = new TATransactionAuthResponse(executeNoLoad(new TATransactionAuthRequest(bArr)));
            if (tATransactionAuthResponse.mRetVal != null && tATransactionAuthResponse.mRetVal._result != null) {
                return tATransactionAuthResponse.mRetVal._result.get();
            }
            Log.m286e(TAG, "MC TA authenticate transaction failed.");
            return -1;
        }
        Log.m286e(TAG, "MCTAController: in authenticateTransaction: TA is not loaded.");
        return -1;
    }

    public long prepareMSTtracks(long j) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: prepareMSTtracks");
        }
        if (isTALoaded()) {
            TAPrepareMSTtracksResponse tAPrepareMSTtracksResponse = new TAPrepareMSTtracksResponse(executeNoLoad(new TAPrepareMSTtracksRequest(j)));
            if (tAPrepareMSTtracksResponse.mRetVal != null && tAPrepareMSTtracksResponse.mRetVal._result != null) {
                return tAPrepareMSTtracksResponse.mRetVal._result.get();
            }
            Log.m286e(TAG, "MC TA prepare MST tracks request failed.");
            return -1;
        }
        Log.m286e(TAG, "MCTAController: in authenticateTransaction: TA is not loaded.");
        return -1;
    }

    public long clearMstData() {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: calling clearMstData");
        }
        if (isTALoaded()) {
            TACommandResponse executeNoLoad = executeNoLoad(new ClearMstTracksRequest(0));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: clearMstData executeNoLoad failed");
                return -1;
            } else if (executeNoLoad.mResponseCode != 0) {
                Log.m286e(TAG, "clearMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                return -1;
            } else {
                ClearMstTracksResponse clearMstTracksResponse = new ClearMstTracksResponse(executeNoLoad);
                if (clearMstTracksResponse != null && clearMstTracksResponse.mRetVal != null && clearMstTracksResponse.mRetVal.return_code != null) {
                    return clearMstTracksResponse.mRetVal.return_code.get();
                }
                Log.m286e(TAG, "MC TA clear MST tracks request failed.");
                return -1;
            }
        }
        Log.m286e(TAG, "clearMstData: Error: TA is not loaded.");
        return -1;
    }

    public long clearSecureData() {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: calling clearSecureData");
        }
        if (isTALoaded()) {
            TACommandResponse executeNoLoad = executeNoLoad(new ClearSecureDataRequest(0));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: clearSecureData executeNoLoad failed");
                return -1;
            } else if (executeNoLoad.mResponseCode != 0) {
                Log.m286e(TAG, "clearSecureData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                return -1;
            } else {
                ClearSecureDataResponse clearSecureDataResponse = new ClearSecureDataResponse(executeNoLoad);
                if (clearSecureDataResponse != null && clearSecureDataResponse.mRetVal != null && clearSecureDataResponse.mRetVal.return_code != null) {
                    return clearSecureDataResponse.mRetVal.return_code.get();
                }
                Log.m286e(TAG, "MC TA clearSecureData request failed.");
                return -1;
            }
        }
        Log.m286e(TAG, "clearSecureData: Error: TA is not loaded.");
        return -1;
    }

    public byte[] copyACKey(byte[] bArr) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: copyACKey");
        }
        if (isTALoaded()) {
            TACopyACKeyResponse tACopyACKeyResponse = new TACopyACKeyResponse(executeNoLoad(new TACopyACKeyRequest(bArr)));
            if (tACopyACKeyResponse.getErrorCode() != 0) {
                Log.m286e(TAG, "MC TA copyACKey command error: keys are valid, not copied: " + tACopyACKeyResponse.getErrorCode());
                return null;
            } else if (tACopyACKeyResponse.getSecureProfile() != null) {
                return tACopyACKeyResponse.getSecureProfile();
            } else {
                Log.m286e(TAG, "MC TA copyACKey command failed.");
                return null;
            }
        }
        Log.m286e(TAG, "copyACKey: Error: TA is not loaded.");
        return null;
    }

    public static String getTaid() {
        if (bQC) {
            return McTAInfo.CONFIG_QC_PROCESS;
        }
        return McTAInfo.CONFIG_LSI_UUID;
    }

    public synchronized CasdParams getCasdParameters() {
        CasdParams casdParams;
        if (DEBUG) {
            Log.m285d(TAG, "Calling getCasdParameters");
        }
        if (isTALoaded()) {
            CertInfo deviceMcSignCert = this.mMcDeviceCert.getDeviceMcSignCert();
            if (deviceMcSignCert == null || deviceMcSignCert.mCerts.isEmpty()) {
                Log.m286e(TAG, "Error : getCertInfo is null ");
                casdParams = null;
            } else if (deviceMcSignCert.mCerts.get(MC_PAY_CERT_SIGN_FILENAME) != null) {
                if (DEBUG) {
                    Log.m285d(TAG, "Device image is M-Version, x509 cert present. No need for mc.dat to rst.dat conversion");
                }
                casdParams = null;
            } else if (deviceMcSignCert.mCerts.get("/efs/prov_data/mc_pay/mc_pay_sign.dat") != null) {
                if (DEBUG) {
                    Log.m285d(TAG, "x509 cert present. No need for mc.dat to rst.dat conversion");
                }
                casdParams = null;
            } else if (deviceMcSignCert.mCerts.get("/efs/mc/rst.dat") != null) {
                if (DEBUG) {
                    Log.m285d(TAG, "Already Updated");
                }
                casdParams = null;
            } else {
                byte[] bArr = (byte[]) deviceMcSignCert.mCerts.get(MC_PAY_CASD_CERTIFICATE_PATH);
                if (bArr == null) {
                    Log.m286e(TAG, "Error : CASD certs is null");
                    casdParams = null;
                } else {
                    if (DEBUG) {
                        Log.m285d(TAG, "CASD cert length : " + bArr.length);
                    }
                    TACommandResponse executeNoLoad = executeNoLoad(new CasdUpdateGetUid.Request(bArr));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "getCasdParameters: Error: executeNoLoad failed");
                        casdParams = null;
                    } else {
                        CasdUpdateGetUid.Response response = new CasdUpdateGetUid.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "getCasdParameters called Successfully : " + response.mRetVal.return_code.get());
                        }
                        if (response.mRetVal.return_code.get() == 24578) {
                            if (DEBUG) {
                                Log.m285d(TAG, "Copying mc to rst");
                            }
                            copyMctoRst();
                            casdParams = null;
                        } else {
                            casdParams = new CasdParams();
                            casdParams.huid = getByteArray(response.mRetVal.huid);
                            casdParams.hpk = getByteArray(response.mRetVal.hpk);
                        }
                    }
                }
            }
        } else {
            Log.m286e(TAG, "getCasdParameters: TA is not loaded.");
            casdParams = null;
        }
        return casdParams;
    }

    private static byte[] getByteArray(Unsigned8[] unsigned8Arr) {
        if (unsigned8Arr == null || unsigned8Arr.length == 0) {
            return null;
        }
        byte[] bArr = new byte[unsigned8Arr.length];
        for (int i = 0; i < bArr.length; i += SGNATURE_ENC) {
            bArr[i] = (byte) unsigned8Arr[i].get();
        }
        return bArr;
    }

    public synchronized int writeCasdCert(byte[] bArr) {
        int i;
        if (DEBUG) {
            Log.m285d(TAG, "Calling writeCasdCert");
        }
        if (isTALoaded()) {
            byte[] loadOldCasdCerts = this.mMcDeviceCert.loadOldCasdCerts();
            if (loadOldCasdCerts == null) {
                Log.m286e(TAG, "CasdCertData is null");
                i = -1;
            } else {
                TACommandResponse executeNoLoad = executeNoLoad(new CasdUpdateWriteKey.Request(bArr, loadOldCasdCerts));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "writeCasdCert: Error: executeNoLoad failed");
                    i = -1;
                } else {
                    CasdUpdateWriteKey.Response response = new CasdUpdateWriteKey.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d(TAG, "writeCasdCert called Successfully : " + response.mRetVal.return_code.get());
                    }
                    if (response.mRetVal.return_code.get() != 0) {
                        i = -1;
                    } else {
                        CertInfo certInfo = new CertInfo();
                        certInfo.mCerts.put("/efs/mc/rst.dat", response.mRetVal.blob.getData());
                        setCertInfo(certInfo);
                        certInfo = this.mMcDeviceCert.getDeviceCasdCert();
                        if (certInfo == null || certInfo.mCerts.isEmpty()) {
                            Log.m286e(TAG, "Error : getCertInfo is null ");
                            i = -1;
                        } else {
                            loadOldCasdCerts = (byte[]) certInfo.mCerts.get("/efs/mc/rst.dat");
                            if (loadOldCasdCerts == null) {
                                Log.m286e(TAG, "Write Failed");
                                i = -1;
                            } else {
                                executeNoLoad = executeNoLoad(new CasdUpdateVerifyKey.Request(loadOldCasdCerts));
                                if (executeNoLoad == null) {
                                    Log.m286e(TAG, "verifyCasdCert: Error: executeNoLoad failed");
                                    i = -1;
                                } else {
                                    CasdUpdateVerifyKey.Response response2 = new CasdUpdateVerifyKey.Response(executeNoLoad);
                                    if (DEBUG) {
                                        Log.m285d(TAG, "verifyCasdCert called Successfully : " + response2.mRetVal.return_code.get());
                                    }
                                    i = response2.mRetVal.return_code.get() != 0 ? -1 : 0;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Log.m286e(TAG, "writeCasdCert: TA is not loaded.");
            i = -1;
        }
        return i;
    }

    public byte[] generateUN() {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: generate UN");
        }
        if (isTALoaded()) {
            TAGenerateUNResponse tAGenerateUNResponse = new TAGenerateUNResponse(executeNoLoad(new TAGenerateUNRequest()));
            if (tAGenerateUNResponse.mRetVal == null || tAGenerateUNResponse.mRetVal._un == null || tAGenerateUNResponse.mRetVal._returnCode.get() != 0) {
                Log.m285d(TAG, "MC TA generateMAC command failed.");
                return null;
            }
            if (DEBUG) {
                Log.m285d(TAG, "MCTAController: generateUN - ending");
            }
            return tAGenerateUNResponse.mRetVal._un.getData();
        }
        Log.m285d(TAG, "MCTAController: in generateUN: TA is not loaded.");
        return null;
    }

    public byte[] getDsrpCnccData(byte[] bArr, byte[] bArr2) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: getDsrpCnccData");
        }
        if (!isTALoaded() || bArr == null) {
            Log.m286e(TAG, "MCTAController: in getDsrpCnccData: TA is not loaded.");
            return null;
        }
        TAEncryptDsrpCnccPaymentInfoResponse tAEncryptDsrpCnccPaymentInfoResponse = new TAEncryptDsrpCnccPaymentInfoResponse(executeNoLoad(new TAEncryptDsrpCnccPaymentInfoRequest(bArr, bArr2)));
        if (tAEncryptDsrpCnccPaymentInfoResponse == null || tAEncryptDsrpCnccPaymentInfoResponse.mRetVal == null || tAEncryptDsrpCnccPaymentInfoResponse.mRetVal._taJweData == null) {
            Log.m286e(TAG, "MCTAController: getDsrpCnccData : response is null");
            return null;
        } else if (tAEncryptDsrpCnccPaymentInfoResponse.mRetVal.result.get() == McTAError.MC_PAY_OK.getValue()) {
            return tAEncryptDsrpCnccPaymentInfoResponse.mRetVal._taJweData.getData();
        } else {
            Log.m286e(TAG, "MCTAController: getDsrpCnccData : TA error : " + tAEncryptDsrpCnccPaymentInfoResponse.mRetVal.result.get());
            return null;
        }
    }

    public byte[] getDsrpJweData(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (DEBUG) {
            Log.m285d(TAG, "MCTAController: getDsrpJweData");
        }
        if (!isTALoaded() || bArr == null || bArr2 == null || bArr3 == null) {
            Log.m286e(TAG, "MCTAController: in getDsrpJweData: TA is not loaded.");
            return null;
        }
        TAEncryptDsrpPaymentInfoResponse tAEncryptDsrpPaymentInfoResponse = new TAEncryptDsrpPaymentInfoResponse(executeNoLoad(new TAEncryptDsrpPaymentInfoRequest(bArr, bArr2, bArr3)));
        if (tAEncryptDsrpPaymentInfoResponse == null || tAEncryptDsrpPaymentInfoResponse.mRetVal == null || tAEncryptDsrpPaymentInfoResponse.mRetVal._taJweData == null) {
            Log.m286e(TAG, "MCTAController: getDsrpJweData : response is null");
            return null;
        } else if (tAEncryptDsrpPaymentInfoResponse.mRetVal.result.get() == McTAError.MC_PAY_OK.getValue()) {
            return tAEncryptDsrpPaymentInfoResponse.mRetVal._taJweData.getData();
        } else {
            Log.m286e(TAG, "MCTAController: getDsrpJweData : TA error : " + tAEncryptDsrpPaymentInfoResponse.mRetVal.result.get());
            return null;
        }
    }
}
