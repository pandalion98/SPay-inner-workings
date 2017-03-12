package android.widget;

import android.app.Service;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.os.Debug;
import android.os.IBinder;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.widget.IRemoteViewsFactory.Stub;
import java.util.HashMap;

public abstract class RemoteViewsService extends Service {
    private static final int DEBUG_LEVEL_HIGH = 18760;
    private static final int DEBUG_LEVEL_LOW = 20300;
    private static final int DEBUG_LEVEL_MID = 18765;
    private static final String LOG_TAG = "RemoteViewsService";
    private static boolean mNoProductShip = (Debug.isProductShip() == 0);
    private static final Object sLock = new Object();
    private static final HashMap<FilterComparison, RemoteViewsFactory> sRemoteViewFactories = new HashMap();

    public interface RemoteViewsFactory {
        int getCount();

        long getItemId(int i);

        RemoteViews getLoadingView();

        RemoteViews getViewAt(int i);

        int getViewTypeCount();

        boolean hasStableIds();

        void onCreate();

        void onDataSetChanged();

        void onDestroy();
    }

    private static class RemoteViewsFactoryAdapter extends Stub {
        private RemoteViewsFactory mFactory;
        private boolean mIsCreated;

        public RemoteViewsFactoryAdapter(RemoteViewsFactory factory, boolean isCreated) {
            this.mFactory = factory;
            this.mIsCreated = isCreated;
        }

        public synchronized boolean isCreated() {
            return this.mIsCreated;
        }

        public synchronized void onDataSetChanged() {
            try {
                this.mFactory.onDataSetChanged();
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
        }

        public synchronized void onDataSetChangedAsync() {
            onDataSetChanged();
        }

        public synchronized int getCount() {
            int count;
            count = 0;
            try {
                count = this.mFactory.getCount();
                if (count == 0 && (RemoteViewsService.mNoProductShip || RemoteViewsService.getDebugLevel() > 0)) {
                    Log.d(RemoteViewsService.LOG_TAG, "getCount = 0");
                }
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            return count;
        }

        public synchronized RemoteViews getViewAt(int position) {
            RemoteViews rv;
            rv = null;
            try {
                rv = this.mFactory.getViewAt(position);
                if (rv != null) {
                    rv.setIsWidgetCollectionChild(true);
                }
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            return rv;
        }

        public synchronized RemoteViews getLoadingView() {
            RemoteViews rv;
            rv = null;
            try {
                rv = this.mFactory.getLoadingView();
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            return rv;
        }

        public synchronized int getViewTypeCount() {
            int count;
            count = 0;
            try {
                count = this.mFactory.getViewTypeCount();
                if (count == 0 && (RemoteViewsService.mNoProductShip || RemoteViewsService.getDebugLevel() > 0)) {
                    Log.d(RemoteViewsService.LOG_TAG, "getViewTypeCount = 0");
                }
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            return count;
        }

        public synchronized long getItemId(int position) {
            long id;
            id = 0;
            try {
                id = this.mFactory.getItemId(position);
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            return id;
        }

        public synchronized boolean hasStableIds() {
            boolean hasStableIds;
            hasStableIds = false;
            try {
                hasStableIds = this.mFactory.hasStableIds();
            } catch (Exception ex) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            return hasStableIds;
        }

        public void onDestroy(Intent intent) {
            synchronized (RemoteViewsService.sLock) {
                FilterComparison fc = new FilterComparison(intent);
                if (RemoteViewsService.sRemoteViewFactories.containsKey(fc)) {
                    try {
                        ((RemoteViewsFactory) RemoteViewsService.sRemoteViewFactories.get(fc)).onDestroy();
                    } catch (Exception ex) {
                        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
                    }
                    RemoteViewsService.sRemoteViewFactories.remove(fc);
                }
            }
        }
    }

    public abstract RemoteViewsFactory onGetViewFactory(Intent intent);

    public IBinder onBind(Intent intent) {
        IBinder remoteViewsFactoryAdapter;
        synchronized (sLock) {
            RemoteViewsFactory factory;
            boolean isCreated;
            FilterComparison fc = new FilterComparison(intent);
            if (sRemoteViewFactories.containsKey(fc)) {
                factory = (RemoteViewsFactory) sRemoteViewFactories.get(fc);
                isCreated = true;
            } else {
                factory = onGetViewFactory(intent);
                sRemoteViewFactories.put(fc, factory);
                factory.onCreate();
                isCreated = false;
            }
            remoteViewsFactoryAdapter = new RemoteViewsFactoryAdapter(factory, isCreated);
        }
        return remoteViewsFactoryAdapter;
    }

    private static int getDebugLevel() {
        String state = SystemProperties.get("ro.debug_level", "Unknown");
        if (state.equals("Unknown")) {
            return 0;
        }
        try {
            int debugLevel = Integer.parseInt(state.substring(2), 16);
            if (debugLevel == DEBUG_LEVEL_LOW) {
                return 0;
            }
            if (debugLevel == DEBUG_LEVEL_MID) {
                return 1;
            }
            if (debugLevel == DEBUG_LEVEL_HIGH) {
                return 2;
            }
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
