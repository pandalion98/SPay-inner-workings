/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.util.Enumeration
 *  org.bouncycastle.util.encoders.Hex
 */
package org.bouncycastle.asn1.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.BERApplicationSpecific;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERExternal;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERVisibleString;
import org.bouncycastle.util.encoders.Hex;

public class ASN1Dump {
    private static final int SAMPLE_SIZE = 32;
    private static final String TAB = "    ";

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static void _dumpAsString(String var0, boolean var1_1, ASN1Primitive var2_2, StringBuffer var3_3) {
        block43 : {
            var4_4 = System.getProperty((String)"line.separator");
            if (!(var2_2 instanceof ASN1Sequence)) break block43;
            var58_5 = ((ASN1Sequence)var2_2).getObjects();
            var59_6 = var0 + "    ";
            var3_3.append(var0);
            if (var2_2 instanceof BERSequence) {
                var3_3.append("BER Sequence");
            } else if (var2_2 instanceof DERSequence) {
                var3_3.append("DER Sequence");
            } else {
                var3_3.append("Sequence");
            }
            ** GOTO lbl121
        }
        if (var2_2 instanceof ASN1TaggedObject) {
            var46_8 = var0 + "    ";
            var3_3.append(var0);
            if (var2_2 instanceof BERTaggedObject) {
                var3_3.append("BER Tagged [");
            } else {
                var3_3.append("Tagged [");
            }
            var49_9 = (ASN1TaggedObject)var2_2;
            var3_3.append(Integer.toString((int)var49_9.getTagNo()));
            var3_3.append(']');
            if (!var49_9.isExplicit()) {
                var3_3.append(" IMPLICIT ");
            }
            var3_3.append(var4_4);
            if (var49_9.isEmpty()) {
                var3_3.append(var46_8);
                var3_3.append("EMPTY");
                var3_3.append(var4_4);
                return;
            }
            ASN1Dump._dumpAsString(var46_8, var1_1, var49_9.getObject(), var3_3);
            return;
        }
        if (var2_2 instanceof ASN1Set) {
            var36_10 = ((ASN1Set)var2_2).getObjects();
            var37_11 = var0 + "    ";
            var3_3.append(var0);
            if (var2_2 instanceof BERSet) {
                var3_3.append("BER Set");
            } else {
                var3_3.append("DER Set");
            }
        } else {
            if (var2_2 instanceof ASN1OctetString) {
                var31_13 = (ASN1OctetString)var2_2;
                if (var2_2 instanceof BEROctetString) {
                    var3_3.append(var0 + "BER Constructed Octet String" + "[" + var31_13.getOctets().length + "] ");
                } else {
                    var3_3.append(var0 + "DER Octet String" + "[" + var31_13.getOctets().length + "] ");
                }
                if (var1_1) {
                    var3_3.append(ASN1Dump.dumpBinaryDataAsString(var0, var31_13.getOctets()));
                    return;
                }
                var3_3.append(var4_4);
                return;
            }
            if (var2_2 instanceof ASN1ObjectIdentifier) {
                var3_3.append(var0 + "ObjectIdentifier(" + ((ASN1ObjectIdentifier)var2_2).getId() + ")" + var4_4);
                return;
            }
            if (var2_2 instanceof ASN1Boolean) {
                var3_3.append(var0 + "Boolean(" + ((ASN1Boolean)var2_2).isTrue() + ")" + var4_4);
                return;
            }
            if (var2_2 instanceof ASN1Integer) {
                var3_3.append(var0 + "Integer(" + (Object)((ASN1Integer)var2_2).getValue() + ")" + var4_4);
                return;
            }
            if (var2_2 instanceof DERBitString) {
                var24_14 = (DERBitString)var2_2;
                var3_3.append(var0 + "DER Bit String" + "[" + var24_14.getBytes().length + ", " + var24_14.getPadBits() + "] ");
                if (var1_1) {
                    var3_3.append(ASN1Dump.dumpBinaryDataAsString(var0, var24_14.getBytes()));
                    return;
                }
                var3_3.append(var4_4);
                return;
            }
            if (var2_2 instanceof DERIA5String) {
                var3_3.append(var0 + "IA5String(" + ((DERIA5String)var2_2).getString() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof DERUTF8String) {
                var3_3.append(var0 + "UTF8String(" + ((DERUTF8String)var2_2).getString() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof DERPrintableString) {
                var3_3.append(var0 + "PrintableString(" + ((DERPrintableString)var2_2).getString() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof DERVisibleString) {
                var3_3.append(var0 + "VisibleString(" + ((DERVisibleString)var2_2).getString() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof DERBMPString) {
                var3_3.append(var0 + "BMPString(" + ((DERBMPString)var2_2).getString() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof DERT61String) {
                var3_3.append(var0 + "T61String(" + ((DERT61String)var2_2).getString() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof ASN1UTCTime) {
                var3_3.append(var0 + "UTCTime(" + ((ASN1UTCTime)var2_2).getTime() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof ASN1GeneralizedTime) {
                var3_3.append(var0 + "GeneralizedTime(" + ((ASN1GeneralizedTime)var2_2).getTime() + ") " + var4_4);
                return;
            }
            if (var2_2 instanceof BERApplicationSpecific) {
                var3_3.append(ASN1Dump.outputApplicationSpecific("BER", var0, var1_1, var2_2, var4_4));
                return;
            }
            if (var2_2 instanceof DERApplicationSpecific) {
                var3_3.append(ASN1Dump.outputApplicationSpecific("DER", var0, var1_1, var2_2, var4_4));
                return;
            }
            if (var2_2 instanceof ASN1Enumerated) {
                var12_15 = (ASN1Enumerated)var2_2;
                var3_3.append(var0 + "DER Enumerated(" + (Object)var12_15.getValue() + ")" + var4_4);
                return;
            }
            if (!(var2_2 instanceof DERExternal)) {
                var3_3.append(var0 + var2_2.toString() + var4_4);
                return;
            }
            var6_16 = (DERExternal)var2_2;
            var3_3.append(var0 + "External " + var4_4);
            var8_17 = var0 + "    ";
            if (var6_16.getDirectReference() != null) {
                var3_3.append(var8_17 + "Direct Reference: " + var6_16.getDirectReference().getId() + var4_4);
            }
            if (var6_16.getIndirectReference() != null) {
                var3_3.append(var8_17 + "Indirect Reference: " + var6_16.getIndirectReference().toString() + var4_4);
            }
            if (var6_16.getDataValueDescriptor() != null) {
                ASN1Dump._dumpAsString(var8_17, var1_1, var6_16.getDataValueDescriptor(), var3_3);
            }
            var3_3.append(var8_17 + "Encoding: " + var6_16.getEncoding() + var4_4);
            ASN1Dump._dumpAsString(var8_17, var1_1, var6_16.getExternalContent(), var3_3);
            return;
lbl121: // 3 sources:
            var3_3.append(var4_4);
            while (var58_5.hasMoreElements() != false) {
                var63_7 = var58_5.nextElement();
                if (var63_7 == null || var63_7.equals((Object)DERNull.INSTANCE)) {
                    var3_3.append(var59_6);
                    var3_3.append("NULL");
                    var3_3.append(var4_4);
                    continue;
                }
                if (var63_7 instanceof ASN1Primitive) {
                    ASN1Dump._dumpAsString(var59_6, var1_1, (ASN1Primitive)var63_7, var3_3);
                    continue;
                }
                ASN1Dump._dumpAsString(var59_6, var1_1, ((ASN1Encodable)var63_7).toASN1Primitive(), var3_3);
            }
            return;
        }
        var3_3.append(var4_4);
        while (var36_10.hasMoreElements() != false) {
            var41_12 = var36_10.nextElement();
            if (var41_12 == null) {
                var3_3.append(var37_11);
                var3_3.append("NULL");
                var3_3.append(var4_4);
                continue;
            }
            if (var41_12 instanceof ASN1Primitive) {
                ASN1Dump._dumpAsString(var37_11, var1_1, (ASN1Primitive)var41_12, var3_3);
                continue;
            }
            ASN1Dump._dumpAsString(var37_11, var1_1, ((ASN1Encodable)var41_12).toASN1Primitive(), var3_3);
        }
    }

    private static String calculateAscString(byte[] arrby, int n2, int n3) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = n2; i2 != n2 + n3; ++i2) {
            if (arrby[i2] < 32 || arrby[i2] > 126) continue;
            stringBuffer.append((char)arrby[i2]);
        }
        return stringBuffer.toString();
    }

    public static String dumpAsString(Object object) {
        return ASN1Dump.dumpAsString(object, false);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String dumpAsString(Object object, boolean bl) {
        StringBuffer stringBuffer = new StringBuffer();
        if (object instanceof ASN1Primitive) {
            ASN1Dump._dumpAsString("", bl, (ASN1Primitive)object, stringBuffer);
            do {
                return stringBuffer.toString();
                break;
            } while (true);
        }
        if (!(object instanceof ASN1Encodable)) return "unknown object type " + object.toString();
        ASN1Dump._dumpAsString("", bl, ((ASN1Encodable)object).toASN1Primitive(), stringBuffer);
        return stringBuffer.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    private static String dumpBinaryDataAsString(String string, byte[] arrby) {
        String string2 = System.getProperty((String)"line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        String string3 = string + TAB;
        stringBuffer.append(string2);
        int n2 = 0;
        while (n2 < arrby.length) {
            if (arrby.length - n2 > 32) {
                stringBuffer.append(string3);
                stringBuffer.append(new String(Hex.encode((byte[])arrby, (int)n2, (int)32)));
                stringBuffer.append(TAB);
                stringBuffer.append(ASN1Dump.calculateAscString(arrby, n2, 32));
                stringBuffer.append(string2);
            } else {
                stringBuffer.append(string3);
                stringBuffer.append(new String(Hex.encode((byte[])arrby, (int)n2, (int)(arrby.length - n2))));
                for (int i2 = arrby.length - n2; i2 != 32; ++i2) {
                    stringBuffer.append("  ");
                }
                stringBuffer.append(TAB);
                stringBuffer.append(ASN1Dump.calculateAscString(arrby, n2, arrby.length - n2));
                stringBuffer.append(string2);
            }
            n2 += 32;
        }
        return stringBuffer.toString();
    }

    private static String outputApplicationSpecific(String string, String string2, boolean bl, ASN1Primitive aSN1Primitive, String string3) {
        DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific)aSN1Primitive;
        StringBuffer stringBuffer = new StringBuffer();
        if (dERApplicationSpecific.isConstructed()) {
            try {
                ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16));
                stringBuffer.append(string2 + string + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "]" + string3);
                Enumeration enumeration = aSN1Sequence.getObjects();
                while (enumeration.hasMoreElements()) {
                    ASN1Dump._dumpAsString(string2 + TAB, bl, (ASN1Primitive)enumeration.nextElement(), stringBuffer);
                }
            }
            catch (IOException iOException) {
                stringBuffer.append((Object)iOException);
            }
            return stringBuffer.toString();
        }
        return string2 + string + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "] (" + new String(Hex.encode((byte[])dERApplicationSpecific.getContents())) + ")" + string3;
    }
}

