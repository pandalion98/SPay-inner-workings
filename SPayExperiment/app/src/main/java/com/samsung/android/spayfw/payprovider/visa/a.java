/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.location.Address
 *  android.location.Geocoder
 *  android.location.Location
 *  android.provider.Settings
 *  android.provider.Settings$Global
 *  android.provider.Settings$SettingNotFoundException
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IndexOutOfBoundsException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.payprovider.visa;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.fraud.b;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.h;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class a {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ArrayList<RiskDataParam> a(Context context, ProvisionTokenInfo provisionTokenInfo, d d2) {
        ArrayList arrayList = new ArrayList();
        Log.d("RiskDataCollector", "getRiskData: tokenInfo = " + provisionTokenInfo);
        if (provisionTokenInfo != null && provisionTokenInfo.getActivationParams() != null) {
            String string = (String)provisionTokenInfo.getActivationParams().get((Object)"cvv2");
            String string2 = (String)provisionTokenInfo.getActivationParams().get((Object)"billingZip");
            String string3 = (String)provisionTokenInfo.getActivationParams().get((Object)"line1");
            String string4 = (String)provisionTokenInfo.getActivationParams().get((Object)"daysSinceConsumerDataLastAccountChange");
            String string5 = (String)provisionTokenInfo.getActivationParams().get((Object)"walletAccountFirstCreated");
            String string6 = (String)provisionTokenInfo.getActivationParams().get((Object)"userAccountFirstCreated");
            String string7 = (String)provisionTokenInfo.getActivationParams().get((Object)"walletAccountHolderCardNameMatch");
            String string8 = (String)provisionTokenInfo.getActivationParams().get((Object)"accountHolderName");
            String string9 = (String)provisionTokenInfo.getActivationParams().get((Object)"accountToDeviceBindingAge");
            String string10 = (String)provisionTokenInfo.getActivationParams().get((Object)"walletAccountCountry");
            String string11 = (String)provisionTokenInfo.getActivationParams().get((Object)"suspendedCardsInAccount");
            String string12 = (String)provisionTokenInfo.getActivationParams().get((Object)"daysSinceLastAccountActivity");
            String string13 = (String)provisionTokenInfo.getActivationParams().get((Object)"numberOfTransactionsInLast12months");
            String string14 = (String)provisionTokenInfo.getActivationParams().get((Object)"numberOfActiveTokens");
            String string15 = (String)provisionTokenInfo.getActivationParams().get((Object)"deviceWithActiveTokens");
            String string16 = (String)provisionTokenInfo.getActivationParams().get((Object)"activeTokenOnAllDevicesForAccount");
            String string17 = (String)provisionTokenInfo.getActivationParams().get((Object)"billingCountryCode");
            String string18 = (String)provisionTokenInfo.getActivationParams().get((Object)"month");
            String string19 = (String)provisionTokenInfo.getActivationParams().get((Object)"year");
            if (h.DEBUG) {
                Log.d("RiskDataCollector", "getRiskData: cvv = " + string + "; zip = " + string2 + "; line1 = " + string3 + "; sinceAccSettingsChanged = " + string4 + "; walletAccFirstCreated = " + string5 + "; userAccFirstCreated = " + string6 + "; walletAccHolderCardNameMatch = " + string7 + "; accHolderName = " + string8 + "; accToDevBindingAge = " + string9 + "; walletAccCountry = " + string10 + "; susCardsInAcc = " + string11 + "; sinceLastAccActivity = " + string12 + "; numTransacLast12Months = " + string13 + "; numActiveTokens = " + string14 + "; devWithActiveTokens = " + string15 + "; activeTokOnAllDevicesForAcc" + string16 + "; billingCountryCode = " + string17 + "; expirationDateMM = " + string18 + "; expirationDateYY = " + string19);
            }
            if (string != null && string.length() > 0 && string.length() <= 4 && TextUtils.isDigitsOnly((CharSequence)string)) {
                if (d2 != null) {
                    byte[] arrby = d2.aY(string);
                    if (arrby != null && arrby.length > 0) {
                        String string20 = new String(arrby);
                        arrayList.add((Object)new RiskDataParam("encCvv2", string20));
                        Log.d("RiskDataCollector", "Encrypted cvv added to risk data: length " + arrby.length);
                    } else {
                        Log.e("RiskDataCollector", "unable to get Encrypted cvv for risk data");
                    }
                }
                h.clearMemory(string);
            }
            if (string2 != null && string2.length() >= 3 && string2.length() <= 16) {
                arrayList.add((Object)new RiskDataParam("billingZip", string2));
            }
            if (string3 != null && string3.length() > 0) {
                try {
                    if (string3.length() > 32) {
                        string3 = string3.substring(0, 32);
                    }
                    arrayList.add((Object)new RiskDataParam("line1", string3));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    Log.d("RiskDataCollector", "getRiskData: line1 incorrect format!");
                }
            }
            if (string4 != null && string4.length() > 0 && string4.length() <= 4) {
                try {
                    if (Integer.valueOf((String)string4) > 9999) {
                        string4 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("daysSinceConsumerDataLastAccountChange", string4));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: sinceAccSettingsChanged incorrect format!");
                }
            }
            if (string5 != null && string5.length() > 0 && string5.length() <= 4) {
                try {
                    if (Integer.valueOf((String)string5) > 9999) {
                        string5 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("walletAccountFirstCreated", string5));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: walletAccFirstCreated incorrect format!");
                }
            }
            if (string6 != null && string6.length() > 0 && string6.length() <= 4 && TextUtils.isDigitsOnly((CharSequence)string6)) {
                try {
                    if (Integer.valueOf((String)string6) > 9999) {
                        string6 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("userAccountFirstCreated", string6));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: userAccFirstCreated incorrect format!");
                }
            }
            if (string7 != null && string7.length() == 1 && (string7.equals((Object)"Y") || string7.equals((Object)"N"))) {
                arrayList.add((Object)new RiskDataParam("walletAccountHolderCardNameMatch", string7));
            }
            if (string8 != null && !string8.isEmpty()) {
                try {
                    if (string8.length() > 64) {
                        string8 = string8.substring(0, 64);
                    }
                    arrayList.add((Object)new RiskDataParam("accountHolderName", string8));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    Log.d("RiskDataCollector", "getRiskData: accHolderName incorrect format!");
                }
            }
            if (string9 != null && string9.length() > 0 && string9.length() <= 4) {
                try {
                    if (Integer.valueOf((String)string9) > 9999) {
                        string9 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("accountToDeviceBindingAge", string9));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: accToDevBindingAge incorrect format!");
                }
            }
            if (string10 != null && string10.length() == 2) {
                arrayList.add((Object)new RiskDataParam("walletAccountCountry", string10));
            }
            if (string11 != null && string11.length() > 0 && string11.length() <= 2) {
                try {
                    if (Integer.valueOf((String)string11) > 99) {
                        string11 = "99";
                    }
                    arrayList.add((Object)new RiskDataParam("suspendedCardsInAccount", string11));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: susCardsInAcc incorrect format!");
                }
            }
            if (string12 != null && string12.length() > 0 && string12.length() <= 4) {
                try {
                    if (Integer.valueOf((String)string12) > 9999) {
                        string12 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("daysSinceLastAccountActivity", string12));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: sinceLastAccActivity incorrect format!");
                }
            }
            if (string13 != null && string13.length() > 0 && string13.length() <= 4) {
                try {
                    if (Integer.valueOf((String)string13) > 9999) {
                        string13 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("numberOfTransactionsInLast12months", string13));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: numTransacLast12Months incorrect format!");
                }
            }
            if (string14 != null && string14.length() > 0 && string14.length() <= 2) {
                try {
                    if (Integer.valueOf((String)string14) > 99) {
                        string14 = "99";
                    }
                    arrayList.add((Object)new RiskDataParam("numberOfActiveTokens", string14));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: numActiveTokens incorrect format!");
                }
            }
            if (string15 != null && string15.length() > 0 && string15.length() <= 2) {
                try {
                    if (Integer.valueOf((String)string15) > 99) {
                        string15 = "99";
                    }
                    arrayList.add((Object)new RiskDataParam("deviceWithActiveTokens", string15));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: devWithActiveTokens incorrect format!");
                }
            }
            if (string16 != null && string16.length() > 0 && string16.length() <= 4) {
                try {
                    if (Integer.valueOf((String)string16) > 9999) {
                        string16 = "9999";
                    }
                    arrayList.add((Object)new RiskDataParam("activeTokenOnAllDevicesForAccount", string16));
                }
                catch (NumberFormatException numberFormatException) {
                    Log.d("RiskDataCollector", "getRiskData: activeTokOnAllDevicesForAcc incorrect format!");
                }
            }
            if (string17 != null && string17.length() == 2) {
                arrayList.add((Object)new RiskDataParam("billingCountryCode", string17));
            }
            if (string18 != null && !string18.isEmpty() && string19 != null && !string19.isEmpty()) {
                Log.d("RiskDataCollector", "getRiskData: adding expiration month & year");
                arrayList.add((Object)new RiskDataParam("paymentInstrument.expirationDate.month", string18));
                arrayList.add((Object)new RiskDataParam("paymentInstrument.expirationDate.year", a.aV(string19)));
            }
        }
        String string = DeviceInfo.getDeviceSerialNumber();
        Log.d("RiskDataCollector", "getRiskData: devSerialNumber = " + string);
        if (string != null) {
            Log.d("RiskDataCollector", "getRiskData: devSerialNumber.len = " + string.length());
        }
        if (string != null && string.length() > 0 && string.length() <= 24) {
            arrayList.add((Object)new RiskDataParam("deviceSerialNumber", string));
        }
        String string21 = DeviceInfo.getDeviceImei(context);
        Log.d("RiskDataCollector", "getRiskData: imei = " + string21);
        if (string21 != null) {
            Log.d("RiskDataCollector", "getRiskData: imei.len = " + string21.length());
        }
        if (string21 != null && string21.length() > 0 && string21.length() <= 24) {
            arrayList.add((Object)new RiskDataParam("deviceIMEI", string21));
        }
        String string22 = DeviceInfo.getDeviceBluetoothMac();
        Log.d("RiskDataCollector", "getRiskData: deviceBluetoothMac = " + string22);
        if (string22 != null) {
            Log.d("RiskDataCollector", "getRiskData: devBtMac.len = " + string22.length());
        }
        if (string22 != null && string22.length() > 0 && string22.length() <= 24) {
            arrayList.add((Object)new RiskDataParam("deviceBluetoothMac", string22));
        }
        String string23 = TimeZone.getDefault().getDisplayName(false, 0);
        Log.d("RiskDataCollector", "getRiskData: timeZone = " + string23);
        if (string23 != null) {
            Log.d("RiskDataCollector", "getRiskData: timeZone.len = " + string23.length());
        }
        if (string23 != null && string23.length() > 0 && string23.length() <= 5) {
            arrayList.add((Object)new RiskDataParam("deviceTimeZone", string23));
        }
        try {
            int n2 = Settings.Global.getInt((ContentResolver)context.getContentResolver(), (String)"auto_time_zone");
            String string24 = n2 == 1 ? "1" : "2";
            Log.d("RiskDataCollector", "getRiskData: deviceTimeZoneSetting = " + n2 + "; tzSetting = " + string24);
            arrayList.add((Object)new RiskDataParam("deviceTimeZoneSetting", string24));
        }
        catch (Settings.SettingNotFoundException settingNotFoundException) {
            Log.c("RiskDataCollector", settingNotFoundException.getMessage(), settingNotFoundException);
        }
        String string25 = DeviceInfo.getDeviceId(context);
        Log.d("RiskDataCollector", "getRiskData: seId = " + string25);
        if (string25 != null) {
            Log.d("RiskDataCollector", "getRiskData: seId.len = " + string25.length());
        }
        if (string25 != null && string25.length() > 0 && string25.length() <= 24) {
            arrayList.add((Object)new RiskDataParam("seID", string25));
        }
        String string26 = DeviceInfo.getAndroidId(context);
        Log.d("RiskDataCollector", "getRiskData: androidId = " + string26);
        if (string26 != null) {
            Log.d("RiskDataCollector", "getRiskData: androidId.len = " + string26.length());
        }
        if (string26 != null && string26.length() > 0 && string26.length() <= 24) {
            arrayList.add((Object)new RiskDataParam("androidId", string26));
        }
        String string27 = DeviceInfo.getSimSerialNumber(context);
        Log.d("RiskDataCollector", "getRiskData: simSerialNum = " + string27);
        if (string27 != null) {
            Log.d("RiskDataCollector", "getRiskData: simSerialNum.len = " + string27.length());
        }
        if (string27 != null && string27.length() > 0 && string27.length() <= 24) {
            arrayList.add((Object)new RiskDataParam("simSerialNumber", string27));
        }
        b b2 = new b(context);
        try {
            int n3 = b2.x(1);
            if (n3 > 99) {
                n3 = 99;
            }
            String string28 = String.valueOf((int)n3);
            Log.d("RiskDataCollector", "getRiskData: provAttempts = " + string28);
            if (string28 != null && !string28.isEmpty()) {
                arrayList.add((Object)new RiskDataParam("provisioningAttemptsOnDevicein24hours", string28));
            }
        }
        catch (Exception exception) {
            Log.c("RiskDataCollector", exception.getMessage(), exception);
        }
        try {
            int n4 = b2.A(-1);
            if (n4 > 99) {
                n4 = 99;
            }
            String string29 = String.valueOf((int)n4);
            Log.d("RiskDataCollector", "getRiskData: distinctNames = " + string29);
            if (string29 != null && !string29.isEmpty()) {
                arrayList.add((Object)new RiskDataParam("distinctCardholderNames", string29));
            }
        }
        catch (Exception exception) {
            Log.c("RiskDataCollector", exception.getMessage(), exception);
        }
        Location location = DeviceInfo.getLastKnownLocation(context);
        Geocoder geocoder = new Geocoder(context);
        if (location != null && geocoder != null) {
            try {
                List list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (list != null && !list.isEmpty()) {
                    String string30 = ((Address)list.get(0)).getCountryCode();
                    Log.d("RiskDataCollector", "getRiskData: deviceCountry = " + string30);
                    if (string30 != null && string30.length() == 2) {
                        arrayList.add((Object)new RiskDataParam("deviceCountry", string30));
                    }
                }
            }
            catch (Exception exception) {
                Log.c("RiskDataCollector", exception.getMessage(), exception);
            }
        }
        String string31 = String.valueOf((int)((int)Math.round((double)b2.D(0))));
        Log.d("RiskDataCollector", "getRiskData: accountScore = " + string31);
        if (string31 != null && !string31.isEmpty()) {
            arrayList.add((Object)new RiskDataParam("accountScore", string31));
        }
        String string32 = String.valueOf((int)b2.E((int)0).nk);
        Log.d("RiskDataCollector", "getRiskData: deviceScore = " + string32);
        if (string32 != null && !string32.isEmpty()) {
            arrayList.add((Object)new RiskDataParam("deviceScore", string32));
        }
        return arrayList;
    }

    private static String aV(String string) {
        if (string != null && !string.isEmpty() && string.length() == 2) {
            string = "20" + string;
        }
        return string;
    }
}

