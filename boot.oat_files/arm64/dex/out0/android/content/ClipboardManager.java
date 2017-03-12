package android.content;

import android.content.IOnPrimaryClipChangedListener.Stub;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ServiceManager;
import android.sec.clipboard.IClipboardService;
import android.sec.clipboard.data.ClipboardData;
import android.sec.clipboard.data.list.ClipboardDataHtml;
import android.sec.clipboard.data.list.ClipboardDataIntent;
import android.sec.clipboard.data.list.ClipboardDataText;
import android.sec.clipboard.data.list.ClipboardDataUri;
import android.util.Log;
import java.util.ArrayList;

public class ClipboardManager extends android.text.ClipboardManager {
    static final int MSG_REPORT_PRIMARY_CLIP_CHANGED = 1;
    private static final String TAG = "ClipboardManager";
    private static IClipboard sService;
    private static IClipboardService sServiceEx;
    private static final Object sStaticLock = new Object();
    private final Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ClipboardManager.this.reportPrimaryClipChanged();
                    return;
                default:
                    return;
            }
        }
    };
    private final ArrayList<OnPrimaryClipChangedListener> mPrimaryClipChangedListeners = new ArrayList();
    private final Stub mPrimaryClipChangedServiceListener = new Stub() {
        public void dispatchPrimaryClipChanged() {
            ClipboardManager.this.mHandler.sendEmptyMessage(1);
        }
    };

    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged();
    }

    private static IClipboard getService() {
        IClipboard iClipboard;
        synchronized (sStaticLock) {
            if (sService != null) {
                iClipboard = sService;
            } else {
                sService = IClipboard.Stub.asInterface(ServiceManager.getService(Context.CLIPBOARD_SERVICE));
                iClipboard = sService;
            }
        }
        return iClipboard;
    }

    private static IClipboardService getServiceEx() {
        if (sServiceEx != null) {
            return sServiceEx;
        }
        sServiceEx = IClipboardService.Stub.asInterface(ServiceManager.getService(Context.CLIPBOARDEX_SERVICE));
        return sServiceEx;
    }

    public ClipboardManager(Context context, Handler handler) {
        this.mContext = context;
    }

    public void setPrimaryClip(ClipData clip) {
        if (clip != null) {
            try {
                clip.prepareToLeaveProcess();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        getService().setPrimaryClip(clip, this.mContext.getOpPackageName());
        if (getServiceEx() != null && clip != null && clip.getDescription().getMimeTypeCount() > 0) {
            ClipboardDataText txt;
            if (clip.getDescription().getMimeType(0).compareTo(ClipDescription.MIMETYPE_TEXT_PLAIN) == 0) {
                txt = new ClipboardDataText();
                if (clip.getItemCount() <= 0) {
                    return;
                }
                if (txt != null) {
                    txt.setText(clip.getItemAt(0).getText());
                    sServiceEx.setData(txt.getFormat(), txt, true);
                    return;
                }
                Log.w(TAG, "In ClipboardManager...sServiceEx : " + sServiceEx + ", txt : " + txt);
            } else if (clip.getDescription().getMimeType(0).compareTo(ClipDescription.MIMETYPE_TEXT_HTML) == 0) {
                ClipboardDataHtml html = new ClipboardDataHtml();
                if (clip.getItemCount() <= 0) {
                    return;
                }
                if (html != null) {
                    html.setHtmlInternal(clip.getItemAt(0).getText(), clip.getItemAt(0).getHtmlText());
                    sServiceEx.setData(html.getFormat(), html, true);
                    return;
                }
                Log.w(TAG, "In ClipboardManager...sServiceEx :" + sServiceEx + ", html :" + html);
            } else if (clip.getDescription().getMimeType(0).compareTo(ClipDescription.MIMETYPE_TEXT_URILIST) == 0) {
                ClipboardDataUri uriData = new ClipboardDataUri();
                if (clip.getItemCount() > 0) {
                    Uri uri = clip.getItemAt(0).getUri();
                    if (uriData != null) {
                        uriData.setUri(uri);
                        sServiceEx.setData(uriData.getFormat(), uriData, true);
                        return;
                    }
                    Log.w(TAG, "In ClipboardManager...sServiceEx :" + sServiceEx + ", uri :" + uri);
                }
            } else if (clip.getDescription().getMimeType(0).compareTo(ClipDescription.MIMETYPE_TEXT_INTENT) == 0) {
                ClipboardDataIntent intent = new ClipboardDataIntent();
                if (clip.getItemCount() <= 0) {
                    return;
                }
                if (intent != null) {
                    intent.setIntent(clip.getItemAt(0).getIntent());
                    sServiceEx.setData(intent.getFormat(), intent, true);
                    return;
                }
                Log.w(TAG, "In ClipboardManager...sServiceEx :" + sServiceEx + ", intent :" + intent);
            } else {
                CharSequence data = clip.getItemAt(0).coerceToText(this.mContext);
                txt = new ClipboardDataText();
                if (data != null && data.length() > 0 && txt != null) {
                    if (txt != null) {
                        txt.setText(data);
                        sServiceEx.setData(txt.getFormat(), txt, true);
                        return;
                    }
                    Log.w(TAG, "In ClipboardManager...sServiceEx :" + sServiceEx + ", txt :" + txt);
                }
            }
        }
    }

    public ClipData getPrimaryClip() {
        try {
            if (getServiceEx() == null) {
                return getService().getPrimaryClip(this.mContext.getOpPackageName());
            }
            if (getServiceEx().getDataSize() <= 0) {
                Log.w(TAG, "clipboardEx has no item.");
                return null;
            } else if (!getService().isSEContainerAndIsolated(this.mContext.getOpPackageName())) {
                return getService().getPrimaryClip(this.mContext.getOpPackageName());
            } else {
                ClipboardData cbData = sServiceEx.GetClipboardData(1);
                if (cbData != null) {
                    return cbData.getClipData();
                }
                Log.w(TAG, "getPrimaryClip, cbData is null");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ClipDescription getPrimaryClipDescription() {
        ClipDescription clipDescription = null;
        try {
            if (getServiceEx() == null || getServiceEx().getDataSize() > 0) {
                clipDescription = getService().getPrimaryClipDescription(this.mContext.getOpPackageName());
                return clipDescription;
            }
            Log.w(TAG, "clipboardEx has no item.");
            return clipDescription;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasPrimaryClip() {
        boolean z = false;
        try {
            if (getServiceEx() == null || getServiceEx().getDataSize() > 0) {
                z = getService().hasPrimaryClip(this.mContext.getOpPackageName());
                return z;
            }
            Log.w(TAG, "clipboardEx has no item.");
            return z;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        synchronized (this.mPrimaryClipChangedListeners) {
            if (this.mPrimaryClipChangedListeners.size() == 0) {
                try {
                    getService().addPrimaryClipChangedListener(this.mPrimaryClipChangedServiceListener, this.mContext.getOpPackageName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mPrimaryClipChangedListeners.add(what);
        }
    }

    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        synchronized (this.mPrimaryClipChangedListeners) {
            this.mPrimaryClipChangedListeners.remove(what);
            if (this.mPrimaryClipChangedListeners.size() == 0) {
                try {
                    getService().removePrimaryClipChangedListener(this.mPrimaryClipChangedServiceListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CharSequence getText() {
        ClipData clip = getPrimaryClip();
        if (clip == null || clip.getItemCount() <= 0) {
            return null;
        }
        return clip.getItemAt(0).coerceToText(this.mContext);
    }

    public void setText(CharSequence text) {
        setPrimaryClip(ClipData.newPlainText(null, text));
    }

    public boolean hasText() {
        boolean z = false;
        try {
            if (getServiceEx() == null || getServiceEx().getDataSize() > 0) {
                z = getService().hasClipboardText(this.mContext.getOpPackageName());
                return z;
            }
            Log.w(TAG, "clipboardEx has no item.");
            return z;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void reportPrimaryClipChanged() {
        /*
        r5 = this;
        r4 = r5.mPrimaryClipChangedListeners;
        monitor-enter(r4);
        r3 = r5.mPrimaryClipChangedListeners;	 Catch:{ all -> 0x0022 }
        r0 = r3.size();	 Catch:{ all -> 0x0022 }
        if (r0 > 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r4);	 Catch:{ all -> 0x0022 }
    L_0x000c:
        return;
    L_0x000d:
        r3 = r5.mPrimaryClipChangedListeners;	 Catch:{ all -> 0x0022 }
        r2 = r3.toArray();	 Catch:{ all -> 0x0022 }
        monitor-exit(r4);	 Catch:{ all -> 0x0022 }
        r1 = 0;
    L_0x0015:
        r3 = r2.length;
        if (r1 >= r3) goto L_0x000c;
    L_0x0018:
        r3 = r2[r1];
        r3 = (android.content.ClipboardManager.OnPrimaryClipChangedListener) r3;
        r3.onPrimaryClipChanged();
        r1 = r1 + 1;
        goto L_0x0015;
    L_0x0022:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0022 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.ClipboardManager.reportPrimaryClipChanged():void");
    }
}
