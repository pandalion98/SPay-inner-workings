package android.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.util.TimedRemoteCaller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.RemoteViews.OnClickHandler;
import com.android.internal.R;
import com.android.internal.widget.IRemoteViewsAdapterConnection.Stub;
import com.android.internal.widget.IRemoteViewsFactory;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class RemoteViewsAdapter extends BaseAdapter implements Callback {
    private static final String MULTI_USER_PERM = "android.permission.INTERACT_ACROSS_USERS_FULL";
    private static final int REMOTE_VIEWS_CACHE_DURATION = 5000;
    private static final String TAG = "RemoteViewsAdapter";
    private static Handler sCacheRemovalQueue = null;
    private static HandlerThread sCacheRemovalThread = null;
    private static final HashMap<RemoteViewsCacheKey, FixedSizeRemoteViewsCache> sCachedRemoteViewsCaches = new HashMap();
    private static final int sDefaultCacheSize = 40;
    private static final int sDefaultLoadingViewHeight = 50;
    private static final int sDefaultMessageType = 0;
    private static final HashMap<RemoteViewsCacheKey, Runnable> sRemoteViewsCacheRemoveRunnables = new HashMap();
    private static final int sUnbindServiceDelay = 5000;
    private static final int sUnbindServiceMessageType = 1;
    private final int mAppWidgetId;
    private FixedSizeRemoteViewsCache mCache;
    private WeakReference<RemoteAdapterConnectionCallback> mCallback;
    private final Context mContext;
    private boolean mDataReady = false;
    private final Intent mIntent;
    private LayoutInflater mLayoutInflater;
    private Handler mMainQueue;
    private boolean mNotifyDataSetChangedAfterOnServiceConnected = false;
    private OnClickHandler mRemoteViewsOnClickHandler;
    private RemoteViewsFrameLayoutRefSet mRequestedViews;
    private RemoteViewsAdapterServiceConnection mServiceConnection;
    private int mVisibleWindowLowerBound;
    private int mVisibleWindowUpperBound;
    private Handler mWorkerQueue;
    private HandlerThread mWorkerThread;

    public interface RemoteAdapterConnectionCallback {
        void deferNotifyDataSetChanged();

        boolean onRemoteAdapterConnected();

        void onRemoteAdapterDisconnected();
    }

    private static class FixedSizeRemoteViewsCache {
        private static final String TAG = "FixedSizeRemoteViewsCache";
        private static final float sMaxCountSlackPercent = 0.75f;
        private static final int sMaxMemoryLimitInBytes = 2097152;
        private HashMap<Integer, RemoteViewsIndexMetaData> mIndexMetaData = new HashMap();
        private HashMap<Integer, RemoteViews> mIndexRemoteViews = new HashMap();
        private int mLastRequestedIndex = -1;
        private HashSet<Integer> mLoadIndices = new HashSet();
        private int mMaxCount;
        private int mMaxCountSlack = Math.round(sMaxCountSlackPercent * ((float) (this.mMaxCount / 2)));
        private final RemoteViewsMetaData mMetaData = new RemoteViewsMetaData();
        private int mPreloadLowerBound = 0;
        private int mPreloadUpperBound = -1;
        private HashSet<Integer> mRequestedIndices = new HashSet();
        private final RemoteViewsMetaData mTemporaryMetaData = new RemoteViewsMetaData();

        public FixedSizeRemoteViewsCache(int maxCacheSize) {
            this.mMaxCount = maxCacheSize;
        }

        public void insert(int position, RemoteViews v, long itemId, ArrayList<Integer> visibleWindow) {
            int pruneFromPosition;
            if (this.mIndexRemoteViews.size() >= this.mMaxCount) {
                this.mIndexRemoteViews.remove(Integer.valueOf(getFarthestPositionFrom(position, visibleWindow)));
            }
            if (this.mLastRequestedIndex > -1) {
                pruneFromPosition = this.mLastRequestedIndex;
            } else {
                pruneFromPosition = position;
            }
            while (getRemoteViewsBitmapMemoryUsage() >= 2097152) {
                int trimIndex = getFarthestPositionFrom(pruneFromPosition, visibleWindow);
                if (trimIndex < 0) {
                    break;
                }
                this.mIndexRemoteViews.remove(Integer.valueOf(trimIndex));
            }
            if (this.mIndexMetaData.containsKey(Integer.valueOf(position))) {
                ((RemoteViewsIndexMetaData) this.mIndexMetaData.get(Integer.valueOf(position))).set(v, itemId);
            } else {
                this.mIndexMetaData.put(Integer.valueOf(position), new RemoteViewsIndexMetaData(v, itemId));
            }
            this.mIndexRemoteViews.put(Integer.valueOf(position), v);
        }

        public RemoteViewsMetaData getMetaData() {
            return this.mMetaData;
        }

        public RemoteViewsMetaData getTemporaryMetaData() {
            return this.mTemporaryMetaData;
        }

        public RemoteViews getRemoteViewsAt(int position) {
            if (this.mIndexRemoteViews.containsKey(Integer.valueOf(position))) {
                return (RemoteViews) this.mIndexRemoteViews.get(Integer.valueOf(position));
            }
            return null;
        }

        public RemoteViewsIndexMetaData getMetaDataAt(int position) {
            if (this.mIndexMetaData.containsKey(Integer.valueOf(position))) {
                return (RemoteViewsIndexMetaData) this.mIndexMetaData.get(Integer.valueOf(position));
            }
            return null;
        }

        public void commitTemporaryMetaData() {
            synchronized (this.mTemporaryMetaData) {
                synchronized (this.mMetaData) {
                    this.mMetaData.set(this.mTemporaryMetaData);
                }
            }
        }

        private int getRemoteViewsBitmapMemoryUsage() {
            int mem = 0;
            for (Integer i : this.mIndexRemoteViews.keySet()) {
                RemoteViews v = (RemoteViews) this.mIndexRemoteViews.get(i);
                if (v != null) {
                    mem += v.estimateMemoryUsage();
                }
            }
            return mem;
        }

        private int getFarthestPositionFrom(int pos, ArrayList<Integer> visibleWindow) {
            int maxDist = 0;
            int maxDistIndex = -1;
            int maxDistNotVisible = 0;
            int maxDistIndexNotVisible = -1;
            for (Integer intValue : this.mIndexRemoteViews.keySet()) {
                int i = intValue.intValue();
                int dist = Math.abs(i - pos);
                if (dist > maxDistNotVisible && !visibleWindow.contains(Integer.valueOf(i))) {
                    maxDistIndexNotVisible = i;
                    maxDistNotVisible = dist;
                }
                if (dist >= maxDist) {
                    maxDistIndex = i;
                    maxDist = dist;
                }
            }
            return maxDistIndexNotVisible > -1 ? maxDistIndexNotVisible : maxDistIndex;
        }

        public void queueRequestedPositionToLoad(int position) {
            this.mLastRequestedIndex = position;
            synchronized (this.mLoadIndices) {
                this.mRequestedIndices.add(Integer.valueOf(position));
                this.mLoadIndices.add(Integer.valueOf(position));
            }
        }

        public boolean queuePositionsToBePreloadedFromRequestedPosition(int position) {
            if (this.mPreloadLowerBound <= position && position <= this.mPreloadUpperBound && Math.abs(position - ((this.mPreloadUpperBound + this.mPreloadLowerBound) / 2)) < this.mMaxCountSlack) {
                return false;
            }
            synchronized (this.mMetaData) {
                int count = this.mMetaData.count;
            }
            synchronized (this.mLoadIndices) {
                this.mLoadIndices.clear();
                this.mLoadIndices.addAll(this.mRequestedIndices);
                int halfMaxCount = this.mMaxCount / 2;
                this.mPreloadLowerBound = position - halfMaxCount;
                this.mPreloadUpperBound = position + halfMaxCount;
                int effectiveLowerBound = Math.max(0, this.mPreloadLowerBound);
                int effectiveUpperBound = Math.min(this.mPreloadUpperBound, count - 1);
                for (int i = effectiveLowerBound; i <= effectiveUpperBound; i++) {
                    this.mLoadIndices.add(Integer.valueOf(i));
                }
                this.mLoadIndices.removeAll(this.mIndexRemoteViews.keySet());
            }
            return true;
        }

        public int[] getNextIndexToLoad() {
            int[] iArr;
            synchronized (this.mLoadIndices) {
                Integer i;
                if (!this.mRequestedIndices.isEmpty()) {
                    i = (Integer) this.mRequestedIndices.iterator().next();
                    this.mRequestedIndices.remove(i);
                    this.mLoadIndices.remove(i);
                    iArr = new int[]{i.intValue(), 1};
                } else if (this.mLoadIndices.isEmpty()) {
                    iArr = new int[]{-1, 0};
                } else {
                    this.mLoadIndices.remove((Integer) this.mLoadIndices.iterator().next());
                    iArr = new int[]{i.intValue(), 0};
                }
            }
            return iArr;
        }

        public boolean containsRemoteViewAt(int position) {
            return this.mIndexRemoteViews.containsKey(Integer.valueOf(position));
        }

        public boolean containsMetaDataAt(int position) {
            return this.mIndexMetaData.containsKey(Integer.valueOf(position));
        }

        public void reset() {
            this.mPreloadLowerBound = 0;
            this.mPreloadUpperBound = -1;
            this.mLastRequestedIndex = -1;
            this.mIndexRemoteViews.clear();
            this.mIndexMetaData.clear();
            synchronized (this.mLoadIndices) {
                this.mRequestedIndices.clear();
                this.mLoadIndices.clear();
            }
        }
    }

    private static class RemoteViewsAdapterServiceConnection extends Stub {
        private WeakReference<RemoteViewsAdapter> mAdapter;
        private boolean mIsConnected;
        private boolean mIsConnecting;
        private IRemoteViewsFactory mRemoteViewsFactory;

        public RemoteViewsAdapterServiceConnection(RemoteViewsAdapter adapter) {
            this.mAdapter = new WeakReference(adapter);
        }

        public synchronized void bind(Context context, int appWidgetId, Intent intent) {
            if (!this.mIsConnecting) {
                try {
                    AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                    if (((RemoteViewsAdapter) this.mAdapter.get()) != null) {
                        mgr.bindRemoteViewsService(context.getOpPackageName(), appWidgetId, intent, asBinder());
                    } else {
                        Slog.w(RemoteViewsAdapter.TAG, "bind: adapter was null");
                    }
                    this.mIsConnecting = true;
                } catch (Exception e) {
                    Log.e("RemoteViewsAdapterServiceConnection", "bind(): " + e.getMessage());
                    this.mIsConnecting = false;
                    this.mIsConnected = false;
                }
            }
        }

        public synchronized void unbind(Context context, int appWidgetId, Intent intent) {
            try {
                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                if (((RemoteViewsAdapter) this.mAdapter.get()) != null) {
                    mgr.unbindRemoteViewsService(context.getOpPackageName(), appWidgetId, intent);
                } else {
                    Slog.w(RemoteViewsAdapter.TAG, "unbind: adapter was null");
                }
                this.mIsConnecting = false;
            } catch (Exception e) {
                Log.e("RemoteViewsAdapterServiceConnection", "unbind(): " + e.getMessage());
                this.mIsConnecting = false;
                this.mIsConnected = false;
            }
        }

        public synchronized void onServiceConnected(IBinder service) {
            this.mRemoteViewsFactory = IRemoteViewsFactory.Stub.asInterface(service);
            final RemoteViewsAdapter adapter = (RemoteViewsAdapter) this.mAdapter.get();
            if (adapter != null) {
                adapter.mWorkerQueue.post(new Runnable() {
                    public void run() {
                        if (adapter.mNotifyDataSetChangedAfterOnServiceConnected) {
                            adapter.onNotifyDataSetChanged();
                        } else {
                            IRemoteViewsFactory factory = adapter.mServiceConnection.getRemoteViewsFactory();
                            try {
                                if (!factory.isCreated()) {
                                    factory.onDataSetChanged();
                                }
                            } catch (RemoteException e) {
                                Log.e(RemoteViewsAdapter.TAG, "Error notifying factory of data set changed in onServiceConnected(): " + e.getMessage());
                                return;
                            } catch (RuntimeException e2) {
                                Log.e(RemoteViewsAdapter.TAG, "Error notifying factory of data set changed in onServiceConnected(): " + e2.getMessage());
                            }
                            adapter.updateTemporaryMetaData();
                            adapter.mMainQueue.post(new Runnable() {
                                public void run() {
                                    synchronized (adapter.mCache) {
                                        adapter.mCache.commitTemporaryMetaData();
                                    }
                                    RemoteAdapterConnectionCallback callback = (RemoteAdapterConnectionCallback) adapter.mCallback.get();
                                    if (callback != null) {
                                        callback.onRemoteAdapterConnected();
                                    }
                                }
                            });
                        }
                        adapter.enqueueDeferredUnbindServiceMessage();
                        RemoteViewsAdapterServiceConnection.this.mIsConnected = true;
                        RemoteViewsAdapterServiceConnection.this.mIsConnecting = false;
                    }
                });
            }
        }

        public synchronized void onServiceDisconnected() {
            this.mIsConnected = false;
            this.mIsConnecting = false;
            this.mRemoteViewsFactory = null;
            final RemoteViewsAdapter adapter = (RemoteViewsAdapter) this.mAdapter.get();
            if (adapter != null) {
                adapter.mMainQueue.post(new Runnable() {
                    public void run() {
                        adapter.mMainQueue.removeMessages(1);
                        RemoteAdapterConnectionCallback callback = (RemoteAdapterConnectionCallback) adapter.mCallback.get();
                        if (callback != null) {
                            callback.onRemoteAdapterDisconnected();
                        }
                    }
                });
            }
        }

        public synchronized IRemoteViewsFactory getRemoteViewsFactory() {
            return this.mRemoteViewsFactory;
        }

        public synchronized boolean isConnected() {
            return this.mIsConnected;
        }
    }

    static class RemoteViewsCacheKey {
        final FilterComparison filter;
        final int widgetId;

        RemoteViewsCacheKey(FilterComparison filter, int widgetId) {
            this.filter = filter;
            this.widgetId = widgetId;
        }

        public boolean equals(Object o) {
            if (!(o instanceof RemoteViewsCacheKey)) {
                return false;
            }
            RemoteViewsCacheKey other = (RemoteViewsCacheKey) o;
            if (other.filter.equals(this.filter) && other.widgetId == this.widgetId) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.filter == null ? 0 : this.filter.hashCode()) ^ (this.widgetId << 2);
        }
    }

    private static class RemoteViewsFrameLayout extends FrameLayout {
        public RemoteViewsFrameLayout(Context context) {
            super(context);
        }

        public void onRemoteViewsLoaded(RemoteViews view, OnClickHandler handler) {
            try {
                removeAllViews();
                addView(view.apply(getContext(), this, handler));
            } catch (Exception e) {
                Log.e(RemoteViewsAdapter.TAG, "Failed to apply RemoteViews.");
            }
        }
    }

    private class RemoteViewsFrameLayoutRefSet {
        private HashMap<Integer, LinkedList<RemoteViewsFrameLayout>> mReferences = new HashMap();
        private HashMap<RemoteViewsFrameLayout, LinkedList<RemoteViewsFrameLayout>> mViewToLinkedList = new HashMap();

        public void add(int position, RemoteViewsFrameLayout layout) {
            LinkedList<RemoteViewsFrameLayout> refs;
            Integer pos = Integer.valueOf(position);
            if (this.mReferences.containsKey(pos)) {
                refs = (LinkedList) this.mReferences.get(pos);
            } else {
                refs = new LinkedList();
                this.mReferences.put(pos, refs);
            }
            this.mViewToLinkedList.put(layout, refs);
            refs.add(layout);
        }

        public void notifyOnRemoteViewsLoaded(int position, RemoteViews view) {
            if (view != null) {
                Integer pos = Integer.valueOf(position);
                if (this.mReferences.containsKey(pos)) {
                    LinkedList<RemoteViewsFrameLayout> refs = (LinkedList) this.mReferences.get(pos);
                    Iterator i$ = refs.iterator();
                    while (i$.hasNext()) {
                        RemoteViewsFrameLayout ref = (RemoteViewsFrameLayout) i$.next();
                        ref.onRemoteViewsLoaded(view, RemoteViewsAdapter.this.mRemoteViewsOnClickHandler);
                        if (this.mViewToLinkedList.containsKey(ref)) {
                            this.mViewToLinkedList.remove(ref);
                        }
                    }
                    refs.clear();
                    this.mReferences.remove(pos);
                }
            }
        }

        public void removeView(RemoteViewsFrameLayout rvfl) {
            if (this.mViewToLinkedList.containsKey(rvfl)) {
                ((LinkedList) this.mViewToLinkedList.get(rvfl)).remove(rvfl);
                this.mViewToLinkedList.remove(rvfl);
            }
        }

        public void clear() {
            this.mReferences.clear();
            this.mViewToLinkedList.clear();
        }
    }

    private static class RemoteViewsIndexMetaData {
        long itemId;
        int typeId;

        public RemoteViewsIndexMetaData(RemoteViews v, long itemId) {
            set(v, itemId);
        }

        public void set(RemoteViews v, long id) {
            this.itemId = id;
            if (v != null) {
                this.typeId = v.getLayoutId();
            } else {
                this.typeId = 0;
            }
        }
    }

    private static class RemoteViewsMetaData {
        int count;
        boolean hasStableIds;
        RemoteViews mFirstView;
        int mFirstViewHeight;
        private final HashMap<Integer, Integer> mTypeIdIndexMap = new HashMap();
        RemoteViews mUserLoadingView;
        int viewTypeCount;

        public RemoteViewsMetaData() {
            reset();
        }

        public void set(RemoteViewsMetaData d) {
            synchronized (d) {
                this.count = d.count;
                this.viewTypeCount = d.viewTypeCount;
                this.hasStableIds = d.hasStableIds;
                setLoadingViewTemplates(d.mUserLoadingView, d.mFirstView);
            }
        }

        public void reset() {
            this.count = 0;
            this.viewTypeCount = 1;
            this.hasStableIds = true;
            this.mUserLoadingView = null;
            this.mFirstView = null;
            this.mFirstViewHeight = 0;
            this.mTypeIdIndexMap.clear();
        }

        public void setLoadingViewTemplates(RemoteViews loadingView, RemoteViews firstView) {
            this.mUserLoadingView = loadingView;
            if (firstView != null) {
                this.mFirstView = firstView;
                this.mFirstViewHeight = -1;
            }
        }

        public int getMappedViewType(int typeId) {
            if (this.mTypeIdIndexMap.containsKey(Integer.valueOf(typeId))) {
                return ((Integer) this.mTypeIdIndexMap.get(Integer.valueOf(typeId))).intValue();
            }
            int incrementalTypeId = this.mTypeIdIndexMap.size() + 1;
            this.mTypeIdIndexMap.put(Integer.valueOf(typeId), Integer.valueOf(incrementalTypeId));
            return incrementalTypeId;
        }

        public boolean isViewTypeInRange(int typeId) {
            if (getMappedViewType(typeId) >= this.viewTypeCount) {
                return false;
            }
            return true;
        }

        private RemoteViewsFrameLayout createLoadingView(int position, View convertView, ViewGroup parent, Object lock, LayoutInflater layoutInflater, OnClickHandler handler) {
            Context context = parent.getContext();
            ViewGroup layout = new RemoteViewsFrameLayout(context);
            synchronized (lock) {
                boolean customLoadingViewAvailable = false;
                if (this.mUserLoadingView != null) {
                    try {
                        View loadingView = this.mUserLoadingView.apply(parent.getContext(), parent, handler);
                        loadingView.setTagInternal(R.id.rowTypeId, new Integer(0));
                        layout.addView(loadingView);
                        customLoadingViewAvailable = true;
                    } catch (Exception e) {
                        Log.w(RemoteViewsAdapter.TAG, "Error inflating custom loading view, using default loadingview instead", e);
                    }
                }
                if (!customLoadingViewAvailable) {
                    if (this.mFirstViewHeight < 0) {
                        try {
                            View firstView = this.mFirstView.apply(parent.getContext(), parent, handler);
                            firstView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                            this.mFirstViewHeight = firstView.getMeasuredHeight();
                            this.mFirstView = null;
                        } catch (Exception e2) {
                            this.mFirstViewHeight = Math.round(50.0f * context.getResources().getDisplayMetrics().density);
                            this.mFirstView = null;
                            Log.w(RemoteViewsAdapter.TAG, "Error inflating first RemoteViews" + e2);
                        }
                    }
                    TextView loadingTextView = (TextView) layoutInflater.inflate((int) R.layout.remote_views_adapter_default_loading_view, layout, false);
                    loadingTextView.setHeight(this.mFirstViewHeight);
                    loadingTextView.setTag(new Integer(0));
                    layout.addView(loadingTextView);
                }
            }
            return layout;
        }
    }

    public RemoteViewsAdapter(Context context, Intent intent, RemoteAdapterConnectionCallback callback) {
        this.mContext = context;
        this.mIntent = intent;
        if (this.mIntent == null) {
            throw new IllegalArgumentException("Non-null Intent must be specified.");
        }
        this.mAppWidgetId = intent.getIntExtra("remoteAdapterAppWidgetId", -1);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRequestedViews = new RemoteViewsFrameLayoutRefSet();
        if (intent.hasExtra("remoteAdapterAppWidgetId")) {
            intent.removeExtra("remoteAdapterAppWidgetId");
        }
        this.mWorkerThread = new HandlerThread("RemoteViewsCache-loader");
        this.mWorkerThread.start();
        this.mWorkerQueue = new Handler(this.mWorkerThread.getLooper());
        this.mMainQueue = new Handler(Looper.myLooper(), this);
        if (sCacheRemovalThread == null) {
            sCacheRemovalThread = new HandlerThread("RemoteViewsAdapter-cachePruner");
            sCacheRemovalThread.start();
            sCacheRemovalQueue = new Handler(sCacheRemovalThread.getLooper());
        }
        this.mCallback = new WeakReference(callback);
        this.mServiceConnection = new RemoteViewsAdapterServiceConnection(this);
        RemoteViewsCacheKey key = new RemoteViewsCacheKey(new FilterComparison(this.mIntent), this.mAppWidgetId);
        synchronized (sCachedRemoteViewsCaches) {
            if (sCachedRemoteViewsCaches.containsKey(key)) {
                this.mCache = (FixedSizeRemoteViewsCache) sCachedRemoteViewsCaches.get(key);
                synchronized (this.mCache.mMetaData) {
                    if (this.mCache.mMetaData.count > 0) {
                        this.mDataReady = true;
                    }
                }
            } else {
                this.mCache = new FixedSizeRemoteViewsCache(40);
            }
            if (!this.mDataReady) {
                requestBindService();
            }
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mWorkerThread != null) {
                this.mWorkerThread.quit();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public boolean isDataReady() {
        return this.mDataReady;
    }

    public void setRemoteViewsOnClickHandler(OnClickHandler handler) {
        this.mRemoteViewsOnClickHandler = handler;
    }

    public void saveRemoteViewsCache() {
        final RemoteViewsCacheKey key = new RemoteViewsCacheKey(new FilterComparison(this.mIntent), this.mAppWidgetId);
        synchronized (sCachedRemoteViewsCaches) {
            if (sRemoteViewsCacheRemoveRunnables.containsKey(key)) {
                sCacheRemovalQueue.removeCallbacks((Runnable) sRemoteViewsCacheRemoveRunnables.get(key));
                sRemoteViewsCacheRemoveRunnables.remove(key);
            }
            synchronized (this.mCache.mMetaData) {
                int metaDataCount = this.mCache.mMetaData.count;
            }
            synchronized (this.mCache) {
                int numRemoteViewsCached = this.mCache.mIndexRemoteViews.size();
            }
            if (metaDataCount > 0 && numRemoteViewsCached > 0) {
                sCachedRemoteViewsCaches.put(key, this.mCache);
            }
            Runnable r = new Runnable() {
                public void run() {
                    synchronized (RemoteViewsAdapter.sCachedRemoteViewsCaches) {
                        if (RemoteViewsAdapter.sCachedRemoteViewsCaches.containsKey(key)) {
                            RemoteViewsAdapter.sCachedRemoteViewsCaches.remove(key);
                        }
                        if (RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.containsKey(key)) {
                            RemoteViewsAdapter.sRemoteViewsCacheRemoveRunnables.remove(key);
                        }
                    }
                }
            };
            sRemoteViewsCacheRemoveRunnables.put(key, r);
            sCacheRemovalQueue.postDelayed(r, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
        }
    }

    private void loadNextIndexInBackground() {
        this.mWorkerQueue.post(new Runnable() {
            public void run() {
                if (RemoteViewsAdapter.this.mServiceConnection.isConnected()) {
                    int position;
                    synchronized (RemoteViewsAdapter.this.mCache) {
                        position = RemoteViewsAdapter.this.mCache.getNextIndexToLoad()[0];
                    }
                    if (position > -1) {
                        RemoteViewsAdapter.this.updateRemoteViews(position, true);
                        RemoteViewsAdapter.this.loadNextIndexInBackground();
                        return;
                    }
                    RemoteViewsAdapter.this.enqueueDeferredUnbindServiceMessage();
                }
            }
        });
    }

    private void processException(String method, Exception e) {
        Log.e(TAG, "Error in " + method + ": " + e.getMessage());
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            metaData.reset();
        }
        synchronized (this.mCache) {
            this.mCache.reset();
        }
        this.mMainQueue.post(new Runnable() {
            public void run() {
                RemoteViewsAdapter.this.superNotifyDataSetChanged();
            }
        });
    }

    private void updateTemporaryMetaData() {
        IRemoteViewsFactory factory = this.mServiceConnection.getRemoteViewsFactory();
        try {
            boolean hasStableIds = factory.hasStableIds();
            int viewTypeCount = factory.getViewTypeCount();
            int count = factory.getCount();
            RemoteViews loadingView = factory.getLoadingView();
            RemoteViews firstView = null;
            if (count > 0 && loadingView == null) {
                firstView = factory.getViewAt(0);
            }
            RemoteViewsMetaData tmpMetaData = this.mCache.getTemporaryMetaData();
            synchronized (tmpMetaData) {
                tmpMetaData.hasStableIds = hasStableIds;
                tmpMetaData.viewTypeCount = viewTypeCount + 1;
                tmpMetaData.count = count;
                tmpMetaData.setLoadingViewTemplates(loadingView, firstView);
            }
        } catch (RemoteException e) {
            processException("updateMetaData", e);
        } catch (RuntimeException e2) {
            processException("updateMetaData", e2);
        }
        Log.d("ViewSystem", "updateTemporaryMetaData() has done.");
    }

    private void updateRemoteViews(final int position, boolean notifyWhenLoaded) {
        IRemoteViewsFactory factory = this.mServiceConnection.getRemoteViewsFactory();
        try {
            RemoteViews remoteViews = factory.getViewAt(position);
            long itemId = factory.getItemId(position);
            if (remoteViews == null) {
                Log.e(TAG, "Error in updateRemoteViews(" + position + "): " + " null RemoteViews " + "returned from RemoteViewsFactory.");
                return;
            }
            boolean viewTypeInRange;
            int cacheCount;
            int layoutId = remoteViews.getLayoutId();
            RemoteViewsMetaData metaData = this.mCache.getMetaData();
            synchronized (metaData) {
                viewTypeInRange = metaData.isViewTypeInRange(layoutId);
                cacheCount = this.mCache.mMetaData.count;
            }
            synchronized (this.mCache) {
                if (viewTypeInRange) {
                    this.mCache.insert(position, remoteViews, itemId, getVisibleWindow(this.mVisibleWindowLowerBound, this.mVisibleWindowUpperBound, cacheCount));
                    final RemoteViews rv = remoteViews;
                    if (notifyWhenLoaded) {
                        this.mMainQueue.post(new Runnable() {
                            public void run() {
                                RemoteViewsAdapter.this.mRequestedViews.notifyOnRemoteViewsLoaded(position, rv);
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Error: widget's RemoteViewsFactory returns more view types than  indicated by getViewTypeCount() ");
                }
            }
            Log.d("ViewSystem", "updateRemoteViews(" + position + ") has done.");
        } catch (RemoteException e) {
            Log.e(TAG, "Error in updateRemoteViews(" + position + "): " + e.getMessage());
        } catch (RuntimeException e2) {
            Log.e(TAG, "Error in updateRemoteViews(" + position + "): " + e2.getMessage());
        }
    }

    public Intent getRemoteViewsServiceIntent() {
        return this.mIntent;
    }

    public int getCount() {
        int i;
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            i = metaData.count;
        }
        return i;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        long j;
        synchronized (this.mCache) {
            if (this.mCache.containsMetaDataAt(position)) {
                j = this.mCache.getMetaDataAt(position).itemId;
            } else {
                j = 0;
            }
        }
        return j;
    }

    public int getItemViewType(int position) {
        int mappedViewType;
        synchronized (this.mCache) {
            if (this.mCache.containsMetaDataAt(position)) {
                int typeId = this.mCache.getMetaDataAt(position).typeId;
                RemoteViewsMetaData metaData = this.mCache.getMetaData();
                synchronized (metaData) {
                    mappedViewType = metaData.getMappedViewType(typeId);
                }
            } else {
                mappedViewType = 0;
            }
        }
        return mappedViewType;
    }

    private int getConvertViewTypeId(View convertView) {
        if (convertView == null) {
            return -1;
        }
        Object tag = convertView.getTag(R.id.rowTypeId);
        if (tag != null) {
            return ((Integer) tag).intValue();
        }
        return -1;
    }

    public void setVisibleRangeHint(int lowerBound, int upperBound) {
        this.mVisibleWindowLowerBound = lowerBound;
        this.mVisibleWindowUpperBound = upperBound;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View getView(int r25, android.view.View r26, android.view.ViewGroup r27) {
        /*
        r24 = this;
        r0 = r24;
        r0 = r0.mCache;
        r23 = r0;
        monitor-enter(r23);
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x0124 }
        r0 = r25;
        r16 = r3.containsRemoteViewAt(r0);	 Catch:{ all -> 0x0124 }
        r0 = r24;
        r3 = r0.mServiceConnection;	 Catch:{ all -> 0x0124 }
        r15 = r3.isConnected();	 Catch:{ all -> 0x0124 }
        r13 = 0;
        if (r26 == 0) goto L_0x002e;
    L_0x001c:
        r0 = r26;
        r3 = r0 instanceof android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout;	 Catch:{ all -> 0x0124 }
        if (r3 == 0) goto L_0x002e;
    L_0x0022:
        r0 = r24;
        r4 = r0.mRequestedViews;	 Catch:{ all -> 0x0124 }
        r0 = r26;
        r0 = (android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout) r0;	 Catch:{ all -> 0x0124 }
        r3 = r0;
        r4.removeView(r3);	 Catch:{ all -> 0x0124 }
    L_0x002e:
        if (r16 != 0) goto L_0x008a;
    L_0x0030:
        if (r15 != 0) goto L_0x008a;
    L_0x0032:
        r24.requestBindService();	 Catch:{ all -> 0x0124 }
    L_0x0035:
        if (r16 == 0) goto L_0x0131;
    L_0x0037:
        r10 = 0;
        r11 = 0;
        r17 = 0;
        r0 = r26;
        r3 = r0 instanceof android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout;	 Catch:{ all -> 0x0124 }
        if (r3 == 0) goto L_0x0177;
    L_0x0041:
        r0 = r26;
        r0 = (android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout) r0;	 Catch:{ all -> 0x0124 }
        r17 = r0;
        r3 = 0;
        r0 = r17;
        r10 = r0.getChildAt(r3);	 Catch:{ all -> 0x0124 }
        r0 = r24;
        r11 = r0.getConvertViewTypeId(r10);	 Catch:{ all -> 0x0124 }
        r18 = r17;
    L_0x0056:
        r9 = r27.getContext();	 Catch:{ all -> 0x0124 }
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x0124 }
        r0 = r25;
        r21 = r3.getRemoteViewsAt(r0);	 Catch:{ all -> 0x0124 }
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x0124 }
        r0 = r25;
        r14 = r3.getMetaDataAt(r0);	 Catch:{ all -> 0x0124 }
        r0 = r14.typeId;	 Catch:{ all -> 0x0124 }
        r22 = r0;
        if (r18 == 0) goto L_0x00c5;
    L_0x0074:
        r0 = r22;
        if (r11 != r0) goto L_0x0095;
    L_0x0078:
        r0 = r24;
        r3 = r0.mRemoteViewsOnClickHandler;	 Catch:{ Exception -> 0x00cd, all -> 0x0170 }
        r0 = r21;
        r0.reapply(r9, r10, r3);	 Catch:{ Exception -> 0x00cd, all -> 0x0170 }
        if (r13 == 0) goto L_0x0086;
    L_0x0083:
        r24.loadNextIndexInBackground();	 Catch:{ all -> 0x0124 }
    L_0x0086:
        monitor-exit(r23);	 Catch:{ all -> 0x0124 }
        r19 = r18;
    L_0x0089:
        return r19;
    L_0x008a:
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x0124 }
        r0 = r25;
        r13 = r3.queuePositionsToBePreloadedFromRequestedPosition(r0);	 Catch:{ all -> 0x0124 }
        goto L_0x0035;
    L_0x0095:
        r18.removeAllViews();	 Catch:{ Exception -> 0x00cd, all -> 0x0170 }
        r17 = r18;
    L_0x009a:
        r0 = r24;
        r3 = r0.mRemoteViewsOnClickHandler;	 Catch:{ Exception -> 0x0174 }
        r0 = r21;
        r1 = r27;
        r20 = r0.apply(r9, r1, r3);	 Catch:{ Exception -> 0x0174 }
        r3 = 16908358; // 0x1020046 float:2.3877425E-38 double:8.353839E-317;
        r4 = new java.lang.Integer;	 Catch:{ Exception -> 0x0174 }
        r0 = r22;
        r4.<init>(r0);	 Catch:{ Exception -> 0x0174 }
        r0 = r20;
        r0.setTagInternal(r3, r4);	 Catch:{ Exception -> 0x0174 }
        r0 = r17;
        r1 = r20;
        r0.addView(r1);	 Catch:{ Exception -> 0x0174 }
        if (r13 == 0) goto L_0x00c1;
    L_0x00be:
        r24.loadNextIndexInBackground();	 Catch:{ all -> 0x0124 }
    L_0x00c1:
        monitor-exit(r23);	 Catch:{ all -> 0x0124 }
        r19 = r17;
        goto L_0x0089;
    L_0x00c5:
        r17 = new android.widget.RemoteViewsAdapter$RemoteViewsFrameLayout;	 Catch:{ Exception -> 0x00cd, all -> 0x0170 }
        r0 = r17;
        r0.<init>(r9);	 Catch:{ Exception -> 0x00cd, all -> 0x0170 }
        goto L_0x009a;
    L_0x00cd:
        r12 = move-exception;
        r17 = r18;
    L_0x00d0:
        r3 = "RemoteViewsAdapter";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x012a }
        r4.<init>();	 Catch:{ all -> 0x012a }
        r5 = "Error inflating RemoteViews at position: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x012a }
        r0 = r25;
        r4 = r4.append(r0);	 Catch:{ all -> 0x012a }
        r5 = ", using";
        r4 = r4.append(r5);	 Catch:{ all -> 0x012a }
        r5 = "loading view instead";
        r4 = r4.append(r5);	 Catch:{ all -> 0x012a }
        r4 = r4.append(r12);	 Catch:{ all -> 0x012a }
        r4 = r4.toString();	 Catch:{ all -> 0x012a }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x012a }
        r19 = 0;
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x012a }
        r2 = r3.getMetaData();	 Catch:{ all -> 0x012a }
        monitor-enter(r2);	 Catch:{ all -> 0x012a }
        r0 = r24;
        r6 = r0.mCache;	 Catch:{ all -> 0x0127 }
        r0 = r24;
        r7 = r0.mLayoutInflater;	 Catch:{ all -> 0x0127 }
        r0 = r24;
        r8 = r0.mRemoteViewsOnClickHandler;	 Catch:{ all -> 0x0127 }
        r3 = r25;
        r4 = r26;
        r5 = r27;
        r19 = r2.createLoadingView(r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x0127 }
        monitor-exit(r2);	 Catch:{ all -> 0x0127 }
        if (r13 == 0) goto L_0x0121;
    L_0x011e:
        r24.loadNextIndexInBackground();	 Catch:{ all -> 0x0124 }
    L_0x0121:
        monitor-exit(r23);	 Catch:{ all -> 0x0124 }
        goto L_0x0089;
    L_0x0124:
        r3 = move-exception;
        monitor-exit(r23);	 Catch:{ all -> 0x0124 }
        throw r3;
    L_0x0127:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0127 }
        throw r3;	 Catch:{ all -> 0x012a }
    L_0x012a:
        r3 = move-exception;
    L_0x012b:
        if (r13 == 0) goto L_0x0130;
    L_0x012d:
        r24.loadNextIndexInBackground();	 Catch:{ all -> 0x0124 }
    L_0x0130:
        throw r3;	 Catch:{ all -> 0x0124 }
    L_0x0131:
        r19 = 0;
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x0124 }
        r2 = r3.getMetaData();	 Catch:{ all -> 0x0124 }
        monitor-enter(r2);	 Catch:{ all -> 0x0124 }
        r0 = r24;
        r6 = r0.mCache;	 Catch:{ all -> 0x016d }
        r0 = r24;
        r7 = r0.mLayoutInflater;	 Catch:{ all -> 0x016d }
        r0 = r24;
        r8 = r0.mRemoteViewsOnClickHandler;	 Catch:{ all -> 0x016d }
        r3 = r25;
        r4 = r26;
        r5 = r27;
        r19 = r2.createLoadingView(r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x016d }
        monitor-exit(r2);	 Catch:{ all -> 0x016d }
        r0 = r24;
        r3 = r0.mRequestedViews;	 Catch:{ all -> 0x0124 }
        r0 = r25;
        r1 = r19;
        r3.add(r0, r1);	 Catch:{ all -> 0x0124 }
        r0 = r24;
        r3 = r0.mCache;	 Catch:{ all -> 0x0124 }
        r0 = r25;
        r3.queueRequestedPositionToLoad(r0);	 Catch:{ all -> 0x0124 }
        r24.loadNextIndexInBackground();	 Catch:{ all -> 0x0124 }
        monitor-exit(r23);	 Catch:{ all -> 0x0124 }
        goto L_0x0089;
    L_0x016d:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x016d }
        throw r3;	 Catch:{ all -> 0x0124 }
    L_0x0170:
        r3 = move-exception;
        r17 = r18;
        goto L_0x012b;
    L_0x0174:
        r12 = move-exception;
        goto L_0x00d0;
    L_0x0177:
        r18 = r17;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.RemoteViewsAdapter.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }

    public int getViewTypeCount() {
        int i;
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            i = metaData.viewTypeCount;
        }
        return i;
    }

    public boolean hasStableIds() {
        boolean z;
        RemoteViewsMetaData metaData = this.mCache.getMetaData();
        synchronized (metaData) {
            z = metaData.hasStableIds;
        }
        return z;
    }

    public boolean isEmpty() {
        return getCount() <= 0;
    }

    private void onNotifyDataSetChanged() {
        try {
            int newCount;
            ArrayList<Integer> visibleWindow;
            this.mServiceConnection.getRemoteViewsFactory().onDataSetChanged();
            synchronized (this.mCache) {
                this.mCache.reset();
            }
            updateTemporaryMetaData();
            synchronized (this.mCache.getTemporaryMetaData()) {
                newCount = this.mCache.getTemporaryMetaData().count;
                visibleWindow = getVisibleWindow(this.mVisibleWindowLowerBound, this.mVisibleWindowUpperBound, newCount);
            }
            Iterator i$ = visibleWindow.iterator();
            while (i$.hasNext()) {
                int i = ((Integer) i$.next()).intValue();
                if (i < newCount) {
                    updateRemoteViews(i, false);
                }
            }
            this.mMainQueue.post(new Runnable() {
                public void run() {
                    synchronized (RemoteViewsAdapter.this.mCache) {
                        RemoteViewsAdapter.this.mCache.commitTemporaryMetaData();
                    }
                    RemoteViewsAdapter.this.superNotifyDataSetChanged();
                    RemoteViewsAdapter.this.enqueueDeferredUnbindServiceMessage();
                }
            });
            this.mNotifyDataSetChangedAfterOnServiceConnected = false;
        } catch (RemoteException e) {
            Log.e(TAG, "Error in updateNotifyDataSetChanged(): " + e.getMessage());
        } catch (RuntimeException e2) {
            Log.e(TAG, "Error in updateNotifyDataSetChanged(): " + e2.getMessage());
        }
    }

    private ArrayList<Integer> getVisibleWindow(int lower, int upper, int count) {
        ArrayList<Integer> window = new ArrayList();
        if (!(lower == 0 && upper == 0) && lower >= 0 && upper >= 0) {
            int i;
            if (lower <= upper) {
                for (i = lower; i <= upper; i++) {
                    window.add(Integer.valueOf(i));
                }
            } else {
                for (i = lower; i < count; i++) {
                    window.add(Integer.valueOf(i));
                }
                for (i = 0; i <= upper; i++) {
                    window.add(Integer.valueOf(i));
                }
            }
        }
        return window;
    }

    public void notifyDataSetChanged() {
        this.mMainQueue.removeMessages(1);
        if (this.mServiceConnection.isConnected()) {
            this.mWorkerQueue.post(new Runnable() {
                public void run() {
                    RemoteViewsAdapter.this.onNotifyDataSetChanged();
                }
            });
            return;
        }
        this.mNotifyDataSetChangedAfterOnServiceConnected = true;
        requestBindService();
    }

    void superNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void twSuperNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (this.mServiceConnection.isConnected()) {
                    this.mServiceConnection.unbind(this.mContext, this.mAppWidgetId, this.mIntent);
                }
                return true;
            default:
                return false;
        }
    }

    private void enqueueDeferredUnbindServiceMessage() {
        this.mMainQueue.removeMessages(1);
        this.mMainQueue.sendEmptyMessageDelayed(1, TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
    }

    private boolean requestBindService() {
        if (!this.mServiceConnection.isConnected()) {
            this.mServiceConnection.bind(this.mContext, this.mAppWidgetId, this.mIntent);
        }
        this.mMainQueue.removeMessages(1);
        return this.mServiceConnection.isConnected();
    }
}
