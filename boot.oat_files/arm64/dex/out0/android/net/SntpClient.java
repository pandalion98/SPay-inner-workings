package android.net;

import android.media.AudioParameter;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SntpClient {
    private static final int NTP_MODE_CLIENT = 3;
    private static final int NTP_PACKET_SIZE = 48;
    private static final int NTP_PORT = 123;
    private static final int NTP_VERSION = 3;
    private static final long OFFSET_1900_TO_1970 = 2208988800L;
    private static final int ORIGINATE_TIME_OFFSET = 24;
    private static final int RECEIVE_TIME_OFFSET = 32;
    private static final int REFERENCE_TIME_OFFSET = 16;
    private static final boolean SHIP_BUILD = AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(SystemProperties.get("ro.product_ship", AudioParameter.AUDIO_PARAMETER_VALUE_false));
    private static final String TAG = "SntpClient";
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private long mNtpTime;
    private long mNtpTimeReference;
    private long mRoundTripTime;

    private static boolean isCHNOrHKTW() {
        String salesCode = SystemProperties.get("ro.csc.sales_code");
        return "CHC".equals(salesCode) || "CHU".equals(salesCode) || "CHM".equals(salesCode) || "CTC".equals(salesCode) || "BRI".equals(salesCode) || "TGY".equals(salesCode) || "CWT".equals(salesCode) || "FET".equals(salesCode) || "TWM".equals(salesCode) || "CHZ".equals(salesCode) || "CHN".equals(salesCode);
    }

    public boolean requestTime(String host, int timeout) {
        Exception e;
        Throwable th;
        DatagramSocket socket = null;
        try {
            DatagramSocket socket2 = new DatagramSocket();
            try {
                socket2.setSoTimeout(timeout);
                InetAddress address = InetAddress.getByName(host);
                if (isCHNOrHKTW() && address.getHostAddress().equals("120.198.233.90")) {
                    if (!SHIP_BUILD) {
                        Log.d(TAG, "China network resolved wrong dns. Return false.");
                    }
                    if (socket2 != null) {
                        socket2.close();
                    }
                    socket = socket2;
                    return false;
                }
                byte[] buffer = new byte[48];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, 123);
                buffer[0] = (byte) 27;
                long requestTime = System.currentTimeMillis();
                long requestTicks = SystemClock.elapsedRealtime();
                writeTimeStamp(buffer, 40, requestTime);
                socket2.send(request);
                socket2.receive(new DatagramPacket(buffer, buffer.length));
                long responseTicks = SystemClock.elapsedRealtime();
                long responseTime = requestTime + (responseTicks - requestTicks);
                long originateTime = readTimeStamp(buffer, 24);
                long receiveTime = readTimeStamp(buffer, 32);
                long transmitTime = readTimeStamp(buffer, 40);
                long roundTripTime = (responseTicks - requestTicks) - (transmitTime - receiveTime);
                long clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime)) / 2;
                if ((originateTime < 0 || receiveTime < 0 || transmitTime < 0) && isCHNOrHKTW()) {
                    if (!SHIP_BUILD) {
                        Log.d(TAG, "time value is wrong originateTime : " + originateTime + " receiveTime : " + receiveTime + " transmitTime : " + transmitTime);
                    }
                    if (socket2 != null) {
                        socket2.close();
                    }
                    socket = socket2;
                    return false;
                }
                this.mNtpTime = responseTime + clockOffset;
                this.mNtpTimeReference = responseTicks;
                this.mRoundTripTime = roundTripTime;
                if (socket2 != null) {
                    socket2.close();
                }
                socket = socket2;
                return true;
            } catch (Exception e2) {
                e = e2;
                socket = socket2;
                try {
                    Log.d(TAG, "request time failed server: " + host + " with error : " + e);
                    if (socket != null) {
                        return false;
                    }
                    socket.close();
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    if (socket != null) {
                        socket.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                socket = socket2;
                if (socket != null) {
                    socket.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            if (!SHIP_BUILD && isCHNOrHKTW()) {
                Log.d(TAG, "request time failed server: " + host + " with error : " + e);
            }
            if (socket != null) {
                return false;
            }
            socket.close();
            return false;
        }
    }

    public long getNtpTime() {
        return this.mNtpTime;
    }

    public long getNtpTimeReference() {
        return this.mNtpTimeReference;
    }

    public long getRoundTripTime() {
        return this.mRoundTripTime;
    }

    private long read32(byte[] buffer, int offset) {
        int i0;
        int i1;
        int i2;
        int i3;
        byte b0 = buffer[offset];
        byte b1 = buffer[offset + 1];
        byte b2 = buffer[offset + 2];
        byte b3 = buffer[offset + 3];
        if ((b0 & 128) == 128) {
            i0 = (b0 & 127) + 128;
        } else {
            byte i02 = b0;
        }
        if ((b1 & 128) == 128) {
            i1 = (b1 & 127) + 128;
        } else {
            byte i12 = b1;
        }
        if ((b2 & 128) == 128) {
            i2 = (b2 & 127) + 128;
        } else {
            byte i22 = b2;
        }
        if ((b3 & 128) == 128) {
            i3 = (b3 & 127) + 128;
        } else {
            byte i32 = b3;
        }
        return (((((long) i0) << 24) + (((long) i1) << 16)) + (((long) i2) << 8)) + ((long) i3);
    }

    private long readTimeStamp(byte[] buffer, int offset) {
        return ((read32(buffer, offset) - OFFSET_1900_TO_1970) * 1000) + ((1000 * read32(buffer, offset + 4)) / 4294967296L);
    }

    private void writeTimeStamp(byte[] buffer, int offset, long time) {
        long seconds = time / 1000;
        long milliseconds = time - (1000 * seconds);
        seconds += OFFSET_1900_TO_1970;
        int i = offset + 1;
        buffer[offset] = (byte) ((int) (seconds >> 24));
        offset = i + 1;
        buffer[i] = (byte) ((int) (seconds >> 16));
        i = offset + 1;
        buffer[offset] = (byte) ((int) (seconds >> 8));
        offset = i + 1;
        buffer[i] = (byte) ((int) (seconds >> null));
        long fraction = (4294967296L * milliseconds) / 1000;
        i = offset + 1;
        buffer[offset] = (byte) ((int) (fraction >> 24));
        offset = i + 1;
        buffer[i] = (byte) ((int) (fraction >> 16));
        i = offset + 1;
        buffer[offset] = (byte) ((int) (fraction >> 8));
        offset = i + 1;
        buffer[i] = (byte) ((int) (Math.random() * 255.0d));
    }
}
