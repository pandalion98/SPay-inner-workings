package org.bouncycastle.asn1.util;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
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

    static void _dumpAsString(String str, boolean z, ASN1Primitive aSN1Primitive, StringBuffer stringBuffer) {
        String property = System.getProperty("line.separator");
        Enumeration objects;
        String str2;
        Object nextElement;
        if (aSN1Primitive instanceof ASN1Sequence) {
            objects = ((ASN1Sequence) aSN1Primitive).getObjects();
            str2 = str + TAB;
            stringBuffer.append(str);
            if (aSN1Primitive instanceof BERSequence) {
                stringBuffer.append("BER Sequence");
            } else if (aSN1Primitive instanceof DERSequence) {
                stringBuffer.append("DER Sequence");
            } else {
                stringBuffer.append("Sequence");
            }
            stringBuffer.append(property);
            while (objects.hasMoreElements()) {
                nextElement = objects.nextElement();
                if (nextElement == null || nextElement.equals(DERNull.INSTANCE)) {
                    stringBuffer.append(str2);
                    stringBuffer.append("NULL");
                    stringBuffer.append(property);
                } else if (nextElement instanceof ASN1Primitive) {
                    _dumpAsString(str2, z, (ASN1Primitive) nextElement, stringBuffer);
                } else {
                    _dumpAsString(str2, z, ((ASN1Encodable) nextElement).toASN1Primitive(), stringBuffer);
                }
            }
        } else if (aSN1Primitive instanceof ASN1TaggedObject) {
            r0 = str + TAB;
            stringBuffer.append(str);
            if (aSN1Primitive instanceof BERTaggedObject) {
                stringBuffer.append("BER Tagged [");
            } else {
                stringBuffer.append("Tagged [");
            }
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Primitive;
            stringBuffer.append(Integer.toString(aSN1TaggedObject.getTagNo()));
            stringBuffer.append(']');
            if (!aSN1TaggedObject.isExplicit()) {
                stringBuffer.append(" IMPLICIT ");
            }
            stringBuffer.append(property);
            if (aSN1TaggedObject.isEmpty()) {
                stringBuffer.append(r0);
                stringBuffer.append("EMPTY");
                stringBuffer.append(property);
                return;
            }
            _dumpAsString(r0, z, aSN1TaggedObject.getObject(), stringBuffer);
        } else if (aSN1Primitive instanceof ASN1Set) {
            objects = ((ASN1Set) aSN1Primitive).getObjects();
            str2 = str + TAB;
            stringBuffer.append(str);
            if (aSN1Primitive instanceof BERSet) {
                stringBuffer.append("BER Set");
            } else {
                stringBuffer.append("DER Set");
            }
            stringBuffer.append(property);
            while (objects.hasMoreElements()) {
                nextElement = objects.nextElement();
                if (nextElement == null) {
                    stringBuffer.append(str2);
                    stringBuffer.append("NULL");
                    stringBuffer.append(property);
                } else if (nextElement instanceof ASN1Primitive) {
                    _dumpAsString(str2, z, (ASN1Primitive) nextElement, stringBuffer);
                } else {
                    _dumpAsString(str2, z, ((ASN1Encodable) nextElement).toASN1Primitive(), stringBuffer);
                }
            }
        } else if (aSN1Primitive instanceof ASN1OctetString) {
            ASN1OctetString aSN1OctetString = (ASN1OctetString) aSN1Primitive;
            if (aSN1Primitive instanceof BEROctetString) {
                stringBuffer.append(str + "BER Constructed Octet String" + "[" + aSN1OctetString.getOctets().length + "] ");
            } else {
                stringBuffer.append(str + "DER Octet String" + "[" + aSN1OctetString.getOctets().length + "] ");
            }
            if (z) {
                stringBuffer.append(dumpBinaryDataAsString(str, aSN1OctetString.getOctets()));
            } else {
                stringBuffer.append(property);
            }
        } else if (aSN1Primitive instanceof ASN1ObjectIdentifier) {
            stringBuffer.append(str + "ObjectIdentifier(" + ((ASN1ObjectIdentifier) aSN1Primitive).getId() + ")" + property);
        } else if (aSN1Primitive instanceof ASN1Boolean) {
            stringBuffer.append(str + "Boolean(" + ((ASN1Boolean) aSN1Primitive).isTrue() + ")" + property);
        } else if (aSN1Primitive instanceof ASN1Integer) {
            stringBuffer.append(str + "Integer(" + ((ASN1Integer) aSN1Primitive).getValue() + ")" + property);
        } else if (aSN1Primitive instanceof DERBitString) {
            DERBitString dERBitString = (DERBitString) aSN1Primitive;
            stringBuffer.append(str + "DER Bit String" + "[" + dERBitString.getBytes().length + ", " + dERBitString.getPadBits() + "] ");
            if (z) {
                stringBuffer.append(dumpBinaryDataAsString(str, dERBitString.getBytes()));
            } else {
                stringBuffer.append(property);
            }
        } else if (aSN1Primitive instanceof DERIA5String) {
            stringBuffer.append(str + "IA5String(" + ((DERIA5String) aSN1Primitive).getString() + ") " + property);
        } else if (aSN1Primitive instanceof DERUTF8String) {
            stringBuffer.append(str + "UTF8String(" + ((DERUTF8String) aSN1Primitive).getString() + ") " + property);
        } else if (aSN1Primitive instanceof DERPrintableString) {
            stringBuffer.append(str + "PrintableString(" + ((DERPrintableString) aSN1Primitive).getString() + ") " + property);
        } else if (aSN1Primitive instanceof DERVisibleString) {
            stringBuffer.append(str + "VisibleString(" + ((DERVisibleString) aSN1Primitive).getString() + ") " + property);
        } else if (aSN1Primitive instanceof DERBMPString) {
            stringBuffer.append(str + "BMPString(" + ((DERBMPString) aSN1Primitive).getString() + ") " + property);
        } else if (aSN1Primitive instanceof DERT61String) {
            stringBuffer.append(str + "T61String(" + ((DERT61String) aSN1Primitive).getString() + ") " + property);
        } else if (aSN1Primitive instanceof ASN1UTCTime) {
            stringBuffer.append(str + "UTCTime(" + ((ASN1UTCTime) aSN1Primitive).getTime() + ") " + property);
        } else if (aSN1Primitive instanceof ASN1GeneralizedTime) {
            stringBuffer.append(str + "GeneralizedTime(" + ((ASN1GeneralizedTime) aSN1Primitive).getTime() + ") " + property);
        } else if (aSN1Primitive instanceof BERApplicationSpecific) {
            stringBuffer.append(outputApplicationSpecific(ASN1Encoding.BER, str, z, aSN1Primitive, property));
        } else if (aSN1Primitive instanceof DERApplicationSpecific) {
            stringBuffer.append(outputApplicationSpecific(ASN1Encoding.DER, str, z, aSN1Primitive, property));
        } else if (aSN1Primitive instanceof ASN1Enumerated) {
            stringBuffer.append(str + "DER Enumerated(" + ((ASN1Enumerated) aSN1Primitive).getValue() + ")" + property);
        } else if (aSN1Primitive instanceof DERExternal) {
            DERExternal dERExternal = (DERExternal) aSN1Primitive;
            stringBuffer.append(str + "External " + property);
            r0 = str + TAB;
            if (dERExternal.getDirectReference() != null) {
                stringBuffer.append(r0 + "Direct Reference: " + dERExternal.getDirectReference().getId() + property);
            }
            if (dERExternal.getIndirectReference() != null) {
                stringBuffer.append(r0 + "Indirect Reference: " + dERExternal.getIndirectReference().toString() + property);
            }
            if (dERExternal.getDataValueDescriptor() != null) {
                _dumpAsString(r0, z, dERExternal.getDataValueDescriptor(), stringBuffer);
            }
            stringBuffer.append(r0 + "Encoding: " + dERExternal.getEncoding() + property);
            _dumpAsString(r0, z, dERExternal.getExternalContent(), stringBuffer);
        } else {
            stringBuffer.append(str + aSN1Primitive.toString() + property);
        }
    }

    private static String calculateAscString(byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        int i3 = i;
        while (i3 != i + i2) {
            if (bArr[i3] >= SAMPLE_SIZE && bArr[i3] <= 126) {
                stringBuffer.append((char) bArr[i3]);
            }
            i3++;
        }
        return stringBuffer.toString();
    }

    public static String dumpAsString(Object obj) {
        return dumpAsString(obj, false);
    }

    public static String dumpAsString(Object obj, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        if (obj instanceof ASN1Primitive) {
            _dumpAsString(BuildConfig.FLAVOR, z, (ASN1Primitive) obj, stringBuffer);
        } else if (!(obj instanceof ASN1Encodable)) {
            return "unknown object type " + obj.toString();
        } else {
            _dumpAsString(BuildConfig.FLAVOR, z, ((ASN1Encodable) obj).toASN1Primitive(), stringBuffer);
        }
        return stringBuffer.toString();
    }

    private static String dumpBinaryDataAsString(String str, byte[] bArr) {
        String property = System.getProperty("line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        String str2 = str + TAB;
        stringBuffer.append(property);
        for (int i = 0; i < bArr.length; i += SAMPLE_SIZE) {
            if (bArr.length - i > SAMPLE_SIZE) {
                stringBuffer.append(str2);
                stringBuffer.append(new String(Hex.encode(bArr, i, SAMPLE_SIZE)));
                stringBuffer.append(TAB);
                stringBuffer.append(calculateAscString(bArr, i, SAMPLE_SIZE));
                stringBuffer.append(property);
            } else {
                stringBuffer.append(str2);
                stringBuffer.append(new String(Hex.encode(bArr, i, bArr.length - i)));
                for (int length = bArr.length - i; length != SAMPLE_SIZE; length++) {
                    stringBuffer.append("  ");
                }
                stringBuffer.append(TAB);
                stringBuffer.append(calculateAscString(bArr, i, bArr.length - i));
                stringBuffer.append(property);
            }
        }
        return stringBuffer.toString();
    }

    private static String outputApplicationSpecific(String str, String str2, boolean z, ASN1Primitive aSN1Primitive, String str3) {
        DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific) aSN1Primitive;
        StringBuffer stringBuffer = new StringBuffer();
        if (!dERApplicationSpecific.isConstructed()) {
            return str2 + str + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "] (" + new String(Hex.encode(dERApplicationSpecific.getContents())) + ")" + str3;
        }
        try {
            ASN1Sequence instance = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16));
            stringBuffer.append(str2 + str + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "]" + str3);
            Enumeration objects = instance.getObjects();
            while (objects.hasMoreElements()) {
                _dumpAsString(str2 + TAB, z, (ASN1Primitive) objects.nextElement(), stringBuffer);
            }
        } catch (IOException e) {
            stringBuffer.append(e);
        }
        return stringBuffer.toString();
    }
}
