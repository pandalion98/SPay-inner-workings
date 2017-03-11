package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.QueryTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.x */
public class TokenManager extends Processor {
    private ICardDataCallback my;

    /* renamed from: com.samsung.android.spayfw.core.a.x.1 */
    class TokenManager extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ Card jL;
        final /* synthetic */ String lV;
        final /* synthetic */ TokenManager mz;

        TokenManager(TokenManager tokenManager, String str, Card card) {
            this.mz = tokenManager;
            this.lV = str;
            this.jL = card;
        }

        public void m530a(int i, Response<TokenResponseData> response) {
            Token token;
            int i2;
            TokenResponseData tokenResponseData;
            TokenMetaData tokenMetaData;
            Card r = this.mz.iJ.m559r(this.lV);
            Log.m285d("TokenManager", "onRequestComplete: getCardData: code " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    token = null;
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    token = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    TokenRecord bq = this.mz.jJ.bq(this.lV);
                    if (r != null && bq != null) {
                        if (response != null && response.getResult() != null) {
                            TokenResponseData tokenResponseData2 = (TokenResponseData) response.getResult();
                            Token a = ResponseDataBuilder.m632a(this.mz.mContext, r, tokenResponseData2);
                            if (!(a == null || a.getMetadata() == null)) {
                                ResponseDataBuilder.m637a(this.mz.mContext, a.getMetadata(), r);
                            }
                            TokenStatus status = tokenResponseData2.getStatus();
                            String y = Card.m574y(r.getCardBrand());
                            if (status != null && status.getCode() != null) {
                                Object obj;
                                int i3;
                                String a2 = ResponseDataBuilder.m635a(tokenResponseData2);
                                Log.m287i("TokenManager", "TokenResponseData:tokenStaus in db  " + r.ac().getTokenStatus());
                                Log.m287i("TokenManager", "TokenResponseData:tokenStaus from server  " + status.getCode());
                                if (TokenStatus.PENDING_PROVISION.equals(r.ac().getTokenStatus())) {
                                    if (tokenResponseData2.getData() == null || r.ac().aQ() != null) {
                                        if (this.mz.m535j(r.ac().getTokenStatus(), status.getCode())) {
                                            if (PaymentResponseData.Card.STATUS_PENDING.equals(status.getCode())) {
                                                obj = null;
                                                i3 = 0;
                                            } else {
                                                obj = 1;
                                                i3 = 0;
                                            }
                                        }
                                        obj = null;
                                        i3 = 0;
                                    } else {
                                        int i4;
                                        TokenRecord tokenRecord;
                                        ProviderRequestData providerRequestData = new ProviderRequestData();
                                        providerRequestData.m822a(tokenResponseData2.getData());
                                        providerRequestData.m823e(ResponseDataBuilder.m643b((TokenResponseData) response.getResult()));
                                        ProviderResponseData createTokenTA = r.ad().createTokenTA(r.ac().getTokenId(), providerRequestData, 2);
                                        if (createTokenTA == null || createTokenTA.getErrorCode() != 0) {
                                            Log.m286e("TokenManager", "Provider unable to store provision info");
                                            i4 = -1;
                                            obj = null;
                                            tokenRecord = bq;
                                        } else {
                                            ProviderTokenKey providerTokenKey = createTokenTA.getProviderTokenKey();
                                            if (providerTokenKey == null) {
                                                Log.m286e("TokenManager", "Provision Token- onRequestComplete: provider not returning tokenref ");
                                                i2 = -1;
                                                token = a;
                                                break;
                                            }
                                            r.ac().setTokenStatus(a2);
                                            r.ac().m662H(status.getReason());
                                            r.ac().m663c(providerTokenKey);
                                            bq.setTokenStatus(r.ac().getTokenStatus());
                                            bq.m1251H(r.ac().aP());
                                            tokenRecord = ResponseDataBuilder.m634a(bq, tokenResponseData2, providerTokenKey);
                                            if (this.mz.m333a(tokenRecord)) {
                                                Log.m286e("TokenManager", "Duplicate Token Ref Id / Tr Token Id");
                                                this.jL.ad().updateTokenStatusTA(null, new TokenStatus(TokenStatus.DISPOSED, null));
                                                this.mz.iJ.m561t(this.lV);
                                                this.mz.jJ.m1229d(TokenColumn.TR_TOKEN_ID, this.lV);
                                                Log.m286e("TokenManager", "processProvision:Send error report to TR server");
                                                this.mz.m335b(null, this.lV, ResponseDataBuilder.m628F(tokenRecord.getTokenStatus()), PushMessage.TYPE_PROVISION, y, null, false);
                                                Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                                                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_SYNC_ALL_CARDS);
                                                PaymentFrameworkApp.m315a(intent);
                                                token = a;
                                                i2 = -6;
                                                break;
                                            }
                                            this.mz.jJ.m1230d(tokenRecord);
                                            r.m577j(tokenRecord.ab());
                                            i4 = 0;
                                            obj = 1;
                                            FraudDataCollector x = FraudDataCollector.m718x(this.mz.mContext);
                                            if (x == null || this.lV == null) {
                                                Log.m285d("TokenManager", "FraudCollector: storeTokenEnrollmentSuccess cannot get data");
                                            } else {
                                                x.m724l(this.lV, status.getCode());
                                            }
                                        }
                                        if (obj != null) {
                                            this.mz.m331a(null, this.lV, tokenRecord.getTokenStatus(), PushMessage.TYPE_PROVISION, y, createTokenTA, false);
                                        } else {
                                            Log.m286e("TokenManager", "processProvision:Send error report to TR server");
                                            this.mz.m335b(null, this.lV, ResponseDataBuilder.m628F(tokenRecord.getTokenStatus()), PushMessage.TYPE_PROVISION, y, createTokenTA, false);
                                        }
                                        obj = null;
                                        bq = tokenRecord;
                                        i3 = i4;
                                    }
                                } else if (TokenStatus.SUSPENDED.equals(r.ac().getTokenStatus())) {
                                    if (this.mz.m535j(r.ac().getTokenStatus(), status.getCode())) {
                                        obj = 1;
                                        i3 = 0;
                                    }
                                    obj = null;
                                    i3 = 0;
                                } else {
                                    if (TokenStatus.ACTIVE.equals(r.ac().getTokenStatus()) && this.mz.m535j(r.ac().getTokenStatus(), status.getCode())) {
                                        obj = 1;
                                        i3 = 0;
                                    }
                                    obj = null;
                                    i3 = 0;
                                }
                                if (obj == null) {
                                    Log.m285d("TokenManager", "getCardData: payprovider update token meta data called ");
                                    r.ad().updateTokenMetaDataTA(tokenResponseData2.getData(), a);
                                    token = a;
                                    i2 = i3;
                                    break;
                                }
                                Log.m285d("TokenManager", "getCardData: update Card status ");
                                r.ad().updateTokenStatusTA(tokenResponseData2.getData(), status);
                                bq.setTokenStatus(a2);
                                bq.m1251H(status.getReason());
                                this.mz.jJ.m1230d(bq);
                                r.ac().setTokenStatus(a2);
                                r.ac().m662H(status.getReason());
                                String F = ResponseDataBuilder.m628F(bq.getTokenStatus());
                                FraudDataCollector x2 = FraudDataCollector.m718x(this.mz.mContext);
                                if (x2 != null) {
                                    x2.m723k(bq.getTokenRefId(), bq.getTokenStatus());
                                } else {
                                    Log.m285d("TokenManager", "FraudCollector: updateFTokenRecordStatus cannot get data");
                                }
                                if (TokenStatus.DISPOSED.equals(status.getCode())) {
                                    if (this.mz.jJ.m1229d(TokenColumn.TR_TOKEN_ID, this.lV) < 1) {
                                        Log.m286e("TokenManager", "Not able to delete Token from DB");
                                    }
                                    this.mz.iJ.m560s(r.ac().getTokenId());
                                }
                                this.mz.m331a(null, this.lV, F, PushMessage.TYPE_STATUS_CHANGE, y, null, false);
                                token = a;
                                i2 = i3;
                                break;
                            }
                            Log.m286e("TokenManager", "TokenResponseData:  newTokenStatus is null");
                            i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                            token = a;
                            break;
                        }
                        Log.m286e("TokenManager", "TokenResponseData is null");
                        token = null;
                        i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                        break;
                    }
                    Log.m286e("TokenManager", "unable to get card object ");
                    if (bq != null) {
                        Log.m287i("TokenManager", "delete record from db ");
                        this.mz.jJ.m1229d(TokenColumn.TR_TOKEN_ID, this.lV);
                    }
                    if (this.jL != null) {
                        Log.m286e("TokenManager", "delete card object");
                        this.mz.iJ.m561t(this.lV);
                    }
                    token = null;
                    i2 = -6;
                    break;
                    break;
                case 404:
                case 410:
                    Log.m290w("TokenManager", "unable to find the token on server. something wrong. deleting the token");
                    if (this.mz.jJ.m1229d(TokenColumn.TR_TOKEN_ID, this.lV) < 1) {
                        Log.m286e("TokenManager", "Not able to delete Token from DB");
                    }
                    TokenStatus tokenStatus = new TokenStatus(TokenStatus.DISPOSED, null);
                    if (r != null) {
                        r.ad().updateTokenStatusTA(null, tokenStatus);
                        this.mz.iJ.m560s(r.ac().getTokenId());
                    }
                    token = null;
                    i2 = -6;
                    break;
                case 500:
                    token = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    break;
                default:
                    token = null;
                    i2 = -1;
                    break;
            }
            if (!(token == null || token.getMetadata() == null || r == null)) {
                ResponseDataBuilder.m637a(this.mz.mContext, token.getMetadata(), r);
            }
            if (i2 != 0) {
                try {
                    this.mz.my.onFail(this.lV, i2);
                } catch (Throwable e) {
                    Log.m284c("TokenManager", e.getMessage(), e);
                    tokenResponseData = response == null ? null : (TokenResponseData) response.getResult();
                    if (token == null) {
                        tokenMetaData = null;
                    } else {
                        tokenMetaData = token.getMetadata();
                    }
                    ResponseDataBuilder.m642a(tokenResponseData, tokenMetaData);
                    return;
                } catch (Throwable e2) {
                    Throwable th = e2;
                    TokenResponseData tokenResponseData3 = response == null ? null : (TokenResponseData) response.getResult();
                    if (token == null) {
                        tokenMetaData = null;
                    } else {
                        tokenMetaData = token.getMetadata();
                    }
                    ResponseDataBuilder.m642a(tokenResponseData3, tokenMetaData);
                }
            } else {
                if (token != null) {
                    Log.m285d("TokenManager", "getCardData: " + token.toString());
                }
                this.mz.my.onSuccess(this.lV, token);
            }
            tokenResponseData = response == null ? null : (TokenResponseData) response.getResult();
            if (token == null) {
                tokenMetaData = null;
            } else {
                tokenMetaData = token.getMetadata();
            }
            ResponseDataBuilder.m642a(tokenResponseData, tokenMetaData);
        }
    }

    public TokenManager(Context context) {
        super(context);
        this.my = null;
    }

    public List<String> isDsrpBlobMissing() {
        Log.m285d("TokenManager", "Entered isDsrpBlobMissing");
        List<String> arrayList = new ArrayList();
        if (this.iJ == null) {
            Log.m286e("TokenManager", "isDsrpBlobMissing  - mAccount is null");
            return arrayList;
        }
        List W = this.iJ.m553W();
        if (!(W == null || W.isEmpty())) {
            for (int i = 0; i < W.size(); i++) {
                Card card = (Card) W.get(i);
                if (!(card == null || card.ac() == null || card.ac().aQ() == null || card.ad() == null || !card.ad().isDsrpBlobMissing())) {
                    Log.m285d("TokenManager", "dsrpBlobMissing: tokenId = " + card.ac().getTokenId());
                    arrayList.add(card.ac().getTokenId());
                }
            }
        }
        return arrayList;
    }

    public int isDsrpBlobMissingForTokenId(String str) {
        Log.m285d("TokenManager", "Entered isDsrpBlobMissingForTokenId");
        if (this.iJ == null) {
            Log.m286e("TokenManager", "isDsrpBlobMissingForTokenId  - mAccount is null");
            return 0;
        }
        Card r = this.iJ.m559r(str);
        if (r == null || r.ac() == null || r.ac().aQ() == null || r.ad() == null) {
            return 0;
        }
        boolean isDsrpBlobMissing = r.ad().isDsrpBlobMissing();
        if (!isDsrpBlobMissing) {
            return 0;
        }
        Log.m285d("TokenManager", "dsrpBlobMissing = " + isDsrpBlobMissing);
        return -45;
    }

    public List<CardState> getAllCardState(Bundle bundle) {
        if (this.iJ == null) {
            Log.m286e("TokenManager", "getAllCardState  - mAccount is null");
            return new ArrayList();
        } else if (bundle == null || bundle.getStringArray(PaymentFramework.EXTRA_CARD_TYPE) == null) {
            Log.m286e("TokenManager", "getAllCardState - card filter or card type is null. return null");
            return null;
        } else {
            String[] stringArray = bundle.getStringArray(PaymentFramework.EXTRA_CARD_TYPE);
            Log.m285d("TokenManager", "getAllCardState: " + Arrays.toString(stringArray));
            List W = this.iJ.m553W();
            List<CardState> arrayList = new ArrayList();
            List arrayList2 = new ArrayList(Arrays.asList(stringArray));
            if (!(W == null || W.isEmpty())) {
                Log.m285d("TokenManager", "getAllCards: cardList:" + W.size());
                for (int i = 0; i < W.size(); i++) {
                    CardState cardState = new CardState();
                    Card card = (Card) W.get(i);
                    if (card == null || card.getCardBrand() == null) {
                        Log.m290w("TokenManager", "card or card brand is null");
                    } else {
                        Object obj;
                        Log.m285d("TokenManager", "getAllCards: card:" + card.toString());
                        if (card.ac() == null || card.ac().getTokenId() == null || !card.ac().getTokenId().equals(PlccTAController.CARD_BRAND_GIFT)) {
                            obj = null;
                        } else if (arrayList2.contains(PlccTAController.CARD_BRAND_GIFT)) {
                            Log.m285d("TokenManager", "card type is gift and request filter have gift");
                            obj = 1;
                        } else {
                            Log.m285d("TokenManager", "card type is gift and request filter doesn't have gift");
                        }
                        if (card.getCardBrand().equals(PaymentFramework.CARD_BRAND_LOYALTY)) {
                            if (arrayList2.contains(PlccTAController.CARD_BRAND_LOYALTY)) {
                                Log.m285d("TokenManager", "card brand is loyalty and request filter have loyalty");
                                obj = 1;
                            } else {
                                Log.m285d("TokenManager", "card type is loyalty and request filter doesn't have loyalty");
                            }
                        }
                        if (card.getCardBrand().equals(PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP)) {
                            if (arrayList2.contains(PaymentFramework.CARD_TYPE_GLOBAL_MEMBERSHIP)) {
                                Log.m285d("TokenManager", "card brand is Global Membership and request filter have Global Membership");
                                obj = 1;
                            } else {
                                Log.m285d("TokenManager", "card type is Global Membership and request filter doesn't have Global Membership");
                            }
                        }
                        if (arrayList2.contains(PaymentFramework.CARD_TYPE_DEBIT) || arrayList2.contains(PaymentFramework.CARD_TYPE_CREDIT) || arrayList2.contains(PaymentFramework.CARD_TYPE_SAMSUNG_REWARD) || r3 != null) {
                            cardState.setEnrollmentId(card.getEnrollmentId());
                            if (card.ac() != null) {
                                boolean payReadyState;
                                if (card.ac().getTokenId() != null) {
                                    cardState.setTokenId(card.ac().getTokenId());
                                }
                                cardState.setTokenStatus(new TokenStatus(card.ac().getTokenStatus(), card.ac().aP()));
                                if (card.ac().aQ() != null) {
                                    payReadyState = card.ad().getPayReadyState();
                                } else {
                                    payReadyState = false;
                                }
                                cardState.setAvailableForPayState(payReadyState);
                            }
                            arrayList.add(cardState);
                        } else {
                            Log.m285d("TokenManager", "card type is payment.Request filter dose not have payment type");
                        }
                    }
                }
            }
            if (arrayList.isEmpty()) {
                Log.m286e("TokenManager", "getAllCardState is empty");
            } else {
                Log.m285d("TokenManager", "getAllCardState: " + arrayList.toString());
            }
            return arrayList;
        }
    }

    public int clearEnrolledCard(String str) {
        if (this.iJ != null && str != null) {
            Card q = this.iJ.m558q(str);
            if (q == null || q.ac() == null) {
                return -1;
            }
            if (TokenStatus.PENDING_ENROLLED.equals(q.ac().getTokenStatus())) {
                Log.m287i("TokenManager", "clearEnrolledCard: " + str);
                this.iJ.m560s(str);
                if (this.jJ.m1229d(TokenColumn.ENROLLMENT_ID, str) < 1) {
                    Log.m286e("TokenManager", "Not able to delete enrollementId from DB");
                }
                return 0;
            }
            Log.m290w("TokenManager", "Not able to delete : Tokenstatus: " + q.ac().getTokenStatus());
            return -4;
        } else if (this.iJ == null) {
            Log.m286e("TokenManager", "clearEnrolledCard  - Failed to initialize account");
            return -1;
        } else {
            Log.m286e("TokenManager", "clearEnrolledCard  - enrollmentId is null");
            return -5;
        }
    }

    public List<String> getPaymentReadyState(String str) {
        List<String> arrayList = new ArrayList();
        if (this.iJ == null) {
            Log.m286e("TokenManager", "getPaymentReadyState  - mAccount is null");
            return arrayList;
        }
        Card card;
        if (str == null || this.iJ.m559r(str) == null) {
            List W = this.iJ.m553W();
            for (int i = 0; i < W.size(); i++) {
                card = (Card) W.get(i);
                if (!(card == null || card.ac() == null || card.ac().getTokenId() == null || card.ac().aQ() == null)) {
                    if (card.ad().getPayReadyState()) {
                        arrayList.add(card.ac().getTokenId());
                    } else {
                        Log.m287i("TokenManager", "not ready for payment: " + card.ac().getTokenId());
                    }
                }
            }
        } else {
            card = this.iJ.m559r(str);
            if (!(card == null || card.ac() == null || card.ac().aQ() == null)) {
                if (card.ad().getPayReadyState()) {
                    arrayList.add(str);
                } else {
                    Log.m287i("TokenManager", "not ready for payment: " + str);
                }
            }
        }
        return arrayList;
    }

    public void m536a(String str, ICardDataCallback iCardDataCallback) {
        if (str == null || iCardDataCallback == null || this.iJ == null) {
            int i = -5;
            if (str == null) {
                Log.m286e("TokenManager", "getCardData Failed - token Id is null");
            }
            if (this.iJ == null) {
                Log.m286e("TokenManager", "getCardData Failed - Failed to initialize account");
                i = -1;
            }
            if (iCardDataCallback != null) {
                iCardDataCallback.onFail(str, i);
                return;
            } else {
                Log.m286e("TokenManager", "getCardData Failed - Provision Callback is null");
                return;
            }
        }
        this.my = iCardDataCallback;
        Card r = this.iJ.m559r(str);
        if (r == null) {
            Log.m286e("TokenManager", "getCardData Failed - Invalid token Id");
            this.my.onFail(str, -6);
            return;
        }
        m534f(str, r);
    }

    public TokenStatus getTokenStatus(String str) {
        Log.m285d("TokenManager", "getTokenStatus()");
        if (str == null || str.isEmpty()) {
            Log.m286e("TokenManager", "getTokenStatus() token id is empty or null");
            return null;
        }
        TokenRecord bq;
        if (this.jJ != null) {
            bq = this.jJ.bq(str);
        } else {
            bq = null;
        }
        if (bq != null) {
            return new TokenStatus(bq.getTokenStatus(), bq.fy());
        }
        return null;
    }

    private void m534f(String str, Card card) {
        this.lQ.m1141x(Card.m574y(card.getCardBrand()), str).m836a(new TokenManager(this, str, card));
    }

    private boolean m535j(String str, String str2) {
        if (str.equals(str2) || ((TokenStatus.PENDING_ENROLLED.equals(str) || TokenStatus.PENDING_PROVISION.equals(str) || TokenStatus.ACTIVE.equals(str) || TokenStatus.SUSPENDED.equals(str)) && PaymentResponseData.Card.STATUS_PENDING.equals(str2))) {
            return false;
        }
        return true;
    }
}
