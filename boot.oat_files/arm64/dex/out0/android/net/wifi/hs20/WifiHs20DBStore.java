package android.net.wifi.hs20;

import android.net.Uri;

public class WifiHs20DBStore {
    private static String AUTHORITY = HOTSPOT_TABLE;
    private static String AUTHORITY_SLASH = ("content://" + AUTHORITY + "/");
    public static final String DEV_DETAIL_EAP_TABLE = "dev_details_eap";
    public static final String DEV_DETAIL_SPCERTIFICATE_TABLE = "hotspot_sp_certificate";
    public static final String DEV_DETAIL_TABLE = "dev_details";
    public static final String HOTSPOT_AAASERVER_TRUSTTROOT_TABLE = "hotspot_aaaserver_trustroot";
    public static final String HOTSPOT_BACKHAUL_BANDWIDTH_THRESHOLD_TABLE = "hostspot_backhaul_bandwidth_threshold";
    public static final String HOTSPOT_CREDENTIALS_TABLE = "hotspot_credentials";
    public static final String HOTSPOT_FQDN_TABLE = "hotspot_fqdn";
    public static final String HOTSPOT_HOMEOILIST_TABLE = "hotspot_homeoilist";
    public static final String HOTSPOT_HOMESP_NETWORK_TABLE = "hotspot_homesp_network";
    public static final String HOTSPOT_HOMESP_OTHERHOME_PARTNER_TABLE = "hotspot_otherhome_partner";
    public static final String HOTSPOT_POLICY_SUBSCRIPTION_UPDATE_TABLE = "hotspot_policy_subsription_update";
    public static final String HOTSPOT_PREFERRED_ROAMING_PARTNER_TABLE = "hotspot_preferredroaming_partnerlist";
    public static final String HOTSPOT_SPEXCLUSION_LIST_TABLE = "hotspot_spexclusion_list";
    public static final String HOTSPOT_TABLE = "hotspot";
    public static final String HOTSPOT_TUPPLE_TABLE = "hotspot_tupple";
    public static final String SERVICE_PROVICER_ICON_TABLE = "sp_icon";

