/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Hashtable
 */
package org.bouncycastle.asn1.eac;

import java.util.Hashtable;

public class BidirectionalMap
extends Hashtable {
    private static final long serialVersionUID = -7457289971962812909L;
    Hashtable reverseMap = new Hashtable();

    public Object getReverse(Object object) {
        return this.reverseMap.get(object);
    }

    public Object put(Object object, Object object2) {
        this.reverseMap.put(object2, object);
        return super.put(object, object2);
    }
}

