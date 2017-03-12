package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;

public abstract class CommandProcess {
    protected DataContext dataContext;

    protected abstract TokenAPDUResponse process(CommandAPDU commandAPDU);

    protected final void set() {
        this.dataContext = DataContext.getSessionInstance();
    }

    protected final void reset() {
        this.dataContext = null;
    }
}
