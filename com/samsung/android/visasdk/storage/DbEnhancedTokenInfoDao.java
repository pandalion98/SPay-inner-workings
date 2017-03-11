package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.google.gson.Gson;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardSchema;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.facade.exception.TokenKeyInvalidException;
import com.samsung.android.visasdk.p023a.Utils;
import com.samsung.android.visasdk.p024b.CryptoManager;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.Mst;
import com.samsung.android.visasdk.paywave.model.PaymentData;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.samsung.android.visasdk.storage.model.DbSugarData;
import java.lang.reflect.Type;
import java.util.List;

/* renamed from: com.samsung.android.visasdk.storage.b */
public class DbEnhancedTokenInfoDao {
    private final DbAdapter Gu;

    public DbEnhancedTokenInfoDao(Context context) {
        if (context == null) {
            Log.m1301e("DbEnhancedTokenInfo", "ctx is null");
        }
        this.Gu = DbAdapter.as(context);
        if (this.Gu == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    public boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS, tokenStatus.getStatus());
        try {
            if (this.Gu.m1357a("tbl_enhanced_token_info", contentValues, "_id", Long.toString(tokenKey.getTokenId())) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public TokenKey m1364a(ProvisionResponse provisionResponse, String str, byte[] bArr, byte[] bArr2) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("vPanEnrollmentID", str);
            contentValues.put("vProvisionedTokenID", provisionResponse.getVProvisionedTokenID());
            contentValues.put("token_requester_id", provisionResponse.getTokenInfo().getTokenRequestorID());
            contentValues.put("encryption_metadata", provisionResponse.getEncryptionMetaData());
            contentValues.put(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS, provisionResponse.getTokenInfo().getTokenStatus());
            contentValues.put("payment_instrument_last4", provisionResponse.getPaymentInstrument().getLast4());
            if (!(provisionResponse.getPaymentInstrument().getExpirationDate() == null || provisionResponse.getPaymentInstrument().getExpirationDate().getMonth() == null || provisionResponse.getPaymentInstrument().getExpirationDate().getYear() == null)) {
                contentValues.put("payment_instrument_expiration_month", provisionResponse.getPaymentInstrument().getExpirationDate().getMonth());
                contentValues.put("payment_instrument_expiration_year", provisionResponse.getPaymentInstrument().getExpirationDate().getYear());
            }
            contentValues.put("token_expirationDate_month", provisionResponse.getTokenInfo().getExpirationDate().getMonth());
            contentValues.put("token_expirationDate_year", provisionResponse.getTokenInfo().getExpirationDate().getYear());
            contentValues.put("appPrgrmID", provisionResponse.getTokenInfo().getAppPrgrmID());
            contentValues.put("static_params", new Gson().toJson(provisionResponse.getTokenInfo().getHceData().getStaticParams(), (Type) StaticParams.class));
            contentValues.put("dynamic_key", bArr);
            contentValues.put("mac_left_key", bArr);
            contentValues.put("mac_right_key", bArr);
            contentValues.put("enc_token_info", bArr2);
            contentValues.put("token_last4", provisionResponse.getTokenInfo().getLast4());
            if (provisionResponse.getTokenInfo().getMst() != null) {
                contentValues.put("mst_cvv", provisionResponse.getTokenInfo().getMst().getCvv());
                contentValues.put("mst_svc_code", provisionResponse.getTokenInfo().getMst().getSvcCode());
            }
            contentValues.put("sugar", CryptoManager.fV().storeData(new DbSugarData(provisionResponse.getTokenInfo()).toJson().getBytes()));
            return new TokenKey((long) this.Gu.m1360b("tbl_enhanced_token_info", contentValues));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("cannot store token");
        }
    }

    public boolean m1366a(TokenKey tokenKey, TokenInfo tokenInfo) {
        if (tokenKey == null || tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null) {
            Log.m1301e("DbEnhancedTokenInfo", "cannot replenish token");
            return false;
        }
        DynParams dynParams = tokenInfo.getHceData().getDynParams();
        DbSugarData d = m1368d(tokenKey);
        if (dynParams == null || d == null) {
            return false;
        }
        if (dynParams.getApi() != null && dynParams.getApi().equals(d.getApi())) {
            return true;
        }
        d.updateForReplenish(dynParams);
        ContentValues contentValues = new ContentValues();
        contentValues.put("dynamic_key", Utils.hexStringToBytes(dynParams.getEncKeyInfo()));
        try {
            contentValues.put("sugar", CryptoManager.fV().storeData(d.toJson().getBytes()));
            if (this.Gu.m1357a("tbl_enhanced_token_info", contentValues, "_id", Long.toString(tokenKey.getTokenId())) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.m1301e("DbEnhancedTokenInfo", "cannot update token");
            return false;
        }
    }

    public String getTokenStatus(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new SQLException("token key is null");
        }
        List a = this.Gu.m1359a("tbl_enhanced_token_info", PlccCardSchema.COLUMN_NAME_TOKEN_STATUS, "_id", Long.toString(tokenKey.getTokenId()));
        if (a == null) {
            return TokenStatus.NOT_FOUND.getStatus();
        }
        if (a.size() == 1) {
            return (String) a.get(0);
        }
        if (a.size() > 1) {
            throw new SQLException("more than one token is found");
        }
        throw new SQLException("cannot find token");
    }

    public DbSugarData m1368d(TokenKey tokenKey) {
        Exception exception;
        DbSugarData dbSugarData = null;
        if (tokenKey == null) {
            throw new SQLException("token key is null");
        }
        Log.m1300d("DbEnhancedTokenInfo", "getReplenishTokenInfo: TokenId " + tokenKey.getTokenId());
        List b = this.Gu.m1361b("tbl_enhanced_token_info", "sugar", "_id", Long.toString(tokenKey.getTokenId()));
        if (b == null) {
            throw new SQLException("dataList null, cannot find token");
        } else if (b.size() == 1) {
            r0 = (byte[]) b.get(0);
            byte[] bArr;
            if (r0 != null) {
                try {
                    r0 = CryptoManager.fV().retrieveFromStorage(r0);
                    if (r0 != null) {
                        try {
                            if (r0.length > 0) {
                                Log.m1300d("DbEnhancedTokenInfo", "sugarData: " + new String(r0));
                                dbSugarData = DbSugarData.fromJson(new String(r0));
                            }
                        } catch (Exception e) {
                            Exception exception2 = e;
                            bArr = r0;
                            exception = exception2;
                            exception.printStackTrace();
                            if (bArr == null) {
                                Log.m1301e("DbEnhancedTokenInfo", "sugarData is null");
                            }
                            return dbSugarData;
                        }
                    }
                    bArr = r0;
                } catch (Exception e2) {
                    exception = e2;
                    bArr = null;
                    exception.printStackTrace();
                    if (bArr == null) {
                        Log.m1301e("DbEnhancedTokenInfo", "sugarData is null");
                    }
                    return dbSugarData;
                }
                if (bArr == null) {
                    Log.m1301e("DbEnhancedTokenInfo", "sugarData is null");
                }
            } else {
                r0 = null;
                bArr = r0;
                if (bArr == null) {
                    Log.m1301e("DbEnhancedTokenInfo", "sugarData is null");
                }
            }
            return dbSugarData;
        } else if (b.size() > 1) {
            r0 = (byte[]) b.get(0);
            throw new SQLException("more than one token is found");
        } else {
            throw new SQLException("cannot find token");
        }
    }

    public PaymentData m1367b(TokenKey tokenKey, boolean z) {
        Throwable th;
        PaymentData paymentData = null;
        if (tokenKey == null) {
            throw new SQLException("token key is null");
        }
        Cursor f;
        try {
            f = this.Gu.m1363f("tbl_enhanced_token_info", "_id", Long.toString(tokenKey.getTokenId()));
            if (f != null) {
                if (f.getCount() > 0) {
                    if (f.getCount() > 1) {
                        throw new TokenInvalidException("more than one token is found");
                    }
                    TokenInfo tokenInfo = new TokenInfo();
                    while (f.moveToNext()) {
                        paymentData = new PaymentData();
                        tokenInfo.setAppPrgrmID(f.getString(f.getColumnIndex("appPrgrmID")));
                        tokenInfo.setEncTokenInfo(Utils.m1285o(f.getBlob(f.getColumnIndex("enc_token_info"))));
                        tokenInfo.setLast4(f.getString(f.getColumnIndex("token_last4")));
                        tokenInfo.setTokenReferenceID(f.getString(f.getColumnIndex("vProvisionedTokenID")));
                        tokenInfo.setTokenStatus(f.getString(f.getColumnIndex(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS)));
                        tokenInfo.setTokenRequestorID(f.getString(f.getColumnIndex("token_requester_id")));
                        Log.m1300d("DbEnhancedTokenInfo", "left key=" + Utils.m1285o(f.getBlob(f.getColumnIndex("mac_left_key"))));
                        Log.m1300d("DbEnhancedTokenInfo", "right key=" + Utils.m1285o(f.getBlob(f.getColumnIndex("mac_right_key"))));
                        ExpirationDate expirationDate = new ExpirationDate();
                        expirationDate.setMonth(f.getString(f.getColumnIndex("token_expirationDate_month")));
                        expirationDate.setYear(f.getString(f.getColumnIndex("token_expirationDate_year")));
                        tokenInfo.setExpirationDate(expirationDate);
                        Mst mst = new Mst();
                        mst.setCvv(f.getString(f.getColumnIndex("mst_cvv")));
                        mst.setSvcCode(f.getString(f.getColumnIndex("mst_svc_code")));
                        tokenInfo.setMst(mst);
                        StaticParams fromJson = StaticParams.fromJson(f.getString(f.getColumnIndex("static_params")));
                        DynParams dynParams = new DynParams();
                        if (z) {
                            try {
                                DbSugarData fromJson2 = DbSugarData.fromJson(new String(CryptoManager.fV().retrieveFromStorage(f.getBlob(f.getColumnIndex("sugar")))));
                                dynParams.setApi(fromJson2.getApi());
                                dynParams.setDki(fromJson2.getDki());
                                dynParams.setSc(fromJson2.getSequenceCounter());
                                dynParams.setKeyExpTS(Long.valueOf(fromJson2.getKeyExpTS()));
                                dynParams.setMaxPmts(Integer.valueOf(fromJson2.getMaxPmts()));
                                dynParams.setEncKeyInfo(Utils.m1285o(f.getBlob(f.getColumnIndex("dynamic_key"))));
                                paymentData.setAtc(fromJson2.getAtc());
                                paymentData.setLukUseCount(fromJson2.getLukUseCount());
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new TokenInvalidException("unable to decrypt sugar data");
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        }
                        HceData hceData = new HceData();
                        hceData.setStaticParams(fromJson);
                        hceData.setDynParams(dynParams);
                        tokenInfo.setHceData(hceData);
                        paymentData.setTokenInfo(tokenInfo);
                        paymentData.setEnrollmentId(f.getString(f.getColumnIndex("vPanEnrollmentID")));
                    }
                    DbAdapter dbAdapter = this.Gu;
                    DbAdapter.m1356a(f);
                    return paymentData;
                }
            }
            throw new TokenKeyInvalidException("token key is invalid");
        } catch (Throwable th3) {
            th = th3;
            f = null;
            DbAdapter dbAdapter2 = this.Gu;
            DbAdapter.m1356a(f);
            throw th;
        }
    }

    public void deleteToken(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        try {
            this.Gu.m1362e("tbl_enhanced_token_info", "_id", Long.toString(tokenKey.getTokenId()));
            Log.m1300d("DbEnhancedTokenInfo", "token " + tokenKey.getTokenId() + " is deleted");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TokenInvalidException("cannot delete token");
        }
    }

    public void m1365a(TokenKey tokenKey, int i, int i2) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        DbSugarData d = m1368d(tokenKey);
        if (d == null) {
            throw new TokenInvalidException("cannot get sugar data");
        }
        int atc = d.getAtc();
        if (atc >= i) {
            Log.m1301e("DbEnhancedTokenInfo", "old atc > new atc");
            return;
        }
        d.setAtc(i);
        d.setLukUseCount((d.getLukUseCount() + i) - atc);
        d.setMaxPmts(i2);
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("sugar", CryptoManager.fV().storeData(d.toJson().getBytes()));
            this.Gu.m1357a("tbl_enhanced_token_info", contentValues, "_id", Long.toString(tokenKey.getTokenId()));
            Log.m1300d("DbEnhancedTokenInfo", "sugar data is updated");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TokenInvalidException("cannot update sugar data");
        }
    }

