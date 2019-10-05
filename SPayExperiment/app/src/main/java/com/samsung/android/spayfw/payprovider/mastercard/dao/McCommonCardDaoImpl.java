/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.NoSuchFieldError
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.b.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class McCommonCardDaoImpl<T>
extends MCAbstractDaoImpl<T> {
    private static final String TAG = "McCommonCardDaoImpl";
    protected static final String TEXT_ENCODING = "UTF8";

    public McCommonCardDaoImpl(Context context) {
        super(context);
    }

    protected Gson createGson(CardInfoType cardInfoType) {
        if (cardInfoType == null) {
            Log.d(TAG, "Invalid cardInfo type selected..Using default gson:" + (Object)((Object)cardInfoType));
            return new GsonBuilder().create();
        }
        switch (1.$SwitchMap$com$samsung$android$spayfw$payprovider$mastercard$dao$McCommonCardDaoImpl$CardInfoType[cardInfoType.ordinal()]) {
            default: {
                Log.d(TAG, "Invalid cardInfo type selected..Using default gson" + cardInfoType.name());
                return null;
            }
            case 1: {
                return new GsonBuilder().registerTypeAdapter(ByteArray.class, new ByteArrayInterfaceAdapter()).create();
            }
            case 2: 
            case 3: 
            case 4: 
        }
        return new GsonBuilder().create();
    }

    private static final class ByteArrayInterfaceAdapter<T>
    implements JsonDeserializer<T>,
    JsonSerializer<T> {
        private static final String CONTAINER_DATA = "container-data";
        private static final String CONTAINER_TYPE = "container-type";
        private static final List<String> sWhiteList = new ArrayList();

        static {
            sWhiteList.add((Object)"com.mastercard.mobile_api.bytes.AndroidByteArray");
            sWhiteList.add((Object)"com.mastercard.mobile_api.bytes.AndroidByteArrayFactory");
            sWhiteList.add((Object)"com.mastercard.mobile_api.bytes.DefaultByteArrayFactory");
            sWhiteList.add((Object)"com.mastercard.mobile_api.bytes.DefaultByteArrayImpl");
        }

        private ByteArrayInterfaceAdapter() {
        }

        private JsonElement get(JsonObject jsonObject, String string) {
            if (jsonObject == null) {
                throw new JsonParseException("no wrapper object found");
            }
            JsonElement jsonElement = jsonObject.get(string);
            if (jsonElement == null) {
                throw new JsonParseException("no '" + string + "' member found in what was expected to be an interface wrapper");
            }
            return jsonElement;
        }

        private Type typeForName(JsonElement jsonElement) {
            block4 : {
                if (sWhiteList.contains((Object)jsonElement.getAsString())) break block4;
                Log.e(McCommonCardDaoImpl.TAG, "deSerialization Err.. Not part of whitelist: " + jsonElement.getAsString());
                return null;
            }
            try {
                Class class_ = Class.forName((String)jsonElement.getAsString());
                return class_;
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new JsonParseException((Throwable)classNotFoundException);
            }
            catch (NullPointerException nullPointerException) {
                throw new JsonParseException((Throwable)nullPointerException);
            }
        }

        public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            JsonObject jsonObject = (JsonObject)jsonElement;
            JsonElement jsonElement2 = this.get(jsonObject, CONTAINER_TYPE);
            return (T)jsonDeserializationContext.deserialize(this.get(jsonObject, CONTAINER_DATA), this.typeForName(jsonElement2));
        }

        public JsonElement serialize(T t2, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            try {
                jsonObject.addProperty(CONTAINER_TYPE, t2.getClass().getName());
                jsonObject.add(CONTAINER_DATA, jsonSerializationContext.serialize(t2));
                return jsonObject;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                Log.e(McCommonCardDaoImpl.TAG, "Exception occured in during custom serialization: " + exception.getMessage());
                return null;
            }
        }
    }

    protected static final class CardInfoType
    extends Enum<CardInfoType> {
        private static final /* synthetic */ CardInfoType[] $VALUES;
        public static final /* enum */ CardInfoType MCPSE_CARD_PROFILE = new CardInfoType(1);
        public static final /* enum */ CardInfoType PROFILE_TABLE;
        public static final /* enum */ CardInfoType TA_ATC_DATA;
        public static final /* enum */ CardInfoType TA_DATA;
        public static final /* enum */ CardInfoType UNUSED_DGI_ELEMENTS;
        private final int mValue;

        static {
            TA_DATA = new CardInfoType(2);
            PROFILE_TABLE = new CardInfoType(3);
            TA_ATC_DATA = new CardInfoType(4);
            UNUSED_DGI_ELEMENTS = new CardInfoType(5);
            CardInfoType[] arrcardInfoType = new CardInfoType[]{MCPSE_CARD_PROFILE, TA_DATA, PROFILE_TABLE, TA_ATC_DATA, UNUSED_DGI_ELEMENTS};
            $VALUES = arrcardInfoType;
        }

        private CardInfoType(int n3) {
            this.mValue = n3;
        }

        public static CardInfoType valueOf(String string) {
            return (CardInfoType)Enum.valueOf(CardInfoType.class, (String)string);
        }

        public static CardInfoType[] values() {
            return (CardInfoType[])$VALUES.clone();
        }

        public int getValue() {
            return this.mValue;
        }
    }

}

