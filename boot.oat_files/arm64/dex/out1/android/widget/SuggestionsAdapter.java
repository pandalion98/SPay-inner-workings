package android.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentResolver.OpenResourceIdResult;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter.Delayer;
import com.android.internal.R;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.WeakHashMap;

class SuggestionsAdapter extends ResourceCursorAdapter implements OnClickListener {
    private static final boolean DBG = false;
    private static final long DELETE_KEY_POST_DELAY = 500;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed = false;
    private final int mCommitIconResId;
    private int mDefaultSpannableColor = -16736050;
    private int mFlagsCol = -1;
    private int mIconName1Col = -1;
    private int mIconName2Col = -1;
    private boolean mIsDeviceDefault = false;
    private final WeakHashMap<String, ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement = 1;
    private final SearchManager mSearchManager = ((SearchManager) this.mContext.getSystemService("search"));
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mSpannableTextColor = this.mDefaultSpannableColor;
    private int mText1Col = -1;
    private int mText2Col = -1;
    private int mText2UrlCol = -1;
    private ColorStateList mUrlColor;

    private static final class ChildViewCache {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;

        public ChildViewCache(View v) {
            this.mText1 = (TextView) v.findViewById(R.id.text1);
            this.mText2 = (TextView) v.findViewById(R.id.text2);
            this.mIcon1 = (ImageView) v.findViewById(R.id.icon1);
            this.mIcon2 = (ImageView) v.findViewById(R.id.icon2);
            this.mIconRefine = (ImageView) v.findViewById(R.id.edit_query);
        }
    }

