package com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ICurrentPositionRequestObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.CurrentPositionRequestRunner.Position;

public interface ICurrentPositionRequest {
    void notifyObserver(Position position);

    void registerObserver(ICurrentPositionRequestObserver iCurrentPositionRequestObserver);

    void unregisterObserver();
}
