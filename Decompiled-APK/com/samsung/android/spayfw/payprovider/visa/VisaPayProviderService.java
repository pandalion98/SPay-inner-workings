package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.visa.transaction.Element;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.payprovider.visa.transaction.VisaPayTransactionData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.visa.tainterface.VisaTAController;
import com.visa.tainterface.VisaTAController.C0694a;
import com.visa.tainterface.VisaTAException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.d */
public class VisaPayProviderService {
    private VisaTAController zP;

    public VisaPayProviderService(Context context, VisaTAController visaTAController) {
        this.zP = visaTAController;
    }

    public String m1105i(Object obj) {
        return VisaTAController.MF;
    }

    public byte[] getNonce() {
        byte[] nonce;
        Throwable e;
        try {
            nonce = this.zP.getNonce(32);
            if (nonce == null) {
                try {
                    Log.m286e("VisaPayProviderService", "failure to get Nonce from TA");
                } catch (VisaTAException e2) {
                    e = e2;
                    Log.m284c("VisaPayProviderService", e.getMessage(), e);
                    return nonce;
                }
            }
        } catch (Throwable e3) {
            Throwable th = e3;
            nonce = null;
            e = th;
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            return nonce;
        }
        return nonce;
    }

    public boolean authenticateTransaction(SecuredObject securedObject) {
        boolean authenticateTransaction;
        try {
            authenticateTransaction = this.zP.authenticateTransaction(securedObject.getSecureObjectData());
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            authenticateTransaction = false;
        }
        if (!authenticateTransaction) {
            Log.m286e("VisaPayProviderService", "failure to do prepareSecureObjectForVTA");
        }
        return authenticateTransaction;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        boolean a;
        Log.m285d("VisaPayProviderService", "startMstPay: input config " + Arrays.toString(bArr));
        try {
            a = this.zP.m1710a(i, bArr);
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            a = false;
        }
        if (!a) {
            Log.m286e("VisaPayProviderService", "failure to do startMstPay");
        }
        return a;
    }

    public void stopMstPay() {
        Log.m287i("VisaPayProviderService", "clearMstData: start " + System.currentTimeMillis());
        try {
            this.zP.clearMstData();
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
        }
        Log.m287i("VisaPayProviderService", "clearMstData: end " + System.currentTimeMillis());
    }

