/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.samsung.contextservice.a.a;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.exception.InitializationException;
import com.samsung.contextservice.server.j;
import com.samsung.contextservice.server.models.CtxPolicy;
import com.samsung.contextservice.server.models.PolicyResponseData;

public class c {
    private final a GC;

    public c(Context context) {
        if (context == null) {
            b.e("PolicyDao", "ctx is null");
        }
        this.GC = a.au(context);
        if (this.GC == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long a(PolicyResponseData policyResponseData) {
        b.d("PolicyDao", "addOrUpdatePolicies()");
        if (policyResponseData == null || policyResponseData.getId() == null || policyResponseData.getVersion() == null || policyResponseData.getPolicy() == null) {
            return -1L;
        }
        ContentValues contentValues = new ContentValues();
        try {
            j.a(policyResponseData.getPolicyObject());
            contentValues.put("policyid", "ALL_POLICIES");
            contentValues.put("version", policyResponseData.getVersion());
            contentValues.put("content", policyResponseData.toJson().getBytes());
            long l2 = this.GC.c("policies", contentValues);
            if (l2 < 0L) {
                b.e("PolicyDao", "cannot add policy");
            }
            contentValues.clear();
            return l2;
        }
        catch (Exception exception) {
            b.c("PolicyDao", "add policy exception", exception);
            return -1L;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PolicyResponseData bI(String string) {
        b.d("PolicyDao", "queryPolicy()");
        String string2 = " WHERE policyid == \"" + string + "\"";
        String string3 = " SELECT DISTINCT content FROM policies" + string2;
        Cursor cursor = this.GC.rawQuery(string3, null);
        if (cursor == null) return null;
        try {
            if (cursor.getCount() <= 0) return null;
            cursor.moveToLast();
            Gson gson = new Gson();
            byte[] arrby = cursor.getBlob(cursor.getColumnIndex("content"));
            if (arrby == null) return null;
            String string4 = new String(arrby);
            b.d("PolicyDao", string4);
            PolicyResponseData policyResponseData = (PolicyResponseData)gson.fromJson(string4, PolicyResponseData.class);
            return policyResponseData;
        }
        catch (Exception exception) {
            b.c("PolicyDao", "queryPolicy error", exception);
            return null;
        }
        finally {
            a.a(cursor);
            return null;
        }
    }
}

