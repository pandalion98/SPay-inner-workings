/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.SQLException
 *  com.google.gson.Gson
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 *  java.util.List
 */
package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.google.gson.Gson;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.facade.exception.TokenKeyInvalidException;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.Mst;
import com.samsung.android.visasdk.paywave.model.PaymentData;
import com.samsung.android.visasdk.paywave.model.PaymentInstrument;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.samsung.android.visasdk.storage.a;
import com.samsung.android.visasdk.storage.model.DbSugarData;
import java.lang.reflect.Type;
import java.util.List;

public class b {
    private final a Gu;

    public b(Context context) {
        if (context == null) {
            com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "ctx is null");
        }
        this.Gu = a.as(context);
        if (this.Gu == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    public TokenKey a(ProvisionResponse provisionResponse, String string, byte[] arrby, byte[] arrby2) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("vPanEnrollmentID", string);
            contentValues.put("vProvisionedTokenID", provisionResponse.getVProvisionedTokenID());
            contentValues.put("token_requester_id", provisionResponse.getTokenInfo().getTokenRequestorID());
            contentValues.put("encryption_metadata", provisionResponse.getEncryptionMetaData());
            contentValues.put("tokenStatus", provisionResponse.getTokenInfo().getTokenStatus());
            contentValues.put("payment_instrument_last4", provisionResponse.getPaymentInstrument().getLast4());
            if (provisionResponse.getPaymentInstrument().getExpirationDate() != null && provisionResponse.getPaymentInstrument().getExpirationDate().getMonth() != null && provisionResponse.getPaymentInstrument().getExpirationDate().getYear() != null) {
                contentValues.put("payment_instrument_expiration_month", provisionResponse.getPaymentInstrument().getExpirationDate().getMonth());
                contentValues.put("payment_instrument_expiration_year", provisionResponse.getPaymentInstrument().getExpirationDate().getYear());
            }
            contentValues.put("token_expirationDate_month", provisionResponse.getTokenInfo().getExpirationDate().getMonth());
            contentValues.put("token_expirationDate_year", provisionResponse.getTokenInfo().getExpirationDate().getYear());
            contentValues.put("appPrgrmID", provisionResponse.getTokenInfo().getAppPrgrmID());
            contentValues.put("static_params", new Gson().toJson((Object)provisionResponse.getTokenInfo().getHceData().getStaticParams(), StaticParams.class));
            contentValues.put("dynamic_key", arrby);
            contentValues.put("mac_left_key", arrby);
            contentValues.put("mac_right_key", arrby);
            contentValues.put("enc_token_info", arrby2);
            contentValues.put("token_last4", provisionResponse.getTokenInfo().getLast4());
            if (provisionResponse.getTokenInfo().getMst() != null) {
                contentValues.put("mst_cvv", provisionResponse.getTokenInfo().getMst().getCvv());
                contentValues.put("mst_svc_code", provisionResponse.getTokenInfo().getMst().getSvcCode());
            }
            byte[] arrby3 = new DbSugarData(provisionResponse.getTokenInfo()).toJson().getBytes();
            contentValues.put("sugar", com.samsung.android.visasdk.b.b.fV().storeData(arrby3));
            TokenKey tokenKey = new TokenKey(this.Gu.b("tbl_enhanced_token_info", contentValues));
            return tokenKey;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new SQLException("cannot store token");
        }
    }

    public void a(TokenKey tokenKey, int n2, int n3) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        DbSugarData dbSugarData = this.d(tokenKey);
        if (dbSugarData == null) {
            throw new TokenInvalidException("cannot get sugar data");
        }
        int n4 = dbSugarData.getAtc();
        if (n4 >= n2) {
            com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "old atc > new atc");
            return;
        }
        dbSugarData.setAtc(n2);
        dbSugarData.setLukUseCount(n2 + dbSugarData.getLukUseCount() - n4);
        dbSugarData.setMaxPmts(n3);
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("sugar", com.samsung.android.visasdk.b.b.fV().storeData(dbSugarData.toJson().getBytes()));
            this.Gu.a("tbl_enhanced_token_info", contentValues, "_id", Long.toString((long)tokenKey.getTokenId()));
            com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "sugar data is updated");
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new TokenInvalidException("cannot update sugar data");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean a(TokenKey tokenKey, TokenInfo tokenInfo) {
        boolean bl = true;
        if (tokenKey == null || tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null) {
            com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "cannot replenish token");
            return false;
        }
        DynParams dynParams = tokenInfo.getHceData().getDynParams();
        DbSugarData dbSugarData = this.d(tokenKey);
        if (dynParams == null) return false;
        if (dbSugarData == null) {
            return false;
        }
        if (dynParams.getApi() != null) {
            if (dynParams.getApi().equals((Object)dbSugarData.getApi())) return bl;
        }
        dbSugarData.updateForReplenish(dynParams);
        ContentValues contentValues = new ContentValues();
        contentValues.put("dynamic_key", com.samsung.android.visasdk.a.b.hexStringToBytes(dynParams.getEncKeyInfo()));
        try {
            contentValues.put("sugar", com.samsung.android.visasdk.b.b.fV().storeData(dbSugarData.toJson().getBytes()));
            int n2 = this.Gu.a("tbl_enhanced_token_info", contentValues, "_id", Long.toString((long)tokenKey.getTokenId()));
            if (n2 > 0) return bl;
            return false;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "cannot update token");
            return false;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public PaymentData b(TokenKey var1_1, boolean var2_2) {
        block12 : {
            var3_3 = null;
            if (var1_1 == null) {
                throw new SQLException("token key is null");
            }
            var5_5 = var7_4 = this.Gu.f("tbl_enhanced_token_info", "_id", Long.toString((long)var1_1.getTokenId()));
            if (var5_5 == null) throw new TokenKeyInvalidException("token key is invalid");
            try {
                if (var5_5.getCount() <= 0) {
                    throw new TokenKeyInvalidException("token key is invalid");
                }
                if (var5_5.getCount() > 1) {
                    throw new TokenInvalidException("more than one token is found");
                }
                break block12;
            }
            catch (Throwable var4_6) {}
            ** GOTO lbl-1000
        }
        var8_9 = new TokenInfo();
        while (var5_5.moveToNext()) {
            var3_3 = new PaymentData();
            var8_9.setAppPrgrmID(var5_5.getString(var5_5.getColumnIndex("appPrgrmID")));
            var8_9.setEncTokenInfo(com.samsung.android.visasdk.a.b.o(var5_5.getBlob(var5_5.getColumnIndex("enc_token_info"))));
            var8_9.setLast4(var5_5.getString(var5_5.getColumnIndex("token_last4")));
            var8_9.setTokenReferenceID(var5_5.getString(var5_5.getColumnIndex("vProvisionedTokenID")));
            var8_9.setTokenStatus(var5_5.getString(var5_5.getColumnIndex("tokenStatus")));
            var8_9.setTokenRequestorID(var5_5.getString(var5_5.getColumnIndex("token_requester_id")));
            com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "left key=" + com.samsung.android.visasdk.a.b.o(var5_5.getBlob(var5_5.getColumnIndex("mac_left_key"))));
            com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "right key=" + com.samsung.android.visasdk.a.b.o(var5_5.getBlob(var5_5.getColumnIndex("mac_right_key"))));
            var9_10 = new ExpirationDate();
            var9_10.setMonth(var5_5.getString(var5_5.getColumnIndex("token_expirationDate_month")));
            var9_10.setYear(var5_5.getString(var5_5.getColumnIndex("token_expirationDate_year")));
            var8_9.setExpirationDate(var9_10);
            var10_11 = new Mst();
            var10_11.setCvv(var5_5.getString(var5_5.getColumnIndex("mst_cvv")));
            var10_11.setSvcCode(var5_5.getString(var5_5.getColumnIndex("mst_svc_code")));
            var8_9.setMst(var10_11);
            var11_12 = StaticParams.fromJson(var5_5.getString(var5_5.getColumnIndex("static_params")));
            var12_13 = new DynParams();
            if (var2_2) {
                var13_14 = var5_5.getBlob(var5_5.getColumnIndex("sugar"));
                var15_15 = com.samsung.android.visasdk.b.b.fV().retrieveFromStorage(var13_14);
                var16_16 = DbSugarData.fromJson(new String(var15_15));
                var12_13.setApi(var16_16.getApi());
                var12_13.setDki(var16_16.getDki());
                var12_13.setSc(var16_16.getSequenceCounter());
                var12_13.setKeyExpTS(var16_16.getKeyExpTS());
                var12_13.setMaxPmts(var16_16.getMaxPmts());
                var12_13.setEncKeyInfo(com.samsung.android.visasdk.a.b.o(var5_5.getBlob(var5_5.getColumnIndex("dynamic_key"))));
                var3_3.setAtc(var16_16.getAtc());
                var3_3.setLukUseCount(var16_16.getLukUseCount());
            }
            var17_17 = new HceData();
            var17_17.setStaticParams(var11_12);
            var17_17.setDynParams(var12_13);
            var8_9.setHceData(var17_17);
            var3_3.setTokenInfo(var8_9);
            var3_3.setEnrollmentId(var5_5.getString(var5_5.getColumnIndex("vPanEnrollmentID")));
            continue;
            catch (Exception var14_18) {
                var14_18.printStackTrace();
                throw new TokenInvalidException("unable to decrypt sugar data");
            }
        }
        a.a(var5_5);
        return var3_3;
        catch (Throwable var4_8) {
            var5_5 = null;
        }
