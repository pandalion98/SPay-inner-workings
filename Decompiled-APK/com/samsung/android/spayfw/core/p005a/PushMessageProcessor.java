package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.QueryTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.List;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.p */
public class PushMessageProcessor extends Processor {
    protected PushMessage kU;
    protected IPushMessageCallback lS;
    protected Integer lT;
    private String lU;

    /* renamed from: com.samsung.android.spayfw.core.a.p.1 */
    class PushMessageProcessor extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ String lV;
        final /* synthetic */ String lW;
        final /* synthetic */ PushMessageProcessor lX;

        PushMessageProcessor(PushMessageProcessor pushMessageProcessor, String str, String str2) {
            this.lX = pushMessageProcessor;
            this.lV = str;
            this.lW = str2;
        }

        public void m478a(int i, Response<TokenResponseData> response) {
            int i2;
            boolean z;
            ProviderResponseData providerResponseData;
            String str;
            TokenMetaData tokenMetaData;
            TokenStatus tokenStatus;
            boolean z2 = true;
            Log.m285d("PushMessageProcessor", "onRequestComplete: Asset change: code " + i);
            Card r = this.lX.iJ.m559r(this.lV);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    z2 = false;
                    z = false;
                    providerResponseData = null;
                    str = null;
                    tokenMetaData = null;
                    tokenStatus = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    z2 = false;
                    z = false;
                    providerResponseData = null;
                    str = null;
                    tokenMetaData = null;
                    tokenStatus = null;
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (r == null) {
                        z = false;
                        providerResponseData = null;
                        str = null;
                        tokenMetaData = null;
                        i2 = 0;
                        tokenStatus = null;
                        break;
                    }
                    TokenMetaData tokenMetaData2;
                    boolean z3;
                    str = r.ac().getTokenStatus();
                    TokenResponseData tokenResponseData = (TokenResponseData) response.getResult();
                    if (tokenResponseData != null) {
                        TokenMetaData metadata;
                        Token a = ResponseDataBuilder.m632a(this.lX.mContext, r, tokenResponseData);
                        if (a != null) {
                            metadata = a.getMetadata();
                        } else {
                            metadata = null;
                        }
                        if (!(a == null || a.getMetadata() == null)) {
                            ResponseDataBuilder.m637a(this.lX.mContext, a.getMetadata(), r);
                        }
                        tokenMetaData2 = metadata;
                        z3 = true;
                    } else {
                        z3 = false;
                        Object obj = null;
                    }
                    providerResponseData = null;
                    tokenMetaData = tokenMetaData2;
                    tokenStatus = null;
                    z = z3;
                    i2 = 0;
                    break;
                case 404:
                case 410:
                    ProviderResponseData updateTokenStatusTA;
                    Log.m290w("PushMessageProcessor", "unable to find the token. something wrong. deleting the token");
                    TokenStatus tokenStatus2 = new TokenStatus(TokenStatus.DISPOSED, null);
                    str = TokenStatus.DISPOSED;
                    if (r != null) {
                        updateTokenStatusTA = r.ad().updateTokenStatusTA(null, tokenStatus2);
                        this.lX.m484Q(this.lV);
                        if (updateTokenStatusTA == null || updateTokenStatusTA.getErrorCode() != 0) {
                            z2 = false;
                        }
                    } else {
                        z2 = false;
                        updateTokenStatusTA = null;
                    }
                    providerResponseData = updateTokenStatusTA;
                    tokenMetaData = null;
                    tokenStatus = tokenStatus2;
                    z = z2;
                    i2 = -6;
                    z2 = false;
                    break;
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    z2 = false;
                    z = false;
                    providerResponseData = null;
                    str = null;
                    tokenMetaData = null;
                    tokenStatus = null;
                    break;
                default:
                    i2 = -1;
                    z2 = false;
                    z = false;
                    providerResponseData = null;
                    str = null;
                    tokenMetaData = null;
                    tokenStatus = null;
                    break;
            }
            if (i2 == -6) {
                try {
                    this.lX.lS.onTokenStatusUpdate(this.lX.kU.getNotificationId(), this.lV, tokenStatus);
                } catch (Throwable e) {
                    Log.m284c("PushMessageProcessor", e.getMessage(), e);
                    if (response != null) {
                        ResponseDataBuilder.m642a((TokenResponseData) response.getResult(), tokenMetaData);
                    } else {
                        ResponseDataBuilder.m642a(null, tokenMetaData);
                    }
                } catch (Throwable e2) {
                    Throwable th = e2;
                    if (response != null) {
                        ResponseDataBuilder.m642a((TokenResponseData) response.getResult(), tokenMetaData);
                    } else {
                        ResponseDataBuilder.m642a(null, tokenMetaData);
                    }
                }
            } else if (i2 != 0) {
                this.lX.lS.onFail(this.lX.kU.getNotificationId(), i2);
            } else {
                this.lX.lS.onTokenMetaDataUpdate(this.lX.lU, this.lV, tokenMetaData);
            }
            if (response != null) {
                ResponseDataBuilder.m642a((TokenResponseData) response.getResult(), tokenMetaData);
            } else {
                ResponseDataBuilder.m642a(null, tokenMetaData);
            }
            if (!z2) {
                return;
            }
            if (z) {
                this.lX.m331a(this.lX.kU.getNotificationId(), this.lV, str, PushMessage.TYPE_ASSET_CHANGE, Card.m574y(this.lW), providerResponseData, false);
                return;
            }
            Log.m286e("PushMessageProcessor", "processAssetChange:Send error report to TR server");
            this.lX.m335b(this.lX.kU.getNotificationId(), this.lV, str, PushMessage.TYPE_ASSET_CHANGE, Card.m574y(this.lW), providerResponseData, false);
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.p.2 */
    class PushMessageProcessor extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ Card jL;
        final /* synthetic */ String lV;
        final /* synthetic */ PushMessageProcessor lX;
        final /* synthetic */ String lY;

