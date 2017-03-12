package com.google.gson.stream;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.android.gms.location.places.Place;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.EncryptionAlgorithm;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.jce.X509KeyUsage;

public class JsonReader implements Closeable {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final char[] NON_EXECUTE_PREFIX;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 17;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final char[] buffer;
    private final Reader in;
    private boolean lenient;
    private int limit;
    private int lineNumber;
    private int lineStart;
    private int[] pathIndices;
    private String[] pathNames;
    private int peeked;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos;
    private int[] stack;
    private int stackSize;

    /* renamed from: com.google.gson.stream.JsonReader.1 */
    static class C03201 extends JsonReaderInternalAccess {
        C03201() {
        }

        public void promoteNameToValue(JsonReader jsonReader) {
            if (jsonReader instanceof JsonTreeReader) {
                ((JsonTreeReader) jsonReader).promoteNameToValue();
                return;
            }
            int access$000 = jsonReader.peeked;
            if (access$000 == 0) {
                access$000 = jsonReader.doPeek();
            }
            if (access$000 == JsonReader.PEEKED_DOUBLE_QUOTED_NAME) {
                jsonReader.peeked = JsonReader.PEEKED_DOUBLE_QUOTED;
            } else if (access$000 == JsonReader.PEEKED_SINGLE_QUOTED_NAME) {
                jsonReader.peeked = JsonReader.PEEKED_SINGLE_QUOTED;
            } else if (access$000 == JsonReader.PEEKED_UNQUOTED_NAME) {
                jsonReader.peeked = JsonReader.PEEKED_UNQUOTED;
            } else {
                throw new IllegalStateException("Expected a name but was " + jsonReader.peek() + " " + " at line " + jsonReader.getLineNumber() + " column " + jsonReader.getColumnNumber() + " path " + jsonReader.getPath());
            }
        }
    }

    static {
        NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
        JsonReaderInternalAccess.INSTANCE = new C03201();
    }

    public JsonReader(Reader reader) {
        this.lenient = false;
        this.buffer = new char[SkeinMac.SKEIN_1024];
        this.pos = PEEKED_NONE;
        this.limit = PEEKED_NONE;
        this.lineNumber = PEEKED_NONE;
        this.lineStart = PEEKED_NONE;
        this.peeked = PEEKED_NONE;
        this.stack = new int[32];
        this.stackSize = PEEKED_NONE;
        int[] iArr = this.stack;
        int i = this.stackSize;
        this.stackSize = i + PEEKED_BEGIN_OBJECT;
        iArr[i] = PEEKED_FALSE;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (reader == null) {
            throw new NullPointerException("in == null");
        }
        this.in = reader;
    }

    public final void setLenient(boolean z) {
        this.lenient = z;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_BEGIN_ARRAY) {
            push(PEEKED_BEGIN_OBJECT);
            this.pathIndices[this.stackSize - 1] = PEEKED_NONE;
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    public void endArray() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_END_ARRAY) {
            this.stackSize--;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    public void beginObject() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_BEGIN_OBJECT) {
            push(PEEKED_BEGIN_ARRAY);
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    public void endObject() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_END_OBJECT) {
            this.stackSize--;
            this.pathNames[this.stackSize] = null;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
            this.peeked = PEEKED_NONE;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    public boolean hasNext() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        return (i == PEEKED_END_OBJECT || i == PEEKED_END_ARRAY) ? false : true;
    }

