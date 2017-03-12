package android.nfc.cardemulation;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcCardEmulation;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import java.util.HashMap;
import java.util.List;

public final class CardEmulation {
    public static final String ACTION_CHANGE_DEFAULT = "android.nfc.cardemulation.action.ACTION_CHANGE_DEFAULT";
    public static final String CATEGORY_OTHER = "other";
    public static final String CATEGORY_PAYMENT = "payment";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_SERVICE_COMPONENT = "component";
    public static final int SELECTION_MODE_ALWAYS_ASK = 1;
    public static final int SELECTION_MODE_ASK_IF_CONFLICT = 2;
    public static final int SELECTION_MODE_PREFER_DEFAULT = 0;
    static final String TAG = "CardEmulation";
    static HashMap<Context, CardEmulation> sCardEmus = new HashMap();
    static boolean sIsInitialized = false;
    static INfcCardEmulation sService;
    final Context mContext;

    private CardEmulation(Context context, INfcCardEmulation service) {
        this.mContext = context.getApplicationContext();
        sService = service;
    }

    public static synchronized CardEmulation getInstance(NfcAdapter adapter) {
        CardEmulation manager;
        synchronized (CardEmulation.class) {
            if (adapter == null) {
                throw new NullPointerException("NfcAdapter is null");
            }
            Context context = adapter.getContext();
            if (context == null) {
                Log.e(TAG, "NfcAdapter context is null.");
                throw new UnsupportedOperationException();
            }
            if (!sIsInitialized) {
                IPackageManager pm = ActivityThread.getPackageManager();
                if (pm == null) {
                    Log.e(TAG, "Cannot get PackageManager");
                    throw new UnsupportedOperationException();
                }
                try {
                    if (pm.hasSystemFeature("android.hardware.nfc.hce")) {
                        sIsInitialized = true;
                    } else {
                        Log.e(TAG, "This device does not support card emulation");
                        throw new UnsupportedOperationException();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "PackageManager query failed.");
                    throw new UnsupportedOperationException();
                }
            }
            manager = (CardEmulation) sCardEmus.get(context);
            if (manager == null) {
                INfcCardEmulation service = adapter.getCardEmulationService();
                if (service == null) {
                    Log.e(TAG, "This device does not implement the INfcCardEmulation interface.");
                    throw new UnsupportedOperationException();
                }
                manager = new CardEmulation(context, service);
                sCardEmus.put(context, manager);
            }
        }
        return manager;
    }

