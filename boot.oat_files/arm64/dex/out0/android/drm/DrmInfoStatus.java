package android.drm;

import android.net.ProxyInfo;

public class DrmInfoStatus {
    public static final int STATUS_ERROR = 2;
    public static final int STATUS_ERROR_DRM_E_BUFFERTOOSMALL = 3518;
    public static final int STATUS_ERROR_DRM_E_CLK_INVALID_DATE = 3523;
    public static final int STATUS_ERROR_DRM_E_DEVCERTEXCEEDSIZELIMIT = 3524;
    public static final int STATUS_ERROR_DRM_E_DEVCERTREADERROR = 3519;
    public static final int STATUS_ERROR_DRM_E_DEVCERT_REVOKED = 3506;
    public static final int STATUS_ERROR_DRM_E_DOMAIN_INVALID_CUSTOM_DATA = 3525;
    public static final int STATUS_ERROR_DRM_E_DOMAIN_INVALID_CUSTOM_DATA_TYPE = 3526;
    public static final int STATUS_ERROR_DRM_E_DOMAIN_NOT_FOUND = 3505;
    public static final int STATUS_ERROR_DRM_E_DOMAIN_STORE_DELETE_DATA = 3530;
    public static final int STATUS_ERROR_DRM_E_DOMAIN_STORE_GET_DATA = 3538;
    public static final int STATUS_ERROR_DRM_E_DRMNOTINIT = 3532;
    public static final int STATUS_ERROR_DRM_E_INVALIDARG = 3516;
    public static final int STATUS_ERROR_DRM_E_INVALIDDEVICECERTIFICATE = 3521;
    public static final int STATUS_ERROR_DRM_E_INVALIDDEVICECERTIFICATETEMPLATE = 3520;
    public static final int STATUS_ERROR_DRM_E_INVALIDLICENSESTORE = 3522;
    public static final int STATUS_ERROR_DRM_E_INVALID_METERCERT_SIGNATURE = 3533;
    public static final int STATUS_ERROR_DRM_E_INVALID_METERRESPONSE_SIGNATURE = 3536;
    public static final int STATUS_ERROR_DRM_E_LICENSEEXPIRED = 3504;
    public static final int STATUS_ERROR_DRM_E_LICENSENOTFOUND = 3503;
    public static final int STATUS_ERROR_DRM_E_METERING_INVALID_COMMAND = 3535;
    public static final int STATUS_ERROR_DRM_E_METERING_NOT_SUPPORTED = 3517;
    public static final int STATUS_ERROR_DRM_E_METERSTORE_DATA_NOT_FOUND = 3534;
    public static final int STATUS_ERROR_DRM_E_NOMORE = 3531;
    public static final int STATUS_ERROR_DRM_E_OUTOFMEMORY = 3537;
    public static final int STATUS_ERROR_DRM_E_SERVER_DEVICE_LIMIT_REACHED = 3513;
    public static final int STATUS_ERROR_DRM_E_SERVER_DOMAIN_REQUIRED = 3510;
    public static final int STATUS_ERROR_DRM_E_SERVER_INTERNAL_ERROR = 3508;
    public static final int STATUS_ERROR_DRM_E_SERVER_INVALID_MESSAGE = 3507;
    public static final int STATUS_ERROR_DRM_E_SERVER_NOT_A_MEMBER = 3511;
    public static final int STATUS_ERROR_DRM_E_SERVER_PROTOCOL_REDIRECT = 3515;
    public static final int STATUS_ERROR_DRM_E_SERVER_PROTOCOL_VERSION_MISMATCH = 3514;
    public static final int STATUS_ERROR_DRM_E_SERVER_SERVICE_SPECIFIC = 3509;
    public static final int STATUS_ERROR_DRM_E_SERVER_UNKNOWN_ACCOUNTID = 3512;
    public static final int STATUS_ERROR_DRM_E_SOAPXML_XML_FORMAT = 3528;
    public static final int STATUS_ERROR_DRM_E_XMLNOTFOUND = 3529;
    public static final int STATUS_ERROR_DRM_S_MORE_DATA = 3527;
    public static final int STATUS_OK = 1;
    public final ProcessedData data;
    public final int infoType;
    public final String mimeType;
    public final int statusCode;

    public DrmInfoStatus(int statusCode, int infoType, ProcessedData data, String mimeType) {
        if (!DrmInfoRequest.isValidType(infoType)) {
            throw new IllegalArgumentException("infoType: " + infoType);
        } else if (!isValidStatusCode(statusCode)) {
            throw new IllegalArgumentException("Unsupported status code: " + statusCode);
        } else if (mimeType == null || mimeType == ProxyInfo.LOCAL_EXCL_LIST) {
            throw new IllegalArgumentException("mimeType is null or an empty string");
        } else {
            this.statusCode = statusCode;
            this.infoType = infoType;
            this.data = data;
            this.mimeType = mimeType;
        }
    }

    private boolean isValidStatusCode(int statusCode) {
        if (statusCode == 1 || statusCode == 2) {
            return true;
        }
        return statusCode >= 3503 && statusCode <= 3538;
    }
}
