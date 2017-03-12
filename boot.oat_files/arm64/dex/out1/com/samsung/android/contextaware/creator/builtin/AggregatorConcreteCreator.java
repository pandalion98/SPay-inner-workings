package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.builtin.LifeLogAggregator;
import com.samsung.android.contextaware.aggregator.builtin.LocationAggregator;
import com.samsung.android.contextaware.aggregator.builtin.MovingAggregator;
import com.samsung.android.contextaware.aggregator.builtin.TemperatureHumidityAggregator;
import com.samsung.android.contextaware.aggregator.lpp.LppAggregator;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.IListObjectCreator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class AggregatorConcreteCreator extends ContextProviderCreator {
    private static CopyOnWriteArrayList<ContextProviderCreator> sRunnerCreator;

    private enum AggregatorList implements IListObjectCreator {
        LOCATION(ContextType.AGGREGATOR_LOCATION.getCode()) {
            public CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
                list.add(ContextType.ANDROID_RUNNER_RAW_GPS.getCode());
                list.add(ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode());
                list.add(ContextType.ANDROID_RUNNER_RAW_WPS.getCode());
                return list;
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LocationAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        MOVING(ContextType.AGGREGATOR_MOVING.getCode()) {
            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
                list.add(ContextType.AGGREGATOR_LOCATION.getCode());
                list.add(ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode());
                return list;
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovingAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        LPP(ContextType.AGGREGATOR_LPP.getCode()) {
            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
                list.add(ContextType.SENSORHUB_RUNNER_APDR.getCode());
                return list;
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LppAggregator(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        TEMPERATURE_HUMIDITY(ContextType.AGGREGATOR_TEMPERATURE_HUMIDITY.getCode()) {
            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
                list.add(ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.getCode());
                return list;
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TemperatureHumidityAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        LIFE_LOG(ContextType.AGGREGATOR_LIFE_LOG.getCode()) {
            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
                list.add(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode());
                list.add(ContextType.AGGREGATOR_LPP.getCode());
                return list;
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LifeLogAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        };
        
        private final String name;

        private AggregatorList(String name) {
            this.name = name;
        }

        private static ContextComponent getRunnerObj(String runnerName, Object... property) {
            ContextComponent obj = null;
            Iterator i = AggregatorConcreteCreator.getRunnerCreator().iterator();
            while (i.hasNext()) {
                ContextProviderCreator creator = (ContextProviderCreator) i.next();
                if (creator != null) {
                    obj = creator.create(runnerName, true, property);
                    if (obj != null) {
                        break;
                    }
                }
            }
            return obj;
        }

        protected final CopyOnWriteArrayList<ContextComponent> makeListForContextCreation() {
            CopyOnWriteArrayList<ContextComponent> list = new CopyOnWriteArrayList();
            CopyOnWriteArrayList<String> subCollectionList = getSubCollectionList();
            if (subCollectionList == null) {
                CaLogger.error("list is null.");
                return null;
            }
            Iterator i$ = subCollectionList.iterator();
            while (i$.hasNext()) {
                list.add(getRunnerObj((String) i$.next(), new Object[0]));
            }
            return list;
        }

        public CopyOnWriteArrayList<String> getSubCollectionList() {
            return new CopyOnWriteArrayList();
        }

        public final ContextComponent getObjectForSubCollection() {
            return getObject();
        }

        public final ContextComponent getObject(Object... property) {
            return getObject();
        }

        public final ContextComponent getObjectForSubCollection(Object... property) {
            return getObjectForSubCollection();
        }

        public void removeObject(String service) {
            ContextProviderCreator.removeObj(service);
        }
    }

    public AggregatorConcreteCreator(CopyOnWriteArrayList<ContextProviderCreator> runnerCreator, Context context, Looper looper, ISensorHubResetObservable observable, int version) {
        super(context, looper, observable, version);
        setRunnerCreator(new CopyOnWriteArrayList());
        setRunnerCreator(runnerCreator);
        sRunnerCreator.add(this);
    }

    public final IListObjectCreator getValueOfList(String name) {
        return AggregatorList.valueOf(name);
    }

    public final CopyOnWriteArrayList<String> getSubCollectionList(String name) {
        return AggregatorList.valueOf(name).getSubCollectionList();
    }

    private static void setRunnerCreator(CopyOnWriteArrayList<ContextProviderCreator> runnerCreator) {
        sRunnerCreator = runnerCreator;
    }

    private static CopyOnWriteArrayList<ContextProviderCreator> getRunnerCreator() {
        return sRunnerCreator;
    }
}
