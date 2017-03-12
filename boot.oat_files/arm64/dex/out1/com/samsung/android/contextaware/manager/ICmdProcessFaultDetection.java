package com.samsung.android.contextaware.manager;

import android.os.Bundle;

public interface ICmdProcessFaultDetection {
    public static final int CMD_PACKET_FALSE = -777;
    public static final int I2C_COMM_ERROR = -5;
    public static final int INSUFFICIENT_REQUIREMENT = -20;
    public static final int NOT_RECEIVE_ACK = -11;
    public static final int SENSORHUBMANAGER_NULL_EXEPTION = -999;

    String getContextTypeOfFaultDetection();

    Bundle getFaultDetectionResult();

    String[] getFaultDetectionResultValueNames();
}
