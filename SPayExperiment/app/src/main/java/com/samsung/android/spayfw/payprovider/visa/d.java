/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.util.Base64
 *  com.google.gson.Gson
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.visa.transaction.Element;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.payprovider.visa.transaction.VisaPayTransactionData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;
import com.visa.tainterface.VisaTAController;
import com.visa.tainterface.VisaTAException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class d {
    private VisaTAController zP;

    public d(Context context, VisaTAController visaTAController) {
        this.zP = visaTAController;
    }

    /*
     * Enabled aggressive block sorting
     */
    private TransactionData a(TransactionInfo transactionInfo) {
        TransactionData transactionData = null;
        if (transactionInfo != null) {
            transactionData = new TransactionData();
            transactionData.setAmount(transactionInfo.getAmount());
            transactionData.setCurrencyCode(transactionInfo.getCurrencyCode());
            transactionData.setMechantName(transactionInfo.getMerchantName());
            transactionData.setTransactionDate(transactionInfo.getTransactionDate());
            transactionData.setTransactionId(transactionInfo.getTransactionID());
            if (transactionInfo.getTransactionType().equals((Object)"0")) {
                transactionData.setTransactionType("Purchase");
            } else if (transactionInfo.getTransactionType().equals((Object)"1")) {
                transactionData.setTransactionType("Refund");
            }
            if (transactionInfo.getTransactionStatus().equals((Object)"1")) {
                transactionData.setTransactionStatus("Approved");
            } else if (transactionInfo.getTransactionStatus().equals((Object)"0")) {
                transactionData.setTransactionStatus("Pending");
            } else if (transactionInfo.getTransactionStatus().equals((Object)"2")) {
                transactionData.setTransactionStatus("Refunded");
            } else if (transactionInfo.getTransactionStatus().equals((Object)"3")) {
                transactionData.setTransactionStatus("Declined");
            }
            Bundle bundle = new Bundle();
            bundle.putString("industryCatgCode", transactionInfo.getIndustryCatgCode().toString());
            bundle.putString("industryCatgName", transactionInfo.getIndustryCatgName());
            bundle.putString("industryCode", transactionInfo.getIndustryCode().toString());
            bundle.putString("industryName", transactionInfo.getIndustryName());
            transactionData.setCustomData(bundle);
            c.d("VisaPayProviderService", "processTransactionData: TransactionData: " + transactionData.toString());
        }
        return transactionData;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public TransactionDetails a(f f2, Object object) {
        c.d("VisaPayProviderService", "processTransactionData()");
        VisaPayTransactionData visaPayTransactionData = (VisaPayTransactionData)object;
        if (visaPayTransactionData == null || visaPayTransactionData.getElements() == null || visaPayTransactionData.getElements().isEmpty()) {
            c.e("VisaPayProviderService", "processTransactionData: input data is null ");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < visaPayTransactionData.getElements().size(); ++i2) {
            byte[] arrby;
            String string = ((Element)visaPayTransactionData.getElements().get(i2)).getEncTransactionInfo();
            if (string == null) {
                c.w("VisaPayProviderService", "processTransactionData: encTransactionInfo is null ");
                continue;
            }
            c.d("VisaPayProviderService", "processTransactionData: calling storeVtsData: ");
            try {
                byte[] arrby2;
                byte[] arrby3 = this.zP.q(string.getBytes());
                arrby = arrby2 = this.zP.retrieveFromStorage(arrby3);
            }
            catch (VisaTAException visaTAException) {
                c.c("VisaPayProviderService", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
                arrby = null;
            }
            if (arrby != null) {
                String string2 = new String(arrby);
                c.d("VisaPayProviderService", "processTransactionData: decTxDataStr: " + string2);
                TransactionData transactionData = this.a((TransactionInfo)new Gson().fromJson(string2, TransactionInfo.class));
                if (transactionData == null) continue;
                arrayList.add((Object)transactionData);
                continue;
            }
            c.w("VisaPayProviderService", "error occured while process transaction history");
        }
        if (!arrayList.isEmpty()) {
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setTransactionData((List<TransactionData>)arrayList);
            return transactionDetails;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(CertificateInfo[] arrcertificateInfo) {
        byte[] arrby = null;
        if (arrcertificateInfo == null || arrcertificateInfo.length == 0) {
            c.e("VisaPayProviderService", "setProviderCertificates invalid input");
            return false;
        }
        int n2 = arrcertificateInfo.length;
        byte[] arrby2 = null;
        for (int i2 = 0; i2 < n2; ++i2) {
            CertificateInfo certificateInfo = arrcertificateInfo[i2];
            String string = certificateInfo.getContent();
            if (string == null || string.length() == 0) {
                c.e("VisaPayProviderService", "cert received null or empty");
                continue;
            }
            String string2 = string.replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"");
            try {
                byte[] arrby3;
                if (certificateInfo.getUsage().equals((Object)"ENC")) {
                    arrby2 = Base64.decode((String)string2, (int)0);
                    continue;
                }
                if (!certificateInfo.getUsage().equals((Object)"SIG")) continue;
                arrby = arrby3 = Base64.decode((String)string2, (int)0);
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                c.e("VisaPayProviderService", "base64 decode failed for certs received");
            }
        }
        if (arrby2 != null && arrby != null) {
            c.d("VisaPayProviderService", "processReceivedCerts: buf_cert_enc = " + Arrays.toString(arrby2));
            c.d("VisaPayProviderService", "processReceivedCerts: buf_cert_sign = " + Arrays.toString(arrby));
            this.zP.p(arrby, arrby2);
            return true;
        }
        c.e("VisaPayProviderService", "setProviderCertificates invalid certs");
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] a(String var1_1, String var2_2, byte[] var3_3, byte[] var4_4, byte[] var5_5) {
        if (var1_1 == null || var2_2 == null || var4_4 == null || var5_5 == null) {
            c.e("VisaPayProviderService", "getInAppPayloadJwe input is null");
            return null;
        }
        try {
            var8_7 = var1_1.getBytes("utf-8");
            var9_8 = var2_2.getBytes("utf-8");
            var6_6 = var10_9 = this.zP.b(var8_7, var9_8, var4_4, var5_5);
        }
        catch (VisaTAException var7_10) {}
        ** GOTO lbl-1000
        catch (UnsupportedEncodingException var7_12) {}
lbl-1000: // 2 sources:
        {
            c.c("VisaPayProviderService", var7_11.getMessage(), (Throwable)var7_11);
            var6_6 = null;
        }
        if (var6_6 != null) return var6_6;
        c.d("VisaPayProviderService", "encData: returns null");
        return var6_6;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] aY(String var1_1) {
        block4 : {
            block5 : {
                if (var1_1 == null) break block5;
                try {
                    var4_2 = var1_1.getBytes("utf-8");
                    var5_3 = new byte[1 + var4_2.length];
                    var5_3[0] = 21;
                    System.arraycopy((Object)var4_2, (int)0, (Object)var5_3, (int)1, (int)var4_2.length);
                    var2_5 = var6_4 = this.zP.b(var5_3, false);
                    break block4;
                }
                catch (VisaTAException var3_6) {}
                ** GOTO lbl-1000
                catch (UnsupportedEncodingException var3_8) {}
lbl-1000: // 2 sources:
                {
                    c.c("VisaPayProviderService", var3_7.getMessage(), (Throwable)var3_7);
                }
            }
            var2_5 = null;
        }
        if (var2_5 != null) {
            c.d("VisaPayProviderService", "encData: " + new String(var2_5));
            return var2_5;
        }
        c.d("VisaPayProviderService", "encData: returns null");
        return var2_5;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean authenticateTransaction(SecuredObject securedObject) {
        boolean bl;
        try {
            boolean bl2;
            bl = bl2 = this.zP.authenticateTransaction(securedObject.getSecureObjectData());
        }
        catch (VisaTAException visaTAException) {
            c.c("VisaPayProviderService", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
            bl = false;
        }
        if (!bl) {
            c.e("VisaPayProviderService", "failure to do prepareSecureObjectForVTA");
        }
        return bl;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] b(String var1_1, String var2_2, byte[] var3_3) {
        if (var1_1 == null || var2_2 == null) {
            c.e("VisaPayProviderService", "getInAppPayload input is null");
            return null;
        }
        try {
            var6_5 = var1_1.getBytes("utf-8");
            var7_6 = var2_2.getBytes("utf-8");
            var4_4 = var8_7 = this.zP.e(var6_5, var7_6, var3_3);
        }
        catch (VisaTAException var5_8) {}
        ** GOTO lbl-1000
        catch (UnsupportedEncodingException var5_10) {}
lbl-1000: // 2 sources:
        {
            c.c("VisaPayProviderService", var5_9.getMessage(), (Throwable)var5_9);
            var4_4 = null;
        }
        if (var4_4 != null) return var4_4;
        c.d("VisaPayProviderService", "encData: returns null");
        return var4_4;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public CertificateInfo[] getCertificates() {
        VisaTAController.a a2;
        c.d("VisaPayProviderService", "getCertificates() called");
        try {
            a2 = this.zP.is();
            c.d("VisaPayProviderService", "after vtacontroller.getAllCerts : " + a2);
            if (a2 == null) return null;
        }
        catch (VisaTAException visaTAException) {
            c.c("VisaPayProviderService", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
            return null;
        }
        if (a2.drk == null || a2.encryptcert == null || a2.signcert == null) return null;
        CertificateInfo[] arrcertificateInfo = new CertificateInfo[3];
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setAlias("token_encryption_cert");
        String string = h.convertToPem(a2.encryptcert);
        c.d("VisaPayProviderService", "getCertificates: tec = " + string);
        certificateInfo.setContent(string);
        certificateInfo.setUsage("ENC");
        arrcertificateInfo[0] = certificateInfo;
        CertificateInfo certificateInfo2 = new CertificateInfo();
        certificateInfo2.setAlias("pan_verification_cert");
        String string2 = h.convertToPem(a2.signcert);
        c.d("VisaPayProviderService", "getCertificates: pvc = " + string2);
        certificateInfo2.setContent(string2);
        certificateInfo2.setUsage("VER");
        arrcertificateInfo[1] = certificateInfo2;
        CertificateInfo certificateInfo3 = new CertificateInfo();
        certificateInfo3.setAlias("device_root_cert");
        String string3 = h.convertToPem(a2.drk);
        c.d("VisaPayProviderService", "getCertificates: drc = " + string3);
        certificateInfo3.setContent(string3);
        certificateInfo3.setUsage("CA");
        arrcertificateInfo[2] = certificateInfo3;
        return arrcertificateInfo;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] getNonce() {
        byte[] arrby;
        try {
            byte[] arrby2;
            arrby = arrby2 = this.zP.getNonce(32);
            if (arrby != null) return arrby;
        }
        catch (VisaTAException visaTAException) {
            arrby = null;
            VisaTAException visaTAException2 = visaTAException;
            c.c("VisaPayProviderService", visaTAException2.getMessage(), (Throwable)((Object)visaTAException2));
            return arrby;
        }
        c.e("VisaPayProviderService", "failure to get Nonce from TA");
        return arrby;
        {
            catch (VisaTAException visaTAException) {}
        }
    }

    public String i(Object object) {
        return VisaTAController.MF;
    }

    public void interruptMstPay() {
        c.d("VisaPayProviderService", "interruptMstPay() Start : " + System.currentTimeMillis());
        this.zP.abortMstTransmission();
        c.d("VisaPayProviderService", "interruptMstPay() End : " + System.currentTimeMillis());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean startMstPay(int n2, byte[] arrby) {
        boolean bl;
        c.d("VisaPayProviderService", "startMstPay: input config " + Arrays.toString((byte[])arrby));
        try {
            boolean bl2;
            bl = bl2 = this.zP.a(n2, arrby);
        }
        catch (VisaTAException visaTAException) {
            c.c("VisaPayProviderService", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
            bl = false;
        }
        if (!bl) {
            c.e("VisaPayProviderService", "failure to do startMstPay");
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void stopMstPay() {
        c.i("VisaPayProviderService", "clearMstData: start " + System.currentTimeMillis());
        try {
            this.zP.clearMstData();
        }
        catch (VisaTAException visaTAException) {
            c.c("VisaPayProviderService", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
        }
        c.i("VisaPayProviderService", "clearMstData: end " + System.currentTimeMillis());
    }
}

