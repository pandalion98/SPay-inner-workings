/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  java.io.BufferedWriter
 *  java.io.File
 *  java.io.FileWriter
 *  java.io.IOException
 *  java.io.Writer
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Random
 */
package com.samsung.android.spayfw.payprovider.c;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EncryptedImage;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAException;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class a
extends PaymentNetworkProvider {
    private static PlccTAController zg;
    private final Gson mGson = new GsonBuilder().disableHtmlEscaping().create();
    private byte[] zD = null;

    public a(Context context, String string, f f2) {
        super(context, string);
        this.mContext = context;
        this.mTAController = PlccTAController.createOnlyInstance(this.mContext);
        zg = (PlccTAController)this.mTAController;
    }

    private ParcelFileDescriptor aS(String string) {
        File file = new File(string);
        try {
            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open((File)file, (int)805306368);
            return parcelFileDescriptor;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private JsonObject b(EnrollCardLoyaltyInfo enrollCardLoyaltyInfo) {
        String string;
        Log.d("LoyaltyCardPayProvider", "Entered buildJsonFromCard");
        String string2 = enrollCardLoyaltyInfo.getLoyaltyInfo();
        List<EncryptedImage> list = enrollCardLoyaltyInfo.getEncryptedImages();
        Log.d("LoyaltyCardPayProvider", "loyaltyCardInfo = " + string2);
        JsonObject jsonObject = new JsonObject();
        try {
            String string3;
            byte[] arrby = string2.getBytes();
            long l2 = h.am(this.mContext);
            Log.d("LoyaltyCardPayProvider", "Network Time = " + l2);
            PlccTAController plccTAController = zg;
            string = string3 = Base64.encodeToString((byte[])plccTAController.utility_enc4Server_Transport("LOYALTY", arrby, l2), (int)2);
        }
        catch (Exception exception) {
            Log.c("LoyaltyCardPayProvider", exception.getMessage(), exception);
            string = null;
        }
        if (string == null || string.equals((Object)"")) {
            Log.d("LoyaltyCardPayProvider", "encrypted data is either null or empty = " + string);
            return null;
        }
        Log.d("LoyaltyCardPayProvider", "encrypted data  = " + string);
        jsonObject.addProperty("encryptedData", string);
        if (list == null) {
            Log.d("LoyaltyCardPayProvider", "Encrypted images null");
            return jsonObject;
        }
        JsonArray jsonArray = new JsonArray();
        Iterator iterator = list.iterator();
        do {
            if (!iterator.hasNext()) {
                jsonObject.add("encImages", (JsonElement)jsonArray);
                return jsonObject;
            }
            EncryptedImage encryptedImage = (EncryptedImage)iterator.next();
            Log.d("LoyaltyCardPayProvider", "encrypted image = " + encryptedImage);
            if (encryptedImage == null) continue;
            JsonObject jsonObject2 = new JsonObject();
            Log.d("LoyaltyCardPayProvider", "encrypted image: Usage = " + encryptedImage.getUsage());
            jsonObject2.addProperty("usage", encryptedImage.getUsage());
            Log.d("LoyaltyCardPayProvider", "encrypted image: Content = " + encryptedImage.getContent());
            jsonObject2.addProperty("content", encryptedImage.getContent());
            jsonArray.add((JsonElement)jsonObject2);
        } while (true);
    }

    private File eC() {
        File file = this.aU("LoyaltyBundle");
        int n2 = new Random().nextInt();
        return new File(file, "serverResponse_" + System.currentTimeMillis() + String.valueOf((int)n2));
    }

    private String getEmailAddressHash(String string) {
        try {
            Log.d("LoyaltyCardPayProvider", "Entered getEmailAddressHash");
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            messageDigest.update(string.getBytes());
            byte[] arrby = messageDigest.digest();
            Log.d("LoyaltyCardPayProvider", "getEmailAddressHash: digest " + Arrays.toString((byte[])arrby));
            String string2 = Base64.encodeToString((byte[])arrby, (int)11);
            Log.d("LoyaltyCardPayProvider", "getEmailAddressHash: emailAddress " + string + " Hash: " + string2);
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.c("LoyaltyCardPayProvider", noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
            return null;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected String aT(String var1_1) {
        Log.d("LoyaltyCardPayProvider", "writeStringToFile");
        var2_2 = this.eC();
        Log.d("LoyaltyCardPayProvider", "File = " + (Object)var2_2);
        var3_3 = new BufferedWriter((Writer)new FileWriter(var2_2));
        var3_3.write(var1_1);
        var7_5 = var11_4 = var2_2.getAbsolutePath();
        if (var3_3 == null) return var7_5;
        if (!false) ** GOTO lbl18
        try {
            try {
                var3_3.close();
                return var7_5;
            }
            catch (Throwable var13_6) {
                null.addSuppressed(var13_6);
                return var7_5;
            }
lbl18: // 1 sources:
            var3_3.close();
            return var7_5;
        }
        catch (Exception var12_7) {}
        ** GOTO lbl-1000
        catch (Throwable var9_8) {
            try {
                throw var9_8;
            }
            catch (Throwable var10_9) {
                block15 : {
                    var5_10 = var9_8;
                    var4_11 = var10_9;
                    break block15;
                    catch (Throwable var4_12) {
                        var5_10 = null;
                    }
                }
                if (var3_3 == null) throw var4_11;
                if (var5_10 == null) ** GOTO lbl41
                try {
                    try {
                        var3_3.close();
                    }
                    catch (Throwable var8_13) {
                        var5_10.addSuppressed(var8_13);
                        throw var4_11;
                    }
                    throw var4_11;
lbl41: // 1 sources:
                    var3_3.close();
                    throw var4_11;
                }
                catch (Exception var6_14) {
                    var7_5 = null;
                }
            }
        }
lbl-1000: // 2 sources:
        {
            Log.e("LoyaltyCardPayProvider", "Exception when writing string to file");
            return var7_5;
        }
    }

    public File aU(String string) {
        return this.mContext.getDir(string, 0);
    }

    @Override
    protected boolean authenticateTransaction(SecuredObject securedObject) {
        Log.d("LoyaltyCardPayProvider", "Entered authenticateTransaction");
        if (zg == null) {
            Log.e("LoyaltyCardPayProvider", "TAController is null");
            return false;
        }
        try {
            Log.d("LoyaltyCardPayProvider", "Calling Plcc TA Controller Authenticate Transaction");
            boolean bl = zg.authenticateTransaction(securedObject.getSecureObjectData());
            return bl;
        }
        catch (PlccTAException plccTAException) {
            Log.c("LoyaltyCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return false;
        }
    }

    @Override
    protected void clearCard() {
        Log.d("LoyaltyCardPayProvider", "Entered clearCard");
        this.zD = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected e createToken(String string, c c2, int n2) {
        e e2;
        block8 : {
            Bundle bundle;
            block10 : {
                block9 : {
                    Log.d("LoyaltyCardPayProvider", "Entered createToken()");
                    JsonObject jsonObject = c2.ch();
                    e2 = new e();
                    e2.setErrorCode(-7);
                    try {
                        e2.setProviderTokenKey(new f(string));
                        if (jsonObject == null) break block8;
                        Log.d("LoyaltyCardPayProvider", "Response Data = " + jsonObject.toString());
                        JsonElement jsonElement = jsonObject.get("encrypted");
                        if (jsonElement == null) break block8;
                        String string2 = jsonElement.getAsString();
                        Log.d("LoyaltyCardPayProvider", "ac = " + string2.toString());
                        byte[] arrby = Base64.decode((String)string2, (int)0);
                        PlccTAController plccTAController = zg;
                        byte[] arrby2 = plccTAController.addCard("LOYALTY", arrby);
                        String string3 = Base64.encodeToString((byte[])arrby2, (int)2);
                        Log.d("LoyaltyCardPayProvider", "tzEnc = " + string3.toString());
                        jsonObject.addProperty("encrypted", string3);
                        String string4 = this.mGson.toJson((JsonElement)jsonObject);
                        Log.d("LoyaltyCardPayProvider", "Response Data String = " + string4);
                        bundle = new Bundle();
                        String string5 = this.aT(string4);
                        bundle.putString("loyaltyResponseDataFilePath", string5);
                        bundle.putParcelable("loyaltyResponseDataFd", (Parcelable)this.aS(string5));
                        if (c2.cg() == null || !c2.cg().containsKey("cardRefId")) break block9;
                        String string6 = c2.cg().getString("cardRefId");
                        ExtractCardDetailResult extractCardDetailResult = zg.extractLoyaltyCardDetail(string6.getBytes(), arrby2);
                        if (extractCardDetailResult != null) {
                            if (extractCardDetailResult.getExtraContent() != null && !extractCardDetailResult.getExtraContent().isEmpty()) {
                                bundle.putString("acTokenExtra", extractCardDetailResult.getExtraContent());
                                Log.d("LoyaltyCardPayProvider", "added extra as part of Provision Response:" + extractCardDetailResult.getExtraContent());
                            } else if (extractCardDetailResult.getImgSessionKey() != null && !extractCardDetailResult.getImgSessionKey().isEmpty()) {
                                bundle.putString("imgSessionKey", extractCardDetailResult.getImgSessionKey());
                                Log.d("LoyaltyCardPayProvider", "added ImgSessionKey as part of Provision Response:" + extractCardDetailResult.getImgSessionKey());
                            }
                        } else {
                            Log.i("LoyaltyCardPayProvider", " ImgSessionKey not added part of Provision Response");
                        }
                        break block10;
                    }
                    catch (Exception exception) {
                        Log.c("LoyaltyCardPayProvider", exception.getMessage(), exception);
                        e2.setErrorCode(-2);
                        return e2;
                    }
                }
                Log.i("LoyaltyCardPayProvider", " CardRefId not part of request");
            }
            e2.e(bundle);
            e2.setErrorCode(0);
            return e2;
        }
        return e2;
    }

    @Override
    public void delete() {
    }

    @Override
    protected ExtractCardDetailResult extractLoyaltyCardDetail(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest) {
        Log.d("LoyaltyCardPayProvider", "Entered extractLoyaltyCardDetail");
        if (zg == null || extractLoyaltyCardDetailRequest == null) {
            Log.e("LoyaltyCardPayProvider", "Error: mPlccTAC = " + zg + "request = " + extractLoyaltyCardDetailRequest);
            return null;
        }
        try {
            ExtractCardDetailResult extractCardDetailResult = zg.extractLoyaltyCardDetail(extractLoyaltyCardDetailRequest.getCardRefID(), extractLoyaltyCardDetailRequest.getTzEncData());
            return extractCardDetailResult;
        }
        catch (InterruptedException interruptedException) {
            Log.c("LoyaltyCardPayProvider", interruptedException.getMessage(), interruptedException);
            return null;
        }
        catch (PlccTAException plccTAException) {
            Log.c("LoyaltyCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return null;
        }
    }

    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }

    @Override
    protected c getDeleteRequestData(Bundle bundle) {
        c c2 = new c();
        c2.setErrorCode(0);
        if (bundle != null && bundle.getString("deleteCardData") != null) {
            String string = bundle.getString("deleteCardData");
            c2.a((JsonObject)new Gson().fromJson(string, JsonObject.class));
            Log.d("LoyaltyCardPayProvider", "getDeleteRequestData: " + string);
        }
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected CertificateInfo[] getDeviceCertificates() {
        CertificateInfo[] arrcertificateInfo = new CertificateInfo[3];
        Log.d("LoyaltyCardPayProvider", "getDeviceCertificates");
        try {
            PlccTAController plccTAController = zg;
            PlccTAController.TACerts tACerts = plccTAController.getAllCerts("LOYALTY");
            if (tACerts == null || tACerts.encryptcert == null || tACerts.encryptcert.length <= 0 || tACerts.drk == null || tACerts.drk.length <= 0 || tACerts.signcert == null || tACerts.signcert.length <= 0) {
                Log.e("LoyaltyCardPayProvider", "getAllCerts failed for Loyalty card");
                return null;
            }
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"cert_enc", (Object)tACerts.encryptcert);
            hashMap.put((Object)"cert_sign", (Object)tACerts.signcert);
            hashMap.put((Object)"cert_ca", (Object)tACerts.drk);
            String string = Util.convertToPem((byte[])hashMap.get((Object)"cert_enc"));
            String string2 = Util.convertToPem((byte[])hashMap.get((Object)"cert_sign"));
            String string3 = Util.convertToPem((byte[])hashMap.get((Object)"cert_ca"));
            if (string != null && string2 != null && string3 != null) {
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setAlias("ENC");
                certificateInfo.setContent(string);
                certificateInfo.setUsage("ENC");
                arrcertificateInfo[0] = certificateInfo;
                CertificateInfo certificateInfo2 = new CertificateInfo();
                certificateInfo2.setAlias("SIG");
                certificateInfo2.setContent(string2);
                certificateInfo2.setUsage("SIG");
                arrcertificateInfo[1] = certificateInfo2;
                CertificateInfo certificateInfo3 = new CertificateInfo();
                certificateInfo3.setAlias("CA");
                certificateInfo3.setContent(string3);
                certificateInfo3.setUsage("CA");
                arrcertificateInfo[2] = certificateInfo3;
                Log.d("LoyaltyCardPayProvider", "getDeviceCertificates: success");
                return arrcertificateInfo;
            }
            Log.e("LoyaltyCardPayProvider", "getDeviceCertificates failed");
            return null;
        }
        catch (Exception exception) {
            Log.c("LoyaltyCardPayProvider", exception.getMessage(), exception);
            return arrcertificateInfo;
        }
    }

    @Override
    protected c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        c c2;
        block6 : {
            block5 : {
                Log.d("LoyaltyCardPayProvider", "Entered getEnrollmentRequestData");
                c2 = new c();
                c2.setErrorCode(0);
                if (enrollCardInfo == null) {
                    Log.e("LoyaltyCardPayProvider", " getEnrollmentRequestData: Invalid input");
                    c2.setErrorCode(-4);
                    return c2;
                }
                if (!(enrollCardInfo instanceof EnrollCardLoyaltyInfo)) break block5;
                JsonObject jsonObject = this.b((EnrollCardLoyaltyInfo)enrollCardInfo);
                if (jsonObject == null) break block6;
                c2.a(jsonObject);
                Log.d("LoyaltyCardPayProvider", "Set Encrypted result data");
            }
            Bundle bundle = new Bundle();
            if (enrollCardInfo.getUserEmail() != null) {
                bundle.putString("emailHash", this.getEmailAddressHash(enrollCardInfo.getUserEmail()));
            }
            c2.e(bundle);
            return c2;
        }
        c2.setErrorCode(-2);
        Log.e("LoyaltyCardPayProvider", " getEnrollmentRequestData: PAYFW_ERROR_PAY_PROVIDER");
        return c2;
    }

    @Override
    public boolean getPayReadyState() {
        return true;
    }

    @Override
    protected c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    @Override
    protected c getReplenishmentRequestData() {
        return null;
    }

    @Override
    protected int getTransactionData(Bundle bundle, i i2) {
        return 0;
    }

    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        return new byte[0];
    }

    @Override
    protected void init() {
    }

    @Override
    protected void interruptMstPay() {
    }

    @Override
    protected void loadTA() {
        zg.loadTA();
        Log.i("LoyaltyCardPayProvider", "load real TA");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected String prepareLoyaltyDataForServer(String string) {
        String string2;
        Log.d("LoyaltyCardPayProvider", "Entered prepareLoyaltyDataForServer");
        if (zg == null || string == null) {
            Log.e("LoyaltyCardPayProvider", "prepareLoyaltyDataForServer: Error: mPlccTAC = " + zg + "lData = " + string);
            return null;
        }
        try {
            String string3;
            byte[] arrby = string.getBytes();
            long l2 = h.am(this.mContext);
            Log.d("LoyaltyCardPayProvider", "Network Time = " + l2);
            PlccTAController plccTAController = zg;
            string2 = string3 = Base64.encodeToString((byte[])plccTAController.utility_enc4Server_Transport("LOYALTY", arrby, l2), (int)2);
        }
        catch (Exception exception) {
            Log.c("LoyaltyCardPayProvider", exception.getMessage(), exception);
            string2 = null;
        }
        if (string2 == null) {
            Log.e("LoyaltyCardPayProvider", "prepareLoyaltyDataForServer : encData is null");
            return null;
        }
        Log.d("LoyaltyCardPayProvider", "prepareLoyaltyDataForServer: encrypted data  = " + string2);
        return string2;
    }

    @Override
    protected boolean prepareMstPay() {
        return true;
    }

    @Override
    public boolean prepareNfcPay() {
        return false;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        return null;
    }

    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public SelectCardResult selectCard() {
        Log.d("LoyaltyCardPayProvider", "Entered selectCard");
        if (zg == null) {
            Log.e("LoyaltyCardPayProvider", "TAController is null");
            return null;
        }
        try {
            return new SelectCardResult(PlccTAController.getInstance().getTAInfo().getTAId(), zg.getNonce(32));
        }
        catch (PlccTAException plccTAException) {
            Log.c("LoyaltyCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return null;
        }
    }

    @Override
    public void setCardTzEncData(byte[] arrby) {
        Log.d("LoyaltyCardPayProvider", "Entered setCardTzEncData");
        this.zD = arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        byte[] arrby = null;
        Log.d("LoyaltyCardPayProvider", "Entered setServerCertificates");
        if (arrcertificateInfo == null || arrcertificateInfo.length == 0) {
            Log.e("LoyaltyCardPayProvider", "setServerCertificates : invalid input");
            return false;
        }
        int n2 = arrcertificateInfo.length;
        byte[] arrby2 = null;
        byte[] arrby3 = null;
        for (int i2 = 0; i2 < n2; ++i2) {
            CertificateInfo certificateInfo = arrcertificateInfo[i2];
            String string = certificateInfo.getContent().replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"");
            if ("ENC".equals((Object)certificateInfo.getUsage())) {
                arrby3 = Base64.decode((String)string, (int)0);
                continue;
            }
            if ("VER".equals((Object)certificateInfo.getUsage())) {
                arrby2 = Base64.decode((String)string, (int)0);
                continue;
            }
            if (!"CA".equals((Object)certificateInfo.getUsage())) continue;
            arrby = Base64.decode((String)string, (int)0);
        }
        if (arrby3 != null && arrby2 != null && arrby != null) {
            PlccTAController plccTAController = zg;
            plccTAController.setPlccServerCerts("LOYALTY", arrby2, arrby3, arrby);
            return true;
        }
        return false;
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        Log.d("LoyaltyCardPayProvider", "Entered startMstPay");
        if (zg == null) {
            Log.e("LoyaltyCardPayProvider", "TAController is null");
            return false;
        }
        try {
            boolean bl = zg.mstTransmit(this.zD, n2, arrby);
            return bl;
        }
        catch (InterruptedException interruptedException) {
            Log.c("LoyaltyCardPayProvider", interruptedException.getMessage(), interruptedException);
            return false;
        }
        catch (PlccTAException plccTAException) {
            Log.c("LoyaltyCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void stopMstPay(boolean bl) {
        Log.i("LoyaltyCardPayProvider", "stopMstPay: start ");
        if (zg == null) {
            Log.e("LoyaltyCardPayProvider", "TAController is null");
            return;
        }
        try {
            zg.clearMstData();
        }
        catch (PlccTAException plccTAException) {
            Log.c("LoyaltyCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
        }
        Log.i("LoyaltyCardPayProvider", "stopMstPay: end ");
    }

    @Override
    protected Bundle stopNfcPay(int n2) {
        return null;
    }

    @Override
    protected void unloadTA() {
        zg.unloadTA();
        Log.i("LoyaltyCardPayProvider", "unload real TA");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected String updateLoyaltyCard(JsonObject jsonObject) {
        JsonElement jsonElement;
        Log.d("LoyaltyCardPayProvider", "Entered updateLoyaltyCard");
        String string = null;
        if (zg == null || jsonObject == null) {
            Log.e("LoyaltyCardPayProvider", "Error: mPlccTAC = " + zg + "; responseData = " + (Object)jsonObject);
            return null;
        }
        try {
            Log.d("LoyaltyCardPayProvider", "updateLoyaltyCard: Response Data = " + jsonObject.toString());
            jsonElement = jsonObject.get("encrypted");
            string = null;
            if (jsonElement == null) return null;
        }
        catch (Exception exception) {
            Log.c("LoyaltyCardPayProvider", exception.getMessage(), exception);
            return string;
        }
        String string2 = jsonElement.getAsString();
        Log.d("LoyaltyCardPayProvider", "updateLoyaltyCard: ac = " + string2);
        byte[] arrby = Base64.decode((String)string2, (int)0);
        PlccTAController plccTAController = zg;
        String string3 = Base64.encodeToString((byte[])plccTAController.addCard("LOYALTY", arrby), (int)2);
        Log.d("LoyaltyCardPayProvider", "updateLoyaltyCard: tzEnc = " + string3);
        jsonObject.addProperty("encrypted", string3);
        string = this.mGson.toJson((JsonElement)jsonObject);
        Log.d("LoyaltyCardPayProvider", "updateLoyaltyCard: Response Data String = " + string);
        return string;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void updateTokenMetaData(JsonObject jsonObject, Token token) {
        block10 : {
            Log.d("LoyaltyCardPayProvider", "updateTokenMetaData");
            if (jsonObject != null) {
                try {
                    Log.d("LoyaltyCardPayProvider", "updateTokenMetaData:Response Data = " + jsonObject.toString());
                    JsonElement jsonElement = jsonObject.get("encrypted");
                    if (jsonElement == null) break block10;
                    String string = jsonElement.getAsString();
                    Log.d("LoyaltyCardPayProvider", "ac = " + string.toString());
                    byte[] arrby = Base64.decode((String)string, (int)0);
                    PlccTAController plccTAController = zg;
                    byte[] arrby2 = plccTAController.addCard("LOYALTY", arrby);
                    String string2 = Base64.encodeToString((byte[])arrby2, (int)2);
                    Log.d("LoyaltyCardPayProvider", "tzEnc = " + string2.toString());
                    jsonObject.addProperty("encrypted", string2);
                    String string3 = this.mGson.toJson((JsonElement)jsonObject);
                    Log.d("LoyaltyCardPayProvider", "Response Data String = " + string3);
                    Bundle bundle = new Bundle();
                    String string4 = this.aT(string3);
                    bundle.putString("extraMetaDataFilePath", string4);
                    bundle.putParcelable("extraMetaDataFd", (Parcelable)this.aS(string4));
                    if (token != null && token.getMetadata() != null && token.getMetadata().getCardRefernceId() != null) {
                        String string5 = token.getMetadata().getCardRefernceId();
                        ExtractCardDetailResult extractCardDetailResult = zg.extractLoyaltyCardDetail(string5.getBytes(), arrby2);
                        if (extractCardDetailResult != null) {
                            if (extractCardDetailResult.getExtraContent() != null && !extractCardDetailResult.getExtraContent().isEmpty()) {
                                bundle.putString("acTokenExtra", extractCardDetailResult.getExtraContent());
                                Log.d("LoyaltyCardPayProvider", "added extra as part of Provision Response:" + extractCardDetailResult.getExtraContent());
                            } else if (extractCardDetailResult.getImgSessionKey() != null && !extractCardDetailResult.getImgSessionKey().isEmpty()) {
                                bundle.putString("imgSessionKey", extractCardDetailResult.getImgSessionKey());
                                Log.d("LoyaltyCardPayProvider", "added ImgSessionKey as part of Provision Response:" + extractCardDetailResult.getImgSessionKey());
                            }
                        } else {
                            Log.i("LoyaltyCardPayProvider", " ImgSessionKey not added part of Provision Response");
                        }
                        token.getMetadata().setExtraMetaData(bundle);
                        return;
                    }
                }
                catch (Exception exception) {
                    Log.c("LoyaltyCardPayProvider", exception.getMessage(), exception);
                    return;
                }
                Log.i("LoyaltyCardPayProvider", " CardRefId not part of request");
            }
        }
    }

    @Override
    protected e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }
}

