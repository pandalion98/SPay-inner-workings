/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.io.PrintStream
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.List
 */
package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spayfw.cncc.CNCCDeviceCert;
import com.samsung.android.spayfw.cncc.CNCCTAException;
import com.samsung.android.spayfw.cncc.CNCCTAInfo;
import com.samsung.android.spayfw.cncc.ManagedTAHandle;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.cncc.Utils;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.io.PrintStream;
import java.util.List;
import javolution.io.Struct;

public class CNCCTAController
extends TAController {
    private static final String SPAYDCM_CERTSIGN_NAME = "cncc_pay_sign.dat";
    private static final String SPAYDCM_SERVICE_NAME = "CNCC_PAY";
    private static final String TAG = "SPAY:CNCCTAController";
    private static CNCCTAInfo TA_INFO = new CNCCTAInfo();
    private static CNCCTAController mInstance;
    private DevicePublicCerts mDevicePublicCerts = null;
    private ManagedTAHandle mManagedTAHandle = new ManagedTAHandle(this);
    private CNCCDeviceCert mSpayDCMSSLDeviceCert = null;

    private CNCCTAController(Context context) {
        super(context, TA_INFO);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean bringup(Context context) {
        boolean bl = true;
        Class<CNCCTAController> class_ = CNCCTAController.class;
        synchronized (CNCCTAController.class) {
            c.d(TAG, "CNCC Bringup - Start");
            if (!CNCCTAController.isSupported(context)) {
                c.d(TAG, "CNCC can not be supported in this binary");
            } else {
                try {
                    if (!CNCCTAController.createOnlyInstance(context).initializeSecuritySetup()) {
                        c.e(TAG, "initializeSecuritySetup failed");
                        return false;
                    }
                    c.d(TAG, "CNCC Bringup - End");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return false;
                }
            }
            // ** MonitorExit[var4_2] (shouldn't be in output)
            return bl;
        }
    }

    public static CNCCTAController createOnlyInstance(Context context) {
        Class<CNCCTAController> class_ = CNCCTAController.class;
        synchronized (CNCCTAController.class) {
            if (mInstance == null) {
                mInstance = new CNCCTAController(context);
            }
            CNCCTAController cNCCTAController = mInstance;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return cNCCTAController;
        }
    }

    public static CNCCTAController getInstance() {
        Class<CNCCTAController> class_ = CNCCTAController.class;
        synchronized (CNCCTAController.class) {
            CNCCTAController cNCCTAController = mInstance;
            // ** MonitorExit[var2] (shouldn't be in output)
            return cNCCTAController;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean initializeSecuritySetup() {
        block9 : {
            block8 : {
                this.mManagedTAHandle.loadTA();
                if (!this.isSecuritySetupInitialized()) {
                    if (DEBUG) {
                        c.d(TAG, "Calling initializeSecuritySetup");
                    }
                    if (!this.isTALoaded()) {
                        c.e(TAG, "initializeSecuritySetup: Error: TA is not loaded, please call loadTA() API first!");
                        return false;
                    }
                    this.initTA();
                    if (!this.mSpayDCMSSLDeviceCert.load()) {
                        c.e(TAG, "Error: CNCC Device Certs Load failed");
                        return false;
                    }
                    byte[] arrby = this.mSpayDCMSSLDeviceCert.getSPayDCMDevicePrivateSignCert();
                    byte[] arrby2 = this.mSpayDCMSSLDeviceCert.getSPayDCMDevicePrivateEncryptCert();
                    try {
                        TACommandResponse tACommandResponse = this.executeNoLoad(new CNCCCommands.LoadCerts.Request(arrby, arrby2));
                        if (tACommandResponse == null || tACommandResponse.mResponseCode != 0) {
                            c.e(TAG, "loadAllCerts: Error: executeNoLoad failed");
                            throw new CNCCTAException("TZ Communication Error", 983040);
                        }
                        CNCCCommands.LoadCerts.Response response = new CNCCCommands.LoadCerts.Response(tACommandResponse);
                        this.mDevicePublicCerts = new DevicePublicCerts();
                        this.mDevicePublicCerts.deviceCertificate = Utils.convertToPem(response.mRetVal.deviceRootRSA2048PubCert.getData(), false);
                        this.mDevicePublicCerts.deviceSigningCertificate = Utils.convertToPem(response.mRetVal.deviceSignRSA2048PubCert.getData(), false);
                        if (!DEBUG) break block8;
                        break block9;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        throw new CNCCTAException("TZ Communication Error", 983040);
                    }
                }
            }
            return true;
        }
        c.d(TAG, "initializeSecuritySetup called Successfully");
        System.out.println("DRK Cert= \n" + this.mDevicePublicCerts.deviceCertificate);
        System.out.println("Signing Cert = \n" + this.mDevicePublicCerts.deviceSigningCertificate);
        return true;
    }

    private boolean isSecuritySetupInitialized() {
        if (this.mDevicePublicCerts != null) {
            if (DEBUG) {
                c.d(TAG, "Device Certs already loaded)");
            }
            return true;
        }
        return false;
    }

    public static boolean isSupported(Context context) {
        return SpayDRKManager.isSupported(context);
    }

    public void doManagedLoad() {
        CNCCTAController cNCCTAController = this;
        synchronized (cNCCTAController) {
            this.mManagedTAHandle.loadTA();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DevicePublicCerts getDeviceCertificates() {
        CNCCTAController cNCCTAController = this;
        synchronized (cNCCTAController) {
            if (this.initializeSecuritySetup()) return this.mDevicePublicCerts;
            c.e(TAG, "initializeSecuritySetup failed");
            throw new CNCCTAException("TZ Communication Error", 983040);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getNonce(int n2) {
        TACommandResponse tACommandResponse;
        byte[] arrby;
        if (DEBUG) {
            c.d(TAG, "Calling getNonce");
        }
        if (!this.initializeSecuritySetup()) {
            c.e(TAG, "initializeSecuritySetup failed");
            throw new CNCCTAException("TZ Communication Error", 983040);
        }
        CNCCCommands.GetNonce.Request request = new CNCCCommands.GetNonce.Request(n2);
        try {
            tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                c.e(TAG, "getNonce: Error: executeNoLoad failed");
                throw new CNCCTAException("TZ Communication Error", 983040);
            }
            if (tACommandResponse.mResponseCode != 0) {
                c.e(TAG, "getNonce: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                throw new CNCCTAException("TA command returned error", 4);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            if (!(exception instanceof IllegalArgumentException)) throw new CNCCTAException("Error communicating with the TA", 983040);
            {
                throw new CNCCTAException("Invalid Input", 1);
            }
        }
        CNCCCommands.GetNonce.Response response = new CNCCCommands.GetNonce.Response(tACommandResponse);
        int n3 = (int)response.mRetVal.return_code.get();
        if (n3 != 0) {
            c.e(TAG, "Error processing GetNonce");
            throw new CNCCTAException("Error processing GetNonce", n3);
        }
        int n4 = (int)response.mRetVal.return_code.get();
        String string = new String(Utils.getByteArray(response.mRetVal.error_msg));
        if (n4 != 0) {
            c.e(TAG, "ProcessData Call Failed");
            throw new CNCCTAException(string, n4);
        }
        if (DEBUG) {
            c.d(TAG, "processData called Successfully");
        }
        if ((arrby = response.mRetVal.out_data.getData()) == null || !DEBUG) return arrby;
        {
            this.dumpHex(TAG, "getNonce:", arrby);
        }
        return arrby;
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            c.e(TAG, "Error: init failed");
            return false;
        }
        this.mSpayDCMSSLDeviceCert = new CNCCDeviceCert(this);
        return true;
    }

    @Override
    public boolean loadTA() {
        CNCCTAController cNCCTAController = this;
        synchronized (cNCCTAController) {
            boolean bl = super.loadTA();
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] processData(List<byte[]> list, byte[] arrby, DataType dataType, ProcessingOption processingOption, String string, String string2) {
        CNCCTAController cNCCTAController = this;
        synchronized (cNCCTAController) {
            CNCCCommands.ProcessData.Response response;
            if (DEBUG) {
                c.d(TAG, "Calling processData");
            }
            if (!this.initializeSecuritySetup()) {
                c.e(TAG, "initializeSecuritySetup failed");
                throw new CNCCTAException("TZ Communication Error", 983040);
            }
            try {
                TACommandResponse tACommandResponse;
                int n2 = dataType.ordinal();
                int n3 = processingOption.ordinal();
                byte[] arrby2 = string != null ? string.getBytes() : null;
                byte[] arrby3 = null;
                if (string2 != null) {
                    arrby3 = string2.getBytes();
                }
                if ((tACommandResponse = this.executeNoLoad(new CNCCCommands.ProcessData.Request(list, arrby, n2, n3, arrby2, arrby3))) == null || tACommandResponse.mResponseCode != 0) {
                    c.e(TAG, "processData: Error: executeNoLoad failed");
                    throw new CNCCTAException("TZ Communication Error", 983040);
                }
                response = new CNCCCommands.ProcessData.Response(tACommandResponse);
                int n4 = (int)response.mRetVal.return_code.get();
                String string3 = new String(Utils.getByteArray(response.mRetVal.error_msg));
                if (n4 != 0) {
                    c.e(TAG, "ProcessData Call Failed");
                    throw new CNCCTAException(string3, n4);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new CNCCTAException("TZ Communication Error", 983040);
            }
            if (!DEBUG) return response.mRetVal.data.getData();
            c.d(TAG, "processData called Successfully");
            return response.mRetVal.data.getData();
        }
    }

    @Override
    public void unloadTA() {
        CNCCTAController cNCCTAController = this;
        synchronized (cNCCTAController) {
            this.mDevicePublicCerts = null;
            super.unloadTA();
            return;
        }
    }

    public static final class DataType
    extends Enum<DataType> {
        private static final /* synthetic */ DataType[] $VALUES;
        public static final /* enum */ DataType DATATYPE_CERTIFICATE;
        public static final /* enum */ DataType DATATYPE_RAW_DATA;
        public static final /* enum */ DataType DATATYPE_UNDEFINED;

        static {
            DATATYPE_UNDEFINED = new DataType();
            DATATYPE_RAW_DATA = new DataType();
            DATATYPE_CERTIFICATE = new DataType();
            DataType[] arrdataType = new DataType[]{DATATYPE_UNDEFINED, DATATYPE_RAW_DATA, DATATYPE_CERTIFICATE};
            $VALUES = arrdataType;
        }

        public static DataType valueOf(String string) {
            return (DataType)Enum.valueOf(DataType.class, (String)string);
        }

        public static DataType[] values() {
            return (DataType[])$VALUES.clone();
        }
    }

    public static class DevicePublicCerts {
        public String deviceCertificate;
        public String deviceSigningCertificate;
    }

    public static final class ProcessingOption
    extends Enum<ProcessingOption> {
        private static final /* synthetic */ ProcessingOption[] $VALUES;
        public static final /* enum */ ProcessingOption OPTION_DECRYPT;
        public static final /* enum */ ProcessingOption OPTION_ENCRYPT;
        public static final /* enum */ ProcessingOption OPTION_JWEJWS_DECRYPT_VERIFY;
        public static final /* enum */ ProcessingOption OPTION_JWEJWS_ENCRYPT_SIGN;
        public static final /* enum */ ProcessingOption OPTION_RAW_SIGN;
        public static final /* enum */ ProcessingOption OPTION_SIGN;
        public static final /* enum */ ProcessingOption OPTION_UNDEFINED;
        public static final /* enum */ ProcessingOption OPTION_UNWRAP_FOR_SELF;
        public static final /* enum */ ProcessingOption OPTION_UNWRAP_FROM_SRCTA_AND_WRAP_FOR_DESTTA;
        public static final /* enum */ ProcessingOption OPTION_UNWRAP_FROM_TA_AND_ENCRYPT;
        public static final /* enum */ ProcessingOption OPTION_UNWRAP_FROM_TA_AND_JWEJWS_ENCRYPT_SIGN;
        public static final /* enum */ ProcessingOption OPTION_UNWRAP_FROM_TA_AND_JWE_ENCRYPT;
        public static final /* enum */ ProcessingOption OPTION_VERIFY;
        public static final /* enum */ ProcessingOption OPTION_WRAP_FOR_SELF;

        static {
            OPTION_UNDEFINED = new ProcessingOption();
            OPTION_WRAP_FOR_SELF = new ProcessingOption();
            OPTION_UNWRAP_FOR_SELF = new ProcessingOption();
            OPTION_UNWRAP_FROM_SRCTA_AND_WRAP_FOR_DESTTA = new ProcessingOption();
            OPTION_RAW_SIGN = new ProcessingOption();
            OPTION_UNWRAP_FROM_TA_AND_JWEJWS_ENCRYPT_SIGN = new ProcessingOption();
            OPTION_UNWRAP_FROM_TA_AND_JWE_ENCRYPT = new ProcessingOption();
            OPTION_SIGN = new ProcessingOption();
            OPTION_JWEJWS_DECRYPT_VERIFY = new ProcessingOption();
            OPTION_JWEJWS_ENCRYPT_SIGN = new ProcessingOption();
            OPTION_DECRYPT = new ProcessingOption();
            OPTION_VERIFY = new ProcessingOption();
            OPTION_ENCRYPT = new ProcessingOption();
            OPTION_UNWRAP_FROM_TA_AND_ENCRYPT = new ProcessingOption();
            ProcessingOption[] arrprocessingOption = new ProcessingOption[]{OPTION_UNDEFINED, OPTION_WRAP_FOR_SELF, OPTION_UNWRAP_FOR_SELF, OPTION_UNWRAP_FROM_SRCTA_AND_WRAP_FOR_DESTTA, OPTION_RAW_SIGN, OPTION_UNWRAP_FROM_TA_AND_JWEJWS_ENCRYPT_SIGN, OPTION_UNWRAP_FROM_TA_AND_JWE_ENCRYPT, OPTION_SIGN, OPTION_JWEJWS_DECRYPT_VERIFY, OPTION_JWEJWS_ENCRYPT_SIGN, OPTION_DECRYPT, OPTION_VERIFY, OPTION_ENCRYPT, OPTION_UNWRAP_FROM_TA_AND_ENCRYPT};
            $VALUES = arrprocessingOption;
        }

        public static ProcessingOption valueOf(String string) {
            return (ProcessingOption)Enum.valueOf(ProcessingOption.class, (String)string);
        }

        public static ProcessingOption[] values() {
            return (ProcessingOption[])$VALUES.clone();
        }
    }

}

