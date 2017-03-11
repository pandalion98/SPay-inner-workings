package com.samsung.contextservice.exception;

import com.samsung.contextservice.p029b.CSlog;

public class InitializationException extends RuntimeException {
    public InitializationException(String str) {
        super(str);
        CSlog.m1409e("EXCEPTION", str);
    }
}
