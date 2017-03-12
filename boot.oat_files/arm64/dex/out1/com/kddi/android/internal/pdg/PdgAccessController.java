package com.kddi.android.internal.pdg;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.PhoneConstants;
import com.kddi.android.internal.pdg.PdgStatusManager.PrivacyData;

public class PdgAccessController {
    private static final String AU_MARKET_PACKAGE = "com.kddi.market";
    private static final int DATA_TYPE_UNKNOWN = -1;
    private static final String DIALOG_PACKAGE_NAME = "com.kddi.android.pdg";
    private long mAccessTime;
    private Context mContext = null;
    private int mDataType;
    private boolean mDefaultPrivacy;
    private String mPackageName;
    private boolean mPrivacyEnable;

    public PdgAccessController(Context context, String packageName) {
        PdgLog.d("PdgAccessController( " + context + " ) start");
        this.mContext = context;
        this.mPackageName = packageName;
        PdgLog.d("PdgAccessController() end");
    }

    public final boolean getPrivacy(int requestType) {
        PdgLog.d("boolean getPrivacy( " + requestType + " ) start");
        if (this.mContext == null) {
            PdgLog.d("boolean getPrivacy() end / return = true / context = null");
            return true;
        } else if (this.mPackageName.isEmpty()) {
            PdgLog.d("boolean getPrivacy() end / return = true / package name empty");
            return true;
        } else {
            PackageManager pm = this.mContext.getPackageManager();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(this.mPackageName, 0);
                if (ai == null || (ai.flags & 1) == 1) {
                    PdgLog.d("boolean getPrivacy() end / return = true / ai = null or flag system");
                    return true;
                } else if (AU_MARKET_PACKAGE.equals(pm.getInstallerPackageName(ai.packageName))) {
                    PdgLog.d("boolean getPrivacy() end / return = true / au Market app");
                    return true;
                } else {
                    this.mDataType = getDataType(requestType);
                    if (this.mDataType == -1) {
                        PdgLog.d("boolean getPrivacy() end / return = true / data tyep unknown");
                        return true;
                    } else if (PdgWhiteListManager.contain(this.mContext, this.mPackageName)) {
                        PdgLog.d("boolean getPrivacy() end / return = true / white list match");
                        return true;
                    } else {
                        if (PdgSettingManager.getFirstLaunch(this.mContext.getApplicationContext()) == 0) {
                            sendFirstAccess(this.mContext);
                        }
                        this.mAccessTime = System.currentTimeMillis();
                        this.mDefaultPrivacy = getDefaultPrivacy();
                        this.mPrivacyEnable = getPrivacyEnable();
                        if (this.mPrivacyEnable) {
                            PrivacyData privacyData = PdgStatusManager.getPrivacyData(this.mContext, this.mPackageName, this.mDataType);
                            if (privacyData == null || !(privacyData.getSettingState() == 1 || privacyData.getSettingState() == 2)) {
                                if (isCallActive(this.mContext)) {
                                    boolean defaultPrivacy = this.mDefaultPrivacy;
                                    sendUpdatePdgStatus(this.mContext, this.mPackageName, this.mDataType, this.mAccessTime);
                                    sendShowNotification(this.mContext, this.mPackageName, this.mDataType, this.mAccessTime);
                                    PdgLog.d("boolean getPrivacy() end / calll active / defaultPrivacy = " + defaultPrivacy);
                                    return defaultPrivacy;
                                }
                                boolean result = getSelectedPrivacy();
                                if (sendShowDialog(this.mContext, this.mPackageName, this.mDataType, this.mAccessTime, result)) {
                                    PdgLog.d("boolean getPrivacy() end / return = " + result);
                                    return result;
                                }
                                PdgLog.d("boolean getPrivacy() end / return = true / dialog show failed ");
                                return true;
                            } else if (privacyData.getAuthState() == 1) {
                                sendUpdatePdgStatus(this.mContext, this.mPackageName, this.mDataType, this.mAccessTime);
                                PdgLog.d("boolean getPrivacy() end / return = true / state configured");
                                return true;
                            } else if (privacyData.getAuthState() == 2) {
                                sendUpdatePdgStatus(this.mContext, this.mPackageName, this.mDataType, this.mAccessTime);
                                PdgLog.d("boolean getPrivacy() end / return = false / state configured");
                                return false;
                            } else {
                                PdgLog.e("pdg settingState error !!!");
                                return true;
                            }
                        }
                        PdgLog.d("boolean getPrivacy() end / return = true / privacy disable");
                        return true;
                    }
                }
            } catch (NameNotFoundException e) {
                PdgLog.d("boolean getPrivacy() end / return = true / NameNotFoundException");
                return true;
            }
        }
    }

    private int getDataType(int requestType) {
        PdgLog.d("int getDataType( " + requestType + " ) start");
        int result = -1;
        switch (requestType) {
            case 0:
            case 1:
            case 2:
                result = requestType;
                break;
        }
        PdgLog.d("int getDataType() end / return = " + result);
        return result;
    }

    private boolean getPrivacyEnable() {
        PdgLog.d("boolean getPrivacyEnable() start");
        boolean result = PdgSettingManager.getBoolean(this.mContext, 0);
        PdgLog.d("boolean getPrivacyEnable() end / return = " + result);
        return result;
    }

    private boolean getDefaultPrivacy() {
        PdgLog.d("boolean getDefaultPrivacy() start");
        boolean result = PdgSettingManager.getBoolean(this.mContext, 1);
        PdgLog.d("boolean getDefaultPrivacy() end / return = " + result);
        return result;
    }

    private boolean getSelectedPrivacy() {
        PdgLog.d("boolean getSelectedPrivacy() start");
        boolean privacyEnable = this.mPrivacyEnable;
        boolean defaultPrivacy = this.mDefaultPrivacy;
        if (privacyEnable) {
            PdgLog.d("boolean getSelectedPrivacy() end / return = " + defaultPrivacy);
            return defaultPrivacy;
        }
        PdgLog.d("boolean getSelectedPrivacy() end / return = true");
        return true;
    }

    private boolean sendShowDialog(Context context, String packageName, int dataTyepe, long accessTime, boolean selectedPrivacy) {
        PdgLog.d("boolean sendShowDialog( " + context + ", " + packageName + ", " + dataTyepe + ", " + accessTime + " ) start");
        Intent intent = new Intent();
        intent.setAction(PdgIntent.ACTION_SHOW_PDG_DIALOG);
        intent.setPackage("com.kddi.android.pdg");
        intent.putExtra("packagename", packageName);
        intent.putExtra(PdgIntent.EXTRA_DATA_TYPE, dataTyepe);
        intent.putExtra(PdgIntent.EXTRA_ACCESS_TIME, accessTime);
        intent.putExtra(PdgIntent.EXTRA_CURRENT_PRIVACY, selectedPrivacy);
        try {
            context.sendBroadcast(intent);
            PdgLog.d("boolean sendShowDialog() end / return = true");
            return true;
        } catch (Exception e) {
            PdgLog.e(e.toString());
            PdgLog.d("boolean sendShowDialog() end / return = false");
            return false;
        }
    }

    private void sendUpdatePdgStatus(Context context, String packageName, int dataTyepe, long accessTime) {
        PdgLog.d("void sendUpdatePdgStatus() start");
        Intent intent = new Intent();
        intent.setAction(PdgIntent.ACTION_UPDATE_PDG_STATUS);
        intent.setPackage("com.kddi.android.pdg");
        intent.putExtra("packagename", packageName);
        intent.putExtra(PdgIntent.EXTRA_DATA_TYPE, dataTyepe);
        intent.putExtra(PdgIntent.EXTRA_ACCESS_TIME, accessTime);
        context.sendBroadcast(intent);
        PdgLog.d("void sendUpdatePdgStatus() end");
    }

    private void sendShowNotification(Context context, String packageName, int dataTyepe, long accessTime) {
        PdgLog.d("void sendShowNotification() start");
        Intent intent = new Intent();
        intent.setAction(PdgIntent.ACTION_SHOW_PDG_NOTIFICATION);
        intent.setPackage("com.kddi.android.pdg");
        intent.putExtra("packagename", packageName);
        intent.putExtra(PdgIntent.EXTRA_DATA_TYPE, dataTyepe);
        intent.putExtra(PdgIntent.EXTRA_ACCESS_TIME, accessTime);
        context.sendBroadcast(intent);
        PdgLog.d("void sendShowNotification() end");
    }

    private void sendFirstAccess(Context context) {
        PdgLog.d("void sendFirstAccess() start");
        Intent intent = new Intent();
        intent.setAction(PdgIntent.ACTION_FIRST_ACCESS);
        intent.setPackage("com.kddi.android.pdg");
        context.sendBroadcast(intent);
        PdgLog.d("void sendFirstAccess() end");
    }

    private boolean isCallActive(Context context) {
        PdgLog.d("void isCallActive() start");
        int state = ((TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY)).getCallState();
        if (state == 2 || state == 1) {
            PdgLog.d("void isCallActive() end / true state = " + state);
            return true;
        }
        PdgLog.d("void isCallActive() end / false state = " + state);
        return false;
    }
}
