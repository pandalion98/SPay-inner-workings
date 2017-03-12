package android.support.v4.widget;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

class SearchViewCompatHoneycomb {

    interface OnQueryTextListenerCompatBridge {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    interface OnCloseListenerCompatBridge {
        boolean onClose();
    }

    /* renamed from: android.support.v4.widget.SearchViewCompatHoneycomb.1 */
    static class C00591 implements OnQueryTextListener {
        final /* synthetic */ OnQueryTextListenerCompatBridge val$listener;

        C00591(OnQueryTextListenerCompatBridge onQueryTextListenerCompatBridge) {
            this.val$listener = onQueryTextListenerCompatBridge;
        }

        public boolean onQueryTextSubmit(String str) {
            return this.val$listener.onQueryTextSubmit(str);
        }

        public boolean onQueryTextChange(String str) {
            return this.val$listener.onQueryTextChange(str);
        }
    }

    /* renamed from: android.support.v4.widget.SearchViewCompatHoneycomb.2 */
    static class C00602 implements OnCloseListener {
        final /* synthetic */ OnCloseListenerCompatBridge val$listener;

        C00602(OnCloseListenerCompatBridge onCloseListenerCompatBridge) {
            this.val$listener = onCloseListenerCompatBridge;
        }

        public boolean onClose() {
            return this.val$listener.onClose();
        }
    }

    SearchViewCompatHoneycomb() {
    }

    public static View newSearchView(Context context) {
        return new SearchView(context);
    }

    public static void setSearchableInfo(View view, ComponentName componentName) {
        SearchView searchView = (SearchView) view;
        searchView.setSearchableInfo(((SearchManager) searchView.getContext().getSystemService("search")).getSearchableInfo(componentName));
    }

    public static Object newOnQueryTextListener(OnQueryTextListenerCompatBridge onQueryTextListenerCompatBridge) {
        return new C00591(onQueryTextListenerCompatBridge);
    }

    public static void setOnQueryTextListener(Object obj, Object obj2) {
        ((SearchView) obj).setOnQueryTextListener((OnQueryTextListener) obj2);
    }

    public static Object newOnCloseListener(OnCloseListenerCompatBridge onCloseListenerCompatBridge) {
        return new C00602(onCloseListenerCompatBridge);
    }

    public static void setOnCloseListener(Object obj, Object obj2) {
        ((SearchView) obj).setOnCloseListener((OnCloseListener) obj2);
    }

    public static CharSequence getQuery(View view) {
        return ((SearchView) view).getQuery();
    }

    public static void setQuery(View view, CharSequence charSequence, boolean z) {
        ((SearchView) view).setQuery(charSequence, z);
    }

    public static void setQueryHint(View view, CharSequence charSequence) {
        ((SearchView) view).setQueryHint(charSequence);
    }

    public static void setIconified(View view, boolean z) {
        ((SearchView) view).setIconified(z);
    }

    public static boolean isIconified(View view) {
        return ((SearchView) view).isIconified();
    }

    public static void setSubmitButtonEnabled(View view, boolean z) {
        ((SearchView) view).setSubmitButtonEnabled(z);
    }

    public static boolean isSubmitButtonEnabled(View view) {
        return ((SearchView) view).isSubmitButtonEnabled();
    }

    public static void setQueryRefinementEnabled(View view, boolean z) {
        ((SearchView) view).setQueryRefinementEnabled(z);
    }

    public static boolean isQueryRefinementEnabled(View view) {
        return ((SearchView) view).isQueryRefinementEnabled();
    }

    public static void setMaxWidth(View view, int i) {
        ((SearchView) view).setMaxWidth(i);
    }
}
