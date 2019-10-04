/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.lang.Throwable
 */
package org.bouncycastle.util.test;

import org.bouncycastle.util.test.Test;
import org.bouncycastle.util.test.TestResult;

public class SimpleTestResult
implements TestResult {
    private static final String SEPARATOR = System.getProperty((String)"line.separator");
    private Throwable exception;
    private String message;
    private boolean success;

    public SimpleTestResult(boolean bl, String string) {
        this.success = bl;
        this.message = string;
    }

    public SimpleTestResult(boolean bl, String string, Throwable throwable) {
        this.success = bl;
        this.message = string;
        this.exception = throwable;
    }

    public static TestResult failed(Test test, String string) {
        return new SimpleTestResult(false, test.getName() + ": " + string);
    }

    public static TestResult failed(Test test, String string, Object object, Object object2) {
        return SimpleTestResult.failed(test, string + SEPARATOR + "Expected: " + object + SEPARATOR + "Found   : " + object2);
    }

    public static TestResult failed(Test test, String string, Throwable throwable) {
        return new SimpleTestResult(false, test.getName() + ": " + string, throwable);
    }

    public static String failedMessage(String string, String string2, String string3, String string4) {
        StringBuffer stringBuffer = new StringBuffer(string);
        stringBuffer.append(" failing ").append(string2);
        stringBuffer.append(SEPARATOR).append("    expected: ").append(string3);
        stringBuffer.append(SEPARATOR).append("    got     : ").append(string4);
        return stringBuffer.toString();
    }

    public static TestResult successful(Test test, String string) {
        return new SimpleTestResult(true, test.getName() + ": " + string);
    }

    @Override
    public Throwable getException() {
        return this.exception;
    }

    @Override
    public boolean isSuccessful() {
        return this.success;
    }

    @Override
    public String toString() {
        return this.message;
    }
}

