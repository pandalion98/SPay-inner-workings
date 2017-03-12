package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner.ContextValIndex;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class LppAggregator extends Aggregator {
    private static final int DEFAULT_LPP_RESOLUTION = 0;
    private static final int LPP_DEBUG_MSG_END = 8095150;
    private static final int LPP_DEBUG_MSG_START = 19316221;
    private static final int NEXT_APDR = 43946;
    private static final String TAG = "LppAggregator";
    private final LPPFusionListener LPPLnr = new LPPFusionListener();
    private double[] altitude;
    int count = 0;
    private final int gpsKeepOnTimer = 10;
    private final int gpsRequestApdr = 100;
    private final int gpsRequestTimer = 20;
    private double[] latitude;
    private double[] longitude;
    private ApdrRunner mApdrRunner = null;
    private LppFusion mLPPFusion = null;
    int[] mStatus = new int[]{1, 2, 2, 2, 2, 2, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2, 1};
    Handler sendApdr;
    private final String strConfigEdit = "[Note] \n";
    private final String strConfigText = "";
    private long[] timestamp;

    private class LPPFusionListener implements ILppDataProvider {
        private LPPFusionListener() {
        }

        public void onLocationChanged(Location loc) {
            Log.i(LppAggregator.TAG, "loc time:" + loc.getTime());
            if (LppAggregator.this.mApdrRunner != null) {
                LppAggregator.this.mApdrRunner.locationUpdate(loc);
            }
        }

        public void lppUpdate(ArrayList<Location> listLPPResult) {
            Log.d(LppAggregator.TAG, "LPPUpdate");
            LppAggregator.this.notifyPositionContext(listLPPResult);
        }

        public void lppStatus(String str) {
        }

        public void gpsBatchStarted() {
            LppAggregator.this.mApdrRunner.gpsBatchStarted();
        }

        public void gpsOnBatchStopped() {
            LppAggregator.this.mApdrRunner.gpsOnBatchStopped();
        }

        public void gpsOffBatchStopped() {
            LppAggregator.this.mApdrRunner.gpsOffBatchStopped();
        }

        public void gpsAvailable() {
            LppAggregator.this.mApdrRunner.gpsAvailable();
        }

        public void gpsUnavailable() {
            LppAggregator.this.mApdrRunner.gpsUnavailable();
        }
    }

    public LppAggregator(int version, Context context, Looper looper, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, context, looper, collectionList, observable);
        Iterator i$ = collectionList.iterator();
        while (i$.hasNext()) {
            ContextComponent i = (ContextComponent) i$.next();
            if (i.getContextType().equals(ContextType.SENSORHUB_RUNNER_APDR.getCode())) {
                this.mApdrRunner = (ApdrRunner) i;
                return;
            }
        }
    }

    protected final void initializeAggregator() {
    }

    protected final void terminateAggregator() {
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    private void notifyLppContext(int count) {
        String[] names = getContextValueNames();
        super.getContextBean().putContext(names[0], count);
        super.getContextBean().putContext(names[1], this.timestamp);
        super.getContextBean().putContext(names[2], this.latitude);
        super.getContextBean().putContext(names[3], this.longitude);
        super.getContextBean().putContext(names[4], this.altitude);
        super.notifyObserver();
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_LPP.getCode();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        if (property == 32) {
            int resolution = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Resolution = " + Integer.toString(resolution));
            if (resolution == 0 || resolution == 1 || resolution == 2) {
                if (this.mApdrRunner != null) {
                    this.mApdrRunner.setLppResolution(resolution);
                }
                if (this.mLPPFusion != null) {
                    this.mLPPFusion.setLppResolution(resolution);
                }
            } else {
                if (this.mLPPFusion != null) {
                    if (resolution == LPP_DEBUG_MSG_START) {
                        this.mLPPFusion.sendStatusEnable();
                    }
                    if (resolution == LPP_DEBUG_MSG_END) {
                        this.mLPPFusion.sendStatusDisable();
                    }
                }
                CaLogger.warning("Invalid value for LPP resolution");
                return false;
            }
        }
        result = false;
        return result;
    }

    public final String[] getContextValueNames() {
        return new String[]{"TrajectoryCount", "TrajectoryTimeStamp", "TrajectoryLatitude", "TrajectoryLongitude", "TrajectoryAltitude"};
    }

    public final void updateContext(String type, Bundle context) {
        CaLogger.debug("Context type " + type);
        if (type.equals(ContextType.SENSORHUB_RUNNER_APDR.getCode()) && this.mLPPFusion != null && this.mApdrRunner != null) {
            String[] names = this.mApdrRunner.getContextValueNames();
            int stayA = context.getInt(names[ContextValIndex.StayingArea.index()]);
            if (stayA != 0) {
                this.mLPPFusion.notifyStayArea(stayA);
                return;
            }
            int dataSize = context.getInt(names[ContextValIndex.Count.index()]);
            int hour = context.getInt(names[ContextValIndex.Hour.index()]);
            int minute = context.getInt(names[ContextValIndex.Minute.index()]);
            int second = context.getInt(names[ContextValIndex.Second.index()]);
            int doe = context.getInt(names[ContextValIndex.doe.index()]);
            long[] timeDiff = context.getLongArray(names[ContextValIndex.TimeDifference.index()]);
            int[] incEast = context.getIntArray(names[ContextValIndex.IncrementEast.index()]);
            int[] incNorth = context.getIntArray(names[ContextValIndex.IncrementNorth.index()]);
            int[] activityType = context.getIntArray(names[ContextValIndex.ActivityType.index()]);
            long rxTime = convertToUtc2(hour, minute, second);
            ArrayList<ApdrData> list = new ArrayList();
            for (int i = 0; i < dataSize; i++) {
                ApdrData apdr = new ApdrData();
                apdr.stepFlag = 1.0d;
                apdr.stepLength = Math.sqrt((((double) (incEast[i] * incEast[i])) * 1.0d) + (((double) (incNorth[i] * incNorth[i])) * 1.0d));
                apdr.utctime = rxTime + (0 + timeDiff[i]);
                apdr.apdrHeading = Math.asin(((double) incEast[i]) / apdr.stepLength);
                if (apdr.apdrHeading < 0.0d) {
                    apdr.apdrHeading += 3.141592653589793d;
                }
                apdr.mag[3] = (double) doe;
                apdr.movingStatus = activityType[i] & 15;
                apdr.carryPos = activityType[i] & 240;
                list.add(apdr);
            }
            this.mLPPFusion.notifyApdrData(list);
        }
    }

    public final void enable() {
        CaLogger.trace();
        final LppConfig config = new LppConfig(super.getContext(), 100, 20, 10);
        config.setContext(super.getContext());
        config.looper = super.getLooper();
        new Handler(super.getLooper()).postDelayed(new Runnable() {
            public void run() {
                LppAggregator.this.mLPPFusion = new LppFusion(config);
                LppAggregator.this.mLPPFusion.registerListener(LppAggregator.this.LPPLnr);
                LppAggregator.this.mLPPFusion.start();
            }
        }, 0);
    }

    private void test() {
        this.sendApdr = new Handler(super.getLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == LppAggregator.NEXT_APDR) {
                    Log.d(LppAggregator.TAG, "send apdr");
                    ArrayList<ApdrData> list = new ArrayList();
                    for (int i = 0; i < 1; i++) {
                        ApdrData apdr = new ApdrData();
                        apdr.stepFlag = 1.0d;
                        apdr.stepLength = 0.0d;
                        apdr.utctime = System.currentTimeMillis() + System.currentTimeMillis();
                        apdr.mag[3] = 1.0d;
                        apdr.movingStatus = LppAggregator.this.mStatus[new Random().nextInt(LppAggregator.this.mStatus.length)];
                        apdr.carryPos = 0;
                        list.add(apdr);
                    }
                    LppAggregator.this.mLPPFusion.notifyApdrData(list);
                }
                LppAggregator.this.sendApdr.sendEmptyMessageDelayed(LppAggregator.NEXT_APDR, 25000);
            }
        };
        this.sendApdr.sendEmptyMessageDelayed(NEXT_APDR, DateUtils.MINUTE_IN_MILLIS);
    }

    public final void disable() {
        CaLogger.trace();
        new Handler(super.getLooper()).postDelayed(new Runnable() {
            public void run() {
                if (LppAggregator.this.mLPPFusion != null) {
                    LppAggregator.this.mLPPFusion.stopLpp();
                }
            }
        }, 0);
    }

    protected void display() {
    }

    private void notifyPositionContext(ArrayList<Location> listLPPResult) {
        Log.d(TAG, "notifyPositionContext");
        Time tim = new Time();
        int cnt = 0;
        int size = listLPPResult.size();
        this.timestamp = new long[size];
        this.latitude = new double[size];
        this.longitude = new double[size];
        this.altitude = new double[size];
        for (int i = 0; i < listLPPResult.size(); i++) {
            Location loc = (Location) listLPPResult.get(i);
            tim.set(loc.getTime());
            tim.switchTimezone("GMT+00:00");
            this.timestamp[i] = loc.getTime();
            this.latitude[i] = loc.getLatitude();
            this.longitude[i] = loc.getLongitude();
            this.altitude[i] = loc.getAltitude();
            cnt++;
        }
        notifyLppContext(cnt);
    }

    private long convertToUtc(int hour, int minute, int second) {
        Time syncT = this.mApdrRunner.getSyncTime();
        Log.d(TAG, "syncT:" + syncT);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time1 = syncT.hour + ":" + syncT.minute + ":" + syncT.second;
        String time2 = hour + ":" + minute + ":" + second;
        Date syncDate = null;
        Date rxDate = null;
        try {
            syncDate = format.parse(time1);
            rxDate = format.parse(time2);
            Log.d(TAG, "syncDate:" + syncDate);
            Log.d(TAG, "rxDate:" + rxDate);
            if (syncDate.after(rxDate)) {
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(rxDate);
                cal2.add(5, 1);
                rxDate = cal2.getTime();
            }
        } catch (ParseException e) {
            Log.e(TAG, "time parse error");
            e.printStackTrace();
        }
        long lapse = 0;
        if (rxDate != null) {
            lapse = rxDate.getTime() - syncDate.getTime();
        }
        Log.d(TAG, "lapse:" + lapse);
        if (lapse < 0) {
            Log.e(TAG, "lapse is -ve");
        }
        if (lapse > 43200000) {
            Log.e(TAG, "lapse is more than 12 hours");
        }
        long lapHr = TimeUnit.MILLISECONDS.toHours(lapse);
        long lapMin = TimeUnit.MILLISECONDS.toMinutes(lapse - (((60 * lapHr) * 60) * 1000));
        long lapSec = (lapse - ((((60 * lapHr) * 60) * 1000) + ((60 * lapMin) * 1000))) / 1000;
        Log.d(TAG, "lapHr:" + lapHr + " lapMin:" + lapMin + " lapSec:" + lapSec);
        Time rxTime = new Time(syncT);
        rxTime.hour = (int) (((long) rxTime.hour) + lapHr);
        rxTime.minute = (int) (((long) rxTime.minute) + lapMin);
        rxTime.second = (int) (((long) rxTime.second) + lapSec);
        Log.d(TAG, "rxTime:" + rxTime);
        return rxTime.toMillis(false);
    }

    private long convertToUtc2(int hour, int min, int sec) {
        return System.currentTimeMillis();
    }
}
