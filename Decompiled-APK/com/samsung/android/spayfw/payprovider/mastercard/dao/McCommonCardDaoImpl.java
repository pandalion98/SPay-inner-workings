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
import com.samsung.android.spayfw.p002b.Log;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class McCommonCardDaoImpl<T> extends MCAbstractDaoImpl<T> {
    private static final String TAG = "McCommonCardDaoImpl";
    protected static final String TEXT_ENCODING = "UTF8";

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.dao.McCommonCardDaoImpl.1 */
    static /* synthetic */ class C05591 {
        static final /* synthetic */ int[] f11x2fd9a198;

        static {
            f11x2fd9a198 = new int[CardInfoType.values().length];
            try {
                f11x2fd9a198[CardInfoType.MCPSE_CARD_PROFILE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f11x2fd9a198[CardInfoType.TA_DATA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f11x2fd9a198[CardInfoType.PROFILE_TABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f11x2fd9a198[CardInfoType.UNUSED_DGI_ELEMENTS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static final class ByteArrayInterfaceAdapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {
        private static final String CONTAINER_DATA = "container-data";
        private static final String CONTAINER_TYPE = "container-type";
        private static final List<String> sWhiteList;

        private ByteArrayInterfaceAdapter() {
        }

        static {
            sWhiteList = new ArrayList();
            sWhiteList.add("com.mastercard.mobile_api.bytes.AndroidByteArray");
            sWhiteList.add("com.mastercard.mobile_api.bytes.AndroidByteArrayFactory");
            sWhiteList.add("com.mastercard.mobile_api.bytes.DefaultByteArrayFactory");
            sWhiteList.add("com.mastercard.mobile_api.bytes.DefaultByteArrayImpl");
        }

        public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonObject = new JsonObject();
            try {
                jsonObject.addProperty(CONTAINER_TYPE, t.getClass().getName());
                jsonObject.add(CONTAINER_DATA, jsonSerializationContext.serialize(t));
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                Log.m286e(McCommonCardDaoImpl.TAG, "Exception occured in during custom serialization: " + e.getMessage());
                return null;
            }
        }

        public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            JsonObject jsonObject = (JsonObject) jsonElement;
            return jsonDeserializationContext.deserialize(get(jsonObject, CONTAINER_DATA), typeForName(get(jsonObject, CONTAINER_TYPE)));
        }

        private Type typeForName(JsonElement jsonElement) {
            try {
                if (sWhiteList.contains(jsonElement.getAsString())) {
                    return Class.forName(jsonElement.getAsString());
                }
                Log.m286e(McCommonCardDaoImpl.TAG, "deSerialization Err.. Not part of whitelist: " + jsonElement.getAsString());
                return null;
            } catch (Throwable e) {
                throw new JsonParseException(e);
            } catch (Throwable e2) {
                throw new JsonParseException(e2);
            }
        }

        private JsonElement get(JsonObject jsonObject, String str) {
            if (jsonObject == null) {
                throw new JsonParseException("no wrapper object found");
            }
            JsonElement jsonElement = jsonObject.get(str);
            if (jsonElement != null) {
                return jsonElement;
            }
            throw new JsonParseException("no '" + str + "' member found in what was expected to be an interface wrapper");
        }
    }

    protected enum CardInfoType {
        MCPSE_CARD_PROFILE(1),
        TA_DATA(2),
        PROFILE_TABLE(3),
        TA_ATC_DATA(4),
        UNUSED_DGI_ELEMENTS(5);
        
        private final int mValue;

        private CardInfoType(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    public McCommonCardDaoImpl(Context context) {
        super(context);
    }

    protected Gson createGson(CardInfoType cardInfoType) {
        if (cardInfoType == null) {
            Log.m285d(TAG, "Invalid cardInfo type selected..Using default gson:" + cardInfoType);
            return new GsonBuilder().create();
        }
        switch (C05591.f11x2fd9a198[cardInfoType.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new GsonBuilder().registerTypeAdapter(ByteArray.class, new ByteArrayInterfaceAdapter()).create();
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case F2m.PPB /*3*/:
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new GsonBuilder().create();
            default:
                Log.m285d(TAG, "Invalid cardInfo type selected..Using default gson" + cardInfoType.name());
                return null;
        }
    }
}
