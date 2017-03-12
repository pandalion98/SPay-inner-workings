package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings.Global;
import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.fraud.FraudDataProvider;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.a */
public class RiskDataCollector {
    public static ArrayList<RiskDataParam> m1083a(Context context, ProvisionTokenInfo provisionTokenInfo, VisaPayProviderService visaPayProviderService) {
        String str;
        int x;
        ArrayList<RiskDataParam> arrayList = new ArrayList();
        Log.m285d("RiskDataCollector", "getRiskData: tokenInfo = " + provisionTokenInfo);
        if (!(provisionTokenInfo == null || provisionTokenInfo.getActivationParams() == null)) {
            str = (String) provisionTokenInfo.getActivationParams().get(ActivationData.CARD_INFO_CVV);
            String str2 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.CARD_INFO_BILLING_ZIP);
            Object obj = (String) provisionTokenInfo.getActivationParams().get(ActivationData.CARD_INFO_BILLING_LINE1);
            Object obj2 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.LAST_ACC_SETTINGS_CHANGED_IN_DAYS);
            Object obj3 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_ACCOUNT_FIRST_CREATED_IN_DAYS);
            Object obj4 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.USER_ACCOUNT_FIRST_CREATED_IN_DAYS);
            String str3 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_AND_CARDHOLDER_NAME_MATCH);
            Object obj5 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_ACCOUNT_HOLDER_NAME);
            Object obj6 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_ACCOUNT_DEVICE_BINDING_AGE_IN_DAYS);
            String str4 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_ACCOUNT_COUNTRY_CODE);
            Object obj7 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.SUSPENDED_CARDS_IN_ACCOUNT);
            Object obj8 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.LAST_ACCOUNT_ACTIVITY_IN_DAYS);
            Object obj9 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.LAST_12_MONTHS_TRANSACTION_COUNT);
            Object obj10 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_ACC_ACTIVE_TOKENS_GIVEN_DEVICE);
            String str5 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.NUM_DEVICES_FOR_ACC_WITH_ACTIVE_TOKENS);
            String str6 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.WALLET_ACC_ACTIVE_TOKENS_ALL_DEVICES);
            String str7 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.CARD_INFO_BILLING_COUNTRY);
            String str8 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.EXPIRATION_DATE_MONTH);
            String str9 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.EXPIRATION_DATE_YEAR);
            if (Utils.DEBUG) {
                Log.m285d("RiskDataCollector", "getRiskData: cvv = " + str + "; zip = " + str2 + "; line1 = " + obj + "; sinceAccSettingsChanged = " + obj2 + "; walletAccFirstCreated = " + obj3 + "; userAccFirstCreated = " + obj4 + "; walletAccHolderCardNameMatch = " + str3 + "; accHolderName = " + obj5 + "; accToDevBindingAge = " + obj6 + "; walletAccCountry = " + str4 + "; susCardsInAcc = " + obj7 + "; sinceLastAccActivity = " + obj8 + "; numTransacLast12Months = " + obj9 + "; numActiveTokens = " + obj10 + "; devWithActiveTokens = " + str5 + "; activeTokOnAllDevicesForAcc" + str6 + "; billingCountryCode = " + str7 + "; expirationDateMM = " + str8 + "; expirationDateYY = " + str9);
            }
            if (str != null && str.length() > 0 && str.length() <= 4 && TextUtils.isDigitsOnly(str)) {
                if (visaPayProviderService != null) {
                    byte[] aY = visaPayProviderService.aY(str);
                    if (aY == null || aY.length <= 0) {
                        Log.m286e("RiskDataCollector", "unable to get Encrypted cvv for risk data");
                    } else {
                        arrayList.add(new RiskDataParam("encCvv2", new String(aY)));
                        Log.m285d("RiskDataCollector", "Encrypted cvv added to risk data: length " + aY.length);
                    }
                }
                Utils.clearMemory(str);
            }
            if (str2 != null && str2.length() >= 3 && str2.length() <= 16) {
                arrayList.add(new RiskDataParam(ActivationData.CARD_INFO_BILLING_ZIP, str2));
            }
            if (obj != null && obj.length() > 0) {
                try {
                    if (obj.length() > 32) {
                        obj = obj.substring(0, 32);
                    }
                    arrayList.add(new RiskDataParam(ActivationData.CARD_INFO_BILLING_LINE1, obj));
                } catch (IndexOutOfBoundsException e) {
                    Log.m285d("RiskDataCollector", "getRiskData: line1 incorrect format!");
                }
            }
            if (obj2 != null && obj2.length() > 0 && obj2.length() <= 4) {
                try {
                    if (Integer.valueOf(obj2).intValue() > 9999) {
                        obj2 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.LAST_ACC_SETTINGS_CHANGED_IN_DAYS, obj2));
                } catch (NumberFormatException e2) {
                    Log.m285d("RiskDataCollector", "getRiskData: sinceAccSettingsChanged incorrect format!");
                }
            }
            if (obj3 != null && obj3.length() > 0 && obj3.length() <= 4) {
                try {
                    if (Integer.valueOf(obj3).intValue() > 9999) {
                        obj3 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_FIRST_CREATED_IN_DAYS, obj3));
                } catch (NumberFormatException e3) {
                    Log.m285d("RiskDataCollector", "getRiskData: walletAccFirstCreated incorrect format!");
                }
            }
            if (obj4 != null && obj4.length() > 0 && obj4.length() <= 4 && TextUtils.isDigitsOnly(obj4)) {
                try {
                    if (Integer.valueOf(obj4).intValue() > 9999) {
                        obj4 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.USER_ACCOUNT_FIRST_CREATED_IN_DAYS, obj4));
                } catch (NumberFormatException e4) {
                    Log.m285d("RiskDataCollector", "getRiskData: userAccFirstCreated incorrect format!");
                }
            }
            if (str3 != null && str3.length() == 1 && (str3.equals(ActivationData.YES) || str3.equals(ActivationData.NO))) {
                arrayList.add(new RiskDataParam(ActivationData.WALLET_AND_CARDHOLDER_NAME_MATCH, str3));
            }
            if (!(obj5 == null || obj5.isEmpty())) {
                try {
                    if (obj5.length() > 64) {
                        obj5 = obj5.substring(0, 64);
                    }
                    arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_HOLDER_NAME, obj5));
                } catch (IndexOutOfBoundsException e5) {
                    Log.m285d("RiskDataCollector", "getRiskData: accHolderName incorrect format!");
                }
            }
            if (obj6 != null && obj6.length() > 0 && obj6.length() <= 4) {
                try {
                    if (Integer.valueOf(obj6).intValue() > 9999) {
                        obj6 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_DEVICE_BINDING_AGE_IN_DAYS, obj6));
                } catch (NumberFormatException e6) {
                    Log.m285d("RiskDataCollector", "getRiskData: accToDevBindingAge incorrect format!");
                }
            }
            if (str4 != null && str4.length() == 2) {
                arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_COUNTRY_CODE, str4));
            }
            if (obj7 != null && obj7.length() > 0 && obj7.length() <= 2) {
                try {
                    if (Integer.valueOf(obj7).intValue() > 99) {
                        obj7 = "99";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.SUSPENDED_CARDS_IN_ACCOUNT, obj7));
                } catch (NumberFormatException e7) {
                    Log.m285d("RiskDataCollector", "getRiskData: susCardsInAcc incorrect format!");
                }
            }
            if (obj8 != null && obj8.length() > 0 && obj8.length() <= 4) {
                try {
                    if (Integer.valueOf(obj8).intValue() > 9999) {
                        obj8 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.LAST_ACCOUNT_ACTIVITY_IN_DAYS, obj8));
                } catch (NumberFormatException e8) {
                    Log.m285d("RiskDataCollector", "getRiskData: sinceLastAccActivity incorrect format!");
                }
            }
            if (obj9 != null && obj9.length() > 0 && obj9.length() <= 4) {
                try {
                    if (Integer.valueOf(obj9).intValue() > 9999) {
                        obj9 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.LAST_12_MONTHS_TRANSACTION_COUNT, obj9));
                } catch (NumberFormatException e9) {
                    Log.m285d("RiskDataCollector", "getRiskData: numTransacLast12Months incorrect format!");
                }
            }
            if (obj10 != null && obj10.length() > 0 && obj10.length() <= 2) {
                try {
                    if (Integer.valueOf(obj10).intValue() > 99) {
                        obj10 = "99";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.WALLET_ACC_ACTIVE_TOKENS_GIVEN_DEVICE, obj10));
                } catch (NumberFormatException e10) {
                    Log.m285d("RiskDataCollector", "getRiskData: numActiveTokens incorrect format!");
                }
            }
            if (str5 != null && str5.length() > 0 && str5.length() <= 2) {
                try {
                    if (Integer.valueOf(str5).intValue() > 99) {
                        str5 = "99";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.NUM_DEVICES_FOR_ACC_WITH_ACTIVE_TOKENS, str5));
                } catch (NumberFormatException e11) {
                    Log.m285d("RiskDataCollector", "getRiskData: devWithActiveTokens incorrect format!");
                }
            }
            if (str6 != null && str6.length() > 0 && str6.length() <= 4) {
                try {
                    if (Integer.valueOf(str6).intValue() > 9999) {
                        str6 = "9999";
                    }
                    arrayList.add(new RiskDataParam(ActivationData.WALLET_ACC_ACTIVE_TOKENS_ALL_DEVICES, str6));
                } catch (NumberFormatException e12) {
                    Log.m285d("RiskDataCollector", "getRiskData: activeTokOnAllDevicesForAcc incorrect format!");
                }
            }
            if (str7 != null && str7.length() == 2) {
                arrayList.add(new RiskDataParam(ActivationData.CARD_INFO_BILLING_COUNTRY, str7));
            }
            if (!(str8 == null || str8.isEmpty() || str9 == null || str9.isEmpty())) {
                Log.m285d("RiskDataCollector", "getRiskData: adding expiration month & year");
                arrayList.add(new RiskDataParam("paymentInstrument.expirationDate.month", str8));
                arrayList.add(new RiskDataParam("paymentInstrument.expirationDate.year", RiskDataCollector.aV(str9)));
            }
        }
        str = DeviceInfo.getDeviceSerialNumber();
        Log.m285d("RiskDataCollector", "getRiskData: devSerialNumber = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: devSerialNumber.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 24) {
            arrayList.add(new RiskDataParam("deviceSerialNumber", str));
        }
        str = DeviceInfo.getDeviceImei(context);
        Log.m285d("RiskDataCollector", "getRiskData: imei = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: imei.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 24) {
            arrayList.add(new RiskDataParam("deviceIMEI", str));
        }
        str = DeviceInfo.getDeviceBluetoothMac();
        Log.m285d("RiskDataCollector", "getRiskData: deviceBluetoothMac = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: devBtMac.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 24) {
            arrayList.add(new RiskDataParam("deviceBluetoothMac", str));
        }
        str = TimeZone.getDefault().getDisplayName(false, 0);
        Log.m285d("RiskDataCollector", "getRiskData: timeZone = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: timeZone.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 5) {
            arrayList.add(new RiskDataParam("deviceTimeZone", str));
        }
        try {
            int i = Global.getInt(context.getContentResolver(), "auto_time_zone");
            if (i == 1) {
                str = TransactionInfo.VISA_TRANSACTIONTYPE_REFUND;
            } else {
                str = TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED;
            }
            Log.m285d("RiskDataCollector", "getRiskData: deviceTimeZoneSetting = " + i + "; tzSetting = " + str);
            arrayList.add(new RiskDataParam("deviceTimeZoneSetting", str));
        } catch (Throwable e13) {
            Log.m284c("RiskDataCollector", e13.getMessage(), e13);
        }
        str = DeviceInfo.getDeviceId(context);
        Log.m285d("RiskDataCollector", "getRiskData: seId = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: seId.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 24) {
            arrayList.add(new RiskDataParam("seID", str));
        }
        str = DeviceInfo.getAndroidId(context);
        Log.m285d("RiskDataCollector", "getRiskData: androidId = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: androidId.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 24) {
            arrayList.add(new RiskDataParam("androidId", str));
        }
        str = DeviceInfo.getSimSerialNumber(context);
        Log.m285d("RiskDataCollector", "getRiskData: simSerialNum = " + str);
        if (str != null) {
            Log.m285d("RiskDataCollector", "getRiskData: simSerialNum.len = " + str.length());
        }
        if (str != null && str.length() > 0 && str.length() <= 24) {
            arrayList.add(new RiskDataParam("simSerialNumber", str));
        }
        FraudDataProvider fraudDataProvider = new FraudDataProvider(context);
        try {
            x = fraudDataProvider.m743x(1);
            if (x > 99) {
                x = 99;
            }
            str = String.valueOf(x);
            Log.m285d("RiskDataCollector", "getRiskData: provAttempts = " + str);
            if (!(str == null || str.isEmpty())) {
                arrayList.add(new RiskDataParam(ActivationData.PROVISION_ATTEMPTS_LAST_24_HOURS, str));
            }
        } catch (Throwable e132) {
            Log.m284c("RiskDataCollector", e132.getMessage(), e132);
        }
        try {
            x = fraudDataProvider.m738A(-1);
            if (x > 99) {
                x = 99;
            }
            str = String.valueOf(x);
            Log.m285d("RiskDataCollector", "getRiskData: distinctNames = " + str);
            if (!(str == null || str.isEmpty())) {
                arrayList.add(new RiskDataParam(ActivationData.DISTINCT_CARD_HOLDER_NAMES, str));
            }
        } catch (Throwable e1322) {
            Log.m284c("RiskDataCollector", e1322.getMessage(), e1322);
        }
        Location lastKnownLocation = DeviceInfo.getLastKnownLocation(context);
        Geocoder geocoder = new Geocoder(context);
        if (!(lastKnownLocation == null || geocoder == null)) {
            try {
                List fromLocation = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                if (!(fromLocation == null || fromLocation.isEmpty())) {
                    str = ((Address) fromLocation.get(0)).getCountryCode();
                    Log.m285d("RiskDataCollector", "getRiskData: deviceCountry = " + str);
                    if (str != null && str.length() == 2) {
                        arrayList.add(new RiskDataParam(ActivationData.DEVICE_COUNTRY_CODE, str));
                    }
                }
            } catch (Throwable e13222) {
                Log.m284c("RiskDataCollector", e13222.getMessage(), e13222);
            }
        }
        str = String.valueOf((int) Math.round(fraudDataProvider.m741D(0)));
        Log.m285d("RiskDataCollector", "getRiskData: accountScore = " + str);
        if (!(str == null || str.isEmpty())) {
            arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_SCORE, str));
        }
        str = String.valueOf(fraudDataProvider.m742E(0).nk);
        Log.m285d("RiskDataCollector", "getRiskData: deviceScore = " + str);
        if (!(str == null || str.isEmpty())) {
            arrayList.add(new RiskDataParam(ActivationData.DEVICE_SCORE, str));
        }
        return arrayList;
    }

    private static String aV(String str) {
        if (str == null || str.isEmpty() || str.length() != 2) {
            return str;
        }
        return "20" + str;
    }
}
