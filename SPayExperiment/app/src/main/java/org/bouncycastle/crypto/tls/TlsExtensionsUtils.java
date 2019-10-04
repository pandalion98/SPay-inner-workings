/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Hashtable
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import org.bouncycastle.crypto.tls.CertificateStatusRequest;
import org.bouncycastle.crypto.tls.HeartbeatExtension;
import org.bouncycastle.crypto.tls.ServerNameList;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Integers;

public class TlsExtensionsUtils {
    public static final Integer EXT_encrypt_then_mac = Integers.valueOf((int)22);
    public static final Integer EXT_extended_master_secret = Integers.valueOf((int)23);
    public static final Integer EXT_heartbeat = Integers.valueOf((int)15);
    public static final Integer EXT_max_fragment_length = Integers.valueOf((int)1);
    public static final Integer EXT_server_name = Integers.valueOf((int)0);
    public static final Integer EXT_status_request = Integers.valueOf((int)5);
    public static final Integer EXT_truncated_hmac = Integers.valueOf((int)4);

    public static void addEncryptThenMACExtension(Hashtable hashtable) {
        hashtable.put((Object)EXT_encrypt_then_mac, (Object)TlsExtensionsUtils.createEncryptThenMACExtension());
    }

    public static void addExtendedMasterSecretExtension(Hashtable hashtable) {
        hashtable.put((Object)EXT_extended_master_secret, (Object)TlsExtensionsUtils.createExtendedMasterSecretExtension());
    }

    public static void addHeartbeatExtension(Hashtable hashtable, HeartbeatExtension heartbeatExtension) {
        hashtable.put((Object)EXT_heartbeat, (Object)TlsExtensionsUtils.createHeartbeatExtension(heartbeatExtension));
    }

    public static void addMaxFragmentLengthExtension(Hashtable hashtable, short s2) {
        hashtable.put((Object)EXT_max_fragment_length, (Object)TlsExtensionsUtils.createMaxFragmentLengthExtension(s2));
    }

    public static void addServerNameExtension(Hashtable hashtable, ServerNameList serverNameList) {
        hashtable.put((Object)EXT_server_name, (Object)TlsExtensionsUtils.createServerNameExtension(serverNameList));
    }

    public static void addStatusRequestExtension(Hashtable hashtable, CertificateStatusRequest certificateStatusRequest) {
        hashtable.put((Object)EXT_status_request, (Object)TlsExtensionsUtils.createStatusRequestExtension(certificateStatusRequest));
    }

    public static void addTruncatedHMacExtension(Hashtable hashtable) {
        hashtable.put((Object)EXT_truncated_hmac, (Object)TlsExtensionsUtils.createTruncatedHMacExtension());
    }

    public static byte[] createEmptyExtensionData() {
        return TlsUtils.EMPTY_BYTES;
    }

    public static byte[] createEncryptThenMACExtension() {
        return TlsExtensionsUtils.createEmptyExtensionData();
    }

    public static byte[] createExtendedMasterSecretExtension() {
        return TlsExtensionsUtils.createEmptyExtensionData();
    }

    public static byte[] createHeartbeatExtension(HeartbeatExtension heartbeatExtension) {
        if (heartbeatExtension == null) {
            throw new TlsFatalAlert(80);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        heartbeatExtension.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] createMaxFragmentLengthExtension(short s2) {
        TlsUtils.checkUint8(s2);
        byte[] arrby = new byte[1];
        TlsUtils.writeUint8(s2, arrby, 0);
        return arrby;
    }

    public static byte[] createServerNameExtension(ServerNameList serverNameList) {
        if (serverNameList == null) {
            throw new TlsFatalAlert(80);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serverNameList.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] createStatusRequestExtension(CertificateStatusRequest certificateStatusRequest) {
        if (certificateStatusRequest == null) {
            throw new TlsFatalAlert(80);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificateStatusRequest.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] createTruncatedHMacExtension() {
        return TlsExtensionsUtils.createEmptyExtensionData();
    }

    public static Hashtable ensureExtensionsInitialised(Hashtable hashtable) {
        if (hashtable == null) {
            hashtable = new Hashtable();
        }
        return hashtable;
    }

    public static HeartbeatExtension getHeartbeatExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_heartbeat);
        if (arrby == null) {
            return null;
        }
        return TlsExtensionsUtils.readHeartbeatExtension(arrby);
    }

    public static short getMaxFragmentLengthExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_max_fragment_length);
        if (arrby == null) {
            return -1;
        }
        return TlsExtensionsUtils.readMaxFragmentLengthExtension(arrby);
    }

    public static ServerNameList getServerNameExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_server_name);
        if (arrby == null) {
            return null;
        }
        return TlsExtensionsUtils.readServerNameExtension(arrby);
    }

    public static CertificateStatusRequest getStatusRequestExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_status_request);
        if (arrby == null) {
            return null;
        }
        return TlsExtensionsUtils.readStatusRequestExtension(arrby);
    }

    public static boolean hasEncryptThenMACExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_encrypt_then_mac);
        if (arrby == null) {
            return false;
        }
        return TlsExtensionsUtils.readEncryptThenMACExtension(arrby);
    }

    public static boolean hasExtendedMasterSecretExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_extended_master_secret);
        if (arrby == null) {
            return false;
        }
        return TlsExtensionsUtils.readExtendedMasterSecretExtension(arrby);
    }

    public static boolean hasTruncatedHMacExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_truncated_hmac);
        if (arrby == null) {
            return false;
        }
        return TlsExtensionsUtils.readTruncatedHMacExtension(arrby);
    }

    private static boolean readEmptyExtensionData(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        if (arrby.length != 0) {
            throw new TlsFatalAlert(47);
        }
        return true;
    }

    public static boolean readEncryptThenMACExtension(byte[] arrby) {
        return TlsExtensionsUtils.readEmptyExtensionData(arrby);
    }

    public static boolean readExtendedMasterSecretExtension(byte[] arrby) {
        return TlsExtensionsUtils.readEmptyExtensionData(arrby);
    }

    public static HeartbeatExtension readHeartbeatExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        HeartbeatExtension heartbeatExtension = HeartbeatExtension.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return heartbeatExtension;
    }

    public static short readMaxFragmentLengthExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        if (arrby.length != 1) {
            throw new TlsFatalAlert(50);
        }
        return TlsUtils.readUint8(arrby, 0);
    }

    public static ServerNameList readServerNameExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        ServerNameList serverNameList = ServerNameList.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return serverNameList;
    }

    public static CertificateStatusRequest readStatusRequestExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        CertificateStatusRequest certificateStatusRequest = CertificateStatusRequest.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return certificateStatusRequest;
    }

    public static boolean readTruncatedHMacExtension(byte[] arrby) {
        return TlsExtensionsUtils.readEmptyExtensionData(arrby);
    }
}

