package com.samsung.android.spayfw.payprovider.discover.tokenmanager;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.discover.DiscoverPayProvider;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.DcStorageManager;
import com.samsung.android.spayfw.payprovider.discover.db.DcStorageManager.ResultCode;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverDataTags;
import com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverInnAppPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIssuerOptions;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CL;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CRM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile.CVM;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.TLVData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.AccountEligibilityRequestData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.AccountProvisionRequestData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearCardInfo;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearProfileData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearProvisionData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearRefreshCredentialsRequestData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.DevicePublicKeyContext;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ProvisionCredentialsData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.RefreshCredentialsRequestData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.RefreshCredentialsResponseData;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.TransactionProfile;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ZIP_MS_TransactionProfile;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.CardCtxEncryption.Response.C0511a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DevicePublicKeyCtxEncryption.Response.C0513a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessCardProfile.Response.C0533a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessDataOperationType;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ProcessReplenishmentData.Response.C0537a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.ReplenishContextEncryption.Response.C0539a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException.Code;
import com.samsung.android.spayfw.payprovider.discover.util.CryptoUtils;
import com.samsung.android.spayfw.payprovider.discover.util.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bouncycastle.util.encoders.Hex;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.tokenmanager.b */
public class DcTokenManager {
    private static Gson mGson;
    private DcCardMasterDaoImpl wu;
    private SecureContext wv;
    private boolean ww;

    static {
        mGson = new Gson();
    }

    public DcTokenManager(Context context) {
        this.wu = null;
        this.ww = false;
        this.wu = new DcCardMasterDaoImpl(context);
    }

    public void m1023d(ProviderTokenKey providerTokenKey) {
        Log.m287i("DCSDK_DcTokenManager", "delete");
        if (providerTokenKey == null) {
            Log.m286e("DCSDK_DcTokenManager", "Token Id is null");
            return;
        }
        try {
            if (providerTokenKey.cn() != null) {
                if (DcStorageManager.m872k(providerTokenKey.cm()) != ResultCode.ERR_NONE) {
                    Log.m286e("DCSDK_DcTokenManager", "Failed to delete token");
                    return;
                }
            } else if (providerTokenKey.getTrTokenId() == null) {
                Log.m286e("DCSDK_DcTokenManager", "updateToken: getTrTokenId is null");
                return;
            }
            Log.m285d("DCSDK_DcTokenManager", "Successfully deleted Token rowId: " + providerTokenKey.cn() + ", TR Id: " + providerTokenKey.getTrTokenId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            if (this.ww) {
                Log.m286e("DCSDK_DcTokenManager", "delete: mProcessTokenComplete - true but failed to get token key from tokenId");
            }
            DcStorageManager.cH();
        } catch (Exception e2) {
            e2.printStackTrace();
            DcStorageManager.cH();
        }
    }

    public ProviderRequestData m1016a(EnrollCardPanInfo enrollCardPanInfo, BillingInfo billingInfo, List<byte[]> list) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        Log.m287i("DCSDK_DcTokenManager", "getEnrollmentData");
        if (!m1014b(enrollCardPanInfo)) {
            Log.m286e("DCSDK_DcTokenManager", "getEnrollmentData: validateCardInfo failed");
            providerRequestData.setErrorCode(-4);
        } else if (!m1013a(billingInfo)) {
            Log.m286e("DCSDK_DcTokenManager", "getEnrollmentData: validateBillingInfo failed");
            providerRequestData.setErrorCode(-4);
        } else if (list == null) {
            Log.m286e("DCSDK_DcTokenManager", "getEnrollmentData: serverCertChain is null");
            providerRequestData.setErrorCode(-4);
        } else {
            C0511a a = m1011a(ClearCardInfo.getEnrollmentPayload(enrollCardPanInfo, billingInfo), (List) list);
            if (a == null) {
                Log.m286e("DCSDK_DcTokenManager", "createSecureCardContext(Enrollment) Failed");
                providerRequestData.setErrorCode(-6);
            } else {
                C0511a a2 = m1011a(ClearCardInfo.getProvisionPayload(enrollCardPanInfo), (List) list);
                if (a2 == null) {
                    Log.m286e("DCSDK_DcTokenManager", "createSecureCardContext(Provision) Failed");
                    providerRequestData.setErrorCode(-6);
                } else {
                    AccountEligibilityRequestData accountEligibilityRequestData = new AccountEligibilityRequestData();
                    SecureContext secureContext = new SecureContext();
                    secureContext.setEncryptedPayload(new String(a.getEncryptedData()));
                    Log.m287i("DCSDK_DcTokenManager", "Length of Encrypted Data: " + a.getEncryptedData().length);
                    accountEligibilityRequestData.setSecureCardContext(secureContext);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("accountEligibilityRequest", m1015h(accountEligibilityRequestData));
                    Log.m287i("DCSDK_DcTokenManager", "Eligibility Req Data: " + jsonObject.toString());
                    providerRequestData.m822a(jsonObject);
                    Bundle bundle = new Bundle();
                    bundle.putString("emailHash", aP(enrollCardPanInfo.getUserEmail()));
                    providerRequestData.m823e(bundle);
                    this.wv = new SecureContext(new String(a2.getEncryptedData()));
                }
            }
        }
        return providerRequestData;
    }

