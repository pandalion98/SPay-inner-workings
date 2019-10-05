/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.spay.CertInfo
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import android.content.Context;
import android.os.Build;
import android.spay.CertInfo;
import android.spay.TACommandResponse;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.Arrays;

import javolution.io.Struct;

public class McTAController
extends TAController {
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
    public static TAInfo TA_INFO = new McTAInfo();
    private static final boolean bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
    private static McTAController mInstance;
    private static McTAController mMcTAController;
    private final String MC_PAY_CASD_NEW_CERTIFICATE_PATH = "/efs/mc/rst.dat";
    private final String MC_PAY_CERT_PATH_X509 = "/efs/prov_data/mc_pay/mc_pay_sign.dat";
    private final int MC_TA_INTERNAL_ERROR = -1;
    private final int MC_TA_SUCCESS = 0;
    private byte[] PING_DATA = new byte[]{0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3};
    private McDeviceCert mMcDeviceCert = null;

    private McTAController(Context context) {
        super(context, TA_INFO);
    }

    public static McTAController createOnlyInstance(Context context) {
        Class<McTAController> class_ = McTAController.class;
        synchronized (McTAController.class) {
            if (mInstance == null) {
                mInstance = new McTAController(context);
            }
            McTAController mcTAController = mInstance;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return mcTAController;
        }
    }

    private static byte[] getByteArray(Struct.Unsigned8[] arrunsigned8) {
        if (arrunsigned8 == null || arrunsigned8.length == 0) {
            return null;
        }
        byte[] arrby = new byte[arrunsigned8.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)arrunsigned8[i2].get();
        }
        return arrby;
    }

    public static McTAController getInstance() {
        Class<McTAController> class_ = McTAController.class;
        synchronized (McTAController.class) {
            McTAController mcTAController = mInstance;
            // ** MonitorExit[var2] (shouldn't be in output)
            return mcTAController;
        }
    }

    public static String getTaid() {
        if (bQC) {
            return "mc_pay";
        }
        return "ffffffff000000000000000000000021";
    }

    public long authenticateTransaction(byte[] arrby) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: authenticateTransaction");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController: in authenticateTransaction: TA is not loaded.");
            return -1L;
        }
        McTACommands.TATransactionAuth.TATransactionAuthResponse tATransactionAuthResponse = new McTACommands.TATransactionAuth.TATransactionAuthResponse(this.executeNoLoad(new McTACommands.TATransactionAuth.TATransactionAuthRequest(arrby)));
        if (tATransactionAuthResponse.mRetVal == null || tATransactionAuthResponse.mRetVal._result == null) {
            Log.e(TAG, "MC TA authenticate transaction failed.");
            return -1L;
        }
        return tATransactionAuthResponse.mRetVal._result.get();
    }

    public long clearMstData() {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: calling clearMstData");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "clearMstData: Error: TA is not loaded.");
            return -1L;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new McTACommands.TAClearMstTracks.ClearMstTracksRequest(0));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: clearMstData executeNoLoad failed");
            return -1L;
        }
        if (tACommandResponse.mResponseCode != 0) {
            Log.e(TAG, "clearMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
            return -1L;
        }
        McTACommands.TAClearMstTracks.ClearMstTracksResponse clearMstTracksResponse = new McTACommands.TAClearMstTracks.ClearMstTracksResponse(tACommandResponse);
        if (clearMstTracksResponse == null || clearMstTracksResponse.mRetVal == null || clearMstTracksResponse.mRetVal.return_code == null) {
            Log.e(TAG, "MC TA clear MST tracks request failed.");
            return -1L;
        }
        return clearMstTracksResponse.mRetVal.return_code.get();
    }

    public long clearSecureData() {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: calling clearSecureData");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "clearSecureData: Error: TA is not loaded.");
            return -1L;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new McTACommands.TAClearSecureData.ClearSecureDataRequest(0));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: clearSecureData executeNoLoad failed");
            return -1L;
        }
        if (tACommandResponse.mResponseCode != 0) {
            Log.e(TAG, "clearSecureData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
            return -1L;
        }
        McTACommands.TAClearSecureData.ClearSecureDataResponse clearSecureDataResponse = new McTACommands.TAClearSecureData.ClearSecureDataResponse(tACommandResponse);
        if (clearSecureDataResponse == null || clearSecureDataResponse.mRetVal == null || clearSecureDataResponse.mRetVal.return_code == null) {
            Log.e(TAG, "MC TA clearSecureData request failed.");
            return -1L;
        }
        return clearSecureDataResponse.mRetVal.return_code.get();
    }

    public McTACommands.TAComputeCC.TAComputeCCResponse.TAComputeCCOut computeCC(int n2, int n3, byte[] arrby, byte[] arrby2) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: computeCC");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "computeCC: in generateMAC: TA is not loaded.");
            return null;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new McTACommands.TAComputeCC.TAComputeCCRequest(n2, n3, arrby, arrby2));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: computeCC executeNoLoad failed");
            return null;
        }
        if (tACommandResponse.mResponseCode != 0) {
            Log.e(TAG, "computeCC: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
            return null;
        }
        McTACommands.TAComputeCC.TAComputeCCResponse tAComputeCCResponse = new McTACommands.TAComputeCC.TAComputeCCResponse(tACommandResponse);
        if (tAComputeCCResponse.mRetVal == null || tAComputeCCResponse.mRetVal.result == null || tAComputeCCResponse.mRetVal.result.get() != 0L) {
            Log.e(TAG, "computeCC: Error: command returned parsing error");
            return null;
        }
        return tAComputeCCResponse.mRetVal;
    }

    public byte[] copyACKey(byte[] arrby) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: copyACKey");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "copyACKey: Error: TA is not loaded.");
            return null;
        }
        McTACommands.TACopyACKey.TACopyACKeyResponse tACopyACKeyResponse = new McTACommands.TACopyACKey.TACopyACKeyResponse(this.executeNoLoad(new McTACommands.TACopyACKey.TACopyACKeyRequest(arrby)));
        if (tACopyACKeyResponse.getErrorCode() != 0L) {
            Log.e(TAG, "MC TA copyACKey command error: keys are valid, not copied: " + tACopyACKeyResponse.getErrorCode());
            return null;
        }
        if (tACopyACKeyResponse.getSecureProfile() == null) {
            Log.e(TAG, "MC TA copyACKey command failed.");
            return null;
        }
        return tACopyACKeyResponse.getSecureProfile();
    }

    /*
     * Enabled aggressive block sorting
     */
    public McTACommands.CardInfoEncryption.Response.CardData encryptCardInfo(byte[] arrby, byte[] arrby2) {
        if (!this.isTALoaded()) {
            Log.e(TAG, "encryptCardInfo: TA is not loaded.");
            return null;
        }
        if (arrby == null || arrby2 == null) return null;
        {
            McTACommands.CardInfoEncryption.Response response = new McTACommands.CardInfoEncryption.Response(this.executeNoLoad(new McTACommands.CardInfoEncryption.Request(arrby, arrby2)));
            if (response != null) return response.mRetVal;
            {
                Log.e(TAG, "encryptCardInfo: response is null");
                return null;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] generateMAC(int n2, byte[] arrby, byte[] arrby2, byte[] arrby3) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: generateMAC");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController: in generateMAC: TA is not loaded.");
            return null;
        }
        McTACommands.TAGenerateMAC.TAGenerateMACResponse tAGenerateMACResponse = new McTACommands.TAGenerateMAC.TAGenerateMACResponse(this.executeNoLoad(new McTACommands.TAGenerateMAC.TAGenerateMACRequest(n2, arrby, arrby2, arrby3)));
        if (tAGenerateMACResponse.mRetVal == null || tAGenerateMACResponse.mRetVal.result == null || tAGenerateMACResponse.mRetVal.result.get() != 0L || tAGenerateMACResponse.mRetVal._taMAC == null) {
            Log.e(TAG, "MC TA generateMAC command failed.");
            if (tAGenerateMACResponse.mRetVal == null || tAGenerateMACResponse.mRetVal.result == null) return null;
            {
                Log.e(TAG, "MC TA generateMAC: Error: TA command returned error = " + tAGenerateMACResponse.mRetVal.result.get());
                return null;
            }
        }
        if (!DEBUG) return tAGenerateMACResponse.mRetVal._taMAC.getData();
        {
            Log.d(TAG, "MCTAController: generateMAC - ending");
        }
        return tAGenerateMACResponse.mRetVal._taMAC.getData();
    }

    public byte[] generateUN() {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: generate UN");
        }
        if (!this.isTALoaded()) {
            Log.d(TAG, "MCTAController: in generateUN: TA is not loaded.");
            return null;
        }
        McTACommands.TAGenerateUN.TAGenerateUNResponse tAGenerateUNResponse = new McTACommands.TAGenerateUN.TAGenerateUNResponse(this.executeNoLoad(new McTACommands.TAGenerateUN.TAGenerateUNRequest()));
        if (tAGenerateUNResponse.mRetVal == null || tAGenerateUNResponse.mRetVal._un == null || tAGenerateUNResponse.mRetVal._returnCode.get() != 0L) {
            Log.d(TAG, "MC TA generateMAC command failed.");
            return null;
        }
        if (DEBUG) {
            Log.d(TAG, "MCTAController: generateUN - ending");
        }
        return tAGenerateUNResponse.mRetVal._un.getData();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CasdParams getCasdParameters() {
        McTAController mcTAController = this;
        synchronized (mcTAController) {
            McTACommands.CasdUpdateGetUid.Response response;
            block15 : {
                block14 : {
                    block13 : {
                        block12 : {
                            TACommandResponse tACommandResponse;
                            if (DEBUG) {
                                Log.d(TAG, "Calling getCasdParameters");
                            }
                            if (!this.isTALoaded()) {
                                Log.e(TAG, "getCasdParameters: TA is not loaded.");
                                return null;
                            }
                            CertInfo certInfo = this.mMcDeviceCert.getDeviceMcSignCert();
                            if (certInfo == null || certInfo.mCerts.isEmpty()) {
                                Log.e(TAG, "Error : getCertInfo is null ");
                                return null;
                            }
                            if (certInfo.mCerts.get((Object)MC_PAY_CERT_SIGN_FILENAME) != null) break block12;
                            if (certInfo.mCerts.get((Object)"/efs/prov_data/mc_pay/mc_pay_sign.dat") != null) break block13;
                            if (certInfo.mCerts.get((Object)"/efs/mc/rst.dat") != null) break block14;
                            byte[] arrby = (byte[])certInfo.mCerts.get((Object)MC_PAY_CASD_CERTIFICATE_PATH);
                            if (arrby == null) {
                                Log.e(TAG, "Error : CASD certs is null");
                                return null;
                            }
                            if (DEBUG) {
                                Log.d(TAG, "CASD cert length : " + arrby.length);
                            }
                            if ((tACommandResponse = this.executeNoLoad(new McTACommands.CasdUpdateGetUid.Request(arrby))) == null) {
                                Log.e(TAG, "getCasdParameters: Error: executeNoLoad failed");
                                return null;
                            }
                            response = new McTACommands.CasdUpdateGetUid.Response(tACommandResponse);
                            if (DEBUG) {
                                Log.d(TAG, "getCasdParameters called Successfully : " + response.mRetVal.return_code.get());
                            }
                            if (response.mRetVal.return_code.get() == 24578L) {
                                if (DEBUG) {
                                    Log.d(TAG, "Copying mc to rst");
                                }
                                this.copyMctoRst();
                                return null;
                            }
                            break block15;
                        }
                        if (!DEBUG) return null;
                        Log.d(TAG, "Device image is M-Version, x509 cert present. No need for mc.dat to rst.dat conversion");
                        return null;
                    }
                    if (!DEBUG) return null;
                    Log.d(TAG, "x509 cert present. No need for mc.dat to rst.dat conversion");
                    return null;
                }
                if (!DEBUG) return null;
                Log.d(TAG, "Already Updated");
                return null;
            }
            CasdParams casdParams = new CasdParams();
            casdParams.huid = McTAController.getByteArray(response.mRetVal.huid);
            casdParams.hpk = McTAController.getByteArray(response.mRetVal.hpk);
            return casdParams;
        }
    }

    public byte[] getDsrpCnccData(byte[] arrby, byte[] arrby2) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: getDsrpCnccData");
        }
        if (!this.isTALoaded() || arrby == null) {
            Log.e(TAG, "MCTAController: in getDsrpCnccData: TA is not loaded.");
            return null;
        }
        McTACommands.TAEncryptDsrpCnccPaymentInfo.TAEncryptDsrpCnccPaymentInfoResponse tAEncryptDsrpCnccPaymentInfoResponse = new McTACommands.TAEncryptDsrpCnccPaymentInfo.TAEncryptDsrpCnccPaymentInfoResponse(this.executeNoLoad(new McTACommands.TAEncryptDsrpCnccPaymentInfo.TAEncryptDsrpCnccPaymentInfoRequest(arrby, arrby2)));
        if (tAEncryptDsrpCnccPaymentInfoResponse == null || tAEncryptDsrpCnccPaymentInfoResponse.mRetVal == null || tAEncryptDsrpCnccPaymentInfoResponse.mRetVal._taJweData == null) {
            Log.e(TAG, "MCTAController: getDsrpCnccData : response is null");
            return null;
        }
        if (tAEncryptDsrpCnccPaymentInfoResponse.mRetVal.result.get() != McTAError.MC_PAY_OK.getValue()) {
            Log.e(TAG, "MCTAController: getDsrpCnccData : TA error : " + tAEncryptDsrpCnccPaymentInfoResponse.mRetVal.result.get());
            return null;
        }
        return tAEncryptDsrpCnccPaymentInfoResponse.mRetVal._taJweData.getData();
    }

    public byte[] getDsrpJweData(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: getDsrpJweData");
        }
        if (!this.isTALoaded() || arrby == null || arrby2 == null || arrby3 == null) {
            Log.e(TAG, "MCTAController: in getDsrpJweData: TA is not loaded.");
            return null;
        }
        McTACommands.TAEncryptDsrpPaymentInfo.TAEncryptDsrpPaymentInfoResponse tAEncryptDsrpPaymentInfoResponse = new McTACommands.TAEncryptDsrpPaymentInfo.TAEncryptDsrpPaymentInfoResponse(this.executeNoLoad(new McTACommands.TAEncryptDsrpPaymentInfo.TAEncryptDsrpPaymentInfoRequest(arrby, arrby2, arrby3)));
        if (tAEncryptDsrpPaymentInfoResponse == null || tAEncryptDsrpPaymentInfoResponse.mRetVal == null || tAEncryptDsrpPaymentInfoResponse.mRetVal._taJweData == null) {
            Log.e(TAG, "MCTAController: getDsrpJweData : response is null");
            return null;
        }
        if (tAEncryptDsrpPaymentInfoResponse.mRetVal.result.get() != McTAError.MC_PAY_OK.getValue()) {
            Log.e(TAG, "MCTAController: getDsrpJweData : TA error : " + tAEncryptDsrpPaymentInfoResponse.mRetVal.result.get());
            return null;
        }
        return tAEncryptDsrpPaymentInfoResponse.mRetVal._taJweData.getData();
    }

    public byte[] getNonce(byte[] arrby, byte[] arrby2) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: getNonce");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController: in getNonce: TA is not loaded.");
            return null;
        }
        McTACommands.TAGetNonce.TAGetNonceResponse tAGetNonceResponse = new McTACommands.TAGetNonce.TAGetNonceResponse(this.executeNoLoad(new McTACommands.TAGetNonce.TAGetNonceRequest(arrby, arrby2)));
        if (tAGetNonceResponse.mRetVal == null || tAGetNonceResponse.mRetVal._nonce == null) {
            Log.e(TAG, "MC TA getNonce command failed.");
            return null;
        }
        return tAGetNonceResponse.mRetVal._nonce.getData();
    }

    /*
     * Enabled aggressive block sorting
     */
    public McTACommands.GetSpsd.Response.SpsdResponse getSpsdInfo(byte[] arrby, byte[] arrby2) {
        if (!this.isTALoaded()) {
            Log.e(TAG, "getSpsdInfo: TA is not loaded.");
            return null;
        }
        McCertInfo mcCertInfo = this.mMcDeviceCert.getCASDCertEx();
        if (mcCertInfo == null || mcCertInfo.blob == null) return null;
        {
            McTACommands.GetSpsd.Response response = new McTACommands.GetSpsd.Response(this.executeNoLoad(new McTACommands.GetSpsd.Request(mcCertInfo.type, mcCertInfo.blob, arrby, arrby2)));
            if (response != null) return response.mRetVal;
            {
                Log.e(TAG, "getSpsdInfo : response is null");
                return null;
            }
        }
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            Log.e(TAG, "Error: init failed");
            return false;
        }
        this.mMcDeviceCert = new McDeviceCert(this);
        return true;
    }

    public boolean pingTA() {
        if (DEBUG) {
            Log.d(TAG, "pingTA...");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController:pingTA Error: TA is not loaded.");
            return false;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new McTACommands.PingTA.Request(this.PING_DATA));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: executeNoLoad failed");
            return false;
        }
        byte[] arrby = new McTACommands.PingTA.Response((TACommandResponse)tACommandResponse).mRetVal.mPingRespData.getData();
        if (DEBUG) {
            Log.d(TAG, "Ping Response" + Arrays.toString((byte[])arrby));
        }
        return true;
    }

    public long prepareMSTtracks(long l2) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: prepareMSTtracks");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController: in authenticateTransaction: TA is not loaded.");
            return -1L;
        }
        McTACommands.TAPrepareMSTtracks.TAPrepareMSTtracksResponse tAPrepareMSTtracksResponse = new McTACommands.TAPrepareMSTtracks.TAPrepareMSTtracksResponse(this.executeNoLoad(new McTACommands.TAPrepareMSTtracks.TAPrepareMSTtracksRequest(l2)));
        if (tAPrepareMSTtracksResponse.mRetVal == null || tAPrepareMSTtracksResponse.mRetVal._result == null) {
            Log.e(TAG, "MC TA prepare MST tracks request failed.");
            return -1L;
        }
        return tAPrepareMSTtracksResponse.mRetVal._result.get();
    }

    public long processMST(int n2, byte[] arrby) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: processMST");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController: in processMST: TA is not loaded.");
            return -1L;
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(new McTACommands.TAProcessMST.TAProcessMSTRequest(n2, arrby));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: processMST executeNoLoad failed");
            return -1L;
        }
        if (tACommandResponse.mResponseCode != 0) {
            Log.e(TAG, "processMST: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
            return -1L;
        }
        McTACommands.TAProcessMST.TAProcessMSTResponse tAProcessMSTResponse = new McTACommands.TAProcessMST.TAProcessMSTResponse(tACommandResponse);
        if (tAProcessMSTResponse == null || tAProcessMSTResponse.mRetVal == null || tAProcessMSTResponse.mRetVal._result == null) {
            Log.e(TAG, "MC TA processMST command failed.");
            return -1L;
        }
        return tAProcessMSTResponse.mRetVal._result.get();
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] processSignatureData(byte[] arrby, int n2) {
        int n3 = 1;
        if (DEBUG) {
            Log.d(TAG, "MCTAController:processSignatureData");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController:processSignatureData Error: TA is not loaded");
            return null;
        }
        if (arrby == null) {
            Log.e(TAG, "MCTAController:processSignatureData Error: input data is null");
            return null;
        }
        if (arrby.length >= 8192) {
            Log.e(TAG, "Input Signature data size is too big : " + arrby.length);
            return null;
        }
        if (n2 != n3 && n2 != 2) {
            n3 = 0;
        }
        if (n3 == 0) {
            Log.e(TAG, "processSignatureData : wrong input data");
            return null;
        }
        McTACommands.ProcessSignature.ProcessSignatureResponse processSignatureResponse = new McTACommands.ProcessSignature.ProcessSignatureResponse(this.executeNoLoad(new McTACommands.ProcessSignature.ProcessSignatureRequest(arrby, n2)));
        if (processSignatureResponse == null || processSignatureResponse.mRetVal == null || processSignatureResponse.mRetVal.mSignatureResponse == null) {
            Log.e(TAG, "processSignatureData : response is null");
            return null;
        }
        if ((long)processSignatureResponse.mResponseCode != McTAError.MC_PAY_OK.getValue()) {
            Log.e(TAG, "processSignatureData : error => " + processSignatureResponse.mResponseCode);
            return null;
        }
        return processSignatureResponse.mRetVal.mSignatureResponse.getData();
    }

    public McTACommands.ProvisionToken.Response provisionToken(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        if (!this.isTALoaded()) {
            Log.e(TAG, "provisionToken: TA is not loaded.");
            return null;
        }
        if (arrby.length > 8192) {
            Log.e(TAG, "apdu size is too big to handle : " + arrby.length);
            return null;
        }
        McTACommands.ProvisionToken.Response response = new McTACommands.ProvisionToken.Response(this.executeNoLoad(new McTACommands.ProvisionToken.Request(arrby, arrby2, arrby3)));
        if (response == null) {
            Log.e(TAG, "provisionToken : response is null");
            return null;
        }
        return response;
    }

    public McTACommands.TASetContext.TASetContextResponse.SetContextOut setContext(int n2) {
        if (DEBUG) {
            Log.d(TAG, "MCTAController: setContext");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "MCTAController: in setContext: TA is not loaded.");
            return null;
        }
        McTACommands.TASetContext.TASetContextResponse tASetContextResponse = new McTACommands.TASetContext.TASetContextResponse(this.executeNoLoad(new McTACommands.TASetContext.TASetContextRequest(n2)));
        if (tASetContextResponse.mRetVal == null || tASetContextResponse.mRetVal._returnCode == null || tASetContextResponse.mRetVal._returnCode.get() != 0L) {
            Log.e(TAG, "MC TA setContext command failed.");
            return null;
        }
        if (DEBUG) {
            Log.d(TAG, "MCTAController: setContext - ending");
        }
        return tASetContextResponse.mRetVal;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int writeCasdCert(byte[] arrby) {
        McTAController mcTAController = this;
        synchronized (mcTAController) {
            long l2;
            if (DEBUG) {
                Log.d(TAG, "Calling writeCasdCert");
            }
            if (!this.isTALoaded()) {
                Log.e(TAG, "writeCasdCert: TA is not loaded.");
                return -1;
            }
            byte[] arrby2 = this.mMcDeviceCert.loadOldCasdCerts();
            if (arrby2 == null) {
                Log.e(TAG, "CasdCertData is null");
                return -1;
            }
            TACommandResponse tACommandResponse = this.executeNoLoad(new McTACommands.CasdUpdateWriteKey.Request(arrby, arrby2));
            if (tACommandResponse == null) {
                Log.e(TAG, "writeCasdCert: Error: executeNoLoad failed");
                return -1;
            }
            McTACommands.CasdUpdateWriteKey.Response response = new McTACommands.CasdUpdateWriteKey.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "writeCasdCert called Successfully : " + response.mRetVal.return_code.get());
            }
            if (response.mRetVal.return_code.get() != 0L) {
                return -1;
            }
            CertInfo certInfo = new CertInfo();
            byte[] arrby3 = response.mRetVal.blob.getData();
            certInfo.mCerts.put((Object)"/efs/mc/rst.dat", (Object)arrby3);
            this.setCertInfo(certInfo);
            CertInfo certInfo2 = this.mMcDeviceCert.getDeviceCasdCert();
            if (certInfo2 == null || certInfo2.mCerts.isEmpty()) {
                Log.e(TAG, "Error : getCertInfo is null ");
                return -1;
            }
            byte[] arrby4 = (byte[])certInfo2.mCerts.get((Object)"/efs/mc/rst.dat");
            if (arrby4 == null) {
                Log.e(TAG, "Write Failed");
                return -1;
            }
            TACommandResponse tACommandResponse2 = this.executeNoLoad(new McTACommands.CasdUpdateVerifyKey.Request(arrby4));
            if (tACommandResponse2 == null) {
                Log.e(TAG, "verifyCasdCert: Error: executeNoLoad failed");
                return -1;
            }
            McTACommands.CasdUpdateVerifyKey.Response response2 = new McTACommands.CasdUpdateVerifyKey.Response(tACommandResponse2);
            if (DEBUG) {
                Log.d(TAG, "verifyCasdCert called Successfully : " + response2.mRetVal.return_code.get());
            }
            if ((l2 = response2.mRetVal.return_code.get()) == 0L) return 0;
            return -1;
        }
    }

    public static class CasdParams {
        public byte[] hpk;
        public byte[] huid;
    }

    public static class McCertInfo {
        public byte[] blob;
        public int type;
    }

}

