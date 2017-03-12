package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class PhoneRestrictionPolicy {
    public static final String ACTION_PHONE_READY = "edm.intent.action.PHONE_READY";
    public static final String ACTION_SEND_BLOCKED_MMS = "edm.intent.action.internal.RESTRICTION_SEND_BLOCKED_MMS";
    public static final String ACTION_SEND_BLOCKED_SMS = "edm.intent.action.internal.RESTRICTION_SEND_BLOCKED_SMS";
    public static final String EXTRA_ORIG_ADDRESS = "extra_orig_address";
    public static final String EXTRA_PDU = "extra_pdu";
    public static final String EXTRA_SEND_TYPE = "send_type";
    public static final String EXTRA_TIME_STAMP = "extra_time_stamp";
    public static final String ICCID_AVAILABLE = "com.android.server.enterprise.ICCID_AVAILABLE";
    public static final String PERMISSION_RECEIVE_BLOCKED_SMS_MMS = "android.permission.sec.RECEIVE_BLOCKED_SMS_MMS";
    public static final int SENDTYPE_GENERIC = -1;
    private static String TAG = SecContentProviderURI.PHONERESTRICTIONPOLICY;

    public boolean canIncomingCall(String dialNumber) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.canIncomingCall(dialNumber);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-canIncomingCall returning default value");
        }
        return true;
    }

    public boolean canOutgoingCall(String dialNumber) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.canOutgoingCall(dialNumber);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-canOutgoingCall returning default value");
        }
        return true;
    }

    public boolean getEmergencyCallOnly(boolean allAdmins) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getEmergencyCallOnly(allAdmins);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getEmergencyCallOnly returning default value");
        }
        return false;
    }

    public boolean addNumberOfIncomingCalls() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.addNumberOfIncomingCalls();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-addNumberOfIncomingCalls returning default value");
        }
        return false;
    }

    public boolean addNumberOfOutgoingCalls() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.addNumberOfOutgoingCalls();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-addNumberOfOutgoingCalls returning default value");
        }
        return false;
    }

    public boolean isLimitNumberOfSmsEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isLimitNumberOfSmsEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isLimitNumberOfSmsEnabled returning default value");
        }
        return false;
    }

    public boolean addNumberOfIncomingSms() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.addNumberOfIncomingSms();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-addNumberOfIncomingSms returning default value");
        }
        return false;
    }

    public boolean addNumberOfOutgoingSms() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.addNumberOfOutgoingSms();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-addNumberOfOutgoingSms returning default value");
        }
        return false;
    }

    public boolean decreaseNumberOfOutgoingSms() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.decreaseNumberOfOutgoingSms();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-decreaseNumberOfOutgoingSms returning default value");
        }
        return false;
    }

    public boolean canOutgoingSms(String phoneNumber) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.canOutgoingSms(phoneNumber);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-canOutgoingSms returning default value");
        }
        return true;
    }

    public boolean canIncomingSms(String phoneNumber) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.canIncomingSms(phoneNumber);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-canIncomingSms returning default value");
        }
        return true;
    }

    public boolean isIncomingSmsAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isIncomingSmsAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isIncomingSmsAllowed returning default value");
        }
        return true;
    }

    public boolean isOutgoingSmsAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isOutgoingSmsAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isOutgoingSmsAllowed returning default value");
        }
        return true;
    }

    public boolean isIncomingMmsAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isIncomingMmsAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isIncomingMmsAllowed returning default value");
        }
        return true;
    }

    public boolean isBlockSmsWithStorageEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBlockSmsWithStorageEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isBlockSmsWithStorageEnabled returning default value", e);
        }
        return false;
    }

    public boolean isBlockMmsWithStorageEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBlockMmsWithStorageEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isBlockMmsWithStorageEnabled returning default value", e);
        }
        return false;
    }

    public void storeBlockedSmsMms(boolean isSms, byte[] pdu, String srcAddress, int sendType, String timeStamp) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.storeBlockedSmsMms(isSms, pdu, srcAddress, sendType, timeStamp);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-storeBlockedSmsMms fail to save sms/mms");
        }
    }

    public boolean isWapPushAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isWapPushAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isWapPushAllowed returning default value");
        }
        return true;
    }

    public boolean isSimLockedByAdmin(String iccId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isSimLockedByAdmin(iccId);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isSimLockedByAdmin returning default value");
        }
        return false;
    }

    public boolean isCopyContactToSimAllowed(int message) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isCopyContactToSimAllowed(message);
            }
        } catch (Exception e) {
            Log.w(TAG, "PXY-isCopyContactToSimAllowed returning default value ", e);
        }
        return true;
    }
}
