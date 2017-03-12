package android.net.wifi.p2p;

public class WifiP2pStaticIpConfig {
    public int candidateStaticIp = 0;
    public String deviceAddress;
    public boolean isStaticIp = false;
    public int mThisDeviceStaticIp = 0;

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n address: ").append(this.deviceAddress);
        sbuf.append("\n candidateStaticIp: ").append(this.candidateStaticIp);
        sbuf.append("\n mThisDeviceStaticIp: ").append(this.mThisDeviceStaticIp);
        sbuf.append("\n isStaticIp: ").append(this.isStaticIp);
        return sbuf.toString();
    }
}
