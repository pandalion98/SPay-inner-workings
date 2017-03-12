package com.samsung.contextservice.system;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.samsung.contextclient.IContextClient.IContextClient;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextclient.p027a.IPoiListener;
import com.samsung.contextclient.p027a.PoiListener;
import com.samsung.contextservice.p029b.CSlog;
import java.util.ArrayList;
import java.util.List;

public class ContextService extends Service {
    private ContextClientManager Hr;
    private final IContextClient Hs;

    /* renamed from: com.samsung.contextservice.system.ContextService.1 */
    class C06161 extends IContextClient {
        final /* synthetic */ ContextService Ht;

        C06161(ContextService contextService) {
            this.Ht = contextService;
        }

        private String m1460e(int i, int i2) {
            return String.valueOf(i) + String.valueOf(i2);
        }

        public void m1463a(List<Poi> list, IPoiListener iPoiListener) {
            CSlog.m1408d("CtxService", "registerPoi()");
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            String e = m1460e(callingUid, callingPid);
            CSlog.m1408d("CtxService", "callingpid=" + callingPid + ", callingUID=" + callingUid);
            if (this.Ht.Hr != null) {
                this.Ht.Hr.m1479a(callingPid, e, (ArrayList) list, (PoiListener) iPoiListener);
            } else {
                CSlog.m1409e("CtxService", "Cannot register new poi");
            }
        }

        public void m1464d(List<Poi> list) {
            CSlog.m1408d("CtxService", "addPoi()");
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            m1460e(callingUid, callingPid);
            CSlog.m1408d("CtxService", "callingpid=" + callingPid + ", callingUID=" + callingUid);
            if (this.Ht.Hr != null) {
                this.Ht.Hr.m1481a(callingPid, (List) list);
            } else {
                CSlog.m1409e("CtxService", "Cannot register new poi");
            }
        }

        public void m1465e(List<Poi> list) {
            CSlog.m1408d("CtxService", "removePoi()");
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            m1460e(callingUid, callingPid);
            CSlog.m1408d("CtxService", "callingpid=" + callingPid + ", callingUID=" + callingUid);
            if (this.Ht.Hr != null) {
                this.Ht.Hr.m1483b(callingUid, (List) list);
            } else {
                CSlog.m1409e("CtxService", "Cannot register new poi");
            }
        }

        public void m1466f(List<Poi> list) {
            CSlog.m1408d("CtxService", "addPersistentPoi()");
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            m1460e(callingUid, callingPid);
            CSlog.m1408d("CtxService", "callingpid=" + callingPid + ", callingUID=" + callingUid);
            if (this.Ht.Hr != null) {
                this.Ht.Hr.m1480a(callingUid, (ArrayList) list);
            } else {
                CSlog.m1409e("CtxService", "Cannot register new poi");
            }
        }

        public void m1467g(List<Poi> list) {
            CSlog.m1408d("CtxService", "removePersistentPoi()");
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            m1460e(callingUid, callingPid);
            CSlog.m1408d("CtxService", "callingpid=" + callingPid + ", callingUID=" + callingUid);
            if (this.Ht.Hr != null) {
                this.Ht.Hr.m1482b(callingUid, (ArrayList) list);
            } else {
                CSlog.m1409e("CtxService", "Cannot register new poi");
            }
        }

        public void gs() {
            CSlog.m1408d("CtxService", "unregisterPoi()");
            int callingPid = Binder.getCallingPid();
            if (this.Ht.Hr != null) {
                this.Ht.Hr.m1478X(callingPid);
            } else {
                CSlog.m1409e("CtxService", "Cannot register new poi");
            }
        }

        public void m1462a(String str, int i, String str2) {
            CSlog.m1408d("CtxService", "sendContextData()");
        }

        public List<Poi> m1461a(Location location, double d) {
            List<Poi> list = null;
            CSlog.m1408d("CtxService", "findPoiByLocation()");
            if (location != null) {
                try {
                    VerdictManager aG = VerdictManager.aG(this.Ht.getApplicationContext());
                    if (aG != null) {
                        list = aG.m1495a(location.getLatitude(), location.getLongitude(), d, Poi.RADIUS_SMALL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    public ContextService() {
        this.Hr = null;
        this.Hs = new C06161(this);
    }

    public void onCreate() {
        super.onCreate();
        CSlog.m1410i("CtxService", "context service is started");
        try {
            ManagerHub.aE(getApplicationContext());
            this.Hr = (ContextClientManager) ManagerHub.m1472Z(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            ManagerHub.onDestroy();
            sendBroadcast(new Intent("YouWillNeverKillMe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        CSlog.m1408d("CtxService", "onStartCommand");
        return 1;
    }

    public IBinder onBind(Intent intent) {
        CSlog.m1408d("CtxService", "onBinder()");
        CSlog.m1408d("CtxService", intent.getStringExtra("myname") + " is connected");
        return this.Hs;
    }
}
