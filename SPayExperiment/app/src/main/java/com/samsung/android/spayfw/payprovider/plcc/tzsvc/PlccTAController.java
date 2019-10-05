/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.CertInfo
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.nio.ByteBuffer
 *  java.text.SimpleDateFormat
 *  java.util.Arrays
 *  java.util.Date
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import android.content.Context;
import android.spay.CertInfo;
import android.spay.TACommandResponse;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class PlccTAController
extends TAController {
    private static final long AUTHENTICATE_TRANSACT_SUCCESS_VAL = 0L;
    public static final String CARD_BRAND_GIFT = "GIFT";
    public static final String CARD_BRAND_LOYALTY = "LOYALTY";
    public static final String CARD_BRAND_PLCC = "PLCC";
    public static final int FAILURE_RET_VAL = -1;
    private static final TimeZone GMT = TimeZone.getTimeZone((String)"GMT");
    public static final int SUCCESS_RET_VAL = 0;
    private static final String TAG = "PlccTAController";
    public static TAInfo TA_INFO;
    private static PlccTAController mInstance;
    private static Map<String, ServerCerts> mServerCertsHash;
    private CertInfo certsInfoCache = null;
    private PlccDeviceCerts mPlccDeviceCerts = null;
    private TACerts mTACerts = null;

    static {
        mInstance = null;
        TA_INFO = new PlccTAInfo();
        mServerCertsHash = new HashMap();
    }

    private PlccTAController(Context context) {
        super(context, TA_INFO);
    }

    private static byte charToByte(char c2) {
        return (byte)"0123456789ABCDEF".indexOf((int)c2);
    }

    public static PlccTAController createOnlyInstance(Context context) {
        Class<PlccTAController> class_ = PlccTAController.class;
        synchronized (PlccTAController.class) {
            if (mInstance == null) {
                mInstance = new PlccTAController(context);
            }
            PlccTAController plccTAController = mInstance;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return plccTAController;
        }
    }

    private static byte[] getCurrentTimestamp(long l2) {
        String string = PlccTAController.gmtTimestamp(new Date(l2));
        if (DEBUG) {
            Log.d(TAG, "Network timestamp in sdf  =" + string);
        }
        return PlccTAController.hexStringToBytes(string);
    }

    public static PlccTAController getInstance() {
        Class<PlccTAController> class_ = PlccTAController.class;
        synchronized (PlccTAController.class) {
            PlccTAController plccTAController = mInstance;
            // ** MonitorExit[var2] (shouldn't be in output)
            return plccTAController;
        }
    }

    private static String gmtTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(GMT);
        return simpleDateFormat.format(date);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static byte[] hexStringToBytes(String string) {
        if (string == null) return null;
        if (string.equals((Object)"")) {
            return null;
        }
        String string2 = string.toUpperCase();
        int n2 = string2.length() / 2;
        char[] arrc = string2.toCharArray();
        byte[] arrby = new byte[n2];
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3 * 2;
            arrby[n3] = (byte)(PlccTAController.charToByte(arrc[n4]) << 4 | PlccTAController.charToByte(arrc[n4 + 1]));
            ++n3;
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean loadAllCerts(String string) {
        PlccCommands.LoadCerts.Response response;
        if (DEBUG) {
            Log.d(TAG, "Calling loadAllCerts: cardBrand:  " + string);
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "loadAllCerts: Error: TA is not loaded, please call loadTA() API first!");
            return false;
        }
        if (!this.mPlccDeviceCerts.isLoaded()) {
            Log.d(TAG, "mPlccDeviceCerts is not loaded");
            if (!this.mPlccDeviceCerts.load()) {
                Log.e(TAG, "Error: Plcc Device Certs Load failed");
                return false;
            }
        }
        byte[] arrby = this.mPlccDeviceCerts.getDevicePrivateSignCert();
        byte[] arrby2 = this.mPlccDeviceCerts.getDevicePrivateEncryptionCert();
        if (arrby == null || arrby2 == null) {
            Log.e(TAG, "loadAllCerts: Error: Certificate Data is NULL");
            this.certsInfoCache = null;
            return false;
        }
        try {
            ServerCerts serverCerts = (ServerCerts)mServerCertsHash.get((Object)string);
            PlccCommands.LoadCerts.Request request = serverCerts != null ? new PlccCommands.LoadCerts.Request(arrby, arrby2, serverCerts.plcc_cert_sign, serverCerts.plcc_cert_enc, serverCerts.plcc_cert_sub) : new PlccCommands.LoadCerts.Request(arrby, arrby2, null, null, null);
            TACommandResponse tACommandResponse = this.executeNoLoad(request);
            if (tACommandResponse == null) {
                Log.e(TAG, "loadAllCerts: Error: executeNoLoad failed");
                return false;
            }
            response = new PlccCommands.LoadCerts.Response(tACommandResponse);
            long l2 = response.mRetVal.return_code.get();
            if (tACommandResponse.mResponseCode != 0 || l2 != 0L) {
                Log.e(TAG, "loadAllCerts: Error: code: " + tACommandResponse.mResponseCode + " errorMsg:" + tACommandResponse.mErrorMsg);
                return false;
            }
        }
        catch (Exception exception) {
            Log.c(TAG, exception.getMessage(), exception);
            if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
            {
                throw new PlccTAException("Invalid Input", 1004);
            }
        }
        this.mTACerts = new TACerts(response.mRetVal.cert_drk.getData(), response.mRetVal.cert_sign.getData(), response.mRetVal.cert_encrypt.getData());
        if (!DEBUG) return true;
        {
            Log.d(TAG, "enc_cert= " + Arrays.toString((byte[])response.mRetVal.cert_encrypt.getData()));
            Log.d(TAG, "sign_cert= " + Arrays.toString((byte[])response.mRetVal.cert_sign.getData()));
            Log.d(TAG, "drk= " + Arrays.toString((byte[])response.mRetVal.cert_drk.getData()));
            Log.d(TAG, "loadAllCerts called Successfully");
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] lump2ByteArray(MstConfigLump mstConfigLump) {
        byte[] arrby = new byte[4 * mstConfigLump.segments.size()];
        Iterator iterator = mstConfigLump.segments.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            MstConfigSegment mstConfigSegment = (MstConfigSegment)iterator.next();
            int n3 = n2 + 1;
            arrby[n2] = (byte)mstConfigSegment.trackIndex;
            int n4 = n3 + 1;
            arrby[n3] = (byte)mstConfigSegment.leadingZeros;
            int n5 = n4 + 1;
            arrby[n4] = (byte)mstConfigSegment.trailingZeros;
            n2 = n5 + 1;
            byte by = mstConfigSegment.reverse ? (byte)1 : 0;
            arrby[n5] = by;
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public byte[] addCard(String string, byte[] arrby) {
        byte[] arrby2 = null;
        PlccTAController plccTAController = this;
        // MONITORENTER : plccTAController
        if (DEBUG) {
            Log.d(TAG, "Calling plcc add card");
        }
        if (!this.isTALoaded()) {
            Log.e(TAG, "addCard: Error: TA is not loaded, please call loadTA() API first!");
            // MONITOREXIT : plccTAController
            return arrby2;
        }
        if (!this.loadAllCerts(string)) {
            Log.e(TAG, "addCard: Error: loadAllCerts returned false!");
            return null;
        }
        try {
            TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.AddCard.Request(arrby));
            if (tACommandResponse == null) {
                Log.e(TAG, "add Card: Error: executeNoLoad failed");
                return null;
            }
            PlccCommands.AddCard.Response response = new PlccCommands.AddCard.Response(tACommandResponse);
            byte[] arrby3 = response.mRetVal.encPlccData.getData();
            if (!DEBUG) return arrby3;
            Log.d(TAG, "tzEnc data  = " + Arrays.toString((byte[])response.mRetVal.encPlccData.getData()));
            return arrby3;
        }
        catch (Exception exception) {
            Log.c(TAG, exception.getMessage(), exception);
            if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
            throw new PlccTAException("Invalid Input", 1004);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean authenticateTransaction(byte[] arrby) {
        boolean bl;
        block10 : {
            bl = false;
            PlccTAController plccTAController = this;
            // MONITORENTER : plccTAController
            if (DEBUG) {
                Log.d(TAG, "Calling authenticateTransaction");
            }
            if (!this.isTALoaded()) {
                Log.e(TAG, "authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
                return bl;
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.AuthenticateTransaction.Request(arrby));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: authenticateTransaction executeNoLoad failed");
                    return false;
                }
                long l2 = new PlccCommands.AuthenticateTransaction.Response((TACommandResponse)tACommandResponse).mRetVal.auth_result.get();
                long l3 = l2 LCMP 0L;
                bl = false;
                if (l3 == false) {
                    bl = true;
                }
                if (!DEBUG) break block10;
                Log.d(TAG, "authenticateTransaction: auth_result = " + l2);
                return bl;
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
                if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                throw new PlccTAException("Invalid Input", 1004);
            }
        }
        // MONITOREXIT : plccTAController
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean clearMstData() {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            TACommandResponse tACommandResponse;
            if (DEBUG) {
                Log.d(TAG, "Calling clearMstData");
            }
            this.resetMstFlag();
            if (!this.isTALoaded()) {
                Log.e(TAG, "clearMstData: Error: TA is not loaded, please call loadTA() API first!");
                throw new PlccTAException("Plcc TA not loaded", 1003);
            }
            try {
                tACommandResponse = this.executeNoLoad(new PlccCommands.ClearData.Request(0));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: clearMstData executeNoLoad failed");
                    throw new PlccTAException("Error communicating with the TA", 1001);
                }
                if (tACommandResponse.mResponseCode != 0) {
                    Log.e(TAG, "clearMstData: Error: TA command returned error! resp.mResponseCode = " + tACommandResponse.mResponseCode);
                    throw new PlccTAException("TA command returned error", 9001);
                }
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
                if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                throw new PlccTAException("Invalid Input", 1004);
            }
            PlccCommands.ClearData.Response response = new PlccCommands.ClearData.Response(tACommandResponse);
            if (DEBUG) {
                Log.d(TAG, "clearMstData: success ");
            }
            long l2 = response.mRetVal.return_code.get();
            long l3 = l2 LCMP 0L;
            boolean bl = false;
            if (l3 == false) return true;
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ExtractCardDetailResult extractGiftCardDetail(byte[] arrby, byte[] arrby2) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            ExtractCardDetailResult extractCardDetailResult;
            block15 : {
                block16 : {
                    if (DEBUG) {
                        Log.d(TAG, "Calling extractGiftCardDetail");
                    }
                    extractCardDetailResult = new ExtractCardDetailResult();
                    extractCardDetailResult.setErrorCode(-1);
                    try {
                        TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.ExtractGiftCardDetail.Request(arrby, arrby2));
                        if (tACommandResponse == null) {
                            Log.e(TAG, "Error: extractGiftCardDetail executeNoLoad failed");
                            break block15;
                        }
                        PlccCommands.ExtractGiftCardDetail.Response response = new PlccCommands.ExtractGiftCardDetail.Response(tACommandResponse);
                        if (response == null || response.mRetVal == null || response.mRetVal.return_code == null) break block16;
                        long l2 = response.mRetVal.return_code.get();
                        if (l2 == 0L) {
                            if (DEBUG) {
                                Log.d(TAG, "extractGiftCardDetail Success: ");
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
                            extractCardDetailResult.setErrorCode(0);
                        } else {
                            Log.e(TAG, "extractGiftCardDetail Fail: respValue = " + l2);
                        }
                        break block15;
                    }
                    catch (Exception exception) {
                        Log.c(TAG, exception.getMessage(), exception);
                        if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                        {
                            throw new PlccTAException("Invalid Input", 1004);
                        }
                    }
                }
                Log.e(TAG, "response or response.mRetVal or response.mRetVal.return_code is null");
            }
            return extractCardDetailResult;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ExtractCardDetailResult extractLoyaltyCardDetail(byte[] arrby, byte[] arrby2) {
        ExtractCardDetailResult extractCardDetailResult = null;
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            block17 : {
                if (DEBUG) {
                    Log.d(TAG, "Calling extractLoyaltyCardDetail");
                }
                if (arrby == null || arrby.length <= 0) {
                    Log.e(TAG, "extractLoyaltyCardDetail: cardId is null or empty");
                }
                if (arrby2 == null || arrby2.length <= 0) {
                    Log.e(TAG, "extractLoyaltyCardDetail: cardTzEncData is null or empty");
                } else {
                    Log.i(TAG, "extractLoyaltyCardDetail data : " + new String(arrby2));
                }
                ExtractCardDetailResult extractCardDetailResult2 = new ExtractCardDetailResult();
                extractCardDetailResult2.setErrorCode(-1);
                try {
                    TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.ExtractLoyaltyCardDetail.Request(arrby, arrby2));
                    if (tACommandResponse == null) {
                        Log.e(TAG, "Error: extractLoyaltyCardDetail executeNoLoad failed");
                        break block17;
                    }
                    PlccCommands.ExtractLoyaltyCardDetail.Response response = new PlccCommands.ExtractLoyaltyCardDetail.Response(tACommandResponse);
                    if (response != null && response.mRetVal != null && response.mRetVal.return_code != null) {
                        long l2 = response.mRetVal.return_code.get();
                        if (l2 == 0L) {
                            Log.i(TAG, "extractLoyaltyCardDetail Success: ");
                            if (response.mRetVal.cardnumber != null && response.mRetVal.cardnumber.getData().length > 0) {
                                Log.i(TAG, "cardNumber not null ");
                                extractCardDetailResult2.setCardnumber(new String(response.mRetVal.cardnumber.getData(), "UTF-8"));
                            }
                            if (response.mRetVal.barcodecontent != null && response.mRetVal.barcodecontent.getData().length > 0) {
                                Log.i(TAG, "barcode content not null ");
                                extractCardDetailResult2.setBarcodeContent(new String(response.mRetVal.barcodecontent.getData(), "UTF-8"));
                            }
                            if (response.mRetVal.imgSessionKey != null && response.mRetVal.imgSessionKey.getData().length > 0) {
                                Log.i(TAG, "key not null ");
                                extractCardDetailResult2.setImgSessionKey(new String(response.mRetVal.imgSessionKey.getData(), "UTF-8"));
                            }
                            if (response.mRetVal.extra != null && response.mRetVal.extra.getData().length > 0) {
                                Log.i(TAG, "extra not null ");
                                extractCardDetailResult2.setExtraContent(new String(response.mRetVal.extra.getData(), "UTF-8"));
                            }
                            extractCardDetailResult2.setErrorCode(0);
                            return extractCardDetailResult2;
                        }
                        Log.e(TAG, "extractLoyaltyCardDetail Fail: respValue = " + l2);
                        return null;
                    }
                    Log.e(TAG, "response or response.mRetVal or response.mRetVal.return_code is null");
                    return null;
                }
                catch (Exception exception) {
                    Log.c(TAG, exception.getMessage(), exception);
                    if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                    throw new PlccTAException("Invalid Input", 1004);
                }
            }
            return extractCardDetailResult;
        }
    }

    public TACerts getAllCerts(String string) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            if (this.mTACerts == null && !this.loadAllCerts(string)) {
                Log.e(TAG, "getCerts: Error: loadAllCerts returned false!");
            }
            TACerts tACerts = this.mTACerts;
            return tACerts;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getCerts(String string) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            if (this.mTACerts == null && !this.loadAllCerts(string)) {
                Log.e(TAG, "getCerts: Error: loadAllCerts returned false!");
                return null;
            }
            byte[] arrby = new byte[2 + (1 + (2 + (1 + (3 + this.mTACerts.drk.length)) + this.mTACerts.signcert.length)) + this.mTACerts.encryptcert.length];
            arrby[0] = 1;
            System.arraycopy((Object)ByteBuffer.allocate((int)2).putShort((short)this.mTACerts.drk.length).array(), (int)0, (Object)arrby, (int)1, (int)2);
            System.arraycopy((Object)this.mTACerts.drk, (int)0, (Object)arrby, (int)3, (int)this.mTACerts.drk.length);
            int n2 = 3 + this.mTACerts.drk.length;
            arrby[n2] = 2;
            int n3 = n2 + 1;
            System.arraycopy((Object)ByteBuffer.allocate((int)2).putShort((short)this.mTACerts.signcert.length).array(), (int)0, (Object)arrby, (int)n3, (int)2);
            int n4 = n3 + 2;
            System.arraycopy((Object)this.mTACerts.signcert, (int)0, (Object)arrby, (int)n4, (int)this.mTACerts.signcert.length);
            int n5 = n4 + this.mTACerts.signcert.length;
            arrby[n5] = 3;
            int n6 = n5 + 1;
            System.arraycopy((Object)ByteBuffer.allocate((int)2).putShort((short)this.mTACerts.encryptcert.length).array(), (int)0, (Object)arrby, (int)n6, (int)2);
            int n7 = n6 + 2;
            System.arraycopy((Object)this.mTACerts.encryptcert, (int)0, (Object)arrby, (int)n7, (int)this.mTACerts.encryptcert.length);
            int n8 = this.mTACerts.encryptcert.length;
            n7 + n8;
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public byte[] getNonce(int n2) {
        byte[] arrby;
        block9 : {
            arrby = null;
            PlccTAController plccTAController = this;
            // MONITORENTER : plccTAController
            if (DEBUG) {
                Log.d(TAG, "Calling getNonce");
            }
            if (!this.isTALoaded()) {
                Log.e(TAG, "getNonce: Error: TA is not loaded, please call loadTA() API first!");
                return arrby;
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.GetNonce.Request(n2));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error:getNonce executeNoLoad failed");
                    return null;
                }
                arrby = new PlccCommands.GetNonce.Response((TACommandResponse)tACommandResponse).mRetVal.out_data.getData();
                if (!DEBUG) break block9;
                this.dumpHex(TAG, "getNonce: ", arrby);
                return arrby;
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
                if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                throw new PlccTAException("Invalid Input", 1004);
            }
        }
        // MONITOREXIT : plccTAController
        return arrby;
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            Log.e(TAG, "Error: init failed");
            return false;
        }
        this.mPlccDeviceCerts = new PlccDeviceCerts(this);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean mstTransmit(byte[] arrby, int n2, byte[] arrby2) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            if (DEBUG) {
                Log.d(TAG, "Calling mstTransmit");
            }
            boolean bl = false;
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.MstTransmit.Request(n2, arrby, arrby2));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Error: mstTransmit executeNoLoad failed");
                } else {
                    long l2 = new PlccCommands.MstTransmit.Response((TACommandResponse)tACommandResponse).mRetVal.return_code.get() LCMP 0L;
                    bl = false;
                    if (l2 == false) {
                        bl = true;
                    }
                    if (DEBUG) {
                        Log.d(TAG, "mstTransmit: " + bl);
                    }
                }
                return bl;
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
                if (exception instanceof IllegalArgumentException) {
                    throw new PlccTAException("Invalid Input", 1004);
                }
                throw new PlccTAException("Error communicating with the TA", 1001);
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] retrieveFromStorage(byte[] arrby) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            TACommandResponse tACommandResponse;
            block7 : {
                byte[] arrby2 = null;
                tACommandResponse = this.executeNoLoad(new PlccCommands.RetrieveFromStorage.Request(arrby));
                if (tACommandResponse != null) break block7;
                Log.e(TAG, "Error: retrieveFromStorage executeNoLoad failed");
                return arrby2;
            }
            try {
                byte[] arrby3 = new PlccCommands.RetrieveFromStorage.Response((TACommandResponse)tACommandResponse).mRetVal.buf_plcc_data.getData();
                return arrby3;
            }
            catch (Exception exception) {
                try {
                    Log.e(TAG, "Error: retrieveFromStorage failed");
                    Log.c(TAG, exception.getMessage(), exception);
                    if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                    throw new PlccTAException("Invalid Input", 1004);
                }
                catch (Throwable throwable) {
                    return null;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setPlccServerCerts(String string, byte[] arrby, byte[] arrby2, byte[] arrby3) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            Log.d(TAG, "setPlccServerCerts: cardBrand " + string);
            ServerCerts serverCerts = new ServerCerts();
            serverCerts.plcc_cert_sign = arrby;
            serverCerts.plcc_cert_enc = arrby2;
            serverCerts.plcc_cert_sub = arrby3;
            if (CARD_BRAND_PLCC.equals((Object)string)) {
                mServerCertsHash.put((Object)CARD_BRAND_PLCC, (Object)serverCerts);
            } else if (CARD_BRAND_LOYALTY.equals((Object)string)) {
                mServerCertsHash.put((Object)CARD_BRAND_LOYALTY, (Object)serverCerts);
            } else if (CARD_BRAND_GIFT.equals((Object)string)) {
                mServerCertsHash.put((Object)CARD_BRAND_GIFT, (Object)serverCerts);
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] storeData(byte[] arrby) {
        PlccTAController plccTAController = this;
        synchronized (plccTAController) {
            TACommandResponse tACommandResponse;
            block7 : {
                byte[] arrby2 = null;
                tACommandResponse = this.executeNoLoad(new PlccCommands.StoreData.Request(arrby));
                if (tACommandResponse != null) break block7;
                Log.e(TAG, "Error: storeData executeNoLoad failed");
                return arrby2;
            }
            try {
                byte[] arrby3 = new PlccCommands.StoreData.Response((TACommandResponse)tACommandResponse).mRetVal.buf_plcc_data.getData();
                return arrby3;
            }
            catch (Exception exception) {
                try {
                    Log.e(TAG, "Error: storeData failed");
                    Log.c(TAG, exception.getMessage(), exception);
                    if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                    throw new PlccTAException("Invalid Input", 1004);
                }
                catch (Throwable throwable) {
                    return null;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public byte[] utility_enc4Server_Transport(String string, byte[] arrby, long l2) {
        byte[] arrby2;
        block12 : {
            arrby2 = null;
            PlccTAController plccTAController = this;
            // MONITORENTER : plccTAController
            if (DEBUG) {
                Log.d(TAG, "Calling utility_enc4Server_Transport");
            }
            if (!this.isTALoaded()) {
                Log.e(TAG, "utility_enc4Server_Transport: Error: TA is not loaded, please call loadTA() API first!");
                return arrby2;
            }
            if (!this.loadAllCerts(string)) {
                Log.e(TAG, "utility_enc4Server_Transport: Error: loadAllCerts returned false!");
                return null;
            }
            byte[] arrby3 = PlccTAController.getCurrentTimestamp(l2);
            if (arrby3 != null && DEBUG) {
                Log.d(TAG, "Utility_enc4Server_Transport: timestamp_bytes = " + Arrays.toString((byte[])arrby3));
            }
            try {
                TACommandResponse tACommandResponse = this.executeNoLoad(new PlccCommands.Utility_enc4Server_Transport.Request(arrby, arrby3));
                if (tACommandResponse == null) {
                    Log.e(TAG, "Utility_enc4Server_Transport: Error: executeNoLoad failed");
                    return null;
                }
                PlccCommands.Utility_enc4Server_Transport.Response response = new PlccCommands.Utility_enc4Server_Transport.Response(tACommandResponse);
                if (DEBUG) {
                    Log.d(TAG, "Utility_enc4Server_Transport called Successfully");
                }
                if ((arrby2 = response.mRetVal.resp.getData()) == null || !DEBUG) break block12;
                Log.d(TAG, "utility_enc4Server_Transport: encryptedData length = " + arrby2.length);
                return arrby2;
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
                if (!(exception instanceof IllegalArgumentException)) throw new PlccTAException("Error communicating with the TA", 1001);
                throw new PlccTAException("Invalid Input", 1004);
            }
        }
        // MONITOREXIT : plccTAController
        return arrby2;
    }

    private static class ServerCerts {
        private byte[] plcc_cert_enc = null;
        private byte[] plcc_cert_sign = null;
        private byte[] plcc_cert_sub = null;

        private ServerCerts() {
        }
    }

    public static class TACerts {
        public byte[] drk;
        public byte[] encryptcert;
        public byte[] signcert;

        TACerts(byte[] arrby, byte[] arrby2, byte[] arrby3) {
            this.drk = arrby;
            this.signcert = arrby2;
            this.encryptcert = arrby3;
        }
    }

}

