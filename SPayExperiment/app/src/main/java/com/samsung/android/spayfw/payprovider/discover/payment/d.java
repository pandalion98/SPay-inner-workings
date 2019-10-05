/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import android.content.Context;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppPaymentInfoData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.b;
import com.samsung.android.spayfw.utils.h;
import org.json.JSONObject;

public class d {
    private void a(DiscoverInAppCryptoData discoverInAppCryptoData) {
        if (discoverInAppCryptoData.getCryptogram() == null || discoverInAppCryptoData.getCryptogram().length != 20) {
            Log.e("DCSDK_DiscoverInAppHandler", "checkCryptoData, wrong cryptogram length.");
            throw new PaymentProviderException(-36);
        }
        if (discoverInAppCryptoData.getExpiryDate() == null || discoverInAppCryptoData.getExpiryDate().length < 2) {
            Log.e("DCSDK_DiscoverInAppHandler", "checkCryptoData, wrong expiration date length.");
            throw new PaymentProviderException(-36);
        }
        if (discoverInAppCryptoData.getPan() == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "checkCryptoData, wrong PAN data.");
            throw new PaymentProviderException(-36);
        }
    }

    public static String byteToHex(byte by) {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] arrobject = new Object[]{by & 255};
        stringBuilder.append(String.format((String)"%02x", (Object[])arrobject));
        return stringBuilder.toString();
    }

    private String convertToFDRDate(byte[] arrby) {
        if (arrby == null || arrby.length <= 1) {
            return null;
        }
        return d.byteToHex(arrby[1]) + d.byteToHex(arrby[0]);
    }

    public static String getRealTime(Context context) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            String string = String.valueOf((long)h.am(context));
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return string;
        }
    }

    public DiscoverInAppCryptoData a(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        if (discoverContactlessPaymentData == null || discoverContactlessPaymentData.getDiscoverApplicationData() == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "getCryptoData, payment data is null.");
            throw new PaymentProviderException(-36);
        }
        DiscoverInAppCryptoData discoverInAppCryptoData = new DiscoverInAppCryptoData();
        discoverInAppCryptoData.setEciValue("4");
        try {
            Log.d("DCSDK_DiscoverInAppHandler", "getCryptoData, convert amount to long.");
            discoverInAppCryptoData.setTransactionAmount(Long.parseLong((String)inAppDetailedTransactionInfo.getAmount()));
        }
        catch (NumberFormatException numberFormatException) {
            Log.e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot convert amount to long: " + numberFormatException.getMessage());
            numberFormatException.printStackTrace();
            throw new PaymentProviderException(-36);
        }
        try {
            Log.d("DCSDK_DiscoverInAppHandler", "getCryptoData, convert currency code to int.");
            discoverInAppCryptoData.setCurrencyCode(Integer.parseInt((String)"840"));
        }
        catch (NumberFormatException numberFormatException) {
            Log.e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot convert currency code to int: " + numberFormatException.getMessage());
            numberFormatException.printStackTrace();
            throw new PaymentProviderException(-36);
        }
        if (discoverContactlessPaymentData.getDiscoverApplicationData().getPan() == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot find PAN in profile, PAN is null.");
            throw new PaymentProviderException(-36);
        }
        discoverInAppCryptoData.setPan(discoverContactlessPaymentData.getDiscoverApplicationData().getPan().toHexString());
        if (discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationExpirationDate() == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot find expiration date in profile, expiration date is null.");
            throw new PaymentProviderException(-36);
        }
        discoverInAppCryptoData.setExpiryDate(discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationExpirationDate().getBytes());
        return discoverInAppCryptoData;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] a(byte[] arrby, DiscoverInAppCryptoData discoverInAppCryptoData) {
        if (discoverInAppCryptoData == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, cryptoData is null.");
            throw new PaymentProviderException(-36);
        }
        if (arrby == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, nonce is null.");
            throw new PaymentProviderException(-36);
        }
        this.a(discoverInAppCryptoData);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String string = this.convertToFDRDate(discoverInAppCryptoData.getExpiryDate());
        Log.d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, GSON output data:" + gson.toJson((Object)discoverInAppCryptoData));
        Log.d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, GSON output crypgtam base 64 encoded:" + Base64.encodeToString((byte[])discoverInAppCryptoData.getCryptogram(), (int)2));
        JSONObject jSONObject = DiscoverInAppPaymentInfoData.a.dX().aL(String.valueOf((long)discoverInAppCryptoData.getTransactionAmount())).aM("USD").aN(d.getRealTime((Context)PaymentFrameworkApp.aB())).aH(discoverInAppCryptoData.getEciValue()).aK(Base64.encodeToString((byte[])discoverInAppCryptoData.getCryptogram(), (int)2)).aI(discoverInAppCryptoData.getPan()).aJ(string).dY().getJSONObject();
        Log.d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, In App data convert to 3DS: " + gson.toJson((Object)jSONObject));
        if (jSONObject == null) {
            Log.e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat: paymentInfoJSONObject is null");
            throw new PaymentProviderException(-36);
        }
        Log.d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, wrap in app data and get it from TA");
        b b2 = b.eu();
        try {
            DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataResponse discoverTAGetInAppCnccDataResponse = b2.d(jSONObject.toString().getBytes(), arrby);
            if (discoverTAGetInAppCnccDataResponse != null) return discoverTAGetInAppCnccDataResponse.getPayload();
            Log.e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, TA response is null.");
            throw new PaymentProviderException(-36);
        }
        catch (DcTAException dcTAException) {
            Log.e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, unexpected DC TA exception.");
            throw new PaymentProviderException(-36);
        }
    }
}

