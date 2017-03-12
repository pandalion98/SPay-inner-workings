package android.media;

import android.media.IMediaHTTPService.Stub;
import android.os.IBinder;

public class MediaHTTPService extends Stub {
    private static final String TAG = "MediaHTTPService";

    public IMediaHTTPConnection makeHTTPConnection() {
        return new MediaHTTPConnection();
    }

    static IBinder createHttpServiceBinderIfNecessary(String path) {
        if (path.startsWith("http://") || path.startsWith("https://") || path.startsWith("sshttp://") || path.startsWith("slhttp://") || path.startsWith("ntcl://") || path.startsWith("groupplay://") || path.startsWith("widevine://") || path.endsWith(".sdp") || path.endsWith(".SDP")) {
            return new MediaHTTPService().asBinder();
        }
        return null;
    }
}
