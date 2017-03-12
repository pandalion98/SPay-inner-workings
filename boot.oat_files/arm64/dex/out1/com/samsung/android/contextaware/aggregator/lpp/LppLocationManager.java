package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings$System;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimedRemoteCaller;
import com.android.internal.util.IState;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.samsung.android.smartclip.SmartClipMetaTagType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

class LppLocationManager {
    private static final int CHECK_GPS_WIFI_AVAILABILITY = 40;
    private static final int FIRST_TIME_GPS_TIMEOUT = 40;
    private static final int GPSBATCH_ENTRY_COUNT = 4;
    private static final int GPS_BATCH_REQ_TIMEOUT = 5;
    private static final int INDOOR_ENTRY_NO_GPS_COUNT = 3;
    private static final int LOC_VALID_ACCURACY_GPS = 16;
    private static final int LOC_VALID_ACCURACY_NW = 40;
    private static final int LOC_VALID_TIME_GPS = 3;
    private static final int NLP_TIMEOUT = 4;
    private static final int OUTDOOR_ENTRY_GPS_COUNT = 3;
    private static final int OUTDOOR_EXIT_ACCURACY = 50;
    private static final int PASSIVE_INACTIVE_TIME = 20;
    public static final float PASSIVE_LOC_ACC_VALIDITY = 32.0f;
    private static final double PASSIVE_LOC_DIST_VALIDITY = 10.0d;
    private static final int PASSIVE_LOC_MIN_TIME = 5;
    private static final int PASSIVE_LOC_VALIDITY = 3;
    private static final String TAG = "LppLocationManager";
    static final Msg[] vals = Msg.values();
    int count = 0;
    private Context mContext = null;
    private final LocationListener mFindGps = new FindGps();
    private final GpsStatusListener mGpsStatusLnr = new GpsStatusListener();
    private long mGpsTimeout;
    private Location mLastLoc = null;
    private final ArrayList<Location> mListLoc = new ArrayList();
    private final ArrayList<Location> mListPassiveLoc = new ArrayList();
    private LppLocationManagerListener mListener;
    private LocationListener mLocLnr;
    private LocationManager mLocMgr;
    private Location mLocMostAccGps = null;
    private Location mLocNw = null;
    private Looper mLooper;
    private int mLppResolution = 0;
    private PassiveSM mPassiveSM = null;
    private LppLocManSM mStateMachine = null;
    private long mTimeRequest;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg = new int[Msg.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.WALK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.VEHICLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.STATIONARY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.GPS_NOT_AVAILABLE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.GPS_AVAILABLE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_REQ.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_FOUND_NETWORK.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_FOUND_GPS.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.STOP.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.CHECK_GPS_WIFI.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_FOUND_BATCH.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_FOUND_PASSIVE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.GPS_BATCH_STARTED.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.LOC_MGR_RETRY.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[Msg.GPS_BATCH_ENDED.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
        }
    }

    private class FindGps implements LocationListener {
        private FindGps() {
        }

        public void onLocationChanged(Location loc) {
            Log.d(LppLocationManager.TAG, "FindGps- onLocationChanged");
            if (loc != null) {
                LppLocationManager.this.mStateMachine.sendMessage(Msg.GPS_AVAILABLE.ordinal(), (Object) new Location(loc));
            }
        }

        public void onProviderDisabled(String arg0) {
        }

