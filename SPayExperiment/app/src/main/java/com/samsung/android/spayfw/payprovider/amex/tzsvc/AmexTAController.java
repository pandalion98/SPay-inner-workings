/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAInfo;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.a;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.b;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.c;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.io.Struct;

public class AmexTAController
extends b {
    private static AmexTAController qq;
    private static AmexTAInfo qt;
    private a qr = null;
    private DevicePublicCerts qs = null;

    static {
        qt = new AmexTAInfo();
    }

    private AmexTAController(Context context) {
        super(context, qt);
    }

    public static AmexTAController C(Context context) {
        Class<AmexTAController> class_ = AmexTAController.class;
        synchronized (AmexTAController.class) {
            if (qq == null) {
                qq = new AmexTAController(context);
            }
            AmexTAController amexTAController = qq;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return amexTAController;
        }
    }

    public static AmexTAController ct() {
        Class<AmexTAController> class_ = AmexTAController.class;
        synchronized (AmexTAController.class) {
            AmexTAController amexTAController = qq;
            // ** MonitorExit[var2] (shouldn't be in output)
            return amexTAController;
        }
    }

    private boolean isSecuritySetupInitialized() {
        if (this.qs != null) {
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Device Certs already loaded)");
            }
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public GenerateInAppPaymentPayloadResponse a(String string, Object object) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            HashMap hashMap;
            GenerateInAppPaymentPayloadResponse generateInAppPaymentPayloadResponse;
            InAppTZTxnInfo inAppTZTxnInfo;
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                inAppTZTxnInfo = (InAppTZTxnInfo)object;
                if (inAppTZTxnInfo == null || inAppTZTxnInfo.txnAttributes == null) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processTokenData: Error: invalid input data");
                    throw new AmexTAException(983040);
                }
                hashMap = new HashMap();
                for (Map.Entry entry : inAppTZTxnInfo.txnAttributes.entrySet()) {
                    hashMap.put((Object)((String)entry.getKey()).getBytes(), (Object)((String)entry.getValue()).getBytes());
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (inAppTZTxnInfo.merchantCertificate == null) {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.GenerateInAppPaymentPayload.Request(string.getBytes(), (Map<byte[], byte[]>)hashMap, inAppTZTxnInfo.nonce));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                AmexCommands.GenerateInAppPaymentPayload.Response response = new AmexCommands.GenerateInAppPaymentPayload.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "GenerateInAppPaymentPayload Call Failed");
                    throw new AmexTAException(n2);
                }
                if (DEBUG) {
                    com.samsung.android.spayfw.b.c.d("AmexTAController", "GenerateInAppPaymentPayload called Successfully");
                }
                generateInAppPaymentPayloadResponse = new GenerateInAppPaymentPayloadResponse();
                com.samsung.android.spayfw.b.c.d("AmexTAController", "InApp Payload from Response " + Arrays.toString((byte[])response.mRetVal.pK.getData()));
                com.samsung.android.spayfw.b.c.d("AmexTAController", "InApp Payload length from Response" + response.mRetVal.pK.getData().length);
                generateInAppPaymentPayloadResponse.payload = c.toBase64(response.mRetVal.pK.getData());
                generateInAppPaymentPayloadResponse.tid = new String(response.mRetVal.pL.getData());
                if (DEBUG) {
                    com.samsung.android.spayfw.b.c.d("AmexTAController", "InApp Payload " + generateInAppPaymentPayloadResponse.payload);
                }
            } else {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "GenerateInAppJwePaymentPayload Call");
                List<byte[]> list = h.bE(inAppTZTxnInfo.merchantCertificate);
                if (list == null || list.isEmpty()) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "GenerateInAppJwePaymentPayload: cannot get certificate");
                    throw new AmexTAException(983042);
                }
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.GenerateInAppJwePaymentPayload.Request(string.getBytes(), (Map<byte[], byte[]>)hashMap, list));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                AmexCommands.GenerateInAppJwePaymentPayload.Response response = new AmexCommands.GenerateInAppJwePaymentPayload.Response(tACommandResponse);
                int n3 = (int)response.mRetVal.return_code.get();
                if (n3 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "GenerateInAppPaymentPayload Call Failed");
                    throw new AmexTAException(n3);
                }
                if (DEBUG) {
                    com.samsung.android.spayfw.b.c.d("AmexTAController", "GenerateInAppPaymentPayload called Successfully");
                }
                generateInAppPaymentPayloadResponse = new GenerateInAppPaymentPayloadResponse();
                com.samsung.android.spayfw.b.c.d("AmexTAController", "InApp Payload from Response " + Arrays.toString((byte[])response.mRetVal.pK.getData()));
                com.samsung.android.spayfw.b.c.d("AmexTAController", "InApp Payload length from Response" + response.mRetVal.pK.getData().length);
                generateInAppPaymentPayloadResponse.payload = c.toBase64(response.mRetVal.pK.getData());
                generateInAppPaymentPayloadResponse.tid = new String(response.mRetVal.pL.getData());
                if (DEBUG) {
                    com.samsung.android.spayfw.b.c.d("AmexTAController", "InApp Payload " + generateInAppPaymentPayloadResponse.payload);
                }
            }
            return generateInAppPaymentPayloadResponse;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ProcessTokenDataResponse a(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.ProcessTokenData.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling processTokenData");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessTokenData.Request(c.getDerChain(string), c.fromBase64(string2), c.fromBase64(string3), c.fromBase64(string4), string5.getBytes(), c.decodeHex(string6), c.decodeHex(string7)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.ProcessTokenData.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "ProcessTokenData Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "processTokenData called Successfully");
            }
            ProcessTokenDataResponse processTokenDataResponse = new ProcessTokenDataResponse();
            processTokenDataResponse.eAPDUBlob = c.toBase64(response.mRetVal.pE.getData());
            processTokenDataResponse.eNFCLUPCBlob = c.toBase64(response.mRetVal.qi.getData());
            processTokenDataResponse.eOtherLUPCBlob = c.toBase64(response.mRetVal.qj.getData());
            processTokenDataResponse.eMetadataBlob = c.toBase64(response.mRetVal.qk.getData());
            processTokenDataResponse.lupcMetadataBlob = new String(response.mRetVal.ql.getData());
            processTokenDataResponse.responseDataSignature = c.toBase64(response.mRetVal.qm.getData());
            return processTokenDataResponse;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ProcessTransactionResponse a(int n2, String string, String string2, String string3, String string4, String string5) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.ProcessTransaction.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling processTransaction");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessTransaction.Request(n2, c.fromBase64(string), c.fromBase64(string2), c.fromBase64(string3), c.fromBase64(string4), string5.getBytes()));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.ProcessTransaction.Response(tACommandResponse);
                int n3 = (int)response.mRetVal.return_code.get();
                if (n3 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "ProcessTransaction Call Failed");
                    throw new AmexTAException(n3);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "processTransaction called Successfully");
            }
            ProcessTransactionResponse processTransactionResponse = new ProcessTransactionResponse();
            processTransactionResponse.eNFCLUPCBlob = c.toBase64(response.mRetVal.qi.getData());
            processTransactionResponse.eOtherLUPCBlob = c.toBase64(response.mRetVal.qj.getData());
            processTransactionResponse.lupcMetaDataBlob = new String(response.mRetVal.qn.getData());
            processTransactionResponse.atcLUPC = c.encodeHex(response.mRetVal.qo.getData());
            processTransactionResponse.otherTID = new String(response.mRetVal.qp.getData());
            return processTransactionResponse;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String a(int n2, int n3, Map<String, String> map) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            TACommandResponse tACommandResponse;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling getNFCCryptogram");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                HashMap hashMap = new HashMap();
                for (Map.Entry entry : map.entrySet()) {
                    hashMap.put((Object)((String)entry.getKey()).getBytes(), (Object)((String)entry.getValue()).getBytes());
                }
                tACommandResponse = this.executeNoLoad(new AmexCommands.GetNFCCryptogram.Request(n2, n3, (Map<byte[], byte[]>)hashMap));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "getNFCCryptogram: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            AmexCommands.GetNFCCryptogram.Response response = new AmexCommands.GetNFCCryptogram.Response(tACommandResponse);
            int n4 = (int)response.mRetVal.return_code.get();
            if (n4 != 0) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "GetNFCCryptogram Call Failed");
                throw new AmexTAException(n4);
            }
            if (!DEBUG) return c.encodeHex(response.mRetVal.pP.getData());
            com.samsung.android.spayfw.b.c.d("AmexTAController", "getNFCCryptogram called Successfully");
            return c.encodeHex(response.mRetVal.pP.getData());
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public String a(String var1_1, boolean var2_2) {
        block12 : {
            block11 : {
                block14 : {
                    block13 : {
                        var12_3 = this;
                        // MONITORENTER : var12_3
                        if (AmexTAController.DEBUG) {
                            com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling processData");
                        }
                        if (!this.initializeSecuritySetup()) {
                            com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                            throw new AmexTAException(983040);
                        }
                        if (!var2_2) ** GOTO lbl18
                        if (var1_1 != null) break block13;
                        var11_4 = null;
                        ** GOTO lbl16
                    }
                    var11_4 = var1_1.getBytes();
lbl16: // 3 sources:
                    var4_5 = new AmexCommands.ProcessData.Request(var11_4, true);
                    break block14;
lbl18: // 1 sources:
                    var4_5 = new AmexCommands.ProcessData.Request(c.fromBase64(var1_1), false);
                }
                if ((var6_6 = this.executeNoLoad(var4_5)) != null && var6_6.mResponseCode == 0) break block11;
                com.samsung.android.spayfw.b.c.e("AmexTAController", "processData: Error: executeNoLoad failed");
                throw new AmexTAException(983040);
            }
            var7_8 = new AmexCommands.ProcessData.Response(var6_6);
            var8_9 = (int)var7_8.mRetVal.return_code.get();
            if (var8_9 != 0) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "ProcessData Call Failed");
                throw new AmexTAException(var8_9);
            }
            if (AmexTAController.DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "processData called Successfully");
            }
            if (!var2_2) break block12;
            var10_11 = var9_10 = c.toBase64(var7_8.mRetVal.data.getData());
            // MONITOREXIT : var12_3
            return var10_11;
        }
        try {
            return new String(var7_8.mRetVal.data.getData());
        }
        catch (Exception var5_7) {
            var5_7.printStackTrace();
            throw new AmexTAException(983040);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(int n2, byte[] arrby) {
        TACommandResponse tACommandResponse;
        boolean bl;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling transmitMstData");
        }
        if (!(bl = this.initializeSecuritySetup())) {
            com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(983040);
        }
        try {
            tACommandResponse = this.executeNoLoad(new AmexCommands.TransmitMstData.Request(n2, arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "Error: transmitMstData executeNoLoad failed");
                throw new AmexTAException(983040);
            }
            if (tACommandResponse.mResponseCode != 0) {
                throw new AmexTAException(tACommandResponse.mResponseCode);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new AmexTAException(983040);
        }
        long l2 = new AmexCommands.TransmitMstData.Response((TACommandResponse)tACommandResponse).mRetVal.return_code.get();
        if (l2 == 0L) {
            bl = true;
        }
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("AmexTAController", "TransmitMstData: ret = " + bl);
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String activateToken(String string) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.ActivateToken.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling suspendToken");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ActivateToken.Request(c.fromBase64(string)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "suspendToken: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.ActivateToken.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "ActivateToken Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (!DEBUG) return c.toBase64(response.mRetVal.pC.getData());
            com.samsung.android.spayfw.b.c.d("AmexTAController", "activateToken called Successfully");
            return c.toBase64(response.mRetVal.pC.getData());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ProcessRequestDataResponse b(String string, String string2, String string3) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            List<byte[]> list;
            byte[] arrby;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling processRequestData");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                list = c.getDerChain(string);
                arrby = string2 == null ? null : string2.getBytes();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            byte[] arrby2 = string3 == null ? null : string3.getBytes();
            TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessRequestData.Request(list, arrby, arrby2));
            if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "processRequestData: Error: executeNoLoad failed");
                throw new AmexTAException(983040);
            }
            AmexCommands.ProcessRequestData.Response response = new AmexCommands.ProcessRequestData.Response(tACommandResponse);
            int n2 = (int)response.mRetVal.return_code.get();
            if (n2 != 0) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "ProcessRequestData Call Failed");
                throw new AmexTAException(n2);
            }
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "processRequestData called Successfully");
            }
            ProcessRequestDataResponse processRequestDataResponse = new ProcessRequestDataResponse();
            processRequestDataResponse.encryptedRequestData = c.toBase64(response.mRetVal.pW.getData());
            processRequestDataResponse.encryptionParams = c.toBase64(response.mRetVal.pX.getData());
            processRequestDataResponse.requestDataSignature = c.toBase64(response.mRetVal.pY.getData());
            return processRequestDataResponse;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DevicePublicCerts cu() {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            if (this.initializeSecuritySetup()) return this.qs;
            com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
            throw new AmexTAException(983040);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public EphemeralKeyInfo cv() {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.GenerateECDHKeys.Response response;
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.GenerateECDHKeys.Request());
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "generateECDHKeys: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.GenerateECDHKeys.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "GenerateECDHKeys Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            EphemeralKeyInfo ephemeralKeyInfo = new EphemeralKeyInfo();
            ephemeralKeyInfo.ephemeralPublicKey = c.toBase64(response.mRetVal.pF.getData());
            ephemeralKeyInfo.encryptedEphemeralPrivateKey = c.encodeHex(response.mRetVal.pG.getData());
            return ephemeralKeyInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean cw() {
        boolean bl = false;
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            block11 : {
                long l2;
                if (DEBUG) {
                    com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling clearLUPC");
                }
                if (!this.initializeSecuritySetup()) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                    throw new AmexTAException(983040);
                }
                try {
                    TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ClearLUPC.Request());
                    if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                        com.samsung.android.spayfw.b.c.e("AmexTAController", "Error: clearLUPC executeNoLoad failed");
                        break block11;
                    }
                    AmexCommands.ClearLUPC.Response response = new AmexCommands.ClearLUPC.Response(tACommandResponse);
                    int n2 = (int)response.mRetVal.return_code.get();
                    if (n2 != 0) {
                        com.samsung.android.spayfw.b.c.e("AmexTAController", "ClearLUPC Call Failed");
                        throw new AmexTAException(n2);
                    }
                    if (DEBUG) {
                        com.samsung.android.spayfw.b.c.d("AmexTAController", "clearLUPC: success ");
                    }
                    l2 = response.mRetVal.return_code.get();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    throw new AmexTAException(983040);
                }
                long l3 = l2 LCMP 0L;
                bl = false;
                if (l3 != false) return bl;
                return true;
            }
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String decryptTokenData(String string) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.DecryptTokenData.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling decryptTokenData");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.DecryptTokenData.Request(c.fromBase64(string)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processTokenData: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.DecryptTokenData.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "DecryptTokenData Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (!DEBUG) return new String(response.mRetVal.blob.getData());
            com.samsung.android.spayfw.b.c.d("AmexTAController", "decryptTokenData called Successfully");
            return new String(response.mRetVal.blob.getData());
        }
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            com.samsung.android.spayfw.b.c.e("AmexTAController", "Error: init failed");
            return false;
        }
        this.qr = new a(this);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean initializeSecuritySetup() {
        block10 : {
            block9 : {
                if (!this.isSecuritySetupInitialized()) {
                    if (DEBUG) {
                        com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling initializeSecuritySetup");
                    }
                    if (!this.isTALoaded()) {
                        com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup: Error: TA is not loaded, please call loadTA() API first!");
                        return false;
                    }
                    if (!this.qr.isLoaded()) {
                        com.samsung.android.spayfw.b.c.d("AmexTAController", "mAmexDeviceCerts is not loaded");
                        if (!this.qr.load()) {
                            com.samsung.android.spayfw.b.c.e("AmexTAController", "Error: Amex Device Certs Load failed");
                            return false;
                        }
                    }
                    byte[] arrby = this.qr.getDevicePrivateSignCert();
                    byte[] arrby2 = this.qr.getDevicePrivateEncryptionCert();
                    try {
                        TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.LoadCerts.Request(arrby, arrby2));
                        if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                            com.samsung.android.spayfw.b.c.e("AmexTAController", "loadAllCerts: Error: executeNoLoad failed");
                            throw new AmexTAException(983040);
                        }
                        AmexCommands.LoadCerts.Response response = new AmexCommands.LoadCerts.Response(tACommandResponse);
                        this.qs = new DevicePublicCerts();
                        this.qs.deviceCertificate = c.convertToPem(response.mRetVal.deviceRootRSA2048PubCert.getData(), false);
                        this.qs.deviceSigningCertificate = c.convertToPem(response.mRetVal.deviceSignRSA2048PubCert.getData(), false);
                        this.qs.deviceEncryptionCertificate = c.convertToPem(response.mRetVal.pR.getData(), false);
                        if (!DEBUG) break block9;
                        break block10;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        throw new AmexTAException(983040);
                    }
                }
            }
            return true;
        }
        com.samsung.android.spayfw.b.c.d("AmexTAController", "initializeSecuritySetup called Successfully");
        return true;
    }

    @Override
    public boolean loadTA() {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            boolean bl = super.loadTA();
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String o(String string, String string2) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.ProcessResponseData.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling processRequestData");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ProcessResponseData.Request(c.fromBase64(string), c.fromBase64(string2)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "processRequestData: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.ProcessResponseData.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "ProcessResponseData Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (!DEBUG) return c.toBase64(response.mRetVal.qb.getData());
            com.samsung.android.spayfw.b.c.d("AmexTAController", "processRequestData called Successfully");
            return c.toBase64(response.mRetVal.qb.getData());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String resumeToken(String string) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.ResumeToken.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling suspendToken");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.ResumeToken.Request(c.fromBase64(string)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "suspendToken: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.ResumeToken.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "ResumeToken Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (!DEBUG) return c.toBase64(response.mRetVal.pC.getData());
            com.samsung.android.spayfw.b.c.d("AmexTAController", "resumeToken called Successfully");
            return c.toBase64(response.mRetVal.pC.getData());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String suspendToken(String string) {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            AmexCommands.SuspendToken.Response response;
            if (DEBUG) {
                com.samsung.android.spayfw.b.c.d("AmexTAController", "Calling suspendToken");
            }
            if (!this.initializeSecuritySetup()) {
                com.samsung.android.spayfw.b.c.e("AmexTAController", "initializeSecuritySetup failed");
                throw new AmexTAException(983040);
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new AmexCommands.SuspendToken.Request(c.fromBase64(string)));
                if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "suspendToken: Error: executeNoLoad failed");
                    throw new AmexTAException(983040);
                }
                response = new AmexCommands.SuspendToken.Response(tACommandResponse);
                int n2 = (int)response.mRetVal.return_code.get();
                if (n2 != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexTAController", "SuspendToken Call Failed");
                    throw new AmexTAException(n2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new AmexTAException(983040);
            }
            if (!DEBUG) return c.toBase64(response.mRetVal.pC.getData());
            com.samsung.android.spayfw.b.c.d("AmexTAController", "suspendToken called Successfully");
            return c.toBase64(response.mRetVal.pC.getData());
        }
    }

    @Override
    public void unloadTA() {
        AmexTAController amexTAController = this;
        synchronized (amexTAController) {
            this.qs = null;
            super.unloadTA();
            return;
        }
    }

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

}

