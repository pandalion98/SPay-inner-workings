package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import com.mastercard.mobile_api.utils.apdu.Apdu;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PutTemplateApdu extends Apdu {
    public static final byte CANCEL_OVERRIDE = (byte) 4;
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte DEVICE_NOT_SWITCHED_ON = (byte) 2;
    public static final byte DEVICE_SWITCHED_ON_NO_OVERRIDE = (byte) 1;
    public static final byte DIRECTORY_TEMPLATE_TAG = (byte) 97;
    public static final byte FCI_ISSUER_DATA_HIGHER_BYTE_TAG = (byte) -65;
    public static final byte FCI_ISSUER_DATA_LOWER_BYTE_TAG = (byte) 12;
    public static final byte FCI_PROPRIETARY_TEMPLATE_TAG = (byte) -91;
    public static final byte HIDE = (byte) 6;
    public static final byte INS = (byte) -46;
    public static final short KERNEL_IDENTIFIER_TAG = (short) -24790;
    public static final byte MANDATORY_DATA_ONLY = (byte) 5;
    public static final byte OVERRIDE_UNTIL_CANCEL = (byte) 3;
    public static final short PPSE_VERSION_TAG = (short) -24824;

    public PutTemplateApdu(byte b, ByteArray byteArray) {
        setCLA(CLA);
        setINS(INS);
        setP1(b);
        setP2((byte) 0);
        ByteArray defaultByteArrayImpl = new DefaultByteArrayImpl();
        switch (b) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case F2m.PPB /*3*/:
                setDataField(byteArray);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                defaultByteArrayImpl.appendByte((byte) 0);
                appendData(defaultByteArrayImpl, true);
            default:
        }
    }
}
