package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import android.content.Context;
import android.spay.CertInfo;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AddCard;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.AuthenticateTransaction;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.ExtractGlobalMembershipCardDetail;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Request;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.LoadCerts.Response;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipCommands.Utility_enc4Server_Transport;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c */
public class GlobalMembershipTAController extends TAController {
    private static final TimeZone GMT;
    public static TAInfo TA_INFO;
    private static Map<String, GlobalMembershipTAController> mServerCertsHash;
    private static GlobalMembershipTAController zw;
    private CertInfo certsInfoCache;
    private GlobalMembershipTAController zx;
    private GlobalMembershipDeviceCerts zy;

    /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c.a */
    private static class GlobalMembershipTAController {
        private byte[] zA;
        private byte[] zB;
        private byte[] zz;

        private GlobalMembershipTAController() {
            this.zz = null;
            this.zA = null;
            this.zB = null;
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c.b */
    public static class GlobalMembershipTAController {
        public byte[] drk;
        public byte[] encryptcert;
        public byte[] signcert;

        GlobalMembershipTAController(byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this.drk = bArr;
            this.signcert = bArr2;
            this.encryptcert = bArr3;
        }
    }

    static {
        GMT = TimeZone.getTimeZone("GMT");
        zw = null;
        TA_INFO = new GlobalMembershipTAInfo();
        mServerCertsHash = new HashMap();
    }

    private GlobalMembershipTAController(Context context) {
        super(context, TA_INFO);
        this.certsInfoCache = null;
        this.zx = null;
        this.zy = null;
    }

    protected boolean init() {
        if (super.init()) {
            this.zy = new GlobalMembershipDeviceCerts(this);
            return true;
        }
        Log.e("SpayFw_GMTAController", "Error: init failed");
        return false;
    }

    public static synchronized GlobalMembershipTAController m1066F(Context context) {
        GlobalMembershipTAController globalMembershipTAController;
        synchronized (GlobalMembershipTAController.class) {
            if (zw == null) {
                zw = new GlobalMembershipTAController(context);
            }
            globalMembershipTAController = zw;
        }
        return globalMembershipTAController;
    }

    public static synchronized GlobalMembershipTAController eB() {
        GlobalMembershipTAController globalMembershipTAController;
        synchronized (GlobalMembershipTAController.class) {
            globalMembershipTAController = zw;
        }
        return globalMembershipTAController;
    }

    public void m1068b(String str, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        GlobalMembershipTAController globalMembershipTAController = new GlobalMembershipTAController();
        globalMembershipTAController.zz = bArr;
        globalMembershipTAController.zA = bArr2;
        globalMembershipTAController.zB = bArr3;
        mServerCertsHash.put(str, globalMembershipTAController);
    }

    private boolean loadAllCerts(String str) {
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling loadAllCerts");
        }
        if (isTALoaded()) {
            if (!this.zy.isLoaded()) {
                Log.d("SpayFw_GMTAController", "mGlobalMembershipDeviceCerts is not loaded");
                if (!this.zy.load()) {
                    Log.e("SpayFw_GMTAController", "Error: GlobalMembership Device Certs Load failed");
                    return false;
                }
            }
            byte[] devicePrivateSignCert = this.zy.getDevicePrivateSignCert();
            byte[] devicePrivateEncryptionCert = this.zy.getDevicePrivateEncryptionCert();
            if (devicePrivateSignCert == null || devicePrivateEncryptionCert == null) {
                Log.e("SpayFw_GMTAController", "loadAllCerts: Error: Certificate Data is NULL");
                this.certsInfoCache = null;
                return false;
            }
            try {
                TACommandRequest request;
                GlobalMembershipTAController globalMembershipTAController = (GlobalMembershipTAController) mServerCertsHash.get(str);
                if (globalMembershipTAController != null) {
                    request = new Request(devicePrivateSignCert, devicePrivateEncryptionCert, globalMembershipTAController.zz, globalMembershipTAController.zA, globalMembershipTAController.zB);
                } else {
                    request = new Request(devicePrivateSignCert, devicePrivateEncryptionCert, null, null, null);
                }
                TACommandResponse executeNoLoad = executeNoLoad(request);
                if (executeNoLoad == null) {
                    Log.e("SpayFw_GMTAController", "loadAllCerts: Error: executeNoLoad failed");
                    return false;
                }
                Response response = new Response(executeNoLoad);
                this.zx = new GlobalMembershipTAController(response.zu.cert_drk.getData(), response.zu.cert_sign.getData(), response.zu.cert_encrypt.getData());
                if (DEBUG) {
                    Log.d("SpayFw_GMTAController", "enc_cert= " + Arrays.toString(response.zu.cert_encrypt.getData()));
                    Log.d("SpayFw_GMTAController", "sign_cert= " + Arrays.toString(response.zu.cert_sign.getData()));
                    Log.d("SpayFw_GMTAController", "drk= " + Arrays.toString(response.zu.cert_drk.getData()));
                    Log.d("SpayFw_GMTAController", "loadAllCerts called Successfully");
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new GlobalMembershipTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new GlobalMembershipTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        }
        Log.e("SpayFw_GMTAController", "loadAllCerts: Error: TA is not loaded, please call loadTA() API first!");
        return false;
    }

    public GlobalMembershipTAController aR(String str) {
        if (this.zx == null && !loadAllCerts(str)) {
            Log.e("SpayFw_GMTAController", "getCerts: Error: loadAllCerts returned false!");
        }
        return this.zx;
    }

    public synchronized ExtractGlobalMembershipCardDetailResult m1069f(byte[] bArr, byte[] bArr2) {
        ExtractGlobalMembershipCardDetailResult extractGlobalMembershipCardDetailResult;
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling extractGlobalMembershipCardDetail");
        }
        extractGlobalMembershipCardDetailResult = new ExtractGlobalMembershipCardDetailResult();
        extractGlobalMembershipCardDetailResult.setErrorCode(-1);
        long j = -1;
        try {
            TACommandResponse executeNoLoad = executeNoLoad(new ExtractGlobalMembershipCardDetail.Request(bArr, bArr2));
            if (executeNoLoad == null) {
                Log.e("SpayFw_GMTAController", "Error: extractGlobalMembershipCardDetail executeNoLoad failed");
            } else {
                ExtractGlobalMembershipCardDetail.Response response = new ExtractGlobalMembershipCardDetail.Response(executeNoLoad);
                if (!(response == null || response.zq == null || response.zq.return_code == null)) {
                    j = response.zq.return_code.get();
                }
                if (j == 0) {
                    if (DEBUG) {
                        Log.d("SpayFw_GMTAController", "extractGlobalMembershipCardDetail Success: ");
                    }
                    if (response.zq.cardnumber != null) {
                        extractGlobalMembershipCardDetailResult.setCardnumber(new String(response.zq.cardnumber.getData(), "UTF-8"));
                    }
                    if (response.zq.pin != null) {
                        extractGlobalMembershipCardDetailResult.setPin(new String(response.zq.pin.getData(), "UTF-8"));
                    }
                    if (response.zq.barcodecontent != null) {
                        extractGlobalMembershipCardDetailResult.setBarcodeContent(new String(response.zq.barcodecontent.getData(), "UTF-8"));
                    }
                    if (response.zq.zr != null) {
                        extractGlobalMembershipCardDetailResult.setBarcodeType(new String(response.zq.zr.getData(), "UTF-8"));
                    }
                    if (response.zq.zt != null) {
                        extractGlobalMembershipCardDetailResult.setNumericValue(new String(response.zq.zt.getData(), "UTF-8"));
                    }
                    extractGlobalMembershipCardDetailResult.setErrorCode(0);
                } else {
                    Log.e("SpayFw_GMTAController", "extractGlobalMembershipCardDetail Fail: respValue = " + j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IllegalArgumentException) {
                throw new GlobalMembershipTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
            }
            throw new GlobalMembershipTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
        }
        return extractGlobalMembershipCardDetailResult;
    }

    public byte[] getNonce(int i) {
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling getNonce");
        }
        if (!isTALoaded()) {
            Log.e("SpayFw_GMTAController", "getNonce: Error: TA is not loaded, please call loadTA() API first!");
        }
        return null;
    }

    public boolean authenticateTransaction(byte[] bArr) {
        boolean z = false;
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling authenticateTransaction");
        }
        if (isTALoaded()) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new AuthenticateTransaction.Request(bArr));
                if (executeNoLoad == null) {
                    Log.e("SpayFw_GMTAController", "Error: authenticateTransaction executeNoLoad failed");
                } else {
                    long j = new AuthenticateTransaction.Response(executeNoLoad).zo.auth_result.get();
                    if (j == 0) {
                        z = true;
                    }
                    if (DEBUG) {
                        Log.d("SpayFw_GMTAController", "authenticateTransaction: auth_result = " + j);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new GlobalMembershipTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new GlobalMembershipTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        }
        Log.e("SpayFw_GMTAController", "authenticateTransaction: Error: TA is not loaded, please call loadTA() API first!");
        return z;
    }

    public byte[] m1067a(String str, String str2, byte[] bArr) {
        byte[] bArr2 = null;
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling Global Membership add card");
        }
        if (!isTALoaded()) {
            Log.e("SpayFw_GMTAController", "addCard: Error: TA is not loaded, please call loadTA() API first!");
        } else if (loadAllCerts(str)) {
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new AddCard.Request(str2.getBytes(), bArr));
                if (executeNoLoad == null) {
                    Log.e("SpayFw_GMTAController", "add Card: Error: executeNoLoad failed");
                } else {
                    AddCard.Response response = new AddCard.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.d("SpayFw_GMTAController", "tzEnc data  = " + Arrays.toString(response.zm.zn.getData()));
                    }
                    bArr2 = response.zm.zn.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new GlobalMembershipTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new GlobalMembershipTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        } else {
            Log.e("SpayFw_GMTAController", "addCard: Error: loadAllCerts returned false!");
        }
        return bArr2;
    }

