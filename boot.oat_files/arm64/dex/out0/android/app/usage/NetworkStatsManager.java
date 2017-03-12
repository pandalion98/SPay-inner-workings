package android.app.usage;

import android.app.usage.NetworkStats.Bucket;
import android.content.Context;
import android.net.NetworkIdentity;
import android.net.NetworkTemplate;
import android.os.RemoteException;
import android.util.Log;

public class NetworkStatsManager {
    private static final String TAG = "NetworkStatsManager";
    private final Context mContext;

    public NetworkStatsManager(Context context) {
        this.mContext = context;
    }

    public Bucket querySummaryForDevice(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        if (template == null) {
            return null;
        }
        NetworkStats stats = new NetworkStats(this.mContext, template, startTime, endTime);
        Bucket bucket = stats.getDeviceSummaryForNetwork();
        stats.close();
        return bucket;
    }

    public Bucket querySummaryForUser(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        if (template == null) {
            return null;
        }
        NetworkStats stats = new NetworkStats(this.mContext, template, startTime, endTime);
        stats.startSummaryEnumeration();
        stats.close();
        return stats.getSummaryAggregate();
    }

    public NetworkStats querySummary(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        if (template == null) {
            return null;
        }
        NetworkStats result = new NetworkStats(this.mContext, template, startTime, endTime);
        result.startSummaryEnumeration();
        return result;
    }

    public NetworkStats queryDetailsForUid(int networkType, String subscriberId, long startTime, long endTime, int uid) throws SecurityException, RemoteException {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        if (template == null) {
            return null;
        }
        NetworkStats result = new NetworkStats(this.mContext, template, startTime, endTime);
        result.startHistoryEnumeration(uid);
        return result;
    }

    public NetworkStats queryDetails(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        if (template == null) {
            return null;
        }
        NetworkStats result = new NetworkStats(this.mContext, template, startTime, endTime);
        result.startUserUidEnumeration();
        return result;
    }

    private static NetworkTemplate createTemplate(int networkType, String subscriberId) {
        switch (networkType) {
            case 0:
                return NetworkTemplate.buildTemplateMobileAll(subscriberId);
            case 1:
                return NetworkTemplate.buildTemplateWifiWildcard();
            default:
                Log.w(TAG, "Cannot create template for network type " + networkType + ", subscriberId '" + NetworkIdentity.scrubSubscriberId(subscriberId) + "'.");
                return null;
        }
    }
}
