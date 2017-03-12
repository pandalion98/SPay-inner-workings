package android.app.usage;

import android.content.Context;
import android.net.INetworkStatsService.Stub;
import android.net.INetworkStatsSession;
import android.net.NetworkStatsHistory;
import android.net.NetworkStatsHistory.Entry;
import android.net.NetworkTemplate;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.IntArray;
import android.util.Log;
import dalvik.system.CloseGuard;

public final class NetworkStats implements AutoCloseable {
    private static final String TAG = "NetworkStats";
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final long mEndTimeStamp;
    private int mEnumerationIndex = 0;
    private NetworkStatsHistory mHistory = null;
    private Entry mRecycledHistoryEntry = null;
    private android.net.NetworkStats.Entry mRecycledSummaryEntry = null;
    private INetworkStatsSession mSession;
    private final long mStartTimeStamp;
    private android.net.NetworkStats mSummary = null;
    private NetworkTemplate mTemplate;
    private int mUidOrUidIndex;
    private int[] mUids;

    public static class Bucket {
        public static final int STATE_ALL = -1;
        public static final int STATE_DEFAULT = 1;
        public static final int STATE_FOREGROUND = 2;
        public static final int UID_ALL = -1;
        public static final int UID_REMOVED = -4;
        public static final int UID_TETHERING = -5;
        private long mBeginTimeStamp;
        private long mEndTimeStamp;
        private long mRxBytes;
        private long mRxPackets;
        private int mState;
        private long mTxBytes;
        private long mTxPackets;
        private int mUid;

        private static int convertState(int networkStatsSet) {
            switch (networkStatsSet) {
                case -1:
                    return -1;
                case 0:
                    return 1;
                case 1:
                    return 2;
                default:
                    return 0;
            }
        }

        private static int convertUid(int uid) {
            switch (uid) {
                case -5:
                    return -5;
                case -4:
                    return -4;
                default:
                    return uid;
            }
        }

        public int getUid() {
            return this.mUid;
        }

        public int getState() {
            return this.mState;
        }

        public long getStartTimeStamp() {
            return this.mBeginTimeStamp;
        }

        public long getEndTimeStamp() {
            return this.mEndTimeStamp;
        }

        public long getRxBytes() {
            return this.mRxBytes;
        }

        public long getTxBytes() {
            return this.mTxBytes;
        }

        public long getRxPackets() {
            return this.mRxPackets;
        }

        public long getTxPackets() {
            return this.mTxPackets;
        }
    }

