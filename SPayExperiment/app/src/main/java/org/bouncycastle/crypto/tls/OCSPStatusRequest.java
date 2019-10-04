/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
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
import org.bouncycastle.asn1.ocsp.ResponderID;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.crypto.tls.TlsUtils;

public class OCSPStatusRequest {
    protected Extensions requestExtensions;
    protected Vector responderIDList;

    public OCSPStatusRequest(Vector vector, Extensions extensions) {
        this.responderIDList = vector;
        this.requestExtensions = extensions;
    }

    public static OCSPStatusRequest parse(InputStream inputStream) {
        Vector vector = new Vector();
        int n2 = TlsUtils.readUint16(inputStream);
        if (n2 > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(n2, inputStream));
            do {
                vector.addElement((Object)ResponderID.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque16((InputStream)byteArrayInputStream))));
            } while (byteArrayInputStream.available() > 0);
        }
        int n3 = TlsUtils.readUint16(inputStream);
        Extensions extensions = null;
        if (n3 > 0) {
            extensions = Extensions.getInstance(TlsUtils.readDERObject(TlsUtils.readFully(n3, inputStream)));
        }
        return new OCSPStatusRequest(vector, extensions);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void encode(OutputStream outputStream) {
        if (this.responderIDList == null || this.responderIDList.isEmpty()) {
            TlsUtils.writeUint16(0, outputStream);
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i2 = 0; i2 < this.responderIDList.size(); ++i2) {
                TlsUtils.writeOpaque16(((ResponderID)this.responderIDList.elementAt(i2)).getEncoded("DER"), (OutputStream)byteArrayOutputStream);
            }
            TlsUtils.checkUint16(byteArrayOutputStream.size());
            TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
            byteArrayOutputStream.writeTo(outputStream);
        }
        if (this.requestExtensions == null) {
            TlsUtils.writeUint16(0, outputStream);
            return;
        }
        byte[] arrby = this.requestExtensions.getEncoded("DER");
        TlsUtils.checkUint16(arrby.length);
        TlsUtils.writeUint16(arrby.length, outputStream);
        outputStream.write(arrby);
    }

    public Extensions getRequestExtensions() {
        return this.requestExtensions;
    }

    public Vector getResponderIDList() {
        return this.responderIDList;
    }
}

