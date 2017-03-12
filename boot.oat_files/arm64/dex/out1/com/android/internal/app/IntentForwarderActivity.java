package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.PersonaState;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.PersonaPolicyManager;
import android.os.RemoteException;
import android.os.UserManager;
import android.service.notification.ZenModeConfig;
import android.util.Slog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;
import com.android.ims.ImsConferenceState;
import com.android.internal.R;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import java.util.ArrayList;

public class IntentForwarderActivity extends Activity {
    public static String FORWARD_INTENT_TO_MANAGED_PROFILE = "com.android.internal.app.ForwardIntentToManagedProfile";
    public static String FORWARD_INTENT_TO_USER_OWNER = "com.android.internal.app.ForwardIntentToUserOwner";
    public static String TAG = "IntentForwarderActivity";
    private final int TARGET_USER_ID_DEFAULT_VALUE = -1;
    private int targetUserIdForKnox = 0;

    protected void onCreate(Bundle savedInstanceState) {
        int userMessageId;
        int targetUserId;
        super.onCreate(savedInstanceState);
        Intent intentReceived = getIntent();
        String className = intentReceived.getComponent().getClassName();
        try {
            this.targetUserIdForKnox = new Intent(intentReceived).getIntExtra("crossProfileTargetUserId", -1);
        } catch (Exception e) {
        }
        if (className.equals(FORWARD_INTENT_TO_USER_OWNER)) {
            userMessageId = R.string.forward_intent_to_owner;
            targetUserId = 0;
        } else if (className.equals(FORWARD_INTENT_TO_MANAGED_PROFILE)) {
            userMessageId = R.string.forward_intent_to_work;
            if (this.targetUserIdForKnox != -1) {
                targetUserId = this.targetUserIdForKnox;
            } else {
                targetUserId = getManagedProfile();
            }
        } else {
            Slog.wtf(TAG, IntentForwarderActivity.class.getName() + " cannot be called directly");
            userMessageId = -1;
            targetUserId = DeviceRootKeyServiceManager.ERR_SERVICE_ERROR;
        }
        if (targetUserId == -10000) {
            finish();
            return;
        }
        Intent intent = new Intent(intentReceived);
        intent.setComponent(null);
        intent.setPackage(null);
        intent.addFlags(View.SCROLLBARS_OUTSIDE_INSET);
        int callingUserId = getUserId();
        boolean forwardfromKnox = false;
        if (callingUserId >= 100) {
            forwardfromKnox = true;
        }
        if (canForward(intent, targetUserId)) {
            if (intent.getAction().equals("android.intent.action.CHOOSER")) {
                ((Intent) intent.getParcelableExtra("android.intent.extra.INTENT")).prepareToLeaveUser(callingUserId);
            } else {
                intent.prepareToLeaveUser(callingUserId);
            }
            ResolveInfo ri = getPackageManager().resolveActivityAsUser(intent, 65536, targetUserId);
            boolean shouldShowDisclosure = ri == null || ri.activityInfo == null || !ZenModeConfig.SYSTEM_AUTHORITY.equals(ri.activityInfo.packageName) || !(ResolverActivity.class.getName().equals(ri.activityInfo.name) || ChooserActivity.class.getName().equals(ri.activityInfo.name));
            try {
                if (PersonaManager.isBBCContainer(callingUserId) && targetUserId == 0 && intent != null && "file".equals(intent.getScheme()) && !intent.getBooleanExtra("isFromBBCProvider", false)) {
                    Intent bbcIntent = new Intent();
                    bbcIntent.putExtra("base_intent", intent);
                    bbcIntent.setClassName("com.samsung.android.bbc.fileprovider", "com.samsung.android.bbc.fileprovider.IntentForwardActivity");
                    startActivityAsCaller(bbcIntent, null, false, callingUserId);
                    finish();
                    return;
                }
                UserInfo ui;
                if (intent != null) {
                    if (intent.getAction().equals("android.intent.action.SEND") || intent.getAction().equals("android.intent.action.SEND_MULTIPLE")) {
                        getResources();
                        Intent ChooserIntent = Intent.createChooser(intent, Resources.getSystem().getText(R.string.whichSendApplication));
                        ArrayList<ComponentName> compArrary = new ArrayList();
                        compArrary.add(new ComponentName("com.sec.knox.bluetooth", "com.sec.knox.bluetooth.BluetoothFileTransfer"));
                        ChooserIntent.putParcelableArrayListExtra("extra_chooser_droplist", compArrary);
                        startActivityAsCaller(ChooserIntent, null, false, targetUserId);
                        ui = ((UserManager) getSystemService(ImsConferenceState.USER)).getUserInfo(targetUserId);
                        if (ui != null && ui.isKnoxWorkspace()) {
                            shouldShowDisclosure = false;
                        }
                        if (!forwardfromKnox) {
                            if (shouldShowDisclosure) {
                                Toast.makeText((Context) this, (CharSequence) getString(userMessageId), 1).show();
                            }
                        } else if (!(((ActivityManager) getSystemService("activity")).isInLockTaskMode() || (PersonaManager.isKioskModeEnabled(this) && PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_0)))) {
                            Toast.makeText(new ContextThemeWrapper(getApplicationContext(), (int) R.style.Theme_DeviceDefault_Light), getString(R.string.forward_intent_from_knox), 0).show();
                        }
                    }
                }
                startActivityAsCaller(intent, null, false, targetUserId);
                ui = ((UserManager) getSystemService(ImsConferenceState.USER)).getUserInfo(targetUserId);
                shouldShowDisclosure = false;
                if (!forwardfromKnox) {
                    Toast.makeText(new ContextThemeWrapper(getApplicationContext(), (int) R.style.Theme_DeviceDefault_Light), getString(R.string.forward_intent_from_knox), 0).show();
                } else if (shouldShowDisclosure) {
                    Toast.makeText((Context) this, (CharSequence) getString(userMessageId), 1).show();
                }
            } catch (RuntimeException e2) {
                int i = -1;
                String launchedFromPackage = "?";
                try {
                    i = ActivityManagerNative.getDefault().getLaunchedFromUid(getActivityToken());
                    launchedFromPackage = ActivityManagerNative.getDefault().getLaunchedFromPackage(getActivityToken());
                } catch (RemoteException e3) {
                }
                Slog.wtf(TAG, "Unable to launch as UID " + i + " package " + launchedFromPackage + ", while running in " + ActivityThread.currentProcessName(), e2);
            }
        } else {
            Slog.wtf(TAG, "the intent: " + intent + "cannot be forwarded from user " + callingUserId + " to user " + targetUserId);
        }
        finish();
    }

    boolean canForward(Intent intent, int targetUserId) {
        int i = false;
        IPackageManager ipm = AppGlobals.getPackageManager();
        if (PersonaManager.isKnoxId(targetUserId)) {
            PersonaManager personaManager = (PersonaManager) getApplicationContext().getSystemService("persona");
            PersonaPolicyManager personaPolicyManager = (PersonaPolicyManager) personaManager.getPersonaService("persona_policy");
            PersonaState curState = personaManager.getStateManager(targetUserId).getState();
            if (personaManager.getStateManager(targetUserId).inState(PersonaState.SUPER_LOCKED) || curState == PersonaState.ADMIN_LOCKED || curState == PersonaState.ADMIN_LICENSE_LOCKED || curState == PersonaState.LICENSE_LOCKED || curState == PersonaState.CONTAINER_APPS_URGENT_UPDATE || curState == PersonaState.TIMA_COMPROMISED) {
                Toast.makeText(getApplicationContext(), getString(R.string.knox_feature_disabled_toast), i).show();
                return i;
            }
            String intentAction;
            if (intent.getAction().equals("android.intent.action.CHOOSER")) {
                intentAction = ((Intent) intent.getParcelableExtra("android.intent.extra.INTENT")).getAction();
            } else {
                intentAction = intent.getAction();
            }
            if (intentAction != null && (intentAction.equals("android.intent.action.SEND") || intentAction.equals("android.intent.action.SEND_MULTIPLE"))) {
                if (personaPolicyManager.isMoveFilesToContainerAllowed(targetUserId)) {
                    return true;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.knox_feature_disabled_toast), i).show();
                return i;
            }
        }
        if (intent.getAction().equals("android.intent.action.CHOOSER")) {
            if (intent.hasExtra("android.intent.extra.INITIAL_INTENTS")) {
                Slog.wtf(TAG, "An chooser intent with extra initial intents cannot be forwarded to a different user");
                return i;
            } else if (intent.hasExtra("android.intent.extra.REPLACEMENT_EXTRAS")) {
                Slog.wtf(TAG, "A chooser intent with replacement extras cannot be forwarded to a different user");
                return i;
            } else {
                intent = (Intent) intent.getParcelableExtra("android.intent.extra.INTENT");
            }
        }
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (intent.getSelector() != null) {
            intent = intent.getSelector();
        }
        try {
            return ipm.canForwardTo(intent, resolvedType, getUserId(), targetUserId);
        } catch (RemoteException e) {
            Slog.e(TAG, "PackageManagerService is dead?");
            return i;
        }
    }

    private int getManagedProfile() {
        for (UserInfo userInfo : ((UserManager) getSystemService(ImsConferenceState.USER)).getProfiles(0)) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        Slog.wtf(TAG, FORWARD_INTENT_TO_MANAGED_PROFILE + " has been called, but there is no managed profile");
        return DeviceRootKeyServiceManager.ERR_SERVICE_ERROR;
    }
}
