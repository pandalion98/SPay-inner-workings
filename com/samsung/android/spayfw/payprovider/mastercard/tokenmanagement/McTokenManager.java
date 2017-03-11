package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.GetTokenResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

public class McTokenManager {
    private static final String TAG = "McTokenManager";
    private static final String TDS_TAG_ERROR = "e__McTokenManager";
    private static final String TDS_TAG_INFO = "i__McTokenManager";
    private static Context sContext;
    private final McCardMasterDaoImpl mCardMasterDaoImpl;
    private final McTokenProvisioningHandler mMcTokenProvisioningHandler;

    private enum CertName {
        RGK("MC_RGK_CERT_CURRENT"),
        CIK("MC_CIK_CERT_CURRENT"),
        NONE("none");
        
        private String name;

        private CertName(String str) {
            this.name = str;
        }

        public String getName() {
            return this.name;
        }
    }

    public McTokenManager(Context context) {
        this.mMcTokenProvisioningHandler = new McTokenProvisioningHandler();
        this.mCardMasterDaoImpl = new McCardMasterDaoImpl(context);
        sContext = context;
    }

    public ProviderRequestData getEnrollmentMcData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        ProviderRequestData enrollmentMcData = this.mMcTokenProvisioningHandler.getEnrollmentMcData(enrollCardInfo, billingInfo);
        if (enrollmentMcData.getErrorCode() != 0) {
            Log.m286e(TAG, "Failed to create Enrollment Request" + enrollmentMcData.getErrorCode());
        } else {
            Log.m285d(TAG, "Generated Mc Json : " + enrollmentMcData.ch().toString());
        }
        return enrollmentMcData;
    }

    public ProviderResponseData provisionToken(JsonObject jsonObject, int i) {
        return this.mMcTokenProvisioningHandler.provisionToken(jsonObject, i);
    }

    public ProviderResponseData updateTokenData(ProviderTokenKey providerTokenKey, JsonObject jsonObject, TokenStatus tokenStatus) {
        Exception e;
        int i;
        Throwable th;
        ProviderResponseData providerResponseData = new ProviderResponseData();
        try {
            boolean deleteData;
            if (tokenStatus.getCode().equalsIgnoreCase(TokenStatus.DISPOSED)) {
                Log.m287i(TDS_TAG_INFO, "updateTokenData: DISPOSED");
                unRegisterTds(providerTokenKey.cm());
                deleteData = this.mCardMasterDaoImpl.deleteData(providerTokenKey.cm());
                if (!deleteData) {
                    try {
                        Log.m286e(TAG, "updateTokenData: failed to delete token in Card DB: ");
                    } catch (Exception e2) {
                        e = e2;
                        try {
                            e.printStackTrace();
                            Log.m286e(TAG, "updateTokenData: exception occured: " + e.getMessage());
                            if (i != 1) {
                                providerResponseData.setErrorCode(-2);
                            } else {
                                providerResponseData.setErrorCode(0);
                            }
                            return providerResponseData;
                        } catch (Throwable th2) {
                            th = th2;
                            if (i != 1) {
                                providerResponseData.setErrorCode(-2);
                            } else {
                                providerResponseData.setErrorCode(0);
                            }
                            throw th;
                        }
                    }
                }
                if (deleteData) {
                    providerResponseData.setErrorCode(0);
                } else {
                    providerResponseData.setErrorCode(-2);
                }
                return providerResponseData;
            }
            if (tokenStatus.getCode().equalsIgnoreCase(TokenStatus.SUSPENDED)) {
                Log.m287i(TDS_TAG_INFO, "updateTokenData: SUSPENDED");
                unRegisterTds(providerTokenKey.cm());
            }
            McCardMaster mcCardMaster = (McCardMaster) this.mCardMasterDaoImpl.getData(providerTokenKey.cm());
            mcCardMaster.setStatus(tokenStatus.getCode());
            deleteData = this.mCardMasterDaoImpl.updateData(mcCardMaster, providerTokenKey.cm());
            Log.m285d(TAG, "DB Row Id: " + providerTokenKey.cm() + ", Status: " + tokenStatus.getCode() + ", DB Update result: " + deleteData);
            if (tokenStatus.getCode().equalsIgnoreCase(TokenStatus.ACTIVE)) {
                Log.m287i(TDS_TAG_INFO, "updateTokenData: ACTIVE");
                checkTds(jsonObject, providerTokenKey.cm());
            }
            if (deleteData) {
                providerResponseData.setErrorCode(0);
            } else {
                providerResponseData.setErrorCode(-2);
            }
            return providerResponseData;
        } catch (Exception e3) {
            e = e3;
            i = 0;
            e.printStackTrace();
            Log.m286e(TAG, "updateTokenData: exception occured: " + e.getMessage());
            if (i != 1) {
                providerResponseData.setErrorCode(0);
            } else {
                providerResponseData.setErrorCode(-2);
            }
            return providerResponseData;
        } catch (Throwable th3) {
            th = th3;
            i = 0;
            if (i != 1) {
                providerResponseData.setErrorCode(0);
            } else {
                providerResponseData.setErrorCode(-2);
            }
            throw th;
        }
    }

    private int checkTds(JsonObject jsonObject, long j) {
        if (j < 1 || jsonObject == null) {
            return 0;
        }
        GetTokenResponse parseJson = GetTokenResponse.parseJson(jsonObject);
        if (parseJson == null) {
            Log.m286e(TDS_TAG_ERROR, "extractTds:McTds Parsing error");
            return -2;
        }
        return McTdsManager.getInstance(j).onTokenStateUpdate(parseJson.getTdsRegistrationUrl(), parseJson.getPaymentAppInstanceId());
    }

    public boolean updateMcCerts(CertificateInfo[] certificateInfoArr) {
        byte[] bArr = null;
        Log.m285d(TAG, "updateMcCerts called: ");
        if (certificateInfoArr == null || certificateInfoArr.length != 2) {
            Log.m286e(TAG, "updateMcCerts called with invalid number of  certifcates: ");
            if (certificateInfoArr != null) {
                Log.m286e(TAG, "invalid certificates length :" + certificateInfoArr.length);
            }
            return false;
        }
        int i = 0;
        byte[] bArr2 = null;
        while (i < 2) {
            if (certificateInfoArr[i] == null || TextUtils.isEmpty(certificateInfoArr[i].getAlias())) {
                Log.m286e(TAG, "Empty certificate recevied");
                return false;
            }
            try {
                certificateInfoArr[i].getAlias().getBytes();
                certificateInfoArr[i].getContent().getBytes();
                if (certificateInfoArr[i].getAlias().equalsIgnoreCase(CertName.RGK.getName())) {
                    bArr2 = Base64.decode(certificateInfoArr[i].getContent().getBytes(), 0);
                } else if (certificateInfoArr[i].getAlias().equalsIgnoreCase(CertName.CIK.getName())) {
                    bArr = Base64.decode(certificateInfoArr[i].getContent().getBytes(), 0);
                }
                i++;
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.m286e(TAG, "NPE Err in certificate recevied:" + e.getMessage());
                return false;
            }
        }
        if (bArr2 == null || bArr == null) {
            Log.m286e(TAG, "updateMcCerts: failed");
            return false;
        }
        this.mMcTokenProvisioningHandler.setMcCerts(bArr2, bArr);
        Log.m285d(TAG, "updateMcCerts: successful");
        return true;
    }

    public static Context getContext() {
        return sContext;
    }

    public void deleteToken(ProviderTokenKey providerTokenKey) {
        if (providerTokenKey == null) {
            try {
                Log.m287i(TAG, "deleteToken: Delete called .. Remove leftover data");
                long dbReference = this.mMcTokenProvisioningHandler.getDbReference();
                if (!this.mMcTokenProvisioningHandler.cleanupDbReference(dbReference, this.mCardMasterDaoImpl)) {
                    Log.m286e(TAG, "deleteToken: Delete operation failed for dbReference: " + dbReference);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.m286e(TAG, "deleteToken: Null Exception during MC Token delete : " + e.getMessage());
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.m286e(TAG, "deleteToken: Exception during MC Token delete : " + e2.getMessage());
            }
        } else if (((McCardMaster) this.mCardMasterDaoImpl.getData(providerTokenKey.cm())) != null) {
            Log.m285d(TAG, "deleteToken: Delete tokenId: " + providerTokenKey.cm());
            if (!this.mCardMasterDaoImpl.deleteData(providerTokenKey.cm())) {
                Log.m286e(TAG, "deleteToken: MC Token delete failed : " + providerTokenKey.cm());
            }
        }
    }

    public ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        if (provisionTokenInfo == null) {
            return null;
        }
        return McDecisioningData.getInstance().setRiskData(provisionTokenInfo).populateRiskData();
    }

    private void unRegisterTds(long j) {
        Log.m287i(TDS_TAG_INFO, "unRegisterTds:McTds Unregistering tds : " + j);
        McTdsManager.getInstance(j).unRegister();
    }

    public static long deleteStaleEnrollmentData(Context context) {
        return new McCardMasterDaoImpl(context).deleteStaleEnrollmentData();
    }

    public byte[] processSignatureData(byte[] bArr, int i) {
        return this.mMcTokenProvisioningHandler.processSignatureData(bArr, i);
    }

    public void setUniqueTokenReference(String str) {
        this.mMcTokenProvisioningHandler.setUniqueTokenReference(str);
    }
}
