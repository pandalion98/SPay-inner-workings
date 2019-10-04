/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 */
package org.bouncycastle.util.test;

import java.io.PrintStream;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.test.SimpleTestResult;
import org.bouncycastle.util.test.Test;
import org.bouncycastle.util.test.TestFailedException;
import org.bouncycastle.util.test.TestResult;

public abstract class SimpleTest
implements Test {
    protected static void runTest(Test test) {
        SimpleTest.runTest(test, System.out);
    }

    protected static void runTest(Test test, PrintStream printStream) {
        TestResult testResult = test.perform();
        printStream.println(testResult.toString());
        if (testResult.getException() != null) {
            testResult.getException().printStackTrace(printStream);
        }
    }

    private TestResult success() {
        return SimpleTestResult.successful(this, "Okay");
    }

    protected boolean areEqual(byte[] arrby, byte[] arrby2) {
        return Arrays.areEqual(arrby, arrby2);
    }

    protected void fail(String string) {
        throw new TestFailedException(SimpleTestResult.failed(this, string));
    }

    protected void fail(String string, Object object, Object object2) {
        throw new TestFailedException(SimpleTestResult.failed(this, string, object, object2));
    }

    protected void fail(String string, Throwable throwable) {
        throw new TestFailedException(SimpleTestResult.failed(this, string, throwable));
    }

    @Override
    public abstract String getName();

    @Override
    public TestResult perform() {
        try {
            this.performTest();
            TestResult testResult = this.success();
            return testResult;
        }
        catch (TestFailedException testFailedException) {
            return testFailedException.getResult();
        }
        catch (Exception exception) {
            return SimpleTestResult.failed(this, "Exception: " + (Object)((Object)exception), exception);
        }
    }

    public abstract void performTest();
}

