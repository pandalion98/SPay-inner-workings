package com.samsung.android.spayfw.core;

import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardEnrollmentInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Initiator;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Mode;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserName;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.WalletInfo;
import com.samsung.android.spayfw.utils.Utils;
import java.util.Locale;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.core.l */
public class RequestDataBuilder {
    public static EnrollmentRequestData m624a(EnrollCardInfo enrollCardInfo, CertificateInfo[] certificateInfoArr, DeviceInfo deviceInfo, ProviderRequestData providerRequestData, Card card, String str) {
        String str2;
        String str3;
        UserInfo userInfo;
        EnrollmentRequestData enrollmentRequestData = new EnrollmentRequestData();
        enrollmentRequestData.setWallet(new WalletInfo(enrollCardInfo.getWalletId()));
        CardEnrollmentInfo cardEnrollmentInfo = new CardEnrollmentInfo(null);
        cardEnrollmentInfo.setBrand(card.getCardBrand());
        String applicationId = enrollCardInfo.getApplicationId();
        if (providerRequestData != null) {
            if (providerRequestData.ch() != null) {
                cardEnrollmentInfo.setData(providerRequestData.ch());
            }
            Bundle cg = providerRequestData.cg();
            if (cg != null) {
                str2 = null;
                for (String str32 : cg.keySet()) {
                    if (str32.equals("emailHash")) {
                        String str4 = applicationId;
                        applicationId = cg.getString("emailHash");
                        str32 = str4;
                    } else if (str32.equals("appId")) {
                        str32 = cg.getString("appId");
                        applicationId = str2;
                    } else {
                        str32 = applicationId;
                        applicationId = str2;
                    }
                    str2 = applicationId;
                    applicationId = str32;
                }
                if (str != null) {
                    Log.m285d("RequestDataBuilder", "CardRef Id based enrollment. setting cardRef id ");
                    cardEnrollmentInfo.setReference(str);
                }
                deviceInfo.setParentId(enrollCardInfo.getDeviceParentId());
                enrollmentRequestData.setCard(cardEnrollmentInfo);
                if (certificateInfoArr == null) {
                    deviceInfo.setCertificates(certificateInfoArr);
                } else {
                    Log.m290w("RequestDataBuilder", "device certificates are empty");
                    deviceInfo.setCertificates(new CertificateInfo[3]);
                }
                enrollmentRequestData.setDevice(deviceInfo);
                enrollmentRequestData.setEntry(new Mode(enrollCardInfo.getCardEntryMode()));
                enrollmentRequestData.setPresentation(new Mode(RequestDataBuilder.m627n(card.ab())));
                userInfo = new UserInfo(enrollCardInfo.getUserId());
                userInfo.setMask(Utils.bA(enrollCardInfo.getUserEmail()));
                userInfo.setLanguage(Locale.getDefault().getLanguage());
                userInfo.setHash(str2);
                enrollmentRequestData.setApp(new Id(applicationId));
                str32 = enrollCardInfo instanceof EnrollCardPanInfo ? ((EnrollCardPanInfo) enrollCardInfo).getName() : enrollCardInfo instanceof EnrollCardReferenceInfo ? ((EnrollCardReferenceInfo) enrollCardInfo).getName() : enrollCardInfo instanceof EnrollCardLoyaltyInfo ? "Loyalty" : null;
                if (str32 == null) {
                    str32 = "SamsungPayUser";
                }
                userInfo.setName(new UserName(str32));
                enrollmentRequestData.setUser(userInfo);
                if (!(enrollCardInfo == null || enrollCardInfo.getExtraEnrollData() == null)) {
                    str32 = enrollCardInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.ENROLL_FEATURE_KEY);
                    applicationId = enrollCardInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.ENROLL_INITIATOR_ID_KEY);
                    if (!(applicationId == null && str32 == null)) {
                        Log.m285d("RequestDataBuilder", "Initiator id " + applicationId);
                        Log.m285d("RequestDataBuilder", "Initiator feature " + str32);
                        enrollmentRequestData.setInitiator(new Initiator(applicationId, str32));
                    }
                }
                return enrollmentRequestData;
            }
        }
        str2 = null;
        if (str != null) {
            Log.m285d("RequestDataBuilder", "CardRef Id based enrollment. setting cardRef id ");
            cardEnrollmentInfo.setReference(str);
        }
        deviceInfo.setParentId(enrollCardInfo.getDeviceParentId());
        enrollmentRequestData.setCard(cardEnrollmentInfo);
        if (certificateInfoArr == null) {
            Log.m290w("RequestDataBuilder", "device certificates are empty");
            deviceInfo.setCertificates(new CertificateInfo[3]);
        } else {
            deviceInfo.setCertificates(certificateInfoArr);
        }
        enrollmentRequestData.setDevice(deviceInfo);
        enrollmentRequestData.setEntry(new Mode(enrollCardInfo.getCardEntryMode()));
        enrollmentRequestData.setPresentation(new Mode(RequestDataBuilder.m627n(card.ab())));
        userInfo = new UserInfo(enrollCardInfo.getUserId());
        userInfo.setMask(Utils.bA(enrollCardInfo.getUserEmail()));
        userInfo.setLanguage(Locale.getDefault().getLanguage());
        userInfo.setHash(str2);
        enrollmentRequestData.setApp(new Id(applicationId));
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
        }
        if (str32 == null) {
            str32 = "SamsungPayUser";
        }
        userInfo.setName(new UserName(str32));
        enrollmentRequestData.setUser(userInfo);
        str32 = enrollCardInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.ENROLL_FEATURE_KEY);
        applicationId = enrollCardInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.ENROLL_INITIATOR_ID_KEY);
        Log.m285d("RequestDataBuilder", "Initiator id " + applicationId);
        Log.m285d("RequestDataBuilder", "Initiator feature " + str32);
        enrollmentRequestData.setInitiator(new Initiator(applicationId, str32));
        return enrollmentRequestData;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData m625a(long r10, java.lang.String r12, com.samsung.android.spayfw.appinterface.ProvisionTokenInfo r13, com.samsung.android.spayfw.payprovider.ProviderRequestData r14) {
        /*
        r1 = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
        r1.<init>(r12);
        r0 = 0;
        r2 = -1;
        r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0011;
    L_0x000c:
        r0 = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.TimeStamp;
        r0.<init>(r10);
    L_0x0011:
        r2 = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
        r2.<init>(r1, r0);
        if (r14 == 0) goto L_0x006b;
    L_0x0018:
        r0 = r14.getErrorCode();
        if (r0 != 0) goto L_0x006b;
    L_0x001e:
        r1 = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.Activation;
        r4 = java.lang.System.currentTimeMillis();
        r1.<init>(r4);
        r3 = r14.cg();
        if (r3 == 0) goto L_0x0061;
    L_0x002d:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r3.keySet();
        r5 = r0.iterator();
    L_0x003a:
        r0 = r5.hasNext();
        if (r0 == 0) goto L_0x005e;
    L_0x0040:
        r0 = r5.next();
        r0 = (java.lang.String) r0;
        r6 = "riskData";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x003a;
    L_0x004e:
        r0 = "riskData";
        r0 = r3.getSerializable(r0);
        r0 = (java.util.ArrayList) r0;
        if (r0 == 0) goto L_0x005e;
    L_0x0058:
        r6 = r0.isEmpty();
        if (r6 == 0) goto L_0x006c;
    L_0x005e:
        r1.setParameters(r4);
    L_0x0061:
        r2.setActivation(r1);
        r0 = r14.ch();
        r2.setData(r0);
    L_0x006b:
        return r2;
    L_0x006c:
        r6 = r0.iterator();
    L_0x0070:
        r0 = r6.hasNext();
        if (r0 == 0) goto L_0x003a;
    L_0x0076:
        r0 = r6.next();
        r0 = (com.samsung.android.spayfw.payprovider.RiskDataParam) r0;
        if (r0 == 0) goto L_0x0070;
    L_0x007e:
        r7 = r0.getKey();
        if (r7 == 0) goto L_0x0070;
    L_0x0084:
        r7 = r0.getValue();
        if (r7 == 0) goto L_0x0070;
    L_0x008a:
        r7 = "RequestDataBuilder";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "RiskData: key = ";
        r8 = r8.append(r9);
        r9 = r0.getKey();
        r8 = r8.append(r9);
        r9 = "; value = ";
        r8 = r8.append(r9);
        r9 = r0.getValue();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);
        r7 = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.ActivationParameters;
        r8 = r0.getKey();
        r0 = r0.getValue();
        r7.<init>(r8, r0);
        r4.add(r7);
        goto L_0x0070;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.l.a(long, java.lang.String, com.samsung.android.spayfw.appinterface.ProvisionTokenInfo, com.samsung.android.spayfw.payprovider.c):com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData");
    }

    public static final void m626a(ProviderRequestData providerRequestData, EnrollmentRequestData enrollmentRequestData, String str) {
        if (providerRequestData != null && enrollmentRequestData != null) {
            if (!(providerRequestData.ch() == null || enrollmentRequestData.getCard() == null)) {
                enrollmentRequestData.getCard().setData(providerRequestData.ch());
            }
            Bundle cg = providerRequestData.cg();
            if (cg != null) {
                for (String str2 : cg.keySet()) {
                    String str22;
                    if (str22.equals("emailHash")) {
                        str22 = cg.getString("emailHash");
                        if (!(str22 == null || enrollmentRequestData.getUser() == null)) {
                            enrollmentRequestData.getUser().setHash(str22);
                        }
                    } else if (str22.equals("appId")) {
                        str22 = cg.getString("appId");
                        if (str22 != null) {
                            enrollmentRequestData.setApp(new Id(str22));
                        }
                    }
                }
            }
            if (str != null) {
                Log.m285d("RequestDataBuilder", "CardRef Id based enrollment. setting cardRef id ");
                enrollmentRequestData.getCard().setReference(str);
            }
        }
    }

    private static String m627n(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return EnrollCardInfo.CARD_PRESENTATION_MODE_NFC;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return EnrollCardInfo.CARD_PRESENTATION_MODE_MST;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return EnrollCardInfo.CARD_PRESENTATION_MODE_ECM;
            case NamedCurve.secp160k1 /*15*/:
                return EnrollCardInfo.CARD_PRESENTATION_MODE_ALL;
            default:
                return EnrollCardInfo.CARD_PRESENTATION_MODE_ALL;
        }
    }
}
