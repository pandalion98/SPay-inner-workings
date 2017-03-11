package com.samsung.android.spayfw.payprovider.mastercard.payload;

import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.McTokenUpdatePayload;

public class McPayloadParser {
    public static McTokenUpdatePayload parseMcTokenUpdatePayload(String str) {
        return (McTokenUpdatePayload) McPayloadBuilder.getGson().fromJson(str, McTokenUpdatePayload.class);
    }
}