    NetworkStats(Context context, NetworkTemplate template, long startTimestamp, long endTimestamp) throws RemoteException, SecurityException {
        this.mSession = Stub.asInterface(ServiceManager.getService(Context.NETWORK_STATS_SERVICE)).openSessionForUsageStats(context.getOpPackageName());
        this.mCloseGuard.open("close");
        this.mTemplate = template;
        this.mStartTimeStamp = startTimestamp;
        this.mEndTimeStamp = endTimestamp;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public boolean getNextBucket(Bucket bucketOut) {
        if (this.mSummary != null) {
            return getNextSummaryBucket(bucketOut);
        }
        return getNextHistoryBucket(bucketOut);
    }

    public boolean hasNextBucket() {
        boolean z = true;
        if (this.mSummary != null) {
            if (this.mEnumerationIndex >= this.mSummary.size()) {
                z = false;
            }
            return z;
        } else if (this.mHistory == null) {
            return false;
        } else {
            if (this.mEnumerationIndex < this.mHistory.size() || hasNextUid()) {
                return true;
            }
            return false;
        }
    }

    public void close() {
        if (this.mSession != null) {
            try {
                this.mSession.close();
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
        }
        this.mSession = null;
        if (this.mCloseGuard != null) {
            this.mCloseGuard.close();
        }
    }

    Bucket getDeviceSummaryForNetwork() throws RemoteException {
        this.mSummary = this.mSession.getDeviceSummaryForNetwork(this.mTemplate, this.mStartTimeStamp, this.mEndTimeStamp);
        this.mEnumerationIndex = this.mSummary.size();
        return getSummaryAggregate();
    }

    void startSummaryEnumeration() throws RemoteException {
        this.mSummary = this.mSession.getSummaryForAllUid(this.mTemplate, this.mStartTimeStamp, this.mEndTimeStamp, false);
        this.mEnumerationIndex = 0;
    }

    void startHistoryEnumeration(int uid) {
        this.mHistory = null;
        try {
            this.mHistory = this.mSession.getHistoryIntervalForUid(this.mTemplate, uid, -1, 0, -1, this.mStartTimeStamp, this.mEndTimeStamp);
            setSingleUid(uid);
        } catch (RemoteException e) {
            Log.w(TAG, e);
        }
        this.mEnumerationIndex = 0;
    }

    void startUserUidEnumeration() throws RemoteException {
        int[] uids = this.mSession.getRelevantUids();
        IntArray filteredUids = new IntArray(uids.length);
        for (int uid : uids) {
            try {
                NetworkStatsHistory history = this.mSession.getHistoryIntervalForUid(this.mTemplate, uid, -1, 0, -1, this.mStartTimeStamp, this.mEndTimeStamp);
                if (history != null && history.size() > 0) {
                    filteredUids.add(uid);
                }
            } catch (RemoteException e) {
                Log.w(TAG, "Error while getting history of uid " + uid, e);
            }
        }
        this.mUids = filteredUids.toArray();
        this.mUidOrUidIndex = -1;
        stepHistory();
    }

    private void stepHistory() {
        if (hasNextUid()) {
            stepUid();
            this.mHistory = null;
            try {
                this.mHistory = this.mSession.getHistoryIntervalForUid(this.mTemplate, getUid(), -1, 0, -1, this.mStartTimeStamp, this.mEndTimeStamp);
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
            this.mEnumerationIndex = 0;
        }
    }

    private void fillBucketFromSummaryEntry(Bucket bucketOut) {
        bucketOut.mUid = Bucket.convertUid(this.mRecycledSummaryEntry.uid);
        bucketOut.mState = Bucket.convertState(this.mRecycledSummaryEntry.set);
        bucketOut.mBeginTimeStamp = this.mStartTimeStamp;
        bucketOut.mEndTimeStamp = this.mEndTimeStamp;
        bucketOut.mRxBytes = this.mRecycledSummaryEntry.rxBytes;
        bucketOut.mRxPackets = this.mRecycledSummaryEntry.rxPackets;
        bucketOut.mTxBytes = this.mRecycledSummaryEntry.txBytes;
        bucketOut.mTxPackets = this.mRecycledSummaryEntry.txPackets;
    }

    private boolean getNextSummaryBucket(Bucket bucketOut) {
        if (bucketOut == null || this.mEnumerationIndex >= this.mSummary.size()) {
            return false;
        }
        android.net.NetworkStats networkStats = this.mSummary;
        int i = this.mEnumerationIndex;
        this.mEnumerationIndex = i + 1;
        this.mRecycledSummaryEntry = networkStats.getValues(i, this.mRecycledSummaryEntry);
        fillBucketFromSummaryEntry(bucketOut);
        return true;
    }

    Bucket getSummaryAggregate() {
        if (this.mSummary == null) {
            return null;
        }
        Bucket bucket = new Bucket();
        if (this.mRecycledSummaryEntry == null) {
            this.mRecycledSummaryEntry = new android.net.NetworkStats.Entry();
        }
        this.mSummary.getTotal(this.mRecycledSummaryEntry);
        fillBucketFromSummaryEntry(bucket);
        return bucket;
    }

    private boolean getNextHistoryBucket(Bucket bucketOut) {
        if (!(bucketOut == null || this.mHistory == null)) {
            if (this.mEnumerationIndex < this.mHistory.size()) {
                NetworkStatsHistory networkStatsHistory = this.mHistory;
                int i = this.mEnumerationIndex;
                this.mEnumerationIndex = i + 1;
                this.mRecycledHistoryEntry = networkStatsHistory.getValues(i, this.mRecycledHistoryEntry);
                bucketOut.mUid = Bucket.convertUid(getUid());
                bucketOut.mState = -1;
                bucketOut.mBeginTimeStamp = this.mRecycledHistoryEntry.bucketStart;
                bucketOut.mEndTimeStamp = this.mRecycledHistoryEntry.bucketStart + this.mRecycledHistoryEntry.bucketDuration;
                bucketOut.mRxBytes = this.mRecycledHistoryEntry.rxBytes;
                bucketOut.mRxPackets = this.mRecycledHistoryEntry.rxPackets;
                bucketOut.mTxBytes = this.mRecycledHistoryEntry.txBytes;
                bucketOut.mTxPackets = this.mRecycledHistoryEntry.txPackets;
                return true;
            } else if (hasNextUid()) {
                stepHistory();
                return getNextHistoryBucket(bucketOut);
            }
        }
        return false;
    }

    private boolean isUidEnumeration() {
        return this.mUids != null;
    }

    private boolean hasNextUid() {
        return isUidEnumeration() && this.mUidOrUidIndex + 1 < this.mUids.length;
    }

    private int getUid() {
        if (!isUidEnumeration()) {
            return this.mUidOrUidIndex;
        }
        if (this.mUidOrUidIndex >= 0 && this.mUidOrUidIndex < this.mUids.length) {
            return this.mUids[this.mUidOrUidIndex];
        }
        throw new IndexOutOfBoundsException("Index=" + this.mUidOrUidIndex + " mUids.length=" + this.mUids.length);
    }

    private void setSingleUid(int uid) {
        this.mUidOrUidIndex = uid;
    }

    private void stepUid() {
        if (this.mUids != null) {
            this.mUidOrUidIndex++;
        }
    }
}
