package com.samsung.contextservice.server;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.location.Location;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.p029b.Config;
import com.samsung.contextservice.system.ContextSFManager;
import com.samsung.contextservice.system.ManagerHub;
import com.samsung.contextservice.system.ManagerHub.State;
import com.samsung.contextservice.system.VerdictManager;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TaskSchedulerService extends JobService {
    private static ContextServerManager Hc;
    private static VerdictManager Hd;

    public boolean onStartJob(JobParameters jobParameters) {
        if (jobParameters != null) {
            if (ManagerHub.gI() == State.RUNNING) {
                CSlog.m1408d("TaskSchedSrv", "start query job id " + jobParameters.getJobId());
                if (Hc == null) {
                    try {
                        Hc = (ContextServerManager) ManagerHub.m1472Z(2);
                    } catch (Throwable e) {
                        CSlog.m1403a("TaskSchedSrv", "onStartJob sCtxServerManager exception", e);
                    }
                }
                if (Hd == null) {
                    try {
                        Hd = VerdictManager.aG(getApplicationContext());
                    } catch (Throwable e2) {
                        CSlog.m1403a("TaskSchedSrv", "onStartJob sRCacheDao exception", e2);
                    }
                }
                switch (jobParameters.getJobId()) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        Hc.m1453b("ALL_POLICIES", null);
                        break;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        try {
                            Location lastLocation = ContextSFManager.aB(getApplicationContext()).getLastLocation();
                            if (lastLocation != null) {
                                if (Hd != null) {
                                    if (!Hd.m1497a(lastLocation.getLatitude(), lastLocation.getLongitude())) {
                                        com.samsung.contextclient.data.Location location = new com.samsung.contextclient.data.Location(lastLocation);
                                        location.setRadius(Config.HL[4]);
                                        Hc.m1452b(location, null);
                                        break;
                                    }
                                }
                                CSlog.m1408d("TaskSchedSrv", "cannot fetch cache from server, vm is null");
                                break;
                            }
                            CSlog.m1408d("TaskSchedSrv", "cannot fetch cache from server, location is null");
                            break;
                        } catch (Exception e3) {
                            CSlog.m1408d("TaskSchedSrv", "cannot schedule cache");
                            break;
                        }
                        break;
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        break;
                    default:
                        break;
                }
            }
            CSlog.m1410i("TaskSchedSrv", "context service is not running" + jobParameters.getJobId());
        }
        return false;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
