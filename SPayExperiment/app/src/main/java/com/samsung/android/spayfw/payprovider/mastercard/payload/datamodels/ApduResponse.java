/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.BufferUnderflowException
 *  java.nio.ByteBuffer
 *  java.nio.ByteOrder
 *  java.util.ArrayList
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
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
        if (apduCommand == null) {
            return;
        }
        this.messageId = apduCommand.getMessageId();
        this.apduResponse = "";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ApduResponse[] generateApduFromTa(ApduResponse[] arrapduResponse, byte[] arrby) {
        int n2;
        ByteBuffer byteBuffer;
        int n3;
        if (arrapduResponse == null) return null;
        if (arrby == null) {
            return null;
        }
        try {
            byteBuffer = ByteBuffer.wrap((byte[])arrby);
            mClearDataObject = new McCardClearData();
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            n2 = byteBuffer.getInt();
            Log.d(TAG, "Num of apdus received =" + n2);
            n3 = 0;
        }
        catch (BufferUnderflowException bufferUnderflowException) {
            bufferUnderflowException.printStackTrace();
            Log.d(TAG, "Num of apdus received from TA =");
            return null;
        }
        while (n3 < n2) {
            if (arrapduResponse[n3] != null) {
                String string;
                int n4 = byteBuffer.getInt();
                if (n4 > 0) {
                    byte[] arrby2 = new byte[n4];
                    byteBuffer.get(arrby2, 0, n4);
                    if (!mClearDataObject.loadClearDataElement(arrby2)) {
                        Log.e(TAG, "<<< DGI Parsing Failed");
                    }
                    String string2 = CryptoUtils.convertbyteToHexString(arrby2);
                    Log.d(TAG, "Received Apdu =" + string2);
                    string = string2;
                } else {
                    string = null;
                }
                byte[] arrby3 = new byte[2];
                byteBuffer.get(arrby3, 0, 2);
                String string3 = CryptoUtils.convertbyteToHexString(arrby3);
                Log.d(TAG, "Apdu value =" + string3);
                if (n3 < 2 && !TextUtils.isEmpty((CharSequence)string)) {
                    string3 = string + string3;
                }
                arrapduResponse[n3].setApduResponse(string3);
            }
            ++n3;
        }
        return arrapduResponse;
    }

    public static McCardClearData getMcCardClearDataObject() {
        return mClearDataObject;
    }

    public static JsonObject getNotifyTokenRequest(ApduResponse[] arrapduResponse) {
        if (arrapduResponse == null) {
            return null;
        }
        NotifyTokenProvisionResult notifyTokenProvisionResult = new NotifyTokenProvisionResult(arrapduResponse);
        try {
            String string = new Gson().toJson((Object)notifyTokenProvisionResult);
            Log.d(TAG, "Apdu String for notify token report" + string);
            JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();
            Log.d(TAG, "Apdu jsonObject for notify report" + jsonObject.toString());
            return jsonObject;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Log.d(TAG, "Error composing Notify Token Result from Apdu Responses");
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ApduResponse[] initApduResponseFromCommand(ApduCommand[] arrapduCommand) {
        if (arrapduCommand == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int n2 = arrapduCommand.length;
        int n3 = 0;
        while (n3 < n2) {
            ApduCommand apduCommand = arrapduCommand[n3];
            if (apduCommand != null) {
                arrayList.add((Object)new ApduResponse(apduCommand));
            }
            ++n3;
        }
        return (ApduResponse[])arrayList.toArray((Object[])new ApduResponse[arrayList.size()]);
    }

    public String getApduResponse() {
        return this.apduResponse;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setApduResponse(String string) {
        this.apduResponse = string;
    }

    public void setMessageId(String string) {
        this.messageId = string;
    }
}

