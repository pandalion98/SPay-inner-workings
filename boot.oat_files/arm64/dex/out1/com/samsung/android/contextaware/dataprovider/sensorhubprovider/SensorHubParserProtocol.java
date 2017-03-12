package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

public class SensorHubParserProtocol {

    public enum ACTIVITY_TRACKER_EXT_LIB_TYPE {
        ACTIVITY_TRACKER_CURRENT_INFO((byte) 1),
        ACTIVITY_TRACKER_BATCH_CURRENT_INFO((byte) 2);
        
        protected byte value;

        private ACTIVITY_TRACKER_EXT_LIB_TYPE(byte value) {
            this.value = value;
        }
    }

    public enum DATA_TYPE {
        LIBRARY_DATATYPE_CALL_POSE((byte) 2),
        LIBRARY_DATATYPE_PEDOMETER((byte) 3),
        LIBRARY_DATATYPE_MOTION((byte) 4),
        LIBRARY_DATATYPE_GESTURE_APPROACH((byte) 5),
        LIBRARY_DATATYPE_STEP_COUNT_ALERT((byte) 6),
        LIBRARY_DATATYPE_AUTO_ROTATION((byte) 7),
        LIBRARY_DATATYPE_MOVEMENT((byte) 8),
        LIBRARY_DATATYPE_MOVEMENT_FOR_POSITIONING((byte) 9),
        LIBRARY_DATATYPE_DIRECT_CALL((byte) 10),
        LIBRARY_DATATYPE_STOP_ALERT(ISensorHubCmdProtocol.TYPE_STOP_ALERT_SERVICE),
        LIBRARY_DATATYPE_ENVIRONMENT_SENSOR(ISensorHubCmdProtocol.TYPE_ENVIRONMENT_SENSOR_SERVICE),
        LIBRARY_DATATYPE_SHAKE_MOTION((byte) 13),
        LIBRARY_DATATYPE_FLIP_COVER_ACTION((byte) 14),
        LIBRARY_DATATYPE_GYRO_TEMPERATURE((byte) 15),
        LIBRARY_DATATYPE_PUT_DOWN_MOTION(ISensorHubCmdProtocol.TYPE_PUT_DOWN_MOTION_SERVICE),
        LIBRARY_DATATYPE_BOUNCE_SHORT_MOTION((byte) 18),
        LIBRARY_DATATYPE_BOUNCE_LONG_MOTION((byte) 20),
        LIBRARY_DATATYPE_WRIST_UP_MOTION((byte) 19),
        LIBRARY_DATATYPE_FLAT_MOTION((byte) 21),
        LIBRARY_DATATYPE_MOVEMENT_ALERT((byte) 22),
        LIBRARY_DATATYPE_TEST_FLAT_MOTION((byte) 23),
        LIBRARY_DATATYPE_TEMPERATURE_ALERT(ISensorHubCmdProtocol.TYPE_TEMPERATURE_ALERT_SERVICE),
        LIBRARY_DATATYPE_SPECIFIC_POSE_ALERT(ISensorHubCmdProtocol.TYPE_SPECIFIC_POSE_ALERT_SERVICE),
        LIBRARY_DATATYPE_ACTIVITY_TRACKER(ISensorHubCmdProtocol.TYPE_ACTIVITY_TRACKER_SERVICE),
        LIBRARY_DATATYPE_STAYING_ALERT((byte) 27),
        LIBRARY_DATATYPE_APDR(ISensorHubCmdProtocol.TYPE_APDR_SERVICE),
        LIBRARY_DATATYPE_LIFE_LOG_COMPONENT(ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE),
        LIBRARY_DATATYPE_CARE_GIVER(ISensorHubCmdProtocol.TYPE_CARE_GIVER_SERVICE),
        LIBRARY_DATATYPE_SLEEP_MONITOR((byte) 37),
        LIBRARY_DATATYPE_ABNORMAL_SHOCK(ISensorHubCmdProtocol.TYPE_ABNORMAL_SHOCK_SERVICE),
        LIBRARY_DATATYPE_CAPTURE_MOTION(ISensorHubCmdProtocol.TYPE_CAPTURE_MOTION_SERVICE),
        LIBRARY_DATATYPE_CALL_MOTION(ISensorHubCmdProtocol.TYPE_CALL_MOTION_SERVICE),
        LIBRARY_DATATYPE_STEP_LEVEL_MONITOR(ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE),
        LIBRARY_DATATYPE_FLAT_MOTION_FOR_TABLE_MODE(ISensorHubCmdProtocol.TYPE_FLAT_MOTION_FOR_TABLE_MODE_SERVICE),
        LIBRARY_DATATYPE_EXERCISE(ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE),
        LIBRARY_DATATYPE_PHONE_STATE_MONITOR(ISensorHubCmdProtocol.TYPE_PHONE_STATE_MONITOR_SERVICE),
        LIBRARY_DATATYPE_AUTO_BRIGHTNESS((byte) 48),
        LIBRARY_DATATYPE_ABNORMAL_PRESSURE_MONITOR(ISensorHubCmdProtocol.TYPE_ABNORMAL_PRESSURE_MONITOR),
        LIBRARY_DATATYPE_HALL_SENSOR((byte) 50),
        LIBRARY_DATATYPE_EAD(ISensorHubCmdProtocol.TYPE_EAD_SERVICE),
        LIBRARY_DATATYPE_DUAL_DISPLAY_ANGLE(ISensorHubCmdProtocol.TYPE_DUAL_DISPLAY_ANGLE_SERVICE),
        LIBRARY_DATATYPE_WIRELESS_CHARGING_MONITOR(ISensorHubCmdProtocol.TYPE_WIRELESS_CHARGING_MONITOR),
        LIBRARY_DATATYPE_SLOCATION(ISensorHubCmdProtocol.TYPE_SLOCATION_SERVICE),
        LIBRARY_DATATYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR(ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE),
        LIBRARY_DATATYPE_MAIN_SCREEN_DETECTION(ISensorHubCmdProtocol.TYPE_MAIN_SCREEN_DETECTION_SERVICE),
        LIBRARY_DATATYPE_ANY_MOTION_DETECTOR(ISensorHubCmdProtocol.TYPE_ANY_MOTION_DETECTOR_SERVICE),
        LIBRARY_DATATYPE_SENSOR_STATUS_CHECK(ISensorHubCmdProtocol.TYPE_SENSOR_STATUS_CHECK_SERVICE),
        LIBRARY_DATATYPE_BOTTOM_FLAT_DETECTOR(ISensorHubCmdProtocol.TYPE_BOTTOM_FLAT_DETECTOR_SERVICE),
        LIBRARY_DATATYPE_CARRYING_STATUS_MONITOR(ISensorHubCmdProtocol.TYPE_CARRYING_STATUS_MONITOR_SERVICE),
        LIBRARY_DATATYPE_STEP_COUNT_TIMER(ISensorHubCmdProtocol.TYPE_STEP_COUNT_TIMER_SERVICE),
        LIBRARY_DATATYPE_SENSORHUB_TIMER(ISensorHubCmdProtocol.TYPE_SENSORHUB_TIMER_SERVICE),
        LIBRARY_DATATYPE_WAKE_UP_VOICE((byte) -29),
        NONLIBRARY_DATATYPE_ORIENTATION((byte) 1),
        NONLIBRARY_DATATYPE_PDRREFPOINT((byte) 2),
        NONLIBRARY_DATATYPE_ACCURACY((byte) 3);
        
