/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util.test;

import org.bouncycastle.util.test.TestResult;

public interface Test {
    public String getName();

    public TestResult perform();
}

