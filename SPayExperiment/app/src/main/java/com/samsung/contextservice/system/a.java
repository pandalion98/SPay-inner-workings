/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.HandlerThread
 *  android.os.Message
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package com.samsung.contextservice.system;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.samsung.contextclient.a.b;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.a.d;
import com.samsung.contextservice.exception.InitializationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class a
extends com.samsung.contextservice.a {
    private static final Object He = new Object();
    private static final Object Hf = new Object();
    private static a Hg = null;
    private static HandlerThread Hm = null;
    private d GK = null;
    private final ArrayList<String> Hh = new ArrayList();
    private final HashMap<String, ArrayList<b>> Hi = new HashMap();
    private final HashMap<b, ArrayList<String>> Hj = new HashMap();
    private ArrayList<Poi> Hk = new ArrayList();
    private final HashMap<String, b> Hl = new HashMap();
    private boolean isInitialized = false;

    private a(Context context) {
        super(context, "ContextClientManager");
        com.samsung.contextservice.b.b.d("ContextClientManager", "init context client manager");
        if (context == null) {
            throw new InitializationException("context is null");
        }
        if (!this.isInitialized) {
            this.isInitialized = true;
            this.GK = new d(context);
            return;
        }
        com.samsung.contextservice.b.b.d("ContextClientManager", "context client manager has been initialized");
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean B(String string, String string2) {
        return string != null && string2 != null && TextUtils.equals((CharSequence)string, (CharSequence)string2);
    }

    private static String a(Poi poi) {
        if (poi == null) {
            return null;
        }
        return poi.getName();
    }

    private static boolean a(String string, Poi poi) {
        if (string == null || poi == null) {
            com.samsung.contextservice.b.b.d("ContextClientManager", "poi is null");
            return false;
        }
        return a.B(string, a.a(poi));
    }

    public static a aA(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (Hg == null) {
                Hg = new a(context);
            }
            a a2 = Hg;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void X(int n2) {
        Object object;
        com.samsung.contextservice.b.b.d("ContextClientManager", "unregisterPois");
        String string = String.valueOf((int)n2);
        Object object2 = object = He;
        synchronized (object2) {
            if (!this.Hl.containsKey((Object)string)) {
                com.samsung.contextservice.b.b.e("ContextClientManager", "pid " + string + " did not register poi listener");
                return;
            }
            b b2 = (b)this.Hl.remove((Object)string);
            if (b2 != null) {
                for (String string2 : (ArrayList)this.Hj.remove((Object)b2)) {
                    ArrayList arrayList = (ArrayList)this.Hi.get((Object)string2);
                    if (arrayList == null) continue;
                    arrayList.remove((Object)b2);
                    if (arrayList.size() > 0) continue;
                    this.Hi.remove((Object)string2);
                    this.Hh.remove((Object)string2);
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void a(int n2, String string, ArrayList<Poi> arrayList, b b2) {
        Object object;
        com.samsung.contextservice.b.b.d("ContextClientManager", "registerPois");
        if (n2 < 0 || string == null || arrayList == null || b2 == null) {
            com.samsung.contextservice.b.b.e("ContextClientManager", "cannot register POIs");
            return;
        }
        Object object2 = object = He;
        synchronized (object2) {
            String string2 = String.valueOf((int)n2);
            if (this.Hl.containsKey((Object)string2)) {
                com.samsung.contextservice.b.b.e("ContextClientManager", "pid " + string2 + " has registered poi listener");
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            Iterator iterator = arrayList.iterator();
            do {
                ArrayList arrayList3;
                if (!iterator.hasNext()) {
                    this.Hj.put((Object)b2, (Object)arrayList2);
                    this.Hl.put((Object)string2, (Object)b2);
                    return;
                }
                String string3 = a.a((Poi)iterator.next());
                if (this.Hi.containsKey((Object)string3)) {
                    arrayList3 = (ArrayList)this.Hi.get((Object)string3);
                } else {
                    arrayList3 = new ArrayList();
                    this.Hi.put((Object)string3, (Object)arrayList3);
                }
                arrayList3.add((Object)b2);
                arrayList2.add((Object)string3);
                this.Hh.add((Object)string3);
            } while (true);
        }
    }

    void a(int n2, ArrayList<Poi> arrayList) {
        com.samsung.contextservice.b.b.d("ContextClientManager", "addPersistentPoi for uid " + n2);
        long l2 = System.currentTimeMillis();
        long l3 = l2 - 813934592L;
        this.GK.a("DEALS_AND_OFFERS", "DEALS_AND_OFFERS", l2, l3, arrayList, false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void a(int n2, List<Poi> list) {
        Object object;
        com.samsung.contextservice.b.b.d("ContextClientManager", "addPoi");
        String string = String.valueOf((int)n2);
        Object object2 = object = He;
        synchronized (object2) {
            if (!this.Hl.containsKey((Object)string)) {
                com.samsung.contextservice.b.b.e("ContextClientManager", "no listener available");
                return;
            }
            b b2 = (b)this.Hl.get((Object)string);
            ArrayList arrayList = new ArrayList();
            Iterator iterator = list.iterator();
            do {
                ArrayList arrayList2;
                if (!iterator.hasNext()) {
                    this.Hj.put((Object)b2, (Object)arrayList);
                    return;
                }
                String string2 = a.a((Poi)iterator.next());
                if (this.Hi.containsKey((Object)string2)) {
                    arrayList2 = (ArrayList)this.Hi.get((Object)string2);
                } else {
                    arrayList2 = new ArrayList();
                    this.Hi.put((Object)string2, (Object)arrayList2);
                }
                arrayList2.add((Object)b2);
                arrayList.add((Object)string2);
                this.Hh.add((Object)string2);
            } while (true);
        }
    }

    void b(int n2, ArrayList<Poi> arrayList) {
        com.samsung.contextservice.b.b.d("ContextClientManager", "removePersistentPoi for uid " + n2);
        this.GK.a(arrayList);
    }

    void b(int n2, List<Poi> list) {
        com.samsung.contextservice.b.b.d("ContextClientManager", "removePoi");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void c(Message var1_1) {
        com.samsung.contextservice.b.b.d("ContextClientManager", "handle sNotifier thread msg");
        var2_2 = new ArrayList();
        var3_3 = new HashMap();
        var20_5 = var4_4 = a.Hf;
        // MONITORENTER : var20_5
        var6_6 = this.Hh.iterator();
        block5 : do {
            if (!var6_6.hasNext()) {
                // MONITOREXIT : var20_5
                var7_10 = var2_2.iterator();
                break;
            }
            var15_9 = (String)var6_6.next();
            var16_7 = this.Hk.iterator();
            do {
                if (!var16_7.hasNext()) continue block5;
                var17_8 = (Poi)var16_7.next();
                if (var17_8 == null) {
                    com.samsung.contextservice.b.b.d("ContextClientManager", "poi is null");
                    continue;
                }
                if (!a.a(var15_9, var17_8)) continue;
                var3_3.put((Object)var15_9, (Object)var17_8);
                var2_2.add((Object)var15_9);
            } while (true);
            break;
        } while (true);
        do {
            if (var7_10.hasNext() == false) return;
            var8_11 = (Poi)var3_3.get((Object)((String)var7_10.next()));
            a.a(var8_11);
            var10_12 = new ArrayList();
            var10_12.addAll((Collection)this.Hj.keySet());
            if (var10_12 == null) {
                com.samsung.contextservice.b.b.d("ContextClientManager", "listener is null");
                continue;
            }
            com.samsung.contextservice.b.b.d("ContextClientManager", "listener is not null");
            var12_13 = var10_12.iterator();
            do {
                if (!var12_13.hasNext()) ** break;
                var13_14 = (b)var12_13.next();
                try {
                    var13_14.a(var8_11, var8_11.getLocation());
                }
                catch (Exception var14_15) {
                    var14_15.printStackTrace();
                    continue;
                }
                break;
            } while (true);
            break;
        } while (true);
    }
}

