package org.bouncycastle.asn1;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.android.gms.location.places.Place;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.agreement.jpake.JPAKEParticipant;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class DERPrintableString extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public DERPrintableString(String str) {
        this(str, false);
    }

    public DERPrintableString(String str, boolean z) {
        if (!z || isPrintableString(str)) {
            this.string = Strings.toByteArray(str);
            return;
        }
        throw new IllegalArgumentException("string contains illegal characters");
    }

    DERPrintableString(byte[] bArr) {
        this.string = bArr;
    }

    public static DERPrintableString getInstance(Object obj) {
        if (obj == null || (obj instanceof DERPrintableString)) {
            return (DERPrintableString) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return (DERPrintableString) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (Exception e) {
                throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERPrintableString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        return (z || (object instanceof DERPrintableString)) ? getInstance(object) : new DERPrintableString(ASN1OctetString.getInstance(object).getOctets());
    }

    public static boolean isPrintableString(String str) {
        for (int length = str.length() - 1; length >= 0; length--) {
            char charAt = str.charAt(length);
            if (charAt > '\u007f') {
                return false;
            }
            if (('a' > charAt || charAt > 'z') && (('A' > charAt || charAt > Matrix.MATRIX_TYPE_ZERO) && (LLVARUtil.EMPTY_STRING > charAt || charAt > '9'))) {
                switch (charAt) {
                    case X509KeyUsage.keyEncipherment /*32*/:
                    case Place.TYPE_FUNERAL_HOME /*39*/:
                    case JPAKEParticipant.STATE_ROUND_2_VALIDATED /*40*/:
                    case EACTags.INTERCHANGE_PROFILE /*41*/:
                    case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                    case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                    case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                    case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
                    case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    case CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA /*58*/:
                    case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
                    case SkeinParameterSpec.PARAM_TYPE_OUTPUT /*63*/:
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERPrintableString)) {
            return false;
        }
        return Arrays.areEqual(this.string, ((DERPrintableString) aSN1Primitive).string);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(19, this.string);
    }

    int encodedLength() {
        return (StreamUtil.calculateBodyLength(this.string.length) + 1) + this.string.length;
    }

    public byte[] getOctets() {
        return Arrays.clone(this.string);
    }

    public String getString() {
        return Strings.fromByteArray(this.string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return getString();
    }
}
