package android.app.im.feature;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

public interface IInjection {
    void onConfigurationChanged(Configuration configuration);

    void onCreate(Activity activity, Context context, Bundle bundle);

    void onDestroy();

    void onPause();

    void onRestart();

    void onRestoreInstanceState(Bundle bundle);

    void onResume();

    void onSaveInstanceState(Bundle bundle);

    boolean onSearchRequested();

    void onStart();

    void onStop();
}
