/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Class
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.content.Context;
import android.spay.TACommandResponse;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAException;

import java.util.List;

public class b
extends TAController {
    private static b xE = null;
    private a xF;

    protected b(Context context, int n2, byte[] arrby) {
        super(context, new c());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b E(Context context) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (xE != null) return xE;
            xE = new b(context, 1, DcTACommands.TL_MAGIC_NUM);
            if (xE != null) return xE;
            throw new TAException("Failed to create DcTAController", -1);
        }
    }

    private TACommandResponse a(DcTaCommandRequest dcTaCommandRequest) {
        Log.d("DcTAController", "executeNoLoadInternal, start");
        if (!this.isTALoaded()) {
            throw new DcTAException(DcTAException.Code.xI.ey(), DcTAException.Code.xI.getCode());
        }
        if (dcTaCommandRequest == null || !dcTaCommandRequest.validate()) {
            throw new DcTAException(DcTAException.Code.xJ.ey(), DcTAException.Code.xJ.getCode());
        }
        TACommandResponse tACommandResponse = this.executeNoLoad(dcTaCommandRequest);
        if (tACommandResponse == null) {
            throw new DcTAException(DcTAException.Code.xL.ey(), DcTAException.Code.xL.getCode());
        }
        return tACommandResponse;
    }

    private boolean a(DcTaCommandResponse dcTaCommandResponse) {
        if (dcTaCommandResponse == null) {
            throw new DcTAException("Invalid response input", DcTAException.Code.xJ.getCode());
        }
        if (!dcTaCommandResponse.validate()) {
            Log.d("DcTAController", "responseValidate, TA command return code :" + dcTaCommandResponse.getReturnCode());
            throw DcTAException.b(dcTaCommandResponse.getReturnCode(), dcTaCommandResponse.ez());
        }
        return true;
    }

    public static b eu() {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            b b2 = xE;
            // ** MonitorExit[var2] (shouldn't be in output)
            return b2;
        }
    }

    public DcTACommands.ProcessCardProfile.Response.a a(byte[] arrby, List<byte[]> list, boolean bl) {
        if (!this.xF.ej()) {
            throw new DcTAException(DcTAException.Code.xK.ey(), DcTAException.Code.xK.getCode());
        }
        DcTACommands.ProcessCardProfile.Response response = new DcTACommands.ProcessCardProfile.Response(this.a(new DcTACommands.ProcessCardProfile.Request(arrby, this.xF.getDevicePrivateEncryptionCert(), list, bl)));
        this.a(response);
        return (DcTACommands.ProcessCardProfile.Response.a)response.yX;
    }

    public DcTACommands.ReplenishContextEncryption.Response.a a(byte[] arrby, List<byte[]> list, String string) {
        if (!this.xF.ej()) {
            throw new DcTAException(DcTAException.Code.xK.ey(), DcTAException.Code.xK.getCode());
        }
        DcTACommands.ReplenishContextEncryption.Response response = new DcTACommands.ReplenishContextEncryption.Response(this.a(new DcTACommands.ReplenishContextEncryption.Request(arrby, this.xF.getDevicePrivateEncryptionCert(), list, string)));
        this.a(response);
        return (DcTACommands.ReplenishContextEncryption.Response.a)response.yX;
    }

    public String a(String string, List<byte[]> list) {
        if (!this.xF.ej()) {
            throw new DcTAException(DcTAException.Code.xK.ey(), DcTAException.Code.xK.getCode());
        }
        byte[] arrby = this.xF.getDevicePrivateEncryptionCert();
        DcTACommands.ProcessAcctTxns.Response response = new DcTACommands.ProcessAcctTxns.Response(this.a(new DcTACommands.ProcessAcctTxns.Request(string.getBytes(), arrby, list)));
        this.a(response);
        return response.ep();
    }

    public byte[] a(byte[] arrby, DcTACommands.ProcessDataOperationType processDataOperationType) {
        if (!this.xF.ej()) {
            throw new DcTAException(DcTAException.Code.xK.ey(), DcTAException.Code.xK.getCode());
        }
        byte[] arrby2 = this.xF.getDevicePrivateEncryptionCert();
        return new DcTACommands.ProcessDataGeneric.Response(this.a(new DcTACommands.ProcessDataGeneric.Request(processDataOperationType, DcTACommands.ProcessDataDataType.xq, arrby, arrby2, "None"))).getSignature();
    }

    public long authenticateTransaction(byte[] arrby) {
        Log.d("DcTAController", "authenticateTransaction, start");
        DcTACommands.DiscoverTAAuthenticateTransaction.DiscoverTAAuthenticateTransactionResponse discoverTAAuthenticateTransactionResponse = new DcTACommands.DiscoverTAAuthenticateTransaction.DiscoverTAAuthenticateTransactionResponse(this.a(new DcTACommands.DiscoverTAAuthenticateTransaction.DiscoverTAAuthenticateTransactionRequest(arrby)));
        this.a(discoverTAAuthenticateTransactionResponse);
        return discoverTAAuthenticateTransactionResponse.ek();
    }

    public long b(int n2, byte[] arrby) {
        Log.d("DcTAController", "DCTAController: transmitMSTTracks");
        DcTACommands.DiscoverTATransmitMst.DiscoverTATransmitMstResponse discoverTATransmitMstResponse = new DcTACommands.DiscoverTATransmitMst.DiscoverTATransmitMstResponse(this.a(new DcTACommands.DiscoverTATransmitMst.DiscoverTATransmitMstRequest(n2, arrby)));
        this.a(discoverTATransmitMstResponse);
        return discoverTATransmitMstResponse.eo();
    }

    public DcTACommands.CardCtxEncryption.Response.a b(byte[] arrby, List<byte[]> list) {
        DcTACommands.CardCtxEncryption.Response response = new DcTACommands.CardCtxEncryption.Response(this.a(new DcTACommands.CardCtxEncryption.Request(arrby, list)));
        this.a(response);
        return (DcTACommands.CardCtxEncryption.Response.a)response.yX;
    }

    public DcTACommands.ProcessReplenishmentData.Response.a b(byte[] arrby, byte[] arrby2, List<byte[]> list, boolean bl) {
        if (!this.xF.ej()) {
            throw new DcTAException(DcTAException.Code.xK.ey(), DcTAException.Code.xK.getCode());
        }
        DcTACommands.ProcessReplenishmentData.Response response = new DcTACommands.ProcessReplenishmentData.Response(this.a(new DcTACommands.ProcessReplenishmentData.Request(arrby, arrby2, this.xF.getDevicePrivateEncryptionCert(), list, bl)));
        this.a(response);
        return (DcTACommands.ProcessReplenishmentData.Response.a)response.yX;
    }

    public DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataResponse c(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        Log.d("DcTAController", "getInAppData, start");
        if (arrby != null) {
            Log.d("DcTAController", "getInAppData, otpk bundle not null");
        }
        if (arrby2 != null) {
            Log.d("DcTAController", "getInAppData, secureObject bundle not null");
        }
        if (arrby3 != null) {
            Log.d("DcTAController", "getInAppData, timestamp not null");
        }
        DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataResponse discoverTAGetInAppDataResponse = new DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataResponse(this.a(new DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataRequest(arrby, arrby2, arrby3)));
        this.a(discoverTAGetInAppDataResponse);
        Log.d("DcTAController", "getInAppData, end");
        return discoverTAGetInAppDataResponse;
    }

    public byte[] c(byte[] arrby, byte[] arrby2) {
        Log.d("DcTAController", "computeAppCryptogram, start");
        DcTACommands.DiscoverTAComputeAppCryptogram.DiscoverTAComputeAppCryptogramResponse discoverTAComputeAppCryptogramResponse = new DcTACommands.DiscoverTAComputeAppCryptogram.DiscoverTAComputeAppCryptogramResponse(this.a(new DcTACommands.DiscoverTAComputeAppCryptogram.DiscoverTAComputeAppCryptogramRequest(arrby, arrby2)));
        this.a(discoverTAComputeAppCryptogramResponse);
        return discoverTAComputeAppCryptogramResponse.getCryptogram();
    }

    public DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataResponse d(byte[] arrby, byte[] arrby2) {
        Log.d("DcTAController", "getInAppCnccData, start");
        DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataResponse discoverTAGetInAppCnccDataResponse = new DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataResponse(this.a(new DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataRequest(arrby, arrby2)));
        this.a(discoverTAGetInAppCnccDataResponse);
        Log.d("DcTAController", "getInAppCnccData, end");
        return discoverTAGetInAppCnccDataResponse;
    }

    public DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse.a e(byte[] arrby, byte[] arrby2) {
        Log.d("DcTAController", "initiateTransaction, start");
        DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse discoverTAInitiateTransactionResponse = new DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse(this.a(new DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionRequest(arrby, arrby2)));
        this.a(discoverTAInitiateTransactionResponse);
        return (DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse.a)discoverTAInitiateTransactionResponse.yX;
    }

    public DcTACommands.DevicePublicKeyCtxEncryption.Response.a ev() {
        if (!this.xF.ej()) {
            throw new DcTAException(DcTAException.Code.xK.ey(), DcTAException.Code.xK.getCode());
        }
        DcTACommands.DevicePublicKeyCtxEncryption.Response response = new DcTACommands.DevicePublicKeyCtxEncryption.Response(this.a(new DcTACommands.DevicePublicKeyCtxEncryption.Request(this.xF.getDevicePrivateEncryptionCert())));
        this.a(response);
        return (DcTACommands.DevicePublicKeyCtxEncryption.Response.a)response.yX;
    }

    public long ew() {
        Log.d("DcTAController", "DCTAController: composeMSTtracks");
        DcTACommands.DiscoverTAComposeMST.DiscoverTAComposeMSTResponse discoverTAComposeMSTResponse = new DcTACommands.DiscoverTAComposeMST.DiscoverTAComposeMSTResponse(this.a(new DcTACommands.DiscoverTAComposeMST.DiscoverTAComposeMSTRequest(0)));
        this.a(discoverTAComposeMSTResponse);
        return discoverTAComposeMSTResponse.em();
    }

    public long ex() {
        Log.d("DcTAController", "DCTAController: clearMstData: calling clearMstData...");
        DcTACommands.DiscoverTAClearMST.DiscoverTAClearMSTResponse discoverTAClearMSTResponse = new DcTACommands.DiscoverTAClearMST.DiscoverTAClearMSTResponse(this.a(new DcTACommands.DiscoverTAClearMST.DiscoverTAClearMSTRequest(0)));
        this.a(discoverTAClearMSTResponse);
        return discoverTAClearMSTResponse.el();
    }

    @Override
    protected boolean init() {
        if (super.init()) {
            this.xF = new a(b.eu());
            return true;
        }
        return false;
    }

    public byte[] k(byte[] arrby) {
        Log.d("DcTAController", "getNonce, execute TA command");
        DcTACommands.DiscoverTAGetNonceCommand.DiscoverTAGetNonceResponse discoverTAGetNonceResponse = new DcTACommands.DiscoverTAGetNonceCommand.DiscoverTAGetNonceResponse(this.a(new DcTACommands.DiscoverTAGetNonceCommand.DiscoverTAGetNonceRequest(arrby)));
        this.a(discoverTAGetNonceResponse);
        return discoverTAGetNonceResponse.getNonce();
    }

    public DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvResponse.a l(byte[] arrby) {
        Log.d("DcTAController", "computeDcvv, start");
        DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvResponse discoverTAComputeDcvvResponse = new DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvResponse(this.a(new DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvRequest(arrby)));
        this.a(discoverTAComputeDcvvResponse);
        return (DcTACommands.DiscoverTAComputeDcvv.DiscoverTAComputeDcvvResponse.a)discoverTAComputeDcvvResponse.yX;
    }
}

