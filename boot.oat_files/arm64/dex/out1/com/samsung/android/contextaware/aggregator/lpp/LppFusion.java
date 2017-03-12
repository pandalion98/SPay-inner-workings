package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Message;
import android.util.Log;
import android.view.InputDevice;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.samsung.android.contextaware.aggregator.lpp.algorithm.LppAlgorithm;
import com.samsung.android.contextaware.aggregator.lpp.log.LppLogManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LppFusion extends Thread {
    private static final int PASSIVE_LOC_COLL_FREQ = 60;
    private static final String TAG = "LppFusion";
    private static final long locReqType = 0;
    static final StateMsg[] vals = StateMsg.values();
    String LogFromAPDR = "0   0   0   0   0   0";
    String LogFromLocM = "0   0   0   0";
    private long apdrStepNumber = 100;
    boolean flagGPSAlwaysOn = false;
    private long locRequestInterval = 45;
    private final BlockingQueue<QData> lppQ = new ArrayBlockingQueue(32);
    private final LppConfig mCfg;
    private final LocManListener mLMLnr = new LocManListener();
    private final LppAlgoLnr mLPPAloLnr = new LppAlgoLnr();
    private final ArrayList<Location> mListGPSPos = new ArrayList();
    private final ArrayList<Location> mListLPPPos = new ArrayList();
    private ILppDataProvider mListener;
    private final LppLogManager mLogManager = new LppLogManager();
    private final LppAlgorithm mLppAlgo = new LppAlgorithm();
    private final LppLocationManager mLppLm = new LppLocationManager();
    private LppFusionSM mStateMachine = null;
    private boolean sendBrFlag = false;
    String strAlgo = "\n<<LPosition>>\n\n";
    String strLM = "\n<<LoManager>>\n\n";

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg = new int[QMsg.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg = new int[StateMsg.values().length];

        static {
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_SLEEP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_APDR_NOTI.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_LPPA_STOP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_APDR_DATA_RXED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_LOCATION_LIST_RXED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_BATCH_LOC_LIST_RXED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_BATCH_LOC_RXED.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_PASS_LOC_RXED.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[QMsg.QMSG_PASS_LOC_BATCH_RXED.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.START.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_REQUEST.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal()] = 3;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_PASS_FOUND.ordinal()] = 4;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal()] = 5;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.STOP.ordinal()] = 6;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_FOUND.ordinal()] = 7;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_BATCH_FOUND.ordinal()] = 8;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[StateMsg.LOCATION_NOT_FOUND.ordinal()] = 9;
            } catch (NoSuchFieldError e18) {
            }
        }
    }

    private class LocManListener implements LppLocationManagerListener {
        private LocManListener() {
        }

        public void locUpdate(ArrayList<Location> listLoc) {
            LppFusion.this.mListGPSPos.addAll(new ArrayList(listLoc));
            LppFusion.this.addQ(QMsg.QMSG_LOCATION_LIST_RXED, listLoc);
            for (Location l : (Location[]) listLoc.toArray(new Location[listLoc.size()])) {
                LppFusion.this.updateLppFusionStatus("LppLocMan: GPS/NLP PosIn => " + LppFusion.this.LocationInfoString(l));
            }
        }

        public void logData(String str) {
            LppFusion.this.mLogManager.LogData(5, str);
        }

        public void logNmeaData(String str) {
            LppFusion.this.mLogManager.LogData(8, str);
        }

        public void status(String str) {
            LppFusion.this.strLM = str;
            LppFusion.this.updateLppFusionStatus(str);
        }

        public void locationNotFound() {
            LppFusion.this.mStateMachine.sendMessage(StateMsg.LOCATION_NOT_FOUND.ordinal());
        }

        public void batchLocListUpdate(ArrayList<Location> listLoc) {
            LppFusion.this.addQ(QMsg.QMSG_BATCH_LOC_LIST_RXED, listLoc);
            Iterator i$ = listLoc.iterator();
            while (i$.hasNext()) {
                Location loc1 = (Location) i$.next();
                if (loc1 != null) {
                    LppFusion.this.updateLppFusionStatus("LppLocMan: Batch PosIn => " + LppFusion.this.LocationInfoString(loc1));
                }
            }
        }

        public void batchLocUpdate(Location loc) {
            LppFusion.this.addQ(QMsg.QMSG_BATCH_LOC_RXED, loc);
        }

        public void locPassUpdate(Location loc) {
            LppFusion.this.addQ(QMsg.QMSG_PASS_LOC_RXED, loc);
            if (loc != null) {
                LppFusion.this.updateLppFusionStatus("LppLocMan: Passive PosIn => " + LppFusion.this.LocationInfoString(loc));
            }
        }

        public void locPassBatchUpdate(Location loc) {
            LppFusion.this.addQ(QMsg.QMSG_PASS_LOC_BATCH_RXED, loc);
            if (loc != null) {
                LppFusion.this.updateLppFusionStatus("LppLocMan: Passive Batch PosIn => " + LppFusion.this.LocationInfoString(loc));
            }
        }

        public void gpsBatchStarted() {
            LppFusion.this.mListener.gpsBatchStarted();
            LppFusion.this.mLppAlgo.setGPSBatchingStatus(true);
        }

        public void gpsBatchStopped() {
            LppFusion.this.mLppAlgo.setGPSBatchingStatus(false);
        }

        public void gpsOnBatchStopped() {
            LppFusion.this.mListener.gpsOnBatchStopped();
        }

        public void gpsOffBatchStopped() {
            LppFusion.this.mListener.gpsOffBatchStopped();
        }

        public void gpsAvailable() {
            LppFusion.this.mListener.gpsAvailable();
        }

        public void gpsUnavailable() {
            LppFusion.this.mListener.gpsUnavailable();
        }
    }

    private class LppAlgoLnr implements LppAlgoListener {
        private LppAlgoLnr() {
        }

        public void onUpdate(Location loc) {
            Log.d(LppFusion.TAG, "LppAlgoLnr: onUpdate");
        }

        public void logData(int target, String str) {
            LppFusion.this.mLogManager.LogData(target, str);
        }

        public void status(String str) {
            LppFusion.this.strAlgo = str;
            LppFusion.this.updateLppFusionStatus(str);
        }

        public void onUpdateLPPtraj(ArrayList<LppLocation> listLppLocation) {
            Log.d(LppFusion.TAG, "onUpdateLPPtraj");
            for (int inx = 0; inx < listLppLocation.size(); inx++) {
                LppFusion.this.mListLPPPos.add(((LppLocation) listLppLocation.get(inx)).getLoc());
            }
            LppFusion.this.mListener.lppUpdate(LppFusion.this.mListLPPPos);
            LppFusion.this.mLogManager.AddCoordinate(LppFusion.this.mListLPPPos);
            LppFusion.this.mLogManager.AddGPSCoordinate(LppFusion.this.mListGPSPos);
            LppFusion.this.mListLPPPos.clear();
            LppFusion.this.mListGPSPos.clear();
        }

        public void requestLoc() {
        }
    }

    private class LppFusionSM extends StateMachine {
        private LFIdleState mIdleState = null;
        private LFWaitLocState mWaitLocState = null;

        class LFIdleState extends State {
            LFIdleState() {
            }

            public void enter() {
                Log.d(LppFusion.TAG, "Entering " + getName());
            }

            public boolean processMessage(Message message) {
                Log.d(LppFusion.TAG, "Handling message " + LppFusion.vals[message.what] + " in " + getName());
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[LppFusion.vals[message.what].ordinal()]) {
                    case 1:
                        LppFusion.this.mLogManager.start();
                        LppFusion.this.mLppLm.start(LppFusion.this.mCfg, LppFusion.this.mLMLnr);
                        LppFusion.this.mLppAlgo.start();
                        LppFusion.this.mListLPPPos.clear();
                        LppFusion.this.mListGPSPos.clear();
                        break;
                    case 2:
                        LppFusion.this.mLppLm.locRequest(message.arg1);
                        LppFusion.this.mLogManager.LogData(6, "LPPAlgoLnr\t Location is requested");
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mWaitLocState);
                        break;
                    case 3:
                        ArrayList<Location> listLoc = message.obj;
                        Log.d(LppFusion.TAG, "batch loc list size:" + listLoc.size());
                        for (Location l : (Location[]) listLoc.toArray(new Location[listLoc.size()])) {
                            LppFusion.this.mLppAlgo.deliverLocationData(new Location(l));
                        }
                        LppFusionSM.this.goToSleep();
                        break;
                    case 4:
                        LppFusion.this.mLppAlgo.deliverLocationData(message.obj);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 5:
                        LppFusionSM.this.goToSleep();
                        break;
                    case 6:
                        LppFusion.this.mLppLm.stop();
                        LppFusion.this.mLppAlgo.stop();
                        LppFusion.this.mLogManager.stop();
                        LppFusion.this.mStateMachine.exit();
                        break;
                    default:
                        Log.d(LppFusion.TAG, "Msg not handled");
                        return false;
                }
                return true;
            }
        }

        class LFWaitLocState extends State {
            LFWaitLocState() {
            }

            public void enter() {
                Log.d(LppFusion.TAG, "Entering " + getName());
            }

            public boolean processMessage(Message message) {
                Log.d(LppFusion.TAG, "Handling message " + LppFusion.vals[message.what] + " in " + getName());
                ArrayList<Location> listLoc;
                switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$StateMsg[LppFusion.vals[message.what].ordinal()]) {
                    case 3:
                        listLoc = (ArrayList) message.obj;
                        Log.d(LppFusion.TAG, "batch loc list size:" + listLoc.size());
                        for (Location l : (Location[]) listLoc.toArray(new Location[listLoc.size()])) {
                            LppFusion.this.mLppAlgo.deliverLocationData(new Location(l));
                        }
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 6:
                        LppFusion.this.mLppLm.stop();
                        LppFusion.this.mLppAlgo.stop();
                        LppFusion.this.mLogManager.stop();
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusion.this.mStateMachine.exit();
                        break;
                    case 7:
                        LppFusion.this.mLogManager.LogData(6, "LocManListener\t Location is delivered to Algo");
                        listLoc = message.obj;
                        Log.d(LppFusion.TAG, "loc list size:" + listLoc.size());
                        for (Location l2 : (Location[]) listLoc.toArray(new Location[listLoc.size()])) {
                            LppFusion.this.mLppAlgo.deliverLocationData(new Location(l2));
                        }
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 8:
                        LppFusion.this.mListener.onLocationChanged((Location) message.obj);
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 9:
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    default:
                        Log.d(LppFusion.TAG, "Msg not handled");
                        return false;
                }
                return true;
            }

            public void exit() {
                Log.d(LppFusion.TAG, "Exiting " + getName());
            }
        }

        protected LppFusionSM(String name) {
            super(name);
            Log.d(LppFusion.TAG, "Creating State Machine");
            this.mIdleState = new LFIdleState();
            addState(this.mIdleState);
            this.mWaitLocState = new LFWaitLocState();
            addState(this.mWaitLocState);
            setInitialState(this.mIdleState);
        }

        public void exit() {
            quit();
        }

        private void goToSleep() {
            synchronized (this) {
                Log.d(LppFusion.TAG, "goToSleep");
                if (LppFusion.this.lppQ.size() == 0 && !smHasMessages()) {
                    LppFusion.this.addQ(QMsg.QMSG_SLEEP);
                }
            }
        }

        private boolean smHasMessages() {
            return LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_BATCH_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_PASS_FOUND.ordinal());
        }
    }

    private static class QData {
        private ArrayList<ApdrData> listAPDR;
        private ArrayList<Location> listLoc;
        private Location loc;
        private QMsg msgid;

        <E> QData(QMsg id, E value) {
            this.msgid = id;
            if (value != null) {
                if (id == QMsg.QMSG_APDR_DATA_RXED) {
                    this.listAPDR = new ArrayList();
                    Iterator i$ = ((ArrayList) value).iterator();
                    while (i$.hasNext()) {
                        this.listAPDR.add(new ApdrData((ApdrData) i$.next()));
                    }
                } else if (id == QMsg.QMSG_PASS_LOC_RXED || id == QMsg.QMSG_PASS_LOC_BATCH_RXED || id == QMsg.QMSG_BATCH_LOC_RXED) {
                    this.loc = (Location) value;
                } else if (id == QMsg.QMSG_BATCH_LOC_LIST_RXED || id == QMsg.QMSG_LOCATION_LIST_RXED) {
                    Location[] larray = (Location[]) ((ArrayList) value).toArray(new Location[((ArrayList) value).size()]);
                    this.listLoc = new ArrayList();
                    for (Location l : larray) {
                        this.listLoc.add(new Location(l));
                    }
                }
            }
        }
    }

    private enum QMsg {
        QMSG_APDR_NOTI(4096),
        QMSG_LPPA_PAUSE(4097),
        QMSG_LPPA_RESUME(InputDevice.SOURCE_TOUCHSCREEN),
        QMSG_LPPA_STOP(4099),
        QMSG_SLEEP(4100),
        QMSG_APDR_DATA_RXED(4101),
        QMSG_LOCATION_LIST_RXED(4102),
        QMSG_BATCH_LOC_LIST_RXED(4103),
        QMSG_BATCH_LOC_RXED(4104),
        QMSG_PASS_LOC_RXED(4105),
        QMSG_PASS_LOC_BATCH_RXED(4106);
        
        private int value;

        private QMsg(int val) {
            this.value = val;
        }
    }

    private enum StateMsg {
        START,
        LOCATION_REQUEST,
        LOCATION_FOUND,
        LOCATION_BATCH_LIST_FOUND,
        LOCATION_BATCH_FOUND,
        LOCATION_PASS_FOUND,
        LOCATION_PASS_IN_BATCH_FOUND,
        LOCATION_NOT_FOUND,
        STOP
    }

    public LppFusion(LppConfig config) {
        Log.v(TAG, TAG);
        this.mLogManager.init(config);
        this.mLppAlgo.init(this.mLPPAloLnr);
        this.apdrStepNumber = (long) config.GPSRequest_APDR;
        this.locRequestInterval = (long) config.GPSRequest_Timer;
        if (this.locRequestInterval == 0) {
            this.flagGPSAlwaysOn = true;
        } else {
            this.flagGPSAlwaysOn = false;
        }
        this.mCfg = new LppConfig(config);
    }

    public void run() {
        Log.v(TAG, "run");
        setName("LPPThread");
        this.mStateMachine = new LppFusionSM(TAG);
        this.mStateMachine.start();
        this.mStateMachine.sendMessage(StateMsg.START.ordinal());
        waitOnQ();
    }

    public void stopLpp() {
        Log.d(TAG, "LPP stop!");
        addQ(QMsg.QMSG_LPPA_STOP);
    }

    public void pauseLPP() {
        Log.v(TAG, "pause()");
        this.mLogManager.LogData(6, "LppFusion, pause()");
    }

    public void resumeLPP() {
        Log.v(TAG, "resume()");
        this.mLogManager.LogData(6, "LppFusion, resume()");
    }

    public void registerListener(ILppDataProvider lnr) {
        this.mListener = lnr;
        this.mLogManager.setILppDataProviderListener(lnr);
    }

    public void setLppResolution(int res) {
        if (this.mLppLm != null) {
            this.mLppLm.setLppResolution(res);
        }
        Debug_LogString("set property command from APP : " + res);
    }

    public void Debug_LogString(String str) {
        this.mLogManager.LogData(6, str);
    }

    private void threadSleep() {
        try {
            Log.i(TAG, "Sleep!");
            sleep(Long.MAX_VALUE);
            Log.d(TAG, "Out of Sleep!");
        } catch (InterruptedException e) {
            Log.d(TAG, "Out of Sleep! 2");
        }
    }

    private void waitOnQ() {
        boolean polling = true;
        while (polling) {
            try {
                synchronized (this) {
                    QData qdata = (QData) this.lppQ.take();
                    Log.d(TAG, "Received msg:" + qdata.msgid + " in Q:" + this.lppQ.size());
                    switch (AnonymousClass1.$SwitchMap$com$samsung$android$contextaware$aggregator$lpp$LppFusion$QMsg[qdata.msgid.ordinal()]) {
                        case 1:
                            threadSleep();
                            break;
                        case 2:
                            break;
                        case 3:
                            polling = false;
                            this.mStateMachine.sendMessage(StateMsg.STOP.ordinal());
                            break;
                        case 4:
                            Log.d(TAG, "size of APDR data " + qdata.listAPDR.size());
                            int inx = 0;
                            while (inx < qdata.listAPDR.size()) {
                                updateLppFusionStatus("Location request MovingStatus " + ((ApdrData) qdata.listAPDR.get(inx)).movingStatus);
                                this.apdrStepNumber = 0;
                                if (((long) (inx + 1)) > this.apdrStepNumber || ((ApdrData) qdata.listAPDR.get(inx)).movingStatus == 4) {
                                    Log.d(TAG, "Location request");
                                    this.mStateMachine.sendMessage(StateMsg.LOCATION_REQUEST.ordinal(), ((ApdrData) qdata.listAPDR.get(inx)).movingStatus);
                                    this.mLppAlgo.deliverAPDRData(qdata.listAPDR);
                                    break;
                                }
                                inx++;
                            }
                            this.mLppAlgo.deliverAPDRData(qdata.listAPDR);
                            break;
                        case 5:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_FOUND.ordinal(), (Object) qdata.listLoc);
                            break;
                        case 6:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal(), (Object) qdata.listLoc);
                            break;
                        case 7:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_BATCH_FOUND.ordinal(), (Object) qdata.loc);
                            break;
                        case 8:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_PASS_FOUND.ordinal(), (Object) qdata.loc);
                            break;
                        case 9:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal(), (Object) qdata.loc);
                            break;
                        default:
                            Log.e(TAG, "unspecified msg id");
                            break;
                    }
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "IE in q");
                e.printStackTrace();
            }
        }
        Log.d(TAG, "polling stopped");
    }

    private void addQ(QMsg msgid) {
        addQ(msgid, null);
    }

    private <E> void addQ(QMsg msgid, E value) {
        Log.d(TAG, "addQ:" + msgid);
        boolean res = this.lppQ.offer(new QData(msgid, value));
        if (msgid == QMsg.QMSG_APDR_DATA_RXED && value != null) {
            Log.d(TAG, "addQ, array size: " + ((ArrayList) value).size() + " res:" + res);
        }
        if (msgid != QMsg.QMSG_SLEEP && getState() == Thread.State.TIMED_WAITING) {
            interrupt();
        }
    }

    public void notifyApdrData(ArrayList<ApdrData> apdrList) {
        Log.d(TAG, "notifyApdrData");
        for (int inx = 0; inx < apdrList.size(); inx++) {
            this.mLogManager.LogData(6, "APDR data from sensor Hub - moving status : " + ((ApdrData) apdrList.get(inx)).movingStatus);
        }
        addQ(QMsg.QMSG_APDR_DATA_RXED, apdrList);
    }

    public void notifyStayArea(int status) {
        if (this.mLppAlgo != null) {
            this.mLppAlgo.setStayingAreaFlag(status);
        }
    }

    private void updateLppFusionStatus(String str) {
        if (this.mCfg == null || !this.sendBrFlag) {
            Log.e(TAG, "Config is null!");
            return;
        }
        Log.d(TAG, str);
        Context context_ = this.mCfg.getContext();
        if (context_ != null) {
            Intent intent = new Intent("android.hardware.contextaware.aggregator.lpp.LppFusion").putExtra(TAG, str);
            if (intent != null) {
                context_.sendBroadcast(intent);
                Log.d(TAG, "Intent sent");
                return;
            }
            Log.d(TAG, "Intent creation failed!");
            return;
        }
        Log.e(TAG, "Context is null");
    }

    private String LocationInfoString(Location loc) {
        String str = "";
        if (loc != null) {
            return "Time : " + loc.getTime() + " Pos : " + loc.getProvider() + " , " + loc.getLatitude() + " , " + loc.getLongitude() + " , " + loc.getAltitude() + " , " + loc.getAccuracy() + " , " + loc.getBearing() + " , " + loc.getSpeed();
        }
        return str;
    }

    public LppLogManager getLogHandle() {
        return this.mLogManager;
    }

    public void sendStatusEnable() {
        this.sendBrFlag = true;
    }

    public void sendStatusDisable() {
        this.sendBrFlag = false;
    }
}
