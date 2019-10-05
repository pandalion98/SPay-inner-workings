/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  com.google.gson.JsonObject
 *  java.io.Serializable
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Locale
 *  java.util.Set
 */
package com.samsung.android.spayfw.core;

import android.os.Bundle;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Activation;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ActivationParameters;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardEnrollmentInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Initiator;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Mode;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TimeStamp;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserName;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.WalletInfo;
import com.samsung.android.spayfw.utils.h;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class l {
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static EnrollmentRequestData a(EnrollCardInfo var0, CertificateInfo[] var1_1, DeviceInfo var2_2, com.samsung.android.spayfw.payprovider.c var3_3, c var4_4, String var5_5) {
        block8 : {
            block7 : {
                var6_6 = new EnrollmentRequestData();
                var6_6.setWallet(new WalletInfo(var0.getWalletId()));
                var7_7 = new CardEnrollmentInfo(null);
                var7_7.setBrand(var4_4.getCardBrand());
                var8_8 = var0.getApplicationId();
                if (var3_3 == null) break block7;
                if (var3_3.ch() != null) {
                    var7_7.setData(var3_3.ch());
                }
                if ((var14_9 = var3_3.cg()) != null) break block8;
            }
            var9_11 = null;
            ** GOTO lbl30
        }
        var15_10 = var14_9.keySet().iterator();
        var9_11 = null;
        do {
            block11 : {
                block12 : {
                    block9 : {
                        block10 : {
                            if (!var15_10.hasNext()) break block9;
                            var16_12 = (String)var15_10.next();
                            if (!var16_12.equals((Object)"emailHash")) break block10;
                            var19_15 = var14_9.getString("emailHash");
                            var20_16 = var8_8;
                            var18_14 = var19_15;
                            var17_13 = var20_16;
                            break block11;
                        }
                        if (!var16_12.equals((Object)"appId")) break block12;
                        var17_13 = var14_9.getString("appId");
                        var18_14 = var9_11;
                        break block11;
                    }
                    if (var5_5 != null) {
                        Log.d("RequestDataBuilder", "CardRef Id based enrollment. setting cardRef id ");
                        var7_7.setReference(var5_5);
                    }
                    var2_2.setParentId(var0.getDeviceParentId());
                    var6_6.setCard(var7_7);
                    if (var1_1 != null) {
                        var2_2.setCertificates(var1_1);
                    } else {
                        Log.w("RequestDataBuilder", "device certificates are empty");
                        var2_2.setCertificates(new CertificateInfo[3]);
                    }
                    var6_6.setDevice(var2_2);
                    var6_6.setEntry(new Mode(var0.getCardEntryMode()));
                    var6_6.setPresentation(new Mode(l.n(var4_4.ab())));
                    var10_17 = new UserInfo(var0.getUserId());
                    var10_17.setMask(h.bA(var0.getUserEmail()));
                    var10_17.setLanguage(Locale.getDefault().getLanguage());
                    var10_17.setHash(var9_11);
                    var6_6.setApp(new Id(var8_8));
                    var11_18 = var0 instanceof EnrollCardPanInfo != false ? ((EnrollCardPanInfo)var0).getName() : (var0 instanceof EnrollCardReferenceInfo != false ? ((EnrollCardReferenceInfo)var0).getName() : (var0 instanceof EnrollCardLoyaltyInfo != false ? "Loyalty" : null));
                    if (var11_18 == null) {
                        var11_18 = "SamsungPayUser";
                    }
                    var10_17.setName(new UserName(var11_18));
                    var6_6.setUser(var10_17);
                    if (var0 == null) return var6_6;
                    if (var0.getExtraEnrollData() == null) return var6_6;
                    var12_19 = var0.getExtraEnrollData().getString("enrollCardFeature");
                    var13_20 = var0.getExtraEnrollData().getString("enrollCardInitiatorId");
                    if (var13_20 == null) {
                        if (var12_19 == null) return var6_6;
                    }
                    Log.d("RequestDataBuilder", "Initiator id " + var13_20);
                    Log.d("RequestDataBuilder", "Initiator feature " + var12_19);
                    var6_6.setInitiator(new Initiator(var13_20, var12_19));
                    return var6_6;
                }
                var17_13 = var8_8;
                var18_14 = var9_11;
            }
            var9_11 = var18_14;
            var8_8 = var17_13;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static TokenRequestData a(long var0, String var2_1, ProvisionTokenInfo var3_2, com.samsung.android.spayfw.payprovider.c var4_3) {
        var5_4 = new Id(var2_1);
        var6_5 = var0 LCMP -1L;
        var7_6 = null;
        if (var6_5 != false) {
            var7_6 = new TimeStamp(var0);
        }
        var8_7 = new TokenRequestData(var5_4, var7_6);
        if (var4_3 == null) return var8_7;
        if (var4_3.getErrorCode() != 0) return var8_7;
        var9_8 = new Activation(System.currentTimeMillis());
        var10_9 = var4_3.cg();
        if (var10_9 == null) ** GOTO lbl21
        var11_10 = new ArrayList();
        var12_11 = var10_9.keySet().iterator();
        block0 : do lbl-1000: // 3 sources:
        {
            block7 : {
                block6 : {
                    if (!var12_11.hasNext()) break block6;
                    if (!((String)var12_11.next()).equals((Object)"riskData")) ** GOTO lbl-1000
                    var13_12 = (ArrayList)var10_9.getSerializable("riskData");
                    if (var13_12 != null && !var13_12.isEmpty()) break block7;
                }
                var9_8.setParameters((List<ActivationParameters>)var11_10);
lbl21: // 2 sources:
                var8_7.setActivation(var9_8);
                var8_7.setData(var4_3.ch());
                return var8_7;
            }
            var14_13 = var13_12.iterator();
            do {
                if (!var14_13.hasNext()) continue block0;
                var15_14 = (RiskDataParam)var14_13.next();
                if (var15_14 == null || var15_14.getKey() == null || var15_14.getValue() == null) continue;
                Log.d("RequestDataBuilder", "RiskData: key = " + var15_14.getKey() + "; value = " + var15_14.getValue());
                var11_10.add((Object)new ActivationParameters(var15_14.getKey(), var15_14.getValue()));
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final void a(com.samsung.android.spayfw.payprovider.c c2, EnrollmentRequestData enrollmentRequestData, String string) {
        block8 : {
            block7 : {
                Bundle bundle;
                if (c2 == null || enrollmentRequestData == null) break block7;
                if (c2.ch() != null && enrollmentRequestData.getCard() != null) {
                    enrollmentRequestData.getCard().setData(c2.ch());
                }
                if ((bundle = c2.cg()) != null) {
                    for (String string2 : bundle.keySet()) {
                        String string3;
                        if (string2.equals((Object)"emailHash")) {
                            String string4 = bundle.getString("emailHash");
                            if (string4 == null || enrollmentRequestData.getUser() == null) continue;
                            enrollmentRequestData.getUser().setHash(string4);
                            continue;
                        }
                        if (!string2.equals((Object)"appId") || (string3 = bundle.getString("appId")) == null) continue;
                        enrollmentRequestData.setApp(new Id(string3));
                    }
                }
                if (string != null) break block8;
            }
            return;
        }
        Log.d("RequestDataBuilder", "CardRef Id based enrollment. setting cardRef id ");
        enrollmentRequestData.getCard().setReference(string);
    }

    private static String n(int n2) {
        switch (n2) {
            default: {
                return "ALL";
            }
            case 15: {
                return "ALL";
            }
            case 1: {
                return "NFC";
            }
            case 2: {
                return "MST";
            }
            case 4: 
        }
        return "ECM";
    }
}

