package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.DeleteTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.w */
public class TokenDeleter extends Processor {
    protected String mEnrollmentId;
    protected IDeleteCardCallback mt;
    protected Bundle mv;

    /* renamed from: com.samsung.android.spayfw.core.a.w.a */
    private class TokenDeleter extends C0413a<Response<String>, DeleteTokenRequest> {
        String mEnrollmentId;
        IDeleteCardCallback mt;
        Card mw;
        final /* synthetic */ TokenDeleter mx;

        public TokenDeleter(TokenDeleter tokenDeleter, String str, Card card, IDeleteCardCallback iDeleteCardCallback) {
            this.mx = tokenDeleter;
            this.mw = null;
            this.mEnrollmentId = str;
            this.mt = iDeleteCardCallback;
            this.mw = card;
        }

        public void m526a(int i, Response<String> response) {
            int i2 = -1;
            Log.m287i("TokenDeleter", "TokenDeleter: onRequestComplete:  " + i);
            Card q = this.mx.iJ.m558q(this.mEnrollmentId);
            TokenStatus tokenStatus = new TokenStatus(TokenStatus.DISPOSED, null);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                case 202:
                    i2 = 0;
                    break;
                case 204:
                case 404:
                case 410:
                    i2 = 0;
                    break;
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    break;
            }
            if (q != null) {
                try {
                    if (q.ac() != null) {
                        if (i2 != 0) {
                            Log.m286e("TokenDeleter", "Delete Token Failed - Error Code = " + i2);
                            q.ad().updateRequestStatus(new ProviderRequestStatus(22, -1, q.ac().aQ()));
                        } else {
                            q.ad().updateRequestStatus(new ProviderRequestStatus(22, 0, q.ac().aQ()));
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c("TokenDeleter", e.getMessage(), e);
                    return;
                }
            }
            TokenStatus tokenStatus2;
            if (i2 == 0) {
                tokenStatus2 = new TokenStatus(TokenStatus.DISPOSED, null);
                if (q != null) {
                    ProviderResponseData updateTokenStatusTA = q.ad().updateTokenStatusTA(null, tokenStatus2);
                    TokenRecord bp = this.mx.jJ.bp(this.mEnrollmentId);
                    String y = Card.m574y(q.getCardBrand());
                    String tokenId = q.ac().getTokenId();
                    if (bp != null) {
                        this.mx.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId);
                        Log.m285d("TokenDeleter", "TokenDeleter: db recored deleted for : " + this.mEnrollmentId);
                        FraudDataCollector x = FraudDataCollector.m718x(this.mx.mContext);
                        if (x != null) {
                            x.m723k(bp.getTrTokenId(), TokenStatus.DISPOSED);
                        }
                    }
                    this.mx.iJ.m560s(this.mEnrollmentId);
                    if (i2 != 204) {
                        if (updateTokenStatusTA == null || updateTokenStatusTA.getErrorCode() != 0) {
                            this.mx.m335b(null, tokenId, TokenStatus.DISPOSED, PushMessage.TYPE_STATUS_CHANGE, y, updateTokenStatusTA, false);
                        } else {
                            this.mx.m331a(null, tokenId, TokenStatus.DISPOSED, PushMessage.TYPE_STATUS_CHANGE, y, updateTokenStatusTA, false);
                        }
                    }
                }
                this.mt.onSuccess(this.mEnrollmentId, tokenStatus);
                return;
            }
            if (q == null || q.ac() == null) {
                tokenStatus2 = tokenStatus;
            } else {
                tokenStatus2 = new TokenStatus(q.ac().getTokenStatus(), q.ac().aP());
            }
            this.mt.onFail(this.mEnrollmentId, i2, tokenStatus2);
        }

