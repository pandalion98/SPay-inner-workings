package com.samsung.android.spayfw.payprovider.mastercard.payload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.McEnrollmentRequestPayload;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McDeviceInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSeInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSpsdInfo;

public class McPayloadBuilder {
    private static Gson mGson;

    static {
        mGson = new Gson();
    }

    public static JsonObject buildMcEnrollmentRequestPayload(McSeInfo mcSeInfo, McCardInfoWrapper mcCardInfoWrapper, String str, McSpsdInfo mcSpsdInfo, McDeviceInfo mcDeviceInfo, String str2, String str3) {
        McEnrollmentRequestPayload mcEnrollmentRequestPayload = new McEnrollmentRequestPayload();
        mcEnrollmentRequestPayload.setDevice(mcDeviceInfo);
        mcEnrollmentRequestPayload.setSeInfo(mcSeInfo);
        mcEnrollmentRequestPayload.setCardInfo(mcCardInfoWrapper);
        mcEnrollmentRequestPayload.setCardletId(str);
        mcEnrollmentRequestPayload.setSpsdInfo(mcSpsdInfo);
        mcEnrollmentRequestPayload.setPaymentAppInstanceID(str2);
        mcEnrollmentRequestPayload.setTokenizationAuthenticationValue(str3);
        return toJsonObject(toString(mcEnrollmentRequestPayload));
    }

    public static String toString(Object obj) {
        return mGson.toJson(obj);
    }

    public static JsonObject toJsonObject(String str) {
        return new JsonParser().parse(str).getAsJsonObject();
    }

    public static byte[] toByteArray(Object obj) {
        return toString(obj).getBytes();
    }

    public static Gson getGson() {
        return mGson;
    }
}
