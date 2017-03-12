package android.content.res;

import android.content.res.Resources.Theme;

public class ConfigurationBoundResourceCache<T> extends ThemedResourceCache<ConstantState<T>> {
    private final Resources mResources;

    public /* bridge */ /* synthetic */ void onConfigurationChange(int x0) {
        super.onConfigurationChange(x0);
    }

    public ConfigurationBoundResourceCache(Resources resources) {
        this.mResources = resources;
    }

    public T getInstance(long key, Theme theme) {
        ConstantState<T> entry = (ConstantState) get(key, theme);
        if (entry != null) {
            return entry.newInstance(this.mResources, theme);
        }
        return null;
    }

    public boolean shouldInvalidateEntry(ConstantState<T> entry, int configChanges) {
        return Configuration.needNewResources(configChanges, entry.getChangingConfigurations());
    }
}
