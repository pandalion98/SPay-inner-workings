package org.bouncycastle.crypto.generators;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class OpenBSDBCrypt {
    private static final byte[] decodingTable;
    private static final byte[] encodingTable;
    private static final String version = "2a";

    static {
        int i = 0;
        encodingTable = new byte[]{(byte) 46, (byte) 47, (byte) 65, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) 67, (byte) 68, (byte) 69, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, GetTemplateApdu.TAG_DF_NAME_4F, GetTemplateApdu.TAG_APPLICATION_LABEL_50, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) 82, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 84, (byte) 85, (byte) 86, (byte) 87, ApplicationInfoManager.TERM_XP1, (byte) 89, (byte) 90, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 118, ApplicationInfoManager.TERM_XP2, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, ApplicationInfoManager.TERM_XP3, (byte) 52, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 54, (byte) 55, (byte) 56, ApplicationInfoManager.EMV_MS};
        decodingTable = new byte[X509KeyUsage.digitalSignature];
        for (int i2 = 0; i2 < decodingTable.length; i2++) {
            decodingTable[i2] = (byte) -1;
        }
        while (i < encodingTable.length) {
            decodingTable[encodingTable[i]] = (byte) i;
            i++;
        }
    }

    public static boolean checkPassword(String str, char[] cArr) {
        if (str.length() != 60) {
            throw new DataLengthException("Bcrypt String length: " + str.length() + ", 60 required.");
        } else if (str.charAt(0) != '$' || str.charAt(3) != '$' || str.charAt(6) != '$') {
            throw new IllegalArgumentException("Invalid Bcrypt String format.");
        } else if (str.substring(1, 3).equals(version)) {
            try {
                int parseInt = Integer.parseInt(str.substring(4, 6));
                if (parseInt < 4 || parseInt > 31) {
                    throw new IllegalArgumentException("Invalid cost factor: " + parseInt + ", 4 < cost < 31 expected.");
                } else if (cArr != null) {
                    return str.equals(generate(cArr, decodeSaltString(str.substring(str.lastIndexOf(36) + 1, str.length() - 31)), parseInt));
                } else {
                    throw new IllegalArgumentException("Missing password.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid cost factor:" + str.substring(4, 6));
            }
        } else {
            throw new IllegalArgumentException("Wrong Bcrypt version, 2a expected.");
        }
    }

    private static String createBcryptString(byte[] bArr, byte[] bArr2, int i) {
        StringBuffer stringBuffer = new StringBuffer(60);
        stringBuffer.append('$');
        stringBuffer.append(version);
        stringBuffer.append('$');
        stringBuffer.append(i < 10 ? TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + i : Integer.toString(i));
        stringBuffer.append('$');
        stringBuffer.append(encodeData(bArr2));
        stringBuffer.append(encodeData(BCrypt.generate(bArr, bArr2, i)));
        return stringBuffer.toString();
    }

    private static byte[] decodeSaltString(String str) {
        Object toCharArray = str.toCharArray();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16);
        if (toCharArray.length != 22) {
            throw new DataLengthException("Invalid base64 salt length: " + toCharArray.length + " , 22 required.");
        }
        int i;
        for (char c : toCharArray) {
            if (c > 'z' || c < '.' || (c > '9' && c < 'A')) {
                throw new IllegalArgumentException("Salt string contains invalid character: " + c);
            }
        }
        Object obj = new char[24];
        System.arraycopy(toCharArray, 0, obj, 0, toCharArray.length);
        int length = obj.length;
        for (i = 0; i < length; i += 4) {
            byte b = decodingTable[obj[i]];
            byte b2 = decodingTable[obj[i + 1]];
            byte b3 = decodingTable[obj[i + 2]];
            byte b4 = decodingTable[obj[i + 3]];
            byteArrayOutputStream.write((b << 2) | (b2 >> 4));
            byteArrayOutputStream.write((b2 << 4) | (b3 >> 2));
            byteArrayOutputStream.write((b3 << 6) | b4);
        }
        toCharArray = new byte[16];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, toCharArray, 0, toCharArray.length);
        return toCharArray;
    }

    private static String encodeData(byte[] bArr) {
        if (bArr.length == 24 || bArr.length == 16) {
            Object obj;
            if (bArr.length == 16) {
                obj = new byte[18];
                System.arraycopy(bArr, 0, obj, 0, bArr.length);
                bArr = obj;
                obj = 1;
            } else {
                bArr[bArr.length - 1] = (byte) 0;
                obj = (byte) 0;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int length = bArr.length;
            for (int i = 0; i < length; i += 3) {
                int i2 = bArr[i] & GF2Field.MASK;
                int i3 = bArr[i + 1] & GF2Field.MASK;
                int i4 = bArr[i + 2] & GF2Field.MASK;
                byteArrayOutputStream.write(encodingTable[(i2 >>> 2) & 63]);
                byteArrayOutputStream.write(encodingTable[((i2 << 4) | (i3 >>> 4)) & 63]);
                byteArrayOutputStream.write(encodingTable[((i3 << 2) | (i4 >>> 6)) & 63]);
                byteArrayOutputStream.write(encodingTable[i4 & 63]);
            }
            String fromByteArray = Strings.fromByteArray(byteArrayOutputStream.toByteArray());
            return obj == 1 ? fromByteArray.substring(0, 22) : fromByteArray.substring(0, fromByteArray.length() - 1);
        } else {
            throw new DataLengthException("Invalid length: " + bArr.length + ", 24 for key or 16 for salt expected");
        }
    }

    public static String generate(char[] cArr, byte[] bArr, int i) {
        int i2 = 72;
        if (cArr == null) {
            throw new IllegalArgumentException("Password required.");
        } else if (bArr == null) {
            throw new IllegalArgumentException("Salt required.");
        } else if (bArr.length != 16) {
            throw new DataLengthException("16 byte salt required: " + bArr.length);
        } else if (i < 4 || i > 31) {
            throw new IllegalArgumentException("Invalid cost factor.");
        } else {
            byte[] toUTF8ByteArray = Strings.toUTF8ByteArray(cArr);
            if (toUTF8ByteArray.length < 72) {
                i2 = toUTF8ByteArray.length + 1;
            }
            byte[] bArr2 = new byte[i2];
            if (bArr2.length > toUTF8ByteArray.length) {
                System.arraycopy(toUTF8ByteArray, 0, bArr2, 0, toUTF8ByteArray.length);
            } else {
                System.arraycopy(toUTF8ByteArray, 0, bArr2, 0, bArr2.length);
            }
            Arrays.fill(toUTF8ByteArray, (byte) 0);
            String createBcryptString = createBcryptString(bArr2, bArr, i);
            Arrays.fill(bArr2, (byte) 0);
            return createBcryptString;
        }
    }
}
