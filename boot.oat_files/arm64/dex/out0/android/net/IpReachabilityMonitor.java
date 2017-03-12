package android.net;

import android.content.Context;
import android.net.LinkProperties.ProvisioningChange;
import android.net.netlink.NetlinkConstants;
import android.net.netlink.NetlinkErrorMessage;
import android.net.netlink.NetlinkMessage;
import android.net.netlink.NetlinkSocket;
import android.net.netlink.RtNetlinkNeighborMessage;
import android.net.netlink.StructNdMsg;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.system.ErrnoException;
import android.system.NetlinkSocketAddress;
import android.system.OsConstants;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IpReachabilityMonitor {
    private static final boolean DBG = true;
    private static final String TAG = "IpReachabilityMonitor";
    private static final boolean VDBG = false;
    private final Callback mCallback;
    private final int mInterfaceIndex;
    private final String mInterfaceName;
    @GuardedBy("mLock")
    private Map<InetAddress, Short> mIpWatchList = new HashMap();
    @GuardedBy("mLock")
    private int mIpWatchListVersion;
    @GuardedBy("mLock")
    private LinkProperties mLinkProperties = new LinkProperties();
    private final Object mLock = new Object();
    private final NetlinkSocketObserver mNetlinkSocketObserver;
    private final Thread mObserverThread;
    @GuardedBy("mLock")
    private boolean mRunning;
    private final WakeLock mWakeLock;

    public interface Callback {
        void notifyLost(InetAddress inetAddress, String str);
    }

    private final class NetlinkSocketObserver implements Runnable {
        private static final String TAG = "NetlinkSocketObserver";
        private NetlinkSocket mSocket;

        private NetlinkSocketObserver() {
        }

        public void run() {
            Exception e;
            ByteBuffer byteBuffer;
            long whenMs;
            synchronized (IpReachabilityMonitor.this.mLock) {
                IpReachabilityMonitor.this.mRunning = true;
            }
            try {
                setupNetlinkSocket();
            } catch (ErrnoException e2) {
                e = e2;
                Log.e(TAG, "Failed to suitably initialize a netlink socket", e);
                synchronized (IpReachabilityMonitor.this.mLock) {
                    IpReachabilityMonitor.this.mRunning = false;
                }
                while (IpReachabilityMonitor.this.stillRunning()) {
                    try {
                        byteBuffer = recvKernelReply();
                        whenMs = SystemClock.elapsedRealtime();
                        if (byteBuffer == null) {
                            parseNetlinkMessageBuffer(byteBuffer, whenMs);
                        }
                    } catch (ErrnoException e3) {
                        Log.w(TAG, "ErrnoException: ", e3);
                    }
                }
                clearNetlinkSocket();
                synchronized (IpReachabilityMonitor.this.mLock) {
                    IpReachabilityMonitor.this.mRunning = false;
                }
            } catch (SocketException e4) {
                e = e4;
                Log.e(TAG, "Failed to suitably initialize a netlink socket", e);
                synchronized (IpReachabilityMonitor.this.mLock) {
                    IpReachabilityMonitor.this.mRunning = false;
                }
                while (IpReachabilityMonitor.this.stillRunning()) {
                    byteBuffer = recvKernelReply();
                    whenMs = SystemClock.elapsedRealtime();
                    if (byteBuffer == null) {
                        parseNetlinkMessageBuffer(byteBuffer, whenMs);
                    }
                }
                clearNetlinkSocket();
                synchronized (IpReachabilityMonitor.this.mLock) {
                    IpReachabilityMonitor.this.mRunning = false;
                }
            }
            while (IpReachabilityMonitor.this.stillRunning()) {
                byteBuffer = recvKernelReply();
                whenMs = SystemClock.elapsedRealtime();
                if (byteBuffer == null) {
                    parseNetlinkMessageBuffer(byteBuffer, whenMs);
                }
            }
            clearNetlinkSocket();
            synchronized (IpReachabilityMonitor.this.mLock) {
                IpReachabilityMonitor.this.mRunning = false;
            }
        }

        private void clearNetlinkSocket() {
            if (this.mSocket != null) {
                this.mSocket.close();
            }
        }

        private void setupNetlinkSocket() throws ErrnoException, SocketException {
            clearNetlinkSocket();
            this.mSocket = new NetlinkSocket(OsConstants.NETLINK_ROUTE);
            this.mSocket.bind(new NetlinkSocketAddress(0, OsConstants.RTMGRP_NEIGH));
        }

        private ByteBuffer recvKernelReply() throws ErrnoException {
            try {
                return this.mSocket.recvMessage(0);
            } catch (InterruptedIOException e) {
                return null;
            } catch (ErrnoException e2) {
                if (e2.errno != OsConstants.EAGAIN) {
                    throw e2;
                }
                return null;
            }
        }

        private void parseNetlinkMessageBuffer(ByteBuffer byteBuffer, long whenMs) {
            while (byteBuffer.remaining() > 0) {
                int position = byteBuffer.position();
                NetlinkMessage nlMsg = NetlinkMessage.parse(byteBuffer);
                if (nlMsg == null || nlMsg.getHeader() == null) {
                    byteBuffer.position(position);
                    Log.e(TAG, "unparsable netlink msg: " + NetlinkConstants.hexify(byteBuffer));
                    return;
                }
                int srcPortId = nlMsg.getHeader().nlmsg_pid;
                if (srcPortId != 0) {
                    Log.e(TAG, "non-kernel source portId: " + ((long) (srcPortId & -1)));
                    return;
                } else if (nlMsg instanceof NetlinkErrorMessage) {
                    Log.e(TAG, "netlink error: " + nlMsg);
                } else if (nlMsg instanceof RtNetlinkNeighborMessage) {
                    evaluateRtNetlinkNeighborMessage((RtNetlinkNeighborMessage) nlMsg, whenMs);
                } else {
                    Log.d(TAG, "non-rtnetlink neighbor msg: " + nlMsg);
                }
            }
        }

        private void evaluateRtNetlinkNeighborMessage(RtNetlinkNeighborMessage neighMsg, long whenMs) {
            StructNdMsg ndMsg = neighMsg.getNdHeader();
            if (ndMsg != null && ndMsg.ndm_ifindex == IpReachabilityMonitor.this.mInterfaceIndex) {
                InetAddress destination = neighMsg.getDestination();
                if (IpReachabilityMonitor.this.isWatching(destination)) {
                    short msgType = neighMsg.getHeader().nlmsg_type;
                    short nudState = ndMsg.ndm_state;
                    String eventMsg = "NeighborEvent{elapsedMs=" + whenMs + ", " + destination.getHostAddress() + ", " + "[" + NetlinkConstants.hexify(neighMsg.getLinkLayerAddress()) + "], " + NetlinkConstants.stringForNlMsgType(msgType) + ", " + StructNdMsg.stringForNudState(nudState) + "}";
                    Log.d(TAG, eventMsg);
                    synchronized (IpReachabilityMonitor.this.mLock) {
                        if (IpReachabilityMonitor.this.mIpWatchList.containsKey(destination)) {
                            IpReachabilityMonitor.this.mIpWatchList.put(destination, Short.valueOf(msgType == (short) 29 ? (short) 0 : nudState));
                        }
                    }
                    if (nudState == (short) 32) {
                        Log.w(TAG, "ALERT: " + eventMsg);
                        IpReachabilityMonitor.this.handleNeighborLost(eventMsg);
                    }
                }
            }
        }
    }

    public static boolean probeNeighbor(int ifIndex, InetAddress ip) {
        NetlinkSocket nlSocket;
        Throwable th;
        Throwable th2;
        String msgSnippet = "probing ip=" + ip.getHostAddress() + "%" + ifIndex;
        Log.d(TAG, msgSnippet);
        byte[] msg = RtNetlinkNeighborMessage.newNewNeighborMessage(1, ip, (short) 16, ifIndex, null);
        boolean returnValue = false;
        try {
            nlSocket = new NetlinkSocket(OsConstants.NETLINK_ROUTE);
            try {
                nlSocket.connectToKernel();
                nlSocket.sendMessage(msg, 0, msg.length, 300);
                ByteBuffer bytes = nlSocket.recvMessage(300);
                NetlinkMessage response = NetlinkMessage.parse(bytes);
                if (response == null || !(response instanceof NetlinkErrorMessage) || ((NetlinkErrorMessage) response).getNlMsgError() == null || ((NetlinkErrorMessage) response).getNlMsgError().error != 0) {
                    String errmsg;
                    if (bytes == null) {
                        errmsg = "null recvMessage";
                    } else if (response == null) {
                        bytes.position(0);
                        errmsg = "raw bytes: " + NetlinkConstants.hexify(bytes);
                    } else {
                        errmsg = response.toString();
                    }
                    Log.e(TAG, "Error " + msgSnippet + ", errmsg=" + errmsg);
                } else {
                    returnValue = true;
                }
                if (nlSocket != null) {
                    if (null != null) {
                        try {
                            nlSocket.close();
                        } catch (Throwable x2) {
                            null.addSuppressed(x2);
                        }
                    } else {
                        nlSocket.close();
                    }
                }
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
        } catch (ErrnoException e) {
            e = e;
        } catch (InterruptedIOException e2) {
            e = e2;
        } catch (SocketException e3) {
            e = e3;
        }
        return returnValue;
        throw th;
        Exception e4;
        Log.d(TAG, "Error " + msgSnippet, e4);
        return returnValue;
        if (nlSocket != null) {
            if (th22 != null) {
                try {
                    nlSocket.close();
                } catch (Throwable x22) {
                    th22.addSuppressed(x22);
                }
            } else {
                nlSocket.close();
            }
        }
        throw th;
    }

    public IpReachabilityMonitor(Context context, String ifName, Callback callback) throws IllegalArgumentException {
        Exception e;
        this.mInterfaceName = ifName;
        try {
            this.mInterfaceIndex = NetworkInterface.getByName(ifName).getIndex();
            this.mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, "IpReachabilityMonitor." + this.mInterfaceName);
            this.mCallback = callback;
            this.mNetlinkSocketObserver = new NetlinkSocketObserver();
            this.mObserverThread = new Thread(this.mNetlinkSocketObserver);
            this.mObserverThread.start();
        } catch (SocketException e2) {
            e = e2;
            throw new IllegalArgumentException("invalid interface '" + ifName + "': ", e);
        } catch (NullPointerException e3) {
            e = e3;
            throw new IllegalArgumentException("invalid interface '" + ifName + "': ", e);
        }
    }

    public void stop() {
        synchronized (this.mLock) {
            this.mRunning = false;
        }
        clearLinkProperties();
        this.mNetlinkSocketObserver.clearNetlinkSocket();
    }

    private String describeWatchList() {
        String delimiter = ", ";
        StringBuilder sb = new StringBuilder();
        synchronized (this.mLock) {
            sb.append("iface{" + this.mInterfaceName + "/" + this.mInterfaceIndex + "}, ");
            sb.append("v{" + this.mIpWatchListVersion + "}, ");
            sb.append("ntable=[");
            boolean firstTime = true;
            for (Entry<InetAddress, Short> entry : this.mIpWatchList.entrySet()) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(", ");
                }
                sb.append(((InetAddress) entry.getKey()).getHostAddress() + "/" + StructNdMsg.stringForNudState(((Short) entry.getValue()).shortValue()));
            }
            sb.append("]");
        }
        return sb.toString();
    }

    private boolean isWatching(InetAddress ip) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRunning && this.mIpWatchList.containsKey(ip);
        }
        return z;
    }

    private boolean stillRunning() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRunning;
        }
        return z;
    }

    private static boolean isOnLink(List<RouteInfo> routes, InetAddress ip) {
        for (RouteInfo route : routes) {
            if (!route.hasGateway() && route.matches(ip)) {
                return true;
            }
        }
        return false;
    }

    private short getNeighborStateLocked(InetAddress ip) {
        if (this.mIpWatchList.containsKey(ip)) {
            return ((Short) this.mIpWatchList.get(ip)).shortValue();
        }
        return (short) 0;
    }

    public void updateLinkProperties(LinkProperties lp) {
        if (this.mInterfaceName.equals(lp.getInterfaceName())) {
            synchronized (this.mLock) {
                this.mLinkProperties = new LinkProperties(lp);
                Map<InetAddress, Short> newIpWatchList = new HashMap();
                List<RouteInfo> routes = this.mLinkProperties.getRoutes();
                for (RouteInfo route : routes) {
                    if (route.hasGateway()) {
                        InetAddress gw = route.getGateway();
                        if (isOnLink(routes, gw)) {
                            newIpWatchList.put(gw, Short.valueOf(getNeighborStateLocked(gw)));
                        }
                    }
                }
                for (InetAddress nameserver : lp.getDnsServers()) {
                    if (isOnLink(routes, nameserver)) {
                        newIpWatchList.put(nameserver, Short.valueOf(getNeighborStateLocked(nameserver)));
                    }
                }
                this.mIpWatchList = newIpWatchList;
                this.mIpWatchListVersion++;
            }
            Log.d(TAG, "watch: " + describeWatchList());
            return;
        }
        Log.wtf(TAG, "requested LinkProperties interface '" + lp.getInterfaceName() + "' does not match: " + this.mInterfaceName);
    }

    public void clearLinkProperties() {
        synchronized (this.mLock) {
            this.mLinkProperties.clear();
            this.mIpWatchList.clear();
            this.mIpWatchListVersion++;
        }
        Log.d(TAG, "clear: " + describeWatchList());
    }

    private void handleNeighborLost(String msg) {
        InetAddress ip = null;
        synchronized (this.mLock) {
            LinkProperties whatIfLp = new LinkProperties(this.mLinkProperties);
            for (Entry<InetAddress, Short> entry : this.mIpWatchList.entrySet()) {
                if (((Short) entry.getValue()).shortValue() == (short) 32) {
                    ip = (InetAddress) entry.getKey();
                    for (RouteInfo route : this.mLinkProperties.getRoutes()) {
                        if (ip.equals(route.getGateway())) {
                            whatIfLp.removeRoute(route);
                        }
                    }
                    whatIfLp.removeDnsServer(ip);
                }
            }
            ProvisioningChange delta = LinkProperties.compareProvisioning(this.mLinkProperties, whatIfLp);
        }
        if (delta == ProvisioningChange.LOST_PROVISIONING) {
            String logMsg = "FAILURE: LOST_PROVISIONING, " + msg;
            Log.w(TAG, logMsg);
            if (this.mCallback != null) {
                this.mCallback.notifyLost(ip, logMsg);
            }
        }
    }

    public void probeAll() {
        Set<InetAddress> ipProbeList = new HashSet();
        synchronized (this.mLock) {
            ipProbeList.addAll(this.mIpWatchList.keySet());
        }
        if (!ipProbeList.isEmpty() && stillRunning()) {
            this.mWakeLock.acquire(getProbeWakeLockDuration());
        }
        for (InetAddress target : ipProbeList) {
            if (stillRunning()) {
                probeNeighbor(this.mInterfaceIndex, target);
            } else {
                return;
            }
        }
    }

    private long getProbeWakeLockDuration() {
        return 3500;
    }
}
