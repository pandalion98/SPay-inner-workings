package android.app.im.feature;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public interface IPreference extends IInjection {
    void addPreference(PreferenceActivity preferenceActivity);

    void addPreference(PreferenceFragment preferenceFragment);
}
