package android.media;

import android.media.SubtitleTrack.Cue;
import android.media.SubtitleTrack.RenderingWidget;
import java.util.Vector;

/* compiled from: ClosedCaptionRenderer */
class ClosedCaptionTrack extends SubtitleTrack {
    private final CCParser mCCParser;
    private final ClosedCaptionWidget mRenderingWidget;

    ClosedCaptionTrack(ClosedCaptionWidget renderingWidget, MediaFormat format) {
        super(format);
        this.mRenderingWidget = renderingWidget;
        this.mCCParser = new CCParser(renderingWidget);
    }

    public void onData(byte[] data, boolean eos, long runID) {
        this.mCCParser.parse(data);
    }

    public RenderingWidget getRenderingWidget() {
        return this.mRenderingWidget;
    }

    public void updateView(Vector<Cue> vector) {
    }
}
