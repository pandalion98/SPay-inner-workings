package com.qualcomm.qti.biometrics.fingerprint;

import java.util.ArrayList;

public class BGEInfo {
    public int binIndexToReplace;
    public ArrayList<BinData> currentBins;
    public int currentTemp;

    public BGEInfo() {
        this.currentBins = new ArrayList<>();
        this.currentTemp = 0;
        this.binIndexToReplace = -1;
    }

    public BGEInfo(ArrayList<BinData> currentBins2, int currentTemp2, int binIndexToReplace2) {
        this.currentBins = currentBins2;
        this.currentTemp = currentTemp2;
        this.binIndexToReplace = binIndexToReplace2;
    }

    public void addBin(BinData newBin) {
        this.currentBins.add(newBin);
    }

    public void setCurrentTemp(int currentTemp2) {
        this.currentTemp = currentTemp2;
    }

    public void setBinIndexToReplace(int binIndexToReplace2) {
        this.binIndexToReplace = binIndexToReplace2;
    }

    public ArrayList<BinData> getCurrentBins() {
        return this.currentBins;
    }

    public int getCurrentTemp() {
        return this.currentTemp;
    }

    public int getBinIndexToReplace() {
        return this.binIndexToReplace;
    }
}
