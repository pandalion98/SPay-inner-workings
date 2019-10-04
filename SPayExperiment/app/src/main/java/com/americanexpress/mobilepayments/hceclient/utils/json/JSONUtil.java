/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.Reader
 *  java.io.StringReader
 *  java.io.StringWriter
 *  java.io.Writer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.LinkedList
 *  java.util.List
 *  java.util.Map
 *  java.util.TreeMap
 */
package com.americanexpress.mobilepayments.hceclient.utils.json;

import com.americanexpress.mobilepayments.hceclient.utils.json.ContainerFactory;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONHelper;
import com.americanexpress.mobilepayments.hceclient.utils.json.ParseException;
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
    public static String toJSONString(Map map) {
        StringWriter stringWriter = new StringWriter();
        try {
            JSONHelper.writeJSONString(map, (Writer)stringWriter);
            String string = stringWriter.toString();
            return string;
        }
        catch (IOException iOException) {
            throw new RuntimeException((Throwable)iOException);
        }
    }

    public Object parse(String string) {
        StringReader stringReader = new StringReader(string);
        JSONHelper jSONHelper = new JSONHelper();
        try {
            Object object = jSONHelper.parse((Reader)stringReader, new ContainerFactory(){

                @Override
                public List creatArrayContainer() {
                    return new LinkedList();
                }

                @Override
                public Map createObjectContainer() {
                    return new TreeMap();
                }
            });
            return object;
        }
        catch (IOException iOException) {
            throw new ParseException(-1, 2, (Object)iOException);
        }
    }

}

