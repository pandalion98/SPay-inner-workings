package android.net.wifi;

import android.net.ProxyInfo;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.os.SystemProperties;
import android.util.Log;
import com.samsung.android.feature.FloatingFeature;
import com.sec.android.app.CscFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

public class WifiApCust {
    private static final boolean DBG;
    private static final String LOGFILE = "/data/misc/wifi_hostapd/mhs.log";
    public static final String TAG = "WifiApCust";
    public static int mDefaultMaxClientNum = CscFeature.getInstance().getInteger("CscFeature_Wifi_MaxClient4MobileAp", 10);
    public static String mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "SamsungDefault");
    public static String mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "Default");
    public static String mDefaultSecurity = "WPA2PSK";
    public static boolean mDefaultShowPassword = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_EnableShowPasswordAsDefault", false);
    public static int mDefaultTimeOut = CscFeature.getInstance().getInteger("CscFeature_Wifi_ConfigMobileApDefaultTimeOut", Process.SPAY_UID);
    public static String mMHSCustomer = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigOpBrandingForMobileAp", "ALL");
    public static boolean mModemPowerBackOff = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApOnTrigger", false);
    public static boolean mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", false);
    public static boolean mSupportMaxClientMenu = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMenuMobileApMaxClient", false);
    public static boolean mSupportWPSPBC = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApWPSPBC", false);
    public static boolean mSupportWPSPIN = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApWPSPIN", false);
    private static WifiApCust sInstance = null;

    static {
        boolean z = true;
        if (!"eng".equals(Build.TYPE) && Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public static void setVZW() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "VZWRandomRule");
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "Verizon-,ModelName,-,Mac4Digits");
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setTMO() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "UserDefined");
        mDefaultTimeOut = CscFeature.getInstance().getInteger("CscFeature_Wifi_ConfigMobileApDefaultTimeOut", CalendarColumns.CAL_ACCESS_EDITOR);
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "Samsung,Space,BrandName,Space,IMEILast4Digits");
        mDefaultMaxClientNum = CscFeature.getInstance().getInteger("CscFeature_Wifi_MaxClient4MobileAp", 8);
        mDefaultShowPassword = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_EnableShowPasswordAsDefault", true);
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setATT() {
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "BrandName,Space,Random4Digits");
        mDefaultTimeOut = CscFeature.getInstance().getInteger("CscFeature_Wifi_ConfigMobileApDefaultTimeOut", CalendarColumns.CAL_ACCESS_EDITOR);
        mDefaultShowPassword = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_EnableShowPasswordAsDefault", true);
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setSPR() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "Min10Digits");
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "ChameleonSSID,Mac3Digits");
        mDefaultTimeOut = CscFeature.getInstance().getInteger("CscFeature_Wifi_ConfigMobileApDefaultTimeOut", CalendarColumns.CAL_ACCESS_EDITOR);
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setUSC() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "Min10Digits");
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "BrandName,Space,IMEILast4Digits");
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setLGT() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "ModelWith4RandomDigits");
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "AndroidHotspot,Min4Digits");
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setSKT() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "None");
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "AndroidHotspot,Min4Digits");
        mSupport5G = CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G", true);
    }

    public static void setACG() {
        mDefaultPassword = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd", "Min10Digits");
    }

    public static void setCHC() {
        mDefaultSSID = CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID", "Default,Mac4Digits");
    }

    private WifiApCust() {
    }

    public static WifiApCust getInstance() {
        if (sInstance == null) {
            Log.w(TAG, " getInstance() sInstance is null");
            if (DBG) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
            sInstance = new WifiApCust();
        }
        return sInstance;
    }

    public static void setDefault() {
        Log.w(TAG, " setDefault()");
    }

    public static void setDefaultAsCustomer() {
        Log.w(TAG, " setDefaultAsCustomer() :" + mMHSCustomer);
        if (mMHSCustomer.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            mMHSCustomer = readSalesCode();
            Log.w(TAG, " from salescode setDefaultAsCustomer() :" + mMHSCustomer);
        }
        if ("VZW".equals(mMHSCustomer)) {
            setVZW();
        } else if ("SPR".equals(mMHSCustomer) || "SPRINT".equals(mMHSCustomer) || "XAS".equals(mMHSCustomer) || "VMU".equals(mMHSCustomer) || "BST".equals(mMHSCustomer)) {
            mMHSCustomer = "SPRINT";
            setSPR();
        } else if ("TMO".equals(mMHSCustomer) || "TMB".equals(mMHSCustomer)) {
            mMHSCustomer = "TMO";
            setTMO();
        } else if ("ATT".equals(mMHSCustomer)) {
            setATT();
        } else if ("USC".equals(mMHSCustomer)) {
            setUSC();
        } else if ("SKC".equals(mMHSCustomer) || "KTC".equals(mMHSCustomer)) {
            setSKT();
        } else if ("LUC".equals(mMHSCustomer)) {
            setLGT();
        } else if ("CHM".equals(mMHSCustomer)) {
            mMHSCustomer = "CMCC";
        } else if ("CHC".equals(mMHSCustomer)) {
            setCHC();
        } else {
            mMHSCustomer = "ALL";
        }
    }

    public static void getCSC() {
        setDefaultAsCustomer();
    }

    public static int getTestProp() {
        String str = SystemProperties.get("mhs.test");
        Log.w(TAG, " getTestProp() mhs.test:" + str);
        if (str.equals(ProxyInfo.LOCAL_EXCL_LIST) || str == null) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    public static String showCSCvalues() {
        StringBuffer value = new StringBuffer();
        value.append("default set value\n");
        if (DBG) {
            value.append("CONFIGOPBRANDINGFORMOBILEAP=" + mMHSCustomer).append("\n");
            value.append("ENABLESHOWPASSWORDASDEFAULT=" + mDefaultShowPassword).append("\n");
            value.append("SUPPORTMOBILEAP5G=" + mSupport5G).append("\n");
            value.append("MAXCLIENT4MOBILEAP=" + mDefaultMaxClientNum).append("\n");
            value.append("SUPPORTMOBILEAPONTRIGGER=" + mModemPowerBackOff).append("\n");
            value.append("SUPPORTMOBILEAPWPSPBC=" + mSupportWPSPBC).append("\n");
            value.append("SUPPORTMOBILEAPWPSPIN=" + mSupportWPSPIN).append("\n");
            value.append("CONFIGMOBILEAPDEFAULTTIMEOUT=" + mDefaultTimeOut).append("\n");
            value.append("SUPPORTMENUMOBILEAPMAXCLIENT=" + mSupportMaxClientMenu).append("\n");
            value.append("CONFIGMOBILEAPDEFAULTSSID=" + mDefaultSSID).append("\n");
            value.append("CONFIGMOBILEAPDEFAULTPWD=" + mDefaultPassword).append("\n");
        } else {
            value.append("OPBRANDING=" + mMHSCustomer).append("\n");
            value.append("SHOWPASSWORD=" + mDefaultShowPassword).append("\n");
            value.append("MOBILEAP5G=" + mSupport5G).append("\n");
            value.append("MAXCLIENT=" + mDefaultMaxClientNum).append("\n");
            value.append("ONTRIGGER=" + mModemPowerBackOff).append("\n");
            value.append("PBC=" + mSupportWPSPBC).append("\n");
            value.append("PIN=" + mSupportWPSPIN).append("\n");
            value.append("TIMEOUT=" + mDefaultTimeOut).append("\n");
            value.append("MENUMAXCLIENT=" + mSupportMaxClientMenu).append("\n");
            value.append("SSID=" + mDefaultSSID).append("\n");
            value.append("PWD=" + mDefaultPassword).append("\n");
        }
        return value.toString();
    }

    public static String getCSCFile() {
        StringBuffer value = new StringBuffer();
        value.append("csc file info\n");
        if (DBG) {
            value.append("CONFIGOPBRANDINGFORMOBILEAP=" + CscFeature.getInstance().getString("CscFeature_Wifi_ConfigOpBrandingForMobileAp")).append("\n");
            value.append("ENABLESHOWPASSWORDASDEFAULT=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_EnableShowPasswordAsDefault")).append("\n");
            value.append("SUPPORTMOBILEAP5G=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G")).append("\n");
            value.append("MAXCLIENT4MOBILEAP=" + CscFeature.getInstance().getInteger("CscFeature_Wifi_MaxClient4MobileAp")).append("\n");
            value.append("SUPPORTMOBILEAPONTRIGGER=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApOnTrigger")).append("\n");
            value.append("SUPPORTMOBILEAPWPSPBC=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApWPSPBC")).append("\n");
            value.append("SUPPORTMOBILEAPWPSPIN=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApWPSPIN")).append("\n");
            value.append("CONFIGMOBILEAPDEFAULTTIMEOUT=" + CscFeature.getInstance().getInteger("CscFeature_Wifi_ConfigMobileApDefaultTimeOut")).append("\n");
            value.append("SUPPORTMENUMOBILEAPMAXCLIENT=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMenuMobileApMaxClient")).append("\n");
            value.append("CONFIGMOBILEAPDEFAULTSSID=" + CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID")).append("\n");
            value.append("CONFIGMOBILEAPDEFAULTPWD=" + CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd")).append("\n");
        } else {
            value.append("OPBRANDING=" + CscFeature.getInstance().getString("CscFeature_Wifi_ConfigOpBrandingForMobileAp")).append("\n");
            value.append("SHOWPASSWORD=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_EnableShowPasswordAsDefault")).append("\n");
            value.append("MOBILEAP5G=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileAp5G")).append("\n");
            value.append("MAXCLIENT=" + CscFeature.getInstance().getInteger("CscFeature_Wifi_MaxClient4MobileAp")).append("\n");
            value.append("ONTRIGGER=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApOnTrigger")).append("\n");
            value.append("PBC=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApWPSPBC")).append("\n");
            value.append("PIN=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMobileApWPSPIN")).append("\n");
            value.append("TIMEOUT=" + CscFeature.getInstance().getInteger("CscFeature_Wifi_ConfigMobileApDefaultTimeOut")).append("\n");
            value.append("MENUMAXCLIENT=" + CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportMenuMobileApMaxClient")).append("\n");
            value.append("SSID=" + CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultSSID")).append("\n");
            value.append("PWD=" + CscFeature.getInstance().getString("CscFeature_Wifi_ConfigMobileApDefaultPwd")).append("\n");
        }
        return value.toString();
    }

    public static String showMoreInfo() {
        StringBuffer value = new StringBuffer();
        value.append("More Info\n");
        if (DBG) {
            value.append("readSalesCode=" + readSalesCode() + "\n");
            value.append("SEC_FLOATING_FEATURE_SETTINGS_CONFIG_BRAND_NAME=" + FloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_SETTINGS_CONFIG_BRAND_NAME")).append("\n");
        } else {
            value.append("SalesCode:" + CscFeature.getInstance().getString("SalesCode") + "\n");
            value.append("SFF BRAND_NAME=" + FloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_SETTINGS_CONFIG_BRAND_NAME")).append("\n");
        }
        return value.toString();
    }

    public static String readSalesCode() {
        return CscFeature.getInstance().getString("SalesCode");
    }

    public void dump(PrintWriter pw) {
        Log.i(TAG, "WifiApCust dump()");
        pw.println("WifiApCust dump:");
        pw.println(getCSCFile());
        pw.println(showCSCvalues());
        pw.println(showMoreInfo());
        pw.println("mhs.logtofile - start -");
        if (DBG) {
            loadLogFile(pw);
        } else {
            pw.println("DBG:" + DBG + " eng:" + "eng".equals(Build.TYPE) + " productship:" + Debug.isProductShip());
        }
        pw.println("mhs.logtofile - end -");
    }

    public static String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
    }

    public static void logToFile(String log) {
        IOException e1;
        if (DBG) {
            Log.i(TAG, "logToFile :" + log);
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(new File(LOGFILE), true);
                try {
                    fos2.write((getDateTime() + " " + log + "\r\n").getBytes(Charset.forName("UTF-8")));
                    fos2.close();
                    fos = fos2;
                } catch (IOException e) {
                    e1 = e;
                    fos = fos2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e2) {
                            Log.i(TAG, "logToFile e2:", e2);
                        }
                    }
                    Log.i(TAG, "logToFile e1:", e1);
                }
            } catch (IOException e3) {
                e1 = e3;
                if (fos != null) {
                    fos.close();
                }
                Log.i(TAG, "logToFile e1:", e1);
            }
        }
    }

    public static String loadLogFile(PrintWriter pw) {
        Exception e;
        Throwable th;
        if (!DBG) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        BufferedReader bufferedReader = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(LOGFILE));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    pw.println(line);
                } catch (Exception e2) {
                    e = e2;
                    bufferedReader = br;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = br;
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e3) {
                    bufferedReader = br;
                }
            }
            bufferedReader = br;
        } catch (Exception e4) {
            e = e4;
            try {
                e.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                    }
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
        return null;
    }
}
