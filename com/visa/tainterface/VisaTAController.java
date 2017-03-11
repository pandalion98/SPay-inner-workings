package com.visa.tainterface;

import android.content.Context;
import android.os.Build;
import android.spay.CertInfo;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.visa.tainterface.VisaCommands.AuthenticateTransaction;
import com.visa.tainterface.VisaCommands.ClearMstData;
import com.visa.tainterface.VisaCommands.GenInAppJwePaymentInfo;
import com.visa.tainterface.VisaCommands.GenInAppPaymentInfo;
import com.visa.tainterface.VisaCommands.Generate;
import com.visa.tainterface.VisaCommands.GetNonce;
import com.visa.tainterface.VisaCommands.LoadCerts.Request;
import com.visa.tainterface.VisaCommands.LoadCerts.Response;
import com.visa.tainterface.VisaCommands.PrepareDataForVTS;
import com.visa.tainterface.VisaCommands.PrepareMstData;
import com.visa.tainterface.VisaCommands.RetrieveFromStorage;
import com.visa.tainterface.VisaCommands.StoreData;
import com.visa.tainterface.VisaCommands.StoreVTSData;
import com.visa.tainterface.VisaCommands.TransmitMstData;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;

public class VisaTAController extends TAController {
    private static VisaTAController ME;
    public static final String MF;
    public static TAInfo TA_INFO;
    private static final boolean bQC;
    private C0694a MH;
    private byte[] MI;
    private byte[] MJ;
    private VisaDeviceCerts MK;
    private CertInfo certsInfoCache;

    public enum VISA_CRYPTO_ALG {
        VISA_ALG_NFC_QVSDC_CRYPTOGRAM,
        VISA_ALG_NFC_MSD_CRYPTOGRAM,
        VISA_ALG_MST_ARQC_CRYPTOGRAM,
        VISA_ALG_NFC_SDAD,
        VISA_ALG_NFC_QVSDC_MSD_CRYPTOGRAM
    }

    /* renamed from: com.visa.tainterface.VisaTAController.a */
    public class C0694a {
        public long ML;
        public long MM;
        public byte[] MN;
        final /* synthetic */ VisaTAController MO;
        public byte[] drk;
        public byte[] encryptcert;
        public byte[] signcert;

        public C0694a(VisaTAController visaTAController, byte[] bArr, byte[] bArr2, byte[] bArr3, long j, long j2, byte[] bArr4) {
            this.MO = visaTAController;
            this.drk = bArr;
            this.signcert = bArr2;
            this.encryptcert = bArr3;
            this.ML = j;
            this.MM = j2;
            this.MN = bArr4;
        }
    }

    static {
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        MF = bQC ? "visa_pay" : "ffffffff00000000000000000000001c";
        TA_INFO = new VisaTAInfo();
    }

    private VisaTAController(Context context) {
        super(context, TA_INFO);
        this.MH = null;
        this.certsInfoCache = null;
        this.MI = null;
        this.MJ = null;
        this.MK = null;
    }

    protected boolean init() {
        if (super.init()) {
            this.MK = new VisaDeviceCerts(this);
            return true;
        }
        Log.m286e("VisaTAController", "Error: init failed");
        return false;
    }

    public static synchronized VisaTAController bv(Context context) {
        VisaTAController visaTAController;
        synchronized (VisaTAController.class) {
            if (ME == null) {
                ME = new VisaTAController(context);
            }
            visaTAController = ME;
        }
        return visaTAController;
    }

    public static synchronized VisaTAController iq() {
        VisaTAController visaTAController;
        synchronized (VisaTAController.class) {
            visaTAController = ME;
        }
        return visaTAController;
    }

    public void m1716p(byte[] bArr, byte[] bArr2) {
        this.MI = bArr;
        this.MJ = bArr2;
    }