        public void m527a(int i, ServerCertificates serverCertificates, DeleteTokenRequest deleteTokenRequest) {
            Log.m285d("TokenDeleter", "onCertsReceived: called for Delete");
            Object obj = null;
            Card q = this.mx.iJ.m558q(this.mEnrollmentId);
            if (q == null) {
                Log.m286e("TokenDeleter", "TokenDeleter : unable to get Card object :" + this.mEnrollmentId);
                try {
                    this.mt.onSuccess(this.mEnrollmentId, new TokenStatus(TokenStatus.DISPOSED, null));
                    return;
                } catch (Throwable e) {
                    Log.m284c("TokenDeleter", e.getMessage(), e);
                    return;
                }
            }
            int i2;
            if (this.mx.m334a(q.getEnrollmentId(), q, serverCertificates)) {
                q.ac().aQ();
                ProviderRequestData deleteRequestDataTA = q.ad().getDeleteRequestDataTA(null);
                if (deleteRequestDataTA == null || deleteRequestDataTA.getErrorCode() != 0) {
                    Log.m286e("TokenDeleter", " unable to get delete data from pay provider");
                    i2 = 1;
                } else {
                    Data data = new Data();
                    data.setData(deleteRequestDataTA.ch());
                    deleteTokenRequest.m843k(data);
                    deleteTokenRequest.bf(this.mx.m329P(this.mw.getCardBrand()));
                    deleteTokenRequest.m836a((C0413a) this);
                    Log.m287i("TokenDeleter", "TokenDeleter: deleteToken request made for: " + this.mw.ac().getTokenId());
                }
            } else {
                Log.m286e("TokenDeleter", "Server certificate update failed.Delete aborted");
                i2 = 1;
            }
            if (obj != null && this.mt != null) {
                try {
                    this.mt.onFail(this.mEnrollmentId, -1, new TokenStatus(q.ac().getTokenStatus(), q.ac().aP()));
                } catch (Throwable e2) {
                    Log.m284c("TokenDeleter", e2.getMessage(), e2);
                }
            }
        }
    }

    public TokenDeleter(Context context, String str, Bundle bundle, IDeleteCardCallback iDeleteCardCallback) {
        super(context);
        this.mEnrollmentId = str;
        this.mt = iDeleteCardCallback;
        this.mv = bundle;
    }

    public void process() {
        int i = -1;
        if (this.mEnrollmentId != null && this.mt != null && this.iJ != null) {
            Card q = this.iJ.m558q(this.mEnrollmentId);
            if (q == null) {
                Log.m286e("TokenDeleter", "Token delete Failed - Invalid Enrollment Id");
                try {
                    this.mt.onFail(this.mEnrollmentId, -6, null);
                    return;
                } catch (Throwable e) {
                    Log.m284c("TokenDeleter", e.getMessage(), e);
                    return;
                }
            }
            Log.m287i("TokenDeleter", "TokenDeleter: Tokenstatus" + q.ac().getTokenStatus());
            if (TokenStatus.PENDING_ENROLLED.equals(q.ac().getTokenStatus()) || q.ac().getTokenId() == null || TextUtils.equals(q.getCardBrand(), PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP)) {
                this.iJ.m560s(this.mEnrollmentId);
                if (this.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId) < 1) {
                    Log.m286e("TokenDeleter", "Not able to delete enrollementId from DB");
                }
                try {
                    this.mt.onSuccess(this.mEnrollmentId, new TokenStatus(TokenStatus.DISPOSED, null));
                    return;
                } catch (Throwable e2) {
                    Log.m284c("TokenDeleter", e2.getMessage(), e2);
                    return;
                }
            }
            ProviderRequestData deleteRequestDataTA = q.ad().getDeleteRequestDataTA(this.mv);
            if (deleteRequestDataTA == null || deleteRequestDataTA.getErrorCode() != 0) {
                Log.m286e("TokenDeleter", "Not able to delete: pay provider error");
                try {
                    this.mt.onFail(this.mEnrollmentId, -1, null);
                    return;
                } catch (Throwable e22) {
                    Log.m284c("TokenDeleter", e22.getMessage(), e22);
                    return;
                }
            }
            Data data = new Data();
            data.setData(deleteRequestDataTA.ch());
            DeleteTokenRequest a = this.lQ.m1127a(Card.m574y(q.getCardBrand()), q.ac().getTokenId(), data);
            a.bf(m329P(q.getCardBrand()));
            a.m836a(new TokenDeleter(this, this.mEnrollmentId, q, this.mt));
            Log.m285d("TokenDeleter", "TokenDeleter: deleteToken request made for: " + q.ac().getTokenId());
            Log.m287i("TokenDeleter", "TokenDeleter: deleteToken request made");
        } else if (this.mt != null) {
            if (this.iJ != null) {
                i = -5;
            }
            try {
                Log.m286e("TokenDeleter", "Token delete Failed - Invalid inputs");
                this.mt.onFail(this.mEnrollmentId, i, null);
            } catch (Throwable e222) {
                Log.m284c("TokenDeleter", e222.getMessage(), e222);
            }
        }
    }
}
