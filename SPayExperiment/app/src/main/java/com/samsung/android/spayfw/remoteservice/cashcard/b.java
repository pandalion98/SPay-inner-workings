/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.reflect.TypeToken
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.reflect.Type
 */
package com.samsung.android.spayfw.remoteservice.cashcard;

import com.google.gson.reflect.TypeToken;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.cashcard.a;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection;
import java.lang.reflect.Type;

public class b
extends Request<String, Collection<CashCardInfo>, c<Collection<CashCardInfo>>, b> {
    private String AL;
    private boolean AM;
    private String bN;

    protected b(a a2, String string) {
        super(a2, Client.HttpRequest.RequestMethod.Ag, "");
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Card Id is null or empty in QueryCashCardRequest");
        }
        this.bN = string;
    }

    @Override
    protected c<Collection<CashCardInfo>> b(int n2, String string) {
        return new c<Collection<CashCardInfo>>(null, (Collection)this.Al.fromJson(string, new TypeToken<Collection<CashCardInfo>>(){}.getType()), n2);
    }

    @Override
    protected String cG() {
        StringBuilder stringBuilder = new StringBuilder("/cards").append("?user.id=").append(this.bN);
        if (this.AM) {
            stringBuilder.append("&isAutoImport=true");
        }
        return stringBuilder.toString();
    }

    public void g(boolean bl) {
        this.AM = bl;
    }

    @Override
    protected String getRequestType() {
        return "ListCashCardRequest";
    }

    @Override
    protected void init() {
        if (this.AL != null && !this.AL.isEmpty()) {
            this.addHeader("OTP", this.AL);
        }
    }

}

