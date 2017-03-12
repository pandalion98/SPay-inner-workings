package com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ICurrentPositionRequestObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.REQUEST_DATA;
import com.samsung.android.contextaware.manager.ICurrrentPositionObserver;
import com.samsung.android.contextaware.utilbundle.CaCurrentPositionManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CurrentPositionRequestRunner implements ISensorHubRequestParser, ICurrrentPositionObserver, ICurrentPositionRequest {
    private static final int LOCATION_MODE_SLOCATION = 2;
    private final CaCurrentPositionManager mCurrentPosition;
    private ICurrentPositionRequestObserver mListener;
    private final Position mPosition = new Position();

    public static class Position {
        private float accuracy;
        private double altitude;
        private double distance;
        private double latitude;
        private double longitude;
        private int satelliteCount;
        private float speed;
        private int type;
        private int[] utcTime;

        public int getType() {
            return this.type;
        }

        public int[] getUtcTime() {
            return this.utcTime;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public double getAltitude() {
            return this.altitude;
        }

        public double getDistance() {
            return this.distance;
        }

        public float getSpeed() {
            return this.speed;
        }

        public float getAccuracy() {
            return this.accuracy;
        }

        public int getSatelliteCount() {
            return this.satelliteCount;
        }
    }

    public CurrentPositionRequestRunner(Context context, Looper looper) {
        this.mCurrentPosition = new CaCurrentPositionManager(context, looper, this);
    }

    public final int getRequestType() {
        return REQUEST_DATA.REQUEST_CURRENT_POSITION.value;
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        this.mCurrentPosition.enable(2, packet[tmpNext] * 10);
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    public final void updateCurrentPosition(int type, int[] utcTime, double latitude, double longitude, double altitude, double distance, float speed, float accuracy, int satelliteCount) {
        this.mPosition.type = type;
        this.mPosition.utcTime = utcTime;
        this.mPosition.latitude = latitude;
        this.mPosition.longitude = longitude;
        this.mPosition.altitude = altitude;
        this.mPosition.distance = distance;
        this.mPosition.speed = speed;
        this.mPosition.accuracy = accuracy;
        this.mPosition.satelliteCount = satelliteCount;
        notifyObserver(this.mPosition);
    }

    public Position getPosition() {
        return this.mPosition;
    }

    public final void registerObserver(ICurrentPositionRequestObserver observer) {
        this.mListener = observer;
    }

    public final void unregisterObserver() {
        this.mListener = null;
    }

    public final void notifyObserver(Position position) {
        if (this.mListener != null) {
            this.mListener.updatePosition(position);
        }
    }
}
