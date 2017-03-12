package android.app;

public interface BarBeamCommand {
    void addListener(BarBeamListener barBeamListener);

    boolean isImplementationCompatible() throws Exception;

    void removeListener(BarBeamListener barBeamListener);

    void setBarcode(byte[] bArr) throws BarBeamException;

    void setHopSequence(Hop[] hopArr) throws BarBeamException;

    void startBeaming() throws BarBeamException;

    void startBeaming(int i) throws BarBeamException;

    void stopBeaming() throws BarBeamException;
}
