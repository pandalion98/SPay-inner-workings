package com.squareup.okhttp.internal;

import com.squareup.okhttp.Protocol;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;
import okio.Buffer;

public class Platform {
    private static final Platform PLATFORM;

    private static class Android extends Platform {
        private final OptionalMethod<Socket> getAlpnSelectedProtocol;
        private final OptionalMethod<Socket> setAlpnProtocols;
        private final OptionalMethod<Socket> setHostname;
        private final OptionalMethod<Socket> setUseSessionTickets;
        private final Method trafficStatsTagSocket;
        private final Method trafficStatsUntagSocket;

        public Android(OptionalMethod<Socket> optionalMethod, OptionalMethod<Socket> optionalMethod2, Method method, Method method2, OptionalMethod<Socket> optionalMethod3, OptionalMethod<Socket> optionalMethod4) {
            this.setUseSessionTickets = optionalMethod;
            this.setHostname = optionalMethod2;
            this.trafficStatsTagSocket = method;
            this.trafficStatsUntagSocket = method2;
            this.getAlpnSelectedProtocol = optionalMethod3;
            this.setAlpnProtocols = optionalMethod4;
        }

        public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int i) {
            try {
                socket.connect(inetSocketAddress, i);
            } catch (Throwable e) {
                IOException iOException = new IOException("Exception in connect");
                iOException.initCause(e);
                throw iOException;
            }
        }

