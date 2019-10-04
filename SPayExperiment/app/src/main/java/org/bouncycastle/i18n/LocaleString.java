/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Locale
 *  java.util.TimeZone
 */
package org.bouncycastle.i18n;

import java.util.Locale;
import java.util.TimeZone;
import org.bouncycastle.i18n.LocalizedMessage;

public class LocaleString
extends LocalizedMessage {
    public LocaleString(String string, String string2) {
        super(string, string2);
    }

    public LocaleString(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public LocaleString(String string, String string2, String string3, Object[] arrobject) {
        super(string, string2, string3, arrobject);
    }

    public String getLocaleString(Locale locale) {
        return this.getEntry(null, locale, null);
    }
}

