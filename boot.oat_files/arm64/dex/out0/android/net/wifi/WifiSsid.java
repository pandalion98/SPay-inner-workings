package android.net.wifi;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Locale;

public class WifiSsid implements Parcelable {
    public static final Creator<WifiSsid> CREATOR = new Creator<WifiSsid>() {
        public WifiSsid createFromParcel(Parcel in) {
            WifiSsid ssid = new WifiSsid();
            int length = in.readInt();
            byte[] b = new byte[length];
            in.readByteArray(b);
            ssid.octets.write(b, 0, length);
            return ssid;
        }

        public WifiSsid[] newArray(int size) {
            return new WifiSsid[size];
        }
    };
    private static final int HEX_RADIX = 16;
    public static final String NONE = "<unknown ssid>";
    private static final String TAG = "WifiSsid";
    public final ByteArrayOutputStream octets;

    private WifiSsid() {
        this.octets = new ByteArrayOutputStream(32);
    }

    public static WifiSsid createFromAsciiEncoded(String asciiEncoded) {
        WifiSsid a = new WifiSsid();
        a.convertToBytes(asciiEncoded);
        return a;
    }

    public static WifiSsid createFromHex(String hexStr) {
        WifiSsid a = new WifiSsid();
        if (hexStr != null) {
            if (hexStr.startsWith("0x") || hexStr.startsWith("0X")) {
                hexStr = hexStr.substring(2);
            }
            for (int i = 0; i < hexStr.length() - 1; i += 2) {
                int val;
                try {
                    val = Integer.parseInt(hexStr.substring(i, i + 2), 16);
                } catch (NumberFormatException e) {
                    val = 0;
                }
                a.octets.write(val);
            }
        }
        return a;
    }

    private void convertToBytes(String asciiEncoded) {
        int i = 0;
        while (i < asciiEncoded.length()) {
            char c = asciiEncoded.charAt(i);
            switch (c) {
                case '\\':
                    i++;
                    int val;
                    switch (asciiEncoded.charAt(i)) {
                        case '\"':
                            this.octets.write(34);
                            i++;
                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                            val = asciiEncoded.charAt(i) - 48;
                            i++;
                            if (asciiEncoded.charAt(i) >= '0' && asciiEncoded.charAt(i) <= '7') {
                                val = ((val * 8) + asciiEncoded.charAt(i)) - 48;
                                i++;
                            }
                            if (asciiEncoded.charAt(i) >= '0' && asciiEncoded.charAt(i) <= '7') {
                                val = ((val * 8) + asciiEncoded.charAt(i)) - 48;
                                i++;
                            }
                            this.octets.write(val);
                            break;
                        case '\\':
                            this.octets.write(92);
                            i++;
                            break;
                        case 'e':
                            this.octets.write(27);
                            i++;
                            break;
                        case 'n':
                            this.octets.write(10);
                            i++;
                            break;
                        case 'r':
                            this.octets.write(13);
                            i++;
                            break;
                        case 't':
                            this.octets.write(9);
                            i++;
                            break;
                        case 'x':
                            i++;
                            try {
                                val = Integer.parseInt(asciiEncoded.substring(i, i + 2), 16);
                            } catch (NumberFormatException e) {
                                val = -1;
                            } catch (StringIndexOutOfBoundsException e2) {
                                val = -1;
                            }
                            if (val >= 0) {
                                this.octets.write(val);
                                i += 2;
                                break;
                            }
                            val = Character.digit(asciiEncoded.charAt(i), 16);
                            if (val < 0) {
                                break;
                            }
                            this.octets.write(val);
                            i++;
                            break;
                        default:
                            break;
                    }
                default:
                    this.octets.write(c);
                    i++;
                    break;
            }
        }
    }

    public String toString() {
        byte[] ssidBytes = this.octets.toByteArray();
        if (this.octets.size() <= 0 || isArrayAllZeroes(ssidBytes)) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        CharBuffer out = CharBuffer.allocate(32);
        CoderResult result = decoder.decode(ByteBuffer.wrap(ssidBytes), out, true);
        out.flip();
        if (result.isError()) {
            return NONE;
        }
        return out.toString();
    }

    private boolean isArrayAllZeroes(byte[] ssidBytes) {
        for (byte b : ssidBytes) {
            if (b != (byte) 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isHidden() {
        return isArrayAllZeroes(this.octets.toByteArray());
    }

    public byte[] getOctets() {
        return this.octets.toByteArray();
    }

    public String getHexString() {
        if (this.octets.size() == 0) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        String out = "0x";
        byte[] ssidbytes = getOctets();
        for (int i = 0; i < this.octets.size(); i++) {
            out = out + String.format(Locale.US, "%02x", new Object[]{Byte.valueOf(ssidbytes[i])});
        }
        return out;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (dest != null) {
            dest.writeInt(this.octets.size());
            dest.writeByteArray(this.octets.toByteArray());
        }
    }
}
