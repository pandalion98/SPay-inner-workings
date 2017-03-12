package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class JsonTreeReader extends JsonReader {
    private static final Object SENTINEL_CLOSED;
    private static final Reader UNREADABLE_READER;
    private final List<Object> stack;

    /* renamed from: com.google.gson.internal.bind.JsonTreeReader.1 */
    static class C03031 extends Reader {
        C03031() {
        }

        public int read(char[] cArr, int i, int i2) {
            throw new AssertionError();
        }

        public void close() {
            throw new AssertionError();
        }
    }

    static {
        UNREADABLE_READER = new C03031();
        SENTINEL_CLOSED = new Object();
    }

    public JsonTreeReader(JsonElement jsonElement) {
        super(UNREADABLE_READER);
        this.stack = new ArrayList();
        this.stack.add(jsonElement);
    }

    public void beginArray() {
        expect(JsonToken.BEGIN_ARRAY);
        this.stack.add(((JsonArray) peekStack()).iterator());
    }

    public void endArray() {
        expect(JsonToken.END_ARRAY);
        popStack();
        popStack();
    }

    public void beginObject() {
        expect(JsonToken.BEGIN_OBJECT);
        this.stack.add(((JsonObject) peekStack()).entrySet().iterator());
    }

    public void endObject() {
        expect(JsonToken.END_OBJECT);
        popStack();
        popStack();
    }

    public boolean hasNext() {
        JsonToken peek = peek();
        return (peek == JsonToken.END_OBJECT || peek == JsonToken.END_ARRAY) ? false : true;
    }

    public JsonToken peek() {
        if (this.stack.isEmpty()) {
            return JsonToken.END_DOCUMENT;
        }
        Object peekStack = peekStack();
        if (peekStack instanceof Iterator) {
            boolean z = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
            Iterator it = (Iterator) peekStack;
            if (!it.hasNext()) {
                return z ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            } else {
                if (z) {
                    return JsonToken.NAME;
                }
                this.stack.add(it.next());
                return peek();
            }
        } else if (peekStack instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        } else {
            if (peekStack instanceof JsonArray) {
                return JsonToken.BEGIN_ARRAY;
            }
            if (peekStack instanceof JsonPrimitive) {
                JsonPrimitive jsonPrimitive = (JsonPrimitive) peekStack;
                if (jsonPrimitive.isString()) {
                    return JsonToken.STRING;
                }
                if (jsonPrimitive.isBoolean()) {
                    return JsonToken.BOOLEAN;
                }
                if (jsonPrimitive.isNumber()) {
                    return JsonToken.NUMBER;
                }
                throw new AssertionError();
            } else if (peekStack instanceof JsonNull) {
                return JsonToken.NULL;
            } else {
                if (peekStack == SENTINEL_CLOSED) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                throw new AssertionError();
            }
        }
    }

    private Object peekStack() {
        return this.stack.get(this.stack.size() - 1);
    }

    private Object popStack() {
        return this.stack.remove(this.stack.size() - 1);
    }

    private void expect(JsonToken jsonToken) {
        if (peek() != jsonToken) {
            throw new IllegalStateException("Expected " + jsonToken + " but was " + peek());
        }
    }

    public String nextName() {
        expect(JsonToken.NAME);
        Entry entry = (Entry) ((Iterator) peekStack()).next();
        this.stack.add(entry.getValue());
        return (String) entry.getKey();
    }

    public String nextString() {
        JsonToken peek = peek();
        if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return ((JsonPrimitive) popStack()).getAsString();
        }
        throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + peek);
    }

    public boolean nextBoolean() {
        expect(JsonToken.BOOLEAN);
        return ((JsonPrimitive) popStack()).getAsBoolean();
    }

    public void nextNull() {
        expect(JsonToken.NULL);
        popStack();
    }

    public double nextDouble() {
        JsonToken peek = peek();
        if (peek == JsonToken.NUMBER || peek == JsonToken.STRING) {
            double asDouble = ((JsonPrimitive) peekStack()).getAsDouble();
            if (isLenient() || !(Double.isNaN(asDouble) || Double.isInfinite(asDouble))) {
                popStack();
                return asDouble;
            }
            throw new NumberFormatException("JSON forbids NaN and infinities: " + asDouble);
        }
        throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + peek);
    }

    public long nextLong() {
        JsonToken peek = peek();
        if (peek == JsonToken.NUMBER || peek == JsonToken.STRING) {
            long asLong = ((JsonPrimitive) peekStack()).getAsLong();
            popStack();
            return asLong;
        }
        throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + peek);
    }

    public int nextInt() {
        JsonToken peek = peek();
        if (peek == JsonToken.NUMBER || peek == JsonToken.STRING) {
            int asInt = ((JsonPrimitive) peekStack()).getAsInt();
            popStack();
            return asInt;
        }
        throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + peek);
    }

    public void close() {
        this.stack.clear();
        this.stack.add(SENTINEL_CLOSED);
    }

    public void skipValue() {
        if (peek() == JsonToken.NAME) {
            nextName();
        } else {
            popStack();
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public void promoteNameToValue() {
        expect(JsonToken.NAME);
        Entry entry = (Entry) ((Iterator) peekStack()).next();
        this.stack.add(entry.getValue());
        this.stack.add(new JsonPrimitive((String) entry.getKey()));
    }
}
