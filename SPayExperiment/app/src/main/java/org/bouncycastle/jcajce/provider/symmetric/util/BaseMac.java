/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.spec.AlgorithmParameterSpec
 *  java.util.Hashtable
 *  java.util.Map
 *  java.util.Set
 *  javax.crypto.MacSpi
 *  javax.crypto.spec.IvParameterSpec
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.crypto.MacSpi;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.SkeinParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;

public class BaseMac
extends MacSpi
implements PBE {
    private int keySize = 160;
    private Mac macEngine;
    private int pbeHash = 1;
    private int pbeType = 2;

    protected BaseMac(Mac mac) {
        this.macEngine = mac;
    }

    protected BaseMac(Mac mac, int n, int n2, int n3) {
        this.macEngine = mac;
        this.pbeType = n;
        this.pbeHash = n2;
        this.keySize = n3;
    }

    private static Hashtable copyMap(Map map) {
        Hashtable hashtable = new Hashtable();
        for (Object object : map.keySet()) {
            hashtable.put(object, map.get(object));
        }
        return hashtable;
    }

    protected byte[] engineDoFinal() {
        byte[] arrby = new byte[this.engineGetMacLength()];
        this.macEngine.doFinal(arrby, 0);
        return arrby;
    }

    protected int engineGetMacLength() {
        return this.macEngine.getMacSize();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        CipherParameters cipherParameters;
        if (key == null) {
            throw new InvalidKeyException("key is null");
        }
        if (key instanceof BCPBEKey) {
            BCPBEKey bCPBEKey = (BCPBEKey)key;
            if (bCPBEKey.getParam() != null) {
                cipherParameters = bCPBEKey.getParam();
            } else {
                if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                cipherParameters = PBE.Util.makePBEMacParameters(bCPBEKey, algorithmParameterSpec);
            }
        } else if (algorithmParameterSpec instanceof IvParameterSpec) {
            cipherParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
        } else if (algorithmParameterSpec instanceof SkeinParameterSpec) {
            cipherParameters = new SkeinParameters.Builder(BaseMac.copyMap(((SkeinParameterSpec)algorithmParameterSpec).getParameters())).setKey(key.getEncoded()).build();
        } else {
            if (algorithmParameterSpec != null) {
                throw new InvalidAlgorithmParameterException("unknown parameter type.");
            }
            cipherParameters = new KeyParameter(key.getEncoded());
        }
        this.macEngine.init(cipherParameters);
    }

    protected void engineReset() {
        this.macEngine.reset();
    }

    protected void engineUpdate(byte by) {
        this.macEngine.update(by);
    }

    protected void engineUpdate(byte[] arrby, int n, int n2) {
        this.macEngine.update(arrby, n, n2);
    }
}

