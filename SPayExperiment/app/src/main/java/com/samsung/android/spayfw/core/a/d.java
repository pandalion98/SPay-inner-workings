/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.RemoteException
 *  android.util.Base64
 *  com.google.gson.JsonObject
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.MessageDigest
 *  java.util.List
 *  java.util.Random
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.RemoteException;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.FactoryResetDetector;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.core.l;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EventReport;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

public class d
extends o {
    private static final String[] CARD_ENTRY_MODES;
    private static final String[] CARD_PRESENTATION_MODES;
    private static final String kN;
    private static final String[] kO;
    protected final IEnrollCardCallback kP;
    protected EnrollCardInfo kQ;
    protected BillingInfo mBillingInfo;

    static {
        kN = TAController.getEfsDirectory() + "/salt";
        CARD_ENTRY_MODES = new String[]{"MAN", "OCR", "APP", "FILE"};
        CARD_PRESENTATION_MODES = new String[]{"NFC", "MST", "ECM", "ALL"};
        kO = new String[]{"app2app", "referenceId", "cof"};
    }

    public d(Context context, EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
        super(context);
        this.kQ = enrollCardInfo;
        this.mBillingInfo = billingInfo;
        this.kP = iEnrollCardCallback;
    }

    /*
     * Loose catch block
     * Enabled aggressive exception aggregation
     */
    private String K(String string) {
        String string2;
        Log.d("CardEnroller", "generateRiskDataHash : " + string);
        MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
        byte[] arrby = string.getBytes("UTF-8");
        messageDigest.update(arrby);
        byte[] arrby2 = messageDigest.digest();
        String string3 = h.encodeHex(arrby);
        String string4 = h.encodeHex(arrby2) + "00000001";
        byte[] arrby3 = this.getSalt();
        String string5 = null;
        for (int i2 = 2000; i2 > 0; --i2) {
            string4 = h.n(string3, string4);
            h.xor(arrby3, h.decodeHex(string4));
            string5 = h.encodeHex(arrby3);
        }
        try {
            string2 = new String(Base64.encode((byte[])h.decodeHex(string5), (int)2));
        }
        catch (Exception exception) {
            return string5;
        }
        Log.d("CardEnroller", "generateRiskDataHash : " + string2);
        return string2;
        catch (Exception exception) {
            return null;
        }
    }

    static /* synthetic */ void a(d d2) {
        d2.clearSensitiveData();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(EnrollmentRequestData enrollmentRequestData) {
        String string;
        String string2;
        String string3;
        String string4;
        block13 : {
            String string5;
            block12 : {
                try {
                    if (this.mBillingInfo != null && this.mBillingInfo.getZip() != null && !this.mBillingInfo.getZip().isEmpty()) {
                        String string6;
                        Log.d("CardEnroller", "ZipCode : " + this.mBillingInfo.getZip());
                        string5 = string6 = this.K(this.mBillingInfo.getZip());
                        break block12;
                    }
                    string5 = null;
                }
                catch (Exception exception) {
                    Log.c("CardEnroller", exception.getMessage(), exception);
                    string = null;
                }
            }
            string = string5;
            try {
                String[] arrstring;
                EnrollCardPanInfo enrollCardPanInfo;
                if (this.kQ instanceof EnrollCardPanInfo && (enrollCardPanInfo = (EnrollCardPanInfo)this.kQ).getName() != null && !enrollCardPanInfo.getName().isEmpty() && (arrstring = enrollCardPanInfo.getName().split(" ")) != null) {
                    String string7;
                    Log.d("CardEnroller", "Last Name : " + arrstring[-1 + arrstring.length]);
                    string4 = string7 = this.K(arrstring[-1 + arrstring.length]);
                    break block13;
                }
                string4 = null;
            }
            catch (Exception exception) {
                Log.c("CardEnroller", exception.getMessage(), exception);
                string2 = null;
            }
        }
        string2 = string4;
        try {
            boolean bl = this.kQ instanceof EnrollCardPanInfo;
            string3 = null;
            if (bl) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo)this.kQ;
                String string8 = enrollCardPanInfo.getPAN();
                string3 = null;
                if (string8 != null) {
                    boolean bl2 = enrollCardPanInfo.getPAN().isEmpty();
                    string3 = null;
                    if (!bl2) {
                        String string9 = enrollCardPanInfo.getPAN();
                        String string10 = string9.substring(-4 + string9.length());
                        Log.d("CardEnroller", "Last 4 : " + string10);
                        string3 = null;
                        if (string10 != null) {
                            String string11;
                            string3 = string11 = this.K(string10);
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            Log.c("CardEnroller", exception.getMessage(), exception);
            string3 = null;
        }
        enrollmentRequestData.getCard().setRiskData(string, string2, string3);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean a(EnrollCardInfo enrollCardInfo, IEnrollCardCallback iEnrollCardCallback) {
        if (enrollCardInfo == null || iEnrollCardCallback == null) {
            Log.e("CardEnroller", "validateEnrollCardRequest: input is null !");
            return false;
        } else {
            if (enrollCardInfo.getApplicationId() != null && enrollCardInfo.getUserId() != null && enrollCardInfo.getGcmId() != null && enrollCardInfo.getSppId() != null && enrollCardInfo.getWalletId() != null && enrollCardInfo.getUserEmail() != null) {
                if (!d.isValidMode(CARD_ENTRY_MODES, enrollCardInfo.getCardEntryMode())) {
                    Log.e("CardEnroller", "validateEnrollCardRequest: entry mode incorrect! = " + enrollCardInfo.getCardEntryMode());
                    return false;
                }
                if (!d.isValidMode(CARD_PRESENTATION_MODES, enrollCardInfo.getCardPresentationMode())) {
                    Log.e("CardEnroller", "validateEnrollCardRequest: presentation mode incorrect! = " + enrollCardInfo.getCardPresentationMode());
                    return false;
                }
                if (enrollCardInfo instanceof EnrollCardPanInfo) {
                    return this.a((EnrollCardPanInfo)enrollCardInfo);
                }
                if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
                    return this.a((EnrollCardReferenceInfo)enrollCardInfo);
                }
                if (enrollCardInfo instanceof EnrollCardLoyaltyInfo) {
                    return this.a((EnrollCardLoyaltyInfo)enrollCardInfo);
                }
                Log.e("CardEnroller", "validateEnrollCardRequest: UNKNOWN ENROLL CARD CLASS");
                return false;
            }
            if (this.mBillingInfo == null || this.mBillingInfo.getZip() == null || this.mBillingInfo.getZip().length() <= 16) return false;
            {
                Log.e("CardEnroller", "validateEnrollCardRequest: zipcode invalid! too long = " + this.mBillingInfo.getZip());
                return false;
            }
        }
    }

    private boolean a(EnrollCardLoyaltyInfo enrollCardLoyaltyInfo) {
        if (enrollCardLoyaltyInfo.getLoyaltyInfo() == null || enrollCardLoyaltyInfo.getLoyaltyInfo().isEmpty()) {
            Log.e("CardEnroller", "Loyalty cardInfo is null or empty");
            return false;
        }
        return true;
    }

    private boolean a(EnrollCardPanInfo enrollCardPanInfo) {
        if (enrollCardPanInfo.getCVV() == null || enrollCardPanInfo.getCVV().isEmpty()) {
            Log.e("CardEnroller", "CVV is null or empty");
            return false;
        }
        if (enrollCardPanInfo.getPAN() == null || enrollCardPanInfo.getPAN().isEmpty()) {
            Log.e("CardEnroller", "PAN is null or empty");
            return false;
        }
        if (enrollCardPanInfo.getName() == null || enrollCardPanInfo.getName().isEmpty()) {
            Log.e("CardEnroller", "Name is null or empty");
            return false;
        }
        return true;
    }

    private boolean a(EnrollCardReferenceInfo enrollCardReferenceInfo) {
        if (!d.isValidMode(kO, enrollCardReferenceInfo.getReferenceType())) {
            Log.e("CardEnroller", "validateEnrollCardRequest: card Reference type incorrect! = " + enrollCardReferenceInfo.getReferenceType());
            return false;
        }
        if (enrollCardReferenceInfo.getExtraEnrollData() == null) {
            Log.e("CardEnroller", "Extra Enroll Data is null or empty");
            return false;
        }
        return true;
    }

    private String aV() {
        EnrollCardReferenceInfo enrollCardReferenceInfo;
        if (this.kQ instanceof EnrollCardReferenceInfo && (enrollCardReferenceInfo = (EnrollCardReferenceInfo)this.kQ).getExtraEnrollData() != null) {
            return enrollCardReferenceInfo.getExtraEnrollData().getString("cardReferenceId");
        }
        return null;
    }

    private void clearSensitiveData() {
        if (this.kQ != null) {
            this.kQ.decrementRefCount();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private String g(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty() || !h.fP().equals((Object)"BR")) {
            return null;
        }
        if (string2.equals((Object)"VI")) {
            return "000";
        }
        Random random = new Random(System.currentTimeMillis());
        do {
            int n2;
            String string3;
            if (!(string3 = (n2 = random.nextInt(1000)) / 10 == 0 ? "00" + Integer.toString((int)n2) : (n2 / 100 == 0 ? "0" + Integer.toString((int)n2) : Integer.toString((int)n2))).equals((Object)string) && !string3.equals((Object)"000")) {
                Log.i("CardEnroller", "Code Generated");
                return string3;
            }
            Log.w("CardEnroller", "Code equals Avoid Code or Invalid Code. Retry.");
        } while (true);
    }

    /*
     * Exception decompiling
     */
    private final byte[] getSalt() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private static boolean isValidMode(String[] arrstring, String string) {
        int n2 = arrstring.length;
        int n3 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    bl = false;
                    if (n3 >= n2) break block3;
                    if (!arrstring[n3].equals((Object)string)) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n3;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void process() {
        com.samsung.android.spayfw.payprovider.c c2;
        EnrollCardPanInfo enrollCardPanInfo;
        String string;
        c c3;
        if (!this.a(this.kQ, this.kP)) {
            if (this.kP != null) {
                this.kP.onFail(-5, null);
            }
            this.clearSensitiveData();
            Log.e("CardEnroller", "validateEnrollCardRequest failed");
            return;
        }
        Log.d("CardEnroller", this.kQ.toString());
        if (this.iJ == null) {
            this.iJ = com.samsung.android.spayfw.core.a.a(this.mContext, this.kQ.getUserId());
        }
        if (this.iJ == null) {
            Log.e("CardEnroller", "unable to create account");
            this.kP.onFail(-1, null);
            this.clearSensitiveData();
            return;
        }
        if (!this.iJ.p(this.kQ.getUserId())) {
            Log.e("CardEnroller", "account ids are not same ");
            this.kP.onFail(-5, null);
            this.clearSensitiveData();
            return;
        }
        e.h(this.mContext).setConfig("CONFIG_USER_ID", this.kQ.getUserId());
        try {
            c c4;
            c3 = c4 = this.iJ.a(this.kQ);
        }
        catch (TAException tAException) {
            Log.c("CardEnroller", tAException.getMessage(), (Throwable)((Object)tAException));
            if (tAException.getErrorCode() == 2) {
                this.kP.onFail(-42, null);
            } else {
                this.kP.onFail(-41, null);
            }
            this.clearSensitiveData();
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c3 = null;
        }
        if (c3 == null) {
            Log.e("CardEnroller", "unable to add new card");
            this.kP.onFail(-10, null);
            this.clearSensitiveData();
            return;
        }
        if (this.lh != null) {
            Log.i("CardEnroller", "Server Certs DB not null");
            Log.d("CardEnroller", "Card Brand = " + c3.getCardBrand());
            List<CertificateInfo> list = this.lh.a(ServerCertsStorage.ServerCertsDb.ServerCertsColumn.Cg, c3.getCardBrand());
            if (list != null && !list.isEmpty()) {
                Log.i("CardEnroller", "Certificates exist for : " + c3.getCardBrand());
                c3.ad().setServerCertificates((CertificateInfo[])list.toArray((Object[])new CertificateInfo[list.size()]));
            } else {
                Log.i("CardEnroller", "No certs stored for current card");
            }
        }
        String string2 = String.valueOf((long)System.currentTimeMillis());
        c3.setEnrollmentId(string2);
        c3.ad().setPaymentFrameworkRequester(new PaymentFrameworkApp.a());
        com.samsung.android.spayfw.payprovider.a a2 = c3.ad().getCasdParameters();
        CertificateInfo[] arrcertificateInfo = c3.ad().getDeviceCertificatesTA();
        if (arrcertificateInfo == null || arrcertificateInfo.length <= 0) {
            Log.w("CardEnroller", "getCerts returns null");
        }
        if ((string = this.P(c3.getCardBrand())) != null && !string.isEmpty()) {
            c2 = c3.ad().getEnrollmentRequestDataTA(this.kQ, this.mBillingInfo);
            JsonObject jsonObject = c2 != null && c2.getErrorCode() == 0 ? c2.ch() : null;
            if (jsonObject == null && !(this.kQ instanceof EnrollCardReferenceInfo)) {
                Log.e("CardEnroller", "provider data is null");
                try {
                    this.kP.onFail(-1, null);
                    this.clearSensitiveData();
                }
                catch (RemoteException remoteException) {
                    Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                }
                this.iJ.s(string2);
                return;
            }
        } else {
            c2 = null;
        }
        DeviceInfo deviceInfo = DeviceInfo.getDefaultDeviceInfo(this.mContext);
        String string3 = this.kQ.getSppId();
        String string4 = this.kQ.getGcmId();
        Log.d("CardEnroller", "spp id= " + string3);
        Log.d("CardEnroller", "gcm id= " + string4);
        if (string3 != null && !string3.isEmpty()) {
            deviceInfo.setSppId(string3);
        } else {
            deviceInfo.setSppId("89235dce-9e25-11e4-89d3-123b93f75cba");
        }
        if (string4 != null && !string4.isEmpty()) {
            deviceInfo.setGcmId(string4);
        }
        EnrollmentRequestData enrollmentRequestData = l.a(this.kQ, arrcertificateInfo, deviceInfo, c2, c3, this.aV());
        com.samsung.android.spayfw.fraud.a a3 = com.samsung.android.spayfw.fraud.a.x(this.mContext);
        if (a3 != null) {
            a3.a(this.kQ, this.mBillingInfo);
        } else {
            Log.d("CardEnroller", "Collector: buildFCardRecord cannot get data");
        }
        this.a(enrollmentRequestData);
        if (this.kQ instanceof EnrollCardPanInfo && (enrollCardPanInfo = (EnrollCardPanInfo)this.kQ).getCVV() != null) {
            c3.setSecurityCode(this.g(enrollCardPanInfo.getCVV(), c3.getCardBrand()));
        }
        com.samsung.android.spayfw.remoteservice.tokenrequester.c c5 = this.lQ.a(c.y(c3.getCardBrand()), enrollmentRequestData);
        String string5 = e.h(this.mContext).getConfig("CONFIG_WALLET_ID");
        Log.d("CardEnroller", "Current Wallet ID = " + string5);
        String string6 = this.kQ.getWalletId();
        if (string5 == null) {
            c5.bl(string6);
        } else if (!string5.equals((Object)string6)) {
            Log.e("CardEnroller", "Current Wallet ID not match");
            this.kP.onFail(-208, null);
            return;
        }
        c5.bf(string);
        if (a2 != null) {
            c5.s(a2.cb(), a2.ca());
        }
        c5.bk(this.iJ.u(c3.getCardBrand()));
        c5.a(new a(string2, this.kQ, this.mBillingInfo, this.kP));
        Log.i("CardEnroller", "enrollCard request successfully sent");
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<EnrollmentResponseData>, com.samsung.android.spayfw.remoteservice.tokenrequester.c> {
        IEnrollCardCallback kP;
        EnrollCardInfo kQ;
        String kR;
        BillingInfo mBillingInfo;

        public a(String string, EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
            this.kP = iEnrollCardCallback;
            this.kQ = enrollCardInfo;
            this.mBillingInfo = billingInfo;
            this.kR = string;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<EnrollmentResponseData> var2_2) {
            block26 : {
                block27 : {
                    block28 : {
                        var3_3 = -3;
                        var4_4 = -11;
                        Log.d("CardEnroller", "EnrollCallback:onRequestComplete: code: " + var1_1);
                        switch (var1_1) {
                            default: {
                                var3_3 = -1;
                                var5_5 = null;
                                break block26;
                            }
                            case 201: {
                                if (var2_2 == null || var2_2.getResult() == null || var2_2.getResult().getId() == null) {
                                    Log.e("CardEnroller", "EnrollCallback:onRequestComplete: invalid response from server");
                                    var3_3 = -204;
                                    var5_5 = null;
                                } else {
                                    var21_8 = var2_2.getResult().getId();
                                    if (d.this.iJ.q(this.kR) == null) {
                                        Log.e("CardEnroller", "EnrollCallback:onRequestComplete: unable to find the card ");
                                        var3_3 = -1;
                                        var5_5 = null;
                                    } else {
                                        d.this.iJ.q(this.kR).setEnrollmentId(var21_8);
                                        var22_9 = d.this.iJ.q(var21_8);
                                        if (var22_9 == null) {
                                            Log.e("CardEnroller", "EnrollCallback:onRequestComplete: unable to find the card ");
                                            var3_3 = -1;
                                            var5_5 = null;
                                        } else {
                                            var23_10 = new com.samsung.android.spayfw.storage.models.a(var21_8);
                                            var23_10.setUserId(com.samsung.android.spayfw.core.a.a(d.this.mContext, null).getAccountId());
                                            var23_10.j(var22_9.ab());
                                            var23_10.setCardBrand(var22_9.getCardBrand());
                                            var23_10.setTokenStatus("ENROLLED");
                                            d.this.jJ.c(var23_10);
                                            var22_9.ad().setEnrollmentId(var21_8);
                                            FactoryResetDetector.ai();
                                            var25_11 = new q();
                                            var25_11.setTokenStatus(var23_10.getTokenStatus());
                                            var22_9.a(var25_11);
                                            var5_5 = m.a(d.this.mContext, var2_2.getResult());
                                            var3_3 = 0;
                                        }
                                    }
                                }
                                break block26;
                            }
                            case 204: {
                                var3_3 = -207;
                                var5_5 = null;
                                break block26;
                            }
                            case 409: {
                                if (var2_2 != null && var2_2.getResult() != null && var2_2.getResult().getId() != null) ** GOTO lbl52
                                Log.e("CardEnroller", "EnrollCallback:onRequestComplete: invalid response from server");
                                var3_3 = -204;
                                var5_5 = null;
                                break block26;
lbl52: // 1 sources:
                                var15_12 = d.this.iJ.q(this.kR);
                                if (var15_12 != null) ** GOTO lbl58
                                Log.e("CardEnroller", "EnrollCallback:unable to find the card in PF");
                                var3_3 = -1;
                                var5_5 = null;
                                break block26;
lbl58: // 1 sources:
                                var16_13 = var2_2.getResult().getId();
                                var17_14 = m.a(d.this.mContext, var2_2.getResult());
                                if (d.this.jJ.bp(var16_13) != null) break block27;
                                var15_12.setEnrollmentId(var16_13);
                                var18_15 = new com.samsung.android.spayfw.storage.models.a(var16_13);
                                var18_15.setUserId(com.samsung.android.spayfw.core.a.a(d.this.mContext, null).getAccountId());
                                var18_15.j(var15_12.ab());
                                var18_15.setCardBrand(var15_12.getCardBrand());
                                var18_15.setTokenStatus("ENROLLED");
                                d.this.jJ.c(var18_15);
                                var15_12.ad().setEnrollmentId(var16_13);
                                var20_16 = new q();
                                var20_16.setTokenStatus(var18_15.getTokenStatus());
                                var15_12.a(var20_16);
                                Log.d("CardEnroller", "EnrollCallback:onRequestComplete: add a new token record " + var18_15.dump());
                                var5_5 = var17_14;
                                var3_3 = 0;
                                break block26;
                            }
                            case 400: {
                                var12_17 = var2_2.fa();
                                if (var12_17 != null && var12_17.getCode() != null) {
                                    var13_18 = var12_17.getCode();
                                    if ("400.5".equals((Object)var13_18)) break;
                                    if ("400.4".equals((Object)var13_18)) {
                                        d.this.iJ.s(this.kR);
                                        var4_4 = var3_3;
                                        break;
                                    }
                                    if ("400.3".equals((Object)var13_18)) break;
                                    var4_4 = "403.4".equals((Object)var13_18) != false ? -13 : ("403.8".equals((Object)var13_18) != false ? -209 : -1);
                                }
                                break block28;
                            }
                            case 500: {
                                var3_3 = -205;
                                var5_5 = null;
                                break block26;
                            }
                            case 0: 
                            case 503: {
                                var3_3 = -201;
                                var5_5 = null;
                                break block26;
                            }
                            case -2: {
                                var3_3 = -206;
                                var5_5 = null;
                                break block26;
                            }
                        }
                        var3_3 = var4_4;
                        var5_5 = null;
                        break block26;
                    }
                    var3_3 = -1;
                    var5_5 = null;
                    break block26;
                }
                var5_5 = var17_14;
            }
            if (var5_5 != null && var5_5.getEnrollmentId() != null && (var5_5.getTnC() == null || var5_5.getTnC().isEmpty()) && (var10_6 = d.this.jJ.bp(var5_5.getEnrollmentId())) != null) {
                var10_6.b(System.currentTimeMillis());
                d.this.jJ.d(var10_6);
            }
            if (var3_3 != 0) ** GOTO lbl122
            try {
                block29 : {
                    Log.i("CardEnroller", "EnrollCallback:onRequestComplete: invoking app callback");
                    if (e.h(d.this.mContext).getConfig("CONFIG_WALLET_ID") == null) {
                        var9_7 = e.h(d.this.mContext).setConfig("CONFIG_WALLET_ID", this.kQ.getWalletId());
                        Log.i("CardEnroller", "Wallet ID Set: " + var9_7);
                    }
                    this.kP.onSuccess(var5_5);
                    break block29;
lbl122: // 2 sources:
                    Log.e("CardEnroller", "EnrollCard Failed - Error Code = " + var3_3 + "code: " + var1_1);
                    this.kP.onFail(var3_3, var5_5);
                    d.this.iJ.s(this.kR);
                }
                if (var2_2 == null) ** GOTO lbl134
            }
            catch (Exception var7_19) {
                block31 : {
                    block30 : {
                        Log.c("CardEnroller", var7_19.getMessage(), var7_19);
                        if (var2_2 == null) break block30;
                        m.a(var2_2.getResult(), var5_5);
                        break block31;
lbl134: // 1 sources:
                        m.a(null, var5_5);
lbl135: // 2 sources:
                        d.a(d.this);
                        return;
                    }
                    m.a(null, var5_5);
                }
                d.a(d.this);
                return;
            }
            catch (Throwable var6_20) {
                if (var2_2 != null) {
                    m.a(var2_2.getResult(), var5_5);
                } else {
                    m.a(null, var5_5);
                }
                d.a(d.this);
                throw var6_20;
            }
            m.a(var2_2.getResult(), var5_5);
            ** GOTO lbl135
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void a(int n2, ServerCertificates serverCertificates, com.samsung.android.spayfw.remoteservice.tokenrequester.c c2) {
            Log.d("CardEnroller", "onCertsReceived: called for EnrollCallback");
            c c3 = d.this.iJ.q(this.kR);
            if (c3 == null) {
                Log.e("CardEnroller", "unable to find card object");
                try {
                    this.kP.onFail(-1, null);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            if (!d.this.a(this.kR, c3, serverCertificates)) {
                Log.e("CardEnroller", "Server certificate update failed. Enrollment aborted");
                try {
                    this.kP.onFail(-1, null);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            com.samsung.android.spayfw.payprovider.c c4 = c3.ad().getEnrollmentRequestDataTA(this.kQ, this.mBillingInfo);
            JsonObject jsonObject = null;
            if (c4 != null) {
                int n3 = c4.getErrorCode();
                jsonObject = null;
                if (n3 == 0) {
                    jsonObject = c4.ch();
                }
            }
            if (jsonObject == null && !(this.kQ instanceof EnrollCardReferenceInfo)) {
                Log.w("CardEnroller", "provider data is null");
                try {
                    this.kP.onFail(-1, null);
                }
                catch (RemoteException remoteException) {
                    Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                }
                d.this.iJ.s(this.kR);
                return;
            }
            l.a(c4, (EnrollmentRequestData)c2.eT(), d.this.aV());
            c2.bf(d.this.P(c3.getCardBrand()));
            c2.a(this);
            Log.i("CardEnroller", "enrollCard request successfully sent after server cert update");
        }

        @Override
        public boolean a(int n2, String string) {
            Log.d("CardEnroller", "onCasdUpdate : called for EnrollCallback");
            if (string == null || string.isEmpty()) {
                Log.e("CardEnroller", "CASD certificate is null. Enrollment aborted");
                try {
                    this.kP.onFail(-43, null);
                    return false;
                }
                catch (RemoteException remoteException) {
                    Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                    return false;
                }
            }
            c c2 = d.this.iJ.q(this.kR);
            if (c2 == null || c2.ad() == null) {
                Log.e("CardEnroller", "no card, CASD aborted");
                try {
                    this.kP.onFail(-43, null);
                    return false;
                }
                catch (RemoteException remoteException) {
                    Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                    return false;
                }
            }
            Log.d("CardEnroller", "onCasdUpdate : " + c2.getCardBrand());
            boolean bl = c2.ad().setCasdCertificate(string);
            Log.d("CardEnroller", "onCasdUpdate : " + bl);
            if (bl) {
                EventReport eventReport = new EventReport();
                eventReport.setCategory("Security");
                eventReport.setCode("EVENT-21000");
                eventReport.setSource("Payment Framework");
                eventReport.setDescription("CASD certificate provisioned");
                d.this.lQ.a(c.y(c2.getCardBrand()), eventReport).fe();
                return true;
            }
            ErrorReport errorReport = new ErrorReport();
            errorReport.setSeverity("ERROR");
            errorReport.setCode("ERROR-30000");
            errorReport.setDescription("CASD certificate provision failure");
            d.this.lQ.a(c.y(c2.getCardBrand()), errorReport).fe();
            Log.e("CardEnroller", "CASD certificate update faild. Enrollment aborted");
            try {
                this.kP.onFail(-43, null);
                return false;
            }
            catch (RemoteException remoteException) {
                Log.c("CardEnroller", remoteException.getMessage(), remoteException);
                return false;
            }
        }
    }

}

