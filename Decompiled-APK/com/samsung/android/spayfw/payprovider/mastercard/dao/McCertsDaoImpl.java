package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCerts;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.Certificates;

public class McCertsDaoImpl extends MCAbstractDaoImpl<McCerts> {
    private static final String TAG;

    static {
        TAG = McCertsDaoImpl.class.getSimpleName();
    }

    public McCertsDaoImpl(Context context) {
        super(context);
    }

    protected ContentValues getContentValues(McCerts mcCerts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Certificates.COL_PUBLIC_CERT_PEM, mcCerts.getPublicCertPem());
        contentValues.put(Certificates.COL_PUBLIC_CERT_ALIAS, mcCerts.getPublicCertAlias());
        contentValues.put(Certificates.COL_CARD_INFO_CERT_PEM, mcCerts.getCardInfoCertPem());
        contentValues.put(Certificates.COL_CARD_INFO_CERT_ALIAS, mcCerts.getCardInfoAlias());
        return contentValues;
    }

    protected McCerts getDataValues(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        McCerts mcCerts = new McCerts();
        mcCerts.setPublicCertPem(cursor.getBlob(cursor.getColumnIndex(Certificates.COL_PUBLIC_CERT_PEM)));
        mcCerts.setPublicCertAlias(cursor.getBlob(cursor.getColumnIndex(Certificates.COL_PUBLIC_CERT_ALIAS)));
        mcCerts.setCardInfoCertPem(cursor.getBlob(cursor.getColumnIndex(Certificates.COL_CARD_INFO_CERT_PEM)));
        mcCerts.setCardInfoAlias(cursor.getBlob(cursor.getColumnIndex(Certificates.COL_CARD_INFO_CERT_ALIAS)));
        return mcCerts;
    }

    protected String getTableName() {
        return Certificates.TABLE_NAME;
    }

    protected ContentValues getContentValues(McCerts mcCerts, long j) {
        return null;
    }

    public boolean createOrUpdate(McCerts mcCerts) {
        boolean z = true;
        if (mcCerts == null) {
            return false;
        }
        ContentValues contentValues = getContentValues(mcCerts);
        if (this.db == null || contentValues == null) {
            return false;
        }
        int update = this.db.update(getTableName(), contentValues, null, null);
        if (((long) update) == -1) {
            return false;
        }
        if (update < 1 && this.db.insert(getTableName(), null, contentValues) <= 0) {
            z = false;
        }
        return z;
    }

    public McCerts getCerts() {
        Cursor query;
        NullPointerException e;
        Throwable th;
        try {
            McCerts dataValues;
            query = this.db.query(getTableName(), null, null, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        dataValues = getDataValues(query);
                        if (query != null) {
                            return dataValues;
                        }
                        query.close();
                        return dataValues;
                    }
                } catch (NullPointerException e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        Log.m286e(TAG, "NPE exception occured during getCerts: " + e.getMessage());
                        if (query != null) {
                            return null;
                        }
                        query.close();
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        if (query != null) {
                            query.close();
                        }
                        throw th;
                    }
                }
            }
            dataValues = null;
            if (query != null) {
                return dataValues;
            }
            query.close();
            return dataValues;
        } catch (NullPointerException e3) {
            e = e3;
            query = null;
            e.printStackTrace();
            Log.m286e(TAG, "NPE exception occured during getCerts: " + e.getMessage());
            if (query != null) {
                return null;
            }
            query.close();
            return null;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public boolean deleteCerts() {
        if (this.db != null && this.db.delete(getTableName(), null, null) > 0) {
            return true;
        }
        return false;
    }

    public McCerts getData(long j) {
        return null;
    }

    public long saveData(McCerts mcCerts) {
        return -1;
    }

    public long saveData(McCerts mcCerts, long j) {
        return -1;
    }

    public boolean deleteData(long j) {
        return false;
    }

    public boolean updateData(McCerts mcCerts, long j) {
        return false;
    }
}
