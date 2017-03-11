package com.samsung.android.spayfw.fraud.p013b;

import android.database.Cursor;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.fraud.FraudModule;
import com.samsung.android.spayfw.fraud.p011a.FCardRecord;
import com.samsung.android.spayfw.fraud.p011a.FraudDbAdapter;
import com.samsung.android.spayfw.fraud.p011a.p012a.FraudEfsAdapter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.fraud.b.b */
public class ModelUtils {
    private static final String[] EMPTY_STRING_ARRAY;

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.a */
    public static class ModelUtils {
        private String oo;
        private ArrayList<String> oq;

        private ModelUtils(String str, ArrayList<String> arrayList) {
            this.oq = new ArrayList();
            this.oo = str;
            this.oq.addAll(arrayList);
        }

        public ModelUtils bP() {
            this.oo = "select distinct * from (" + this.oo + ")";
            return this;
        }

        public List<ModelUtils> bQ() {
            Cursor b = ModelUtils.rawQuery(this.oo, (String[]) this.oq.toArray(ModelUtils.EMPTY_STRING_ARRAY));
            List linkedList = new LinkedList();
            if (b != null) {
                while (b.moveToNext()) {
                    String string = b.getString(0);
                    String string2 = b.getString(1);
                    if (!(string == null || string2 == null)) {
                        linkedList.add(new ModelUtils(string, string2));
                    }
                }
                b.close();
            }
            return linkedList;
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.b */
    public static class ModelUtils {
        public final String street;
        public final String zip;

        public ModelUtils(String str, String str2) {
            this.street = str;
            this.zip = str2;
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.c */
    public static class ModelUtils {
        public final String nM;
        public final String nN;

        public ModelUtils(String str, String str2) {
            this.nM = str;
            this.nN = str2;
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.d */
    public static class ModelUtils {
        private String oo;
        private ArrayList<String> oq;

        private ModelUtils(String str, ArrayList<String> arrayList) {
            this.oo = "select * from fcard";
            this.oq = new ArrayList();
            this.oo = str;
            this.oq.addAll(arrayList);
        }

        public ModelUtils bR() {
            return new ModelUtils(this.oq, null);
        }

        public ModelUtils bS() {
            return new ModelUtils(this.oq, null);
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.e */
    public static class ModelUtils {
        private String oo;
        private ArrayList<String> oq;

        public ModelUtils() {
            this.oo = "select * from fdevice_info";
            this.oq = new ArrayList();
        }

        public ModelUtils ak(String str) {
            String str2 = HCEClientConstants.TAG_KEY_SEPARATOR + str;
            this.oo = "select * from (" + this.oo + ") where datetime(time/1000, 'unixepoch') > datetime(current_timestamp, ?)";
            this.oq.add(str2);
            return this;
        }

        public ModelUtils bT() {
            this.oo = "select * from (" + this.oo + ") where " + "reason" + " in " + "(\"" + "app_reset" + "\", \"" + "factory_reset" + "\")";
            return this;
        }

        public int getCount() {
            Cursor c = ModelUtils.m728a("select count(*) from (" + this.oo + ")", (String[]) this.oq.toArray(ModelUtils.EMPTY_STRING_ARRAY));
            int intValue = ModelUtils.m729a(c, 0, Integer.valueOf(0)).intValue();
            if (c != null) {
                c.close();
            }
            return intValue;
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.f */
    public static class ModelUtils {
        private String oo;
        private ArrayList<String> oq;

        public ModelUtils() {
            this.oo = "select * from fcard";
            this.oq = new ArrayList();
        }

        public ModelUtils bU() {
            this.oo = "select * from (" + this.oo + ") where result = ?";
            this.oq.add(String.valueOf(0));
            return this;
        }

        public ModelUtils al(String str) {
            String str2 = HCEClientConstants.TAG_KEY_SEPARATOR + str;
            this.oo = "select * from (" + this.oo + ") where datetime(attempt_time/1000, 'unixepoch') > datetime(current_timestamp, ?)";
            this.oq.add(str2);
            return this;
        }

        public ModelUtils bV() {
            return new ModelUtils(this.oq, null);
        }

        public int getCount() {
            Cursor rawQuery;
            String str = "select count(*) from (" + this.oo + ")";
            String[] strArr = (String[]) this.oq.toArray(ModelUtils.EMPTY_STRING_ARRAY);
            FraudDbAdapter z = FraudDbAdapter.m709z(FraudModule.getContext());
            if (z != null) {
                rawQuery = z.rawQuery(str, strArr);
            } else {
                rawQuery = null;
            }
            int intValue = ModelUtils.m729a(rawQuery, 0, Integer.valueOf(0)).intValue();
            if (rawQuery != null) {
                rawQuery.close();
            }
            return intValue;
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.g */
    public static class ModelUtils {
        private String oo;
        private ArrayList<String> oq;

        private ModelUtils(String str, ArrayList<String> arrayList) {
            this.oq = new ArrayList();
            this.oo = str;
            this.oq.addAll(arrayList);
        }

        public ModelUtils bW() {
            this.oo = "select distinct * from (" + this.oo + ")";
            return this;
        }

        public List<ModelUtils> bQ() {
            Cursor b = ModelUtils.rawQuery(this.oo, (String[]) this.oq.toArray(ModelUtils.EMPTY_STRING_ARRAY));
            List linkedList = new LinkedList();
            if (b != null) {
                while (b.moveToNext()) {
                    linkedList.add(new ModelUtils(b.getString(0), b.getString(1)));
                }
                if (b != null) {
                    b.close();
                }
            }
            return linkedList;
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.b.b.h */
    public static class ModelUtils {
        private String oo;
        private ArrayList<String> oq;

        public ModelUtils() {
            this.oo = "select * from ftoken";
            this.oq = new ArrayList();
        }

        public ModelUtils am(String str) {
            String str2 = HCEClientConstants.TAG_KEY_SEPARATOR + str;
            this.oo = "select * from (" + this.oo + ") where id in (" + ("select token_id from (" + "select distinct token_id, time from ftoken_status_history" + ") where datetime(time/1000, 'unixepoch') < datetime(current_timestamp, ?)") + ")";
            this.oq.add(str2);
            return this;
        }

        public int getCount() {
            Cursor b = ModelUtils.rawQuery("select count(*) from (" + this.oo + ")", (String[]) this.oq.toArray(ModelUtils.EMPTY_STRING_ARRAY));
            if (b == null) {
                return 0;
            }
            int intValue = ModelUtils.m729a(b, 0, Integer.valueOf(0)).intValue();
            b.close();
            return intValue;
        }
    }

    static {
        EMPTY_STRING_ARRAY = new String[0];
    }

    public static ModelUtils bK() {
        FCardRecord bN = ModelUtils.bN();
        if (bN == null) {
            return null;
        }
        return new ModelUtils(bN.getFirstName(), bN.getLastName());
    }

    public static ModelUtils bL() {
        FCardRecord bN = ModelUtils.bN();
        if (bN == null) {
            return null;
        }
        return new ModelUtils(bN.bE(), bN.getZip());
    }

    public static int m727a(ModelUtils modelUtils, List<ModelUtils> list) {
        String aj = ModelUtils.aj(modelUtils.nM);
        String aj2 = ModelUtils.aj(modelUtils.nN);
        Log.m285d("ModelUtils", "found " + list.size() + " existing names");
        int i = 0;
        for (ModelUtils modelUtils2 : list) {
            int i2;
            String aj3 = ModelUtils.aj(modelUtils2.nM);
            String aj4 = ModelUtils.aj(modelUtils2.nN);
            Log.m285d("ModelUtils", "comparing |" + aj + "| against |" + aj3 + "|");
            if (ModelUtils.m730a(aj, aj3, 2) && ModelUtils.m730a(aj2, aj4, 2)) {
                Log.m285d("ModelUtils", aj + " matches " + aj3);
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        return i;
    }

    public static int m732b(ModelUtils modelUtils, List<ModelUtils> list) {
        String aj = ModelUtils.aj(modelUtils.nM);
        String aj2 = ModelUtils.aj(modelUtils.nN);
        Log.m285d("ModelUtils", "found " + list.size() + " existing names");
        int i = 0;
        for (ModelUtils modelUtils2 : list) {
            int i2;
            String aj3 = ModelUtils.aj(modelUtils2.nM);
            String aj4 = ModelUtils.aj(modelUtils2.nN);
            Log.m285d("ModelUtils", "comparing |" + aj + "| against |" + aj3 + "|");
            if (ModelUtils.m730a(aj, aj3, 2) && ModelUtils.m730a(aj2, aj4, 2)) {
                i2 = i;
            } else {
                Log.m285d("ModelUtils", aj + " contradicts " + aj3);
                i2 = i + 1;
            }
            i = i2;
        }
        return i;
    }

    public static int m726a(ModelUtils modelUtils, List<ModelUtils> list) {
        Log.m285d("ModelUtils", "found " + list.size() + " existing addresses");
        int i = 0;
        for (ModelUtils modelUtils2 : list) {
            int i2;
            Log.m285d("ModelUtils", "comparing |" + modelUtils.street + "| against |" + modelUtils2.street + "|");
            if (ModelUtils.m730a(modelUtils.street, modelUtils2.street, 10) && ModelUtils.m730a(modelUtils.zip, modelUtils2.zip, 10)) {
                Log.m285d("ModelUtils", modelUtils.street + " (and zip) matches " + modelUtils2.street);
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        return i;
    }

    public static int m731b(ModelUtils modelUtils, List<ModelUtils> list) {
        Log.m285d("ModelUtils", "found " + list.size() + " existing addresses");
        int i = 0;
        for (ModelUtils modelUtils2 : list) {
            int i2;
            Log.m285d("ModelUtils", "comparing |" + modelUtils.street + "| against |" + modelUtils2.street + "|");
            if (ModelUtils.m730a(modelUtils.street, modelUtils2.street, 10) && ModelUtils.m730a(modelUtils.zip, modelUtils2.zip, 10)) {
                i2 = i;
            } else {
                Log.m285d("ModelUtils", modelUtils.street + " (or zip) contradicts " + modelUtils2.street);
                i2 = i + 1;
            }
            i = i2;
        }
        return i;
    }

    public static int ag(String str) {
        return new ModelUtils().al(str).getCount();
    }

    public static int ah(String str) {
        return new ModelUtils().ak(str).bT().getCount();
    }

    public static int ai(String str) {
        return new ModelUtils().am(str).getCount();
    }

    public static ModelUtils bM() {
        return new ModelUtils();
    }

    private static Integer m729a(Cursor cursor, int i, Integer num) {
        if (cursor != null && cursor.moveToNext()) {
            return ModelUtils.m734b(cursor, i, num);
        }
        Log.m285d("ModelUtils", "Expected to read an integer where none exists");
        return num;
    }

    private static Integer m734b(Cursor cursor, int i, Integer num) {
        if (cursor == null) {
            Utils.m1274a(new IllegalStateException("Expected to read an integer where none exists"));
        } else {
            try {
                num = Integer.valueOf(cursor.getInt(i));
            } catch (Throwable e) {
                Utils.m1274a(new RuntimeException(e));
            }
        }
        return num;
    }

    private static boolean m730a(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return false;
        }
        if (str.length() > i) {
            str = str.substring(0, i);
        }
        if (str2.length() > i) {
            str2 = str2.substring(0, i);
        }
        return str.equals(str2);
    }

    private static Cursor rawQuery(String str, String[] strArr) {
        FraudDbAdapter z = FraudDbAdapter.m709z(FraudModule.getContext());
        if (z == null) {
            return null;
        }
        return z.rawQuery(str, strArr);
    }

    private static Cursor m728a(String str, String[] strArr) {
        FraudEfsAdapter A = FraudEfsAdapter.m693A(FraudModule.getContext());
        if (A != null) {
            return A.rawQuery(str, strArr);
        }
        return null;
    }

    private static FCardRecord bN() {
        FraudDataCollector x = FraudDataCollector.m718x(FraudModule.getContext());
        if (x != null) {
            FCardRecord bt = x.bt();
            if (bt != null) {
                return bt;
            }
            Utils.m1274a(new NullPointerException("getProvisioningCard() returned null. Make sure this is only called after a provisioning request."));
            return bt;
        }
        Log.m285d("ModelUtils", "cannot get current card");
        return null;
    }

    private static String aj(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }
}
