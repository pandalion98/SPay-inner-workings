package android.app.admin;

import java.util.List;

public abstract class DevicePolicyManagerInternal {

    public interface OnCrossProfileWidgetProvidersChangeListener {
        void onCrossProfileWidgetProvidersChanged(int i, List<String> list);
    }

    public abstract void addOnCrossProfileWidgetProvidersChangeListener(OnCrossProfileWidgetProvidersChangeListener onCrossProfileWidgetProvidersChangeListener);

    public abstract List<String> getCrossProfileWidgetProviders(int i);

    public abstract boolean isActiveAdminWithPolicy(int i, int i2);
}
