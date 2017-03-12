package android.net.arp;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.RouteInfo;
import android.net.netlink.StructNlMsgHdr;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import libcore.net.RawSocket;

public class ArpPeer {
    private static final int ARP_LENGTH = 28;
    private static final boolean DBG = false;
    private static final int ETHERNET_TYPE = 1;
    private static final int IPV4_LENGTH = 4;
    private static final int MAC_ADDR_LENGTH = 6;
    private static final int MAX_LENGTH = 1500;
    private static final String TAG = "ArpPeer";
    private final byte[] L2_BROADCAST;
    private String mInterfaceName;
    private final InetAddress mMyAddr;
    private final byte[] mMyMac = new byte[6];
    private final InetAddress mPeer;
    private final RawSocket mSocket;

    public ArpPeer(String interfaceName, InetAddress myAddr, String mac, InetAddress peer) throws SocketException {
        this.mInterfaceName = interfaceName;
        this.mMyAddr = myAddr;
        if (mac != null) {
            for (int i = 0; i < 6; i++) {
                this.mMyMac[i] = (byte) Integer.parseInt(mac.substring(i * 3, (i * 3) + 2), 16);
            }
        }
        if ((myAddr instanceof Inet6Address) || (peer instanceof Inet6Address)) {
            throw new IllegalArgumentException("IPv6 unsupported");
        }
        this.mPeer = peer;
        this.L2_BROADCAST = new byte[6];
        Arrays.fill(this.L2_BROADCAST, (byte) -1);
        this.mSocket = new RawSocket(this.mInterfaceName, (short) 2054);
    }

    public byte[] doArp(int timeoutMillis) {
        ByteBuffer buf = ByteBuffer.allocate(1500);
        byte[] desiredIp = this.mPeer.getAddress();
        long timeout = SystemClock.elapsedRealtime() + ((long) timeoutMillis);
        buf.clear();
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort((short) 1);
        buf.putShort(StructNlMsgHdr.NLM_F_APPEND);
        buf.put((byte) 6);
        buf.put((byte) 4);
        buf.putShort((short) 1);
        buf.put(this.mMyMac);
        buf.put(this.mMyAddr.getAddress());
        buf.put(new byte[6]);
        buf.put(desiredIp);
        buf.flip();
        this.mSocket.write(this.L2_BROADCAST, buf.array(), 0, buf.limit());
        byte[] recvBuf = new byte[1500];
        while (SystemClock.elapsedRealtime() < timeout) {
            if (this.mSocket.read(recvBuf, 0, recvBuf.length, -1, (int) (timeout - SystemClock.elapsedRealtime())) >= 28 && recvBuf[0] == (byte) 0 && recvBuf[1] == (byte) 1 && recvBuf[2] == (byte) 8 && recvBuf[3] == (byte) 0 && recvBuf[4] == (byte) 6 && recvBuf[5] == (byte) 4 && recvBuf[6] == (byte) 0 && recvBuf[7] == (byte) 2 && recvBuf[14] == desiredIp[0] && recvBuf[15] == desiredIp[1] && recvBuf[16] == desiredIp[2] && recvBuf[17] == desiredIp[3]) {
                byte[] result = new byte[6];
                System.arraycopy(recvBuf, 8, result, 0, 6);
                return result;
            }
        }
        return null;
    }

    public static boolean doArp(String myMacAddress, LinkProperties linkProperties, int timeoutMillis, int numArpPings, int minArpResponses) {
        String interfaceName = linkProperties.getInterfaceName();
        InetAddress inetAddress = null;
        InetAddress gateway = null;
        Iterator i$ = linkProperties.getLinkAddresses().iterator();
        if (i$.hasNext()) {
            inetAddress = ((LinkAddress) i$.next()).getAddress();
        }
        i$ = linkProperties.getRoutes().iterator();
        if (i$.hasNext()) {
            gateway = ((RouteInfo) i$.next()).getGateway();
        }
        try {
            ArpPeer peer = new ArpPeer(interfaceName, inetAddress, myMacAddress, gateway);
            int responses = 0;
            for (int i = 0; i < numArpPings; i++) {
                if (peer.doArp(timeoutMillis) != null) {
                    responses++;
                }
            }
            boolean success = responses >= minArpResponses;
            peer.close();
            return success;
        } catch (SocketException se) {
            Log.e(TAG, "ARP test initiation failure: " + se);
            return true;
        }
    }

    public void close() {
        try {
            this.mSocket.close();
        } catch (IOException e) {
        }
    }
}
