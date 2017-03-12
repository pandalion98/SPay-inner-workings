package com.android.server;

import android.os.ParcelFileDescriptor;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import com.sec.android.app.CscFeature;
import dalvik.system.SocketTagger;
import java.io.FileDescriptor;
import java.net.SocketException;

public final class NetworkManagementSocketTagger extends SocketTagger {
    private static final boolean LOGD = false;
    public static final String PROP_QTAGUID_ENABLED = "net.qtaguid_enabled";
    private static final String TAG = "NetworkManagementSocketTagger";
    private static ThreadLocal<SocketTags> threadSocketTags = new ThreadLocal<SocketTags>() {
        protected SocketTags initialValue() {
            return new SocketTags();
        }
    };

    public static class SocketTags {
        public int statsTag = -1;
        public int statsUid = -1;
    }

    private static native int native_deleteTagData(int i, int i2);

    private static native int native_setCounterSet(int i, int i2);

    private static native int native_tagSocketFd(FileDescriptor fileDescriptor, int i, int i2);

    private static native int native_untagSocketFd(FileDescriptor fileDescriptor);

    public static void install() {
        SocketTagger.set(new NetworkManagementSocketTagger());
    }

    public static void setThreadSocketStatsTag(int tag) {
        ((SocketTags) threadSocketTags.get()).statsTag = tag;
    }

    public static int getThreadSocketStatsTag() {
        return ((SocketTags) threadSocketTags.get()).statsTag;
    }

    public static void setThreadSocketStatsUid(int uid) {
        ((SocketTags) threadSocketTags.get()).statsUid = uid;
    }

    public void tag(FileDescriptor fd) throws SocketException {
        SocketTags options = (SocketTags) threadSocketTags.get();
        tagSocketFd(fd, options.statsTag, options.statsUid);
    }

    private void tagSocketFd(FileDescriptor fd, int tag, int uid) {
        if (!(tag == -1 && uid == -1) && SystemProperties.getBoolean(PROP_QTAGUID_ENABLED, false)) {
            int errno = native_tagSocketFd(fd, tag, uid);
            if (errno < 0) {
                Log.i(TAG, "tagSocketFd(" + fd.getInt$() + ", " + tag + ", " + uid + ") failed with errno" + errno);
            }
        }
    }

    public void untag(FileDescriptor fd) throws SocketException {
        unTagSocketFd(fd);
    }

    private void unTagSocketFd(FileDescriptor fd) {
        SocketTags options = (SocketTags) threadSocketTags.get();
        if (!(options.statsTag == -1 && options.statsUid == -1) && SystemProperties.getBoolean(PROP_QTAGUID_ENABLED, false)) {
            int errno = native_untagSocketFd(fd);
            if (errno < 0) {
                Log.w(TAG, "untagSocket(" + fd.getInt$() + ") failed with errno " + errno);
            }
        }
    }

    public static void setKernelCounterSet(int uid, int counterSet) {
        if (SystemProperties.getBoolean(PROP_QTAGUID_ENABLED, false)) {
            int errno = native_setCounterSet(counterSet, uid);
            if (errno < 0) {
                Log.w(TAG, "setKernelCountSet(" + uid + ", " + counterSet + ") failed with errno " + errno);
            }
        }
    }

    public static void resetKernelUidStats(int uid) {
        if (SystemProperties.getBoolean(PROP_QTAGUID_ENABLED, false)) {
            int errno = native_deleteTagData(0, uid);
            if (errno < 0) {
                Slog.w(TAG, "problem clearing counters for uid " + uid + " : errno " + errno);
            }
        }
    }

    private static void tagSocketFdOpera(ParcelFileDescriptor pfd, int tag, int uid) {
        Log.d("SamsungOperaMax  ", "Inside method tagSocketFd() in NetworkManagementSocketTagger.java!! .SOCKET : " + pfd.getFileDescriptor().getInt$() + " ,uid : " + uid + " ,tag :" + tag);
        if (!CscFeature.getInstance().getString("CscFeature_SmartManager_ConfigSubFeatures").equals("UDS")) {
            return;
        }
        if (!(tag == -1 && uid == -1) && SystemProperties.getBoolean(PROP_QTAGUID_ENABLED, false)) {
            int errno = native_tagSocketFd(pfd.getFileDescriptor(), tag, uid);
            if (errno < 0) {
                Log.i(TAG, " SamsungOperaMax  tagSocketFd(" + pfd.getFileDescriptor().getInt$() + ", " + tag + ", " + uid + ") failed with errno" + errno);
            } else {
                Log.i(TAG, " SamsungOperaMax  successful in  tagSocketFd(" + pfd.getFileDescriptor().getInt$() + ", " + tag + ", " + uid + ") passed with " + errno);
            }
        }
    }

    public static void tagSocketForOperaVpn(ParcelFileDescriptor pfd, int uid, int tag) throws SocketException {
        if (CscFeature.getInstance().getString("CscFeature_SmartManager_ConfigSubFeatures").equals("UDS")) {
            Log.d("SamsungOperaMax  ", "Inside method tagsocket3() in NetworkManagementSocketTagger.java!! .SOCKET : " + pfd.getFileDescriptor().getInt$() + " ,uid : " + uid + " ,tag :" + tag);
            tagSocketFdOpera(pfd, tag, uid);
        }
    }

    public static int kernelToTag(String string) {
        int length = string.length();
        if (length > 10) {
            return Long.decode(string.substring(0, length - 8)).intValue();
        }
        return 0;
    }
}
