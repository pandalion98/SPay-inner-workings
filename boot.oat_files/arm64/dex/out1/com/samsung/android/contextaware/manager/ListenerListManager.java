package com.samsung.android.contextaware.manager;

import android.os.IBinder;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.builtin.AggregatorConcreteCreator;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareService.Watcher;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListenerListManager {
    private static volatile ListenerListManager instance;
    private CopyOnWriteArrayList<ContextProviderCreator> mCreator;
    private final CopyOnWriteArrayList<Listener> mListenerList = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Watcher> mWatcherList = new CopyOnWriteArrayList();

    public static ListenerListManager getInstance() {
        if (instance == null) {
            synchronized (ListenerListManager.class) {
                if (instance == null) {
                    instance = new ListenerListManager();
                }
            }
        }
        return instance;
    }

    protected final CopyOnWriteArrayList<Listener> getListenerList() {
        return this.mListenerList;
    }

    protected final CopyOnWriteArrayList<Watcher> getWatcherList() {
        return this.mWatcherList;
    }

    protected final void addListener(Listener listener) {
        if (listener != null && !this.mListenerList.contains(listener)) {
            this.mListenerList.add(listener);
        }
    }

    protected final void addWatcher(Watcher watcher) {
        if (watcher != null && !this.mWatcherList.contains(watcher)) {
            this.mWatcherList.add(watcher);
        }
    }

    public final void removeListener(Listener listener) {
        if (listener != null && this.mListenerList.contains(listener)) {
            this.mListenerList.remove(listener);
        }
    }

    public final void removeWatcher(Watcher watcher) {
        if (watcher != null && this.mWatcherList.contains(watcher)) {
            this.mWatcherList.remove(watcher);
        }
    }

    protected final Listener getListener(IBinder binder) {
        Iterator<Listener> i = this.mListenerList.iterator();
        while (i.hasNext()) {
            Listener next = (Listener) i.next();
            if (binder.equals(next.getToken())) {
                return next;
            }
        }
        return null;
    }

    public final int getUsedTotalCount(String service) {
        return getUsedServiceCount(service) + getUsedSubCollectionCount(service);
    }

    public final int getUsedServiceCount(String service) {
        int count = 0;
        Iterator<Listener> i = this.mListenerList.iterator();
        while (i.hasNext()) {
            Listener next = (Listener) i.next();
            int key = ContextList.getInstance().getServiceOrdinal(service);
            if (next.getServices().containsKey(Integer.valueOf(key))) {
                count += ((Integer) next.getServices().get(Integer.valueOf(key))).intValue();
            }
        }
        return count;
    }

    public final int getUsedSubCollectionCount(String service) {
        int count = 0;
        Iterator<Listener> i = this.mListenerList.iterator();
        while (i.hasNext()) {
            Iterator<Integer> iter = ((Listener) i.next()).getServices().keySet().iterator();
            Iterator<?> j = iter;
            while (iter.hasNext()) {
                String code = ContextList.getInstance().getServiceCode(((Integer) j.next()).intValue());
                if (!(code == null || code.isEmpty())) {
                    count += getUsedSubCollectionCount(code, service);
                }
            }
        }
        return count;
    }

    private int getUsedSubCollectionCount(String aggregator, String service) {
        int count = 0;
        Iterator<ContextProviderCreator> i = this.mCreator.iterator();
        while (i.hasNext()) {
            ContextProviderCreator creator = (ContextProviderCreator) i.next();
            if (creator != null && creator.existContextProvider(aggregator) && (creator instanceof AggregatorConcreteCreator)) {
                CopyOnWriteArrayList<String> list = ((AggregatorConcreteCreator) creator).getSubCollectionList(aggregator);
                if (!(list == null || list.isEmpty())) {
                    Iterator<String> j = list.iterator();
                    while (j.hasNext()) {
                        String subCollector = (String) j.next();
                        if (!(subCollector == null || subCollector.isEmpty())) {
                            if (isAggregator(subCollector)) {
                                count += getUsedSubCollectionCount(subCollector, service);
                            }
                            if (subCollector.equals(service)) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private boolean isAggregator(String collector) {
        Iterator<ContextProviderCreator> i = this.mCreator.iterator();
        while (i.hasNext()) {
            ContextProviderCreator creator = (ContextProviderCreator) i.next();
            if (creator != null && creator.existContextProvider(collector) && (creator instanceof AggregatorConcreteCreator)) {
                return true;
            }
        }
        return false;
    }

    protected final void setCreator(CopyOnWriteArrayList<ContextProviderCreator> creator) {
        this.mCreator = creator;
    }
}