    public ProviderRequestData m1021b(EnrollCardReferenceInfo enrollCardReferenceInfo) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        Log.m287i("DCSDK_DcTokenManager", "getPushEnrollementData");
        AccountEligibilityRequestData accountEligibilityRequestData = new AccountEligibilityRequestData();
        SecureContext secureContext = new SecureContext();
        String str = new String(enrollCardReferenceInfo.getExtraEnrollData().getByteArray(EnrollCardReferenceInfo.ENROLL_PAYLOAD));
        secureContext.setEncryptedPayload(str);
        Log.m287i("DCSDK_DcTokenManager", "Length of Encrypted Data: " + str.length() + ", Data: " + str.substring(0, 50));
        accountEligibilityRequestData.setSecureCardContext(secureContext);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("accountEligibilityRequest", m1015h(accountEligibilityRequestData));
        Log.m287i("DCSDK_DcTokenManager", "Eligibility Req Data: " + jsonObject.toString());
        providerRequestData.m822a(jsonObject);
        Bundle bundle = new Bundle();
        bundle.putString("emailHash", aP(enrollCardReferenceInfo.getUserEmail()));
        providerRequestData.m823e(bundle);
        this.wv = secureContext;
        return providerRequestData;
    }

    public ProviderRequestData m1022c(ProvisionTokenInfo provisionTokenInfo) {
        Log.m287i("DCSDK_DcTokenManager", "getProvisionData");
        ProviderRequestData providerRequestData = new ProviderRequestData();
        AccountProvisionRequestData accountProvisionRequestData = new AccountProvisionRequestData();
        DevicePublicKeyContext eh = eh();
        if (eh != null) {
            accountProvisionRequestData.setSecureCardContext(new SecureContext(this.wv.getEncryptedPayload()));
            accountProvisionRequestData.setDevicePublicKeyContext(eh);
            Log.m285d("DCSDK_DcTokenManager", "2.0 Prov Request: ");
            providerRequestData.m822a(m1015h(accountProvisionRequestData));
            Bundle bundle = new Bundle();
            bundle.putSerializable("riskData", DcRiskData.eg().m1008b(provisionTokenInfo));
            providerRequestData.m823e(bundle);
            providerRequestData.setErrorCode(0);
        } else {
            Log.m286e("DCSDK_DcTokenManager", "getProvisionData: TA Response Null");
            providerRequestData.setErrorCode(-2);
        }
        return providerRequestData;
    }

    private DevicePublicKeyContext eh() {
        C0513a ev;
        Log.m285d("DCSDK_DcTokenManager", "getDevicePubKeyContext");
        try {
            ev = DcTAController.eu().ev();
        } catch (DcTAException e) {
            Log.m286e("DCSDK_DcTokenManager", "getDevicePubKeyContext: TA Exception - " + e.toString());
            e.printStackTrace();
            ev = null;
        } catch (Exception e2) {
            Log.m286e("DCSDK_DcTokenManager", "getDevicePubKeyContext: Exception - " + e2.getMessage());
            e2.printStackTrace();
            ev = null;
        }
        if (ev == null) {
            return null;
        }
        String str = new String(ev.getEncryptedData());
        Log.m285d("DCSDK_DcTokenManager", "certChain = ");
        Log.m285d("DCSDK_DcTokenManager", str);
        DevicePublicKeyContext devicePublicKeyContext = new DevicePublicKeyContext();
        devicePublicKeyContext.setPublicKeyCertificateChain(str);
        return devicePublicKeyContext;
    }

    public ProviderResponseData m1019a(String str, JsonObject jsonObject, int i, List<byte[]> list) {
        Log.m285d("DCSDK_DcTokenManager", "processToken " + jsonObject.toString());
        ProviderResponseData providerResponseData = new ProviderResponseData();
        ProvisionCredentialsData provisionCredentialsData = (ProvisionCredentialsData) mGson.fromJson(jsonObject.getAsJsonObject("provisionCredentialsContext"), ProvisionCredentialsData.class);
        if (provisionCredentialsData == null) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Failed to parse responseData");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        }
        C0533a c0533a = null;
        if (list == null) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: serverCertChain is null");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        }
        try {
            c0533a = DcTAController.eu().m1042a(provisionCredentialsData.getSecureProvisionCredentialsRequestContext().getEncryptedPayload().getBytes(), (List) list, true);
        } catch (DcTAException e) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: TA Exception" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Exception " + e2.getMessage());
            e2.printStackTrace();
        }
        if (c0533a == null) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Failed to process card profile");
            providerResponseData.setErrorCode(-6);
            return providerResponseData;
        }
        DcCardMaster dcCardMaster = new DcCardMaster();
        DcCardMasterDaoImpl dcCardMasterDaoImpl = new DcCardMasterDaoImpl(DiscoverPayProvider.cC());
        long j = -1;
        dcCardMaster.setDpanSuffix(BuildConfig.FLAVOR);
        dcCardMaster.setFpanSuffix(BuildConfig.FLAVOR);
        dcCardMaster.setIsProvisioned(1);
        dcCardMaster.setTokenId(provisionCredentialsData.getTokenId());
        try {
            j = dcCardMasterDaoImpl.saveData(dcCardMaster);
        } catch (DcDbException e3) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: DcDbException " + e3.getMessage());
            e3.printStackTrace();
        }
        if (j == -1) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Failed to process card profile");
            providerResponseData.setErrorCode(-2);
            return providerResponseData;
        }
        this.ww = true;
        DiscoverPaymentCard a = DcTokenManager.m1009a(j, c0533a);
        if (a == null) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Failed to convert2DiscoverPaymentCard");
            providerResponseData.setErrorCode(-2);
            return providerResponseData;
        }
        int lowCredentialsThreshold;
        ResultCode resultCode;
        a.setSecureObject(c0533a.getEncryptedData());
        a.setOTPK(c0533a.eq());
        ResultCode a2 = DcStorageManager.m862a(a);
        try {
            lowCredentialsThreshold = ((ClearProvisionData) mGson.fromJson(c0533a.er(), ClearProvisionData.class)).getProfileData().getConstraints().getLowCredentialsThreshold();
        } catch (NullPointerException e4) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Failed to get Replenishment Threshold value - " + e4.getMessage() + ". Using default value - " + 5);
            e4.printStackTrace();
            lowCredentialsThreshold = 5;
        }
        if (a2 == ResultCode.ERR_NONE) {
            dcCardMaster.setRemainingOtpkCount((long) c0533a.es());
            dcCardMaster.setReplenishmentThreshold((long) lowCredentialsThreshold);
            try {
                this.wu.updateData(dcCardMaster, j);
                resultCode = a2;
            } catch (DcDbException e32) {
                e32.printStackTrace();
                resultCode = ResultCode.ERR_UPDATE_DATA_FAILED;
            }
        } else {
            resultCode = a2;
        }
        if (resultCode != ResultCode.ERR_NONE) {
            Log.m286e("DCSDK_DcTokenManager", "processToken: Failed to save Discover Payment Card object " + resultCode.getErrorMessage());
            providerResponseData.setErrorCode(-2);
            try {
                this.wu.deleteData(j);
            } catch (DcDbException e322) {
                e322.printStackTrace();
            }
            return providerResponseData;
        }
        providerResponseData.setProviderTokenKey(new ProviderTokenKey(j));
        providerResponseData.setErrorCode(0);
        return providerResponseData;
    }

    public String m1024j(byte[] bArr) {
        byte[] a;
        try {
            a = DcTAController.eu().m1045a(bArr, ProcessDataOperationType.OP_ENCRYPTION);
        } catch (DcTAException e) {
            Log.m286e("DCSDK_DcTokenManager", "encryptSignatureData: " + e.toString());
            e.printStackTrace();
            a = null;
        } catch (Exception e2) {
            Log.m286e("DCSDK_DcTokenManager", "encryptSignatureData: Exception Occured - " + e2.getMessage());
            e2.printStackTrace();
            a = null;
        }
        if (a != null) {
            return Base64.encodeToString(a, 2);
        }
        return null;
    }

    public byte[] aO(String str) {
        byte[] decode = Base64.decode(str, 2);
        if (decode != null) {
            try {
                return DcTAController.eu().m1045a(decode, ProcessDataOperationType.OP_DECRYPTION);
            } catch (DcTAException e) {
                Log.m286e("DCSDK_DcTokenManager", "decryptSignatureData: " + e.toString());
                e.printStackTrace();
            } catch (Exception e2) {
                Log.m286e("DCSDK_DcTokenManager", "decryptSignatureData: Exception Occured - " + e2.getMessage());
                e2.printStackTrace();
            }
        }
        return null;
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        RefreshCredentialsResponseData refreshCredentialsResponseData = (RefreshCredentialsResponseData) mGson.fromJson(jsonObject.getAsJsonObject("refreshCredentialsResponse"), RefreshCredentialsResponseData.class);
        return (refreshCredentialsResponseData == null || refreshCredentialsResponseData.getBundleSeqNum() == null) ? false : true;
    }

    public ProviderRequestData m1017a(ProviderTokenKey providerTokenKey, List<byte[]> list) {
        Log.m287i("DCSDK_DcTokenManager", "generateReplenishRequest");
        ProviderRequestData providerRequestData = new ProviderRequestData();
        RefreshCredentialsRequestData refreshCredentialsRequestData = new RefreshCredentialsRequestData();
        if (list == null) {
            Log.m286e("DCSDK_DcTokenManager", "generateReplenishRequest: serverCertChain is null");
            providerRequestData.setErrorCode(-4);
            return providerRequestData;
        }
        DcCardMaster dcCardMaster;
        byte[] h = DcStorageManager.m869h(providerTokenKey.cm());
        try {
            dcCardMaster = (DcCardMaster) this.wu.getData(providerTokenKey.cm());
        } catch (DcDbException e) {
            e.printStackTrace();
            dcCardMaster = null;
        }
        if (dcCardMaster == null) {
            Log.m286e("DCSDK_DcTokenManager", "generateReplenishRequest: Failed to load CardMaster record for id: " + providerTokenKey.cm());
            providerRequestData.setErrorCode(-2);
            return providerRequestData;
        }
        C0539a a;
        String toJson = mGson.toJson(new ClearRefreshCredentialsRequestData(dcCardMaster.getTokenId()));
        Log.m285d("DCSDK_DcTokenManager", "Replenish Request Payload: " + toJson);
        try {
            a = DcTAController.eu().m1043a(h, (List) list, toJson);
        } catch (DcTAException e2) {
            Log.m286e("DCSDK_DcTokenManager", "generateReplenishRequest: TA Exception - " + e2.toString());
            e2.printStackTrace();
            a = null;
        } catch (Exception e3) {
            Log.m286e("DCSDK_DcTokenManager", "generateReplenishRequest: Exception - " + e3.getMessage());
            e3.printStackTrace();
            a = null;
        }
        if (a == null) {
            Log.m286e("DCSDK_DcTokenManager", "TA Failed");
            providerRequestData.setErrorCode(-6);
            return providerRequestData;
        }
        DevicePublicKeyContext eh = eh();
        if (eh == null) {
            Log.m286e("DCSDK_DcTokenManager", "Failed to get DevicePublicKeyContext");
            providerRequestData.setErrorCode(-9);
            return providerRequestData;
        }
        SecureContext secureContext = new SecureContext(new String(a.getEncryptedData()));
        refreshCredentialsRequestData.setTokenId(dcCardMaster.getTokenId());
        refreshCredentialsRequestData.setRequestContext(secureContext);
        refreshCredentialsRequestData.setDevicePublicKeyContext(eh);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("refreshCredentialsRequest", m1015h(refreshCredentialsRequestData));
        providerRequestData.m822a(jsonObject);
        return providerRequestData;
    }

    public ProviderResponseData m1020a(String str, ProviderTokenKey providerTokenKey, JsonObject jsonObject, TokenStatus tokenStatus, List<byte[]> list) {
        Log.m285d("DCSDK_DcTokenManager", "processOTPK " + jsonObject.toString());
        ProviderResponseData providerResponseData = new ProviderResponseData();
        RefreshCredentialsResponseData refreshCredentialsResponseData = (RefreshCredentialsResponseData) mGson.fromJson(jsonObject.getAsJsonObject("refreshCredentialsResponse"), RefreshCredentialsResponseData.class);
        if (refreshCredentialsResponseData == null) {
            Log.m286e("DCSDK_DcTokenManager", "processOTPK: Failed to parse responseData");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        } else if (list == null) {
            Log.m286e("DCSDK_DcTokenManager", "processOTPK: serverCertChain is null");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        } else {
            C0537a c0537a = null;
            try {
                c0537a = DcTAController.eu().m1048b(refreshCredentialsResponseData.getResponseContext().getEncryptedPayload().getBytes(), DcStorageManager.m869h(providerTokenKey.cm()), list, true);
            } catch (DcTAException e) {
                Log.m286e("DCSDK_DcTokenManager", "processOTPK: TA Exception - " + e.toString());
                e.printStackTrace();
            } catch (Exception e2) {
                Log.m286e("DCSDK_DcTokenManager", "processOTPK: Exception - " + e2.getMessage());
                e2.printStackTrace();
            }
            if (c0537a == null) {
                Log.m286e("DCSDK_DcTokenManager", "processOTPK: Failed to process OTPK in TA");
                providerResponseData.setErrorCode(-6);
                return providerResponseData;
            }
            ResultCode c = DcStorageManager.m865c(providerTokenKey.cm(), c0537a.eq());
            if (c == ResultCode.ERR_NONE) {
                Log.m285d("DCSDK_DcTokenManager", "saveOTPKData Success");
                c = DcStorageManager.m855a(providerTokenKey.cm(), (long) c0537a.es());
            }
            if (c != ResultCode.ERR_NONE) {
                Log.m286e("DCSDK_DcTokenManager", "processOTPK: Failed to store data in DB");
                providerResponseData.setErrorCode(-2);
                return providerResponseData;
            }
            providerResponseData.setErrorCode(0);
            return providerResponseData;
        }
    }

    public ProviderResponseData m1018a(ProviderTokenKey providerTokenKey, JsonObject jsonObject, TokenStatus tokenStatus) {
        Log.m287i("DCSDK_DcTokenManager", "updateToken:Status - " + tokenStatus.getCode());
        if (jsonObject != null) {
            Log.m285d("DCSDK_DcTokenManager", "responseData: " + jsonObject.getAsString());
        }
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        try {
            if (providerTokenKey.cn() != null) {
                DcStorageManager.m860a(providerTokenKey.cm(), tokenStatus.getCode());
            } else if (providerTokenKey.getTrTokenId() == null) {
                Log.m286e("DCSDK_DcTokenManager", "updateToken: getTrTokenId is null");
                providerResponseData.setErrorCode(-4);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            providerResponseData.setErrorCode(-2);
        }
        return providerResponseData;
    }

    public static DiscoverPaymentCard m1009a(long j, C0533a c0533a) {
        ClearProfileData profileData = ((ClearProvisionData) mGson.fromJson(c0533a.er(), ClearProvisionData.class)).getProfileData();
        DiscoverPaymentCard discoverPaymentCard = new DiscoverPaymentCard(j, null, null, null);
        DiscoverContactlessPaymentData discoverContactlessPaymentData = new DiscoverContactlessPaymentData();
        DiscoverInnAppPaymentData discoverInnAppPaymentData = new DiscoverInnAppPaymentData();
        discoverContactlessPaymentData.setCountryCode(ByteBuffer.fromHexString(profileData.getCRM_Country_Code()));
        DiscoverApplicationData discoverApplicationData = new DiscoverApplicationData();
        discoverApplicationData.setApplicationEffectiveDate(ByteBuffer.fromHexString(profileData.getEffective_Date()));
        discoverApplicationData.setCLApplicationConfigurationOptions(ByteBuffer.fromHexString(profileData.getCL_ACO()));
        discoverApplicationData.setApplicationExpirationDate(ByteBuffer.fromHexString(profileData.getExp_Date()));
        discoverApplicationData.setApplicationVersionNumber(ByteBuffer.fromHexString(profileData.getAVN()));
        discoverApplicationData.setCardholderName(ByteBuffer.fromHexString(profileData.getCardholder_Name()));
        discoverApplicationData.setPan(ByteBuffer.fromHexString(profileData.getToken_PAN()));
        discoverApplicationData.setPanSn(ByteBuffer.fromHexString(profileData.getPAN_Seq_NBR()));
        discoverApplicationData.setApplicationState(ByteBuffer.fromHexString(profileData.getApplication_State()));
        discoverContactlessPaymentData.setDiscoverApplicationData(discoverApplicationData);
        HashMap hashMap = new HashMap();
        String alt_aid1_fci = profileData.getALT_AID1_FCI();
        String alt_aid2_fci = profileData.getALT_AID2_FCI();
        DcTokenManager.m1012a(new String[]{alt_aid1_fci, alt_aid2_fci}, hashMap);
        if (!hashMap.isEmpty()) {
            discoverContactlessPaymentData.setFciAltAid(hashMap);
        }
        String common_debit_FCI = profileData.getCommon_debit_FCI();
        if (common_debit_FCI != null) {
            discoverContactlessPaymentData.setFciDebitAid(ByteBuffer.fromHexString(common_debit_FCI));
        }
        discoverContactlessPaymentData.setFciMainAid(ByteBuffer.fromHexString(profileData.getDPAS_AID_FCI()));
        try {
            Log.m285d("DCSDK_DcTokenManager", "Start parsing PPSE FCI");
            List<ByteBuffer> G = BERTLV.m999G(ByteBuffer.fromHexString(profileData.getPPSE_FCI()));
            Log.m285d("DCSDK_DcTokenManager", "Parsing PPSE FCI completed.");
            ByteBuffer fromHexString;
            DiscoverIssuerOptions discoverIssuerOptions;
            List arrayList;
            ZIP_MS_TransactionProfile bf51;
            Object profiles;
            HashMap hashMap2;
            TransactionProfile bf50;
            DiscoverPaymentProfile a;
            TransactionProfile df2f;
            if (G != null) {
                for (ByteBuffer fromHexString2 : G) {
                    Log.m285d("DCSDK_DcTokenManager", "AID: " + fromHexString2.toHexString());
                }
                discoverContactlessPaymentData.setAliasList(G);
                discoverContactlessPaymentData.setFciPpse(ByteBuffer.fromHexString(profileData.getPPSE_FCI()));
                discoverContactlessPaymentData.setFciZipAid(ByteBuffer.fromHexString(profileData.getZip_AID_FCI()));
                discoverIssuerOptions = new DiscoverIssuerOptions();
                discoverIssuerOptions.setIssuerApplicationData(ByteBuffer.fromHexString(profileData.getIssuer_Application_Data()));
                discoverIssuerOptions.setIssuerLifeCycleData(ByteBuffer.fromHexString(profileData.getIssuer_Life_Cycle_Data()));
                common_debit_FCI = profileData.getIADOL();
                if (common_debit_FCI != null) {
                    discoverIssuerOptions.setIADOL(ByteBuffer.fromHexString(common_debit_FCI));
                }
                arrayList = new ArrayList();
                fromHexString2 = ByteBuffer.fromHexString(profileData.getIDDT0());
                if (fromHexString2 != null) {
                    try {
                        arrayList.add(new DiscoverIDDTag(fromHexString2, DiscoverIDDTag.IDDT_DF01));
                    } catch (ParseException e) {
                        Log.m286e("DCSDK_DcTokenManager", "ParseException has been thrown...");
                        e.printStackTrace();
                    }
                }
                try {
                    fromHexString2 = ByteBuffer.fromHexString(profileData.getIDDT1());
                    if (fromHexString2 != null) {
                        try {
                            arrayList.add(new DiscoverIDDTag(fromHexString2, DiscoverIDDTag.IDDT_DF02));
                        } catch (ParseException e2) {
                            Log.m286e("DCSDK_DcTokenManager", "ParseException has been thrown...");
                            e2.printStackTrace();
                        }
                    }
                    fromHexString2 = ByteBuffer.fromHexString(profileData.getIDDT2());
                    if (fromHexString2 != null) {
                        try {
                            arrayList.add(new DiscoverIDDTag(fromHexString2, DiscoverIDDTag.IDDT_DF03));
                        } catch (ParseException e22) {
                            Log.m286e("DCSDK_DcTokenManager", "ParseException has been thrown...");
                            e22.printStackTrace();
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        Log.m285d("DCSDK_DcTokenManager", "Add IDD tags to the payment data.");
                        discoverIssuerOptions.setIDDTags(arrayList);
                    }
                    discoverContactlessPaymentData.setIssuerApplicationData(discoverIssuerOptions);
                    discoverContactlessPaymentData.setPasscodeRetryCounter(ByteBuffer.fromHexString(profileData.getPASSCODE_Retry_Counter()));
                    discoverContactlessPaymentData.setCaco(ByteBuffer.fromHexString(profileData.getCACO()));
                    bf51 = profileData.getProfiles().getBF51();
                    discoverContactlessPaymentData.setZipAfl(ByteBuffer.fromHexString(bf51.getZIP_MS_Mode_AFL()));
                    discoverContactlessPaymentData.setZipAip(ByteBuffer.fromHexString(bf51.getZIP_MS_Mode_AIP()));
                    discoverContactlessPaymentData.setCurrencyCode(ByteBuffer.fromHexString(profileData.getCRM_Currency_Code()));
                    discoverContactlessPaymentData.setRecords(null);
                    discoverContactlessPaymentData.setSecondaryCurrency1(ByteBuffer.fromHexString(profileData.getSecondary_Currency_1()));
                    discoverContactlessPaymentData.setSecondaryCurrency2(ByteBuffer.fromHexString(profileData.getSecondary_Currency_2()));
                    discoverContactlessPaymentData.setServiceCode(profileData.getService_Code());
                    discoverContactlessPaymentData.setPth(ByteBuffer.fromHexString("0000"));
                    if (profileData.getPDOL_Profile_check() != null) {
                        Log.m285d("DCSDK_DcTokenManager", "PDOL profile check not null: " + profileData.getPDOL_Profile_check());
                        discoverContactlessPaymentData.setPDOLProfileCheckTable(ByteBuffer.fromHexString(profileData.getPDOL_Profile_check()));
                        try {
                            discoverContactlessPaymentData.setPdolProfileEntries(PDOLCheckEntry.parsePdolEntries(ByteBuffer.fromHexString(profileData.getPDOL_Profile_check())));
                        } catch (ParseException e222) {
                            Log.m286e("DCSDK_DcTokenManager", "TransactionProfilesContainerJSON: ParseException in parsing PDOL_profile_check.");
                            e222.printStackTrace();
                        }
                    }
                    profiles = profileData.getProfiles();
                    Log.m285d("DCSDK_DcTokenManager", "TransactionProfilesContainerJSON: " + mGson.toJson(profiles));
                    hashMap2 = new HashMap();
                    bf50 = profiles.getBF50();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - BF50: " + mGson.toJson((Object) bf50));
                        a = DcTokenManager.m1010a(bf50, 0);
                        hashMap2.put(Integer.valueOf(0), a);
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 0);
                        Log.m285d("DCSDK_DcTokenManager", "TokenManager cvm counter: " + a.getCVM().getCvmCounter());
                    }
                    bf50 = profiles.getDF21();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF21: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(1), DcTokenManager.m1010a(bf50, 1));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 1);
                    }
                    bf50 = profiles.getDF22();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF22: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(2), DcTokenManager.m1010a(bf50, 2));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 2);
                    }
                    bf50 = profiles.getDF23();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF23: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(3), DcTokenManager.m1010a(bf50, 3));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 3);
                    }
                    bf50 = profiles.getDF24();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF24: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(4), DcTokenManager.m1010a(bf50, 4));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 4);
                    }
                    bf50 = profiles.getDF25();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF25: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(5), DcTokenManager.m1010a(bf50, 5));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 5);
                    }
                    bf50 = profiles.getDF26();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF26: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(6), DcTokenManager.m1010a(bf50, 6));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 6);
                    }
                    bf50 = profiles.getDF27();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF27: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(7), DcTokenManager.m1010a(bf50, 7));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 7);
                    }
                    bf50 = profiles.getDF28();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF28: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(8), DcTokenManager.m1010a(bf50, 8));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 8);
                    }
                    bf50 = profiles.getDF29();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF29: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(9), DcTokenManager.m1010a(bf50, 9));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 9);
                    }
                    bf50 = profiles.getDF2A();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2A: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(10), DcTokenManager.m1010a(bf50, 10));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 10);
                    }
                    bf50 = profiles.getDF2B();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2B: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(11), DcTokenManager.m1010a(bf50, 11));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 11);
                    }
                    bf50 = profiles.getDF2C();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2C: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(12), DcTokenManager.m1010a(bf50, 12));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 12);
                    }
                    bf50 = profiles.getDF2D();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2D: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(13), DcTokenManager.m1010a(bf50, 13));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 13);
                    }
                    bf50 = profiles.getDF2E();
                    if (bf50 != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2E: " + mGson.toJson((Object) bf50));
                        hashMap2.put(Integer.valueOf(14), DcTokenManager.m1010a(bf50, 14));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 14);
                    }
                    df2f = profiles.getDF2F();
                    if (df2f != null) {
                        Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2F: " + mGson.toJson((Object) df2f));
                        hashMap2.put(Integer.valueOf(15), DcTokenManager.m1010a(df2f, 15));
                        Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 15);
                    }
                    discoverContactlessPaymentData.setPaymentProfiles(hashMap2);
                    discoverContactlessPaymentData.setTrack1DataZipMsMode(ByteBuffer.fromHexString(profileData.getTrack_1_Data_for_ZIPMode()));
                    discoverContactlessPaymentData.setTrack2DataZipMsMode(ByteBuffer.fromHexString(profileData.getTrack_2_Data_for_ZIPMode()));
                    discoverContactlessPaymentData.setTrack2EquivalentData(ByteBuffer.fromHexString(profileData.getTrack_2_Equivalent_Data()));
                    discoverPaymentCard.setDiscoverContactlessPaymentData(discoverContactlessPaymentData);
                    discoverPaymentCard.setDiscoverInnAppPaymentData(discoverInnAppPaymentData);
                    return discoverPaymentCard;
                } catch (NullPointerException e3) {
                    Log.m286e("DCSDK_DcTokenManager", "NPE while processing Profile Data: " + e3.getMessage());
                    e3.printStackTrace();
                    return null;
                }
            }
            Log.m286e("DCSDK_DcTokenManager", "Alias list cannot be parsed, returned null.");
            discoverContactlessPaymentData.setFciPpse(ByteBuffer.fromHexString(profileData.getPPSE_FCI()));
            discoverContactlessPaymentData.setFciZipAid(ByteBuffer.fromHexString(profileData.getZip_AID_FCI()));
            discoverIssuerOptions = new DiscoverIssuerOptions();
            discoverIssuerOptions.setIssuerApplicationData(ByteBuffer.fromHexString(profileData.getIssuer_Application_Data()));
            discoverIssuerOptions.setIssuerLifeCycleData(ByteBuffer.fromHexString(profileData.getIssuer_Life_Cycle_Data()));
            common_debit_FCI = profileData.getIADOL();
            if (common_debit_FCI != null) {
                discoverIssuerOptions.setIADOL(ByteBuffer.fromHexString(common_debit_FCI));
            }
            arrayList = new ArrayList();
            fromHexString2 = ByteBuffer.fromHexString(profileData.getIDDT0());
            if (fromHexString2 != null) {
                arrayList.add(new DiscoverIDDTag(fromHexString2, DiscoverIDDTag.IDDT_DF01));
            }
            fromHexString2 = ByteBuffer.fromHexString(profileData.getIDDT1());
            if (fromHexString2 != null) {
                arrayList.add(new DiscoverIDDTag(fromHexString2, DiscoverIDDTag.IDDT_DF02));
            }
            fromHexString2 = ByteBuffer.fromHexString(profileData.getIDDT2());
            if (fromHexString2 != null) {
                arrayList.add(new DiscoverIDDTag(fromHexString2, DiscoverIDDTag.IDDT_DF03));
            }
            if (arrayList.isEmpty()) {
                Log.m285d("DCSDK_DcTokenManager", "Add IDD tags to the payment data.");
                discoverIssuerOptions.setIDDTags(arrayList);
            }
            discoverContactlessPaymentData.setIssuerApplicationData(discoverIssuerOptions);
            discoverContactlessPaymentData.setPasscodeRetryCounter(ByteBuffer.fromHexString(profileData.getPASSCODE_Retry_Counter()));
            discoverContactlessPaymentData.setCaco(ByteBuffer.fromHexString(profileData.getCACO()));
            bf51 = profileData.getProfiles().getBF51();
            discoverContactlessPaymentData.setZipAfl(ByteBuffer.fromHexString(bf51.getZIP_MS_Mode_AFL()));
            discoverContactlessPaymentData.setZipAip(ByteBuffer.fromHexString(bf51.getZIP_MS_Mode_AIP()));
            discoverContactlessPaymentData.setCurrencyCode(ByteBuffer.fromHexString(profileData.getCRM_Currency_Code()));
            discoverContactlessPaymentData.setRecords(null);
            discoverContactlessPaymentData.setSecondaryCurrency1(ByteBuffer.fromHexString(profileData.getSecondary_Currency_1()));
            discoverContactlessPaymentData.setSecondaryCurrency2(ByteBuffer.fromHexString(profileData.getSecondary_Currency_2()));
            discoverContactlessPaymentData.setServiceCode(profileData.getService_Code());
            discoverContactlessPaymentData.setPth(ByteBuffer.fromHexString("0000"));
            if (profileData.getPDOL_Profile_check() != null) {
                Log.m285d("DCSDK_DcTokenManager", "PDOL profile check not null: " + profileData.getPDOL_Profile_check());
                discoverContactlessPaymentData.setPDOLProfileCheckTable(ByteBuffer.fromHexString(profileData.getPDOL_Profile_check()));
                discoverContactlessPaymentData.setPdolProfileEntries(PDOLCheckEntry.parsePdolEntries(ByteBuffer.fromHexString(profileData.getPDOL_Profile_check())));
            }
            profiles = profileData.getProfiles();
            Log.m285d("DCSDK_DcTokenManager", "TransactionProfilesContainerJSON: " + mGson.toJson(profiles));
            hashMap2 = new HashMap();
            bf50 = profiles.getBF50();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - BF50: " + mGson.toJson((Object) bf50));
                a = DcTokenManager.m1010a(bf50, 0);
                hashMap2.put(Integer.valueOf(0), a);
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 0);
                Log.m285d("DCSDK_DcTokenManager", "TokenManager cvm counter: " + a.getCVM().getCvmCounter());
            }
            bf50 = profiles.getDF21();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF21: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(1), DcTokenManager.m1010a(bf50, 1));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 1);
            }
            bf50 = profiles.getDF22();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF22: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(2), DcTokenManager.m1010a(bf50, 2));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 2);
            }
            bf50 = profiles.getDF23();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF23: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(3), DcTokenManager.m1010a(bf50, 3));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 3);
            }
            bf50 = profiles.getDF24();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF24: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(4), DcTokenManager.m1010a(bf50, 4));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 4);
            }
            bf50 = profiles.getDF25();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF25: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(5), DcTokenManager.m1010a(bf50, 5));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 5);
            }
            bf50 = profiles.getDF26();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF26: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(6), DcTokenManager.m1010a(bf50, 6));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 6);
            }
            bf50 = profiles.getDF27();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF27: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(7), DcTokenManager.m1010a(bf50, 7));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 7);
            }
            bf50 = profiles.getDF28();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF28: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(8), DcTokenManager.m1010a(bf50, 8));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 8);
            }
            bf50 = profiles.getDF29();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF29: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(9), DcTokenManager.m1010a(bf50, 9));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 9);
            }
            bf50 = profiles.getDF2A();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2A: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(10), DcTokenManager.m1010a(bf50, 10));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 10);
            }
            bf50 = profiles.getDF2B();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2B: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(11), DcTokenManager.m1010a(bf50, 11));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 11);
            }
            bf50 = profiles.getDF2C();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2C: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(12), DcTokenManager.m1010a(bf50, 12));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 12);
            }
            bf50 = profiles.getDF2D();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2D: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(13), DcTokenManager.m1010a(bf50, 13));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 13);
            }
            bf50 = profiles.getDF2E();
            if (bf50 != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2E: " + mGson.toJson((Object) bf50));
                hashMap2.put(Integer.valueOf(14), DcTokenManager.m1010a(bf50, 14));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 14);
            }
            df2f = profiles.getDF2F();
            if (df2f != null) {
                Log.m285d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2F: " + mGson.toJson((Object) df2f));
                hashMap2.put(Integer.valueOf(15), DcTokenManager.m1010a(df2f, 15));
                Log.m285d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 15);
            }
            discoverContactlessPaymentData.setPaymentProfiles(hashMap2);
            discoverContactlessPaymentData.setTrack1DataZipMsMode(ByteBuffer.fromHexString(profileData.getTrack_1_Data_for_ZIPMode()));
            discoverContactlessPaymentData.setTrack2DataZipMsMode(ByteBuffer.fromHexString(profileData.getTrack_2_Data_for_ZIPMode()));
            discoverContactlessPaymentData.setTrack2EquivalentData(ByteBuffer.fromHexString(profileData.getTrack_2_Equivalent_Data()));
            discoverPaymentCard.setDiscoverContactlessPaymentData(discoverContactlessPaymentData);
            discoverPaymentCard.setDiscoverInnAppPaymentData(discoverInnAppPaymentData);
            return discoverPaymentCard;
        } catch (ParseException e2222) {
            Log.m286e("DCSDK_DcTokenManager", "ParseException while parsing PPSE FCI: " + e2222.toString());
            e2222.printStackTrace();
        } catch (Exception e4) {
            Log.m286e("DCSDK_DcTokenManager", "Unexpected exception while parsing PPSE FCI: " + e4.toString());
            e4.printStackTrace();
        }
    }

    private static void m1012a(String[] strArr, HashMap<String, ByteBuffer> hashMap) {
        for (String str : strArr) {
            if (str == null) {
                Log.m285d("DCSDK_DcTokenManager", "parseAltAids, alt fci is null, check next.");
            } else {
                try {
                    Log.m287i("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse alt fci: " + str);
                    TLVData F = BERTLV.m998F(ByteBuffer.fromHexString(str));
                    if (F == null) {
                        Log.m286e("DCSDK_DcTokenManager", "parseAltAids, parsed aid is null.");
                        return;
                    }
                    ByteBuffer byteBuffer = F.m1006O(DiscoverDataTags.vP.getInt()) != null ? (ByteBuffer) F.m1006O(DiscoverDataTags.vP.getInt()).get(0) : null;
                    if (byteBuffer != null) {
                        Log.m285d("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse alt aid: " + byteBuffer.toHexString());
                        hashMap.put(byteBuffer.toHexString(), ByteBuffer.fromHexString(str));
                    } else {
                        Log.m286e("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse alt aid is null.");
                    }
                } catch (ParseException e) {
                    Log.m286e("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse exception observed while alt aid parsing.");
                    e.printStackTrace();
                }
            }
        }
    }

    private JsonObject m1015h(Object obj) {
        String toJson = mGson.toJson(obj);
        Log.m285d("DCSDK_DcTokenManager", "createJsonObj: " + toJson);
        return new JsonParser().parse(toJson).getAsJsonObject();
    }

    private boolean m1013a(BillingInfo billingInfo) {
        return true;
    }

    private boolean m1014b(EnrollCardPanInfo enrollCardPanInfo) {
        if (enrollCardPanInfo.getPAN().trim().length() != 16) {
            Log.m286e("DCSDK_DcTokenManager", "validateCardInfo: PAN");
            return false;
        }
        int length = enrollCardPanInfo.getCVV().trim().length();
        if (length < 3 || length > 4) {
            Log.m286e("DCSDK_DcTokenManager", "validateCardInfo: CVV");
            return false;
        } else if (!enrollCardPanInfo.getName().trim().isEmpty()) {
            return true;
        } else {
            Log.m286e("DCSDK_DcTokenManager", "validateCardInfo: Name");
            return false;
        }
    }

    private String aP(String str) {
        String toLowerCase = str.toLowerCase();
        return Hex.toHexString(CryptoUtils.m1056a(toLowerCase, CryptoUtils.aQ(toLowerCase), "SHA-256", 2000));
    }

    private C0511a m1011a(ClearCardInfo clearCardInfo, List<byte[]> list) {
        byte[] bytes = mGson.toJson((Object) clearCardInfo).getBytes();
        if (bytes.length == 0) {
            Log.m286e("DCSDK_DcTokenManager", "createSecureCardContext: CardContext serialization failed");
            return null;
        }
        C0511a b;
        try {
            b = DcTAController.eu().m1047b(bytes, (List) list);
        } catch (DcTAException e) {
            DcTAException dcTAException = e;
            if (dcTAException.getErrorCode() == Code.ERR_TA_VERIFY_CERT.getCode()) {
                String byteArrayToHex = Utils.byteArrayToHex((byte[]) list.get(1));
                int length = byteArrayToHex.length();
                Log.m287i("DCSDK_DcTokenManager", "createSecureCardContext: Cert(1) - " + byteArrayToHex.substring(0, 30) + " ... " + byteArrayToHex.substring(length - 20, length));
            }
            Log.m286e("DCSDK_DcTokenManager", "createSecureCardContext: TA Exception - " + dcTAException.toString());
            dcTAException.printStackTrace();
            b = null;
        } catch (Exception e2) {
            Log.m286e("DCSDK_DcTokenManager", "createSecureCardContext: Exception - " + e2.getMessage());
            e2.printStackTrace();
            b = null;
        }
        return b;
    }

    private static DiscoverPaymentProfile m1010a(TransactionProfile transactionProfile, int i) {
        DiscoverPaymentProfile discoverPaymentProfile = new DiscoverPaymentProfile();
        discoverPaymentProfile.setProfileId(i);
        discoverPaymentProfile.setAip(ByteBuffer.fromHexString(transactionProfile.getAIP()));
        discoverPaymentProfile.setCpr(ByteBuffer.fromHexString(transactionProfile.getCPR()));
        discoverPaymentProfile.setApplicationUsageControl(ByteBuffer.fromHexString(transactionProfile.getAUC()));
        String afl = transactionProfile.getAFL();
        if (afl != null) {
            discoverPaymentProfile.setAfl(ByteBuffer.fromHexString(afl));
        }
        if (transactionProfile.getPRU() != null) {
            discoverPaymentProfile.setPru(ByteBuffer.fromHexString(transactionProfile.getPRU()));
        }
        CL cl = new CL();
        cl.setCL_Cons_Limit(ByteBuffer.fromHexString(transactionProfile.getCL_Cons_limit()).getLong());
        cl.setCL_Cum_Limit(Long.parseLong(transactionProfile.getCL_Cum_limit()));
        cl.setCL_STA_Limit(Long.parseLong(transactionProfile.getCL_STA_limit()));
        if (transactionProfile.getCL_Counter() != null) {
            cl.setClCounter(ByteBuffer.fromHexString(transactionProfile.getCL_Counter()).getLong());
        }
        if (transactionProfile.getCL_Accumulator() != null) {
            cl.setClAccumulator(Long.parseLong(transactionProfile.getCL_Accumulator()));
        }
        discoverPaymentProfile.setCl(cl);
        CRM crm = new CRM();
        crm.setLCOA(Long.parseLong(transactionProfile.getLCOA()));
        crm.setLCOL(ByteBuffer.fromHexString(transactionProfile.getLCOL()).getLong());
        crm.setSTA(Long.parseLong(transactionProfile.getSTA()));
        crm.setUCOA(Long.parseLong(transactionProfile.getUCOA()));
        crm.setUCOL(ByteBuffer.fromHexString(transactionProfile.getUCOL()).getLong());
        crm.setCRM_CAC_Default(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Default()));
        crm.setCRM_CAC_Denial(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Denial()));
        crm.setCRM_CAC_Online(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Online()));
        crm.setCrmAccumulator(Long.parseLong(transactionProfile.getCOA()));
        crm.setCrmCounter(ByteBuffer.fromHexString(transactionProfile.getNCOT()).getLong());
        if (transactionProfile.getCRM_CAC_Switch_Interface() != null) {
            crm.setCRM_CAC_Switch_Interface(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Switch_Interface()));
        }
        discoverPaymentProfile.setCRM(crm);
        CVM cvm = new CVM();
        cvm.setCVM_CAC_Online_PIN(ByteBuffer.fromHexString(transactionProfile.getCVM_CAC_Online_PIN()));
        if (transactionProfile.getCVM_CAC_Signature() != null) {
            cvm.setCVM_CAC_Signature(ByteBuffer.fromHexString(transactionProfile.getCVM_CAC_Signature()).getLong());
        }
        if (!(transactionProfile.getCVM_Cons_limit_1() == null || ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_1()) == null)) {
            cvm.setCVM_Cons_Limit_1(ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_1()).getLong());
        }
        if (!(transactionProfile.getCVM_Cons_limit_2() == null || ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_2()) == null)) {
            cvm.setCVM_Cons_Limit_2(ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_2()).getLong());
        }
        if (transactionProfile.getCVM_Cum_limit_1() != null) {
            cvm.setCVM_Cum_Limit_1(Long.parseLong(transactionProfile.getCVM_Cum_limit_1()));
        }
        if (transactionProfile.getCVM_Cum_limit_2() != null) {
            cvm.setCVM_Cum_Limit_2(Long.parseLong(transactionProfile.getCVM_Cum_limit_2()));
        }
        if (transactionProfile.getCVM_Sta_limit_1() != null) {
            cvm.setCVM_Sta_Limit_1(Long.parseLong(transactionProfile.getCVM_Sta_limit_1()));
        }
        if (transactionProfile.getCVM_Sta_limit_2() != null) {
            cvm.setCVM_Sta_Limit_2(Long.parseLong(transactionProfile.getCVM_Sta_limit_2()));
        }
        if (transactionProfile.getCVM_Counter() != null) {
            cvm.setCvmCounter(ByteBuffer.fromHexString(transactionProfile.getCVM_Counter()).getLong());
        }
        if (transactionProfile.getCVM_Accumulator() != null) {
            cvm.setCvmAccumulator(Long.parseLong(transactionProfile.getCVM_Accumulator()));
        }
        discoverPaymentProfile.setCVM(cvm);
        return discoverPaymentProfile;
    }
}
