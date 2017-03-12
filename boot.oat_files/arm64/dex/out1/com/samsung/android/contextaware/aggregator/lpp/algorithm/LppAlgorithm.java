package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import android.location.Location;
import android.text.format.Time;
import android.util.Log;
import com.samsung.android.contextaware.aggregator.lpp.ApdrData;
import com.samsung.android.contextaware.aggregator.lpp.LppAlgoListener;
import com.samsung.android.contextaware.aggregator.lpp.LppLocation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class LppAlgorithm {
    int APDRMAXNUMBER = 500;
    LppLocation AlgoLocP = new LppLocation();
    LppLocation AlgoLocPP = new LppLocation();
    LppLocation AlgoLocPPP = new LppLocation();
    long Cnt_SameLocSet = 0;
    LppLocation CurrSetLoc = new LppLocation();
    LppLocation CurrentLoc = new LppLocation();
    LppLocation FilterdOldLoc = new LppLocation();
    LppLocation FilterdOlderLoc = new LppLocation();
    boolean Flag_FineLocAcquired = false;
    boolean Flag_First_loc = false;
    boolean Flag_Loc_init = false;
    LppLocation LastLMLoc;
    final double MAX_MOVEMENT_SPEED_RUN = 2.0d;
    final double MAX_MOVEMENT_SPEED_STATIONARY = 1.0d;
    final double MAX_MOVEMENT_SPEED_VEHICLE = 20.0d;
    final double MAX_MOVEMENT_SPEED_WALK = 1.0d;
    final double MIN_MOVEMENT_DISTANCE = 20.0d;
    final double MIN_UPDTAE_TIME = 5000.0d;
    LppLocation OldLoc = new LppLocation();
    double[] Origin_LLH = new double[]{1.0d, 1.0d, 100.0d};
    final double POS_SET_RADIUS = 5.0d;
    final double POS_SET_TIME = 2.0d;
    KalmanFilter PosKF = new KalmanFilter(3, 3);
    final double Position_Jump_Sec_TH = 10.0d;
    double Prev_StateTime = 0.0d;
    double[] ProcessNoisePerSecondVehicle = new double[]{10.0d, 10.0d, 0.5d};
    double[] ProcessNoisePerSecondWalk = new double[]{1.0d, 1.0d, 0.5d};
    final int RST_LOC_VALID_CHK_NEWSET = 1;
    final int RST_LOC_VALID_CHK_NOUPDATE = 0;
    final int RST_LOC_VALID_CHK_STATIONARY = 2;
    final int STATE_STATIONARY = 1;
    final int STATE_UNKNWON = 0;
    final int STATE_VEHICLE = 4;
    String TAG = "LppAlgorithm";
    final long Trajectory_Time_Gap = 20;
    final int _DATAFROMAPDR = 0;
    final int _DATAFROMGPS = 1;
    final int _DATAFROMNETWORK = 2;
    boolean flag_AlgorithmOn = false;
    int lastStatus = 1;
    long lastTrajTime = 0;
    public ArrayList<ApdrData> mAPDRResults = new ArrayList();
    public ArrayList<ApdrData> mAPDRStack = new ArrayList();
    boolean mFlagIsGPSBatchMode = false;
    boolean mFlagLocInputReady = true;
    boolean mFlagStayingArea = false;
    private final ArrayList<LppLocation> mInputPosBuf = new ArrayList();
    private final ArrayList<LppLocation> mInputPosBufSync = new ArrayList();
    LppAlgoListener mLPPLnr = null;
    private final ArrayList<LppLocation> mLPPPosition = new ArrayList();
    long time_lastSent = 0;

    public void init(LppAlgoListener LPPLnr) {
        this.mLPPLnr = LPPLnr;
        Log.d(this.TAG, "init()");
        this.flag_AlgorithmOn = false;
        this.Flag_FineLocAcquired = false;
        PositionFilteringInit();
    }

    public void start() {
        this.flag_AlgorithmOn = true;
    }

    public void stop() {
        Log.d(this.TAG, "Stop - mLPPPosition size() : " + this.mLPPPosition.size());
        if (this.mLPPPosition.size() > 0 && this.mLPPLnr != null) {
            this.mLPPLnr.onUpdateLPPtraj(this.mLPPPosition);
            this.mLPPPosition.clear();
        }
        this.LastLMLoc = null;
        this.flag_AlgorithmOn = false;
    }

    private void resetwithLastLoc(Location lastloc) {
        if (lastloc == null) {
            Log.e(this.TAG, "resetwithLastLoc() unknown error - lastloc is null");
            return;
        }
        if (this.LastLMLoc == null) {
            this.LastLMLoc = new LppLocation(lastloc);
        } else {
            this.LastLMLoc.set(lastloc);
        }
        this.OldLoc.set(lastloc);
        this.Origin_LLH[0] = lastloc.getLatitude();
        this.Origin_LLH[1] = lastloc.getLongitude();
        this.Origin_LLH[2] = lastloc.getAltitude();
        this.OldLoc.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        this.CurrentLoc.set(this.OldLoc);
        this.mAPDRStack.clear();
        this.mAPDRStack.clear();
        this.mLPPPosition.clear();
        this.mInputPosBuf.clear();
        this.mInputPosBuf.add(new LppLocation(lastloc));
        this.mInputPosBufSync.clear();
        this.mFlagLocInputReady = true;
        this.lastTrajTime = 0;
        this.Cnt_SameLocSet = 0;
    }

    public void deliverAPDRData(ArrayList<ApdrData> listAPDR) {
        Log.d(this.TAG, "deliverAPDRData(), array size \t\t: " + listAPDR.size());
        ApdrData apdrD = new ApdrData();
        for (int inx = 0; inx < listAPDR.size(); inx++) {
            SendStatus("APDR time - " + ((ApdrData) listAPDR.get(inx)).utctime + " status - " + ((ApdrData) listAPDR.get(inx)).movingStatus + " step length - " + ((ApdrData) listAPDR.get(inx)).stepLength + " step heading - " + ((ApdrData) listAPDR.get(inx)).apdrHeading);
            apdrD.set((ApdrData) listAPDR.get(inx));
            this.mAPDRResults.add(new ApdrData(apdrD));
            if (apdrD.movingStatus == 1) {
                setStayingAreaFlag(1);
            }
        }
    }

    public void deliverLocationData(Location loc) {
        if (loc == null) {
            Log.e(this.TAG, "deliverLocationData - loc is null");
            return;
        }
        SendStatus("LppAlgorithm - PosIn => Time : " + loc.getTime() + " Pos : " + loc.getProvider() + " , " + loc.getLatitude() + " , " + loc.getLongitude() + " , " + loc.getAltitude() + " , " + loc.getAccuracy() + " , " + loc.getBearing() + " , " + loc.getSpeed());
        if (this.LastLMLoc == null) {
            resetwithLastLoc(loc);
        }
        this.mInputPosBufSync.add(new LppLocation(loc));
        if (this.mInputPosBufSync.size() > 100) {
            this.mInputPosBufSync.remove(0);
        }
        SendStatus("Size of mInputPosBufSync : " + this.mInputPosBufSync.size());
        SendStatus("Position filter Status - " + this.mFlagLocInputReady);
        if (this.mFlagLocInputReady) {
            this.mFlagLocInputReady = false;
            Iterator i$ = this.mInputPosBufSync.iterator();
            while (i$.hasNext()) {
                this.mInputPosBuf.add((LppLocation) i$.next());
            }
            this.mInputPosBufSync.clear();
            Collections.sort(this.mInputPosBuf, new Comparator<LppLocation>() {
                public int compare(LppLocation LppLoc1, LppLocation LppLoc2) {
                    if (LppLoc1.Capturedtime == LppLoc2.Capturedtime) {
                        return 0;
                    }
                    return LppLoc1.Capturedtime > LppLoc2.Capturedtime ? 1 : -1;
                }
            });
            if (this.mFlagIsGPSBatchMode) {
                SendStatus("Position Filtering hold - GPS batching mode , accumulated index : " + this.mInputPosBuf.size());
            } else {
                boolean Flag_Stationary = false;
                i$ = this.mInputPosBuf.iterator();
                while (i$.hasNext()) {
                    boolean Flag_LocOK;
                    LppLocation lpploca = (LppLocation) i$.next();
                    SendStatus("Fine Location Acq Flag : " + this.Flag_FineLocAcquired);
                    if (this.Flag_FineLocAcquired) {
                        switch (LocValidCheck(lpploca, this.LastLMLoc, true)) {
                            case 0:
                                Flag_LocOK = false;
                                break;
                            case 1:
                                Flag_LocOK = true;
                                break;
                            case 2:
                                Flag_LocOK = true;
                                Flag_Stationary = true;
                                break;
                            default:
                                Flag_LocOK = false;
                                break;
                        }
                    } else if (lpploca.getAccuracy() > 50.0f) {
                        SendStatus("LppAlgorithm - check Initial Fine location : false - accuracy is " + lpploca.getAccuracy());
                        Flag_LocOK = false;
                    } else {
                        SendStatus("LppAlgorithm - check Initial Fine location : OK - accuracy is " + lpploca.getAccuracy());
                        this.Flag_FineLocAcquired = true;
                        Flag_LocOK = true;
                    }
                    if (Flag_LocOK) {
                        LppAlgorithmCheckAndRun(lpploca);
                        if (Flag_Stationary) {
                            lpploca.getLoc().setTime(lpploca.getLoc().getTime() + 1000);
                            LppAlgorithmCheckAndRun(lpploca);
                        }
                        this.LastLMLoc.set(lpploca);
                    } else {
                        SendStatus("Flag_Loc false - loc time is " + lpploca.getTime());
                    }
                }
                this.mInputPosBuf.clear();
            }
            this.mFlagLocInputReady = true;
            return;
        }
        SendStatus("Position filter is not ready");
    }

    private void LppAlgorithmCheckAndRun(LppLocation lpploca) {
        SendStatus("Flag_LocOK - loc time is " + lpploca.getTime());
        if (this.OldLoc.getLoc().getProvider().equals("NOPROVIDER")) {
            this.OldLoc.set(lpploca);
        }
        boolean flag_goAlgo = false;
        this.Cnt_SameLocSet++;
        if (10000.0d + ((double) this.lastTrajTime) < ((double) lpploca.getTime())) {
            flag_goAlgo = true;
            this.Cnt_SameLocSet = 0;
        } else if (this.Cnt_SameLocSet > 2) {
            flag_goAlgo = true;
        }
        if (flag_goAlgo) {
            ApdrData sample;
            this.CurrSetLoc.set(lpploca);
            ArrayList<ApdrData> mAPDRDeleteList = new ArrayList();
            Iterator i$ = this.mAPDRResults.iterator();
            while (i$.hasNext()) {
                sample = (ApdrData) i$.next();
                if (sample.utctime < this.OldLoc.getTime()) {
                    mAPDRDeleteList.add(sample);
                }
            }
            SendStatus("Delete ApdrData num : " + mAPDRDeleteList.size());
            if (mAPDRDeleteList.size() > 0) {
                this.mAPDRResults.removeAll(mAPDRDeleteList);
            }
            this.mAPDRStack.clear();
            i$ = this.mAPDRResults.iterator();
            while (i$.hasNext()) {
                sample = (ApdrData) i$.next();
                if (sample.utctime < this.CurrSetLoc.getTime()) {
                    this.mAPDRStack.add(new ApdrData(sample));
                }
            }
            if (this.flag_AlgorithmOn) {
                LppAlgorithmRun();
            }
            SendStatus("LppAlgorithmRun end");
            this.OldLoc.set(this.CurrSetLoc);
        }
    }

    private int LocValidCheck(LppLocation lpploc, LppLocation lpplocOld, boolean flag_fixPos) {
        double MAX_VELOCITY;
        ApdrData apdrCurrent = new ApdrData();
        ApdrData apdrOld = new ApdrData();
        Iterator i$ = this.mAPDRResults.iterator();
        while (i$.hasNext()) {
            ApdrData apdr = (ApdrData) i$.next();
            if (apdrCurrent.utctime < apdr.utctime && apdr.utctime < lpploc.getTime()) {
                apdrCurrent.set(apdr);
            }
            if (apdrOld.utctime < apdr.utctime && apdr.utctime < lpplocOld.getTime()) {
                apdrOld.set(apdr);
            }
        }
        int currStatus = apdrCurrent.movingStatus;
        if (currStatus == 1 || apdrOld.movingStatus == 1) {
            if (currStatus == 4 || apdrOld.movingStatus == 4) {
                MAX_VELOCITY = 20.0d;
            } else {
                MAX_VELOCITY = 1.0d;
            }
        } else if (currStatus == 2 || apdrOld.movingStatus == 2) {
            MAX_VELOCITY = 1.0d;
        } else if (currStatus == 3 || apdrOld.movingStatus == 3) {
            MAX_VELOCITY = 2.0d;
        } else {
            MAX_VELOCITY = 20.0d;
        }
        double dist_2_point = lpploc.distanceTo(lpplocOld);
        double timediff = ((double) (lpploc.getTime() - lpplocOld.getTime())) * 0.001d;
        double maxDistance = MAX_VELOCITY * timediff;
        if (currStatus == 2 && apdrOld.movingStatus == 2 && maxDistance > ((double) this.APDRMAXNUMBER) * 0.8d) {
            maxDistance = ((double) this.APDRMAXNUMBER) * 0.8d;
        }
        if (timediff <= 0.0d) {
            return 0;
        }
        if (lpploc.getLoc().getProvider().compareTo("gps") == 0) {
            return 1;
        }
        if (dist_2_point > maxDistance && lpploc.getLoc().getAccuracy() > 50.0f) {
            i$ = this.mAPDRResults.iterator();
            while (i$.hasNext()) {
                apdr = (ApdrData) i$.next();
                SendStatus("Loc Rejection : apdr time - " + apdr.utctime + " " + apdr.movingStatus);
            }
            SendStatus("Loc Rejection : currStatus - " + currStatus + " oldStatus: " + apdrOld.movingStatus);
            SendStatus("Loc Rejection : timediff - " + timediff + " MAX_VELOCITY: " + MAX_VELOCITY);
            SendStatus("Loc Rejection : dist - " + dist_2_point + " MaxDist: " + maxDistance);
            SendStatus("Loc Rejection :Too long distance, eliminate pos " + lpploc.getTime() + " , dist: " + dist_2_point + " MaxDist: " + maxDistance);
            return 0;
        } else if (dist_2_point > (20.0d * timediff) * 3.0d) {
            i$ = this.mAPDRResults.iterator();
            while (i$.hasNext()) {
                apdr = (ApdrData) i$.next();
                SendStatus("Loc Rejection : apdr time - " + apdr.utctime + " " + apdr.movingStatus);
            }
            SendStatus("Loc Rejection : currStatus - " + currStatus + " oldStatus: " + apdrOld.movingStatus);
            SendStatus("Loc Rejection : timediff - " + timediff + " MAX_VELOCITY: " + MAX_VELOCITY);
            SendStatus("Loc Rejection : dist - " + dist_2_point + " MaxDist: " + maxDistance);
            SendStatus("Loc Rejection :Too long distance, eliminate pos " + lpploc.getTime() + " , dist: " + dist_2_point + " MaxDist: " + maxDistance);
            return 0;
        } else if (currStatus == 1) {
            return 2;
        } else {
            return 1;
        }
    }

    private void LppAlgorithmRun() {
        this.AlgoLocPPP.set(this.AlgoLocPP);
        this.AlgoLocPP.set(this.OldLoc);
        this.AlgoLocP.set(this.CurrSetLoc);
        if (this.AlgoLocPPP.getLoc().getProvider().equals("NOPROVIDER")) {
            SendStatus("LppAlgorithmRun - Initial update");
            return;
        }
        estimateSinglePoint();
        makeTrajectory();
        if (this.mLPPPosition.size() < 1) {
            SendStatus("ERROR: LppAlgorithmRun() unkwon error - [mLPPPosition.size() < 1]");
            return;
        }
        this.mLPPLnr.onUpdateLPPtraj(this.mLPPPosition);
        this.mLPPPosition.clear();
    }

    private void makeTrajectory() {
        double X_D1_2;
        double Y_D1_2;
        LppLocation loc1 = new LppLocation(this.FilterdOlderLoc);
        LppLocation lppLocation = new LppLocation(this.FilterdOldLoc);
        loc1.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        lppLocation.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        if (this.lastTrajTime == 0) {
            if (this.FilterdOlderLoc.getTime() == 0) {
                SendStatus("makeTrajectory : No need to update");
                return;
            }
            this.lastTrajTime = SetTrajStartTime(this.FilterdOlderLoc.getTime());
        }
        double time_diff = 20.0d - (((double) (loc1.getTime() - this.lastTrajTime)) * 0.001d);
        boolean dataValid = true;
        double t1 = ((double) loc1.getTime()) * 0.001d;
        double t2 = ((double) lppLocation.getTime()) * 0.001d;
        double t1_p1 = (t2 - t1) * 0.2d;
        double t1_p2 = (t2 - t1) * 0.8d;
        double del_t1_t2 = t2 - t1;
        if (del_t1_t2 <= 0.0d) {
            SendStatus("WARNING: makeTrajectory - abnormal t1, t2");
            dataValid = false;
            X_D1_2 = 0.0d;
            Y_D1_2 = 0.0d;
        } else if (del_t1_t2 > 300.0d) {
            SendStatus("WARNING: makeTrajectory - too much gap between t1 and t2");
            dataValid = false;
            X_D1_2 = 0.0d;
            Y_D1_2 = 0.0d;
        } else {
            X_D1_2 = (lppLocation.getPosEastLocal() - loc1.getPosEastLocal()) / (t2 - t1);
            Y_D1_2 = (lppLocation.getPosNorthLocal() - loc1.getPosNorthLocal()) / (t2 - t1);
        }
        double[] XY_D1_filtered = loc1.getFilteredVelocity();
        double[] XY_D2_filtered = lppLocation.getFilteredVelocity();
        if ((XY_D1_filtered[0] == 0.0d && XY_D1_filtered[1] == 0.0d) || (XY_D2_filtered[0] == 0.0d && XY_D2_filtered[1] == 0.0d)) {
            dataValid = false;
        }
        LppLocation LocOut;
        double t;
        if (dataValid) {
            double norm = Math.sqrt((X_D1_2 * X_D1_2) + (Y_D1_2 * Y_D1_2));
            double X1 = loc1.getPosEastLocal();
            double Y1 = loc1.getPosNorthLocal();
            double X2 = lppLocation.getPosEastLocal();
            double Y2 = lppLocation.getPosNorthLocal();
            double X_D2 = XY_D2_filtered[0] * norm;
            double Y_D2 = XY_D2_filtered[1] * norm;
            double e_x = XY_D1_filtered[0] * norm;
            double f_x = X1;
            double e_y = XY_D1_filtered[1] * norm;
            double f_y = Y1;
            vals = new double[4][];
            vals[0] = new double[]{(((5.0d * t1_p1) * t1_p1) * t1_p1) * t1_p1, ((4.0d * t1_p1) * t1_p1) * t1_p1, (3.0d * t1_p1) * t1_p1, 2.0d * t1_p1};
            vals[1] = new double[]{(((5.0d * t1_p2) * t1_p2) * t1_p2) * t1_p2, ((4.0d * t1_p2) * t1_p2) * t1_p2, (3.0d * t1_p2) * t1_p2, 2.0d * t1_p2};
            vals[2] = new double[]{(((del_t1_t2 * del_t1_t2) * del_t1_t2) * del_t1_t2) * del_t1_t2, ((del_t1_t2 * del_t1_t2) * del_t1_t2) * del_t1_t2, (del_t1_t2 * del_t1_t2) * del_t1_t2, del_t1_t2 * del_t1_t2};
            vals[3] = new double[]{(((5.0d * del_t1_t2) * del_t1_t2) * del_t1_t2) * del_t1_t2, ((4.0d * del_t1_t2) * del_t1_t2) * del_t1_t2, (3.0d * del_t1_t2) * del_t1_t2, 2.0d * del_t1_t2};
            Matrix A_mat = new Matrix(vals);
            double[][] valsX = new double[4][];
            valsX[0] = new double[]{X_D1_2 - e_x};
            valsX[1] = new double[]{X_D1_2 - e_x};
            valsX[2] = new double[]{(X2 - (e_x * del_t1_t2)) - f_x};
            valsX[3] = new double[]{X_D2 - e_x};
            Matrix matrix = new Matrix(valsX);
            double[][] valsY = new double[4][];
            valsY[0] = new double[]{Y_D1_2 - e_y};
            valsY[1] = new double[]{Y_D1_2 - e_y};
            valsY[2] = new double[]{(Y2 - (e_y * del_t1_t2)) - f_y};
            valsY[3] = new double[]{Y_D2 - e_y};
            matrix = new Matrix(valsY);
            Matrix X_coef = A_mat.inverse().times(matrix);
            Matrix Y_coef = A_mat.inverse().times(matrix);
            LocOut = new LppLocation(lppLocation);
            LocOut.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
            for (t = time_diff; t < del_t1_t2; t += 20.0d) {
                LocOut.setPosENU((((((((((X_coef.get(0, 0) * t) * t) * t) * t) * t) + ((((X_coef.get(1, 0) * t) * t) * t) * t)) + (((X_coef.get(2, 0) * t) * t) * t)) + ((X_coef.get(3, 0) * t) * t)) + (e_x * t)) + f_x, (((((((((Y_coef.get(0, 0) * t) * t) * t) * t) * t) + ((((Y_coef.get(1, 0) * t) * t) * t) * t)) + (((Y_coef.get(2, 0) * t) * t) * t)) + ((Y_coef.get(3, 0) * t) * t)) + (e_y * t)) + f_y, lppLocation.getAltitude());
                LocOut.getLoc().setTime(loc1.getTime() + ((long) (1000.0d * t)));
                this.mLPPPosition.add(new LppLocation(LocOut));
                this.lastTrajTime = LocOut.getTime();
            }
        } else if (del_t1_t2 > 900.0d) {
            this.mLPPPosition.add(new LppLocation(lppLocation));
            this.lastTrajTime = lppLocation.getTime();
        } else if (del_t1_t2 > 0.0d) {
            LocOut = new LppLocation(loc1);
            double[] EN1 = new double[2];
            EN2 = new double[2];
            double[] EN_new = new double[]{loc1.getPosEastLocal(), loc1.getPosNorthLocal()};
            EN2[0] = lppLocation.getPosEastLocal();
            EN2[1] = lppLocation.getPosNorthLocal();
            for (t = time_diff; t < del_t1_t2; t += 20.0d) {
                double ratio = t / del_t1_t2;
                EN_new[0] = EN1[0] + ((EN2[0] - EN1[0]) * ratio);
                EN_new[1] = EN1[1] + ((EN2[1] - EN1[1]) * ratio);
                LocOut.setPosENU(EN_new[0], EN_new[1], loc1.getAltitude());
                LocOut.getLoc().setTime(loc1.getTime() + ((long) (1000.0d * t)));
                this.mLPPPosition.add(new LppLocation(LocOut));
                this.lastTrajTime = LocOut.getTime();
            }
        }
    }

    private long SetTrajStartTime(long time) {
        Time dataLocalTime = new Time();
        dataLocalTime.set(time);
        return ((((long) (((double) time) / 1000.0d)) - ((long) dataLocalTime.second)) + (((long) dataLocalTime.second) - (((((long) dataLocalTime.second) / 20) - 1) * 20))) * 1000;
    }

    private void estimateSinglePoint() {
        LppLocation EstResult = null;
        if (!this.AlgoLocPP.getLoc().getProvider().equals("NOPROVIDER")) {
            LppLocation LocMeas = new LppLocation(this.AlgoLocPP);
            LocMeas.setMovingStatus(4);
            EstResult = PositionFiltering(LocMeas);
        }
        this.FilterdOlderLoc.set(this.FilterdOldLoc);
        if (EstResult != null) {
            this.FilterdOldLoc.set(this.AlgoLocPP);
        } else {
            SendStatus("EstimateSinglePoint(), abnormal filter output - null");
            this.FilterdOldLoc.set(this.AlgoLocPP);
        }
        this.CurrentLoc.set(this.AlgoLocP);
        this.FilterdOldLoc.estimateVelocity(this.FilterdOlderLoc, this.CurrentLoc);
    }

    private LppLocation PositionFiltering(LppLocation LocMeas) {
        LppLocation result = LocMeas;
        resultENU = new double[3];
        result.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        resultENU[0] = result.getPosEastLocal();
        resultENU[1] = result.getPosNorthLocal();
        resultENU[2] = result.getPosUpLocal();
        if (((resultENU[0] * resultENU[0]) + (resultENU[1] * resultENU[1])) + (resultENU[2] * resultENU[2]) > 1.410065408E9d) {
            this.Origin_LLH[0] = result.getLatitude();
            this.Origin_LLH[1] = result.getLongitude();
            this.Origin_LLH[2] = result.getAltitude();
            result.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
            PositionFilteringInit();
            this.PosKF.setInitialState(new double[]{result.getPosEastLocal(), result.getPosNorthLocal(), result.getPosUpLocal()});
            this.Prev_StateTime = (double) result.getTime();
            return result;
        }
        SendStatus("Measurement: E : " + result.getPosEastLocal() + " N " + result.getPosNorthLocal() + " U " + result.getPosNorthLocal());
        double delT = (((double) result.getTime()) - this.Prev_StateTime) * 0.001d;
        this.Prev_StateTime = (double) result.getTime();
        if (result.getMovingStatus() == 2) {
            mProcessNoise = new double[3][];
            mProcessNoise[0] = new double[]{this.ProcessNoisePerSecondWalk[0] * delT, 0.0d, 0.0d};
            mProcessNoise[1] = new double[]{0.0d, this.ProcessNoisePerSecondWalk[1] * delT, 0.0d};
            mProcessNoise[2] = new double[]{0.0d, 0.0d, this.ProcessNoisePerSecondWalk[2] * delT};
            if (!this.PosKF.setProcessNoise(mProcessNoise)) {
                Log.e(this.TAG, "PositionFiltering - Process noise error");
                return null;
            }
        }
        if (result.getMovingStatus() == 4) {
            if (!this.PosKF.setProcessNoise(new double[][]{new double[]{225.0d, 0.0d, 0.0d}, new double[]{225.0d, 100.0d, 0.0d}, new double[]{0.0d, 0.0d, 25.0d}})) {
                Log.e(this.TAG, "PositionFiltering - Process noise error");
                return null;
            } else if (!this.PosKF.TimePropagation(delT)) {
                Log.e(this.TAG, "PositionFiltering - TimePropagation error");
                return null;
            }
        }
        Meas = new double[3][];
        Meas[0] = new double[]{result.getPosEastLocal()};
        Meas[1] = new double[]{result.getPosNorthLocal()};
        Meas[2] = new double[]{result.getPosUpLocal()};
        Matrix matrix = new Matrix(Meas);
        double[][] MeasNoise;
        if (result.getAccuracy() >= 100.0f) {
            MeasNoise = new double[3][];
            MeasNoise[0] = new double[]{(double) (result.getAccuracy() * result.getAccuracy()), 0.0d, 0.0d};
            MeasNoise[1] = new double[]{0.0d, (double) (result.getAccuracy() * result.getAccuracy()), 0.0d};
            MeasNoise[2] = new double[]{0.0d, 0.0d, (double) ((result.getAccuracy() * 10.0f) * (result.getAccuracy() * 10.0f))};
            this.PosKF.setMeasurementNoise(MeasNoise);
        } else if (result.getLoc().getProvider().equals("network")) {
            MeasNoise = new double[3][];
            MeasNoise[0] = new double[]{(double) ((result.getAccuracy() / 20.0f) * (result.getAccuracy() / 20.0f)), 0.0d, 0.0d};
            MeasNoise[1] = new double[]{0.0d, (double) ((result.getAccuracy() / 20.0f) * (result.getAccuracy() / 20.0f)), 0.0d};
            MeasNoise[2] = new double[]{0.0d, 0.0d, 1.0E8d};
            this.PosKF.setMeasurementNoise(MeasNoise);
        } else {
            MeasNoise = new double[3][];
            MeasNoise[0] = new double[]{(double) ((result.getAccuracy() / 100.0f) * (result.getAccuracy() / 100.0f)), 0.0d, 0.0d};
            MeasNoise[1] = new double[]{0.0d, (double) ((result.getAccuracy() / 100.0f) * (result.getAccuracy() / 100.0f)), 0.0d};
            MeasNoise[2] = new double[]{0.0d, 0.0d, (double) ((result.getAccuracy() * 5.0f) * (result.getAccuracy() * 5.0f))};
            this.PosKF.setMeasurementNoise(MeasNoise);
        }
        if (this.PosKF.MeasurementUpdate(matrix)) {
            double[] LLH_updated = CoordinateTransform.enu2llh(this.PosKF.getCurrentState(), new double[]{(this.Origin_LLH[0] / 180.0d) * 3.141592d, (this.Origin_LLH[1] / 180.0d) * 3.141592d, this.Origin_LLH[2]});
            result.setLatitude((LLH_updated[0] / 3.141592d) * 180.0d);
            result.setLongitude((LLH_updated[1] / 3.141592d) * 180.0d);
            result.setAltitude(LLH_updated[2]);
            return result;
        }
        Log.e(this.TAG, "PositionFiltering - MeasurementUpdate error");
        return null;
    }

    private void PositionFilteringInit() {
        SendStatus("PositionFilteringInit()");
        this.PosKF.setInitialCovariance(new double[][]{new double[]{100.0d, 0.0d, 0.0d}, new double[]{0.0d, 100.0d, 0.0d}, new double[]{0.0d, 0.0d, 100.0d}});
        double[][] mMeasMatrix = new double[][]{new double[]{1.0d, 0.0d, 0.0d}, new double[]{0.0d, 1.0d, 0.0d}, new double[]{0.0d, 0.0d, 1.0d}};
        this.PosKF.setMeasurementMatrix(mMeasMatrix);
        this.PosKF.setInitialState(new double[]{0.0d, 0.0d, 0.0d});
        this.PosKF.setMeasurementNoise(new double[][]{new double[]{16.0d, 0.0d, 0.0d}, new double[]{0.0d, 16.0d, 0.0d}, new double[]{0.0d, 0.0d, 10000.0d}});
        this.PosKF.setProcessNoise(new double[][]{new double[]{200.0d, 0.0d, 0.0d}, new double[]{0.0d, 200.0d, 0.0d}, new double[]{0.0d, 0.0d, 2.0d}});
        this.PosKF.setTransitionMatrix(mMeasMatrix);
    }

    private void SendStatus(String str) {
        if (this.mLPPLnr != null) {
            this.mLPPLnr.status("LppAlgorithm: " + str);
        }
    }

    public void setGPSBatchingStatus(boolean flag) {
        this.mFlagIsGPSBatchMode = flag;
    }

    public void setStayingAreaFlag(int status) {
        this.mFlagStayingArea = true;
    }
}
