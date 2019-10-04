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
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package com.visa.tainterface;

import android.content.Context;
import android.os.Build;
import android.spay.CertInfo;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.visa.tainterface.VisaCommands;
import com.visa.tainterface.VisaTAException;
import com.visa.tainterface.b;
import com.visa.tainterface.c;
import javolution.io.Struct;

public class VisaTAController
extends TAController {
    private static VisaTAController ME;
    public static final String MF;
    public static TAInfo TA_INFO;
    private static final boolean bQC;
    private a MH = null;
    private byte[] MI = null;
    private byte[] MJ = null;
    private b MK = null;
    private CertInfo certsInfoCache = null;

    /*
     * Enabled aggressive block sorting
     */
    static {
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        String string = bQC ? "visa_pay" : "ffffffff00000000000000000000001c";
        MF = string;
        TA_INFO = new c();
    }

    private VisaTAController(Context context) {
        super(context, TA_INFO);
    }

    private boolean b(byte[] arrby, com.visa.tainterface.a a2) {
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: _encrypted_luk length invalid! _encrypted_luk.len = " + arrby.length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: _encrypted_luk is null!");
            return false;
        }
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: trackData is null!");
            return false;
        }
        if (a2.ii() == null || a2.ii().length > 256) {
            if (a2.ii() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: tokenBytes length invalid! tokenBytes.len = " + a2.ii().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: tokenBytes is null!");
            return false;
        }
        if (a2.ij() == null || a2.ij().length > 256) {
            if (a2.ij() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: TokenExpirationDate length invalid! TokenExpirationDate.len = " + a2.ij().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: TokenExpirationDate is null!");
            return false;
        }
        if (a2.ik() == null || a2.ik().length > 256) {
            if (a2.ik() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: ServiceCode length invalid! ServiceCode.len = " + a2.ik().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: ServiceCode is null!");
            return false;
        }
        if (a2.il() == null || a2.il().length > 256) {
            if (a2.il() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: Timestamp length invalid! Timestamp.len = " + a2.il().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: Timestamp is null!");
            return false;
        }
        if (a2.im() == null || a2.im().length > 256) {
            if (a2.im() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: SequenceCounter length invalid! SequenceCounter.len = " + a2.im().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: SequenceCounter is null!");
            return false;
        }
        if (a2.in() == null || a2.in().length > 256) {
            if (a2.in() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: AppTransactionCounter length invalid! AppTransactionCounter.len = " + a2.in().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: AppTransactionCounter is null!");
            return false;
        }
        if (a2.io() == null || a2.io().length > 256) {
            if (a2.io() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: CVV length invalid! CVV.len = " + a2.io().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: CVV is null!");
            return false;
        }
        if (a2.ip() == null || a2.ip().length > 256) {
            if (a2.ip() != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: ReservedBytes length invalid! ReservedBytes.len = " + a2.ip().length);
                return false;
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "validateInputPrepareMstData: Error: ReservedBytes is null!");
            return false;
        }
        return true;
    }

    public static VisaTAController bv(Context context) {
        Class<VisaTAController> class_ = VisaTAController.class;
        synchronized (VisaTAController.class) {
            if (ME == null) {
                ME = new VisaTAController(context);
            }
            VisaTAController visaTAController = ME;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return visaTAController;
        }
    }

    public static VisaTAController iq() {
        Class<VisaTAController> class_ = VisaTAController.class;
        synchronized (VisaTAController.class) {
            VisaTAController visaTAController = ME;
            // ** MonitorExit[var2] (shouldn't be in output)
            return visaTAController;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void ir() {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling loadAllCerts");
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "loadAllCerts: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        if (!this.MK.isLoaded()) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "mVisaDeviceCerts is not loaded");
            if (!this.MK.load()) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "Error: Visa Device Certs Load failed");
                throw new VisaTAException("Error loading Visa Certs", 2);
            }
        }
        byte[] arrby = this.MK.getDevicePrivateSignCert();
        byte[] arrby2 = this.MK.getDevicePrivateEncryptionCert();
        if (arrby == null || arrby2 == null) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "loadAllCerts: Error: Certificate Data is NULL");
            this.certsInfoCache = null;
            throw new VisaTAException("Error loading Visa Certs", 2);
        }
        if (arrby.length > 4096 || arrby2.length > 4096 || this.MI != null && this.MI.length > 4096 || this.MJ != null && this.MJ.length > 4096) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "loadAllCerts: Error: certs length invalid! - signCertData.length = " + arrby.length + "; encryptCertData.length = " + arrby2.length);
            this.certsInfoCache = null;
            throw new VisaTAException("Error loading Visa Certs", 2);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.LoadCerts.Request(arrby, arrby2, this.MI, this.MJ));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "loadAllCerts: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "loadAllCerts: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            {
                throw new VisaTAException("Invalid Input", 5);
            }
        }
        VisaCommands.LoadCerts.Response response = new VisaCommands.LoadCerts.Response(tACommandResponse);
        int n2 = (int)response.Mf.return_code.get();
        if (n2 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing LoadCerts");
            throw new VisaTAException("Error processing LoadCerts", n2);
        }
        this.MH = new a(response.Mf.cert_drk.getData(), response.Mf.cert_sign.getData(), response.Mf.cert_encrypt.getData(), response.Mf.Mg.get(), response.Mf.Mh.get(), response.Mf.Mi.getData());
        if (!DEBUG) return;
        {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "loadAllCerts called Successfully");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(int n2, byte[] arrby) {
        TACommandResponse tACommandResponse;
        boolean bl = true;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling transmitMstData");
        }
        if (arrby == null || arrby.length > 28) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "transmitMstData: Error: config length invalid! config.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "transmitMstData: Error: config is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "transmitMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", (int)bl);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.TransmitMstData.Request(n2, arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "Error: transmitMstData executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "transmitMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        long l2 = new VisaCommands.TransmitMstData.Response((TACommandResponse)tACommandResponse).MD.return_code.get();
        if (l2 != 0L) {
            bl = false;
        }
        if (!DEBUG) return bl;
        com.samsung.android.spayfw.b.c.d("VisaTAController", "TransmitMstData: ret = " + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(byte[] arrby, com.visa.tainterface.a a2) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling prepareMstData");
        }
        if (!this.b(arrby, a2)) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareMstData: Error: one of the inputs is invalid!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.PrepareMstData.Request(a2.ii(), a2.ij(), a2.ik(), a2.il(), a2.im(), a2.in(), a2.io(), a2.ip(), arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareMstData: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            {
                throw new VisaTAException("Invalid Input", 5);
            }
        }
        VisaCommands.PrepareMstData.Response response = new VisaCommands.PrepareMstData.Response(tACommandResponse);
        int n2 = (int)response.Mt.return_code.get();
        if (n2 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing PrepareMstData");
            throw new VisaTAException("Error processing PrepareMstData", n2);
        }
        long l2 = response.Mt.return_code.get();
        if (!DEBUG) return l2 == 0L;
        {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "prepareMstData: response.mRetVal.error_msg = " + response.Mt.error_msg);
        }
        return l2 == 0L;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] a(byte[] arrby, VISA_CRYPTO_ALG vISA_CRYPTO_ALG, byte[] arrby2) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling generate  ");
        }
        if (arrby == null || arrby2 == null) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "generate: Error: input is null! encryptedLUK = " + arrby + "; transactionData = " + arrby2);
            throw new VisaTAException("Invalid Input", 5);
        }
        if (arrby.length > 4096 || arrby2.length > 4096) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "generate: Error: input length invalid! encryptedLUK.length = " + arrby.length + "; transactionData.length = " + arrby2.length);
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "generate: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.Generate.Request(arrby, vISA_CRYPTO_ALG, arrby2));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "generate: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "generate: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.Generate.Response response = new VisaCommands.Generate.Response(tACommandResponse);
        int n2 = (int)response.Mc.return_code.get();
        if (n2 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing Generate");
            throw new VisaTAException("Error processing Generate", n2);
        }
        if (!DEBUG) return response.Mc.Md.getData();
        com.samsung.android.spayfw.b.c.d("VisaTAController", "generate called Successfully");
        return response.Mc.Md.getData();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean authenticateTransaction(byte[] arrby) {
        TACommandResponse tACommandResponse;
        boolean bl = true;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling authenticateTransaction");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "authenticateTransaction: Error: secureObject length invalid! secureObject.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "authenticateTransaction: Error: secureObject is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", (int)bl);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.AuthenticateTransaction.Request(arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "Error: authenticateTransaction executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "authenticateTransaction: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        long l2 = new VisaCommands.AuthenticateTransaction.Response((TACommandResponse)tACommandResponse).LS.auth_result.get();
        if (l2 != 0L) {
            bl = false;
        }
        if (!DEBUG) return bl;
        com.samsung.android.spayfw.b.c.d("VisaTAController", "authenticateTransaction: auth_result = " + l2);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] b(byte[] arrby, boolean bl) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling prepareDataForVTS");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareDataForVTS: Error: data length invalid! data.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareDataForVTS: Error: data is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareDataForVTS: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        this.ir();
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.PrepareDataForVTS.Request(arrby, bl));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareDataForVTS: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "prepareDataForVTS: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.PrepareDataForVTS.Response response = new VisaCommands.PrepareDataForVTS.Response(tACommandResponse);
        int n2 = (int)response.Mk.return_code.get();
        if (n2 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing PrepareDataForVTS");
            throw new VisaTAException("Error processing PrepareDataForVTS", n2);
        }
        if (!DEBUG) return response.Mk.resp.getData();
        com.samsung.android.spayfw.b.c.d("VisaTAController", "prepareDataForVTS called Successfully");
        return response.Mk.resp.getData();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] b(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling getInAppJwePayload");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: data length invalid! data.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: data is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (arrby2 == null || arrby2.length > 4096) {
            if (arrby2 != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: encToken length invalid! encToken.len = " + arrby2.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: encToken is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (arrby3 == null || arrby3.length > 4096) {
            if (arrby3 != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: merchantCert length invalid! merchantCert.len = " + arrby3.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: merchantCert is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (arrby4 == null || arrby4.length > 4096) {
            if (arrby4 != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: caCert length invalid! caCert.len = " + arrby2.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: caCert is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        this.ir();
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.GenInAppJwePaymentInfo.Request(arrby, arrby2, arrby3, arrby4));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppJwePayload: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.GenInAppPaymentInfo.Response response = new VisaCommands.GenInAppPaymentInfo.Response(tACommandResponse);
        int n2 = (int)response.LY.return_code.get();
        if (n2 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing getInAppJwePayload");
            throw new VisaTAException("Error processing getInAppJwePayload", n2);
        }
        if (!DEBUG) return response.LY.resp.getData();
        com.samsung.android.spayfw.b.c.d("VisaTAController", "getInAppJwePayload called Successfully");
        return response.LY.resp.getData();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean clearMstData() {
        TACommandResponse tACommandResponse;
        com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling clearMstData");
        this.resetMstFlag();
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "clearMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.ClearMstData.Request(0));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "Error: clearMstData executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "clearMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            {
                throw new VisaTAException("Invalid Input", 5);
            }
        }
        VisaCommands.ClearMstData.Response response = new VisaCommands.ClearMstData.Response(tACommandResponse);
        long l2 = response.LT.return_code.get();
        if (!DEBUG) return l2 == 0L;
        {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "clearMstData: success ");
        }
        return l2 == 0L;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] e(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling getInAppPayload");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: data length invalid! data.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: data is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (arrby2 == null || arrby2.length > 4096) {
            if (arrby2 != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: encToken length invalid! merchantCert.len = " + arrby2.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: encToken is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (arrby3 == null) {
            com.samsung.android.spayfw.b.c.w("VisaTAController", "getInAppPayload: Error: nonce is null!");
            throw new VisaTAException("Invalid NONCE Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        this.ir();
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.GenInAppPaymentInfo.Request(arrby, arrby2, arrby3));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getInAppPayload: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.GenInAppPaymentInfo.Response response = new VisaCommands.GenInAppPaymentInfo.Response(tACommandResponse);
        int n2 = (int)response.LY.return_code.get();
        if (n2 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing getInAppPayload");
            throw new VisaTAException("Error processing getInAppPayload", n2);
        }
        if (!DEBUG) return response.LY.resp.getData();
        com.samsung.android.spayfw.b.c.d("VisaTAController", "getInAppPayload called Successfully");
        return response.LY.resp.getData();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getNonce(int n2) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling getNonce");
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "getNonce: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        VisaCommands.GetNonce.Request request = new VisaCommands.GetNonce.Request(n2);
        try {
            tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "Error:getNonce executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "getNonce: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            {
                throw new VisaTAException("Invalid Input", 5);
            }
        }
        VisaCommands.GetNonce.Response response = new VisaCommands.GetNonce.Response(tACommandResponse);
        int n3 = (int)response.Me.return_code.get();
        if (n3 != 0) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing GetNonce");
            throw new VisaTAException("Error processing GetNonce", n3);
        }
        byte[] arrby = response.Me.out_data.getData();
        if (arrby == null || !DEBUG) return arrby;
        {
            this.dumpHex("VisaTAController", "getNonce: ", arrby);
        }
        return arrby;
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "Error: init failed");
            return false;
        }
        this.MK = new b(this);
        return true;
    }

    public a is() {
        if (this.MH == null) {
            this.ir();
        }
        return this.MH;
    }

    public void p(byte[] arrby, byte[] arrby2) {
        this.MI = arrby;
        this.MJ = arrby2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] q(byte[] arrby) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling storeVTSData");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "storeVTSData: Error: vtsData length invalid! vtsData.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "storeVTSData: Error: vtsData is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "storeVTSData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        this.ir();
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.StoreVTSData.Request(arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "StoreVTSData: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "StoreVTSData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.StoreVTSData.Response response = new VisaCommands.StoreVTSData.Response(tACommandResponse);
        int n2 = (int)response.MB.return_code.get();
        if (n2 == 0) return response.MB.MC.getData();
        com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing StoreVTSData");
        throw new VisaTAException("Error processing StoreVTSData", n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] retrieveFromStorage(byte[] arrby) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling retrieveFromStorage");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "retrieveFromStorage: Error: data length invalid! data.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "retrieveFromStorage: Error: data is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "retrieveFromStorage: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.RetrieveFromStorage.Request(arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "RetrieveFromStorage: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "RetrieveFromStorage: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.RetrieveFromStorage.Response response = new VisaCommands.RetrieveFromStorage.Response(tACommandResponse);
        int n2 = (int)response.Mv.return_code.get();
        if (n2 == 0) return response.Mv.Mw.getData();
        com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing RetrieveFromStorage");
        throw new VisaTAException("Error processing RetrieveFromStorage", n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] storeData(byte[] arrby) {
        TACommandResponse tACommandResponse;
        if (DEBUG) {
            com.samsung.android.spayfw.b.c.d("VisaTAController", "Calling storeData");
        }
        if (arrby == null || arrby.length > 4096) {
            if (arrby != null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "storeData: Error: dataToStore length invalid! dataToStore.len = " + arrby.length);
                throw new VisaTAException("Invalid Input", 5);
            }
            com.samsung.android.spayfw.b.c.e("VisaTAController", "storeData: Error: dataToStore is null!");
            throw new VisaTAException("Invalid Input", 5);
        }
        if (!this.isTALoaded()) {
            com.samsung.android.spayfw.b.c.e("VisaTAController", "storeData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
        try {
            tACommandResponse = this.executeNoLoad(new VisaCommands.StoreData.Request(arrby));
            if (tACommandResponse == null) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "storeData: Error: executeNoLoad failed");
                throw new VisaTAException("Error communicating with the TA", 3);
            }
            if (tACommandResponse.mResponseCode != 0) {
                com.samsung.android.spayfw.b.c.e("VisaTAController", "storeData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new VisaTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new VisaTAException("Error communicating with the TA", 3);
            throw new VisaTAException("Invalid Input", 5);
        }
        VisaCommands.StoreData.Response response = new VisaCommands.StoreData.Response(tACommandResponse);
        int n2 = (int)response.My.return_code.get();
        if (n2 == 0) return response.My.Mz.getData();
        com.samsung.android.spayfw.b.c.e("VisaTAController", "Error processing StoreData");
        throw new VisaTAException("Error processing StoreData", n2);
    }

    public static final class VISA_CRYPTO_ALG
    extends Enum<VISA_CRYPTO_ALG> {
        public static final /* enum */ VISA_CRYPTO_ALG MP = new VISA_CRYPTO_ALG();
        public static final /* enum */ VISA_CRYPTO_ALG MQ = new VISA_CRYPTO_ALG();
        public static final /* enum */ VISA_CRYPTO_ALG MR = new VISA_CRYPTO_ALG();
        public static final /* enum */ VISA_CRYPTO_ALG MS = new VISA_CRYPTO_ALG();
        public static final /* enum */ VISA_CRYPTO_ALG MT = new VISA_CRYPTO_ALG();
        private static final /* synthetic */ VISA_CRYPTO_ALG[] MU;

        static {
            VISA_CRYPTO_ALG[] arrvISA_CRYPTO_ALG = new VISA_CRYPTO_ALG[]{MP, MQ, MR, MS, MT};
            MU = arrvISA_CRYPTO_ALG;
        }

        public static VISA_CRYPTO_ALG valueOf(String string) {
            return (VISA_CRYPTO_ALG)Enum.valueOf(VISA_CRYPTO_ALG.class, (String)string);
        }

        public static VISA_CRYPTO_ALG[] values() {
            return (VISA_CRYPTO_ALG[])MU.clone();
        }
    }

    public class a {
        public long ML;
        public long MM;
        public byte[] MN;
        public byte[] drk;
        public byte[] encryptcert;
        public byte[] signcert;

        public a(byte[] arrby, byte[] arrby2, byte[] arrby3, long l2, long l3, byte[] arrby4) {
            this.drk = arrby;
            this.signcert = arrby2;
            this.encryptcert = arrby3;
            this.ML = l2;
            this.MM = l3;
            this.MN = arrby4;
        }
    }

}

