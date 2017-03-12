package com.samsung.sensorframework.sda.p039d;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p030a.p031a.p032a.MicrophoneConfig;
import com.samsung.sensorframework.sda.p039d.p040a.AccelerometerSensor;
import com.samsung.sensorframework.sda.p039d.p040a.BluetoothSensor;
import com.samsung.sensorframework.sda.p039d.p040a.CalendarContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.CallContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.CellSensor;
import com.samsung.sensorframework.sda.p039d.p040a.ContactsContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.GenericAndroidSensor;
import com.samsung.sensorframework.sda.p039d.p040a.ImageContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.LocationSensor;
import com.samsung.sensorframework.sda.p039d.p040a.MicrophoneSensor;
import com.samsung.sensorframework.sda.p039d.p040a.SMSContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.SettingsContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.VideoContentReaderSensor;
import com.samsung.sensorframework.sda.p039d.p040a.WifiSensor;
import com.samsung.sensorframework.sda.p039d.p041b.BatterySensor;
import com.samsung.sensorframework.sda.p039d.p041b.ClipboardSensor;
import com.samsung.sensorframework.sda.p039d.p041b.ConnectionStateSensor;
import com.samsung.sensorframework.sda.p039d.p041b.MusicPlayerStateSensor;
import com.samsung.sensorframework.sda.p039d.p041b.PackageSensor;
import com.samsung.sensorframework.sda.p039d.p041b.PassiveLocationSensor;
import com.samsung.sensorframework.sda.p039d.p041b.PhoneStateSensor;
import com.samsung.sensorframework.sda.p039d.p041b.ProximitySensor;
import com.samsung.sensorframework.sda.p039d.p041b.ScreenSensor;
import com.samsung.sensorframework.sda.p039d.p041b.SmsSensor;

/* renamed from: com.samsung.sensorframework.sda.d.c */
public class SensorUtils {
    public static boolean an(int i) {
        switch (i) {
            case 5001:
            case 5003:
            case 5004:
            case 5005:
            case 5010:
            case 5013:
            case 5014:
            case 5016:
            case 5019:
            case 5020:
            case 5021:
            case 5025:
            case 5026:
            case 5027:
            case 5028:
            case 5029:
            case 5030:
            case 5031:
            case 5032:
            case 5033:
            case 5037:
                return true;
            default:
                return false;
        }
    }

    public static SensorInterface m1639a(int i, Context context) {
        switch (i) {
            case 5001:
                return AccelerometerSensor.aO(context);
            case 5002:
                return BatterySensor.bb(context);
            case 5003:
                return BluetoothSensor.aP(context);
            case 5004:
                return LocationSensor.aV(context);
            case 5005:
                return MicrophoneSensor.aW(context);
            case 5006:
                return PhoneStateSensor.bh(context);
            case 5007:
                return ProximitySensor.bi(context);
            case 5008:
                return ScreenSensor.bj(context);
            case 5009:
                return SmsSensor.bk(context);
            case 5010:
                return WifiSensor.ba(context);
            case 5011:
                return ConnectionStateSensor.bd(context);
            case 5013:
                return SMSContentReaderSensor.aX(context);
            case 5014:
                return CallContentReaderSensor.aR(context);
            case 5016:
                return ContactsContentReaderSensor.aT(context);
            case 5017:
                return PackageSensor.bf(context);
            case 5019:
                return ImageContentReaderSensor.aU(context);
            case 5020:
                return VideoContentReaderSensor.aZ(context);
            case 5021:
                return CalendarContentReaderSensor.aQ(context);
            case 5022:
                return MusicPlayerStateSensor.be(context);
            case 5024:
                return ClipboardSensor.bc(context);
            case 5025:
                return SettingsContentReaderSensor.aY(context);
            case 5026:
            case 5027:
            case 5028:
            case 5029:
            case 5030:
            case 5031:
            case 5032:
            case 5033:
                return GenericAndroidSensor.m1593b(context, i);
            case 5037:
                return CellSensor.aS(context);
            case 5038:
                return PassiveLocationSensor.bg(context);
            default:
                Log.m285d("SensorUtils", "Unknown sensor id: " + i);
                throw new SDAException(8001, "Unknown sensor id");
        }
    }

