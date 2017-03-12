package com.samsung.android.spayfw.payprovider.mastercard;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.mastercard.mobile_api.utils.Date;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.McInAppPaymentInfoData.McInAppPaymentInfoDataBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.Iso4217CurrencyCode;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.utils.Utils;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;

public class McPayDataHandler {
    private static final String TAG = "mcpce_McPayDataHandler";

    public DSRPInputData convertToDsrpInput(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        if (inAppDetailedTransactionInfo != null) {
            return populateDSRPInputData(inAppDetailedTransactionInfo);
        }
        Log.m286e(TAG, "transactionInfo is null");
        throw new PaymentProviderException(-36);
    }

    private DSRPInputData populateDSRPInputData(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        CryptogramType cryptogramType = CryptogramType.UCAF;
        DSRPInputData dSRPInputData = new DSRPInputData();
        String currencyCode = inAppDetailedTransactionInfo.getCurrencyCode();
        if (currencyCode == null || currencyCode.length() != 3) {
            Log.m286e(TAG, "Transaction CurrencyCode is null");
            throw new PaymentProviderException(-36);
        }
        try {
            dSRPInputData.setCurrencyCode(Iso4217CurrencyCode.valueOf(currencyCode).getNumericCurrencyCode());
            currencyCode = inAppDetailedTransactionInfo.getAmount();
            if (currencyCode == null || currencyCode.isEmpty()) {
                Log.m286e(TAG, "Transaction amount is null");
                throw new PaymentProviderException(-36);
            }
            try {
                dSRPInputData.setTransactionAmount(Long.valueOf(currencyCode).longValue());
                dSRPInputData.setOtherAmount(0);
                dSRPInputData.setTransactionType((byte) 0);
                dSRPInputData.setCryptogramType(cryptogramType);
                dSRPInputData.setCountryCode(0);
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(Utils.am(McProvider.getContext()));
                Date date = new Date(instance.get(1), instance.get(2) + 1, instance.get(5));
                dSRPInputData.setTransactionDate(date);
                Log.m285d(TAG, "transaction date :" + date.toString());
                dSRPInputData.setUnpredictableNumber(0);
                return dSRPInputData;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.m286e(TAG, "Invalid Amount format");
                throw new PaymentProviderException(-36);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.m286e(TAG, "Invalid Transaction CurrencyCode");
            throw new PaymentProviderException(-36);
        }
    }

    private String convertToFDRDate(byte[] bArr) {
        if (bArr == null || bArr.length <= 1) {
            return null;
        }
        return McUtils.byteToHex(bArr[1]) + McUtils.byteToHex(bArr[0]);
    }

    public byte[] convertToPaymentGatewayFormat(String str, byte[] bArr, DSRPOutputData dSRPOutputData) {
        Gson gson = new Gson();
        String convertToFDRDate = convertToFDRDate(dSRPOutputData.getExpiryDate());
        Log.m285d(TAG, "Exp" + convertToFDRDate);
        Log.m285d(TAG, "DSRPOutputData:" + gson.toJson((Object) dSRPOutputData));
        Object build = McInAppPaymentInfoDataBuilder.getInstance().setAmount(String.valueOf(dSRPOutputData.getTransactionAmount())).setCurrency_code(Iso4217CurrencyCode.getStringCurrencyCode(dSRPOutputData.getCurrencyCode())).setUtc(McUtils.getRealTime(McProvider.getContext())).setEci_indicator(dSRPOutputData.getEciValue()).setCryptogram(new String(dSRPOutputData.getTransactionCryptogramData())).setTokenPAN(dSRPOutputData.getPan()).setTokenPanExpiry(convertToFDRDate).build();
        Log.m285d(TAG, "InApp Output convertToPaymentGatewayFormat: " + gson.toJson(build));
        JSONObject jSONObject = build.getJSONObject();
        if (jSONObject == null) {
            Log.m286e(TAG, "DSRP: paymentInfoJSONObject is null");
            throw new PaymentProviderException(-36);
        }
        byte[] dsrpJweData;
        Log.m285d(TAG, "InApp Output : get dsrp data");
        McTAController instance = McTAController.getInstance();
        if (!TextUtils.isEmpty(str)) {
            List bE = Utils.bE(str);
            if (bE == null || bE.size() < 2) {
                Log.m286e(TAG, "inApp Cert parse error");
                if (bE != null) {
                    Log.m286e(TAG, "inApp Cert parse error size : " + bE.size());
                }
                throw new PaymentProviderException(-36);
            }
            dsrpJweData = instance.getDsrpJweData(jSONObject.toString().getBytes(), (byte[]) bE.get(0), (byte[]) bE.get(1));
        } else if (bArr == null) {
            Log.m286e(TAG, " InApp Nonce is null");
            throw new PaymentProviderException(-36);
        } else {
            dsrpJweData = instance.getDsrpCnccData(jSONObject.toString().getBytes(), bArr);
        }
        Log.m285d(TAG, "UCAF is generated");
        if (dsrpJweData != null) {
            Log.m285d(TAG, " jwePayload : " + McUtils.byteArrayToHex(dsrpJweData));
        } else {
            Log.m286e(TAG, "JWE encoding failed, JWE is null");
        }
        return dsrpJweData;
    }
}
