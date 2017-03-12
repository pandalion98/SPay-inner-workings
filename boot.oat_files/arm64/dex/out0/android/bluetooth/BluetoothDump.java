package android.bluetooth;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class BluetoothDump {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothDump";
    private static final boolean VDBG = false;
    static String day;
    static String hour;
    private static String lineFeed = "\n";
    static String milisec;
    static String min;
    static String month;
    static String sec;
    private static String sysdump_time;
    BluetoothAdapter mBluetoothAdapter;

    public static void BtLog(String cmd) {
        new BluetoothDump().putLogs(new String(new StringBuilder(getTimeToString()).append("--").append(cmd).append(lineFeed)));
    }

    public void putLogs(String cmd) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            this.mBluetoothAdapter.putLogs(cmd);
        }
    }

    private static String getTimeToString() {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00", DecimalFormatSymbols.getInstance(Locale.US));
        DecimalFormat df3 = new DecimalFormat("000", DecimalFormatSymbols.getInstance(Locale.US));
        month = df.format((long) (cal.get(2) + 1));
        day = df.format((long) cal.get(5));
        hour = df.format((long) cal.get(11));
        min = df.format((long) cal.get(12));
        sec = df.format((long) cal.get(13));
        milisec = df3.format((long) cal.get(14));
        sysdump_time = hour + ":" + min + ":" + sec + "." + milisec;
        return sysdump_time;
    }
}
