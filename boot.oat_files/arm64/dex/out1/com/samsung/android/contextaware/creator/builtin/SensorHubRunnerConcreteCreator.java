package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.MultiModeContextList;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.IListObjectCreator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubMultiModeParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserBean;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.ACTIVITY_TRACKER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.MODE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.PEDOMETER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.POSITIONING_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SLEEP_MONITOR_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SUB_DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.TypeParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AbnormalPressureMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AbnormalShockRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActiveTimeRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerBatchCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerBatchRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerExtandedInterruptRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerInterruptRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AnyMotionDetectorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AutoBrightnessRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AutoRotationRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.BottomFlatDetectorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.BounceLongMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.BounceShortMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CallMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CallPoseRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CaptureMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CareGiverRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CarryingStatusMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CurrentStatusForMovementPositioningRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.DevicePhysicalContextMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.DirectCallRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.DualDisplayAngleRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.EADRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ExerciseRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FlatMotionForTableModeRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FlatMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FlipCoverActionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.GestureApproachRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.GyroTemperatureRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.HallSensorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.LifeLogComponentRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MainScreenDetectionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MovementAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MovementForPositioningRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MovementRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PedometerCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PedometerOtherVerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PedometerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PowerNotiRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PutDownMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SLMonitorExtendedInterruptRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SLMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SensorStatusCheckRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ShakeMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SleepMonitorCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SleepMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SpecificPoseAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.StayingAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.StepCountAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.StopAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.TemperatureAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.TestFlatMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.WakeUpVoiceRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.WirelessChargingMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.WristUpMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.rpc.SLocationRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorHandler;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin.BarometerSensorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin.TemperatureHumiditySensorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.internal.PhoneStateMonitorRunner;
import com.samsung.android.contextaware.manager.BatchContextProvider;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ExtandedInterruptContextProvider;
import com.samsung.android.contextaware.manager.InterruptContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class SensorHubRunnerConcreteCreator extends ContextProviderCreator {

    private enum SensorHubRunnerList implements IListObjectCreator {
        APDR(ContextType.SENSORHUB_RUNNER_APDR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ApdrRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_APDR.toString();
            }
        },
        PEDOMETER(ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextComponent runner;
                    if (ContextProviderCreator.getVersion() == 2) {
                        runner = new PedometerOtherVerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable());
                    } else {
                        runner = new PedometerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable());
                    }
                    ContextProviderCreator.getContextProviderMap().put(name(), runner);
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_PEDOMETER.toString();
            }
        },
        GESTURE_APPROACH(ContextType.SENSORHUB_RUNNER_GESTURE_APPROACH.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GestureApproachRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_GESTURE_APPROACH.toString();
            }
        },
        STEP_COUNT_ALERT(ContextType.SENSORHUB_RUNNER_STEP_COUNT_ALERT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new StepCountAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_COUNT_ALERT.toString();
            }
        },
        MOTION(ContextType.SENSORHUB_RUNNER_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOTION.toString();
            }
        },
        MOVEMENT(ContextType.SENSORHUB_RUNNER_MOVEMENT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovementRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT.toString();
            }
        },
        AUTO_ROTATION(ContextType.SENSORHUB_RUNNER_AUTO_ROTATION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AutoRotationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_AUTO_ROTATION.toString();
            }
        },
        POWER_NOTI(ContextType.SENSORHUB_RUNNER_POWER_NOTI.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PowerNotiRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return null;
            }
        },
        MOVEMENT_FOR_POSITIONING(ContextType.SENSORHUB_RUNNER_MOVEMENT_FOR_POSITIONING.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovementForPositioningRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT_FOR_POSITIONING.toString();
            }
        },
        CURRENT_STATUS_FOR_MOVEMENT_POSITIONING(ContextType.REQUEST_SENSORHUB_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CurrentStatusForMovementPositioningRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return POSITIONING_EXT_LIB_TYPE.POSITIONING_CURRENT_STATUS.toString();
            }
        },
        DIRECT_CALL(ContextType.SENSORHUB_RUNNER_DIRECT_CALL.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new DirectCallRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_DIRECT_CALL.toString();
            }
        },
        STOP_ALERT(ContextType.SENSORHUB_RUNNER_STOP_ALERT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new StopAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STOP_ALERT.toString();
            }
        },
        RAW_TEMPERATURE_HUMIDITY_SENSOR(ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TemperatureHumiditySensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.toString();
            }

            protected String getSubKey() {
                return SUB_DATA_TYPE.ENVIRONMENT_SENSORTYPE_TEMPERATURE_HUMIDITY.toString();
            }
        },
        RAW_BAROMETER_SENSOR(ContextType.SENSORHUB_RUNNER_RAW_BAROMETER_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BarometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.toString();
            }

            protected String getSubKey() {
                return SUB_DATA_TYPE.ENVIRONMENT_SENSORTYPE_BAROMETER.toString();
            }
        },
        CALL_POSE(ContextType.SENSORHUB_RUNNER_CALL_POSE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CallPoseRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CALL_POSE.toString();
            }
        },
        SHAKE_MOTION(ContextType.SENSORHUB_RUNNER_SHAKE_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ShakeMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SHAKE_MOTION.toString();
            }
        },
        CARE_GIVER(ContextType.SENSORHUB_RUNNER_CARE_GIVER.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CareGiverRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CARE_GIVER.toString();
            }
        },
        ABNORMAL_SHOCK(ContextType.SENSORHUB_RUNNER_ABNORMAL_SHOCK.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AbnormalShockRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ABNORMAL_SHOCK.toString();
            }
        },
        FLIP_COVER_ACTION(ContextType.SENSORHUB_RUNNER_FLIP_COVER_ACTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FlipCoverActionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FLIP_COVER_ACTION.toString();
            }
        },
        GYRO_TEMPERATURE(ContextType.SENSORHUB_RUNNER_GYRO_TEMPERATURE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GyroTemperatureRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_GYRO_TEMPERATURE.toString();
            }
        },
        PUT_DOWN_MOTION(ContextType.SENSORHUB_RUNNER_PUT_DOWN_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PutDownMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_PUT_DOWN_MOTION.toString();
            }
        },
        WAKE_UP_VOICE(ContextType.SENSORHUB_RUNNER_WAKE_UP_VOICE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WakeUpVoiceRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_WAKE_UP_VOICE.toString();
            }
        },
        BOUNCE_SHORT_MOTION(ContextType.SENSORHUB_RUNNER_BOUNCE_SHORT_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BounceShortMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_BOUNCE_SHORT_MOTION.toString();
            }
        },
        BOUNCE_LONG_MOTION(ContextType.SENSORHUB_RUNNER_BOUNCE_LONG_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BounceLongMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_BOUNCE_LONG_MOTION.toString();
            }
        },
        WRIST_UP_MOTION(ContextType.SENSORHUB_RUNNER_WRIST_UP_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WristUpMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_WRIST_UP_MOTION.toString();
            }
        },
        FLAT_MOTION(ContextType.SENSORHUB_RUNNER_FLAT_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FlatMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FLAT_MOTION.toString();
            }
        },
        MOVEMENT_ALERT(ContextType.SENSORHUB_RUNNER_MOVEMENT_ALERT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovementAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT_ALERT.toString();
            }
        },
        TEST_FLAT_MOTION(ContextType.SENSORHUB_RUNNER_TEST_FLAT_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TestFlatMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_TEST_FLAT_MOTION.toString();
            }
        },
        TEMPERATURE_ALERT(ContextType.SENSORHUB_RUNNER_TEMPERATURE_ALERT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TemperatureAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_TEMPERATURE_ALERT.toString();
            }
        },
        PEDOMETER_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_PEDOMETER_CURRENT_INFO.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PedometerCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return PEDOMETER_EXT_LIB_TYPE.PEDOMETER_CURRENT_INFO.toString();
            }
        },
        STAYING_ALERT(ContextType.SENSORHUB_RUNNER_STAYING_ALERT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new StayingAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STAYING_ALERT.toString();
            }
        },
        LIFE_LOG_COMPONENT(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LifeLogComponentRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                setOptionForRequestLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_LIFE_LOG_COMPONENT.toString();
            }
        },
        ACTIVITY_TRACKER(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityTrackerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            protected String getSubKey() {
                return MODE.NORMAL_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_INTERRUPT(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new InterruptContextProvider(new ActivityTrackerInterruptRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            protected String getSubKey() {
                return MODE.INTERRUPT_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_BATCH(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BatchContextProvider(new ActivityTrackerBatchRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            protected String getSubKey() {
                return MODE.BATCH_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_EXTANDED_INTERRUPT(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_EXTANDED_INTERRUPT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ExtandedInterruptContextProvider(new ActivityTrackerExtandedInterruptRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            protected String getSubKey() {
                return MODE.EXTANDED_INTERRUPT_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_ACTIVITY_TRACKER_CURRENT_INFO.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityTrackerCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return ACTIVITY_TRACKER_EXT_LIB_TYPE.ACTIVITY_TRACKER_CURRENT_INFO.toString();
            }
        },
        ACTIVITY_TRACKER_BATCH_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_ACTIVITY_TRACKER_BATCH_CURRENT_INFO.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityTrackerBatchCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return ACTIVITY_TRACKER_EXT_LIB_TYPE.ACTIVITY_TRACKER_BATCH_CURRENT_INFO.toString();
            }
        },
        SPECIFIC_POSE_ALERT(ContextType.SENSORHUB_RUNNER_SPECIFIC_POSE_ALERT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SpecificPoseAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SPECIFIC_POSE_ALERT.toString();
            }
        },
        SLEEP_MONITOR(ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SleepMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SLEEP_MONITOR.toString();
            }
        },
        SLEEP_MONITOR_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_SLEEP_MONITOR_CURRENT_INFO.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SleepMonitorCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return SLEEP_MONITOR_EXT_LIB_TYPE.SLEEP_MONITOR_CURRENT_INFO.toString();
            }
        },
        CAPTURE_MOTION(ContextType.SENSORHUB_RUNNER_CAPTURE_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CaptureMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CAPTURE_MOTION.toString();
            }
        },
        CALL_MOTION(ContextType.SENSORHUB_RUNNER_CALL_MOTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CallMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CALL_MOTION.toString();
            }
        },
        STEP_LEVEL_MONITOR(ContextType.SENSORHUB_RUNNER_SL_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SLMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString();
            }

            protected String getSubKey() {
                return MODE.NORMAL_MODE.toString();
            }
        },
        STEP_LEVEL_MONITOR_EXTENDED_INTERRUPT(ContextType.SENSORHUB_RUNNER_SL_MONITOR_EXTENDED_INTERRUPT.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ExtandedInterruptContextProvider(new SLMonitorExtendedInterruptRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString();
            }

            protected String getSubKey() {
                return MODE.EXTANDED_INTERRUPT_MODE.toString();
            }
        },
        ACTIVE_TIME(ContextType.SENSORHUB_RUNNER_ACTIVE_TIME.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActiveTimeRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString();
            }

            protected String getSubKey() {
                return MODE.BATCH_MODE.toString();
            }
        },
        FLAT_MOTION_FOR_TABLE_MODE(ContextType.SENSORHUB_RUNNER_FLAT_MOTION_FOR_TABLE_MODE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FlatMotionForTableModeRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FLAT_MOTION_FOR_TABLE_MODE.toString();
            }
        },
        CARRYING_STATUS_MONITOR(ContextType.SENSORHUB_RUNNER_CARRYING_STATUS_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CarryingStatusMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CARRYING_STATUS_MONITOR.toString();
            }
        },
        BOTTOM_FLAT_DETECTOR(ContextType.SENSORHUB_RUNNER_BOTTOM_FLAT_DETECTOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BottomFlatDetectorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_BOTTOM_FLAT_DETECTOR.toString();
            }
        },
        PHONE_STATE_MONITOR(ContextType.SENSORHUB_RUNNER_PHONE_STATE_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PhoneStateMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_PHONE_STATE_MONITOR.toString();
            }
        },
        EXERCISE(ContextType.SENSORHUB_RUNNER_EXERCISE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ExerciseRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_EXERCISE.toString();
            }
        },
        AUTO_BRIGHTNESS(ContextType.SENSORHUB_RUNNER_AUTO_BRIGHTNESS.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AutoBrightnessRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_AUTO_BRIGHTNESS.toString();
            }
        },
        ABNORMAL_PRESSURE_MONITOR(ContextType.SENSORHUB_RUNNER_ABNORMAL_PRESSURE_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AbnormalPressureMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ABNORMAL_PRESSURE_MONITOR.toString();
            }
        },
        HALL_SENSOR(ContextType.SENSORHUB_RUNNER_HALL_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new HallSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_HALL_SENSOR.toString();
            }
        },
        EAD(ContextType.SENSORHUB_RUNNER_EAD.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new EADRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_EAD.toString();
            }
        },
        DUAL_DISPLAY_ANGLE(ContextType.SENSORHUB_RUNNER_DUAL_DISPLAY_ANGLE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new DualDisplayAngleRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_DUAL_DISPLAY_ANGLE.toString();
            }
        },
        WIRELESS_CHARGING_MONITOR(ContextType.SENSORHUB_RUNNER_WIRELESS_CHARGING_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WirelessChargingMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_WIRELESS_CHARGING_MONITOR.toString();
            }
        },
        SLOCATION_RUNNER(ContextType.SENSORHUB_RUNNER_SLOCATION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SLocationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SLOCATION.toString();
            }
        },
        DEVICE_PHYSICAL_CONTEXT_MONITOR(ContextType.SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new DevicePhysicalContextMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR.toString();
            }

            protected String getSubKey() {
                return MODE.NORMAL_MODE.toString();
            }
        },
        MAIN_SCREEN_DETECTION_RUNNER(ContextType.SENSORHUB_RUNNER_MAIN_SCREEN_DETECTION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MainScreenDetectionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MAIN_SCREEN_DETECTION.toString();
            }
        },
        ANY_MOTION_DETECTOR_RUNNER(ContextType.SENSORHUB_RUNNER_ANY_MOTION_DETECTOR_RUNNER.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AnyMotionDetectorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ANY_MOTION_DETECTOR.toString();
            }
        },
        SENSOR_STATUS_CHECK_RUNNER(ContextType.SENSORHUB_RUNNER_SENSOR_STATUS_CHECK.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SensorStatusCheckRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SENSOR_STATUS_CHECK.toString();
            }
        };
        
        private final String name;

        protected abstract String getKey();

        private SensorHubRunnerList(String name) {
            this.name = name;
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

        protected final void setOptionForLib(String key, String name) {
            registerLibParser(key, name);
        }

        protected final void setOptionForRequestLib(String key, String name) {
            registerRequestLibParser(key, name);
        }

        protected final void setOptionForLib(String key, String subKey, String name) {
            registerLibParser(key, name);
            if (SensorHubMultiModeParser.getInstance().containsParser(key)) {
                SensorHubParserBean bean = (SensorHubParserBean) SensorHubMultiModeParser.getInstance().getParser(key);
                if (bean == null) {
                    CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
                } else {
                    bean.registerParser(subKey, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(name)).getContextProvider());
                }
            } else if (key.equals(DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.toString())) {
                EnvironmentSensorHandler.getInstance().registerParser(subKey, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(name)).getContextProvider());
            }
        }

        protected final void setOptionForExtLib(String key, String name) {
            registerExtLibParser(key, name);
        }

        public void removeObject(String service) {
            if (service == null || service.isEmpty()) {
                CaLogger.error("Service is null");
            } else if (!ContextProviderCreator.removeObj(service)) {
            } else {
                if (getKey() == null || getKey().isEmpty()) {
                    CaLogger.error("Key is null");
                } else {
                    unregisterLibParser(getKey(), getSubKey());
                }
            }
        }

        protected String getSubKey() {
            return null;
        }

        private String getParserMapKey() {
            return getSubKey() != null ? getSubKey() : getKey();
        }

        private void registerLibParser(String key, String name) {
            TypeParser parser = SensorHubParserProvider.getInstance().getLibParser();
            if (parser != null) {
                parser.registerParser(key, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(name)).getContextProvider());
            }
        }

        private void registerExtLibParser(String key, String name) {
            TypeParser parser = SensorHubParserProvider.getInstance().getExtLibParser();
            if (parser != null) {
                parser.registerParser(key, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(name)).getContextProvider());
            }
        }

        private void registerRequestLibParser(String key, String name) {
            TypeParser parser = SensorHubParserProvider.getInstance().getRequestLibParser();
            if (parser != null) {
                parser.registerParser(key, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(name)).getContextProvider());
            }
        }

        private void unregisterLibParser(String key, String subKey) {
            if (key == null || key.isEmpty()) {
                CaLogger.error("Key is null");
                return;
            }
            SensorHubParserBean bean;
            if (SensorHubMultiModeParser.getInstance().containsParser(key)) {
                bean = (SensorHubParserBean) SensorHubMultiModeParser.getInstance().getParser(key);
                if (!(bean == null || subKey == null || subKey.isEmpty())) {
                    bean.unregisterParser(subKey);
                }
            }
            TypeParser parser = SensorHubParserProvider.getInstance().getLibParser();
            if (parser != null) {
                if (MultiModeContextList.getInstance().isMultiModeType(key)) {
                    bean = (SensorHubParserBean) SensorHubMultiModeParser.getInstance().getParser(key);
                    if (bean == null || bean.checkParserMap()) {
                        return;
                    }
                }
                parser.unregisterParser(key);
                if (subKey != null && !subKey.isEmpty()) {
                    parser.unregisterParser(subKey);
                }
            }
        }
    }

    public SensorHubRunnerConcreteCreator(Context context, Looper looper, ISensorHubResetObservable observable, int version) {
        super(context, looper, observable, version);
    }

    public final IListObjectCreator getValueOfList(String name) {
        return SensorHubRunnerList.valueOf(name);
    }
}
