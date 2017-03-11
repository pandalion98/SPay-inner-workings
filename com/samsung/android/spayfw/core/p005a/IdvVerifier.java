package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.RequestDataBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.VerifyIdvRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;

/* renamed from: com.samsung.android.spayfw.core.a.k */
public class IdvVerifier extends Processor {
    protected IVerifyIdvCallback lr;
    protected VerifyIdvInfo ls;
    protected String mEnrollmentId;

    /* renamed from: com.samsung.android.spayfw.core.a.k.a */
    private class IdvVerifier extends C0413a<Response<TokenResponseData>, VerifyIdvRequest> {
        IVerifyIdvCallback lr;
        final /* synthetic */ IdvVerifier lt;
        String mEnrollmentId;

        public IdvVerifier(IdvVerifier idvVerifier, String str, IVerifyIdvCallback iVerifyIdvCallback) {
            this.lt = idvVerifier;
            this.mEnrollmentId = str;
            this.lr = iVerifyIdvCallback;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void m427a(int r18, com.samsung.android.spayfw.remoteservice.Response<com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData> r19) {
            /*
            r17 = this;
            r1 = "IdvVerifier";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "VerifyIdv : onRequestComplete:  ";
            r2 = r2.append(r3);
            r0 = r18;
            r2 = r2.append(r0);
            r2 = r2.toString();
            com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
            r9 = 0;
            r8 = 0;
            r6 = 0;
            r7 = 0;
            r5 = 0;
            r4 = 0;
            r3 = 0;
            r2 = 0;
            switch(r18) {
                case -2: goto L_0x03f2;
                case 0: goto L_0x03d6;
                case 201: goto L_0x0051;
                case 202: goto L_0x01e2;
                case 205: goto L_0x03ad;
                case 400: goto L_0x0400;
                case 403: goto L_0x0346;
                case 404: goto L_0x03e4;
                case 407: goto L_0x0346;
                case 500: goto L_0x03e4;
                case 503: goto L_0x03d6;
                default: goto L_0x0025;
            };
        L_0x0025:
            r1 = -1;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
        L_0x0030:
            if (r8 != 0) goto L_0x0427;
        L_0x0032:
            r0 = r17;
            r8 = r0.lr;	 Catch:{ RemoteException -> 0x0434 }
            r0 = r17;
            r9 = r0.mEnrollmentId;	 Catch:{ RemoteException -> 0x0434 }
            r8.onSuccess(r9, r1);	 Catch:{ RemoteException -> 0x0434 }
        L_0x003d:
            if (r5 == 0) goto L_0x0050;
        L_0x003f:
            if (r2 == 0) goto L_0x0440;
        L_0x0041:
            r0 = r17;
            r1 = r0.lt;
            r2 = 0;
            r5 = "STATUS_CHANGE";
            r6 = com.samsung.android.spayfw.core.Card.m574y(r6);
            r8 = 0;
            r1.m331a(r2, r3, r4, r5, r6, r7, r8);
        L_0x0050:
            return;
        L_0x0051:
            r10 = 0;
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.iJ;
            r0 = r17;
            r11 = r0.mEnrollmentId;
            r11 = r1.m558q(r11);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.jJ;
            r0 = r17;
            r12 = r0.mEnrollmentId;
            r12 = r1.bp(r12);
            if (r11 == 0) goto L_0x0072;
        L_0x0070:
            if (r12 != 0) goto L_0x00b4;
        L_0x0072:
            r1 = "IdvVerifier";
            r9 = "unable to get card object ";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r9);
            if (r12 == 0) goto L_0x0091;
        L_0x007b:
            r1 = "IdvVerifier";
            r9 = "delete record from db ";
            com.samsung.android.spayfw.p002b.Log.m287i(r1, r9);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.jJ;
            r9 = com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn.ENROLLMENT_ID;
            r0 = r17;
            r10 = r0.mEnrollmentId;
            r1.m1229d(r9, r10);
        L_0x0091:
            if (r11 == 0) goto L_0x00a7;
        L_0x0093:
            r1 = "IdvVerifier";
            r9 = "delete card object";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r9);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.iJ;
            r0 = r17;
            r9 = r0.mEnrollmentId;
            r1.m560s(r9);
        L_0x00a7:
            r1 = -8;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x00b4:
            if (r19 == 0) goto L_0x00c8;
        L_0x00b6:
            r1 = r19.getResult();
            if (r1 == 0) goto L_0x00c8;
        L_0x00bc:
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = r1.getId();
            if (r1 != 0) goto L_0x00d8;
        L_0x00c8:
            r1 = "IdvVerifier";
            r10 = "VerifyIdv - onRequestComplete: resultData/TrTokenId is null";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r10);
            r1 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r9;
            goto L_0x0030;
        L_0x00d8:
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r13 = r1.getId();
            r14 = new com.samsung.android.spayfw.core.q;
            r14.<init>();
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = r1.getId();
            r14.setTokenId(r1);
            r11.m576a(r14);
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = r1.getData();
            if (r1 == 0) goto L_0x014b;
        L_0x0103:
            r14 = new com.samsung.android.spayfw.payprovider.c;
            r14.<init>();
            r14.m822a(r1);
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = com.samsung.android.spayfw.core.ResponseDataBuilder.m643b(r1);
            r14.m823e(r1);
            r1 = r11.ad();
            r15 = 3;
            r1 = r1.createTokenTA(r13, r14, r15);
            if (r1 == 0) goto L_0x046d;
        L_0x0123:
            r1 = r1.getProviderTokenKey();
        L_0x0127:
            if (r1 != 0) goto L_0x013d;
        L_0x0129:
            r1 = "IdvVerifier";
            r9 = "Provision Token- onRequestComplete: provider not returning tokenref";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r9);
            r1 = -1;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x013d:
            r2 = r1.cn();
            r12.setTokenRefId(r2);
            r2 = r11.ac();
            r2.m663c(r1);
        L_0x014b:
            r0 = r17;
            r1 = r0.lt;
            r2 = r1.mContext;
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = com.samsung.android.spayfw.core.ResponseDataBuilder.m632a(r2, r11, r1);
            if (r1 == 0) goto L_0x0170;
        L_0x015d:
            r2 = r1.getMetadata();
            if (r2 == 0) goto L_0x0170;
        L_0x0163:
            r0 = r17;
            r2 = r0.lt;
            r2 = r2.mContext;
            r10 = r1.getMetadata();
            com.samsung.android.spayfw.core.ResponseDataBuilder.m637a(r2, r10, r11);
        L_0x0170:
            r12.setTrTokenId(r13);
            r0 = r17;
            r2 = r0.lt;
            r2 = r2.m333a(r12);
            if (r2 == 0) goto L_0x01d1;
        L_0x017d:
            r5 = 0;
            r6 = -6;
            r2 = "IdvVerifier";
            r7 = "Duplicate Token Ref Id / Tr Token Id";
            com.samsung.android.spayfw.p002b.Log.m286e(r2, r7);
            r7 = new com.samsung.android.spayfw.appinterface.TokenStatus;
            r2 = "DISPOSED";
            r9 = 0;
            r7.<init>(r2, r9);
            r2 = "DISPOSED";
            r9 = r11.ad();
            r10 = 0;
            r9.updateTokenStatusTA(r10, r7);
            r0 = r17;
            r7 = r0.lt;
            r7 = r7.iJ;
            r0 = r17;
            r9 = r0.mEnrollmentId;
            r7.m560s(r9);
            r0 = r17;
            r7 = r0.lt;
            r7 = r7.jJ;
            r9 = com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn.ENROLLMENT_ID;
            r0 = r17;
            r10 = r0.mEnrollmentId;
            r7.m1229d(r9, r10);
            r7 = 0;
            r9 = new android.content.Intent;
            r10 = "com.samsung.android.spayfw.action.notification";
            r9.<init>(r10);
            r10 = "notiType";
            r11 = "syncAllCards";
            r9.putExtra(r10, r11);
            com.samsung.android.spayfw.core.PaymentFrameworkApp.m315a(r9);
            r16 = r3;
            r3 = r4;
            r4 = r2;
            r2 = r5;
            r5 = r8;
            r8 = r6;
            r6 = r16;
            goto L_0x0030;
        L_0x01d1:
            r0 = r17;
            r2 = r0.lt;
            r2 = r2.jJ;
            r2.m1230d(r12);
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r9;
            goto L_0x0030;
        L_0x01e2:
            if (r19 == 0) goto L_0x01f6;
        L_0x01e4:
            r1 = r19.getResult();
            if (r1 == 0) goto L_0x01f6;
        L_0x01ea:
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = r1.getStatus();
            if (r1 != 0) goto L_0x0206;
        L_0x01f6:
            r1 = "IdvVerifier";
            r10 = "VerifyIdv::onRequestComplete: resultData/status is null";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r10);
            r1 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r9;
            goto L_0x0030;
        L_0x0206:
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.iJ;
            r0 = r17;
            r10 = r0.mEnrollmentId;
            r10 = r1.m558q(r10);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.jJ;
            r0 = r17;
            r11 = r0.mEnrollmentId;
            r11 = r1.bp(r11);
            if (r10 == 0) goto L_0x0226;
        L_0x0224:
            if (r11 != 0) goto L_0x0268;
        L_0x0226:
            r1 = "IdvVerifier";
            r9 = "unable to get card object ";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r9);
            if (r11 == 0) goto L_0x0245;
        L_0x022f:
            r1 = "IdvVerifier";
            r9 = "delete record from db ";
            com.samsung.android.spayfw.p002b.Log.m287i(r1, r9);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.jJ;
            r9 = com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn.ENROLLMENT_ID;
            r0 = r17;
            r11 = r0.mEnrollmentId;
            r1.m1229d(r9, r11);
        L_0x0245:
            if (r10 == 0) goto L_0x025b;
        L_0x0247:
            r1 = "IdvVerifier";
            r9 = "delete card object";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r9);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.iJ;
            r0 = r17;
            r9 = r0.mEnrollmentId;
            r1.m560s(r9);
        L_0x025b:
            r1 = -8;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x0268:
            r8 = 1;
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r4 = r1.getId();
            r3 = r10.getCardBrand();
            r12 = r10.ac();
            if (r12 == 0) goto L_0x0287;
        L_0x027d:
            r12 = r10.ac();
            r12 = r12.aQ();
            if (r12 != 0) goto L_0x031d;
        L_0x0287:
            r12 = "IdvVerifier";
            r13 = "VerifyIdv::onRequestComplete: Token Ref id is null. no need to inform to provider";
            com.samsung.android.spayfw.p002b.Log.m287i(r12, r13);
        L_0x028e:
            if (r6 == 0) goto L_0x0464;
        L_0x0290:
            r2 = new com.samsung.android.spayfw.appinterface.TokenStatus;
            r5 = com.samsung.android.spayfw.core.ResponseDataBuilder.m635a(r1);
            r1 = r1.getStatus();
            r1 = r1.getReason();
            r2.<init>(r5, r1);
            r1 = r2.getCode();
            r11.setTokenStatus(r1);
            r1 = r2.getReason();
            r11.m1251H(r1);
            r0 = r17;
            r1 = r0.lt;
            r1 = r1.jJ;
            r1.m1230d(r11);
            r1 = r10.ac();
            r2 = r11.getTokenStatus();
            r1.setTokenStatus(r2);
            r1 = r10.ac();
            r2 = r11.fy();
            r1.m662H(r2);
            r1 = r11.getTokenStatus();
            r2 = com.samsung.android.spayfw.core.ResponseDataBuilder.m628F(r1);
            r0 = r17;
            r1 = r0.lt;
            r5 = r1.mContext;
            r1 = r19.getResult();
            r1 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData) r1;
            r1 = com.samsung.android.spayfw.core.ResponseDataBuilder.m632a(r5, r10, r1);
            if (r1 == 0) goto L_0x02fb;
        L_0x02e8:
            r5 = r1.getMetadata();
            if (r5 == 0) goto L_0x02fb;
        L_0x02ee:
            r0 = r17;
            r5 = r0.lt;
            r5 = r5.mContext;
            r12 = r1.getMetadata();
            com.samsung.android.spayfw.core.ResponseDataBuilder.m637a(r5, r12, r10);
        L_0x02fb:
            r0 = r17;
            r5 = r0.lt;
            r5 = r5.mContext;
            r5 = com.samsung.android.spayfw.fraud.FraudDataCollector.m718x(r5);
            if (r5 == 0) goto L_0x033e;
        L_0x0307:
            r10 = r11.getTrTokenId();
            r11 = r11.getTokenStatus();
            r5.m723k(r10, r11);
        L_0x0312:
            r5 = r8;
            r8 = r9;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r16;
            goto L_0x0030;
        L_0x031d:
            r7 = r10.ad();
            r12 = 0;
            r13 = r1.getStatus();
            r7 = r7.updateTokenStatusTA(r12, r13);
            if (r7 == 0) goto L_0x0332;
        L_0x032c:
            r12 = r7.getErrorCode();
            if (r12 == 0) goto L_0x033b;
        L_0x0332:
            r12 = "IdvVerifier";
            r13 = "VerifyIdv::onRequestComplete: updateTokenStatus failed on provider side";
            com.samsung.android.spayfw.p002b.Log.m286e(r12, r13);
            goto L_0x028e;
        L_0x033b:
            r6 = 1;
            goto L_0x028e;
        L_0x033e:
            r5 = "IdvVerifier";
            r10 = "VerifyIdv::onRequestComplete: updateFTokenRecordStatus cannot get data";
            com.samsung.android.spayfw.p002b.Log.m285d(r5, r10);
            goto L_0x0312;
        L_0x0346:
            r1 = -17;
            r9 = r19.fa();
            if (r9 == 0) goto L_0x0458;
        L_0x034e:
            r10 = r9.getCode();
            if (r10 == 0) goto L_0x0458;
        L_0x0354:
            r9 = r9.getCode();
            r10 = "407.3";
            r10 = r10.equals(r9);
            if (r10 != 0) goto L_0x0368;
        L_0x0360:
            r10 = "407.2";
            r10 = r10.equals(r9);
            if (r10 == 0) goto L_0x0376;
        L_0x0368:
            r1 = -17;
        L_0x036a:
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x0376:
            r10 = "407.1";
            r10 = r10.equals(r9);
            if (r10 == 0) goto L_0x0381;
        L_0x037e:
            r1 = -16;
            goto L_0x036a;
        L_0x0381:
            r10 = "403.2";
            r10 = r10.equals(r9);
            if (r10 == 0) goto L_0x038c;
        L_0x0389:
            r1 = -18;
            goto L_0x036a;
        L_0x038c:
            r10 = "403.3";
            r10 = r10.equals(r9);
            if (r10 == 0) goto L_0x0397;
        L_0x0394:
            r1 = -19;
            goto L_0x036a;
        L_0x0397:
            r10 = "403.6";
            r10 = r10.equals(r9);
            if (r10 == 0) goto L_0x03a2;
        L_0x039f:
            r1 = -20;
            goto L_0x036a;
        L_0x03a2:
            r10 = "403.7";
            r9 = r10.equals(r9);
            if (r9 == 0) goto L_0x036a;
        L_0x03aa:
            r1 = -21;
            goto L_0x036a;
        L_0x03ad:
            r1 = -8;
            r0 = r17;
            r9 = r0.lt;
            r9 = r9.iJ;
            r0 = r17;
            r10 = r0.mEnrollmentId;
            r9.m560s(r10);
            r0 = r17;
            r9 = r0.lt;
            r9 = r9.jJ;
            r10 = com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn.ENROLLMENT_ID;
            r0 = r17;
            r11 = r0.mEnrollmentId;
            r9.m1229d(r10, r11);
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x03d6:
            r1 = -201; // 0xffffffffffffff37 float:NaN double:NaN;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x03e4:
            r1 = -205; // 0xffffffffffffff33 float:NaN double:NaN;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x03f2:
            r1 = -206; // 0xffffffffffffff32 float:NaN double:NaN;
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x0400:
            r1 = -1;
            r9 = r19.fa();
            if (r9 == 0) goto L_0x0458;
        L_0x0407:
            r10 = r9.getCode();
            if (r10 == 0) goto L_0x0458;
        L_0x040d:
            r9 = r9.getCode();
            r10 = "403.1";
            r9 = r10.equals(r9);
            if (r9 == 0) goto L_0x041b;
        L_0x0419:
            r1 = -202; // 0xffffffffffffff36 float:NaN double:NaN;
        L_0x041b:
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x0427:
            r0 = r17;
            r9 = r0.lr;	 Catch:{ RemoteException -> 0x0434 }
            r0 = r17;
            r10 = r0.mEnrollmentId;	 Catch:{ RemoteException -> 0x0434 }
            r9.onFail(r10, r8, r1);	 Catch:{ RemoteException -> 0x0434 }
            goto L_0x003d;
        L_0x0434:
            r1 = move-exception;
            r8 = "IdvVerifier";
            r9 = r1.getMessage();
            com.samsung.android.spayfw.p002b.Log.m284c(r8, r9, r1);
            goto L_0x003d;
        L_0x0440:
            r1 = "IdvVerifier";
            r2 = "error happend during token status change. report to server";
            com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
            r0 = r17;
            r1 = r0.lt;
            r2 = 0;
            r5 = "STATUS_CHANGE";
            r6 = com.samsung.android.spayfw.core.Card.m574y(r6);
            r8 = 0;
            r1.m335b(r2, r3, r4, r5, r6, r7, r8);
            goto L_0x0050;
        L_0x0458:
            r16 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r1;
            r1 = r16;
            goto L_0x0030;
        L_0x0464:
            r1 = r2;
            r2 = r6;
            r6 = r3;
            r3 = r4;
            r4 = r5;
            r5 = r8;
            r8 = r9;
            goto L_0x0030;
        L_0x046d:
            r1 = r10;
            goto L_0x0127;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.k.a.a(int, com.samsung.android.spayfw.remoteservice.c):void");
        }
    }

    public IdvVerifier(Context context, String str, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback) {
        super(context);
        this.mEnrollmentId = str;
        this.ls = verifyIdvInfo;
        this.lr = iVerifyIdvCallback;
    }

    public void process() {
        Log.m285d("IdvVerifier", "verifyIdv()");
        if (this.mEnrollmentId == null || this.lr == null || this.iJ == null || this.ls == null || this.ls.getValue() == null) {
            int i = -5;
            if (this.iJ == null) {
                Log.m286e("IdvVerifier", "verifyIdv  - Failed to initialize account");
                i = -1;
            } else {
                Log.m286e("IdvVerifier", "verifyIdv Failed - Invalid inputs");
            }
            if (this.lr != null) {
                try {
                    this.lr.onFail(this.mEnrollmentId, i, null);
                    return;
                } catch (Throwable e) {
                    Log.m284c("IdvVerifier", e.getMessage(), e);
                    return;
                }
            }
            return;
        }
        Card q = this.iJ.m558q(this.mEnrollmentId);
        if (q == null) {
            Log.m286e("IdvVerifier", "verifyIdv Failed - unable to find the card in memory. delete card in db");
            try {
                this.lr.onFail(this.mEnrollmentId, -6, null);
            } catch (Throwable e2) {
                Log.m284c("IdvVerifier", e2.getMessage(), e2);
            }
        } else if (q.ac() == null || q.ac().getTokenStatus() == null || !TokenStatus.PENDING_PROVISION.equals(q.ac().getTokenStatus())) {
            Log.m286e("IdvVerifier", "verifyIdv Failed - token is null or token staus is not correct. ");
            if (!(q.ac() == null || q.ac().getTokenStatus() == null)) {
                Log.m286e("IdvVerifier", "verifyIdv Failed - token status:  " + q.ac().getTokenStatus());
            }
            try {
                this.lr.onFail(this.mEnrollmentId, -4, null);
            } catch (Throwable e22) {
                Log.m284c("IdvVerifier", e22.getMessage(), e22);
            }
        } else {
            String string;
            long currentTimeMillis = System.currentTimeMillis();
            Log.m285d("IdvVerifier", "Verify Idv Info : " + this.ls.toString());
            ProviderRequestData verifyIdvRequestDataTA = q.ad().getVerifyIdvRequestDataTA(this.ls);
            if (!(verifyIdvRequestDataTA == null || verifyIdvRequestDataTA.cg() == null)) {
                string = verifyIdvRequestDataTA.cg().getString("tac");
                verifyIdvRequestDataTA.cg().remove("tac");
                if (string != null) {
                    string = "TAC " + string;
                    if (this.ls.getType().equals(IdvMethod.IDV_TYPE_APP) || !(string == null || string.isEmpty())) {
                        if (string == null) {
                            string = "OTP " + this.ls.getValue();
                        }
                        this.lQ.m1137a(Card.m574y(q.getCardBrand()), RequestDataBuilder.m625a(currentTimeMillis, this.mEnrollmentId, null, verifyIdvRequestDataTA), string).m836a(new IdvVerifier(this, this.mEnrollmentId, this.lr));
                    }
                    Log.m285d("IdvVerifier", "No TAC, status via push");
                    try {
                        this.lr.onSuccess(this.mEnrollmentId, null);
                        return;
                    } catch (Throwable e222) {
                        Log.m284c("IdvVerifier", e222.getMessage(), e222);
                        return;
                    }
                }
            }
            string = null;
            if (this.ls.getType().equals(IdvMethod.IDV_TYPE_APP)) {
            }
            if (string == null) {
                string = "OTP " + this.ls.getValue();
            }
            this.lQ.m1137a(Card.m574y(q.getCardBrand()), RequestDataBuilder.m625a(currentTimeMillis, this.mEnrollmentId, null, verifyIdvRequestDataTA), string).m836a(new IdvVerifier(this, this.mEnrollmentId, this.lr));
        }
    }
}
