package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import java.util.Map;
import javax.crypto.MacSpi;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.SkeinParameters.Builder;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE.Util;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;

public class BaseMac extends MacSpi implements PBE {
    private int keySize;
    private Mac macEngine;
    private int pbeHash;
    private int pbeType;

    protected BaseMac(Mac mac) {
        this.pbeType = 2;
        this.pbeHash = 1;
        this.keySize = CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256;
        this.macEngine = mac;
    }

    protected BaseMac(Mac mac, int i, int i2, int i3) {
        this.pbeType = 2;
        this.pbeHash = 1;
        this.keySize = CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256;
        this.macEngine = mac;
        this.pbeType = i;
        this.pbeHash = i2;
        this.keySize = i3;
    }

    private static Hashtable copyMap(Map map) {
        Hashtable hashtable = new Hashtable();
        for (Object next : map.keySet()) {
            hashtable.put(next, map.get(next));
        }
        return hashtable;
    }

    protected byte[] engineDoFinal() {
        byte[] bArr = new byte[engineGetMacLength()];
        this.macEngine.doFinal(bArr, 0);
        return bArr;
    }

    protected int engineGetMacLength() {
        return this.macEngine.getMacSize();
    }

    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        if (key == null) {
            throw new InvalidKeyException("key is null");
        }
        CipherParameters param;
        if (key instanceof BCPBEKey) {
            BCPBEKey bCPBEKey = (BCPBEKey) key;
            if (bCPBEKey.getParam() != null) {
                param = bCPBEKey.getParam();
            } else if (algorithmParameterSpec instanceof PBEParameterSpec) {
                param = Util.makePBEMacParameters(bCPBEKey, algorithmParameterSpec);
            } else {
                throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
            }
        } else if (algorithmParameterSpec instanceof IvParameterSpec) {
            param = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec) algorithmParameterSpec).getIV());
        } else if (algorithmParameterSpec instanceof SkeinParameterSpec) {
            param = new Builder(copyMap(((SkeinParameterSpec) algorithmParameterSpec).getParameters())).setKey(key.getEncoded()).build();
        } else if (algorithmParameterSpec == null) {
            param = new KeyParameter(key.getEncoded());
        } else {
            throw new InvalidAlgorithmParameterException("unknown parameter type.");
        }
        this.macEngine.init(param);
    }

    protected void engineReset() {
        this.macEngine.reset();
    }

    protected void engineUpdate(byte b) {
        this.macEngine.update(b);
    }

    protected void engineUpdate(byte[] bArr, int i, int i2) {
        this.macEngine.update(bArr, i, i2);
    }
}
