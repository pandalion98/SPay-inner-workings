package com.mastercard.mobile_api.bytes;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.EncryptionAlgorithm;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;

public class DefaultByteArrayFactory extends ByteArrayFactory {
    protected static final char[] HEX_DIGITS;

    static {
        HEX_DIGITS = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    public ByteArray getByteArray(int i) {
        return new DefaultByteArrayImpl(i);
    }

    public ByteArray getByteArray(byte[] bArr, int i) {
        return new DefaultByteArrayImpl(bArr, i);
    }

    public ByteArray getFromByteArray(ByteArray byteArray) {
        return new DefaultByteArrayImpl(byteArray.getBytes());
    }

    public ByteArray getFromWord(int i) {
        return new DefaultByteArrayImpl(ByteBuffer.allocate(2).putShort((short) i).array(), 2);
    }

    public String toString(ByteArray byteArray, int i, int i2) {
        char[] cArr = new char[(i2 * 2)];
        int i3 = 0;
        for (int i4 = i; i4 < i + i2; i4++) {
            byte b = byteArray.getByte(i4);
            int i5 = i3 + 1;
            cArr[i3] = HEX_DIGITS[(b >>> 4) & 15];
            i3 = i5 + 1;
            cArr[i5] = HEX_DIGITS[b & 15];
        }
        return new String(cArr);
    }

    public int fromChar(char c) {
        switch (c) {
            case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_CBC_SHA /*49*/:
            case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
            case CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA /*52*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA /*53*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA /*54*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA /*55*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA /*56*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA /*57*/:
                return c - 48;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA /*65*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA /*67*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA /*68*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA /*69*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA /*70*/:
                return (c - 65) + 10;
            case EACTags.APPLICATION_TEMPLATE /*97*/:
            case EACTags.FCP_TEMPLATE /*98*/:
            case EACTags.WRAPPER /*99*/:
            case EncryptionAlgorithm.ESTREAM_SALSA20 /*100*/:
            case ExtensionType.negotiated_ff_dhe_groups /*101*/:
            case EncryptionAlgorithm.AEAD_CHACHA20_POLY1305 /*102*/:
                return (c - 97) + 10;
            default:
                throw new IllegalArgumentException("invalid hex digit [" + c + "]");
        }
    }

    public char toChar(int i) {
        if (i >= 0 && i <= 15) {
            return HEX_DIGITS[i];
        }
        throw new IllegalArgumentException("invalid hex digit [" + i + "]");
    }

    public String getStringLengthAsHex(String str) {
        String str2 = null;
        try {
            str2 = integerToHex(str.getBytes("UTF-8").length);
        } catch (UnsupportedEncodingException e) {
        }
        return str2.toUpperCase();
    }

    public String getHexStringLengthAsHex(String str) {
        return integerToHex(fromHexString(str).getLength()).toUpperCase();
    }

    public String getHexStringLengthAsHex(String str, int i) {
        return padleft(integerToHex(fromHexString(str).getLength()), i * 2, LLVARUtil.EMPTY_STRING).toUpperCase();
    }

    public String padleft(String str, int i, char c) {
        String trim = str.trim();
        StringBuilder stringBuilder = new StringBuilder(i);
        int length = i - trim.length();
        while (true) {
            int i2 = length - 1;
            if (length <= 0) {
                return stringBuilder.append(trim).toString();
            }
            stringBuilder.append(c);
            length = i2;
        }
    }

    public int hexToInteger(String str) {
        return Integer.parseInt(str, 16);
    }

    public String integerToHex(int i) {
        String toHexString = Integer.toHexString(i);
        if (toHexString.length() % 2 != 0) {
            toHexString = padleft(toHexString, toHexString.length() + 1, LLVARUtil.EMPTY_STRING);
        }
        return toHexString.toUpperCase();
    }

    public String formatAtc(int i) {
        return padleft(integerToHex(i), 4, LLVARUtil.EMPTY_STRING).toUpperCase();
    }

    public boolean compareString(String str, String str2) {
        return str.equals(str2);
    }

    public int stringToInt(String str) {
        return Integer.parseInt(str);
    }

    public boolean isNull(String str) {
        return str == null;
    }

    public ByteArray fromHexString(String str) {
        return new DefaultByteArrayImpl(str);
    }

    public String getUTF8String(ByteArray byteArray) {
        return new String(byteArray.getBytes());
    }

    public ByteArray convertString(String str) {
        return ByteArrayFactory.getInstance().getByteArray(str.getBytes(), str.length());
    }

    public String hexStringToBase64(String str) {
        int i = 0;
        String str2 = BuildConfig.FLAVOR;
        String str3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String str4 = BuildConfig.FLAVOR;
        for (int i2 = 0; i2 < str.length(); i2++) {
            String str5 = "000000" + Integer.toBinaryString(Integer.parseInt(str.substring(i2, i2 + 1), 16));
            str4 = new StringBuilder(String.valueOf(str4)).append(str5.substring(str5.length() - 4)).toString();
        }
        str4 = new StringBuilder(String.valueOf(str4)).append("000000".substring(str4.length() % 6)).toString();
        String str6 = str2;
        while (i < str4.length()) {
            str6 = new StringBuilder(String.valueOf(str6)).append(str3.charAt(Integer.parseInt(str4.substring(i, i + 6), 2))).toString();
            i += 6;
        }
        return new StringBuilder(String.valueOf(str6)).append("====".substring(str6.length() % 4)).toString();
    }
}