        public void onProviderEnabled(String arg0) {
        }

        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }
    }

    private class GpsNmeaListener implements NmeaListener {
        private GpsNmeaListener() {
        }

        public void onNmeaReceived(long timestamp, String nmea) {
            LppLocationManager.this.mListener.logNmeaData(timestamp + "\t" + nmea);
        }
    }

    private static class GpsStatusListener implements Listener {
        private GpsStatusListener() {
        }

        public void onGpsStatusChanged(int event) {
            switch (event) {
                case 1:
                    Log.d(LppLocationManager.TAG, "GPS engine started");
                    return;
                case 2:
                    Log.d(LppLocationManager.TAG, "GPS engine stopped");
                    return;
                case 3:
                    Log.d(LppLocationManager.TAG, "GPS engine calcuates first fix");
                    return;
                default:
                    return;
            }
        }
    }

    private enum LocValidity {
        VALID,
        INVALID_TIME,
        INVALID_ACC
    }

    private class LppLocManSM extends StateMachine {
        private boolean firstTimeGps = true;
        private AllNM mAllNM = null;
        private GpsBatch mGpsBatch = null;
        private Indoor mIndoor = null;
        private Outdoor mOutdoor = null;
        private Restricted mRestricted = null;
        private StatNM mStatNM = null;
        private VehNM mVehNM = null;
        private WalkNM mWalkNM = null;

        class AllNM extends State {
            AllNM() {
            }

            public void enter() {
                Log.d(LppLocationManager.TAG, "Entering " + getName());
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        break;
                    case 2:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mVehNM);
                        break;
                    case 3:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mStatNM);
                        break;
                    case 4:
                        LppLocationManager.this.mListener.gpsOffBatchStopped();
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        break;
                    case 5:
                        LppLocationManager.this.mListener.gpsOnBatchStopped();
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        LppLocManSM.this.removeMessages(Msg.GPS_NOT_AVAILABLE.ordinal());
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class GpsBatch extends State {
            private long deliveredT = System.currentTimeMillis();
            private boolean exit = false;
            private final ArrayList<Location> mListBatchLoc = new ArrayList();
            private int period = 3;
            private final int requestId = 0;
            private boolean walk = false;

            GpsBatch() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                if (LppLocationManager.this.mLppResolution == 0) {
                    this.period = 3;
                } else if (LppLocationManager.this.mLppResolution == 1) {
                    this.period = 2;
                } else {
                    this.period = 1;
                }
                Log.e(LppLocationManager.TAG, "error in start batch:0");
                LppLocManSM.this.transitionTo(LppLocManSM.this.mVehNM);
                this.exit = false;
                this.walk = false;
                this.mListBatchLoc.clear();
                this.deliveredT = System.currentTimeMillis();
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        this.walk = true;
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 7:
                        if (!this.exit) {
                            LppLocManSM.this.sendMessageDelayed(Msg.GPS_BATCH_TIMEOUT.ordinal(), (long) TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                            break;
                        }
                        break;
                    case 11:
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 15:
                        ArrayList<Location> listAllLoc = new ArrayList();
                        LppLocManSM.this.removeMessages(Msg.GPS_BATCH_TIMEOUT.ordinal());
                        listAllLoc.clear();
                        listAllLoc.addAll(this.mListBatchLoc);
                        Log.d(LppLocationManager.TAG, "num of batch locs:" + this.mListBatchLoc.size());
                        if (LppLocationManager.this.mListPassiveLoc.size() != 0) {
                            synchronized (this) {
                                listAllLoc.addAll(LppLocationManager.this.mListPassiveLoc);
                                LppLocationManager.this.mListPassiveLoc.clear();
                            }
                        }
                        Collections.sort(listAllLoc, new Comparator<Location>() {
                            public int compare(Location arg0, Location arg1) {
                                if (arg0.getTime() < arg1.getTime()) {
                                    return -1;
                                }
                                if (arg0.getTime() > arg1.getTime()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                        Log.d(LppLocationManager.TAG, "deliveredT:" + this.deliveredT);
                        int i = 0;
                        while (i < listAllLoc.size() && ((Location) listAllLoc.get(i)).getTime() < this.deliveredT) {
                            i++;
                        }
                        for (int j = 0; j < i; j++) {
                            listAllLoc.remove(0);
                        }
                        if (this.mListBatchLoc.size() > 0) {
                            Location recentLoc = (Location) this.mListBatchLoc.get(0);
                            Iterator i$ = this.mListBatchLoc.iterator();
                            while (i$.hasNext()) {
                                Location loc = (Location) i$.next();
                                if (loc.getTime() > recentLoc.getTime()) {
                                    recentLoc = loc;
                                }
                            }
                            Location recentLoc2 = new Location(recentLoc);
                            this.mListBatchLoc.clear();
                            LppLocationManager.this.mListener.batchLocUpdate(recentLoc2);
                            LppLocationManager.this.mListener.batchLocListUpdate(listAllLoc);
                            this.deliveredT = System.currentTimeMillis();
                        }
                        if (this.exit) {
                            if (!this.walk) {
                                LppLocManSM.this.transitionTo(LppLocManSM.this.mStatNM);
                                break;
                            }
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                            break;
                        }
                        break;
                    case 16:
                        LppLocationManager.this.mListener.locationNotFound();
                        if (this.exit) {
                            if (!this.walk) {
                                LppLocManSM.this.transitionTo(LppLocManSM.this.mStatNM);
                                break;
                            }
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                            break;
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mVehNM);
                        break;
                    default:
                        return false;
                }
                LppLocManSM.this.removeMessages(Msg.GPS_BATCH_TIMEOUT.ordinal());
                LppLocManSM.this.sendMessageDelayed(Msg.GPS_BATCH_TIMEOUT.ordinal(), (long) TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                this.exit = true;
                return true;
            }

            public void exit() {
                Log.d(LppLocationManager.TAG, "Exiting " + getName());
                LppLocManSM.this.removeMessages(Msg.GPS_BATCH_TIMEOUT.ordinal());
                LppLocationManager.this.mListener.gpsBatchStopped();
                if (LppLocationManager.this.mLocMgr.isProviderEnabled("gps")) {
                    LppLocationManager.this.mLocMgr.requestSingleUpdate("gps", LppLocationManager.this.mFindGps, LppLocationManager.this.mLooper);
                    LppLocManSM.this.sendMessageDelayed(Msg.GPS_NOT_AVAILABLE.ordinal(), LppLocationManager.this.mGpsTimeout * 1000);
                } else {
                    LppLocationManager.this.mListener.gpsOffBatchStopped();
                }
                LppLocationManager.this.mPassiveSM.sendMessage(Msg.GPS_BATCH_ENDED.ordinal());
            }
        }

        class Indoor extends State {
            Indoor() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocNw = null;
                LppLocationManager.this.mListener.gpsUnavailable();
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        return true;
                    case 7:
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                        LppLocationManager.this.sendStatus("requestSingleUpdate, timeout:4");
                        if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                            LppLocationManager.this.mLocMgr.requestSingleUpdate("network", LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                        }
                        LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_NLP_TIMEOUT.ordinal(), 4000);
                        return true;
                    case 8:
                        Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        return true;
                    case 11:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        return true;
                    case 12:
                        if (LppLocationManager.this.mLocNw != null) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        return true;
                    default:
                        return false;
                }
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_NLP_TIMEOUT.ordinal());
                LppLocationManager.this.mLocNw = null;
            }
        }

        class Outdoor extends State {
            Outdoor() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mListener.gpsAvailable();
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        break;
                    case 7:
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                        Log.d(LppLocationManager.TAG, "requestLocationUpdates, timeout:" + LppLocationManager.this.mGpsTimeout);
                        LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                        LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                        LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), LppLocationManager.this.mGpsTimeout * 1000);
                        break;
                    case 9:
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            Log.e(LppLocationManager.TAG, "mLocMostAccGps is null");
                            LppLocationManager.this.mListener.locationNotFound();
                            break;
                        }
                        Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        break;
                    case 10:
                        if (LppLocationManager.this.mLocMostAccGps != null) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        if ((LppLocationManager.this.mLocMostAccGps != null && LppLocationManager.this.mLocMostAccGps.getAccuracy() > 50.0f) || LppLocationManager.this.mLocMostAccGps == null) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                            break;
                        }
                    case 11:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    default:
                        return false;
                }
                return true;
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
            }
        }

        class Restricted extends State {
            Restricted() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocNw = null;
                LppLocationManager.this.mListener.gpsUnavailable();
                LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        break;
                    case 5:
                    case 14:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        break;
                    case 7:
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                        Log.d(LppLocationManager.TAG, "requestSingleUpdate, timeout:4");
                        if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                            LppLocationManager.this.mLocMgr.requestSingleUpdate("network", LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                        }
                        LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_NLP_TIMEOUT.ordinal(), 4000);
                        LppLocManSM.this.removeMessages(Msg.CHECK_GPS_WIFI.ordinal());
                        LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        break;
                    case 8:
                        Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        if (!LppLocManSM.this.isWifiAvailable()) {
                            LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
                            break;
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mIndoor);
                        break;
                    case 10:
                        LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        break;
                    case 11:
                        exit();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 12:
                        if (LppLocationManager.this.mLocNw != null) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
                        break;
                    case 13:
                        if (!LppLocManSM.this.isWifiAvailable()) {
                            long tOut;
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mFindGps, LppLocationManager.this.mLooper);
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                tOut = 40;
                            } else {
                                tOut = LppLocationManager.this.mGpsTimeout;
                            }
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * tOut);
                            break;
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mIndoor);
                        break;
                    default:
                        return false;
                }
                return true;
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_NLP_TIMEOUT.ordinal());
                LppLocationManager.this.mLocNw = null;
            }

            public void exit() {
                Log.d(LppLocationManager.TAG, "Exiting " + getName());
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_NLP_TIMEOUT.ordinal());
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                LppLocManSM.this.removeMessages(Msg.CHECK_GPS_WIFI.ordinal());
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
            }
        }

        class StatNM extends State {
            private boolean nwFound = false;

            StatNM() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 3:
                    case 6:
                        break;
                    case 7:
                        if (LppLocationManager.this.mLocMgr == null) {
                            LppLocationManager.this.mLocMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
                        }
                        if (LppLocationManager.this.mLocMgr != null) {
                            long tOut;
                            LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                                LppLocationManager.this.mLocMgr.requestLocationUpdates("network", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            }
                            LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                tOut = 40;
                            } else {
                                tOut = LppLocationManager.this.mGpsTimeout;
                            }
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * tOut);
                            Log.d(LppLocationManager.TAG, "requestLocationUpdates,timeout:" + tOut);
                            break;
                        }
                        Log.e(LppLocationManager.TAG, "mLocMgr is null");
                        LppLocationManager.this.mListener.locationNotFound();
                        break;
                    case 8:
                        this.nwFound = true;
                        break;
                    case 9:
                        Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy");
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        break;
                    case 10:
                        if (LppLocationManager.this.mLocMostAccGps != null && !this.nwFound) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else if (LppLocationManager.this.mLocNw != null) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        break;
                    case 11:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    default:
                        return false;
                }
                return true;
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeGpsStatusListener(LppLocationManager.this.mGpsStatusLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                this.nwFound = false;
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }
        }

        class VehNM extends State {
            private boolean nwFound = false;
            private int reqCount = 0;

            VehNM() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
                this.reqCount = 0;
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 2:
                        break;
                    case 7:
                        if (LppLocationManager.this.mLocMgr == null) {
                            LppLocationManager.this.mLocMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
                        }
                        if (LppLocationManager.this.mLocMgr != null) {
                            long tOut;
                            this.reqCount++;
                            LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                                LppLocationManager.this.mLocMgr.requestSingleUpdate("network", LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            }
                            LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                tOut = 40;
                            } else {
                                tOut = LppLocationManager.this.mGpsTimeout;
                            }
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * tOut);
                            Log.d(LppLocationManager.TAG, "requestLocationUpdates, timeout:" + tOut);
                            break;
                        }
                        Log.e(LppLocationManager.TAG, "mLocMgr is null");
                        LppLocationManager.this.mListener.locationNotFound();
                        break;
                    case 8:
                        this.nwFound = true;
                        break;
                    case 9:
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            Log.e(LppLocationManager.TAG, "mLocMostAccGps is null!");
                            LppLocationManager.this.mListener.locationNotFound();
                            break;
                        }
                        Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        if (this.reqCount >= 4 && LppLocationManager.this.mLppResolution == 0) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mGpsBatch);
                            break;
                        }
                    case 10:
                        if (LppLocationManager.this.mLocMostAccGps != null && !this.nwFound) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else if (LppLocationManager.this.mLocNw != null) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        if (this.reqCount >= 4 && LppLocationManager.this.mLppResolution == 0) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mGpsBatch);
                            break;
                        }
                        break;
                    case 11:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    default:
                        return false;
                }
                return true;
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeGpsStatusListener(LppLocationManager.this.mGpsStatusLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                this.nwFound = false;
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }
        }

        class WalkNM extends State {
            private int gpsCount = 0;
            private int noGpsCount = 0;
            private boolean nwFound = false;

            WalkNM() {
            }

            public void enter() {
                Log.v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
                this.noGpsCount = 0;
                this.gpsCount = 0;
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        break;
                    case 7:
                        if (LppLocationManager.this.mLocMgr == null) {
                            LppLocationManager.this.mLocMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
                        }
                        if (LppLocationManager.this.mLocMgr != null) {
                            long tOut;
                            LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                                LppLocationManager.this.mLocMgr.requestLocationUpdates("network", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            }
                            LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                tOut = 40;
                            } else {
                                tOut = LppLocationManager.this.mGpsTimeout;
                            }
                            Log.d(LppLocationManager.TAG, "requestLocationUpdates, timeout:" + tOut);
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * tOut);
                            break;
                        }
                        Log.e(LppLocationManager.TAG, "mLocMgr is null");
                        LppLocationManager.this.mListener.locationNotFound();
                        break;
                    case 8:
                        this.nwFound = true;
                        break;
                    case 9:
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            Log.e(LppLocationManager.TAG, "mLocMostAccGps is null");
                            LppLocationManager.this.mListener.locationNotFound();
                            break;
                        }
                        Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        this.noGpsCount = 0;
                        this.gpsCount++;
                        clear();
                        if (this.gpsCount >= 3) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mOutdoor);
                            break;
                        }
                        break;
                    case 10:
                        if (LppLocationManager.this.mLocMostAccGps != null && !this.nwFound) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else if (LppLocationManager.this.mLocNw != null) {
                            Log.d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            this.noGpsCount++;
                            this.gpsCount = 0;
                        } else {
                            this.noGpsCount = 0;
                            this.gpsCount++;
                        }
                        clear();
                        if (this.gpsCount < 3) {
                            if (this.noGpsCount >= 3) {
                                if (!LppLocManSM.this.isWifiAvailable()) {
                                    LppLocManSM.this.transitionTo(LppLocManSM.this.mRestricted);
                                    break;
                                }
                                LppLocManSM.this.transitionTo(LppLocManSM.this.mIndoor);
                                break;
                            }
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mOutdoor);
                        break;
                        break;
                    case 11:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    default:
                        return false;
                }
                return true;
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeGpsStatusListener(LppLocationManager.this.mGpsStatusLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                this.nwFound = false;
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }
        }

        protected LppLocManSM(String name) {
            super(name);
            Log.d(LppLocationManager.TAG, "Creating State Machine");
            this.mAllNM = new AllNM();
            this.mStatNM = new StatNM();
            addState(this.mStatNM, this.mAllNM);
            this.mWalkNM = new WalkNM();
            addState(this.mWalkNM, this.mAllNM);
            this.mIndoor = new Indoor();
            addState(this.mIndoor, this.mAllNM);
            this.mOutdoor = new Outdoor();
            addState(this.mOutdoor, this.mAllNM);
            this.mVehNM = new VehNM();
            addState(this.mVehNM, this.mAllNM);
            this.mGpsBatch = new GpsBatch();
            addState(this.mGpsBatch, this.mAllNM);
            this.mRestricted = new Restricted();
            addState(this.mRestricted, this.mAllNM);
            setInitialState(this.mStatNM);
        }

        private boolean isWifiAvailable() {
            WifiManager wifi = (WifiManager) LppLocationManager.this.mContext.getSystemService(Settings$System.RADIO_WIFI);
            if (wifi == null || !wifi.isWifiEnabled()) {
                return false;
            }
            return true;
        }

        private void exit() {
            quit();
        }

        private IState getState() {
            return getCurrentState();
        }
    }

    private class MainLocationListener implements LocationListener {
        private MainLocationListener() {
        }

        public void onLocationChanged(Location loc) {
            Log.d(LppLocationManager.TAG, "MainLocationListener - onLocationChanged:" + loc);
            if (loc != null) {
                Log.i(LppLocationManager.TAG, "loc time:" + loc.getTime());
                LppLocationManager.this.mListLoc.add(new Location(loc));
                if (LppLocationManager.this.mStateMachine == null) {
                    Log.e(LppLocationManager.TAG, "unhandled update");
                    return;
                }
                if (LppLocationManager.this.mLastLoc == null) {
                    LppLocationManager.this.mLastLoc = new Location(loc);
                } else {
                    LppLocationManager.this.mLastLoc.set(loc);
                }
                if (loc.getProvider().equals("gps")) {
                    Log.d(LppLocationManager.TAG, "onLocationChanged provider : " + loc.getProvider() + " Accuracy " + loc.getAccuracy());
                    LocValidity isValid = LppLocationManager.this.locValidCheckGps(loc);
                    if (isValid != LocValidity.INVALID_TIME) {
                        LppLocationManager.this.setMostAccLocGps(loc);
                    }
                    if (isValid == LocValidity.VALID) {
                        LppLocationManager.this.mStateMachine.sendMessage(Msg.LOC_FOUND_GPS.ordinal());
                    }
                } else {
                    LppLocationManager.this.mLocNw = new Location(loc);
                    if (LppLocationManager.this.locValidCheckNw(loc) == LocValidity.VALID) {
                        LppLocationManager.this.mStateMachine.sendMessage(Msg.LOC_FOUND_NETWORK.ordinal());
                    }
                }
                int LocSource = 1;
                if (LppLocationManager.this.mLastLoc.getProvider().equals("gps")) {
                    LocSource = 2;
                }
                LppLocationManager.this.mListener.logData("\t" + LocSource + "\t" + loc.getLatitude() + "\t" + loc.getLongitude() + "\t" + loc.getAltitude() + "\t" + loc.getAccuracy() + "\t" + loc.getTime());
            }
        }

        public void onProviderDisabled(String provider) {
            Log.w(LppLocationManager.TAG, "onProviderDisabled:" + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.d(LppLocationManager.TAG, "onProviderEnabled:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private enum Msg {
        START,
        STATIONARY,
        WALK,
        VEHICLE,
        LOC_REQ,
        LOC_REQ_GPS_TIMEOUT,
        LOC_REQ_NLP_TIMEOUT,
        LOC_FOUND_GPS,
        LOC_FOUND_NETWORK,
        LOC_FOUND_BATCH,
        LOC_FOUND_PASSIVE,
        LOC_MGR_RETRY,
        PASSIVE_INACTIVE_TIMEOUT,
        GPS_BATCH_STARTED,
        GPS_BATCH_ENDED,
        GPS_BATCH_TIMEOUT,
        GPS_AVAILABLE,
        GPS_PASSIVE_AVAILABLE,
        GPS_NOT_AVAILABLE,
        CHECK_GPS_WIFI,
        STOP
    }

    private class PassiveSM extends StateMachine {
        private Location lastLoc;
        private Listening mListening;
        private LocationManager mLocationMgr;
        private PassGpsBatch mPassGpsBatch;
        private final LocationListener mPassLnr;
        private Pause mPause;

        class Listening extends State {
            Listening() {
            }

            public void enter() {
                Log.d(LppLocationManager.TAG, "Entering " + getName());
                if (PassiveSM.this.mLocationMgr == null) {
                    Log.e(LppLocationManager.TAG, "mLocationMgr is null");
                    PassiveSM.this.sendMessageDelayed(Msg.LOC_MGR_RETRY.ordinal(), (long) DateUtils.MINUTE_IN_MILLIS);
                    return;
                }
                PassiveSM.this.mLocationMgr.requestLocationUpdates("passive", TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, 0.0f, PassiveSM.this.mPassLnr, LppLocationManager.this.mLooper);
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 7:
                        PassiveSM.this.transitionTo(PassiveSM.this.mPause);
                        break;
                    case 17:
                        Location curLoc = message.obj;
                        LppLocationManager.this.mListener.locPassUpdate(curLoc);
                        PassiveSM.this.lastLoc = curLoc;
                        break;
                    case 18:
                        PassiveSM.this.transitionTo(PassiveSM.this.mPassGpsBatch);
                        break;
                    case 19:
                        PassiveSM.this.mLocationMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
                        if (PassiveSM.this.mLocationMgr != null) {
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("passive", TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, 0.0f, PassiveSM.this.mPassLnr, LppLocationManager.this.mLooper);
                            break;
                        }
                        Log.e(LppLocationManager.TAG, "mLocationMgr is null");
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class PassGpsBatch extends State {
            PassGpsBatch() {
            }

            public void enter() {
                Log.d(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mListPassiveLoc.clear();
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 17:
                        Location loc = message.obj;
                        if (PassiveSM.this.lastLoc == null && loc.getAccuracy() < LppLocationManager.PASSIVE_LOC_ACC_VALIDITY) {
                            LppLocationManager.this.mListPassiveLoc.add(loc);
                        } else if (loc.getAccuracy() < LppLocationManager.PASSIVE_LOC_ACC_VALIDITY && LppLocationManager.validPassDist(PassiveSM.this.lastLoc.getLatitude(), PassiveSM.this.lastLoc.getLongitude(), loc.getLatitude(), loc.getLongitude())) {
                            LppLocationManager.this.mListPassiveLoc.add(loc);
                        }
                        PassiveSM.this.lastLoc = new Location(loc);
                        break;
                    case 21:
                        PassiveSM.this.transitionTo(PassiveSM.this.mListening);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        private class PassiveListener implements LocationListener {
            private PassiveListener() {
            }

            public void onLocationChanged(Location loc) {
                if (loc != null) {
                    LppLocationManager.this.sendStatus("PassiveListener:" + loc.getAccuracy());
                    LppLocationManager.this.mPassiveSM.sendMessage(Msg.LOC_FOUND_PASSIVE.ordinal(), (Object) new Location(loc));
                    LppLocationManager.this.mStateMachine.sendMessage(Msg.GPS_PASSIVE_AVAILABLE.ordinal());
                }
            }

            public void onProviderDisabled(String arg0) {
            }

            public void onProviderEnabled(String arg0) {
            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            }
        }

        class Pause extends State {
            boolean batchStart = false;
            boolean firstTime = true;

            Pause() {
            }

            public void enter() {
                Log.d(LppLocationManager.TAG, "Entering " + getName());
                if (PassiveSM.this.mLocationMgr != null) {
                    PassiveSM.this.mLocationMgr.removeUpdates(PassiveSM.this.mPassLnr);
                }
                if (this.firstTime) {
                    PassiveSM.this.sendMessageDelayed(Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal(), 45000);
                    this.firstTime = false;
                } else {
                    PassiveSM.this.sendMessageDelayed(Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal(), 20000);
                }
                this.batchStart = false;
            }

            public boolean processMessage(Message message) {
                Log.d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppLocationManager$Msg[LppLocationManager.vals[message.what].ordinal()]) {
                    case 18:
                        this.batchStart = true;
                        break;
                    case 20:
                        if (!this.batchStart) {
                            PassiveSM.this.transitionTo(PassiveSM.this.mListening);
                            break;
                        }
                        PassiveSM.this.mLocationMgr.requestLocationUpdates("passive", TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, 0.0f, PassiveSM.this.mPassLnr, LppLocationManager.this.mLooper);
                        PassiveSM.this.transitionTo(PassiveSM.this.mPassGpsBatch);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        protected PassiveSM(String name) {
            super(name);
            this.mLocationMgr = null;
            this.mPassLnr = new PassiveListener();
            this.lastLoc = null;
            this.mListening = null;
            this.mPause = null;
            this.mPassGpsBatch = null;
            this.mListening = new Listening();
            addState(this.mListening);
            this.mPause = new Pause();
            addState(this.mPause);
            this.mPassGpsBatch = new PassGpsBatch();
            addState(this.mPassGpsBatch);
            setInitialState(this.mListening);
            this.mLocationMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
        }

        private Location getLastLoc() {
            return this.lastLoc;
        }

        private void exit() {
            if (this.mLocationMgr != null) {
                this.mLocationMgr.removeUpdates(this.mPassLnr);
            }
            quit();
        }
    }

    LppLocationManager() {
    }

    public void start(LppConfig config, LppLocationManagerListener mLMLnr) {
        Log.v(TAG, "start");
        if (config == null) {
            Log.e(TAG, "config null");
            return;
        }
        this.mContext = config.getContext();
        if (this.mContext == null) {
            Log.e(TAG, "context null");
            return;
        }
        HandlerThread handlerThread = new HandlerThread("CAE_LPPLOCMGR");
        handlerThread.start();
        this.mLooper = handlerThread.getLooper();
        if (this.mLooper == null) {
            Log.e(TAG, "looper null");
            return;
        }
        this.mLocMgr = (LocationManager) this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
        if (this.mLocMgr == null) {
            Log.e(TAG, "mLocMgr is null");
        }
        this.mLocLnr = new MainLocationListener();
        this.mListener = mLMLnr;
        this.mGpsTimeout = (long) config.GPSKeepOn_Timer;
        this.mStateMachine = new LppLocManSM(TAG);
        this.mStateMachine.start();
        this.mStateMachine.sendMessage(Msg.START.ordinal());
        this.mPassiveSM = new PassiveSM(TAG);
        this.mPassiveSM.start();
    }

    public void stop() {
        Log.v(TAG, "stop");
        if (this.mStateMachine != null) {
            this.mStateMachine.sendMessage(Msg.STOP.ordinal());
        }
        if (this.mPassiveSM != null) {
            this.mPassiveSM.exit();
        }
    }

    public void setLppResolution(int res) {
        this.mLppResolution = res;
    }

    public static boolean validPassDist(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.sin(dLon / 2.0d) * Math.sin(dLon / 2.0d)) * Math.cos(Math.toRadians(lat1))) * Math.cos(Math.toRadians(lat2)));
        double d = (6371.0d * (2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)))) * 1000.0d;
        if (d < PASSIVE_LOC_DIST_VALIDITY) {
            Log.w(TAG, "distance not valid:" + d);
            return false;
        }
        Log.d(TAG, "distance valid:" + d);
        return true;
    }

    public void locRequest(int movingStatus) {
        Log.d(TAG, "LocRequest");
        this.count++;
        if (this.mStateMachine != null) {
            Location loc = this.mPassiveSM.getLastLoc();
            if (loc == null || System.currentTimeMillis() - loc.getTime() >= 3000 || this.mStateMachine.getState() == this.mStateMachine.mGpsBatch) {
                if (movingStatus == 1) {
                    this.mStateMachine.sendMessage(Msg.STATIONARY.ordinal());
                } else if (movingStatus == 2) {
                    this.mStateMachine.sendMessage(Msg.WALK.ordinal());
                } else if (movingStatus == 4) {
                    this.mStateMachine.sendMessage(Msg.VEHICLE.ordinal());
                }
                this.mStateMachine.sendMessage(Msg.LOC_REQ.ordinal());
                this.mPassiveSM.sendMessage(Msg.LOC_REQ.ordinal());
                return;
            }
            Log.v(TAG, "passive loc found!: " + System.currentTimeMillis() + ":" + loc.getTime());
            this.mListLoc.clear();
            this.mListLoc.add(new Location(loc));
            this.mListener.locUpdate(this.mListLoc);
        }
    }

    private void setMostAccLocGps(Location loc) {
        Log.d(TAG, "setMostAccLoc");
        if (this.mLocMostAccGps == null) {
            this.mLocMostAccGps = new Location(loc);
        } else if (this.mLocMostAccGps.getAccuracy() > loc.getAccuracy()) {
            this.mLocMostAccGps.set(loc);
        }
    }

    private LocValidity locValidCheckGps(Location loc) {
        if (System.currentTimeMillis() - this.mTimeRequest < 3000) {
            Log.w(TAG, "time is not enough - " + (System.currentTimeMillis() - this.mTimeRequest));
            return LocValidity.INVALID_TIME;
        } else if (loc.getAccuracy() <= 16.0f) {
            return LocValidity.VALID;
        } else {
            Log.w(TAG, "GPS: Accuracy is not good:" + loc.getAccuracy());
            return LocValidity.INVALID_ACC;
        }
    }

    private LocValidity locValidCheckNw(Location loc) {
        if (loc.getAccuracy() <= 40.0f) {
            return LocValidity.VALID;
        }
        Log.w(TAG, "N/W: Accuracy is not good:" + loc.getAccuracy());
        return LocValidity.INVALID_ACC;
    }

    public double getLastLocLat() {
        if (this.mLastLoc != null) {
            return this.mLastLoc.getLatitude();
        }
        return 0.0d;
    }

    public double getLastLocLon() {
        if (this.mLastLoc != null) {
            return this.mLastLoc.getLongitude();
        }
        return 0.0d;
    }

    public double getLastHeight() {
        if (this.mLastLoc != null) {
            return this.mLastLoc.getAltitude();
        }
        return 0.0d;
    }

    public Location getLastLoc() {
        return this.mLastLoc;
    }

    public void sendStatus(String strDisp) {
        this.mListener.status("LppLocMan: " + strDisp);
    }
}
