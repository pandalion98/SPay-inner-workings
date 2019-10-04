/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCerts;
import com.samsung.android.spayfw.payprovider.mastercard.dao.MCAbstractDaoImpl;

public class McCertsDaoImpl
extends MCAbstractDaoImpl<McCerts> {
    private static final String TAG = McCertsDaoImpl.class.getSimpleName();

    public McCertsDaoImpl(Context context) {
        super(context);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean createOrUpdate(McCerts mcCerts) {
        int n2 = 1;
        if (mcCerts == null) {
            return false;
        }
        ContentValues contentValues = this.getContentValues(mcCerts);
        if (this.db == null) return false;
        if (contentValues == null) return false;
        int n3 = this.db.update(this.getTableName(), contentValues, null, null);
        if ((long)n3 == -1L) return false;
        if (n3 >= n2) return (boolean)n2;
        if (this.db.insert(this.getTableName(), null, contentValues) <= 0L) return (boolean)0;
        return (boolean)n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean deleteCerts() {
        return this.db != null && this.db.delete(this.getTableName(), null, null) > 0;
    }

    @Override
    public boolean deleteData(long l2) {
        return false;
    }

    /*
     * Exception decompiling
     */
    public McCerts getCerts() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[CATCHBLOCK]], but top level block is 7[UNCONDITIONALDOLOOP]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    protected ContentValues getContentValues(McCerts mcCerts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("publicCertPem", mcCerts.getPublicCertPem());
        contentValues.put("publicCertAlias", mcCerts.getPublicCertAlias());
        contentValues.put("cardInfoCertPem", mcCerts.getCardInfoCertPem());
        contentValues.put("cardInfoCertAlias", mcCerts.getCardInfoAlias());
        return contentValues;
    }

    @Override
    protected ContentValues getContentValues(McCerts mcCerts, long l2) {
        return null;
    }

    @Override
    public McCerts getData(long l2) {
        return null;
    }

    @Override
    protected McCerts getDataValues(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        McCerts mcCerts = new McCerts();
        mcCerts.setPublicCertPem(cursor.getBlob(cursor.getColumnIndex("publicCertPem")));
        mcCerts.setPublicCertAlias(cursor.getBlob(cursor.getColumnIndex("publicCertAlias")));
        mcCerts.setCardInfoCertPem(cursor.getBlob(cursor.getColumnIndex("cardInfoCertPem")));
        mcCerts.setCardInfoAlias(cursor.getBlob(cursor.getColumnIndex("cardInfoCertAlias")));
        return mcCerts;
    }

    @Override
    protected String getTableName() {
        return "CERTIFICATES";
    }

    @Override
    public long saveData(McCerts mcCerts) {
        return -1L;
    }

    @Override
    public long saveData(McCerts mcCerts, long l2) {
        return -1L;
    }

    @Override
    public boolean updateData(McCerts mcCerts, long l2) {
        return false;
    }
}

