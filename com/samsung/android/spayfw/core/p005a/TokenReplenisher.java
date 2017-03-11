package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.core.retry.ReplenishRetryRequester;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.QueryTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ReplenishTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.z */
public class TokenReplenisher extends Processor implements Runnable {
    private static final Map<String, TokenReplenisher> mD;
    private static Handler mHandler;
    protected IPushMessageCallback lS;
    protected String lU;
    protected String mTokenId;

    /* renamed from: com.samsung.android.spayfw.core.a.z.1 */
    class TokenReplenisher extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ Card jL;
        final /* synthetic */ TokenReplenisher mE;

        TokenReplenisher(TokenReplenisher tokenReplenisher, Card card) {
            this.mE = tokenReplenisher;
            this.jL = card;
        }

        public void m539a(int i, Response<TokenResponseData> response) {
            Log.m287i("TokenReplenisher", "onRequestComplete: Token change: code " + i);
            if (this.jL == null) {
                Log.m286e("TokenReplenisher", "Abort Card is Null");
                return;
            }
            ProviderResponseData providerResponseData = null;
            TokenRecord bq = this.mE.jJ.bq(this.mE.mTokenId);
            if (bq == null) {
                Log.m286e("TokenReplenisher", "Abort Record is Null");
                return;
            }
            TokenStatus tokenStatus;
            int i2;
            String str;
            Object obj;
            String tokenStatus2 = this.jL.ac().getTokenStatus();
            Object obj2 = null;
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    tokenStatus = null;
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    str = tokenStatus2;
                    obj = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                    if (Utils.ak(this.mE.mContext)) {
                        ReplenishRetryRequester.m666b(this.mE);
                    } else {
                        ReplenishRetryRequester.m665a(this.mE);
                    }
                    tokenStatus = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    str = tokenStatus2;
                    obj = null;
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    TokenResponseData tokenResponseData = (TokenResponseData) response.getResult();
                    int i3;
                    if (tokenResponseData != null && tokenResponseData.getStatus() != null) {
                        TokenStatus tokenStatus3 = new TokenStatus(ResponseDataBuilder.m635a(tokenResponseData), tokenResponseData.getStatus().getReason());
                        if (!tokenStatus3.getCode().equals(tokenStatus2)) {
                            Object obj3;
                            Log.m287i("TokenReplenisher", "Token Status Changed Before Replenishment");
                            providerResponseData = this.jL.ad().updateTokenStatusTA(tokenResponseData.getData(), tokenResponseData.getStatus());
                            if (providerResponseData == null || providerResponseData.getErrorCode() != 0) {
                                Log.m286e("TokenReplenisher", "updateTokenStatus failed on provider side");
                                obj3 = null;
                            } else {
                                obj3 = 1;
                            }
                            if (obj3 != null) {
                                bq.setTokenStatus(tokenStatus3.getCode());
                                bq.m1251H(tokenStatus3.getReason());
                                this.mE.jJ.m1230d(bq);
                                this.jL.ac().setTokenStatus(bq.getTokenStatus());
                                this.jL.ac().m662H(bq.fy());
                                tokenStatus2 = ResponseDataBuilder.m628F(bq.getTokenStatus());
                                FraudDataCollector x = FraudDataCollector.m718x(this.mE.mContext);
                                if (x != null) {
                                    x.m723k(bq.getTrTokenId(), bq.getTokenStatus());
                                } else {
                                    Log.m285d("TokenReplenisher", "FraudCollector: updateFTokenRecordStatus cannot get data");
                                }
                            }
                            if (tokenStatus2.equals(TokenStatus.SUSPENDED)) {
                                Log.m286e("TokenReplenisher", "TOKEN SUSPENDED");
                                i = -4;
                                obj2 = null;
                                TokenReplenisher.remove(this.mE.mTokenId);
                                tokenStatus = tokenStatus3;
                                i2 = -4;
                                str = tokenStatus2;
                                obj = null;
                                break;
                            }
                        }
                        Log.m287i("TokenReplenisher", "TOKEN ACTIVE ... Proceed with replenishment.");
                        if (tokenResponseData.getData() != null && this.jL.ad().isReplenishDataAvailable(tokenResponseData.getData())) {
                            providerResponseData = this.jL.ad().replenishTokenTA(tokenResponseData.getData(), tokenResponseData.getStatus());
                            if (providerResponseData == null || providerResponseData.getErrorCode() != 0 || tokenStatus3 == null) {
                                if (!Utils.ak(this.mE.mContext)) {
                                    ReplenishRetryRequester.m665a(this.mE);
                                    i3 = 1;
                                    tokenStatus = tokenStatus3;
                                    i2 = 0;
                                    str = tokenStatus2;
                                    obj = null;
                                    break;
                                }
                                ReplenishRetryRequester.m666b(this.mE);
                                i3 = 1;
                                tokenStatus = tokenStatus3;
                                i2 = 0;
                                str = tokenStatus2;
                                obj = null;
                                break;
                            }
                            bq.setTokenStatus(tokenStatus3.getCode());
                            bq.m1251H(tokenStatus3.getReason());
                            this.jL.ac().setTokenStatus(tokenStatus3.getCode());
                            this.jL.ac().m662H(tokenStatus3.getReason());
                            this.mE.jJ.m1230d(bq);
                            str = bq.getTokenStatus();
                            tokenStatus = tokenStatus3;
                            i2 = 0;
                            int i4 = 1;
                            i3 = 1;
                            break;
                        } else if (!this.mE.m549b(this.jL)) {
                            TokenReplenisher.remove(this.mE.mTokenId);
                            return;
                        } else {
                            return;
                        }
                    }
                    Log.m286e("TokenReplenisher", "TokenResponseData or status is null");
                    i3 = 1;
                    tokenStatus = null;
                    i2 = 0;
                    str = tokenStatus2;
                    obj = null;
                    break;
                    break;
                case 404:
                case 410:
                    Log.m290w("TokenReplenisher", "unable to find the token on server. something wrong. deleting the token");
                    TokenStatus tokenStatus4 = new TokenStatus(TokenStatus.DISPOSED, null);
                    tokenStatus2 = TokenStatus.DISPOSED;
                    providerResponseData = this.jL.ad().updateTokenStatusTA(null, tokenStatus4);
                    this.mE.m550g(this.mE.mTokenId, this.jL);
                    TokenReplenisher.remove(this.mE.mTokenId);
                    tokenStatus = tokenStatus4;
                    i2 = -6;
                    str = tokenStatus2;
                    obj = null;
                    break;
                case 500:
                    ReplenishRetryRequester.m666b(this.mE);
                    tokenStatus = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    str = tokenStatus2;
                    obj = null;
                    break;
                case 503:
                    ReplenishRetryRequester.m666b(this.mE);
                    tokenStatus = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    str = tokenStatus2;
                    obj = null;
                    break;
                default:
                    if (i >= 500) {
                        ReplenishRetryRequester.m666b(this.mE);
                    } else {
                        TokenReplenisher.remove(this.mE.mTokenId);
                    }
                    tokenStatus = null;
                    i2 = -1;
                    str = tokenStatus2;
                    obj = null;
                    break;
            }
            if (i2 != 0) {
                Log.m286e("TokenReplenisher", "Replenish Token Failed - Error Code = " + i2);
                this.jL.ad().updateRequestStatus(new ProviderRequestStatus(23, -1, this.jL.ac().aQ()));
            } else {
                this.jL.ad().updateRequestStatus(new ProviderRequestStatus(23, 0, this.jL.ac().aQ()));
            }
            if (obj2 != null) {
                if (obj != null) {
                    this.mE.m331a(null, this.mE.mTokenId, str, PushMessage.TYPE_TOKEN_CHANGE, Card.m574y(this.jL.getCardBrand()), providerResponseData, true);
                } else {
                    Log.m286e("TokenReplenisher", "processTokenChange:Send error report to TR server");
                    this.mE.m335b(null, this.mE.mTokenId, str, PushMessage.TYPE_TOKEN_CHANGE, Card.m574y(this.jL.getCardBrand()), providerResponseData, true);
                }
            }
            if (this.mE.lS != null) {
                if (i2 == -6 || i2 == -4) {
                    try {
                        this.mE.lS.onTokenStatusUpdate(this.mE.lU, this.mE.mTokenId, tokenStatus);
                    } catch (Throwable e) {
                        Log.m284c("TokenReplenisher", e.getMessage(), e);
                    }
                } else if (i2 != 0) {
                    this.mE.lS.onFail(this.mE.lU, i2);
                } else {
                    this.mE.lS.onTokenReplenishRequested(this.mE.lU, this.mE.mTokenId);
                }
                this.mE.lS = null;
            } else if (i == -6 || i == -2 || i == -4) {
                Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                if (i == -6 || i == -4) {
                    intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_SYNC_ALL_CARDS);
                } else if (i == -2) {
                    intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_JWT_TOKEN);
                }
                PaymentFrameworkApp.m315a(intent);
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.z.a */
    private class TokenReplenisher extends C0413a<Response<ReplenishTokenRequestData>, ReplenishTokenRequest> {
        final /* synthetic */ TokenReplenisher mE;
        String mF;
        ProviderTokenKey mProviderTokenKey;
        Card mw;

        public TokenReplenisher(TokenReplenisher tokenReplenisher, String str, Card card, ProviderTokenKey providerTokenKey) {
            this.mE = tokenReplenisher;
            this.mw = null;
            this.mProviderTokenKey = null;
            this.mF = null;
            this.mF = str;
            this.mw = card;
            this.mProviderTokenKey = providerTokenKey;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void m541a(int r15, com.samsung.android.spayfw.remoteservice.Response<com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData> r16) {
            /*
            r14 = this;
            r0 = "TokenReplenisher";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "onRequestComplete:Replenish Request: ";
            r1 = r1.append(r2);
            r1 = r1.append(r15);
            r1 = r1.toString();
            com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
            r0 = r14.mE;
            r0 = r0.iJ;
            r1 = r14.mE;
            r1 = r1.mTokenId;
            r10 = r0.m559r(r1);
            r4 = 0;
            r7 = 0;
            r6 = 0;
            r2 = 0;
            r1 = 0;
            r3 = 0;
            r0 = r14.mw;
            r11 = r0.getCardBrand();
            r5 = "";
            switch(r15) {
                case -2: goto L_0x01f5;
                case 0: goto L_0x01d8;
                case 200: goto L_0x00ae;
                case 202: goto L_0x00ae;
                case 403: goto L_0x01fe;
                case 404: goto L_0x01cb;
                case 503: goto L_0x01cb;
                default: goto L_0x0035;
            };
        L_0x0035:
            r0 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            if (r15 < r0) goto L_0x0227;
        L_0x0039:
            r0 = r14.mE;
            com.samsung.android.spayfw.core.retry.ReplenishRetryRequester.m666b(r0);
        L_0x003e:
            r0 = -1;
            r7 = "TokenReplenisher";
            r8 = "DEFAULT CASE";
            com.samsung.android.spayfw.p002b.Log.m287i(r7, r8);
            r8 = r6;
            r9 = r0;
            r0 = r3;
            r6 = r4;
            r3 = r2;
        L_0x004b:
            if (r9 == 0) goto L_0x0230;
        L_0x004d:
            r2 = "TokenReplenisher";
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r7 = "Replenish Token Failed - Error Code = ";
            r4 = r4.append(r7);
            r4 = r4.append(r9);
            r4 = r4.toString();
            com.samsung.android.spayfw.p002b.Log.m286e(r2, r4);
            if (r10 == 0) goto L_0x007b;
        L_0x0067:
            r2 = new com.samsung.android.spayfw.payprovider.d;
            r4 = 11;
            r7 = -1;
            r12 = r14.mProviderTokenKey;
            r2.<init>(r4, r7, r12);
            r2.ar(r5);
            r4 = r10.ad();
            r4.updateRequestStatus(r2);
        L_0x007b:
            if (r0 == 0) goto L_0x0090;
        L_0x007d:
            if (r1 == 0) goto L_0x0245;
        L_0x007f:
            r0 = r14.mE;
            r1 = 0;
            r2 = r14.mE;
            r2 = r2.mTokenId;
            r4 = "TOKEN_CHANGE";
            r5 = com.samsung.android.spayfw.core.Card.m574y(r11);
            r7 = 1;
            r0.m331a(r1, r2, r3, r4, r5, r6, r7);
        L_0x0090:
            r0 = r14.mE;
            r0 = r0.lS;
            if (r0 == 0) goto L_0x028b;
        L_0x0096:
            r0 = -6;
            if (r9 != r0) goto L_0x025f;
        L_0x0099:
            r0 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r0 = r0.lS;	 Catch:{ RemoteException -> 0x026e }
            r1 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r1 = r1.lU;	 Catch:{ RemoteException -> 0x026e }
            r2 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r2 = r2.mTokenId;	 Catch:{ RemoteException -> 0x026e }
            r0.onTokenStatusUpdate(r1, r2, r8);	 Catch:{ RemoteException -> 0x026e }
        L_0x00a8:
            r0 = r14.mE;
            r1 = 0;
            r0.lS = r1;
        L_0x00ad:
            return;
        L_0x00ae:
            r3 = 1;
            if (r16 != 0) goto L_0x00be;
        L_0x00b1:
            r0 = "TokenReplenisher";
            r8 = "ReplenishTokenRequestData is null";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r8);
            r0 = r3;
            r8 = r6;
            r9 = r7;
            r3 = r2;
            r6 = r4;
            goto L_0x004b;
        L_0x00be:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            if (r0 == 0) goto L_0x00cc;
        L_0x00c6:
            r8 = r0.getStatus();
            if (r8 != 0) goto L_0x00ea;
        L_0x00cc:
            if (r0 != 0) goto L_0x00dc;
        L_0x00ce:
            r0 = "TokenReplenisher";
            r8 = "TokenResponseData is null";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r8);
            r0 = r3;
            r8 = r6;
            r9 = r7;
            r3 = r2;
            r6 = r4;
            goto L_0x004b;
        L_0x00dc:
            r0 = "TokenReplenisher";
            r8 = "TokenResponseData token status is null";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r8);
            r0 = r3;
            r8 = r6;
            r9 = r7;
            r3 = r2;
            r6 = r4;
            goto L_0x004b;
        L_0x00ea:
            r8 = r14.mE;
            r8 = r8.jJ;
            r9 = r14.mE;
            r9 = r9.mTokenId;
            r8 = r8.bq(r9);
            if (r10 == 0) goto L_0x00fa;
        L_0x00f8:
            if (r8 != 0) goto L_0x0133;
        L_0x00fa:
            r0 = "TokenReplenisher";
            r7 = "unable to get card object ";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r7);
            if (r8 == 0) goto L_0x0117;
        L_0x0103:
            r0 = "TokenReplenisher";
            r7 = "delete record from db ";
            com.samsung.android.spayfw.p002b.Log.m287i(r0, r7);
            r0 = r14.mE;
            r0 = r0.jJ;
            r7 = com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn.ENROLLMENT_ID;
            r8 = r8.getEnrollmentId();
            r0.m1229d(r7, r8);
        L_0x0117:
            if (r10 == 0) goto L_0x012b;
        L_0x0119:
            r0 = "TokenReplenisher";
            r7 = "delete card object";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r7);
            r0 = r14.mE;
            r0 = r0.iJ;
            r7 = r14.mE;
            r7 = r7.mTokenId;
            r0.m561t(r7);
        L_0x012b:
            r0 = -6;
            r8 = r6;
            r9 = r0;
            r0 = r3;
            r6 = r4;
            r3 = r2;
            goto L_0x004b;
        L_0x0133:
            r6 = new com.samsung.android.spayfw.appinterface.TokenStatus;
            r2 = com.samsung.android.spayfw.core.ResponseDataBuilder.m635a(r0);
            r9 = r0.getStatus();
            r9 = r9.getReason();
            r6.<init>(r2, r9);
            r2 = r6.getCode();
            r9 = r0.getData();
            if (r9 == 0) goto L_0x01bc;
        L_0x014e:
            r9 = r10.ad();
            r12 = r0.getData();
            r9 = r9.isReplenishDataAvailable(r12);
            if (r9 == 0) goto L_0x01bc;
        L_0x015c:
            r4 = r10.ad();
            r9 = r0.getData();
            r0 = r0.getStatus();
            r4 = r4.replenishTokenTA(r9, r0);
            if (r4 == 0) goto L_0x02bd;
        L_0x016e:
            r0 = r4.getErrorCode();
            if (r0 != 0) goto L_0x02bd;
        L_0x0174:
            r0 = r6.getCode();
            r8.setTokenStatus(r0);
            r0 = r6.getReason();
            r8.m1251H(r0);
            r0 = r10.ac();
            r1 = r6.getCode();
            r0.setTokenStatus(r1);
            r0 = r10.ac();
            r1 = r6.getReason();
            r0.m662H(r1);
            r0 = r14.mE;
            r0 = r0.jJ;
            r0.m1230d(r8);
            r1 = r8.getTokenStatus();
            r0 = 1;
        L_0x01a4:
            r2 = r14.mE;
            r2 = r2.mTokenId;
            com.samsung.android.spayfw.core.p005a.TokenChangeChecker.remove(r2);
            r2 = r1;
            r1 = r0;
            r0 = r3;
            r3 = r4;
        L_0x01af:
            r4 = r14.mE;
            r4 = r4.mTokenId;
            com.samsung.android.spayfw.core.p005a.TokenReplenisher.remove(r4);
            r8 = r6;
            r9 = r7;
            r6 = r3;
            r3 = r2;
            goto L_0x004b;
        L_0x01bc:
            r0 = 0;
            r3 = r14.mE;
            r3 = r3.mContext;
            r8 = r14.mF;
            r12 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
            com.samsung.android.spayfw.core.p005a.TokenChangeChecker.m523a(r3, r8, r12);
            r3 = r4;
            goto L_0x01af;
        L_0x01cb:
            r0 = r14.mE;
            com.samsung.android.spayfw.core.retry.ReplenishRetryRequester.m666b(r0);
            r0 = -1;
            r8 = r6;
            r9 = r0;
            r0 = r3;
            r6 = r4;
            r3 = r2;
            goto L_0x004b;
        L_0x01d8:
            r0 = r14.mE;
            r0 = r0.mContext;
            r0 = com.samsung.android.spayfw.utils.Utils.ak(r0);
            if (r0 == 0) goto L_0x01ef;
        L_0x01e2:
            r0 = r14.mE;
            com.samsung.android.spayfw.core.retry.ReplenishRetryRequester.m666b(r0);
        L_0x01e7:
            r0 = -1;
            r8 = r6;
            r9 = r0;
            r0 = r3;
            r6 = r4;
            r3 = r2;
            goto L_0x004b;
        L_0x01ef:
            r0 = r14.mE;
            com.samsung.android.spayfw.core.retry.ReplenishRetryRequester.m665a(r0);
            goto L_0x01e7;
        L_0x01f5:
            r0 = -206; // 0xffffffffffffff32 float:NaN double:NaN;
            r8 = r6;
            r9 = r0;
            r0 = r3;
            r6 = r4;
            r3 = r2;
            goto L_0x004b;
        L_0x01fe:
            r7 = -202; // 0xffffffffffffff36 float:NaN double:NaN;
            r0 = r16.fa();
            if (r0 == 0) goto L_0x02ba;
        L_0x0206:
            r8 = r0.getCode();
            if (r8 == 0) goto L_0x02ba;
        L_0x020c:
            r0 = r0.getCode();
            r8 = "403.5";
            r8 = r8.equals(r0);
            if (r8 == 0) goto L_0x02ba;
        L_0x0218:
            r5 = r14.mE;
            r5 = r5.mTokenId;
            com.samsung.android.spayfw.core.p005a.TokenReplenisher.remove(r5);
            r5 = r0;
            r8 = r6;
            r9 = r7;
            r0 = r3;
            r6 = r4;
            r3 = r2;
            goto L_0x004b;
        L_0x0227:
            r0 = r14.mE;
            r0 = r0.mTokenId;
            com.samsung.android.spayfw.core.p005a.TokenReplenisher.remove(r0);
            goto L_0x003e;
        L_0x0230:
            if (r10 == 0) goto L_0x007b;
        L_0x0232:
            r2 = new com.samsung.android.spayfw.payprovider.d;
            r4 = 11;
            r5 = 0;
            r7 = r14.mProviderTokenKey;
            r2.<init>(r4, r5, r7);
            r4 = r10.ad();
            r4.updateRequestStatus(r2);
            goto L_0x007b;
        L_0x0245:
            r0 = "TokenReplenisher";
            r1 = "processTokenChange:Send error report to TR server";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
            r0 = r14.mE;
            r1 = 0;
            r2 = r14.mE;
            r2 = r2.mTokenId;
            r4 = "TOKEN_CHANGE";
            r5 = com.samsung.android.spayfw.core.Card.m574y(r11);
            r7 = 1;
            r0.m335b(r1, r2, r3, r4, r5, r6, r7);
            goto L_0x0090;
        L_0x025f:
            if (r9 == 0) goto L_0x027a;
        L_0x0261:
            r0 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r0 = r0.lS;	 Catch:{ RemoteException -> 0x026e }
            r1 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r1 = r1.lU;	 Catch:{ RemoteException -> 0x026e }
            r0.onFail(r1, r9);	 Catch:{ RemoteException -> 0x026e }
            goto L_0x00a8;
        L_0x026e:
            r0 = move-exception;
            r1 = "TokenReplenisher";
            r2 = r0.getMessage();
            com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
            goto L_0x00a8;
        L_0x027a:
            r0 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r0 = r0.lS;	 Catch:{ RemoteException -> 0x026e }
            r1 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r1 = r1.lU;	 Catch:{ RemoteException -> 0x026e }
            r2 = r14.mE;	 Catch:{ RemoteException -> 0x026e }
            r2 = r2.mTokenId;	 Catch:{ RemoteException -> 0x026e }
            r0.onTokenReplenishRequested(r1, r2);	 Catch:{ RemoteException -> 0x026e }
            goto L_0x00a8;
        L_0x028b:
            r0 = new android.content.Intent;
            r1 = "com.samsung.android.spayfw.action.notification";
            r0.<init>(r1);
            r1 = -6;
            if (r15 == r1) goto L_0x0298;
        L_0x0295:
            r1 = -2;
            if (r15 != r1) goto L_0x02b2;
        L_0x0298:
            r1 = -6;
            if (r15 != r1) goto L_0x02a7;
        L_0x029b:
            r1 = "notiType";
            r2 = "syncAllCards";
            r0.putExtra(r1, r2);
        L_0x02a2:
            com.samsung.android.spayfw.core.PaymentFrameworkApp.m315a(r0);
            goto L_0x00ad;
        L_0x02a7:
            r1 = -2;
            if (r15 != r1) goto L_0x02a2;
        L_0x02aa:
            r1 = "notiType";
            r2 = "updateJwtToken";
            r0.putExtra(r1, r2);
            goto L_0x02a2;
        L_0x02b2:
            r1 = "notiType";
            r2 = "syncAllCards";
            r0.putExtra(r1, r2);
            goto L_0x02a2;
        L_0x02ba:
            r0 = r5;
            goto L_0x0218;
        L_0x02bd:
            r0 = r1;
            r1 = r2;
            goto L_0x01a4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.z.a.a(int, com.samsung.android.spayfw.remoteservice.c):void");
        }

        public void m542a(int i, ServerCertificates serverCertificates, ReplenishTokenRequest replenishTokenRequest) {
            Object obj = 1;
            Log.m290w("TokenReplenisher", "onCertsReceived: called for Replenish");
            Card r = this.mE.iJ.m559r(this.mF);
            if (r == null) {
                Log.m286e("TokenReplenisher", "TokenReplenisher : unable to get Card object :" + this.mF);
                try {
                    if (this.mE.lS != null) {
                        this.mE.lS.onFail(this.mE.lU, -6);
                    }
                } catch (Throwable e) {
                    Log.m284c("TokenReplenisher", e.getMessage(), e);
                }
                this.mE.lS = null;
                return;
            }
            if (this.mE.m334a(r.getEnrollmentId(), r, serverCertificates)) {
                ProviderRequestData replenishmentRequestDataTA = r.ad().getReplenishmentRequestDataTA();
                if (replenishmentRequestDataTA == null || replenishmentRequestDataTA.getErrorCode() != 0) {
                    Log.m286e("TokenReplenisher", " unable to get replenish data from pay provider");
                } else {
                    ((ReplenishTokenRequestData) replenishTokenRequest.eT()).setData(replenishmentRequestDataTA.ch());
                    replenishTokenRequest.bf(this.mE.m329P(this.mw.getCardBrand()));
                    replenishTokenRequest.m836a((C0413a) this);
                    Log.m287i("TokenReplenisher", "replenish request successfully sent after server cert update");
                    obj = null;
                }
            } else {
                Log.m286e("TokenReplenisher", "Server certificate update failed.Replenishment aborted");
            }
            if (obj != null && this.mE.lS != null) {
                try {
                    this.mE.lS.onFail(this.mE.lU, -1);
                } catch (Throwable e2) {
                    Log.m284c("TokenReplenisher", e2.getMessage(), e2);
                }
                this.mE.lS = null;
            }
        }
    }

    static {
        mD = new HashMap();
        HandlerThread handlerThread = new HandlerThread("PaymentFramework");
        Log.m285d("TokenReplenisher", "TokenReplenisher Thread is Started");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static synchronized TokenReplenisher m547b(Context context, String str) {
        TokenReplenisher tokenReplenisher;
        synchronized (TokenReplenisher.class) {
            TokenChangeChecker S = TokenChangeChecker.m522S(str);
            if (S == null || ((long) S.mr) >= 10) {
                if (S != null) {
                    Log.m290w("TokenReplenisher", "Removing Token Change Checker");
                    TokenChangeChecker.remove(str);
                } else {
                    Log.m290w("TokenReplenisher", "No Token Change Checker");
                }
                if (((TokenReplenisher) mD.get(str)) == null) {
                    Log.m287i("TokenReplenisher", "New Instance of Token Replenisher");
                    tokenReplenisher = new TokenReplenisher(context, str);
                    mD.put(str, tokenReplenisher);
                } else {
                    Log.m287i("TokenReplenisher", "Token Replenisher Pending. Do not start replenish request.");
                    tokenReplenisher = null;
                }
            } else {
                Log.m290w("TokenReplenisher", "Token Change Checker Pending. Do not start replenish request.");
                tokenReplenisher = null;
            }
        }
        return tokenReplenisher;
    }

    public static synchronized TokenReplenisher m548b(Context context, String str, IPushMessageCallback iPushMessageCallback, String str2) {
        TokenReplenisher tokenReplenisher;
        synchronized (TokenReplenisher.class) {
            TokenChangeChecker S = TokenChangeChecker.m522S(str);
            if (S == null || ((long) S.mr) >= 10) {
                Log.m290w("TokenReplenisher", "Removing Token Change Checker");
                TokenChangeChecker.remove(str);
                tokenReplenisher = (TokenReplenisher) mD.get(str);
                if (tokenReplenisher == null) {
                    Log.m287i("TokenReplenisher", "New Instance of Token Replenisher");
                    tokenReplenisher = new TokenReplenisher(context, str, iPushMessageCallback, str2);
                    mD.put(str, tokenReplenisher);
                } else {
                    Log.m287i("TokenReplenisher", "Update Instance of Token Replenisher");
                    tokenReplenisher.lU = str2;
                    tokenReplenisher.lS = iPushMessageCallback;
                }
            } else {
                Log.m290w("TokenReplenisher", "Token Change Checker Pending. Do not start replenish request.");
                tokenReplenisher = null;
            }
        }
        return tokenReplenisher;
    }

    public static synchronized void remove(String str) {
        synchronized (TokenReplenisher.class) {
            Log.m287i("TokenReplenisher", "Remove Instance of Token Replenisher");
            TokenReplenisher.getHandler().removeCallbacks((TokenReplenisher) mD.remove(str));
        }
    }

    private TokenReplenisher(Context context, String str) {
        super(context);
        this.mTokenId = str;
    }

    private TokenReplenisher(Context context, String str, IPushMessageCallback iPushMessageCallback, String str2) {
        super(context);
        this.mTokenId = str;
        this.lS = iPushMessageCallback;
        this.lU = str2;
    }

    public void process() {
        mHandler.post(this);
    }

    public void run() {
        Log.m287i("TokenReplenisher", "Entered replenish Request");
        Log.m285d("TokenReplenisher", "Entered replenish Request: tokenId " + this.mTokenId);
        Card r = this.iJ.m559r(this.mTokenId);
        if (r == null) {
            Log.m286e("TokenReplenisher", " unable to get card based on tokenId. ignore replenish request");
            return;
        }
        QueryTokenRequest x = this.lQ.m1141x(Card.m574y(r.getCardBrand()), this.mTokenId);
        x.m1205h(false);
        x.bk(this.iJ.m562u(r.getCardBrand()));
        x.m839b(new TokenReplenisher(this, r));
    }

    public String getTokenId() {
        return this.mTokenId;
    }

    private boolean m549b(Card card) {
        Log.m287i("TokenReplenisher", "startReplenishRequest");
        ProviderTokenKey aQ = card.ac().aQ();
        ProviderRequestData replenishmentRequestDataTA = card.ad().getReplenishmentRequestDataTA();
        if (replenishmentRequestDataTA == null || replenishmentRequestDataTA.getErrorCode() != 0) {
            Log.m286e("TokenReplenisher", " unable to get replenish data from pay provider");
            return false;
        }
        ReplenishTokenRequestData replenishTokenRequestData = new ReplenishTokenRequestData(this.mTokenId);
        if (replenishmentRequestDataTA.ch() != null) {
            replenishTokenRequestData.setData(replenishmentRequestDataTA.ch());
            Log.m285d("TokenReplenisher", "replenishRequestData:" + replenishmentRequestDataTA.ch().toString());
        } else {
            Log.m290w("TokenReplenisher", "replenishRequestData:replenishRequstData is null ");
        }
        ReplenishTokenRequest a = this.lQ.m1131a(Card.m574y(card.getCardBrand()), card.ac().getTokenId(), replenishTokenRequestData);
        a.setCardBrand(Card.m574y(card.getCardBrand()));
        a.bf(m329P(card.getCardBrand()));
        a.m839b(new TokenReplenisher(this, this.mTokenId, card, aQ));
        return true;
    }

    private void m550g(String str, Card card) {
        Log.m285d("TokenReplenisher", "deleting token id = " + str);
        if (this.jJ.m1229d(TokenColumn.TR_TOKEN_ID, str) < 1) {
            Log.m286e("TokenReplenisher", "Not able to delete Token from DB");
        }
        this.iJ.m560s(card.getEnrollmentId());
    }
}
