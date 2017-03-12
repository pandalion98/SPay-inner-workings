package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.SMSContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.SMSContentReaderEntry;
import java.util.HashMap;
import java.util.Iterator;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.sensorframework.sda.c.a.l */
public class SMSContentReaderProcessor extends ContentReaderProcessor {
    public SMSContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected AbstractContentReaderListData m1553b(long j, SensorConfig sensorConfig) {
        return new SMSContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1552b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry sMSContentReaderEntry = new SMSContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                if (str.equals("address")) {
                    str2 = cd(str2);
                } else if (str.equals("body")) {
                    sMSContentReaderEntry.set("bodyWordCount", cf(str2) + BuildConfig.FLAVOR);
                    sMSContentReaderEntry.set("bodyLength", cg(str2) + BuildConfig.FLAVOR);
                    str2 = ce(str2);
                } else if (str.equals("type")) {
                    str2 = getType(str2);
                }
                sMSContentReaderEntry.set(str, str2);
                it.remove();
            }
            return sMSContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getType(String str) {
        try {
            switch (Integer.valueOf(str).intValue()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    return "all";
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    return "inbox";
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    return "sent";
                case F2m.PPB /*3*/:
                    return "draft";
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    return "outbox";
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    return "failed";
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    return "queued";
                default:
                    return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private int cf(String str) {
        int i = 0;
        if (str == null) {
            return i;
        }
        try {
            return str.split(" ").length;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    private int cg(String str) {
        int i = 0;
        if (str != null) {
            try {
                i = str.length();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }
}
