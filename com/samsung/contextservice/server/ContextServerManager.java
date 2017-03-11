package com.samsung.contextservice.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.Manager;
import com.samsung.contextservice.p028a.PolicyDao;
import com.samsung.contextservice.p028a.RCacheDao;
import com.samsung.contextservice.p029b.AppInterface;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.p029b.Utils;
import com.samsung.contextservice.server.models.Cache;
import com.samsung.contextservice.server.models.CacheResponseData;
import com.samsung.contextservice.server.models.DeviceInfo;
import com.samsung.contextservice.server.models.PolicyResponseData;
import com.samsung.contextservice.server.models.TriggerRequestData;
import com.samsung.contextservice.server.models.TriggerResponseData;
import com.samsung.contextservice.server.models.WalletInfo;
import java.util.ArrayList;
import java.util.Iterator;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.contextservice.server.e */
public class ContextServerManager extends Manager {
    private static ContextServerManager GH;
    private ContextRequesterClient GI;
    private CacheFileDownloaderClient GJ;
    private RCacheDao GK;
    private PolicyDao GL;
    private PolicyConnPolicy GM;
    private CacheConnPolicy GN;

    /* renamed from: com.samsung.contextservice.server.e.a */
    private class ContextServerManager extends C0413a<Response<CacheResponseData>, CacheRequest> {
        private ServerListener GE;
        final /* synthetic */ ContextServerManager GO;

        public ContextServerManager(ContextServerManager contextServerManager, ServerListener serverListener) {
            this.GO = contextServerManager;
            this.GE = serverListener;
        }

        public ServerListener gv() {
            return this.GE;
        }

        public void m1435a(int i, Response<CacheResponseData> response) {
            CSlog.m1408d("CtxSrvManager", "CacheRequestCallback:onRequestComplete():  " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    this.GO.ay(this.GO.mContext);
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (!(response == null || response.getResult() == null)) {
                        CacheResponseData cacheResponseData = (CacheResponseData) response.getResult();
                        CSlog.m1408d("CtxSrvManager", cacheResponseData.toString());
                        if (cacheResponseData != null) {
                            try {
                                ArrayList caches = cacheResponseData.getCaches();
                                if (caches != null) {
                                    Iterator it = caches.iterator();
                                    while (it.hasNext()) {
                                        Cache cache = (Cache) it.next();
                                        if (cache.getGeohash() == null) {
                                            CSlog.m1409e("CtxSrvManager", "Geohash is missing");
                                        } else if (cache.getHref() == null || cache.getContentHash() == null) {
                                            this.GO.GK.m1394a(cache.getId(), cache.getGeohash(), cache.getUpdatedAt(), cache.getExpireAt(), null, true);
                                            CSlog.m1408d("CtxSrvManager", "Update empty poi cache: update at " + cache.getUpdatedAt() + ", expire at " + cache.getExpireAt());
                                        } else {
                                            CSlog.m1408d("CtxSrvManager", "The available cache is " + cache.toString());
                                            long[] bJ = this.GO.GK.bJ(cache.getGeohash());
                                            long updatedAt = cache.getUpdatedAt();
                                            long expireAt = cache.getExpireAt();
                                            if (bJ == null) {
                                                this.GO.GJ.m1430a(cache, gv());
                                                CSlog.m1408d("CtxSrvManager", "No cache, fetching cache from s3 server, new update at " + updatedAt + " and new expire at " + expireAt);
                                            } else if (bJ.length != 2) {
                                                this.GO.GJ.m1430a(cache, gv());
                                                CSlog.m1408d("CtxSrvManager", "Cannot identify cache, fetching cache from s3 server, new update at " + updatedAt + " and new expire at " + expireAt);
                                            } else if (bJ[0] < updatedAt) {
                                                this.GO.GJ.m1430a(cache, gv());
                                                CSlog.m1408d("CtxSrvManager", "Fetching latest cache from s3 serve, prev update at " + bJ[0] + " and new update at " + updatedAt + "; prev expiry at " + bJ[1] + " and new update at " + expireAt);
                                            } else {
                                                this.GO.GK.m1394a(cache.getId(), cache.getGeohash(), cache.getUpdatedAt(), cache.getExpireAt(), null, false);
                                                CSlog.m1408d("CtxSrvManager", "No cache update is needed, only update expiry date, prev update at " + bJ[0] + " and new update at " + updatedAt + "; prev expiry at " + bJ[1] + " and new update at " + expireAt);
                                            }
                                        }
                                    }
                                    break;
                                }
                            } catch (Throwable e) {
                                CSlog.m1406c("CtxSrvManager", "Cannot download cache", e);
                                break;
                            }
                        }
                    }
                    break;
                case 400:
                    if (!(response == null || response.fa() == null)) {
                        ErrorResponseData fa = response.fa();
                        if (fa != null) {
                            CSlog.m1408d("CtxSrvManager", fa.getMessage());
                            break;
                        }
                    }
                    break;
            }
            if (i != 0) {
                this.GO.GN.m1420C(System.currentTimeMillis());
            } else {
                CSlog.m1408d("CtxSrvManager", "Cache, Unable to connect");
            }
        }
    }

