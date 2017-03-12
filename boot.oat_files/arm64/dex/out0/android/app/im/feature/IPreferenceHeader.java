package android.app.im.feature;

import android.preference.PreferenceActivity.Header;
import java.util.List;

public interface IPreferenceHeader extends IInjection {
    void onBuildHeader(List<Header> list);
}
