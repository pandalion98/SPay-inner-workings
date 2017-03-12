package com.android.internal.os;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Parcel;
import android.os.SystemClock;
import android.security.KeyChain;
import android.text.format.DateUtils;
import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

public class BatteryStatsDBHelper extends SQLiteOpenHelper {
    public static final String ACTION_BATTERY_ABUSE = "sec.intent.action.BATTERY_ABUSE";
    public static final int ALL = 2;
    public static final int ALL_ONLY_APP = 8;
    public static final int ALL_ONLY_APP_CONSUMER_LIST = 128;
    public static final int ALL_ONLY_APP_TOTAL = 32;
    static final String BATTERY_DELTA = "Battery_Delta";
    private static final String BATTERY_DELTA_QUERY_STRING = "SELECT batterydelta,lcd_condition FROM Battery_Delta";
    public static final int BATTERY_PERCENTAGE = 64;
    private static final String COLLECTION_QUERY_STRING = "SELECT lcd_condition,time,power FROM";
    private static final String COMMA = ",";
    private static final Uri CONTENT_URI = Uri.parse("content://com.samsung.android.sm/settings");
    private static final String DATABASE_NAME = "powerManager";
    private static final int DATABASE_VERSION = 7;
    private static final boolean DEBUG_LOG = false;
    private static final String DELETE_QUERY_STRING = "SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='Battery_Delta' AND name!='power_consuming_packages' AND name!='null'";
    private static final String KEY_ABUSIVE_TYPE = "abusive_type";
    private static final String KEY_BATTERY_DELTA = "batterydelta";
    private static final String KEY_BATTERY_PERC = "batterypercent";
    private static final String KEY_LCD = "lcd_condition";
    private static final String KEY_METADATA = "meta_data";
    private static final String KEY_NETWORK_USAGE = "network_usage";
    private static final String KEY_NOTIFIED_TIME = "notified_time";
    private static final String KEY_PACKAGE_NAME = "packageName";
    private static final String KEY_PERCENTAGE = "percentage";
    private static final String KEY_POWER = "power";
    private static final String KEY_TIME = "time";
    private static final String KEY_TOTALPOWER = "totalpower";
    private static final String KEY_USAGE_TIME = "usage_time";
    private static final String KEY_WAKEUP = "wakeup_count";
    private static final String LAST_ENTRY_QUERY_STRING = "SELECT time,totalpower,network_usage FROM";
    private static final String LAST_TOTAL_ENTRY_QUERY_STRING = "SELECT time,totalpower,batterypercent FROM [all]";
    public static final int LCD_OFF = 4;
    public static final int LCD_OFF_ONLY_APP = 16;
    private static final long MAX_TIME_DIFF = 86400000;
    private static final long MILLIS_IN_DAY = 86400000;
    private static final long MILLIS_IN_HOUR = 3600000;
    static final String NAME_END = "]";
    static final String NAME_START = "[";
    private static final int NETWORK_USAGE = 32;
    private static final String ONE_HR_BATTERY_THRESHOLD = "one_hr_battery_threshold";
    private static final String ONE_HR_HEALING_CAMP_THRESHOLD = "one_hr_healingcamp_threshold";
    static final String PACKAGE_QUERY_STRING = "SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='Battery_Delta' AND name!='power_consuming_packages' AND name!='null' AND name!='all'";
    private static final String PERC_QUERY_STRING = "SELECT lcd_condition,time,batterypercent FROM Battery_Delta";
    static final String POWER_CONSUMING_PACKAGES = "power_consuming_packages";
    private static final String RATE_QUERY_STRING = "SELECT usage_time,power FROM";
    private static final int SCREEN_OFF = 8;
    private static final int SCREEN_ON = 4;
    private static final String SETTING_KEY = "key";
    private static final String SETTING_VALUE = "value";
    private static final String SIGNATURE_END = ")";
    private static final String SIGNATURE_START = "(";
    private static final int SYSTEM_APP = 16;
    private static final String TAG = "BatteryStatsDBHelper";
    private static final long TIME_DIFF_THRESHOLD = 2000;
    private static final String TOTAL_POWER = "all";
    private static final String TYPE_BLE = "ble";
    private static final String TYPE_GPS = "gps";
    private static final String TYPE_WAKELOCK = "wakelock";
    private static BatteryStatsDBHelper mBatteryStatsDBHelper;
    private static Context mContext;
    private static HashMap<String, Long> notifiedPowerDrainingApps = new HashMap();
    public static double sBatteryCapacity;
    private static long sRealTimeSnapshot;
    private static long sSystemTimeSnapshot;
    private static HashMap<String, Double> unNotifiedAbuserMap = new HashMap();
    private double ONE_HOUR_THRESHOLD = 3.0d;
    private double ONE_HOUR_THRESHOLD_HEALING_CAMP = 1.0d;
    private BroadcastReceiver mFakeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BatteryStatsDBHelper.printDebugLog(BatteryStatsDBHelper.TAG, "Sending fake excessive power drain broadcast");
            BatteryStatsDBHelper.mContext.sendBroadcast(new Intent(BatteryStatsDBHelper.ACTION_BATTERY_ABUSE));
        }
    };

    private static class LastDatabaseInfo {
        private long networkUsage;
        private long time;
        private double totalPower;

        private LastDatabaseInfo(long time, double totalPower, long networkUsage) {
            this.time = time;
            this.totalPower = totalPower;
            this.networkUsage = networkUsage;
        }
    }

    private static class LastTotalDatabaseInfo {
        private int batteryLevel;
        private double totalPower;

        private LastTotalDatabaseInfo(double totalPower, int batteryLevel) {
            this.totalPower = totalPower;
            this.batteryLevel = batteryLevel;
        }
    }

    private static class TotalTableData {
        private int batteryDeltaForOneDay;
        private int batteryDeltaForOneHour;
        private double totalPowerForOneDay;
        private double totalPowerForOneHour;

        private TotalTableData(double oneHourPower, int oneHourDelta, double oneDayPower, int oneDayDelta) {
            this.totalPowerForOneHour = oneHourPower;
            this.batteryDeltaForOneHour = oneHourDelta;
            this.totalPowerForOneDay = oneDayPower;
            this.batteryDeltaForOneDay = oneDayDelta;
        }
    }

    public synchronized void deleteRecordsExceptLatest(int r10) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x0023 in {9, 14, 17, 29, 31, 37, 44, 50, 53, 54, 58, 60, 61, 62, 63, 64, 65} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r9 = this;
        monitor-enter(r9);
        if (r10 != 0) goto L_0x0027;
    L_0x0003:
        r2 = r9.getWritableDatabase();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r2.beginTransaction();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r7 = 7;
        r8 = 7;
        r9.onUpgrade(r2, r7, r8);	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r2.setTransactionSuccessful();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r2.endTransaction();
    L_0x0015:
        monitor-exit(r9);
        return;
    L_0x0017:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r2.endTransaction();
        goto L_0x0015;
    L_0x001f:
        r7 = move-exception;
        monitor-exit(r9);
        throw r7;
    L_0x0022:
        r7 = move-exception;
        r2.endTransaction();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        throw r7;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x0027:
        r2 = r9.getReadableDatabase();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r5.<init>();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r0 = "SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'null'";	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r1 = 0;
        r7 = "SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'null'";	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        r8 = 0;	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        r1 = r2.rawQuery(r7, r8);	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        r7 = r1.moveToFirst();	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        if (r7 == 0) goto L_0x008c;	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
    L_0x0040:
        r7 = r1.isAfterLast();	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        if (r7 != 0) goto L_0x008c;	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
    L_0x0046:
        r7 = "name";	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        r7 = r1.getColumnIndex(r7);	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        r6 = r1.getString(r7);	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        if (r6 == 0) goto L_0x005c;	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
    L_0x0053:
        r7 = r6.length();	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        if (r7 <= 0) goto L_0x005c;	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
    L_0x0059:
        r5.add(r6);	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
    L_0x005c:
        r1.moveToNext();	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        goto L_0x0040;
    L_0x0060:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ Exception -> 0x0060, all -> 0x0092 }
        if (r1 == 0) goto L_0x0069;
    L_0x0066:
        r1.close();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x0069:
        r2 = r9.getWritableDatabase();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r2.beginTransaction();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r4 = r5.iterator();	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
    L_0x0074:
        r7 = r4.hasNext();	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
        if (r7 == 0) goto L_0x0099;	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
    L_0x007a:
        r6 = r4.next();	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
        r9.deleteRecordsExceptLatest(r2, r6, r10);	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
        goto L_0x0074;
    L_0x0084:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
        r2.endTransaction();
        goto L_0x0015;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x008c:
        if (r1 == 0) goto L_0x0069;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x008e:
        r1.close();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        goto L_0x0069;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x0092:
        r7 = move-exception;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        if (r1 == 0) goto L_0x0098;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x0095:
        r1.close();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x0098:
        throw r7;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x0099:
        r2.setTransactionSuccessful();	 Catch:{ Exception -> 0x0084, all -> 0x00a1 }
        r2.endTransaction();
        goto L_0x0015;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
    L_0x00a1:
        r7 = move-exception;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r2.endTransaction();	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        throw r7;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsDBHelper.deleteRecordsExceptLatest(int):void");
    }

    private static void printDebugLog(String tag, String msg) {
    }

    private BatteryStatsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 7);
        mContext = context;
        sSystemTimeSnapshot = System.currentTimeMillis();
        sRealTimeSnapshot = SystemClock.elapsedRealtime();
        registerReceiver();
    }

    public static synchronized BatteryStatsDBHelper getInstance(Context context) {
        BatteryStatsDBHelper batteryStatsDBHelper;
        synchronized (BatteryStatsDBHelper.class) {
            if (mBatteryStatsDBHelper == null) {
                mBatteryStatsDBHelper = new BatteryStatsDBHelper(context);
            }
            batteryStatsDBHelper = mBatteryStatsDBHelper;
        }
        return batteryStatsDBHelper;
    }

    private Set<String> getAppUsageStats() {
        Set<String> launchedPackageSet = new HashSet();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService("usagestats");
        Calendar cal = Calendar.getInstance();
        cal.add(6, -1);
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(4, cal.getTimeInMillis(), System.currentTimeMillis());
        if (stats != null) {
            int count = stats.size();
            for (int i = 0; i < count; i++) {
                String pkgName = ((UsageStats) stats.get(i)).getPackageName();
                printDebugLog(TAG, "LaunchedPackageSet contains " + pkgName);
                launchedPackageSet.add(pkgName);
            }
        }
        return launchedPackageSet;
    }

    private void registerReceiver() {
        mContext.registerReceiver(this.mFakeReceiver, new IntentFilter("send.battery.drain.broadcast"));
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Battery_Delta(lcd_condition INTEGER,time LONG,batterydelta INTEGER,batterypercent INTEGER)");
        db.execSQL("CREATE TABLE power_consuming_packages(time LONG,packageName TEXT,percentage DOUBLE,notified_time LONG,abusive_type TEXT,meta_data TEXT)");
        db.execSQL("CREATE TABLE [all](time LONG,power DOUBLE,totalpower DOUBLE,batterydelta INTEGER,batterypercent INTEGER)");
    }

    private boolean createTable(SQLiteDatabase db, String TABLE_PROCESS) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS [" + TABLE_PROCESS + NAME_END + SIGNATURE_START + "lcd_condition" + " INTEGER" + "," + "time" + " LONG" + "," + "power" + " DOUBLE" + "," + "totalpower" + " DOUBLE" + "," + "network_usage" + " LONG" + "," + "usage_time" + " LONG" + "," + KEY_WAKEUP + " INTEGER" + SIGNATURE_END);
            return true;
        } catch (SQLException e) {
            printDebugLog(TAG, "Table not created for " + TABLE_PROCESS + " Exception : " + e.getMessage());
            return false;
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<String> tables = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tableName = cursor.getString(1);
            if (!(tableName.equals("android_metadata") || tableName.equals("sqlite_sequence"))) {
                tables.add(tableName);
            }
            cursor.moveToNext();
        }
        cursor.close();
        for (String tableName2 : tables) {
            db.execSQL("DROP TABLE IF EXISTS [" + tableName2 + NAME_END);
        }
        onCreate(db);
    }

    private synchronized void addBatteryDelta(boolean writeForScreenChange, long time, int batteryDelta, int currentBatteryPercent, SQLiteDatabase db) {
        boolean screenCondition = writeForScreenChange ? !BatteryStatsDumper.sScreenOn : BatteryStatsDumper.sScreenOn;
        int lcdCondition = screenCondition ? 4 : 8;
        maintainDayData(db, time);
        ContentValues values = new ContentValues();
        values.put("lcd_condition", Integer.valueOf(lcdCondition));
        values.put("time", Long.valueOf(time));
        values.put("batterydelta", Integer.valueOf(batteryDelta));
        values.put("batterypercent", Integer.valueOf(currentBatteryPercent));
        db.insert(BATTERY_DELTA, null, values);
    }

    synchronized void addStatsToDB(boolean writeForScreenChange, boolean screenOn, long time, ArrayList<PowerObject> entries, int batteryDelta, int currentBatteryPercent, double curTotalPower, int dischargeLevel) {
        boolean screenCondition = writeForScreenChange ? !screenOn : screenOn;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int lcdCondition = screenCondition ? 4 : 8;
        try {
            double power;
            ContentValues values;
            Iterator i$ = entries.iterator();
            while (i$.hasNext()) {
                PowerObject entry = (PowerObject) i$.next();
                if (createTable(db, entry.packageName)) {
                    printDebugLog(TAG, "Writing for package : " + entry.packageName);
                    double totalPower = entry.powerUid;
                    long networkUsage = entry.networkUsage;
                    long uidUsageTime = entry.usageTime;
                    int finalLcdCondition = lcdCondition;
                    LastDatabaseInfo lastData = getLastEntry(db, entry.packageName);
                    if (lastData != null) {
                        double lastPower = lastData.totalPower;
                        printDebugLog(TAG, "Written to db : lastpower = " + lastPower);
                        double diff = totalPower - lastPower;
                        if (diff >= 0.0d) {
                            power = diff;
                            printDebugLog(TAG, "Written to db : power[diff] = " + power + (screenCondition ? " on" : " off"));
                        } else {
                            power = 0.0d;
                            printDebugLog(TAG, "Written to db : power[strange] = " + totalPower);
                        }
                        if (networkUsage - lastData.networkUsage != 0) {
                            finalLcdCondition |= 32;
                        }
                    } else {
                        power = 0.0d;
                        if (networkUsage != 0) {
                            finalLcdCondition |= 32;
                        }
                        printDebugLog(TAG, "Written to db : power[noLastSipper] = " + totalPower);
                    }
                    if (entry.isSystem) {
                        finalLcdCondition |= 16;
                    }
                    values = new ContentValues();
                    values.put("lcd_condition", Integer.valueOf(finalLcdCondition));
                    values.put("time", Long.valueOf(time));
                    values.put("power", Double.valueOf(power));
                    values.put("totalpower", Double.valueOf(totalPower));
                    values.put("network_usage", Long.valueOf(networkUsage));
                    values.put("usage_time", Long.valueOf(uidUsageTime));
                    values.put(KEY_WAKEUP, Integer.valueOf(entry.wakeUpCount));
                    db.insert(NAME_START + entry.packageName + NAME_END, null, values);
                }
            }
            printDebugLog(TAG, "Writing for db : Total = all");
            LastTotalDatabaseInfo mLastEntry = getLastTotalEntry(db);
            power = 0.0d;
            int delta = 0;
            if (mLastEntry != null) {
                power = curTotalPower - mLastEntry.totalPower;
                delta = dischargeLevel - mLastEntry.batteryLevel;
                if (power < 0.0d) {
                    power = 0.0d;
                }
                if (delta < 0) {
                    delta = 0;
                }
            }
            values = new ContentValues();
            values.put("time", Long.valueOf(time));
            values.put("power", Double.valueOf(power));
            values.put("totalpower", Double.valueOf(curTotalPower));
            values.put("batterydelta", Integer.valueOf(delta));
            values.put("batterypercent", Integer.valueOf(dischargeLevel));
            db.insert("[all]", null, values);
            printDebugLog(TAG, "Written to db : power = " + power);
            addBatteryDelta(writeForScreenChange, time, batteryDelta, currentBatteryPercent, db);
            checkForPowerThreshold(db, time, curTotalPower);
            db.setTransactionSuccessful();
        } catch (IllegalStateException e) {
            printDebugLog(TAG, "addStatsToDB IllegalStateException : " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public synchronized void abusiveDetectionFromHCamp(List<String> list, String type) {
        try {
            long time = System.currentTimeMillis();
            SQLiteDatabase db = getReadableDatabase();
            TotalTableData totalTableDataSet = collectTotalTableData(db, time);
            if (totalTableDataSet.totalPowerForOneHour <= 0.0d) {
                printDebugLog(TAG, "abusiveDetectionFromHCamp:: no total power");
            } else {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String meta = (String) list.get(i);
                    String packageName = meta.split(",")[0];
                    printDebugLog(TAG, "abusiveDetectionFromHCamp:: packageName:" + packageName + " meta:" + meta);
                    checkPowerAbuseLocked(db, totalTableDataSet, packageName, type, meta, time);
                }
                mContext.sendBroadcast(new Intent(ACTION_BATTERY_ABUSE));
            }
        } catch (Exception e) {
            printDebugLog(TAG, "abusiveDetectionFromHCamp exception : " + e.getMessage());
        }
    }

    public synchronized void abusiveDetectionFromHCamp(String packageName, String type, String metaData) {
        try {
            long time = System.currentTimeMillis();
            SQLiteDatabase db = getReadableDatabase();
            TotalTableData totalTableDataSet = collectTotalTableData(db, time);
            if (totalTableDataSet.totalPowerForOneHour <= 0.0d) {
                printDebugLog(TAG, "abusiveDetectionFromHCamp:: no total power");
            } else {
                checkPowerAbuseLocked(db, totalTableDataSet, packageName, type, metaData, time);
                mContext.sendBroadcast(new Intent(ACTION_BATTERY_ABUSE));
            }
        } catch (Exception e) {
            printDebugLog(TAG, "abusiveDetectionFromHCamp exception : " + e.getMessage());
        }
    }

    void checkPowerAbuseLocked(SQLiteDatabase db, TotalTableData totalTableDataSet, String packageName, String type, String metaData, long curTime) {
        printDebugLog(TAG, "checkPowerAbuse called : " + curTime);
        try {
            Cursor cursor = db.query(NAME_START + packageName + NAME_END, new String[]{"time", "lcd_condition", "power"}, null, null, null, null, null);
            int lcdColumn = cursor.getColumnIndex("lcd_condition");
            int timeColumn = cursor.getColumnIndex("time");
            int powerColumn = cursor.getColumnIndex("power");
            double lcdOffPowerInLastHour = 0.0d;
            while (!cursor.isBeforeFirst() && curTime - cursor.getLong(timeColumn) < 3600000) {
                double curPower = cursor.getDouble(powerColumn);
                if ((cursor.getInt(lcdColumn) & 8) != 8) {
                    curPower = 0.0d;
                }
                lcdOffPowerInLastHour += curPower;
                cursor.moveToPrevious();
            }
            cursor.close();
            double percentage = (lcdOffPowerInLastHour / totalTableDataSet.totalPowerForOneHour) * ((double) totalTableDataSet.batteryDeltaForOneHour);
            if (percentage > ((double) totalTableDataSet.batteryDeltaForOneHour)) {
                printDebugLog(TAG, "Strange!!. Percentage is greater than battery delta");
                percentage = (double) totalTableDataSet.batteryDeltaForOneHour;
            }
            if (!type.equals(TYPE_WAKELOCK)) {
                if (!type.equals(TYPE_GPS)) {
                    if (type.equals(TYPE_BLE)) {
                        printDebugLog(TAG, "Setting percentage to 1 as type is: " + type);
                        percentage = 1.0d;
                    }
                    printDebugLog(TAG, packageName + " has consumed more than " + this.ONE_HOUR_THRESHOLD_HEALING_CAMP + "% battery in last 1 hour when LCD was off and found abusive by Hcamp type:" + type + " meta:" + metaData + "total power in lastonehour:" + lcdOffPowerInLastHour + "percentage:" + percentage);
                    addPowerConsumingApps(db, curTime, packageName, type, metaData, percentage);
                    printDebugLog(TAG, "checkPowerAbuse finished");
                }
            }
            if (percentage < this.ONE_HOUR_THRESHOLD_HEALING_CAMP) {
                printDebugLog(TAG, "percentage i.e. " + percentage + " is less than ONE_HOUR_THRESHOLD for healing camp i.e. " + this.ONE_HOUR_THRESHOLD_HEALING_CAMP);
                return;
            }
            printDebugLog(TAG, packageName + " has consumed more than " + this.ONE_HOUR_THRESHOLD_HEALING_CAMP + "% battery in last 1 hour when LCD was off and found abusive by Hcamp type:" + type + " meta:" + metaData + "total power in lastonehour:" + lcdOffPowerInLastHour + "percentage:" + percentage);
            addPowerConsumingApps(db, curTime, packageName, type, metaData, percentage);
        } catch (Exception e) {
            printDebugLog(TAG, "checkPowerAbuse SQLiteException exception:" + e.getMessage());
        }
        printDebugLog(TAG, "checkPowerAbuse finished");
    }

    private void checkForPowerThreshold(SQLiteDatabase db, long curTime, double power) {
        printDebugLog(TAG, "checkForPowerThreshold called : " + curTime);
        Cursor c = db.rawQuery(PACKAGE_QUERY_STRING, null);
        if (c.moveToFirst()) {
            ArrayList<UidSipper> appSippersForLastHour = new ArrayList();
            ArrayList<UidSipper> appSippersFor24Hours = new ArrayList();
            maintainNotifiedAppList(curTime);
            if (BatteryStatsDumper.mIsOnBattery) {
                TotalTableData totalTableDataSet = collectTotalTableData(db, curTime);
                if (totalTableDataSet.totalPowerForOneHour <= 0.0d) {
                    c.close();
                    return;
                }
                String[] requestingColumn = new String[]{"time", "lcd_condition", "power"};
                while (!c.isAfterLast()) {
                    String packageName = c.getString(c.getColumnIndex(KeyChain.EXTRA_NAME));
                    printDebugLog(TAG, "In checkForPowerThreshold, Package name = " + packageName);
                    Cursor cursor = db.query(NAME_START + packageName + NAME_END, requestingColumn, null, null, null, null, null);
                    if (cursor.moveToLast()) {
                        int lcdColumn = cursor.getColumnIndex("lcd_condition");
                        int timeColumn = cursor.getColumnIndex("time");
                        int powerColumn = cursor.getColumnIndex("power");
                        if ((cursor.getInt(lcdColumn) & 16) == 16) {
                            c.moveToNext();
                            cursor.close();
                        } else {
                            double curPower;
                            UidSipper appSipper;
                            double lcdOffPowerInLastHour = 0.0d;
                            while (!cursor.isBeforeFirst() && curTime - cursor.getLong(timeColumn) < 3600000) {
                                curPower = cursor.getDouble(powerColumn);
                                if ((cursor.getInt(lcdColumn) & 8) != 8) {
                                    curPower = 0.0d;
                                }
                                lcdOffPowerInLastHour += curPower;
                                cursor.moveToPrevious();
                            }
                            if (lcdOffPowerInLastHour > 0.0d) {
                                appSipper = new UidSipper(packageName);
                                appSipper.setTotalPower(lcdOffPowerInLastHour);
                                appSippersForLastHour.add(appSipper);
                            }
                            double lcdOffPowerIn24Hours = lcdOffPowerInLastHour;
                            while (!cursor.isBeforeFirst()) {
                                curPower = cursor.getDouble(powerColumn);
                                if ((cursor.getInt(lcdColumn) & 8) != 8) {
                                    curPower = 0.0d;
                                }
                                lcdOffPowerIn24Hours += curPower;
                                cursor.moveToPrevious();
                            }
                            if (lcdOffPowerIn24Hours > 0.0d) {
                                appSipper = new UidSipper(packageName);
                                appSipper.setTotalPower(lcdOffPowerIn24Hours);
                                appSippersFor24Hours.add(appSipper);
                            }
                            cursor.close();
                            c.moveToNext();
                        }
                    } else {
                        db.execSQL("DROP TABLE IF EXISTS [" + packageName + NAME_END);
                        c.moveToNext();
                        cursor.close();
                    }
                }
                c.close();
                if (!appSippersForLastHour.isEmpty() || !appSippersFor24Hours.isEmpty()) {
                    HashMap<String, Double> currentAbuserMap = new HashMap();
                    boolean sendBroadcast = BatteryStatsDumper.sScreenOn && !unNotifiedAbuserMap.isEmpty();
                    Iterator i$ = appSippersForLastHour.iterator();
                    while (i$.hasNext()) {
                        UidSipper sipper = (UidSipper) i$.next();
                        int oneHourTotalDelta = totalTableDataSet.batteryDeltaForOneHour;
                        double percentage = (sipper.getTotalPower() / totalTableDataSet.totalPowerForOneHour) * ((double) oneHourTotalDelta);
                        if (percentage > ((double) oneHourTotalDelta)) {
                            printDebugLog(TAG, "Strange!!. Percentage is greater than battery delta");
                            percentage = (double) oneHourTotalDelta;
                        }
                        printDebugLog(TAG, sipper.name + " has consumed " + percentage + "%");
                        if (percentage > this.ONE_HOUR_THRESHOLD) {
                            printDebugLog(TAG, sipper.name + " has consumed more than " + this.ONE_HOUR_THRESHOLD + "% battery in last 1 hour when LCD was off");
                            currentAbuserMap.put(sipper.name, Double.valueOf(percentage));
                            if (!notifiedPowerDrainingApps.containsKey(sipper.name)) {
                                if (BatteryStatsDumper.sScreenOn) {
                                    sendBroadcast |= 1;
                                } else {
                                    printDebugLog(TAG, sipper.name + " will be broadcasted abusive in screen on");
                                }
                                unNotifiedAbuserMap.put(sipper.name, Double.valueOf(percentage));
                            }
                        }
                    }
                    addPowerConsumingApps(db, curTime, currentAbuserMap);
                    if (BatteryStatsDumper.sScreenOn) {
                        markAsNotified(db, curTime, unNotifiedAbuserMap);
                        unNotifiedAbuserMap.clear();
                    }
                    if (sendBroadcast) {
                        printDebugLog(TAG, "Sending Battery abuse broadcast");
                        mContext.sendBroadcast(new Intent(ACTION_BATTERY_ABUSE));
                    }
                    printDebugLog(TAG, "checkForPowerThreshold finished");
                    return;
                }
                return;
            }
            c.close();
            return;
        }
        c.close();
        printDebugLog(TAG, "checkForPowerThreshold:: no tables found, closing cursor");
    }

    private TotalTableData collectTotalTableData(SQLiteDatabase db, long curTime) {
        SQLiteDatabase sQLiteDatabase = db;
        Cursor cursor = sQLiteDatabase.query("[all]", new String[]{"time", "power", "batterydelta"}, null, null, null, null, null);
        double oneHourPower = 0.0d;
        double oneDayPower = 0.0d;
        int oneHourBatteryDelta = 0;
        int oneDayBatteryDelta = 0;
        if (cursor.moveToLast()) {
            int timeColumn = cursor.getColumnIndex("time");
            int powerColumn = cursor.getColumnIndex("power");
            int batteryColumn = cursor.getColumnIndex("batterydelta");
            while (!cursor.isBeforeFirst() && curTime - cursor.getLong(timeColumn) < 3600000) {
                oneHourPower += cursor.getDouble(powerColumn);
                oneHourBatteryDelta += cursor.getInt(batteryColumn);
                cursor.moveToPrevious();
            }
            oneDayPower = oneHourPower;
            oneDayBatteryDelta = oneHourBatteryDelta;
            while (!cursor.isBeforeFirst()) {
                oneDayPower += cursor.getDouble(powerColumn);
                oneDayBatteryDelta += cursor.getInt(batteryColumn);
                cursor.moveToPrevious();
            }
        }
        cursor.close();
        return new TotalTableData(oneHourPower, oneHourBatteryDelta, oneDayPower, oneDayBatteryDelta);
    }

    private void deleteExceptDayData(SQLiteDatabase db, long curTime, String tableName) {
        long maxTime = curTime + TIME_DIFF_THRESHOLD;
        long minTime = curTime - DateUtils.DAY_IN_MILLIS;
        if (minTime < 0) {
            minTime = 0;
        }
        db.delete(NAME_START + tableName + NAME_END, "time > ? OR time <= ?", new String[]{String.valueOf(maxTime), String.valueOf(minTime)});
    }

    private void maintainDayData(SQLiteDatabase db, long curTime) {
        Iterator i$;
        printDebugLog(TAG, "Checking for old data before adding this new one at " + curTime);
        ArrayList<String> tableList = new ArrayList();
        String QUERY_GET_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'null'";
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'null'", null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    String tableName = c.getString(c.getColumnIndex(KeyChain.EXTRA_NAME));
                    if (tableName != null && tableName.length() > 0) {
                        tableList.add(tableName);
                    }
                    c.moveToNext();
                }
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            i$ = tableList.iterator();
            while (i$.hasNext()) {
                deleteExceptDayData(db, curTime, (String) i$.next());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        try {
            i$ = tableList.iterator();
            while (i$.hasNext()) {
                deleteExceptDayData(db, curTime, (String) i$.next());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void maintainNotifiedAppList(long curTime) {
        printDebugLog(TAG, " maintainNotifiedAppList called ");
        Iterator<Entry<String, Long>> iter = notifiedPowerDrainingApps.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Long> entry = (Entry) iter.next();
            String packageName = (String) entry.getKey();
            if (curTime - ((Long) entry.getValue()).longValue() >= 3600000) {
                printDebugLog(TAG, "Removing package name from notified list: " + packageName);
                iter.remove();
            }
        }
    }

    private void addPowerConsumingApps(SQLiteDatabase db, long curTime, HashMap<String, Double> abuserMap) {
        String[] columns = new String[]{"packageName"};
        String abusiveType = "battery";
        for (Entry<String, Double> entry : abuserMap.entrySet()) {
            String packageName = (String) entry.getKey();
            ContentValues values = new ContentValues();
            values.put("time", Long.valueOf(curTime));
            values.put("packageName", packageName);
            values.put(KEY_PERCENTAGE, (Double) entry.getValue());
            values.put(KEY_ABUSIVE_TYPE, abusiveType);
            SQLiteDatabase sQLiteDatabase = db;
            Cursor cursor = sQLiteDatabase.query(POWER_CONSUMING_PACKAGES, columns, "packageName = ?", new String[]{packageName}, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                db.insert(POWER_CONSUMING_PACKAGES, null, values);
                printDebugLog(TAG, "Inserted abuse of " + packageName);
            } else {
                SQLiteDatabase sQLiteDatabase2 = db;
                sQLiteDatabase2.update(POWER_CONSUMING_PACKAGES, values, "packageName = ?", new String[]{packageName});
                printDebugLog(TAG, "Updated abuse of " + packageName);
            }
            cursor.close();
        }
    }

    private void addPowerConsumingApps(SQLiteDatabase db, long curTime, String packageName, String abusiveType, String metadata, double percentage) {
        String[] columns = new String[]{"packageName"};
        ContentValues values = new ContentValues();
        values.put("time", Long.valueOf(curTime));
        values.put("packageName", packageName);
        values.put(KEY_PERCENTAGE, Double.valueOf(percentage));
        values.put(KEY_ABUSIVE_TYPE, abusiveType);
        values.put(KEY_METADATA, metadata);
        SQLiteDatabase sQLiteDatabase = db;
        Cursor cursor = sQLiteDatabase.query(POWER_CONSUMING_PACKAGES, columns, "packageName = ?", new String[]{packageName}, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            db.insert(POWER_CONSUMING_PACKAGES, null, values);
            printDebugLog(TAG, "Inserted abuse of " + packageName);
        } else {
            db.update(POWER_CONSUMING_PACKAGES, values, "packageName = ?", new String[]{packageName});
            printDebugLog(TAG, "Updated abuse of " + packageName);
        }
        cursor.close();
    }

    private void markAsNotified(SQLiteDatabase db, long curTime, HashMap<String, Double> abuserMap) {
        ContentValues notiValues = new ContentValues();
        notiValues.put(KEY_NOTIFIED_TIME, Long.valueOf(curTime));
        for (String packageName : abuserMap.keySet()) {
            notifiedPowerDrainingApps.put(packageName, Long.valueOf(curTime));
            db.update(POWER_CONSUMING_PACKAGES, notiValues, "packageName = ?", new String[]{packageName});
            printDebugLog(TAG, "Updated abuse notification time for " + packageName);
        }
    }

    private LastDatabaseInfo getLastEntry(SQLiteDatabase db, String packageName) {
        Cursor cursor = db.rawQuery("SELECT time,totalpower,network_usage FROM[" + packageName + NAME_END, null);
        LastDatabaseInfo lastData = null;
        if (cursor.moveToLast()) {
            lastData = new LastDatabaseInfo(cursor.getLong(cursor.getColumnIndex("time")), cursor.getDouble(cursor.getColumnIndex("totalpower")), cursor.getLong(cursor.getColumnIndex("network_usage")));
        }
        cursor.close();
        return lastData;
    }

    private LastTotalDatabaseInfo getLastTotalEntry(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(LAST_TOTAL_ENTRY_QUERY_STRING, null);
        LastTotalDatabaseInfo lastData = null;
        if (cursor.moveToLast()) {
            lastData = new LastTotalDatabaseInfo(cursor.getDouble(cursor.getColumnIndex("totalpower")), cursor.getInt(cursor.getColumnIndex("batterypercent")));
        }
        cursor.close();
        return lastData;
    }

    synchronized double getAverageLevelDropPerHour(String packageName) {
        double i;
        try {
            String selectQuery = "SELECT usage_time,power FROM[" + packageName + NAME_END;
            printDebugLog(TAG, "getAverageLevelDropPerHour:: start");
            printDebugLog(TAG, "getAverageLevelDropPerHour:: for package:" + packageName);
            Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);
            int usageTimeColumn = cursor.getColumnIndex("usage_time");
            int powerColumn = cursor.getColumnIndex("power");
            long appUsageTime = 0;
            double power = 0.0d;
            if (cursor.moveToLast()) {
                long appTotalTime = cursor.getLong(usageTimeColumn);
                appUsageTime = appTotalTime;
                printDebugLog(TAG, "getAverageLevelDropPerHour:: start loop appTotalTime:" + appTotalTime);
                power = cursor.getDouble(powerColumn);
                while (cursor.moveToPrevious()) {
                    long tempTime = cursor.getLong(usageTimeColumn);
                    printDebugLog(TAG, "getAverageLevelDropPerHour::  loop tempTime:" + tempTime);
                    printDebugLog(TAG, "getAverageLevelDropPerHour::  loop before appTotalTime:" + appTotalTime);
                    printDebugLog(TAG, "getAverageLevelDropPerHour::  loop before appUsageTime:" + appUsageTime);
                    if (tempTime > appTotalTime) {
                        appUsageTime += tempTime;
                    }
                    appTotalTime = tempTime;
                    printDebugLog(TAG, "getAverageLevelDropPerHour::  loop after appUsageTime:" + appUsageTime);
                    printDebugLog(TAG, "getAverageLevelDropPerHour::  loop after appTotalTime:" + appTotalTime);
                    power += cursor.getDouble(powerColumn);
                    printDebugLog(TAG, "getAverageLevelDropPerHour::  loop after power:" + power);
                }
            }
            cursor.close();
            printDebugLog(TAG, "AverageLevelDropCalculation : Package = " + packageName + " Power = " + power + " Usage Time = " + appUsageTime + " ms");
            if (appUsageTime == 0) {
                appUsageTime = 1;
            }
            printDebugLog(TAG, " getAverageLevelDropPerHour sBatteryCapacity : " + sBatteryCapacity + "appUsageTime : " + appUsageTime);
            i = (((100.0d * power) * 3600.0d) * 1000.0d) / (((double) appUsageTime) * sBatteryCapacity);
            printDebugLog(TAG, "getAverageLevelDropPerHour::  returning i:" + i);
        } catch (Exception e) {
            printDebugLog(TAG, "getAverageLevelDropPerHour::  Exception");
            i = 0.0d;
        }
        return i;
    }

    synchronized void deleteTableForApp(String packageName) {
        String[] strArr;
        SQLException e;
        Throwable th;
        printDebugLog(TAG, "deleting table for application:" + packageName);
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = new String[]{"packageName"};
        Cursor cursor = null;
        try {
            db.execSQL("DROP TABLE IF EXISTS [" + packageName + NAME_END);
            cursor = db.query(POWER_CONSUMING_PACKAGES, columns, "packageName = ?", new String[]{packageName}, null, null, null);
            if (cursor == null || cursor.getCount() <= 0) {
                printDebugLog(TAG, "Package not found in the table power_consuming_packages");
            } else {
                String[] requestingColumn = new String[]{packageName};
                try {
                    db.delete(POWER_CONSUMING_PACKAGES, "packageName = ?", requestingColumn);
                    strArr = requestingColumn;
                } catch (SQLException e2) {
                    e = e2;
                    strArr = requestingColumn;
                    try {
                        printDebugLog(TAG, " deleteTableForApp SQLException:" + e.getMessage());
                        if (cursor != null) {
                            cursor.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    strArr = requestingColumn;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e3) {
            e = e3;
            printDebugLog(TAG, " deleteTableForApp SQLException:" + e.getMessage());
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    synchronized void modifyThresholdIfNecessary() {
        printDebugLog(TAG, "modifyThresholdIfNecessary called ");
        try {
            Cursor cThreshold = mContext.getContentResolver().query(CONTENT_URI, new String[]{SETTING_KEY, SETTING_VALUE}, null, null, null);
            if (cThreshold == null) {
                printDebugLog(TAG, "cursor null in modifyThresholdIfNecessary");
            } else if (cThreshold.moveToFirst()) {
                while (!cThreshold.isAfterLast()) {
                    if (ONE_HR_BATTERY_THRESHOLD.equals(cThreshold.getString(0))) {
                        this.ONE_HOUR_THRESHOLD = Double.parseDouble(cThreshold.getString(1));
                    }
                    cThreshold.moveToNext();
                }
                cThreshold.close();
            } else {
                printDebugLog(TAG, "cursor empty in modifyThresholdIfNecessary");
                cThreshold.close();
            }
        } catch (Exception e) {
            printDebugLog(TAG, "modifyThresholdIfNecessary remote exception ");
            e.printStackTrace();
        }
    }

    synchronized void modifyHealingCampThresholdIfNecessary() {
        printDebugLog(TAG, "modifyHealingCampThresholdIfNecessary called ");
        try {
            Cursor cThreshold = mContext.getContentResolver().query(CONTENT_URI, new String[]{SETTING_KEY, SETTING_VALUE}, null, null, null);
            if (cThreshold == null) {
                printDebugLog(TAG, "cursor null in modifyHealingCampThresholdIfNecessary");
            } else if (cThreshold.moveToFirst()) {
                while (!cThreshold.isAfterLast()) {
                    if (ONE_HR_HEALING_CAMP_THRESHOLD.equals(cThreshold.getString(0))) {
                        this.ONE_HOUR_THRESHOLD_HEALING_CAMP = Double.parseDouble(cThreshold.getString(1));
                    }
                    cThreshold.moveToNext();
                }
                cThreshold.close();
            } else {
                printDebugLog(TAG, "cursor empty in modifyHealingCampThresholdIfNecessary");
                cThreshold.close();
            }
        } catch (Exception e) {
            printDebugLog(TAG, "modifyHealingCampThresholdIfNecessary remote exception ");
            e.printStackTrace();
        }
    }

    public synchronized int getBatteryTotalDelta(int screenCondition) {
        int totalDelta;
        Cursor cursor = getReadableDatabase().rawQuery(BATTERY_DELTA_QUERY_STRING, null);
        int batteryDeltaColumn = cursor.getColumnIndex("batterydelta");
        int lcdColumn = cursor.getColumnIndex("lcd_condition");
        totalDelta = 0;
        if (cursor.moveToFirst()) {
            do {
                if (screenCondition == 2) {
                    totalDelta += cursor.getInt(batteryDeltaColumn);
                } else if (cursor.getInt(lcdColumn) == 8) {
                    totalDelta += cursor.getInt(batteryDeltaColumn);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return totalDelta;
    }

    public byte[] getUsageList(int screenCondition) throws IllegalArgumentException {
        switch (screenCondition) {
            case 2:
                printDebugLog(TAG, "Requesting lcd all data");
                return getScreenOffUsageList(false);
            case 4:
                printDebugLog(TAG, "Requesting lcd off data");
                return getScreenOffUsageList(true);
            case 8:
                printDebugLog(TAG, "Requesting lcd all data(only app)");
                return getScreenOffUsageListOnlyApp(false);
            case 16:
                printDebugLog(TAG, "Requesting lcd off data(only app)");
                return getScreenOffUsageListOnlyApp(true);
            case 32:
                printDebugLog(TAG, "Requesting summation of lcd all data(only app)");
                return getTotalUsageList();
            case 64:
                printDebugLog(TAG, "Requesting battery percentage data");
                return getBatteryPercentageList();
            case 128:
                printDebugLog(TAG, "Requesting lcd all data(only app) with power consumer list");
                return getConsumerListOnlyApp();
            default:
                throw new IllegalArgumentException("Wrong screen condition. Please check BatteryStatsDBHandler.java");
        }
    }

    private synchronized byte[] getScreenOffUsageList(boolean needOffList) {
        byte[] data;
        SQLiteDatabase db = getReadableDatabase();
        UidSipperImpl result = new UidSipperImpl();
        Cursor c = db.rawQuery(PACKAGE_QUERY_STRING, null);
        double allUidTotalPower = 0.0d;
        if (c.moveToFirst()) {
            long curTime = System.currentTimeMillis();
            Set<String> launchedAppList = getAppUsageStats();
            while (!c.isAfterLast()) {
                String val = c.getString(c.getColumnIndex(KeyChain.EXTRA_NAME));
                Cursor cursor = db.rawQuery("SELECT lcd_condition,time,power FROM[" + val + NAME_END, null);
                cursor.moveToFirst();
                int lcdColumn = cursor.getColumnIndex("lcd_condition");
                int timeColumn = cursor.getColumnIndex("time");
                int powerColumn = cursor.getColumnIndex("power");
                if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                    cursor.moveToNext();
                }
                UidSipper sipper = new UidSipper(val);
                double totalPower = 0.0d;
                boolean isUsingNetwork = false;
                boolean isSystemApp = (cursor.getInt(lcdColumn) & 16) == 16;
                while (!cursor.isAfterLast()) {
                    int lcdNtype = cursor.getInt(lcdColumn);
                    long time;
                    double power;
                    if (!needOffList) {
                        time = cursor.getLong(timeColumn);
                        boolean lcdOn = (lcdNtype & 4) == 4;
                        power = cursor.getDouble(powerColumn);
                        sipper.addBatterySipStat(time, lcdOn, power, 0.0d);
                        totalPower += power;
                        isUsingNetwork |= (lcdNtype & 32) == 32 ? 1 : 0;
                    } else if ((lcdNtype & 8) == 8) {
                        time = cursor.getLong(timeColumn);
                        power = cursor.getDouble(powerColumn);
                        sipper.addBatterySipStat(time, false, power, 0.0d);
                        totalPower += power;
                        isUsingNetwork |= (lcdNtype & 32) == 32 ? 1 : 0;
                    }
                    cursor.moveToNext();
                }
                printDebugLog(TAG, val + " power:" + totalPower);
                if (!sipper.batterySipStats.isEmpty()) {
                    sipper.setTotalPower(totalPower);
                    allUidTotalPower += totalPower;
                    if (launchedAppList.contains(val)) {
                        sipper.makeUserLaunch();
                    } else if (isUsingNetwork && !isSystemApp) {
                        sipper.makeNetworkUser();
                    }
                    result.addUidSipper(sipper);
                    printDebugLog(TAG, "added");
                }
                cursor.close();
                c.moveToNext();
            }
            if (allUidTotalPower <= 0.0d) {
                allUidTotalPower = 1.0d;
            }
            result.setFinalPower(allUidTotalPower);
        }
        c.close();
        Parcel out = Parcel.obtain();
        result.writeToParcel(out);
        data = out.marshall();
        out.recycle();
        return data;
    }

    private synchronized byte[] getScreenOffUsageListOnlyApp(boolean needOffList) {
        byte[] data;
        SQLiteDatabase db = getReadableDatabase();
        UidSipperImpl result = new UidSipperImpl();
        Cursor c = db.rawQuery(PACKAGE_QUERY_STRING, null);
        double allUidTotalPower = 0.0d;
        if (c.moveToFirst()) {
            long curTime = System.currentTimeMillis();
            Set<String> launchedAppList = getAppUsageStats();
            while (!c.isAfterLast()) {
                String val = c.getString(c.getColumnIndex(KeyChain.EXTRA_NAME));
                Cursor cursor = db.rawQuery("SELECT lcd_condition,time,power FROM[" + val + NAME_END, null);
                cursor.moveToFirst();
                int lcdColumn = cursor.getColumnIndex("lcd_condition");
                int timeColumn = cursor.getColumnIndex("time");
                int powerColumn = cursor.getColumnIndex("power");
                if ((cursor.getInt(lcdColumn) & 16) == 16) {
                    printDebugLog(TAG, "System app");
                    cursor.close();
                    c.moveToNext();
                } else {
                    if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                        cursor.moveToNext();
                    }
                    UidSipper sipper = new UidSipper(val);
                    double totalPower = 0.0d;
                    boolean isUsingNetwork = false;
                    while (!cursor.isAfterLast()) {
                        int lcdNtype = cursor.getInt(lcdColumn);
                        long time;
                        double power;
                        if (!needOffList) {
                            time = cursor.getLong(timeColumn);
                            boolean lcdOn = (lcdNtype & 4) == 4;
                            power = cursor.getDouble(powerColumn);
                            sipper.addBatterySipStat(time, lcdOn, power, 0.0d);
                            totalPower += power;
                            isUsingNetwork |= (lcdNtype & 32) == 32 ? 1 : 0;
                        } else if (lcdNtype == 8) {
                            time = cursor.getLong(timeColumn);
                            power = cursor.getDouble(powerColumn);
                            sipper.addBatterySipStat(time, false, power, 0.0d);
                            totalPower += power;
                            isUsingNetwork |= (lcdNtype & 32) == 32 ? 1 : 0;
                        }
                        cursor.moveToNext();
                    }
                    printDebugLog(TAG, val + " power:" + totalPower);
                    if (!sipper.batterySipStats.isEmpty()) {
                        sipper.setTotalPower(totalPower);
                        allUidTotalPower += totalPower;
                        if (launchedAppList.contains(val)) {
                            sipper.makeUserLaunch();
                        } else if (isUsingNetwork) {
                            sipper.makeNetworkUser();
                        }
                        result.addUidSipper(sipper);
                        printDebugLog(TAG, "added");
                    }
                    cursor.close();
                    c.moveToNext();
                }
            }
            if (allUidTotalPower <= 0.0d) {
                allUidTotalPower = 1.0d;
            }
            result.setFinalPower(allUidTotalPower);
        }
        c.close();
        Parcel out = Parcel.obtain();
        result.writeToParcel(out);
        data = out.marshall();
        out.recycle();
        return data;
    }

    private synchronized byte[] getTotalUsageList() {
        byte[] data;
        SQLiteDatabase db = getReadableDatabase();
        UidSipperImpl result = new UidSipperImpl();
        long curTime = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT lcd_condition,time,power FROM[all]", null);
        int lcdColumn = cursor.getColumnIndex("lcd_condition");
        int timeColumn = cursor.getColumnIndex("time");
        int powerColumn = cursor.getColumnIndex("power");
        UidSipper sipper = new UidSipper(TOTAL_POWER);
        double totalPower = 0.0d;
        if (cursor.moveToFirst()) {
            if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast()) {
                long time = cursor.getLong(timeColumn);
                boolean lcdOn = cursor.getInt(lcdColumn) == 4;
                double power = cursor.getDouble(powerColumn);
                sipper.addBatterySipStat(time, lcdOn, power, 0.0d);
                totalPower += power;
                cursor.moveToNext();
            }
        }
        cursor.close();
        sipper.setTotalPower(totalPower);
        result.addUidSipper(sipper);
        result.setFinalPower(totalPower);
        printDebugLog(TAG, "Final set to all = " + totalPower);
        Parcel out = Parcel.obtain();
        result.writeToParcel(out);
        data = out.marshall();
        out.recycle();
        return data;
    }

    private synchronized byte[] getBatteryPercentageList() {
        byte[] data;
        SQLiteDatabase db = getReadableDatabase();
        UidSipperImpl result = new UidSipperImpl();
        UidSipper sipper = new UidSipper("Battery");
        long curTime = System.currentTimeMillis();
        Cursor cursor = db.rawQuery(PERC_QUERY_STRING, null);
        int lcdColumn = cursor.getColumnIndex("lcd_condition");
        int timeColumn = cursor.getColumnIndex("time");
        int batteryPercColumn = cursor.getColumnIndex("batterypercent");
        if (cursor.moveToFirst()) {
            if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast()) {
                sipper.addBatterySipStat(cursor.getLong(timeColumn), cursor.getInt(lcdColumn) == 4, (double) cursor.getInt(batteryPercColumn), 0.0d);
                cursor.moveToNext();
            }
        }
        cursor.close();
        result.addUidSipper(sipper);
        Parcel out = Parcel.obtain();
        result.writeToParcel(out);
        data = out.marshall();
        out.recycle();
        return data;
    }

    private synchronized byte[] getConsumerListOnlyApp() {
        byte[] data;
        Cursor cursor;
        int timeColumn;
        SQLiteDatabase db = getReadableDatabase();
        UidSipperImpl result = new UidSipperImpl();
        long curTime = System.currentTimeMillis();
        Cursor c = db.rawQuery(PACKAGE_QUERY_STRING, null);
        double allUidTotalPower = 0.0d;
        if (c.moveToFirst()) {
            Set<String> launchedAppList = getAppUsageStats();
            while (!c.isAfterLast()) {
                String val = c.getString(c.getColumnIndex(KeyChain.EXTRA_NAME));
                cursor = db.rawQuery("SELECT lcd_condition,time,power FROM[" + val + NAME_END, null);
                cursor.moveToFirst();
                int lcdColumn = cursor.getColumnIndex("lcd_condition");
                timeColumn = cursor.getColumnIndex("time");
                int powerColumn = cursor.getColumnIndex("power");
                if ((cursor.getInt(lcdColumn) & 16) == 16) {
                    printDebugLog(TAG, "System app");
                    cursor.close();
                    c.moveToNext();
                } else {
                    if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                        cursor.moveToNext();
                    }
                    UidSipper sipper = new UidSipper(val);
                    double totalPower = 0.0d;
                    boolean isUsingNetwork = false;
                    while (!cursor.isAfterLast()) {
                        int lcdNtype = cursor.getInt(lcdColumn);
                        long time = cursor.getLong(timeColumn);
                        boolean lcdOn = (lcdNtype & 4) == 4;
                        double power = cursor.getDouble(powerColumn);
                        sipper.addBatterySipStat(time, lcdOn, power, 0.0d);
                        totalPower += power;
                        isUsingNetwork |= (lcdNtype & 32) == 32 ? 1 : 0;
                        cursor.moveToNext();
                    }
                    printDebugLog(TAG, val + " power:" + totalPower);
                    if (!sipper.batterySipStats.isEmpty()) {
                        sipper.setTotalPower(totalPower);
                        allUidTotalPower += totalPower;
                        if (launchedAppList.contains(val)) {
                            sipper.makeUserLaunch();
                        } else if (isUsingNetwork) {
                            sipper.makeNetworkUser();
                        }
                        result.addUidSipper(sipper);
                        printDebugLog(TAG, "added");
                    }
                    cursor.close();
                    c.moveToNext();
                }
            }
            if (allUidTotalPower <= 0.0d) {
                allUidTotalPower = 1.0d;
            }
            result.setFinalPower(allUidTotalPower);
        }
        c.close();
        cursor = db.rawQuery("SELECT  * FROM power_consuming_packages", null);
        timeColumn = cursor.getColumnIndex("time");
        int abuserNameColumn = cursor.getColumnIndex("packageName");
        result.initializeConsumerList();
        if (cursor.moveToFirst()) {
            if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast()) {
                result.addConsumerPackages(String.valueOf(cursor.getLong(timeColumn)), cursor.getString(abuserNameColumn));
                cursor.moveToNext();
            }
        }
        cursor.close();
        Parcel out = Parcel.obtain();
        result.writeToParcel(out);
        data = out.marshall();
        out.recycle();
        return data;
    }

    public synchronized String[] getAbusiveAppList() {
        String[] strArr;
        Cursor cursor = getReadableDatabase().query(POWER_CONSUMING_PACKAGES, new String[]{"packageName", KEY_PERCENTAGE}, null, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()) {
            cursor.close();
            strArr = new String[0];
        } else {
            int packageColumn = cursor.getColumnIndex("packageName");
            int percentageColumn = cursor.getColumnIndex(KEY_PERCENTAGE);
            strArr = new String[cursor.getCount()];
            int index = 0;
            while (!cursor.isAfterLast()) {
                int index2 = index + 1;
                strArr[index] = cursor.getString(packageColumn) + "#" + cursor.getDouble(percentageColumn);
                cursor.moveToNext();
                index = index2;
            }
            cursor.close();
        }
        return strArr;
    }

    public synchronized void handleTimeChange() {
        SQLiteDatabase db;
        long delta = calculateTimeChange();
        if (delta != 0) {
            if (delta >= DateUtils.DAY_IN_MILLIS) {
                Log.w(TAG, "Time delta is too big " + delta + " init db");
                db = getWritableDatabase();
                db.beginTransaction();
                try {
                    onUpgrade(db, 7, 7);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            } else {
                String deltaStr = delta > 0 ? "+" + delta : String.valueOf(delta);
                printDebugLog(TAG, "Time diff = " + deltaStr);
                db = getWritableDatabase();
                db.beginTransaction();
                try {
                    Cursor listCursor = db.rawQuery(DELETE_QUERY_STRING, null);
                    if (listCursor.moveToFirst()) {
                        while (!listCursor.isAfterLast()) {
                            db.execSQL("UPDATE [" + listCursor.getString(listCursor.getColumnIndex(KeyChain.EXTRA_NAME)) + NAME_END + " SET time=time" + deltaStr);
                            listCursor.moveToNext();
                        }
                    }
                    listCursor.close();
                    db.execSQL("UPDATE Battery_Delta SET time=time" + deltaStr);
                    db.execSQL("UPDATE power_consuming_packages SET time=time" + deltaStr + ", notified_time=notified_time" + deltaStr);
                    db.setTransactionSuccessful();
                } catch (Exception e2) {
                    e2.printStackTrace();
                } finally {
                    db.endTransaction();
                }
                printDebugLog(TAG, "Updated db with new timestamps");
            }
        }
        return;
        db.endTransaction();
    }

    private long calculateTimeChange() {
        long actualSystemTime = System.currentTimeMillis();
        long actualRealtime = SystemClock.elapsedRealtime();
        long timeDiff = actualSystemTime - ((actualRealtime - sRealTimeSnapshot) + sSystemTimeSnapshot);
        long time = Math.abs(timeDiff);
        sRealTimeSnapshot = actualRealtime;
        sSystemTimeSnapshot = actualSystemTime;
        return time > TIME_DIFF_THRESHOLD ? timeDiff : 0;
    }

    public static boolean deleteDatabase() {
        boolean ret = SQLiteDatabase.deleteDatabase(mContext.getDatabasePath(DATABASE_NAME));
        if (ret) {
            printDebugLog(TAG, "Database has been deleted");
        } else {
            printDebugLog(TAG, "Database was not deleted");
        }
        return ret;
    }

    public static String makemAh(double power) {
        if (power == 0.0d) {
            return SmartFaceManager.PAGE_MIDDLE;
        }
        String format;
        if (power < 1.0E-5d) {
            format = "%.8f";
        } else if (power < 1.0E-4d) {
            format = "%.7f";
        } else if (power < 0.001d) {
            format = "%.6f";
        } else if (power < 0.01d) {
            format = "%.5f";
        } else if (power < 0.1d) {
            format = "%.4f";
        } else if (power < 1.0d) {
            format = "%.3f";
        } else if (power < 10.0d) {
            format = "%.2f";
        } else if (power < 100.0d) {
            format = "%.1f";
        } else {
            format = "%.0f";
        }
        return String.format(Locale.ENGLISH, format, new Object[]{Double.valueOf(power)});
    }

    public synchronized void dumpUsageList(long duration, boolean needOffList, PrintWriter pw) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(PACKAGE_QUERY_STRING, null);
        double allUidTotalPower = 0.0d;
        if (c.moveToFirst()) {
            long curTime = System.currentTimeMillis();
            Set<String> launchedAppList = getAppUsageStats();
            while (!c.isAfterLast()) {
                String val = c.getString(c.getColumnIndex(KeyChain.EXTRA_NAME));
                Cursor cursor = db.rawQuery("SELECT lcd_condition,time,power FROM[" + val + NAME_END, null);
                cursor.moveToFirst();
                int lcdColumn = cursor.getColumnIndex("lcd_condition");
                int timeColumn = cursor.getColumnIndex("time");
                int powerColumn = cursor.getColumnIndex("power");
                double totalPower = 0.0d;
                if (curTime - cursor.getLong(timeColumn) >= DateUtils.DAY_IN_MILLIS) {
                    cursor.moveToNext();
                }
                while (!cursor.isAfterLast()) {
                    int lcdNtype = cursor.getInt(lcdColumn);
                    if (curTime - cursor.getLong(timeColumn) < duration) {
                        if (!needOffList) {
                            totalPower += cursor.getDouble(powerColumn);
                        } else if ((lcdNtype & 8) == 8) {
                            totalPower += cursor.getDouble(powerColumn);
                        }
                    }
                    cursor.moveToNext();
                }
                if (totalPower > 0.01d) {
                    pw.println(val + " : " + makemAh(totalPower));
                }
                allUidTotalPower += totalPower;
                cursor.close();
                c.moveToNext();
            }
            if (allUidTotalPower <= 0.0d) {
                allUidTotalPower = 1.0d;
            }
            pw.println("Total power: " + allUidTotalPower);
        }
        c.close();
    }

    public void dumpUsageList(PrintWriter pw) {
        pw.println("3 Hours batterystats in screen off:");
        dumpUsageList(10800000, true, pw);
        pw.println();
        pw.println("24 Hours batterystats in screen off:");
        dumpUsageList(DateUtils.DAY_IN_MILLIS, true, pw);
        pw.println();
    }

    public synchronized void dumpConsumingAppDetails(PrintWriter pw) {
        SQLiteDatabase db = getReadableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Cursor cursor = db.query(POWER_CONSUMING_PACKAGES, null, null, null, null, null, null);
        String[] abuserList = new String[0];
        if (cursor.moveToFirst()) {
            int timeColumn = cursor.getColumnIndex("time");
            int packageColumn = cursor.getColumnIndex("packageName");
            int percentageColumn = cursor.getColumnIndex(KEY_PERCENTAGE);
            int notifiedTimeColumn = cursor.getColumnIndex(KEY_NOTIFIED_TIME);
            abuserList = new String[cursor.getCount()];
            StringBuilder sb = new StringBuilder();
            pw.println("Abusive app list:");
            int index = 0;
            while (!cursor.isAfterLast()) {
                sb.setLength(0);
                sb.append("Package name: ");
                String pkgName = cursor.getString(packageColumn);
                sb.append(pkgName);
                sb.append(" consumed ");
                sb.append(cursor.getDouble(percentageColumn));
                sb.append(" was found abusive at: ");
                sb.append(simpleDateFormat.format(Long.valueOf(cursor.getLong(timeColumn))));
                sb.append(" and last notified at: ");
                sb.append(simpleDateFormat.format(Long.valueOf(cursor.getLong(notifiedTimeColumn))));
                pw.println(sb.toString());
                cursor.moveToNext();
                int index2 = index + 1;
                abuserList[index] = pkgName;
                index = index2;
            }
        }
        cursor.close();
        pw.println();
        TotalTableData totalData = collectTotalTableData(db, System.currentTimeMillis());
        pw.println("oneHourPower: " + totalData.totalPowerForOneHour + " mAh, oneDayPower: " + totalData.totalPowerForOneDay + " mAh, oneHourBatteryDelta: " + totalData.batteryDeltaForOneHour + "%, oneDayBatteryDelta: " + totalData.batteryDeltaForOneDay + "%");
        pw.println();
        pw.println("One Hour Threshold: " + this.ONE_HOUR_THRESHOLD);
        pw.println("One Hour Threshold for Healing Camp: " + this.ONE_HOUR_THRESHOLD_HEALING_CAMP);
        pw.println();
        dumpConsumingAppDetails(pw, abuserList);
    }

    public synchronized void dumpConsumingAppDetails(PrintWriter pw, String[] abuserList) {
        SQLiteDatabase db = getReadableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        for (String name : abuserList) {
            Cursor cursor = db.query(NAME_START + name + NAME_END, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int lcdColumn = cursor.getColumnIndex("lcd_condition");
                int timeColumn = cursor.getColumnIndex("time");
                int powerColumn = cursor.getColumnIndex("power");
                int totalPowerColumn = cursor.getColumnIndex("totalpower");
                int networkColumn = cursor.getColumnIndex("network_usage");
                int usageColumn = cursor.getColumnIndex("usage_time");
                int wakeUpColumn = cursor.getColumnIndex(KEY_WAKEUP);
                pw.println(name + " table: ");
                pw.println("lcd |        time        |    power    |   totalpower |    network    |    usage_time |    wakeUpCount");
                while (!cursor.isAfterLast()) {
                    int lcdCondition = cursor.getInt(lcdColumn);
                    double power = cursor.getDouble(powerColumn);
                    String strLcdCond = (lcdCondition & 8) == 8 ? "OFF" : "ON";
                    String resultTime = simpleDateFormat.format(Long.valueOf(cursor.getLong(timeColumn)));
                    double totalPower = cursor.getDouble(totalPowerColumn);
                    long networkUsage = cursor.getLong(networkColumn);
                    long usageTime = cursor.getLong(usageColumn);
                    int wakeUpCount = cursor.getInt(wakeUpColumn);
                    pw.println(String.format("%s | %s | %11.5f | %11.5f | %13d | %13d | %10d", new Object[]{strLcdCond, resultTime, Double.valueOf(power), Double.valueOf(totalPower), Long.valueOf(networkUsage), Long.valueOf(usageTime), Integer.valueOf(wakeUpCount)}));
                    cursor.moveToNext();
                }
            }
            cursor.close();
            pw.println();
        }
    }

    private void deleteRecordsExceptLatest(SQLiteDatabase db, String tableName_, int maxNumOfItems) {
        String tableName = NAME_START + tableName_ + NAME_END;
        long numOfRemoval = DatabaseUtils.queryNumEntries(db, tableName) - ((long) maxNumOfItems);
        if (numOfRemoval > 0) {
            db.execSQL("DELETE FROM " + tableName + " WHERE time IN (SELECT time FROM " + tableName + " LIMIT " + numOfRemoval + SIGNATURE_END);
        }
    }
}
