/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload;

import com.samsung.android.spayfw.payprovider.mastercard.payload.McPayloadBuilder;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.McTokenUpdatePayload;

public class McPayloadParser {
    public static McTokenUpdatePayload parseMcTokenUpdatePayload(String string) {
        return (McTokenUpdatePayload)McPayloadBuilder.getGson().fromJson(string, McTokenUpdatePayload.class);
    }
}

