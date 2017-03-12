package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.manager.fault.IContextAwareErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public enum SensorHubErrors implements IContextAwareErrors {
    SUCCESS("Success"),
    ERROR_UNKNOWN("ERROR : Unknown"),
    ERROR_I2C_COMM("ERROR : I2C communication"),
    ERROR_NOT_RECEIVE_ACK("ERROR : not receive ack"),
    ERROR_SENSOR_HUB_MANAGER_FAULT("ERROR : SensorHubManager fault"),
    ERROR_SENSOR_HUB_MANAGER_NULL_EXEPTION("ERROR : SensorHubManager is null"),
    ERROR_LIBRARY_PARSER_NULL_EXEPTION("ERROR : Library parser is null"),
    ERROR_CMD_PACKET_HEADER_LENGTH("ERROR : Header length of Commend packet is fault"),
    ERROR_CMD_PACKET_GENERATION_FAIL("ERROR : Commend packet is not generated"),
    ERROR_EMPTY_PARSER_MAP("ERROR : Parser map is empty"),
    ERROR_LIBRARY_PARSER_OBJECT("ERROR : Library parser object is fault"),
    ERROR_GET_SENSOR_HUB_EVENT("ERROR : SensorHub event error"),
    ERROR_NOT_REGISTERED_SERVICE("ERROR : Service is not registered"),
    ERROR_PACKET_LENGTH_ZERO("ERROR : Received packet size is zero"),
    ERROR_PACKET_LENGTH_OVERFLOW("ERROR : Received packet length is fault"),
    ERROR_PACKET_HEADER_LENGTH("ERROR : Header length of received packet is fault"),
    ERROR_INSTRUCTION_FIELD_PARSING("ERROR : Instruction field parsing error"),
    ERROR_TYPE_FIELD_PARSING("ERROR : Type field parsing error"),
    ERROR_DATA_FIELD_PARSING("ERROR : Data field parsing error"),
    ERROR_INSTRUCTION_VALUE("ERROR : Instruction value is fault"),
    ERROR_TYPE_VALUE("ERROR : Type value is fault"),
    ERROR_PACKET_LOST("ERROR : Packet is lost"),
    ERROR_EMPTY_REQUEST_LIST("ERROR : Empty request parser list"),
    ERROR_ENVIRONMENT_SENSOR_COUNT("ERROR : Environment sensor count is fault"),
    ERROR_STAYING_AREA_COUNT("ERROR : Staying area count is fault"),
    ERROR_MOVING_COUNT("ERROR : Moving count is fault"),
    ERROR_ENVIRONMENT_LOGGING_STATE("ERROR : Environment logging state error"),
    ERROR_ENVIRONMENT_PACKAGE_SIZE("ERROR : Environment package size is fault"),
    ERROR_PARSER_NOT_EXIST("ERROR : Parser is not exist"),
    ERROR_TIME_OUT_CHECK_THREAD_NULL_EXCEPTION("ERROR : Time out check thread is null"),
    ERROR_TIME_OUT_CHECK_THREAD_NOT_ALIVE("ERROR : Time out check thread isn't alive"),
    ERROR_TIME_OUT_CHECK_SERVICE_NULL_EXCEPTION("ERROR : Time out check service is null"),
    ERROR_CURRENT_POSITION_NULL_EXCEPTION("ERROR : Current position is null"),
    ERROR_BATCH_DATA_COUNT("ERROR : Batch data count is fault"),
    ERROR_MODE_FAULT("ERROR : Mode is fault"),
    ERROR_LOGGING_PACKAGE_SIZE("ERROR : Logging package size is fault"),
    ERROR_CMD_PACKET_CREATION_FAULT("ERROR : Commend packet creation is fault"),
    ERROR_TIME_OUT("ERROR : Time out");
    
    private String message;

    private SensorHubErrors(String message) {
        this.message = message;
    }

    public final int getCode() {
        return ordinal();
    }

    public final String getMessage() {
        return this.message;
    }

    public static final String getMessage(int code) {
        String msg = "";
        for (SensorHubErrors i : values()) {
            if (i.ordinal() == code) {
                msg = i.message;
                break;
            }
        }
        if (msg.isEmpty()) {
            CaLogger.error("Message code is fault");
        }
        return msg;
    }

    public void notifyFatalError() {
    }
}
