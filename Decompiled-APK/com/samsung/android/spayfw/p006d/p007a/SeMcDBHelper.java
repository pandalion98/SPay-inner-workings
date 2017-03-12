package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardProvisionData;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.Certificates;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.TdsMetaData;

/* renamed from: com.samsung.android.spayfw.d.a.f */
public class SeMcDBHelper extends SeBaseDBHelper {
    private static SeMcDBHelper BF;
    private static SQLiteDatabase Bw;
    private static final String TAG;

    static {
        TAG = SeMcDBHelper.class.getSimpleName();
    }

    public static synchronized SeMcDBHelper m686Y(Context context) {
        SeMcDBHelper seMcDBHelper;
        synchronized (SeMcDBHelper.class) {
            if (BF == null) {
                BF = new SeMcDBHelper(context);
            }
            seMcDBHelper = BF;
        }
        return seMcDBHelper;
    }

    private SeMcDBHelper(Context context) {
        super(context, McDbContract.DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.d(TAG, "Creating Database");
        try {
            Log.d(TAG, "Creating TABLE=CREATE TABLE CARD_MASTER (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenUniqueReference TEXT,mpaInstanceId TEXT,status TEXT,suspendedBy TEXT,tokenPanSuffix TEXT,accountPanSuffix TEXT,isProvisioned INTEGER NOT NULL,rgkDerivedKeys TEXT,tokenExpiry TEXT )");
            sQLiteDatabase.execSQL(CardMaster.CREATE_TABLE);
            Log.d(TAG, "Creating TABLE=CREATE TABLE CARD_PROVISION_DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )");
            sQLiteDatabase.execSQL(CardProvisionData.CREATE_TABLE);
            Log.d(TAG, "Creating TABLE=CREATE TABLE TDS_METADATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,tdsUrl TEXT,tdsRegisterUrl TEXT,paymentAppInstanceId TEXT,hash TEXT,authCode TEXT,lastUpdateTag, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )");
            sQLiteDatabase.execSQL(TdsMetaData.CREATE_TABLE);
            Log.d(TAG, "Creating TABLE=CREATE TABLE CERTIFICATES (_id INTEGER PRIMARY KEY,publicCertPem TEXT NOT NULL,publicCertAlias TEXT NOT NULL,cardInfoCertPem TEXT NOT NULL,cardInfoCertAlias TEXT NOT NULL )");
            sQLiteDatabase.execSQL(Certificates.CREATE_TABLE);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate: SqlException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.d(TAG, "Database Upgrade not supported at this point");
    }

    public synchronized void close() {
        super.close();
        if (Bw != null && Bw.isOpen()) {
            Bw.close();
        }
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        super.onConfigure(sQLiteDatabase);
        Log.d(TAG, "onConfigure: FOREIGN KEY ENFORCEMENT = true");
        try {
            sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
        } catch (IllegalStateException e) {
            Log.e(TAG, "onConfigure: IllegalStateException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e2) {
            Log.e(TAG, "onConfigure: Exception: " + e2.getMessage());
            e2.printStackTrace();
        }
    }
}
