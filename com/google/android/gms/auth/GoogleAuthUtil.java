package com.google.android.gms.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzm;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.zza;
import com.google.android.gms.internal.zzjo;
import com.google.android.gms.internal.zzr;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public final class GoogleAuthUtil {
    public static final int CHANGE_TYPE_ACCOUNT_ADDED = 1;
    public static final int CHANGE_TYPE_ACCOUNT_REMOVED = 2;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_FROM = 3;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_TO = 4;
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    public static final String KEY_ANDROID_PACKAGE_NAME;
    public static final String KEY_CALLER_UID;
    public static final String KEY_REQUEST_ACTIONS = "request_visible_actions";
    @Deprecated
    public static final String KEY_REQUEST_VISIBLE_ACTIVITIES = "request_visible_actions";
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    private static final ComponentName zzHk;
    private static final ComponentName zzHl;

    static {
        KEY_CALLER_UID = VERSION.SDK_INT >= 11 ? "callerUid" : "callerUid";
        KEY_ANDROID_PACKAGE_NAME = VERSION.SDK_INT >= 14 ? "androidPackageName" : "androidPackageName";
        zzHk = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.auth.GetToken");
        zzHl = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.recovery.RecoveryService");
    }

    private GoogleAuthUtil() {
    }

    public static void clearToken(Context context, String str) {
        Context applicationContext = context.getApplicationContext();
        zzx.zzbe("Calling this from your main thread can lead to deadlock");
        zzI(applicationContext);
        Bundle bundle = new Bundle();
        String str2 = context.getApplicationInfo().packageName;
        bundle.putString("clientPackageName", str2);
        if (!bundle.containsKey(KEY_ANDROID_PACKAGE_NAME)) {
            bundle.putString(KEY_ANDROID_PACKAGE_NAME, str2);
        }
        ServiceConnection com_google_android_gms_common_zza = new zza();
        zzm zzP = zzm.zzP(applicationContext);
        if (zzP.zza(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil")) {
            try {
                Bundle zza = zzr.zza.zza(com_google_android_gms_common_zza.zzhJ()).zza(str, bundle);
                String string = zza.getString(zzjo.zzIi);
                if (zza.getBoolean("booleanResult")) {
                    zzP.zzb(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil");
                    return;
                }
                throw new GoogleAuthException(string);
            } catch (Throwable e) {
                Log.i("GoogleAuthUtil", "GMS remote exception ", e);
                throw new IOException("remote exception");
            } catch (InterruptedException e2) {
                throw new GoogleAuthException("Interrupted");
            } catch (Throwable th) {
                zzP.zzb(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil");
            }
        } else {
            throw new IOException("Could not bind to service with the given context.");
        }
    }

    public static List<AccountChangeEvent> getAccountChangeEvents(Context context, int i, String str) {
        zzx.zzb(str, (Object) "accountName must be provided");
        zzx.zzbe("Calling this from your main thread can lead to deadlock");
        Context applicationContext = context.getApplicationContext();
        zzI(applicationContext);
        ServiceConnection com_google_android_gms_common_zza = new zza();
        zzm zzP = zzm.zzP(applicationContext);
        if (zzP.zza(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil")) {
            try {
                List<AccountChangeEvent> events = zzr.zza.zza(com_google_android_gms_common_zza.zzhJ()).zza(new AccountChangeEventsRequest().setAccountName(str).setEventIndex(i)).getEvents();
                zzP.zzb(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil");
                return events;
            } catch (Throwable e) {
                Log.i("GoogleAuthUtil", "GMS remote exception ", e);
                throw new IOException("remote exception");
            } catch (InterruptedException e2) {
                throw new GoogleAuthException("Interrupted");
            } catch (Throwable th) {
                zzP.zzb(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil");
            }
        } else {
            throw new IOException("Could not bind to service with the given context.");
        }
    }

    public static String getAccountId(Context context, String str) {
        zzx.zzb(str, (Object) "accountName must be provided");
        zzx.zzbe("Calling this from your main thread can lead to deadlock");
        zzI(context.getApplicationContext());
        return getToken(context, str, "^^_account_id_^^", new Bundle());
    }

    public static String getToken(Context context, Account account, String str) {
        return getToken(context, account, str, new Bundle());
    }

    public static String getToken(Context context, Account account, String str, Bundle bundle) {
        return zza(context, account, str, bundle, null);
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2) {
        return getToken(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2);
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2, Bundle bundle) {
        return getToken(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle);
    }

    public static String getTokenWithNotification(Context context, Account account, String str, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean("handle_notification", true);
        return zza(context, account, str, bundle);
    }

    public static String getTokenWithNotification(Context context, Account account, String str, Bundle bundle, Intent intent) {
        zzh(intent);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable("callback_intent", intent);
        bundle.putBoolean("handle_notification", true);
        return zza(context, account, str, bundle);
    }

    public static String getTokenWithNotification(Context context, Account account, String str, Bundle bundle, String str2, Bundle bundle2) {
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("Authority cannot be empty or null.");
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        ContentResolver.validateSyncExtrasBundle(bundle2);
        bundle.putString("authority", str2);
        bundle.putBundle("sync_extras", bundle2);
        bundle.putBoolean("handle_notification", true);
        return zza(context, account, str, bundle);
    }

    @Deprecated
    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle) {
        return getTokenWithNotification(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle);
    }

    @Deprecated
    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle, Intent intent) {
        return getTokenWithNotification(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle, intent);
    }

    @Deprecated
    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle, String str3, Bundle bundle2) {
        return getTokenWithNotification(context, new Account(str, GOOGLE_ACCOUNT_TYPE), str2, bundle, str3, bundle2);
    }

    @Deprecated
    public static void invalidateToken(Context context, String str) {
        AccountManager.get(context).invalidateAuthToken(GOOGLE_ACCOUNT_TYPE, str);
    }

    private static void zzI(Context context) {
        try {
            GooglePlayServicesUtil.zzI(context);
        } catch (GooglePlayServicesRepairableException e) {
            throw new GooglePlayServicesAvailabilityException(e.getConnectionStatusCode(), e.getMessage(), e.getIntent());
        } catch (GooglePlayServicesNotAvailableException e2) {
            throw new GoogleAuthException(e2.getMessage());
        }
    }

    private static String zza(Context context, Account account, String str, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        try {
            return getToken(context, account, str, bundle);
        } catch (GooglePlayServicesAvailabilityException e) {
            GooglePlayServicesUtil.showErrorNotification(e.getConnectionStatusCode(), context);
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        } catch (UserRecoverableAuthException e2) {
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        }
    }

    public static String zza(Context context, Account account, String str, Bundle bundle, Boolean bool) {
        Context applicationContext = context.getApplicationContext();
        zzx.zzbe("Calling this from your main thread can lead to deadlock");
        zzI(applicationContext);
        Bundle bundle2 = bundle == null ? new Bundle() : new Bundle(bundle);
        String str2 = context.getApplicationInfo().packageName;
        bundle2.putString("clientPackageName", str2);
        if (!bundle2.containsKey(KEY_ANDROID_PACKAGE_NAME)) {
            bundle2.putString(KEY_ANDROID_PACKAGE_NAME, str2);
        }
        if (bool != null) {
            bundle2.putBoolean("UseCache", bool.booleanValue());
        }
        ServiceConnection com_google_android_gms_common_zza = new zza();
        zzm zzP = zzm.zzP(applicationContext);
        if (zzP.zza(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil")) {
            try {
                bundle2 = zzr.zza.zza(com_google_android_gms_common_zza.zzhJ()).zza(account, str, bundle2);
                if (bundle2 == null) {
                    Log.w("GoogleAuthUtil", "Binder call returned null.");
                    throw new GoogleAuthException("ServiceUnavailable");
                }
                Object string = bundle2.getString("authtoken");
                if (TextUtils.isEmpty(string)) {
                    String string2 = bundle2.getString("Error");
                    Intent intent = (Intent) bundle2.getParcelable("userRecoveryIntent");
                    if (zzaD(string2)) {
                        throw new UserRecoverableAuthException(string2, intent);
                    } else if (zzaC(string2)) {
                        throw new IOException(string2);
                    } else {
                        throw new GoogleAuthException(string2);
                    }
                }
                zzP.zzb(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil");
                return string;
            } catch (Throwable e) {
                Log.i("GoogleAuthUtil", "GMS remote exception ", e);
                throw new IOException("remote exception");
            } catch (InterruptedException e2) {
                throw new GoogleAuthException("Interrupted");
            } catch (Throwable th) {
                zzP.zzb(zzHk, com_google_android_gms_common_zza, "GoogleAuthUtil");
            }
        } else {
            throw new IOException("Could not bind to service with the given context.");
        }
    }

    private static boolean zzaC(String str) {
        return "NetworkError".equals(str) || "ServiceUnavailable".equals(str) || "Timeout".equals(str);
    }

    private static boolean zzaD(String str) {
        return "BadAuthentication".equals(str) || "CaptchaRequired".equals(str) || "DeviceManagementRequiredOrSyncDisabled".equals(str) || "NeedPermission".equals(str) || "NeedsBrowser".equals(str) || "UserCancel".equals(str) || "AppDownloadRequired".equals(str) || zzjo.DM_SYNC_DISABLED.zzhg().equals(str) || zzjo.DM_ADMIN_BLOCKED.zzhg().equals(str) || zzjo.DM_ADMIN_PENDING_APPROVAL.zzhg().equals(str) || zzjo.DM_STALE_SYNC_REQUIRED.zzhg().equals(str) || zzjo.DM_DEACTIVATED.zzhg().equals(str) || zzjo.DM_REQUIRED.zzhg().equals(str) || zzjo.THIRD_PARTY_DEVICE_MANAGEMENT_REQUIRED.zzhg().equals(str);
    }

    private static void zzh(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Callback cannot be null.");
        }
        try {
            Intent.parseUri(intent.toUri(CHANGE_TYPE_ACCOUNT_ADDED), CHANGE_TYPE_ACCOUNT_ADDED);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
        }
    }
}
