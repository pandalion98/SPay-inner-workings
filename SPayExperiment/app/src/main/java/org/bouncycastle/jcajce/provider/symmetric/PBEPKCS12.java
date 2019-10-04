/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public class PBEPKCS12 {
    private PBEPKCS12() {
    }

    public static class AlgParams
    extends BaseAlgorithmParameters {
        PKCS12PBEParams params;

        protected byte[] engineGetEncoded() {
            try {
                byte[] arrby = this.params.getEncoded("DER");
                return arrby;
            }
            catch (IOException iOException) {
                throw new RuntimeException("Oooops! " + iOException.toString());
            }
        }

        protected byte[] engineGetEncoded(String string) {
            if (this.isASN1FormatString(string)) {
                return this.engineGetEncoded();
            }
            return null;
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PKCS12 PBE parameters algorithm parameters object");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            this.params = new PKCS12PBEParams(pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
        }

        protected void engineInit(byte[] arrby) {
            this.params = PKCS12PBEParams.getInstance(ASN1Primitive.fromByteArray(arrby));
        }

        protected void engineInit(byte[] arrby, String string) {
            if (this.isASN1FormatString(string)) {
                this.engineInit(arrby);
                return;
            }
            throw new IOException("Unknown parameters format in PKCS12 PBE parameters object");
        }

        protected String engineToString() {
            return "PKCS12 PBE Parameters";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (class_ == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getIV(), this.params.getIterations().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PKCS12 PBE parameters object.");
        }
    }

    public static class Mappings
    extends AlgorithmProvider {
        private static final String PREFIX = PBEPKCS12.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.PKCS12PBE", PREFIX + "$AlgParams");
        }
    }

}

