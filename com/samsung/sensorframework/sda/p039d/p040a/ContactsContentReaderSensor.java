package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.ContactsContract.Data;

/* renamed from: com.samsung.sensorframework.sda.d.a.h */
public class ContactsContentReaderSensor extends AbstractContentReaderSensor {
    private static ContactsContentReaderSensor JG;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_CONTACTS"};
    }

    public static ContactsContentReaderSensor aT(Context context) {
        if (JG == null) {
            synchronized (lock) {
                if (JG == null) {
                    JG = new ContactsContentReaderSensor(context);
                }
            }
        }
        return JG;
    }

    private ContactsContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        JG = null;
    }

    protected String[] hb() {
        return Jz;
    }

    public int getSensorType() {
        return 5016;
    }

    protected String[] hj() {
        return new String[]{Data.CONTENT_URI.toString()};
    }

    protected String[] hk() {
        if (VERSION.SDK_INT >= 18) {
            return new String[]{"_id", "display_name", "data1", "data1", "mimetype", "contact_last_updated_timestamp"};
        }
        return new String[]{"_id", "display_name", "data1", "data1", "mimetype"};
    }

    protected String he() {
        return "ContactsContentReaderSensor";
    }
}
