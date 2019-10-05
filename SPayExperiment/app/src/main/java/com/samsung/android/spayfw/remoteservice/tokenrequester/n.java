/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Instruction;
import java.util.List;

public class n
extends m<List<Instruction>, Data, c<Data>, n> {
    private static final StringBuilder zX = new StringBuilder("/cards");
    private String cardId;

    protected n(l l2, String string, List<Instruction> list) {
        super(l2, Client.HttpRequest.RequestMethod.Aj, list);
        this.cardId = string;
    }

    @Override
    protected c<Data> b(int n2, String string) {
        return new c<Data>(null, this.Al.fromJson(string, Data.class), n2);
    }

    @Override
    protected String cG() {
        Log.i("UpdateLoyaltyCardRequest", "getApiPath: cardId = " + this.cardId);
        return (Object)zX + "/" + this.cardId.toString();
    }

    @Override
    protected String getRequestType() {
        return "UpdateLoyaltyCardRequest";
    }
}

