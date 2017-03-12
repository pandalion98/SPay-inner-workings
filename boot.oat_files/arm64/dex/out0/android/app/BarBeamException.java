package android.app;

public class BarBeamException extends Exception {
    private static final long serialVersionUID = 1;

    public BarBeamException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BarBeamException(String detailMessage) {
        super(detailMessage);
    }

    public BarBeamException(Throwable throwable) {
        super(throwable);
    }
}