    /* renamed from: com.samsung.contextservice.server.e.b */
    private class ContextServerManager extends C0413a<Response<PolicyResponseData>, PolicyRequest> {
        private ServerListener GE;
        final /* synthetic */ ContextServerManager GO;

        public ContextServerManager(ContextServerManager contextServerManager, ServerListener serverListener) {
            this.GO = contextServerManager;
            this.GE = serverListener;
        }

        public void m1437a(int i, Response<PolicyResponseData> response) {
            CSlog.m1408d("CtxSrvManager", "PolicyRequestCallback:onRequestComplete():  " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    this.GO.ay(this.GO.mContext);
                    if (this.GE != null) {
                        this.GE.m1418W(-2);
                        break;
                    }
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (!(response == null || response.getResult() == null)) {
                        PolicyResponseData policyResponseData = (PolicyResponseData) response.getResult();
                        CSlog.m1408d("CtxSrvManager", policyResponseData.toString());
                        long a = this.GO.GL.m1389a(policyResponseData);
                        if (a >= 0) {
                            CSlog.m1408d("CtxSrvManager", "add policy " + policyResponseData.getId() + " at row " + a);
                        } else {
                            CSlog.m1408d("CtxSrvManager", "cannot add policy to db");
                        }
                        if (this.GE != null) {
                            this.GE.onSuccess();
                            break;
                        }
                    }
                    break;
            }
            if (i != 0) {
                this.GO.GM.m1420C(System.currentTimeMillis());
            }
        }
    }

    /* renamed from: com.samsung.contextservice.server.e.c */
    private class ContextServerManager extends C0413a<Response<TriggerResponseData>, TriggerRequest> {
        private ServerListener GE;
        final /* synthetic */ ContextServerManager GO;

        private ContextServerManager(ContextServerManager contextServerManager) {
            this.GO = contextServerManager;
        }

        public ContextServerManager(ContextServerManager contextServerManager, ServerListener serverListener) {
            this.GO = contextServerManager;
            this.GE = serverListener;
        }

        public void m1439a(int i, Response<TriggerResponseData> response) {
            CSlog.m1408d("CtxSrvManager", "TriggerRequestCallback:onRequestComplete():  " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    this.GO.ay(this.GO.mContext);
                    if (this.GE != null) {
                        this.GE.m1418W(-2);
                    }
                case ECCurve.COORD_AFFINE /*0*/:
                    if (this.GE != null) {
                        this.GE.m1418W(0);
                    }
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                case 202:
                case 204:
                    if (this.GE != null) {
                        this.GE.onSuccess();
                    }
                case 400:
                case 401:
                case 404:
                case 503:
                    if (this.GE != null) {
                        this.GE.gD();
                    }
                default:
                    if (this.GE != null) {
                        this.GE.gD();
                    }
            }
        }
    }

    static {
        GH = null;
    }

    public static synchronized ContextServerManager ax(Context context) {
        ContextServerManager contextServerManager;
        synchronized (ContextServerManager.class) {
            if (GH == null) {
                if (context == null) {
                    throw new Exception("context is null");
                }
                GH = new ContextServerManager(context);
            }
            contextServerManager = GH;
        }
        return contextServerManager;
    }

