/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.JsonObject
 *  java.lang.CharSequence
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.GetTokenResponse;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McDecisioningData;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenProvisioningHandler;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

public class McTokenManager {
    private static final String TAG = "McTokenManager";
    private static final String TDS_TAG_ERROR = "e__McTokenManager";
    private static final String TDS_TAG_INFO = "i__McTokenManager";
    private static Context sContext;
    private final McCardMasterDaoImpl mCardMasterDaoImpl;
    private final McTokenProvisioningHandler mMcTokenProvisioningHandler = new McTokenProvisioningHandler();

    public McTokenManager(Context context) {
        this.mCardMasterDaoImpl = new McCardMasterDaoImpl(context);
        sContext = context;
    }

    private int checkTds(JsonObject jsonObject, long l2) {
        if (l2 < 1L || jsonObject == null) {
            return 0;
        }
        GetTokenResponse getTokenResponse = GetTokenResponse.parseJson(jsonObject);
        if (getTokenResponse == null) {
            com.samsung.android.spayfw.b.c.e(TDS_TAG_ERROR, "extractTds:McTds Parsing error");
            return -2;
        }
        String string = getTokenResponse.getTdsRegistrationUrl();
        String string2 = getTokenResponse.getPaymentAppInstanceId();
        return McTdsManager.getInstance(l2).onTokenStateUpdate(string, string2);
    }

    public static long deleteStaleEnrollmentData(Context context) {
        return new McCardMasterDaoImpl(context).deleteStaleEnrollmentData();
    }

    public static Context getContext() {
        return sContext;
    }

    private void unRegisterTds(long l2) {
        com.samsung.android.spayfw.b.c.i(TDS_TAG_INFO, "unRegisterTds:McTds Unregistering tds : " + l2);
        McTdsManager.getInstance(l2).unRegister();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void deleteToken(f var1_1) {
        if (var1_1 != null) ** GOTO lbl8
        try {
            com.samsung.android.spayfw.b.c.i("McTokenManager", "deleteToken: Delete called .. Remove leftover data");
            var4_2 = this.mMcTokenProvisioningHandler.getDbReference();
            if (this.mMcTokenProvisioningHandler.cleanupDbReference(var4_2, this.mCardMasterDaoImpl) != false) return;
            com.samsung.android.spayfw.b.c.e("McTokenManager", "deleteToken: Delete operation failed for dbReference: " + var4_2);
            return;
lbl8: // 1 sources:
            if ((McCardMaster)this.mCardMasterDaoImpl.getData(var1_1.cm()) == null) return;
            com.samsung.android.spayfw.b.c.d("McTokenManager", "deleteToken: Delete tokenId: " + var1_1.cm());
            if (this.mCardMasterDaoImpl.deleteData(var1_1.cm()) != false) return;
            com.samsung.android.spayfw.b.c.e("McTokenManager", "deleteToken: MC Token delete failed : " + var1_1.cm());
            return;
        }
        catch (NullPointerException var3_3) {
            var3_3.printStackTrace();
            com.samsung.android.spayfw.b.c.e("McTokenManager", "deleteToken: Null Exception during MC Token delete : " + var3_3.getMessage());
            return;
        }
        catch (Exception var2_4) {
            var2_4.printStackTrace();
            com.samsung.android.spayfw.b.c.e("McTokenManager", "deleteToken: Exception during MC Token delete : " + var2_4.getMessage());
        }
    }

    public c getEnrollmentMcData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        c c2 = this.mMcTokenProvisioningHandler.getEnrollmentMcData(enrollCardInfo, billingInfo);
        if (c2.getErrorCode() != 0) {
            com.samsung.android.spayfw.b.c.e(TAG, "Failed to create Enrollment Request" + c2.getErrorCode());
            return c2;
        }
        com.samsung.android.spayfw.b.c.d(TAG, "Generated Mc Json : " + c2.ch().toString());
        return c2;
    }

