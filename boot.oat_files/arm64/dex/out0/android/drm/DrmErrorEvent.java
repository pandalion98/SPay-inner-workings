package android.drm;

import java.util.HashMap;

public class DrmErrorEvent extends DrmEvent {
    public static final int TYPE_ACQUIRE_DRM_INFO_FAILED = 2008;
    public static final int TYPE_DRM_E_BUFFERTOOSMALL = 3518;
    public static final int TYPE_DRM_E_CLK_INVALID_DATE = 3523;
    public static final int TYPE_DRM_E_DEVCERTEXCEEDSIZELIMIT = 3524;
    public static final int TYPE_DRM_E_DEVCERTREADERROR = 3519;
    public static final int TYPE_DRM_E_DEVCERT_REVOKED = 3506;
    public static final int TYPE_DRM_E_DOMAIN_INVALID_CUSTOM_DATA = 3525;
    public static final int TYPE_DRM_E_DOMAIN_INVALID_CUSTOM_DATA_TYPE = 3526;
    public static final int TYPE_DRM_E_DOMAIN_NOT_FOUND = 3505;
    public static final int TYPE_DRM_E_DOMAIN_STORE_DELETE_DATA = 3530;
    public static final int TYPE_DRM_E_DOMAIN_STORE_GET_DATA = 3538;
    public static final int TYPE_DRM_E_DRMNOTINIT = 3532;
    public static final int TYPE_DRM_E_INVALIDARG = 3516;
    public static final int TYPE_DRM_E_INVALIDDEVICECERTIFICATE = 3521;
    public static final int TYPE_DRM_E_INVALIDDEVICECERTIFICATETEMPLATE = 3520;
    public static final int TYPE_DRM_E_INVALIDLICENSESTORE = 3522;
    public static final int TYPE_DRM_E_INVALID_METERCERT_SIGNATURE = 3533;
    public static final int TYPE_DRM_E_INVALID_METERRESPONSE_SIGNATURE = 3536;
    public static final int TYPE_DRM_E_LICENSEEXPIRED = 3504;
    public static final int TYPE_DRM_E_LICENSENOTFOUND = 3503;
    public static final int TYPE_DRM_E_METERING_INVALID_COMMAND = 3535;
    public static final int TYPE_DRM_E_METERING_NOT_SUPPORTED = 3517;
    public static final int TYPE_DRM_E_METERSTORE_DATA_NOT_FOUND = 3534;
    public static final int TYPE_DRM_E_NOMORE = 3531;
    public static final int TYPE_DRM_E_OUTOFMEMORY = 3537;
    public static final int TYPE_DRM_E_SERVER_DEVICE_LIMIT_REACHED = 3513;
    public static final int TYPE_DRM_E_SERVER_DOMAIN_REQUIRED = 3510;
    public static final int TYPE_DRM_E_SERVER_INTERNAL_ERROR = 3508;
    public static final int TYPE_DRM_E_SERVER_INVALID_MESSAGE = 3507;
    public static final int TYPE_DRM_E_SERVER_NOT_A_MEMBER = 3511;
    public static final int TYPE_DRM_E_SERVER_PROTOCOL_REDIRECT = 3515;
    public static final int TYPE_DRM_E_SERVER_PROTOCOL_VERSION_MISMATCH = 3514;
    public static final int TYPE_DRM_E_SERVER_SERVICE_SPECIFIC = 3509;
    public static final int TYPE_DRM_E_SERVER_UNKNOWN_ACCOUNTID = 3512;
    public static final int TYPE_DRM_E_SOAPXML_XML_FORMAT = 3528;
    public static final int TYPE_DRM_E_XMLNOTFOUND = 3529;
    public static final int TYPE_DRM_S_MORE_DATA = 3527;
    public static final int TYPE_GENERAL = 3502;
    public static final int TYPE_NOT_SUPPORTED = 2003;
    public static final int TYPE_NO_INTERNET_CONNECTION = 2005;
    public static final int TYPE_OUT_OF_MEMORY = 2004;
    public static final int TYPE_PROCESS_DRM_INFO_FAILED = 2006;
    public static final int TYPE_REMOVE_ALL_RIGHTS_FAILED = 2007;
    public static final int TYPE_RIGHTS_NOT_INSTALLED = 2001;
    public static final int TYPE_RIGHTS_RENEWAL_NOT_ALLOWED = 2002;

    public DrmErrorEvent(int uniqueId, int type, String message) {
        super(uniqueId, type, message);
        checkTypeValidity(type);
    }

    public DrmErrorEvent(int uniqueId, int type, String message, HashMap<String, Object> attributes) {
        super(uniqueId, type, message, attributes);
        checkTypeValidity(type);
    }

    private void checkTypeValidity(int type) {
        boolean isValid = false;
        switch (type) {
            case 2001:
            case 2002:
            case 2003:
            case 2004:
            case 2005:
            case TYPE_PROCESS_DRM_INFO_FAILED /*2006*/:
            case TYPE_REMOVE_ALL_RIGHTS_FAILED /*2007*/:
            case TYPE_ACQUIRE_DRM_INFO_FAILED /*2008*/:
            case TYPE_GENERAL /*3502*/:
            case 3503:
            case 3504:
            case 3505:
            case 3506:
            case 3507:
            case 3508:
            case 3509:
            case 3510:
            case 3511:
            case 3512:
            case 3513:
            case 3514:
            case 3515:
            case 3516:
            case 3517:
            case 3518:
            case 3519:
            case 3520:
            case 3521:
            case 3522:
            case 3523:
            case 3524:
            case 3525:
            case 3526:
            case 3527:
            case 3528:
            case 3529:
            case 3530:
            case 3531:
            case 3532:
            case 3533:
            case 3534:
            case 3535:
            case 3536:
                isValid = true;
                break;
        }
        if (!isValid) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