        public byte value;

        private DATA_TYPE(byte value) {
            this.value = value;
        }

        public static final String getCode(byte value) {
            String code = "";
            for (DATA_TYPE i : values()) {
                if (i.value == value) {
                    return i.name();
                }
            }
            return code;
        }
    }

    public enum INSTRUCTION {
        INST_LIBRARY((byte) 1),
        INST_NOTI((byte) 2);
        
        public byte value;

        private INSTRUCTION(byte value) {
            this.value = value;
        }
    }

    public enum LIB_TYPE {
        TYPE_LIBRARY((byte) 1),
        TYPE_NONLIBRARY((byte) 2),
        TYPE_LIBRARY_EXT((byte) 3),
        TYPE_SENSORHUB_DEBUG_MSG((byte) 4),
        TYPE_LIBRARY_REQUEST((byte) 5),
        TYPE_NOTI_POWER((byte) 1);
        
        public byte value;

        private LIB_TYPE(byte value) {
            this.value = value;
        }
    }

    public enum MODE {
        NORMAL_MODE((byte) 1),
        INTERRUPT_MODE((byte) 2),
        BATCH_MODE((byte) 4),
        EXTANDED_INTERRUPT_MODE((byte) 8);
        
        public byte value;

        private MODE(byte value) {
            this.value = value;
        }
    }

    public enum PEDOMETER_EXT_LIB_TYPE {
        PEDOMETER_CURRENT_INFO((byte) 1);
        
        protected byte value;

        private PEDOMETER_EXT_LIB_TYPE(byte value) {
            this.value = value;
        }
    }

    public enum POSITIONING_EXT_LIB_TYPE {
        POSITIONING_CURRENT_STATUS((byte) 1);
        
        protected byte value;

        private POSITIONING_EXT_LIB_TYPE(byte value) {
            this.value = value;
        }
    }

    public enum REQUEST_DATA {
        REQUEST_CURRENT_POSITION((byte) 1);
        
        public byte value;

        private REQUEST_DATA(byte value) {
            this.value = value;
        }
    }

    public enum SLEEP_MONITOR_EXT_LIB_TYPE {
        SLEEP_MONITOR_CURRENT_INFO((byte) 1);
        
        protected byte value;

        private SLEEP_MONITOR_EXT_LIB_TYPE(byte value) {
            this.value = value;
        }
    }

    public enum SUB_DATA_TYPE {
        ENVIRONMENT_SENSORTYPE_ACCELEROMETER((byte) 0),
        ENVIRONMENT_SENSORTYPE_GYROSCOPE((byte) 1),
        ENVIRONMENT_SENSORTYPE_GEOMAGNETIC((byte) 2),
        ENVIRONMENT_SENSORTYPE_BAROMETER((byte) 3),
        ENVIRONMENT_SENSORTYPE_GESTURE((byte) 4),
        ENVIRONMENT_SENSORTYPE_PROXIMITY((byte) 5),
        ENVIRONMENT_SENSORTYPE_TEMPERATURE_HUMIDITY((byte) 6),
        ENVIRONMENT_SENSORTYPE_LIGHT((byte) 7);
        
        public byte value;

        private SUB_DATA_TYPE(byte value) {
            this.value = value;
        }
    }
}
