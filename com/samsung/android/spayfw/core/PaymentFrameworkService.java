package com.samsung.android.spayfw.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import com.samsung.android.analytics.AnalyticFrameworkService;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.CommonSpayResponse;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.IPaymentFramework.Stub;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.ServerRequest;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.hce.SPayHCEReceiver;
import com.samsung.android.spayfw.core.hce.SPayHCEService;
import com.samsung.android.spayfw.core.p005a.PaymentProcessor;
import com.samsung.android.spayfw.core.p005a.ServerRequestProcessor;
import com.samsung.android.spayfw.core.p005a.TokenManager;
import com.samsung.android.spayfw.fraud.FraudReceiver;
import com.samsung.android.spayfw.p001a.UserHandleAdapter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.TokenReplenishReceiver;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.sec.enterprise.knox.seams.SEAMS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentFrameworkService extends Service {
    private PaymentFrameworkHandler jB;
    private C0412a jT;
    private final Stub jU;
    private Context mContext;

    /* renamed from: com.samsung.android.spayfw.core.PaymentFrameworkService.1 */
    class C04111 extends Stub {
        final /* synthetic */ PaymentFrameworkService jV;

        /* renamed from: com.samsung.android.spayfw.core.PaymentFrameworkService.1.1 */
        class C04101 extends Thread {
            final /* synthetic */ ServerRequest jW;
            final /* synthetic */ IServerResponseCallback jX;
            final /* synthetic */ C04111 jY;

            C04101(C04111 c04111, ServerRequest serverRequest, IServerResponseCallback iServerResponseCallback) {
                this.jY = c04111;
                this.jW = serverRequest;
                this.jX = iServerResponseCallback;
            }

            public void run() {
                try {
                    ServerRequestProcessor.m507u(this.jY.jV.mContext).m509a(this.jW, this.jX);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        C04111(PaymentFrameworkService paymentFrameworkService) {
            this.jV = paymentFrameworkService;
        }

        public int enrollCard(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
            Log.m287i("PaymentFrameworkService", "Entered method = enrollCard");
            if (!this.jV.m327o("enrollCard")) {
                Log.m286e("PaymentFrameworkService", "enrollCard:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "enrollCard:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                String a = Utils.m1273a(this.jV.mContext, Binder.getCallingPid());
                if (enrollCardInfo != null) {
                    enrollCardInfo.setApplicationId(a);
                }
                Message a2 = PaymentFrameworkMessage.m621a(1, enrollCardInfo, billingInfo, iEnrollCardCallback);
                if (!(enrollCardInfo == null || enrollCardInfo.getExtraEnrollData() == null)) {
                    PaymentFrameworkHandler a3 = this.jV.m323a(enrollCardInfo.getExtraEnrollData());
                    if (a3 != null) {
                        Log.m285d("PaymentFrameworkService", "send to restore handler");
                        a3.m618a(a2);
                        return 0;
                    }
                }
                Log.m285d("PaymentFrameworkService", "send to main handler");
                this.jV.jB.m618a(a2);
                return 0;
            }
        }

        public int acceptTnC(String str, boolean z, ICommonCallback iCommonCallback) {
            Log.m287i("PaymentFrameworkService", "Entered method = acceptTnC");
            if (!this.jV.m327o("acceptTnC")) {
                Log.m286e("PaymentFrameworkService", "acceptTnC:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "acceptTnC:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(2, str, Boolean.valueOf(z), iCommonCallback));
                return 0;
            }
        }

        public int provisionToken(String str, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback) {
            Log.m287i("PaymentFrameworkService", "Entered method = provisionToken");
            if (!this.jV.m327o("provisionToken")) {
                Log.m286e("PaymentFrameworkService", "provisionToken:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "provisionToken:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                Message a = PaymentFrameworkMessage.m621a(3, str, provisionTokenInfo, iProvisionTokenCallback);
                if (!(provisionTokenInfo == null || provisionTokenInfo.getActivationParamsBundle() == null)) {
                    PaymentFrameworkHandler a2 = this.jV.m323a(provisionTokenInfo.getActivationParamsBundle());
                    if (a2 != null) {
                        Log.m285d("PaymentFrameworkService", "send to restore handler");
                        a2.m618a(a);
                        return 0;
                    }
                }
                Log.m285d("PaymentFrameworkService", "send to handler ");
                this.jV.jB.m618a(a);
                return 0;
            }
        }

        public TokenStatus getTokenStatus(String str) {
            Log.m287i("PaymentFrameworkService", "Entered method = getTokenStatus");
            if (!this.jV.m327o("getTokenStatus")) {
                Log.m286e("PaymentFrameworkService", "getTokenStatus:permission denied");
                return null;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return new TokenManager(this.jV.mContext).getTokenStatus(str);
            } else {
                Log.m286e("PaymentFrameworkService", "getTokenStatus:TAException");
                return null;
            }
        }

        public int selectIdv(String str, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback) {
            Log.m287i("PaymentFrameworkService", "Entered method = selectIdv");
            if (!this.jV.m327o("selectIdv")) {
                Log.m286e("PaymentFrameworkService", "selectIdv:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "selectIdv:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(4, str, idvMethod, iSelectIdvCallback));
                return 0;
            }
        }

        public int verifyIdv(String str, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = verifyIdv");
            if (!this.jV.m327o("verifyIdv")) {
                Log.m286e("PaymentFrameworkService", "verifyIdv:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "verifyIdv:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(5, str, verifyIdvInfo, iVerifyIdvCallback));
                return 0;
            }
        }

        public int selectCard(String str, ISelectCardCallback iSelectCardCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = selectCard");
            if (!this.jV.m327o("selectCard")) {
                Log.m286e("PaymentFrameworkService", "selectCard:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "selectCard:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                try {
                    MstConfigurationManager.m604j(this.jV.mContext).m605A(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(6, str, iSelectCardCallback));
                return 0;
            }
        }

        public int startPay(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  startPay()");
            if (!this.jV.m327o("startPay")) {
                Log.m286e("PaymentFrameworkService", "startPay:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "startPay:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(7, securedObject, payConfig, iPayCallback));
                return 0;
            }
        }

        public int stopPay(ICommonCallback iCommonCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = stopPay()");
            if (!this.jV.m327o("stopPay")) {
                Log.m286e("PaymentFrameworkService", "stopPay:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "stopPay:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(8, null, iCommonCallback));
                return 0;
            }
        }

        public int processPushMessage(PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
            Log.m285d("PaymentFrameworkService", "processPushMessage()");
            if (!this.jV.m327o("processPushMessage")) {
                Log.m286e("PaymentFrameworkService", "processPushMessage:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "processPushMessage:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(9, pushMessage, Integer.valueOf(1), iPushMessageCallback));
                return 0;
            }
        }

        public void clearCard() {
            Log.m285d("PaymentFrameworkService", "clearCard()");
            if (!this.jV.m327o("clearCard")) {
                Log.m286e("PaymentFrameworkService", "clearCard:permission denied");
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "clearCard:TAException");
            } else {
                try {
                    MstConfigurationManager.m604j(this.jV.mContext).ap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(10, null, null));
            }
        }

        public int notifyDeviceReset(ICommonCallback iCommonCallback) {
            return reset(BuildConfig.FLAVOR, iCommonCallback);
        }

        public int getCardAttributes(String str, boolean z, ICardAttributeCallback iCardAttributeCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = getCardAttributes");
            if (!this.jV.m327o("getCardAttributes")) {
                Log.m286e("PaymentFrameworkService", "getCardAttributes:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "getCardAttributes:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(14, str, Boolean.valueOf(z), iCardAttributeCallback));
                return 0;
            }
        }

        public int setJwtToken(String str) {
            Log.m287i("PaymentFrameworkService", "Entered method = setJwtToken");
            if (!this.jV.m327o("setJwtToken")) {
                Log.m286e("PaymentFrameworkService", "not enough permission.throwing security exception");
                return -40;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return ConfigurationManager.m581h(this.jV.mContext).setConfig(PaymentFramework.CONFIG_JWT_TOKEN, str);
            } else {
                Log.m286e("PaymentFrameworkService", "setJwtToken:TAException");
                return PaymentFrameworkApp.ay();
            }
        }

        public int reset(String str, ICommonCallback iCommonCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = reset : " + str);
            if (this.jV.m327o("reset")) {
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(13, str, iCommonCallback));
                return 0;
            }
            Log.m286e("PaymentFrameworkService", "reset:permission denied");
            return -40;
        }

        public List<CardState> getAllCardState(Bundle bundle) {
            Log.m285d("PaymentFrameworkService", "getAllTokenState : ");
            if (!this.jV.m327o("getAllTokenState")) {
                Log.m286e("PaymentFrameworkService", "getAllCardState:permission denied");
                return null;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return new TokenManager(this.jV.mContext).getAllCardState(bundle);
            } else {
                Log.m286e("PaymentFrameworkService", "getAllCardState:TAException");
                return null;
            }
        }

        public int clearEnrolledCard(String str) {
            Log.m285d("PaymentFrameworkService", "clearEnrolledCard : " + str);
            if (!this.jV.m327o("clearEnrolledCard")) {
                Log.m286e("PaymentFrameworkService", "clearEnrolledCard:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return new TokenManager(this.jV.mContext).clearEnrolledCard(str);
            } else {
                Log.m286e("PaymentFrameworkService", "clearEnrolledCard:TAException");
                return PaymentFrameworkApp.ay();
            }
        }

        public List<String> getPaymentReadyState(String str) {
            Log.m285d("PaymentFrameworkService", "getPaymentReadyState : ");
            if (!this.jV.m327o("getPaymentReadyState")) {
                Log.m286e("PaymentFrameworkService", "getPaymentReadyState:permission denied");
                return null;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return new TokenManager(this.jV.mContext).getPaymentReadyState(str);
            } else {
                Log.m286e("PaymentFrameworkService", "getPaymentReadyState:TAException");
                return null;
            }
        }

        public int getCardData(String str, ICardDataCallback iCardDataCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = getTokenData : " + str);
            if (!this.jV.m327o("getTokenData")) {
                Log.m286e("PaymentFrameworkService", "getCardData:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "getCardData:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(20, str, iCardDataCallback));
                return 0;
            }
        }

        public int deleteCard(String str, Bundle bundle, IDeleteCardCallback iDeleteCardCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method = deleteCard : " + str);
            if (!this.jV.m327o("deleteCard")) {
                Log.m286e("PaymentFrameworkService", "deleteCard:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "deleteCard:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(22, str, bundle, iDeleteCardCallback));
                return 0;
            }
        }

        public int synchronizeCards(String str, ISynchronizeCardsCallback iSynchronizeCardsCallback) {
            return 0;
        }

        public int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  startInAppPay()");
            if (!this.jV.m327o("startInAppPay")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "startInAppPay:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m621a(24, securedObject, inAppTransactionInfo, iInAppPayCallback));
            return 0;
        }

        public int getLogs(ParcelFileDescriptor parcelFileDescriptor, String str, ICommonCallback iCommonCallback) {
            if (parcelFileDescriptor == null || iCommonCallback == null || str == null) {
                return -5;
            }
            if (!this.jV.m327o("getLogs")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "getLogs:TAException");
                return PaymentFrameworkApp.ay();
            }
            try {
                new SecurityManager().checkWrite(parcelFileDescriptor.getFileDescriptor());
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(25, parcelFileDescriptor, str, iCommonCallback));
                return 0;
            } catch (SecurityException e) {
                Log.m286e("PaymentFrameworkService", "can't write to file descriptor");
                return -5;
            }
        }

        public int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getGiftCardRegisterData()");
            if (!this.jV.m327o("getGiftCardRegisterData")) {
                return -40;
            }
            Message a = PaymentFrameworkMessage.m620a(27, giftCardRegisterRequestData, iGiftCardRegisterCallback);
            if (!(giftCardRegisterRequestData == null || giftCardRegisterRequestData.getExtraData() == null)) {
                PaymentFrameworkHandler a2 = this.jV.m323a(giftCardRegisterRequestData.getExtraData());
                if (a2 != null) {
                    Log.m285d("PaymentFrameworkService", "send to restore handler");
                    a2.m618a(a);
                    return 0;
                }
            }
            this.jV.jB.m618a(a);
            return 0;
        }

        public int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getGiftCardTzEncData()");
            if (!this.jV.m327o("getGiftCardTzEncData")) {
                return -40;
            }
            Message a = PaymentFrameworkMessage.m620a(28, giftCardRegisterRequestData, iGiftCardRegisterCallback);
            if (!(giftCardRegisterRequestData == null || giftCardRegisterRequestData.getExtraData() == null)) {
                PaymentFrameworkHandler a2 = this.jV.m323a(giftCardRegisterRequestData.getExtraData());
                if (a2 != null) {
                    Log.m285d("PaymentFrameworkService", "send to restore handler");
                    a2.m618a(a);
                    return 0;
                }
            }
            this.jV.jB.m618a(a);
            return 0;
        }

        public int startGiftCardPay(byte[] bArr, byte[] bArr2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  startGiftCardPay()");
            if (this.jV.m327o("startGiftCardPay")) {
                this.jV.jB.m618a(PaymentFrameworkMessage.m623a(29, bArr, bArr2, securedObject, payConfig, iPayCallback));
                return 0;
            }
            Log.m286e("PaymentFrameworkService", "startGiftCardPay:permission denied");
            return -40;
        }

        public int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  extractGiftCardDetail()");
            if (this.jV.m327o("extractGiftCardDetail")) {
                try {
                    MstConfigurationManager.m604j(this.jV.mContext).m605A(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(30, extractGiftCardDetailRequest, securedObject, iGiftCardExtractDetailCallback));
                return 0;
            }
            Log.m286e("PaymentFrameworkService", "extractGiftCardDetail:permission denied");
            return -40;
        }

        public int retryPay(PayConfig payConfig) {
            Log.m285d("PaymentFrameworkService", "Entered method =  retryPay()");
            if (!this.jV.m327o("retryPay")) {
                Log.m286e("PaymentFrameworkService", "retryPay:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return PaymentProcessor.m470q(this.jV.mContext).retryPay(payConfig);
            } else {
                Log.m286e("PaymentFrameworkService", "retryPay:TAException");
                return PaymentFrameworkApp.ay();
            }
        }

        public int getEnvironment(ICommonCallback iCommonCallback) {
            this.jV.jB.m618a(PaymentFrameworkMessage.m620a(26, null, iCommonCallback));
            return 0;
        }

        public List<String> isDsrpBlobMissing() {
            Log.m285d("PaymentFrameworkService", "isDsrpBlobMissing : ");
            List<String> arrayList = new ArrayList();
            if (!this.jV.m327o("isDsrpBlobMissing")) {
                Log.m286e("PaymentFrameworkService", "isDsrpBlobMissing:permission denied");
                return arrayList;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return new TokenManager(this.jV.mContext).isDsrpBlobMissing();
            } else {
                Log.m286e("PaymentFrameworkService", "isDsrpBlobMissing:TAException");
                return arrayList;
            }
        }

        public int isDsrpBlobMissingForTokenId(String str) {
            Log.m285d("PaymentFrameworkService", "isDsrpBlobMissing : ");
            if (!this.jV.m327o("isDsrpBlobMissing")) {
                Log.m286e("PaymentFrameworkService", "isDsrpBlobMissing:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() == 0) {
                return new TokenManager(this.jV.mContext).isDsrpBlobMissingForTokenId(str);
            } else {
                Log.m286e("PaymentFrameworkService", "isDsrpBlobMissing:TAException");
                return PaymentFrameworkApp.ay();
            }
        }

        public int storeUserSignature(String str, byte[] bArr, ICommonCallback iCommonCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  storeUserSignature()");
            if (!this.jV.m327o("storeUserSignature")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "storeUserSignature:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m621a(32, str, bArr, iCommonCallback));
            return 0;
        }

        public int getUserSignature(String str, IUserSignatureCallback iUserSignatureCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getUserSignature()");
            if (!this.jV.m327o("getUserSignature")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "getUserSignature:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m620a(31, str, iUserSignatureCallback));
            return 0;
        }

        public int setConfig(String str, String str2) {
            Log.m285d("PaymentFrameworkService", "Entered method =  setConfig()");
            if (this.jV.m327o("setConfig")) {
                return ConfigurationManager.m581h(this.jV.mContext).setConfig(str, str2);
            }
            return -40;
        }

        public String getConfig(String str) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getConfig()");
            if (this.jV.m327o("getConfig")) {
                return ConfigurationManager.m581h(this.jV.mContext).getConfig(str);
            }
            return String.valueOf(-40);
        }

        public int processServerRequest(ServerRequest serverRequest, IServerResponseCallback iServerResponseCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  processServerRequest()");
            if (!this.jV.m327o("processServerRequest")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "processServerRequest:TAException");
                return PaymentFrameworkApp.ay();
            }
            new C04101(this, serverRequest, iServerResponseCallback).start();
            return 0;
        }

        public int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  updateLoyaltyCard()");
            if (!this.jV.m327o("updateLoyaltyCard")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "updateLoyaltyCard:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m620a(34, updateLoyaltyCardInfo, iUpdateLoyaltyCardCallback));
            return 0;
        }

        public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback iExtractLoyaltyCardDetailResponseCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  extractLoyaltyCardDetails()");
            if (!this.jV.m327o("extractLoyaltyCardDetails")) {
                Log.m286e("PaymentFrameworkService", "extractLoyaltyCardDetails:CallerPermission failed");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "extractLoyaltyCardDetails:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                try {
                    MstConfigurationManager.m604j(this.jV.mContext).m605A(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(35, extractLoyaltyCardDetailRequest, iExtractLoyaltyCardDetailResponseCallback));
                return 0;
            }
        }

        public int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  storeUserSignature()");
            if (!this.jV.m327o("storeUserSignature")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "storeUserSignature:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m620a(36, loyaltyCardShowRequest, iPayCallback));
            return 0;
        }

        public int refreshIdv(String str, IRefreshIdvCallback iRefreshIdvCallback) {
            Log.m287i("PaymentFrameworkService", "Entered method = refreshIdv");
            if (!this.jV.m327o("refreshIdv")) {
                Log.m286e("PaymentFrameworkService", "refreshIdv:permission denied");
                return -40;
            } else if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "refreshIdv:TAException");
                return PaymentFrameworkApp.ay();
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(38, str, iRefreshIdvCallback));
                return 0;
            }
        }

        public int getTransactionDetails(String str, long j, long j2, int i, ITransactionDetailsCallback iTransactionDetailsCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getTransactionDetails()");
            if (!this.jV.m327o("getTransactionDetails")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "getTransactionDetails:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m623a(37, str, Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i), iTransactionDetailsCallback));
            return 0;
        }

        public int setPin(String str, char[] cArr, ICommonCallback iCommonCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  setPin()");
            if (!this.jV.m327o("setPin")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("PaymentFrameworkService", "setPin:TAException");
                return PaymentFrameworkApp.ay();
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m621a(39, str, cArr, iCommonCallback));
            return 0;
        }

        public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getGlobalMembershipCardRegisterData()");
            if (!this.jV.m327o("getGlobalMembershipCardRegisterData")) {
                return -40;
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m620a(40, globalMembershipCardRegisterRequestData, iGlobalMembershipCardRegisterCallback));
            return 0;
        }

        public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  getGlobalMembershipCardTzEncData()");
            if (!this.jV.m327o("getGlobalMembershipCardTzEncData")) {
                return -40;
            }
            this.jV.jB.m618a(PaymentFrameworkMessage.m620a(41, globalMembershipCardRegisterRequestData, iGlobalMembershipCardRegisterCallback));
            return 0;
        }

        public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, IGlobalMembershipCardExtractDetailCallback iGlobalMembershipCardExtractDetailCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  extractGlobalMembershipCardDetail()");
            if (this.jV.m327o("extractGlobalMembershipCardDetail")) {
                try {
                    MstConfigurationManager.m604j(this.jV.mContext).m605A(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.jV.jB.m618a(PaymentFrameworkMessage.m620a(43, list, iGlobalMembershipCardExtractDetailCallback));
                return 0;
            }
            Log.m286e("PaymentFrameworkService", "extractGlobalMembershipCardDetail:permission denied");
            return -40;
        }

        public int startGlobalMembershipCardPay(String str, byte[] bArr, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  startGlobalMembershipCardPay()");
            if (this.jV.m327o("startGlobalMembershipCardPay")) {
                this.jV.jB.m618a(PaymentFrameworkMessage.m623a(42, str, bArr, securedObject, payConfig, iPayCallback));
                return 0;
            }
            Log.m286e("PaymentFrameworkService", "startGlobalMembershipCardPay:permission denied");
            return -40;
        }

        public int updateBinAttribute(String str, String str2, ICommonCallback iCommonCallback) {
            Log.m285d("PaymentFrameworkService", "Entered method =  updateBinAttribute()");
            if (!this.jV.m327o("updateBinAttribute")) {
                Log.m286e("PaymentFrameworkService", "updateBinAttribute:permission denied");
                return -40;
            } else if (TextUtils.equals(BinAttribute.getServerBinVersion(), str)) {
                Log.m285d("PaymentFrameworkService", "updateBinAttribute. requested version is same. skip update BIN");
                return -3;
            } else {
                this.jV.jB.m618a(PaymentFrameworkMessage.m621a(44, str, str2, iCommonCallback));
                return 0;
            }
        }

        public CommonSpayResponse processSpayApdu(byte[] bArr, Bundle bundle) {
            CommonSpayResponse commonSpayResponse = new CommonSpayResponse();
            Log.m285d("PaymentFrameworkService", "Entered method =  processSpayApdu()");
            if (bArr != null) {
                Log.m285d("PaymentFrameworkService", "processSpayApdu data : " + Arrays.toString(bArr));
            }
            if (bundle != null) {
                Log.m285d("PaymentFrameworkService", "processSpayApdu extras size [" + bundle.size() + "]");
            }
            commonSpayResponse.setStatus(0);
            commonSpayResponse.setData(new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5});
            return commonSpayResponse;
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.PaymentFrameworkService.a */
    private class C0412a implements DeathRecipient {
        private IBinder iG;
        final /* synthetic */ PaymentFrameworkService jV;

        private C0412a(PaymentFrameworkService paymentFrameworkService) {
            this.jV = paymentFrameworkService;
        }

        public void m321a(IBinder iBinder) {
            this.iG = iBinder;
        }

        public void binderDied() {
            Log.m286e("PaymentFrameworkService", "DeathRecipient: Error: Wallet App died, handle clean up");
            if (this.iG != null) {
                this.iG.unlinkToDeath(this, 0);
            }
            PaymentFrameworkApp.az().aL();
            PaymentFrameworkApp.aA().aL();
        }
    }

    public PaymentFrameworkService() {
        this.jT = null;
        this.jU = new C04111(this);
    }

    public void onCreate() {
        super.onCreate();
        this.jT = new C0412a();
        this.jB = PaymentFrameworkApp.az();
        this.mContext = getApplicationContext();
    }

    public IBinder onBind(Intent intent) {
        int callingUserId = UserHandleAdapter.getCallingUserId();
        if (callingUserId != UserHandleAdapter.USER_OWNER) {
            Log.m286e("PaymentFrameworkService", "onBind: only app which runs on USER_OWNER has permission to bind: " + callingUserId);
            return null;
        }
        if (!PaymentFrameworkApp.aB().isReady()) {
            Log.m287i("PaymentFrameworkService", "PF not ready. requesting init ");
            if (PaymentFrameworkApp.aB().init()) {
                Log.m285d("PaymentFrameworkService", "PF init success ");
            } else {
                Log.m286e("PaymentFrameworkService", "PF init failed. Not return binder to caller ");
                return null;
            }
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            IBinder binder = extras.getBinder("deathDetectorBinder");
            if (binder != null) {
                Log.m285d("PaymentFrameworkService", "onBind: registering deathBinder : " + binder);
                try {
                    this.jT.m321a(binder);
                    binder.linkToDeath(this.jT, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        PackageStateReceiver.enable();
        FraudReceiver.enable();
        PFGenericReceiver.enable();
        UpdateReceiver.enable();
        if (!Utils.fR()) {
            SPayHCEService.enable();
            SPayHCEReceiver.enable();
            AnalyticFrameworkService.enable();
            TokenReplenishReceiver.enable();
        }
        return this.jU;
    }

    private boolean m327o(String str) {
        SEAMS instance = SEAMS.getInstance(getApplicationContext());
        String str2 = "PaymentFramework";
        if (instance == null) {
            Log.m286e("PaymentFrameworkService", "PaymentFrameworkService.checkCallerPermission(): SEAMS is null");
            return false;
        } else if (instance.isAuthorized(Binder.getCallingPid(), Binder.getCallingUid(), str2, str) == 0) {
            Log.m285d("PaymentFrameworkService", "PaymentFrameworkService.checkCallerPermission(): Access Granted");
            return true;
        } else {
            Log.m286e("PaymentFrameworkService", "PaymentFrameworkService.checkCallerPermission(): Access Denied");
            return false;
        }
    }

    private PaymentFrameworkHandler m323a(Bundle bundle) {
        String string = bundle != null ? bundle.getString(PaymentFramework.EXTRA_OPERATION_TYPE, null) : null;
        if (string == null) {
            Log.m285d("PaymentFrameworkService", "no operation type for handler");
            return null;
        } else if (string.equals(PaymentFramework.OPERATION_TYPE_RESTORE)) {
            PaymentFrameworkHandler aA = PaymentFrameworkApp.aA();
            Log.m285d("PaymentFrameworkService", "getRestoreHandler");
            return aA;
        } else {
            Log.m286e("PaymentFrameworkService", "no registered handler for " + string);
            return null;
        }
    }
}