        PushMessageProcessor(PushMessageProcessor pushMessageProcessor, String str, Card card, String str2) {
            this.lX = pushMessageProcessor;
            this.lV = str;
            this.jL = card;
            this.lY = str2;
        }

        public void m480a(int r15, com.samsung.android.spayfw.remoteservice.Response<com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData> r16) {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.samsung.android.spayfw.core.a.p.2.a(int, com.samsung.android.spayfw.remoteservice.c):void. bs: [B:39:0x0133, B:91:0x0348]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:57)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r14 = this;
            r0 = "PushMessageProcessor";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "processProvision::onRequestComplete: code = ";
            r1 = r1.append(r2);
            r1 = r1.append(r15);
            r1 = r1.toString();
            com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
            r0 = r14.lX;
            r0 = r0.iJ;
            r1 = r14.lV;
            r9 = r0.m559r(r1);
            if (r9 != 0) goto L_0x008d;
        L_0x0024:
            r0 = "PushMessageProcessor";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "processProvision : unable to get Card object :";
            r1 = r1.append(r2);
            r2 = r14.lV;
            r1 = r1.append(r2);
            r1 = r1.toString();
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
            r0 = new com.samsung.android.spayfw.appinterface.TokenStatus;	 Catch:{ RemoteException -> 0x0065 }
            r1 = "DISPOSED";	 Catch:{ RemoteException -> 0x0065 }
            r2 = 0;	 Catch:{ RemoteException -> 0x0065 }
            r0.<init>(r1, r2);	 Catch:{ RemoteException -> 0x0065 }
            r1 = r14.lX;	 Catch:{ RemoteException -> 0x0065 }
            r1 = r1.lS;	 Catch:{ RemoteException -> 0x0065 }
            r2 = r14.lX;	 Catch:{ RemoteException -> 0x0065 }
            r2 = r2.kU;	 Catch:{ RemoteException -> 0x0065 }
            r2 = r2.getNotificationId();	 Catch:{ RemoteException -> 0x0065 }
            r3 = r14.lV;	 Catch:{ RemoteException -> 0x0065 }
            r1.onTokenStatusUpdate(r2, r3, r0);	 Catch:{ RemoteException -> 0x0065 }
            r0 = 0;
            if (r16 == 0) goto L_0x0060;
        L_0x005a:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
        L_0x0060:
            r1 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
        L_0x0064:
            return;
        L_0x0065:
            r0 = move-exception;
            r1 = "PushMessageProcessor";	 Catch:{ all -> 0x007d }
            r2 = r0.getMessage();	 Catch:{ all -> 0x007d }
            com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);	 Catch:{ all -> 0x007d }
            r0 = 0;
            if (r16 == 0) goto L_0x0078;
        L_0x0072:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
        L_0x0078:
            r1 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
            goto L_0x0064;
        L_0x007d:
            r0 = move-exception;
            r1 = r0;
            r0 = 0;
            if (r16 == 0) goto L_0x0088;
        L_0x0082:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
        L_0x0088:
            r2 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r2);
            throw r1;
        L_0x008d:
            r7 = 0;
            r6 = "PENDING";
            r5 = 0;
            r1 = 0;
            r3 = 0;
            r2 = -1;
            r10 = r9.getCardBrand();
            r0 = "ACTIVE";
            r4 = r9.ac();
            r4 = r4.getTokenStatus();
            r0 = r0.equals(r4);
            if (r0 == 0) goto L_0x0127;
        L_0x00a8:
            r0 = "PushMessageProcessor";
            r1 = "processProvision: token creation already happend on SDK. just send report ";
            com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
            r0 = r14.lX;
            r1 = r14.lX;
            r1 = r1.kU;
            r1 = r1.getNotificationId();
            r2 = r14.lV;
            r3 = r9.ac();
            r3 = r3.getTokenStatus();
            r4 = "PROVISION";
            r5 = r14.jL;
            r5 = r5.getCardBrand();
            r5 = com.samsung.android.spayfw.core.Card.m574y(r5);
            r6 = 0;
            r7 = 0;
            r0.m331a(r1, r2, r3, r4, r5, r6, r7);
            r0 = r14.lX;	 Catch:{ RemoteException -> 0x0104 }
            r0 = r0.lS;	 Catch:{ RemoteException -> 0x0104 }
            r1 = r14.lX;	 Catch:{ RemoteException -> 0x0104 }
            r1 = r1.lU;	 Catch:{ RemoteException -> 0x0104 }
            r2 = r14.lV;	 Catch:{ RemoteException -> 0x0104 }
            r3 = new com.samsung.android.spayfw.appinterface.TokenStatus;	 Catch:{ RemoteException -> 0x0104 }
            r4 = r9.ac();	 Catch:{ RemoteException -> 0x0104 }
            r4 = r4.getTokenStatus();	 Catch:{ RemoteException -> 0x0104 }
            r5 = r9.ac();	 Catch:{ RemoteException -> 0x0104 }
            r5 = r5.aP();	 Catch:{ RemoteException -> 0x0104 }
            r3.<init>(r4, r5);	 Catch:{ RemoteException -> 0x0104 }
            r0.onTokenStatusUpdate(r1, r2, r3);	 Catch:{ RemoteException -> 0x0104 }
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r1 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
            goto L_0x0064;
        L_0x0104:
            r0 = move-exception;
            r1 = "PushMessageProcessor";	 Catch:{ all -> 0x011a }
            r2 = r0.getMessage();	 Catch:{ all -> 0x011a }
            com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);	 Catch:{ all -> 0x011a }
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r1 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
            goto L_0x0064;
        L_0x011a:
            r0 = move-exception;
            r1 = r0;
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r2 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r2);
            throw r1;
        L_0x0127:
            switch(r15) {
                case -2: goto L_0x0306;
                case 0: goto L_0x02fd;
                case 200: goto L_0x017a;
                case 404: goto L_0x02c9;
                case 410: goto L_0x02c9;
                case 500: goto L_0x02f4;
                case 503: goto L_0x02fd;
                default: goto L_0x012a;
            };
        L_0x012a:
            r0 = -1;
            r2 = r1;
            r4 = r5;
            r1 = r3;
            r3 = r6;
            r6 = r7;
        L_0x0130:
            r5 = 0;
            if (r0 == 0) goto L_0x0336;
        L_0x0133:
            r1 = r14.lX;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r1 = r1.lS;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r7 = r14.lX;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r7 = r7.lU;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r1.onFail(r7, r0);	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r0 = r14.lX;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r0 = r0.mContext;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r0 = com.samsung.android.spayfw.fraud.FraudDataCollector.m718x(r0);	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            if (r0 == 0) goto L_0x030f;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
        L_0x014a:
            r0.bs();	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
        L_0x014d:
            if (r16 == 0) goto L_0x015e;
        L_0x014f:
            if (r5 == 0) goto L_0x03b6;
        L_0x0151:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r1 = r5.getMetadata();
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
        L_0x015e:
            if (r2 == 0) goto L_0x0064;
        L_0x0160:
            if (r4 == 0) goto L_0x03d9;
        L_0x0162:
            r0 = r14.lX;
            r1 = r14.lX;
            r1 = r1.kU;
            r1 = r1.getNotificationId();
            r2 = r14.lV;
            r4 = "PROVISION";
            r5 = com.samsung.android.spayfw.core.Card.m574y(r10);
            r7 = 0;
            r0.m331a(r1, r2, r3, r4, r5, r6, r7);
            goto L_0x0064;
        L_0x017a:
            r4 = 1;
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r8 = 0;
            r1 = "PushMessageProcessor";
            r11 = new java.lang.StringBuilder;
            r11.<init>();
            r12 = "Provision Data : ";
            r11 = r11.append(r12);
            r12 = r0.getData();
            r11 = r11.append(r12);
            r11 = r11.toString();
            com.samsung.android.spayfw.p002b.Log.m285d(r1, r11);
            r1 = r14.lX;
            r1 = r1.jJ;
            r11 = r14.lV;
            r11 = r1.bq(r11);
            if (r11 != 0) goto L_0x01da;
        L_0x01aa:
            r0 = "PushMessageProcessor";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "processProvision : unable to get Card object from db :";
            r1 = r1.append(r2);
            r2 = r14.lV;
            r1 = r1.append(r2);
            r1 = r1.toString();
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
            r0 = r14.lX;
            r0 = r0.iJ;
            r1 = r9.getEnrollmentId();
            r0.m560s(r1);
            r0 = -6;
            r1 = "DISPOSED";
            r2 = r4;
            r6 = r7;
            r4 = r5;
            r13 = r1;
            r1 = r3;
            r3 = r13;
            goto L_0x0130;
        L_0x01da:
            r1 = r14.jL;
            r1 = r1.ac();
            if (r1 == 0) goto L_0x0230;
        L_0x01e2:
            r1 = r14.jL;
            r1 = r1.ac();
            r1 = r1.aQ();
            if (r1 == 0) goto L_0x0230;
        L_0x01ee:
            r1 = "PushMessageProcessor";
            r3 = "processProvision: token creation already happend on SDK. just update status";
            com.samsung.android.spayfw.p002b.Log.m285d(r1, r3);
            r1 = r14.jL;
            r1 = r1.ac();
            r3 = r1.aQ();
            r1 = 1;
            r7 = r14.jL;
            r7 = r7.ad();
            r8 = r0.getData();
            r9 = r0.getStatus();
            r7 = r7.updateTokenStatusTA(r8, r9);
        L_0x0212:
            if (r7 == 0) goto L_0x02bb;
        L_0x0214:
            r8 = r7.getErrorCode();
            if (r8 != 0) goto L_0x02bb;
        L_0x021a:
            if (r1 != 0) goto L_0x03fc;
        L_0x021c:
            r2 = r7.getProviderTokenKey();
        L_0x0220:
            if (r2 != 0) goto L_0x0259;
        L_0x0222:
            r0 = "PushMessageProcessor";
            r2 = "Provision Token- onRequestComplete: provider not returning tokenref ";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
            r0 = -1;
            r2 = r4;
            r3 = r6;
            r4 = r5;
            r6 = r7;
            goto L_0x0130;
        L_0x0230:
            r7 = new com.samsung.android.spayfw.payprovider.c;
            r7.<init>();
            r1 = r0.getData();
            r7.m822a(r1);
            r1 = r16.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = com.samsung.android.spayfw.core.ResponseDataBuilder.m643b(r1);
            r7.m823e(r1);
            r1 = r14.jL;
            r1 = r1.ad();
            r9 = r14.lV;
            r12 = 2;
            r7 = r1.createTokenTA(r9, r7, r12);
            r1 = r3;
            r3 = r8;
            goto L_0x0212;
        L_0x0259:
            r3 = com.samsung.android.spayfw.core.ResponseDataBuilder.m634a(r11, r0, r2);
            r0 = r14.jL;
            r0 = r0.ac();
            r5 = r3.getTokenStatus();
            r0.setTokenStatus(r5);
            r0 = r14.jL;
            r0 = r0.ac();
            r0.m663c(r2);
            r0 = r14.jL;
            r0 = r0.ac();
            r2 = r3.fy();
            r0.m662H(r2);
            r0 = r14.jL;
            r2 = r3.ab();
            r0.m577j(r2);
            r0 = r14.lX;
            r0 = r0.jJ;
            r0.m1230d(r3);
            r0 = 0;
            r2 = 1;
            r3 = r3.getTokenStatus();
            r3 = com.samsung.android.spayfw.core.ResponseDataBuilder.m628F(r3);
            r5 = r14.lX;
            r5 = r5.mContext;
            r5 = com.samsung.android.spayfw.fraud.FraudDataCollector.m718x(r5);
            if (r5 == 0) goto L_0x02b3;
        L_0x02a4:
            r6 = r14.lV;
            if (r6 == 0) goto L_0x02b3;
        L_0x02a8:
            r6 = r14.lV;
            r5.m724l(r6, r3);
        L_0x02ad:
            r6 = r7;
            r13 = r2;
            r2 = r4;
            r4 = r13;
            goto L_0x0130;
        L_0x02b3:
            r5 = "PushMessageProcessor";
            r6 = "FraudCollector: storeTokenEnrollmentSuccess cannot get data";
            com.samsung.android.spayfw.p002b.Log.m285d(r5, r6);
            goto L_0x02ad;
        L_0x02bb:
            r0 = "PushMessageProcessor";
            r3 = "Provider unable to store provision info";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r3);
            r0 = r2;
            r3 = r6;
            r2 = r4;
            r6 = r7;
            r4 = r5;
            goto L_0x0130;
        L_0x02c9:
            r0 = "PushMessageProcessor";
            r2 = "unable to find the token. something wrong. deleting the token";
            com.samsung.android.spayfw.p002b.Log.m290w(r0, r2);
            r0 = -6;
            r4 = new com.samsung.android.spayfw.appinterface.TokenStatus;
            r2 = "DISPOSED";
            r6 = 0;
            r4.<init>(r2, r6);
            r2 = "DISPOSED";
            r6 = r14.jL;
            r6 = r6.ad();
            r7 = 0;
            r6 = r6.updateTokenStatusTA(r7, r4);
            r4 = r14.lX;
            r7 = r14.lV;
            r4.m484Q(r7);
            r4 = r5;
            r13 = r1;
            r1 = r3;
            r3 = r2;
            r2 = r13;
            goto L_0x0130;
        L_0x02f4:
            r0 = -205; // 0xffffffffffffff33 float:NaN double:NaN;
            r2 = r1;
            r4 = r5;
            r1 = r3;
            r3 = r6;
            r6 = r7;
            goto L_0x0130;
        L_0x02fd:
            r0 = -201; // 0xffffffffffffff37 float:NaN double:NaN;
            r2 = r1;
            r4 = r5;
            r1 = r3;
            r3 = r6;
            r6 = r7;
            goto L_0x0130;
        L_0x0306:
            r0 = -206; // 0xffffffffffffff32 float:NaN double:NaN;
            r2 = r1;
            r4 = r5;
            r1 = r3;
            r3 = r6;
            r6 = r7;
            goto L_0x0130;
        L_0x030f:
            r0 = "PushMessageProcessor";	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r1 = "FraudCollector: TokenEnrollmentFailed cannot get data";	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            goto L_0x014d;
        L_0x0318:
            r0 = move-exception;
            r1 = r5;
        L_0x031a:
            r5 = "PushMessageProcessor";	 Catch:{ all -> 0x03f8 }
            r7 = r0.getMessage();	 Catch:{ all -> 0x03f8 }
            com.samsung.android.spayfw.p002b.Log.m284c(r5, r7, r0);	 Catch:{ all -> 0x03f8 }
            if (r16 == 0) goto L_0x015e;
        L_0x0325:
            if (r1 == 0) goto L_0x03c2;
        L_0x0327:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r1 = r1.getMetadata();
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
            goto L_0x015e;
        L_0x0336:
            r0 = r14.lX;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r7 = r0.mContext;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r8 = r14.jL;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r0 = r16.getResult();	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            r5 = com.samsung.android.spayfw.core.ResponseDataBuilder.m632a(r7, r8, r0);	 Catch:{ Exception -> 0x0318, all -> 0x03a2 }
            if (r5 == 0) goto L_0x035b;
        L_0x0348:
            r0 = r5.getMetadata();	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            if (r0 == 0) goto L_0x035b;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
        L_0x034e:
            r0 = r14.lX;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r0 = r0.mContext;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r7 = r5.getMetadata();	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r8 = r14.jL;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            com.samsung.android.spayfw.core.ResponseDataBuilder.m637a(r0, r7, r8);	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
        L_0x035b:
            if (r1 != 0) goto L_0x0378;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
        L_0x035d:
            r0 = "PushMessageProcessor";	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r1 = "On Create Token";	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r0 = r14.lX;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r0 = r0.lS;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r1 = r14.lX;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r1 = r1.lU;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r7 = r14.lY;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r0.onCreateToken(r1, r7, r5);	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            goto L_0x014d;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
        L_0x0375:
            r0 = move-exception;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r1 = r5;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            goto L_0x031a;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
        L_0x0378:
            r0 = r14.lX;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r0 = r0.lS;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r1 = r14.lX;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r1 = r1.lU;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r7 = r14.lV;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r8 = new com.samsung.android.spayfw.appinterface.TokenStatus;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r9 = r14.jL;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r9 = r9.ac();	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r9 = r9.getTokenStatus();	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r11 = r14.jL;	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r11 = r11.ac();	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r11 = r11.aP();	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r8.<init>(r9, r11);	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            r0.onTokenStatusUpdate(r1, r7, r8);	 Catch:{ Exception -> 0x0375, all -> 0x03a2 }
            goto L_0x014d;
        L_0x03a2:
            r0 = move-exception;
            r1 = r0;
        L_0x03a4:
            if (r16 == 0) goto L_0x03b5;
        L_0x03a6:
            if (r5 == 0) goto L_0x03ce;
        L_0x03a8:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r2 = r5.getMetadata();
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r2);
        L_0x03b5:
            throw r1;
        L_0x03b6:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r1 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
            goto L_0x015e;
        L_0x03c2:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r1 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r1);
            goto L_0x015e;
        L_0x03ce:
            r0 = r16.getResult();
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r0;
            r2 = 0;
            com.samsung.android.spayfw.core.ResponseDataBuilder.m642a(r0, r2);
            goto L_0x03b5;
        L_0x03d9:
            r0 = "PushMessageProcessor";
            r1 = "processProvision:Send error report to TR server";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
            r0 = r14.lX;
            r1 = r14.lX;
            r1 = r1.kU;
            r1 = r1.getNotificationId();
            r2 = r14.lV;
            r4 = "PROVISION";
            r5 = com.samsung.android.spayfw.core.Card.m574y(r10);
            r7 = 0;
            r0.m335b(r1, r2, r3, r4, r5, r6, r7);
            goto L_0x0064;
        L_0x03f8:
            r0 = move-exception;
            r5 = r1;
            r1 = r0;
            goto L_0x03a4;
        L_0x03fc:
            r2 = r3;
            goto L_0x0220;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.p.2.a(int, com.samsung.android.spayfw.remoteservice.c):void");
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.p.3 */
    class PushMessageProcessor extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ Card jL;
        final /* synthetic */ String lV;
        final /* synthetic */ String lW;
        final /* synthetic */ PushMessageProcessor lX;

        PushMessageProcessor(PushMessageProcessor pushMessageProcessor, String str, Card card, String str2) {
            this.lX = pushMessageProcessor;
            this.lV = str;
            this.jL = card;
            this.lW = str2;
        }

        public void m482a(int i, Response<TokenResponseData> response) {
            String tokenStatus;
            boolean z;
            int i2;
            ProviderResponseData providerResponseData = null;
            boolean z2 = true;
            Log.m285d("PushMessageProcessor", "onRequestComplete: Status change: code " + i);
            Card r = this.lX.iJ.m559r(this.lV);
            TokenRecord bq = this.lX.jJ.bq(this.lV);
            TokenStatus tokenStatus2;
            if (bq != null && r != null) {
                tokenStatus = bq.getTokenStatus();
                switch (i) {
                    case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                        z2 = false;
                        z = false;
                        i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                        tokenStatus2 = null;
                        break;
                    case ECCurve.COORD_AFFINE /*0*/:
                    case 503:
                        z2 = false;
                        z = false;
                        i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                        tokenStatus2 = null;
                        break;
                    case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                        TokenResponseData tokenResponseData = (TokenResponseData) response.getResult();
                        if (tokenResponseData != null && tokenResponseData.getStatus() != null) {
                            if (r.ac() != null && r.ac().aQ() != null) {
                                providerResponseData = r.ad().updateTokenStatusTA(tokenResponseData.getData(), tokenResponseData.getStatus());
                                if (providerResponseData == null || providerResponseData.getErrorCode() != 0) {
                                    Log.m286e("PushMessageProcessor", "updateTokenStatus failed on provider side");
                                    z = false;
                                } else {
                                    z = true;
                                }
                                TokenStatus tokenStatus3 = new TokenStatus(ResponseDataBuilder.m635a(tokenResponseData), tokenResponseData.getStatus().getReason());
                                if (!z) {
                                    tokenStatus2 = tokenStatus3;
                                    i2 = 0;
                                    break;
                                }
                                bq.setTokenStatus(tokenStatus3.getCode());
                                bq.m1251H(tokenStatus3.getReason());
                                this.lX.jJ.m1230d(bq);
                                r.ac().setTokenStatus(bq.getTokenStatus());
                                r.ac().m662H(bq.fy());
                                tokenStatus = ResponseDataBuilder.m628F(bq.getTokenStatus());
                                FraudDataCollector x = FraudDataCollector.m718x(this.lX.mContext);
                                if (x != null) {
                                    x.m723k(bq.getTrTokenId(), bq.getTokenStatus());
                                } else {
                                    Log.m285d("PushMessageProcessor", "FraudCollector: updateFTokenRecordStatus cannot get data");
                                }
                                tokenStatus2 = tokenStatus3;
                                i2 = 0;
                                break;
                            }
                            Log.m287i("PushMessageProcessor", "Token Ref id is null. no need to inform to provider");
                            z = false;
                            tokenStatus2 = null;
                            i2 = 0;
                            break;
                        }
                        Log.m286e("PushMessageProcessor", "TokenResponseData or status is null");
                        z = false;
                        tokenStatus2 = null;
                        i2 = 0;
                        break;
                        break;
                    case 404:
                    case 410:
                        Log.m290w("PushMessageProcessor", "unable to find the token on server. something wrong. deleting the token");
                        tokenStatus2 = new TokenStatus(TokenStatus.DISPOSED, null);
                        tokenStatus = TokenStatus.DISPOSED;
                        providerResponseData = this.jL.ad().updateTokenStatusTA(null, tokenStatus2);
                        if (providerResponseData != null && providerResponseData.getErrorCode() == 0) {
                            z = true;
                            i2 = 0;
                            break;
                        }
                        Log.m286e("PushMessageProcessor", "updateTokenStatus failed on provider side");
                        z = false;
                        i2 = 0;
                        break;
                        break;
                    case 500:
                        z2 = false;
                        z = false;
                        i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                        tokenStatus2 = null;
                        break;
                    default:
                        z2 = false;
                        z = false;
                        i2 = -1;
                        tokenStatus2 = null;
                        break;
                }
            }
            if (r == null) {
                Log.m286e("PushMessageProcessor", "processStatusChange : unable to get Card object :" + this.lV);
            } else {
                Log.m286e("PushMessageProcessor", "processStatusChange: munable to get Card object from db :" + this.lV);
            }
            tokenStatus2 = new TokenStatus(TokenStatus.DISPOSED, null);
            tokenStatus = TokenStatus.DISPOSED;
            z = false;
            i2 = 0;
            if (i2 != 0) {
                try {
                    this.lX.lS.onFail(this.lX.lU, i2);
                } catch (Throwable e) {
                    Log.m284c("PushMessageProcessor", e.getMessage(), e);
                }
            } else {
                this.lX.lS.onTokenStatusUpdate(this.lX.lU, this.lV, tokenStatus2);
            }
            if (z2) {
                if (z) {
                    this.lX.m331a(this.lX.kU.getNotificationId(), this.lV, tokenStatus, PushMessage.TYPE_STATUS_CHANGE, Card.m574y(this.lW), providerResponseData, false);
                } else {
                    Log.m286e("PushMessageProcessor", "error happend during token status change. report to server");
                    this.lX.m335b(this.lX.kU.getNotificationId(), this.lV, tokenStatus, PushMessage.TYPE_STATUS_CHANGE, Card.m574y(this.lW), providerResponseData, false);
                }
            }
            if (TokenStatus.DISPOSED.equals(tokenStatus)) {
                this.lX.m484Q(this.lV);
            }
        }
    }

    public PushMessageProcessor(Context context, PushMessage pushMessage, Integer num, IPushMessageCallback iPushMessageCallback) {
        super(context);
        this.lU = null;
        this.kU = pushMessage;
        this.lS = iPushMessageCallback;
        this.lT = num;
    }

    public void process() {
        int i = -5;
        if (this.lS == null) {
            Log.m286e("PushMessageProcessor", "Callback is NULL");
        } else if (this.kU == null || this.kU.getMessage() == null || this.kU.getCategory() == null) {
            Log.m286e("PushMessageProcessor", "pushMessage is null/empty/undefined category");
            r0 = null;
            if (!(this.kU == null || this.kU.getMessage() == null || this.kU.getNotificationId() == null)) {
                r0 = this.kU.getNotificationId();
            }
            try {
                this.lS.onFail(r0, -5);
            } catch (Throwable e) {
                Log.m284c("PushMessageProcessor", e.getMessage(), e);
            }
        } else {
            this.lU = this.kU.getNotificationId();
            String category = this.kU.getCategory();
            if (PushMessage.CATEGORY_CARD.equals(category)) {
                r0 = this.kU.getCardEvent();
                Log.m287i("PushMessageProcessor", "Push Event : " + r0);
                if (PushMessage.TYPE_ENROLL_CC.equals(r0)) {
                    bd();
                } else if (PushMessage.TYPE_UPDATE_CC.equals(r0)) {
                    be();
                } else {
                    Log.m286e("PushMessageProcessor", "processPushMessage::process - Invalid event type = " + r0);
                    try {
                        this.lS.onFail(this.lU, -5);
                    } catch (Throwable e2) {
                        Log.m284c("PushMessageProcessor", e2.getMessage(), e2);
                    }
                }
            } else if (PushMessage.CATEGORY_TOKEN.equals(category)) {
                category = this.kU.getTokenId();
                String enrollmentId = this.kU.getEnrollmentId();
                Log.m285d("PushMessageProcessor", "processPushMessage- process: " + this.kU.toString());
                if (this.iJ == null || this.jJ == null || category == null) {
                    if (category != null) {
                        i = -1;
                        Log.m286e("PushMessageProcessor", "Internal error - Account/DB null");
                    }
                    try {
                        this.lS.onFail(this.lU, i);
                        return;
                    } catch (Throwable e22) {
                        Log.m284c("PushMessageProcessor", e22.getMessage(), e22);
                        return;
                    }
                }
                Card r;
                if (this.iJ.m559r(category) != null) {
                    r = this.iJ.m559r(category);
                } else if (enrollmentId == null || enrollmentId.isEmpty()) {
                    Log.m286e("PushMessageProcessor", "processPushMessage- unable to find card for given enrollment id and TokenId");
                    try {
                        this.lS.onFail(this.lU, -6);
                        return;
                    } catch (Throwable e222) {
                        Log.m284c("PushMessageProcessor", e222.getMessage(), e222);
                        return;
                    }
                } else {
                    r = this.iJ.m558q(enrollmentId);
                }
                if (r == null) {
                    try {
                        this.lS.onFail(this.lU, -5);
                    } catch (Throwable e2222) {
                        Log.m284c("PushMessageProcessor", e2222.getMessage(), e2222);
                    }
                    Log.m286e("PushMessageProcessor", "processPushMessage- unable to get card object");
                    return;
                }
                String tokenEvent = this.kU.getTokenEvent();
                Log.m287i("PushMessageProcessor", "Push Event : " + tokenEvent);
                if (PushMessage.TYPE_PROVISION.equals(tokenEvent)) {
                    m488a(enrollmentId, category, r);
                } else if (PushMessage.TYPE_STATUS_CHANGE.equals(tokenEvent)) {
                    m490c(category, r);
                } else if (PushMessage.TYPE_REPLENISH.equals(tokenEvent)) {
                    m489b(category, r);
                } else if (PushMessage.TYPE_TOKEN_CHANGE.equals(tokenEvent)) {
                    m491d(category, r);
                } else if (PushMessage.TYPE_TRANSACTION.equals(tokenEvent)) {
                    m492e(category, r);
                } else if (PushMessage.TYPE_ASSET_CHANGE.equals(tokenEvent)) {
                    m487a(category, r);
                } else {
                    Log.m286e("PushMessageProcessor", "processPushMessage::process - Invalid event type = " + tokenEvent);
                    try {
                        this.lS.onFail(this.lU, -5);
                    } catch (Throwable e22222) {
                        Log.m284c("PushMessageProcessor", e22222.getMessage(), e22222);
                    }
                }
            }
        }
    }

    private void m487a(String str, Card card) {
        this.lQ.m1141x(Card.m574y(card.getCardBrand()), str).m836a(new PushMessageProcessor(this, str, card.getCardBrand()));
    }

    private void m488a(String str, String str2, Card card) {
        String cardBrand = card.getCardBrand();
        if (cardBrand == null || cardBrand.isEmpty()) {
            Log.m286e("PushMessageProcessor", "unable to find card brand");
            try {
                this.lS.onFail(this.lU, -5);
                return;
            } catch (Throwable e) {
                Log.m284c("PushMessageProcessor", e.getMessage(), e);
                return;
            }
        }
        String tokenStatus = card.ac().getTokenStatus();
        if (!TokenStatus.PENDING_ENROLLED.equals(tokenStatus) || this.lT == null) {
            if (TokenStatus.ACTIVE.equals(tokenStatus)) {
                Log.m285d("PushMessageProcessor", "processProvision: token creation already happened on SDK. just send report ");
                m331a(this.kU.getNotificationId(), str2, tokenStatus, PushMessage.TYPE_PROVISION, Card.m574y(card.getCardBrand()), null, false);
                try {
                    this.lS.onTokenStatusUpdate(this.lU, str2, new TokenStatus(tokenStatus, card.ac().aP()));
                    return;
                } catch (Throwable e2) {
                    Log.m284c("PushMessageProcessor", e2.getMessage(), e2);
                    return;
                }
            }
            this.lQ.m1141x(Card.m574y(cardBrand), str2).m836a(new PushMessageProcessor(this, str2, card, str));
        } else if (this.lT.intValue() > 13) {
            Log.m285d("PushMessageProcessor", "processProvision: Max attemps exceeded; something went wrong, aborting!");
            try {
                this.lS.onFail(this.lU, -1);
            } catch (Throwable e22) {
                Log.m284c("PushMessageProcessor", e22.getMessage(), e22);
            }
        } else {
            long intValue = 1000 * ((long) this.lT.intValue());
            this.lT = Integer.valueOf(this.lT.intValue() + 1);
            PaymentFrameworkApp.az().sendMessageDelayed(PaymentFrameworkMessage.m621a(9, this.kU, this.lT, this.lS), intValue);
            Log.m287i("PushMessageProcessor", "received push before complete provisionToken : delay process count   " + this.lT);
        }
    }

    private void m489b(String str, Card card) {
        ProviderRequestData replenishmentRequestDataTA = card.ad().getReplenishmentRequestDataTA();
        if (replenishmentRequestDataTA == null || replenishmentRequestDataTA.getErrorCode() != 0) {
            try {
                this.lS.onFail(this.lU, -36);
            } catch (Throwable e) {
                Log.m284c("PushMessageProcessor", e.getMessage(), e);
            }
            Log.m286e("PushMessageProcessor", " unable to get replenish data from pay provider");
            return;
        }
        try {
            TokenReplenisher b = TokenReplenisher.m548b(this.mContext, str, this.lS, this.lU);
            if (b != null) {
                b.process();
            }
        } catch (Throwable e2) {
            Log.m284c("PushMessageProcessor", e2.getMessage(), e2);
        }
    }

    private void m490c(String str, Card card) {
        String cardBrand = card.getCardBrand();
        QueryTokenRequest x = this.lQ.m1141x(Card.m574y(card.getCardBrand()), str);
        x.m1205h(false);
        x.m836a(new PushMessageProcessor(this, str, card, cardBrand));
    }

    private void m484Q(String str) {
        Log.m285d("PushMessageProcessor", "deleting token id = " + str);
        if (this.jJ.m1229d(TokenColumn.TR_TOKEN_ID, str) < 1) {
            Log.m286e("PushMessageProcessor", "Not able to delete Token from DB");
        }
        try {
            this.iJ.m561t(str);
        } catch (Exception e) {
            Log.m286e("PushMessageProcessor", e.getMessage());
        }
    }

    private void m491d(String str, Card card) {
        if (TokenChangeChecker.m522S(str) != null) {
            Log.m290w("PushMessageProcessor", "Token Change Checker Pending. Update.");
            TokenChangeChecker.m524a(this.mContext, str, this.lS, this.lU);
            return;
        }
        Log.m290w("PushMessageProcessor", "Ignore Token Change Event");
    }

    private void m492e(String str, Card card) {
        new aa(this.mContext, str, this.kU, this.lS).process();
    }

    private void bd() {
        try {
            new CashCardProcessor(this.mContext, this.kU, this.lS).process();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void be() {
        List c = this.jJ.m1228c(TokenColumn.CASH_CARD_ID, this.kU.getCardNumber());
        Log.m285d("PushMessageProcessor", "Card Id : " + this.kU.getCardNumber());
        if (c != null && c.size() > 0) {
            Log.m285d("PushMessageProcessor", "TokenRecord Size : " + c.size());
            TokenRecord tokenRecord = (TokenRecord) c.get(0);
            Log.m285d("PushMessageProcessor", "Token Id : " + tokenRecord.getTrTokenId());
            if (tokenRecord.getTrTokenId() != null) {
                new aa(this.mContext, tokenRecord.getTrTokenId(), this.kU, this.lS).process();
                return;
            }
        }
        Log.m287i("PushMessageProcessor", "No Card with Id : " + this.kU.getCardNumber() + ". Proceed with " + PushMessage.JSON_KEY_ENROLLMENT);
        bd();
    }
}
