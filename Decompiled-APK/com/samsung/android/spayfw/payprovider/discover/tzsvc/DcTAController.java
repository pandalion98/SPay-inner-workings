package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.content.Context;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.CardCtxEncryption.Request;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.CardCtxEncryption.Response;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.CardCtxEncryption.Response.C0511a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DevicePublicKeyCtxEncryption;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DevicePublicKeyCtxEncryption.Response.C0513a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAAuthenticateTransaction.DiscoverTAAuthenticateTransactionRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAAuthenticateTransaction.DiscoverTAAuthenticateTransactionResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAClearMST.DiscoverTAClearMSTRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAClearMST.DiscoverTAClearMSTResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComposeMST.DiscoverTAComposeMSTRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComposeMST.DiscoverTAComposeMSTResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComputeAppCryptogram.DiscoverTAComputeAppCryptogramRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComputeAppCryptogram.DiscoverTAComputeAppCryptogramResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvResponse.C0520a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetNonceCommand.DiscoverTAGetNonceRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetNonceCommand.DiscoverTAGetNonceResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse.C0528a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTATransmitMst.DiscoverTATransmitMstRequest;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTATransmitMst.DiscoverTATransmitMstResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessAcctTxns;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessCardProfile;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessCardProfile.Response.C0533a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessDataDataType;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessDataGeneric;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessDataOperationType;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessReplenishmentData;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessReplenishmentData.Response.C0537a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ReplenishContextEncryption;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ReplenishContextEncryption.Response.C0539a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException.Code;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAException;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.tzsvc.b */
public class DcTAController extends TAController {
    private static DcTAController xE;
    private DcDeviceCerts xF;

    static {
        xE = null;
    }

    protected DcTAController(Context context, int i, byte[] bArr) {
        super(context, new DcTAInfo());
    }

    public static synchronized DcTAController m1039E(Context context) {
        DcTAController dcTAController;
        synchronized (DcTAController.class) {
            if (xE == null) {
                xE = new DcTAController(context, 1, DcTACommands.TL_MAGIC_NUM);
                if (xE == null) {
                    throw new TAException("Failed to create DcTAController", -1);
                }
            }
            dcTAController = xE;
        }
        return dcTAController;
    }

    protected boolean init() {
        if (!super.init()) {
            return false;
        }
        this.xF = new DcDeviceCerts(DcTAController.eu());
        return true;
    }

    public static synchronized DcTAController eu() {
        DcTAController dcTAController;
        synchronized (DcTAController.class) {
            dcTAController = xE;
        }
        return dcTAController;
    }

    private TACommandResponse m1040a(DcTaCommandRequest dcTaCommandRequest) {
        Log.m285d("DcTAController", "executeNoLoadInternal, start");
        if (!isTALoaded()) {
            throw new DcTAException(Code.ERR_TA_NOT_LOADED.ey(), Code.ERR_TA_NOT_LOADED.getCode());
        } else if (dcTaCommandRequest == null || !dcTaCommandRequest.validate()) {
            throw new DcTAException(Code.ERR_INVALID_INPUT.ey(), Code.ERR_INVALID_INPUT.getCode());
        } else {
            TACommandResponse executeNoLoad = executeNoLoad(dcTaCommandRequest);
            if (executeNoLoad != null) {
                return executeNoLoad;
            }
            throw new DcTAException(Code.ERR_TA_FAILED.ey(), Code.ERR_TA_FAILED.getCode());
        }
    }

    public C0511a m1047b(byte[] bArr, List<byte[]> list) {
        DcTaCommandResponse response = new Response(m1040a(new Request(bArr, list)));
        m1041a(response);
        return (C0511a) response.yX;
    }

    public C0513a ev() {
        if (this.xF.ej()) {
            DcTaCommandResponse response = new DevicePublicKeyCtxEncryption.Response(m1040a(new DevicePublicKeyCtxEncryption.Request(this.xF.getDevicePrivateEncryptionCert())));
            m1041a(response);
            return (C0513a) response.yX;
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED.ey(), Code.ERR_LOAD_CERT_FAILED.getCode());
    }

