package com.samsung.contextservice.p028a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.google.gson.Gson;
import com.samsung.contextservice.exception.InitializationException;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.server.ServerConfig;
import com.samsung.contextservice.server.models.PolicyResponseData;

/* renamed from: com.samsung.contextservice.a.c */
public class PolicyDao {
    private final DbAdapter GC;

    public PolicyDao(Context context) {
        if (context == null) {
            CSlog.m1409e("PolicyDao", "ctx is null");
        }
        this.GC = DbAdapter.au(context);
        if (this.GC == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    public long m1389a(PolicyResponseData policyResponseData) {
        CSlog.m1408d("PolicyDao", "addOrUpdatePolicies()");
        if (policyResponseData == null || policyResponseData.getId() == null || policyResponseData.getVersion() == null || policyResponseData.getPolicy() == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        try {
            ServerConfig.m1457a(policyResponseData.getPolicyObject());
            contentValues.put("policyid", "ALL_POLICIES");
            contentValues.put("version", policyResponseData.getVersion());
            contentValues.put("content", policyResponseData.toJson().getBytes());
            long c = this.GC.m1388c("policies", contentValues);
            if (c < 0) {
                CSlog.m1409e("PolicyDao", "cannot add policy");
            }
            contentValues.clear();
            return c;
        } catch (Throwable e) {
            CSlog.m1406c("PolicyDao", "add policy exception", e);
            return -1;
        }
    }

    public PolicyResponseData bI(String str) {
        CSlog.m1408d("PolicyDao", "queryPolicy()");
        Cursor rawQuery = this.GC.rawQuery(" SELECT DISTINCT content FROM policies" + (" WHERE policyid == \"" + str + "\""), null);
        if (rawQuery != null) {
            PolicyResponseData policyResponseData;
            try {
                if (rawQuery.getCount() > 0) {
                    rawQuery.moveToLast();
                    Gson gson = new Gson();
                    byte[] blob = rawQuery.getBlob(rawQuery.getColumnIndex("content"));
                    if (blob != null) {
                        String str2 = new String(blob);
                        CSlog.m1408d("PolicyDao", str2);
                        policyResponseData = (PolicyResponseData) gson.fromJson(str2, PolicyResponseData.class);
                        return policyResponseData;
                    }
                }
            } catch (Exception e) {
                policyResponseData = e;
                CSlog.m1406c("PolicyDao", "queryPolicy error", policyResponseData);
            } finally {
                DbAdapter.m1387a(rawQuery);
            }
        }
        DbAdapter.m1387a(rawQuery);
        return null;
    }
}
