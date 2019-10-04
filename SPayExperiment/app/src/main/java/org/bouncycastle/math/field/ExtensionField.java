/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.field;

import org.bouncycastle.math.field.FiniteField;

public interface ExtensionField
extends FiniteField {
    public int getDegree();

    public FiniteField getSubfield();
}

