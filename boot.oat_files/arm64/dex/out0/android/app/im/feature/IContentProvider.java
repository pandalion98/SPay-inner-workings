package android.app.im.feature;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public interface IContentProvider extends IInjection {
    Cursor query(Uri uri, String str, ContentResolver contentResolver, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5);
}
