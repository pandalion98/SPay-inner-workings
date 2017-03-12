package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.cncc.CNCCTAException;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ActivateToken;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ClearLUPC;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.DecryptTokenData;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateECDHKeys;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateInAppJwePaymentPayload;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GenerateInAppPaymentPayload;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.GetNFCCryptogram;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Request;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.LoadCerts.Response;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessData;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessRequestData;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessResponseData;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTokenData;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ProcessTransaction;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.ResumeToken;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.SuspendToken;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands.TransmitMstData;
import com.samsung.android.spayfw.utils.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AmexTAController extends SPayTAAuthController {
    private static AmexTAController qq;
    private static AmexTAInfo qt;
    private AmexDeviceCerts qr;
    private DevicePublicCerts qs;

    public static class DevicePublicCerts {
        public String deviceCertificate;
        public String deviceEncryptionCertificate;
        public String deviceSigningCertificate;
    }

    public static class EphemeralKeyInfo {
        public String encryptedEphemeralPrivateKey;
        public String ephemeralPublicKey;
    }

    public static class GenerateInAppPaymentPayloadResponse {
        public String payload;
        public String tid;
    }

    public static class InAppTZTxnInfo {
        public String merchantCertificate;
        public byte[] nonce;
        public Map<String, String> txnAttributes;
    }

    public static class ProcessRequestDataResponse {
        public String encryptedRequestData;
        public String encryptionParams;
        public String requestDataSignature;
    }

    public static class ProcessTokenDataResponse {
        public String eAPDUBlob;
        public String eMetadataBlob;
        public String eNFCLUPCBlob;
        public String eOtherLUPCBlob;
        public String lupcMetadataBlob;
        public String responseDataSignature;
    }

    public static class ProcessTransactionResponse {
        public String atcLUPC;
        public String eNFCLUPCBlob;
        public String eOtherLUPCBlob;
        public String lupcMetaDataBlob;
        public String otherTID;
    }

    private AmexTAController(Context context) {
        super(context, qt);
        this.qr = null;
        this.qs = null;
    }

    protected boolean init() {
        if (super.init()) {
            this.qr = new AmexDeviceCerts(this);
            return true;
        }
        Log.m286e("AmexTAController", "Error: init failed");
        return false;
    }

    public static synchronized AmexTAController m780C(Context context) {
        AmexTAController amexTAController;
        synchronized (AmexTAController.class) {
            if (qq == null) {
                qq = new AmexTAController(context);
            }
            amexTAController = qq;
        }
        return amexTAController;
    }

    public static synchronized AmexTAController ct() {
        AmexTAController amexTAController;
        synchronized (AmexTAController.class) {
            amexTAController = qq;
        }
        return amexTAController;
    }

    static {
        qt = new AmexTAInfo();
    }

    public synchronized boolean loadTA() {
        return super.loadTA();
    }

    public synchronized void unloadTA() {
        this.qs = null;
        super.unloadTA();
    }

    private boolean isSecuritySetupInitialized() {
        if (this.qs == null) {
            return false;
        }
        if (DEBUG) {
            Log.m285d("AmexTAController", "Device Certs already loaded)");
        }
        return true;
    }

    public boolean initializeSecuritySetup() {
        if (isSecuritySetupInitialized()) {
            return true;
        }
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling initializeSecuritySetup");
        }
        if (isTALoaded()) {
            if (!this.qr.isLoaded()) {
                Log.m285d("AmexTAController", "mAmexDeviceCerts is not loaded");
                if (!this.qr.load()) {
                    Log.m286e("AmexTAController", "Error: Amex Device Certs Load failed");
                    return false;
                }
            }
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Request(this.qr.getDevicePrivateSignCert(), this.qr.getDevicePrivateEncryptionCert()));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "loadAllCerts: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                Response response = new Response(executeNoLoad);
                this.qs = new DevicePublicCerts();
                this.qs.deviceCertificate = Utils.convertToPem(response.mRetVal.deviceRootRSA2048PubCert.getData(), false);
                this.qs.deviceSigningCertificate = Utils.convertToPem(response.mRetVal.deviceSignRSA2048PubCert.getData(), false);
                this.qs.deviceEncryptionCertificate = Utils.convertToPem(response.mRetVal.pR.getData(), false);
                if (!DEBUG) {
                    return true;
                }
                Log.m285d("AmexTAController", "initializeSecuritySetup called Successfully");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup: Error: TA is not loaded, please call loadTA() API first!");
        return false;
    }

    public synchronized DevicePublicCerts cu() {
        if (initializeSecuritySetup()) {
        } else {
            Log.m286e("AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        }
        return this.qs;
    }

    public synchronized EphemeralKeyInfo cv() {
        EphemeralKeyInfo ephemeralKeyInfo;
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new GenerateECDHKeys.Request());
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "generateECDHKeys: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                GenerateECDHKeys.Response response = new GenerateECDHKeys.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "GenerateECDHKeys Call Failed");
                    throw new AmexTAException(i);
                }
                ephemeralKeyInfo = new EphemeralKeyInfo();
                ephemeralKeyInfo.ephemeralPublicKey = Utils.toBase64(response.mRetVal.pF.getData());
                ephemeralKeyInfo.encryptedEphemeralPrivateKey = Utils.encodeHex(response.mRetVal.pG.getData());
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return ephemeralKeyInfo;
    }

    public synchronized ProcessRequestDataResponse m787b(String str, String str2, String str3) {
        ProcessRequestDataResponse processRequestDataResponse;
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d("AmexTAController", "Calling processRequestData");
            }
            if (initializeSecuritySetup()) {
                try {
                    byte[] bArr2;
                    List derChain = Utils.getDerChain(str);
                    if (str2 == null) {
                        bArr2 = null;
                    } else {
                        bArr2 = str2.getBytes();
                    }
                    if (str3 != null) {
                        bArr = str3.getBytes();
                    }
                    TACommandResponse executeNoLoad = executeNoLoad(new ProcessRequestData.Request(derChain, bArr2, bArr));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.m286e("AmexTAController", "processRequestData: Error: executeNoLoad failed");
                        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                    }
                    ProcessRequestData.Response response = new ProcessRequestData.Response(executeNoLoad);
                    int i = (int) response.mRetVal.return_code.get();
                    if (i != 0) {
                        Log.m286e("AmexTAController", "ProcessRequestData Call Failed");
                        throw new AmexTAException(i);
                    }
                    if (DEBUG) {
                        Log.m285d("AmexTAController", "processRequestData called Successfully");
                    }
                    processRequestDataResponse = new ProcessRequestDataResponse();
                    processRequestDataResponse.encryptedRequestData = Utils.toBase64(response.mRetVal.pW.getData());
                    processRequestDataResponse.encryptionParams = Utils.toBase64(response.mRetVal.pX.getData());
                    processRequestDataResponse.requestDataSignature = Utils.toBase64(response.mRetVal.pY.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
            }
            Log.m286e("AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        }
        return processRequestDataResponse;
    }

    public synchronized String m785a(String str, boolean z) {
        String toBase64;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling processData");
        }
        if (initializeSecuritySetup()) {
            TACommandRequest request;
            if (z) {
                try {
                    byte[] bArr;
                    if (str == null) {
                        bArr = null;
                    } else {
                        bArr = str.getBytes();
                    }
                    request = new ProcessData.Request(bArr, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
            }
            request = new ProcessData.Request(Utils.fromBase64(str), false);
            TACommandResponse executeNoLoad = executeNoLoad(request);
            if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                Log.m286e("AmexTAController", "processData: Error: executeNoLoad failed");
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
            ProcessData.Response response = new ProcessData.Response(executeNoLoad);
            int i = (int) response.mRetVal.return_code.get();
            if (i != 0) {
                Log.m286e("AmexTAController", "ProcessData Call Failed");
                throw new AmexTAException(i);
            }
            if (DEBUG) {
                Log.m285d("AmexTAController", "processData called Successfully");
            }
            if (z) {
                toBase64 = Utils.toBase64(response.mRetVal.data.getData());
            } else {
                toBase64 = new String(response.mRetVal.data.getData());
            }
        } else {
            Log.m286e("AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        }
        return toBase64;
    }

    public synchronized String m788o(String str, String str2) {
        ProcessResponseData.Response response;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling processRequestData");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ProcessResponseData.Request(Utils.fromBase64(str), Utils.fromBase64(str2)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "processRequestData: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                response = new ProcessResponseData.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "ProcessResponseData Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "processRequestData called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return Utils.toBase64(response.mRetVal.qb.getData());
    }

    public synchronized ProcessTokenDataResponse m782a(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        ProcessTokenDataResponse processTokenDataResponse;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling processTokenData");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ProcessTokenData.Request(Utils.getDerChain(str), Utils.fromBase64(str2), Utils.fromBase64(str3), Utils.fromBase64(str4), str5.getBytes(), Utils.decodeHex(str6), Utils.decodeHex(str7)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                ProcessTokenData.Response response = new ProcessTokenData.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "ProcessTokenData Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "processTokenData called Successfully");
                }
                processTokenDataResponse = new ProcessTokenDataResponse();
                processTokenDataResponse.eAPDUBlob = Utils.toBase64(response.mRetVal.pE.getData());
                processTokenDataResponse.eNFCLUPCBlob = Utils.toBase64(response.mRetVal.qi.getData());
                processTokenDataResponse.eOtherLUPCBlob = Utils.toBase64(response.mRetVal.qj.getData());
                processTokenDataResponse.eMetadataBlob = Utils.toBase64(response.mRetVal.qk.getData());
                processTokenDataResponse.lupcMetadataBlob = new String(response.mRetVal.ql.getData());
                processTokenDataResponse.responseDataSignature = Utils.toBase64(response.mRetVal.qm.getData());
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return processTokenDataResponse;
    }

    public synchronized String decryptTokenData(String str) {
        DecryptTokenData.Response response;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling decryptTokenData");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new DecryptTokenData.Request(Utils.fromBase64(str)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                response = new DecryptTokenData.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "DecryptTokenData Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "decryptTokenData called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return new String(response.mRetVal.blob.getData());
    }

    public synchronized ProcessTransactionResponse m783a(int i, String str, String str2, String str3, String str4, String str5) {
        ProcessTransactionResponse processTransactionResponse;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling processTransaction");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ProcessTransaction.Request(i, Utils.fromBase64(str), Utils.fromBase64(str2), Utils.fromBase64(str3), Utils.fromBase64(str4), str5.getBytes()));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                ProcessTransaction.Response response = new ProcessTransaction.Response(executeNoLoad);
                int i2 = (int) response.mRetVal.return_code.get();
                if (i2 != 0) {
                    Log.m286e("AmexTAController", "ProcessTransaction Call Failed");
                    throw new AmexTAException(i2);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "processTransaction called Successfully");
                }
                processTransactionResponse = new ProcessTransactionResponse();
                processTransactionResponse.eNFCLUPCBlob = Utils.toBase64(response.mRetVal.qi.getData());
                processTransactionResponse.eOtherLUPCBlob = Utils.toBase64(response.mRetVal.qj.getData());
                processTransactionResponse.lupcMetaDataBlob = new String(response.mRetVal.qn.getData());
                processTransactionResponse.atcLUPC = Utils.encodeHex(response.mRetVal.qo.getData());
                processTransactionResponse.otherTID = new String(response.mRetVal.qp.getData());
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return processTransactionResponse;
    }

    public synchronized String suspendToken(String str) {
        SuspendToken.Response response;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling suspendToken");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new SuspendToken.Request(Utils.fromBase64(str)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "suspendToken: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                response = new SuspendToken.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "SuspendToken Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "suspendToken called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return Utils.toBase64(response.mRetVal.pC.getData());
    }

    public synchronized String resumeToken(String str) {
        ResumeToken.Response response;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling suspendToken");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ResumeToken.Request(Utils.fromBase64(str)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "suspendToken: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                response = new ResumeToken.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "ResumeToken Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "resumeToken called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return Utils.toBase64(response.mRetVal.pC.getData());
    }

    public synchronized String activateToken(String str) {
        ActivateToken.Response response;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling suspendToken");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ActivateToken.Request(Utils.fromBase64(str)));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "suspendToken: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                response = new ActivateToken.Response(executeNoLoad);
                int i = (int) response.mRetVal.return_code.get();
                if (i != 0) {
                    Log.m286e("AmexTAController", "ActivateToken Call Failed");
                    throw new AmexTAException(i);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "activateToken called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return Utils.toBase64(response.mRetVal.pC.getData());
    }

    public synchronized String m784a(int i, int i2, Map<String, String> map) {
        GetNFCCryptogram.Response response;
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling getNFCCryptogram");
        }
        if (initializeSecuritySetup()) {
            try {
                Map hashMap = new HashMap();
                for (Entry entry : map.entrySet()) {
                    hashMap.put(((String) entry.getKey()).getBytes(), ((String) entry.getValue()).getBytes());
                }
                TACommandResponse executeNoLoad = executeNoLoad(new GetNFCCryptogram.Request(i, i2, hashMap));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e("AmexTAController", "getNFCCryptogram: Error: executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                response = new GetNFCCryptogram.Response(executeNoLoad);
                int i3 = (int) response.mRetVal.return_code.get();
                if (i3 != 0) {
                    Log.m286e("AmexTAController", "GetNFCCryptogram Call Failed");
                    throw new AmexTAException(i3);
                }
                if (DEBUG) {
                    Log.m285d("AmexTAController", "getNFCCryptogram called Successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return Utils.encodeHex(response.mRetVal.pP.getData());
    }

    public boolean m786a(int i, byte[] bArr) {
        if (DEBUG) {
            Log.m285d("AmexTAController", "Calling transmitMstData");
        }
        boolean initializeSecuritySetup = initializeSecuritySetup();
        if (initializeSecuritySetup) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new TransmitMstData.Request(i, bArr));
                if (executeNoLoad == null) {
                    Log.m286e("AmexTAController", "Error: transmitMstData executeNoLoad failed");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    throw new AmexTAException(executeNoLoad.mResponseCode);
                } else {
                    if (new TransmitMstData.Response(executeNoLoad).mRetVal.return_code.get() == 0) {
                        initializeSecuritySetup = true;
                    }
                    if (DEBUG) {
                        Log.m285d("AmexTAController", "TransmitMstData: ret = " + initializeSecuritySetup);
                    }
                    return initializeSecuritySetup;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
    }

    public synchronized boolean cw() {
        boolean z = false;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d("AmexTAController", "Calling clearLUPC");
            }
            if (initializeSecuritySetup()) {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new ClearLUPC.Request());
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.m286e("AmexTAController", "Error: clearLUPC executeNoLoad failed");
                    } else {
                        ClearLUPC.Response response = new ClearLUPC.Response(executeNoLoad);
                        int i = (int) response.mRetVal.return_code.get();
                        if (i != 0) {
                            Log.m286e("AmexTAController", "ClearLUPC Call Failed");
                            throw new AmexTAException(i);
                        }
                        if (DEBUG) {
                            Log.m285d("AmexTAController", "clearLUPC: success ");
                        }
                        if (response.mRetVal.return_code.get() == 0) {
                            z = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
            }
            Log.m286e("AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        }
        return z;
    }

    public synchronized GenerateInAppPaymentPayloadResponse m781a(String str, Object obj) {
        GenerateInAppPaymentPayloadResponse generateInAppPaymentPayloadResponse;
        if (initializeSecuritySetup()) {
            try {
                InAppTZTxnInfo inAppTZTxnInfo = (InAppTZTxnInfo) obj;
                if (inAppTZTxnInfo == null || inAppTZTxnInfo.txnAttributes == null) {
                    Log.m286e("AmexTAController", "processTokenData: Error: invalid input data");
                    throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                }
                Map hashMap = new HashMap();
                for (Entry entry : inAppTZTxnInfo.txnAttributes.entrySet()) {
                    hashMap.put(((String) entry.getKey()).getBytes(), ((String) entry.getValue()).getBytes());
                }
                TACommandResponse executeNoLoad;
                int i;
                if (inAppTZTxnInfo.merchantCertificate == null) {
                    executeNoLoad = executeNoLoad(new GenerateInAppPaymentPayload.Request(str.getBytes(), hashMap, inAppTZTxnInfo.nonce));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.m286e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                    }
                    GenerateInAppPaymentPayload.Response response = new GenerateInAppPaymentPayload.Response(executeNoLoad);
                    i = (int) response.mRetVal.return_code.get();
                    if (i != 0) {
                        Log.m286e("AmexTAController", "GenerateInAppPaymentPayload Call Failed");
                        throw new AmexTAException(i);
                    }
                    if (DEBUG) {
                        Log.m285d("AmexTAController", "GenerateInAppPaymentPayload called Successfully");
                    }
                    generateInAppPaymentPayloadResponse = new GenerateInAppPaymentPayloadResponse();
                    Log.m285d("AmexTAController", "InApp Payload from Response " + Arrays.toString(response.mRetVal.pK.getData()));
                    Log.m285d("AmexTAController", "InApp Payload length from Response" + response.mRetVal.pK.getData().length);
                    generateInAppPaymentPayloadResponse.payload = Utils.toBase64(response.mRetVal.pK.getData());
                    generateInAppPaymentPayloadResponse.tid = new String(response.mRetVal.pL.getData());
                    if (DEBUG) {
                        Log.m285d("AmexTAController", "InApp Payload " + generateInAppPaymentPayloadResponse.payload);
                    }
                } else {
                    Log.m285d("AmexTAController", "GenerateInAppJwePaymentPayload Call");
                    List bE = Utils.bE(inAppTZTxnInfo.merchantCertificate);
                    if (bE == null || bE.isEmpty()) {
                        Log.m286e("AmexTAController", "GenerateInAppJwePaymentPayload: cannot get certificate");
                        throw new AmexTAException(CNCCTAException.CNCC_INVALID_INPUT_BUFFER);
                    }
                    executeNoLoad = executeNoLoad(new GenerateInAppJwePaymentPayload.Request(str.getBytes(), hashMap, bE));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.m286e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
                    }
                    GenerateInAppJwePaymentPayload.Response response2 = new GenerateInAppJwePaymentPayload.Response(executeNoLoad);
                    i = (int) response2.mRetVal.return_code.get();
                    if (i != 0) {
                        Log.m286e("AmexTAController", "GenerateInAppPaymentPayload Call Failed");
                        throw new AmexTAException(i);
                    }
                    if (DEBUG) {
                        Log.m285d("AmexTAController", "GenerateInAppPaymentPayload called Successfully");
                    }
                    generateInAppPaymentPayloadResponse = new GenerateInAppPaymentPayloadResponse();
                    Log.m285d("AmexTAController", "InApp Payload from Response " + Arrays.toString(response2.mRetVal.pK.getData()));
                    Log.m285d("AmexTAController", "InApp Payload length from Response" + response2.mRetVal.pK.getData().length);
                    generateInAppPaymentPayloadResponse.payload = Utils.toBase64(response2.mRetVal.pK.getData());
                    generateInAppPaymentPayloadResponse.tid = new String(response2.mRetVal.pL.getData());
                    if (DEBUG) {
                        Log.m285d("AmexTAController", "InApp Payload " + generateInAppPaymentPayloadResponse.payload);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e("AmexTAController", "initializeSecuritySetup failed");
        throw new AmexTAException(CNCCTAException.ERR_TZ_COM_ERR);
        return generateInAppPaymentPayloadResponse;
    }
}
