/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.spec.AlgorithmParameterSpec
 *  java.util.HashMap
 *  java.util.Map
 */
package org.bouncycastle.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.util.Arrays;

public class GOST28147ParameterSpec
implements AlgorithmParameterSpec {
    private static Map oidMappings = new HashMap();
    private byte[] iv = null;
    private byte[] sBox = null;

    static {
        oidMappings.put((Object)CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet, (Object)"E-A");
        oidMappings.put((Object)CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_B_ParamSet, (Object)"E-B");
        oidMappings.put((Object)CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_C_ParamSet, (Object)"E-C");
        oidMappings.put((Object)CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_D_ParamSet, (Object)"E-D");
    }

    public GOST28147ParameterSpec(String string) {
        this.sBox = GOST28147Engine.getSBox(string);
    }

    public GOST28147ParameterSpec(String string, byte[] arrby) {
        this(string);
        this.iv = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)this.iv, (int)0, (int)arrby.length);
    }

    public GOST28147ParameterSpec(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] arrby) {
        this(GOST28147ParameterSpec.getName(aSN1ObjectIdentifier));
        this.iv = Arrays.clone(arrby);
    }

    public GOST28147ParameterSpec(byte[] arrby) {
        this.sBox = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)this.sBox, (int)0, (int)arrby.length);
    }

    public GOST28147ParameterSpec(byte[] arrby, byte[] arrby2) {
        this(arrby);
        this.iv = new byte[arrby2.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)this.iv, (int)0, (int)arrby2.length);
    }

    private static String getName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        String string = (String)oidMappings.get((Object)aSN1ObjectIdentifier);
        if (string == null) {
            throw new IllegalArgumentException("unknown OID: " + aSN1ObjectIdentifier);
        }
        return string;
    }

    public byte[] getIV() {
        if (this.iv == null) {
            return null;
        }
        byte[] arrby = new byte[this.iv.length];
        System.arraycopy((Object)this.iv, (int)0, (Object)arrby, (int)0, (int)arrby.length);
        return arrby;
    }

    public byte[] getSbox() {
        return this.sBox;
    }
}

