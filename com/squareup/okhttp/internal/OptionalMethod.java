package com.squareup.okhttp.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod<T> {
    private final String methodName;
    private final Class[] methodParams;
    private final Class<?> returnType;

    public OptionalMethod(Class<?> cls, String str, Class... clsArr) {
        this.returnType = cls;
        this.methodName = str;
        this.methodParams = clsArr;
    }

    public boolean isSupported(T t) {
        return getMethod(t.getClass()) != null;
    }

    public Object invokeOptional(T t, Object... objArr) {
        Object obj = null;
        Method method = getMethod(t.getClass());
        if (method != null) {
            try {
                obj = method.invoke(t, objArr);
            } catch (IllegalAccessException e) {
            }
        }
        return obj;
    }

    public Object invokeOptionalWithoutCheckedException(T t, Object... objArr) {
        try {
            return invokeOptional(t, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            AssertionError assertionError = new AssertionError("Unexpected exception");
            assertionError.initCause(targetException);
            throw assertionError;
        }
    }

    public Object invoke(T t, Object... objArr) {
        Object method = getMethod(t.getClass());
        if (method == null) {
            throw new AssertionError("Method " + this.methodName + " not supported for object " + t);
        }
        try {
            return method.invoke(t, objArr);
        } catch (Throwable e) {
            AssertionError assertionError = new AssertionError("Unexpectedly could not call: " + method);
            assertionError.initCause(e);
            throw assertionError;
        }
    }

    public Object invokeWithoutCheckedException(T t, Object... objArr) {
        try {
            return invoke(t, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            AssertionError assertionError = new AssertionError("Unexpected exception");
            assertionError.initCause(targetException);
            throw assertionError;
        }
    }

    private Method getMethod(Class<?> cls) {
        if (this.methodName == null) {
            return null;
        }
        Method publicMethod = getPublicMethod(cls, this.methodName, this.methodParams);
        if (publicMethod == null || this.returnType == null || this.returnType.isAssignableFrom(publicMethod.getReturnType())) {
            return publicMethod;
        }
        return null;
    }

    private static Method getPublicMethod(Class<?> cls, String str, Class[] clsArr) {
        try {
            Method method = cls.getMethod(str, clsArr);
            try {
                if ((method.getModifiers() & 1) == 0) {
                    return null;
                }
                return method;
            } catch (NoSuchMethodException e) {
                return method;
            }
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }
}
