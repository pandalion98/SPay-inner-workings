/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.tls.AlertDescription;

public class TlsFatalAlert
extends IOException {
    private static final long serialVersionUID = 3584313123679111168L;
    protected Throwable alertCause;
    protected short alertDescription;

    public TlsFatalAlert(short s2) {
        this(s2, null);
    }

    public TlsFatalAlert(short s2, Throwable throwable) {
        super(AlertDescription.getText(s2));
        this.alertDescription = s2;
        this.alertCause = throwable;
    }

    public short getAlertDescription() {
        return this.alertDescription;
    }

    public Throwable getCause() {
        return this.alertCause;
    }
}