    public c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        if (provisionTokenInfo == null) {
            return null;
        }
        return McDecisioningData.getInstance().setRiskData(provisionTokenInfo).populateRiskData();
    }

    public byte[] processSignatureData(byte[] arrby, int n2) {
        return this.mMcTokenProvisioningHandler.processSignatureData(arrby, n2);
    }

    public e provisionToken(JsonObject jsonObject, int n2) {
        return this.mMcTokenProvisioningHandler.provisionToken(jsonObject, n2);
    }

    public void setUniqueTokenReference(String string) {
        this.mMcTokenProvisioningHandler.setUniqueTokenReference(string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean updateMcCerts(CertificateInfo[] arrcertificateInfo) {
        byte[] arrby = null;
        com.samsung.android.spayfw.b.c.d(TAG, "updateMcCerts called: ");
        if (arrcertificateInfo == null || arrcertificateInfo.length != 2) {
            com.samsung.android.spayfw.b.c.e(TAG, "updateMcCerts called with invalid number of  certifcates: ");
            if (arrcertificateInfo != null) {
                com.samsung.android.spayfw.b.c.e(TAG, "invalid certificates length :" + arrcertificateInfo.length);
            }
            return false;
        }
        byte[] arrby2 = null;
        for (int i2 = 0; i2 < 2; ++i2) {
            if (arrcertificateInfo[i2] == null || TextUtils.isEmpty((CharSequence)arrcertificateInfo[i2].getAlias())) {
                com.samsung.android.spayfw.b.c.e(TAG, "Empty certificate recevied");
                return false;
            }
            try {
                arrcertificateInfo[i2].getAlias().getBytes();
                arrcertificateInfo[i2].getContent().getBytes();
            }
            catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
                com.samsung.android.spayfw.b.c.e(TAG, "NPE Err in certificate recevied:" + nullPointerException.getMessage());
                return false;
            }
            if (arrcertificateInfo[i2].getAlias().equalsIgnoreCase(CertName.RGK.getName())) {
                arrby2 = Base64.decode((byte[])arrcertificateInfo[i2].getContent().getBytes(), (int)0);
                continue;
            }
            if (!arrcertificateInfo[i2].getAlias().equalsIgnoreCase(CertName.CIK.getName())) continue;
            arrby = Base64.decode((byte[])arrcertificateInfo[i2].getContent().getBytes(), (int)0);
        }
        if (arrby2 != null && arrby != null) {
            this.mMcTokenProvisioningHandler.setMcCerts(arrby2, arrby);
            com.samsung.android.spayfw.b.c.d(TAG, "updateMcCerts: successful");
            return true;
        }
        com.samsung.android.spayfw.b.c.e(TAG, "updateMcCerts: failed");
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public e updateTokenData(f var1_1, JsonObject var2_2, TokenStatus var3_3) {
        block20 : {
            block18 : {
                block19 : {
                    var4_4 = new e();
                    if (!var3_3.getCode().equalsIgnoreCase("DISPOSED")) break block18;
                    com.samsung.android.spayfw.b.c.i("i__McTokenManager", "updateTokenData: DISPOSED");
                    this.unRegisterTds(var1_1.cm());
                    var6_6 = var11_5 = this.mCardMasterDaoImpl.deleteData(var1_1.cm());
                    if (var6_6) break block19;
                    com.samsung.android.spayfw.b.c.e("McTokenManager", "updateTokenData: failed to delete token in Card DB: ");
                }
                if (var6_6) {
                    var4_4.setErrorCode(0);
lbl13: // 2 sources:
                    do {
                        return var4_4;
                        break;
                    } while (true);
                }
                var4_4.setErrorCode(-2);
                ** while (true)
            }
            if (var3_3.getCode().equalsIgnoreCase("SUSPENDED")) {
                com.samsung.android.spayfw.b.c.i("i__McTokenManager", "updateTokenData: SUSPENDED");
                this.unRegisterTds(var1_1.cm());
            }
            var8_7 = (McCardMaster)this.mCardMasterDaoImpl.getData(var1_1.cm());
            var8_7.setStatus(var3_3.getCode());
            var6_6 = var9_8 = this.mCardMasterDaoImpl.updateData(var8_7, var1_1.cm());
            try {
                com.samsung.android.spayfw.b.c.d("McTokenManager", "DB Row Id: " + var1_1.cm() + ", Status: " + var3_3.getCode() + ", DB Update result: " + var6_6);
                if (var3_3.getCode().equalsIgnoreCase("ACTIVE")) {
                    com.samsung.android.spayfw.b.c.i("i__McTokenManager", "updateTokenData: ACTIVE");
                    this.checkTds(var2_2, var1_1.cm());
                }
                if (!var6_6) break block20;
            }
            catch (Exception var7_11) {
                ** continue;
            }
            var4_4.setErrorCode(0);
lbl32: // 4 sources:
            do {
                return var4_4;
                break;
            } while (true);
        }
        var4_4.setErrorCode(-2);
        ** GOTO lbl32
        catch (Exception var7_9) {
            var6_6 = false;
lbl39: // 2 sources:
            do {
                block21 : {
                    var7_10.printStackTrace();
                    com.samsung.android.spayfw.b.c.e("McTokenManager", "updateTokenData: exception occured: " + var7_10.getMessage());
                    if (!var6_6) break block21;
                    var4_4.setErrorCode(0);
                    ** GOTO lbl32
                }
                var4_4.setErrorCode(-2);
                ** continue;
                break;
            } while (true);
        }
        catch (Throwable var5_12) {
            var6_6 = false;
lbl51: // 2 sources:
            do {
                if (var6_6) {
                    var4_4.setErrorCode(0);
lbl54: // 2 sources:
                    do {
                        throw var5_13;
                        break;
                    } while (true);
                }
                var4_4.setErrorCode(-2);
                ** continue;
                break;
            } while (true);
        }
        {
            catch (Throwable var5_14) {
                ** continue;
            }
        }
    }

    private static final class CertName
    extends Enum<CertName> {
        private static final /* synthetic */ CertName[] $VALUES;
        public static final /* enum */ CertName CIK;
        public static final /* enum */ CertName NONE;
        public static final /* enum */ CertName RGK;
        private String name;

        static {
            RGK = new CertName("MC_RGK_CERT_CURRENT");
            CIK = new CertName("MC_CIK_CERT_CURRENT");
            NONE = new CertName("none");
            CertName[] arrcertName = new CertName[]{RGK, CIK, NONE};
            $VALUES = arrcertName;
        }

        private CertName(String string2) {
            this.name = string2;
        }

        public static CertName valueOf(String string) {
            return (CertName)Enum.valueOf(CertName.class, (String)string);
        }

        public static CertName[] values() {
            return (CertName[])$VALUES.clone();
        }

        public String getName() {
            return this.name;
        }
    }

}

