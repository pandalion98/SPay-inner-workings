package android.provider;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Process;
import android.os.UserHandle;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.widget.Toast;
import java.util.List;

public class ContactsInternal {
    private static final int CONTACTS_URI_LOOKUP_ID = 1000;
    private static final UriMatcher sContactsUriMatcher = new UriMatcher(-1);

    private ContactsInternal() {
    }

    static {
        sContactsUriMatcher.addURI(ContactsContract.AUTHORITY, "contacts/lookup/*/#", 1000);
    }

    public static void startQuickContactWithErrorToast(Context context, Intent intent) {
        switch (sContactsUriMatcher.match(intent.getData())) {
            case 1000:
                if (maybeStartManagedQuickContact(context, intent)) {
                    return;
                }
                break;
        }
        startQuickContactWithErrorToastForUser(context, intent, Process.myUserHandle());
    }

    public static void startQuickContactWithErrorToastForUser(Context context, Intent intent, UserHandle user) {
        try {
            context.startActivityAsUser(intent, user);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, 17039979, 0).show();
        }
    }

    private static boolean maybeStartManagedQuickContact(Context context, Intent originalIntent) {
        Uri uri = originalIntent.getData();
        List<String> pathSegments = uri.getPathSegments();
        long contactId = ContentUris.parseId(uri);
        String lookupKey = (String) pathSegments.get(2);
        if (TextUtils.isEmpty(lookupKey) || !lookupKey.startsWith(Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX)) {
            return false;
        }
        ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).startManagedQuickContact(lookupKey.substring(Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX.length()), contactId - Contacts.ENTERPRISE_CONTACT_ID_BASE, originalIntent);
        return true;
    }
}
