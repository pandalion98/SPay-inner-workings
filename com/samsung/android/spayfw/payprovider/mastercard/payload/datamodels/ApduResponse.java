package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public final class ApduResponse {
    private static final String TAG = "ApduResponse";
    private static McCardClearData mClearDataObject;
    private String apduResponse;
    private String messageId;

    public ApduResponse(ApduCommand apduCommand) {
        if (apduCommand != null) {
            this.messageId = apduCommand.getMessageId();
            this.apduResponse = BuildConfig.FLAVOR;
        }
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String str) {
        this.messageId = str;
    }

    public String getApduResponse() {
        return this.apduResponse;
    }

    public void setApduResponse(String str) {
        this.apduResponse = str;
    }

    public static ApduResponse[] initApduResponseFromCommand(ApduCommand[] apduCommandArr) {
        if (apduCommandArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ApduCommand apduCommand : apduCommandArr) {
            if (apduCommand != null) {
                arrayList.add(new ApduResponse(apduCommand));
            }
        }
        return (ApduResponse[]) arrayList.toArray(new ApduResponse[arrayList.size()]);
    }

    public static JsonObject getNotifyTokenRequest(ApduResponse[] apduResponseArr) {
        if (apduResponseArr == null) {
            return null;
        }
        try {
            String toJson = new Gson().toJson(new NotifyTokenProvisionResult(apduResponseArr));
            Log.m285d(TAG, "Apdu String for notify token report" + toJson);
            JsonObject asJsonObject = new JsonParser().parse(toJson).getAsJsonObject();
            Log.m285d(TAG, "Apdu jsonObject for notify report" + asJsonObject.toString());
            return asJsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Log.m285d(TAG, "Error composing Notify Token Result from Apdu Responses");
            return null;
        }
    }

    public static ApduResponse[] generateApduFromTa(ApduResponse[] apduResponseArr, byte[] bArr) {
        if (apduResponseArr == null || bArr == null) {
            return null;
        }
        try {
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            mClearDataObject = new McCardClearData();
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            int i = wrap.getInt();
            Log.m285d(TAG, "Num of apdus received =" + i);
            for (int i2 = 0; i2 < i; i2++) {
                if (apduResponseArr[i2] != null) {
                    String convertbyteToHexString;
                    Object obj;
                    int i3 = wrap.getInt();
                    if (i3 > 0) {
                        byte[] bArr2 = new byte[i3];
                        wrap.get(bArr2, 0, i3);
                        if (!mClearDataObject.loadClearDataElement(bArr2)) {
                            Log.m286e(TAG, "<<< DGI Parsing Failed");
                        }
                        convertbyteToHexString = CryptoUtils.convertbyteToHexString(bArr2);
                        Log.m285d(TAG, "Received Apdu =" + convertbyteToHexString);
                        obj = convertbyteToHexString;
                    } else {
                        obj = null;
                    }
                    byte[] bArr3 = new byte[2];
                    wrap.get(bArr3, 0, 2);
                    convertbyteToHexString = CryptoUtils.convertbyteToHexString(bArr3);
                    Log.m285d(TAG, "Apdu value =" + convertbyteToHexString);
                    if (i2 < 2 && !TextUtils.isEmpty(obj)) {
                        convertbyteToHexString = obj + convertbyteToHexString;
                    }
                    apduResponseArr[i2].setApduResponse(convertbyteToHexString);
                }
            }
            return apduResponseArr;
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
            Log.m285d(TAG, "Num of apdus received from TA =");
            return null;
        }
    }

    public static McCardClearData getMcCardClearDataObject() {
        return mClearDataObject;
    }
}
