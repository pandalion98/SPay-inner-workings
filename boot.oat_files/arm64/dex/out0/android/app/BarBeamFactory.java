package android.app;

import android.content.Context;
import android.util.Log;

public class BarBeamFactory {
    private static final String TAG = "BarBeamFactory";
    private BarBeamCommand mbarbeamcmd = null;

    public BarBeamFactory(Context context) throws BarBeamException {
        try {
            this.mbarbeamcmd = getBarBeamCommand(context);
        } catch (BarBeamException e) {
            e.printStackTrace();
            throw new BarBeamException("BarBeamException in getBarBeam :", e);
        }
    }

    public BarBeamFactory(Context context, Hop[] HopSequencearray) throws BarBeamException {
        try {
            this.mbarbeamcmd = getBarBeamCommand(context);
            if (this.mbarbeamcmd != null) {
                try {
                    this.mbarbeamcmd.setHopSequence(HopSequencearray);
                } catch (BarBeamException e) {
                    e.printStackTrace();
                    throw new BarBeamException("BarBeamException in setHopSequence :", e);
                }
            }
        } catch (BarBeamException e2) {
            e2.printStackTrace();
            throw new BarBeamException("BarBeamException in getBarBeam :", e2);
        }
    }

    protected BarBeamCommand getBarBeamCommand(Context context) throws BarBeamException {
        if (context == null) {
            throw new BarBeamException("Context is null");
        }
        try {
            BarBeamCommand m = (BarBeamCommand) context.getSystemService(Context.BARBEAM_SERVICE);
            if (m.isImplementationCompatible()) {
                return m;
            }
        } catch (Throwable e) {
            Log.d(TAG, "Failed loading MobeamLED implementation: ", e);
        }
        throw new BarBeamException("No implementaion found.");
    }

    public void setHopSequence(Hop[] HopSequence) throws BarBeamException {
        if (this.mbarbeamcmd != null) {
            try {
                this.mbarbeamcmd.setHopSequence(HopSequence);
            } catch (BarBeamException e) {
                e.printStackTrace();
                throw new BarBeamException("BarBeamException in setHopSequence :", e);
            }
        }
    }

    public boolean StartBarBeamFactory(byte[] barcode) throws BarBeamException {
        try {
            this.mbarbeamcmd.setBarcode(barcode);
            this.mbarbeamcmd.startBeaming();
            for (int i = 0; i < 5; i++) {
                if (((BarBeamCommandImpl) this.mbarbeamcmd).getCurrentStatus()) {
                    return true;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            return false;
        } catch (Throwable e2) {
            Log.d(TAG, "Failed loading MobeamLED implementation: ", e2);
            BarBeamException barBeamException = new BarBeamException("BarBeamException in setHopSequence :", e2);
        }
    }

    public boolean StopBarBeamFactory() throws BarBeamException {
        try {
            this.mbarbeamcmd.stopBeaming();
            for (int i = 0; i < 5; i++) {
                if (!((BarBeamCommandImpl) this.mbarbeamcmd).getCurrentStatus()) {
                    return true;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            return false;
        } catch (Throwable e2) {
            Log.d(TAG, "BarBeamException in getCurrentStatus: ", e2);
            BarBeamException barBeamException = new BarBeamException("BarBeamException in getCurrentStatus :", e2);
        }
    }
}
