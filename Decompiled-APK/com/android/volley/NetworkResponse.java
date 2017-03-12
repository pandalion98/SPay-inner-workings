package com.android.volley;

import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;

/* renamed from: com.android.volley.g */
public class NetworkResponse {
    public final Map<String, String> aw;
    public final boolean ax;
    public final byte[] data;
    public final long networkTimeMs;
    public final int statusCode;

    public NetworkResponse(int i, byte[] bArr, Map<String, String> map, boolean z, long j) {
        this.statusCode = i;
        this.data = bArr;
        this.aw = map;
        this.ax = z;
        this.networkTimeMs = j;
    }

    public NetworkResponse(byte[] bArr, Map<String, String> map) {
        this(DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE, bArr, map, false, 0);
    }
}
