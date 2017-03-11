package com.samsung.sensorframework.sdi.p046c;

import android.content.Context;
import android.location.Location;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.sensorframework.sda.SensorDataListener;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p033b.p035b.BatteryData;
import com.samsung.sensorframework.sda.p033b.p035b.ConnectionStateData;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;

/* renamed from: com.samsung.sensorframework.sdi.c.b */
public class SFSensorDataListener implements SensorDataListener {
    private LocationData JP;
    private final Object KN;
    private boolean Kb;
    private SensingController La;
    private BatteryData Lk;
    private ConnectionStateData Ll;
    private LocationData Lm;

    /* renamed from: com.samsung.sensorframework.sdi.c.b.a */
    class SFSensorDataListener extends Thread {
        final /* synthetic */ SFSensorDataListener Ln;

        public SFSensorDataListener(SFSensorDataListener sFSensorDataListener) {
            this.Ln = sFSensorDataListener;
            super("SensorDataProcessorThread");
            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread instantiated");
        }

        public void run() {
            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread started");
            while (this.Ln.Kb) {
                try {
                    BatteryData c;
                    ConnectionStateData d;
                    LocationData e;
                    synchronized (this.Ln.KN) {
                        if (this.Ln.Lk == null && this.Ln.Ll == null && this.Ln.JP == null) {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread waiting");
                            this.Ln.KN.wait();
                        }
                        Log.m285d("SFSensorDataListener", "SensorDataProcessorThread processing data");
                        c = this.Ln.Lk;
                        this.Ln.Lk = null;
                        d = this.Ln.Ll;
                        this.Ln.Ll = null;
                        e = this.Ln.JP;
                        this.Ln.JP = null;
                    }
                    if (c != null) {
                        if (this.Ln.Kb) {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread calling onBatteryDataReceived()");
                            this.Ln.La.m1687a(c);
                        } else {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread Battery: isRunning is false");
                        }
                    }
                    if (d != null) {
                        Log.m285d("SFSensorDataListener", "SensorDataProcessorThread, Connection: " + d.toString());
                        Log.m285d("SFSensorDataListener", "SensorDataProcessorThread, sleeping for 30 seconds ");
                        Thread.sleep(PaymentNetworkProvider.NFC_WAIT_TIME);
                        if (this.Ln.Kb) {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread calling onConnectivityChanged()");
                            this.Ln.La.m1688a(d);
                        } else {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread ConnectionState: isRunning is false");
                        }
                    }
                    if (e != null) {
                        if (this.Ln.Kb) {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread calling onLocationDataReceived()");
                            this.Ln.La.m1690c(e);
                        } else {
                            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread Location: isRunning is false");
                        }
                    }
                } catch (InterruptedException e2) {
                }
            }
            Log.m285d("SFSensorDataListener", "SensorDataProcessorThread terminated");
        }
    }

    public SFSensorDataListener(Context context) {
        this.La = SensingController.br(context);
        this.KN = new Object();
        this.Kb = true;
        new SFSensorDataListener(this).start();
        Log.m285d("SFSensorDataListener", "sensor data listener created.");
    }

    public void hP() {
        Log.m285d("SFSensorDataListener", "stop listener.");
        this.Kb = false;
        synchronized (this.KN) {
            this.KN.notify();
        }
    }

    private boolean m1671a(LocationData locationData, LocationData locationData2) {
        if (!(locationData == null || locationData2 == null)) {
            Location location = locationData.getLocation();
            Location location2 = locationData2.getLocation();
            if (location != null && location2 != null && location.getTime() == location2.getTime() && location.getElapsedRealtimeNanos() == location2.getElapsedRealtimeNanos() && location.getLatitude() == location2.getLatitude() && location.getLongitude() == location2.getLongitude()) {
                Log.m285d("SFSensorDataListener", "Location1: " + location.toString() + " Location2: " + location2.toString());
                return true;
            }
        }
        return false;
    }

    public void m1678a(SensorData sensorData) {
        if (sensorData != null) {
            Log.m285d("SFSensorDataListener", "Received sensor data: " + sensorData.getClass());
            synchronized (this.KN) {
                if (sensorData.getSensorType() == 5002) {
                    this.Lk = (BatteryData) sensorData;
                } else if (sensorData.getSensorType() == 5011) {
                    this.Ll = (ConnectionStateData) sensorData;
                } else if (sensorData.getSensorType() == 5004 || sensorData.getSensorType() == 5038) {
                    if (m1671a(this.Lm, (LocationData) sensorData)) {
                        Log.m285d("SFSensorDataListener", "onDataSensed() ignoring sensorData and it's same as lastProcessedLocationData");
                    } else {
                        this.Lm = (LocationData) sensorData;
                        this.JP = (LocationData) sensorData;
                    }
                } else {
                    Log.m285d("SFSensorDataListener", "Unknown sensor: " + DataAcquisitionUtils.ap(sensorData.getSensorType()));
                }
                this.KN.notify();
            }
        } else {
            Log.m285d("SFSensorDataListener", "Received sensor data is null");
        }
        Log.m285d("SFSensorDataListener", "onDataSensed() finished");
    }
}
