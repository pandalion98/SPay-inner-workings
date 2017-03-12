package com.samsung.sensorframework.sda.p036c;

import android.content.Context;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.p036c.p037a.AccelerometerProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.AudioProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.BluetoothProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.CalendarContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.CallContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.CellProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.ContactsContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.GenericAndroidSensorProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.ImageContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.LocationProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.SMSContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.SettingsContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.VideoContentReaderProcessor;
import com.samsung.sensorframework.sda.p036c.p037a.WifiProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.BatteryProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.ClipboardProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.ConnectionStateProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.MusicPlayerStateProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.PackageProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.PhoneStateProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.ProximityProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.SMSProcessor;
import com.samsung.sensorframework.sda.p036c.p038b.ScreenProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.a */
public abstract class AbstractProcessor {
    protected final boolean Je;
    protected final boolean Jf;
    protected final Context Jg;

    public static AbstractProcessor m1531a(Context context, int i, boolean z, boolean z2) {
        if (z || z2) {
            switch (i) {
                case 5001:
                    return new AccelerometerProcessor(context, z, z2);
                case 5002:
                    return new BatteryProcessor(context, z, z2);
                case 5003:
                    return new BluetoothProcessor(context, z, z2);
                case 5004:
                    return new LocationProcessor(context, z, z2);
                case 5005:
                    return new AudioProcessor(context, z, z2);
                case 5006:
                    return new PhoneStateProcessor(context, z, z2);
                case 5007:
                    return new ProximityProcessor(context, z, z2);
                case 5008:
                    return new ScreenProcessor(context, z, z2);
                case 5009:
                    return new SMSProcessor(context, z, z2);
                case 5010:
                    return new WifiProcessor(context, z, z2);
                case 5011:
                    return new ConnectionStateProcessor(context, z, z2);
                case 5013:
                    return new SMSContentReaderProcessor(context, z, z2);
                case 5014:
                    return new CallContentReaderProcessor(context, z, z2);
                case 5016:
                    return new ContactsContentReaderProcessor(context, z, z2);
                case 5017:
                    return new PackageProcessor(context, z, z2);
                case 5019:
                    return new ImageContentReaderProcessor(context, z, z2);
                case 5020:
                    return new VideoContentReaderProcessor(context, z, z2);
                case 5021:
                    return new CalendarContentReaderProcessor(context, z, z2);
                case 5022:
                    return new MusicPlayerStateProcessor(context, z, z2);
                case 5024:
                    return new ClipboardProcessor(context, z, z2);
                case 5025:
                    return new SettingsContentReaderProcessor(context, z, z2);
                case 5026:
                case 5027:
                case 5028:
                case 5029:
                case 5030:
                case 5031:
                case 5032:
                case 5033:
                    return new GenericAndroidSensorProcessor(context, z, z2);
                case 5037:
                    return new CellProcessor(context, z, z2);
                case 5038:
                    return new LocationProcessor(context, z, z2);
                default:
                    throw new SDAException(8001, "No processor defined for this sensor.");
            }
        }
        throw new SDAException(8007, "No data (raw/processed) requested from the processor");
    }

    public AbstractProcessor(Context context, boolean z, boolean z2) {
        this.Jg = context;
        this.Je = z;
        this.Jf = z2;
    }
}
