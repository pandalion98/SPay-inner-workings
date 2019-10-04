/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.remoteservice.cashcard;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.cashcard.a;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;

public class c
extends Request<String, CashCardInfo, com.samsung.android.spayfw.remoteservice.c<CashCardInfo>, c> {
    private String AL;
    private String AO;
    private boolean AP;

    protected c(a a2, String string, boolean bl) {
        super(a2, Client.HttpRequest.RequestMethod.Ag, "");
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Card Id is null or empty in QueryCashCardRequest");
        }
        this.AO = string;
        this.AP = bl;
    }

    @Override
    protected com.samsung.android.spayfw.remoteservice.c<CashCardInfo> b(int n2, String string) {
        return new com.samsung.android.spayfw.remoteservice.c<CashCardInfo>(null, this.Al.fromJson(string, CashCardInfo.class), n2);
    }

    @Override
    protected String cG() {
        StringBuilder stringBuilder = new StringBuilder("/cards/").append(this.AO).append("?expand=transactions");
        if (this.AP) {
            stringBuilder.append("&fields=data");
        }
        return stringBuilder.toString();
    }

    @Override
    protected String getRequestType() {
        return "QueryCashCardRequest";
    }

    @Override
    protected void init() {
        if (this.AL != null && !this.AL.isEmpty()) {
            this.addHeader("OTP", this.AL);
        }
        super.init();
    }
}

