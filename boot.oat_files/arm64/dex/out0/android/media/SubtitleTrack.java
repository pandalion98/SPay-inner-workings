package android.media;

import android.graphics.Canvas;
import android.media.MediaTimeProvider.OnMediaTimeListener;
import android.net.ProxyInfo;
import android.os.Handler;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public abstract class SubtitleTrack implements OnMediaTimeListener {
    private static final String TAG = "SubtitleTrack";
    public boolean DEBUG = false;
    protected final Vector<Cue> mActiveCues = new Vector();
    protected CueList mCues;
    private MediaFormat mFormat;
    protected Handler mHandler = new Handler();
    private long mLastTimeMs;
    private long mLastUpdateTimeMs;
    private long mNextScheduledTimeMs = -1;
    private Runnable mRunnable;
    protected final LongSparseArray<Run> mRunsByEndTime = new LongSparseArray();
    protected final LongSparseArray<Run> mRunsByID = new LongSparseArray();
    protected MediaTimeProvider mTimeProvider;
    protected boolean mVisible;

    public interface RenderingWidget {

        public interface OnChangedListener {
            void onChanged(RenderingWidget renderingWidget);
        }

        void draw(Canvas canvas);

        void onAttachedToWindow();

        void onDetachedFromWindow();

        void setOnChangedListener(OnChangedListener onChangedListener);

        void setSize(int i, int i2);

        void setVisible(boolean z);
    }

    public static class Cue {
        public long mEndTimeMs;
        public long[] mInnerTimesMs;
        public Cue mNextInRun;
        public long mRunID;
        public long mStartTimeMs;

        public void onTime(long timeMs) {
        }
    }

    static class CueList {
        private static final String TAG = "CueList";
        public boolean DEBUG = false;
        private SortedMap<Long, Vector<Cue>> mCues = new TreeMap();

        class EntryIterator implements Iterator<Pair<Long, Cue>> {
            private long mCurrentTimeMs;
            private boolean mDone;
            private Pair<Long, Cue> mLastEntry;
            private Iterator<Cue> mLastListIterator;
            private Iterator<Cue> mListIterator;
            private SortedMap<Long, Vector<Cue>> mRemainingCues;

            public boolean hasNext() {
                return !this.mDone;
            }

            public Pair<Long, Cue> next() {
                if (this.mDone) {
                    throw new NoSuchElementException(ProxyInfo.LOCAL_EXCL_LIST);
                }
                this.mLastEntry = new Pair(Long.valueOf(this.mCurrentTimeMs), this.mListIterator.next());
                this.mLastListIterator = this.mListIterator;
                if (!this.mListIterator.hasNext()) {
                    nextKey();
                }
                return this.mLastEntry;
            }

            public void remove() {
                if (this.mLastListIterator == null || ((Cue) this.mLastEntry.second).mEndTimeMs != ((Long) this.mLastEntry.first).longValue()) {
                    throw new IllegalStateException(ProxyInfo.LOCAL_EXCL_LIST);
                }
                this.mLastListIterator.remove();
                this.mLastListIterator = null;
                if (((Vector) CueList.this.mCues.get(this.mLastEntry.first)).size() == 0) {
                    CueList.this.mCues.remove(this.mLastEntry.first);
                }
                Cue cue = this.mLastEntry.second;
                CueList.this.removeEvent(cue, cue.mStartTimeMs);
                if (cue.mInnerTimesMs != null) {
                    for (long timeMs : cue.mInnerTimesMs) {
                        CueList.this.removeEvent(cue, timeMs);
                    }
                }
            }

            public EntryIterator(SortedMap<Long, Vector<Cue>> cues) {
                if (CueList.this.DEBUG) {
                    Log.v(CueList.TAG, cues + ProxyInfo.LOCAL_EXCL_LIST);
                }
                this.mRemainingCues = cues;
                this.mLastListIterator = null;
                nextKey();
            }

            private void nextKey() {
                do {
                    try {
                        if (this.mRemainingCues == null) {
                            throw new NoSuchElementException(ProxyInfo.LOCAL_EXCL_LIST);
                        }
                        this.mCurrentTimeMs = ((Long) this.mRemainingCues.firstKey()).longValue();
                        this.mListIterator = ((Vector) this.mRemainingCues.get(Long.valueOf(this.mCurrentTimeMs))).iterator();
                        try {
                            this.mRemainingCues = this.mRemainingCues.tailMap(Long.valueOf(this.mCurrentTimeMs + 1));
                        } catch (IllegalArgumentException e) {
                            this.mRemainingCues = null;
                        }
                        this.mDone = false;
                    } catch (NoSuchElementException e2) {
                        this.mDone = true;
                        this.mRemainingCues = null;
                        this.mListIterator = null;
                        return;
                    }
                } while (!this.mListIterator.hasNext());
            }
        }

        private boolean addEvent(Cue cue, long timeMs) {
            Vector<Cue> cues = (Vector) this.mCues.get(Long.valueOf(timeMs));
            if (cues == null) {
                cues = new Vector(2);
                this.mCues.put(Long.valueOf(timeMs), cues);
            } else if (cues.contains(cue)) {
                return false;
            }
            cues.add(cue);
            return true;
        }

        private void removeEvent(Cue cue, long timeMs) {
            Vector<Cue> cues = (Vector) this.mCues.get(Long.valueOf(timeMs));
            if (cues != null) {
                cues.remove(cue);
                if (cues.size() == 0) {
                    this.mCues.remove(Long.valueOf(timeMs));
                }
            }
        }

        public void add(Cue cue) {
            if (cue.mStartTimeMs < cue.mEndTimeMs && addEvent(cue, cue.mStartTimeMs)) {
                long lastTimeMs = cue.mStartTimeMs;
                if (cue.mInnerTimesMs != null) {
                    for (long timeMs : cue.mInnerTimesMs) {
                        if (timeMs > lastTimeMs && timeMs < cue.mEndTimeMs) {
                            addEvent(cue, timeMs);
                            lastTimeMs = timeMs;
                        }
                    }
                }
                addEvent(cue, cue.mEndTimeMs);
            }
        }

        public void remove(Cue cue) {
            removeEvent(cue, cue.mStartTimeMs);
            if (cue.mInnerTimesMs != null) {
                for (long timeMs : cue.mInnerTimesMs) {
                    removeEvent(cue, timeMs);
                }
            }
            removeEvent(cue, cue.mEndTimeMs);
        }

        public Iterable<Pair<Long, Cue>> entriesBetween(long lastTimeMs, long timeMs) {
            final long j = lastTimeMs;
            final long j2 = timeMs;
            return new Iterable<Pair<Long, Cue>>() {
                public Iterator<Pair<Long, Cue>> iterator() {
                    if (CueList.this.DEBUG) {
                        Log.d(CueList.TAG, "slice (" + j + ", " + j2 + "]=");
                    }
                    try {
                        return new EntryIterator(CueList.this.mCues.subMap(Long.valueOf(j + 1), Long.valueOf(j2 + 1)));
                    } catch (IllegalArgumentException e) {
                        return new EntryIterator(null);
                    }
                }
            };
        }

        public long nextTimeAfter(long timeMs) {
            try {
                SortedMap<Long, Vector<Cue>> tail = this.mCues.tailMap(Long.valueOf(1 + timeMs));
                if (tail != null) {
                    return ((Long) tail.firstKey()).longValue();
                }
                return -1;
            } catch (IllegalArgumentException e) {
                return -1;
            } catch (NoSuchElementException e2) {
                return -1;
            }
        }

        CueList() {
        }
    }

    private static class Run {
        static final /* synthetic */ boolean $assertionsDisabled = (!SubtitleTrack.class.desiredAssertionStatus());
        public long mEndTimeMs;
        public Cue mFirstCue;
        public Run mNextRunAtEndTimeMs;
        public Run mPrevRunAtEndTimeMs;
        public long mRunID;
        private long mStoredEndTimeMs;

        private Run() {
            this.mEndTimeMs = -1;
            this.mRunID = 0;
            this.mStoredEndTimeMs = -1;
        }

        public void storeByEndTimeMs(LongSparseArray<Run> runsByEndTime) {
            int ix = runsByEndTime.indexOfKey(this.mStoredEndTimeMs);
            if (ix >= 0) {
                if (this.mPrevRunAtEndTimeMs == null) {
                    if (!$assertionsDisabled && this != runsByEndTime.valueAt(ix)) {
                        throw new AssertionError();
                    } else if (this.mNextRunAtEndTimeMs == null) {
                        runsByEndTime.removeAt(ix);
                    } else {
                        runsByEndTime.setValueAt(ix, this.mNextRunAtEndTimeMs);
                    }
                }
                removeAtEndTimeMs();
            }
            if (this.mEndTimeMs >= 0) {
                this.mPrevRunAtEndTimeMs = null;
                this.mNextRunAtEndTimeMs = (Run) runsByEndTime.get(this.mEndTimeMs);
                if (this.mNextRunAtEndTimeMs != null) {
                    this.mNextRunAtEndTimeMs.mPrevRunAtEndTimeMs = this;
                }
                runsByEndTime.put(this.mEndTimeMs, this);
                this.mStoredEndTimeMs = this.mEndTimeMs;
            }
        }

        public void removeAtEndTimeMs() {
            Run prev = this.mPrevRunAtEndTimeMs;
            if (this.mPrevRunAtEndTimeMs != null) {
                this.mPrevRunAtEndTimeMs.mNextRunAtEndTimeMs = this.mNextRunAtEndTimeMs;
                this.mPrevRunAtEndTimeMs = null;
            }
            if (this.mNextRunAtEndTimeMs != null) {
                this.mNextRunAtEndTimeMs.mPrevRunAtEndTimeMs = prev;
                this.mNextRunAtEndTimeMs = null;
            }
        }
    }

    public abstract RenderingWidget getRenderingWidget();

    public abstract void onData(byte[] bArr, boolean z, long j);

    public abstract void updateView(Vector<Cue> vector);

    public SubtitleTrack(MediaFormat format) {
        this.mFormat = format;
        this.mCues = new CueList();
        clearActiveCues();
        this.mLastTimeMs = -1;
    }

    public final MediaFormat getFormat() {
        return this.mFormat;
    }

    protected void onData(SubtitleData data) {
        long runID = data.getStartTimeUs() + 1;
        onData(data.getData(), true, runID);
        setRunDiscardTimeMs(runID, (data.getStartTimeUs() + data.getDurationUs()) / 1000);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected synchronized void updateActiveCues(boolean r9, long r10) {
        /*
        r8 = this;
        monitor-enter(r8);
        if (r9 != 0) goto L_0x0009;
    L_0x0003:
        r4 = r8.mLastUpdateTimeMs;	 Catch:{ all -> 0x0063 }
        r3 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r3 <= 0) goto L_0x000c;
    L_0x0009:
        r8.clearActiveCues();	 Catch:{ all -> 0x0063 }
    L_0x000c:
        r3 = r8.mCues;	 Catch:{ all -> 0x0063 }
        r4 = r8.mLastUpdateTimeMs;	 Catch:{ all -> 0x0063 }
        r3 = r3.entriesBetween(r4, r10);	 Catch:{ all -> 0x0063 }
        r2 = r3.iterator();	 Catch:{ all -> 0x0063 }
    L_0x0018:
        r3 = r2.hasNext();	 Catch:{ all -> 0x0063 }
        if (r3 == 0) goto L_0x00a7;
    L_0x001e:
        r1 = r2.next();	 Catch:{ all -> 0x0063 }
        r1 = (android.util.Pair) r1;	 Catch:{ all -> 0x0063 }
        r0 = r1.second;	 Catch:{ all -> 0x0063 }
        r0 = (android.media.SubtitleTrack.Cue) r0;	 Catch:{ all -> 0x0063 }
        r4 = r0.mEndTimeMs;	 Catch:{ all -> 0x0063 }
        r3 = r1.first;	 Catch:{ all -> 0x0063 }
        r3 = (java.lang.Long) r3;	 Catch:{ all -> 0x0063 }
        r6 = r3.longValue();	 Catch:{ all -> 0x0063 }
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 != 0) goto L_0x0066;
    L_0x0036:
        r3 = r8.DEBUG;	 Catch:{ all -> 0x0063 }
        if (r3 == 0) goto L_0x0052;
    L_0x003a:
        r3 = "SubtitleTrack";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0063 }
        r4.<init>();	 Catch:{ all -> 0x0063 }
        r5 = "Removing ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0063 }
        r4 = r4.append(r0);	 Catch:{ all -> 0x0063 }
        r4 = r4.toString();	 Catch:{ all -> 0x0063 }
        android.util.Log.v(r3, r4);	 Catch:{ all -> 0x0063 }
    L_0x0052:
        r3 = r8.mActiveCues;	 Catch:{ all -> 0x0063 }
        r3.remove(r0);	 Catch:{ all -> 0x0063 }
        r4 = r0.mRunID;	 Catch:{ all -> 0x0063 }
        r6 = 0;
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 != 0) goto L_0x0018;
    L_0x005f:
        r2.remove();	 Catch:{ all -> 0x0063 }
        goto L_0x0018;
    L_0x0063:
        r3 = move-exception;
        monitor-exit(r8);
        throw r3;
    L_0x0066:
        r4 = r0.mStartTimeMs;	 Catch:{ all -> 0x0063 }
        r3 = r1.first;	 Catch:{ all -> 0x0063 }
        r3 = (java.lang.Long) r3;	 Catch:{ all -> 0x0063 }
        r6 = r3.longValue();	 Catch:{ all -> 0x0063 }
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 != 0) goto L_0x009e;
    L_0x0074:
        r3 = r8.DEBUG;	 Catch:{ all -> 0x0063 }
        if (r3 == 0) goto L_0x0090;
    L_0x0078:
        r3 = "SubtitleTrack";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0063 }
        r4.<init>();	 Catch:{ all -> 0x0063 }
        r5 = "Adding ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0063 }
        r4 = r4.append(r0);	 Catch:{ all -> 0x0063 }
        r4 = r4.toString();	 Catch:{ all -> 0x0063 }
        android.util.Log.v(r3, r4);	 Catch:{ all -> 0x0063 }
    L_0x0090:
        r3 = r0.mInnerTimesMs;	 Catch:{ all -> 0x0063 }
        if (r3 == 0) goto L_0x0097;
    L_0x0094:
        r0.onTime(r10);	 Catch:{ all -> 0x0063 }
    L_0x0097:
        r3 = r8.mActiveCues;	 Catch:{ all -> 0x0063 }
        r3.add(r0);	 Catch:{ all -> 0x0063 }
        goto L_0x0018;
    L_0x009e:
        r3 = r0.mInnerTimesMs;	 Catch:{ all -> 0x0063 }
        if (r3 == 0) goto L_0x0018;
    L_0x00a2:
        r0.onTime(r10);	 Catch:{ all -> 0x0063 }
        goto L_0x0018;
    L_0x00a7:
        r3 = r8.mRunsByEndTime;	 Catch:{ all -> 0x0063 }
        r3 = r3.size();	 Catch:{ all -> 0x0063 }
        if (r3 <= 0) goto L_0x00bf;
    L_0x00af:
        r3 = r8.mRunsByEndTime;	 Catch:{ all -> 0x0063 }
        r4 = 0;
        r4 = r3.keyAt(r4);	 Catch:{ all -> 0x0063 }
        r3 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r3 > 0) goto L_0x00bf;
    L_0x00ba:
        r3 = 0;
        r8.removeRunsByEndTimeIndex(r3);	 Catch:{ all -> 0x0063 }
        goto L_0x00a7;
    L_0x00bf:
        r8.mLastUpdateTimeMs = r10;	 Catch:{ all -> 0x0063 }
        monitor-exit(r8);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleTrack.updateActiveCues(boolean, long):void");
    }

    private void removeRunsByEndTimeIndex(int ix) {
        Run run = (Run) this.mRunsByEndTime.valueAt(ix);
        while (run != null) {
            Cue cue = run.mFirstCue;
            while (cue != null) {
                this.mCues.remove(cue);
                Cue nextCue = cue.mNextInRun;
                cue.mNextInRun = null;
                cue = nextCue;
            }
            this.mRunsByID.remove(run.mRunID);
            Run nextRun = run.mNextRunAtEndTimeMs;
            run.mPrevRunAtEndTimeMs = null;
            run.mNextRunAtEndTimeMs = null;
            run = nextRun;
        }
        this.mRunsByEndTime.removeAt(ix);
    }

    protected void finalize() throws Throwable {
        for (int ix = this.mRunsByEndTime.size() - 1; ix >= 0; ix--) {
            removeRunsByEndTimeIndex(ix);
        }
        super.finalize();
    }

    private synchronized void takeTime(long timeMs) {
        this.mLastTimeMs = timeMs;
    }

    protected synchronized void clearActiveCues() {
        if (this.DEBUG) {
            Log.v(TAG, "Clearing " + this.mActiveCues.size() + " active cues");
        }
        this.mActiveCues.clear();
        this.mLastUpdateTimeMs = -1;
    }

    protected void scheduleTimedEvents() {
        if (this.mTimeProvider != null) {
            this.mNextScheduledTimeMs = this.mCues.nextTimeAfter(this.mLastTimeMs);
            if (this.DEBUG) {
                Log.d(TAG, "sched @" + this.mNextScheduledTimeMs + " after " + this.mLastTimeMs);
            }
            this.mTimeProvider.notifyAt(this.mNextScheduledTimeMs >= 0 ? this.mNextScheduledTimeMs * 1000 : -1, this);
        }
    }

    public void onTimedEvent(long timeUs) {
        if (this.DEBUG) {
            Log.d(TAG, "onTimedEvent " + timeUs);
        }
        synchronized (this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(false, timeMs);
            takeTime(timeMs);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    public void onSeek(long timeUs) {
        if (this.DEBUG) {
            Log.d(TAG, "onSeek " + timeUs);
        }
        synchronized (this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(true, timeMs);
            takeTime(timeMs);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    public void onStop() {
        synchronized (this) {
            if (this.DEBUG) {
                Log.d(TAG, "onStop");
            }
            clearActiveCues();
            this.mLastTimeMs = -1;
        }
        updateView(this.mActiveCues);
        this.mNextScheduledTimeMs = -1;
        this.mTimeProvider.notifyAt(-1, this);
    }

    public void show() {
        if (!this.mVisible) {
            this.mVisible = true;
            RenderingWidget renderingWidget = getRenderingWidget();
            if (renderingWidget != null) {
                renderingWidget.setVisible(true);
            }
            if (this.mTimeProvider != null) {
                this.mTimeProvider.scheduleUpdate(this);
            }
        }
    }

    public void hide() {
        if (this.mVisible) {
            if (this.mTimeProvider != null) {
                this.mTimeProvider.cancelNotifications(this);
            }
            RenderingWidget renderingWidget = getRenderingWidget();
            if (renderingWidget != null) {
                renderingWidget.setVisible(false);
            }
            this.mVisible = false;
        }
    }

    protected synchronized boolean addCue(Cue cue) {
        boolean z = true;
        synchronized (this) {
            this.mCues.add(cue);
            if (cue.mRunID != 0) {
                Run run = (Run) this.mRunsByID.get(cue.mRunID);
                if (run == null) {
                    run = new Run();
                    this.mRunsByID.put(cue.mRunID, run);
                    run.mEndTimeMs = cue.mEndTimeMs;
                } else if (run.mEndTimeMs < cue.mEndTimeMs) {
                    run.mEndTimeMs = cue.mEndTimeMs;
                }
                cue.mNextInRun = run.mFirstCue;
                run.mFirstCue = cue;
            }
            long nowMs = -1;
            if (this.mTimeProvider != null) {
                try {
                    nowMs = this.mTimeProvider.getCurrentTimeUs(false, true) / 1000;
                } catch (IllegalStateException e) {
                }
            }
            if (this.DEBUG) {
                Log.v(TAG, "mVisible=" + this.mVisible + ", " + cue.mStartTimeMs + " <= " + nowMs + ", " + cue.mEndTimeMs + " >= " + this.mLastTimeMs);
            }
            if (!this.mVisible || cue.mStartTimeMs > nowMs || cue.mEndTimeMs < this.mLastTimeMs) {
                if (this.mVisible && cue.mEndTimeMs >= this.mLastTimeMs && (cue.mStartTimeMs < this.mNextScheduledTimeMs || this.mNextScheduledTimeMs < 0)) {
                    scheduleTimedEvents();
                }
                z = false;
            } else {
                if (this.mRunnable != null) {
                    this.mHandler.removeCallbacks(this.mRunnable);
                }
                final SubtitleTrack track = this;
                final long thenMs = nowMs;
                this.mRunnable = new Runnable() {
                    public void run() {
                        synchronized (track) {
                            SubtitleTrack.this.mRunnable = null;
                            SubtitleTrack.this.updateActiveCues(true, thenMs);
                            SubtitleTrack.this.updateView(SubtitleTrack.this.mActiveCues);
                        }
                    }
                };
                if (this.mHandler.postDelayed(this.mRunnable, 10)) {
                    if (this.DEBUG) {
                        Log.v(TAG, "scheduling update");
                    }
                } else if (this.DEBUG) {
                    Log.w(TAG, "failed to schedule subtitle view update");
                }
            }
        }
        return z;
    }

    public synchronized void setTimeProvider(MediaTimeProvider timeProvider) {
        if (this.mTimeProvider != timeProvider) {
            if (this.mTimeProvider != null) {
                this.mTimeProvider.cancelNotifications(this);
            }
            this.mTimeProvider = timeProvider;
            if (this.mTimeProvider != null) {
                this.mTimeProvider.scheduleUpdate(this);
            }
        }
    }

    protected void finishedRun(long runID) {
        if (runID != 0 && runID != -1) {
            Run run = (Run) this.mRunsByID.get(runID);
            if (run != null) {
                run.storeByEndTimeMs(this.mRunsByEndTime);
            }
        }
    }

    public void setRunDiscardTimeMs(long runID, long timeMs) {
        if (runID != 0 && runID != -1) {
            Run run = (Run) this.mRunsByID.get(runID);
            if (run != null) {
                run.mEndTimeMs = timeMs;
                run.storeByEndTimeMs(this.mRunsByEndTime);
            }
        }
    }

    public int getTrackType() {
        return getRenderingWidget() == null ? 3 : 4;
    }
}
