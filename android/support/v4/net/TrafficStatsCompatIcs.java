package android.support.v4.net;

import android.net.TrafficStats;
import java.net.Socket;

class TrafficStatsCompatIcs {
    TrafficStatsCompatIcs() {
    }

    public static void clearThreadStatsTag() {
        TrafficStats.clearThreadStatsTag();
    }

    public static int getThreadStatsTag() {
        return TrafficStats.getThreadStatsTag();
    }

    public static void incrementOperationCount(int i) {
        TrafficStats.incrementOperationCount(i);
    }

    public static void incrementOperationCount(int i, int i2) {
        TrafficStats.incrementOperationCount(i, i2);
    }

    public static void setThreadStatsTag(int i) {
        TrafficStats.setThreadStatsTag(i);
    }

    public static void tagSocket(Socket socket) {
        TrafficStats.tagSocket(socket);
    }

    public static void untagSocket(Socket socket) {
        TrafficStats.untagSocket(socket);
    }
}
