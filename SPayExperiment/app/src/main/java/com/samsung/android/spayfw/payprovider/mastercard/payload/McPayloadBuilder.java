/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.McEnrollmentRequestPayload;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McDeviceInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSeInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSpsdInfo;

public class McPayloadBuilder {
    private static Gson mGson = new Gson();

    public static JsonObject buildMcEnrollmentRequestPayload(McSeInfo mcSeInfo, McCardInfoWrapper mcCardInfoWrapper, String string, McSpsdInfo mcSpsdInfo, McDeviceInfo mcDeviceInfo, String string2, String string3) {
        McEnrollmentRequestPayload mcEnrollmentRequestPayload = new McEnrollmentRequestPayload();
        mcEnrollmentRequestPayload.setDevice(mcDeviceInfo);
        mcEnrollmentRequestPayload.setSeInfo(mcSeInfo);
        mcEnrollmentRequestPayload.setCardInfo(mcCardInfoWrapper);
        mcEnrollmentRequestPayload.setCardletId(string);
        mcEnrollmentRequestPayload.setSpsdInfo(mcSpsdInfo);
        mcEnrollmentRequestPayload.setPaymentAppInstanceID(string2);
        mcEnrollmentRequestPayload.setTokenizationAuthenticationValue(string3);
        return McPayloadBuilder.toJsonObject(McPayloadBuilder.toString(mcEnrollmentRequestPayload));
    }

    public static Gson getGson() {
        return mGson;
    }

    public static byte[] toByteArray(Object object) {
        return McPayloadBuilder.toString(object).getBytes();
    }

    public static JsonObject toJsonObject(String string) {
        return new JsonParser().parse(string).getAsJsonObject();
    }

    public static String toString(Object object) {
        return mGson.toJson(object);
    }
}

