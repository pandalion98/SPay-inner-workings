/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.AssertionError
 *  java.lang.Class
 *  java.lang.IllegalAccessException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 */
package com.squareup.okhttp.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod<T> {
    private final String methodName;
    private final Class[] methodParams;
    private final Class<?> returnType;

    public /* varargs */ OptionalMethod(Class<?> class_, String string, Class ... arrclass) {
        this.returnType = class_;
        this.methodName = string;
        this.methodParams = arrclass;
    }

    private Method getMethod(Class<?> class_) {
        Method method;
        if (this.methodName == null || (method = OptionalMethod.getPublicMethod(class_, this.methodName, this.methodParams)) != null && this.returnType != null && !this.returnType.isAssignableFrom(method.getReturnType())) {
            return null;
        }
        return method;
    }

    private static Method getPublicMethod(Class<?> class_, String string, Class[] arrclass) {
        Method method;
        int n2;
        try {
            method = class_.getMethod(string, arrclass);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return null;
        }
        try {
            n2 = method.getModifiers();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return method;
        }
        if ((n2 & 1) == 0) {
            return null;
        }
        return method;
    }

    public /* varargs */ Object invoke(T t2, Object ... arrobject) {
        Method method = this.getMethod(t2.getClass());
        if (method == null) {
            throw new AssertionError((Object)("Method " + this.methodName + " not supported for object " + t2));
        }
        try {
            Object object = method.invoke(t2, arrobject);
            return object;
        }
        catch (IllegalAccessException illegalAccessException) {
            AssertionError assertionError = new AssertionError((Object)("Unexpectedly could not call: " + (Object)method));
            assertionError.initCause((Throwable)illegalAccessException);
            throw assertionError;
        }
    }

    public /* varargs */ Object invokeOptional(T t2, Object ... arrobject) {
        Method method = this.getMethod(t2.getClass());
        if (method == null) {
            return null;
        }
        try {
            Object object = method.invoke(t2, arrobject);
            return object;
        }
        catch (IllegalAccessException illegalAccessException) {
            return null;
        }
    }

    public /* varargs */ Object invokeOptionalWithoutCheckedException(T t2, Object ... arrobject) {
        try {
            Object object = this.invokeOptional(t2, arrobject);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            Throwable throwable = invocationTargetException.getTargetException();
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            }
            AssertionError assertionError = new AssertionError((Object)"Unexpected exception");
            assertionError.initCause(throwable);
            throw assertionError;
        }
    }

    public /* varargs */ Object invokeWithoutCheckedException(T t2, Object ... arrobject) {
        try {
            Object object = this.invoke(t2, arrobject);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            Throwable throwable = invocationTargetException.getTargetException();
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            }
            AssertionError assertionError = new AssertionError((Object)"Unexpected exception");
            assertionError.initCause(throwable);
            throw assertionError;
        }
    }

    public boolean isSupported(T t2) {
        return this.getMethod(t2.getClass()) != null;
    }
}

