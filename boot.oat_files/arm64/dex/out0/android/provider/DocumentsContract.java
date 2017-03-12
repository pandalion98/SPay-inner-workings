package android.provider;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.AudioParameter;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import libcore.io.IoUtils;

public final class DocumentsContract {
    public static final String ACTION_BROWSE_DOCUMENT_ROOT = "android.provider.action.BROWSE_DOCUMENT_ROOT";
    public static final String ACTION_DOCUMENT_ROOT_SETTINGS = "android.provider.action.DOCUMENT_ROOT_SETTINGS";
    public static final String ACTION_MANAGE_DOCUMENT = "android.provider.action.MANAGE_DOCUMENT";
    public static final String ACTION_MANAGE_ROOT = "android.provider.action.MANAGE_ROOT";
    public static final String EXTRA_ERROR = "error";
    public static final String EXTRA_EXCLUDE_SELF = "android.provider.extra.EXCLUDE_SELF";
    public static final String EXTRA_INFO = "info";
    public static final String EXTRA_LOADING = "loading";
    public static final String EXTRA_ORIENTATION = "android.content.extra.ORIENTATION";
    public static final String EXTRA_PACKAGE_NAME = "android.content.extra.PACKAGE_NAME";
    public static final String EXTRA_PROMPT = "android.provider.extra.PROMPT";
    public static final String EXTRA_SHOW_ADVANCED = "android.content.extra.SHOW_ADVANCED";
    public static final String EXTRA_URI = "uri";
    public static final String METHOD_CREATE_DOCUMENT = "android:createDocument";
    public static final String METHOD_DELETE_DOCUMENT = "android:deleteDocument";
    public static final String METHOD_RENAME_DOCUMENT = "android:renameDocument";
    public static final String PACKAGE_DOCUMENTS_UI = "com.android.documentsui";
    private static final String PARAM_MANAGE = "manage";
    private static final String PARAM_QUERY = "query";
    private static final String PATH_CHILDREN = "children";
    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_RECENT = "recent";
    private static final String PATH_ROOT = "root";
    private static final String PATH_SEARCH = "search";
    private static final String PATH_TREE = "tree";
    public static final String PROVIDER_INTERFACE = "android.content.action.DOCUMENTS_PROVIDER";
    private static final String TAG = "Documents";
    private static final int THUMBNAIL_BUFFER_SIZE = 131072;

    public static final class Document {
        public static final String COLUMN_DISPLAY_NAME = "_display_name";
        public static final String COLUMN_DOCUMENT_ID = "document_id";
        public static final String COLUMN_FLAGS = "flags";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_LAST_MODIFIED = "last_modified";
        public static final String COLUMN_MIME_TYPE = "mime_type";
        public static final String COLUMN_SIZE = "_size";
        public static final String COLUMN_SUMMARY = "summary";
        public static final int FLAG_DIR_HIDE_GRID_TITLES = 65536;
        public static final int FLAG_DIR_PREFERS_GRID = 16;
        public static final int FLAG_DIR_PREFERS_LAST_MODIFIED = 32;
        public static final int FLAG_DIR_SUPPORTS_CREATE = 8;
        public static final int FLAG_SUPPORTS_DELETE = 4;
        public static final int FLAG_SUPPORTS_RENAME = 64;
        public static final int FLAG_SUPPORTS_THUMBNAIL = 1;
        public static final int FLAG_SUPPORTS_WRITE = 2;
        public static final String MIME_TYPE_DIR = "vnd.android.document/directory";

        private Document() {
        }
    }

