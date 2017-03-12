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
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.ExtractGlobalMembershipCardDetailResult;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipTAController;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.a */
public class GlobalMembershipPayProvider extends PaymentNetworkProvider {
    private static ProviderTokenKey pj;
    private static GlobalMembershipTAController zi;
    private byte[] zj;

    static {
        zi = null;
        pj = null;
    }

    public GlobalMembershipPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.zj = null;
        this.mContext = context;
        this.mTAController = GlobalMembershipTAController.m1066F(this.mContext);
        zi = (GlobalMembershipTAController) this.mTAController;
    }

    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
            globalMembershipCardRegisterResponseData.setErrorCode(-1);
        } else {
            try {
                if (globalMembershipCardRegisterRequestData.allServerCertsNotNull()) {
                    zi.m1068b(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getServerVerCert(), globalMembershipCardRegisterRequestData.getServerEncCert(), globalMembershipCardRegisterRequestData.getServerCaCert());
                }
                GlobalMembershipTAController.GlobalMembershipTAController aR = zi.aR(globalMembershipCardRegisterRequestData.getPartnerId());
                if (aR != null) {
                    globalMembershipCardRegisterResponseData.setDeviceSignCert(aR.signcert);
                    globalMembershipCardRegisterResponseData.setDeviceEncryptCert(aR.encryptcert);
                    globalMembershipCardRegisterResponseData.setDeviceDrk(aR.drk);
                    long am = Utils.am(this.mContext);
                    Log.m285d("GlobalMembershipPayProvider", "Network Time = " + am);
                    if (globalMembershipCardRegisterRequestData.allServerCertsNotNull()) {
                        byte[] utility_enc4Server_Transport = zi.utility_enc4Server_Transport(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getGlobalMembershipCardData(), am);
                        if (utility_enc4Server_Transport != null) {
                            globalMembershipCardRegisterResponseData.setErrorCode(0);
                            globalMembershipCardRegisterResponseData.setGlobalMembershipCardEncryptedData(utility_enc4Server_Transport);
                        } else {
                            globalMembershipCardRegisterResponseData.setErrorCode(-1);
                        }
                    } else {
                        globalMembershipCardRegisterResponseData.setErrorCode(0);
                    }
                } else {
                    globalMembershipCardRegisterResponseData.setErrorCode(-1);
                }
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
                globalMembershipCardRegisterResponseData.setErrorCode(-1);
            }
        }
        return globalMembershipCardRegisterResponseData;
    }

    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
            globalMembershipCardRegisterResponseData.setErrorCode(-1);
        } else {
            try {
                if (globalMembershipCardRegisterRequestData.allServerCertsNotNull()) {
                    zi.m1068b(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getServerVerCert(), globalMembershipCardRegisterRequestData.getServerEncCert(), globalMembershipCardRegisterRequestData.getServerCaCert());
                }
                byte[] a = zi.m1067a(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getTokenId(), globalMembershipCardRegisterRequestData.getGlobalMembershipCardData());
                if (a != null) {
                    globalMembershipCardRegisterResponseData.setErrorCode(0);
                } else {
                    globalMembershipCardRegisterResponseData.setErrorCode(-1);
                }
                globalMembershipCardRegisterResponseData.setGlobalMembershipCardEncryptedData(a);
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
                globalMembershipCardRegisterResponseData.setErrorCode(-1);
            }
        }
        return globalMembershipCardRegisterResponseData;
    }

    public void setCardTzEncData(byte[] bArr) {
        this.zj = bArr;
    }

    public SelectCardResult selectCard() {
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
            return null;
        }
        SelectCardResult selectCardResult;
        try {
            selectCardResult = new SelectCardResult(getTaid(), zi.getNonce(32));
        } catch (Throwable e) {
            Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
            selectCardResult = null;
        }
        return selectCardResult;
    }

    public String getTaid() {
        return GlobalMembershipTAController.eB().getTAInfo().getTAId();
    }

    protected void clearCard() {
        this.zj = null;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        boolean z = false;
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
        } else {
            try {
                z = zi.mstTransmit(this.zj, i, bArr);
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("GlobalMembershipPayProvider", e2.getMessage(), e2);
            }
        }
        return z;
    }

    protected void stopMstPay(boolean z) {
        Log.m287i("GlobalMembershipPayProvider", "stopMstPay: start ");
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
            return;
        }
        try {
            zi.clearMstData();
        } catch (Throwable e) {
            Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
        }
        Log.m287i("GlobalMembershipPayProvider", "stopMstPay: end ");
    }

    protected List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetail(String[] strArr, byte[][] bArr) {
        List<GlobalMembershipCardDetail> arrayList = new ArrayList();
        for (int i = 0; i < strArr.length; i++) {
            arrayList.add(m1059c(strArr[i], bArr[i]));
        }
        return arrayList;
    }

    private GlobalMembershipCardDetail m1059c(String str, byte[] bArr) {
        GlobalMembershipCardDetail globalMembershipCardDetail = new GlobalMembershipCardDetail();
        globalMembershipCardDetail.setErrorCode(-1);
        globalMembershipCardDetail.setTokenId(str);
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
        } else {
            try {
                ExtractGlobalMembershipCardDetailResult f = zi.m1069f(str.getBytes(), bArr);
                if (f != null && f.getErrorCode() == 0) {
                    globalMembershipCardDetail.setCardnumber(f.getCardnumber());
                    globalMembershipCardDetail.setPin(f.getPin());
                    globalMembershipCardDetail.setBarcodeContent(f.getBarcodeContent());
                    globalMembershipCardDetail.setBarcodeType(f.getBarcodeType());
                    globalMembershipCardDetail.setNumericValue(f.getNumericValue());
                    globalMembershipCardDetail.setErrorCode(0);
                }
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("GlobalMembershipPayProvider", e2.getMessage(), e2);
            }
        }
        return globalMembershipCardDetail;
    }

    protected boolean authenticateTransaction(SecuredObject securedObject) {
        boolean z = false;
        if (zi == null) {
            Log.m286e("GlobalMembershipPayProvider", "TAController is null");
        } else {
            try {
                Log.m285d("GlobalMembershipPayProvider", "Calling Global Membership TA Controller Authenticate Transaction");
                z = zi.authenticateTransaction(securedObject.getSecureObjectData());
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipPayProvider", e.getMessage(), e);
            }
        }
        return z;
    }

    public boolean getPayReadyState() {
        return true;
    }

    protected boolean prepareMstPay() {
        return true;
    }

    protected void init() {
    }

    public void delete() {
    }

    protected CertificateInfo[] getDeviceCertificates() {
        return new CertificateInfo[0];
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        return false;
    }

    protected ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        return null;
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    protected ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        return null;
    }

    protected void interruptMstPay() {
    }

    public boolean prepareNfcPay() {
        return false;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        return new byte[0];
    }

    protected Bundle stopNfcPay(int i) {
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short) 1);
        return bundle;
    }

    protected ProviderRequestData getReplenishmentRequestData() {
        return null;
    }

    protected ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    protected ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    protected int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        return 0;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        return null;
    }

    protected void loadTA() {
        zi.loadTA();
        Log.m287i("GlobalMembershipPayProvider", "load real TA");
    }

    protected void unloadTA() {
        zi.unloadTA();
        Log.m287i("GlobalMembershipPayProvider", "unload real TA");
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }
}