    public C0533a m1042a(byte[] bArr, List<byte[]> list, boolean z) {
        if (this.xF.ej()) {
            DcTaCommandResponse response = new ProcessCardProfile.Response(m1040a(new ProcessCardProfile.Request(bArr, this.xF.getDevicePrivateEncryptionCert(), list, z)));
            m1041a(response);
            return (C0533a) response.yX;
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED.ey(), Code.ERR_LOAD_CERT_FAILED.getCode());
    }

    public C0539a m1043a(byte[] bArr, List<byte[]> list, String str) {
        if (this.xF.ej()) {
            DcTaCommandResponse response = new ReplenishContextEncryption.Response(m1040a(new ReplenishContextEncryption.Request(bArr, this.xF.getDevicePrivateEncryptionCert(), list, str)));
            m1041a(response);
            return (C0539a) response.yX;
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED.ey(), Code.ERR_LOAD_CERT_FAILED.getCode());
    }

    public C0537a m1048b(byte[] bArr, byte[] bArr2, List<byte[]> list, boolean z) {
        if (this.xF.ej()) {
            DcTaCommandResponse response = new ProcessReplenishmentData.Response(m1040a(new ProcessReplenishmentData.Request(bArr, bArr2, this.xF.getDevicePrivateEncryptionCert(), list, z)));
            m1041a(response);
            return (C0537a) response.yX;
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED.ey(), Code.ERR_LOAD_CERT_FAILED.getCode());
    }

    public String m1044a(String str, List<byte[]> list) {
        if (this.xF.ej()) {
            DcTaCommandResponse response = new ProcessAcctTxns.Response(m1040a(new ProcessAcctTxns.Request(str.getBytes(), this.xF.getDevicePrivateEncryptionCert(), list)));
            m1041a(response);
            return response.ep();
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED.ey(), Code.ERR_LOAD_CERT_FAILED.getCode());
    }

    public byte[] m1045a(byte[] bArr, ProcessDataOperationType processDataOperationType) {
        if (this.xF.ej()) {
            return new ProcessDataGeneric.Response(m1040a(new ProcessDataGeneric.Request(processDataOperationType, ProcessDataDataType.USER_SIGNATURE, bArr, this.xF.getDevicePrivateEncryptionCert(), PaymentNetworkProvider.AUTHTYPE_NONE))).getSignature();
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED.ey(), Code.ERR_LOAD_CERT_FAILED.getCode());
    }

    public byte[] m1053k(byte[] bArr) {
        Log.m285d("DcTAController", "getNonce, execute TA command");
        DcTaCommandResponse discoverTAGetNonceResponse = new DiscoverTAGetNonceResponse(m1040a(new DiscoverTAGetNonceRequest(bArr)));
        m1041a(discoverTAGetNonceResponse);
        return discoverTAGetNonceResponse.getNonce();
    }

    public long authenticateTransaction(byte[] bArr) {
        Log.m285d("DcTAController", "authenticateTransaction, start");
        DcTaCommandResponse discoverTAAuthenticateTransactionResponse = new DiscoverTAAuthenticateTransactionResponse(m1040a(new DiscoverTAAuthenticateTransactionRequest(bArr)));
        m1041a(discoverTAAuthenticateTransactionResponse);
        return discoverTAAuthenticateTransactionResponse.ek();
    }

    public C0520a m1054l(byte[] bArr) {
        Log.m285d("DcTAController", "computeDcvv, start");
        DcTaCommandResponse discoverTAComputeDcvvResponse = new DiscoverTAComputeDcvvResponse(m1040a(new DiscoverTAComputeDcvvRequest(bArr)));
        m1041a(discoverTAComputeDcvvResponse);
        return (C0520a) discoverTAComputeDcvvResponse.yX;
    }

