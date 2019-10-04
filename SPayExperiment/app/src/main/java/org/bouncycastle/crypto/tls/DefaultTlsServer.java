/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.util.Vector;
import org.bouncycastle.crypto.agreement.DHStandardGroups;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.tls.AbstractTlsServer;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsDHEKeyExchange;
import org.bouncycastle.crypto.tls.TlsDHKeyExchange;
import org.bouncycastle.crypto.tls.TlsECDHEKeyExchange;
import org.bouncycastle.crypto.tls.TlsECDHKeyExchange;
import org.bouncycastle.crypto.tls.TlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsRSAKeyExchange;
import org.bouncycastle.crypto.tls.TlsServerContext;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;

public abstract class DefaultTlsServer
extends AbstractTlsServer {
    public DefaultTlsServer() {
    }

    public DefaultTlsServer(TlsCipherFactory tlsCipherFactory) {
        super(tlsCipherFactory);
    }

    protected TlsKeyExchange createDHEKeyExchange(int n2) {
        return new TlsDHEKeyExchange(n2, this.supportedSignatureAlgorithms, this.getDHParameters());
    }

    protected TlsKeyExchange createDHKeyExchange(int n2) {
        return new TlsDHKeyExchange(n2, this.supportedSignatureAlgorithms, this.getDHParameters());
    }

    protected TlsKeyExchange createECDHEKeyExchange(int n2) {
        return new TlsECDHEKeyExchange(n2, this.supportedSignatureAlgorithms, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }

    protected TlsKeyExchange createECDHKeyExchange(int n2) {
        return new TlsECDHKeyExchange(n2, this.supportedSignatureAlgorithms, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }

    protected TlsKeyExchange createRSAKeyExchange() {
        return new TlsRSAKeyExchange(this.supportedSignatureAlgorithms);
    }

    @Override
    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 10: 
            case 13: 
            case 16: 
            case 19: 
            case 22: 
            case 49155: 
            case 49160: 
            case 49165: 
            case 49170: {
                return this.cipherFactory.createCipher(this.context, 7, 2);
            }
            case 52243: 
            case 52244: 
            case 52245: {
                return this.cipherFactory.createCipher(this.context, 102, 0);
            }
            case 47: 
            case 48: 
            case 49: 
            case 50: 
            case 51: 
            case 49156: 
            case 49161: 
            case 49166: 
            case 49171: {
                return this.cipherFactory.createCipher(this.context, 8, 2);
            }
            case 60: 
            case 62: 
            case 63: 
            case 64: 
            case 103: 
            case 49187: 
            case 49189: 
            case 49191: 
            case 49193: {
                return this.cipherFactory.createCipher(this.context, 8, 3);
            }
            case 49308: 
            case 49310: 
            case 49324: {
                return this.cipherFactory.createCipher(this.context, 15, 0);
            }
            case 49312: 
            case 49314: 
            case 49326: {
                return this.cipherFactory.createCipher(this.context, 16, 0);
            }
            case 156: 
            case 158: 
            case 160: 
            case 162: 
            case 164: 
            case 49195: 
            case 49197: 
            case 49199: 
            case 49201: {
                return this.cipherFactory.createCipher(this.context, 10, 0);
            }
            case 53: 
            case 54: 
            case 55: 
            case 56: 
            case 57: 
            case 49157: 
            case 49162: 
            case 49167: 
            case 49172: {
                return this.cipherFactory.createCipher(this.context, 9, 2);
            }
            case 61: 
            case 104: 
            case 105: 
            case 106: 
            case 107: {
                return this.cipherFactory.createCipher(this.context, 9, 3);
            }
            case 49188: 
            case 49190: 
            case 49192: 
            case 49194: {
                return this.cipherFactory.createCipher(this.context, 9, 4);
            }
            case 49309: 
            case 49311: 
            case 49325: {
                return this.cipherFactory.createCipher(this.context, 17, 0);
            }
            case 49313: 
            case 49315: 
            case 49327: {
                return this.cipherFactory.createCipher(this.context, 18, 0);
            }
            case 157: 
            case 159: 
            case 161: 
            case 163: 
            case 165: 
            case 49196: 
            case 49198: 
            case 49200: 
            case 49202: {
                return this.cipherFactory.createCipher(this.context, 11, 0);
            }
            case 65: 
            case 66: 
            case 67: 
            case 68: 
            case 69: {
                return this.cipherFactory.createCipher(this.context, 12, 2);
            }
            case 186: 
            case 187: 
            case 188: 
            case 189: 
            case 190: 
            case 49266: 
            case 49268: 
            case 49270: 
            case 49272: {
                return this.cipherFactory.createCipher(this.context, 12, 3);
            }
            case 49274: 
            case 49276: 
            case 49278: 
            case 49280: 
            case 49282: 
            case 49286: 
            case 49288: 
            case 49290: 
            case 49292: {
                return this.cipherFactory.createCipher(this.context, 19, 0);
            }
            case 132: 
            case 133: 
            case 134: 
            case 135: 
            case 136: {
                return this.cipherFactory.createCipher(this.context, 13, 2);
            }
            case 192: 
            case 193: 
            case 194: 
            case 195: 
            case 196: {
                return this.cipherFactory.createCipher(this.context, 13, 3);
            }
            case 49275: 
            case 49277: 
            case 49279: 
            case 49281: 
            case 49283: 
            case 49287: 
            case 49289: 
            case 49291: 
            case 49293: {
                return this.cipherFactory.createCipher(this.context, 20, 0);
            }
            case 49267: 
            case 49269: 
            case 49271: 
            case 49273: {
                return this.cipherFactory.createCipher(this.context, 13, 4);
            }
            case 58384: 
            case 58386: 
            case 58388: 
            case 58398: {
                return this.cipherFactory.createCipher(this.context, 100, 2);
            }
            case 1: {
                return this.cipherFactory.createCipher(this.context, 0, 1);
            }
            case 2: 
            case 49153: 
            case 49158: 
            case 49163: 
            case 49168: {
                return this.cipherFactory.createCipher(this.context, 0, 2);
            }
            case 59: {
                return this.cipherFactory.createCipher(this.context, 0, 3);
            }
            case 4: {
                return this.cipherFactory.createCipher(this.context, 2, 1);
            }
            case 5: 
            case 49154: 
            case 49159: 
            case 49164: 
            case 49169: {
                return this.cipherFactory.createCipher(this.context, 2, 2);
            }
            case 58385: 
            case 58387: 
            case 58389: 
            case 58399: {
                return this.cipherFactory.createCipher(this.context, 101, 2);
            }
            case 150: 
            case 151: 
            case 152: 
            case 153: 
            case 154: 
        }
        return this.cipherFactory.createCipher(this.context, 14, 2);
    }

    @Override
    protected int[] getCipherSuites() {
        return new int[]{49200, 49199, 49192, 49191, 49172, 49171, 157, 156, 61, 60, 53, 47};
    }

    @Override
    public TlsCredentials getCredentials() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 12: 
            case 13: 
            case 18: 
            case 19: 
            case 48: 
            case 50: 
            case 54: 
            case 56: 
            case 62: 
            case 64: 
            case 66: 
            case 68: 
            case 104: 
            case 106: 
            case 133: 
            case 135: 
            case 151: 
            case 153: 
            case 162: 
            case 163: 
            case 164: 
            case 165: 
            case 187: 
            case 189: 
            case 193: 
            case 195: 
            case 49180: 
            case 49183: 
            case 49186: 
            case 49280: 
            case 49281: 
            case 49282: 
            case 49283: {
                return this.getDSASignerCredentials();
            }
            case 49153: 
            case 49154: 
            case 49155: 
            case 49156: 
            case 49157: 
            case 49158: 
            case 49159: 
            case 49160: 
            case 49161: 
            case 49162: 
            case 49187: 
            case 49188: 
            case 49189: 
            case 49190: 
            case 49195: 
            case 49196: 
            case 49197: 
            case 49198: 
            case 49266: 
            case 49267: 
            case 49268: 
            case 49269: 
            case 49286: 
            case 49287: 
            case 49288: 
            case 49289: 
            case 49324: 
            case 49325: 
            case 49326: 
            case 49327: 
            case 52244: 
            case 58388: 
            case 58389: {
                return this.getECDSASignerCredentials();
            }
            case 1: 
            case 2: 
            case 4: 
            case 5: 
            case 10: 
            case 47: 
            case 53: 
            case 59: 
            case 60: 
            case 61: 
            case 65: 
            case 132: 
            case 150: 
            case 156: 
            case 157: 
            case 186: 
            case 192: 
            case 49274: 
            case 49275: 
            case 49308: 
            case 49309: 
            case 49312: 
            case 49313: {
                return this.getRSAEncryptionCredentials();
            }
            case 22: 
            case 51: 
            case 57: 
            case 69: 
            case 103: 
            case 107: 
            case 136: 
            case 154: 
            case 158: 
            case 159: 
            case 190: 
            case 196: 
            case 49168: 
            case 49169: 
            case 49170: 
            case 49171: 
            case 49172: 
            case 49191: 
            case 49192: 
            case 49199: 
            case 49200: 
            case 49270: 
            case 49271: 
            case 49276: 
            case 49277: 
            case 49290: 
            case 49291: 
            case 49310: 
            case 49311: 
            case 49314: 
            case 49315: 
            case 52243: 
            case 52245: 
            case 58386: 
            case 58387: 
        }
        return this.getRSASignerCredentials();
    }

    protected DHParameters getDHParameters() {
        return DHStandardGroups.rfc5114_1024_160;
    }

    protected TlsSignerCredentials getDSASignerCredentials() {
        throw new TlsFatalAlert(80);
    }

    protected TlsSignerCredentials getECDSASignerCredentials() {
        throw new TlsFatalAlert(80);
    }

    @Override
    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 13: 
            case 48: 
            case 54: 
            case 62: 
            case 66: 
            case 104: 
            case 133: 
            case 151: 
            case 164: 
            case 165: 
            case 187: 
            case 193: 
            case 49282: 
            case 49283: {
                return this.createDHKeyExchange(7);
            }
            case 16: 
            case 49: 
            case 55: 
            case 63: 
            case 67: 
            case 105: 
            case 134: 
            case 152: 
            case 160: 
            case 161: 
            case 188: 
            case 194: 
            case 49278: 
            case 49279: {
                return this.createDHKeyExchange(9);
            }
            case 19: 
            case 50: 
            case 56: 
            case 64: 
            case 68: 
            case 106: 
            case 135: 
            case 153: 
            case 162: 
            case 163: 
            case 189: 
            case 195: 
            case 49280: 
            case 49281: {
                return this.createDHEKeyExchange(3);
            }
            case 22: 
            case 51: 
            case 57: 
            case 69: 
            case 103: 
            case 107: 
            case 136: 
            case 154: 
            case 158: 
            case 159: 
            case 190: 
            case 196: 
            case 49276: 
            case 49277: 
            case 49310: 
            case 49311: 
            case 49314: 
            case 49315: 
            case 52245: 
            case 58398: 
            case 58399: {
                return this.createDHEKeyExchange(5);
            }
            case 49153: 
            case 49154: 
            case 49155: 
            case 49156: 
            case 49157: 
            case 49189: 
            case 49190: 
            case 49197: 
            case 49198: 
            case 49268: 
            case 49269: 
            case 49288: 
            case 49289: {
                return this.createECDHKeyExchange(16);
            }
            case 49163: 
            case 49164: 
            case 49165: 
            case 49166: 
            case 49167: 
            case 49193: 
            case 49194: 
            case 49201: 
            case 49202: 
            case 49272: 
            case 49273: 
            case 49292: 
            case 49293: {
                return this.createECDHKeyExchange(18);
            }
            case 49158: 
            case 49159: 
            case 49160: 
            case 49161: 
            case 49162: 
            case 49187: 
            case 49188: 
            case 49195: 
            case 49196: 
            case 49266: 
            case 49267: 
            case 49286: 
            case 49287: 
            case 49324: 
            case 49325: 
            case 49326: 
            case 49327: 
            case 52244: 
            case 58388: 
            case 58389: {
                return this.createECDHEKeyExchange(17);
            }
            case 49168: 
            case 49169: 
            case 49170: 
            case 49171: 
            case 49172: 
            case 49191: 
            case 49192: 
            case 49199: 
            case 49200: 
            case 49270: 
            case 49271: 
            case 49290: 
            case 49291: 
            case 52243: 
            case 58386: 
            case 58387: {
                return this.createECDHEKeyExchange(19);
            }
            case 1: 
            case 2: 
            case 4: 
            case 5: 
            case 10: 
            case 47: 
            case 53: 
            case 59: 
            case 60: 
            case 61: 
            case 65: 
            case 132: 
            case 150: 
            case 156: 
            case 157: 
            case 186: 
            case 192: 
            case 49274: 
            case 49275: 
            case 49308: 
            case 49309: 
            case 49312: 
            case 49313: 
            case 58384: 
            case 58385: 
        }
        return this.createRSAKeyExchange();
    }

    protected TlsEncryptionCredentials getRSAEncryptionCredentials() {
        throw new TlsFatalAlert(80);
    }

    protected TlsSignerCredentials getRSASignerCredentials() {
        throw new TlsFatalAlert(80);
    }
}