    public SuggestionsAdapter(Context context, SearchView searchView, SearchableInfo searchable, WeakHashMap<String, ConstantState> outsideDrawablesCache) {
        super(context, searchView.getSuggestionRowLayout(), null, true);
        this.mSearchView = searchView;
        this.mSearchable = searchable;
        this.mCommitIconResId = searchView.getSuggestionCommitIconResId();
        TypedArray a = context.obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorPrimaryDark, R.attr.parentIsDeviceDefault});
        this.mIsDeviceDefault = a.getBoolean(1, false);
        this.mSpannableTextColor = a.getColor(0, this.mDefaultSpannableColor);
        this.mProviderContext = this.mSearchable.getProviderContext(this.mContext, this.mSearchable.getActivityContext(this.mContext));
        this.mOutsideDrawablesCache = outsideDrawablesCache;
        getFilter().setDelayer(new Delayer() {
            private int mPreviousLength = 0;

            public long getPostingDelay(CharSequence constraint) {
                long j = 0;
                if (constraint != null) {
                    if (constraint.length() < this.mPreviousLength) {
                        j = SuggestionsAdapter.DELETE_KEY_POST_DELAY;
                    }
                    this.mPreviousLength = constraint.length();
                }
                return j;
            }
        });
        a.recycle();
    }

    public void setQueryRefinement(int refineWhat) {
        this.mQueryRefinement = refineWhat;
    }

    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }

    public boolean hasStableIds() {
        return false;
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        String query = constraint == null ? "" : constraint.toString();
        if (this.mSearchView.getVisibility() != 0 || this.mSearchView.getWindowVisibility() != 0) {
            return null;
        }
        try {
            Cursor cursor = this.mSearchManager.getSuggestions(this.mSearchable, query, 50);
            if (cursor == null) {
                return null;
            }
            cursor.getCount();
            return cursor;
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions query threw an exception.", e);
            return null;
        }
    }

    public void close() {
        changeCursor(null);
        this.mClosed = true;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateSpinnerState(getCursor());
    }

    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        updateSpinnerState(getCursor());
    }

    private void updateSpinnerState(Cursor cursor) {
        Bundle extras = cursor != null ? cursor.getExtras() : null;
        if (extras != null && !extras.getBoolean("in_progress")) {
        }
    }

    public void changeCursor(Cursor c) {
        if (this.mClosed) {
            Log.w(LOG_TAG, "Tried to change cursor after adapter was closed.");
            if (c != null) {
                c.close();
                return;
            }
            return;
        }
        try {
            super.changeCursor(c);
            if (c != null) {
                this.mText1Col = c.getColumnIndex("suggest_text_1");
                this.mText2Col = c.getColumnIndex("suggest_text_2");
                this.mText2UrlCol = c.getColumnIndex("suggest_text_2_url");
                this.mIconName1Col = c.getColumnIndex("suggest_icon_1");
                this.mIconName2Col = c.getColumnIndex("suggest_icon_2");
                this.mFlagsCol = c.getColumnIndex("suggest_flags");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "error changing cursor and caching columns", e);
        }
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = super.newView(context, cursor, parent);
        v.setTag(new ChildViewCache(v));
        ((ImageView) v.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return v;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        int queryIndex;
        char[] lQueryForIndian;
        ChildViewCache views = (ChildViewCache) view.getTag();
        String lQuery = this.mSearchView.getQuery().toString();
        int queryLength = lQuery.length();
        boolean isURL = false;
        int flags = 0;
        if (this.mFlagsCol != -1) {
            flags = cursor.getInt(this.mFlagsCol);
        }
        if (views.mText1 != null) {
            String text1 = getStringOrNull(cursor, this.mText1Col);
            if (!this.mIsDeviceDefault || text1 == null) {
                setViewText(views.mText1, text1);
            } else {
                SpannableStringBuilder matchText1 = null;
                if (queryLength == 0) {
                    queryIndex = -1;
                } else {
                    lQueryForIndian = TextUtils.getPrefixCharForIndian(views.mText1.getPaint(), text1, lQuery.toCharArray());
                    if (lQueryForIndian != null) {
                        String s = new String(lQueryForIndian);
                        queryIndex = text1.toLowerCase().indexOf(s.toLowerCase());
                        queryLength = s.length();
                    } else {
                        queryIndex = text1.toLowerCase().indexOf(lQuery.toLowerCase());
                    }
                }
                if (queryIndex != -1 && queryLength > 0) {
                    matchText1 = new SpannableStringBuilder(text1);
                    matchText1.setSpan(new ForegroundColorSpan(this.mSpannableTextColor), queryIndex, queryIndex + queryLength, 33);
                }
                views.mText1.setContentDescription(text1);
                if (matchText1 != null) {
                    setViewText(views.mText1, matchText1);
                } else {
                    setViewText(views.mText1, text1);
                }
            }
            views.mText1.setFingerHoveredInAppWidget(false);
        }
        if (views.mText2 != null) {
            CharSequence text2 = getStringOrNull(cursor, this.mText2UrlCol);
            if (text2 != null) {
                text2 = formatUrl(context, text2);
                if (this.mIsDeviceDefault) {
                    isURL = true;
                }
            } else {
                text2 = getStringOrNull(cursor, this.mText2Col);
            }
            if (TextUtils.isEmpty(text2)) {
                if (views.mText1 != null) {
                    views.mText1.setSingleLine(false);
                    views.mText1.setMaxLines(2);
                }
            } else if (views.mText1 != null) {
                views.mText1.setSingleLine(true);
                views.mText1.setMaxLines(1);
            }
            if (!this.mIsDeviceDefault || isURL) {
                setViewText(views.mText2, text2);
            } else {
                SpannableStringBuilder matchText2 = null;
                queryIndex = -1;
                String text2_1 = getStringOrNull(cursor, this.mText2Col);
                if (queryLength == 0) {
                    queryIndex = -1;
                } else if (text2_1 != null) {
                    lQueryForIndian = TextUtils.getPrefixCharForIndian(views.mText1.getPaint(), text2_1, lQuery.toCharArray());
                    if (lQueryForIndian != null) {
                        s = new String(lQueryForIndian);
                        queryIndex = text2_1.toLowerCase().indexOf(s.toLowerCase());
                        queryLength = s.length();
                    } else {
                        queryIndex = text2_1.toLowerCase().indexOf(lQuery.toLowerCase());
                    }
                }
                if (!(queryIndex == -1 || queryLength <= 0 || text2_1 == null)) {
                    matchText2 = new SpannableStringBuilder(text2_1);
                    matchText2.setSpan(new ForegroundColorSpan(this.mSpannableTextColor), queryIndex, queryIndex + queryLength, 33);
                }
                views.mText2.setContentDescription(text2_1);
                if (matchText2 != null) {
                    setViewText(views.mText2, matchText2);
                } else if (text2_1 != null) {
                    setViewText(views.mText2, text2_1);
                } else {
                    views.mText2.setVisibility(8);
                }
            }
            views.mText2.setFingerHoveredInAppWidget(false);
        }
        if (views.mIcon1 != null) {
            if (this.mIsDeviceDefault) {
                setViewDrawable(views.mIcon1, getIcon1(cursor), 8);
            } else {
                setViewDrawable(views.mIcon1, getIcon1(cursor), 4);
            }
        }
        if (views.mIcon2 != null) {
            setViewDrawable(views.mIcon2, getIcon2(cursor), 8);
        }
        if (this.mQueryRefinement == 2 || (this.mQueryRefinement == 1 && (flags & 1) != 0)) {
            views.mIconRefine.setVisibility(0);
            if (views.mText1 != null) {
                views.mIconRefine.setTag(views.mText1.getText());
            }
            views.mIconRefine.setOnClickListener(this);
            return;
        }
        views.mIconRefine.setVisibility(8);
    }

    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence) tag);
        }
    }

    private CharSequence formatUrl(Context context, CharSequence url) {
        if (this.mUrlColor == null) {
            TypedValue colorValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.textColorSearchUrl, colorValue, true);
            this.mUrlColor = context.getColorStateList(colorValue.resourceId);
        }
        SpannableString text = new SpannableString(url);
        text.setSpan(new TextAppearanceSpan(null, 0, 0, this.mUrlColor, null), 0, url.length(), 33);
        return text;
    }

    private void setViewText(TextView v, CharSequence text) {
        v.setText(text);
        if (TextUtils.isEmpty(text)) {
            v.setVisibility(8);
        } else {
            v.setVisibility(0);
        }
    }

    private Drawable getIcon1(Cursor cursor) {
        if (this.mIconName1Col == -1) {
            return null;
        }
        Drawable drawable = getDrawableFromResourceValue(cursor.getString(this.mIconName1Col));
        return drawable == null ? getDefaultIcon1(cursor) : drawable;
    }

    private Drawable getIcon2(Cursor cursor) {
        if (this.mIconName2Col == -1) {
            return null;
        }
        return getDrawableFromResourceValue(cursor.getString(this.mIconName2Col));
    }

    private void setViewDrawable(ImageView v, Drawable drawable, int nullVisibility) {
        v.setImageDrawable(drawable);
        if (drawable == null) {
            v.setVisibility(nullVisibility);
            return;
        }
        v.setVisibility(0);
        drawable.setVisible(false, false);
        drawable.setVisible(true, false);
    }

    public CharSequence convertToString(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        CharSequence query = getColumnString(cursor, "suggest_intent_query");
        if (query != null) {
            return query;
        }
        if (this.mSearchable.shouldRewriteQueryFromData()) {
            String data = getColumnString(cursor, "suggest_intent_data");
            if (data != null) {
                return data;
            }
        }
        if (this.mSearchable.shouldRewriteQueryFromText()) {
            String text1 = getColumnString(cursor, "suggest_text_1");
            if (text1 != null) {
                return text1;
            }
        }
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        try {
            view = super.getView(position, convertView, parent);
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions cursor threw exception.", e);
            view = newView(this.mContext, this.mCursor, parent);
            if (view != null) {
                ((ChildViewCache) view.getTag()).mText1.setText(e.toString());
            }
        }
        return view;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View dropDownView;
        try {
            dropDownView = super.getDropDownView(position, convertView, parent);
        } catch (RuntimeException e) {
            Log.w(LOG_TAG, "Search suggestions cursor threw exception.", e);
            dropDownView = newDropDownView(this.mDropDownContext == null ? this.mContext : this.mDropDownContext, this.mCursor, parent);
            if (dropDownView != null) {
                ((ChildViewCache) dropDownView.getTag()).mText1.setText(e.toString());
            }
        }
        return dropDownView;
    }

    private Drawable getDrawableFromResourceValue(String drawableId) {
        Drawable drawable;
        if (drawableId == null || drawableId.length() == 0 || SmartFaceManager.PAGE_MIDDLE.equals(drawableId)) {
            return null;
        }
        try {
            int resourceId = Integer.parseInt(drawableId);
            String drawableUri = "android.resource://" + this.mProviderContext.getPackageName() + "/" + resourceId;
            drawable = checkIconCache(drawableUri);
            if (drawable != null) {
                return drawable;
            }
            drawable = this.mProviderContext.getDrawable(resourceId);
            storeInIconCache(drawableUri, drawable);
            return drawable;
        } catch (NumberFormatException e) {
            drawable = checkIconCache(drawableId);
            if (drawable != null) {
                return drawable;
            }
            drawable = getDrawable(Uri.parse(drawableId));
            storeInIconCache(drawableId, drawable);
            return drawable;
        } catch (NotFoundException e2) {
            Log.w(LOG_TAG, "Icon resource not found: " + drawableId);
            return null;
        }
    }

    private Drawable getDrawable(Uri uri) {
        InputStream stream;
        try {
            if ("android.resource".equals(uri.getScheme())) {
                OpenResourceIdResult r = this.mProviderContext.getContentResolver().getResourceId(uri);
                return r.r.getDrawable(r.id, this.mProviderContext.getTheme());
            }
            stream = this.mProviderContext.getContentResolver().openInputStream(uri);
            if (stream == null) {
                throw new FileNotFoundException("Failed to open " + uri);
            }
            Drawable createFromStream = Drawable.createFromStream(stream, null);
            try {
                stream.close();
                return createFromStream;
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Error closing icon stream for " + uri, ex);
                return createFromStream;
            }
        } catch (NotFoundException e) {
            throw new FileNotFoundException("Resource does not exist: " + uri);
        } catch (FileNotFoundException fnfe) {
            Log.w(LOG_TAG, "Icon not found: " + uri + ", " + fnfe.getMessage());
            return null;
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (IOException ex2) {
                Log.e(LOG_TAG, "Error closing icon stream for " + uri, ex2);
            }
        }
    }

    private Drawable checkIconCache(String resourceUri) {
        ConstantState cached = (ConstantState) this.mOutsideDrawablesCache.get(resourceUri);
        if (cached == null) {
            return null;
        }
        return cached.newDrawable();
    }

    private void storeInIconCache(String resourceUri, Drawable drawable) {
        if (drawable != null) {
            this.mOutsideDrawablesCache.put(resourceUri, drawable.getConstantState());
        }
    }

    private Drawable getDefaultIcon1(Cursor cursor) {
        Drawable drawable = getActivityIconWithCache(this.mSearchable.getSearchActivity());
        return drawable != null ? drawable : this.mContext.getPackageManager().getDefaultActivityIcon();
    }

    private Drawable getActivityIconWithCache(ComponentName component) {
        String componentIconKey = component.flattenToShortString();
        if (this.mOutsideDrawablesCache.containsKey(componentIconKey)) {
            ConstantState cached = (ConstantState) this.mOutsideDrawablesCache.get(componentIconKey);
            if (cached == null) {
                return null;
            }
            return cached.newDrawable(this.mProviderContext.getResources());
        }
        Drawable drawable = getActivityIcon(component);
        this.mOutsideDrawablesCache.put(componentIconKey, drawable == null ? null : drawable.getConstantState());
        return drawable;
    }

    private Drawable getActivityIcon(ComponentName component) {
        PackageManager pm = this.mContext.getPackageManager();
        try {
            ActivityInfo activityInfo = pm.getActivityInfo(component, 128);
            int iconId = activityInfo.getIconResource();
            if (iconId == 0) {
                return null;
            }
            Drawable drawable = pm.getDrawable(component.getPackageName(), iconId, activityInfo.applicationInfo);
            if (drawable != null) {
                return drawable;
            }
            Log.w(LOG_TAG, "Invalid icon resource " + iconId + " for " + component.flattenToShortString());
            return null;
        } catch (NameNotFoundException ex) {
            Log.w(LOG_TAG, ex.toString());
            return null;
        }
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return getStringOrNull(cursor, cursor.getColumnIndex(columnName));
    }

    private static String getStringOrNull(Cursor cursor, int col) {
        String str = null;
        if (col != -1) {
            try {
                str = cursor.getString(col);
            } catch (Exception e) {
                Log.e(LOG_TAG, "unexpected error retrieving valid column from cursor, did the remote process die?", e);
            }
        }
        return str;
    }
}
