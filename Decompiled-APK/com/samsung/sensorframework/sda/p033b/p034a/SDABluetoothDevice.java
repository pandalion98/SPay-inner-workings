package com.samsung.sensorframework.sda.p033b.p034a;

/* renamed from: com.samsung.sensorframework.sda.b.a.r */
public class SDABluetoothDevice {
    private final String Ir;
    private final String Is;
    private final float It;
    private final long timestamp;

    public /* synthetic */ Object clone() {
        return gV();
    }

    public SDABluetoothDevice(long j, String str, String str2, float f) {
        this.timestamp = j;
        this.Ir = str;
        this.Is = str2;
        this.It = f;
    }

    public SDABluetoothDevice gV() {
        return new SDABluetoothDevice(this.timestamp, this.Ir, this.Is, this.It);
    }
}
