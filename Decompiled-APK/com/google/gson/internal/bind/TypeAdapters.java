package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.UUID;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public final class TypeAdapters {
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
    public static final TypeAdapter<BigInteger> BIG_INTEGER;
    public static final TypeAdapter<BitSet> BIT_SET;
    public static final TypeAdapterFactory BIT_SET_FACTORY;
    public static final TypeAdapter<Boolean> BOOLEAN;
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
    public static final TypeAdapterFactory BOOLEAN_FACTORY;
    public static final TypeAdapter<Number> BYTE;
    public static final TypeAdapterFactory BYTE_FACTORY;
    public static final TypeAdapter<Calendar> CALENDAR;
    public static final TypeAdapterFactory CALENDAR_FACTORY;
    public static final TypeAdapter<Character> CHARACTER;
    public static final TypeAdapterFactory CHARACTER_FACTORY;
    public static final TypeAdapter<Class> CLASS;
    public static final TypeAdapterFactory CLASS_FACTORY;
    public static final TypeAdapter<Number> DOUBLE;
    public static final TypeAdapterFactory ENUM_FACTORY;
    public static final TypeAdapter<Number> FLOAT;
    public static final TypeAdapter<InetAddress> INET_ADDRESS;
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
    public static final TypeAdapter<Number> INTEGER;
    public static final TypeAdapterFactory INTEGER_FACTORY;
    public static final TypeAdapter<JsonElement> JSON_ELEMENT;
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
    public static final TypeAdapter<Locale> LOCALE;
    public static final TypeAdapterFactory LOCALE_FACTORY;
    public static final TypeAdapter<Number> LONG;
    public static final TypeAdapter<Number> NUMBER;
    public static final TypeAdapterFactory NUMBER_FACTORY;
    public static final TypeAdapter<Number> SHORT;
    public static final TypeAdapterFactory SHORT_FACTORY;
    public static final TypeAdapter<String> STRING;
    public static final TypeAdapter<StringBuffer> STRING_BUFFER;
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
    public static final TypeAdapter<StringBuilder> STRING_BUILDER;
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
    public static final TypeAdapterFactory STRING_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapter<URI> URI;
    public static final TypeAdapterFactory URI_FACTORY;
    public static final TypeAdapter<URL> URL;
    public static final TypeAdapterFactory URL_FACTORY;
    public static final TypeAdapter<UUID> UUID;
    public static final TypeAdapterFactory UUID_FACTORY;

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.1 */
    static class C03101 extends TypeAdapter<Class> {
        C03101() {
        }

        public void write(JsonWriter jsonWriter, Class cls) {
            if (cls == null) {
                jsonWriter.nullValue();
                return;
            }
            throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + cls.getName() + ". Forgot to register a type adapter?");
        }

        public Class read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.27 */
    static class AnonymousClass27 implements TypeAdapterFactory {
        final /* synthetic */ TypeToken val$type;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass27(TypeToken typeToken, TypeAdapter typeAdapter) {
            this.val$type = typeToken;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.equals(this.val$type) ? this.val$typeAdapter : null;
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.28 */
    static class AnonymousClass28 implements TypeAdapterFactory {
        final /* synthetic */ Class val$type;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass28(Class cls, TypeAdapter typeAdapter) {
            this.val$type = cls;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == this.val$type ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[type=" + this.val$type.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.29 */
    static class AnonymousClass29 implements TypeAdapterFactory {
        final /* synthetic */ Class val$boxed;
        final /* synthetic */ TypeAdapter val$typeAdapter;
        final /* synthetic */ Class val$unboxed;

        AnonymousClass29(Class cls, Class cls2, TypeAdapter typeAdapter) {
            this.val$unboxed = cls;
            this.val$boxed = cls2;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class rawType = typeToken.getRawType();
            return (rawType == this.val$unboxed || rawType == this.val$boxed) ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[type=" + this.val$boxed.getName() + "+" + this.val$unboxed.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.2 */
    static class C03122 extends TypeAdapter<BitSet> {
        C03122() {
        }

        public BitSet read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            BitSet bitSet = new BitSet();
            jsonReader.beginArray();
            JsonToken peek = jsonReader.peek();
            int i = 0;
            while (peek != JsonToken.END_ARRAY) {
                boolean z;
                switch (AnonymousClass32.$SwitchMap$com$google$gson$stream$JsonToken[peek.ordinal()]) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        if (jsonReader.nextInt() == 0) {
                            z = false;
                            break;
                        }
                        z = true;
                        break;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        z = jsonReader.nextBoolean();
                        break;
                    case F2m.PPB /*3*/:
                        String nextString = jsonReader.nextString();
                        try {
                            if (Integer.parseInt(nextString) == 0) {
                                z = false;
                                break;
                            }
                            z = true;
                            break;
                        } catch (NumberFormatException e) {
                            throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + nextString);
                        }
                    default:
                        throw new JsonSyntaxException("Invalid bitset value type: " + peek);
                }
                if (z) {
                    bitSet.set(i);
                }
                i++;
                peek = jsonReader.peek();
            }
            jsonReader.endArray();
            return bitSet;
        }

        public void write(JsonWriter jsonWriter, BitSet bitSet) {
            if (bitSet == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginArray();
            for (int i = 0; i < bitSet.length(); i++) {
                int i2;
                if (bitSet.get(i)) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                jsonWriter.value((long) i2);
            }
            jsonWriter.endArray();
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.30 */
    static class AnonymousClass30 implements TypeAdapterFactory {
        final /* synthetic */ Class val$base;
        final /* synthetic */ Class val$sub;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass30(Class cls, Class cls2, TypeAdapter typeAdapter) {
            this.val$base = cls;
            this.val$sub = cls2;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class rawType = typeToken.getRawType();
            return (rawType == this.val$base || rawType == this.val$sub) ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[type=" + this.val$base.getName() + "+" + this.val$sub.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.31 */
    static class AnonymousClass31 implements TypeAdapterFactory {
        final /* synthetic */ Class val$clazz;
        final /* synthetic */ TypeAdapter val$typeAdapter;

        AnonymousClass31(Class cls, TypeAdapter typeAdapter) {
            this.val$clazz = cls;
            this.val$typeAdapter = typeAdapter;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return this.val$clazz.isAssignableFrom(typeToken.getRawType()) ? this.val$typeAdapter : null;
        }

        public String toString() {
            return "Factory[typeHierarchy=" + this.val$clazz.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.32 */
    static /* synthetic */ class AnonymousClass32 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken;

        static {
            $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_DOCUMENT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_ARRAY.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.3 */
    static class C03133 extends TypeAdapter<Boolean> {
        C03133() {
        }

        public Boolean read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else if (jsonReader.peek() == JsonToken.STRING) {
                return Boolean.valueOf(Boolean.parseBoolean(jsonReader.nextString()));
            } else {
                return Boolean.valueOf(jsonReader.nextBoolean());
            }
        }

        public void write(JsonWriter jsonWriter, Boolean bool) {
            if (bool == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(bool.booleanValue());
            }
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.4 */
    static class C03144 extends TypeAdapter<Boolean> {
        C03144() {
        }

        public Boolean read(JsonReader jsonReader) {
            if (jsonReader.peek() != JsonToken.NULL) {
                return Boolean.valueOf(jsonReader.nextString());
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, Boolean bool) {
            jsonWriter.value(bool == null ? "null" : bool.toString());
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.5 */
    static class C03155 extends TypeAdapter<Number> {
        C03155() {
        }

        public Number read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                return Byte.valueOf((byte) jsonReader.nextInt());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter jsonWriter, Number number) {
            jsonWriter.value(number);
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.6 */
    static class C03166 extends TypeAdapter<Number> {
        C03166() {
        }

        public Number read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                return Short.valueOf((short) jsonReader.nextInt());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter jsonWriter, Number number) {
            jsonWriter.value(number);
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.7 */
    static class C03177 extends TypeAdapter<Number> {
        C03177() {
        }

        public Number read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                return Integer.valueOf(jsonReader.nextInt());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter jsonWriter, Number number) {
            jsonWriter.value(number);
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.8 */
    static class C03188 extends TypeAdapter<Number> {
        C03188() {
        }

        public Number read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            try {
                return Long.valueOf(jsonReader.nextLong());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter jsonWriter, Number number) {
            jsonWriter.value(number);
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters.9 */
    static class C03199 extends TypeAdapter<Number> {
        C03199() {
        }

        public Number read(JsonReader jsonReader) {
            if (jsonReader.peek() != JsonToken.NULL) {
                return Float.valueOf((float) jsonReader.nextDouble());
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, Number number) {
            jsonWriter.value(number);
        }
    }

    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<T, String> constantToName;
        private final Map<String, T> nameToConstant;

        public EnumTypeAdapter(Class<T> cls) {
            this.nameToConstant = new HashMap();
            this.constantToName = new HashMap();
            try {
                for (Enum enumR : (Enum[]) cls.getEnumConstants()) {
                    Object value;
                    String name = enumR.name();
                    SerializedName serializedName = (SerializedName) cls.getField(name).getAnnotation(SerializedName.class);
                    if (serializedName != null) {
                        value = serializedName.value();
                    } else {
                        String str = name;
                    }
                    this.nameToConstant.put(value, enumR);
                    this.constantToName.put(enumR, value);
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError();
            }
        }

        public T read(JsonReader jsonReader) {
            if (jsonReader.peek() != JsonToken.NULL) {
                return (Enum) this.nameToConstant.get(jsonReader.nextString());
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, T t) {
            jsonWriter.value(t == null ? null : (String) this.constantToName.get(t));
        }
    }

    private TypeAdapters() {
    }

    static {
        CLASS = new C03101();
        CLASS_FACTORY = newFactory(Class.class, CLASS);
        BIT_SET = new C03122();
        BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
        BOOLEAN = new C03133();
        BOOLEAN_AS_STRING = new C03144();
        BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
        BYTE = new C03155();
        BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, BYTE);
        SHORT = new C03166();
        SHORT_FACTORY = newFactory(Short.TYPE, Short.class, SHORT);
        INTEGER = new C03177();
        INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, INTEGER);
        LONG = new C03188();
        FLOAT = new C03199();
        DOUBLE = new TypeAdapter<Number>() {
            public Number read(JsonReader jsonReader) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return Double.valueOf(jsonReader.nextDouble());
                }
                jsonReader.nextNull();
                return null;
            }

            public void write(JsonWriter jsonWriter, Number number) {
                jsonWriter.value(number);
            }
        };
        NUMBER = new TypeAdapter<Number>() {
            public Number read(JsonReader jsonReader) {
                JsonToken peek = jsonReader.peek();
                switch (AnonymousClass32.$SwitchMap$com$google$gson$stream$JsonToken[peek.ordinal()]) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        return new LazilyParsedNumber(jsonReader.nextString());
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        jsonReader.nextNull();
                        return null;
                    default:
                        throw new JsonSyntaxException("Expecting number, got: " + peek);
                }
            }

            public void write(JsonWriter jsonWriter, Number number) {
                jsonWriter.value(number);
            }
        };
        NUMBER_FACTORY = newFactory(Number.class, NUMBER);
        CHARACTER = new TypeAdapter<Character>() {
            public Character read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                String nextString = jsonReader.nextString();
                if (nextString.length() == 1) {
                    return Character.valueOf(nextString.charAt(0));
                }
                throw new JsonSyntaxException("Expecting character, got: " + nextString);
            }

            public void write(JsonWriter jsonWriter, Character ch) {
                jsonWriter.value(ch == null ? null : String.valueOf(ch));
            }
        };
        CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, CHARACTER);
        STRING = new TypeAdapter<String>() {
            public String read(JsonReader jsonReader) {
                JsonToken peek = jsonReader.peek();
                if (peek == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                } else if (peek == JsonToken.BOOLEAN) {
                    return Boolean.toString(jsonReader.nextBoolean());
                } else {
                    return jsonReader.nextString();
                }
            }

            public void write(JsonWriter jsonWriter, String str) {
                jsonWriter.value(str);
            }
        };
        BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
            public BigDecimal read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return new BigDecimal(jsonReader.nextString());
                } catch (Throwable e) {
                    throw new JsonSyntaxException(e);
                }
            }

            public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) {
                jsonWriter.value((Number) bigDecimal);
            }
        };
        BIG_INTEGER = new TypeAdapter<BigInteger>() {
            public BigInteger read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return new BigInteger(jsonReader.nextString());
                } catch (Throwable e) {
                    throw new JsonSyntaxException(e);
                }
            }

            public void write(JsonWriter jsonWriter, BigInteger bigInteger) {
                jsonWriter.value((Number) bigInteger);
            }
        };
        STRING_FACTORY = newFactory(String.class, STRING);
        STRING_BUILDER = new TypeAdapter<StringBuilder>() {
            public StringBuilder read(JsonReader jsonReader) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return new StringBuilder(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            public void write(JsonWriter jsonWriter, StringBuilder stringBuilder) {
                jsonWriter.value(stringBuilder == null ? null : stringBuilder.toString());
            }
        };
        STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
        STRING_BUFFER = new TypeAdapter<StringBuffer>() {
            public StringBuffer read(JsonReader jsonReader) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return new StringBuffer(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            public void write(JsonWriter jsonWriter, StringBuffer stringBuffer) {
                jsonWriter.value(stringBuffer == null ? null : stringBuffer.toString());
            }
        };
        STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
        URL = new TypeAdapter<URL>() {
            public URL read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                String nextString = jsonReader.nextString();
                if ("null".equals(nextString)) {
                    return null;
                }
                return new URL(nextString);
            }

            public void write(JsonWriter jsonWriter, URL url) {
                jsonWriter.value(url == null ? null : url.toExternalForm());
            }
        };
        URL_FACTORY = newFactory(URL.class, URL);
        URI = new TypeAdapter<URI>() {
            public URI read(JsonReader jsonReader) {
                URI uri = null;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                } else {
                    try {
                        String nextString = jsonReader.nextString();
                        if (!"null".equals(nextString)) {
                            uri = new URI(nextString);
                        }
                    } catch (Throwable e) {
                        throw new JsonIOException(e);
                    }
                }
                return uri;
            }

            public void write(JsonWriter jsonWriter, URI uri) {
                jsonWriter.value(uri == null ? null : uri.toASCIIString());
            }
        };
        URI_FACTORY = newFactory(URI.class, URI);
        INET_ADDRESS = new TypeAdapter<InetAddress>() {
            public InetAddress read(JsonReader jsonReader) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return InetAddress.getByName(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            public void write(JsonWriter jsonWriter, InetAddress inetAddress) {
                jsonWriter.value(inetAddress == null ? null : inetAddress.getHostAddress());
            }
        };
        INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
        UUID = new TypeAdapter<UUID>() {
            public UUID read(JsonReader jsonReader) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return UUID.fromString(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            public void write(JsonWriter jsonWriter, UUID uuid) {
                jsonWriter.value(uuid == null ? null : uuid.toString());
            }
        };
        UUID_FACTORY = newFactory(UUID.class, UUID);
        TIMESTAMP_FACTORY = new TypeAdapterFactory() {

            /* renamed from: com.google.gson.internal.bind.TypeAdapters.22.1 */
            class C03111 extends TypeAdapter<Timestamp> {
                final /* synthetic */ TypeAdapter val$dateTypeAdapter;

                C03111(TypeAdapter typeAdapter) {
                    this.val$dateTypeAdapter = typeAdapter;
                }

                public Timestamp read(JsonReader jsonReader) {
                    Date date = (Date) this.val$dateTypeAdapter.read(jsonReader);
                    return date != null ? new Timestamp(date.getTime()) : null;
                }

                public void write(JsonWriter jsonWriter, Timestamp timestamp) {
                    this.val$dateTypeAdapter.write(jsonWriter, timestamp);
                }
            }

            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() != Timestamp.class) {
                    return null;
                }
                return new C03111(gson.getAdapter(Date.class));
            }
        };
        CALENDAR = new TypeAdapter<Calendar>() {
            private static final String DAY_OF_MONTH = "dayOfMonth";
            private static final String HOUR_OF_DAY = "hourOfDay";
            private static final String MINUTE = "minute";
            private static final String MONTH = "month";
            private static final String SECOND = "second";
            private static final String YEAR = "year";

            public Calendar read(JsonReader jsonReader) {
                int i = 0;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                jsonReader.beginObject();
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                    String nextName = jsonReader.nextName();
                    int nextInt = jsonReader.nextInt();
                    if (YEAR.equals(nextName)) {
                        i6 = nextInt;
                    } else if (MONTH.equals(nextName)) {
                        i5 = nextInt;
                    } else if (DAY_OF_MONTH.equals(nextName)) {
                        i4 = nextInt;
                    } else if (HOUR_OF_DAY.equals(nextName)) {
                        i3 = nextInt;
                    } else if (MINUTE.equals(nextName)) {
                        i2 = nextInt;
                    } else if (SECOND.equals(nextName)) {
                        i = nextInt;
                    }
                }
                jsonReader.endObject();
                return new GregorianCalendar(i6, i5, i4, i3, i2, i);
            }

            public void write(JsonWriter jsonWriter, Calendar calendar) {
                if (calendar == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginObject();
                jsonWriter.name(YEAR);
                jsonWriter.value((long) calendar.get(1));
                jsonWriter.name(MONTH);
                jsonWriter.value((long) calendar.get(2));
                jsonWriter.name(DAY_OF_MONTH);
                jsonWriter.value((long) calendar.get(5));
                jsonWriter.name(HOUR_OF_DAY);
                jsonWriter.value((long) calendar.get(11));
                jsonWriter.name(MINUTE);
                jsonWriter.value((long) calendar.get(12));
                jsonWriter.name(SECOND);
                jsonWriter.value((long) calendar.get(13));
                jsonWriter.endObject();
            }
        };
        CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);
        LOCALE = new TypeAdapter<Locale>() {
            public Locale read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                String nextToken;
                String nextToken2;
                String nextToken3;
                StringTokenizer stringTokenizer = new StringTokenizer(jsonReader.nextString(), "_");
                if (stringTokenizer.hasMoreElements()) {
                    nextToken = stringTokenizer.nextToken();
                } else {
                    nextToken = null;
                }
                if (stringTokenizer.hasMoreElements()) {
                    nextToken2 = stringTokenizer.nextToken();
                } else {
                    nextToken2 = null;
                }
                if (stringTokenizer.hasMoreElements()) {
                    nextToken3 = stringTokenizer.nextToken();
                } else {
                    nextToken3 = null;
                }
                if (nextToken2 == null && nextToken3 == null) {
                    return new Locale(nextToken);
                }
                if (nextToken3 == null) {
                    return new Locale(nextToken, nextToken2);
                }
                return new Locale(nextToken, nextToken2, nextToken3);
            }

            public void write(JsonWriter jsonWriter, Locale locale) {
                jsonWriter.value(locale == null ? null : locale.toString());
            }
        };
        LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
        JSON_ELEMENT = new TypeAdapter<JsonElement>() {
            public JsonElement read(JsonReader jsonReader) {
                JsonElement jsonArray;
                switch (AnonymousClass32.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        return new JsonPrimitive(new LazilyParsedNumber(jsonReader.nextString()));
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        return new JsonPrimitive(Boolean.valueOf(jsonReader.nextBoolean()));
                    case F2m.PPB /*3*/:
                        return new JsonPrimitive(jsonReader.nextString());
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        jsonReader.nextNull();
                        return JsonNull.INSTANCE;
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        jsonArray = new JsonArray();
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            jsonArray.add(read(jsonReader));
                        }
                        jsonReader.endArray();
                        return jsonArray;
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        jsonArray = new JsonObject();
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            jsonArray.add(jsonReader.nextName(), read(jsonReader));
                        }
                        jsonReader.endObject();
                        return jsonArray;
                    default:
                        throw new IllegalArgumentException();
                }
            }

            public void write(JsonWriter jsonWriter, JsonElement jsonElement) {
                if (jsonElement == null || jsonElement.isJsonNull()) {
                    jsonWriter.nullValue();
                } else if (jsonElement.isJsonPrimitive()) {
                    JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                    if (asJsonPrimitive.isNumber()) {
                        jsonWriter.value(asJsonPrimitive.getAsNumber());
                    } else if (asJsonPrimitive.isBoolean()) {
                        jsonWriter.value(asJsonPrimitive.getAsBoolean());
                    } else {
                        jsonWriter.value(asJsonPrimitive.getAsString());
                    }
                } else if (jsonElement.isJsonArray()) {
                    jsonWriter.beginArray();
                    Iterator it = jsonElement.getAsJsonArray().iterator();
                    while (it.hasNext()) {
                        write(jsonWriter, (JsonElement) it.next());
                    }
                    jsonWriter.endArray();
                } else if (jsonElement.isJsonObject()) {
                    jsonWriter.beginObject();
                    for (Entry entry : jsonElement.getAsJsonObject().entrySet()) {
                        jsonWriter.name((String) entry.getKey());
                        write(jsonWriter, (JsonElement) entry.getValue());
                    }
                    jsonWriter.endObject();
                } else {
                    throw new IllegalArgumentException("Couldn't write " + jsonElement.getClass());
                }
            }
        };
        JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
        ENUM_FACTORY = new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class rawType = typeToken.getRawType();
                if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                    return null;
                }
                if (!rawType.isEnum()) {
                    rawType = rawType.getSuperclass();
                }
                return new EnumTypeAdapter(rawType);
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(TypeToken<TT> typeToken, TypeAdapter<TT> typeAdapter) {
        return new AnonymousClass27(typeToken, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> cls, TypeAdapter<TT> typeAdapter) {
        return new AnonymousClass28(cls, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> cls, Class<TT> cls2, TypeAdapter<? super TT> typeAdapter) {
        return new AnonymousClass29(cls, cls2, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(Class<TT> cls, Class<? extends TT> cls2, TypeAdapter<? super TT> typeAdapter) {
        return new AnonymousClass30(cls, cls2, typeAdapter);
    }

    public static <TT> TypeAdapterFactory newTypeHierarchyFactory(Class<TT> cls, TypeAdapter<TT> typeAdapter) {
        return new AnonymousClass31(cls, typeAdapter);
    }
}
