package com.americanexpress.mobilepayments.hceclient.utils.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JSONUtil {

    /* renamed from: com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil.1 */
    class C00711 implements ContainerFactory {
        C00711() {
        }

        public Map createObjectContainer() {
            return new TreeMap();
        }

        public List creatArrayContainer() {
            return new LinkedList();
        }
    }

    public static String toJSONString(Map map) {
        Writer stringWriter = new StringWriter();
        try {
            JSONHelper.writeJSONString(map, stringWriter);
            return stringWriter.toString();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(String str) {
        Reader stringReader = new StringReader(str);
        try {
            return new JSONHelper().parse(stringReader, new C00711());
        } catch (IOException e) {
            throw new ParseException(-1, 2, e);
        }
    }
}
