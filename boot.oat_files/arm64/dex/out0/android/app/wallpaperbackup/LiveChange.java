package android.app.wallpaperbackup;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public class LiveChange {
    public Context mContext;

    LiveChange(Context context) {
        this.mContext = context;
    }

    public void set(String mComponent) {
        String[] classpackage = mComponent.split("/");
        Intent queryIntent = new Intent("android.service.wallpaper.WallpaperService");
        queryIntent.setPackage(classpackage[0]);
        List<ResolveInfo> list = this.mContext.getPackageManager().queryIntentServices(queryIntent, 128);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ResolveInfo ri = (ResolveInfo) list.get(i);
                if (ri.serviceInfo.name.equals(classpackage[1])) {
                    try {
                        WallpaperInfo info = new WallpaperInfo(this.mContext, ri);
                        Intent intent = new Intent("android.service.wallpaper.WallpaperService");
                        intent.setClassName(info.getPackageName(), info.getServiceName());
                        new LivePreview(this.mContext).set(0, intent, info, false);
                        return;
                    } catch (XmlPullParserException e) {
                        ((Activity) this.mContext).finish();
                        return;
                    } catch (IOException e2) {
                        ((Activity) this.mContext).finish();
                        return;
                    }
                }
            }
        }
    }
}
