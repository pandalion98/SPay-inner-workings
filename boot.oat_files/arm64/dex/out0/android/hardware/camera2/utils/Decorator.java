package android.hardware.camera2.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Decorator<T> implements InvocationHandler {
    private final DecoratorListener mListener;
    private final T mObject;

    public interface DecoratorListener {
        void onAfterInvocation(Method method, Object[] objArr, Object obj);

        void onBeforeInvocation(Method method, Object[] objArr);

        boolean onCatchException(Method method, Object[] objArr, Throwable th);

        void onFinally(Method method, Object[] objArr);
    }

    public static <T> T newInstance(T obj, DecoratorListener listener) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new Decorator(obj, listener));
    }

    private Decorator(T obj, DecoratorListener listener) {
        this.mObject = obj;
        this.mListener = listener;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object obj = null;
        try {
            this.mListener.onBeforeInvocation(m, args);
            obj = m.invoke(this.mObject, args);
            this.mListener.onAfterInvocation(m, args, obj);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (!this.mListener.onCatchException(m, args, t)) {
                throw t;
            }
        } finally {
            this.mListener.onFinally(m, args);
        }
        return obj;
    }
}
