/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  java.io.Serializable
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NullPointerException
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  org.bouncycastle.util.encoders.Hex
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.DcStorageManager;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.discover.payment.data.PDOLCheckEntry;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverInnAppPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIssuerOptions;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.a;
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
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.TransactionProfilesContainer;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ZIP_MS_TransactionProfile;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bouncycastle.util.encoders.Hex;

public class b {
    private static Gson mGson = new Gson();
    private com.samsung.android.spayfw.payprovider.discover.db.dao.c wu = null;
    private SecureContext wv;
    private boolean ww = false;

    public b(Context context) {
        this.wu = new com.samsung.android.spayfw.payprovider.discover.db.dao.c(context);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static DiscoverPaymentCard a(long var0, DcTACommands.ProcessCardProfile.Response.a var2_1) {
        var3_2 = var2_1.er();
        var4_3 = ((ClearProvisionData)b.mGson.fromJson(var3_2, ClearProvisionData.class)).getProfileData();
        var5_4 = new DiscoverPaymentCard(var0, null, null, null);
        var6_5 = new DiscoverContactlessPaymentData();
        var7_6 = new DiscoverInnAppPaymentData();
        try {
            block42 : {
                var6_5.setCountryCode(ByteBuffer.fromHexString(var4_3.getCRM_Country_Code()));
                var9_7 = new DiscoverApplicationData();
                var9_7.setApplicationEffectiveDate(ByteBuffer.fromHexString(var4_3.getEffective_Date()));
                var9_7.setCLApplicationConfigurationOptions(ByteBuffer.fromHexString(var4_3.getCL_ACO()));
                var9_7.setApplicationExpirationDate(ByteBuffer.fromHexString(var4_3.getExp_Date()));
                var9_7.setApplicationVersionNumber(ByteBuffer.fromHexString(var4_3.getAVN()));
                var9_7.setCardholderName(ByteBuffer.fromHexString(var4_3.getCardholder_Name()));
                var9_7.setPan(ByteBuffer.fromHexString(var4_3.getToken_PAN()));
                var9_7.setPanSn(ByteBuffer.fromHexString(var4_3.getPAN_Seq_NBR()));
                var9_7.setApplicationState(ByteBuffer.fromHexString(var4_3.getApplication_State()));
                var6_5.setDiscoverApplicationData(var9_7);
                var10_8 = new HashMap();
                b.a(new String[]{var4_3.getALT_AID1_FCI(), var4_3.getALT_AID2_FCI()}, (HashMap<String, ByteBuffer>)var10_8);
                if (!var10_8.isEmpty()) {
                    var6_5.setFciAltAid((HashMap<String, ByteBuffer>)var10_8);
                }
                if ((var11_9 = var4_3.getCommon_debit_FCI()) != null) {
                    var6_5.setFciDebitAid(ByteBuffer.fromHexString(var11_9));
                }
                var6_5.setFciMainAid(ByteBuffer.fromHexString(var4_3.getDPAS_AID_FCI()));
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Start parsing PPSE FCI");
                var78_10 = com.samsung.android.spayfw.payprovider.discover.payment.utils.a.G(ByteBuffer.fromHexString(var4_3.getPPSE_FCI()));
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Parsing PPSE FCI completed.");
                if (var78_10 == null) break block42;
                for (ByteBuffer var80_12 : var78_10) {
                    com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "AID: " + var80_12.toHexString());
                }
                var6_5.setAliasList(var78_10);
                ** GOTO lbl51
            }
            ** try [egrp 9[TRYBLOCK] [66, 67 : 2506->2517)] { 
lbl37: // 1 sources:
        }
        catch (NullPointerException var8_56) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "NPE while processing Profile Data: " + var8_56.getMessage());
            var8_56.printStackTrace();
            return null;
        }
        {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "Alias list cannot be parsed, returned null.");
            ** GOTO lbl51
        }
