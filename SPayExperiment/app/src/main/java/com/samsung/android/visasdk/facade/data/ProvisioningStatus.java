/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

public final class ProvisioningStatus
extends Enum<ProvisioningStatus> {
    private static final /* synthetic */ ProvisioningStatus[] $VALUES;
    public static final /* enum */ ProvisioningStatus FAILURE;
    public static final /* enum */ ProvisioningStatus SUCCESS;

    static {
        SUCCESS = new ProvisioningStatus();
        FAILURE = new ProvisioningStatus();
        ProvisioningStatus[] arrprovisioningStatus = new ProvisioningStatus[]{SUCCESS, FAILURE};
        $VALUES = arrprovisioningStatus;
    }

    public static ProvisioningStatus valueOf(String string) {
        return (ProvisioningStatus)Enum.valueOf(ProvisioningStatus.class, (String)string);
    }

    public static ProvisioningStatus[] values() {
        return (ProvisioningStatus[])$VALUES.clone();
    }
}

