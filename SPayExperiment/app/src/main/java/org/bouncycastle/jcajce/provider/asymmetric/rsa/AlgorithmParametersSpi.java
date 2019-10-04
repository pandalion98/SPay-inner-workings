/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.AlgorithmParametersSpi
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  java.security.spec.MGF1ParameterSpec
 *  java.security.spec.PSSParameterSpec
 *  javax.crypto.spec.OAEPParameterSpec
 *  javax.crypto.spec.PSource
 *  javax.crypto.spec.PSource$PSpecified
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAESOAEPparams;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.provider.util.DigestFactory;

public abstract class AlgorithmParametersSpi
extends java.security.AlgorithmParametersSpi {
    protected AlgorithmParameterSpec engineGetParameterSpec(Class class_) {
        if (class_ == null) {
            throw new NullPointerException("argument to getParameterSpec must not be null");
        }
        return this.localEngineGetParameterSpec(class_);
    }

    protected boolean isASN1FormatString(String string) {
        return string == null || string.equals((Object)"ASN.1");
    }

    protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(Class var1);

    public static class OAEP
    extends AlgorithmParametersSpi {
        OAEPParameterSpec currentSpec;

        protected byte[] engineGetEncoded() {
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(DigestFactory.getOID(this.currentSpec.getDigestAlgorithm()), DERNull.INSTANCE);
            MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec)this.currentSpec.getMGFParameters();
            AlgorithmIdentifier algorithmIdentifier2 = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(DigestFactory.getOID(mGF1ParameterSpec.getDigestAlgorithm()), DERNull.INSTANCE));
            PSource.PSpecified pSpecified = (PSource.PSpecified)this.currentSpec.getPSource();
            RSAESOAEPparams rSAESOAEPparams = new RSAESOAEPparams(algorithmIdentifier, algorithmIdentifier2, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(pSpecified.getValue())));
            try {
                byte[] arrby = rSAESOAEPparams.getEncoded("DER");
                return arrby;
            }
            catch (IOException iOException) {
                throw new RuntimeException("Error encoding OAEPParameters");
            }
        }

        protected byte[] engineGetEncoded(String string) {
            if (this.isASN1FormatString(string) || string.equalsIgnoreCase("X.509")) {
                return this.engineGetEncoded();
            }
            return null;
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (!(algorithmParameterSpec instanceof OAEPParameterSpec)) {
                throw new InvalidParameterSpecException("OAEPParameterSpec required to initialise an OAEP algorithm parameters object");
            }
            this.currentSpec = (OAEPParameterSpec)algorithmParameterSpec;
        }

        protected void engineInit(byte[] arrby) {
            try {
                RSAESOAEPparams rSAESOAEPparams = RSAESOAEPparams.getInstance(arrby);
                this.currentSpec = new OAEPParameterSpec(rSAESOAEPparams.getHashAlgorithm().getAlgorithm().getId(), rSAESOAEPparams.getMaskGenAlgorithm().getAlgorithm().getId(), (AlgorithmParameterSpec)new MGF1ParameterSpec(AlgorithmIdentifier.getInstance(rSAESOAEPparams.getMaskGenAlgorithm().getParameters()).getAlgorithm().getId()), (PSource)new PSource.PSpecified(ASN1OctetString.getInstance(rSAESOAEPparams.getPSourceAlgorithm().getParameters()).getOctets()));
                return;
            }
            catch (ClassCastException classCastException) {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
        }

        protected void engineInit(byte[] arrby, String string) {
            if (string.equalsIgnoreCase("X.509") || string.equalsIgnoreCase("ASN.1")) {
                this.engineInit(arrby);
                return;
            }
            throw new IOException("Unknown parameter format " + string);
        }

        protected String engineToString() {
            return "OAEP Parameters";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (class_ == OAEPParameterSpec.class && this.currentSpec != null) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to OAEP parameters object.");
        }
    }

    public static class PSS
    extends AlgorithmParametersSpi {
        PSSParameterSpec currentSpec;

        protected byte[] engineGetEncoded() {
            PSSParameterSpec pSSParameterSpec = this.currentSpec;
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(DigestFactory.getOID(pSSParameterSpec.getDigestAlgorithm()), DERNull.INSTANCE);
            MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec)pSSParameterSpec.getMGFParameters();
            return new RSASSAPSSparams(algorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(DigestFactory.getOID(mGF1ParameterSpec.getDigestAlgorithm()), DERNull.INSTANCE)), new ASN1Integer(pSSParameterSpec.getSaltLength()), new ASN1Integer(pSSParameterSpec.getTrailerField())).getEncoded("DER");
        }

        protected byte[] engineGetEncoded(String string) {
            if (string.equalsIgnoreCase("X.509") || string.equalsIgnoreCase("ASN.1")) {
                return this.engineGetEncoded();
            }
            return null;
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
                throw new InvalidParameterSpecException("PSSParameterSpec required to initialise an PSS algorithm parameters object");
            }
            this.currentSpec = (PSSParameterSpec)algorithmParameterSpec;
        }

        protected void engineInit(byte[] arrby) {
            try {
                RSASSAPSSparams rSASSAPSSparams = RSASSAPSSparams.getInstance(arrby);
                this.currentSpec = new PSSParameterSpec(rSASSAPSSparams.getHashAlgorithm().getAlgorithm().getId(), rSASSAPSSparams.getMaskGenAlgorithm().getAlgorithm().getId(), (AlgorithmParameterSpec)new MGF1ParameterSpec(AlgorithmIdentifier.getInstance(rSASSAPSSparams.getMaskGenAlgorithm().getParameters()).getAlgorithm().getId()), rSASSAPSSparams.getSaltLength().intValue(), rSASSAPSSparams.getTrailerField().intValue());
                return;
            }
            catch (ClassCastException classCastException) {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
        }

        protected void engineInit(byte[] arrby, String string) {
            if (this.isASN1FormatString(string) || string.equalsIgnoreCase("X.509")) {
                this.engineInit(arrby);
                return;
            }
            throw new IOException("Unknown parameter format " + string);
        }

        protected String engineToString() {
            return "PSS Parameters";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (class_ == PSSParameterSpec.class && this.currentSpec != null) {
                return this.currentSpec;
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PSS parameters object.");
        }
    }

}

