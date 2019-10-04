/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.crypto.tls.ServerName;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;

public class ServerNameList {
    protected Vector serverNameList;

    public ServerNameList(Vector vector) {
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("'serverNameList' must not be null or empty");
        }
        this.serverNameList = vector;
    }

    public static ServerNameList parse(InputStream inputStream) {
        int n2 = TlsUtils.readUint16(inputStream);
        if (n2 < 1) {
            throw new TlsFatalAlert(50);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(n2, inputStream));
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement((Object)ServerName.parse((InputStream)byteArrayInputStream));
        }
        return new ServerNameList(vector);
    }

    public void encode(OutputStream outputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 < this.serverNameList.size(); ++i2) {
            ((ServerName)this.serverNameList.elementAt(i2)).encode((OutputStream)byteArrayOutputStream);
        }
        TlsUtils.checkUint16(byteArrayOutputStream.size());
        TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
        byteArrayOutputStream.writeTo(outputStream);
    }

    public Vector getServerNameList() {
        return this.serverNameList;
    }
}

