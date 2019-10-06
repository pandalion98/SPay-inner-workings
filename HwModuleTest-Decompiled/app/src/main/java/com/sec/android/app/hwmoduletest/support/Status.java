package com.sec.android.app.hwmoduletest.support;

import com.goodix.cap.fingerprint.utils.TestResultParser;

public enum Status {
    NOTEST(-1),
    PASS(0),
    FAIL(1),
    FAIL_CAL(2),
    FAIL_TOUCH(3),
    FAIL_SPECOUT(4),
    FAIL_SCAN(5),
    FAIL_TIMEOUT(6),
    FAIL_CALLBACK_TIMEOUT(7),
    FAIL_SENSORID(8),
    FAIL_INIT(9),
    FAIL_INT1(10),
    FAIL_INT2(11),
    FAIL_CONTAMINATION(12),
    READY(100),
    RUNNING(200),
    STOP(TestResultParser.TEST_TOKEN_AVG_DIFF_VAL),
    ERROR(1000);
    
    private final int status;

    private Status(int status2) {
        this.status = status2;
    }
}
