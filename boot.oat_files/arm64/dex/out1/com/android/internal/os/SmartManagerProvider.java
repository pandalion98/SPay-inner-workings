package com.android.internal.os;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.android.internal.telephony.PhoneConstants;

public class SmartManagerProvider extends ContentProvider {
    static final int ALL_PACKAGES = 1;
    static final int BATTERY_DELTA = 2;
    static final int PACKAGE_NAME = 4;
    static final int POWER_CONSUMING_PACKAGES = 3;
    static final UriMatcher uriMatcher = new UriMatcher(-1);
    BatteryStatsDBHelper batteryStatsDBHelper;

    static {
        uriMatcher.addURI(SMProviderContract.PROVIDER_NAME, "Battery_Delta", 2);
        uriMatcher.addURI(SMProviderContract.PROVIDER_NAME, "power_consuming_packages", 3);
        uriMatcher.addURI(SMProviderContract.PROVIDER_NAME, PhoneConstants.APN_TYPE_ALL, 1);
    }

    public boolean onCreate() {
        this.batteryStatsDBHelper = BatteryStatsDBHelper.getInstance(getContext());
        if (this.batteryStatsDBHelper != null) {
            return true;
        }
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        if (uri != null) {
            synchronized (this.batteryStatsDBHelper) {
                SQLiteDatabase database = this.batteryStatsDBHelper.getReadableDatabase();
                BatteryStatsDBHelper batteryStatsDBHelper;
                switch (uriMatcher.match(uri)) {
                    case 1:
                        String table_name = uri.getLastPathSegment();
                        if (!PhoneConstants.APN_TYPE_ALL.equals(table_name)) {
                            cursor = database.query("[" + table_name + "]", projection, selection, selectionArgs, sortOrder, null, null);
                            break;
                        }
                        cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='Battery_Delta' AND name!='power_consuming_packages' AND name!='null' AND name!='all'", null);
                        break;
                    case 2:
                        batteryStatsDBHelper = this.batteryStatsDBHelper;
                        cursor = database.query("Battery_Delta", projection, selection, selectionArgs, sortOrder, null, null);
                        break;
                    case 3:
                        batteryStatsDBHelper = this.batteryStatsDBHelper;
                        cursor = database.query("power_consuming_packages", projection, selection, selectionArgs, sortOrder, null, null);
                        break;
                    default:
                        String contentURI = uri.toString();
                        if (contentURI.contains(SMProviderContract.URL)) {
                            String newURI = contentURI.replace("content://com.sec.smartmanager.provider/", "");
                            if (!newURI.isEmpty()) {
                                cursor = database.query("[" + newURI + "]", projection, selection, selectionArgs, sortOrder, null, null);
                                break;
                            }
                        }
                        throw new IllegalArgumentException("Unknown URI " + uri);
                        break;
                }
            }
        }
        return cursor;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
