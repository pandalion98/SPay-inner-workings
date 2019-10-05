/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  com.google.gson.JsonObject
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.globalmembership;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardDetail;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipTAException;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;

import java.util.ArrayList;
import java.util.List;

public class a
extends PaymentNetworkProvider {
    private static f pj;
    private static com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c zi;
    private byte[] zj = null;

    static {
        zi = null;
        pj = null;
    }

    public a(Context context, String string, f f2) {
        super(context, string);
        this.mContext = context;
        this.mTAController = com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c.F(this.mContext);
        zi = (com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c)this.mTAController;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private GlobalMembershipCardDetail c(String string, byte[] arrby) {
        GlobalMembershipCardDetail globalMembershipCardDetail = new GlobalMembershipCardDetail();
        globalMembershipCardDetail.setErrorCode(-1);
        globalMembershipCardDetail.setTokenId(string);
        if (zi == null) {
            Log.e("GlobalMembershipPayProvider", "TAController is null");
            return globalMembershipCardDetail;
        }
        try {
            byte[] arrby2 = string.getBytes();
            com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.a a2 = zi.f(arrby2, arrby);
            if (a2 == null || a2.getErrorCode() != 0) return globalMembershipCardDetail;
            globalMembershipCardDetail.setCardnumber(a2.getCardnumber());
            globalMembershipCardDetail.setPin(a2.getPin());
            globalMembershipCardDetail.setBarcodeContent(a2.getBarcodeContent());
            globalMembershipCardDetail.setBarcodeType(a2.getBarcodeType());
            globalMembershipCardDetail.setNumericValue(a2.getNumericValue());
            globalMembershipCardDetail.setErrorCode(0);
            return globalMembershipCardDetail;
        }
        catch (InterruptedException interruptedException) {
            Log.c("GlobalMembershipPayProvider", interruptedException.getMessage(), interruptedException);
            return globalMembershipCardDetail;
        }
        catch (GlobalMembershipTAException globalMembershipTAException) {
            Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
            return globalMembershipCardDetail;
        }
    }

    @Override
    protected boolean authenticateTransaction(SecuredObject securedObject) {
        if (zi == null) {
            Log.e("GlobalMembershipPayProvider", "TAController is null");
            return false;
        }
        try {
            Log.d("GlobalMembershipPayProvider", "Calling Global Membership TA Controller Authenticate Transaction");
            boolean bl = zi.authenticateTransaction(securedObject.getSecureObjectData());
            return bl;
        }
        catch (GlobalMembershipTAException globalMembershipTAException) {
            Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
            return false;
        }
    }

    @Override
    protected void clearCard() {
        this.zj = null;
    }

    @Override
    protected e createToken(String string, c c2, int n2) {
        return null;
    }

    @Override
    public void delete() {
    }

    @Override
    protected List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetail(String[] arrstring, byte[][] arrby) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrayList.add((Object)this.c(arrstring[i2], arrby[i2]));
        }
        return arrayList;
    }

    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }

    @Override
    protected CertificateInfo[] getDeviceCertificates() {
        return new CertificateInfo[0];
    }

    @Override
    protected c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData;
        block7 : {
            block8 : {
                globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
                if (zi == null) {
                    Log.e("GlobalMembershipPayProvider", "TAController is null");
                    globalMembershipCardRegisterResponseData.setErrorCode(-1);
                    return globalMembershipCardRegisterResponseData;
                }
                try {
                    c.b b2;
                    if (globalMembershipCardRegisterRequestData.allServerCertsNotNull()) {
                        zi.b(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getServerVerCert(), globalMembershipCardRegisterRequestData.getServerEncCert(), globalMembershipCardRegisterRequestData.getServerCaCert());
                    }
                    if ((b2 = zi.aR(globalMembershipCardRegisterRequestData.getPartnerId())) != null) {
                        globalMembershipCardRegisterResponseData.setDeviceSignCert(b2.signcert);
                        globalMembershipCardRegisterResponseData.setDeviceEncryptCert(b2.encryptcert);
                        globalMembershipCardRegisterResponseData.setDeviceDrk(b2.drk);
                        long l2 = h.am(this.mContext);
                        Log.d("GlobalMembershipPayProvider", "Network Time = " + l2);
                        if (!globalMembershipCardRegisterRequestData.allServerCertsNotNull()) break block7;
                        byte[] arrby = zi.utility_enc4Server_Transport(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getGlobalMembershipCardData(), l2);
                        if (arrby != null) {
                            globalMembershipCardRegisterResponseData.setErrorCode(0);
                            globalMembershipCardRegisterResponseData.setGlobalMembershipCardEncryptedData(arrby);
                            return globalMembershipCardRegisterResponseData;
                        }
                        break block8;
                    }
                    globalMembershipCardRegisterResponseData.setErrorCode(-1);
                    return globalMembershipCardRegisterResponseData;
                }
                catch (GlobalMembershipTAException globalMembershipTAException) {
                    Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
                    globalMembershipCardRegisterResponseData.setErrorCode(-1);
                    return globalMembershipCardRegisterResponseData;
                }
            }
            globalMembershipCardRegisterResponseData.setErrorCode(-1);
            return globalMembershipCardRegisterResponseData;
        }
        globalMembershipCardRegisterResponseData.setErrorCode(0);
        return globalMembershipCardRegisterResponseData;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
        if (zi == null) {
            Log.e("GlobalMembershipPayProvider", "TAController is null");
            globalMembershipCardRegisterResponseData.setErrorCode(-1);
            return globalMembershipCardRegisterResponseData;
        }
        try {
            byte[] arrby;
            if (globalMembershipCardRegisterRequestData.allServerCertsNotNull()) {
                zi.b(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getServerVerCert(), globalMembershipCardRegisterRequestData.getServerEncCert(), globalMembershipCardRegisterRequestData.getServerCaCert());
            }
            if ((arrby = zi.a(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getTokenId(), globalMembershipCardRegisterRequestData.getGlobalMembershipCardData())) != null) {
                globalMembershipCardRegisterResponseData.setErrorCode(0);
            } else {
                globalMembershipCardRegisterResponseData.setErrorCode(-1);
            }
            globalMembershipCardRegisterResponseData.setGlobalMembershipCardEncryptedData(arrby);
            return globalMembershipCardRegisterResponseData;
        }
        catch (GlobalMembershipTAException globalMembershipTAException) {
            Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
            globalMembershipCardRegisterResponseData.setErrorCode(-1);
            return globalMembershipCardRegisterResponseData;
        }
    }

    @Override
    public boolean getPayReadyState() {
        return true;
    }

    @Override
    protected c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    @Override
    protected c getReplenishmentRequestData() {
        return null;
    }

    public String getTaid() {
        return com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c.eB().getTAInfo().getTAId();
    }

    @Override
    protected int getTransactionData(Bundle bundle, i i2) {
        return 0;
    }

    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        return new byte[0];
    }

    @Override
    protected void init() {
    }

    @Override
    protected void interruptMstPay() {
    }

    @Override
    protected void loadTA() {
        zi.loadTA();
        Log.i("GlobalMembershipPayProvider", "load real TA");
    }

    @Override
    protected boolean prepareMstPay() {
        return true;
    }

    @Override
    public boolean prepareNfcPay() {
        return false;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        return null;
    }

    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public SelectCardResult selectCard() {
        if (zi == null) {
            Log.e("GlobalMembershipPayProvider", "TAController is null");
            return null;
        }
        try {
            return new SelectCardResult(this.getTaid(), zi.getNonce(32));
        }
        catch (GlobalMembershipTAException globalMembershipTAException) {
            Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
            return null;
        }
    }

    @Override
    public void setCardTzEncData(byte[] arrby) {
        this.zj = arrby;
    }

    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        return false;
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        if (zi == null) {
            Log.e("GlobalMembershipPayProvider", "TAController is null");
            return false;
        }
        try {
            boolean bl = zi.mstTransmit(this.zj, n2, arrby);
            return bl;
        }
        catch (InterruptedException interruptedException) {
            Log.c("GlobalMembershipPayProvider", interruptedException.getMessage(), interruptedException);
            return false;
        }
        catch (GlobalMembershipTAException globalMembershipTAException) {
            Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void stopMstPay(boolean bl) {
        Log.i("GlobalMembershipPayProvider", "stopMstPay: start ");
        if (zi == null) {
            Log.e("GlobalMembershipPayProvider", "TAController is null");
            return;
        }
        try {
            zi.clearMstData();
        }
        catch (GlobalMembershipTAException globalMembershipTAException) {
            Log.c("GlobalMembershipPayProvider", globalMembershipTAException.getMessage(), (Throwable)((Object)globalMembershipTAException));
        }
        Log.i("GlobalMembershipPayProvider", "stopMstPay: end ");
    }

    @Override
    protected Bundle stopNfcPay(int n2) {
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short)1);
        return bundle;
    }

    @Override
    protected void unloadTA() {
        zi.unloadTA();
        Log.i("GlobalMembershipPayProvider", "unload real TA");
    }

    @Override
    protected e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }
}

