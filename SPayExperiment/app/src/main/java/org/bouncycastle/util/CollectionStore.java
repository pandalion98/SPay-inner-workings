/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 */
package org.bouncycastle.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.bouncycastle.util.Iterable;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

public class CollectionStore<T>
implements Iterable<T>,
Store<T> {
    private Collection<T> _local;

    public CollectionStore(Collection<T> collection) {
        this._local = new ArrayList(collection);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public Collection<T> getMatches(Selector<T> selector) {
        if (selector == null) {
            return new ArrayList(this._local);
        }
        ArrayList arrayList = new ArrayList();
        Iterator iterator = this._local.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!selector.match(object)) continue;
            arrayList.add(object);
        }
        return arrayList;
    }

    @Override
    public Iterator<T> iterator() {
        return this.getMatches(null).iterator();
    }
}

