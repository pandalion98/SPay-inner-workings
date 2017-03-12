package android.drm;

import android.net.ProxyInfo;
import java.util.HashMap;
import java.util.Iterator;

public class DrmInfoRequest {
    public static final String ACCOUNT_ID = "account_id";
    public static final String ANALOG_VIDEO = "analog_video";
    public static final String APP_ID = "app_Id";
    public static final String COMP_DIGI_AUDIO = "comp_digi_audio";
    public static final String COMP_DIGI_VIDEO = "comp_digi_video";
    public static final String CUSTOM_DATA = "custom_data";
    public static final String DAY = "day";
    public static final String DOMAIN_CONTROLLER = "domain_controller";
    public static final String DRMPERMISSIONTYPE_ID = "drmpermission_type";
    public static final String DRM_BUFFER = "drm_buffer";
    public static final String DRM_BUFFER_SIZE = "drm_buffer_size";
    public static final String DRM_EOF = "drm_eof";
    public static final String DRM_HEADER = "drmHeader";
    public static final String DRM_INFO_NEVER_REGISTERED = "Drm Never Registered";
    public static final String DRM_INFO_NOT_AUTHORIZED = "Not Authorized";
    public static final String DRM_INFO_NOT_REGISTERED = "Not Registered";
    public static final String DRM_INFO_REGISTERED = "Registered";
    public static final String DRM_INFO_REGISTRATION_STATUS = "Registration Status";
    public static final String DRM_INFO_RENTAL_EXPIRED = "Drm  Rental Expired";
    public static final int DRM_INIT_JOINDOMAIN = 24;
    public static final int DRM_INIT_LEAVEDOMAIN = 25;
    public static final int DRM_INIT_LICENSEACQUISITION = 26;
    public static final int DRM_INIT_METERING = 27;
    public static final int DRM_INIT_UNKNOWN = 23;
    public static final String DRM_OFFSET = "drm_offset";
    public static final String DRM_PATH = "drm_path";
    public static final String FAIL = "fail";
    public static final String HOUR = "hour";
    public static final String IMEI = "imei";
    public static final String INITIATOR_URL = "Initiator_Url";
    public static final String MAX_PACKETS = "max_packets";
    public static final String MEDIA_HUB = "media_hub";
    public static final String METERING_ID = "metering_id";
    public static final String MINUTE = "minute";
    public static final String MONTH = "month";
    public static final String MV_ID = "mv_id";
    public static final String ORDER_ID = "orderId";
    public static final String PREVIEWOPTION_ID = "preview_option";
    public static final String REMOVE_RIGHTS = "remove_rights";
    public static final String REVISION = "revision";
    public static final String SECOND = "second";
    public static final String SERVICE_ID = "service_id";
    public static final String STATUS = "status";
    public static final String SUBSCRIPTION_ID = "subscription_id";
    public static final String SUCCESS = "success";
    public static final String SVR_ID = "svr_id";
    public static final int TYPE_CONSUME_RIGHTS = 28;
    public static final int TYPE_CONVERT_DRM_FILE = 7;
    public static final int TYPE_DELETE_DRM_FILE = 21;
    public static final int TYPE_ENY_DECRYPT = 51;
    public static final int TYPE_GET_DCFHEADER_INFO = 19;
    public static final int TYPE_GET_DECRYPT_IMAGE = 10;
    public static final int TYPE_GET_DRMFILE_INFO = 14;
    public static final int TYPE_GET_FILE_PATH = 9;
    public static final int TYPE_GET_OPTION_MENU = 16;
    public static final int TYPE_GET_PERMISSION_TYPE = 15;
    public static final int TYPE_HANDLE_TVOUT = 11;
    public static final int TYPE_HANDLE_TVOUT_UNPLUGGED = 12;
    public static final int TYPE_INITIATOR_PROCESS_INFO = 22;
    public static final int TYPE_IS_CONVERTED_FL = 17;
    public static final int TYPE_JOIN_DOMAIN = 32;
    public static final int TYPE_LEAVE_DOMAIN = 33;
    public static final int TYPE_OUTPUT_PROTECTION_LEVEL = 29;
    public static final int TYPE_QUERY_DOMAIN = 31;
    public static final int TYPE_REGISTER_DRM_FILE = 8;
    public static final int TYPE_REGISTRATION_INFO = 1;
    public static final int TYPE_REGISTRATION_STATUS = 30;
    public static final int TYPE_RIGHTS_ACQUISITION_INFO = 3;
    public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 4;
    public static final int TYPE_RINGTONE_UPDATE_ALARM = 20;
    public static final int TYPE_SEND_METER_DATA = 34;
    public static final int TYPE_SET_AS_RINGTONE = 18;
    public static final int TYPE_SET_SECURE_CLOCK = 35;
    public static final int TYPE_UNREGISTRATION_INFO = 2;
    public static final int TYPE_UPDATE_SECURE_CLOCK = 36;
    public static final String UNCOMP_DIGI_AUDIO = "uncomp_digi_audio";
    public static final String UNCOMP_DIGI_VIDEO = "uncomp_digi_video";
    public static final String USER_GUID = "user_guid";
    public static final String YEAR = "year";
    private final int mInfoType;
    private final String mMimeType;
    private final HashMap<String, Object> mRequestInformation = new HashMap();

    public DrmInfoRequest(int infoType, String mimeType) {
        this.mInfoType = infoType;
        this.mMimeType = mimeType;
        if (!isValid()) {
            throw new IllegalArgumentException("infoType: " + infoType + "," + "mimeType: " + mimeType);
        }
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public int getInfoType() {
        return this.mInfoType;
    }

    public void put(String key, Object value) {
        this.mRequestInformation.put(key, value);
    }

    public Object get(String key) {
        return this.mRequestInformation.get(key);
    }

    public Iterator<String> keyIterator() {
        return this.mRequestInformation.keySet().iterator();
    }

    public Iterator<Object> iterator() {
        return this.mRequestInformation.values().iterator();
    }

    boolean isValid() {
        return (this.mMimeType == null || this.mMimeType.equals(ProxyInfo.LOCAL_EXCL_LIST) || this.mRequestInformation == null || !isValidType(this.mInfoType)) ? false : true;
    }

    static boolean isValidType(int infoType) {
        switch (infoType) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 51:
                return true;
            default:
                return false;
        }
    }
}
