/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public interface MTBPCardListener {
    public void onCardDeactivated();

    public void onCardSelected();

    public void onTransactionCompleted();

    public void onTransactionTerminated(MCTransactionResult var1);
}

