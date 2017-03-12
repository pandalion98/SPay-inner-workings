package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import com.google.android.gms.location.places.Place;
import com.samsung.android.spayfw.cncc.CNCCTAException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPConstants;
import com.samsung.android.spaytui.SPayTUIException;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.agreement.jpake.JPAKEParticipant;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class AmexTAException extends Exception {
    private int errorCode;

    public AmexTAException(int i) {
        super(toString(i));
        this.errorCode = SPayTUIException.ERR_UNKNOWN;
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String toString() {
        return toString(this.errorCode);
    }

    public static String toString(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return "No Error";
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "Invalid Input Param";
            case NamedCurve.secp160r2 /*17*/:
                return "HMAC calculation failed/ HMAC did not match/Decryption of token data failed";
            case NamedCurve.secp192k1 /*18*/:
                return "Token Data cannot be parsed";
            case NamedCurve.secp192r1 /*19*/:
                return "Decryption Session key or KCV calculation failed";
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                return "KCV mismatch failure";
            case NamedCurve.secp224r1 /*21*/:
                return "Failed creating blobs";
            case NamedCurve.secp256k1 /*22*/:
                return "Failure encrypting blobs";
            case NamedCurve.secp256r1 /*23*/:
                return "Unable to compute response data signature";
            case NamedCurve.secp384r1 /*24*/:
                return "Encryption of request data failed";
            case NamedCurve.secp521r1 /*25*/:
                return "Signature cal of request data failed";
            case NamedCurve.brainpoolP256r1 /*26*/:
                return "Decryption of response data failed";
            case NamedCurve.brainpoolP512r1 /*28*/:
                return "Parsing failed";
            case DSRPConstants.APP_CRYPTO_GENERATION_CDOL_PART_LAST_BYTE /*29*/:
                return "ATC cannot be returned";
            case JPAKEParticipant.STATE_ROUND_2_CREATED /*30*/:
                return "TID cal failed";
            case Place.TYPE_ELECTRICIAN /*31*/:
                return "Blobs cannot be constructed";
            case X509KeyUsage.keyEncipherment /*32*/:
                return "Parsing failed";
            case EACTags.CARDHOLDER_CERTIFICATE /*33*/:
                return "ATC cannot be returned";
            case Place.TYPE_ESTABLISHMENT /*34*/:
                return "TID cal failed";
            case ExtensionType.session_ticket /*35*/:
                return "Failed generating crypto";
            case EACTags.APPLICATION_EXPIRATION_DATE /*36*/:
                return "Decryption failed";
            case EACTags.APPLICATION_EFFECTIVE_DATE /*37*/:
                return "Parsing failed";
            case Place.TYPE_FOOD /*38*/:
                return "Meta data blob cannot be constructed";
            case Place.TYPE_FUNERAL_HOME /*39*/:
                return "Decryption failed";
            case JPAKEParticipant.STATE_ROUND_2_VALIDATED /*40*/:
                return "Parsing failed";
            case EACTags.INTERCHANGE_PROFILE /*41*/:
                return "Meta data blob cannot be constructed";
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return "Decryption failed";
            case Place.TYPE_GROCERY_OR_SUPERMARKET /*43*/:
                return "Parsing failed";
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
                return "Meta data blob cannot be constructed";
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                return "User Authentication Failure";
            case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
                return "MST Turn On Failure";
            case CNCCTAException.ERR_TZ_COM_ERR /*983040*/:
                return "TZ Communication Error";
            case CNCCTAException.CNCC_UNKNOWN_CMD /*983041*/:
                return "No Such TZ Command Supported";
            case CNCCTAException.CNCC_INVALID_INPUT_BUFFER /*983042*/:
                return "Invalid Input Buffer";
            default:
                return "Unknown Error";
        }
    }
}