    public CertificateInfo[] getCertificates() {
        Log.m285d("VisaPayProviderService", "getCertificates() called");
        try {
            C0694a is = this.zP.is();
            Log.m285d("VisaPayProviderService", "after vtacontroller.getAllCerts : " + is);
            if (is == null || is.drk == null || is.encryptcert == null || is.signcert == null) {
                return null;
            }
            CertificateInfo[] certificateInfoArr = new CertificateInfo[3];
            CertificateInfo certificateInfo = new CertificateInfo();
            certificateInfo.setAlias("token_encryption_cert");
            String convertToPem = Utils.convertToPem(is.encryptcert);
            Log.m285d("VisaPayProviderService", "getCertificates: tec = " + convertToPem);
            certificateInfo.setContent(convertToPem);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_ENC);
            certificateInfoArr[0] = certificateInfo;
            certificateInfo = new CertificateInfo();
            certificateInfo.setAlias("pan_verification_cert");
            convertToPem = Utils.convertToPem(is.signcert);
            Log.m285d("VisaPayProviderService", "getCertificates: pvc = " + convertToPem);
            certificateInfo.setContent(convertToPem);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_VER);
            certificateInfoArr[1] = certificateInfo;
            certificateInfo = new CertificateInfo();
            certificateInfo.setAlias("device_root_cert");
            String convertToPem2 = Utils.convertToPem(is.drk);
            Log.m285d("VisaPayProviderService", "getCertificates: drc = " + convertToPem2);
            certificateInfo.setContent(convertToPem2);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_CA);
            certificateInfoArr[2] = certificateInfo;
            return certificateInfoArr;
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            return null;
        }
    }

    public boolean m1102a(CertificateInfo[] certificateInfoArr) {
        byte[] bArr = null;
        if (certificateInfoArr == null || certificateInfoArr.length == 0) {
            Log.m286e("VisaPayProviderService", "setProviderCertificates invalid input");
            return false;
        }
        byte[] bArr2 = null;
        for (CertificateInfo certificateInfo : certificateInfoArr) {
            String content = certificateInfo.getContent();
            if (content == null || content.length() == 0) {
                Log.m286e("VisaPayProviderService", "cert received null or empty");
            } else {
                content = content.replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR);
                try {
                    if (certificateInfo.getUsage().equals(CertificateInfo.CERT_USAGE_ENC)) {
                        bArr2 = Base64.decode(content, 0);
                    } else if (certificateInfo.getUsage().equals(CertificateInfo.CERT_USAGE_SIG)) {
                        bArr = Base64.decode(content, 0);
                    }
                } catch (IllegalArgumentException e) {
                    Log.m286e("VisaPayProviderService", "base64 decode failed for certs received");
                }
            }
        }
        if (bArr2 == null || bArr == null) {
            Log.m286e("VisaPayProviderService", "setProviderCertificates invalid certs");
            return false;
        }
        Log.m285d("VisaPayProviderService", "processReceivedCerts: buf_cert_enc = " + Arrays.toString(bArr2));
        Log.m285d("VisaPayProviderService", "processReceivedCerts: buf_cert_sign = " + Arrays.toString(bArr));
        this.zP.m1716p(bArr, bArr2);
        return true;
    }

    public TransactionDetails m1101a(ProviderTokenKey providerTokenKey, Object obj) {
        Log.m285d("VisaPayProviderService", "processTransactionData()");
        VisaPayTransactionData visaPayTransactionData = (VisaPayTransactionData) obj;
        if (visaPayTransactionData == null || visaPayTransactionData.getElements() == null || visaPayTransactionData.getElements().isEmpty()) {
            Log.m286e("VisaPayProviderService", "processTransactionData: input data is null ");
            return null;
        }
        List arrayList = new ArrayList();
        for (int i = 0; i < visaPayTransactionData.getElements().size(); i++) {
            String encTransactionInfo = ((Element) visaPayTransactionData.getElements().get(i)).getEncTransactionInfo();
            if (encTransactionInfo == null) {
                Log.m290w("VisaPayProviderService", "processTransactionData: encTransactionInfo is null ");
            } else {
                byte[] retrieveFromStorage;
                Log.m285d("VisaPayProviderService", "processTransactionData: calling storeVtsData: ");
                try {
                    retrieveFromStorage = this.zP.retrieveFromStorage(this.zP.m1717q(encTransactionInfo.getBytes()));
                } catch (Throwable e) {
                    Log.m284c("VisaPayProviderService", e.getMessage(), e);
                    retrieveFromStorage = null;
                }
                if (retrieveFromStorage != null) {
                    String str = new String(retrieveFromStorage);
                    Log.m285d("VisaPayProviderService", "processTransactionData: decTxDataStr: " + str);
                    TransactionData a = m1100a((TransactionInfo) new Gson().fromJson(str, TransactionInfo.class));
                    if (a != null) {
                        arrayList.add(a);
                    }
                } else {
                    Log.m290w("VisaPayProviderService", "error occured while process transaction history");
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setTransactionData(arrayList);
        return transactionDetails;
    }

    private TransactionData m1100a(TransactionInfo transactionInfo) {
        TransactionData transactionData = null;
        if (transactionInfo != null) {
            transactionData = new TransactionData();
            transactionData.setAmount(transactionInfo.getAmount());
            transactionData.setCurrencyCode(transactionInfo.getCurrencyCode());
            transactionData.setMechantName(transactionInfo.getMerchantName());
            transactionData.setTransactionDate(transactionInfo.getTransactionDate());
            transactionData.setTransactionId(transactionInfo.getTransactionID());
            if (transactionInfo.getTransactionType().equals(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_PURCHASE);
            } else if (transactionInfo.getTransactionType().equals(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_REFUND);
            }
            if (transactionInfo.getTransactionStatus().equals(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND)) {
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_APPROVED);
            } else if (transactionInfo.getTransactionStatus().equals(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE)) {
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_PENDING);
            } else if (transactionInfo.getTransactionStatus().equals(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED)) {
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_REFUNDED);
            } else if (transactionInfo.getTransactionStatus().equals(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED)) {
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_DECLINED);
            }
            Bundle bundle = new Bundle();
            bundle.putString(TransactionData.INDUSTRYCATGCODE, transactionInfo.getIndustryCatgCode().toString());
            bundle.putString(TransactionData.INDUSTRYCATGNAME, transactionInfo.getIndustryCatgName());
            bundle.putString(TransactionData.INDUSTRYCODE, transactionInfo.getIndustryCode().toString());
            bundle.putString(TransactionData.INDUSTRYNAME, transactionInfo.getIndustryName());
            transactionData.setCustomData(bundle);
            Log.m285d("VisaPayProviderService", "processTransactionData: TransactionData: " + transactionData.toString());
        }
        return transactionData;
    }

    public void interruptMstPay() {
        Log.m285d("VisaPayProviderService", "interruptMstPay() Start : " + System.currentTimeMillis());
        this.zP.abortMstTransmission();
        Log.m285d("VisaPayProviderService", "interruptMstPay() End : " + System.currentTimeMillis());
    }

    public byte[] aY(String str) {
        byte[] b;
        Throwable e;
        if (str != null) {
            try {
                Object bytes = str.getBytes("utf-8");
                byte[] bArr = new byte[(bytes.length + 1)];
                bArr[0] = (byte) 21;
                System.arraycopy(bytes, 0, bArr, 1, bytes.length);
                b = this.zP.m1713b(bArr, false);
            } catch (VisaTAException e2) {
                e = e2;
                Log.m284c("VisaPayProviderService", e.getMessage(), e);
                b = null;
                if (b == null) {
                    Log.m285d("VisaPayProviderService", "encData: " + new String(b));
                } else {
                    Log.m285d("VisaPayProviderService", "encData: returns null");
                }
                return b;
            } catch (UnsupportedEncodingException e3) {
                e = e3;
                Log.m284c("VisaPayProviderService", e.getMessage(), e);
                b = null;
                if (b == null) {
                    Log.m285d("VisaPayProviderService", "encData: returns null");
                } else {
                    Log.m285d("VisaPayProviderService", "encData: " + new String(b));
                }
                return b;
            }
            if (b == null) {
                Log.m285d("VisaPayProviderService", "encData: " + new String(b));
            } else {
                Log.m285d("VisaPayProviderService", "encData: returns null");
            }
            return b;
        }
        b = null;
        if (b == null) {
            Log.m285d("VisaPayProviderService", "encData: returns null");
        } else {
            Log.m285d("VisaPayProviderService", "encData: " + new String(b));
        }
        return b;
    }

    public byte[] m1104b(String str, String str2, byte[] bArr) {
        Throwable e;
        if (str == null || str2 == null) {
            Log.m286e("VisaPayProviderService", "getInAppPayload input is null");
            return null;
        }
        byte[] e2;
        try {
            e2 = this.zP.m1715e(str.getBytes("utf-8"), str2.getBytes("utf-8"), bArr);
        } catch (VisaTAException e3) {
            e = e3;
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            e2 = null;
            if (e2 == null) {
                return e2;
            }
            Log.m285d("VisaPayProviderService", "encData: returns null");
            return e2;
        } catch (UnsupportedEncodingException e4) {
            e = e4;
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            e2 = null;
            if (e2 == null) {
                return e2;
            }
            Log.m285d("VisaPayProviderService", "encData: returns null");
            return e2;
        }
        if (e2 == null) {
            return e2;
        }
        Log.m285d("VisaPayProviderService", "encData: returns null");
        return e2;
    }

    public byte[] m1103a(String str, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        Throwable e;
        if (str == null || str2 == null || bArr2 == null || bArr3 == null) {
            Log.m286e("VisaPayProviderService", "getInAppPayloadJwe input is null");
            return null;
        }
        byte[] b;
        try {
            b = this.zP.m1714b(str.getBytes("utf-8"), str2.getBytes("utf-8"), bArr2, bArr3);
        } catch (VisaTAException e2) {
            e = e2;
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            b = null;
            if (b == null) {
                return b;
            }
            Log.m285d("VisaPayProviderService", "encData: returns null");
            return b;
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            Log.m284c("VisaPayProviderService", e.getMessage(), e);
            b = null;
            if (b == null) {
                return b;
            }
            Log.m285d("VisaPayProviderService", "encData: returns null");
            return b;
        }
        if (b == null) {
            return b;
        }
        Log.m285d("VisaPayProviderService", "encData: returns null");
        return b;
    }
}
