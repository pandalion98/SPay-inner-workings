package android.content.pm;

import android.util.ArraySet;

public class PackageUserState {
    public int appLinkGeneration;
    public boolean blockUninstall;
    public ArraySet<String> disabledComponents;
    public int domainVerificationStatus;
    public int enabled;
    public ArraySet<String> enabledComponents;
    public boolean hidden;
    public boolean installed;
    public String lastDisableAppCaller;
    public boolean notLaunched;
    public boolean stopped;

    public PackageUserState() {
        this.installed = true;
        this.hidden = false;
        this.enabled = 0;
        this.domainVerificationStatus = 0;
    }

    public PackageUserState(PackageUserState o) {
        ArraySet arraySet = null;
        this.installed = o.installed;
        this.stopped = o.stopped;
        this.notLaunched = o.notLaunched;
        this.enabled = o.enabled;
        this.hidden = o.hidden;
        this.lastDisableAppCaller = o.lastDisableAppCaller;
        this.disabledComponents = o.disabledComponents != null ? new ArraySet(o.disabledComponents) : null;
        if (o.enabledComponents != null) {
            arraySet = new ArraySet(o.enabledComponents);
        }
        this.enabledComponents = arraySet;
        this.blockUninstall = o.blockUninstall;
        this.domainVerificationStatus = o.domainVerificationStatus;
        this.appLinkGeneration = o.appLinkGeneration;
    }
}
