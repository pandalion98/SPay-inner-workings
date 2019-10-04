/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionData;

public class IdvOptionsData {
    private IdvOptions activation;

    public IdvOptionData[] getMethods() {
        if (this.activation != null) {
            return this.activation.methods;
        }
        return null;
    }

    private static class IdvOptions {
        private IdvOptionData[] methods;

        private IdvOptions() {
        }
    }

}

