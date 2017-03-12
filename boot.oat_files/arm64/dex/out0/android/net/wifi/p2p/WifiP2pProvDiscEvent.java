package android.net.wifi.p2p;

import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiP2pProvDiscEvent {
    public static final int ENTER_PIN = 3;
    public static final int EXT_NFC = 5;
    public static final int PBC_REQ = 1;
    public static final int PBC_RSP = 2;
    public static final int SHOW_PIN = 4;
    private static final String TAG = "WifiP2pProvDiscEvent";
    public static final int USER_REJECT = 6;
    private static final Pattern groupCapabPattern = Pattern.compile("group_capab=(0x[0-9a-fA-F]+)");
    private static final Pattern staticIpPattern = Pattern.compile("static_ip=(0x[0-9a-fA-F]+)");
    public WifiP2pDevice device;
    public int event;
    public String pin;

    public WifiP2pProvDiscEvent() {
        this.device = new WifiP2pDevice();
    }

    public WifiP2pProvDiscEvent(String string) throws IllegalArgumentException {
        String[] tokens = string.split(" ");
        if (tokens.length < 2) {
            throw new IllegalArgumentException("Malformed event " + string);
        }
        if (tokens[0].endsWith("PBC-REQ")) {
            this.event = 1;
        } else if (tokens[0].endsWith("PBC-RESP")) {
            this.event = 2;
        } else if (tokens[0].endsWith("ENTER-PIN")) {
            this.event = 3;
        } else if (tokens[0].endsWith("SHOW-PIN")) {
            this.event = 4;
        } else {
            if (tokens[0].endsWith("EXTNFC-RESP")) {
            }
            if (tokens[0].endsWith("USER-REJECT")) {
                this.event = 6;
            } else {
                throw new IllegalArgumentException("Malformed event " + string);
            }
        }
        this.device = new WifiP2pDevice();
        this.device.deviceAddress = tokens[1];
        Matcher matchForGroupCapability = groupCapabPattern.matcher(string);
        if (matchForGroupCapability.find()) {
            this.device.groupCapability = parseHex(matchForGroupCapability.group(1));
        }
        Matcher matchForStaticIp = staticIpPattern.matcher(string);
        if (matchForStaticIp.find()) {
            this.device.candidateStaticIp = parseHex(matchForStaticIp.group(1));
        }
        if (this.event == 4) {
            this.pin = tokens[2];
        }
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(this.device);
        sbuf.append("\n event: ").append(this.event);
        sbuf.append("\n pin: ").append(this.pin);
        return sbuf.toString();
    }

    private int parseHex(String hexString) {
        int num = 0;
        if (hexString.startsWith("0x") || hexString.startsWith("0X")) {
            hexString = hexString.substring(2);
        }
        try {
            num = Integer.parseInt(hexString, 16);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Failed to parse hex string " + hexString);
        }
        return num;
    }
}