    public JsonToken peek() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        switch (i) {
            case PEEKED_BEGIN_OBJECT /*1*/:
                return JsonToken.BEGIN_OBJECT;
            case PEEKED_END_OBJECT /*2*/:
                return JsonToken.END_OBJECT;
            case PEEKED_BEGIN_ARRAY /*3*/:
                return JsonToken.BEGIN_ARRAY;
            case PEEKED_END_ARRAY /*4*/:
                return JsonToken.END_ARRAY;
            case PEEKED_TRUE /*5*/:
            case PEEKED_FALSE /*6*/:
                return JsonToken.BOOLEAN;
            case PEEKED_NULL /*7*/:
                return JsonToken.NULL;
            case PEEKED_SINGLE_QUOTED /*8*/:
            case PEEKED_DOUBLE_QUOTED /*9*/:
            case PEEKED_UNQUOTED /*10*/:
            case PEEKED_BUFFERED /*11*/:
                return JsonToken.STRING;
            case PEEKED_SINGLE_QUOTED_NAME /*12*/:
            case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
            case PEEKED_UNQUOTED_NAME /*14*/:
                return JsonToken.NAME;
            case PEEKED_LONG /*15*/:
            case PEEKED_NUMBER /*16*/:
                return JsonToken.NUMBER;
            case PEEKED_EOF /*17*/:
                return JsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    private int doPeek() {
        int nextNonWhitespace;
        int i = this.stack[this.stackSize - 1];
        if (i == PEEKED_BEGIN_OBJECT) {
            this.stack[this.stackSize - 1] = PEEKED_END_OBJECT;
        } else if (i == PEEKED_END_OBJECT) {
            switch (nextNonWhitespace(true)) {
                case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                    break;
                case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                    checkLenient();
                    break;
                case EACTags.HEADER_LIST /*93*/:
                    this.peeked = PEEKED_END_ARRAY;
                    return PEEKED_END_ARRAY;
                default:
                    throw syntaxError("Unterminated array");
            }
        } else if (i == PEEKED_BEGIN_ARRAY || i == PEEKED_TRUE) {
            this.stack[this.stackSize - 1] = PEEKED_END_ARRAY;
            if (i == PEEKED_TRUE) {
                switch (nextNonWhitespace(true)) {
                    case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                        break;
                    case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                        checkLenient();
                        break;
                    case EACTags.SECURE_MESSAGING_TEMPLATE /*125*/:
                        this.peeked = PEEKED_END_OBJECT;
                        return PEEKED_END_OBJECT;
                    default:
                        throw syntaxError("Unterminated object");
                }
            }
            nextNonWhitespace = nextNonWhitespace(true);
            switch (nextNonWhitespace) {
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    this.peeked = PEEKED_DOUBLE_QUOTED_NAME;
                    return PEEKED_DOUBLE_QUOTED_NAME;
                case Place.TYPE_FUNERAL_HOME /*39*/:
                    checkLenient();
                    this.peeked = PEEKED_SINGLE_QUOTED_NAME;
                    return PEEKED_SINGLE_QUOTED_NAME;
                case EACTags.SECURE_MESSAGING_TEMPLATE /*125*/:
                    if (i != PEEKED_TRUE) {
                        this.peeked = PEEKED_END_OBJECT;
                        return PEEKED_END_OBJECT;
                    }
                    throw syntaxError("Expected name");
                default:
                    checkLenient();
                    this.pos--;
                    if (isLiteral((char) nextNonWhitespace)) {
                        this.peeked = PEEKED_UNQUOTED_NAME;
                        return PEEKED_UNQUOTED_NAME;
                    }
                    throw syntaxError("Expected name");
            }
        } else if (i == PEEKED_END_ARRAY) {
            this.stack[this.stackSize - 1] = PEEKED_TRUE;
            switch (nextNonWhitespace(true)) {
                case CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA /*58*/:
                    break;
                case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
                    checkLenient();
                    if ((this.pos < this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) && this.buffer[this.pos] == '>') {
                        this.pos += PEEKED_BEGIN_OBJECT;
                        break;
                    }
                default:
                    throw syntaxError("Expected ':'");
            }
        } else if (i == PEEKED_FALSE) {
            if (this.lenient) {
                consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = PEEKED_NULL;
        } else if (i == PEEKED_NULL) {
            if (nextNonWhitespace(false) == -1) {
                this.peeked = PEEKED_EOF;
                return PEEKED_EOF;
            }
            checkLenient();
            this.pos--;
        } else if (i == PEEKED_SINGLE_QUOTED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        switch (nextNonWhitespace(true)) {
            case Place.TYPE_ESTABLISHMENT /*34*/:
                if (this.stackSize == PEEKED_BEGIN_OBJECT) {
                    checkLenient();
                }
                this.peeked = PEEKED_DOUBLE_QUOTED;
                return PEEKED_DOUBLE_QUOTED;
            case Place.TYPE_FUNERAL_HOME /*39*/:
                checkLenient();
                this.peeked = PEEKED_SINGLE_QUOTED;
                return PEEKED_SINGLE_QUOTED;
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                break;
            case EACTags.NAME /*91*/:
                this.peeked = PEEKED_BEGIN_ARRAY;
                return PEEKED_BEGIN_ARRAY;
            case EACTags.HEADER_LIST /*93*/:
                if (i == PEEKED_BEGIN_OBJECT) {
                    this.peeked = PEEKED_END_ARRAY;
                    return PEEKED_END_ARRAY;
                }
                break;
            case EACTags.SECURITY_ENVIRONMENT_TEMPLATE /*123*/:
                this.peeked = PEEKED_BEGIN_OBJECT;
                return PEEKED_BEGIN_OBJECT;
            default:
                this.pos--;
                if (this.stackSize == PEEKED_BEGIN_OBJECT) {
                    checkLenient();
                }
                nextNonWhitespace = peekKeyword();
                if (nextNonWhitespace != 0) {
                    return nextNonWhitespace;
                }
                nextNonWhitespace = peekNumber();
                if (nextNonWhitespace != 0) {
                    return nextNonWhitespace;
                }
                if (isLiteral(this.buffer[this.pos])) {
                    checkLenient();
                    this.peeked = PEEKED_UNQUOTED;
                    return PEEKED_UNQUOTED;
                }
                throw syntaxError("Expected value");
        }
        if (i == PEEKED_BEGIN_OBJECT || i == PEEKED_END_OBJECT) {
            checkLenient();
            this.pos--;
            this.peeked = PEEKED_NULL;
            return PEEKED_NULL;
        }
        throw syntaxError("Unexpected value");
    }

    private int peekKeyword() {
        String str;
        int i;
        char c = this.buffer[this.pos];
        String str2;
        if (c == 't' || c == 'T') {
            str = "true";
            str2 = PaymentFramework.CONFIG_VALUE_TRUE;
            i = PEEKED_TRUE;
        } else if (c == 'f' || c == 'F') {
            str = "false";
            str2 = PaymentFramework.CONFIG_VALUE_FALSE;
            i = PEEKED_FALSE;
        } else if (c != 'n' && c != 'N') {
            return PEEKED_NONE;
        } else {
            str = "null";
            str2 = "NULL";
            i = PEEKED_NULL;
        }
        int length = str.length();
        int i2 = PEEKED_BEGIN_OBJECT;
        while (i2 < length) {
            if (this.pos + i2 >= this.limit && !fillBuffer(i2 + PEEKED_BEGIN_OBJECT)) {
                return PEEKED_NONE;
            }
            char c2 = this.buffer[this.pos + i2];
            if (c2 != str.charAt(i2) && c2 != r1.charAt(i2)) {
                return PEEKED_NONE;
            }
            i2 += PEEKED_BEGIN_OBJECT;
        }
        if ((this.pos + length < this.limit || fillBuffer(length + PEEKED_BEGIN_OBJECT)) && isLiteral(this.buffer[this.pos + length])) {
            return PEEKED_NONE;
        }
        this.pos += length;
        this.peeked = i;
        return i;
    }

    private int peekNumber() {
        char[] cArr = this.buffer;
        int i = this.pos;
        int i2 = this.limit;
        long j = 0;
        Object obj = null;
        int i3 = PEEKED_BEGIN_OBJECT;
        int i4 = PEEKED_NONE;
        int i5 = PEEKED_NONE;
        int i6 = i2;
        i2 = i;
        while (true) {
            Object obj2;
            if (i2 + i5 == i6) {
                if (i5 == cArr.length) {
                    return PEEKED_NONE;
                }
                if (fillBuffer(i5 + PEEKED_BEGIN_OBJECT)) {
                    i2 = this.pos;
                    i6 = this.limit;
                } else if (i4 != PEEKED_END_OBJECT && i3 != 0 && (j != Long.MIN_VALUE || obj != null)) {
                    if (obj == null) {
                        j = -j;
                    }
                    this.peekedLong = j;
                    this.pos += i5;
                    this.peeked = PEEKED_LONG;
                    return PEEKED_LONG;
                } else if (i4 == PEEKED_END_OBJECT && i4 != PEEKED_END_ARRAY && i4 != PEEKED_NULL) {
                    return PEEKED_NONE;
                } else {
                    this.peekedNumberLength = i5;
                    this.peeked = PEEKED_NUMBER;
                    return PEEKED_NUMBER;
                }
            }
            char c = cArr[i2 + i5];
            int i7;
            switch (c) {
                case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                    if (i4 != PEEKED_TRUE) {
                        return PEEKED_NONE;
                    }
                    i = PEEKED_FALSE;
                    i4 = i3;
                    obj2 = obj;
                    continue;
                case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                    if (i4 == 0) {
                        i = PEEKED_BEGIN_OBJECT;
                        i7 = i3;
                        obj2 = PEEKED_BEGIN_OBJECT;
                        i4 = i7;
                        continue;
                    } else if (i4 == PEEKED_TRUE) {
                        i = PEEKED_FALSE;
                        i4 = i3;
                        obj2 = obj;
                        break;
                    } else {
                        return PEEKED_NONE;
                    }
                case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
                    if (i4 != PEEKED_END_OBJECT) {
                        return PEEKED_NONE;
                    }
                    i = PEEKED_BEGIN_ARRAY;
                    i4 = i3;
                    obj2 = obj;
                    continue;
                case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA /*69*/:
                case ExtensionType.negotiated_ff_dhe_groups /*101*/:
                    if (i4 != PEEKED_END_OBJECT && i4 != PEEKED_END_ARRAY) {
                        return PEEKED_NONE;
                    }
                    i = PEEKED_TRUE;
                    i4 = i3;
                    obj2 = obj;
                    continue;
                default:
                    if (c >= LLVARUtil.EMPTY_STRING && c <= '9') {
                        if (i4 != PEEKED_BEGIN_OBJECT && i4 != 0) {
                            if (i4 != PEEKED_END_OBJECT) {
                                if (i4 != PEEKED_BEGIN_ARRAY) {
                                    if (i4 != PEEKED_TRUE && i4 != PEEKED_FALSE) {
                                        i = i4;
                                        i4 = i3;
                                        obj2 = obj;
                                        break;
                                    }
                                    i = PEEKED_NULL;
                                    i4 = i3;
                                    obj2 = obj;
                                    break;
                                }
                                i = PEEKED_END_ARRAY;
                                i4 = i3;
                                obj2 = obj;
                                break;
                            } else if (j != 0) {
                                long j2 = (10 * j) - ((long) (c - 48));
                                i = (j > MIN_INCOMPLETE_INTEGER || (j == MIN_INCOMPLETE_INTEGER && j2 < j)) ? PEEKED_BEGIN_OBJECT : PEEKED_NONE;
                                i &= i3;
                                obj2 = obj;
                                j = j2;
                                i7 = i4;
                                i4 = i;
                                i = i7;
                                break;
                            } else {
                                return PEEKED_NONE;
                            }
                        }
                        j = (long) (-(c - 48));
                        i = PEEKED_END_OBJECT;
                        i4 = i3;
                        obj2 = obj;
                        continue;
                    } else if (isLiteral(c)) {
                        return PEEKED_NONE;
                    }
                    break;
            }
            if (i4 != PEEKED_END_OBJECT) {
            }
            if (i4 == PEEKED_END_OBJECT) {
            }
            this.peekedNumberLength = i5;
            this.peeked = PEEKED_NUMBER;
            return PEEKED_NUMBER;
            i5 += PEEKED_BEGIN_OBJECT;
            obj = obj2;
            i3 = i4;
            i4 = i;
        }
    }

    private boolean isLiteral(char c) {
        switch (c) {
            case PEEKED_DOUBLE_QUOTED /*9*/:
            case PEEKED_UNQUOTED /*10*/:
            case PEEKED_SINGLE_QUOTED_NAME /*12*/:
            case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
            case X509KeyUsage.keyEncipherment /*32*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
            case CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA /*58*/:
            case EACTags.NAME /*91*/:
            case EACTags.HEADER_LIST /*93*/:
            case EACTags.SECURITY_ENVIRONMENT_TEMPLATE /*123*/:
            case EACTags.SECURE_MESSAGING_TEMPLATE /*125*/:
                break;
            case ExtensionType.session_ticket /*35*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case EACTags.TAG_LIST /*92*/:
                checkLenient();
                break;
            default:
                return true;
        }
        return false;
    }

    public String nextName() {
        String nextUnquotedValue;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_UNQUOTED_NAME) {
            nextUnquotedValue = nextUnquotedValue();
        } else if (i == PEEKED_SINGLE_QUOTED_NAME) {
            nextUnquotedValue = nextQuotedValue('\'');
        } else if (i == PEEKED_DOUBLE_QUOTED_NAME) {
            nextUnquotedValue = nextQuotedValue('\"');
        } else {
            throw new IllegalStateException("Expected a name but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
        }
        this.peeked = PEEKED_NONE;
        this.pathNames[this.stackSize - 1] = nextUnquotedValue;
        return nextUnquotedValue;
    }

    public String nextString() {
        String nextUnquotedValue;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_UNQUOTED) {
            nextUnquotedValue = nextUnquotedValue();
        } else if (i == PEEKED_SINGLE_QUOTED) {
            nextUnquotedValue = nextQuotedValue('\'');
        } else if (i == PEEKED_DOUBLE_QUOTED) {
            nextUnquotedValue = nextQuotedValue('\"');
        } else if (i == PEEKED_BUFFERED) {
            nextUnquotedValue = this.peekedString;
            this.peekedString = null;
        } else if (i == PEEKED_LONG) {
            nextUnquotedValue = Long.toString(this.peekedLong);
        } else if (i == PEEKED_NUMBER) {
            nextUnquotedValue = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            throw new IllegalStateException("Expected a string but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
        }
        this.peeked = PEEKED_NONE;
        int[] iArr = this.pathIndices;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
        return nextUnquotedValue;
    }

    public boolean nextBoolean() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_TRUE) {
            this.peeked = PEEKED_NONE;
            int[] iArr = this.pathIndices;
            i = this.stackSize - 1;
            iArr[i] = iArr[i] + PEEKED_BEGIN_OBJECT;
            return true;
        } else if (i == PEEKED_FALSE) {
            this.peeked = PEEKED_NONE;
            int[] iArr2 = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr2[i2] = iArr2[i2] + PEEKED_BEGIN_OBJECT;
            return false;
        } else {
            throw new IllegalStateException("Expected a boolean but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
        }
    }

    public void nextNull() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_NULL) {
            this.peeked = PEEKED_NONE;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
            return;
        }
        throw new IllegalStateException("Expected null but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    public double nextDouble() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_LONG) {
            this.peeked = PEEKED_NONE;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
            return (double) this.peekedLong;
        }
        if (i == PEEKED_NUMBER) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (i == PEEKED_SINGLE_QUOTED || i == PEEKED_DOUBLE_QUOTED) {
            this.peekedString = nextQuotedValue(i == PEEKED_SINGLE_QUOTED ? '\'' : '\"');
        } else if (i == PEEKED_UNQUOTED) {
            this.peekedString = nextUnquotedValue();
        } else if (i != PEEKED_BUFFERED) {
            throw new IllegalStateException("Expected a double but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
        }
        this.peeked = PEEKED_BUFFERED;
        double parseDouble = Double.parseDouble(this.peekedString);
        if (this.lenient || !(Double.isNaN(parseDouble) || Double.isInfinite(parseDouble))) {
            this.peekedString = null;
            this.peeked = PEEKED_NONE;
            int[] iArr2 = this.pathIndices;
            int i3 = this.stackSize - 1;
            iArr2[i3] = iArr2[i3] + PEEKED_BEGIN_OBJECT;
            return parseDouble;
        }
        throw new MalformedJsonException("JSON forbids NaN and infinities: " + parseDouble + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    public long nextLong() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == PEEKED_LONG) {
            this.peeked = PEEKED_NONE;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
            return this.peekedLong;
        }
        long parseLong;
        if (i == PEEKED_NUMBER) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (i == PEEKED_SINGLE_QUOTED || i == PEEKED_DOUBLE_QUOTED) {
            this.peekedString = nextQuotedValue(i == PEEKED_SINGLE_QUOTED ? '\'' : '\"');
            try {
                parseLong = Long.parseLong(this.peekedString);
                this.peeked = PEEKED_NONE;
                int[] iArr2 = this.pathIndices;
                int i3 = this.stackSize - 1;
                iArr2[i3] = iArr2[i3] + PEEKED_BEGIN_OBJECT;
                return parseLong;
            } catch (NumberFormatException e) {
            }
        } else {
            throw new IllegalStateException("Expected a long but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
        }
        this.peeked = PEEKED_BUFFERED;
        double parseDouble = Double.parseDouble(this.peekedString);
        parseLong = (long) parseDouble;
        if (((double) parseLong) != parseDouble) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
        }
        this.peekedString = null;
        this.peeked = PEEKED_NONE;
        iArr2 = this.pathIndices;
        i3 = this.stackSize - 1;
        iArr2[i3] = iArr2[i3] + PEEKED_BEGIN_OBJECT;
        return parseLong;
    }

    private String nextQuotedValue(char c) {
        char[] cArr = this.buffer;
        StringBuilder stringBuilder = new StringBuilder();
        do {
            int i = this.pos;
            int i2 = this.limit;
            int i3 = i;
            while (i3 < i2) {
                int i4 = i3 + PEEKED_BEGIN_OBJECT;
                char c2 = cArr[i3];
                if (c2 == c) {
                    this.pos = i4;
                    stringBuilder.append(cArr, i, (i4 - i) - 1);
                    return stringBuilder.toString();
                }
                if (c2 == '\\') {
                    this.pos = i4;
                    stringBuilder.append(cArr, i, (i4 - i) - 1);
                    stringBuilder.append(readEscapeCharacter());
                    i = this.pos;
                    i2 = this.limit;
                    i4 = i;
                } else if (c2 == '\n') {
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = i4;
                }
                i3 = i4;
            }
            stringBuilder.append(cArr, i, i3 - i);
            this.pos = i3;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
        throw syntaxError("Unterminated string");
    }

    private String nextUnquotedValue() {
        StringBuilder stringBuilder = null;
        int i = PEEKED_NONE;
        while (true) {
            String str;
            if (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case PEEKED_DOUBLE_QUOTED /*9*/:
                    case PEEKED_UNQUOTED /*10*/:
                    case PEEKED_SINGLE_QUOTED_NAME /*12*/:
                    case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
                    case X509KeyUsage.keyEncipherment /*32*/:
                    case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                    case CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA /*58*/:
                    case EACTags.NAME /*91*/:
                    case EACTags.HEADER_LIST /*93*/:
                    case EACTags.SECURITY_ENVIRONMENT_TEMPLATE /*123*/:
                    case EACTags.SECURE_MESSAGING_TEMPLATE /*125*/:
                        break;
                    case ExtensionType.session_ticket /*35*/:
                    case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                    case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
                    case EACTags.TAG_LIST /*92*/:
                        checkLenient();
                        break;
                    default:
                        i += PEEKED_BEGIN_OBJECT;
                        continue;
                }
            } else if (i >= this.buffer.length) {
                if (stringBuilder == null) {
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append(this.buffer, this.pos, i);
                this.pos = i + this.pos;
                if (fillBuffer(PEEKED_BEGIN_OBJECT)) {
                    i = PEEKED_NONE;
                } else {
                    i = PEEKED_NONE;
                }
            } else if (fillBuffer(i + PEEKED_BEGIN_OBJECT)) {
            }
            if (stringBuilder == null) {
                str = new String(this.buffer, this.pos, i);
            } else {
                stringBuilder.append(this.buffer, this.pos, i);
                str = stringBuilder.toString();
            }
            this.pos = i + this.pos;
            return str;
        }
    }

    private void skipQuotedValue(char c) {
        char[] cArr = this.buffer;
        do {
            int i = this.pos;
            int i2 = this.limit;
            int i3 = i;
            while (i3 < i2) {
                i = i3 + PEEKED_BEGIN_OBJECT;
                char c2 = cArr[i3];
                if (c2 == c) {
                    this.pos = i;
                    return;
                }
                if (c2 == '\\') {
                    this.pos = i;
                    readEscapeCharacter();
                    i = this.pos;
                    i2 = this.limit;
                } else if (c2 == '\n') {
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = i;
                }
                i3 = i;
            }
            this.pos = i3;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
        throw syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() {
        do {
            int i = PEEKED_NONE;
            while (this.pos + i < this.limit) {
                switch (this.buffer[this.pos + i]) {
                    case PEEKED_DOUBLE_QUOTED /*9*/:
                    case PEEKED_UNQUOTED /*10*/:
                    case PEEKED_SINGLE_QUOTED_NAME /*12*/:
                    case PEEKED_DOUBLE_QUOTED_NAME /*13*/:
                    case X509KeyUsage.keyEncipherment /*32*/:
                    case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                    case CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA /*58*/:
                    case EACTags.NAME /*91*/:
                    case EACTags.HEADER_LIST /*93*/:
                    case EACTags.SECURITY_ENVIRONMENT_TEMPLATE /*123*/:
                    case EACTags.SECURE_MESSAGING_TEMPLATE /*125*/:
                        break;
                    case ExtensionType.session_ticket /*35*/:
                    case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                    case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
                    case EACTags.TAG_LIST /*92*/:
                        checkLenient();
                        break;
                    default:
                        i += PEEKED_BEGIN_OBJECT;
                }
                this.pos = i + this.pos;
                return;
            }
            this.pos = i + this.pos;
        } while (fillBuffer(PEEKED_BEGIN_OBJECT));
    }

    public int nextInt() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        int[] iArr;
        int i2;
        if (i == PEEKED_LONG) {
            i = (int) this.peekedLong;
            if (this.peekedLong != ((long) i)) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
            }
            this.peeked = PEEKED_NONE;
            iArr = this.pathIndices;
            i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
        } else {
            if (i == PEEKED_NUMBER) {
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            } else if (i == PEEKED_SINGLE_QUOTED || i == PEEKED_DOUBLE_QUOTED) {
                this.peekedString = nextQuotedValue(i == PEEKED_SINGLE_QUOTED ? '\'' : '\"');
                try {
                    i = Integer.parseInt(this.peekedString);
                    this.peeked = PEEKED_NONE;
                    iArr = this.pathIndices;
                    i2 = this.stackSize - 1;
                    iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
                } catch (NumberFormatException e) {
                }
            } else {
                throw new IllegalStateException("Expected an int but was " + peek() + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
            }
            this.peeked = PEEKED_BUFFERED;
            double parseDouble = Double.parseDouble(this.peekedString);
            i = (int) parseDouble;
            if (((double) i) != parseDouble) {
                throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
            }
            this.peekedString = null;
            this.peeked = PEEKED_NONE;
            iArr = this.pathIndices;
            i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + PEEKED_BEGIN_OBJECT;
        }
        return i;
    }

    public void close() {
        this.peeked = PEEKED_NONE;
        this.stack[PEEKED_NONE] = PEEKED_SINGLE_QUOTED;
        this.stackSize = PEEKED_BEGIN_OBJECT;
        this.in.close();
    }

    public void skipValue() {
        int i = PEEKED_NONE;
        do {
            int i2 = this.peeked;
            if (i2 == 0) {
                i2 = doPeek();
            }
            if (i2 == PEEKED_BEGIN_ARRAY) {
                push(PEEKED_BEGIN_OBJECT);
                i += PEEKED_BEGIN_OBJECT;
            } else if (i2 == PEEKED_BEGIN_OBJECT) {
                push(PEEKED_BEGIN_ARRAY);
                i += PEEKED_BEGIN_OBJECT;
            } else if (i2 == PEEKED_END_ARRAY) {
                this.stackSize--;
                i--;
            } else if (i2 == PEEKED_END_OBJECT) {
                this.stackSize--;
                i--;
            } else if (i2 == PEEKED_UNQUOTED_NAME || i2 == PEEKED_UNQUOTED) {
                skipUnquotedValue();
            } else if (i2 == PEEKED_SINGLE_QUOTED || i2 == PEEKED_SINGLE_QUOTED_NAME) {
                skipQuotedValue('\'');
            } else if (i2 == PEEKED_DOUBLE_QUOTED || i2 == PEEKED_DOUBLE_QUOTED_NAME) {
                skipQuotedValue('\"');
            } else if (i2 == PEEKED_NUMBER) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = PEEKED_NONE;
        } while (i != 0);
        int[] iArr = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr[i3] = iArr[i3] + PEEKED_BEGIN_OBJECT;
        this.pathNames[this.stackSize - 1] = "null";
    }

    private void push(int i) {
        if (this.stackSize == this.stack.length) {
            Object obj = new int[(this.stackSize * PEEKED_END_OBJECT)];
            Object obj2 = new int[(this.stackSize * PEEKED_END_OBJECT)];
            Object obj3 = new String[(this.stackSize * PEEKED_END_OBJECT)];
            System.arraycopy(this.stack, PEEKED_NONE, obj, PEEKED_NONE, this.stackSize);
            System.arraycopy(this.pathIndices, PEEKED_NONE, obj2, PEEKED_NONE, this.stackSize);
            System.arraycopy(this.pathNames, PEEKED_NONE, obj3, PEEKED_NONE, this.stackSize);
            this.stack = obj;
            this.pathIndices = obj2;
            this.pathNames = obj3;
        }
        int[] iArr = this.stack;
        int i2 = this.stackSize;
        this.stackSize = i2 + PEEKED_BEGIN_OBJECT;
        iArr[i2] = i;
    }

    private boolean fillBuffer(int i) {
        Object obj = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(obj, this.pos, obj, PEEKED_NONE, this.limit);
        } else {
            this.limit = PEEKED_NONE;
        }
        this.pos = PEEKED_NONE;
        do {
            int read = this.in.read(obj, this.limit, obj.length - this.limit);
            if (read == -1) {
                return false;
            }
            this.limit = read + this.limit;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && obj[PEEKED_NONE] == '\ufeff') {
                this.pos += PEEKED_BEGIN_OBJECT;
                this.lineStart += PEEKED_BEGIN_OBJECT;
                i += PEEKED_BEGIN_OBJECT;
            }
        } while (this.limit < i);
        return true;
    }

    private int getLineNumber() {
        return this.lineNumber + PEEKED_BEGIN_OBJECT;
    }

    private int getColumnNumber() {
        return (this.pos - this.lineStart) + PEEKED_BEGIN_OBJECT;
    }

    private int nextNonWhitespace(boolean z) {
        char[] cArr = this.buffer;
        int i = this.pos;
        int i2 = this.limit;
        while (true) {
            if (i == i2) {
                this.pos = i;
                if (fillBuffer(PEEKED_BEGIN_OBJECT)) {
                    i = this.pos;
                    i2 = this.limit;
                } else if (!z) {
                    return -1;
                } else {
                    throw new EOFException("End of input at line " + getLineNumber() + " column " + getColumnNumber());
                }
            }
            int i3 = i + PEEKED_BEGIN_OBJECT;
            char c = cArr[i];
            if (c == '\n') {
                this.lineNumber += PEEKED_BEGIN_OBJECT;
                this.lineStart = i3;
                i = i3;
            } else if (c == ' ' || c == '\r') {
                i = i3;
            } else if (c == '\t') {
                i = i3;
            } else if (c == '/') {
                this.pos = i3;
                if (i3 == i2) {
                    this.pos--;
                    boolean fillBuffer = fillBuffer(PEEKED_END_OBJECT);
                    this.pos += PEEKED_BEGIN_OBJECT;
                    if (!fillBuffer) {
                        return c;
                    }
                }
                checkLenient();
                switch (cArr[this.pos]) {
                    case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                        this.pos += PEEKED_BEGIN_OBJECT;
                        if (skipTo("*/")) {
                            i = this.pos + PEEKED_END_OBJECT;
                            i2 = this.limit;
                            break;
                        }
                        throw syntaxError("Unterminated comment");
                    case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                        this.pos += PEEKED_BEGIN_OBJECT;
                        skipToEndOfLine();
                        i = this.pos;
                        i2 = this.limit;
                        break;
                    default:
                        return c;
                }
            } else if (c == '#') {
                this.pos = i3;
                checkLenient();
                skipToEndOfLine();
                i = this.pos;
                i2 = this.limit;
            } else {
                this.pos = i3;
                return c;
            }
        }
    }

    private void checkLenient() {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() {
        char c;
        do {
            if (this.pos < this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + PEEKED_BEGIN_OBJECT;
                c = cArr[i];
                if (c == '\n') {
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = this.pos;
                    return;
                }
            } else {
                return;
            }
        } while (c != '\r');
    }

    private boolean skipTo(String str) {
        while (true) {
            if (this.pos + str.length() > this.limit && !fillBuffer(str.length())) {
                return false;
            }
            if (this.buffer[this.pos] == '\n') {
                this.lineNumber += PEEKED_BEGIN_OBJECT;
                this.lineStart = this.pos + PEEKED_BEGIN_OBJECT;
            } else {
                int i = PEEKED_NONE;
                while (i < str.length()) {
                    if (this.buffer[this.pos + i] == str.charAt(i)) {
                        i += PEEKED_BEGIN_OBJECT;
                    }
                }
                return true;
            }
            this.pos += PEEKED_BEGIN_OBJECT;
        }
    }

    public String toString() {
        return getClass().getSimpleName() + " at line " + getLineNumber() + " column " + getColumnNumber();
    }

    public String getPath() {
        StringBuilder append = new StringBuilder().append('$');
        int i = this.stackSize;
        for (int i2 = PEEKED_NONE; i2 < i; i2 += PEEKED_BEGIN_OBJECT) {
            switch (this.stack[i2]) {
                case PEEKED_BEGIN_OBJECT /*1*/:
                case PEEKED_END_OBJECT /*2*/:
                    append.append('[').append(this.pathIndices[i2]).append(']');
                    break;
                case PEEKED_BEGIN_ARRAY /*3*/:
                case PEEKED_END_ARRAY /*4*/:
                case PEEKED_TRUE /*5*/:
                    append.append('.');
                    if (this.pathNames[i2] == null) {
                        break;
                    }
                    append.append(this.pathNames[i2]);
                    break;
                default:
                    break;
            }
        }
        return append.toString();
    }

    private char readEscapeCharacter() {
        if (this.pos != this.limit || fillBuffer(PEEKED_BEGIN_OBJECT)) {
            char[] cArr = this.buffer;
            int i = this.pos;
            this.pos = i + PEEKED_BEGIN_OBJECT;
            char c = cArr[i];
            switch (c) {
                case PEEKED_UNQUOTED /*10*/:
                    this.lineNumber += PEEKED_BEGIN_OBJECT;
                    this.lineStart = this.pos;
                    return c;
                case EACTags.FCP_TEMPLATE /*98*/:
                    return '\b';
                case EncryptionAlgorithm.AEAD_CHACHA20_POLY1305 /*102*/:
                    return '\f';
                case EACTags.APPLICATION_RELATED_DATA /*110*/:
                    return '\n';
                case 'r':
                    return '\r';
                case 't':
                    return '\t';
                case 'u':
                    if (this.pos + PEEKED_END_ARRAY <= this.limit || fillBuffer(PEEKED_END_ARRAY)) {
                        int i2 = this.pos;
                        int i3 = i2 + PEEKED_END_ARRAY;
                        int i4 = i2;
                        c = '\u0000';
                        for (i = i4; i < i3; i += PEEKED_BEGIN_OBJECT) {
                            char c2 = this.buffer[i];
                            c = (char) (c << PEEKED_END_ARRAY);
                            if (c2 >= LLVARUtil.EMPTY_STRING && c2 <= '9') {
                                c = (char) (c + (c2 - 48));
                            } else if (c2 >= 'a' && c2 <= 'f') {
                                c = (char) (c + ((c2 - 97) + PEEKED_UNQUOTED));
                            } else if (c2 < 'A' || c2 > 'F') {
                                throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, PEEKED_END_ARRAY));
                            } else {
                                c = (char) (c + ((c2 - 65) + PEEKED_UNQUOTED));
                            }
                        }
                        this.pos += PEEKED_END_ARRAY;
                        return c;
                    }
                    throw syntaxError("Unterminated escape sequence");
                default:
                    return c;
            }
        }
        throw syntaxError("Unterminated escape sequence");
    }

    private IOException syntaxError(String str) {
        throw new MalformedJsonException(str + " at line " + getLineNumber() + " column " + getColumnNumber() + " path " + getPath());
    }

    private void consumeNonExecutePrefix() {
        nextNonWhitespace(true);
        this.pos--;
        if (this.pos + NON_EXECUTE_PREFIX.length <= this.limit || fillBuffer(NON_EXECUTE_PREFIX.length)) {
            int i = PEEKED_NONE;
            while (i < NON_EXECUTE_PREFIX.length) {
                if (this.buffer[this.pos + i] == NON_EXECUTE_PREFIX[i]) {
                    i += PEEKED_BEGIN_OBJECT;
                } else {
                    return;
                }
            }
            this.pos += NON_EXECUTE_PREFIX.length;
        }
    }
}
