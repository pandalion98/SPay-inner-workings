package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.ContactDetails;
import com.samsung.sensorframework.sda.p033b.p034a.ContactsContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.ContactsContentReaderEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.samsung.sensorframework.sda.c.a.g */
public class ContactsContentReaderProcessor extends ContentReaderProcessor {
    public ContactsContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public AbstractContentReaderListData m1544a(long j, int i, ArrayList<HashMap<String, String>> arrayList, SensorConfig sensorConfig) {
        ContactsContentListData contactsContentListData = (ContactsContentListData) super.m1535a(j, i, arrayList, sensorConfig);
        HashMap hashMap = new HashMap();
        Iterator it = contactsContentListData.gU().iterator();
        while (it.hasNext()) {
            AbstractContentReaderEntry abstractContentReaderEntry = (AbstractContentReaderEntry) it.next();
            String str = abstractContentReaderEntry.get("display_name");
            if (str != null) {
                String str2 = abstractContentReaderEntry.get("mimetype");
                if (str2 != null) {
                    String str3;
                    if (str2.equals("vnd.android.cursor.item/phone_v2")) {
                        str2 = null;
                        str3 = abstractContentReaderEntry.get("data1");
                    } else if (str2.equals("vnd.android.cursor.item/email_v2")) {
                        str2 = abstractContentReaderEntry.get("data1");
                        str3 = null;
                    } else {
                        str2 = null;
                        str3 = null;
                    }
                    if (str3 != null || str2 != null) {
                        ContactDetails contactDetails;
                        if (hashMap.containsKey(str)) {
                            contactDetails = (ContactDetails) hashMap.get(str);
                        } else {
                            contactDetails = new ContactDetails(str);
                            hashMap.put(str, contactDetails);
                        }
                        if (str3 != null) {
                            contactDetails.setPhoneNumber(str3);
                        }
                        if (str2 != null) {
                            contactDetails.setEmail(str2);
                        }
                    }
                }
            }
        }
        contactsContentListData.m1518a(hashMap);
        return contactsContentListData;
    }

    protected AbstractContentReaderListData m1546b(long j, SensorConfig sensorConfig) {
        return new ContactsContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1545b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry contactsContentReaderEntry = new ContactsContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                if (str.equals("data1") || str.equals("data1")) {
                    String str3 = contactsContentReaderEntry.get("mimetype");
                    if (str3 == null || !str3.equals("vnd.android.cursor.item/phone_v2")) {
                        str2 = ce(str2);
                    } else {
                        str2 = cd(str2);
                    }
                } else if (str.equals("display_name")) {
                    str2 = ce(str2);
                }
                contactsContentReaderEntry.set(str, str2);
                it.remove();
            }
            return contactsContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
