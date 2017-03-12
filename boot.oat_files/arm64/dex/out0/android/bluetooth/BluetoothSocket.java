package android.bluetooth;

import android.net.LocalSocket;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.sec.enterprise.BluetoothUtils;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.util.Log;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import java.util.UUID;

public final class BluetoothSocket implements Closeable {
    static final int BTSOCK_FLAG_NO_SDP = 4;
    private static final boolean DBG = Log.isLoggable(TAG, 3);
    static final int EADDRINUSE = 98;
    static final int EBADFD = 77;
    static final int MAX_L2CAP_PACKAGE_SIZE = 65535;
    public static final int MAX_RFCOMM_CHANNEL = 30;
    private static int PROXY_CONNECTION_TIMEOUT = 5000;
    static final int SEC_FLAG_AUTH = 2;
    static final int SEC_FLAG_AUTH_16_DIGIT = 16;
    static final int SEC_FLAG_AUTH_MITM = 8;
    static final int SEC_FLAG_ENCRYPT = 1;
    private static int SOCK_SIGNAL_SIZE = 20;
    private static final String TAG = "BluetoothSocket";
    public static final int TYPE_L2CAP = 3;
    public static final int TYPE_RFCOMM = 1;
    public static final int TYPE_SCO = 2;
    public static final int TYPE_VENDOR_HCI = 4;
    private static final boolean VDBG = Log.isLoggable(TAG, 2);
    private String mAddress;
    private final boolean mAuth;
    private boolean mAuthMitm;
    private BluetoothDevice mDevice;
    private final boolean mEncrypt;
    private boolean mExcludeSdp;
    private int mFd;
    private final BluetoothInputStream mInputStream;
    private ByteBuffer mL2capBuffer;
    private int mMaxRxPacketSize;
    private int mMaxTxPacketSize;
    private boolean mMin16DigitPin;
    private final BluetoothOutputStream mOutputStream;
    private ParcelFileDescriptor mPfd;
    private int mPort;
    private String mServiceName;
    private LocalSocket mSocket;
    private InputStream mSocketIS;
    private OutputStream mSocketOS;
    private volatile SocketState mSocketState;
    private final int mType;
    private final ParcelUuid mUuid;

    private enum SocketState {
        INIT,
        CONNECTED,
        LISTENING,
        CLOSED
    }

    BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, BluetoothDevice device, int port, ParcelUuid uuid) throws IOException {
        this(type, fd, auth, encrypt, device, port, uuid, false, false);
    }

    BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, BluetoothDevice device, int port, ParcelUuid uuid, boolean mitm, boolean min16DigitPin) throws IOException {
        this.mExcludeSdp = false;
        this.mAuthMitm = false;
        this.mMin16DigitPin = false;
        this.mL2capBuffer = null;
        this.mMaxTxPacketSize = 0;
        this.mMaxRxPacketSize = 0;
        if (VDBG) {
            Log.d(TAG, "Creating new BluetoothSocket of type: " + type);
        }
        if (type == 1 && uuid == null && fd == -1 && port != -2 && (port < 1 || port > 30)) {
            throw new IOException("Invalid RFCOMM channel: " + port);
        }
        if (uuid != null) {
            this.mUuid = uuid;
        } else {
            this.mUuid = new ParcelUuid(new UUID(0, 0));
        }
        this.mType = type;
        this.mAuth = auth;
        this.mAuthMitm = mitm;
        this.mMin16DigitPin = min16DigitPin;
        this.mEncrypt = encrypt;
        this.mDevice = device;
        this.mPort = port;
        this.mFd = fd;
        this.mSocketState = SocketState.INIT;
        if (device == null) {
            this.mAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
        } else {
            this.mAddress = device.getAddress();
        }
        this.mInputStream = new BluetoothInputStream(this);
        this.mOutputStream = new BluetoothOutputStream(this);
        BluetoothUtils.bluetoothSocketLog("Socket Created", this.mDevice, this.mPort, this.mType);
    }

    private BluetoothSocket(BluetoothSocket s) {
        this.mExcludeSdp = false;
        this.mAuthMitm = false;
        this.mMin16DigitPin = false;
        this.mL2capBuffer = null;
        this.mMaxTxPacketSize = 0;
        this.mMaxRxPacketSize = 0;
        if (VDBG) {
            Log.d(TAG, "Creating new Private BluetoothSocket of type: " + s.mType);
        }
        this.mUuid = s.mUuid;
        this.mType = s.mType;
        this.mAuth = s.mAuth;
        this.mEncrypt = s.mEncrypt;
        this.mPort = s.mPort;
        this.mInputStream = new BluetoothInputStream(this);
        this.mOutputStream = new BluetoothOutputStream(this);
        this.mMaxRxPacketSize = s.mMaxRxPacketSize;
        this.mMaxTxPacketSize = s.mMaxTxPacketSize;
        this.mServiceName = s.mServiceName;
        this.mExcludeSdp = s.mExcludeSdp;
        this.mAuthMitm = s.mAuthMitm;
        this.mMin16DigitPin = s.mMin16DigitPin;
    }

    private BluetoothSocket acceptSocket(String RemoteAddr) throws IOException {
        BluetoothSocket as = new BluetoothSocket(this);
        as.mSocketState = SocketState.CONNECTED;
        FileDescriptor[] fds = this.mSocket.getAncillaryFileDescriptors();
        if (DBG) {
            Log.d(TAG, "socket fd passed by stack  fds: " + fds);
        }
        if (fds == null || fds.length != 1) {
            Log.e(TAG, "socket fd passed from stack failed, fds: " + fds);
            as.close();
            throw new IOException("bt socket acept failed");
        }
        as.mPfd = new ParcelFileDescriptor(fds[0]);
        as.mSocket = new LocalSocket(fds[0]);
        as.mSocketIS = as.mSocket.getInputStream();
        as.mSocketOS = as.mSocket.getOutputStream();
        as.mAddress = RemoteAddr;
        as.mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(RemoteAddr);
        if (as.mDevice != null) {
            as.mDevice.setRfcommConnected(true);
        }
        return as;
    }

    private BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, String address, int port) throws IOException {
        this(type, fd, auth, encrypt, new BluetoothDevice(address), port, null, false, false);
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private int getSecurityFlags() {
        int flags = 0;
        if (this.mAuth) {
            flags = 0 | 2;
        }
        if (this.mEncrypt) {
            flags |= 1;
        }
        if (this.mExcludeSdp) {
            flags |= 4;
        }
        if (this.mAuthMitm) {
            flags |= 8;
        }
        if (this.mMin16DigitPin) {
            return flags | 16;
        }
        return flags;
    }

    public BluetoothDevice getRemoteDevice() {
        return this.mDevice;
    }

    public InputStream getInputStream() throws IOException {
        if (BluetoothUtils.isSocketAllowedBySecurityPolicy(this.mDevice, this.mPort, this.mType, this.mUuid)) {
            if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0)) {
                int userId = Process.myUserHandle().getIdentifier();
                Log.d(TAG, "getInputStream(): myUserId = " + userId);
                if (userId >= 100 && userId <= 200) {
                    boolean isBtEnabled = false;
                    IEDMProxy lService = EDMProxyServiceHelper.getService();
                    if (lService != null) {
                        try {
                            isBtEnabled = lService.isKnoxBluetoothEnabled(userId);
                        } catch (RemoteException re) {
                            Log.w(TAG, "getInputStream(): isKnoxBluetoothEnabled on EDMProxy failed! ", re);
                        }
                    }
                    if (!isBtEnabled) {
                        Log.d(TAG, "getInputStream(): Bluetooth for KNOX blocked by MDM policy: userId = " + userId);
                        throw new IOException("Knox Permission Denied");
                    }
                }
            }
            if (!WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("service.bt.security.policy.mode")) || BluetoothUuid.Handsfree_AG.toString().equals(this.mUuid.toString()) || BluetoothUuid.HSP_AG.toString().equals(this.mUuid.toString())) {
                return this.mInputStream;
            }
            Log.d(TAG, "Data transfer is not allowed by IT policy - getInputStream()");
            throw new IOException("Permission Denied");
        }
        Log.d(TAG, "Data transfer is not allowed by MDM policy - getInputStream()");
        throw new IOException("Permission Denied");
    }

    public OutputStream getOutputStream() throws IOException {
        if (BluetoothUtils.isSocketAllowedBySecurityPolicy(this.mDevice, this.mPort, this.mType, this.mUuid)) {
            if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0)) {
                int userId = Process.myUserHandle().getIdentifier();
                Log.d(TAG, "getOutputStream(): myUserId = " + userId);
                if (userId >= 100 && userId <= 200) {
                    boolean isBtEnabled = false;
                    IEDMProxy lService = EDMProxyServiceHelper.getService();
                    if (lService != null) {
                        try {
                            isBtEnabled = lService.isKnoxBluetoothEnabled(userId);
                        } catch (RemoteException re) {
                            Log.w(TAG, "getOutputStream(): isKnoxBluetoothEnabled on EDMProxy failed! ", re);
                        }
                    }
                    if (!isBtEnabled) {
                        Log.d(TAG, "getOutputStream(): Bluetooth for KNOX blocked by MDM policy: userId = " + userId);
                        throw new IOException("Knox Permission Denied");
                    }
                }
            }
            if (!WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("service.bt.security.policy.mode")) || BluetoothUuid.Handsfree_AG.toString().equals(this.mUuid.toString()) || BluetoothUuid.HSP_AG.toString().equals(this.mUuid.toString())) {
                return this.mOutputStream;
            }
            Log.d(TAG, "Data transfer is not allowed by IT policy - getInputStream()");
            throw new IOException("Permission Denied");
        }
        Log.d(TAG, "Data transfer is not allowed by MDM policy - getOutputStream()");
        throw new IOException("Permission Denied");
    }

    public boolean isConnected() {
        return this.mSocketState == SocketState.CONNECTED;
    }

    void setServiceName(String name) {
        this.mServiceName = name;
    }

    public void connect() throws IOException {
        if (this.mDevice == null) {
            throw new IOException("Connect is called on null device");
        } else if (BluetoothUtils.isSocketAllowedBySecurityPolicy(this.mDevice, this.mPort, this.mType, this.mUuid)) {
            if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0)) {
                int userId = Process.myUserHandle().getIdentifier();
                Log.d(TAG, "connect(): myUserId = " + userId);
                if (userId >= 100 && userId <= 200) {
                    boolean isBtEnabled = false;
                    IEDMProxy lService = EDMProxyServiceHelper.getService();
                    if (lService != null) {
                        try {
                            isBtEnabled = lService.isKnoxBluetoothEnabled(userId);
                        } catch (RemoteException re) {
                            Log.w(TAG, "connect(): isKnoxBluetoothEnabled on EDMProxy failed! ", re);
                        }
                    }
                    if (!isBtEnabled) {
                        Log.d(TAG, "connect(): Bluetooth for KNOX blocked by MDM policy: userId = " + userId);
                        throw new IOException("Knox Permission Denied");
                    }
                }
            }
            if (!WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("service.bt.security.policy.mode")) || BluetoothUuid.Handsfree_AG.toString().equals(this.mUuid.toString()) || BluetoothUuid.HSP_AG.toString().equals(this.mUuid.toString())) {
                try {
                    if (this.mSocketState == SocketState.CLOSED) {
                        throw new IOException("socket closed");
                    }
                    IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
                    if (bluetoothProxy == null) {
                        throw new IOException("Bluetooth is off");
                    }
                    this.mPfd = bluetoothProxy.connectSocket(this.mDevice, this.mType, this.mUuid, this.mPort, getSecurityFlags());
                    synchronized (this) {
                        if (DBG) {
                            Log.d(TAG, "connect(), SocketState: " + this.mSocketState + ", mPfd: " + this.mPfd);
                        }
                        if (this.mSocketState == SocketState.CLOSED) {
                            throw new IOException("socket closed");
                        } else if (this.mPfd == null) {
                            throw new IOException("bt socket connect failed");
                        } else {
                            this.mSocket = new LocalSocket(this.mPfd.getFileDescriptor());
                            this.mSocketIS = this.mSocket.getInputStream();
                            this.mSocketOS = this.mSocket.getOutputStream();
                        }
                    }
                    int channel = readInt(this.mSocketIS);
                    if (channel <= 0) {
                        throw new IOException("bt socket connect failed");
                    }
                    this.mPort = channel;
                    waitSocketSignal(this.mSocketIS);
                    synchronized (this) {
                        if (this.mSocketState == SocketState.CLOSED) {
                            throw new IOException("bt socket closed");
                        }
                        this.mSocketState = SocketState.CONNECTED;
                        this.mDevice.setRfcommConnected(true);
                        BluetoothDump.BtLog("[0006]{000A}(27::" + this.mSocketState + ")(13::" + this.mUuid + ")(01::" + this.mDevice.getAddressForLog() + ")");
                        BluetoothUtils.bluetoothSocketLog("Socket Connected", this.mDevice, this.mPort, this.mType);
                    }
                } catch (RemoteException e) {
                    BluetoothDump.BtLog("[0006]{000A}(27::" + this.mSocketState + ")(13::" + this.mUuid + ")(01::" + this.mDevice.getAddressForLog() + ")");
                    Log.e(TAG, Log.getStackTraceString(new Throwable()));
                    throw new IOException("unable to send RPC: " + e.getMessage());
                } catch (IOException e2) {
                    BluetoothDump.BtLog("[0006]{000A}(2B::" + e2.getMessage() + ")");
                    throw e2;
                }
            } else if (DBG) {
                Log.d(TAG, "connect not allowed ; IT Policy HandsfreeOnly");
            }
        } else {
            Log.d(TAG, "Port " + this.mPort + " is not allowed by MDM policy");
            throw new IOException("Permission Denied");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    int bindListen() {
        /*
        r15 = this;
        r1 = r15.mPort;
        r1 = android.sec.enterprise.BluetoothUtils.isSvcRfComPortNumberBlockedBySecurityPolicy(r1);
        if (r1 == 0) goto L_0x002a;
    L_0x0008:
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Port ";
        r2 = r2.append(r3);
        r3 = r15.mPort;
        r2 = r2.append(r3);
        r3 = " is not allowed by MDM policy";
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Log.d(r1, r2);
        r13 = -1;
    L_0x0029:
        return r13;
    L_0x002a:
        r1 = android.os.PersonaManager.KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0;
        r1 = android.os.PersonaManager.isKnoxVersionSupported(r1);
        if (r1 == 0) goto L_0x008a;
    L_0x0032:
        r1 = android.os.Process.myUserHandle();
        r14 = r1.getIdentifier();
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "bindListen(): myUserId = ";
        r2 = r2.append(r3);
        r2 = r2.append(r14);
        r2 = r2.toString();
        android.util.Log.d(r1, r2);
        r1 = 100;
        if (r14 < r1) goto L_0x008a;
    L_0x0056:
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r14 > r1) goto L_0x008a;
    L_0x005a:
        r10 = 0;
        r11 = android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper.getService();
        if (r11 == 0) goto L_0x0065;
    L_0x0061:
        r10 = r11.isKnoxBluetoothEnabled(r14);	 Catch:{ RemoteException -> 0x0081 }
    L_0x0065:
        if (r10 != 0) goto L_0x008a;
    L_0x0067:
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "bindListen(): Bluetooth for KNOX blocked by MDM policy: userId = ";
        r2 = r2.append(r3);
        r2 = r2.append(r14);
        r2 = r2.toString();
        android.util.Log.d(r1, r2);
        r13 = -1;
        goto L_0x0029;
    L_0x0081:
        r12 = move-exception;
        r1 = "BluetoothSocket";
        r2 = "bindListen(): isKnoxBluetoothEnabled on EDMProxy failed! ";
        android.util.Log.w(r1, r2, r12);
        goto L_0x0065;
    L_0x008a:
        r1 = "1";
        r2 = "service.bt.security.policy.mode";
        r2 = android.os.SystemProperties.get(r2);
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x00cb;
    L_0x0099:
        r1 = android.bluetooth.BluetoothUuid.Handsfree_AG;
        r1 = r1.toString();
        r2 = r15.mUuid;
        r2 = r2.toString();
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x00cb;
    L_0x00ab:
        r1 = android.bluetooth.BluetoothUuid.HSP_AG;
        r1 = r1.toString();
        r2 = r15.mUuid;
        r2 = r2.toString();
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x00cb;
    L_0x00bd:
        r1 = DBG;
        if (r1 == 0) goto L_0x00c8;
    L_0x00c1:
        r1 = "BluetoothSocket";
        r2 = "connect not allowed ; IT Policy HandsfreeOnly";
        android.util.Log.d(r1, r2);
    L_0x00c8:
        r13 = -1;
        goto L_0x0029;
    L_0x00cb:
        r1 = r15.mSocketState;
        r2 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;
        if (r1 != r2) goto L_0x00d5;
    L_0x00d1:
        r13 = 77;
        goto L_0x0029;
    L_0x00d5:
        r1 = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        r2 = 0;
        r0 = r1.getBluetoothService(r2);
        if (r0 != 0) goto L_0x00ea;
    L_0x00e0:
        r1 = "BluetoothSocket";
        r2 = "bindListen fail, reason: bluetooth is off";
        android.util.Log.e(r1, r2);
        r13 = -1;
        goto L_0x0029;
    L_0x00ea:
        r1 = r15.mType;	 Catch:{ RemoteException -> 0x015d }
        r2 = r15.mServiceName;	 Catch:{ RemoteException -> 0x015d }
        r3 = r15.mUuid;	 Catch:{ RemoteException -> 0x015d }
        r4 = r15.mPort;	 Catch:{ RemoteException -> 0x015d }
        r5 = r15.getSecurityFlags();	 Catch:{ RemoteException -> 0x015d }
        r1 = r0.createSocketChannel(r1, r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x015d }
        r15.mPfd = r1;	 Catch:{ RemoteException -> 0x015d }
        monitor-enter(r15);	 Catch:{ IOException -> 0x0135 }
        r1 = DBG;	 Catch:{ all -> 0x0132 }
        if (r1 == 0) goto L_0x0127;
    L_0x0101:
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0132 }
        r2.<init>();	 Catch:{ all -> 0x0132 }
        r3 = "bindListen(), SocketState: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0132 }
        r3 = r15.mSocketState;	 Catch:{ all -> 0x0132 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0132 }
        r3 = ", mPfd: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0132 }
        r3 = r15.mPfd;	 Catch:{ all -> 0x0132 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0132 }
        r2 = r2.toString();	 Catch:{ all -> 0x0132 }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0132 }
    L_0x0127:
        r1 = r15.mSocketState;	 Catch:{ all -> 0x0132 }
        r2 = android.bluetooth.BluetoothSocket.SocketState.INIT;	 Catch:{ all -> 0x0132 }
        if (r1 == r2) goto L_0x016f;
    L_0x012d:
        r13 = 77;
        monitor-exit(r15);	 Catch:{ all -> 0x0132 }
        goto L_0x0029;
    L_0x0132:
        r1 = move-exception;
        monitor-exit(r15);	 Catch:{ all -> 0x0132 }
        throw r1;	 Catch:{ IOException -> 0x0135 }
    L_0x0135:
        r7 = move-exception;
        r1 = r15.mPfd;
        if (r1 == 0) goto L_0x0142;
    L_0x013a:
        r1 = r15.mPfd;	 Catch:{ IOException -> 0x0204 }
        r1.close();	 Catch:{ IOException -> 0x0204 }
    L_0x013f:
        r1 = 0;
        r15.mPfd = r1;
    L_0x0142:
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "bindListen, fail to get port number, exception: ";
        r2 = r2.append(r3);
        r2 = r2.append(r7);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
        r13 = -1;
        goto L_0x0029;
    L_0x015d:
        r7 = move-exception;
        r1 = "BluetoothSocket";
        r2 = new java.lang.Throwable;
        r2.<init>();
        r2 = android.util.Log.getStackTraceString(r2);
        android.util.Log.e(r1, r2);
        r13 = -1;
        goto L_0x0029;
    L_0x016f:
        r1 = r15.mPfd;	 Catch:{ all -> 0x0132 }
        if (r1 != 0) goto L_0x0177;
    L_0x0173:
        r13 = -1;
        monitor-exit(r15);	 Catch:{ all -> 0x0132 }
        goto L_0x0029;
    L_0x0177:
        r1 = r15.mPfd;	 Catch:{ all -> 0x0132 }
        r9 = r1.getFileDescriptor();	 Catch:{ all -> 0x0132 }
        r1 = DBG;	 Catch:{ all -> 0x0132 }
        if (r1 == 0) goto L_0x0188;
    L_0x0181:
        r1 = "BluetoothSocket";
        r2 = "bindListen(), new LocalSocket ";
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0132 }
    L_0x0188:
        r1 = new android.net.LocalSocket;	 Catch:{ all -> 0x0132 }
        r1.<init>(r9);	 Catch:{ all -> 0x0132 }
        r15.mSocket = r1;	 Catch:{ all -> 0x0132 }
        r1 = DBG;	 Catch:{ all -> 0x0132 }
        if (r1 == 0) goto L_0x019a;
    L_0x0193:
        r1 = "BluetoothSocket";
        r2 = "bindListen(), new LocalSocket.getInputStream() ";
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0132 }
    L_0x019a:
        r1 = r15.mSocket;	 Catch:{ all -> 0x0132 }
        r1 = r1.getInputStream();	 Catch:{ all -> 0x0132 }
        r15.mSocketIS = r1;	 Catch:{ all -> 0x0132 }
        r1 = r15.mSocket;	 Catch:{ all -> 0x0132 }
        r1 = r1.getOutputStream();	 Catch:{ all -> 0x0132 }
        r15.mSocketOS = r1;	 Catch:{ all -> 0x0132 }
        monitor-exit(r15);	 Catch:{ all -> 0x0132 }
        r1 = DBG;	 Catch:{ IOException -> 0x0135 }
        if (r1 == 0) goto L_0x01c9;
    L_0x01af:
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0135 }
        r2.<init>();	 Catch:{ IOException -> 0x0135 }
        r3 = "bindListen(), readInt mSocketIS: ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0135 }
        r3 = r15.mSocketIS;	 Catch:{ IOException -> 0x0135 }
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0135 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0135 }
        android.util.Log.d(r1, r2);	 Catch:{ IOException -> 0x0135 }
    L_0x01c9:
        r1 = r15.mSocketIS;	 Catch:{ IOException -> 0x0135 }
        r6 = r15.readInt(r1);	 Catch:{ IOException -> 0x0135 }
        monitor-enter(r15);	 Catch:{ IOException -> 0x0135 }
        r1 = r15.mSocketState;	 Catch:{ all -> 0x0201 }
        r2 = android.bluetooth.BluetoothSocket.SocketState.INIT;	 Catch:{ all -> 0x0201 }
        if (r1 != r2) goto L_0x01da;
    L_0x01d6:
        r1 = android.bluetooth.BluetoothSocket.SocketState.LISTENING;	 Catch:{ all -> 0x0201 }
        r15.mSocketState = r1;	 Catch:{ all -> 0x0201 }
    L_0x01da:
        monitor-exit(r15);	 Catch:{ all -> 0x0201 }
        r1 = DBG;	 Catch:{ IOException -> 0x0135 }
        if (r1 == 0) goto L_0x01f7;
    L_0x01df:
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0135 }
        r2.<init>();	 Catch:{ IOException -> 0x0135 }
        r3 = "channel: ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0135 }
        r2 = r2.append(r6);	 Catch:{ IOException -> 0x0135 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0135 }
        android.util.Log.d(r1, r2);	 Catch:{ IOException -> 0x0135 }
    L_0x01f7:
        r1 = r15.mPort;	 Catch:{ IOException -> 0x0135 }
        r2 = -1;
        if (r1 > r2) goto L_0x01fe;
    L_0x01fc:
        r15.mPort = r6;	 Catch:{ IOException -> 0x0135 }
    L_0x01fe:
        r13 = 0;
        goto L_0x0029;
    L_0x0201:
        r1 = move-exception;
        monitor-exit(r15);	 Catch:{ all -> 0x0201 }
        throw r1;	 Catch:{ IOException -> 0x0135 }
    L_0x0204:
        r8 = move-exception;
        r1 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "bindListen, close mPfd: ";
        r2 = r2.append(r3);
        r2 = r2.append(r8);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
        goto L_0x013f;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothSocket.bindListen():int");
    }

    BluetoothSocket accept(int timeout) throws IOException {
        if (this.mSocketState != SocketState.LISTENING) {
            throw new IOException("bt socket is not in listen state");
        }
        BluetoothSocket acceptedSocket;
        if (timeout > 0) {
            Log.d(TAG, "accept() set timeout (ms):" + timeout);
            this.mSocket.setSoTimeout(timeout);
        }
        String RemoteAddr = waitSocketSignal(this.mSocketIS);
        if (timeout > 0) {
            this.mSocket.setSoTimeout(0);
        }
        synchronized (this) {
            if (this.mSocketState != SocketState.LISTENING) {
                throw new IOException("bt socket is not in listen state");
            }
            acceptedSocket = acceptSocket(RemoteAddr);
        }
        return acceptedSocket;
    }

    int available() throws IOException {
        if (VDBG) {
            Log.d(TAG, "available: " + this.mSocketIS);
        }
        return this.mSocketIS.available();
    }

    void flush() throws IOException {
        if (this.mSocketOS == null) {
            throw new IOException("flush is called on null OutputStream");
        }
        if (VDBG) {
            Log.d(TAG, "flush: " + this.mSocketOS);
        }
        this.mSocketOS.flush();
    }

    int read(byte[] b, int offset, int length) throws IOException {
        int ret;
        if (VDBG) {
            Log.d(TAG, "read in:  " + this.mSocketIS + " len: " + length);
        }
        if (this.mType == 3) {
            int bytesToRead = length;
            if (VDBG) {
                Log.v(TAG, "l2cap: read(): offset: " + offset + " length:" + length + "mL2capBuffer= " + this.mL2capBuffer);
            }
            if (this.mL2capBuffer == null) {
                createL2capRxBuffer();
            }
            if (this.mL2capBuffer.remaining() == 0) {
                if (VDBG) {
                    Log.v(TAG, "l2cap buffer empty, refilling...");
                }
                if (fillL2capRxBuffer() == -1) {
                    return -1;
                }
            }
            if (bytesToRead > this.mL2capBuffer.remaining()) {
                bytesToRead = this.mL2capBuffer.remaining();
            }
            if (VDBG) {
                Log.v(TAG, "get(): offset: " + offset + " bytesToRead: " + bytesToRead);
            }
            this.mL2capBuffer.get(b, offset, bytesToRead);
            ret = bytesToRead;
        } else {
            if (VDBG) {
                Log.v(TAG, "default: read(): offset: " + offset + " length:" + length);
            }
            ret = this.mSocketIS.read(b, offset, length);
        }
        if (ret < 0) {
            throw new IOException("bt socket closed, read return: " + ret);
        }
        if (VDBG) {
            Log.d(TAG, "read out:  " + this.mSocketIS + " ret: " + ret);
        }
        return ret;
    }

    int write(byte[] b, int offset, int length) throws IOException {
        if (VDBG) {
            Log.d(TAG, "write: " + this.mSocketOS + " length: " + length);
        }
        if (this.mType != 3) {
            this.mSocketOS.write(b, offset, length);
        } else if (length <= this.mMaxTxPacketSize) {
            this.mSocketOS.write(b, offset, length);
        } else {
            int tmpOffset = offset;
            int tmpLength = this.mMaxTxPacketSize;
            int endIndex = offset + length;
            boolean done = false;
            if (DBG) {
                Log.w(TAG, "WARNING: Write buffer larger than L2CAP packet size!\nPacket will be divided into SDU packets of size " + this.mMaxTxPacketSize);
            }
            do {
                this.mSocketOS.write(b, tmpOffset, tmpLength);
                tmpOffset += this.mMaxTxPacketSize;
                if (this.mMaxTxPacketSize + tmpOffset > endIndex) {
                    tmpLength = endIndex - tmpOffset;
                    done = true;
                    continue;
                }
            } while (!done);
        }
        if (VDBG) {
            Log.d(TAG, "write out: " + this.mSocketOS + " length: " + length);
        }
        return length;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        /*
        r3 = this;
        r0 = DBG;
        if (r0 == 0) goto L_0x0034;
    L_0x0004:
        r0 = "BluetoothSocket";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "close() in, this: ";
        r1 = r1.append(r2);
        r1 = r1.append(r3);
        r2 = ", channel: ";
        r1 = r1.append(r2);
        r2 = r3.mPort;
        r1 = r1.append(r2);
        r2 = ", state: ";
        r1 = r1.append(r2);
        r2 = r3.mSocketState;
        r1 = r1.append(r2);
        r1 = r1.toString();
        android.util.Log.d(r0, r1);
    L_0x0034:
        r0 = r3.mSocketState;
        r1 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;
        if (r0 != r1) goto L_0x003b;
    L_0x003a:
        return;
    L_0x003b:
        monitor-enter(r3);
        r0 = r3.mSocketState;	 Catch:{ all -> 0x0044 }
        r1 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;	 Catch:{ all -> 0x0044 }
        if (r0 != r1) goto L_0x0047;
    L_0x0042:
        monitor-exit(r3);	 Catch:{ all -> 0x0044 }
        goto L_0x003a;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0044 }
        throw r0;
    L_0x0047:
        r0 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;	 Catch:{ all -> 0x0044 }
        r3.mSocketState = r0;	 Catch:{ all -> 0x0044 }
        r0 = DBG;	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x0098;
    L_0x004f:
        r0 = "BluetoothSocket";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0044 }
        r1.<init>();	 Catch:{ all -> 0x0044 }
        r2 = "close() this: ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r1 = r1.append(r3);	 Catch:{ all -> 0x0044 }
        r2 = ", channel: ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = r3.mPort;	 Catch:{ all -> 0x0044 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = ", mSocketIS: ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = r3.mSocketIS;	 Catch:{ all -> 0x0044 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = ", mSocketOS: ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = r3.mSocketOS;	 Catch:{ all -> 0x0044 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = "mSocket: ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = r3.mSocket;	 Catch:{ all -> 0x0044 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r1 = r1.toString();	 Catch:{ all -> 0x0044 }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0044 }
    L_0x0098:
        r0 = r3.mSocket;	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x00cc;
    L_0x009c:
        r0 = DBG;	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x00ba;
    L_0x00a0:
        r0 = "BluetoothSocket";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0044 }
        r1.<init>();	 Catch:{ all -> 0x0044 }
        r2 = "Closing mSocket: ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r2 = r3.mSocket;	 Catch:{ all -> 0x0044 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0044 }
        r1 = r1.toString();	 Catch:{ all -> 0x0044 }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0044 }
    L_0x00ba:
        r0 = r3.mSocket;	 Catch:{ all -> 0x0044 }
        r0.shutdownInput();	 Catch:{ all -> 0x0044 }
        r0 = r3.mSocket;	 Catch:{ all -> 0x0044 }
        r0.shutdownOutput();	 Catch:{ all -> 0x0044 }
        r0 = r3.mSocket;	 Catch:{ all -> 0x0044 }
        r0.close();	 Catch:{ all -> 0x0044 }
        r0 = 0;
        r3.mSocket = r0;	 Catch:{ all -> 0x0044 }
    L_0x00cc:
        r0 = r3.mPfd;	 Catch:{ all -> 0x0044 }
        if (r0 == 0) goto L_0x00d8;
    L_0x00d0:
        r0 = r3.mPfd;	 Catch:{ all -> 0x0044 }
        r0.close();	 Catch:{ all -> 0x0044 }
        r0 = 0;
        r3.mPfd = r0;	 Catch:{ all -> 0x0044 }
    L_0x00d8:
        monitor-exit(r3);	 Catch:{ all -> 0x0044 }
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothSocket.close():void");
    }

    void removeChannel() {
    }

    int getPort() {
        return this.mPort;
    }

    public int getMaxTransmitPacketSize() {
        return this.mMaxTxPacketSize;
    }

    public int getMaxReceivePacketSize() {
        return this.mMaxRxPacketSize;
    }

    public int getConnectionType() {
        return this.mType;
    }

    public void setExcludeSdp(boolean excludeSdp) {
        this.mExcludeSdp = excludeSdp;
    }

    private String convertAddr(byte[] addr) {
        return String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", new Object[]{Byte.valueOf(addr[0]), Byte.valueOf(addr[1]), Byte.valueOf(addr[2]), Byte.valueOf(addr[3]), Byte.valueOf(addr[4]), Byte.valueOf(addr[5])});
    }

    private String waitSocketSignal(InputStream is) throws IOException {
        byte[] sig = new byte[SOCK_SIGNAL_SIZE];
        int ret = readAll(is, sig);
        if (VDBG) {
            Log.d(TAG, "waitSocketSignal read " + SOCK_SIGNAL_SIZE + " bytes signal ret: " + ret);
        }
        ByteBuffer bb = ByteBuffer.wrap(sig);
        bb.order(ByteOrder.nativeOrder());
        int size = bb.getShort();
        if (size != SOCK_SIGNAL_SIZE) {
            throw new IOException("Connection failure, wrong signal size: " + size);
        }
        byte[] addr = new byte[6];
        bb.get(addr);
        int channel = bb.getInt();
        int status = bb.getInt();
        this.mMaxTxPacketSize = bb.getShort() & 65535;
        this.mMaxRxPacketSize = bb.getShort() & 65535;
        String RemoteAddr = convertAddr(addr);
        if (VDBG) {
            Log.d(TAG, "waitSocketSignal: sig size: " + size + ", remote addr: " + RemoteAddr + ", channel: " + channel + ", status: " + status + " MaxRxPktSize: " + this.mMaxRxPacketSize + " MaxTxPktSize: " + this.mMaxTxPacketSize);
        }
        if (status == 0) {
            return RemoteAddr;
        }
        throw new IOException("Connection failure, status: " + status);
    }

    private void createL2capRxBuffer() {
        if (this.mType == 3) {
            if (VDBG) {
                Log.v(TAG, "  Creating mL2capBuffer: mMaxPacketSize: " + this.mMaxRxPacketSize);
            }
            this.mL2capBuffer = ByteBuffer.wrap(new byte[this.mMaxRxPacketSize]);
            if (VDBG) {
                Log.v(TAG, "mL2capBuffer.remaining()" + this.mL2capBuffer.remaining());
            }
            this.mL2capBuffer.limit(0);
            if (VDBG) {
                Log.v(TAG, "mL2capBuffer.remaining() after limit(0):" + this.mL2capBuffer.remaining());
            }
        }
    }

    private int readAll(InputStream is, byte[] b) throws IOException {
        int left = b.length;
        while (left > 0) {
            int ret = is.read(b, b.length - left, left);
            if (ret <= 0) {
                throw new IOException("read failed, socket might closed or timeout, read ret: " + ret);
            }
            left -= ret;
            if (left != 0) {
                Log.w(TAG, "readAll() looping, read partial size: " + (b.length - left) + ", expect size: " + b.length);
            }
        }
        return b.length;
    }

    private int readInt(InputStream is) throws IOException {
        byte[] ibytes = new byte[4];
        int ret = readAll(is, ibytes);
        if (VDBG) {
            Log.d(TAG, "inputStream.read ret: " + ret);
        }
        ByteBuffer bb = ByteBuffer.wrap(ibytes);
        bb.order(ByteOrder.nativeOrder());
        return bb.getInt();
    }

    private int fillL2capRxBuffer() throws IOException {
        this.mL2capBuffer.rewind();
        int ret = this.mSocketIS.read(this.mL2capBuffer.array());
        if (ret == -1) {
            this.mL2capBuffer.limit(0);
            return -1;
        }
        this.mL2capBuffer.limit(ret);
        return ret;
    }
}
