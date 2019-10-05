/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.location.Location
 *  java.lang.Class
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 */
package com.samsung.sensorframework.sdi.c;

import android.content.Context;
import android.location.Location;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.b.a.p;

public class b
implements com.samsung.sensorframework.sda.b {
    private p JP;
    private final Object KN;
    private boolean Kb;
    private c La;
    private com.samsung.sensorframework.sda.b.b.a Lk;
    private com.samsung.sensorframework.sda.b.b.c Ll;
    private p Lm;

    public b(Context context) {
        this.La = c.br(context);
        this.KN = new Object();
        this.Kb = true;
        new a().start();
        Log.d("SFSensorDataListener", "sensor data listener created.");
    }

    private boolean a(p p2, p p3) {
        if (p2 != null && p3 != null) {
            Location location = p2.getLocation();
            Location location2 = p3.getLocation();
            if (location != null && location2 != null && location.getTime() == location2.getTime() && location.getElapsedRealtimeNanos() == location2.getElapsedRealtimeNanos() && location.getLatitude() == location2.getLatitude() && location.getLongitude() == location2.getLongitude()) {
                Log.d("SFSensorDataListener", "Location1: " + location.toString() + " Location2: " + location2.toString());
                return true;
            }
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void a(com.samsung.sensorframework.sda.b.a a2) {
        if (a2 != null) {
            Object object;
            Log.d("SFSensorDataListener", "Received sensor data: " + (Object)a2.getClass());
            Object object2 = object = this.KN;
            synchronized (object2) {
                if (a2.getSensorType() == 5002) {
                    this.Lk = (com.samsung.sensorframework.sda.b.b.a)a2;
                } else if (a2.getSensorType() == 5011) {
                    this.Ll = (com.samsung.sensorframework.sda.b.b.c)a2;
                } else if (a2.getSensorType() == 5004 || a2.getSensorType() == 5038) {
                    if (!this.a(this.Lm, (p)a2)) {
                        this.Lm = (p)a2;
                        this.JP = (p)a2;
                    } else {
                        Log.d("SFSensorDataListener", "onDataSensed() ignoring sensorData and it's same as lastProcessedLocationData");
                    }
                } else {
                    Log.d("SFSensorDataListener", "Unknown sensor: " + com.samsung.sensorframework.sda.f.a.ap(a2.getSensorType()));
                }
                this.KN.notify();
            }
        } else {
            Log.d("SFSensorDataListener", "Received sensor data is null");
        }
        Log.d("SFSensorDataListener", "onDataSensed() finished");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void hP() {
        Object object;
        Log.d("SFSensorDataListener", "stop listener.");
        this.Kb = false;
        Object object2 = object = this.KN;
        synchronized (object2) {
            this.KN.notify();
            return;
        }
    }

    class a
    extends Thread {
        public a() {
            super("SensorDataProcessorThread");
            Log.d("SFSensorDataListener", "SensorDataProcessorThread instantiated");
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public void run() {
            Log.d("SFSensorDataListener", "SensorDataProcessorThread started");
            do {
                com.samsung.sensorframework.sda.b.b.c c2;
                p p2;
                block16 : {
                    block15 : {
                        if (!b.this.Kb) {
                            Log.d("SFSensorDataListener", "SensorDataProcessorThread terminated");
                            return;
                        }
                        try {
                            Object object;
                            Object object2 = object = b.this.KN;
                            // MONITORENTER : object2
                        }
                        catch (InterruptedException interruptedException) {}
                        if (b.this.Lk == null && b.this.Ll == null && b.this.JP == null) {
                            Log.d("SFSensorDataListener", "SensorDataProcessorThread waiting");
                            b.this.KN.wait();
                        }
                        Log.d("SFSensorDataListener", "SensorDataProcessorThread processing data");
                        com.samsung.sensorframework.sda.b.b.a a2 = b.this.Lk;
                        b.this.Lk = null;
                        c2 = b.this.Ll;
                        b.this.Ll = null;
                        p2 = b.this.JP;
                        b.this.JP = null;
                        // MONITOREXIT : object2
                        if (a2 != null) {
                            if (!b.this.Kb) break block15;
                            Log.d("SFSensorDataListener", "SensorDataProcessorThread calling onBatteryDataReceived()");
                            b.this.La.a(a2);
                            break block16;
                        }
                        break block16;
                        continue;
                    }
                    Log.d("SFSensorDataListener", "SensorDataProcessorThread Battery: isRunning is false");
                }
                if (c2 != null) {
                    Log.d("SFSensorDataListener", "SensorDataProcessorThread, Connection: " + c2.toString());
                    Log.d("SFSensorDataListener", "SensorDataProcessorThread, sleeping for 30 seconds ");
                    Thread.sleep((long)30000L);
                    if (b.this.Kb) {
                        Log.d("SFSensorDataListener", "SensorDataProcessorThread calling onConnectivityChanged()");
                        b.this.La.a(c2);
                    } else {
                        Log.d("SFSensorDataListener", "SensorDataProcessorThread ConnectionState: isRunning is false");
                    }
                }
                if (p2 == null) continue;
                if (b.this.Kb) {
                    Log.d("SFSensorDataListener", "SensorDataProcessorThread calling onLocationDataReceived()");
                    b.this.La.c(p2);
                    continue;
                }
                Log.d("SFSensorDataListener", "SensorDataProcessorThread Location: isRunning is false");
            } while (true);
        }
    }

}

