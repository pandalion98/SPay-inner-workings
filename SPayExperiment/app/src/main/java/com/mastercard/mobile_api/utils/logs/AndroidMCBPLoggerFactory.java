/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils.logs;

import com.mastercard.mobile_api.utils.logs.AndroidMCBPLogger;
import com.mastercard.mobile_api.utils.logs.Logger;
import com.mastercard.mobile_api.utils.logs.LoggerFactory;

public class AndroidMCBPLoggerFactory
extends LoggerFactory {
    @Override
    public Logger getLogger(Object object) {
        return new AndroidMCBPLogger(object);
    }
}

