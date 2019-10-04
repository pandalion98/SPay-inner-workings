/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.PrintStream
 *  java.lang.AssertionError
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.ClassNotFoundException
 *  java.lang.IllegalAccessException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.Void
 *  java.lang.reflect.InvocationHandler
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 *  java.lang.reflect.Proxy
 *  java.net.InetSocketAddress
 *  java.net.Socket
 *  java.net.SocketAddress
 *  java.nio.charset.Charset
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.logging.Level
 *  java.util.logging.Logger
 *  javax.net.ssl.SSLSocket
 */
package com.squareup.okhttp.internal;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.OptionalMethod;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import okio.Buffer;

public class Platform {
    private static final Platform PLATFORM = Platform.findPlatform();

    /*
     * Enabled aggressive block sorting
     */
    static byte[] concatLengthPrefixed(List<Protocol> list) {
        Buffer buffer = new Buffer();
        int n2 = list.size();
        int n3 = 0;
        while (n3 < n2) {
            Protocol protocol = (Protocol)((Object)list.get(n3));
            if (protocol != Protocol.HTTP_1_0) {
                buffer.writeByte(protocol.toString().length());
                buffer.writeUtf8(protocol.toString());
            }
            ++n3;
        }
        return buffer.readByteArray();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static Platform findPlatform() {
        Class.forName((String)"com.android.org.conscrypt.OpenSSLSocketImpl");
        {
            catch (ClassNotFoundException var0_13) {
                Class.forName((String)"org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl");
            }
            var10 = new Class[]{Boolean.TYPE};
            var11_1 = new OptionalMethod<Socket>(null, "setUseSessionTickets", var10);
            var12_2 = new OptionalMethod<Socket>(null, "setHostname", new Class[]{String.class});
        }
lbl10: // 2 sources:
        catch (ClassNotFoundException var1_14) {
            try {
                var4_15 = Class.forName((String)"org.eclipse.jetty.alpn.ALPN");
                var5_16 = Class.forName((String)("org.eclipse.jetty.alpn.ALPN" + "$Provider"));
                var6_17 = Class.forName((String)("org.eclipse.jetty.alpn.ALPN" + "$ClientProvider"));
                var7_18 = Class.forName((String)("org.eclipse.jetty.alpn.ALPN" + "$ServerProvider"));
                return new JdkWithJettyBootPlatform(var4_15.getMethod("put", new Class[]{SSLSocket.class, var5_16}), var4_15.getMethod("get", new Class[]{SSLSocket.class}), var4_15.getMethod("remove", new Class[]{SSLSocket.class}), var6_17, var7_18);
            }
            catch (ClassNotFoundException var3_20) {
                return new Platform();
            }
            catch (NoSuchMethodException var2_27) {
                return new Platform();
            }
        }
        {
            block22 : {
                block20 : {
                    var22_3 = Class.forName((String)"android.net.TrafficStats");
                    var23_4 = var22_3.getMethod("tagSocket", new Class[]{Socket.class});
                    var26_5 = var22_3.getMethod("untagSocket", new Class[]{Socket.class});
                    Class.forName((String)"android.net.Network");
                    var33_6 = new OptionalMethod<T>(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
                    var31_7 = new OptionalMethod<T>(null, "setAlpnProtocols", new Class[]{byte[].class});
                    var30_8 = var33_6;
                    break block20;
                    catch (ClassNotFoundException var28_21) {
                        block23 : {
                            var29_22 = null;
                            break block23;
                            catch (ClassNotFoundException var20_23) {
                                block21 : {
                                    block24 : {
                                        var21_24 = null;
                                        break block24;
                                        catch (ClassNotFoundException var25_28) {
                                            var21_24 = var23_4;
                                        }
                                    }
                                    var15_9 = null;
                                    var16_25 = var21_24;
                                    var14_26 = null;
                                    break block21;
                                    catch (NoSuchMethodException var13_29) {
                                        var14_26 = null;
                                        var15_9 = null;
                                        var16_25 = null;
                                        break block21;
                                    }
                                    catch (NoSuchMethodException var24_30) {
                                        var16_25 = var23_4;
                                        var14_26 = null;
                                        var15_9 = null;
                                        break block21;
                                    }
                                    catch (NoSuchMethodException var27_31) {
                                        var15_9 = var26_5;
                                        var16_25 = var23_4;
                                        var14_26 = null;
                                        break block21;
                                    }
                                    catch (NoSuchMethodException var35_32) {
                                        var14_26 = var33_6;
                                        var15_9 = var26_5;
                                        var16_25 = var23_4;
                                    }
                                }
                                var17_11 = var16_25;
                                var18_10 = var14_26;
                                var19_12 = null;
                                break block22;
                            }
                            catch (ClassNotFoundException var34_33) {
                                var29_22 = var33_6;
                            }
                        }
                        var30_8 = var29_22;
                        var31_7 = null;
                    }
                }
                var15_9 = var26_5;
                var18_10 = var30_8;
                var17_11 = var23_4;
                var19_12 = var31_7;
            }
            ** try [egrp 6[TRYBLOCK] [10 : 161->191)] { 
lbl82: // 1 sources:
            return new Android(var11_1, var12_2, var17_11, var15_9, var18_10, var19_12);
        }
    }

    public static Platform get() {
        return PLATFORM;
    }

    public void afterHandshake(SSLSocket sSLSocket) {
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String string, List<Protocol> list) {
    }

    public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int n2) {
        socket.connect((SocketAddress)inetSocketAddress, n2);
    }

    public String getPrefix() {
        return "OkHttp";
    }

    public String getSelectedProtocol(SSLSocket sSLSocket) {
        return null;
    }

    public void logW(String string) {
        System.out.println(string);
    }

    public void tagSocket(Socket socket) {
    }

    public void untagSocket(Socket socket) {
    }

    private static class Android
    extends Platform {
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

        @Override
        public void configureTlsExtensions(SSLSocket sSLSocket, String string, List<Protocol> list) {
            if (string != null) {
                OptionalMethod<Socket> optionalMethod = this.setUseSessionTickets;
                Object[] arrobject = new Object[]{true};
                optionalMethod.invokeOptionalWithoutCheckedException((Socket)sSLSocket, arrobject);
                this.setHostname.invokeOptionalWithoutCheckedException((Socket)sSLSocket, string);
            }
            if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported((Socket)sSLSocket)) {
                Object[] arrobject = new Object[]{Android.concatLengthPrefixed(list)};
                this.setAlpnProtocols.invokeWithoutCheckedException((Socket)sSLSocket, arrobject);
            }
        }

        @Override
        public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int n2) {
            try {
                socket.connect((SocketAddress)inetSocketAddress, n2);
                return;
            }
            catch (SecurityException securityException) {
                IOException iOException = new IOException("Exception in connect");
                iOException.initCause((Throwable)securityException);
                throw iOException;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public String getSelectedProtocol(SSLSocket sSLSocket) {
            if (this.getAlpnSelectedProtocol == null) {
                return null;
            }
            if (!this.getAlpnSelectedProtocol.isSupported((Socket)sSLSocket)) return null;
            byte[] arrby = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException((Socket)sSLSocket, new Object[0]);
            if (arrby == null) return null;
            return new String(arrby, Util.UTF_8);
        }

        @Override
        public void tagSocket(Socket socket) {
            if (this.trafficStatsTagSocket == null) {
                return;
            }
            try {
                this.trafficStatsTagSocket.invoke(null, new Object[]{socket});
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException((Throwable)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
        }

        @Override
        public void untagSocket(Socket socket) {
            if (this.trafficStatsUntagSocket == null) {
                return;
            }
            try {
                this.trafficStatsUntagSocket.invoke(null, new Object[]{socket});
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException((Throwable)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
        }
    }

    private static class JdkWithJettyBootPlatform
    extends Platform {
        private final Class<?> clientProviderClass;
        private final Method getMethod;
        private final Method putMethod;
        private final Method removeMethod;
        private final Class<?> serverProviderClass;

        public JdkWithJettyBootPlatform(Method method, Method method2, Method method3, Class<?> class_, Class<?> class_2) {
            this.putMethod = method;
            this.getMethod = method2;
            this.removeMethod = method3;
            this.clientProviderClass = class_;
            this.serverProviderClass = class_2;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void afterHandshake(SSLSocket sSLSocket) {
            try {
                this.removeMethod.invoke(null, new Object[]{sSLSocket});
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                do {
                    throw new AssertionError();
                    break;
                } while (true);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new AssertionError();
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void configureTlsExtensions(SSLSocket sSLSocket, String string, List<Protocol> list) {
            void var7_12;
            ArrayList arrayList = new ArrayList(list.size());
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Protocol protocol = (Protocol)((Object)list.get(i2));
                if (protocol == Protocol.HTTP_1_0) continue;
                arrayList.add((Object)protocol.toString());
            }
            try {
                ClassLoader classLoader = Platform.class.getClassLoader();
                Class[] arrclass = new Class[]{this.clientProviderClass, this.serverProviderClass};
                Object object = Proxy.newProxyInstance((ClassLoader)classLoader, (Class[])arrclass, (InvocationHandler)new JettyNegoProvider((List<String>)arrayList));
                this.putMethod.invoke(null, new Object[]{sSLSocket, object});
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError((Object)var7_12);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new AssertionError((Object)var7_12);
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public String getSelectedProtocol(SSLSocket sSLSocket) {
            JettyNegoProvider jettyNegoProvider;
            block7 : {
                block6 : {
                    jettyNegoProvider = (JettyNegoProvider)Proxy.getInvocationHandler((Object)this.getMethod.invoke(null, new Object[]{sSLSocket}));
                    if (jettyNegoProvider.unsupported || jettyNegoProvider.selected != null) break block6;
                    Internal.logger.log(Level.INFO, "ALPN callback dropped: SPDY and HTTP/2 are disabled. Is alpn-boot on the boot class path?");
                    return null;
                }
                if (!jettyNegoProvider.unsupported) break block7;
                return null;
            }
            try {
                String string = jettyNegoProvider.selected;
                return string;
            }
            catch (IllegalAccessException illegalAccessException) {
                do {
                    throw new AssertionError();
                    break;
                } while (true);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new AssertionError();
            }
        }
    }

    private static class JettyNegoProvider
    implements InvocationHandler {
        private final List<String> protocols;
        private String selected;
        private boolean unsupported;

        public JettyNegoProvider(List<String> list) {
            this.protocols = list;
        }

        public Object invoke(Object object, Method method, Object[] arrobject) {
            String string = method.getName();
            Class class_ = method.getReturnType();
            if (arrobject == null) {
                arrobject = Util.EMPTY_STRING_ARRAY;
            }
            if (string.equals((Object)"supports") && Boolean.TYPE == class_) {
                return true;
            }
            if (string.equals((Object)"unsupported") && Void.TYPE == class_) {
                this.unsupported = true;
                return null;
            }
            if (string.equals((Object)"protocols") && arrobject.length == 0) {
                return this.protocols;
            }
            if ((string.equals((Object)"selectProtocol") || string.equals((Object)"select")) && String.class == class_ && arrobject.length == 1 && arrobject[0] instanceof List) {
                String string2;
                List list = (List)arrobject[0];
                int n2 = list.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    String string3;
                    if (!this.protocols.contains(list.get(i2))) continue;
                    this.selected = string3 = (String)list.get(i2);
                    return string3;
                }
                this.selected = string2 = (String)this.protocols.get(0);
                return string2;
            }
            if ((string.equals((Object)"protocolSelected") || string.equals((Object)"selected")) && arrobject.length == 1) {
                this.selected = (String)arrobject[0];
                return null;
            }
            return method.invoke((Object)this, arrobject);
        }
    }

}

