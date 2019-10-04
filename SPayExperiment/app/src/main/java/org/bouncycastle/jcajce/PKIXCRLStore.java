/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.cert.CRL
 *  java.util.Collection
 */
package org.bouncycastle.jcajce;

import java.security.cert.CRL;
import java.util.Collection;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

public interface PKIXCRLStore<T extends CRL>
extends Store<T> {
    @Override
    public Collection<T> getMatches(Selector<T> var1);
}

