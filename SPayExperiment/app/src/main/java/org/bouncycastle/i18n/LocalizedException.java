/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Locale
 */
package org.bouncycastle.i18n;

import java.util.Locale;
import org.bouncycastle.i18n.ErrorBundle;

public class LocalizedException
extends Exception {
    private Throwable cause;
    protected ErrorBundle message;

    public LocalizedException(ErrorBundle errorBundle) {
        super(errorBundle.getText(Locale.getDefault()));
        this.message = errorBundle;
    }

    public LocalizedException(ErrorBundle errorBundle, Throwable throwable) {
        super(errorBundle.getText(Locale.getDefault()));
        this.message = errorBundle;
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public ErrorBundle getErrorMessage() {
        return this.message;
    }
}

