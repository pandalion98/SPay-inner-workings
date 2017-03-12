package com.samsung.android.smartclip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import java.util.List;

class SmartClipIntentUtils {
    private static final String TAG = "SmartClipIntentUtils";

    SmartClipIntentUtils() {
    }

    public static boolean isValidIntent(Context context, Intent intent) {
        if (intent == null) {
            Log.e(TAG, "intent is null");
            return false;
        }
        List<ResolveInfo> resolveList = context.getPackageManager().queryIntentActivities(intent, 65536);
        if (resolveList.size() != 1) {
            Log.e(TAG, "resolveList(" + resolveList + ") is duplicated or null.");
            return false;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return true;
        }
        if (bundle.getClassLoader() == null) {
            return true;
        }
        Log.e(TAG, "bundle have classloader");
        return false;
    }

    public static Intent getUsefullIntent(Context context) {
        if (context == null) {
            Log.e(TAG, "context is null");
            return null;
        } else if (context instanceof Activity) {
            Intent i = ((Activity) context).getIntent();
            if (i == null) {
                Log.e(TAG, "intent is null");
                return null;
            } else if (i.getComponent() == null) {
                Log.e(TAG, "Component is null");
                return null;
            } else {
                List<ResolveInfo> resolveList = context.getPackageManager().queryIntentActivities(i, 65536);
                if (resolveList.size() != 1) {
                    Log.e(TAG, "resolveList(" + resolveList + ") is duplicated or null.");
                    return null;
                }
                Bundle bundle = i.getExtras();
                if (bundle == null || bundle.getClassLoader() == null) {
                    return i;
                }
                Log.e(TAG, "bundle is not null");
                return null;
            }
        } else {
            Log.e(TAG, "context(" + context + ") is not Activity.");
            return null;
        }
    }

    public static byte[] marshall(Intent intent) {
        Parcel parcel = Parcel.obtain();
        intent.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        byte[] value = parcel.marshall();
        parcel.recycle();
        return value;
    }

    public static Intent unmarshall(byte[] data) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(data, 0, data.length);
        parcel.setDataPosition(0);
        Intent intent = new Intent();
        intent.readFromParcel(parcel);
        parcel.recycle();
        return intent;
    }
}
