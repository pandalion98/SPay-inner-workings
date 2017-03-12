package android.provider;

import android.Manifest.permission;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract.Root;
import android.util.Log;
import java.io.FileNotFoundException;
import java.util.Objects;
import libcore.io.IoUtils;

public abstract class DocumentsProvider extends ContentProvider {
    private static final int MATCH_CHILDREN = 6;
    private static final int MATCH_CHILDREN_TREE = 8;
    private static final int MATCH_DOCUMENT = 5;
    private static final int MATCH_DOCUMENT_TREE = 7;
    private static final int MATCH_RECENT = 3;
    private static final int MATCH_ROOT = 2;
    private static final int MATCH_ROOTS = 1;
    private static final int MATCH_SEARCH = 4;
    private static final String TAG = "DocumentsProvider";
    private String mAuthority;
    private UriMatcher mMatcher;

    public abstract ParcelFileDescriptor openDocument(String str, String str2, CancellationSignal cancellationSignal) throws FileNotFoundException;

    public abstract Cursor queryChildDocuments(String str, String[] strArr, String str2) throws FileNotFoundException;

    public abstract Cursor queryDocument(String str, String[] strArr) throws FileNotFoundException;

    public abstract Cursor queryRoots(String[] strArr) throws FileNotFoundException;

