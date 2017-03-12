package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.IListObjectCreator;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.AccelerometerSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.BestLocationRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.GpsRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.GyroscopeSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.MagneticSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.OrientationSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.SatelliteRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.WpsRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;

public class AndroidRunnerConcreteCreator extends ContextProviderCreator {

    private enum AndroidRunnerList implements IListObjectCreator {
        RAW_GPS(ContextType.ANDROID_RUNNER_RAW_GPS.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new GpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        RAW_WPS(ContextType.ANDROID_RUNNER_RAW_WPS.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new WpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        RAW_SATELLITE(ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SatelliteRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new SatelliteRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        BEST_LOCATION(ContextType.ANDROID_RUNNER_BEST_LOCATION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BestLocationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new BestLocationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        ACCELEROMETER_SENSOR(ContextType.ANDROID_RUNNER_ACCELEROMETER_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObject(Object... property) {
                if (property.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) property[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection(Object... property) {
                return new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) property[0]).intValue());
            }
        },
        ORIENTATION_SENSOR(ContextType.ANDROID_RUNNER_ORIENTATION_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObject(Object... property) {
                if (property.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) property[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection(Object... property) {
                return new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) property[0]).intValue());
            }
        },
        MAGNETIC_SENSOR(ContextType.ANDROID_RUNNER_MAGNETIC_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObject(Object... property) {
                if (property.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) property[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection(Object... property) {
                return new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) property[0]).intValue());
            }
        },
        GYROSCOPE_SENSOR(ContextType.ANDROID_RUNNER_GYROSCOPE_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObject(Object... property) {
                if (property.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) property[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection(Object... property) {
                return new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) property[0]).intValue());
            }
        };
        
        private final String name;

        private AndroidRunnerList(String name) {
            this.name = name;
        }

        public ContextComponent getObjectForSubCollection() {
            return getObject();
        }

        public ContextComponent getObject(Object... property) {
            return getObject();
        }

        public ContextComponent getObjectForSubCollection(Object... property) {
            return getObjectForSubCollection();
        }

        public void removeObject(String service) {
            ContextProviderCreator.removeObj(service);
        }
    }

    public AndroidRunnerConcreteCreator(Context context, Looper looper, ISensorHubResetObservable observable, int version) {
        super(context, looper, observable, version);
    }

    public final IListObjectCreator getValueOfList(String name) {
        return AndroidRunnerList.valueOf(name);
    }
}
