/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.util;

public interface ErrorConstants {
    public static final String[] ACTIVATE_TOKEN_MESSAGE_FAILURE;
    public static final String[] ACTIVATE_TOKEN_MESSAGE_SUCCESS;
    public static final String[] ACTIVATE_TOKEN_MESSAGE_TA_COMM_FAILURE;
    public static final String[] ACTIVATE_TOKEN_MESSAGE_TA_FAILURE;
    public static final String[] BUSINESS_DGI_DATA_PARSE_ERROR;
    public static final String[] DELETE_TOKEN_MESSAGE_FAILURE;
    public static final String[] DELETE_TOKEN_MESSAGE_SUCCESS;
    public static final String[] DELETE_TOKEN_MESSAGE_TA_COMM_FAILURE;
    public static final String[] DELETE_TOKEN_MESSAGE_TA_FAILURE;
    public static final String[] END_TRANS_FAILURE;
    public static final String[] END_TRANS_PARTIAL_SUCCESS;
    public static final String[] END_TRANS_SESSION_FAILURE;
    public static final String[] END_TRANS_SUCCESS;
    public static final String[] END_TRANS_TAP2PAY_FAILURE;
    public static final String[] END_TRANS_TA_COMM_FAILURE;
    public static final String[] END_TRANS_TA_FAILURE;
    public static final String[] GENERATE_APDU_RESPONSE;
    public static final String[] GET_CLIENT_VERSION_SUCCESS;
    public static final String[] GET_TOKEN_DATA_MESSAGE_FAILURE;
    public static final String[] GET_TOKEN_DATA_MESSAGE_SUCCESS;
    public static final String[] GET_TOKEN_DATA_MESSAGE_TA_COMM_FAILURE;
    public static final String[] GET_TOKEN_DATA_MESSAGE_TA_FAILURE;
    public static final String[] GET_TOKEN_STATUS_MESSAGE_FAILURE;
    public static final String[] GET_TOKEN_STATUS_MESSAGE_SUCCESS;
    public static final String[] GET_TOKEN_STATUS_MESSAGE_TA_COMM_FAILURE;
    public static final String[] GET_TOKEN_STATUS_MESSAGE_TA_FAILURE;
    public static final String[] GET_TOKEN_VERSION_MESSAGE_FAILURE;
    public static final String[] GET_TOKEN_VERSION_MESSAGE_SUCCESS;
    public static final String[] GET_TOKEN_VERSION_MESSAGE_TA_COMM_FAILURE;
    public static final String[] GET_TOKEN_VERSION_MESSAGE_TA_FAILURE;
    public static final String[] PROCESS_IN_APP_FAILURE;
    public static final String[] PROCESS_IN_APP_RESPONSE;
    public static final String[] PROCESS_IN_APP_TA_COMM_FAILURE;
    public static final String[] PROCESS_IN_APP_TA_FAILURE;
    public static final String[] PROCESS_OTHER_FAILURE;
    public static final String[] PROCESS_OTHER_RESPONSE;
    public static final String[] PROCESS_OTHER_TA_COMM_FAILURE;
    public static final String[] PROCESS_OTHER_TA_FAILURE;
    public static final String[] RESUME_TOKEN_MESSAGE_FAILURE;
    public static final String[] RESUME_TOKEN_MESSAGE_SUCCESS;
    public static final String[] RESUME_TOKEN_MESSAGE_TA_COMM_FAILURE;
    public static final String[] RESUME_TOKEN_MESSAGE_TA_FAILURE;
    public static final String[] START_TRANS_CLIENT_VERSION_FAILURE;
    public static final String[] START_TRANS_GENERIC_FAILURE;
    public static final String[] START_TRANS_PARSE_FAILURE;
    public static final String[] START_TRANS_PARTIAL_TOKEN_DATA_MALFORMED_FAILURE;
    public static final String[] START_TRANS_SUCCESS;
    public static final String[] START_TRANS_TA_COMM_FAILURE;
    public static final String[] START_TRANS_TA_FAILURE;
    public static final String[] START_TRANS_TOKEN_DATA_MALFORMED_FAILURE;
    public static final String[] START_TRANS_TOKEN_VERSION_FAILURE;
    public static final String[] SUSPEND_TOKEN_MESSAGE_FAILURE;
    public static final String[] SUSPEND_TOKEN_MESSAGE_SUCCESS;
    public static final String[] SUSPEND_TOKEN_MESSAGE_TA_COMM_FAILURE;
    public static final String[] SUSPEND_TOKEN_MESSAGE_TA_FAILURE;
    public static final String[] UPDATE_DETAIL_MESSAGE_FAILURE;
    public static final String[] UPDATE_DETAIL_MESSAGE_SUCCESS;
    public static final String[] UPDATE_DETAIL_MESSAGE_TA_COMM_FAILURE;
    public static final String[] UPDATE_DETAIL_MESSAGE_TA_FAILURE;

