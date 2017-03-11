package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.ApduCommand.ApduParser;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.ApduResponse;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.GetTokenResponse;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.Iso3166Alpha3CountryCode;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McBillingAddress;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCasdJwk;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McEligibilityReceipt;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McIssuerInitiatedDigitizationData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.ProvisionToken.Response;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class McTokenProvisioningHandler {
    private static final String OS_NAME_ANDROID = "Android";
    public static final String STRING_EMPTY = "";
    private static final String TAG = "McTokenProvisioningHandler";
    private static final int USER_NAME_SIZE_MAX = 27;
    private byte[] mAp1CertBuffer;
    private byte[] mAp2CertBuffer;
    private long mDbReference;
    private McTAController mMcTAController;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler.1 */
    class C05721 extends TypeToken<DC_CP> {
        C05721() {
        }
    }

    private enum CardSource {
        CARD_ON_FILE(EnrollCardInfo.CARD_ENTRY_MODE_FILE),
        CARD_ADDED_MANUALLY(EnrollCardInfo.CARD_ENTRY_MODE_MANUAL),
        CARD_ADDED_VIA_APPLICATION(IdvMethod.IDV_TYPE_APP);
        
        private static final Map<String, CardSource> mCardSourceMap;
        private final String mSource;

        static {
            mCardSourceMap = new HashMap();
            CardSource[] values = values();
            int length = values.length;
            int i;
            while (i < length) {
                CardSource cardSource = values[i];
                mCardSourceMap.put(cardSource.mSource, cardSource);
                i++;
            }
            mCardSourceMap.put(EnrollCardInfo.CARD_ENTRY_MODE_OCR, CARD_ADDED_MANUALLY);
        }

        private CardSource(String str) {
            this.mSource = str;
        }

        public static CardSource getMcCardSource(String str) {
            return (CardSource) mCardSourceMap.get(str);
        }
    }

    public McTokenProvisioningHandler() {
        Log.m285d(TAG, "McTokenProvisioningHandler: Creating a new instance");
        this.mMcTAController = McTAController.getInstance();
        this.mDbReference = -1;
    }

    private String generatePaymentAppInstanceId(String str) {
        int i = 0;
        if (str == null) {
            Log.m286e(TAG, "Wallet Id is null");
            return null;
        }
        String deviceId = DeviceInfo.getDeviceId(this.mMcTAController.getContext());
        int length = deviceId.length();
        int length2 = str.length();
        String str2 = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < 24 - length; i2++) {
            stringBuilder.append(str2);
        }
        stringBuilder.append(deviceId);
        while (i < 24 - length2) {
            stringBuilder.append(str2);
            i++;
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.samsung.android.spayfw.payprovider.ProviderRequestData getEnrollmentMcData(com.samsung.android.spayfw.appinterface.EnrollCardInfo r21, com.samsung.android.spayfw.appinterface.BillingInfo r22) {
        /*
        r20 = this;
        r9 = new com.samsung.android.spayfw.payprovider.c;
        r9.<init>();
        r2 = 0;
        r9.setErrorCode(r2);
        r0 = r20;
        r2 = r0.mMcTAController;
        if (r2 != 0) goto L_0x001e;
    L_0x000f:
        r2 = "McTokenProvisioningHandler";
        r3 = "mMcTAController is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -6;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
    L_0x001d:
        return r2;
    L_0x001e:
        r3 = new com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;
        r3.<init>();
        r2 = r21.getWalletId();
        r0 = r20;
        r7 = r0.generatePaymentAppInstanceId(r2);
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.MC_PAY_OK;
        r0 = r20;
        r2 = r0.mMcTAController;
        r2 = r2.getCasdParameters();
        r0 = r20;
        r4 = r0.mAp1CertBuffer;
        if (r4 == 0) goto L_0x0045;
    L_0x003d:
        r0 = r20;
        r4 = r0.mAp2CertBuffer;
        if (r4 == 0) goto L_0x0045;
    L_0x0043:
        if (r2 == 0) goto L_0x006b;
    L_0x0045:
        r2 = "McTokenProvisioningHandler";
        r4 = "Need Certs Update";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r4);
        r2 = new com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSeInfo;
        r0 = r20;
        r4 = r0.mMcTAController;
        r4 = r4.getContext();
        r4 = com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.getDeviceId(r4);
        r5 = 0;
        r2.<init>(r4, r5);
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = 0;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder.buildMcEnrollmentRequestPayload(r2, r3, r4, r5, r6, r7, r8);
        r9.m822a(r2);
        r2 = r9;
        goto L_0x001d;
    L_0x006b:
        r2 = new com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfo;
        r2.<init>();
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r4 = "McTokenProvisioningHandler";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "cardInfo Type : ";
        r5 = r5.append(r6);
        r6 = r21.getClass();
        r6 = r6.getSimpleName();
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);
        r0 = r20;
        r1 = r22;
        r4 = r0.generateMcBillingAddress(r1);
        r2.setBillingAddress(r4);
        r4 = 0;
        r2.setDataValidUntilTimestamp(r4);
        r0 = r21;
        r4 = r0 instanceof com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
        if (r4 == 0) goto L_0x00f1;
    L_0x00aa:
        r21 = (com.samsung.android.spayfw.appinterface.EnrollCardPanInfo) r21;
        r0 = r20;
        r1 = r21;
        r0.generateMcCardInfo(r2, r1);
        r2.refactorBillingAddress();
    L_0x00b6:
        r3.setCardInfo(r2);
        r5 = new com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSpsdInfo;
        r5.<init>();
        r4 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager.getContext();
        r6 = 2130968611; // 0x7f040023 float:1.754588E38 double:1.052838383E-314;
        r4 = r4.getString(r6);
        r6 = new com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
        r6.<init>();
        r10 = new com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
        r11 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager.getContext();
        r10.<init>(r11);
        r12 = r10.saveData(r6);
        r14 = -1;
        r11 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        if (r11 != 0) goto L_0x01b8;
    L_0x00e1:
        r2 = "McTokenProvisioningHandler";
        r3 = "Err...Failed to create empty db record";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -2;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x00f1:
        r0 = r21;
        r4 = r0 instanceof com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
        if (r4 == 0) goto L_0x00b6;
    L_0x00f7:
        r21 = (com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo) r21;
        r4 = r21.getReferenceType();
        if (r4 == 0) goto L_0x01a7;
    L_0x00ff:
        r4 = r21.getExtraEnrollData();
        if (r4 == 0) goto L_0x01a7;
    L_0x0105:
        r4 = "McTokenProvisioningHandler";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Card Type : ";
        r5 = r5.append(r6);
        r6 = r21.getReferenceType();
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);
        r4 = r21.getReferenceType();
        r5 = "app2app";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x018f;
    L_0x012d:
        r3 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler.CardSource.CARD_ADDED_VIA_APPLICATION;
        r3 = r3.toString();
        r2.setSource(r3);
        r3 = r21.getExtraEnrollData();
        r4 = "enrollPayload";
        r3 = r3.getByteArray(r4);
        if (r3 == 0) goto L_0x0160;
    L_0x0142:
        r4 = new java.lang.String;
        r4.<init>(r3);
        r0 = r20;
        r3 = r0.getIssuerInitiatedDigitizationData(r4, r8);
        if (r3 != 0) goto L_0x0171;
    L_0x014f:
        r2 = "McTokenProvisioningHandler";
        r3 = "mc app2app mcCardWrapper is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -9;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x0160:
        r2 = "McTokenProvisioningHandler";
        r3 = "mc app2app issuer payload is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -9;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x0171:
        r4 = "McTokenProvisioningHandler";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "mcCardWrapper: ";
        r5 = r5.append(r6);
        r6 = r3.toString();
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);
        goto L_0x00b6;
    L_0x018f:
        r4 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler.CardSource.CARD_ON_FILE;
        r4 = r4.toString();
        r2.setSource(r4);
        r4 = r21.getExtraEnrollData();
        r5 = "cardReferenceId";
        r4 = r4.getString(r5);
        r3.setPanUniqueReference(r4);
        goto L_0x00b6;
    L_0x01a7:
        r2 = "McTokenProvisioningHandler";
        r3 = "enrollCardReferenceInfo reference type or extra data is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -9;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x01b8:
        r11 = "McTokenProvisioningHandler";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "Created a new Db record with value=";
        r14 = r14.append(r15);
        r14 = r14.append(r12);
        r14 = r14.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r11, r14);
        r11 = new com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McAidSelector;
        r14 = r2.getAccountNumber();
        r15 = r2.getSource();
        r16 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler.CardSource.CARD_ADDED_MANUALLY;
        r16 = r16.toString();
        r15 = r15.equals(r16);
        r11.<init>(r14, r15);
        r14 = r11.getAid();
        if (r14 != 0) goto L_0x01fd;
    L_0x01ed:
        r2 = "McTokenProvisioningHandler";
        r3 = "Err...Failed to create empty db record";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -2;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x01fd:
        r14 = r11.generateAppletInstanceAids(r12);
        r15 = android.text.TextUtils.isEmpty(r14);
        if (r15 == 0) goto L_0x0217;
    L_0x0207:
        r2 = "McTokenProvisioningHandler";
        r3 = "Applet instance id is null or empty";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -2;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x0217:
        r15 = r11.getAid();
        r16 = android.text.TextUtils.isEmpty(r15);
        if (r16 == 0) goto L_0x0231;
    L_0x0221:
        r2 = "McTokenProvisioningHandler";
        r3 = "AID selection failed!!";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -2;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x0231:
        r0 = r20;
        r0 = r0.mMcTAController;
        r16 = r0;
        r0 = r20;
        r0 = r0.mAp1CertBuffer;
        r17 = r0;
        r15 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.convertStirngToByteArray(r15);
        r0 = r16;
        r1 = r17;
        r15 = r0.getSpsdInfo(r1, r15);
        if (r15 == 0) goto L_0x0277;
    L_0x024b:
        r16 = r15.getRgk();
        r0 = r16;
        r0 = r0.length;
        r16 = r0;
        if (r16 == 0) goto L_0x0277;
    L_0x0256:
        r16 = r15.getSkuKeys();
        r0 = r16;
        r0 = r0.length;
        r16 = r0;
        if (r16 == 0) goto L_0x0277;
    L_0x0261:
        r16 = r15.getSpsdSequenceCounter();
        r0 = r16;
        r0 = r0.length;
        r16 = r0;
        if (r16 == 0) goto L_0x0277;
    L_0x026c:
        r16 = r15.getCertEncrypt();
        r0 = r16;
        r0 = r0.length;
        r16 = r0;
        if (r16 != 0) goto L_0x02ae;
    L_0x0277:
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.MC_PAY_OK;
        r2.getValue();
        if (r15 == 0) goto L_0x02a7;
    L_0x027e:
        r2 = r15.getReturnCode();
        r2 = r2.get();
    L_0x0286:
        r4 = "McTokenProvisioningHandler";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Spsd Response from TA failed.. Check for Null values..Abort Enrollment Err : ";
        r5 = r5.append(r6);
        r2 = r5.append(r2);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r4, r2);
        r2 = -6;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x02a7:
        r2 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.MC_PAY_INTERNAL_ERROR;
        r2 = r2.getValue();
        goto L_0x0286;
    L_0x02ae:
        r16 = r15.getReturnCode();
        r16 = r16.get();
        r16 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.getMcTAError(r16);
        r17 = "McTokenProvisioningHandler";
        r18 = new java.lang.StringBuilder;
        r18.<init>();
        r19 = "getSpsdInfo : TA Result : ";
        r18 = r18.append(r19);
        r19 = r16.name();
        r18 = r18.append(r19);
        r18 = r18.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r17, r18);
        r17 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.MC_PAY_OK;
        r0 = r16;
        r1 = r17;
        if (r0 == r1) goto L_0x02ee;
    L_0x02de:
        r2 = "McTokenProvisioningHandler";
        r3 = "Spsd Response from TA failed.. Abort Enrollment";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -6;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x02ee:
        r16 = r15.getSpsdSequenceCounter();
        r16 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteArrayToHex(r16);
        r0 = r16;
        r5.setSpsdSequenceCounter(r0);
        r16 = r15.getRgk();
        r16 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteArrayToHex(r16);
        r0 = r16;
        r5.setRgk(r0);
        r16 = r15.getSkuKeys();
        r0 = r16;
        r6.setRgkDerivedkeys(r0);
        r10.updateData(r6, r12);
        r0 = r20;
        r0.setDbReference(r12);
        r6 = r15.getCasdCertType();
        r0 = r20;
        r10 = r0.mMcTAController;
        r10 = 1;
        if (r6 == r10) goto L_0x032f;
    L_0x0324:
        r6 = r15.getCasdCertType();
        r0 = r20;
        r10 = r0.mMcTAController;
        r10 = 2;
        if (r6 != r10) goto L_0x0335;
    L_0x032f:
        r6 = r15.getCertEncrypt();
        if (r6 != 0) goto L_0x0345;
    L_0x0335:
        r2 = "McTokenProvisioningHandler";
        r3 = "getEnrollmentMcData: Unknown type / null CASD cert detected";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -6;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x0345:
        r6 = r15.getCasdCertType();
        r0 = r20;
        r10 = r0.mMcTAController;
        r10 = 1;
        if (r6 != r10) goto L_0x040a;
    L_0x0350:
        r6 = "McTokenProvisioningHandler";
        r10 = "getEnrollmentMcData: CASD cert type x509 detected";
        com.samsung.android.spayfw.p002b.Log.m286e(r6, r10);
        r6 = r15.getCertEncrypt();
        r0 = r20;
        r6 = r0.convertByteArrayToString(r6);
        r5.setCasdPkJwk(r6);
        r6 = 0;
        r5.setCasdPkCertificate(r6);
    L_0x0368:
        r6 = r11.getAid();
        r5.setAid(r6);
        r5.setAppletInstanceAid(r14);
        r2 = r2.getSource();
        r6 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler.CardSource.CARD_ADDED_VIA_APPLICATION;
        r6 = r6.toString();
        r2 = r2.equals(r6);
        if (r2 != 0) goto L_0x0453;
    L_0x0382:
        r0 = r20;
        r2 = r0.mAp2CertBuffer;
        r6 = com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils.ShaConstants.SHA1;
        r2 = com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils.getShaDigest(r2, r6);
        r2 = com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils.convertbyteToHexString(r2);
        r6 = "McTokenProvisioningHandler";
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Certificate Alias: ";
        r10 = r10.append(r11);
        r10 = r10.append(r2);
        r10 = r10.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r6, r10);
        r3.setCertificateFingerprint(r2);
        r0 = r20;
        r2 = r0.mMcTAController;
        r6 = r3.getUnenryptedData();
        r6 = com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder.toByteArray(r6);
        r0 = r20;
        r10 = r0.mAp2CertBuffer;
        r2 = r2.encryptCardInfo(r6, r10);
        if (r2 == 0) goto L_0x04a9;
    L_0x03c1:
        r10 = r2.getReturnCode();
        r6 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.getMcTAError(r10);
        r10 = "McTokenProvisioningHandler";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "encryptCardInfo : TA Result : ";
        r11 = r11.append(r12);
        r12 = r6.name();
        r11 = r11.append(r12);
        r11 = r11.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r10, r11);
        r10 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError.MC_PAY_OK;
        if (r6 == r10) goto L_0x042d;
    L_0x03e9:
        r2 = "McTokenProvisioningHandler";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Failed to encrypt Card Information Error : ";
        r3 = r3.append(r4);
        r3 = r3.append(r6);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -6;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x040a:
        r6 = r15.getCasdCertType();
        r0 = r20;
        r10 = r0.mMcTAController;
        r10 = 2;
        if (r6 != r10) goto L_0x0368;
    L_0x0415:
        r6 = "McTokenProvisioningHandler";
        r10 = "getEnrollmentMcData: CASD cert type GP detected";
        com.samsung.android.spayfw.p002b.Log.m286e(r6, r10);
        r6 = r15.getCertEncrypt();
        r6 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteArrayToHex(r6);
        r5.setCasdPkCertificate(r6);
        r6 = 0;
        r5.setCasdPkJwk(r6);
        goto L_0x0368;
    L_0x042d:
        r6 = r2.getEncryptedData();
        if (r6 == 0) goto L_0x043e;
    L_0x0433:
        r6 = r2.getEncryptedData();
        r6 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteArrayToHex(r6);
        r3.setEncryptedData(r6);
    L_0x043e:
        r6 = r2.getEncryptedKey();
        if (r6 == 0) goto L_0x044f;
    L_0x0444:
        r2 = r2.getEncryptedKey();
        r2 = com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils.byteArrayToHex(r2);
        r3.setEncryptedKey(r2);
    L_0x044f:
        r2 = 0;
        r3.setOaepHashingAlgorithm(r2);
    L_0x0453:
        r2 = 0;
        r3.setCardInfo(r2);
        r6 = new com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McDeviceInfo;
        r6.<init>();
        r2 = "Android";
        r6.setOsName(r2);
        r2 = android.os.Build.VERSION.SDK_INT;
        r2 = java.lang.String.valueOf(r2);
        r6.setOsVersion(r2);
        r2 = 1;
        r6.setNfcCapable(r2);
        r2 = com.samsung.android.spayfw.payprovider.mastercard.McProvider.getContext();
        r2 = r2.getPackageManager();
        r10 = "android.hardware.nfc";
        r2 = r2.hasSystemFeature(r10);
        if (r2 != 0) goto L_0x0482;
    L_0x047e:
        r2 = 0;
        r6.setNfcCapable(r2);
    L_0x0482:
        r2 = new com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSeInfo;
        r0 = r20;
        r10 = r0.mMcTAController;
        r10 = r10.getContext();
        r10 = com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.getDeviceId(r10);
        r11 = 0;
        r2.<init>(r10, r11);
        r10 = r8.toString();
        r10 = r10.isEmpty();
        if (r10 == 0) goto L_0x04b9;
    L_0x049e:
        r8 = 0;
    L_0x049f:
        r2 = com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder.buildMcEnrollmentRequestPayload(r2, r3, r4, r5, r6, r7, r8);
        r9.m822a(r2);
        r2 = r9;
        goto L_0x001d;
    L_0x04a9:
        r2 = "McTokenProvisioningHandler";
        r3 = "getEnrollmentMcData: ecryptCardInfo is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
        r2 = -6;
        r0 = r20;
        r2 = r0.getEnrollmentMcDataError(r9, r2);
        goto L_0x001d;
    L_0x04b9:
        r8 = r8.toString();
        goto L_0x049f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler.getEnrollmentMcData(com.samsung.android.spayfw.appinterface.EnrollCardInfo, com.samsung.android.spayfw.appinterface.BillingInfo):com.samsung.android.spayfw.payprovider.c");
    }

    public void setUniqueTokenReference(String str) {
        McCardMasterDaoImpl mcCardMasterDaoImpl = new McCardMasterDaoImpl(McTokenManager.getContext());
        McCardMaster mcCardMaster = (McCardMaster) mcCardMasterDaoImpl.getData(getDbReference());
        if (mcCardMaster != null) {
            mcCardMaster.setTokenUniqueReference(str);
            mcCardMasterDaoImpl.updateData(mcCardMaster, getDbReference());
            return;
        }
        Log.m286e(TAG, " No valid McCardMaster object : " + getDbReference());
    }

    public void setMcCerts(byte[] bArr, byte[] bArr2) {
        this.mAp1CertBuffer = bArr;
        this.mAp2CertBuffer = bArr2;
    }

    private byte[] getappletInstanceIdBytes(String str) {
        byte[] convertStringToByteArray = ApduParser.convertStringToByteArray(str);
        Log.m285d(TAG, "Applet Instance Id: " + str + ", Byte Array: " + Arrays.toString(convertStringToByteArray) + ", Length: " + convertStringToByteArray.length);
        return convertStringToByteArray;
    }

    private ProviderRequestData getEnrollmentMcDataError(ProviderRequestData providerRequestData, int i) {
        providerRequestData.m822a(null);
        providerRequestData.setErrorCode(i);
        return providerRequestData;
    }

    boolean cleanupDbReference(long j, McCardMasterDaoImpl mcCardMasterDaoImpl) {
        if (mcCardMasterDaoImpl == null || j < 0) {
            return false;
        }
        setDbReference(-1);
        return mcCardMasterDaoImpl.deleteData(j);
    }

    private void generateMcCardInfo(McCardInfo mcCardInfo, EnrollCardPanInfo enrollCardPanInfo) {
        if (enrollCardPanInfo != null) {
            mcCardInfo.setAccountNumber(enrollCardPanInfo.getPAN());
            mcCardInfo.setExpiryMonth(enrollCardPanInfo.getExpMonth());
            mcCardInfo.setExpiryYear(enrollCardPanInfo.getExpYear());
            mcCardInfo.setSecurityCode(enrollCardPanInfo.getCVV());
            if (enrollCardPanInfo.getName() != null) {
                mcCardInfo.setCardholderName(generateUserName(enrollCardPanInfo.getName()));
                Log.m285d(TAG, "Card Holder Name: " + mcCardInfo.getCardholderName());
            } else {
                Log.m286e(TAG, "CardInfo does not have card holder name value");
            }
            mcCardInfo.setSource(CardSource.getMcCardSource(enrollCardPanInfo.getCardEntryMode()).toString());
        }
        Log.m285d(TAG, "mcCardInfo Source: " + mcCardInfo.getSource());
    }

    private McBillingAddress generateMcBillingAddress(BillingInfo billingInfo) {
        String str = null;
        if (billingInfo != null) {
            Log.m285d(TAG, "McCardInfo : mcCardInfo Sent by app: " + billingInfo.toString());
            McBillingAddress mcBillingAddress = new McBillingAddress();
            String country = billingInfo.getCountry();
            if (country != null) {
                Iso3166Alpha3CountryCode alpha3Code = Iso3166Alpha3CountryCode.getAlpha3Code(country.toUpperCase());
                if (alpha3Code != null) {
                    str = alpha3Code.name();
                }
            } else {
                str = country;
            }
            mcBillingAddress.setCity(billingInfo.getCity());
            mcBillingAddress.setCountry(str);
            mcBillingAddress.setLine1(billingInfo.getStreet1());
            mcBillingAddress.setLine2(billingInfo.getStreet2());
            mcBillingAddress.setPostalCode(billingInfo.getZip());
            mcBillingAddress.setCountrySubdivision(billingInfo.getState());
            return mcBillingAddress;
        }
        Log.m287i(TAG, "Input BillingInfo is null -2-");
        return null;
    }

    public boolean saveEligibilityReceipt(McEligibilityReceipt mcEligibilityReceipt) {
        return true;
    }

    public ProviderResponseData provisionToken(JsonObject jsonObject, int i) {
        if (i != 2) {
            Log.m285d(TAG, "provisionToken Called invalid src type: " + i);
            return getProviderResponseData(null, -4);
        }
        McTAError mcTAError = McTAError.MC_PAY_OK;
        GetTokenResponse parseJson = GetTokenResponse.parseJson(jsonObject);
        if (parseJson == null || McTokenManager.getContext() == null) {
            Log.m286e(TAG, "provisionToken Called with no data");
            return getProviderResponseData(null, -4);
        } else if (parseJson.getAppletInstanceAid() == null || parseJson.getAppletInstanceAid().isEmpty()) {
            Log.m286e(TAG, "provisionToken : Incorrect AppletInstanceAid");
            return getProviderResponseData(null, -4);
        } else {
            McCardMasterDaoImpl mcCardMasterDaoImpl = new McCardMasterDaoImpl(McTokenManager.getContext());
            setDbReference(mcCardMasterDaoImpl.getDbIdFromTokenUniqueRefence(parseJson.getMdesTokenUniqueReference()));
            if (getDbReference() == -1) {
                Log.m285d(TAG, "Db reference lost before token provisioning call. Cannot associate data");
                return getProviderResponseData(null, -2);
            }
            McCardProfileDaoImpl mcCardProfileDaoImpl = new McCardProfileDaoImpl(McTokenManager.getContext(), new C05721().getType());
            parseJson.setPanUniqueReference("panUniqueReference");
            if (GetTokenResponse.isCreateTokenRequestValid(parseJson)) {
                String mdesTokenUniqueReference = parseJson.getMdesTokenUniqueReference();
                byte[] generateApduForTa = ApduParser.generateApduForTa(parseJson.getApduCommands());
                if (generateApduForTa == null) {
                    Log.m286e(TAG, "Internal error. Failed to extract apdu");
                    return getProviderResponseData(null, -2);
                }
                McCardMaster mcCardMaster = (McCardMaster) mcCardMasterDaoImpl.getData(getDbReference());
                if (mcCardMaster == null || mcCardMaster.getRgkDerivedkeys() == null) {
                    Log.m286e(TAG, "SKU keys missing in db");
                    return getProviderResponseData(null, -2);
                }
                ApduResponse[] initApduResponseFromCommand = ApduResponse.initApduResponseFromCommand(parseJson.getApduCommands());
                if (initApduResponseFromCommand == null) {
                    Log.m286e(TAG, "Internal Err while process apduResponses init");
                    return getProviderResponseData(null, -2);
                }
                Response provisionToken = this.mMcTAController.provisionToken(generateApduForTa, mcCardMaster.getRgkDerivedkeys(), getappletInstanceIdBytes(parseJson.getAppletInstanceAid()));
                if (provisionToken == null || provisionToken.mRetVal == null || provisionToken.mRetVal.result == null || provisionToken.mRetVal.rApdus == null || provisionToken.mRetVal.cardSecureData == null) {
                    Log.m286e(TAG, "TA returnes empty data for a provisionToken in the response.");
                    return getProviderResponseData(null, -6);
                }
                Log.m285d(TAG, "provisionToken : TA Result : " + McTAError.getMcTAError(provisionToken.mRetVal.result.get()).name());
                ApduResponse[] generateApduFromTa = ApduResponse.generateApduFromTa(initApduResponseFromCommand, provisionToken.mRetVal.rApdus.getData());
                if (generateApduFromTa == null) {
                    Log.m286e(TAG, "Internal Err while process apduResponses from TEE");
                    return getProviderResponseData(generateApduFromTa, -9);
                }
                McCardClearData mcCardClearDataObject = ApduResponse.getMcCardClearDataObject();
                MCUnusedDGIElements mCUnusedDGIElements = mcCardClearDataObject.getMCUnusedDGIElements();
                DC_CP paymentProfile = mcCardClearDataObject.getPaymentProfile();
                MCProfilesTable profilesTable = mcCardClearDataObject.getProfilesTable();
                if (profilesTable == null) {
                    Log.m286e(TAG, "Cannot get profiles table data. Deserializer returns null.");
                    return getProviderResponseData(generateApduFromTa, -9);
                }
                MCBaseCardProfile mCBaseCardProfile = new MCBaseCardProfile();
                mCBaseCardProfile.setDigitalizedCardContainer(paymentProfile);
                mCBaseCardProfile.setTADataContainer(provisionToken.mRetVal.cardSecureData.getData());
                mCBaseCardProfile.setTaAtcContainer(provisionToken.mRetVal.wrappedAtcObj.getData());
                mCBaseCardProfile.setUniqueTokenReferenceId(getDbReference());
                mCBaseCardProfile.setTAProfilesTable(profilesTable);
                mCBaseCardProfile.setUnusedDGIElements(mCUnusedDGIElements);
                if (mcCardProfileDaoImpl.saveData(mCBaseCardProfile) == -1) {
                    Log.m286e(TAG, "Unable to store Profile in Db");
                    return getProviderResponseData(generateApduFromTa, -9);
                }
                mcCardMaster.setTokenUniqueReference(mdesTokenUniqueReference);
                mcCardMaster.setProvisionedState(1);
                if (mcCardMasterDaoImpl.updateData(mcCardMaster, getDbReference())) {
                    return getProviderResponseData(generateApduFromTa, 0);
                }
                Log.m286e(TAG, "provisionToken: Update operation failed.. Discarding provisioned data");
                mcCardMasterDaoImpl.deleteData(getDbReference());
                return getProviderResponseData(generateApduFromTa, -2);
            }
            Log.m285d(TAG, "provisionToken Called with no data");
            return getProviderResponseData(null, -4);
        }
    }

    private ProviderResponseData getProviderResponseData(ApduResponse[] apduResponseArr, int i) {
        ProviderResponseData providerResponseData = new ProviderResponseData();
        if (apduResponseArr != null) {
            providerResponseData.m1057b(ApduResponse.getNotifyTokenRequest(apduResponseArr));
        }
        providerResponseData.setProviderTokenKey(new ProviderTokenKey(getDbReference()));
        providerResponseData.setErrorCode(i);
        return providerResponseData;
    }

    public long getDbReference() {
        return this.mDbReference;
    }

    public void setDbReference(long j) {
        this.mDbReference = j;
    }

    public byte[] processSignatureData(byte[] bArr, int i) {
        if (this.mMcTAController == null) {
            return null;
        }
        return this.mMcTAController.processSignatureData(bArr, i);
    }

    private String generateUserName(String str) {
        String str2 = null;
        if (str == null) {
            return null;
        }
        String[] split = str.split("[ ]+");
        if (split.length > 0) {
            String str3;
            String str4 = split[0];
            if (split.length > 1) {
                str3 = split[split.length - 1];
            } else {
                str3 = null;
            }
            if (str4 != null && str4.isEmpty()) {
                str4 = null;
            }
            if (str3 != null && str3.isEmpty()) {
                str3 = null;
            }
            int length;
            if (str3 != null) {
                str2 = str3;
                length = USER_NAME_SIZE_MAX - str3.length();
            } else {
                length = USER_NAME_SIZE_MAX;
            }
            if (str2 == null && str4 != null) {
                str2 = str4;
            } else if (!(str2 == null || str4 == null || r2 < 2)) {
                str2 = str2 + "/" + str4;
            }
            if (str2 == null || str2.length() <= USER_NAME_SIZE_MAX) {
                return str2;
            }
            return str2.substring(0, USER_NAME_SIZE_MAX);
        }
        Log.m286e(TAG, "Card holder name is null");
        return null;
    }

    private McCasdJwk convertByteArrayToString(byte[] bArr) {
        return (McCasdJwk) McPayloadBuilder.getGson().fromJson(new String(bArr, StandardCharsets.UTF_8), McCasdJwk.class);
    }

    private McCardInfoWrapper getIssuerInitiatedDigitizationData(String str, StringBuilder stringBuilder) {
        if (str == null) {
            Log.m286e(TAG, "McIssuerInitiatedDigitizationData :  IssuerInitiatedDigitizationData is null");
            return null;
        } else if (stringBuilder == null) {
            Log.m286e(TAG, "McIssuerInitiatedDigitizationData :  wrong tav object");
            return null;
        } else {
            McIssuerInitiatedDigitizationData mcIssuerInitiatedDigitizationData = (McIssuerInitiatedDigitizationData) new Gson().fromJson(new String(Base64.decode(str.getBytes(), 0)), McIssuerInitiatedDigitizationData.class);
            if (mcIssuerInitiatedDigitizationData == null || mcIssuerInitiatedDigitizationData.getCardInfo() == null) {
                Log.m286e(TAG, "McIssuerInitiatedDigitizationData : Wrong IssuerInitiatedDigitizationData object");
                return null;
            }
            if (McUtils.isStringValid(mcIssuerInitiatedDigitizationData.getTokenizationAuthenticationValue(), 0, PKIFailureInfo.wrongIntegrity)) {
                stringBuilder.append(mcIssuerInitiatedDigitizationData.getTokenizationAuthenticationValue());
            } else {
                Log.m287i(TAG, "getIssuerInitiatedDigitizationData : TAV isn't available");
            }
            Log.m285d(TAG, "[in] TAV value : " + stringBuilder.toString());
            return mcIssuerInitiatedDigitizationData.getCardInfo();
        }
    }
}
