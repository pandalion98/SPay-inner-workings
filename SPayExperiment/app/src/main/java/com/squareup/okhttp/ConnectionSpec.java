/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Arrays
 *  java.util.List
 *  javax.net.ssl.SSLSocket
 */
package com.squareup.okhttp;

import com.squareup.okhttp.CipherSuite;
import com.squareup.okhttp.TlsVersion;
import com.squareup.okhttp.internal.Util;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;

public final class ConnectionSpec {
    private static final CipherSuite[] APPROVED_CIPHER_SUITES;
    public static final ConnectionSpec CLEARTEXT;
    public static final ConnectionSpec COMPATIBLE_TLS;
    public static final ConnectionSpec MODERN_TLS;
    private final String[] cipherSuites;
    final boolean supportsTlsExtensions;
    final boolean tls;
    private final String[] tlsVersions;

    static {
        CipherSuite[] arrcipherSuite = new CipherSuite[]{CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA};
        APPROVED_CIPHER_SUITES = arrcipherSuite;
        Builder builder = new Builder(true).cipherSuites(APPROVED_CIPHER_SUITES);
        TlsVersion[] arrtlsVersion = new TlsVersion[]{TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0};
        MODERN_TLS = builder.tlsVersions(arrtlsVersion).supportsTlsExtensions(true).build();
        Builder builder2 = new Builder(MODERN_TLS);
        TlsVersion[] arrtlsVersion2 = new TlsVersion[]{TlsVersion.TLS_1_0};
        COMPATIBLE_TLS = builder2.tlsVersions(arrtlsVersion2).supportsTlsExtensions(true).build();
        CLEARTEXT = new Builder(false).build();
    }

    private ConnectionSpec(Builder builder) {
        this.tls = builder.tls;
        this.cipherSuites = builder.cipherSuites;
        this.tlsVersions = builder.tlsVersions;
        this.supportsTlsExtensions = builder.supportsTlsExtensions;
    }

