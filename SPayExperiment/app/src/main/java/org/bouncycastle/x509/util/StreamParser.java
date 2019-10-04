/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Collection
 */
package org.bouncycastle.x509.util;

import java.util.Collection;

public interface StreamParser {
    public Object read();

    public Collection readAll();
}

