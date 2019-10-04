/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.URL
 *  java.net.URLClassLoader
 *  java.util.Locale
 */
package org.bouncycastle.i18n;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;

public class MissingEntryException
extends RuntimeException {
    private String debugMsg;
    protected final String key;
    protected final ClassLoader loader;
    protected final Locale locale;
    protected final String resource;

    public MissingEntryException(String string, String string2, String string3, Locale locale, ClassLoader classLoader) {
        super(string);
        this.resource = string2;
        this.key = string3;
        this.locale = locale;
        this.loader = classLoader;
    }

    public MissingEntryException(String string, Throwable throwable, String string2, String string3, Locale locale, ClassLoader classLoader) {
        super(string, throwable);
        this.resource = string2;
        this.key = string3;
        this.locale = locale;
        this.loader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }

    public String getDebugMsg() {
        if (this.debugMsg == null) {
            this.debugMsg = "Can not find entry " + this.key + " in resource file " + this.resource + " for the locale " + (Object)this.locale + ".";
            if (this.loader instanceof URLClassLoader) {
                URL[] arruRL = ((URLClassLoader)this.loader).getURLs();
                this.debugMsg = this.debugMsg + " The following entries in the classpath were searched: ";
                for (int i2 = 0; i2 != arruRL.length; ++i2) {
                    this.debugMsg = this.debugMsg + (Object)arruRL[i2] + " ";
                }
            }
        }
        return this.debugMsg;
    }

    public String getKey() {
        return this.key;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getResource() {
        return this.resource;
    }
}

