package com.americanexpress.mobilepayments.hceclient.utils.stash;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.americanexpress.mobilepayments.hceclient.utils.context.AppContext;

public class DataStashCoreImpl implements DataStash {
    private Context context;

    public DataStashCoreImpl() {
        this.context = null;
        this.context = AppContext.getInstance().getContext();
    }

    public String getDataFromStorage(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        return this.context.getSharedPreferences(str, 0).getString(str2, null);
    }

    public void putDataIntoStorage(String str, String str2, String str3) {
        if (str != null && str2 != null) {
            Editor edit = this.context.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            edit.commit();
        }
    }

    public boolean isDataPresent(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return this.context.getSharedPreferences(str, 0).contains(str2);
    }

    public void deleteDataFromStorage(String str, String str2) {
        if (str != null && str2 != null) {
            Editor edit = this.context.getSharedPreferences(str, 0).edit();
            edit.remove(str2);
            edit.commit();
        }
    }

    public void deleteStorage(String str) {
        if (str != null) {
            Editor edit = this.context.getSharedPreferences(str, 0).edit();
            edit.clear();
            edit.commit();
        }
    }
}