    public byte[] m1050c(byte[] bArr, byte[] bArr2) {
        Log.m285d("DcTAController", "computeAppCryptogram, start");
        DcTaCommandResponse discoverTAComputeAppCryptogramResponse = new DiscoverTAComputeAppCryptogramResponse(m1040a(new DiscoverTAComputeAppCryptogramRequest(bArr, bArr2)));
        m1041a(discoverTAComputeAppCryptogramResponse);
        return discoverTAComputeAppCryptogramResponse.getCryptogram();
    }

    public DiscoverTAGetInAppDataResponse m1049c(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        Log.m285d("DcTAController", "getInAppData, start");
        if (bArr != null) {
            Log.m285d("DcTAController", "getInAppData, otpk bundle not null");
        }
        if (bArr2 != null) {
            Log.m285d("DcTAController", "getInAppData, secureObject bundle not null");
        }
        if (bArr3 != null) {
            Log.m285d("DcTAController", "getInAppData, timestamp not null");
        }
        DcTaCommandResponse discoverTAGetInAppDataResponse = new DiscoverTAGetInAppDataResponse(m1040a(new DiscoverTAGetInAppDataRequest(bArr, bArr2, bArr3)));
        m1041a(discoverTAGetInAppDataResponse);
        Log.m285d("DcTAController", "getInAppData, end");
        return discoverTAGetInAppDataResponse;
    }

    public DiscoverTAGetInAppCnccDataResponse m1051d(byte[] bArr, byte[] bArr2) {
        Log.m285d("DcTAController", "getInAppCnccData, start");
        DcTaCommandResponse discoverTAGetInAppCnccDataResponse = new DiscoverTAGetInAppCnccDataResponse(m1040a(new DiscoverTAGetInAppCnccDataRequest(bArr, bArr2)));
        m1041a(discoverTAGetInAppCnccDataResponse);
        Log.m285d("DcTAController", "getInAppCnccData, end");
        return discoverTAGetInAppCnccDataResponse;
    }

    public C0528a m1052e(byte[] bArr, byte[] bArr2) {
        Log.m285d("DcTAController", "initiateTransaction, start");
        DcTaCommandResponse discoverTAInitiateTransactionResponse = new DiscoverTAInitiateTransactionResponse(m1040a(new DiscoverTAInitiateTransactionRequest(bArr, bArr2)));
        m1041a(discoverTAInitiateTransactionResponse);
        return (C0528a) discoverTAInitiateTransactionResponse.yX;
    }

    public long ew() {
        Log.m285d("DcTAController", "DCTAController: composeMSTtracks");
        DcTaCommandResponse discoverTAComposeMSTResponse = new DiscoverTAComposeMSTResponse(m1040a(new DiscoverTAComposeMSTRequest(0)));
        m1041a(discoverTAComposeMSTResponse);
        return discoverTAComposeMSTResponse.em();
    }

    public long m1046b(int i, byte[] bArr) {
        Log.m285d("DcTAController", "DCTAController: transmitMSTTracks");
        DcTaCommandResponse discoverTATransmitMstResponse = new DiscoverTATransmitMstResponse(m1040a(new DiscoverTATransmitMstRequest(i, bArr)));
        m1041a(discoverTATransmitMstResponse);
        return discoverTATransmitMstResponse.eo();
    }

    public long ex() {
        Log.m285d("DcTAController", "DCTAController: clearMstData: calling clearMstData...");
        DcTaCommandResponse discoverTAClearMSTResponse = new DiscoverTAClearMSTResponse(m1040a(new DiscoverTAClearMSTRequest(0)));
        m1041a(discoverTAClearMSTResponse);
        return discoverTAClearMSTResponse.el();
    }

    private boolean m1041a(DcTaCommandResponse dcTaCommandResponse) {
        if (dcTaCommandResponse == null) {
            throw new DcTAException("Invalid response input", Code.ERR_INVALID_INPUT.getCode());
        } else if (dcTaCommandResponse.validate()) {
            return true;
        } else {
            Log.m285d("DcTAController", "responseValidate, TA command return code :" + dcTaCommandResponse.getReturnCode());
            throw DcTAException.m1038b(dcTaCommandResponse.getReturnCode(), dcTaCommandResponse.ez());
        }
    }
}