    private void ir() {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling loadAllCerts");
        }
        if (isTALoaded()) {
            if (!this.MK.isLoaded()) {
                Log.m285d("VisaTAController", "mVisaDeviceCerts is not loaded");
                if (!this.MK.load()) {
                    Log.m286e("VisaTAController", "Error: Visa Device Certs Load failed");
                    throw new VisaTAException("Error loading Visa Certs", 2);
                }
            }
            byte[] devicePrivateSignCert = this.MK.getDevicePrivateSignCert();
            byte[] devicePrivateEncryptionCert = this.MK.getDevicePrivateEncryptionCert();
            if (devicePrivateSignCert == null || devicePrivateEncryptionCert == null) {
                Log.m286e("VisaTAController", "loadAllCerts: Error: Certificate Data is NULL");
                this.certsInfoCache = null;
                throw new VisaTAException("Error loading Visa Certs", 2);
            } else if (devicePrivateSignCert.length > PKIFailureInfo.certConfirmed || devicePrivateEncryptionCert.length > PKIFailureInfo.certConfirmed || ((this.MI != null && this.MI.length > PKIFailureInfo.certConfirmed) || (this.MJ != null && this.MJ.length > PKIFailureInfo.certConfirmed))) {
                Log.m286e("VisaTAController", "loadAllCerts: Error: certs length invalid! - signCertData.length = " + devicePrivateSignCert.length + "; encryptCertData.length = " + devicePrivateEncryptionCert.length);
                this.certsInfoCache = null;
                throw new VisaTAException("Error loading Visa Certs", 2);
            } else {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new Request(devicePrivateSignCert, devicePrivateEncryptionCert, this.MI, this.MJ));
                    if (executeNoLoad == null) {
                        Log.m286e("VisaTAController", "loadAllCerts: Error: executeNoLoad failed");
                        throw new VisaTAException("Error communicating with the TA", 3);
                    } else if (executeNoLoad.mResponseCode != 0) {
                        Log.m286e("VisaTAController", "loadAllCerts: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                        throw new VisaTAException("TA command returned error", 4);
                    } else {
                        Response response = new Response(executeNoLoad);
                        int i = (int) response.Mf.return_code.get();
                        if (i != 0) {
                            Log.m286e("VisaTAController", "Error processing LoadCerts");
                            throw new VisaTAException("Error processing LoadCerts", i);
                        }
                        this.MH = new C0694a(this, response.Mf.cert_drk.getData(), response.Mf.cert_sign.getData(), response.Mf.cert_encrypt.getData(), response.Mf.Mg.get(), response.Mf.Mh.get(), response.Mf.Mi.getData());
                        if (DEBUG) {
                            Log.m285d("VisaTAController", "loadAllCerts called Successfully");
                            return;
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof IllegalArgumentException) {
                        throw new VisaTAException("Invalid Input", 5);
                    }
                    throw new VisaTAException("Error communicating with the TA", 3);
                }
            }
        }
        Log.m286e("VisaTAController", "loadAllCerts: Error: TA is not loaded, please call loadTA() API first!");
        throw new VisaTAException("Visa TA not loaded", 1);
    }

    public C0694a is() {
        if (this.MH == null) {
            ir();
        }
        return this.MH;
    }

    public byte[] m1713b(byte[] bArr, boolean z) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling prepareDataForVTS");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "prepareDataForVTS: Error: data length invalid! data.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "prepareDataForVTS: Error: data is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            ir();
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new PrepareDataForVTS.Request(bArr, z));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "prepareDataForVTS: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "prepareDataForVTS: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    PrepareDataForVTS.Response response = new PrepareDataForVTS.Response(executeNoLoad);
                    int i = (int) response.Mk.return_code.get();
                    if (i != 0) {
                        Log.m286e("VisaTAController", "Error processing PrepareDataForVTS");
                        throw new VisaTAException("Error processing PrepareDataForVTS", i);
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "prepareDataForVTS called Successfully");
                    }
                    return response.Mk.resp.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "prepareDataForVTS: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public byte[] m1717q(byte[] bArr) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling storeVTSData");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "storeVTSData: Error: vtsData length invalid! vtsData.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "storeVTSData: Error: vtsData is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            ir();
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new StoreVTSData.Request(bArr));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "StoreVTSData: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "StoreVTSData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    StoreVTSData.Response response = new StoreVTSData.Response(executeNoLoad);
                    int i = (int) response.MB.return_code.get();
                    if (i == 0) {
                        return response.MB.MC.getData();
                    }
                    Log.m286e("VisaTAController", "Error processing StoreVTSData");
                    throw new VisaTAException("Error processing StoreVTSData", i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "storeVTSData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public byte[] storeData(byte[] bArr) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling storeData");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "storeData: Error: dataToStore length invalid! dataToStore.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "storeData: Error: dataToStore is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new StoreData.Request(bArr));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "storeData: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "storeData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    StoreData.Response response = new StoreData.Response(executeNoLoad);
                    int i = (int) response.My.return_code.get();
                    if (i == 0) {
                        return response.My.Mz.getData();
                    }
                    Log.m286e("VisaTAController", "Error processing StoreData");
                    throw new VisaTAException("Error processing StoreData", i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "storeData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public byte[] retrieveFromStorage(byte[] bArr) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling retrieveFromStorage");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "retrieveFromStorage: Error: data length invalid! data.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "retrieveFromStorage: Error: data is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new RetrieveFromStorage.Request(bArr));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "RetrieveFromStorage: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "RetrieveFromStorage: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    RetrieveFromStorage.Response response = new RetrieveFromStorage.Response(executeNoLoad);
                    int i = (int) response.Mv.return_code.get();
                    if (i == 0) {
                        return response.Mv.Mw.getData();
                    }
                    Log.m286e("VisaTAController", "Error processing RetrieveFromStorage");
                    throw new VisaTAException("Error processing RetrieveFromStorage", i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "retrieveFromStorage: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    private boolean m1709b(byte[] bArr, TrackData trackData) {
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: _encrypted_luk length invalid! _encrypted_luk.len = " + bArr.length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: _encrypted_luk is null!");
            return false;
        } else if (trackData == null) {
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: trackData is null!");
            return false;
        } else if (trackData.ii() == null || trackData.ii().length > SkeinMac.SKEIN_256) {
            if (trackData.ii() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: tokenBytes length invalid! tokenBytes.len = " + trackData.ii().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: tokenBytes is null!");
            return false;
        } else if (trackData.ij() == null || trackData.ij().length > SkeinMac.SKEIN_256) {
            if (trackData.ij() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: TokenExpirationDate length invalid! TokenExpirationDate.len = " + trackData.ij().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: TokenExpirationDate is null!");
            return false;
        } else if (trackData.ik() == null || trackData.ik().length > SkeinMac.SKEIN_256) {
            if (trackData.ik() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: ServiceCode length invalid! ServiceCode.len = " + trackData.ik().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: ServiceCode is null!");
            return false;
        } else if (trackData.il() == null || trackData.il().length > SkeinMac.SKEIN_256) {
            if (trackData.il() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: Timestamp length invalid! Timestamp.len = " + trackData.il().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: Timestamp is null!");
            return false;
        } else if (trackData.im() == null || trackData.im().length > SkeinMac.SKEIN_256) {
            if (trackData.im() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: SequenceCounter length invalid! SequenceCounter.len = " + trackData.im().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: SequenceCounter is null!");
            return false;
        } else if (trackData.in() == null || trackData.in().length > SkeinMac.SKEIN_256) {
            if (trackData.in() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: AppTransactionCounter length invalid! AppTransactionCounter.len = " + trackData.in().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: AppTransactionCounter is null!");
            return false;
        } else if (trackData.io() == null || trackData.io().length > SkeinMac.SKEIN_256) {
            if (trackData.io() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: CVV length invalid! CVV.len = " + trackData.io().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: CVV is null!");
            return false;
        } else if (trackData.ip() != null && trackData.ip().length <= SkeinMac.SKEIN_256) {
            return true;
        } else {
            if (trackData.ip() != null) {
                Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: ReservedBytes length invalid! ReservedBytes.len = " + trackData.ip().length);
                return false;
            }
            Log.m286e("VisaTAController", "validateInputPrepareMstData: Error: ReservedBytes is null!");
            return false;
        }
    }

    public boolean m1711a(byte[] bArr, TrackData trackData) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling prepareMstData");
        }
        if (!m1709b(bArr, trackData)) {
            Log.m286e("VisaTAController", "prepareMstData: Error: one of the inputs is invalid!");
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new PrepareMstData.Request(trackData.ii(), trackData.ij(), trackData.ik(), trackData.il(), trackData.im(), trackData.in(), trackData.io(), trackData.ip(), bArr));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "prepareMstData: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "prepareMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    PrepareMstData.Response response = new PrepareMstData.Response(executeNoLoad);
                    int i = (int) response.Mt.return_code.get();
                    if (i != 0) {
                        Log.m286e("VisaTAController", "Error processing PrepareMstData");
                        throw new VisaTAException("Error processing PrepareMstData", i);
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "prepareMstData: response.mRetVal.error_msg = " + response.Mt.error_msg);
                    }
                    if (response.Mt.return_code.get() == 0) {
                        return true;
                    }
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "prepareMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public byte[] m1712a(byte[] bArr, VISA_CRYPTO_ALG visa_crypto_alg, byte[] bArr2) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling generate  ");
        }
        if (bArr == null || bArr2 == null) {
            Log.m286e("VisaTAController", "generate: Error: input is null! encryptedLUK = " + bArr + "; transactionData = " + bArr2);
            throw new VisaTAException("Invalid Input", 5);
        } else if (bArr.length > PKIFailureInfo.certConfirmed || bArr2.length > PKIFailureInfo.certConfirmed) {
            Log.m286e("VisaTAController", "generate: Error: input length invalid! encryptedLUK.length = " + bArr.length + "; transactionData.length = " + bArr2.length);
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Generate.Request(bArr, visa_crypto_alg, bArr2));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "generate: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "generate: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    Generate.Response response = new Generate.Response(executeNoLoad);
                    int i = (int) response.Mc.return_code.get();
                    if (i != 0) {
                        Log.m286e("VisaTAController", "Error processing Generate");
                        throw new VisaTAException("Error processing Generate", i);
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "generate called Successfully");
                    }
                    return response.Mc.Md.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "generate: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public byte[] getNonce(int i) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling getNonce");
        }
        if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new GetNonce.Request(i));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "Error:getNonce executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "getNonce: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    GetNonce.Response response = new GetNonce.Response(executeNoLoad);
                    int i2 = (int) response.Me.return_code.get();
                    if (i2 != 0) {
                        Log.m286e("VisaTAController", "Error processing GetNonce");
                        throw new VisaTAException("Error processing GetNonce", i2);
                    }
                    byte[] data = response.Me.out_data.getData();
                    if (data != null && DEBUG) {
                        dumpHex("VisaTAController", "getNonce: ", data);
                    }
                    return data;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        }
        Log.m286e("VisaTAController", "getNonce: Error: TA is not loaded, please call loadTA() API first!");
        throw new VisaTAException("Visa TA not loaded", 1);
    }

    public boolean authenticateTransaction(byte[] bArr) {
        boolean z = true;
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling authenticateTransaction");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "authenticateTransaction: Error: secureObject length invalid! secureObject.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "authenticateTransaction: Error: secureObject is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new AuthenticateTransaction.Request(bArr));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "Error: authenticateTransaction executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "authenticateTransaction: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    long j = new AuthenticateTransaction.Response(executeNoLoad).LS.auth_result.get();
                    if (j != 0) {
                        z = false;
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "authenticateTransaction: auth_result = " + j);
                    }
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public boolean m1710a(int i, byte[] bArr) {
        boolean z = true;
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling transmitMstData");
        }
        if (bArr == null || bArr.length > 28) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "transmitMstData: Error: config length invalid! config.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "transmitMstData: Error: config is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new TransmitMstData.Request(i, bArr));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "Error: transmitMstData executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "transmitMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    if (new TransmitMstData.Response(executeNoLoad).MD.return_code.get() != 0) {
                        z = false;
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "TransmitMstData: ret = " + z);
                    }
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "transmitMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public boolean clearMstData() {
        Log.m285d("VisaTAController", "Calling clearMstData");
        resetMstFlag();
        if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ClearMstData.Request(0));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "Error: clearMstData executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "clearMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    ClearMstData.Response response = new ClearMstData.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "clearMstData: success ");
                    }
                    if (response.LT.return_code.get() != 0) {
                        return false;
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        }
        Log.m286e("VisaTAController", "clearMstData: Error: TA is not loaded, please call loadTA() API first!");
        throw new VisaTAException("Visa TA not loaded", 1);
    }

    public byte[] m1715e(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling getInAppPayload");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "getInAppPayload: Error: data length invalid! data.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "getInAppPayload: Error: data is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (bArr2 == null || bArr2.length > PKIFailureInfo.certConfirmed) {
            if (bArr2 != null) {
                Log.m286e("VisaTAController", "getInAppPayload: Error: encToken length invalid! merchantCert.len = " + bArr2.length);
            } else {
                Log.m286e("VisaTAController", "getInAppPayload: Error: encToken is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (bArr3 == null) {
            Log.m290w("VisaTAController", "getInAppPayload: Error: nonce is null!");
            throw new VisaTAException("Invalid NONCE Input", 5);
        } else if (isTALoaded()) {
            ir();
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new GenInAppPaymentInfo.Request(bArr, bArr2, bArr3));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "getInAppPayload: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "getInAppPayload: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    GenInAppPaymentInfo.Response response = new GenInAppPaymentInfo.Response(executeNoLoad);
                    int i = (int) response.LY.return_code.get();
                    if (i != 0) {
                        Log.m286e("VisaTAController", "Error processing getInAppPayload");
                        throw new VisaTAException("Error processing getInAppPayload", i);
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "getInAppPayload called Successfully");
                    }
                    return response.LY.resp.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "getInAppPayload: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }

    public byte[] m1714b(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        if (DEBUG) {
            Log.m285d("VisaTAController", "Calling getInAppJwePayload");
        }
        if (bArr == null || bArr.length > PKIFailureInfo.certConfirmed) {
            if (bArr != null) {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: data length invalid! data.len = " + bArr.length);
            } else {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: data is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (bArr2 == null || bArr2.length > PKIFailureInfo.certConfirmed) {
            if (bArr2 != null) {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: encToken length invalid! encToken.len = " + bArr2.length);
            } else {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: encToken is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (bArr3 == null || bArr3.length > PKIFailureInfo.certConfirmed) {
            if (bArr3 != null) {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: merchantCert length invalid! merchantCert.len = " + bArr3.length);
            } else {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: merchantCert is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (bArr4 == null || bArr4.length > PKIFailureInfo.certConfirmed) {
            if (bArr4 != null) {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: caCert length invalid! caCert.len = " + bArr2.length);
            } else {
                Log.m286e("VisaTAController", "getInAppJwePayload: Error: caCert is null!");
            }
            throw new VisaTAException("Invalid Input", 5);
        } else if (isTALoaded()) {
            ir();
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new GenInAppJwePaymentInfo.Request(bArr, bArr2, bArr3, bArr4));
                if (executeNoLoad == null) {
                    Log.m286e("VisaTAController", "getInAppJwePayload: Error: executeNoLoad failed");
                    throw new VisaTAException("Error communicating with the TA", 3);
                } else if (executeNoLoad.mResponseCode != 0) {
                    Log.m286e("VisaTAController", "getInAppJwePayload: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                    throw new VisaTAException("TA command returned error", 4);
                } else {
                    GenInAppPaymentInfo.Response response = new GenInAppPaymentInfo.Response(executeNoLoad);
                    int i = (int) response.LY.return_code.get();
                    if (i != 0) {
                        Log.m286e("VisaTAController", "Error processing getInAppJwePayload");
                        throw new VisaTAException("Error processing getInAppJwePayload", i);
                    }
                    if (DEBUG) {
                        Log.m285d("VisaTAController", "getInAppJwePayload called Successfully");
                    }
                    return response.LY.resp.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new VisaTAException("Invalid Input", 5);
                }
                throw new VisaTAException("Error communicating with the TA", 3);
            }
        } else {
            Log.m286e("VisaTAController", "getInAppJwePayload: Error: TA is not loaded, please call loadTA() API first!");
            throw new VisaTAException("Visa TA not loaded", 1);
        }
    }
}