    static {
        UPDATE_DETAIL_MESSAGE_SUCCESS = new String[]{"00", "10101", "Success: Token data updated successfully"};
        UPDATE_DETAIL_MESSAGE_FAILURE = new String[]{"01", "10101", "Failure: Token data updated failed"};
        UPDATE_DETAIL_MESSAGE_TA_COMM_FAILURE = new String[]{"01", "10102", "Failure: TA communication failure "};
        UPDATE_DETAIL_MESSAGE_TA_FAILURE = new String[]{"01", "10103", "Failure: TA failure "};
        DELETE_TOKEN_MESSAGE_SUCCESS = new String[]{"00", "10201", "Success: Token data deleted successfully"};
        DELETE_TOKEN_MESSAGE_FAILURE = new String[]{"05", "10201", "Failure: Token data delete failed"};
        DELETE_TOKEN_MESSAGE_TA_COMM_FAILURE = new String[]{"05", "10202", "Failure: TA communication failure "};
        DELETE_TOKEN_MESSAGE_TA_FAILURE = new String[]{"05", "10203", "Failure: TA failure "};
        ACTIVATE_TOKEN_MESSAGE_SUCCESS = new String[]{"00", "10301", "Success: Token activated successfully"};
        ACTIVATE_TOKEN_MESSAGE_FAILURE = new String[]{"05", "10301", "Failure: Token activation failed"};
        ACTIVATE_TOKEN_MESSAGE_TA_COMM_FAILURE = new String[]{"05", "10302", "Failure: TA communication failure "};
        ACTIVATE_TOKEN_MESSAGE_TA_FAILURE = new String[]{"05", "10303", "Failure: TA failure "};
        SUSPEND_TOKEN_MESSAGE_SUCCESS = new String[]{"00", "10401", "Success: Token suspended successfully"};
        SUSPEND_TOKEN_MESSAGE_FAILURE = new String[]{"05", "10401", "Failure: Token suspend failed"};
        SUSPEND_TOKEN_MESSAGE_TA_COMM_FAILURE = new String[]{"05", "10402", "Failure: TA communication failure "};
        SUSPEND_TOKEN_MESSAGE_TA_FAILURE = new String[]{"05", "10403", "Failure: TA failure "};
        RESUME_TOKEN_MESSAGE_SUCCESS = new String[]{"00", "10501", "Success: Token resumed successfully"};
        RESUME_TOKEN_MESSAGE_FAILURE = new String[]{"05", "10501", "Failure: Token resume failed"};
        RESUME_TOKEN_MESSAGE_TA_COMM_FAILURE = new String[]{"05", "10502", "Failure: TA communication failure "};
        RESUME_TOKEN_MESSAGE_TA_FAILURE = new String[]{"05", "10503", "Failure: TA failure "};
        GET_TOKEN_DATA_MESSAGE_SUCCESS = new String[]{"00", "10601", "Success: Token data fetched successful"};
        GET_TOKEN_DATA_MESSAGE_FAILURE = new String[]{"06", "10601", "Failure: Token data fetch failed"};
        GET_TOKEN_DATA_MESSAGE_TA_COMM_FAILURE = new String[]{"06", "10602", "Failure: TA communication failure "};
        GET_TOKEN_DATA_MESSAGE_TA_FAILURE = new String[]{"06", "10603", "Failure: TA failure "};
        GET_TOKEN_STATUS_MESSAGE_SUCCESS = new String[]{"00", "10701", "Success: Token data status fetch successful"};
        GET_TOKEN_STATUS_MESSAGE_FAILURE = new String[]{"06", "10701", "Failure: Token data status fetch failed"};
        GET_TOKEN_STATUS_MESSAGE_TA_COMM_FAILURE = new String[]{"06", "10702", "Failure: TA communication failure "};
        GET_TOKEN_STATUS_MESSAGE_TA_FAILURE = new String[]{"06", "10703", "Failure: TA failure "};
        GET_TOKEN_VERSION_MESSAGE_SUCCESS = new String[]{"00", "10801", "Success: Token data version fetch successful"};
        GET_TOKEN_VERSION_MESSAGE_FAILURE = new String[]{"06", "10801", "Failure: Token data version fetch failed"};
        GET_TOKEN_VERSION_MESSAGE_TA_COMM_FAILURE = new String[]{"06", "10802", "Failure: TA communication failure "};
        GET_TOKEN_VERSION_MESSAGE_TA_FAILURE = new String[]{"06", "10803", "Failure: TA failure "};
        GET_CLIENT_VERSION_SUCCESS = new String[]{"00", "10101", "Success"};
        START_TRANS_SUCCESS = new String[]{"00", "10801", "Success: Ready to tap and pay"};
        START_TRANS_GENERIC_FAILURE = new String[]{"03", "10801", "Failure: Token data storage failed"};
        START_TRANS_PARSE_FAILURE = new String[]{"01", "10801", "Failure: Token data cannot be parsed"};
        START_TRANS_PARTIAL_TOKEN_DATA_MALFORMED_FAILURE = START_TRANS_PARSE_FAILURE;
        START_TRANS_TOKEN_DATA_MALFORMED_FAILURE = START_TRANS_PARSE_FAILURE;
        START_TRANS_TOKEN_VERSION_FAILURE = new String[]{"01", "10802", "Failure: Refresh token data required"};
        START_TRANS_CLIENT_VERSION_FAILURE = new String[]{"02", "10801", "Failure: Client update required"};
        START_TRANS_TA_COMM_FAILURE = new String[]{"03", "10802", "Failure: TA communication failure "};
        START_TRANS_TA_FAILURE = new String[]{"03", "10803", "Failure: TA failure "};
        GENERATE_APDU_RESPONSE = new String[]{"00", "10901", "Success: Response APDU generated successfully"};
        PROCESS_OTHER_RESPONSE = new String[]{"00", "11001", "Success: Process other successful"};
        PROCESS_OTHER_FAILURE = new String[]{"03", "11001", "Failure: Process Other"};
        PROCESS_OTHER_TA_COMM_FAILURE = new String[]{"03", "11002", "Failure: TA communication failure "};
        PROCESS_OTHER_TA_FAILURE = new String[]{"03", "11003", "Failure: TA failure "};
        PROCESS_IN_APP_RESPONSE = new String[]{"00", "11301", "Success: processInAppPayment successful"};
        PROCESS_IN_APP_FAILURE = new String[]{"03", "11301", "Failure: processInAppPayment"};
        PROCESS_IN_APP_TA_COMM_FAILURE = new String[]{"03", "11302", "Failure: TA communication failure "};
        PROCESS_IN_APP_TA_FAILURE = new String[]{"03", "11303", "Failure: TA failure "};
        END_TRANS_SUCCESS = new String[]{"00", "11201", "Success: endTransaction successful"};
        END_TRANS_PARTIAL_SUCCESS = new String[]{"00", "11202", "Success: endTransaction successful"};
        END_TRANS_FAILURE = new String[]{"00", "11203", "Failure: Token Data Storage failed"};
        END_TRANS_TAP2PAY_FAILURE = new String[]{"03", "11201", "Failure: Transaction failed"};
        END_TRANS_SESSION_FAILURE = new String[]{"04", "11201", "Failure: Session not available"};
        END_TRANS_TA_COMM_FAILURE = new String[]{"00", "11204", "Failure: TA communication failure "};
        END_TRANS_TA_FAILURE = new String[]{"00", "11205", "Failure: TA failure "};
        BUSINESS_DGI_DATA_PARSE_ERROR = new String[]{"601", "601", "Failure: Improper DGI data"};
    }
}

