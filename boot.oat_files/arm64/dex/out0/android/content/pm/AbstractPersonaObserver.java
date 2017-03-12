package android.content.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersonaManager;
import java.util.Set;

public abstract class AbstractPersonaObserver {
    public static final int FLAG_OBSERVER_ONATTRIBUTECHANGE = 8;
    public static final int FLAG_OBSERVER_ONKEYGUARDSTATECHANGED = 4;
    public static final int FLAG_OBSERVER_ONSESSIONEXPIRED = 2;
    public static final int FLAG_OBSERVER_ONSTATECHANGE = 1;
    private boolean checkOnAttributeChange = false;
    private boolean checkOnKeyguardStateChanged = false;
    private boolean checkOnSessionExpired = false;
    private boolean checkOnStateChange = false;
    protected int containerId = -1;
    private Context context = null;
    protected int flags = 0;
    private PersonaObserverReceiver personaObserverReceiver;

    private class PersonaObserverReceiver extends BroadcastReceiver {
        private PersonaObserverReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Set<String> categories = intent.getCategories();
            if (AbstractPersonaObserver.this.checkOnStateChange && categories.contains(PersonaManager.INTENT_CATEGORY_OBSERVER_ONSTATECHANGE)) {
                AbstractPersonaObserver.this.onStateChange(PersonaState.valueOf(intent.getStringExtra(PersonaManager.INTENT_EXTRA_OBSERVER_NEWSTATE)), PersonaState.valueOf(intent.getStringExtra(PersonaManager.INTENT_EXTRA_OBSERVER_PREVIOUSSTATE)));
            } else if (AbstractPersonaObserver.this.checkOnSessionExpired && categories.contains(PersonaManager.INTENT_CATEGORY_OBSERVER_ONSESSIONEXPIRED)) {
                AbstractPersonaObserver.this.onSessionExpired();
            } else if (AbstractPersonaObserver.this.checkOnKeyguardStateChanged && categories.contains(PersonaManager.INTENT_CATEGORY_OBSERVER_ONKEYGUARDSTATECHANGED)) {
                AbstractPersonaObserver.this.onKeyGuardStateChanged(intent.getBooleanExtra(PersonaManager.INTENT_EXTRA_OBSERVER_KEYGUARDSTATE, true));
            } else if (AbstractPersonaObserver.this.checkOnAttributeChange && categories.contains(PersonaManager.INTENT_CATEGORY_OBSERVER_ONATTRIBUTECHANGE)) {
                AbstractPersonaObserver.this.onAttributeChange(PersonaAttribute.valueOf(intent.getStringExtra(PersonaManager.INTENT_EXTRA_OBSERVER_ATTRIBUTE)), intent.getBooleanExtra(PersonaManager.INTENT_EXTRA_OBSERVER_ATTRIBUTE_STATE, true));
            }
        }
    }

    public abstract void onKeyGuardStateChanged(boolean z);

    public abstract void onSessionExpired();

    public abstract void onStateChange(PersonaState personaState, PersonaState personaState2);

    public AbstractPersonaObserver(Context context, int containerId, int flags) {
        this.context = context;
        this.containerId = containerId;
        this.flags = flags;
        initializeReceiver();
    }

    private void initializeReceiver() {
        IntentFilter intentFilter;
        this.personaObserverReceiver = new PersonaObserverReceiver();
        if ((this.flags & 1) == 1) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(PersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_ONSTATECHANGE);
            this.checkOnStateChange = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, "com.samsung.container.OBSERVER", null);
            }
        }
        if ((this.flags & 2) == 2) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(PersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_ONSESSIONEXPIRED);
            this.checkOnSessionExpired = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, "com.samsung.container.OBSERVER", null);
            }
        }
        if ((this.flags & 4) == 4) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(PersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_ONKEYGUARDSTATECHANGED);
            this.checkOnKeyguardStateChanged = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, "com.samsung.container.OBSERVER", null);
            }
        }
        if ((this.flags & 8) == 8) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(PersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(PersonaManager.INTENT_CATEGORY_OBSERVER_ONATTRIBUTECHANGE);
            this.checkOnAttributeChange = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, "com.samsung.container.OBSERVER", null);
            }
        }
    }

    public void unregisterPersonaObserver() {
        if (this.context != null) {
            this.context.unregisterReceiver(this.personaObserverReceiver);
        }
    }

    public void onAttributeChange(PersonaAttribute attribute, boolean enabled) {
    }
}
