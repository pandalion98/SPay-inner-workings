package org.bouncycastle.crypto.tls;

import com.google.android.gms.location.places.Place;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.agreement.jpake.JPAKEParticipant;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;

public class AlertDescription {
    public static final short access_denied = (short) 49;
    public static final short bad_certificate = (short) 42;
    public static final short bad_certificate_hash_value = (short) 114;
    public static final short bad_certificate_status_response = (short) 113;
    public static final short bad_record_mac = (short) 20;
    public static final short certificate_expired = (short) 45;
    public static final short certificate_revoked = (short) 44;
    public static final short certificate_unknown = (short) 46;
    public static final short certificate_unobtainable = (short) 111;
    public static final short close_notify = (short) 0;
    public static final short decode_error = (short) 50;
    public static final short decompression_failure = (short) 30;
    public static final short decrypt_error = (short) 51;
    public static final short decryption_failed = (short) 21;
    public static final short export_restriction = (short) 60;
    public static final short handshake_failure = (short) 40;
    public static final short illegal_parameter = (short) 47;
    public static final short inappropriate_fallback = (short) 86;
    public static final short insufficient_security = (short) 71;
    public static final short internal_error = (short) 80;
    public static final short no_certificate = (short) 41;
    public static final short no_renegotiation = (short) 100;
    public static final short protocol_version = (short) 70;
    public static final short record_overflow = (short) 22;
    public static final short unexpected_message = (short) 10;
    public static final short unknown_ca = (short) 48;
    public static final short unknown_psk_identity = (short) 115;
    public static final short unrecognized_name = (short) 112;
    public static final short unsupported_certificate = (short) 43;
    public static final short unsupported_extension = (short) 110;
    public static final short user_canceled = (short) 90;

    public static String getName(short s) {
        switch (s) {
            case ECCurve.COORD_AFFINE /*0*/:
                return "close_notify";
            case NamedCurve.sect283r1 /*10*/:
                return "unexpected_message";
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                return "bad_record_mac";
            case NamedCurve.secp224r1 /*21*/:
                return "decryption_failed";
            case NamedCurve.secp256k1 /*22*/:
                return "record_overflow";
            case JPAKEParticipant.STATE_ROUND_2_CREATED /*30*/:
                return "decompression_failure";
            case JPAKEParticipant.STATE_ROUND_2_VALIDATED /*40*/:
                return "handshake_failure";
            case EACTags.INTERCHANGE_PROFILE /*41*/:
                return "no_certificate";
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return "bad_certificate";
            case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                return "unsupported_certificate";
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                return "certificate_revoked";
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                return "certificate_expired";
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
                return "certificate_unknown";
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                return "illegal_parameter";
            case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
                return "unknown_ca";
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_CBC_SHA /*49*/:
                return "access_denied";
            case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
                return "decode_error";
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
                return "decrypt_error";
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
                return "export_restriction";
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA /*70*/:
                return "protocol_version";
            case EACTags.CARD_CAPABILITIES /*71*/:
                return "insufficient_security";
            case EACTags.APPLICATION_LABEL /*80*/:
                return "internal_error";
            case EACTags.TRACK1_APPLICATION /*86*/:
                return "inappropriate_fallback";
            case EACTags.PRIMARY_ACCOUNT_NUMBER /*90*/:
                return "user_canceled";
            case EncryptionAlgorithm.ESTREAM_SALSA20 /*100*/:
                return "no_renegotiation";
            case EACTags.APPLICATION_RELATED_DATA /*110*/:
                return "unsupported_extension";
            case EACTags.FCI_TEMPLATE /*111*/:
                return "certificate_unobtainable";
            case (short) 112:
                return "unrecognized_name";
            case (short) 113:
                return "bad_certificate_status_response";
            case (short) 114:
                return "bad_certificate_hash_value";
            case EACTags.DISCRETIONARY_DATA_OBJECTS /*115*/:
                return "unknown_psk_identity";
            default:
                return PaymentFramework.CARD_TYPE_UNKNOWN;
        }
    }

    public static String getText(short s) {
        return getName(s) + "(" + s + ")";
    }
}
