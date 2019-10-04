/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.Key
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.KeySpec
 *  javax.crypto.Mac
 *  javax.crypto.SecretKey
 *  javax.crypto.SecretKeyFactory
 *  javax.crypto.spec.PBEKeySpec
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.MacData;
import org.bouncycastle.asn1.pkcs.Pfx;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;

public class PKCS12Util {
    private static byte[] calculatePbeMac(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] arrby, int n, char[] arrc, byte[] arrby2, String string) {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance((String)aSN1ObjectIdentifier.getId(), (String)string);
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(arrby, n);
        SecretKey secretKey = secretKeyFactory.generateSecret((KeySpec)new PBEKeySpec(arrc));
        Mac mac = Mac.getInstance((String)aSN1ObjectIdentifier.getId(), (String)string);
        mac.init((Key)secretKey, (AlgorithmParameterSpec)pBEParameterSpec);
        mac.update(arrby2);
        return mac.doFinal();
    }

    public static byte[] convertToDefiniteLength(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream = new DEROutputStream((OutputStream)byteArrayOutputStream);
        Pfx pfx = Pfx.getInstance(arrby);
        byteArrayOutputStream.reset();
        dEROutputStream.writeObject(pfx);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] convertToDefiniteLength(byte[] arrby, char[] arrc, String string) {
        MacData macData;
        Pfx pfx = Pfx.getInstance(arrby);
        ContentInfo contentInfo = pfx.getAuthSafe();
        ASN1OctetString aSN1OctetString = ASN1OctetString.getInstance(contentInfo.getContent());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream = new DEROutputStream((OutputStream)byteArrayOutputStream);
        dEROutputStream.writeObject(new ASN1InputStream(aSN1OctetString.getOctets()).readObject());
        ContentInfo contentInfo2 = new ContentInfo(contentInfo.getContentType(), new DEROctetString(byteArrayOutputStream.toByteArray()));
        MacData macData2 = pfx.getMacData();
        try {
            int n = macData2.getIterationCount().intValue();
            byte[] arrby2 = ASN1OctetString.getInstance(contentInfo2.getContent()).getOctets();
            byte[] arrby3 = PKCS12Util.calculatePbeMac(macData2.getMac().getAlgorithmId().getObjectId(), macData2.getSalt(), n, arrc, arrby2, string);
            macData = new MacData(new DigestInfo(new AlgorithmIdentifier(macData2.getMac().getAlgorithmId().getObjectId(), DERNull.INSTANCE), arrby3), macData2.getSalt(), n);
        }
        catch (Exception exception) {
            throw new IOException("error constructing MAC: " + exception.toString());
        }
        Pfx pfx2 = new Pfx(contentInfo2, macData);
        byteArrayOutputStream.reset();
        dEROutputStream.writeObject(pfx2);
        return byteArrayOutputStream.toByteArray();
    }
}

