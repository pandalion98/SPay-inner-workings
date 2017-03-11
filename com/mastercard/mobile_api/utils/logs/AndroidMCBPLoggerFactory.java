package com.mastercard.mobile_api.utils.logs;

public class AndroidMCBPLoggerFactory extends LoggerFactory {
    public Logger getLogger(Object obj) {
        return new AndroidMCBPLogger(obj);
    }
}
