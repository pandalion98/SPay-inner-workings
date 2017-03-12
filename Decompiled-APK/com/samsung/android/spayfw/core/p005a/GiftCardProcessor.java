package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.Handler;
import com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.MstConfigurationManager;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.core.Token;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytzsvc.api.TAException;

/* renamed from: com.samsung.android.spayfw.core.a.f */
public class GiftCardProcessor extends Processor {
    private static GiftCardProcessor le;
    PaymentDetailsRecord lf;

    public static final synchronized GiftCardProcessor m402n(Context context) {
        GiftCardProcessor giftCardProcessor;
        synchronized (GiftCardProcessor.class) {
            try {
                if (le == null) {
                    le = new GiftCardProcessor(context);
                }
                giftCardProcessor = le;
            } catch (Throwable e) {
                Log.m284c("GiftCardProcessor", e.getMessage(), e);
                giftCardProcessor = null;
            }
        }
        return giftCardProcessor;
    }

    private GiftCardProcessor(Context context) {
        super(context);
    }

    public void m405a(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        try {
            Log.m285d("GiftCardProcessor", "getGiftCardRegisterData()");
            if (giftCardRegisterRequestData == null || iGiftCardRegisterCallback == null) {
                if (iGiftCardRegisterCallback != null) {
                    Log.m286e("GiftCardProcessor", "getGiftCardRegisterData- invalid inputs: data is null");
                    iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
                    return;
                }
                Log.m286e("GiftCardProcessor", "getGiftCardRegisterData- invalid inputs: callback is null");
            } else if (giftCardRegisterRequestData.getGiftCardData() == null || giftCardRegisterRequestData.getServerCaCert() == null || giftCardRegisterRequestData.getServerEncCert() == null || giftCardRegisterRequestData.getServerVerCert() == null || giftCardRegisterRequestData.getUserId() == null || giftCardRegisterRequestData.getWalletId() == null) {
                Log.m286e("GiftCardProcessor", "getGiftCardRegisterData- invalid data");
                iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
            } else {
                Log.m285d("GiftCardProcessor", "Wallet ID =  " + giftCardRegisterRequestData.getWalletId());
                String config = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID);
                if (config == null) {
                    int config2 = ConfigurationManager.m581h(this.mContext).setConfig(PaymentFramework.CONFIG_WALLET_ID, giftCardRegisterRequestData.getWalletId());
                    Log.m287i("GiftCardProcessor", "Wallet ID Set: " + config2);
                    if (config2 != 0) {
                        iGiftCardRegisterCallback.onFail(-5, null);
                        return;
                    }
                } else if (!config.equals(giftCardRegisterRequestData.getWalletId())) {
                    Log.m286e("GiftCardProcessor", "Wallet ID Mismatched");
                    iGiftCardRegisterCallback.onFail(PaymentFramework.RESULT_CODE_FAIL_WALLET_ID_MISMATCH, null);
                    return;
                }
                Card M = m403M(giftCardRegisterRequestData.getUserId());
                if (M == null || M.ad() == null) {
                    iGiftCardRegisterCallback.onFail(-1, giftCardRegisterResponseData);
                    Log.m286e("GiftCardProcessor", "getGiftCardRegisterData- unable to get card Object");
                    return;
                }
                giftCardRegisterResponseData = M.ad().getGiftCardRegisterDataTA(giftCardRegisterRequestData);
                if (giftCardRegisterResponseData.getErrorCode() == 0) {
                    iGiftCardRegisterCallback.onSuccess(giftCardRegisterResponseData);
                } else {
                    iGiftCardRegisterCallback.onFail(giftCardRegisterResponseData.getErrorCode(), giftCardRegisterResponseData);
                }
            }
        } catch (Throwable e) {
            Log.m284c("GiftCardProcessor", e.getMessage(), e);
        }
    }

    public void m407b(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        try {
            Log.m285d("GiftCardProcessor", "getGiftCardTzEncData()");
            if (giftCardRegisterRequestData == null || iGiftCardRegisterCallback == null) {
                if (iGiftCardRegisterCallback != null) {
                    Log.m286e("GiftCardProcessor", "getGiftCardTzEncData- invalid inputs: data is null");
                    iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
                    return;
                }
                Log.m286e("GiftCardProcessor", "getGiftCardTzEncData- invalid inputs: callback is null");
            } else if (giftCardRegisterRequestData.getGiftCardData() == null || giftCardRegisterRequestData.getServerCaCert() == null || giftCardRegisterRequestData.getServerEncCert() == null || giftCardRegisterRequestData.getServerVerCert() == null) {
                Log.m286e("GiftCardProcessor", "getGiftCardTzEncData- invalid data");
                iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
            } else {
                Card M = m403M(null);
                if (M == null || M.ad() == null) {
                    iGiftCardRegisterCallback.onFail(-1, giftCardRegisterResponseData);
                    Log.m286e("GiftCardProcessor", "getGiftCardTzEncData- unable to get card Object");
                    return;
                }
                giftCardRegisterResponseData = M.ad().getGiftCardTzEncDataTA(giftCardRegisterRequestData);
                if (giftCardRegisterResponseData.getErrorCode() == 0) {
                    iGiftCardRegisterCallback.onSuccess(giftCardRegisterResponseData);
                } else {
                    iGiftCardRegisterCallback.onFail(giftCardRegisterResponseData.getErrorCode(), giftCardRegisterResponseData);
                }
            }
        } catch (Throwable e) {
            Log.m284c("GiftCardProcessor", e.getMessage(), e);
        }
    }

    public void m406a(byte[] bArr, byte[] bArr2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
        try {
            Log.m285d("GiftCardProcessor", "startGiftCardPay()");
            if (bArr2 == null || iPayCallback == null || securedObject == null) {
                if (iPayCallback != null) {
                    Log.m286e("GiftCardProcessor", "startGiftCardPay- invalid inputs: data is null");
                    iPayCallback.onFail(PlccTAController.CARD_BRAND_GIFT, -5);
                }
                if (bArr2 == null) {
                    Log.m286e("GiftCardProcessor", "startGiftCardPay- invalid inputs: giftCardTzEncData is null");
                    return;
                } else if (securedObject == null) {
                    Log.m286e("GiftCardProcessor", "startGiftCardPay- invalid inputs: secObj is null");
                    return;
                } else {
                    Log.m286e("GiftCardProcessor", "startGiftCardPay- invalid inputs: callback is null");
                    return;
                }
            }
            Card M = m403M(null);
            if (M == null || M.ad() == null) {
                if (iPayCallback != null) {
                    iPayCallback.onFail(PlccTAController.CARD_BRAND_GIFT, -1);
                }
                Log.m286e("GiftCardProcessor", "startGiftCardPay - unable to get card Object");
                return;
            }
            GiftCardDetail extractGiftCardDetailTA = M.ad().extractGiftCardDetailTA(bArr, bArr2, securedObject, true);
            if (extractGiftCardDetailTA.getErrorCode() == 0) {
                Log.m285d("GiftCardProcessor", "startGiftCardPay - extract giftCardDetail success");
            }
            Log.m285d("GiftCardProcessor", "startGiftCardPay - invoking onExtractGiftCardDetail cb");
            iPayCallback.onExtractGiftCardDetail(extractGiftCardDetailTA);
            M.ad().setCardTzEncData(bArr2);
            if (PaymentProcessor.m470q(this.mContext) != null) {
                PaymentProcessor.m470q(this.mContext).m419a(securedObject, payConfig, iPayCallback, true);
            }
        } catch (Throwable e) {
            Log.m284c("GiftCardProcessor", e.getMessage(), e);
        }
    }

    public void m404a(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback) {
        String str = null;
        try {
            byte[] tzEncData;
            byte[] cardRefID;
            String tokenId;
            Log.m285d("GiftCardProcessor", "extractGiftCardDetail()");
            if (extractGiftCardDetailRequest != null) {
                tzEncData = extractGiftCardDetailRequest.getTzEncData();
                cardRefID = extractGiftCardDetailRequest.getCardRefID();
                tokenId = extractGiftCardDetailRequest.getTokenId();
                str = extractGiftCardDetailRequest.getCardName();
            } else {
                tokenId = null;
                cardRefID = null;
                tzEncData = null;
            }
            if (tzEncData == null || iGiftCardExtractDetailCallback == null || securedObject == null) {
                if (iGiftCardExtractDetailCallback != null) {
                    iGiftCardExtractDetailCallback.onFail(-5);
                } else {
                    Log.m286e("GiftCardProcessor", "pay callback is null");
                }
                if (tzEncData == null) {
                    Log.m286e("GiftCardProcessor", "extractGiftCardDetail- invalid inputs: giftCardTzEncData is null");
                } else if (securedObject == null) {
                    Log.m286e("GiftCardProcessor", "extractGiftCardDetail- invalid inputs: secObj is null");
                } else {
                    Log.m286e("GiftCardProcessor", "extractGiftCardDetail- invalid inputs: callback is null");
                }
                MstConfigurationManager.m604j(this.mContext).ap();
                return;
            }
            Card M = m403M(null);
            if (M == null || M.ad() == null) {
                iGiftCardExtractDetailCallback.onFail(-1);
                Log.m286e("GiftCardProcessor", "extractGiftCardDetail - unable to get card Object");
                MstConfigurationManager.m604j(this.mContext).ap();
                return;
            }
            GiftCardDetail extractGiftCardDetailTA = M.ad().extractGiftCardDetailTA(cardRefID, tzEncData, securedObject, false);
            this.lf = new PaymentDetailsRecord();
            if (this.lf != null) {
                this.lf.setPaymentType(Card.m574y(M.getCardBrand()));
                this.lf.setBarcodeAttempted("ATTEMPTED");
                this.lf.setTrTokenId(PlccTAController.CARD_BRAND_GIFT);
                this.lf.setCardId(tokenId);
                this.lf.setCardName(str);
            }
            m401d(this.lf);
            if (extractGiftCardDetailTA.getErrorCode() == 0) {
                iGiftCardExtractDetailCallback.onSuccess(extractGiftCardDetailTA);
            } else {
                iGiftCardExtractDetailCallback.onFail(extractGiftCardDetailTA.getErrorCode());
            }
            MstConfigurationManager.m604j(this.mContext).ap();
        } catch (Throwable e) {
            Log.m284c("GiftCardProcessor", e.getMessage(), e);
            MstConfigurationManager.m604j(this.mContext).ap();
        }
    }

    protected Card m403M(String str) {
        Throwable th;
        Card card = null;
        try {
            if (this.iJ == null) {
                Log.m285d("GiftCardProcessor", "Initializing Samsung Account - userId = " + str);
                this.iJ = Account.m551a(this.mContext, str);
            }
            if (this.iJ == null) {
                Log.m286e("GiftCardProcessor", "unable to create account");
                return null;
            }
            Card r = this.iJ.m559r(PlccTAController.CARD_BRAND_GIFT);
            if (r != null) {
                return r;
            }
            try {
                card = new Card(this.mContext, PaymentFramework.CARD_BRAND_GIFT, PlccTAController.CARD_BRAND_GIFT, PlccTAController.CARD_BRAND_GIFT, 0);
                Token token = new Token();
                token.setTokenStatus(TokenStatus.ACTIVE);
                token.setTokenId(PlccTAController.CARD_BRAND_GIFT);
                ProviderTokenKey providerTokenKey = new ProviderTokenKey(PlccTAController.CARD_BRAND_GIFT);
                token.m663c(providerTokenKey);
                card.m576a(token);
                this.iJ.m556a(card);
                if (card.ad() != null) {
                    Log.m285d("GiftCardProcessor", "Set Provider Token Key for Gift Card");
                    card.ad().setProviderTokenKey(providerTokenKey);
                }
                TokenRecord tokenRecord = new TokenRecord(PlccTAController.CARD_BRAND_GIFT);
                tokenRecord.setUserId(Account.m551a(this.mContext, null).getAccountId());
                tokenRecord.setTrTokenId(PlccTAController.CARD_BRAND_GIFT);
                tokenRecord.m1255j(0);
                tokenRecord.setCardBrand(card.getCardBrand());
                tokenRecord.setTokenStatus(TokenStatus.ACTIVE);
                tokenRecord.setTokenRefId(PlccTAController.CARD_BRAND_GIFT);
                this.jJ.m1227c(tokenRecord);
                return card;
            } catch (Throwable e) {
                th = e;
                card = r;
                Log.m284c("GiftCardProcessor", th.getMessage(), th);
                return card;
            } catch (Throwable e2) {
                th = e2;
                card = r;
                Log.m284c("GiftCardProcessor", th.getMessage(), th);
                return card;
            }
        } catch (TAException e3) {
            th = e3;
            Log.m284c("GiftCardProcessor", th.getMessage(), th);
            return card;
        } catch (Exception e4) {
            th = e4;
            Log.m284c("GiftCardProcessor", th.getMessage(), th);
            return card;
        }
    }

    private void m401d(PaymentDetailsRecord paymentDetailsRecord) {
        Handler az = PaymentFrameworkApp.az();
        if (az != null) {
            Log.m285d("GiftCardProcessor", "Post PAYFW_OPT_ANALYTICS_REPORT request");
            az.sendMessage(PaymentFrameworkMessage.m620a(21, paymentDetailsRecord, null));
            return;
        }
        Log.m286e("GiftCardProcessor", "HANDLER IS NOT INITIAILIZED");
    }
}
