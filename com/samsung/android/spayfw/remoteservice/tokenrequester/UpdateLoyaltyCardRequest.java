package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Instruction;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.n */
public class UpdateLoyaltyCardRequest extends TokenRequesterRequest<List<Instruction>, Data, Response<Data>, UpdateLoyaltyCardRequest> {
    private static final StringBuilder zX;
    private String cardId;

    static {
        zX = new StringBuilder("/cards");
    }

    protected UpdateLoyaltyCardRequest(TokenRequesterClient tokenRequesterClient, String str, List<Instruction> list) {
        super(tokenRequesterClient, RequestMethod.PATCH, list);
        this.cardId = str;
    }

    protected String cG() {
        Log.m287i("UpdateLoyaltyCardRequest", "getApiPath: cardId = " + this.cardId);
        return zX + "/" + this.cardId.toString();
    }

    protected String getRequestType() {
        return "UpdateLoyaltyCardRequest";
    }

    protected Response<Data> m1216b(int i, String str) {
        return new Response(null, (Data) this.Al.fromJson(str, Data.class), i);
    }
}
