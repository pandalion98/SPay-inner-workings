/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.cert.Certificate
 *  java.util.Collection
 */
package org.bouncycastle.jcajce;

import java.security.cert.Certificate;
import java.util.Collection;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

public interface PKIXCertStore<T extends Certificate>
extends Store<T> {
    @Override
    public Collection<T> getMatches(Selector<T> var1);
}