    public void attachInfo(Context context, ProviderInfo info) {
        this.mAuthority = info.authority;
        this.mMatcher = new UriMatcher(-1);
        this.mMatcher.addURI(this.mAuthority, "root", 1);
        this.mMatcher.addURI(this.mAuthority, "root/*", 2);
        this.mMatcher.addURI(this.mAuthority, "root/*/recent", 3);
        this.mMatcher.addURI(this.mAuthority, "root/*/search", 4);
        this.mMatcher.addURI(this.mAuthority, "document/*", 5);
        this.mMatcher.addURI(this.mAuthority, "document/*/children", 6);
        this.mMatcher.addURI(this.mAuthority, "tree/*/document/*", 7);
        this.mMatcher.addURI(this.mAuthority, "tree/*/document/*/children", 8);
        if (!info.exported) {
            throw new SecurityException("Provider must be exported");
        } else if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grantUriPermissions");
        } else if (permission.MANAGE_DOCUMENTS.equals(info.readPermission) && permission.MANAGE_DOCUMENTS.equals(info.writePermission)) {
            super.attachInfo(context, info);
        } else {
            throw new SecurityException("Provider must be protected by MANAGE_DOCUMENTS");
        }
    }

    public boolean isChildDocument(String parentDocumentId, String documentId) {
        return false;
    }

    private void enforceTree(Uri documentUri) {
        if (DocumentsContract.isTreeUri(documentUri)) {
            String parent = DocumentsContract.getTreeDocumentId(documentUri);
            String child = DocumentsContract.getDocumentId(documentUri);
            if (!Objects.equals(parent, child) && !isChildDocument(parent, child)) {
                throw new SecurityException("Document " + child + " is not a descendant of " + parent);
            }
        }
    }

    public String createDocument(String parentDocumentId, String mimeType, String displayName) throws FileNotFoundException {
        throw new UnsupportedOperationException("Create not supported");
    }

    public String renameDocument(String documentId, String displayName) throws FileNotFoundException {
        throw new UnsupportedOperationException("Rename not supported");
    }

    public void deleteDocument(String documentId) throws FileNotFoundException {
        throw new UnsupportedOperationException("Delete not supported");
    }

    public Cursor queryRecentDocuments(String rootId, String[] projection) throws FileNotFoundException {
        throw new UnsupportedOperationException("Recent not supported");
    }

    public Cursor queryChildDocumentsForManage(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        throw new UnsupportedOperationException("Manage not supported");
    }

    public Cursor querySearchDocuments(String rootId, String query, String[] projection) throws FileNotFoundException {
        throw new UnsupportedOperationException("Search not supported");
    }

    public String getDocumentType(String documentId) throws FileNotFoundException {
        String str = null;
        Cursor cursor = queryDocument(documentId, null);
        try {
            if (cursor.moveToFirst()) {
                str = cursor.getString(cursor.getColumnIndexOrThrow("mime_type"));
            } else {
                IoUtils.closeQuietly(cursor);
            }
            return str;
        } finally {
            IoUtils.closeQuietly(cursor);
        }
    }

    public AssetFileDescriptor openDocumentThumbnail(String documentId, Point sizeHint, CancellationSignal signal) throws FileNotFoundException {
        throw new UnsupportedOperationException("Thumbnails not supported");
    }

    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            switch (this.mMatcher.match(uri)) {
                case 1:
                    return queryRoots(projection);
                case 3:
                    return queryRecentDocuments(DocumentsContract.getRootId(uri), projection);
                case 4:
                    return querySearchDocuments(DocumentsContract.getRootId(uri), DocumentsContract.getSearchDocumentsQuery(uri), projection);
                case 5:
                case 7:
                    enforceTree(uri);
                    return queryDocument(DocumentsContract.getDocumentId(uri), projection);
                case 6:
                case 8:
                    enforceTree(uri);
                    if (DocumentsContract.isManageMode(uri)) {
                        return queryChildDocumentsForManage(DocumentsContract.getDocumentId(uri), projection, sortOrder);
                    }
                    return queryChildDocuments(DocumentsContract.getDocumentId(uri), projection, sortOrder);
                default:
                    throw new UnsupportedOperationException("Unsupported Uri " + uri);
            }
        } catch (FileNotFoundException e) {
            Log.w(TAG, "Failed during query", e);
            return null;
        }
        Log.w(TAG, "Failed during query", e);
        return null;
    }

    public final String getType(Uri uri) {
        try {
            switch (this.mMatcher.match(uri)) {
                case 2:
                    return Root.MIME_TYPE_ITEM;
                case 5:
                case 7:
                    enforceTree(uri);
                    return getDocumentType(DocumentsContract.getDocumentId(uri));
                default:
                    return null;
            }
        } catch (FileNotFoundException e) {
            Log.w(TAG, "Failed during getType", e);
            return null;
        }
    }

    public Uri canonicalize(Uri uri) {
        Context context = getContext();
        switch (this.mMatcher.match(uri)) {
            case 7:
                enforceTree(uri);
                Uri narrowUri = DocumentsContract.buildDocumentUri(uri.getAuthority(), DocumentsContract.getDocumentId(uri));
                context.grantUriPermission(getCallingPackage(), narrowUri, getCallingOrSelfUriPermissionModeFlags(context, uri));
                return narrowUri;
            default:
                return null;
        }
    }

    private static int getCallingOrSelfUriPermissionModeFlags(Context context, Uri uri) {
        int modeFlags = 0;
        if (context.checkCallingOrSelfUriPermission(uri, 1) == 0) {
            modeFlags = 0 | 1;
        }
        if (context.checkCallingOrSelfUriPermission(uri, 2) == 0) {
            modeFlags |= 2;
        }
        if (context.checkCallingOrSelfUriPermission(uri, 65) == 0) {
            return modeFlags | 64;
        }
        return modeFlags;
    }

    public final Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Insert not supported");
    }

    public final int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete not supported");
    }

    public final int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Update not supported");
    }

    public Bundle call(String method, String arg, Bundle extras) {
        if (!method.startsWith("android:")) {
            return super.call(method, arg, extras);
        }
        Context context = getContext();
        Uri documentUri = (Uri) extras.getParcelable("uri");
        String authority = documentUri.getAuthority();
        String documentId = DocumentsContract.getDocumentId(documentUri);
        if (this.mAuthority.equals(authority)) {
            enforceTree(documentUri);
            Bundle out = new Bundle();
            try {
                if (DocumentsContract.METHOD_CREATE_DOCUMENT.equals(method)) {
                    enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                    String str = "uri";
                    out.putParcelable(str, DocumentsContract.buildDocumentUriMaybeUsingTree(documentUri, createDocument(documentId, extras.getString("mime_type"), extras.getString("_display_name"))));
                    return out;
                } else if (DocumentsContract.METHOD_RENAME_DOCUMENT.equals(method)) {
                    enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                    String newDocumentId = renameDocument(documentId, extras.getString("_display_name"));
                    if (newDocumentId == null) {
                        return out;
                    }
                    Uri newDocumentUri = DocumentsContract.buildDocumentUriMaybeUsingTree(documentUri, newDocumentId);
                    if (!DocumentsContract.isTreeUri(newDocumentUri)) {
                        context.grantUriPermission(getCallingPackage(), newDocumentUri, getCallingOrSelfUriPermissionModeFlags(context, documentUri));
                    }
                    out.putParcelable("uri", newDocumentUri);
                    revokeDocumentPermission(documentId);
                    return out;
                } else if (DocumentsContract.METHOD_DELETE_DOCUMENT.equals(method)) {
                    enforceWritePermissionInner(documentUri, getCallingPackage(), null);
                    deleteDocument(documentId);
                    revokeDocumentPermission(documentId);
                    return out;
                } else {
                    throw new UnsupportedOperationException("Method not supported " + method);
                }
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Failed call " + method, e);
            }
        }
        throw new SecurityException("Requested authority " + authority + " doesn't match provider " + this.mAuthority);
    }

    public final void revokeDocumentPermission(String documentId) {
        Context context = getContext();
        context.revokeUriPermission(DocumentsContract.buildDocumentUri(this.mAuthority, documentId), -1);
        context.revokeUriPermission(DocumentsContract.buildTreeDocumentUri(this.mAuthority, documentId), -1);
    }

    public final ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        enforceTree(uri);
        return openDocument(DocumentsContract.getDocumentId(uri), mode, null);
    }

    public final ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        enforceTree(uri);
        return openDocument(DocumentsContract.getDocumentId(uri), mode, signal);
    }

    public final AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        enforceTree(uri);
        ParcelFileDescriptor fd = openDocument(DocumentsContract.getDocumentId(uri), mode, null);
        if (fd != null) {
            return new AssetFileDescriptor(fd, 0, -1);
        }
        return null;
    }

    public final AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        enforceTree(uri);
        ParcelFileDescriptor fd = openDocument(DocumentsContract.getDocumentId(uri), mode, signal);
        return fd != null ? new AssetFileDescriptor(fd, 0, -1) : null;
    }

    public final AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts) throws FileNotFoundException {
        enforceTree(uri);
        if (opts == null || !opts.containsKey(ContentResolver.EXTRA_SIZE)) {
            return super.openTypedAssetFile(uri, mimeTypeFilter, opts);
        }
        return openDocumentThumbnail(DocumentsContract.getDocumentId(uri), (Point) opts.getParcelable(ContentResolver.EXTRA_SIZE), null);
    }

    public final AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws FileNotFoundException {
        enforceTree(uri);
        if (opts == null || !opts.containsKey(ContentResolver.EXTRA_SIZE)) {
            return super.openTypedAssetFile(uri, mimeTypeFilter, opts, signal);
        }
        return openDocumentThumbnail(DocumentsContract.getDocumentId(uri), (Point) opts.getParcelable(ContentResolver.EXTRA_SIZE), signal);
    }
}
