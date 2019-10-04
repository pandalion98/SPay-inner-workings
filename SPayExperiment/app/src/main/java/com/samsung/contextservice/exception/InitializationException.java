/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package com.samsung.contextservice.exception;

import com.samsung.contextservice.b.b;

public class InitializationException
extends RuntimeException {
    public InitializationException(String string) {
        super(string);
        b.e("EXCEPTION", string);
    }
}

