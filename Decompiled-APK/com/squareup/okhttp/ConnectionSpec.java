package com.squareup.okhttp;

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

    public static final class Builder {
        private String[] cipherSuites;
        private boolean supportsTlsExtensions;
        private boolean tls;
        private String[] tlsVersions;

        Builder(boolean z) {
            this.tls = z;
        }

        public Builder(ConnectionSpec connectionSpec) {
            this.tls = connectionSpec.tls;
            this.cipherSuites = connectionSpec.cipherSuites;
            this.tlsVersions = connectionSpec.tlsVersions;
            this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
        }

        public Builder cipherSuites(CipherSuite... cipherSuiteArr) {
            if (this.tls) {
                String[] strArr = new String[cipherSuiteArr.length];
                for (int i = 0; i < cipherSuiteArr.length; i++) {
                    strArr[i] = cipherSuiteArr[i].javaName;
                }
                this.cipherSuites = strArr;
                return this;
            }
            throw new IllegalStateException("no cipher suites for cleartext connections");
        }

        public Builder cipherSuites(String... strArr) {
            if (this.tls) {
                if (strArr == null) {
                    this.cipherSuites = null;
                } else {
                    this.cipherSuites = (String[]) strArr.clone();
                }
                return this;
            }
            throw new IllegalStateException("no cipher suites for cleartext connections");
        }

        public Builder tlsVersions(TlsVersion... tlsVersionArr) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            } else if (tlsVersionArr.length == 0) {
                throw new IllegalArgumentException("At least one TlsVersion is required");
            } else {
                String[] strArr = new String[tlsVersionArr.length];
                for (int i = 0; i < tlsVersionArr.length; i++) {
                    strArr[i] = tlsVersionArr[i].javaName;
                }
                this.tlsVersions = strArr;
                return this;
            }
        }

        public Builder tlsVersions(String... strArr) {
            if (this.tls) {
                if (strArr == null) {
                    this.tlsVersions = null;
                } else {
                    this.tlsVersions = (String[]) strArr.clone();
                }
                return this;
            }
            throw new IllegalStateException("no TLS versions for cleartext connections");
        }

        public Builder supportsTlsExtensions(boolean z) {
            if (this.tls) {
                this.supportsTlsExtensions = z;
                return this;
            }
            throw new IllegalStateException("no TLS extensions for cleartext connections");
        }

        public ConnectionSpec build() {
            return new ConnectionSpec();
        }
    }

    static {
        APPROVED_CIPHER_SUITES = new CipherSuite[]{CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA};
        MODERN_TLS = new Builder(true).cipherSuites(APPROVED_CIPHER_SUITES).tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        COMPATIBLE_TLS = new Builder(MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        CLEARTEXT = new Builder(false).build();
    }

    private ConnectionSpec(Builder builder) {
        this.tls = builder.tls;
        this.cipherSuites = builder.cipherSuites;
        this.tlsVersions = builder.tlsVersions;
        this.supportsTlsExtensions = builder.supportsTlsExtensions;
    }

    public boolean isTls() {
        return this.tls;
    }

    public List<CipherSuite> cipherSuites() {
        if (this.cipherSuites == null) {
            return null;
        }
        Object[] objArr = new CipherSuite[this.cipherSuites.length];
        for (int i = 0; i < this.cipherSuites.length; i++) {
            objArr[i] = CipherSuite.forJavaName(this.cipherSuites[i]);
        }
        return Util.immutableList(objArr);
    }

    public List<TlsVersion> tlsVersions() {
        Object[] objArr = new TlsVersion[this.tlsVersions.length];
        for (int i = 0; i < this.tlsVersions.length; i++) {
            objArr[i] = TlsVersion.forJavaName(this.tlsVersions[i]);
        }
        return Util.immutableList(objArr);
    }

    public boolean supportsTlsExtensions() {
        return this.supportsTlsExtensions;
    }

    void apply(SSLSocket sSLSocket, boolean z) {
        ConnectionSpec supportedSpec = supportedSpec(sSLSocket, z);
        sSLSocket.setEnabledProtocols(supportedSpec.tlsVersions);
        String[] strArr = supportedSpec.cipherSuites;
        if (strArr != null) {
            sSLSocket.setEnabledCipherSuites(strArr);
        }
    }

    private ConnectionSpec supportedSpec(SSLSocket sSLSocket, boolean z) {
        String[] strArr;
        Object obj = null;
        if (this.cipherSuites != null) {
            obj = (String[]) Util.intersect(String.class, this.cipherSuites, sSLSocket.getEnabledCipherSuites());
        }
        if (z) {
            String str = "TLS_FALLBACK_SCSV";
            if (Arrays.asList(sSLSocket.getSupportedCipherSuites()).contains("TLS_FALLBACK_SCSV")) {
                if (obj == null) {
                    obj = sSLSocket.getEnabledCipherSuites();
                }
                strArr = new String[(obj.length + 1)];
                System.arraycopy(obj, 0, strArr, 0, obj.length);
                strArr[strArr.length - 1] = "TLS_FALLBACK_SCSV";
                return new Builder(this).cipherSuites(strArr).tlsVersions((String[]) Util.intersect(String.class, this.tlsVersions, sSLSocket.getEnabledProtocols())).build();
            }
        }
        Object obj2 = obj;
        return new Builder(this).cipherSuites(strArr).tlsVersions((String[]) Util.intersect(String.class, this.tlsVersions, sSLSocket.getEnabledProtocols())).build();
    }

    public boolean isCompatible(SSLSocket sSLSocket) {
        if (!this.tls) {
            return false;
        }
        if (!nonEmptyIntersection(this.tlsVersions, sSLSocket.getEnabledProtocols())) {
            return false;
        }
        if (this.cipherSuites != null) {
            return nonEmptyIntersection(this.cipherSuites, sSLSocket.getEnabledCipherSuites());
        } else if (sSLSocket.getEnabledCipherSuites().length > 0) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean nonEmptyIntersection(String[] strArr, String[] strArr2) {
        if (strArr == null || strArr2 == null || strArr.length == 0 || strArr2.length == 0) {
            return false;
        }
        for (Object contains : strArr) {
            if (contains(strArr2, contains)) {
                return true;
            }
        }
        return false;
    }

    private static <T> boolean contains(T[] tArr, T t) {
        for (Object equal : tArr) {
            if (Util.equal(t, equal)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ConnectionSpec)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        ConnectionSpec connectionSpec = (ConnectionSpec) obj;
        if (this.tls != connectionSpec.tls) {
            return false;
        }
        if (!this.tls || (Arrays.equals(this.cipherSuites, connectionSpec.cipherSuites) && Arrays.equals(this.tlsVersions, connectionSpec.tlsVersions) && this.supportsTlsExtensions == connectionSpec.supportsTlsExtensions)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (!this.tls) {
            return 17;
        }
        return (this.supportsTlsExtensions ? 0 : 1) + ((((Arrays.hashCode(this.cipherSuites) + 527) * 31) + Arrays.hashCode(this.tlsVersions)) * 31);
    }

    public String toString() {
        if (!this.tls) {
            return "ConnectionSpec()";
        }
        List cipherSuites = cipherSuites();
        return "ConnectionSpec(cipherSuites=" + (cipherSuites == null ? "[use default]" : cipherSuites.toString()) + ", tlsVersions=" + tlsVersions() + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
    }
}
