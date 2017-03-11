package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class ServerNameList {
    protected Vector serverNameList;

    public ServerNameList(Vector vector) {
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("'serverNameList' must not be null or empty");
        }
        this.serverNameList = vector;
    }

    public static ServerNameList parse(InputStream inputStream) {
        int readUint16 = TlsUtils.readUint16(inputStream);
        if (readUint16 < 1) {
            throw new TlsFatalAlert((short) 50);
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(readUint16, inputStream));
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement(ServerName.parse(byteArrayInputStream));
        }
        return new ServerNameList(vector);
    }

    public void encode(OutputStream outputStream) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < this.serverNameList.size(); i++) {
            ((ServerName) this.serverNameList.elementAt(i)).encode(byteArrayOutputStream);
        }
        TlsUtils.checkUint16(byteArrayOutputStream.size());
        TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
        byteArrayOutputStream.writeTo(outputStream);
    }

    public Vector getServerNameList() {
        return this.serverNameList;
    }
}
