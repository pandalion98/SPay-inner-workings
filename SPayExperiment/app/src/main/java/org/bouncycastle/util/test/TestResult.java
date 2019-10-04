/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.util.test;

public interface TestResult {
    public Throwable getException();

    public boolean isSuccessful();

    public String toString();
}