    private static <T> boolean contains(T[] arrT, T t2) {
        int n2 = arrT.length;
        int n3 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    bl = false;
                    if (n3 >= n2) break block3;
                    if (!Util.equal(t2, arrT[n3])) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n3;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean nonEmptyIntersection(String[] arrstring, String[] arrstring2) {
        if (arrstring != null && arrstring2 != null && arrstring.length != 0 && arrstring2.length != 0) {
            int n2 = arrstring.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                if (!ConnectionSpec.contains(arrstring2, arrstring[i2])) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ConnectionSpec supportedSpec(SSLSocket sSLSocket, boolean bl) {
        String[] arrstring;
        String[] arrstring2 = this.cipherSuites;
        String[] arrstring3 = null;
        if (arrstring2 != null) {
            String[] arrstring4 = sSLSocket.getEnabledCipherSuites();
            arrstring3 = Util.intersect(String.class, this.cipherSuites, arrstring4);
        }
        if (bl && Arrays.asList((Object[])sSLSocket.getSupportedCipherSuites()).contains((Object)"TLS_FALLBACK_SCSV")) {
            if (arrstring3 == null) {
                arrstring3 = sSLSocket.getEnabledCipherSuites();
            }
            arrstring = new String[1 + arrstring3.length];
            System.arraycopy((Object)arrstring3, (int)0, (Object)arrstring, (int)0, (int)arrstring3.length);
            arrstring[-1 + arrstring.length] = "TLS_FALLBACK_SCSV";
        } else {
            arrstring = arrstring3;
        }
        String[] arrstring5 = sSLSocket.getEnabledProtocols();
        String[] arrstring6 = Util.intersect(String.class, this.tlsVersions, arrstring5);
        return new Builder(this).cipherSuites(arrstring).tlsVersions(arrstring6).build();
    }

    void apply(SSLSocket sSLSocket, boolean bl) {
        ConnectionSpec connectionSpec = this.supportedSpec(sSLSocket, bl);
        sSLSocket.setEnabledProtocols(connectionSpec.tlsVersions);
        String[] arrstring = connectionSpec.cipherSuites;
        if (arrstring != null) {
            sSLSocket.setEnabledCipherSuites(arrstring);
        }
    }

    public List<CipherSuite> cipherSuites() {
        if (this.cipherSuites == null) {
            return null;
        }
        CipherSuite[] arrcipherSuite = new CipherSuite[this.cipherSuites.length];
        for (int i2 = 0; i2 < this.cipherSuites.length; ++i2) {
            arrcipherSuite[i2] = CipherSuite.forJavaName(this.cipherSuites[i2]);
        }
        return Util.immutableList(arrcipherSuite);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (!(object instanceof ConnectionSpec)) break block4;
                if (object == this) {
                    return true;
                }
                ConnectionSpec connectionSpec = (ConnectionSpec)object;
                if (this.tls == connectionSpec.tls && (!this.tls || Arrays.equals((Object[])this.cipherSuites, (Object[])connectionSpec.cipherSuites) && Arrays.equals((Object[])this.tlsVersions, (Object[])connectionSpec.tlsVersions) && this.supportsTlsExtensions == connectionSpec.supportsTlsExtensions)) break block5;
            }
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int hashCode() {
        int n2;
        int n3 = 17;
        if (!this.tls) return n3;
        int n4 = 31 * (31 * (527 + Arrays.hashCode((Object[])this.cipherSuites)) + Arrays.hashCode((Object[])this.tlsVersions));
        if (this.supportsTlsExtensions) {
            n2 = 0;
            do {
                return n2 + n4;
                break;
            } while (true);
        }
        n2 = 1;
        return n2 + n4;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isCompatible(SSLSocket sSLSocket) {
        block5 : {
            block4 : {
                String[] arrstring;
                if (!this.tls || !ConnectionSpec.nonEmptyIntersection(this.tlsVersions, arrstring = sSLSocket.getEnabledProtocols())) break block4;
                if (this.cipherSuites != null) {
                    String[] arrstring2 = sSLSocket.getEnabledCipherSuites();
                    return ConnectionSpec.nonEmptyIntersection(this.cipherSuites, arrstring2);
                }
                if (sSLSocket.getEnabledCipherSuites().length > 0) break block5;
            }
            return false;
        }
        return true;
    }

    public boolean isTls() {
        return this.tls;
    }

    public boolean supportsTlsExtensions() {
        return this.supportsTlsExtensions;
    }

    public List<TlsVersion> tlsVersions() {
        TlsVersion[] arrtlsVersion = new TlsVersion[this.tlsVersions.length];
        for (int i2 = 0; i2 < this.tlsVersions.length; ++i2) {
            arrtlsVersion[i2] = TlsVersion.forJavaName(this.tlsVersions[i2]);
        }
        return Util.immutableList(arrtlsVersion);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        String string;
        if (!this.tls) return "ConnectionSpec()";
        List<CipherSuite> list = this.cipherSuites();
        if (list == null) {
            string = "[use default]";
            do {
                return "ConnectionSpec(cipherSuites=" + string + ", tlsVersions=" + this.tlsVersions() + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
                break;
            } while (true);
        }
        string = list.toString();
        return "ConnectionSpec(cipherSuites=" + string + ", tlsVersions=" + this.tlsVersions() + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
    }

    public static final class Builder {
        private String[] cipherSuites;
        private boolean supportsTlsExtensions;
        private boolean tls;
        private String[] tlsVersions;

        public Builder(ConnectionSpec connectionSpec) {
            this.tls = connectionSpec.tls;
            this.cipherSuites = connectionSpec.cipherSuites;
            this.tlsVersions = connectionSpec.tlsVersions;
            this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
        }

        Builder(boolean bl) {
            this.tls = bl;
        }

        public ConnectionSpec build() {
            return new ConnectionSpec(this);
        }

        public /* varargs */ Builder cipherSuites(CipherSuite ... arrcipherSuite) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            String[] arrstring = new String[arrcipherSuite.length];
            for (int i2 = 0; i2 < arrcipherSuite.length; ++i2) {
                arrstring[i2] = arrcipherSuite[i2].javaName;
            }
            this.cipherSuites = arrstring;
            return this;
        }

        public /* varargs */ Builder cipherSuites(String ... arrstring) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            if (arrstring == null) {
                this.cipherSuites = null;
                return this;
            }
            this.cipherSuites = (String[])arrstring.clone();
            return this;
        }

        public Builder supportsTlsExtensions(boolean bl) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS extensions for cleartext connections");
            }
            this.supportsTlsExtensions = bl;
            return this;
        }

        public /* varargs */ Builder tlsVersions(TlsVersion ... arrtlsVersion) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            if (arrtlsVersion.length == 0) {
                throw new IllegalArgumentException("At least one TlsVersion is required");
            }
            String[] arrstring = new String[arrtlsVersion.length];
            for (int i2 = 0; i2 < arrtlsVersion.length; ++i2) {
                arrstring[i2] = arrtlsVersion[i2].javaName;
            }
            this.tlsVersions = arrstring;
            return this;
        }

        public /* varargs */ Builder tlsVersions(String ... arrstring) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            if (arrstring == null) {
                this.tlsVersions = null;
                return this;
            }
            this.tlsVersions = (String[])arrstring.clone();
            return this;
        }
    }

}