    public String m1369e(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        try {
            List a = this.Gu.m1359a("tbl_enhanced_token_info", "encryption_metadata", "_id", Long.toString(tokenKey.getTokenId()));
            if (a != null && a.size() == 1) {
                return (String) a.get(0);
            }
            Log.m1301e("DbEnhancedTokenInfo", "cannot get meta data");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] m1370f(TokenKey tokenKey) {
        try {
            List b = this.Gu.m1361b("tbl_enhanced_token_info", "dynamic_key", "_id", Long.toString(tokenKey.getTokenId()));
            if (b == null || b.size() <= 0) {
                throw new TokenKeyInvalidException("token key is invalid");
            } else if (b.size() <= 1) {
                return (byte[]) b.get(0);
            } else {
                throw new TokenInvalidException("more than one token is found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.m1301e("DbEnhancedTokenInfo", "cannot get key");
            return null;
        }
    }

    public boolean isMstSupported(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new SQLException("token key is null");
        }
        Cursor cursor = null;
        try {
            cursor = this.Gu.m1363f("tbl_enhanced_token_info", "_id", Long.toString(tokenKey.getTokenId()));
            if (cursor == null || cursor.getCount() <= 0) {
                throw new TokenKeyInvalidException("token key is invalid");
            } else if (cursor.getCount() > 1) {
                throw new TokenInvalidException("more than one token is found");
            } else {
                while (cursor.moveToNext()) {
                    String string = cursor.getString(cursor.getColumnIndex("mst_cvv"));
                    String string2 = cursor.getString(cursor.getColumnIndex("mst_svc_code"));
                    if (string != null && !string.isEmpty() && string2 != null && !string2.isEmpty()) {
                        Log.m1300d("DbEnhancedTokenInfo", "mst supported");
                        return true;
                    }
                }
                DbAdapter dbAdapter = this.Gu;
                DbAdapter.m1356a(cursor);
                Log.m1300d("DbEnhancedTokenInfo", "mst not supported");
                return false;
            }
        } finally {
            DbAdapter dbAdapter2 = this.Gu;
            DbAdapter.m1356a(cursor);
        }
    }
}
