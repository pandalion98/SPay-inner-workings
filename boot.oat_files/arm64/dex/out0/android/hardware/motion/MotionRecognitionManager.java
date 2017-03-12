package android.hardware.motion;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.samsung.android.motion.MREvent;
import com.samsung.android.motion.MRListener;
import java.util.ArrayList;

@Deprecated
public class MotionRecognitionManager extends com.samsung.android.motion.MotionRecognitionManager {
    private final ArrayList<ListenerDelegate> sListeners = new ArrayList();

    private static class ListenerDelegate {
        private final MRListener mListener;
        private final MRListener moldListener;

        private ListenerDelegate(MRListener listener) {
            this.mListener = new MRListener() {
                public void onMotionListener(MREvent motionEvent) {
                    MREvent event = new MREvent();
                    event.setMotion(motionEvent.getMotion());
                    event.setTilt(motionEvent.getTilt());
                    event.setPanningDx(motionEvent.getPanningDx());
                    event.setPanningDy(motionEvent.getPanningDy());
                    event.setPanningDz(motionEvent.getPanningDz());
                    event.setPanningDxImage(motionEvent.getPanningDxImage());
                    event.setPanningDyImage(motionEvent.getPanningDyImage());
                    event.setPanningDzImage(motionEvent.getPanningDzImage());
                    ListenerDelegate.this.moldListener.onMotionListener(event);
                }
            };
            this.moldListener = listener;
        }
    }

    public MotionRecognitionManager(Looper mainLooper) {
        super(mainLooper);
    }

    @Deprecated
    public void registerListenerEvent(MRListener listener, int motion_events) {
        registerListenerEvent(listener, motion_events, null);
    }

    @Deprecated
    public void registerListenerEvent(MRListener listener, int motion_events, Handler handler) {
        registerListenerEvent(listener, 0, motion_events, handler);
    }

    @Deprecated
    public void registerListenerEvent(MRListener listener, int motion_sensors, int motion_events, Handler handler) {
        synchronized (this.sListeners) {
            int size = this.sListeners.size();
            for (int i = 0; i < size; i++) {
                if (((ListenerDelegate) this.sListeners.get(i)).moldListener == listener) {
                    Log.d("MotionRecognitionManager", "registerListener " + i);
                    return;
                }
            }
            ListenerDelegate l = new ListenerDelegate(listener);
            this.sListeners.add(l);
            super.registerListenerEvent(l.mListener, motion_sensors, motion_events, handler);
        }
    }

    @Deprecated
    public void unregisterListener(MRListener listener, int motion_events) {
        unregisterListener(listener);
    }

    @Deprecated
    public void unregisterListener(MRListener listener) {
        synchronized (this.sListeners) {
            int size = this.sListeners.size();
            ListenerDelegate l = null;
            for (int i = 0; i < size; i++) {
                l = (ListenerDelegate) this.sListeners.get(i);
                if (l.moldListener == listener) {
                    Log.d("MotionRecognitionManager", "unregisterListener " + i);
                    break;
                }
            }
            if (l != null) {
                this.sListeners.remove(l);
                super.unregisterListener(l.mListener);
            }
        }
    }

    @Deprecated
    public void useMotionAlways(MRListener listener, boolean bUseAlways) {
    }

    @Deprecated
    public void setMotionAngle(MRListener listener, int status) {
    }

    @Deprecated
    public void setSmartMotionAngle(MRListener listener, int status) {
        synchronized (this.sListeners) {
            int size = this.sListeners.size();
            for (int i = 0; i < size; i++) {
                ListenerDelegate l = (ListenerDelegate) this.sListeners.get(i);
                if (l.moldListener == listener) {
                    super.setSmartMotionAngle(l.mListener, status);
                    return;
                }
            }
        }
    }
}