    public static final class Root {
        public static final String COLUMN_AVAILABLE_BYTES = "available_bytes";
        public static final String COLUMN_DOCUMENT_ID = "document_id";
        public static final String COLUMN_FLAGS = "flags";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_MIME_TYPES = "mime_types";
        public static final String COLUMN_ROOT_ID = "root_id";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_TITLE = "title";
        public static final int FLAG_ADVANCED = 131072;
        public static final int FLAG_EMPTY = 65536;
        public static final int FLAG_HAS_SETTINGS = 262144;
        public static final int FLAG_LOCAL_ONLY = 2;
        public static final int FLAG_SUPPORTS_CREATE = 1;
        public static final int FLAG_SUPPORTS_IS_CHILD = 16;
        public static final int FLAG_SUPPORTS_RECENTS = 4;
        public static final int FLAG_SUPPORTS_SEARCH = 8;
        public static final String MIME_TYPE_ITEM = "vnd.android.document/root";

        private Root() {
        }
    }

    private DocumentsContract() {
    }

    public static Uri buildRootsUri(String authority) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).build();
    }

    public static Uri buildRootUri(String authority, String rootId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).appendPath(rootId).build();
    }

    public static Uri buildRecentDocumentsUri(String authority, String rootId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).appendPath(rootId).appendPath(PATH_RECENT).build();
    }

    public static Uri buildTreeDocumentUri(String authority, String documentId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_TREE).appendPath(documentId).build();
    }

    public static Uri buildDocumentUri(String authority, String documentId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_DOCUMENT).appendPath(documentId).build();
    }

    public static Uri buildDocumentUriUsingTree(Uri treeUri, String documentId) {
        return new Builder().scheme("content").authority(treeUri.getAuthority()).appendPath(PATH_TREE).appendPath(getTreeDocumentId(treeUri)).appendPath(PATH_DOCUMENT).appendPath(documentId).build();
    }

    public static Uri buildDocumentUriMaybeUsingTree(Uri baseUri, String documentId) {
        if (isTreeUri(baseUri)) {
            return buildDocumentUriUsingTree(baseUri, documentId);
        }
        return buildDocumentUri(baseUri.getAuthority(), documentId);
    }

    public static Uri buildChildDocumentsUri(String authority, String parentDocumentId) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_DOCUMENT).appendPath(parentDocumentId).appendPath(PATH_CHILDREN).build();
    }

    public static Uri buildChildDocumentsUriUsingTree(Uri treeUri, String parentDocumentId) {
        return new Builder().scheme("content").authority(treeUri.getAuthority()).appendPath(PATH_TREE).appendPath(getTreeDocumentId(treeUri)).appendPath(PATH_DOCUMENT).appendPath(parentDocumentId).appendPath(PATH_CHILDREN).build();
    }

    public static Uri buildSearchDocumentsUri(String authority, String rootId, String query) {
        return new Builder().scheme("content").authority(authority).appendPath(PATH_ROOT).appendPath(rootId).appendPath("search").appendQueryParameter("query", query).build();
    }

    public static boolean isDocumentUri(Context context, Uri uri) {
        List<String> paths = uri.getPathSegments();
        if (paths.size() == 2 && PATH_DOCUMENT.equals(paths.get(0))) {
            return isDocumentsProvider(context, uri.getAuthority());
        }
        if (paths.size() == 4 && PATH_TREE.equals(paths.get(0)) && PATH_DOCUMENT.equals(paths.get(2))) {
            return isDocumentsProvider(context, uri.getAuthority());
        }
        return false;
    }

    public static boolean isTreeUri(Uri uri) {
        List<String> paths = uri.getPathSegments();
        if (paths.size() < 2 || !PATH_TREE.equals(paths.get(0))) {
            return false;
        }
        return true;
    }

    private static boolean isDocumentsProvider(Context context, String authority) {
        for (ResolveInfo info : context.getPackageManager().queryIntentContentProviders(new Intent(PROVIDER_INTERFACE), 0)) {
            if (authority.equals(info.providerInfo.authority)) {
                return true;
            }
        }
        return false;
    }

    public static String getRootId(Uri rootUri) {
        List<String> paths = rootUri.getPathSegments();
        if (paths.size() >= 2 && PATH_ROOT.equals(paths.get(0))) {
            return (String) paths.get(1);
        }
        throw new IllegalArgumentException("Invalid URI: " + rootUri);
    }

    public static String getDocumentId(Uri documentUri) {
        List<String> paths = documentUri.getPathSegments();
        if (paths.size() >= 2 && PATH_DOCUMENT.equals(paths.get(0))) {
            return (String) paths.get(1);
        }
        if (paths.size() >= 4 && PATH_TREE.equals(paths.get(0)) && PATH_DOCUMENT.equals(paths.get(2))) {
            return (String) paths.get(3);
        }
        throw new IllegalArgumentException("Invalid URI: " + documentUri);
    }

    public static String getTreeDocumentId(Uri documentUri) {
        List<String> paths = documentUri.getPathSegments();
        if (paths.size() >= 2 && PATH_TREE.equals(paths.get(0))) {
            return (String) paths.get(1);
        }
        throw new IllegalArgumentException("Invalid URI: " + documentUri);
    }

    public static String getSearchDocumentsQuery(Uri searchDocumentsUri) {
        return searchDocumentsUri.getQueryParameter("query");
    }

    public static Uri setManageMode(Uri uri) {
        return uri.buildUpon().appendQueryParameter(PARAM_MANAGE, AudioParameter.AUDIO_PARAMETER_VALUE_true).build();
    }

    public static boolean isManageMode(Uri uri) {
        return uri.getBooleanQueryParameter(PARAM_MANAGE, false);
    }

    public static Bitmap getDocumentThumbnail(ContentResolver resolver, Uri documentUri, Point size, CancellationSignal signal) {
        ContentProviderClient client = resolver.acquireUnstableContentProviderClient(documentUri.getAuthority());
        try {
            Bitmap documentThumbnail = getDocumentThumbnail(client, documentUri, size, signal);
            ContentProviderClient.releaseQuietly(client);
            return documentThumbnail;
        } catch (Exception e) {
            if (!(e instanceof OperationCanceledException)) {
                Log.w(TAG, "Failed to load thumbnail for " + documentUri + ": " + e);
            }
            ContentProviderClient.releaseQuietly(client);
            return null;
        } catch (Throwable th) {
            ContentProviderClient.releaseQuietly(client);
        }
    }

    public static Bitmap getDocumentThumbnail(ContentProviderClient client, Uri documentUri, Point size, CancellationSignal signal) throws RemoteException, IOException {
        Bitmap bitmap;
        Bundle openOpts = new Bundle();
        openOpts.putParcelable(ContentResolver.EXTRA_SIZE, size);
        AssetFileDescriptor afd = null;
        try {
            afd = client.openTypedAssetFileDescriptor(documentUri, "image/*", openOpts, signal);
            FileDescriptor fd = afd.getFileDescriptor();
            long offset = afd.getStartOffset();
            InputStream is = null;
            Os.lseek(fd, offset, OsConstants.SEEK_SET);
        } catch (ErrnoException e) {
            InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fd), 131072);
            bufferedInputStream.mark(131072);
        } catch (Throwable th) {
            IoUtils.closeQuietly(afd);
        }
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        if (is != null) {
            BitmapFactory.decodeStream(is, null, opts);
        } else {
            BitmapFactory.decodeFileDescriptor(fd, null, opts);
        }
        int widthSample = opts.outWidth / size.x;
        int heightSample = opts.outHeight / size.y;
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = Math.min(widthSample, heightSample);
        if (is != null) {
            is.reset();
            bitmap = BitmapFactory.decodeStream(is, null, opts);
        } else {
            try {
                Os.lseek(fd, offset, OsConstants.SEEK_SET);
            } catch (ErrnoException e2) {
                e2.rethrowAsIOException();
            }
            bitmap = BitmapFactory.decodeFileDescriptor(fd, null, opts);
        }
        Bundle extras = afd.getExtras();
        int orientation = extras != null ? extras.getInt(EXTRA_ORIENTATION, 0) : 0;
        if (orientation != 0) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix m = new Matrix();
            m.setRotate((float) orientation, (float) (width / 2), (float) (height / 2));
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, false);
        }
        IoUtils.closeQuietly(afd);
        return bitmap;
    }

    public static Uri createDocument(ContentResolver resolver, Uri parentDocumentUri, String mimeType, String displayName) {
        Uri createDocument;
        ContentProviderClient client = resolver.acquireUnstableContentProviderClient(parentDocumentUri.getAuthority());
        try {
            createDocument = createDocument(client, parentDocumentUri, mimeType, displayName);
        } catch (Exception e) {
            Log.w(TAG, "Failed to create document", e);
            createDocument = null;
        } finally {
            ContentProviderClient.releaseQuietly(client);
        }
        return createDocument;
    }

    public static Uri createDocument(ContentProviderClient client, Uri parentDocumentUri, String mimeType, String displayName) throws RemoteException {
        Bundle in = new Bundle();
        in.putParcelable("uri", parentDocumentUri);
        in.putString("mime_type", mimeType);
        in.putString("_display_name", displayName);
        return (Uri) client.call(METHOD_CREATE_DOCUMENT, null, in).getParcelable("uri");
    }

    public static Uri renameDocument(ContentResolver resolver, Uri documentUri, String displayName) {
        Uri renameDocument;
        ContentProviderClient client = resolver.acquireUnstableContentProviderClient(documentUri.getAuthority());
        try {
            renameDocument = renameDocument(client, documentUri, displayName);
        } catch (Exception e) {
            Log.w(TAG, "Failed to rename document", e);
            renameDocument = null;
        } finally {
            ContentProviderClient.releaseQuietly(client);
        }
        return renameDocument;
    }

    public static Uri renameDocument(ContentProviderClient client, Uri documentUri, String displayName) throws RemoteException {
        Bundle in = new Bundle();
        in.putParcelable("uri", documentUri);
        in.putString("_display_name", displayName);
        Uri outUri = (Uri) client.call(METHOD_RENAME_DOCUMENT, null, in).getParcelable("uri");
        return outUri != null ? outUri : documentUri;
    }

    public static boolean deleteDocument(ContentResolver resolver, Uri documentUri) {
        boolean z;
        ContentProviderClient client = resolver.acquireUnstableContentProviderClient(documentUri.getAuthority());
        try {
            deleteDocument(client, documentUri);
            z = true;
        } catch (Exception e) {
            Log.w(TAG, "Failed to delete document", e);
            z = false;
        } finally {
            ContentProviderClient.releaseQuietly(client);
        }
        return z;
    }

    public static void deleteDocument(ContentProviderClient client, Uri documentUri) throws RemoteException {
        Bundle in = new Bundle();
        in.putParcelable("uri", documentUri);
        client.call(METHOD_DELETE_DOCUMENT, null, in);
    }

    public static AssetFileDescriptor openImageThumbnail(File file) throws FileNotFoundException {
        Bundle extras;
        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, 268435456);
        Bundle extras2 = null;
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)) {
                case 3:
                    extras = new Bundle(1);
                    extras.putInt(EXTRA_ORIENTATION, 180);
                    extras2 = extras;
                    break;
                case 6:
                    extras = new Bundle(1);
                    try {
                        extras.putInt(EXTRA_ORIENTATION, 90);
                        extras2 = extras;
                        break;
                    } catch (IOException e) {
                        extras2 = extras;
                        break;
                    }
                case 8:
                    extras = new Bundle(1);
                    extras.putInt(EXTRA_ORIENTATION, 270);
                    extras2 = extras;
                    break;
            }
            long[] thumb = exif.getThumbnailRange();
            if (thumb != null) {
                return new AssetFileDescriptor(pfd, thumb[0], thumb[1], extras2);
            }
        } catch (IOException e2) {
        }
        return new AssetFileDescriptor(pfd, 0, -1, extras2);
    }
}