    private ContextServerManager(Context context) {
        super(context, "ContextServerManager");
        this.GJ = null;
        this.mContext = context;
        this.GI = ContextRequesterClient.aw(this.mContext);
        this.GJ = new CacheFileDownloaderClient(this.mContext);
        this.GL = new PolicyDao(this.mContext);
        this.GK = new RCacheDao(this.mContext);
        this.GM = new PolicyConnPolicy(this.mContext, "PolicyPolicy", 86400000, 1);
        this.GN = new CacheConnPolicy(this.mContext, "CachePolicy", 3600000, 2);
    }

    public void m1455c(Message message) {
        if (message != null) {
            Bundle data = message.getData();
            ServerListener serverListener = (ServerListener) data.getParcelable("listener");
            switch (message.what) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    m1443a(data.getString(PushMessage.JSON_KEY_ID), serverListener);
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    m1442a((Location) data.getParcelable("location"), serverListener);
                case F2m.PPB /*3*/:
                    m1444a(data.getParcelableArrayList("pois"), serverListener);
                default:
            }
        }
    }

    private synchronized boolean m1442a(Location location, ServerListener serverListener) {
        boolean z;
        if (location == null) {
            CSlog.m1408d("CtxSrvManager", "Update cache, location is null");
            z = false;
        } else {
            CSlog.m1408d("CtxSrvManager", "using " + location.toString() + " to query POIs from server");
            if (Utils.aL(this.mContext)) {
                CSlog.m1410i("CtxSrvManager", "Update cache");
                CacheRequest a = this.GI.m1432a(location);
                C0413a contextServerManager = new ContextServerManager(this, serverListener);
                if (a == null || contextServerManager == null) {
                    CSlog.m1409e("CtxSrvManager", "cannot update cache");
                } else {
                    this.GN.m1422b(new RequestBundle(a, contextServerManager));
                }
            }
            z = true;
        }
        return z;
    }

    public void m1452b(Location location, ServerListener serverListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", location);
        bundle.putParcelable("listener", serverListener);
        message.what = 2;
        message.setData(bundle);
        getHandler().sendMessage(message);
    }

    private synchronized boolean m1443a(String str, ServerListener serverListener) {
        boolean z;
        if (Utils.aL(this.mContext)) {
            CSlog.m1410i("CtxSrvManager", "query policy");
            PolicyRequest bL = this.GI.bL(null);
            C0413a contextServerManager = new ContextServerManager(this, serverListener);
            if (!(bL == null || contextServerManager == null)) {
                this.GM.m1422b(new RequestBundle(bL, contextServerManager));
            }
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public void m1453b(String str, ServerListener serverListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(PushMessage.JSON_KEY_ID, str);
        bundle.putParcelable("listener", serverListener);
        message.what = 1;
        message.setData(bundle);
        getHandler().sendMessage(message);
    }

    private synchronized boolean m1444a(ArrayList<Poi> arrayList, ServerListener serverListener) {
        boolean z = false;
        synchronized (this) {
            if (arrayList != null) {
                if (Utils.aL(this.mContext)) {
                    CSlog.m1410i("CtxSrvManager", "send trigger");
                    TriggerRequestData triggerRequestData = new TriggerRequestData();
                    triggerRequestData.setPois(arrayList);
                    triggerRequestData.setUser(Utils.aH(this.mContext));
                    triggerRequestData.setWallet(new WalletInfo(Utils.aI(this.mContext)));
                    triggerRequestData.setDevice(DeviceInfo.getDefaultDeviceInfo(this.mContext));
                    CSlog.m1408d("CtxSrvManager", "Sending trigger request data " + triggerRequestData.toString());
                    TriggerRequest a = this.GI.m1433a(triggerRequestData);
                    if (a != null) {
                        if (serverListener == null) {
                            a.m836a(new ContextServerManager());
                        } else {
                            a.m836a(new ContextServerManager(this, serverListener));
                        }
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public void m1454b(ArrayList<Poi> arrayList, ServerListener serverListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("pois", arrayList);
        bundle.putParcelable("listener", serverListener);
        message.what = 3;
        message.setData(bundle);
        getHandler().sendMessage(message);
    }

    public void ay(Context context) {
        if (context != null) {
            Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
            intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_JWT_TOKEN);
            AppInterface.m1399a(intent, context);
        }
    }
}