    public static SensorConfig ao(int i) {
        SensorConfig sensorConfig = new SensorConfig();
        switch (i) {
            case 5001:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(120000));
                sensorConfig.setParameter("ACCELEROMETER_SAMPLING_DELAY", Integer.valueOf(1));
                sensorConfig.setParameter("SENSE_WINDOW_LENGTH_MILLIS", Long.valueOf(8000));
                break;
            case 5002:
                sensorConfig.setParameter("BATTERY_INTENT_FILTERS", "BATTERY_INTENT_FILTER_ALL");
                break;
            case 5003:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(900000));
                sensorConfig.setParameter("NUMBER_OF_SENSE_CYCLES", Integer.valueOf(1));
                sensorConfig.setParameter("SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS", Long.valueOf(12000));
                break;
            case 5004:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(900000));
                sensorConfig.setParameter("SENSE_WINDOW_LENGTH_MILLIS", Long.valueOf(15000));
                sensorConfig.setParameter("LOCATION_ACCURACY", "LOCATION_ACCURACY_COARSE");
                break;
            case 5005:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(120000));
                sensorConfig.setParameter("SENSE_WINDOW_LENGTH_MILLIS", Long.valueOf(5000));
                sensorConfig.setParameter("AUDIO_FILES_DIRECTORY", "s-label/raw_audio_files");
                sensorConfig.setParameter("SAVE_RAW_AUDIO_FILES", MicrophoneConfig.HY);
                sensorConfig.setParameter("ENABLE_AUDIO_STREAMING", MicrophoneConfig.HZ);
                break;
            case 5010:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(900000));
                sensorConfig.setParameter("NUMBER_OF_SENSE_CYCLES", Integer.valueOf(1));
                sensorConfig.setParameter("SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS", Long.valueOf(5000));
                break;
            case 5013:
            case 5014:
            case 5016:
            case 5019:
            case 5020:
            case 5025:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(21600000));
                sensorConfig.setParameter("NUMBER_OF_SENSE_CYCLES", Integer.valueOf(1));
                break;
            case 5021:
                sensorConfig.setParameter("RETRIEVE_FUTURE_EVENTS_ONLY", Boolean.valueOf(false));
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(21600000));
                sensorConfig.setParameter("NUMBER_OF_SENSE_CYCLES", Integer.valueOf(1));
                break;
            case 5026:
            case 5027:
            case 5028:
            case 5029:
            case 5030:
            case 5031:
            case 5032:
            case 5033:
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(120000));
                sensorConfig.setParameter("SENSOR_SAMPLING_DELAY", Integer.valueOf(1));
                sensorConfig.setParameter("SENSE_WINDOW_LENGTH_MILLIS", Long.valueOf(8000));
                break;
            case 5037:
                sensorConfig.setParameter("NUMBER_OF_SENSE_CYCLES", Integer.valueOf(1));
                sensorConfig.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(900000));
                break;
        }
        return sensorConfig;
    }

    public static String ap(int i) {
        switch (i) {
            case 5001:
                return "Accelerometer";
            case 5002:
                return "Battery";
            case 5003:
                return "Bluetooth";
            case 5004:
                return "Location";
            case 5005:
                return "Microphone";
            case 5006:
                return "PhoneState";
            case 5007:
                return "Proximity";
            case 5008:
                return "Screen";
            case 5009:
                return "SMS";
            case 5010:
                return "WiFi";
            case 5011:
                return "Connection";
            case 5013:
                return "SMSContentReader";
            case 5014:
                return "CallContentReader";
            case 5016:
                return "ContactsContentReader";
            case 5017:
                return "Package";
            case 5019:
                return "Image";
            case 5020:
                return "Video";
            case 5021:
                return "CalendarContentReader";
            case 5022:
                return "MusicPlayerState";
            case 5024:
                return "Clipboard";
            case 5025:
                return "Settings";
            case 5026:
                return "AmbientTemperature";
            case 5027:
                return "Gravity";
            case 5028:
                return "Gyroscope";
            case 5029:
                return "Light";
            case 5030:
                return "MagneticField";
            case 5031:
                return "Pressure";
            case 5032:
                return "Humidity";
            case 5033:
                return "Rotation";
            case 5037:
                return "Cell";
            case 5038:
                return "PassiveLocation";
            default:
                throw new SDAException(8002, "unknown sensor type " + i);
        }
    }
}
