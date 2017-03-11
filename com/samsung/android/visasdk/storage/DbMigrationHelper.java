package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.visasdk.facade.data.TokenData;
import com.samsung.android.visasdk.p024b.CryptoManager;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.storage.model.DbSugarData;
import java.io.File;

/* renamed from: com.samsung.android.visasdk.storage.c */
public class DbMigrationHelper {
    public static void m1371a(Context context, SQLiteDatabase sQLiteDatabase) {
        Cursor query;
        Exception e;
        File databasePath;
        Throwable th;
        File databasePath2;
        Log.m1300d("DbMigrationHelper", "doSdkMigrationIfNeeded");
        File databasePath3 = context.getDatabasePath("cbp_april.db");
        Log.m1300d("DbMigrationHelper", "db file Name: " + databasePath3.getAbsolutePath());
        if (databasePath3.exists()) {
            Log.m1300d("TAG", "need migration");
            try {
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                sQLiteDatabase.execSQL("ATTACH DATABASE '" + databasePath3.getAbsolutePath() + "' as cbp_april KEY ''");
                sQLiteDatabase.execSQL("ALTER TABLE `cbp_april`.`tbl_enhanced_token_info` ADD COLUMN sugar BLOB");
                sQLiteDatabase.beginTransaction();
                sQLiteDatabase.execSQL("INSERT INTO `tbl_settings` SELECT * FROM `cbp_april`.`tbl_settings` ");
                sQLiteDatabase.execSQL("INSERT INTO `tbl_enhanced_token_info` SELECT * FROM `cbp_april`.`tbl_enhanced_token_info` ");
                sQLiteDatabase.execSQL("INSERT INTO `tbl_data_group` SELECT * FROM `cbp_april`.`tbl_data_group` ");
                sQLiteDatabase.execSQL("INSERT INTO `tbl_tvl` SELECT * FROM `cbp_april`.`tbl_tvl` ");
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                sQLiteDatabase.execSQL("DETACH DATABASE 'cbp_april'");
                sQLiteDatabase.beginTransaction();
                CryptoManager fV = CryptoManager.fV();
                query = sQLiteDatabase.query("tbl_enhanced_token_info", null, null, null, null, null, null);
                if (query != null) {
                    try {
                        if (query.getCount() > 0) {
                            while (query.moveToNext()) {
                                byte[] blob = query.getBlob(query.getColumnIndex("bouncy_submarine"));
                                Log.m1300d("DbMigrationHelper", " getting decrypted data from TA: ");
                                blob = fV.retrieveFromStorage(blob);
                                if (blob == null) {
                                    Log.m1301e("DbMigrationHelper", "token data is null: ");
                                } else {
                                    TokenData fromJson = TokenData.fromJson(new String(blob));
                                    DbSugarData dbSugarData = new DbSugarData();
                                    dbSugarData.setApi(fromJson.getApi());
                                    dbSugarData.setAtc(fromJson.getAtc());
                                    dbSugarData.setKeyExpTS(fromJson.getKeyExpTS());
                                    dbSugarData.setLukUseCount(fromJson.getLukUseCount());
                                    dbSugarData.setMaxPmts(fromJson.getMaxPmts());
                                    dbSugarData.setSequenceCounter(fromJson.getSequenceCounter());
                                    int i = query.getInt(query.getColumnIndex("_id"));
                                    Log.m1300d("DbMigrationHelper", " sugar data: tokenKey " + i);
                                    dbSugarData.setDki(query.getString(query.getColumnIndex("dynamic_dki")));
                                    Log.m1300d("DbMigrationHelper", " sugar data: " + dbSugarData.toJson());
                                    ContentValues contentValues = new ContentValues();
                                    try {
                                        blob = fV.storeData(dbSugarData.toJson().getBytes());
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                        Log.m1301e("DbMigrationHelper", "error on encrypt sugar data ");
                                        blob = null;
                                    }
                                    if (blob != null) {
                                        if (blob.length >= 0) {
                                            Log.m1300d("DbMigrationHelper", "encSugarData is success ");
                                            contentValues.put("sugar", blob);
                                            String str = "tbl_enhanced_token_info";
                                            Log.m1300d("DbMigrationHelper", " sugar data: is updated : tokenKey: " + i + " return : " + sQLiteDatabase.update(str, contentValues, "_id = ?", new String[]{Integer.toString(i)}));
                                        }
                                    }
                                    Log.m1301e("DbMigrationHelper", "encSugarData is null ");
                                }
                            }
                            Log.m1300d("TAG", "migration completed from Visa sdk to Samsu qng sdk ");
                            DbMigrationHelper.m1372a(query);
                            databasePath3.delete();
                            Log.m1300d("DbMigrationHelper", "deleted visa sdk db file ");
                            databasePath = context.getDatabasePath("cbp_april.db-journal");
                            if (databasePath.exists()) {
                                databasePath.delete();
                                Log.m1300d("DbMigrationHelper", "deleted visa sdk db journel file ");
                                return;
                            }
                            return;
                        }
                    } catch (Exception e3) {
                        e2 = e3;
                    }
                }
                Log.m1300d("DbMigrationHelper", " no value found in  bouncy submarine column in db ");
                DbMigrationHelper.m1372a(query);
                databasePath3.delete();
                Log.m1300d("DbMigrationHelper", "deleted visa sdk db file ");
                databasePath = context.getDatabasePath("cbp_april.db-journal");
                if (databasePath.exists()) {
                    databasePath.delete();
                    Log.m1300d("DbMigrationHelper", "deleted visa sdk db journel file ");
                    return;
                }
                return;
            } catch (Exception e4) {
                e2 = e4;
                query = null;
                try {
                    e2.printStackTrace();
                    DbMigrationHelper.m1372a(query);
                    databasePath3.delete();
                    Log.m1300d("DbMigrationHelper", "deleted visa sdk db file ");
                    databasePath = context.getDatabasePath("cbp_april.db-journal");
                    if (databasePath.exists()) {
                        databasePath.delete();
                        Log.m1300d("DbMigrationHelper", "deleted visa sdk db journel file ");
                        return;
                    }
                    return;
                } catch (Throwable th2) {
                    th = th2;
                    DbMigrationHelper.m1372a(query);
                    databasePath3.delete();
                    Log.m1300d("DbMigrationHelper", "deleted visa sdk db file ");
                    databasePath2 = context.getDatabasePath("cbp_april.db-journal");
                    if (databasePath2.exists()) {
                        databasePath2.delete();
                        Log.m1300d("DbMigrationHelper", "deleted visa sdk db journel file ");
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                query = null;
                DbMigrationHelper.m1372a(query);
                databasePath3.delete();
                Log.m1300d("DbMigrationHelper", "deleted visa sdk db file ");
                databasePath2 = context.getDatabasePath("cbp_april.db-journal");
                if (databasePath2.exists()) {
                    databasePath2.delete();
                    Log.m1300d("DbMigrationHelper", "deleted visa sdk db journel file ");
                }
                throw th;
            }
        }
        Log.m1300d("DbMigrationHelper", "no migration is needed");
    }

    private static final void m1372a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
