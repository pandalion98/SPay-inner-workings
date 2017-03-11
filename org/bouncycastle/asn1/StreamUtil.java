package org.bouncycastle.asn1;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.jce.X509KeyUsage;

class StreamUtil {
    private static final long MAX_MEMORY;

    static {
        MAX_MEMORY = Runtime.getRuntime().maxMemory();
    }

    StreamUtil() {
    }

    static int calculateBodyLength(int i) {
        int i2 = 1;
        if (i > CertificateBody.profileType) {
            int i3 = 1;
            while (true) {
                i >>>= 8;
                if (i == 0) {
                    break;
                }
                i3++;
            }
            i3 = (i3 - 1) * 8;
            while (i3 >= 0) {
                i3 -= 8;
                i2++;
            }
        }
        return i2;
    }

    static int calculateTagLength(int i) {
        if (i < 31) {
            return 1;
        }
        if (i < X509KeyUsage.digitalSignature) {
            return 2;
        }
        byte[] bArr = new byte[5];
        int length = bArr.length - 1;
        bArr[length] = (byte) (i & CertificateBody.profileType);
        do {
            i >>= 7;
            length--;
            bArr[length] = (byte) ((i & CertificateBody.profileType) | X509KeyUsage.digitalSignature);
        } while (i > CertificateBody.profileType);
        return (bArr.length - length) + 1;
    }

    static int findLimit(InputStream inputStream) {
        if (inputStream instanceof LimitedInputStream) {
            return ((LimitedInputStream) inputStream).getRemaining();
        }
        if (inputStream instanceof ASN1InputStream) {
            return ((ASN1InputStream) inputStream).getLimit();
        }
        if (inputStream instanceof ByteArrayInputStream) {
            return ((ByteArrayInputStream) inputStream).available();
        }
        if (inputStream instanceof FileInputStream) {
            try {
                FileChannel channel = ((FileInputStream) inputStream).getChannel();
                long size = channel != null ? channel.size() : 2147483647L;
                if (size < 2147483647L) {
                    return (int) size;
                }
            } catch (IOException e) {
            }
        }
        return MAX_MEMORY > 2147483647L ? CNCCCommands.CMD_CNCC_CMD_UNKNOWN : (int) MAX_MEMORY;
    }
}
