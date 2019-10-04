/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Reader
 *  java.io.Writer
 *  java.lang.Boolean
 *  java.lang.Double
 *  java.lang.Float
 *  java.lang.Integer
 *  java.lang.Number
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.americanexpress.mobilepayments.hceclient.utils.json;

import com.americanexpress.mobilepayments.hceclient.utils.json.ContainerFactory;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONArray;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONObject;
import com.americanexpress.mobilepayments.hceclient.utils.json.Yylex;
import com.americanexpress.mobilepayments.hceclient.utils.json.Yytoken;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONHelper {
    public static final int S_INIT = 0;
    public static final int S_IN_ARRAY = 3;
    public static final int S_IN_ERROR = -1;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_PASSED_PAIR_KEY = 4;
    private LinkedList handlerStatusStack;
    private Yylex lexer = new Yylex((Reader)null);
    private int status = 0;
    private Yytoken token = null;

    /*
     * Enabled aggressive block sorting
     */
    private List createArrayContainer(ContainerFactory containerFactory) {
        void var2_3;
        if (containerFactory == null) {
            JSONArray jSONArray = new JSONArray();
            return var2_3;
        } else {
            List list = containerFactory.creatArrayContainer();
            if (list != null) return var2_3;
            return new JSONArray();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Map createObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONObject();
        }
        Map map = containerFactory.createObjectContainer();
        if (map != null) return map;
        return new JSONObject();
    }

    public static String escape(String string) {
        if (string == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        JSONHelper.escape(string, stringBuffer);
        return stringBuffer.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    static void escape(String string, StringBuffer stringBuffer) {
        int n2 = string.length();
        int n3 = 0;
        do {
            block15 : {
                char c2;
                block14 : {
                    String string2;
                    if (n3 >= n2) {
                        return;
                    }
                    c2 = string.charAt(n3);
                    switch (c2) {
                        default: {
                            if (c2 >= '\u0000' && c2 <= '\u001f' || c2 >= '' && c2 <= '\u009f' || c2 >= '\u2000' && c2 <= '\u20ff') {
                                string2 = Integer.toHexString((int)c2);
                                stringBuffer.append("\\u");
                                for (int i2 = 0; i2 < 4 - string2.length(); ++i2) {
                                    stringBuffer.append('0');
                                }
                                break;
                            }
                            break block14;
                        }
                        case '\"': {
                            stringBuffer.append("\\\"");
                            break block15;
                        }
                        case '\\': {
                            stringBuffer.append("\\\\");
                            break block15;
                        }
                        case '\b': {
                            stringBuffer.append("\\b");
                            break block15;
                        }
                        case '\f': {
                            stringBuffer.append("\\f");
                            break block15;
                        }
                        case '\n': {
                            stringBuffer.append("\\n");
                            break block15;
                        }
                        case '\r': {
                            stringBuffer.append("\\r");
                            break block15;
                        }
                        case '\t': {
                            stringBuffer.append("\\t");
                            break block15;
                        }
                        case '/': {
                            stringBuffer.append("\\/");
                            break block15;
                        }
                    }
                    stringBuffer.append(string2.toUpperCase());
                    break block15;
                }
                stringBuffer.append(c2);
            }
            ++n3;
        } while (true);
    }

    private void nextToken() {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new Yytoken(-1, null);
        }
    }

    private int peekStatus(LinkedList linkedList) {
        if (linkedList.size() == 0) {
            return -1;
        }
        return (Integer)linkedList.getFirst();
    }

    public static void writeJSONString(Object object, Writer writer) {
        if (object == null) {
            writer.write("null");
            return;
        }
        if (object instanceof String) {
            writer.write(34);
            writer.write(JSONHelper.escape((String)object));
            writer.write(34);
            return;
        }
        if (object instanceof Double) {
            if (((Double)object).isInfinite() || ((Double)object).isNaN()) {
                writer.write("null");
                return;
            }
            writer.write(object.toString());
            return;
        }
        if (object instanceof Float) {
            if (((Float)object).isInfinite() || ((Float)object).isNaN()) {
                writer.write("null");
                return;
            }
            writer.write(object.toString());
            return;
        }
        if (object instanceof Number) {
            writer.write(object.toString());
            return;
        }
        if (object instanceof Boolean) {
            writer.write(object.toString());
            return;
        }
        if (object instanceof Map) {
            JSONHelper.writeJSONString((Map)object, writer);
            return;
        }
        if (object instanceof Collection) {
            JSONHelper.writeJSONString((Object)((Collection)object), writer);
            return;
        }
        if (object instanceof byte[]) {
            JSONHelper.writeJSONString((byte[])object, writer);
            return;
        }
        if (object instanceof short[]) {
            JSONHelper.writeJSONString((short[])object, writer);
            return;
        }
        if (object instanceof int[]) {
            JSONHelper.writeJSONString((int[])object, writer);
            return;
        }
        if (object instanceof long[]) {
            JSONHelper.writeJSONString((long[])object, writer);
            return;
        }
        if (object instanceof float[]) {
            JSONHelper.writeJSONString((float[])object, writer);
            return;
        }
        if (object instanceof double[]) {
            JSONHelper.writeJSONString((double[])object, writer);
            return;
        }
        if (object instanceof boolean[]) {
            JSONHelper.writeJSONString((boolean[])object, writer);
            return;
        }
        if (object instanceof char[]) {
            JSONHelper.writeJSONString((char[])object, writer);
            return;
        }
        if (object instanceof Object[]) {
            JSONHelper.writeJSONString((Object[])object, writer);
            return;
        }
        if (object instanceof TagValue) {
            writer.write(34);
            writer.write(JSONHelper.escape(object.toString()));
            writer.write(34);
            return;
        }
        writer.write(object.toString());
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void writeJSONString(Map map, Writer writer) {
        if (map == null) {
            writer.write("null");
            return;
        }
        boolean bl = true;
        Iterator iterator = map.entrySet().iterator();
        writer.write(123);
        do {
            boolean bl2;
            if (!iterator.hasNext()) {
                writer.write(125);
                return;
            }
            if (bl) {
                bl2 = false;
            } else {
                writer.write(44);
                bl2 = bl;
            }
            Map.Entry entry = (Map.Entry)iterator.next();
            writer.write(34);
            writer.write(JSONHelper.escape(String.valueOf((Object)entry.getKey())));
            writer.write(34);
            writer.write(58);
            JSONHelper.writeJSONString(entry.getValue(), writer);
            bl = bl2;
        } while (true);
    }

    public static void writeJSONString(boolean[] arrbl, Writer writer) {
        if (arrbl == null) {
            writer.write("null");
            return;
        }
        if (arrbl.length == 0) {
            writer.write("[]");
            return;
        }
        writer.write("[");
        writer.write(String.valueOf((boolean)arrbl[0]));
        for (int i2 = 1; i2 < arrbl.length; ++i2) {
            writer.write(",");
            writer.write(String.valueOf((boolean)arrbl[i2]));
        }
        writer.write("]");
    }

    public int getPosition() {
        return this.lexer.getPosition();
    }

    /*
     * Exception decompiling
     */
    public Object parse(Reader var1_1, ContainerFactory var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: First case is not immediately after switch.
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:358)
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:328)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:462)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public void reset() {
        this.token = null;
        this.status = 0;
        this.handlerStatusStack = null;
    }

    public void reset(Reader reader) {
        this.lexer.yyreset(reader);
        this.reset();
    }
}

