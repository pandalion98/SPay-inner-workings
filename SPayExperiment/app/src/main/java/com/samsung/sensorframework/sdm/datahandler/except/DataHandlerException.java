/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.sensorframework.sdm.datahandler.except;

public class DataHandlerException
extends Exception {
    private static final long serialVersionUID = 8240175615135197888L;
    private final int errorCode;

    public String getMessage() {
        switch (this.errorCode) {
            default: {
                return super.getMessage();
            }
            case 10: {
                return "Unknown config key.";
            }
            case 11: {
                return "Missing URL target.";
            }
            case 12: {
                return "I/O Error!";
            }
            case 13: {
                return "This feature is unimplemented";
            }
            case 14: {
                return "Error: attempting to write to default directory.";
            }
            case 15: {
                return "Conflict in config values!";
            }
            case 16: {
                return "Failure posting data to server.";
            }
            case 17: 
        }
        return "Permission denied, check for required permissions.";
    }
}

