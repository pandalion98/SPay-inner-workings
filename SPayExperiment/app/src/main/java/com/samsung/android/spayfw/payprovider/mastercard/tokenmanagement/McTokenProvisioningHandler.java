/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 *  com.google.gson.reflect.TypeToken
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.reflect.Type
 *  java.nio.charset.Charset
 *  java.nio.charset.StandardCharsets
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.ApduCommand;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.ApduResponse;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.GetTokenResponse;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.Iso3166Alpha3CountryCode;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McBillingAddress;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCasdJwk;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McDeviceInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McEligibilityReceipt;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McIssuerInitiatedDigitizationData;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSeInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSpsdInfo;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAError;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class McTokenProvisioningHandler {
    private static final String OS_NAME_ANDROID = "Android";
    public static final String STRING_EMPTY = "";
    private static final String TAG = "McTokenProvisioningHandler";
    private static final int USER_NAME_SIZE_MAX = 27;
    private byte[] mAp1CertBuffer;
    private byte[] mAp2CertBuffer;
    private long mDbReference;
    private McTAController mMcTAController;

    public McTokenProvisioningHandler() {
        Log.d(TAG, "McTokenProvisioningHandler: Creating a new instance");
        this.mMcTAController = McTAController.getInstance();
        this.mDbReference = -1L;
    }

    private McCasdJwk convertByteArrayToString(byte[] arrby) {
        return (McCasdJwk)McPayloadBuilder.getGson().fromJson(new String(arrby, StandardCharsets.UTF_8), McCasdJwk.class);
    }

    /*
     * Enabled aggressive block sorting
     */
    private McBillingAddress generateMcBillingAddress(BillingInfo billingInfo) {
        String string;
        if (billingInfo == null) {
            Log.i(TAG, "Input BillingInfo is null -2-");
            return null;
        }
        Log.d(TAG, "McCardInfo : mcCardInfo Sent by app: " + billingInfo.toString());
        McBillingAddress mcBillingAddress = new McBillingAddress();
        String string2 = billingInfo.getCountry();
        if (string2 != null) {
            Iso3166Alpha3CountryCode iso3166Alpha3CountryCode = Iso3166Alpha3CountryCode.getAlpha3Code(string2.toUpperCase());
            string = null;
            if (iso3166Alpha3CountryCode != null) {
                string = iso3166Alpha3CountryCode.name();
            }
        } else {
            string = string2;
        }
        mcBillingAddress.setCity(billingInfo.getCity());
        mcBillingAddress.setCountry(string);
        mcBillingAddress.setLine1(billingInfo.getStreet1());
        mcBillingAddress.setLine2(billingInfo.getStreet2());
        mcBillingAddress.setPostalCode(billingInfo.getZip());
        mcBillingAddress.setCountrySubdivision(billingInfo.getState());
        return mcBillingAddress;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void generateMcCardInfo(McCardInfo mcCardInfo, EnrollCardPanInfo enrollCardPanInfo) {
        if (enrollCardPanInfo != null) {
            mcCardInfo.setAccountNumber(enrollCardPanInfo.getPAN());
            mcCardInfo.setExpiryMonth(enrollCardPanInfo.getExpMonth());
            mcCardInfo.setExpiryYear(enrollCardPanInfo.getExpYear());
            mcCardInfo.setSecurityCode(enrollCardPanInfo.getCVV());
            if (enrollCardPanInfo.getName() != null) {
                mcCardInfo.setCardholderName(this.generateUserName(enrollCardPanInfo.getName()));
                Log.d(TAG, "Card Holder Name: " + mcCardInfo.getCardholderName());
            } else {
                Log.e(TAG, "CardInfo does not have card holder name value");
            }
            mcCardInfo.setSource(CardSource.getMcCardSource(enrollCardPanInfo.getCardEntryMode()).toString());
        }
        Log.d(TAG, "mcCardInfo Source: " + mcCardInfo.getSource());
    }

    private String generatePaymentAppInstanceId(String string) {
        int n2 = 0;
        if (string == null) {
            Log.e(TAG, "Wallet Id is null");
            return null;
        }
        String string2 = DeviceInfo.getDeviceId(this.mMcTAController.getContext());
        int n3 = string2.length();
        int n4 = string.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < 24 - n3; ++i2) {
            stringBuilder.append("0");
        }
        stringBuilder.append(string2);
        while (n2 < 24 - n4) {
            stringBuilder.append("0");
            ++n2;
        }
        stringBuilder.append(string);
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    private String generateUserName(String string) {
        int n2;
        String string2 = null;
        if (string == null) return string2;
        String[] arrstring = string.split("[ ]+");
        if (arrstring.length <= 0) {
            Log.e(TAG, "Card holder name is null");
            return null;
        }
        String string3 = arrstring[0];
        String string4 = arrstring.length > 1 ? arrstring[-1 + arrstring.length] : null;
        if (string3 != null && string3.isEmpty()) {
            string3 = null;
        }
        if (string4 != null && string4.isEmpty()) {
            string4 = null;
        }
        if (string4 != null) {
            int n3 = 27 - string4.length();
            string2 = string4;
            n2 = n3;
        } else {
            n2 = 27;
            string2 = null;
        }
        if (string2 == null && string3 != null) {
            string2 = string3;
        } else if (string2 != null && string3 != null && n2 >= 2) {
            string2 = string2 + "/" + string3;
        }
        if (string2 == null) return string2;
        if (string2.length() <= 27) return string2;
        return string2.substring(0, 27);
    }

    private c getEnrollmentMcDataError(c c2, int n2) {
        c2.a(null);
        c2.setErrorCode(n2);
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private McCardInfoWrapper getIssuerInitiatedDigitizationData(String string, StringBuilder stringBuilder) {
        if (string == null) {
            Log.e(TAG, "McIssuerInitiatedDigitizationData :  IssuerInitiatedDigitizationData is null");
            return null;
        }
        if (stringBuilder == null) {
            Log.e(TAG, "McIssuerInitiatedDigitizationData :  wrong tav object");
            return null;
        }
        McIssuerInitiatedDigitizationData mcIssuerInitiatedDigitizationData = (McIssuerInitiatedDigitizationData)new Gson().fromJson(new String(Base64.decode((byte[])string.getBytes(), (int)0)), McIssuerInitiatedDigitizationData.class);
        if (mcIssuerInitiatedDigitizationData == null || mcIssuerInitiatedDigitizationData.getCardInfo() == null) {
            Log.e(TAG, "McIssuerInitiatedDigitizationData : Wrong IssuerInitiatedDigitizationData object");
            return null;
        }
        if (McUtils.isStringValid(mcIssuerInitiatedDigitizationData.getTokenizationAuthenticationValue(), 0, 2048)) {
            stringBuilder.append(mcIssuerInitiatedDigitizationData.getTokenizationAuthenticationValue());
        } else {
            Log.i(TAG, "getIssuerInitiatedDigitizationData : TAV isn't available");
        }
        Log.d(TAG, "[in] TAV value : " + stringBuilder.toString());
        return mcIssuerInitiatedDigitizationData.getCardInfo();
    }

    private e getProviderResponseData(ApduResponse[] arrapduResponse, int n2) {
        e e2 = new e();
        if (arrapduResponse != null) {
            e2.b(ApduResponse.getNotifyTokenRequest(arrapduResponse));
        }
        e2.setProviderTokenKey(new f(this.getDbReference()));
        e2.setErrorCode(n2);
        return e2;
    }

    private byte[] getappletInstanceIdBytes(String string) {
        byte[] arrby = ApduCommand.ApduParser.convertStringToByteArray(string);
        Log.d(TAG, "Applet Instance Id: " + string + ", Byte Array: " + Arrays.toString((byte[])arrby) + ", Length: " + arrby.length);
        return arrby;
    }

    boolean cleanupDbReference(long l2, McCardMasterDaoImpl mcCardMasterDaoImpl) {
        if (mcCardMasterDaoImpl == null || l2 < 0L) {
            return false;
        }
        this.setDbReference(-1L);
        return mcCardMasterDaoImpl.deleteData(l2);
    }

    public long getDbReference() {
        return this.mDbReference;
    }

    /*
     * Enabled aggressive block sorting
     */
    public c getEnrollmentMcData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        int n2;
        c c2 = new c();
        c2.setErrorCode(0);
        if (this.mMcTAController == null) {
            Log.e(TAG, "mMcTAController is null");
            return this.getEnrollmentMcDataError(c2, -6);
        }
        McCardInfoWrapper mcCardInfoWrapper = new McCardInfoWrapper();
        String string = this.generatePaymentAppInstanceId(enrollCardInfo.getWalletId());
        McTAController.CasdParams casdParams = this.mMcTAController.getCasdParameters();
        if (this.mAp1CertBuffer == null || this.mAp2CertBuffer == null || casdParams != null) {
            Log.e(TAG, "Need Certs Update");
            c2.a(McPayloadBuilder.buildMcEnrollmentRequestPayload(new McSeInfo(DeviceInfo.getDeviceId(this.mMcTAController.getContext()), null), mcCardInfoWrapper, null, null, null, string, null));
            return c2;
        }
        McCardInfo mcCardInfo = new McCardInfo();
        StringBuilder stringBuilder = new StringBuilder();
        Log.d(TAG, "cardInfo Type : " + enrollCardInfo.getClass().getSimpleName());
        mcCardInfo.setBillingAddress(this.generateMcBillingAddress(billingInfo));
        mcCardInfo.setDataValidUntilTimestamp(null);
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            this.generateMcCardInfo(mcCardInfo, (EnrollCardPanInfo)enrollCardInfo);
            mcCardInfo.refactorBillingAddress();
        } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            EnrollCardReferenceInfo enrollCardReferenceInfo = (EnrollCardReferenceInfo)enrollCardInfo;
            if (enrollCardReferenceInfo.getReferenceType() == null || enrollCardReferenceInfo.getExtraEnrollData() == null) {
                Log.e(TAG, "enrollCardReferenceInfo reference type or extra data is null");
                return this.getEnrollmentMcDataError(c2, -9);
            }
            Log.d(TAG, "Card Type : " + enrollCardReferenceInfo.getReferenceType());
            if (enrollCardReferenceInfo.getReferenceType().equals((Object)"app2app")) {
                mcCardInfo.setSource(CardSource.CARD_ADDED_VIA_APPLICATION.toString());
                byte[] arrby = enrollCardReferenceInfo.getExtraEnrollData().getByteArray("enrollPayload");
                if (arrby == null) {
                    Log.e(TAG, "mc app2app issuer payload is null");
                    return this.getEnrollmentMcDataError(c2, -9);
                }
                mcCardInfoWrapper = this.getIssuerInitiatedDigitizationData(new String(arrby), stringBuilder);
                if (mcCardInfoWrapper == null) {
                    Log.e(TAG, "mc app2app mcCardWrapper is null");
                    return this.getEnrollmentMcDataError(c2, -9);
                }
                Log.d(TAG, "mcCardWrapper: " + mcCardInfoWrapper.toString());
            } else {
                mcCardInfo.setSource(CardSource.CARD_ON_FILE.toString());
                mcCardInfoWrapper.setPanUniqueReference(enrollCardReferenceInfo.getExtraEnrollData().getString("cardReferenceId"));
            }
        }
        mcCardInfoWrapper.setCardInfo(mcCardInfo);
        McSpsdInfo mcSpsdInfo = new McSpsdInfo();
        String string2 = McTokenManager.getContext().getString(2130968611);
        McCardMaster mcCardMaster = new McCardMaster();
        McCardMasterDaoImpl mcCardMasterDaoImpl = new McCardMasterDaoImpl(McTokenManager.getContext());
        long l2 = mcCardMasterDaoImpl.saveData(mcCardMaster);
        if (l2 == -1L) {
            Log.e(TAG, "Err...Failed to create empty db record");
            return this.getEnrollmentMcDataError(c2, -2);
        }
        Log.d(TAG, "Created a new Db record with value=" + l2);
        McAidSelector mcAidSelector = new McAidSelector(mcCardInfo.getAccountNumber(), mcCardInfo.getSource().equals((Object)CardSource.CARD_ADDED_MANUALLY.toString()));
        if (mcAidSelector.getAid() == null) {
            Log.e(TAG, "Err...Failed to create empty db record");
            return this.getEnrollmentMcDataError(c2, -2);
        }
        String string3 = mcAidSelector.generateAppletInstanceAids(l2);
        if (TextUtils.isEmpty((CharSequence)string3)) {
            Log.e(TAG, "Applet instance id is null or empty");
            return this.getEnrollmentMcDataError(c2, -2);
        }
        String string4 = mcAidSelector.getAid();
        if (TextUtils.isEmpty((CharSequence)string4)) {
            Log.e(TAG, "AID selection failed!!");
            return this.getEnrollmentMcDataError(c2, -2);
        }
        McTACommands.GetSpsd.Response.SpsdResponse spsdResponse = this.mMcTAController.getSpsdInfo(this.mAp1CertBuffer, McUtils.convertStirngToByteArray(string4));
        if (spsdResponse == null || spsdResponse.getRgk().length == 0 || spsdResponse.getSkuKeys().length == 0 || spsdResponse.getSpsdSequenceCounter().length == 0 || spsdResponse.getCertEncrypt().length == 0) {
            McTAError.MC_PAY_OK.getValue();
            long l3 = spsdResponse != null ? spsdResponse.getReturnCode().get() : McTAError.MC_PAY_INTERNAL_ERROR.getValue();
            Log.e(TAG, "Spsd Response from TA failed.. Check for Null values..Abort Enrollment Err : " + l3);
            return this.getEnrollmentMcDataError(c2, -6);
        }
        McTAError mcTAError = McTAError.getMcTAError(spsdResponse.getReturnCode().get());
        Log.d(TAG, "getSpsdInfo : TA Result : " + mcTAError.name());
        if (mcTAError != McTAError.MC_PAY_OK) {
            Log.e(TAG, "Spsd Response from TA failed.. Abort Enrollment");
            return this.getEnrollmentMcDataError(c2, -6);
        }
        mcSpsdInfo.setSpsdSequenceCounter(McUtils.byteArrayToHex(spsdResponse.getSpsdSequenceCounter()));
        mcSpsdInfo.setRgk(McUtils.byteArrayToHex(spsdResponse.getRgk()));
        mcCardMaster.setRgkDerivedkeys(spsdResponse.getSkuKeys());
        mcCardMasterDaoImpl.updateData(mcCardMaster, l2);
        this.setDbReference(l2);
        int n3 = spsdResponse.getCasdCertType();
        if (n3 != 1 && (n2 = spsdResponse.getCasdCertType()) != 2 || spsdResponse.getCertEncrypt() == null) {
            Log.e(TAG, "getEnrollmentMcData: Unknown type / null CASD cert detected");
            return this.getEnrollmentMcDataError(c2, -6);
        }
        int n4 = spsdResponse.getCasdCertType();
        if (n4 == 1) {
            Log.e(TAG, "getEnrollmentMcData: CASD cert type x509 detected");
            mcSpsdInfo.setCasdPkJwk(this.convertByteArrayToString(spsdResponse.getCertEncrypt()));
            mcSpsdInfo.setCasdPkCertificate(null);
        } else {
            int n5 = spsdResponse.getCasdCertType();
            if (n5 == 2) {
                Log.e(TAG, "getEnrollmentMcData: CASD cert type GP detected");
                mcSpsdInfo.setCasdPkCertificate(McUtils.byteArrayToHex(spsdResponse.getCertEncrypt()));
                mcSpsdInfo.setCasdPkJwk(null);
            }
        }
        mcSpsdInfo.setAid(mcAidSelector.getAid());
        mcSpsdInfo.setAppletInstanceAid(string3);
        if (!mcCardInfo.getSource().equals((Object)CardSource.CARD_ADDED_VIA_APPLICATION.toString())) {
            String string5 = CryptoUtils.convertbyteToHexString(CryptoUtils.getShaDigest(this.mAp2CertBuffer, CryptoUtils.ShaConstants.SHA1));
            Log.d(TAG, "Certificate Alias: " + string5);
            mcCardInfoWrapper.setCertificateFingerprint(string5);
            McTACommands.CardInfoEncryption.Response.CardData cardData = this.mMcTAController.encryptCardInfo(McPayloadBuilder.toByteArray(mcCardInfoWrapper.getUnenryptedData()), this.mAp2CertBuffer);
            if (cardData == null) {
                Log.e(TAG, "getEnrollmentMcData: ecryptCardInfo is null");
                return this.getEnrollmentMcDataError(c2, -6);
            }
            McTAError mcTAError2 = McTAError.getMcTAError(cardData.getReturnCode());
            Log.d(TAG, "encryptCardInfo : TA Result : " + mcTAError2.name());
            if (mcTAError2 != McTAError.MC_PAY_OK) {
                Log.e(TAG, "Failed to encrypt Card Information Error : " + (Object)((Object)mcTAError2));
                return this.getEnrollmentMcDataError(c2, -6);
            }
            if (cardData.getEncryptedData() != null) {
                mcCardInfoWrapper.setEncryptedData(McUtils.byteArrayToHex(cardData.getEncryptedData()));
            }
            if (cardData.getEncryptedKey() != null) {
                mcCardInfoWrapper.setEncryptedKey(McUtils.byteArrayToHex(cardData.getEncryptedKey()));
            }
            mcCardInfoWrapper.setOaepHashingAlgorithm(null);
        }
        mcCardInfoWrapper.setCardInfo(null);
        McDeviceInfo mcDeviceInfo = new McDeviceInfo();
        mcDeviceInfo.setOsName(OS_NAME_ANDROID);
        mcDeviceInfo.setOsVersion(String.valueOf((int)Build.VERSION.SDK_INT));
        mcDeviceInfo.setNfcCapable(true);
        if (!McProvider.getContext().getPackageManager().hasSystemFeature("android.hardware.nfc")) {
            mcDeviceInfo.setNfcCapable(false);
        }
        McSeInfo mcSeInfo = new McSeInfo(DeviceInfo.getDeviceId(this.mMcTAController.getContext()), null);
        String string6 = stringBuilder.toString().isEmpty() ? null : stringBuilder.toString();
        c2.a(McPayloadBuilder.buildMcEnrollmentRequestPayload(mcSeInfo, mcCardInfoWrapper, string2, mcSpsdInfo, mcDeviceInfo, string, string6));
        return c2;
    }

    public byte[] processSignatureData(byte[] arrby, int n2) {
        if (this.mMcTAController == null) {
            return null;
        }
        return this.mMcTAController.processSignatureData(arrby, n2);
    }

    public e provisionToken(JsonObject jsonObject, int n2) {
        if (n2 != 2) {
            Log.d(TAG, "provisionToken Called invalid src type: " + n2);
            return this.getProviderResponseData(null, -4);
        }
        GetTokenResponse getTokenResponse = GetTokenResponse.parseJson(jsonObject);
        if (getTokenResponse == null || McTokenManager.getContext() == null) {
            Log.e(TAG, "provisionToken Called with no data");
            return this.getProviderResponseData(null, -4);
        }
        if (getTokenResponse.getAppletInstanceAid() == null || getTokenResponse.getAppletInstanceAid().isEmpty()) {
            Log.e(TAG, "provisionToken : Incorrect AppletInstanceAid");
            return this.getProviderResponseData(null, -4);
        }
        McCardMasterDaoImpl mcCardMasterDaoImpl = new McCardMasterDaoImpl(McTokenManager.getContext());
        this.setDbReference(mcCardMasterDaoImpl.getDbIdFromTokenUniqueRefence(getTokenResponse.getMdesTokenUniqueReference()));
        if (this.getDbReference() == -1L) {
            Log.d(TAG, "Db reference lost before token provisioning call. Cannot associate data");
            return this.getProviderResponseData(null, -2);
        }
        Type type = new TypeToken<DC_CP>(){}.getType();
        McCardProfileDaoImpl mcCardProfileDaoImpl = new McCardProfileDaoImpl(McTokenManager.getContext(), type);
        getTokenResponse.setPanUniqueReference("panUniqueReference");
        if (!GetTokenResponse.isCreateTokenRequestValid(getTokenResponse)) {
            Log.d(TAG, "provisionToken Called with no data");
            return this.getProviderResponseData(null, -4);
        }
        String string = getTokenResponse.getMdesTokenUniqueReference();
        byte[] arrby = ApduCommand.ApduParser.generateApduForTa(getTokenResponse.getApduCommands());
        if (arrby == null) {
            Log.e(TAG, "Internal error. Failed to extract apdu");
            return this.getProviderResponseData(null, -2);
        }
        McCardMaster mcCardMaster = (McCardMaster)mcCardMasterDaoImpl.getData(this.getDbReference());
        if (mcCardMaster == null || mcCardMaster.getRgkDerivedkeys() == null) {
            Log.e(TAG, "SKU keys missing in db");
            return this.getProviderResponseData(null, -2);
        }
        ApduResponse[] arrapduResponse = ApduResponse.initApduResponseFromCommand(getTokenResponse.getApduCommands());
        if (arrapduResponse == null) {
            Log.e(TAG, "Internal Err while process apduResponses init");
            return this.getProviderResponseData(null, -2);
        }
        McTACommands.ProvisionToken.Response response = this.mMcTAController.provisionToken(arrby, mcCardMaster.getRgkDerivedkeys(), this.getappletInstanceIdBytes(getTokenResponse.getAppletInstanceAid()));
        if (response == null || response.mRetVal == null || response.mRetVal.result == null || response.mRetVal.rApdus == null || response.mRetVal.cardSecureData == null) {
            Log.e(TAG, "TA returnes empty data for a provisionToken in the response.");
            return this.getProviderResponseData(null, -6);
        }
        McTAError mcTAError = McTAError.getMcTAError(response.mRetVal.result.get());
        Log.d(TAG, "provisionToken : TA Result : " + mcTAError.name());
        ApduResponse[] arrapduResponse2 = ApduResponse.generateApduFromTa(arrapduResponse, response.mRetVal.rApdus.getData());
        if (arrapduResponse2 == null) {
            Log.e(TAG, "Internal Err while process apduResponses from TEE");
            return this.getProviderResponseData(arrapduResponse2, -9);
        }
        McCardClearData mcCardClearData = ApduResponse.getMcCardClearDataObject();
        MCUnusedDGIElements mCUnusedDGIElements = mcCardClearData.getMCUnusedDGIElements();
        DC_CP dC_CP = mcCardClearData.getPaymentProfile();
        MCProfilesTable mCProfilesTable = mcCardClearData.getProfilesTable();
        if (mCProfilesTable == null) {
            Log.e(TAG, "Cannot get profiles table data. Deserializer returns null.");
            return this.getProviderResponseData(arrapduResponse2, -9);
        }
        MCBaseCardProfile mCBaseCardProfile = new MCBaseCardProfile();
        mCBaseCardProfile.setDigitalizedCardContainer(dC_CP);
        mCBaseCardProfile.setTADataContainer(response.mRetVal.cardSecureData.getData());
        mCBaseCardProfile.setTaAtcContainer(response.mRetVal.wrappedAtcObj.getData());
        mCBaseCardProfile.setUniqueTokenReferenceId(this.getDbReference());
        mCBaseCardProfile.setTAProfilesTable(mCProfilesTable);
        mCBaseCardProfile.setUnusedDGIElements(mCUnusedDGIElements);
        if (mcCardProfileDaoImpl.saveData(mCBaseCardProfile) == -1L) {
            Log.e(TAG, "Unable to store Profile in Db");
            return this.getProviderResponseData(arrapduResponse2, -9);
        }
        mcCardMaster.setTokenUniqueReference(string);
        mcCardMaster.setProvisionedState(1L);
        if (!mcCardMasterDaoImpl.updateData(mcCardMaster, this.getDbReference())) {
            Log.e(TAG, "provisionToken: Update operation failed.. Discarding provisioned data");
            mcCardMasterDaoImpl.deleteData(this.getDbReference());
            return this.getProviderResponseData(arrapduResponse2, -2);
        }
        return this.getProviderResponseData(arrapduResponse2, 0);
    }

    public boolean saveEligibilityReceipt(McEligibilityReceipt mcEligibilityReceipt) {
        return true;
    }

    public void setDbReference(long l2) {
        this.mDbReference = l2;
    }

    public void setMcCerts(byte[] arrby, byte[] arrby2) {
        this.mAp1CertBuffer = arrby;
        this.mAp2CertBuffer = arrby2;
    }

    public void setUniqueTokenReference(String string) {
        McCardMasterDaoImpl mcCardMasterDaoImpl = new McCardMasterDaoImpl(McTokenManager.getContext());
        McCardMaster mcCardMaster = (McCardMaster)mcCardMasterDaoImpl.getData(this.getDbReference());
        if (mcCardMaster != null) {
            mcCardMaster.setTokenUniqueReference(string);
            mcCardMasterDaoImpl.updateData(mcCardMaster, this.getDbReference());
            return;
        }
        Log.e(TAG, " No valid McCardMaster object : " + this.getDbReference());
    }

    private static final class CardSource
    extends Enum<CardSource> {
        private static final /* synthetic */ CardSource[] $VALUES;
        public static final /* enum */ CardSource CARD_ADDED_MANUALLY;
        public static final /* enum */ CardSource CARD_ADDED_VIA_APPLICATION;
        public static final /* enum */ CardSource CARD_ON_FILE;
        private static final Map<String, CardSource> mCardSourceMap;
        private final String mSource;

        static {
            CARD_ON_FILE = new CardSource("FILE");
            CARD_ADDED_MANUALLY = new CardSource("MAN");
            CARD_ADDED_VIA_APPLICATION = new CardSource("APP");
            CardSource[] arrcardSource = new CardSource[]{CARD_ON_FILE, CARD_ADDED_MANUALLY, CARD_ADDED_VIA_APPLICATION};
            $VALUES = arrcardSource;
            mCardSourceMap = new HashMap();
            for (CardSource cardSource : CardSource.values()) {
                mCardSourceMap.put((Object)cardSource.mSource, (Object)cardSource);
            }
            mCardSourceMap.put((Object)"OCR", (Object)CARD_ADDED_MANUALLY);
        }

        private CardSource(String string2) {
            this.mSource = string2;
        }

        public static CardSource getMcCardSource(String string) {
            return (CardSource)((Object)mCardSourceMap.get((Object)string));
        }

        public static CardSource valueOf(String string) {
            return (CardSource)Enum.valueOf(CardSource.class, (String)string);
        }

        public static CardSource[] values() {
            return (CardSource[])$VALUES.clone();
        }
    }

}

