package android.net;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.Global;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class DnsPinger extends Handler {
    private static final int ACTION_CANCEL_ALL_PINGS = 327683;
    private static final int ACTION_LISTEN_FOR_RESPONSE = 327682;
    private static final int ACTION_PING_DNS = 327681;
    private static final int ACTION_PING_DNS_SPECIFIC = 327684;
    private static final int BASE = 327680;
    public static final int CACHED_RESULT = 1;
    private static final boolean DBG;
    public static final int DNS_PING_RESULT = 327680;
    public static final int DNS_PING_RESULT_SPECIFIC = 327685;
    private static final int DNS_PORT = 53;
    private static final int DNS_RESPONSE_BUFFER_SIZE = 512;
    private static HashMap<String, DnsResult> MostRecentDnsResultMap = new HashMap();
    public static final int NO_INTERNET = -3;
    public static final int PRIVATE_IP_ADDRESS = 2;
    private static final int RECEIVE_POLL_INTERVAL_MS = 200;
    public static final int REQUESTED_URL_ALREADY_IP_ADDRESS = 3;
    private static final boolean SMARTCM_DBG = false;
    public static final int SOCKET_EXCEPTION = -2;
    private static final int SOCKET_TIMEOUT_MS = 1;
    public static final int TIMEOUT = -1;
    private static final AtomicInteger sCounter = new AtomicInteger();
    private static final Random sRandom = new Random();
    HashMap<String, List<DnsResult>> DnsResultMap = new HashMap();
    private String TAG;
    private List<ActivePing> mActivePings = new ArrayList();
    private final int mConnectionType;
    private ConnectivityManager mConnectivityManager = null;
    private final Context mContext;
    private AtomicInteger mCurrentToken = new AtomicInteger();
    private final ArrayList<InetAddress> mDefaultDns;
    private byte[] mDnsQuery;
    private int mEventCounter;
    LinkProperties mLp = null;
    private final Handler mTarget;

    private class ActivePing {
        int internalId;
        short packetId;
        Integer result;
        DatagramSocket socket;
        long start;
        int timeout;
        String url;

        private ActivePing() {
            this.start = SystemClock.elapsedRealtime();
        }
    }

    private class DnsArg {
        InetAddress dns;
        int seq;
        String targetUrl;

        DnsArg(InetAddress d, int s, String u) {
            this.dns = d;
            this.seq = s;
            this.targetUrl = u;
        }
    }

    private class DnsResult {
        InetAddress resultIp;
        long ttl;

        DnsResult(InetAddress ip, long t) {
            this.resultIp = ip;
            this.ttl = t;
        }
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public DnsPinger(Context context, String TAG, Looper looper, Handler target, int connectionType) {
        super(looper);
        this.TAG = TAG;
        this.mContext = context;
        this.mTarget = target;
        this.mConnectionType = connectionType;
        if (ConnectivityManager.isNetworkTypeValid(connectionType)) {
            this.mDefaultDns = new ArrayList();
            this.mDefaultDns.add(getDefaultDns());
            this.mEventCounter = 0;
            return;
        }
        throw new IllegalArgumentException("Invalid connectionType in constructor: " + connectionType);
    }

    public void handleMessage(Message msg) {
        DatagramPacket datagramPacket;
        switch (msg.what) {
            case ACTION_PING_DNS /*327681*/:
            case ACTION_PING_DNS_SPECIFIC /*327684*/:
                DnsArg dnsArg = msg.obj;
                if (dnsArg.seq == this.mCurrentToken.get()) {
                    try {
                        DnsPinger dnsPinger = this;
                        ActivePing activePing = new ActivePing();
                        InetAddress dnsAddress = dnsArg.dns;
                        updateDnsQuery(dnsArg.targetUrl);
                        activePing.internalId = msg.arg1;
                        activePing.timeout = msg.arg2;
                        activePing.url = dnsArg.targetUrl;
                        activePing.socket = new DatagramSocket();
                        activePing.socket.setSoTimeout(1);
                        try {
                            activePing.socket.setNetworkInterface(NetworkInterface.getByName(getCurrentLinkProperties().getInterfaceName()));
                        } catch (IOException e) {
                            loge("sendDnsPing::Error binding to socket " + e);
                        }
                        if (msg.what == ACTION_PING_DNS) {
                            activePing.packetId = (short) (sRandom.nextInt() << 1);
                        } else {
                            activePing.packetId = (short) ((sRandom.nextInt() << 1) + 1);
                        }
                        byte[] buf = (byte[]) this.mDnsQuery.clone();
                        buf[0] = (byte) (activePing.packetId >> 8);
                        buf[1] = (byte) activePing.packetId;
                        datagramPacket = new DatagramPacket(buf, buf.length, dnsAddress, 53);
                        if (DBG) {
                            log("Sending a ping " + activePing.internalId + " to " + dnsAddress.getHostAddress() + " with packetId " + activePing.packetId + ".");
                        }
                        activePing.socket.send(datagramPacket);
                        this.mActivePings.add(activePing);
                        this.mEventCounter++;
                        sendMessageDelayed(obtainMessage(ACTION_LISTEN_FOR_RESPONSE, this.mEventCounter, 0), 200);
                        return;
                    } catch (IOException e2) {
                        if (msg.what == ACTION_PING_DNS) {
                            sendResponse(msg.arg1, -9998, -2);
                            return;
                        }
                        sendResponse(msg.arg1, -9999, -2);
                        return;
                    }
                }
                return;
            case ACTION_LISTEN_FOR_RESPONSE /*327682*/:
                if (msg.arg1 == this.mEventCounter) {
                    ActivePing curPing;
                    for (ActivePing curPing2 : this.mActivePings) {
                        try {
                            byte[] responseBuf = new byte[512];
                            datagramPacket = new DatagramPacket(responseBuf, 512);
                            curPing2.socket.receive(datagramPacket);
                            boolean isUsableResponse = false;
                            if (responseBuf[0] == ((byte) (curPing2.packetId >> 8)) && responseBuf[1] == ((byte) curPing2.packetId)) {
                                isUsableResponse = true;
                            } else {
                                if (DBG) {
                                    log("response ID doesn't match with query ID.");
                                }
                                for (ActivePing activePingForIdCheck : this.mActivePings) {
                                    if (responseBuf[0] == ((byte) (activePingForIdCheck.packetId >> 8)) && responseBuf[1] == ((byte) activePingForIdCheck.packetId) && curPing2.url != null && curPing2.url.equals(activePingForIdCheck.url)) {
                                        log("response ID didn't match, but DNS response is usable.");
                                        isUsableResponse = true;
                                    }
                                }
                            }
                            if (!isUsableResponse) {
                                log("response ID didn't match, ignoring packet");
                            } else if ((responseBuf[3] & 15) != 0 || (responseBuf[6] == (byte) 0 && responseBuf[7] == (byte) 0)) {
                                if (DBG) {
                                    loge("Reply code is not 0(No Error) or Answer Record Count is 0");
                                }
                                curPing2.result = Integer.valueOf(-3);
                            } else {
                                curPing2.result = Integer.valueOf((int) (SystemClock.elapsedRealtime() - curPing2.start));
                                updateDnsDB((byte[]) responseBuf.clone(), datagramPacket.getLength(), curPing2.url);
                                if (isDnsResponsePrivateAddress(curPing2.url)) {
                                    curPing2.result = Integer.valueOf(2);
                                }
                            }
                        } catch (SocketTimeoutException e3) {
                        } catch (Exception e4) {
                            if (DBG) {
                                log("DnsPinger.pingDns got socket exception: " + e4);
                            }
                            curPing2.result = Integer.valueOf(-2);
                        }
                    }
                    Iterator<ActivePing> iter = this.mActivePings.iterator();
                    while (iter.hasNext()) {
                        curPing2 = (ActivePing) iter.next();
                        if (curPing2.result != null) {
                            if ((curPing2.packetId & 1) != 1 || curPing2.result.intValue() <= 0) {
                                sendResponse(curPing2.internalId, curPing2.packetId, curPing2.result.intValue());
                            } else {
                                List<DnsResult> list = (List) this.DnsResultMap.get(curPing2.url);
                                if (list == null || list.size() <= 0) {
                                    if (DBG) {
                                        Log.e(this.TAG, "There are no results about " + curPing2.url);
                                    }
                                    sendResponse(curPing2.internalId, curPing2.packetId, -2);
                                } else {
                                    try {
                                        sendResponse(curPing2.internalId, curPing2.packetId, curPing2.result.intValue(), curPing2.url, sRandom.nextInt(((List) this.DnsResultMap.get(curPing2.url)).size()), 0);
                                    } catch (Exception e5) {
                                    }
                                }
                            }
                            curPing2.socket.close();
                            iter.remove();
                        } else if (SystemClock.elapsedRealtime() > curPing2.start + ((long) curPing2.timeout)) {
                            sendResponse(curPing2.internalId, curPing2.packetId, -1, curPing2.url);
                            curPing2.socket.close();
                            iter.remove();
                        }
                    }
                    if (!this.mActivePings.isEmpty()) {
                        sendMessageDelayed(obtainMessage(ACTION_LISTEN_FOR_RESPONSE, this.mEventCounter, 0), 200);
                        return;
                    }
                    return;
                }
                return;
            case ACTION_CANCEL_ALL_PINGS /*327683*/:
                for (ActivePing activePing2 : this.mActivePings) {
                    activePing2.socket.close();
                }
                this.mActivePings.clear();
                return;
            default:
                return;
        }
    }

    public List<InetAddress> getDnsList() {
        LinkProperties curLinkProps = getCurrentLinkProperties();
        if (curLinkProps == null) {
            loge("getCurLinkProperties:: LP for type" + this.mConnectionType + " is null!");
            return this.mDefaultDns;
        }
        Collection<InetAddress> dnses = curLinkProps.getDnsServers();
        if (dnses != null && dnses.size() != 0) {
            return new ArrayList(dnses);
        }
        loge("getDns::LinkProps has null dns - returning default");
        return this.mDefaultDns;
    }

    public int pingDnsAsync(InetAddress dns, int timeout, int delay) {
        int id = sCounter.incrementAndGet();
        updateDnsResultMap("www.google.com");
        sendMessageDelayed(obtainMessage(ACTION_PING_DNS, id, timeout, new DnsArg(dns, this.mCurrentToken.get(), "www.google.com")), (long) delay);
        return id;
    }

    public int pingDnsAsyncSpecificForce(InetAddress dns, int timeout, int delay, String url) {
        int id = sCounter.incrementAndGet();
        sendMessageDelayed(obtainMessage(ACTION_PING_DNS_SPECIFIC, id, timeout, new DnsArg(dns, this.mCurrentToken.get(), url)), (long) delay);
        return id;
    }

    public int pingDnsAsyncSpecific(InetAddress dns, int timeout, int delay, String url) {
        int id = sCounter.incrementAndGet();
        try {
            InetAddress addr = NetworkUtils.numericToInetAddress(url);
            if (DBG) {
                log("URL is already an IP address. " + url);
            }
            this.mTarget.sendMessageDelayed(obtainMessage(DNS_PING_RESULT_SPECIFIC, id, 3, addr), 50);
        } catch (IllegalArgumentException e) {
            if (this.DnsResultMap.get(url) == null) {
                if (DBG) {
                    log("DNS Result Hashmap - NO HIT!!! SENDING DNS QUERY!  " + url);
                }
                sendMessageDelayed(obtainMessage(ACTION_PING_DNS_SPECIFIC, id, timeout, new DnsArg(dns, this.mCurrentToken.get(), url)), (long) delay);
            } else {
                updateDnsResultMap(url);
                int numOfResults = 0;
                if (this.DnsResultMap.get(url) != null) {
                    numOfResults = ((List) this.DnsResultMap.get(url)).size();
                }
                if (numOfResults == 0) {
                    if (DBG) {
                        log("DNS Result Hashmap - HIT!!! BUT NO RESULTS   (" + numOfResults + ")" + url);
                    }
                    sendMessageDelayed(obtainMessage(ACTION_PING_DNS_SPECIFIC, id, timeout, new DnsArg(dns, this.mCurrentToken.get(), url)), (long) delay);
                } else {
                    if (DBG) {
                        log("DNS Result Hashmap - HIT!!! USE PREVIOUS RESULT   (" + numOfResults + ")" + url);
                    }
                    sendResponse(id, -11111, 1, url, sRandom.nextInt(numOfResults), 50);
                }
            }
        }
        return id;
    }

    public void clear() {
        this.DnsResultMap.clear();
    }

    public void cancelPings() {
        this.mCurrentToken.incrementAndGet();
        obtainMessage(ACTION_CANCEL_ALL_PINGS).sendToTarget();
    }

    private void sendResponse(int internalId, int externalId, int responseVal) {
        if (DBG) {
            log("Responding to packet " + internalId + " externalId " + externalId + " and val " + responseVal);
        }
        if ((externalId & 1) == 1) {
            this.mTarget.sendMessage(obtainMessage(DNS_PING_RESULT_SPECIFIC, internalId, responseVal, (InetAddress) null));
        } else {
            this.mTarget.sendMessage(obtainMessage(327680, internalId, responseVal));
        }
    }

    private void sendResponse(int internalId, int externalId, int responseVal, String url, int index, int delay) {
        if (DBG) {
            log("Responding to packet " + internalId + " externalId " + externalId + " and val " + responseVal);
            log("SPECIFIC DNS PING: url - " + url + ", responseVal : " + responseVal);
        }
        try {
            this.mTarget.sendMessageDelayed(obtainMessage(DNS_PING_RESULT_SPECIFIC, internalId, responseVal, ((DnsResult) ((List) this.DnsResultMap.get(url)).get(index)).resultIp), (long) delay);
        } catch (Exception e) {
        }
    }

    private void sendResponse(int internalId, int externalId, int responseVal, String url) {
        if (DBG) {
            log("Responding to packet " + internalId + " externalId " + externalId + " val " + responseVal + " url " + url);
        }
        InetAddress resultIp = null;
        if (responseVal == -1 && MostRecentDnsResultMap.containsKey(url)) {
            resultIp = ((DnsResult) MostRecentDnsResultMap.get(url)).resultIp;
            if (DBG) {
                log("Sending most recent DNS result, " + resultIp.toString() + ", expired " + (System.currentTimeMillis() - ((DnsResult) MostRecentDnsResultMap.get(url)).ttl) + " msec ago.");
            }
        }
        if ((externalId & 1) == 1) {
            this.mTarget.sendMessage(obtainMessage(DNS_PING_RESULT_SPECIFIC, internalId, responseVal, resultIp));
        } else {
            this.mTarget.sendMessage(obtainMessage(327680, internalId, responseVal));
        }
    }

    public void setCurrentLinkProperties(LinkProperties lp) {
        if (lp != null) {
            Log.d(this.TAG, "setCurrentLinkProperties: lp=" + lp);
        }
        this.mLp = lp;
    }

    private LinkProperties getCurrentLinkProperties() {
        if (this.mLp != null) {
            return this.mLp;
        }
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return this.mConnectivityManager.getLinkProperties(this.mConnectionType);
    }

    private InetAddress getDefaultDns() {
        String dns = Global.getString(this.mContext.getContentResolver(), Global.DEFAULT_DNS_SERVER);
        if (dns == null || dns.length() == 0) {
            dns = this.mContext.getResources().getString(17039408);
        }
        try {
            return NetworkUtils.numericToInetAddress(dns);
        } catch (IllegalArgumentException e) {
            loge("getDefaultDns::malformed default dns address");
            return null;
        }
    }

    private void updateDnsQuery(String url) {
        byte[] header = new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        byte[] trailer = new byte[]{(byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1};
        int length = url.length();
        byte blockSize = (byte) 0;
        StringBuilder mid = new StringBuilder();
        mid.append('.');
        mid.append(url);
        byte[] middle = (byte[]) mid.toString().getBytes().clone();
        for (int i = length; i >= 0; i--) {
            if (middle[i] == (byte) 46) {
                middle[i] = blockSize;
                blockSize = (byte) 0;
            } else {
                blockSize = (byte) (blockSize + 1);
            }
        }
        byte[] query = new byte[(length + 18)];
        System.arraycopy(header, 0, query, 0, 12);
        System.arraycopy(middle, 0, query, 12, length + 1);
        System.arraycopy(trailer, 0, query, length + 13, 5);
        this.mDnsQuery = (byte[]) query.clone();
    }

    private void updateDnsDB(byte[] buf, int length, String reqUrl) {
        List<DnsResult> mDnsResultList;
        StringBuilder dbgShowResult;
        int i;
        long currTime = System.currentTimeMillis();
        int currPos = 0 + 1;
        int packetId = ((buf[0] & 255) << 8) + (buf[currPos] & 255);
        currPos++;
        currPos++;
        int flag = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
        currPos++;
        currPos++;
        int numOfQuestion = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
        currPos++;
        currPos++;
        int numOfAnswerRR = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
        currPos++;
        currPos++;
        int numOfAthorityRR = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
        currPos++;
        currPos++;
        int numOfAdditionalRR = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
        StringBuilder url = new StringBuilder();
        while (true) {
            currPos++;
            if (buf[currPos] == (byte) 0) {
                break;
            }
            for (byte i2 = (byte) 1; i2 <= buf[currPos]; i2++) {
                url.append(String.format("%c", new Object[]{Byte.valueOf(buf[currPos + i2])}));
            }
            url.append('.');
            currPos += buf[currPos];
        }
        url.deleteCharAt(url.length() - 1);
        if (url.toString().equals(reqUrl)) {
            currPos += 4;
            mDnsResultList = new ArrayList();
            dbgShowResult = new StringBuilder();
        } else {
            currPos += 4;
            mDnsResultList = new ArrayList();
            dbgShowResult = new StringBuilder();
        }
        for (i = 0; i < numOfAnswerRR && currPos + 12 < 512; i++) {
            currPos = (currPos + 2) + 1;
            currPos++;
            int rrType = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
            currPos++;
            currPos++;
            int rrClass = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
            currPos++;
            currPos++;
            currPos++;
            currPos++;
            int rrTtl = ((((buf[currPos] & 255) << 24) + ((buf[currPos] & 255) << 16)) + ((buf[currPos] & 255) << 8)) + (buf[currPos] & 255);
            currPos++;
            currPos++;
            int rrLength = ((buf[currPos] & 255) << 8) + (buf[currPos] & 255);
            if (currPos + rrLength >= 512) {
                break;
            }
            if (rrType == 1) {
                StringBuilder ipString = new StringBuilder();
                currPos++;
                ipString.append(Integer.toString(buf[currPos] & 255));
                ipString.append('.');
                currPos++;
                ipString.append(Integer.toString(buf[currPos] & 255));
                ipString.append('.');
                currPos++;
                ipString.append(Integer.toString(buf[currPos] & 255));
                ipString.append('.');
                currPos++;
                ipString.append(Integer.toString(buf[currPos] & 255));
                mDnsResultList.add(new DnsResult(NetworkUtils.numericToInetAddress(ipString.toString()), ((long) (rrTtl * 1000)) + currTime));
                dbgShowResult.append("[");
                dbgShowResult.append(ipString.toString());
                dbgShowResult.append("] ");
            } else {
                StringBuilder rrData = new StringBuilder();
                for (int j = 0; j < rrLength; j++) {
                    rrData.append('[');
                    Object[] objArr = new Object[1];
                    currPos++;
                    objArr[0] = Byte.valueOf(buf[currPos]);
                    rrData.append(String.format("%02X", objArr));
                    rrData.append(']');
                }
            }
        }
        if (DBG) {
            log("DNS Result - " + url.toString() + ", " + dbgShowResult.toString());
        }
        if (this.DnsResultMap.containsKey(reqUrl)) {
            for (i = 0; i < mDnsResultList.size(); i++) {
                ((List) this.DnsResultMap.get(reqUrl)).add(mDnsResultList.get(i));
            }
        } else {
            this.DnsResultMap.put(reqUrl, mDnsResultList);
        }
        if (!isDnsResponsePrivateAddress(reqUrl)) {
            MostRecentDnsResultMap.put(reqUrl, mDnsResultList.get(0));
            for (String str : MostRecentDnsResultMap.keySet()) {
            }
        }
        if (DBG) {
            log("Hashmap DnsResultMap contains " + this.DnsResultMap.size() + " entries, url: " + reqUrl + " - " + ((List) this.DnsResultMap.get(reqUrl)).size() + " IPs");
        }
    }

    private void updateDnsResultMap(String url) {
        List<DnsResult> mDnsResultList = (List) this.DnsResultMap.get(url);
        long currTime = System.currentTimeMillis();
        if (mDnsResultList != null) {
            for (int i = mDnsResultList.size() - 1; i >= 0; i--) {
                int ipByte1st = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[0] & 255;
                int ipByte2nd = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[1] & 255;
                int ipByte3rd = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[2] & 255;
                int ipByte4th = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[3] & 255;
                if (ipByte1st == 10 || ((ipByte1st == 192 && ipByte2nd == 168) || ((ipByte1st == 172 && ipByte2nd >= 16 && ipByte2nd <= 31) || (ipByte1st == 1 && ipByte2nd == 33 && ipByte3rd == 203 && ipByte4th == 39)))) {
                    mDnsResultList.remove(i);
                } else if (currTime > ((DnsResult) mDnsResultList.get(i)).ttl) {
                    mDnsResultList.remove(i);
                }
            }
        }
    }

    private boolean isDnsResponsePrivateAddress(String url) {
        List<DnsResult> mDnsResultList = (List) this.DnsResultMap.get(url);
        if (mDnsResultList != null) {
            for (int i = mDnsResultList.size() - 1; i >= 0; i--) {
                int ipByte1st = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[0] & 255;
                int ipByte2nd = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[1] & 255;
                int ipByte3rd = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[2] & 255;
                int ipByte4th = ((DnsResult) mDnsResultList.get(i)).resultIp.getAddress()[3] & 255;
                if (ipByte1st == 10 || ((ipByte1st == 192 && ipByte2nd == 168) || ((ipByte1st == 172 && ipByte2nd >= 16 && ipByte2nd <= 31) || (ipByte1st == 1 && ipByte2nd == 33 && ipByte3rd == 203 && ipByte4th == 39)))) {
                    if (DBG) {
                        log(url + " - Dns Response with Private Network IP Address !!! - " + ipByte1st + "." + ipByte2nd + "." + ipByte3rd + "." + ipByte4th);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void log(String s) {
        Log.d(this.TAG, s);
    }

    private void loge(String s) {
        Log.e(this.TAG, s);
    }
}
