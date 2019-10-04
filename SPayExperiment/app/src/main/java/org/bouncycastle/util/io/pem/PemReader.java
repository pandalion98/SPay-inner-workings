/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedReader
 *  java.io.IOException
 *  java.io.Reader
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.List
 */
package org.bouncycastle.util.io.pem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;

public class PemReader
extends BufferedReader {
    private static final String BEGIN = "-----BEGIN ";
    private static final String END = "-----END ";

    public PemReader(Reader reader) {
        super(reader);
    }

    private PemObject loadObject(String string) {
        String string2 = END + string;
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = new ArrayList();
        do {
            String string3;
            block6 : {
                block5 : {
                    if ((string3 = this.readLine()) == null) break block5;
                    if (string3.indexOf(":") >= 0) {
                        int n = string3.indexOf(58);
                        arrayList.add((Object)new PemHeader(string3.substring(0, n), string3.substring(n + 1).trim()));
                        continue;
                    }
                    if (string3.indexOf(string2) == -1) break block6;
                }
                if (string3 != null) break;
                throw new IOException(string2 + " not found");
            }
            stringBuffer.append(string3.trim());
        } while (true);
        return new PemObject(string, (List)arrayList, Base64.decode(stringBuffer.toString()));
    }

    public PemObject readPemObject() {
        String string = this.readLine();
        while (string != null && !string.startsWith(BEGIN)) {
            string = this.readLine();
        }
        if (string != null) {
            String string2 = string.substring(BEGIN.length());
            int n = string2.indexOf(45);
            String string3 = string2.substring(0, n);
            if (n > 0) {
                return this.loadObject(string3);
            }
        }
        return null;
    }
}

