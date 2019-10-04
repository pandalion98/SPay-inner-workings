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
 *  java.security.SecureRandom
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public abstract class DTLSProtocol {
    protected final SecureRandom secureRandom;

    protected DTLSProtocol(SecureRandom secureRandom) {
        if (secureRandom == null) {
            throw new IllegalArgumentException("'secureRandom' cannot be null");
        }
        this.secureRandom = secureRandom;
    }

    protected static short evaluateMaxFragmentLengthExtension(Hashtable hashtable, Hashtable hashtable2, short s2) {
        short s3 = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable2);
        if (s3 >= 0 && s3 != TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable)) {
            throw new TlsFatalAlert(s2);
        }
        return s3;
    }

    protected static byte[] generateCertificate(Certificate certificate) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificate.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected static byte[] generateSupplementalData(Vector vector) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsProtocol.writeSupplementalData((OutputStream)byteArrayOutputStream, vector);
        return byteArrayOutputStream.toByteArray();
    }

    protected static void validateSelectedCipherSuite(int n2, short s2) {
        switch (TlsUtils.getEncryptionAlgorithm(n2)) {
            default: {
                return;
            }
            case 1: 
            case 2: 
        }
        throw new TlsFatalAlert(s2);
    }

    protected void processFinished(byte[] arrby, byte[] arrby2) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        byte[] arrby3 = TlsUtils.readFully(arrby2.length, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!Arrays.constantTimeAreEqual((byte[])arrby2, (byte[])arrby3)) {
            throw new TlsFatalAlert(40);
        }
    }
}

