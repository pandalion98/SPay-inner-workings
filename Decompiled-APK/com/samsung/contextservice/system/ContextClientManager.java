package com.samsung.contextservice.system;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextclient.p027a.PoiListener;
import com.samsung.contextservice.Manager;
import com.samsung.contextservice.exception.InitializationException;
import com.samsung.contextservice.p028a.RCacheDao;
import com.samsung.contextservice.p029b.CSlog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.samsung.contextservice.system.a */
public class ContextClientManager extends Manager {
    private static final Object He;
    private static final Object Hf;
    private static ContextClientManager Hg;
    private static HandlerThread Hm;
    private RCacheDao GK;
    private final ArrayList<String> Hh;
    private final HashMap<String, ArrayList<PoiListener>> Hi;
    private final HashMap<PoiListener, ArrayList<String>> Hj;
    private ArrayList<Poi> Hk;
    private final HashMap<String, PoiListener> Hl;
    private boolean isInitialized;

    static {
        He = new Object();
        Hf = new Object();
        Hg = null;
        Hm = null;
    }

    private ContextClientManager(Context context) {
        super(context, "ContextClientManager");
        this.isInitialized = false;
        this.GK = null;
        this.Hh = new ArrayList();
        this.Hi = new HashMap();
        this.Hj = new HashMap();
        this.Hk = new ArrayList();
        this.Hl = new HashMap();
        CSlog.m1408d("ContextClientManager", "init context client manager");
        if (context == null) {
            throw new InitializationException("context is null");
        } else if (this.isInitialized) {
            CSlog.m1408d("ContextClientManager", "context client manager has been initialized");
        } else {
            this.isInitialized = true;
            this.GK = new RCacheDao(context);
        }
    }

    public static synchronized ContextClientManager aA(Context context) {
        ContextClientManager contextClientManager;
        synchronized (ContextClientManager.class) {
            if (Hg == null) {
                Hg = new ContextClientManager(context);
            }
            contextClientManager = Hg;
        }
        return contextClientManager;
    }

    private static String m1476a(Poi poi) {
        if (poi == null) {
            return null;
        }
        return poi.getName();
    }

    private static boolean m1477a(String str, Poi poi) {
        if (str != null && poi != null) {
            return ContextClientManager.m1475B(str, ContextClientManager.m1476a(poi));
        }
        CSlog.m1408d("ContextClientManager", "poi is null");
        return false;
    }

    private static boolean m1475B(String str, String str2) {
        if (str == null || str2 == null || !TextUtils.equals(str, str2)) {
            return false;
        }
        return true;
    }

    void m1479a(int i, String str, ArrayList<Poi> arrayList, PoiListener poiListener) {
        CSlog.m1408d("ContextClientManager", "registerPois");
        if (i < 0 || str == null || arrayList == null || poiListener == null) {
            CSlog.m1409e("ContextClientManager", "cannot register POIs");
            return;
        }
        synchronized (He) {
            String valueOf = String.valueOf(i);
            if (this.Hl.containsKey(valueOf)) {
                CSlog.m1409e("ContextClientManager", "pid " + valueOf + " has registered poi listener");
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ArrayList arrayList3;
                String a = ContextClientManager.m1476a((Poi) it.next());
                if (this.Hi.containsKey(a)) {
                    arrayList3 = (ArrayList) this.Hi.get(a);
                } else {
                    arrayList3 = new ArrayList();
                    this.Hi.put(a, arrayList3);
                }
                arrayList3.add(poiListener);
                arrayList2.add(a);
                this.Hh.add(a);
            }
            this.Hj.put(poiListener, arrayList2);
            this.Hl.put(valueOf, poiListener);
        }
    }

    void m1478X(int i) {
        CSlog.m1408d("ContextClientManager", "unregisterPois");
        String valueOf = String.valueOf(i);
        synchronized (He) {
            if (this.Hl.containsKey(valueOf)) {
                PoiListener poiListener = (PoiListener) this.Hl.remove(valueOf);
                if (poiListener != null) {
                    Iterator it = ((ArrayList) this.Hj.remove(poiListener)).iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        ArrayList arrayList = (ArrayList) this.Hi.get(str);
                        if (arrayList != null) {
                            arrayList.remove(poiListener);
                            if (arrayList.size() <= 0) {
                                this.Hi.remove(str);
                                this.Hh.remove(str);
                            }
                        }
                    }
                }
                return;
            }
            CSlog.m1409e("ContextClientManager", "pid " + valueOf + " did not register poi listener");
        }
    }

    void m1481a(int i, List<Poi> list) {
        CSlog.m1408d("ContextClientManager", "addPoi");
        String valueOf = String.valueOf(i);
        synchronized (He) {
            if (this.Hl.containsKey(valueOf)) {
                PoiListener poiListener = (PoiListener) this.Hl.get(valueOf);
                ArrayList arrayList = new ArrayList();
                for (Poi a : list) {
                    ArrayList arrayList2;
                    String a2 = ContextClientManager.m1476a(a);
                    if (this.Hi.containsKey(a2)) {
                        arrayList2 = (ArrayList) this.Hi.get(a2);
                    } else {
                        arrayList2 = new ArrayList();
                        this.Hi.put(a2, arrayList2);
                    }
                    arrayList2.add(poiListener);
                    arrayList.add(a2);
                    this.Hh.add(a2);
                }
                this.Hj.put(poiListener, arrayList);
                return;
            }
            CSlog.m1409e("ContextClientManager", "no listener available");
        }
    }

    void m1483b(int i, List<Poi> list) {
        CSlog.m1408d("ContextClientManager", "removePoi");
    }

    void m1480a(int i, ArrayList<Poi> arrayList) {
        CSlog.m1408d("ContextClientManager", "addPersistentPoi for uid " + i);
        long currentTimeMillis = System.currentTimeMillis();
        this.GK.m1394a("DEALS_AND_OFFERS", "DEALS_AND_OFFERS", currentTimeMillis, currentTimeMillis - 813934592, (ArrayList) arrayList, false);
    }

    void m1482b(int i, ArrayList<Poi> arrayList) {
        CSlog.m1408d("ContextClientManager", "removePersistentPoi for uid " + i);
        this.GK.m1396a(arrayList);
    }

    public void m1484c(Message message) {
        CSlog.m1408d("ContextClientManager", "handle sNotifier thread msg");
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        synchronized (Hf) {
            Iterator it = this.Hh.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Iterator it2 = this.Hk.iterator();
                while (it2.hasNext()) {
                    Poi poi = (Poi) it2.next();
                    if (poi == null) {
                        CSlog.m1408d("ContextClientManager", "poi is null");
                    } else if (ContextClientManager.m1477a(str, poi)) {
                        hashMap.put(str, poi);
                        arrayList.add(str);
                    }
                }
            }
        }
        Iterator it3 = arrayList.iterator();
        while (it3.hasNext()) {
            Poi poi2 = (Poi) hashMap.get((String) it3.next());
            ContextClientManager.m1476a(poi2);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(this.Hj.keySet());
            if (arrayList2 != null) {
                CSlog.m1408d("ContextClientManager", "listener is not null");
                Iterator it4 = arrayList2.iterator();
                while (it4.hasNext()) {
                    try {
                        ((PoiListener) it4.next()).m1386a(poi2, poi2.getLocation());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                CSlog.m1408d("ContextClientManager", "listener is null");
            }
        }
    }
}
