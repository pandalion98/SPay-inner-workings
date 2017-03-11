package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.SmsData;
import com.samsung.sensorframework.sda.p036c.CommunicationProcessor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.samsung.sensorframework.sda.c.b.h */
public class SMSProcessor extends CommunicationProcessor {
    private final HashMap<String, ArrayList<String>> Jh;

    public SMSProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
        this.Jh = gX();
    }

    public SmsData m1569a(long j, SensorConfig sensorConfig, String str, String str2, String str3, String str4) {
        SmsData smsData = new SmsData(j, sensorConfig);
        String[] split = str.split(" ");
        if (this.Je) {
            smsData.am(split.length);
            smsData.al(str.length());
            smsData.setAddress(cd(str2));
            smsData.cb(str3);
            smsData.bZ(ce(str));
            smsData.ca(str4);
        }
        if (this.Jf) {
            for (String ch : split) {
                Iterator it = ch(ch).iterator();
                while (it.hasNext()) {
                    smsData.addCategory((String) it.next());
                }
            }
        }
        return smsData;
    }

    private ArrayList<String> ch(String str) {
        ArrayList<String> arrayList = new ArrayList();
        for (String str2 : this.Jh.keySet()) {
            if (str.matches(str2)) {
                arrayList.addAll((Collection) this.Jh.get(str2));
            }
        }
        return arrayList;
    }

    private HashMap<String, ArrayList<String>> gX() {
        HashMap<String, ArrayList<String>> hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.Jg.getAssets().open("UNIMPLEMENTED")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(",");
                Object obj = split[0];
                ArrayList arrayList = new ArrayList();
                for (int i = 1; i < split.length; i++) {
                    arrayList.add(split[1]);
                }
                hashMap.put(obj, arrayList);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            hashMap.clear();
        }
        return hashMap;
    }
}
