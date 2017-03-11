package org.bouncycastle.util.io.pem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.util.encoders.Base64;

public class PemReader extends BufferedReader {
    private static final String BEGIN = "-----BEGIN ";
    private static final String END = "-----END ";

    public PemReader(Reader reader) {
        super(reader);
    }

    private PemObject loadObject(String str) {
        String str2 = END + str;
        StringBuffer stringBuffer = new StringBuffer();
        List arrayList = new ArrayList();
        while (true) {
            String readLine = readLine();
            if (readLine == null) {
                break;
            } else if (readLine.indexOf(":") >= 0) {
                int indexOf = readLine.indexOf(58);
                arrayList.add(new PemHeader(readLine.substring(0, indexOf), readLine.substring(indexOf + 1).trim()));
            } else if (readLine.indexOf(str2) != -1) {
                break;
            } else {
                stringBuffer.append(readLine.trim());
            }
        }
        if (readLine != null) {
            return new PemObject(str, arrayList, Base64.decode(stringBuffer.toString()));
        }
        throw new IOException(str2 + " not found");
    }

    public PemObject readPemObject() {
        String readLine = readLine();
        while (readLine != null && !readLine.startsWith(BEGIN)) {
            readLine = readLine();
        }
        if (readLine != null) {
            readLine = readLine.substring(BEGIN.length());
            int indexOf = readLine.indexOf(45);
            readLine = readLine.substring(0, indexOf);
            if (indexOf > 0) {
                return loadObject(readLine);
            }
        }
        return null;
    }
}
