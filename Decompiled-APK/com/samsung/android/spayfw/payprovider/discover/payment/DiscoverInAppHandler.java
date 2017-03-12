package com.samsung.android.spayfw.payprovider.discover.payment;

import android.content.Context;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppPaymentInfoData.C0509a;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetInAppCnccData.DiscoverTAGetInAppCnccDataResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.utils.Utils;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.d */
public class DiscoverInAppHandler {
    private String convertToFDRDate(byte[] bArr) {
        if (bArr == null || bArr.length <= 1) {
            return null;
        }
        return DiscoverInAppHandler.byteToHex(bArr[1]) + DiscoverInAppHandler.byteToHex(bArr[0]);
    }

    public static String byteToHex(byte b) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(b & GF2Field.MASK)}));
        return stringBuilder.toString();
    }

    public static synchronized String getRealTime(Context context) {
        String valueOf;
        synchronized (DiscoverInAppHandler.class) {
            valueOf = String.valueOf(Utils.am(context));
        }
        return valueOf;
    }

    public byte[] m943a(byte[] bArr, DiscoverInAppCryptoData discoverInAppCryptoData) {
        if (discoverInAppCryptoData == null) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, cryptoData is null.");
            throw new PaymentProviderException(-36);
        } else if (bArr == null) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, nonce is null.");
            throw new PaymentProviderException(-36);
        } else {
            m941a(discoverInAppCryptoData);
            Gson create = new GsonBuilder().disableHtmlEscaping().create();
            String convertToFDRDate = convertToFDRDate(discoverInAppCryptoData.getExpiryDate());
            Log.m285d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, GSON output data:" + create.toJson((Object) discoverInAppCryptoData));
            Log.m285d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, GSON output crypgtam base 64 encoded:" + Base64.encodeToString(discoverInAppCryptoData.getCryptogram(), 2));
            Object jSONObject = C0509a.dX().aL(String.valueOf(discoverInAppCryptoData.getTransactionAmount())).aM("USD").aN(DiscoverInAppHandler.getRealTime(PaymentFrameworkApp.aB())).aH(discoverInAppCryptoData.getEciValue()).aK(Base64.encodeToString(discoverInAppCryptoData.getCryptogram(), 2)).aI(discoverInAppCryptoData.getPan()).aJ(convertToFDRDate).dY().getJSONObject();
            Log.m285d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, In App data convert to 3DS: " + create.toJson(jSONObject));
            if (jSONObject == null) {
                Log.m286e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat: paymentInfoJSONObject is null");
                throw new PaymentProviderException(-36);
            }
            Log.m285d("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, wrap in app data and get it from TA");
            try {
                DiscoverTAGetInAppCnccDataResponse d = DcTAController.eu().m1051d(jSONObject.toString().getBytes(), bArr);
                if (d != null) {
                    return d.getPayload();
                }
                Log.m286e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, TA response is null.");
                throw new PaymentProviderException(-36);
            } catch (DcTAException e) {
                Log.m286e("DCSDK_DiscoverInAppHandler", "convertTo3DSFormat, unexpected DC TA exception.");
                throw new PaymentProviderException(-36);
            }
        }
    }

    private void m941a(DiscoverInAppCryptoData discoverInAppCryptoData) {
        if (discoverInAppCryptoData.getCryptogram() == null || discoverInAppCryptoData.getCryptogram().length != 20) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "checkCryptoData, wrong cryptogram length.");
            throw new PaymentProviderException(-36);
        } else if (discoverInAppCryptoData.getExpiryDate() == null || discoverInAppCryptoData.getExpiryDate().length < 2) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "checkCryptoData, wrong expiration date length.");
            throw new PaymentProviderException(-36);
        } else if (discoverInAppCryptoData.getPan() == null) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "checkCryptoData, wrong PAN data.");
            throw new PaymentProviderException(-36);
        }
    }

    public DiscoverInAppCryptoData m942a(InAppDetailedTransactionInfo inAppDetailedTransactionInfo, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        if (discoverContactlessPaymentData == null || discoverContactlessPaymentData.getDiscoverApplicationData() == null) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "getCryptoData, payment data is null.");
            throw new PaymentProviderException(-36);
        }
        DiscoverInAppCryptoData discoverInAppCryptoData = new DiscoverInAppCryptoData();
        discoverInAppCryptoData.setEciValue(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        try {
            Log.m285d("DCSDK_DiscoverInAppHandler", "getCryptoData, convert amount to long.");
            discoverInAppCryptoData.setTransactionAmount(Long.parseLong(inAppDetailedTransactionInfo.getAmount()));
            try {
                Log.m285d("DCSDK_DiscoverInAppHandler", "getCryptoData, convert currency code to int.");
                discoverInAppCryptoData.setCurrencyCode(Integer.parseInt("840"));
                if (discoverContactlessPaymentData.getDiscoverApplicationData().getPan() == null) {
                    Log.m286e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot find PAN in profile, PAN is null.");
                    throw new PaymentProviderException(-36);
                }
                discoverInAppCryptoData.setPan(discoverContactlessPaymentData.getDiscoverApplicationData().getPan().toHexString());
                if (discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationExpirationDate() == null) {
                    Log.m286e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot find expiration date in profile, expiration date is null.");
                    throw new PaymentProviderException(-36);
                }
                discoverInAppCryptoData.setExpiryDate(discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationExpirationDate().getBytes());
                return discoverInAppCryptoData;
            } catch (NumberFormatException e) {
                Log.m286e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot convert currency code to int: " + e.getMessage());
                e.printStackTrace();
                throw new PaymentProviderException(-36);
            }
        } catch (NumberFormatException e2) {
            Log.m286e("DCSDK_DiscoverInAppHandler", "getCryptoData, cannot convert amount to long: " + e2.getMessage());
            e2.printStackTrace();
            throw new PaymentProviderException(-36);
        }
    }
}
