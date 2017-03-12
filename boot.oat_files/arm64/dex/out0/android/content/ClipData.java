package android.content;

import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClipData implements Parcelable {
    public static final Creator<ClipData> CREATOR = new Creator<ClipData>() {
        public ClipData createFromParcel(Parcel source) {
            return new ClipData(source);
        }

        public ClipData[] newArray(int size) {
            return new ClipData[size];
        }
    };
    static final String[] MIMETYPES_TEXT_HTML = new String[]{ClipDescription.MIMETYPE_TEXT_HTML};
    static final String[] MIMETYPES_TEXT_INTENT = new String[]{ClipDescription.MIMETYPE_TEXT_INTENT};
    static final String[] MIMETYPES_TEXT_PLAIN = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
    static final String[] MIMETYPES_TEXT_URILIST = new String[]{ClipDescription.MIMETYPE_TEXT_URILIST};
    final ClipDescription mClipDescription;
    final Bitmap mIcon;
    final ArrayList<Item> mItems;

    public static class Item {
        final String mHtmlText;
        final Intent mIntent;
        final CharSequence mText;
        Uri mUri;

        public Item(CharSequence text) {
            this.mText = text;
            this.mHtmlText = null;
            this.mIntent = null;
            this.mUri = null;
        }

        public Item(CharSequence text, String htmlText) {
            this.mText = text;
            this.mHtmlText = htmlText;
            this.mIntent = null;
            this.mUri = null;
        }

        public Item(Intent intent) {
            this.mText = null;
            this.mHtmlText = null;
            this.mIntent = intent;
            this.mUri = null;
        }

        public Item(Uri uri) {
            this.mText = null;
            this.mHtmlText = null;
            this.mIntent = null;
            this.mUri = uri;
        }

        public Item(CharSequence text, Intent intent, Uri uri) {
            this.mText = text;
            this.mHtmlText = null;
            this.mIntent = intent;
            this.mUri = uri;
        }

        public Item(CharSequence text, String htmlText, Intent intent, Uri uri) {
            if (htmlText == null || text != null) {
                this.mText = text;
                this.mHtmlText = htmlText;
                this.mIntent = intent;
                this.mUri = uri;
                return;
            }
            throw new IllegalArgumentException("Plain text must be supplied if HTML text is supplied");
        }

        public CharSequence getText() {
            return this.mText;
        }

        public String getHtmlText() {
            return this.mHtmlText;
        }

        public Intent getIntent() {
            return this.mIntent;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public CharSequence coerceToText(Context context) {
            CharSequence text = getText();
            if (text != null) {
                return text;
            }
            Uri uri = getUri();
            if (uri != null) {
                FileInputStream stream = null;
                try {
                    stream = context.getContentResolver().openTypedAssetFileDescriptor(uri, "text/*", null).createInputStream();
                    InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                    StringBuilder builder = new StringBuilder(128);
                    char[] buffer = new char[8192];
                    while (true) {
                        int len = reader.read(buffer);
                        if (len <= 0) {
                            break;
                        }
                        builder.append(buffer, 0, len);
                    }
                    text = builder.toString();
                    if (stream == null) {
                        return text;
                    }
                    try {
                        stream.close();
                        return text;
                    } catch (IOException e) {
                        return text;
                    }
                } catch (FileNotFoundException e2) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e3) {
                        }
                    }
                    return uri.toString();
                } catch (IOException e4) {
                    Log.w("ClippedData", "Failure loading text", e4);
                    text = e4.toString();
                    if (stream == null) {
                        return text;
                    }
                    try {
                        stream.close();
                        return text;
                    } catch (IOException e5) {
                        return text;
                    }
                } catch (Throwable th) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e6) {
                        }
                    }
                }
            } else {
                Intent intent = getIntent();
                if (intent != null) {
                    return intent.toUri(1);
                }
                return ProxyInfo.LOCAL_EXCL_LIST;
            }
        }

        public CharSequence coerceToStyledText(Context context) {
            CharSequence text = getText();
            if (text instanceof Spanned) {
                return text;
            }
            String htmlText = getHtmlText();
            if (htmlText != null) {
                try {
                    CharSequence newText = Html.fromHtml(htmlText);
                    if (newText != null) {
                        return newText;
                    }
                } catch (RuntimeException e) {
                }
            }
            return text == null ? coerceToHtmlOrStyledText(context, true) : text;
        }

        public CharSequence coerceToStyledTextForEditor(Context context) {
            CharSequence text = getText();
            if (text instanceof Spanned) {
                return text;
            }
            String htmlText = getHtmlText();
            if (htmlText != null) {
                try {
                    CharSequence newText = Html.fromHtml(htmlText);
                    if (newText != null) {
                        return newText;
                    }
                } catch (RuntimeException e) {
                }
            }
            return text == null ? null : text;
        }

        public String coerceToHtmlText(Context context) {
            String htmlText = getHtmlText();
            if (htmlText != null) {
                return htmlText;
            }
            CharSequence text = getText();
            if (text == null) {
                text = coerceToHtmlOrStyledText(context, false);
                return text != null ? text.toString() : null;
            } else if (text instanceof Spanned) {
                return Html.toHtml((Spanned) text);
            } else {
                return Html.escapeHtml(text);
            }
        }

        private CharSequence coerceToHtmlOrStyledText(Context context, boolean styled) {
            CharSequence text;
            if (this.mUri != null) {
                String[] types = context.getContentResolver().getStreamTypes(this.mUri, "text/*");
                boolean hasHtml = false;
                boolean hasText = false;
                if (types != null) {
                    for (String type : types) {
                        if (ClipDescription.MIMETYPE_TEXT_HTML.equals(type)) {
                            hasHtml = true;
                        } else if (type.startsWith("text/")) {
                            hasText = true;
                        }
                    }
                }
                if (hasHtml || hasText) {
                    FileInputStream stream = null;
                    try {
                        stream = context.getContentResolver().openTypedAssetFileDescriptor(this.mUri, hasHtml ? ClipDescription.MIMETYPE_TEXT_HTML : ClipDescription.MIMETYPE_TEXT_PLAIN, null).createInputStream();
                        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                        StringBuilder builder = new StringBuilder(128);
                        char[] buffer = new char[8192];
                        while (true) {
                            int len = reader.read(buffer);
                            if (len <= 0) {
                                break;
                            }
                            builder.append(buffer, 0, len);
                        }
                        text = builder.toString();
                        if (hasHtml) {
                            if (styled) {
                                try {
                                    CharSequence newText = Html.fromHtml(text);
                                    if (newText == null) {
                                        newText = text;
                                    }
                                    if (stream != null) {
                                        try {
                                            stream.close();
                                        } catch (IOException e) {
                                        }
                                    }
                                    return newText;
                                } catch (RuntimeException e2) {
                                    if (stream == null) {
                                        return text;
                                    }
                                    try {
                                        stream.close();
                                        return text;
                                    } catch (IOException e3) {
                                        return text;
                                    }
                                }
                            }
                            text = text.toString();
                            if (stream == null) {
                                return text;
                            }
                            try {
                                stream.close();
                                return text;
                            } catch (IOException e4) {
                                return text;
                            }
                        } else if (!styled) {
                            text = Html.escapeHtml(text);
                            if (stream == null) {
                                return text;
                            }
                            try {
                                stream.close();
                                return text;
                            } catch (IOException e5) {
                                return text;
                            }
                        } else if (stream == null) {
                            return text;
                        } else {
                            try {
                                stream.close();
                                return text;
                            } catch (IOException e6) {
                                return text;
                            }
                        }
                    } catch (FileNotFoundException e7) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e8) {
                            }
                        }
                    } catch (IOException e9) {
                        Log.w("ClippedData", "Failure loading text", e9);
                        text = Html.escapeHtml(e9.toString());
                        if (stream == null) {
                            return text;
                        }
                        try {
                            stream.close();
                            return text;
                        } catch (IOException e10) {
                            return text;
                        }
                    } catch (Throwable th) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e11) {
                            }
                        }
                    }
                }
                if (styled) {
                    return uriToStyledText(this.mUri.toString());
                }
                return uriToHtml(this.mUri.toString());
            } else if (this.mIntent == null) {
                return ProxyInfo.LOCAL_EXCL_LIST;
            } else {
                if (styled) {
                    return uriToStyledText(this.mIntent.toUri(1));
                }
                return uriToHtml(this.mIntent.toUri(1));
            }
        }

        private String uriToHtml(String uri) {
            StringBuilder builder = new StringBuilder(256);
            builder.append("<a href=\"");
            builder.append(Html.escapeHtml(uri));
            builder.append("\">");
            builder.append(Html.escapeHtml(uri));
            builder.append("</a>");
            return builder.toString();
        }

        private CharSequence uriToStyledText(String uri) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(uri);
            builder.setSpan(new URLSpan(uri), 0, builder.length(), 33);
            return builder;
        }

        public String toString() {
            StringBuilder b = new StringBuilder(128);
            b.append("ClipData.Item { ");
            toShortString(b);
            b.append(" }");
            return b.toString();
        }

        public void toShortString(StringBuilder b) {
            if (this.mHtmlText != null) {
                b.append("H:");
                b.append(this.mHtmlText);
            } else if (this.mText != null) {
                b.append("T:");
                b.append(this.mText);
            } else if (this.mUri != null) {
                b.append("U:");
                b.append(this.mUri);
            } else if (this.mIntent != null) {
                b.append("I:");
                this.mIntent.toShortString(b, true, true, true, true);
            } else {
                b.append(WifiEnterpriseConfig.EMPTY_VALUE);
            }
        }

        public void toShortSummaryString(StringBuilder b) {
            if (this.mHtmlText != null) {
                b.append("HTML");
            } else if (this.mText != null) {
                b.append("TEXT");
            } else if (this.mUri != null) {
                b.append("U:");
                b.append(this.mUri);
            } else if (this.mIntent != null) {
                b.append("I:");
                this.mIntent.toShortString(b, true, true, true, true);
            } else {
                b.append(WifiEnterpriseConfig.EMPTY_VALUE);
            }
        }
    }

    public ClipData(CharSequence label, String[] mimeTypes, Item item) {
        this.mClipDescription = new ClipDescription(label, mimeTypes);
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        this.mIcon = null;
        this.mItems = new ArrayList();
        this.mItems.add(item);
    }

    public ClipData(ClipDescription description, Item item) {
        this.mClipDescription = description;
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        this.mIcon = null;
        this.mItems = new ArrayList();
        this.mItems.add(item);
    }

    public ClipData(ClipData other) {
        this.mClipDescription = other.mClipDescription;
        this.mIcon = other.mIcon;
        this.mItems = new ArrayList(other.mItems);
    }

    public static ClipData newPlainText(CharSequence label, CharSequence text) {
        return new ClipData(label, MIMETYPES_TEXT_PLAIN, new Item(text));
    }

    public static ClipData newHtmlText(CharSequence label, CharSequence text, String htmlText) {
        return new ClipData(label, MIMETYPES_TEXT_HTML, new Item(text, htmlText));
    }

    public static ClipData newIntent(CharSequence label, Intent intent) {
        return new ClipData(label, MIMETYPES_TEXT_INTENT, new Item(intent));
    }

    public static ClipData newUri(ContentResolver resolver, CharSequence label, Uri uri) {
        int i = 2;
        Item item = new Item(uri);
        String[] mimeTypes = null;
        if ("content".equals(uri.getScheme())) {
            String realType = resolver.getType(uri);
            mimeTypes = resolver.getStreamTypes(uri, "*/*");
            if (mimeTypes != null) {
                int length = mimeTypes.length;
                if (realType == null) {
                    i = 1;
                }
                String[] tmp = new String[(i + length)];
                int i2 = 0;
                if (realType != null) {
                    tmp[0] = realType;
                    i2 = 0 + 1;
                }
                System.arraycopy(mimeTypes, 0, tmp, i2, mimeTypes.length);
                tmp[mimeTypes.length + i2] = ClipDescription.MIMETYPE_TEXT_URILIST;
                mimeTypes = tmp;
            } else if (realType != null) {
                mimeTypes = new String[]{realType, ClipDescription.MIMETYPE_TEXT_URILIST};
            }
        }
        if (mimeTypes == null) {
            mimeTypes = MIMETYPES_TEXT_URILIST;
        }
        return new ClipData(label, mimeTypes, item);
    }

    public static ClipData newRawUri(CharSequence label, Uri uri) {
        return new ClipData(label, MIMETYPES_TEXT_URILIST, new Item(uri));
    }

    public ClipDescription getDescription() {
        return this.mClipDescription;
    }

    public void addItem(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        this.mItems.add(item);
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public Item getItemAt(int index) {
        return (Item) this.mItems.get(index);
    }

    public void prepareToLeaveProcess() {
        int size = this.mItems.size();
        for (int i = 0; i < size; i++) {
            Item item = (Item) this.mItems.get(i);
            if (item.mIntent != null) {
                item.mIntent.prepareToLeaveProcess();
            }
            if (item.mUri != null && StrictMode.vmFileUriExposureEnabled()) {
                item.mUri.checkFileUriExposed("ClipData.Item.getUri()");
            }
        }
    }

    public void fixUris(int contentUserHint) {
        int size = this.mItems.size();
        for (int i = 0; i < size; i++) {
            Item item = (Item) this.mItems.get(i);
            if (item.mIntent != null) {
                item.mIntent.fixUris(contentUserHint);
            }
            if (item.mUri != null) {
                item.mUri = ContentProvider.maybeAddUserId(item.mUri, contentUserHint);
            }
        }
    }

    public void fixUrisLight(int contentUserHint) {
        int size = this.mItems.size();
        for (int i = 0; i < size; i++) {
            Item item = (Item) this.mItems.get(i);
            if (item.mIntent != null) {
                Uri data = item.mIntent.getData();
                if (data != null) {
                    item.mIntent.setData(ContentProvider.maybeAddUserId(data, contentUserHint));
                }
            }
            if (item.mUri != null) {
                item.mUri = ContentProvider.maybeAddUserId(item.mUri, contentUserHint);
            }
        }
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        b.append("ClipData { ");
        toShortString(b);
        b.append(" }");
        return b.toString();
    }

    public void toShortString(StringBuilder b) {
        boolean first = this.mClipDescription != null ? !this.mClipDescription.toShortString(b) : true;
        if (this.mIcon != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("I:");
            b.append(this.mIcon.getWidth());
            b.append('x');
            b.append(this.mIcon.getHeight());
        }
        for (int i = 0; i < this.mItems.size(); i++) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append('{');
            ((Item) this.mItems.get(i)).toShortString(b);
            b.append('}');
        }
    }

    public void toShortStringShortItems(StringBuilder b, boolean first) {
        if (this.mItems.size() > 0) {
            if (!first) {
                b.append(' ');
            }
            ((Item) this.mItems.get(0)).toShortString(b);
            if (this.mItems.size() > 1) {
                b.append(" ...");
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mClipDescription.writeToParcel(dest, flags);
        if (this.mIcon != null) {
            dest.writeInt(1);
            this.mIcon.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        int N = this.mItems.size();
        dest.writeInt(N);
        for (int i = 0; i < N; i++) {
            Item item = (Item) this.mItems.get(i);
            TextUtils.writeToParcel(item.mText, dest, flags);
            dest.writeString(item.mHtmlText);
            if (item.mIntent != null) {
                dest.writeInt(1);
                item.mIntent.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            if (item.mUri != null) {
                dest.writeInt(1);
                item.mUri.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
        }
    }

    ClipData(Parcel in) {
        this.mClipDescription = new ClipDescription(in);
        if (in.readInt() != 0) {
            this.mIcon = (Bitmap) Bitmap.CREATOR.createFromParcel(in);
        } else {
            this.mIcon = null;
        }
        this.mItems = new ArrayList();
        int N = in.readInt();
        for (int i = 0; i < N; i++) {
            Intent intent;
            Uri uri;
            CharSequence text = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            String htmlText = in.readString();
            if (in.readInt() != 0) {
                intent = (Intent) Intent.CREATOR.createFromParcel(in);
            } else {
                intent = null;
            }
            if (in.readInt() != 0) {
                uri = (Uri) Uri.CREATOR.createFromParcel(in);
            } else {
                uri = null;
            }
            this.mItems.add(new Item(text, htmlText, intent, uri));
        }
    }

    public boolean checkEquals(ClipData clip) {
        if (clip == null || !(clip instanceof ClipData) || clip.getDescription().getMimeTypeCount() <= 0 || getItemCount() <= 0 || clip.getItemCount() <= 0) {
            return false;
        }
        String type = clip.getDescription().getMimeType(0);
        if (type.compareTo(ClipDescription.MIMETYPE_TEXT_PLAIN) == 0) {
            return getItemAt(0).getText().toString().compareTo(clip.getItemAt(0).getText().toString()) == 0;
        } else {
            if (type.compareTo(ClipDescription.MIMETYPE_TEXT_URILIST) == 0) {
                return getItemAt(0).getUri().toString().compareTo(clip.getItemAt(0).getUri().toString()) == 0;
            } else if (type.compareTo(ClipDescription.MIMETYPE_TEXT_INTENT) == 0) {
                return getItemAt(0).getIntent().toUri(1).compareTo(clip.getItemAt(0).getIntent().toUri(1)) == 0;
            } else if (type.compareTo(ClipDescription.MIMETYPE_TEXT_HTML) != 0) {
                return false;
            } else {
                return getItemAt(0).getHtmlText().compareTo(clip.getItemAt(0).getHtmlText()) == 0;
            }
        }
    }
}
