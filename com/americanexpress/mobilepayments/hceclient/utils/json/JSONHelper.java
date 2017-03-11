package com.americanexpress.mobilepayments.hceclient.utils.json;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.google.android.gms.location.places.Place;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;

public class JSONHelper {
    public static final int S_INIT = 0;
    public static final int S_IN_ARRAY = 3;
    public static final int S_IN_ERROR = -1;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_PASSED_PAIR_KEY = 4;
    private LinkedList handlerStatusStack;
    private Yylex lexer;
    private int status;
    private Yytoken token;

    public JSONHelper() {
        this.lexer = new Yylex((Reader) null);
        this.token = null;
        this.status = S_INIT;
    }

    public static void writeJSONString(Map map, Writer writer) {
        if (map == null) {
            writer.write("null");
            return;
        }
        Object obj = S_IN_FINISHED_VALUE;
        writer.write(EACTags.SECURITY_ENVIRONMENT_TEMPLATE);
        for (Entry entry : map.entrySet()) {
            Object obj2;
            if (obj != null) {
                obj2 = null;
            } else {
                writer.write(44);
                obj2 = obj;
            }
            writer.write(34);
            writer.write(escape(String.valueOf(entry.getKey())));
            writer.write(34);
            writer.write(58);
            writeJSONString(entry.getValue(), writer);
            obj = obj2;
        }
        writer.write(EACTags.SECURE_MESSAGING_TEMPLATE);
    }

