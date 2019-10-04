/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.util.StringTokenizer
 *  org.bouncycastle.util.IPAddress
 */
package org.bouncycastle.asn1.x509;

import java.io.IOException;
import java.util.StringTokenizer;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.util.IPAddress;

public class GeneralName
extends ASN1Object
implements ASN1Choice {
    public static final int dNSName = 2;
    public static final int directoryName = 4;
    public static final int ediPartyName = 5;
    public static final int iPAddress = 7;
    public static final int otherName = 0;
    public static final int registeredID = 8;
    public static final int rfc822Name = 1;
    public static final int uniformResourceIdentifier = 6;
    public static final int x400Address = 3;
    private ASN1Encodable obj;
    private int tag;

    public GeneralName(int n2, String string) {
        this.tag = n2;
        if (n2 == 1 || n2 == 2 || n2 == 6) {
            this.obj = new DERIA5String(string);
            return;
        }
        if (n2 == 8) {
            this.obj = new ASN1ObjectIdentifier(string);
            return;
        }
        if (n2 == 4) {
            this.obj = new X500Name(string);
            return;
        }
        if (n2 == 7) {
            byte[] arrby = this.toGeneralNameEncoding(string);
            if (arrby != null) {
                this.obj = new DEROctetString(arrby);
                return;
            }
            throw new IllegalArgumentException("IP Address is invalid");
        }
        throw new IllegalArgumentException("can't process String for tag: " + n2);
    }

    public GeneralName(int n2, ASN1Encodable aSN1Encodable) {
        this.obj = aSN1Encodable;
        this.tag = n2;
    }

    public GeneralName(X500Name x500Name) {
        this.obj = x500Name;
        this.tag = 4;
    }

    public GeneralName(X509Name x509Name) {
        this.obj = X500Name.getInstance(x509Name);
        this.tag = 4;
    }

    private void copyInts(int[] arrn, byte[] arrby, int n2) {
        for (int i2 = 0; i2 != arrn.length; ++i2) {
            arrby[n2 + i2 * 2] = (byte)(arrn[i2] >> 8);
            arrby[n2 + (1 + i2 * 2)] = (byte)arrn[i2];
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static GeneralName getInstance(Object var0) {
        if (var0 == null) return (GeneralName)var0;
        if (var0 instanceof GeneralName) {
            return (GeneralName)var0;
        }
        if (!(var0 instanceof ASN1TaggedObject)) ** GOTO lbl-1000
        var3_1 = (ASN1TaggedObject)var0;
        var4_2 = var3_1.getTagNo();
        switch (var4_2) {
            default: lbl-1000: // 2 sources:
            {
                if (var0 instanceof byte[] == false) throw new IllegalArgumentException("unknown object in getInstance: " + var0.getClass().getName());
                return GeneralName.getInstance(ASN1Primitive.fromByteArray((byte[])var0));
            }
            case 0: {
                return new GeneralName(var4_2, ASN1Sequence.getInstance(var3_1, false));
            }
            case 1: {
                return new GeneralName(var4_2, DERIA5String.getInstance(var3_1, false));
            }
            case 2: {
                return new GeneralName(var4_2, DERIA5String.getInstance(var3_1, false));
            }
            case 3: {
                throw new IllegalArgumentException("unknown tag: " + var4_2);
            }
            case 4: {
                return new GeneralName(var4_2, X500Name.getInstance(var3_1, true));
            }
            case 5: {
                return new GeneralName(var4_2, ASN1Sequence.getInstance(var3_1, false));
            }
            case 6: {
                return new GeneralName(var4_2, DERIA5String.getInstance(var3_1, false));
            }
            case 7: {
                return new GeneralName(var4_2, ASN1OctetString.getInstance(var3_1, false));
            }
            case 8: 
        }
        return new GeneralName(var4_2, ASN1ObjectIdentifier.getInstance(var3_1, false));
        catch (IOException var1_4) {
            throw new IllegalArgumentException("unable to parse encoded general name");
        }
    }

    public static GeneralName getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return GeneralName.getInstance(ASN1TaggedObject.getInstance(aSN1TaggedObject, true));
    }

    private void parseIPv4(String string, byte[] arrby, int n2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, "./");
        int n3 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            int n4 = n3 + 1;
            arrby[n3 + n2] = (byte)Integer.parseInt((String)stringTokenizer.nextToken());
            n3 = n4;
        }
    }

    private void parseIPv4Mask(String string, byte[] arrby, int n2) {
        int n3 = Integer.parseInt((String)string);
        for (int i2 = 0; i2 != n3; ++i2) {
            int n4 = n2 + i2 / 8;
            arrby[n4] = (byte)(arrby[n4] | 1 << 7 - i2 % 8);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int[] parseIPv6(String var1_1) {
        var2_2 = new StringTokenizer(var1_1, ":", true);
        var3_3 = new int[8];
        if (var1_1.charAt(0) == ':' && var1_1.charAt(1) == ':') {
            var2_2.nextToken();
        }
        var4_4 = -1;
        var5_5 = 0;
        do {
            if (!var2_2.hasMoreTokens()) ** GOTO lbl31
            var7_6 = var2_2.nextToken();
            if (var7_6.equals((Object)":")) {
                var12_10 = var5_5 + 1;
                var3_3[var5_5] = 0;
                var13_11 = var5_5;
                var5_5 = var12_10;
                var4_4 = var13_11;
                continue;
            }
            if (var7_6.indexOf(46) < 0) {
                var10_9 = var5_5 + 1;
                var3_3[var5_5] = Integer.parseInt((String)var7_6, (int)16);
                if (var2_2.hasMoreTokens()) {
                    var2_2.nextToken();
                    var5_5 = var10_9;
                    continue;
                }
            } else {
                var8_7 = new StringTokenizer(var7_6, ".");
                var9_8 = var5_5 + 1;
                var3_3[var5_5] = Integer.parseInt((String)var8_7.nextToken()) << 8 | Integer.parseInt((String)var8_7.nextToken());
                var5_5 = var9_8 + 1;
                var3_3[var9_8] = Integer.parseInt((String)var8_7.nextToken()) << 8 | Integer.parseInt((String)var8_7.nextToken());
                continue;
lbl31: // 1 sources:
                if (var5_5 == var3_3.length) return var3_3;
                System.arraycopy((Object)var3_3, (int)var4_4, (Object)var3_3, (int)(var3_3.length - (var5_5 - var4_4)), (int)(var5_5 - var4_4));
                var6_12 = var4_4;
                while (var6_12 != var3_3.length - (var5_5 - var4_4)) {
                    var3_3[var6_12] = 0;
                    ++var6_12;
                }
                return var3_3;
            }
            var5_5 = var10_9;
        } while (true);
    }

    private int[] parseMask(String string) {
        int[] arrn = new int[8];
        int n2 = Integer.parseInt((String)string);
        for (int i2 = 0; i2 != n2; ++i2) {
            int n3 = i2 / 16;
            arrn[n3] = arrn[n3] | 1 << 15 - i2 % 16;
        }
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] toGeneralNameEncoding(String string) {
        if (IPAddress.isValidIPv6WithNetmask((String)string) || IPAddress.isValidIPv6((String)string)) {
            int n2 = string.indexOf(47);
            if (n2 < 0) {
                byte[] arrby = new byte[16];
                this.copyInts(this.parseIPv6(string), arrby, 0);
                return arrby;
            }
            byte[] arrby = new byte[32];
            this.copyInts(this.parseIPv6(string.substring(0, n2)), arrby, 0);
            String string2 = string.substring(n2 + 1);
            int[] arrn = string2.indexOf(58) > 0 ? this.parseIPv6(string2) : this.parseMask(string2);
            this.copyInts(arrn, arrby, 16);
            return arrby;
        }
        if (!IPAddress.isValidIPv4WithNetmask((String)string) && !IPAddress.isValidIPv4((String)string)) {
            return null;
        }
        int n3 = string.indexOf(47);
        if (n3 < 0) {
            byte[] arrby = new byte[4];
            this.parseIPv4(string, arrby, 0);
            return arrby;
        }
        byte[] arrby = new byte[8];
        this.parseIPv4(string.substring(0, n3), arrby, 0);
        String string3 = string.substring(n3 + 1);
        if (string3.indexOf(46) > 0) {
            this.parseIPv4(string3, arrby, 4);
            return arrby;
        }
        this.parseIPv4Mask(string3, arrby, 4);
        return arrby;
    }

    public ASN1Encodable getName() {
        return this.obj;
    }

    public int getTagNo() {
        return this.tag;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.tag == 4) {
            return new DERTaggedObject(true, this.tag, this.obj);
        }
        return new DERTaggedObject(false, this.tag, this.obj);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tag);
        stringBuffer.append(": ");
        switch (this.tag) {
            default: {
                stringBuffer.append(this.obj.toString());
                do {
                    return stringBuffer.toString();
                    break;
                } while (true);
            }
            case 1: 
            case 2: 
            case 6: {
                stringBuffer.append(DERIA5String.getInstance(this.obj).getString());
                return stringBuffer.toString();
            }
            case 4: 
        }
        stringBuffer.append(X500Name.getInstance(this.obj).toString());
        return stringBuffer.toString();
    }
}

