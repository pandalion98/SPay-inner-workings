/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.database.Cursor
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.List
 */
package com.samsung.android.spayfw.fraud.b;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class b {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /*
     * Enabled aggressive block sorting
     */
    public static int a(b b2, List<b> list) {
        com.samsung.android.spayfw.b.c.d("ModelUtils", "found " + list.size() + " existing addresses");
        Iterator iterator = list.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            int n3;
            b b3 = (b)iterator.next();
            com.samsung.android.spayfw.b.c.d("ModelUtils", "comparing |" + b2.street + "| against |" + b3.street + "|");
            if (b.a(b2.street, b3.street, 10) && b.a(b2.zip, b3.zip, 10)) {
                com.samsung.android.spayfw.b.c.d("ModelUtils", b2.street + " (and zip) matches " + b3.street);
                n3 = n2 + 1;
            } else {
                n3 = n2;
            }
            n2 = n3;
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int a(c c2, List<c> list) {
        String string = b.aj(c2.nM);
        String string2 = b.aj(c2.nN);
        com.samsung.android.spayfw.b.c.d("ModelUtils", "found " + list.size() + " existing names");
        Iterator iterator = list.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            int n3;
            c c3 = (c)iterator.next();
            String string3 = b.aj(c3.nM);
            String string4 = b.aj(c3.nN);
            com.samsung.android.spayfw.b.c.d("ModelUtils", "comparing |" + string + "| against |" + string3 + "|");
            if (b.a(string, string3, 2) && b.a(string2, string4, 2)) {
                com.samsung.android.spayfw.b.c.d("ModelUtils", string + " matches " + string3);
                n3 = n2 + 1;
            } else {
                n3 = n2;
            }
            n2 = n3;
        }
        return n2;
    }

    private static Cursor a(String string, String[] arrstring) {
        com.samsung.android.spayfw.fraud.a.a.b b2 = com.samsung.android.spayfw.fraud.a.a.b.A(com.samsung.android.spayfw.fraud.e.getContext());
        Cursor cursor = null;
        if (b2 != null) {
            cursor = b2.rawQuery(string, arrstring);
        }
        return cursor;
    }

    private static Integer a(Cursor cursor, int n2, Integer n3) {
        if (cursor == null || !cursor.moveToNext()) {
            com.samsung.android.spayfw.b.c.d("ModelUtils", "Expected to read an integer where none exists");
            return n3;
        }
        return b.b(cursor, n2, n3);
    }

    private static boolean a(String string, String string2, int n2) {
        if (string == null || string2 == null) {
            return false;
        }
        if (string.length() > n2) {
            string = string.substring(0, n2);
        }
        if (string2.length() > n2) {
            string2 = string2.substring(0, n2);
        }
        return string.equals((Object)string2);
    }

    public static int ag(String string) {
        return new f().al(string).getCount();
    }

    public static int ah(String string) {
        return new e().ak(string).bT().getCount();
    }

    public static int ai(String string) {
        return new h().am(string).getCount();
    }

    private static String aj(String string) {
        if (string == null) {
            return null;
        }
        return string.toLowerCase();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int b(b b2, List<b> list) {
        com.samsung.android.spayfw.b.c.d("ModelUtils", "found " + list.size() + " existing addresses");
        Iterator iterator = list.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            int n3;
            b b3 = (b)iterator.next();
            com.samsung.android.spayfw.b.c.d("ModelUtils", "comparing |" + b2.street + "| against |" + b3.street + "|");
            if (!b.a(b2.street, b3.street, 10) || !b.a(b2.zip, b3.zip, 10)) {
                com.samsung.android.spayfw.b.c.d("ModelUtils", b2.street + " (or zip) contradicts " + b3.street);
                n3 = n2 + 1;
            } else {
                n3 = n2;
            }
            n2 = n3;
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int b(c c2, List<c> list) {
        String string = b.aj(c2.nM);
        String string2 = b.aj(c2.nN);
        com.samsung.android.spayfw.b.c.d("ModelUtils", "found " + list.size() + " existing names");
        Iterator iterator = list.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            int n3;
            c c3 = (c)iterator.next();
            String string3 = b.aj(c3.nM);
            String string4 = b.aj(c3.nN);
            com.samsung.android.spayfw.b.c.d("ModelUtils", "comparing |" + string + "| against |" + string3 + "|");
            if (!b.a(string, string3, 2) || !b.a(string2, string4, 2)) {
                com.samsung.android.spayfw.b.c.d("ModelUtils", string + " contradicts " + string3);
                n3 = n2 + 1;
            } else {
                n3 = n2;
            }
            n2 = n3;
        }
        return n2;
    }

    private static Integer b(Cursor cursor, int n2, Integer n3) {
        if (cursor == null) {
            com.samsung.android.spayfw.utils.h.a((RuntimeException)new IllegalStateException("Expected to read an integer where none exists"));
            return n3;
        }
        try {
            Integer n4 = cursor.getInt(n2);
            return n4;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.utils.h.a(new RuntimeException((Throwable)exception));
            return n3;
        }
    }

    public static c bK() {
        com.samsung.android.spayfw.fraud.a.b b2 = b.bN();
        if (b2 == null) {
            return null;
        }
        return new c(b2.getFirstName(), b2.getLastName());
    }

    public static b bL() {
        com.samsung.android.spayfw.fraud.a.b b2 = b.bN();
        if (b2 == null) {
            return null;
        }
        return new b(b2.bE(), b2.getZip());
    }

    public static f bM() {
        return new f();
    }

    private static com.samsung.android.spayfw.fraud.a.b bN() {
        com.samsung.android.spayfw.fraud.a a2 = com.samsung.android.spayfw.fraud.a.x(com.samsung.android.spayfw.fraud.e.getContext());
        if (a2 != null) {
            com.samsung.android.spayfw.fraud.a.b b2 = a2.bt();
            if (b2 == null) {
                com.samsung.android.spayfw.utils.h.a((RuntimeException)new NullPointerException("getProvisioningCard() returned null. Make sure this is only called after a provisioning request."));
            }
            return b2;
        }
        com.samsung.android.spayfw.b.c.d("ModelUtils", "cannot get current card");
        return null;
    }

    private static Cursor rawQuery(String string, String[] arrstring) {
        com.samsung.android.spayfw.fraud.a.g g2 = com.samsung.android.spayfw.fraud.a.g.z(com.samsung.android.spayfw.fraud.e.getContext());
        if (g2 == null) {
            return null;
        }
        return g2.rawQuery(string, arrstring);
    }

    public static class a {
        private String oo;
        private ArrayList<String> oq = new ArrayList();

        private a(String string, ArrayList<String> arrayList) {
            this.oo = string;
            this.oq.addAll(arrayList);
        }

        public a bP() {
            this.oo = "select distinct * from (" + this.oo + ")";
            return this;
        }

        public List<b> bQ() {
            String[] arrstring = (String[])this.oq.toArray((Object[])EMPTY_STRING_ARRAY);
            Cursor cursor = b.rawQuery(this.oo, arrstring);
            LinkedList linkedList = new LinkedList();
            if (cursor == null) {
                return linkedList;
            }
            while (cursor.moveToNext()) {
                String string = cursor.getString(0);
                String string2 = cursor.getString(1);
                if (string == null || string2 == null) continue;
                linkedList.add((Object)new b(string, string2));
            }
            cursor.close();
            return linkedList;
        }
    }

    public static class b {
        public final String street;
        public final String zip;

        public b(String string, String string2) {
            this.street = string;
            this.zip = string2;
        }
    }

    public static class c {
        public final String nM;
        public final String nN;

        public c(String string, String string2) {
            this.nM = string;
            this.nN = string2;
        }
    }

    public static class d {
        private String oo = "select * from fcard";
        private ArrayList<String> oq = new ArrayList();

        private d(String string, ArrayList<String> arrayList) {
            this.oo = string;
            this.oq.addAll(arrayList);
        }

        public g bR() {
            return new g("select first_name, last_name from (" + this.oo + ")", this.oq);
        }

        public a bS() {
            return new a("select avsaddr, avszip from (" + this.oo + ")", this.oq);
        }
    }

    public static class e {
        private String oo = "select * from fdevice_info";
        private ArrayList<String> oq = new ArrayList();

        public e ak(String string) {
            String string2 = "-" + string;
            this.oo = "select * from (" + this.oo + ") where datetime(time/1000, 'unixepoch') > datetime(current_timestamp, ?)";
            this.oq.add((Object)string2);
            return this;
        }

        public e bT() {
            this.oo = "select * from (" + this.oo + ") where " + "reason" + " in " + "(\"" + "app_reset" + "\", \"" + "factory_reset" + "\")";
            return this;
        }

        public int getCount() {
            Cursor cursor = b.a("select count(*) from (" + this.oo + ")", (String[])this.oq.toArray((Object[])EMPTY_STRING_ARRAY));
            int n2 = b.a(cursor, 0, 0);
            if (cursor != null) {
                cursor.close();
            }
            return n2;
        }
    }

    public static class f {
        private String oo = "select * from fcard";
        private ArrayList<String> oq = new ArrayList();

        public f al(String string) {
            String string2 = "-" + string;
            this.oo = "select * from (" + this.oo + ") where datetime(attempt_time/1000, 'unixepoch') > datetime(current_timestamp, ?)";
            this.oq.add((Object)string2);
            return this;
        }

        public f bU() {
            this.oo = "select * from (" + this.oo + ") where result = ?";
            this.oq.add((Object)String.valueOf((int)0));
            return this;
        }

        public d bV() {
            return new d(this.oo, this.oq);
        }

        /*
         * Enabled aggressive block sorting
         */
        public int getCount() {
            String string = "select count(*) from (" + this.oo + ")";
            String[] arrstring = (String[])this.oq.toArray((Object[])EMPTY_STRING_ARRAY);
            com.samsung.android.spayfw.fraud.a.g g2 = com.samsung.android.spayfw.fraud.a.g.z(com.samsung.android.spayfw.fraud.e.getContext());
            Cursor cursor = g2 != null ? g2.rawQuery(string, arrstring) : null;
            int n2 = b.a(cursor, 0, 0);
            if (cursor != null) {
                cursor.close();
            }
            return n2;
        }
    }

    public static class g {
        private String oo;
        private ArrayList<String> oq = new ArrayList();

        private g(String string, ArrayList<String> arrayList) {
            this.oo = string;
            this.oq.addAll(arrayList);
        }

        /*
         * Enabled aggressive block sorting
         */
        public List<c> bQ() {
            LinkedList linkedList;
            Cursor cursor;
            block4 : {
                block3 : {
                    String[] arrstring = (String[])this.oq.toArray((Object[])EMPTY_STRING_ARRAY);
                    cursor = b.rawQuery(this.oo, arrstring);
                    linkedList = new LinkedList();
                    if (cursor == null) break block3;
                    while (cursor.moveToNext()) {
                        linkedList.add((Object)new c(cursor.getString(0), cursor.getString(1)));
                    }
                    if (cursor != null) break block4;
                }
                return linkedList;
            }
            cursor.close();
            return linkedList;
        }

        public g bW() {
            this.oo = "select distinct * from (" + this.oo + ")";
            return this;
        }
    }

    public static class h {
        private String oo = "select * from ftoken";
        private ArrayList<String> oq = new ArrayList();

        public h am(String string) {
            String string2 = "-" + string;
            String string3 = "select token_id from (" + "select distinct token_id, time from ftoken_status_history" + ") where datetime(time/1000, 'unixepoch') < datetime(current_timestamp, ?)";
            this.oo = "select * from (" + this.oo + ") where id in (" + string3 + ")";
            this.oq.add((Object)string2);
            return this;
        }

        public int getCount() {
            Cursor cursor = b.rawQuery("select count(*) from (" + this.oo + ")", (String[])this.oq.toArray((Object[])EMPTY_STRING_ARRAY));
            if (cursor != null) {
                int n2 = b.a(cursor, 0, 0);
                cursor.close();
                return n2;
            }
            return 0;
        }
    }

}