lbl44: // 2 sources:
        catch (ParseException var77_13) {
            block43 : {
                com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "ParseException while parsing PPSE FCI: " + var77_13.toString());
                var77_13.printStackTrace();
                break block43;
lbl48: // 2 sources:
                catch (Exception var12_55) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "Unexpected exception while parsing PPSE FCI: " + var12_55.toString());
                    var12_55.printStackTrace();
                }
            }
            var6_5.setFciPpse(ByteBuffer.fromHexString(var4_3.getPPSE_FCI()));
            var6_5.setFciZipAid(ByteBuffer.fromHexString(var4_3.getZip_AID_FCI()));
            var13_14 = new DiscoverIssuerOptions();
            var13_14.setIssuerApplicationData(ByteBuffer.fromHexString(var4_3.getIssuer_Application_Data()));
            var13_14.setIssuerLifeCycleData(ByteBuffer.fromHexString(var4_3.getIssuer_Life_Cycle_Data()));
            var14_15 = var4_3.getIADOL();
            if (var14_15 != null) {
                var13_14.setIADOL(ByteBuffer.fromHexString(var14_15));
            }
            var15_16 = new ArrayList();
            var16_17 = ByteBuffer.fromHexString(var4_3.getIDDT0());
            if (var16_17 != null) {
                try {
                    var15_16.add((Object)new DiscoverIDDTag(var16_17, DiscoverIDDTag.IDDT_DF01));
                }
                catch (ParseException var75_57) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "ParseException has been thrown...");
                    var75_57.printStackTrace();
                }
            }
            if ((var17_18 = ByteBuffer.fromHexString(var4_3.getIDDT1())) != null) {
                try {
                    var15_16.add((Object)new DiscoverIDDTag(var17_18, DiscoverIDDTag.IDDT_DF02));
                }
                catch (ParseException var73_58) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "ParseException has been thrown...");
                    var73_58.printStackTrace();
                }
            }
            if ((var18_19 = ByteBuffer.fromHexString(var4_3.getIDDT2())) != null) {
                try {
                    var15_16.add((Object)new DiscoverIDDTag(var18_19, DiscoverIDDTag.IDDT_DF03));
                }
                catch (ParseException var71_59) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "ParseException has been thrown...");
                    var71_59.printStackTrace();
                }
            }
            if (!var15_16.isEmpty()) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Add IDD tags to the payment data.");
                var13_14.setIDDTags((List<DiscoverIDDTag>)var15_16);
            }
            var6_5.setIssuerApplicationData(var13_14);
            var6_5.setPasscodeRetryCounter(ByteBuffer.fromHexString(var4_3.getPASSCODE_Retry_Counter()));
            var6_5.setCaco(ByteBuffer.fromHexString(var4_3.getCACO()));
            var19_20 = var4_3.getProfiles().getBF51();
            var6_5.setZipAfl(ByteBuffer.fromHexString(var19_20.getZIP_MS_Mode_AFL()));
            var6_5.setZipAip(ByteBuffer.fromHexString(var19_20.getZIP_MS_Mode_AIP()));
            var6_5.setCurrencyCode(ByteBuffer.fromHexString(var4_3.getCRM_Currency_Code()));
            var6_5.setRecords(null);
            var6_5.setSecondaryCurrency1(ByteBuffer.fromHexString(var4_3.getSecondary_Currency_1()));
            var6_5.setSecondaryCurrency2(ByteBuffer.fromHexString(var4_3.getSecondary_Currency_2()));
            var6_5.setServiceCode(var4_3.getService_Code());
            var6_5.setPth(ByteBuffer.fromHexString("0000"));
            if (var4_3.getPDOL_Profile_check() != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "PDOL profile check not null: " + var4_3.getPDOL_Profile_check());
                var6_5.setPDOLProfileCheckTable(ByteBuffer.fromHexString(var4_3.getPDOL_Profile_check()));
                try {
                    var6_5.setPdolProfileEntries(PDOLCheckEntry.parsePdolEntries(ByteBuffer.fromHexString(var4_3.getPDOL_Profile_check())));
                }
                catch (ParseException var70_60) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "TransactionProfilesContainerJSON: ParseException in parsing PDOL_profile_check.");
                    var70_60.printStackTrace();
                }
            }
            var20_21 = var4_3.getProfiles();
            com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "TransactionProfilesContainerJSON: " + b.mGson.toJson((Object)var20_21));
            var21_22 = new HashMap();
            var22_23 = var20_21.getBF50();
            if (var22_23 != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - BF50: " + b.mGson.toJson((Object)var22_23));
                var23_24 = b.a(var22_23, 0);
                var21_22.put((Object)0, (Object)var23_24);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 0);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "TokenManager cvm counter: " + var23_24.getCVM().getCvmCounter());
            }
            if ((var25_25 = var20_21.getDF21()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF21: " + b.mGson.toJson((Object)var25_25));
                var26_26 = b.a(var25_25, 1);
                var21_22.put((Object)1, (Object)var26_26);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 1);
            }
            if ((var28_27 = var20_21.getDF22()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF22: " + b.mGson.toJson((Object)var28_27));
                var29_28 = b.a(var28_27, 2);
                var21_22.put((Object)2, (Object)var29_28);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 2);
            }
            if ((var31_29 = var20_21.getDF23()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF23: " + b.mGson.toJson((Object)var31_29));
                var32_30 = b.a(var31_29, 3);
                var21_22.put((Object)3, (Object)var32_30);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 3);
            }
            if ((var34_31 = var20_21.getDF24()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF24: " + b.mGson.toJson((Object)var34_31));
                var35_32 = b.a(var34_31, 4);
                var21_22.put((Object)4, (Object)var35_32);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 4);
            }
            if ((var37_33 = var20_21.getDF25()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF25: " + b.mGson.toJson((Object)var37_33));
                var38_34 = b.a(var37_33, 5);
                var21_22.put((Object)5, (Object)var38_34);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 5);
            }
            if ((var40_35 = var20_21.getDF26()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF26: " + b.mGson.toJson((Object)var40_35));
                var41_36 = b.a(var40_35, 6);
                var21_22.put((Object)6, (Object)var41_36);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 6);
            }
            if ((var43_37 = var20_21.getDF27()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF27: " + b.mGson.toJson((Object)var43_37));
                var44_38 = b.a(var43_37, 7);
                var21_22.put((Object)7, (Object)var44_38);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 7);
            }
            if ((var46_39 = var20_21.getDF28()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF28: " + b.mGson.toJson((Object)var46_39));
                var47_40 = b.a(var46_39, 8);
                var21_22.put((Object)8, (Object)var47_40);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 8);
            }
            if ((var49_41 = var20_21.getDF29()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF29: " + b.mGson.toJson((Object)var49_41));
                var50_42 = b.a(var49_41, 9);
                var21_22.put((Object)9, (Object)var50_42);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 9);
            }
            if ((var52_43 = var20_21.getDF2A()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2A: " + b.mGson.toJson((Object)var52_43));
                var53_44 = b.a(var52_43, 10);
                var21_22.put((Object)10, (Object)var53_44);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 10);
            }
            if ((var55_45 = var20_21.getDF2B()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2B: " + b.mGson.toJson((Object)var55_45));
                var56_46 = b.a(var55_45, 11);
                var21_22.put((Object)11, (Object)var56_46);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 11);
            }
            if ((var58_47 = var20_21.getDF2C()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2C: " + b.mGson.toJson((Object)var58_47));
                var59_48 = b.a(var58_47, 12);
                var21_22.put((Object)12, (Object)var59_48);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 12);
            }
            if ((var61_49 = var20_21.getDF2D()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2D: " + b.mGson.toJson((Object)var61_49));
                var62_50 = b.a(var61_49, 13);
                var21_22.put((Object)13, (Object)var62_50);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 13);
            }
            if ((var64_51 = var20_21.getDF2E()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2E: " + b.mGson.toJson((Object)var64_51));
                var65_52 = b.a(var64_51, 14);
                var21_22.put((Object)14, (Object)var65_52);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 14);
            }
            if ((var67_53 = var20_21.getDF2F()) != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "transactionProfileJSON - DF2F: " + b.mGson.toJson((Object)var67_53));
                var68_54 = b.a(var67_53, 15);
                var21_22.put((Object)15, (Object)var68_54);
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Created DiscoverPaymentProfile with Id: " + 15);
            }
            var6_5.setPaymentProfiles((HashMap<Integer, DiscoverPaymentProfile>)var21_22);
            var6_5.setTrack1DataZipMsMode(ByteBuffer.fromHexString(var4_3.getTrack_1_Data_for_ZIPMode()));
            var6_5.setTrack2DataZipMsMode(ByteBuffer.fromHexString(var4_3.getTrack_2_Data_for_ZIPMode()));
            var6_5.setTrack2EquivalentData(ByteBuffer.fromHexString(var4_3.getTrack_2_Equivalent_Data()));
            var5_4.setDiscoverContactlessPaymentData(var6_5);
            var5_4.setDiscoverInnAppPaymentData(var7_6);
            return var5_4;
        }
    }

    private static DiscoverPaymentProfile a(TransactionProfile transactionProfile, int n2) {
        DiscoverPaymentProfile discoverPaymentProfile = new DiscoverPaymentProfile();
        discoverPaymentProfile.setProfileId(n2);
        discoverPaymentProfile.setAip(ByteBuffer.fromHexString(transactionProfile.getAIP()));
        discoverPaymentProfile.setCpr(ByteBuffer.fromHexString(transactionProfile.getCPR()));
        discoverPaymentProfile.setApplicationUsageControl(ByteBuffer.fromHexString(transactionProfile.getAUC()));
        String string = transactionProfile.getAFL();
        if (string != null) {
            discoverPaymentProfile.setAfl(ByteBuffer.fromHexString(string));
        }
        if (transactionProfile.getPRU() != null) {
            discoverPaymentProfile.setPru(ByteBuffer.fromHexString(transactionProfile.getPRU()));
        }
        DiscoverPaymentProfile.CL cL = new DiscoverPaymentProfile.CL();
        cL.setCL_Cons_Limit(ByteBuffer.fromHexString(transactionProfile.getCL_Cons_limit()).getLong());
        cL.setCL_Cum_Limit(Long.parseLong((String)transactionProfile.getCL_Cum_limit()));
        cL.setCL_STA_Limit(Long.parseLong((String)transactionProfile.getCL_STA_limit()));
        if (transactionProfile.getCL_Counter() != null) {
            cL.setClCounter(ByteBuffer.fromHexString(transactionProfile.getCL_Counter()).getLong());
        }
        if (transactionProfile.getCL_Accumulator() != null) {
            cL.setClAccumulator(Long.parseLong((String)transactionProfile.getCL_Accumulator()));
        }
        discoverPaymentProfile.setCl(cL);
        DiscoverPaymentProfile.CRM cRM = new DiscoverPaymentProfile.CRM();
        cRM.setLCOA(Long.parseLong((String)transactionProfile.getLCOA()));
        cRM.setLCOL(ByteBuffer.fromHexString(transactionProfile.getLCOL()).getLong());
        cRM.setSTA(Long.parseLong((String)transactionProfile.getSTA()));
        cRM.setUCOA(Long.parseLong((String)transactionProfile.getUCOA()));
        cRM.setUCOL(ByteBuffer.fromHexString(transactionProfile.getUCOL()).getLong());
        cRM.setCRM_CAC_Default(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Default()));
        cRM.setCRM_CAC_Denial(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Denial()));
        cRM.setCRM_CAC_Online(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Online()));
        cRM.setCrmAccumulator(Long.parseLong((String)transactionProfile.getCOA()));
        cRM.setCrmCounter(ByteBuffer.fromHexString(transactionProfile.getNCOT()).getLong());
        if (transactionProfile.getCRM_CAC_Switch_Interface() != null) {
            cRM.setCRM_CAC_Switch_Interface(ByteBuffer.fromHexString(transactionProfile.getCRM_CAC_Switch_Interface()));
        }
        discoverPaymentProfile.setCRM(cRM);
        DiscoverPaymentProfile.CVM cVM = new DiscoverPaymentProfile.CVM();
        cVM.setCVM_CAC_Online_PIN(ByteBuffer.fromHexString(transactionProfile.getCVM_CAC_Online_PIN()));
        if (transactionProfile.getCVM_CAC_Signature() != null) {
            cVM.setCVM_CAC_Signature(ByteBuffer.fromHexString(transactionProfile.getCVM_CAC_Signature()).getLong());
        }
        if (transactionProfile.getCVM_Cons_limit_1() != null && ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_1()) != null) {
            cVM.setCVM_Cons_Limit_1(ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_1()).getLong());
        }
        if (transactionProfile.getCVM_Cons_limit_2() != null && ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_2()) != null) {
            cVM.setCVM_Cons_Limit_2(ByteBuffer.fromHexString(transactionProfile.getCVM_Cons_limit_2()).getLong());
        }
        if (transactionProfile.getCVM_Cum_limit_1() != null) {
            cVM.setCVM_Cum_Limit_1(Long.parseLong((String)transactionProfile.getCVM_Cum_limit_1()));
        }
        if (transactionProfile.getCVM_Cum_limit_2() != null) {
            cVM.setCVM_Cum_Limit_2(Long.parseLong((String)transactionProfile.getCVM_Cum_limit_2()));
        }
        if (transactionProfile.getCVM_Sta_limit_1() != null) {
            cVM.setCVM_Sta_Limit_1(Long.parseLong((String)transactionProfile.getCVM_Sta_limit_1()));
        }
        if (transactionProfile.getCVM_Sta_limit_2() != null) {
            cVM.setCVM_Sta_Limit_2(Long.parseLong((String)transactionProfile.getCVM_Sta_limit_2()));
        }
        if (transactionProfile.getCVM_Counter() != null) {
            cVM.setCvmCounter(ByteBuffer.fromHexString(transactionProfile.getCVM_Counter()).getLong());
        }
        if (transactionProfile.getCVM_Accumulator() != null) {
            cVM.setCvmAccumulator(Long.parseLong((String)transactionProfile.getCVM_Accumulator()));
        }
        discoverPaymentProfile.setCVM(cVM);
        return discoverPaymentProfile;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private DcTACommands.CardCtxEncryption.Response.a a(ClearCardInfo clearCardInfo, List<byte[]> list) {
        byte[] arrby = mGson.toJson((Object)clearCardInfo).getBytes();
        if (arrby.length == 0) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "createSecureCardContext: CardContext serialization failed");
            return null;
        }
        try {
            DcTACommands.CardCtxEncryption.Response.a a2 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().b(arrby, list);
            return a2;
        }
        catch (DcTAException dcTAException) {
            if (dcTAException.getErrorCode() == DcTAException.Code.xU.getCode()) {
                String string = com.samsung.android.spayfw.payprovider.discover.util.b.byteArrayToHex((byte[])list.get(1));
                int n2 = string.length();
                com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "createSecureCardContext: Cert(1) - " + string.substring(0, 30) + " ... " + string.substring(n2 - 20, n2));
            }
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "createSecureCardContext: TA Exception - " + dcTAException.toString());
            dcTAException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "createSecureCardContext: Exception - " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void a(String[] arrstring, HashMap<String, ByteBuffer> hashMap) {
        int n2 = arrstring.length;
        int n3 = 0;
        while (n3 < n2) {
            String string = arrstring[n3];
            if (string == null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "parseAltAids, alt fci is null, check next.");
            } else {
                try {
                    com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse alt fci: " + string);
                    com.samsung.android.spayfw.payprovider.discover.payment.utils.b b2 = com.samsung.android.spayfw.payprovider.discover.payment.utils.a.F(ByteBuffer.fromHexString(string));
                    if (b2 == null) {
                        com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "parseAltAids, parsed aid is null.");
                        return;
                    }
                    ByteBuffer byteBuffer = b2.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vP.getInt()) != null ? (ByteBuffer)b2.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vP.getInt()).get(0) : null;
                    if (byteBuffer != null) {
                        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse alt aid: " + byteBuffer.toHexString());
                        hashMap.put((Object)byteBuffer.toHexString(), (Object)ByteBuffer.fromHexString(string));
                    }
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse alt aid is null.");
                }
                catch (ParseException parseException) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "clearProvisionDataJSON, parse exception observed while alt aid parsing.");
                    parseException.printStackTrace();
                }
            }
            ++n3;
        }
        return;
    }

    private boolean a(BillingInfo billingInfo) {
        return true;
    }

    private String aP(String string) {
        String string2 = string.toLowerCase();
        return Hex.toHexString((byte[])com.samsung.android.spayfw.payprovider.discover.util.a.a(string2, com.samsung.android.spayfw.payprovider.discover.util.a.aQ(string2), "SHA-256", 2000));
    }

    private boolean b(EnrollCardPanInfo enrollCardPanInfo) {
        if (enrollCardPanInfo.getPAN().trim().length() != 16) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "validateCardInfo: PAN");
            return false;
        }
        int n2 = enrollCardPanInfo.getCVV().trim().length();
        if (n2 < 3 || n2 > 4) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "validateCardInfo: CVV");
            return false;
        }
        if (enrollCardPanInfo.getName().trim().isEmpty()) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "validateCardInfo: Name");
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private DevicePublicKeyContext eh() {
        DcTACommands.DevicePublicKeyCtxEncryption.Response.a a2;
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "getDevicePubKeyContext");
        try {
            DcTACommands.DevicePublicKeyCtxEncryption.Response.a a3;
            a2 = a3 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().ev();
            if (a2 == null) {
                return null;
            }
        }
        catch (DcTAException dcTAException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "getDevicePubKeyContext: TA Exception - " + dcTAException.toString());
            dcTAException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "getDevicePubKeyContext: Exception - " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        String string = new String(a2.getEncryptedData());
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "certChain = ");
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", string);
        DevicePublicKeyContext devicePublicKeyContext = new DevicePublicKeyContext();
        devicePublicKeyContext.setPublicKeyCertificateChain(string);
        return devicePublicKeyContext;
    }

    private JsonObject h(Object object) {
        String string = mGson.toJson(object);
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "createJsonObj: " + string);
        return new JsonParser().parse(string).getAsJsonObject();
    }

    public c a(EnrollCardPanInfo enrollCardPanInfo, BillingInfo billingInfo, List<byte[]> list) {
        c c2 = new c();
        c2.setErrorCode(0);
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "getEnrollmentData");
        if (!this.b(enrollCardPanInfo)) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "getEnrollmentData: validateCardInfo failed");
            c2.setErrorCode(-4);
            return c2;
        }
        if (!this.a(billingInfo)) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "getEnrollmentData: validateBillingInfo failed");
            c2.setErrorCode(-4);
            return c2;
        }
        if (list == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "getEnrollmentData: serverCertChain is null");
            c2.setErrorCode(-4);
            return c2;
        }
        DcTACommands.CardCtxEncryption.Response.a a2 = this.a(ClearCardInfo.getEnrollmentPayload(enrollCardPanInfo, billingInfo), list);
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "createSecureCardContext(Enrollment) Failed");
            c2.setErrorCode(-6);
            return c2;
        }
        DcTACommands.CardCtxEncryption.Response.a a3 = this.a(ClearCardInfo.getProvisionPayload(enrollCardPanInfo), list);
        if (a3 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "createSecureCardContext(Provision) Failed");
            c2.setErrorCode(-6);
            return c2;
        }
        AccountEligibilityRequestData accountEligibilityRequestData = new AccountEligibilityRequestData();
        SecureContext secureContext = new SecureContext();
        secureContext.setEncryptedPayload(new String(a2.getEncryptedData()));
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "Length of Encrypted Data: " + a2.getEncryptedData().length);
        accountEligibilityRequestData.setSecureCardContext(secureContext);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("accountEligibilityRequest", (JsonElement)this.h(accountEligibilityRequestData));
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "Eligibility Req Data: " + jsonObject.toString());
        c2.a(jsonObject);
        Bundle bundle = new Bundle();
        bundle.putString("emailHash", this.aP(enrollCardPanInfo.getUserEmail()));
        c2.e(bundle);
        this.wv = new SecureContext(new String(a3.getEncryptedData()));
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public c a(f f2, List<byte[]> list) {
        DcTACommands.ReplenishContextEncryption.Response.a a2;
        DcCardMaster dcCardMaster;
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "generateReplenishRequest");
        c c2 = new c();
        RefreshCredentialsRequestData refreshCredentialsRequestData = new RefreshCredentialsRequestData();
        if (list == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "generateReplenishRequest: serverCertChain is null");
            c2.setErrorCode(-4);
            return c2;
        }
        byte[] arrby = DcStorageManager.h(f2.cm());
        try {
            DcCardMaster dcCardMaster2;
            dcCardMaster = dcCardMaster2 = (DcCardMaster)this.wu.getData(f2.cm());
        }
        catch (DcDbException dcDbException) {
            dcDbException.printStackTrace();
            dcCardMaster = null;
        }
        if (dcCardMaster == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "generateReplenishRequest: Failed to load CardMaster record for id: " + f2.cm());
            c2.setErrorCode(-2);
            return c2;
        }
        ClearRefreshCredentialsRequestData clearRefreshCredentialsRequestData = new ClearRefreshCredentialsRequestData(dcCardMaster.getTokenId());
        String string = mGson.toJson((Object)clearRefreshCredentialsRequestData);
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Replenish Request Payload: " + string);
        try {
            DcTACommands.ReplenishContextEncryption.Response.a a3;
            a2 = a3 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().a(arrby, list, string);
        }
        catch (DcTAException dcTAException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "generateReplenishRequest: TA Exception - " + dcTAException.toString());
            dcTAException.printStackTrace();
            a2 = null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "generateReplenishRequest: Exception - " + exception.getMessage());
            exception.printStackTrace();
            a2 = null;
        }
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "TA Failed");
            c2.setErrorCode(-6);
            return c2;
        }
        DevicePublicKeyContext devicePublicKeyContext = this.eh();
        if (devicePublicKeyContext == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "Failed to get DevicePublicKeyContext");
            c2.setErrorCode(-9);
            return c2;
        }
        SecureContext secureContext = new SecureContext(new String(a2.getEncryptedData()));
        refreshCredentialsRequestData.setTokenId(dcCardMaster.getTokenId());
        refreshCredentialsRequestData.setRequestContext(secureContext);
        refreshCredentialsRequestData.setDevicePublicKeyContext(devicePublicKeyContext);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("refreshCredentialsRequest", (JsonElement)this.h(refreshCredentialsRequestData));
        c2.a(jsonObject);
        return c2;
    }

    public e a(f f2, JsonObject jsonObject, TokenStatus tokenStatus) {
        e e2;
        block5 : {
            com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "updateToken:Status - " + tokenStatus.getCode());
            if (jsonObject != null) {
                com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "responseData: " + jsonObject.getAsString());
            }
            e2 = new e();
            e2.setErrorCode(0);
            if (f2.cn() == null) break block5;
            DcStorageManager.a(f2.cm(), tokenStatus.getCode());
            return e2;
        }
        try {
            if (f2.getTrTokenId() == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "updateToken: getTrTokenId is null");
                e2.setErrorCode(-4);
                return e2;
            }
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            e2.setErrorCode(-2);
        }
        return e2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public e a(String string, JsonObject jsonObject, int n2, List<byte[]> list) {
        int n3;
        DcStorageManager.ResultCode resultCode;
        DcTACommands.ProcessCardProfile.Response.a a2;
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "processToken " + jsonObject.toString());
        e e2 = new e();
        ProvisionCredentialsData provisionCredentialsData = (ProvisionCredentialsData)mGson.fromJson((JsonElement)jsonObject.getAsJsonObject("provisionCredentialsContext"), ProvisionCredentialsData.class);
        if (provisionCredentialsData == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Failed to parse responseData");
            e2.setErrorCode(-4);
            return e2;
        }
        if (list == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: serverCertChain is null");
            e2.setErrorCode(-4);
            return e2;
        }
        try {
            DcTACommands.ProcessCardProfile.Response.a a3;
            byte[] arrby = provisionCredentialsData.getSecureProvisionCredentialsRequestContext().getEncryptedPayload().getBytes();
            a2 = a3 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().a(arrby, list, true);
        }
        catch (DcTAException dcTAException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: TA Exception" + dcTAException.toString());
            dcTAException.printStackTrace();
            a2 = null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Exception " + exception.getMessage());
            exception.printStackTrace();
            a2 = null;
        }
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Failed to process card profile");
            e2.setErrorCode(-6);
            return e2;
        }
        DcCardMaster dcCardMaster = new DcCardMaster();
        com.samsung.android.spayfw.payprovider.discover.db.dao.c c2 = new com.samsung.android.spayfw.payprovider.discover.db.dao.c(com.samsung.android.spayfw.payprovider.discover.a.cC());
        long l2 = -1L;
        dcCardMaster.setDpanSuffix("");
        dcCardMaster.setFpanSuffix("");
        dcCardMaster.setIsProvisioned(1);
        dcCardMaster.setTokenId(provisionCredentialsData.getTokenId());
        try {
            long l3;
            l2 = l3 = c2.saveData(dcCardMaster);
        }
        catch (DcDbException dcDbException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: DcDbException " + dcDbException.getMessage());
            dcDbException.printStackTrace();
        }
        if (l2 == -1L) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Failed to process card profile");
            e2.setErrorCode(-2);
            return e2;
        }
        this.ww = true;
        DiscoverPaymentCard discoverPaymentCard = b.a(l2, a2);
        if (discoverPaymentCard == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Failed to convert2DiscoverPaymentCard");
            e2.setErrorCode(-2);
            return e2;
        }
        discoverPaymentCard.setSecureObject(a2.getEncryptedData());
        discoverPaymentCard.setOTPK(a2.eq());
        DcStorageManager.ResultCode resultCode2 = DcStorageManager.a(discoverPaymentCard);
        try {
            int n4;
            String string2 = a2.er();
            n3 = n4 = ((ClearProvisionData)mGson.fromJson(string2, ClearProvisionData.class)).getProfileData().getConstraints().getLowCredentialsThreshold();
        }
        catch (NullPointerException nullPointerException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Failed to get Replenishment Threshold value - " + nullPointerException.getMessage() + ". Using default value - " + 5);
            nullPointerException.printStackTrace();
            n3 = 5;
        }
        if (resultCode2 == DcStorageManager.ResultCode.sw) {
            dcCardMaster.setRemainingOtpkCount(a2.es());
            dcCardMaster.setReplenishmentThreshold(n3);
            try {
                this.wu.updateData(dcCardMaster, l2);
                resultCode = resultCode2;
            }
            catch (DcDbException dcDbException) {
                dcDbException.printStackTrace();
                resultCode = DcStorageManager.ResultCode.sB;
            }
        } else {
            resultCode = resultCode2;
        }
        if (resultCode == DcStorageManager.ResultCode.sw) {
            e2.setProviderTokenKey(new f(l2));
            e2.setErrorCode(0);
            return e2;
        }
        com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processToken: Failed to save Discover Payment Card object " + resultCode.getErrorMessage());
        e2.setErrorCode(-2);
        try {
            this.wu.deleteData(l2);
            return e2;
        }
        catch (DcDbException dcDbException) {
            dcDbException.printStackTrace();
            return e2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public e a(String string, f f2, JsonObject jsonObject, TokenStatus tokenStatus, List<byte[]> list) {
        DcTACommands.ProcessReplenishmentData.Response.a a2;
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "processOTPK " + jsonObject.toString());
        e e2 = new e();
        RefreshCredentialsResponseData refreshCredentialsResponseData = (RefreshCredentialsResponseData)mGson.fromJson((JsonElement)jsonObject.getAsJsonObject("refreshCredentialsResponse"), RefreshCredentialsResponseData.class);
        if (refreshCredentialsResponseData == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processOTPK: Failed to parse responseData");
            e2.setErrorCode(-4);
            return e2;
        }
        if (list == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processOTPK: serverCertChain is null");
            e2.setErrorCode(-4);
            return e2;
        }
        try {
            DcTACommands.ProcessReplenishmentData.Response.a a3;
            byte[] arrby = refreshCredentialsResponseData.getResponseContext().getEncryptedPayload().getBytes();
            a2 = a3 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().b(arrby, DcStorageManager.h(f2.cm()), list, true);
        }
        catch (DcTAException dcTAException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processOTPK: TA Exception - " + dcTAException.toString());
            dcTAException.printStackTrace();
            a2 = null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processOTPK: Exception - " + exception.getMessage());
            exception.printStackTrace();
            a2 = null;
        }
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processOTPK: Failed to process OTPK in TA");
            e2.setErrorCode(-6);
            return e2;
        }
        DcStorageManager.ResultCode resultCode = DcStorageManager.c(f2.cm(), a2.eq());
        if (resultCode == DcStorageManager.ResultCode.sw) {
            com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "saveOTPKData Success");
            int n2 = a2.es();
            resultCode = DcStorageManager.a(f2.cm(), (long)n2);
        }
        if (resultCode != DcStorageManager.ResultCode.sw) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "processOTPK: Failed to store data in DB");
            e2.setErrorCode(-2);
            return e2;
        }
        e2.setErrorCode(0);
        return e2;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] aO(String string) {
        byte[] arrby = Base64.decode((String)string, (int)2);
        if (arrby == null) return null;
        try {
            return com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().a(arrby, DcTACommands.ProcessDataOperationType.xy);
        }
        catch (DcTAException dcTAException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "decryptSignatureData: " + dcTAException.toString());
            dcTAException.printStackTrace();
        }
        return null;
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "decryptSignatureData: Exception Occured - " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    public c b(EnrollCardReferenceInfo enrollCardReferenceInfo) {
        c c2 = new c();
        c2.setErrorCode(0);
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "getPushEnrollementData");
        AccountEligibilityRequestData accountEligibilityRequestData = new AccountEligibilityRequestData();
        SecureContext secureContext = new SecureContext();
        String string = new String(enrollCardReferenceInfo.getExtraEnrollData().getByteArray("enrollPayload"));
        secureContext.setEncryptedPayload(string);
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "Length of Encrypted Data: " + string.length() + ", Data: " + string.substring(0, 50));
        accountEligibilityRequestData.setSecureCardContext(secureContext);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("accountEligibilityRequest", (JsonElement)this.h(accountEligibilityRequestData));
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "Eligibility Req Data: " + jsonObject.toString());
        c2.a(jsonObject);
        Bundle bundle = new Bundle();
        bundle.putString("emailHash", this.aP(enrollCardReferenceInfo.getUserEmail()));
        c2.e(bundle);
        this.wv = secureContext;
        return c2;
    }

    public c c(ProvisionTokenInfo provisionTokenInfo) {
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "getProvisionData");
        c c2 = new c();
        AccountProvisionRequestData accountProvisionRequestData = new AccountProvisionRequestData();
        DevicePublicKeyContext devicePublicKeyContext = this.eh();
        if (devicePublicKeyContext != null) {
            accountProvisionRequestData.setSecureCardContext(new SecureContext(this.wv.getEncryptedPayload()));
            accountProvisionRequestData.setDevicePublicKeyContext(devicePublicKeyContext);
            com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "2.0 Prov Request: ");
            c2.a(this.h(accountProvisionRequestData));
            Bundle bundle = new Bundle();
            bundle.putSerializable("riskData", a.eg().b(provisionTokenInfo));
            c2.e(bundle);
            c2.setErrorCode(0);
            return c2;
        }
        com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "getProvisionData: TA Response Null");
        c2.setErrorCode(-2);
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void d(f f2) {
        com.samsung.android.spayfw.b.c.i("DCSDK_DcTokenManager", "delete");
        if (f2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "Token Id is null");
            return;
        }
        try {
            if (f2.cn() != null) {
                if (DcStorageManager.k(f2.cm()) != DcStorageManager.ResultCode.sw) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "Failed to delete token");
                    return;
                }
            } else if (f2.getTrTokenId() == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "updateToken: getTrTokenId is null");
                return;
            }
        }
        catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            if (this.ww) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "delete: mProcessTokenComplete - true but failed to get token key from tokenId");
            }
            DcStorageManager.cH();
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            DcStorageManager.cH();
            return;
        }
        com.samsung.android.spayfw.b.c.d("DCSDK_DcTokenManager", "Successfully deleted Token rowId: " + f2.cn() + ", TR Id: " + f2.getTrTokenId());
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        RefreshCredentialsResponseData refreshCredentialsResponseData = (RefreshCredentialsResponseData)mGson.fromJson((JsonElement)jsonObject.getAsJsonObject("refreshCredentialsResponse"), RefreshCredentialsResponseData.class);
        return refreshCredentialsResponseData != null && refreshCredentialsResponseData.getBundleSeqNum() != null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String j(byte[] arrby) {
        byte[] arrby2;
        try {
            byte[] arrby3;
            arrby2 = arrby3 = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.eu().a(arrby, DcTACommands.ProcessDataOperationType.xx);
            String string = null;
            if (arrby2 == null) return string;
        }
        catch (DcTAException dcTAException) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "encryptSignatureData: " + dcTAException.toString());
            dcTAException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DcTokenManager", "encryptSignatureData: Exception Occured - " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        return Base64.encodeToString((byte[])arrby2, (int)2);
    }
}

