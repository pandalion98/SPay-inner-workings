/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Calendar
 *  java.util.List
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.payprovider.mastercard;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.mastercard.mobile_api.utils.Date;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.McInAppPaymentInfoData;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.Iso4217CurrencyCode;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.utils.h;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;

public class McPayDataHandler {
    private static final String TAG = "mcpce_McPayDataHandler";

    private String convertToFDRDate(byte[] arrby) {
        if (arrby == null || arrby.length <= 1) {
            return null;
        }
        return McUtils.byteToHex(arrby[1]) + McUtils.byteToHex(arrby[0]);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private DSRPInputData populateDSRPInputData(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        block6 : {
            block5 : {
                CryptogramType cryptogramType = CryptogramType.UCAF;
                DSRPInputData dSRPInputData = new DSRPInputData();
                String string = inAppDetailedTransactionInfo.getCurrencyCode();
                if (string == null || string.length() != 3) break block5;
                try {
                    dSRPInputData.setCurrencyCode(Iso4217CurrencyCode.valueOf(string).getNumericCurrencyCode());
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    c.e(TAG, "Invalid Transaction CurrencyCode");
                    throw new PaymentProviderException(-36);
                }
                String string2 = inAppDetailedTransactionInfo.getAmount();
                if (string2 != null && !string2.isEmpty()) {
                    dSRPInputData.setTransactionAmount(Long.valueOf((String)string2));
                    dSRPInputData.setOtherAmount(0L);
                    dSRPInputData.setTransactionType((byte)0);
                    dSRPInputData.setCryptogramType(cryptogramType);
                    dSRPInputData.setCountryCode(0);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(h.am(McProvider.getContext()));
                    Date date = new Date(calendar.get(1), 1 + calendar.get(2), calendar.get(5));
                    dSRPInputData.setTransactionDate(date);
                    c.d(TAG, "transaction date :" + date.toString());
                    dSRPInputData.setUnpredictableNumber(0L);
                    return dSRPInputData;
                }
                break block6;
            }
            c.e(TAG, "Transaction CurrencyCode is null");
            throw new PaymentProviderException(-36);
            catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                c.e(TAG, "Invalid Amount format");
                throw new PaymentProviderException(-36);
            }
        }
        c.e(TAG, "Transaction amount is null");
        throw new PaymentProviderException(-36);
    }

    public DSRPInputData convertToDsrpInput(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        if (inAppDetailedTransactionInfo == null) {
            c.e(TAG, "transactionInfo is null");
            throw new PaymentProviderException(-36);
        }
        return this.populateDSRPInputData(inAppDetailedTransactionInfo);
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] convertToPaymentGatewayFormat(String string, byte[] arrby, DSRPOutputData dSRPOutputData) {
        byte[] arrby2;
        Gson gson = new Gson();
        String string2 = this.convertToFDRDate(dSRPOutputData.getExpiryDate());
        c.d(TAG, "Exp" + string2);
        c.d(TAG, "DSRPOutputData:" + gson.toJson((Object)dSRPOutputData));
        McInAppPaymentInfoData mcInAppPaymentInfoData = McInAppPaymentInfoData.McInAppPaymentInfoDataBuilder.getInstance().setAmount(String.valueOf((long)dSRPOutputData.getTransactionAmount())).setCurrency_code(Iso4217CurrencyCode.getStringCurrencyCode(dSRPOutputData.getCurrencyCode())).setUtc(McUtils.getRealTime(McProvider.getContext())).setEci_indicator(dSRPOutputData.getEciValue()).setCryptogram(new String(dSRPOutputData.getTransactionCryptogramData())).setTokenPAN(dSRPOutputData.getPan()).setTokenPanExpiry(string2).build();
        c.d(TAG, "InApp Output convertToPaymentGatewayFormat: " + gson.toJson((Object)mcInAppPaymentInfoData));
        JSONObject jSONObject = mcInAppPaymentInfoData.getJSONObject();
        if (jSONObject == null) {
            c.e(TAG, "DSRP: paymentInfoJSONObject is null");
            throw new PaymentProviderException(-36);
        }
        c.d(TAG, "InApp Output : get dsrp data");
        McTAController mcTAController = McTAController.getInstance();
        if (!TextUtils.isEmpty((CharSequence)string)) {
            List<byte[]> list = h.bE(string);
            if (list == null || list.size() < 2) {
                c.e(TAG, "inApp Cert parse error");
                if (list != null) {
                    c.e(TAG, "inApp Cert parse error size : " + list.size());
                }
                throw new PaymentProviderException(-36);
            }
            arrby2 = mcTAController.getDsrpJweData(jSONObject.toString().getBytes(), (byte[])list.get(0), (byte[])list.get(1));
        } else {
            if (arrby == null) {
                c.e(TAG, " InApp Nonce is null");
                throw new PaymentProviderException(-36);
            }
            arrby2 = mcTAController.getDsrpCnccData(jSONObject.toString().getBytes(), arrby);
        }
        c.d(TAG, "UCAF is generated");
        if (arrby2 != null) {
            c.d(TAG, " jwePayload : " + McUtils.byteArrayToHex(arrby2));
            return arrby2;
        }
        c.e(TAG, "JWE encoding failed, JWE is null");
        return arrby2;
    }
}

