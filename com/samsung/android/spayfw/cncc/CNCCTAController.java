package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.cncc.CNCCCommands.GetNonce;
import com.samsung.android.spayfw.cncc.CNCCCommands.LoadCerts.Request;
import com.samsung.android.spayfw.cncc.CNCCCommands.LoadCerts.Response;
import com.samsung.android.spayfw.cncc.CNCCCommands.ProcessData;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.List;

public class CNCCTAController extends TAController {
    private static final String SPAYDCM_CERTSIGN_NAME = "cncc_pay_sign.dat";
    private static final String SPAYDCM_SERVICE_NAME = "CNCC_PAY";
    private static final String TAG = "SPAY:CNCCTAController";
    private static CNCCTAInfo TA_INFO;
    private static CNCCTAController mInstance;
    private DevicePublicCerts mDevicePublicCerts;
    private ManagedTAHandle mManagedTAHandle;
    private CNCCDeviceCert mSpayDCMSSLDeviceCert;

    public enum DataType {
        DATATYPE_UNDEFINED,
        DATATYPE_RAW_DATA,
        DATATYPE_CERTIFICATE
    }

    public static class DevicePublicCerts {
        public String deviceCertificate;
        public String deviceSigningCertificate;
    }

    public enum ProcessingOption {
        OPTION_UNDEFINED,
        OPTION_WRAP_FOR_SELF,
        OPTION_UNWRAP_FOR_SELF,
        OPTION_UNWRAP_FROM_SRCTA_AND_WRAP_FOR_DESTTA,
        OPTION_RAW_SIGN,
        OPTION_UNWRAP_FROM_TA_AND_JWEJWS_ENCRYPT_SIGN,
        OPTION_UNWRAP_FROM_TA_AND_JWE_ENCRYPT,
        OPTION_SIGN,
        OPTION_JWEJWS_DECRYPT_VERIFY,
        OPTION_JWEJWS_ENCRYPT_SIGN,
        OPTION_DECRYPT,
        OPTION_VERIFY,
        OPTION_ENCRYPT,
        OPTION_UNWRAP_FROM_TA_AND_ENCRYPT
    }

    static {
        TA_INFO = new CNCCTAInfo();
    }

    private CNCCTAController(Context context) {
        super(context, TA_INFO);
        this.mSpayDCMSSLDeviceCert = null;
        this.mDevicePublicCerts = null;
        this.mManagedTAHandle = new ManagedTAHandle(this);
    }

    protected boolean init() {
        if (super.init()) {
            this.mSpayDCMSSLDeviceCert = new CNCCDeviceCert(this);
            return true;
        }
        Log.m286e(TAG, "Error: init failed");
        return false;
    }

    public static synchronized CNCCTAController createOnlyInstance(Context context) {
        CNCCTAController cNCCTAController;
        synchronized (CNCCTAController.class) {
            if (mInstance == null) {
                mInstance = new CNCCTAController(context);
            }
            cNCCTAController = mInstance;
        }
        return cNCCTAController;
    }

    public static synchronized CNCCTAController getInstance() {
        CNCCTAController cNCCTAController;
        synchronized (CNCCTAController.class) {
            cNCCTAController = mInstance;
        }
        return cNCCTAController;
    }

    public static synchronized boolean bringup(Context context) {
        boolean z = true;
        synchronized (CNCCTAController.class) {
            Log.m285d(TAG, "CNCC Bringup - Start");
            if (isSupported(context)) {
                try {
                    if (createOnlyInstance(context).initializeSecuritySetup()) {
                        Log.m285d(TAG, "CNCC Bringup - End");
                    } else {
                        Log.m286e(TAG, "initializeSecuritySetup failed");
                        z = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    z = false;
                }
            } else {
                Log.m285d(TAG, "CNCC can not be supported in this binary");
            }
        }
        return z;
    }

    public synchronized void doManagedLoad() {
        this.mManagedTAHandle.loadTA();
    }

    public synchronized boolean loadTA() {
        return super.loadTA();
    }

    public synchronized void unloadTA() {
        this.mDevicePublicCerts = null;
        super.unloadTA();
    }

    private boolean isSecuritySetupInitialized() {
        if (this.mDevicePublicCerts == null) {
            return false;
        }
        if (DEBUG) {
            Log.m285d(TAG, "Device Certs already loaded)");
        }
        return true;
    }

    private boolean initializeSecuritySetup() {
        this.mManagedTAHandle.loadTA();
        if (isSecuritySetupInitialized()) {
            return true;
        }
        if (DEBUG) {
            Log.m285d(TAG, "Calling initializeSecuritySetup");
        }
        if (isTALoaded()) {
            initTA();
            if (!this.mSpayDCMSSLDeviceCert.load()) {
                Log.m286e(TAG, "Error: CNCC Device Certs Load failed");
                return false;
            }
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Request(this.mSpayDCMSSLDeviceCert.getSPayDCMDevicePrivateSignCert(), this.mSpayDCMSSLDeviceCert.getSPayDCMDevicePrivateEncryptCert()));
                if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                    Log.m286e(TAG, "loadAllCerts: Error: executeNoLoad failed");
                    throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
                }
                Response response = new Response(executeNoLoad);
                this.mDevicePublicCerts = new DevicePublicCerts();
                this.mDevicePublicCerts.deviceCertificate = Utils.convertToPem(response.mRetVal.deviceRootRSA2048PubCert.getData(), false);
                this.mDevicePublicCerts.deviceSigningCertificate = Utils.convertToPem(response.mRetVal.deviceSignRSA2048PubCert.getData(), false);
                if (!DEBUG) {
                    return true;
                }
                Log.m285d(TAG, "initializeSecuritySetup called Successfully");
                System.out.println("DRK Cert= \n" + this.mDevicePublicCerts.deviceCertificate);
                System.out.println("Signing Cert = \n" + this.mDevicePublicCerts.deviceSigningCertificate);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e(TAG, "initializeSecuritySetup: Error: TA is not loaded, please call loadTA() API first!");
        return false;
    }

