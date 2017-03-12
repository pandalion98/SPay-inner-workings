package android.net.wifi.p2p.nsd;

import android.net.nsd.DnsSdTxtRecord;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WifiP2pDnsSdServiceInfo extends WifiP2pServiceInfo {
    public static final int DNS_TYPE_PTR = 12;
    public static final int DNS_TYPE_TXT = 16;
    public static final int VERSION_1 = 1;
    private static final Map<String, String> sVmPacket = new HashMap();

    static {
        sVmPacket.put("_tcp.local.", "c00c");
        sVmPacket.put("local.", "c011");
        sVmPacket.put("_udp.local.", "c01c");
    }

    private WifiP2pDnsSdServiceInfo(List<String> queryList) {
        super(queryList);
    }

    public static WifiP2pDnsSdServiceInfo newInstance(String instanceName, String serviceType, Map<String, String> txtMap) {
        if (TextUtils.isEmpty(instanceName) || TextUtils.isEmpty(serviceType)) {
            throw new IllegalArgumentException("instance name or service type cannot be empty");
        }
        DnsSdTxtRecord txtRecord = new DnsSdTxtRecord();
        if (txtMap != null) {
            for (String key : txtMap.keySet()) {
                txtRecord.set(key, (String) txtMap.get(key));
            }
        }
        ArrayList<String> queries = new ArrayList();
        queries.add(createPtrServiceQuery(instanceName, serviceType));
        queries.add(createTxtServiceQuery(instanceName, serviceType, txtRecord));
        return new WifiP2pDnsSdServiceInfo(queries);
    }

    private static String createPtrServiceQuery(String instanceName, String serviceType) {
        StringBuffer sb = new StringBuffer();
        sb.append("bonjour ");
        sb.append(createRequest(serviceType + ".local.", 12, 1));
        sb.append(" ");
        sb.append(String.format(Locale.US, "%02x", new Object[]{Integer.valueOf(instanceName.getBytes().length)}));
        sb.append(WifiP2pServiceInfo.bin2HexStr(data));
        sb.append("c027");
        return sb.toString();
    }

    private static String createTxtServiceQuery(String instanceName, String serviceType, DnsSdTxtRecord txtRecord) {
        StringBuffer sb = new StringBuffer();
        sb.append("bonjour ");
        sb.append(createRequest(instanceName + "." + serviceType + ".local.", 16, 1));
        sb.append(" ");
        byte[] rawData = txtRecord.getRawData();
        if (rawData.length == 0) {
            sb.append("00");
        } else {
            sb.append(WifiP2pServiceInfo.bin2HexStr(rawData));
        }
        return sb.toString();
    }

    static String createRequest(String dnsName, int dnsType, int version) {
        StringBuffer sb = new StringBuffer();
        if (dnsType == 16) {
            dnsName = dnsName.toLowerCase(Locale.ROOT);
        }
        sb.append(compressDnsName(dnsName));
        sb.append(String.format(Locale.US, "%04x", new Object[]{Integer.valueOf(dnsType)}));
        sb.append(String.format(Locale.US, "%02x", new Object[]{Integer.valueOf(version)}));
        return sb.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String compressDnsName(java.lang.String r10) {
        /*
        r9 = 1;
        r8 = 0;
        r3 = new java.lang.StringBuffer;
        r3.<init>();
    L_0x0007:
        r4 = sVmPacket;
        r0 = r4.get(r10);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x0019;
    L_0x0011:
        r3.append(r0);
    L_0x0014:
        r4 = r3.toString();
        return r4;
    L_0x0019:
        r4 = 46;
        r1 = r10.indexOf(r4);
        r4 = -1;
        if (r1 != r4) goto L_0x0050;
    L_0x0022:
        r4 = r10.length();
        if (r4 <= 0) goto L_0x004a;
    L_0x0028:
        r4 = java.util.Locale.US;
        r5 = "%02x";
        r6 = new java.lang.Object[r9];
        r7 = r10.length();
        r7 = java.lang.Integer.valueOf(r7);
        r6[r8] = r7;
        r4 = java.lang.String.format(r4, r5, r6);
        r3.append(r4);
        r4 = r10.getBytes();
        r4 = android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(r4);
        r3.append(r4);
    L_0x004a:
        r4 = "00";
        r3.append(r4);
        goto L_0x0014;
    L_0x0050:
        r2 = r10.substring(r8, r1);
        r4 = r1 + 1;
        r10 = r10.substring(r4);
        r4 = java.util.Locale.US;
        r5 = "%02x";
        r6 = new java.lang.Object[r9];
        r7 = r2.length();
        r7 = java.lang.Integer.valueOf(r7);
        r6[r8] = r7;
        r4 = java.lang.String.format(r4, r5, r6);
        r3.append(r4);
        r4 = r2.getBytes();
        r4 = android.net.wifi.p2p.nsd.WifiP2pServiceInfo.bin2HexStr(r4);
        r3.append(r4);
        goto L_0x0007;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo.compressDnsName(java.lang.String):java.lang.String");
    }
}
