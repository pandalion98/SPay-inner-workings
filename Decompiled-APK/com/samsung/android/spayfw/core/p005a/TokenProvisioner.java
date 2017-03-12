package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.RequestDataBuilder;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ProvisionRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ProvisionResponse;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.y */
public class TokenProvisioner extends Processor {
    protected final IProvisionTokenCallback mA;
    protected ProvisionTokenInfo mB;
    protected final String mEnrollmentId;

    /* renamed from: com.samsung.android.spayfw.core.a.y.a */
    private class TokenProvisioner extends C0413a<ProvisionResponse, ProvisionRequest> {
        IProvisionTokenCallback mA;
        final /* synthetic */ TokenProvisioner mC;
        String mEnrollmentId;

        public TokenProvisioner(TokenProvisioner tokenProvisioner, String str, IProvisionTokenCallback iProvisionTokenCallback) {
            this.mC = tokenProvisioner;
            this.mEnrollmentId = str;
            this.mA = iProvisionTokenCallback;
        }

        public void m537a(int i, ProvisionResponse provisionResponse) {
            int i2;
            Object obj;
            String str;
            ProviderResponseData providerResponseData;
            Object obj2;
            String str2;
            String brand;
            ProvisionTokenResult provisionTokenResult = null;
            Log.m287i("TokenProvisioner", "Provision Token  - onRequestComplete: " + i);
            Card q = this.mC.iJ.m558q(this.mEnrollmentId);
            Object obj3 = null;
            Object obj4 = null;
            String str3 = PaymentResponseData.Card.STATUS_PENDING;
            ProviderResponseData providerResponseData2 = null;
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    obj = null;
                    str = str3;
                    providerResponseData = null;
                    obj2 = null;
                    str2 = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    obj = null;
                    str = str3;
                    providerResponseData = null;
                    obj2 = null;
                    str2 = null;
                    break;
                case 202:
                    if (provisionResponse == null) {
                        Log.m286e("TokenProvisioner", "Provision Response is null");
                        i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_OUTPUT_INVALID;
                        obj = null;
                        str = str3;
                        providerResponseData = null;
                        obj2 = null;
                        str2 = null;
                        break;
                    }
                    TokenResponseData tokenResponseData = (TokenResponseData) provisionResponse.getResult();
                    if (tokenResponseData.getCard() != null) {
                        brand = tokenResponseData.getCard().getBrand();
                    } else {
                        brand = null;
                    }
                    Log.m285d("TokenProvisioner", "ProvisionCallback:onRequestComplete: Provision Data : " + tokenResponseData.getData());
                    if (tokenResponseData.getData() != null) {
                        obj3 = 1;
                    }
                    TokenRecord bp = this.mC.jJ.bp(this.mEnrollmentId);
                    if (q != null && bp != null) {
                        String str4;
                        ProviderTokenKey providerTokenKey = null;
                        if (tokenResponseData.getData() != null) {
                            ProviderRequestData providerRequestData = new ProviderRequestData();
                            providerRequestData.m822a(tokenResponseData.getData());
                            providerRequestData.m823e(ResponseDataBuilder.m643b((TokenResponseData) provisionResponse.getResult()));
                            providerResponseData2 = q.ad().createTokenTA(tokenResponseData.getId(), providerRequestData, 1);
                            if (providerResponseData2 == null || providerResponseData2.getErrorCode() != 0 || providerResponseData2.getProviderTokenKey() == null) {
                                if (providerResponseData2 == null) {
                                    Log.m286e("TokenProvisioner", "PayProvider error: ProviderResponseData is null");
                                } else if (providerResponseData2.getErrorCode() != 0) {
                                    Log.m286e("TokenProvisioner", "PayProvider error: pay provider return error: " + providerResponseData2.getErrorCode());
                                } else {
                                    Log.m286e("TokenProvisioner", "PayProvider error: providerTokenKey is null");
                                }
                                i2 = -1;
                                str4 = brand;
                                obj = obj3;
                                str = str3;
                                providerResponseData = providerResponseData2;
                                obj2 = null;
                                str2 = str4;
                                break;
                            }
                            providerTokenKey = providerResponseData2.getProviderTokenKey();
                            obj4 = 1;
                        }
                        bp = ResponseDataBuilder.m634a(bp, tokenResponseData, providerTokenKey);
                        if (tokenResponseData.getData() == null || tokenResponseData.getStatus() == null) {
                            bp.setTokenStatus(TokenStatus.PENDING_PROVISION);
                        }
                        String F;
                        if (!this.mC.m333a(bp)) {
                            this.mC.jJ.m1230d(bp);
                            q.ac().setTokenId(bp.getTrTokenId());
                            q.ac().setTokenStatus(bp.getTokenStatus());
                            q.ac().m662H(bp.fy());
                            if (providerTokenKey != null) {
                                q.ac().m663c(providerTokenKey);
                            } else {
                                ProviderTokenKey providerTokenKey2 = new ProviderTokenKey(null);
                                providerTokenKey2.setTrTokenId(bp.getTrTokenId());
                                q.ad().setProviderTokenKey(providerTokenKey2);
                            }
                            q.setCardBrand(bp.getCardBrand());
                            q.setCardType(bp.getCardType());
                            q.m577j(bp.ab());
                            ProvisionTokenResult a = ResponseDataBuilder.m630a(this.mC.mContext, provisionResponse, q, providerResponseData2);
                            if (obj4 == null) {
                                provisionTokenResult = a;
                                i2 = 0;
                                str4 = str3;
                                providerResponseData = providerResponseData2;
                                obj2 = obj4;
                                str2 = brand;
                                obj = obj3;
                                str = str4;
                                break;
                            }
                            F = ResponseDataBuilder.m628F(bp.getTokenStatus());
                            if (q.getCardBrand() != null) {
                                if (!q.getCardBrand().equals(PaymentFramework.CARD_BRAND_LOYALTY)) {
                                    FraudDataCollector x = FraudDataCollector.m718x(this.mC.mContext);
                                    if (x != null) {
                                        x.m724l(q.ac().getTokenId(), F);
                                    } else {
                                        Log.m285d("TokenProvisioner", "ProvisionCallback:onRequestComplete: cannot get fraud data");
                                    }
                                    providerResponseData = providerResponseData2;
                                    provisionTokenResult = a;
                                    obj2 = obj4;
                                    str2 = brand;
                                    obj = obj3;
                                    str = F;
                                    i2 = 0;
                                    break;
                                }
                                providerResponseData = providerResponseData2;
                                provisionTokenResult = a;
                                obj2 = obj4;
                                str2 = brand;
                                obj = obj3;
                                str = F;
                                i2 = 0;
                                break;
                            }
                            Log.m286e("TokenProvisioner", "Card Brand is null");
                            providerResponseData = providerResponseData2;
                            provisionTokenResult = a;
                            obj2 = obj4;
                            str2 = brand;
                            obj = obj3;
                            str = F;
                            i2 = 0;
                            break;
                        }
                        Log.m286e("TokenProvisioner", "Duplicate Token Ref Id / Tr Token Id");
                        TokenStatus tokenStatus = new TokenStatus(TokenStatus.DISPOSED, null);
                        F = TokenStatus.DISPOSED;
                        q.ad().updateTokenStatusTA(null, tokenStatus);
                        this.mC.iJ.m560s(this.mEnrollmentId);
                        this.mC.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId);
                        Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                        intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_SYNC_ALL_CARDS);
                        PaymentFrameworkApp.m315a(intent);
                        providerResponseData = null;
                        obj2 = null;
                        str2 = brand;
                        obj = obj3;
                        str = F;
                        i2 = -6;
                        break;
                    }
                    Log.m286e("TokenProvisioner", "unable to get card object from mAccount or db record is null ");
                    if (bp != null) {
                        Log.m287i("TokenProvisioner", "delete record from db ");
                        this.mC.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId);
                    }
                    if (q != null) {
                        Log.m287i("TokenProvisioner", "delete card object");
                        this.mC.iJ.m560s(this.mEnrollmentId);
                    }
                    providerResponseData = null;
                    obj2 = null;
                    str2 = brand;
                    obj = obj3;
                    str = TokenStatus.DISPOSED;
                    i2 = -8;
                    break;
                case 205:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT;
                    this.mC.iJ.m560s(this.mEnrollmentId);
                    this.mC.jJ.m1229d(TokenColumn.ENROLLMENT_ID, this.mEnrollmentId);
                    obj = null;
                    str = str3;
                    providerResponseData = null;
                    obj2 = null;
                    str2 = null;
                    break;
                case 400:
                    i2 = -1;
                    if (provisionResponse == null) {
                        obj = null;
                        str = str3;
                        providerResponseData = null;
                        obj2 = null;
                        str2 = null;
                        break;
                    }
                    ErrorResponseData fa = provisionResponse.fa();
                    if (!(fa == null || fa.getCode() == null)) {
                        brand = fa.getCode();
                        if (ErrorResponseData.ERROR_CODE_PAN_NOT_ELIGIBLE.equals(brand) || ErrorResponseData.ERROR_CODE_INVALID_DATA.equals(brand)) {
                            i2 = -11;
                        } else if (ErrorResponseData.ERROR_CODE_PAN_ALREADY_ENROLLED.equals(brand)) {
                            i2 = -3;
                        } else if (ErrorResponseData.ERROR_CODE_PROVISION_EXCEEDED.equals(brand)) {
                            i2 = -13;
                        } else if (ErrorResponseData.ERROR_CODE_DEVICE_TOKEN_MAX_LIMIT_REACHED.equals(brand)) {
                            i2 = PaymentFramework.RESULT_CODE_FAIL_DEVICE_TOKENS_MAX_LIMIT_REACHED;
                        }
                    }
                    obj = null;
                    str = str3;
                    providerResponseData = null;
                    obj2 = null;
                    str2 = null;
                    break;
                    break;
                case 404:
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    obj = null;
                    str = str3;
                    providerResponseData = null;
                    obj2 = null;
                    str2 = null;
                    break;
                default:
                    i2 = -1;
                    obj = null;
                    str = str3;
                    providerResponseData = null;
                    obj2 = null;
                    str2 = null;
                    break;
            }
            if (i2 != 0) {
                try {
                    Log.m286e("TokenProvisioner", "Provision Token Failed - Error Code = " + i2);
                    if (q != null) {
                        q.ad().updateRequestStatus(new ProviderRequestStatus(3, -1, null));
                    }
                    this.mA.onFail(this.mEnrollmentId, i2, provisionTokenResult);
                    if (!(obj == null || q == null || q.getCardBrand().equals(PaymentFramework.CARD_BRAND_LOYALTY))) {
                        FraudDataCollector x2 = FraudDataCollector.m718x(this.mC.mContext);
                        if (x2 != null) {
                            x2.bs();
                        } else {
                            Log.m285d("TokenProvisioner", "ProvisionCallback:onRequestComplete: cannot get fraud data");
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c("TokenProvisioner", e.getMessage(), e);
                } finally {
                    ResponseDataBuilder.m640a(provisionResponse, provisionTokenResult);
                }
            } else {
                if (q != null) {
                    providerTokenKey2 = null;
                    if (provisionResponse != null) {
                        if (provisionResponse.getResult() != null) {
                            providerTokenKey2 = new ProviderTokenKey(((TokenResponseData) provisionResponse.getResult()).getId());
                        }
                    }
                    q.ad().updateRequestStatus(new ProviderRequestStatus(3, 0, providerTokenKey2));
                }
                this.mA.onSuccess(this.mEnrollmentId, provisionTokenResult);
            }
            ResponseDataBuilder.m640a(provisionResponse, provisionTokenResult);
            if (obj != null) {
                brand = null;
                if (q != null) {
                    brand = q.ac().getTokenId();
                }
                if (obj2 != null) {
                    Log.m287i("TokenProvisioner", "processProvision:Send success report to TR server");
                    this.mC.m331a(null, brand, str, PushMessage.TYPE_PROVISION, Card.m574y(str2), providerResponseData, false);
                    return;
                }
                Log.m287i("TokenProvisioner", "processProvision:Send error report to TR server");
                this.mC.m335b(null, brand, str, PushMessage.TYPE_PROVISION, Card.m574y(str2), providerResponseData, false);
            }
        }
    }

    public TokenProvisioner(Context context, String str, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback) {
        super(context);
        this.mEnrollmentId = str;
        this.mB = provisionTokenInfo;
        this.mA = iProvisionTokenCallback;
    }

    public void process() {
        int i = -1;
        if (this.mEnrollmentId == null || this.mA == null || this.iJ == null) {
            if (this.mEnrollmentId == null) {
                Log.m286e("TokenProvisioner", "Provision Token Failed - Enrollment Id is null");
            }
            if (this.iJ == null) {
                Log.m286e("TokenProvisioner", "Provision Token Failed - Failed to initialize account");
            } else {
                i = -5;
            }
            if (this.mA != null) {
                this.mA.onFail(this.mEnrollmentId, i, null);
                return;
            } else {
                Log.m286e("TokenProvisioner", "Provision Token Failed - Provision Callback is null");
                return;
            }
        }
        Card q = this.iJ.m558q(this.mEnrollmentId);
        if (q == null || q.ac() == null) {
            if (q == null) {
                i = -6;
                Log.m286e("TokenProvisioner", "Provision Token Failed - unable to get card object");
            } else {
                Log.m286e("TokenProvisioner", "Provision Token Failed - token is null");
            }
            this.mA.onFail(this.mEnrollmentId, i, null);
        } else if (q.ac().getTokenId() == null && TokenStatus.PENDING_ENROLLED.equals(q.ac().getTokenStatus())) {
            TokenRecord bp = this.jJ.bp(this.mEnrollmentId);
            if (bp == null) {
                Log.m286e("TokenProvisioner", "Provision Token Failed - unable to find record from db");
                this.iJ.m560s(this.mEnrollmentId);
                this.mA.onFail(this.mEnrollmentId, -8, null);
                return;
            }
            long fA = bp.fA();
            ProviderRequestData provisionRequestDataTA = q.ad().getProvisionRequestDataTA(this.mB);
            FraudDataCollector x = FraudDataCollector.m718x(this.mContext);
            if (!q.getCardBrand().equals(PaymentFramework.CARD_BRAND_LOYALTY)) {
                if (x != null) {
                    x.bs();
                } else {
                    Log.m285d("TokenProvisioner", "process: cannot get fraud data");
                }
            }
            ProvisionRequest a = this.lQ.m1130a(Card.m574y(q.getCardBrand()), RequestDataBuilder.m625a(fA, this.mEnrollmentId, this.mB, provisionRequestDataTA));
            a.bk(this.iJ.m562u(q.getCardBrand()));
            a.m836a(new TokenProvisioner(this, this.mEnrollmentId, this.mA));
            if (!q.getCardBrand().equals(PaymentFramework.CARD_BRAND_LOYALTY) && x != null) {
                x.m720X(this.mEnrollmentId);
            }
        } else {
            Log.m290w("TokenProvisioner", "Provision Token Failed - alrerady provisioned");
            this.mA.onFail(this.mEnrollmentId, -3, null);
        }
    }
}