    public byte[] utility_enc4Server_Transport(String str, byte[] bArr, long j) {
        byte[] bArr2 = null;
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling utility_enc4Server_Transport");
        }
        if (!isTALoaded()) {
            Log.e("SpayFw_GMTAController", "utility_enc4Server_Transport: Error: TA is not loaded, please call loadTA() API first!");
        } else if (loadAllCerts(str)) {
            byte[] currentTimestamp = GlobalMembershipTAController.getCurrentTimestamp(j);
            if (currentTimestamp != null && DEBUG) {
                Log.d("SpayFw_GMTAController", "Utility_enc4Server_Transport: timestamp_bytes = " + Arrays.toString(currentTimestamp));
            }
            try {
                TACommandResponse executeNoLoad = executeNoLoad(new Utility_enc4Server_Transport.Request(bArr, currentTimestamp));
                if (executeNoLoad == null) {
                    Log.e("SpayFw_GMTAController", "Utility_enc4Server_Transport: Error: executeNoLoad failed");
                } else {
                    Utility_enc4Server_Transport.Response response = new Utility_enc4Server_Transport.Response(executeNoLoad);
                    if (DEBUG) {
                        Log.d("SpayFw_GMTAController", "Utility_enc4Server_Transport called Successfully");
                    }
                    bArr2 = response.zv.resp.getData();
                    if (bArr2 != null && DEBUG) {
                        Log.d("SpayFw_GMTAController", "utility_enc4Server_Transport: encryptedData length = " + bArr2.length);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IllegalArgumentException) {
                    throw new GlobalMembershipTAException("Invalid Input", SPayTUIException.ERR_INVALID_INPUT);
                }
                throw new GlobalMembershipTAException("Error communicating with the TA", SPayTUIException.ERR_TZ_COM_ERR);
            }
        } else {
            Log.e("SpayFw_GMTAController", "utility_enc4Server_Transport: Error: loadAllCerts returned false!");
        }
        return bArr2;
    }

