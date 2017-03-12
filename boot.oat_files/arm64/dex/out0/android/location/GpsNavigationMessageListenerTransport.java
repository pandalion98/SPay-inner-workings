package android.location;

import android.content.Context;
import android.location.GpsNavigationMessageEvent.Listener;
import android.location.IGpsNavigationMessageListener.Stub;
import android.os.RemoteException;

class GpsNavigationMessageListenerTransport extends LocalListenerHelper<Listener> {
    private final IGpsNavigationMessageListener mListenerTransport = new ListenerTransport();
    private final ILocationManager mLocationManager;

    private class ListenerTransport extends Stub {
        private ListenerTransport() {
        }

        public void onGpsNavigationMessageReceived(final GpsNavigationMessageEvent event) {
            GpsNavigationMessageListenerTransport.this.foreach(new ListenerOperation<Listener>() {
                public void execute(Listener listener) throws RemoteException {
                    listener.onGpsNavigationMessageReceived(event);
                }
            });
        }

        public void onStatusChanged(final int status) {
            GpsNavigationMessageListenerTransport.this.foreach(new ListenerOperation<Listener>() {
                public void execute(Listener listener) throws RemoteException {
                    listener.onStatusChanged(status);
                }
            });
        }
    }

    public GpsNavigationMessageListenerTransport(Context context, ILocationManager locationManager) {
        super(context, "GpsNavigationMessageListenerTransport");
        this.mLocationManager = locationManager;
    }

    protected boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGpsNavigationMessageListener(this.mListenerTransport, getContext().getPackageName());
    }

    protected void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGpsNavigationMessageListener(this.mListenerTransport);
    }
}
