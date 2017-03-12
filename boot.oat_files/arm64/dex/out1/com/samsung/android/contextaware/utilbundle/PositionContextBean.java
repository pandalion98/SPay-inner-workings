package com.samsung.android.contextaware.utilbundle;

class PositionContextBean {
    static final int FUSED_TYPE = 3;
    static final int GPS_TYPE = 1;
    static final int NONE_TYPE = 0;
    static final int SLOCATION_TYPE = 4;
    static final int WPS_TYPE = 2;
    private float accuracy;
    private double altitude;
    private double distance;
    private double latitude;
    private double longitude;
    private int satelliteCount;
    private float speed;
    private int type;
    private int[] utcTime;

    PositionContextBean() {
        clearPosition();
    }

    PositionContextBean(float accuracy) {
        clearPosition();
        this.accuracy = accuracy;
    }

    final void clearPosition() {
        this.type = 0;
        this.utcTime = new int[3];
        this.latitude = 0.0d;
        this.longitude = 0.0d;
        this.altitude = 0.0d;
        this.distance = 0.0d;
        this.speed = 0.0f;
        this.accuracy = 1000.0f;
        this.satelliteCount = 0;
    }

    final void setPosition(int type, int[] utcTime, double latitude, double longitude, double altitude, double distance, float speed, float accuracy, int satelliteCount) {
        this.type = type;
        this.utcTime = utcTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.distance = distance;
        this.speed = speed;
        this.accuracy = accuracy;
        this.satelliteCount = satelliteCount;
    }

    final int getType() {
        return this.type;
    }

    final void setType(int type) {
        this.type = type;
    }

    final int[] getUtcTime() {
        return this.utcTime;
    }

    final void setUtcTime(int[] utcTime) {
        this.utcTime = utcTime;
    }

    final double getLatitude() {
        return this.latitude;
    }

    final void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    final double getLongitude() {
        return this.longitude;
    }

    final void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    final double getAltitude() {
        return this.altitude;
    }

    final void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    final double getDistance() {
        return this.distance;
    }

    final void setDistance(double distance) {
        this.distance = distance;
    }

    final float getSpeed() {
        return this.speed;
    }

    final void setSpeed(float speed) {
        this.speed = speed;
    }

    final float getAccuracy() {
        return this.accuracy;
    }

    final void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    final int getSatelliteCount() {
        return this.satelliteCount;
    }

    final void setSatelliteCount(int satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    static final double calculationDistance(double preLatitude, double preLongitude, double curLatitude, double curLongitude) {
        if (preLatitude < 0.0d || preLongitude < 0.0d) {
            return -1.0d;
        }
        return Math.sqrt(Math.pow(curLatitude - preLatitude, 2.0d) + Math.pow(curLongitude - preLongitude, 2.0d));
    }
}
