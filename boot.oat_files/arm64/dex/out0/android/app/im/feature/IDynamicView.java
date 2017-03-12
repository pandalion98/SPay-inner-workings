package android.app.im.feature;

import android.view.View;
import android.widget.AdapterView;

public interface IDynamicView extends IInjection {
    public static final int ON_ITEMCLICK = 1;
    public static final int ON_ITEMLONGCLICK = 2;
    public static final int ON_ITEMSELECTED = 3;
    public static final int ON_NOTHINGSELECTED = 4;

    boolean onItemClick(AdapterView<?> adapterView, View view, int i, long j);

    boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j);

    boolean onItemSelected(AdapterView<?> adapterView, View view, int i, long j);

    boolean onNavigationItemSelected(int i, long j);

    boolean onNothingSelected(AdapterView<?> adapterView);

    void onTabChanged(String str);

    void onViewCreated(View view);
}
