package android.location;

import android.content.Context;
import android.location.GpsMeasurementsEvent.Listener;
import android.location.IGpsMeasurementsListener.Stub;
import android.os.RemoteException;

class GpsMeasurementListenerTransport extends LocalListenerHelper<Listener> {
    private final IGpsMeasurementsListener mListenerTransport = new ListenerTransport();
    private final ILocationManager mLocationManager;

    private class ListenerTransport extends Stub {
        private ListenerTransport() {
        }

        public void onGpsMeasurementsReceived(final GpsMeasurementsEvent event) {
            GpsMeasurementListenerTransport.this.foreach(new ListenerOperation<Listener>() {
                public void execute(Listener listener) throws RemoteException {
                    listener.onGpsMeasurementsReceived(event);
                }
            });
        }

        public void onStatusChanged(final int status) {
            GpsMeasurementListenerTransport.this.foreach(new ListenerOperation<Listener>() {
                public void execute(Listener listener) throws RemoteException {
                    listener.onStatusChanged(status);
                }
            });
        }
    }

    public GpsMeasurementListenerTransport(Context context, ILocationManager locationManager) {
        super(context, "GpsMeasurementListenerTransport");
        this.mLocationManager = locationManager;
    }

    protected boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGpsMeasurementsListener(this.mListenerTransport, getContext().getPackageName());
    }

    protected void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGpsMeasurementsListener(this.mListenerTransport);
    }
}
