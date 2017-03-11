package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.util.Vector;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class AbstractTlsKeyExchange implements TlsKeyExchange {
    protected TlsContext context;
    protected int keyExchange;
    protected Vector supportedSignatureAlgorithms;

    protected AbstractTlsKeyExchange(int i, Vector vector) {
        this.keyExchange = i;
        this.supportedSignatureAlgorithms = vector;
    }

    public byte[] generateServerKeyExchange() {
        if (!requiresServerKeyExchange()) {
            return null;
        }
        throw new TlsFatalAlert((short) 80);
    }

    public void init(TlsContext tlsContext) {
        this.context = tlsContext;
        ProtocolVersion clientVersion = tlsContext.getClientVersion();
        if (TlsUtils.isSignatureAlgorithmsExtensionAllowed(clientVersion)) {
            if (this.supportedSignatureAlgorithms == null) {
                switch (this.keyExchange) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    case NamedCurve.sect283k1 /*9*/:
                    case NamedCurve.secp160k1 /*15*/:
                    case NamedCurve.secp192k1 /*18*/:
                    case NamedCurve.secp192r1 /*19*/:
                    case NamedCurve.secp256r1 /*23*/:
                        this.supportedSignatureAlgorithms = TlsUtils.getDefaultRSASignatureAlgorithms();
                    case F2m.PPB /*3*/:
                    case ECCurve.COORD_SKEWED /*7*/:
                    case NamedCurve.secp256k1 /*22*/:
                        this.supportedSignatureAlgorithms = TlsUtils.getDefaultDSSSignatureAlgorithms();
                    case NamedCurve.sect571k1 /*13*/:
                    case NamedCurve.sect571r1 /*14*/:
                    case NamedCurve.secp224r1 /*21*/:
                    case NamedCurve.secp384r1 /*24*/:
                    case X509KeyUsage.dataEncipherment /*16*/:
                    case NamedCurve.secp160r2 /*17*/:
                        this.supportedSignatureAlgorithms = TlsUtils.getDefaultECDSASignatureAlgorithms();
                    default:
                        throw new IllegalStateException("unsupported key exchange algorithm");
                }
            }
        } else if (this.supportedSignatureAlgorithms != null) {
            throw new IllegalStateException("supported_signature_algorithms not allowed for " + clientVersion);
        }
    }

    public void processClientCertificate(Certificate certificate) {
    }

    public void processClientKeyExchange(InputStream inputStream) {
        throw new TlsFatalAlert((short) 80);
    }

    public void processServerCertificate(Certificate certificate) {
        if (this.supportedSignatureAlgorithms != null) {
        }
    }

    public void processServerCredentials(TlsCredentials tlsCredentials) {
        processServerCertificate(tlsCredentials.getCertificate());
    }

    public void processServerKeyExchange(InputStream inputStream) {
        if (!requiresServerKeyExchange()) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    public boolean requiresServerKeyExchange() {
        return false;
    }

    public void skipClientCredentials() {
    }

    public void skipServerKeyExchange() {
        if (requiresServerKeyExchange()) {
            throw new TlsFatalAlert((short) 10);
        }
    }
}
