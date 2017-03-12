package android.app;

import android.app.IBarBeamListener.Stub;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;

public class BarBeamCommandImpl implements BarBeamCommand {
    private static final int hopSize = 7;
    private final String TAG = "BarBeamCommandImpl";
    private final IBarBeamService mService;
    private final ArrayList<BarBeamListenerDelegate> sListenerDelegates = new ArrayList();

    private class BarBeamListenerDelegate extends Stub {
        private final BarBeamListener mListener;

        BarBeamListenerDelegate(BarBeamListener listener) {
            this.mListener = listener;
        }

        public BarBeamListener getBarBeamListener() {
            return this.mListener;
        }

        public String getListenerInfo() {
            return this.mListener.toString();
        }

        public void onBeamingStoppped() {
            this.mListener.onBeamingStoppped();
        }

        public void onBeamingStarted() {
            this.mListener.onBeamingStarted();
        }
    }

    protected BarBeamCommandImpl(IBarBeamService service) {
        this.mService = service;
        Log.d("BarBeamCommandImpl", "BarBeamCommandImpl" + this.mService);
    }

    public void addListener(BarBeamListener listener) {
        if (listener != null) {
            synchronized (this.sListenerDelegates) {
                int size = this.sListenerDelegates.size();
                for (int i = 0; i < size; i++) {
                    if (((BarBeamListenerDelegate) this.sListenerDelegates.get(i)).getBarBeamListener() == listener) {
                        Log.d("BarBeamCommandImpl", "  .addListener : fail. already registered / listener count = " + this.sListenerDelegates.size() + ", listener=" + listener);
                        return;
                    }
                }
                BarBeamListenerDelegate l = new BarBeamListenerDelegate(listener);
                this.sListenerDelegates.add(l);
                try {
                    this.mService.addListener(l);
                } catch (RemoteException e) {
                    Log.e("BarBeamCommandImpl", "RemoteException in registerListener: ", e);
                }
                Log.v("BarBeamCommandImpl", "  .addListener : success. listener count = " + size + "->" + this.sListenerDelegates.size() + ", listener=" + listener);
            }
        }
    }

    public void removeListener(BarBeamListener listener) {
        synchronized (this.sListenerDelegates) {
            int i;
            int size = this.sListenerDelegates.size();
            for (i = 0; i < size; i++) {
                BarBeamListenerDelegate barBeamListenerDelegate = (BarBeamListenerDelegate) this.sListenerDelegates.get(i);
            }
            i = 0;
            while (i < size) {
                BarBeamListenerDelegate l = (BarBeamListenerDelegate) this.sListenerDelegates.get(i);
                if (l.getBarBeamListener() == listener) {
                    this.sListenerDelegates.remove(i);
                    try {
                        this.mService.removeListener(l);
                        break;
                    } catch (RemoteException e) {
                        Log.e("BarBeamCommandImpl", "RemoteException in removeListener : ", e);
                    }
                } else {
                    i++;
                }
            }
            Log.i("BarBeamCommandImpl", "  .removeListener : / listener count = " + size + "->" + this.sListenerDelegates.size() + ", listener=" + listener);
        }
    }

    public void startBeaming() throws BarBeamException {
        Log.d("BarBeamCommandImpl", "startBeaming");
        try {
            this.mService.startBeaming();
        } catch (RemoteException e) {
            throw new BarBeamException("RemoteException in startBarBeam: ", e);
        }
    }

    public void startBeaming(int repeat) throws BarBeamException {
        Log.d("BarBeamCommandImpl", "startBeaming");
        try {
            this.mService.startBeaming_repeat(repeat);
        } catch (RemoteException e) {
            throw new BarBeamException("RemoteException in startBeaming(repeat): ", e);
        }
    }

    public void stopBeaming() throws BarBeamException {
        Log.d("BarBeamCommandImpl", "stopBeaming");
        try {
            this.mService.stopBeaming();
        } catch (RemoteException e) {
            throw new BarBeamException("RemoteException in stopBarBeam(repeat): ", e);
        }
    }

    protected boolean getCurrentStatus() throws BarBeamException {
        try {
            return this.mService.getCurrentStatus();
        } catch (RemoteException e) {
            throw new BarBeamException("RemoteException in getCurrentStatus(): ", e);
        }
    }

    protected int writeHop2ByteArray(Hop hop, byte[] buf, int offset) {
        int i = offset + 1;
        buf[offset] = (byte) hop.barWidth;
        offset = i + 1;
        buf[i] = (byte) hop.symbolCnt;
        i = offset + 1;
        buf[offset] = (byte) (hop.interSymbolDelay >>> 8);
        offset = i + 1;
        buf[i] = (byte) hop.interSymbolDelay;
        i = offset + 1;
        buf[offset] = (byte) hop.packetCnt;
        offset = i + 1;
        buf[i] = (byte) (hop.interPacketDelay >>> 8);
        i = offset + 1;
        buf[offset] = (byte) hop.interPacketDelay;
        return 7;
    }

    public void setHopSequence(Hop[] hopSequence) throws BarBeamException {
        byte[] tempbuffer = new byte[(hopSequence.length * 7)];
        for (int i = 0; i < hopSequence.length; i++) {
            writeHop2ByteArray(hopSequence[i], tempbuffer, i * 7);
        }
        Log.d("BarBeamCommandImpl", "sendHopSequenceTable " + hopSequence.length);
        try {
            this.mService.setHopSequence(tempbuffer, hopSequence.length, 7);
        } catch (RemoteException e) {
            throw new BarBeamException("RemoteException in sendHopSequenceTable: ", e);
        }
    }

    public void setBarcode(byte[] barcode) throws BarBeamException {
        Log.d("BarBeamCommandImpl", "sendDataTable");
        try {
            this.mService.setBarcode(barcode);
        } catch (RemoteException e) {
            throw new BarBeamException("RemoteException in setBarcode: ", e);
        }
    }

    public boolean isImplementationCompatible() throws Exception {
        try {
            return this.mService.isImplementationCompatible();
        } catch (RemoteException e) {
            throw new BarBeamException("isImplementationCompatible in setBarcode: ", e);
        }
    }
}
