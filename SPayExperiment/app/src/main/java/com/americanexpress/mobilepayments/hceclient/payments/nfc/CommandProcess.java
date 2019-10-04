/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;

public abstract class CommandProcess {
    protected DataContext dataContext;

    protected abstract TokenAPDUResponse process(CommandAPDU var1);

    protected final void reset() {
        this.dataContext = null;
    }

    protected final void set() {
        this.dataContext = DataContext.getSessionInstance();
    }
}

