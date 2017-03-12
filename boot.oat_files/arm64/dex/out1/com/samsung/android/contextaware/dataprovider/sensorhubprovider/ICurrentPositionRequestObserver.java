package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.CurrentPositionRequestRunner.Position;

public interface ICurrentPositionRequestObserver {
    void updatePosition(Position position);
}
