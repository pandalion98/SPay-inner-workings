/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Base64
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Arrays
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;
import com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardDetail;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.TAException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class g
extends o {
    public static final boolean DEBUG = h.DEBUG;
    private static g lg;
    private ServerCertsStorage lh = ServerCertsStorage.ad(this.mContext);
    private HashSet<String> li = new HashSet();

    private g(Context context) {
        super(context);
        this.li.add((Object)"AU");
        this.li.add((Object)"BR");
        this.li.add((Object)"SG");
        this.li.add((Object)"ES");
        this.li.add((Object)"MY");
        this.li.add((Object)"TH");
        this.li.add((Object)"CH");
        this.li.add((Object)"TW");
        this.li.add((Object)"RU");
        this.li.add((Object)"AE");
        this.li.add((Object)"SE");
    }

    /*
     * Enabled aggressive block sorting
     */
    private c N(String string) {
        c c2 = null;
        if (this.iJ == null) {
            Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardObject. Initializing Samsung Account - null userId ");
            this.iJ = com.samsung.android.spayfw.core.a.a(this.mContext, null);
        }
        if (this.iJ == null) {
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardObject. unable to create account");
            return c2;
        } else {
            c2 = this.iJ.r(string);
            if (c2 != null) return c2;
            {
                Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardObject. failed to find Card");
                return c2;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private a O(String string) {
        List<CertificateInfo> list = this.lh.a(ServerCertsStorage.ServerCertsDb.ServerCertsColumn.Cg, string);
        if (list == null || list.size() <= 0) {
            if (DEBUG) {
                Log.e("GlobalMembershipCardProcessor", "getServerCerts : failed to get certificates [" + string + "]");
                return null;
            }
            Log.e("GlobalMembershipCardProcessor", "getServerCerts : failed to get certificates");
            return null;
        }
        Iterator iterator = list.iterator();
        byte[] arrby = null;
        byte[] arrby2 = null;
        byte[] arrby3 = null;
        do {
            byte[] arrby4;
            byte[] arrby5;
            byte[] arrby6;
            block6 : {
                block8 : {
                    block4 : {
                        CertificateInfo certificateInfo;
                        String string2;
                        block7 : {
                            block5 : {
                                if (!iterator.hasNext()) break block4;
                                certificateInfo = (CertificateInfo)iterator.next();
                                string2 = certificateInfo.getContent().replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"");
                                if (!"ENC".equals((Object)certificateInfo.getUsage())) break block5;
                                byte[] arrby7 = Base64.decode((String)string2, (int)0);
                                byte[] arrby8 = arrby;
                                arrby5 = arrby2;
                                arrby4 = arrby7;
                                arrby6 = arrby8;
                                break block6;
                            }
                            if (!"VER".equals((Object)certificateInfo.getUsage())) break block7;
                            byte[] arrby9 = Base64.decode((String)string2, (int)0);
                            arrby4 = arrby3;
                            arrby6 = arrby;
                            arrby5 = arrby9;
                            break block6;
                        }
                        if (!"CA".equals((Object)certificateInfo.getUsage())) break block8;
                        arrby6 = Base64.decode((String)string2, (int)0);
                        arrby5 = arrby2;
                        arrby4 = arrby3;
                        break block6;
                    }
                    if (arrby3 != null && arrby2 != null && arrby != null) {
                        return new a(arrby, arrby3, arrby2);
                    }
                    Log.e("GlobalMembershipCardProcessor", "getServerCerts : failed to get certificates. cert value is null");
                    return null;
                }
                arrby6 = arrby;
                arrby5 = arrby2;
                arrby4 = arrby3;
            }
            arrby3 = arrby4;
            arrby2 = arrby5;
            arrby = arrby6;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(String string, String string2, String string3) {
        if (this.iJ == null) {
            Log.d("GlobalMembershipCardProcessor", "Initializing Samsung Account - userId = " + string3);
            this.iJ = com.samsung.android.spayfw.core.a.a(this.mContext, string3);
        }
        if (this.iJ == null) {
            Log.e("GlobalMembershipCardProcessor", "unable to create account");
            return;
        }
        try {
            c c2 = new c(this.mContext, "GM", "GM", "GM", 0);
            q q2 = new q();
            q2.setTokenStatus("ACTIVE");
            q2.setTokenId(string);
            f f2 = new f(string);
            q2.c(f2);
            c2.a(q2);
            c2.setEnrollmentId(string);
            this.iJ.a(c2);
            if (c2.ad() != null) {
                Log.d("GlobalMembershipCardProcessor", "Set Provider Token Key for Global Membership Card");
                c2.ad().setProviderTokenKey(f2);
            }
            com.samsung.android.spayfw.storage.models.a a2 = new com.samsung.android.spayfw.storage.models.a(string);
            a2.setUserId(string3);
            a2.setTrTokenId(string);
            a2.j(0);
            a2.setCardBrand("GM");
            a2.setTokenStatus("ACTIVE");
            a2.setTokenRefId(string2);
            this.jJ.c(a2);
            if (!DEBUG) return;
            Log.d("GlobalMembershipCardProcessor", "addCardToken. add partnerId [" + string2 + "], tokenId[" + string + "]");
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private c aY() {
        try {
            c c2 = new c(this.mContext, "GM", "GM", "GM", 0);
            return c2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private boolean aZ() {
        return this.li.contains((Object)h.fP().toUpperCase());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final g o(Context context) {
        Class<g> class_ = g.class;
        synchronized (g.class) {
            try {
                if (lg != null) return lg;
                lg = new g(context);
                return lg;
            }
            catch (TAException tAException) {
                Log.c("GlobalMembershipCardProcessor", tAException.getMessage(), (Throwable)((Object)tAException));
                return null;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void a(GlobalMembershipCardRegisterRequestData var1_1, IGlobalMembershipCardRegisterCallback var2_2) {
        var3_3 = new GlobalMembershipCardRegisterResponseData();
        if (var1_1 != null && var2_2 != null) ** GOTO lbl10
        if (var2_2 == null) ** GOTO lbl8
        try {
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: invalid inputs: data is null");
            var2_2.onFail(-5, var3_3);
            return;
lbl8: // 1 sources:
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: invalid inputs: callback is null");
            return;
lbl10: // 1 sources:
            if (!this.aZ()) {
                var5_5 = new GlobalMembershipCardRegisterResponseData();
                Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData() calls onFail. this country doesn't support GlobalMembership");
                if (var2_2 == null) return;
                var2_2.onFail(-207, var5_5);
                return;
            }
        }
        catch (RemoteException var4_4) {
            Log.c("GlobalMembershipCardProcessor", var4_4.getMessage(), var4_4);
            return;
        }
        Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData() [" + var1_1 + "]");
        if (var1_1.getGlobalMembershipCardData() == null || TextUtils.isEmpty((CharSequence)var1_1.getUserId()) || TextUtils.isEmpty((CharSequence)var1_1.getPartnerId())) {
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: invalid data");
            var2_2.onFail(-5, var3_3);
            return;
        }
        if (var1_1.allServerCertsNotNull()) {
            this.a(var1_1.getPartnerId(), var1_1.getServerCaCert(), var1_1.getServerEncCert(), var1_1.getServerVerCert());
        } else {
            var6_7 = this.O(var1_1.getPartnerId());
            if (var6_7 == null) {
                Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: serverCerts are null and not stored.");
            } else {
                var1_1.setServerCaCert(var6_7.lj);
                var1_1.setServerEncCert(var6_7.lk);
                var1_1.setServerVerCert(var6_7.ll);
            }
        }
        if ((var7_6 = this.aY()) == null || var7_6.ad() == null) {
            var2_2.onFail(-1, var3_3);
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: unable to get card Object");
            return;
        }
        var8_8 = var7_6.ad().getGlobalMembershipCardRegisterDataTA(var1_1);
        if (var8_8.getErrorCode() == 0) {
            var2_2.onSuccess(var8_8);
            return;
        }
        var2_2.onFail(var8_8.getErrorCode(), var8_8);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(String string, byte[] arrby, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
        block6 : {
            try {
                Log.d("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay is not supported yet");
                if (arrby == null || iPayCallback == null) {
                    if (iPayCallback != null) {
                        Log.e("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay: invalid inputs: data is null");
                        iPayCallback.onFail(string, -5);
                        return;
                    }
                    Log.e("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay: invalid inputs: callback is null");
                    return;
                }
                if (this.aZ()) break block6;
                new GlobalMembershipCardRegisterResponseData();
                Log.d("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay() calls onFail. this country doesn't support GlobalMembership");
                if (iPayCallback == null) return;
                {
                    iPayCallback.onFail(string, -207);
                    return;
                }
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                return;
            }
        }
        iPayCallback.onFail(string, 999);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void a(List<ExtractGlobalMembershipCardDetailRequest> var1_1, IGlobalMembershipCardExtractDetailCallback var2_2) {
        if (var1_1 == null) ** GOTO lbl4
        try {
            block15 : {
                if (var1_1.size() > 0 && var2_2 != null) break block15;
lbl4: // 2 sources:
                if (var2_2 != null) {
                    Log.e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: invalid inputs: data is null");
                    var2_2.onFail(-5);
                    return;
                }
                Log.e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: invalid inputs: callback is null");
                return;
            }
            if (!this.aZ()) {
                new GlobalMembershipCardRegisterResponseData();
                Log.d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail() calls onFail. this country doesn't support GlobalMembership");
                if (var2_2 == null) return;
                var2_2.onFail(-207);
                return;
            }
            Log.d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail() " + var1_1);
            var5_4 = new String[var1_1.size()];
            var6_5 = new byte[var1_1.size()][];
            var7_6 = null;
            var8_7 = var1_1.iterator();
            var9_8 = 0;
        }
        catch (RemoteException var3_3) {
            Log.c("GlobalMembershipCardProcessor", var3_3.getMessage(), var3_3);
            return;
        }
        while (var8_7.hasNext()) {
            var13_9 = (ExtractGlobalMembershipCardDetailRequest)var8_7.next();
            if (TextUtils.isEmpty((CharSequence)var13_9.getTokenId()) || var13_9.getTzEncData() == null) {
                Log.e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: invalid inputs: data value is null");
                var2_2.onFail(-5);
                return;
            }
            Log.d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: tokenId [" + var13_9.getTokenId() + "] [" + Arrays.toString((byte[])var13_9.getTzEncData()) + "]");
            var14_10 = this.N(var13_9.getTokenId());
            if (var14_10 == null || var14_10.ad() == null) {
                Log.e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: unable to get card Object");
                var2_2.onFail(-1);
                return;
            }
            var5_4[var9_8] = var13_9.getTokenId();
            var6_5[var9_8] = var13_9.getTzEncData();
            ++var9_8;
            var7_6 = var14_10;
        }
        {
            var10_11 = var7_6.ad().extractGlobalMembershipCardDetailTA(var5_4, var6_5);
            if (var10_11 == null || var10_11.size() <= 0) {
                Log.e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: return data is null");
                var2_2.onFail(-1);
            }
            for (GlobalMembershipCardDetail var12_13 : var10_11) {
                if (var12_13.getErrorCode() != 0) {
                    Log.e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: response error [" + var12_13.getErrorCode() + "]");
                    var2_2.onFail(var12_13.getErrorCode());
                    return;
                }
                if (!g.DEBUG) continue;
                Log.d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail response[" + var12_13 + "]");
            }
            var2_2.onSuccess(var10_11);
        }
    }

    protected boolean a(String string, byte[] arrby, byte[] arrby2, byte[] arrby3) {
        Log.d("GlobalMembershipCardProcessor", "saveReceivedCerts: partnerId:" + string);
        if (TextUtils.isEmpty((CharSequence)string) || arrby == null || arrby2 == null || arrby3 == null) {
            Log.e("GlobalMembershipCardProcessor", "saveReceivedCerts: invalid input");
            return false;
        }
        if (this.lh == null) {
            Log.w("GlobalMembershipCardProcessor", "saveReceivedCerts: mServerCertsDb null, cant add server certs to Db");
            return false;
        }
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setContent(h.convertToPem(arrby));
        certificateInfo.setAlias("CA");
        certificateInfo.setUsage("CA");
        CertificateInfo certificateInfo2 = new CertificateInfo();
        certificateInfo2.setContent(h.convertToPem(arrby2));
        certificateInfo2.setAlias("ENC");
        certificateInfo2.setUsage("ENC");
        CertificateInfo certificateInfo3 = new CertificateInfo();
        certificateInfo3.setContent(h.convertToPem(arrby3));
        certificateInfo3.setAlias("VER");
        certificateInfo3.setUsage("VER");
        this.lh.a(string, certificateInfo);
        this.lh.a(string, certificateInfo2);
        this.lh.a(string, certificateInfo3);
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void b(GlobalMembershipCardRegisterRequestData var1_1, IGlobalMembershipCardRegisterCallback var2_2) {
        var3_3 = new GlobalMembershipCardRegisterResponseData();
        if (var1_1 != null && var2_2 != null) ** GOTO lbl10
        if (var2_2 == null) ** GOTO lbl8
        try {
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: invalid inputs: data is null");
            var2_2.onFail(-5, var3_3);
            return;
lbl8: // 1 sources:
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: invalid inputs: callback is null");
            return;
lbl10: // 1 sources:
            if (!this.aZ()) {
                var5_5 = new GlobalMembershipCardRegisterResponseData();
                Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData() calls onFail. this country doesn't support GlobalMembership");
                if (var2_2 == null) return;
                var2_2.onFail(-207, var5_5);
                return;
            }
        }
        catch (RemoteException var4_4) {
            Log.c("GlobalMembershipCardProcessor", var4_4.getMessage(), var4_4);
            return;
        }
        Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData() [" + var1_1 + "]");
        if (var1_1.getGlobalMembershipCardData() == null) {
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: invalid data");
            var2_2.onFail(-5, var3_3);
            return;
        }
        if (TextUtils.isEmpty((CharSequence)var1_1.getTokenId())) {
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: tokenId is empty");
            var2_2.onFail(-5, var3_3);
            return;
        }
        var6_6 = this.O(var1_1.getPartnerId());
        if (var6_6 == null) {
            Log.d("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: serverCerts are null and not stored.");
        } else {
            var1_1.setServerCaCert(var6_6.lj);
            var1_1.setServerEncCert(var6_6.lk);
            var1_1.setServerVerCert(var6_6.ll);
        }
        if ((var7_7 = this.aY()) == null || var7_7.ad() == null) {
            var2_2.onFail(-1, var3_3);
            Log.e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: unable to get card Object");
            return;
        }
        var8_8 = var7_7.ad().getGlobalMembershipCardTzEncDataTA(var1_1);
        if (var8_8.getErrorCode() == 0) {
            this.a(var1_1.getTokenId(), var1_1.getPartnerId(), var1_1.getUserId());
            var2_2.onSuccess(var8_8);
            return;
        }
        var2_2.onFail(var8_8.getErrorCode(), var8_8);
    }

    static class a {
        public byte[] lj = null;
        public byte[] lk = null;
        public byte[] ll = null;

        public a(byte[] arrby, byte[] arrby2, byte[] arrby3) {
            this.lj = arrby;
            this.lk = arrby2;
            this.ll = arrby3;
        }
    }

}