    public boolean isDefaultServiceForCategory(ComponentName service, String category) {
        boolean z = false;
        try {
            z = sService.isDefaultServiceForCategory(UserHandle.myUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.isDefaultServiceForCategory(UserHandle.myUserId(), service, category);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to recover CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean isDefaultServiceForAid(ComponentName service, String aid) {
        boolean z = false;
        try {
            z = sService.isDefaultServiceForAid(UserHandle.myUserId(), service, aid);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.isDefaultServiceForAid(UserHandle.myUserId(), service, aid);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean categoryAllowsForegroundPreference(String category) {
        if (!CATEGORY_PAYMENT.equals(category)) {
            return true;
        }
        try {
            return Secure.getInt(this.mContext.getContentResolver(), Secure.NFC_PAYMENT_FOREGROUND) != 0;
        } catch (SettingNotFoundException e) {
            return false;
        }
    }

    public int getSelectionModeForCategory(String category) {
        if (!CATEGORY_PAYMENT.equals(category)) {
            return 2;
        }
        if (Secure.getString(this.mContext.getContentResolver(), Secure.NFC_PAYMENT_DEFAULT_COMPONENT) != null) {
            return 0;
        }
        return 1;
    }

    public boolean registerAidsForService(ComponentName service, String category, List<String> aids) {
        boolean z = false;
        AidGroup aidGroup = new AidGroup((List) aids, category);
        try {
            z = sService.registerAidGroupForService(UserHandle.myUserId(), service, aidGroup);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.registerAidGroupForService(UserHandle.myUserId(), service, aidGroup);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public List<String> getAidsForService(ComponentName service, String category) {
        List<String> list = null;
        AidGroup group;
        try {
            group = sService.getAidGroupForService(UserHandle.myUserId(), service, category);
            if (group != null) {
                list = group.getAids();
            }
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    group = sService.getAidGroupForService(UserHandle.myUserId(), service, category);
                    if (group != null) {
                        list = group.getAids();
                    }
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to recover CardEmulationService.");
                }
            }
        }
        return list;
    }

    public boolean removeAidsForService(ComponentName service, String category) {
        boolean z = false;
        try {
            z = sService.removeAidGroupForService(UserHandle.myUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.removeAidGroupForService(UserHandle.myUserId(), service, category);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean setPreferredService(Activity activity, ComponentName service) {
        boolean z = false;
        if (activity == null || service == null) {
            throw new NullPointerException("activity or service or category is null");
        } else if (activity.isResumed()) {
            try {
                z = sService.setPreferredService(service);
            } catch (RemoteException e) {
                recoverService();
                if (sService == null) {
                    Log.e(TAG, "Failed to recover CardEmulationService.");
                } else {
                    try {
                        z = sService.setPreferredService(service);
                    } catch (RemoteException e2) {
                        Log.e(TAG, "Failed to reach CardEmulationService.");
                    }
                }
            }
            return z;
        } else {
            throw new IllegalArgumentException("Activity must be resumed.");
        }
    }

    public boolean unsetPreferredService(Activity activity) {
        boolean z = false;
        if (activity == null) {
            throw new NullPointerException("activity is null");
        } else if (activity.isResumed()) {
            try {
                z = sService.unsetPreferredService();
            } catch (RemoteException e) {
                recoverService();
                if (sService == null) {
                    Log.e(TAG, "Failed to recover CardEmulationService.");
                } else {
                    try {
                        z = sService.unsetPreferredService();
                    } catch (RemoteException e2) {
                        Log.e(TAG, "Failed to reach CardEmulationService.");
                    }
                }
            }
            return z;
        } else {
            throw new IllegalArgumentException("Activity must be resumed.");
        }
    }

    public boolean supportsAidPrefixRegistration() {
        boolean z = false;
        try {
            z = sService.supportsAidPrefixRegistration();
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.supportsAidPrefixRegistration();
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean setDefaultServiceForCategory(ComponentName service, String category) {
        boolean z = false;
        try {
            z = sService.setDefaultServiceForCategory(UserHandle.myUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.setDefaultServiceForCategory(UserHandle.myUserId(), service, category);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean setDefaultForNextTap(ComponentName service) {
        boolean z = false;
        try {
            z = sService.setDefaultForNextTap(UserHandle.myUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.setDefaultForNextTap(UserHandle.myUserId(), service);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public List<ApduServiceInfo> getServices(String category) {
        List<ApduServiceInfo> list = null;
        try {
            list = sService.getServices(UserHandle.myUserId(), category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    list = sService.getServices(UserHandle.myUserId(), category);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return list;
    }

    public static boolean isValidAid(String aid) {
        if (aid == null) {
            return false;
        }
        if (aid.endsWith("*") && aid.length() % 2 == 0) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else if (!aid.endsWith("*") && aid.length() % 2 != 0) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else if (aid.matches("[0-9A-Fa-f]{10,32}\\*?")) {
            return true;
        } else {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        }
    }

    public boolean supportsAutoRouting() {
        boolean z = false;
        try {
            z = sService.supportsAutoRouting();
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.supportsAutoRouting();
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean enableAutoRouting() {
        boolean z = false;
        try {
            z = sService.enableAutoRouting();
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.enableAutoRouting();
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean disableAutoRouting() {
        boolean z = false;
        try {
            z = sService.disableAutoRouting();
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.disableAutoRouting();
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public int registerService(ComponentName app, String category) {
        int i = 9111;
        if (app == null) {
            throw new NullPointerException("service or category is null");
        }
        try {
            i = sService.registerService(UserHandle.myUserId(), app, category, 0);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    i = sService.registerService(UserHandle.myUserId(), app, category, 0);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return i;
    }

    public int unregisterOtherService(ComponentName app) {
        int i = 9111;
        if (app == null) {
            throw new NullPointerException("service or category is null");
        }
        try {
            i = sService.unregisterOtherService(UserHandle.myUserId(), app);
        } catch (RemoteException e) {
            recoverService();
            if (sService != null) {
                try {
                    i = sService.unregisterOtherService(UserHandle.myUserId(), app);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return i;
    }

    public boolean isRegisteredService(ComponentName app, String category) {
        boolean z = false;
        try {
            z = sService.isRegisteredService(UserHandle.myUserId(), app, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.isRegisteredService(UserHandle.myUserId(), app, category);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public int getUsedAidTableSizeInPercent(String category) {
        int i = 0;
        try {
            i = sService.getUsedAidTableSizeInPercent(UserHandle.myUserId(), category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    i = sService.getUsedAidTableSizeInPercent(UserHandle.myUserId(), category);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return i;
    }

    public int getAidSizeForServiceInPercent(ComponentName app) {
        int i = 0;
        try {
            i = sService.getAidSizeForServiceInPercent(UserHandle.myUserId(), app);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    i = sService.getAidSizeForServiceInPercent(UserHandle.myUserId(), app);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return i;
    }

    public boolean setLockPassword(String data) {
        boolean z = false;
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        try {
            z = sService.setLockPassword(data);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.setLockPassword(data);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean setOtherService(ComponentName service) {
        boolean z = false;
        if (service == null) {
            throw new NullPointerException("activity or service or category is null");
        }
        try {
            z = sService.setOtherService(UserHandle.myUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.setOtherService(UserHandle.myUserId(), service);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    public boolean unsetOtherService(ComponentName service) {
        boolean z = false;
        if (service == null) {
            throw new NullPointerException("activity or service or category is null");
        }
        try {
            z = sService.unsetOtherService(UserHandle.myUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
            } else {
                try {
                    z = sService.unsetOtherService(UserHandle.myUserId(), service);
                } catch (RemoteException e2) {
                    Log.e(TAG, "Failed to reach CardEmulationService.");
                }
            }
        }
        return z;
    }

    void recoverService() {
        sService = NfcAdapter.getDefaultAdapter(this.mContext).getCardEmulationService();
    }
}
