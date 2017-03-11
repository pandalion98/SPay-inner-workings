package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import org.bouncycastle.util.Integers;

public class TlsExtensionsUtils {
    public static final Integer EXT_encrypt_then_mac;
    public static final Integer EXT_extended_master_secret;
    public static final Integer EXT_heartbeat;
    public static final Integer EXT_max_fragment_length;
    public static final Integer EXT_server_name;
    public static final Integer EXT_status_request;
    public static final Integer EXT_truncated_hmac;

    static {
        EXT_encrypt_then_mac = Integers.valueOf(22);
        EXT_extended_master_secret = Integers.valueOf(23);
        EXT_heartbeat = Integers.valueOf(15);
        EXT_max_fragment_length = Integers.valueOf(1);
        EXT_server_name = Integers.valueOf(0);
        EXT_status_request = Integers.valueOf(5);
        EXT_truncated_hmac = Integers.valueOf(4);
    }

    public static void addEncryptThenMACExtension(Hashtable hashtable) {
        hashtable.put(EXT_encrypt_then_mac, createEncryptThenMACExtension());
    }

    public static void addExtendedMasterSecretExtension(Hashtable hashtable) {
        hashtable.put(EXT_extended_master_secret, createExtendedMasterSecretExtension());
    }

    public static void addHeartbeatExtension(Hashtable hashtable, HeartbeatExtension heartbeatExtension) {
        hashtable.put(EXT_heartbeat, createHeartbeatExtension(heartbeatExtension));
    }

    public static void addMaxFragmentLengthExtension(Hashtable hashtable, short s) {
        hashtable.put(EXT_max_fragment_length, createMaxFragmentLengthExtension(s));
    }

    public static void addServerNameExtension(Hashtable hashtable, ServerNameList serverNameList) {
        hashtable.put(EXT_server_name, createServerNameExtension(serverNameList));
    }

    public static void addStatusRequestExtension(Hashtable hashtable, CertificateStatusRequest certificateStatusRequest) {
        hashtable.put(EXT_status_request, createStatusRequestExtension(certificateStatusRequest));
    }

    public static void addTruncatedHMacExtension(Hashtable hashtable) {
        hashtable.put(EXT_truncated_hmac, createTruncatedHMacExtension());
    }

    public static byte[] createEmptyExtensionData() {
        return TlsUtils.EMPTY_BYTES;
    }

    public static byte[] createEncryptThenMACExtension() {
        return createEmptyExtensionData();
    }

    public static byte[] createExtendedMasterSecretExtension() {
        return createEmptyExtensionData();
    }

    public static byte[] createHeartbeatExtension(HeartbeatExtension heartbeatExtension) {
        if (heartbeatExtension == null) {
            throw new TlsFatalAlert((short) 80);
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        heartbeatExtension.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] createMaxFragmentLengthExtension(short s) {
        TlsUtils.checkUint8(s);
        byte[] bArr = new byte[1];
        TlsUtils.writeUint8(s, bArr, 0);
        return bArr;
    }

    public static byte[] createServerNameExtension(ServerNameList serverNameList) {
        if (serverNameList == null) {
            throw new TlsFatalAlert((short) 80);
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serverNameList.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] createStatusRequestExtension(CertificateStatusRequest certificateStatusRequest) {
        if (certificateStatusRequest == null) {
            throw new TlsFatalAlert((short) 80);
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificateStatusRequest.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] createTruncatedHMacExtension() {
        return createEmptyExtensionData();
    }

    public static Hashtable ensureExtensionsInitialised(Hashtable hashtable) {
        return hashtable == null ? new Hashtable() : hashtable;
    }

    public static HeartbeatExtension getHeartbeatExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_heartbeat);
        return extensionData == null ? null : readHeartbeatExtension(extensionData);
    }

    public static short getMaxFragmentLengthExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_max_fragment_length);
        return extensionData == null ? (short) -1 : readMaxFragmentLengthExtension(extensionData);
    }

    public static ServerNameList getServerNameExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_server_name);
        return extensionData == null ? null : readServerNameExtension(extensionData);
    }

    public static CertificateStatusRequest getStatusRequestExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_status_request);
        return extensionData == null ? null : readStatusRequestExtension(extensionData);
    }

    public static boolean hasEncryptThenMACExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_encrypt_then_mac);
        return extensionData == null ? false : readEncryptThenMACExtension(extensionData);
    }

    public static boolean hasExtendedMasterSecretExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_extended_master_secret);
        return extensionData == null ? false : readExtendedMasterSecretExtension(extensionData);
    }

    public static boolean hasTruncatedHMacExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_truncated_hmac);
        return extensionData == null ? false : readTruncatedHMacExtension(extensionData);
    }

    private static boolean readEmptyExtensionData(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        } else if (bArr.length == 0) {
            return true;
        } else {
            throw new TlsFatalAlert((short) 47);
        }
    }

    public static boolean readEncryptThenMACExtension(byte[] bArr) {
        return readEmptyExtensionData(bArr);
    }

    public static boolean readExtendedMasterSecretExtension(byte[] bArr) {
        return readEmptyExtensionData(bArr);
    }

    public static HeartbeatExtension readHeartbeatExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        HeartbeatExtension parse = HeartbeatExtension.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return parse;
    }

    public static short readMaxFragmentLengthExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        } else if (bArr.length == 1) {
            return TlsUtils.readUint8(bArr, 0);
        } else {
            throw new TlsFatalAlert((short) 50);
        }
    }

    public static ServerNameList readServerNameExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ServerNameList parse = ServerNameList.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return parse;
    }

    public static CertificateStatusRequest readStatusRequestExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        CertificateStatusRequest parse = CertificateStatusRequest.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return parse;
    }

    public static boolean readTruncatedHMacExtension(byte[] bArr) {
        return readEmptyExtensionData(bArr);
    }
}
