package com.samsung.android.contextaware.manager;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.builtin.AggregatorConcreteCreator;
import com.samsung.android.contextaware.creator.builtin.AndroidRunnerConcreteCreator;
import com.samsung.android.contextaware.creator.builtin.SensorHubParserConcreteCreator;
import com.samsung.android.contextaware.creator.builtin.SensorHubRunnerConcreteCreator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubMultiModeParser;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.fault.FaultDetectionManager;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import com.samsung.android.contextaware.utilbundle.CaAlarmManager;
import com.samsung.android.contextaware.utilbundle.CaBootStatus;
import com.samsung.android.contextaware.utilbundle.CaCoverManager;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.CaTelephonyManager;
import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.IUtilManager;
import com.samsung.android.contextaware.utilbundle.SensorHubCommManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextManager {
    private final CopyOnWriteArrayList<ContextProviderCreator> mCreator = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<IUtilManager> mUtilManager;

    protected ContextManager(Context context, Looper looper, int version) {
        SensorHubParserConcreteCreator sensorHubParserConcreteCreator = new SensorHubParserConcreteCreator(context);
        this.mCreator.add(new AndroidRunnerConcreteCreator(context, looper, sensorHubParserConcreteCreator.getPowerObservable(), version));
        this.mCreator.add(new SensorHubRunnerConcreteCreator(context, looper, sensorHubParserConcreteCreator.getPowerObservable(), version));
        CopyOnWriteArrayList<ContextProviderCreator> creator = new CopyOnWriteArrayList();
        creator.add(this.mCreator.get(0));
        creator.add(this.mCreator.get(1));
        this.mCreator.add(new AggregatorConcreteCreator(creator, context, looper, sensorHubParserConcreteCreator.getPowerObservable(), version));
        this.mUtilManager = new CopyOnWriteArrayList();
        this.mUtilManager.add(CaPowerManager.getInstance());
        this.mUtilManager.add(CaTelephonyManager.getInstance());
        this.mUtilManager.add(CaAlarmManager.getInstance());
        this.mUtilManager.add(SensorHubCommManager.getInstance());
        this.mUtilManager.add(SensorHubMultiModeParser.getInstance());
        this.mUtilManager.add(CaBootStatus.getInstance());
        this.mUtilManager.add(CaTimeChangeManager.getInstance());
        this.mUtilManager.add(CaCoverManager.getInstance(looper));
        initializeUtil(context);
    }

    public final void start(Listener listener, String service, IContextObserver observer, int operation) {
        ContextComponent provider = getContextProviderObj(service);
        if (provider != null) {
            provider.getContextProvider().registerObserver(observer);
            provider.getContextProvider().registerCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
            provider.start(listener, operation);
        }
    }

    public final void stop(Listener listener, String service, IContextObserver observer, ICmdProcessResultObserver resultObserver, int operation) {
        ContextComponent provider = getContextProviderObj(service);
        if (provider != null) {
            provider.stop(listener, operation);
            int key = ContextList.getInstance().getServiceOrdinal(service);
            if (!listener.getServices().containsKey(Integer.valueOf(key)) || ((Integer) listener.getServices().get(Integer.valueOf(key))).intValue() <= 0) {
                if (resultObserver != null) {
                    provider.getContextProvider().unregisterCmdProcessResultObserver(resultObserver);
                } else {
                    provider.getContextProvider().unregisterCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
                }
                removeContextProviderObj(service);
            }
        }
    }

    protected final void getContextInfo(Listener listener, String service, IContextObserver observer) {
        ContextComponent provider = getContextProviderObj(service);
        if (provider != null) {
            provider.getContextProvider().registerObserver(observer);
            provider.getContextProvider().registerCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
            provider.getContextProvider().getContextInfo(listener);
        }
    }

    protected final void unregisterObservers(String service, IContextObserver observer) {
        ContextComponent provider = getContextProviderObj(service);
        if (provider != null) {
            provider.getContextProvider().unregisterCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
        }
    }

    protected final <E> boolean setProperty(String service, int property, E value) {
        ContextComponent provider = getContextProviderObj(service);
        if (provider != null) {
            return provider.getContextProvider().setProperty(property, value);
        }
        return false;
    }

    protected final void reset(String service) {
        ContextComponent component = getContextProviderObj(service);
        if (component == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_NOT_RUNNING.getCode()));
            return;
        }
        ContextProvider provider = component.getContextProvider();
        if (provider == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_NOT_RUNNING.getCode()));
        } else if (provider.isRunning()) {
            provider.getContextProvider().clearAccordingToRequest();
        } else {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_NOT_RUNNING.getCode()));
        }
    }

    public final void notifyInitContext(String service) {
        ContextComponent provider = getContextProviderObj(service);
        if (provider != null) {
            provider.getContextProvider().notifyInitContext();
        }
    }

    protected final ContextComponent getContextProviderObj(String service) {
        Iterator<ContextProviderCreator> i = this.mCreator.iterator();
        while (i.hasNext()) {
            ContextProviderCreator creator = (ContextProviderCreator) i.next();
            if (creator != null && creator.existContextProvider(service)) {
                return creator.create(service);
            }
        }
        return null;
    }

    protected void removeContextProviderObj(String service) {
        Iterator<ContextProviderCreator> i = this.mCreator.iterator();
        while (i.hasNext()) {
            ContextProviderCreator creator = (ContextProviderCreator) i.next();
            if (creator != null && creator.existContextProvider(service)) {
                creator.removeContextObj(service);
                return;
            }
        }
    }

    private void initializeUtil(Context context) {
        Iterator i$ = this.mUtilManager.iterator();
        while (i$.hasNext()) {
            ((IUtilManager) i$.next()).initializeManager(context);
        }
    }

    protected final CopyOnWriteArrayList<ContextProviderCreator> getCreator() {
        return this.mCreator;
    }

    protected final void setVersion(int version) {
        Iterator<ContextProviderCreator> i = this.mCreator.iterator();
        while (i.hasNext()) {
            ContextProviderCreator contextProviderCreator = (ContextProviderCreator) i.next();
            ContextProviderCreator.setVersion(version);
        }
    }
}