lbl-1000: // 2 sources:
        {
            a.a(var5_5);
            throw var4_7;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public DbSugarData d(TokenKey tokenKey) {
        byte[] arrby;
        block10 : {
            byte[] arrby2;
            block9 : {
                if (tokenKey == null) {
                    throw new SQLException("token key is null");
                }
                com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "getReplenishTokenInfo: TokenId " + tokenKey.getTokenId());
                List<byte[]> list = this.Gu.b("tbl_enhanced_token_info", "sugar", "_id", Long.toString((long)tokenKey.getTokenId()));
                if (list == null) {
                    throw new SQLException("dataList null, cannot find token");
                }
                if (list.size() != 1) {
                    if (list.size() <= 1) throw new SQLException("cannot find token");
                    (byte[])list.get(0);
                    throw new SQLException("more than one token is found");
                }
                byte[] arrby3 = (byte[])list.get(0);
                if (arrby3 != null) {
                    byte[] arrby4;
                    arrby2 = arrby4 = com.samsung.android.visasdk.b.b.fV().retrieveFromStorage(arrby3);
                    if (arrby2 == null) break block9;
                    if (arrby2.length > 0) {
                        com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "sugarData: " + new String(arrby2));
                        DbSugarData dbSugarData = DbSugarData.fromJson(new String(arrby2));
                        return dbSugarData;
                    }
                    break block9;
                }
                arrby2 = null;
            }
            arrby = arrby2;
            break block10;
            catch (Exception exception) {
                Exception exception2;
                block11 : {
                    arrby = null;
                    break block11;
                    catch (Exception exception3) {
                        arrby = arrby2;
                        exception2 = exception3;
                    }
                }
                exception2.printStackTrace();
            }
        }
        DbSugarData dbSugarData = null;
        if (arrby != null) return dbSugarData;
        com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "sugarData is null");
        return null;
    }

    public void deleteToken(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        try {
            this.Gu.e("tbl_enhanced_token_info", "_id", Long.toString((long)tokenKey.getTokenId()));
            com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "token " + tokenKey.getTokenId() + " is deleted");
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new TokenInvalidException("cannot delete token");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String e(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new TokenInvalidException("token key is null");
        }
        try {
            List<String> list = this.Gu.a("tbl_enhanced_token_info", "encryption_metadata", "_id", Long.toString((long)tokenKey.getTokenId()));
            if (list != null && list.size() == 1) {
                return (String)list.get(0);
            }
            com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "cannot get meta data");
            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] f(TokenKey tokenKey) {
        try {
            List<byte[]> list = this.Gu.b("tbl_enhanced_token_info", "dynamic_key", "_id", Long.toString((long)tokenKey.getTokenId()));
            if (list == null) throw new TokenKeyInvalidException("token key is invalid");
            if (list.size() <= 0) {
                throw new TokenKeyInvalidException("token key is invalid");
            }
            if (list.size() <= 1) return (byte[])list.get(0);
            throw new TokenInvalidException("more than one token is found");
        }
        catch (Exception exception) {
            exception.printStackTrace();
            com.samsung.android.visasdk.c.a.e("DbEnhancedTokenInfo", "cannot get key");
            return null;
        }
    }

    public String getTokenStatus(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new SQLException("token key is null");
        }
        List<String> list = this.Gu.a("tbl_enhanced_token_info", "tokenStatus", "_id", Long.toString((long)tokenKey.getTokenId()));
        if (list == null) {
            return TokenStatus.NOT_FOUND.getStatus();
        }
        if (list.size() == 1) {
            return (String)list.get(0);
        }
        if (list.size() > 1) {
            throw new SQLException("more than one token is found");
        }
        throw new SQLException("cannot find token");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isMstSupported(TokenKey tokenKey) {
        if (tokenKey == null) {
            throw new SQLException("token key is null");
        }
        Cursor cursor = null;
        try {
            cursor = this.Gu.f("tbl_enhanced_token_info", "_id", Long.toString((long)tokenKey.getTokenId()));
            if (cursor == null || cursor.getCount() <= 0) {
                throw new TokenKeyInvalidException("token key is invalid");
            }
            if (cursor.getCount() > 1) {
                throw new TokenInvalidException("more than one token is found");
            }
        }
        catch (Throwable throwable) {
            a.a(cursor);
            throw throwable;
        }
        while (cursor.moveToNext()) {
            String string = cursor.getString(cursor.getColumnIndex("mst_cvv"));
            String string2 = cursor.getString(cursor.getColumnIndex("mst_svc_code"));
            if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) continue;
            com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "mst supported");
            a.a(cursor);
            return true;
        }
        a.a(cursor);
        com.samsung.android.visasdk.c.a.d("DbEnhancedTokenInfo", "mst not supported");
        return false;
    }

    public boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tokenStatus", tokenStatus.getStatus());
        try {
            int n2 = this.Gu.a("tbl_enhanced_token_info", contentValues, "_id", Long.toString((long)tokenKey.getTokenId()));
            boolean bl = false;
            if (n2 > 0) {
                bl = true;
            }
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}