    public static void writeJSONString(Object obj, Writer writer) {
        if (obj == null) {
            writer.write("null");
        } else if (obj instanceof String) {
            writer.write(34);
            writer.write(escape((String) obj));
            writer.write(34);
        } else if (obj instanceof Double) {
            if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
                writer.write("null");
            } else {
                writer.write(obj.toString());
            }
        } else if (obj instanceof Float) {
            if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
                writer.write("null");
            } else {
                writer.write(obj.toString());
            }
        } else if (obj instanceof Number) {
            writer.write(obj.toString());
        } else if (obj instanceof Boolean) {
            writer.write(obj.toString());
        } else if (obj instanceof Map) {
            writeJSONString((Map) obj, writer);
        } else if (obj instanceof Collection) {
            writeJSONString((Collection) obj, writer);
        } else if (obj instanceof byte[]) {
            writeJSONString((Object) (byte[]) obj, writer);
        } else if (obj instanceof short[]) {
            writeJSONString((Object) (short[]) obj, writer);
        } else if (obj instanceof int[]) {
            writeJSONString((Object) (int[]) obj, writer);
        } else if (obj instanceof long[]) {
            writeJSONString((Object) (long[]) obj, writer);
        } else if (obj instanceof float[]) {
            writeJSONString((Object) (float[]) obj, writer);
        } else if (obj instanceof double[]) {
            writeJSONString((Object) (double[]) obj, writer);
        } else if (obj instanceof boolean[]) {
            writeJSONString((boolean[]) obj, writer);
        } else if (obj instanceof char[]) {
            writeJSONString((Object) (char[]) obj, writer);
        } else if (obj instanceof Object[]) {
            writeJSONString((Object) (Object[]) obj, writer);
        } else if (obj instanceof TagValue) {
            writer.write(34);
            writer.write(escape(obj.toString()));
            writer.write(34);
        } else {
            writer.write(obj.toString());
        }
    }

    public static void writeJSONString(boolean[] zArr, Writer writer) {
        if (zArr == null) {
            writer.write("null");
        } else if (zArr.length == 0) {
            writer.write("[]");
        } else {
            writer.write("[");
            writer.write(String.valueOf(zArr[S_INIT]));
            for (int i = S_IN_FINISHED_VALUE; i < zArr.length; i += S_IN_FINISHED_VALUE) {
                writer.write(",");
                writer.write(String.valueOf(zArr[i]));
            }
            writer.write("]");
        }
    }

    public static String escape(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        escape(str, stringBuffer);
        return stringBuffer.toString();
    }

    static void escape(String str, StringBuffer stringBuffer) {
        int length = str.length();
        for (int i = S_INIT; i < length; i += S_IN_FINISHED_VALUE) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case X509KeyUsage.keyAgreement /*8*/:
                    stringBuffer.append("\\b");
                    break;
                case NamedCurve.sect283k1 /*9*/:
                    stringBuffer.append("\\t");
                    break;
                case NamedCurve.sect283r1 /*10*/:
                    stringBuffer.append("\\n");
                    break;
                case CertStatus.UNDETERMINED /*12*/:
                    stringBuffer.append("\\f");
                    break;
                case NamedCurve.sect571k1 /*13*/:
                    stringBuffer.append("\\r");
                    break;
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    stringBuffer.append("\\\"");
                    break;
                case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    stringBuffer.append("\\/");
                    break;
                case EACTags.TAG_LIST /*92*/:
                    stringBuffer.append("\\\\");
                    break;
                default:
                    if ((charAt >= '\u0000' && charAt <= '\u001f') || ((charAt >= '\u007f' && charAt <= '\u009f') || (charAt >= '\u2000' && charAt <= '\u20ff'))) {
                        String toHexString = Integer.toHexString(charAt);
                        stringBuffer.append("\\u");
                        for (int i2 = S_INIT; i2 < 4 - toHexString.length(); i2 += S_IN_FINISHED_VALUE) {
                            stringBuffer.append(LLVARUtil.EMPTY_STRING);
                        }
                        stringBuffer.append(toHexString.toUpperCase());
                        break;
                    }
                    stringBuffer.append(charAt);
                    break;
            }
        }
    }

    public Object parse(Reader reader, ContainerFactory containerFactory) {
        reset(reader);
        LinkedList linkedList = new LinkedList();
        LinkedList linkedList2 = new LinkedList();
        do {
            nextToken();
            Map createObjectContainer;
            switch (this.status) {
                case S_IN_ERROR /*-1*/:
                    throw new ParseException(getPosition(), S_IN_FINISHED_VALUE, this.token);
                case S_INIT /*0*/:
                    try {
                        switch (this.token.type) {
                            case S_INIT /*0*/:
                                this.status = S_IN_FINISHED_VALUE;
                                linkedList.addFirst(Integer.valueOf(this.status));
                                linkedList2.addFirst(this.token.value);
                                break;
                            case S_IN_FINISHED_VALUE /*1*/:
                                this.status = S_IN_OBJECT;
                                linkedList.addFirst(Integer.valueOf(this.status));
                                linkedList2.addFirst(createObjectContainer(containerFactory));
                                break;
                            case S_IN_ARRAY /*3*/:
                                this.status = S_IN_ARRAY;
                                linkedList.addFirst(Integer.valueOf(this.status));
                                linkedList2.addFirst(createArrayContainer(containerFactory));
                                break;
                            default:
                                this.status = S_IN_ERROR;
                                break;
                        }
                    } catch (IOException e) {
                        throw e;
                    }
                case S_IN_FINISHED_VALUE /*1*/:
                    if (this.token.type == S_IN_ERROR) {
                        return linkedList2.removeFirst();
                    }
                    throw new ParseException(getPosition(), S_IN_FINISHED_VALUE, this.token);
                case S_IN_OBJECT /*2*/:
                    switch (this.token.type) {
                        case S_INIT /*0*/:
                            if (!(this.token.value instanceof String)) {
                                this.status = S_IN_ERROR;
                                break;
                            }
                            linkedList2.addFirst((String) this.token.value);
                            this.status = S_PASSED_PAIR_KEY;
                            linkedList.addFirst(Integer.valueOf(this.status));
                            break;
                        case S_IN_OBJECT /*2*/:
                            if (linkedList2.size() <= S_IN_FINISHED_VALUE) {
                                this.status = S_IN_FINISHED_VALUE;
                                break;
                            }
                            linkedList.removeFirst();
                            linkedList2.removeFirst();
                            this.status = peekStatus(linkedList);
                            break;
                        case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                            break;
                        default:
                            this.status = S_IN_ERROR;
                            break;
                    }
                case S_IN_ARRAY /*3*/:
                    List list;
                    switch (this.token.type) {
                        case S_INIT /*0*/:
                            ((List) linkedList2.getFirst()).add(this.token.value);
                            break;
                        case S_IN_FINISHED_VALUE /*1*/:
                            list = (List) linkedList2.getFirst();
                            createObjectContainer = createObjectContainer(containerFactory);
                            list.add(createObjectContainer);
                            this.status = S_IN_OBJECT;
                            linkedList.addFirst(Integer.valueOf(this.status));
                            linkedList2.addFirst(createObjectContainer);
                            break;
                        case S_IN_ARRAY /*3*/:
                            list = (List) linkedList2.getFirst();
                            List createArrayContainer = createArrayContainer(containerFactory);
                            list.add(createArrayContainer);
                            this.status = S_IN_ARRAY;
                            linkedList.addFirst(Integer.valueOf(this.status));
                            linkedList2.addFirst(createArrayContainer);
                            break;
                        case S_PASSED_PAIR_KEY /*4*/:
                            if (linkedList2.size() <= S_IN_FINISHED_VALUE) {
                                this.status = S_IN_FINISHED_VALUE;
                                break;
                            }
                            linkedList.removeFirst();
                            linkedList2.removeFirst();
                            this.status = peekStatus(linkedList);
                            break;
                        case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                            break;
                        default:
                            this.status = S_IN_ERROR;
                            break;
                    }
                case S_PASSED_PAIR_KEY /*4*/:
                    String str;
                    switch (this.token.type) {
                        case S_INIT /*0*/:
                            linkedList.removeFirst();
                            ((Map) linkedList2.getFirst()).put((String) linkedList2.removeFirst(), this.token.value);
                            this.status = peekStatus(linkedList);
                            break;
                        case S_IN_FINISHED_VALUE /*1*/:
                            linkedList.removeFirst();
                            str = (String) linkedList2.removeFirst();
                            createObjectContainer = (Map) linkedList2.getFirst();
                            Map createObjectContainer2 = createObjectContainer(containerFactory);
                            createObjectContainer.put(str, createObjectContainer2);
                            this.status = S_IN_OBJECT;
                            linkedList.addFirst(Integer.valueOf(this.status));
                            linkedList2.addFirst(createObjectContainer2);
                            break;
                        case S_IN_ARRAY /*3*/:
                            linkedList.removeFirst();
                            str = (String) linkedList2.removeFirst();
                            createObjectContainer = (Map) linkedList2.getFirst();
                            List createArrayContainer2 = createArrayContainer(containerFactory);
                            createObjectContainer.put(str, createArrayContainer2);
                            this.status = S_IN_ARRAY;
                            linkedList.addFirst(Integer.valueOf(this.status));
                            linkedList2.addFirst(createArrayContainer2);
                            break;
                        case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                            break;
                        default:
                            this.status = S_IN_ERROR;
                            break;
                    }
            }
            if (this.status == S_IN_ERROR) {
                throw new ParseException(getPosition(), S_IN_FINISHED_VALUE, this.token);
            }
        } while (this.token.type != S_IN_ERROR);
        throw new ParseException(getPosition(), S_IN_FINISHED_VALUE, this.token);
    }

    public int getPosition() {
        return this.lexer.getPosition();
    }

    private List createArrayContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONArray();
        }
        List creatArrayContainer = containerFactory.creatArrayContainer();
        if (creatArrayContainer == null) {
            return new JSONArray();
        }
        return creatArrayContainer;
    }

    private int peekStatus(LinkedList linkedList) {
        if (linkedList.size() == 0) {
            return S_IN_ERROR;
        }
        return ((Integer) linkedList.getFirst()).intValue();
    }

    public void reset() {
        this.token = null;
        this.status = S_INIT;
        this.handlerStatusStack = null;
    }

    public void reset(Reader reader) {
        this.lexer.yyreset(reader);
        reset();
    }

    private void nextToken() {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new Yytoken(S_IN_ERROR, null);
        }
    }

    private Map createObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONObject();
        }
        Map createObjectContainer = containerFactory.createObjectContainer();
        if (createObjectContainer == null) {
            return new JSONObject();
        }
        return createObjectContainer;
    }
}
