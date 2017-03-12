package com.samsung.sensorframework.sda.p039d.p040a;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.BluetoothData;
import com.samsung.sensorframework.sda.p033b.p034a.SDABluetoothDevice;
import com.samsung.sensorframework.sda.p036c.p037a.BluetoothProcessor;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.d.a.d */
public class BluetoothSensor extends AbstractPullSensor {
    private static BluetoothSensor Jx;
    private static final String[] Jz;
    private static final Object lock;
    private ArrayList<SDABluetoothDevice> Jt;
    private HashMap<String, SDABluetoothDevice> Ju;
    private BluetoothAdapter Jv;
    private int Jw;
    private BluetoothData Jy;

    /* renamed from: com.samsung.sensorframework.sda.d.a.d.1 */
    class BluetoothSensor extends BroadcastReceiver {
        final /* synthetic */ BluetoothSensor JA;

        BluetoothSensor(BluetoothSensor bluetoothSensor) {
            this.JA = bluetoothSensor;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                SDABluetoothDevice sDABluetoothDevice = new SDABluetoothDevice(System.currentTimeMillis(), ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress(), ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getName(), (float) intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE));
                if (this.JA.Jt != null && !this.JA.Jt.contains(sDABluetoothDevice)) {
                    this.JA.Jt.add(sDABluetoothDevice);
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                this.JA.Jw = this.JA.Jw - 1;
                if (this.JA.Jw > 0) {
                    this.JA.Jv.startDiscovery();
                } else {
                    this.JA.ho();
                }
            } else if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                String address = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                String name = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getName();
                short shortExtra = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
                if (address != null && address.length() > 0) {
                    this.JA.Ju.put(address.toUpperCase(), new SDABluetoothDevice(System.currentTimeMillis(), address, name, (float) shortExtra));
                }
            } else if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                action = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                if (action != null && action.length() > 0 && this.JA.Ju.containsKey(action.toUpperCase())) {
                    this.JA.Ju.remove(action.toUpperCase());
                }
            }
        }
    }

    protected /* synthetic */ SensorData hl() {
        return hq();
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN"};
    }

    public static BluetoothSensor aP(Context context) {
        if (Jx == null) {
            synchronized (lock) {
                if (Jx == null) {
                    Jx = new BluetoothSensor(context);
                }
            }
        }
        return Jx;
    }

    private BluetoothSensor(Context context) {
        super(context);
        this.Jv = null;
        this.Jv = BluetoothAdapter.getDefaultAdapter();
        if (this.Jv == null) {
            Log.m285d("BluetoothSensor", "Device does not support Bluetooth");
            return;
        }
        this.Ju = new HashMap();
        BroadcastReceiver bluetoothSensor = new BluetoothSensor(this);
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        IntentFilter intentFilter2 = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        IntentFilter intentFilter3 = new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED");
        IntentFilter intentFilter4 = new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.HR.registerReceiver(bluetoothSensor, intentFilter);
        this.HR.registerReceiver(bluetoothSensor, intentFilter2);
        this.HR.registerReceiver(bluetoothSensor, intentFilter3);
        this.HR.registerReceiver(bluetoothSensor, intentFilter4);
    }

    public void gY() {
        super.gY();
        Jx = null;
    }

    protected String[] hb() {
        return Jz;
    }

    protected String he() {
        return "BluetoothSensor";
    }

    public int getSensorType() {
        return 5003;
    }

    protected BluetoothData hq() {
        return this.Jy;
    }

    protected ArrayList<SDABluetoothDevice> hr() {
        ArrayList<SDABluetoothDevice> arrayList = new ArrayList();
        for (String str : this.Ju.keySet()) {
            arrayList.add(((SDABluetoothDevice) this.Ju.get(str)).gV());
        }
        return arrayList;
    }

    protected void hm() {
        this.Jy = ((BluetoothProcessor) hi()).m1534b(this.Jn, this.Jt, hr(), this.Id.gS());
    }

    protected boolean hc() {
        this.Jt = null;
        if (!this.Jv.isEnabled()) {
            return false;
        }
        this.Jt = new ArrayList();
        this.Jw = ((Integer) this.Id.getParameter("NUMBER_OF_SENSE_CYCLES")).intValue();
        this.Jv.startDiscovery();
        return true;
    }

    protected void hd() {
        if (this.Jv != null) {
            this.Jv.cancelDiscovery();
        }
    }
}
