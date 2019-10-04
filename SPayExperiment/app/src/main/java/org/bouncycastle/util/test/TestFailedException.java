/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.RuntimeException
 */
package org.bouncycastle.util.test;

import org.bouncycastle.util.test.TestResult;

public class TestFailedException
extends RuntimeException {
    private TestResult _result;

    public TestFailedException(TestResult testResult) {
        this._result = testResult;
    }

    public TestResult getResult() {
        return this._result;
    }
}

