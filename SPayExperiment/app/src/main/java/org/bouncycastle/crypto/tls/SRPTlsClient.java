/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.AbstractTlsClient;
import org.bouncycastle.crypto.tls.DefaultTlsCipherFactory;
import org.bouncycastle.crypto.tls.DefaultTlsSRPGroupVerifier;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsClientContext;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsSRPGroupVerifier;
import org.bouncycastle.crypto.tls.TlsSRPKeyExchange;
import org.bouncycastle.crypto.tls.TlsSRPUtils;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class SRPTlsClient
extends AbstractTlsClient {
    protected TlsSRPGroupVerifier groupVerifier;
    protected byte[] identity;
    protected byte[] password;

    public SRPTlsClient(TlsCipherFactory tlsCipherFactory, TlsSRPGroupVerifier tlsSRPGroupVerifier, byte[] arrby, byte[] arrby2) {
        super(tlsCipherFactory);
        this.groupVerifier = tlsSRPGroupVerifier;
        this.identity = Arrays.clone((byte[])arrby);
        this.password = Arrays.clone((byte[])arrby2);
    }

    public SRPTlsClient(TlsCipherFactory tlsCipherFactory, byte[] arrby, byte[] arrby2) {
        this(tlsCipherFactory, new DefaultTlsSRPGroupVerifier(), arrby, arrby2);
    }

    public SRPTlsClient(byte[] arrby, byte[] arrby2) {
        this(new DefaultTlsCipherFactory(), new DefaultTlsSRPGroupVerifier(), arrby, arrby2);
    }

    protected TlsKeyExchange createSRPKeyExchange(int n2) {
        return new TlsSRPKeyExchange(n2, this.supportedSignatureAlgorithms, this.groupVerifier, this.identity, this.password);
    }

    @Override
    public TlsAuthentication getAuthentication() {
        throw new TlsFatalAlert(80);
    }

    @Override
    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 49178: 
            case 49179: 
            case 49180: {
                return this.cipherFactory.createCipher(this.context, 7, 2);
            }
            case 49181: 
            case 49182: 
            case 49183: {
                return this.cipherFactory.createCipher(this.context, 8, 2);
            }
            case 49184: 
            case 49185: 
            case 49186: 
        }
        return this.cipherFactory.createCipher(this.context, 9, 2);
    }

    @Override
    public int[] getCipherSuites() {
        return new int[]{49182};
    }

    @Override
    public Hashtable getClientExtensions() {
        Hashtable hashtable = TlsExtensionsUtils.ensureExtensionsInitialised(super.getClientExtensions());
        TlsSRPUtils.addSRPExtension(hashtable, this.identity);
        return hashtable;
    }

    @Override
    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 49178: 
            case 49181: 
            case 49184: {
                return this.createSRPKeyExchange(21);
            }
            case 49179: 
            case 49182: 
            case 49185: {
                return this.createSRPKeyExchange(23);
            }
            case 49180: 
            case 49183: 
            case 49186: 
        }
        return this.createSRPKeyExchange(22);
    }

    @Override
    public void processServerExtensions(Hashtable hashtable) {
        if (!TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsSRPUtils.EXT_SRP, (short)47) && this.requireSRPServerExtension()) {
            throw new TlsFatalAlert(47);
        }
        super.processServerExtensions(hashtable);
    }

    protected boolean requireSRPServerExtension() {
        return false;
    }
}

