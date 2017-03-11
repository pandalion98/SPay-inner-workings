package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import android.content.Context;
import android.spay.CertInfo;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AddCard;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.AuthenticateTransaction;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ClearData;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractGiftCardDetail;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.ExtractLoyaltyCardDetail;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.GetNonce;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Request;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.LoadCerts.Response;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.MstTransmit;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.RetrieveFromStorage;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.StoreData;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccCommands.Utility_enc4Server_Transport;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PlccTAController extends TAController {
    private static final long AUTHENTICATE_TRANSACT_SUCCESS_VAL = 0;
    public static final String CARD_BRAND_GIFT = "GIFT";
    public static final String CARD_BRAND_LOYALTY = "LOYALTY";
    public static final String CARD_BRAND_PLCC = "PLCC";
    public static final int FAILURE_RET_VAL = -1;
    private static final TimeZone GMT;
    public static final int SUCCESS_RET_VAL = 0;
    private static final String TAG = "PlccTAController";
    public static TAInfo TA_INFO;
    private static PlccTAController mInstance;
    private static Map<String, ServerCerts> mServerCertsHash;
    private CertInfo certsInfoCache;
    private PlccDeviceCerts mPlccDeviceCerts;
    private TACerts mTACerts;

    private static class ServerCerts {
        private byte[] plcc_cert_enc;
        private byte[] plcc_cert_sign;
        private byte[] plcc_cert_sub;

        private ServerCerts() {
            this.plcc_cert_sign = null;
            this.plcc_cert_enc = null;
            this.plcc_cert_sub = null;
        }
    }

    public static class TACerts {
        public byte[] drk;
        public byte[] encryptcert;
        public byte[] signcert;

        TACerts(byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this.drk = bArr;
            this.signcert = bArr2;
            this.encryptcert = bArr3;
        }
    }

    static {
        GMT = TimeZone.getTimeZone("GMT");
        mInstance = null;
        TA_INFO = new PlccTAInfo();
        mServerCertsHash = new HashMap();
    }

    private PlccTAController(Context context) {
        super(context, TA_INFO);
        this.mTACerts = null;
        this.certsInfoCache = null;
        this.mPlccDeviceCerts = null;
    }

    protected boolean init() {
        if (super.init()) {
            this.mPlccDeviceCerts = new PlccDeviceCerts(this);
            return true;
        }
        Log.m286e(TAG, "Error: init failed");
        return false;
    }

    public static synchronized PlccTAController createOnlyInstance(Context context) {
        PlccTAController plccTAController;
        synchronized (PlccTAController.class) {
            if (mInstance == null) {
                mInstance = new PlccTAController(context);
            }
            plccTAController = mInstance;
        }
        return plccTAController;
    }

    public static synchronized PlccTAController getInstance() {
        PlccTAController plccTAController;
        synchronized (PlccTAController.class) {
            plccTAController = mInstance;
        }
        return plccTAController;
    }

    public synchronized void setPlccServerCerts(String str, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        Log.m285d(TAG, "setPlccServerCerts: cardBrand " + str);
        ServerCerts serverCerts = new ServerCerts();
        serverCerts.plcc_cert_sign = bArr;
        serverCerts.plcc_cert_enc = bArr2;
        serverCerts.plcc_cert_sub = bArr3;
        if (CARD_BRAND_PLCC.equals(str)) {
            mServerCertsHash.put(CARD_BRAND_PLCC, serverCerts);
        } else if (CARD_BRAND_LOYALTY.equals(str)) {
            mServerCertsHash.put(CARD_BRAND_LOYALTY, serverCerts);
        } else if (CARD_BRAND_GIFT.equals(str)) {
            mServerCertsHash.put(CARD_BRAND_GIFT, serverCerts);
        }
    }

    private boolean loadAllCerts(String str) {
        if (DEBUG) {
            Log.m285d(TAG, "Calling loadAllCerts: cardBrand:  " + str);
        }
        if (isTALoaded()) {
            if (!this.mPlccDeviceCerts.isLoaded()) {
                Log.m285d(TAG, "mPlccDeviceCerts is not loaded");
                if (!this.mPlccDeviceCerts.load()) {
                    Log.m286e(TAG, "Error: Plcc Device Certs Load failed");
                    return false;
                }
            }
            byte[] devicePrivateSignCert = this.mPlccDeviceCerts.getDevicePrivateSignCert();
            byte[] devicePrivateEncryptionCert = this.mPlccDeviceCerts.getDevicePrivateEncryptionCert();
            if (devicePrivateSignCert == null || devicePrivateEncryptionCert == null) {
                Log.m286e(TAG, "loadAllCerts: Error: Certificate Data is NULL");
                this.certsInfoCache = null;
                return false;
            }
            try {
                TACommandRequest request;
                ServerCerts serverCerts = (ServerCerts) mServerCertsHash.get(str);
                if (serverCerts != null) {
                    request = new Request(devicePrivateSignCert, devicePrivateEncryptionCert, serverCerts.plcc_cert_sign, serverCerts.plcc_cert_enc, serverCerts.plcc_cert_sub);
                } else {
                    request = new Request(devicePrivateSignCert, devicePrivateEncryptionCert, null, null, null);
                }
                TACommandResponse executeNoLoad = executeNoLoad(request);
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "loadAllCerts: Error: executeNoLoad failed");
                    return false;
                }
                Response response = new Response(executeNoLoad);
                long j = response.mRetVal.return_code.get();
                if (executeNoLoad.mResponseCode == 0 && j == AUTHENTICATE_TRANSACT_SUCCESS_VAL) {
                    this.mTACerts = new TACerts(response.mRetVal.cert_drk.getData(), response.mRetVal.cert_sign.getData(), response.mRetVal.cert_encrypt.getData());
                    if (DEBUG) {
                        Log.m285d(TAG, "enc_cert= " + Arrays.toString(response.mRetVal.cert_encrypt.getData()));
                        Log.m285d(TAG, "sign_cert= " + Arrays.toString(response.mRetVal.cert_sign.getData()));
                        Log.m285d(TAG, "drk= " + Arrays.toString(response.mRetVal.cert_drk.getData()));
                        Log.m285d(TAG, "loadAllCerts called Successfully");
                    }
                    return true;
                }
                Log.m286e(TAG, "loadAllCerts: Error: code: " + executeNoLoad.mResponseCode + " errorMsg:" + executeNoLoad.mErrorMsg);
                return false;
            } catch (Throwable e) {
                Log.m284c(TAG, e.getMessage(), e);
                if (e instanceof IllegalArgumentException) {
                    throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        }
        Log.m286e(TAG, "loadAllCerts: Error: TA is not loaded, please call loadTA() API first!");
        return false;
    }

    public synchronized TACerts getAllCerts(String str) {
        if (this.mTACerts == null && !loadAllCerts(str)) {
            Log.m286e(TAG, "getCerts: Error: loadAllCerts returned false!");
        }
        return this.mTACerts;
    }

    public synchronized byte[] getCerts(String str) {
        byte[] bArr;
        if (this.mTACerts != null || loadAllCerts(str)) {
            bArr = new byte[(((((((this.mTACerts.drk.length + 3) + 1) + 2) + this.mTACerts.signcert.length) + 1) + 2) + this.mTACerts.encryptcert.length)];
            bArr[SUCCESS_RET_VAL] = (byte) 1;
            System.arraycopy(ByteBuffer.allocate(2).putShort((short) this.mTACerts.drk.length).array(), SUCCESS_RET_VAL, bArr, 1, 2);
            System.arraycopy(this.mTACerts.drk, SUCCESS_RET_VAL, bArr, 3, this.mTACerts.drk.length);
            int length = this.mTACerts.drk.length + 3;
            bArr[length] = (byte) 2;
            length++;
            System.arraycopy(ByteBuffer.allocate(2).putShort((short) this.mTACerts.signcert.length).array(), SUCCESS_RET_VAL, bArr, length, 2);
            length += 2;
            System.arraycopy(this.mTACerts.signcert, SUCCESS_RET_VAL, bArr, length, this.mTACerts.signcert.length);
            length += this.mTACerts.signcert.length;
            bArr[length] = (byte) 3;
            length++;
            System.arraycopy(ByteBuffer.allocate(2).putShort((short) this.mTACerts.encryptcert.length).array(), SUCCESS_RET_VAL, bArr, length, 2);
            int i = length + 2;
            System.arraycopy(this.mTACerts.encryptcert, SUCCESS_RET_VAL, bArr, i, this.mTACerts.encryptcert.length);
            i += this.mTACerts.encryptcert.length;
        } else {
            Log.m286e(TAG, "getCerts: Error: loadAllCerts returned false!");
            bArr = null;
        }
        return bArr;
    }

    private byte[] lump2ByteArray(MstConfigLump mstConfigLump) {
        byte[] bArr = new byte[(mstConfigLump.segments.size() * 4)];
        int i = SUCCESS_RET_VAL;
        for (MstConfigSegment mstConfigSegment : mstConfigLump.segments) {
            int i2 = i + 1;
            bArr[i] = (byte) mstConfigSegment.trackIndex;
            i = i2 + 1;
            bArr[i2] = (byte) mstConfigSegment.leadingZeros;
            i2 = i + 1;
            bArr[i] = (byte) mstConfigSegment.trailingZeros;
            i = i2 + 1;
            bArr[i2] = mstConfigSegment.reverse ? (byte) 1 : (byte) 0;
        }
        return bArr;
    }

    public synchronized byte[] addCard(String str, byte[] bArr) {
        byte[] bArr2 = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling plcc add card");
            }
            if (!isTALoaded()) {
                Log.m286e(TAG, "addCard: Error: TA is not loaded, please call loadTA() API first!");
            } else if (loadAllCerts(str)) {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new AddCard.Request(bArr));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "add Card: Error: executeNoLoad failed");
                    } else {
                        AddCard.Response response = new AddCard.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "tzEnc data  = " + Arrays.toString(response.mRetVal.encPlccData.getData()));
                        }
                        bArr2 = response.mRetVal.encPlccData.getData();
                    }
                } catch (Throwable e) {
                    Log.m284c(TAG, e.getMessage(), e);
                    if (e instanceof IllegalArgumentException) {
                        throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                    }
                    throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                }
            } else {
                Log.m286e(TAG, "addCard: Error: loadAllCerts returned false!");
            }
        }
        return bArr2;
    }

    public synchronized byte[] utility_enc4Server_Transport(String str, byte[] bArr, long j) {
        byte[] bArr2 = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling utility_enc4Server_Transport");
            }
            if (!isTALoaded()) {
                Log.m286e(TAG, "utility_enc4Server_Transport: Error: TA is not loaded, please call loadTA() API first!");
            } else if (loadAllCerts(str)) {
                byte[] currentTimestamp = getCurrentTimestamp(j);
                if (currentTimestamp != null && DEBUG) {
                    Log.m285d(TAG, "Utility_enc4Server_Transport: timestamp_bytes = " + Arrays.toString(currentTimestamp));
                }
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new Utility_enc4Server_Transport.Request(bArr, currentTimestamp));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Utility_enc4Server_Transport: Error: executeNoLoad failed");
                    } else {
                        Utility_enc4Server_Transport.Response response = new Utility_enc4Server_Transport.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "Utility_enc4Server_Transport called Successfully");
                        }
                        bArr2 = response.mRetVal.resp.getData();
                        if (bArr2 != null && DEBUG) {
                            Log.m285d(TAG, "utility_enc4Server_Transport: encryptedData length = " + bArr2.length);
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c(TAG, e.getMessage(), e);
                    if (e instanceof IllegalArgumentException) {
                        throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                    }
                    throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                }
            } else {
                Log.m286e(TAG, "utility_enc4Server_Transport: Error: loadAllCerts returned false!");
            }
        }
        return bArr2;
    }

    public synchronized boolean mstTransmit(byte[] bArr, int i, byte[] bArr2) {
        boolean z;
        if (DEBUG) {
            Log.m285d(TAG, "Calling mstTransmit");
        }
        z = false;
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new MstTransmit.Request(i, bArr, bArr2));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: mstTransmit executeNoLoad failed");
            } else {
                if (new MstTransmit.Response(executeNoLoad).mRetVal.return_code.get() == AUTHENTICATE_TRANSACT_SUCCESS_VAL) {
                    z = true;
                }
                if (DEBUG) {
                    Log.m285d(TAG, "mstTransmit: " + z);
                }
            }
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
            }
            throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
        }
        return z;
    }

    public synchronized ExtractCardDetailResult extractGiftCardDetail(byte[] bArr, byte[] bArr2) {
        ExtractCardDetailResult extractCardDetailResult;
        if (DEBUG) {
            Log.m285d(TAG, "Calling extractGiftCardDetail");
        }
        extractCardDetailResult = new ExtractCardDetailResult();
        extractCardDetailResult.setErrorCode(FAILURE_RET_VAL);
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new ExtractGiftCardDetail.Request(bArr, bArr2));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: extractGiftCardDetail executeNoLoad failed");
            } else {
                ExtractGiftCardDetail.Response response = new ExtractGiftCardDetail.Response(executeNoLoad);
                if (response == null || response.mRetVal == null || response.mRetVal.return_code == null) {
                    Log.m286e(TAG, "response or response.mRetVal or response.mRetVal.return_code is null");
                } else {
                    long j = response.mRetVal.return_code.get();
                    if (j == AUTHENTICATE_TRANSACT_SUCCESS_VAL) {
                        if (DEBUG) {
                            Log.m285d(TAG, "extractGiftCardDetail Success: ");
                        }
                        if (response.mRetVal.cardnumber != null) {
                            extractCardDetailResult.setCardnumber(new String(response.mRetVal.cardnumber.getData(), "UTF-8"));
                        }
                        if (response.mRetVal.pin != null) {
                            extractCardDetailResult.setPin(new String(response.mRetVal.pin.getData(), "UTF-8"));
                        }
                        if (response.mRetVal.barcodecontent != null) {
                            extractCardDetailResult.setBarcodeContent(new String(response.mRetVal.barcodecontent.getData(), "UTF-8"));
                        }
                        extractCardDetailResult.setErrorCode(SUCCESS_RET_VAL);
                    } else {
                        Log.m286e(TAG, "extractGiftCardDetail Fail: respValue = " + j);
                    }
                }
            }
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
            }
            throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
        }
        return extractCardDetailResult;
    }

    public synchronized ExtractCardDetailResult extractLoyaltyCardDetail(byte[] bArr, byte[] bArr2) {
        ExtractCardDetailResult extractCardDetailResult = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling extractLoyaltyCardDetail");
            }
            if (bArr == null || bArr.length <= 0) {
                Log.m286e(TAG, "extractLoyaltyCardDetail: cardId is null or empty");
            }
            if (bArr2 == null || bArr2.length <= 0) {
                Log.m286e(TAG, "extractLoyaltyCardDetail: cardTzEncData is null or empty");
            } else {
                Log.m287i(TAG, "extractLoyaltyCardDetail data : " + new String(bArr2));
            }
            ExtractCardDetailResult extractCardDetailResult2 = new ExtractCardDetailResult();
            extractCardDetailResult2.setErrorCode(FAILURE_RET_VAL);
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new ExtractLoyaltyCardDetail.Request(bArr, bArr2));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: extractLoyaltyCardDetail executeNoLoad failed");
                } else {
                    ExtractLoyaltyCardDetail.Response response = new ExtractLoyaltyCardDetail.Response(executeNoLoad);
                    if (response == null || response.mRetVal == null || response.mRetVal.return_code == null) {
                        Log.m286e(TAG, "response or response.mRetVal or response.mRetVal.return_code is null");
                        extractCardDetailResult2 = null;
                    } else {
                        long j = response.mRetVal.return_code.get();
                        if (j == AUTHENTICATE_TRANSACT_SUCCESS_VAL) {
                            Log.m287i(TAG, "extractLoyaltyCardDetail Success: ");
                            if (response.mRetVal.cardnumber != null && response.mRetVal.cardnumber.getData().length > 0) {
                                Log.m287i(TAG, "cardNumber not null ");
                                extractCardDetailResult2.setCardnumber(new String(response.mRetVal.cardnumber.getData(), "UTF-8"));
                            }
                            if (response.mRetVal.barcodecontent != null && response.mRetVal.barcodecontent.getData().length > 0) {
                                Log.m287i(TAG, "barcode content not null ");
                                extractCardDetailResult2.setBarcodeContent(new String(response.mRetVal.barcodecontent.getData(), "UTF-8"));
                            }
                            if (response.mRetVal.imgSessionKey != null && response.mRetVal.imgSessionKey.getData().length > 0) {
                                Log.m287i(TAG, "key not null ");
                                extractCardDetailResult2.setImgSessionKey(new String(response.mRetVal.imgSessionKey.getData(), "UTF-8"));
                            }
                            if (response.mRetVal.extra != null && response.mRetVal.extra.getData().length > 0) {
                                Log.m287i(TAG, "extra not null ");
                                extractCardDetailResult2.setExtraContent(new String(response.mRetVal.extra.getData(), "UTF-8"));
                            }
                            extractCardDetailResult2.setErrorCode(SUCCESS_RET_VAL);
                        } else {
                            Log.m286e(TAG, "extractLoyaltyCardDetail Fail: respValue = " + j);
                            extractCardDetailResult2 = null;
                        }
                    }
                    extractCardDetailResult = extractCardDetailResult2;
                }
            } catch (Throwable e) {
                Log.m284c(TAG, e.getMessage(), e);
                if (e instanceof IllegalArgumentException) {
                    throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        }
        return extractCardDetailResult;
    }

    public synchronized byte[] getNonce(int i) {
        byte[] bArr = null;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling getNonce");
            }
            if (isTALoaded()) {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new GetNonce.Request(i));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error:getNonce executeNoLoad failed");
                    } else {
                        bArr = new GetNonce.Response(executeNoLoad).mRetVal.out_data.getData();
                        if (DEBUG) {
                            dumpHex(TAG, "getNonce: ", bArr);
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c(TAG, e.getMessage(), e);
                    if (e instanceof IllegalArgumentException) {
                        throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                    }
                    throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                }
            }
            Log.m286e(TAG, "getNonce: Error: TA is not loaded, please call loadTA() API first!");
        }
        return bArr;
    }

    public synchronized boolean authenticateTransaction(byte[] bArr) {
        boolean z = false;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling authenticateTransaction");
            }
            if (isTALoaded()) {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new AuthenticateTransaction.Request(bArr));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: authenticateTransaction executeNoLoad failed");
                    } else {
                        long j = new AuthenticateTransaction.Response(executeNoLoad).mRetVal.auth_result.get();
                        if (j == AUTHENTICATE_TRANSACT_SUCCESS_VAL) {
                            z = true;
                        }
                        if (DEBUG) {
                            Log.m285d(TAG, "authenticateTransaction: auth_result = " + j);
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c(TAG, e.getMessage(), e);
                    if (e instanceof IllegalArgumentException) {
                        throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                    }
                    throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                }
            }
            Log.m286e(TAG, "authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
        }
        return z;
    }

    public synchronized boolean clearMstData() {
        boolean z = false;
        synchronized (this) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling clearMstData");
            }
            resetMstFlag();
            if (isTALoaded()) {
                try {
                    TACommandResponse executeNoLoad = executeNoLoad(new ClearData.Request(SUCCESS_RET_VAL));
                    if (executeNoLoad == null) {
                        Log.m286e(TAG, "Error: clearMstData executeNoLoad failed");
                        throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                    } else if (executeNoLoad.mResponseCode != 0) {
                        Log.m286e(TAG, "clearMstData: Error: TA command returned error! resp.mResponseCode = " + executeNoLoad.mResponseCode);
                        throw new PlccTAException("TA command returned error", SPayTUIException.ERR_UNKNOWN);
                    } else {
                        ClearData.Response response = new ClearData.Response(executeNoLoad);
                        if (DEBUG) {
                            Log.m285d(TAG, "clearMstData: success ");
                        }
                        if (response.mRetVal.return_code.get() == AUTHENTICATE_TRANSACT_SUCCESS_VAL) {
                            z = true;
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c(TAG, e.getMessage(), e);
                    if (e instanceof IllegalArgumentException) {
                        throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                    }
                    throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
                }
            }
            Log.m286e(TAG, "clearMstData: Error: TA is not loaded, please call loadTA() API first!");
            throw new PlccTAException("Plcc TA not loaded", SPayTUIException.ERR_TZ_TA_NOT_AVAILABLE);
        }
        return z;
    }

    private static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        String toUpperCase = str.toUpperCase();
        int length = toUpperCase.length() / 2;
        char[] toCharArray = toUpperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = SUCCESS_RET_VAL; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(toCharArray[i2 + 1]) | (charToByte(toCharArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte[] getCurrentTimestamp(long j) {
        String gmtTimestamp = gmtTimestamp(new Date(j));
        if (DEBUG) {
            Log.m285d(TAG, "Network timestamp in sdf  =" + gmtTimestamp);
        }
        return hexStringToBytes(gmtTimestamp);
    }

    private static String gmtTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(GMT);
        return simpleDateFormat.format(date);
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public synchronized byte[] storeData(byte[] bArr) {
        byte[] bArr2;
        bArr2 = null;
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new StoreData.Request(bArr));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: storeData executeNoLoad failed");
            } else {
                bArr2 = new StoreData.Response(executeNoLoad).mRetVal.buf_plcc_data.getData();
            }
        } catch (Throwable e) {
            Log.m286e(TAG, "Error: storeData failed");
            Log.m284c(TAG, e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
            }
            throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
        } catch (Throwable th) {
        }
        return bArr2;
    }

    public synchronized byte[] retrieveFromStorage(byte[] bArr) {
        byte[] bArr2;
        bArr2 = null;
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new RetrieveFromStorage.Request(bArr));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: retrieveFromStorage executeNoLoad failed");
            } else {
                bArr2 = new RetrieveFromStorage.Response(executeNoLoad).mRetVal.buf_plcc_data.getData();
            }
        } catch (Throwable e) {
            Log.m286e(TAG, "Error: retrieveFromStorage failed");
            Log.m284c(TAG, e.getMessage(), e);
            if (e instanceof IllegalArgumentException) {
                throw new PlccTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
            }
            throw new PlccTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
        } catch (Throwable th) {
        }
        return bArr2;
    }
}
