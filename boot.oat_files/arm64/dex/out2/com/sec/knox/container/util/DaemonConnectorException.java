package com.sec.knox.container.util;

public class DaemonConnectorException extends Exception {
    private String mCmd;
    private DaemonEvent mEvent;

    public DaemonConnectorException(String detailMessage) {
        super(detailMessage);
    }

    public DaemonConnectorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DaemonConnectorException(String cmd, DaemonEvent event) {
        super("command '" + cmd + "' failed with '" + event + "'");
        this.mCmd = cmd;
        this.mEvent = event;
    }

    public int getCode() {
        return this.mEvent.getCode();
    }

    public String getCmd() {
        return this.mCmd;
    }

    public IllegalArgumentException rethrowAsParcelableException() {
        throw new IllegalStateException(getMessage(), this);
    }
}
