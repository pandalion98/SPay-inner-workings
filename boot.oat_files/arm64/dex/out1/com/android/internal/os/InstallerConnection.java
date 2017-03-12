package com.android.internal.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.telephony.PhoneConstants;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import libcore.io.IoUtils;
import libcore.io.Streams;

public class InstallerConnection {
    private static final boolean LOCAL_DEBUG = false;
    private static final String TAG = "InstallerConnection";
    private final byte[] buf = new byte[1024];
    private InputStream mIn;
    private OutputStream mOut;
    private LocalSocket mSocket;

    public synchronized String transact(String cmd) {
        String str;
        if (connect()) {
            if (!writeCommand(cmd)) {
                Slog.e(TAG, "write command failed? reconnect!");
                if (!(connect() && writeCommand(cmd))) {
                    str = SmartFaceManager.PAGE_TOP;
                }
            }
            int replyLength = readReply();
            if (replyLength > 0) {
                str = new String(this.buf, 0, replyLength);
            } else {
                str = SmartFaceManager.PAGE_TOP;
            }
        } else {
            Slog.e(TAG, "connection failed");
            str = SmartFaceManager.PAGE_TOP;
        }
        return str;
    }

    public int execute(String cmd) {
        try {
            return Integer.parseInt(transact(cmd));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int dexopt(String apkPath, int uid, boolean isPublic, String instructionSet, int dexoptNeeded, boolean bootComplete) {
        return dexopt(apkPath, uid, isPublic, PhoneConstants.APN_TYPE_ALL, instructionSet, dexoptNeeded, false, false, null, bootComplete, false);
    }

    public int dexopt(String apkPath, int uid, boolean isPublic, String pkgName, String instructionSet, int dexoptNeeded, boolean vmSafeMode, boolean debuggable, String outputPath, boolean bootComplete) {
        return dexopt(apkPath, uid, isPublic, pkgName, instructionSet, dexoptNeeded, vmSafeMode, debuggable, outputPath, bootComplete, false);
    }

    public int dexopt(String apkPath, int uid, boolean isPublic, String pkgName, String instructionSet, int dexoptNeeded, boolean vmSafeMode, boolean debuggable, String outputPath, boolean bootComplete, boolean interpret_only) {
        StringBuilder builder = new StringBuilder("dexopt");
        builder.append(' ');
        builder.append(apkPath);
        builder.append(' ');
        builder.append(uid);
        builder.append(isPublic ? " 1" : " 0");
        builder.append(' ');
        builder.append(pkgName);
        builder.append(' ');
        builder.append(instructionSet);
        builder.append(' ');
        builder.append(dexoptNeeded);
        builder.append(vmSafeMode ? " 1" : " 0");
        builder.append(debuggable ? " 1" : " 0");
        builder.append(' ');
        if (outputPath == null) {
            outputPath = "!";
        }
        builder.append(outputPath);
        builder.append(bootComplete ? " 1" : " 0");
        builder.append(' ');
        builder.append(interpret_only ? " 1" : " 0");
        return execute(builder.toString());
    }

    public int asyncDexopt(String apkPath, int uid, boolean isPublic, String pkgName, String instructionSet, int dexoptNeeded, boolean vmSafeMode, boolean debuggable, String outputPath, boolean bootComplete, boolean interpret_only, int hashCode) {
        StringBuilder builder = new StringBuilder("asyncdexopt");
        builder.append(' ');
        builder.append(apkPath);
        builder.append(' ');
        builder.append(uid);
        builder.append(isPublic ? " 1" : " 0");
        builder.append(' ');
        builder.append(pkgName);
        builder.append(' ');
        builder.append(instructionSet);
        builder.append(' ');
        builder.append(dexoptNeeded);
        builder.append(vmSafeMode ? " 1" : " 0");
        builder.append(debuggable ? " 1" : " 0");
        builder.append(' ');
        if (outputPath == null) {
            outputPath = "!";
        }
        builder.append(outputPath);
        builder.append(bootComplete ? " 1" : " 0");
        builder.append(' ');
        builder.append(interpret_only ? " 1" : " 0");
        builder.append(' ');
        builder.append(hashCode);
        return execute(builder.toString());
    }

    public String processAsyncResult() {
        StringBuilder builder = new StringBuilder("asyncresult");
        return transact("asyncresult");
    }

    private boolean connect() {
        if (this.mSocket != null) {
            return true;
        }
        Slog.i(TAG, "connecting...");
        try {
            this.mSocket = new LocalSocket();
            this.mSocket.connect(new LocalSocketAddress("installd", Namespace.RESERVED));
            this.mIn = this.mSocket.getInputStream();
            this.mOut = this.mSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            disconnect();
            return false;
        }
    }

    public void disconnect() {
        Slog.i(TAG, "disconnecting...");
        IoUtils.closeQuietly(this.mSocket);
        IoUtils.closeQuietly(this.mIn);
        IoUtils.closeQuietly(this.mOut);
        this.mSocket = null;
        this.mIn = null;
        this.mOut = null;
    }

    private boolean readFully(byte[] buffer, int len) {
        try {
            Streams.readFully(this.mIn, buffer, 0, len);
            return true;
        } catch (IOException e) {
            Slog.e(TAG, "read exception");
            disconnect();
            return false;
        }
    }

    private int readReply() {
        if (!readFully(this.buf, 2)) {
            return -1;
        }
        int len = (this.buf[0] & 255) | ((this.buf[1] & 255) << 8);
        if (len < 1 || len > this.buf.length) {
            Slog.e(TAG, "invalid reply length (" + len + ")");
            disconnect();
            return -1;
        } else if (readFully(this.buf, len)) {
            return len;
        } else {
            return -1;
        }
    }

    private boolean writeCommand(String cmdString) {
        byte[] cmd = cmdString.getBytes();
        int len = cmd.length;
        if (len < 1 || len > this.buf.length) {
            return false;
        }
        this.buf[0] = (byte) (len & 255);
        this.buf[1] = (byte) ((len >> 8) & 255);
        try {
            this.mOut.write(this.buf, 0, 2);
            this.mOut.write(cmd, 0, len);
            return true;
        } catch (IOException e) {
            Slog.e(TAG, "write error");
            disconnect();
            return false;
        }
    }

    public void waitForConnection() {
        while (execute("ping") < 0) {
            Slog.w(TAG, "installd not ready");
            SystemClock.sleep(1000);
        }
    }
}
