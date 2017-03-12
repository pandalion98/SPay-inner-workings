package android.database;

import android.database.sqlite.SQLiteDatabaseConfiguration;
import android.util.Log;
import java.io.File;

public final class DefaultSecureDatabaseErrorHandler implements DatabaseErrorHandler {
    private static final String TAG = "DefaultSecureDatabaseErrorHandler";
    private String[] err_msg = new String[]{".corrupt", ".back"};
    private int[] err_num = new int[]{11, 26};
    private String suffix = ".unknown";

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCorruption(android.database.sqlite.SQLiteDatabase r9) {
        /*
        r8 = this;
        r5 = "DefaultSecureDatabaseErrorHandler";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Corruption reported by sqlite on database: ";
        r6 = r6.append(r7);
        r7 = r9.getPath();
        r6 = r6.append(r7);
        r6 = r6.toString();
        android.util.Log.e(r5, r6);
        r1 = r9.getCorruptCode();
        r2 = 0;
    L_0x0021:
        r5 = r8.err_num;
        r5 = r5.length;
        if (r2 >= r5) goto L_0x0035;
    L_0x0026:
        r5 = r8.err_num;
        r5 = r5[r2];
        if (r5 != r1) goto L_0x0032;
    L_0x002c:
        r5 = r8.err_msg;
        r5 = r5[r2];
        r8.suffix = r5;
    L_0x0032:
        r2 = r2 + 1;
        goto L_0x0021;
    L_0x0035:
        r5 = r9.isOpen();
        if (r5 != 0) goto L_0x0043;
    L_0x003b:
        r5 = r9.getPath();
        r8.backupDatabaseFile(r5);
    L_0x0042:
        return;
    L_0x0043:
        r0 = 0;
        r0 = r9.getAttachedDbs();	 Catch:{ SQLiteException -> 0x0091, all -> 0x006d }
    L_0x0048:
        r9.close();	 Catch:{ SQLiteException -> 0x0093, all -> 0x006d }
    L_0x004b:
        if (r0 == 0) goto L_0x0065;
    L_0x004d:
        r3 = r0.iterator();
    L_0x0051:
        r5 = r3.hasNext();
        if (r5 == 0) goto L_0x0042;
    L_0x0057:
        r4 = r3.next();
        r4 = (android.util.Pair) r4;
        r5 = r4.second;
        r5 = (java.lang.String) r5;
        r8.backupDatabaseFile(r5);
        goto L_0x0051;
    L_0x0065:
        r5 = r9.getPath();
        r8.backupDatabaseFile(r5);
        goto L_0x0042;
    L_0x006d:
        r5 = move-exception;
        r6 = r5;
        if (r0 == 0) goto L_0x0089;
    L_0x0071:
        r3 = r0.iterator();
    L_0x0075:
        r5 = r3.hasNext();
        if (r5 == 0) goto L_0x0090;
    L_0x007b:
        r4 = r3.next();
        r4 = (android.util.Pair) r4;
        r5 = r4.second;
        r5 = (java.lang.String) r5;
        r8.backupDatabaseFile(r5);
        goto L_0x0075;
    L_0x0089:
        r5 = r9.getPath();
        r8.backupDatabaseFile(r5);
    L_0x0090:
        throw r6;
    L_0x0091:
        r5 = move-exception;
        goto L_0x0048;
    L_0x0093:
        r5 = move-exception;
        goto L_0x004b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.DefaultSecureDatabaseErrorHandler.onCorruption(android.database.sqlite.SQLiteDatabase):void");
    }

    private void backupDatabaseFile(String fileName) {
        if (!fileName.equalsIgnoreCase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH) && fileName.trim().length() != 0) {
            Log.e(TAG, "backup the database file: " + fileName);
            try {
                new File(fileName).renameTo(new File(fileName + this.suffix));
            } catch (Exception e) {
                Log.w(TAG, "backup failed: " + e.getMessage());
            }
        }
    }
}