    public static class AAAServerTrustRoot {
        public static final String CERT_SHA256_FINGER_PRINT_TEXT = "cert_sha256_finger_print";
        public static final String CERT_URL = "certurl";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_AAASERVER_TRUSTTROOT_TABLE);
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String SERVER_TRUSTROOT_ID = "server_trustroot_id";
        public static final String TRUSTROOT_ID = "trustroot_id";
    }

    public static class BackhaulBandwidthThreshold {
        public static final String BACKHAUL_THRESHOLD_ID = "backhaul_threshold_id";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_BACKHAUL_BANDWIDTH_THRESHOLD_TABLE);
        public static final String DLBANDWIDTH = "dlbandwidh";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String NETWORK_TYPE = "network_type";
        public static final String SERVER_BACKHAUL_THRESHOLD_ID = "server_backhaul_threshold_id";
        public static final String ULBANDWIDTH = "ulbandwidth";
    }

    public static class Credential {
        public static final String AAA_CA_CERT_HASH = "aaa_ca_cert_hash";
        public static final String ABLETOSHARE = "abletoshare";
        public static final String CERTIFICATE_TYPE = "certificate_type";
        public static final String CERT_ID = "cert_id";
        public static final String CERT_SHA256_FINGER_PRINT_TEXT = "cert_sha256_finger_print";
        public static final String CHECK_AAA_SERVER_CERT_STATUS = "check_aaa_server_cert_status";
        public static final String CLIENT_CERT_HASH = "client_cert_hash";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_CREDENTIALS_TABLE);
        public static final String CREATION_DATE = "creation_date";
        public static final String CREDENTIAL_ID = "credential_id";
        public static final String CREDENTIAL_TYPE = "credential_type";
        public static final String EAP_TYPE = "eap_type";
        public static final String EXPIRATION_DATE = "expiration_date";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String HS20_AAA_CA_CERT_HASH = "hs20_aaa_ca_cert_hash";
        public static final String IMSI = "imsi";
        public static final String INNER_EAP_TYPE = "inner_eaptype";
        public static final String INNER_METHOD = "inner_method";
        public static final String INNER_VENDOR_ID = "inner_vendor_id";
        public static final String INNER_VENDOR_TYPE = "inner_vendor_type";
        public static final String MACHINE_MANAGED = "machine_managed";
        public static final String METHOD_TYPE = "method_type";
        public static final String PASSWORD = "password";
        public static final String PRIVATE_KEY_HASH = "private_key_hash";
        public static final String REALM = "realm";
        public static final String SIM_EAPTYPE = "sim_eaptype";
        public static final String SOFT_TOKENAPP = "soft_tokenapp";
        public static final String USERNAME = "username";
        public static final String VENDOR_ID = "vendor_id";
        public static final String VENDOR_TYPE = "vendor_type";
    }

    public static class DevDetails {
        public static final String CERT_ENROLAPP_PATH = "cert_enrollapp_path";
        public static final String CLIENT_TRIGGER_REDIRECT_URI = "client_trigger_redirect_uri";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.DEV_DETAIL_TABLE);
        public static final String DEV_DETAILS_ID = "dev_details_id";
        public static final String GET_CERTIFICATE = "getcertificate";
        public static final String IMEI_MEID = "imei_meid";
        public static final String IMSI = "imsi";
        public static final String LAUNCH_BROWSER_TO_URI = "launchbrowsertouri";
        public static final String MANUFACTURING_CERTIFICATE = "manufacturing_certificate";
        public static final String NEGOTIATE_CLIENT_CERTTLS = "negotiateclientcerttls";
        public static final String WIFI_MACADDRESS = "wifi_macaddress";
    }

    public static class DevDetailsEAP {
        public static final String CLIENT_TRIGGER_REDIRECT_URI = "client_trigger_redirect_uri";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.DEV_DETAIL_EAP_TABLE);
        public static final String EAP_ID = "eap_id";
        public static final String EAP_TYPE = "eap_type";
        public static final String INNER_EAP_TYPE = "inner_eaptype";
        public static final String INNER_METHOD = "inner_method";
        public static final String INNER_VENDOR_ID = "inner_vendor_id";
        public static final String INNER_VENDOR_TYPE = "inner_vendor_type";
        public static final String VENDOR_ID = "vendor_id";
        public static final String VENDOR_TYPE = "vendor_type";
    }

    public static class DevDetailsSPCertificate {
        public static final String CERTIFICATE_ISSUER_NAME = "certificate_issuer_name";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.DEV_DETAIL_SPCERTIFICATE_TABLE);
        public static final String SP_ID = "sp_id";
    }

    public static class Fqdn {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_FQDN_TABLE);
        public static final String ENABLE_SUBSCRIPTION = "enable_subscr";
        public static final String FQDN = "fqdn";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String PPSMO_ID = "ppsmo_id";
        public static final String UPDATE_IDENTIFIER = "update_identifier";
    }

    public static class HomeOI {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_HOMEOILIST_TABLE);
        public static final String HOMEOI = "homeoi";
        public static final String HOMEOIREQUIRED = "homeoirequired";
        public static final String HOMESP_ID = "homesp_id";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String SERVER_HOMEOILIST_ID = "server_homeoilist_id";
    }

    public static class HomeSPNetwork {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_HOMESP_NETWORK_TABLE);
        public static final String HESSID = "hessid";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String NETWORK_ID = "network_id";
        public static final String SERVER_NETWORK_ID = "server_network_id";
        public static final String SSID = "ssid";
    }

    public static class HotSpot {
        public static final String CERT_SHA256_FINGER_PRINT_TEXT = "cert_sha256_finger_print";
        public static final String CERT_URL = "certurl";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_TABLE);
        public static final String CREATION_DATE = "creation_date";
        public static final String DATE_LIMIT = "date_limit";
        public static final String EXCLUSION_SSIDs = "exclusion_ssids";
        public static final String EXPIRATION_DATE = "expiration_date";
        public static final String EXT = "ext";
        public static final String FQDN = "fqdn";
        public static final String FRIENDLY_NAME = "friendly_name";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String ICON_URL = "icon_url";
        public static final String MAXIMUM_LOAD_VALUE = "maximumbss_load_value";
        public static final String ROAMING_CONSORTIUMOI = "roamingconsortiumoi";
        public static final String START_DATE = "start_date";
        public static final String SUBSCRIPTION_PRIORITY = "subscription_priority";
        public static final String TIME_LIMIT = "time_limit";
        public static final String TYPE_OF_SUBSCRIPTION = "type_of_subscription";
        public static final String UPDATE_IDENTIFIER = "update_identifier";
        public static final String URI = "uri";
        public static final String USAGE_INTERVAL = "usage_interval";
        public static final String USER_PRIORITY = "user_priority";
    }

    public static class OtherHomePartner {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_HOMESP_OTHERHOME_PARTNER_TABLE);
        public static final String FQDN = "fqdn";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String OTHERHOME_PARTNER_ID = "otherhome_partner_id";
        public static final String SERVER_OTHERHOME_PARTNER_ID = "server_otherhome_network_id";
    }

    public static class PolicySubscriptionUpdate {
        public static final String CERT_SHA256_FINGER_PRINT_TEXT = "cert_sha256_finger_print";
        public static final String CERT_URL = "certurl";
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_POLICY_SUBSCRIPTION_UPDATE_TABLE);
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String OTHER = "other";
        public static final String PASSWORD = "password";
        public static final String POLICY_SUSCRIPT_ID = "policy_subscript_id";
        public static final String RESTRICTION = "restriction";
        public static final String TYPE_OF_NODE = "type_of_node";
        public static final String UPDATE_INTERVAL = "update_interval";
        public static final String UPDATE_METHOD = "update_method";
        public static final String URI = "uri";
        public static final String USERNAME = "username";
    }

    public static class PreferredRoamingPartner {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_PREFERRED_ROAMING_PARTNER_TABLE);
        public static final String COUNTRY = "country";
        public static final String FQDN_MATCH = "fqdn_match";
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String PRIORITY = "priority";
        public static final String ROAMING_PARTNER_ID = "roaming_partner_id";
        public static final String SERVER_ROAMING_PARTNER_ID = "server_roaming_partner_id";
    }

    public static class SPExclusionList {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_SPEXCLUSION_LIST_TABLE);
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String SERVER_SP_EXCLUSION_ID = "server_spexclusion_id";
        public static final String SP_EXCLUSION_ID = "sp_exclusion_id";
        public static final String SSID = "ssid";
    }

    public static class SpIcon {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.SERVICE_PROVICER_ICON_TABLE);
        public static final String SP_HOTSPOT_ID = "hotspot_id";
        public static final String SP_ICON_HASH = "sp_icon_hash";
        public static final String SP_ICON_NAME = "sp_icon_name";
        public static final String SP_ICON_TYPE = "sp_icon_type";
    }

    public static class Tupple {
        public static final Uri CONTENT_URI = Uri.parse(WifiHs20DBStore.AUTHORITY_SLASH + WifiHs20DBStore.HOTSPOT_TUPPLE_TABLE);
        public static final String HOTSPOT_ID = "hotspot_id";
        public static final String IP_PROTOCOL = "ip_protocol";
        public static final String PORT_NUMBER = "port_number";
        public static final String SERVER_TUPPLE_ID = "server_tupple_id";
        public static final String TUPPLE_ID = "tupple_id";
    }
}
