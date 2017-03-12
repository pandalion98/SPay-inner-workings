package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.CallContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.CallContentReaderEntry;
import java.util.HashMap;
import java.util.Iterator;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.sensorframework.sda.c.a.e */
public class CallContentReaderProcessor extends ContentReaderProcessor {
    public CallContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected AbstractContentReaderListData m1542b(long j, SensorConfig sensorConfig) {
        return new CallContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1541b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry callContentReaderEntry = new CallContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                if (str.equals("number") || str.equals("address")) {
                    str2 = cd(str2);
                } else if (str.equals("type")) {
                    str2 = getType(str2);
                }
                callContentReaderEntry.set(str, str2);
                it.remove();
            }
            return callContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getType(String str) {
        try {
            switch (Integer.valueOf(str).intValue()) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    return "incoming";
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    return "outgoing";
                case F2m.PPB /*3*/:
                    return "missed";
                default:
                    return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }
}
