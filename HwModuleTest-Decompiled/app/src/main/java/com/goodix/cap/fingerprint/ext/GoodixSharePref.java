package com.goodix.cap.fingerprint.ext;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class GoodixSharePref {
    private static GoodixSharePref sInstance;
    private Context mContext;
    private Editor mEditor;
    private PreferenceManager mPreferenceManager;
    private SharedPreferences mSp;

    private GoodixSharePref() {
    }

    public static GoodixSharePref getInstance() {
        if (sInstance == null) {
            sInstance = new GoodixSharePref();
        }
        return sInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mSp = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        this.mEditor = this.mSp.edit();
    }

    public void putString(String key, String value) {
        this.mEditor.putString(key, value);
        this.mEditor.commit();
    }

    public void putInt(String key, int value) {
        this.mEditor.putInt(key, value);
        this.mEditor.commit();
    }

    public String getString(String key, String def) {
        return this.mSp.getString(key, def);
    }

    public int getInt(String key, int def) {
        return this.mSp.getInt(key, def);
    }

    public void putBoolean(String key, boolean value) {
        this.mEditor.putBoolean(key, value);
        this.mEditor.commit();
    }

    public boolean getBoolean(String key, boolean def) {
        return this.mSp.getBoolean(key, def);
    }
}
