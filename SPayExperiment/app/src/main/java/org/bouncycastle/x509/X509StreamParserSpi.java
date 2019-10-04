/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.lang.Object
 *  java.util.Collection
 */
package org.bouncycastle.x509;

import java.io.InputStream;
import java.util.Collection;

public abstract class X509StreamParserSpi {
    public abstract void engineInit(InputStream var1);

    public abstract Object engineRead();

    public abstract Collection engineReadAll();
}

