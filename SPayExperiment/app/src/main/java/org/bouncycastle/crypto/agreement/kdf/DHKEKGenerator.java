/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.agreement.kdf;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle.util.Pack;

public class DHKEKGenerator
implements DerivationFunction {
    private ASN1ObjectIdentifier algorithm;
    private final Digest digest;
    private int keySize;
    private byte[] partyAInfo;
    private byte[] z;

    public DHKEKGenerator(Digest digest) {
        this.digest = digest;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public int generateBytes(byte[] var1_1, int var2_2, int var3_3) {
        if (var1_1.length - var3_3 < var2_2) {
            throw new DataLengthException("output buffer too small");
        }
        var4_4 = var3_3;
        var6_5 = this.digest.getDigestSize();
        if (var4_4 > 0x1FFFFFFFFL) {
            throw new IllegalArgumentException("Output length too large");
        }
        var7_6 = (int)((var4_4 + (long)var6_5 - 1L) / (long)var6_5);
        var8_7 = new byte[this.digest.getDigestSize()];
        var9_8 = 0;
        var10_9 = 1;
        var11_10 = var3_3;
        var12_11 = var2_2;
        do lbl-1000: // 2 sources:
        {
            if (var9_8 >= var7_6) {
                this.digest.reset();
                return (int)var4_4;
            }
            this.digest.update(this.z, 0, this.z.length);
            var13_12 = new ASN1EncodableVector();
            var14_13 = new ASN1EncodableVector();
            var14_13.add(this.algorithm);
            var14_13.add(new DEROctetString(Pack.intToBigEndian((int)var10_9)));
            var13_12.add(new DERSequence(var14_13));
            if (this.partyAInfo != null) {
                var13_12.add(new DERTaggedObject(true, 0, new DEROctetString(this.partyAInfo)));
            }
            var13_12.add(new DERTaggedObject(true, 2, new DEROctetString(Pack.intToBigEndian((int)this.keySize))));
            var16_14 = new DERSequence(var13_12).getEncoded("DER");
            this.digest.update(var16_14, 0, var16_14.length);
            break;
        } while (true);
        catch (IOException var15_15) {
            throw new IllegalArgumentException("unable to encode parameter info: " + var15_15.getMessage());
        }
        {
            this.digest.doFinal(var8_7, 0);
            if (var11_10 > var6_5) {
                System.arraycopy((Object)var8_7, (int)0, (Object)var1_1, (int)var12_11, (int)var6_5);
                var12_11 += var6_5;
                var11_10 -= var6_5;
            } else {
                System.arraycopy((Object)var8_7, (int)0, (Object)var1_1, (int)var12_11, (int)var11_10);
            }
            ++var10_9;
            ++var9_8;
            ** while (true)
        }
    }

    public Digest getDigest() {
        return this.digest;
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        DHKDFParameters dHKDFParameters = (DHKDFParameters)derivationParameters;
        this.algorithm = dHKDFParameters.getAlgorithm();
        this.keySize = dHKDFParameters.getKeySize();
        this.z = dHKDFParameters.getZ();
        this.partyAInfo = dHKDFParameters.getExtraInfo();
    }
}

