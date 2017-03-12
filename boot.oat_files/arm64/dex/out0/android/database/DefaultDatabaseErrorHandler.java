package android.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseConfiguration;
import android.database.sqlite.SQLiteException;
import android.net.ProxyInfo;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.util.List;

public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler {
    private static final String TAG = "DefaultDatabaseErrorHandler";
    private String[] err_msg = new String[]{".corrupt", ".back"};
    private int[] err_num = new int[]{11, 26};
    private String suffix = ".unknown";

    private void backCorruption(String path) {
        if (path.equalsIgnoreCase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH) || path.trim().length() == 0) {
            Log.e(TAG, "!@ Corruption in memory DB");
            return;
        }
        try {
            File file = new File(path + this.suffix);
            if (file.exists()) {
                Log.e(TAG, "!@ DB Corruption has happened before this");
                if (!file.delete()) {
                    Log.e(TAG, "!@ File delete failed!");
                }
            }
            SQLiteDatabase.renameDatabaseFile(path, path + this.suffix);
        } catch (Exception e) {
            Log.e(TAG, "!@ openDatabase - Exception during copying and renaming");
        }
    }

    public void onCorruption(SQLiteDatabase dbObj) {
        String path = dbObj.getPath();
        Log.e(TAG, "Corruption reported by sqlite on database: " + path);
        int err_code = dbObj.getCorruptCode();
        for (int i = 0; i < this.err_num.length; i++) {
            if (this.err_num[i] == err_code) {
                this.suffix = this.err_msg[i];
            }
        }
        if (dbObj.isOpen()) {
            try {
                if (dbObj.isDatabaseIntegrityOk()) {
                    Log.e(TAG, "!@ Integrity Check for corrupted DB file gets OK as result");
                    if (new File(path + ".mark").exists()) {
                        Log.e(TAG, "!@ Delete old .mark file");
                        SQLiteDatabase.renameDatabaseFile(path + ".mark", path + ".mark2");
                    } else {
                        Log.e(TAG, "!@ Make .mark file to indicate Integrity is Ok");
                        new File(path + ".mark").createNewFile();
                        return;
                    }
                }
                boolean needContinue = true;
                Log.e(TAG, "!@ Integrity Check failed for corrupted DB file");
                do {
                    String errInfo = dbObj.getIntegrityErrorInfo();
                    if (errInfo != null) {
                        int pos = errInfo.lastIndexOf(" index ");
                        if (pos > 0) {
                            dbObj.execSQL("REINDEX " + errInfo.substring(pos + 7, errInfo.length()).replaceAll(" ", ProxyInfo.LOCAL_EXCL_LIST) + ";");
                            if (dbObj.isDatabaseIntegrityOk()) {
                                return;
                            }
                        } else {
                            needContinue = false;
                            continue;
                        }
                    } else {
                        needContinue = false;
                        continue;
                    }
                } while (needContinue);
            } catch (Exception e) {
            }
            try {
                List<Pair<String, String>> attachedDbs = dbObj.getAttachedDbs();
                dbObj.close();
                if (attachedDbs != null) {
                    for (Pair<String, String> p : attachedDbs) {
                        Log.e(TAG, "!@ Back up corrupted DB File : " + ((String) p.second));
                        backCorruption((String) p.second);
                    }
                    return;
                }
                Log.e(TAG, "!@ Failed to get attachedDbs");
                backCorruption(path);
                return;
            } catch (SQLiteException e2) {
                return;
            }
        }
        Log.e(TAG, "!@ dbObj has been closed");
        backCorruption(path);
    }

    private void deleteDatabaseFile(String fileName) {
        if (fileName.equalsIgnoreCase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH) || fileName.trim().length() == 0) {
            Log.e(TAG, "!@ Specified file is memory DB");
            return;
        }
        Log.e(TAG, "deleting the database file: " + fileName);
        try {
            SQLiteDatabase.deleteDatabase(new File(fileName));
        } catch (Exception e) {
            Log.w(TAG, "delete failed: " + e.getMessage());
        }
    }
}