    public synchronized DevicePublicCerts getDeviceCertificates() {
        if (initializeSecuritySetup()) {
        } else {
            Log.m286e(TAG, "initializeSecuritySetup failed");
            throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
        }
        return this.mDevicePublicCerts;
    }

    public synchronized byte[] processData(List<byte[]> list, byte[] bArr, DataType dataType, ProcessingOption processingOption, String str, String str2) {
        byte[] data;
        byte[] bArr2 = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling processData");
            }
            if (initializeSecuritySetup()) {
                try {
                    int ordinal = dataType.ordinal();
                    int ordinal2 = processingOption.ordinal();
                    byte[] bytes = str != null ? str.getBytes() : null;
                    if (str2 != null) {
                        bArr2 = str2.getBytes();
                    }
                    TACommandResponse executeNoLoad = executeNoLoad(new ProcessData.Request(list, bArr, ordinal, ordinal2, bytes, bArr2));
                    if (executeNoLoad == null || executeNoLoad.mResponseCode != 0) {
                        Log.m286e(TAG, "processData: Error: executeNoLoad failed");
                        throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
                    }
                    ProcessData.Response response = new ProcessData.Response(executeNoLoad);
                    int i = (int) response.mRetVal.return_code.get();
                    String str3 = new String(Utils.getByteArray(response.mRetVal.error_msg));
                    if (i != 0) {
                        Log.m286e(TAG, "ProcessData Call Failed");
                        throw new CNCCTAException(str3, i);
                    }
                    if (DEBUG) {
                        Log.m285d(TAG, "processData called Successfully");
                    }
                    data = response.mRetVal.data.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
                }
            }
            Log.m286e(TAG, "initializeSecuritySetup failed");
            throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
        }
        return data;
    }

    public static boolean isSupported(Context context) {
        return SpayDRKManager.isSupported(context);
    }

    public byte[] getNonce(int i) {
        if (DEBUG) {
            Log.m285d(TAG, "Calling getNonce");
        }
        if (initializeSecuritySetup()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new GetNonce.Request(i));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "getNonce: Error: executeNoLoad failed");
                    throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e(TAG, "getNonce: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new CNCCTAException("TA command returned error", 4);
                } else {
                    GetNonce.Response response = new GetNonce.Response(executeNoLoad);
                    int i2 = (int) response.mRetVal.return_code.get();
                    if (i2 != 0) {
                        Log.m286e(TAG, "Error processing GetNonce");
                        throw new CNCCTAException("Error processing GetNonce", i2);
                    }
                    i2 = (int) response.mRetVal.return_code.get();
                    String str = new String(Utils.getByteArray(response.mRetVal.error_msg));
                    if (i2 != 0) {
                        Log.m286e(TAG, "ProcessData Call Failed");
                        throw new CNCCTAException(str, i2);
                    }
                    if (DEBUG) {
                        Log.m285d(TAG, "processData called Successfully");
                    }
                    byte[] data = response.mRetVal.out_data.getData();
                    if (data != null && DEBUG) {
                        dumpHex(TAG, "getNonce:", data);
                    }
                    return data;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new CNCCTAException("Invalid Input", 1);
                }
                throw new CNCCTAException("Error communicating with the TA", CNCCTAException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e(TAG, "initializeSecuritySetup failed");
        throw new CNCCTAException("TZ Communication Error", CNCCTAException.ERR_TZ_COM_ERR);
    }
}