    public synchronized boolean mstTransmit(byte[] bArr, int i, byte[] bArr2) {
        if (DEBUG) {
            com.samsung.android.spayfw.p002b.Log.m285d("SpayFw_GMTAController", "Calling mstTransmit");
        }
        return false;
    }

    public boolean clearMstData() {
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Calling clearMstData");
        }
        resetMstFlag();
        if (isTALoaded()) {
            return true;
        }
        Log.e("SpayFw_GMTAController", "clearMstData: Error: TA is not loaded, please call loadTA() API first!");
        throw new GlobalMembershipTAException("Global Membership TA not loaded", SPayTUIException.ERR_TZ_TA_NOT_AVAILABLE);
    }

    private static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        String toUpperCase = str.toUpperCase();
        int length = toUpperCase.length() / 2;
        char[] toCharArray = toUpperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (GlobalMembershipTAController.charToByte(toCharArray[i2 + 1]) | (GlobalMembershipTAController.charToByte(toCharArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte[] getCurrentTimestamp(long j) {
        String gmtTimestamp = GlobalMembershipTAController.gmtTimestamp(new Date(j));
        if (DEBUG) {
            Log.d("SpayFw_GMTAController", "Network timestamp in sdf  =" + gmtTimestamp);
        }
        return GlobalMembershipTAController.hexStringToBytes(gmtTimestamp);
    }

    private static String gmtTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(GMT);
        return simpleDateFormat.format(date);
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
