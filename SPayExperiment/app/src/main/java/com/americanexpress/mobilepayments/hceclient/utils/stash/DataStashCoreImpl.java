/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.stash;

import android.content.Context;
import android.content.SharedPreferences;
import com.americanexpress.mobilepayments.hceclient.utils.context.AppContext;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;

public class DataStashCoreImpl
implements DataStash {
    private Context context = AppContext.getInstance().getContext();

    @Override
    public void deleteDataFromStorage(String string, String string2) {
        if (string != null && string2 != null) {
            SharedPreferences.Editor editor = this.context.getSharedPreferences(string, 0).edit();
            editor.remove(string2);
            editor.commit();
        }
    }

    @Override
    public void deleteStorage(String string) {
        if (string != null) {
            SharedPreferences.Editor editor = this.context.getSharedPreferences(string, 0).edit();
            editor.clear();
            editor.commit();
        }
    }

    @Override
    public String getDataFromStorage(String string, String string2) {
        String string3 = null;
        if (string != null) {
            string3 = null;
            if (string2 != null) {
                string3 = this.context.getSharedPreferences(string, 0).getString(string2, null);
            }
        }
        return string3;
    }

    @Override
    public boolean isDataPresent(String string, String string2) {
        boolean bl = false;
        if (string != null) {
            bl = false;
            if (string2 != null) {
                bl = this.context.getSharedPreferences(string, 0).contains(string2);
            }
        }
        return bl;
    }

    @Override
    public void putDataIntoStorage(String string, String string2, String string3) {
        if (string != null && string2 != null) {
            SharedPreferences.Editor editor = this.context.getSharedPreferences(string, 0).edit();
            editor.putString(string2, string3);
            editor.commit();
        }
    }
}