        public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) {
            if (str != null) {
                this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sSLSocket, Boolean.valueOf(true));
                this.setHostname.invokeOptionalWithoutCheckedException(sSLSocket, str);
            }
            if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sSLSocket)) {
                this.setAlpnProtocols.invokeWithoutCheckedException(sSLSocket, Platform.concatLengthPrefixed(list));
            }
        }

        public String getSelectedProtocol(SSLSocket sSLSocket) {
            if (this.getAlpnSelectedProtocol == null || !this.getAlpnSelectedProtocol.isSupported(sSLSocket)) {
                return null;
            }
            byte[] bArr = (byte[]) this.getAlpnSelectedProtocol.invokeWithoutCheckedException(sSLSocket, new Object[0]);
            return bArr != null ? new String(bArr, Util.UTF_8) : null;
        }

        public void tagSocket(Socket socket) {
            if (this.trafficStatsTagSocket != null) {
                try {
                    this.trafficStatsTagSocket.invoke(null, new Object[]{socket});
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2.getCause());
                }
            }
        }

        public void untagSocket(Socket socket) {
            if (this.trafficStatsUntagSocket != null) {
                try {
                    this.trafficStatsUntagSocket.invoke(null, new Object[]{socket});
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2.getCause());
                }
            }
        }
    }

    private static class JdkWithJettyBootPlatform extends Platform {
        private final Class<?> clientProviderClass;
        private final Method getMethod;
        private final Method putMethod;
        private final Method removeMethod;
        private final Class<?> serverProviderClass;

        public JdkWithJettyBootPlatform(Method method, Method method2, Method method3, Class<?> cls, Class<?> cls2) {
            this.putMethod = method;
            this.getMethod = method2;
            this.removeMethod = method3;
            this.clientProviderClass = cls;
            this.serverProviderClass = cls2;
        }

        public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) {
            Object newProxyInstance;
            List arrayList = new ArrayList(list.size());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Protocol protocol = (Protocol) list.get(i);
                if (protocol != Protocol.HTTP_1_0) {
                    arrayList.add(protocol.toString());
                }
            }
            try {
                newProxyInstance = Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[]{this.clientProviderClass, this.serverProviderClass}, new JettyNegoProvider(arrayList));
                this.putMethod.invoke(null, new Object[]{sSLSocket, newProxyInstance});
            } catch (InvocationTargetException e) {
                newProxyInstance = e;
                throw new AssertionError(newProxyInstance);
            } catch (IllegalAccessException e2) {
                newProxyInstance = e2;
                throw new AssertionError(newProxyInstance);
            }
        }

        public void afterHandshake(SSLSocket sSLSocket) {
            try {
                this.removeMethod.invoke(null, new Object[]{sSLSocket});
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                throw new AssertionError();
            }
        }

        public String getSelectedProtocol(SSLSocket sSLSocket) {
            try {
                JettyNegoProvider jettyNegoProvider = (JettyNegoProvider) Proxy.getInvocationHandler(this.getMethod.invoke(null, new Object[]{sSLSocket}));
                if (jettyNegoProvider.unsupported || jettyNegoProvider.selected != null) {
                    return jettyNegoProvider.unsupported ? null : jettyNegoProvider.selected;
                }
                Internal.logger.log(Level.INFO, "ALPN callback dropped: SPDY and HTTP/2 are disabled. Is alpn-boot on the boot class path?");
                return null;
            } catch (InvocationTargetException e) {
                throw new AssertionError();
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            }
        }
    }

    private static class JettyNegoProvider implements InvocationHandler {
        private final List<String> protocols;
        private String selected;
        private boolean unsupported;

        public JettyNegoProvider(List<String> list) {
            this.protocols = list;
        }

        public Object invoke(Object obj, Method method, Object[] objArr) {
            String name = method.getName();
            Class returnType = method.getReturnType();
            if (objArr == null) {
                objArr = Util.EMPTY_STRING_ARRAY;
            }
            if (name.equals("supports") && Boolean.TYPE == returnType) {
                return Boolean.valueOf(true);
            }
            if (name.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                return null;
            } else if (name.equals("protocols") && objArr.length == 0) {
                return this.protocols;
            } else {
                if ((name.equals("selectProtocol") || name.equals("select")) && String.class == returnType && objArr.length == 1 && (objArr[0] instanceof List)) {
                    List list = (List) objArr[0];
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        if (this.protocols.contains(list.get(i))) {
                            name = (String) list.get(i);
                            this.selected = name;
                            return name;
                        }
                    }
                    name = (String) this.protocols.get(0);
                    this.selected = name;
                    return name;
                } else if ((!name.equals("protocolSelected") && !name.equals("selected")) || objArr.length != 1) {
                    return method.invoke(this, objArr);
                } else {
                    this.selected = (String) objArr[0];
                    return null;
                }
            }
        }
    }

    static {
        PLATFORM = findPlatform();
    }

    public static Platform get() {
        return PLATFORM;
    }

    public String getPrefix() {
        return "OkHttp";
    }

    public void logW(String str) {
        System.out.println(str);
    }

    public void tagSocket(Socket socket) {
    }

    public void untagSocket(Socket socket) {
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) {
    }

    public void afterHandshake(SSLSocket sSLSocket) {
    }

    public String getSelectedProtocol(SSLSocket sSLSocket) {
        return null;
    }

    public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int i) {
        socket.connect(inetSocketAddress, i);
    }

    private static Platform findPlatform() {
        Method method;
        Method method2;
        OptionalMethod optionalMethod;
        OptionalMethod optionalMethod2;
        Method method3;
        OptionalMethod optionalMethod3;
        OptionalMethod optionalMethod4;
        Method method4;
        try {
            Class.forName("com.android.org.conscrypt.OpenSSLSocketImpl");
        } catch (ClassNotFoundException e) {
            Class.forName("org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl");
        }
        try {
            OptionalMethod optionalMethod5 = new OptionalMethod(null, "setUseSessionTickets", Boolean.TYPE);
            OptionalMethod optionalMethod6 = new OptionalMethod(null, "setHostname", String.class);
            try {
                Class cls = Class.forName("android.net.TrafficStats");
                Method method5 = cls.getMethod("tagSocket", new Class[]{Socket.class});
                try {
                    method = cls.getMethod("untagSocket", new Class[]{Socket.class});
                    method2 = method;
                    optionalMethod = optionalMethod2;
                    method3 = method5;
                    optionalMethod3 = optionalMethod4;
                } catch (ClassNotFoundException e2) {
                    method4 = method5;
                    method2 = null;
                    method = method4;
                    optionalMethod4 = null;
                    optionalMethod3 = null;
                    method3 = method;
                    optionalMethod = optionalMethod4;
                    return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
                } catch (NoSuchMethodException e3) {
                    optionalMethod4 = null;
                    method2 = null;
                    method = method5;
                    optionalMethod3 = null;
                    method3 = method;
                    optionalMethod = optionalMethod4;
                    return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
                }
                try {
                    Class.forName("android.net.Network");
                    OptionalMethod optionalMethod7 = new OptionalMethod(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
                    try {
                        optionalMethod4 = new OptionalMethod(null, "setAlpnProtocols", byte[].class);
                        optionalMethod2 = optionalMethod7;
                    } catch (ClassNotFoundException e4) {
                        optionalMethod4 = optionalMethod7;
                        optionalMethod2 = optionalMethod4;
                        optionalMethod4 = null;
                        method2 = method;
                        optionalMethod = optionalMethod2;
                        method3 = method5;
                        optionalMethod3 = optionalMethod4;
                        return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
                    } catch (NoSuchMethodException e5) {
                        optionalMethod4 = optionalMethod7;
                        method2 = method;
                        method = method5;
                        optionalMethod3 = null;
                        method3 = method;
                        optionalMethod = optionalMethod4;
                        return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
                    }
                } catch (ClassNotFoundException e6) {
                    optionalMethod4 = null;
                    optionalMethod2 = optionalMethod4;
                    optionalMethod4 = null;
                    method2 = method;
                    optionalMethod = optionalMethod2;
                    method3 = method5;
                    optionalMethod3 = optionalMethod4;
                    return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
                } catch (NoSuchMethodException e7) {
                    optionalMethod4 = null;
                    method2 = method;
                    method = method5;
                    optionalMethod3 = null;
                    method3 = method;
                    optionalMethod = optionalMethod4;
                    return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
                }
            } catch (ClassNotFoundException e8) {
                method4 = null;
                method2 = null;
                method = method4;
                optionalMethod4 = null;
                optionalMethod3 = null;
                method3 = method;
                optionalMethod = optionalMethod4;
                return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
            } catch (NoSuchMethodException e9) {
                optionalMethod4 = null;
                method2 = null;
                method = null;
                optionalMethod3 = null;
                method3 = method;
                optionalMethod = optionalMethod4;
                return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
            }
            return new Android(optionalMethod5, optionalMethod6, method3, method2, optionalMethod, optionalMethod3);
        } catch (ClassNotFoundException e10) {
            try {
                String str = "org.eclipse.jetty.alpn.ALPN";
                Class cls2 = Class.forName(str);
                Class cls3 = Class.forName(str + "$Provider");
                Class cls4 = Class.forName(str + "$ClientProvider");
                Class cls5 = Class.forName(str + "$ServerProvider");
                return new JdkWithJettyBootPlatform(cls2.getMethod("put", new Class[]{SSLSocket.class, cls3}), cls2.getMethod("get", new Class[]{SSLSocket.class}), cls2.getMethod("remove", new Class[]{SSLSocket.class}), cls4, cls5);
            } catch (ClassNotFoundException e11) {
                return new Platform();
            } catch (NoSuchMethodException e12) {
                return new Platform();
            }
        }
    }

    static byte[] concatLengthPrefixed(List<Protocol> list) {
        Buffer buffer = new Buffer();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Protocol protocol = (Protocol) list.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                buffer.writeByte(protocol.toString().length());
                buffer.writeUtf8(protocol.toString());
            }
        }
        return buffer.readByteArray();
    }
}
