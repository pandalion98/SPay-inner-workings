package com.sensoryinc.fluentsoftsdk;

import android.util.Log;

public class SensoryBargeInEngineWrapper {
    private static final String TAG = SensoryBargeInEngineWrapper.class.getSimpleName();
    private static SensoryBargeInEngine uniqueInstance;

    private SensoryBargeInEngineWrapper() {
    }

    public static synchronized SensoryBargeInEngine getInstance() {
        SensoryBargeInEngine sensoryBargeInEngine;
        synchronized (SensoryBargeInEngineWrapper.class) {
            if (uniqueInstance == null) {
                Log.i(TAG, "getInstance() : make new SensoryBargeInEngine");
                if (SensoryBargeInEngine.init() == 0) {
                    uniqueInstance = new SensoryBargeInEngine();
                } else {
                    Log.e(TAG, "cannot load libSensoryBargeInEngien.so");
                    sensoryBargeInEngine = null;
                }
            } else {
                Log.i(TAG, "getInstance() : get existed SensoryBargeInEngine");
            }
            sensoryBargeInEngine = uniqueInstance;
        }
        return sensoryBargeInEngine;
    }
}
